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

package it.iccu.sbn.web.actions.gestionebibliografica.luogo;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.luogo.InterrogazioneLuogoForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

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

public class InterrogazioneLuogoAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(InterrogazioneLuogoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca", "cercaLuogo");
		map.put("button.creaLuo", "creaLuo");
		map.put("button.creaLuoLoc", "creaLuoLoc");
		return map;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equals("POST"))
			((InterrogazioneLuogoForm)form).getInterrGener().save();
		return super.execute(mapping, form, request, response);
	}

	private void loadDefault(HttpServletRequest request, InterrogazioneLuogoForm ricLuogo) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {

			if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_LUO_PUNTUALE))) {
				ricLuogo.getInterrGener().setTipoRicerca("intero");
			} else {
				ricLuogo.getInterrGener().setTipoRicerca("inizio");
			}
			ricLuogo.getInterrGener().setElemXBlocchi(Integer.parseInt((String)utenteEjb.getDefault(ConstantDefault.RIC_LUO_ELEMENTI_BLOCCHI)));
			ricLuogo.getInterrGener().setTipoOrdinamSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_LUO_ORDINAMENTO));
			ricLuogo.getInterrGener().setRicLocale(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_LUO_LIVELLO_POLO)));
			ricLuogo.getInterrGener().setRicIndice(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_LUO_LIVELLO_INDICE)));
		} catch (DefaultNotFoundException e) {}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneLuogoForm ricLuogo = (InterrogazioneLuogoForm) form;
		log.debug("InterrogazioneLuogoAction");
		if(Navigation.getInstance(request).isFromBar() ) {
			ricLuogo.getInterrGener().restore();
			return mapping.getInputForward();
		}

		/** INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE */
		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,"LU");
			utenteEjb.isAbilitatoAuthority("LU");
			ricLuogo.setCreaLuo("SI");
			ricLuogo.setCreaLuoLoc("SI");
		}catch(UtenteNotAuthorizedException ute)
		{
			ricLuogo.setCreaLuo("NO");
			ricLuogo.setCreaLuoLoc("NO");
		}
		/** FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */

		if (request.getParameter("NEWLEGAME") != null) {
			ricLuogo.setProvenienza("NEWLEGAME");
			ricLuogo.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
		} else {
			ricLuogo.setProvenienza("");
		}

		//Viene settato il token per le transazioni successive
		this.saveToken(request);

//		Impostazione di inizializzazione jsp
		ricLuogo.getInterrGener().setTipoRicerca("inizio");

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	ricLuogo.getInterrGener().setListaPaese(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
    	ricLuogo.getInterrGener().setListaTipiOrdinam(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceOrdinamentoLuoghi()));
    	this.loadDefault(request, ricLuogo);
//		ricLuogo.getInterrGener().setRicLocale(false);
//		ricLuogo.getInterrGener().setRicIndice(true);
//		ricLuogo.getInterrGener().setElemXBlocchi(10);

		return mapping.getInputForward();
	}

	public ActionForward cercaLuogo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneLuogoForm ricLuogo = (InterrogazioneLuogoForm) form;

		// BUG MANTIS 3382 punto 2 - almaviva2 DICEMBRE 2015 eliminato il isTokenValid altrimenti provenendo
		// dalla barra di navigazione è necessario premere due volte il tasto "cerca";
//		if (!isTokenValid(request)) {
//			saveToken(request);
//			return mapping.getInputForward();
//		}

		AreaDatiPassaggioInterrogazioneLuogoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneLuogoVO();
		areaDatiPass.setRicercaIndice(false);
		areaDatiPass.setRicercaPolo(false);

		if (ricLuogo.getInterrGener().isRicLocale()) {
			request.setAttribute("livRicerca", "P");
			ricLuogo.setLivRicerca("P");
			areaDatiPass.setRicercaPolo(true);
		}
		if (ricLuogo.getInterrGener().isRicIndice()) {
			request.setAttribute("livRicerca", "I");
			ricLuogo.setLivRicerca("I");
			areaDatiPass.setRicercaIndice(true);
		}

		areaDatiPass.setInterrGener(ricLuogo.getInterrGener());
		areaDatiPass.setOggChiamante(99);
		areaDatiPass.setTipoOggetto(99);
		areaDatiPass.setOggDiRicerca("");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaLuoghi(areaDatiPass, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
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


		if (areaDatiPassReturn.getNumNotizie() > 0) {
			request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);
			request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassReturn);
			if (ricLuogo.getProvenienza().equals("NEWLEGAME")) {
				request.setAttribute("AreaDatiLegameTitoloVO", ricLuogo.getAreaDatiLegameTitoloVO());
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaLuoghiPerLegame"));
			} else {
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaLuoghi"));
			}
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.luoNotFound"));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}


	public ActionForward creaLuo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneLuogoForm ricLuogo = (InterrogazioneLuogoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (!ricLuogo.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(ricLuogo.getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricLuogo.getInterrGener().getDenominazione());

			resetToken(request);
			return mapping.findForward("creaLuogo");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			ricLuogo.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricLuogo.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("LU");
			ricLuogo.getAreaDatiLegameTitoloVO().setDescArrivo("");
			ricLuogo.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricLuogo.getAreaDatiLegameTitoloVO());

			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(ricLuogo.getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricLuogo.getInterrGener().getDenominazione());

			resetToken(request);
			return mapping.findForward("creaLuogo");

		}
    }

	public ActionForward creaLuoLoc(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneLuogoForm ricLuogo = (InterrogazioneLuogoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}


		if (!ricLuogo.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(ricLuogo.getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricLuogo.getInterrGener().getDenominazione());

			resetToken(request);
			return mapping.findForward("creaLuogoLocale");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			ricLuogo.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricLuogo.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("LU");
			ricLuogo.getAreaDatiLegameTitoloVO().setDescArrivo("");
			ricLuogo.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricLuogo.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricLuogo.getAreaDatiLegameTitoloVO());

			DettaglioLuogoGeneraleVO dettLuoVO = new DettaglioLuogoGeneraleVO();
			dettLuoVO.setDenomLuogo(ricLuogo.getInterrGener().getDenominazione());
			request.setAttribute("dettaglioLuo", dettLuoVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricLuogo.getInterrGener().getDenominazione());

			resetToken(request);
			return mapping.findForward("creaLuogoLocale");

		}
    }

}
