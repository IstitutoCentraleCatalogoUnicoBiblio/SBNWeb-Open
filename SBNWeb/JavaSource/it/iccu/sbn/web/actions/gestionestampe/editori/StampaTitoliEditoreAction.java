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
package it.iccu.sbn.web.actions.gestionestampe.editori;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTitoliEditoreVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.editori.StampaTitoliEditoreForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class StampaTitoliEditoreAction extends ReportAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("editori.label.editore","fornitoreCerca");
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		try {

			StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);

			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
				return mapping.getInputForward();

			// controllo se ho già i dati in sessione;
			if(!currentForm.isSessione())	{
				currentForm.setTicket(navi.getUserTicket());
				currentForm.setCodBib(navi.getUtente().getCodBib());
				currentForm.setCodPolo(navi.getUtente().getCodPolo());
				currentForm.setDescrBib(navi.getUtente().getBiblioteca());
				this.loadPagina(form);
				currentForm.setSessione(true);
			}

			currentForm.setElencoModelli(getElencoModelli());
			currentForm.setTipoModello(currentForm.getElencoModelli().get(0).getJrxml());
			currentForm.setTipoFormato(TipoStampa.XLS.name());

			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				currentForm.setCodBib((String) request.getAttribute("codBib"));
				currentForm.setDescrBib((String)request.getAttribute("descrBib"));
			}

			if (request.getAttribute("cartiglioEditore") != null  && request.getAttribute("cartiglioEditore").equals("SI")) {
				currentForm.setCodEditore((String) request.getAttribute("codForn"));
				currentForm.setDescrEditore((String) request.getAttribute("descrForn"));
			}
			if (currentForm.getRegione() != null && !currentForm.getRegione().equals("")){
				currentForm.setListaProvincie(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE)));
				List<CodiceVO> listaProvReg = new ArrayList<CodiceVO>();
				for (int i = 0; i < currentForm.getListaProvincie().size(); i++) {
					CodiceVO prov = (CodiceVO)currentForm.getListaProvincie().get(i);
					if (prov.getTerzo() != null){
						if (currentForm.getRegione().equals(prov.getTerzo())){
							listaProvReg.add(prov);
						}
					}else{
						prov.setCodice("");
						prov.setDescrizione("");
						listaProvReg.add(prov);
					}
				}
				currentForm.setListaProvincie(listaProvReg);
			}else{
				currentForm.setListaProvincie(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE)));
			}
			if (currentForm.getCheckEditore()!= null && currentForm.getCheckEditore().equals("editore")){
				currentForm.setDisableCodEditore(false);
				currentForm.setDisableDescrEditore(false);
				currentForm.setDisableIsbn(true);
				currentForm.setDisableRegione(true);
				currentForm.setDisablePaese(true);
				currentForm.setDisableProvincia(true);
				currentForm.setIsbn("");
				currentForm.setRegione("");
				currentForm.setProvincia("");
				currentForm.setPaese("");
			}
			if (currentForm.getCheckEditore()!= null && currentForm.getCheckEditore().equals("regione")){
				currentForm.setDisableRegione(false);
				currentForm.setDisablePaese(true);
				currentForm.setDisableProvincia(false);
				currentForm.setDisableIsbn(true);
				currentForm.setDisableCodEditore(true);
				currentForm.setDisableDescrEditore(true);
				currentForm.setIsbn("");
				currentForm.setCodEditore("");
				currentForm.setDescrEditore("");
				currentForm.setPaese("");
			}
			if (currentForm.getCheckEditore()!= null && currentForm.getCheckEditore().equals("isbn")){
				currentForm.setDisableIsbn(false);
				currentForm.setDisablePaese(true);
				currentForm.setDisableRegione(true);
				currentForm.setDisableProvincia(true);
				currentForm.setDisableCodEditore(true);
				currentForm.setDisableDescrEditore(true);
				currentForm.setPaese("");
				currentForm.setRegione("");
				currentForm.setProvincia("");
				currentForm.setCodEditore("");
				currentForm.setDescrEditore("");
			}
			if (currentForm.getCheckEditore()!= null && currentForm.getCheckEditore().equals("paese")){
				currentForm.setDisablePaese(false);
				currentForm.setDisableIsbn(true);
				currentForm.setDisableRegione(true);
				currentForm.setDisableProvincia(true);
				currentForm.setDisableCodEditore(true);
				currentForm.setDisableDescrEditore(true);
				currentForm.setRegione("");
				currentForm.setProvincia("");
				currentForm.setCodEditore("");
				currentForm.setDescrEditore("");
			}

		} catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward conferma (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);
		try{
			StampaTitoliEditoreVO steVO = new StampaTitoliEditoreVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			CodiceVO date = null;
			steVO.setCodAttivita(CodiciAttivita.getIstance().STAMPA_TITOLI_EDITORI);
			steVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
			steVO.setCodPolo(currentForm.getCodPolo());
			steVO.setCodBib(currentForm.getCodBib());

			//editore
			if (currentForm.getCheckEditore() != null && currentForm.getCheckEditore().equals("editore")){
				if (currentForm.getCodEditore() != null && currentForm.getCodEditore().equals("")){
						if (currentForm.getDescrEditore() != null){
							if (currentForm.getDescrEditore().equals("")){
								throw new ValidationException("indicareAlmenoUnFiltroEditore");
							}else{
								throw new ValidationException("codEditoreMancante");
//								if (currentForm.getDescrEditore().trim().length() > 50){
//									throw new ValidationException("descrEditoreMinoreUguale50");
//								}
							}
						}

				}else{

				}
			}else if (currentForm.getCheckEditore() != null && currentForm.getCheckEditore().equals("regione")){
				if (currentForm.getRegione() != null && currentForm.getRegione().equals("")){
					if (currentForm.getProvincia() != null && currentForm.getProvincia().equals("")){
						throw new ValidationException("indicareAlmenoUnFiltroRegione");
					}
				}
			}else if (currentForm.getCheckEditore() != null && currentForm.getCheckEditore().equals("isbn")){
				if (currentForm.getIsbn() != null && currentForm.getIsbn().equals("")){
					throw new ValidationException("indicareFiltroIsbn");
				}
			}else if (currentForm.getCheckEditore() != null && currentForm.getCheckEditore().equals("paese")){
				if (currentForm.getPaese() != null && currentForm.getPaese().equals("")){
					throw new ValidationException("indicareAlmenoUnFiltroRegione");
				}
			}else{
				throw new ValidationException("checkNonImpostato");
			}
			steVO.setCodEditore(currentForm.getCodEditore());
			steVO.setDescrEditore(currentForm.getDescrEditore());
			steVO.setIsbn(currentForm.getIsbn());
			steVO.setRegione(currentForm.getRegione());
			steVO.setProvincia(currentForm.getProvincia());
			steVO.setPaese(currentForm.getPaese());

			//titolo
				if (currentForm.getDataPubbl1Da() != null && currentForm.getDataPubbl1Da().trim().equals("")){
					if (currentForm.getDataPubbl1A() != null && currentForm.getDataPubbl1A().trim().equals("")){
						steVO.setDataPubbl1Da("");
						steVO.setDataPubbl1A("");
					}else{
						throw new ValidationException("valorizzareDataDa");
					}
				}else{
					if (!ValidazioneDati.strIsNumeric(currentForm.getDataPubbl1Da())){
						throw new ValidationException("numericoDataDa");
					}
					if (currentForm.getDataPubbl1Da().length() > 0 && currentForm.getDataPubbl1Da().length() < 4){
						throw new ValidationException("lunghezzaDataDa4");
					}

					if (currentForm.getDataPubbl1A() != null && currentForm.getDataPubbl1A().trim().equals("")){
						steVO.setDataPubbl1Da(currentForm.getDataPubbl1Da().trim());
						steVO.setDataPubbl1A("");
					}else{
						if (!ValidazioneDati.strIsNumeric(currentForm.getDataPubbl1A())){
							throw new ValidationException("numericoDataA");
						}
						if (currentForm.getDataPubbl1A().length() > 0 && currentForm.getDataPubbl1A().length() < 4){
							throw new ValidationException("lunghezzaDataA4");
						}
						if (Integer.valueOf(currentForm.getDataPubbl1Da()) > Integer.valueOf(currentForm.getDataPubbl1A())){
							throw new ValidationException("dataDaMinoreDataA");
						}
						steVO.setDataPubbl1Da(currentForm.getDataPubbl1Da().trim());
						steVO.setDataPubbl1A(currentForm.getDataPubbl1A().trim());
					}
				}
			steVO.setTipoRecord(currentForm.getTipoRecord());
			steVO.setLingua(currentForm.getLingua());
			steVO.setNatura(currentForm.getNatura());
			if (currentForm.getCheckTipoPosseduto() != null){
				if (currentForm.getCheckTipoPosseduto().equals("titPossTutti")){
					steVO.setCheckTipoPosseduto("T");
				}else if (currentForm.getCheckTipoPosseduto().equals("titPossSoloBib")){
					steVO.setCheckTipoPosseduto("P");
				}else if (currentForm.getCheckTipoPosseduto().equals("titNonPossBib")){
					steVO.setCheckTipoPosseduto("NP");
				}
			}

			//inventario
			if (currentForm.getDataIngressoDa() != null && !currentForm.getDataIngressoDa().trim().equals("")
					|| currentForm.getDataIngressoA() != null && !currentForm.getDataIngressoA().trim().equals("")){
				date = controlloDate(currentForm.getDataIngressoDa(), currentForm.getDataIngressoA());
				steVO.setDataIngressoDa(date.getCodice());
				steVO.setDataIngressoA(date.getDescrizione());
			}else{
				steVO.setDataIngressoDa("");
				steVO.setDataIngressoA("");
			}
			steVO.setTipoAcq(currentForm.getTipoAcq());

			// Maggio 2013 inserito il filtro per Tipo materiale inventariabile
			steVO.setCodiceTipoMateriale(currentForm.getCodiceTipoMateriale());


			//classificazione
			steVO.setSistema(currentForm.getSistema());
			steVO.setSimbolo(currentForm.getSimbolo());

			if (currentForm.getCodEditore() != null && !currentForm.getCodEditore().trim().equals("") ||
					currentForm.getDescrEditore() != null && !currentForm.getDescrEditore().trim().equals("") ||
					currentForm.getRegione() != null && !currentForm.getRegione().trim().equals("") ||
					currentForm.getPaese() != null && !currentForm.getPaese().trim().equals("") ||
					currentForm.getIsbn() != null && !currentForm.getIsbn().trim().equals("")){
			}else{
				throw new ValidationException("indicareAlmenoUnFiltroRiquadroEditore");
			}

			for (int index2 = 0; index2 < currentForm.getElencoModelli().size(); index2++) {
				ModelloStampaVO recMod = currentForm.getElencoModelli().get(index2);
				if (recMod.getJrxml() != null)
					steVO.setNomeSubReport(recMod.getSubReports().get(0) + ".jasper");

			}
			currentForm.setElencoModelli(getElencoModelli());
			currentForm.setTipoFormato(TipoStampa.XLS.name());
			//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.
			List<StampaTitoliEditoreVO> parametri = new ArrayList<StampaTitoliEditoreVO>();
			parametri.add(steVO);
			request.setAttribute("DatiVo", parametri);


			String fileJrxml = currentForm.getTipoModello();
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
			//NB: Se voglio memorizzare sul server
			//String pathDownload = basePath+File.separator+"download";
			String pathDownload = StampeUtil.getBatchFilesPath();

			//Se voglio memorizzare in locale

			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setTipoStampa(currentForm.getTipoFormato());
			stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
			stam.setCodPolo(currentForm.getCodPolo());
			stam.setCodBib(currentForm.getCodBib());

			stam.setTipoOrdinamento("");
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setCodAttivita(CodiciAttivita.getIstance().STAMPA_TITOLI_EDITORI);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setTipoOperazione("STAMPA_TITOLI_EDITORI");
			stam.setTicket(currentForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idMessaggio = factory.getStampeOnline().stampaTitoliEditore(stam);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));

			this.saveErrors(request, errors);

//			currentForm.setDisable(true);
			return mapping.getInputForward();

		} catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.stampaTitoliEditore."+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.stampaTitoliEditore."+ e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	/**
	  * it.iccu.sbn.web.actions.gestionestampe.editori
	  * StampaTitoliEditoreAction.java
	  * controlloDate
	  * void
	  * @param currentForm
	  * @throws ValidationException
	  *
	  *
	 */
	private CodiceVO controlloDate(String dataDa, String dataA)
			throws ValidationException {
		CodiceVO rec = new CodiceVO();
		int codRitorno = -1;
		if (dataDa.trim().equals("")){
			if (dataA.trim().equals("")
					|| dataA.equals("00/00/0000")
					|| !dataA.equals("00/00/0000")){
				codRitorno = ValidazioneDati.validaDataPassata(dataA);
				if (codRitorno != ValidazioneDati.DATA_OK){
					throw new ValidationException("dataDaErrata");
				}
			}
		}else{
			if (!dataDa.trim().equals("") && dataDa.equals("00/00/0000")){
				throw new ValidationException("formatoDataDaNonValido");
			}else{
				codRitorno = ValidazioneDati.validaDataPassata(dataDa);
				if (codRitorno != ValidazioneDati.DATA_OK){
					throw new ValidationException("dataDaErrata");
				}else{
					if (dataA != null && !dataA.trim().equals("")) {
						if (dataA.equals("00/00/0000")){
							throw new ValidationException("formatoDataANonValido");
						}else{
							codRitorno = ValidazioneDati.validaDataPassata(dataA);
							if (codRitorno != ValidazioneDati.DATA_OK){
								throw new ValidationException("dataAErrata");
							}else{
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
								  long longData1 = 0;
								  long longData2 = 0;

								try {
									  Date data1 = simpleDateFormat.parse(dataDa.trim());
									  Date data2 = simpleDateFormat.parse(dataA.trim());
									  longData1 = data1.getTime();
									  longData2 = data2.getTime();
								  } catch (ParseException e) {
									  throw new ValidationException("erroreParse");
								  }

								  if ((longData2 - longData1) < 0) {
									  throw new ValidationException("dataDaAErrata");
								  }
							}
						}
					}
				}
			}
		}
		rec.setCodice(dataDa);
		rec.setDescrizione(dataA);
		return rec;
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
//			return mapping.findForward("indietro");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadPagina(ActionForm form) throws Exception {
		StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);
		CaricamentoCombo carCombo = new CaricamentoCombo();

		//Ottobre 2013: gestione della stampa titoli per Editore anche per Paese
		currentForm.setListaPaesi(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_PAESE)));
		currentForm.setListaProvincie(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE)));
		currentForm.setListaRegioni(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_REGIONE)));
		currentForm.setListaTipiRecord(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC)));
		currentForm.setListaLingue(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_LINGUA)));
		currentForm.setListaTipoAcq(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE)));
//		Maggio 2013: Inserito il manca il filtro per Tipo materiale inventariabile
		currentForm.setListaTipoMateriale(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE)));

		currentForm.setListaClassificazioni(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_SISTEMA_CLASSE)));

//		Maggio 2013:Modifica la lista delle nature utilizzabili nella mappa dirichiesta Stampa titoli x Editore
//		intervento è stato eseguito inserendo le nature  M,W,S e C
//		currentForm.setListaNature(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_NATURA_BIBLIOGRAFICA)));
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		currentForm.setListaNature(carCombo.loadComboCodiciDescNatura(factory.getCodici().getCodiceNaturaBibliografica(),
				"Solo_nature_per_editori", ""));

		currentForm.setCheckEditore("editore");
		currentForm.setCheckTipoPosseduto("titPossTutti");
		currentForm.setCheckTipoRicerca("parole");

	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().STAMPA_TITOLI_EDITORI, currentForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().STAMPA_TITOLI_EDITORI);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	private FornitoreVO getFornitore(String codPolo, String codBib, String codFornitore,
			String descr, String ticket) throws Exception {
				FornitoreVO rec = null;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				rec = factory.getGestioneAcquisizioni().getFornitore(codPolo, codBib, codFornitore, descr, ticket);
				if (rec.getRegione() != null){
					return rec;
				}
				return rec;
			}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);
//		try {
//			ActionForward forward = fornitoreCercaVeloce(mapping, request, currentForm);
//			if (forward != null){
//				return forward;
//			}
//		} catch (Exception e) {
//			return mapping.getInputForward();
//		}
//		return mapping.getInputForward();

//		StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);

		request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
		FornitoreVO fornVO = null;
		try{
			if (currentForm.getCodEditore() != null && currentForm.getCodEditore().equals("")){
				if (currentForm.getDescrEditore() != null && currentForm.getDescrEditore().equals("")){
					request.setAttribute("editore", "SI");
					fornVO = new FornitoreVO();
					fornVO.setCodFornitore(currentForm.getCodEditore());
					fornVO.setNomeFornitore(currentForm.getDescrEditore());
					request.setAttribute("FornitoreVO", fornVO);
					return Navigation.getInstance(request).goForward(mapping.findForward("cartiglioEditore"));
				}else{
					//cerco il fornitore per nome
					fornVO = this.getFornitore(currentForm.getCodPolo(), currentForm.getCodBib(),
							null, currentForm.getDescrEditore(), currentForm.getTicket());
					if (fornVO != null){
						currentForm.setCodEditore(fornVO.getCodFornitore().toString());
						currentForm.setDescrEditore(fornVO.getNomeFornitore());
						return mapping.getInputForward();
					}else{
						//sif
					}
				}
			}else{
				//cerco il fornitore per cod
				fornVO = this.getFornitore(currentForm.getCodPolo(), currentForm.getCodBib(),
						currentForm.getCodEditore(), null, currentForm.getTicket());
				if (fornVO != null){
					currentForm.setCodEditore(fornVO.getCodFornitore().toString());
					currentForm.setDescrEditore(fornVO.getNomeFornitore());
					return mapping.getInputForward();
				}else{
					//sif
				}
			}
		} catch (DataException ve) {
			if (ve.getMessage() != null && ve.getMessage().equals("editoreInesistente")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultati"));
				this.saveErrors(request, errors);
			}else if (ve.getMessage() != null && ve.getMessage().equals("mancanoEstremiPerRicercaEditore")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.mancanoEstremiPerRicercaFornitore"));
				this.saveErrors(request, errors);
			}else{
				request.setAttribute("editore", "SI");
				fornVO = new FornitoreVO();
				fornVO.setCodFornitore(currentForm.getCodEditore());
				fornVO.setNomeFornitore(currentForm.getDescrEditore());
				request.setAttribute("FornitoreVO", fornVO);
				return Navigation.getInstance(request).goForward(mapping.findForward("cartiglioEditore"));
			}
		}catch (Exception e) { // altri tipi di errore
			return mapping.getInputForward();
		}
		return null;

	}


//	public ActionForward fornitoreCercaVeloce(ActionMapping mapping, HttpServletRequest request, ActionForm form) {
//		StampaTitoliEditoreForm currentForm = ((StampaTitoliEditoreForm) form);
//		FornitoreVO fornVO = null;
//		try{
//			if (currentForm.getCodEditore() != null && currentForm.getCodEditore().equals("")){
//				if (currentForm.getDescrEditore() != null && currentForm.getDescrEditore().equals("")){
//					request.setAttribute("editore", "SI");
//					request.setAttribute("FornitoreVO", fornVO);
//					return Navigation.getInstance(request).goForward(mapping.findForward("cartiglioEditore"));
//				}else{
//					//cerco il fornitore per nome
//					fornVO = this.getFornitore(currentForm.getCodPolo(), currentForm.getCodBib(),
//							null, currentForm.getCodEditore(), currentForm.getTicket());
//					if (fornVO != null){
//						currentForm.setCodEditore(fornVO.getCodFornitore().toString());
//						currentForm.setDescrEditore(fornVO.getNomeFornitore());
//						return mapping.getInputForward();
//					}else{
//						//sif
//					}
//				}
//			}else{
//				//cerco il fornitore per cod
//				fornVO = this.getFornitore(currentForm.getCodPolo(), currentForm.getCodBib(),
//						currentForm.getCodEditore(), null, currentForm.getTicket());
//				if (fornVO != null){
//					currentForm.setCodEditore(fornVO.getCodFornitore().toString());
//					currentForm.setDescrEditore(fornVO.getNomeFornitore());
//					return mapping.getInputForward();
//				}else{
//					//sif
//				}
//			}
//		} catch (DataException ve) {
//			if (ve.getMessage() != null && ve.getMessage().equals("editoreInesistente")){
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultati"));
//				this.saveErrors(request, errors);
//			}else if (ve.getMessage() != null && ve.getMessage().equals("mancanoEstremiPerRicercaEditore")){
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.acquisizioni.mancanoEstremiPerRicercaFornitore"));
//				this.saveErrors(request, errors);
//			}else{
//				request.setAttribute("editore", "SI");
//				request.setAttribute("FornitoreVO", fornVO);
//				return Navigation.getInstance(request).goForward(mapping.findForward("cartiglioEditore"));
//			}
//		}catch (Exception e) { // altri tipi di errore
//			return mapping.getInputForward();
//		}
//		return null;
//	}

}
