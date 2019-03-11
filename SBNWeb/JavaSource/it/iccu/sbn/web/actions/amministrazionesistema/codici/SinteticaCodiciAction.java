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

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiciPermessiType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.codici.SinteticaCodiciForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.GestioneCodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public final class SinteticaCodiciAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(SinteticaCodiciAction.class);

	private GestioneCodiciDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new GestioneCodiciDelegate(FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	private CodiceConfigVO getSelected(ActionForm form) {
		SinteticaCodiciForm currentForm = (SinteticaCodiciForm) form;
		String selected = currentForm.getSelezRadio();
		if (ValidazioneDati.strIsNull(selected) )
			return null;

		List<CodiceConfigVO> elencoCodici = currentForm.getElencoCodici();
		for (CodiceConfigVO codice : elencoCodici) {
			if (codice.getCdTabella().equals(selected))
				return codice;
		}

		return null;
	}

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.blocco", "blocco");
		map.put("elenco.codici.button.dettaglio", "dettaglio");
		map.put("elenco.codici.button.abilita", "abilita");
		return map;
	}

	class ordinaCodiceAsc implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getCdTabella() != null)
				sa = gp.getCdTabella();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getCdTabella() != null)
				sb = gp.getCdTabella();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sa).compareTo(sb)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaCodiceDec implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getCdTabella() != null)
				sa = gp.getCdTabella();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getCdTabella() != null)
				sb = gp.getCdTabella();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sb).compareTo(sa)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaTitoloAsc implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getDescrizione() != null)
				sa = gp.getDescrizione();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getDescrizione() != null)
				sb = gp.getDescrizione();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sa).compareTo(sb)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaTitoloDec implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getDescrizione() != null)
				sa = gp.getDescrizione();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getDescrizione() != null)
				sb = gp.getDescrizione();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sb).compareTo(sa)); // Ascending

		} // end compare

	} // end class StringComparator

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String cmd = request.getParameter("cmd");
		SinteticaCodiciForm currentForm = (SinteticaCodiciForm) form;

		log.debug("unspecified()");
		if (cmd != null && cmd.equals("codice")) {
			String ordinamento = currentForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "codiceAsc";
			List<CodiceConfigVO> codici = currentForm.getElencoCodici();
			if (ordinamento != null && ordinamento.equals("codiceAsc")) {
				Collections.sort(codici, new ordinaCodiceDec());
				currentForm.setOrdinamento("codiceDec");
				return mapping.getInputForward();
			} else {
				Collections.sort(codici, new ordinaCodiceAsc());
				currentForm.setOrdinamento("codiceAsc");
				return mapping.getInputForward();
			}
		}

		if (cmd != null && cmd.equals("titolo")) {
			String ordinamento = currentForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "titoloAsc";
			List<CodiceConfigVO> codici = currentForm.getElencoCodici();
			if (ordinamento != null && ordinamento.equals("titoloAsc")) {
				Collections.sort(codici, new ordinaTitoloDec());
				currentForm.setOrdinamento("titoloDec");
				return mapping.getInputForward();
			} else {
				Collections.sort(codici, new ordinaTitoloAsc());
				currentForm.setOrdinamento("titoloAsc");
				return mapping.getInputForward();
			}
		}

		String user = Navigation.getInstance(request).getUtente().getUserId();

		if (currentForm.getElencoCodici().size() == 0) {

			GestioneCodiciDelegate delegate = getDelegate(request);
			DescrittoreBloccoVO blocco1 = delegate.cercaConfigTabelleCodici(currentForm.getMaxRighe());
			if (blocco1 == null)
				return mapping.getInputForward();

			if (blocco1.getLista().size() == 1)
				currentForm.setSelezRadio(((CodiceConfigVO) blocco1.getLista().get(0))
						.getCdTabella()
						+ "");

			currentForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setBloccoCorrente(blocco1.getNumBlocco());
			currentForm.setOrdinamento("codiceAsc");

			currentForm.setElencoCodici(blocco1.getLista());
			if (user.toUpperCase().equals("ROOT"))
				currentForm.setAbilitaScrittura("TRUE");
			else
				currentForm.setAbilitaScrittura("FALSE");
		}

		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaCodiciForm currentForm = (SinteticaCodiciForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		int numBlocco = currentForm.getBloccoCorrente();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getSistema().nextBlocco(
					ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				currentForm.getElencoCodici().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					currentForm.setBloccoCorrente(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (currentForm.getElencoCodici().size() == bloccoVO.getTotRighe())
					currentForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward dettaglio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaCodiciForm currentForm = (SinteticaCodiciForm) form;
		CodiceConfigVO config = getSelected(currentForm);
		if (config == null) {
			LinkableTagUtils.addError(request, new ActionMessage("elenco.codici.info.nocodice"));

			return mapping.getInputForward();
		}

		DescrittoreBloccoVO blocco1 = getDelegate(request).cercaTabellaCodici(config.getCdTabella(), currentForm.getMaxRighe());
		if (blocco1 == null)
			return mapping.getInputForward();

		request.setAttribute(GestioneCodiciDelegate.CONFIG_CODICI, config);
		request.setAttribute(GestioneCodiciDelegate.LISTA_CODICI, blocco1);
		return Navigation.getInstance(request).goForward(mapping.findForward("dettaglio") );
	}

	public ActionForward abilita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaCodiciForm currentForm = (SinteticaCodiciForm) form;

		CodiceConfigVO selected = getSelected(currentForm);
		if (selected == null) {
			LinkableTagUtils.addError(request, new ActionMessage("elenco.codici.info.nocodice"));

			return mapping.getInputForward();
		}

		GestioneCodiciDelegate delegate = getDelegate(request);
		boolean ok = false;
		switch (selected.getPermessi()) {
		case READ:
			ok = delegate.abilitaTabella(selected, CodiciPermessiType.WRITE);
			break;
		case WRITE:
			ok = delegate.abilitaTabella(selected, CodiciPermessiType.READ);
			break;
		}

		if (!ok)
			return mapping.getInputForward();

		DescrittoreBloccoVO blocco1 = delegate.cercaConfigTabelleCodici(currentForm.getMaxRighe());
		if (blocco1 == null)
			return mapping.getInputForward();

		if (blocco1.getLista().size() == 1)
			currentForm.setSelezRadio(((CodiceConfigVO) blocco1.getLista().get(0))
					.getCdTabella()	+ "");

		currentForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
		// memorizzo le informazioni per la gestione blocchi
		currentForm.setIdLista(blocco1.getIdLista());
		currentForm.setTotRighe(blocco1.getTotRighe());
		currentForm.setTotBlocchi(blocco1.getTotBlocchi());
		currentForm.setBloccoCorrente(blocco1.getNumBlocco());
		currentForm.setElencoCodici(blocco1.getLista());

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		if (idCheck.equals("GESTIONE_TABELLA_CODICI")) {
			boolean auth;
			try {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().GESTIONE_CODICI_VALIDAZIONE);
				auth = true;
			} catch (UtenteNotAuthorizedException e) {
				auth = false;
			} catch (RemoteException e) {
				log.error("", e);
				return false;
			}

			Navigation navi = Navigation.getInstance(request);
			UserVO utente = navi.getUtente();
			return (auth && utente.isRoot());
		}

		return true;
	}

}
