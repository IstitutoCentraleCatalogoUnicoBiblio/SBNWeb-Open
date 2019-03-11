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
package it.iccu.sbn.web.actions.gestionestampe.stampaOnLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBuoniDiOrdineVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.stampaOnLine.StampaOnLineForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

public class StampaOnLineAction extends ReportAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(StampaOnLineAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma", "conferma");
		map.put("button.indietro", "indietro");
		map.put("servizi.bottone.si", "conferma");
		map.put("servizi.bottone.no", "indietro");
		map.put("documentofisico.lsModelli", "listaSupportoModelli");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {


		StampaOnLineForm currentForm = (StampaOnLineForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			String numCopie = (String) utenteEjb.getDefault(ConstantDefault.GDF_NRO_COPIE_ETICH);
			if (!ValidazioneDati.strIsEmpty(numCopie))
				currentForm.setNumCopie(numCopie);

			UserVO utente = Navigation.getInstance(request).getUtente();
			if (currentForm.getTipoModello() == null) {
				ModelloDefaultVO modello = this.getModelloDefault(utente
						.getCodPolo(), currentForm.getCodBibEtichetta(), utente.getTicket());
				if (modello == null) {
					currentForm.setTipoModello("");
					currentForm.setTipoFormato(TipoStampa.PDF.name());
				} else {
					currentForm.setTipoModello(modello.getCodModello());
					currentForm.setTipoFormato(modello.getFormatoStampa());
					currentForm.setDescrBibEtichetta(modello.getDescrBibModello());
				}
				currentForm.setEtichetteVuoteIniziali("");

			} else
				currentForm.setTipoFormato(TipoStampa.PDF.name());

			if (utente.getCodBib().equals(currentForm.getCodBibEtichetta())){
				// almaviva5_20090708 leggo il default per utente - inserito per BVE (roma)
				String modelloEtichette = (String) utenteEjb.getDefault(ConstantDefault.GDF_MODELLO_ETICH);
				if (ValidazioneDati.isFilled(modelloEtichette)) {
					ModelloDefaultVO modello = this.getModelloDefault(utente
							.getCodPolo(), currentForm.getCodBibEtichetta(), utente.getTicket());
					if (modello == null) {
						currentForm.setTipoModello("");
						currentForm.setTipoFormato(TipoStampa.PDF.name());
					} else {
						currentForm.setTipoModello(modelloEtichette);
						String formatoStampa = modello.getFormatoStampa();
						if (ValidazioneDati.equals(formatoStampa, "HTM"))
							formatoStampa = "HTML";
						currentForm.setTipoFormato(TipoStampa.valueOf(formatoStampa).name());
					}
				}
			}

		} catch (ValidationException ve) {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."
					+ ve.getMessage()));

			return mapping.getInputForward();
		} catch (DataException de) { // altri tipi di errore
			if (de.getMessage().equals("recModelloDefaultInesistente")) {
				currentForm.setTipoModello("");
				currentForm.setTipoFormato(TipoStampa.PDF.name());
			}
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + de.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) {
			LinkableTagUtils.resetErrors(request);
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

			return Navigation.getInstance(request).goBack(true);
		}
		return null;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaOnLineForm currentForm = (StampaOnLineForm) form;

		try {
			if (!currentForm.isInitialized()) {
				if (currentForm.getTipoStampa() == null) {
					currentForm.setTipoStampa((StampaType) request.getAttribute("FUNZIONE_STAMPA"));
				}
				if (currentForm.getTipoStampa() == StampaType.STAMPA_ETICHETTE) {
					if (request.getAttribute("codBib") != null) {
						// provengo dalla lista biblioteche
						// carico la lista relativa al codice selezionato
						currentForm.setCodBibEtichetta((String) request.getAttribute("codBib"));
						currentForm.setDescrBibEtichetta((String)request.getAttribute("descrBib"));
					}
					currentForm.setInitialized(true);
					currentForm.setModalita("modConfig");
					//almaviva5_20140703 #3918
					impostaEtichette(request, currentForm);
					ActionForward loadDefault = loadDefault(request, mapping, form);
					if (loadDefault != null) {
						return loadDefault;
					}
				}
			}
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
				return mapping.getInputForward();

			if (request.getAttribute("prov") != null
					&& request.getAttribute("prov").equals("listaSupportoModelli"))
				currentForm.setTipoModello((String) request.getAttribute("codModello"));

			if (currentForm.getTipoStampa() == null) {
				currentForm.setTipoStampa((StampaType) request.getAttribute("FUNZIONE_STAMPA"));
			}

			if (currentForm.getTipoStampa() == StampaType.STAMPA_ETICHETTE) {
				UserVO utente = navi.getUtente();
				String codPolo = utente.getCodPolo();
				String codBib = currentForm.getCodBibEtichetta();
				String ticket = utente.getTicket();
				ModelloEtichetteVO modello = this.getModello(codPolo, codBib, currentForm.getTipoModello(), ticket);
				if (modello != null){
					if (currentForm.getDatiLista().size() > 0){
						List listaEti = currentForm.getDatiLista();
						if (listaEti.size() > 0){
							List listaEti2 = (List)listaEti.get(0);
							if (listaEti2.size() > 0){
								EtichettaDettaglioVO etichetta = (EtichettaDettaglioVO)listaEti2.get(0);
								etichetta.setBiblioteca(modello.getDescrBib());
								currentForm.setCodBibEtichetta(modello.getCodBib());
								currentForm.setDescrBibEtichetta(modello.getDescrBib());
							}
						}
					}
				}
			}

			List listaDati = ValidazioneDati.coalesce(currentForm.getDatiLista(), new ArrayList() );
			if (currentForm.getTipoStampa() == StampaType.STAMPA_COMUNICAZIONE) {
				ComunicazioneVO stampaC = (ComunicazioneVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(stampaC);
				currentForm.setTipoFormato(TipoStampa.RTF.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_FATTURA) {
				FatturaVO stampaF = (FatturaVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(stampaF);
				currentForm.setTipoFormato(TipoStampa.HTML.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_SUGGERIMENTI_BIBLIOTECARIO) {
				List<SuggerimentoVO> stampaSB = (List<SuggerimentoVO>) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati = stampaSB;
				currentForm.setTipoFormato(TipoStampa.HTML.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_SUGGERIMENTI_LETTORE) {
				List<DocumentoVO> stampaSL = (List<DocumentoVO>) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati = stampaSL;
				currentForm.setTipoFormato(TipoStampa.HTML.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_LISTA_ORDINI) {
				StampaBuonoOrdineVO stampaBO = (StampaBuonoOrdineVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(stampaBO);// stampaBO.getListaOrdiniDaStampare();//
				currentForm.setTipoFormato(TipoStampa.HTML.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_BUONI_ORDINE) {
				List<StampaBuoniVO> stampaBO = (List<StampaBuoniVO>) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati = stampaBO;// siccome arriva un arraylist lo ASSEGNO
										// invece di aggiungere all'array
				currentForm.setTipoFormato(TipoStampa.PDF.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));

			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_TERMINI_THESAURO) {
				ParametriStampaTerminiThesauroVO parStamTerThes = (ParametriStampaTerminiThesauroVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(parStamTerThes);// stampaBO.getListaOrdiniDaStampare();//
				currentForm.setTipoFormato(TipoStampa.HTML.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_ETICHETTE) {
				//almaviva5_20140703 #3918
				//impostaEtichette(request, currentForm);

			} else if (ValidazioneDati.in(currentForm.getTipoStampa(), StampaType.STAMPA_RICHIESTA, StampaType.STAMPA_RICHIESTA_AVANZA) ) {
				MovimentoListaVO stampaRichiesta = (MovimentoListaVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(stampaRichiesta);
				currentForm.setTipoFormato(TipoStampa.PDF.name());

				currentForm.setNumCopie(stampaRichiesta.getNumeroCopieStampaModulo());

				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));

			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_ORDINE_RILEGATURA) {
				StampaBuonoOrdineVO stampaBO = (StampaBuonoOrdineVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(stampaBO);// stampaBO.getListaOrdiniDaStampare();//
				currentForm.setTipoFormato(TipoStampa.PDF.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
				currentForm.getOrdine().setTipoStampaOrdine(currentForm.getTipoStampa().name());

			} else if (currentForm.getTipoStampa() == StampaType.STAMPA_MODULO_PRELIEVO) {
				InventarioVO inv = (InventarioVO) request.getAttribute("DATI_STAMPE_ON_LINE");
				listaDati.add(inv);
				currentForm.setTipoFormato(TipoStampa.PDF.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));

			} else {
				listaDati = (List) request.getAttribute("DATI_STAMPE_ON_LINE");
				currentForm.setTipoFormato(TipoStampa.HTML.name());
				currentForm.setElencoModelli(getElencoModelli(currentForm.getTipoStampa()));
			}
			currentForm.setDatiLista(listaDati);

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	private void impostaEtichette(HttpServletRequest request,
			StampaOnLineForm currentForm) {
		if (request.getAttribute("findForward") != null
				&& request.getAttribute("findForward").equals("back")) {
			String chiamante;
			if (request.getAttribute("prov") != null) {
				chiamante = (String) request.getAttribute("prov");
				currentForm.setFindForward("back");
				request.setAttribute("prov", chiamante);
				request.setAttribute("findForward", "back");
			}
		}
		String descrBibEtichetta = currentForm.getDescrBibEtichetta();
		List listaDati = ValidazioneDati.coalesce(currentForm.getDatiLista(), new ArrayList() );
		currentForm.setDatiLista(listaDati);

		if (request.getAttribute("listaEtichetteBarcode") == null) {
			EtichettaDettaglioVO datiStampaEtichette = null;
			if (request.getAttribute("DATI_STAMPE_ON_LINE") != null) {
				datiStampaEtichette = (EtichettaDettaglioVO) request.getAttribute("DATI_STAMPE_ON_LINE");
			} else {
				datiStampaEtichette = new EtichettaDettaglioVO();
			}
			if (descrBibEtichetta != null && !descrBibEtichetta.trim().equals("")) {
				datiStampaEtichette.setBiblioteca(descrBibEtichetta);
			} else {
				datiStampaEtichette.setBiblioteca("");
			}
			listaDati.add(datiStampaEtichette);
		} else {
			List<EtichettaDettaglioVO> listaEtichetteDaStampare = null;
			if (request.getAttribute("prov") != null
					&& request.getAttribute("prov").equals("modInvColl")) {
				currentForm.setProv("modInvColl");
				if (request.getAttribute("ordineCompletato") != null) {
					currentForm.setRitornareAOrdine("ordineCompletato");
				}
			}
			if (request.getAttribute("DATI_STAMPE_ON_LINE") != null) {
				listaEtichetteDaStampare = (List<EtichettaDettaglioVO>) request
						.getAttribute("DATI_STAMPE_ON_LINE");
			} else {
				listaEtichetteDaStampare = new ArrayList<EtichettaDettaglioVO>();
			}
			List<EtichettaDettaglioVO> listaEtiConBib = new ArrayList<EtichettaDettaglioVO>();
			for (int i = 0; i < listaEtichetteDaStampare.size(); i++) {
				EtichettaDettaglioVO etichettaDiLista = listaEtichetteDaStampare
						.get(i);
				if (descrBibEtichetta != null
						&& !descrBibEtichetta.trim().equals("")) {
					etichettaDiLista.setBiblioteca(descrBibEtichetta);
				} else {
					etichettaDiLista.setBiblioteca("");
				}
				listaEtiConBib.add(etichettaDiLista);

			}
			listaDati.add(listaEtiConBib);
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaOnLineForm currentForm = ((StampaOnLineForm) form);
		// id della sessione
		UserVO user = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		String utente = user.getUserId();

		try {
			StampaType tipoStampa = currentForm.getTipoStampa();// (StampaType)request.getAttribute("FUNZIONE_STAMPA");
			List listaDati = currentForm.getDatiLista();
			String tipoOrdinamento = currentForm.getTipoOrdinamento();

			if (!ValidazioneDati.isFilled(listaDati) ) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.stampa.Inesistente"));
				 return mapping.getInputForward();

			} else {
				// base path per i percorsi fisici
				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				// file e path del template jrxml
				String fileJrxml = currentForm.getTipoModello() + ".jrxml";
				String pathJrxml = basePath + File.separator + "jrxml" + File.separator + fileJrxml;
				// cartella da utilizzare come storage dei file generati con i
				// report
				String pathDownload = StampeUtil.getBatchFilesPath();
				String tipoFormato = currentForm.getTipoFormato();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				// codici polo e biblioteca per dove servono
				String codPolo = Navigation.getInstance(request).getUtente().getCodPolo();
				String codBib = currentForm.getCodBibEtichetta();

				// preparo StampaOnLineVO e vi inserisco i dati comuni a tutte
				// le stampe
				StampaOnLineVO stampaOnLineVO = currentForm.getOrdine();
				stampaOnLineVO.setDownload(pathDownload);
				stampaOnLineVO.setDownloadLinkPath("/");
				stampaOnLineVO.setTipoStampa(tipoFormato);
				stampaOnLineVO.setUser(utente);

				// vado a preparare l'ouput stampa per stampa
				OutputStampaVo output = null;
				if (tipoStampa == StampaType.STAMPA_MODULO_PRELIEVO) {
					listaDati.add(1, ValidazioneDati.trimOrEmpty(currentForm.getMotivoPrelievo()) );
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaModuloPrelievo(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_LISTA_FORNITORI) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					stampaOnLineVO.setCodPolo(codPolo);
					stampaOnLineVO.setCodBib(codBib);
					output = factory.getStampeOnline().stampaOnlineFornitori(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_BOLLETTARIO) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineBollettario(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_FATTURA) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineFattura((StampaOnLineVO) stampaOnLineVO.clone());
				} else if (tipoStampa == StampaType.STAMPA_COMUNICAZIONE) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineComunicazione(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_LISTA_UTENTI) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineUtente(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_LISTA_BIBLIOTECHE) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					stampaOnLineVO.setTipoOrdinamento(tipoOrdinamento);
					output = factory.getStampeOnline().stampaOnlineBiblioteche(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_BUONI_ORDINE) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineBuoniOrdine(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_SUGGERIMENTI_BIBLIOTECARIO) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline()
					.stampaOnlineSuggerimentiBibliotecario(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_SUGGERIMENTI_LETTORE) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineSuggerimentiLettore(stampaOnLineVO);
				} else if (ValidazioneDati.in(tipoStampa, StampaType.STAMPA_RICHIESTA, StampaType.STAMPA_RICHIESTA_AVANZA)) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setNumCopie(Integer.valueOf(currentForm.getNumCopie()));
					stampaOnLineVO.setDatiStampa(listaDati);
					output = factory.getStampeOnline().stampaOnlineRichiesta(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_ETICHETTE) {
					// eccezione perché il Design viene letto da DB
					// fileJrxml NON contiene quindi il path
					stampaOnLineVO.setEtichetteVuoteIniziali(currentForm.getEtichetteVuoteIniziali()); //
					if (ValidazioneDati.strIsNumeric(currentForm.getNumCopie())
							&& (Integer.valueOf(currentForm.getNumCopie()) > 0)) {
						stampaOnLineVO.setNumCopie(Integer.parseInt(currentForm.getNumCopie())); //
					} else {

						LinkableTagUtils.addError(request, new ActionMessage("errors.documentofisico.numeroCopieDeveEssereNumerico"));

						return mapping.getInputForward();
					}
					stampaOnLineVO.setDatiStampa(listaDati);
					stampaOnLineVO.setCodPolo(codPolo);
					stampaOnLineVO.setCodBib(codBib);
					if (currentForm.getModalita() != null && currentForm.getModalita().equals("modConfig")){
						//stampa solo modello
						stampaOnLineVO.setTemplate(fileJrxml);
//						stampaOnLineVO.setTemplate(stampaOnLineForm.getTipoModello());
						stampaOnLineVO.setTemplateBarCode(null);
					}else if (currentForm.getModalita() != null && currentForm.getModalita().equals("modBarCode")){
						//stampa solo barcode
						currentForm.setElencoModelli(getElencoModelli());
						if (currentForm.getElencoModelli() != null && currentForm.getElencoModelli().size() > 0){
							for (int index = 0; index < currentForm.getElencoModelli().size(); index++) {
								ModelloStampaVO rec = (ModelloStampaVO) getElencoModelli().get(index);
								currentForm.setModello(rec);
							}
//							fileJrxml = stampaOnLineForm.getModello().getJrxml()+".jrxml";
							basePath=this.servlet.getServletContext().getRealPath(File.separator);

							pathJrxml = null;
							fileJrxml = currentForm.getModello().getJrxml() + ".jrxml";
//							fileJrxml = stampaOnLineForm.getModello().getNomeModello() + ".jrxml";
							pathJrxml = basePath + File.separator + "jrxml" + File.separator + fileJrxml;
							stampaOnLineVO.setTemplateBarCode(pathJrxml);
//							stampaOnLineVO.setTemplateBarCode(fileJrxml);
							stampaOnLineVO.setTemplate("");


						}else{

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));

							return mapping.getInputForward();
						}
					}
					output = factory.getStampeOnline().stampaOnlineEtichette(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_TERMINI_THESAURO) {
					stampaOnLineVO.setTemplate(pathJrxml);
					stampaOnLineVO.setDatiStampa(listaDati);

					List listaDaStampar = new ArrayList();// <StampaBuoniDiOrdineVO>
					StampaTerminiThesauroVO thesauroDaStam = new StampaTerminiThesauroVO();
					ParametriStampaTerminiThesauroVO parStaTerThes = (ParametriStampaTerminiThesauroVO) (listaDati.get(0));

					// Output: Lista di ElementoStampaSematicaVO che contiene la
					// lista dei titoli e dei termini
					List<ElementoStampaSemanticaVO> listaTuttiElemThesauro = new ArrayList<ElementoStampaSemanticaVO>();
					listaTuttiElemThesauro = parStaTerThes.getOutput();
					int dimArrayElemThesauro = listaTuttiElemThesauro.size();
					int count = 0;
					boolean nuovoThesauro = false;
					if (dimArrayElemThesauro > 0) {

						ElementoStampaTerminiThesauroVO elemStampa = new ElementoStampaTerminiThesauroVO();
						elemStampa = (ElementoStampaTerminiThesauroVO) listaTuttiElemThesauro.get(count);
						thesauroDaStam = impostaThesauroDaStampare(elemStampa, parStaTerThes);
						// , elemStampa.getLegami(),
						// elemStampa.getLegamiTermini());
						// //, linguaIT, confBO,
						// presenzaProt);
						int numRemoved = 1;
						boolean sameFornitore = true;
						nuovoThesauro = false;
						while ((dimArrayElemThesauro > (count))) {
							if (nuovoThesauro) {
								thesauroDaStam = impostaThesauroDaStampare(elemStampa, parStaTerThes);// ,
								// legamiTitoloUnThesauro,
								// legameTermineUnThesauro);//,
								// confBO,
								// presenzaProt)
								// ;
							}
							StrutturaTerna strutturaLTit = null;
							List legamiTitolo = new ArrayList();

							LegameTitoloVO singleTitolo;
							List<LegameTitoloVO> lisLT = elemStampa.getLegamiTitoli();// .subList(0,
							// sizeo);
							for (Iterator it = lisLT.iterator(); it.hasNext();) {
								singleTitolo = (LegameTitoloVO) it.next();
								strutturaLTit = new StrutturaTerna(singleTitolo.getBid(), singleTitolo.getTitolo(),
										singleTitolo.getNotaLegame());
								legamiTitolo.add(strutturaLTit);
							}

							JRRewindableDataSource titoli = new JRBeanCollectionDataSource(legamiTitolo);// elemStampa.getLegami());//legami
							thesauroDaStam.setTitoli(titoli);

							StrutturaTerna strutturaLTerm = null;
							List legamiTerms = new ArrayList();// elemStampa.getLegamiTermini());
							LegameTermineVO singleTermine;
							List<LegameTermineVO> lisLTer = elemStampa.getLegamiTermini();
							String appS1;
							String appS2;
							String appS3;
							for (Iterator it = lisLTer.iterator(); it.hasNext();) {
								singleTermine = (LegameTermineVO) it.next();
								appS1 = new String(singleTermine.getDid());
								appS2 = new String(singleTermine.getTipoLegame());
								appS3 = new String(singleTermine.getTesto());
								strutturaLTerm = new StrutturaTerna(appS1, appS2, appS3);
								// singleTermine.getDid(),
								// singleTermine.getTipoLegame(),
								// singleTermine.getTesto());
								legamiTerms.add(strutturaLTerm);
							}

							JRRewindableDataSource termini = new JRBeanCollectionDataSource(legamiTerms);
							// legamiTermini
							thesauroDaStam.setTermini(termini);

							listaDaStampar.add(thesauroDaStam);
							nuovoThesauro = true;
							count++;

							thesauroDaStam = new StampaTerminiThesauroVO();

							if (dimArrayElemThesauro > (count)) {
								elemStampa = new ElementoStampaTerminiThesauroVO();
								elemStampa = (ElementoStampaTerminiThesauroVO) listaTuttiElemThesauro.get(count);
							}
						}
					}
					stampaOnLineVO.setDatiStampa(listaDaStampar);
					output = factory.getStampeOnline().stampaOnlineTerminiThesauro(stampaOnLineVO);
				} else if (tipoStampa == StampaType.STAMPA_LISTA_ORDINI) {
					stampaOnLineVO.setTemplate(pathJrxml);

					List listaDaStampar = new ArrayList();// <StampaBuoniDiOrdineVO>
					StampaBuoniDiOrdineVO buonoOrdDaStam = new StampaBuoniDiOrdineVO();
					List ordiniUnBuonoOrd = new ArrayList();

					StampaBuonoOrdineVO sbovo = (StampaBuonoOrdineVO) (listaDati.get(0));// get(0)
					ConfigurazioneBOVO confBO = sbovo.getConfigurazione();
					boolean linguaIT = confBO.isLinguaIT();
					boolean prezzo = confBO.isPresenzaPrezzo();
					boolean presenzaProt = confBO.isEtichettaProtocollo();

					List listaTuttiTipiOrdini = sbovo.getListaOrdiniDaStampare();
					int dimArrayOrdini = listaTuttiTipiOrdini.size();

					// TODO testare e correggere
					String nomelogo = "images" + File.separator + "on.jpg";// basePath+File.separator+"images"+File.separator+"Sbn_sfondo.gif";
					if (sbovo.getConfigurazione().getNomeLogoImg() != null
							&& sbovo.getConfigurazione().getNomeLogoImg().trim().length() > 0) {
						nomelogo = StampeUtil.getBatchFilesPath() + File.separator
						+ sbovo.getConfigurazione().getNomeLogoImg();
					}
					double actualPri = 0;// 0.0d;
					String dataBO = "          ";
					if (dimArrayOrdini > 0) {

						OrdiniVO ordine;
						int count = 0;
						String tipoPrec = "";
						String tipoSucc = "";
						int resultCompare = 0;
						ordine = new OrdiniVO();
						ordine = (OrdiniVO) listaTuttiTipiOrdini.get(count);// remove(count);
						tipoPrec = ordine.getTipoOrdine();
						FornitoreVO forni = new FornitoreVO();
						forni = ordine.getAnagFornitore();
						String codiceForn = forni.getCodFornitore();
						boolean nuovoBuono = false;
						buonoOrdDaStam = impostaBuonoOrdineDaStampare(ordine,
								linguaIT, confBO, presenzaProt);
						/**/

						int numRemoved = 1;
						boolean sameFornitore = true;
						while (dimArrayOrdini > (count)) {
							nuovoBuono = false;
							while ((dimArrayOrdini > (count))) {
								if (nuovoBuono) {
									buonoOrdDaStam = impostaBuonoOrdineDaStampare(ordine, linguaIT, confBO,
											presenzaProt);
								}
								tipoSucc = ordine.getTipoOrdine();
								resultCompare = tipoSucc.compareToIgnoreCase(tipoPrec);
								if ((resultCompare != 0) || (!sameFornitore)) {
									// se i tipi di Ordine sono uguali

									JRRewindableDataSource ordini = new JRBeanCollectionDataSource(ordiniUnBuonoOrd);
									buonoOrdDaStam.setOrdini(ordini);

									// Da eliminare
									buonoOrdDaStam.setNomeLogoImg(nomelogo);
									/**/
									try {
										buonoOrdDaStam.setImporto_Fornitura_Euro(actualPri);
									} catch (Exception e) {
										log.error("", e);
									}

									buonoOrdDaStam.setAnagFornitore(forni);
									buonoOrdDaStam.setDataBuonoOrdine(dataBO);
									listaDaStampar.add(buonoOrdDaStam);
									nuovoBuono = true;
									actualPri = 0;// 0.0d;

									buonoOrdDaStam = new StampaBuoniDiOrdineVO();
									ordiniUnBuonoOrd = new ArrayList();
									ordiniUnBuonoOrd.add(ordine);
									actualPri = actualPri + ordine.getPrezzoEuroOrdine();
									count++;
								} else { // se i tipi di ordine non sono uguali
									// ho un ordine di uno stesso
									// StampaBuoniDiOrdineVO
									if (!prezzo) {
										/*
										 * setPrezzoEuroOrdineStr(""); //da
										 * cambiare in przzo Ordine poi
										 */
										ordine.setPrezzoEuroOrdine(0.0d);
									}
									ordiniUnBuonoOrd.add(ordine);
									actualPri = actualPri + ordine.getPrezzoEuroOrdine();
									count++;
								}
								// Da DECOMMENTARE fornAtt =
								// ordine.getAnagFornitore().getCodFornitore();

								/* if(dimArrayOrdini > (count+1)){ */
								if (dimArrayOrdini > (count)) {
									tipoPrec = tipoSucc;
									/*
									 * ordine =
									 * (OrdiniVO)listaTuttiTipiOrdini.get
									 * (count+1);//remove(count);
									 */
									if (!sameFornitore) {// 20
										forni = ordine.getAnagFornitore();
									}
									ordine = (OrdiniVO) listaTuttiTipiOrdini.get(count);
									if (!(codiceForn.equals(ordine
											.getAnagFornitore()
											.getCodFornitore()))) {
										sameFornitore = false;
										// forni = ordine.getAnagFornitore();
									}
								}
							}
						}

						JRRewindableDataSource ordini = new JRBeanCollectionDataSource(ordiniUnBuonoOrd);
						buonoOrdDaStam = new StampaBuoniDiOrdineVO();// NB: le
						// impostazioni
						// sono
						// sempre
						// le
						// stesse
						buonoOrdDaStam = impostaBuonoOrdineDaStampare(ordine,
								linguaIT, confBO, presenzaProt);

						forni = ordine.getAnagFornitore();
						buonoOrdDaStam.setOrdini(ordini);
						/**/

						buonoOrdDaStam.setNomeLogoImg(nomelogo);

						buonoOrdDaStam.setImporto_Fornitura_Euro(actualPri);

						buonoOrdDaStam.setAnagFornitore(forni);
						buonoOrdDaStam.setDataBuonoOrdine(dataBO);
						listaDaStampar.add(buonoOrdDaStam);
					}
					stampaOnLineVO.setDatiStampa(listaDaStampar);
					// fine buoni ordine
					output = factory.getStampeOnline().stampaOnlineBuoniOrdine(
							stampaOnLineVO);

				} else
					if (tipoStampa == StampaType.STAMPA_ORDINE_RILEGATURA) {
						stampaOnLineVO.setTicket(Navigation.getInstance(request).getUserTicket());

						StringBuilder model = new StringBuilder(512);
						model.append(basePath);
						model.append(File.separator);
						model.append("jrxml");
						model.append(File.separator);

						StampaType stype = StampaType.valueOf(currentForm.getOrdine().getTipoStampaOrdine());
						switch (stype) {
						case STAMPA_ORDINE_RILEGATURA:
						case STAMPA_PATRON_CARD:
						case STAMPA_CART_ROUTING_SHEET:
							model.append(currentForm.getTipoModello());
							model.append(currentForm.getOrdine().getTipoStampaOrdine());
							break;

						case STAMPA_MODULO_PRELIEVO:
							ModelloStampaVO modello = (ModelloStampaVO) ValidazioneDati.first(getElencoModelli(StampaType.STAMPA_MODULO_PRELIEVO));
							model.append(modello.getJrxml());
							break;
						}
						model.append(".jrxml");
						stampaOnLineVO.setTemplate(model.toString());

						stampaOnLineVO.setDatiStampa(listaDati);
						stampaOnLineVO.setTipoOperazione(currentForm.getOrdine().getTipoStampaOrdine());
						output = factory.getStampeOnline().stampaOnlineBuoniOrdine(stampaOnLineVO);
					}
				// ora ho finito di preparare la stampa, devo effettuare alcune
				// operazioni prima di uscire

				if (output.getStato().equals("OUTOK")) {
					// Nel caso ho stampe con immagini -> ho OUTOK
					InputStream streamOutput = output.getOutput();
					JasperPrint jasPr = output.getJasPr();
					this.stampaOnLine(response, streamOutput, tipoFormato,
							fileJrxml, jasPr);

					if (tipoFormato.equals(TipoStampa.HTML.name())) {
						String percRel = request.getServletPath();
						StringBuffer percUrl = request.getRequestURL();
						String percorsoOK = percUrl.toString();
						int indice = percorsoOK.indexOf(percRel);
						pathDownload = percorsoOK.substring(0, indice);
						pathDownload = pathDownload + "/images?image=";

						PrintWriter out = response.getWriter();
						JRHtmlExporter htmlExporter = output.getHtmlExport();
						Map imagesMap = new HashMap();

						request.getSession().setAttribute("IMAGES_MAP",	imagesMap);
						request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasPr);
						htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, pathDownload);
						htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
						// OuJASPER_PRINT, jasPr);
						// List jasperPrintList =
						// BaseHttpServlet.getJasperPrintList(request);
						htmlExporter.exportReport();
					}
				} else if (output.getStato().equals(ConstantsJMS.STATO_OK)) {
					// altrimenti ho OK come stato di ritorno
					if (tipoStampa == StampaType.STAMPA_LISTA_ORDINI) {
						// todo SALVARE LO STATO ORDINE PERCHé STAMPATO
						boolean risultato = factory
						.getGestioneAcquisizioni()
						.gestioneStampaOrdini(((StampaBuonoOrdineVO) listaDati.get(0)).getListaOrdiniDaStampare(),
								null, "ORD", ((StampaBuonoOrdineVO) listaDati.get(0)).getUtente(), "");
						if (!risultato) {

							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

							return mapping.getInputForward();
						}

					}
					if (tipoStampa == StampaType.STAMPA_BUONI_ORDINE) {
						// todo SALVARE LO STATO ORDINE PERCHé STAMPATO
						String tipoOggetti = "ORD";
						if (((StampaBuoniVO) listaDati.get(0)).getNumBuonoOrdine() != null
								&& ((StampaBuoniVO) listaDati.get(0)).getNumBuonoOrdine().trim().length() > 0) {
							tipoOggetti = "BUO";
						}
						boolean risultato = factory.getGestioneAcquisizioni().gestioneStampaOrdini(
								((StampaBuoniVO) listaDati.get(0)).getListaOggDaStampare(),
								null, tipoOggetti, ((StampaBuoniVO) listaDati.get(0)).getUtente(), "");
						if (!risultato) {

							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

							return mapping.getInputForward();
						}
					}
					InputStream streamOutput = output.getOutput();
					this.stampaOnLine(response, streamOutput, tipoFormato,
							fileJrxml, null);
				}

				LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa"));


			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return null;// mapping.findForward("indietroUtenti");
	}

	private void stampaOnLine(HttpServletResponse response, InputStream datiStream,
			String tipoStampa, String exportFileName, JasperPrint jasperPrint)
			throws IOException, JRException {
		if (exportFileName.indexOf(".") > 0) {
			exportFileName = exportFileName.substring(0, exportFileName.lastIndexOf(".") - 1);
		}
		if (tipoStampa.equals(TipoStampa.HTML.name())) {
			if (exportFileName.equals("default_buoni_ordine2")
					|| exportFileName.equals("default_buoni_ordine1")) {
				response.setContentType("text/html");// application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + exportFileName + ".html");
				return;
			}
		}
		impostaResponse(response, exportFileName, tipoStampa);
		ServletOutputStream servletOutputStream = response.getOutputStream();
		SbnStampe.transferData(datiStream, servletOutputStream);//servletOutputStream.write(datiStream);
		datiStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	public StampaBuoniDiOrdineVO impostaBuonoOrdineDaStampare(OrdiniVO ordine,
			boolean linguaIT, ConfigurazioneBOVO confBO, boolean presenzaProt) {
		StampaBuoniDiOrdineVO stampa = new StampaBuoniDiOrdineVO();
		String tipoOrd = ordine.getTipoOrdine();
		StrutturaCombo[] strTestoOggetto = null;
		StrutturaCombo[] testoIntroduttivo = null;
		int l = 0;
		int lun = 0;
		if (linguaIT) {
			stampa.setTestoEn("");
			strTestoOggetto = confBO.getTestoOggetto();
			testoIntroduttivo = confBO.getTestoIntroduttivo();
			l = strTestoOggetto.length;
			if (!(l > 0)) {
				strTestoOggetto = confBO.getTestoOggettoEng();
			}
			lun = testoIntroduttivo.length;
			if (!(lun > 0)) {
				testoIntroduttivo = confBO.getTestoIntroduttivoEng();
			}
		} else {
			stampa.setTestoIt("");
			strTestoOggetto = confBO.getTestoOggettoEng();
			testoIntroduttivo = confBO.getTestoIntroduttivo();
			l = strTestoOggetto.length;
			if (!(l > 0)) {
				strTestoOggetto = confBO.getTestoOggetto();
			}
			lun = testoIntroduttivo.length;
			if (!(lun > 0)) {
				testoIntroduttivo = confBO.getTestoIntroduttivo();
			}
		}

		String text = "";
		int contator = 0;
		boolean trovato = false;
		while ((l > contator) && (!trovato)) {// (l >0)
			if ((strTestoOggetto[contator].getCodice()).equals(tipoOrd)) {
				stampa.setTestoIt(strTestoOggetto[contator].getDescrizione());
				trovato = true;
			}
			contator++;
		}

		contator = 0;
		trovato = false;
		while ((lun > contator) && (!trovato)) {// (l >0)
			if ((testoIntroduttivo[contator].getCodice()).equals(tipoOrd)) {
				stampa.setTestoIntroduttivo(testoIntroduttivo[contator]
						.getDescrizione());
				trovato = true;
			}
			contator++;
		}

		if (presenzaProt) {
			stampa.setNumBuonoOrdine("0");
		}
		stampa.setDataBuonoOrdine("          ");

		JRRewindableDataSource intestazioni;
		List intestazi = new ArrayList();
		StrutturaTerna[] confListaDatiIntest = confBO.getListaDatiIntestazione();// confListaDatiIntest[1]
		intestazi.add(confListaDatiIntest[0]);

		intestazioni = new JRBeanCollectionDataSource(intestazi);
		stampa.setIntestazioni(intestazioni);

		JRRewindableDataSource datiFineStampa;
		List datiFineStam = new ArrayList();
		StrutturaTerna[] confListaDatiFineStam = confBO.getListaDatiFineStampa();// confListaDatiIntest[1]
		datiFineStam.add(confListaDatiFineStam[0]);

		datiFineStampa = new JRBeanCollectionDataSource(datiFineStam);
		stampa.setDatiFineStampa(datiFineStampa);
		return stampa;
	}

	public StampaTerminiThesauroVO impostaThesauroDaStampare(
			ElementoStampaTerminiThesauroVO elemStampa,
			ParametriStampaTerminiThesauroVO parStaTerThes) {

		boolean bapp = false;
		StampaTerminiThesauroVO stampa = new StampaTerminiThesauroVO();
		// denominazione della biblioteca
		stampa.setDenBib(parStaTerThes.getDescrizioneBiblioteca());

		stampa.setDescThesauro(parStaTerThes.getDescrizioneThesauro());
		stampa.setDateInsDa(parStaTerThes.getTsIns_da());
		stampa.setDateInsA(parStaTerThes.getTsIns_a());
		stampa.setDateAggDa(parStaTerThes.getTsVar_da());
		stampa.setDateAggA(parStaTerThes.getTsVar_a());
		bapp = parStaTerThes.isStampaTitoli();
		stampa.setStampaTitoli(String.valueOf(bapp));// parStaTerThes.isStampaTitoli());//Boolean.valueOf(parStaTerThes.isStampaTitoli()));
		bapp = parStaTerThes.isStampaNote();
		stampa.setStampaStringaThes(String.valueOf(bapp));// Boolean.valueOf(parStaTerThes.isStampaNote()));
		bapp = parStaTerThes.isSoloTerminiBiblioteca();
		stampa.setThesauriBiblio(String.valueOf(bapp));// Boolean.valueOf(parStaTerThes.isSoloTerminiBiblioteca()));
		stampa.setRelazAltriTerm(String.valueOf(parStaTerThes.isStampaTerminiCollegati()));// Boolean.valueOf(parStaTerThes.isStampaTerminiCollegati()));
		stampa.setCodBib(parStaTerThes.getCodBib());
		stampa.setCodPolo(parStaTerThes.getCodPolo());

		stampa.setStampaNoteTitle(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setStampaNoteThes(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setRelazAltreForm(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setStampaTermBiblio(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setTermine(elemStampa.getTesto());
		stampa.setDid(elemStampa.getId());
		stampa.setNote(elemStampa.getNote());

		return stampa;
	}

	private List getElencoModelli(StampaType tipoStam) {
		try {
			if (tipoStam == StampaType.STAMPA_COMUNICAZIONE) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_COMUNCAZIONE);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_SUGGERIMENTI_BIBLIOTECARIO) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_SUGGERIMENTI_BIBL);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_SUGGERIMENTI_LETTORE) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_SUGGERIMENTI_LETT);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_FATTURA) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_FATTURA);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_BOLLETTARIO) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_LISTA_UTENTI) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI_STAMPA_UTENTE);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_LISTA_FORNITORI) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_LISTA_BIBLIOTECHE) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_BIBLIOTECA);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_LISTA_ORDINI) {
				// TODO gv chiedere come mai ci sono due tipi diversi con gli
				// stessi template
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_BUONI_ORDINE) {
				// TODO gv chiedere come mai ci sono due tipi diversi con gli
				// stessi template
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE);
				return listaModelli;
			} else if (tipoStam == StampaType.STAMPA_TERMINI_THESAURO) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().STAMPA_THESAURO_POLO);
				return listaModelli;
			} else if (ValidazioneDati.in(tipoStam, StampaType.STAMPA_RICHIESTA, StampaType.STAMPA_RICHIESTA_AVANZA)) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI_STAMPA_RICHIESTA);
				return listaModelli;
			} else if (ValidazioneDati.in(tipoStam, StampaType.STAMPA_ORDINE_RILEGATURA)) {
				ModelloStampaVO modello = new ModelloStampaVO();
				modello.setJrxml("default_ordine_");
				return ValidazioneDati.asSingletonList(modello);
			} else if (tipoStam == StampaType.STAMPA_MODULO_PRELIEVO) {
				List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_MODELLO_PRELIEVO);
				return listaModelli;
			}

		} catch (Exception e) {
			log.error("", e);
		}
		return new ArrayList();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaOnLineForm currentForm = (StampaOnLineForm) form;
		Navigation navi = Navigation.getInstance(request);

		try {
			StampaType tipoStampa = currentForm.getTipoStampa();
			if (tipoStampa == StampaType.STAMPA_LISTA_FORNITORI) {
				return mapping.findForward("indietroFornitori");
			} else if (tipoStampa == StampaType.STAMPA_LISTA_UTENTI) {
				return mapping.findForward("indietroUtenti");
			} else if (tipoStampa == StampaType.STAMPA_LISTA_BIBLIOTECHE) {
				return mapping.findForward("indietroBiblioteche");
			} else {
				if (tipoStampa == StampaType.STAMPA_ETICHETTE) {
					if (ValidazioneDati.equals(currentForm.getProv(), "modInvColl") ) {
						if (ValidazioneDati.isFilled(currentForm.getRitornareAOrdine()) ) {
							request.setAttribute("ordineCompletato", "ordineCompletato");
						}
						request.setAttribute("findForward", "back");
						return navi.goBack(true);
					}else{
						request.setAttribute("findForward", "back");
					}
					return navi.goBack();

				} else {
					return navi.goBack(true);
				}
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoModelli(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StampaOnLineForm stampForm = (StampaOnLineForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			request.setAttribute("codPolo", Navigation.getInstance(request).getUtente().getCodPolo());
			request.setAttribute("codBib", stampForm.getCodBibEtichetta());
			request.setAttribute("descrBib", stampForm.getDescrBibEtichetta());
			request.setAttribute("prov", "listaSupportoModelli");
			return mapping.findForward("lenteModelli");
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib,
			String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}

	private List getElencoModelli() {
		try {
			List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE);
			return listaModelli;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private ModelloEtichetteVO getModello(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		ModelloEtichetteVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getModello(codPolo, codBib, codSez, ticket);
		return rec;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		StampaOnLineForm currentForm = (StampaOnLineForm) form;
		if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.GOOGLE)) {
			if (currentForm.getTipoStampa() != StampaType.STAMPA_ORDINE_RILEGATURA)
				return false;

			StampaBuonoOrdineVO params = (StampaBuonoOrdineVO) ValidazioneDati.first(currentForm.getDatiLista());
			OrdiniVO ordine = ValidazioneDati.first(params.getListaOrdiniDaStampare());

			OrdineCarrelloSpedizioneVO ocs = ordine.getOrdineCarrelloSpedizione();
			if (ocs == null || ocs.getPrgSpedizione() < 1)
				return false;

			return ordine.isGoogle() && ordine.isSpedito();
		}

		return false;
	}
}
