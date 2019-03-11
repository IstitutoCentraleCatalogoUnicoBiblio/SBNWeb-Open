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
package it.iccu.sbn.web.actions.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.amministrazione.BatchAttivazioneVO;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.StatoElaborazioniDifferiteForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.BatchDelegate;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;


public class StatoElaborazioniDifferiteAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.cerca","cerca");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StatoElaborazioniDifferiteForm currentForm = (StatoElaborazioniDifferiteForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
		        return mapping.getInputForward();

			if(!currentForm.isSessione()) {
				currentForm.getRicerca().setOrdinamento("3"); // ord. per progressivo desc
				currentForm.setSessione(true);
				navi.addBookmark(BatchDelegate.RICERCA_BATCH_BOOKMARK);
			}

			UtenteVO bibliotecario = (UtenteVO) request.getAttribute("bibliotecario");
			if (bibliotecario != null) {
				currentForm.getRicerca().setRichiedente(bibliotecario.getUsername());
				return mapping.getInputForward();
			}

			if (request.getSession().getAttribute(Constants.CURRENT_MENU)
					.equals("menu.elaborazionidifferite.statoRichieste")
					&& currentForm.getCodiceBibl() == null
					&& navi.getActionCaller() == null)
				// si proviene dal menu
				// pulizia integrale delle variabili di sessione
				Pulisci.PulisciVar(request);

			String biblio = navi.getUtente().getCodBib();
			currentForm.setCodiceBibl(biblio);

			this.loadDataEsecuzioneProgrammata(currentForm);
			this.loadVisibilita(currentForm);
			this.loadProcedure(currentForm);
			this.loadRichiedente(navi.getUtente().getUserId(), currentForm);
			this.loadOrdinamento(currentForm);
			this.loadStato(currentForm);

			return mapping.getInputForward();

		}	catch (ValidationException ve) {
			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.elabdiff.erroreNonGestito"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ActionMessages errors = new ActionMessages();
		StatoElaborazioniDifferiteForm currentForm = (StatoElaborazioniDifferiteForm) form;
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			RichiestaElaborazioniDifferiteVO richiesta = currentForm.getRicerca();
			richiesta.validate();
			BatchDelegate delegate = BatchDelegate.getDelegate(request);
			DescrittoreBloccoVO blocco1 = delegate.cercaRichieste(richiesta);
			if (blocco1 == null)
				return mapping.getInputForward();

			request.setAttribute(BatchDelegate.PARAMETRI_RICERCA, richiesta);
			request.setAttribute(BatchDelegate.SINTETICA_RICHIESTE, blocco1);

			return navi.goForward(mapping.findForward("sinteticaElaborazioniDifferite"));

		} catch (ValidationException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	private void loadDataEsecuzioneProgrammata(StatoElaborazioniDifferiteForm statoElaborazioniDifferiteForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<CodaJMSVO> listaCode = factory.getElaborazioniDifferite().getListaCodeBatch();
		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		lista.add(new StrutturaCombo(" ", ""));
		for (CodaJMSVO coda : listaCode ) {
			StrutturaCombo elem = new StrutturaCombo(coda.getId_coda() + "", coda.getId_descrizione() );
			lista.add(elem);
		}

		statoElaborazioniDifferiteForm.setListaDataEsecuzioneProgrammata(lista);
	}

	private void loadStato(StatoElaborazioniDifferiteForm statoElaborazioniDifferiteForm) throws Exception {

		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		StrutturaCombo elem = new StrutturaCombo(" ", " ");
		lista.add(elem);
		elem = new StrutturaCombo(ConstantsJMS.STATO_OK, ConstantsJMS.STATO_OK);
		lista.add(elem);
		elem = new StrutturaCombo(ConstantsJMS.STATO_ERROR, ConstantsJMS.STATO_ERROR);
		lista.add(elem);
		elem = new StrutturaCombo(ConstantsJMS.STATO_SEND, ConstantsJMS.STATO_SEND);
		lista.add(elem);
		elem = new StrutturaCombo(ConstantsJMS.STATO_EXEC, ConstantsJMS.STATO_EXEC);
		lista.add(elem);
		statoElaborazioniDifferiteForm.setListaStato(lista);
	}

	private void loadOrdinamento(StatoElaborazioniDifferiteForm statoElaborazioniDifferiteForm) throws Exception {

		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<TB_CODICI> codici = factory.getCodici().getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_BATCH);
		for (TB_CODICI cod : codici) {
			StrutturaCombo elem = new StrutturaCombo(cod.getCd_tabella().trim(), cod.getDs_tabella().trim() );
			lista.add(elem);
		}
		lista = CaricamentoCombo.cutFirst(lista);
		statoElaborazioniDifferiteForm.setListaTipiOrdinamento(lista);
	}

	private void loadVisibilita(StatoElaborazioniDifferiteForm statoElaborazioniDifferiteForm) throws Exception {

		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		StrutturaCombo elem = new StrutturaCombo("P", "Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("B", "Biblioteca");
		lista.add(elem);
		statoElaborazioniDifferiteForm.setListaVisibilita(lista);
	}



	private void loadRichiedente(String utente, StatoElaborazioniDifferiteForm statoElaborazioniDifferiteForm) throws Exception {

/*		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		DescrittoreBloccoVO blocco1 = factory.getSistema().cercaUtenti(ticket, "*", "*", "*", biblioteca, "", "SI'", "cognome", 100);

		List<UtenteVO> utenti = blocco1.getLista();
		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		lista.add(new StrutturaCombo(" ", " ") );

		for (UtenteVO utente : utenti)
		{
			if (!utente.getAbilitato().equals("NO"))
			{
				StrutturaCombo elem = new StrutturaCombo(utente.getUsername(), utente.getCognome());
				lista.add(elem);
			}
		}

		statoElaborazioniDifferiteForm.setListaRichiedente(lista);
*/

		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		StrutturaCombo elem = new StrutturaCombo(" ", "Tutti");
		lista.add(elem);
		elem = new StrutturaCombo(utente, "Solo utente corrente");
		lista.add(elem);
		statoElaborazioniDifferiteForm.setListaRichiedente(lista);
	}

	private void loadProcedure(StatoElaborazioniDifferiteForm statoElaborazioniDifferiteForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<BatchAttivazioneVO> listaBacth = factory.getElaborazioniDifferite().getBatchAttivabili();
		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();

		lista.add(new StrutturaCombo(" ", " ") );

		for (BatchAttivazioneVO batch : listaBacth) {
			StrutturaCombo elem = new StrutturaCombo(batch.getCod_attivita(), batch.getDescrizione() );
			lista.add(elem);
		}
		statoElaborazioniDifferiteForm.setListaProcedure(lista);
	}

}

