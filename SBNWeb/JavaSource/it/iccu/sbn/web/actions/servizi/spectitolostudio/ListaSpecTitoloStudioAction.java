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
package it.iccu.sbn.web.actions.servizi.spectitolostudio;


import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.web.actionforms.servizi.spectitolostudio.ListaSpecTitoloStudioForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.servizi.util.FileConstants;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class ListaSpecTitoloStudioAction extends ServiziBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esaminaOne", "Ok");
//		map.put("servizi.bottone.modifica", "Ok");
		map.put("servizi.bottone.si",       "Si");
		map.put("servizi.bottone.no",       "No");
		map.put("servizi.bottone.annulla",  "Annulla");
		map.put("servizi.bottone.nuovo",    "Nuovo");
		map.put("servizi.bottone.cancella", "Cancella");
		map.put("servizi.bottone.deselTutti", "deselTutti");
		map.put("servizi.bottone.selTutti",   "selTutti");
		map.put("button.blocco",            "blocco");
		map.put("servizi.bottone.esamina", "Esamina");
		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!elespecialita.isSessione()) {
				elespecialita.setSessione(true);
				elespecialita.setDatiRicerca((RicercaTitoloStudioVO) request.getAttribute(ServiziDelegate.PARAMETRI_RICERCA));
			}

			this.caricaListaSpecTitoliStudio(request, elespecialita);

			if (elespecialita.getTotRighe() == 0) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

			//if (elespecialita.getListaSpecialita().size()==1) elespecialita.setCodSelSpecialitaSing("0");
			// gestione automatismo check su unico elemento lista
			if (elespecialita.getListaSpecialita()!=null &&  elespecialita.getListaSpecialita().size()==1)
			{
				String  [] appoSelProva= new String [1];
				appoSelProva[0]=String.valueOf(((SpecTitoloStudioVO) elespecialita.getListaSpecialita().get(0)).getIdTitoloStudio());
				elespecialita.setCodSelSpecialita(appoSelProva);
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
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elespecialita.isSessione()) {
			elespecialita.setSessione(true);
		}

		if (elespecialita.getCodSelSpecialitaSing() != null && elespecialita.getCodSelSpecialitaSing().length() > 0) {
			resetToken(request);

			// carico il vo con i dati selezioonati
			elespecialita.setAnaSpecialita(this.passaSelezionato(elespecialita
					.getListaSpecialita(), elespecialita.getCodSelSpecialitaSing()));


			elespecialita.getAnaSpecialita().setNewSpecialita(FileConstants.OLD);

			//int valore = Integer.parseInt(elespecialita.getCodSelSpecialitaSing())-1;
			//elespecialita.setAnaSpecialita((SpecTitoloStudioVO)((SpecTitoloStudioVO)elespecialita.getListaSpecialita().get(valore)).clone());
			request.setAttribute(ServiziDelegate.DETTAGLIO_SPEC_TITOLO_STUDIO, elespecialita.getAnaSpecialita().clone() );

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



	public ActionForward Esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm currentForm = (ListaSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelSpecialita() != null
				&& currentForm.getCodSelSpecialita().length > 0) {
			resetToken(request);
			// carico il vo con i dati selezioonati
			currentForm.setSelectedSpecialita(currentForm.getCodSelSpecialita() );
			currentForm.setAnaSpecialita(this.passaSelezionato(currentForm
					.getListaSpecialita(), currentForm.getCodSelSpecialita()[0]));
			currentForm.getAnaSpecialita().setNewSpecialita(SpecTitoloStudioVO.OLD);

			List arrSpecSel=new ArrayList();
			for (int t = 0; t < currentForm.getSelectedSpecialita().length; t++)
			{
				SpecTitoloStudioVO  eleSpec=this.passaSelezionato(currentForm
						.getListaSpecialita(),  currentForm.getSelectedSpecialita()[t]);
				arrSpecSel.add(eleSpec.clone());
			}

			request.setAttribute("SpecSel", arrSpecSel);
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

	private SpecTitoloStudioVO passaSelezionato(List listaSpec,
			String codSelSpecialita) {
		for (int i = 0; i < listaSpec.size(); i++) {
			SpecTitoloStudioVO currentVo = (SpecTitoloStudioVO) listaSpec
					.get(i);
			if (currentVo.getIdTitoloStudio() == Integer.valueOf(codSelSpecialita)) {
				return currentVo;
			}
		}
		return null;
	}

	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elespecialita.isSessione()) {
			elespecialita.setSessione(true);
		}
		resetToken(request);

		return mapping.findForward("nuovo");
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elespecialita.isSessione()) {
			elespecialita.setSessione(true);
		}
		resetToken(request);
		elespecialita.setConferma(false);

		return mapping.findForward("annulla");
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elespecialita.isSessione()) {
			elespecialita.setSessione(true);
		}

		ActionMessages errors = new ActionMessages();
		if (elespecialita.getCodSelSpecialita() != null && elespecialita.getCodSelSpecialita().length > 0) {
			elespecialita.setConferma(true);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			elespecialita.setConferma(true);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			saveToken(request);
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		ActionMessages errors = new ActionMessages();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!elespecialita.isSessione()) {
				elespecialita.setSessione(true);
			}

			String[] listaId = elespecialita.getCodSelSpecialita();
			Integer[] listaIdInteri = new Integer[listaId.length];

			for (int i=0; i<listaId.length; i++) {
				listaIdInteri[i] = Integer.parseInt(listaId[i]);
				if(elespecialita.getListaSpecialita()!=null  && elespecialita.getListaSpecialita().size()>0 && elespecialita.getListaSpecialita().get(0)!=null )
				{
					// inibizione cancellazione
					//Verifica esistenza utenti cui è assegnata l'autorizzazione che si vuole cancellare
					boolean ret = true;
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					ret = factory.getGestioneServizi().esistonoUtentiConSpecTit(Navigation.getInstance(request).getUserTicket(),((SpecTitoloStudioVO)elespecialita.getListaSpecialita().get(0)).getCodPolo(), ((SpecTitoloStudioVO)elespecialita.getListaSpecialita().get(0)).getCodBiblioteca(), Integer.parseInt(listaId[i]));
					if (ret) {
						//Impossibile cancellare autorizzazione perchè assegnata almeno a un utente
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.specTitoliLista.utilizzata"));
						this.saveErrors(request, errors);
						this.resetToken(request);
						elespecialita.setConferma(false);
						return mapping.getInputForward();
					}
					// fine inibizione
				}
			}

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.cancellaSpecTitoloStudio(listaIdInteri, Navigation.getInstance(request).getUtente().getFirmaUtente());

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
			this.saveErrors(request, errors);
			this.resetToken(request);
			elespecialita.setConferma(false);

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
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elespecialita.isSessione()) {
			elespecialita.setSessione(true);
		}
		elespecialita.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm elespecialita = (ListaSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elespecialita.isSessione()) {
			elespecialita.setSessione(true);
		}
		int numBlocco = elespecialita.getBloccoSelezionato();
		String idLista = elespecialita.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);

			if (bloccoVO != null  && bloccoVO.getLista().size()>0) {
				int progressivoPrimoElementoBloccoCaricato=((SpecTitoloStudioVO)bloccoVO.getLista().get(0)).getProgressivo();

				List listaDaAggiornare=elespecialita.getListaSpecialita();
				Iterator iterator = listaDaAggiornare.iterator();
				int posizioneInserimentoBlocco=1;
				while (iterator.hasNext()) {
					if (progressivoPrimoElementoBloccoCaricato < ((SpecTitoloStudioVO)iterator.next()).getProgressivo()) {
						break;
					}
					posizioneInserimentoBlocco++;
				}
				if (posizioneInserimentoBlocco>listaDaAggiornare.size())
					elespecialita.getListaSpecialita().addAll(bloccoVO.getLista());
				else
					elespecialita.getListaSpecialita().addAll(posizioneInserimentoBlocco-1, bloccoVO.getLista());

				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					elespecialita.setBloccoSelezionato(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (elespecialita.getListaSpecialita().size() == bloccoVO.getTotRighe())
					elespecialita.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}


	private void caricaListaSpecTitoliStudio(HttpServletRequest request, ListaSpecTitoloStudioForm elespecialita)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		DescrittoreBloccoVO blocco1 = delegate.caricaListaSpecialita(elespecialita.getDatiRicerca());

		if (blocco1!=null && blocco1.getTotRighe()>0) {
			//abilito i tasti per il blocco se necessario
			elespecialita.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			//memorizzo le informazioni per la gestione blocchi
			elespecialita.setIdLista(blocco1.getIdLista());
			elespecialita.setTotRighe(blocco1.getTotRighe());
			elespecialita.setTotBlocchi(blocco1.getTotBlocchi());
			elespecialita.setBloccoSelezionato(blocco1.getNumBlocco());
			elespecialita.setListaSpecialita(blocco1.getLista());
			elespecialita.setConferma(false);
		} else {
			elespecialita.setListaSpecialita(new ArrayList<MateriaVO>());
			elespecialita.setTotRighe(0);
			elespecialita.setTotBlocchi(0);
			elespecialita.setBloccoSelezionato(0);
			elespecialita.setMaxRighe(0);
			elespecialita.setAbilitaBlocchi(false);
		}
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm currentForm = (ListaSpecTitoloStudioForm) form;
		try {
			currentForm.setCodSelSpecialita(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSpecTitoloStudioForm currentForm = (ListaSpecTitoloStudioForm) form;

		int numAut=0;
		try {
			//currentForm.setCodSelAutSing("");
			numAut = currentForm.getListaSpecialita().size();
			if (numAut>0) {
				String[] codAut = new String[numAut];
				int i=0;
				java.util.Iterator iterator=currentForm.getListaSpecialita().iterator();
				while (iterator.hasNext()) {
					codAut[i] = String.valueOf(((SpecTitoloStudioVO)iterator.next()).getIdTitoloStudio());
					i++;
				}
				currentForm.setCodSelSpecialita(codAut);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}

}
