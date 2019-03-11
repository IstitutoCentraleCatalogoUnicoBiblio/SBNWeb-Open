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
package it.iccu.sbn.web.integration.bd.gestioneservizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.calendario.Calendario;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CalendarioDelegate {

	static Logger log = Logger.getLogger(CalendarioDelegate.class);

	private static final String DELEGATE_ATTR = "req." + CalendarioDelegate.class.getSimpleName();

	private Utente utenteEjb;
	private final HttpServletRequest request;
	private UserVO utente;

	private Calendario calendario;

	public static final CalendarioDelegate getInstance(HttpServletRequest request) {
		CalendarioDelegate delegate = (CalendarioDelegate) request.getAttribute(DELEGATE_ATTR);
		if (delegate == null) {
			delegate = new CalendarioDelegate(request);
			request.setAttribute(DELEGATE_ATTR, delegate);
		}
		return delegate;
	}

	protected CalendarioDelegate(HttpServletRequest request) {
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		this.utente = Navigation.getInstance(request).getUtente();
		try {
			this.calendario = DomainEJBFactory.getInstance().getCalendario();
		} catch (Exception e) {	}
	}

	public ModelloCalendarioVO getCalendarioBiblioteca(String codPolo, String codBib) throws Exception {
		return calendario.getCalendarioBiblioteca(utente.getTicket(), codPolo, codBib);
	}

	public ModelloCalendarioVO aggiornaModelloCalendario(ModelloCalendarioVO modello) throws Exception {
		return calendario.aggiornaModelloCalendario(utente.getTicket(), modello);
	}

	public CalendarioVO cancellaModelloCalendario(ModelloCalendarioVO modello) throws Exception {
		return calendario.cancellaModelloCalendario(utente.getTicket(), modello);
	}

	public List<GiornoVO> getGrigliaCalendario(RicercaGrigliaCalendarioVO ricerca) throws Exception {
		return calendario.getGrigliaCalendario(utente.getTicket(), ricerca);
	}

}
