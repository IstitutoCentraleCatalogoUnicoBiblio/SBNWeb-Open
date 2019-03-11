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

import it.iccu.sbn.ejb.vo.servizi.serviziweb.ListaServiziVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.RiproduzioneForm;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web2.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
public class RiproduzioneAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.indietro", "indietro");
		map.put("servizi.bottone.ok", "ok");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RiproduzioneForm currentForm = (RiproduzioneForm)form;
		HttpSession session = request.getSession();

		Calendar data = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdierna =df.format(data.getTime());
		currentForm.setDataRichiesta(dataOdierna);

		currentForm.setTitolo((String)session.getAttribute(Constants.DOCUMENTO));
		currentForm.setSegnatura((String)request.getAttribute("segnatura"));
		UtenteWeb utenteEjb = null;
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		utenteEjb = factory.getGestioneServiziWeb().modErog();
		currentForm.setModErogazione(utenteEjb.getModErogazione());
		return mapping.getInputForward();
	}
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RiproduzioneForm currentForm = (RiproduzioneForm)form;
		String serv= currentForm.getServ().getCod_mod_erog();
		ListaServiziVO modErogaz = (ListaServiziVO)currentForm.getModErogazione().get(Integer.parseInt(serv)-1);
		request.setAttribute("MOD_EROG_SEL",modErogaz);

		return (mapping.findForward("riproduzioneSupporto"));
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RiproduzioneForm currentForm = (RiproduzioneForm)form;
		//request.setAttribute("segnatura",currentForm.getSegnatura());
		request.setAttribute("segnatura",currentForm.getSegnatura());
		return (mapping.findForward("sceltaServizi"));
	}

}
