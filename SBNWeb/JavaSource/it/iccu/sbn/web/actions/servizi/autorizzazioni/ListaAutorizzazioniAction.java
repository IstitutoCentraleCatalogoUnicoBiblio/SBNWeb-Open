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
package it.iccu.sbn.web.actions.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.web.actionforms.servizi.autorizzazioni.ListaAutorizzazioniForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
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
import org.apache.struts.action.ActionMessages;

public class ListaAutorizzazioniAction extends AutorizzazioniBaseAction {

	private static Logger log = Logger.getLogger(ListaAutorizzazioniAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esaminaOne", "Ok");
		map.put("servizi.bottone.esamina", "Esamina");
		map.put("servizi.bottone.si", "Si");
		map.put("servizi.bottone.no", "No");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.nuovo", "Nuovo");
		map.put("servizi.bottone.cancella", "Cancella");
		map.put("button.blocco", "blocco");
		map.put("servizi.bottone.deselTutti", "deselTutti");
		map.put("servizi.bottone.selTutti",   "selTutti");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() )
		{
			if 	(currentForm.getSelectedAutorizzazioni()!= null && currentForm.getSelectedAutorizzazioni().length>0)
			{
				currentForm.setCodSelAut(currentForm.getSelectedAutorizzazioni());
			}
			return mapping.getInputForward();
		}
		log.info("unspecified");
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			currentForm.setAutRicerca( (RicercaAutorizzazioneVO) request
					.getAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI));
			DescrittoreBloccoVO blocco1 =  (DescrittoreBloccoVO) request.getAttribute(ServiziDelegate.LISTA_AUTORIZZAZIONI);
			//abilito i tasti per il blocco se necessario
			if (blocco1!=null)
			{
				currentForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
				//memorizzo le informazioni per la gestione blocchi
				currentForm.setIdLista(blocco1.getIdLista());
				currentForm.setTotRighe(blocco1.getTotRighe());
				currentForm.setTotBlocchi(blocco1.getTotBlocchi());
				currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
				currentForm.setListaAutorizzazioni(blocco1.getLista());
			}
			// gestione automatismo check su unico elemento lista
			if (currentForm.getListaAutorizzazioni()!=null &&  currentForm.getListaAutorizzazioni().size()==1)
			{
				String  [] appoSelProva= new String [1];
				appoSelProva[0]=String.valueOf(((AutorizzazioneVO) currentForm.getListaAutorizzazioni().get(0)).getIdAutorizzazione());
				currentForm.setCodSelAut(appoSelProva);
			}
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelAutSing() != null
				&& currentForm.getCodSelAutSing().length() > 0) {
			resetToken(request);
			// carico il vo con i dati selezioonati
			//currentForm.setSelectedAutorizzazioni(new String[0]);
			currentForm.setAutAna(this.passaSelezionato(currentForm
					.getListaAutorizzazioni(), currentForm.getCodSelAutSing()));
			currentForm.getAutAna().setNuovaAut(AutorizzazioneVO.OLD);
			request.setAttribute(ServiziDelegate.DETTAGLIO_AUTORIZZAZIONE, currentForm.getAutAna().clone());
			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI, currentForm.getAutRicerca());
			request.setAttribute("VengoDa", "ListaAut");
			return mapping.findForward("esamina");

		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}
	}

	public ActionForward Esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelAut() != null
				&& currentForm.getCodSelAut().length > 0) {
			resetToken(request);
			// carico il vo con i dati selezioonati
			currentForm.setSelectedAutorizzazioni(currentForm.getCodSelAut() );
			currentForm.setAutAna(this.passaSelezionato(currentForm
					.getListaAutorizzazioni(), currentForm.getCodSelAut()[0]));
			currentForm.getAutAna().setNuovaAut(AutorizzazioneVO.OLD);

			List arrAutorizSel=new ArrayList();
			for (int t = 0; t < currentForm.getSelectedAutorizzazioni().length; t++)
			{
				AutorizzazioneVO  eleAutoriz=this.passaSelezionato(currentForm
						.getListaAutorizzazioni(),  currentForm.getSelectedAutorizzazioni()[t]);
				arrAutorizSel.add(eleAutoriz.clone() );
			}

			request.setAttribute("AutSel", arrAutorizSel);
			//request.setAttribute("AutAna", currentForm.getAutAna());
			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI, currentForm.getAutRicerca());
			request.setAttribute("VengoDa", "ListaAut");
			return mapping.findForward("esamina");
			// }
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}
	}


	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				currentForm.getListaAutorizzazioni().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					 currentForm.setBloccoSelezionato(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (currentForm.getListaAutorizzazioni().size() == bloccoVO.getTotRighe())
					currentForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

	private AutorizzazioneVO passaSelezionato(List listaAut,
			String codSelAut) {
		for (int i = 0; i < listaAut.size(); i++) {
			AutorizzazioneVO currentForm = (AutorizzazioneVO) listaAut
					.get(i);
			if (currentForm.getIdAutorizzazione() == Integer.valueOf(codSelAut)) {
				return currentForm;
			}
		}
		return null;
	}

	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}

		/*this.currentForm.getAutAna().setCodBiblioteca(this.currentForm.getAutRicerca().getCodBib());
		this.currentForm.getAutAna().setCodAutorizzazione("");
		this.currentForm.getAutAna().setDesAutorizzazione("");
		this.currentForm.getAutAna().setNuovaAut(AnagAutorizzazioniVO.NEW);
		request.setAttribute("AutAna", this.currentForm.getAutAna());
		*/
		currentForm.getAutRicerca().setNuovaAut(AutorizzazioneVO.NEW);

		request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI, currentForm.getAutRicerca());

		//Aggiunto AR
		this.resetToken(request);

		return mapping.findForward("nuovo");
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		return mapping.findForward("annulla");
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelAut() != null
				&& currentForm.getCodSelAut().length > 0) {
			currentForm.setConferma(true);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
			saveToken(request);
			return mapping.getInputForward();
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			int stato = this.cancmultipla(currentForm.getListaAutorizzazioni(), currentForm.getCodSelAut(), request);

			ActionMessages errors = new ActionMessages();
			if (stato==0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
				this.saveErrors(request, errors);
				currentForm.setConferma(false);
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.autorizzazioni.utilizzate"));
				this.saveErrors(request, errors);
				currentForm.setConferma(false);
			}
			return mapping.findForward("annulla");
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi.autorizzazioni
	  * ListaAutorizzazioniAction.java
	  * cancmultipla
	  * int
	  * @param listaAut    lista di tutte le autorizzazioni
	  * @param codSelAut  array degli id_autorizzazione da cancellare
	 * @param request
	  * @return <ul>
	  * 		<li>0: tutte le autorizzazioni selezionate sono state cancellate
	  * 		<li>1: alcune autorizzazioni non sono state cancellate in quanto assegnate a qualche utente
	  * 		</ul>
	  * @throws Exception In caso di exception
	  *
	  *
	 */
	private int cancmultipla(List listaAut, String[] codSelAut, HttpServletRequest request)
			throws Exception {
		boolean ret = true;
		int stato=0;

		if (codSelAut.length == 0) {
			return stato;
		}
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		for (int y = 0; y < codSelAut.length; y++) {
			for (int i = 0; i < listaAut.size(); i++) {
				AutorizzazioneVO currentForm = (AutorizzazioneVO) listaAut.get(i);
				if (String.valueOf(currentForm.getIdAutorizzazione()).equals(codSelAut[y])) {
					ret = factory.getGestioneServizi().esistonoUtentiCon(Navigation.getInstance(request).getUserTicket(), currentForm.getCodPolo(),
							currentForm.getCodBiblioteca(), currentForm.getCodAutorizzazione());
					if (!ret) {
						//Se non esistono utenti con assegnata l'autorizzazione allora cancello
						ret = factory.getGestioneServizi().cancelAutorizzazione(Navigation.getInstance(request).getUserTicket(), currentForm);
					}
					else stato=1;
					/* if (!ret) return ret;
					break; */
				}
			}
		}
		return stato;
	}


	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;
		try {
			currentForm.setCodSelAut(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAutorizzazioniForm currentForm = (ListaAutorizzazioniForm) form;

		int numAut=0;
		try {
			//currentForm.setCodSelAutSing("");
			numAut = currentForm.getListaAutorizzazioni().size();
			if (numAut>0) {
				String[] codAut = new String[numAut];
				int i=0;
				java.util.Iterator iterator=currentForm.getListaAutorizzazioni().iterator();
				while (iterator.hasNext()) {
					codAut[i] = String.valueOf(((AutorizzazioneVO)iterator.next()).getIdAutorizzazione());
					i++;
				}
				currentForm.setCodSelAut(codAut);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}


}
