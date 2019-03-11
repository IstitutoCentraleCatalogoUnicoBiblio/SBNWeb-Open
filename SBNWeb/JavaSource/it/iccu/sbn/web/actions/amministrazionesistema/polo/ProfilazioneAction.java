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
package it.iccu.sbn.web.actions.amministrazionesistema.polo;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.amministrazionesistema.ProfilazioneTreeElementView;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.custom.amministrazione.OrderedTreeElement;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.polo.ProfilazioneForm;
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
import org.apache.struts.actions.LookupDispatchAction;

public final class ProfilazioneAction extends LookupDispatchAction {

    private static Logger log = Logger.getLogger(ProfilazioneAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.polo.salva", 				"salva");
		map.put("profilo.polo.reset", 				"annulla");
		map.put("profilo.polo.selezionaTutti", 		"SelTutti");
		map.put("profilo.polo.deSelezionaTutti", 	"DeSelTutti");
		map.put("profilo.polo.button.auth",			"authorities");
		map.put("profilo.polo.button.mat",			"materiali");
		map.put("profilo.polo.button.salva.ok",		"salva");
		map.put("profilo.polo.button.salva.no",		"nosalva");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm currentForm = (ProfilazioneForm) form;

		Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().PROFILAZIONE_POLO);
        }
        catch (UtenteNotAuthorizedException e) {
        	log.error("", e);
            LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));
            return mapping.findForward("blank");
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
			elencoAttivita = factory.getPolo().getElencoAttivita("ticket");
			String[] selezioni = factory.getPolo().getElencoAttivitaProfilo();


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
//					myForm.setFlagAuth(true);
//				if (attivita.equals(codiciAtt.CREA_DOCUMENTO_1016) || attivita.equals(codiciAtt.MODIFICA_1019))
//					myForm.setFlagMat(true);
			}
			this.controllaSelezioniFigli(root.getChildren(), selezioni);

			root.open();
		}

		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(root, elementiCheck);
		currentForm.setCheckItemSelez(trasformaArray(elementiCheck));
		String codicePolo = Navigation.getInstance(request).getUtente().getCodPolo();
		currentForm.setCodicePolo(codicePolo);

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

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneForm currentForm = (ProfilazioneForm) form;

		ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
		String[] elencoSelezioni = null;
		if (currentForm.getCheckBackup() != null)
			elencoSelezioni = currentForm.getCheckBackup();
		else
			elencoSelezioni = currentForm.getCheckItemSelez();
		this.controllaSelezioniAperti(root.getChildren(), elencoSelezioni);
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

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if (currentForm.getConferma() == null || !currentForm.getConferma().equals("TRUE")) {
			List<String> controllo = factory.getPolo().controllaAttivita(elencoAttivita);
			if (controllo.size() > 0) {
				currentForm.setConferma("TRUE");
				ActionMessages messaggio = new ActionMessages();
				messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.attivitasub"));
				this.saveErrors(request,messaggio);
				this.impostaNodiRossi(root, controllo);
				currentForm.setRootBackup((ProfilazioneTreeElementView)root.clone());
//				this.impostaIntoccabili(root);
				currentForm.setProfilazioneTreeElementView(root);
				currentForm.setCheckItemSelez(selected);
				currentForm.setCheckBackup(selected);
				return mapping.getInputForward();
			}
		}

		String codicePolo = Navigation.getInstance(request).getUtente().getCodPolo();
		String codiceUtente = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale() + "";
		boolean inserito = factory.getPolo().setProfiloPolo(codicePolo, codiceUtente, elencoAttivita, elencoAuthorities, elencoMateriali, elencoSemantica);
		if (!inserito) {
			ActionMessages errors = new ActionMessages();
			errors.add("messaggioErrore", new ActionMessage("error.profilo.polo.salva"));
			this.saveErrors(request, errors);
		}
		else {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.salvaok"));
			this.saveErrors(request,messaggio);
		}
		if (currentForm.getRootBackup() != null && !currentForm.getRootBackup().getKey().equals("")) {
			root = (ProfilazioneTreeElementView)currentForm.getRootBackup().clone();
			currentForm.setRootBackup(new ProfilazioneTreeElementView());
		}
		this.togliNodiRossi(root);
		currentForm.setProfilazioneTreeElementView(root);
		currentForm.setConferma("FALSE");
		currentForm.setCheckBackup(null);
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm myForm = (ProfilazioneForm) form;
		myForm.setCheckItemSelez(null);
		myForm.setElencoParAuth(new ArrayList<GruppoParametriVO>());
		myForm.setElencoParMateriali(new ArrayList<GruppoParametriVO>());
		myForm.setElencoParSem(new ArrayList<GruppoParametriVO>());
		myForm.setProfilazioneTreeElementView(new ProfilazioneTreeElementView());
		myForm.setRootBackup(new ProfilazioneTreeElementView());
		myForm.setFlagAuth(false);
		myForm.setFlagMat(false);
		ActionMessages messaggio = new ActionMessages();
		messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.annulla"));
		this.saveErrors(request,messaggio);
//		form = new ProfilazioneForm();
		return unspecified(mapping, form, request, response);
//		NavigationForward forward = new NavigationForward(DirectionType.DEFAULT);
//		forward.forceFormInstance(new ProfilazioneForm());
//		return mapping.findForward("profilo");
//		return null;
	}

	public ActionForward nosalva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm myForm = (ProfilazioneForm) form;
		myForm.setConferma("FALSE");
		ProfilazioneTreeElementView root = (ProfilazioneTreeElementView)myForm.getRootBackup().clone();
//		this.controllaSelezioniVisibili(root.getChildren(), myForm.getCheckItemSelez());
		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(root, elementiCheck);
		String[] selected = trasformaArray(elementiCheck);
		selected = impostaSelezioniPadri(root, selected);
		myForm.setCheckItemSelez(selected);
		myForm.setProfilazioneTreeElementView(root);
		myForm.setCheckBackup(null);
//		this.togliNodiRossi(root);
//		myForm.setProfilazioneTreeElementView(root);

		return mapping.getInputForward();
	}

	public ActionForward authorities(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm myForm = (ProfilazioneForm) form;

		ProfilazioneTreeElementView root = myForm.getProfilazioneTreeElementView();
		this.controllaSelezioniAperti(root.getChildren(), myForm.getCheckItemSelez());
		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(myForm.getProfilazioneTreeElementView(), elementiCheck);
		String[] selected = trasformaArray(elementiCheck);
		ProfilazioneTreeElementView albero = myForm.getProfilazioneTreeElementView();
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
			myForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}
		request.setAttribute("checked", selected);
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY, myForm.getElencoParAuth());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, myForm.getElencoParSem());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, myForm.getElencoParMateriali());
		request.setAttribute("flagMat", myForm.isFlagMat());
		return mapping.findForward("authorities");
	}

	public ActionForward materiali(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProfilazioneForm myForm = (ProfilazioneForm) form;

		ProfilazioneTreeElementView root = myForm.getProfilazioneTreeElementView();
		this.controllaSelezioniAperti(root.getChildren(), myForm.getCheckItemSelez());
		List<String> elementiCheck = new ArrayList<String>();
		getTreeChecked(myForm.getProfilazioneTreeElementView(), elementiCheck);
		String[] selected = trasformaArray(elementiCheck);
		ProfilazioneTreeElementView albero = myForm.getProfilazioneTreeElementView();
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
			myForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}
		request.setAttribute("checked", selected);
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, myForm.getElencoParMateriali());
		request.setAttribute("flagAuth", myForm.isFlagAuth());
		return mapping.findForward("materiali");
	}

	public ActionForward SelTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneForm myForm = (ProfilazioneForm) form;
		List<String> selezionati = new ArrayList<String>();
		impostaSelezioni(myForm.getProfilazioneTreeElementView(), selezionati);
		String[] selected = new String[selezionati.size()];
		for (int i = 0; i<selezionati.size(); i++) {
			selected[i] = selezionati.get(i);
		}
		//myForm.setCheckItemSelez(impostaSelezioniPadri(myForm.getProfilazioneTreeElementView(), selected));
		myForm.setCheckItemSelez(selected);
		return mapping.getInputForward();
	}

	public ActionForward DeSelTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneForm myForm = (ProfilazioneForm) form;
		impostaDeSelezioni(myForm.getProfilazioneTreeElementView());
		myForm.setCheckItemSelez(null);
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
//
//			}
//			if (nodo.hasChildren())
//				controllaSelezioniVisibili(nodo.getChildren(), input);
//		}
//	}

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

}
