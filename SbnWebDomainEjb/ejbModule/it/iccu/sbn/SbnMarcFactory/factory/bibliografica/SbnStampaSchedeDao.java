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

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityCampiTitolo;
import it.iccu.sbn.batch.unimarc.SbnUnimarcBIDExtractor;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoOutput;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaSchedeVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescDatiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.BidInventarioSegnaturaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.SchedaVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.NumeriRomani;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

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
public class SbnStampaSchedeDao {


	public static final String SBNWEB_EXPORT_UNIMARC_HOME = "SBNWEB_EXPORT_UNIMARC_HOME";

	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;

	public SbnStampaSchedeDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	public AreaParametriStampaSchedeVo schedulatorePassiStampaSchede(AreaParametriStampaSchedeVo areaDatiPass, BatchLogWriter blog) {

		Logger _log = blog.getLogger();
		SbnGestioneTitoliDao sbnGestioneTitoliDao = new SbnGestioneTitoliDao(indice, polo, user);

		// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
		// operante che nel caso di centro Sistema non coincidono
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO =
			new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
//		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
		// Fine Modifica almaviva2 16.07.2010


		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO titoloAnaliticaReturnVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
		String bidAttuale="";
		boolean almenoUnTitolo = false;
		String tipoCatalogo = "";
		String tipoOrdinamento = "";
		String appoMateriali="";

		List listaBid = areaDatiPass.getListaBid() != null ? areaDatiPass.getListaBid() : ValidazioneDati.emptyList();

		List listaBidOrdinati=null;
		List listaBidOrdinatiConPoss=null;
		List listaBidOrdinatiConIntestazione=null;
		int countBidOrdinati=0;



		// Intervento su richiesta ICCU mentre compilavano Manuale Utente
		// Anche nel caso di tipoOperazione "S" si passa per l'estrattore magno e i filtri per doc fisico si estraggono li
		if (ValidazioneDati.equals(areaDatiPass.getCodAttivita(), CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)
				&& (ValidazioneDati.size(listaBid) == 0)) {
//			if (ValidazioneDati.equals(areaDatiPass.getCodAttivita(), CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)
//					&& (!ValidazioneDati.isFilled(areaDatiPass.getTipoOperazione()))
//					&& (ValidazioneDati.size(listaBid) == 0)) {

			EsportaVO richiesta = areaDatiPass.getEsportaVO();
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
			if (richiesta.getTipoCatalogo().equals("AUT")) {
				tipoCatalogo ="Autore";
				tipoOrdinamento = "Autore/Titolo";
			} else if (richiesta.getTipoCatalogo().equals("TIP")) {
				tipoCatalogo = "Editore/Tipografo";
				tipoOrdinamento = "Titolo";
			} else if (richiesta.getTipoCatalogo().equals("TIT")) {
				tipoCatalogo = "Titolo";
				tipoOrdinamento = "Titolo";
			} else if (richiesta.getTipoCatalogo().equals("POS")) {
				tipoCatalogo = "Possessore";
				tipoOrdinamento = "Possessore/Titolo";
			} else if (richiesta.getTipoCatalogo().equals("SOG")) {
				tipoCatalogo = "Soggetto";
				tipoOrdinamento = "Soggetto/Titolo";
			} else if (richiesta.getTipoCatalogo().equals("CLA")) {
				tipoCatalogo = "Classe";
				tipoOrdinamento = "Classe/Titolo";
			} else if (richiesta.getTipoCatalogo().equals("EDI")) {
				tipoCatalogo = "Produzione Editoriale";
				tipoOrdinamento = "Produzione Editoriale/Titolo";
			}

			// Maggio 2013 - Migliorativa per indicare quale soggettario (OBBLIGATORIO) è stato scelto nella stampa catalogo
			// per soggetti anche quando non sono indicati estremi di ricerca
			if (richiesta.getTipoCatalogo().equals("SOG")) {
				tipoCatalogo = tipoCatalogo + " per " + richiesta.getCodSoggettario();
			}
			if (richiesta.getCatalogoSelezDa() != null && richiesta.getCatalogoSelezDa().length()>0) {
				tipoCatalogo = tipoCatalogo + " da: " + richiesta.getCatalogoSelezDa();
				tipoCatalogo = tipoCatalogo + " a: " + richiesta.getCatalogoSelezA();
			}

			if (richiesta.getMateriali() == null) {
				appoMateriali = "Tutti i materiali";
			} else {
				int sizeMateriale = richiesta.getMateriali().length;
				String value="";
				for (int numMater = 0; numMater < sizeMateriale; numMater++) {
					value=richiesta.getMateriali()[numMater];
					if (value.equals("M")) {
						appoMateriali=appoMateriali+ "Moderno ";
					} else if (value.equals("E")) {
						appoMateriali=appoMateriali+ "Antico ";
					} else if (value.equals("C")) {
						appoMateriali=appoMateriali+ "Cartografia ";
					} else if (value.equals("G")) {
						appoMateriali=appoMateriali+ "Grafica ";
					} else if (value.equals("U")) {
						appoMateriali=appoMateriali+ "Musica ";
						// Manutenzione Febbraio 2018 - gestione nuovi materiale Elettronico e Audiovisivo ("H", "L")
					} else if (value.equals("H")) {
						appoMateriali=appoMateriali+ "Audiovisivo ";
					} else if (value.equals("L")) {
						appoMateriali=appoMateriali+ "Elettronico ";
					}
				}
			}

			// Inizio Modifica almaviva2 01.10.2012 Bug Mantis esercizio 5127 e Bug Mantis Liguria 0000052
			// Viene reinserito il filtro in automatico per Biblioteca che se mancante portava il
			// batch a scaricare il catalogo di tutto il Polo.
			List<BibliotecaVO> filtroLocBib = new ArrayList<BibliotecaVO>();
			BibliotecaVO bibVO = new BibliotecaVO();
			bibVO.setCod_polo(richiesta.getCodPolo());
			bibVO.setCod_bib(richiesta.getCodBib());
			filtroLocBib.add(bibVO);
			richiesta.setListaBib(filtroLocBib);
			// Fine Modifica almaviva2 01.10.2012 Bug Mantis esercizio 5127 e Bug Mantis Liguria 0000052

			String fileName;
			try {
				File f = File.createTempFile(areaDatiPass.getFirmaBatch() + "_bid_list_", ".tmp");
				BatchManager.getBatchManagerInstance().markForDeletion(f);
				fileName = f.getAbsolutePath();

				// Gestione Bibliografica: Intervento interno Per i Possessori si deve richiedere un tipo output (TipoOutput.BID_POSS)
				// che contenga sia il bid che l'area dell'inventario che quella della descrizione del possessore (area3 della select)
//				if (richiesta.getTipoCatalogo().equals("SOG") ||
//						richiesta.getTipoCatalogo().equals("CLA") ||
//						richiesta.getTipoCatalogo().equals("EDI") ||
//						richiesta.getTipoCatalogo().equals("POS")) {
//					richiesta.setTipoOutput(TipoOutput.BID_DATI);
//				}
				if (richiesta.getTipoCatalogo().equals("SOG") ||
						richiesta.getTipoCatalogo().equals("CLA") ||
						richiesta.getTipoCatalogo().equals("EDI")) {
					richiesta.setTipoOutput(TipoOutput.BID_DATI);
				} else if (richiesta.getTipoCatalogo().equals("POS")) {
					richiesta.setTipoOutput(TipoOutput.BID_POSS);
				}

				SbnUnimarcBIDExtractor extractor = new SbnUnimarcBIDExtractor(richiesta, blog);
				long bidCount = extractor.extract(fileName);
				_log.debug("numero bid estratti: " + bidCount);
				if (bidCount < 1) {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo
						(":ATTENZIONE: NON ESISTONO TITOLI RELATIVI ALLA RICHIESTA INVIATA - VERIFICARE I PARAMETRI DELLA RICHIESTA");
					return areaDatiPass;
				}
			} catch (Exception e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			}

			FileReader fr;
			try {
				fr = new FileReader(fileName);
				BufferedReader br = new BufferedReader(fr);
				String s;
				int pos=0;
				if (richiesta.getTipoCatalogo().equals("SOG") ||
						richiesta.getTipoCatalogo().equals("CLA") ||
						richiesta.getTipoCatalogo().equals("EDI") ||
						richiesta.getTipoCatalogo().equals("POS")) {
					listaBidOrdinatiConIntestazione = new ArrayList();
					while ((s = br.readLine()) != null) {
						pos = s.indexOf("|");
						listaBid.add(s.substring(0,pos));
						listaBidOrdinatiConIntestazione.add(s.substring(pos+1).replaceAll("\\*", ""));
					}
				} else {
					while ((s = br.readLine()) != null) {
						listaBid.add(s);
					}
				}

				areaDatiPass.setListaBid(listaBid);

			} catch (FileNotFoundException e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			}
			catch (IOException e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			}
		}


		// Modifica almaviva2 28/01/2010	BUG MANTIS 3275 (si stampa solo se ci sono segnature se
		// è stato chiesto Solo Documenti inventariati; dichiarazione e gestione nuovo campo
		boolean presenzaGestPerBidAttuale;

		TracciatoStampaSchedeVO schedaVO;
		int contatoreListaAutore = 0;
		// Modifiche varie per risoluzione BUG 3447 almaviva2 22.12.2009
		List listaBidInvSegn = new ArrayList();
		int sizeBidInvSegn = 0;
		int sizeListaBid = 0;

		if (areaDatiPass.getTipoOperazione() == null ||
				areaDatiPass.getTipoOperazione().equals("")) {

		} else {

			//================================================================================================
			// Chiamata a Documento fisico per rintracciare i BID ed i relativi Topografico/Inventario

			try {
//				listaBid =  getInventarioBean().getBid(areaDatiPass, areaDatiPass.getTipoOperazione(),areaDatiPass.getTicket());
				listaBidInvSegn =  getInventarioBean().getBid2(areaDatiPass, areaDatiPass.getTipoOperazione(),areaDatiPass.getTicket());

			} catch (DataException e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			} catch (ApplicationException e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			} catch (ValidationException e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			} catch (RemoteException e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
				return areaDatiPass;
			}
			areaDatiPass.setListaBid(new ArrayList());
			sizeBidInvSegn = listaBidInvSegn.size();
			String bidInventario = "";
			String bidBibliografico = "";

			List listaBidtemp  = new ArrayList();
			List listaBidOrdinatiConIntestazioneTemp = new ArrayList();

			if (listaBid != null && listaBid.size() > 0)  {
				sizeListaBid = listaBid.size();
			}

			if (sizeListaBid == 0 && sizeBidInvSegn > 0) {
				for (int i = 0; i < sizeBidInvSegn; i++) {
					if (((BidInventarioSegnaturaVO)listaBidInvSegn.get(i)).getCodSit().equals("2")) {
						bidInventario = ((BidInventarioSegnaturaVO)listaBidInvSegn.get(i)).getBid();
						listaBidtemp.add(bidInventario);
						listaBidOrdinatiConIntestazioneTemp.add("");
					}
				}
			} else {

				// almaviva2 - Ottobre 2016
				// Bug Mantis 6277 - stampa catalogo soggetti ordinato in modo errato: il problema viene risolto modificando la
				// select di estrazione dei titoli legati al soggettario e variando le modalità di accoppiamento fra i bid estratti
				// da questa select (di tipo bibliografico) e quelli estratti in base ai filtri di documento fisico
				// INIZIO PROVA PER CAMBIARE  IL BILANCIAMENTO FRA LE TABELLE listaBidInvSegn e listaBid A PARTIRE DA listaBid
				// E NON DA listaBidInvSegn ALTRIMENTI SI PERDE L'ORDINAMENTO NEL CASO DI STAMPA CATALOGHI PER SOGGETTI
				// ESTRATTI PER DQTI INVENTARIALI
				for (int idxListaBid = 0; idxListaBid < sizeListaBid; idxListaBid++) {
					bidBibliografico = ((String)listaBid.get(idxListaBid));
					if (sizeBidInvSegn > 0) {
						for (int idxBidInvSegn = 0; idxBidInvSegn < sizeBidInvSegn; idxBidInvSegn++) {
							if (((BidInventarioSegnaturaVO)listaBidInvSegn.get(idxBidInvSegn)).getCodSit().equals("2")) {
								bidInventario = ((BidInventarioSegnaturaVO)listaBidInvSegn.get(idxBidInvSegn)).getBid();
								if (bidInventario.equals(bidBibliografico)) {
									listaBidtemp.add(bidBibliografico);
									// Mantis esercizio 0006212, 0006249; almaviva2, inserito controllo su listaBidOrdinatiConIntestazione vuota;
									if (listaBidOrdinatiConIntestazione != null && listaBidOrdinatiConIntestazione.size() > 0)  {
										listaBidOrdinatiConIntestazioneTemp.add(listaBidOrdinatiConIntestazione.get(idxListaBid));
									} else {
										listaBidOrdinatiConIntestazioneTemp.add("");
									}
									break;
								}
							}
						}
					}
				}
				// FINE PROVA PER CAMBIARE  IL BILANCIAMENTO FRA LE TABELLE listaBidInvSegn e listaBid
//				for (int i = 0; i < sizeBidInvSegn; i++) {
//					// Intervento del 13.02.2013 almaviva2
//					// Intervento su richiesta ICCu mentre compilavano Manuale Utente
//					// per verificare se si deve stampare o no l'oggetto e le sue segnature si deve verificare che il
//					// getCodSit sia uguale a 2 (esiste quindi una sezione).
//					if (((BidInventarioSegnaturaVO)listaBidInvSegn.get(i)).getCodSit().equals("2")) {
//						bidInventario = ((BidInventarioSegnaturaVO)listaBidInvSegn.get(i)).getBid();
//						if (listaBid != null && listaBid.size() > 0)  {
//							sizeListaBid = listaBid.size();
//							for (int j = 0; j < sizeListaBid; j++) {
//								bidBibliografico = ((String)listaBid.get(j));
//								if (bidInventario.equals(bidBibliografico)) {
//									listaBidtemp.add(bidBibliografico);
//									// Mantis esercizio 0006212, 0006249; almaviva2, inserito controllo su listaBidOrdinatiConIntestazione vuota;
//									if (listaBidOrdinatiConIntestazione != null && listaBidOrdinatiConIntestazione.size() > 0)  {
//										listaBidOrdinatiConIntestazioneTemp.add(listaBidOrdinatiConIntestazione.get(j));
//									} else {
//										listaBidOrdinatiConIntestazioneTemp.add("");
//									}
//									break;
//								}
//							}
//						}
//						// areaDatiPass.addListaBid(((BidInventarioSegnaturaVO)listaBidInvSegn.get(i)).getBid());
//					}
//				}

				// FINE PROVA PER CAMBIARE  IL BILANCIAMENTO FRA LE TABELLE listaBidInvSegn e listaBid
			}

			//================================================================================================
			//listaBid = areaDatiPass.getListaBid();//almaviva2 3/9/2010
			listaBid = listaBidtemp;
			listaBidOrdinatiConIntestazione = listaBidOrdinatiConIntestazioneTemp;
			areaDatiPass.setListaBid(listaBid);
		}

		sizeListaBid = ValidazioneDati.size(listaBid);
		if (sizeListaBid == 0) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo(":ATTENZIONE: NON ESITONO TITOLI RELATIVI ALLA RICHIESTA INVIATA - VERIFICARE I PARAMETRI DELLA RICHIESTA");
			return areaDatiPass;
		}

		EsportaVO richiesta = areaDatiPass.getEsportaVO();

		// Inizio Modifica PER STAMPA CATALOGHI Ordinata in base alla richiesta Utente
		// Qui deve essere richiamata la classe per ordinare i bid in base al catalogo da stampare
		// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
		if (areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)){
			if (richiesta.getTipoCatalogo().equals("AUT") ||
								richiesta.getTipoCatalogo().equals("TIP") ||
								richiesta.getTipoCatalogo().equals("SOG") ||
								richiesta.getTipoCatalogo().equals("CLA") ||
								richiesta.getTipoCatalogo().equals("EDI") ||
								richiesta.getTipoCatalogo().equals("POS")) {
						// nel caso di stampa cataloghi per Autore/Titolo e SoggettoAutore/Titolo listaBid è stata gia prodotta in maniera ordinata
						// dal metodo di estrazione quindi il metodo estrai NON deve essere richiamato
					} else {

						try {

							SbnUnimarcBIDExtractor extractor = new SbnUnimarcBIDExtractor(richiesta, blog);
							listaBidOrdinati = extractor.ordina(listaBid);
							countBidOrdinati = listaBidOrdinati.size();

							_log.debug("numero bid ordinati: " + countBidOrdinati);
							if (countBidOrdinati < 1) {
								areaDatiPass.setCodErr("9999");
								areaDatiPass.setTestoProtocollo(":ATTENZIONE: NON ESISTONO TITOLI RELATIVI ALLA RICHIESTA INVIATA - VERIFICARE I PARAMETRI DELLA RICHIESTA");
								return areaDatiPass;
							}

							listaBid = new ArrayList<String>();
							if (richiesta.getTipoCatalogo().equals("POS")) {
								for (int i = 0; i < countBidOrdinati; i++) {
									listaBid.add(((TabellaNumSTDImpronteVO)listaBidOrdinatiConPoss.get(i)).getCampoUno());
								}
							} else {
								listaBid.addAll(listaBidOrdinati);
							}

						} catch (Exception e) {
							_log.error("", e);
							areaDatiPass.setCodErr("9999");
							areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
							return areaDatiPass;
						}
					}
		}

		// Fine Modifica PER STAMPA CATALOGHI Ordinata in base alla richiesta Utente

		int cnt = 0;
		for (int i = 0; i < sizeListaBid; i++) {

			// Inizio Modifica 19.03.2010 - almaviva2 gestione STOP processi batch differiti (per gent. conc.  almaviva5)
			try {
				if ((++cnt % 50) == 0)
					BatchManager.getBatchManagerInstance().checkForInterruption(areaDatiPass.getIdBatch());
				// Intervento interno: Inserito voce di log per identificare il punto di lavorazione del batch
				if ((cnt % 100) == 0)
					_log.debug("StampaSchede/StampaCataloghi: elaborati i primi " + cnt + " titoli") ;
			} catch (BatchInterruptedException eBI) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo
					(eBI.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
				return areaDatiPass;
			} catch (Exception e) {
				_log.error("", e);
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo
					(e.getMessage() + ":ATTENZIONE: TERMINAZIONE FORZATA DEL BATCH " + areaDatiPass.getCodAttivita() + " Id:" + areaDatiPass.getIdBatch());
				return areaDatiPass;
			}
			// Fine Modifica 19.03.2010 - almaviva2

			presenzaGestPerBidAttuale = false;

			// Passo1: creazione della scheda principale valida per tutti i cataloghi;
			schedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());

			// Modiofica per adeguamento a nuovo Client di Documento Fisico
//			bidAttuale = (String) areaDatiPass.getListaBid().get(i);

			if (areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)) {
				if (areaDatiPass.getDataDiElaborazione() == null || areaDatiPass.getDataDiElaborazione().equals("")) {
					GregorianCalendar gc = new GregorianCalendar();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
					areaDatiPass.setDataDiElaborazione(sdf.format(gc.getTime()));
				}
				schedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());

				schedaVO.setTitoloCollana(areaDatiPass.getTitoloCollana());
				schedaVO.setTitoliAnalitici(areaDatiPass.getTitoliAnalitici());
				schedaVO.setDatiCollocazione(areaDatiPass.getDatiCollocazione());
				schedaVO.setIntestTitoloAdAutore(areaDatiPass.getIntestTitoloAdAutore());

			}

			if (listaBid.get(i) instanceof BidInventarioSegnaturaVO) {
				bidAttuale = ((BidInventarioSegnaturaVO) listaBid.get(i)).getBid();
			} else if (listaBid.get(i) instanceof CodiceVO){
				bidAttuale = ((CodiceVO) listaBid.get(i)).getCodice();
			}else {
				bidAttuale = ((String) listaBid.get(i));
			}

			schedaVO.setBidEsaminato("");
			List listaSegnature = new ArrayList();

			if ((areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE))
					|| (areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)
							&& areaDatiPass.getDatiCollocazione().equals("SI"))) {

				if (richiesta.getTipoCatalogo().equals("POS")) {
					presenzaGestPerBidAttuale = true;
				} else {
					if (sizeBidInvSegn == 0) {
						// Chiamata al servizio di Documento Fisico per la predisposizione delle area relative alla Collocazione e Num. Inventario
						//			================================================================================================

						try {
							listaSegnature =  getInventarioBean().getSegnature(areaDatiPass.getCodPolo(),
									areaDatiPass.getCodBib(),bidAttuale, areaDatiPass.getTicket());

						} catch (DataException e) {
							_log.error("", e);
							if (e.getErrorCode() != SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO) {
								areaDatiPass.setCodErr("9999");
								areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
								return areaDatiPass;
							}
						} catch (ApplicationException e) {
							_log.error("", e);
							areaDatiPass.setCodErr("9999");
							areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
							return areaDatiPass;
						} catch (ValidationException e) {
							_log.error("", e);
							areaDatiPass.setCodErr("9999");
							areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
							return areaDatiPass;
						} catch (RemoteException e) {
							_log.error("", e);
							areaDatiPass.setCodErr("9999");
							areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
							return areaDatiPass;
						}
						// Modifica per nuovo output di listaSegnature = getInventarioBean().getSegnature che diventa ShedaVO
						// almaviva2 12.03.2010 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
						//					EtichettaDettaglioVO rec = null;
						SchedaVO rec = null;
						// Segnalazione interna testando MANTIS 3307 - almaviva2 Controllare il null su Segnature 10.11.2009
						int sizeSegnature = 0;
						if (listaSegnature != null) {
							sizeSegnature = listaSegnature.size();
						}

						if (sizeSegnature == 0) {
							schedaVO.addListaTopografico("");
							schedaVO.addListaNumInventario("");
						} else {
							// Intervento del 13.02.2013 almaviva2
							// Intervento su richiesta ICCu mentre compilavano Manuale Utente
							// per verificare se si deve stampare o no l'oggetto e le sue segnature si deve verificare che il
							// getCodSit sia uguale a 2 (esiste quindi una sezione).
//							presenzaGestPerBidAttuale = true;
							for (int j = 0; j < sizeSegnature; j++) {
								//	rec = (EtichettaDettaglioVO) listaSegnature.get(j);

								rec = (SchedaVO) listaSegnature.get(j);
								if (rec.getCodSit().equals("2")) {
									if (rec.getBidInventario().equals(bidAttuale)) {

										String precisazione="";
										if (rec.getPrecisazione() != null && !rec.getPrecisazione().trim().equals("")) {
											precisazione="(" + rec.getPrecisazione().trim() + ")";
										}

										// Inizio modifica almaviva2 12.03.2010 PUNTO 1 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
										// modifcata la costruzione della stringa del topografico.
										//								if (rec.getSequenza() == null || rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
										//									schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
										//								} else {
										//									schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
										//								}
										if (rec.getSequenza() != null && !rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
											schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
										} else {
											schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
										}
										schedaVO.addListaNumInventario(rec.getSerie() + " " + rec.getInventario() + " "  + precisazione);
										presenzaGestPerBidAttuale = true;
									}
								}
							}
						}

					} else {
						for (int j = 0; j < sizeBidInvSegn; j++) {
							BidInventarioSegnaturaVO rec = null;
							rec = (BidInventarioSegnaturaVO) listaBidInvSegn.get(j);
							// Maggio 2016 almaviva2 - vari interventia copertura di varie casistiche
							// Qui inseito controllo su getCodSit che non era stato effettuato con quello principale del 13.02.2013
							if (rec.getCodSit().equals("2")) {
								if (rec.getBid().equals(bidAttuale)) {

									String precisazione="";
									if (rec.getPrecisazione() != null && !rec.getPrecisazione().trim().equals("")) {
										precisazione="(" + rec.getPrecisazione().trim() + ")";
									}
									if (rec.getSequenza() != null && !rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
										schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
									}else{
										schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
									}
									//almaviva2 bug 0004916 esercizio	schedaVO.addListaTopografico(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
									schedaVO.addListaNumInventario(rec.getSerie() + " " + rec.getInventario() + " "  + precisazione);
									presenzaGestPerBidAttuale = true;//almaviva2 3/9/2010
									break;
								}
							}
						}
					}
				}

			}

			// Modifica almaviva2 28/01/2010	BUG MANTIS 3275 (si stampa solo se ci sono gestionali se
			// è stato chiesto Solo Documenti inventariati; dichiarazione e gestione nuovo campo
			if (areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE)) {
				if (!areaDatiPass.isTitoliNonPosseduti()) {
					if (!presenzaGestPerBidAttuale) {
						continue;
					}
				}
			}

			//================================================================================================
			// ............................................
			// Interrogazione del Bid sulla base dati per le informazioni di Gestione bibliografica
			titoloAnaliticaVO.setBidRicerca(bidAttuale);
			titoloAnaliticaVO.setRicercaIndice(false);
			titoloAnaliticaVO.setRicercaPolo(true);
			titoloAnaliticaVO.setInviaSoloSbnMarc(true);
			titoloAnaliticaReturnVO = sbnGestioneTitoliDao.creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
			if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
				TabellaNumSTDImpronteVO elem=null;
				if (listaBidOrdinatiConPoss != null) {
					elem = (TabellaNumSTDImpronteVO)listaBidOrdinatiConPoss.get(i);
				}
				schedaVO = datiBibliograficiScheda(areaDatiPass.getCodAttivita(),
						bidAttuale,
						schedaVO,
						titoloAnaliticaReturnVO.getSbnMarcType(),
						areaDatiPass.getTicket(),
						areaDatiPass.isTitoliNonPosseduti(),
						listaSegnature,
						elem);
				almenoUnTitolo = true;
			} else {
				schedaVO.setTopografico(bidAttuale + ": " + titoloAnaliticaReturnVO.getCodErr() + " " + titoloAnaliticaReturnVO.getTestoProtocollo());
				schedaVO.setNumInventario("");
			}

			if (areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE)) {
				if (areaDatiPass.isRichListaPerAutore()) {
					if (areaDatiPass.getTipoResponsabilitaRich().equals("TUTTI")) {

						int sizeInvent = schedaVO.getListaNumInventario().size();
						if (sizeInvent == 0) {
							schedaVO.addListaNumInventario("");
							schedaVO.addListaTopografico("");
							sizeInvent = 1;
						}
						for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {
							int size4 = schedaVO.getListaNatureD().size();
							int size5 = schedaVO.getListaLegami51().size();

							if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
								schedaVO.setTopografico("");
							} else {
								schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
							}
							if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
								schedaVO.setNumInventario("");
							} else {
								// Inizio modifica almaviva2 12.03.2010 PUNTO 2 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
								// etichetta "N.INV" all'inventario come vecchio paccoSBN
								schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
							}

							if (schedaVO.getAutoreRespons1() == null || schedaVO.getAutoreRespons1().equals("")) {
								TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());
								versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
								if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {

									// Modifica almaviva2 04.01.2011	BUG MANTIS 4029 (si stampa solo la $a e non tutta l'area
									// del titolo (SbnStampaSchedeDao-schedulatorePassiStampaSchede)
									// Nuova modifica sempre con riferimento BUG MANTIS 4029 24.01.2011 - non si stampa nulla !!!
//									versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getIsbdArea1());
//									versioneBaseSchedaVO.setIntestazionePerCatalogo(
//											utilityCampiTitolo.componiStringaRicTitEsatta(schedaVO.getIsbdArea1()));
									versioneBaseSchedaVO.setIntestazionePerCatalogo("");

									versioneBaseSchedaVO.setIntestPrimaria("");
								} else {
									versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getTitoloUniforme());
									versioneBaseSchedaVO.setIntestPrimaria("");
								}
								versioneBaseSchedaVO.setId(++contatoreListaAutore + "");
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerAutoreSchedeVO(versioneBaseSchedaVO);

								int size3 = schedaVO.getListaAutoriRespons3().size();
								for (int aut3 = 0; aut3 < size3; aut3++) {
									TracciatoStampaSchedeVO versioneAutori3SchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
									versioneAutori3SchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaAutoriRespons3().get(aut3));

									// Inizio almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
									// versioneAutori3SchedaVO.setIntestPrimaria("");
									versioneAutori3SchedaVO.setIntestPrimaria((String) schedaVO.getListaAutoriRespons3().get(aut3));
									// Fine almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato

									// Inizio almaviva2 20.09.2012 Bug Manits esercizio 5092
									// nella stampa delle schede di inventari che presentano solo un legame con una responsabilità 3
									// si deve produrre la scheda  riportante in alto a dx la collocazione e in basso a sx il numero di inventario
									if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
										versioneAutori3SchedaVO.setTopografico("");
									} else {
										versioneAutori3SchedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
									}
									if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
										versioneAutori3SchedaVO.setNumInventario("");
									} else {
										versioneAutori3SchedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
									}
									// Fine almaviva2 20.09.2012 Bug Manits esercizio 5092

									versioneAutori3SchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneAutori3SchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneAutori3SchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneAutori3SchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneAutori3SchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneAutori3SchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneAutori3SchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());
									versioneAutori3SchedaVO.setId(++contatoreListaAutore + "");
									versioneAutori3SchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneAutori3SchedaVO);
								}

								for (int natD = 0; natD < size4; natD++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaNatureD().get(natD));

									// Inizio almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
									// versioneNaturaDSchedaVO.setIntestPrimaria("");
									versioneNaturaDSchedaVO.setIntestPrimaria((String) schedaVO.getListaNatureD().get(natD));
									// Fine almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato


									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());
									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}
								for (int leg51 = 0; leg51 < size5; leg51++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
//									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaLegami51().get(leg51));
									versioneNaturaDSchedaVO.setIntestazionePerCatalogo(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());

									// Inizio almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
									// versioneNaturaDSchedaVO.setIntestPrimaria("");
									versioneNaturaDSchedaVO.setIntestPrimaria(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());
									// Fine almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato


//									Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 11
//									(11. se la richiesta di stampa di opere in più volumi è per bid la descrizione che sta nelle schede
//									di ogni singolo volume dovrebbero essere così organizzata eliminando il vedi che va sostituito
//									con FA PARTE DI e aggiungendo il n. di inventario )
//									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1(" FA PARTE DI ");

									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());

									versioneNaturaDSchedaVO.setTopografico(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiDue());
									versioneNaturaDSchedaVO.setNumInventario("N.Inv. " + ((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiUno());

									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}
							} else {
								TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
								versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
								versioneBaseSchedaVO.setIntestPrimaria(versioneBaseSchedaVO.getAutoreRespons1());

//								Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 11
//								(5. il titolo uniforme A attualmente compare accanto all’intestazione autore:
//								in questo caso, cioè in presenza di opera NON anonima, andrebbe invece inserito in nota,
//								preceduto da Tit. orig.:  perché è un titolo originale;
//								il titolo uniforme dovrebbe essere intestazione soltanto dei record senza autore principale (opere anonime)
								if (schedaVO.getTitoloUniforme() != null) {
									versioneBaseSchedaVO.setIsbdArea7(versioneBaseSchedaVO.getIsbdArea7() + "Tit. orig.:" + schedaVO.getTitoloUniforme());
								}

								versioneBaseSchedaVO.setId(++contatoreListaAutore + "");
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerAutoreSchedeVO(versioneBaseSchedaVO);

								TracciatoStampaSchedeVO versioneRichiamoSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;

								versioneRichiamoSchedaVO.setTopografico(versioneBaseSchedaVO.getTopografico());
								versioneRichiamoSchedaVO.setNumInventario(versioneBaseSchedaVO.getNumInventario());

								versioneRichiamoSchedaVO.setIntestPrimariaFacolt1("vedi");
								versioneRichiamoSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
								versioneRichiamoSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
								versioneRichiamoSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
								versioneRichiamoSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
								versioneRichiamoSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
								versioneRichiamoSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());

								int sizeListaAutoriRespons2 = schedaVO.getListaAutoriRespons2().size();
								for (int aut2 = 0; aut2 < sizeListaAutoriRespons2; aut2++) {
									TracciatoStampaSchedeVO versioneCicloSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;

									// almaviva2 11.04.2012 - BUG MANTIS 4890 (esercizio)
									// prima nota [errore nella gestione della scheda autore quando la responsabilità è 2]
									versioneCicloSchedaVO = (TracciatoStampaSchedeVO) versioneRichiamoSchedaVO.clone();

									versioneCicloSchedaVO.setIntestPrimaria((String) schedaVO.getListaAutoriRespons2().get(aut2));
									versioneCicloSchedaVO.setId(++contatoreListaAutore + "");
									versioneCicloSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneCicloSchedaVO);
								}

								int size3 = schedaVO.getListaAutoriRespons3().size();
								for (int aut3 = 0; aut3 < size3; aut3++) {
									TracciatoStampaSchedeVO versioneCicloSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
									versioneCicloSchedaVO = (TracciatoStampaSchedeVO) versioneRichiamoSchedaVO.clone();
									versioneCicloSchedaVO.setIntestPrimaria((String) schedaVO.getListaAutoriRespons3().get(aut3));
									versioneCicloSchedaVO.setId(++contatoreListaAutore + "");
									versioneCicloSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneCicloSchedaVO);
								}

								for (int natD = 0; natD < size4; natD++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;

									versioneNaturaDSchedaVO.setTopografico(versioneBaseSchedaVO.getTopografico());
									versioneNaturaDSchedaVO.setNumInventario(versioneBaseSchedaVO.getNumInventario());


									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaNatureD().get(natD));
									versioneNaturaDSchedaVO.setIntestPrimaria((String) schedaVO.getListaNatureD().get(natD));
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());
									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}

								for (int leg51 = 0; leg51 < size5; leg51++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
//									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaLegami51().get(leg51));
//									versioneNaturaDSchedaVO.setIntestPrimaria((String) schedaVO.getListaLegami51().get(leg51));
									versioneNaturaDSchedaVO.setIntestazionePerCatalogo(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());
									versioneNaturaDSchedaVO.setIntestPrimaria(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());


//									Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 11
//									(11. se la richiesta di stampa di opere in più volumi è per bid la descrizione che sta nelle schede
//									di ogni singolo volume dovrebbero essere così organizzata eliminando il vedi che va sostituito
//									con FA PARTE DI e aggiungendo il n. di inventario )
//									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1(" FA PARTE DI ");

									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());

									versioneNaturaDSchedaVO.setTopografico(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiDue());
									versioneNaturaDSchedaVO.setNumInventario("N.Inv. " + ((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiUno());

									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}
							}
						}
					} else if (areaDatiPass.getTipoResponsabilitaRich().equals("RESP1")) {

						int sizeInvent = schedaVO.getListaNumInventario().size();
						if (sizeInvent == 0) {
							schedaVO.addListaNumInventario("");
							schedaVO.addListaTopografico("");
							sizeInvent = 1;
						}
						for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {
							int size4 = schedaVO.getListaNatureD().size();
							int size5 = schedaVO.getListaLegami51().size();

							if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
								schedaVO.setTopografico("");
							} else {
								schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
							}
							if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
								schedaVO.setNumInventario("");
							} else {
								// Inizio modifica almaviva2 12.03.2010 PUNTO 2 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
								// etichetta "N.INV" all'inventario come vecchio paccoSBN
								schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
							}

							if (schedaVO.getAutoreRespons1() == null || schedaVO.getAutoreRespons1().equals("")) {
								TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
								versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
								if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {

									// Modifica almaviva2 04.01.2011	BUG MANTIS 4029 (si stampa solo la $a e non tutta l'area
									// del titolo (SbnStampaSchedeDao-schedulatorePassiStampaSchede)
									// Nuova modifica sempre con riferimento BUG MANTIS 4029 24.01.2011 - non si stampa nulla !!!
//									versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getIsbdArea1());
//									versioneBaseSchedaVO.setIntestazionePerCatalogo(
//											utilityCampiTitolo.componiStringaRicTitEsatta(schedaVO.getIsbdArea1()));
									versioneBaseSchedaVO.setIntestazionePerCatalogo("");

									versioneBaseSchedaVO.setIntestPrimaria("");
								} else {
									versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getTitoloUniforme());
									versioneBaseSchedaVO.setIntestPrimaria("");
								}
								versioneBaseSchedaVO.setId(++contatoreListaAutore + "");
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerAutoreSchedeVO(versioneBaseSchedaVO);


								// Inizio intervento almaviva2 19.04.2012 richiesta Contardi
								// se si chiede "Solo auotri principali si stampano solo RESP 1 e 2
//								int size3 = schedaVO.getListaAutoriRespons3().size();
//								for (int aut3 = 0; aut3 < size3; aut3++) {
//									TracciatoStampaSchedeVO versioneAutori3SchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
//									versioneAutori3SchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaAutoriRespons3().get(aut3));
//									// Inizio almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
//									// versioneAutori3SchedaVO.setIntestPrimaria("");
//									versioneAutori3SchedaVO.setIntestPrimaria((String) schedaVO.getListaAutoriRespons3().get(aut3));
//									// Fine almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
//									versioneAutori3SchedaVO.setIntestPrimariaFacolt1("vedi");
//									versioneAutori3SchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
//									versioneAutori3SchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
//									versioneAutori3SchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
//									versioneAutori3SchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
//									versioneAutori3SchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
//									versioneAutori3SchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());
//									versioneAutori3SchedaVO.setId(++contatoreListaAutore + "");
//									versioneAutori3SchedaVO.getStringaToPrintSchede();
//									areaDatiPass.addListaPerAutoreSchedeVO(versioneAutori3SchedaVO);
//								}
								// Fine intervento almaviva2 19.04.2012 richiesta Contardi


								for (int natD = 0; natD < size4; natD++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaNatureD().get(natD));

									// Inizio almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
									// versioneNaturaDSchedaVO.setIntestPrimaria("");
									versioneNaturaDSchedaVO.setIntestPrimaria((String) schedaVO.getListaNatureD().get(natD));
									// Fine almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato


									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());
									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}
								for (int leg51 = 0; leg51 < size5; leg51++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
//									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaLegami51().get(leg51));
									versioneNaturaDSchedaVO.setIntestazionePerCatalogo(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());

									// Inizio almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato
									// versioneNaturaDSchedaVO.setIntestPrimaria("");
									versioneNaturaDSchedaVO.setIntestPrimaria(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());
									// Fine almaviva2 29.03.2010 l'autore di resp3 deve esere messo in IntestPrimaria altrimenti non viene stampato


//									Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 11
//									(11. se la richiesta di stampa di opere in più volumi è per bid la descrizione che sta nelle schede
//									di ogni singolo volume dovrebbero essere così organizzata eliminando il vedi che va sostituito
//									con FA PARTE DI e aggiungendo il n. di inventario )
//									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1(" FA PARTE DI ");

									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());

									versioneNaturaDSchedaVO.setTopografico(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiDue());
									versioneNaturaDSchedaVO.setNumInventario("N.Inv. " + ((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiUno());

									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}
							} else {
								TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
								versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
								versioneBaseSchedaVO.setIntestPrimaria(versioneBaseSchedaVO.getAutoreRespons1());

//								Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 11
//								(5. il titolo uniforme A attualmente compare accanto all’intestazione autore:
//								in questo caso, cioè in presenza di opera NON anonima, andrebbe invece inserito in nota,
//								preceduto da Tit. orig.:  perché è un titolo originale;
//								il titolo uniforme dovrebbe essere intestazione soltanto dei record senza autore principale (opere anonime)
								if (schedaVO.getTitoloUniforme() != null) {
									versioneBaseSchedaVO.setIsbdArea7(versioneBaseSchedaVO.getIsbdArea7() + "Tit. orig.:" + schedaVO.getTitoloUniforme());
								}

								versioneBaseSchedaVO.setId(++contatoreListaAutore + "");
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerAutoreSchedeVO(versioneBaseSchedaVO);

								TracciatoStampaSchedeVO versioneRichiamoSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;

								versioneRichiamoSchedaVO.setTopografico(versioneBaseSchedaVO.getTopografico());
								versioneRichiamoSchedaVO.setNumInventario(versioneBaseSchedaVO.getNumInventario());

								versioneRichiamoSchedaVO.setIntestPrimariaFacolt1("vedi");
								versioneRichiamoSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
								versioneRichiamoSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
								versioneRichiamoSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
								versioneRichiamoSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
								versioneRichiamoSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
								versioneRichiamoSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());

								int sizeListaAutoriRespons2 = schedaVO.getListaAutoriRespons2().size();
								for (int aut2 = 0; aut2 < sizeListaAutoriRespons2; aut2++) {
									TracciatoStampaSchedeVO versioneCicloSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;

									// almaviva2 11.04.2012 - BUG MANTIS 4890 (esercizio)
									// prima nota [errore nella gestione della scheda autore quando la responsabilità è 2]
									versioneCicloSchedaVO = (TracciatoStampaSchedeVO) versioneRichiamoSchedaVO.clone();

									versioneCicloSchedaVO.setIntestPrimaria((String) schedaVO.getListaAutoriRespons2().get(aut2));
									versioneCicloSchedaVO.setId(++contatoreListaAutore + "");
									versioneCicloSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneCicloSchedaVO);
								}


								// Inizio intervento almaviva2 19.04.2012 richiesta Contardi
								// se si chiede "Solo auotri principali si stampano solo RESP 1 e 2
//								int size3 = schedaVO.getListaAutoriRespons3().size();
//								for (int aut3 = 0; aut3 < size3; aut3++) {
//									TracciatoStampaSchedeVO versioneCicloSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
//									versioneCicloSchedaVO = (TracciatoStampaSchedeVO) versioneRichiamoSchedaVO.clone();
//									versioneCicloSchedaVO.setIntestPrimaria((String) schedaVO.getListaAutoriRespons3().get(aut3));
//									versioneCicloSchedaVO.setId(++contatoreListaAutore + "");
//									versioneCicloSchedaVO.getStringaToPrintSchede();
//									areaDatiPass.addListaPerAutoreSchedeVO(versioneCicloSchedaVO);
//								}
								// Fine intervento almaviva2 19.04.2012 richiesta Contardi

								for (int natD = 0; natD < size4; natD++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;

									versioneNaturaDSchedaVO.setTopografico(versioneBaseSchedaVO.getTopografico());
									versioneNaturaDSchedaVO.setNumInventario(versioneBaseSchedaVO.getNumInventario());


									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaNatureD().get(natD));
									versioneNaturaDSchedaVO.setIntestPrimaria((String) schedaVO.getListaNatureD().get(natD));
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());
									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}

								for (int leg51 = 0; leg51 < size5; leg51++) {
									TracciatoStampaSchedeVO versioneNaturaDSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
//									versioneNaturaDSchedaVO.setIntestazionePerCatalogo((String) schedaVO.getListaLegami51().get(leg51));
//									versioneNaturaDSchedaVO.setIntestPrimaria((String) schedaVO.getListaLegami51().get(leg51));
									versioneNaturaDSchedaVO.setIntestazionePerCatalogo(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());
									versioneNaturaDSchedaVO.setIntestPrimaria(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDescrizione());


//									Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 11
//									(11. se la richiesta di stampa di opere in più volumi è per bid la descrizione che sta nelle schede
//									di ogni singolo volume dovrebbero essere così organizzata eliminando il vedi che va sostituito
//									con FA PARTE DI e aggiungendo il n. di inventario )
//									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1("vedi");
									versioneNaturaDSchedaVO.setIntestPrimariaFacolt1(" FA PARTE DI ");

									versioneNaturaDSchedaVO.setIntestPrimariaFacolt2(versioneBaseSchedaVO.getAutoreRespons1());
									versioneNaturaDSchedaVO.setIsbdArea1(versioneBaseSchedaVO.getIsbdArea1());
									versioneNaturaDSchedaVO.setIsbdArea2(versioneBaseSchedaVO.getIsbdArea2());
									versioneNaturaDSchedaVO.setIsbdArea3(versioneBaseSchedaVO.getIsbdArea3());
									versioneNaturaDSchedaVO.setIsbdArea4(versioneBaseSchedaVO.getIsbdArea4());
									versioneNaturaDSchedaVO.setIsbdArea5(versioneBaseSchedaVO.getIsbdArea5());

									versioneNaturaDSchedaVO.setTopografico(((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiDue());
									versioneNaturaDSchedaVO.setNumInventario("N.Inv. " + ((ComboCodDescDatiVO) schedaVO.getListaLegami51().get(leg51)).getDatiUno());

									versioneNaturaDSchedaVO.setId(++contatoreListaAutore + "");
									versioneNaturaDSchedaVO.getStringaToPrintSchede();
									areaDatiPass.addListaPerAutoreSchedeVO(versioneNaturaDSchedaVO);
								}
							}
						}
					} else if (areaDatiPass.getTipoResponsabilitaRich().equals("RESP2")) {
					}
				}

				// Inizio intervento MAggio 2013 - Attivazione della linea Stampa schede x editore
				if (areaDatiPass.isRichListaPerEditore()) {
					for (int edi = 0; edi < schedaVO.getListaAutoriRespons4().size(); edi++) {

						int sizeInvent = schedaVO.getListaNumInventario().size();
						for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {

							if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
								schedaVO.setTopografico("");
							} else {
								schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
							}
							if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
								schedaVO.setNumInventario("");
							} else {
								schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
							}

							TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
							versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
							String editoreAttuale = (String) schedaVO.getListaAutoriRespons4().get(edi);
							versioneBaseSchedaVO.setIntestazionePerCatalogo(editoreAttuale);

							if (editoreAttuale == null || editoreAttuale.equals("")) {
								if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
									versioneBaseSchedaVO.setIntestPrimaria("");
								} else {
									versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getTitoloUniforme());
								}
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerEditoreSchedeVO(versioneBaseSchedaVO);
							} else {
								if (schedaVO.getAutoreRespons1() != null) {
									versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getAutoreRespons1());
								}
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerEditoreSchedeVO(versioneBaseSchedaVO);
							}
						}
					}
				}

				if (areaDatiPass.isRichListaPerTitolo()) {

					// Inizio modifica almaviva2 12.03.2010 PUNTO 7 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
					// ciclicità per inventario per stampa catalogo dei titolo
					int sizeInvent = schedaVO.getListaNumInventario().size();
					for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {

						if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
							schedaVO.setTopografico("");
						} else {
							schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
						}
						if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
							schedaVO.setNumInventario("");
						} else {
							// Inizio modifica almaviva2 12.03.2010 PUNTO 2 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
							// etichetta "N.INV" all'inventario come vecchio paccoSBN
							schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
						}

						TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
						versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
						versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getIsbdArea1());
						if (schedaVO.getAutoreRespons1() == null || schedaVO.getAutoreRespons1().equals("")) {
							if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
								versioneBaseSchedaVO.setIntestPrimaria("");
							} else {
								versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getTitoloUniforme());
							}
							versioneBaseSchedaVO.getStringaToPrintSchede();
							areaDatiPass.addListaPerTitoloSchedeVO(versioneBaseSchedaVO);
						} else {
							versioneBaseSchedaVO.setIntestPrimaria(versioneBaseSchedaVO.getAutoreRespons1());
							versioneBaseSchedaVO.getStringaToPrintSchede();
							areaDatiPass.addListaPerTitoloSchedeVO(versioneBaseSchedaVO);
						}
					}
				}

				if (areaDatiPass.isRichListaPerSoggetto()) {
					for (int sog = 0; sog < schedaVO.getListaSoggetti().size(); sog++) {

						// Inizio modifica almaviva2 12.03.2010 PUNTO 5 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
						// ciclicità per inventario per stampa catalogo dei soggetti
						int sizeInvent = schedaVO.getListaNumInventario().size();
						for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {

							if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
								schedaVO.setTopografico("");
							} else {
								schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
							}
							if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
								schedaVO.setNumInventario("");
							} else {
								// Inizio modifica almaviva2 12.03.2010 PUNTO 2 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
								// etichetta "N.INV" all'inventario come vecchio paccoSBN
								schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
							}

							TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
							versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
							// Inizio modifica almaviva2 12.03.2010 PUNTO 8 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
							String soggettoAttuale = (String) schedaVO.getListaSoggetti().get(sog);
							versioneBaseSchedaVO.setIntestazionePerCatalogo(soggettoAttuale);

							if (soggettoAttuale == null || soggettoAttuale.equals("")) {
								if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
									versioneBaseSchedaVO.setIntestPrimaria("");
								} else {
									versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getTitoloUniforme());
								}
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerSoggettoSchedeVO(versioneBaseSchedaVO);
							} else {
								// Inizio modifica almaviva2 2011.12.08 Bug Mantis 3889
								// nella seconda riga deve comunque comparire l'autore (se valorizzato) dopo il soggettola classificazione
//								versioneBaseSchedaVO.setIntestPrimaria(soggettoAttuale);
								if (schedaVO.getAutoreRespons1() != null) {
									versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getAutoreRespons1());
								}
								// Fine modifica almaviva2 2010.11.03 Bug Mantis 3889

								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerSoggettoSchedeVO(versioneBaseSchedaVO);
							}
						}
//						if (schedaVO.getAutoreRespons1() == null || schedaVO.getAutoreRespons1().equals("")) {
//							if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
//								versioneBaseSchedaVO.setIntestPrimaria("");
//							} else {
//								versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getTitoloUniforme());
//							}
//							versioneBaseSchedaVO.getStringaToPrintSchede();
//							areaDatiPass.addListaPerSoggettoSchedeVO(versioneBaseSchedaVO);
//						} else {
//							versioneBaseSchedaVO.setIntestPrimaria(versioneBaseSchedaVO.getAutoreRespons1());
//							versioneBaseSchedaVO.getStringaToPrintSchede();
//							areaDatiPass.addListaPerSoggettoSchedeVO(versioneBaseSchedaVO);
//						}
					}
				}

				if (areaDatiPass.isRichListaPerClassificazione()) {
					for (int cla = 0; cla < schedaVO.getListaClassi().size(); cla++) {

						// Inizio modifica almaviva2 08.02.2011 PUNTO 5 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
						// ciclicità per inventario per stampa catalogo dei classi
						int sizeInvent = schedaVO.getListaNumInventario().size();
						for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {
							if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() == 0) {
								schedaVO.setTopografico("");
							} else {
								schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));
							}
							if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
								schedaVO.setNumInventario("");
							} else {
								// Inizio modifica almaviva2 12.03.2010 PUNTO 2 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
								// etichetta "N.INV" all'inventario come vecchio paccoSBN
								schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
							}

							TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
							versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
							// Inizio modifica almaviva2 28.10.2010 BUG Mantis 3889  stampa schede per classificazione: aggiustamenti
							String classifAttuale = (String) schedaVO.getListaClassi().get(cla);
							versioneBaseSchedaVO.setIntestazionePerCatalogo(classifAttuale);

							if (classifAttuale == null || classifAttuale.equals("")) {
								if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
									versioneBaseSchedaVO.setIntestPrimaria("");
								} else {
									versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getTitoloUniforme());
								}
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerClassificazioneSchedeVO(versioneBaseSchedaVO);
							} else {

								// Inizio modifica almaviva2 2010.11.03 Bug Mantis 3889
								// nella seconda riga deve comunque comparire l'autore (se valorizzato) dopo la classificazione
//								versioneBaseSchedaVO.setIntestPrimaria(classifAttuale);
								if (schedaVO.getAutoreRespons1() != null) {
									versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getAutoreRespons1());
								}
								// Fine modifica almaviva2 2010.11.03 Bug Mantis 3889

								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerClassificazioneSchedeVO(versioneBaseSchedaVO);
							}

						}

//						if (schedaVO.getAutoreRespons1() == null || schedaVO.getAutoreRespons1().equals("")) {
//							if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
//								versioneBaseSchedaVO.setIntestPrimaria("");
//							} else {
//								versioneBaseSchedaVO.setIntestPrimaria(schedaVO.getTitoloUniforme());
//							}
//							versioneBaseSchedaVO.getStringaToPrintSchede();
//							areaDatiPass.addListaPerClassificazioneSchedeVO(versioneBaseSchedaVO);
//						} else {
//							versioneBaseSchedaVO.setIntestPrimaria(versioneBaseSchedaVO.getAutoreRespons1());
//							versioneBaseSchedaVO.getStringaToPrintSchede();
//							areaDatiPass.addListaPerClassificazioneSchedeVO(versioneBaseSchedaVO);
//						}
					}
				}

				if (areaDatiPass.isRichListaPerTopografico()) {
					int sizeInvent = schedaVO.getListaNumInventario().size();
					for (int numInvent = 0; numInvent < sizeInvent; numInvent++) {

						if (((String) schedaVO.getListaTopografico().get(numInvent)).trim().length() != 0) {
							// Modifica almaviva2 BUG MANTIS 3836 - nota Contardi
							// (Nel topografico non vanno prodotte le schede di documenti non collocati) sposto tutta l'elaborazione
							// dentro l'if del topografico che viene quindi invertito (SbnStampaSchedeDao-schedulatorePassiStampaSchede);

							schedaVO.setTopografico((String) schedaVO.getListaTopografico().get(numInvent));

							if (((String) schedaVO.getListaNumInventario().get(numInvent)).trim().length() == 0) {
								schedaVO.setNumInventario("");
							} else {
								// Inizio modifica almaviva2 12.03.2010 PUNTO 2 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
								// etichetta "N.INV" all'inventario come vecchio paccoSBN
								schedaVO.setNumInventario("N.Inv. " + (String) schedaVO.getListaNumInventario().get(numInvent));
							}
							if (schedaVO.getAutoreRespons1() == null || schedaVO.getAutoreRespons1().equals("")) {
								TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
								versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
								if (schedaVO.getTitoloUniforme() == null || schedaVO.getTitoloUniforme().equals("")) {
									versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getIsbdArea1());
									versioneBaseSchedaVO.setIntestPrimaria("");
								} else {
									versioneBaseSchedaVO.setIntestazionePerCatalogo(schedaVO.getTitoloUniforme());
									versioneBaseSchedaVO.setIntestPrimaria("");
								}
								versioneBaseSchedaVO.setId(++contatoreListaAutore + "");
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerTopograficoSchedeVO(versioneBaseSchedaVO);
							} else {
								TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
								versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();
								versioneBaseSchedaVO.setIntestPrimaria(versioneBaseSchedaVO.getAutoreRespons1());
								versioneBaseSchedaVO.setId(++contatoreListaAutore + "");
								versioneBaseSchedaVO.getStringaToPrintSchede();
								areaDatiPass.addListaPerTopograficoSchedeVO(versioneBaseSchedaVO);
							}
						}
					}
				}

				if (areaDatiPass.isRichListaPerPossessore()) {
//					........
				}
			} else if (areaDatiPass.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)) {

				TracciatoStampaSchedeVO versioneBaseSchedaVO = new TracciatoStampaSchedeVO(areaDatiPass.getDataDiElaborazione(), areaDatiPass.getDescrizioneBiblioteca());;
				versioneBaseSchedaVO = (TracciatoStampaSchedeVO) schedaVO.clone();

				// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
				// Ulteriori segnalazioni mail contardi07.11.2012 (Il numero Bid e la natura del titolo andrebbero  aboliti)
//				String intestPerTitolo = "  N.BID:  " + bidAttuale + "    " + descNatura;
//				String descNatura = "";
//				try {
//					descNatura = CodiciProvider.cercaDescrizioneCodice(
//							schedaVO.getNaturaBidEsaminato(),
//							CodiciType.CODICE_NATURA_BIBLIOGRAFICA,
//							CodiciRicercaType.RICERCA_CODICE_UNIMARC);
//				} catch (Exception e) {
//					_log.error("", e);
//				}

//				String intestPerTitolo = "";
//				versioneBaseSchedaVO.setIntestazionePerCatalogo(intestPerTitolo);
//				versioneBaseSchedaVO.setIntestPrimaria(intestPerTitolo);

				versioneBaseSchedaVO.setTipoCatalogoDesc(richiesta.getTipoCatalogo());
				if (ValidazioneDati.in(richiesta.getTipoCatalogo(), "SOG", "CLA", "EDI")) {
					// Maggio 2013 - manutenzione temporanea per non mandare in errore la Stampa Catalogo x produzione editoriale
					// quando viene richiesta x lista bid e non da canali/filtri; in questo caso non esiste la tabella
					// listaBidOrdinatiConIntestazione e la ricerca dell'editore è pesante .... si farà!!!!
					if (listaBidOrdinatiConIntestazione != null) {
						versioneBaseSchedaVO.setIntestazionePerCatalogo((String)listaBidOrdinatiConIntestazione.get(i));
					} else {
						versioneBaseSchedaVO.setIntestazionePerCatalogo("");
					}
				} else if (richiesta.getTipoCatalogo().equals("POS")) {
					int inizioIntest = ((String)listaBidOrdinatiConIntestazione.get(i)).indexOf("<");
					//almaviva5_20140613
					int fineIntest = ValidazioneDati.max(((String)listaBidOrdinatiConIntestazione.get(i)).indexOf(">"), inizioIntest + 1);
					String intestPerCatalogo = ((String)listaBidOrdinatiConIntestazione.get(i)).substring(inizioIntest + 1, fineIntest);
					versioneBaseSchedaVO.setIntestazionePerCatalogo(intestPerCatalogo);
					versioneBaseSchedaVO.setNumInventario((String)listaBidOrdinatiConIntestazione.get(i));
				} else if (richiesta.getTipoCatalogo().equals("TIP")) {
					// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
					versioneBaseSchedaVO.setIntestazionePerCatalogo((String)versioneBaseSchedaVO.getListaAutoriRespons4().get(0));
				} else {
					versioneBaseSchedaVO.setIntestazionePerCatalogo("");
				}
				versioneBaseSchedaVO.setIntestPrimaria("RIGA IntestPrimaria");

				versioneBaseSchedaVO.getStringaToPrintCataloghi();

				versioneBaseSchedaVO.setTipoCatalogoDesc(tipoCatalogo);
				versioneBaseSchedaVO.setTipoOrdinamento(tipoOrdinamento);
				versioneBaseSchedaVO.setTipoMaterialeDescr(appoMateriali);
				versioneBaseSchedaVO.setNumTitoliTot(String.valueOf(sizeListaBid));
				areaDatiPass.addListaPerTitoloSchedeVO(versioneBaseSchedaVO);
			}
		}
		// BUG MANTIS 3275 errore se non si elabora almeno un record - almaviva2 10.02.2010
		if (!almenoUnTitolo) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo(":ATTENZIONE: NON ESITONO TITOLI RELATIVI ALLA RICHIESTA INVIATA - VERIFICARE I PARAMETRI DELLA RICHIESTA");
			return areaDatiPass;
		}

		return areaDatiPass;
	}



	public TracciatoStampaSchedeVO datiBibliograficiScheda(String codAttivita,
			String bidAttuale,
			TracciatoStampaSchedeVO schedaVO,
			SBNMarc sbnMarcType,
			String ticket,
			boolean titoliNonPosseduti,
			List listaSegnature,
			TabellaNumSTDImpronteVO elem) {

		String area7 = "";
		String area7conBid = "";

		UtilityCampiTitolo utilityCampiTitolo = new  UtilityCampiTitolo(bidAttuale, bidAttuale, sbnMarcType);
		UtilityCastor utilityCastor = new UtilityCastor();
		DatiDocType datiDocType = null;

		DocumentoType documentoType = utilityCastor.getElementoDocumento(bidAttuale, sbnMarcType);
		if (documentoType == null) {
			return schedaVO;
		}
		datiDocType = documentoType.getDocumentoTypeChoice().getDatiDocumento();

		// area1: titolo/responsabilità
		// area2: edizione
		// area3: area specifica del materiale
		// area4: pubblicazione
		// area5: collazione (descrizione fisica)
		// area6: isbd collana se presente nei legami del bid richiesto
		// area6Bis: numero sequenza collana (vedi sopra area6)
		// area7: area delle note (ed eventualmente isbd del titolo di natura B se legato)
		// area7Uniforme: serve per salvare il titolo uniforme da inserire solo nel caso in cui l'Autore sia presente
		// area8: numero Standard (issn o isbn)

//		schedaVO.setIsbdArea1(utilityCampiTitolo.getTitoloResponsabilita(datiDocType));

		String appoTitoloOrigNew = utilityCampiTitolo.componiTitSenzaAsterisco(utilityCampiTitolo.getTitoloResponsabilita(datiDocType));

		if (datiDocType.getCondiviso() != null && datiDocType.getCondiviso().toString().equals("n")) {
			schedaVO.setOggettoCondiviso("[Loc]");
		}

		schedaVO.setIsbdArea1(appoTitoloOrigNew);
		schedaVO.setIsbdArea2(utilityCampiTitolo.getAreaEdizioneCompleta(bidAttuale, sbnMarcType));
		schedaVO.setNaturaBidEsaminato(datiDocType.getNaturaDoc().toString());
		schedaVO.setTipoMateriale(datiDocType.getTipoMateriale().toString());

        String myTipoRecordDesc = "";
        if (datiDocType.getGuida().getTipoRecord() != null) {
    		try {
    			myTipoRecordDesc = CodiciProvider.cercaDescrizioneCodice(datiDocType.getGuida().getTipoRecord().toString(),
    			        		CodiciType.CODICE_GENERE_MATERIALE_UNIMARC,	CodiciRicercaType.RICERCA_CODICE_UNIMARC);
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
        }

        if (myTipoRecordDesc == null) {
        	schedaVO.setTipoRecBidEsaminato("");
        } else {
        	schedaVO.setTipoRecBidEsaminato(myTipoRecordDesc);
        }

		if (datiDocType.getT101() != null) {
			for (int i=0; i < datiDocType.getT101().getA_101().length; i++) {
				schedaVO.setLinguaBidEsaminato(datiDocType.getT101().getA_101()[0].toString());
			}
		}

		if (datiDocType.getT102() != null) {
			if (datiDocType.getT102().getA_102() != null) {
				schedaVO.setPaeseBidEsaminato(datiDocType.getT102().getA_102().toString());
			}
		}

		String area3 = "";
		if (utilityCampiTitolo.getAreaDatiMatematici() != null) {
			area3 = area3 + utilityCampiTitolo.getAreaDatiMatematici();
		}
		if (utilityCampiTitolo.getAreaMusica() != null) {
			area3 = area3 + utilityCampiTitolo.getAreaMusica();
		}
		String appoggioArea3 = null;
		appoggioArea3 = utilityCampiTitolo.getAreaNumerazioneCompleta(bidAttuale, sbnMarcType);
		if (appoggioArea3 != null) {
			area3 = area3 + appoggioArea3;
		}

		schedaVO.setIsbdArea4(utilityCampiTitolo.getAreaPubblicazioneCompleta(bidAttuale, sbnMarcType));
		schedaVO.setIsbdArea5(utilityCampiTitolo.getAreaDescrizioneFisicaCompleta(bidAttuale, sbnMarcType));

		schedaVO.setIsbdArea6Bis("");

		if (!utilityCampiTitolo.getAreaNoteCompleta(bidAttuale, sbnMarcType).equals("")) {
			area7 = utilityCampiTitolo.getAreaNoteCompleta(bidAttuale, sbnMarcType) + " ";
			area7conBid = utilityCampiTitolo.getAreaNoteCompleta(bidAttuale, sbnMarcType) + " ";
		}

		if (datiDocType.getNumSTDCount() > 0) {
			String descNumStandard = "";
			NumStdType numStdType;
			for (int i = 0; i < datiDocType.getNumSTD().length; i++) {
				numStdType = new NumStdType();
				numStdType = datiDocType.getNumSTD(i);

				// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
				// l'ISBN accettato deve essere solo il tipo I e J
				if (numStdType.getTipoSTD().toString().equals("010") || numStdType.getTipoSTD().toString().equals("011")) {
					try {
						descNumStandard = CodiciProvider.getDescrizioneCodiceUNIMARC(
								CodiciType.CODICE_TIPO_NUMERO_STANDARD, numStdType
										.getTipoSTD().toString());
					} catch (Exception e) {
						descNumStandard="";
					}
					// Inizio modifica almaviva2 12.03.2010 PUNTO 3 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
//					schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + descNumStandard.toLowerCase() + " " + numStdType.getNumeroSTD() + " ");
					schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + descNumStandard + " " + numStdType.getNumeroSTD() + " ");
					// Fine modifica almaviva2 12.03.2010 PUNTO 3

					// 6.11.2009 almaviva2 BUG 3307 Punto 2 inserito controllo su NotaNumStandard vuota
					if (numStdType.getNotaSTD() != null && !numStdType.getNotaSTD().trim().equals("")) {
						schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + "(" + numStdType.getNotaSTD() + ") ");
					}

//					// numero issn (J = 011 periodici), numero isbn  (I = 010 monografie), altri
//					if (numStdType.getTipoSTD().toString().equals("011")) {
//						schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + " ISSN: " + numStdType.getNumeroSTD());
//						// 6.11.2009 almaviva2 BUG 3307 Punto 2 inserito controllo su NotaNumStandard vuota
//						if (numStdType.getNotaSTD() != null && !numStdType.getNotaSTD().trim().equals("")) {
//							schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + "(" + numStdType.getNotaSTD() + ")");
//						}
//					} else if (numStdType.getTipoSTD().toString().equals("010")) {
//						schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + " ISBN: " + numStdType.getNumeroSTD());
//						// 6.11.2009 almaviva2 BUG 3307 Punto 2 inserito controllo su NotaNumStandard vuota
//						if (numStdType.getNotaSTD() != null && !numStdType.getNotaSTD().trim().equals("")) {
//							schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + "(" + numStdType.getNotaSTD() + ")");
//						}
//					} else {
//						try {
//							String descNumStandard = "";
//							descNumStandard = codici.getDescrizioneCodice(CodiciType.CODICE_TIPO_NUMERO_STANDARD,numStdType.getTipoSTD().toString());
//							descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,numStdType.getTipoSTD().toString());
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//
//						schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + " ALTRO " + numStdType.getTipoSTD().toString() + ": " + numStdType.getNumeroSTD());
//						// 6.11.2009 almaviva2 BUG 3307 Punto 2 inserito controllo su NotaNumStandard vuota
//						if (numStdType.getNotaSTD() != null && !numStdType.getNotaSTD().trim().equals("")) {
//							schedaVO.setIsbdArea8(schedaVO.getIsbdArea8() + "(" + numStdType.getNotaSTD() + ")");
//						}
//					}

				}
			}
		} else {
			schedaVO.setIsbdArea8("");
		}

		String autorePrincipale = "";
		int contatoreIntestSecond = 0;
		String intestSecondaria = "";
		int contatoreSoggetti = 0;
		String elencoSoggetti = "";
		String elencoClassi = "";
//		String legami51 = "";

		// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - PUNTO 11
		// aggiungere il numero di inventario in coda alla descrizione degli inferiori; per ottenerlo
		// aggiunto creato nuovo oggetto (ComboCodDescDatiVO) che oltre al codice /descrizione contiene anche una parte dati
		ComboCodDescDatiVO comboLegami51 = new ComboCodDescDatiVO("", "", "", "");
		String elencoCollane = "";
		String elencoCollaneConBid = "";

		// Scansione dei legami del bid per valorizzazione aree relative;
		if (documentoType.getLegamiDocumentoCount() > 0) {
			LegamiType legamiType;
			for (int i = 0; i < documentoType.getLegamiDocumento().length; i++) {
				legamiType = documentoType.getLegamiDocumento(i);
				for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {
					ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
					LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
					LegameDocType legameDocType = arrivoLegame.getLegameDoc();
					LegameTitAccessoType legameTitAccessoType = arrivoLegame.getLegameTitAccesso();

					if (legameElemento != null) {
						if (legameElemento.getElementoAutLegato() != null) {
							if (legameElemento.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().getType() == SbnAuthority.AU_TYPE){
								if (legameElemento.getTipoRespons().getType() == SbnRespons.VALUE_1_TYPE) {
									schedaVO.setVidAutoreRespons1(legameElemento.getElementoAutLegato().getDatiElementoAut().getT001());
									schedaVO.setAutoreRespons1(utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()));
									autorePrincipale = utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()) + " ";
									// Inizio Modifica in test DATI CATALOGO AUTORI PER STAMPA CATALOGHI
									if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)){
										contatoreIntestSecond++;
										intestSecondaria = intestSecondaria
											+ NumeriRomani.converti(contatoreIntestSecond) + ". "
											+ utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()) + " ";
									}
									// Fine Modifica in test DATI CATALOGO AUTORI PER STAMPA CATALOGHI
								} else if (legameElemento.getTipoRespons().getType() == SbnRespons.VALUE_2_TYPE) {
									schedaVO.addListaAutoriRespons2(utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()));
									contatoreIntestSecond++;
									intestSecondaria = intestSecondaria
										+ NumeriRomani.converti(contatoreIntestSecond) + ". "
										+ utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()) + " ";
								}  else if (legameElemento.getTipoRespons().getType() == SbnRespons.VALUE_3_TYPE) {
									schedaVO.addListaAutoriRespons3(utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()));
									contatoreIntestSecond++;
									intestSecondaria = intestSecondaria
										+ NumeriRomani.converti(contatoreIntestSecond) + ". "
										+ utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()) + " ";
								}  else if (legameElemento.getTipoRespons().getType() == SbnRespons.VALUE_4_TYPE) {
									// almaviva2 Intervento interno MAGGIO 2013 - per consentire la stampa per editore delle schede
									schedaVO.addListaAutoriRespons4(utilityCastor.getNominativoDatiElemento(legameElemento.getElementoAutLegato().getDatiElementoAut()));
								}
							} else if (legameElemento.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
								contatoreSoggetti++;
								SoggettoType soggettoType = (SoggettoType) legameElemento.getElementoAutLegato().getDatiElementoAut();
								// Inizio modifica almaviva2 12.03.2010 PUNTO 6 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
								String stringaSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);
//									sogg.setTesto(stringaSoggetto);
//									elencoSoggetti = elencoSoggetti + contatoreSoggetti + ". " + a250.getA_250() + " ";
								elencoSoggetti = elencoSoggetti + contatoreSoggetti + ". " + stringaSoggetto + " ";
//									schedaVO.addListaSoggetti(a250.getA_250());
								schedaVO.addListaSoggetti(stringaSoggetto);
								// Fine modifica almaviva2 12.03.2010
 							} else if (legameElemento.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
 								ClasseType classeType = (ClasseType) legameElemento.getElementoAutLegato().getDatiElementoAut();
 								if (legameElemento.getTipoLegame().toString().equals("676")) {
 									elencoClassi = classeType.getClasseTypeChoice().getT676().getA_676().trim() + " ";
									// 6.11.2009 almaviva2 BUG 3307 Punto 4 La notazione dewey non deve includere l'indicazione dell'edizione Dewey di riferimento.
// 									elencoClassi = elencoClassi + "(ed. " + classeType.getClasseTypeChoice().getT676().getV_676() + ")";
// 									elencoClassi = elencoClassi + " - " + classeType.getClasseTypeChoice().getT676().getC_676();
 									elencoClassi = elencoClassi + classeType.getClasseTypeChoice().getT676().getC_676();
 									elencoClassi = elencoClassi + " ";
 								} else {
 									elencoClassi = classeType.getT001() + " " + classeType.getClasseTypeChoice().getT686().getC_686() + " ";
 								}
 								schedaVO.addListaClassi(elencoClassi);
 							} else if (legameElemento.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
 								TitoloUniformeType titoloUniformeType = (TitoloUniformeType) legameElemento.getElementoAutLegato().getDatiElementoAut();

 								// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - intervento interno
 								// punto 2. l’asterisco nei titoli sviluppati, paralleli e uniformi non deve essere visualizzato
 								schedaVO.setTitoloUniforme((utilityCastor.getNominativoDatiElemento(titoloUniformeType)).replace("*", ""));

 								// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - intervento interno
 								// Siamo nel caso dei legami su documenti legati quindi non si deve modificere autore principale
// 								autorePrincipale =  autorePrincipale + utilityCastor.getNominativoDatiElemento(titoloUniformeType) + " ";


 							} else if (legameElemento.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
 								TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) legameElemento.getElementoAutLegato().getDatiElementoAut();
 								schedaVO.setTitoloUniforme(schedaVO.getTitoloUniforme() + utilityCastor.getNominativoDatiElemento(titoloUniformeMusicaType).replace("*", ""));
 								// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - intervento interno
 								// Siamo nel caso dei legami su documenti legati quindi non si deve modificere autore principale
// 								autorePrincipale =  autorePrincipale + utilityCastor.getNominativoDatiElemento(titoloUniformeMusicaType);

 							}
						}
					} else if (legameTitAccessoType != null) {
						if (legameTitAccessoType.getTitAccessoLegato() != null) {
							if (legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso() != null) {
								if (legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
										SbnNaturaTitAccesso.B_TYPE) {
									UtilityCampiTitolo utilityCampiTitoloAccesso = new  UtilityCampiTitolo(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
											bidAttuale,
											sbnMarcType);

									appoTitoloOrigNew = utilityCampiTitolo.componiTitSenzaAsterisco(utilityCampiTitoloAccesso.getAreaTitoloCompleto(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
											sbnMarcType,
											true));
									String appoggio = appoTitoloOrigNew;

									area7 = area7 + "Tit. orig.:" + appoggio + " ";
									area7conBid = area7conBid + "Tit. orig.:"
											+ legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001()
											+ " " + appoggio + " ";
								} else if (legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
										SbnNaturaTitAccesso.D_TYPE) {
									UtilityCampiTitolo utilityCampiTitoloAccesso = new  UtilityCampiTitolo(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
											bidAttuale,
											sbnMarcType);
									String appoggio = utilityCampiTitoloAccesso.getAreaTitoloCompleto(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
										sbnMarcType,
										true);

									// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - intervento interno
	 								// punto 6 7. (il titolo sviluppato, che attualmente compare sia come intestazione che nel tracciato andrebbe eliminato;
									//            il titolo parallelo, che non compare mai come intestazione, va eliminato dal tracciato)

//									intestSecondaria = intestSecondaria + appoggio + " ";


									schedaVO.addListaNatureD(appoggio);
								} else if (legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
												SbnNaturaTitAccesso.P_TYPE) {
//									UtilityCampiTitolo utilityCampiTitoloAccesso = new  UtilityCampiTitolo(
//											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
//											bidAttuale,
//											sbnMarcType);
//									String appoggio = utilityCampiTitoloAccesso.getAreaTitoloCompleto(
//											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
//										sbnMarcType,
//										true);

	 								// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - intervento interno
	 								// punto 6 7. (il titolo sviluppato, che attualmente compare sia come intestazione che nel tracciato andrebbe eliminato;
									//            il titolo parallelo, che non compare mai come intestazione, va eliminato dal tracciato)
//									intestSecondaria = intestSecondaria + appoggio + " ";


								} else if (legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
										SbnNaturaTitAccesso.T_TYPE) {
									UtilityCampiTitolo utilityCampiTitoloAccesso = new  UtilityCampiTitolo(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
											bidAttuale,
											sbnMarcType);
									String appoggio = utilityCampiTitoloAccesso.getAreaTitoloCompleto(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
										sbnMarcType,
										true);
	 								// Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 - intervento interno
	 								// punto 2. l’asterisco nei titoli sviluppati, paralleli e uniformi non deve essere visualizzato
									schedaVO.addListaNatureT(appoggio.replace("*", ""));
								}
							}
						}

					} else if (legameDocType != null) {
						if (legameDocType.getDocumentoLegato() != null) {
							if (legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento() != null) {
								if (legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().getType() ==
										SbnNaturaDocumento.C_TYPE) {
									UtilityCampiTitolo utilityCampiTitoloCollana = new  UtilityCampiTitolo(
											legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
											bidAttuale,
											sbnMarcType);


									appoTitoloOrigNew = utilityCampiTitolo.componiTitSenzaAsterisco(utilityCampiTitoloCollana.getAreaTitoloCompleto(
											legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
											sbnMarcType,
											true));

									elencoCollane = elencoCollane + "(" + appoTitoloOrigNew;
									elencoCollaneConBid = elencoCollaneConBid + "("
											+ legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001()
											+ " " + appoTitoloOrigNew;
									// 20.11.2009 almaviva2 BUG 3307 All'interno delle parentesi che racchiudono la collezione,
									// il n. di sequenza è preceduto da spazio, punto e virgola, spazio
									if (legameDocType.getSequenza() != null) {
										elencoCollane = elencoCollane + " ; "+  legameDocType.getSequenza().trim();
										elencoCollaneConBid = elencoCollaneConBid + " ; "+  legameDocType.getSequenza().trim();
									}
									elencoCollane = elencoCollane + ")";
									elencoCollaneConBid = elencoCollaneConBid + ")";

								} else if (legameDocType.getTipoLegame().getType() == SbnLegameDoc.VALUE_10_TYPE) {
									comboLegami51 = new ComboCodDescDatiVO("", "", "", "");
									UtilityCampiTitolo utilityCampiTitoloLegame51 = new  UtilityCampiTitolo(
											legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
											bidAttuale,
											sbnMarcType);

									String descNatura = "";
									String intestPerTitoloInferiore = "";


									// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
									// richieste contardi mail 07.11.2012 Il numero Bid e la natura del titolo andrebbero  aboliti.
//									if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)){
//										try {
//											descNatura = CodiciProvider.cercaDescrizioneCodice(
//													legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString(),
//													CodiciType.CODICE_NATURA_BIBLIOGRAFICA,
//													CodiciRicercaType.RICERCA_CODICE_UNIMARC);
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//										intestPerTitoloInferiore = "  N.BID:  "
//											+ legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001()
//											+ "    "
//											+ descNatura;
//									}

									comboLegami51.setCodice(intestPerTitoloInferiore);

//									Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 8
//									(8.	nella descrizione dei volumi inferiori che seguono CONTIENE non c’è il . – a suddividere le aree )
									String delimitatoreAree = ". - ";
									comboLegami51.setDescrizione(utilityCampiTitoloLegame51.getAreaTitoloCompleto(
												legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
												sbnMarcType, false) + delimitatoreAree +
												utilityCampiTitoloLegame51.getAreaEdizioneCompleta(
														legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
														sbnMarcType) + delimitatoreAree +
														utilityCampiTitoloLegame51.getAreaPubblicazioneCompleta(
																legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
																sbnMarcType) + delimitatoreAree +
																utilityCampiTitoloLegame51.getAreaDescrizioneFisicaCompleta(
																		legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
																		sbnMarcType));


//									legami51 = intestPerTitoloInferiore +
//										utilityCampiTitoloLegame51.getAreaTitoloCompleto(
//												legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
//												sbnMarcType, false) + " " +
//												utilityCampiTitoloLegame51.getAreaEdizioneCompleta(
//														legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
//														sbnMarcType) + " " +
//														utilityCampiTitoloLegame51.getAreaPubblicazioneCompleta(
//																legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
//																sbnMarcType) + " " +
//																utilityCampiTitoloLegame51.getAreaDescrizioneFisicaCompleta(
//																		legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
//																		sbnMarcType);

									if (legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNumSTDCount() > 0) {
										NumStdType numStdTypeLegame51;
										String isbdArea8Legame51 = "";
										for (int k = 0; k < legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNumSTD().length; k++) {
											numStdTypeLegame51 = new NumStdType();
											numStdTypeLegame51 = legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNumSTD(k);
											// numero issn (J = 011 periodici)
											if (numStdTypeLegame51.getTipoSTD().toString().equals("011")) {
												isbdArea8Legame51= "ISSN: " + numStdTypeLegame51.getNumeroSTD();
												// 6.11.2009 almaviva2 BUG 3307 Punto 2 inserito controllo su NotaNumStandard vuota
												if (numStdTypeLegame51.getNotaSTD() != null && !numStdTypeLegame51.getNotaSTD().trim().equals("")) {
													if (numStdTypeLegame51.getNotaSTD().startsWith("(")) {
														isbdArea8Legame51=isbdArea8Legame51 + numStdTypeLegame51.getNotaSTD();
													} else {
														isbdArea8Legame51=isbdArea8Legame51 + "(" + numStdTypeLegame51.getNotaSTD() + ")";
													}
//													isbdArea8Legame51=isbdArea8Legame51 + "(" + numStdTypeLegame51.getNotaSTD() + ")";
												}
											}
											// numero isbn  (I = 010 monografie)
											if (numStdTypeLegame51.getTipoSTD().toString().equals("010")) {
												isbdArea8Legame51 = "ISBN: " + numStdTypeLegame51.getNumeroSTD();
												// 6.11.2009 almaviva2 BUG 3307 Punto 2 inserito controllo su NotaNumStandard vuota
												if (numStdTypeLegame51.getNotaSTD() != null && !numStdTypeLegame51.getNotaSTD().trim().equals("")) {
													if (numStdTypeLegame51.getNotaSTD().startsWith("(")) {
														isbdArea8Legame51=isbdArea8Legame51 + numStdTypeLegame51.getNotaSTD();
													} else {
														isbdArea8Legame51=isbdArea8Legame51 + "(" + numStdTypeLegame51.getNotaSTD() + ")";
													}
//													isbdArea8Legame51=isbdArea8Legame51 + "(" + numStdTypeLegame51.getNotaSTD() + ")";
												}
											}
										}
//										legami51 = legami51 + isbdArea8Legame51;
										// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
										// l'ISBN deve andara a riga nuova (come le note) e a riga nuova riportare l'inventario
										if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)){
											comboLegami51.setDatiTre(isbdArea8Legame51);
										} else {
											comboLegami51.setDescrizione(comboLegami51.getDescrizione() + " " + isbdArea8Legame51);
										}
//										comboLegami51.setDescrizione(comboLegami51.getDescrizione() + isbdArea8Legame51);
									} else {
										// CREDO SIA UN ERRORE: lo asterisco
//										schedaVO.setIsbdArea8("");
									}

									// Chiamata al servizio di Documento Fisico per la predisposizione delle area relative alla Collocazione e Num. Inventario
									//================================================================================================
									List segnatureLegame51 = new ArrayList();
									try {
										segnatureLegame51 =  getInventarioBean().getSegnature(sbnMarcType.getSbnUser().getBiblioteca().substring(0,3),
												sbnMarcType.getSbnUser().getBiblioteca().substring(3,6),
												utilityCampiTitoloLegame51.getBid(),
												ticket);

									} catch (DataException e) {
									} catch (ApplicationException e) {
									} catch (ValidationException e) {
									} catch (RemoteException e) {
									}

									// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
									// Reinserita sopra la chiamata al servizio di DOC Fisico per avere le segnature del bid in legame 51
									// nelle ricerca si sostituisge la lista segnatureLegame51 a quella di listaSegnature
									int size2 = ValidazioneDati.size(segnatureLegame51);

									if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI) && size2 == 0) {
										// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
										// il titolo contenuto deve essere stampato solo se ha inventari x la biblioteca.
									} else {
										SchedaVO rec = null;
										boolean trovatiInv = false;
										for (int k = 0; k < size2; k++) {
											rec = (SchedaVO) segnatureLegame51.get(k);
											if (rec.getBidInventario().equals(utilityCampiTitoloLegame51.getBid())) {
												trovatiInv = true;
												// Inizio modifica almaviva2 12.03.2010 PUNTO 1 - MAIL SCAFUTO del 12 marzo 2010 - stampa schede: aggiustamenti
												// modifcata la costruzione della stringa del topografico.
//												if (rec.getSequenza() == null || rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
//													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
//													comboLegami51.setDatiUno(rec.getInventario().trim());
//													comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
//												} else {
//													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
//													comboLegami51.setDatiUno(rec.getInventario().trim());
//													comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
//												}

												// Inizio modifica almaviva2 05.042012 BUG MANTIS collaudo 4946
												// inseriti spazi fra desxrizione impostata precedentemente e la parte relativa alla sezione ....


												// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
												// l'ISBN deve andara a riga nuova (come le note) e a riga nuova riportare l'inventario
												if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)
														&& schedaVO.getDatiCollocazione().equals("SI")) {
													String precisazione="";
													if (rec.getPrecisazione() != null && !rec.getPrecisazione().trim().equals("")) {
														precisazione="(" + rec.getPrecisazione().trim() + ")";
													}
													if (rec.getSequenza() != null && !rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
														comboLegami51.setDatiUno(rec.getInventario().trim() + " "  + precisazione);
														comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
													} else {
														comboLegami51.setDatiUno(rec.getInventario().trim() + " "  + precisazione);
														comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
													}
												} else {
													if (rec.getSequenza() != null && !rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
														comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "   " + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
														comboLegami51.setDatiUno(rec.getInventario().trim());
														comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
													} else {
														comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "   " + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
														comboLegami51.setDatiUno(rec.getInventario().trim());
														comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
													}
												}


//												if (rec.getSequenza() != null && !rec.getSequenza().trim().equals("")) {//almaviva2 bug 0004916 esercizio
////													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
//													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "   " + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
//													comboLegami51.setDatiUno(rec.getInventario().trim());
//													comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim() + "/" + rec.getSequenza().trim());
//												} else {
////													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
//													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "   " + rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
//													comboLegami51.setDatiUno(rec.getInventario().trim());
//													comboLegami51.setDatiDue(rec.getSezione().trim() + " " + rec.getCollocazione().trim() + " " + rec.getSpecificazione().trim());
//												}
											}
										}
										if (!trovatiInv) {
//											comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "                                  ");
											comboLegami51.setDescrizione(comboLegami51.getDescrizione());
										}


//										Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011
//										PUNTO 10: il n. di sequenza dovrebbe posizionarsi alla fine della collocazione e non all'inizio
										// Il progressivo sequenza del legame non ci deve essere !!!!!!!
//										if (legameDocType.getSequenza() != null) {
////											legami51 = legami51 + " " + legameDocType.getSequenza();
////											Modifica corretiva almaviva2 MAIL Contardi-Scognamiglio 24.10.2011 PUNTO 8
////											PUNTO 10: RISPOSTA il numero in questione è il numero di sequenza del legame e non il primo carattere
////											dell’area1; vengono comunque eliminati gli spazi non significativi
////											comboLegami51.setDescrizione(comboLegami51.getDescrizione() + legameDocType.getSequenza());
//											comboLegami51.setDescrizione(comboLegami51.getDescrizione() + " " + legameDocType.getSequenza().trim());
//										}






	// Inzio Modifica almaviva2 12.11.2009	BUG MANTIS 2824
//										if (listaSegnature != null) {
//											if (size2 == 0) {
////												legami51 = legami51 + "     Dati del topografico mancanti";
////												legami51 = legami51 + "                                  ";
//												comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "                                  ");
//											} else {
//												for (int k = 0; k < size2; k++) {
//													rec = (EtichettaDettaglioVO) segnature.get(k);
////													legami51 = legami51 + rec.getSezione() + " " + rec.getCollocazione() + " " + rec.getSpecificazione() + " " + rec.getSequenza();
//													comboLegami51.setDescrizione(comboLegami51.getDescrizione() + rec.getSezione() + " " + rec.getCollocazione() + " " + rec.getSpecificazione() + " " + rec.getSequenza());
//												}
//											}
//										} else {
////											legami51 = legami51 + "                                  ";
//											comboLegami51.setDescrizione(comboLegami51.getDescrizione() + "                                  ");
//										}
	// Inzio Modifica almaviva2 12.11.2009	BUG MANTIS 2824
										//================================================================================================
//										legami51 = legami51 + "     dati doc.fisico: Topografico";
//										schedaVO.addListaLegami51(legami51);


										// Inzio Modifica almaviva2 28/01/2010	BUG MANTIS 3275 (si stampa solo se ci sono segnature se
										// è stato chiesto Solo Documenti inventariati
										if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE)){
											if (titoliNonPosseduti) {
												schedaVO.addListaLegami51(comboLegami51);
											} else {
//												if (size2 > 0) {
												if (trovatiInv) {
													schedaVO.addListaLegami51(comboLegami51);
												}
											}
										} else if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)){
											schedaVO.addListaLegami51(comboLegami51);
										}

//										schedaVO.addListaLegami51(comboLegami51);
										// Inzio Modifica almaviva2 28/01/2010	BUG MANTIS 3275 (si stampa solo se ci sono segnature se
										// è stato chiesto Solo Documenti inventariati


									}

								} else if (legameDocType.getTipoLegame().getType() == SbnLegameDoc.VALUE_9_TYPE) {
									// Legame 01 Fa parte
									// Eliminare dal catalogo gli spogli se non checkati nella mappa di richiesta stampa.
									if (codAttivita.equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)
											&& schedaVO.getTitoliAnalitici().equals("NO")) {

									} else {
										UtilityCampiTitolo utilityCampiTitoloMadre = new  UtilityCampiTitolo(
												legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
												bidAttuale,
												sbnMarcType);

										schedaVO.setIsbdArea1Madre(utilityCampiTitoloMadre.getAreaTitoloCompleto(
												legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
												sbnMarcType, false));

										// Modifica almaviva2 23 ottobre 2009 reperimento areaPubbl per legami51- BUG 3225
										schedaVO.setIsbdArea1Madre(schedaVO.getIsbdArea1Madre()
												+ ". - "
												+ utilityCampiTitoloMadre.getAreaPubblicazioneCompleta(
															legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
															sbnMarcType)
												+ ". - "
												+ utilityCampiTitoloMadre.getAreaDescrizioneFisicaCompleta(
															legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
															sbnMarcType));

										schedaVO.setIsbdArea1MadreconBid(legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001()
												+ schedaVO.getIsbdArea1Madre()
												+ ". - "
												+ utilityCampiTitoloMadre.getAreaPubblicazioneCompleta(
															legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
															sbnMarcType)
												+ ". - "
												+ utilityCampiTitoloMadre.getAreaDescrizioneFisicaCompleta(
																	legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001(),
																	sbnMarcType));
									}
								}

							} else if (legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso() != null) {
								if (legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
										SbnNaturaTitAccesso.B_TYPE) {
									area7 = area7 + legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001() + " ";
									area7conBid = area7conBid + legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001() + " ";
								} else if (legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
										SbnNaturaTitAccesso.D_TYPE
										|| legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
												SbnNaturaTitAccesso.P_TYPE) {
									intestSecondaria = intestSecondaria + legameDocType.getDocumentoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001() + " ";
								}  else if (legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().getType() ==
										SbnNaturaTitAccesso.T_TYPE) {
									UtilityCampiTitolo utilityCampiTitoloAccesso = new  UtilityCampiTitolo(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
											bidAttuale,
											sbnMarcType);
									String appoggio = utilityCampiTitoloAccesso.getAreaTitoloCompleto(
											legameTitAccessoType.getTitAccessoLegato().getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
										sbnMarcType,
										true);
									schedaVO.addListaNatureT(appoggio);
								}
							}
						}
					}
				}
			}
		}

		// Inserimento degli eventuali possessori nel caso di Stampa Cataloghi per Possessori/Provenienza
		if (codAttivita.equals("ZG200") && elem != null) {
			String appo="";
			contatoreIntestSecond++;
			if (elem.getDescCampoDue().equals("P")) {
				appo = " " + NumeriRomani.converti(contatoreIntestSecond) + ". "
					+ elem.getCampoDue() + " (Possessore dell'Inventario: " + elem.getNota() + ")";
			} else if (elem.getDescCampoDue().equals("R")) {
				appo = " " + NumeriRomani.converti(contatoreIntestSecond) + ". "
					+ elem.getCampoDue() + " (Provenienza dell'Inventario: " + elem.getNota() + ")";
			}
			intestSecondaria = intestSecondaria + appo;
		}

		schedaVO.setIsbdArea6(elencoCollane);
		schedaVO.setIsbdArea6conBid(elencoCollaneConBid);
		schedaVO.setIsbdArea7(area7);
		schedaVO.setIsbdArea7conBid(area7conBid);
		schedaVO.setAutoreRespons1(autorePrincipale);
		schedaVO.setElencoIntestSecondarie(intestSecondaria);
		schedaVO.setElencoSoggetti(elencoSoggetti);

		return schedaVO;
	}


	private DocumentoFisicoBMT getInventarioBean() {
		try {
			return DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static final String getDataPrepHome() throws Exception {
		return CommonConfiguration.getProperty(SBNWEB_EXPORT_UNIMARC_HOME, "c:/bidStampaCataloghi");
	}

}
