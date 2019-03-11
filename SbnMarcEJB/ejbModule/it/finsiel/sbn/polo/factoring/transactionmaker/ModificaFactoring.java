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
package it.finsiel.sbn.polo.factoring.transactionmaker;

/**
 * @author
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.ServiceLocatorException;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.util.IndiceJMSObjectLocator;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe ModificaFactoring
 * <p>
 * Classe "Dispatcher" - attiva i Factoring di tipo "Modifica" Ogni Factoring
 * richiama(esegue) a sua volta le azioni specifiche
 * </p>
 */
public abstract class ModificaFactoring extends Factoring {

    protected List elencoDiagnostico = null;
    protected final static int maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));


    	//Integer.parseInt(IndiceServiceLocator.getProperty("RIGHE_PER_BLOCCO_SBNMARC"));

    /**
     * attributi
     */
    protected ModificaType factoring_object = null;
    //almaviva5_20140217 evolutive google3
	protected boolean _cattura;

    static Category log = Category
            .getInstance("iccu.serversbnmarc.ModificaFactoring");

    /**
     * Metodo costruttore classe di factoring
     * <p>
     * Riceve il root object da cui estrapolare le informazioni XML ricevute in
     * input riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
     * </p>
     *
     * @param SBNMarc
     *            oggetto radice XML in input
     * @param input_root_object
     */
    public ModificaFactoring(SBNMarc input_root_object) {
        // Assegno radice e classi XML principali
        super(input_root_object);

        // Assegno classi specifiche per questo factoring
        factoring_object = request_object.getModifica();
        //almaviva5_20140217 evolutive google3
        _cattura = factoring_object.getCattura();

    }

    /**
     * getFactoring - ritorna il Factoring opportuno
     * <p>
     * Metodo che lancia il Factoring verificando le informazioni ricevute in
     * input La Request verificata è di tipo SBnRequest (XML)
     * </p>
     *
     * @param nessuno
     * @param sbnmarcObj
     * @return void
     * @throws iccu.box.exception.EccezioneFactoring
     */
    static public Factoring getFactoring(SBNMarc sbnmarcObj)
            throws EccezioneFactoring {
        Factoring current_factoring = null;
        ModificaType factoring_object = sbnmarcObj.getSbnMessage()
                .getSbnRequest().getModifica();
        log.debug("STO PER VERIFICARE COSA LANCIARE");
        // Creo il secondo livello di factoring (valutando input)
        if (factoring_object.getElementoAut() != null) {
            current_factoring = ModificaElementoAutFactoring
                    .getFactoring(sbnmarcObj);
        } else if (factoring_object.getDocumento() != null) {
            if (factoring_object.getDocumento().getDocumentoTypeChoice()
                    .getDatiDocumento() != null)
                current_factoring = new ModificaTitolo(sbnmarcObj);
            else
                current_factoring = new ModificaTitoloAccesso(sbnmarcObj);
        } else if (factoring_object.getPropostaCorrezione() != null) {
            current_factoring = new ModificaPropostaCorrezione(sbnmarcObj);
        } else
            throw new EccezioneFactoring(100);
        //log.debug("HO CREATO IL CURRENT FACTORING:" + current_factoring + ":");

        return current_factoring;
    }

    /**
     * Metodo utilizzato per preparare gli output alle fasi successive
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void proseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
            numeroRecord = elencoDiagnostico.size();
            try {
                // Salvo il file in ldap.
                if (numeroRecord > maxRighe) {
                    rowCounter = maxRighe;
                    String nomeBlocco = assegnaNomeBlocco(1);
                    log.info("Scrivo in LDAP con nome file:" + nomeBlocco);
                    try {
                        // Anche se non riesco a scrivere su ldap posso
                        // terminare correttamente
                        IndiceJMSObjectLocator.bind(nomeBlocco,object_response);
                        //IndiceLDAPObjectLocator.bind(nomeBlocco,object_response);
                    } catch (Exception e) {
                        log.error("Non riesco a scrivere su ldap.", e);
                        // Chiudo la tavola, non ha senso continuare ad eseguire
                    }
                } else {
                    rowCounter = numeroRecord;
                }
                // Quale blocco sto estraendo
                int blockCounter = 1;

                while (rowCounter < numeroRecord) {
                    // while (tavola_response.hasMoreElements()) {
                    blockCounter++;
                    String nomeBlocco = assegnaNomeBlocco(blockCounter);
                    SBNMarc stringaBlocco = formattaLista(blockCounter);
                    log.info("Scrivo in LDAP con nome file:" + nomeBlocco);
                    long tempo = System.currentTimeMillis();
                    IndiceJMSObjectLocator.bind(nomeBlocco, stringaBlocco);
                    //IndiceLDAPObjectLocator.bind(nomeBlocco, stringaBlocco);
                    tempo -= System.currentTimeMillis();
                    log.debug("LDAP; tempo impiegato in ms :" + tempo);
                    rowCounter += maxRighe;
                }
            } catch (ServiceLocatorException e) {
                log.error("Problemi in scrittura del file di recupero", e);
            } catch (EccezioneIccu ecc) {
                log.debug("Errore, eccezione:" + ecc);
            }
        }
    }

    protected SBNMarc formattaLista(int numeroBlocco) throws EccezioneDB,
            EccezioneFactoring, EccezioneSbnDiagnostico, IllegalArgumentException, InvocationTargetException, Exception {
        log.error("invocato metodo non implementato");
        return null;
    }

    /**
     * eseguiTransazione
     * <p>
     * Metodo che lancia l'esecuzione dell'operazione di Insert (Crea)
     * richiamando tutti i metodi che lanciano la insert nelle varie tavole del db
     * </p>
     * @param  nessuno
     * @return void
     * @throws iccu.box.exception.EccezioneDB
    * Metodo che lancia l'esecuzione dell'operazione di Insert (Crea)
    * richiamando tutti i metodi che lanciano la insert nelle varie tavole del db
    * </p>
    * @param  nessuno
    * @return void
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IndexOutOfBoundsException
     * @throws IllegalArgumentException
    */
    public final void eseguiTransazione() throws IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception {
        try {
        	// Inizio modifica almaviva2 BUG MANTIS 3973 - in cattura documenti non devono essere effettuati controlli su abilitazioni
        	// Inserire if per verificare che si sia nel caso dei titoli documento e non altri !!!!!!!!!

        	// ATTENZIONE modifica almaviva2 BUG MANTIS 4124 19.01.2011 la regola vale per tutti e non solo per i documenti!!!!

        	if (!_cattura) {
        		  verificaAbilitazioni();
        	}
        	// Fine modifica almaviva2 BUG MANTIS 3973 - in cattura documenti non devono essere effettuati controlli su abilitazioni

            executeModifica();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            object_response = FormatoErrore.buildMessaggioErrore(ecc, user_object);
            throw ecc;
        }
    }

    protected abstract void executeModifica() throws EccezioneIccu, InfrastructureException, IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception;


    public void settaIdLista() throws EccezioneDB, InfrastructureException {
        if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
            numeroRecord = elencoDiagnostico.size();
            // Salvo il file in ldap.
            if (numeroRecord > maxRighe) {
                Progressivi progr = new Progressivi();
                idLista = progr.getIdLista();
            }
        }

    }

}
