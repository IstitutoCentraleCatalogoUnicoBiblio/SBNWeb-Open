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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloCartografiaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGraficaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloMusicaVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.EsaminaTitoliConFiltroForm;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;


public class EsaminaTitoliConFiltroAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(InterrogazioneTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("ricerca.button.aggiornaCanali", "aggTipoRecord");
		map.put("ricerca.button.cerca", "cercaTit");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.debug("unspecified()");
		EsaminaTitoliConFiltroForm esaTitCF = (EsaminaTitoliConFiltroForm) form;

		//Viene settato il token per le transazioni successive
		this.saveToken(request);

//		Impostazione di inizializzazione jsp
		esaTitCF.setBidTitRifer((String) request
				.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
		esaTitCF.setDescBidTitRifer((String) request
				.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));

		esaTitCF.getInterrGener().setTitolo("");
		esaTitCF.getInterrGener().setTitoloPunt(false);
		esaTitCF.getInterrGener().setNumStandardSelez("");
		esaTitCF.getInterrGener().setNumStandard1("");
		esaTitCF.getInterrGener().setNumStandard2("");
		esaTitCF.getInterrGener().setImpronta1("");
		esaTitCF.getInterrGener().setImpronta2("");
		esaTitCF.getInterrGener().setImpronta3("");

        InterrogazioneTitoloAction interrTit = new InterrogazioneTitoloAction();
        InterrogazioneTitoloGeneraleVO titGenVO = new InterrogazioneTitoloGeneraleVO();

        interrTit.caricaComboGenerTitolo(titGenVO, "NO");
        titGenVO.setTipoMateriale("M");
		esaTitCF.setInterrGener(titGenVO);


// Impostazione del diagnostico eventualmente impostato

		return mapping.getInputForward();
	}

	public ActionForward aggTipoRecord(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		EsaminaTitoliConFiltroForm esaTitCF = (EsaminaTitoliConFiltroForm) form;

		String tipoRec = esaTitCF.getInterrGener().getTipiRecordSelez().toUpperCase();

		InterrogazioneTitoloAction interrTit = new InterrogazioneTitoloAction();
		InterrogazioneTitoloGeneraleVO titGenVO = new InterrogazioneTitoloGeneraleVO();
		if ((tipoRec.equals("A") || tipoRec.equals("B")) && esaTitCF.getInterrGener().isMatAntico()) {
	        interrTit.caricaComboGenerTitolo(titGenVO, "NO");
			titGenVO.setTipoMateriale("E");
			esaTitCF.setInterrGener(titGenVO);
			return mapping.getInputForward();
		}
		if (tipoRec.equals("A") || tipoRec.equals("B")) {
	        interrTit.caricaComboGenerTitolo(titGenVO, "NO");
			titGenVO.setTipoMateriale("M");
			esaTitCF.setInterrGener(titGenVO);
			return mapping.getInputForward();
		}
		if (tipoRec.equals("C") || tipoRec.equals("D")) {
			InterrogazioneTitoloMusicaVO titMusVO = new InterrogazioneTitoloMusicaVO();
	        interrTit.caricaComboGenerTitolo(titGenVO, "NO");
			titGenVO.setTipoMateriale("U");
			esaTitCF.setInterrGener(titGenVO);
			interrTit.inizCaricaMusic(titMusVO);
			esaTitCF.setInterrMusic(titMusVO);
			return mapping.getInputForward();
		}

		if (tipoRec.equals("E") || tipoRec.equals("F")) {
			InterrogazioneTitoloCartografiaVO titCarVO = new InterrogazioneTitoloCartografiaVO();
	        interrTit.caricaComboGenerTitolo(titGenVO, "NO");
			titGenVO.setTipoMateriale("C");
			esaTitCF.setInterrGener(titGenVO);
			interrTit.inizCaricaCartog(titCarVO);
			esaTitCF.setInterrCartog(titCarVO);
			return mapping.getInputForward();
		}
		if (tipoRec.equals("K")) {
			InterrogazioneTitoloGraficaVO titGraVO = new InterrogazioneTitoloGraficaVO();
	        interrTit.caricaComboGenerTitolo(titGenVO, "NO");
			titGenVO.setTipoMateriale("G");
			esaTitCF.setInterrGener(titGenVO);
			interrTit.inizCaricaGrafic(titGraVO);
			esaTitCF.setInterrGrafic(titGraVO);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
    }


	public ActionForward cercaTit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.findForward("sinteticaTitoli");
    }


	public ActionForward creaTit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.findForward("creaTitolo");
    }

}
