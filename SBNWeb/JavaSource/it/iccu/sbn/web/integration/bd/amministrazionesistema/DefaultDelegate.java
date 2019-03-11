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
package it.iccu.sbn.web.integration.bd.amministrazionesistema;

import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.AreaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.GruppoVO;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

public class DefaultDelegate {

	private static final String PERIODICI_DELEGATE = "req.default.delegate";
	private FactoryEJBDelegate factory;

	public static final DefaultDelegate getInstance(HttpServletRequest request) throws Exception {
		DefaultDelegate delegate = (DefaultDelegate) request.getAttribute(PERIODICI_DELEGATE);
		if (delegate == null) {
			delegate = new DefaultDelegate();
			request.setAttribute(PERIODICI_DELEGATE, delegate);
		}
		return delegate;
	}

	private DefaultDelegate() throws RemoteException, NamingException, CreateException {
		factory = FactoryEJBDelegate.getInstance();
	}

	// Ottiene i Default proprietari dell'utente
	public List<GruppoVO> caricaDefaultUtente(HttpServletRequest request,
			int idUtente, String idArea, Map attivita, String idBiblioteca, String idPolo)
			throws Exception {
		List<GruppoVO> lista = null;
		lista = this.factory.getGestioneDefault().getDefaultUtente(idUtente, idArea, attivita, idBiblioteca, idPolo);
		return lista;
	}

	// Ottiene i tutti i Default da visualizzare per l'utente in sessione
	public Map<String, String> caricaTuttiDefaultUtente(HttpServletRequest request,
			String idBiblioteca, String idPolo, Map attivita)
			throws Exception {
		Map<String, String> mappa = new HashMap<String, String>();
		mappa = this.factory.getGestioneDefault().getTuttiDefaultUtente(idBiblioteca, idPolo, attivita);
		return mappa;
	}

	// Salva i default impostati dall'utente
	public boolean salvaDefaultUtente(HttpServletRequest request, int idUtente, List<GruppoVO> campi, String idBiblioteca, String idPolo) throws Exception {
		return this.factory.getGestioneDefault().setDefaultUtente(idUtente, campi, idBiblioteca, idPolo);
	}

	// Ottiene le aree di competenza dell'utente in sessione. Se la mappa è null, restituisce tutte le aree.
	public List<AreaVO> caricaAreeUtente(HttpServletRequest request, Map attivita) throws Exception {
		return this.factory.getGestioneDefault().getAreeUtente(attivita);
	}

}
