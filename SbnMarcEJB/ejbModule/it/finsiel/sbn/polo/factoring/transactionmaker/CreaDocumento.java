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


import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Nota;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloCreaDocumento;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloValidaCreazione;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.C321;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
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
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoNota321;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;



/**
 * Classe CreaTitolo.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 7-gen-03
 */
public class CreaDocumento extends CreaFactoring {

    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CreaDocumento");

    // Attributi da XML-input
    private DocumentoType documento;
    private DatiDocType datiDoc;

    private SbnLivello livelloAut;

    //entità collegata: ArrivoLegame
    private LegamiType[] arrivoLegame = null;

    private SbnSimile tipoControllo = null;
    //Utente che esegue la richiesta
    private SbnUserType sbnUser = null;

    private String id_lista = null;

    private final static int maxRighe =
        Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));
    //Autore creato dal factoring
    private Tb_titolo titoloCreato;

    private LocalizzaType localizzaType = null;
    private String  _condiviso;

    //vettore utilizzato per i risultati delle ricerche
    /**
     * Metodo costruttore classe di factoring
     * <p>
     * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
     * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
     * </p>
     * @param  SBNMarc oggetto radice XML in input
     * @return istanza oggetto (default)
     */
    public CreaDocumento(SBNMarc input_root_object) {
        super(input_root_object);
        //do per scontato che ci siano tutti. Le verifiche sono fatte da chi
        //chiama il factoring
        documento = factoring_object.getCreaTypeChoice().getDocumento();
        datiDoc = documento.getDocumentoTypeChoice().getDatiDocumento();

        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
        datiDoc.setVersioneSchema(input_root_object.getSchemaVersion().toString());

        if (datiDoc.getTipoMateriale() != null) {
            setTp_materiale(datiDoc.getTipoMateriale().toString());
        }
        arrivoLegame = documento.getLegamiDocumento();
        tipoControllo = factoring_object.getTipoControllo();
        if (tipoControllo==null) {
            tipoControllo = SbnSimile.SIMILE;
            factoring_object.setTipoControllo(tipoControllo);
        }
        sbnUser = input_root_object.getSbnUser();
        localizzaType = input_root_object.getSbnMessage().getSbnRequest().getCrea().getLocalizza();

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
     * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di ricerca
     * opportuno.
     * gestisce il tipo di risposta richiesto (lista o esame) e produce il
     * file xml di output richiesto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IndexOutOfBoundsException
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
    protected void executeCrea()
        throws NumberFormatException, IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception {
        //tipoForma=null;
        //controllo la validità del luogo
        TitoloValidaCreazione validaTitolo = new TitoloValidaCreazione(factoring_object, scheduled);
        boolean ignoraID = datiDoc.getT001().equals(SBNMARC_DEFAULT_ID);
        TimestampHash timeHash = new TimestampHash();
        if (validaTitolo.validaPerCrea(getCdUtente(), ignoraID, timeHash, _cattura)) {
            if (SbnSimile.SIMILEIMPORT.getType() == tipoControllo.getType())
                throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
            String user = getCdUtente();
            Titolo titoloDB = new Titolo();
            //controllo se esistono informazioni ArrivoLegami
            String cd_livello = datiDoc.getLivelloAutDoc().toString();
            String cd_natura = validaTitolo.estraiNatura(datiDoc);
            TitoloCreaDocumento titoloCrea = new TitoloCreaDocumento(factoring_object);
            Tb_titolo tb_titolo = new Tb_titolo();
            if (SbnSimile.CONFERMA.getType() == tipoControllo.getType() && !scheduled) {
                tb_titolo.setUTE_FORZA_INS(user);
                tb_titolo.setUTE_FORZA_VAR(user);
            }
            tb_titolo.setUTE_INS(user);
            tb_titolo.setUTE_VAR(user);

            //il cd livello potrebbe forse essere spostato.
            tb_titolo.setCD_LIVELLO(cd_livello);

            tb_titolo.setCD_NATURA(cd_natura);
            tb_titolo.setFL_SPECIALE(" ");
            tb_titolo.setFL_CANC(" ");
            //Questo lo metto perchè il bid serve per la lingua.ovviamente può essere000
            tb_titolo.setBID(datiDoc.getT001());
            // Timbro Condivisione
            tb_titolo.setFL_CONDIVISO(_condiviso);
            tb_titolo.setTS_CONDIVISO(TableDao.now());
            tb_titolo.setUTE_CONDIVISO(user);

            tb_titolo = titoloCrea.creaDocumento(tb_titolo);
            if (ignoraID) {
                //Devo settare il vid automaticamente
                Progressivi progress = new Progressivi();
                tb_titolo.setBID(progress.getNextIdTitolo(tb_titolo.getTP_MATERIALE(),tb_titolo.getTP_RECORD_UNI()));
            } else
                tb_titolo.setBID(datiDoc.getT001());
            titoloDB.inserisci(tb_titolo);
            titoloCrea.crea856(tb_titolo);

            if (datiDoc.getNumSTDCount() > 0) {
                titoloCrea.creaNumeroStandard(datiDoc.getNumSTD(), tb_titolo);
            }

            // Luglio 2016 almaviva2
            Nota nota = new Nota();
            for (int i = 0; i < datiDoc.getT3XXCount(); i++) {
                SbnTipoNota tipoNota = datiDoc.getT3XX(i).getTipoNota();
                if (!tipoNota.equals(SbnTipoNota.valueOf("300"))) {
                    String tipo = Decodificatore.getCd_tabella("Tb_nota", "tp_nota", tipoNota.toString());
                    if (tipo != null)
                    nota.inserisci(
                        tb_titolo.getBID(),
                        tipo,
                        datiDoc.getT3XX(i).getA_3XX(),
                        getCdUtente());
                    else
                        throw new EccezioneSbnDiagnostico(3250); //, "Tipo nota non valido");
                }
            }



         // Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
         // al link dei documenti su Basi Esterne - Link verso base date digitali
         // Giuno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
         // ai repertori cartacei - Riferimento a repertorio cartaceo

            C321[] c321 = datiDoc.getT321();
            String appoC321 = "";

            for (int i = 0; i < datiDoc.getT321Count(); i++) { //si inseriscono le note 321 di tipo 'I'
               	if (c321[i].getTipoNota() != null) {
   	                if (c321[i].getTipoNota().getType() != TipoNota321.DATABASE_TYPE
   	                		&& c321[i].getTipoNota().getType() != TipoNota321.REPERTORIO_TYPE) {
                           throw new EccezioneSbnDiagnostico(3250); //"Tipo nota non valido");
   	                }
               	} else {
                       throw new EccezioneSbnDiagnostico(3250); //"Tipo nota non valido");
               	}

           	if (c321[i].getTipoNota().getType() == TipoNota321.DATABASE_TYPE) {
		    		if( ( c321 != null &&
			    			( c321.length == 0 || c321[i] == null || c321[i].getA_321() == null || c321[i].getA_321().equals(""))
		    			))
           			   throw new EccezioneSbnDiagnostico(3398); //"Base dati di riferimento obbligatoria"
		    		if( ( c321 != null &&
			    			( c321.length == 0 || c321[i] == null || c321[i].getU_321() == null|| c321[i].getU_321().equals("") )
		    			))
           			   throw new EccezioneSbnDiagnostico(3400);    //"URI obbligatoria"
                if  ( datiDoc.getT321(i).getC_321() != null) {
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
					String TAG = null;
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
								// URI: La Uri inserita non Ã¨ corretta
							}
						}
					}

					// EDIT16 controllo sul campo identificativo:
					// Si controlla nel campo se esiste il prefisso CNCE
					// in caso affermativo si elimina.
					// Si controlla se esiste il prefisso CNCE seguito
					// da spazio in caso affermativo si elimina.
					// Si controlla che il campo Ã¨ numerico.
					// In tutti gli altri casi viene emesso un messaggio
					// di ERRORE.
					if (DB.equalsIgnoreCase("b")) {
						// PRIMO PASSO CONFRONTO STRINGA INVIATA CON STRINGA COMPOSTA
						String tag = "_ID_";
						URL_Composta = URL_Da_Repertorio.replace(tag,ID_record);
						if ((URL_Client.compareTo(URL_Composta) != 0)) {
							throw new EccezioneSbnDiagnostico(3402, " "	+ URL_Composta + " - Basedati:"	+ Sigla_repertorio);
							// URI: La Uri inserita non Ã¨ corretta
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
									// URI: La Uri inserita non Ã¨ corretta
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
									// URI: la Uri inserita non Ã¨ corretta
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
									// URI: la Uri inserita non Ã¨ corretta
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
									// URI: la Uri inserita non Ã¨ corretta
								}
							}
						}
					}
				} else {
					throw new EccezioneSbnDiagnostico(3401); // "Basedati ID e URI obbligatoria"

				} // end controllo url
		    		// almaviva MODIFICA LETTURA 321. ORA SI CONFRONTA LA URL CON I DATI DELLA TB_REPERTORI PER VERIFICARE LA GIUSTA STRUTTURA

		    		 nota.inserisci(
		    				 tb_titolo.getBID(),
		           	          "321",
		           	          "I##" +
		           	          "DB##" +
		           	          Decodificatore.getDs_tabella("LINK", datiDoc.getT321(i).getA_321()) +
		                      "##" + //$b vuota non presente
		                      "##" + appoC321 +
		           	          "##" + datiDoc.getT321(i).getU_321(),
		           	          getCdUtente());

		        } //end if datadase_type
           	else
           	{ //if repertorio_type
           		if( ( c321 != null
               			&& ( (c321[i].getB_321() != null) && (c321[i].getB_321() != ""))
               			&& ( (c321[i].getC_321() != null) && (c321[i].getC_321() != ""))
               			&& ( (c321[i].getA_321() != null) && (c321[i].getA_321() != "")
               		))){
           			nota.inserisci(
              				 tb_titolo.getBID(),
                     	          "321",
                     	          "I##" +
                     	          "REP##" + datiDoc.getT321(i).getA_321() +
                                "##" + datiDoc.getT321(i).getB_321() +
                                "##" + datiDoc.getT321(i).getC_321() +
                     	          "##", //$u vuota non presente
                     	          getCdUtente());
           		} else {
           			throw new EccezioneSbnDiagnostico(3405,"      ERRORE IN COMPILAZIONE LINK REPERTORI: ");    // da sistemare
           		}

           	} //end if datadase_type
          	} // end for getT321Count()


            // test mdf da eliminare ?? titoloCrea.settasbnUser( sbnUser);
            titoloCrea.creaLegami(timeHash, tb_titolo, sbnUser);
            //Chiamo le creazioni aggiuntive, se non serve non vengono create.
            titoloCrea.inserisciImpronta(tb_titolo, datiDoc);
            titoloCrea.creaMusica(tb_titolo, datiDoc);
            titoloCrea.creaGrafico(tb_titolo, datiDoc);
            titoloCrea.creaCartografico(tb_titolo, datiDoc);

            // Inizio almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
            titoloCrea.creaAudiovisivo(tb_titolo, datiDoc);
            titoloCrea.creaDiscosonoro(tb_titolo, datiDoc);
            titoloCrea.creaElettronico(tb_titolo, datiDoc);
            // Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro


            // Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
            // viene esteso anche al Materiale Moderno e Antico
        	titoloCrea.inserisciPersRapp(tb_titolo, datiDoc);




            // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
            if (root_object.getSchemaVersion().compareTo(new BigDecimal(2.00)) > -1)
            	titoloCrea.creaComuni(tb_titolo, datiDoc);



            new TitoloGestisceLocalizza().localizzaTitolo(tb_titolo.getBID(), localizzaType,user_object);

            titoloCreato = titoloDB.estraiTitoloPerID(tb_titolo.getBID());

            object_response = formattaOutput();

        } else {
            //ritorna il messaggio di diagnostica
            elencoDiagnostico = validaTitolo.getElencoDiagnostico();
            settaIdLista();
            if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
                object_response = formattaLista(1);
            } else {
                //Qui non ci dovrebbe mai arrivare: utilizza le eccezioni.
                object_response = null;
            }
        }

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
            sbnUser,
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


    /**
     * Prepara l'xml di risposta
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000"); //Esito positivo
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        DatiDocType datiResp = new DatiDocType();
        datiResp.setT001(titoloCreato.getBID());
        SbnDatavar data = new SbnDatavar(titoloCreato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAutDoc(datiDoc.getLivelloAutDoc());
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

    public String getIdAttivita() {

    	// Inizio Intervento google3: Per cattura non viene chiamata la verifica delle abilitazioni
    	// serve solo il Cerca + Localizza per Gestione
    	if(_cattura){
    		return CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009;
    	} else {
    		return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    	}
    	//  	return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    	// Fine Intervento google3:
    }


    // Metodo almaviva2 29.12.2010 per verificare i null nei campi
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
