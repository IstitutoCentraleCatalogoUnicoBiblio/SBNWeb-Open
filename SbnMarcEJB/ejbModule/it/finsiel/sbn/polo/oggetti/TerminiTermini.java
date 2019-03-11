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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_termini_terminiResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_termini_terminiResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tr_termini_termini;
import it.finsiel.sbn.polo.orm.viste.Vl_termini_termini;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;

import java.lang.reflect.InvocationTargetException;
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
public class TerminiTermini extends Tr_termini_termini {

	private static final long serialVersionUID = 5135101224488271544L;

	public TerminiTermini() {
	}

	public void cancellaLegame(String user,String did1) throws IllegalArgumentException, InvocationTargetException, Exception{
        List TableDaoResult = new ArrayList();
		Tr_termini_termini tr_termini_termini = new Tr_termini_termini();
		tr_termini_termini.setDID_BASE(did1);
		Tr_termini_terminiResult tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);


		tr_termini_terminiResult.executeCustom("selectLegamePerDid");
		TableDaoResult = tr_termini_terminiResult.getElencoRisultati();

		int ciclo = TableDaoResult.size();
		for (int i=0; i<ciclo;i++){
			tr_termini_termini = (Tr_termini_termini)TableDaoResult.get(i);
			tr_termini_termini.setUTE_VAR(user);
			tr_termini_termini.setFL_CANC("S");
			tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);
			tr_termini_terminiResult.executeCustom("cancellaLegami");
			TableDaoResult = tr_termini_terminiResult.getElencoRisultati();

		}
		TableDaoResult = new ArrayList();
		tr_termini_termini = new Tr_termini_termini();
		tr_termini_termini.setDID_COLL(did1);
		tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);


		tr_termini_terminiResult.executeCustom("selectLegamePerDid");
		TableDaoResult = tr_termini_terminiResult.getElencoRisultati();

		int ciclo2 = TableDaoResult.size();
		for (int i=0; i<ciclo2;i++){

			tr_termini_termini = (Tr_termini_termini)TableDaoResult.get(i);
			tr_termini_termini.setUTE_VAR(user);
			tr_termini_termini.setFL_CANC("S");
			tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);
			tr_termini_terminiResult.executeCustom("cancellaLegami");
			TableDaoResult = tr_termini_terminiResult.getElencoRisultati();

		}
	}

	public List cercaLegame(String did) throws IllegalArgumentException, InvocationTargetException, Exception{
        List TableDaoResult = new ArrayList();
		Tr_termini_termini tr_termini_termini = new Tr_termini_termini();
		tr_termini_termini.setDID_BASE(did);
		tr_termini_termini.setDID_COLL(did);
		Tr_termini_terminiResult tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);


		tr_termini_terminiResult.executeCustom("cercaLegamiPerDid");
		TableDaoResult = tr_termini_terminiResult.getElencoRisultati();

		return TableDaoResult;
	}

	public boolean cercaLegameCount(String did) throws IllegalArgumentException, InvocationTargetException, Exception{
		boolean trovato = true;
        List TableDaoResult = new ArrayList();
		Tr_termini_termini tr_termini_termini = new Tr_termini_termini();
		tr_termini_termini.setDID_BASE(did);
		tr_termini_termini.setDID_COLL(did);
		Tr_termini_terminiResult tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);


		tr_termini_terminiResult.executeCustom("cercaLegamiPerDid");
		TableDaoResult = tr_termini_terminiResult.getElencoRisultati();

		if (TableDaoResult.size()==0)
			trovato=false;
    	return trovato;
	}

	public boolean cercaTerminiTermini(String termineArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
		boolean trovato = true;
		Vl_termini_termini vl_termini_termini = new  Vl_termini_termini();
		vl_termini_termini.setDID_1(termineArrivo);
		Vl_termini_terminiResult tavola = new Vl_termini_terminiResult(vl_termini_termini);


		tavola.executeCustom("selectTerminiPerRinvii");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

		if (risultato.size()==0)
			trovato=false;
    	return trovato;
   }
	public List cercaTermineTermine(String termineArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
		boolean trovato = true;
		Vl_termini_termini vl_termini_termini = new  Vl_termini_termini();
		vl_termini_termini.setDID_1(termineArrivo);
		Vl_termini_terminiResult tavola = new Vl_termini_terminiResult(vl_termini_termini);


		tavola.executeCustom("selectTerminiPerRinvii");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

    	return risultato;
   }

	public boolean cercaTerminiTermini(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {
		boolean trovato = true;
		Tr_termini_termini tr_termini_termini = new  Tr_termini_termini();
		tr_termini_termini.setDID_BASE(des1);
		tr_termini_termini.setDID_COLL(des2);
		Tr_termini_terminiResult tavola = new Tr_termini_terminiResult(tr_termini_termini);


		tavola.executeCustom("cercaTerminiTermini");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

		if (risultato.size()==0)
			trovato=false;
    	return trovato;
   }

	public List<Tr_termini_termini> getTerminiTermini(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_termini_termini tr_termini_termini = new  Tr_termini_termini();
		tr_termini_termini.setDID_BASE(des1);
		tr_termini_termini.setDID_COLL(des2);
		Tr_termini_terminiResult tavola = new Tr_termini_terminiResult(tr_termini_termini);

		tavola.executeCustom("cercaTerminiTermini");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

    	return risultato;
   }

	public int countLegamiTerminiTerminiTabella(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_termini_termini tr_termini_termini = new  Tr_termini_termini();
		tr_termini_termini.setDID_BASE(des1);
		tr_termini_termini.setDID_COLL(des2);
		Tr_termini_terminiResult tavola = new Tr_termini_terminiResult(tr_termini_termini);


		tavola.executeCustom("cercaLegamiTerminiTermini");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

    	return risultato.size();
   }
	public int countLegamiTerminiTermini(String des1, String des2) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_termini_termini vl_termini_termini = new  Vl_termini_termini();
		vl_termini_termini.setDID(des1);
		vl_termini_termini.setDID_1(des2);
		Vl_termini_terminiResult tavola = new Vl_termini_terminiResult(vl_termini_termini);
		// non esisteva dava errore tavola.executeCustom("cercaLegamiTerminiTermini");
		tavola.executeCustom("cercaLegamiTerminiTermini");
        List risultato = new ArrayList();
		risultato = tavola.getElencoRisultati();

    	return risultato.size();
   }

	/**
	 * Method inserisciInTr_termini_termini.
	 * @param t001
	 * @param legamiType
	 * @param codiceUtente
	 * @throws InfrastructureException
	 */
	public void inserisciInTr_termini_termini(
		String 		t001,
		LegamiType 	legamiType,
		String 		codiceUtente) throws EccezioneDB, InfrastructureException {
			//in fase di creazione mi fermo al primo livello
			ArrivoLegame[] arrivoLegame = null;
			String did_coll = null;
			String tipo_coll = legamiType.getArrivoLegame(0).getLegameElementoAut().getTipoLegame().toString();
			arrivoLegame = legamiType.getArrivoLegame();
			did_coll = arrivoLegame[0].getLegameElementoAut().getIdArrivo();
			inserisciLegame(t001,did_coll,tipo_coll,codiceUtente);
			String tipo_coll_inv = "";
            if (tipo_coll.equals("USE"))
            	tipo_coll_inv = "UF";
            else if (tipo_coll.equals("UF"))
            	tipo_coll_inv = "USE";


            if (tipo_coll.equals("NT"))
            	tipo_coll_inv = "BT";
            else if (tipo_coll.equals("BT"))
            	tipo_coll_inv = "NT";
            else if (tipo_coll.equals("RT"))
            	tipo_coll_inv = "RT";

			inserisciLegame(did_coll,t001,tipo_coll_inv,codiceUtente);

	}

	private void inserisciLegame(
	String t001,
	String did_coll,
	String tipo_coll,
	String codiceUtente) throws EccezioneDB, InfrastructureException{
        Tr_termini_termini tr_termini_termini = new Tr_termini_termini();
        tr_termini_termini.setDID_BASE(t001);
        tr_termini_termini.setDID_COLL(did_coll);
        tr_termini_termini.setFL_CANC(" ");
        tr_termini_termini.setTIPO_COLL(tipo_coll);
        tr_termini_termini.setUTE_INS(codiceUtente);
        tr_termini_termini.setUTE_VAR(codiceUtente);
		Tr_termini_terminiResult tavola = new Tr_termini_terminiResult(tr_termini_termini);

		tavola.insert(tr_termini_termini);

	}





	public void cancellaTr_termini_termini(
		String 			t001,
		LegamiType 		legamiType,
		String 			codiceUtente,
		TimestampHash	timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
			String timeStamp = null;
			ArrivoLegame[] arrivoLegame = null;
			String did_coll = null;
			arrivoLegame = legamiType.getArrivoLegame();
			did_coll = arrivoLegame[0].getLegameElementoAut().getIdArrivo();
			timeStamp = timeHash.getTimestamp("Tr_termini_termini",t001+did_coll);
			cancellaTerminiTermini(t001,did_coll,codiceUtente, timeStamp);
			timeStamp = timeHash.getTimestamp("Tr_termini_termini",did_coll+t001);
			cancellaTerminiTermini(did_coll,t001,codiceUtente, timeStamp);
	}

	private void cancellaTerminiTermini(
	String t001,
	String did_coll,
	String codiceUtente,
	String timeStamp) throws IllegalArgumentException, InvocationTargetException, Exception{
			setDID_BASE(t001);
			setDID_COLL(did_coll);
			setFL_CANC("S");
			setUTE_VAR(codiceUtente);
			setTS_VAR(ConverterDate.SbnDataVarToDate(timeStamp));
			Tr_termini_terminiResult tavola = new Tr_termini_terminiResult(this);
			tavola.executeCustom("cancellaLegameTermini");
	}
    /**
     * Method inserisciInTr_termini_termini.
     * @param t001
     * @param legamiType
     * @param codiceUtente
     * @throws InfrastructureException
     */
    public void modificaInTr_termini_termini(
        String      t001,
        LegamiType  legamiType,
        String      codiceUtente) throws EccezioneDB, InfrastructureException {
            //in fase di creazione mi fermo al primo livello
            ArrivoLegame[] arrivoLegame = null;
            String did_coll = null;
            String tipo_coll = legamiType.getArrivoLegame(0).getLegameElementoAut().getTipoLegame().toString();
            arrivoLegame = legamiType.getArrivoLegame();
            did_coll = arrivoLegame[0].getLegameElementoAut().getIdArrivo();
            modificaLegame(t001,did_coll,tipo_coll,codiceUtente);
            // ORIGINALE che funzionave male con le viste
//            if (tipo_coll.equals("USE"))
//                tipo_coll = "UF";
//            else if (tipo_coll.equals("NT"))
//                tipo_coll = "BT";


            // MODIFICATO Inserisco il legami nei due versi e tolgo la duplicazione
            // su la vista v_des_des
            String tipo_coll_inv = "";
            if (tipo_coll.equals("USE"))
            	tipo_coll_inv = "UF";
            else if (tipo_coll.equals("UF"))
            	tipo_coll_inv = "USE";


            if (tipo_coll.equals("NT"))
            	tipo_coll_inv = "BT";
            else if (tipo_coll.equals("BT"))
            	tipo_coll_inv = "NT";

            modificaLegame(did_coll,t001,tipo_coll_inv,codiceUtente);
    }

    private void modificaLegame(
    String t001,
    String did_coll,
    String tipo_coll,
    String codiceUtente) throws EccezioneDB, InfrastructureException{
        Tr_termini_termini tr_termini_termini = new Tr_termini_termini();
        tr_termini_termini.setDID_BASE(t001);
        tr_termini_termini.setDID_COLL(did_coll);
        tr_termini_termini.setFL_CANC(" ");
        tr_termini_termini.setTIPO_COLL(tipo_coll);
        tr_termini_termini.setUTE_INS(codiceUtente);
        tr_termini_termini.setUTE_VAR(codiceUtente);
        Tr_termini_terminiResult tavola = new Tr_termini_terminiResult(tr_termini_termini);

        tavola.update(tr_termini_termini);

    }

  public void scambioForma(Tb_termine_thesauro termine1, String vid2,
			String ute_var, TimestampHash timeHash)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		// 'SCAMBIO FORMA' occorre verificare che non ci
		// siano tr_aut_bib con fl_allinea diverso da spazio
		AutoreBiblioteca autBib = new AutoreBiblioteca();
		//DA VERIFICARE SE REINSERIRE
//		if (autBib.cercaFlagAllineaDiverso(termine1.getDID(), " ").size() > 0) {
//			throw new EccezioneSbnDiagnostico(3312);
//		}
//		if (autBib.cercaFlagAllineaDiverso(vid2, " ").size() > 0) {
//			throw new EccezioneSbnDiagnostico(3312);
//		}
		// aggiorna ute_var e timestamp_var nella relazione
		Tr_termini_termini tr_termini_termini = new Tr_termini_termini();
		List resultTableDao;
		tr_termini_termini.settaParametro(TableDao.XXXdid_1, termine1.getDID());
		tr_termini_termini.settaParametro(TableDao.XXXdid_2, vid2);
		tr_termini_termini.setUTE_VAR(ute_var);
		tr_termini_termini.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_termini_termini", termine1.getDID() + vid2)));
		Tr_termini_terminiResult tr_termini_terminiResult = new Tr_termini_terminiResult(tr_termini_termini);
		tr_termini_terminiResult.executeCustomUpdate("updateModifica");

		// DEVO ESEGUIRE LO SCAMBIO EFFETTIVO: modificare i due autori.
		// rifaccio la select (se no diventa un casino l'update).
		TermineThesauro termineThesauro = new TermineThesauro();
		// resultTableDao = autore.cercaDueAutoriPerID(vid1, vid2);
		TableDao tavola = termineThesauro.cercaTerminePerIdInTavola(vid2);
		resultTableDao = tavola.getElencoRisultati();

		if (resultTableDao.size() != 1)
			throw new EccezioneSbnDiagnostico(3013,
					"Termine thesauro non presente");

		Tb_termine_thesauro tb_termine_thesauro2 = (Tb_termine_thesauro) resultTableDao.get(0);
		// String tipo_aut = aut1.getTp_forma_aut();
		String did = termine1.getDID();
		// aut1.setTp_forma_aut(tb_autore2.getTp_forma_aut());
		termine1.setDID(tb_termine_thesauro2.getDID());
		//termine1.setDID_LINK(vid);
		termine1.setUTE_VAR(ute_var);
		termine1.setTS_VAR(tb_termine_thesauro2.getTS_VAR());
		// tb_autore2.setTp_forma_aut(tipo_aut);
		tb_termine_thesauro2.setDID(did);
		//tb_termine_thesauro2.setVID_LINK(termine1.getVID());
		tb_termine_thesauro2.setUTE_VAR(ute_var);
		tb_termine_thesauro2.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_Termine_thesauro", did)));

		termineThesauro.eseguiUpdateScambioForma(termine1, tb_termine_thesauro2);
// NON SO COSA FA DA VERIFICARE
//		AllineamentoAutore allineaAut = new AllineamentoAutore(termine1);
//		allineaAut.setTb_autore_scambio(true);
//		new AutoreAllineamento().aggiornaFlagAllineamento(allineaAut, ute_var);
//		// /Devo risettare per non incasinare l'allineamento del primo autore
//		termine1.setDID(did);
	}


}






