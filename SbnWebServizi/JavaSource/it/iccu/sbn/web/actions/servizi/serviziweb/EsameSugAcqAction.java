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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.EsameSugAcqForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class EsameSugAcqAction extends SinteticaLookupDispatchAction {

	private CaricamentoCombo carCombo = new CaricamentoCombo();
	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca", "cerca");
		map.put("servizi.bottone.nuova.proposta", "nuovo");
		return map;
	}
	//metodo di init esameSugAcq.jsp
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsameSugAcqForm currentForm = (EsameSugAcqForm) form;
		//
		Navigation navi = Navigation.getInstance(request);
		//
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);
			//
			HttpSession session = request.getSession();
			//
			Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
			currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));
			//
			if (idUtente == null ) {
				//invio msg:"selezionare una biblioteca e premere "Ok".
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.dirittiUtente"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).purgeThis();
				return (mapping.findForward("selezioneBiblioteca"));
			}else {
				//
				//caricamento combo stato suggerimento
				//
				this.loadStatoSuggerimento(currentForm);
				currentForm.setStatoSug(new StrutturaCombo("",""));

				if (currentForm.getListaStatoSuggerimento() == null) {
					//Errore di caricamento dello stato del suggerimento
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.lista.stato.suggerimento"));
					this.saveErrors(request, errors);
					return (mapping.getInputForward());
				}
				return mapping.getInputForward();
			}
			//return mapping.getInputForward();
		}
		catch (Exception ex) {
			//Errore di caricamento dello stato del suggerimento
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.lista.stato.suggerimento"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}
	}
	//metodo bottone esamina
	public ActionForward cerca(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		EsameSugAcqForm currentForm = (EsameSugAcqForm)form;
		//
		try {
			HttpSession session = request.getSession();
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
			//
			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
			//
			Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);
			//
			String ticket = Navigation.getInstance(request).getUserTicket();
			String codPolo = Navigation.getInstance(request).getPolo();
			//
			if (idUtente == null) {
				//invio msg:"selezionare una biblioteca e premere "Ok".
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.dirittiUtente"));
				this.saveErrors(request, errors);
				return (mapping.findForward("selezioneBiblioteca"));
				//return mapping.getInputForward();
			}else {
				//Gestione criteri di ricerca
				if (currentForm.getSugAcq().getStatoSuggerimentoDocumento() != null) {
					//Init lista
					if (currentForm.getListaSugAcq()!=null){
						currentForm.getListaSugAcq().clear();
					}
					//
					//caricamento lista suggerimento di acquisto
					ListaSuppDocumentoVO ricArr = new ListaSuppDocumentoVO();
					//
					StrutturaTerna ute = new  StrutturaTerna();
					ute.setCodice1(utente.getCodBib());
					ute.setCodice2(utente.getUserId());
					ute.setCodice3(codPolo);
					ricArr.setUtente(ute);//???
					ricArr.setCodBibl((String)session.getAttribute(Constants.COD_BIBLIO));
					ricArr.setCodPolo(codPolo);
					ricArr.setTicket(ticket);
					ricArr.setStatoSuggerimentoDocumento(currentForm.getSugAcq().getStatoSuggerimentoDocumento());
					ricArr.setDataSuggerimentoDocDa(currentForm.getDal());
					ricArr.setDataSuggerimentoDocA(currentForm.getAl());

					List<DocumentoVO> listaSuggerimenti;
					listaSuggerimenti = this.getListaSuggerimentiVO(ricArr); //

					if (listaSuggerimenti == null ) {
						//invio msg:"Non sono presenti suggerimenti di acquisto"
						ActionMessages errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.suggerimenti.non.presenti"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					//Carico lista
					currentForm.setListaSugAcq(listaSuggerimenti);
					return mapping.getInputForward();
				}
			}
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			//
			if (ex.getMessage().equals("assenzaRisultati")) {
				//invio msg:"Non sono presenti suggerimenti di acquisto"
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.suggerimenti.non.presenti"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			//Errore di caricamento lista suggerimenti di acquisto
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.lista.suggerimenti.acquisto"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}
	}
	public ActionForward nuovo(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		//Inserimento suggerimento d'acquisto
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);
				return (mapping.findForward("datiDocumento"));

		}catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			//Errore inserimento suggerimento di acquisto
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.suggerimento.datiDoc"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}
	}
	//metodo per caricamento combo dello stato del suggerimento
	private void loadStatoSuggerimento(EsameSugAcqForm currentForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		currentForm.setListaStatoSuggerimento(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoSuggerimento()));
	}
	//metodo per caricamento lista suggerimenti d' acquisto
	private List<DocumentoVO> getListaSuggerimentiVO(ListaSuppDocumentoVO criRicerca) throws Exception {
		List <DocumentoVO> listaSuggerimenti = new ArrayList();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaSuggerimenti =  factory.getGestioneAcquisizioni().getRicercaListaDocumenti(criRicerca);

		return listaSuggerimenti;
	}
}
