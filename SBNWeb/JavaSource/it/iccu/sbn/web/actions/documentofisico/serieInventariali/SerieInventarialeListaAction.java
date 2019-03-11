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
package it.iccu.sbn.web.actions.documentofisico.serieInventariali;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.serieInventariali.SerieInventarialeListaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SerieInventarialeListaAction extends SinteticaLookupDispatchAction  implements SbnAttivitaChecker{

	private static Log log = LogFactory.getLog(SerieInventarialeListaAction.class);

//	private SerieInventarialeListaForm form;

	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.bottone.nuova", "nuova");
		map.put("documentofisico.bottone.modifica", "modifica");
		map.put("documentofisico.bottone.esamina", "esamina");
		map.put("documentofisico.bottone.indietro", "chiudi");
//		map.put("documentofisico.bottone.caricaBlocco", "caricaBlocco");
		map.put("button.blocco", "blocco");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		SerieInventarialeListaForm currentForm = (SerieInventarialeListaForm) form;
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

		HttpSession httpSession = request.getSession();
		this.saveToken(request);
		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
//		this.loadTipiOrdinamento(form);
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
		if (request.getAttribute("codBibDaLista") != null) {
			BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
			request.setAttribute("codBib", bib.getCod_bib());
			request.setAttribute("descrBib", bib.getNom_biblioteca());
		}
		if(request.getAttribute("codBib") != null && request.getAttribute("descrBib")!= null) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			myForm.setCodBib((String)request.getAttribute("codBib"));
			myForm.setDescrBib((String)request.getAttribute("descrBib"));
		}
		try{
			List listaSerie = this.getListaSerie(myForm.getCodPolo(), myForm.getCodBib(),
					myForm.getTicket(), myForm.getNRec(), form);
			if (listaSerie == null ||  listaSerie.size() <1)  {
				if (!myForm.getCodBib().equals(Navigation.getInstance(request).getUtente().getCodBib())){
					myForm.setConferma(false);
					myForm.setListaSerie(null);
					myForm.setTotRighe(0);
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.listaSerieVuota"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_SERIE_INVENTARIALE, myForm.getCodPolo(), myForm.getCodBib(), null);
				myForm.setNoSerie(true);
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("prov", "nuova");
				if (!this.checkAttivita(request, myForm, "df")){
					return mapping.getInputForward();
				}else{
					return mapping.findForward("nuova");
				}
			}else{
//				impostazione campi riga blocco
				if (listaSerie.size() == 1){
					myForm.setSelectedSerie("0");
				}
				myForm.setNumSerie(listaSerie.size());
				myForm.setCodBib(myForm.getCodBib());
				myForm.setDescrBib(myForm.getDescrBib());
				myForm.setListaSerie(listaSerie);
			}
		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		} catch (UtenteNotAuthorizedException e) {
			//??
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward nuova(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (myForm.getListaSerie() != null && myForm.getListaSerie().size() == 1){
				SerieVO serie = (SerieVO)myForm.getListaSerie().get(0);
				ModelloDefaultVO modello = this.getModelloDefault(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket());
				if (modello != null){
					if (ValidazioneDati.isFilled(modello.getN_serie()) && modello.getN_serie().equals("0")){
						if (serie.getCodSerie() != null && serie.getCodSerie().trim().equals("")){
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("error.documentofisico.laBibliotecaGestisceSoloLaSerieASpazio"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
					}
				}
			}
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("prov","nuova");
			return mapping.findForward("nuova");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			request.setAttribute("check", myForm.getSelectedSerie());
			String check = myForm.getSelectedSerie();
			int seriesel;
			if (check != null && check.length() != 0) {
				seriesel = Integer.parseInt(myForm.getSelectedSerie());
				SerieVO scelSerie = (SerieVO) myForm.getListaSerie().get(seriesel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("codSerie",scelSerie.getCodSerie());
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
			return mapping.getInputForward();
		}
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int serieSel;
			request.setAttribute("checkS", myForm.getSelectedSerie());
			String check = myForm.getSelectedSerie();
			if (check != null && check.length() != 0) {
				serieSel = Integer.parseInt(myForm.getSelectedSerie());
				SerieVO scelSerie = (SerieVO) myForm.getListaSerie().get(serieSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("codSerie", scelSerie.getCodSerie());
				request.setAttribute("prov","esamina");
				return mapping.findForward("esamina");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
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

		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			return mapping.findForward("chiudi");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
			CodiciAttivita.getIstance().GDF_SERIE_INVENTARIALE, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		try{
			int numBlocco = myForm.getBloccoSelezionato();
			String idLista = myForm.getIdLista();
			String ticket = Navigation.getInstance(request).getUserTicket();
			if (numBlocco>1 && idLista != null) {
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
				if (bloccoVO != null) {
					myForm.getListaSerie().addAll(bloccoVO.getLista());
					Collections.sort(myForm.getListaSerie());
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

	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}
	private List getListaSerie(String codPolo, String codBib, String ticket, int nRec, ActionForm form) throws Exception {
		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			myForm.setNoSerie(true);
			return null;
		}else{
			myForm.setNoSerie(false);
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
		SerieInventarialeListaForm myForm = (SerieInventarialeListaForm) form;
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_SERIE_INVENTARIALE, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
