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
package it.iccu.sbn.web.actions.documentofisico.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ScaricoInventarialeVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.ScaricoInventarialeForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
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

public class ScaricoInventarialeAction  extends RicercaInventariCollocazioniAction {

	private static Log log = LogFactory.getLog(ScaricoInventarialeAction.class);
	private CaricamentoCombo caricaCombo = new CaricamentoCombo();
	private ScaricoInventarialeForm myForm;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerInventari", "selInv");
		map.put("documentofisico.bottone.ok", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.SIFbibl", "SIFbibl");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		ScaricoInventarialeForm currentForm = (ScaricoInventarialeForm) form;
		if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {

			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ScaricoInventarialeForm myForm = (ScaricoInventarialeForm) form;
		super.unspecified(mapping, myForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try {
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();

			// controllo se ho già i dati in sessione;
			if (!myForm.isSessione()) {
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				loadDefault(request, mapping, form);

				myForm.setSessione(true);
			}
			if (myForm.getListaMotivoScarico() == null){
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				myForm.setListaMotivoScarico(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceMotivoScarico()));
				myForm.setVersoBibliotecaDescr(null);
				myForm.setPolo(null);
				myForm.setMotivoDelloScarico("");
				myForm.setNumBuonoScarico(0);
				myForm.setDataScarico("00/00/0000");
				myForm.setDataDelibera("00/00/0000");
				myForm.setTestoDelibera(null);
			}

			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				//
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			if (myForm.getMotivoDelloScarico().equals("T")){
				myForm.setTrasferimento(true);
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("sinteticaBiblioteche")){
				BibliotecaVO biblioteca = (BibliotecaVO)request.getAttribute("biblioteca");
				myForm.setPolo(biblioteca.getCod_polo());
				myForm.setVersoBibliotecaDescr(biblioteca.getNom_biblioteca());
				myForm.setVersoBiblioteca(biblioteca.getIdBiblioteca());
			}
			return mapping.getInputForward();
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (DataException de) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ de.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
//		} catch (UtenteNotAuthorizedException e) {
//			//??
//			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ScaricoInventarialeForm myForm = (ScaricoInventarialeForm) form;
		try {
			ScaricoInventarialeVO scaricoInv = new ScaricoInventarialeVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

			if (myForm.getFolder() != null && (myForm.getFolder().equals("Inventari"))){
				if (myForm.getSelezione() != null && (myForm.getSelezione().equals("F")
						|| myForm.getSelezione().equals("N"))){
					super.validaInputInventari(mapping, request, myForm);
					scaricoInv.setListaInventari(myForm.getListaInventari());
					scaricoInv.setSelezione(myForm.getSelezione());
					if (myForm.getSelezione() != null && (myForm.getSelezione().equals("F"))){
						scaricoInv.setNomeFileAppoggioInv(myForm.getNomeFileAppoggioInv());
					}
				}
			}

			if (myForm.getFolder() != null && (myForm.getFolder().equals("RangeInv"))){
				super.validaInputRangeInventari(mapping, request, myForm);
				scaricoInv.setSerie(myForm.getSerie());
				scaricoInv.setStartInventario(myForm.getStartInventario());
				scaricoInv.setEndInventario(myForm.getEndInventario());
			}

			if (myForm.getMotivoDelloScarico().trim().equals("T") ){
				if (myForm.getVersoBiblioteca() > 0){

					if (myForm.getPolo() != null){
						scaricoInv.setVersoBiblioteca(String.valueOf(myForm.getVersoBiblioteca()));
						scaricoInv.setPolo(myForm.getPolo().trim());
						scaricoInv.setVersoBibliotecaDescr(myForm.getVersoBibliotecaDescr());
					}else{
						scaricoInv.setPolo(null);
					}
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.codBibArrivoObbligatorio"));
					this.saveErrors(request, errors);
					resetToken(request);
					request.setAttribute("currentForm", myForm);
					return mapping.getInputForward();
				}
			}else{
				scaricoInv.setVersoBiblioteca(null);
				scaricoInv.setPolo("");
				scaricoInv.setVersoBibliotecaDescr("");
			}


			if (myForm.getMotivoDelloScarico() == null || (myForm.getMotivoDelloScarico()!=null && myForm.getMotivoDelloScarico().trim().length()==0)){
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("error.documentofisico.motivoDelloScaricoObbligatorio"));
				this.saveErrors(request, errors);
				resetToken(request);
				request.setAttribute("currentForm", myForm);
				return mapping.getInputForward();
			}
			if (myForm.getMotivoDelloScarico() !=null &&  myForm.getMotivoDelloScarico().length()!=0)	{
				if (myForm.getMotivoDelloScarico().length()>1)	{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.motivoDelloScaricoEccedente"));
					this.saveErrors(request, errors);
					resetToken(request);
					request.setAttribute("currentForm", myForm);
					return mapping.getInputForward();
				}
				scaricoInv.setMotivoDelloScarico(myForm.getMotivoDelloScarico().trim());
				scaricoInv.setDescrMotivoDelloScarico("");
			}
			if (!ValidazioneDati.strIsNull(String.valueOf(myForm.getNumBuonoScarico()))) {
				if (ValidazioneDati.strIsNumeric(String.valueOf(myForm.getNumBuonoScarico()))){
					if (String.valueOf(myForm.getNumBuonoScarico()).length()>9) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("error.documentofisico.numeroBuonoScaricoEccedente"));
						this.saveErrors(request, errors);
						resetToken(request);
						request.setAttribute("currentForm", myForm);
						return mapping.getInputForward();
					}else{
						scaricoInv.setNumBuonoScarico(myForm.getNumBuonoScarico());
					}
				}else{
					throw new ValidationException("numeroBuonoScaricoNumerico", ValidationException.errore);
				}
			}
			if (myForm.getDataScarico()!=null && myForm.getDataScarico().length()!=0) {
				int codRitorno = ValidazioneDati.validaData(myForm.getDataScarico());
				if (codRitorno != ValidazioneDati.DATA_OK){
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.dataScaricoErrata"));
					this.saveErrors(request, errors);
					resetToken(request);
					request.setAttribute("currentForm", myForm);
					return mapping.getInputForward();
				}
				if (myForm.getDataScarico().equals("00/00/0000")){
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.dataScaricoObbligatoria"));
					this.saveErrors(request, errors);
					resetToken(request);
					request.setAttribute("currentForm", myForm);
					return mapping.getInputForward();
				}
				if (myForm.getMotivoDelloScarico().trim().equals("T")){
					if (myForm.getVersoBiblioteca() == 0){
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("error.documentofisico.motivoScaricoTScegliereBiblioDallaLista"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
				scaricoInv.setDataScarico(myForm.getDataScarico());
			}

			if (myForm.getDataDelibera()!=null && myForm.getDataDelibera().length()!=0) {
				int codRitorno = ValidazioneDati.validaData(myForm.getDataDelibera());
				if (codRitorno != ValidazioneDati.DATA_OK){
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.dataDelibera"));
					this.saveErrors(request, errors);
					resetToken(request);
					request.setAttribute("currentForm", myForm);
					return mapping.getInputForward();
				}
				scaricoInv.setDataDelibera(myForm.getDataDelibera());
			}else{
				scaricoInv.setDataDelibera("");
			}
			if (myForm.getTestoDelibera() !=null &&  myForm.getTestoDelibera().length()!=0)	{
				if (myForm.getTestoDelibera().length()>50)	{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.testoDeliberaEccedente"));
					this.saveErrors(request, errors);
					resetToken(request);
					request.setAttribute("currentForm", myForm);
					return mapping.getInputForward();
				}
				scaricoInv.setTestoDelibera(myForm.getTestoDelibera());
			}else{
				scaricoInv.setTestoDelibera("");
			}
			scaricoInv.setTipoOperazione(myForm.getTipoOperazione());
			//criteri spostamento
			scaricoInv.setCodPolo(myForm.getCodPolo());
			scaricoInv.setCodBib(myForm.getCodBib());


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			// INIZIO PRENOTAZIONE ALLINEAMENTO
			//
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			scaricoInv.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			log.info("download path: " + downloadPath);
			String downloadLinkPath = "/";
			scaricoInv.setDownloadPath(downloadPath);
			scaricoInv.setDownloadLinkPath(downloadLinkPath);
			scaricoInv.setTicket(Navigation.getInstance(request).getUserTicket());
			scaricoInv.setUser(Navigation.getInstance(request).getUtente().getUserId());
			scaricoInv.setCodAttivita(CodiciAttivita.getIstance().GDF_SCARICO_INVENTARIALE);
			String s = factory.getElaborazioniDifferite().scaricoInventariale(scaricoInv);

			if (s == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotScaricoInventarialeFallita"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotScaricoInventarialeOk", s.toString()));
			this.saveErrors(request, errors);
			resetToken(request);
			myForm.setDisable(true);
			return mapping.getInputForward();


		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ScaricoInventarialeForm myForm = (ScaricoInventarialeForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_SCARICO_INVENTARIALE, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward SIFbibl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			request.setAttribute("scaricoInventariale", "scaricoInventariale");
			return mapping.findForward("SIFbibl");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		// TODO Auto-generated method stub
		return false;
	}
	private BibliotecaVO getBiblioteca(String codPolo, String codBib/*, String ticket*/) throws Exception {
		BibliotecaVO biblioteca = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		biblioteca = factory.getSistema().getBiblioteca(codPolo, codBib);
		return biblioteca;
	}

}
