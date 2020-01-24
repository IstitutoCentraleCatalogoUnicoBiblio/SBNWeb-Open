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

import gnu.trove.THashSet;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.ServiceLocatorException;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.util.Elenco;
import it.finsiel.sbn.polo.factoring.util.ElencoArticoli;
import it.finsiel.sbn.polo.factoring.util.IndiceJMSObjectLocator;

import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.MarshallingUtil;
import it.finsiel.sbn.util.RandomIdGenerator;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Classe CercaFactoring
 * <p>
 * Classe "Dispatcher" - attiva i Factoring di tipo "Cerca"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 * </p>
 */
public abstract class CercaFactoring extends Factoring {

	private static final ThreadLocal<Set<String>> tlElencoBid = new ThreadLocal<Set<String>>() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected Set<String> initialValue() {
			return new THashSet(ResourceLoader.getPropertyInteger("NRO_MAX_RIGHE_RICHIESTA"));
		}
	};

    //tipoOrd: ordinamento richiesto, può essere su identificativo (vid) o sul nome (ky_cles1_a ky_cles2_a)
    protected String tipoOrd = null;
    //tipoOut: tipo di output richiesto: Esame, Lista
    protected SbnTipoOutput tipoOut = null;

    //Mantiene il resultSet che deve essere spacchettato
    protected TableDao tavola_response = null;

    protected int maxRighe;

    //attributi
    protected CercaType factoring_object = null;

	private Set<String> elencoBid;

    static Logger log = Logger.getLogger("iccu.serversbnmarc.CercaFactoring");

    /**
     * Metodo costruttore classe di factoring
     * <p>
     * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
     * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
     * </p>
     * @param  SBNMarc oggetto radice XML in input
     * @return istanza oggetto (default)
     */
    //costruttore
    public CercaFactoring(SBNMarc input_root_object) {
        // Assegno radice e classi XML principali
        super(input_root_object);

        elencoBid = tlElencoBid.get();
        elencoBid.clear();

        // Assegno classi specifiche per questo factoring
        factoring_object = request_object.getCerca();

        setOrder(""+factoring_object.getTipoOrd());
        tipoOut = factoring_object.getTipoOutput();
        idLista = factoring_object.getIdLista();

        if (factoring_object.getMaxRighe() <= 0)
            maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));
        else
            maxRighe = factoring_object.getMaxRighe();

        //almaviva5_20111003 max righe risposta
        if (factoring_object.hasLimit() && factoring_object.getLimit() > 0)
        	maxResponseRecord = factoring_object.getLimit();
    }

    public void setOrder(String in_tipoOrd) {
        tipoOrd = "order_" + in_tipoOrd;
    }

    /**
     * getFactoring - ritorna il Factoring opportuno
     * <p>
     * Metodo che lancia il Factoring verificando le informazioni ricevute in input
     * La Request verificata è di tipo SBnRequest (XML)
     * </p>
     * @param  nessuno
     * @return void
     */
    static public Factoring getFactoring(SBNMarc sbnmarcObj) throws EccezioneFactoring {
        Factoring current_factoring = null;
		CercaType factoring_object = sbnmarcObj.getSbnMessage().getSbnRequest().getCerca();
        log.debug("STO PER VERIFICARE COSA LANCIARE");
        // Creo il secondo livello di factoring (valutando input)
        if (factoring_object.getCercaElementoAut() != null) {
            current_factoring = CercaElementoAutFactoring.getFactoring(sbnmarcObj);
        } else if (factoring_object.getCercaLocalizzaInfo() != null) {
            current_factoring = new CercaLocalizzaInfoFactoring(sbnmarcObj);
        } else if (factoring_object.getCercaTitolo() != null) {
            CercaDatiTitType cercadati = factoring_object.getCercaTitolo().getCercaDatiTit();
            if (cercadati != null
                && cercadati.getNaturaSbnCount() == 1
                && cercadati.getNaturaSbn(0).equals("A")
                && cercadati.getTipoMaterialeCount() == 1
                && cercadati.getTipoMateriale(0).getType() == SbnMateriale.valueOf("U").getType()) {
                current_factoring = new CercaTitoloUniformeMusica(sbnmarcObj);
            } else {
                current_factoring = new CercaTitolo(sbnmarcObj);
            }
        } else if (factoring_object.getCercaSbnProfile() != null) {
            current_factoring = new CercaSbnProfile(sbnmarcObj);
        } else if (factoring_object.getCercaPropostaCorrezione() != null) {
            current_factoring = new CercaPropostaCorrezione(sbnmarcObj);

        } else
            throw new EccezioneFactoring(100);
        //log.info("HO CREATO IL CURRENT FACTORING:" + current_factoring + ":");


        return current_factoring;
    }

    /** Controlla che il numero di record non sia troppo grande */
    public void controllaNumeroRecord(int num) throws EccezioneSbnDiagnostico {
        numeroRecord = num;
        //Non c'è il controllo se è scheduled o esporta.
        if (esporta || isScheduled()) {
            return;
        } else if (num == 0) {
            throw new EccezioneSbnDiagnostico(3001,"Nessun elemento trovato");
        } else if (num > 1 && SbnTipoOutput.valueOf("000").getType() == tipoOut.getType()) {
            throw new EccezioneSbnDiagnostico(3236,"La ricerca ha individuato più di un elemento, esame analitico non ammesso");
        } else if (num > maxResponseRecord) {
            EccezioneSbnDiagnostico e = new EccezioneSbnDiagnostico(3003, "Troppi record");
            e.appendMessaggio("; Numero di record: " + num);
            throw e;
        }
    }

    /** Utilizzato qualora il risultato fosse su più pagine
     */
    public SBNMarc eseguiRecupero() {
        Serializable ret = null;
        try {
            //if (start >= numeroRecord) return "terminato";
            if (factoring_object.getIdLista().toUpperCase().startsWith("BIS")) {
                // almaviva non eseguo recupero bis in quanto non vengono gestite
                //ret = leggiRecuperoBis();
                if (ret == null)
                    ret = leggiRecupero(true);
            } else
                ret = leggiRecupero(false);
            if (ret == null) {
                log.error("Eccezione nella lettura del file xml di recupero");
                throw new EccezioneFactoring(102);
            }
//            if (ret instanceof String)
//            	return (String)ret;

            return (SBNMarc)ret;

        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            return FormatoErrore.buildMessaggioErrore(ecc, user_object);
        }
    }

//    private String leggiRecuperoBis() throws EccezioneFactoring {
//        ProcessoInDifferita dummy_proc = new ProcessoInDifferita();
//        try {
//            int id_processo = Integer.parseInt(factoring_object.getIdLista().substring(3));
//            int num_primo = factoring_object.getNumPrimo() - 1;
//            int righe_per_blocco =
//                Integer.parseInt(ResourceLoader.getPropertyString("BIS_RIGHE_PER_BLOCCO_SBNMARC"));
//            //TableDao names = dummy_proc.getListaFileId(id_processo);
//            //Da qua sotto tolgo il +1 finale.
//            //int num_blocco = ((num_primo - 1) / righe_per_blocco);
//            //String nome_file = id_processo + "_SBNMARC_" + num_blocco + ".xml";
//            String nome_file = id_processo + "_SBNMARC_" + num_primo + ".xml";
//            log.info("Restituisco file con nome: " + nome_file);
//            String ret = dummy_proc.getFile(nome_file);
//            //    for (Enumeration elenco = names.elements(); elenco.hasMoreElements();) {
//            //        String nome_file = (String) elenco.nextElement();
//            //        if (nome_file.indexOf("_SBNMARC_"+num_primo)>0) {
//            //            log.info("Restituisco file con nome: "+nome_file);
//            //            return dummy_proc.getFile(nome_file);
//            //        }
//            //    }
//            return ret;
//        } catch (Exception e) {
//            log.error("Errore in leggiRecuperoBis",e);
//            throw new EccezioneFactoring(102);
//        }
//    }

    private Serializable leggiRecupero(boolean diff) throws EccezioneFactoring {
        try {
            String nome;
            if (diff)
                nome = idLista.substring(3) + "_diff_" + factoring_object.getNumPrimo();
            else
                nome = idLista + "_" + factoring_object.getNumPrimo();

            log.info("leggo da JMS con nome file: " + nome);

            Serializable result = IndiceJMSObjectLocator.lookup(nome);
            return result;

        } catch (Exception e) {
            log.error("Eccezione nella lettura del file xml di recupero, ecc: " + e);
            throw new EccezioneFactoring(102);
        }
    }
    /**
     * Legge l'opportuno file da JMS. Se non lo trova genera eccezione.
     * @return
     * @throws EccezioneFactoring
     */
    public SBNMarc leggiRecuperoE() throws EccezioneFactoring {
		try {
			String nome = factoring_object.getIdLista() + "_" + factoring_object.getNumPrimo();
			log.info("leggo da JMS con nome file: " + nome);

			SBNMarc ret = (SBNMarc) IndiceJMSObjectLocator.lookup(nome);
			return ret;

		} catch (Exception e) {
			log.error("Eccezione nella lettura del file xml di recupero, ecc: " + e);
			throw new EccezioneFactoring(102);
		}
    }

    /**
     * Metodo principale invocato dall'esterno per dare avvio all'esecuzione
     * E' final perchè non deve essere sovrascritto.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    final public void eseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        try {
            verificaAbilitazioni();
            if (factoring_object.getIdLista() != null)
                object_response = eseguiRecupero();
            else {
                executeCerca();
                if (scheduled)
                    idLista = idScript;
                else if (numeroRecord > maxRighe) {
                    //Progressivi progr = new Progressivi();
                    //idLista = progr.getIdLista();
                }
                idLista = RandomIdGenerator.getId();
                object_response = preparaOutput();
            }
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            if (tavola_response != null) {
//                try {
//
//                } catch (EccezioneDB ex) {
//                    log.error("Errore chiusura statement",ex);
//                }
                tavola_response = null;
            }
            object_response = FormatoErrore.buildMessaggioErrore(ecc, user_object);
            //Se il processo è stato schedulato non ho bisogno di generare un'eccezione.
            //idem anche se non ci sono risultati
            if (ecc.getErrorID() != 3251 && ecc.getErrorID() != 3001)
                throw ecc;
        }
    }

    /**
     * Metodo utilizzato per preparare gli output alle fasi successive.
     * Se è un processo in differita viene anche inviata una mail.
     * E' final perchè non deve essere sovrascritto.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    final public void proseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (tavola_response != null) {
            try {
                //Salvo il file in JMS.
                //if (numeroRecord > maxRighe || scheduled) {
                if (scheduled) {
                    String nomeBlocco = assegnaNomeBlocco(1);
                    log.info("Scrivo in JMS con nome file: " + nomeBlocco);
                    try {
                        //Anche se non riesco a scrivere su JMS posso terminare correttamente
                        IndiceJMSObjectLocator.bind(nomeBlocco, object_response);
                    } catch (Exception e) {
                        log.error("Non riesco a scrivere su JMS.",e);
                        //Chiudo la tavola, non ha senso continuare ad eseguire
                        if (tavola_response != null) {
//                            try {
//
//                            } catch (EccezioneDB ex) {
//                                log.error("Errore chiusura statement",ex);
//                            }
                            tavola_response = null;
                        }
                    }
                }
                //Quale blocco sto estraendo
                int blockCounter = 1;
                int lastRowCounter = rowCounter;
                while (numeroRecord > 0 && rowCounter < numeroRecord) {
                    //while (tavola_response.hasMoreElements()) {
                    blockCounter++;
                    String nomeBlocco = assegnaNomeBlocco(blockCounter);
                    Serializable blocco = preparaOutput();
                    log.info("Scrivo in JMS con nome file: " + nomeBlocco);

                    IndiceJMSObjectLocator.bind(nomeBlocco, blocco);
                    //Controllo che si continui ad estrarre qualcosa, altrimenti esco
                    if (lastRowCounter == rowCounter) {
                        break;
                    } else {
                        lastRowCounter = rowCounter;
                    }
                }

            } catch (ServiceLocatorException e) {
                log.error("Problemi in scrittura del file di recupero",e);
            } catch (EccezioneIccu ecc) {
                log.debug("Errore, eccezione:" + ecc);
            }
        }
    }

    protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico, IllegalArgumentException, InvocationTargetException, Exception {
        log.error("PREPARA OUTPUT NON IMPLEMENTATO");
        throw new EccezioneSbnDiagnostico(1,"Codice non implementato");
    }
    protected void executeCerca() throws EccezioneAbilitazioni, EccezioneDB, EccezioneSbnDiagnostico, IllegalArgumentException, InvocationTargetException, Exception {
        throw new EccezioneSbnDiagnostico(1,"Codice non implementato");
    }

    /**
     *
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
//    public final SBNMarc eseguiTransazioneEsporta() throws IllegalArgumentException, InvocationTargetException, Exception {
//        SBNMarc sbnmarc = null;
//        try {
//            verificaAbilitazioni();
//            log.info(">>>>>>>>dddddddddddddd>>>>>>>recupero"+factoring_object.getIdLista());
//            executeCerca();
//            sbnmarc = preparaOutput();
//        } catch (EccezioneIccu ecc) {
//            if (tavola_response != null) {
//                try {
//
//                } catch (EccezioneDB ex) {
//                    log.error("Errore chiusura statement",ex);
//                }
//                tavola_response = null;
//            }
//            log.debug("Errore, eccezione:" + ecc,ecc);
//            //throw new EccezioneFactoring(56, "Errore in fase di esecuzione", ecc);
//            throw ecc;
//        }
//        return sbnmarc;
//    }

//    public final TableDao eseguiTransazioneEsportaDue() throws IllegalArgumentException, InvocationTargetException, Exception {
//        try {
//            log.info(">>>>>>>>dddddddddddddd>>>>>>>recupero"+factoring_object.getIdLista());
//            executeCerca();
//            return tavola_response;
//        } catch (EccezioneIccu ecc) {
//            if (tavola_response != null) {
//                try {
//
//                } catch (EccezioneDB ex) {
//                    log.error("Errore chiusura statement",ex);
//                }
//                tavola_response = null;
//            }
//            log.debug("Errore, eccezione:" + ecc,ecc);
//            //throw new EccezioneFactoring(56, "Errore in fase di esecuzione", ecc);
//            throw ecc;
//        }
//    }
    /**
     * Metodo utilizzato per preparare gli output alle fasi successive, solo per esporta:
     * non scrive su JMS.
     */
//
//    public final SBNMarc proseguiTransazioneEsporta() {
//        if (tavola_response == null) {
//            return null;
//        }
//        try {
//
//            //if (rowCounter < numeroRecord) {
//            if (tavola_response.hasMoreElements()) {
//                return preparaOutput();
//            }
//        } catch (EccezioneIccu ecc) {
//            log.debug("Errore, eccezione:" + ecc);
//        }
//        try {
//
//            tavola_response = null;
//        } catch (EccezioneDB ecc) {
//            log.debug("Errore, eccezione in closeStatement:" + ecc);
//        }
//        return null;
//    }
//
    /** Restituisce la stringa che segue l'asterisco */
    protected String dopoAsterisco(String stringa) {
        if (stringa == null)
            return null;
        int n = stringa.indexOf('*');
        if (n >= 0)
            return stringa.substring(n + 1);
        else {
            n = stringa.indexOf(' ');
            int n2 = stringa.indexOf('\'');
            if (n < 0 || (n2 >= 0 && n > n2)) {
                n = n2;
            }
            if (n >= 0)
                if (ElencoArticoli.getInstance().contiene(stringa.substring(0, n)))
                    return stringa.substring(n + 1);
        }
        return stringa;
    }

    /**
     * Metodo chiamato per estarre da tavola_response i titoli non doppi
     * @return TableDao di Tb_titolo
     * @throws EccezioneDB
     */
    protected List eliminaBidDuplicati() throws EccezioneDB {

    	log.debug("invocato metodo eliminaBidDuplicati() per thread n. " + Thread.currentThread().getId());

    	int size = ValidazioneDati.size(tavola_response.getElencoRisultati());
    	if (size == 1)	//analitica?
    		return tavola_response.getElencoRisultati(1);

        List response = tavola_response.getElencoRisultati(maxRighe);
        //Elimino i titoli che ho già trattato.
        Iterator<Tb_titolo> i = response.iterator();
        int deleted = 0;
        while (i.hasNext()) {
            Tb_titolo tit = i.next();
            if (elencoBid.contains(tit.getBID())) {
            	deleted++;
				i.remove();
			} else
                elencoBid.add(tit.getBID());
        }

        log.debug("bid eliminati: " + deleted);

        //Reintegro gli eventuali doppi scartati.
		while (response.size() < maxRighe) {
            List prossimi = tavola_response.getElencoRisultati(1);
            if (prossimi.size() == 0)
                break;
            else {
                Tb_titolo tit = (Tb_titolo)prossimi.get(0);
                if (!elencoBid.contains(tit.getBID()) ) {
                    response.add(tit);
                    elencoBid.add(tit.getBID());
                }
            }
        }
        return response;
    }
    /**
     * Prende un oggetto DOM Castor e ritorna il documento XML che
     * esso rappresenta in una stringa.
     * @param castorObject istanza di un oggetto castor radice di un documento xml
     * @return il documento xml che l'oggetto Castor rappresenta in una stringa
     */
    public static String castorObjectToString(SBNMarc sbnmarc) {
        String xml = null;
        if (sbnmarc != null) {
            try {
                // 1- Validate
                sbnmarc.validate();
                // 2- Marshall
                xml = MarshallingUtil.marshal(sbnmarc);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return xml;
    }

    public static String castorObjectToStringElement(SBNMarc sbnmarc) {
	    String xml = null;
	    if (sbnmarc != null  ) {
	        try {
	            StringWriter stringWriter = new StringWriter();
	            // 1- Validate
				SbnMessageType sbnMessage = sbnmarc.getSbnMessage();
				SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
				if(!sbnResponse.getSbnResult().getEsito().equalsIgnoreCase("0000")) {
					SbnResultType result = sbnResponse.getSbnResult();
					sbnmarc.validate();
	                // 2- Marshall
	                result.marshal(stringWriter);
	                xml = stringWriter.toString();
	                stringWriter.close();

				} else {
					SbnOutputType sbnOutput = sbnResponse.getSbnResponseTypeChoice().getSbnOutput();
					if(sbnOutput.getDocumento(0) instanceof DocumentoType) {
						DocumentoType doc =	sbnOutput.getDocumento(0);
					    sbnmarc.validate();
					    // 2- Marshall
					    doc.marshal(stringWriter);
					    xml = stringWriter.toString();
					    stringWriter.close();
					}
					else if(sbnOutput.getElementoAut(0) instanceof ElementAutType) {
						ElementAutType ele = sbnOutput.getElementoAut(0);
					    sbnmarc.validate();
					    // 2- Marshall
					    //ele.toString();
					    ele.marshal(stringWriter);
					    xml = stringWriter.toString();
					    stringWriter.close();
					}
				}
	            //sbnmarc.validate();
	            // 2- Marshall
	            //sbnmarc.marshal(stringWriter);
	            xml = stringWriter.toString();
	            stringWriter.close();
	        } catch (Exception x) {
	            x.printStackTrace();
	        }
	    }
	    return xml;
	}

	/**
     *
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */

    public final SBNMarc eseguiTransazioneEsporta() throws IllegalArgumentException, InvocationTargetException, Exception {
        SBNMarc sbnmarc = null;
        try {
            //verificaAbilitazioni();
            log.info(">>>>>>>>dddddddddddddd>>>>>>>recupero"+factoring_object.getIdLista());
            executeCerca();
            sbnmarc = preparaOutput();
        } catch (EccezioneIccu ecc) {
            if (tavola_response != null) {
//                try {
//                   // tavola_response.closeStatement();
//                } catch (EccezioneDB ex) {
//                    log.error("Errore chiusura statement",ex);
//                }
                tavola_response = null;
            }
            log.debug("Errore, eccezione:" + ecc,ecc);
            //throw new EccezioneFactoring(56, "Errore in fase di esecuzione", ecc);
            throw ecc;
        }
        return sbnmarc;
    }

    public final SBNMarc eseguiTransazioneEsportaLista(String[] t001) throws IllegalArgumentException, InvocationTargetException, Exception {
        SBNMarc sbnmarc = null;
        try {
            //verificaAbilitazioni();
            log.info(">>>>>>>>dddddddddddddd>>>>>>>recupero"+factoring_object.getIdLista());
            CercaTitolo estrae = null;
            for (int s = 0; s < t001.length; s++) {

    			estrae.cercaPerIdLista(t001[s]);
    			sbnmarc = preparaOutput();
    		}
            //sbnmarc = preparaOutput();
        } catch (EccezioneIccu ecc) {
            if (tavola_response != null) {
//                try {
//                   // tavola_response.closeStatement();
//                } catch (EccezioneDB ex) {
//                    log.error("Errore chiusura statement",ex);
//                }
                tavola_response = null;
            }
            log.debug("Errore, eccezione:" + ecc,ecc);
            //throw new EccezioneFactoring(56, "Errore in fase di esecuzione", ecc);
            throw ecc;
        }
        return sbnmarc;
    }



    public final TableDao eseguiTransazioneEsportaDue() throws IllegalArgumentException, InvocationTargetException, Exception {
        try {
            log.info(">>>>>>>>dddddddddddddd>>>>>>>recupero"+factoring_object.getIdLista());
            executeCerca();
            return tavola_response;
        } catch (EccezioneIccu ecc) {
            if (tavola_response != null) {
                tavola_response = null;
            }
            log.debug("Errore, eccezione:" + ecc,ecc);
            //throw new EccezioneFactoring(56, "Errore in fase di esecuzione", ecc);
            throw ecc;
        }
    }
    /**
     * Metodo utilizzato per preparare gli output alle fasi successive, solo per esporta:
     * non scrive su JMS.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public final SBNMarc proseguiTransazioneEsporta() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (tavola_response == null) {
            return null;
        }
        try {

            //if (rowCounter < numeroRecord) {
            //if (tavola_response.getCount() != 0) {
        	if (tavola_response != null) {
                return preparaOutput();
            }
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
        }
        //tavola_response.closeStatement();
		tavola_response = null;
        return null;
    }

    //almaviva5_20090706
    protected String[] normalizzaParolePerRicerca(String[] parole, Elenco... elenchi) throws EccezioneSbnDiagnostico {

    	List<String> tmp = new ArrayList<String>();
    	int length = ValidazioneDati.size(parole);
    	for (int i = 0; i < length; i++) {
    		String norm = ValidazioneDati.trimOrEmpty(NormalizzaNomi.normalizzazioneGenerica(parole[i]) );
    		// rieseguo lo split sulla singola stringa, questo perché
    		// la normalizzazione può aver sostituito dei separatori con spazio
    		// e risulta necessario trattare le parole composte (es. "nord-ovest", "d'amato")
    		// come due parole distinte
    		tmp.addAll(Arrays.asList(norm.split("\\s")));
    	}

    	Iterator<String> i = tmp.iterator();
    	while (i.hasNext()) {
    		String parola = i.next();
    		// cancello le stringhe vuote e quelle incluse nella stoplist
    		if (!ValidazioneDati.isFilled(parola) ) {
				i.remove();
				continue;
			}

    		if (ValidazioneDati.isFilled(elenchi))
    			for (Elenco e : elenchi)
    				if (e.contiene(parola) ) {
    					i.remove();
    					continue;
    				}
    	}

    	//almaviva5_20130205 #5240 check numero parole dopo normalizzazione
    	if (!ValidazioneDati.isFilled(tmp))
    		throw new EccezioneSbnDiagnostico(9349);	//Nessuna parola significativa

		return tmp.toArray(new String[0]);

    }

}
