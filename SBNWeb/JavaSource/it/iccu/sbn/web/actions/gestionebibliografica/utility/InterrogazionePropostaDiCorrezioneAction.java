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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.InterrogazionePropostaDiCorrezioneForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

public class InterrogazionePropostaDiCorrezioneAction extends LookupDispatchAction {

	private static Log log = LogFactory
			.getLog(InterrogazionePropostaDiCorrezioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.cercaPropostaCorr", "cercaPropostaCorr");
		map.put("button.creaPropostaCorr", "creaPropostaCorr");
		map.put("button.annullaPropostaCorr", "annullaTornaInter");

		return map;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.execute(mapping, form, request, response);
	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazionePropostaDiCorrezioneForm propostaDiCorrezioneForm = (InterrogazionePropostaDiCorrezioneForm) form;


		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		// Inizio Modifica almaviva2 BUG 3900 26.10.2010 i check richiestiDaMe e richiestiAMe vengono trasformati in Radio Button esclusivi;
		// Inizializzazioni
//		propostaDiCorrezioneForm.setRichAMe(false);
//		propostaDiCorrezioneForm.setRichDaMe(false);
//		propostaDiCorrezioneForm.setRichDaMeUserid(false);

		propostaDiCorrezioneForm.setTipoRichiesta("richAMe");
		// Fine Modifica almaviva2 BUG 3900 26.10.2010




		// Inizio gestione lista per tipo Stato proposta
		ComboCodDescVO comboCodDescVO;
		// Predisposizione lista Stati Proposta

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("");
		comboCodDescVO.setDescrizione("");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("I");
		comboCodDescVO.setDescrizione("inserita");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("E");
		comboCodDescVO.setDescrizione("evasa");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("R");
		comboCodDescVO.setDescrizione("respinta");
		propostaDiCorrezioneForm.addListaStatiProposta(comboCodDescVO);

		log.debug("unspecified()");
		if (request.getParameter("SINTETICA") != null) {
			if (request.getAttribute("areaDatiPropostaDiCorrezioneVO") != null) {
				propostaDiCorrezioneForm.setAreaDatiPropostaDiCorrezioneVO(
						(AreaDatiPropostaDiCorrezioneVO) request.getAttribute("areaDatiPropostaDiCorrezioneVO"));
				ActionForward forward = cercaPropostaCorr(mapping, propostaDiCorrezioneForm, request, response);
				propostaDiCorrezioneForm.setTastoCreaPresente("SI");
				return forward;
			}
		}

		if (propostaDiCorrezioneForm.getTipoProspettazione() == null) {
			propostaDiCorrezioneForm.setTipoProspettazione("INT");
		}
		// Richiesta esplicita BUG 3003
		propostaDiCorrezioneForm.setTastoCreaPresente("SI");

		return mapping.getInputForward();
	}

	public ActionForward cercaPropostaCorr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazionePropostaDiCorrezioneForm propostaDiCorrezioneForm = (InterrogazionePropostaDiCorrezioneForm) form;
		propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setMittenteProposta(null);
		propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setUseridProposta(null);
		propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setDestinatarioProposta(null);

		// Inizio Modifica almaviva2 14.01.2010 BUG 3454
		// Inizio verifica dell'Impostazione sul range di date e se presente sulla loro correttezza
		// Manutenzione 12.03.2010 almaviva2 3626 - controllo sul null
		if (propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getDataInserimentoPropostaDa() != null) {
			if (!propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getDataInserimentoPropostaDa().equals("")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				  long longData1 = 0;
				  long longData2 = 0;

				if (propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getDataInserimentoPropostaA().equals("")) {
					propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setDataInserimentoPropostaA(
							propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getDataInserimentoPropostaDa());
				}
				try {
					  Date data1 = simpleDateFormat.parse(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getDataInserimentoPropostaDa());
					  Date data2 = simpleDateFormat.parse(propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getDataInserimentoPropostaA());
					  longData1 = data1.getTime();
					  longData2 = data2.getTime();
				  } catch (ParseException e) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins024"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
				  }

				  if ((longData2 - longData1) < 0) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric002"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
				  }
			}
		}
		// Fine verifica dell'Impostazione sul range di date e se presente sulla loro correttezza
		// Fine Modifica almaviva2 14.01.2010 BUG 3454


		// Inizio Modifica almaviva2 BUG 3900 26.10.2010 i check richiestiDaMe e richiestiAMe vengono trasformati in Radio Button esclusivi;
//		if (propostaDiCorrezioneForm.isRichDaMe()) {
//			propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setMittenteProposta(Navigation.getInstance(request).getUtente().getCodPolo()
//					+ Navigation.getInstance(request).getUtente().getCodBib());
//			if (propostaDiCorrezioneForm.isRichDaMeUserid()) {
//				propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setUseridProposta(Navigation.getInstance(request).getUtente().getUserId());
//			}
//		}
//
//		if (propostaDiCorrezioneForm.isRichAMe()) {
//			propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setDestinatarioProposta(Navigation.getInstance(request).getUtente().getCodPolo()
//					+ Navigation.getInstance(request).getUtente().getCodBib());
//		}

		if (propostaDiCorrezioneForm.getTipoRichiesta().equals("richAMe")) {
			propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setDestinatarioProposta(Navigation.getInstance(request).getUtente().getCodPolo()
					+ Navigation.getInstance(request).getUtente().getCodBib());
		} else {
			propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().setMittenteProposta(Navigation.getInstance(request).getUtente().getCodPolo()
					+ Navigation.getInstance(request).getUtente().getCodBib());
		}
		// Fine Modifica almaviva2 BUG 3900 26.10.2010


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVOReturn = factory.getGestioneBibliografica().cercaPropostaDiCorrezione(
				propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO(), Navigation.getInstance(request).getUserTicket());

		if (areaDatiPropostaDiCorrezioneVOReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}
		if (areaDatiPropostaDiCorrezioneVOReturn.getCodErr().equals("3001")
				|| areaDatiPropostaDiCorrezioneVOReturn.getCodErr().equals("3013")) {
			// manutenzione Vedi BUG 2829
			propostaDiCorrezioneForm.setTastoCreaPresente("SI");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.propCorrNotFound"));
			this.saveErrors(request, errors);
//			ActionForward forward = creaPropostaCorr(mapping, propostaDiCorrezioneForm, request, response);
			return mapping.getInputForward();


		} else if (areaDatiPropostaDiCorrezioneVOReturn.getCodErr().equals("9999") || areaDatiPropostaDiCorrezioneVOReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo" ,areaDatiPropostaDiCorrezioneVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPropostaDiCorrezioneVOReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPropostaDiCorrezioneVOReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		request.setAttribute("areaDatiPropostaDiCorrezioneVO", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO());
		if (propostaDiCorrezioneForm.getTipoRichiesta().equals("richAMe")) {
			request.setAttribute("PresenzaTastoInsRisposta", "SI");
		} else {
			request.setAttribute("PresenzaTastoInsRisposta", "NO");
		}
		return mapping.findForward("sinteticaPropostaCorr");

	}


	public ActionForward creaPropostaCorr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazionePropostaDiCorrezioneForm propostaDiCorrezioneForm = (InterrogazionePropostaDiCorrezioneForm) form;

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
		return mapping.findForward("creaPropostaCorr");
	}


	public ActionForward annullaTornaInter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			InterrogazionePropostaDiCorrezioneForm propostaDiCorrezioneForm = (InterrogazionePropostaDiCorrezioneForm) form;

			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			request.setAttribute("bid", propostaDiCorrezioneForm.getAreaDatiPropostaDiCorrezioneVO().getIdOggettoProposta());
			request.setAttribute("livRicerca", "I");
			return Navigation.getInstance(request).goBack();
	}

}
