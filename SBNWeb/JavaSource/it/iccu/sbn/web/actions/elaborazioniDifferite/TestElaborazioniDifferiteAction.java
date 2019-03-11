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
package it.iccu.sbn.web.actions.elaborazioniDifferite;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriCancellazioneSoggettiNonUtilizzatiVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.TestElaborazioniDifferiteForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class TestElaborazioniDifferiteAction extends SinteticaLookupDispatchAction {
//public class SinteticaElaborazioniDifferiteAction extends LookupDispatchAction {
	//private OrdineRicercaParzialeForm ricOrdini;
//	private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		//map.put("button.cerca",		"cerca");
		//map.put("button.blocco", 	"caricaBlocco");
		map.put("button.indietro", 	"indietro");



		return map;
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
//		TestElaborazioniDifferiteForm testElaborazioniDifferiteForm = (TestElaborazioniDifferiteForm) form;
//		return mapping.findForward("indietro");
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();
		return navi.goBack(true);
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//OrdineRicercaParzialeForm ricOrdini = (OrdineRicercaParzialeForm) form;
		TestElaborazioniDifferiteForm testElaborazioniDifferiteForm = (TestElaborazioniDifferiteForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
		        return mapping.getInputForward();

			if(!testElaborazioniDifferiteForm.isSessione())
				testElaborazioniDifferiteForm.setSessione(true);

			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.elaborazionidifferite.statoRichieste") &&
					testElaborazioniDifferiteForm.getCodiceBibl()==null &&
					navi.getActionCaller()==null )
				// si proviene dal menu
				// pulizia integrale delle variabili di sessione
				Pulisci.PulisciVar(request);

			String procedura = request.getParameter("procedura");
			if (procedura == null)
				return mapping.getInputForward();

			if (procedura.equals(CodiciAttivita.getIstance().CANCELLAZIONE_SOGGETTI_INUTILIZZATI))
				return testCancellaSoggettiInutilizzati(request, mapping);

			if (procedura.equals(CodiciAttivita.getIstance().CANCELLAZIONE_DESCRITTORI_INUTILIZZATI))
				return testCancellaDescrittoriInutilizzati(request, mapping);

			String ticket=navi.getUserTicket();
			String biblio=navi.getUtente().getCodBib();
			testElaborazioniDifferiteForm.setCodiceBibl(biblio);

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ParametriRichiestaElaborazioneDifferitaVO richiesta = new AllineaVO();
			richiesta.setCodPolo(navi.getUtente().getCodPolo());
			richiesta.setCodBib(navi.getUtente().getCodBib());
			richiesta.setUser(navi.getUtente().getFirmaUtente());
			richiesta.setCodAttivita("IC001");
			factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, richiesta, null);

			return mapping.getInputForward();

		}
		catch (Exception e) {

			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.elabdiff.erroreNonGestito"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}



	private ActionForward testCancellaDescrittoriInutilizzati(
			HttpServletRequest request, ActionMapping mapping) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta = new ParametriCancellazioneSoggettiNonUtilizzatiVO();

		richiesta.setCodPolo(navi.getUtente().getCodPolo());
		richiesta.setCodBib(navi.getUtente().getCodBib());
		richiesta.setUser(navi.getUtente().getUserId());
		richiesta.setCodAttivita(CodiciAttivita.getIstance().CANCELLAZIONE_DESCRITTORI_INUTILIZZATI);

		richiesta.setCodSoggettario("FIR");
		richiesta.setTsVar_Da(DateUtil.toTimestamp("01/01/1901"));
		richiesta.setTsVar_A(DaoManager.now());

		String id = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(navi.getUserTicket(), richiesta, null);

		ActionMessages errors = new ActionMessages();
		errors.add("Avviso", new ActionMessage("errors.finestampa" , id));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}



	private ActionForward testCancellaSoggettiInutilizzati(HttpServletRequest request, ActionMapping mapping) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta = new ParametriCancellazioneSoggettiNonUtilizzatiVO();

		richiesta.setCodPolo(navi.getUtente().getCodPolo());
		richiesta.setCodBib(navi.getUtente().getCodBib());
		richiesta.setUser(navi.getUtente().getUserId());
		richiesta.setCodAttivita(CodiciAttivita.getIstance().CANCELLAZIONE_SOGGETTI_INUTILIZZATI);

		richiesta.setCodSoggettario("FIR");
		richiesta.setTsVar_Da(DateUtil.toTimestamp("01/01/1901"));
		richiesta.setTsVar_A(DaoManager.now());

		String id = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(navi.getUserTicket(), richiesta, null);

		ActionMessages errors = new ActionMessages();
		errors.add("Avviso", new ActionMessage("errors.finestampa" , id));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}

}

