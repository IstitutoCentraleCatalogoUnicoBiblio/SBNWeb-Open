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
package it.iccu.sbn.web.actions.amministrazionesistema;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public abstract class ProfilazioneBaseAction extends NavigationBaseAction {

	@SuppressWarnings("unchecked")
	protected ActionForward checkLivAutBiblioteca(ActionMapping mapping,
			String codBib, List<GruppoParametriVO> elenco, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// almaviva5_20111207 #4722
		if (!ValidazioneDati.isFilled(elenco))
			return null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		@SuppressWarnings("rawtypes")
		List bibParams = factory.getBiblioteca().getElencoParametri(codBib);
		//lista mat+auth
		List<GruppoParametriVO> elencoBib = (List<GruppoParametriVO>) bibParams.get(0);
		elencoBib.addAll((List<GruppoParametriVO>) bibParams.get(1));
		//
		boolean error = false;
		for (GruppoParametriVO grp : elenco) {
			GruppoParametriVO authBib = BaseVO.searchRepeatableId(grp.getRepeatableId(), elencoBib);
			int livUte = Integer.valueOf(BaseVO.searchRepeatableId(NavigazioneProfilazione.LIVAUT_SEARCH_ID, grp.getElencoParametri()).getSelezione().trim());
			int livBib = Integer.valueOf(BaseVO.searchRepeatableId(NavigazioneProfilazione.LIVAUT_SEARCH_ID, authBib.getElencoParametri()).getSelezione().trim());
			if (livBib < livUte) {
				// livello utente maggiore livello bib.
				LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.livello.eccede.bib", grp.getDescrizione(), livBib), false);
				error = true;
			}

		}

		return error ? mapping.getInputForward() : null; // tutto ok

	}

	@SuppressWarnings("unchecked")
	protected ActionForward checkLivAutPolo(ActionMapping mapping,
			List<GruppoParametriVO> elenco, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// almaviva5_20111207 #4722
		if (!ValidazioneDati.isFilled(elenco))
			return null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		@SuppressWarnings("rawtypes")
		List poloParams = factory.getPolo().getElencoParametri();
		//lista mat+auth
		List<GruppoParametriVO> elencoPolo = (List<GruppoParametriVO>) poloParams.get(0);
		elencoPolo.addAll((List<GruppoParametriVO>) poloParams.get(1));
		//
		boolean error = false;
		for (GruppoParametriVO grp : elenco) {
			GruppoParametriVO authPolo = BaseVO.searchRepeatableId(grp.getRepeatableId(), elencoPolo);
			int livBibl = Integer.valueOf(BaseVO.searchRepeatableId(NavigazioneProfilazione.LIVAUT_SEARCH_ID, grp.getElencoParametri()).getSelezione().trim());
			int livPolo = Integer.valueOf(BaseVO.searchRepeatableId(NavigazioneProfilazione.LIVAUT_SEARCH_ID, authPolo.getElencoParametri()).getSelezione().trim());
			if (livPolo < livBibl) {
				// livello bib maggiore livello polo.
				LinkableTagUtils.addError(request, new ActionMessage("profilo.biblioteca.livello.eccede.polo", grp.getDescrizione(), livPolo), false);
				error = true;
			}

		}

		return error ? mapping.getInputForward() : null; // tutto ok

	}

	@SuppressWarnings("unchecked")
	protected ActionForward checkParSemBiblioteca(ActionMapping mapping,
			String codBib, List<GruppoParametriVO> uteParSem, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// almaviva5_20111207 #4722
		if (!ValidazioneDati.isFilled(uteParSem))
			return null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		@SuppressWarnings("rawtypes")
		List bibParams = factory.getBiblioteca().getElencoParametri(codBib);
		//parsem
		List<GruppoParametriVO> bibParSem = (List<GruppoParametriVO>) bibParams.get(2);

		boolean error = false;
		for (GruppoParametriVO ups : uteParSem) {
			GruppoParametriVO found = BaseVO.searchRepeatableId(ups.getRepeatableId(), bibParSem);
			if (found == null) {
				ParametroVO p = ups.getElencoParametri().get(0);
				LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.parsem.non.abilitato.bib",
						ups.getCodice() + " - " + p.getSelezione()), false);
				error = true;
			}
		}
		//

		return error ? mapping.getInputForward() : null; // tutto ok

	}

	@SuppressWarnings("unchecked")
	protected ActionForward checkParSemPolo(ActionMapping mapping,
			List<GruppoParametriVO> bibParSem, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// almaviva5_20111207 #4722
		if (!ValidazioneDati.isFilled(bibParSem))
			return null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		@SuppressWarnings("rawtypes")
		List poloParams = factory.getPolo().getElencoParametri();
		//parsem
		List<GruppoParametriVO> poloParSem = (List<GruppoParametriVO>) poloParams.get(2);

		boolean error = false;
		for (GruppoParametriVO bps : bibParSem) {
			GruppoParametriVO found = BaseVO.searchRepeatableId(bps.getRepeatableId(), poloParSem);
			if (found == null) {
				ParametroVO p = bps.getElencoParametri().get(0);
				LinkableTagUtils.addError(request, new ActionMessage("profilo.biblioteca.parsem.non.abilitato.polo",
					bps.getCodice() + " - " + p.getSelezione()), false);
				error = true;
			}
		}
		//

		return error ? mapping.getInputForward() : null; // tutto ok

	}

}
