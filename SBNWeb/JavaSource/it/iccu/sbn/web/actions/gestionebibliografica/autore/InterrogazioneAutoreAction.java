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

package it.iccu.sbn.web.actions.gestionebibliografica.autore;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.autore.InterrogazioneAutoreForm;
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

public class InterrogazioneAutoreAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(InterrogazioneAutoreAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca", "cercaAut");
		map.put("button.creaAut", "creaAut");
		map.put("button.creaAutLoc", "creaAutLocale");
		return map;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equals("POST"))
			((InterrogazioneAutoreForm)form).getInterrGener().save();
		return super.execute(mapping, form, request, response);
	}

	private void loadDefault(HttpServletRequest request, InterrogazioneAutoreForm ricAut) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			ricAut.getInterrGener().setElemXBlocchi(Integer.parseInt((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_ELEMENTI_BLOCCHI)));
			ricAut.getInterrGener().setTipoOrdinamSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_ORDINAMENTO));

			ricAut.getInterrGener().setRicLocale(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_LIVELLO_POLO)));
			ricAut.getInterrGener().setRicIndice(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_LIVELLO_INDICE)));

			if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_NOME_INIZIO))) {
				ricAut.getInterrGener().setTipoRicerca("inizio");
			} else if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_NOME_PAROLE))) {
				ricAut.getInterrGener().setTipoRicerca("parole");
			} else if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_NOME_PUNTUALE))) {
				ricAut.getInterrGener().setTipoRicerca("intero");
			}
			if (ricAut.getTipoAutore() == null) {
				if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RICERCA_TUTTI_NOMI))) {
					ricAut.setTipoAutore("tuttiNomi");
					ricAut.getInterrGener().setChkTipoNomeA(false);
					ricAut.getInterrGener().setChkTipoNomeB(false);
					ricAut.getInterrGener().setChkTipoNomeC(false);
					ricAut.getInterrGener().setChkTipoNomeD(false);
					ricAut.getInterrGener().setChkTipoNomeE(false);
					ricAut.getInterrGener().setChkTipoNomeG(false);
					ricAut.getInterrGener().setChkTipoNomeR(false);
				} else if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RICERCA_NOME_PERSONALE))) {
					ricAut.setTipoAutore("autorePersonale");
					ricAut.getInterrGener().setChkTipoNomeA(true);
					ricAut.getInterrGener().setChkTipoNomeB(true);
					ricAut.getInterrGener().setChkTipoNomeC(true);
					ricAut.getInterrGener().setChkTipoNomeD(true);
					ricAut.getInterrGener().setChkTipoNomeE(false);
					ricAut.getInterrGener().setChkTipoNomeG(false);
					ricAut.getInterrGener().setChkTipoNomeR(false);
				} else if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RICERCA_NOME_COLLETTIVO))) {
					ricAut.setTipoAutore("autoreCollettivo");
					ricAut.getInterrGener().setChkTipoNomeA(false);
					ricAut.getInterrGener().setChkTipoNomeB(false);
					ricAut.getInterrGener().setChkTipoNomeC(false);
					ricAut.getInterrGener().setChkTipoNomeD(false);
					ricAut.getInterrGener().setChkTipoNomeE(true);
					ricAut.getInterrGener().setChkTipoNomeG(true);
					ricAut.getInterrGener().setChkTipoNomeR(true);
				}
			}

			if (ricAut.getInterrGener().getFormaAutore() == null || ricAut.getInterrGener().getFormaAutore().equals("")) {
				if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_FORMA_TUTTI))) {
					ricAut.getInterrGener().setFormaAutore("tutti");
				} else if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_FORMA_ACCETTATA))) {
					ricAut.getInterrGener().setFormaAutore("autore");
				} else if (Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_AUT_FORMA_RINVIO))) {
					ricAut.getInterrGener().setFormaAutore("rinvio");
				}
			}

		} catch (DefaultNotFoundException e) {}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneAutoreForm ricAut = (InterrogazioneAutoreForm) form;

		if(Navigation.getInstance(request).isFromBar() ) {
			log.debug("InterrogazioneAutoreAction");
			ricAut.getInterrGener().restore();
			return mapping.getInputForward();
		}

		/** INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE */
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,"AU");
			utenteEjb.isAbilitatoAuthority("AU");
			ricAut.setCreaAut("SI");
			ricAut.setCreaAutLoc("SI");
		}catch(UtenteNotAuthorizedException ute)
		{
			ricAut.setCreaAut("NO");
			ricAut.setCreaAutLoc("NO");
		}
		/** FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */

		if (ricAut.getProvenienza() == null) {
			ricAut.setProvenienza("");
		}
		if (request.getParameter("NEWLEGAME") != null) {
			ricAut.setProvenienza("NEWLEGAME");
			ricAut.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			if (ricAut.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& ricAut.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
				ricAut.setPresenzaRicLocale("NO");
				ricAut.getInterrGener().setNome(ricAut.getAreaDatiLegameTitoloVO().getDescPartenza());
			}
		}

		//Viene settato il token per le transazioni successive
		this.saveToken(request);

		caricaComboGenerAutore(ricAut);
//		Impostazione di inizializzazione jsp
		if (ricAut.getInterrGener().getElemXBlocchi() == 0) {
			this.loadDefault(request, ricAut);
		} else {
			if (ricAut.getTipoAutore() == null) {
				ricAut.setTipoAutore("tuttiNomi");
			}
			if (ricAut.getTipoAutore().equals("tuttiNomi")) {
				ricAut.getInterrGener().setChkTipoNomeA(false);
				ricAut.getInterrGener().setChkTipoNomeB(false);
				ricAut.getInterrGener().setChkTipoNomeC(false);
				ricAut.getInterrGener().setChkTipoNomeD(false);
				ricAut.getInterrGener().setChkTipoNomeE(false);
				ricAut.getInterrGener().setChkTipoNomeG(false);
				ricAut.getInterrGener().setChkTipoNomeR(false);
			} else if (ricAut.getTipoAutore().equals("autorePersonale")) {
				ricAut.getInterrGener().setChkTipoNomeA(true);
				ricAut.getInterrGener().setChkTipoNomeB(true);
				ricAut.getInterrGener().setChkTipoNomeC(true);
				ricAut.getInterrGener().setChkTipoNomeD(true);
				ricAut.getInterrGener().setChkTipoNomeE(false);
				ricAut.getInterrGener().setChkTipoNomeG(false);
				ricAut.getInterrGener().setChkTipoNomeR(false);
			} else if (ricAut.getTipoAutore().equals("autoreCollettivo")){
				ricAut.getInterrGener().setChkTipoNomeA(false);
				ricAut.getInterrGener().setChkTipoNomeB(false);
				ricAut.getInterrGener().setChkTipoNomeC(false);
				ricAut.getInterrGener().setChkTipoNomeD(false);
				ricAut.getInterrGener().setChkTipoNomeE(true);
				ricAut.getInterrGener().setChkTipoNomeG(true);
				ricAut.getInterrGener().setChkTipoNomeR(true);
			}
		}


		if (ricAut.getPresenzaRicLocale().equals("NO")) {
			ricAut.getInterrGener().setRicLocale(false);
		}

		// Febbraio 2016: gestione ISNI (International standard number identifier)
		// ATTENZIONE su CercaAutore e su interrogazioneAutore.jsp viene asteriscata la ricerca per ISADN
		// ma non sostituita da quella per ISNI: in attesa dell'Indice
		if (ricAut.getInterrGener().getIsadn() == null) {
			ricAut.getInterrGener().setIsadn("");
		}

		return mapping.getInputForward();
	}

	public ActionForward cercaAut(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneAutoreForm ricAut = (InterrogazioneAutoreForm) form;

		// BUG MANTIS 3382 punto 2 - almaviva2 DICEMBRE 2015 eliminato il isTokenValid altrimenti provenendo
		// dalla barra di navigazione è necessario premere due volte il tasto "cerca";
//		if (!isTokenValid(request)) {
//			saveToken(request);
//			return mapping.getInputForward();
//		}

		try {
			ricAut.getInterrGener().validaCanaliPrim();
		} catch (ValidationException e) {
			e.printStackTrace();
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + e.getMessage()));
			this.saveErrors(request, errors);
	        return mapping.getInputForward();
		}

		try {
			ricAut.getInterrGener().validaParametriGener();
		} catch (ValidationException e) {
			e.printStackTrace();
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + e.getMessage()));
			this.saveErrors(request, errors);
	        return mapping.getInputForward();
		}


		AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass = new AreaDatiPassaggioInterrogazioneAutoreVO();
		areaDatiPass.setRicercaIndice(false);
		areaDatiPass.setRicercaPolo(false);

		if (ricAut.getInterrGener().isRicLocale()) {
			request.setAttribute("livRicerca", "P");
			ricAut.setLivRicerca("P");
			areaDatiPass.setRicercaPolo(true);
		}
		if (ricAut.getInterrGener().isRicIndice()) {
			request.setAttribute("livRicerca", "I");
			ricAut.setLivRicerca("I");
			areaDatiPass.setRicercaIndice(true);
		}

		areaDatiPass.setInterrGener(ricAut.getInterrGener());
		areaDatiPass.setOggChiamante(99);
		areaDatiPass.setTipoOggetto(99);
		areaDatiPass.setOggDiRicerca("");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaAutori(areaDatiPass, Navigation.getInstance(request).getUserTicket());

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
			if (ricAut.getProvenienza().equals("NEWLEGAME")) {
				request.setAttribute("AreaDatiLegameTitoloVO", ricAut.getAreaDatiLegameTitoloVO());
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaAutoriPerLegame"));
			} else {
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaAutori"));
			}
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.autNotFound"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}


	public ActionForward creaAut(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneAutoreForm ricAut = (InterrogazioneAutoreForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (!ricAut.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			this.caricaValoriPerCreazione(ricAut, dettAutVO);

			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricAut.getInterrGener().getNome());

			resetToken(request);
			return mapping.findForward("creaAutore");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			ricAut.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricAut.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
			ricAut.getAreaDatiLegameTitoloVO().setTipoNomeArrivo("");
			ricAut.getAreaDatiLegameTitoloVO().setDescArrivo("");

			ricAut.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricAut.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricAut.getAreaDatiLegameTitoloVO());

			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			this.caricaValoriPerCreazione(ricAut, dettAutVO);

			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricAut.getInterrGener().getNome());

			resetToken(request);
			return mapping.findForward("creaAutore");
		}
    }

	public ActionForward creaAutLocale(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {


		InterrogazioneAutoreForm ricAut = (InterrogazioneAutoreForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (!ricAut.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			this.caricaValoriPerCreazione(ricAut, dettAutVO);

			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricAut.getInterrGener().getNome());

			resetToken(request);
			return mapping.findForward("creaAutoreLocale");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			ricAut.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricAut.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
			ricAut.getAreaDatiLegameTitoloVO().setTipoNomeArrivo("");
			ricAut.getAreaDatiLegameTitoloVO().setDescArrivo("");

			ricAut.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricAut.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricAut.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricAut.getAreaDatiLegameTitoloVO());

			DettaglioAutoreGeneraleVO dettAutVO = new DettaglioAutoreGeneraleVO();
			this.caricaValoriPerCreazione(ricAut, dettAutVO);

			request.setAttribute("dettaglioAut", dettAutVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricAut.getInterrGener().getNome());

			resetToken(request);
			return mapping.findForward("creaAutoreLocale");
		}

    }


    public void caricaComboGenerAutore(InterrogazioneAutoreForm ricAut) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	ricAut.getInterrGener().setFormaAutore("");
    	ricAut.getInterrGener().setListaPaese(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
    	ricAut.getInterrGener().setListaLingue(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceLingua()));
    	ricAut.getInterrGener().setListaTipiOrdinam(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceOrdinamentoAutori()));
    }

    public void caricaValoriPerCreazione(InterrogazioneAutoreForm ricAut, DettaglioAutoreGeneraleVO dettAutVO) throws Exception {
    	dettAutVO.setNome(ricAut.getInterrGener().getNome());

    }
}
