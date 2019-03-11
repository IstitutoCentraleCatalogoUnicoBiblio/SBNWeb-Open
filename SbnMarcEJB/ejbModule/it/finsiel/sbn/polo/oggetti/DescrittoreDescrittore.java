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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_des_desResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_descrittore_desResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_codici;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tr_des_des;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_des;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DescrittoreDescrittore extends Tr_des_des {

	private static final long serialVersionUID = -7815376914454461232L;

	public DescrittoreDescrittore() {
		super();
	}

	public void cancellaLegame(String user,String did1) throws IllegalArgumentException, InvocationTargetException, Exception{
        List TableDaoResult = new ArrayList();
		Tr_des_des tr_des_des = new Tr_des_des();
		tr_des_des.setDID_BASE(did1);
		Tr_des_desResult tr_des_desResult = new Tr_des_desResult(tr_des_des);


		tr_des_desResult.executeCustom("selectLegamePerDid");
		TableDaoResult = tr_des_desResult.getElencoRisultati();

		int ciclo = TableDaoResult.size();
		for (int i = 0; i < ciclo; i++) {
			tr_des_des = (Tr_des_des)TableDaoResult.get(i);
			tr_des_des.setUTE_VAR(user);
			tr_des_des.setFL_CANC("S");
			tr_des_desResult = new Tr_des_desResult(tr_des_des);
			tr_des_desResult.executeCustom("cancellaLegami");
			//almaviva5_20091103 #3282
			//TableDaoResult = tr_des_desResult.getElencoRisultati();
		}

		TableDaoResult = new ArrayList();
		tr_des_des = new Tr_des_des();
		tr_des_des.setDID_COLL(did1);
		tr_des_desResult = new Tr_des_desResult(tr_des_des);

		tr_des_desResult.executeCustom("selectLegamePerDid");
		TableDaoResult = tr_des_desResult.getElencoRisultati();

		int ciclo2 = TableDaoResult.size();
		for (int i = 0; i < ciclo2; i++) {

			tr_des_des = (Tr_des_des)TableDaoResult.get(i);
			tr_des_des.setUTE_VAR(user);
			tr_des_des.setFL_CANC("S");
			tr_des_desResult = new Tr_des_desResult(tr_des_des);
			tr_des_desResult.executeCustom("cancellaLegami");
			//almaviva5_20091103 #3282
			//TableDaoResult = tr_des_desResult.getElencoRisultati();
		}
	}

	public List<Tr_des_des> cercaLegame(String did) throws IllegalArgumentException, InvocationTargetException, Exception{

		Tr_des_des tr_des_des = new Tr_des_des();
		tr_des_des.setDID_BASE(did);
		tr_des_des.setDID_COLL(did);
		Tr_des_desResult tr_des_desResult = new Tr_des_desResult(tr_des_des);

		tr_des_desResult.executeCustom("cercaLegamiPerDid");
		List<Tr_des_des> TableDaoResult = tr_des_desResult.getElencoRisultati();

		return TableDaoResult;
	}

	public boolean cercaLegameCount(String did) throws IllegalArgumentException, InvocationTargetException, Exception{

        List TableDaoResult = new ArrayList();
		Tr_des_des tr_des_des = new Tr_des_des();
		tr_des_des.setDID_BASE(did);
		tr_des_des.setDID_COLL(did);
		Tr_des_desResult tr_des_desResult = new Tr_des_desResult(tr_des_des);

		tr_des_desResult.executeCustom("cercaLegamiPerDid");
		TableDaoResult = tr_des_desResult.getElencoRisultati();

		return ValidazioneDati.isFilled(TableDaoResult);
	}

	public boolean cercaDescrittoreDescrittore(String descrittoreArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {

		Vl_descrittore_des vl_descrittore_des = new  Vl_descrittore_des();
		vl_descrittore_des.setDID_1(descrittoreArrivo);
		Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(vl_descrittore_des);

		tavola.executeCustom("selectDescrittorePerRinvii");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

		return ValidazioneDati.isFilled(risultato);
   }

	public boolean cercaDescrittoreDescrittore(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {

		Tr_des_des tr_des_des = new  Tr_des_des();
		tr_des_des.setDID_BASE(des1);
		tr_des_des.setDID_COLL(des2);
		Tr_des_desResult tavola = new Tr_des_desResult(tr_des_des);

		tavola.executeCustom("cercaDescrittoreDescrittore");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

		return ValidazioneDati.isFilled(risultato);
   }
	public int countLegamiDescrittoreDescrittoreTabella(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {

		Tr_des_des tr_des_des = new  Tr_des_des();
		tr_des_des.setDID_BASE(des1);
		tr_des_des.setDID_COLL(des2);
		Tr_des_desResult tavola = new Tr_des_desResult(tr_des_des);

		tavola.executeCustom("cercaLegamiDescrittoreDescrittore");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

		return ValidazioneDati.size(risultato);
   }
	public int countLegamiDescrittoreDescrittore(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {

		Vl_descrittore_des tr_des_des = new  Vl_descrittore_des();
		tr_des_des.setDID(des1);
		tr_des_des.setDID_1(des2);
		Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(tr_des_des);

		tavola.executeCustom("cercaLegamiDescrittoreDescrittore");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

		return ValidazioneDati.size(risultato);
   }

	/**
	 * Method inserisciInTr_des_des.
	 * @param t001
	 * @param legamiType
	 * @param codiceUtente
	 * @throws InfrastructureException
	 */
	public void inserisciLegame(String t001, LegamiType legamiType,
			String codiceUtente) throws EccezioneDB, InfrastructureException {
		// in fase di creazione mi fermo al primo livello
		ArrivoLegame[] arrivoLegame = null;
		String did_coll = null;
		String tp_legame = legamiType.getArrivoLegame(0).getLegameElementoAut().getTipoLegame().toString();
		arrivoLegame = legamiType.getArrivoLegame();
		did_coll = arrivoLegame[0].getLegameElementoAut().getIdArrivo();
		inserisciLegame(t001, did_coll, tp_legame, codiceUtente);
		// ORIGINALE che funzionave male con le viste
		// if (tp_legame.equals("USE"))
		// tp_legame = "UF";
		// else if (tp_legame.equals("NT"))
		// tp_legame = "BT";

		// almaviva5_20090220 tipo legame inverso letto da tb_codici
		Tb_codici confLegame = Decodificatore.getTb_codici("LEDD", tp_legame);
		String cd_flg8 = confLegame.getCd_flg8();
		String tp_legame_inv = cd_flg8.trim();

		// MODIFICATO Inserisco il legami nei due versi e tolgo la duplicazione
		// su la vista v_des_des

		inserisciLegame(did_coll, t001, tp_legame_inv, codiceUtente);

	}

	private void inserisciLegame(
	String t001,
	String did_coll,
	String tp_legame,
	String codiceUtente) throws EccezioneDB, InfrastructureException{
        Tr_des_des tr_des_des = new Tr_des_des();
        tr_des_des.setDID_BASE(t001);
        tr_des_des.setDID_COLL(did_coll);
        tr_des_des.setFL_CANC(" ");
        tr_des_des.setTP_LEGAME(tp_legame);
        tr_des_des.setUTE_INS(codiceUtente);
        tr_des_des.setUTE_VAR(codiceUtente);
		Tr_des_desResult tavola = new Tr_des_desResult(tr_des_des);

		tavola.insert(tr_des_des);

	}





	public void cancellaTr_des_des(
		String 			t001,
		LegamiType 		legamiType,
		String 			codiceUtente,
		TimestampHash	timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
			String timeStamp = null;
			ArrivoLegame[] arrivoLegame = null;
			String did_coll = null;
			arrivoLegame = legamiType.getArrivoLegame();
			did_coll = arrivoLegame[0].getLegameElementoAut().getIdArrivo();
			timeStamp = timeHash.getTimestamp("Tr_des_des",t001+did_coll);
			cancellaDescrittoreDes(t001,did_coll,codiceUtente, timeStamp);
			timeStamp = timeHash.getTimestamp("Tr_des_des",did_coll+t001);
			cancellaDescrittoreDes(did_coll,t001,codiceUtente, timeStamp);
	}

	private void cancellaDescrittoreDes(
	String t001,
	String did_coll,
	String codiceUtente,
	String timeStamp) throws IllegalArgumentException, InvocationTargetException, Exception{
			setDID_BASE(t001);
			setDID_COLL(did_coll);
			setFL_CANC("S");
			setUTE_VAR(codiceUtente);
			setTS_VAR(ConverterDate.SbnDataVarToDate(timeStamp));
			Tr_des_desResult tavola = new Tr_des_desResult(this);
			tavola.executeCustom("cancellaLegameDescrittore");
	}
    /**
     * Method inserisciInTr_des_des.
     * @param t001
     * @param legamiType
     * @param codiceUtente
     * @throws InfrastructureException
     */
    public void modificaInTr_des_des(
        String      t001,
        LegamiType  legamiType,
        String      codiceUtente) throws EccezioneDB, InfrastructureException {
            //in fase di creazione mi fermo al primo livello
            ArrivoLegame[] arrivoLegame = null;
            String did_coll = null;
            String tp_legame = legamiType.getArrivoLegame(0).getLegameElementoAut().getTipoLegame().toString();
            arrivoLegame = legamiType.getArrivoLegame();
            did_coll = arrivoLegame[0].getLegameElementoAut().getIdArrivo();
            modificaLegame(t001,did_coll,tp_legame,codiceUtente);
            // ORIGINALE che funzionave male con le viste
//            if (tp_legame.equals("USE"))
//                tp_legame = "UF";
//            else if (tp_legame.equals("NT"))
//                tp_legame = "BT";

			//almaviva5_20090220 tipo legame inverso letto da tb_codici
			Tb_codici confLegame = Decodificatore.getTb_codici("LEDD", tp_legame);
            String cd_flg8 = confLegame.getCd_flg8();
            String tp_legame_inv = cd_flg8.trim();

            modificaLegame(did_coll,t001,tp_legame_inv,codiceUtente);
    }

    private void modificaLegame(
    String t001,
    String did_coll,
    String tp_legame,
    String codiceUtente) throws EccezioneDB, InfrastructureException{
        Tr_des_des tr_des_des = new Tr_des_des();
        tr_des_des.setDID_BASE(t001);
        tr_des_des.setDID_COLL(did_coll);
        tr_des_des.setFL_CANC(" ");
        tr_des_des.setTP_LEGAME(tp_legame);
        tr_des_des.setUTE_INS(codiceUtente);
        tr_des_des.setUTE_VAR(codiceUtente);
        Tr_des_desResult tavola = new Tr_des_desResult(tr_des_des);

        tavola.update(tr_des_des);

    }
//    public void scambioForma(Tb_descrittore aut1, String vid2, String ute_var,
//			TimestampHash timeHash) throws IllegalArgumentException,
//			InvocationTargetException, Exception {
//		// 'SCAMBIO FORMA' occorre verificare che non ci
//		// siano tr_aut_bib con fl_allinea diverso da spazio
//		AutoreBiblioteca autBib = new AutoreBiblioteca();
//		if (autBib.cercaFlagAllineaDiverso(aut1.getDID(), " ").size() > 0) {
//			throw new EccezioneSbnDiagnostico(3312);
//		}
//		if (autBib.cercaFlagAllineaDiverso(vid2, " ").size() > 0) {
//			throw new EccezioneSbnDiagnostico(3312);
//		}
//		// aggiorna ute_var e timestamp_var nella relazione
//		Tr_des_des tr_des_des = new Tr_des_des();
//		List resultTableDao;
//		tr_des_des.settaParametro(TableDao.XXXvid_1, aut1.getDID());
//		tr_des_des.settaParametro(TableDao.XXXvid_2, vid2);
//		tr_des_des.setUTE_VAR(ute_var);
//		tr_des_des.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash
//				.getTimestamp("Tr_aut_aut", aut1.getDID() + vid2)));
//		Tr_des_desResult tr_des_desResult = new Tr_des_desResult(tr_des_des);
//		tr_des_desResult.executeCustomUpdate("updateModifica");
//
//		// DEVO ESEGUIRE LO SCAMBIO EFFETTIVO: modificare i due autori.
//		// rifaccio la select (se no diventa un casino l'update).
//		Descrittore descrittore = new Descrittore();
//		// resultTableDao = autore.cercaDueAutoriPerID(vid1, vid2);
//		TableDao tavola = descrittore.cercaDescrittorePerId(vid2);
//		resultTableDao = tavola.getElencoRisultati();
//
//		if (resultTableDao.size() != 1)
//			throw new EccezioneSbnDiagnostico(3013, "Autore non presente");
//
//		Tb_descrittore tb_autore2 = (Tb_ autore) resultTableDao.get(0);
//		// String tipo_aut = aut1.getTp_forma_aut();
//		String vid = aut1.getVID();
//		// aut1.setTp_forma_aut(tb_autore2.getTp_forma_aut());
//		aut1.setVID(tb_autore2.getVID());
//		aut1.setVID_LINK(vid);
//		aut1.setUTE_VAR(ute_var);
//		aut1.setTS_VAR(tb_autore2.getTS_VAR());
//		// tb_autore2.setTp_forma_aut(tipo_aut);
//		tb_autore2.setVID(vid);
//		tb_autore2.setVID_LINK(aut1.getVID());
//		tb_autore2.setUTE_VAR(ute_var);
//		tb_autore2.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash
//				.getTimestamp("Tb_Autore", vid)));
//		autore.eseguiUpdateScambioForma(aut1, tb_autore2);
//		AllineamentoAutore allineaAut = new AllineamentoAutore(aut1);
//		allineaAut.setTb_autore_scambio(true);
//		new AutoreAllineamento().aggiornaFlagAllineamento(allineaAut, ute_var);
//		// /Devo risettare per non incasinare l'allineamento del primo autore
//		aut1.setVID(vid);
//	}

	public TableDao cercaDescrittorePerLegamiDescrittore(String did_base) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_descrittore_des desDes = new Vl_descrittore_des();
		desDes.setDID_1(did_base);
		desDes.setFL_CANC(" ");
		Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(desDes);

		tavola.executeCustom("cercaDescrittorePerLegami");
        return tavola;
	}

	public void legameRinvio2legameStorico(String did, SbnEdizioneSoggettario edizioneIdPartenza, String cd_utente) throws Exception {
		List<Tr_des_des> legami = cercaLegame(did);
		if (!ValidazioneDati.isFilled(legami))
			return;

		Descrittore d = new Descrittore();
		Tr_des_des first = ValidazioneDati.first(legami);
		String idArrivo = ValidazioneDati.equals(first.getDID_BASE(), did) ? first.getDID_COLL() : first.getDID_BASE();
		Tb_descrittore des_arrivo = d.cercaDescrittorePerId(idArrivo);
		if (des_arrivo == null)
			throw new EccezioneSbnDiagnostico(3001);	//elemento non trovato

		if (ValidazioneDati.in(des_arrivo.getCD_EDIZIONE(), edizioneIdPartenza.toString(),
				SbnEdizioneSoggettario.E.toString()) )
			throw new EccezioneSbnDiagnostico(3348, true, new String[]{did, idArrivo} );	//errore legame

		String tpLegame;
		Timestamp ts = TableDao.now();
		for (Tr_des_des legame : legami) {
			legame.setUTE_VAR(cd_utente);
			legame.setTS_VAR(ts);
			switch (edizioneIdPartenza.getType()) {
			case SbnEdizioneSoggettario.I_TYPE:
				//legame UF/USE --> HSF/HSEE
				tpLegame = ValidazioneDati.trimOrEmpty(legame.getTP_LEGAME());
				if (ValidazioneDati.equals(tpLegame, SbnLegameAut.valueOf("UF").toString()) )	//usato al posto di
					legame.setTP_LEGAME(SbnLegameAut.valueOf("HSF").toString());
				else
					if (ValidazioneDati.equals(tpLegame, SbnLegameAut.valueOf("USE").toString()) )	//vedi
						legame.setTP_LEGAME(SbnLegameAut.valueOf("HSEE").toString());
				break;

			case SbnEdizioneSoggettario.N_TYPE:
				//legame UF/USE --> HSEE/HSF
				tpLegame = ValidazioneDati.trimOrEmpty(legame.getTP_LEGAME());
				if (ValidazioneDati.equals(tpLegame, SbnLegameAut.valueOf("UF").toString()) )	//usato al posto di
					legame.setTP_LEGAME(SbnLegameAut.valueOf("HSEE").toString());
				else
					if (ValidazioneDati.equals(tpLegame, SbnLegameAut.valueOf("USE").toString()) )	//vedi
						legame.setTP_LEGAME(SbnLegameAut.valueOf("HSF").toString());
				break;

			default:
				throw new EccezioneSbnDiagnostico(3214); //tipo legame non corretto
			}

			Tr_des_desResult dao = new Tr_des_desResult(legame);
			dao.insert(legame);
		}

	}

	public List<Tr_des_des> getLegamiStorici(String did) throws Exception {
		//almaviva5_20120504 evolutive CFI
		List<Tr_des_des> output = new ArrayList<Tr_des_des>();

		List<Tr_des_des> legami = cercaLegame(did);
		if (!ValidazioneDati.isFilled(legami))
			return output;

		for (Tr_des_des legame : legami) {
			//almaviva5_20160404 #6144 esclusione legami cancellati
			if (ValidazioneDati.in(legame.getFL_CANC(), "s", "S"))
				continue;

			//check legame storico
			String tpLegame = ValidazioneDati.trimOrEmpty(legame.getTP_LEGAME() );
			Tb_codici cod = Decodificatore.getTb_codici("LEDD", tpLegame);
			if (cod == null)
				throw new EccezioneSbnDiagnostico(3214); //tipo legame non corretto

			if (ValidazioneDati.equals(cod.getCd_flg9(), "S"))
				output.add(legame);
		}

		return output;
	}

	public int countLegamiStorici(String did) throws Exception {
		//almaviva5_20120504 evolutive CFI
		List<Tr_des_des> legami = getLegamiStorici(did);
		return ValidazioneDati.size(legami);
	}

}
