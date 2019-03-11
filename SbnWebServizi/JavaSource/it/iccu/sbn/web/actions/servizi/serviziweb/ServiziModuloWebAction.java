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

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.LogoutDelegate;
import it.iccu.sbn.web2.util.Constants;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public abstract class ServiziModuloWebAction extends LookupDispatchAction {

	public ServiziModuloWebAction() {
		super();
	}

	protected ActionForward selectNextForwardRichiesta(HttpServletRequest request, ActionMapping mapping,
			UtenteWeb uteWeb, RichiestaOpacVO richiesta) throws Exception {
		switch (richiesta.getTipo()) {
		case SALA:
			ParametriServizi parametri = new ParametriServizi();
			RicercaGrigliaCalendarioVO grigliaCalendario = new RicercaGrigliaCalendarioVO();
			UtenteBaseVO utente = ServiziDelegate.getInstance(request).getUtente(uteWeb.getUserId());
			grigliaCalendario.setUtente(utente);

			grigliaCalendario.getCd_cat_mediazione().add(richiesta.getTipoMediazione());
			parametri.put(ParamType.GRIGLIA_CALENDARIO, grigliaCalendario);
			parametri.put(ParamType.MODALITA_GESTIONE_PRENOT_POSTO, ModalitaGestioneType.CREA);
			BibliotecaVO bib = (BibliotecaVO) request.getSession().getAttribute(Constants.BIBLIO);
			parametri.put(ParamType.BIBLIOTECA, bib);
			ParametriServizi.send(request, parametri);

			return mapping.findForward("prenotazionePosto");	//griglia calendario

		case INVENTARIO:
			return mapping.findForward("listaMovimentiOpac");	//mappa selezione servizio

		case RICHIESTA_ILL:
		default:
			LogoutDelegate.clear(request);
			return mapping.findForward("login");

		}
	}

}
