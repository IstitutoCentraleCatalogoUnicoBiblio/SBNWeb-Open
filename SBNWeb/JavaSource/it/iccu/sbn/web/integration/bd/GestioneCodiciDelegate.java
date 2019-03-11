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
package it.iccu.sbn.web.integration.bd;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiciPermessiType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class GestioneCodiciDelegate {

	public static final String CONFIG_CODICI = "config.tab.codici";
    public static final String LISTA_CODICI = "lista.tab.codici";
    public static final String DETTAGLIO_CODICE = "dettaglio.codice";
	public static final String DATA_LIMITE_VALIDITA = "31-12-9999";

	private final FactoryEJBDelegate factory;
	private final UserVO utente;
	private final HttpServletRequest request;

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

    public GestioneCodiciDelegate(FactoryEJBDelegate factory, UserVO utente,
			HttpServletRequest request) {
		this.factory = factory;
		this.utente = utente;
		this.request = request;

	}

	public DescrittoreBloccoVO cercaConfigTabelleCodici(int maxRighe) {

		ActionMessages messaggio = new ActionMessages();

		try {
			DescrittoreBloccoVO blocco1 = factory.getSistema()
					.cercaConfigTabelleCodici(utente.getTicket(),
							utente.getUserId(), maxRighe);

			if (blocco1.getLista() == null || blocco1.getLista().size() == 0) {
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"elenco.codici.info.null"));
				saveErrors(request, messaggio, null);
				return null;
			}

			return blocco1;
		}

		catch (Exception e) {
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"dettaglio.codici.errore"));
			saveErrors(request, messaggio, e);
			return null;
		}
	}

	public boolean salvaCodice(CodiceVO codice, boolean validate) {
		ActionMessages messaggio = new ActionMessages();
		try {
			boolean ok = factory.getSistema().salvaTabellaCodici(codice, validate);
			if (ok) {
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("dettaglio.codici.salva.ok"));
				saveErrors(request, messaggio, null);
			}
			return ok;

		} catch (ValidationException e) {
			messaggio.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(e.getErrorCode().getErrorMessage(), e.getLabels()));
			this.saveErrors(request, messaggio, e);
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"dettaglio.codici.errore"));
			saveErrors(request, messaggio, e);
			return false;
		}

	}

	public DescrittoreBloccoVO cercaTabellaCodici(String cdTabella, int maxRighe) {
		ActionMessages messaggio = new ActionMessages();
		try {
			DescrittoreBloccoVO blocco1 = factory
					.getSistema()
					.cercaTabellaCodici(utente.getTicket(), maxRighe, cdTabella);

			if (blocco1.isEmpty() ) {
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"elenco.codici.info.null"));
				saveErrors(request, messaggio, null);
				//return null;
			}
			return blocco1;

		} catch (Exception e) {
			e.printStackTrace();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"dettaglio.codici.errore"));
			saveErrors(request, messaggio, e);
			return null;
		}
	}

	public List<ComboCodDescVO> getListaCodici(CodiciType type) {
		try {
			List<TB_CODICI> codici = factory.getCodici().getCodici(type);
			CaricamentoCombo combo = new CaricamentoCombo();
			return combo.loadComboCodiciDesc(codici);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ComboCodDescVO>();
	}

	public boolean abilitaTabella(CodiceConfigVO config, CodiciPermessiType permessi) {
		ActionMessages messaggio = new ActionMessages();
		try {
			return factory.getSistema().abilitaTabella(utente.getTicket(), config, permessi);

		} catch (Exception e) {
			e.printStackTrace();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"dettaglio.codici.errore"));
			saveErrors(request, messaggio, e);
			return false;
		}

	}

}
