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
package it.iccu.sbn.web.actions.servizi.configurazione;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

public abstract class ConfigurazioneBaseAction extends ServiziBaseAction implements SbnAttivitaChecker {

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		Utente ejb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.ESAME))
				ejb.checkAttivita(CodiciAttivita.getIstance().SRV_CONFIGURAZIONE_ESAME);
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.GESTIONE))
				ejb.checkAttivita(CodiciAttivita.getIstance().SRV_CONFIGURAZIONE_GESTIONE);
		} catch (UtenteNotAuthorizedException e) {
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;
	}

}
