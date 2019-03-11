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
package it.iccu.sbn.SbnMarcFactory.factory.bibliografica;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.bibliografica.Repertorio;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineatiType;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ChiediAllineaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ComunicaAllineatiType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.RepertorioType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoModifica;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioCancAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaOutputAllineamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.FusioneMassivaBatchVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO.ScheduleType;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.util.batch.BatchUtil;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.exolab.castor.xml.MarshalException;

/**
 * <p>
 * Title: SbnWEB
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * <p>
 * Funzioni per la creazione e parsing di alberi dom castor relativi alle
 * interrogazioni sugli Autori. Utilizza la classe XMLFactory per scambiare
 * flussi XML con il server sbn, il formato dei flussi XML scambiati rispetta lo
 * schema XSD del protocollo SBN-MARC, tale schema è rappresentato mediante un
 * object model generato con Castor. Le classi che realizzano i frame ed i
 * pannelli dell'interfaccia grafica per l'area autori utilizzano XMLAutori per
 * effettuare interrogazioni e modifiche sui dati degli autori mediante il
 * protocollo SBN-MARcercaAutoreType.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @author Finsiel
 * @version 1.0
 */
public class SbnGestioneAllineamentiIndicePoloDao extends SbnGestioneAllineamentoBaseImpl {

	private FactorySbn polo;
	private int contatoreLegami = 0;
	private int contatoreDati = 0;
	private int contatoreCancellaz = 0;
	private int contatoreScambioForma = 0;
	private int contatoreFusione = 0;
	private int contatoreDatiLegami = 0;
	private int contatoreNatura = 0;
	private int contatoreSenzaTipol = 0;

	private int contatoreLegamiOK = 0;
	private int contatoreDatiOK = 0;
	private int contatoreCancellazOK = 0;
	private int contatoreScambioFormaOK = 0;
	private int contatoreFusioneOK = 0;
	private int contatoreDatiLegamiOK = 0;
	private int contatoreNaturaOK = 0;
	private int contatoreSenzaTipolOK = 0;

	public SbnGestioneAllineamentiIndicePoloDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}


	public AllineaVO richiediListaAllineamenti(
			AllineaVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		TracciatoStampaOutputAllineamentoVO outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<html><head>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</head>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<body>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);


		// Allineamento richiesto con identificativo
		if (ValidazioneDati.isFilled(areaDatiPass.getIdFileAllineamenti())) {
			return allineaBaseLocale(areaDatiPass);
		}

		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
		// del flag di avvenuto allineamento.
		if (ValidazioneDati.isFilled(areaDatiPass.getIdFileLocaleAllineamenti())) {
			return allineaBaseLocale(areaDatiPass);
		}

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<H3>INIZIO FUNZIONE ALLINEAMENTO  "
				+ DateUtil.getDate()
				+ " "
				+ DateUtil.getTime()
				+ "</H3>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		if ((areaDatiPass.getDataAllineaDa() != null && !areaDatiPass.getDataAllineaDa().equals(""))
				&& (areaDatiPass.getDataAllineaA() != null && !areaDatiPass.getDataAllineaA().equals(""))) {
			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<H3>Modifiche effettuate in Indice DAL "
					+ areaDatiPass.getDataAllineaDa()
					+ " AL "
					+ areaDatiPass.getDataAllineaA()
					+ "</H3>");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		}

		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo(null);
		String messaggio = "";

		try {

			SbnMessageType sbnmessage;
			SbnRequestType sbnrequest;
			ChiediAllineaType chiediAllineaType;
			SbnOggetto sbnOggetto;


			//====================================================================================================
			// inizio ciclo di richiesta lista allineamenti/xml allineamenti se inferiori a 50

//			String[] listaOggetti = new String[5];
//			listaOggetti[0] = "";
//			listaOggetti[1] = "AU";
//			listaOggetti[2] = "MA";
//			listaOggetti[3] = "TU" (è compreso nei documenti);
//			listaOggetti[4] = "UM" (è compreso nei documenti);

			String[] listaOggetti = null;
			if (areaDatiPass.getTipoMaterialeDaAllineare().equals("M")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--M";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals("E")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--E";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals("C")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--C";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals("G")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--G";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals("U")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--U ";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
				// Manutenzione Febbraio 2018 - gestione nuovi materiale Elettronico e Audiovisivo ("H", "L")
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals("H")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--H ";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals("L")) {
				listaOggetti = new String[3];
				listaOggetti[0] = "--L ";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else if (areaDatiPass.getTipoMaterialeDaAllineare().equals(" ")) {
				// Attenzione il valore " " vuol dire TUTTI I DOCUMENTI
				listaOggetti = new String[3];
				listaOggetti[0] = "-- ";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
			} else {
				listaOggetti = new String[3];
				listaOggetti[0] = "-- ";
				listaOggetti[1] = "AU";
				listaOggetti[2] = "MA";
//
//				listaOggetti = new String[8];
//				listaOggetti[0] = "--M";
//				listaOggetti[1] = "--E";
//				listaOggetti[2] = "--C";
//				listaOggetti[3] = "--G";
//				listaOggetti[4] = "--U";
//				listaOggetti[5] = "-- ";
//				listaOggetti[6] = "AU";
//				listaOggetti[7] = "MA";

			}

//			String[] listaOggetti = new String[3];
//			listaOggetti[0] = "";
//			listaOggetti[1] = "AU";
//			listaOggetti[2] = "MA";
			String numListaDaAllineare = "";

			// Intervento almaviva2 29.12.2010 BUG MANTIS 3963 -  In tutte le dichiarative di nuova TABE viene eliminata
			// la ALIGN=\"left\" perchè manadva in confusione la visualizazione del report con Internet Explorer e Excel



			for (int i = 0; i < listaOggetti.length; i++) {

				// Inizio Modifica 19.03.2010 - almaviva2 gestione STOP processi batch differiti (per gent. conc. almaviva5)
				try {
					BatchManager.getBatchManagerInstance().checkForInterruption(areaDatiPass.getIdBatch());
				} catch (BatchInterruptedException eBI) {
					eBI.printStackTrace();
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(eBI.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
					return areaDatiPass;
				} catch (Exception e) {
					e.printStackTrace();
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(e.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
					return areaDatiPass;
				}
				// Fine Modifica 19.03.2010 - almaviva2

				contatoreLegami = 0;
				contatoreDati = 0;
				contatoreCancellaz = 0;
				contatoreScambioForma = 0;
				contatoreFusione = 0;
				contatoreDatiLegami = 0;
				contatoreNatura = 0;
				contatoreSenzaTipol = 0;

				contatoreLegamiOK = 0;
				contatoreDatiOK = 0;
				contatoreCancellazOK = 0;
				contatoreScambioFormaOK = 0;
				contatoreFusioneOK = 0;
				contatoreDatiLegamiOK = 0;
				contatoreNaturaOK = 0;
				contatoreSenzaTipolOK = 0;

				sbnmessage = new SbnMessageType();
				sbnrequest = new SbnRequestType();
				chiediAllineaType = new ChiediAllineaType();



				// Inizio modifica almaviva2 27 maggio 2014
				// Viene attivata la richiesta ARCOBALENO -richiesta tipo 4 per avere subito le localizzazioni - per
				// effettuare la localizzazione del reticolo in allineamento, non per la biblioteca operante che potrebbe
				// anche non gestire il bid ma per una delle biblioteche che effettivamente lo gestiscono;
				// al momento dell'allineamento del bid radice verrà inviata la richiesta di localizzazione che l'Indice
				// automaticamente esploderà a tutto il reticolo; in questo modo anche i nuovi elementi di reticolo
				// (autori o collane ad esempio) verranno correttamente localizzati;

				// chiediAllineaType.setTipoOutput(SbnTipoOutput.VALUE_0);
				chiediAllineaType.setTipoOutput(SbnTipoOutput.VALUE_3);
				// Fine modifica almaviva2 ARCOBALENO


				chiediAllineaType.setTipoInfo(SbnTipoLocalizza.GESTIONE);

				String tipoOggetto = "";
				sbnOggetto = new SbnOggetto();
				if (listaOggetti[i].substring(0,2).equals("--")) {
					sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(listaOggetti[i].substring(2,3)));
					if (SbnMateriale.valueOf(listaOggetti[i].substring(2,3)).toString().equals(" ")) {
						tipoOggetto = "DOCUMENTI Tutti i Materiale";
					} else {
						tipoOggetto = "DOCUMENTI Materiale " + SbnMateriale.valueOf(listaOggetti[i].substring(2,3));
					}

				} else {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(listaOggetti[i]));
					if (listaOggetti[i].equals("AU")) {
						tipoOggetto = "AUTORI";
					} else if (listaOggetti[i].equals("MA")) {
						tipoOggetto = "MARCHE";
					} else {
						tipoOggetto = listaOggetti[i].substring(0,2);
					}
				}

				chiediAllineaType.setTipoOggetto(sbnOggetto);
				if (areaDatiPass.getDataAllineaDa() != null) {
					if (!areaDatiPass.getDataAllineaDa().equals("")) {
						SimpleDateFormat simpleDateFormatDa = new SimpleDateFormat("dd/MM/yyyy");
						chiediAllineaType.setDataInizio(new org.exolab.castor.types.Date(simpleDateFormatDa.parse(areaDatiPass.getDataAllineaDa())));

						// La data di fine allineamento deve essere portata avanti di 1 giorni
						String dataAOld = areaDatiPass.getDataAllineaA().substring(6,10)
							+ areaDatiPass.getDataAllineaA().substring(3,5)
							+ areaDatiPass.getDataAllineaA().substring(0,2);

						String dataANew = DateUtil.addDay(dataAOld, 1);
						SimpleDateFormat simpleDateFormatA = new SimpleDateFormat("yyyyMMdd");
						chiediAllineaType.setDataFine(new org.exolab.castor.types.Date(simpleDateFormatA.parse(dataANew)));
					}
				}

				// Intervento almaviva2 29.12.2010 BUG MANTIS 3963 -
				// inserimento/eliminazione  righe bianche fra una tipologia di allineamento
				// ed un altro (esempio fra allineamento BID e VID c'erano almeno 8 righe vuote);
				// (SbnGestioneAllineamentiIndicePoloDao - richiediListaAllineamenti)
				outputAllineamentoVO =
					new TracciatoStampaOutputAllineamentoVO("<br><br><H3>Oggetti da allineare: "
								+ tipoOggetto
								+ "</H3>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);


				String dataInizioRichTrasf = "";
				String dataFineRichTrasf = "";
				dataInizioRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();

				sbnrequest.setChiediAllinea(chiediAllineaType);
				sbnmessage.setSbnRequest(sbnrequest);
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				dataFineRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();


				// Inizio Intervento interno almaviva2 su indicazione di POLO NAP esercizio 30.11.2010
				// se l'Indice non risponde per varie cause il controllo su Esito null si deve bloccare e tornare
				// con le aree correttamente impostate; SbnGestioneAllineamentiIndicePoloDao metodo:richiediListaAllineamenti
//				if (sbnRisposta == null) {
//					areaDatiPass.setCodErr("noServerInd");
//					return areaDatiPass;
//				}

				// Controllo su Risposta alla PRIMA CHIAMATA all'Indice per avere gli allineamenti
				if (sbnRisposta == null
						|| sbnRisposta.getSbnMessage() == null
						|| sbnRisposta.getSbnMessage().getSbnResponse() == null
						|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult() == null
						|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() == null) {

					messaggio = messaggio + "Presenza del valore null nella risposta da Indice probabilmente dovuta a chiusura Indice - PRIMA CHIAMATA";


					// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
					// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
					// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
					// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
					// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
					// ma di seguito a: Errore in richiesta:
					outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
							dataInizioRichTrasf + "</TD><TD>" + dataFineRichTrasf +
							"</TD><TD>Errore in richiesta: Presenza del valore null nella risposta da Indice testo probabilmente dovuta a chiusura Indice - PRIMA CHIAMATA" + "</TD><TD>" + " " + "</TD></TR>");
					areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

					outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
					areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo("Presenza del valore null nella risposta da Indice probabilmente dovuta a chiusura Indice - PRIMA CHIAMATA");
					return areaDatiPass;
				}
				// Fine Intervento interno almaviva2 su indicazione di POLO NAP esercizio 30.11.2010

				outputAllineamentoVO =
					new TracciatoStampaOutputAllineamentoVO("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

				outputAllineamentoVO =
					new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\">" +
							"<TH WIDTH=\"15%\">Invio richiesta a Indice</TH>" +
							"<TH WIDTH=\"15%\">Arrivo risposta da Indice</TH>" +
							"<TH WIDTH=\"15%\">Tipo risposta</TH>" +
							"<TH WIDTH=\"35%\">Totale oggetti</TH>" +
							"</TR>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
					outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
								dataInizioRichTrasf +
								"</TD><TD>" +
								dataFineRichTrasf +
								"</TD><TD>&nbsp;</TD><TD>Non ci sono allineamenti da eseguire</TD></TR>");
					areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
					numListaDaAllineare = "0";
				} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					// Risposta positiva dell'INDICE sulla prima chiamata
					String numListaStringa = "0";
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista() != null) {
						numListaStringa = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
					}
					numListaDaAllineare = numListaStringa;
					boolean continuaCiclo = true;
					int numRigheTot = 0;
					int progressivoBlocco = 1;
					int numBlocchi = 0;
					int maxRighe = 0;
					AllineaVO areaDiRitorno = new AllineaVO();
					numRigheTot = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
					if (numRigheTot == 0) {
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
								dataInizioRichTrasf +
								"</TD><TD>" +
								dataFineRichTrasf +
								"</TD><TD>&nbsp;</TD><TD>Non ci sono allineamenti da eseguire</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
						numListaDaAllineare = "0";
					} else {
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
								dataInizioRichTrasf +
								"</TD><TD>" +
								dataFineRichTrasf +
								"</TD><TD>Invio diretto</TD><TD>" +
								numRigheTot +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();

						if (maxRighe > 0) {
							numBlocchi = (int) (Math.ceil((double) numRigheTot	/ (double) maxRighe));
						}
						// MARZO 2014: Intervento Interno - Modifica nella gestione della risposta in 102:
						// non si esce dal ciclo degli allineamenti ma si riprova con un blocco successivo fino ad esaurimento dei blocchi possibili
						// inzia il ciclo di elaborazione dove si comincia ad elaborare i primi 50 record e poi si continua chiedendo
						// i file su blocchi successivi
						while (continuaCiclo) {
							String valorePassaggio = "";
							if (!listaOggetti[i].substring(0,2).equals("--")) {
								valorePassaggio = listaOggetti[i].substring(0,2);
							}

							areaDiRitorno = allineamentiDocumenti(areaDatiPass, sbnRisposta, valorePassaggio);

							if (!areaDiRitorno.getCodErr().equals("") && !areaDiRitorno.getCodErr().equals("0000")) {
								messaggio = messaggio + areaDatiPass.getTestoProtocollo();
							}


							if (numBlocchi > progressivoBlocco) {
								progressivoBlocco++;

								dataInizioRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();
								if (numListaDaAllineare == null || numListaDaAllineare.equals("0")) {
									this.indice.setMessage(sbnmessage, this.user);
									sbnRisposta = this.indice.eseguiRichiestaServer();
								} else {
									sbnRisposta = scaricoOggettiDaIndice(String.valueOf(numListaDaAllineare), progressivoBlocco, "");
								}
								dataFineRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();


								// Inizio Intervento interno almaviva2 su indicazione di POLO NAP esercizio 30.11.2010
								// se l'Indice non risponde per varie cause il controllo su Esito null si deve bloccare e tornare
								// con le aree correttamente impostate; SbnGestioneAllineamentiIndicePoloDao metodo:richiediListaAllineamenti

//								if (sbnRisposta == null) {
//									areaDatiPass.setCodErr("noServerInd");
//									return areaDatiPass;
//								}

								if (sbnRisposta == null
										|| sbnRisposta.getSbnMessage() == null
										|| sbnRisposta.getSbnMessage().getSbnResponse() == null
										|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult() == null
										|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() == null) {

									messaggio = messaggio + "Presenza del valore null nella risposta da Indice probabilmente dovuta a chiusura Indice - CHIAMATA SUCCESSIVA";

									// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
									// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
									// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
									// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
									// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
									// ma di seguito a: Errore in richiesta:
									outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
											dataInizioRichTrasf + "</TD><TD>" +	dataFineRichTrasf +
											"</TD><TD>Errore in richiesta: Presenza del valore null nella risposta da Indice testo probabilmente dovuta a chiusura Indice - CHIAMATA SUCCESSIVA</TD><TD>" + " " + "</TD></TR>");
									areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

									outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
									areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

									areaDatiPass.setCodErr("9999");
									areaDatiPass.setTestoProtocollo("Presenza del valore null nella risposta da Indice probabilmente dovuta a chiusura Indice - CHIAMATA SUCCESSIVA");
									return areaDatiPass;
								}
								// Fine Intervento interno almaviva2 su indicazione di POLO NAP esercizio 30.11.2010


								outputAllineamentoVO =
									new TracciatoStampaOutputAllineamentoVO("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");
								areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

								outputAllineamentoVO =
									new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\">" +
											"<TH WIDTH=\"15%\">Invio richiesta a Indice</TH>" +
											"<TH WIDTH=\"15%\">Arrivo risposta da Indice</TH>" +
											"<TH WIDTH=\"15%\">Tipo risposta</TH>" +
											"<TH WIDTH=\"35%\">Totale oggetti</TH>" +
											"</TR>");
								areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

								if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
									if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0102")) {
										// Siamo nel caso di 102 esplicito dell'Indice SITUAZIONE DI ERRORE GESTITO
										messaggio = messaggio + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

										outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
												dataInizioRichTrasf +
												"</TD><TD>" +
												dataFineRichTrasf +
												"</TD><TD>Richiesta invio su file:</TD><TD>" +
												sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +
												"</TD></TR>");
										areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
										continuaCiclo = false;
									} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0101")) {
										// Siamo nel caso di 101 esplicito dell'Indice (ERRORE DI MARSHALL) SITUAZIONE DI ERRORE GESTITO
										messaggio = messaggio + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

										outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
												dataInizioRichTrasf +
												"</TD><TD>" +
												dataFineRichTrasf +
												"</TD><TD>Richiesta invio su file:</TD><TD>" +
												sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +
												"</TD></TR>");
										areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
										continuaCiclo = false;
									} else {
										// Siamo nel caso di DIVERSO DA 102 esplicito dell'Indice SITUAZIONE DI ERRORE ***USCITA***
										messaggio = messaggio + areaDatiPass.getTestoProtocollo();
										outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
												dataInizioRichTrasf +
												"</TD><TD>" +
												dataFineRichTrasf +
												"</TD><TD>Richiesta invio su file:</TD><TD>" +
												sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +
												"</TD></TR>");
										areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
									}
								}
							} else {
								continuaCiclo = false;
							}
						}

						// Chiusura table di allineamento ciclico
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br><br>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);


//						 Inizio gestione Contatori per la tipologia di oggetto in lavorazione
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");


						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\" >");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						// Intervento almaviva2 29.12.2010 BUG MANTIS 3963 -
						// colspan portato a 3  (SbnGestioneAllineamentiIndicePoloDao - richiediListaAllineamenti)

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TD colspan=\"3\">Statistiche allineamento " + tipoOggetto + "</TD>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\" >" +
								"<TD WIDTH=\"15%\"><U>Tipo modifica</U></TD>" +
								"<TD WIDTH=\"15%\"><U>Inviati</U></TD>" +
								"<TD WIDTH=\"70%\"><U>Correttamente allineati</U></TD>" +
								"</TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Legami</TD><TD>" +
								contatoreLegami +
								"</TD><TD>" +
								contatoreLegamiOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Dati</TD><TD>" +
								contatoreDati +
								"</TD><TD>" +
								contatoreDatiOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Cancellazione</TD><TD>" +
								contatoreCancellaz +
								"</TD><TD>" +
								contatoreCancellazOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Scambio Forma</TD><TD>" +
								contatoreScambioForma +
								"</TD><TD>" +
								contatoreScambioFormaOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Fusione</TD><TD>" +
								contatoreFusione +
								"</TD><TD>" +
								contatoreFusioneOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Dati e Legami</TD><TD>" +
								contatoreDatiLegami +
								"</TD><TD>" +
								contatoreDatiLegamiOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Cambio Natura</TD><TD>" +
								contatoreNatura +
								"</TD><TD>" +
								contatoreNaturaOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Tipo non impostato</TD><TD>" +
								contatoreSenzaTipol +
								"</TD><TD>" +
								contatoreSenzaTipolOK +
								"</TD></TR>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br>&nbsp;");
						areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);



						// Fine gestione Contatori per la tipologia di oggetto in lavorazione
						numListaDaAllineare = "999999999";
					}
				} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3089")) {
					String appoggio = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
					String numListaStringa = this.ricavaIdLista(appoggio);
					numListaDaAllineare = numListaStringa;

					//almaviva5_20150330 allineamento posticipato per invio su file
					String dataOra = prenotaAllineamentoPerIdFile(areaDatiPass, numListaDaAllineare);
					boolean prenotato = ValidazioneDati.isFilled(dataOra);

					// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
					// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
					// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
					// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
					// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
					// ma di seguito a: Errore in richiesta:
					outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
							dataInizioRichTrasf + "</TD><TD>" + dataFineRichTrasf +
							"</TD><TD>Invio su file: " + numListaStringa + "</TD><TD>" +
							(prenotato ? "Per questo file una nuova richiesta sarà inoltrata in data " + dataOra : " ") + "</TD></TR>");
					areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

					outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
					areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

					//almaviva5_20150330 allineamento posticipato per invio su file
					if (!prenotato) {
						//allineamento per file immediato
						areaDatiPass.setIdFileAllineamenti(numListaStringa);
						areaDatiPass = allineaBaseLocale(areaDatiPass);
						areaDatiPass.setIdFileAllineamenti("");
					}

				} else {
					if (areaDatiPass.getTestoProtocollo() != null) {
						messaggio = messaggio + areaDatiPass.getTestoProtocollo();
						messaggio = messaggio + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();


						// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
						// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
						// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
						// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
						// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
						// ma di seguito a: Errore in richiesta:
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
								dataInizioRichTrasf + "</TD><TD>" + dataFineRichTrasf +
								"</TD><TD>Errore in richiesta:"+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +"</TD><TD>" + " " + "</TD></TR>");
					} else {
						messaggio = messaggio + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

						// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
						// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
						// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
						// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
						// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
						// ma di seguito a: Errore in richiesta:
						outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
								dataInizioRichTrasf + "</TD><TD>" + dataFineRichTrasf +
								"</TD><TD>Errore in richiesta:"+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +"</TD><TD>" + " " + "</TD></TR>");
					}
					areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
				}


				String valorePassaggio = "";
				if (!listaOggetti[i].substring(0,2).equals("--")) {
					valorePassaggio = listaOggetti[i].substring(0,2);
				}

				if (valorePassaggio.equals("")) {
					areaDatiPass.setNumListaDaAllineareDocumento(Integer.valueOf(numListaDaAllineare));
				} else if (valorePassaggio.equals("AU")) {
					areaDatiPass.setNumListaDaAllineareAutore(Integer.valueOf(numListaDaAllineare));
				} else if (valorePassaggio.equals("MA")) {
					areaDatiPass.setNumListaDaAllineareMarca(Integer.valueOf(numListaDaAllineare));
				} else if (valorePassaggio.equals("TU")) {
					areaDatiPass.setNumListaDaAllineareTitUniforme(Integer.valueOf(numListaDaAllineare));
				} else if (valorePassaggio.equals("UM")) {
					areaDatiPass.setNumListaDaAllineareTitUniformeMus(Integer.valueOf(numListaDaAllineare));
				}

				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			}

			// fine ciclo di richiesta lista allineamenti/xml allineamenti se inferiori a 50
			//====================================================================================================

			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br>&nbsp;");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br><H3>FINE FUNZIONE ALLINEAMENTO "
					+ DateUtil.getDate()
					+ " "
					+ DateUtil.getTime()
					+ "</H3>");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br>");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</body></html>");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			if (!messaggio.equals("")) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiPass;
			}

			return areaDatiPass;

		} catch (SbnMarcException ve) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPass;
	}

	public String ricavaIdLista(String messaggio) {
		String idLista="";
		int lunghezzaTot = messaggio.length() -1;
		for (int i = lunghezzaTot ; i > 0; i--) {
			try {
				Integer.valueOf(String.valueOf(messaggio.charAt(i)));
			}
			 catch (Exception e) {
				 return idLista;
			}
			 idLista = messaggio.charAt(i) + idLista;
		}
		return "";
	}


	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	public SBNMarc caricoOggettiDaFileLocale(final String identAllineamento, int progressivoInizio) {

		SBNMarc sbnRisposta = null;

		try {
			String nomeFile = costruisciNomeFile(identAllineamento, progressivoInizio);

        	File f = new File(nomeFile);
        	if (!f.exists()) {
        		return null;
        	}
        	String Xml = FileUtil.streamToString(new FileInputStream(f));
        	sbnRisposta = SBNMarc.unmarshalSBNMarc(new StringReader(Xml));


            return sbnRisposta;
		} catch (IllegalArgumentException ie) {
			System.out.println("ERROR >>" + ie.getMessage());
		} catch (MarshalException me) {
			return null;
		} catch (Exception e) {
			System.out.println("ERROR >>" + e.getMessage());
		}

		return sbnRisposta;
	}

	public AllineaVO allineamentiDocumenti(
			AllineaVO areaDatiPassReturn, SBNMarc sbnRisposta, String tipoAuthority) {


		SbnOutputType sbnOutputType;
		AllineaInfoType allineaInfoType;
		ComunicaAllineatiType comunicaAllineatiType;
		AllineatiType allineatiType;
		SbnOggetto sbnOggetto;

		String lastOggettoDaEleborare = "";
		String lastOggettoElaborato = "";
		String esito = "";
		int numOggetti = 0;

		numOggetti = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

		if (numOggetti == 0) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		sbnOutputType = new SbnOutputType();
		sbnOutputType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput();
		allineaInfoType = new AllineaInfoType();
		comunicaAllineatiType = new ComunicaAllineatiType();
		lastOggettoDaEleborare = "";
		lastOggettoElaborato = "";
		esito = "";
		DocumentoType documentoType = new DocumentoType();
		ElementAutType elementAutType = new ElementAutType();
		SbnMateriale sbnMateriale = null;
		SbnAuthority sbnAuthority = null;

		String messaggioIncrementale = "";


		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO;
		TracciatoStampaOutputAllineamentoVO outputAllineamentoVO;

		try {
			int contatore = 0;
			int allineaCount = sbnOutputType.getAllineaInfoCount();
	        for (int i = 0; i < allineaCount; i++) {
		        allineaInfoType = sbnOutputType.getAllineaInfo(i);
		       	lastOggettoDaEleborare = allineaInfoType.getT001();
		       	if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice() != null) {
			       	if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
			       		documentoType = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
			       		if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
			       			// AREA TITOLO-DOCUMENTO
			       			sbnMateriale = documentoType.getDocumentoTypeChoice().getDatiDocumento().getTipoMateriale();
			       			sbnAuthority = null;
			       		}
			       		if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
			       			// AREA TITOLO-ACCESSO
			       		// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
			       			//sbnMateriale = SbnMateriale.VALUE_5;
			       			sbnMateriale = SbnMateriale.valueOf(" ");
			       			sbnAuthority = null;
			       		}
			       	}
			       	if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
		       			// AREA AUTHORITY
			       		elementAutType = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
		       			sbnMateriale = null;
		       			sbnAuthority = elementAutType.getDatiElementoAut().getTipoAuthority();
			       	}
		       	} else {
		       		if (tipoAuthority == null || tipoAuthority.equals("")) {
		       		// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
		       			// sbnMateriale = SbnMateriale.VALUE_5;
		       			sbnMateriale = SbnMateriale.valueOf(" ");
		       			sbnAuthority = null;
		       		} else {
		       			sbnMateriale = null;
		       			sbnAuthority = SbnAuthority.valueOf(tipoAuthority);
		       		}
		       	}

		       	//allineaInfoType.getOggettoVariato().
	//			Valori ammessi per SbnTipoModifica Modifica:
	//	       	value_0 ="Legami"         FATTO
	//			value 1 ="Dati"           FATTO
	//			value 2 ="Cancellato"     FATTO
	//			value 3 ="Scambio Forma"  RIVEDERE ........... NON FUNZIONA
	//			value 4 ="Fusione"        FATTO
	//			value 5 ="Dati e Legami"  FATTO
	//			value 6 ="Natura"		  FATTO
		       	SbnOutputType sbnOutputTypeDaAllineare;
		       	if (allineaInfoType.getOggettoVariato().getTipoModifica() == null) {
		       		contatoreSenzaTipol++;
		       		messaggioIncrementale = messaggioIncrementale + " Mancata valorizzazione della tipologia di modifica da effettuare-Si effettua l'aggiornamento di Dati e legami" + " ";
		       		// esito = "9999";
		       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		       		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO(0, "", lastOggettoDaEleborare, "", "", messaggioIncrementale);
		       		areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		       		messaggioIncrementale = "";



		       		// Si invia una richiesta dome fosse Dati e Legami (value 5) per non lasciare l'allineamento inconcluso
	        		sbnOutputTypeDaAllineare = new SbnOutputType();
	        		String tipoAuthorityVariazione = "";
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
	        			DocumentoType[] documentoTypes = new DocumentoType[1];
	        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
	        			sbnOutputTypeDaAllineare.setDocumento(documentoTypes);
	        		}
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			sbnOutputTypeDaAllineare.setElementoAut(elementAutTypes);
	        			tipoAuthorityVariazione = elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString();
	        		}

	        		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
	        		areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	        		areaTabellaOggettiDaCatturareVO.setIdPadre(allineaInfoType.getT001());
	        		areaTabellaOggettiDaCatturareVO.setSoloRadice(false);
	        		areaTabellaOggettiDaCatturareVO.setTipoAuthority(tipoAuthorityVariazione);
	        		areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(sbnOutputTypeDaAllineare);
	        		// Modifica almaviva2 02.03.2010 -Gestire la cattura quando si proviene dall'Allinea quindi non si deve richiedere la localizzazione in Indice
	        		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(true);

	        		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	        		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	        		// del flag di avvenuto allineamento.
	        		areaTabellaOggettiDaCatturareVO.setAllineamentoDaFileLocale(areaDatiPassReturn.isAllineamentoDaFileLocale());

	        		String esitoAllinea = "0000";
	        		if (tipoAuthorityVariazione.equals("") || tipoAuthorityVariazione.equals("TU") || tipoAuthorityVariazione.equals("UM")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiReticoloPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("AU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiAutorePerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("MA")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiMarcaPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("LU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiLuogoPerCattura(areaTabellaOggettiDaCatturareVO);
	        		}

	        		if (!esitoAllinea.equals("0000")) {
	        			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio di Allineamento" + ":" + esitoAllinea + " ";
	        			esito = "9999";

			       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>VUOTO</TD><TD colspan=\"2\">" +
	        					messaggioIncrementale +
	        					"</TD></TR>");
	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";
	        		} else {
		        		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		        		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		        		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		        		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>VUOTO</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        		} else {
		        			esito = "";
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>VUOTO</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreSenzaTipolOK++;
		        		}
	        		}




		       	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_0_TYPE) {
	        		//=============================================================================================
	        		// value_0 ="Legami"
		       		// viene attivata la cattura completa anche perchè devo localizzare la radice al fine di localizzare
		       		// tutto il reticolo (legami nuovi compresi)

		       		contatoreLegami++;
	        		sbnOutputTypeDaAllineare = new SbnOutputType();
	        		String tipoAuthorityVariazione = "";
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
	        			DocumentoType[] documentoTypes = new DocumentoType[1];
	        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
	        			sbnOutputTypeDaAllineare.setDocumento(documentoTypes);
	        		}
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			sbnOutputTypeDaAllineare.setElementoAut(elementAutTypes);
	        			tipoAuthorityVariazione = elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString();
	        		}

	        		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
	        		areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	        		areaTabellaOggettiDaCatturareVO.setIdPadre(allineaInfoType.getT001());
	        		areaTabellaOggettiDaCatturareVO.setSoloRadice(false);
	        		areaTabellaOggettiDaCatturareVO.setTipoAuthority(tipoAuthorityVariazione);
	        		areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(sbnOutputTypeDaAllineare);
	        		// Modifica almaviva2 02.03.2010 -Gestire la cattura quando si proviene dall'Allinea quindi non si deve richiedere la localizzazione in Indice
	        		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(true);

	        		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	        		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	        		// del flag di avvenuto allineamento.
	        		areaTabellaOggettiDaCatturareVO.setAllineamentoDaFileLocale(areaDatiPassReturn.isAllineamentoDaFileLocale());

	        		String esitoAllinea = "0000";
	        		if (tipoAuthorityVariazione.equals("") || tipoAuthorityVariazione.equals("TU") || tipoAuthorityVariazione.equals("UM")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiReticoloPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("AU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiAutorePerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("MA")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiMarcaPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("LU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiLuogoPerCattura(areaTabellaOggettiDaCatturareVO);
	        		}

	        		if (!esitoAllinea.equals("0000")) {
	        			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio di Allineamento" + ":" + esitoAllinea + " ";
	        			esito = "9999";

			       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					"</TD><TD colspan=\"2\">" +
	        					messaggioIncrementale +
	        					"</TD></TR>");
			       		areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
			       		messaggioIncrementale = "";

	        		} else {
		        		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		        		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		        		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		        		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        		} else {
		        			esito = "";
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreLegamiOK++;
		        		}
	        		}
	        		//=============================================================================================
	        	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_1_TYPE) {
	        		//=============================================================================================
	        		// value 1 ="Dati"
	        		// Viene attivata la cattura con il flag SoloRadice(true);
	        		contatoreDati++;
	        		sbnOutputTypeDaAllineare = new SbnOutputType();
	        		String tipoAuthorityVariazione = "";
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
	        			DocumentoType[] documentoTypes = new DocumentoType[1];
	        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
	        			sbnOutputTypeDaAllineare.setDocumento(documentoTypes);
	        		}
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			sbnOutputTypeDaAllineare.setElementoAut(elementAutTypes);
	        			tipoAuthorityVariazione = elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString();
	        		}

	        		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
	        		areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	        		areaTabellaOggettiDaCatturareVO.setIdPadre(allineaInfoType.getT001());
	        		areaTabellaOggettiDaCatturareVO.setSoloRadice(true);
	        		areaTabellaOggettiDaCatturareVO.setTipoAuthority(tipoAuthorityVariazione);
	        		areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(sbnOutputTypeDaAllineare);
	        		// Modifica almaviva2 02.03.2010 -Gestire la cattura quando si proviene dall'Allinea quindi non si deve richiedere la localizzazione in Indice
	        		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(true);

	        		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	        		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	        		// del flag di avvenuto allineamento.
	        		areaTabellaOggettiDaCatturareVO.setAllineamentoDaFileLocale(areaDatiPassReturn.isAllineamentoDaFileLocale());

	        		String esitoAllinea = "0000";
	        		if (tipoAuthorityVariazione.equals("") || tipoAuthorityVariazione.equals("TU") || tipoAuthorityVariazione.equals("UM")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiReticoloPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("AU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiAutorePerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("MA")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiMarcaPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("LU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiLuogoPerCattura(areaTabellaOggettiDaCatturareVO);
	        		}

	        		if (!esitoAllinea.equals("0000")) {
	        			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio di Allineamento" + ":" + esitoAllinea + " ";
	        			esito = "9999";

	        			messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					"</TD><TD colspan=\"2\">" +
	        					messaggioIncrementale +
	        					"</TD></TR>");
	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";

	        		} else {
		        		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		        		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		        		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		        		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";

		        		} else {
		        			esito = "";
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreDatiOK++;
		        		}
	        		}

	        		//=============================================================================================
	        	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_2_TYPE) {
	        		// =============================================================================================
	        		// value 2 ="Cancellato"
	        		contatoreCancellaz++;
	        		AreaDatiPassaggioCancAuthorityVO areaDatiPassaggioCancAuthorityVO = new AreaDatiPassaggioCancAuthorityVO();
	        		areaDatiPassaggioCancAuthorityVO.setBid(allineaInfoType.getT001());
	        		areaDatiPassaggioCancAuthorityVO.setRicercaIndice(false);
	        		areaDatiPassaggioCancAuthorityVO.setRicercaPolo(true);
	        		//almaviva5_20111115 controllo esistenza legami a fascicoli
	        		areaDatiPassaggioCancAuthorityVO.setTicket(areaDatiPassReturn.getTicket());
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice() == null) {

	        			// Modifica almaviva2 02.12.2010 BUG MANTIS 3967
	        			// vengono eliminate varie righe diagnostica perchè duplicate con quelle successive
	        			// (SbnGestioneAllineamentiIndicePoloDao - allineamentiDocumenti)
//	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
//	        					lastOggettoDaEleborare +
//	        					"</TD><TD>" +
//	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
//	        					"</TD><TD colspan=\"2\">" +
//	        					"PASSAGGIO PER CANCELLAZIONE OGGETTO" +
//	        					"</TD></TR>");
//	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);


//	        			if (sbnMateriale != null) {
//	        				areaDatiPassaggioCancAuthorityVO.setTipoMat(sbnMateriale.toString());
//	        			}
//	        			if (sbnAuthority != null) {
//	        				areaDatiPassaggioCancAuthorityVO.setTipoAut(sbnAuthority.toString());
//	        			}

	        			if (allineaInfoType.getT001().substring(3, 4).equals("V")) {
	        				areaDatiPassaggioCancAuthorityVO.setTipoAut("AU");
	        			} else if (allineaInfoType.getT001().substring(3, 4).equals("M")) {
	        				areaDatiPassaggioCancAuthorityVO.setTipoAut("MA");
	        			} else if (allineaInfoType.getT001().substring(3, 4).equals("L")) {
	        				areaDatiPassaggioCancAuthorityVO.setTipoAut("LU");
	        			} else {
	        				// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
	        				// areaDatiPassaggioCancAuthorityVO.setTipoMat((SbnMateriale.VALUE_5).toString());
	        				areaDatiPassaggioCancAuthorityVO.setTipoMat((SbnMateriale.valueOf(" ")).toString());
	        			}

	        		} else {
	        			if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
		        			DocumentoType[] documentoTypes = new DocumentoType[1];
		        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
		        			if (documentoTypes[0].getDocumentoTypeChoice().getDatiDocumento() != null) {
		        				DatiDocType datiDocType = new DatiDocType();
		        				datiDocType = documentoTypes[0].getDocumentoTypeChoice().getDatiDocumento();
		        				areaDatiPassaggioCancAuthorityVO.setTipoMat(datiDocType.getTipoMateriale().toString());
		        			}
		        			if (documentoTypes[0].getDocumentoTypeChoice().getDatiTitAccesso() != null) {
		        				// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
		        				// areaDatiPassaggioCancAuthorityVO.setTipoMat((SbnMateriale.VALUE_5).toString());
		        				areaDatiPassaggioCancAuthorityVO.setTipoMat((SbnMateriale.valueOf(" ")).toString());
		        			}
		        		}
		        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
		        			ElementAutType[] elementAutTypes = new ElementAutType[1];
		        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
		        			areaDatiPassaggioCancAuthorityVO.setTipoAut(elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString());
		        		}
	        		}

	        		SbnGestioneAllAuthorityDao sbnGestioneAllAuthorityDao = new SbnGestioneAllAuthorityDao(indice, polo, user);

//	        		almaviva2 27 gennaio 2010 - Segnalazione Scafuto - creata per gestire la cancellazione Authority in Allineamento
	        		sbnGestioneAllAuthorityDao.cancellaAuthorityDaAllineamento(areaDatiPassaggioCancAuthorityVO);
	        		if (!areaDatiPassaggioCancAuthorityVO.getCodErr().equals("0000")) {
	        			// Intervento interno - 24.11.2009 almaviva2 - Se l'oggetto di partenza della fuione è già assente
	        			// dalla Base Dati di Polo si invia segnalazione ma comunque si spegne il flig di allineamento perchè
	        			// l'operazione è comunque stata fatta.
	        			if (areaDatiPassaggioCancAuthorityVO.getTestoProtocollo().contains("3001")
	        					|| areaDatiPassaggioCancAuthorityVO.getTestoProtocollo().contains("3013")) {
	        				messaggioIncrementale = messaggioIncrementale + areaDatiPassaggioCancAuthorityVO.getTestoProtocollo() +
        					" - L'oggetto della cancellazione è già assente dalla Base Dati di Polo." + " ";

//		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
	        			} else {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassaggioCancAuthorityVO.getTestoProtocollo() + " ";
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
	        			}

	        		} else {
	        			esito = "";
	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					"</TD><TD colspan=\"2\">" +
	        					"OK" +
	        					"</TD></TR>");
	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";
	        			contatoreCancellazOK++;
	        		}

	        		//=============================================================================================
	        	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_3_TYPE) {
	        		// value 3 ="Scambio Forma"
	        		contatoreScambioForma++;
	        		sbnOutputTypeDaAllineare = new SbnOutputType();

	        		String tipoAuthorityVariazione = "";
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			sbnOutputTypeDaAllineare.setElementoAut(elementAutTypes);
	        			tipoAuthorityVariazione = elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString();
	        		}

	        		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
	        		areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	        		areaTabellaOggettiDaCatturareVO.setIdPadre(allineaInfoType.getT001());
	        		areaTabellaOggettiDaCatturareVO.setSoloRadice(false);
	        		areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(sbnOutputTypeDaAllineare);
	        		areaTabellaOggettiDaCatturareVO.setTipoAuthority(tipoAuthorityVariazione);
	        		// Modifica almaviva2 02.03.2010 -Gestire la cattura quando si proviene dall'Allinea quindi non si deve richiedere la localizzazione in Indice
	        		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(true);

	        		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	        		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	        		// del flag di avvenuto allineamento.
	        		areaTabellaOggettiDaCatturareVO.setAllineamentoDaFileLocale(areaDatiPassReturn.isAllineamentoDaFileLocale());

	        		String esitoAllinea = "0000";
	        		if (tipoAuthorityVariazione.equals("AU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiAutorePerCattura(areaTabellaOggettiDaCatturareVO);
	        		}
	        		if (tipoAuthorityVariazione.equals("LU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiLuogoPerCattura(areaTabellaOggettiDaCatturareVO);
	        		}

	        		if (!esitoAllinea.equals("0000")) {
	        			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio di Allineamento" + ":" + esitoAllinea + " ";
	        			esito = "9999";

			       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					"</TD><TD colspan=\"2\">" +
	        					messaggioIncrementale +
	        					"</TD></TR>");
	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";
	        		} else {
		        		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		        		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		        		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		        		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        		} else {
		        			esito = "";
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreScambioFormaOK++;
		        		}
	        		}
	        	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_4_TYPE) {
	        		//=============================================================================================
	        		// value 4 ="Fusione"
	        		contatoreFusione++;
	        		AreaDatiAccorpamentoVO areaDatiPassAccorpamento = new AreaDatiAccorpamentoVO();

	        		areaDatiPassAccorpamento.setLivelloBaseDati("I");
	        		areaDatiPassAccorpamento.setChiamataAllineamento(true);
	        		areaDatiPassAccorpamento.setIdElementoEliminato(allineaInfoType.getT001());

	        		sbnOutputTypeDaAllineare = new SbnOutputType();
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
	        			DocumentoType[] documentoTypes = new DocumentoType[1];
	        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
	        			if (documentoTypes[0].getDocumentoTypeChoice().getDatiDocumento() != null) {
	        				DatiDocType datiDocType = new DatiDocType();
	        				datiDocType = documentoTypes[0].getDocumentoTypeChoice().getDatiDocumento();
	        				areaDatiPassAccorpamento.setIdElementoAccorpante(datiDocType.getT001());
	        				areaDatiPassAccorpamento.setTipoMateriale(datiDocType.getTipoMateriale().toString());
	        			}
	        			if (documentoTypes[0].getDocumentoTypeChoice().getDatiTitAccesso() != null) {
	        				TitAccessoType titAccessoType = new TitAccessoType();
	        				titAccessoType = documentoTypes[0].getDocumentoTypeChoice().getDatiTitAccesso();
	        				areaDatiPassAccorpamento.setIdElementoAccorpante(titAccessoType.getT001());
	        			}
	        		}
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			areaDatiPassAccorpamento.setIdElementoAccorpante(elementAutTypes[0].getDatiElementoAut().getT001());
	        			areaDatiPassAccorpamento.setTipoAuthority(elementAutTypes[0].getDatiElementoAut().getTipoAuthority());
	        		}

	        		SbnGestioneAccorpamentoDao sbnGestioneAccorpamentoDao = new SbnGestioneAccorpamentoDao(indice, polo, user);
	        		AreaDatiAccorpamentoReturnVO areaDatiAccorpamentoReturnVO = sbnGestioneAccorpamentoDao.richiestaAccorpamento(areaDatiPassAccorpamento);

	        		if (!areaDatiAccorpamentoReturnVO.getCodErr().equals("0000")) {
	        			// Intervento interno - 24.11.2009 almaviva2 - Se l'oggetto di partenza della fuione è già assente
	        			// dalla Base Dati di Polo si invia segnalazione ma comunque si spegne il flig di allineamento perchè
	        			// l'operazione è comunque stata fatta.

	        			// Intervento almaviva2 04.01.2011 - BUG MANTIS 4114 - il codice di ritorno in caso di fusione di
	        			// un oggetto assente da Base Dati di Polo oltre al 3991 è anche 3013 - implementato l'if
	        			// (SbnGestioneAllineamentiIndicePoloDao - allineamentiDocumenti)
//	        			if (areaDatiAccorpamentoReturnVO.getCodErr().equals("3991")) {
	        			if (areaDatiAccorpamentoReturnVO.getCodErr().equals("3991") || areaDatiAccorpamentoReturnVO.getCodErr().equals("3013")) {
	        				messaggioIncrementale = messaggioIncrementale + areaDatiAccorpamentoReturnVO.getTestoProtocollo() +
	        					" - L'oggetto di partenza della fusione e' gia' cancellato." + " ";
//		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");

		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					" su " +
		        					areaDatiPassAccorpamento.getIdElementoAccorpante() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreFusioneOK++;
	        			} else {
	        				messaggioIncrementale = messaggioIncrementale + areaDatiAccorpamentoReturnVO.getTestoProtocollo() + " ";
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");

		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					" su " +
		        					areaDatiPassAccorpamento.getIdElementoAccorpante() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
	        			}
	        		} else {
	        			esito = "";
	        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					" su " +
	        					areaDatiPassAccorpamento.getIdElementoAccorpante() +
	        					"</TD><TD colspan=\"2\">" +
	        					"OK" +
	        					"</TD></TR>");
	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";
	        			contatoreFusioneOK++;
	        		}
	        		//=============================================================================================
	        	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_5_TYPE) {
	        		//=============================================================================================
	        		// value 5 ="Dati e Legami"
	        		contatoreDatiLegami++;
	        		sbnOutputTypeDaAllineare = new SbnOutputType();
	        		String tipoAuthorityVariazione = "";
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
	        			DocumentoType[] documentoTypes = new DocumentoType[1];
	        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
	        			sbnOutputTypeDaAllineare.setDocumento(documentoTypes);
	        		}
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			sbnOutputTypeDaAllineare.setElementoAut(elementAutTypes);
	        			tipoAuthorityVariazione = elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString();
	        		}

	        		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
	        		areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	        		areaTabellaOggettiDaCatturareVO.setIdPadre(allineaInfoType.getT001());
	        		areaTabellaOggettiDaCatturareVO.setSoloRadice(false);
	        		areaTabellaOggettiDaCatturareVO.setTipoAuthority(tipoAuthorityVariazione);
	        		areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(sbnOutputTypeDaAllineare);
	        		// Modifica almaviva2 02.03.2010 -Gestire la cattura quando si proviene dall'Allinea quindi non si deve richiedere la localizzazione in Indice
	        		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(true);

	        		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	        		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	        		// del flag di avvenuto allineamento.
	        		areaTabellaOggettiDaCatturareVO.setAllineamentoDaFileLocale(areaDatiPassReturn.isAllineamentoDaFileLocale());

	        		String esitoAllinea = "0000";
	        		if (tipoAuthorityVariazione.equals("") || tipoAuthorityVariazione.equals("TU") || tipoAuthorityVariazione.equals("UM")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiReticoloPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("AU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiAutorePerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("MA")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiMarcaPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("LU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiLuogoPerCattura(areaTabellaOggettiDaCatturareVO);
	        		}

	        		if (!esitoAllinea.equals("0000")) {
	        			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio di Allineamento" + ":" + esitoAllinea + " ";
	        			esito = "9999";

			       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
			       		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					"</TD><TD colspan=\"2\">" +
	        					messaggioIncrementale +
	        					"</TD></TR>");
	        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";
	        		} else {
		        		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		        		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		        		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		        		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
				       		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
				       		areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        		} else {
		        			esito = "";
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreDatiLegamiOK++;
		        		}
	        		}

	        		//=============================================================================================
	        	} else if (allineaInfoType.getOggettoVariato().getTipoModifica().getType() == SbnTipoModifica.VALUE_6_TYPE) {
	        		// value 6 ="Natura"
	        		contatoreNatura++;
	        		// Viene attivata la cattura con il flag SoloRadice(true);
	        		sbnOutputTypeDaAllineare = new SbnOutputType();
	        		String tipoAuthorityVariazione = "";
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento() != null) {
	        			DocumentoType[] documentoTypes = new DocumentoType[1];
	        			documentoTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getDocumento();
	        			sbnOutputTypeDaAllineare.setDocumento(documentoTypes);
	        		}
	        		if (allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut() != null) {
	        			ElementAutType[] elementAutTypes = new ElementAutType[1];
	        			elementAutTypes[0] = allineaInfoType.getOggettoVariato().getOggettoVariatoTypeChoice().getElementoAut();
	        			sbnOutputTypeDaAllineare.setElementoAut(elementAutTypes);
	        			tipoAuthorityVariazione = elementAutTypes[0].getDatiElementoAut().getTipoAuthority().toString();
	        		}

	        		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
	        		areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	        		areaTabellaOggettiDaCatturareVO.setIdPadre(allineaInfoType.getT001());
	        		areaTabellaOggettiDaCatturareVO.setSoloRadice(true);
	        		areaTabellaOggettiDaCatturareVO.setTipoAuthority(tipoAuthorityVariazione);
	        		areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(sbnOutputTypeDaAllineare);
	        		// Modifica almaviva2 02.03.2010 -Gestire la cattura quando si proviene dall'Allinea quindi non si deve richiedere la localizzazione in Indice
	        		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(true);

	        		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	        		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	        		// del flag di avvenuto allineamento.
	        		areaTabellaOggettiDaCatturareVO.setAllineamentoDaFileLocale(areaDatiPassReturn.isAllineamentoDaFileLocale());

	        		String esitoAllinea = "0000";
	        		if (tipoAuthorityVariazione.equals("") || tipoAuthorityVariazione.equals("TU") || tipoAuthorityVariazione.equals("UM")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiReticoloPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("AU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiAutorePerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("MA")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiMarcaPerCattura(areaTabellaOggettiDaCatturareVO);
	        		} else if (tipoAuthorityVariazione.equals("LU")) {
	        			esitoAllinea = gestioneAllAuthority.ricercaElementiLuogoPerCattura(areaTabellaOggettiDaCatturareVO);
	        		}

	        		if (!esitoAllinea.equals("0000")) {
	        			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio di Allineamento" + ":" + esitoAllinea + " ";
	        			esito = "9999";

			       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
			       		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	        					lastOggettoDaEleborare +
	        					"</TD><TD>" +
	        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
	        					"</TD><TD colspan=\"2\">" +
	        					messaggioIncrementale +
	        					"</TD></TR>");
			       		areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
	        			messaggioIncrementale = "";
	        		} else {
		        		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		        		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		        		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		        		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
		        			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
		        			esito = "9999";

				       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
				       		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					messaggioIncrementale +
		        					"</TD></TR>");
				       		areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        		} else {
		        			esito = "";
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		        					lastOggettoDaEleborare +
		        					"</TD><TD>" +
		        					allineaInfoType.getOggettoVariato().getTipoModifica().toString() +
		        					"</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
		        			messaggioIncrementale = "";
		        			contatoreNaturaOK++;
		        		}
	        		}
	        	}


				if (esito.equals("0000") || esito.equals("")) {
					lastOggettoElaborato = lastOggettoDaEleborare;
					lastOggettoDaEleborare = "";

					// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
			    	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
			    	// del flag di avvenuto allineamento.
					if (!areaDatiPassReturn.isAllineamentoDaFileLocale()) {
			       		allineatiType = new AllineatiType();
						sbnOggetto = new SbnOggetto();

						if (sbnAuthority != null && !sbnAuthority.equals("")) {
							sbnOggetto.setTipoAuthority(sbnAuthority);
						} else {
							sbnOggetto.setTipoMateriale(sbnMateriale);
						}

						allineatiType.setTipoOggetto(sbnOggetto);
						allineatiType.setIdAllineato(lastOggettoElaborato);
						comunicaAllineatiType.addAllineati(allineatiType);
						contatore++;

					    SbnMessageType sbnmessage = new SbnMessageType();
				        SbnRequestType sbnrequest = new SbnRequestType();
						sbnrequest.setComunicaAllineati(comunicaAllineatiType);
						sbnmessage.setSbnRequest(sbnrequest);

						this.indice.setMessage(sbnmessage, this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							areaDatiPassReturn.setCodErr("noServerInd");
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>" +
		        					lastOggettoElaborato +
		        					"</TD><TD>" +
		        					"Spegnimento flag allineamento" +
		        					"</TD><TD colspan=\"2\">" +
		        					"noServerInd" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
							return areaDatiPassReturn;
						} else if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
							areaDatiPassReturn.setCodErr("9999");
							areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>" +
		        					lastOggettoElaborato +
		        					"</TD><TD>" +
		        					"Spegnimento flag allineamento" +
		        					"</TD><TD colspan=\"2\">" +
		        					areaDatiPassReturn.getTestoProtocollo() +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
//							return areaDatiPassReturn;
						} else {
		        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>" +
		        					lastOggettoElaborato +
		        					"</TD><TD>" +
		        					"Spegnimento flag allineamento" +
		        					"</TD><TD colspan=\"2\">" +
		        					"OK" +
		        					"</TD></TR>");
		        			areaDatiPassReturn.getLogAnalitico().add(outputAllineamentoVO);
						}
			       	}


					contatore = 0;
					comunicaAllineatiType.clearAllineati();
				} else {
					esito = "";
				}
	        }

		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (IllegalArgumentException ie) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setCodErr("");
		areaDatiPassReturn.setTestoProtocollo("");
		return areaDatiPassReturn;
	}


	public AllineaVO allineamentoRepertoriDaIndice(String ticket) {

		AllineaVO areaDatiAllineamentoPoloIndiceVO  = new AllineaVO();
		areaDatiAllineamentoPoloIndiceVO.setCodErr("0000");

		SBNMarc sbnRisposta = null;
		SbnMessageType sbnmessage = null;
		SbnRequestType sbnrequest = null;
		CercaType cercaType = null;
		CercaElementoAutType cercaElemento = null;

		CanaliCercaDatiAutType canaliCercaDatiAutType = null;
		StringaCercaType stringaCercaType = null;
		StringaCercaTypeChoice stringaCercaTypeChoice = null;

		try {
			sbnRisposta = null;

			sbnmessage = new SbnMessageType();
			sbnrequest = new SbnRequestType();

			cercaType = new CercaType();
			cercaElemento = new CercaElementoAutType();

			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(3999);

			cercaType.setCercaElementoAut(cercaElemento);
			CercaSoggettoDescrittoreClassiReperType cercaSoggettoDescrittoreClassiReperType = new CercaSoggettoDescrittoreClassiReperType();

			canaliCercaDatiAutType = new CanaliCercaDatiAutType();
			stringaCercaType = new StringaCercaType();
			stringaCercaTypeChoice = new StringaCercaTypeChoice();
			stringaCercaTypeChoice.setStringaLike("");

			stringaCercaType.setStringaCercaTypeChoice(stringaCercaTypeChoice);
			canaliCercaDatiAutType.setStringaCerca(stringaCercaType);

			cercaSoggettoDescrittoreClassiReperType.setTipoAuthority(SbnAuthority.RE);
			cercaSoggettoDescrittoreClassiReperType.setCanaliCercaDatiAut(canaliCercaDatiAutType);

			cercaElemento.setCercaDatiAut(cercaSoggettoDescrittoreClassiReperType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			this.indice.setMessage(sbnmessage,this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiAllineamentoPoloIndiceVO.setCodErr("noServerInd");
				return areaDatiAllineamentoPoloIndiceVO;
			} else if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				areaDatiAllineamentoPoloIndiceVO.setCodErr("9999");
				areaDatiAllineamentoPoloIndiceVO.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiAllineamentoPoloIndiceVO;
			}

			SbnOutputType sbnOutput = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput();

			//almaviva5_20140129 #5479
			getRepertori().truncate(ticket);

			if (sbnOutput.getElementoAutCount() > 0) {
				int numRepertori = sbnOutput.getElementoAutCount();

				for (int numRep = 0; numRep < numRepertori; numRep++) {
					ElementAutType elementAutType = sbnOutput.getElementoAut(numRep);
					RepertorioType repertorioType = (RepertorioType) elementAutType.getDatiElementoAut();

					CreaType creaType = null;
					CreaTypeChoice creaTypeChoice = null;

					SbnMessageType sbnmessageInsert = new SbnMessageType();
					SbnRequestType sbnrequestInsert = new SbnRequestType();
					SBNMarc sbnRispostaInsert = null;

					creaType = new CreaType();
					creaType.setTipoControllo(SbnSimile.CONFERMA);
					creaType.setCattura(true);
					creaTypeChoice = new CreaTypeChoice();

					ElementAutType elementAutTypeInsert = new ElementAutType();
					elementAutTypeInsert.setDatiElementoAut(repertorioType);

					creaTypeChoice.setElementoAut(elementAutTypeInsert);
					creaType.setCreaTypeChoice(creaTypeChoice);
					sbnrequestInsert.setCrea(creaType);

					sbnmessageInsert.setSbnRequest(sbnrequestInsert);
					this.polo.setMessage(sbnmessageInsert, this.user);
					sbnRispostaInsert = this.polo.eseguiRichiestaServer();

					if (sbnRispostaInsert == null) {
						areaDatiAllineamentoPoloIndiceVO.setCodErr("noServerPol");
						return areaDatiAllineamentoPoloIndiceVO;
					}
					SbnResultType sbnResult = sbnRispostaInsert.getSbnMessage().getSbnResponse().getSbnResult();
					String esito = sbnResult.getEsito();
					if (!ValidazioneDati.in(esito, "0000", "3090")) {
						areaDatiAllineamentoPoloIndiceVO.setCodErr("9999");
						areaDatiAllineamentoPoloIndiceVO.setTestoProtocollo(sbnResult.getTestoEsito());
						return areaDatiAllineamentoPoloIndiceVO;
					}
				}

				//almaviva5_20140129 #5479
				getRepertori().bindRepertori();
			}
		} catch (SbnMarcException ve) {
			areaDatiAllineamentoPoloIndiceVO.setCodErr("9999");
			areaDatiAllineamentoPoloIndiceVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiAllineamentoPoloIndiceVO;
		} catch (IllegalArgumentException ie) {
			areaDatiAllineamentoPoloIndiceVO.setCodErr("9999");
			areaDatiAllineamentoPoloIndiceVO.setTestoProtocollo("ERROR >>" + ie.getMessage());
			return areaDatiAllineamentoPoloIndiceVO;
		} catch (Exception e) {
			areaDatiAllineamentoPoloIndiceVO.setCodErr("9999");
			areaDatiAllineamentoPoloIndiceVO.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiAllineamentoPoloIndiceVO;
		}

		return areaDatiAllineamentoPoloIndiceVO;
	}

	public CatturaMassivaBatchVO catturaMassivaBatch(
			CatturaMassivaBatchVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		TracciatoStampaOutputAllineamentoVO outputAllineamentoVO =
				new TracciatoStampaOutputAllineamentoVO(0, "", "", "", "", "INIZIO FUNZIONE CATTURA MASSIVA " + DateUtil.getDate()+ " " + DateUtil.getTime());
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo(null);
		String messaggio = "";
		String messaggioIncrementale = "";
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO;

		contatoreDati = areaDatiPass.getListaBidDaCatturare().size();
		contatoreDatiOK = 0;
		String esitoAllinea = "0000";

		for (int i = 0; i < contatoreDati; i++) {

			// Inizio Modifica 19.03.2010 - almaviva2 gestione STOP processi batch differiti (per gent. conc. almaviva5)
			try {
				BatchManager.getBatchManagerInstance().checkForInterruption(areaDatiPass.getIdBatch());
			} catch (BatchInterruptedException eBI) {
				eBI.printStackTrace();
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo
					(eBI.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
				return areaDatiPass;
			} catch (Exception e) {
				e.printStackTrace();
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo
					(e.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
				return areaDatiPass;
			}
			// Fine Modifica 19.03.2010 - almaviva2

			esitoAllinea = "0000";

			areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
			areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getListaBidDaCatturare().get(i));
			areaTabellaOggettiDaCatturareVO.setSoloRadice(false);
			areaTabellaOggettiDaCatturareVO.setSbnOutputTypeDaAllineare(null);

			esitoAllinea = gestioneAllAuthority.ricercaElementiReticoloPerCattura(areaTabellaOggettiDaCatturareVO);

    		if (!esitoAllinea.equals("0000")) {
    			messaggioIncrementale = messaggioIncrementale + " Errore durante la fase di decodifica messaggio da Indice per "
    				+ areaDatiPass.getListaBidDaCatturare().get(i) + ":" + esitoAllinea + " ";

	       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
    			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO(0, "", "", "", "", messaggioIncrementale);
    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
    			messaggioIncrementale = "";
    		} else {
    			// almaviva2 Settembre 2014: viene inserito il controllo sull'esito della lettura in Indice del Doc da Catturare
    			// per inviare la corretta messaggistica sul log e continuarec la cattura dei successivi elementi
    			if (areaTabellaOggettiDaCatturareVO.getTabellaOggettiPerCattura().isEmpty()) {
        			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO
       				(0, "", areaDatiPass.getListaBidDaCatturare().get(i), "", "", "Documento assente o cancellato dalla Base Dati di Indice");
        			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
        			messaggioIncrementale = "";
    			} else {
    				// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
            		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
            		areaDatiPassReturnInsert = gestioneAllAuthority.inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
            		if (areaDatiPassReturnInsert.getTestoProtocollo() != null && !areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {
            			messaggioIncrementale = messaggioIncrementale + areaDatiPassReturnInsert.getTestoProtocollo();
    		       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
            			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO
    	       				(0, "", areaDatiPass.getListaBidDaCatturare().get(i), "", "", messaggioIncrementale);
            			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
            			messaggioIncrementale = "";
            		} else {
            			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO
    	       				(0, "", areaDatiPass.getListaBidDaCatturare().get(i), "", "", "OK");
            			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
            			messaggioIncrementale = "";
            			contatoreDatiOK++;
            		}
    			}
    		}
		}

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO
			(0, "", "", "", "", "Totale oggetti catturati = " + contatoreDati	+ " di cui " + contatoreDatiOK + " correttamente completati");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		outputAllineamentoVO =
				new TracciatoStampaOutputAllineamentoVO(0, "", "", "", "", "FINE FUNZIONE CATTURA MASSIVA " + DateUtil.getDate() + " " + DateUtil.getTime());
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		return areaDatiPass;

	}



	public FusioneMassivaBatchVO fusioneMassivaBatchVO(
			FusioneMassivaBatchVO areaDatiPass) {

		TracciatoStampaOutputAllineamentoVO outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO(
				"<TR><TD colspan=\"5\">INIZIO FUNZIONE FUSIONE MASSIVA "
						+ DateUtil.getDate() + " " + DateUtil.getTime()
						+ "</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo(null);
		String messaggioIncrementale = "";

		SbnGestioneAccorpamentoDao sbnGestioneAccorpamentoDao = new SbnGestioneAccorpamentoDao(indice, polo, user);
		AreaDatiAccorpamentoVO areaDatiPassAccorpamento;
		AreaDatiAccorpamentoReturnVO areaDatiAccorpamentoReturnVO;

		String tipoOggettoDaFondere = "";

		List<String> listaCoppieBidDaFondere = areaDatiPass.getListaCoppieBidDaFondere();
		if (ValidazioneDati.isFilled(listaCoppieBidDaFondere) ) {
			contatoreDati = listaCoppieBidDaFondere.size();

			for (int i = 0; i < contatoreDati; i++) {

				// Inizio Modifica 19.03.2010 - almaviva2 gestione STOP processi batch differiti (per gent. conc. almaviva5)
				try {
					BatchManager.getBatchManagerInstance().checkForInterruption(areaDatiPass.getIdBatch());
				} catch (BatchInterruptedException eBI) {
					eBI.printStackTrace();
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(eBI.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
					return areaDatiPass;
				} catch (Exception e) {
					e.printStackTrace();
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(e.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
					return areaDatiPass;
				}
				// Fine Modifica 19.03.2010 - almaviva2

				areaDatiPassAccorpamento = new AreaDatiAccorpamentoVO();
				areaDatiPassAccorpamento.setLivelloBaseDati("I");
	    		areaDatiPassAccorpamento.setChiamataAllineamento(true);

	    		// Inizio Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797 - si identifica la chiamata per fusione massiva perchè
	    		// il bid da eliminare deve essere marcato come solo locale (non condiviso)
	    		areaDatiPassAccorpamento.setChiamataFusioneMassiva(true);
	    		// Fine Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797
	    		areaDatiPassAccorpamento.setIdElementoEliminato(listaCoppieBidDaFondere.get(i)
	    				.toString().substring(0,10));
				areaDatiPassAccorpamento.setIdElementoAccorpante(listaCoppieBidDaFondere.get(i)
	    				.toString().substring(11));
				if (areaDatiPassAccorpamento.getIdElementoEliminato().substring(3,4).equals("V")) {
					areaDatiPassAccorpamento.setTipoAuthority(SbnAuthority.AU);
					tipoOggettoDaFondere ="Fusione tra autori";
				} else if (areaDatiPassAccorpamento.getIdElementoEliminato().substring(3,4).equals("M")) {
					areaDatiPassAccorpamento.setTipoAuthority(SbnAuthority.MA);
					tipoOggettoDaFondere ="Fusione tra marche";
				} else if (areaDatiPassAccorpamento.getIdElementoEliminato().substring(3,4).equals("L")) {
					areaDatiPassAccorpamento.setTipoAuthority(SbnAuthority.LU);
					tipoOggettoDaFondere ="Fusione tra luoghi";
				} else {
					// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
					// areaDatiPassAccorpamento.setTipoMateriale(SbnMateriale.VALUE_5.toString());
					areaDatiPassAccorpamento.setTipoMateriale(SbnMateriale.valueOf(" ").toString());
					tipoOggettoDaFondere ="Fusione tra titoli";
				}

	    		areaDatiAccorpamentoReturnVO = sbnGestioneAccorpamentoDao.richiestaAccorpamento(areaDatiPassAccorpamento);

	    		if (!areaDatiAccorpamentoReturnVO.getCodErr().equals("0000")) {
	    			messaggioIncrementale = messaggioIncrementale + areaDatiAccorpamentoReturnVO.getTestoProtocollo() + " ";

		       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");

	    			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	    					listaCoppieBidDaFondere.get(i) +
	    					"</TD><TD>" +
	    					" " + tipoOggettoDaFondere +
	    					" - " +
	    					areaDatiPassAccorpamento.getIdElementoEliminato() + " su " + areaDatiPassAccorpamento.getIdElementoAccorpante() +
	    					"</TD><TD colspan=\"2\">" +
	    					" " + messaggioIncrementale +
	    					"</TD></TR>");
	    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
	    			messaggioIncrementale = "";
	    		} else {
	    			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	    					listaCoppieBidDaFondere.get(i) +
	    					"</TD><TD>" +
	    					" " + tipoOggettoDaFondere +
	    					" - " +
	    					areaDatiPassAccorpamento.getIdElementoEliminato() + " su " + areaDatiPassAccorpamento.getIdElementoAccorpante() +
	    					"</TD><TD colspan=\"2\">" +
	    					" OK" +
	    					"</TD></TR>");
	    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
	    			messaggioIncrementale = "";
	    			contatoreFusioneOK++;
	    		}
			}
		}

		// Modifica almaviva2 11.07.2012 Mantis 5046 collaudo: modificato controllo per errore in fusione massiva
		if (ValidazioneDati.isFilled(areaDatiPass.getListaDiConfrontoSelez()) ) {

			SbnGestioneTitoliDao sbnGestioneTitoliDao = new SbnGestioneTitoliDao(indice, polo, user);
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPassaggioSchedaDocCiclicaVO;
			areaDatiPassaggioSchedaDocCiclicaVO = new AreaDatiPassaggioSchedaDocCiclicaVO();
			areaDatiPassaggioSchedaDocCiclicaVO.setCodErr("0000");
			areaDatiPassaggioSchedaDocCiclicaVO.setTestoProtocollo("");
			areaDatiPassaggioSchedaDocCiclicaVO.setIdListaSelez(Integer.parseInt(areaDatiPass.getListaDiConfrontoSelez()));
			areaDatiPassaggioSchedaDocCiclicaVO.setStatoLavorRecordSelez("2");
			areaDatiPassaggioSchedaDocCiclicaVO = sbnGestioneTitoliDao.getListaCoppieFusioneMassiva(areaDatiPassaggioSchedaDocCiclicaVO);
			if (!areaDatiPassaggioSchedaDocCiclicaVO.getCodErr().equals("0000")) {
    			messaggioIncrementale = messaggioIncrementale + areaDatiPassaggioSchedaDocCiclicaVO.getTestoProtocollo() + " ";
    			messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");
	       		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
    					"ERRORE NELLA PRODUZIONE DELLA LISTA DEGLI OGGETTI FA FONDERE" +
    					"</TD><TD>" +
    					" " + "" +
    					" - " +
    					"" + " su " + "" +
    					"</TD><TD colspan=\"2\">" +
    					" " + messaggioIncrementale +
    					"</TD></TR>");
    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
    			messaggioIncrementale = "";
			}


			contatoreDati = ValidazioneDati.size(areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere());

			for (int i = 0; i < contatoreDati; i++) {

				// Inizio Modifica 19.03.2010 - almaviva2 gestione STOP processi batch differiti (per gent. conc. almaviva5)
				try {
					BatchManager.getBatchManagerInstance().checkForInterruption(areaDatiPass.getIdBatch());
				} catch (BatchInterruptedException eBI) {
					eBI.printStackTrace();
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(eBI.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
					return areaDatiPass;
				} catch (Exception e) {
					e.printStackTrace();
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(e.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
					return areaDatiPass;
				}
				// Fine Modifica 19.03.2010 - almaviva2

				areaDatiPassAccorpamento = new AreaDatiAccorpamentoVO();
				areaDatiPassAccorpamento.setLivelloBaseDati("I");
	    		areaDatiPassAccorpamento.setChiamataAllineamento(true);

	    		// Inizio Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797 - si identifica la chiamata per fusione massiva perchè
	    		// il bid da eliminare deve essere marcato come solo locale (non condiviso)
	    		areaDatiPassAccorpamento.setChiamataFusioneMassiva(true);
	    		// Fine Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797
	    		areaDatiPassAccorpamento.setIdElementoEliminato(areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere().get(i)
	    				.toString().substring(0, 10));
				areaDatiPassAccorpamento.setIdElementoAccorpante(areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere().get(i)
	    				.toString().substring(11, 21));
				if (areaDatiPassAccorpamento.getIdElementoEliminato().substring(3,4).equals("V")) {
					areaDatiPassAccorpamento.setTipoAuthority(SbnAuthority.AU);
					tipoOggettoDaFondere ="Fusione tra autori";
				} else if (areaDatiPassAccorpamento.getIdElementoEliminato().substring(3,4).equals("M")) {
					areaDatiPassAccorpamento.setTipoAuthority(SbnAuthority.MA);
					tipoOggettoDaFondere ="Fusione tra marche";
				} else if (areaDatiPassAccorpamento.getIdElementoEliminato().substring(3,4).equals("L")) {
					areaDatiPassAccorpamento.setTipoAuthority(SbnAuthority.LU);
					tipoOggettoDaFondere ="Fusione tra luoghi";
				} else {
					// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
					// areaDatiPassAccorpamento.setTipoMateriale(SbnMateriale.VALUE_5.toString());
					areaDatiPassAccorpamento.setTipoMateriale(SbnMateriale.valueOf(" ").toString());
					tipoOggettoDaFondere ="Fusione tra titoli";
				}

	    		areaDatiAccorpamentoReturnVO = sbnGestioneAccorpamentoDao.richiestaAccorpamento(areaDatiPassAccorpamento);

	    		if (!areaDatiAccorpamentoReturnVO.getCodErr().equals("0000")) {
	    			messaggioIncrementale = messaggioIncrementale + areaDatiAccorpamentoReturnVO.getTestoProtocollo() + " ";

		       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");

	    			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	    					areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere().get(i) +
	    					"</TD><TD>" +
	    					" " + tipoOggettoDaFondere +
	    					" - " +
	    					areaDatiPassAccorpamento.getIdElementoEliminato() + " su " + areaDatiPassAccorpamento.getIdElementoAccorpante() +
	    					"</TD><TD colspan=\"2\">" +
	    					" " + messaggioIncrementale +
	    					"</TD></TR>");
	    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
	    			messaggioIncrementale = "";
	    		} else {
	    			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
	    					areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere().get(i) +
	    					"</TD><TD>" +
	    					" " + tipoOggettoDaFondere +
	    					" - " +
	    					areaDatiPassAccorpamento.getIdElementoEliminato() + " su " + areaDatiPassAccorpamento.getIdElementoAccorpante() +
	    					"</TD><TD colspan=\"2\">" +
	    					" OK" +
	    					"</TD></TR>");
	    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
	    			messaggioIncrementale = "";
	    			contatoreFusioneOK++;

	    			// Aggiornamento della tabella TbReportIndiceIdLocali con il valore TRATTATO (4)
	    			areaDatiPassaggioSchedaDocCiclicaVO.setBidDocLocale(areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere().get(i)
		    				.toString().substring(0, 10));
	    			areaDatiPassaggioSchedaDocCiclicaVO.setStatoLavorRecordNew("4");
	    			areaDatiPassaggioSchedaDocCiclicaVO.setIdArrivoFusione("");
	    			areaDatiPassaggioSchedaDocCiclicaVO.setTipoTrattamento("");
	    			areaDatiPassaggioSchedaDocCiclicaVO = sbnGestioneTitoliDao.aggiornaTbReportIndiceIdLocali(areaDatiPassaggioSchedaDocCiclicaVO);
	    			if (!areaDatiPassaggioSchedaDocCiclicaVO.getCodErr().equals("0000")) {
		    			messaggioIncrementale = messaggioIncrementale + areaDatiPassaggioSchedaDocCiclicaVO.getTestoProtocollo() + " ";

			       		messaggioIncrementale = messaggioIncrementale.replace("<br />", " ");

		    			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"top\"><TD>" +
		    					areaDatiPassaggioSchedaDocCiclicaVO.getListaCoppieBidDaFondere().get(i) +
		    					"</TD><TD>" +
		    					" " + tipoOggettoDaFondere +
		    					" - " +
		    					areaDatiPassAccorpamento.getIdElementoEliminato() + " su " + areaDatiPassAccorpamento.getIdElementoAccorpante() +
		    					"</TD><TD colspan=\"2\">" +
		    					" " + messaggioIncrementale +
		    					"</TD></TR>");
		    			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		    			messaggioIncrementale = "";
	    			}
	    		}
			}
		}

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO(
				"<TR><TD colspan=\"5\">Totale oggetti da fondere = "
						+ contatoreDati + " di cui " + contatoreFusioneOK
						+ " correttamente completate</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO(
				"<TR><TD colspan=\"5\">FINE FUNZIONE FUSIONE MASSIVA "
						+ DateUtil.getDate() + " " + DateUtil.getTime()
						+ "</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		return areaDatiPass;

	}



	public AllineaVO allineaBaseLocale(
			AllineaVO areaDatiPass) {

		SBNMarc sbnRisposta = null;
		int progressivoBlocco = 1;
		boolean continuaCiclo = true;
		int numRigheTot = 0;
		int maxRighe = 0;
		int numBlocchi = 0;


		contatoreLegami = 0;
		contatoreDati = 0;
		contatoreCancellaz = 0;
		contatoreScambioForma = 0;
		contatoreFusione = 0;
		contatoreDatiLegami = 0;
		contatoreNatura = 0;

		contatoreLegamiOK = 0;
		contatoreDatiOK = 0;
		contatoreCancellazOK = 0;
		contatoreScambioFormaOK = 0;
		contatoreFusioneOK = 0;
		contatoreDatiLegamiOK = 0;
		contatoreNaturaOK = 0;


		//====================================================================================================
		TracciatoStampaOutputAllineamentoVO outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO();

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<H3>INIZIO FUNZIONE ALLINEAMENTO DA FILE: "
				+ areaDatiPass.getIdFileAllineamenti()
				+ " "
				+ DateUtil.getDate()
				+ " "
				+ DateUtil.getTime()
				+ "</H3>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		// inizio ciclo di richiesta allineaBaseLocale

		String messaggio = "";
		String numListaDaAllineare = "";
		String tipoOggetto = "";

		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
		// del flag di avvenuto allineamento.
		areaDatiPass.setAllineamentoDaFileLocale(false);

		if (ValidazioneDati.isFilled(areaDatiPass.getIdFileAllineamenti())) {
			numListaDaAllineare = String.valueOf(areaDatiPass.getIdFileAllineamenti());
 			tipoOggetto = "COMPLETO";
		} else if (ValidazioneDati.isFilled(areaDatiPass.getIdFileLocaleAllineamenti())) {
			numListaDaAllineare = String.valueOf(areaDatiPass.getIdFileLocaleAllineamenti());
 			tipoOggetto = "COMPLETO";
 			areaDatiPass.setAllineamentoDaFileLocale(true);
		}else if (areaDatiPass.getTipoAuthorityDaAllineare().equals("")) {
 			numListaDaAllineare = String.valueOf(areaDatiPass.getNumListaDaAllineareDocumento());
 			tipoOggetto = "DOCUMENTI";
		} else if (areaDatiPass.getTipoAuthorityDaAllineare().equals("AU")) {
 			numListaDaAllineare = String.valueOf(areaDatiPass.getNumListaDaAllineareAutore());
 			tipoOggetto = "AUTORI";
		} else if (areaDatiPass.getTipoAuthorityDaAllineare().equals("MA")) {
 			numListaDaAllineare = String.valueOf(areaDatiPass.getNumListaDaAllineareMarca());
 			tipoOggetto = "MARCHE";
		} else if (areaDatiPass.getTipoAuthorityDaAllineare().equals("TU")) {
 			numListaDaAllineare = String.valueOf(areaDatiPass.getNumListaDaAllineareTitUniforme());
 			tipoOggetto = "TIT. UNIFORMI";
		} else if (areaDatiPass.getTipoAuthorityDaAllineare().equals("UM")) {
 			numListaDaAllineare = String.valueOf(areaDatiPass.getNumListaDaAllineareTitUniformeMus());
 			tipoOggetto = "TIT. UNIFORMI MUSICALI";
		}


		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
		// del flag di avvenuto allineamento.

		outputAllineamentoVO =
			new TracciatoStampaOutputAllineamentoVO("<br><br><H3>Oggetti da allineare: "
						+ tipoOggetto + " " + numListaDaAllineare
						+ "</H3>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		String dataInizioRichTrasf = "";
		String dataFineRichTrasf = "";

		while (continuaCiclo) {

			dataInizioRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();
			// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
			// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
			// del flag di avvenuto allineamento.
			if (areaDatiPass.isAllineamentoDaFileLocale()) {
				sbnRisposta = caricoOggettiDaFileLocale(numListaDaAllineare, progressivoBlocco);
			} else {
				sbnRisposta = scaricoOggettiDaIndice(numListaDaAllineare, progressivoBlocco, "BIS");
			}
			// sbnRisposta = scaricoOggettiDaIndice(numListaDaAllineare, progressivoBlocco, "BIS");

			dataFineRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();

			outputAllineamentoVO =
				new TracciatoStampaOutputAllineamentoVO("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			outputAllineamentoVO =
				new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\">" +
						"<TH WIDTH=\"15%\">Invio richiesta a Indice</TH>" +
						"<TH WIDTH=\"15%\">Arrivo risposta da Indice</TH>" +
						"<TH WIDTH=\"15%\">Tipo risposta</TH>" +
						"<TH WIDTH=\"35%\">Totale oggetti</TH>" +
						"</TR>");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			// Inizio Intervento interno almaviva2 su indicazione di POLO NAP esercizio 30.11.2010
			// se l'Indice non risponde per varie cause il controllo su Esito null si deve bloccare e tornare
			// con le aree correttamente impostate; SbnGestioneAllineamentiIndicePoloDao metodo:allineaBaseLocale
			if (sbnRisposta == null
					|| sbnRisposta.getSbnMessage() == null
					|| sbnRisposta.getSbnMessage().getSbnResponse() == null
					|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult() == null
					|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() == null) {

				// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
				// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
				// del flag di avvenuto allineamento.
				String testoDaProspettare = "";
				if (areaDatiPass.isAllineamentoDaFileLocale()) {
					testoDaProspettare = "Il file per allineamento locale " + String.valueOf(progressivoBlocco) + " non è presente. Si continua con il prossimo file";
				} else {
					testoDaProspettare = "Presenza del valore null nella risposta da Indice probabilmente dovuta a chiusura Indice";
				}
				messaggio = messaggio + testoDaProspettare;

				// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
				// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
				// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
				// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
				// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
				// ma di seguito a: Errore in richiesta:
				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
						dataInizioRichTrasf + "</TD><TD>" +	dataFineRichTrasf +
						"</TD><TD>Errore in richiesta:" + testoDaProspettare + "</TD><TD>" + " " + "</TD></TR>");
				// "</TD><TD>Errore in richiesta:Presenza del valore null nella risposta da Indice testo probabilmente dovuta a chiusura Indice</TD><TD>" + " " + "</TD></TR>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

				if (areaDatiPass.isAllineamentoDaFileLocale()) {
					if (numBlocchi > progressivoBlocco) {
						progressivoBlocco++;
					} else {
						continuaCiclo = false;
						messaggio = "";
					}
					continue;
				} else {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo(testoDaProspettare);
					return areaDatiPass;
				}

			}
			// Fine Intervento interno almaviva2 su indicazione di POLO NAP esercizio 30.11.2010



			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				messaggio = messaggio + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

				// Intervento almaviva2 BUG MANTIS 4396 (collaudo)
				// in Totale oggetti, risulta un numero altissimo che in realtà è il numero del file elaborato dall'Indice in differita.
				// Modificato in modo che nel caso di invio su file il nome file sia riportato di seguito ai due punti;
				// esempio 'Invio su file: 14486' lasciando vuoto il Totale oggetti, (non è recuperabile dalla risposta dell'Indice)
				// Anche nel caso di Protocollo di INDICE: 102 File XML inesistente,: sarà riportato non sotto Totale oggetti,
				// ma di seguito a: Errore in richiesta:
				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
						dataInizioRichTrasf + "</TD><TD>" +	dataFineRichTrasf +
						"</TD><TD>Errore in richiesta:" + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +"</TD><TD>" + " " + "</TD></TR>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiPass;
			}


			numRigheTot = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			if (maxRighe > 0) {
				numBlocchi = (int) (Math.ceil((double) numRigheTot	/ (double) maxRighe));
			}

			allineamentiDocumenti(areaDatiPass, sbnRisposta, areaDatiPass.getTipoAuthorityDaAllineare());

			if (!areaDatiPass.getCodErr().equals("") && !areaDatiPass.getCodErr().equals("0000")) {
				messaggio = messaggio + areaDatiPass.getTestoProtocollo();
			}

			if (numBlocchi > progressivoBlocco) {
				progressivoBlocco++;
			} else {
				continuaCiclo = false;
			}
		}

		// fine ciclo di richiesta allineaBaseLocale

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br>&nbsp;");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		// Inizio gestione Contatori per la tipologia di oggetto in lavorazione
//		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TABLE ALIGN=\"left\" BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");
		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");

		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\" >");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TD colspan=\"2\">Statistiche allineamento " + tipoOggetto + "</TD>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\" >" +
				"<TD WIDTH=\"15%\"><U>Tipo modifica</U></TD>" +
				"<TD WIDTH=\"15%\"><U>Inviati</U></TD>" +
				"<TD WIDTH=\"70%\"><U>Correttamente allineati</U></TD>" +
				"</TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Legami</TD><TD>" +
				contatoreLegami +
				"</TD><TD>" +
				contatoreLegamiOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Dati</TD><TD>" +
				contatoreDati +
				"</TD><TD>" +
				contatoreDatiOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Cancellazione</TD><TD>" +
				contatoreCancellaz +
				"</TD><TD>" +
				contatoreCancellazOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Scambio Forma</TD><TD>" +
				contatoreScambioForma +
				"</TD><TD>" +
				contatoreScambioFormaOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Fusione</TD><TD>" +
				contatoreFusione +
				"</TD><TD>" +
				contatoreFusioneOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Dati e Legami</TD><TD>" +
				contatoreDatiLegami +
				"</TD><TD>" +
				contatoreDatiLegamiOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Cambio Natura</TD><TD>" +
				contatoreNatura +
				"</TD><TD>" +
				contatoreNaturaOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR ALIGN=\"left\" VALIGN=\"middle\"><TD>Tipo non impostato</TD><TD>" +
				contatoreSenzaTipol +
				"</TD><TD>" +
				contatoreSenzaTipolOK +
				"</TD></TR>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</TABLE>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<br>&nbsp;");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);
		// Fine gestione Contatori per la tipologia di oggetto in lavorazione


		//====================================================================================================

		if (!messaggio.equals("")) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
			return areaDatiPass;
		}

		return areaDatiPass;
	}

	private Repertorio getRepertori() throws Exception {
		//almaviva5_20140129 #5479
		Repertorio rep = DomainEJBFactory.getInstance().getRepertorio();
		return rep;
	}

	private String prenotaAllineamentoPerIdFile(AllineaVO richiesta, String idFile) throws Exception {
		SchedulableBatchVO batch = new SchedulableBatchVO();
		batch.setCd_attivita(richiesta.getCodAttivita());
		String idBatch = richiesta.getIdBatch();
		batch.setIdBatch(idBatch);
		batch.setUser(DaoManager.getFirmaUtente(richiesta.getTicket()));
		batch.setScheduleType(ScheduleType.TIME);

		//preparazione job a un'ora di distanza
		int timeout = CommonConfiguration.getPropertyAsInteger(Configuration.SBNMARC_ALLINEAMENTO_SU_FILE_INTERVAL, 0);
		if (timeout < 1)
			return null;	//prenotazione disattivata

		Timestamp when = new Timestamp(DaoManager.now().getTime() + timeout);
		String dataOra = DateUtil.formattaDataOra(when);
		batch.setActivationExpr(dataOra);
		batch.setParams(idFile);
		String descr = String.format("Prenotazione allineamento per file id '%s' (da batch n. %s)", idFile, idBatch);
		batch.getConfig().setDs_tabella(descr);
		batch.getConfig().setCd_flg7("T");

		//generazione job_name fittizio
		String name = Integer.toString(Math.abs((richiesta.getTicket() + descr + IdGenerator.getId()).hashCode()));
		batch.setJobName(ValidazioneDati.trunc(name, 5));

		if (BatchUtil.schedule(batch) )
			BatchManager.getBatchManagerInstance().reload();

		return dataOra;
	}

}

