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
package it.iccu.sbn.web.integration.action.erogazione.prenotazioni;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.integration.action.erogazione.ErogazioneAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.prenotazioni.ListaPrenotazioniForm;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaPrenotazioniAction extends ErogazioneAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esamina",           "esamina");
		map.put("servizi.bottone.cancella",          "cancella");
		map.put("servizi.bottone.respingi",          "rifiuta");
		map.put("servizi.bottone.ordina",            "ordinaLista");
		map.put("servizi.bottone.annulla",           "chiudi");

		map.put("button.blocco",                     "blocco");
		map.put("servizi.bottone.deselTutti",        "deselTutti");
		map.put("servizi.bottone.selTutti",          "selTutti");

		map.put("servizi.bottone.si",                "si");
		map.put("servizi.bottone.no",                "no");

		map.put("servizi.bottone.stampa", "stampa");

		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm)form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setChiamante(request.getParameter("CHIAMANTE"));
				currentForm.setMovimentoSelezionato((MovimentoVO)request.getAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO));
				//almaviva5_20131105 #5426
				this.caricaOrdinamentiPrenotazioni(currentForm, request);
				loadDefault(request, currentForm);
			}

			this.caricaPrenotazioni(request, currentForm);

			if (!ValidazioneDati.isFilled(currentForm.getListaPrenotazioni()) ) {
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.prenotazioniNonPresenti"));
				if (currentForm.getChiamante() != null) {
					return this.backForward(request, true);
				}
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;

		try{
			if (!isTokenValid(request))
				saveToken(request);

			if (ValidazioneDati.isFilled(currentForm.getCodSelSing()) ) {
				resetToken(request);

				MovimentoListaVO prenSelezionata = this.passaSelezionato(currentForm.getListaPrenotazioni(), currentForm.getCodSelSing());

				//almaviva5_20100401
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				MovimentoListaVO movimentoSelezionato = delegate
						.getDettaglioMovimento(prenSelezionata, getLocale(request));

				request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimentoSelezionato);
				request.setAttribute(NavigazioneServizi.PROVENIENZA,         "ListaMov");

				return mapping.findForward("esamina");
			}

			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();

		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		return this.backForward(request, true);
	}


	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (ValidazioneDati.isFilled(currentForm.getCodSel() )) {
				currentForm.setConferma(true);
				currentForm.setRichiesta("Cancella");
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerCancRif"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward rifiuta(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (ValidazioneDati.isFilled(currentForm.getCodSel()) ) {
				currentForm.setConferma(true);
				currentForm.setRichiesta("Rifiuta");
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerCancRif"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			ActionMessages errors = new ActionMessages();

			String firmaUtente = Navigation.getInstance(request).getUtente().getFirmaUtente();
			if (currentForm.getRichiesta().equals("Cancella")) {
				this.cancellaMultipla(Arrays.asList(currentForm.getCodSel()), firmaUtente, request);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
			}

			if (currentForm.getRichiesta().equals("Rifiuta")) {
				this.rifiutaMultipla(Arrays.asList(currentForm.getCodSel()), firmaUtente, request);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceRifiutiEffettuati"));
			}


			this.saveErrors(request, errors);
			currentForm.setConferma(false);

			return this.unspecified(mapping, form, request, response);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			Long[] elemSel = new Long[currentForm.getListaPrenotazioni().size()];
			if (currentForm.getListaPrenotazioni().size() > 0) {
				for (int m = 0; m < currentForm.getListaPrenotazioni().size(); m++) {
					MovimentoVO singMov = currentForm.getListaPrenotazioni().get(m);
					elemSel[m] = new Long(singMov.getCodRichServ());
				}
				currentForm.setCodSel(elemSel);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
									HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			currentForm.setCodSel(null);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (DescrittoreBloccoVO.isFilled(bloccoVO)) {
				int posizioneInserimentoBlocco = 1;
				int progressivoPrimoElementoBloccoCaricato = ((MovimentoListaVO) bloccoVO.getLista().get(0)).getProgr();
				List<MovimentoListaVO> listaDaAggiornare = currentForm.getListaPrenotazioni();
				Iterator<MovimentoListaVO> iterator = listaDaAggiornare.iterator();
				while (iterator.hasNext()) {
					if (progressivoPrimoElementoBloccoCaricato < iterator.next().getProgr())
						break;

					posizioneInserimentoBlocco++;
				}
				if (posizioneInserimentoBlocco > listaDaAggiornare.size())
					currentForm.getListaPrenotazioni().addAll(bloccoVO.getLista());
				else
					currentForm.getListaPrenotazioni()
							.addAll(posizioneInserimentoBlocco - 1,	bloccoVO.getLista());
				currentForm.setBloccoSelezionato(bloccoVO.getNumBlocco());
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;

		String check = null;
		MovimentoListaVO movimento = null;

		request.setAttribute("checkS", currentForm.getCodSelSing());
		check = currentForm.getCodSelSing();
		if (ValidazioneDati.isFilled(check))
			movimento = this.passaSelezionato(currentForm
					.getListaPrenotazioni(), check);

		if (movimento == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.listaUtenti.nessunaSelezionePerStampa"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		// acquisisco il numero copie e il testo della stampa modulo
		// dalla configurazione dell'attività

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		List<IterServizioVO> listaIterServizio = this.getIterServizio(movimento
				.getCodPolo(), movimento.getCodBibOperante(), movimento
				.getCodTipoServ(), request);

		Iterator<IterServizioVO> iterator = listaIterServizio.iterator();
		IterServizioVO iterServizioVO = null;

		while (iterator.hasNext()) {
			iterServizioVO = iterator.next();
			if (iterServizioVO.getCodAttivita().equals(
					movimento.getCodAttivita()))
				break;
		}

		ControlloAttivitaServizio attivita = delegate.getAttivita(movimento
				.getCodPolo(), iterServizioVO);

		// Imposto il numero copie della stampa modulo richiesta
		if (String.valueOf(attivita.getPassoIter().getNumPag()).equals("0")) {
			movimento.setNumeroCopieStampaModulo("1");
		} else {
			movimento.setNumeroCopieStampaModulo(String.valueOf(attivita
					.getPassoIter().getNumPag()));
		}
		// Imposto il testo della stampa modulo richiesta
		movimento.setTestoStampaModulo(attivita.getPassoIter().getTesto());

		request.setAttribute("codBib", currentForm.getBiblioteca());
		request.setAttribute("FUNZIONE_STAMPA", StampaType.STAMPA_RICHIESTA);
		request.setAttribute("DATI_STAMPE_ON_LINE", movimento);
		return mapping.findForward("stampaRichiesta");
	}


	public ActionForward ordinaLista(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			//almaviva5_20131105 #5426
			this.caricaPrenotazioni(request, currentForm);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	private void caricaPrenotazioni(HttpServletRequest request, ListaPrenotazioniForm currentForm)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();

		DescrittoreBloccoVO blocco1 = null;
		MovimentoVO mov = currentForm.getMovimentoSelezionato();
		if (mov != null) {
			mov.setTipoOrdinamento(currentForm.getOrdinamento());
			blocco1 = delegate.getPrenotazioni(mov, utente.getCodBib(), this.getLocale(request, Constants.SBN_LOCALE));
		} else
			blocco1 = delegate.getPrenotazioni(utente.getCodBib(),
					currentForm.getMaxRighe(), utente.getTicket(),
					this.getLocale(request, Constants.SBN_LOCALE),
					currentForm.getOrdinamento());


		if (DescrittoreBloccoVO.isFilled(blocco1) ) {
			navi.getCache().getCurrentElement().setInfoBlocchi(null);
			// abilito i tasti per il blocco se necessario
			currentForm.setAbilitaBlocchi   ((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista          (blocco1.getIdLista());
			currentForm.setTotRighe         (blocco1.getTotRighe());
			currentForm.setTotBlocchi       (blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setMaxRighe         (blocco1.getMaxRighe());
			currentForm.setLivelloRicerca   (utente.getCodBib());
			currentForm.setListaPrenotazioni((blocco1.getLista()));

			if (blocco1.getTotRighe() == 1)
				currentForm.setCodSelSing(((MovimentoListaVO)blocco1.getLista().get(0)).getCodRichServ());

		} else {
			currentForm.setListaPrenotazioni(new ArrayList<MovimentoListaVO>());
		}
	}


	private void caricaOrdinamentiPrenotazioni(ListaPrenotazioniForm form, HttpServletRequest request)
    {
    	ServiziDelegate delegate = ServiziDelegate.getInstance(request);
    	List<ComboCodDescVO> ordinamentiPrenotazioni;
		try {
			ordinamentiPrenotazioni = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_PRENOTAZIONI);
			ordinamentiPrenotazioni = CaricamentoCombo.cutFirst(ordinamentiPrenotazioni);
		} catch (RemoteException e) {
			ordinamentiPrenotazioni = new ArrayList<ComboCodDescVO>();
		} catch (CreateException e) {
			ordinamentiPrenotazioni = new ArrayList<ComboCodDescVO>();
		}

		form.setOrdinamentiPrenotazioni(ordinamentiPrenotazioni);
    }

	private MovimentoListaVO passaSelezionato(List<MovimentoListaVO> listaPrenotazioni, String codRicSer) {
		for (int i = 0; i < listaPrenotazioni.size(); i++) {
			MovimentoListaVO movimento = listaPrenotazioni.get(i);
			if (movimento.getCodRichServ().equals(codRicSer)) {
				return movimento;
			}
		}
		return null;
	}


	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)	throws Exception {
		super.loadDefault(request, form);
		ListaPrenotazioniForm currentForm = (ListaPrenotazioniForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		currentForm.setOrdinamento((String) utenteEjb.getDefault(ConstantDefault.SER_RIC_ORD_LISTA_PRENOTAZIONI));
	}

}
