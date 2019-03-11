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
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiciPermessiType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.TabellaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.codici.DettaglioCodiceForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.GestioneCodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

public final class DettaglioCodiceAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(DettaglioCodiceAction.class);

	private static String[] BOTTONIERA = new String[] {
		"dettaglio.codici.button.nuovo", "dettaglio.codici.button.modifica",
		"dettaglio.codici.button.allinea", "dettaglio.codici.button.elimina",
		"dettaglio.codici.button.annulla" };

	private static String[] BOTTONIERA_ESAMINA = new String[] {
		"dettaglio.codici.button.annulla" };

	private GestioneCodiciDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new GestioneCodiciDelegate(FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.blocco", "blocco");
		map.put("dettaglio.codici.button.nuovo", "nuovo");
		map.put("dettaglio.codici.button.modifica", "modifica");
		map.put("dettaglio.codici.button.allinea", "riattiva");
		map.put("dettaglio.codici.button.annulla", "annulla");
		map.put("dettaglio.codici.button.elimina", "disattiva");
		return map;
	}

	class ordinaCodiceAsc implements Comparator<CodiceVO> {
		public final int compare(CodiceVO a, CodiceVO b) {

			boolean x = false;
			boolean y = false;
			CodiceVO gp = a;
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

	class ordinaCodiceDec implements Comparator<CodiceVO> {
		public final int compare(CodiceVO a, CodiceVO b) {

			boolean x = false;
			boolean y = false;
			CodiceVO gp = a;
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

	class ordinaTitoloAsc implements Comparator<CodiceVO> {
		public final int compare(CodiceVO a, CodiceVO b) {

			boolean x = false;
			boolean y = false;
			CodiceVO gp = a;
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

	class ordinaTitoloDec implements Comparator<CodiceVO> {
		public final int compare(CodiceVO a, CodiceVO b) {

			boolean x = false;
			boolean y = false;
			CodiceVO gp = a;
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

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;
		if (!currentForm.isInitialized()) {
			log.debug("DettaglioCodiceAction:unspecified");
			CodiceConfigVO configCodici = (CodiceConfigVO) request.getAttribute(GestioneCodiciDelegate.CONFIG_CODICI);
			currentForm.setConfigCodici(configCodici);

			// verifico abilitazione utente
			boolean auth = this.checkAttivita(request, currentForm, "GESTIONE_TABELLA_CODICI");
			if (auth)
				currentForm.setPulsanti(BOTTONIERA);
			else
				currentForm.setPulsanti(BOTTONIERA_ESAMINA);

			currentForm.setDettaglioOrdinamento("codiceAsc");
			currentForm.setInitialized(true);
		}

		String cmd = request.getParameter("cmd");

		if (cmd != null && cmd.equals("codice")) {
			String ordinamento = currentForm.getDettaglioOrdinamento();
			if (ordinamento == null)
				ordinamento = "codiceAsc";
			List<CodiceVO> codici = currentForm.getDettaglioElencoCodici();
			if (ordinamento != null && ordinamento.equals("codiceAsc")) {
				Collections.sort(codici, new ordinaCodiceDec());
				currentForm.setDettaglioOrdinamento("codiceDec");
				return mapping.getInputForward();
			} else {
				Collections.sort(codici, new ordinaCodiceAsc());
				currentForm.setDettaglioOrdinamento("codiceAsc");
				return mapping.getInputForward();
			}
		}

		if (cmd != null && cmd.equals("titolo")) {
			String ordinamento = currentForm.getDettaglioOrdinamento();
			if (ordinamento == null)
				ordinamento = "titoloAsc";
			List<CodiceVO> codici = currentForm.getDettaglioElencoCodici();
			if (ordinamento != null && ordinamento.equals("titoloAsc")) {
				Collections.sort(codici, new ordinaTitoloDec());
				currentForm.setDettaglioOrdinamento("titoloDec");
				return mapping.getInputForward();
			} else {
				Collections.sort(codici, new ordinaTitoloAsc());
				currentForm.setDettaglioOrdinamento("titoloAsc");
				return mapping.getInputForward();
			}
		}

		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) request.getAttribute(GestioneCodiciDelegate.LISTA_CODICI);
		if (blocco1 == null)
			return mapping.getInputForward();

		currentForm.setDettaglioAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
		// memorizzo le informazioni per la gestione blocchi
		currentForm.setDettaglioIdLista(blocco1.getIdLista());
		currentForm.setDettaglioTotRighe(blocco1.getTotRighe());
		currentForm.setDettaglioTotBlocchi(blocco1.getTotBlocchi());
		currentForm.setDettaglioBloccoCorrente(blocco1.getNumBlocco());

		currentForm.setDettaglioElencoCodici(blocco1.getLista());

		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioCodiceForm myForm = (DettaglioCodiceForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		int numBlocco = myForm.getDettaglioBloccoCorrente();
		String idLista = myForm.getDettaglioIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getSistema().nextBlocco(
					ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				myForm.getDettaglioElencoCodici().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					myForm.setDettaglioBloccoCorrente(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (myForm.getDettaglioElencoCodici().size() == bloccoVO
						.getTotRighe())
					myForm.setDettaglioAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;
		CodiceConfigVO config = currentForm.getConfigCodici();

		CodiceVO codice = new CodiceVO();
		codice.setNomeTabella(config.getCdTabella());
		codice.setNuovo(true);
		codice.setDataAttivazione("31-12-9999");
		if (config.getTipoTabella() == TabellaType.CROSS) {
			codice.setCdTabella(CodiceConfigVO.getDummyCode());
			codice.setDescrizione(CodiceConfigVO.getDummyCode());
		}

		request.setAttribute(GestioneCodiciDelegate.CONFIG_CODICI, config);
		request.setAttribute(GestioneCodiciDelegate.DETTAGLIO_CODICE, codice);
		request.setAttribute(GestioneCodiciDelegate.LISTA_CODICI, currentForm.getDettaglioElencoCodici());

		return Navigation.getInstance(request).goForward(mapping.findForward("gestione"));
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;
		CodiceVO codice = getSelected(form);
		if (codice == null) {
			LinkableTagUtils.addError(request, new ActionMessage("dettaglio.codici.modifica"));

			return mapping.getInputForward();
		}

		codice.setNuovo(false);
		request.setAttribute(GestioneCodiciDelegate.CONFIG_CODICI, currentForm.getConfigCodici());
		request.setAttribute(GestioneCodiciDelegate.DETTAGLIO_CODICE, codice.clone() );
		request.setAttribute(GestioneCodiciDelegate.LISTA_CODICI, currentForm.getDettaglioElencoCodici());

		return Navigation.getInstance(request).goForward(mapping.findForward("gestione"));
	}

	private CodiceVO getSelected(ActionForm form) {
		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;
		String selected = currentForm.getDettaglioSelezRadio();
		if (ValidazioneDati.strIsNull(selected) )
			return null;

		List<CodiceVO> elencoCodici = currentForm.getDettaglioElencoCodici();
		for (CodiceVO codice : elencoCodici) {
			if (codice.getCdTabella().equals(selected))
				return codice;
		}

		return null;
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward riattiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;

		CodiceVO codice = getSelected(currentForm);
		if (codice == null) {
			LinkableTagUtils.addError(request, new ActionMessage("dettaglio.codici.allinea"));

			return mapping.getInputForward();
		}

		codice = codice.copy();
		codice.setDataAttivazione(GestioneCodiciDelegate.DATA_LIMITE_VALIDITA);
		GestioneCodiciDelegate delegate = getDelegate(request);
		boolean ok = delegate.salvaCodice(codice, false);
		if (!ok)
			return mapping.getInputForward();

		// ricarico la lista aggiornata
		DescrittoreBloccoVO elencoCodici = delegate.cercaTabellaCodici(
				currentForm.getConfigCodici().getCdTabella(), currentForm
						.getDettaglioMaxRighe());
		if (elencoCodici == null)
			return mapping.getInputForward();

		currentForm.setDettaglioElencoCodici(elencoCodici.getLista());
		return mapping.getInputForward();
	}

	public ActionForward disattiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;

		CodiceVO codice = getSelected(currentForm);
		if (codice == null) {
			LinkableTagUtils.addError(request, new ActionMessage("dettaglio.codici.allinea"));

			return mapping.getInputForward();
		}

		SimpleDateFormat _data1 = new SimpleDateFormat("dd-MM-yyyy");
		codice = codice.copy();
		codice.setDataAttivazione(_data1.format(new Date()));
		GestioneCodiciDelegate delegate = getDelegate(request);
		boolean ok = delegate.salvaCodice(codice, false);
		if (!ok)
			return mapping.getInputForward();

		// ricarico la lista aggiornata
		DescrittoreBloccoVO elencoCodici = delegate.cercaTabellaCodici(
				currentForm.getConfigCodici().getCdTabella(), currentForm
						.getDettaglioMaxRighe());
		if (elencoCodici == null)
			return mapping.getInputForward();

		currentForm.setDettaglioElencoCodici(elencoCodici.getLista());
		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		DettaglioCodiceForm currentForm = (DettaglioCodiceForm) form;

		if (idCheck.equals("dettaglio.codici.button.modifica")) {

			CodiceConfigVO config = currentForm.getConfigCodici();
			return (config.getTipoTabella() != TabellaType.CROSS);
		}

		if (idCheck.equals("GESTIONE_TABELLA_CODICI")) {
			Boolean auth = currentForm.getAuthorized();
			if (auth == null) // chiamo l'ejb solo una volta
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

			currentForm.setAuthorized(auth);
			Navigation navi = Navigation.getInstance(request);
			CodiceConfigVO config = currentForm.getConfigCodici();
			UserVO utente = navi.getUtente();
			currentForm.setRoot(utente.isRoot());
			return (auth && (config.getPermessi() == CodiciPermessiType.WRITE || utente.isRoot()) );
		}
		return true;
	}

}
