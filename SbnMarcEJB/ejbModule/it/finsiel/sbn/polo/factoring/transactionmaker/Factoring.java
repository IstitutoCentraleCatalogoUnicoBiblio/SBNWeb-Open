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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Memorizator;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.util.MarshallingUtil;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
// fine
/**
 *Classe Factoring
 *<p>
 * Classe "Dispatcher" - attiva il Factoring in base all'informazione presente nel file XML
 * Ogni Factoring richiama a sua volta le azioni specifiche
 *</p>
 */
public abstract class Factoring implements FactoringAPI {

	private static final ThreadLocal<SbnUserType> currentUser = new ThreadLocal<SbnUserType>() {

		@Override
		protected SbnUserType initialValue() {
			//almaviva5_20120530 utente globale fittizio
			SbnUserType user = new SbnUserType();
	        user.setBiblioteca("      ");
	        user.setUserId("      ");
			return user;
		}

	};

    protected static Logger log = Logger.getLogger("iccu.serversbnmarc.Factoring");
    //A quale riga sono.
    protected int rowCounter = 0;
    // Connessione al db
    protected UserTransaction transaction = null;
    //Xml info ricevute in input
    protected SBNMarc root_object = null;
    protected SbnMessageType message_object = null;
    protected SbnUserType user_object = null;
    protected String cd_utente = null;
    protected SbnRequestType request_object = null;
    protected SbnResponseType response_object = null;
    protected MServDoc servDoc = null;

    protected static BigDecimal schemaVersion = ResourceLoader.getPropertyBigDecimal("SCHEMA_VERSION");

    protected ValidatorProfiler validator = ValidatorProfiler.getInstance();
    //Flag per l'esecuzione differita del factoring
    protected boolean scheduled = false;
    /** Flag vero solo per i factoring di esporta */
    protected boolean esporta = false;
    /** stringa utilizzata per passare un parametro dal main process */
    protected String idScript = null;
    /** Numero di record */
    protected int numeroRecord;
    /** Stringa di risposta */
    //protected String object_response = null;
    protected SBNMarc object_response = null;

	private String tp_materiale = null;
	protected String idLista = null;
    protected int maxResponseRecord;

    {
    	/** numero massimo di record che è possibile mettere in uscita */
        this.maxResponseRecord = ResourceLoader.getPropertyInteger("NRO_MAX_RIGHE_RICHIESTA");
    }

    /**
     * Costruttore, setta i vari campi chiamando il metodo setBase.
     * @param input_root_object
     */
    protected Factoring(SBNMarc input_root_object) {
        setBase(input_root_object);
        //almaviva5_20120530 filtro utente globale
        currentUser.set(user_object);
    }

    /**
     * Costruttore da usare con molta cautela perchè non setta i vari campi.
     * Per il momento viene usato solo in LocalizzaFactoring.
     */
    protected Factoring() {
    }
    /**
     * Settings classi principali input formato XML
     * <p>
    	* Metodo utile a tutte le classi di factoring
    	* la sua signature è inserita nella classe di Interfaccia
    	* </p>
    	* @param  Radice xml in input
    	* @return void
    	*/
    private final void setBase(SBNMarc input_root_object) {
        root_object = input_root_object;
        user_object = root_object.getSbnUser();
        setCdUtente(user_object);
        message_object = root_object.getSbnMessage();
        request_object = message_object.getSbnRequest();
        response_object = message_object.getSbnResponse();
        servDoc = message_object.getMServDoc();
        schemaVersion = root_object.getSchemaVersion();
    }
    /**
     * setting handler di Connessione
     * <p>
    	* Metodo utile a tutte le classi di factoring
    	* la sua signature è inserita nella classe di Interfaccia
    	* </p>
    	* @param  nessuno
    	* @return void
    	*/
    /**
     * getFactoring - ritorna il Factoring opportuno
     * <p>
    	* Metodo che lancia il Factoring verificando le informazioni ricevute in input
    	* La Request verificata è di tipo SBnRequest (XML)
    	* </p>
    	* @param  nessuno
    	* @return void
    	*/
    public static Factoring getFactoring(SBNMarc sbnmarcObj) throws EccezioneFactoring {
        Factoring factoring = null;
        // Assegno radice e classi XML principali
        //setBase(sbnmarcObj);
        // Creo il primo livello di factoring (valutando input)
        SbnRequestType request_object = null;
        MServDoc servDoc = null;


        if (sbnmarcObj != null && sbnmarcObj.getSbnMessage() != null){
            request_object = sbnmarcObj.getSbnMessage().getSbnRequest();
        	servDoc = sbnmarcObj.getSbnMessage().getMServDoc();
        }

        if ((request_object == null) && (servDoc == null))
            throw new EccezioneFactoring(100);
        if(request_object != null){
			if (request_object.getCrea() != null) {
				factoring = CreaFactoring.getFactoring(sbnmarcObj);
				log.debug("LANCIO CREA-FACTORING:" + factoring);
			} else if (request_object.getCerca() != null) {
				factoring = CercaFactoring.getFactoring(sbnmarcObj);
				log.debug("LANCIO CERCA-FACTORING:" + factoring);
			} else if (request_object.getModifica() != null) {
				factoring = ModificaFactoring.getFactoring(sbnmarcObj);
				log.debug("LANCIO MODIFICA-FACTORING:" + factoring);
			} else if (request_object.getLocalizza() != null) {
				factoring = new LocalizzaFactoring(sbnmarcObj);
				log.debug("LANCIO LOCALIZZA-FACTORING:" + factoring);
			} else if (request_object.getImporta() != null) {
				// factoring = new ImportaFactoring(sbnmarcObj);
				// log.debug("LANCIO IMPORTAFACTORING:" + factoring);
				throw new EccezioneFactoring(501);
			} else if (request_object.getComunicaAllineati() != null) {
				factoring = new ComunicaAllineatiFactoring(sbnmarcObj);
				log.debug("LANCIO COMUNICAALLINEATI-FACTORING:" + factoring);
			} else if (request_object.getChiediAllinea() != null) {
				// factoring = new ChiediAllineaFactoring(sbnmarcObj);
				// log.debug("LANCIO CHIEDIALLINEAFACTORING:" + factoring);
				throw new EccezioneFactoring(501);
			} else if (request_object.getEsporta() != null) {
				factoring = EsportaFactoring.getFactoring(sbnmarcObj);
				log.debug("LANCIO ESPORTA-FACTORING:" + factoring);
			} else if (request_object.getCancella() != null) {
				factoring = CancellaFactoring.getFactoring(sbnmarcObj);
				log.debug("LANCIO CANCELLA-FACTORING:" + factoring);
			} else if (request_object.getFonde() != null) {
				factoring = FondeFactoring.getFactoring(sbnmarcObj);
				log.debug("LANCIO FONDE-FACTORING:" + factoring);
			} else
				throw new EccezioneFactoring(100);
		}
        else if (servDoc != null){
 	            factoring = MServDocFactoring.getFactoring(sbnmarcObj);
 	            log.debug("LANCIO MSERVDOC:" + factoring);
 	    } else{
 	            throw new EccezioneFactoring(100);
 	    }
        // Mi faccio tornare l'ennesimo livello, quello che esegue l'azione
        return factoring;
    }

    public String getXMLResult() {
    	try {
			return marshal(object_response);
		} catch (EccezioneSbnDiagnostico e) {
			return FormatoErrore.preparaMessaggioErrore(e);
		}
    }

    public final SBNMarc getSBNMarcResult() {
        return object_response;
    }

    public void eseguiTransazione() throws EccezioneIccu, InfrastructureException, IllegalArgumentException, InvocationTargetException, Exception {
    }

    public SBNMarc eseguiRecupero() {
        return null;
    }
    public void proseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        log.info("PROSEGUI TRANSAZIONE NON IMPLEMENTATA");
    }

    /**
     * Method eseguiTransazioneInDifferita.
     * La funzione deve essere utilizzata per le operazioni di
     * lancio di processi batch. (Es. importa, esporta).
     * @param utente    Identificativo di chi ha fatto la richiesta
     * @param attivita  attivita per quel tipo di stampa
     *
     */
// TODO almaviva ELIMINATA
//
//    public String eseguiTransazioneInDifferita(String utente, String attivita)
//        throws EccezioneSbnDiagnostico {
//        StringWriter sw = new StringWriter();
//        String file_name = "";
//        int id_script = -1;
//        String SBNMarc_string = new String();
//        try {
//            root_object.marshal(sw);
//            SBNMarc_string = sw.toString(); // ottengo in formato stringa il messaggio xml request
//        } catch (Exception e) {
//            log.error("Errore nel marshalling per salvataggio messaggio formato stringa:", e);
//            throw new EccezioneSbnDiagnostico(101);
//        }
//        try {
//            /*
//            * Inserimento dell'sbnmarc in ldap
//            */
//            file_name = "sbnmarc_diff_" + Math.abs(new Random().nextInt());
//            // assegna un nome univoco al file parametri
//            IndiceLDAPObjectLocator.uploadSbnmarc(file_name, SBNMarc_string); // xml_request differita
//        } catch (Exception e) {
//            log.error("Errore nel salvataggio messaggio: ", e);
//            throw new EccezioneSbnDiagnostico(210, "Non riesco a contattare ldap", e);
//        }
//        try {
//            /*
//            * CHIAMATA ALLO SCHEDULER
//            *
//            * Si può controllare l'esistenza del file in ldap
//            * prima di schedulare il processo
//            */
//                ScheduledJobBean sjb = new ScheduledJobBean(null, // cod. utente amministrazione
//        utente, // cod. utente
//        attivita, // id tipo attività
//        "filename#" + file_name + "#tipo#sbnmarc_differita#", // parametri
//        3, // id server
//    "iccu.box.engine.serversbnmarc.MainProcess",
//                // classe da eseguire
//    new String[] {
//            }, // obj
//            new String[] {
//            }, // class
//            System.currentTimeMillis() + 10000, // fra 10 secondi
//            1, // num. di ripetizioni
//            1, false); // priorità
//
//            JobHandlerLocalHome jobHandlerHome =
//                (JobHandlerLocalHome) ServiceLocator.getInstance().getLocalHome("local:ejb/JobHandler");
//            JobHandlerLocal jobHandler = jobHandlerHome.create();
//            id_script = jobHandler.addNightJobSbnmarc(sjb);
//            log.info("scheduled job added successfully!");
//        } catch (Exception ecc) {
//            log.error("Errore in esecuzione differita", ecc);
//            throw new EccezioneSbnDiagnostico(70, "Errore ", ecc);
//        } /*
//                      * FINE CHIAMATA ALLO SCHEDULER
//                      *
//                      */
//        log.info("file parametro >" + file_name + "<");
//        return "" + id_script;
//    }
//

    /**
     * Method inviaMail
     * La funzione deve essere utilizzata per le operazioni che prevodono a seguito
     * di una operazione in differita il lancio di una mail di fine elaborazione
     * @param utente    Identificativo di chi ha fatto la richiesta
     * @param response  la stringa contenente il messaggio SBNMarc di risposta
     * @param altreEmail se sono disponibili altre email invio per conoscenza
     *
     */
//// TODO almaviva ELIMINATA
//    public void inviaMail(String utente, String response, String altreEmail) {
//        if (isScheduled()) {
//            int inizio = 0;
//            String stringa = toString();
//            int fine = stringa.length();
//            if (stringa.lastIndexOf(".") > 0)
//                inizio = (stringa.lastIndexOf(".") + 1);
//            if (stringa.indexOf("Factoring") > 0)
//                fine = stringa.indexOf("Factoring");
//            String funzione = stringa.substring(inizio, fine);
//            String e_mail = validator.getIndirizziEMail(utente);
//            if (e_mail == null)
//                e_mail = (IndiceServiceLocator.getProperty("ADMIN_MAIL"));
//            MailServer mail = new MailServer();
//            mail.setSubject("Risultato esecuzione differita di " + funzione);
//            //mail.setContent("File disponibile ... " + file_name );
//            mail.setContent(response);
//            mail.setToAddress(e_mail);
//            if (altreEmail != null)
//                mail.setCcAddresses(altreEmail);
//            if (mail.send() == 0)
//                log.info("Mail OK");
//            else
//                log.warn("Warning: Mail KO");
//        }
//    }
////  TODO almaviva ELIMINATA
//    public void inviaMailFtpBase(String utente, String messaggioDaInviare, String altreEmail) {
//        if (isScheduled()) {
//            String e_mail = validator.getIndirizziEMail(utente);
//            log.info(
//                "inviaMailFtpBase--> utente:"
//                    + utente
//                    + " mail:"
//                    + e_mail
//                    + " messaggio:"
//                    + messaggioDaInviare
//                   + " altreEmail:"
//                    + altreEmail);
//            //Seleziona la funzione dal nome della classe.
//            //es. it.finsiel.sbn.polo.factoring.transactionmaker.ImportaFactoring
//            //Seleziona Importa
//            int inizio = 0;
//            String nome = this.getClass().getName();
//            int fine = nome.length();
//            if (nome.lastIndexOf(".") > 0)
//                inizio = (nome.lastIndexOf(".") + 1);
//            if (nome.indexOf("Factoring") > 0)
//                fine = nome.indexOf("Factoring");
//            String funzione = (nome).substring(inizio, fine);
//            if (e_mail == null)
//                e_mail = (IndiceServiceLocator.getProperty("ADMIN_MAIL"));
//            MailServer mail = new MailServer();
//            String soggetto = "Risultato esecuzione differita di " + funzione;
//            mail.setSubject(soggetto);
//            mail.setContent(messaggioDaInviare);
//            mail.setToAddress(e_mail);
//            if (altreEmail != null)
//                mail.setCcAddresses(altreEmail);
//            if (mail.send() == 0)
//                log.info("Mail OK");
//            else {
//                log.warn("Warning: Mail KO");
//                log.info("Soggetto della mail: "+soggetto);
//                log.info("Contenuto della mail: "+messaggioDaInviare);
//            }
//        }
//    }
//  TODO almaviva ELIMINATA
//    public void inviaMailFtp(
//        String utente,
//        String biblio,
//        String authority,
//        String resto_del_messaggio,
//        String altreEmail) {
//        if (isScheduled()) {
//            String messaggioDaInviare = "Esporta n. " + idScript + "\n";
//            if (biblio != null && biblio.length() > 0) {
//                messaggioDaInviare += "\nFile MARC bibliografico disponibile ... " + biblio;
//            }
//            if (authority != null && authority.length() > 0) {
//                messaggioDaInviare += "\nFile MARC authority disponibile ... " + authority;
//            }
//            messaggioDaInviare += "\n\n" + resto_del_messaggio;
//            this.inviaMailFtpBase(utente, messaggioDaInviare, altreEmail);
//        }
//    }

    /**
     * Method getDbConn.
    * Metodo utile a tutte le classi di factoring
    * la sua signature è inserita nella classe di Interfaccia
     * @return Connection
     */
//    public Connection getDbConn() {
//        return this.db_conn;
//    }
    /**
     * Returns the scheduled.
     * @return boolean
     */
    public boolean isScheduled() {
        return scheduled;
    }
    /**
     * Sets the scheduled.
     * @param scheduled The scheduled to set
     */
    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }
    /**
     * Ritorna data/ora attuale
     * <br>
     * Rispetta il formato TIMESTAMP (stringa nel formato YYYYMMDDhhmmSS.t)
     * <br>
     * @param  nessuno
     * @return String data & ora
     */
    public static String getTimestamp() {
        Memorizator mem = new Memorizator();
        // Mi prendo preventivamente il timestamp di inizio attività
        return mem.getTimestamp();
    }
    /**
     * Set completo di un factoring effettuato
     * <br>
     * In questo caso ho precedentemente memorizzato il timestamp in ts_start
     * e memorizzo il tutto in Tb_tim in un solo step (in maniera asincrona)
     * tramite lo SCHEDULER.
     * <br>
     * @param  id_tipo_attivita
     * @param  id_server (1=SBN,2=SBNMARC,3=AMMINISTRAZIONE)
     * @param  fl_esito (S/N)
     * @param  ts_start  (stringa nel formato YYYYMMDDHHmmSS.t)
     * @param  ts_end  (stringa nel formato YYYYMMDDHHmmSS.t)
     * @param  cd_utente (codice di 12 caratteri)
     * @return nessuno
     */
// TODO almaviva ELIMINATA
//
//    public void setMemorizator(
//        int id_tipo_attivita,
//        int id_server,
//        String fl_esito,
//        String ts_start,
//        String ts_end,
//        String cd_utente) {
//        try {
//            //Se il factoring è già schedulato (import o export) esegue la scrittura in tb_tim direttamente
//            //ORA VA TUTTO IN DIRETTA.
//            //if (isScheduled()) {
//            new Memorizator().setFactoringInfo(
//                id_tipo_attivita,
//                id_server,
//                fl_esito,
//                ts_start,
//                ts_end,
//                cd_utente,
//                tp_materiale);
//            //} else {
//            //    Memorizator.start(id_tipo_attivita, id_server, fl_esito, ts_start, ts_end, cd_utente);
//            //}
//        }
//        // Se avviene un errore non devo intervenire sul processo di Factoring
//        // Piuttosto aggiorno il log file
//        catch (EccezioneMonitoraggio ex_mon) {
//            log.error(ex_mon);
//        }
//    }
//


    /** Deve essere ridefinito da ogni sottoclasse per restituire il codice dell'attività */
    public String getIdAttivita() {
        log.error("GetIdAttivita da implementare");
        return "";
    }
    /** Deve essere ridefinito da ogni sottoclasse per restituire il codice dell'attività */
    public String getIdAttivitaSt() {
        return getIdAttivita();
    }
    /** Restituisce la stringa contenente il codice dell'utente che ha eseguito la
     * richiesta.
     */
    public String getCdUtente() {
        return cd_utente;
    }

    public static final SbnUserType getCurrentUser() {
    	return currentUser.get();
    }

    public void setCdUtente(SbnUserType user) {
        cd_utente = user.getBiblioteca() + (user.getUserId() == null ? "      " : user.getUserId());
    }

    /**
     * Metodo per il controllo delle autorizzazioni
     */
    public void verificaAbilitazioni() throws EccezioneAbilitazioni, EccezioneSbnDiagnostico {
        if (!validator.verificaAttivitaID(getCdUtente(), getIdAttivita()))
            throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
    }

    /**
     * Metodo che prepara l'esecuzione in differita in notturna.
     * QUESTE RICHIESTE PASSANO PER LA FORM DI ATTIVAZIONE
     * DEI PROCESSI SBNMARC
     *
     * @return
     * @throws EccezioneSbnDiagnostico
     */
//// TODO almaviva ELIMINATA
//    protected String eseguiTransazioneNotturna() throws EccezioneSbnDiagnostico {
//        StringWriter sw = new StringWriter();
//        String SBNMarc_string = null;
//        try {
//            root_object.marshal(sw);
//            SBNMarc_string = sw.toString(); // ottengo in formato stringa il messaggio xml request
//        } catch (Exception e) {
//            log.error("Errore nel marshalling per salvataggio messaggio formato stringa:" + e);
//            throw new EccezioneSbnDiagnostico(101);
//        }
//        //Inserimento dell'sbnmarc in ldap
//        try {
//            //Ho bisogno di una nuova connessione: deve committare.
//            // assegna un nome univoco al file parametri
//            Script diff = new Script();
//            int pid = diff.getIdScript();
//            diff.inserisciRecord(
//                pid,
//                getIdAttivitaSt(),
//                getCdUtente(),
//                getCdUtente(),
//                "filename#sbnmarc_diff_" + pid + "#",
//                3);
//            //transaction.commit();
//            SbnmarcDifferitaBean sdb =
//                new SbnmarcDifferitaBean(
//                    pid,
//                    false,
//                    getCdUtente(),
//                    getIdAttivitaSt(),
//                    getDescAttivita(getIdAttivitaSt()),
//                    SBNMarc_string,
//                    System.currentTimeMillis(),
//                    null,
//                    null,
//                    null);
//            IndiceLDAPObjectLocator.setSbnmarcDifferitaRequest(sdb);
//            return "" + pid;
//        } catch (Exception ecc) {
//            log.error("Errore nel metodo eseguiTransazioneNotturna", ecc);
//            throw new EccezioneSbnDiagnostico(70, "Errore ", ecc);
//        }
//    }
////  TODO almaviva ELIMINATA
//    protected String schedulaTransazioneNotturna() throws EccezioneSbnDiagnostico {
//        StringWriter sw = new StringWriter();
//        String SBNMarc_string = null;
//        try {
//            root_object.marshal(sw);
//            SBNMarc_string = sw.toString(); // ottengo in formato stringa il messaggio xml request
//        } catch (Exception e) {
//            log.error("Errore nel marshalling per salvataggio messaggio formato stringa:" + e);
//            throw new EccezioneSbnDiagnostico(101);
//        }
//        try {
//            Script diff = new Script();
//            int pid = diff.getIdScript();
//            diff.inserisciRecord(
//                pid,
//                getIdAttivitaSt(),
//                getCdUtente(),
//                getCdUtente(),
//                "filename#sbnmarc_diff_" + pid + "#tipo#sbnmarc_differita#",
//                3);
//            ScheduledJobBean sjb =
//                new ScheduledJobBean(
//                    "" + getCdUtente(),
//                    "" + getCdUtente(),
//                    "" + getIdAttivita(),
//                    "",
//                    3,
//                    "iccu.box.engine.serversbnmarc.MainProcess",
//                    new String[] {
//            }, new String[] {
//            },
//            //ScheduledJobBean.START_NIGHT,
//            System.currentTimeMillis() + 10000, 1, 0, true);
//            //Setto il pid in modo che venga utilizzato dal JobHandler
//            sjb.setId_script(pid);
//            JobHandlerLocalHome jobHandlerHome =
//                (JobHandlerLocalHome) ServiceLocator.getInstance().getLocalHome("local:ejb/JobHandler");
//            JobHandlerLocal jobHandler = jobHandlerHome.create();
//            jobHandler.addNightJobSbnmarc(sjb);
//            log.info("scheduled job added successfully!");
//            //Lo carico nei file da eseguire
//            IndiceLDAPObjectLocator.uploadSbnmarc("sbnmarc_diff_" + pid, SBNMarc_string);
//            return "" + pid;
//        } catch (Exception ex) {
//            log.error("Errore nel metodo schedulaTransazioneNotturna", ex);
//            throw new EccezioneSbnDiagnostico(1, "Errore ", ex);
//        }
//    }

    /**
     * Restituisce la descrizione dell'attività dato il numero identificativo:
     * attenzione esegue una select nel DB.
     * @param id
     * @return
     * @throws InfrastructureException
     */
//    protected String getDescAttivita(int id) throws InfrastructureException {
//        Tbf_attivita att = new Tbf_attivita();
//        att.setID_TIPO_ATTIVITA(id);
//        try {
//            Tb_tp_attivitaResult attDB = new Tb_tp_attivitaResult(att);
//
//
//            attDB.selectPerKey(att.leggiAllParametro());
//            List v = attDB.getElencoRisultati();
//
//            if (v.size() > 0)
//                return ((Tb_tp_attivita) v.get(0)).getDS_ATTIVITA();
//        } catch (EccezioneDB ecc) {
//            log.error("Errore lettura descrizione attivita", ecc);
//        }
//        return "";
//    }
    /**
     * Metodo utilizzato se si tratta di un'operazione di esportazione dati,
     * nel qual caso si possono eseguire delle ricerche differenti
     * @param esporta true se si tratta di esporta
     */
    public void setEsporta(boolean esporta) {
        this.esporta = esporta;
    }

	protected String marshal(SBNMarc root) throws EccezioneSbnDiagnostico {
		return MarshallingUtil.marshal(root);
	}
    /**
     * @return
     */
    public UserTransaction getTransaction() {
        return transaction;
    }
    /**
     * @param transaction
     */
    public void setTransaction(UserTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * @param idScritp
     */
    public void setIdScript(String script) {
        idScript = script;
    }
    /**
     * Legge il valore di tp_materiale.
     * @return Returns the tp_materiale.
     */
    public String getTp_materiale() {
        return tp_materiale;
    }
    /**
     * Setta il valore di tp_materiale
     * @param tp_materiale The tp_materiale to set.
     */
    public void setTp_materiale(String tp_materiale) {
        this.tp_materiale = tp_materiale;
    }


    protected String assegnaNomeBlocco(int n) {
        if (!scheduled)
            return idLista +"_"+n;
        else
            return idLista +"_diff_"+n;
    }

}
