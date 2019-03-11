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
package it.iccu.sbn.web.actions.amministrazionesistema.codici;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.TabellaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.actionforms.amministrazionesistema.codici.GestioneCodiceForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.GestioneCodiciDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class GestioneCodiceAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(GestioneCodiceAction.class);

	private GestioneCodiciDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new GestioneCodiciDelegate(FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dettaglio.codici.button.indietro", "annulla");
		map.put("dettaglio.codici.button.ok", "ok");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		GestioneCodiceForm currentForm = (GestioneCodiceForm) form;
		if (!currentForm.isInitialized()) {
			log.debug("GestioneCodiceAction:unspecified");
			CodiceConfigVO config = (CodiceConfigVO) request.getAttribute(GestioneCodiciDelegate.CONFIG_CODICI);
			currentForm.setConfigCodici(config);
			CodiceVO dettaglio = (CodiceVO) request.getAttribute(GestioneCodiciDelegate.DETTAGLIO_CODICE);
			currentForm.setDettaglio(dettaglio);
			currentForm.setInitialized(true);
			List<CodiceVO> listaCodici = (List<CodiceVO>) request.getAttribute(GestioneCodiciDelegate.LISTA_CODICI);
			preparaCodiciKeySet(form, listaCodici);
			if (dettaglio.isNuovo() ) {
				navi.setTesto(".amministrazionesistema.gestioneCodice.INS.testo");
				navi.setDescrizioneX(".amministrazionesistema.gestioneCodice.INS.descrizione");
			}

			if (config.getTipoTabella() == TabellaType.CROSS ) {
				GestioneCodiciDelegate delegate = getDelegate(request);
				currentForm.setListaCodiciP(delegate.getListaCodici(config.getTpTabellaP()));
				currentForm.setListaCodiciC(delegate.getListaCodici(config.getTpTabellaC()));
			}
		}

		return mapping.getInputForward();
	}

	private void preparaCodiciKeySet(ActionForm form, List<CodiceVO> listaCodici) {

		if (!ValidazioneDati.isFilled(listaCodici))
			return;

		Set<String> keySet = new HashSet<String>();
		GestioneCodiceForm currentForm = (GestioneCodiceForm) form;
		currentForm.setCodiciKeySet(keySet);
		CodiceConfigVO config = currentForm.getConfigCodici();

		switch (config.getTipoTabella()) {
		case DICT:
			for (CodiceVO codice : listaCodici)
				keySet.add(codice.getCdTabella().trim());
			break;
		case CROSS:
			for (CodiceVO codice : listaCodici)
				keySet.add(codice.getFlag1().trim() + "-" + codice.getFlag2().trim());
			break;
		}
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneCodiceForm currentForm = (GestioneCodiceForm) form;
		CodiceConfigVO config = currentForm.getConfigCodici();
		CodiceVO dettaglio = currentForm.getDettaglio();

		try {
			config.validate(dettaglio);

		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage(), e.getLabels()));

			return mapping.getInputForward();
		}

		// le tabelle cross hanno nel campo cdTabella
		// la concatenazione flag1+flag2
		if (config.getTipoTabella() == TabellaType.CROSS) {
			String code = dettaglio.getFlag1() + "-" + dettaglio.getFlag2();
			dettaglio.setCdTabella(code);
			dettaglio.setDescrizione(code);
		}

		if (isChiaveDuplicata(currentForm)) {

			LinkableTagUtils.addError(request, new ActionMessage("dettaglio.codici.unico"));

			return mapping.getInputForward();
		}

		GestioneCodiciDelegate delegate = getDelegate(request);
		boolean ok = delegate.salvaCodice(dettaglio, true);
		if (!ok)
			return mapping.getInputForward();

		DescrittoreBloccoVO blocco1 = delegate.cercaTabellaCodici(config.getCdTabella(), 20000);
		if (blocco1 == null)
			return mapping.getInputForward();

		request.setAttribute(GestioneCodiciDelegate.LISTA_CODICI, blocco1);
		return Navigation.getInstance(request).goBack();
	}

	private boolean isChiaveDuplicata(ActionForm form) {

		GestioneCodiceForm currentForm = (GestioneCodiceForm) form;
		CodiceVO dettaglio = currentForm.getDettaglio();

		if (!dettaglio.isNuovo() )
			return false;

		Set<String> codiciKeySet = currentForm.getCodiciKeySet();
		CodiceConfigVO config = currentForm.getConfigCodici();
		switch (config.getTipoTabella()) {
		case DICT:
			return codiciKeySet.contains(dettaglio.getCdTabella());

		case CROSS:
			return codiciKeySet.contains(dettaglio.getFlag1().trim() + "-" + dettaglio.getFlag2().trim());
		}

		return false;
	}

}
