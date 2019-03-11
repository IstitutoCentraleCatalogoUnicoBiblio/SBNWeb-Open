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

package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaProposteDiCorrezioneView;
import it.iccu.sbn.web.actionforms.gestionebibliografica.SinteticaPropostaDiCorrezioneForm;
import it.iccu.sbn.web2.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class SinteticaPropostaDiCorrezioneAction extends LookupDispatchAction {

	private static Log log = LogFactory
			.getLog(SinteticaPropostaDiCorrezioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.creaPropostaCorr", "creaPropostaCorr");
		map.put("button.variaPropostaCorr", "variaPropostaCorr");
		map.put("button.dettaglio", "dettaglioPropostaCorr");
		map.put("button.annullaPropostaCorr", "annullaTornaInter");

		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaPropostaDiCorrezioneForm propostaDiCorrezioneForm = (SinteticaPropostaDiCorrezioneForm) form;


		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");
		if (request.getAttribute("areaDatiPropostaDiCorrezioneVO") != null) {
			propostaDiCorrezioneForm.setAreaDatiPropostaDiCorrezioneVO(
					(AreaDatiPropostaDiCorrezioneVO) request.getAttribute("areaDatiPropostaDiCorrezioneVO"));
			propostaDiCorrezioneForm.setTipoProspettazione("SINcreaOK");
		} else {
			propostaDiCorrezioneForm.setTipoProspettazione("SINcreaKO");
		}
		propostaDiCorrezioneForm.setListaSintetica(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getListaSintetica());

		if (propostaDiCorrezioneForm.getListaSintetica() != null) {
			if (propostaDiCorrezioneForm.getListaSintetica().size() == 1) {
				SinteticaProposteDiCorrezioneView correzioneView = new SinteticaProposteDiCorrezioneView();
				correzioneView = (SinteticaProposteDiCorrezioneView) propostaDiCorrezioneForm.getListaSintetica().get(0);
				propostaDiCorrezioneForm.setSelezRadio(String.valueOf(correzioneView.getIdProposta()));
			}
		}

		if (request.getAttribute("PresenzaTastoInsRisposta") != null) {
			propostaDiCorrezioneForm.setPresenzaTastoInsRisposta((String) request.getAttribute("PresenzaTastoInsRisposta"));
		}

		return mapping.getInputForward();
	}

	public ActionForward variaPropostaCorr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaPropostaDiCorrezioneForm propostaDiCorrezioneForm = (SinteticaPropostaDiCorrezioneForm) form;

		String idProposta = "";
		if (propostaDiCorrezioneForm.getSelezRadio() != null
				&& !propostaDiCorrezioneForm.getSelezRadio().equals("")) {
			idProposta = propostaDiCorrezioneForm.getSelezRadio();
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		SinteticaProposteDiCorrezioneView proposteDiCorrezioneView;
		proposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();
		for (int i = 0; i < propostaDiCorrezioneForm.getListaSintetica().size(); i++) {

			proposteDiCorrezioneView = (SinteticaProposteDiCorrezioneView) propostaDiCorrezioneForm.getListaSintetica().get(i);
			if ((String.valueOf(proposteDiCorrezioneView.getIdProposta()).equals(idProposta))) {
				propostaDiCorrezioneForm.setProposteDiCorrezioneView(proposteDiCorrezioneView);
				request.setAttribute("proposteDiCorrezioneView", proposteDiCorrezioneView);
				break;
			}
		}
		request.setAttribute("tipoOperazione", "VARIA");
		return mapping.findForward("creaPropostaCorr");
	}

	public ActionForward dettaglioPropostaCorr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaPropostaDiCorrezioneForm propostaDiCorrezioneForm = (SinteticaPropostaDiCorrezioneForm) form;

		String idProposta = "";
		if (propostaDiCorrezioneForm.getSelezRadio() != null
				&& !propostaDiCorrezioneForm.getSelezRadio().equals("")) {
			idProposta = propostaDiCorrezioneForm.getSelezRadio();
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		SinteticaProposteDiCorrezioneView proposteDiCorrezioneView;
		proposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();
		for (int i = 0; i < propostaDiCorrezioneForm.getListaSintetica().size(); i++) {

			proposteDiCorrezioneView = (SinteticaProposteDiCorrezioneView) propostaDiCorrezioneForm.getListaSintetica().get(i);
			if ((String.valueOf(proposteDiCorrezioneView.getIdProposta()).equals(idProposta))) {
				propostaDiCorrezioneForm.setProposteDiCorrezioneView(proposteDiCorrezioneView);
				request.setAttribute("proposteDiCorrezioneView", proposteDiCorrezioneView);
				break;
			}
		}
		request.setAttribute("tipoOperazione", "DETTAGLIO");
		return mapping.findForward("creaPropostaCorr");
	}


	public ActionForward creaPropostaCorr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaPropostaDiCorrezioneForm propostaDiCorrezioneForm = (SinteticaPropostaDiCorrezioneForm) form;

		SinteticaProposteDiCorrezioneView sinteticaProposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();

		sinteticaProposteDiCorrezioneView.setIdOggetto(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getIdOggettoProposta());
		sinteticaProposteDiCorrezioneView.setTipoAuthority(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getTipoAuthorityProposta());
		sinteticaProposteDiCorrezioneView.setTipoMateriale(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getTipoMaterialeProposta());
		sinteticaProposteDiCorrezioneView.setNatura(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getNaturaProposta());
		sinteticaProposteDiCorrezioneView.setIdProposta(0);
		sinteticaProposteDiCorrezioneView.setStatoProposta("I");
		sinteticaProposteDiCorrezioneView.setMittenteBiblioteca(
				Navigation.getInstance(request).getUtente().getCodPolo() + " " + Navigation.getInstance(request).getUtente().getCodBib());
		sinteticaProposteDiCorrezioneView.setMittenteUserId(
				Navigation.getInstance(request).getUtente().getUserId());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdierna = df.format(calendar.getTime());
		sinteticaProposteDiCorrezioneView.setDataInserimento(dataOdierna);

		request.setAttribute("proposteDiCorrezioneView", sinteticaProposteDiCorrezioneView);
		request.setAttribute("tipoOperazione", "CREA");
		return mapping.findForward("creaPropostaCorr");
	}

	public ActionForward annullaTornaInter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		SinteticaPropostaDiCorrezioneForm propostaDiCorrezioneForm = (SinteticaPropostaDiCorrezioneForm) form;

//		 Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute("areaDatiPropostaDiCorrezioneVO", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO());
		request.setAttribute("bid", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getIdOggettoProposta());
		request.setAttribute("livRicerca", "I");
		return Navigation.getInstance(request).goBack();

	}


}
