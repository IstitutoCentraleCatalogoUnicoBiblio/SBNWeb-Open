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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tr_aut_mar;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/**
 * Classe AutoreMarca
 * Operazioni sul DB per le relazioni tra autore e marca.
 * @author
 * @version 29/11/2002
 */
public class AutoreMarca extends Tr_aut_mar {


    /**
	 * 
	 */
	private static final long serialVersionUID = -7728864306422893851L;

	public AutoreMarca() {

    }

    public TableDao cercaLegame(String idAutore, String idMarca) throws EccezioneDB, InfrastructureException {
        setVID(idAutore);
        setMID(idMarca);
        Tr_aut_marResult tr_autResult = new Tr_aut_marResult(this);


        tr_autResult.valorizzaElencoRisultati(tr_autResult.selectPerKey(this.leggiAllParametro()));
        return tr_autResult;

    }

    public Tr_aut_mar estraiLegame(String idAutore, String idMarca) throws EccezioneDB, InfrastructureException {
        TableDao tavola = cercaLegame(idAutore,idMarca);
        List v = tavola.getElencoRisultati();

        if (v.size()>0)
            return (Tr_aut_mar)v.get(0);
        return null;
    }


    /**
     * Method validaPerCreaLegame.
     * @param elementoAut
     * @return boolean
     * @throws InfrastructureException
     */
    public void validaPerModificaLegame(
        Tb_autore autore,
        ArrivoLegame legame,
        SbnTipoOperazione operazione,
        TimestampHash timeHash,
        boolean cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        if (operazione.getType() == SbnTipoOperazione.CREA_TYPE)
            validaPerCreaLegame(autore, legame);
        else if (operazione.getType() == SbnTipoOperazione.CANCELLA_TYPE)
            validaPerCancellaLegame(autore.getVID(), legame, timeHash);
    }

    /**
     * Verifica la presenza della marca e che esista il legame.
     * @param elementoAut
     * @return boolean
     * @throws InfrastructureException
     */
    public void validaPerCancellaLegame(String vid, ArrivoLegame legame, TimestampHash timeHash)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        Marca marca = new Marca();
        LegameElementoAutType legamedoc = legame.getLegameElementoAut();
        Tb_marca tbm = marca.estraiMarcaPerID(legamedoc.getIdArrivo());
        if (tbm == null)
            throw new EccezioneSbnDiagnostico(3024, "Marca non esistente");
        timeHash.putTimestamp("Tb_marca", tbm.getMID(), new SbnDatavar( tbm.getTS_VAR()));
        TableDao tavola = cercaLegame(vid, legamedoc.getIdArrivo());
        List v = tavola.getElencoRisultati();

        if (v.size() == 0)
            //Non esiste il legame
            throw new EccezioneSbnDiagnostico(3029, "Legame non esistente");
        timeHash.putTimestamp("Tr_aut_mar",vid+tbm.getMID(), new SbnDatavar( tbm.getTS_VAR()));
    }

    /**
     * Verifica la presenza della marca e che non esista già il legame.
     * @param elementoAut
     * @return boolean
     * @throws InfrastructureException
     */
    public void validaPerCreaLegame(Tb_autore autore, ArrivoLegame legame)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        if (!autore.getTP_NOME_AUT().equals("E"))
            throw new EccezioneSbnDiagnostico(3045);

        //Tr_aut_mar tr_aut_aut = new Tr_aut_mar();
        Marca marca = new Marca();

        LegameElementoAutType legamedoc = legame.getLegameElementoAut();
        if (marca.estraiMarcaPerID(legamedoc.getIdArrivo()) == null)
            throw new EccezioneSbnDiagnostico(3024, "Marca non esistente");
        TableDao tavola = cercaLegame(autore.getVID(), legamedoc.getIdArrivo());
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            //Esiste già un legame
            throw new EccezioneSbnDiagnostico(3030, "Legame già esistente");
    }

    /**
     * Method validaPerCreaLegame.utilizzato dal metodo creaMarca
     * @param elementoAut
     * @return boolean
     * @author
     * @throws InfrastructureException
     */
    public boolean esisteLegame(LegamiType legame, LegameElementoAutType legamedoc)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        TableDao tavola = cercaLegame(legamedoc.getIdArrivo(), legame.getIdPartenza());
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            //Esiste già un legame
            return true;
        return false;
    }

    /**
     * Inserisce un legame autore-marca nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean inserisciAutoreMarca(Tr_aut_mar autMar) throws IllegalArgumentException, InvocationTargetException, Exception {
   		Tr_aut_marResult tr_aut_marResult = new Tr_aut_marResult(autMar);


   		autMar.setFL_CANC(" ");
		if (verificaEsistenza(autMar) == null){
	   		autMar.setFL_CANC(" ");
	   		return tr_aut_marResult.insert(autMar);
		} else{
	   		autMar.setFL_CANC(" ");
	   		return tr_aut_marResult.executeCustom("updatePerModifica");
		}

    }

	/**
	 * Method controllaEsistenzaLegame.
	 * @param autMar
	 * @return boolean
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private Tr_aut_mar verificaEsistenza(Tr_aut_mar legame) throws IllegalArgumentException, InvocationTargetException, Exception {
		boolean esito = false;
		Tr_aut_marResult tavola = new Tr_aut_marResult(legame);


		tavola.executeCustom("selectEsistenza");
        List risultato = tavola.getElencoRisultati();

		if (risultato.size() >0)
			return (Tr_aut_mar)risultato.get(0);
		return null;
	}


    /**
     * Method cancellaAutoreMarca.
     * Tr_aut_mar deve avere settati utevar, ts_var vid e mid.
     * @param tr_aut_mar
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean cancellaAutoreMarca(Tr_aut_mar tr_aut_mar, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        tr_aut_mar.setFL_CANC("S");
        tr_aut_mar.setUTE_VAR(user);
        Tr_aut_marResult t_autmar = new Tr_aut_marResult(tr_aut_mar);

        return t_autmar.executeCustom("updateCancella");

    }

    public boolean modificaAutoreMarca(Tr_aut_mar tr_aut_mar, String user) throws EccezioneDB, InfrastructureException {
        tr_aut_mar.setUTE_VAR(user);
        tr_aut_mar.setFL_CANC("N");
        Tr_aut_marResult t_autmar = new Tr_aut_marResult(tr_aut_mar);


        return t_autmar.update(tr_aut_mar);

    }

	public void cancellaLegamiMarche(String idAutore, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
		setVID(idAutore);
    setFL_CANC("S");
		Tr_aut_marResult tavola = new Tr_aut_marResult(this);

        tavola.executeCustom("selectAutoreMarca");
        List vec = tavola.getElencoRisultati();

		Tr_aut_mar tr_aut_mar;
		for (int i=0; i<vec.size();i++){
			tr_aut_mar = new Tr_aut_mar();
			tr_aut_mar = (Tr_aut_mar)vec.get(i);
			tr_aut_mar.setUTE_VAR(user);
			cancellaAutoreMarca(tr_aut_mar,user);
		}
	}

    /**
     * Cancella tutti i legami di una marca
     * @param idAMarca
     * @param user
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancellaLegamiAdAutore(String idMarca, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
        setMID(idMarca);
        setUTE_VAR(user);
        Tr_aut_marResult tavola = new Tr_aut_marResult(this);
        tavola.executeCustom("cancellaLegamePerMid");

    }



	/**
	 * Method spostaLegami tra due autori
	 * @param idPartenza
	 * @param idArrivo
	 * @param user
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void spostaLegamiAutore(
		String idPartenza,
		String idArrivo,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        //Prendo tutti i legami dell'autore
        List legami = cercaLegamiAutore(idPartenza);
        //li cancello tutti.
        cancellaLegamiMarche(idPartenza,user);
        for (int i =0 ; i<legami.size(); i++) {
            //inserisco ad uno ad uno i legami con il nuovo autoer
            Tr_aut_mar legame = (Tr_aut_mar)legami.get(i);
            legame.setVID(idArrivo);
            legame.setUTE_VAR(user);
            Tr_aut_marResult tabella = new Tr_aut_marResult(legame);
            tabella.insert(legame);
        }
	}

    /**
     * Method spostaLegami tra due marche
     * @param idPartenza
     * @param idArrivo
     * @param user
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void spostaLegamiMarca(
        String idPartenza,
        String idArrivo,
        String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        //Prendo tutti i legami della marca
        List legami = cercaLegamiMarca(idPartenza);
        //li cancello tutti.
        cancellaLegamiAdAutore(idPartenza,user);
        for (int i =0 ; i<legami.size(); i++) {
            //inserisco ad uno ad uno i legami con la nuova marca
            Tr_aut_mar legame = (Tr_aut_mar)legami.get(i);
            legame.setMID(idArrivo);
            legame.setUTE_VAR(user);
            Tr_aut_marResult tabella = new Tr_aut_marResult(legame);
            tabella.insert(legame);

        }

    }

    public List cercaLegamiMarca(String mid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setMID(mid);
        Tr_aut_marResult tabella = new Tr_aut_marResult(this);


        tabella.executeCustom("selectPerMarca");
        List v = tabella.getElencoRisultati();

        return v;

    }

    public List cercaLegamiAutore(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setMID(vid);
        setVID(vid);
        Tr_aut_marResult tabella = new Tr_aut_marResult(this);


        tabella.executeCustom("selectPerAutore");
        List v = tabella.getElencoRisultati();

        return v;

    }
}
