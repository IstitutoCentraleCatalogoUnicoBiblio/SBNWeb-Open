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
package it.iccu.sbn.web.actions.servizi.serviziweb.sale;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.sale.PrenotazionePostoForm;
import it.iccu.sbn.web.integration.bd.serviziweb.LogoutDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrenotazionePostoAction
		extends it.iccu.sbn.web.integration.action.sale.GestionePrenotazionePostoAction {

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		super.unspecified(mapping, form, request, response);

		if (navi.isFirst()) {
			PrenotazionePostoForm currentForm = (PrenotazionePostoForm) form;
			RicercaGrigliaCalendarioVO ricerca = currentForm.getRicerca();
			String cd_cat_mediazione = ValidazioneDati.first(ricerca.getCd_cat_mediazione());
			TB_CODICI cod = CodiciProvider.cercaCodice(cd_cat_mediazione,
					CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE, CodiciRicercaType.RICERCA_CODICE_SBN);
			if (cod == null) {
				ValidationException ve = new ValidationException(
						SbnErrorTypes.SRV_ERROR_SALE_CAT_MEDIAZIONE_NON_TROVATA_NON_GESTITA);
				LinkableTagUtils.addError(request, ve);
				return annulla(mapping, currentForm, request, response);
			}
			currentForm.setDescrCatMediazione(cod.getDs_tabella());
		}
		return mapping.getInputForward();
	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		Navigation navi = Navigation.getInstance(request);
		PrenotazionePostoForm currentForm = (PrenotazionePostoForm) form;
		if (ValidazioneDati.equals(idCheck, "DATI_DOCUMENTO") ) {
			PrenotazionePostoVO pp = currentForm.getPrenotazione();
			return !navi.isFirst() && pp != null && !pp.isNuovo() && pp.isWithMovimento();
		}

		if (ValidazioneDati.equals(idCheck, "SERVIZIO") ) {
			return ValidazioneDati.isFilled(currentForm.getMovimenti())
					&& super.checkAttivita(request, currentForm, "MESE");
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.scegli") ) {
			//pulsante spostato da bottoniera a lista (ripetuto)
			return false;
		}

		if (ValidazioneDati.equals(idCheck, "MEDIAZIONE") ) {
			return !ValidazioneDati.isFilled(currentForm.getMovimenti())
					&& super.checkAttivita(request, currentForm, "MESE")
					&& ValidazioneDati.isFilled(currentForm.getRicerca().getCd_cat_mediazione());
		}


		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ok")) {
			return false;
		}

		return super.checkAttivita(request, form, idCheck);
	}

	@Override
	public ActionForward annulla(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//se la pagina è attivata da link esterno eseguo il logout e torno alla pagina di accesso,
		//altrimenti si mantiene la navigazione all'indietro (come nel gestionale)
		if (Navigation.getInstance(request).isFirst() ) {
			LogoutDelegate.clear(request);
			return (mapping.findForward("login"));
		} else
			return super.annulla(mapping, form, request, response);
	}

	@Override
	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.si(mapping, form, request, response);
		Navigation.getInstance(request).purgeThis();
		return mapping.findForward("prenotazioni");
	}

}
