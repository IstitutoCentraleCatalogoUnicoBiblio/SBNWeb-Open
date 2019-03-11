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
package it.iccu.sbn.web.actions.servizi.utenti;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.actionforms.servizi.utenti.ListaMaterieForm;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaMaterieAction extends UtenteBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.conferma","ok");
		map.put("servizi.bottone.annulla", "chiudi");

		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaMaterieForm currentForm = (ListaMaterieForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setAnagraficaUtente((UtenteBibliotecaVO) request.getAttribute("UtenteScelto"));
				if (currentForm.getAnagraficaUtente()==null) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.utenti.listaMaterie.anagraficaNonPresente"));
					return this.backForward(request, true);
				}

				this.loadMaterie(request, currentForm);
				if (!ValidazioneDati.isFilled(currentForm.getListaMaterie()) ) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.utenti.listaMaterie.materieNonCensite"));
					return this.backForward(request, true);
				}
				//almaviva5_20120216 gestione bib. affiliate
				String bib = (String) request.getAttribute(BIBLIOTECA_ATTR);
				currentForm.setBiblioteca(ValidazioneDati.isFilled(bib) ? bib : navi.getUtente().getCodBib());
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}


	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		request.setAttribute("VengoDa",      "MaterieInteresse");

		return this.backForward(request, false);
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaMaterieForm listaMaterieForm = (ListaMaterieForm)form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			this.impostaMaterieScelteInUscita(listaMaterieForm);

			request.setAttribute("UtenteScelto", listaMaterieForm.getAnagraficaUtente());
			request.setAttribute("VengoDa",      "MaterieInteresse");
			resetToken(request);

			return this.backForward(request, false);//mapping.findForward("dettaglioUtente");
		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}

	private void loadMaterie(HttpServletRequest request, ListaMaterieForm form)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		RicercaMateriaVO ricMat = new RicercaMateriaVO();
		ricMat.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
		ricMat.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		ricMat.setCodice("");
		ricMat.setDescrizione("");
		ricMat.setNumeroElementiBlocco(4000);
		ricMat.setOrdinamento("1");
		ricMat.setTicket(Navigation.getInstance(request).getUserTicket());
		form.setListaMaterie(delegate.getListaMaterie(ricMat));

		this.impostaMaterieScelteInEntrata(form);
	}


	private void impostaMaterieScelteInEntrata(ListaMaterieForm listaForm) {
		int indiceArraySelezionate=0;
		List<MateriaVO> materieUtente =  listaForm.getAnagraficaUtente().getProfessione().getMaterie();
		if (materieUtente!=null && materieUtente.size()>0) {
			listaForm.setIndiciSelezionate(new Integer[materieUtente.size()]);
			Iterator<MateriaVO> iterator = listaForm.getListaMaterie().iterator();
			MateriaVO materia;
			int indiceIterator=0;
			while (iterator.hasNext()) {
				materia = iterator.next();
				if (materieUtente.contains(materia)) {
					listaForm.getIndiciSelezionate()[indiceArraySelezionate] = indiceIterator;
					indiceArraySelezionate++;
				}
				indiceIterator++;
			}
		}
	}


	private void impostaMaterieScelteInUscita(ListaMaterieForm listaForm) {
		List<MateriaVO> materie =  new ArrayList<MateriaVO>();
		MateriaVO materia;
		for (int i=0; i<listaForm.getIndiciSelezionate().length; i++) {
			materia = listaForm.getListaMaterie().get(listaForm.getIndiciSelezionate()[i]);
			materia.setSelezionato("S");
			materie.add(materia);
		}
		listaForm.getAnagraficaUtente().getProfessione().setMaterie(materie);
	}


	protected void impostaStatoMaterie(ListaMaterieForm listaForm) {
		List<MateriaVO> materie = listaForm.getAnagraficaUtente().getProfessione().getMaterie();
		if (materie == null)
			return;
		for (MateriaVO m : materie) {
			String selez = m.getSelezionato();
			if (selez != null) {
				int stato = m.getStato();
				switch (stato) {
				case MateriaVO.INIZIALE_NON_SELEZIONATO:
					if (selez.equals(""))
						continue;
					m.setStato(MateriaVO.INSERITO_NUOVO);
					break;
				case MateriaVO.INIZIALE_SELEZIONATO:
					if (selez.equals("S"))
						continue;
					m.setStato(MateriaVO.CANCELLATO_DA_INIZIALE_SELEZIONATO);
					break;
				case MateriaVO.INSERITO_NUOVO:
					if (selez.equals("S"))
						continue;
					m.setStato(MateriaVO.INIZIALE_NON_SELEZIONATO);
					break;
				case MateriaVO.CANCELLATO_DA_INIZIALE_SELEZIONATO:
					if (selez.equals(""))
						continue;
					m.setStato(MateriaVO.INIZIALE_SELEZIONATO);
					break;
				}
			}
		}
	}



}
