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
//	SBNWeb - Rifacimento ClientServer
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica;

import it.iccu.sbn.ejb.vo.gestionebibliografica.SifRicercaRepertoriVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.SifRicercaRepertoriForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class SifRicercaRepertoriAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(SifRicercaRepertoriAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sifRepertori.button.cerca", "cerca");
		map.put("sifRepertori.button.ricarica", "ricarica");
		map.put("sifRepertori.button.stampa", "stampa");
		map.put("sifRepertori.button.selez", "seleziona");
		map.put("sifRepertori.button.annul", "annulla");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");
		SifRicercaRepertoriForm sifRicRepForm = (SifRicercaRepertoriForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		this.caricaListe(sifRicRepForm);

		return mapping.getInputForward();
	}

	public ActionForward seleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SifRicercaRepertoriForm sifRicRepForm = (SifRicercaRepertoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sifRicRepForm.getSelezRadio() != null) {
			String siglaRep = sifRicRepForm.getSelezRadio();
			request.setAttribute("sigl", siglaRep);
			resetToken(request);
			return	Navigation.getInstance(request).goBack();
		}

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage(
				"errors.gestioneBibliografica.selObblOggSint"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}


	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SifRicercaRepertoriForm sifRicRepForm = (SifRicercaRepertoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (sifRicRepForm.getTestoRicerca() == null || sifRicRepForm.getTestoRicerca().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		SifRicercaRepertoriVO eleRepertorio = null;
		List<SifRicercaRepertoriVO> listaRepertoriNew = new ArrayList<SifRicercaRepertoriVO>();
		List<SifRicercaRepertoriVO> listaRepertori = sifRicRepForm.getListaRepertori();

		// Modifica Bug collaudo 5128: modificata la voce generica 'Descrizione' in quella più specifica 'Parole'
		// dato che la ricerca dei repertori può esser fatta, oltre che per 'Sigla' e 'Tipo', anche tramite parole della descrizione
//		if (sifRicRepForm.getTipoRicercaSelez().equals("Descrizione")) {
		if (sifRicRepForm.getTipoRicercaSelez().equals("Parole")) {
			// estrazione della parole dalla stringa; possono essere fino a 4
			String [] parole = sifRicRepForm.getTestoRicerca().split(" ");
			if (parole.length > 4) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.gestioneBibliografica.ric021"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (parole.length == 1) {
				for (int i = 0; i < listaRepertori.size(); i++) {
					eleRepertorio = listaRepertori.get(i);
					if (eleRepertorio.getDesc().toUpperCase().indexOf(parole[0].toUpperCase()) > -1) {
						listaRepertoriNew.add(listaRepertori.get(i));
					}
				}
			} else if (parole.length == 2) {
				for (int i = 0; i < listaRepertori.size(); i++) {
					eleRepertorio = listaRepertori.get(i);
					if (eleRepertorio.getDesc().toUpperCase().indexOf(parole[0].toUpperCase()) > -1
							&& eleRepertorio.getDesc().toUpperCase().indexOf(parole[1].toUpperCase()) > -1 ) {
						listaRepertoriNew.add(listaRepertori.get(i));
					}
				}
			} else if (parole.length == 3) {
				for (int i = 0; i < listaRepertori.size(); i++) {
					eleRepertorio = listaRepertori.get(i);
					if (eleRepertorio.getDesc().toUpperCase().indexOf(parole[0].toUpperCase()) > -1
							&& eleRepertorio.getDesc().toUpperCase().indexOf(parole[1].toUpperCase()) > -1
							&& eleRepertorio.getDesc().toUpperCase().indexOf(parole[2].toUpperCase()) > -1) {
						listaRepertoriNew.add(listaRepertori.get(i));
					}
				}
			} else if (parole.length == 4) {
				for (int i = 0; i < listaRepertori.size(); i++) {
					eleRepertorio = listaRepertori.get(i);
					if (eleRepertorio.getDesc().toUpperCase().indexOf(parole[0].toUpperCase()) > -1
							&& eleRepertorio.getDesc().toUpperCase().indexOf(parole[1].toUpperCase()) > -1
							&& eleRepertorio.getDesc().toUpperCase().indexOf(parole[2].toUpperCase()) > -1
							&& eleRepertorio.getDesc().toUpperCase().indexOf(parole[3].toUpperCase()) > -1) {
						listaRepertoriNew.add(listaRepertori.get(i));
					}
				}
			}
		} else if (sifRicRepForm.getTipoRicercaSelez().equals("Sigla")) {
			// RICERCA PER SIGLA
			for (int i = 0; i < listaRepertori.size(); i++) {
				eleRepertorio = listaRepertori.get(i);
				if (eleRepertorio.getSigl().toUpperCase().equals(sifRicRepForm.getTestoRicerca().toUpperCase())) {
					listaRepertoriNew.add(listaRepertori.get(i));
				}
			}
		} else {
//			 RICERCA PER TIPO
			for (int i = 0; i < listaRepertori.size(); i++) {
				eleRepertorio = listaRepertori.get(i);
				if (eleRepertorio.getTipo().toUpperCase().equals(sifRicRepForm.getTestoRicerca().toUpperCase())) {
					listaRepertoriNew.add(listaRepertori.get(i));
				}
			}
		}

		sifRicRepForm.setListaRepertori(listaRepertoriNew);
		return mapping.getInputForward();
	}

	public ActionForward ricarica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SifRicercaRepertoriForm sifRicRepForm = (SifRicercaRepertoriForm) form;
		this.caricaListe(sifRicRepForm);
		return mapping.getInputForward();
	}


	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("errors.gestioneBibliografica.testoProtocollo", "FUNZIONE NON DISPONIBILE"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return	Navigation.getInstance(request).goBack(true);
	}

	private void caricaListe(SifRicercaRepertoriForm sifRicercaRepertoriForm)
		throws RemoteException, CreateException, NamingException {

		List<ComboCodDescVO> lista = new ArrayList<ComboCodDescVO>();
		ComboCodDescVO codDesc;
		codDesc = new ComboCodDescVO();
		codDesc.setCodice("1");

		// Modifica Bug collaudo 5128: modificata la voce generica 'Descrizione' in quella più specifica 'Parole'
		// dato che la ricerca dei repertori può esser fatta, oltre che per 'Sigla' e 'Tipo', anche tramite parole della descrizione
//		codDesc.setDescrizione("Descrizione");
		codDesc.setDescrizione("Parole");

		lista.add(codDesc);

		codDesc = new ComboCodDescVO();
		codDesc.setCodice("2");
		codDesc.setDescrizione("Sigla");
		lista.add(codDesc);

		codDesc = new ComboCodDescVO();
		codDesc.setCodice("3");
		codDesc.setDescrizione("Tipo");
		lista.add(codDesc);

		sifRicercaRepertoriForm.setListaTipoRicerca(lista);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		sifRicercaRepertoriForm.setListaRepertori(factory.getGestioneRepertorio().getAllRepertori());

		return;
	}



}
