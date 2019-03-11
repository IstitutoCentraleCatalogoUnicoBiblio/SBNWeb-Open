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
package it.iccu.sbn.web.actions.gestionestampe.storicoill;

//import it.iccu.sbn.web.integration.bo.NavigationPathBO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.storicoill.StampaStoricoIllForm;

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

public class StampaStoricoIllAction extends LookupDispatchAction {
	private StampaStoricoIllForm ricStorIll;
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
		elem = new StrutturaCombo("13","nessuno");
		lista.add(elem);
		this.ricStorIll.setListaStatoRichiesta(lista);
	}

	private void loadStatoCircolazione() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Documento inviato dalla biblioteca fornitrice");
		lista.add(elem);
		elem = new StrutturaCombo("02","Documento pervenuto alla biblioteca richiedente");
		lista.add(elem);
		elem = new StrutturaCombo("03","Documento restituito dalla biblioteca richiedente)");
		lista.add(elem);
		elem = new StrutturaCombo("04","Documento rientrato alla biblioteca fornitrice");
		lista.add(elem);
		elem = new StrutturaCombo("05","Riproduzione inviata dalla biblioteca fornitrice");
		lista.add(elem);
		elem = new StrutturaCombo("06","Riproduzione pervenuta alla biblioteca richiedente");
		lista.add(elem);
		this.ricStorIll.setListaStatoCircolazione(lista);
	}

	private void loadBibliotecaRichiedente() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Biblioteca Comunale di Colleferro");
		lista.add(elem);
		elem = new StrutturaCombo("02","Biblioteca di prova client/server");
		lista.add(elem);
		elem = new StrutturaCombo("03","Storia moderna e contemporanea");
		lista.add(elem);
		elem = new StrutturaCombo("04","Biblioteca di prova Cosenza");
		lista.add(elem);
		elem = new StrutturaCombo("05","Biblioteca prv Uni Stranieri Perugia");
		lista.add(elem);
		elem = new StrutturaCombo("06","Bib. prova Perugia");
		lista.add(elem);
		this.ricStorIll.setListaBibliotecaRichiedente(lista);
	}

	private void loadBibliotecaDestinataria() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Biblioteca Comunale di Colleferro");
		lista.add(elem);
		elem = new StrutturaCombo("02","Biblioteca di prova client/server");
		lista.add(elem);
		elem = new StrutturaCombo("03","Storia moderna e contemporanea");
		lista.add(elem);
		elem = new StrutturaCombo("04","Biblioteca di prova Cosenza");
		lista.add(elem);
		elem = new StrutturaCombo("05","Biblioteca prv Uni Stranieri Perugia");
		lista.add(elem);
		elem = new StrutturaCombo("06","Bib. prova Perugia");
		lista.add(elem);
		this.ricStorIll.setListaBibliotecaDestinataria(lista);
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
		ricStorIll = (StampaStoricoIllForm) form;

		this.loadStatoRichiesta();
		this.loadStatoCircolazione();
		this.loadBibliotecaRichiedente();
		this.loadBibliotecaDestinataria();

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricStorIll = (StampaStoricoIllForm) form;
		try {
			request.setAttribute("parametroPassato", ricStorIll.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricStorIll = (StampaStoricoIllForm) form;
		try {
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("default");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
}
