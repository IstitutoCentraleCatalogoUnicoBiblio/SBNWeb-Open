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
//		ACTION
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.documentofisico.possessori;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriRicercaForm;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriSinteticaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriRicercaAction extends LookupDispatchAction implements SbnAttivitaChecker  {

	private static Log log = LogFactory.getLog(PossessoriRicercaAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca", "cercaPoss");
		map.put("button.creaPoss", "creaPoss");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PossessoriRicercaForm possRic = (PossessoriRicercaForm) form;

		if(Navigation.getInstance(request).isFromBar() ) {
			if (possRic.getRicercaPoss().getTipoRicerca() == null || possRic.getRicercaPoss().getTipoRicerca().trim().equals("")){
				possRic.getRicercaPoss().setTipoRicerca("inizio");
			}
			return mapping.getInputForward();
		}
		if(!possRic.isSessione()){
			caricaComboGenerAutore(possRic);
//			Impostazione di inizializzazione jsp
			possRic.getRicercaPoss().setTipoRicerca("inizio");
			possRic.getRicercaPoss().setNRec(10);
			if (possRic.getRicercaPoss().getFormaAutore() == null || possRic.getRicercaPoss().getFormaAutore().equals("")) {
				possRic.getRicercaPoss().setFormaAutore("tutti");
			}
			if (possRic.getTipoAutore() == null) {
				possRic.setTipoAutore("tuttiNomi");
			}
			possRic.getRicercaPoss().setSoloBib(false);
			possRic.setCreaPoss("NO");
			possRic.setSessione(true);
			possRic.getRicercaPoss().setNome("");
			possRic.getRicercaPoss().setPid("");
		}

		if (Navigation.getInstance(request).isFirst() ){
			request.getSession().removeAttribute("parametriDOCFIS");
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		String parametri [] = new String [5];
		if ((String)request.getAttribute("prov")!= null && !((String)request.getAttribute("prov")).trim().equals("")){
			//sono stato chiamato dal documento fisico e quindi prendo i parametri inviati
			parametri [0] = (String)request.getAttribute("codBib");
			parametri [1] = (String)request.getAttribute("codSerie");
			parametri [2] = ((String)request.getAttribute("codInvent")).toString();
			parametri [3] = (String)request.getAttribute("descrBib");
			parametri [4] = (String)request.getAttribute("prov");
			request.getSession().setAttribute("parametriDOCFIS", parametri);
		}

		if (possRic.getRicercaPoss().getFormaAutore() == null || possRic.getRicercaPoss().getFormaAutore().equals("")) {
			possRic.getRicercaPoss().setFormaAutore("tutti");
		}
		if (possRic.getTipoAutore() == null) {
			possRic.setTipoAutore("tuttiNomi");
		}

		if (possRic.getTipoAutore().equals("tuttiNomi")) {
			possRic.getRicercaPoss().setChkTipoNomeA(false);
			possRic.getRicercaPoss().setChkTipoNomeB(false);
			possRic.getRicercaPoss().setChkTipoNomeC(false);
			possRic.getRicercaPoss().setChkTipoNomeD(false);
			possRic.getRicercaPoss().setChkTipoNomeE(false);
			possRic.getRicercaPoss().setChkTipoNomeG(false);
			possRic.getRicercaPoss().setChkTipoNomeR(false);
		} else if (possRic.getTipoAutore().equals("autorePersonale")) {
			possRic.getRicercaPoss().setChkTipoNomeA(true);
			possRic.getRicercaPoss().setChkTipoNomeB(true);
			possRic.getRicercaPoss().setChkTipoNomeC(true);
			possRic.getRicercaPoss().setChkTipoNomeD(true);
			possRic.getRicercaPoss().setChkTipoNomeE(false);
			possRic.getRicercaPoss().setChkTipoNomeG(false);
			possRic.getRicercaPoss().setChkTipoNomeR(false);
		} else if (possRic.getTipoAutore().equals("autoreCollettivo")){
			possRic.getRicercaPoss().setChkTipoNomeA(false);
			possRic.getRicercaPoss().setChkTipoNomeB(false);
			possRic.getRicercaPoss().setChkTipoNomeC(false);
			possRic.getRicercaPoss().setChkTipoNomeD(false);
			possRic.getRicercaPoss().setChkTipoNomeE(true);
			possRic.getRicercaPoss().setChkTipoNomeG(true);
			possRic.getRicercaPoss().setChkTipoNomeR(true);
		}
		if (request.getAttribute("cancellazione") != null){
			possRic.getRicercaPoss().setNome("");
			possRic.getRicercaPoss().setPid("");
		}
		ActionForward loadDefault = loadDefault(request, mapping, form);
		if (loadDefault != null)
			return loadDefault;

		return mapping.getInputForward();
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ActionMessages errors = new ActionMessages();
		log.debug("loadDefault()");
		PossessoriRicercaForm myform = (PossessoriRicercaForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
//		if (Navigation.getInstance(request).getUtente().getCodBib().equals(myform.getCodBib())){
			try {
				Boolean soloBiblioteca = Boolean.parseBoolean((String) utenteEjb.getDefault(ConstantDefault.GDF_RIC_SOLO_DI_BIBLIOTECA));
				if (!soloBiblioteca) {
					myform.getRicercaPoss().setSoloBib(soloBiblioteca);
				}
			} catch (ValidationException e) {
				errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				this.saveErrors(request, errors);
				this.saveToken(request);
				return mapping.getInputForward();
			} catch (DataException e) {
				errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				this.saveErrors(request, errors);
				this.saveToken(request);
				return Navigation.getInstance(request).goBack(true);
			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}
			return null;
//		}else{
//			//il diagnostico non è necessario
//			return null;
//		}
	}

	public ActionForward cercaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		PossessoriRicercaForm possRic = (PossessoriRicercaForm) form;
		List<?> listaPoss = null ;
		PossessoriSinteticaForm possSint = new PossessoriSinteticaForm();
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

//		String parametriDOCFIS [] = (String [])request.getAttribute("parametriDOCFIS");
//		request.setAttribute("parametriDOCFIS", parametriDOCFIS);

		try {
			possRic.getRicercaPoss().validaCanaliPrim();
			if (possRic.getRicercaPoss().getPid() != null && !possRic.getRicercaPoss().getPid().trim().equals("")){
				possRic.getRicercaPoss().setTipoRicerca("");
			}else{
				if (possRic.getRicercaPoss().getTipoRicerca() != null &&
						(!possRic.getRicercaPoss().getTipoRicerca().equals("inizio") &&
								!possRic.getRicercaPoss().getTipoRicerca().equals("intero") &&
								!possRic.getRicercaPoss().getTipoRicerca().equals("parole"))){

					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.documentofisico.ricercaPerNomeCheckObbligatorio"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			if (e.getMessage().trim().toLowerCase().startsWith("p"))
				errors.add("generico", new ActionMessage("errors.documentofisico." + e.getMessage()));
			else
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + e.getMessage()));

			this.saveErrors(request, errors);
	        return mapping.getInputForward();
		}

		try {
			possRic.getRicercaPoss().validaParametriGener();
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + e.getMessage()));
			this.saveErrors(request, errors);
	        return mapping.getInputForward();
		}

		try {
			GeneraChiave key = new GeneraChiave(possRic.getRicercaPoss().getNome(),"");
			key.calcoloClesToSearch(possRic.getRicercaPoss().getNome());
			listaPoss = this.getListaPossessori(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(), form, Navigation.getInstance(request).getUserTicket(),
					possRic.getRicercaPoss().getNRec() , possSint ,key);
		} catch (Exception e) {
			if ( listaPoss==null || (null != listaPoss && listaPoss.size() == 0)  ){
				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = Navigation.getInstance(request).getUtente();
				//request.setAttribute("", arg1)
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, utente.getCodPolo(), utente.getCodBib(), null);
					possRic.setCreaPoss("SI");
					request.setAttribute("creaPoss", "SI");
				} catch (Exception ee) {
					request.setAttribute("creaPoss", "NO");
				}

			}
			ActionMessages errors = new ActionMessages();//errors.documentoFisico.operOk
			if (possRic.getRicercaPoss().getTipoRicerca() == null || possRic.getRicercaPoss().getTipoRicerca().trim().equals("")){
				possRic.getRicercaPoss().setTipoRicerca("inizio");
			}
			if (e.getMessage().contains("errors.documentofisico"))
				errors.add("generico", new ActionMessage( e.getMessage()));
			else
				errors.add("generico", new ActionMessage("errors.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
	        return mapping.getInputForward();
		}

		//request.setAttribute("possessoriSinteticaForm", possSint);
		possSint.setNomeRicerca(possRic.getRicercaPoss().getNome());
		possSint.setProv("normale");
		request.getSession().setAttribute("possessoriSinteticaForm", possSint);
		if ( request.getSession().getAttribute("parametriDOCFIS")!= null){
			request.setAttribute("bottoneLega", "ok");
		}

		return mapping.findForward("sinteticaPossessore");

	}
	public ActionForward creaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		PossessoriRicercaForm possRic = (PossessoriRicercaForm) form;
		PossessoriDettaglioVO possDett = new PossessoriDettaglioVO();
		possDett.setNome(possRic.getRicercaPoss().getNome());
		request.setAttribute("dettaglioPoss", possDett);
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
//		String parametriDOCFIS [] = (String [])request.getAttribute("parametriDOCFIS");
//		request.setAttribute("parametriDOCFIS", parametriDOCFIS);

		return mapping.findForward("creaPossessore");
    }
	public void caricaComboGenerAutore(PossessoriRicercaForm possRic) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	possRic.getRicercaPoss().setFormaAutore("");
    	//possRic.getRicercaPoss().setListaPaese(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
    	possRic.getRicercaPoss().setListaTipiOrdinam(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceOrdinamentoAutori()));
    }
	private List<?> getListaPossessori(String codPolo, String codBib, ActionForm form ,String ticket, int nRec ,PossessoriSinteticaForm possSint,GeneraChiave key
			) throws Exception {
		PossessoriRicercaForm possRic = (PossessoriRicercaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaPossessori(codPolo, codBib, possRic.getRicercaPoss() ,ticket , nRec,key);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			possRic.setCreaPoss("SI");
			return null;
		}else{
			possRic.setCreaPoss("NO");
			possSint.setIdLista(blocco1.getIdLista());
			possSint.setTotBlocchi(blocco1.getTotBlocchi());
			possSint.setTotRighe(blocco1.getTotRighe());
			possSint.setBloccoSelezionato(blocco1.getNumBlocco());
			possSint.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				possSint.setAbilitaBottoneCarBlocchi(false);
			}else{
				possSint.setAbilitaBottoneCarBlocchi(true);
			}
			possSint.setListaPossessori(blocco1.getLista());
			return blocco1.getLista();
		}
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		PossessoriRicercaForm myForm = (PossessoriRicercaForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
