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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.marca;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.SinteticaImmaginiMarcheView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.marca.SinteticaImmaginiMarcheForm;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaImmaginiMarcheAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(SinteticaImmaginiMarcheAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.analiticaMar", "analiticaMar");
		map.put("button.gestLegami.lega", "prospettaPerLegame");
		map.put("button.annulla", "annullaOper");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		log.info("SinteticaImmaginiMarcheAction::unspecified");
		SinteticaImmaginiMarcheForm sinteticaImmaginiMarcheForm = (SinteticaImmaginiMarcheForm) form;
		sinteticaImmaginiMarcheForm.setLivRicerca((String)request.getAttribute("livRicerca"));
		List<SinteticaImmaginiMarcheView> listaSintImmagini =
			(List<SinteticaImmaginiMarcheView>)request.getAttribute("listaSintImmagini");
		if (ValidazioneDati.isFilled(listaSintImmagini))
			sinteticaImmaginiMarcheForm.setSelezImg(listaSintImmagini.get(0).getMid());

		sinteticaImmaginiMarcheForm.setListaSintImmagini(listaSintImmagini);

		if (request.getParameter("SINTNEWLEGAME") != null) {
			sinteticaImmaginiMarcheForm.setProspettazionePerLegami("SI");
			sinteticaImmaginiMarcheForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			sinteticaImmaginiMarcheForm.setProspettaDatiOggColl("SI");
			sinteticaImmaginiMarcheForm.setIdOggColl(sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			sinteticaImmaginiMarcheForm.setDescOggColl(sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().getDescPartenza());
		}

		return mapping.getInputForward();
	}

	public ActionForward analiticaMar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaImmaginiMarcheForm sinteticaImmaginiMarcheForm = (SinteticaImmaginiMarcheForm) form;

		if (sinteticaImmaginiMarcheForm.getSelezImg() != null) {
			String midNome = sinteticaImmaginiMarcheForm.getSelezImg();
			request.setAttribute("bid", midNome.substring(0, 10));
			request.setAttribute("livRicerca", sinteticaImmaginiMarcheForm.getLivRicerca());
			request.setAttribute("tipoAuthority", "MA");
			resetToken(request);
			return mapping.findForward("analitica");
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
		}
		return mapping.getInputForward();
	}


	public ActionForward prospettaPerLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaImmaginiMarcheForm sinteticaImmaginiMarcheForm = (SinteticaImmaginiMarcheForm) form;

		if (sinteticaImmaginiMarcheForm.getSelezImg() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setIdArrivo(sinteticaImmaginiMarcheForm.getSelezImg().substring(0,10));
		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("MA");

		SinteticaImmaginiMarcheView eleSinteticaImmaginiMarcheView = null;
		String midDaLegare = sinteticaImmaginiMarcheForm.getSelezImg().substring(0,10);
		for (int i = 0; i < sinteticaImmaginiMarcheForm.getListaSintImmagini().size(); i++) {
			eleSinteticaImmaginiMarcheView = sinteticaImmaginiMarcheForm.getListaSintImmagini().get(i);
			if (eleSinteticaImmaginiMarcheView.getMid().equals(midDaLegare)) {
				sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setDescArrivo(eleSinteticaImmaginiMarcheView.getNome());
				break;
			}
		}

		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setSiciNew("");
		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
		sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

		request.setAttribute("AreaDatiLegameTitoloVO", sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO());
		if (sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza() == null) {
			return mapping.findForward("gestioneLegameTitoloMarca");
		}
		if (sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("AU") ||
				sinteticaImmaginiMarcheForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("MA")) {
			return mapping.findForward("gestioneLegameFraAutority");
		}
		return mapping.getInputForward();
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		return Navigation.getInstance(request).goBack(true);
	}


}
