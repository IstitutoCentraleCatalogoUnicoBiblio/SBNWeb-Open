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
package it.iccu.sbn.web.integration.bd.statistiche;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.statistiche.AreaVO;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StatisticheDelegate {

	public static final String CONFIG_CODICI = "config.tab.codici";
    public static final String LISTA_CODICI = "lista.tab.codici";
    public static final String DETTAGLIO_CODICE = "dettaglio.codice";
	public static final String DATA_LIMITE_VALIDITA = "31-12-9999";

	private final FactoryEJBDelegate factory;
	private final UserVO utente;
	private final HttpServletRequest request;

    public StatisticheDelegate(FactoryEJBDelegate factory, UserVO utente,
			HttpServletRequest request) {
		this.factory = factory;
		this.utente = utente;
		this.request = request;
	}

	public List<AreaVO> caricaAreeUtente(HttpServletRequest request, Map attivita) throws Exception {
		return this.factory.getStatistiche().getAreeUtente(attivita);
	}

	public DescrittoreBloccoVO getListaStatistiche(String area, String ticket, int elemBlocco) {
		ActionMessages messaggio = new ActionMessages();
		try {
			DescrittoreBloccoVO blocco1 = factory.getStatistiche().getListaStatistiche(area, ticket, elemBlocco);
			if (blocco1.getLista() == null || blocco1.getLista().size() == 0) {
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("elenco.codici.info.null"));
				saveErrors(request, messaggio, null);
				return null;
			}
			return blocco1;
		}catch (Exception e) {
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("dettaglio.codici.errore"));
			saveErrors(request, messaggio, e);
			return null;
		}
	}

	public List<DettVarStatisticaVO> getDettVarStatistica(int idConfig, String ticket) throws RemoteException {
		return this.factory.getStatistiche().getDettVarStatistica(idConfig, ticket);
	}

	private void saveErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);

		if (errors == null || errors.isEmpty()) {
			request.removeAttribute("org.apache.struts.action.ERROR");
			return;
		} else {
			request.setAttribute("org.apache.struts.action.ERROR", errors);
			return;
		}
	}

}
