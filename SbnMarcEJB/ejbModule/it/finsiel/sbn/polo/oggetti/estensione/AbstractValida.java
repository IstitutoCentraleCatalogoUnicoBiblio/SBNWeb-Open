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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.polo.dao.entity.tavole.Tr_sog_desResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Abstract;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 */
public class AbstractValida extends Abstract{


	private static final long serialVersionUID = -493337641161271843L;
	private static Category 	log = Category.getInstance("iccu.sbnmarcserver.AbstractValida");



	public AbstractValida(){
		super();
	}


	/**
	 * metodo utilizzato in fase di modifica:
	 * si controlla che il livello in input  sia minore di quello dell'oggetto nel db
	 */
	public boolean verificaLivello(
	Tb_abstract tb_abstract,
	String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico {
        if(_cattura)
            return true;
		if (Integer.parseInt(livelloAut)<Integer.parseInt(tb_abstract.getCD_LIVELLO()))
				throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
		return true;
	}

	private void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico{
		// almaviva ATTENZIONE NON ESISTE UNA VOCE SPECIFICA PER GLI ABSTRACT
		// ASSOCIO LE AUTORIZZAZIONE DEL SOGGETTO COME SE FOSSERO DELL'ABSTRACT
		// DEVE ESSERE CAMBIATO IN FASE DI PROFILAZIONE
	      Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "SO");
	      if(!_cattura){
	          if (par == null)
	             throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
	          String livelloUtente = par.getCd_livello();
	          if (par.getTp_abil_auth()!='S')
	             throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
	          if (Integer.parseInt(livelloAut) > Integer.parseInt(livelloUtente))
	             throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
	      }
		}

	/**
	 * Method validaPerCancella.
	 * controllo di esistenza per identificativo,
	 * @param _idCancellazione
	 * @return Tb_abstract
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_abstract validaPerCancella(String utente, String idCancellazione,TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
	    Tb_abstract abstrcatTrovato = null;
	    abstrcatTrovato = cercaAbstractPerId(idCancellazione);
		if (abstrcatTrovato == null)
			throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
		timeHash.putTimestamp("Tb_abstract",idCancellazione,new SbnDatavar( abstrcatTrovato.getTS_VAR()));
		controllaParametriUtente(utente,abstrcatTrovato.getCD_LIVELLO(),false);
		return abstrcatTrovato;
	}

	/**
	 * Method controllalegami.
	 * @param _idCancellazione
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void controllalegami(String did) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_sog_des tr_sog_des = new Tr_sog_des();
		tr_sog_des.setDID(did);
		Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);


		tavola.executeCustom("selectLegameDescrittore");
        List vec = tavola.getElencoRisultati();

		if (vec.size() >0)
			throw new EccezioneSbnDiagnostico(3130,"esistono legami con alcuni soggetti");

	}


	/**
	 * @param bid
	 * @param livello
	 * @param vettoreParametriSemantici
	 * questo metodo riceve in input un vettore di parametri semantici (calcolato tramite il Validator)
	 *
	 * l'utente deve essere abilitato alla gestione abstract
	 *
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean validaPerCrea(
	String bid,
	String livello,
	String utente,
    boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {

		controllaParametriUtente(utente,livello,_cattura);

		Tb_abstract abstractTrovato = null;

		if (bid.equals("0000000000")){
			throw new EccezioneSbnDiagnostico(6666,"bid di associazione non valido");
		}
		abstractTrovato = verificaEsistenza(bid);
		if (abstractTrovato != null)
			throw new EccezioneSbnDiagnostico(3012,"elemento già presente in base dati");
		return true;
	}







    public boolean verificaVersioneAbstract(Tb_abstract tb_abstract, String t005) throws EccezioneSbnDiagnostico {
        if (t005 != null){
            SbnDatavar datavar = new SbnDatavar(tb_abstract.getTS_VAR());
            return datavar.getT005Date().equals(t005);
        }
        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }


	/**
	 * Method validaPerModifica.
	 * @param t001
	 * @param cd_livello
	 * @param t005
	 * @param codiceUtente
	 * @param t931
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_abstract validaPerModifica(
		String 	idDescrittore,
		String 	cd_livello,
		String	t005,
		String 	codiceUtente,
		String 	descrizione,
        boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
			Tb_abstract abstrcatTrovato = new Tb_abstract();
			abstrcatTrovato = cercaAbstractPerId(idDescrittore);
			if (abstrcatTrovato == null)
				throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
			if (!verificaLivello(abstrcatTrovato,cd_livello,_cattura))
				throw new EccezioneSbnDiagnostico(3007,"livello di authority troppo basso");
			controllaParametriUtente(codiceUtente,cd_livello,_cattura);
			verificaVersioneAbstract(abstrcatTrovato,t005);
			return abstrcatTrovato;
	}


}
