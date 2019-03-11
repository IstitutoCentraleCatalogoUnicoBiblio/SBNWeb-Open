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
package it.iccu.sbn.web.actions.gestionestampe.topografico;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroTopograficoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.topografico.StampaRegistroTopograficoForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StampaRegistroTopograficoAction extends RicercaInventariCollocazioniAction {

	private static Logger log = Logger.getLogger(StampaRegistroTopograficoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		try{


			StampaRegistroTopograficoForm myForm = ((StampaRegistroTopograficoForm) form);
			super.unspecified(mapping, myForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			if (Navigation.getInstance(request).isFromBar() ){
				return mapping.getInputForward();
			}
			// controllo se ho già i dati in sessione;
			if(!myForm.isSessione())	{
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				this.loadPagina(form);
				myForm.setSessione(true);
			}
			myForm.setElencoModelli(getElencoModelli());
			myForm.setTipoFormato(TipoStampa.PDF.name());

			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
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

		StampaRegistroTopograficoForm currentForm = ((StampaRegistroTopograficoForm) form);
		try {
			currentForm.setTipoOperazione("S");
			super.validaInputCollocazioni(mapping, request, currentForm);
			StampaRegistroTopograficoVO regTopVO = new StampaRegistroTopograficoVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			regTopVO.setCodPolo(currentForm.getCodPolo());
			regTopVO.setCodBib(currentForm.getCodBib());
			regTopVO.setSezione(currentForm.getSezione());
			regTopVO.setDallaCollocazione(currentForm.getDallaCollocazione());
			regTopVO.setDallaSpecificazione(currentForm.getDallaSpecificazione());
			regTopVO.setAllaCollocazione(currentForm.getAllaCollocazione());
			regTopVO.setAllaSpecificazione(currentForm.getAllaSpecificazione());

			regTopVO.setTipoOperazione(currentForm.getTipoOperazione());
			regTopVO.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_TOPOGRAFICO);
			regTopVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
			// ho finito di preparare il VO, ora lo metto nell'arraylist che
			// passerò alla coda.
			List parametri = new ArrayList();
			parametri.add(regTopVO);
			request.setAttribute("DatiVo", parametri);

			String fileJrxml = currentForm.getTipoModello();
			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml = basePath + File.separator + "jrxml" + File.separator + File.separator + fileJrxml;
			// NB: Se voglio memorizzare sul server
			// String pathDownload = basePath+File.separator+"download";
			String pathDownload = StampeUtil.getBatchFilesPath();

			// Se voglio memorizzare in locale

			// codice standard inserimento messaggio di richiesta stampa
			// differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setTipoStampa(currentForm.getTipoFormato());
			stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
			stam.setCodPolo(currentForm.getCodPolo());
			stam.setCodBib(currentForm.getCodBibSez());

			stam.setTipoOrdinamento("");
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_TOPOGRAFICO);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setTipoOperazione("STAMPA_REGISTRO_TOPOGRAFICO");
			stam.setTicket(currentForm.getTicket());
			UtilityCastor util = new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idMessaggio = factory.getStampeOnline().stampaRegistroTopografico(stam);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: " + idMessaggio;
			errors.add("Avviso", new ActionMessage("errors.finestampa",	idMessaggio));

			this.saveErrors(request, errors);

			//currentForm.setDisable(true);
			return mapping.getInputForward();

		} catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."
					+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."
					+ e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		StampaRegistroTopograficoForm myForm = ((StampaRegistroTopograficoForm) form);
		try {
//			return mapping.findForward("indietro");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadPagina(ActionForm form) throws Exception {
		StampaRegistroTopograficoForm myForm = ((StampaRegistroTopograficoForm) form);
		myForm.setDallaCollocazione("");
		myForm.setAllaSpecificazione("");
		myForm.setDallaSpecificazione("");
		myForm.setAllaCollocazione("");
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		StampaRegistroTopograficoForm myForm = ((StampaRegistroTopograficoForm) form);
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_TOPOGRAFICO, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private List getElencoModelli() {
		try {
			List listaRidotta = null;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_TOPOGRAFICO);
			if (listaModelli != null && listaModelli.size() > 0){
					listaRidotta = new ArrayList<ModelloStampaVO>();
					ModelloStampaVO cod = null;
					for (int i = 0; i < listaModelli.size(); i++ ){
						cod = new ModelloStampaVO();
						ModelloStampaVO rec = listaModelli.get(i);
						if (!rec.getNomeModello().equals("registro topografico_xls") ){
							listaRidotta.add(rec);
						}
					}
			}
			return listaRidotta;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
}
