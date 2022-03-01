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
package it.iccu.sbn.web.actions.gestionestampe.etichette;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaEtichetteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.etichette.StampaEtichetteForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.util.ArrayList;
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

public class StampaEtichetteAction  extends RicercaInventariCollocazioniAction {


	private static Log log = LogFactory.getLog(StampaEtichetteAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("documentofisico.lsModelli", "listaSupportoModelli");
		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.selPerInventari", "selInv");
		map.put("documentofisico.bottone.SIFbibliotecario", "SIFbibliotecario");
		return map;
	}




	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		ActionMessages errors = new ActionMessages();
		StampaEtichetteForm currentForm = (StampaEtichetteForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			try {
				int numCopie = Integer.valueOf(utenteEjb.getDefault(ConstantDefault.GDF_NRO_COPIE_ETICH).toString());
				if (!ValidazioneDati.strIsEmpty(String.valueOf(numCopie))){
					currentForm.setNumCopie(numCopie);
				}else{
					currentForm.setNumCopie(2);
				}

				//almaviva5_20140701 #3198
				String modello = (String) utenteEjb.getDefault(ConstantDefault.GDF_MODELLO_ETICH);
				if (ValidazioneDati.isFilled(modello))
					currentForm.setCodModello(modello);

			} catch (ValidationException ve) {
				errors.add("generico", new ActionMessage("error.documentofisico." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} catch (DataException de) { // altri tipi di errore
				errors.add("generico", new ActionMessage("error.documentofisico." + de.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}
			return null;
		}else{
			return null;
		}
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		this.saveToken(request);


		StampaEtichetteForm currentForm = (StampaEtichetteForm)form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			request.setAttribute("currentForm", currentForm);
			return mapping.getInputForward();
		}
		super.unspecified(mapping, currentForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		try{
				//questi valori vanno messi nel form

				currentForm.setTicket(navi.getUserTicket());
				UserVO utente = navi.getUtente();
				currentForm.setCodPolo(utente.getCodPolo());
				currentForm.setCodBib(utente.getCodBib());
				currentForm.setDescrBib(utente.getBiblioteca());
				currentForm.setCodBibliotec(utente.getUserId());
				currentForm.setNomeBibliotec(utente.getNome_cognome());
				currentForm.setDataInizio(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
				currentForm.setDataFine(DateUtil.formattaDataOra(DaoManager.now()).toString().substring(0, 11));
				ActionForward loadDefault = loadDefault(request, mapping, form);
				if (loadDefault != null) {
					return loadDefault;
				}

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}else{

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

				if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaSupportoModelli")) {
					currentForm.setCodBib((String) request.getAttribute("codBib"));
					currentForm.setDescrBib((String) request.getAttribute("descrBib"));
					currentForm.setCodModello((String) request.getAttribute("codModello"));


				}

				if (request.getAttribute("bibliotecario") != null && request.getAttribute("bibliotecario").equals("bibliotecario")) {
					UtenteVO bibliotecario = (UtenteVO)request.getAttribute("bibliotecario");
					currentForm.setCodBibliotec(bibliotecario.getUsername());
					currentForm.setNomeBibliotec(bibliotecario.getNome()+" " + bibliotecario.getCognome());
					currentForm.setNumCopie(2);

				}

				request.getSession().setAttribute("conferma","ko");
				// controllo che non sia stata attivata la lista supporto dei bibliotecari

				UtenteVO bibliote=(UtenteVO) request.getAttribute("bibliotecario");

				if (bibliote!=null && bibliote.getUsername()!=null && bibliote.getUsername().length()>0) {
					currentForm.setCodBibliotec(bibliote.getUsername());
					currentForm.setNomeBibliotec(bibliote.getNome()+" " + bibliote.getCognome() );
				}

				//almaviva5_20140701 #3198
				ModelloDefaultVO modello = this.getModelloDefault(utente.getCodPolo(), currentForm.getCodBib(), utente.getTicket());
				if (modello == null){
					currentForm.setCodModello("");
					currentForm.setTipoFormato(TipoStampa.PDF.name());
					currentForm.setDescrBibEtichetta("");
				}else{
					if (!ValidazioneDati.isFilled(currentForm.getCodModello()) ) {
						currentForm.setCodModello(modello.getCodModello());
						if (modello.getDescrBibModello() != null && !modello.getDescrBibModello().trim().equals("")){
							currentForm.setDescrBibEtichetta(modello.getDescrBibModello());
						}else{
							currentForm.setDescrBibEtichetta(currentForm.getDescrBib());
						}
					}
					String formatoStampa = modello.getFormatoStampa();
					if (ValidazioneDati.equals(formatoStampa, "HTM"))
						formatoStampa = "HTML";
					currentForm.setTipoFormato(TipoStampa.valueOf(formatoStampa).name());
				}

				currentForm.setModConfig(true);
				currentForm.setModBarCode(false);
				currentForm.setElencoModelli(getElencoModelli());

			}

		} catch (ValidationException e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (DataException e) { // altri tipi di errore
			if (e.getMessage().equals("recModelloDefaultInesistente")){
				currentForm.setCodModello("");
				currentForm.setTipoFormato(TipoStampa.PDF.name());
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
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


	public ActionForward listaSupportoModelli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {



		StampaEtichetteForm ricEtichette = (StampaEtichetteForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			request.setAttribute("codPolo", ricEtichette.getCodPolo());
			request.setAttribute("codBib", ricEtichette.getCodBib());
			request.setAttribute("descrBib", ricEtichette.getDescrBib());
			request.setAttribute("prov", "listaSupportoModelli");
			return mapping.findForward("lenteModelli");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
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



	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

	StampaEtichetteForm myForm = (StampaEtichetteForm)form;



	try {
		//creo il VO da inserire nel messaggio di richiesta per la stampa batch
		StampaEtichetteVO stampaEtichetteVO = new StampaEtichetteVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

		if (myForm.getFolder().equals("Collocazioni")){
			if (myForm.getCodPoloSez() == null && myForm.getCodBibSez() == null){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.premereTastoSezione"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			super.validaInputCollocazioni(mapping, request, myForm);
			myForm.setTipoOperazione("S");
			stampaEtichetteVO.setCodPoloSez(myForm.getCodPoloSez());
			stampaEtichetteVO.setCodBibSez(myForm.getCodBibSez());
			stampaEtichetteVO.setSezione(myForm.getSezione());
			stampaEtichetteVO.setDallaCollocazione(myForm.getDallaCollocazione());
			stampaEtichetteVO.setDallaSpecificazione(myForm.getDallaSpecificazione());
			stampaEtichetteVO.setAllaCollocazione(myForm.getAllaCollocazione());
			stampaEtichetteVO.setAllaSpecificazione(myForm.getAllaSpecificazione());
		}

		//Ricerca per range di inventari
		if (myForm.getFolder().equals("RangeInv")){
			super.validaInputRangeInventari(mapping, request, myForm);
			myForm.setTipoOperazione("R");
			//validazione startInv e endInv
			stampaEtichetteVO.setSerie(myForm.getSerie());
			stampaEtichetteVO.setEndInventario(myForm.getEndInventario());
			stampaEtichetteVO.setStartInventario(myForm.getStartInventario());
		}

		//Ricerca per inventari
		if (myForm.getFolder().equals("Inventari")){
			super.validaInputInventari(mapping, request, myForm);

			if (myForm.getTipoOperazione() != null && myForm.getTipoOperazione().equals("A")){
				List<CodiceVO> listaInv = this.getListaInventariCollocati(Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(),
						myForm.getCodBibliotec(), myForm.getDataInizio(), myForm.getDataFine(), myForm.getTicket());
				if (listaInv != null && listaInv.size() > 0){
					int numCopie = myForm.getNumCopie();
					if (numCopie > 0){
					myForm.setListaInventari(listaInv);
					}else{
						throw new ValidationException("controllareNumeroCopie");
					}
				}else{
					throw new DataException("inventariNotFound");
				}

			}
			//
			if (!myForm.isModBarCode() && !myForm.isModConfig()){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.nessunModelloSelezionato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			//
			myForm.setTipoOperazione("N");
			stampaEtichetteVO.setListaInventari(myForm.getListaInventari());
			stampaEtichetteVO.setSelezione(myForm.getSelezione());
			if (myForm.getSelezione() != null && (myForm.getSelezione().equals("F"))){
				stampaEtichetteVO.setNomeFileAppoggioInv(myForm.getNomeFileAppoggioInv());
			}
		}


		//codBib
		String codBib=myForm.getCodBib();
		stampaEtichetteVO.setCodBib(codBib);

		//codPolo
		String codPolo=myForm.getCodPolo();
		stampaEtichetteVO.setCodPolo(codPolo);

		//user
		UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
		String utente= user.getUserId();
		stampaEtichetteVO.setUser(utente);

		if (myForm.isModConfig()){
			//codModello
			String codModello = myForm.getCodModello();
			stampaEtichetteVO.setCodModello(codModello);
		}
		if (myForm.isModBarCode()){
			//codModello
			String barCode = "BARCODE";
			stampaEtichetteVO.setBarCode(barCode);
		}

		//tipoFormato
		String tipoFormato=myForm.getTipoFormato();
		stampaEtichetteVO.setTipoFormato(tipoFormato);
		request.setAttribute("TipoFormato", tipoFormato);

		//

		stampaEtichetteVO.setTipoOperazione(myForm.getTipoOperazione());
		stampaEtichetteVO.setCodAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE);

		ModelloEtichetteVO modello = this.getModello(codPolo, codBib, myForm.getCodModello(),
				myForm.getTicket());
		if (modello != null){
			stampaEtichetteVO.setDescrBibEtichetta(modello.getDescrBib());
		}
		//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.
		List parametri=new ArrayList();
		parametri.add(stampaEtichetteVO);
		request.setAttribute("DatiVo", parametri);


		//il questo particolare caso il file jrxml NON ESISTE su filesystem, ma verrà
		//generato "on the fly" sulla base del nome del modello importato
		//a questo livello passiamo quindi IL NOME DEL MODELLO (che andrà letto dal db)
		//e non il nome del file che lo contiene
		String pathDownload = StampeUtil.getBatchFilesPath();

		//codice standard inserimento messaggio di richiesta stampa differita
		StampaDiffVO stam = new StampaDiffVO();
		stam.setTipoStampa(tipoFormato);
		stam.setUser(utente);
		stam.setCodPolo(codPolo);
		stam.setCodBib(codBib);
		stam.setTipoOrdinamento("");
		stam.setNumCopie(myForm.getNumCopie());
		stam.setParametri(parametri);

//		stam.setTemplate(codModello);
		if (myForm.isModConfig()){
			stam.setTemplate(stampaEtichetteVO.getCodModello());
		}
		if (myForm.isModBarCode()){
			myForm.setElencoModelli(getElencoModelli());
			if (myForm.getElencoModelli() != null && myForm.getElencoModelli().size() > 0){
				for (int index = 0; index < myForm.getElencoModelli().size(); index++) {
					ModelloStampaVO rec = (ModelloStampaVO) getElencoModelli().get(index);
					myForm.setModello(rec);
				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			//
			String fileJrxml = myForm.getModello().getJrxml()+".jrxml";
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);

			String pathJrxml = null;
			pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+ fileJrxml;
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			stam.setTemplateBarCode(pathJrxml);
		}
		stam.setDownload(pathDownload);
		stam.setCodAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE);
		stam.setDownloadLinkPath("/");
		stam.setTipoOperazione("STAMPA_ETICHETTE");
		stam.setTicket(myForm.getTicket());
		UtilityCastor util= new UtilityCastor();
		String dataCorr = util.getCurrentDate();
		stam.setData(dataCorr);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		String idMessaggio = factory.getStampeOnline().stampaEtichette(stam);

		ActionMessages errors = new ActionMessages();
		idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
		errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));

		this.saveErrors(request, errors);
		if (idMessaggio == null) {
			errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotStampaNonEffettuata"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		//myForm.setDisable(true);
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

	public ActionForward cambioBiblioteca(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		StampaEtichetteForm myForm = (StampaEtichetteForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}

	public ActionForward SIFbibliotecario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			request.setAttribute("stampaEtichette", "stampaEtichette");
			return mapping.findForward("SIFbibliotecario");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}
	private List<CodiceVO> getListaInventariCollocati(String codPolo, String codBib, String codUtente, String dataDa, String dataA, String ticket) throws Exception {
		List lista = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		lista = factory.getGestioneDocumentoFisico().getListaInventariCollocatiDa(codPolo, codBib, codUtente, dataDa, dataA, ticket);
		return lista;
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE);
			return listaModelli;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		request.setAttribute("currentForm", form);
		StampaEtichetteForm myForm = (StampaEtichetteForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private ModelloEtichetteVO getModello(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		ModelloEtichetteVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getModello(codPolo, codBib, codSez, ticket);
		return rec;
	}


}
