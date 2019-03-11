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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBMT;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RicercaTitCollEditoriVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.persistence.dao.common.AbstractJDBCManager;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.DataException;

public class GenericJDBCAcquisizioniEditoreDAO extends AbstractJDBCManager //AbstactJDBCManager
{
	private static Codici codici;
	private static ServiziBibliografici servizi;
	private static Inventario inventario;
	private static Log log = LogFactory.getLog(GenericJDBCAcquisizioniDAO.class);

	private AcquisizioniBMT acquisizioniBMT;


	static {
		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			codici = factory.getCodici();
			servizi = factory.getSrvBibliografica();
			inventario = factory.getInventario();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error("", e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("", e);
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
	}




	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Inserito nuovo metodo per la ricerca dei titoli collegati in modo esplicito o implicito all'editore selezionato
	// viene richiamata dalla sintetica fornitori/editori con il tasto Titoli Collegati
	public List getRicercaTitCollEditori(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException {

		 List listaTitColl = new ArrayList();

		String rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int elencoSize;;
		elencoSize = ricercaTitCollEditoriVO.getEditSelez().length;

		try {
			// contiene i criteri di ricerca
			connection = getConnection();

			String sqlTitColl = "";
			sqlTitColl ="select ca.bid from v_catalogo_editoria ca where ";
			for (int i = 0; i < elencoSize; i++) {
				sqlTitColl = sqlTitColl + " ca.cod_fornitore = '" + ricercaTitCollEditoriVO.getEditSelez()[i] + "'";
				if ((i+1) < elencoSize) {
					sqlTitColl = sqlTitColl + " or";
				}
			}

			// Maggio 2013 Inserito ordinamento titoli collegati a Editoer per Nome editore + Data pubblicazione + Titolo
			sqlTitColl = sqlTitColl + " order by ca.chiave_for, ca.aa_pubb_1, ca.ky_cles1_t||COALESCE(ca.ky_cles2_t, '')";

			pstmt = connection.prepareStatement(sqlTitColl);
			rs = pstmt.executeQuery();
			int numRighe=0;

			while (rs.next()) {
				numRighe = numRighe+1;
				rec = new String();
				rec = rs.getString("bid");
				listaTitColl.add(rec);
			}
			rs.close();
			connection.close();

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
       return listaTitColl;


	}


	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Inserito nuovo metodo per la ricerca degli eventuali editori gia collegati ad un titolo
	// Metodo richiamata dal Vai a su un titolo x cercare tutti gli editori già legati al titolo stesso
	public List getRicercaEditCollTitolo(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket)
		throws ResourceNotFoundException, ApplicationException, ValidationException {

		List<SinteticaTitoliView> listaSintentica = new ArrayList<SinteticaTitoliView>();

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// contiene i criteri di ricerca
			connection = getConnection();
			String sqlTitColl = "";
			sqlTitColl ="select ca.cod_fornitore, ca.nom_fornitore, ca.nota_legame " ;
			sqlTitColl = sqlTitColl + "from v_catalogo_editoria ca where ";
			sqlTitColl = sqlTitColl + "ca.bid = ('" + ricercaTitCollEditoriVO.getBidSelez() + "') ";

			pstmt = connection.prepareStatement(sqlTitColl);
			rs = pstmt.executeQuery();
			int numRighe=0;
			String desc="";

			SinteticaTitoliView data = null;

			while (rs.next()) {
				numRighe = numRighe+1;

				data = new SinteticaTitoliView();
				data.setImageUrl("blank.png");
				data.setProgressivo(numRighe);
				data.setFlagCondiviso(true);
				data.setBid(rs.getString("cod_fornitore"));
				data.setNatura("");
				data.setLivelloAutorita("");
				data.setTipoMateriale("bianco");
				if (rs.getString("nota_legame").contains("[")) {
					desc = ValidazioneDati.rtrim(rs.getString("nom_fornitore")) + " " + rs.getString("nota_legame");
				} else {
					desc = ValidazioneDati.rtrim(rs.getString("nom_fornitore")) +  " (" + rs.getString("nota_legame")+  ")";
				}
				data.setDescrizioneLegami(desc);
				data.setTipoRecDescrizioneLegami(desc);
				data.setTipoRecord("");
				data.setTipoRecordDesc("");
				data.setDataIns("");

				listaSintentica.add(data);
			}
			rs.close();
			connection.close();

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
        return listaSintentica;
	}

	public List getAreeIsbdListaBid(List listaBid) throws DaoManagerException {

		List results = null;
		SinteticaTitoliView data = null;
		Connection connectionTer = null;
		PreparedStatement pstmtTer = null;
		ResultSet rsInter = null;

		int elencoSize = listaBid.size();
		String seqBid = "";
		for (int i = 0; i < elencoSize; i++) {
			seqBid = seqBid + "'" + listaBid.get(i)  + "'" + ",";
		}
		if (seqBid.endsWith(",")) {
			seqBid = seqBid.substring(0, seqBid.length() - 1);
		}

		List<SinteticaTitoliView> listaSintentica = new ArrayList<SinteticaTitoliView>();

		try {

			String sqlString="select t.bid, t.cd_natura, t.cd_livello, t.tp_record_uni, t.tp_materiale, t.isbd" ;
			sqlString=sqlString + " from Tb_titolo t ";
			sqlString=sqlString +" where t.fl_canc<>'S'";
			sqlString=sqlString +" and t.bid in (" + seqBid + ")";

			connectionTer = getConnection();

			pstmtTer = connectionTer.prepareStatement(sqlString);
			rsInter = pstmtTer.executeQuery();
			int numRighe=0;
			String desc="";

			results=new ArrayList();

			while (rsInter.next()) {
				numRighe=numRighe+1;
				data = new SinteticaTitoliView();
				data.setImageUrl("blank.png");
				data.setProgressivo(numRighe);
				data.setFlagCondiviso(true);
				data.setBid(rsInter.getString("bid"));
				data.setNatura(rsInter.getString("cd_natura"));
				data.setLivelloAutorita(rsInter.getString("cd_livello"));

				String myTipoMateriale = "";
				if (rsInter.getString("tp_materiale") != null) {
					myTipoMateriale = rsInter.getString("tp_materiale");
				}
				if (myTipoMateriale.equals("E")) {
	                data.setTipoMateriale("antico");
	                data.setImageUrl("sintAntico.gif");
	            } else if (myTipoMateriale.equals("C")) {
	                data.setTipoMateriale("cartografia");
	                data.setImageUrl("sintCartografia.gif");
	            } else if (myTipoMateriale.equals("G")) {
	                data.setTipoMateriale("grafica");
	                data.setImageUrl("sintGrafica.gif");
	            } else if (myTipoMateriale.equals("M")) {
	                data.setTipoMateriale("moderno");
	                data.setImageUrl("sintModerno.gif");
	            } else if (myTipoMateriale.equals("U")) {
	                data.setTipoMateriale("musica");
	                data.setImageUrl("sintMusica.gif");
	            } else {
	            	 data.setTipoMateriale("bianco");
	            }

	            String myTipoRecord = "";
	            String myTipoRecordDesc = "";
	            if (rsInter.getString("tp_record_uni") != null)
	            	myTipoRecord = rsInter.getString("tp_record_uni");
                	myTipoRecordDesc = codici.cercaDescrizioneCodice(myTipoRecord,
	                    		CodiciType.CODICE_GENERE_MATERIALE_UNIMARC, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                if (myTipoRecordDesc == null) {
                   	myTipoRecordDesc = "";
                }

				desc = data.getBid() + " " + data.getNatura() + " " + data.getLivelloAutorita() + " " + myTipoRecordDesc
									+ SinteticaTitoliView.HTML_NEW_LINE + rsInter.getString("isbd");
				data.setDescrizioneLegami(desc);
				data.setTipoRecDescrizioneLegami(desc);
				data.setTipoRecord("");
				data.setTipoRecordDesc("");
				data.setDataIns("");

				listaSintentica.add(data);

			}
			rsInter.close();
			connectionTer.close();


		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connectionTer);
		}
		return listaSintentica;
	}



	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// inserimento occorrenza nella tabella tr_editore_titolo che contiene i legami espliciti fra titolo e fornitore/editore
	public AreaDatiVariazioneReturnVO  gestioneLegameTitEdit(AreaDatiLegameTitoloVO areaDatiPass, String ticket)
					throws DataException, ApplicationException , ValidationException {

		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = new AreaDatiVariazioneReturnVO();
		areaDatiVariazioneReturnVO.setCodErr("0000");

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esistenzaLegame = false;

		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select legame.fl_canc, legame.nota_legame from tr_editore_titolo legame ";
			sql0=this.struttura(sql0);
			sql0=sql0 + " legame.cod_fornitore='" + areaDatiPass.getIdArrivo() +"'";
			sql0=this.struttura(sql0);
			sql0=sql0 + " legame.bid='" + areaDatiPass.getBidPartenza() +"'";

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			int numRighe=0;

			while (rs.next()) {
				numRighe=numRighe+1;
			}
			if (numRighe >= 1) {
				if (numRighe == 1) {
					esistenzaLegame=true;
				}
			}
			rs.close();

			if (esistenzaLegame) {

				// update anche del flag e non insert da gestire
				// modifica fornitore esistente  LOGICAMENTE CANCELLATO

				// Maggio 2013 - sistemata la select x update xchè mancante la condizione di where
				Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
				String sqlUDP="update tr_editore_titolo set " ;
//				sqlUDP = sqlUDP + "cod_fornitore='" +  areaDatiPass.getIdArrivo().trim().replace("'","''") + "'" ;  //
//				sqlUDP = sqlUDP + ",bid='" +  areaDatiPass.getBidPartenza().trim() + "'" ;  //
				sqlUDP = sqlUDP + "nota_legame='" +  areaDatiPass.getNoteLegameNew().replace("'","''") + "'" ;  //
				sqlUDP = sqlUDP + ", ute_var='" + DaoManager.getFirmaUtente(ticket) + "'" ;   // ute_var
				sqlUDP = sqlUDP + ", ts_var='" + ts + "'";   // ts_var
				if (areaDatiPass.getTipoOperazione().equals("Cancella")) {
					sqlUDP = sqlUDP + ", fl_canc='S'";   // aggiornamento flag di cancellazione
				} else {
					sqlUDP = sqlUDP + ", fl_canc='N'";   // aggiornamento flag di cancellazione
				}
				// CONDIZIONI
				sqlUDP= sqlUDP + " where cod_fornitore='" + areaDatiPass.getIdArrivo().trim().replace("'","''") + "'";
				sqlUDP= sqlUDP + " and bid='" + areaDatiPass.getBidPartenza().trim() + "'" ;


				pstmt = connection.prepareStatement(sqlUDP);
				int intRetUDP = 0;
				intRetUDP = pstmt.executeUpdate();
				pstmt.close();
			}

			if (!esistenzaLegame) {
				// non esistono legami Titolo-Editore/Fornitore quindi si può procedere con l'inserimento
				Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
				String sql="insert into tr_editore_titolo values ( " ;
				sql=sql + "'" +  areaDatiPass.getIdArrivo().trim().replace("'","''") +"'" ;  // cod_fornitore
				sql=sql + ",'" +  areaDatiPass.getBidPartenza() + "'" ;  // bid titolo
				sql=sql + ",'" +  areaDatiPass.getNoteLegameNew().replace("'","''") + "'" ;  // nota al legame
				sql=sql + ",'" + DaoManager.getFirmaUtente(ticket) + "'" ;   // ute_ins
				sql=sql + ",'" + ts + "'" ;   // ts_ins
				sql=sql + ",'" + DaoManager.getFirmaUtente(ticket) + "'" ;   // ute_var
				sql=sql + ",'" + ts + "'";   // ts_var
				sql=sql + ",'N'";   // fl_canc
				sql=sql + ")" ;
				pstmt = connection.prepareStatement(sql);
				log.error("Debug: inserimento  legame Titolo-Editore/Fornitore");
				log.error("Debug: " + sql);

				int intRet = 0;
				intRet = pstmt.executeUpdate();
				pstmt.close();
			}

		} catch (Exception e) {
			log.error("", e);
			areaDatiVariazioneReturnVO.setCodErr("9999");
			areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + e.getMessage());
		} finally {
			close(connection);
			}
		areaDatiVariazioneReturnVO.setCodErr("0000");
        return areaDatiVariazioneReturnVO;
	}


	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// inserimento occorrenza nella tabella tr_editore_titolo che contiene i legami espliciti fra titolo e fornitore/editore
	// Maggio 2013 - l'operazione di cancellazione editore corrisponde solo alla cancellazione del codice regione (gia fatto
	// nella fase di update fornitore) e nella cancellazione dalla tabella degli isbn associati

	public boolean  gestioneEditoreNumStandard(FornitoreVO fornitore, String tipoOper)
					throws DataException, ApplicationException , ValidationException {


		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean esistenzaLegame = false;
		List campoUno = new ArrayList();



		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select editISBN.cod_fornitore, editISBN.numero_std, editISBN.fl_canc from tbr_editore_num_std editISBN ";
			sql0 = this.struttura(sql0);
			sql0 = sql0 + " editISBN.cod_fornitore='" + fornitore.getCodFornitore() +"'";
			sql0 = sql0 + " and editISBN.fl_canc='N'";

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			int numRighe=0;

			while (rs.next()) {
				numRighe=numRighe+1;
				if (tipoOper.equals("I")) {
					fornitore.getListaNumStandard().add(new TabellaNumSTDImpronteVO(rs.getString("numero_std").trim(), "", "", ""));
					if (fornitore.getIsbnEditore() == null || fornitore.getIsbnEditore().equals("")) {
						fornitore.setIsbnEditore(rs.getString("numero_std").trim());
					} else {
						fornitore.setIsbnEditore(fornitore.getIsbnEditore() + " , " + rs.getString("numero_std").trim());
					}
				} else {
					campoUno.add(rs.getString("numero_std").trim());
				}
			}
			if (tipoOper.equals("I")) {
				rs.close();
				return true;
			}

			if (numRighe > 0) {
				esistenzaLegame=true;
			}

			rs.close();


			if (esistenzaLegame) {
				for (int iDB = 0; iDB < campoUno.size(); iDB++) {
					Boolean trovato = false;
					for (int iNew = 0; iNew < fornitore.getListaNumStandard().size(); iNew++) {
						if (campoUno.get(iDB).equals(((TabellaNumSTDImpronteVO)fornitore.getListaNumStandard().get(iNew)).getCampoUno())) {
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						// questo ISBD è presente sul DB ma assente dai dati forniti dal Client quindi va effettuata la cancellazione logica
						Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
						String sqlUDP="update tbr_editore_num_std set " ;
						sqlUDP = sqlUDP + "cod_fornitore='" +  fornitore.getCodFornitore().trim().replace("'","''") + "'" ;  //
						sqlUDP = sqlUDP + ",numero_std='" +  ((String)campoUno.get(iDB)).trim() + "'" ;  //
		  				sqlUDP = sqlUDP + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
						sqlUDP = sqlUDP + ", ts_var='" + ts + "'";   // ts_var
						sqlUDP = sqlUDP + ", fl_canc='S'";   // aggiornamento flag di cancellazione
						// CONDIZIONI
						sqlUDP= sqlUDP + " where cod_fornitore='" + fornitore.getCodFornitore().trim() + "'" ;
						sqlUDP= sqlUDP + " and numero_std='" + ((String)campoUno.get(iDB)).trim() + "'" ;
						sqlUDP= sqlUDP + " and fl_canc='N' ";


						pstmt = connection.prepareStatement(sqlUDP);
						log.error("Debug: cancellazione logica tb_Fornitore/tbr_editore_num_std");
						log.error("Debug: " + sqlUDP);
						pstmt.executeUpdate();
					}
				}
				for (int iNew = 0; iNew < fornitore.getListaNumStandard().size(); iNew++) {
					Boolean trovato = false;
					for (int iDB = 0; iDB < campoUno.size(); iDB++) {
						if (((TabellaNumSTDImpronteVO)fornitore.getListaNumStandard().get(iNew)).getCampoUno().equals(campoUno.get(iDB))) {
							trovato = true;
							break;
						}
					}
					if (!trovato) {

						// Intervento Settembre 2013 prima di inserire il legame si deve verificare che non sia già presente
						// ma cancellato logicamente; se assente si effettua l'inserimento altrimenti si aggiorna il flag cancellazione
						String sqlRicLegCanc="select editISBN.cod_fornitore, editISBN.numero_std, editISBN.fl_canc from tbr_editore_num_std editISBN ";
						sqlRicLegCanc = this.struttura(sqlRicLegCanc);
						sqlRicLegCanc = sqlRicLegCanc + " editISBN.cod_fornitore='" + fornitore.getCodFornitore() +"'";
						sqlRicLegCanc = sqlRicLegCanc + " and editISBN.numero_std='" + ((TabellaNumSTDImpronteVO)fornitore.getListaNumStandard().get(iNew)).getCampoUno().trim() + "'";;
						sqlRicLegCanc = sqlRicLegCanc + " and editISBN.fl_canc='S'";

						pstmt = connection.prepareStatement(sqlRicLegCanc);
						rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
						int numRigheLegCanc=0;
						while (rs.next()) {
							numRigheLegCanc=numRigheLegCanc+1;
						}


						if (numRigheLegCanc > 0) {
							// se esiste si effettua update
							Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
							String sqlUDP="update tbr_editore_num_std set " ;
							sqlUDP = sqlUDP + "cod_fornitore='" +  fornitore.getCodFornitore().trim().replace("'","''") + "'" ;  //
							sqlUDP = sqlUDP + ",numero_std='" +  ((TabellaNumSTDImpronteVO)fornitore.getListaNumStandard().get(iNew)).getCampoUno().trim() + "'" ;  //
			  				sqlUDP = sqlUDP + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
							sqlUDP = sqlUDP + ", ts_var='" + ts + "'";   // ts_var
							sqlUDP = sqlUDP + ", fl_canc='N'";   // aggiornamento flag di cancellazione
							// CONDIZIONI
							sqlUDP= sqlUDP + " where cod_fornitore='" + fornitore.getCodFornitore().trim() + "'" ;
							sqlUDP= sqlUDP + " and numero_std='" + ((TabellaNumSTDImpronteVO)fornitore.getListaNumStandard().get(iNew)).getCampoUno().trim() + "'" ;
							sqlUDP= sqlUDP + " and fl_canc='S' ";


							pstmt = connection.prepareStatement(sqlUDP);
							log.error("Debug: annullamento cancellazione logica tb_Fornitore/tbr_editore_num_std");
							log.error("Debug: " + sqlUDP);
							pstmt.executeUpdate();
						} else {
							// se NON esiste si effettua insert
							// questo ISBD è presente sul  dati forniti dal Client ma assente dal DB quindi va effettuato l'inserimento
							Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
							String sql="insert into tbr_editore_num_std values ( " ;
							sql=sql + "'" +  fornitore.getCodFornitore().trim().replace("'","''") +"'" ;  // cod_fornitore
							sql=sql + ",'" + ((TabellaNumSTDImpronteVO)fornitore.getListaNumStandard().get(iNew)).getCampoUno().trim() + "'" ;  // bid titolo
							sql=sql + ",'" + fornitore.getUtente() + "'" ;   // ute_ins
							sql=sql + ",'" + ts + "'" ;   // ts_ins
							sql=sql + ",'" + fornitore.getUtente() + "'" ;   // ute_var
							sql=sql + ",'" + ts + "'";   // ts_var
							sql=sql + ",'N'";   // fl_canc
							sql=sql + ")" ;
							pstmt = connection.prepareStatement(sql);
							log.error("Debug: inserimento tb_Fornitore/tbr_editore_num_std");
							log.error("Debug: " + sql);
							pstmt.executeUpdate();
						}
					}
				}
				pstmt.close();
			}

			if (!esistenzaLegame) {
				// non esistono legami Titolo-Editore/Fornitore quindi si può procedere con l'inserimento
				for (int idxIsbn = 0; idxIsbn < fornitore.getListaNumStandard().size(); idxIsbn++) {
					TabellaNumSTDImpronteVO appoTab = (TabellaNumSTDImpronteVO) fornitore.getListaNumStandard().get(idxIsbn);
					Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
					String sql="insert into tbr_editore_num_std values ( " ;
					sql=sql + "'" +  fornitore.getCodFornitore().trim().replace("'","''") +"'" ;  // cod_fornitore
					sql=sql + ",'" +  appoTab.getCampoUno().trim() + "'" ;  // bid titolo
					sql=sql + ",'" + fornitore.getUtente() + "'" ;   // ute_ins
					sql=sql + ",'" + ts + "'" ;   // ts_ins
					sql=sql + ",'" + fornitore.getUtente() + "'" ;   // ute_var
					sql=sql + ",'" + ts + "'";   // ts_var
					sql=sql + ",'N'";   // fl_canc
					sql=sql + ")" ;
					pstmt = connection.prepareStatement(sql);
					log.error("Debug: inserimento tb_Fornitore/tbr_editore_num_std");
					log.error("Debug: " + sql);

					int intRet = 0;
					intRet = pstmt.executeUpdate();
				}

				pstmt.close();
			}

		} catch (Exception e) {
			log.error("", e);
			return false;
		} finally {
			close(connection);
			}
        return true;
	}

	public String  struttura(String sqlString )	{
		int pos=0;
        pos=sqlString.indexOf("where");
         if (pos>0) {
        	 sqlString+=" and ";
         } else {
        	 sqlString+=" where ";
         }

	return sqlString;
	}

}
//
