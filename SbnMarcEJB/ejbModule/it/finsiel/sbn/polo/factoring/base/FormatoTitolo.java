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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ProfilerCache;
import it.finsiel.sbn.polo.factoring.profile.ValidatorContainerObject;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_2;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe FormatoTitolo.java
 * <p>
 * Ogni tipo di titolo ha un proprio formato con il quale deve essere creato l'XML. Anche
 * i vari legami dipendono dal tipo di titolo.
 * </p>
 *
 * @author
 * @author
 *
 * @version 16-gen-03
 */
public abstract class FormatoTitolo {

    //file di log
    static Category log = Category.getInstance("iccu.box.FormatoTitolo");

    SbnTipoOutput tipoOut;
    String tipoOrd;

    SbnUserType user;

    BigDecimal schema_version = null;

    //Messaggio utile per l'esporta.
    static public String messaggio_errore_per_esporta;

    //protected static ValidatorAdmin validator = ValidatorAdmin.getInstance();
    protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();

    /**
     * Crea un oggetto xml da un oggetto di tipo Tb_titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     *
     */
    public abstract Object formattaTitolo(Tb_titolo titolo, BigDecimal versione) throws EccezioneDB, EccezioneSbnDiagnostico, IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception;
    public abstract void aggiungiAltriLegami(Tb_titolo titolo,DocumentoType doc) throws EccezioneDB, EccezioneSbnDiagnostico, IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception;

    /**
     * Ogni tipo di titolo ha un proprio formato con il quale deve essere creato l'XML. Anche
     * i vari legami dipendono dal tipo di titolo.
     * Questo metodo consente di ottenere una classe in base al tipo di titolo in grado di
     * gestire tutte le formattazioni del titolo.
     * @param tipoOut il tipo di output richiesto
     * @param tipoOrd il tipo di ordinamento richiesto
     * @param user l'utente che esegue la richiesta
     * @param conn una connessione al db
     * @param titolo l'oggetto di cui si deve preparare l'output
     * @return una classe che estende il FormatoTitolo.
     */
    public static FormatoTitolo getInstance(
        SbnTipoOutput tipoOut,
        String tipoOrd,
        SbnUserType user,
        BigDecimal schema_version,
        Tb_titolo titolo) {
        FormatoTitolo ritorno = null;
     // Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
        if (titolo.getCD_NATURA().equals("M")
            || titolo.getCD_NATURA().equals("W")
            || titolo.getCD_NATURA().equals("C")
            || titolo.getCD_NATURA().equals("N")
            || titolo.getCD_NATURA().equals("S")
            || titolo.getCD_NATURA().equals("R")) {
            //documento.datidocumento
            ritorno = FormatoDocumento.getInstance(tipoOut, tipoOrd, user, titolo);
        } else if (
            titolo.getCD_NATURA().equals("D")
                || titolo.getCD_NATURA().equals("B")
                || titolo.getCD_NATURA().equals("P")
                || titolo.getCD_NATURA().equals("T")) {
            //documento.titaccesso
            ritorno = new FormatoTitoloAccesso(tipoOut, tipoOrd, titolo);
        } else if (titolo.getCD_NATURA().equals("A") || titolo.getCD_NATURA().equals("V")) {
            //IN QUESTO CASO NON SI RITORNA UN DOCUMENTO TYPE
        	// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
            ritorno = FormatoElementAut.getInstance(tipoOut, tipoOrd, titolo, user);
        } else {
            log.error(
                "titolo di tipo sconosciuto: titolo -"
                    + titolo.getBID()
                    + "- , tipo -"
                    + titolo.getCD_NATURA()
                    + "-");
        }
        ritorno.user = user;
        ritorno.schema_version = schema_version;
        return ritorno;
    }
    /*
    	static protected FormatoTitolo getFormatoDocumento(
    		SbnTipoOutput tipoOut,
    		String tipoOrd,
    		Connection conn,
    		Tb_titolo titolo) {
    		DocumentoType doc = null;
    		FormatoDocumento formatoDoc = new FormatoDocumento(this);
    		if (tipoOut.equals(tipoOut.VALUE_0)) { //Analitico
    			doc = formatoDoc.formattaDatiDocumentoPerEsameAnalitico(titolo);
    		} else {
    			doc = formatoDoc.formattaDatiDocumentoPerListaSintetica(titolo);
    			TitoloCerca titoloCerca =
    				new TitoloCerca(tipoOut, tipoOrd, formatoDoc);
    			titoloCerca.formattaLegamiDocDoc(titolo.getBID());
    		}
    		return doc;
    	}

    	static protected FormatoTitolo getFormatoTitoloAccesso(
    		SbnTipoOutput tipoOut,
    		String tipoOrd,
    		Connection conn,
    		Tb_titolo titolo) {
    		DocumentoType doc = null;
    		FormatoTitoloAccesso formato = new FormatoTitoloAccesso(this);
    		if (tipoOut.equals(tipoOut.VALUE_0))
    			doc = formato.formattaTitAccessoPerEsameAnalitico(titolo);
    		else
    			doc = formato.formattaTitAccessoPerListaSintetica(titolo);
    		return doc;
    	}

    	static protected FormatoTitolo getFormatoElementoAut(
    		SbnTipoOutput tipoOut,
    		String tipoOrd,
    		Connection conn,
    		Tb_titolo titolo) {
    		ElementAutType doc = null;
    		FormatoElementAut formato = new FormatoElementAut(this);
    		if (!tipoOut.equals(tipoOut.VALUE_0))
    			doc = formato.formattaElementoAutPerListaSintetica(titolo);
    		else if (tipoOut.equals(tipoOut.VALUE_1))
    			doc = formato.formattaElementoAutPerEsameAnalitico(titolo);
    		return doc;

    	}
    */
    protected ElementAutType settaLegamiElementAut(ElementAutType doc, Tb_titolo titolo) {
        //Setto i legami di doc
        return doc;
    }

    /**Formatta un documento senza i legami con altri documenti
     * Non lo faccio più abstract.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public abstract Object formattaElemento(Tb_titolo titolo, BigDecimal versione)
    	throws EccezioneDB, EccezioneSbnDiagnostico, IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception;
    public abstract Object formattaElemento(Tb_titolo titolo, BigDecimal versione, boolean gestisciNaturaV)
    	throws EccezioneDB, EccezioneSbnDiagnostico, IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception;


    public boolean visualizzaTimestamp() {
        if (validator.isPolo(user.getBiblioteca()))
            return false;
        return true;
    }
    /** Filtra legami tra documenti, ritorna true quando un legame deve essere
     * presentato, false se non interessa l'utente.
     * @param tipoAuthority il tipo di authority di cui inserire il legame verificare
     * @param tipoDocumento la stringa che rappresenta il documento con il quale è
     * il legame preso in considerazione
     */
    public boolean filtraLegame(String tipoAuthority) {
        //Controllo utente, codice del legame e tipo documento legato
        //Utilizza i parametri e il metodo getParametriUtente
        ValidatorContainerObject valc = validator.getAbilitazioni(user.getBiblioteca() + user.getUserId());
        if (valc != null) {
            Tbf_par_auth par_auth = valc.get_parametri_authority(tipoAuthority);
            if (par_auth != null)
                if (par_auth.getFl_leg_auth()=='S')
                    return true;
                else
                    return false;
        }
        return false;
    }

    /** Filtra legami tra documento e autore
     * presentato, false se non interessa l'utente.
     */
    public boolean filtraLegameAutore(Vl_autore_tit tit_aut) {
        //Controllo utente, codice del legame e tipo documento legato
        //Utilizza i parametri e il metodo getParametriUtente
        ValidatorContainerObject valc = validator.getAbilitazioni(user.getBiblioteca() + user.getUserId());
        if (valc != null) {
        List v = valc.getElenco_parametri();
        Tbf_parametro par;
        if (tit_aut.getFL_SUPERFLUO().equals("S") && v != null)
            for (int i = 0; i < v.size(); i++) {
                par = (Tbf_parametro) v.get(i);
                if (par.getFl_aut_superflui()=='S')
                    return filtraLegame("AU");
                else
                    return false;
            }
        }
        return filtraLegame("AU");
    }

    /** Filtra legami tra due documento
     * presentato, false se non interessa l'utente.
     */
    public boolean filtraLegameDocumenti(Vl_titolo_tit_b tit_tit) {
        //Controllo utente, codice del legame e tipo documento legato
        //Utilizza i parametri e il metodo getParametriUtente
        ValidatorContainerObject valc = validator.getAbilitazioni(user.getBiblioteca() + user.getUserId());
        if (valc != null) {
            List v = valc.getElenco_parametri();
            Tbf_parametro par;
            if (v!=null) {
                for (int i = 0; i < v.size(); i++) {
                    par = (Tbf_parametro) v.get(i);
                    return ReticoloLegamiDocumenti.verificaLivello(tit_tit, par.getTp_ret_doc());
                }
            }
        }
        return false;
    }

    /** Verifica in tb_codici quale sia il tipo di legame corrispondente ad uno dato */
    public String convertiTpLegame(String tipoLegame, String cd_natura1, String cd_natura2) {
        String tp_legame = cd_natura1 + tipoLegame + cd_natura2;
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_unimarc("LICR", tp_legame);
        if (tp_legame == null)
            return null;
        return tp_legame.substring(1, tp_legame.length() - 1);

    }

    /** Verifica in tb_codici quale sia il tipo di legame corrispondente ad uno dato */
    public String convertiTpLegameDaUnimarc(String tipoLegame, String cd_natura1, String cd_natura2) {
        String tp_legame = cd_natura1 + tipoLegame + cd_natura2;
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
        if (tp_legame == null)
            return null;
        return tp_legame.substring(1, tp_legame.length() - 1);

    }

    private String getCodiceLingua(String lingua) {
        String temp = null;
        if (lingua != null && lingua.trim().length()>0) {
            temp = Decodificatore.getCd_unimarc("LING", lingua);
            if (temp == null && lingua.trim().length() > 0) {
                temp = lingua;
            }
        }
        return temp;
    }

    protected C101 formattaT101(Tb_titolo titolo) {
        if (titolo.getCD_LINGUA_1() == null || titolo.getCD_LINGUA_1().trim().equals(""))
            return null;
        C101 c101 = new C101();
        String lingua = getCodiceLingua(titolo.getCD_LINGUA_1());
        if (lingua != null)
            c101.addA_101(lingua);
        lingua = getCodiceLingua(titolo.getCD_LINGUA_2());
        if (lingua != null)
            c101.addA_101(lingua);
        lingua = getCodiceLingua(titolo.getCD_LINGUA_3());
        if (lingua != null)
            c101.addA_101(lingua);
        return c101;
    }

    /** Verifica se un tipo di materiale deve esssere specializzato oppure no invocando il Validator */
    protected static boolean specializzaMateriale(String tpMateriale, SbnUserType user) {

    	//almaviva5_20170116 #6346
    	//Modificato il caricamento del profilo da utente a polo affinchè tutte le interrogazioni siano coerenti
    	//tra base dati locale e indice.
        //ValidatorContainerObject valc = validator.getAbilitazioni(user.getBiblioteca() + user.getUserId());
    	ValidatorContainerObject valc = validator.getAbilitazioniPolo(ProfilerCache.getPolo().getCd_polo());
        if (valc == null)
            return false;
        Tbf_par_mat par_mat = valc.get_parametri_materiale(tpMateriale);
        if (par_mat != null)
			if (par_mat.getTp_abilitaz() == 'S')
                return true;
            else
                return false;
        return false;
    }

    /** Prepara un elemento di allineamento inserendovi i titoli estratti dal db in base al bid
     * già presente nell'elemento Allinea. Anche l'oggettoVariato deve già essere stato creato
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    public static AllineaInfoType formattaElementoPerAllinea(
        AllineaInfoType allinea,
        SbnUserType utente,
        SbnTipoOutput tipoOut,
        BigDecimal schema_version)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {

    		throw new EccezioneDB(501, "FormatoTitolo, formattaElementoPerAllinea NON IMPLEMENTATO");

    }

    /**
     * Prepara l'oggetto Sbnmarc di risposta
     * Formatta una lista di titoli.
     * @return oggetto SBNMarc contenente l'xml
     */
    public static SBNMarc formattaLista(
        List titoli,
        SbnUserType utente,
        SbnTipoOutput tipoOut,
        SbnTipoOrd tipoOrd,
        String idLista,
        int primoElemento,
        int maxRighe,
        int totRighe,
        BigDecimal versione,
        String codice,
        String messaggio,
        boolean ignoraErrore)
        throws EccezioneDB, EccezioneSbnDiagnostico {
        int totEl = titoli.size();
        int ultimoEl = totEl > maxRighe ? maxRighe : totEl;

        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        sbnmarc.setSbnMessage(message);
        SbnUserType user = new SbnUserType();
        sbnmarc.setSbnUser(utente);
        sbnmarc.setSchemaVersion(versione);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        //sono due tipi di vettore :(
        //TableDao docs = new TableDao();
        //TableDao elAut = new TableDao();
        Tb_titset_2 tb_titset2 = null;
		int righeScartate=0;
		float f = versione.floatValue(); // almaviva7/almaviva4 13/05/2016
     	float vf = (float)2.03;




        Tb_titolo tb_titolo;
        Object documento;
        for (int i = 0; i < totEl; i++) {
            tb_titolo = (Tb_titolo) titoli.get(i);

            // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
            if ((tb_titolo.getCD_NATURA().equals("V") )) // almaviva7/almaviva4 13/05/2016
            {
            	if (f < vf)
            	{
            	righeScartate++;
            	continue; // Non generiamo output per na natura V se versione < 2.03
            	}
            }


            try {
                documento =
                    FormatoTitolo
                        .getInstance(
                            tipoOut,
                            tipoOrd == null ? null : "order_" + tipoOrd,
                            utente,
                            versione,
                            tb_titolo)
                        .formattaTitolo(tb_titolo, versione);

                if (documento instanceof DocumentoType) {
                    //docs.add(documento);
                	output.addDocumento((DocumentoType)documento);
                    ((DocumentoType)documento).setNLista(i);
                } else if (documento instanceof ElementAutType) {
                    //elAut.add(documento);
                	output.addElementoAut((ElementAutType)documento);
                    ((ElementAutType)documento).setNLista(i);
                }
            } catch (EccezioneDB ecc) {
                //Se si hanno problemi nel db, non visualizzo l'errore legato al titolo
                throw ecc;
            } catch (EccezioneSbnDiagnostico e) {
            	throw e;
            } catch (Exception ecc) {
                log.error("Errore durante elaborazione del documento:" + tb_titolo.getBID(), ecc);
                //Solo se sono in esporta
                if (ignoraErrore) {
					if (messaggio_errore_per_esporta != null)
                        messaggio_errore_per_esporta += "Errore documento:"
                            + tb_titolo.getBID()
                            + "; ecc. "
                            + ecc
                            + "\n";
                } else {
                    EccezioneSbnDiagnostico ex = new EccezioneSbnDiagnostico(3940, "Errore su un documento");
                    ex.appendMessaggio("Bid documento:" + tb_titolo.getBID());
                    throw ex;
                }
            }
        }
        //if (ricercaUnivoca && numElementi > 0)
        //    numeroRecord = 1;

        //for (int i = 0; i < docs.size(); i++)
        //    output.addDocumento((DocumentoType) docs.get(i));
        //for (int i = 0; i < elAut.size(); i++)
        //    output.addoAut((ElementAutType) elAut.get(i));
        if (codice == null) {
            if (totRighe > 0) {
                result.setEsito("0000"); //Esito positivo
                result.setTestoEsito("OK");
                if (idLista != null) {
                    output.setIdLista(idLista);
                    output.setMaxRighe(maxRighe);
                    output.setNumPrimo(primoElemento + 1);
                    //output.setTotRighe(totRighe);
                }
                output.setTipoOrd(tipoOrd);
            } else {
                result.setEsito("3001");
                result.setTestoEsito("Nessun elemento trovato");
            }
        } else {
            result.setEsito(codice); //Esito positivo
            result.setTestoEsito(messaggio);
            if (idLista != null) {
                output.setIdLista(idLista);
                output.setMaxRighe(maxRighe);
                output.setNumPrimo(primoElemento + 1);
                //output.setTotRighe(totRighe);
            }
            output.setTipoOrd(tipoOrd);
        }
        output.setTotRighe(totRighe);
        output.setTipoOutput(tipoOut);
        return sbnmarc;
    }

//    // formatta lista per bis
//    public static SBNMarc formattaVectorFile(
//        Connection conn,
//        VectorFileTitolo titoli,
//        SbnUserType utente,
//        SbnTipoOutput tipoOut,
//        SbnTipoOrd tipoOrd,
//        String idLista,
//        int primoElemento,
//        int maxRighe,
//        int totEl,
//        BigDecimal versione,
//        String codice,
//        String messaggio)
//        throws EccezioneDB, EccezioneSbnDiagnostico {
//        int ultimoEl = (totEl > (primoElemento + maxRighe) ? primoElemento + maxRighe : totEl);
//        SBNMarc sbnmarc = new SBNMarc();
//        SbnMessageType message = new SbnMessageType();
//        SbnResponseType response = new SbnResponseType();
//        SbnResultType result = new SbnResultType();
//        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
//        SbnOutputType output = new SbnOutputType();
//        sbnmarc.setSbnMessage(message);
//        SbnUserType user = new SbnUserType();
//        sbnmarc.setSbnUser(utente);
//        sbnmarc.setSchemaVersion(versione);
//        message.setSbnResponse(response);
//        response.setSbnResult(result);
//        response.setSbnResponseTypeChoice(responseChoice);
//        responseChoice.setSbnOutput(output);
//        //sono due tipi di vettore :(
//        TableDao docs = new TableDao();
//        TableDao elAut = new TableDao();
//
//        Tb_titolo tb_titolo;
//        Object documento;
//        for (int i = primoElemento; i < ultimoEl; i++) {
//            tb_titolo = (Tb_titolo) titoli.elementAt(i);
//            if (tb_titolo == null) {
//            	throw new EccezioneSbnDiagnostico(-1);
//            }
//            try {
//                documento =
//                    FormatoTitolo
//                        .getInstance(
//                            tipoOut,
//                            tipoOrd == null ? null : "order_" + tipoOrd,
//                            utente,
//                            versione,
//                            conn,
//                            tb_titolo)
//                        .formattaTitolo(tb_titolo);
//                SbnOutputTypeChoiceItem item = new SbnOutputTypeChoiceItem();
//                SbnOutputTypeChoice choice = new SbnOutputTypeChoice();
//                choice.setSbnOutputTypeChoiceItem(item);
//                output.addSbnOutputTypeChoice(choice);
//                if (documento instanceof DocumentoType)
//                {
//                	item.setDocumento((DocumentoType)documento);
//                    ((DocumentoType)documento).setNLista(i);
//                }
//                else if (documento instanceof ElementAutType)
//                {
//                	item.setElementoAut((ElementAutType)documento);
//                    ((ElementAutType)documento).setNLista(i);
//                }
//
//            } catch (EccezioneDB ecc) {
//                //Se si hanno problemi nel db, non visualizzo l'errore legato al titolo
//                throw ecc;
//            } catch (Exception ecc) {
//                log.error("Errore durante elaborazione del documento:" + tb_titolo.getBID(), ecc);
//                //Solo se sono in esporta
//                if (messaggio_errore_per_esporta != null)
//                    messaggio_errore_per_esporta += "Errore documento:"
//                        + tb_titolo.getBID()
//                        + "; ecc. "
//                        + ecc
//                        + "\n";
//                else {
//                    EccezioneSbnDiagnostico ex = new EccezioneSbnDiagnostico(3940, "Errore su un documento");
//                    ex.appendMessaggio("Bid documento:" + tb_titolo.getBID());
//                    throw ex;
//                }
//            }
//        }
//        //if (ricercaUnivoca && numElementi > 0)
//        //    numeroRecord = 1;
//
//        if (codice == null) {
//            if (totEl > 0) {
//                result.setEsito("0000"); //Esito positivo
//                result.setTestoEsito("OK");
//                if (idLista != null) {
//                    output.setIdLista(idLista);
//                    output.setMaxRighe(maxRighe);
//                    output.setNumPrimo(primoElemento + 1);
//                    //output.setTotRighe(totRighe);
//                }
//                output.setTipoOrd(tipoOrd);
//            } else {
//                result.setEsito("3001");
//                result.setTestoEsito("Nessun elemento trovato");
//            }
//        } else {
//            result.setEsito(codice); //Esito positivo
//            result.setTestoEsito(messaggio);
//            if (idLista != null) {
//                output.setIdLista(idLista);
//                output.setMaxRighe(maxRighe);
//                output.setNumPrimo(primoElemento + 1);
//                //output.setTotRighe(totRighe);
//            }
//            output.setTipoOrd(tipoOrd);
//        }
//        output.setTotRighe(totEl);
//        output.setTipoOutput(tipoOut);
//        return sbnmarc;
//    }
//
    /**
     * Crea il tipo di legame tra un autore e un titolo.
     * @param tp_nome
     * @param tp_respons
     * @return
     */
    public SbnLegameAut convertiTipoLegameAutore(String tp_nome, String tp_respons) {
        if (tp_nome.equals("A") || tp_nome.equals("B") || tp_nome.equals("C") || tp_nome.equals("D")) {
            if (tp_respons.equals("1"))
                return (SbnLegameAut.valueOf("700"));
            else if (tp_respons.equals("2"))
                return (SbnLegameAut.valueOf("701"));
            else
                return (SbnLegameAut.valueOf("702"));
        } else {
            if (tp_respons.equals("1"))
                return (SbnLegameAut.valueOf("710"));
            else if (tp_respons.equals("2"))
                return (SbnLegameAut.valueOf("711"));
            else
                return SbnLegameAut.valueOf("712");
        }

    }

    public BigDecimal getSchema_version() {
        return schema_version;
    }
    /**
     * Formatta i campi in output per il timbro di condivisione
     */
//	public boolean formattaTimbroCondivisione(Tb_titolo tabella, boolean legame) {
//			if(legame){
//				log.debug("TIMBRO CONDIVISIONE ---------- LEGAME-dati da tabella" + tabella.getClass().getName());
//			}else{
//				log.debug("TIMBRO CONDIVISIONE ---------- ELEMENTO-dati da tabella" + tabella.getClass().getName());
//			}
//			if (tabella.getFL_CONDIVISO() == null) {
//				System.out
//						.println("ERRORE"
//								+ "IL FLAG CONDIVISO NON e' ISTANZIATO CORRETTAMENTE NELLA VISTA"
//								+ tabella.getClass().getName());
//				return true;
//			} else {
//				if (tabella.getFL_CONDIVISO().equals("S"))
//					return true;
//				else
//					return false;
//			}
//		}
//    /**
//     * Formatta i campi in output per il timbro di condivisione
//     */
//	public boolean formattaTimbroCondivisione(Tb_autore tabella, boolean legame) {
//		if(legame){
//			log.debug("TIMBRO CONDIVISIONE ---------- LEGAME-dati da tabella" + tabella.getClass().getName());
//		}else{
//			log.debug("TIMBRO CONDIVISIONE ---------- ELEMENTO-dati da tabella" + tabella.getClass().getName());
//		}
//		if (tabella.getFL_CONDIVISO() == null) {
//			System.out
//					.println("ERRORE"
//							+ "IL FLAG CONDIVISO NON e' ISTANZIATO CORRETTAMENTE NELLA VISTA"
//							+ tabella.getClass().getName());
//			return true;
//		} else {
//			if (tabella.getFL_CONDIVISO().equals("S"))
//				return true;
//			else
//				return false;
//		}
//	}

//    /**
//     * Formatta i campi in output per il timbro di condivisione
//     */
//	public boolean formattaTimbroCondivisione(Vl_autore_tit tabella, boolean legame) {
//		if(legame){
//			log.debug("TIMBRO CONDIVISIONE ---------- LEGAME-dati da tabella" + tabella.getClass().getName());
//		}else{
//			log.debug("TIMBRO CONDIVISIONE ---------- ELEMENTO-dati da tabella" + tabella.getClass().getName());
//		}
//		if(legame){
//			if (tabella.getFL_CONDIVISO_LEGAME() == null) {
//				System.out
//						.println("ERRORE"
//								+ "IL FLAG CONDIVISO NON e' ISTANZIATO CORRETTAMENTE NELLA VISTA"
//								+ tabella.getClass().getName());
//				return true;
//			} else {
//				if (tabella.getFL_CONDIVISO_LEGAME().equals("S"))
//					return true;
//				else
//					return false;
//			}
//		}
//		else{
//			if (tabella.getFL_CONDIVISO() == null) {
//				System.out
//						.println("ERRORE"
//								+ "IL FLAG CONDIVISO NON e' ISTANZIATO CORRETTAMENTE NELLA VISTA"
//								+ tabella.getClass().getName());
//				return true;
//			} else {
//				if (tabella.getFL_CONDIVISO().equals("S"))
//					return true;
//				else
//					return false;
//			}
//		}
//	}

}
