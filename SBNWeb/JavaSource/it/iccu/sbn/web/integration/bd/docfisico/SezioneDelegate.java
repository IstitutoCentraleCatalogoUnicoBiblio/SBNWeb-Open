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
package it.iccu.sbn.web.integration.bd.docfisico;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.Collocazione;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.FiltroCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class SezioneDelegate {

	static Logger log = Logger.getLogger(SezioneDelegate.class);

	private static final String DELEGATE_ATTR = "req." + SezioneDelegate.class.getSimpleName();

	private Utente utenteEjb;
	private final HttpServletRequest request;
	private UserVO utente;

	private Collocazione collocazione;

	public static final SezioneDelegate getInstance(HttpServletRequest request) {
		SezioneDelegate delegate = (SezioneDelegate) request.getAttribute(DELEGATE_ATTR);
		if (delegate == null) {
			delegate = new SezioneDelegate(request);
			request.setAttribute(DELEGATE_ATTR, delegate);
		}
		return delegate;
	}

	protected SezioneDelegate(HttpServletRequest request) {
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		this.utente = Navigation.getInstance(request).getUtente();
		try {
			this.collocazione = DomainEJBFactory.getInstance().getCollocazione();
		} catch (Exception e) {	}
	}

	public ActionForward getSIFListaSezioni(FiltroCollocazioneVO filtro, String attribute) {
		Navigation navi = Navigation.getInstance(request);
		NavigationElement currentElement = navi.getCache().getCurrentElement();
		ActionMapping mapping = currentElement.getMapping();
		try {
			request.setAttribute("chiamante", mapping.getPath());
			request.setAttribute("codBib", filtro.getCodBib());
			request.setAttribute("descrBib", navi.getUtente().getBiblioteca());

			SezioneCollocazioneVO sezione = collocazione.getSezioneDettaglio(filtro.getCodPolo(), filtro.getCodBib(),
					filtro.getCodSezione(), utente.getTicket());
			String codSezione = sezione.getCodSezione();
			boolean sezioneSpazio = !ValidazioneDati.isFilled(codSezione);
			if (sezioneSpazio)
				return mapping.findForward("sifListaSezioni");

			filtro.setCodSezione(codSezione);
			//ho trovato la sezione a spazio
			filtro.setSezioneSpazio(sezioneSpazio);

			return mapping.getInputForward();

		} catch (DataException e) {
			if (e.getErrorCode() != SbnErrorTypes.GDF_SEZIONE_SPAZIO_NON_DEFINITA)
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));
			if (!ValidazioneDati.isFilled(e.getMessage()))
				log.error("", e);
			return mapping.findForward("sifListaSezioni");

		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));
			if (!ValidazioneDati.isFilled(e.getMessage()))
				log.error("", e);
		}

		return mapping.getInputForward();
	}


}
