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
package it.iccu.sbn.web.actions.gestionestampe.repertori;

//import it.iccu.sbn.web.integration.bo.NavigationPathBO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.repertori.StampaRepertoriForm;

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

public class StampaRepertoriAction extends LookupDispatchAction {
	private StampaRepertoriForm ricOrdini;
	private void loadContinuativo() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("01","Si");
		lista.add(elem);
		elem = new StrutturaCombo("02","No");
		lista.add(elem);
		elem = new StrutturaCombo("03","Tutti");
		lista.add(elem);
		this.ricOrdini.setListaContinuativo(lista);
	}
    private void loadTipoInvio() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - fax");
		lista.add(elem);
		elem = new StrutturaCombo("P","P - posta");
		lista.add(elem);
		elem = new StrutturaCombo("S","S - stampa");
		lista.add(elem);
		this.ricOrdini.setListaTipoInvio(lista);
	}

	private void loadStato() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - Aperto");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Chiuso");
		lista.add(elem);
		elem = new StrutturaCombo("I","I - Spedito");
		lista.add(elem);
		elem = new StrutturaCombo("N","N - Annullato");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - Rinnovato");
		lista.add(elem);

		this.ricOrdini.setListaStato(lista);
	}

	private void loadTipoOrdine() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("A","Acquisto");
		lista.add(elem);
		elem = new StrutturaCombo("L","Deposito Legale");
		lista.add(elem);
		elem = new StrutturaCombo("D","Dono");
		lista.add(elem);
		elem = new StrutturaCombo("C","Scambio");
		lista.add(elem);
		elem = new StrutturaCombo("V","Visione Trattenuta");
		lista.add(elem);

		this.ricOrdini.setListaTipoOrdine(lista);
	}

    private void loadNatura() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("D","D - Altro Titolo");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Collezione");
		lista.add(elem);
		elem = new StrutturaCombo("M","M - Monografia");
		lista.add(elem);
		elem = new StrutturaCombo("S","S - Pubblicazione in serie");
		lista.add(elem);
		elem = new StrutturaCombo("N","N - Titolo Analitico ");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - Titolo di raggruppamento");
		lista.add(elem);
		elem = new StrutturaCombo("P","P - Titolo parallelo");
		lista.add(elem);
		elem = new StrutturaCombo("T","T - Titolo subordinato");
		lista.add(elem);
		elem = new StrutturaCombo("W","W - Volume senza titolo");
		lista.add(elem);
		this.ricOrdini.setListaNatura(lista);
	}

	private void loadBiblAffil() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("00","Tutte");
		lista.add(elem);
		elem = new StrutturaCombo("01","Biblioteca di Prova");
		lista.add(elem);
		elem = new StrutturaCombo("02","Biblioteca Storia");
		lista.add(elem);

		this.ricOrdini.setListaBiblAffil(lista);
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
		ricOrdini = (StampaRepertoriForm) form;

		this.loadContinuativo();
		this.loadBiblAffil();
		this.loadTipoOrdine();
		this.loadNatura();
		this.loadStato();
		this.loadTipoInvio();
		ricOrdini.setElemBlocco(null);
		ricOrdini.setContinuativo("03");
		ricOrdini.setTipoOrdine("L");
		ricOrdini.setCodiceBibl("01");
		ricOrdini.setBiblAffil("");
		ricOrdini.setSezione("");
		ricOrdini.setFornitore("33 - Libreria Grande");
		ricOrdini.setNatura("");
		ricOrdini.setDataOrdineDa("gg/mm/aaaa");
		ricOrdini.setDataOrdineA("gg/mm/aaaa");
		ricOrdini.setDataOrdineAbbDa("gg/mm/aaaa");
		ricOrdini.setDataOrdineAbbA("gg/mm/aaaa");
		ricOrdini.setStato("");
		ricOrdini.setTipoInvio("");
		ricOrdini.setNumero("");
		ricOrdini.setEsercizio("2006");
		ricOrdini.setCapitolo("1");

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricOrdini = (StampaRepertoriForm) form;
		try {
			request.setAttribute("parametroPassato", ricOrdini.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricOrdini = (StampaRepertoriForm) form;
		try {
			if (ricOrdini.getTipoOrdine().equals("A"))
			{
				//Acquisto
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), true);
				return mapping.findForward("nuovoA");
			}
			else if (ricOrdini.getTipoOrdine().equals("L"))
			{
				//Deposito Legale
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("nuovoL");
			}
			else if (ricOrdini.getTipoOrdine().equals("D"))
			{
				//Dono
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("nuovoD");
			}
			else if (ricOrdini.getTipoOrdine().equals("C"))
			{
				//Scambio
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("nuovoC");
			}
			else if (ricOrdini.getTipoOrdine().equals("V"))
			{
				//Visione Trattenuta
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("nuovoV");
			}
			else
			{
				//DEFAULT SU ACQUISTO
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("nuovoA");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
}
