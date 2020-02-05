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
package it.iccu.sbn.web.actions.documentofisico.datiInventari;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoNumCollVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.periodici.InventariCollocazioneDecorator;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.ListeInventariForm;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocListeCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class ListeInventariAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(ListeInventariAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.blocco", "blocco");
		map.put("documentofisico.bottone.esInv", "esameInventario");
		map.put("documentofisico.bottone.esInv1", "esameInventario1");
		map.put("documentofisico.bottone.scegli", "ok");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
//		map.put("documentofisico.bottone.ok", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.nuovo", "nuovo");
		map.put("documentofisico.bottone.cancella", "cancella");
		map.put("documentofisico.bottone.nuovo", "nuovo");
		//listaInventariDicollocazione
		map.put("documentofisico.bottone.possessori", "possessori");
//		map.put("documentofisico.bottone.esInv", "esameInventario");
		map.put("documentofisico.bottone.disponibilita", "disponibilita");
		map.put("documentofisico.bottone.collDefin", "collocazioneDefinitiva");
		map.put("documentofisico.bottone.etichetta", "etichetta");
		map.put("documentofisico.bottone.modificaInv", "modificaInv");
		map.put("documentofisico.bottone.modificaColl", "modificaColl");

		map.put("documentofisico.bottone.moduloPrelievo", "moduloPrelievo");

		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ListeInventariForm currentForm = (ListeInventariForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				} catch (Exception e) {
					LinkableTagUtils.resetErrors(request);
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

					return mapping.getInputForward();
				}
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		ListeInventariForm currentForm = (ListeInventariForm)form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();
		// controllo se ho già i dati in sessione;
		if(!currentForm.isSessione())		{
			currentForm.setTicket(navi.getUserTicket());
			UserVO utente = navi.getUtente();
			currentForm.setCodPolo(utente.getCodPolo());
			currentForm.setCodBib(utente.getCodBib());
			currentForm.setDescrBib(utente.getBiblioteca());
			loadDefault(request, mapping, form);
			currentForm.setSessione(true);
		}
		try{
			currentForm.setDisable(true);
			List listaInv = null;
			//calcolo limite per l'estrazione dei records
			//almaviva5_20140408
			int elementiBlocco = currentForm.getNRec();
			int maxRows = CommonConfiguration.getPropertyAsInteger(Configuration.ESAME_COLLOCAZIONI_MAX_RESULT_ROWS, DocumentoFisicoCostant.NUM_REC_LISTA);
			maxRows = ValidazioneDati.max(maxRows, elementiBlocco);
			int limite = maxRows / elementiBlocco;
			if (limite < 1){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreCalcoloLimite"));

				return mapping.getInputForward();
			}

			String tokens[] = String.valueOf(limite).split(",");
			//prendo la pare intera del numero e la moltiplico per myForm.getNRec()
			//così stabilisco il nuovo limite e lo passo al servizio con paramtri di ricerca
			int intero = Integer.valueOf(tokens[0]);
			int nuovoLimite = intero * elementiBlocco;
			currentForm.getParamRicerca().setNuovoLimiteRicerca(nuovoLimite);
			//

			if (request.getAttribute("tipoLista") != null && request.getAttribute("tipoLista").equals("invNoncolloc")){
				if(request.getAttribute("codBib") != null &&
						request.getAttribute("descrBib") != null &&
						request.getAttribute("paramRicerca") != null) {
					currentForm.setCodBib((String)request.getAttribute("codBib"));
					currentForm.setDescrBib((String)request.getAttribute("descrBib"));
					currentForm.setParamRicerca((EsameCollocRicercaVO)request.getAttribute("paramRicerca"));
					currentForm.setTipoLista("invNoncolloc");
					Navigation navigation =navi;
					navigation.setDescrizioneX("Lista Inventari Non Collocati");
					navigation.setTesto("Lista Inventari Non Collocati");
				}
				currentForm.getParamRicerca().setCodPolo(currentForm.getCodPolo());
				currentForm.getParamRicerca().setCodBib(currentForm.getCodBib());
				if (currentForm.getParamRicerca().getElemPerBlocchi() != 0){
					currentForm.setNRec(currentForm.getParamRicerca().getElemPerBlocchi());
				}
				listaInv = this.getListaInventariNonCollocati(currentForm.getParamRicerca(),
						currentForm.getTicket(), elementiBlocco, form);
			}else if (request.getAttribute("prov") != null &&
					(request.getAttribute("prov").equals("ordine") || request.getAttribute("prov").equals("ordineIns")
							 || request.getAttribute("prov").equals("listaSuppInvOrd"))){
				if (request.getAttribute("codTipoOrd") != null  &&
						request.getAttribute("annoOrd") != null &&
						request.getAttribute("codBibF") != null &&
						request.getAttribute("codBibO") != null &&
						request.getAttribute("codOrd") != null &&
						request.getAttribute("titOrd") != null ){
//					myForm.setProv((String)request.getAttribute("prov"));
					currentForm.setCodTipoOrd((String)request.getAttribute("codTipoOrd"));
					if (!request.getAttribute("codBibO").equals(currentForm.getCodBib())){
						currentForm.setCodBib((String)request.getAttribute("codBibO"));
					}
					currentForm.setCodBibO((String)request.getAttribute("codBibO"));
					currentForm.setAnnoOrd(Integer.valueOf((String)request.getAttribute("annoOrd")));
					currentForm.setCodOrd(Integer.valueOf((String)request.getAttribute("codOrd")));
					StrutturaCombo titoloOrdine = ((StrutturaCombo)request.getAttribute("titOrd"));
					currentForm.setBidOrd(titoloOrdine.getCodice());
					currentForm.setIsbdOrd(titoloOrdine.getDescrizione());
					currentForm.setCodBibF((String)request.getAttribute("codBibF"));
					if (request.getAttribute("prov").equals("listaSuppInvOrd")){
						currentForm.setBidOrd(titoloOrdine.getCodice());
						currentForm.setIsbdOrd(titoloOrdine.getDescrizione());
						currentForm.setBid((String)request.getAttribute("bid"));
						currentForm.setIsbd((String)request.getAttribute("titolo"));
							}
					navi.setDescrizioneX("Lista Inventari Ordine");
					navi.setTesto("Lista Inventari Ordine");
					currentForm.setProv((String)request.getAttribute("prov"));
					if (request.getAttribute("tastoOrdiniDaListaInvTitolo") != null){
						currentForm.setProv("ordine");
						request.setAttribute("prov", "tastoOrdiniDaListaInvTitolo");
					}
					if (request.getAttribute("nuovo") != null){
						listaInv = this.getListaInventariOrdiniNonFatturati(currentForm.getCodPolo(), currentForm.getCodBib(),
								currentForm.getCodBibO(), currentForm.getCodTipoOrd(), currentForm.getAnnoOrd(), currentForm.getCodOrd(),
								currentForm.getCodBibF(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), elementiBlocco, form);
					}else{
						listaInv = this.getListaInventariOrdine(currentForm.getCodPolo(), currentForm.getCodBib(),
								currentForm.getCodBibO(), currentForm.getCodTipoOrd(), currentForm.getAnnoOrd(), currentForm.getCodOrd(),
								currentForm.getCodBibF(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), elementiBlocco, form);
					}
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioOrdini"));

					return navi.goBack(true);
				}
			}else if (ValidazioneDati.in(request.getAttribute("prov"), "listaOrdini", "listaOrdPeriodici")) {
				if (request.getAttribute("listaOrdini") != null ){
					List listaOrdini = (List)request.getAttribute("listaOrdini");
					navi.setDescrizioneX("Lista Inventari Ordini");
					navi.setTesto("Lista Inventari Ordini");
					currentForm.setProv((String)request.getAttribute("prov"));
						listaInv = this.getListaInventariOrdini(listaOrdini, this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), elementiBlocco, form);
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioOrdini"));

					return navi.goBack(true);
				}
			}else if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaSuppInvPoss")){
				if (request.getAttribute("codPolo") != null  &&
						request.getAttribute("codBib") != null &&
						request.getAttribute("descr") != null &&
						request.getAttribute("pid") != null){
					currentForm.setPid((String)request.getAttribute("pid"));
					currentForm.setDescrPid((String)request.getAttribute("descr"));
					navi.setDescrizioneX("Lista Inventari Possessori");
					navi.setTesto("Lista Inventari Possessori");
					currentForm.setProv((String)request.getAttribute("prov"));
				listaInv = this.getListaInventariPossessori(currentForm.getCodPolo(), currentForm.getCodBib(),
						currentForm.getPid(), currentForm.getTicket(), elementiBlocco, form, currentForm.getParamRicerca());
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioPossessoreInventari"));

					return navi.goBack(true);
				}
			}else if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("fattura") )){
				if (request.getAttribute("codBibF") != null &&
						request.getAttribute("annoFatt") != null &&
						request.getAttribute("prgFatt") != null ) {
					if (!request.getAttribute("codBibF").equals(currentForm.getCodBib())){
						currentForm.setCodBib((String)request.getAttribute("codBibF"));
					}
					currentForm.setCodBibF((String)request.getAttribute("codBibF"));
					currentForm.setAnnoFattura(Integer.valueOf((String)request.getAttribute("annoFatt")));
					currentForm.setProgrFattura(Integer.valueOf((String)request.getAttribute("prgFatt")));
					navi.setDescrizioneX("Lista Inventari Fattura");
					navi.setTesto("Lista Inventari Fattura");
					currentForm.setProv((String)request.getAttribute("prov"));
				listaInv = this.getListaInventariFattura(currentForm.getCodPolo(), currentForm.getCodBibF(),
						currentForm.getAnnoFattura(), currentForm.getProgrFattura(), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), elementiBlocco, form);
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioFattura"));

					return navi.goBack(true);
				}
			}else if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("rigaFattura") )){
				if (request.getAttribute("codBibF") != null &&
						request.getAttribute("annoF") != null &&
						request.getAttribute("prgF") != null &&
						request.getAttribute("rigaFatt") != null &&
						request.getAttribute("codTipoOrd") != null  &&
						request.getAttribute("annoOrd") != null &&
						request.getAttribute("titOrd") != null &&
						request.getAttribute("codBibO") != null &&
						request.getAttribute("codOrd") != null &&
						request.getAttribute("prezzo") != null &&
						request.getAttribute("tipoOperazione") != null) {
					if (!request.getAttribute("codBibF").equals(currentForm.getCodBib())){
						currentForm.setCodBib((String)request.getAttribute("codBibF"));
					}
					currentForm.setCodBibF((String)request.getAttribute("codBibF"));
					currentForm.setAnnoFattura(Integer.valueOf((String)request.getAttribute("annoF")));
					currentForm.setProgrFattura(Integer.valueOf((String)request.getAttribute("prgF")));
					currentForm.setRigaFattura(Integer.valueOf((String)request.getAttribute("rigaFatt")));
					currentForm.setCodTipoOrd((String)request.getAttribute("codTipoOrd"));
					currentForm.setCodBibO((String)request.getAttribute("codBibO"));
					currentForm.setAnnoOrd(Integer.valueOf((String)request.getAttribute("annoOrd")));
					currentForm.setCodOrd(Integer.valueOf((String)request.getAttribute("codOrd")));
					currentForm.setPrezzo(Double.valueOf(request.getAttribute("prezzo").toString()));
					StrutturaCombo titoloOrdine = ((StrutturaCombo)request.getAttribute("titOrd"));
					currentForm.setBidOrd(titoloOrdine.getCodice());
					currentForm.setIsbdOrd(titoloOrdine.getDescrizione());
					currentForm.setTipoOperazione((String)request.getAttribute("tipoOperazione"));
					navi.setDescrizioneX("Lista Inventari Riga Fattura");
					navi.setTesto("Lista Inventari Riga Fattura");
					currentForm.setProv((String)request.getAttribute("prov"));
					listaInv = this.getListaInventariRigaFattura(currentForm.getCodPolo(), currentForm.getCodBib(),
							currentForm.getCodBibO(), currentForm.getCodTipoOrd(), currentForm.getAnnoOrd(), currentForm.getCodOrd(),
							currentForm.getCodBibF(), currentForm.getAnnoFattura(), currentForm.getProgrFattura(), currentForm.getRigaFattura(),
							this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), elementiBlocco, form);
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioFattura"));

					return navi.goBack(true);
				}
			}else if (request.getAttribute("tipoLista") != null && request.getAttribute("tipoLista").equals("listaInvDiColloc")){
				currentForm.setTipoLista("listaInvDiColloc");
				if(request.getAttribute("codBib") != null &&
						request.getAttribute("descrBib") != null &&
						request.getAttribute("paramRicerca") != null) {
					currentForm.setCodBib((String)request.getAttribute("codBib"));
					currentForm.setDescrBib((String)request.getAttribute("descrBib"));
					currentForm.setParamRicerca((EsameCollocRicercaVO)request.getAttribute("paramRicerca"));
					currentForm.setTipoLista("listaInvDiColloc");

					navi.setDescrizioneX("Lista Inventari di Collocazione");
					navi.setTesto("Lista Inventari di Collocazione");
					listaInv = ValidazioneDati.equals(request.getAttribute("prov"), "fascicolo") ?
							this.getListaInventariDiCollocazionePeriodico(request, currentForm.getParamRicerca(), elementiBlocco, form) :
							this.getListaInventariDiCollocazione(currentForm.getParamRicerca(), currentForm.getTicket(), elementiBlocco, form);
					if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("insColl"))){
						currentForm.setProv("insColl");
					} else if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("esaminaPosseduto"))){
						currentForm.setProv("esaminaPosseduto");
					} else if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("esamePeriodici"))){
						currentForm.setProv("esamePeriodici");
					} else if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("fascicolo"))){
						currentForm.setProv("fascicolo");
					}
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioListaInvDiColloc"));

					return navi.goBack(true);
				}
			}else if (request.getAttribute("tipoLista") != null && request.getAttribute("tipoLista").equals("listaInvDiCollocDaEsempl")){
				if(request.getAttribute("codBib") != null &&
						request.getAttribute("descrBib") != null &&
						request.getAttribute("paramRicerca") != null) {
					currentForm.setCodBib((String)request.getAttribute("codBib"));
					currentForm.setDescrBib((String)request.getAttribute("descrBib"));
					currentForm.setParamRicerca((EsameCollocRicercaVO)request.getAttribute("paramRicerca"));
					currentForm.setTipoLista("listaInvDiCollocDaEsempl");
					Navigation navigation =navi;
					navigation.setDescrizioneX("Lista Inventari di Collocazione");
					navigation.setTesto("Lista Inventari di Collocazione");
					listaInv = this.getListaInventariDiCollocazione(currentForm.getParamRicerca(),
							currentForm.getTicket(), elementiBlocco, form);
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreNelPassaggioListaInvDiCollocDaEsempl"));

					return navi.goBack(true);
				}
			}else if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("chiudi")){
				return mapping.getInputForward();
			}
			if (listaInv == null ||  listaInv.size() <1)  {

				currentForm.setNoInv(true);
				NavigationCache cache = navi.getCache();
				NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
				if (prev != null && prev.getForm() instanceof EsameCollocListeCollocazioniForm) {
					EsameCollocListeCollocazioniForm listaCollForm = (EsameCollocListeCollocazioniForm) prev.getForm();
					request.setAttribute("listaColl", "listaColl");
					request.setAttribute("paramRicerca", listaCollForm.getParamRicerca());
					request.setAttribute("codBib", listaCollForm.getCodBib());
					request.setAttribute("descrBib", listaCollForm.getDescrBib());
					return navi.goBack();
				}

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));

				return navi.goBack(true);
			}else{
				if (listaInv.size() == 1){
					currentForm.setSelectedInv("0");
				}
				currentForm.setNoInv(false);
				currentForm.setCodBib(currentForm.getCodBib());
				currentForm.setDescrBib(currentForm.getDescrBib());
				currentForm.setCodSez(currentForm.getParamRicerca().getCodSez());
				currentForm.setCodLoc(currentForm.getParamRicerca().getCodLoc());
				currentForm.setSpecLoc(currentForm.getParamRicerca().getCodSpec());
				if (request.getAttribute("prov") != null &&
						(request.getAttribute("prov").equals("ordine")
								|| request.getAttribute("prov").equals("ordineIns")
								|| request.getAttribute("prov").equals("listaSuppInvOrd")
								|| request.getAttribute("prov").equals("tastoOrdiniDaListaInvTitolo"))){
					this.popolaListaOrdini(listaInv, form);
				}
//				if (myForm.getTipoOperazione().equals("A") && myForm.getProv().equals("rigaFattura")){
//
//					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));
//
//				}
				currentForm.setListaInventari(listaInv);
				//se in modColl è cambiata sez o coll o spec
				//non ricarico la pagina degli inventari di collocazione che
				//dopo il cambio sarebbe modificata nei riferimanti della collocazione
				//e vado a ricaricare la pagina della lista collocazioni
				if (request.getAttribute("datiMappaModificati") != null){
					NavigationCache cache = navi.getCache();
					NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
					if (prev != null && prev.getForm() instanceof EsameCollocListeCollocazioniForm) {
						EsameCollocListeCollocazioniForm listaCollForm = (EsameCollocListeCollocazioniForm) prev.getForm();
						request.setAttribute("listaColl", "listaColl");
					}
					return navi.goBack();
				}
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return navi.goBack(true);
		} catch (DataException e) {
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaSuppInvOrd")){
				if (request.getAttribute("nuovo") != null) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//					return mapping.findForward("chiudi");
					return navi.goBack(true);
				}
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("descrBib", currentForm.getDescrBib());
				request.setAttribute("bid", currentForm.getBidOrd());
				request.setAttribute("titolo", currentForm.getIsbdOrd());
				request.setAttribute("passaggioListaSuppOrdiniVO", null);
				request.setAttribute("tastoOrdiniDaListaInvTitolo", null);
				request.setAttribute("prov", "listaInventariOrdine");
//				return Navigation.getInstance(request).goBack();
				return navi.goBack();
			}else if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("rigaFattura")){
				if (currentForm.getTipoOperazione().equals("A") && currentForm.getProv().equals("rigaFattura")){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariRigaNotFound"));

				}
				return this.nuovo(mapping, form, request, response);
			}else if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaSuppInvPoss")){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariPossessoriNotFound"));

				if (request.getAttribute("analitica") != null &&
						request.getAttribute("analitica").equals("analitica")){
					return navi.goBack();
				}
				request.setAttribute("messaggio", "error.documentofisico.inventariPossessoriNotFound");
				return navi.goBack(true);
			}else if (request.getAttribute("tipoLista") != null && request.getAttribute("tipoLista").equals("listaInvDiColloc")){
				NavigationCache cache = navi.getCache();
				NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
				if (prev != null && prev.getForm() instanceof EsameCollocListeCollocazioniForm) {
					EsameCollocListeCollocazioniForm listaCollForm = (EsameCollocListeCollocazioniForm) prev.getForm();
					request.setAttribute("listaColl", "listaColl");

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

					return navi.goBack();
				}

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return navi.goBack(true);
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return navi.goBack(true);
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

//			return mapping.findForward("chiudi");
			return navi.goBack(true);
		}
		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		try {


			Navigation navi = Navigation.getInstance(request);
			NavigationCache cache = navi.getCache();
			NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
			if (prev != null && prev.getForm() instanceof EsameCollocListeCollocazioniForm) {
				EsameCollocListeCollocazioniForm listaCollForm = (EsameCollocListeCollocazioniForm) prev.getForm();
				request.setAttribute("listaColl", "listaColl");
				request.setAttribute("paramRicerca", listaCollForm.getParamRicerca());
				request.setAttribute("codBib", listaCollForm.getCodBib());
				request.setAttribute("descrBib", listaCollForm.getDescrBib());
				//almaviva5_20101215 periodici
				if (ValidazioneDati.equals(myForm.getProv(), "esamePeriodici"))
					return navi.goBack(true);
				if (ValidazioneDati.equals(myForm.getProv(), "esaminaPosseduto")
						|| (ValidazioneDati.equals(myForm.getTipoLista(), "listaInvDiCollocDaEsempl"))) {
					return navi.goBack(true);
				}else{
					return navi.goBack(listaCollForm.isKardex());
				}
			}
			return navi.goBack(true);
//			return mapping.findForward("chiudi");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward esameInventario1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

			if (!myForm.isNoInv()){

				int invSel;
				request.setAttribute("checkS", myForm.getSelectedInv());
				String check = myForm.getSelectedInv();
				if (check != null && check.length() != 0) {
					invSel = Integer.parseInt(myForm.getSelectedInv());
					InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
					request.setAttribute("codBib",myForm.getCodBib());
					request.setAttribute("codSerie",scelInv.getCodSerie());
					request.setAttribute("codInvent",scelInv.getCodInvent());
					request.setAttribute("descrBib",myForm.getDescrBib());
					request.setAttribute("prov", "interrogazioneEsame");
					return mapping.findForward("esameInventario");
				} else {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

					return mapping.getInputForward();
				}

			} else {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noInv"));

			}

			return mapping.getInputForward();

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	public ActionForward esameInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession httpSession = request.getSession();

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {

			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent",String.valueOf(scelInv.getCodInvent()));
				request.setAttribute("descrBib",myForm.getDescrBib());

//				request.setAttribute("scelInv",scelInv);
				return mapping.findForward("esameInventario");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward possessori(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession httpSession = request.getSession();

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent",""+scelInv.getCodInvent());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("prov", "DOCFIS");

				//return mapping.getInputForward();
				return mapping.findForward("possessori");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward disponibilita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				InventarioTitoloVO invTit = new InventarioTitoloVO(scelInv);
				MovimentoVO movimento = new MovimentoVO();
				movimento.setCodPolo(myForm.getCodPolo());
				movimento.setCodBibOperante(myForm.getCodBib());
				movimento.setCodBibInv(scelInv.getCodBib());
				movimento.setCodSerieInv(scelInv.getCodSerie());
				movimento.setCodInvenInv(String.valueOf(scelInv.getCodInvent()));
				request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, movimento);
				InfoDocumentoVO infoDoc = new InfoDocumentoVO();
				infoDoc.setInventarioTitoloVO(invTit);
				request.setAttribute("InfoDocumentoVO", infoDoc);
				request.setAttribute("TipoRicerca", RicercaRichiesteType.RICERCA_PER_INVENTARIO);
				return mapping.findForward("SIFServizi");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward collocazioneDefinitiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent",scelInv.getCodInvent());
				request.setAttribute("descrBib",myForm.getDescrBib());

				if (ValidazioneDati.isFilled(scelInv.getKeyLocOld())) {
					request.setAttribute("prov", "collDef");
					return mapping.findForward("collocazioneDefinitiva");
				}else{

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noCollTemp"));

					return mapping.getInputForward();
				}

//				return mapping.findForward("collocazioneDefinitiva");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward etichetta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("codSerie",myForm.getCodSerie());
				request.setAttribute("codInvent",scelInv.getCodInvent());
				request.setAttribute("descrBib",myForm.getDescrBib());
				List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
				EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
				datiStampaEtichette.setBiblioteca(myForm.getDescrBib());
				datiStampaEtichette.setCollocazione(myForm.getCodLoc().trim());
				datiStampaEtichette.setInventario(new Integer(scelInv.getCodInvent()).toString());
				datiStampaEtichette.setPrecisazione(scelInv.getPrecInv());
				datiStampaEtichette.setSequenza(scelInv.getSeqColl().trim());
				datiStampaEtichette.setSerie(scelInv.getCodSerie());
				datiStampaEtichette.setSezione(myForm.getCodSez().trim());
				datiStampaEtichette.setSpecificazione(myForm.getSpecLoc().trim());
				listaEtichetteBarcode.add(datiStampaEtichette);
				request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
				request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
//				request.setAttribute("DATI_STAMPE_ON_LINE", datiStampaEtichette);//modifica per stampa barcode
				request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
				return  mapping.findForward("stampaSintetica");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward moduloPrelievo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm currentForm = (ListeInventariForm) form;

		String selected = currentForm.getSelectedInv();
		if (!ValidazioneDati.isFilled(selected))
			return mapping.getInputForward();

		int invSel = Integer.parseInt(selected);
		InventarioVO scelInv = (InventarioVO) currentForm.getListaInventari().get(invSel);

		request.setAttribute("FUNZIONE_STAMPA", StampaType.STAMPA_MODULO_PRELIEVO);
		request.setAttribute("DATI_STAMPE_ON_LINE", scelInv);

		return mapping.findForward("stampaSintetica");
	}

	public ActionForward modificaInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent", scelInv.getCodInvent());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("bid", scelInv.getBid());
				request.setAttribute("tipoLista", "listaInvDiColloc");

				return mapping.findForward("modificaInv");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward modificaColl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioListeVO scelInv = (InventarioListeVO) myForm.getListaInventari().get(invSel);
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("codSerie",scelInv.getCodSerie());
				request.setAttribute("codInvent",scelInv.getCodInvent());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("bid", scelInv.getBid());
				request.setAttribute("titolo", scelInv.getDescr());
				request.setAttribute("keyLoc", scelInv.getKeyLoc());

				return mapping.findForward("modificaColl");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			int invSel;
			request.setAttribute("checkS", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			if (check != null && check.length() != 0) {
				invSel = Integer.parseInt(myForm.getSelectedInv());
				InventarioVO scelInv = (InventarioVO) myForm.getListaInventari().get(invSel);
				scelInv.setCodBib(myForm.getCodBib());
				scelInv.setCodPolo(myForm.getCodPolo());
				scelInv.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				String tipoOp = "C";
				boolean ret = this.aggiornaInventarioFattura(scelInv, tipoOp, myForm.getTicket());{
					if (ret){
						request.setAttribute("codBibO", myForm.getCodBibO());
						request.setAttribute("codTipoOrd", myForm.getCodTipoOrd());
						request.setAttribute("annoOrd", (String.valueOf(myForm.getAnnoOrd())));
						request.setAttribute("codOrd", (String.valueOf(myForm.getCodOrd())));
				          StrutturaCombo tit = new StrutturaCombo(myForm.getBidOrd(), myForm.getIsbdOrd());
				          request.setAttribute("titOrd", tit);
						request.setAttribute("codBibF", myForm.getCodBibF());
						request.setAttribute("prezzo", (String.valueOf(myForm.getPrezzo())));
						request.setAttribute("codBibF", myForm.getCodBibF());
						request.setAttribute("annoF", (String.valueOf(myForm.getAnnoFattura())));
						request.setAttribute("prgF", (String.valueOf(myForm.getProgrFattura())));
						request.setAttribute("rigaFatt", (String.valueOf(myForm.getRigaFattura())));
						//sola visualizzazione - V fattura contabilizzata A fattura non contabilizzata
						request.setAttribute("tipoOperazione", "A" );
						request.setAttribute("prov", "rigaFattura");

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codiceCancellazioneEffettuata"));

						return unspecified(mapping, form, request, response);
					}
				}
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack();
		}
		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			  request.setAttribute("codBibO", myForm.getCodBibO());
	          request.setAttribute("codTipoOrd", myForm.getCodTipoOrd());
	          request.setAttribute("annoOrd", (String.valueOf(myForm.getAnnoOrd())));
	          request.setAttribute("codOrd", (String.valueOf(myForm.getCodOrd())));
			  request.setAttribute("codBibF", "");
	          request.setAttribute("prezzo", (String.valueOf(myForm.getPrezzo())));
	          StrutturaCombo tit = new StrutturaCombo(myForm.getBidOrd(), myForm.getIsbdOrd());
	          request.setAttribute("titOrd", tit);
	          request.setAttribute("bid", "");
	          request.setAttribute("titolo", "");
	          request.setAttribute("nuovo", "nuovo");
	          request.setAttribute("prov", "listaSuppInvOrd");
	          return unspecified(mapping, form, request, response);
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return Navigation.getInstance(request).goBack();
		}
	}

	private List popolaListaOrdini(List listaInv, ActionForm form) throws RemoteException  {

		ListeInventariForm myForm = (ListeInventariForm) form;
		List<InventarioListeVO> lista = new ArrayList<InventarioListeVO>();
		for (int index = 0; index < listaInv.size(); index++) {
			InventarioListeVO inv = (InventarioListeVO) listaInv.get(index);
			if (inv.getBid().equals(myForm.getBidOrd())){
				inv.setBid("");
				inv.setDescr("");
				inv.setSeparatore("");
			}
			lista.add(inv);
		}
		return lista;
	}
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ListeInventariForm myForm = (ListeInventariForm) form;
		try {

			request.setAttribute("check", myForm.getSelectedInv());
			String check = myForm.getSelectedInv();
			int invsel;
			if (!myForm.isNoInv()){
				if (check != null && check.length() != 0) {
//					List listaInv = (List) myForm.getListaInventari();
					invsel = Integer.parseInt(myForm.getSelectedInv());
					InventarioListeVO invLista = (InventarioListeVO) myForm.getListaInventari().get(invsel);
					Navigation navi = Navigation.getInstance(request);
					if (myForm.getTipoOperazione() != null && myForm.getTipoOperazione().equals("A")){
						if (myForm.getPrezzo() != invLista.getImportoDouble()){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prezziDiversiVuoiAllineareInventario"));
							request.setAttribute("recInv", invLista);

							this.saveToken(request);
							myForm.setConferma(true);
							return mapping.getInputForward();
						}else{
							//aggiorna inventario con i soli dati della rigaFattura
							invLista.setCodBib(myForm.getCodBib());
							invLista.setCodPolo(myForm.getCodPolo());
							invLista.setCodBibF(myForm.getCodBibO());
							invLista.setAnnoFattura(String.valueOf(myForm.getAnnoFattura()));
							invLista.setProgrFattura(String.valueOf(myForm.getProgrFattura()));
							invLista.setRigaFattura(String.valueOf(myForm.getRigaFattura()));
							invLista.setUteAgg(navi.getUtente().getFirmaUtente());
							String tipoOp = "R";
							this.controlloDatiFattura(invLista);
							boolean ret = this.aggiornaInventarioFattura(invLista, tipoOp, myForm.getTicket());
							if (ret){
								//devo ritornare alla pagina della fattura

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.aggEff"));

								return navi.goBack();
							}
						}
					}else{
						InventarioVO inv = new InventarioVO(invLista);
						//individuato l'inventario precisato da Ordini procedo con il recupero dell'inventario
						request.setAttribute("codBib", myForm.getCodBib());
						request.setAttribute("descrBib", myForm.getDescrBib());
						request.setAttribute("bidInv", invLista.getBid());
						request.setAttribute("titoloInv", invLista.getDescr());
						request.setAttribute("bidOrd", myForm.getBidOrd());
						request.setAttribute("titoloOrd", myForm.getIsbdOrd());
						request.setAttribute("bid", myForm.getBid());
						request.setAttribute("titolo", myForm.getIsbd());
						request.setAttribute("prov", "listaInventariOrdine");
						request.setAttribute("inventario", inv);
						//almaviva5_20101001 gest.periodici
						if (ValidazioneDati.equals(myForm.getProv(), "fascicolo")) {
							return navi.goToNearestBookmark(new String[] {
									PeriodiciDelegate.BOOKMARK_KARDEX,
									PeriodiciDelegate.BOOKMARK_FASCICOLO },
									navi.goBack(), false);
						} else
							return navi.goBack();
					}
//					return mapping.findForward("vaiAInserimentoInv");
				}else {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

					return mapping.getInputForward();
				}
			}
			return mapping.getInputForward();

		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * ListeInventariAction.java
	  * controlloDatiFattura
	  * void
	  * @param invLista
	  * @throws DataException
	  *
	  *
	 */
	private void controlloDatiFattura(InventarioListeVO invLista)
			throws DataException {
		if ((invLista.getAnnoFattura() != null && ValidazioneDati.strIsNumeric(invLista.getAnnoFattura()) && Integer.valueOf(invLista.getAnnoFattura()) > 0) &&
				(ValidazioneDati.strIsNumeric(invLista.getProgrFattura()) && Integer.valueOf(invLista.getProgrFattura()) > 0) &&
				(ValidazioneDati.strIsNumeric(invLista.getRigaFattura()) && Integer.valueOf(invLista.getRigaFattura()) > 0)){
		}else{
			throw new DataException("erroreValidazioneDatiFattura");
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		try{
			if (myForm.isConferma()){
			myForm.setConferma(false);
				//aggiorna l'inventario per modificarne l'importo
				InventarioListeVO inv = (InventarioListeVO)myForm.getListaInventari().get((Integer.valueOf(myForm.getSelectedInv())));
				inv.setCodBib(myForm.getCodBib());
				inv.setCodPolo(myForm.getCodPolo());
				inv.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());

				//aggiorno inv
				inv.setCodBib(myForm.getCodBib());
				inv.setCodPolo(myForm.getCodPolo());
				inv.setCodBibF(myForm.getCodBibO());
				inv.setAnnoFattura(String.valueOf(myForm.getAnnoFattura()));
				inv.setProgrFattura(String.valueOf(myForm.getProgrFattura()));
				inv.setRigaFattura(String.valueOf(myForm.getRigaFattura()));
				inv.setImportoDouble(myForm.getPrezzo());
				inv.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				String tipoOp = "I";
				this.controlloDatiFattura(inv);
				boolean ret = this.aggiornaInventarioFattura(inv, tipoOp, myForm.getTicket());
				if (ret){
					//devo ritornare alla pagina della fattura

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.aggEff"));

					return Navigation.getInstance(request).goBack();
				}
			}
		}	catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		try {
			if (myForm.isConferma()){
			myForm.setConferma(false);
				//aggiorna inventario con i soli dati della rigaFattura
				InventarioListeVO inv = (InventarioListeVO)myForm.getListaInventari().get((Integer.valueOf(myForm.getSelectedInv())));
				inv.setCodBib(myForm.getCodBib());
				inv.setCodPolo(myForm.getCodPolo());
				inv.setCodBibF(myForm.getCodBibO());
				inv.setAnnoFattura(String.valueOf(myForm.getAnnoFattura()));
				inv.setProgrFattura(String.valueOf(myForm.getProgrFattura()));
				inv.setRigaFattura(String.valueOf(myForm.getRigaFattura()));
				inv.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				String tipoOp = "R";
				this.controlloDatiFattura(inv);
				boolean ret = this.aggiornaInventarioFattura(inv, tipoOp, myForm.getTicket());
				if (ret){
					//devo ritornare alla pagina della fattura

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.aggEff"));

					return Navigation.getInstance(request).goBack();
				}
			}
		}	catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return Navigation.getInstance(request).goBack(true);
	}
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!myForm.isSessione()) {
			myForm.setSessione(true);
		}
		int numBlocco = 0;
		if (myForm.isCaricoAltriBlocchi()){
			myForm.setBloccoSelezionato(myForm.getBloccoSelezionato());
		}
		numBlocco = myForm.getBloccoSelezionato();
		String idLista = myForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				if (myForm.getProv() != null &&
						(myForm.getProv().equals("ordine") || myForm.getProv().equals("ordineIns"))){
					this.popolaListaOrdini(bloccoVO.getLista(), form);
				}
				if (numBlocco == myForm.getTotBlocchi()){
					myForm.setBloccoSelezionato(numBlocco + 1);
				}
				ricalcolaProgressiviBlocco(myForm, bloccoVO);

				myForm.getListaInventari().addAll(bloccoVO.getLista());

			}else{
				if (myForm.isCaricoAltriBlocchi()){
					myForm.getListaInventari().addAll(myForm.getNewLista());
					myForm.setCaricoAltriBlocchi(false);
					myForm.setBloccoSelezionato(numBlocco + 1);
					return mapping.getInputForward();
				}else{
					return mapping.getInputForward();
				}
			}
			//
			//quando si chiede di caricare l'ultimo blocco controllo se ci sono altre collocazioni non ancora estratte dal db
			//su un calcolo: myForm.getNumBlocchi * myForm.getNumRighe, se il prodotto è < di myForm.getTotRighe parte la ricerca
			//delle righe rimanenti che verranno appese all'ultimo blocco trattato
			if (numBlocco == myForm.getTotBlocchi()){
				int totElementi = myForm.getElemPerBlocchi() * myForm.getBloccoSelezionato();
				if ((totElementi >= myForm.getTotRighe())){
					myForm.setBloccoSelezionato(numBlocco);
					myForm.setAbilitaBottoneCarBlocchi(false);
					return mapping.getInputForward();
				}
				myForm.setCaricoAltriBlocchi(true);
				myForm.getParamRicerca().setUltLoc(myForm.getUltCodSerie());
				myForm.getParamRicerca().setUltSpec(myForm.getUltCodInv());

				myForm.getParamRicerca().setOldIdLista(idLista);
				myForm.getParamRicerca().setUltimoBlocco(numBlocco);
				List<?> listaInv = this.getListaInventariPossessori(myForm.getCodPolo(), myForm.getCodBib(),
						myForm.getPid(), myForm.getTicket(), myForm.getNRec(), form, myForm.getParamRicerca());
				if (listaInv != null && listaInv.size() > 0){
					myForm.setCaricoAltriBlocchi(false);
					myForm.setOldLista(listaInv);
				}
			}
		}
		Collections.sort(myForm.getListaInventari());
		return mapping.getInputForward();
	}

	private List getListaInventariNonCollocati(EsameCollocRicercaVO paramRicerca, String ticket, int nRec, ActionForm form) throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariNonCollocati(paramRicerca, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariOrdine(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd,
			int codOrd, String codBibF, Locale locale, String ticket, int nRec, ActionForm form)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariOrdine(codPolo, codBib, codBibO, codTipOrd, annoOrd, codOrd, codBibF, locale, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariOrdini(List listaOrdini, Locale locale, String ticket, int nRec, ActionForm form)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariOrdini(listaOrdini, locale, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariOrdiniNonFatturati(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd,
			int codOrd, String codBibF, Locale locale, String ticket, int nRec, ActionForm form)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariOrdiniNonFatturati(codPolo, codBib, codBibO, codTipOrd, annoOrd, codOrd, codBibF, locale, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariFattura(String codPolo, String codBibF, int annoF, int progF, Locale locale,
			String ticket, int nRec, ActionForm form)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariFattura(codPolo, codBibF, annoF, progF, locale, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariRigaFattura(String codPolo, String codBib, String codBibO, String codTipoOrd, int annoOrd, int codOrd,
			String codBibF, int annoF, int prgF, int rigaF, Locale locale, String ticket, int nRec, ActionForm form)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariRigaFattura(codPolo, codBib, codBibO, codTipoOrd, annoOrd, codOrd,
				codBibF, annoF, prgF, rigaF, locale, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariDiCollocazione(EsameCollocRicercaVO paramRicerca, String ticket, int nRec, ActionForm form)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariDiCollocazione(paramRicerca, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			myForm.setNoInv(true);
			return null;
		}else{
			myForm.setNoInv(false);
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
	private List getListaInventariPossessori(String codPolo, String codBib, String pid, String ticket, int nRec, ActionForm form, EsameCollocRicercaVO paramRicerca)
	throws Exception {
		ListeInventariForm myForm = (ListeInventariForm) form;
		DescrittoreBloccoVO blocco1;
		Object tracciato = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariPossessori(codPolo, codBib, pid, ticket, nRec, paramRicerca);
		return trattaBlocchi(myForm, blocco1);
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * ListeInventariAction.java
	  * trattaBlocchi
	  * List
	  * @param myForm
	  * @param blocco1
	  * @return
	  *
	  *
	 */
	private List trattaBlocchi(ListeInventariForm myForm,
			DescrittoreBloccoVO blocco1) {
		Object tracciato;
		tracciato = blocco1;
		if (tracciato instanceof DescrittoreBloccoNumCollVO){
			DescrittoreBloccoNumCollVO descrBloccoNum = (DescrittoreBloccoNumCollVO)blocco1;
			if (myForm.getTotRighe() == 0){
				myForm.setTotRighe(descrBloccoNum.getCountColl());
			}
			myForm.setUltCodSerie(descrBloccoNum.getUltLoc());
			myForm.setUltCodInv(descrBloccoNum.getUltSpec());
		}
		if (myForm.getTotRighe() == 0){
			myForm.setTotRighe(blocco1.getTotRighe());
		}
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			if (myForm.isCaricoAltriBlocchi()){
				myForm.setNoInv(true);
				return null;
			}else{
				return impostaCampiBlocco(myForm, blocco1, myForm.getTotRighe());
			}
		}else{
			if (myForm.isCaricoAltriBlocchi()){
				myForm.getParamRicerca().setUltLoc(null);
				myForm.getParamRicerca().setUltSpec(null);
				myForm.getParamRicerca().setUltKeyLoc(0);
				myForm.setNoInv(false);
				myForm.setIdLista(blocco1.getIdLista());
				myForm.setBloccoSelezionato(blocco1.getNumBlocco() - 1);
				myForm.setElemPerBlocchi(blocco1.getMaxRighe());
				//
				myForm.setBlocchiTotali((int) (Math.ceil((double) myForm.getTotRighe() / (double) myForm.getNRec())));
				if (myForm.getTotBlocchi() > 0){
					if (myForm.getTotBlocchi() != myForm.getBlocchiTotali()){
						myForm.setTotBlocchi(blocco1.getTotBlocchi() + myForm.getTotBlocchi());
						myForm.setAbilitaBottoneCarBlocchi(true);
					}else{
						myForm.setTotBlocchi(myForm.getTotBlocchi());
						myForm.setAbilitaBottoneCarBlocchi(false);
					}
				}else{
					myForm.setTotBlocchi(blocco1.getTotBlocchi() + myForm.getTotBlocchi());
					myForm.setAbilitaBottoneCarBlocchi(true);
				}
				return blocco1.getLista();
			}else{
				return impostaCampiBlocco(myForm, blocco1, myForm.getTotRighe());
			}
		}
	}
	private boolean aggiornaInventarioFattura(InventarioVO recInv, String tipoOp, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().aggiornaInventarioFattura(recInv, tipoOp, ticket);
		return ret;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		ListeInventariForm myForm = (ListeInventariForm) form;
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			if (idCheck.equalsIgnoreCase("possessori") ){
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (idCheck.equalsIgnoreCase("etichette") ){
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	private List<?> impostaCampiBlocco(ListeInventariForm myForm,
			DescrittoreBloccoVO blocco1, int totRighe) {
		myForm.setNoInv(false);
		myForm.setIdLista(blocco1.getIdLista());
		myForm.setTotBlocchi(blocco1.getTotBlocchi());
		myForm.setTotRighe(totRighe);
		myForm.setBloccoSelezionato(blocco1.getNumBlocco());
		myForm.setElemPerBlocchi(blocco1.getMaxRighe());
		if (blocco1.getTotBlocchi() > 1){
			myForm.setAbilitaBottoneCarBlocchi(false);
		}else{
			myForm.setAbilitaBottoneCarBlocchi(true);
		}
		return blocco1.getLista();
	}
	/**
	  * it.iccu.sbn.web.actions.documentofisico.esameCollocazioni
	  * EsameCollocListeCollocazioniAction.java
	  * ricalcolaProgressiviBlocco
	  * void
	  * @param myForm
	  * @param bloccoVO
	  *
	  *
	 */
	private void ricalcolaProgressiviBlocco(
			ListeInventariForm myForm,
			DescrittoreBloccoVO bloccoVO) {
		//ricalcola i progressivi
		int ultProg = myForm.getListaInventari().size();
		InventarioListeVO ultRec = (InventarioListeVO)myForm.getListaInventari().get(ultProg - 1);
		int nuovoPrg =  (bloccoVO.getMaxRighe() * (bloccoVO.getNumBlocco() - 1)) + 1;//ultRec.getPrg() + 1;
		int sizeBlocco = bloccoVO.getLista().size();
		if (sizeBlocco > 0){
			for (int y = 0; y < sizeBlocco; y++) {
				InventarioListeVO rec = (InventarioListeVO) bloccoVO.getLista().get(y);
				rec.setPrg(nuovoPrg + y);
			}
		}
	}

	private List getListaInventariDiCollocazionePeriodico(HttpServletRequest request, EsameCollocRicercaVO paramRicerca,
		int nRec, ActionForm form) throws Exception {
		ListeInventariForm currentForm = (ListeInventariForm) form;
		List<InventarioListeVO> lista = new ArrayList<InventarioListeVO>();
		log.debug("Inv. fascicolo per annata: " + paramRicerca.getAnnoAbb());
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		InventariCollocazioneDecorator icd = (InventariCollocazioneDecorator) delegate.getListaInventariDiCollocazione(paramRicerca, nRec);
		DescrittoreBloccoVO blocco1 = icd.getBlocco();
		if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
			currentForm.setNoInv(true);
			return null;
		} else {
			currentForm.setNoInv(false);
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1))
				currentForm.setAbilitaBottoneCarBlocchi(false);
			else
				currentForm.setAbilitaBottoneCarBlocchi(true);

			lista.addAll(blocco1.getLista());

			int primoInv = icd.getPrgPrimoInventarioAnnoAbb();

			if (primoInv > 0) {
				currentForm.setSelectedInv((primoInv - 1) + "");
				Navigation.getInstance(request).getCache().getCurrentElement().setAnchorId(primoInv + "");
				request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + primoInv);
				//carico i blocchi fino a quello desiderato
				int next = 1;
				while (primoInv > lista.size()) {
					DescrittoreBloccoVO blocco = delegate.nextBlocco(blocco1.getIdLista(), ++next);
					lista.addAll(blocco.getLista());
					currentForm.setBloccoSelezionato(next);
				}
			}
		}

		return lista;
	}

}
