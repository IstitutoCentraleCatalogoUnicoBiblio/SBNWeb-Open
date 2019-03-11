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
package it.iccu.sbn.web.actions.documentofisico.parametriBiblio;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.parametriBiblio.ParametriBiblioForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

public class ParametriBiblioAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(ParametriBiblioAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.selParStEtich", "selParStEtich");
		map.put("documentofisico.selParStSchede", "selParStSchede");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.ok", "ok");
		map.put("documentofisico.lsBib", "listaSupportoBiblio");
		map.put("documentofisico.lsModelli", "listaSupportoModelli");
		map.put("documentofisico.lsCodUni", "listaSupportoScarUni");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ParametriBiblioForm myForm =(ParametriBiblioForm)form;

		//setto il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		// controllo se ho già i dati in sessione;
		if(!myForm.isSessione()){
			log.debug("ParametriBiblioAction::unspecified");
			UserVO utente = navi.getUtente();
			myForm.setTicket(utente.getTicket());
			myForm.setCodPolo(utente.getCodPolo());
			myForm.setCodBib(utente.getCodBib());
			myForm.setDescrBib(utente.getBiblioteca());
			myForm.setSessione(true);
		}
		try{
			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String) request.getAttribute("descrBib"));
			}
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaSupportoModelli")) {
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String) request.getAttribute("descrBib"));
				myForm.setCodModello((String) request.getAttribute("codModello"));
			}
			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			myForm.setListaCodScarico(caricaCombo.loadCodiceDesc(this.getValuesComboEtichetteDaProdurre()));
			//almaviva5_20100208 #3543
			myForm.setCodScaricoSelez("ALL");
			//
			if (myForm.getCodModello() == null){
				ModelloDefaultVO modello = this.getModelloDefault(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket());
				if (modello != null){
					this.loadParStSchede(form);
					myForm.setCodModello(modello.getCodModello());
					if (modello.getFormatoStampa().equals("HTM")){
						myForm.setTipoFormato(TipoStampa.HTML.name());
					}else{
					myForm.setTipoFormato(modello.getFormatoStampa());
					}
					//
					if (modello.getFl_non_inv() != null && modello.getFl_non_inv().equals("S")){
						myForm.setAutori(true);
					}
//					sceltaAutori;
					if (myForm.getSceltaAutori() != null){
						if (modello.getFl_tipo_leg().equals("T")){
							myForm.setSceltaAutori("T");
						}else if (modello.getFl_tipo_leg().equals("P")){
							myForm.setSceltaAutori("P");
						}else if (modello.getFl_tipo_leg().equals("S")){
							myForm.setSceltaAutori("S");
						}
					}
//					autori;
					if (modello.getSch_autori() != null && modello.getSch_autori().equals("S")){
						myForm.setAutori(true);
					}
//					topografico;
					if (modello.getSch_topog() != null && modello.getSch_topog().equals("S")){
						myForm.setTopografico(true);
					}
//					soggetti;
					if (modello.getSch_soggetti() != null && modello.getSch_soggetti().equals("S")){
						myForm.setSoggetti(true);
					}
//					titoli;
					if (modello.getSch_titoli() != null && modello.getSch_titoli().equals("S")){
						myForm.setTitoli(true);
					}
//					editori;
					if (modello.getSch_edit() != null && modello.getSch_edit().equals("S")){
						myForm.setEditori(true);
					}
//					classificazioni;
					if (modello.getSch_classi() != null && modello.getSch_classi().equals("S")){
						myForm.setClassificazioni(true);
					}
//					possessori;
					if (modello.getSch_poss() != null && modello.getSch_poss().equals("S")){
						myForm.setPossessori(true);
					}
					//
//					autori2
					if (modello.getFl_coll_aut() != null && modello.getFl_coll_aut().equals("S")){
						myForm.setPrincipale(true);
					}
//					topografico2;
					if (modello.getFl_coll_top() != null && modello.getFl_coll_top().equals("S")){
						myForm.setTopografico2(true);
					}
//					soggetti2;
					if (modello.getFl_coll_sog() != null && modello.getFl_coll_sog().equals("S")){
						myForm.setSoggetti2(true);
					}
//					titoli2;
					if (modello.getFl_coll_tit() != null && modello.getFl_coll_tit().equals("S")){
						myForm.setTitoli2(true);
					}
//					editori2;
					if (modello.getFl_coll_edi() != null && modello.getFl_coll_edi().equals("S")){
						myForm.setEditori2(true);
					}
//					classificazioni2;
					if (modello.getFl_coll_cla() != null && modello.getFl_coll_cla().equals("S")){
						myForm.setClassificazioni2(true);
					}
//					possessori2;
					if (modello.getFl_coll_poss() != null && modello.getFl_coll_poss().equals("S")){
						myForm.setPossessori2(true);
					}
//					richiami;
					if (modello.getFlg_coll_richiamo() != null && modello.getFlg_coll_richiamo().equals("S")){
						myForm.setRichiami(true);
					}
					//
//					copieAutore2;
					if (modello.getN_copie_aut() != null){
						myForm.setCopiePrincipale(Integer.valueOf(modello.getN_copie_aut()));
					}
//					copieTopografico2;
					if (modello.getN_copie_top() != null){
						myForm.setCopieTopografico2(Integer.valueOf(modello.getN_copie_top()));
					}
//					copieSoggetti2;
					if (modello.getN_copie_sog() != null){
						myForm.setCopieSoggetti2(Integer.valueOf(modello.getN_copie_sog()));
					}
//					copieTitoli2;
					if (modello.getN_copie_tit() != null){
						myForm.setCopieTitoli2(Integer.valueOf(modello.getN_copie_tit()));
					}
//					copieEditori2;
					if (modello.getN_copie_edi() != null){
						myForm.setCopieEditori2(Integer.valueOf(modello.getN_copie_edi()));
					}
//					copieClassificazioni2;
					if (modello.getN_copie_cla() != null){
						myForm.setCopieClassificazioni2(Integer.valueOf(modello.getN_copie_cla()));
					}
//					copiePossessori2;
					if (modello.getN_copie_poss() != null){
						myForm.setCopiePossessori2(Integer.valueOf(modello.getN_copie_poss()));
					}
//					copieRichiami;
					if (modello.getN_copie_richiamo() != null){
						myForm.setCopieRichiami(Integer.valueOf(modello.getN_copie_richiamo()));
					}
//					codScarico
					if (modello.getCodScarico() != null){
//						myForm.setCodScaricoSelez(modello.getCodScarico());
						myForm.setCodScaricoSelez("ALL");
					}
					//utilizzo serie
					if (modello.getN_serie() != null){
						if (modello.getN_serie().equals("1")){
							myForm.setUtilizzoSerie(true);
						}else{
							myForm.setUtilizzoSerie(false);
						}
					}
				}
			} else {
				myForm.setTipoFormato(TipoStampa.PDF.name());
//				myForm.setUtilizzoSerie(false);
			}
			myForm.setFolder("ParStEtich");
//			myForm.setCodScaricoSelez(myForm.getCodScaricoSelez().trim());
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException de) { // altri tipi di errore
			if (de.getMessage().equals("recModelloDefaultInesistente")){
				myForm.setCodModello("");
				myForm.setTipoFormato(TipoStampa.PDF.name());
				myForm.setUtilizzoSerie(false);
				this.loadParStSchede(myForm);
				this.loadParStEtich(myForm);
				myForm.setFolder("ParStEtich");
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ de.getMessage()));

			}
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward listaSupportoBiblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
			CodiciAttivita.getIstance().GDF_PARAMETRI_BIBLIOTECA, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoScarUni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoModelli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			request.setAttribute("codPolo", myForm.getCodPolo());
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("prov", "listaSupportoModelli");
			return mapping.findForward("lenteModelli");
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.NoPrenotEffet"));

			return mapping.findForward("chiudi");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
		try {
			if (myForm.getCodModello() != null && myForm.getCodModello().trim().length() == 0){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.nomeModelloObbligatorio"));

				return mapping.getInputForward();
			}else if (myForm.getCodScaricoSelez() != null && myForm.getCodScaricoSelez().trim().length() == 0){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.indicazioneScaricoObbligatoria"));

				return mapping.getInputForward();
			}
			//richiamo la lista delle serie della biblioteca
			String nSerie = "0";
			if (myForm.isUtilizzoSerie()){
				nSerie = "1";
			}else{
				List listaSerie = this.getListaSerie(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket(), myForm);
				if ((listaSerie != null && listaSerie.size() > 0)){
					SerieVO serie = (SerieVO)listaSerie.get(0);
					if(listaSerie.size() == 1 && serie.getCodSerie().trim().equals("")){
						nSerie = "0";
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.perLaBibliotecaSonoGiaPresentiSerieDiverseDallaSerieASpazio"));

						return mapping.getInputForward();
					}
				}else{
					nSerie = "0";
				}
			}
			ModelloDefaultVO modello = new ModelloDefaultVO();
			modello.setCodPolo(myForm.getCodPolo());
			modello.setCodBib(myForm.getCodBib());
			modello.setCodModello(myForm.getCodModello());
			modello.setCodScarico(myForm.getCodScaricoSelez());
			modello.setN_serie(nSerie);
			//				stampaTit;
			if (myForm.isStampaTit()){
				modello.setFl_non_inv("S");
			}else{
				modello.setFl_non_inv("N");
			}
			//				sceltaAutori;
			if (myForm.getSceltaAutori() != null){
				if (myForm.getSceltaAutori().equals("T") || myForm.getSceltaAutori().equals("")){
					modello.setFl_tipo_leg("T");
				}else if (myForm.getSceltaAutori().equals("P")){
					modello.setFl_tipo_leg("P");
				}else if (myForm.getSceltaAutori().equals("S")){
					modello.setFl_tipo_leg("S");
				}
			}
			//					autori;
			if (myForm.isAutori()){
				modello.setSch_autori("S");
			}else{
				modello.setSch_autori("N");
			}
			//					topografico;
			if (myForm.isTopografico()){
				modello.setSch_topog("S");
			}else{
				modello.setSch_topog("N");
			}
			//					soggetti;
			if (myForm.isSoggetti()){
				modello.setSch_soggetti("S");
			}else{
				modello.setSch_soggetti("N");
			}
			//					titoli;
			if (myForm.isTitoli()){
				modello.setSch_titoli("S");
			}else{
				modello.setSch_titoli("N");
			}
			//					editori;
			if (myForm.isEditori()){
				modello.setSch_edit("S");
			}else{
				modello.setSch_edit("N");
			}
			//					classificazioni;
			if (myForm.isClassificazioni()){
				modello.setSch_classi("S");
			}else{
				modello.setSch_classi("N");
			}
			//					possessori;
			if (myForm.isPossessori()){
				modello.setSch_poss("S");
			}else{
				modello.setSch_poss("N");
			}
			//
			//					autori2
			if (myForm.isPrincipale()){
				modello.setFl_coll_aut("S");
			}else{
				modello.setFl_coll_aut("N");
			}
			//					topografico2;
			if (myForm.isTopografico2()){
				modello.setFl_coll_top("S");
			}else{
				modello.setFl_coll_top("N");
			}
			//					soggetti2;
			if (myForm.isSoggetti2()){
				modello.setFl_coll_sog("S");
			}else{
				modello.setFl_coll_sog("N");
			}
			//					titoli2;
			if (myForm.isTitoli2()){
				modello.setFl_coll_tit("S");
			}else{
				modello.setFl_coll_tit("N");
			}
			//					editori2;
			if (myForm.isEditori2()){
				modello.setFl_coll_edi("S");
			}else{
				modello.setFl_coll_edi("N");
			}
			//					classificazioni2;
			if (myForm.isClassificazioni2()){
				modello.setFl_coll_cla("S");
			}else{
				modello.setFl_coll_cla("N");
			}
			//					possessori2;
			if (myForm.isPossessori2()){
				modello.setFl_coll_poss("S");
			}else{
				modello.setFl_coll_poss("N");
			}
			//					richiami;
			if (myForm.isRichiami()){
				modello.setFlg_coll_richiamo("S");
			}else{
				modello.setFlg_coll_richiamo("N");
			}
			//
			//					copieAutore2;
			if (myForm.isAutori()){
				if (myForm.getCopiePrincipale() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerAutore"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_aut(String.valueOf(myForm.getCopiePrincipale()));
				}
			}else{
				modello.setN_copie_aut(String.valueOf("0"));
			}
			//					copieTopografico2;
			if (myForm.isTopografico()){
				if (myForm.getCopieTopografico2() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerTopografico"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_top(String.valueOf(myForm.getCopieTopografico2()));
				}
			}else{
				modello.setN_copie_top(String.valueOf("0"));
			}
			//					copieSoggetti2;
			if (myForm.isSoggetti()){
				if (myForm.getCopieSoggetti2() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerSoggetti"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_sog(String.valueOf(myForm.getCopieSoggetti2()));
				}
			}else{
				modello.setN_copie_sog(String.valueOf("0"));
			}
			//					copieTitoli2;
			if (myForm.isTitoli()){
				if (myForm.getCopieTitoli2() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerTitoli"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_tit(String.valueOf(myForm.getCopieTitoli2()));
				}
			}else{
				modello.setN_copie_tit(String.valueOf("0"));
			}
			//					copieEditori2;
			if (myForm.isEditori()){
				if (myForm.getCopieEditori2() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerEditori"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_edi(String.valueOf(myForm.getCopieEditori2()));
				}
			}else{
				modello.setN_copie_edi(String.valueOf("0"));
			}
			//					copieClassificazioni2;
			if (myForm.isClassificazioni()){
				if (myForm.getCopieClassificazioni2() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerClassificazioni"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_cla(String.valueOf(myForm.getCopieClassificazioni2()));
				}
			}else{
				modello.setN_copie_cla(String.valueOf("0"));
			}
			//					copiePossessori2;
			if (myForm.isPossessori()){
				if (myForm.getCopiePossessori2() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerPossessori"));

					return mapping.getInputForward();
				}else{
					modello.setN_copie_poss(String.valueOf(myForm.getCopiePossessori2()));
				}
			}else{
				modello.setN_copie_poss(String.valueOf("0"));
			}
			//					copieRichiami;
			if (myForm.isRichiami()){
				if (myForm.getCopieRichiami() <= 0){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.valorizzareNumeroCopiePerRichiami"));

					return mapping.getInputForward();
				}else {
					modello.setN_copie_richiamo(String.valueOf(myForm.getCopieRichiami()));
				}
			}else{
				modello.setN_copie_richiamo(String.valueOf("0"));
			}

			modello.setCodScarico(myForm.getCodScaricoSelez() );
			if (myForm.getTipoFormato().equals("HTML")){
				modello.setFormatoStampa("HTM");
			}else{
			modello.setFormatoStampa(myForm.getTipoFormato());
			}
			modello.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
			if (this.insertModelloDefault(modello, myForm.getTicket())) {

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.insertOk"));

				myForm.setFolder("ParStEtich");

				//almaviva5_20140701 #3198
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.reloadDefault();

				return mapping.getInputForward();
			}
			//			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		} catch (DataException de) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ de.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}


	public ActionForward selParStEtich(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
		myForm.setFolder("ParStEtich");
		this.loadParStEtich(form);
		return mapping.getInputForward();
	}

	public ActionForward selParStSchede(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
//		this.loadParStSchede(form);
		myForm.setFolder("ParStSchede");
		return mapping.getInputForward();
	}

	private void loadParStEtich(ActionForm form) throws Exception {

//				myForm.setCodScaricoSelez(null);
//				myForm.setFormEtich(null);
//				myForm.setUtilizzoSerie(false);
	}

	private void loadParStSchede(ActionForm form) throws Exception {
//		Stampa Schede
		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
		myForm.setStampaTit(false);
		myForm.setAutori(false);
		myForm.setListaSceltaAutori(this.loadSceltaAutori(form));
		myForm.setSceltaAutori("");
		myForm.setEditori(false);
		myForm.setTopografico(false);
		myForm.setPossessori(false);
		myForm.setSoggetti(false);
		myForm.setClassificazioni(false);
		myForm.setTitoli(false);
		myForm.setPrincipale(false);
		myForm.setCopiePrincipale(0);
		myForm.setCopieEditori2(0);
		myForm.setCopieTopografico2(0);
		myForm.setCopiePossessori2(0);
		myForm.setCopieSoggetti2(0);
		myForm.setCopieClassificazioni2(0);
		myForm.setCopieTitoli2(0);
		myForm.setCopieRichiami(0);

	}
	private List loadSceltaAutori(ActionForm form) throws Exception {
		ParametriBiblioForm myForm = (ParametriBiblioForm) form;
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("T", "Tutti gli autori");
		lista.add(rec);
		rec = new CodiceVO("P", "Solo principale");
		lista.add(rec);
		rec = new CodiceVO("S", "Solo secondari");
		lista.add(rec);
		myForm.setListaSceltaAutori(lista);

		return lista;

	}
	private List getValuesComboEtichetteDaProdurre(){
		CodiceVO comboDescVO = null;
		List testCombo = new ArrayList();
		comboDescVO = new CodiceVO();
		comboDescVO.setCodice("ALL");
		comboDescVO.setDescrizione("Tutte le etichette");
		testCombo.add(comboDescVO);
		comboDescVO = new CodiceVO();
		comboDescVO.setCodice("CAT");
		comboDescVO.setDescrizione("Escluse etichette gestionali");
		testCombo.add(comboDescVO);
		comboDescVO = new CodiceVO();
		comboDescVO.setCodice("GES");
		comboDescVO.setDescrizione("Solo etichette gestionali");
		testCombo.add(comboDescVO);
		comboDescVO = new CodiceVO();
		comboDescVO.setCodice("BID");
		comboDescVO.setDescrizione("Solo lista ID documento");
		testCombo.add(comboDescVO);
//		comboDescVO = null;
		return testCombo;
	}
	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}
	private boolean insertModelloDefault(ModelloDefaultVO modello, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertModelloDefault(modello, ticket);
		return ret;
	}
	private List getListaSerie(String codPolo, String codBib, String ticket, ActionForm form) throws Exception {
		ParametriBiblioForm myForm = (ParametriBiblioForm)form;
		List serie;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		return serie;
	}
}
