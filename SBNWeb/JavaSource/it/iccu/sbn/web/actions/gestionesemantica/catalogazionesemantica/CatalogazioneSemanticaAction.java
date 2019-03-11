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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.AnaliticaTitoloForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.AbstractFolder;
import it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.ClassiFolder;
import it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder;
import it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SoggettiFolder;
import it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.ThesauroFolder;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.VerificaOggettoPerSoggettazione;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class CatalogazioneSemanticaAction extends SinteticaLookupDispatchAction
		implements SemanticaFolder, SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(CatalogazioneSemanticaAction.class);

	private static final Class<?>[] TYPES = {
		ActionMapping.class,
		ActionForm.class,
		HttpServletRequest.class,
		HttpServletResponse.class
	};

	private SoggettiFolder folderSoggetti = new SoggettiFolder();
	private ClassiFolder folderClassi = new ClassiFolder();
	private ThesauroFolder folderThesauro = new ThesauroFolder();
	private AbstractFolder folderAbstract = new AbstractFolder();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.recupera", "recupera");
		map.put("button.aggiornaDati", "aggiornaDati");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.crea", "crea");
		map.put("button.ok", "ok");
		map.put("button.modifica", "modifica");
		map.put("button.elimina", "elimina");
		map.put("button.invioIndice", "invioInIndice");

		map.put("button.indice", "indice");
		map.put("button.cercaIndice", "indice");

		map.put("button.gestione", "gestione");
		map.put("button.vaia", "gestione");

		map.put("button.chiudi", "chiudi");
		map.put("button.conferma", "conferma");

		// pulsanti folder
		map.put("button.tag.soggetti", "soggetti");
		map.put("button.tag.classificazioni", "classificazioni");
		map.put("button.tag.thesauro", "thesauro");
		map.put("button.tag.abstracto", "abstracto");

		//almaviva5_20120507 evolutive CFI
		map.put("servizi.bottone.frecciaSu", "moveUp");
		map.put("servizi.bottone.frecciaGiu", "moveDown");

		return map;
	}

	private Map<FolderType, SemanticaFolder> getFolderMap() {
		Map<FolderType, SemanticaFolder> map = new HashMap<FolderType, SemanticaFolder>();
		folderSoggetti.setServlet(servlet);
		map.put(FolderType.FOLDER_SOGGETTI, folderSoggetti);
		folderClassi.setServlet(servlet);
		map.put(FolderType.FOLDER_CLASSI, folderClassi);
		folderThesauro.setServlet(servlet);
		map.put(FolderType.FOLDER_THESAURO, folderThesauro);
		folderAbstract.setServlet(servlet);
		map.put(FolderType.FOLDER_ABSTRACT, folderAbstract);
		return map;
	}

	private ActionForward invokeFolder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			FolderType folder, String name) throws Exception {

		if (name == null)
			return unspecified(mapping, form, request, response);

		Method method = null;
		method = getFolderMap().get(folder).getClass().getMethod(name, TYPES);
		ActionForward forward = null;

		log.debug("invoke folder semantica: " + folder.toString() + "::" + name);
		Object args[] = { mapping, form, request, response };
		forward = (ActionForward) method.invoke(getFolderMap().get(folder),	args);

		return forward;
	}

	private SoggettiDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new SoggettiDelegate(
				FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	private void loadSoggettario(ActionForm form, String ticket)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		this.loadSoggettario(currentForm, navi.getUserTicket());

		if (!currentForm.isSessione()) {
			navi.addBookmark(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA);

			//almaviva5_20090327 #2744
			String folder = request.getParameter("folderT");
			if (ValidazioneDati.isFilled(folder))
				currentForm.setFolder(FolderType.valueOf(folder) );
			else
				currentForm.setFolder(FolderType.FOLDER_SOGGETTI);

			AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
				.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);

			currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
			currentForm.getCatalogazioneSemanticaComune().setBid(datiGB.getBidPartenza() );
			currentForm.getCatalogazioneSemanticaComune().setTestoBid(datiGB.getDescPartenza() );

			TreeElementViewTitoli reticolo = datiGB.getTreeElement();
			if (reticolo != null) {

				// devo soggettare in polo anche se l'analitica è di indice
				if (datiGB.isInserimentoIndice() ) {
					datiGB.setInserimentoIndice(false);
					datiGB.setInserimentoPolo(true);
					TreeElementViewTitoli reticoloPolo = getDelegate(request).caricaReticoloTitolo(true, datiGB.getBidPartenza());
					if (reticoloPolo == null)
						return mapping.getInputForward();

					//almaviva5_20120718 #47 LIG
					//preservo il reticolo di indice
					TreeElementViewTitoli reticoloIndice = datiGB.getTreeElement();
					currentForm.getCatalogazioneSemanticaComune().setReticoloIndice(reticoloIndice);
					//
					datiGB.setTreeElement(reticoloPolo);
				}

//				String tipoMateriale = reticolo.getTipoMateriale();
//				if (ValidazioneDati.isFilled(tipoMateriale)	&& tipoMateriale.equals("U")) // musica
//					currentForm.setFolder(FolderType.FOLDER_CLASSI);
			}
			else {
				// se non ho il reticolo sto in esamina
				navi.setTesto(".gestionesemantica.catalogazionesemantica.CatalogazioneSemantica.ESAMINA.testo");
				navi.setDescrizioneX(".gestionesemantica.catalogazionesemantica.CatalogazioneSemantica.ESAMINA.descrizione");
				currentForm.setEnableSoloEsamina(true);
				currentForm.setEnableOk(false);
			}

			Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				String maxRighe = (String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI);
				currentForm.setMaxRighe(Integer.valueOf(maxRighe));
			}	catch (Exception e) {

				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.default"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "init");
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "indice");
	}

	public ActionForward aggiornaDati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "aggiornaDati");
	}


	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "invioInIndice");
	}
	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "caricaBlocco");
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		boolean gestione = (navi.getCallerForm() instanceof AnaliticaTitoloForm);
		if (!gestione)
			return navi.goBack(true);

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "chiudi");
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "crea");
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "crea");
	}


	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "elimina");
	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "gestione");
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "modifica");
	}

	public ActionForward recupera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "recupera");

	}

	// pulsanti folder

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setFolder(FolderType.FOLDER_SOGGETTI);
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "init");
	}

	public ActionForward classificazioni(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setFolder(FolderType.FOLDER_CLASSI);
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "init");
	}

	public ActionForward thesauro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setFolder(FolderType.FOLDER_THESAURO);
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "init");
	}

	public ActionForward abstracto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setFolder(FolderType.FOLDER_ABSTRACT);
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "init");
	}

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return null;
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "conferma");
	}

	private enum TipoAttivita {
		FOLDER_SOGGETTI,
		FOLDER_CLASSI,
		FOLDER_THESAURO,
		FOLDER_ABSTRACT,
		CHECK_FOLDER_INDICE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		try {
			AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getAreaDatiPassBiblioSemanticaVO();
			TreeElementViewTitoli reticolo = datiGB.getTreeElement();
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			switch (attivita) {

			case FOLDER_SOGGETTI: // test folder su tab
				return VerificaOggettoPerSoggettazione.isSoggettabile(reticolo);

			case FOLDER_CLASSI:
				reticolo = datiGB.getTreeElement();
				if (reticolo == null)
					return true;

				//solo queste nature
				return VerificaOggettoPerSoggettazione.isNaturaAmmessa(reticolo.getNatura());

			case FOLDER_THESAURO:
				return VerificaOggettoPerSoggettazione.isSoggettabile(reticolo);

			case FOLDER_ABSTRACT:
				return VerificaOggettoPerSoggettazione.isSoggettabile(reticolo);

			case CHECK_FOLDER_INDICE:
				return (!datiGB.isInserimentoIndice());
			}

		} catch (Exception e) {

			FolderType folder = currentForm.getFolder();
			log.info("invoke folder semantica: " + folder.toString() + "::checkAttivita");
			return ((SbnAttivitaChecker) getFolderMap().get(folder)).checkAttivita(request, form, idCheck);
		}

		return false;
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "moveUp");
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		return this.invokeFolder(mapping, form, request, response, currentForm
				.getFolder(), "moveDown");
	}


}
