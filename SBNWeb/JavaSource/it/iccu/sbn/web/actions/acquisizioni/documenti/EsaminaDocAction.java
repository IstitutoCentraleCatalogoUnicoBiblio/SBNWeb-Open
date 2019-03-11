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
package it.iccu.sbn.web.actions.acquisizioni.documenti;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.documenti.EsaminaDocForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

import org.apache.log4j.Logger;

public class EsaminaDocAction extends LookupDispatchAction implements
		SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(EsaminaDocAction.class);

	private static final String[] BOTTONIERA = new String[] {
			"ricerca.button.salva",
			"ricerca.button.accetta",
			"ricerca.button.rifiuta",
			"ricerca.button.in_attesa",
			"ricerca.button.ripristina"
	};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro", "indietro");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("ricerca.button.salva", "salva");
		map.put("ricerca.button.accetta", "accetta");
		map.put("ricerca.button.rifiuta", "rifiuta");
		map.put("servizi.bottone.si", "Si");
		map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti", "scorriAvanti");
		map.put("ricerca.button.scorriIndietro", "scorriIndietro");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("ricerca.button.stampa", "stampaOnLine");

		//almaviva5_20180323 #6553
		map.put("ricerca.button.in_attesa", "attesa");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			EsaminaDocForm currentForm = (EsaminaDocForm) form;
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
				return mapping.getInputForward();

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setPulsanti(BOTTONIERA);
			}

			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppDocumentoVO ricArr = (ListaSuppDocumentoVO) request
					.getSession().getAttribute("attributeListaSuppDocumentoVO");
			// controllo che non riceva l'attributo di sessione di una lista
			// supporto
			if (ricArr != null && ricArr.getChiamante() != null) {
				currentForm.setEsaminaInibito(true);
				currentForm.setDisabilitaTutto(true);
			}

			currentForm
					.setListaDaScorrere((List<ListaSuppDocumentoVO>) request
							.getSession().getAttribute(
									"criteriRicercaDocumento"));
			if (currentForm.getListaDaScorrere() != null
					&& currentForm.getListaDaScorrere().size() != 0) {
				if (currentForm.getListaDaScorrere().size() > 1) {
					currentForm.setEnableScorrimento(true);
					// esaCambio.setPosizioneScorrimento(0);
				} else {
					currentForm.setEnableScorrimento(false);
				}
			}
			// ||
			// strIsAlfabetic(String.valueOf(this.esaSezione.getPosizioneScorrimento()))
			if (String.valueOf(currentForm.getPosizioneScorrimento()).length() == 0) {
				currentForm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			if (!currentForm.isCaricamentoIniziale()) {
				this.loadDatiDocumentoPassato(
						currentForm,
						currentForm.getListaDaScorrere().get(
								currentForm.getPosizioneScorrimento()));
				currentForm.setCaricamentoIniziale(true);
			}
			// this.loadDatiSuggerimentoPassato(esaSugg.getListaDaScorrere().get(this.esaSugg.getPosizioneScorrimento()));
			this.loadStatoSuggerimento(currentForm);
			this.loadLingue(currentForm);
			this.loadPaesi(currentForm);
			// esaDoc.setStatoSuggerimento(esaDoc.getDatiDocumento().getStatoSuggerimentoDocumento());
			// esaDoc.setLingue(esaDoc.getDatiDocumento().getLingua().getCodice());
			// esaDoc.setPaesi(esaDoc.getDatiDocumento().getPaese().getCodice());

			String bid = (String) request.getAttribute("bid");
			if (bid != null && bid.length() != 0) {
				String titolo = (String) request.getAttribute("titolo");
				// controllo se ho un risultato da interrogazione ricerca
				// String acq = request.getParameter("ACQUISIZIONI");
				// if ( acq != null) {
				currentForm.getDatiDocumento().getTitolo().setCodice(bid);
				if (titolo != null) {
					currentForm.getDatiDocumento().getTitolo()
							.setDescrizione(titolo);
				}
			}
			if (currentForm.getDatiDocumento() != null
					&& currentForm.getDatiDocumento()
							.getStatoSuggerimentoDocumento() != null
					&& (currentForm.getDatiDocumento()
							.getStatoSuggerimentoDocumento().equals("A")
							|| currentForm.getDatiDocumento()
									.getStatoSuggerimentoDocumento()
									.equals("O") || currentForm
							.getDatiDocumento().getStatoSuggerimentoDocumento()
							.equals("R"))) {
				currentForm.setDisabilitaTutto(true);
			} else {
				currentForm.setDisabilitaTutto(false);
			}

			//almaviva5_20180323 #6553
			if (navi.bookmarkExists(Bookmark.Acquisizioni.Ordini.IMPORTA_DATI_ORDINE))
				currentForm.setDisabilitaTutto(true);

			return mapping.getInputForward();
		} catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni."
					+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			// errors.add("generico", new ActionMessage("errors.acquisizioni." +
			// e.getMessage()));
			errors.add("generico", new ActionMessage(
					"errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaDocumentoVO(
					esaDoc.getDatiDocumento());
			// fine validazione

			ActionMessages errors = new ActionMessages();
			esaDoc.setConferma(true);
			esaDoc.setPressioneBottone("salva");
			esaDoc.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage(
					"errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request,
					ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni."
					+ ve.getMessage()));
			this.saveErrors(request, errors);
			esaDoc.setConferma(false);
			esaDoc.setPressioneBottone("");
			esaDoc.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			esaDoc.setConferma(false);
			esaDoc.setPressioneBottone("");
			esaDoc.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			this.loadDatiDocumentoPassato(esaDoc, esaDoc.getListaDaScorrere()
					.get(esaDoc.getPosizioneScorrimento()));
			if (esaDoc.getDatiDocumento() != null
					&& esaDoc.getDatiDocumento()
							.getStatoSuggerimentoDocumento() != null
					&& (esaDoc.getDatiDocumento()
							.getStatoSuggerimentoDocumento().equals("A")
							|| esaDoc.getDatiDocumento()
									.getStatoSuggerimentoDocumento()
									.equals("O") || esaDoc.getDatiDocumento()
							.getStatoSuggerimentoDocumento().equals("R"))) {
				esaDoc.setDisabilitaTutto(true);
			} else {
				esaDoc.setDisabilitaTutto(false);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm currentForm = (EsaminaDocForm) form;
		try {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);

			if (currentForm.getPressioneBottone().equals("salva")) {
				currentForm.setPressioneBottone("");

				DocumentoVO eleDocumento = currentForm.getDatiDocumento();
				eleDocumento.setUtenteCod(Navigation.getInstance(request)
						.getUtente().getFirmaUtente());

				ListaSuppDocumentoVO attrLS = (ListaSuppDocumentoVO) request
						.getSession().getAttribute(
								"attributeListaSuppDocumentoVO");
				ListaSuppDocumentoVO attrLSagg = this
						.AggiornaTipoVarRisultatiListaSupporto(eleDocumento,
								attrLS);
				request.getSession().setAttribute(
						"attributeListaSuppDocumentoVO", attrLSagg);
				// imposta a ricevuto se la direzione è "da fornitore"
				/*
				 * if
				 * (esaSugg.getDatiSuggerimento().getDirezioneComunicazione().
				 * equals("D")) {
				 * esaSugg.getDatiSuggerimento().setStatoComunicazione("3"); }
				 */
				if (!this.modificaDocumento(eleDocumento)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreModifica"));
					this.saveErrors(request, errors);
					// return mapping.getInputForward();
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.modificaOK"));
					this.saveErrors(request, errors);
					return ripristina(mapping, form, request, response);

				}
			}
			/*
			 * if (this.esaSugg.getPressioneBottone().equal("cancella")) {
			 * this.esaSugg.setPressioneBottone(""); DocumentoVO
			 * eleSuggerimento=esaSugg.getDatiSuggerimento(); if
			 * (!this.cancellaSuggerimento(eleSuggerimento)) { ActionMessages
			 * errors = new ActionMessages(); errors.add("generico", new
			 * ActionMessage( "errors.acquisizioni.erroreCancella"));
			 * this.saveErrors(request, errors); } else { ActionMessages errors
			 * = new ActionMessages(); errors.add("generico", new ActionMessage(
			 * "errors.acquisizioni.cancellaOK")); this.saveErrors(request,
			 * errors); esaDoc.setDisabilitaTutto(true); }
			 *
			 * }
			 */

			return mapping.getInputForward();
		} catch (ValidationException ve) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			currentForm.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni."
					+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

			// altri tipi di errore
		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			currentForm.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward stampaOnLine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			EsaminaDocForm esaDoc = (EsaminaDocForm) form;
			List<DocumentoVO> listaSugg = new ArrayList<DocumentoVO>();
			DocumentoVO eleSuggerimento = esaDoc.getDatiDocumento();
			listaSugg.add(eleSuggerimento);
			if (listaSugg != null && listaSugg.size() > 0) {
				// DA RIPRISTINARE PER LA STAMPA (almaviva)
				// TODO GVCANCE
				request.setAttribute("FUNZIONE_STAMPA",
						StampaType.STAMPA_SUGGERIMENTI_LETTORE);
				// request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
				request.setAttribute("DATI_STAMPE_ON_LINE", listaSugg);
				return mapping.findForward("stampaOL");

			}
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaDoc.setConferma(false);
			esaDoc.setPressioneBottone("");
			esaDoc.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			int attualePosizione = esaDoc.getPosizioneScorrimento() + 1;
			int dimensione = esaDoc.getListaDaScorrere().size();
			if (attualePosizione > dimensione - 1) {

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

			} else {
				try {
					this.loadDatiDocumentoPassato(esaDoc, esaDoc
							.getListaDaScorrere().get(attualePosizione));
				} catch (ValidationException ve) {
					// impostazione nel caso ci sia assenza di risultati (va in
					// errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr
					// ))
					// assenzaRisultati = 4001;
					if (ve.getError() == 4001) {
						// passa indietro perchè l'elemento è stato cancellato
						esaDoc.setPosizioneScorrimento(attualePosizione);
						return scorriAvanti(mapping, form, request, response);
					}
					return mapping.getInputForward();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}

				esaDoc.setPosizioneScorrimento(attualePosizione);
				esaDoc.setDisabilitaTutto(false);

				// aggiornamento del tab di visualizzazione dei dati per tipo
				// ordine
				if (esaDoc.getDatiDocumento() != null
						&& esaDoc.getDatiDocumento()
								.getStatoSuggerimentoDocumento() != null
						&& (esaDoc.getDatiDocumento()
								.getStatoSuggerimentoDocumento().equals("A")
								|| esaDoc.getDatiDocumento()
										.getStatoSuggerimentoDocumento()
										.equals("O") || esaDoc
								.getDatiDocumento()
								.getStatoSuggerimentoDocumento().equals("R"))) {
					esaDoc.setDisabilitaTutto(true);
				} else {
					esaDoc.setDisabilitaTutto(false);
				}

				if (esaDoc.isEsaminaInibito()) {
					esaDoc.setDisabilitaTutto(true);
				}

			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			int attualePosizione = esaDoc.getPosizioneScorrimento() - 1;
			int dimensione = esaDoc.getListaDaScorrere().size();
			if (attualePosizione < 0) {

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

			} else {
				try {
					this.loadDatiDocumentoPassato(esaDoc, esaDoc
							.getListaDaScorrere().get(attualePosizione));
				} catch (ValidationException ve) {
					// impostazione nel caso ci sia assenza di risultati (va in
					// errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr
					// ))
					// assenzaRisultati = 4001;
					if (ve.getError() == 4001) {
						// passa indietro perchè l'elemento è stato cancellato
						esaDoc.setPosizioneScorrimento(attualePosizione);
						return scorriIndietro(mapping, form, request, response);
					}
					return mapping.getInputForward();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}

				esaDoc.setPosizioneScorrimento(attualePosizione);
				esaDoc.setDisabilitaTutto(false);

				if (esaDoc.getDatiDocumento() != null
						&& esaDoc.getDatiDocumento()
								.getStatoSuggerimentoDocumento() != null
						&& (esaDoc.getDatiDocumento()
								.getStatoSuggerimentoDocumento().equals("A")
								|| esaDoc.getDatiDocumento()
										.getStatoSuggerimentoDocumento()
										.equals("O") || esaDoc
								.getDatiDocumento()
								.getStatoSuggerimentoDocumento().equals("R"))) {
					esaDoc.setDisabilitaTutto(true);
				} else {
					esaDoc.setDisabilitaTutto(false);
				}

				if (esaDoc.isEsaminaInibito()) {
					esaDoc.setDisabilitaTutto(true);
				}

			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadDatiDocumentoPassato(EsaminaDocForm esaDoc,
			ListaSuppDocumentoVO criteriRicercaDocumento) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<DocumentoVO> documentoTrovato = new ArrayList();
		documentoTrovato = factory
				.getGestioneAcquisizioni().getRicercaListaDocumenti(
						criteriRicercaDocumento);
		// gestire l'esistenza del risultato e che sia univoco
		esaDoc.setDatiDocumento(documentoTrovato.get(0));
	}

	private boolean modificaDocumento(DocumentoVO documento) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaDocumento(
				documento);
		return valRitorno;
	}

	private ListaSuppDocumentoVO AggiornaTipoVarRisultatiListaSupporto(
			DocumentoVO eleDocumento, ListaSuppDocumentoVO attributo) {
		try {
			if (eleDocumento != null) {
				List<DocumentoVO> risultati = new ArrayList();
				// carica i criteri di ricerca da passare alla esamina
				risultati = attributo.getSelezioniChiamato();
				for (int i = 0; i < risultati.size(); i++) {
					String eleRis = risultati.get(i).getChiave().trim();
					if (eleRis.equals(eleDocumento.getChiave().trim())) {
						// risultati.get(i).setTipoVariazione(eleCambio.getTipoVariazione());
						risultati.get(i).setTipoVariazione("M");

						break;
					}
				}
				attributo.setSelezioniChiamato(risultati);
			}
		} catch (Exception e) {

		}
		return attributo;
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward accetta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			// return mapping.findForward("accetta");
			// cambia stato al messaggio e disabilita tutto
			if (esaDoc.getDatiDocumento().getTitolo() != null
					&& esaDoc.getDatiDocumento().getTitolo().getCodice() != null
					&& esaDoc.getDatiDocumento().getTitolo().getCodice().trim()
							.length() > 0) {
				esaDoc.getDatiDocumento().setStatoSuggerimentoDocumento("A");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.cambioStatoDaSalvare"));
				this.saveErrors(request, errors);

			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(
						"generico",
						new ActionMessage(
								"errors.acquisizioni.suggerimentoerroreTitoloObbligatorio"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			// esaSugg.setDisabilitaTutto(true);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward rifiuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			// return mapping.findForward("rifiuta");
			// cambia stato al messaggio e disabilita tutto
			esaDoc.getDatiDocumento().setStatoSuggerimentoDocumento("R");
			// this.esaSugg.setDisabilitaTutto(true);

			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.acquisizioni.cambioStatoDaSalvare"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward attesa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			// return mapping.findForward("rifiuta");
			// cambia stato al messaggio e disabilita tutto
			esaDoc.getDatiDocumento().setStatoSuggerimentoDocumento("W");
			// this.esaSugg.setDisabilitaTutto(true);

			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.acquisizioni.cambioStatoDaSalvare"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaDocForm esaDoc = (EsaminaDocForm) form;
		try {
			if (esaDoc.getDatiDocumento().getTitolo() != null
					&& esaDoc.getDatiDocumento().getTitolo().getCodice() != null) {
				request.setAttribute("bidFromRicOrd", esaDoc.getDatiDocumento()
						.getTitolo().getCodice());
			}
			return mapping.findForward("sifbid");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadLingue(EsaminaDocForm esaDoc) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		List lista = carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceLingua());
		esaDoc.setListaLingue(lista);

		/*
		 * List lista = new ArrayList();
		 *
		 * StrutturaCombo elem = new StrutturaCombo("",""); lista.add(elem); elem
		 * = new StrutturaCombo("ITA","ITA - Italiano"); lista.add(elem); elem =
		 * new StrutturaCombo("ENG","ENG - Inglese"); lista.add(elem); elem = new
		 * StrutturaCombo("FRE","FRE - Francese"); lista.add(elem); elem = new
		 * StrutturaCombo("JPN","JPN - Giapponese"); lista.add(elem);
		 *
		 * esaDoc.setListaLingue(lista);
		 */}

	private void loadPaesi(EsaminaDocForm esaDoc) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		List lista = carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodicePaese());
		esaDoc.setListaPaesi(lista);

	}

	private void loadStatoSuggerimento(EsaminaDocForm esaDoc) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		esaDoc.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceStatoSuggerimento()));
	}

	private void check(HttpServletRequest request) throws Exception {
		UserVO utente = Navigation.getInstance(request).getUtente();
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		utenteEJB.checkAttivita(
						CodiciAttivita.getIstance().GA_GESTIONE_SUGGERIMENTO_LETTORE,
						utente.getCodPolo(), utente.getCodBib(), null);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		EsaminaDocForm currentForm = (EsaminaDocForm) form;
		DocumentoVO doc = currentForm.getDatiDocumento();
		String stato = doc.getStatoSuggerimentoDocumento();

		try {
			if (ValidazioneDati.equals(idCheck, "STATO_SUGG")) {
				check(request);
				return ValidazioneDati.in(stato, "W");
			}

			if (ValidazioneDati.equals(idCheck, "ricerca.button.salva")) {
				check(request);
			}

			if (ValidazioneDati.equals(idCheck, "ricerca.button.accetta")) {
				check(request);
				return ValidazioneDati.in(stato, "W");
			}

			if (ValidazioneDati.equals(idCheck, "ricerca.button.rifiuta")) {
				check(request);
				return ValidazioneDati.in(stato, "W");
			}

			if (ValidazioneDati.equals(idCheck, "ricerca.button.in_attesa")) {
				check(request);
				return ValidazioneDati.in(stato, "A", "R");
			}

			if (ValidazioneDati.equals(idCheck, "ricerca.button.ripristina")) {
				check(request);
			}

			return true;

		} 	catch (Exception e) {
			log.error("", e);
			return false;
		}

	}

	/*
	 * private void loadPaesi() throws Exception { List lista = new
	 * List(); StrutturaCombo elem = new StrutturaCombo("","");
	 * lista.add(elem); elem = new StrutturaCombo("IT","IT - ITALIA");
	 * lista.add(elem); elem = new StrutturaCombo("GB","GB - Gran Bretagna");
	 * lista.add(elem); elem = new StrutturaCombo("FR","FR - Francia");
	 * lista.add(elem); elem = new StrutturaCombo("JP","JP - Giappone");
	 * lista.add(elem);
	 *
	 * esaDoc.setListaPaesi(lista); }
	 */

	/*
	 * private void loadStatoSuggerimento() throws Exception { List lista =
	 * new ArrayList(); StrutturaCombo elem = new StrutturaCombo("","");
	 * lista.add(elem); elem = new StrutturaCombo("A","A - Accettato");
	 * lista.add(elem); elem = new StrutturaCombo("W","W - Attesa di risposta");
	 * lista.add(elem); elem = new StrutturaCombo("O","O - Ordinato");
	 * lista.add(elem); elem = new StrutturaCombo("R","R - Rifiutato");
	 * lista.add(elem);
	 *
	 * esaDoc.setListaStatoSuggerimento(lista); }
	 */

	/*
	 * private void loadDatiDocumento() throws Exception { List lista = new
	 * List();
	 *
	 * // String codP, String codB, String codDoc, String statoSuggDoc,
	 * StrutturaTerna ute, StrutturaCombo tit, String dataI, String dataA, String
	 * aut, String edi, String luogo, String pae, String lin, String annoEdi,
	 * String noteDoc, String msgXLet) DocumentoVO doc=new DocumentoVO("X10",
	 * "01", "4", "A", new StrutturaTerna("01","05","Vincenzo Bianchi"),new
	 * StrutturaCombo("LO10423457",
	 * "Da Cartesio a Kant / E. Paolo Lamanna. - Firenze : Le Monnier, c1961. - 565 p. ; 23 cm."
	 * ), "03/05/2004", "03/05/2004",
	 * "De Crescenzo Luciano","","Roma","IT","ITA","2000","","");
	 * esaDoc.setDatiDocumento(doc); }
	 */
}
