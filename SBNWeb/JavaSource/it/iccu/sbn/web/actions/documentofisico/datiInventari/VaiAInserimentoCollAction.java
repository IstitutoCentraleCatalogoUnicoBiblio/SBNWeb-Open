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
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneReticoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioniUltCollSpecVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.sif.AttivazioneSIFListaClassiCollegateVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.DocumentoFisicoFormTypes;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.VaiAInserimentoCollForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.NavigazioneDocFisico;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class VaiAInserimentoCollAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker  {

	private static final String AL_TITOLO_PARTICOLARE = "alTitPart";
	private static final String AL_TITOLO_GENERALE = "alTitGen";
	private static final String AL_TITOLO_A_LIVELLI = "alTitLiv";

	private static Logger log =  Logger.getLogger(VaiAInserimentoCollAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.insCollTab1", "tab1");
		map.put("documentofisico.insCollTab2", "tab2");
		map.put("documentofisico.insCollTab3", "tab3");
		map.put("documentofisico.lsProven", "listaSupportoProven");
		map.put("documentofisico.bottone.sceltaColloc", "sceltaColl");
		map.put("documentofisico.bottone.esEsempl", "esEsempl");
		map.put("documentofisico.bottone.inventari", "inventari");
		map.put("documentofisico.bottone.sceltaTit", "sceltaTit");
		map.put("documentofisico.bottone.avanti", "ok");
		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.lsSez", "listaSupportoSez");
		map.put("documentofisico.lsCl", "listaSupportoClassi");
		map.put("documentofisico.lsUC", "listaUltColl");
		map.put("documentofisico.lsUS", "listaUltSpec");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("button.blocco", "blocco");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {


		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		String provenienza = (String) request.getAttribute("prov");
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){

			try {
				currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				String cdSerieDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_SERIE);
				if (!ValidazioneDati.strIsEmpty(cdSerieDefault.toUpperCase()) ) {
					if (cdSerieDefault.trim().equals("")){
						cdSerieDefault = ValidazioneDati.fillRight("", ' ', 3);
					}
					SerieVO serieDettaglio = this.getSerieDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(),
							cdSerieDefault.toUpperCase(),	currentForm.getTicket());

					if (serieDettaglio == null) {
						//errors.clear();
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.serieDefaultNonEsiste"));

						return Navigation.getInstance(request).goBack(true);
					} else {
						currentForm.setCodSerie(cdSerieDefault.toUpperCase());
					}

				} else {
					if (provenienza != null && provenienza.equals("CV")) {
						//errors.clear();
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.serieDefaultNonImpostata"));

						return Navigation.getInstance(request).goBack(true);
					}
				}

				String tipoPrgDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_TP_PRG_INV);
				if (!ValidazioneDati.strIsEmpty(tipoPrgDefault)) {
					if (provenienza != null && provenienza.equals("CV")) {
						currentForm.setTipoPrgDefault((DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE));
					}else{
						currentForm.setTipoPrgDefault(tipoPrgDefault);
					}
				} else {
					if (provenienza != null && provenienza.equals("CV")) {
						//errors.clear();
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.tipoPrgDefaultNonImpostata"));

						return Navigation.getInstance(request).goBack(true);
					}
				}

				String cdSezDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_SEZ_COLL);
				if (!ValidazioneDati.strIsEmpty(cdSezDefault)) {
					if (cdSezDefault.trim().equals("")){
						cdSezDefault = ValidazioneDati.fillRight("", ' ', 10);
					}
					SezioneCollocazioneVO sezioneDettaglio = null;
					try {
						sezioneDettaglio = this.getSezioneDettaglio(currentForm.getCodPolo(),
								currentForm.getCodBib(), cdSezDefault,
								currentForm.getTicket());
					} catch (Exception e) {
						if (e.getMessage().equals("recSezioneInesistente"))
							log.warn("sezione default non esistente");
						sezioneDettaglio = null;
					}

					if (sezioneDettaglio == null) {
						//errors.clear();
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneDefaultNonEsiste"));

						return null;
					} else {
						currentForm.setRecSezione(sezioneDettaglio);
						currentForm.setCodSez(cdSezDefault);
						currentForm.setCodPoloSez(sezioneDettaglio.getCodPolo());
						currentForm.setCodBibSez(sezioneDettaglio.getCodBib());
						currentForm.setCodSez(sezioneDettaglio.getCodSezione().trim());
						currentForm.setTipoColl(sezioneDettaglio.getTipoColloc());
						currentForm.setDescrTipoCollocazione(sezioneDettaglio.getDescrTipoColl());
						currentForm.setDescrSez(sezioneDettaglio.getDescrSezione().trim());
						currentForm.setNoSezione("siSezione");
						currentForm.setRecFormatiSezioni(null);
						currentForm.setRecFormatiSezioni(null);

						currentForm.setLivello1(null);
						currentForm.setLivello2(null);
						currentForm.setLivello3(null);
						preparaFormPerTipoColloc(request, form, currentForm);
						//bug 0004232 collaudo
						if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
							String bid = currentForm.getBid();
							if (currentForm.getTipoColl() != null && currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
								//almaviva5_20111026 #4688
								associaClasse(currentForm, bid);
							}
						}
							//
						currentForm.setFolder("tab1");
					}
				} else {
					// sezione di default non impostata
					if (provenienza != null && provenienza.equals("CV")) {
						//errors.clear();
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneDefaultNonImpostata"));

					}
					//return null;
				}
				if (provenienza != null && provenienza.equals("CV")) {
					String catFruiDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CAT_FRUIZIONE);
					if (!ValidazioneDati.strIsEmpty(catFruiDefault)) {
						currentForm.setCatFruiDefault(catFruiDefault);
					} else {
						//				// sezione di default non impostata
						//				errors.clear();
						//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.catFruiDefaultNonImpostata"));
						//
						//				return null;
					}
					String tipoAcqDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_TP_ACQUISIZIONE);
					if (!ValidazioneDati.strIsEmpty(tipoAcqDefault)) {
						currentForm.setTipoAcqDefault(tipoAcqDefault);
					} else {
						// sezione di default non impostata
						//				errors.clear();
						//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.tipoAcqDefaultNonImpostata"));
						//
						//				return null;
					}
					String codNoDispDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_NO_DISPONIBILITA);
					if (!ValidazioneDati.strIsEmpty(codNoDispDefault)) {
						currentForm.setCodNoDispDefault(codNoDispDefault);
					} else {
						// sezione di default non impostata
						//				errors.clear();
						//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codNoDispDefaultNonImpostata"));
						//
						//				return null;
					}
					String codStatoConsDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_STATO_CONSERVAZIONE);
					if (!ValidazioneDati.strIsEmpty(codStatoConsDefault)) {
						currentForm.setCodStatoConsDefault(codStatoConsDefault);
					} else {
						// sezione di default non impostata
						//				errors.clear();
						//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codStatoConsDefaultNonImpostata"));
						//
						//				return null;
					}
					String codProvDefault = (String) utenteEjb.getDefault(ConstantDefault.GDF_CD_PROVENIENZA);
					if (!ValidazioneDati.strIsEmpty(codProvDefault)) {
						ProvenienzaInventarioVO provInv = this.getProvenienza(currentForm.getCodPolo(), currentForm.getCodBib(),
								codProvDefault,	currentForm.getTicket());

						if (provInv == null) {
							//errors.clear();
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.provenienzaDefaultNonEsiste"));

							//return Navigation.getInstance(request).goBack(true);
						}else{
							currentForm.setCodPoloProvDefault(provInv.getCodPolo());
							currentForm.setCodBibProvDefault(provInv.getCodBib());
							currentForm.setCodProvDefault(provInv.getCodProvenienza());
							currentForm.setDescrProven(provInv.getDescrProvenienza());
						}
					} else {
						// sezione di default non impostata
						//				errors.clear();
						//				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codProvDefaultNonImpostata"));
						//
						//				return null;
					}
				}
			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

				return mapping.getInputForward();
			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

				return Navigation.getInstance(request).goBack(true);
			}
			return null;
		}else{
			return null;
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm)form;

		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		currentForm.setDisableTastoAvanti(false);
		currentForm.setDisable(false);
		currentForm.setDisablePerModInvDaNav(false);
		Navigation navi = Navigation.getInstance(request);
		if (Navigation.getInstance(request).isFromBar() ){
			NavigationCache cache = navi.getCache();
			NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
			NavigationElement post = cache.getElementAt(cache.getCurrentPosition() + 1);
			if (prev != null && (prev.getName().equals("vaiAListaInventariTitoloForm")
					|| prev.getName().equals("vaiAInserimentoInvForm") || prev.getName().equals("analiticaTitoloForm"))
					&& (post == null || (post!= null && post.getDescrizione().equals("Lista Ultime Collocazioni") ||
							post.getDescrizione().equals("Lista Ultime Specificazioni")
							|| post.getDescrizione().equals("Lista Sezioni di Collocazione")))){
					currentForm.setDisableTastoAvanti(false);
					currentForm.setDisable(false);
					return mapping.getInputForward();
			}else{
				if (currentForm.getTipoColl() != null && (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE) ||
								(request.getAttribute("listaSezioni") != null))){
					currentForm.setDisableTastoAvanti(true);
					currentForm.setDisable(true);
					currentForm.setDisablePerModInvDaNav(true);
				}
//				if (request.getAttribute("listaSezioni") != null){
//					currentForm.setDisableTastoAvanti(false);
//					currentForm.setDisable(false);
//				}
			}
		}

		if (request.getAttribute("fineCV") != null ){
			return navi.goBack(true);
		}
		if (Navigation.getInstance(request).isFromBar() ){
			//per non visualizzare i -1 ma ritornando con la Navigation deve reimpostarli
			if (currentForm.getRecFormatiSezioni() != null && currentForm.getRecFormatiSezioni().getProgNum() == -1){
				currentForm.getRecFormatiSezioni().setProgNum(Integer.valueOf(0));
			}
			if (currentForm.getRecFormatiSezioni() != null && currentForm.getRecFormatiSezioni().getProgSerie() == -1){
				currentForm.getRecFormatiSezioni().setProgSerie(Integer.valueOf(0));
			}
			if (currentForm.getRecFormatiSezioni() != null && currentForm.getRecFormatiSezioni().getPrg() == -1){
				currentForm.getRecFormatiSezioni().setPrg(Integer.valueOf(0));
			}
			return mapping.getInputForward();
		}
		try{
			if(!currentForm.isSessione())	{
				currentForm.setTicket(navi.getUserTicket());
				currentForm.setCodPolo(navi.getUtente().getCodPolo());
				currentForm.setCodBib(navi.getUtente().getCodBib());
				currentForm.setDescrBib(navi.getUtente().getBiblioteca());
				navi.addBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL);
				currentForm.setSessione(true);
				if (!this.checkAttivita(request, currentForm, "df")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.utenteNonAbilitato"));

					return navi.goBack(true);
				}
				//				if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
				//					currentForm.setProv("CV");
				if (request.getAttribute("codBib") != null  &&
						request.getAttribute("descrBib") != null &&
						request.getAttribute("bid") != null &&
						request.getAttribute("titolo") != null ){
					currentForm.setCodBib((String)request.getAttribute("codBib"));
					currentForm.setDescrBib((String)request.getAttribute("descrBib"));
					currentForm.setBid((String)request.getAttribute("bid"));
					List lista = this.getTitolo((String)request.getAttribute("bid"), currentForm.getTicket());
					TitoloVO titolo = null;
					if (lista != null){
						if (lista.size() == 1){
							titolo = (TitoloVO)lista.get(0);
						}
					}else{
						throw new ValidationException("titNotFoundPolo", ValidationException.errore);
					}
					currentForm.setBid(titolo.getBid());
					currentForm.setTitolo(titolo.getIsbd());
					currentForm.setTerzaParte(titolo.getTerzaParte());
					//
					currentForm.setBidDaTit(titolo.getBid());
					currentForm.setIsbdDaTit(titolo.getIsbd());
					//						currentForm.getRecColl().setBid(titolo.getBid());
					currentForm.setTerzaParte(titolo.getTerzaParte());

				}
				//				}

				ActionForward loadDefault = loadDefault(request, mapping, form);
				if (loadDefault != null){
					return loadDefault;
				}
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("CV")){
				currentForm.setProv((String)request.getAttribute("prov"));
				//inserisce inventario
				int nInv = 1;
//				String tipoOperazione = DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE;
				boolean conferma = false;

				InventarioVO inv = new InventarioVO();
				inv.setCodPolo(currentForm.getCodPolo());
				inv.setCodBib(currentForm.getCodBib());
				inv.setCodSerie(currentForm.getCodSerie());
//				inv.setBid((String)request.getAttribute("bid"));
				inv.setBid(currentForm.getBid());
				inv.setDataIngresso(DateUtil.formattaDataOra(DaoManager.now()));
				inv.setPrecInv("");
				inv.setUteIns(navi.getUtente().getFirmaUtente());
				List invCreati = this.insertInventario(inv, currentForm.getTipoPrgDefault(),
						nInv, this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket(), form);
				if (invCreati == null ||  invCreati.size() <1)  {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erratoInserimento"));

					this.saveToken(request);
					return navi.goBack(true);
				}else{
					//imposta i dati dell'inventario
					currentForm.setRecInv((InventarioVO)invCreati.get(0));
					currentForm.setCodSerie(currentForm.getRecInv().getCodSerie());
					currentForm.setCodInvent(currentForm.getRecInv().getCodInvent());
					//
					localizzaGestione(request, currentForm);
					//
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					CaricamentoCombo caricaCombo = new CaricamentoCombo();
					currentForm.setListaTipoFruizione(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE,true)));
					currentForm.setListaMatCons(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,true)));
					currentForm.setListaStatoCons(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));
					currentForm.setListaRiproducibilita(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM,true)));
					currentForm.setListaSupportoCopia(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_COPIA,true)));
					currentForm.setListaTipoOrdine(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,true)));
					currentForm.setListaCodNoDispo(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_NON_DISPONIBILITA,true)));


					//setto nel recInv i valori di default
					currentForm.getRecInv().setCodFrui(currentForm.getCatFruiDefault() != null ? currentForm.getCatFruiDefault(): " ");
					currentForm.getRecInv().setTipoAcquisizione(currentForm.getTipoAcqDefault() != null ? currentForm.getTipoAcqDefault() : " ");
					currentForm.getRecInv().setCodTipoOrd(currentForm.getTipoAcqDefault() != null ? currentForm.getTipoAcqDefault() : " ");
					currentForm.getRecInv().setCodNoDisp(currentForm.getCodNoDispDefault() != null ? currentForm.getCodNoDispDefault() : " ");
					currentForm.getRecInv().setStatoConser(currentForm.getCodStatoConsDefault() != null ? currentForm.getCodStatoConsDefault() : " ");

					currentForm.getRecInv().setCodPoloProven(currentForm.getCodPoloProvDefault() != null ? currentForm.getCodPoloProvDefault() : "");
					currentForm.getRecInv().setCodBibProven(currentForm.getCodBibProvDefault() != null ? currentForm.getCodBibProvDefault() : "");
					currentForm.getRecInv().setCodProven(currentForm.getCodProvDefault() != null ? currentForm.getCodProvDefault() : "");
					currentForm.getRecInv().setDescrProven(currentForm.getDescrProven() != null ? currentForm.getDescrProven() : "");

//					currentForm.getRecInv().setDescrProven(currentForm.getCodProvDefault() != null ? currentForm.getCodProvDefault() : "");
//					currentForm.setDescrProven(currentForm.getRecInv().getDescrProven());

					currentForm.getRecInv().setDataInsPrimaColl(DateUtil.formattaData(new Date(System.currentTimeMillis())));

//					currentForm.getRecInv().setCodFrui(currentForm.getRecInv().getCodFrui().trim());
					if (currentForm.getRecInv().getCodMatInv() != null && currentForm.getRecInv().getCodMatInv().trim().equals("")){
						currentForm.getRecInv().setCodMatInv("VM");
					}else{
						currentForm.getRecInv().setCodMatInv(currentForm.getRecInv().getCodMatInv().trim());
					}
//					currentForm.getRecInv().setCodMatInv(currentForm.getRecInv().getCodMatInv().trim());
//					currentForm.getRecInv().setCodTipoOrd(currentForm.getRecInv().getCodTipoOrd().trim());
//					currentForm.getRecInv().setCodNoDisp(currentForm.getRecInv().getCodNoDisp().trim());
//					currentForm.getRecInv().setStatoConser(currentForm.getRecInv().getStatoConser().trim());
					currentForm.getRecInv().setCodRiproducibilita(currentForm.getRecInv().getCodRiproducibilita().trim());
					currentForm.getRecInv().setSupportoCopia(currentForm.getRecInv().getSupportoCopia().trim());
					currentForm.setDisableDataIns(true);
				}
				currentForm.setLivColl(AL_TITOLO_PARTICOLARE);
				Navigation navigation =navi;
				navigation.setDescrizioneX("Inserimento Collocazione Veloce");
				navigation.setTesto("Inserimento Collocazione Veloce");
			}
			if (request.getAttribute("codProven")!=null){
				currentForm.setDescrProven((String)request.getAttribute("descrProven"));
				currentForm.getRecInv().setCodProven((String)request.getAttribute("codProven"));
				currentForm.getRecInv().setCodPoloProven((String)request.getAttribute("codPoloProven"));
				currentForm.getRecInv().setCodBibProven((String)request.getAttribute("codBibProven"));
			}
			if (request.getAttribute("classe") != null){
				currentForm.setCodCollocazione((String)request.getAttribute("classe"));
				return mapping.getInputForward();
			}

			if (request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA)!= null) {
				SezioneCollocazioneVO sez = (SezioneCollocazioneVO)(request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA));
				currentForm.setCodPoloSez(sez.getCodPolo());
				currentForm.setCodBibSez(sez.getCodBib());
				currentForm.setCodSez(sez.getCodSezione().trim());
				//
				SezioneCollocazioneVO sezioneDettaglio = this.getSezioneDettaglio(currentForm.getCodPoloSez(),
						currentForm.getCodBibSez(), currentForm.getCodSez(),
						currentForm.getTicket());
				currentForm.setRecSezione(sezioneDettaglio);
				//
				currentForm.setTipoColl(sez.getTipoColloc());
				currentForm.setDescrTipoCollocazione(sez.getDescrTipoColl());
				currentForm.setDescrSez(sez.getDescrSezione().trim());
				currentForm.setNoSezione("siSezione");
				currentForm.setRecFormatiSezioni(null);

				currentForm.setLivello1(null);
				currentForm.setLivello2(null);
				currentForm.setLivello3(null);
				this.loadTab1(request, form);
				currentForm.setFolder("tab1");
			}
			//			ritorno da lente Collocazione
			if (request.getAttribute("listaUltColl")!= null && request.getAttribute("listaUltColl") != null){
				CollocazioniUltCollSpecVO coll = (CollocazioniUltCollSpecVO)request.getAttribute("scelColl");
				currentForm.setCodCollocazione(coll.getCodColloc());
				currentForm.setCodSpecificazione("");
				return mapping.getInputForward();
			}
			//			ritorno da lente Specificazione
			if (request.getAttribute("listaUltSpec")!= null && request.getAttribute("listaUltSpec") != null){
				CollocazioniUltCollSpecVO spec = (CollocazioniUltCollSpecVO)request.getAttribute("scelColl");
				currentForm.setCodSpecificazione(spec.getSpecColloc());
				return mapping.getInputForward();
			}
			if (Navigation.getInstance(request).isFromBar()  || request.getParameter("LISTA_ULT_SPEC") != null
					|| request.getParameter("LISTA_ULT_COLL") != null)
				return mapping.getInputForward();
			// controllo se ho già i dati in sessione;
			if (navi.isFromBar()){
				return mapping.getInputForward();
			}
			if((request.getAttribute("codBib") != null
					&& request.getAttribute("codSerie") != null
					&& request.getAttribute("codInvent") != null)){
				//per visualizzare la pagina mi servono i dati dell'inventario, i dati del reticolo
				this.loadPagina(request, mapping, form);
				if (currentForm.getProv() != null && currentForm.getProv().equals("CV")){
					currentForm.setLivColl(AL_TITOLO_PARTICOLARE);
				}
				currentForm.setFolder("tab1");
				currentForm.setSessione(true);
			}
			//carico il reticolo per costrire tab2 e tab 3

			if (currentForm.getReticolo() == null){
				currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBid(), currentForm.getTicket(), form));
			}

			String bidAlTitGen = null;
			if (ValidazioneDati.in(currentForm.getLivColl(), AL_TITOLO_GENERALE)){
				bidAlTitGen = currentForm.getReticolo().getTitoloGenerale().getBid();
				if (bidAlTitGen != null){
					currentForm.getRecColl().setBid(bidAlTitGen);
					//deve ripulire la specificazione
					if (currentForm.getChiave().equals("chiaveAutore") || currentForm.getChiave().equals("chiaveTitolo")){
						currentForm.setCodSpecificazione("");
					}
					if (currentForm.getTipoColl() != null && currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
						associaClasse(currentForm, currentForm.getRecColl().getBid());
					}
				}
				if (currentForm.getTipoColl() != null &&
						currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CHIAVE_TITOLO)) {
					currentForm.setCodCollocazione("");
				}
			}
			String bidAlTitPart = null;
			if (ValidazioneDati.in(currentForm.getLivColl(), AL_TITOLO_PARTICOLARE, AL_TITOLO_A_LIVELLI) ){
				bidAlTitPart = currentForm.getBidDaTit();
				currentForm.getRecColl().setBid(bidAlTitPart);
				//deve ripulire la specificazione
				if (currentForm.getChiave().equals("chiaveAutore") || currentForm.getChiave().equals("chiaveTitolo")){
					currentForm.setCodSpecificazione("");
				}
				if (currentForm.getTipoColl() != null &&
						currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CHIAVE_TITOLO)) {
					currentForm.setCodCollocazione("");
				}
			}
			if (currentForm.getChiave().equals("chiaveTitolo")){
				String chiave = this.getChiaviTitoloAutore("T", currentForm.getRecColl().getBid(), currentForm.getTicket());
				if (chiave != null){
					currentForm.setCodSpecificazione(chiave.trim());
					//bug 4687 collaudo
//					if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
//						currentForm.setCodSpecificazioneDisable(false);
//					}else{
//						currentForm.setCodSpecificazioneDisable(true);
//					}
				}else{
					currentForm.setCodSpecificazione("");
//					currentForm.setCodSpecificazioneDisable(false);//bug 4687 collaudo

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.chiaveT01.itoloNonEsistente"));

				}
			}else if (currentForm.getChiave().equals("chiaveAutore")){
				String vid = null;
				if (bidAlTitGen != null){
					vid = currentForm.getReticolo().getTitoloGenerale().getVidAutore();
					//deve ripulire la specificazione
				}else if (bidAlTitPart != null){
					CollocazioneTitoloVO[] reticolo = currentForm.getReticolo().getListaTitoliCollocazione();
					CollocazioneTitoloVO recRet = null;
					for (int i = 0; i < currentForm.getReticolo().getListaTitoliCollocazione().length; i++) {
						recRet = reticolo[i];
						if (bidAlTitPart.equals(recRet.getBid())){
							if (recRet.getVidAutore() != null){
								vid = recRet.getVidAutore();
							}else{
								currentForm.setCodSpecificazione("");
//								currentForm.setCodSpecificazioneDisable(false);//bug 4687 collaudo

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.chiaveAutoreNonEsistente"));

							}
							//bug 4687 collaudo
//							if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
//								currentForm.setCodSpecificazioneDisable(false);
//							}else{
//								currentForm.setCodSpecificazioneDisable(true);
//							}

						}
					}
				}
				String chiave = this.getChiaviTitoloAutore("A", vid, currentForm.getTicket());
				if (chiave != null){
					currentForm.setCodSpecificazione(chiave);
//					currentForm.setCodSpecificazioneDisable(true);//bug 4687 collaudo

				}else{
					currentForm.setCodSpecificazione("");
//					currentForm.setCodSpecificazioneDisable(false);//bug 4687 collaudo

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.chiaveAutoreNonEsistente"));

				}
			}else if (currentForm.getChiave().equals("altro")){
				currentForm.setCodSpecificazione("");
				currentForm.setCodSpecificazioneDisable(false);
			}

			//gestione tipoCollocazione = chiave Titolo
			if (currentForm.getTipoColl() != null &&
					currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CHIAVE_TITOLO)) {
				currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CHIAVE_TITOLO);
				String cles = null;
				currentForm.setCodCollocazione("");
				String isbd = null;
				String titoloSenzaAstrisco = null;
				if (bidAlTitGen != null){
		            int asterisco = currentForm.getReticolo().getTitoloGenerale().getIsbd().indexOf("*");// si cerca carattere "*" all'interno di desc(descrizione )
		            if (asterisco > 0){
		            	titoloSenzaAstrisco = currentForm.getReticolo().getTitoloGenerale().getIsbd().substring(asterisco);
		            }else{
		            	titoloSenzaAstrisco = currentForm.getReticolo().getTitoloGenerale().getIsbd();
		            }
				}else if (bidAlTitPart != null){
					TreeElementViewTitoli figlio = (TreeElementViewTitoli)currentForm.getReticolo().getAnalitica().findElement(currentForm.getBid());
					if (figlio.getNatura().equals("W")){
						isbd = currentForm.getReticolo().getTitoloGenerale().getIsbd();
					}else{
						isbd = currentForm.getTitolo();
					}
		            int asterisco = isbd.indexOf("*");// si cerca carattere "*" all'interno di desc(descrizione )
		            if (asterisco > 0){
		            	titoloSenzaAstrisco = isbd.substring(asterisco);
		            }else{
		            	titoloSenzaAstrisco = isbd;
		            }
				}
				GeneraChiave keyDuoble = new GeneraChiave(titoloSenzaAstrisco, "");
				keyDuoble.estraiChiavi("", titoloSenzaAstrisco);
				cles = (keyDuoble.getKy_cles1_A());
				if (cles != null){
					if (cles.length() >= 24){
						currentForm.setCodCollocazione(cles.substring(0, 24));
					}else{
						currentForm.setCodCollocazione(cles);
					}
				}else{
					currentForm.setCodCollocazione("");
					currentForm.setCodSpecificazione("");
					currentForm.setChiave("Altro");
				}
				currentForm.setCodCollocazioneDisable(true);
			}

			if (currentForm.getTipoColl() != null && (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
					|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE))){
				if (currentForm.getCodFormato() != null && !currentForm.getCodFormato().equals("")){
					if ((!currentForm.getCodFormato().equals(currentForm.getRecFormatiSezioni().getCodFormato()))){

						FormatiSezioniVO rec = this.trovaFormatoSezione(currentForm.getCodFormato(), form);
						//25/8/2008
						rec.setProgNum(0);
						rec.setProgSerie(0);
						//
						if (rec.getDescrFor() != null){
							String descrFor [] = rec.getDescrFor().split(";");
							if (descrFor.length == 3){
								rec.setDescrFor(descrFor[2]);
							}else if (descrFor.length == 1){
								rec.setDescrFor(descrFor[0]);
							}else if (descrFor.length == 2){
								rec.setDescrFor("");
							}
						}
						currentForm.setRecFormatiSezioni(rec);
					}
				}
			}

			if ((currentForm.getFolder() != null && !currentForm.getFolder().equals("tab2"))){
				if (!currentForm.isFlTab2()){
					currentForm.setFlTab2(true);


					if (currentForm.getReticolo().getListaTitoliCollocazione() != null){
						List listaColl = this.getListaCollocazioniReticolo(currentForm.getCodPolo(), currentForm.getCodBib(),
								currentForm.getReticolo().getListaTitoliCollocazione(), "", currentForm.getTicket(), form, currentForm.getNRec());
						if (listaColl != null &&  listaColl.size() > 0)  {
							return	tab2(mapping, currentForm, request, response);
						}
					}
				}
			}

			//
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			if (currentForm.getTitolo() == null){
				return navi.goBack(true);
			}
			return mapping.getInputForward();
		} catch (DataException e) {


			if (currentForm.getProv() == null ||
					(!currentForm.getProv().equals("CV"))){
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			}
			if (currentForm.getTitolo() == null){
				return navi.goBack(true);
			}
			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			if (currentForm.getTitolo() == null){
				return navi.goBack(true);
			}
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * VaiAInserimentoCollAction.java
	  * associaClasse
	  * void
	  * @param currentForm
	  * @throws RemoteException
	  * @throws InfrastructureException
	  * @throws DataException
	  * @throws ValidationException
	  *
	  *
	 */
	private void associaClasse(VaiAInserimentoCollForm currentForm, String bid)
			throws Exception {
		CatSemClassificazioneVO classe = null;
		if (ValidazioneDati.equals(currentForm.getTipoColl(), DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//almaviva5_20121015 #4694 codice sistema classificazione
			SezioneCollocazioneVO sezione = currentForm.getRecSezione();
			try {
				classe = factory.getGestioneSemantica().ricercaClassiPerBidCollegato(true, bid,
						sezione.getClassific(), null, 0, currentForm.getTicket());
			} catch (ValidationException e) {
				log.error("", e);
				return;
			}
			if (classe != null) {
				if (ValidazioneDati.size(classe.getListaClassi()) == 1) {
					String identificativoClasse = ValidazioneDati.first(classe.getListaClassi()).getSimbolo();
					currentForm.setCodCollocazione(identificativoClasse);
				}
			}
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		request.setAttribute("currentForm", form);
		try {
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("fineCV")){
				return Navigation.getInstance(request).goBack(true);
			}
			if (Navigation.getInstance(request).isFromBar() ){
				if (currentForm.getRecFormatiSezioni() != null){
					if (currentForm.getRecFormatiSezioni().getProgNum() == -1){
						currentForm.getRecFormatiSezioni().setProgNum(0);
					}else if (currentForm.getRecFormatiSezioni().getProgSerie() == -1){
						currentForm.getRecFormatiSezioni().setProgSerie(0);
					}else if (currentForm.getRecFormatiSezioni().getPrg() == -1){
						currentForm.getRecFormatiSezioni().setPrg(0);
					}
					return mapping.getInputForward();
				}
			}
			if (currentForm.getCodSez() != null){
				SezioneCollocazioneVO sezioneDettaglio = this.getSezioneDettaglio(currentForm.getCodPolo(),
						currentForm.getCodBib(), currentForm.getCodSez(),
						currentForm.getTicket());
				if (currentForm.getTipoColl() != null ){
					if (sezioneDettaglio != null){
						currentForm.setRecSezione(sezioneDettaglio);
						if (!sezioneDettaglio.getTipoColloc().equals(currentForm.getTipoColl())){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.tipoCollocSezioneIndicataNonCorrispondente"));

							currentForm.setConferma(false);
							return mapping.getInputForward();
						}
					}
				}
			}
			if (!currentForm.isConferma()){
				if (currentForm.getCodSez().equals(null)){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.codSezObbl"));

					currentForm.setConferma(false);
					return mapping.getInputForward();
				}else{
					currentForm.setCodSez(currentForm.getCodSez().trim());
					if (currentForm.getCodPoloSez()==null){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));

					currentForm.setConferma(false);
					return mapping.getInputForward();
					}
				}

				if (currentForm.getProv() != null &&
						(currentForm.getProv().equals("CV"))){
					controllaDatiInvObbligatori(currentForm, request);
				}
				if (currentForm.getCodSez().trim().length() > 0){
					SezioneCollocazioneVO sezione = this.getSezioneDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getCodSez(), currentForm.getTicket());
					if (sezione != null){
						currentForm.setRecSezione(sezione);
					}
				}


				//inserimento collocazione
//				this.loadTab1(request);
				String tipoOperazione = "I";
//				CollocazioneVO recColl = new CollocazioneVO();
				if(currentForm.getLivello1() != null ||
						currentForm.getLivello2() != null ||
						currentForm.getLivello3() != null){
					currentForm.setLivello1(this.trattamentoLivello(currentForm.getLivello1()));
					currentForm.setLivello2(this.trattamentoLivello(currentForm.getLivello2()));
					currentForm.setLivello3(this.trattamentoLivello(currentForm.getLivello3()));
					currentForm.setCodCollocazione(currentForm.getLivello1()+"/"+currentForm.getLivello2()+"/"+currentForm.getLivello3());
				}
				//inizio gestione senza buchi
				if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
						|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					if (currentForm.getCodFormato() == null || currentForm.getCodFormato().trim().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.validaFormatiSezioniCodFormatoObbligatorio"));

						return mapping.getInputForward();
					}
					int progressivi[]={-1,-1};

//					if ((currentForm.getRecFormatiSezioni().getProgSerie() == null || currentForm.getRecFormatiSezioni().getProgSerie().trim().equals(0))
//							&& (currentForm.getCodSpecificazione() == null || currentForm.getCodSpecificazione().trim().equals(0))){
					if ((currentForm.getRecFormatiSezioni().getProgSerie() == 0) && (currentForm.getRecFormatiSezioni().getProgNum() == 0 )){
						//contestualmente all'ins. della collocazione calcolo i progressivi da inserire in forSez
						currentForm.setCodCollocazione("-1");
						currentForm.setCodSpecificazione("-1");
					}else{
						//l'utente ha indicato i valori di serie/progr
						//controllo la validità dei valori inseriti con getFormatoSezioni
						FormatiSezioniVO forSez = this.getFormatiSezioniDettaglio(currentForm.getCodPoloSez(), currentForm.getCodBibSez(),
								currentForm.getCodSez(), currentForm.getCodFormato(), currentForm.getTicket());
						if (forSez != null){
							//controllo che il numero digitato dal bibliotecario sia valido
							if (currentForm.getRecFormatiSezioni().getProgSerie() > forSez.getProgSerie()){
								//serie non valida

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.serieNonValida"));

								currentForm.setConferma(false);
								return mapping.getInputForward();
							}
							if (currentForm.getRecFormatiSezioni().getProgSerie() == forSez.getProgSerie()){
								if ((currentForm.getRecFormatiSezioni().getProgNum() > forSez.getProgNum()) ||
										(currentForm.getRecFormatiSezioni().getProgNum() == 0)){
									//numero non valido

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroNonValido"));

									currentForm.setConferma(false);
									return mapping.getInputForward();
								}
							}
							if (currentForm.getRecFormatiSezioni().getProgSerie() > 0 &&
									(currentForm.getRecFormatiSezioni().getProgNum() == 0)){
								//numero non valido

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.numeroNonValido"));

								currentForm.setConferma(false);
								return mapping.getInputForward();
							}
							progressivi[0] = currentForm.getRecFormatiSezioni().getProgSerie();
							progressivi[1] = currentForm.getRecFormatiSezioni().getProgNum();
							if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
								currentForm.setCodCollocazione(String.valueOf(currentForm.getRecFormatiSezioni().getProgNum()));
								currentForm.setCodSpecificazione("");
							}else{
								currentForm.setCodCollocazione(currentForm.getRecFormatiSezioni().getCodFormato() + String.valueOf(currentForm.getRecFormatiSezioni().getProgSerie()));
								currentForm.setCodSpecificazione(String.valueOf(currentForm.getRecFormatiSezioni().getProgNum()));
							}
						}
					}
					currentForm.setRecFormatiSezioni(new FormatiSezioniVO());
					currentForm.getRecFormatiSezioni().setCodPolo(currentForm.getCodPoloSez());
					currentForm.getRecFormatiSezioni().setCodBib(currentForm.getCodBibSez());
					currentForm.getRecFormatiSezioni().setCodSez(currentForm.getCodSez());
					currentForm.getRecFormatiSezioni().setCodFormato(currentForm.getCodFormato());
					currentForm.getRecFormatiSezioni().setProgSerie(progressivi[0]);
					currentForm.getRecFormatiSezioni().setProgNum(progressivi[1]);
					currentForm.getRecFormatiSezioni().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				}
				//fine gestione senza buchi
				currentForm.getRecColl().setCodPolo(currentForm.getCodPolo());
				currentForm.getRecColl().setCodBib(currentForm.getCodBib());
				currentForm.getRecColl().setCodPoloSez(currentForm.getCodPoloSez());
				currentForm.getRecColl().setCodBibSez(currentForm.getCodBibSez());
				currentForm.getRecColl().setCodSez(currentForm.getCodSez());
				currentForm.getRecColl().setKeyColloc(0);
//				currentForm.getRecColl().setCodColloc(currentForm.getCodCollocazione().toUpperCase());
//				currentForm.getRecColl().setSpecColloc(currentForm.getCodSpecificazione().toUpperCase());
				currentForm.getRecColl().setCodColloc(currentForm.getCodCollocazione());
				currentForm.getRecColl().setSpecColloc(currentForm.getCodSpecificazione());
				currentForm.getRecColl().setTotInv(0);
				currentForm.getRecColl().setOrdLoc("");
				currentForm.getRecColl().setOrdSpec("");
				currentForm.getRecColl().setUteIns(Navigation.getInstance(request).getUtente().getCodPolo()+
						Navigation.getInstance(request).getUtente().getCodBib()+
						Navigation.getInstance(request).getUtente().getUserId());
				currentForm.getRecColl().setUteAgg(Navigation.getInstance(request).getUtente().getCodPolo()+
						Navigation.getInstance(request).getUtente().getCodBib()+
						Navigation.getInstance(request).getUtente().getUserId());
				currentForm.getRecColl().setCancDb2i("");
				currentForm.getRecColl().setUteIns(Navigation.getInstance(request).getUtente().getFirmaUtente());
//				currentForm.setRecColl(recColl);
				//	per controllare l'esistenza del codColl
				//  false non trovato procede con la creazione dell'inventario
				if (currentForm.getCodSez() != null && currentForm.getCodCollocazione() != null && currentForm.getCodSpecificazione() != null){
					if (currentForm.getCodSez().trim().equals("")
							&& currentForm.getCodCollocazione().trim().equals("")
							&& currentForm.getCodSpecificazione().trim().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneEStringaDiCollocazioneASpazio"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
				if (currentForm.getRecFormatiSezioni() != null){
					if (currentForm.getRecFormatiSezioni().getProgSerie() != -1 && currentForm.getRecFormatiSezioni().getProgNum() != -1){
						CollocazioneVO rec = this.getCollocazione(currentForm.getRecColl(), currentForm.getTicket());
					}
				}else{
					CollocazioneVO rec = this.getCollocazione(currentForm.getRecColl(), currentForm.getTicket());
				}
			}
				//chiama modificaInvColl passando collocazioneVO
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("descrBib", currentForm.getDescrBib());
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("titolo", currentForm.getTitolo());
				request.setAttribute("codSerie", currentForm.getCodSerie());
				request.setAttribute("codInvent", currentForm.getCodInvent());
				request.setAttribute("recColl", currentForm.getRecColl());
				if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
						|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
						|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					currentForm.getRecColl().setRecFS(currentForm.getRecFormatiSezioni());
				}
				request.setAttribute("tipoColloc", currentForm.getTipoColl());
				request.setAttribute("reticolo", currentForm.getReticolo());
				request.setAttribute("chiamante",mapping.getPath());
				if (currentForm.getProv() != null){
					request.setAttribute("prov", "CV");
					Navigation.getInstance(request).purgeThis();
					Navigation navigation =Navigation.getInstance(request);
					navigation.setDescrizioneX("Inserimento Collocazione Veloce");
					navigation.setTesto("Inserimento Collocazione Veloce");
					request.setAttribute("recInv", currentForm.getRecInv());
				}
				currentForm.setConferma(false);
				currentForm.setDisableTastoAvanti(true);
				currentForm.setDisablePerModInvDaNav(true);
				if (currentForm.getCodSez() != null && currentForm.getCodCollocazione() != null && currentForm.getCodSpecificazione() != null){
					if (currentForm.getCodSez().trim().equals("")
							&& currentForm.getCodCollocazione().trim().equals("")
							&& currentForm.getCodSpecificazione().trim().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneEStringaDiCollocazioneASpazio"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
				request.setAttribute("tipocolloc", currentForm.getTipoColl());
				request.setAttribute("codCollocFormato", currentForm.getCodCollocazione());
				request.setAttribute("codSpecifFormato", currentForm.getCodSpecificazione());
				return mapping.findForward("ok");
		} catch (ValidationException e) {
			if (e.getMsg()!=null){
				if (e.getMsg().equals("msgCodLoc")){
					if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							//							|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
							//							c'è la necessità di assegnare la stessa collocazione a titoli diversi nel
							//							caso di cambiamento di titolo della rivista bug #0002965 esercizio
							|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneGiaEsistente"));

						currentForm.setConferma(false);
						return mapping.getInputForward();
					}

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage()/*+" "+ve.getBid()+" " +ve.getIsbd()+" ?"*/));

					this.saveToken(request);
					currentForm.setConferma(true);
					return mapping.getInputForward();
				}
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private void controllaDatiInvObbligatori(VaiAInserimentoCollForm currentForm, HttpServletRequest request)
			throws ValidationException, ParseException {
		InventarioVO recInv = currentForm.getRecInv();
		if (!ValidazioneDati.strIsNull((recInv.getValore()))) {
			if ((recInv.getValore()).length()>14 ) {
				throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
			}
			if (recInv.getValoreDouble() > ServiziConstant.MAX_VALORE){
				throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
			}
		}
		if (recInv.getValoreDouble()==0) {
			throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
		}
		if (recInv.getCodMatInv()!=null && recInv.getCodMatInv().trim().length()==0){
			throw new ValidationException("validaInvCodMatInvObbligatorio", ValidationException.errore);
		}
		if (recInv.getCodMatInv() !=null &&  recInv.getCodMatInv().length()!=0)	{
			if (recInv.getCodMatInv().trim().length()>2)	{
				throw new ValidationException("validaInvCodMatInvEccedente", ValidationException.errore);
			}
		}
//		if (recInv.getCodTipoOrd()!=null && recInv.getCodTipoOrd().trim().length()==0){
//			throw new ValidationException("validaInvCodTipoOrdObbligatorio", ValidationException.errore);
//		}
//		if (recInv.getCodTipoOrd() !=null &&  recInv.getCodTipoOrd().length()!=0)	{
//			if (recInv.getCodTipoOrd().trim().length()>1)	{
//				throw new ValidationException("validaInvCodTipoOrdEccedente", ValidationException.errore);
//			}
//		}
		if (recInv.getTipoAcquisizione()!=null && recInv.getTipoAcquisizione().trim().length()==0){
			throw new ValidationException("validaInvTipoAcquisizioneObbligatorio", ValidationException.errore);
		}
		if (recInv.getTipoAcquisizione() !=null &&  recInv.getTipoAcquisizione().length()!=0)	{
			if (recInv.getTipoAcquisizione().trim().length()>1)	{
				throw new ValidationException("validaInvTipoAcquisizioneEccedente", ValidationException.errore);
			}
		}
		if (recInv.getCodFrui()!=null && recInv.getCodFrui().trim().length()==0){
			throw new ValidationException("validaInvCodFruiObbligatorio", ValidationException.errore);
		}
		if (recInv.getCodFrui() !=null &&  recInv.getCodFrui().length()!=0)	{
			if (recInv.getCodFrui().trim().length()>2)	{
				throw new ValidationException("validaInvCodFruiEccedente", ValidationException.errore);
			}
		}

		DocumentoFisicoFormTypes formType = DocumentoFisicoFormTypes.getFormType(Navigation.getInstance(request).getCallerForm());
		String dataInsPrimaColl = recInv.getDataInsPrimaColl();
		if (ValidazioneDati.strIsNull(dataInsPrimaColl) && formType != DocumentoFisicoFormTypes.ESAME_INVENTARIO)
			throw new ValidationException("validaInvDataInsPrimaCollObbl");
		if (ValidazioneDati.isFilled(dataInsPrimaColl) && ValidazioneDati.validaData(dataInsPrimaColl) != ValidazioneDati.DATA_OK)
			throw new ValidationException("validaInvDataInsPrimaCollErrata");
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			if (currentForm.getProv() != null && currentForm.getProv().equals("CV")){
				request.setAttribute("prov", "fineCV");
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("desc", currentForm.getTitolo());
				Navigation.getInstance(request).purgeThis();

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
				return mapping.findForward("listaInventariDelTitolo");
//				return Navigation.getInstance(request).goBack();
			}
				String chiamante = Navigation.getInstance(request).getActionCaller();
				if (chiamante.toUpperCase().indexOf("INSERIMENTOINV") > -1) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

					this.saveToken(request);
					return mapping.findForward("listaInventariDelTitolo");
				}
				return Navigation.getInstance(request).goBack();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaUltColl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
        if (Navigation.getInstance(request).isFromBar()){
            return mapping.getInputForward();
        }
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try {
    		request.setAttribute("codBib",currentForm.getCodBib());
    		request.setAttribute("descrBib",currentForm.getDescrBib());

    		EsameCollocRicercaVO paramRicercaCollSpec = new EsameCollocRicercaVO();
    		paramRicercaCollSpec.setCodPolo(currentForm.getCodPoloSez());
    		paramRicercaCollSpec.setCodBib(currentForm.getCodBibSez());
    		paramRicercaCollSpec.setCodPoloSez(currentForm.getCodPoloSez());
    		paramRicercaCollSpec.setCodBibSez(currentForm.getCodBibSez());
    		paramRicercaCollSpec.setCodSez(currentForm.getCodSez());
    		paramRicercaCollSpec.setCodLoc("");
    		paramRicercaCollSpec.setCodSpec("");
    		paramRicercaCollSpec.setEsattoColl(false);
    		paramRicercaCollSpec.setEsattoSpec(false);
    		paramRicercaCollSpec.setOrdLst("");

     		request.setAttribute("paramRicerca",paramRicercaCollSpec);
    		request.setAttribute("listaUltColl", "listaUltColl");
    		request.setAttribute("chiamante", mapping.getPath());

    		return mapping.findForward("listaUltColl");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaUltSpec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
        if (Navigation.getInstance(request).isFromBar()){
            return mapping.getInputForward();
        }
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try {


//			this.checkForm(request);
    		resetToken(request);

    		request.setAttribute("codBib",currentForm.getCodBib());
    		request.setAttribute("descrBib",currentForm.getDescrBib());
    		EsameCollocRicercaVO paramRicercaCollSpec = new EsameCollocRicercaVO();
    		paramRicercaCollSpec.setCodPolo(currentForm.getCodPoloSez());
    		paramRicercaCollSpec.setCodBib(currentForm.getCodBibSez());
    		paramRicercaCollSpec.setCodPoloSez(currentForm.getCodPoloSez());
    		paramRicercaCollSpec.setCodBibSez(currentForm.getCodBibSez());
    		paramRicercaCollSpec.setCodSez(currentForm.getCodSez().trim());
    		if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
					|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
    			currentForm.setCodCollocazione(currentForm.getCodFormato());
    		}
    		if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO) ){
    			currentForm.setCodCollocazione("");
    		}
			if(currentForm.getLivello1() != null ||
					currentForm.getLivello2() != null ||
					currentForm.getLivello3() != null){
				currentForm.setLivello1(this.trattamentoLivello(currentForm.getLivello1()));
				currentForm.setLivello2(this.trattamentoLivello(currentForm.getLivello2()));
				currentForm.setLivello3(this.trattamentoLivello(currentForm.getLivello3()));
				currentForm.setCodCollocazione(currentForm.getLivello1()+"/"+currentForm.getLivello2()+"/"+currentForm.getLivello3());
			}
    		paramRicercaCollSpec.setCodLoc(currentForm.getCodCollocazione());
    		paramRicercaCollSpec.setCodSpec("");
    		paramRicercaCollSpec.setEsattoColl(false);
    		paramRicercaCollSpec.setEsattoSpec(false);
    		paramRicercaCollSpec.setOrdLst("");

     		request.setAttribute("paramRicerca",paramRicercaCollSpec);
    		request.setAttribute("listaUltSpec", "listaUltSpec");
    		request.setAttribute("chiamante", mapping.getPath());

			return mapping.findForward("listaUltSpec");


		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoClassi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {
			if (!Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
				Navigation.getInstance(request).getUtente().setCodBib(currentForm.getCodBib());
			}
			ClassiDelegate classi = ClassiDelegate.getInstance(request);

//			AttivazioneSIFListaClassiCollegateVO richiesta = new AttivazioneSIFListaClassiCollegateVO(currentForm.getBid(), currentForm.getTitolo(), currentForm.getNRec(), "classe");
			AttivazioneSIFListaClassiCollegateVO richiesta = new AttivazioneSIFListaClassiCollegateVO(currentForm.getRecColl().getBid(), currentForm.getTitolo(), currentForm.getRecSezione().getClassific(), currentForm.getNRec(), "classe");
			return classi.getSIFListaClassiCollegate(mapping, richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sceltaColl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {

			String check = currentForm.getSelectedColl();
			int collsel;
			if (check != null && check.length() != 0) {
				collsel = Integer.parseInt(currentForm.getSelectedColl());
				CollocazioneReticoloVO scelColl = (CollocazioneReticoloVO) currentForm.getListaCollReticolo().get(collsel);

				//inserimento collocazione
				if(currentForm.getLivello1() != null ||
						currentForm.getLivello2() != null ||
						currentForm.getLivello3() != null){
					currentForm.setCodCollocazione(currentForm.getLivello1()+"/"+currentForm.getLivello2()+"/"+currentForm.getLivello3());
				}
				SezioneCollocazioneVO sezione = this.getSezioneDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(), scelColl.getCodSez(), currentForm.getTicket());
				if (sezione != null){
					currentForm.setRecSezione(sezione);
				}
				CollocazioneVO recColl = this.getCollocazione(scelColl.getKeyColloc(), currentForm.getTicket());
				if (recColl != null){
					currentForm.setRecColl(recColl);
				}
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("descrBib", currentForm.getDescrBib());
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("titolo", currentForm.getTitolo());
				request.setAttribute("codSerie", currentForm.getCodSerie());
				request.setAttribute("codInvent", currentForm.getCodInvent());

				request.setAttribute("tipoColloc", sezione.getTipoColloc());
				request.setAttribute("recColl", currentForm.getRecColl());
				request.setAttribute("reticolo", currentForm.getReticolo());
				request.setAttribute("chiamante",mapping.getPath());
				request.setAttribute("prov","insColl");
				return mapping.findForward("sceltaColl");

			}else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward esEsempl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			request.setAttribute("check", currentForm.getSelectedColl());
			String check = currentForm.getSelectedColl();
			int collsel;
			if (check != null && check.length() != 0) {
				collsel = Integer.parseInt(currentForm.getSelectedColl());
				CollocazioneReticoloVO scelColl = (CollocazioneReticoloVO) currentForm.getListaCollReticolo().get(collsel);
				if (scelColl.getCodDoc() == 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collocazioneNonLegataAdEsemplare"));

					return mapping.getInputForward();
				}else{
					CollocazioneDettaglioVO coll = this.getCollocazioneDettaglio(scelColl.getKeyColloc(), currentForm.getTicket());
					if (coll != null){
						request.setAttribute("codBib", currentForm.getCodBib());
						request.setAttribute("descrBib", currentForm.getDescrBib());
						request.setAttribute("bid", coll.getBid());
						request.setAttribute("titolo", coll.getBidDescr());
						request.setAttribute("recColl", coll);
						request.setAttribute("reticolo", currentForm.getReticolo());
						request.setAttribute("codSerie", currentForm.getCodSerie());
						request.setAttribute("codInv", String.valueOf(currentForm.getCodInvent()));
						request.setAttribute("esamina", "esamina");
						return mapping.findForward("esEsempl");
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.collCollocazioneInesistente"));

						return mapping.getInputForward();
					}
				}
			}else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}
	public ActionForward inventari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSez",currentForm.getCodSez());
			int collSel;
			request.setAttribute("checkS", currentForm.getSelectedColl());
			String check = currentForm.getSelectedColl();
			if (check != null && check.length() != 0) {
				collSel = Integer.parseInt(currentForm.getSelectedColl());
				CollocazioneVO scelColl = (CollocazioneVO) currentForm.getListaCollReticolo().get(collSel);
				currentForm.getParamRicerca().setCodPolo(scelColl.getCodPolo());
				currentForm.getParamRicerca().setCodBib(scelColl.getCodBib());
				currentForm.getParamRicerca().setKeyLoc(scelColl.getKeyColloc());
				currentForm.getParamRicerca().setCodLoc(scelColl.getCodColloc());
				currentForm.getParamRicerca().setCodSpec(scelColl.getSpecColloc());
				currentForm.getParamRicerca().setOrdLst("I");
				request.setAttribute("paramRicerca", currentForm.getParamRicerca());
				request.setAttribute("tipoLista","listaInvDiColloc");
				request.setAttribute("prov","insColl");
				return mapping.findForward("esInv");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sceltaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {

			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
    		int titsel;
			request.setAttribute("check", currentForm.getSelectedTit());
			String check = currentForm.getSelectedTit();
				if (check != null && check.length() != 0) {
		   			titsel = Integer.parseInt(currentForm.getSelectedTit());
		   			CollocazioneTitoloVO scelTit = (CollocazioneTitoloVO) currentForm.getCollLiv().get(titsel);
					request.setAttribute("scelTit",scelTit);
					currentForm.setFolder("tab1");
		    		currentForm.setTasto("sceltaTitolo");
		    		currentForm.setNoSezione("noSezione");
					this.loadTab1(request, form);
					currentForm.setDisableTastoAvanti(false);
					currentForm.setDisable(false);
    			}else {

    				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelection"));

    			}


				return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward listaSupportoSez(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		this.resetToken(request);
		request.setAttribute("currentForm", form);
		SezioneCollocazioneVO sezione = null;
		currentForm.setNoSezione("siSezione");//se si toglie non funziona l'automatismo della sezione digitata
		try {
			if (currentForm.isSezNonEsiste()){
				currentForm.setSezNonEsiste(false);
				request.setAttribute("chiamante",mapping.getPath());
				request.setAttribute("codBib",currentForm.getCodBib());
				request.setAttribute("descrBib",currentForm.getDescrBib());
				return mapping.findForward("lenteSez");
			}else{
				if (currentForm.getCodPoloSez() == null && currentForm.getCodBibSez() == null && currentForm.getCodSez().trim().length()>0
						|| currentForm.getCodPoloSez() != null && currentForm.getCodBibSez() != null && currentForm.getCodSez().trim().length()>0){
					//ho digitato la sezione senza passare dalla lista
					//imposto i parametri e faccio get della sezione
					sezione = this.getSezioneDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(), currentForm.getCodSez().trim(), currentForm.getTicket());
					//se la sezione esiste imposto NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA e faccio return "this.unspecified(mapping, form,	request, response)"
					if (sezione != null && currentForm.getNoSezione().equals("siSezione")){
						currentForm.setRecSezione(sezione);
						request.setAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA, sezione);
						return this.unspecified(mapping, form, request, response);
					}else{
						//sezione non trovata: rimanda la lista
						request.setAttribute("chiamante",mapping.getPath());
						request.setAttribute("codBib",currentForm.getCodBib());
						request.setAttribute("descrBib",currentForm.getDescrBib());
						return mapping.findForward("lenteSez");
					}

				}else{
					currentForm.setSezNonEsiste(false);
					request.setAttribute("chiamante",mapping.getPath());
					request.setAttribute("codBib",currentForm.getCodBib());
					request.setAttribute("descrBib",currentForm.getDescrBib());
					return mapping.findForward("lenteSez");
				}
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException e) {
			if (sezione == null){
				currentForm.setSezNonEsiste(true);

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezNonEsistente"));

				return this.listaSupportoSez(mapping, form,	request, response);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoProven(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		try {
    		request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("codBib",currentForm.getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			// evolutiva Mail Carla del 16.06.2017 - nuova gestione del cartiglio relativo alla Provenienza:
			// se nel campo Provenienza si inserisce una parola/stringa la pressione del cartiglio attiva una ricerca filtrata
			// per stringa inserita altrimenti se il campo rimane vuoto la ricerca rimane uguale a quella attuale.
			request.setAttribute("filtroProvenienza",currentForm.getDescrProven());

			return mapping.findForward("lenteProven");

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward tab1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		currentForm.setFolder("tab1");
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		this.loadTab1(request, form);


		return mapping.getInputForward();
	}

	public ActionForward tab2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// lista collocazioni di reticolo

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		currentForm.setFolder("tab2");
//		if (currentForm.isFlTab2()){
//			currentForm.setFlTab2(false);
//		}
		try{
			//			String tipoOperazione = DocumentoFisicoCostant.LISTA_DA_RETICOLO_4;
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
			String tipoOperazione = "";
			if (currentForm.getReticolo().getListaTitoliCollocazione() != null){
				List listaColl = this.getListaCollocazioniReticolo(currentForm.getCodPolo(), currentForm.getCodBib(),
						currentForm.getReticolo().getListaTitoliCollocazione(), tipoOperazione, currentForm.getTicket(), form, currentForm.getNRec());
				if (listaColl == null ||  listaColl.size() <1)  {
					currentForm.setNoColl(true);
					request.setAttribute("codBib", currentForm.getCodBib());
					request.setAttribute("descrBib", currentForm.getDescrBib());
					request.setAttribute("bid", currentForm.getBid());
					request.setAttribute("titolo", currentForm.getTitolo());

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noLista"));

					return mapping.getInputForward();
				}else{
					if (listaColl.size() == 1){
						currentForm.setSelectedColl("0");
					}
					currentForm.setNoColl(false);
					currentForm.setCodBib(currentForm.getCodBib());
					currentForm.setListaCollReticolo(listaColl);
				}
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaTitoliReticoloNullControllareNaturaNotiziaCorrente"));

			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return this.tab1(mapping, form,	request, response);
//			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}

	public ActionForward tab3(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		currentForm.setFolder("tab3");
//		this.loadTab3();
		CollocazioneTitoloVO[] reticolo = currentForm.getReticolo().getListaTitoliCollocazione();
		CollocazioneTitoloVO recRet = null;
		CollocazioneTitoloVO collTit = null;
		List lista = new ArrayList();
		try{
//			String tipoOperazione = DocumentoFisicoCostant.LISTA_DA_RETICOLO_4;
			for (int i = 0; i < currentForm.getReticolo().getListaTitoliCollocazione().length; i++) {
				recRet = reticolo[i];
				collTit = new CollocazioneTitoloVO();
				collTit.setPrg(i + 1);
				collTit.setBid(recRet.getBid());
				collTit.setNatura(recRet.getNatura());
				collTit.setIsbd(recRet.getIsbd());
				lista.add(collTit);
			}

			if (lista == null || lista.size() <1)  {
				currentForm.setNoColl(true);
				request.setAttribute("codBib", currentForm.getCodBib());
				request.setAttribute("descrBib", currentForm.getDescrBib());
				request.setAttribute("bid", currentForm.getBid());
				request.setAttribute("titolo", currentForm.getTitolo());

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noLista"));

				return mapping.getInputForward();
			}else{
				if (lista.size() == 1){
					currentForm.setSelectedTit("0");
				}
				currentForm.setNoColl(false);
				currentForm.setCodBib(currentForm.getCodBib());
				currentForm.setCollLiv(lista);
			}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	private void loadPagina(HttpServletRequest request, ActionMapping mapping, ActionForm form) throws Exception {
//		titolo
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
//		if (request.getAttribute("bid")!= null &&
//				request.getAttribute("titolo")!= null &&
//				request.getAttribute("codBib")!= null &&
//				request.getAttribute("descrBib")!= null){
//			currentForm.setBid((String)request.getAttribute("bid"));
//			currentForm.setTitolo((String)request.getAttribute("titolo"));
//			currentForm.setCodBib((String)request.getAttribute("codBib"));
//			currentForm.setDescrBib((String)request.getAttribute("descrBib"));
//		}
		currentForm.setLivColl(AL_TITOLO_GENERALE);
		this.loadTab1(request, form);

	}

	private void loadTab1(HttpServletRequest request, ActionForm form) throws Exception {
		//		nuova collocazione
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if(!currentForm.isSessione()){
			currentForm.setCodSerie((String)request.getAttribute("codSerie"));
			currentForm.setCodInvent((Integer)request.getAttribute("codInvent"));
			currentForm.getRecColl().setBid(currentForm.getBid());

			if (currentForm.getCodSez()!=null){
				currentForm.setCodSez("");
			}else{
				currentForm.getRecColl().setCodSez(currentForm.getCodSez().trim());
			}
		}else{
			if(request.getAttribute("codInvent") != null) {
				currentForm.setCodSerie((String)request.getAttribute("codSerie"));
				currentForm.setCodInvent((Integer)request.getAttribute("codInvent"));
				if (currentForm.getCodSez()==null){
					currentForm.setCodSez("");
				}else{
					currentForm.getRecColl().setCodSez(currentForm.getCodSez().trim());
				}
			}
		}
		List lista = null;
		CollocazioneTitoloVO ct = (CollocazioneTitoloVO)request.getAttribute("scelTit");
		if (ct != null) {
			lista = this.getTitolo(ct.getBid(), currentForm.getTicket());
			currentForm.setLivColl(AL_TITOLO_A_LIVELLI);
			TitoloVO titolo = null;
			if (ValidazioneDati.size(lista) == 1) {
				titolo = (TitoloVO)lista.get(0);
				currentForm.setBidDaTit(titolo.getBid());
				currentForm.setIsbdDaTit(titolo.getIsbd());
				currentForm.getRecColl().setBid(titolo.getBid());
				currentForm.setTerzaParte(titolo.getTerzaParte());
				//almaviva5_20140211 #5498 ricarica reticolo
				//currentForm.setReticolo(this.getAnaliticaPerCollocazione(currentForm.getBidDaTit(), currentForm.getTicket(), form));
			}

		}
		if (currentForm.getCodSez() != null){
			if (currentForm.getCodSez().trim() != null && currentForm.getNoSezione().equals("siSezione")){
				preparaFormPerTipoColloc(request, form, currentForm);

			}
		}else{
			currentForm.setCodSez(null);
			currentForm.setRecFormatiSezioni(null);
		}
	}

	private void preparaFormPerTipoColloc(HttpServletRequest request,
			ActionForm form, VaiAInserimentoCollForm currentForm) throws Exception {
		currentForm.setLenteSez("lenteSez");
		currentForm.setDescrTipoCollocazione(currentForm.getTipoColl());
		if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
			currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.SISTEMA_DI_CLASSIFICAZIONE);
			if (currentForm.getCodCollocazione() != null && currentForm.getCodCollocazione().trim().equals("")){
				currentForm.setCodCollocazione("");
			}
			currentForm.setCodSpecificazione("");
			currentForm.setChiave("altro");
		}else if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_ESPLICITA_NON_STRUTTURATA)) {
			currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_NON_STRUTTURATA);
			currentForm.setCodCollocazione("");
			currentForm.setCodSpecificazione("");
			currentForm.setChiave("altro");
		}else if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CHIAVE_TITOLO)) {
			currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CHIAVE_TITOLO);
			currentForm.setCodCollocazione("");
			currentForm.setCodSpecificazione("");
			currentForm.setChiave("altro");
		}else if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)) {
			currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_NON_A_FORMATO);
			FormatiSezioniVO rec = new FormatiSezioniVO();
			rec.setCodFormato("00");
			rec.setProgSerie(0);
			rec.setProgNum(0);
			rec.setDescrFor("formato 00");
			currentForm.setRecFormatiSezioni(rec);
			currentForm.setCodFormato("00");
		}else if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
				|| currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){

			String[] finoAlPuntoEVirgola = null;
			String dimensioneFormato = null;
			String codFor = null;
//			String[] finoAlPuntoEVirgola;
//			if (currentForm.getRecInv().getNatura().equals("S")){
//				finoAlPuntoEVirgola = currentForm.getTerzaParte().split("");
//			}else{
//				finoAlPuntoEVirgola = currentForm.getTerzaParte().split(";");
//			}
//			String dimensioneFormato = (finoAlPuntoEVirgola[1].substring(0, finoAlPuntoEVirgola[1].indexOf("cm"))).trim();

			if (currentForm.getTerzaParte() != null){
				finoAlPuntoEVirgola = currentForm.getTerzaParte().split(";");
				if (finoAlPuntoEVirgola.length > 1){
					finoAlPuntoEVirgola = currentForm.getTerzaParte().split(";");
					if (finoAlPuntoEVirgola[1].indexOf("cm") < 0 ){
//
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));

					}else{
						dimensioneFormato = (finoAlPuntoEVirgola[1].substring(0, finoAlPuntoEVirgola[1].indexOf("cm"))).trim();
					}
				}else{
					if (finoAlPuntoEVirgola[0].indexOf("cm") < 0 ){
//
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));

					}else{
						dimensioneFormato = (finoAlPuntoEVirgola[0].substring(0, finoAlPuntoEVirgola[0].indexOf("cm"))).trim();
					}
				}
			} else{
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dimensioniNonPresentiNelTitolo"));

			}

			if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)){
				currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.MAGAZZINO_A_FORMATO);
			}
			if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
				currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.CONTINUAZIONE);
			}
			//carico i formati
			List listaForSez = null;
			listaForSez = this.getListaFormatiSezioni(currentForm.getCodPoloSez(), currentForm.getCodBibSez(), currentForm.getCodSez(), currentForm.getTicket(), form);
			if (listaForSez == null || listaForSez.size() <= 0) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.listaVuota"));

			} else {
				codFor = null;
				if (!ValidazioneDati.strIsNull(dimensioneFormato) && ValidazioneDati.strIsNumeric(dimensioneFormato)){
					for (int i = 0; i < listaForSez.size(); i++) {
						FormatiSezioniVO recForSez = (FormatiSezioniVO) listaForSez.get(i);
						String dimFor[] = recForSez.getDescrFor().split(FormatiSezioniVO.SEPARATOREFORSEZ);
						String dimDa = null;
						String dimA = null;
						if (dimFor.length > 1){
							dimDa = Integer.valueOf(dimFor[0]).toString();
							dimA = Integer.valueOf(dimFor[1]).toString();
							if (Integer.valueOf(dimensioneFormato) >= Integer.valueOf(dimDa)
									&& Integer.valueOf(dimensioneFormato) <= Integer.valueOf(dimA)){
								codFor = recForSez.getCodFormato();
								break;
							}
						}
					}
				}
					currentForm.setCodFormato(null);
					for (int i = 0; i < listaForSez.size(); i ++){
						FormatiSezioniVO rec = (FormatiSezioniVO) listaForSez.get(i);
						String descrFor [] = rec.getDescrFor().split(";");
						if (descrFor.length == 3){
							rec.setDescrFor(descrFor[2]);
						}else if (descrFor.length == 1){
							rec.setDescrFor(descrFor[0]);
						}else if (descrFor.length == 2){
							rec.setDescrFor("");
						}
					}
					currentForm.setListaFormatiSezioni(listaForSez);
					List listaCodiciFormati = new ArrayList(listaForSez);
					listaCodiciFormati.add(0, new FormatiSezioniVO());
					currentForm.setListaCodiciFormati(listaCodiciFormati);
					FormatiSezioniVO rec = new FormatiSezioniVO();
					if (codFor != null){
						rec = this.trovaFormatoSezione(codFor, form);
						//25/8/2008
						currentForm.setCodFormato(rec.getCodFormato());
						rec.setProgNum(0);
						rec.setProgSerie(0);
						//
						String descrFor [] = rec.getDescrFor().split(";");
						if (descrFor.length == 3){
							rec.setDescrFor(descrFor[2]);
						}else if (descrFor.length == 1){
							rec.setDescrFor(descrFor[0]);
						}else if (descrFor.length == 2){
							rec.setDescrFor("");
						}
						currentForm.setRecFormatiSezioni(rec);
					}else{
						if (LinkableTagUtils.getErrors(request).isEmpty()){
							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoNonDefinito"));

						}
						rec.setCodFormato(((FormatiSezioniVO) currentForm.getListaCodiciFormati().get(0)).getCodFormato());
						rec.setDescrFor(null);
					}
					rec.setProgSerie(0);
					rec.setProgNum(0);
					if (rec.getDescrFor() != null){
						String descrFor [] = rec.getDescrFor().split(";");
						if (descrFor.length == 3){
							rec.setDescrFor(descrFor[2]);
						}else if (descrFor.length == 1){
							rec.setDescrFor(descrFor[0]);
						}else if (descrFor.length == 2){
							rec.setDescrFor("");
						}
					}
					currentForm.setRecFormatiSezioni(rec);
			}
		}else if (currentForm.getTipoColl().equals(DocumentoFisicoCostant.COD_ESPLICITA_STRUTTURATA)) {
			currentForm.setDescrTipoCollocazione(DocumentoFisicoCostant.ESPLICITA_STRUTTURATA);
			currentForm.setLivello1("");
			currentForm.setLivello2("");
			currentForm.setLivello3("");
			currentForm.setChiave("altro");
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			Navigation.getInstance(request).purgeThis();
			return this.ok(mapping, form, request, response);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		request.setAttribute("currentForm", form);
		try {
			request.setAttribute("prov", "insColl");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		try{
			if (currentForm.getFolder().equals("tab2")){
				int numBlocco = currentForm.getBloccoSelezionato();
				String idLista = currentForm.getIdLista();
				String ticket = Navigation.getInstance(request).getUserTicket();
				if (numBlocco>1 && idLista != null) {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
					if (bloccoVO != null) {
						currentForm.getListaCollReticolo().addAll(bloccoVO.getLista());
					}
				}
			}
			return mapping.getInputForward();

		}catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			currentForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private FormatiSezioniVO trovaFormatoSezione(String codFormato, ActionForm form) {

		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		for (int i = 0; i < currentForm.getListaFormatiSezioni().size(); i++) {
			FormatiSezioniVO rec = (FormatiSezioniVO) currentForm.getListaFormatiSezioni().get(i);
			if (rec.getCodFormato().equals(codFormato.substring(0,2)))
				return rec;
		}

		return null;
	}
	private List getListaFormatiSezioni(String codPolo, String codBib, String codSez, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		List formati;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		formati = factory.getGestioneDocumentoFisico().getListaFormatiSezioni(codPolo, codBib, codSez, currentForm.getTicket());
		if (formati == null || formati.size() <= 0) {
			currentForm.setNoFormati(true);
		}
		return formati;
	}

	private CollocazioneVO getCollocazione(CollocazioneVO recColl, String ticket)
	throws Exception {
		CollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getCollocazione(recColl, ticket);
		return rec;
}
	private CollocazioneVO getCollocazione(int keyLoc, String ticket)
	throws Exception {
		CollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getCollocazione(keyLoc, ticket);
		return rec;
}

	private List getListaCollocazioniReticolo( String codPolo, String codBib, CollocazioneTitoloVO[] collTitolo, String tipoOperazione, String ticket, ActionForm form, int nRec)
	throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaCollocazioniReticolo(codPolo, codBib, collTitolo, tipoOperazione, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() < 1)  {
			currentForm.setNoColl(true);
			return null;
		}else{
			currentForm.setNoColl(false);
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)){
				currentForm.setAbilitaBottoneCarBlocchi(false);
			}else{
				currentForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}

	private String trattamentoLivello(String livello) {
		if (livello == null || livello.trim().equals("")){
			return "       ";
		}
		if (livello.trim().length() < 7){
			return ValidazioneDati.fillRight(livello.trim(), ' ', 7);
		}
		return livello.trim();
	}

	private DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		DatiBibliograficiCollocazioneVO reticolo;
		String bibliotecaOperante = currentForm.getCodPolo() + currentForm.getCodBib();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		reticolo = factory.getGestioneDocumentoFisico().getAnaliticaPerCollocazione(bid, bibliotecaOperante, ticket);
		if (reticolo == null)  {
			currentForm.setNoReticolo(true);
		}else{
			currentForm.setNoReticolo(false);
		}
		return reticolo;
	}
	private CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws Exception {
		CollocazioneDettaglioVO coll;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		coll = factory.getGestioneDocumentoFisico().getCollocazioneDettaglio(keyLoc, ticket);
		return coll;
	}
	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
	private String getChiaviTitoloAutore(String tipo, String bidVid, String ticket) throws Exception {
		String chiave = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		chiave = factory.getGestioneDocumentoFisico().getChiaviTitoloAutore(tipo, bidVid, ticket);
		return chiave;
	}
	private FormatiSezioniVO getFormatiSezioniDettaglio(String codPolo, String codBib, String codSez,
			String codFormato, String ticket)
	throws Exception {
		FormatiSezioniVO rec;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getFormatiSezioniDettaglio(codPolo, codBib, codSez, codFormato, ticket);
		return rec;
	}

	private ProvenienzaInventarioVO getProvenienza(String codPolo, String codBib, String codProven, String ticket) throws Exception {
		try {
			ProvenienzaInventarioVO provenienza;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			provenienza = factory.getGestioneDocumentoFisico().getProvenienza(codPolo, codBib, codProven, ticket);
			return provenienza;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
//			if (idCheck.equalsIgnoreCase("soggetti") ){
//				UserVO utente = Navigation.getInstance(request).getUtente();
//				try {
//					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_RICERCA_CLASSI_SOGGETTI_PER_COLLOCAZIONE, currentForm.getCodPolo(), currentForm.getCodBib(), null);
//					return true;
//				} catch (Exception e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, currentForm.getCodPolo(), currentForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	private List insertInventario(InventarioVO inventario, String tipoOperazione, int nInv, Locale locale, String ticket, ActionForm form) throws Exception {
		VaiAInserimentoCollForm currentForm = (VaiAInserimentoCollForm)form;
		List inv;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		inv = factory.getGestioneDocumentoFisico().insertInventario(inventario, tipoOperazione, nInv, locale, ticket);

		return inv;
	}
	private List getTitolo(String bid, String ticket) throws Exception {
		List titolo = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}
	private SerieVO getSerieDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SerieVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSerieDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
	private InventarioVO getInventario(String codPolo, String codBib, String codSerie, int codInv, Locale locale, String ticket) throws Exception {
		InventarioVO recInv = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		recInv = factory.getGestioneDocumentoFisico().getInventario(codPolo, codBib, codSerie, codInv, locale, ticket);
		return recInv;
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.datiInventari
	  * VaiAInserimentoInvAction.java
	  * localizzaGestione
	  * void
	  * @param request
	  * @param myForm
	  * @throws Exception
	  *
	  *
	 */
	private void localizzaGestione(HttpServletRequest request,
			VaiAInserimentoCollForm currentForm) throws Exception {

		AreaDatiVariazioneReturnVO advr = null;
		AreaDatiLocalizzazioniAuthorityVO al = null;
		AreaDatiLocalizzazioniAuthorityMultiplaVO adlam = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
		List lista = this.getTitolo((String)request.getAttribute("bid"), currentForm.getTicket());
		TitoloVO titolo = null;
		if (lista != null){
			if (lista.size() == 1){
				titolo = (TitoloVO)lista.get(0);
			}
		}else{
			throw new ValidationException("titNotFoundPolo", ValidationException.errore);
		}

		if (titolo != null){
			al = new AreaDatiLocalizzazioniAuthorityVO();
			if (titolo.isFlagCondiviso()){
				al.setIndice(true);
				al.setPolo(false);
				al.setTipoLoc("Gestione");
				al.setIdLoc(currentForm.getBid());
				al.setNatura(titolo.getNatura());
				al.setAuthority("");
				al.setTipoMat(titolo.getTipoMateriale());
				al.setTipoOpe("Localizza");
				al.setCodiceSbn(Navigation.getInstance(request).getUtente().getCodPolo()+
						currentForm.getCodBib());

				adlam.addListaAreaLocalizVO(al);
			}
			advr = this.localizzaAuthorityMultipla(adlam, currentForm.getTicket());

			request.setAttribute("codBib",currentForm.getRecInv().getCodBib());
			request.setAttribute("descrBib",currentForm.getDescrBib());
			request.setAttribute("codSerie",currentForm.getRecInv().getCodSerie());
			request.setAttribute("codInvent",currentForm.getRecInv().getCodInvent());
			request.setAttribute("bid", currentForm.getBid());
			request.setAttribute("desc", currentForm.getTitolo());
			//
			if (advr != null && (advr.getCodErr().equals("9999") || ValidazioneDati.isFilled(advr.getTestoProtocollo()) )) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo", advr.getTestoProtocollo()));
				//							return mapping.findForward("listaInventariDelTitolo");
			} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
				if (!advr.getCodErr().equals("7017")){
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + advr.getCodErr()));
					//								return mapping.findForward("listaInventariDelTitolo");
				}
			}
		}
	}

	private AreaDatiVariazioneReturnVO localizzaAuthorityMultipla(AreaDatiLocalizzazioniAuthorityMultiplaVO area, String ticket) throws Exception {
		AreaDatiVariazioneReturnVO adv;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

	// Evolutiva Google3: Viene creato il nuovo metodo localizzaUnicoXML che effettua la chiamata per localizzazione tramite
	// un unica chiamata ai protocollo di Indice/Polo utilizzando sempre l'area di passaggio AreaDatiLocalizzazioniAuthorityMultiplaVO
	// questo metodo sostituirà localizzaAuthorityMultipla e localizzaAuthority
//	adv = factory.getGestioneBibliografica().localizzaAuthorityMultipla(area, ticket);
	adv = factory.getGestioneBibliografica().localizzaUnicoXML(area, ticket);
	return adv;
	}

}
