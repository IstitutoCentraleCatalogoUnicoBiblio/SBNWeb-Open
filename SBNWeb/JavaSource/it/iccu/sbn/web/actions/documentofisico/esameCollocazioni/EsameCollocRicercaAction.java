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
package it.iccu.sbn.web.actions.documentofisico.esameCollocazioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocRicercaForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class EsameCollocRicercaAction extends RicercaInventariCollocazioniAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(EsameCollocRicercaAction.class);

//	private EsameCollocRicercaForm form;


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("documentofisico.bottone.invNoncolloc", "invNoncolloc");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.cerca", "ok");
		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.selPerInventari", "selRangeInv");
//		map.put("documentofisico.selPerInventari", "selInv");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		EsameCollocRicercaForm currentForm = (EsameCollocRicercaForm) form;
//		if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
				try {
					if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
						currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.GDF_ESA_COLL_ELEM_BLOCCHI)));

						String codOrdinamento = (String) utenteEjb.getDefault(ConstantDefault.GDF_ESA_COLL_ORDINAMENTO);
						if (!ValidazioneDati.strIsEmpty(codOrdinamento)) {
							currentForm.setTipoOrdinamento(codOrdinamento);
						}else {
							LinkableTagUtils.resetErrors(request);
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.tipoOrdinamentoDefaultNonImpostato"));

							return Navigation.getInstance(request).goBack(true);
						}
					}
				} catch (Exception e) {
					LinkableTagUtils.resetErrors(request);
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

					return mapping.getInputForward();
				}
			}
//		}

		//almaviva5_20120222 rfid
		try {
			currentForm.setRfidEnabled(Boolean.valueOf(CommonConfiguration.getProperty(Configuration.RFID_ENABLE, "false")));
		} catch (Exception e) {
			LinkableTagUtils.resetErrors(request);
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameCollocRicercaForm myForm = (EsameCollocRicercaForm) form;
		super.unspecified(mapping, myForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try{
			// controllo se ho già i dati in sessione;
			if (Navigation.getInstance(request).isFromBar() ){
				return mapping.getInputForward();
			}
			if (request.getAttribute("indietro") != null && request.getAttribute("indietro").equals("indietro")){
				return mapping.getInputForward();
			}
//			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
//			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
//			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
//			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			request.getSession().setAttribute("lineaFunzionale", "docFisEsame");
			loadDefault(request, mapping, form);
			myForm.setSessione(true);


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			myForm.setListaTipiOrdinamento(caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_COLLOCAZIONI)));
			myForm.setFolder("Collocazioni");

			return mapping.getInputForward();
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		EsameCollocRicercaForm myForm = (EsameCollocRicercaForm) form;
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		request.setAttribute("currentForm", myForm);
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			EsameCollocRicercaVO ricerca = null;
			String codSerie;
			int codInv;
			codSerie = myForm.getCodSerie();
			codInv = myForm.getCodInvent();

			// Codice serie e codice inventario ci arrivano da RFID?
			String rfid = myForm.getCodRfid();
			if (ValidazioneDati.isFilled(rfid) && ValidazioneDati.equals(myForm.getFolder(), "RangeInv") ) {
				InventarioVO inv = InventarioRFIDParser.parse(rfid);
				log.debug("lettura inventario da rfid: " + inv.getChiaveInventario() );
				codSerie = inv.getCodSerie();
				codInv = inv.getCodInvent();

				//check bib
				if (!ValidazioneDati.equals(myForm.getCodBib(), inv.getCodBib()))
					throw new ValidationException(SbnErrorTypes.SRV_ERRORE_RFID_BIBLIOTECA);

				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codSerie", codSerie);
				request.setAttribute("codInvent", codInv);

				return mapping.findForward("esameInventario");
			}

//			if (Navigation.getInstance(request).isFromBar() )
//				return mapping.getInputForward();
//			valido i dati di ricerca con la richiesta di un servizio di validazione parametri e in base alla risposta
			//attivo listaInvNoncolloc o listaInvColloc
			request.setAttribute("codBib",myForm.getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());
			request.setAttribute("codSerie", myForm.getCodSerie());
			request.setAttribute("codInvent", myForm.getCodInvent());
			if (myForm.getFolder().equals("RangeInv")){
				ricerca = new EsameCollocRicercaVO();
					return mapping.findForward("esameInventario");
			}else if (myForm.getFolder().equals("Collocazioni")){
				ricerca = new EsameCollocRicercaVO();
				if (myForm.getSezione().equals(null)){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codSezObbl"));

					return mapping.getInputForward();
				}else{
					if (myForm.getCodPoloSez()==null){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));

						return mapping.getInputForward();
					}
				}
				//almaviva2 agosto 2009
//					if (myForm.getDallaCollocazione() != null && !myForm.getDallaCollocazione().equals("")
//						&& (myForm.getDallaSpecificazione() != null && myForm.getDallaSpecificazione().equals(""))
//						&& !myForm.isEsattoColl() && !myForm.isEsattoSpec()){
//						ricerca.setALoc(myForm.getDallaCollocazione());
//						ricerca.setASpec(myForm.getDallaSpecificazione());
//					}

					  if (!myForm.isEsattoColl() && !myForm.isEsattoSpec()){
							ricerca.setALoc(myForm.getDallaCollocazione());
							ricerca.setASpec(myForm.getDallaSpecificazione());
						}

					if ((myForm.getDallaSpecificazione() != null && !myForm.getDallaSpecificazione().equals(""))){
						myForm.setEsattoColl(true);
					}
					if ((myForm.getTipoOrdinamento() != null && myForm.getTipoOrdinamento().equals(""))){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.tipoOrdinamentoObbligatorio"));

						return mapping.getInputForward();
					}

				ricerca.setCodPolo(myForm.getCodPolo());
				ricerca.setCodBib(myForm.getCodBib());
				ricerca.setCodSerie(myForm.getCodSerie());
				ricerca.setCodInvent(myForm.getCodInvent());
				if (myForm.getCodPoloSez() != null){
					ricerca.setCodPoloSez(myForm.getCodPoloSez());
				}else{
					ricerca.setCodPoloSez(myForm.getCodPolo());
				}
				if (myForm.getCodBibSez() != null){
					ricerca.setCodBibSez(myForm.getCodBibSez());
				}else{
					ricerca.setCodBibSez(myForm.getCodBib());
				}
				ricerca.setEsattoColl(myForm.isEsattoColl());
				ricerca.setEsattoSpec(myForm.isEsattoSpec());
				ricerca.setCodSez(myForm.getSezione());
				ricerca.setOrdLst(myForm.getTipoOrdinamento());
				ricerca.setCodLoc(myForm.getDallaCollocazione().trim());
				ricerca.setCodSpec(myForm.getDallaSpecificazione().trim());
				ricerca.setUltLoc("");
				ricerca.setUltSpec("");
				ricerca.setElemPerBlocchi(myForm.getElemPerBlocchi());
				request.setAttribute("paramRicerca", ricerca);
				request.setAttribute("listaColl","listaColl");
				return mapping.findForward("listaCollocazioni");
			}

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EsameCollocRicercaForm myForm = (EsameCollocRicercaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
			CodiciAttivita.getIstance().GDF_ESAME_COLLOCAZIONI, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
    		resetToken(request);
			return mapping.findForward("chiudi");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward invNoncolloc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameCollocRicercaForm currentForm = (EsameCollocRicercaForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();

			EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
//			this.impostaCriteriRicercaCollocazioni(form);
			ricerca.setCodPolo(currentForm.getCodPolo());
			ricerca.setCodBib(currentForm.getCodBib());
			ricerca.setCodSerie(currentForm.getCodSerie());
			ricerca.setCodInvent(currentForm.getCodInvent());
			ricerca.setOrdLst(((CodiceVO) currentForm.getListaTipiOrdinamento().get(0)).getDescrizione());
			ricerca.setElemPerBlocchi(currentForm.getElemPerBlocchi());
			request.setAttribute("paramRicerca", ricerca);
			request.setAttribute("tipoLista", "invNoncolloc");

			return mapping.findForward("invNoncolloc");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
//	private void loadTipiOrdinamento(ActionForm form) throws Exception {
//		EsameCollocRicercaForm myForm = (EsameCollocRicercaForm) form;
//		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//		myForm.setListaTipiOrdinamento(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_COLLOCAZIONI)));
//	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		EsameCollocRicercaForm myForm = (EsameCollocRicercaForm) form;
		try{
			//almaviva5_20120222 rfid
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.RFID))
				return myForm.isRfidEnabled();
			//
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_ESAME_COLLOCAZIONI, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
