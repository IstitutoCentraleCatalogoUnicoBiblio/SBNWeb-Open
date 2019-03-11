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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazionePropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaLocalizzazioniView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaProposteDiCorrezioneView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.DettaglioPropostaDiCorrezioneForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class DettaglioPropostaDiCorrezioneAction extends LookupDispatchAction {

	private static Log log = LogFactory
			.getLog(DettaglioPropostaDiCorrezioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.confermaPropostaCorr", "confermaPropostaCorr");
		map.put("button.annullaPropostaCorr", "annullaTornaInter");
		map.put("button.caricaListeDest", "caricaListeDest");

		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioPropostaDiCorrezioneForm propostaDiCorrezioneForm = (DettaglioPropostaDiCorrezioneForm) form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");
		if (request.getAttribute("proposteDiCorrezioneView") != null) {
			propostaDiCorrezioneForm.setProposteDiCorrezioneView((SinteticaProposteDiCorrezioneView) request.getAttribute("proposteDiCorrezioneView"));
			if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdProposta() == 0) {
				propostaDiCorrezioneForm.setTipoProspettazione("INS");
			} else {
				if (request.getAttribute("tipoOperazione") != null && ((String) request.getAttribute("tipoOperazione")).equals("DETTAGLIO")) {
					propostaDiCorrezioneForm.setTipoProspettazione("DET");
					propostaDiCorrezioneForm.setListaDestinatariProp(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getListaDestinatariProp());
				} else {

					propostaDiCorrezioneForm.setTipoProspettazione("MOD");
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String dataOdierna = df.format(calendar.getTime());
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setDestinatariData(dataOdierna);
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setDestinatariBiblio(
							Navigation.getInstance(request).getUtente().getCodPolo()
							+ Navigation.getInstance(request).getUtente().getCodBib() + " "
							+ Navigation.getInstance(request).getUtente().getUserId());
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setDestinatariNote("");
					propostaDiCorrezioneForm.setListaDestinatariProp(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getListaDestinatariProp());
				}
			}
		}

		ComboCodDescVO comboCodDescVO;
		// Predisposizione lista Stati Proposta

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("I");
		comboCodDescVO.setDescrizione("inserita");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("E");
		comboCodDescVO.setDescrizione("evasa");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("R");
		comboCodDescVO.setDescrizione("respinta");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		String messaggio = caricaListe(request, propostaDiCorrezioneForm);
		if (!messaggio.equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(messaggio));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}


	public String caricaListe (HttpServletRequest request, DettaglioPropostaDiCorrezioneForm propostaDiCorrezioneForm) throws RemoteException, NamingException, CreateException {

		ComboCodDescVO comboCodDescVO;

		if (propostaDiCorrezioneForm.getTipoProspettazione().equals("INS")) {
			// Predisposizione lista destinatari (vengono caricati tutti i Polo/biblio per cui esiste localizzazione per Gestione)
			comboCodDescVO = new ComboCodDescVO();
			comboCodDescVO.setCodice("");
			comboCodDescVO.setDescrizione("");
			propostaDiCorrezioneForm.addListaBibliotecari(comboCodDescVO);

			if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto() != null
				&& !propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto().equals("")) {
				// CHIAMATA ALL'EJB DI INTERROGAZIONE
				AreaDatiLocalizzazioniAuthorityVO areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();
				areaDatiPass.setIdLoc(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto());
				areaDatiPass.setTipoMat(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoMateriale());
				areaDatiPass.setNatura(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getNatura());
				areaDatiPass.setAuthority(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoAuthority());
				areaDatiPass.setIndice(true);
				areaDatiPass.setPolo(false);

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
						.getGestioneBibliografica().cercaLocalizzazioni(areaDatiPass, false, Navigation.getInstance(request).getUserTicket());

				if (areaDatiPassReturn == null) {
					return "errors.gestioneBibliografica.noConnessione";
				}

				if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {
					return "errors.gestioneBibliografica.testoProtocollo" + areaDatiPassReturn.getTestoProtocollo();
				} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					return "errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr();
				}

				if (areaDatiPassReturn.getNumNotizie() == 0) {
					return "errors.gestioneBibliografica.titNotFound";
				}

				SinteticaLocalizzazioniView eleSinteticaLocalizzazioniView = null;
				for (int i = 0; i < areaDatiPassReturn.getListaSintetica().size(); i++) {
					eleSinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) areaDatiPassReturn.getListaSintetica().get(i);
					comboCodDescVO = new ComboCodDescVO();
					comboCodDescVO.setCodice(eleSinteticaLocalizzazioniView.getIDSbn());
					comboCodDescVO.setDescrizione(" - " + eleSinteticaLocalizzazioniView.getDescrBiblioteca());
					propostaDiCorrezioneForm.addListaBibliotecari(comboCodDescVO);
				}
			}
		}

		if (propostaDiCorrezioneForm.getTipoProspettazione().equals("MOD")) {
			// Predisposizione lista destinatari (vengono caricati tutti i Polo/biblio per cui esiste localizzazione per Gestione)
			comboCodDescVO = new ComboCodDescVO();
			comboCodDescVO.setCodice(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getDestinatariBiblio());
			comboCodDescVO.setDescrizione("");
			propostaDiCorrezioneForm.addListaBibliotecari(comboCodDescVO);
		}

		return "";
	}


	public ActionForward caricaListeDest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioPropostaDiCorrezioneForm propostaDiCorrezioneForm = (DettaglioPropostaDiCorrezioneForm) form;

		String messaggio = caricaListe(request, propostaDiCorrezioneForm);
		if (!messaggio.equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(messaggio));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}


	public ActionForward confermaPropostaCorr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioPropostaDiCorrezioneForm propostaDiCorrezioneForm = (DettaglioPropostaDiCorrezioneForm) form;

		if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTesto() == null
				|| propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTesto().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoPropostaObbligatorio"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		// BUG MANTIS 3454 - 12.01.2010 almaviva2 allungato campo per risposta e inserito controllo a 320 caratteri
		if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getDestinatariNote() != null &&
				propostaDiCorrezioneForm.getProposteDiCorrezioneView().getDestinatariNote().length() > 320) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaPropostaTroppoLunga"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}


		if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoMateriale() == null
			&& propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoAuthority() == null) {
				if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto().substring(3, 4).equals("V")) {
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setTipoAuthority("AU");
				} else if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto().substring(3, 4).equals("M")) {
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setTipoAuthority("MA");
				} else if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto().substring(3, 4).equals("L")) {
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setTipoAuthority("LU");
				} else {
					propostaDiCorrezioneForm.getProposteDiCorrezioneView().setTipoMateriale("");
				}
		}



		propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO().setProposteDiCorrezioneView(propostaDiCorrezioneForm.getProposteDiCorrezioneView());
		if (propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO().getProposteDiCorrezioneView().getIdProposta() > 0) {
			propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO().setModifica(true);
		} else {
			propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO().setModifica(false);
		}

		propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO().setCodErr("0000");
		propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO().setTestoProtocollo("");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiVariazionePropostaDiCorrezioneVO areaDatiVariazionePropostaDiCorrezioneVOReturn = factory.getGestioneBibliografica().inserisciPropostaDiCorrezione(
				propostaDiCorrezioneForm.getAreaDatiVariazionePropostaDiCorrezioneVO(), Navigation.getInstance(request).getUserTicket());

		if (areaDatiVariazionePropostaDiCorrezioneVOReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiVariazionePropostaDiCorrezioneVOReturn.getCodErr().equals("9999") || areaDatiVariazionePropostaDiCorrezioneVOReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo" ,areaDatiVariazionePropostaDiCorrezioneVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiVariazionePropostaDiCorrezioneVOReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."
							+ areaDatiVariazionePropostaDiCorrezioneVOReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		// Inizio intervento BUG MANTIS 4500 collaudo - si reinterroga la sintetica della proposta di correzione appena aggiornata
		// così da mostrare le modifiche effettuata
		AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVO = new  AreaDatiPropostaDiCorrezioneVO();

		areaDatiPropostaDiCorrezioneVO.setIdOggettoProposta(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getIdOggetto());
		areaDatiPropostaDiCorrezioneVO.setMittenteProposta("");

		areaDatiPropostaDiCorrezioneVO.setTipoMaterialeProposta(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoMateriale());
		areaDatiPropostaDiCorrezioneVO.setNaturaProposta(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getNatura());
		if (propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoAuthority() == null) {
			areaDatiPropostaDiCorrezioneVO.setTipoAuthorityProposta("");
		} else {
			areaDatiPropostaDiCorrezioneVO.setTipoAuthorityProposta(propostaDiCorrezioneForm.getProposteDiCorrezioneView().getTipoAuthority().toString());
		}

		AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVOReturn = factory.getGestioneBibliografica().cercaPropostaDiCorrezione(
				areaDatiPropostaDiCorrezioneVO, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPropostaDiCorrezioneVOReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPropostaDiCorrezioneVOReturn.getCodErr().equals("9999") || areaDatiPropostaDiCorrezioneVOReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo" ,areaDatiPropostaDiCorrezioneVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPropostaDiCorrezioneVOReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica." + areaDatiPropostaDiCorrezioneVOReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setListaSintetica(areaDatiPropostaDiCorrezioneVOReturn.getListaSintetica());
		request.setAttribute("areaDatiPropostaDiCorrezioneVO", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO());
		request.setAttribute("PresenzaTastoInsRisposta", "NO");

		// Fine intervento BUG MANTIS 4500

		request.setAttribute("vaiA", "SI");
		request.setAttribute("bid", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getIdOggettoProposta());
		request.setAttribute("livRicerca", "I");
		ActionMessages errors = new ActionMessages();
		errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		request.setAttribute("messaggio", "operOk");
		return Navigation.getInstance(request).goBack();



	}


	public ActionForward annullaTornaInter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioPropostaDiCorrezioneForm propostaDiCorrezioneForm = (DettaglioPropostaDiCorrezioneForm) form;


//		 Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute("bid", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getIdOggettoProposta());
		request.setAttribute("livRicerca", "I");
		return Navigation.getInstance(request).goBack();

	}




}
