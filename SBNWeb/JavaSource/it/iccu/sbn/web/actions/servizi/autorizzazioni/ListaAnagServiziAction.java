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

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.web.actionforms.servizi.autorizzazioni.ListaAnagServiziForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

public class ListaAnagServiziAction extends AutorizzazioniBaseAction {
	private static Logger log = Logger.getLogger(ListaAnagServiziAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.scegli", "Ok");
		map.put("servizi.bottone.annullaOperazione", "annulla");
		map.put("servizi.bottone.blocco",  "blocco");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (!currentForm.isSessione()) {
				log.debug("unspecified()");
				currentForm.setSessione(true);
				currentForm.setCodBib((String) request.getAttribute("CodiceBiblioteca"));

				AutorizzazioneVO autAna = (AutorizzazioneVO) request.getAttribute(ServiziDelegate.DETTAGLIO_AUTORIZZAZIONE);
				currentForm.setCodPolo(autAna.getCodPolo());

				// carico la lista dei servizi già associati
				currentForm.setServAssociati((List) request.getAttribute("ServAssociati"));
				DescrittoreBloccoVO blocco1 = ((DescrittoreBloccoVO) request.getAttribute("BlocoAnagraficaServizi"));

				currentForm.setServDaAssociare(blocco1.getLista());
				// abilito i tasti per il blocco se necessario
				currentForm.setAbilitaBlocchi(blocco1.getTotBlocchi() > 1);
				// memorizzo le informazioni per la gestione blocchi
				currentForm.setIdLista(blocco1.getIdLista());
				currentForm.setTotRighe(blocco1.getTotRighe());
				currentForm.setTotBlocchi(blocco1.getTotBlocchi());
				currentForm.setBloccoSelezionato(blocco1.getNumBlocco() + 1);
			}

			if (currentForm.getServDaAssociare()!=null && currentForm.getServDaAssociare().size() > 0)
			{
				for (int index = 0; index <currentForm.getServDaAssociare().size(); index++) {
					ElementoSinteticaServizioVO ele=(ElementoSinteticaServizioVO) currentForm.getServDaAssociare().get(index);
					ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getTipServizio()));
				}
			}
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			resetToken(request);

			if (!validaSelezione(form)) {
				//resetSelezione();
				soloSelezionati(form);
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(
					"errors.servizi.autorizzazione.tipoServizioDuplicato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//this.aggiornaListaServiziGiaAssegnati(this.currentForm.getServAssociati());
			this.aggiornaListaServizi(currentForm.getServDaAssociare(),
									  currentForm.getServAssociati());
			request.setAttribute("ServSelezionati", currentForm.getServAssociati());
			request.setAttribute("VengoDa", "ListaServizi");
			return mapping.findForward("ok");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private boolean validaSelezione(ActionForm form) {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		boolean ok=true;
		List<ElementoSinteticaServizioVO> serviziDaAssociare = currentForm.getServDaAssociare();
		HashSet<String> codiciTipiSelezionati=new HashSet<String>();
		ElementoSinteticaServizioVO servizio=null;
		Iterator<ElementoSinteticaServizioVO> iterator = serviziDaAssociare.iterator();
		while (iterator.hasNext()) {
			servizio=iterator.next();
			if (servizio.getCancella().equalsIgnoreCase("C")) {
				if (codiciTipiSelezionati.contains(servizio.getTipServizio())) {
					ok=false;
					break;
				}
				codiciTipiSelezionati.add(servizio.getTipServizio());
			}
		}
		return ok;
	}


	private void resetSelezione(ActionForm form) {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		List<ElementoSinteticaServizioVO> serviziDaAssociare = currentForm.getServDaAssociare();
		ElementoSinteticaServizioVO servizio=null;
		Iterator<ElementoSinteticaServizioVO> iterator = serviziDaAssociare.iterator();
		while (iterator.hasNext()) {
			servizio=iterator.next();
			servizio.setCancella("");
		}
	}

	private void soloSelezionati(ActionForm form) {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		List<ElementoSinteticaServizioVO> serviziSelezionati= new ArrayList<ElementoSinteticaServizioVO>();
		ElementoSinteticaServizioVO servizio=null;
		List<ElementoSinteticaServizioVO> serviziDaAssociare = currentForm.getServDaAssociare();
		Iterator<ElementoSinteticaServizioVO> iterator = serviziDaAssociare.iterator();
		while (iterator.hasNext()) {
			servizio = iterator.next();
			if (servizio.getCancella().equalsIgnoreCase("C"))
				serviziSelezionati.add(servizio);
		}
		currentForm.setServDaAssociare(serviziSelezionati);
	}


	private void aggiornaListaServizi(List servSel, List servGiaAss) {
		if (servSel == null && servSel.size() == 0)
			return;
		int index = 0;
		boolean inserisci = false;
		while (index < servSel.size()) {
			ElementoSinteticaServizioVO serBib = (ElementoSinteticaServizioVO) servSel.get(index);
			if (serBib.getCancella().equals("C")) {
				inserisci = true;
				for (int index2 = 0; index2 < servGiaAss.size(); index2++) {
					ElementoSinteticaServizioVO serAss = (ElementoSinteticaServizioVO) servGiaAss.get(index2);
					if ((serAss.getTipServizio().equals(serBib.getTipServizio())
							&&
						 serAss.getCodServizio().equals(serBib.getCodServizio()))) {
							if (serAss.getStato() == ElementoSinteticaServizioVO.DEL) {
								serAss.setStato(ElementoSinteticaServizioVO.OLD);
								serAss.setCancella("");
								inserisci = false;
								break;
							}
					}
				}
				if (inserisci) {
					serBib.setCancella("");
					serBib.setStato(ElementoSinteticaServizioVO.NEW);
					servGiaAss.add(serBib);
					inserisci = false;
				}
			}
			index++;
		}
	}

	private void aggiornaListaServiziGiaAssegnati(List servAss) {
		if (servAss == null || servAss.size() == 0)
			return;
		int index = 0;

		while (index < servAss.size()) {
			ElementoSinteticaServizioVO serBib = (ElementoSinteticaServizioVO) servAss.get(index);
			serBib.setStato(ElementoSinteticaServizioVO.OLD);

			index++;
		}
	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);
		request.setAttribute("ServSelezionati", currentForm
				.getServAssociati());
		request.setAttribute("VengoDa", "ListaServizi");
		return mapping.findForward("annulla");
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaAnagServiziForm currentForm = (ListaAnagServiziForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket,
					idLista, numBlocco);
			if (bloccoVO != null) {
				currentForm.getServDaAssociare()
						.addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					currentForm.setBloccoSelezionato(bloccoVO
							.getNumBlocco() + 1);
				// ho caricato tutte le righe sulla form
				if (currentForm.getServDaAssociare().size() == bloccoVO
						.getTotRighe())
					currentForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

	   private String cercaDescrServizio(String cod) throws Exception {
		   	String descr="";
		   	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		   	descr=factory.getCodici().cercaDescrizioneCodice(cod, CodiciType.CODICE_TIPO_SERVIZIO,CodiciRicercaType.RICERCA_CODICE_SBN );
			return 	descr;
	   }
}
