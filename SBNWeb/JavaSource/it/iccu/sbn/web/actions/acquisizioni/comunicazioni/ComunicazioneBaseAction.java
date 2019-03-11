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
package it.iccu.sbn.web.actions.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni.ComunicazioneBaseForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public abstract class ComunicazioneBaseAction extends AcquisizioniBaseAction implements SbnAttivitaChecker {

	public ActionForward periodici(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ComunicazioneBaseForm currentForm = (ComunicazioneBaseForm) form;

		ComunicazioneVO com = currentForm.getDatiComunicazione();

		String tpMsg = com.getTipoMessaggio();
		if (!PeriodiciUtil.checkTipoMessaggioComunicazione(tpMsg)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.comunicazione.erroreTipoMsg"));
			return mapping.getInputForward();
		}

		String tipoDocumento = com.getTipoDocumento();
		if (!ValidazioneDati.in(tipoDocumento, "o", "O")) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.comunicazionierroreCampoTipoDocObbligatorio"));
			return mapping.getInputForward();
		}

		StrutturaTerna ordine = com.getIdDocumento();
		if (ordine == null  || !ValidazioneDati.isFilled(ordine.getCodice1())) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.comunicazionierroreDatiOrdineObbligatorio"));
			return mapping.getInputForward();
		}

		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(com.getCodPolo());
		bib.setCod_bib(com.getCodBibl());
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		return delegate.sifScegliFascicoliPerComunicazione(bib, com, NavigazioneAcquisizioni.SIF_KARDEX_RETURN);
	}


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		//almaviva5_20101122 gest. periodici
		map.put("ricerca.button.periodici", "periodici");

		return map;
	}



	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		checkFascicoliComunicazione(request, form);

		return super.execute(mapping, form, request, response);
	}



	private void checkFascicoliComunicazione(HttpServletRequest request, ActionForm form) throws Exception {
		List<FascicoloVO> fascicoli = (List<FascicoloVO>) request.getAttribute(NavigazioneAcquisizioni.SIF_KARDEX_RETURN);
		if (!ValidazioneDati.isFilled(fascicoli))
			return;

		ComunicazioneBaseForm currentForm = (ComunicazioneBaseForm) form;
		ComunicazioneVO com = currentForm.getDatiComunicazione();
		com.setFascicoli(fascicoli);
		//almaviva5_20111205 #4718
		//com.setNoteComunicazione(PeriodiciUtil.preparaNoteComunicazioneFascicoli(fascicoli));
		com.setNoteComunicazione(PeriodiciUtil.preparaConsistenzaFascicoli(fascicoli));
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		try {
			if (ValidazioneDati.equals(idCheck, "ESAME_FASCICOLI")) {
				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().PERIODICI);
				return true;
			}

			if (ValidazioneDati.equals(idCheck, "SIF_DA_PERIODICO")) {
				Navigation navi = Navigation.getInstance(request);
				return navi.bookmarksExist(PeriodiciDelegate.BOOKMARK_KARDEX, PeriodiciDelegate.BOOKMARK_FASCICOLO);
			}
		} catch (UtenteNotAuthorizedException e) {
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;
	}

}
