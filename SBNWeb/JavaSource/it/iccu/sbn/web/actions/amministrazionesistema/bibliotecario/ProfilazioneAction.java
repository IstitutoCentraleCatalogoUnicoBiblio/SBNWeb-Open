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
package it.iccu.sbn.web.actions.amministrazionesistema.bibliotecario;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.ProfilazioneTreeElementView;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.custom.amministrazione.OrderedTreeElement;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario.ProfilazioneForm;
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

public final class ProfilazioneAction extends ProfilazioneBaseAction {

    private static Logger log = Logger.getLogger(ProfilazioneAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.polo.salva", 							"salva");
		map.put("profilo.polo.annulla",							"annulla");
		map.put("profilo.polo.selezionaTutti", 					"tutti");
		map.put("profilo.polo.deSelezionaTutti", 				"nessuno");
		map.put("profilo.polo.button.auth",						"authorities");
		map.put("profilo.polo.button.mat",						"materiali");
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
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ABILITAZIONI_UTENTE);
            currentForm.setAbilitatoWrite("FALSE");
        }
        catch (UtenteNotAuthorizedException e) {

            LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));

            return mapping.findForward("blank");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_UTENTE);
            currentForm.setAbilitatoWrite("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoWrite("FALSE");
        }

        ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
		List<GruppoParametriVO> parametriAut = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY);
		if (parametriAut != null)
			currentForm.setElencoParAuth(parametriAut);
		List<GruppoParametriVO> parametriSem = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA);
		if (parametriSem != null)
			currentForm.setElencoParSem(parametriSem);
		List<GruppoParametriVO> parametriMat = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI);
		if (parametriMat != null)
			currentForm.setElencoParMateriali(parametriMat);
/*
		if (request.getAttribute("flagAuth") != null)
			currentForm.setFlagAuth((Boolean)request.getAttribute("flagAuth"));
		if (request.getAttribute("flagMat") != null)
			currentForm.setFlagMat((Boolean)request.getAttribute("flagMat"));
*/
		if (request.getAttribute(NavigazioneProfilazione.AUTHORITY_OK) != null)
			currentForm.setFlagAuth(true);
		if (request.getAttribute(NavigazioneProfilazione.METERIALI_OK) != null)
			currentForm.setFlagMat(true);

		if (request.getAttribute(NavigazioneProfilazione.ID_UTENTE) != null)
			currentForm.setIdUtente((String)request.getAttribute(NavigazioneProfilazione.ID_UTENTE));
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
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idUtente = currentForm.getIdUtente();
			List<String> datiBibliotecario = factory.getBibliotecario().getNomeBibliotecario(idUtente);
			if (ValidazioneDati.size(datiBibliotecario) == 4) {
				currentForm.setUtente(datiBibliotecario.get(0));
				currentForm.setUsernam(datiBibliotecario.get(1));
				currentForm.setBibli(datiBibliotecario.get(2));
				//almaviva5_20111207 #4722
				currentForm.setCodBiblioteca(datiBibliotecario.get(3));
			}
			else {

				LinkableTagUtils.addError(request, new ActionMessage("error.profilo.bibliotecario.recuperodati"));

				mapping.findForward("sintetica");
			}
			OrderedTreeElement elencoAttivita = factory.getBibliotecario().getElencoAttivita(idUtente);
			String[] selezioni = factory.getBibliotecario().getElencoAttivitaProfilo(idUtente);

			//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
			boolean isNew = !ValidazioneDati.isFilled(selezioni) && !currentForm.isFlagAuth() && !currentForm.isFlagMat();
			currentForm.setFlagAuth(!isNew);
			currentForm.setFlagMat(!isNew);

			root.setKey("Attività");
			root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_TEXT);
			root.setTooltip("Attività");
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
//				if (attivita.equals(codiciAtt.CREA_ELEMENTO_DI_AUTHORITY_1017) || attivita.equals(codiciAtt.MODIFICA_ELEMENTO_DI_AUTHORITY_1026)) {
//					myForm.setFlagAuth(true);
//				}
//				if (attivita.equals(codiciAtt.CREA_DOCUMENTO_1016) || attivita.equals(codiciAtt.MODIFICA_1019)) {
//					myForm.setFlagMat(true);
//				}
			}

			this.controllaSelezioniFigli(root.getChildren(), selezioni);

			root.open();

			//almaviva5_20140129 evolutive google3
			caricaParametri(currentForm, idUtente);
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

		ProfilazioneForm currentForm = (ProfilazioneForm) form;

		ProfilazioneTreeElementView root = currentForm.getProfilazioneTreeElementView();
		this.controllaSelezioniAperti(root.getChildren(), currentForm.getCheckItemSelez());
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
		if (ValidazioneDati.isFilled(selected) ) {

			for (int i = 0; i < selected.length; i++) {
				ProfilazioneTreeElementView elm = (ProfilazioneTreeElementView)root.findElementUnique(Integer.parseInt(selected[i]));
				String cdAttivita = elm.getTooltip();
				if (ValidazioneDati.isFilled(cdAttivita)) {
			/*
					CodiciAttivita attivita = CodiciAttivita.getIstance();
					if ((cdAttivita.equals(attivita.CREA_ELEMENTO_DI_AUTHORITY_1017) || cdAttivita.equals(attivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026)) && elencoAuthorities != null && elencoAuthorities.size() == 0 && !currentForm.isFlagAuth()) {
						errore = 1;
						break;
					}
					else if ((cdAttivita.equals(attivita.CREA_DOCUMENTO_1016) || cdAttivita.equals(attivita.MODIFICA_1019)) && elencoMateriali != null && elencoMateriali.size() == 0 && !currentForm.isFlagMat()) {
						errore = 2;
						break;
					}
					else
			*/
					if (!cdAttivita.equals("Attività"))
						elencoAttivita.add(cdAttivita);
				}
			}

			//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
			if (!currentForm.isFlagAuth()) {
				errore++;
				LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noparauth"));
			}
			if (!currentForm.isFlagMat()) {
				errore++;
				LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noparmat"));
			}

		}
		else {
			errore++;
			LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noattivita"));
		}

		if (errore > 0) {
			currentForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}

		//almaviva5_20111207 #4722 check livelli autorita su biblioteca
		ActionForward check = checkLivAutBiblioteca(mapping, currentForm.getCodBiblioteca(), elencoAuthorities, request, response);
		if (check != null)
			return check;
		check = checkLivAutBiblioteca(mapping, currentForm.getCodBiblioteca(), elencoMateriali, request, response);
		if (check != null)
			return check;

		//almaviva5_20120703 #5032
		check = checkParSemBiblioteca(mapping, currentForm.getCodBiblioteca(), elencoSemantica, request, response);
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
*/
		String idUtente = currentForm.getIdUtente();
		String codiceUtenteInseritore = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale() + "";
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		boolean inserito = factory.getBibliotecario().setProfiloBibliotecario(idUtente, codiceUtenteInseritore, elencoAttivita, elencoAuthorities, elencoMateriali, elencoSemantica);
		if (!inserito) {

			LinkableTagUtils.addError(request, new ActionMessage("error.profilo.polo.salva"));

		}
		else {

			LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.parametri.info.salvaok"));

		}

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
		myForm.setFlagAuth(false);
		myForm.setFlagMat(false);
//
//		LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.annulla"));
//
//		return unspecified(mapping, form, request, response);
		if (myForm.getProvenienza()!= null && myForm.getProvenienza().equals("nuovo")) {
			request.setAttribute(NavigazioneProfilazione.ID_UTENTE, myForm.getIdUtente());
			request.setAttribute("provenienza", "ricerca");
			return mapping.findForward("modifica");
		}
		if (myForm.getProvenienza() == null) {
			return mapping.findForward("ricerca");
		}
		return mapping.findForward("sintetica");
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
/*
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

			LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noauth"));

			currentForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}
*/
		request.setAttribute("checked", selected);
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY, currentForm.getElencoParAuth());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, currentForm.getElencoParSem());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, currentForm.getElencoParMateriali());
		request.setAttribute("flagMat", currentForm.isFlagMat());
		request.setAttribute(NavigazioneProfilazione.ID_UTENTE, currentForm.getIdUtente());
		request.setAttribute(NavigazioneProfilazione.NOME_UTENTE, currentForm.getUtente());
		request.setAttribute(NavigazioneProfilazione.USERNAME, currentForm.getUsernam());
		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, currentForm.getBibli());
		//almaviva5_20111207 #4722
		request.setAttribute(NavigazioneProfilazione.COD_BIBLIOTECA, currentForm.getCodBiblioteca());
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
/*
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

			LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.nomat"));

			currentForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}
*/
		request.setAttribute("checked", selected);
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, currentForm.getElencoParMateriali());
		request.setAttribute("flagAuth", currentForm.isFlagAuth());
		request.setAttribute(NavigazioneProfilazione.ID_UTENTE, currentForm.getIdUtente());
		request.setAttribute(NavigazioneProfilazione.NOME_UTENTE, currentForm.getUtente());
		request.setAttribute(NavigazioneProfilazione.USERNAME, currentForm.getUsernam());
		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, currentForm.getBibli());
		//almaviva5_20111207 #4722
		request.setAttribute(NavigazioneProfilazione.COD_BIBLIOTECA, currentForm.getCodBiblioteca());
		return mapping.findForward("materiali");
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
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

	public ActionForward nessuno(ActionMapping mapping, ActionForm form,
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

		public ActionForward salvaModello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			ProfilazioneForm myForm = (ProfilazioneForm) form;
			myForm.setModelloOp("SALVA");

			List<ComboVO> elencoModelli = new ArrayList<ComboVO>();

			ComboVO combo = new ComboVO();
			combo.setCodice("NUOVO");
			combo.setDescrizione("<NUOVO MODELLO>");
			elencoModelli.add(combo);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<String> elencoDB = factory.getBibliotecario().getElencoModelli();
			if (elencoDB.size() > 0)
				for (int i = 0; i < elencoDB.size(); i++) {
					combo = new ComboVO();
					combo.setCodice(elencoDB.get(i));
					combo.setDescrizione(elencoDB.get(i));
					elencoModelli.add(combo);
				}
			myForm.setElencoModelli(elencoModelli);
			myForm.setModello("NUOVO");
			myForm.setNuovoModello("FALSE");
	        ProfilazioneTreeElementView root = myForm.getProfilazioneTreeElementView();
//			this.controllaSelezioniAperti(root.getChildren(), myForm.getCheckItemSelez());
//			List<String> elementiCheck = new ArrayList<String>();
//			getTreeChecked(root, elementiCheck);
//			myForm.setCheckItemSelez(trasformaArray(elementiCheck));
			this.controllaSelezioniAperti(root.getChildren(), myForm.getCheckItemSelez());
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			String[] selected = trasformaArray(elementiCheck);
			selected = impostaSelezioniPadri(root, selected);
			myForm.setCheckItemSelez(selected);

			return mapping.getInputForward();
		}

		public ActionForward caricaModello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			ProfilazioneForm myForm = (ProfilazioneForm) form;
			myForm.setModelloOp("CARICA");
			myForm.setModello("");
	        ProfilazioneTreeElementView root = myForm.getProfilazioneTreeElementView();
//			List<String> elementiCheck = new ArrayList<String>();
//			getTreeChecked(root, elementiCheck);
//			myForm.setCheckItemSelez(trasformaArray(elementiCheck));
			this.controllaSelezioniAperti(root.getChildren(), myForm.getCheckItemSelez());
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			String[] selected = trasformaArray(elementiCheck);
			selected = impostaSelezioniPadri(root, selected);
			myForm.setCheckItemSelez(selected);

			List<ComboVO> elencoModelli = new ArrayList<ComboVO>();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<String> elencoDB = factory.getBibliotecario().getElencoModelli();
			if (elencoDB.size() > 0)
				for (int i = 0; i < elencoDB.size(); i++) {
					ComboVO combo = new ComboVO();
					combo.setCodice(elencoDB.get(i));
					combo.setDescrizione(elencoDB.get(i));
					elencoModelli.add(combo);
				}
			else {

				LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.modello.nomodelli"));

				myForm.setModelloOp("");
				return mapping.getInputForward();
			}
			myForm.setElencoModelli(elencoModelli);

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
				String[] selezioni = factory.getBibliotecario().getElencoAttivitaProfiloModello(currentForm.getModello());

				//almaviva5_20111117 check abilitazioni mancanti
				List<String> missing = new ArrayList<String>();
				for (int i = 0; i < selezioni.length; i++)
					if (root.findElementByTooltip(selezioni[i]) == null ) {
						log.warn("attività modello mancante: " + selezioni[i]);
						missing.add(selezioni[i]);
					}

				String formatMsg = ValidazioneDati.formatValueList(missing, ", ");
				if (ValidazioneDati.isFilled(formatMsg)) {
					LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.modello.incompatibile", formatMsg));
					return mapping.getInputForward();
				}

				//Carico i parametri (solo per i modelli vengono caricati prima):
				List<?> elencoParametri = factory.getBibliotecario().getElencoParametriModello(currentForm.getModello());

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
				boolean nuovo = false;
				if (!ValidazioneDati.isFilled(nomeModello)) {
					LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.modello.nome"));
					return mapping.getInputForward();
				}
				if (nomeModello.equals("NUOVO") && !currentForm.getNuovoModello().equals("TRUE")) {
					currentForm.setNuovoModello("TRUE");
					currentForm.setModello("");
					return mapping.getInputForward();
				}
				if (currentForm.getNuovoModello().equals("TRUE")) {
					nuovo = true;
					// Controllo che il nome del modello inserito non esiste già nel sistema
					List<ComboVO> elencoModelli = currentForm.getElencoModelli();
					for (int i = 1; i < elencoModelli.size(); i++) {
						String modelloSistema = elencoModelli.get(i).getDescrizione();
						if (modelloSistema.equals(nomeModello)) {
							LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.modello.esiste"));
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
					LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noattivita"));
					currentForm.setModelloOp("");
					return mapping.getInputForward();
				}
				if (errore == 1) {
					LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noparauth"));
					currentForm.setModelloOp("");
					return mapping.getInputForward();
				}
				else if (errore == 2) {
					LinkableTagUtils.addError(request, new ActionMessage("profilo.polo.parametri.info.noparmat"));
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
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				String codiceUtenteInseritore = Navigation.getInstance(request).getUtente().getIdUtenteProfessionale() + "";
				boolean inserito = factory.getBibliotecario().setModelloBibliotecario(nomeModello, nuovo, codiceUtenteInseritore, elencoAttivita, elencoAuthorities, elencoMateriali, elencoSemantica);
				if (!inserito)
					LinkableTagUtils.addError(request, new ActionMessage("error.modello.bibliotecario.salva"));
				else
					LinkableTagUtils.addError(request, new ActionMessage("profilo.bibliotecario.modello.salvaok"));
			}
			currentForm.setModelloOp("");

			return mapping.getInputForward();
		}

		public ActionForward noModello(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			ProfilazioneForm myForm = (ProfilazioneForm) form;
			myForm.setModello("");
			myForm.setModelloOp("");
			ProfilazioneTreeElementView root = myForm.getProfilazioneTreeElementView();
//			this.controllaSelezioniAperti(root.getChildren(), myForm.getCheckItemSelez());
			List<String> elementiCheck = new ArrayList<String>();
			getTreeChecked(root, elementiCheck);
			String[] selected = trasformaArray(elementiCheck);
			selected = impostaSelezioniPadri(root, selected);
			myForm.setCheckItemSelez(selected);
			return mapping.getInputForward();
		}

	protected void caricaParametri(ActionForm form, String idUtente) throws Exception {
		//almaviva5_20140129 evolutive google3
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ProfilazioneForm currentForm = (ProfilazioneForm) form;
		List<?> elencoParametri = factory.getBibliotecario().getElencoParametri(idUtente);
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
