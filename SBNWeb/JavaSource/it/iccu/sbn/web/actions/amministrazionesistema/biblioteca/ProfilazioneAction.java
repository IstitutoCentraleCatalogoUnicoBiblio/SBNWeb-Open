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
package it.iccu.sbn.web.actions.amministrazionesistema.biblioteca;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.ProfilazioneTreeElementView;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.custom.amministrazione.OrderedTreeElement;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.ProfilazioneForm;
import it.iccu.sbn.web.actions.amministrazionesistema.ProfilazioneBaseAction;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.Enumeration;
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

public final class ProfilazioneAction extends ProfilazioneBaseAction {

    private static Logger log = Logger.getLogger(ProfilazioneAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.polo.salva", 					"salva");
		map.put("profilo.polo.annulla", 				"annulla");
		map.put("profilo.polo.selezionaTutti", 			"SelTutti");
		map.put("profilo.polo.deSelezionaTutti", 		"DeSelTutti");
		map.put("profilo.polo.button.auth",				"authorities");
		map.put("profilo.polo.button.mat",				"materiali");
		map.put("profilo.biblioteca.button.sel",    	"selBib");
		map.put("profilo.biblioteca.button.si",			"cambiaBibSalva");
		map.put("profilo.biblioteca.button.salva.ok",	"cambiaBibSalva");
		map.put("profilo.biblioteca.button.no",			"cambiaBibNonSalva");
		map.put("profilo.biblioteca.button.annulla",	"nonCambiaBib");
		map.put("profilo.polo.button.salva.ok",			"salva");
		map.put("profilo.polo.button.salva.no",			"nosalva");
		map.put("profilo.bibliotecario.button.modello.salva",	"salvaModello");
		map.put("profilo.bibliotecario.button.modello.carica",	"caricaModello");
		map.put("profilo.bibliotecario.button.ok",				"modello");
		map.put("profilo.bibliotecario.button.modello.annulla",	"noModello");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm currentForm = (ProfilazioneForm) form;

		Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ABILITAZIONI_BIBLIOTECA);
            currentForm.setAbilitatoWrite("FALSE");
        }
        catch (UtenteNotAuthorizedException e) {
            ActionMessages messaggio = new ActionMessages();
            messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messaggio.info.noaut"));
            this.saveErrors(request,messaggio);
            return mapping.findForward("blank");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_BIBLIOTECA);
            currentForm.setAbilitatoWrite("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoWrite("FALSE");
        }

		ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
		if ((List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY) != null)
			currentForm.setElencoParAuth((List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY));
		if ((List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA) != null)
			currentForm.setElencoParSem((List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA));
		if ((List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI) != null)
			currentForm.setElencoParMateriali((List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI));
		if (request.getAttribute("flagAuth") != null)
			currentForm.setFlagAuth((Boolean)request.getAttribute("flagAuth"));
		if (request.getAttribute("flagMat") != null)
			currentForm.setFlagMat((Boolean)request.getAttribute("flagMat"));
		if (request.getAttribute("nomebiblioteca") != null)
			currentForm.setNomeBib((String)request.getAttribute("nomebiblioteca"));
		if (request.getAttribute("codbiblioteca") != null) {
			currentForm.setSelezioneBib((String)request.getAttribute("codbiblioteca"));
			currentForm.setCodBib((String)request.getAttribute("codbiblioteca"));
		}
		if (request.getAttribute("recapitobiblioteca") != null)
			currentForm.setRecapito((String)request.getAttribute("recapitobiblioteca"));
		if (request.getAttribute("provenienza") != null)
			currentForm.setProvenienza((String)request.getAttribute("provenienza"));

		currentForm.setSelezioniCheck(currentForm.getCheckItemSelez());
		if (expandContractTree(request, root, currentForm.getSelezioniCheck(), currentForm) ) {
			currentForm.setCheckItemSelez(currentForm.getSelezioniCheck());
			return mapping.getInputForward();

		}

		if (selezioneRamo(request, root, currentForm.getSelezioniCheck(), currentForm) ) {
			currentForm.setCheckItemSelez(currentForm.getSelezioniCheck());
			return mapping.getInputForward();
		}

		String[] checked = null;
		if ((String[])request.getAttribute("checked") != null) {
			checked = (String[])request.getAttribute("checked");
			currentForm.setCheckItemSelez(checked);
		}

		// Chiama ejb per avere la lista delle attivita'
		if (root.getChildren() != null && root.getKey().equals("")) {
			OrderedTreeElement elencoAttivita;

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			elencoAttivita = factory.getBiblioteca().getElencoAttivita("ticket");
//			String bibInSess = Navigation.getInstance(request).getUtente().getCodBib();
//			List<ComboVO> elencoBib = biblio.getElencoBiblioteche();
//			for (int o = 0; o < elencoBib.size(); o++) {
//				if (currentForm.getSelezioneBib() != null && elencoBib.get(o).getCodice().equals(currentForm.getSelezioneBib())) {
//					currentForm.setNomeBib(elencoBib.get(o).getDescrizione().trim());
//					currentForm.setCodBib(elencoBib.get(o).getCodice());
//				}
//				if (currentForm.getSelezioneBib() == null && elencoBib.get(o).getCodice().equals(bibInSess)) {
//					currentForm.setNomeBib(elencoBib.get(o).getDescrizione().trim());
//					currentForm.setCodBib(elencoBib.get(o).getCodice());
//				}
//				elencoBib.get(o).setDescrizione(elencoBib.get(o).getCodice() + " - " + elencoBib.get(o).getDescrizione().trim());
//			}
//			currentForm.setElencoBib(elencoBib);
//			if (currentForm.getSelezioneBib() == null)
//				currentForm.setSelezioneBib(bibInSess);
			String[] selezioni = factory.getBiblioteca().getElencoAttivitaProfilo(currentForm.getCodBib());

			OrderedTreeElement.dumpOrderedTreeElement(elencoAttivita, 0);

//			root.setKey("rootKey");
			root.setKey("Attività");
//			root.setText("Attività");
			root.setTooltip("Attività");
//			root.setKeyDisplayMode(KeyDisplayMode.HIDE);
			root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_TEXT);
			root.setCheckVisible(false);
			root.setRadioVisible(false);
			root.setFlagCondiviso(true);
			root.setPlusDelete(true);

			loadTreeviewAttivita(root, elencoAttivita);


			List<String> elencoSelezioni = impostaSelezioniProfilo(root, selezioni, new ArrayList<String>());
			selezioni = new String[elencoSelezioni.size()];
			for (int d = 0; d < elencoSelezioni.size(); d++) {
				selezioni[d] = elencoSelezioni.get(d);
//				String attivita = root.findElementUnique(Integer.parseInt(elencoSelezioni.get(d))).getTooltip();
//				if (attivita.equals(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017) || attivita.equals(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026))
//					currentForm.setFlagAuth(true);
//				if (attivita.equals(codiciAtt.CREA_DOCUMENTO_1016) || attivita.equals(codiciAtt.MODIFICA_1019))
//					currentForm.setFlagMat(true);
			}
			this.controllaSelezioniFigli(root.getChildren(), selezioni);

			root.open();

			//almaviva5_20140129 evolutive google3
			caricaParametri(currentForm, currentForm.getCodBib());
		}

		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(root, elementiCheck);
		currentForm.setCheckItemSelez(trasformaArray(elementiCheck));
		return mapping.getInputForward();
	}

	private boolean expandContractTree(HttpServletRequest request,
			ProfilazioneTreeElementView root, String[] checked, ActionForm form) {
		Enumeration<?> params = request.getParameterNames();
		String id = null;
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(TreeElementView.TREEVIEW_SUBMIT_PARAM)) {

				String paramValue = request.getParameter(param);
				if (paramValue.equals(TreeElementView.TREEVIEW_SUBMIT_NULL))
					continue;
				id = paramValue;
				break;
			}
		}

		if (id != null) {
			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			TreeElementView node = root.findElementUnique(Integer.valueOf(id));
			if (node.isOpened()) {
				node.close();
				this.controllaSelezioniFigli(node.getChildren(), checked);
				this.controllaSelezioniAperti(root.getChildren(), checked);
				List<String> selezionati = new ArrayList<String>();
				getTreeChecked(root, selezionati);
				currentForm.setSelezioniCheck(trasformaArray(selezionati));
			}
			else {
				this.controllaSelezioniAperti(root.getChildren(), checked);
				node.open();
				checked = this.impostaSelezioniFigli(node.getChildren(), checked);
				List<String> selezionati = new ArrayList<String>();
				getTreeChecked(root, selezionati);
				currentForm.setSelezioniCheck(trasformaArray(selezionati));
				this.impostaVisibili(node.getChildren());
			}
			return true;
		}
		return false;
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneForm profilazioneForm = (ProfilazioneForm) form;

		//String[] selected = profilazioneForm.getCheckItemSelez();
		ProfilazioneTreeElementView root = profilazioneForm.getProfilazioneTreeElementView();
		String[] elencoSelezioni = null;
		if (profilazioneForm.getCheckBackup() != null)
			elencoSelezioni = profilazioneForm.getCheckBackup();
		else
			elencoSelezioni = profilazioneForm.getCheckItemSelez();
		this.controllaSelezioniAperti(root.getChildren(), elencoSelezioni);
		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(root, elementiCheck);
		String[] selected = trasformaArray(elementiCheck);
		selected = impostaSelezioniPadri(root, selected);
		profilazioneForm.setCheckItemSelez(selected);
		List<String> elencoAttivita = new ArrayList<String>();
		List<GruppoParametriVO> elencoAuthorities = profilazioneForm.getElencoParAuth();
		List<GruppoParametriVO> elencoMateriali = profilazioneForm.getElencoParMateriali();
		List<GruppoParametriVO> elencoSemantica = profilazioneForm.getElencoParSem();
		int errore = 0;
		if (selected != null && selected.length > 0) {
			for (int i = 0; i < selected.length; i++) {
				ProfilazioneTreeElementView elm = (ProfilazioneTreeElementView)root.findElementUnique(Integer.parseInt(selected[i]));
				if (elm.getTooltip()!= null || !elm.getTooltip().equals("")) {
					CodiciAttivita codiciAtt = CodiciAttivita.getIstance();
					if ((elm.getTooltip().equals(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017) || elm.getTooltip().equals(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026)) && elencoAuthorities != null && elencoAuthorities.size() == 0 && !profilazioneForm.isFlagAuth()) {
						errore = 1;
						break;
					}
					else if ((elm.getTooltip().equals(codiciAtt.CREA_DOCUMENTO_1016) || elm.getTooltip().equals(codiciAtt.MODIFICA_1019)) && elencoMateriali != null && elencoMateriali.size() == 0 && !profilazioneForm.isFlagMat()) {
						errore = 2;
						break;
					}
					else if(!elm.getTooltip().equals("Attività"))
						elencoAttivita.add(elm.getTooltip());
				}
			}

		}
		else {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noattivita"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}
		if (errore == 1) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparauth"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}
		else if (errore == 2) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparmat"));
			this.saveErrors(request,messaggio);
			profilazioneForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}

		//almaviva5_20111216 #4722 check livelli autorita su biblioteca
		ActionForward check = checkLivAutPolo(mapping, elencoAuthorities, request, response);
		if (check != null)
			return check;
		check = checkLivAutPolo(mapping, elencoMateriali, request, response);
		if (check != null)
			return check;

		//almaviva5_20120703 #5032
		check = checkParSemPolo(mapping, elencoSemantica, request, response);
		if (check != null)
			return check;
		//

		/*
		CodiciAttivita codiciAtt = CodiciAttivita.getIstance();
		ProfilazioneTreeElementView elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017);
		boolean creaAuth = false;
		if (elm != null && elm.isChecked())
			creaAuth = true;
		elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);
		boolean modificaAuth = false;
		if (elm != null && elm.isChecked())
			modificaAuth = true;
		if(!creaAuth && !modificaAuth) {
			elencoAuthorities = new ArrayList<GruppoParametriVO>();
			elencoSemantica = new ArrayList<GruppoParametriVO>();
			profilazioneForm.setElencoParAuth(elencoAuthorities);
			profilazioneForm.setElencoParSem(elencoSemantica);
		}

		elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.CREA_DOCUMENTO_1016);
		boolean creaMat = false;
		if (elm != null && elm.isChecked())
			creaMat = true;
		elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.MODIFICA_1019);
		boolean modificaMat = false;
		if (elm != null && elm.isChecked())
			modificaMat = true;
		if (!creaMat && !modificaMat) {
			elencoMateriali = new ArrayList<GruppoParametriVO>();
			profilazioneForm.setElencoParMateriali(elencoMateriali);
		}
		*/
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		String codiceBib = profilazioneForm.getCodBib();

		if (profilazioneForm.getConferma() == null || !profilazioneForm.getConferma().equals("TRUE")) {
			List<String> controllo = factory.getBiblioteca().controllaAttivita(codiceBib, elencoAttivita);
			if (ValidazioneDati.isFilled(controllo) ) {
				profilazioneForm.setConferma("TRUE");
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.info.attivitasub"));
				this.saveErrors(request,messaggio);
				this.impostaNodiRossi(root, controllo);
				profilazioneForm.setRootBackup((ProfilazioneTreeElementView)root.clone());
//				this.impostaIntoccabili(root);
				profilazioneForm.setProfilazioneTreeElementView(root);
				profilazioneForm.setCheckItemSelez(selected);
				profilazioneForm.setCheckBackup(selected);
				return mapping.getInputForward();
			}
		}

		String codiceUtente = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale() + "";
		try {
			int inserito = factory.getBiblioteca().setProfiloBiblioteca(codiceBib, codiceUtente, elencoAttivita, elencoAuthorities, elencoMateriali, elencoSemantica);
			if (inserito == 1) {
				ActionMessages errors = new ActionMessages();
				errors.add("info", new ActionMessage("info.profilo.biblioteca.semantica"));
				this.saveErrors(request, errors);
			}
			else if (inserito == 2) {
				ActionMessages errors = new ActionMessages();
				errors.add("messaggioErrore", new ActionMessage("error.profilo.polo.salva"));
				this.saveErrors(request, errors);
			}
			else if (inserito == 0) {
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.info.salvaok"));
				this.saveErrors(request,messaggio);
			}
		}
		catch (DataException e) {
			if (e.getMessage().equals("3")) {
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.soggettari"));
				this.saveErrors(request,messaggio);
				return mapping.getInputForward();
			}
			else if (e.getMessage().equals("4")) {
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.thesauri"));
				this.saveErrors(request,messaggio);
				return mapping.getInputForward();
			}
		}
		if (profilazioneForm.getRootBackup() != null && !profilazioneForm.getRootBackup().getKey().equals("")) {
			root = (ProfilazioneTreeElementView)profilazioneForm.getRootBackup().clone();
			profilazioneForm.setRootBackup(new ProfilazioneTreeElementView());
		}
		this.togliNodiRossi(root);
		profilazioneForm.setProfilazioneTreeElementView(root);
		profilazioneForm.setConferma("FALSE");
		profilazioneForm.setCheckBackup(null);
		return mapping.getInputForward();

	}

	protected void impostaIntoccabili(ProfilazioneTreeElementView NodoTreeView) {
		NodoTreeView.setGroupingCheck(true);
		List<TreeElementView> figli = null;
		if (NodoTreeView.getChildren() != null) {
			figli = NodoTreeView.getChildren();
			for (int i= 0; i < figli.size(); i++) {
				impostaIntoccabili((ProfilazioneTreeElementView)figli.get(i));
			}
		}
	}

	private void impostaNodiRossi(ProfilazioneTreeElementView NodoTreeView, List<String> elencoAttivita) {
		if (elencoAttivita.contains(NodoTreeView.getTooltip())) {
			NodoTreeView.setRedItem(true);
			if (NodoTreeView.getParent() != null)
					NodoTreeView.getParent().open();
		}
		else {
			NodoTreeView.setRedItem(false);
		}
		List<TreeElementView> figli = null;
		if (NodoTreeView.getChildren() != null) {
			figli = NodoTreeView.getChildren();
			for (int i= 0; i < figli.size(); i++) {
				impostaNodiRossi((ProfilazioneTreeElementView)figli.get(i), elencoAttivita);
			}
		}
	}

	private void togliNodiRossi(ProfilazioneTreeElementView NodoTreeView) {
		if (NodoTreeView.isRedItem())
			NodoTreeView.setRedItem(false);
		List<TreeElementView> figli = null;
		if (NodoTreeView.getChildren() != null) {
			figli = NodoTreeView.getChildren();
			for (int i= 0; i < figli.size(); i++) {
				togliNodiRossi((ProfilazioneTreeElementView)figli.get(i));
			}
		}
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm currentForm = (ProfilazioneForm) form;
		currentForm.setCheckItemSelez(null);
		currentForm.setElencoParAuth(new ArrayList<GruppoParametriVO>());
		currentForm.setElencoParMateriali(new ArrayList<GruppoParametriVO>());
		currentForm.setElencoParSem(new ArrayList<GruppoParametriVO>());
		currentForm.setProfilazioneTreeElementView(new ProfilazioneTreeElementView());
		currentForm.setRootBackup(new ProfilazioneTreeElementView());
		currentForm.setFlagAuth(false);
		currentForm.setFlagMat(false);
//		ActionMessages messaggio = new ActionMessages();
//		messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.annulla"));
//		this.saveErrors(request,messaggio);
		if (currentForm.getProvenienza()!= null && currentForm.getProvenienza().equals("nuovo")) {
			request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, currentForm.getId());
			request.setAttribute("provenienza", "ricerca");
			return mapping.findForward("modifica");
		}
		if (currentForm.getProvenienza() == null) {
			return mapping.findForward("ricerca");
		}
		return mapping.findForward("sintetica");
//		form = new ProfilazioneForm();
//		return unspecified(mapping, form, request, response);
//		NavigationForward forward = new NavigationForward(DirectionType.DEFAULT);
//		forward.forceFormInstance(new ProfilazioneForm());
//		return mapping.findForward("profilo");
//		return null;
	}

	public ActionForward authorities(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm currentForm = (ProfilazioneForm) form;

		ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
		this.controllaSelezioniAperti(root.getChildren(), currentForm.getCheckItemSelez());
		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(currentForm.getProfilazioneTreeElementView(), elementiCheck);
		String[] selected = trasformaArray(elementiCheck);
		ProfilazioneTreeElementView albero = currentForm.getProfilazioneTreeElementView();
		selected = impostaSelezioniPadri(albero, selected);
		boolean trovato = false;
		if (selected != null)
			for (int i = 0; i < selected.length; i++) {
				String element = selected[i];
				String codiceAttivita = albero.findElementUnique(Integer.parseInt(element)).getTooltip();
				CodiciAttivita codici = CodiciAttivita.getIstance();
				if (codiceAttivita.equals(codici.CREA_ELEMENTO_DI_AUTHORITY_1017) || codiceAttivita.equals(codici.MODIFICA_ELEMENTO_DI_AUTHORITY_1026)) {
					trovato = true;
					break;
				}
			}
		if (!trovato) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noauth"));
			this.saveErrors(request,messaggio);
			currentForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}
		request.setAttribute("checked", selected);
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY, currentForm.getElencoParAuth());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, currentForm.getElencoParSem());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, currentForm.getElencoParMateriali());
		request.setAttribute("flagMat", currentForm.isFlagMat());
		request.setAttribute("biblio", currentForm.getCodBib());
		request.setAttribute("nomeBib", currentForm.getNomeBib());
		return mapping.findForward("authorities");
	}

	public ActionForward materiali(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm currentForm = (ProfilazioneForm) form;

		ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
		this.controllaSelezioniAperti(root.getChildren(), currentForm.getCheckItemSelez());
		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(currentForm.getProfilazioneTreeElementView(), elementiCheck);
		String[] selected = trasformaArray(elementiCheck);
		ProfilazioneTreeElementView albero = currentForm.getProfilazioneTreeElementView();
		selected = impostaSelezioniPadri(albero, selected);
		boolean trovato = false;
		if (selected != null)
			for (int i = 0; i < selected.length; i++) {
				String element = selected[i];
				String codiceAttivita = albero.findElementUnique(Integer.parseInt(element)).getTooltip();
				CodiciAttivita codici = CodiciAttivita.getIstance();
				if (codiceAttivita.equals(codici.CREA_DOCUMENTO_1016) || codiceAttivita.equals(codici.MODIFICA_1019)) {
					trovato = true;
					break;
				}
			}
		if (!trovato) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.nomat"));
			this.saveErrors(request,messaggio);
			currentForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}
		request.setAttribute("checked", selected);
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, currentForm.getElencoParMateriali());
		request.setAttribute("flagAuth", currentForm.isFlagAuth());
		request.setAttribute("biblio", currentForm.getCodBib());
		request.setAttribute("nomeBib", currentForm.getNomeBib());
		return mapping.findForward("materiali");
	}

	public ActionForward SelTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneForm currentForm = (ProfilazioneForm) form;
		List<String> selezionati = new ArrayList<String>();
		impostaSelezioni(currentForm.getProfilazioneTreeElementView(), selezionati);
		String[] selected = new String[selezionati.size()];
		for (int i = 0; i<selezionati.size(); i++) {
			selected[i] = selezionati.get(i);
		}
		//currentForm.setCheckItemSelez(impostaSelezioniPadri(currentForm.getProfilazioneTreeElementView(), selected));
		currentForm.setCheckItemSelez(selected);
		return mapping.getInputForward();
	}

	public ActionForward DeSelTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneForm currentForm = (ProfilazioneForm) form;
		impostaDeSelezioni(currentForm.getProfilazioneTreeElementView());
		currentForm.setCheckItemSelez(null);
		return mapping.getInputForward();
	}
/*
	private void dumpOrderedTreeElement(OrderedTreeElement ote, int level)
	{
		StringBuffer sb = new StringBuffer();

		int i;
		for (i=0; i < ote.getElements().size(); i++)
		{
			sb.setLength(0);
			OrderedTreeElement elm = ote.getElements().get(i);
			for (int j=0; j < level; j++)
				//System.out.print("    " );
				sb.append("    ");

			System.out.println(sb.toString() + "Key=" + elm.getKey() + " Value=" + elm.getValue());
			// se ci sono figli stampa i figli
			OrderedTreeElement child = elm.getChild();
			if ( child != null)
				dumpOrderedTreeElement(child, level+1);
		}
	}
*/

	private void controllaSelezioniFigli(List<TreeElementView> nodi, String[] input) {
		for (int i =0; i < nodi.size(); i++) {
			ProfilazioneTreeElementView nodo = (ProfilazioneTreeElementView)nodi.get(i);
			boolean trovato = false;
			if (input != null)
				for (int j = 0; j < input.length; j++) {
					if (nodo.getRepeatableId() == Integer.parseInt(input[j])) {
						trovato = true;
						break;
					}
				}
			if (trovato)
				nodo.setChecked(true);
			else
				nodo.setChecked(false);
			nodo.setVisible(false);
			if (nodo.hasChildren())
				controllaSelezioniFigli(nodo.getChildren(), input);
		}
	}

//	private void controllaSelezioniVisibili(List<TreeElementView> nodi, String[] input) {
//		for (int i =0; i < nodi.size(); i++) {
//			ProfilazioneTreeElementView nodo = (ProfilazioneTreeElementView)nodi.get(i);
//			if (nodo.isVisible()) {
//				boolean trovato = false;
//				if (input != null)
//					for (int j = 0; j < input.length; j++) {
//						if (nodo.getUniqueId() == Integer.parseInt(input[j])) {
//							trovato = true;
//							break;
//						}
//					}
//				if (trovato)
//					nodo.setChecked(true);
//				else
//					nodo.setChecked(false);
//			}
//			if (nodo.hasChildren())
//				controllaSelezioniVisibili(nodo.getChildren(), input);
//		}
//	}

	private String[] impostaSelezioniFigli(List<TreeElementView> nodi, String[] checked) {
		List<String> newChecked = new ArrayList<String>();
		if (checked != null)
			for (int i = 0; i<checked.length; i++)
				newChecked.add(checked[i]);
		for (int i =0; i < nodi.size(); i++) {
			ProfilazioneTreeElementView nodo = (ProfilazioneTreeElementView)nodi.get(i);
			if (nodo.isChecked()) {
				String id = nodo.getRepeatableId() + "";
				if (!newChecked.contains(id))
					newChecked.add(id);
			}
		}
		checked = new String[newChecked.size()];
		for (int p =0; p<newChecked.size(); p++) {
			checked[p] = newChecked.get(p);
		}
		return checked;
	}

	private void controllaSelezioniAperti(List<TreeElementView> nodi, String[] input) {
		for (int i =0; i < nodi.size(); i++) {
			ProfilazioneTreeElementView nodo = (ProfilazioneTreeElementView)nodi.get(i);
			if (nodo.isOpened()) {
				for (int l = 0;l < nodo.getChildren().size(); l++) {
					ProfilazioneTreeElementView figlio = (ProfilazioneTreeElementView)nodo.getChildren().get(l);
					boolean trovato = false;
					if (input != null)
						for (int j = 0; j < input.length; j++) {
							if (figlio.getRepeatableId() == Integer.parseInt(input[j])) {
								trovato = true;
								break;
							}
						}
					if (trovato)
						figlio.setChecked(true);
					else
						figlio.setChecked(false);
					if (figlio.hasChildren())
						controllaSelezioniAperti(figlio.getChildren(), input);
				}
			}
			else if (nodo.getChildren().size() == 0) {
				boolean trovato = false;
				if (input != null)
					for (int j = 0; j < input.length; j++) {
						if (nodo.getRepeatableId() == Integer.parseInt(input[j])) {
							trovato = true;
							break;
						}
					}
				if (trovato)
					nodo.setChecked(true);
				else
					nodo.setChecked(false);
			}
		}
	}

	private void getTreeChecked(ProfilazioneTreeElementView root, List<String> output) {
		if (root.isChecked())
			output.add(root.getRepeatableId() + "");
		if (root.hasChildren()) {
			for (int i = 0; i < root.getChildren().size(); i++) {
				getTreeChecked((ProfilazioneTreeElementView)root.getChildren().get(i), output);
			}
		}
	}

	private String[] trasformaArray (List<String> array) {
		String[] output = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			output[i] = array.get(i);
		}
		return output;
	}

	private void impostaVisibili(List<TreeElementView> root) {
		for (int i = 0; i < root.size(); i++) {
			((ProfilazioneTreeElementView)root.get(i)).setVisible(true);
			if (root.get(i).hasChildren())
				impostaVisibili(((ProfilazioneTreeElementView)root.get(i)).getChildren());
		}
	}

	private void impostaSelezioni(ProfilazioneTreeElementView NodoTreeView, List<String> output) {
		if (NodoTreeView.isCheckVisible()) {
			output.add(NodoTreeView.getRepeatableId() + "");
			NodoTreeView.setChecked(true);
		}

		List<TreeElementView> figli = null;
		if (NodoTreeView.getChildren() != null) {
			figli = NodoTreeView.getChildren();
			for (int i= 0; i < figli.size(); i++) {
				impostaSelezioni((ProfilazioneTreeElementView)figli.get(i), output);
			}
		}

	}

	private void impostaDeSelezioni(ProfilazioneTreeElementView NodoTreeView) {
		NodoTreeView.setChecked(false);

		List<TreeElementView> figli = null;
		if (NodoTreeView.getChildren() != null) {
			figli = NodoTreeView.getChildren();
			for (int i= 0; i < figli.size(); i++) {
				impostaDeSelezioni((ProfilazioneTreeElementView)figli.get(i));
			}
		}

	}

	private List<String> impostaSelezioniProfilo(ProfilazioneTreeElementView NodoTreeView, String[] input, List<String> output) {
		if (input != null) {
			for (int i = 0; i < input.length; i++) {
				if (NodoTreeView.getTooltip().equals(input[i])) {
					output.add(NodoTreeView.getRepeatableId() + "");
					NodoTreeView.setChecked(true);
				}
			}
			List<TreeElementView> figli = null;
			if (NodoTreeView.getChildren() != null) {
				figli = NodoTreeView.getChildren();
				for (int i= 0; i < figli.size(); i++) {
					impostaSelezioniProfilo((ProfilazioneTreeElementView)figli.get(i), input, output);
				}
			}
		}
		return output;
	}

	private String[] impostaSelezioniPadri(ProfilazioneTreeElementView albero, String [] selezioni) {
		List<String> outputArray = new ArrayList<String>();
		if (selezioni != null) {
			for (int i = 0; i < selezioni.length; i++) {
				String element = selezioni[i];
				TreeElementView ramo = albero.findElementUnique(Integer.parseInt(element));
				ProfilazioneTreeElementView parent = (ProfilazioneTreeElementView) ramo.getParent();
				if (parent != null && parent.getParent() != null) {
					//Controllo che non sia già stato inserito:
					String idPadre = parent.getRepeatableId() + "";
					boolean trovato = false;
					for (int j = 0; j < outputArray.size(); j++) {
						if (outputArray.get(j).equals(idPadre)) {
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						parent.setChecked(true);
						outputArray.add(idPadre);
					}
				}
//				if (albero.)
				outputArray.add(element);
			}
			String[] output = new String[outputArray.size()];
			for (int k=0; k<outputArray.size(); k++){
				output[k] = outputArray.get(k);
			}
			return output;
		}
		else return selezioni;
	}

	private void loadTreeviewAttivita(ProfilazioneTreeElementView NodoTreeView, OrderedTreeElement elencoAttivita)
	{
		// Aggiungi i nodi di primo livello
		for (int i=0; i < elencoAttivita.getElements().size(); i++)
		{
			OrderedTreeElement elm = elencoAttivita.getElements().get(i);

			ProfilazioneTreeElementView nodoTreeViewChild = (ProfilazioneTreeElementView) NodoTreeView.addChild();
			nodoTreeViewChild.setKey((String)elm.getKey());
			//nodoTreeViewChild.setText((String)elm.getValue());
			nodoTreeViewChild.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_TEXT);
			nodoTreeViewChild.setCheckVisible(true);
			nodoTreeViewChild.setRadioVisible(false);
			nodoTreeViewChild.setFlagCondiviso(true);
			nodoTreeViewChild.setTooltip(elm.getTooltip());
			// se ci sono figli stampa i figli
			OrderedTreeElement attivitaChild = elm.getChild();
			if ( attivitaChild != null && attivitaChild.getElements().size() > 0)
			{
				nodoTreeViewChild.setCheckVisible(true);
				nodoTreeViewChild.setGroupingCheck(true);
				loadTreeviewAttivita(nodoTreeViewChild, attivitaChild);
			}
		}
	} // end

	private boolean selezioneRamo(HttpServletRequest request,
			ProfilazioneTreeElementView root, String[] checked, ActionForm form) {
		Enumeration<?> params = request.getParameterNames();
		String id = null;
		String selezione = "";
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(TreeElementView.TREEVIEW_SUBMIT_SELECT)) {

				String paramValue = request.getParameter(param);
				if (paramValue.equals(TreeElementView.TREEVIEW_SUBMIT_NULL))
					continue;
				id = paramValue;
				selezione = "SELECT";
				break;
			}
			if (param.startsWith(TreeElementView.TREEVIEW_SUBMIT_DESELECT)) {

				String paramValue = request.getParameter(param);
				if (paramValue.equals(TreeElementView.TREEVIEW_SUBMIT_NULL))
					continue;
				id = paramValue;
				selezione = "DESELECT";
				break;
			}
		}

		if (id != null) {
			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			TreeElementView node = root.findElementUnique(Integer.valueOf(id));
			if (selezione.equals("SELECT")) {
				checked = this.selezionaTuttiFigli(node.getChildren(), checked);
				((ProfilazioneTreeElementView)node).setChecked(true);
				this.controllaSelezioniAperti(root.getChildren(), checked);
				List<String> selezionati = new ArrayList<String>();
				getTreeChecked(root, selezionati);
				currentForm.setSelezioniCheck(trasformaArray(selezionati));
				this.impostaVisibili(node.getChildren());
			}
			else {
				checked = this.deSelezionaTuttiFigli(node.getChildren(), checked);
				((ProfilazioneTreeElementView)node).setChecked(false);
				this.controllaSelezioniAperti(root.getChildren(), checked);
				List<String> selezionati = new ArrayList<String>();
				getTreeChecked(root, selezionati);
				currentForm.setSelezioniCheck(trasformaArray(selezionati));
				this.impostaVisibili(node.getChildren());
			}
			return true;
		}
		return false;
	}

	private String[] selezionaTuttiFigli(List<TreeElementView> nodi, String[] checked) {
		List<String> newChecked = new ArrayList<String>();
		if (checked != null)
			for (int i = 0; i<checked.length; i++)
				newChecked.add(checked[i]);
		for (int i =0; i < nodi.size(); i++) {
			ProfilazioneTreeElementView nodo = (ProfilazioneTreeElementView)nodi.get(i);
			nodo.setChecked(true);
			String id = nodo.getRepeatableId() + "";
			if (!newChecked.contains(id))
				newChecked.add(id);
		}
		checked = new String[newChecked.size()];
		for (int p =0; p<newChecked.size(); p++) {
			checked[p] = newChecked.get(p);
		}
		return checked;
	}

	private String[] deSelezionaTuttiFigli(List<TreeElementView> nodi, String[] checked) {
		List<String> newChecked = new ArrayList<String>();
		if (checked != null)
			for (int i = 0; i<checked.length; i++)
				newChecked.add(checked[i]);
		for (int i =0; i < nodi.size(); i++) {
			ProfilazioneTreeElementView nodo = (ProfilazioneTreeElementView)nodi.get(i);
			nodo.setChecked(false);
			String id = nodo.getRepeatableId() + "";
			if (newChecked.contains(id))
				newChecked.remove(id);
		}
		checked = new String[newChecked.size()];
		for (int p =0; p<newChecked.size(); p++) {
			checked[p] = newChecked.get(p);
		}
		return checked;
	}


		public ActionForward cambiaBibSalva(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
					throws Exception {

					ProfilazioneForm currentForm = (ProfilazioneForm) form;

					ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
					List<String> elementiCheck = new ArrayList<String>();
					getTreeChecked(root, elementiCheck);
					String[] selected = trasformaArray(elementiCheck);
					selected = impostaSelezioniPadri(root, selected);
					currentForm.setCheckItemSelez(selected);
					List<String> elencoAttivita = new ArrayList<String>();
					List<GruppoParametriVO> elencoAuthorities = currentForm.getElencoParAuth();
					List<GruppoParametriVO> elencoMateriali = currentForm.getElencoParMateriali();
					List<GruppoParametriVO> elencoSemantica = currentForm.getElencoParSem();
					int errore = 0;
					if (selected != null && selected.length > 0) {
						for (int i=0; i<selected.length; i++) {
							ProfilazioneTreeElementView elm = (ProfilazioneTreeElementView)root.findElementUnique(Integer.parseInt(selected[i]));
							if (elm.getTooltip()!= null || !elm.getTooltip().equals("")) {
								CodiciAttivita codiciAtt = CodiciAttivita.getIstance();
								if ((elm.getTooltip().equals(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017) || elm.getTooltip().equals(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026)) && elencoAuthorities != null && elencoAuthorities.size() == 0 && !currentForm.isFlagAuth()) {
									errore = 1;
									break;
								}
								else if ((elm.getTooltip().equals(codiciAtt.CREA_DOCUMENTO_1016) || elm.getTooltip().equals(codiciAtt.MODIFICA_1019)) && elencoMateriali != null && elencoMateriali.size() == 0 && !currentForm.isFlagMat()) {
									errore = 2;
									break;
								}
								else if(!elm.getTooltip().equals("Attività"))
									elencoAttivita.add(elm.getTooltip());
							}
						}

					}
					else {
						ActionMessages messaggio = new ActionMessages();
						messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noattivita"));
						this.saveErrors(request,messaggio);
						return mapping.getInputForward();
					}
					if (errore == 1) {
						ActionMessages messaggio = new ActionMessages();
						messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparauth"));
						this.saveErrors(request,messaggio);
						return mapping.getInputForward();
					}
					else if (errore == 2) {
						ActionMessages messaggio = new ActionMessages();
						messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparmat"));
						this.saveErrors(request,messaggio);
						currentForm.setCheckItemSelez(selected);
						return mapping.getInputForward();
					}
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					String codiceBib = currentForm.getCodBib();
					if (currentForm.getConferma() == null || !currentForm.getConferma().equals("TRUE")) {
						List<String> controllo = factory.getBiblioteca().controllaAttivita(codiceBib, elencoAttivita);
						if (controllo.size() > 0) {
							currentForm.setConferma("TRUE");
							ActionMessages messaggio = new ActionMessages();
							messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.info.attivitasub"));
							this.saveErrors(request,messaggio);
							this.impostaNodiRossi(root, controllo);
							currentForm.setRootBackup((ProfilazioneTreeElementView)root.clone());
//							this.impostaIntoccabili(root);
							currentForm.setProfilazioneTreeElementView(root);
							currentForm.setCheckItemSelez(selected);
							return mapping.getInputForward();
						}
					}

					String codiceUtente = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale() + "";
					try {
						int inserito = factory.getBiblioteca().setProfiloBiblioteca(codiceBib, codiceUtente, elencoAttivita, elencoAuthorities, elencoMateriali, elencoSemantica);
						if (inserito == 1) {
							ActionMessages errors = new ActionMessages();
							errors.add("info", new ActionMessage("info.profilo.biblioteca.semantica"));
							this.saveErrors(request, errors);
						}
						else if (inserito == 2) {
							ActionMessages errors = new ActionMessages();
							errors.add("messaggioErrore", new ActionMessage("error.profilo.polo.salva"));
							this.saveErrors(request, errors);
						}
						else if (inserito == 0) {
							ActionMessages messaggio = new ActionMessages();
							messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.info.salvaok"));
							this.saveErrors(request,messaggio);
						}
					}
					catch (DataException e) {
						if (e.getMessage().equals("3")) {
							ActionMessages messaggio = new ActionMessages();
							messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.soggettari"));
							this.saveErrors(request,messaggio);
							return mapping.getInputForward();
						}
						else if (e.getMessage().equals("4")) {
							ActionMessages messaggio = new ActionMessages();
							messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.parametri.thesauri"));
							this.saveErrors(request,messaggio);
							return mapping.getInputForward();
						}
					}

					currentForm.setCheckItemSelez(null);
					currentForm.setElencoParAuth(new ArrayList<GruppoParametriVO>());
					currentForm.setElencoParMateriali(new ArrayList<GruppoParametriVO>());
					currentForm.setElencoParSem(new ArrayList<GruppoParametriVO>());
					currentForm.setProfilazioneTreeElementView(new ProfilazioneTreeElementView());
					currentForm.setFlagAuth(false);
					currentForm.setFlagMat(false);
					currentForm.setSalvataggio("FALSE");
					currentForm.setConferma("FALSE");
					return unspecified(mapping, form, request, response);
			}

		public ActionForward selBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				ProfilazioneForm currentForm = (ProfilazioneForm) form;

				ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
				this.controllaSelezioniAperti(root.getChildren(), currentForm.getCheckItemSelez());
				List<String> elementiCheck = new ArrayList<String>();
				getTreeChecked(root, elementiCheck);
				String[] selected = trasformaArray(elementiCheck);
				selected = impostaSelezioniPadri(root, selected);
				currentForm.setCheckItemSelez(selected);

				if (currentForm.getCodBib().equals(currentForm.getSelezioneBib()))
					return mapping.getInputForward();
				currentForm.setSalvataggio("TRUE");
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.biblioteca.salva.conferma"));
				this.saveErrors(request,messaggio);
				currentForm.setRootBackup((ProfilazioneTreeElementView)root.clone());
//				this.impostaIntoccabili(root);
				currentForm.setProfilazioneTreeElementView(root);

				return mapping.getInputForward();
		}

		public ActionForward cambiaBibNonSalva(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
					throws Exception {

					ProfilazioneForm currentForm = (ProfilazioneForm) form;
					currentForm.setCheckItemSelez(null);
					currentForm.setElencoParAuth(new ArrayList<GruppoParametriVO>());
					currentForm.setElencoParMateriali(new ArrayList<GruppoParametriVO>());
					currentForm.setElencoParSem(new ArrayList<GruppoParametriVO>());
					currentForm.setProfilazioneTreeElementView(new ProfilazioneTreeElementView());
					currentForm.setRootBackup(new ProfilazioneTreeElementView());
					currentForm.setFlagAuth(false);
					currentForm.setFlagMat(false);
					currentForm.setSalvataggio("FALSE");
					return unspecified(mapping, form, request, response);
			}

		public ActionForward nonCambiaBib(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
					throws Exception {

					ProfilazioneForm currentForm = (ProfilazioneForm) form;
					currentForm.setSalvataggio("FALSE");

					ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
					List<String> elementiCheck = new ArrayList<String>();
					getTreeChecked(root, elementiCheck);
					currentForm.setCheckItemSelez(trasformaArray(elementiCheck));
					currentForm.setProfilazioneTreeElementView((ProfilazioneTreeElementView)currentForm.getRootBackup().clone());

					return mapping.getInputForward();
			}

		public ActionForward nosalva(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			currentForm.setConferma("FALSE");
			currentForm.setSalvataggio("FALSE");
			ProfilazioneTreeElementView root = (ProfilazioneTreeElementView)currentForm.getRootBackup().clone();
//			this.controllaSelezioniVisibili(root.getChildren(), currentForm.getCheckItemSelez());
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			String[] selected = trasformaArray(elementiCheck);
			selected = impostaSelezioniPadri(root, selected);
			currentForm.setCheckItemSelez(selected);
			currentForm.setProfilazioneTreeElementView(root);
			currentForm.setCheckBackup(null);
//			this.togliNodiRossi(root);
//			currentForm.setProfilazioneTreeElementView(root);

			return mapping.getInputForward();
		}

		public ActionForward salvaModello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			currentForm.setModelloOp("SALVA");

			AmministrazioneBibliotecario bibliotecario = DomainEJBFactory.getInstance().getBibliotecario();

			List<ComboVO> elencoModelli = new ArrayList<ComboVO>();

			ComboVO combo = new ComboVO();
			combo.setCodice("NUOVO");
			combo.setDescrizione("<NUOVO MODELLO>");
			elencoModelli.add(combo);
			List<String> elencoDB = bibliotecario.getElencoModelli();
			if (elencoDB.size() > 0)
				for (int i = 0; i < elencoDB.size(); i++) {
					combo = new ComboVO();
					combo.setCodice(elencoDB.get(i));
					combo.setDescrizione(elencoDB.get(i));
					elencoModelli.add(combo);
				}
			currentForm.setElencoModelli(elencoModelli);
			currentForm.setModello("NUOVO");
			currentForm.setNuovoModello("FALSE");
	        ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			currentForm.setCheckItemSelez(trasformaArray(elementiCheck));

			return mapping.getInputForward();
		}

		public ActionForward caricaModello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			currentForm.setModelloOp("CARICA");
			currentForm.setModello("");
	        ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			currentForm.setCheckItemSelez(trasformaArray(elementiCheck));

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ComboVO> elencoModelli = new ArrayList<ComboVO>();
			List<String> elencoDB = factory.getBiblioteca().getElencoModelli();
			if (elencoDB.size() > 0)
				for (int i = 0; i < elencoDB.size(); i++) {
					ComboVO combo = new ComboVO();
					combo.setCodice(elencoDB.get(i));
					combo.setDescrizione(elencoDB.get(i));
					elencoModelli.add(combo);
				}
			else {
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.bibliotecario.modello.nomodelli"));
				this.saveErrors(request,messaggio);
				currentForm.setModelloOp("");
				return mapping.getInputForward();
			}
			currentForm.setElencoModelli(elencoModelli);

			return mapping.getInputForward();
		}

		public ActionForward modello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			String provenienza = currentForm.getModelloOp();
			if (provenienza.equals("CARICA")) {
		        ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();

		        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				//Carico le attivita':
				String[] selezioni = factory.getBiblioteca().getElencoAttivitaProfiloModello(currentForm.getModello());

				//almaviva5_20111117 check abilitazioni mancanti
				List<String> missing = new ArrayList<String>();
				for (int i = 0; i < selezioni.length; i++)
					if (root.findElementByTooltip(selezioni[i]) == null ) {
						log.warn("attività modello mancante: " + selezioni[i]);
						missing.add(selezioni[i]);
					}

				String formatMsg = ValidazioneDati.formatValueList(missing, ", ");
				if (ValidazioneDati.isFilled(formatMsg)) {
					LinkableTagUtils.addError(request, new ActionMessage("profilo.biblioteca.modello.incompatibile", formatMsg));
					return mapping.getInputForward();
				}

				//Carico i parametri (solo per i modelli vengono caricati prima):
				List<?> elencoParametri = factory.getBiblioteca().getElencoParametriModello(currentForm.getModello());

				List<GruppoParametriVO> authorities = null;
				List<GruppoParametriVO> materiali = null;
				List<GruppoParametriVO> semantica = null;
				if (elencoParametri != null && elencoParametri.size() == 3) {
					authorities = (List<GruppoParametriVO>)elencoParametri.get(0);
					materiali = (List<GruppoParametriVO>)elencoParametri.get(1);
					semantica = (List<GruppoParametriVO>)elencoParametri.get(2);
					currentForm.setElencoParAuth(authorities);
					currentForm.setElencoParSem(semantica);
					currentForm.setElencoParMateriali(materiali);
					currentForm.setFlagAuth(false);
					currentForm.setFlagMat(false);
				}
				for (int i = 0; i < authorities.size(); i++) {
					GruppoParametriVO gruppo = authorities.get(i);
					gruppo.setAcceso("TRUE");
				}

				for (int i = 0; i < semantica.size(); i++) {
					GruppoParametriVO gruppo = semantica.get(i);
					gruppo.setAcceso("TRUE");
				}

				for (int i = 0; i < materiali.size(); i++) {
					GruppoParametriVO gruppo = materiali.get(i);
					gruppo.setAcceso("TRUE");
				}

				List<String> elencoSelezioni = impostaSelezioniProfilo(root, selezioni, new ArrayList<String>());
				selezioni = new String[elencoSelezioni.size()];

				for (int d = 0; d < elencoSelezioni.size(); d++) {
					selezioni[d] = elencoSelezioni.get(d);
				}
				this.controllaSelezioniFigli(root.getChildren(), selezioni);

				root.open();

				List<String> elementiCheck = new ArrayList<String>();
				getTreeChecked(root, elementiCheck);
				currentForm.setCheckItemSelez(trasformaArray(elementiCheck));

			}
			else if (provenienza.equals("SALVA")) {
		        ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
				List<String> elementiCheck = new ArrayList<String>();
				getTreeChecked(root, elementiCheck);
				currentForm.setCheckItemSelez(trasformaArray(elementiCheck));
				String nomeModello = currentForm.getModello().trim().toUpperCase();

				if (nomeModello == null || nomeModello.equals("")) {
					ActionMessages messaggio = new ActionMessages();
					messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.bibliotecario.modello.nome"));
					this.saveErrors(request,messaggio);
					return mapping.getInputForward();
				}
				if (nomeModello.equals("NUOVO") && !currentForm.getNuovoModello().equals("TRUE")) {
					currentForm.setNuovoModello("TRUE");
					currentForm.setModello("");
					return mapping.getInputForward();
				}
				if (currentForm.getNuovoModello().equals("TRUE")) {
					// Controllo che il nome del modello inserito non esiste già nel sistema
					List<ComboVO> elencoModelli = currentForm.getElencoModelli();
					for (int i = 1; i < elencoModelli.size(); i++) {
						String modelloSistema = elencoModelli.get(i).getDescrizione();
						if (modelloSistema.equals(nomeModello)) {
							ActionMessages messaggio = new ActionMessages();
							messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.bibliotecario.modello.esiste"));
							this.saveErrors(request,messaggio);
							return mapping.getInputForward();
						}
					}
				}
				currentForm.setNuovoModello("FALSE");

				this.controllaSelezioniAperti(root.getChildren(), currentForm.getCheckItemSelez());
				String[] selected = trasformaArray(elementiCheck);
				selected = impostaSelezioniPadri(root, selected);
				this.controllaSelezioniAperti(root.getChildren(), selected);
				getTreeChecked(root, elementiCheck);
				currentForm.setCheckItemSelez(selected);

				List<String> elencoAttivita = new ArrayList<String>();
				List<GruppoParametriVO> elencoAuthorities = currentForm.getElencoParAuth();
				List<GruppoParametriVO> elencoMateriali = currentForm.getElencoParMateriali();
				List<GruppoParametriVO> elencoSemantica = currentForm.getElencoParSem();
				int errore = 0;
				if (selected != null && selected.length > 0) {
					for (int i=0; i<selected.length; i++) {
						ProfilazioneTreeElementView elm = (ProfilazioneTreeElementView)root.findElementUnique(Integer.parseInt(selected[i]));
						if (elm.getTooltip()!= null || !elm.getTooltip().equals("")) {
							CodiciAttivita codiciAtt = CodiciAttivita.getIstance();
							if ((elm.getTooltip().equals(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017) || elm.getTooltip().equals(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026)) && elencoAuthorities != null && elencoAuthorities.size() == 0 && !currentForm.isFlagAuth()) {
								errore = 1;
								break;
							}
							else if ((elm.getTooltip().equals(codiciAtt.CREA_DOCUMENTO_1016) || elm.getTooltip().equals(codiciAtt.MODIFICA_1019)) && elencoMateriali != null && elencoMateriali.size() == 0 && !currentForm.isFlagMat()) {
								errore = 2;
								break;
							}
							else if(!elm.getTooltip().equals("Attività"))
								elencoAttivita.add(elm.getTooltip());
						}
					}

				}
				else {
					ActionMessages messaggio = new ActionMessages();
					messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noattivita"));
					this.saveErrors(request,messaggio);
					currentForm.setModelloOp("");
					return mapping.getInputForward();
				}
				if (errore == 1) {
					ActionMessages messaggio = new ActionMessages();
					messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparauth"));
					this.saveErrors(request,messaggio);
					currentForm.setModelloOp("");
					return mapping.getInputForward();
				}
				else if (errore == 2) {
					ActionMessages messaggio = new ActionMessages();
					messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparmat"));
					this.saveErrors(request,messaggio);
					currentForm.setCheckItemSelez(selected);
					currentForm.setModelloOp("");
					return mapping.getInputForward();
				}

				CodiciAttivita codiciAtt = CodiciAttivita.getIstance();
				ProfilazioneTreeElementView elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017);
				boolean creaAuth = false;
				if (elm != null && elm.isChecked())
					creaAuth = true;
				elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);
				boolean modificaAuth = false;
				if (elm != null && elm.isChecked())
					modificaAuth = true;
				if(!creaAuth && !modificaAuth) {
					elencoAuthorities = new ArrayList<GruppoParametriVO>();
					elencoSemantica = new ArrayList<GruppoParametriVO>();
					currentForm.setElencoParAuth(elencoAuthorities);
					currentForm.setElencoParSem(elencoSemantica);
				}

				elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.CREA_DOCUMENTO_1016);
				boolean creaMat = false;
				if (elm != null && elm.isChecked())
					creaMat = true;
				elm = (ProfilazioneTreeElementView)root.findElementByTooltip(codiciAtt.MODIFICA_1019);
				boolean modificaMat = false;
				if (elm != null && elm.isChecked())
					modificaMat = true;
				if (!creaMat && !modificaMat) {
					elencoMateriali = new ArrayList<GruppoParametriVO>();
					currentForm.setElencoParMateriali(elencoMateriali);
				}

//				String idUtente = currentForm.getIdUtente();
//				String codiceUtenteInseritore = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale() + "";
//				boolean inserito = biblio.setModelloBibliotecario(nomeModello, nuovo, codiceUtenteInseritore, elencoAttivita, elencoAuthorities, elencoMateriali, elencoSemantica);
//				if (!inserito) {
//					ActionMessages errors = new ActionMessages();
//					errors.add("messaggioErrore", new ActionMessage("error.modello.bibliotecario.salva"));
//					this.saveErrors(request, errors);
//				}
//				else {
//					ActionMessages messaggio = new ActionMessages();
//					messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.bibliotecario.modello.salvaok"));
//					this.saveErrors(request,messaggio);
//				}
			}
			currentForm.setModelloOp("");

			return mapping.getInputForward();
		}

		public ActionForward noModello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			ProfilazioneForm currentForm = (ProfilazioneForm) form;
			currentForm.setModello("");
			currentForm.setModelloOp("");
			ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
			this.controllaSelezioniAperti(root.getChildren(), currentForm.getCheckItemSelez());
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			String[] selected = trasformaArray(elementiCheck);
			selected = impostaSelezioniPadri(root, selected);
			currentForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}

	protected void caricaParametri(ActionForm form, String codBib) throws Exception {
		//almaviva5_20140129 evolutive google3
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ProfilazioneForm currentForm = (ProfilazioneForm) form;
		List<?> elencoParametri = factory.getBiblioteca().getElencoParametri(codBib);
		List<GruppoParametriVO> authorities = null;
		List<GruppoParametriVO> materiali = null;
		List<GruppoParametriVO> semantica = null;
		if (ValidazioneDati.size(elencoParametri) == 3) {
			authorities = (List<GruppoParametriVO>)elencoParametri.get(0);
			materiali = (List<GruppoParametriVO>)elencoParametri.get(1);
			semantica = (List<GruppoParametriVO>)elencoParametri.get(2);

			for (Object o : elencoParametri)
				for (Object l : (List<?>)o)
					((GruppoParametriVO)l).setAcceso("TRUE");

		}

		currentForm.setElencoParAuth(authorities);
		currentForm.setElencoParSem(semantica);
		currentForm.setElencoParMateriali(materiali);
	}

}
