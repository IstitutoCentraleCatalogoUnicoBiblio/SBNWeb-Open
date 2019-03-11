/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.persistence.dao.acquisizioni;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.CalcoliVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RigheVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;


public class Calcola extends AcquisizioniBaseDAO {

	public static final int FLAG_ORDINATO = 0x1;
	public static final int FLAG_ACQUISITO = 0x2;
	public static final int FLAG_ALL = 0x7fffffff;

	private static Logger log = Logger.getLogger(Calcola.class);

	private static final void struttura(StringBuilder sql) {
		sql.append(sql.indexOf("where") > 0 ? " and " : " where ");
	}

	enum StatoFattura {
		REGISTRATA,
		CONTROLLATA,
		PAGATA,
		CONTABILIZZATA;
	}

	public static final CalcoliVO calcola(GenericJDBCAcquisizioniDAO dao,
			String codPolo, String codBib, int idSezione, SezioneVO sezione,
			int idBilancio, String impegno, int esercizio, String dataInizio,
			String dataFine, int flags) throws DataException,
			ApplicationException, ValidationException {

		CalcoliVO calcoli = new CalcoliVO();

		int id = calcoli.getUniqueId();
		log.debug(String.format("calcola(%d) begin", id));
		try {
	 		boolean configBil = true;
			try {
				if (idSezione > 0 && sezione != null)
					configBil = calcolaOrdinatoAcquisito(dao, codPolo, codBib, idSezione, sezione,
							esercizio, dataInizio, dataFine, configBil, calcoli, flags);

				if (idBilancio > 0)
					calcolaImpegnatoFatturato(dao, codPolo, codBib, idBilancio, impegno, dataInizio, dataFine, calcoli);

				if (esercizio > 0)
					calcolaOrdinato(dao, codPolo, codBib, esercizio, dataInizio, dataFine, calcoli);

			} catch (ApplicationException e) {
				throw e;

			} catch (ValidationException e) {
				throw e;

			} catch (Exception e) {
				// l'errore capita in questo punto
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.ACQ_RICALCOLO_BILANCIO_FALLITO, e);
			}

			//bug #3706 accorpamento righe stessa tipologia se replicate
			SezioneVO sez = calcoli.getSezione();
			if (sez != null) {
				List<RigheVO> righeEsameSpesaOld = sez.getRigheEsameSpesa();
				int size = ValidazioneDati.size(righeEsameSpesaOld);
				if (size > 0) {
					List<RigheVO> righeEsameSpesaNew = new ArrayList<RigheVO>();
					boolean duplicazioneRiga = false;

					for (int i = 0; i < size; i++) {
						RigheVO riga = righeEsameSpesaOld.get(i);
						double nuovoImpegnato = 0.00;
						double nuovoAcquisito = 0.00;
						double nuovoValoreInventariale = 0.00;
						nuovoImpegnato = riga.getImpegnato();
						nuovoAcquisito = riga.getAcquisito();
						nuovoValoreInventariale = riga.getValoreInventariale();

						int salti = 0;
						for (int u = i + 1; u < size; u++) {
							RigheVO eleRigSucc = righeEsameSpesaOld.get(u);
							if (!configBil	&& riga.getTipologia().equals(eleRigSucc.getTipologia())) {
								duplicazioneRiga = true;
								salti = salti + 1;
								nuovoAcquisito += eleRigSucc.getAcquisito();
								nuovoImpegnato += eleRigSucc.getImpegnato();
								nuovoValoreInventariale += eleRigSucc.getValoreInventariale();
								riga.setAcquisito(nuovoAcquisito);
								riga.setImpegnato(nuovoImpegnato);
								riga.setValoreInventariale(nuovoValoreInventariale);
								// duplicazioneRiga=false;
							}
						}
						i += salti;
						righeEsameSpesaNew.add(riga);
					}
					if (duplicazioneRiga && isFilled(righeEsameSpesaNew))
						sez.setRigheEsameSpesa(new ArrayList<RigheVO>(righeEsameSpesaNew));
				}
			}
		} finally {
			log.debug(String.format("calcola(%d) end", id));
		}

		return calcoli;

	}

	private static boolean calcolaOrdinatoAcquisito(GenericJDBCAcquisizioniDAO dao,
			String codPolo, String codBib, int idSezione, SezioneVO sezione,
			int esercizio, String dataInizio, String dataFine,
			boolean configBil, CalcoliVO calcoli, int flags) throws SQLException,
			Exception {

		Connection c = null;
		try {
			int id = calcoli.getUniqueId();
			log.debug(String.format("calcolaOrdinatoAcquisito(%d)", id));
			// verifica configurazione gestione bilancio
			c = dao.getConnection();

			ConfigurazioneORDVO richiesta = new ConfigurazioneORDVO();
			richiesta.setCodBibl(codBib);
			richiesta.setCodPolo(codPolo);

			try {
				ConfigurazioneORDVO confOrd = dao.loadConfigurazioneOrdini(richiesta);
				if (confOrd != null && !confOrd.isGestioneBilancio())
					configBil = false;

			} catch (Exception e) {
				// l'errore capita in questo punto
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA, e);
			}

			List<RigheVO> righe = new ArrayList<RigheVO>();
			sezione.setRigheEsameSpesa(righe);

			StringBuilder sql = new StringBuilder(1024);
			//ORDINATO
			// almaviva5_20141027 #5661
			if ((flags & FLAG_ORDINATO) != 0) {
				sql.append("select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totORDINATO ");
				if (configBil)
					sql.append(" ,capBil.esercizio , capBil.capitolo, bil.cod_mat  ");
				else
					sql.append(" , ord.natura, ord.continuativo ");

				sql.append(" FROM tba_ordini ord ");
				sql.append(" join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ");
				if (configBil) {
					sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ");
					sql.append(" join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ");
				}
				sql.append(" where ord.fl_canc<>'S'"); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <> 'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and ord.id_sez_acquis_bibliografiche=?");

				if (isFilled(codBib) )
					sql.append(" and ord.cd_bib=?");

				if (isFilled(codPolo) )
					sql.append(" and ord.cd_polo=?");

				if (isFilled(dataInizio) ) {
					struttura(sql);
					sql.append(" ord.data_ord >= TO_DATE (?,'dd/MM/yyyy')");
				}

				if (isFilled(dataFine) ) {
					struttura(sql);
					sql.append(" ord.data_ord <= TO_DATE (?,'dd/MM/yyyy')");
				}

				if (configBil) {
					if (esercizio != 0) {
						struttura(sql);
						sql.append(" capBil.esercizio=?");
					}
				}

				if (configBil)
					sql.append(" group by capBil.id_capitoli_bilanci, capBil.esercizio , capBil.capitolo,  bil.cod_mat ");
				else {
					sql.append(" group by ord.natura, ord.continuativo ");
					sql.append(" HAVING  ((ord.natura='M' or ord.natura='S') and  ord.continuativo='0')"); // ordine
					sql.append(" OR  (ord.natura='S' and  ord.continuativo='1')"); // abbonamento
					sql.append(" OR  ((ord.natura='M' or ord.natura='C') and  ord.continuativo='1')");  // ordine continuativo
				}

				PreparedStatement ps = c.prepareStatement(sql.toString());

				int idx = addParam(ps, 0, idSezione);
				idx = addParam(ps, idx, codBib);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, dataInizio);
				idx = addParam(ps, idx, dataFine);
				if (configBil)
					addParam(ps, idx, esercizio);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				ResultSet rs = ps.executeQuery();
				double impegnato = 0.00;
				double tot = 0.00;
				String natura = "";
				String continuativo = "";

				while (rs.next()) {
					RigheVO riga = new RigheVO();
					impegnato = rs.getDouble("totORDINATO");
					riga.setImpegnato(impegnato);
					if (configBil) {
						riga.setEsercizio(rs.getString("esercizio"));
						//sez.getRigheEsameSpesa().get(numRiga).setEsercizio(rs.getString("esercizio"));
						riga.setCapitolo(rs.getString("capitolo"));
						riga.setImpegno(rs.getString("cod_mat"));

						if (!riga.getImpegno().equals(""))
							righe.add(riga);
					} else {
						natura = rs.getString("natura");
						continuativo = rs.getString("continuativo");
						riga.setNatura(natura);
						riga.setContinuativo(continuativo);

						if (in(natura, "M", "S") && continuativo.equals("0"))
							riga.setTipologia("Ordine");

						if (natura.equals("S") && continuativo.equals("1"))
							riga.setTipologia("Abbonamento");

						if (in(natura, "M", "C") && continuativo.equals("1"))
							riga.setTipologia("Ordine continuativo");

						if (!riga.getTipologia().equals(""))
							righe.add(riga);
					}

					tot += impegnato;
				}
				sezione.setRigheEsameSpesa(righe);
				sezione.setOrdinato(tot);

				calcoli.setSezione(sezione);
				calcoli.setOrdinato(tot);

				rs.close();
				ps.close();
			}

			//ACQUISITO
			// almaviva5_20141027 #5661
			if ((flags & FLAG_ACQUISITO) != 0) {
				sql.setLength(0);
				sql.append("select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO ");
				if (configBil)
					sql.append(" ,capBil.esercizio , capBil.capitolo, bil.cod_mat  ");
				else
					sql.append(" , ord.natura, ord.continuativo ");

				sql.append(" FROM tbc_inventario inv ");
				sql.append(" join tba_ordini ord on ord.cd_bib=inv.cd_bib_ord  and ord.cod_tip_ord=inv.cd_tip_ord and ord.anno_ord=inv.anno_ord and ord.cod_ord=inv.cd_ord and ord.fl_canc<>'S'");
				sql.append(" join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ");
				if (configBil) {
					sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ");
					sql.append(" join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ");
				}
				sql.append(" where inv.fl_canc<>'S'"); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and ord.id_sez_acquis_bibliografiche=?");

				if (isFilled(codBib) )
					sql.append(" and ord.cd_bib=?");

				if (isFilled(codPolo) )
					sql.append(" and ord.cd_polo=?");

				if (isFilled(dataInizio) ) {
					struttura(sql);
					sql.append(" ord.data_ord >= TO_DATE (?,'dd/MM/yyyy')");
				}

				if (isFilled(dataFine) ) {
					struttura(sql);
					sql.append(" ord.data_ord <= TO_DATE (?,'dd/MM/yyyy')");
				}

				if (configBil) {
					if (esercizio != 0) {
						struttura(sql);
						sql.append(" capBil.esercizio=?");
					}
				}

				if (configBil)
					sql.append(" group by capBil.id_capitoli_bilanci, capBil.esercizio , capBil.capitolo,  bil.cod_mat ");
				else {
					sql.append(" group by ord.natura, ord.continuativo ");
					sql.append(" HAVING  ((ord.natura='M' or ord.natura='S') and  ord.continuativo='0')"); // ordine
					sql.append(" OR  (ord.natura='S' and  ord.continuativo='1')"); // abbonamento
					sql.append(" OR  ((ord.natura='M' or ord.natura='C') and  ord.continuativo='1')");  // ordine continuativo
				}

				PreparedStatement ps = c.prepareStatement(sql.toString());

				int idx = addParam(ps, 0, idSezione);
				idx = addParam(ps, idx, codBib);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, dataInizio);
				idx = addParam(ps, idx, dataFine);
				if (configBil)
					addParam(ps, idx, esercizio);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				ResultSet rs = ps.executeQuery();
				double acquisito = 0.00;
				double tot = 0.00;

				while (rs.next()) {
					acquisito = rs.getDouble("totACQUISITO");
					for (RigheVO riga : righe) {
						if (configBil) {
							if (rs.getString("esercizio") != null
									&& riga.getEsercizio().equals(rs.getString("esercizio"))
									&& riga.getCapitolo().equals(rs.getString("capitolo"))
									&& riga.getImpegno().equals(rs.getString("cod_mat"))) {
								riga.setAcquisito(acquisito);
							}
						} else { // in assenza di gestione del bilancio
							if (riga.getNatura() != null
									&& riga.getNatura().equals(rs.getString("natura"))
									&& riga.getContinuativo() != null
									&& riga.getContinuativo().equals(rs.getString("continuativo")))
								riga.setAcquisito(acquisito);
						}

					}
					tot += acquisito;
				}

				sezione.setRigheEsameSpesa(righe);
				sezione.setAcquisito(tot);
				calcoli.setSezione(sezione);

				rs.close();
				ps.close();
			}
		} finally {
			dao.close(c);
		}

		return configBil;
	}

	private static void calcolaOrdinato(GenericJDBCAcquisizioniDAO dao,
			String codPolo, String codBib, int esercizio, String dataInizio,
			String dataFine, CalcoliVO calcoli) throws SQLException {

		Connection c = null;
		try {
			int id = calcoli.getUniqueId();
			log.debug(String.format("calcolaOrdinato(%d)", id) );
			c = dao.getConnection();

			//ORDINATO
			StringBuilder sql = new StringBuilder(1024);
			//String sql="select sum(ord.prezzo_lire) as totORDINATO FROM tba_ordini ord";
			sql.append("select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totORDINATO FROM tba_ordini ord");
			//sql.append(" join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ";
			sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ");
			//sql.append(" join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
			sql.append(" where ord.fl_canc<>'S'"); // ESCLUDO I CANCELLATI
			sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
			sql.append(" and capBil.esercizio=?");
			if (isFilled(codBib) )
				sql.append(" and ord.cd_bib=?");

			if (isFilled(codPolo) )
				sql.append(" and ord.cd_polo=?");

			if (isFilled(dataInizio) ) {
				struttura(sql);
				sql.append(" ord.data_fine >= TO_DATE (?,'dd/MM/yyyy')");
			}
			if (isFilled(dataFine) ) {
				struttura(sql);
				sql.append(" ord.data_fine <= TO_DATE (?,'dd/MM/yyyy')");
			}

			PreparedStatement ps = c.prepareStatement(sql.toString());

			int idx = addParam(ps, 0, esercizio);
			idx = addParam(ps, idx, codBib);
			idx = addParam(ps, idx, codPolo);
			idx = addParam(ps, idx, dataInizio);
			idx = addParam(ps, idx, dataFine);

			log.debug(String.format("sql(%d)= %s", id, sql) );
			ResultSet rs = ps.executeQuery();
			double acq = 0.00;
			while (rs.next()) {
				acq = rs.getDouble("totORDINATO");
			}
			calcoli.setOrdinato(acq);
			rs.close();
			ps.close();

		} finally {
			dao.close(c);
		}
	}

	private static void calcolaImpegnatoFatturato(GenericJDBCAcquisizioniDAO dao,
			String codPolo, String codBib, int idBilancio, String impegno,
			String dataInizio, String dataFine, CalcoliVO calcoli)
			throws SQLException {

		Connection c = null;
		try {
			int id = calcoli.getUniqueId();
			log.debug(String.format("calcolaImpegnatoFatturato(%d)", id) );
			c = dao.getConnection();

			//ORDINATO/IMPEGNATO
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totORDINATO FROM tba_ordini ord ");
			sql.append(" where ord.fl_canc<>'S'"); // ESCLUDO I CANCELLATI
			sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
			sql.append(" and ord.id_capitoli_bilanci=?");
			if (isFilled(codBib))
				sql.append(" and ord.cd_bib=?");

			if (isFilled(codPolo))
				sql.append(" and ord.cd_polo=?");

			if (isFilled(impegno)) {
				struttura(sql);
				sql.append(" ord.tbb_bilancicod_mat = ?");
			}

			if (isFilled(dataInizio)) {
				struttura(sql);
				sql.append(" ord.data_ord >= TO_DATE (?,'dd/MM/yyyy')");
			}

			if (isFilled(dataFine)) {
				struttura(sql);
				sql.append(" ord.data_ord <= TO_DATE (?,'dd/MM/yyyy')");
			}

			PreparedStatement ps = c.prepareStatement(sql.toString());

			int idx = addParam(ps, 0, idBilancio);
			idx = addParam(ps, idx, codBib);
			idx = addParam(ps, idx, codPolo);
			idx = addParam(ps, idx, impegno);
			idx = addParam(ps, idx, dataInizio);
			idx = addParam(ps, idx, dataFine);

			log.debug(String.format("sql(%d)= %s", id, sql) );
			ResultSet rs = ps.executeQuery();
			double ordinato = 0.00;
			while (rs.next()) {
				ordinato = rs.getDouble("totORDINATO");
			}
			calcoli.setOrdinato(ordinato);
			rs.close();
			ps.close();

			if (isFilled(impegno) )	{
				//FATTURATO
				sql.setLength(0);
				sql.append("select  CASE WHEN  (sum(rigaFatt.importo_riga * fatt.cambio ) is null) THEN 0  else ");
				sql.append(" sum(CAST(rigaFatt.importo_riga * fatt.cambio ");
				sql.append(" - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) "); // sconto1 ovvero importo scontato del primo sconto
				sql.append(" - (((rigaFatt.importo_riga* fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100) ");  // sconto2 ovvero importo scontato del primo sconto su cui si applica il secondo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio- ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100) "); // sconto3 importo con gli sconti 1 e 2 su cui si applica il 3 della fattura
				sql.append(" + (((rigaFatt.importo_riga * fatt.cambio - (((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100)) )* CAST(rigaFatt.cod_iva AS int))/100)"); // aggiungo l'iva (calcolo al 20%)
				sql.append(" AS double precision ) ");
				sql.append(" )   END  as sommaFatt	");
				sql.append(" from tba_righe_fatture rigaFatt  ");
				sql.append(" join tba_fatture fatt  on rigaFatt.id_fattura=fatt.id_fattura AND fatt.fl_canc<>'S'  ");
				sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=rigaFatt.id_capitoli_bilanci and capBil.fl_canc<>'S'  ");
				sql.append(" join tba_ordini ord on ord.cd_polo=rigaFatt.cd_polo and ord.cd_bib=rigaFatt.cd_biblioteca and ord.fl_canc<>'S' and ord.id_ordine=rigaFatt.id_ordine  ");
				sql.append(" where rigaFatt.fl_canc<>'S' "); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and fatt.stato_fattura='2'"); // fatture controllate
				sql.append(" and fatt.tipo_fattura='F'"); // fatture
				sql.append(" and rigaFatt.id_capitoli_bilanci=?");
				sql.append(" and rigaFatt.cod_mat= ?");
				if (isFilled(codPolo) )
					sql.append(" and rigaFatt.cd_polo=?");

				if (isFilled(codBib) )
					sql.append(" and rigaFatt.cd_biblioteca=?");

				ps = c.prepareStatement(sql.toString());

				idx = addParam(ps, 0, idBilancio);
				idx = addParam(ps, idx, impegno);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, codBib);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				rs = ps.executeQuery();
				double fatturato = 0.00;
				// esiste un unico risultato
				while (rs.next()) {
					BigDecimal bd = rs.getBigDecimal("sommaFatt");
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					fatturato = bd.doubleValue();
				}

				rs.close();
				ps.close();

				calcoli.setFatturato(fatturato);
			}

			if (isFilled(impegno) )	{
				//PAGATO
				//calcolo pagato (importi fatt pagate)
				sql.setLength(0);
				sql.append("select  CASE WHEN  (sum(rigaFatt.importo_riga * fatt.cambio) is null) THEN 0  else ");
				sql.append(" sum(CAST(rigaFatt.importo_riga * fatt.cambio ");
				sql.append(" - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) "); // sconto1 ovvero importo scontato del primo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100) ");  // sconto2 ovvero importo scontato del primo sconto su cui si applica il secondo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100) "); // sconto3 importo con gli sconti 1 e 2 su cui si applica il 3 della fattura
				// calcolo iva
				// + ((importo – (importoScontato1) - (importoScontato2) – (importoScontato3))*iva/100)
				sql.append(" + (((rigaFatt.importo_riga * fatt.cambio - (((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100)) )* CAST(rigaFatt.cod_iva AS int))/100)"); // aggiungo l'iva (calcolo al 20%)

				sql.append(" AS double precision ) ");
				sql.append(" )   END  as sommaFatt	");

				// se si usa il trigger controlla_riga_fattura eliminare parte sovrastante
				//sql="select sum(CAST(rigaFatt.importo_tot_riga AS double precision)) as sommaFatt " ;


				sql.append(" from tba_righe_fatture rigaFatt  ");
				sql.append(" join tba_fatture fatt  on rigaFatt.id_fattura=fatt.id_fattura AND fatt.fl_canc<>'S'   ");
				sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=rigaFatt.id_capitoli_bilanci and capBil.fl_canc<>'S'  ");
				sql.append(" join tba_ordini ord on ord.cd_polo=rigaFatt.cd_polo and ord.cd_bib=rigaFatt.cd_biblioteca and ord.fl_canc<>'S' and ord.id_ordine=rigaFatt.id_ordine  ");
				sql.append(" where rigaFatt.fl_canc<>'S' "); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and fatt.stato_fattura='3'"); // fatture pagate
				sql.append(" and fatt.tipo_fattura='F'"); // fatture
				sql.append(" and rigaFatt.id_capitoli_bilanci=?");
				sql.append(" and rigaFatt.cod_mat=?");
				if (isFilled(codPolo) )
					sql.append(" and rigaFatt.cd_polo=?");

				if (isFilled(codBib) )
					sql.append(" and rigaFatt.cd_biblioteca=?");

				ps = c.prepareStatement(sql.toString());

				idx = addParam(ps, 0, idBilancio);
				idx = addParam(ps, idx, impegno);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, codBib);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				rs = ps.executeQuery();
				double pagato = 0.00;
				while (rs.next()) {
					pagato = rs.getDouble("sommaFatt");
				}
				calcoli.setPagato(pagato);
				rs.close();
				ps.close();

				// il pagato va considerato anche nel fatturato
				double fatturato = 0.00;
				fatturato = calcoli.getFatturato();

				if (pagato > 0) {
					fatturato += pagato;
					calcoli.setFatturato(fatturato);
				}
			}

			if (isFilled(impegno) )	{
				//NOTE DI CREDITO CONTABILIZZATE
				// calcolo dei  crediti delle note di credito in stato CONTABILIZZATO =4
				// per le righe delle note di credito legate a fatture in stato =2 (controllata)
				// il credito va sottratto al fatturato
				sql.setLength(0);
				sql.append("select  CASE WHEN  (sum(rigaFatt.importo_riga * fatt.cambio) is null) THEN 0  else ");
				sql.append(" sum(CAST(rigaFatt.importo_riga * fatt.cambio ");
				sql.append(" - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) "); // sconto1 ovvero importo scontato del primo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100) ");  // sconto2 ovvero importo scontato del primo sconto su cui si applica il secondo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100) "); // sconto3 importo con gli sconti 1 e 2 su cui si applica il 3 della fattura
				// calcolo iva
				// + ((importo – (importoScontato1) - (importoScontato2) – (importoScontato3))*iva/100)
				sql.append(" + (((rigaFatt.importo_riga * fatt.cambio - (((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100)) )* CAST(rigaFatt.cod_iva AS int))/100)"); // aggiungo l'iva

				sql.append(" AS double precision ) ");
				sql.append(" )   END  as sommaFatt	");

				// se si usa il trigger controlla_riga_fattura eliminare parte sovrastante
				//sqlNC="select sum(CAST(rigaFatt.importo_tot_riga AS double precision)) as sommaFatt " ;


				sql.append(" from tba_righe_fatture rigaFatt  ");
				sql.append(" join tba_fatture fatt  on rigaFatt.id_fattura=fatt.id_fattura AND fatt.fl_canc<>'S' and fatt.tipo_fattura='N' and fatt.stato_fattura='4'  "); // note di credito contabilizzate
				sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=rigaFatt.id_capitoli_bilanci and capBil.fl_canc<>'S'  ");
				sql.append(" join tba_ordini ord on ord.cd_polo=rigaFatt.cd_polo and ord.cd_bib=rigaFatt.cd_biblioteca and ord.fl_canc<>'S' and ord.id_ordine=rigaFatt.id_ordine  ");
				sql.append(" join tba_fatture fattNC  on fattNC.id_fattura=rigaFatt.id_fattura_in_credito AND fattNC.fl_canc<>'S' and fattNC.tipo_fattura='F' ");
				sql.append(" where rigaFatt.fl_canc<>'S' "); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and rigaFatt.id_capitoli_bilanci=?") ;
				sql.append(" and rigaFatt.cod_mat=?");
				sql.append(" and fattNC.stato_fattura='2'"); // fatture controllate (alimentano il fatturato)
				//sql.append(" and fattNC.stato_fattura='3'"; // fatture pagate (alimentano il pagato)

				if (isFilled(codPolo) )
					sql.append(" and rigaFatt.cd_polo=?");

				if (isFilled(codBib) )
					sql.append(" and rigaFatt.cd_biblioteca=?");
				ps = c.prepareStatement(sql.toString());

				idx = addParam(ps, 0, idBilancio);
				idx = addParam(ps, idx, impegno);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, codBib);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				rs = ps.executeQuery();
				double credito=0.00;
				while (rs.next()) {
					credito=rs.getDouble("sommaFatt");
				}
				rs.close();
				ps.close();

				double fattur = calcoli.getFatturato();

				if (credito > 0) {
					fattur =- credito;
					calcoli.setFatturato(fattur);
				}

				// per le righe delle note di credito legate a fatture in stato =3 (pagato)
				// il credito va sottratto al pagato e fatturato
				sql.setLength(0);
				sql.append("select  CASE WHEN  (sum(rigaFatt.importo_riga * fatt.cambio) is null) THEN 0  else ");
				sql.append(" sum(CAST(rigaFatt.importo_riga * fatt.cambio ");
				sql.append(" - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) "); // sconto1 ovvero importo scontato del primo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100) ");  // sconto2 ovvero importo scontato del primo sconto su cui si applica il secondo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100) "); // sconto3 importo con gli sconti 1 e 2 su cui si applica il 3 della fattura
				// calcolo iva
				// + ((importo – (importoScontato1) - (importoScontato2) – (importoScontato3))*iva/100)
				sql.append(" + (((rigaFatt.importo_riga * fatt.cambio - (((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100)) )* CAST(rigaFatt.cod_iva AS int))/100)"); // aggiungo l'iva (calcolo al 20%)

				sql.append(" AS double precision ) ");
				sql.append(" )   END  as sommaFatt	");

				// se si usa il trigger controlla_riga_fattura eliminare parte sovrastante
				//sqlNC2="select sum(CAST(rigaFatt.importo_tot_riga AS double precision)) as sommaFatt " ;

				sql.append(" from tba_righe_fatture rigaFatt  ");
				sql.append(" join tba_fatture fatt  on rigaFatt.id_fattura=fatt.id_fattura AND fatt.fl_canc<>'S' and fatt.tipo_fattura='N' and fatt.stato_fattura='4'  "); // note di credito contabilizzate
				sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=rigaFatt.id_capitoli_bilanci and capBil.fl_canc<>'S'  ");
				sql.append(" join tba_ordini ord on ord.cd_polo=rigaFatt.cd_polo and ord.cd_bib=rigaFatt.cd_biblioteca and ord.fl_canc<>'S' and ord.id_ordine=rigaFatt.id_ordine  ");
				sql.append(" join tba_fatture fattNC  on fattNC.id_fattura=rigaFatt.id_fattura_in_credito AND fattNC.fl_canc<>'S' and fattNC.tipo_fattura='F' ");
				sql.append(" where rigaFatt.fl_canc<>'S' "); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and rigaFatt.id_capitoli_bilanci=?");
				sql.append(" and rigaFatt.cod_mat=?");
				//sql.append(" and fattNC.stato_fattura='2'"; // fatture controllate (alimentano il fatturato)
				sql.append(" and fattNC.stato_fattura='3'"); // fatture pagate (alimentano il pagato)

				if (isFilled(codPolo) )
					sql.append(" and rigaFatt.cd_polo=?");

				if (isFilled(codBib) )
					sql.append(" and rigaFatt.cd_biblioteca=?");

				ps = c.prepareStatement(sql.toString());

				idx = addParam(ps, 0, idBilancio);
				idx = addParam(ps, idx, impegno);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, codBib);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				rs = ps.executeQuery();
				double credito2 = 0.00;
				while (rs.next()) {
					credito2 = rs.getDouble("sommaFatt");
				}
				rs.close();
				ps.close();

				double pagato = calcoli.getPagato();
				if (credito2 > 0) {
					pagato -= credito2;
					calcoli.setPagato(pagato);
				}

				double fatturato = calcoli.getFatturato();
				if (credito2 > 0) {
					fatturato -= credito2;
					calcoli.setFatturato(fatturato);
				}

				// per le righe delle note di credito non legate a fatture  (pagato)
				// il credito va sottratto al pagato
				sql.setLength(0);
				sql.append("select  CASE WHEN  (sum(rigaFatt.importo_riga * fatt.cambio) is null) THEN 0  else ");
				sql.append(" sum(CAST(rigaFatt.importo_riga * fatt.cambio ");
				sql.append(" - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) "); // sconto1 ovvero importo scontato del primo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100) ");  // sconto2 ovvero importo scontato del primo sconto su cui si applica il secondo sconto
				sql.append(" - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100) "); // sconto3 importo con gli sconti 1 e 2 su cui si applica il 3 della fattura
				// calcolo iva
				// + ((importo – (importoScontato1) - (importoScontato2) – (importoScontato3))*iva/100)
				sql.append(" + (((rigaFatt.importo_riga * fatt.cambio - (((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) - ((((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100) - (((rigaFatt.importo_riga * fatt.cambio - ((rigaFatt.importo_riga * fatt.cambio * rigaFatt.sconto_1)/100)) * rigaFatt.sconto_2)/100)) * fatt.sconto)/100)) )* CAST(rigaFatt.cod_iva AS int))/100)"); // aggiungo l'iva (calcolo al 20%)

				sql.append(" AS double precision ) ");
				sql.append(" )   END  as sommaFatt	");

				// se si usa il trigger controlla_riga_fattura eliminare parte sovrastante
				//sqlNC3="select sum(CAST(rigaFatt.importo_tot_riga AS double precision)) as sommaFatt " ;

				sql.append(" from tba_righe_fatture rigaFatt  ");
				sql.append(" join tba_fatture fatt  on rigaFatt.id_fattura=fatt.id_fattura AND fatt.fl_canc<>'S' and fatt.tipo_fattura='N' and fatt.stato_fattura='4'  "); // note di credito contabilizzate
				sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=rigaFatt.id_capitoli_bilanci and capBil.fl_canc<>'S'  ");
				sql.append(" join tba_ordini ord on ord.cd_polo=rigaFatt.cd_polo and ord.cd_bib=rigaFatt.cd_biblioteca and ord.fl_canc<>'S' and ord.id_ordine=rigaFatt.id_ordine  ");
				//sql.append(" join tba_fatture fattNC  on fattNC.id_fattura=rigaFatt.id_fattura_in_credito AND fattNC.fl_canc<>'S' and fattNC.tipo_fattura='F' ";
				sql.append(" where rigaFatt.fl_canc<>'S' "); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				sql.append(" and rigaFatt.id_fattura_in_credito is null ");
				sql.append(" and rigaFatt.cod_mat=?");
				//sql.append(" and fattNC.stato_fattura='2'"; // fatture controllate (alimentano il fatturato)
				//sql.append(" and fattNC.stato_fattura='3'"; // fatture pagate (alimentano il pagato)

				if (isFilled(codPolo) )
					sql.append(" and rigaFatt.cd_polo=?");

				if (isFilled(codBib) )
					sql.append(" and rigaFatt.cd_biblioteca=?");

				sql.append(" and rigaFatt.id_capitoli_bilanci=?");

				ps = c.prepareStatement(sql.toString());

				idx = addParam(ps, 0, impegno);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, codBib);
				idx = addParam(ps, idx, idBilancio);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				rs = ps.executeQuery();
				double credito3 = 0.00;
				while (rs.next()) {
					credito3 = rs.getDouble("sommaFatt");
				}
				rs.close();
				ps.close();

				double pagat3 = 0.00;

				pagat3 = calcoli.getPagato();

				if (credito3 > 0) {
					pagat3 =- credito3;
					calcoli.setPagato(pagat3);
				}
				// fine calcolo credito delle note di credito
			}

			if (isFilled(impegno) )	{
				// calcolo acquisito
				sql.setLength(0);
				sql.append("select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO ");
				sql.append(" FROM tbc_inventario inv ");
				sql.append(" join tba_ordini ord on ord.cd_bib=inv.cd_bib_ord  and ord.cod_tip_ord=inv.cd_tip_ord and ord.anno_ord=inv.anno_ord and ord.cod_ord=inv.cd_ord and ord.fl_canc<>'S' ");
				sql.append(" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ");
				sql.append(" join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ");
				sql.append(" where inv.fl_canc<>'S' "); // ESCLUDO I CANCELLATI
				sql.append(" and ord.stato_ordine <>'N'"); // ESCLUDO GLI ANNULLATI
				//sql.append(" and ord.id_capitoli_bilanci=" + idBil;
				if (isFilled(codBib) )
					sql.append(" and ord.cd_bib=?");

				if (isFilled(codPolo) )
					sql.append(" and ord.cd_polo=?");

				if (isFilled(impegno) )
				{
					struttura(sql);
					sql.append(" ord.tbb_bilancicod_mat = ?");
				}
				struttura(sql);
				sql.append(" ord.id_capitoli_bilanci = ?");

				ps = c.prepareStatement(sql.toString());

				idx = addParam(ps, 0, codBib);
				idx = addParam(ps, idx, codPolo);
				idx = addParam(ps, idx, impegno);
				idx = addParam(ps, idx, idBilancio);

				log.debug(String.format("sql(%d)= %s", id, sql) );
				rs = ps.executeQuery();
				double acq = 0.00;

				DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setGroupingSeparator('.');
				dfs.setDecimalSeparator(',');
				DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
				df.setMaximumFractionDigits(2);
				df.setMinimumFractionDigits(2);
				while (rs.next()) {
					acq = rs.getDouble("totACQUISITO");
				}
				calcoli.setAcquisito(acq);
				rs.close();
				ps.close();
			}

		} finally {
			dao.close(c);
		};
	}
}
