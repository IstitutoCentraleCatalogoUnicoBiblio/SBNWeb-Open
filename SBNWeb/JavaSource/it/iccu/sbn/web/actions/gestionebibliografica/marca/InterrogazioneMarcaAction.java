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

package it.iccu.sbn.web.actions.gestionebibliografica.marca;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SifRicercaRepertoriVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.marca.InterrogazioneMarcaForm;
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

public class InterrogazioneMarcaAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(InterrogazioneMarcaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca", "cercaMar");
		map.put("button.creaMar", "creaMar");
		map.put("button.creaMarLocale", "creaMarLocale");
		return map;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equals("POST"))
			((InterrogazioneMarcaForm)form).getInterrGener().save();
		return super.execute(mapping, form, request, response);
	}

	private void loadDefault(HttpServletRequest request,InterrogazioneMarcaForm ricMar) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			ricMar.getInterrGener().setElemXBlocchi(Integer.parseInt((String)utenteEjb.getDefault(ConstantDefault.RIC_MAR_ELEMENTI_BLOCCHI)));
			ricMar.getInterrGener().setTipoOrdinamSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_MAR_ORDINAMENTO));
			ricMar.getInterrGener().setRicLocale(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_MAR_LIVELLO_POLO)));
			ricMar.getInterrGener().setRicIndice(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_MAR_LIVELLO_INDICE)));
		} catch (DefaultNotFoundException e) {}

	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.debug("InterrogazioneMarcaAction");
		InterrogazioneMarcaForm ricMar = (InterrogazioneMarcaForm) form;
		if(Navigation.getInstance(request).isFromBar() ) {
			ricMar.getInterrGener().restore();
			return mapping.getInputForward();
		}


		/** INIZIO VERIFICA ABILITAZIONE */
		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try{
			utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,"MA");
			utenteEjb.isAbilitatoAuthority("MA");
			ricMar.setCreaMar("SI");
			ricMar.setCreaMarLoc("SI");
		}catch(UtenteNotAuthorizedException ute)
		{
			ricMar.setCreaMar("NO");
			ricMar.setCreaMarLoc("NO");
		}
		/** FINE VERIFICA ABILITAZIONE */

		if (request.getParameter("NEWLEGAME") != null) {
			ricMar.setProvenienza("NEWLEGAME");
			ricMar.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
		} else {
			ricMar.setProvenienza("");
		}

		//Viene settato il token per le transazioni successive
		this.saveToken(request);

		caricaComboGenerMarca(ricMar);
//		ricMar.getInterrGener().setRicLocale(false);
//		ricMar.getInterrGener().setRicIndice(true);
//		ricMar.getInterrGener().setElemXBlocchi(10);
		this.loadDefault(request, ricMar);

		return mapping.getInputForward();
	}


	public ActionForward cercaMar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneMarcaForm ricMar = (InterrogazioneMarcaForm) form;

		// BUG MANTIS 3382 punto 2 - almaviva2 01.12.2009 eleiminato il isTokenValid
//		if (!isTokenValid(request)) {
//			saveToken(request);
//			return mapping.getInputForward();
//		}

		AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneMarcaVO();
		areaDatiPass.setRicercaIndice(false);
		areaDatiPass.setRicercaPolo(false);

		if (ricMar.getInterrGener().isRicLocale()) {
			request.setAttribute("livRicerca", "P");
			ricMar.setLivRicerca("P");
			areaDatiPass.setRicercaPolo(true);
		}
		if (ricMar.getInterrGener().isRicIndice()) {
			request.setAttribute("livRicerca", "I");
			ricMar.setLivRicerca("I");
			areaDatiPass.setRicercaIndice(true);
		}

		areaDatiPass.setInterrGener(ricMar.getInterrGener());
		areaDatiPass.setOggChiamante(99);
		areaDatiPass.setTipoOggetto(99);
		areaDatiPass.setOggDiRicerca("");

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaMarche(areaDatiPass, Navigation.getInstance(request).getUserTicket());

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
			if (ricMar.getProvenienza().equals("NEWLEGAME")) {
				request.setAttribute("AreaDatiLegameTitoloVO", ricMar.getAreaDatiLegameTitoloVO());
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaMarchePerLegame"));
			} else {
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaMarche"));
			}
		}
		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.marNotFound"));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}


	public ActionForward creaMar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		InterrogazioneMarcaForm ricMar = (InterrogazioneMarcaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (!ricMar.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioMarcaGeneraleVO dettMarVO = new DettaglioMarcaGeneraleVO();
			this.caricaValoriPerCreazione(ricMar, dettMarVO);
			request.setAttribute("dettaglioMar", dettMarVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricMar.getInterrGener().getDescrizione());

			resetToken(request);
			return mapping.findForward("creaMarca");
		} else {
			request.setAttribute("tipoProspettazione", "INS");

			ricMar.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricMar.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("MA");
			ricMar.getAreaDatiLegameTitoloVO().setDescArrivo("");
			ricMar.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricMar.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricMar.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricMar.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricMar.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricMar.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricMar.getAreaDatiLegameTitoloVO());

			DettaglioMarcaGeneraleVO dettMarVO = new DettaglioMarcaGeneraleVO();
			this.caricaValoriPerCreazione(ricMar, dettMarVO);
			request.setAttribute("dettaglioMar", dettMarVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricMar.getInterrGener().getDescrizione());

			resetToken(request);
			return mapping.findForward("creaMarca");

		}

	}


	public ActionForward creaMarLocale(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

//		 FUNZIONE MOMENTANEAMENTE NON DISPONIBILE
		ActionMessages errors = new ActionMessages();
		errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
		return mapping.getInputForward();
    }

    public void caricaComboGenerMarca(InterrogazioneMarcaForm ricMar) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricMar.getInterrGener().setListaTipiOrdinam(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceOrdinamentoMarche()));

		List<SifRicercaRepertoriVO> listaRepertori = factory.getGestioneRepertorio().getAllRepertori();
		List<ComboCodDescVO> lista = new ArrayList<ComboCodDescVO>();
		ComboCodDescVO comboCodDescVO = new ComboCodDescVO();;
		comboCodDescVO.setCodice("");
		comboCodDescVO.setDescrizione("");
		lista.add(comboCodDescVO);

		for (int i=0; i<listaRepertori.size(); i++) {
			SifRicercaRepertoriVO sifRicRep = listaRepertori.get(i);
			if (sifRicRep.getTipo().equals("M")) {
				comboCodDescVO = new ComboCodDescVO();
				comboCodDescVO.setCodice(sifRicRep.getSigl());
				comboCodDescVO.setDescrizione(sifRicRep.getDesc());
				lista.add(comboCodDescVO);
			}
		}
		ricMar.getInterrGener().setListaCitazioneStandard(lista);
    }


    public void caricaValoriPerCreazione(InterrogazioneMarcaForm ricMar, DettaglioMarcaGeneraleVO dettMarVO) throws Exception {

		dettMarVO.setDesc(ricMar.getInterrGener().getDescrizione());
		dettMarVO.setMotto(ricMar.getInterrGener().getMotto());
		dettMarVO.setParChiave1(ricMar.getInterrGener().getParolaChiave1());
		dettMarVO.setParChiave2(ricMar.getInterrGener().getParolaChiave2());
		dettMarVO.setParChiave3(ricMar.getInterrGener().getParolaChiave3());

		// Modifica almaviva2 per preimpostare i valori di fonte/repertorio per la creazione 12.04.2010 MANTIS 3684
		dettMarVO.setCampoCodiceRep1Old(ricMar.getInterrGener().getCitazioneStandardSelez());
		dettMarVO.setCampoProgressivoRep1Old(ricMar.getInterrGener().getSiglaRepertorio());

    }
}
