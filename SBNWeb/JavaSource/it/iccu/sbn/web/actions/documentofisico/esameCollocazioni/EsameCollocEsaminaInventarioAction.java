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

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceNotaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.NotaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.ListeInventariForm;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocEsaminaInventarioForm;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
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

public class EsameCollocEsaminaInventarioAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	private static Logger log = Logger.getLogger(EsameCollocEsaminaInventarioAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.bottone.disponibilita", "disponibilita");
		map.put("documentofisico.bottone.etichetta", "etichetta");
		map.put("documentofisico.bottone.modificaInv", "modificaInv");
		map.put("documentofisico.bottone.scriviRfid", "scriviRfid");
		map.put("documentofisico.bottone.indietro", "chiudi");

		map.put("documentofisico.bottone.moduloPrelievo", "moduloPrelievo");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		//setto il token per le transazioni successive
		this.saveToken(request);
		EsameCollocEsaminaInventarioForm currentForm = (EsameCollocEsaminaInventarioForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ){
			return mapping.getInputForward();
		}else{
			if (navi.isFromBack()){
				String chiamante = navi.getBackAction();
				log.info("chiamante back: " + chiamante);
			}
		}
		// controllo se ho già i dati in sessione;
		if(!currentForm.isSessione()) {
			currentForm.setTicket(navi.getUserTicket());
			currentForm.setCodBib(navi.getUtente().getCodBib());
			currentForm.setCodPolo(navi.getUtente().getCodPolo());
			currentForm.setDescrBib(navi.getUtente().getBiblioteca());
			currentForm.setSessione(true);
		}
		try {
			if (request.getAttribute("codBib")!=null
					&& request.getAttribute("codSerie")!=null
					&& request.getAttribute("codInvent")!= null ){
				currentForm.setCodBib((String)request.getAttribute("codBib"));
				currentForm.setCodSerie((String)request.getAttribute("codSerie"));
				currentForm.setCodInvent((request.getAttribute("codInvent").toString()));
				InventarioDettaglioVO invEsam = this.getInventarioDettaglio(currentForm.getCodPolo(), currentForm.getCodBib(),
						currentForm.getCodSerie(), Integer.parseInt(currentForm.getCodInvent()), this.getLocale(request, Constants.SBN_LOCALE), currentForm.getTicket());
				if (invEsam != null) {
					if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("collDef")){
						currentForm.setProv("collDef");
						navi.setDescrizioneX("Collocazione Definitiva");
						navi.setTesto("Collocazione Definitiva");
							invEsam.setCollCodSez(invEsam.getSezOld());
							invEsam.setCollCodLoc(invEsam.getLocOld());
							invEsam.setCollSpecLoc(invEsam.getSpecOld());
					}
					if (invEsam.getDataIngresso() == null){
						invEsam.setDataIngresso(invEsam.getDataIns());
					}
					currentForm.setRecInvDett(invEsam);
				}else{
					throw new ValidationException("ricercaNoInv", ValidationException.errore);
				}
					this.loadPagina(form);
					currentForm.setDescrBib((String)request.getAttribute("descrBib"));
//				}
				List<TitoloVO> lista = this.getTitolo(currentForm.getRecInvDett().getBid(), currentForm.getTicket());
				if (lista != null){
					if (lista.size() == 1) {
						TitoloVO titolo = lista.get(0);
						currentForm.getRecInvDett().setTitIsbd(titolo.getIsbd());
						if (titolo.getNatura().equals("S")){
							currentForm.setPeriodico(true);
						}
					}
				}
				if (currentForm.getRecInvDett().getKeyLoc() != 0){
					List<TitoloVO> listaTitColl = this.getTitolo(currentForm.getRecInvDett().getCollBidLoc(), currentForm.getTicket());
					if (listaTitColl != null){
						if (listaTitColl.size() == 1){
							TitoloVO titolo = listaTitColl.get(0);
							currentForm.setIsbdDiCollocazione(titolo.getIsbd());
						}
					}
					if (currentForm.getRecInvDett().getKeyLocOld() != null){
					}
				}
				if (currentForm.getRecInvDett().getKeyLocOld() != null && (Integer.parseInt(currentForm.getRecInvDett().getKeyLocOld()) > 0)){
//					invEsam.setCollCodSez(invEsam.getSezOld());
//					invEsam.setCollCodLoc(invEsam.getLocOld());
//					invEsam.setCollSpecLoc(invEsam.getSpecOld());
					if (invEsam.getSezOld().endsWith("Squadratura")){
						//
						invEsam.setSezOld(invEsam.getSezOld().substring(0, 9));
						//

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.squadratura"));

						return mapping.getInputForward();
					}
				}
			}
			if (currentForm.getRecInvDett().getIdAccessoRemoto() != null && currentForm.getRecInvDett().getIdAccessoRemoto().trim().equals("")){
				currentForm.getRecInvDett().setIdAccessoRemoto(currentForm.getRecInvDett().getIdAccessoRemoto().trim());
			}
			if (request.getAttribute("prov") != null && (request.getAttribute("prov").equals("interrogazioneEsame"))){
//				myForm.setProv("posseduto");
				currentForm.setProv("interrogazioneEsame");
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			String chiamante = navi.getActionCaller();
			if (chiamante.toUpperCase().indexOf("ESAMECOLLOCRICERCA") > -1 ) {
				return navi.goBack(true);
//				return mapping.findForward("esameCollocRicerca");
			}
			//almaviva5_20151112 segnalazione BVE: loop su fornitore cancellato
			if (chiamante.toUpperCase().indexOf("ESAMECOLLOCESAMINAPOSS") > -1 )
				return navi.goBack(true);
			NavigationCache cache = navi.getCache();
			NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
			if (prev != null && prev.getForm() instanceof ListeInventariForm) {
				ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
				request.setAttribute("tipoLista", listaInvForm.getTipoLista());
				request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
				request.setAttribute("codBib", listaInvForm.getCodBib());
				request.setAttribute("descrBib", listaInvForm.getDescrBib());
				return navi.goBack();
			}
			return navi.goBack();
		} catch (DataException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			String chiamante = navi.getActionCaller();
			if (chiamante.toUpperCase().indexOf("ESAMECOLLOCRICERCA") > -1 ) {
				return navi.goBack(true);
//				return mapping.findForward("esameCollocRicerca");
			}
			return navi.goBack();
		}
		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		try {
//			String chiamante = Navigation.getInstance(request).getActionCaller();
//			if (chiamante.toUpperCase().indexOf("ESAMECOLLOCRICERCA") > -1 ) {
//				return mapping.findForward("esameCollocRicerca");
//			}

			Navigation navi = Navigation.getInstance(request);
			NavigationElement prev = navi.getCache().getElementAt(navi.getCache().getCurrentPosition() - 1);
			if (prev != null && prev.getForm() instanceof ListeInventariForm) {
				if (prev.getDescrizione().equals("Lista Inventari Possessori"))
					return navi.goBack(true);

				if (prev.getDescrizione().equals("Lista Inventari Ordine"))
					return navi.goBack(true);

				//almaviva5_20101209 periodici
				if (navi.bookmarksExist(PeriodiciDelegate.BOOKMARK_FASCICOLO, PeriodiciDelegate.BOOKMARK_KARDEX))
					return navi.goBack(true);

				ListeInventariForm listaInvForm = (ListeInventariForm) prev.getForm();
				request.setAttribute("tipoLista", listaInvForm.getTipoLista());
				request.setAttribute("paramRicerca", listaInvForm.getParamRicerca());
				request.setAttribute("codBib", listaInvForm.getCodBib());
				request.setAttribute("descrBib", listaInvForm.getDescrBib());
				return navi.goBack();
			}else{
				//almaviva5_20110415 #4347
				if (navi.bookmarksExist(Bookmark.Servizi.DETTAGLIO_MOVIMENTO))
					return navi.goBack(true);

				request.setAttribute("indietro", "indietro");
				return navi.goBack();
			}
//			return Navigation.getInstance(request).goBack(true);

		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward disponibilita(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
//		richiama un servizio di Servizi a fronte di un codSerie, codInv
		EsameCollocEsaminaInventarioForm myForm = (EsameCollocEsaminaInventarioForm)form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			InventarioTitoloVO invTit = new InventarioTitoloVO(myForm.getRecInvDett());
			MovimentoVO movimento = new MovimentoRicercaVO();
			movimento.setCodPolo(myForm.getCodPolo());
			movimento.setCodBibOperante(myForm.getCodBib());
			movimento.setCodBibInv(invTit.getCodBib());
			movimento.setCodSerieInv(invTit.getCodSerie());
			movimento.setCodInvenInv(String.valueOf(invTit.getCodInvent()));
			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE, movimento);
			InfoDocumentoVO infoDoc = new InfoDocumentoVO();
			infoDoc.setInventarioTitoloVO(invTit);
			request.setAttribute("InfoDocumentoVO", infoDoc);
			request.setAttribute("TipoRicerca", RicercaRichiesteType.RICERCA_PER_INVENTARIO);
			return Navigation.getInstance(request).goForward(mapping.findForward("SIFServizi"));
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward etichetta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();
		EsameCollocEsaminaInventarioForm myForm = (EsameCollocEsaminaInventarioForm)form;
		request.setAttribute("codBib", myForm.getCodBib());
		request.setAttribute("codSerie",myForm.getCodSerie());
		request.setAttribute("codInvent",myForm.getCodInvent());
		request.setAttribute("descrBib",myForm.getDescrBib());
		EtichettaDettaglioVO datiStampaEtichette = new EtichettaDettaglioVO();
		datiStampaEtichette.setBiblioteca(myForm.getDescrBib());
		datiStampaEtichette.setInventario(new Integer(myForm.getRecInvDett().getCodInvent()).toString());
		datiStampaEtichette.setPrecisazione("Precisato");
		datiStampaEtichette.setSequenza(myForm.getRecInvDett().getSeqColl());
		datiStampaEtichette.setSerie(myForm.getRecInvDett().getCodSerie());
		if (myForm.getRecInvDett().getKeyLoc() > 0){
			datiStampaEtichette.setCollocazione(myForm.getRecInvDett().getCollCodLoc());
			datiStampaEtichette.setSezione(myForm.getRecInvDett().getCollCodSez());
			datiStampaEtichette.setSpecificazione(myForm.getRecInvDett().getCollSpecLoc());
		}else{
			datiStampaEtichette.setCollocazione("");
			datiStampaEtichette.setSezione("");
			datiStampaEtichette.setSpecificazione("");
		}
		listaEtichetteBarcode.add(datiStampaEtichette);
		request.setAttribute("codBib", myForm.getCodBib());
		request.setAttribute("descrBib",myForm.getDescrBib());
		request.setAttribute("listaEtichetteBarcode", "listaEtichetteBarcode");
		request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ETICHETTE);
//		request.setAttribute("DATI_STAMPE_ON_LINE", datiStampaEtichette);//modifica per stampa barcode
		request.setAttribute("DATI_STAMPE_ON_LINE", listaEtichetteBarcode);
		return  mapping.findForward("stampaSintetica");
	}

	public ActionForward moduloPrelievo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsameCollocEsaminaInventarioForm currentForm = (EsameCollocEsaminaInventarioForm)form;

		request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_MODULO_PRELIEVO);
		request.setAttribute("DATI_STAMPE_ON_LINE", currentForm.getRecInvDett());

		return  mapping.findForward("stampaSintetica");
	}

	public ActionForward modificaInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try{
			EsameCollocEsaminaInventarioForm myForm = (EsameCollocEsaminaInventarioForm)form;
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			request.setAttribute("codBib", myForm.getRecInvDett().getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("bid", myForm.getRecInvDett().getBid());
			request.setAttribute("titolo", myForm.getRecInvDett().getTitIsbd());
			request.setAttribute("codSerie", myForm.getRecInvDett().getCodSerie());
			request.setAttribute("codInvent", myForm.getRecInvDett().getCodInvent());
			if (myForm.getRecInvDett().getKeyLoc() == 0){
				request.setAttribute("recColl", null);
				request.setAttribute("reticolo", null);
			}else{
				myForm.setReticolo(this.getAnaliticaPerCollocazione(myForm.getRecInvDett().getBid(), myForm.getTicket(), form));
				CollocazioneVO collocazione = this.getCollocazione(myForm.getRecInvDett().getKeyLoc(), myForm.getTicket());
				if (collocazione != null){
					myForm.setRecColl(collocazione);
				}
				request.setAttribute("recColl", myForm.getRecColl());
				request.setAttribute("reticolo", myForm.getReticolo());
			}
			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("tipoOperazione", DocumentoFisicoCostant.M_MODIFICA_INV);
			return mapping.findForward("modificaInv");
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

	private void loadPagina(ActionForm form) throws Exception {
//		inventario
		EsameCollocEsaminaInventarioForm myForm = (EsameCollocEsaminaInventarioForm)form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if (myForm.getRecInvDett().isPrecisato() ) {
			myForm.setDescrSitAmm("precisato");
		}else if (myForm.getRecInvDett().isCollocato() ) {
			myForm.setDescrSitAmm("collocato");
		}else{
			myForm.setDescrSitAmm("dismesso");
		}
		if (myForm.getRecInvDett().getListaNote() != null && myForm.getRecInvDett().getListaNote().size() >= 1){
			List<CodiceNotaVO> listaNote = new ArrayList<CodiceNotaVO>();
			for (int i=0; i<myForm.getRecInvDett().getListaNote().size();i++){
				NotaInventarioVO nota = myForm.getRecInvDett().getListaNote().get(i);
				CodiceNotaVO codiceNota = new CodiceNotaVO();
				String codDescNota = (nota.getCodNota() + " "+ factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_NOTE_CODIFICATE_INVENTARIO, nota.getCodNota()));
				codiceNota.setCodice1(codDescNota);
				codiceNota.setDescrizione1(nota.getDescrNota());
				listaNote.add(codiceNota);
			}
			myForm.setListaNote(listaNote);
		}
		myForm.setDescrTipoFruizione(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_CATEGORIA_FRUIZIONE, myForm.getRecInvDett().getCodFrui()));
		myForm.setDescrMatInv(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE, myForm.getRecInvDett().getCodMatInv()));
		myForm.setDescrStatoConser(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_STATI_DI_CONSERVAZIONE, myForm.getRecInvDett().getStatoConser()));
		myForm.setDescrRiproducibilta(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM, myForm.getRecInvDett().getCodRiproducibilita()));
		myForm.setDescrSupportoCopia(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_SUPPORTO_COPIA, myForm.getRecInvDett().getSupportoCopia()));
		//myForm.setDescrTipoAcquisizione(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE, myForm.getRecInvDett().getCodTipoOrd()));
		myForm.setDescrTipoAcquisizione(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE, myForm.getRecInvDett().getTipoAcquisizione()));
		myForm.setDescrNoDispo(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_NON_DISPONIBILITA, myForm.getRecInvDett().getCodNoDisp()));
		myForm.setDescrTecaDigitale(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_TECHE_DIGITALI, myForm.getRecInvDett().getRifTecaDigitale()));
		myForm.setDescrTipoDigit(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE, myForm.getRecInvDett().getDigitalizzazione()));
		myForm.setDescrDispDaRemoto(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_DISP_ACCESSO_REMOTO, myForm.getRecInvDett().getDispDaRemoto()));
		myForm.setDescrCodCarico(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MOTIVI_DI_CARICO_INVENTARIALE, myForm.getRecInvDett().getCodCarico()));
		if (myForm.getRecInvDett().getDataScarico().equals("31/12/9999") || myForm.getRecInvDett().getDataScarico().equals("01/01/0001")){
			myForm.getRecInvDett().setDataScarico("00/00/0000");
		}
		if (myForm.getRecInvDett().getDataDelibScar().equals("31/12/9999") || myForm.getRecInvDett().getDataDelibScar().equals("01/01/0001")){
			myForm.getRecInvDett().setDataDelibScar("00/00/0000");
		}
		if (!myForm.getRecInvDett().getDataCarico().trim().equals("")){
			if (myForm.getRecInvDett().getDataCarico().substring(0, 10).equals("31/12/9999") || myForm.getRecInvDett().getDataCarico().substring(0, 10).equals("01/01/0001")){
				myForm.getRecInvDett().setDataCarico("");
			}
		}


	}
	private InventarioDettaglioVO getInventarioDettaglio(String codPolo, String codBib, String codSerie, int codInvent,
			Locale locale, String ticket) throws Exception {
		InventarioDettaglioVO inventario;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		inventario = factory.getGestioneDocumentoFisico().getInventarioDettaglio(codPolo, codBib, codSerie, codInvent, locale, ticket);
		return inventario;
	}
	private DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String ticket, ActionForm form) throws Exception {
		EsameCollocEsaminaInventarioForm myForm = (EsameCollocEsaminaInventarioForm)form;
		DatiBibliograficiCollocazioneVO reticolo;
		String bibliotecaOperante = myForm.getCodPolo() + myForm.getCodBib();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		reticolo = factory.getGestioneDocumentoFisico().getAnaliticaPerCollocazione(bid, bibliotecaOperante, ticket);
		if (reticolo == null)  {
			myForm.setNoReticolo(true);
		}else{
			myForm.setNoReticolo(false);
		}
		return reticolo;
	}

	private CollocazioneVO getCollocazione(int keyLoc, String ticket) throws Exception {
		CollocazioneVO collocazione;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		collocazione = factory.getGestioneDocumentoFisico().getCollocazione(keyLoc, ticket);
		return collocazione;
	}

	private List<TitoloVO> getTitolo(String bid, String ticket) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<TitoloVO> titolo = factory.getGestioneDocumentoFisico().getTitolo(bid, ticket);
		return titolo;
	}

	public ActionForward scriviRfid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		return mapping.getInputForward(); // Fai assolutamente niente e riemetti la pagina

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		EsameCollocEsaminaInventarioForm myForm = (EsameCollocEsaminaInventarioForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			Navigation navi = Navigation.getInstance(request);
			if (idCheck.equalsIgnoreCase("etichette") ){

				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			if (idCheck.equalsIgnoreCase("possessori") ){

				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, myForm.getCodPolo(), myForm.getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			//almaviva5_20110415 #4347
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.DETTAGLIO_MOVIMENTO))
				return navi.bookmarksExist(Bookmark.Servizi.DETTAGLIO_MOVIMENTO);

			//almaviva5_20190128
			if (ValidazioneDati.equals(idCheck, "MODULO_PRELIEVO"))
				return true;

			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

}
