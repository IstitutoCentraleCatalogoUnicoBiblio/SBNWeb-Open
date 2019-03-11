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

package it.iccu.sbn.web.actions.gestionebibliografica.autore;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.web.actionforms.gestionebibliografica.autore.DettaglioAutoreForm;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class DettaglioAutoreAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(DettaglioAutoreAction.class);

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


	private void loadDefault(HttpServletRequest request,DettaglioAutoreForm dettaglioAutoreForm) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			DettaglioAutoreGeneraleVO dettAutoreVO = dettaglioAutoreForm.getDettAutoreVO();
			dettAutoreVO.setLivAut((String)utenteEjb.getDefault(ConstantDefault.CREA_AUT_LIVELLO_AUTORITA));
			dettAutoreVO.setTipoNome((String)utenteEjb.getDefault(ConstantDefault.CREA_AUT_TIPO_NOME));
		} catch (DefaultNotFoundException e) {}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			return mapping.getInputForward();
		}

		DettaglioAutoreForm dettaglioAutoreForm = (DettaglioAutoreForm) form;

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
			dettaglioAutoreForm.setBidPerRientroAnalitica((String) request.getAttribute("bidPerRientroAnalitica"));
		}

		dettaglioAutoreForm.setDettAutoreVO((DettaglioAutoreGeneraleVO) request
				.getAttribute("dettaglioAut"));

		if (((String) request.getAttribute("tipoProspettazione")).equals("AGGCOND")) {
			dettaglioAutoreForm.setTipoProspettazione("DET");
			dettaglioAutoreForm.setAggiornaFlagCondiviso("SI");
		} else {
			dettaglioAutoreForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));
			dettaglioAutoreForm.setAggiornaFlagCondiviso("NO");
		}

		List<TabellaNumSTDImpronteOriginarioVO> listaUno = dettaglioAutoreForm.getDettAutoreVO().getListaRepertori();
		List<TabellaNumSTDImpronteVO> listaDue = new ArrayList<TabellaNumSTDImpronteVO>();
		for (TabellaNumSTDImpronteOriginarioVO tabRepert : listaUno) {
			TabellaNumSTDImpronteVO tabRepertAgg = new TabellaNumSTDImpronteVO();
			tabRepertAgg.setCampoUno(tabRepert.getCampoUno());
			tabRepertAgg.setCampoDue(tabRepert.getCampoDue());
			tabRepertAgg.setDescCampoDue(tabRepert.getDescCampoDue());
			tabRepertAgg.setNota(tabRepert.getNota());
			listaDue.add(tabRepertAgg);
		}
		dettaglioAutoreForm.setListaRepertoriModificato(listaDue);

		if (dettaglioAutoreForm.getTipoProspettazione().equals("INS"))  {
			dettaglioAutoreForm.getDettAutoreVO().setVid("");
			//almaviva5_20170712
			//dettaglioAutoreForm.getDettAutoreVO().setNorme("RICA");
			dettaglioAutoreForm.getDettAutoreVO().setNorme("REICAT");
			dettaglioAutoreForm.getDettAutoreVO().setForma("A");
			dettaglioAutoreForm.getDettAutoreVO().setFontePaese("IT");
			dettaglioAutoreForm.getDettAutoreVO().setFonteAgenzia("ICCU");
			dettaglioAutoreForm.getDettAutoreVO().setNotaAlNome("");
			this.loadDefault(request, dettaglioAutoreForm);
			if (request.getParameter("CONDIVISO") != null) {
				if (request.getParameter("CONDIVISO").equals("TRUE")) {
					dettaglioAutoreForm.setFlagCondiviso(true);
				} else {
					dettaglioAutoreForm.setFlagCondiviso(false);
				}
			}
			navi.setTesto("Crea");
		}

		if (dettaglioAutoreForm.getTipoProspettazione().equals("AGG")) {
			if (request.getAttribute("flagCondiviso") != null) {
				if ((Boolean) request.getAttribute("flagCondiviso")) {
					dettaglioAutoreForm.setFlagCondiviso(true);
				} else {
					dettaglioAutoreForm.setFlagCondiviso(false);
				}
			}
			navi.setTesto("Varia");
		}


		if (dettaglioAutoreForm.getTipoProspettazione().equals("DET")) {
			this.caricaDescrizioni(dettaglioAutoreForm);
		} else if (dettaglioAutoreForm.getTipoProspettazione().equals("INS")
				|| dettaglioAutoreForm.getTipoProspettazione().equals("AGG")) {
			this.caricaComboGenerali(dettaglioAutoreForm);
		}

		if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
			dettaglioAutoreForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));
		}  else {
			dettaglioAutoreForm.setAreaDatiLegameTitoloVO(null);
		}


		return mapping.getInputForward();
	}

	private void caricaDescrizioni(DettaglioAutoreForm dettaglioAutoreForm)
			throws RemoteException, CreateException, NamingException {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioAutoreForm.getDettAutoreVO().getLivAut() != null) {
			dettaglioAutoreForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioAutoreForm.getDettAutoreVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescLivAut("");
		}

		if (dettaglioAutoreForm.getDettAutoreVO().getLingua() != null) {
			dettaglioAutoreForm.setDescLingua1(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioAutoreForm.getDettAutoreVO().getLingua(),
							CodiciType.CODICE_LINGUA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescLingua1("");
		}
		if (dettaglioAutoreForm.getDettAutoreVO().getPaese() != null) {
			dettaglioAutoreForm.setDescPaese(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioAutoreForm.getDettAutoreVO().getPaese(),
							CodiciType.CODICE_PAESE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescPaese("");
		}
		if (dettaglioAutoreForm.getDettAutoreVO().getTipoNome() != null) {
			dettaglioAutoreForm.setDescTipoNome(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioAutoreForm.getDettAutoreVO().getTipoNome(),
							CodiciType.CODICE_TIPO_AUTORE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescTipoNome("");
		}

		if (dettaglioAutoreForm.getDettAutoreVO().getForma() != null) {
			dettaglioAutoreForm.setDescFormaNome(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioAutoreForm.getDettAutoreVO().getForma(),
							CodiciType.CODICE_FORMA_AUTORE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescFormaNome("");
		}


		if (dettaglioAutoreForm.getDettAutoreVO().getRelatorCode() == null) {
			dettaglioAutoreForm.getDettAutoreVO().setRelatorCode("");
			dettaglioAutoreForm.setDescRelatorCode("");
		}

		if (dettaglioAutoreForm.getDettAutoreVO().getTipoLegameCastor() == null) {
			dettaglioAutoreForm.getDettAutoreVO().setTipoLegameCastor("");
		}

		if (dettaglioAutoreForm.getDettAutoreVO().getTipoLegameCastor() != null) {
			dettaglioAutoreForm.setAppoTipoLegameCastor(factory.getCodici().unimarcToSBN(CodiciType.CODICE_LEGAME_AUTORE_AUTORE,
					dettaglioAutoreForm.getDettAutoreVO().getTipoLegameCastor()));
			dettaglioAutoreForm.setDescTipoLegameCastor(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioAutoreForm.getDettAutoreVO().getTipoLegameCastor(),
							CodiciType.CODICE_LEGAME_AUTORE_AUTORE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescTipoLegameCastor("");
		}

		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
		// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
		//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
		if (dettaglioAutoreForm.getDettAutoreVO().getSpecStrumVoci() != null) {
			dettaglioAutoreForm.setDescSpecStrumVoci(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioAutoreForm.getDettAutoreVO().getSpecStrumVoci(),
							CodiciType.CODICE_STRUMENTI_VOCI_ORGANICO, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		}

	}

	private void caricaComboGenerali(DettaglioAutoreForm dettaglioAutoreForm)
			throws RemoteException, CreateException, NamingException {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioAutoreForm.getDettAutoreVO().getLivAut() != null) {
			dettaglioAutoreForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioAutoreForm.getDettAutoreVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioAutoreForm.setDescLivAut("");
		}

		CaricamentoCombo carCombo = new CaricamentoCombo();
		dettaglioAutoreForm.setListaLivAut(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceLivelloAutorita()));
		dettaglioAutoreForm.setListaLingua1(carCombo
				.loadComboCodiciDesc(factory.getCodici().getCodiceLingua()));
		dettaglioAutoreForm.setListaPaese(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodicePaese()));
		dettaglioAutoreForm.setListaTipoNome(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceTipoAutore()));

		// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
		// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
		// In creazione il default è REICAT.
		// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
		// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
//		if (detTitoloPFissaVO.getNorme() == null) {
//			detTitoloPFissaVO.setNorme("RICA");
//		}
		dettaglioAutoreForm.setListaNormaCatalografiche(carCombo.loadListaNormeCatalografiche());

	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioAutoreForm dettaglioAutoreForm = (DettaglioAutoreForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute("bid", dettaglioAutoreForm.getDettAutoreVO().getVid());
		request.setAttribute("livRicerca", "I");
		//return	Navigation.getInstance(request).goBack(mapping.findForward("annulla"), true);
		return Navigation.getInstance(request).goBack();
	}

	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioAutoreForm dettaglioAutoreForm = (DettaglioAutoreForm) form;

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;
		AreaDatiVariazioneAutoreVO areaDatiPass = new AreaDatiVariazioneAutoreVO();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPass.setDettAutoreVO(dettaglioAutoreForm.getDettAutoreVO());


		List<TabellaNumSTDImpronteVO> listaUno = dettaglioAutoreForm.getListaRepertoriModificato();
		List<TabellaNumSTDImpronteAggiornataVO> listaDue = new ArrayList<TabellaNumSTDImpronteAggiornataVO>();
		for (TabellaNumSTDImpronteVO tabRepert : listaUno) {
			TabellaNumSTDImpronteAggiornataVO tabRepertAgg = new TabellaNumSTDImpronteAggiornataVO();
			tabRepertAgg.setCampoUno(tabRepert.getCampoUno());
			tabRepertAgg.setCampoDue(tabRepert.getCampoDue());
			tabRepertAgg.setDescCampoDue(tabRepert.getDescCampoDue());
			tabRepertAgg.setNota(tabRepert.getNota());
			listaDue.add(tabRepertAgg);
		}
		areaDatiPass.getDettAutoreVO().setListaRepertoriNew(listaDue);

//		 TIPO NOME
		if (areaDatiPass.getDettAutoreVO().getTipoNome() == null ||
				areaDatiPass.getDettAutoreVO().getTipoNome().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.ins040"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		areaDatiPass.setConferma(false);
		if (dettaglioAutoreForm.getDettAutoreVO().getVid().equals("")) {
			areaDatiPass.setModifica(false);
		} else {
			areaDatiPass.setModifica(true);
		}

		boolean test;
		if (dettaglioAutoreForm.getAggiornaFlagCondiviso().equals("SI")) {
			areaDatiPass.setConferma(true);
			areaDatiPass.setVariazione(true);

		} else {
			test = dettaglioAutoreForm.getDettAutoreVOOLD().equals(dettaglioAutoreForm.getDettAutoreVO());
			log.info("VO non modificato: " + test);
			areaDatiPass.setVariazione(!test);
		}



		areaDatiPass.setFlagCondiviso(dettaglioAutoreForm.isFlagCondiviso());
		if (dettaglioAutoreForm.isFlagCondiviso()) {
			if (dettaglioAutoreForm.getAggiornaFlagCondiviso().equals("SI")) {
				areaDatiPass.setInserimentoIndice(false);
				areaDatiPass.setInserimentoPolo(true);
			} else {
				areaDatiPass.setInserimentoIndice(true);
				areaDatiPass.setInserimentoPolo(false);
			}
		} else {
			areaDatiPass.setInserimentoIndice(false);
			areaDatiPass.setInserimentoPolo(true);
		}


		// Inizio Evolutive Google3 30.01.2014
		// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
		// la creazione/modifica di oggeti solo su locale
		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			if (dettaglioAutoreForm.isFlagCondiviso()) {
				if (utenteEjb.isAuthoritySoloLocale(SbnAuthority.AU)) {
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
					.inserisciAutore(areaDatiPass, Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
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

				// almaviva2 BUG MANTIS 4236 - inserito il livello di ricerca per la prospettazione corretta sulla sintetica simili trovati
				// nella variazione;
				areaDatiPassSintetica.setLivelloTrovato(areaDatiPassReturn.getLivelloTrovato());


				request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassSintetica);

				areaDatiPass.setBidTemporaneo(areaDatiPassReturn.getBidTemporaneo());
				request.setAttribute("areaDatiPassPerConfVariazione", areaDatiPass);

				request.setAttribute("AreaDatiLegameTitoloVO", dettaglioAutoreForm.getAreaDatiLegameTitoloVO());

				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaSimili"));
			}

			if (dettaglioAutoreForm.getAreaDatiLegameTitoloVO() == null) {

				if (dettaglioAutoreForm.getBidPerRientroAnalitica() != null) {
					request.setAttribute("bid", dettaglioAutoreForm.getBidPerRientroAnalitica());
				} else {
					request.setAttribute("bid", areaDatiPassReturn.getBid());
				}

				if (dettaglioAutoreForm.isFlagCondiviso()) {
					request.setAttribute("livRicerca", "I");
				} else {
					request.setAttribute("livRicerca", "P");
				}
				//request.setAttribute("tipoAuthority", "AU");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).purgeThis();
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			} else {
				dettaglioAutoreForm.getAreaDatiLegameTitoloVO().setIdArrivo(areaDatiPassReturn.getBid());
				dettaglioAutoreForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
				dettaglioAutoreForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo(areaDatiPass.getDettAutoreVO().getTipoNome());
				dettaglioAutoreForm.getAreaDatiLegameTitoloVO().setDescArrivo(areaDatiPass.getDettAutoreVO().getNome());
				dettaglioAutoreForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(dettaglioAutoreForm.isFlagCondiviso());

				request.setAttribute("AreaDatiLegameTitoloVO", dettaglioAutoreForm.getAreaDatiLegameTitoloVO());
				if (dettaglioAutoreForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null) {
					return mapping.findForward("gestioneLegameTitoloAutore");
				}
				if (dettaglioAutoreForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("AU") ||
						dettaglioAutoreForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("MA")) {
					return mapping.findForward("gestioneLegameFraAutority");
				}
			}
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
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

	public ActionForward insRigaVuotaRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioAutoreForm dettaglioAutoreForm = (DettaglioAutoreForm) form;

		dettaglioAutoreForm.addListaRepertoriModificato(new TabellaNumSTDImpronteVO());

		List<TabellaNumSTDImpronteVO> lista = new ArrayList<TabellaNumSTDImpronteVO>();
		for (int i=0; i<dettaglioAutoreForm.getListaRepertoriModificato().size(); i++) {
			TabellaNumSTDImpronteVO tabRepert = dettaglioAutoreForm.getListaRepertoriModificato().get(i);
			if (ValidazioneDati.strIsEmpty(tabRepert.getCampoDue()) )
				tabRepert.setCampoUno("Si");

			lista.add(tabRepert);
		}
		dettaglioAutoreForm.setListaRepertoriModificato(lista);
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	private void insNuovoRepertorio(HttpServletRequest request, ActionForm form) {

		DettaglioAutoreForm dettaglioAutoreForm = (DettaglioAutoreForm) form;

		List<TabellaNumSTDImpronteVO> lista = dettaglioAutoreForm.getListaRepertoriModificato();

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

		DettaglioAutoreForm dettaglioAutoreForm = (DettaglioAutoreForm) form;

		if (dettaglioAutoreForm.getSelezRadioRepertori() == null
				|| dettaglioAutoreForm.getSelezRadioRepertori().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblRepertorio"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioAutoreForm
				.getSelezRadioRepertori());
		dettaglioAutoreForm.getListaRepertoriModificato().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

}
