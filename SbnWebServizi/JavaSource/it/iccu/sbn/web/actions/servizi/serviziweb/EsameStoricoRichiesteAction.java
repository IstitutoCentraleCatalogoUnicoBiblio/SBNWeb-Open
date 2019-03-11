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
import it.iccu.sbn.ejb.vo.gestionestampe.MovimentoPerStampaServCorrVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.EsameStoricoRichiesteForm;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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

public class EsameStoricoRichiesteAction extends SinteticaLookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca", "cerca");
		return map;
	}

	//metodo di init esameSugAcq.jsp
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameStoricoRichiesteForm currentForm = (EsameStoricoRichiesteForm) form;
		//
		Navigation navi = Navigation.getInstance(request);
		//
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);
			//
			HttpSession session = request.getSession();
			//
			Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
			currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));
			//
			if (idUtente == null ) {
				//invio msg:"selezionare una biblioteca e premere "Ok".
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.dirittiUtente"));
				navi.purgeThis();
				return (mapping.findForward("selezioneBiblioteca"));
			}

			return mapping.getInputForward();
		}
		catch (Exception ex) {
			//Errore di caricamento dello stato del suggerimento
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.lista.stato.suggerimento"));
			return (mapping.getInputForward());
		}
	}
	//metodo bottone esamina
	public ActionForward cerca(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {

		EsameStoricoRichiesteForm currentForm = (EsameStoricoRichiesteForm)form;

		try {
			HttpSession session = request.getSession();
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);

			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));

			Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);

			Navigation navi = Navigation.getInstance(request);
			String codPolo = navi.getPolo();
			String codBib = (String) session.getAttribute(Constants.COD_BIBLIO);

			if (idUtente == null) {
				//invio msg:"selezionare una biblioteca e premere "Ok".
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.dirittiUtente"));
				return (mapping.findForward("selezioneBiblioteca"));
			}

			DateUtil.validaRangeDate(currentForm.getDal(), currentForm.getAl());

			//Gestione criteri di ricerca
			MovimentoVO filtro = new MovimentoRicercaVO();
			filtro.setCodPolo(codPolo);
			filtro.setCodBibOperante(codBib);
			filtro.setDataInizioEff(DateUtil.toTimestamp(currentForm.getDal()));
			filtro.setDataFineEff(DateUtil.toTimestampA(currentForm.getAl()));
			filtro.setCodUte(idUtente.toString());
			filtro.setTipoOrdinamento("data_richiesta:D-cod_rich_serv:D");

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			List<MovimentoPerStampaServCorrVO> storico = delegate.getStampaListeTematicheStorico(filtro);

			if (!ValidazioneDati.isFilled(storico) ) {
				//invio msg:"Non sono presenti suggerimenti di acquisto"
				currentForm.setListaRichieste(null);
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
				return mapping.getInputForward();
			}

			//Carico lista
			currentForm.setListaRichieste(storico);
			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage()));
			return mapping.getInputForward();

		} catch (Exception e) {
			if (e.getMessage().equals("assenzaRisultati")) {
				//invio msg:"Non sono presenti suggerimenti di acquisto"

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));

				return mapping.getInputForward();
			}
			//Errore di caricamento lista suggerimenti di acquisto

			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.lista.suggerimenti.acquisto"));

			return (mapping.getInputForward());
		}
	}

}
