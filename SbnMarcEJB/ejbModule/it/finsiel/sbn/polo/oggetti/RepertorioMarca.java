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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_marResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_repertorio_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_repertorio_mar;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

public class RepertorioMarca extends Tr_rep_mar{

    /**
	 * 
	 */
	private static final long serialVersionUID = 281289296983107101L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti");
	private boolean filtriValorizzati;

	public RepertorioMarca(){

   }

    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Tr_rep_mar valorizzaFiltri(Tr_rep_mar tr_rep_aut, CercaDatiAutType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return tr_rep_aut;
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
	            tr_rep_aut.settaParametro(TableDao.XXXdata_var_Da,cerca.getT005_Range().getDataDa().toString());
	            tr_rep_aut.settaParametro(TableDao.XXXdata_var_A,cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
	            tr_rep_aut.settaParametro(TableDao.XXXdata_ins_Da,cerca.getT005_Range().getDataDa().toString());
	            tr_rep_aut.settaParametro(TableDao.XXXdata_ins_A,cerca.getT005_Range().getDataA().toString());
        	}
        }
        return tr_rep_aut;
    }



	public boolean esisteLegame(String mid, int citazione, String sig_repertorio) throws IllegalArgumentException, InvocationTargetException, Exception{
        Vl_repertorio_mar vl = new Vl_repertorio_mar();
        vl.setCD_SIG_REPERTORIO(sig_repertorio);
		vl.setPROGR_REPERTORIO(citazione);
        vl.setMID(mid);
		Vl_repertorio_marResult vl_repertorio_marResult = new Vl_repertorio_marResult(vl);


        vl_repertorio_marResult.executeCustom("selectPerRepertorio");
        List v = vl_repertorio_marResult.getElencoRisultati();

		return v.size()>0;

	}

	public List cercaMarcaPerCitazioneRepertorio(int citazione, String sig_repertorio) throws IllegalArgumentException, InvocationTargetException, Exception{
		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
		vl_rep_mar.setCD_SIG_REPERTORIO(sig_repertorio);
		vl_rep_mar.setPROGR_REPERTORIO(citazione);
		Vl_repertorio_marResult vl_repertorio_marResult = new Vl_repertorio_marResult(vl_rep_mar);


        vl_repertorio_marResult.executeCustom("selectPerRepertorioCitazione");
        List TableDaoResult = vl_repertorio_marResult.getElencoRisultati();

        return TableDaoResult;
	}


	public TableDao cercaMarcaPerRepertorio(String idRep) throws IllegalArgumentException, InvocationTargetException, Exception{
		setID_REPERTORIO(Long.parseLong(idRep));
        Tr_rep_marResult tr_marResult = new Tr_rep_marResult(this);


        tr_marResult.executeCustom("selectMarcaPerRepertorio");
        return tr_marResult;
	}



   /**
    * Metodo che ricerca le citazioni nei repertori di una marca:
    * utilizza una vista in join tra tb_repertorio,tr_rep_mar,tb_marca
    * Input: citazione e repertorio
    * Output= elenco delle citazioni in repertori della marca
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
    public int contaLegamiMarcaPerCitRep(int citazione, String idRep, String mid) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
		vl_rep_mar.setCD_SIG_REPERTORIO(idRep);
        vl_rep_mar.setMID(mid);
		vl_rep_mar.setPROGR_REPERTORIO(citazione);
		Vl_repertorio_marResult vl_repertorio_marResult = new Vl_repertorio_marResult(vl_rep_mar);


        vl_repertorio_marResult.executeCustom("countRepertorioPerCitazione");
        int n = conta(vl_repertorio_marResult);


/*        setId_repertorio(idRep);
        setProgr_repertorio(citazione);
        Tr_rep_marResult tr_marResult = new Tr_rep_marResult(this);


        tr_marResult.executeCustom("countRepertorioPerCitazione");
        int n = conta(tr_marResult);

*/
        return n;
    }


    /** Legge da una tavola il valore del COUNT(*) */
    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//    	try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }



   /**
    * Metodo che valida la creazione di un legame tr_rep_mar tra la marca e un
    * repertorio.
    * Controllo che esista il repertorio con tp_repertorio=M,
    * la citazione è obbligatoria.
    * verifica che non esista già un legame repertorio con stessa citazione ad altra
    * marca.
    * Per coerenza con il protocollo SBN, le citazioni in repertorio  di una marca
    * non possono essere più di 3 (da verificare con Iccu)
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public boolean validaPerCreaLegame(LegamiType legame, LegameElementoAutType legamedoc, String mid_modifica) throws IllegalArgumentException, InvocationTargetException, Exception{
        Repertorio repertorio = new Repertorio();
        if (!legamedoc.hasCitazione())
        	throw new EccezioneSbnDiagnostico(3016,"manca la citazione del repertorio");
        repertorio.setTP_REPERTORIO("M");
        Tb_repertorio tavola = repertorio.cercaRepertorioPerCdSig(legamedoc.getIdArrivo());
        if (tavola == null )
            return false;
		int citazione;
		citazione = legamedoc.getCitazione();
        if (contaLegamiMarcaPerCitRep(citazione,legamedoc.getIdArrivo(),mid_modifica) > 0) {
            //Esiste già un legame
            //return false;
            EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3046,"esiste già legame repertorio con stessa citazione ad altra marca");
            ecc.appendMessaggio("Citazione: "+legamedoc.getIdArrivo()+ citazione);
            throw ecc;
        }
		//controllo che non ci siano più di tre citazioni per marca
		if (contaCitazioni(legame.getIdPartenza(),legamedoc.getIdArrivo())>3)
			return false;

        return true;
	}

	/**
	 * Metodo contaCitazioni.
	 * restituisce il numero di citazioni in repertorio  di una marca
	 * INPUT: mid, cd_repertorio
	 * @param String
	 * @return int
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private int contaCitazioni(String mid, String cd_sig_repertorio) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
		vl_rep_mar.setMID(mid);
		vl_rep_mar.setCD_SIG_REPERTORIO(cd_sig_repertorio);
		Vl_repertorio_marResult tavola = new Vl_repertorio_marResult(vl_rep_mar);


        tavola.executeCustom("contaCitazioniInRepertorio");

/*        setMID(mid);
        setId_repertorio(cd_sig_repertorio);
        Tr_rep_marResult tavola = new Tr_rep_marResult(this);


        tavola.executeCustom("contaCitazioniInRepertorio");
*/
        int n = conta(tavola);

        return n;
	}



   /**
    * crea un record nella tavola tr_rep_mar
    * prima si controlla l'esistenza del record dul db, qualora fosse presente con fl_canc = 'S'
    * verrebbe ripristinato
 * @throws InfrastructureException
    */
	public boolean inserisciRepertorioMarca(Tr_rep_mar tr_rep_mar) throws EccezioneDB, InfrastructureException{
   		Tr_rep_marResult tr_rep_marResult = new Tr_rep_marResult(tr_rep_mar);


		if (!controllaEsistenzaLegame(tr_rep_mar)){
			tr_rep_mar.setFL_CANC(" ");
	   		return tr_rep_marResult.insert(tr_rep_mar);
		} else {
			tr_rep_mar.setFL_CANC(" ");
			return tr_rep_marResult.update(tr_rep_mar);
		}
	}


	private boolean controllaEsistenzaLegame(Tr_rep_mar legame) throws EccezioneDB, InfrastructureException{
		boolean esito = true;
		legame.setFL_CANC("S");
		Tr_rep_marResult tavola = new Tr_rep_marResult(legame);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(legame.leggiAllParametro()));
		if (tavola.getElencoRisultati().size() == 0)
			esito = false;

		return esito;
		}

	/**
	 * Method cancellaRepertorioMarca.
	 * @param tr_rep_mar
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean cancellaRepertorioMarca(Tr_rep_mar tr_rep_mar, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		tr_rep_mar.setFL_CANC("S");
		tr_rep_mar.setUTE_VAR(user);
//		tr_rep_mar.setTS_VAR(tr_rep_mar.getTS_VAR());
   		Tr_rep_marResult tr_rep_marResult = new Tr_rep_marResult(tr_rep_mar);

   		return tr_rep_marResult.executeCustomUpdate("updatePerCancella");


	}


	/**
	 * Method contaRepertorioPerMarca.
	 * @param idArrivo
	 * @param _datiElementoAut
	 * @return int
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public int contaRepertorioPerMarca(
		String idArrivo,
		CercaDatiAutType cercaRepertorio) throws IllegalArgumentException, InvocationTargetException, Exception {
        setMID(idArrivo);
		Tr_rep_marResult tavola = new Tr_rep_marResult(this);


        if (!filtriValorizzati)
            valorizzaFiltri(cercaRepertorio);
        tavola.executeCustom("countRepertorioPerMarca");
        int n = conta(tavola);

        return n;
	}

	/**
	 * Method cercaRepertorioPerMarca.
	 * @param idArrivo
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaRepertorioPerMarca(String idArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
        setMID(idArrivo);
        Tr_rep_marResult tr_marResult = new Tr_rep_marResult(this);


        tr_marResult.executeCustom("selectRepertorioPerMarca");
        return tr_marResult;
	}

	/**
	 * Method spostaLegami.
	 * @param idPartenza
	 * @param idArrivo
	 * @param user
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void spostaLegami(
		String idPartenza,
		String idArrivo,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_mar legame = new Tr_rep_mar();
        legame.settaParametro(TableDao.XXXidPartenza, idPartenza);
        legame.settaParametro(TableDao.XXXidArrivo, idArrivo);
        legame.setUTE_VAR(user);
        Tr_rep_marResult tabella = new Tr_rep_marResult(legame);
        tabella.executeCustom("spostaLegami");
	}

    /**
     * Method spostaLegami.
     * @param idPartenza
     * @param idArrivo
     * @param user
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTotaleLegami(String idPartenza, String idArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_mar legame = new Tr_rep_mar();
        legame.setMID(idArrivo);
        Tr_rep_marResult tabella = new Tr_rep_marResult(legame);


        tabella.executeCustom("selectRepertorioPerMarca");
        int count = tabella.getElencoRisultati().size();

        legame.setMID(idPartenza);

        tabella.executeCustom("selectRepertorioPerMarca");
        count += tabella.getElencoRisultati().size();

        return count;
    }

    public void cancellaLegamiRepertorio(String idRepertorio, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        setID_REPERTORIO(Long.parseLong(idRepertorio));
        setUTE_VAR(user);
        Tr_rep_marResult tavola = new Tr_rep_marResult(this);
        tavola.executeCustom("cancellaLegamiRepertorio");
    }

}
