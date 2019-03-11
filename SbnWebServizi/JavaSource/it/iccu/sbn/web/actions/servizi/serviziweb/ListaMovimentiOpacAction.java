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
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO.TipoRichiesta;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.ListaMovimentiOpacForm;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.LoginForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.LogoutDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class ListaMovimentiOpacAction extends ServiziBaseAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.avanti",   "avanti");
		map.put("servizi.bottone.indietro", "indietro");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaMovimentiOpacForm currentForm = (ListaMovimentiOpacForm) form;
		HttpSession session = request.getSession();

		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		currentForm.setCodUtente(utente.getUserId());
		currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
		currentForm.setCod_biblio((String)session.getAttribute(Constants.COD_BIBLIO));
		currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
		currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));

		Navigation navi = Navigation.getInstance(request);

		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			//
			//1)controllo scadenza diritti globale
			//2)controllo sospenzione diritti globale
			//3)esistenza del documento(categoria di fruizione/disponibilitÃ  intrinseca )
			//4)lista dei servizi forniti sul documento (pagina selezione servizio)
			//5)controllo il servizio selezionato (scadenza diritto/sospenzione diritto)
			//6)controllo del numero delle richieste giÃ  attive per il servizio per l'utente 11
			//7)controllo disponibilitÃ  reale del documento (controllo numero dei
			//movimenti attivi per l'utente "max mov gg 20" controllo 12)

			//passati tutti controlli viene prospettata la mappa di inserimento
			//della richiesta

			RichiestaOpacVO ricOpac = (RichiestaOpacVO)session.getAttribute(Constants.RICHIESTA_OPAC);

			if (!ValidazioneDati.isFilled(ricOpac.getSegnatureOpac()) ) { //richiesta documento SBN da OPAC
				//currentForm.setCod_biblio(ricOpac.getBibIscr());
				currentForm.setCodBibInv(ricOpac.getCodBibOpac());
				currentForm.setCodInvenInv(String.valueOf(ricOpac.getCodInventOpac()));
				currentForm.setCodSerieInv(ricOpac.getCodSerieOpac());
				currentForm.setAutore(ricOpac.getAutore());
				currentForm.setTitolo(ricOpac.getTitolo());
				currentForm.setAnno(ricOpac.getAnno());
				currentForm.setTipoDoc(ricOpac.getNatura());
				//se ha selezionato la bib. eseguo i seguenti controlli
				if (!ValidazioneDati.isFilled(currentForm.getCodSerieInv()) )
					currentForm.setCodSerieInv("   ");

			} else {// richiesta documento nonSBN(DatiDocumento)per segnature
				//impostare tutti i campi del VO(InfoDocumentoVO)
				//currentForm.setCod_biblio(ricOpac.getBibIscr());
				currentForm.setCodBibInv(ricOpac.getCodBibOpac());
				currentForm.setCodInvenInv(String.valueOf(ricOpac.getCodInventOpac()));
				currentForm.setCodSerieInv(ricOpac.getCodSerieOpac());
				currentForm.setAutore(ricOpac.getAutore());
				currentForm.setTitolo(ricOpac.getTitolo());
				currentForm.setAnno(ricOpac.getAnno());

				currentForm.setCodSegnatura(ricOpac.getSegnatureOpac());

				currentForm.setEditore(ricOpac.getEditore());
				currentForm.setLuogoEdizione(ricOpac.getLuogoEdizione());
				currentForm.setTipoDoc(ricOpac.getNatura());
				currentForm.setTipoDocLet(ricOpac.getTipoDocLet());
				currentForm.setCodDocLet(ricOpac.getCodDocLet());
				currentForm.setProgrEsempDocLet(ricOpac.getProgrEsempDocLet());

			}

			//caricamento lista servizi
			this.loadDefault(currentForm, request);

			if (!ValidazioneDati.isFilled(currentForm.getLstCodiciServizio()) ) {
				//Non sono presenti servizi per il doc. richiesto
				//almaviva5_20110915 #4337 messaggio modificato se provengo da OPAC
				NavigationCache cache = navi.getCache();
				NavigationElement first = cache.getElementAt(0);
				if (first != null && first.getForm() instanceof LoginForm)
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.login.serviziDispDocOpac"));
				else
					LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.serviziDispDocOpac"));

				if (!ValidazioneDati.isFilled(ricOpac.getSegnatureOpac()) ) { //richiesta documento SBN da OPAC
					LogoutDelegate.logout(request);
					return navi.goForward(mapping.findForward("login"), true);
				} else //richiesta documento nonSBN per segnatura
					return navi.goBack(true);
			}

			if (currentForm.getInfoDocumentoVO() != null &&
				!ValidazioneDati.isFilled(currentForm.getInfoDocumentoVO().getSegnatura()) ) {
					// se non Ã¨ presente la collocazione
					// imposto tutto l'array a null
					// perchÃ¨ non ci possono essere servizi disponibili
					// per un documento che non ha collocazione
					currentForm.setLstCodiciServizio(null);
			}
			//
			if(request.getParameter("param")!=null){
				if(request.getParameter("param").equals("true")){
					currentForm.setFlgServDisDoc(true);
				}else{
					currentForm.setFlgServDisDoc(false);
				}
			}

			return mapping.getInputForward();

		} catch (ValidationException e) {
			//almaviva5_20190111 #6872
			LogoutDelegate.logout(request);
			return (mapping.findForward("login"));

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			if (ValidazioneDati.equals(e.getMessage(), "invNonEsistente"))
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));
			else
				//errore nel caricamento lista servizi
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.ListaServiziOpac"));

			LogoutDelegate.logout(request);
			return (mapping.findForward("login"));

		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			ListaMovimentiOpacForm currentForm = (ListaMovimentiOpacForm) form;

		try {
			//
			if (currentForm.getCodSegnatura()==null){//richiesta documento SBN da OPAC
				LogoutDelegate.logout(request);
				return (mapping.findForward("login"));
			}else{//richiesta documento nonSBN per segnatura
				//NavigationForward goBack = Navigation.getInstance(request).goBack(true);
				//return goBack;
				HttpSession session = request.getSession();
				session.removeAttribute(Constants.DATI_DOC_KEY);
				return (mapping.findForward("menuServizi"));
			}

		} catch (Exception ex) {
			//ricordare di inserire il msg di errore
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.ListaServiziOpac"));
			this.saveErrors(request, error);
			return (mapping.getInputForward());
		}
	}

	public ActionForward avanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaMovimentiOpacForm currentForm = (ListaMovimentiOpacForm) form;
		HttpSession session = request.getSession();
		RichiestaOpacVO ricOpac = (RichiestaOpacVO)session.getAttribute(Constants.RICHIESTA_OPAC);
		//
		ActionMessages error = new ActionMessages();
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();
		try {
			String idServizioScelto = currentForm.getServizioScelto();
			if (Integer.valueOf(idServizioScelto) > 0){
				//inserimento della richiesta locale Opac "Nuova Richiesta"

				for (ServizioBibliotecaVO servbib : currentForm.getLstCodiciServizio()) {
					if (servbib.getIdServizio() == Integer.valueOf(idServizioScelto)) {
						ricOpac.setServizio(servbib.getCodServ());
						ricOpac.setCodServizio(servbib.getCodTipoServ());
						ricOpac.setDescrServizio(servbib.getDescrTipoServ());
					}
				}
				//
				InfoDocumentoVO infoVO = currentForm.getInfoDocumentoVO();
				infoVO.setAnnoPeriodico(currentForm.getAnnoPeriodico()); //per periodici

				request.setAttribute(Constants.INFO_DOCUMENTO, infoVO);
				request.setAttribute(Constants.SERVIZI_ATTIVI_DOCUMENTO, currentForm.getLstCodiciServizio());
				//
				return (mapping.findForward("richiestaServizioLoc"));//mappa per l'inserimento richiesta
				//
			}else{
				//scegliere il servizio
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.InsRichOpacSceltaServ"));
				this.saveErrors(request, error);
			}
		} catch (Exception ex) {
			//errore nell'inserimento della richiesta
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.utenti.erroreInsRichOpac"));
			this.saveErrors(request, error);
		}
		return mapping.getInputForward();
	}
	//
	private void loadDefault(ListaMovimentiOpacForm currentForm, HttpServletRequest request) throws Exception {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		String codPolo = Navigation.getInstance(request).getPolo();

		Iterator<ServizioBibliotecaVO> iterator = null;
		Iterator<ServizioBibliotecaVO> iterator_1 = null;
		boolean first;
		//

		HttpSession session = request.getSession();
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);

		List<ServizioBibliotecaVO> lstDirittiUtente = delegate.getServiziAttivi(
				codPolo,
				utente.getCodBib(),
				currentForm.getCodUtente(),
				currentForm.getCod_biblio(),//currentForm.getCodBibInv(),
				DaoManager.now(),
				utente.isRemoto() );

		RichiestaOpacVO richiesta = (RichiestaOpacVO)session.getAttribute(Constants.RICHIESTA_OPAC);
		final boolean richiestaILL = richiesta.getTipo() == TipoRichiesta.RICHIESTA_ILL;

		//filtrati solo i servizi locali o ILL in base all'ambito
		lstDirittiUtente = Stream.of(lstDirittiUtente).filter(new Predicate<ServizioBibliotecaVO>() {
			public boolean test(ServizioBibliotecaVO srv) {
				try {
					CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(srv.getCodTipoServ(),
							CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
					if (ts == null)
						return false;

					if (richiestaILL)
						return srv.isILL();		//solo servizi ill
					else
						return ts.isLocale();	//solo servizi locali

				} catch (Exception e) {
					return false;
				}
			}}).toList();
		currentForm.setLstCodiciServizio(lstDirittiUtente);

		if (ValidazioneDati.isFilled(lstDirittiUtente) ) {
			// imposto in lstDirittiUtente la descrizione del Codice Tipo Servizio
			// prendendola dalla tabella tb_codici
			iterator = lstDirittiUtente.iterator();
			while (iterator.hasNext()) {
				ServizioBibliotecaVO servizioVO = iterator.next();
				if (servizioVO.getCodTipoServ() != null) {
					servizioVO.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO.getCodTipoServ(),	CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
				}
			}
		}
		//
		if (!ValidazioneDati.isFilled(lstDirittiUtente) ) {
			// se la lista dei diritti utente Ã¨ vuota non effettuo le successive elaborazioni
			// cioÃ¨ la selezione dei soli diritti comuni tra quelli dell'utente
			// e quelli dell'inventario o segnatura
			currentForm.setLstCodiciServizio(lstDirittiUtente);
		}
		else {
			if (!ValidazioneDati.isFilled(currentForm.getCodSegnatura()) ) { //Documento SBN (ricerca documento per inventario)
				// oltre l'utente c'Ã¨ solo l'inventario
				// elaboro lstDirittiInventario
				InfoDocumentoVO infoDocumento = this.getInfoInventario(codPolo, currentForm.getCodBibInv(), currentForm.getCodSerieInv(), new Integer(currentForm.getCodInvenInv()), Navigation.getInstance(request).getUserTicket(), this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/, request);

				if (infoDocumento != null) {
					currentForm.setInfoDocumentoVO(infoDocumento);
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));
					this.saveErrors(request, errors);
					throw new ValidationException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO);
				}
				//
				List<ServizioBibliotecaVO> lstDirittiInventario = null;
				InventarioTitoloVO inventario = infoDocumento.getInventarioTitoloVO();
				String codFrui = inventario.getCodFrui() != null ? inventario.getCodFrui() : "";
				lstDirittiInventario = delegate.getServiziAttiviPerCatFruizione(codPolo, inventario.getCodBib(), codFrui.trim());
				//lista diritti documento lstDirittiInventario
				// imposto in lstDirittiInventario la descrizione del Codice Tipo Servizio
				// prendendola dalla tabella tb_codici

				iterator_1 = lstDirittiInventario.iterator();
				while (iterator_1.hasNext()) {
					ServizioBibliotecaVO servizioVO_1 = iterator_1.next();
					if (servizioVO_1.getCodTipoServ() != null) {
						servizioVO_1.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO_1.getCodTipoServ(),	CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
					}
				}
				iterator_1 = lstDirittiInventario.iterator();
				//
				String apCodTipoSer = null;
				List<ServizioBibliotecaVO> lstServiziDoc = new ArrayList<ServizioBibliotecaVO>();
				while (iterator_1.hasNext()) {
					ServizioBibliotecaVO servizioVO_1 = iterator_1.next();
					if  (servizioVO_1.getCodTipoServ() != null &&
						 servizioVO_1.getCodServ() != null) {
						 	if  ((!servizioVO_1.getCodTipoServ().equals (apCodTipoSer))){
						 		lstServiziDoc.add(servizioVO_1);
						 		//break;
						 	}
					}
					apCodTipoSer = servizioVO_1.getCodTipoServ();

				}

				if (ValidazioneDati.isFilled(lstDirittiInventario) ) {

					// confronto lstDirittiUtentie e lstDirittiInventario
					// riportando su lstServizi solo gli elementi di
					// lstDirittiUtente che sono presenti anche in lstDirittiInventario
					List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();

					first = true;
					iterator = lstDirittiUtente.iterator();

					// loop su lstDirittiUtente
					while (iterator.hasNext()) {
						ServizioBibliotecaVO servizioVO = iterator.next();
						if (first == true) {
							// il primo elemento viene copiato
							// perchÃ¨ contiene valori a null
							// (nella drop il primo elemento Ã¨ vuoto)
							lstServizi.add(servizioVO);
							first = false;
							continue;
						}

						iterator_1 = lstDirittiInventario.iterator();
						// loop su lstServiziInventario
						while (iterator_1.hasNext()) {
							ServizioBibliotecaVO servizioVO_1 = iterator_1.next();
							if (servizioVO_1.getCodTipoServ() != null &&
								servizioVO_1.getCodServ() != null &&
							    servizioVO_1.getCodTipoServ().equals(servizioVO.getCodTipoServ()) &&
							    servizioVO_1.getCodServ().equals(servizioVO.getCodServ())){
								// copio l'elemento di lstDirittiUtente perchÃ¨ trovato uguale in lstDirittiInventario
								lstServizi.add(servizioVO);
								break;

							}
						}
					}

					currentForm.setLstCodiciServizioDoc(lstServiziDoc);//Modifica del 22/03/2010 almaviva
					if (!richiestaILL)
						currentForm.setLstCodiciServizio(lstServizi);
				}
				else {
					currentForm.setLstCodiciServizioDoc(lstDirittiInventario);//Modifica del 22/03/2010 almaviva
					// imposto setLstCodiciServizio alla lista vuota
					currentForm.setLstCodiciServizio(lstDirittiInventario);
				}
			}//	fine gestione Documento SBN per inventario
			else { //Documento non SBN (ricerca documento per segnatura)
				// oltre l'utente c'Ã¨ solo la segnatura
				// elaboro lstDirittiSegnatura
				InfoDocumentoVO infoDocumento = this.getInfoSegnatura(request,
						codPolo,
						currentForm.getCodBibInv(),
						currentForm.getTipoDocLet(),
						new Integer(currentForm.getCodDocLet() ),
						new Integer(currentForm.getProgrEsempDocLet()) );
				if (infoDocumento != null) {
					currentForm.setInfoDocumentoVO(infoDocumento);
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.datiInventarioNonTrovati"));
					this.saveErrors(request, errors);
					throw new ValidationException(SbnErrorTypes.SRV_SEGNATURA_NON_DISPONIBILE);
				}

				List<ServizioBibliotecaVO> lstDirittiSegnatura = null;

				DocumentoNonSbnVO documento = infoDocumento.getDocumentoNonSbnVO();
				String codFruiDoc = documento.getCodFruizione();
				if (ValidazioneDati.strIsNull(codFruiDoc)) {
					CommandResultVO result = delegate.invoke(CommandType.CATEGORIA_FRUIZIONE_DOCUMENTO_NON_SBN, documento);
					result.throwError();

					DocumentoNonSbnVO doc = (DocumentoNonSbnVO) result.getResult();
					if (ValidazioneDati.isFilled(doc.getCodNoDisp()))
						codFruiDoc = "";
					else
						codFruiDoc = doc.getCodFruizione();

				}
				lstDirittiSegnatura = delegate.getServiziAttiviPerCatFruizione(codPolo, documento.getCodBib(), codFruiDoc.trim());

				iterator_1 = lstDirittiSegnatura.iterator();
				while (iterator_1.hasNext()) {
					ServizioBibliotecaVO servizioVO_1 = iterator_1.next();
					if (servizioVO_1.getCodTipoServ() != null) {
						servizioVO_1.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO_1.getCodTipoServ(),	CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
					}
				}

				iterator_1 = lstDirittiSegnatura.iterator();
				String apCodTipoSer = null;
				List<ServizioBibliotecaVO> lstServiziDoc = new ArrayList<ServizioBibliotecaVO>();
				while (iterator_1.hasNext()) {
					ServizioBibliotecaVO servizioVO_1 = iterator_1.next();
					if  (servizioVO_1.getCodTipoServ() != null &&
						 servizioVO_1.getCodServ() != null) {
						 	if  ((!servizioVO_1.getCodTipoServ().equals (apCodTipoSer))){
						 		lstServiziDoc.add(servizioVO_1);
						 		//break;
						 	}
					}
					apCodTipoSer = servizioVO_1.getCodTipoServ();

				}

				if (ValidazioneDati.isFilled(lstDirittiSegnatura) ) {
					// confronto lstDirittiUtente e lstDirittiSegnatura
					// riportando su lstServizi solo gli elementi di
					// lstDirittiUtente che sono presenti anche in lstDirittiSegnatura
					List<ServizioBibliotecaVO> lstServizi = new ArrayList<ServizioBibliotecaVO>();
					iterator = lstDirittiUtente.iterator();
					first = true;
					// loop su lstDirittiUtente
					while (iterator.hasNext()) {
						ServizioBibliotecaVO servizioVO = iterator.next();
						if (first == true) {
							// il primo elemento viene copiato
							// perchÃ¨ contiene valori a null
							// (nella drop il primo elemento Ã¨ vuoto)
							lstServizi.add(servizioVO);
							first = false;
							continue;
						}
						iterator_1 = lstDirittiSegnatura.iterator();
						// loop su lstDirittiSegnatura
						while (iterator_1.hasNext()) {
							ServizioBibliotecaVO servizioVO_1 = iterator_1.next();
							if (servizioVO_1.getCodTipoServ() != null &&
								servizioVO_1.getCodServ() != null &&
							    servizioVO_1.getCodTipoServ().equals(servizioVO.getCodTipoServ()) &&
							    servizioVO_1.getCodServ().equals(servizioVO.getCodServ())){
								// copio l'elemento di lstDirittiUtente perchÃ¨ trovato uguale in lstDirittiSegnatura
								lstServizi.add(servizioVO);
								break;
							}
						}
					}
					currentForm.setLstCodiciServizioDoc(lstServiziDoc);//Modifica del 22/03/2010 almaviva
					currentForm.setLstCodiciServizio(lstServizi);
				}
				else {
					// imposto setLstCodiciServizio alla lista vuota
					currentForm.setLstCodiciServizio(lstDirittiSegnatura);
				}
			}
		}
	}//FINE LOADDEFAULT

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		//almaviva5_20171006 servizi ill
		if (ValidazioneDati.equals(idCheck, "RICHIESTA_LOCALE")) {
			ListaMovimentiOpacForm currentForm = (ListaMovimentiOpacForm) form;
			return ValidazioneDati.equals(currentForm.getCod_biblio(), currentForm.getCodBibInv());
		}
		return false;
	}

}
