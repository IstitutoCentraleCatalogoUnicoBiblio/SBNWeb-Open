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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.DatiDocumentoForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.Date;
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
import org.apache.struts.actions.LookupDispatchAction;

public class DatiDocumentoAction extends LookupDispatchAction {
	//
	private CaricamentoCombo carCombo = new CaricamentoCombo();
	//
	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");//inserimento suggerimento
		map.put("servizi.bottone.indietro", "indietro");
		map.put("button.avanti", "avanti");//inserimento documento non sbn per collocazione
		return map;
	}
	//
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiDocumentoForm currentForm = (DatiDocumentoForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			HttpSession session = request.getSession();
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);

			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
			currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));

			Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);

			if (idUtente == null) {
				//invio msg:"selezionare una biblioteca e premere "Ok".
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.dirittiUtente"));
				this.saveErrors(request, errors);
				navi.purgeThis();
				return (mapping.findForward("selezioneBiblioteca"));
			}

			DocumentoNonSbnVO datiDoc = (DocumentoNonSbnVO)session.getAttribute(Constants.DATI_DOC_KEY);
			this.loadTipoDocumento(currentForm);
			currentForm.setSegnatura((String)request.getAttribute("segnatura"));

			if (datiDoc != null) {//update
				String annoEdizione = datiDoc.getAnnoEdizione();
				if (ValidazioneDati.isFilled(annoEdizione))
				currentForm.setAnnoEdi(annoEdizione);
				currentForm.setAutore(datiDoc.getAutore());
				currentForm.setEditore(datiDoc.getEditore());
				currentForm.setLuogoEdizione(datiDoc.getLuogoEdizione());
				currentForm.setTitolo(datiDoc.getTitolo());

				//caso dati non modificabili in quanto il doc. è stato inserito dalla bib.
				currentForm.setLettura(datiDoc.getFonte() != 'L');

				currentForm.setDoc(datiDoc);
				currentForm.setIdDocLettore(datiDoc.getIdDocumento());
			}

			return mapping.getInputForward();

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			//Errore caricamento dati suggerimento di acquisto
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.dati.doc.suggerimento"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}
	}
	//
	public ActionForward ok(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {

		DatiDocumentoForm currentForm = (DatiDocumentoForm)form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			HttpSession session = request.getSession();

			//inizio controlli campi obbligatori
			ActionMessages err = this.controlliObbligatorieta(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}
			// fine controlli
			DocumentoNonSbnVO datiDoc = new DocumentoNonSbnVO();
			datiDoc = currentForm.getDoc();
			BibliotecaVO bib = (BibliotecaVO)session.getAttribute(Constants.BIBLIO);
			//
			String polo = Navigation.getInstance(request).getPolo();
			//
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
			datiDoc.setUtente(utente.getUserId());
			datiDoc.setCodBib(bib.getCod_bib());
			datiDoc.setCodPolo(polo);
			//
			datiDoc.setTipo_doc_lett('S');
			//
			datiDoc.setCod_doc_lett(currentForm.getDoc().getCod_doc_lett());
			//
			datiDoc.setFonte('L');
			//
			datiDoc.setStato_sugg('W');
			datiDoc.setData_sugg_lett(new Date());
			datiDoc.setFlCanc("N");

			//datiDoc.setSegnatura(currentForm.getSegnatura());
			datiDoc.setAutore(currentForm.getAutore());
			datiDoc.setTitolo(currentForm.getTitolo());
			datiDoc.setCod_tipo_doc(currentForm.getDoc().getCod_tipo_doc());
			datiDoc.setLuogoEdizione(currentForm.getLuogoEdizione());
			datiDoc.setEditore(currentForm.getEditore());
			datiDoc.setAnnoEdizione(currentForm.getAnnoEdi().toString());
			//String segnNorm = OrdinamentoCollocazione2.normalizza(currentForm.getSegnatura());
			//datiDoc.setOrd_segnatura(segnNorm);

			datiDoc.setUteIns(polo + bib.getCod_bib() + Constants.UTENTE_WEB_TICKET);
			datiDoc.setUteVar(polo + bib.getCod_bib() + Constants.UTENTE_WEB_TICKET);

			datiDoc.setTsIns(DaoManager.now());

			datiDoc.setTsVar(DaoManager.now());
			datiDoc.setSegnatura(" ");
			//
			datiDoc.setNote(currentForm.getSuggerimenti());
			//datiDoc.setAnnoEdizione(currentForm.getAnnataPeriodici());?????
			datiDoc.setCod_doc_lett(0);
			//
			//inserimento nuovo documento(suggerimento d'acquisto)
			List<DocumentoNonSbnVO> ret = null;
			String ticket = Navigation.getInstance(request).getUserTicket();
			ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
			ret = factory.getGestioneServizi().aggiornaDocumentoNonSbn(ticket, ValidazioneDati.asSingletonList(datiDoc));
			//
			if (ret != null) { // inserimento nuovo documento Ok
				//msg:Inserimento nuovo documento effettuato correttamente
				ActionMessages error = new ActionMessages();
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("servizi.utenti.suggerimento"));
				this.saveErrors(request, error);
				return (mapping.findForward("esameSugAcq"));
				//return (mapping.findForward("menuServizi"));
			}
			//
			return (mapping.getInputForward());

		}catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			//Errore inserimento suggerimento di acquisto
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.suggerimento.datiDoc"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}

	}
	//
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DatiDocumentoForm currentForm = (DatiDocumentoForm)form;

		try {
			//request.setAttribute("segnatura",currentForm.getSegnatura());
			HttpSession session = request.getSession();
			session.removeAttribute(Constants.DATI_DOC_KEY);
			if (currentForm.getSegnatura() != null){
				request.setAttribute("segnatura",currentForm.getSegnatura());
				return (mapping.findForward("menuServizi"));
			}
			return (mapping.findForward("esameSugAcq"));
		}catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			//Errore caricamento dati suggerimento di acquisto
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.dati.doc.suggerimento"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}
	}
	//
	public ActionForward avanti(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		DatiDocumentoForm currentForm = (DatiDocumentoForm)form;
		HttpSession session = request.getSession();
		Navigation navi = Navigation.getInstance(request);

		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			//fare l'inserimento del documento non sbn per collocazione
			//se i servizi relativi alla categoria di fruizione risultano
			//congruenti con i diritti dell'utente prospetto la jsp di scelta
			//del servizio

			//inizio controlli campi obbligatori
			ActionMessages err = this.controlliObbligatorieta(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}
			// fine controlli
			DocumentoNonSbnVO datiDoc = currentForm.getDoc();
			BibliotecaVO bib = (BibliotecaVO)session.getAttribute(Constants.BIBLIO);
			//
			String polo = navi.getPolo();
			//
			if (datiDoc != null) {
				request.setAttribute("segnatura",currentForm.getSegnatura());
				request.setAttribute("AUTORE",currentForm.getAutore());
				request.setAttribute("TITOLO", currentForm.getTitolo());
				request.setAttribute("TIPO_DOC",currentForm.getDoc());
				request.setAttribute("LUOGO_EDI",currentForm.getLuogoEdizione());
				request.setAttribute("EDITORE",currentForm.getEditore());
				request.setAttribute("ANNO_EDI",currentForm.getAnnoEdi());
				//
				session.setAttribute(Constants.DOCUMENTO, currentForm.getTitolo());
				//
				UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
				datiDoc.setUtente(utente.getUserId());
				datiDoc.setCodBib(bib.getCod_bib());
				datiDoc.setCodPolo(polo);
				//
				if (!ValidazioneDati.isFilled(datiDoc.getTipo_doc_lett()))
					datiDoc.setTipo_doc_lett('P');


				//datiDoc.setNatura(currentForm.getDoc().getListaTipoDoc().charAt(0));
				//currentForm.getTipoD().getCod_tipo_doc()
				if (!ValidazioneDati.isFilled(datiDoc.getFonte()))
					datiDoc.setFonte('L');//"L = utente lettore"
				//
				//datiDoc.setStato_sugg('W');
				//datiDoc.setData_sugg_lett(new Date());
				datiDoc.setFlCanc("N");
				//
				datiDoc.setSegnatura(currentForm.getSegnatura());
				datiDoc.setAutore(currentForm.getAutore());
				datiDoc.setTitolo(currentForm.getTitolo());
				//datiDoc.setCod_tipo_doc(currentForm.getTipoD().getCod_tipo_doc());
				datiDoc.setLuogoEdizione(currentForm.getLuogoEdizione());
				datiDoc.setEditore(currentForm.getEditore());
				datiDoc.setAnnoEdizione(currentForm.getAnnoEdi().toString());
				//String segnNorm = OrdinamentoCollocazione2.normalizza(currentForm.getSegnatura());
				datiDoc.setOrd_segnatura(OrdinamentoCollocazione2.normalizza(currentForm.getSegnatura()));

				datiDoc.setUteIns(polo + bib.getCod_bib() + Constants.UTENTE_WEB_TICKET);
				datiDoc.setUteVar(polo + bib.getCod_bib() + Constants.UTENTE_WEB_TICKET);
				//
				//inserimento dati doc nonSBN/modifica dati doc nonSBN
				if (currentForm.getIdDocLettore()==0){//Inserimento doc nonSBN
					datiDoc.setCod_doc_lett(0);
					datiDoc.setTsIns(DaoManager.now());
					datiDoc.setTsVar(DaoManager.now());
				}else{//Modifica doc nonSBN
					datiDoc.setIdDocumento(currentForm.getIdDocLettore());
				}

				String ticket = navi.getUserTicket();
				ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
				datiDoc = factory.getGestioneServizi().aggiornaDocumentoNonSbn(ticket, ValidazioneDati.asSingletonList(datiDoc)).get(0);
				//
				RichiestaOpacVO datiDocNOsbn = new RichiestaOpacVO();
				String bibOperante = (String) session.getAttribute(Constants.COD_BIBLIO);
				datiDocNOsbn.setCodBibOpac(bibOperante);//bib selezionata

				datiDocNOsbn.setBibIscr(utente.getCodBib());//bib di prima iscrizione
				datiDocNOsbn.setAnno(currentForm.getAnnoEdi());
				datiDocNOsbn.setTitolo(currentForm.getTitolo());
				datiDocNOsbn.setSegnatureOpac(currentForm.getSegnatura());
				datiDocNOsbn.setAutore(currentForm.getAutore());
				//
				datiDocNOsbn.setLuogoEdizione(currentForm.getLuogoEdizione());
				datiDocNOsbn.setEditore(currentForm.getEditore());
				datiDocNOsbn.setNatura(String.valueOf(currentForm.getDoc().getNatura()));
				datiDocNOsbn.setTipoDocLet(String.valueOf(currentForm.getDoc().getTipo_doc_lett()));
				datiDocNOsbn.setCodDocLet(String.valueOf(datiDoc.getCod_doc_lett()));

				//almaviva5_20100531 cerco un esemplare libero su cui fare la richiesta
				//altrimenti viene scelto l'esemplare occupato con la data fine
				//prevista minore.
				ServiziDelegate delegate = ServiziDelegate.getInstance(request);
				short prgEsemplare = delegate.cercaEsemplareLibero(polo, bibOperante, datiDoc);

				datiDocNOsbn.setProgrEsempDocLet(String.valueOf(prgEsemplare));
				session.setAttribute(Constants.RICHIESTA_OPAC, datiDocNOsbn);

				navi.purgeThis();
				return (mapping.findForward("listaMovimentiOpac"));//mappa selezione servizio
				//return (mapping.findForward("sceltaServizi"));//mappa selezione servizio
			}
			//
			return (mapping.findForward("menuServizi"));
			//
		}catch (Exception e) {
			log.error("", e);
			//Errore nell' inserimento del documento
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.dati.doc.insDatiDoc"));
			return (mapping.getInputForward());
		}
	}
	//metodo per caricamento combo dello stato del suggerimento
	private void loadTipoDocumento(DatiDocumentoForm currentForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		currentForm.setListaTipoDoc(carCombo.loadComboCodiciDesc (factory.getCodici().getCodici(CodiciType.CODICE_NATURA_ORDINE)));

	}
	//
	private ActionMessages controlliObbligatorieta(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		//
		DatiDocumentoForm currentForm = (DatiDocumentoForm) form;
		ActionMessages errors = new ActionMessages();
		//
		if (!ValidazioneDati.isFilled(currentForm.getTitolo()) ) {
			//Titolo campo obbligatorio
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.titolo.Obbligatorio"));
			return errors;
		}
		if (!ValidazioneDati.isFilled(currentForm.getDoc().getNatura()) ) {
			//Tipo documento campo obbligatorio
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.tipoDocumento.Obbligatorio"));
			return errors;
		}

		return errors;
	}
}
