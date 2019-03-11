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
package it.iccu.sbn.web.actions.servizi.occupazioni;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.web.actionforms.servizi.occupazioni.ListaOccupazioniForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.servizi.util.FileConstants;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaOccupazioniAction extends ServiziBaseAction {
	private static Logger log = Logger.getLogger(ListaOccupazioniAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esaminaOne", "Ok");
		map.put("servizi.bottone.esamina", "esamina");
		//map.put("servizi.bottone.modifica", "Ok");
		map.put("servizi.bottone.si",       "Si");
		map.put("servizi.bottone.no",       "No");
		map.put("servizi.bottone.annulla",  "Annulla");
		map.put("servizi.bottone.nuovo",    "Nuovo");
		map.put("servizi.bottone.cancella", "Cancella");
		map.put("button.blocco",            "blocco");
		map.put("servizi.bottone.deselTutti", "deselTutti");
		map.put("servizi.bottone.selTutti",   "selTutti");

		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!eleoccup.isSessione()) {
				eleoccup.setSessione(true);
				eleoccup.setAnaOccup((RicercaOccupazioneVO) request.getAttribute(ServiziDelegate.PARAMETRI_RICERCA));
			}

			this.caricaListaOccupazioni(request, eleoccup);

			if (eleoccup.getTotRighe() == 0) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

			//if (eleoccup.getListaOccupazioni().size()==1) eleoccup.setCodSelOccupSing("0");

			if (eleoccup.getListaOccupazioni()!=null &&  eleoccup.getListaOccupazioni().size()>0)
			{

				if (eleoccup.getAnaOccup()!=null && eleoccup.getAnaOccup().getOrdinamento()!=null &&  eleoccup.getAnaOccup().getOrdinamento().equals("3"))
				{
					List <OccupazioneVO> risLista=this.ElencaPer(eleoccup.getListaOccupazioni(), eleoccup, "profOccup");
					eleoccup.setListaOccupazioni(risLista);
				}
			}
			// gestione automatismo check su unico elemento lista
			if (eleoccup.getListaOccupazioni()!=null && eleoccup.getListaOccupazioni().size()==1)
			{
				String  [] appoSelProva= new String [1];
				appoSelProva[0]=String.valueOf(((OccupazioneVO) eleoccup.getListaOccupazioni().get(0)).getIdOccupazioni());
				eleoccup.setCodSelOccup(appoSelProva);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}


	public ActionForward Ok(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!eleoccup.isSessione()) {
			eleoccup.setSessione(true);
		}

		if (eleoccup.getCodSelOccupSing() != null && eleoccup.getCodSelOccupSing().length() > 0) {
			resetToken(request);
			// carico il vo con i dati selezionati
			//eleoccup.setAnaOccup(this.passaSelezionato(eleoccup.getListaOccupazioni(), eleoccup.getCodSelOccupSing()));


			int valore = Integer.parseInt(eleoccup.getCodSelOccupSing());
			OccupazioneVO anagOccupazioniVO = null;
			//	(OccupazioneVO)((OccupazioneVO)eleoccup.getListaOccupazioni().get(valore)).clone();

			for (int t = 0; t < eleoccup.getListaOccupazioni().size(); t++)
			{
				OccupazioneVO  eleOcc=(OccupazioneVO) eleoccup.getListaOccupazioni().get(t);
				if (eleOcc.getIdOccupazioni()==valore)
				{
					anagOccupazioniVO = eleOcc;
				}
			}

			anagOccupazioniVO.setNewOccupazione(FileConstants.OLD);

			request.setAttribute(ServiziDelegate.DETTAGLIO_OCCUPAZIONE, anagOccupazioniVO.clone());
			return mapping.findForward("esamina");
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaOccupazioniForm currentForm = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelOccup() != null
				&& currentForm.getCodSelOccup().length > 0) {
			resetToken(request);
			// carico il vo con i dati selezioonati
			currentForm.setSelectedOccup(currentForm.getCodSelOccup() );
			//currentForm.setAnaOccup(this.passaSelezionato(currentForm.getListaOccupazioni(), currentForm.getCodSelOccup()[0]));
			//currentForm.getAnaOccup().setnsetNewSpecialita(OccupazioneVO.OLD);

			List<OccupazioneVO> arrOccSel = new ArrayList<OccupazioneVO>();
			for (int t = 0; t < currentForm.getSelectedOccup().length; t++)
			{
				OccupazioneVO  eleOcc=this.passaSelezionato(currentForm.getListaOccupazioni(),  currentForm.getSelectedOccup()[t]);
				arrOccSel.add((OccupazioneVO) eleOcc.clone());
			}

			request.setAttribute("OccSel", arrOccSel);
			//request.setAttribute("AutAna", currentForm.getAutAna());
			//request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI, currentForm.getAutRicerca());
			//request.setAttribute("VengoDa", "ListaAut");
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


	private OccupazioneVO passaSelezionato(List listaOcc,
			String codSelOcc) {
		for (int i = 0; i < listaOcc.size(); i++) {
			OccupazioneVO currentVo = (OccupazioneVO) listaOcc
					.get(i);
			if (currentVo.getIdOccupazioni() == Integer.valueOf(codSelOcc)) {
				return currentVo;
			}
		}
		return null;
	}


	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaOccupazioniForm currentForm = (ListaOccupazioniForm) form;
		try {
			currentForm.setCodSelOccup(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaOccupazioniForm currentForm = (ListaOccupazioniForm) form;

		int numAut=0;
		try {
			//currentForm.setCodSelAutSing("");
			numAut = currentForm.getListaOccupazioni().size();
			if (numAut>0) {
				String[] codAut = new String[numAut];
				int i=0;
				java.util.Iterator iterator=currentForm.getListaOccupazioni().iterator();
				while (iterator.hasNext()) {
					codAut[i] = String.valueOf(((OccupazioneVO)iterator.next()).getIdOccupazioni());
					i++;
				}
				currentForm.setCodSelOccup(codAut);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}



	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!eleoccup.isSessione()) {
			eleoccup.setSessione(true);
		}
		resetToken(request);

		return mapping.findForward("nuovo");
	}


	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!eleoccup.isSessione()) {
			eleoccup.setSessione(true);
		}
		resetToken(request);

		return mapping.findForward("annulla");
	}


	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!eleoccup.isSessione()) {
			eleoccup.setSessione(true);
		}

		if (eleoccup.getCodSelOccup() != null && eleoccup.getCodSelOccup().length > 0) {
			eleoccup.setConferma(true);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}


	public ActionForward Si(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		ActionMessages errors = new ActionMessages();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!eleoccup.isSessione()) {
				eleoccup.setSessione(true);
			}

			String[] listaId = eleoccup.getCodSelOccup();
			Integer[] listaIdInteri = new Integer[listaId.length];
			for (int i=0; i<listaId.length; i++) {
				listaIdInteri[i] = Integer.parseInt(listaId[i]);
				if(eleoccup.getListaOccupazioni()!=null  && eleoccup.getListaOccupazioni().size()>0 && eleoccup.getListaOccupazioni().get(0)!=null )
				{
					// inibizione cancellazione
					//Verifica esistenza utenti cui è assegnata l'autorizzazione che si vuole cancellare
					boolean ret = true;
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					ret = factory.getGestioneServizi().esistonoUtentiConOcc(Navigation.getInstance(request).getUserTicket(),((OccupazioneVO)eleoccup.getListaOccupazioni().get(0)).getCodPolo(), ((OccupazioneVO)eleoccup.getListaOccupazioni().get(0)).getCodBiblioteca(), Integer.parseInt(listaId[i]));
					if (ret) {
						//Impossibile cancellare autorizzazione perchè assegnata almeno a un utente
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.occupazioneLista.utilizzata"));
						this.saveErrors(request, errors);
						this.resetToken(request);
						eleoccup.setConferma(false);
						return mapping.getInputForward();
					}
					// fine inibizione
				}

			}
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.cancellaOccupazioni(listaIdInteri, Navigation.getInstance(request).getUtente().getFirmaUtente());

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
			this.saveErrors(request, errors);
			this.resetToken(request);
			eleoccup.setConferma(false);
			return mapping.findForward("cancella");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreCancellazione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!eleoccup.isSessione()) {
			eleoccup.setSessione(true);
		}
		eleoccup.setConferma(false);
		return mapping.getInputForward();
	}


	public ActionForward blocco(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaOccupazioniForm eleoccup = (ListaOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!eleoccup.isSessione()) {
			eleoccup.setSessione(true);
		}

		int numBlocco = eleoccup.getBloccoSelezionato();
		String idLista = eleoccup.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);

			if (bloccoVO != null && bloccoVO.getLista().size()>0) {
				int progressivoPrimoElementoBloccoCaricato=((OccupazioneVO)bloccoVO.getLista().get(0)).getProgressivo();
				List listaDaAggiornare=eleoccup.getListaOccupazioni();
				Iterator iterator = listaDaAggiornare.iterator();
				int posizioneInserimentoBlocco=1;
				while (iterator.hasNext()) {
					if (progressivoPrimoElementoBloccoCaricato < ((OccupazioneVO)iterator.next()).getProgressivo()) {
						break;
					}
					posizioneInserimentoBlocco++;
				}
				if (posizioneInserimentoBlocco>listaDaAggiornare.size())
					eleoccup.getListaOccupazioni().addAll(bloccoVO.getLista());
				else
					eleoccup.getListaOccupazioni().addAll(posizioneInserimentoBlocco-1, bloccoVO.getLista());

				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					eleoccup.setBloccoSelezionato(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (eleoccup.getListaOccupazioni().size() == bloccoVO.getTotRighe())
					eleoccup.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}


	public List<OccupazioneVO> ElencaPer(List<OccupazioneVO> lst,ListaOccupazioniForm eleoccup,String sortBy ) throws EJBException {
		Comparator comp=null;
		if (sortBy==null) {
			comp =new ProfesOccupComparator();
			}
			else if (sortBy.equals("profOccup")) {
			comp =new ProfesOccupComparator();
			}
		if (lst != null)
		{
			if (comp != null)
			{
				Collections.sort(lst, comp);
			}
		}
		return lst;
	}

	private static class ProfesOccupComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((OccupazioneVO) o1).getComboDescrizione().toLowerCase();  //
				String e2 = ((OccupazioneVO) o2).getComboDescrizione().toLowerCase();
				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		}
	}

	private void caricaListaOccupazioni(HttpServletRequest request, ListaOccupazioniForm form)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);

		DescrittoreBloccoVO blocco1 = delegate.caricaListaOccupazioni(form.getAnaOccup());

		if (blocco1!=null && blocco1.getTotRighe()>0) {
			//abilito i tasti per il blocco se necessario
			form.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			//memorizzo le informazioni per la gestione blocchi
			form.setIdLista(blocco1.getIdLista());
			form.setTotRighe(blocco1.getTotRighe());
			form.setTotBlocchi(blocco1.getTotBlocchi());
			form.setBloccoSelezionato(blocco1.getNumBlocco());
			form.setMaxRighe(blocco1.getMaxRighe());
			form.setListaOccupazioni(blocco1.getLista());
		} else {
			form.setListaOccupazioni(new ArrayList<MateriaVO>());
			form.setTotRighe(0);
			form.setTotBlocchi(0);
			form.setBloccoSelezionato(0);
			form.setMaxRighe(0);
			form.setAbilitaBlocchi(false);
		}
	}

}
