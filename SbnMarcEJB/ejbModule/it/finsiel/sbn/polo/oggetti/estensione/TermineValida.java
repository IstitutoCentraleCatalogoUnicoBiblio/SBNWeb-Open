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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_termine_thesauroResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_sog_desResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Trs_termini_titoli_bibliotecheResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.transactionmaker.Factoring;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.TermineClasse;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.TerminiTermini;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.polo.orm.Tr_termini_termini;
import it.finsiel.sbn.polo.orm.Tr_the_cla;
import it.finsiel.sbn.polo.orm.Tr_thesauri_biblioteche;
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author
 *
 */
public class TermineValida extends TermineThesauro {

	private static final long serialVersionUID = -5180526183725387637L;

	protected static Logger log = Logger.getLogger("iccu.sbnmarcserver.TermineValida");

	public TermineValida(){
		super();
	}

	/**
	 * Method validaPerCancella.
	 * controllo di esistenza per identificativo,
	 * @param _idCancellazione
	 * @return Tb_descrittore
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_termine_thesauro validaPerCancellaOld(String utente, String idCancellazione) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tb_termine_thesauro termineTrovato = null;
		termineTrovato = cercaTerminePerId(idCancellazione);
		if (termineTrovato == null)
			throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
		//sel la forma è 'A' allora controllo che non ci siano legami con i soggetti
//		if (termineTrovato.getTP_FORMA_DES().equals("A"))
//			controllalegami(idCancellazione);
//		controllaParametriUtente(utente,descrittoreTrovato.getCD_LIVELLO(),false);
		return termineTrovato;
	}
	public Tb_termine_thesauro validaPerCancella(String utente, String id) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
		tb_termine_thesauro = cercaTerminePerId(id);
   		if (tb_termine_thesauro == null)
  			throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
   		verificaTitoliPerTermini(id);
   		//almaviva5_20111018 evolutive CFI
   		verificaClassiPerTermini(id);
		//sel la forma è 'A' allora controllo che non ci siano legami con i soggetti
		if (tb_termine_thesauro.getTP_FORMA_THE().equals("A"))
			controllalegami(tb_termine_thesauro.getDID());

		controllaParametriUtente(utente,tb_termine_thesauro.getCD_LIVELLO(),false);
  		return tb_termine_thesauro;
	}


	private void verificaClassiPerTermini(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
		Classe classe = new Classe();
		if (classe.contaClassePerThesauro(id) > 0)
			throw new EccezioneSbnDiagnostico(9127);

	}

	private void verificaTitoliPerTermini(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
		Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche = new Trs_termini_titoli_biblioteche();
		trs_termini_titoli_biblioteche.setDID(id);
		Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(trs_termini_titoli_biblioteche);
		tavola.executeCustom("selectTitoloPerThesauro");
		if (tavola.getElencoRisultati().size() > 0)
			throw new EccezioneSbnDiagnostico(3091,"Esistono legami con i titoli");

	}

	/**
	 * Method controllalegami.
	 * @param _idCancellazione
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
//	private void controllalegami(String did) throws IllegalArgumentException, InvocationTargetException, Exception {
//		Tr_termini_termini tr_termini_termini  = new Tr_sog_des();
//		tr_sog_des.setDID(did);
//		Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);
//
//
//		tavola.executeCustom("selectLegameTermine");
//        List vec = tavola.getElencoRisultati();
//		1930
//		if (vec.size() >0)
//			throw new EccezioneSbnDiagnostico(3130,"esistono legami con alcuni soggetti");
//
//	}

	private void controllalegamiOld(String did) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_sog_des tr_sog_des = new Tr_sog_des();
		tr_sog_des.setDID(did);
		Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);


		tavola.executeCustom("selectLegameTermine");
        List vec = tavola.getElencoRisultati();

		if (vec.size() >0)
			throw new EccezioneSbnDiagnostico(3130,"esistono legami con alcuni soggetti");

	}

	private void controllalegami(String did) throws IllegalArgumentException, InvocationTargetException, Exception {
		TerminiTermini terminiTermini = new TerminiTermini();
		List vettoreDiLegami = terminiTermini.cercaLegame(did);
		Tr_termini_termini legame;
		if (vettoreDiLegami.size() >0)
			throw new EccezioneSbnDiagnostico(3130,"esistono legami con alcuni termini");

	}

	/**
	 * @param idDescrittore
	 * @param livello
	 * @param vettoreParametriSemantici
	 * questo metodo riceve in input un vettore di parametri semantici (calcolato tramite il Validator)
	 *
	 * l'utente deve essere abilitato alla gestione descrittore
	 * l'utente deve essere abilitato alla gestione soggettario
	 * controllo che non esista già il descrittore con uguale k_norm_descritt
	 *
	 * valida legami descrittori-descrittori:
	 * il descrittore deve esistere nel db
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean validaPerCrea(
	String idTermine,
	String livello,
	String utente,
	A935 t935,
    boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
	    String c2_935 = null;
		String a_935;
		Tb_termine_thesauro termineTrovato = null;
		if (!idTermine.equals(Factoring.SBNMARC_DEFAULT_ID)){

			termineTrovato = verificaEsistenza(idTermine);
			if (termineTrovato != null)
				if(!_cattura)
					throw new EccezioneSbnDiagnostico(3012,"elemento già presente in base dati");
		}
		c2_935 = t935.getC2_935();
		a_935 = t935.getA_935();
		controllaVettoreParametriSemantici(c2_935, utente);
		TableDao tavola;
		int contaSimili=0;
		String Ky_norm = NormalizzaNomi.normalizzazioneGenerica(t935.getA_935());
		tavola = cercaTerminePerk_norm_termine(Ky_norm,t935.getC2_935());
		contaSimili = tavola.getElencoRisultati().size();

		if (contaSimili >0)
			throw new EccezioneSbnDiagnostico(3012,"descrittore esistente");
		return true;
	}


	public boolean validaPerCreaLegame(ElementAutType elementAutType) throws IllegalArgumentException, InvocationTargetException, Exception{
		SbnFormaNome 					tipoForma = null;
		boolean 						validato = false;
		LegamiType[]					legamiType;
		String 							tp_forma;
		ArrivoLegame[] 					arrivoLegame;
		int 							sizeArrivoLegame;
		String 							idPartenza;
		legamiType 	= elementAutType.getLegamiElementoAut();
		int size 	= elementAutType.getLegamiElementoAutCount();
		tipoForma	= elementAutType.getDatiElementoAut().getFormaNome();
		idPartenza  = elementAutType.getDatiElementoAut().getT001();
		if (tipoForma == null)	tipoForma = SbnFormaNome.A;
		Tb_termine_thesauro tb_termine_thesauro = null;
		int i = 0;
		int j;
		boolean esito = true;
		while ((i < size)&(esito == true)){
			arrivoLegame=legamiType[i].getArrivoLegame();
			j=0;
			while ((j < legamiType[i].getArrivoLegameCount())&(esito==true)){
				tb_termine_thesauro = cercaTerminePerId(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
				if (tb_termine_thesauro == null)
					throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");
				j++;
			}
			i++;
		}
		TerminiTermini terminiTermini = new TerminiTermini();
        for (i = 0; i < size; i++) {
			arrivoLegame=legamiType[i].getArrivoLegame();
			sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
			String idArrivo;
	        for (j = 0; j < sizeArrivoLegame; j++) {
	        	idArrivo = arrivoLegame[j].getLegameElementoAut().getIdArrivo();
	        	tb_termine_thesauro = cercaTerminePerId(idArrivo);
				if (tb_termine_thesauro!=null){
					controllaTipoLegame(arrivoLegame[j].getLegameElementoAut().getTipoLegame());
		        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("USE")){
		        		if ((tb_termine_thesauro.getTP_FORMA_THE().equals("A") &&
		        			(tipoForma.getType() == SbnFormaNome.R_TYPE))){
		        			terminiTermini = new TerminiTermini();
		        			if (terminiTermini.cercaTerminiTermini(idPartenza)){
								validato = true;
		        			}
		        			if (terminiTermini.countLegamiTerminiTermini(idPartenza, idArrivo) >=1 ){
		        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
		        			}
		        		}
		        	}else if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("UF")){
		        		if ((tb_termine_thesauro.getTP_FORMA_THE().equals("R") &&
		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
		        			terminiTermini = new TerminiTermini();
		        			if (terminiTermini.cercaTerminiTermini(idArrivo)){
								validato = true;
		        			}
		        			terminiTermini.getDID_BASE();
		        			if (terminiTermini.countLegamiTerminiTermini(idArrivo,idPartenza) >=1 ){
		        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
		        			}
		        		}
		        	}else {
		        		if ((tb_termine_thesauro.getTP_FORMA_THE().equals("A") &&
		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
		        			validato = true;
		        		}else throw new EccezioneSbnDiagnostico(3033,"Legame non ammesso per la forma Accettata");
		        	}


				}
				else validato=false;
	        }
        }
    	return validato;
   }

    public boolean validaPerCreaLegameSoggettoDescrittore(ElementAutType elementAutType) throws IllegalArgumentException, InvocationTargetException, Exception{
        SbnFormaNome                    tipoForma = null;
        boolean                         validato = false;
        LegamiType[]                    legamiType;
        String                          tp_forma;
        ArrivoLegame[]                  arrivoLegame;
        int                             sizeArrivoLegame;
        String                          idPartenza;
        legamiType  = elementAutType.getLegamiElementoAut();
        int size    = elementAutType.getLegamiElementoAutCount();
        tipoForma   = elementAutType.getDatiElementoAut().getFormaNome();
        idPartenza  = elementAutType.getDatiElementoAut().getT001();
        if (tipoForma == null)  tipoForma = SbnFormaNome.A;
        Tb_termine_thesauro tb_termine_thesauro = null;
        int i = 0;
        int j;
        boolean esito = true;
        while ((i < size)&(esito == true)){
            arrivoLegame=legamiType[i].getArrivoLegame();
            j=0;
            while ((j < legamiType[i].getArrivoLegameCount())&(esito==true)){
            	tb_termine_thesauro = cercaTerminePerId(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
                if (tb_termine_thesauro == null)
                    throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");
                j++;
            }
            i++;
        }
        for (i = 0; i < size; i++) {
            arrivoLegame=legamiType[i].getArrivoLegame();
            sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
            String idArrivo;
            for (j = 0; j < sizeArrivoLegame; j++) {
                idArrivo = arrivoLegame[j].getLegameElementoAut().getIdArrivo();
                tb_termine_thesauro = cercaTerminePerId(idArrivo);
                if (tb_termine_thesauro!=null){
                    if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString() .equals("935")){
                        validato = true;
                    }else{
                        throw new EccezioneSbnDiagnostico(3033, "tipo legame non valido");
                    }
                }else{
                    validato=false;
                }
            }
        }
        return validato;
   }
	public boolean validaPerModificaLegame(ElementAutType elementAutType, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception{

		for ( LegamiType legame : elementAutType.getLegamiElementoAut()) {
			for (ArrivoLegame arrivoLegame : legame.getArrivoLegame()) {
				LegameElementoAutType autLegato = arrivoLegame.getLegameElementoAut();
				switch (autLegato.getTipoAuthority().getType()) {
				case SbnAuthority.TH_TYPE:
					validaLegameTermineTermine(elementAutType.getDatiElementoAut(), autLegato, legame.getTipoOperazione(), timeHash);
					break;

				//almaviva5_20111014 evolutive CFI
				case SbnAuthority.CL_TYPE:
					validaLegameTermineClasse(elementAutType.getDatiElementoAut(), autLegato, legame.getTipoOperazione(), timeHash);
					break;

				default:
					throw new EccezioneSbnDiagnostico(3240); //auth. errata
				}
			}
		}

		return true;
   }



	private void validaLegameTermineClasse(DatiElementoType datiElementoAut,
			LegameElementoAutType autLegato, SbnTipoOperazione tipoOperazione,
			TimestampHash timeHash) throws Exception {

		String idArrivo = autLegato.getIdArrivo();
		String idPartenza = datiElementoAut.getT001();

		ClasseValida cv = new ClasseValida();
		Tb_classe cla = cv.verificaEsistenzaID(autLegato.getIdArrivo());
		if (cla == null)
			throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");

		TermineClasse tc = new TermineClasse();
		Tr_the_cla legame = tc.getLegameTermineClasse(idPartenza, idArrivo);
		if (legame != null)
			timeHash.putTimestamp("tr_the_cla",	legame.getUniqueId(), new SbnDatavar( legame.getTS_VAR()));

		switch (tipoOperazione.getType()) {
			case SbnTipoOperazione.CREA_TYPE:
				if (legame != null && !legame.getFL_CANC().equals("S"))
	           		throw new EccezioneSbnDiagnostico(3030, "legame già esistente");
				break;

			case SbnTipoOperazione.MODIFICA_TYPE:
			case SbnTipoOperazione.CANCELLA_TYPE:
				if (legame == null || legame.getFL_CANC().equals("S"))
					throw new EccezioneSbnDiagnostico(3029,"legame inesistente");
				break;
		}

	}



	private void validaLegameTermineTermine(DatiElementoType datiElementoAut,
			LegameElementoAutType autLegato, SbnTipoOperazione tipoOperazione, TimestampHash timeHash) throws Exception {

		String idArrivo = autLegato.getIdArrivo();
		String idPartenza = datiElementoAut.getT001();
		SbnFormaNome formaNome = datiElementoAut.getFormaNome();
		SbnLegameAut tipoLegame = autLegato.getTipoLegame();

		Tb_termine_thesauro thes = cercaTerminePerId(idArrivo);
		if (thes == null)
			throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");

		TerminiTermini tt = new TerminiTermini();
		List<Tr_termini_termini> legami = tt.getTerminiTermini(idPartenza, idArrivo);
		for (Tr_termini_termini legame : legami)
			timeHash.putTimestamp("Tr_termini_termini",	legame.getDID_BASE() + legame.getDID_COLL(), new SbnDatavar( legame.getTS_VAR()));

		switch (tipoOperazione.getType()) {
		case SbnTipoOperazione.CREA_TYPE:

			for (Tr_termini_termini legame : legami) {
				if (!legame.getFL_CANC().equals("S"))
	           		throw new EccezioneSbnDiagnostico(3030, "legame già esistente");
			}

            // TEST PER VERIFICA DELLE CONTROLLI PER L'INSERIMENTO
			controllaTipoLegame(tipoLegame);
        	if (tipoLegame.getType() == SbnLegameAut.valueOf("USE").getType() )
        		if ((thes.getTP_FORMA_THE().equals("A") && (formaNome.getType() == SbnFormaNome.R_TYPE))) {
        			if (tt.cercaTerminiTermini(idPartenza))
						return;

        			if (tt.countLegamiTerminiTermini(idPartenza, idArrivo) > 0 )
        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");

        		}

        	if (tipoLegame.getType() == SbnLegameAut.valueOf("UF").getType() )
        		if ((thes.getTP_FORMA_THE().equals("R") && (formaNome.getType() == SbnFormaNome.A_TYPE))) {
        			if (tt.cercaTerminiTermini(idArrivo))
						return;

        			if (tt.countLegamiTerminiTermini(idArrivo,idPartenza) > 0 )
        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
        		}

       		if ((thes.getTP_FORMA_THE().equals("A") && (formaNome.getType() == SbnFormaNome.A_TYPE)))
       			return;
        	else
        		throw new EccezioneSbnDiagnostico(3033,"Legame non ammesso per la forma Accettata");


		case SbnTipoOperazione.MODIFICA_TYPE:
			controllaTipoLegame(tipoLegame);
			if (tipoLegame.getType() == SbnLegameAut.valueOf("USE").getType() )
        		if ((thes.getTP_FORMA_THE().equals("A") && (formaNome.getType() == SbnFormaNome.R_TYPE))) {
        			if (tt.cercaTerminiTermini(idPartenza))
						return;
		        	else
		        		throw new EccezioneSbnDiagnostico(3033);
        		}

			if (tipoLegame.getType() == SbnLegameAut.valueOf("UF").getType() )
        		if ((thes.getTP_FORMA_THE().equals("R") && (formaNome.getType() == SbnFormaNome.A_TYPE))) {
           			if (tt.cercaTerminiTermini(idArrivo))
						return;
           			else
           				throw new EccezioneSbnDiagnostico(3033);
        		}

			if ((thes.getTP_FORMA_THE().equals("A") && (formaNome.getType() == SbnFormaNome.A_TYPE)))
       			return;
        	else
        		throw new EccezioneSbnDiagnostico(3033,"Legame non ammesso per la forma Accettata");


		case SbnTipoOperazione.CANCELLA_TYPE:
			if (!tt.cercaTerminiTermini(idPartenza,idArrivo))
				throw new EccezioneSbnDiagnostico(3029,"legame inesistente");

        }
	}



	/**
	 * Method controllaTipoLegame.
	 * @param tipoLegame
	 */
	private void controllaTipoLegame(SbnLegameAut tipoLegame) throws EccezioneSbnDiagnostico {
		if (!ValidazioneDati.in(tipoLegame.getType(),
				SbnLegameAut.valueOf("USE").getType(),
				SbnLegameAut.valueOf("UF").getType(),
				SbnLegameAut.valueOf("RT").getType(),
				SbnLegameAut.valueOf("BT").getType(),
				SbnLegameAut.valueOf("NT").getType(),
				//almaviva5_20111014 evolutive CFI
				SbnLegameAut.valueOf("941").getType()))
		throw new EccezioneSbnDiagnostico(3031); //legame non valido
	}


	/**
	 * metodo utilizzato in fase di modifica:
	 * si controlla che il livello in input  sia minore di quello dell'oggetto nel db
	 */
	public boolean verificaLivello(
			Tb_termine_thesauro tb_termine_thesauro,
	String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico {
        if(_cattura)
            return true;
		if (Integer.parseInt(livelloAut)<Integer.parseInt(tb_termine_thesauro.getCD_LIVELLO()))
			throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
		return true;
	}

	private void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico{
      Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "TH");
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


	public boolean controllaVettoreParametriSemantici(String c2_935, String codiceUtente) throws EccezioneSbnDiagnostico{
		int i=0;
		ValidatorProfiler validator = ValidatorProfiler.getInstance();
//		ValidatorContainerObject  curr_user =  (ValidatorContainerObject)validator.getAbilitazioni(codiceUtente);
//		List parametri = curr_user.getElenco_parametri_sem();
        Tbf_biblioteca_in_polo biblio = validator.getBiblioteca(codiceUtente.substring(0, 3), codiceUtente.substring(3,6));

        Iterator iter = biblio.getTr_thesauri_biblioteche().iterator();


		boolean trovato = false;
		c2_935 = Decodificatore.getCd_tabella("Tb_termine_thesauro", "cd_the", c2_935.toUpperCase());
		if(c2_935 == null)
			throw new EccezioneSbnDiagnostico(3211,"thesauro non abilitato per la biblioteca");

		while(iter.hasNext())
		{
			Tr_thesauri_biblioteche thesauri = (Tr_thesauri_biblioteche) iter.next();
        	//Tr_soggettari_biblioteche soggettari = (Tr_soggettari_biblioteche) iter.next();
        	if (c2_935.equals(thesauri.getCD_THE().trim()))
        	{
    				trovato = true;
    				break;
        	}
		}





//		while ((!trovato)&&(i<parametri.size())){
//			if (c2_935.equals(((Tbf_par_sem)parametri.get(i)).getCd_tabella_codici().trim())&&
//				(((Tbf_par_sem)parametri.get(i)).getTp_tabella_codici()).equals("SOGG"))
//				trovato = true;
//			i++;
//		}
		if (!trovato)
			throw new EccezioneSbnDiagnostico(3211,"thesauro non abilitato per la biblioteca");
		return trovato;
	}


	public boolean verificaVersioneTermine(
			Tb_termine_thesauro tb_termine_thesauro, String t005)
			throws EccezioneSbnDiagnostico {
		if (t005 != null) {
			SbnDatavar datavar = new SbnDatavar(tb_termine_thesauro.getTS_VAR());
			boolean ok = datavar.getT005Date().equals(t005);
			if (!ok)
				throw new EccezioneSbnDiagnostico(3014);
			return ok;
		}
		throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
	}


	/**
	 * Method validaPerModifica.
	 * @param t001
	 * @param cd_livello
	 * @param t005
	 * @param codiceUtente
	 * @param t935
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_termine_thesauro validaPerModifica(
		String 	idTermine,
		String 	cd_livello,
		String	t005,
		String 	codiceUtente,
		A935 	t935,
        boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
			Tb_termine_thesauro termineTrovato = new Tb_termine_thesauro();
			termineTrovato = cercaTerminePerId(idTermine);
			if (termineTrovato == null)
				throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
			if (!verificaLivello(termineTrovato,cd_livello,_cattura))
				throw new EccezioneSbnDiagnostico(3007,"livello di authority troppo basso");
			controllaParametriUtente(codiceUtente,cd_livello,_cattura);
			verificaVersioneTermine(termineTrovato,t005);
			return termineTrovato;
	}

	public void cercaSimili(
			String 	idTermine,
			String 	cd_livello,
			String	t005,
			String 	codiceUtente,
			A935 	t935,
	        boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
				TableDao tavola;
				int contaSimili=0;
				String Ky_norm = NormalizzaNomi.normalizzazioneGenerica(t935.getA_935());
				tavola = cercaTerminePerk_norm_termine(Ky_norm,t935.getC2_935());
				Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();

				contaSimili = tavola.getElencoRisultati().size();

				if (contaSimili >0){
					Tb_termine_thesauro appo = (Tb_termine_thesauro) tavola.getElencoRisultati().get(0);
					if (!appo.getDID().toString().equalsIgnoreCase(idTermine) ){
						String msg = "descrittore esistente con identificativo= " + appo.getDID().toString();
						throw new EccezioneSbnDiagnostico(7912,msg);
					}
				}
		}

	/**
	 * Method validaPerFonde.
	 * @param idPartenza
	 * @param idArrivo
	 * @throws InfrastructureException
	 */
	public void validaPerFonde(String utente, String idPartenza, String idArrivo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
		TermineThesauro termine = new TermineThesauro();
		tb_termine_thesauro = termine.cercaTerminePerId(idPartenza);
		if (tb_termine_thesauro == null)
			throw new EccezioneSbnDiagnostico(3001,"idPartenza non esistente");
		tb_termine_thesauro = termine.cercaTerminePerId(idArrivo);
		if (tb_termine_thesauro == null)
			throw new EccezioneSbnDiagnostico(3001,"idArrivo non esistente");
		controllaParametriUtente(utente,tb_termine_thesauro.getCD_LIVELLO(),false);
	}
	   /**
     * si effettua la select tb_soggetto per
     * uguaglianza su ky_cles1_s e ky_cles2_s e ds_soggetto
     * se esiste si ritorna il diagnostico 3004
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List verificaSimiliConferma(String id, A935 t250) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_termine_thesauro tb_the = new Tb_termine_thesauro();
        tb_the = gestisceDescrizione(t250, tb_the,true);
        tb_the.setDID(id);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(tb_the);
        if (tb_the.getKY_TERMINE_THESAURO() == null) {
            tb_the.settaParametro(TableDao.XXXky_termine_thesauro,"null");
        }


        tavola.executeCustom("selectSimiliConferma");
        List v= tavola.getElencoRisultati();

        return v;
    }

}
