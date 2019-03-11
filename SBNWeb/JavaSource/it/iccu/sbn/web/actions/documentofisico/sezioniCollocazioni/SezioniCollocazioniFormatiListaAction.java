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
package it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.sezioniCollocazioni.SezioniCollocazioniFormatiListaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SezioniCollocazioniFormatiListaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	private static Log log = LogFactory.getLog(SezioniCollocazioniFormatiListaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.bottone.nuovoFormato", "nuova"); //per lista formati sezioni (scelForSez)
		map.put("documentofisico.bottone.modifica", "modifica"); //per lista formati sezioni (scelForSez)
		map.put("documentofisico.bottone.esamina", "esamina"); //per lista formati sezioni (scelForSez)
		//
		map.put("documentofisico.bottone.scegli", "ok");//per lista formati sezioni di biblioteca (scelForBib)
		map.put("documentofisico.bottone.indietro", "chiudi");//per lista formati sezioni di biblioteca (scelForBib)
		map.put("button.blocco", "blocco");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		SezioniCollocazioniFormatiListaForm currentForm = (SezioniCollocazioniFormatiListaForm) form;
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				} catch (Exception e) {
					errors.clear();
					errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		this.saveToken(request);
		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
//		controllo se ho già i dati in sessione;
		if(!myForm.isSessione()){
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			loadDefault(request, mapping, form);
			myForm.setSessione(true);
		}
		try{
//			if (request.getAttribute("forSez")!=null){//
//				myForm.setForSez(true);
//				myForm.setDisable(true);
//			}

//			gestione richiamo lista sezioni da lente
			if(request.getAttribute("prov") !=null && request.getAttribute("prov").equals("listaFB")) {
				myForm.setRichiamo("lista");
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("sezForGest")
					&& myForm.getRichiamo() == null){
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				return Navigation.getInstance(request).goBack(true);
			}
			if (request.getAttribute("noFormati")!=null){
				return Navigation.getInstance(request).goBack();
			}
			if(request.getAttribute("codPolo") != null &&
					request.getAttribute("codBib") != null &&
					request.getAttribute("descrBib") != null &&
					request.getAttribute("codSez") != null ) {
				myForm.setCodBib((String)request.getAttribute("codBib"));
				myForm.setCodSez((String)request.getAttribute("codSez"));
				myForm.setCodPolo((String)request.getAttribute("codPolo"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			SezioneCollocazioneVO sez = this.getSezioneDettaglio(myForm.getCodPolo(), myForm.getCodBib(),
					myForm.getCodSez(), myForm.getTicket());
			myForm.setSezione(sez);
			if (request.getAttribute("esamina") != null && request.getAttribute("esamina").equals("esamina")){
				myForm.setEsamina(true); //serve a togliere gli option button e lo scegli dalla lista
			}
			List listaFor = null;
			if(myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")){
					//tutti i formati della biblioteca tranne quelli della sezione trattata
					listaFor = this.getListaFormatiBib(myForm.getCodPolo(),myForm.getCodBib(), myForm.getCodSez(),
							myForm.getTicket(), myForm.getNRec(), form);
			}else{
				listaFor = this.getListaFormatiSezione(myForm.getCodPolo(),myForm.getCodBib(), myForm.getCodSez(),
						myForm.getTicket(), myForm.getNRec(), form);
			}
			if (listaFor == null ||  listaFor.size() < 1)  {
				if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("sezGest")){
					if (request.getAttribute("prov").equals("esamina")){
						myForm.setEsamina(true);
					}else{
						this.nuova(mapping, form, request, response);
					}
					return mapping.findForward("nuova");
				}else if(myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.inventariNotFound"));
					this.saveErrors(request, errors);
					myForm.setConferma(false);
					return Navigation.getInstance(request).goBack(true);
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.inventariNotFound"));
					this.saveErrors(request, errors);
					myForm.setConferma(false);
					return mapping.getInputForward();
				}
			}else{
				if (listaFor.size() == 1){
					myForm.setSelectedFor("0");
				}
				for (int i = 0; i < listaFor.size(); i ++){
					FormatiSezioniVO rec = (FormatiSezioniVO) listaFor.get(i);
					String descrFor [] = rec.getDescrFor().split(";");
					if (descrFor.length == 3){
						rec.setDescrFor(descrFor[2]);
					}else if (descrFor.length == 1){
						rec.setDescrFor(descrFor[0]);
					}else if (descrFor.length == 2){
						rec.setDescrFor("");
					}
				}
				myForm.setListaFormatiSezione(listaFor);
				if (myForm.getRichiamo() != null){
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Lista Formati Sezioni Biblioteca");
					navigation.setTesto("Lista Formati Sezioni Biblioteca");
					return mapping.getInputForward();
				}
				return mapping.getInputForward();
			}
		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return Navigation.getInstance(request).goBack(true);
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return Navigation.getInstance(request).goBack(true);
		}
	}

	public ActionForward nuova(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		try {
			request.setAttribute("codPolo", myForm.getCodPolo());
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("codSez", myForm.getCodSez());
			request.setAttribute("prov","nuova");
			return mapping.findForward("nuova");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return Navigation.getInstance(request).goBack(true);
		}
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		try {
			request.setAttribute("check", myForm.getSelectedFor());
			String check = myForm.getSelectedFor();
			int forsel;
			if (check != null && check.length() != 0) {
				forsel = Integer.parseInt(myForm.getSelectedFor());
				FormatiSezioniVO scelForSez = (FormatiSezioniVO) myForm.getListaFormatiSezione().get(forsel);
				request.setAttribute("codPolo", myForm.getCodPolo());
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codSez", myForm.getCodSez());
				request.setAttribute("scelForSez",scelForSez);
				request.setAttribute("prov","modifica");
				return mapping.findForward("modifica");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		try {
			request.setAttribute("check", myForm.getSelectedFor());
			String check = myForm.getSelectedFor();
			int forsel;
			if (check != null && check.length() != 0) {
				forsel = Integer.parseInt(myForm.getSelectedFor());
				FormatiSezioniVO scelForSez = (FormatiSezioniVO) myForm.getListaFormatiSezione().get(forsel);
				request.setAttribute("codPolo", myForm.getCodPolo());
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codSez", myForm.getCodSez());
				request.setAttribute("scelForSez",scelForSez);
				request.setAttribute("prov","esamina");
				return mapping.findForward("modifica");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		try {

			if (myForm.getRichiamo() != null){
				request.setAttribute("check", myForm.getSelectedFor());
				String check = myForm.getSelectedFor();
				int forsel;
				if (check != null && check.length() != 0) {
					forsel = Integer.parseInt(myForm.getSelectedFor());
					FormatiSezioniVO scelForBib = (FormatiSezioniVO) myForm.getListaFormatiSezione().get(forsel);
					request.setAttribute("codPolo", myForm.getCodPolo());
					request.setAttribute("codBib", myForm.getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("codSez", myForm.getCodSez());

					request.setAttribute("scelForBib",scelForBib);
					return Navigation.getInstance(request).goBack();
				}else {
					ActionMessages errors = new ActionMessages();
					errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
			return mapping.findForward("ok");

		} catch (Exception e) {

			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")){
				return Navigation.getInstance(request).goBack(true);
			}else{
				request.setAttribute("prov", "listaForSez");
				return Navigation.getInstance(request).goBack();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		try{

			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!myForm.isSessione()) {
				myForm.setSessione(true);
			}
			int numBlocco = myForm.getBloccoSelezionato();
			String idLista = myForm.getIdLista();
			String ticket = Navigation.getInstance(request).getUserTicket();
			if (numBlocco>1 && idLista != null) {
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
				if (bloccoVO != null) {
					for (int i = 0; i < bloccoVO.getLista().size(); i ++){
						FormatiSezioniVO rec = (FormatiSezioniVO) bloccoVO.getLista().get(i);
						String descrFor [] = rec.getDescrFor().split(";");
						if (descrFor.length == 3){
							rec.setDescrFor(descrFor[2]);
						}else if (descrFor.length == 1){
							rec.setDescrFor(descrFor[0]);
						}else if (descrFor.length == 2){
							rec.setDescrFor("");
						}
					}

					myForm.getListaFormatiSezione().addAll(bloccoVO.getLista());
					Collections.sort(myForm.getListaFormatiSezione());
				}
			}
			return mapping.getInputForward();

		}catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private List getListaFormatiSezione(String codPolo, String codBib, String codSez, String ticket, int nRec, ActionForm form)
	throws Exception {
		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaFormatiSezioni(codPolo, codBib, codSez.toUpperCase(), ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			myForm.setNoFormati(true);
			return null;
		}else{
			myForm.setNoFormati(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				myForm.setAbilitaBottoneCarBlocchi(false);
			}else{
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}
	private List getListaFormatiBib(String codPolo, String codBib, String codSez, String ticket, int nRec, ActionForm form)
	throws Exception {
		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaFormatiBib(codPolo, codBib, codSez.toUpperCase(), ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			myForm.setNoFormati(true);
			return null;
		}else{
			myForm.setNoFormati(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				myForm.setAbilitaBottoneCarBlocchi(false);
			}else{
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		SezioniCollocazioniFormatiListaForm myForm = (SezioniCollocazioniFormatiListaForm) form;
		// gestione bottoni
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_SEZIONI_DI_COLLOCAZIONE, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}

	}
	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
}
