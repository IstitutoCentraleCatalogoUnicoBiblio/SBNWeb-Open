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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiVariazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.DettaglioLuogoForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class DettaglioLuogoAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.annulla", "annullaOper");
		map.put("button.ok", "confermaOper");

		map.put("button.canRepertorio", "canRepertorio");
		map.put("button.insRepertorio", "insRigaVuotaRep");
		map.put("button.hlpRepertorio", "hlpRepertorio");

		return map;
	}

	private void loadDefault(HttpServletRequest request,DettaglioLuogoForm dettaglioLuogoForm) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			dettaglioLuogoForm.getDettLuoComVO().setLivAut((String)utenteEjb.getDefault(ConstantDefault.CREA_LUO_LIVELLO_AUTORITA));
			dettaglioLuogoForm.getDettLuoComVO().setPaese((String)utenteEjb.getDefault(ConstantDefault.CREA_LUO_PAESE));
		} catch (DefaultNotFoundException e) {}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// commento di test

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		DettaglioLuogoForm dettaglioLuogoForm = (DettaglioLuogoForm) form;

		// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
		// nota informativa , nota catalogatore e legame a repertori
		if (request.getParameter("findREPERT") != null) {
			return mapping.findForward("cercaRepertori");
		}
		if (request.getAttribute("sigl") != null) {
			this.insNuovoRepertorio(request, form);
			return mapping.getInputForward();
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (request.getAttribute("bidPerRientroAnalitica") != null) {
			dettaglioLuogoForm.setBidPerRientroAnalitica((String) request.getAttribute("bidPerRientroAnalitica"));
		}
		dettaglioLuogoForm.setDettLuoComVO((DettaglioLuogoGeneraleVO) request.getAttribute("dettaglioLuo"));

		// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
		// nota informativa , nota catalogatore e legame a repertori
		if (!ValidazioneDati.strIsNull((String) request.getAttribute("tipoProspettazione"))) {
			dettaglioLuogoForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));
		}
		// dettaglioLuogoForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));


		if (dettaglioLuogoForm.getTipoProspettazione().equals("INS")) {
			dettaglioLuogoForm.getDettLuoComVO().setLid("");
			dettaglioLuogoForm.getDettLuoComVO().setForma("A");
			this.loadDefault(request, dettaglioLuogoForm);
			if (request.getParameter("CONDIVISO") != null) {
				if (request.getParameter("CONDIVISO").equals("TRUE")) {
					dettaglioLuogoForm.setFlagCondiviso(true);
				} else {
					dettaglioLuogoForm.setFlagCondiviso(false);
				}
			}
			navi.setTesto("Crea");
		}

		if (dettaglioLuogoForm.getTipoProspettazione().equals("AGG")) {
			if (request.getAttribute("flagCondiviso") != null) {
				if ((Boolean) request.getAttribute("flagCondiviso")) {
					dettaglioLuogoForm.setFlagCondiviso(true);
				} else {
					dettaglioLuogoForm.setFlagCondiviso(false);
				}
			}
			navi.setTesto("Varia");
		}

		if (dettaglioLuogoForm.getTipoProspettazione().equals("DET")) {
			this.caricaDescrizioni(dettaglioLuogoForm);
		} else if (dettaglioLuogoForm.getTipoProspettazione().equals("INS")
				|| dettaglioLuogoForm.getTipoProspettazione().equals("AGG")) {
			this.caricaComboGenerali(dettaglioLuogoForm);
		}

		if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
			dettaglioLuogoForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));
		} else {
			dettaglioLuogoForm.setAreaDatiLegameTitoloVO(null);
		}

		return mapping.getInputForward();
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

//		// Viene settato il token per le transazioni successive
//		this.saveToken(request);
//		return Navigation.getInstance(request).goBack(
//				mapping.findForward("annulla"), true);

		DettaglioLuogoForm dettaglioLuogoForm = (DettaglioLuogoForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute("bid", dettaglioLuogoForm.getDettLuoComVO().getLid());
		request.setAttribute("livRicerca", "I");
		return Navigation.getInstance(request).goBack();
	}

	private void caricaDescrizioni(DettaglioLuogoForm dettaglioLuogoForm)
			throws RemoteException, CreateException, NamingException {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioLuogoForm.getDettLuoComVO().getRelatorCode() != null) {
			dettaglioLuogoForm.setDescRelatorCode(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioLuogoForm.getDettLuoComVO()
									.getRelatorCode(),
							CodiciType.CODICE_LEGAME_TITOLO_LUOGO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
			dettaglioLuogoForm.getDettLuoComVO().setRelatorCode(
					factory.getCodici().unimarcToSBN(
							CodiciType.CODICE_LEGAME_TITOLO_LUOGO,
							dettaglioLuogoForm.getDettLuoComVO()
									.getRelatorCode()));
		} else {
			dettaglioLuogoForm.setDescRelatorCode("");
		}

		if (dettaglioLuogoForm.getDettLuoComVO().getLivAut() != null) {
			dettaglioLuogoForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioLuogoForm.getDettLuoComVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioLuogoForm.setDescLivAut("");
		}

		if (dettaglioLuogoForm.getDettLuoComVO().getForma() != null) {
			dettaglioLuogoForm.setDescFormaNome(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioLuogoForm.getDettLuoComVO().getForma(),
							CodiciType.CODICE_FORMA_AUTORE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioLuogoForm.setDescFormaNome("");
		}

		if (dettaglioLuogoForm.getDettLuoComVO().getPaese() != null) {
			dettaglioLuogoForm.setDescPaese(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioLuogoForm.getDettLuoComVO().getPaese(),
							CodiciType.CODICE_PAESE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioLuogoForm.setDescPaese("");
		}

	}


	private void caricaComboGenerali(DettaglioLuogoForm dettaglioLuogoForm)
	throws RemoteException, CreateException, NamingException {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioLuogoForm.getDettLuoComVO().getLivAut() != null) {
			dettaglioLuogoForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioLuogoForm.getDettLuoComVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioLuogoForm.setDescLivAut("");
		}

		CaricamentoCombo carCombo = new CaricamentoCombo();
		dettaglioLuogoForm.setListaLivAut(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceLivelloAutorita()));
		dettaglioLuogoForm.setListaPaese(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodicePaese()));

	}


	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioLuogoForm dettaglioLuogoForm = (DettaglioLuogoForm) form;

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;
		AreaDatiVariazioneLuogoVO areaDatiPass = new AreaDatiVariazioneLuogoVO();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPass.setDettLuogoVO(dettaglioLuogoForm.getDettLuoComVO());

		//evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
		// nota informativa , nota catalogatore e legame a repertori
		List<TabellaNumSTDImpronteVO> listaUno = dettaglioLuogoForm.getListaRepertoriModificato();
		List<TabellaNumSTDImpronteAggiornataVO> listaDue = new ArrayList<TabellaNumSTDImpronteAggiornataVO>();
		for (TabellaNumSTDImpronteVO tabRepert : listaUno) {
			TabellaNumSTDImpronteAggiornataVO tabRepertAgg = new TabellaNumSTDImpronteAggiornataVO();
			tabRepertAgg.setCampoUno(tabRepert.getCampoUno());
			tabRepertAgg.setCampoDue(tabRepert.getCampoDue());
			tabRepertAgg.setDescCampoDue(tabRepert.getDescCampoDue());
			tabRepertAgg.setNota(tabRepert.getNota());
			listaDue.add(tabRepertAgg);
		}
		areaDatiPass.getDettLuogoVO().setListaRepertoriNew(listaDue);




		if (dettaglioLuogoForm.getDettLuoComVO().getLid().equals("")) {
			areaDatiPass.setModifica(false);
		} else {
			areaDatiPass.setModifica(true);
		}

		// salvataggio delle aree prospettate in dettglio per verificare se sono
		// state effettuate modifiche;

		areaDatiPass.setVariazione(true);

		areaDatiPass.setFlagCondiviso(dettaglioLuogoForm.isFlagCondiviso());
		if (dettaglioLuogoForm.isFlagCondiviso()) {
			areaDatiPass.setInserimentoIndice(true);
			areaDatiPass.setInserimentoPolo(false);
		} else {
			areaDatiPass.setInserimentoIndice(false);
			areaDatiPass.setInserimentoPolo(true);
		}


		// Inizio Evolutive Google3 30.01.2014
		// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
		// la creazione/modifica di oggeti solo su locale
		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			if (dettaglioLuogoForm.isFlagCondiviso()) {
				if (utenteEjb.isAuthoritySoloLocale(SbnAuthority.LU)) {
					throw new UtenteNotAuthorizedException();
				}
			}
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		// Fine Evolutive Google3 30.01.2014


		try {
			areaDatiPassReturn = factory.getGestioneBibliografica()
					.inserisciLuogo(areaDatiPass, Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>"
					+ e.getMessage()));
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(
					mapping.findForward("annulla"), true);
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(
					mapping.findForward("annulla"), true);
		}
		if (areaDatiPassReturn.getCodErr().equals("0000")) {

			if (areaDatiPassReturn.getNumNotizie() > 0) {
				AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassSintetica = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
				areaDatiPassSintetica.setListaSimili(true);
				areaDatiPassSintetica.setIdLista(areaDatiPassReturn.getIdLista());
				areaDatiPassSintetica.setListaSintetica(areaDatiPassReturn.getListaSintetica());
				areaDatiPassSintetica.setMaxRighe(areaDatiPassReturn.getMaxRighe());
				areaDatiPassSintetica.setNumBlocco(areaDatiPassReturn.getNumBlocco());
				areaDatiPassSintetica.setNumNotizie(areaDatiPassReturn.getNumNotizie());
				areaDatiPassSintetica.setNumPrimo(areaDatiPassReturn.getNumPrimo());
				areaDatiPassSintetica.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
				areaDatiPassSintetica.setTotRighe(areaDatiPassReturn.getTotRighe());
				request.setAttribute("areaDatiPassReturnSintetica",	areaDatiPassSintetica);

				areaDatiPass.setBidTemporaneo(areaDatiPassReturn.getBidTemporaneo());
				request.setAttribute("areaDatiPassPerConfVariazione", areaDatiPass);

//				===================================================================
				// Caso di simili in fase di creazione di un legame a titolo
				// prospetto l'elenco dei simili ma imposto le aree per il legame
				if (dettaglioLuogoForm.getAreaDatiLegameTitoloVO() != null) {
					if (dettaglioLuogoForm.getAreaDatiLegameTitoloVO().getBidPartenza() != null) {
						dettaglioLuogoForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
						dettaglioLuogoForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("LU");
						dettaglioLuogoForm.getAreaDatiLegameTitoloVO().setDescArrivo(dettaglioLuogoForm.getDettLuoComVO().getDenomLuogo());
						dettaglioLuogoForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(dettaglioLuogoForm.isFlagCondiviso());
						request.setAttribute("AreaDatiLegameTitoloVO", dettaglioLuogoForm.getAreaDatiLegameTitoloVO());
					}
				}
				//===================================================================

				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaSimili"));
			}



			if (dettaglioLuogoForm.getAreaDatiLegameTitoloVO() == null) {

				if (dettaglioLuogoForm.getBidPerRientroAnalitica() != null) {
					request.setAttribute("bid", dettaglioLuogoForm.getBidPerRientroAnalitica());
				} else {
					request.setAttribute("bid", areaDatiPassReturn.getBid());
				}

				if (dettaglioLuogoForm.isFlagCondiviso()) {
					request.setAttribute("livRicerca", "I");
				} else {
					request.setAttribute("livRicerca", "P");
				}

				request.setAttribute("vaiA", "NO");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).purgeThis();
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			} else {
				dettaglioLuogoForm.getAreaDatiLegameTitoloVO().setIdArrivo(
						areaDatiPassReturn.getBid());
				dettaglioLuogoForm.getAreaDatiLegameTitoloVO()
						.setAuthorityOggettoArrivo("LU");
				dettaglioLuogoForm.getAreaDatiLegameTitoloVO().setDescArrivo(
						areaDatiPass.getDettLuogoVO().getDenomLuogo());
				dettaglioLuogoForm.getAreaDatiLegameTitoloVO()
						.setFlagCondivisoArrivo(true);
				request.setAttribute("AreaDatiLegameTitoloVO",
						dettaglioLuogoForm.getAreaDatiLegameTitoloVO());
				if (dettaglioLuogoForm.getAreaDatiLegameTitoloVO()
						.getAuthorityOggettoPartenza() == null) {
					return mapping.findForward("gestioneLegameTitoloLuogo");
				}
			}
		}
		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}


	// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità
	// di gestire i campi nota informativa , nota catalogatore e legame a repertori
	public ActionForward insRigaVuotaRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioLuogoForm dettaglioLuogoForm = (DettaglioLuogoForm) form;

		dettaglioLuogoForm.addListaRepertoriModificato(new TabellaNumSTDImpronteVO());

		List<TabellaNumSTDImpronteVO> lista = new ArrayList<TabellaNumSTDImpronteVO>();
		for (int i=0; i<dettaglioLuogoForm.getListaRepertoriModificato().size(); i++) {
			TabellaNumSTDImpronteVO tabRepert = dettaglioLuogoForm.getListaRepertoriModificato().get(i);
			if (ValidazioneDati.strIsEmpty(tabRepert.getCampoDue()) )
				tabRepert.setCampoUno("Si");

			lista.add(tabRepert);
		}
		dettaglioLuogoForm.setListaRepertoriModificato(lista);
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	private void insNuovoRepertorio(HttpServletRequest request, ActionForm form) {

		DettaglioLuogoForm dettaglioLuogoForm = (DettaglioLuogoForm) form;

		List<TabellaNumSTDImpronteVO> lista = dettaglioLuogoForm.getListaRepertoriModificato();

		for (TabellaNumSTDImpronteVO tabRepert : lista) {
			if (ValidazioneDati.strIsEmpty(tabRepert.getCampoDue()) ) {
				tabRepert.setCampoDue((String) request.getAttribute("sigl"));
				break;
			}
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return;
	}

	public ActionForward hlpRepertorio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("cercaRepertori");
	}

	public ActionForward canRepertorio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioLuogoForm dettaglioLuogoForm = (DettaglioLuogoForm) form;

		if (dettaglioLuogoForm.getSelezRadioRepertori() == null
				|| dettaglioLuogoForm.getSelezRadioRepertori().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblRepertorio"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioLuogoForm
				.getSelezRadioRepertori());
		dettaglioLuogoForm.getListaRepertoriModificato().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

}
