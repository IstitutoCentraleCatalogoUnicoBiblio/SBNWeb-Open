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
package it.iccu.sbn.web.actions.acquisizioni.suggerimenti;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.web.actionforms.acquisizioni.suggerimenti.SugBiblBaseForm;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public abstract class SugBiblBaseAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.label.sezione", "sezioneCerca");
		return map;
	}

	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SugBiblBaseForm currentForm = (SugBiblBaseForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// &&
			// request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

			/*
			 * if
			 * (request.getSession().getAttribute("criteriRicercaSezione")==null
			 * ) {
			 * request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE
			 * ); request.getSession().removeAttribute("sezioniSelected");
			 * request.getSession().removeAttribute("criteriRicercaSezione"); }
			 * else { //throw new Exception("limite di ricorsione");
			 * ActionMessages errors = new ActionMessages();
			 * errors.add("generico", new
			 * ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
			 * this.saveErrors(request, errors); return
			 * mapping.getInputForward(); }
			 */
			this.impostaSezioneCerca(currentForm, request, mapping);
			// return mapping.findForward("sezioneCerca");
			return mapping.findForward("sezioneLista");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaSezioneCerca(SugBiblBaseForm currentForm,
			HttpServletRequest request, ActionMapping mapping) {
		// impostazione di una lista di supporto
		try {
			ListaSuppSezioneVO eleRicerca = new ListaSuppSezioneVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo = Navigation.getInstance(request).getUtente()
					.getCodPolo();
			String codP = polo;
			String codB = currentForm.getDatiSuggerimento().getCodBibl();
			String codSez = currentForm.getDatiSuggerimento().getSezione()
					.getCodice();
			String desSez = "";
			String chiama = mapping.getPath();
			String ordina = "";
			eleRicerca = new ListaSuppSezioneVO(codP, codB, codSez, desSez,
					chiama, ordina);
			request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
		} catch (Exception e) {
		}
	}

	protected void controlloSezione(ActionMapping mapping, HttpServletRequest request,
			SugBiblBaseForm currentForm) {
		ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
		if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
		{
			if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
			{
				if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
				{
					currentForm.getDatiSuggerimento().getSezione().setCodice(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
					currentForm.getDatiSuggerimento().getSezione().setDescrizione(ricSez.getSelezioniChiamato().get(0).getDescrizioneSezione());
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				currentForm.getDatiSuggerimento().getSezione().setCodice("");
				currentForm.getDatiSuggerimento().getSezione().setDescrizione("");
			}

			// il reset dell'attributo di sessione deve avvenire solo dal chiamante
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

		}
	}

}
