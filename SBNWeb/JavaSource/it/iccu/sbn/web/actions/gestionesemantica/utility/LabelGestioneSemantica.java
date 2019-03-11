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
package it.iccu.sbn.web.actions.gestionesemantica.utility;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class LabelGestioneSemantica {

	private static Logger log = Logger.getLogger(LabelGestioneSemantica.class);

	private static final Class<?>[] TYPES = {
		ActionMapping.class,
		ActionForm.class,
		HttpServletRequest.class,
		HttpServletResponse.class
	};

	public static final List<ComboCodDescVO> getComboGestioneSematica(
			ServletContext ctx, HttpServletRequest request, ActionForm form,
			String[] tipoAuthority, SbnAttivitaChecker checker) {

		// lista caricata all'avvio di struts tramite xml di configurazione (gestionesemantica.xml)
		List<LabelGestioneSemanticaVO> labels = (List<LabelGestioneSemanticaVO>) ctx
				.getAttribute("labelGestioneSemantica");
		List<ComboCodDescVO> combo = new ArrayList<ComboCodDescVO>();
		combo.add(new ComboCodDescVO());
		for (LabelGestioneSemanticaVO label : labels) {

			if (!label.isGestione())
				continue;

			// filtro sul tipo oggetto
			if (tipoAuthority == null)
				continue;

			boolean found = false;
			for (String tipoAuth : tipoAuthority)
				if (ValidazioneDati.equals(tipoAuth, label.getTipoOggetto()) ) {
					found = true;
					break;
				}

			if (!found)	continue;

			// controllo abilitazioni
			if (checker != null
					&& !checker.checkAttivita(request, form, label.getId()))
				continue;

			ComboCodDescVO elem = new ComboCodDescVO();
			elem.setCodice(label.getId());
			elem.setDescrizione(label.getKey());
			combo.add(elem);
		}

		return combo;
	}

	public static final List<ComboCodDescVO> getComboGestioneSematicaPerLegame(
			ServletContext ctx, HttpServletRequest request, ActionForm form,
			String[] tipoAuthority, SbnAttivitaChecker checker) {

		// lista caricata all'avvio di struts tramite xml di configurazione (gestionesemantica.xml)
		List<LabelGestioneSemanticaVO> labels = (List<LabelGestioneSemanticaVO>) ctx
				.getAttribute("labelGestioneSemantica");
		List<ComboCodDescVO> combo = new ArrayList<ComboCodDescVO>();
		combo.add(new ComboCodDescVO());
		for (LabelGestioneSemanticaVO label : labels) {

			if (!label.isLegame())
				continue;

			// filtro sul tipo oggetto
			if (tipoAuthority == null)
				continue;

			boolean found = false;
			for (String tipoAuth : tipoAuthority)
				if (tipoAuth.equals(label.getTipoOggetto()) ) {
					found = true;
					break;
				}

			if (!found)	continue;

			// controllo abilitazioni
			if (checker != null
					&& !checker.checkAttivita(request, form, label.getId()))
				continue;

			ComboCodDescVO elem = new ComboCodDescVO();
			elem.setCodice(label.getId());
			elem.setDescrizione(label.getKey());
			combo.add(elem);
		}

		return combo;
	}

	public static final List<ComboCodDescVO> getComboGestioneSematicaPerEsamina(
			ServletContext ctx, HttpServletRequest request, ActionForm form,
			String[] tipoAuthority, SbnAttivitaChecker checker) {

		// lista caricata all'avvio di struts tramite xml di configurazione (gestionesemantica.xml)
		List<LabelGestioneSemanticaVO> labels = (List<LabelGestioneSemanticaVO>) ctx
				.getAttribute("labelGestioneSemantica");
		List<ComboCodDescVO> combo = new ArrayList<ComboCodDescVO>();
		combo.add(new ComboCodDescVO());
		for (LabelGestioneSemanticaVO label : labels) {

			if (!label.isEsamina() )
				continue;

			// filtro sul tipo oggetto
			// filtro sul tipo oggetto
			if (tipoAuthority == null)
				continue;

			boolean found = false;
			for (String tipoAuth : tipoAuthority)
				if (ValidazioneDati.equals(tipoAuth, label.getTipoOggetto()) ) {
					found = true;
					break;
				}

			if (!found)	continue;

			// controllo abilitazioni
			if (checker != null
					&& !checker.checkAttivita(request, form, label.getId()))
				continue;

			ComboCodDescVO elem = new ComboCodDescVO();
			elem.setCodice(label.getId());
			elem.setDescrizione(label.getKey());
			combo.add(elem);
		}

		return combo;
	}

	public static final List<ComboCodDescVO> getComboGestioneSematica(
			ServletContext ctx, HttpServletRequest request, ActionForm form,
			TreeElementViewSoggetti nodoSelezionato, SbnAttivitaChecker checker) {

		String tipoAuthority = null;
		if (nodoSelezionato != null && nodoSelezionato.getTipoAuthority() != null)
			tipoAuthority = nodoSelezionato.getTipoAuthority().toString();

		return getComboGestioneSematica(ctx, request, form, new String[]{tipoAuthority}, checker);
	}

	public static final List<ComboCodDescVO> getComboGestioneSematicaPerEsamina(
			ServletContext ctx, HttpServletRequest request, ActionForm form,
			TreeElementViewSoggetti nodoSelezionato, SbnAttivitaChecker checker) {

		String tipoAuthority = null;
		if (nodoSelezionato != null && nodoSelezionato.getTipoAuthority() != null)
			tipoAuthority = nodoSelezionato.getTipoAuthority().toString();

		return getComboGestioneSematicaPerEsamina(ctx, request, form, new String[]{tipoAuthority}, checker);
	}

	public static final LabelGestioneSemanticaVO getComboGestioneSematicaLabel(ServletContext ctx, String id) {
		// lista caricata all'avvio di struts tramite xml di configurazione (gestionesemantica.xml)
		List<LabelGestioneSemanticaVO> labels = (List<LabelGestioneSemanticaVO>) ctx
				.getAttribute("labelGestioneSemantica");
		for (LabelGestioneSemanticaVO label : labels) {
			if (label.getId().equals(id))
				return label;
		}

		return null;
	}

	public static final ActionForward invokeActionMethod(String idFunzione,
			ServletContext ctx, Action action, ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			// lista caricata all'avvio di struts tramite xml di configurazione
			// (gestionesemantica.xml)
			List<LabelGestioneSemanticaVO> labels = (List<LabelGestioneSemanticaVO>) ctx
					.getAttribute("labelGestioneSemantica");

			for (LabelGestioneSemanticaVO label : labels) {
				if (!label.getId().equals(idFunzione))
					continue;

				Method method = action.getClass().getMethod(label.getMethod(), TYPES);
				Object args[] = { mapping, form, request, response };
				ActionForward forward = (ActionForward) method.invoke(action, args);

				return forward;
			}

			return null;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

}
