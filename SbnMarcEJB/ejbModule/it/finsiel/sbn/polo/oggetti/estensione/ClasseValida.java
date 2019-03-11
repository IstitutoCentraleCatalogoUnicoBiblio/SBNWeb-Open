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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.TermineClasse;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;



/**
 * Valida le informazioni relative a una Classe.
 * Input= DatiElementoAut con tipoAuthority = 'CL'
 * Operazioni di validazione:
 * controllo abilitazione su livello utente/cd_livello
 * controllo esistenza con identificativo: T001 = cd_sistema + cd_edizione + classe
 * controllo versione: T005 = ts_var
 *
 * se cd_sistema='D' (dewey)  in creazione o in modifica del simbolo viene
 * effettuato il controllo formale sulla classe.
 *
 * @author
 */
public class ClasseValida {

	private ClasseType 	_datiElementoAut;
	static Logger log = Logger.getLogger("iccu.sbnmarcserver.ClasseValida");

	/**
	 * costruttore utilizzato in fase di creazione
	 */
	public ClasseValida(ClasseType classeType){

    	_datiElementoAut = classeType;

	}


    public ClasseValida(){

        _datiElementoAut = null;

    }


	private boolean verificaCd_Livello(String livelloAut,Tb_classe tb_classe, String utente)
        throws EccezioneSbnDiagnostico {
        if (Integer.parseInt(livelloAut) < Integer.parseInt(tb_classe.getCD_LIVELLO())) {
        	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
        }
        return true;
    }

	private void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico{
      Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "CL");
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
	 * controllo esistenza con identificativo:
	 * T001 = cd_sistema + cd_edizione + classe
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_classe verificaEsistenzaID(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
		Classe classe = new Classe();
		Tb_classe tb_classe = new Tb_classe();

		tb_classe = classe.verificaEsistenzaID(id);
		return tb_classe;
	}


   /**
    * metodo di validazione per operazione di creazione classe
    * - verificaEsistenzaID: se trovato ritorna diagnostico Classe esistente'
    * - se cd_sistema = 'D' controllaClasseDewey: se errato ritorna diagnostico
 * @throws InfrastructureException
    */
	public Tb_classe validaPerCrea(String id, boolean _cattura) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
//		TableDao vettoreElementiSimili = null;
//		vettoreElementiSimili = cercaClassePerID(id);
		Classe classe = new Classe();
		Tb_classe tb_classe = null;
		tb_classe = classe.cercaClassePerID(id);
		if (tb_classe == null) {
			SimboloDewey sd = SimboloDewey.parse(id);
			if (sd.isDewey()) {
				controllaClasseDewey(sd.getSimbolo());
			} else {
				controllaSistemaDiClassificazione(sd.getSistema());
				controllaChiaveClasse(id);
			}
		}
   		return tb_classe;
	}





	/**
	 * Metodo controllaSistemaDiClassificazione per controllare la presenza di c2_686
	 * nella tabella tb_codici
	 * Se lancio questo metodo significa che il sistema è diverso da "D"
	 *
	 * @param string
	 */
//  public boolean controllaSistemaDiClassificazioneErr(String sistema) throws EccezioneSbnDiagnostico {
//		Map h;
//		h = Decodificatore.getCodici("SCLA");
//		if (!h.containsKey(sistema))
//			throw new EccezioneSbnDiagnostico(3072,"sistema non codificato");
//		log.debug("trovato sistema di classificazione: "+sistema);
//		return true;
//	}

    public boolean controllaSistemaDiClassificazione(String sistema) throws EccezioneSbnDiagnostico {
        String sis= "";
        sis = Decodificatore.getCd_tabella("SCLA",sistema.trim() );
        if(sis==null)
            throw new EccezioneSbnDiagnostico(3072,"sistema non codificato");
        log.debug("trovato sistema di classificazione: "+sistema);
        return true;
    }

	private void controllaChiaveClasse(String id) throws EccezioneSbnDiagnostico {
		//verifica che la chiave sia composta da soli caratteri ascii base
		if (!ValidazioneDati.isFilled(id) || !Formattazione.isASCII(id))
			throw new EccezioneSbnDiagnostico(3914); // carattere non valido
	}

	public boolean validaPerFonde(
	String idPartenza,
	String idArrivo,
	String utente) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		Tb_classe tb_classe = null;
		Classe classe = new Classe();
		tb_classe = classe.cercaClassePerID(idPartenza);
		if (tb_classe == null)
			throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
        controllaParametriUtente(utente,tb_classe.getCD_LIVELLO(),false);
		tb_classe = classe.cercaClassePerID(idArrivo);
		if (tb_classe == null)
			throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
		return true;
	}



   /**
    * Valida le informazioni relative a una classe in modifica.
    * Input= DatiElementoAut con tipoAuthority = 'CL'
    *
    * Operazioni di validazione:
    * controllo esistenza con identificativo: verificaEsistenzaID con T001; se non
    * esiste ritorna diagnostico
    *
    * controllo versione: T005 = ts_var
    * controllo abilitazione su livello utente/cd_livello
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public Tb_classe validaPerModifica(String t005,String livelloAut,String id, String utente,boolean _cattura,String _fl_costruito)
	throws IllegalArgumentException, InvocationTargetException, Exception {

		Tb_classe tb_classe = verificaEsistenzaID(id);
   		if (tb_classe != null) {
   			SimboloDewey sd = SimboloDewey.parse(id);
   			if (sd.isDewey()){
	   			controllaClasseDewey(sd.getSimbolo());
   			} else {
   				controllaSistemaDiClassificazione(sd.getSistema());
   				controllaChiaveClasse(id);
   			}
   			if (!verificaVersioneClasse(t005,tb_classe))
   				throw new EccezioneSbnDiagnostico(3014, "ts_var non coincide");

   			verificaCd_Livello(livelloAut,tb_classe,utente);
	        controllaParametriUtente(utente,livelloAut, _cattura);
   		} else
   			throw new EccezioneSbnDiagnostico(3013, "identificativo non esiste in base dati");

   		return tb_classe;

	}

   /**
    * effettua il controllo sulla versione (ts_var) per autorizzare la modifica della
    * classe
    */
	public boolean verificaVersioneClasse(String t005, Tb_classe tb_classe){
		if (t005 != null){
            SbnDatavar datavar = new SbnDatavar(tb_classe.getTS_VAR());
            return datavar.getT005Date().equals(t005);
        }
        return true;
	}


   /**
    * controllo formale sulla classe dewey:
    * la classe deve avere lunghezza di almeno 3 caratteri;
    * i primi tre caratteri devono essere numerici; se la classe è più lunga di 3
    * caratteri, il quarto deve essere un punto; non ci possono essere altri
    * caratteri di punteggiatura oltre il punto al quarto posto
    */
	public boolean controllaClasseDewey(String classe) throws EccezioneSbnDiagnostico {
		//controllo il codice dall'id che mi viene passato (t001 calcolato in creaClasse)
		char c = 0;
		boolean esito = true;
		try{
			Integer.parseInt(classe.substring(0,3));
		}catch (Exception e){
			throw new EccezioneSbnDiagnostico(3071,"i primi tre caratteri devono essere numerici");
		}
        A676 t676 = _datiElementoAut.getClasseTypeChoice().getT676();
		if (t676 == null)
            throw new EccezioneSbnDiagnostico(3299, "Le classificazioni Dewey devono essere create con il campo 676");

		//almaviva5_20140409
        String edizione = Decodificatore.convertUnimarcToSbn("ECLA", t676.getV_676());
        if (!ValidazioneDati.isFilled(edizione))
        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

//        if (_datiElementoAut.getClasseTypeChoice().getT676().getC9_676() == null) {
//            throw new EccezioneSbnDiagnostico(3299, "Indicatore di classe costruita mancante");
//        }

        if (classe.length() > 3){
			c = classe.charAt(3);
			if (c != '.')
				throw new EccezioneSbnDiagnostico(3077, String.format("carattere: '%s'", c));
		}
		boolean trovato = false;
		int count = 4;
		while ((!trovato) && (count < classe.length())) {
			c = classe.charAt(count);
			if ((c == '.') | (c == ',') | (c == ':') | (c == ';'))
				trovato = true;
			count++;
		}
		if (trovato) {
			throw new EccezioneSbnDiagnostico(3077, String.format("carattere: '%s'", c));
		}
		return esito;
	}

	/**
	 * controlla l'esistenza per identificativo
	 * controlla che non esistano legami a titoli (tr_tit_cla)
	 * . altrimenti segnalo il diagnostico "esistono legami a titoli"
	 *
	 * Method validaPerCancella.
	 * @param _idCancellazione
	 * @return Tb_classe
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_classe validaPerCancella(String idCancellazione, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tb_classe tb_classe = null;
		Classe classe = new Classe();
		tb_classe = classe.cercaClassePerID(idCancellazione);
		if (tb_classe == null)
			throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
		timeHash.putTimestamp("Tb_classe",
				tb_classe.getCD_SISTEMA() +
				tb_classe.getCD_EDIZIONE() +
				tb_classe.getCLASSE(),
				new SbnDatavar( tb_classe.getTS_VAR()));
		TitoloClasse titoloClasse = new TitoloClasse();
		//almaviva5_20091028
		List legamiTitoloClasse = titoloClasse.estraiLegamiTitoloClasse(idCancellazione);
		if (ValidazioneDati.size(legamiTitoloClasse) > 0)
			throw new EccezioneSbnDiagnostico(3091);

		//almaviva5_20111018 evolutive CFI
		TermineClasse tc = new TermineClasse();
		if (tc.countLegamiTermineThesauro(tb_classe) > 0)
			throw new EccezioneSbnDiagnostico(9127);

		return tb_classe;
	}


	public void controllaVettoreParametriSemantici(String idClasse, SbnUserType utente)
			throws Exception {

		//almaviva5_20120702 #5032
		SimboloDewey sd = SimboloDewey.parse(idClasse);
		Classe classe = new Classe();

		String edizione = sd.getEdizione();
		//almaviva5_20141117
		if (sd.isDewey()) {
	        edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
	        if (!ValidazioneDati.isFilled(edizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

		if (!classe.verificaEdizioneDeweyBiblioteca(utente, sd.getSistema(), edizione) )
			throw new EccezioneSbnDiagnostico(3255);	//sistema o edizione non abilitata per la biblioteca

	}

}
