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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.MenuServiziForm;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.Ticket;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

public class MenuServiziAction extends LookupDispatchAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MenuServiziForm currentForm = (MenuServiziForm) form;
		HttpSession session = request.getSession();
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);

		currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
		currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
		currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));

		Navigation navi = Navigation.getInstance(request);

		if (currentForm.getBiblioSel() == null) {
			//invio msg:"selezionare una biblioteca e premere "Ok".
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.dirittiUtente"));
			navi.purgeThis();
			return (mapping.findForward("selezioneBiblioteca"));

		}else {
			navi.makeFirst();
			//valorizzo
			return mapping.getInputForward();
		}
	}
	//
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MenuServiziForm currentForm = (MenuServiziForm)form;
		try {
			if (!ValidazioneDati.isFilled(currentForm.getSegnatura()) ) {
				//invio msg:"Valorizzare la collocazione"
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.valColl"));
				return mapping.getInputForward();
			}
			ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

			HttpSession session = request.getSession();
			String polo = Navigation.getInstance(request).getPolo();
			String codBib = (String)session.getAttribute(Constants.COD_BIBLIO);
			DocumentoNonSbnVO datiDocumento = null;

			String segnatura = currentForm.getSegnatura().trim();
			//controllo l'esistenza del documento
			//per almaviva4
			datiDocumento = factory.getGestioneServiziWeb().ricercaPerSegnatura(polo,codBib,segnatura);

			if (datiDocumento != null) {
				datiDocumento.setSegnatura(segnatura);
				datiDocumento.setCodBib(codBib);
				datiDocumento.setCodPolo(polo);

			}
			//controllo esistenza range di collocazione/cat frui di default

			DocumentoNonSbnVO collCatFrui = null;
			String ticket = Ticket.getUtenteWebTicket(polo, codBib,	Navigation.getInstance(request).getUserAddress());
			String ordSegn = OrdinamentoCollocazione2.normalizza(segnatura);
			collCatFrui = factory.getGestioneServizi().getCategoriaFruizioneSegnatura(ticket, polo, codBib, ordSegn);

			if (collCatFrui.getCodFruizione() == null){
				//???????inviare msg. opportuno
				//gestire msg. di ritorno
				return mapping.getInputForward();
			}

			session.setAttribute(Constants.DATI_DOC_KEY, datiDocumento);
			request.setAttribute("segnatura", segnatura);
			return (mapping.findForward("datiDocumento"));

		} catch (ApplicationException e) {
			if (e.getErrorCode() == SbnErrorTypes.SRV_CATEGORIA_FRUIZIONE_DEFAULT_NON_IMPOSTATA) {
				//almaviva5_20121211 #5164
				LinkableTagUtils.addError(request, new ActionMessage("errors.serviziweb.noCatFruizioneDefault"));
				return mapping.getInputForward();

			}
			//
			if (e.getErrorCode() == SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI) {
				LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();
			}
			//errore nella ricerca della collocazione
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.utenti.ricercaCollocazione"));
			return mapping.getInputForward();
		}
		//per almaviva4
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			if (ValidazioneDati.equals(idCheck, "PRENOTA_POSTO")) {
/*				HttpSession session = request.getSession();
				RicercaSalaVO ricerca = new RicercaSalaVO();
				ricerca.setCodPolo(Navigation.getInstance(request).getPolo());
				ricerca.setCodBib((String) session.getAttribute(Constants.COD_BIBLIO));
				return SaleDelegate.getInstance(request).getNumeroPostiLiberi(ricerca) > 0;*/
			}
		} catch (Exception e) {
			log.error("", e);
		}

		return false;
	}
}
