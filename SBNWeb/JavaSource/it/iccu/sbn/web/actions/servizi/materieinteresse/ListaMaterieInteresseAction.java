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
package it.iccu.sbn.web.actions.servizi.materieinteresse;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.web.actionforms.servizi.materieinteresse.ListaMaterieInteresseForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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


public class ListaMaterieInteresseAction extends ServiziBaseAction {

	private static Logger log = Logger.getLogger(ListaMaterieInteresseAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.esaminaOne", "Ok");
		map.put("servizi.bottone.esamina", "Esamina");
		//map.put("servizi.bottone.esamina",  "Ok");
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
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!elemateria.isSessione()) {
				elemateria.setSessione(true);
				elemateria.setDatiRicerca((RicercaMateriaVO) request.getAttribute(ServiziDelegate.PARAMETRI_RICERCA));
			}

			this.caricaListaMaterie(request, elemateria);

			if (elemateria.getTotRighe() == 0) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

			//if (elemateria.getListaMaterie().size()==1) elemateria.setCodSelMateriaSing("0");
			// gestione automatismo check su unico elemento lista
			if (elemateria.getListaMaterie()!=null && elemateria.getListaMaterie().size()==1)
			{
				String  [] appoSelProva= new String [1];
				appoSelProva[0]=String.valueOf(elemateria.getListaMaterie().get(0).getIdMateria());
				elemateria.setCodSelMateria(appoSelProva);
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
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elemateria.isSessione()) {
			elemateria.setSessione(true);
		}
		if (elemateria.getCodSelMateriaSing() != null && elemateria.getCodSelMateriaSing().length() > 0) {
			resetToken(request);

			int valore = Integer.parseInt(elemateria.getCodSelMateriaSing());

			MateriaVO anaMateriaVO = null;
			//	(OccupazioneVO)((OccupazioneVO)eleoccup.getListaOccupazioni().get(valore)).clone();

			for (int t = 0; t < elemateria.getListaMaterie().size(); t++)
			{
				MateriaVO  eleMat=elemateria.getListaMaterie().get(t);
				if (eleMat.getIdMateria()==valore)
				{
					anaMateriaVO = eleMat;
				}
			}
			request.setAttribute(ServiziDelegate.DETTAGLIO_MATERIA, anaMateriaVO.clone());
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

	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elemateria.isSessione()) {
			elemateria.setSessione(true);
		}
		resetToken(request);

		return mapping.findForward("nuovo");
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elemateria.isSessione()) {
			elemateria.setSessione(true);
		}
		resetToken(request);

		return mapping.findForward("annulla");
	}

	public ActionForward Esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaMaterieInteresseForm currentForm = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelMateria() != null
				&& currentForm.getCodSelMateria().length > 0) {
			resetToken(request);
			// carico il vo con i dati selezioonati
			currentForm.setSelectedMatInt(currentForm.getCodSelMateria() );
			//currentForm.setAnaOccup(this.passaSelezionato(currentForm.getListaOccupazioni(), currentForm.getCodSelOccup()[0]));
			//currentForm.getAnaOccup().setnsetNewSpecialita(OccupazioneVO.OLD);

			List arrMatSel=new ArrayList();
			for (int t = 0; t < currentForm.getSelectedMatInt().length; t++)
			{
				MateriaVO  eleMat=this.passaSelezionato(currentForm
						.getListaMaterie(),  currentForm.getSelectedMatInt()[t]);
				arrMatSel.add(eleMat.clone());
			}

			request.setAttribute("MatSel", arrMatSel);
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


	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMaterieInteresseForm currentForm = (ListaMaterieInteresseForm) form;
		try {
			currentForm.setCodSelMateria(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMaterieInteresseForm currentForm = (ListaMaterieInteresseForm) form;

		int numMat=0;
		try {
			//currentForm.setCodSelAutSing("");
			numMat = currentForm.getListaMaterie().size();
			if (numMat>0) {
				String[] codMat = new String[numMat];
				int i=0;
				java.util.Iterator iterator=currentForm.getListaMaterie().iterator();
				while (iterator.hasNext()) {
					codMat[i] = String.valueOf(((MateriaVO)iterator.next()).getIdMateria());
					i++;
				}
				currentForm.setCodSelMateria(codMat);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}

	private MateriaVO passaSelezionato(List listaMat,
			String codSelMat) {
		for (int i = 0; i < listaMat.size(); i++) {
			MateriaVO currentVo = (MateriaVO) listaMat
					.get(i);
			if (currentVo.getIdMateria() == Integer.valueOf(codSelMat)) {
				return currentVo;
			}
		}
		return null;
	}


	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elemateria.isSessione()) {
			elemateria.setSessione(true);
		}

		if (elemateria.getCodSelMateria() != null && elemateria.getCodSelMateria().length > 0) {
			elemateria.setConferma(true);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMaterieInteresseForm currentForm = (ListaMaterieInteresseForm) form;
		ActionMessages errors = new ActionMessages();
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);


			String[] listaId = currentForm.getCodSelMateria();
			Integer[] ids = new Integer[listaId.length];
			for (int i=0; i<listaId.length; i++) {
				ids[i] = Integer.parseInt(listaId[i]);
			}

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.cancellaMaterie(ids, Navigation.getInstance(request).getUtente().getFirmaUtente());

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
			this.saveErrors(request, errors);

			return mapping.findForward("cancella");

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceErroreCancellazione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} finally {
			this.resetToken(request);
			currentForm.setConferma(false);
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elemateria.isSessione()) {
			elemateria.setSessione(true);
		}
		elemateria.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaMaterieInteresseForm elemateria = (ListaMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!elemateria.isSessione()) {
			elemateria.setSessione(true);
		}

		int numBlocco = elemateria.getBloccoSelezionato();
		String idLista = elemateria.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null && bloccoVO.getLista().size()>0) {
				int progressivoPrimoElementoBloccoCaricato=((MateriaVO)bloccoVO.getLista().get(0)).getProgressivo();

				List listaDaAggiornare=elemateria.getListaMaterie();
				Iterator iterator = listaDaAggiornare.iterator();
				int posizioneInserimentoBlocco=1;
				while (iterator.hasNext()) {
					if (progressivoPrimoElementoBloccoCaricato < ((MateriaVO)iterator.next()).getProgressivo()) {
						break;
					}
					posizioneInserimentoBlocco++;
				}
				if (posizioneInserimentoBlocco>listaDaAggiornare.size())
					elemateria.getListaMaterie().addAll(bloccoVO.getLista());
				else
					elemateria.getListaMaterie().addAll(posizioneInserimentoBlocco-1, bloccoVO.getLista());

				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					elemateria.setBloccoSelezionato(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (elemateria.getListaMaterie().size() == bloccoVO.getTotRighe())
					elemateria.setAbilitaBlocchi(false);
			}
		}

		return mapping.getInputForward();
	}


	private void caricaListaMaterie(HttpServletRequest request, ListaMaterieInteresseForm elemateria)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		DescrittoreBloccoVO blocco1 = delegate.caricaListaMaterie(request, navi.getUserTicket(), elemateria.getDatiRicerca());

		if (blocco1!=null && blocco1.getTotRighe()>0) {
			//abilito i tasti per il blocco se necessario
			elemateria.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			//memorizzo le informazioni per la gestione blocchi
			elemateria.setIdLista(blocco1.getIdLista());
			elemateria.setTotRighe(blocco1.getTotRighe());
			elemateria.setTotBlocchi(blocco1.getTotBlocchi());
			elemateria.setBloccoSelezionato(blocco1.getNumBlocco());
			elemateria.setMaxRighe(blocco1.getMaxRighe());
			elemateria.setListaMaterie(blocco1.getLista());
		} else {
			elemateria.setListaMaterie(new ArrayList<MateriaVO>());
			elemateria.setTotRighe(0);
			elemateria.setTotBlocchi(0);
			elemateria.setBloccoSelezionato(0);
			elemateria.setMaxRighe(0);
			elemateria.setAbilitaBlocchi(false);
		}
	}
}
