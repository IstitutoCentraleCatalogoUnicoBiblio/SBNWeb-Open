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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.EsameRichiesteForm;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.RichiestaProrogaForm;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.action.erogazione.ListaMovimentiAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.vo.MovimentoProrogaVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RichiestaProrogaAction extends ListaMovimentiAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.indietro", "indietro");
		map.put("servizi.bottone.inserimento.proroga", "proroga");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaProrogaForm currentForm = (RichiestaProrogaForm) form;
		HttpSession session = request.getSession();

		UtenteWeb utente = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
		currentForm.setCodUtente(utente.getUserId());
		currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());

		currentForm.setBibsel((String) session.getAttribute(Constants.BIBLIO_SEL));
		currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));

		String codPolo = Navigation.getInstance(request).getPolo();

		InfoDocumentoVO docVO = (InfoDocumentoVO) request.getAttribute(Constants.INFO_DOCUMENTO);
		currentForm.setInfoDocumentoVO(docVO);
		currentForm.setLstCodiciServizio((List<ServizioBibliotecaVO>) request.getAttribute(Constants.SERVIZI_ATTIVI_DOCUMENTO));

		currentForm.setUtenteVO(new UtenteBaseVO());
		currentForm.getUtenteVO().setCodBib(currentForm.getCod_biblio());//utente.getCodBib());
		currentForm.getUtenteVO().setCodUtente(utente.getUserId());
		currentForm.getMovRicerca().setCodPolo(codPolo);
		currentForm.getMovRicerca().setCodBibOperante(currentForm.getCod_biblio());

		currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);

		ActionMessages error = new ActionMessages();

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			//Imposto le date di inizio e fine proroga
			String idMov = request.getParameter("IDMOV");
			NavigationElement prev = Navigation.getInstance(request).getCache().getPreviousElement();
			EsameRichiesteForm prevForm = (EsameRichiesteForm) prev.getForm();
			MovimentoProrogaVO mov = (MovimentoProrogaVO) prevForm.getListaRichieste().get(Integer.valueOf(idMov));
			currentForm.setAnaMov(mov);
			currentForm.setDataRic(DateUtil.formattaData(mov.getDataFinePrev()));

			currentForm.setDataMinProroga(DateUtil.formattaData(mov.getDataProrogaMin()) );
			currentForm.setDataMaxProrogabile(DateUtil.formattaData(mov.getDataProrogaMax()) );

			//inizio caricamento Array di date
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());

			Date dtMinPro = mov.getDataProrogaMin();
			Date dtMaxPro = mov.getDataProrogaMax();

			List<ComboCodDescVO> lstData = new ArrayList<ComboCodDescVO>();

			calendar.setTime(dtMinPro);
			int diff = DateUtil.diffDays(dtMaxPro, dtMinPro);

			Timestamp now = DaoManager.now();

			while (diff >= 0) {
				if (dtMinPro.after(now))
					lstData.add(0, new ComboCodDescVO(DateUtil.formattaData(dtMinPro), null));

				calendar.add(Calendar.DATE, 1);
	            dtMinPro = (new Timestamp(calendar.getTimeInMillis()));
	        	diff--;
			}

			currentForm.setDataPrevRitDoc(lstData);
			//fine caricamento Array di date

			return mapping.getInputForward();
		}
		catch (Exception ex) {
			//caso Errore caricamento data proroga
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.dataProroga"));
			this.saveErrors(request, error);
			return (mapping.getInputForward());
		}
	}
	//
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			//
			return (mapping.findForward("esameRichieste"));
			//
		} catch (Exception ex) {
			// ricordare di inserire il msg di errore
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.ListaServiziOpac"));
			this.saveErrors(request, error);
			return (mapping.getInputForward());
		}
	}
	//
	public ActionForward proroga(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RichiestaProrogaForm currentForm = (RichiestaProrogaForm) form;
		//
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			// inizio controlli
			ActionMessages err = this.controlliObblig(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}
			//
			ActionMessages errF = this.controlliFormali(request, currentForm);
			if (errF.size() > 0) {
				this.saveErrors(request, errF);
				return mapping.getInputForward();
			}
			//
			// fine controlli
			//

			MovimentoVO movimento = currentForm.getAnaMov();
			Date dataProroga = DateUtil.toDate(currentForm.getDataMaxProroga().trim());
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			MovimentoVO prorogato = delegate.aggiornaRichiestaPerProroga(movimento, dataProroga);
			switch (prorogato.getCodStatoRic().charAt(0)) {
			case 'P':	//in attesa di proroga
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.InsProrogaOk"));
				break;
			case 'N':	//prorogata
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.InsProrogaRinnovato", prorogato.getDataProrogaString()));
				break;
			}

		/*
			MovimentoVO movimento = currentForm.getAnaMov();
			movimento.setCodStatoRic("P"); //p=attesa di proroga
			//data proroga
			Date dataProroga = DateUtil.toDate(currentForm.getDataMaxProroga().trim());
			movimento.setDataProroga(new java.sql.Date(dataProroga.getTime()) );
			//
			String bibsel = (String) session.getAttribute(Constants.COD_BIBLIO);
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			ServizioBibliotecaVO servizioSelezionato =
				delegate.getServizioBiblioteca(movimento.getCodPolo(), bibsel, movimento.getCodTipoServ(), movimento.getCodServ());

			//aggiornamento richiesta in attesa di proroga
			delegate.aggiornaRichiesta(movimento, servizioSelezionato.getIdServizio());

			//invio msg."Inserimento richiesta di proroga avvenuto con successo"
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.InsProrogaOk"));
			this.saveErrors(request, error);
		*/
			return (mapping.findForward("esameRichieste"));
			//
		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception ex) {
			// errore nell'inserimento proroga
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.utenti.erroreInsProroga"));
		}
		return mapping.getInputForward();
	}
	//
	private ActionMessages controlliObblig(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		//
		RichiestaProrogaForm currentForm = (RichiestaProrogaForm) form;
		ActionMessages errors = new ActionMessages();
		//
		if (!ValidazioneDati.isFilled(currentForm.getDataMaxProroga()) ) {
			// Data massima di proroga
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.data.massima.proroga.Obbligatorio"));
			return errors;
		}
		//
		return errors;
	}
	//
	private ActionMessages controlliFormali(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		//
		RichiestaProrogaForm currentForm = (RichiestaProrogaForm) form;

		ActionMessages errors = new ActionMessages();
		//
		String apDataProroga = currentForm.getDataMaxProroga().trim();
		String apDataMaxProrogabile = (currentForm.getDataMaxProrogabile().trim());
		String apDataMinProrogabile = (currentForm.getDataRic().trim());
		//
		if (apDataProroga !=""){
			if (!DateUtil.isData(apDataProroga)) {
				// Formato data massima di proroga
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.data.massima.proroga.err"));

				return errors;
			}
		}
		/*
		if (apDataProroga > apDataMaxProrogabile) {
			//data proroga maggiore della data massima prorogabile
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.data.massima.proroga.err"));
			return errors;
		}
		//
		if (apDataProroga <= apDataMinProrogabile) {
			//data proroga minore della data di scadenza del servizio
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.data.massima.proroga.err"));
			return errors;
		}
		*/
		return errors;
	}

}
