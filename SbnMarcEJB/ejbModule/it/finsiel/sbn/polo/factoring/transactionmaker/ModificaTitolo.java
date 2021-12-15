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


import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.CostruttoreIsbd;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.base.Isbd;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.Link_multim;
import it.finsiel.sbn.polo.oggetti.Nota;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloModifica;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloValidaModifica;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.C321;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoNota321;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;



/**
 * Classe ModificaTitolo
 * <p>
 * Modifica il titolo ed i relativi legami.
 * </p>
 *
 * @author
 * @author
 *
 * @version 28-mag-2003
 */

/**
 Adeguamento versione 1.10 dell'indice 27.02.2015
 */
public class ModificaTitolo extends ModificaFactoring {

	//almaviva5_20150324 costanti per controlli su stringa messaggio
    private static final String MSG_LEGAME_INSERITO_CORRETTAMENTE = "Legame inserito correttamente";
	private static final String MSG_SPECIFICITA_MODIFICATA_CORRETTAMENTE = "Specificità modificata correttamente";
	private static final String MSG_LEGAME_NON_INSERITO = "Legame non inserito: ";
	private static final String MSG_SPECIFICITA_NON_MODIFICATA = "Specificità non modificata: ";
	private static final String MSG_TITOLO_BASE_MODIFICATO_CORRETTAMENTE = "Titolo base modificato correttamente";

	private TimestampHash timeHash = new TimestampHash();
    private String T001 = null;
    private DatiDocType datiDoc = null;
    private DocumentoType documento = null;
    private ModificaType modificaType;
    private LegamiType[] legami;

    private SbnLivello livelloAut;
    private SbnSimile tipoControllo;
    private String id_lista = null;
    private Tb_titolo titoloModificato = null;
    private StatoRecord statoRecord;
    private boolean modVariante = false;
    private String  _condiviso;
    int maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));
    private SbnUserType user;
    /**
     * Constructor ModificaTitolo.
     * @param sbnmarcObj
     */
    public ModificaTitolo(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
        documento = modificaType.getDocumento();
        datiDoc = documento.getDocumentoTypeChoice().getDatiDocumento();

        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
        datiDoc.setVersioneSchema(sbnmarcObj.getSchemaVersion().toString());


        T001 = datiDoc.getT001();
        if (datiDoc.getTipoMateriale() != null) {
            setTp_materiale(datiDoc.getTipoMateriale().toString());
        }
        livelloAut = datiDoc.getLivelloAutDoc();
        statoRecord = documento.getStatoRecord();
        //controllare i legami!!!!
        legami = documento.getLegamiDocumento();
        tipoControllo = modificaType.getTipoControllo();
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiDoc.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiDoc.getCondiviso().getType() == DatiDocTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiDoc.getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiDoc.getCondiviso() != null && (datiDoc.getCondiviso().equals(DatiDocTypeCondivisoType.S)) ) {
//        	_condiviso = datiDoc.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }

    }

    /**
     * Prepara l'xml di risposta
     * @return Stringa contenente l'xml
     */


    // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
    // si aggiunge ai parametri anche il campo del messaggio di specificità e la sua gestione all'interno del metodo
//    private String formattaOutput() throws EccezioneDB, EccezioneFactoring {
    private SBNMarc formattaOutput(String msgSpecificita, String msgTitoloBase) throws EccezioneDB, EccezioneFactoring {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000"); //Esito positivo

//        result.setTestoEsito("OK");
        StringBuilder buf = new StringBuilder(128);
        buf.append("OK - ");
        boolean hasBase = ValidazioneDati.isFilled(msgTitoloBase);
		if (hasBase)
        	buf.append(msgTitoloBase);
        boolean hasSpec = ValidazioneDati.isFilled(msgSpecificita);
		if (hasBase && hasSpec)
        	buf.append(" / ");
        if (hasSpec)
        	buf.append(msgSpecificita);
        result.setTestoEsito(buf.toString());

        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(root_object.getSchemaVersion());
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        DatiDocType datiResp = new DatiDocType();
        datiResp.setT001(titoloModificato.getBID());
        SbnDatavar data = new SbnDatavar(titoloModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAutDoc(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(titoloModificato.getCD_LIVELLO())));
        DocumentoType elementoAutResp = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        docChoice.setDatiDocumento(datiResp);
        elementoAutResp.setDocumentoTypeChoice(docChoice);
        output.addDocumento(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }
    // Fine almaviva2 03.08.2010 - Modifiche riportate dal software di Indice


    /**
     * Method executeModifica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {
        boolean modificato = false;
        if (statoRecord != null && statoRecord.getType() == StatoRecord.C_TYPE) {
            modificato = true;
        } else if (statoRecord != null && StatoRecord.V.getType() == statoRecord.getType()) {
            modVariante=true;
        } else {
            //Se non è modificato devono esistere dei legami.
            if (modificaType.getDocumento().getLegamiDocumentoCount() == 0)
                throw new EccezioneSbnDiagnostico(3090, "Nessuna modifica da apportare");
        }

        TitoloValidaModifica titoloValida = new TitoloValidaModifica(modificaType);
        TimestampHash timeHash = new TimestampHash();
        Tb_titolo titolo = titoloValida.validaPerModifica(timeHash, getCdUtente(), modificato, modVariante, _cattura);
        TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
        timbroCondivisione.modificaTimbroCondivisione(titolo, user_object.getBiblioteca() + user_object.getUserId(),_condiviso);
        timeHash.putTimestamp("Tb_titolo", titolo.getBID(), new SbnDatavar( titolo.getTS_VAR()));
        AllineamentoTitolo flagAllineamento = new AllineamentoTitolo(titolo);
        elencoDiagnostico = titoloValida.getElencoDiagnostico();
        if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
            settaIdLista();
            object_response = formattaLista(1);
            return;
        }

        //validaLegami(titolo);
        if (modificato || modVariante) {
            String cd_natura = titolo.getCD_NATURA();
            String tp_materiale = titolo.getTP_MATERIALE();
            if (modVariante && !tp_materiale.equals("E")) {
                throw new EccezioneSbnDiagnostico(3334, "Modifica variante solo per antico");
            }

            // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
			boolean modificaspecificita = false;
			if ((titoloValida.getModificaBaseSpecificita() &  titoloValida.MODIFICA_SPECIFICITA) == titoloValida.MODIFICA_SPECIFICITA)
				modificaspecificita = true;
            // Fine almaviva2 03.08.2010 - Modifiche riportate dal software di Indice

			// almaviva2 16.03.2015 - Adeguamento a Software Indice
			//almaviva4 03/08/2010
            if (modificaspecificita == false && titoloValida.getCdEccezioneTitoloBase() == 3010){
				throw new EccezioneAbilitazioni(3010, "Livello di autorità sulla base dati è superiore");
            }
            //almaviva4 fine 03/08/2010



            // titolo = eseguiModificaTitolo(titolo, modificaType, flagAllineamento);
            titolo = eseguiModificaTitolo(titolo, modificaType, flagAllineamento, modificaspecificita);

            if (!titolo.getCD_NATURA().equals(cd_natura)) {
                if (!validator.verificaAttivitaID(getCdUtente(), CodiciAttivita.getIstance().MODIFICA_NATURA_DOCUMENTO_1021))
                    throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
                if (!(cd_natura.equals("W") && titolo.getCD_NATURA().equals("M"))
                    || !(cd_natura.equals("S") && titolo.getCD_NATURA().equals("C"))
                    || !(cd_natura.equals("C") && titolo.getCD_NATURA().equals("S")))
                    throw new EccezioneSbnDiagnostico(3208, "Cambio natura non consentito");
                if (cd_natura.equals("A") && titolo.getCD_NATURA().equals("B") && tp_materiale.equals("U"))
                    throw new EccezioneSbnDiagnostico(3208, "Cambio natura non consentito");
                if (!titolo.getTP_MATERIALE().equals(tp_materiale)) {
                    if (tp_materiale.equals("E") || titolo.getTP_MATERIALE().equals("M") || titolo.getTP_MATERIALE().equals("E")) {
                        throw new EccezioneSbnDiagnostico(3305,"Modifica del tipo materiale non consentita");
                    }
                    // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
//                    if (!validator.verificaAttivitaID(getCdUtente(), CodiciAttivita.getIstance().MODIFICA_TIPO_MATERIALE_DOCUMENTO_1020)) {
//                        throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
//                    }
                    if ( tp_materiale.equals("M")) {
                        //OK Anche non autorizzato
                      } else {
                        if (!validator.verificaAttivitaID(getCdUtente(), CodiciAttivita.getIstance().MODIFICA_TIPO_MATERIALE_DOCUMENTO_1020)) {
                        	throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
                        }
                      }
                    // Fine almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
                }
            }
            flagAllineamento.setTb_titolo(true);
        }

        TitoloModifica titoloModifica = new TitoloModifica(modificaType);
        // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
        // modificato il tipo di if
//        if (modificato) {
		if (modificato && (titoloValida.getModificaBaseSpecificita() &  titoloValida.MODIFICA_BASE) == titoloValida.MODIFICA_BASE
				&& titoloValida.getCdEccezioneTitoloBase() == titoloValida.ECCEZIONE_NESSUNA) {

            titoloModifica.eseguiUpdate(titolo);
            Link_multim lm = new Link_multim();
            if (lm.cercaLinkMultim(titolo.getBID()).size()>0) {
                lm.cancellaPerKy(titolo.getBID(),titolo.getUTE_VAR());
            }
            titoloModifica.crea856(titolo);

            if (titoloValida.isChiaviModificate()) {
                String cd_natura = titolo.getCD_NATURA();
                if (cd_natura.equals("M") || cd_natura.equals("S"))
                    titoloModifica.aggiornaChiaviLegate(titolo);
            }
            Nota nota = new Nota();

	        // INIZIO 321
	        // Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	        // al link dei documenti su Basi Esterne - Link verso base date digitali
            // Giuno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
        	// ai repertori cartacei - Riferimento a repertorio cartaceo
            C321[] c321 = datiDoc.getT321();
            // START CONTROLLI FORMALI ESISTENZA CAMPI OBBLIGATORI
			for (int i = 0; i < datiDoc.getT321Count(); i++) {
				if (c321[i].getTipoNota() != null) { // VERIFICA DI CORRETTO
					if (c321[i].getTipoNota().getType() != TipoNota321.DATABASE_TYPE &&
						c321[i].getTipoNota().getType() != TipoNota321.REPERTORIO_TYPE) {
						throw new EccezioneSbnDiagnostico(3250); // "Tipo nota non valido");
					}
				} else {
					throw new EccezioneSbnDiagnostico(3250); // "Tipo nota non valido");
				}
				if (c321[i].getTipoNota().getType() == TipoNota321.DATABASE_TYPE) { // CONTRLLI
					if ((c321 != null && (c321.length == 0	|| c321[i] == null	|| c321[i].getA_321() == null || c321[i].getA_321().equals("")))) {
						throw new EccezioneSbnDiagnostico(3398); // <descrizione>Base dati di riferimento obbligatoria</descrizione>
					}
					if ((c321 != null && (c321.length == 0	|| c321[i] == null || c321[i].getU_321() == null || c321[i].getU_321().equals("")))) {
						throw new EccezioneSbnDiagnostico(3400); // <descrizione>URI obbligatoria</descrizione>
					}
				} else {// CONTROLLI FORMALI REPERTORIO
					if ((c321 != null && (c321.length == 0	|| c321[i] == null	|| c321[i].getA_321() == null || c321[i].getA_321().equals("")))) {
						throw new EccezioneSbnDiagnostico(3409); // <descrizione>Autore/Titolo obbligatorio</descrizione>
					}
				}

			} // end for getT321Count()
			// END CONTROLLI FORMALI ESISTENZA CAMPI OBBLIGATORI
			String appoB321 = "";
			String appoC321 = "";
			 // CICLO CONTROLLI FORMATO CAMPI BASEDATI
			for (int i = 0; i < datiDoc.getT321Count(); i++) {
				if (c321[i].getTipoNota().getType() == TipoNota321.DATABASE_TYPE) {
					if (datiDoc.getT321(i).getC_321() != null) {
						appoC321 = datiDoc.getT321(i).getC_321();
					}
					// 28/02/2017 MODIFICATA LOGICA ELABORATIVA DEELA 321 I CAMPI A C U (DATABASE - ID - URI) sono tutti obbligatori
					// IL PROTOCOLLO DEVE RICEVERE OLTRE AL TIPO DATABASE SIA l'ID che la URI COMPLETA compresa di ID
					// LEGGE I LA URL DA TB_REPERTORI E SOSTITUISCE GLI _ID_ CON i DATI DEL CAMPO ID_RECORD, E POI CONFRONTA
					// almaviva MODIFICA LETTURA 321. ORA SI CONFRONTA LA URL CON I DATI DELLA TB_REPERTORI PER VERIFICARE LA GIUSTA STRUTTURA
					if ((c321 != null && ((c321[i].getU_321() != null) && (c321[i].getU_321() != ""))
							&& ((c321[i].getC_321() != null) && (c321[i].getC_321() != ""))
							&& ((c321[i].getA_321() != null) && (c321[i].getA_321() != "")))) {
						// LEGGO lA TB_CODICI PER VERIFICARE il codice database e con la sua descrizione cerco nei repertori la url da compilare
						String URL_Da_Repertorio = null;
						String Sigla_repertorio = Decodificatore.getDs_tabella("LINK", datiDoc.getT321(i).getA_321());
						String DB = Decodificatore.getCd_unimarc("LINK", datiDoc.getT321(i).getA_321());
						if (Sigla_repertorio != null) {
							String codiceRepertorio = datiDoc.getT321(i).getA_321(); // 2017/10/20 Arge/almaviva4
							Tb_repertorio tb_repertorio;
							Repertorio repertorio = new Repertorio();
							tb_repertorio = repertorio.cercaRepertorioPerCdSigTipoRepertorioLink(codiceRepertorio); // Sigla_repertorio
							if (tb_repertorio != null) {
								URL_Da_Repertorio = tb_repertorio.getDS_REPERTORIO();
							} else {
								throw new EccezioneSbnDiagnostico(3403);
								// <descrizione>Codice Basedati non valida.</descrizione>
							}
						} else {
							throw new EccezioneSbnDiagnostico(3403);
							// <descrizione>Codice Basedati non valida.</descrizione>
						}

						String ID_record = c321[i].getC_321();
						String URL_Client = c321[i].getU_321();
						// QUI DETERMINO OGGETTO (TAG) DA SODTITUIRE NEL MODELLO LETTO DA TB REPERTORI CONTO IL NUMERO DI _ID_ NEL MODELLO -
						// SE PRESENTE SOLO 1 IL TAG="_ID_" SE PRESENTE 2 VOLTE SPLITTO IL MODELLO E PRENDO il primo intermezzo
						// SE PRESENTE 3 VOLTE splitto IL MODELLO E PRENDO IL PRIMO E IL SECONDO INTERMEZZO
						String URL_Composta = "";
						// ESTC e ISTC controllo sul campo identificativo:
						// Si controlla che il campo non contiene spazi in caso affermativo viene emesso un messaggio di ERRORE.
						// almaviva5_20211214 aggiunto db corago (g)
						if ((DB.equalsIgnoreCase("c")) || (DB.equalsIgnoreCase("d")) || (DB.equalsIgnoreCase("g"))) {
							String tag = "_ID_";
							int countSpace_C_D = StringUtils.countMatches(ID_record, " ");
							if (countSpace_C_D > 0) {
								throw new EccezioneSbnDiagnostico(3405,	" - BaseDati: " + Sigla_repertorio);
								// <descrizione>L'identificativo record non deve contenere spazio
							} else {
								URL_Composta = URL_Da_Repertorio.replace(tag, ID_record);
								if ((URL_Client.compareTo(URL_Composta) == 0)) {
									System.out.println("LINK ESTERNO COMPILATO CORRETTAMENTE" + URL_Composta);
								} else {
									throw new EccezioneSbnDiagnostico(3402,	" " + URL_Composta	+ " - Basedati:" + Sigla_repertorio);
									// URI: La Uri inserita non è corretta
								}
							}
						}

						// EDIT16 controllo sul campo identificativo:
						// Si controlla nel campo se esiste il prefisso CNCE
						// in caso affermativo si elimina.
						// Si controlla se esiste il prefisso CNCE seguito
						// da spazio in caso affermativo si elimina.
						// Si controlla che il campo è numerico.
						// In tutti gli altri casi viene emesso un messaggio
						// di ERRORE.
						if (DB.equalsIgnoreCase("b")) {
							// PRIMO PASSO CONFRONTO STRINGA INVIATA CON STRINGA COMPOSTA
							String tag = "_ID_";
							URL_Composta = URL_Da_Repertorio.replace(tag,ID_record);
							if ((URL_Client.compareTo(URL_Composta) != 0)) {
								throw new EccezioneSbnDiagnostico(3402, " "	+ URL_Composta + " - Basedati:"	+ Sigla_repertorio);
								// URI: La Uri inserita non è corretta
							}

							String REGEX_EDIT16 = "[0-9]+";
							String REGEX_EDIT16_CNCE = "^(?i)CNCE[0-9]+";
							String REGEX_EDIT16_CNCE_SPAZIO = "^(?i)CNCE [0-9]+";
							if ((!ID_record.matches(REGEX_EDIT16))
									&& (!ID_record.matches(REGEX_EDIT16_CNCE))
									&& (!ID_record.matches(REGEX_EDIT16_CNCE_SPAZIO))) {
								throw new EccezioneSbnDiagnostico(3406,	" - BaseDati: " + Sigla_repertorio);
								// L''identificatico record deve contenere solo caratteri numerici
							} else {
								ID_record = ID_record.replaceAll("(?i)CNCE", "");
								ID_record = ID_record.replace(" ", "");
								URL_Composta = URL_Da_Repertorio.replace(tag, ID_record);
								System.out.println("LINK ESTERNO COMPILATO CORRETTAMENTE"+ URL_Composta);
								// IN CASO DI INSERIMENTO CNCE o CNCE SPAZIO, NEL DATABASE INVIAMO SEMPRE LA FORMA CORRETTA
								datiDoc.getT321(i).setC_321(ID_record);
								datiDoc.getT321(i).setU_321(URL_Composta);
							}
						}
						// VD16 controllo sul campo identificativo:
						// Controllo della presenza obbligatoria dei
						// carattere + o spazio.
						// Controllo della struttura "AlfaNumero Alfanumero"
						// o " AlfaNumero + AlfaNumero ".
						// In tutti gli altri casi viene emesso un messaggio
						// di ERRORE.
						if (DB.equalsIgnoreCase("e")) {
							String tag = "_ID_+_ID_";

							String REGEX_EDIT16_PIU = "^[a-zA-Z0-9]+\\+[a-zA-Z0-9]+$";
							String REGEX_EDIT16_SPAZIO = "^[a-zA-Z0-9]+ [a-zA-Z0-9]+$";
							String tipo = "";
							if (ID_record.matches(REGEX_EDIT16_PIU)) {
								tipo = "piu";
							} else if (ID_record.matches(REGEX_EDIT16_SPAZIO)) {
								tipo = "spazio";
							} else {
								throw new EccezioneSbnDiagnostico(3407,	" - BaseDati: " + Sigla_repertorio);
								// Identificativo record non corretto formato valido [STRINGA+STRINGA] o [STRINGA STRINGA]
							}
							if (tipo.equalsIgnoreCase("piu")) {
								if (!ID_record.matches(REGEX_EDIT16_PIU)) {
									throw new EccezioneSbnDiagnostico(3407,	" - BaseDati: "	+ Sigla_repertorio);
									// Identificativo record non corretto formato valido [STRINGA+STRINGA] o [STRINGA STRINGA]
								} else {
									URL_Composta = URL_Da_Repertorio.replace(tag, ID_record);
									if ((URL_Client.compareTo(URL_Composta) == 0)) {
										System.out.println("LINK ESTERNO COMPILATO CORRETTAMENTE" + URL_Composta);
									} else {
										throw new EccezioneSbnDiagnostico(3402, " " + URL_Composta + " - Basedati:"	+ Sigla_repertorio);
										// URI: La Uri inserita non è corretta
									}
								}
							}

							if (tipo.equalsIgnoreCase("spazio")) {
								if (!ID_record.matches(REGEX_EDIT16_SPAZIO)) {
									throw new EccezioneSbnDiagnostico(3407,	" - BaseDati: "	+ Sigla_repertorio); // Identificativo
									// Identificativo record non corretto formato valido [STRINGA+STRINGA] o [STRINGA STRINGA]
								} else {
									URL_Composta = URL_Da_Repertorio.replace(tag, ID_record);
									if ((URL_Client.compareTo(URL_Composta) == 0)) {
										System.out.println("LINK ESTERNO COMPILATO CORRETTAMENTE"+ URL_Composta);
										// IN CASO DI INSERIMENTO DELLO SPAZIO NEL DATABASE INVIAMO SEMPRE LA FORMA CORRETTA
										String ID_record_Corretto = ID_record.replace(" ", "+");
										String URL_RiComposta = URL_Da_Repertorio.replace(tag,ID_record_Corretto);
										datiDoc.getT321(i).setC_321(ID_record_Corretto);
										datiDoc.getT321(i).setU_321(URL_RiComposta);
									} else {
										throw new EccezioneSbnDiagnostico(3402, " " + URL_Composta+ " - Basedati:"	+ Sigla_repertorio);
										// URI: la Uri inserita non è corretta
									}
								}
							}
						}
						// VD17 controllo sul campo identificativo:
						// Controllo della presenza obbligatoria dei
						// caratteri : o spazio,
						// Controllo della struttura "AlfaNumero:Alfanumero"
						// o " AlfaNumero  AlfaNumero ".
						// In tutti gli altri casi viene emesso un messaggio
						// di ERRORE.

						if (DB.equalsIgnoreCase("f")) {
							String tag = "_ID_:_ID_";
							String REGEX_EDIT17_DUE_PUNTI = "^[a-zA-Z0-9]+:[a-zA-Z0-9]+$";
							String REGEX_EDIT17_SPAZIO = "^[a-zA-Z0-9]+ [a-zA-Z0-9]+$";
							String tipo = "";
							if (ID_record.matches(REGEX_EDIT17_DUE_PUNTI)) {
								tipo = "duepunti";
							} else if (ID_record.matches(REGEX_EDIT17_SPAZIO)) {
								tipo = "spazio";
							} else {
								throw new EccezioneSbnDiagnostico(3408,	" - BaseDati: " + Sigla_repertorio); // Identificativo
								// Identificativo record non corretto formato valido [STRINGA:STRINGA] o [STRINGA STRINGA]
							}
							if (tipo.equalsIgnoreCase("duepunti")) {
								if (!ID_record.matches(REGEX_EDIT17_DUE_PUNTI)) {
									throw new EccezioneSbnDiagnostico(3408,	" - BaseDati: "	+ Sigla_repertorio); // Identificativo
									// Identificativo record non corretto formato valido [STRINGA:STRINGA] o [STRINGA STRINGA]
								} else {
									URL_Composta = URL_Da_Repertorio.replace(tag, ID_record);
									if ((URL_Client.compareTo(URL_Composta) == 0)) {
										System.out.println("LINK ESTERNO COMPILATO CORRETTAMENTE" + URL_Composta);
									} else {
										throw new EccezioneSbnDiagnostico(3402, " " + URL_Composta	+ " - Basedati:" + Sigla_repertorio);
										// URI: la Uri inserita non è corretta
									}
								}
							}
							if (tipo.equalsIgnoreCase("spazio")) {
								if (!ID_record.matches(REGEX_EDIT17_SPAZIO)) {
									throw new EccezioneSbnDiagnostico(3408,	" - BaseDati: "	+ Sigla_repertorio); // Identificativo
									// Identificativo record non corretto formato valido [STRINGA:STRINGA] o [STRINGA STRINGA]
								} else {
									URL_Composta = URL_Da_Repertorio.replace(tag, ID_record);
									if ((URL_Client.compareTo(URL_Composta) == 0)) {
										System.out.println("LINK ESTERNO COMPILATO CORRETTAMENTE" + URL_Composta);
										// IN CASO DI INSERIMENTO DELLO SPAZIO NEL DATABASE INVIAMO SEMPRE LA FORMA CORRETTA
										String ID_record_Corretto = ID_record.replace(" ", ":");
										datiDoc.getT321(i).setC_321(ID_record_Corretto);
										String URL_RiComposta = URL_Da_Repertorio.replace(tag,ID_record_Corretto);
										datiDoc.getT321(i).setU_321(URL_RiComposta);
									} else {
										throw new EccezioneSbnDiagnostico(3402, " " + URL_Composta	+ " - Basedati:" + Sigla_repertorio);
										// URI: la Uri inserita non è corretta
									}
								}
							}
						}
					} else {
						throw new EccezioneSbnDiagnostico(3401); // "Basedati ID e URI obbligatoria"

					} // end controllo url
				} // END IF LOOP LINK ESTERNI

				// CONTROLLO campi REPERTORI
				if (c321[i].getTipoNota().getType() == TipoNota321.REPERTORIO_TYPE) {
					if ((c321[i].getA_321() != null) && (c321[i].getA_321() != "")) {
					} else {
						throw new EccezioneSbnDiagnostico(3409, "      ERRORE IN COMPILAZIONE LINK REPERTORI: "); // Autore/titolo obbligatorio
					}
				}
			} // end for getT321Count()

        	// prima tute le note vengono cancellate logicamente per poi riaccendere solo quelle da utilizzare
            nota.cancella(titolo.getBID(), getCdUtente() );
            flagAllineamento.setTb_nota(true);

            for (int i = 0; i < datiDoc.getT3XXCount(); i++) {
                SbnTipoNota tipoNota = datiDoc.getT3XX(i).getTipoNota();
                if (tipoNota != null && !SbnTipoNota.valueOf("300").equals(tipoNota)) {
                    nota.inserisci(
                        titolo.getBID(),
                        Decodificatore.getCd_tabella("Tb_nota", "tp_nota", tipoNota.toString()),
                        datiDoc.getT3XX(i).getA_3XX(),
                        getCdUtente());
                }
            }

            // INIZIO 321
            // Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
            // al link dei documenti su Basi Esterne - Link verso base date digitali
          // GESTIONE COME IN INDICE
          //tipo 321 'database'
          //$a base dati riferimento - obb.
          //$c identificativo record
          //$u uri - obb

          //tipo 321 'repertorio'
          //$a autore/titolo - obb.
          //$b data
          //$c posizione
			// LOOP INSERIMETO NOTE 321
			for (int i = 0; i < datiDoc.getT321Count(); i++) {

				// if ( datiDoc.getT321(i).getB_321() != null) {
				// appoB321 = datiDoc.getT321(i).getB_321();
				// }
				if (datiDoc.getT321(i).getC_321() != null) {
					appoC321 = datiDoc.getT321(i).getC_321();
				}

				// nota.inserisci(
				// titolo.getBid(),
				// "321",
				// "I" +
				// "##" + "DB" +
				// "##" +
				// Decodificatore.getDs_tabella("LINK",
				// datiDoc.getT321(i).getA_321()) +
				// "####" + appoC321 +
				// "##" + datiDoc.getT321(i).getU_321(),
				// getCdUtente());

				// copiato da sviluppo
				if (c321[i].getTipoNota().getType() == TipoNota321.DATABASE_TYPE) {
					nota.inserisci(
							titolo.getBID(),
							"321",
							"I##"
							+ "DB##"
							+ Decodificatore.getDs_tabella("LINK",datiDoc.getT321(i).getA_321()) + "##"
							+ // $b vuota non presente
							"##" + appoC321 + "##"
							+ datiDoc.getT321(i).getU_321(),
							getCdUtente());
							//
				}
				if (c321[i].getTipoNota().getType() == TipoNota321.REPERTORIO_TYPE) {
					nota.inserisci(
							titolo.getBID(),
							"321",
							"I##"
							+ "REP##"
							+ datiDoc.getT321(i).getA_321() + "##"
							+ datiDoc.getT321(i).getB_321() + "##"
							+ datiDoc.getT321(i).getC_321() + "##",
							// $u vuota non presente
							getCdUtente());
				}
			} // end for getT321Count()

            // FINE 321


        } else if (modVariante) {
            titoloModifica.eseguiUpdate(titolo);
        } else {
            titoloModifica.updateVersione(titolo.getBID(), getCdUtente());
        }
        titoloModifica.modificaLegami(
            modificaType.getDocumento().getLegamiDocumento(),
            timeHash,
            titolo,
            flagAllineamento, user_object);
        new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
        titoloModificato = titoloValida.estraiTitoloPerID(titolo.getBID());

        // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
		String msgSpecificita = "", msgTitoloBase = "";
		if (((titoloValida.getModificaBaseSpecificita() &  titoloValida.MODIFICA_SPECIFICITA) == titoloValida.MODIFICA_SPECIFICITA) ||
			(titoloValida.getCdEccezioneSpecificita() != titoloValida.ECCEZIONE_NESSUNA))
		{
		  if (titoloValida.getCdEccezioneSpecificita() != titoloValida.ECCEZIONE_NESSUNA)
			  msgSpecificita = MSG_SPECIFICITA_NON_MODIFICATA + titoloValida.getCdEccezioneSpecificita() + ", "+ titoloValida.getDescEccezioneSpecificita();
		  else
			  msgSpecificita = MSG_SPECIFICITA_MODIFICATA_CORRETTAMENTE;
		}

		if (((titoloValida.getModificaBaseSpecificita() &  titoloValida.MODIFICA_LEGAMI_SOGGETTO) == titoloValida.MODIFICA_LEGAMI_SOGGETTO) ||
	  	((titoloValida.getModificaBaseSpecificita() &  titoloValida.MODIFICA_LEGAMI_CLASSE) == titoloValida.MODIFICA_LEGAMI_CLASSE))
	  {
		if (titoloValida.getCdEccezioneSpecificita() != titoloValida.ECCEZIONE_NESSUNA)
			msgSpecificita = MSG_LEGAME_NON_INSERITO + titoloValida.getCdEccezioneSpecificita() + ", "+ titoloValida.getDescEccezioneSpecificita();
		else
			msgSpecificita = MSG_LEGAME_INSERITO_CORRETTAMENTE;
	  }
	  if ( (titoloValida.getCdEccezioneTitoloBase() != titoloValida.ECCEZIONE_NESSUNA) && ((modificato) || (modVariante)) )
		msgTitoloBase = "Titolo base non modificato: " + titoloValida.getCdEccezioneTitoloBase() + ", "+ titoloValida.getDescEccezioneTitoloBase();
		else if ((titoloValida.getModificaBaseSpecificita() &  titoloValida.MODIFICA_BASE) == titoloValida.MODIFICA_BASE)
		{
			msgTitoloBase = MSG_TITOLO_BASE_MODIFICATO_CORRETTAMENTE;
	  	}
      if (!msgTitoloBase.equals(MSG_TITOLO_BASE_MODIFICATO_CORRETTAMENTE)) {
      	if ( (msgSpecificita.equals(MSG_SPECIFICITA_MODIFICATA_CORRETTAMENTE)) || (msgSpecificita.equals(MSG_LEGAME_INSERITO_CORRETTAMENTE)) )
      		// nel caso di protocollo di Polo questo metodo non ha significato (sui lascia per ricordare l'interv. in Indice)
      		// new TitoloGestisceAllineamento().aggiornaFlagAllineamentoPoloOperante(flagAllineamento, getCdUtente());
      		new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
		else
			new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
      }
		else
			if ( (msgSpecificita.equals(MSG_SPECIFICITA_NON_MODIFICATA)) || (msgSpecificita.equals(MSG_LEGAME_NON_INSERITO)) )
	      		// nel caso di protocollo di Polo questo metodo non ha significato (sui lascia per ricordare l'interv. in Indice)
				// new TitoloGestisceAllineamento().aggiornaFlagAllineamentoPoloOperante(flagAllineamento, getCdUtente());
				new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
			else
				new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());

//      object_response = formattaOutput();
      object_response = formattaOutput(msgSpecificita, msgTitoloBase);

      // Fine almaviva2 03.08.2010 - Modifiche riportate dal software di Indice


    }

    /**
     * Esegue la modifica di un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private Tb_titolo eseguiModificaTitolo(
        Tb_titolo tb_titolo,
        ModificaType modifica,
        AllineamentoTitolo flagAll,
        // Arge
        boolean modificaSpecificita)

        throws IllegalArgumentException, InvocationTargetException, Exception {
        String cd_livello = datiDoc.getLivelloAutDoc().toString();
        TitoloValidaModifica validaTitolo = new TitoloValidaModifica(modifica);
        String cd_natura = validaTitolo.estraiNatura(datiDoc);
        TitoloModifica titoloModifica = new TitoloModifica(modifica);
        if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
            tb_titolo.setUTE_FORZA_VAR(getCdUtente());
        tb_titolo.setUTE_VAR(getCdUtente());
        if (!modVariante) {
            //il cd livello potrebbe forse essere spostato.
            tb_titolo.setCD_LIVELLO(cd_livello);
            if (!tb_titolo.getCD_NATURA().equals(cd_natura))
                flagAll.setNatura(true);
            tb_titolo.setCD_NATURA(cd_natura);
            String nota = "";
            for (int i = 0; i < datiDoc.getT3XXCount(); i++)
                if (SbnTipoNota.valueOf("300").getType() != datiDoc.getT3XX(i).getTipoNota().getType())
                    nota += datiDoc.getT3XX(i).getA_3XX() + ";";

			//GESTIONE DELLE NOTE AGGIUNTIVE 3204 di Indice
            // Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
            // aggiunto sopra anche il ";" ci concatenazione
			// CODICE ORIGINALE DA ripristinare in caso di deploy in esercizio
			// ( vale solo se non viene deployata l'ID con le nuove modifiche per la gestione delle note)
			// In caso di deploy di ID in esercizio togliere questo commento
//            if (!nota.equals(""))
//                tb_titolo.setNOTA_CAT_TIT(nota);
            if (!nota.equals("")){
            	if (nota.length() > 319){
            		nota = nota.substring(0,319);
					tb_titolo.setNOTA_CAT_TIT(nota);
            	}
            	else{
					tb_titolo.setNOTA_CAT_TIT(nota);
            	}
            }
// Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
			// END GESTIONE DELLE NOTE AGGIUNTIVE 3204

            String materiale = tb_titolo.getTP_MATERIALE();
            String tipoRecord = tb_titolo.getTP_RECORD_UNI();
            tb_titolo = titoloModifica.creaDocumento(tb_titolo);

            // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
            if (root_object.getSchemaVersion().compareTo(new BigDecimal(2.00)) > -1 &&
            		(cd_natura.equals("M") || cd_natura.equals("S")
                    || cd_natura.equals("W") || cd_natura.equals("N"))
                )
            	titoloModifica.modificaComuni(tb_titolo, datiDoc, flagAll);
            // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi


          //almaviva4 25/03/2015
            if (root_object.getSchemaVersion().compareTo(new BigDecimal(2.00)) == -1 &&
            		(cd_natura.equals("M") || cd_natura.equals("S")
                	|| cd_natura.equals("W") || cd_natura.equals("N")) &&
                	(!tb_titolo.getTP_RECORD_UNI().equals(tipoRecord))
            	)
            	titoloModifica.modificaComuniDef(tipoRecord, tb_titolo, datiDoc, flagAll);

	            // Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
	            // viene esteso anche al Materiale Moderno e Antico
                titoloModifica.modificaPersRapp(tb_titolo, datiDoc, flagAll);


            if (!tb_titolo.getTP_MATERIALE().equals(materiale))
                flagAll.setMateriale(true);
            titoloModifica.modificaImpronta(tb_titolo, datiDoc, flagAll);


            if (modificaSpecificita == true)
            {
	            titoloModifica.modificaMusica(tb_titolo, datiDoc, flagAll);
	            titoloModifica.modificaGrafico(tb_titolo, datiDoc, flagAll);
	            titoloModifica.modificaCartografico(tb_titolo, datiDoc, flagAll);

	            // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	            titoloModifica.modificaAudiovisivo(tb_titolo, datiDoc, flagAll);
	            titoloModifica.modificaElettronico(tb_titolo, datiDoc, flagAll);
        	}


            titoloModifica.modificaNumeroStandard(datiDoc.getNumSTD(), tb_titolo, flagAll, root_object.getSchemaVersion());
        } else {
            Isbd isbd = new Isbd();
            isbd.ricostruisciISBD(tb_titolo);
            new CostruttoreIsbd().definisciISBD(tb_titolo, isbd.getC200(), isbd.getC205(), isbd.getC206(), isbd.getC207(), isbd.getC208(), isbd.getC210(), isbd.getC215(), null, datiDoc.getT3XX(), true, tb_titolo.getCD_LINGUA_1());
        }
        return tb_titolo;
    }
    /**
     * Metodo utilizzato per preparare gli output alle fasi successive
     */
    public void proseguiTransazione() {
    }

    /** Prepara la lista per il diagnostico, qualora esistano altri autori simili
     * E' copiata pari pari da Creatitolo: sarebbe il caso di farne una sola.
     */
    protected SBNMarc formattaLista(int numeroBlocco)
        throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        int last = numeroBlocco * maxRighe;
        if (last > elencoDiagnostico.size())
            last = elencoDiagnostico.size();
        List lista = elencoDiagnostico.subList((numeroBlocco - 1) * maxRighe, last);
        return FormatoTitolo.formattaLista(
                lista,
                user_object,
                SbnTipoOutput.valueOf("001"),
                null,
                id_lista,
                (numeroBlocco - 1) * maxRighe,
                maxRighe,
                elencoDiagnostico.size(),
                root_object.getSchemaVersion(),
                "3004",
                "Trovati titoli simili",false);
    }

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023;
    }

    // Metodo almaviva2 02.11.2009 per verificare i null nei campi
    public String trimOrEmpty(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return "";
		return tmp;
	}


    public String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&"};
        String outputString="";
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
                inputString = outputString;
            }
        }
        if(outputString.equals("")){
        	return inputString;
        }
        else{
        	return outputString;
        }
    }
}
