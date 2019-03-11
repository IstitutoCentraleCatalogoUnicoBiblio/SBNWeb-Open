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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.EsameRichiesteForm;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.vo.MovimentoProrogaVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EsameRichiesteAction extends ServiziBaseAction {

	protected static final int FOLDER_RICHIESTE_ATTIVE = 0;
	protected static final int FOLDER_PRENOTAZIONI = 1;
	protected static final int FOLDER_RICHIESTE_RESPINTE = 2;
	protected static final int FOLDER_RICHIESTE_EVASE = 3;

	private static final String[] FOLDERS = new String[4];

	static {
		//ordine dei folder (da replicare nella JSP)
		FOLDERS[FOLDER_RICHIESTE_ATTIVE]	= "servizi.esameRichieste.folder.attive";
		FOLDERS[FOLDER_PRENOTAZIONI] 		= "servizi.esameRichieste.folder.prenotazioni";
		FOLDERS[FOLDER_RICHIESTE_RESPINTE]	= "servizi.esameRichieste.folder.respinte";
		FOLDERS[FOLDER_RICHIESTE_EVASE]		= "servizi.esameRichieste.folder.evase";
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("servizi.esameRichieste.folder.attive", "folder_attive");
		map.put("servizi.esameRichieste.folder.prenotazioni", "folder_prenotazioni");
		map.put("servizi.esameRichieste.folder.respinte", "folder_respinte");
		map.put("servizi.esameRichieste.folder.evase", "folder_evase");

		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameRichiesteForm currentForm = (EsameRichiesteForm) form;
		Navigation navi = Navigation.getInstance(request);

		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			HttpSession session = request.getSession();
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);

			String codPolo = navi.getPolo();

			if (!currentForm.isInitialized()) {
				currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
				currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
				currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));

				Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);

				if (idUtente == null) {
					//invio msg:"selezionare una biblioteca e premere "Ok".

					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.dirittiUtente"));

					navi.purgeThis();
					return (mapping.findForward("selezioneBiblioteca"));
				}
				navi.makeFirst();
				currentForm.setFolders(FOLDERS);
				currentForm.setCurrentFolder(FOLDER_RICHIESTE_ATTIVE);

				MovimentoRicercaVO filtro = currentForm.getFiltro();
				filtro.setCodPolo(codPolo);
				filtro.setCodBibUte(utente.getCodBib()); //bib di prima iscrizione
				filtro.setCodUte(utente.getUserId());
				filtro.setCodBibOperante((String) session.getAttribute(Constants.COD_BIBLIO));
				filtro.setUtenteLettore(true);
				filtro.setTipoOrdinamento(MovimentoListaVO.ORDINAMENTO_DATA_RICHIESTA_DESC);

				//primo caricamento: se non ci sono richieste attive, verranno caricate le prenotazioni
				folder_attive(mapping, currentForm, request, response);
				if (!ValidazioneDati.isFilled(currentForm.getListaRichieste()))
					folder_prenotazioni(mapping, currentForm, request, response);

				currentForm.setInitialized(true);
			}


			return mapping.getInputForward();

		} catch (Exception ex) {
			//Errore caricamento lista servizi richiesti

			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.lista.servizi.richiesti"));

			return (mapping.getInputForward());
		}
	}

	public ActionForward folder_attive(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameRichiesteForm currentForm = (EsameRichiesteForm) form;
		currentForm.setCurrentFolder(FOLDER_RICHIESTE_ATTIVE);
		MovimentoRicercaVO filtro = currentForm.getFiltro();

		//Richieste in corso
		filtro.setRichiesteInCorso(true);
		filtro.setRichiesteRespinte(false);
		filtro.setRichiesteEvase(false);
		filtro.setRichiestePrenotazioni(false);
		filtro.setIncludiPrenotazioniPosto(false);

		currentForm.setFlgInCorso(true);
		currentForm.setFlgRespinte(false);
		currentForm.setFlgEvase(false);
		currentForm.setFlgPrenotazioni(false);

		getRichiesteUtente(request, Navigation.getInstance(request).getUserTicket(), currentForm);

		return mapping.getInputForward();
	}

	public ActionForward folder_prenotazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameRichiesteForm currentForm = (EsameRichiesteForm) form;
		currentForm.setCurrentFolder(FOLDER_PRENOTAZIONI);
		MovimentoRicercaVO filtro = currentForm.getFiltro();

		//Prenotazioni
		filtro.setRichiesteInCorso(false);
		filtro.setRichiesteRespinte(false);
		filtro.setRichiesteEvase(false);
		filtro.setRichiestePrenotazioni(true);
		filtro.setIncludiPrenotazioniPosto(true);
		//
		currentForm.setFlgEvase(false);
		currentForm.setFlgInCorso(false);
		currentForm.setFlgRespinte(false);
		currentForm.setFlgPrenotazioni(true);

		getRichiesteUtente(request, Navigation.getInstance(request).getUserTicket(), currentForm);

		return mapping.getInputForward();
	}

	public ActionForward folder_respinte(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameRichiesteForm currentForm = (EsameRichiesteForm) form;
		currentForm.setCurrentFolder(FOLDER_RICHIESTE_RESPINTE);
		MovimentoRicercaVO filtro = currentForm.getFiltro();

		//Richieste respinte
		filtro.setRichiesteInCorso(false);
		filtro.setRichiesteRespinte(true);
		filtro.setRichiesteEvase(false);
		filtro.setRichiestePrenotazioni(false);
		filtro.setIncludiPrenotazioniPosto(false);
		//
		currentForm.setFlgRespinte(true);
		currentForm.setFlgInCorso(false);
		currentForm.setFlgEvase(false);
		currentForm.setFlgPrenotazioni(false);

		getRichiesteUtente(request, Navigation.getInstance(request).getUserTicket(), currentForm);

		return mapping.getInputForward();
	}

	public ActionForward folder_evase(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameRichiesteForm currentForm = (EsameRichiesteForm) form;
		currentForm.setCurrentFolder(FOLDER_RICHIESTE_EVASE);
		MovimentoRicercaVO filtro = currentForm.getFiltro();

		//Richieste evase
		filtro.setRichiesteInCorso(false);
		filtro.setRichiesteRespinte(false);
		filtro.setRichiesteEvase(true);
		filtro.setRichiestePrenotazioni(false);
		filtro.setIncludiPrenotazioniPosto(false);
		//
		currentForm.setFlgEvase(true);
		currentForm.setFlgInCorso(false);
		currentForm.setFlgRespinte(false);
		currentForm.setFlgPrenotazioni(false);

		getRichiesteUtente(request, Navigation.getInstance(request).getUserTicket(), currentForm);

		return mapping.getInputForward();
	}

	@SuppressWarnings("unchecked")
	private boolean getRichiesteUtente(HttpServletRequest request, String ticket, EsameRichiesteForm currentForm) throws Exception {

		LinkableTagUtils.resetErrors(request);

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) factory.getGestioneServizi()
				.getListaMovimentiPerErogazione(ticket, currentForm.getFiltro(),
						RicercaRichiesteType.RICERCA_PER_UTENTE, this.getLocale(request, Constants.SBN_LOCALE))
				.get(ServiziConstant.BLOCCO_RICHIESTE);

		List<MovimentoVO> listaRichieste = currentForm.getListaRichieste();
		listaRichieste.clear();

		if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.richieste.non.presenti"));
			return false;

		} else
			listaRichieste.addAll(blocco1.getLista());

		//Effettuo un ciclo su listaServiziRichiesti, per ogni richiesta
		//mi chiedo se esistono i presupposti per una
		//proroga, qualora ci fossero procedo al caricamento
		//del link "proroga" sulla jsp.
		List<MovimentoVO> out = new ArrayList<MovimentoVO>();
		for (MovimentoVO m : listaRichieste) {
			MovimentoProrogaVO p = impostaFlagProroga(request, (MovimentoListaVO) m);
			if (p.isWithPrenotazionePosto()) {
				PrenotazionePostoVO pp = p.getPrenotazionePosto();
				p.setPrenotazionePosto(new PrenotazionePostoDecorator(pp));
			}
			out.add(p);
		}

		currentForm.setListaRichieste(out);//lista richieste

		return true;
	}

	private MovimentoProrogaVO impostaFlagProroga(HttpServletRequest request, MovimentoListaVO mov) throws Exception {

		MovimentoProrogaVO p = new MovimentoProrogaVO(mov);

		//check stati del movimento
		p.setProrogabile((p.isRinnovabile() && p.getCodStatoMov().equals("A") && !ValidazioneDati.in(p.getCodStatoRic(), "S", "P")));

		if (!p.isProrogabile())
			return p;

		//check rinnovi
		short numRinnoviGiaEffettuati = mov.getNumRinnovi();
		p.setProrogabile(numRinnoviGiaEffettuati < MovimentoVO.MAX_RINNOVI);
		if (!p.isProrogabile())
			return p;

		ServizioBibliotecaVO servizio = getServizio(p.getCodPolo(), p.getCodBibOperante(), p.getCodTipoServ(), p.getCodServ(), request);
		short durataMassimaRinnovo = 0;
		short numeroRinnovo = (short)(numRinnoviGiaEffettuati+1);

		switch(numeroRinnovo) {
		case 1:
			durataMassimaRinnovo = servizio.getDurMaxRinn1();
			break;
		case 2:
			durataMassimaRinnovo = servizio.getDurMaxRinn2();
			break;
		case 3:
			durataMassimaRinnovo = servizio.getDurMaxRinn3();
			break;
		default:
			break;
		}

		p.setProrogabile(durataMassimaRinnovo > 0);
		if (!p.isProrogabile())
			return p;

		Timestamp now = DaoManager.now();
		//Imposto i campi di out da passare al Jsp richiestaProroga
		//per l'inserimento della stessa
		//data inzio proroga = alla data scadenza del servizio + 1
		Date dataFinePrevista = p.getDataFinePrev();

		Date dataProrogaMin = DateUtil.addDay(dataFinePrevista, 1);

		//data proroga = alla data scadenza del servizio + i giorni di rinnovo
		Date dataProrogaMax = DateUtil.addDay(dataFinePrevista, durataMassimaRinnovo);

		p.setDataProrogaMin(dataProrogaMin);
		p.setDataProrogaMax(dataProrogaMax);

		p.setProrogabile(dataProrogaMax.after(DateUtils.truncate(now, Calendar.DAY_OF_MONTH)) );

		//almaviva5_20150618
		if (!p.isProrogabile())
			return p;

		ParametriBibliotecaVO parBib = getParametriBiblioteca(p.getCodPolo(), p.getCodBibOperante(), request);
		//controllo quanti giorni prima della scadenza prevista può essere richiesta la proroga
		short ggRinnovoRichiesta = parBib.getGgRinnovoRichiesta();
		Date dataLimite = DateUtil.addDay(dataFinePrevista, -ggRinnovoRichiesta);
		boolean prorogabile = (ggRinnovoRichiesta == 0) || (now.after(DateUtils.truncate(dataLimite, Calendar.DAY_OF_MONTH)) );
		p.setProrogabile(prorogabile);

		return p;
	}
}
