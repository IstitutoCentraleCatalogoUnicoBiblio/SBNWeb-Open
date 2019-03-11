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
package it.iccu.sbn.web.actions.gestionestampe.richiesteloc;

//import it.iccu.sbn.web.integration.bo.NavigationPathBO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.richiesteloc.StampaRichiesteLocForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class StampaRichiesteLocAction extends LookupDispatchAction {
	private StampaRichiesteLocForm ricRichLoc;

	private void loadStatoRichiesta() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Immessa (locale)");
		lista.add(elem);
		elem = new StrutturaCombo("02","Respinta (locale)");
		lista.add(elem);
		elem = new StrutturaCombo("03","Inoltrata (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("04","Cancellata (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("05","Rifiutata (bibl. destinataria e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("06","Accettata (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("07","Conclusa (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("08","Pervenuta (bibl. destinataria e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("09","Accettabile con riserva (bibl. destinataria e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("10","Prorogata (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("11","In attesa di proroga (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("12","Inoltrata con riserva (locale e polo)");
		lista.add(elem);
		elem = new StrutturaCombo("13","[nessuno]");
		lista.add(elem);
		this.ricRichLoc.setListaStatoRichiesta(lista);
	}

	private void loadAttivita() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Richiesta servizio");
		lista.add(elem);
		elem = new StrutturaCombo("02","Scarico da magazzino");
		lista.add(elem);
		elem = new StrutturaCombo("03","Consegna del documento al lettore)");
		lista.add(elem);
		elem = new StrutturaCombo("04","Restituzione da parte del lettore");
		lista.add(elem);
		elem = new StrutturaCombo("05","Collocazione presso punto deposito");
		lista.add(elem);
		this.ricRichLoc.setListaAttivita(lista);
	}

	private void loadStatoMovimenti() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Attivo");
		lista.add(elem);
		elem = new StrutturaCombo("02","Chiuso");
		lista.add(elem);
		elem = new StrutturaCombo("03","Annullato");
		lista.add(elem);
		elem = new StrutturaCombo("04","Documento disponibile, servizio non ancora terminato");
		lista.add(elem);
		elem = new StrutturaCombo("05","[Tutte]");
		lista.add(elem);
		this.ricRichLoc.setListaStatoMovimenti(lista);
	}

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession httpSession = request.getSession();
		//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());
		ricRichLoc = (StampaRichiesteLocForm) form;

		this.loadStatoRichiesta();
		this.loadStatoMovimenti();
		this.loadAttivita();

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricRichLoc = (StampaRichiesteLocForm) form;
		try {
			request.setAttribute("parametroPassato", ricRichLoc.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricRichLoc = (StampaRichiesteLocForm) form;
		try {
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("default");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
}
