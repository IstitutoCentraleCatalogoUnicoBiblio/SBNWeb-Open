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
//	SBNWeb - Rifacimento ClientServer
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import static org.hamcrest.Matchers.equalTo;

import it.iccu.sbn.ejb.exception.AuthenticationException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SifRicercaRepertoriVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioIncipitVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloAudiovisivoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloMusicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.DettaglioTitoloForm;
import it.iccu.sbn.web.actions.gestionebibliografica.isbd.finsiel.Isbd;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.CostruttoreIsbd;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

public class DettaglioTitoloAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(DettaglioTitoloAction.class);

	//private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.aggiornaCanali", "aggiornaCanali");
		map.put("button.annulla", "annullaOper");
		map.put("button.ok", "confermaOper");

		map.put("copiaReticolo.button.okLocale", "copiaReticoloLocale");
		map.put("copiaReticolo.button.okIndice", "copiaReticoloCondiviso");

		map.put("button.canArea0", "canArea0DatiComuni");
		map.put("button.insArea0", "insArea0DatiComuni");

		map.put("button.canNumStandard", "canNumStandard");
		map.put("button.insNumStandard", "insNumStandard");

		map.put("button.canImpronta", "canImpronta");
		map.put("button.insImpronta", "insImpronta");

		map.put("button.canIncipit", "canIncipit");
		map.put("button.insIncipit", "insIncipit");
		map.put("button.modIncipit", "modIncipit");

		map.put("button.canPersonaggio", "canPersonaggio");
		map.put("button.insPersonaggio", "insPersonaggio");

		map.put("button.orgSint.hlpElementiOrganico", "hlpElementiOrganicoSintetico");
		map.put("button.orgAnal.hlpElementiOrganico", "hlpElementiOrganicoAnalitico");
		map.put("button.orgSintComp.hlpElementiOrganico", "hlpElementiOrganicoSinteticoComp");
		map.put("button.orgAnalComp.hlpElementiOrganico", "hlpElementiOrganicoAnaliticoComp");

		map.put("button.canRepertorio", "canRepertorio");
		map.put("button.insRepertorio", "insRigaVuotaRep");
		map.put("button.hlpRepertorio", "hlpRepertorio");

		map.put("button.calcolaISBD", "calcolaISBD");
		map.put("button.calcolaArea5", "calcolaArea5");

		map.put("button.canLinkEsterni", "canLinkEsterni");
		map.put("button.insLinkEsterni", "insLinkEsterni");
		map.put("button.ricalcolaURL", "ricalcolaURL");
		map.put("button.canReperCartaceo", "canReperCartaceo");
		map.put("button.insReperCartaceo", "insReperCartaceo");

		return map;
	}

	private void loadDefault(HttpServletRequest request,
			DettaglioTitoloForm dettaglioTitoloForm)
			throws InfrastructureException, NumberFormatException,
			RemoteException	{

		//almaviva5_20100217 #3555
		if (dettaglioTitoloForm.isInitialized())
			return;

		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			log.debug("loadDefault()");
			DettaglioTitoloParteFissaVO detTitoloPFissaVO = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO();
			if (detTitoloPFissaVO.getTipoMat() == null ||
					detTitoloPFissaVO.getTipoMat().equals("")) {
				detTitoloPFissaVO.setTipoMat((String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_TIPO_MATERIALE));
			}

			if (detTitoloPFissaVO.getTipoRec() == null
					|| detTitoloPFissaVO.getTipoRec().equals("")) {
				detTitoloPFissaVO.setTipoRec((String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_TIPO_RECORD));
			}

			// Modifica almaviva2 30.11.2010 BUG MANTIS 3932 impostazione della natura se in fase di interrogazione
			// la natura usata come filtro non sia disponibile per la creazione atomica,
			// si accede ai default e si caricano quelli; (DettaglioTitoloAction metodo loadDefault)
			dettaglioTitoloForm.setNaturaDefault((String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_NATURA1));
			if (detTitoloPFissaVO.getNatura() == null ||
					detTitoloPFissaVO.getNatura().equals("")) {
				detTitoloPFissaVO.setNatura(dettaglioTitoloForm.getNaturaDefault());
			}


			if (detTitoloPFissaVO.getLivAut() == null
					|| detTitoloPFissaVO.getLivAut().equals("")) {
				detTitoloPFissaVO.setLivAut((String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_LIVELLO_AUTORITA));
			}

			if (detTitoloPFissaVO.getLingua() == null
					|| detTitoloPFissaVO.getLingua().equals("")) {
				detTitoloPFissaVO.setLingua1((String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_LINGUA));
			}

			if (detTitoloPFissaVO.getPaese() == null
					|| detTitoloPFissaVO.getPaese().equals("")) {
				detTitoloPFissaVO.setPaese((String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_PAESE));
			}

			// ANCORA NON UTILIZZATO, VERIFICARE LE CASISTICHE:
			//utenteEjb.getDefault(ConstantDefault.CREA_TIT_TIPO_NUMERO_STANDARD);

			//almaviva5_20100217 #3555
			dettaglioTitoloForm.setInitialized(true);

		} catch (DefaultNotFoundException e) {}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


        // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		// Inizio modifica almaviva2 BUG MANTIS 3842 08.07.2010 controllo sulla inizializzazione delle liste per verificare
		// se è la prima volta che si entra nell'oggetto
		dettaglioTitoloForm.setFirstTime(false);
		if (dettaglioTitoloForm.getListaNatura() == null) {
			dettaglioTitoloForm.setFirstTime(true);
		}
		// Fine modifica almaviva2 BUG MANTIS 3842 08.07.2010

		if (Navigation.getInstance(request).isFromBar() ) {
			if (request.getAttribute("dettaglioIncipit") != null) {
				DettaglioIncipitVO detIncipit = ((DettaglioIncipitVO) request.getAttribute("dettaglioIncipit"));
				TabellaNumSTDImpronteVO elemTabIncipit = new TabellaNumSTDImpronteVO(
						detIncipit.getNumMovimento(),
						detIncipit.getNumProgDocumento(),
						"",
						detIncipit.getContestoMusicale());
				if (((Integer) request.getAttribute("progrDettaglioIncipit")) == 999) {
					// Inserimento di un nuovo Incipit sulla tabella
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().addListaDettagliIncipit(detIncipit);
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().addListaIncipit(elemTabIncipit);
				} else {
					// Aggiornamento di un Incipit già presente
					int index = ((Integer) request.getAttribute("progrDettaglioIncipit"));
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaDettagliIncipit().remove(index);
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().addListaDettagliIncipit(detIncipit);
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaIncipit().remove(index);
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().addListaIncipit(elemTabIncipit);
				}
			}
			if (request.getAttribute("codOrg") != null) {
				if (request.getAttribute("tipoOrganico") != null) {
					String organico = (String) request.getAttribute("codOrg");
					if (((String) request.getAttribute("tipoOrganico")).equals("SINTETICO")) {
						if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgSint().equals("")) {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgSint(organico);
						} else {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgSint(
									dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgSint() + "," + organico);
						}
					} else if (((String) request.getAttribute("tipoOrganico")).equals("ANALITICO")){
						if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgAnal().equals("")) {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgAnal(organico);
						} else {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgAnal(
									dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgAnal() + "," + organico);
						}
					} else if (((String) request.getAttribute("tipoOrganico")).equals("SINTETICO-COMPL")) {
						if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgSintComp().equals( "")) {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgSintComp(organico);
						} else {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgSintComp(
									dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgSintComp() + "," + organico);
						}
					} else if (((String) request.getAttribute("tipoOrganico")).equals("ANALITICO-COMPL")){
						if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgAnalComp().equals("")) {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgAnalComp(organico);
						} else {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setOrgAnalComp(
									dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgAnalComp() + "," + organico);
						}
					}
				}
			}
			if (request.getParameter("findREPERT") != null) {
				return mapping.findForward("cercaRepertori");
			}
			if (request.getAttribute("sigl") != null) {
				this.insNuovoRepertorio(request, form);
				return mapping.getInputForward();
			}
			return mapping.getInputForward();
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (request.getParameter("findREPERT") != null) {
			return mapping.findForward("cercaRepertori");
		}
		if (request.getAttribute("sigl") != null) {
			this.insNuovoRepertorio(request, form);
			return mapping.getInputForward();
		}

		if (request.getAttribute("bidPerRientroAnalitica") != null) {
			dettaglioTitoloForm.setBidPerRientroAnalitica((String) request.getAttribute("bidPerRientroAnalitica"));
		}

		if (request.getAttribute("dettaglioTit") != null) {
			dettaglioTitoloForm.setDettTitComVO((DettaglioTitoloCompletoVO) request.getAttribute("dettaglioTit"));
		}

		if (dettaglioTitoloForm.getListaRepertoriModificato() == null) {
			dettaglioTitoloForm.creaListaRepertoriModificato();
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaRepertoriOld() != null) {
				for (int i = 0; i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaRepertoriOld().size(); i++) {
					TabellaNumSTDImpronteVO repertorioMod = new TabellaNumSTDImpronteVO();
					TabellaNumSTDImpronteOriginarioVO repertorioOld = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaRepertoriOld().get(i);

					repertorioMod.setCampoDue(repertorioOld.getCampoDue());
					repertorioMod.setCampoUno(repertorioOld.getCampoUno());
					repertorioMod.setDescCampoDue(repertorioOld.getDescCampoDue());
					repertorioMod.setNota(repertorioOld.getNota());
					dettaglioTitoloForm.addListaRepertoriModificato(repertorioMod);
				}
			}
		}

		if (request.getAttribute("tipoProspettazione") != null) {
			dettaglioTitoloForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));
		}

		if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
			dettaglioTitoloForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));
		}

		if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")) {
			Navigation.getInstance(request).setTesto("Crea");

			// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo serve per gestire le abilitazioni alla sola parte
			// generale e bloccare l'inserimento dell'area delle specificita
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat() != null
					&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")
					&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals(" ")) {
				dettaglioTitoloForm.setFlagAbilitazSpec(true);
				Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try{
					utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat());
				}catch(UtenteNotAuthorizedException ute)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.tipoMaterNotAuthorizedInfo",
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().toString()));
					this.saveErrors(request, errors);
					dettaglioTitoloForm.setFlagAbilitazSpec(false);
				}
			}
			// Fine modifica BUG MANTIS 3551

			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().size() == 0) {

				// Inizio Modifica almaviva2 2011.02.03 BUG MANTIS 4187
				// Se nei default di gestione bibliografica scelgo un valore del tipo numero standard nella maschera di creazione titolo
				// devo trovare una riga del campo numero standard abilitata con il codice tipo numero scelto dei default
				Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().addListaNumStandard(
						new TabellaNumSTDImpronteVO("", (String)utenteEjb.getDefault(ConstantDefault.CREA_TIT_TIPO_NUMERO_STANDARD), "", ""));
			}

			// INIZIO inserimento segnalibro per attivare la clonazione della form nel caso di successiva richiesta di un dettaglio simile
			Navigation.getInstance(request).addBookmark(Bookmark.Bibliografica.Titolo.CREA_TITOLO);
			// FINE inserimento segnalibro

			if (request.getParameter("CONDIVISO") != null) {
				if (request.getParameter("CONDIVISO").equals("TRUE")) {
					dettaglioTitoloForm.setFlagCondiviso(true);
				} else {
					dettaglioTitoloForm.setFlagCondiviso(false);
				}
			}
			if (!dettaglioTitoloForm.isFlagCondiviso()) {
				Navigation.getInstance(request).setTesto("Crea locale");
			}
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setBid("");
			this.loadDefault(request, dettaglioTitoloForm);

			// Intervento interno almaviva2 Luglio 2015: nel caso di prima visualizzazione della mappa di creazione titolo
			// quando il tipo materiale viene impostato di default e non manualmente dal bibliotecario i controlli di ablitazione
			// vanno replicati dopo il caricamento dei default altrimenti il software si comporta
			// come se il bibliotecario non fosse abilitato e manda la mappa di dettaglio e non quella di Inserimento
			if (dettaglioTitoloForm.isFirstTime()) {
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat() != null
						&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")
						&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals(" ")) {
					dettaglioTitoloForm.setFlagAbilitazSpec(true);
					Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
					try{
						utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat());
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.tipoMaterNotAuthorizedInfo",
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().toString()));
						this.saveErrors(request, errors);
						dettaglioTitoloForm.setFlagAbilitazSpec(false);
					}
				}
			}

			// almaviva2 Febbraio 2016 - Intervento interno - la preimpostazione dei dati di AreaComune viene spostata prima
			// delle impostazioni particolari relative alla creazione degli spogli
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null &&
					!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("")) {
				caricaDefaultDatiComuni(dettaglioTitoloForm);
			}

			if (dettaglioTitoloForm.getLegame51() == null) {
				dettaglioTitoloForm.setLegame51("NO");
				if (request.getParameter("GEST51") != null) {
					if (request.getParameter("GEST51").equals("TRUE")) {
						dettaglioTitoloForm.setLegame51("SI");
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("51");
						//	Inizio intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("");
						//	Fine intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
						// Modifica almaviva2 BUG MANTIS COLLAUDO 4384
						// Quando creo un volume inferiore di tipo materiale antico, la maschera di creazione del figlio deve presentarsi
						// con il campo tipo materiale già valorizzato con E senza tener conto dei default di utente
						if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getTipMatBidPartenza().equals("E")) {
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMat("E");
						}

					}
				}
			}
			if (dettaglioTitoloForm.getSpoglio() == null) {
				dettaglioTitoloForm.setSpoglio("NO");
				if (request.getParameter("SPOGLIO") != null) {
					if (request.getParameter("SPOGLIO").equals("TRUE")) {
						dettaglioTitoloForm.setSpoglio("SI");
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("51");
						//	Inizio intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("N");
						//	Fine intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->

						// Inizio Dicembre 2015 almaviva2 : in fase di creazione di uno spoglio sotto una M si porta tutto il dettaglio
						// del titolo madre così da preimpostare tutti i campi della N che salvo alcune rare eccezioni saranno gli stessi
						// Gennaio 2016: la copia dei dati dello Spoglio non deve comprendere il Livello di autorità e l'area Note
						if (request.getAttribute("dettaglioTitMadre") != null) {
							String appoLivAutDefault = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLivAut();
							dettaglioTitoloForm.setDettTitComVO((DettaglioTitoloCompletoVO) request.getAttribute("dettaglioTitMadre"));
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setBid("");
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLivAut(appoLivAutDefault);
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitNote("");

							// BUG 6442: almaviva2 - LIG Correzione affinche quando si crea lo spoglio di un periodico non si deve impostare
							// quella del periodico, che ovviamente non si applica ad uno spoglio di periodico ma solo a quella di Monografia
							if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("S")) {
								// Per spoglio di periodico i campi NON SI DEVONO preimpostare quindi si ricoprono con il
								// valore spazio quelli della S superiore
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoData("");
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setDataPubbl1("");
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setDataPubbl2("");
							} else {
								// Per spoglio di monografia i campi SI DEVONO preimpostare quindi NON si ricoprono con il
								// valore spazio quelli della S superiore
								// Adeguativa/Correttiva Aprile 2017 almaviva2
								// sulla copia dati della Spoglio in Creazione dal doc Madre SI devono preimpostare i campi
								// sia al tipo che alla data di Pubblicazione
								// dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoData("");
								// dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setDataPubbl1("");
								// dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setDataPubbl2("");
							}

							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitEdiz("");
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitDatiMatem("");
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitPubbl("");
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitDescrFis("");

							// almaviva2 Febbraio 2016 - Intervento interno - la copia dei dati dello Spoglio non deve comprendere il tipoSupporto
							// che, nel caso delle n deve rimanere null
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoSupporto(null);

							// Correttiva Gennaio 2016  almaviva2 sulla copia dati della Spoglio in Creazione dal doc Madre
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setListaNumStandard(
									new ArrayList<TabellaNumSTDImpronteVO>());
							if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO() != null) {
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().setDurataRegistraz("");
							}

							// Adeguativa/Correttiva Marzo 2017  almaviva2 sulla copia dati della Spoglio in Creazione dal doc Madre
							// non si devono preimpostare i campi Nota del Contenuto e Impronta (SEGNALAZIONE 6406)
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setListaImpronte(
									new ArrayList<TabellaNumSTDImpronteVO>());
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitNote327("");
						}

						// Fine Dicembre 2015 almaviva2

						// Bug Mantis esercizio 6001: almaviva2 ottobre 2015 - quando si fa una ricerca e poi si va in creazione,
						// nel form non viene riportato il titolo usato per la ricerca (il relativo campo è vuoto)
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitTitolo(
									dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getDescArrivo());

					}
				}
			}

			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
			if (dettaglioTitoloForm.getLegameTitUniRinvio() == null) {
				dettaglioTitoloForm.setLegameTitUniRinvio("NO");
				if (request.getParameter("RINVIO_TIT_UNI") != null) {
					if (request.getParameter("RINVIO_TIT_UNI").equals("TRUE")) {
						dettaglioTitoloForm.setLegameTitUniRinvio("SI");
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("08");
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("V");
						// INIZIO almaviva2 novembre 2019 BUG 7213 Mantis: Cercando di creare una variante del titolo dell'opera la schermata di dettaglio
						// che compare presenta i campi specifici del materiale impostato di default per il bibliotecario.
						// Nel caso di Rinvio Opera i campi tipo materiale e tipo record sono vuoti
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMat(" ");
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoRec(" ");
					}
				}
			}

			// almaviva2 Febbraio 2016 - Intervento interno - la preimpostazione dei dati di AreaComune viene spostata prima
			// delle impostazioni particolari relative alla creazione degli spogli
			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 11) In creazione del record documento l’area 0 dovrebbe valorizzarsi dei default secondo il tipo record
//			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null &&
//					!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("")) {
//				caricaDefaultDatiComuni(dettaglioTitoloForm);
//			}

		} else {
			dettaglioTitoloForm.setLegame51("NO");
		}

		if (dettaglioTitoloForm.getTipoProspettazione().equals("AGG")) {
			Navigation.getInstance(request).setTesto("Varia");



			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 1)	Modificato tipo materiale da M a H, lasciando tipo record a;
			// il sistema prospetta i campi relativi alla sola rappresentazione (N.B.: il libretto NON può essere H!!!);
			// al salvataggio inviato diagnostico: Protocollo di INDICE: 3271 Tipo record non compatibile con tipo materiale
			// il sistema non deve prospettare i campi specifici H per documenti aventi tipo record diverso da ‘i’, ‘j’, ‘g’
			// (ID inibisce la modifica del tipo materiale in H per documenti ad es. con tipo record ‘a’,
			// soluzione che ci sembra buona: occorre modificare il tipo record prima di effettuare la modifica del materiale in H).

			// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo serve per gestire le abilitazioni alla sola parte
			// generale e bloccare la variazione dell'area delle specificita
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat() != null
					&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")
					&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals(" ")) {
				dettaglioTitoloForm.setFlagAbilitazSpec(true);
				Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);


				// almaviva2 11.03.2015 Inizio intervento per consentire il cambio tipo materiale da Antico a Musica/Cartografia/Grafica
				// Novembre 2015 - Bug Mantis 6028 inserito if per controllare che in caso di variazione di qualunque
				// campo parte dati comune, se il tipo materiale è ANTICO (E) i materiali coerenti come arrivo di variazione
				// possono essere U, G, C, ma anche la E stessa ........
				// Inizio Intervento Novembre 2015 DEFINITIVO - Si eliminano tutti i controlli sul cambio materiale e si rimandano
				// tutti al protocollo di indice o polo
//				if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloPFissaVO().getTipoMat() != null) {
//					if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
//						if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U") ||
//								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("G") ||
//								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("C") ||
//								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
//							// Nulla da fare: Operazione Ammessa
//						} else  {
//							// Diagnostico per non Ammesso questo cambio tipo Materiale
//							ActionMessages errors = new ActionMessages();
//							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.cambioTipoMatErrato"));
//							this.saveErrors(request, errors);
//							return mapping.getInputForward();
//						}
//					}
//				}
				// almaviva2 11.03.2015 Fine intervento per consentire il cambio tipo materiale da Antico a Musica/Cartografia/Grafica
				// Fine Intervento Novembre 2015 DEFINITIVO - Si eliminano tutti i controlli sul cambio materiale e si rimandano
				// tutti al protocollo di indice o polo

				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("H")) {
					// Nel caso di Materiale Audiovisivo si devono controllare prima le autorizzazioni per il Materiale in oggetto
					// se è presente si prosegue; se assente si controlla se l'utente è abilitato almeno alla parte musicale
					// perchè in questo caso si dovrà abilitare alla modifica solo quella parte;

					// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
					// 1)	Modificato tipo materiale da M a H, lasciando tipo record a;
					// il sistema prospetta i campi relativi alla sola rappresentazione (N.B.: il libretto NON può essere H!!!);
					// al salvataggio inviato diagnostico: Protocollo di INDICE: 3271 Tipo record non compatibile con tipo materiale
					// il sistema non deve prospettare i campi specifici H per documenti aventi tipo record diverso da ‘i’, ‘j’, ‘g’
					// (ID inibisce la modifica del tipo materiale in H per documenti ad es. con tipo record ‘a’,
					// soluzione che ci sembra buona: occorre modificare il tipo record prima di effettuare la modifica del materiale in H).
					if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("i")
							&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("j")
							&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("g")) {
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoRec("");
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.MatHtipoRecordErrato"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					// Fine Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio

					dettaglioTitoloForm.setTipoProspetSpecSoloMus(dettaglioTitoloForm.getTipoProspettazione());
					dettaglioTitoloForm.setFlagAbilitazSpecSoloMus(true);
					try{
						utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat());
					}catch(UtenteNotAuthorizedException ute)
					{
						dettaglioTitoloForm.setFlagAbilitazSpec(false);
						try{
							utenteEjb.isAbilitatoTipoMateriale("U");
						}catch(UtenteNotAuthorizedException uteMus)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.tipoMaterNotAuthorizedInfo",
									dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().toString()));
							this.saveErrors(request, errors);
							dettaglioTitoloForm.setFlagAbilitazSpecSoloMus(false);
						}
						// MODIFICA 23 SETTEMBRE 2015; nel caso di abilitazione alla sola parte MUSICALE e non AUDIOVISIVA
						// si manda comunque un messaggio informativo
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.tipoMaterParzialmenteNotAuthorizedInfo",
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().toString()));
						this.saveErrors(request, errors);
					}
				} else {
					try{
						utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat());
					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.tipoMaterNotAuthorizedInfo",
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().toString()));
						this.saveErrors(request, errors);
						dettaglioTitoloForm.setFlagAbilitazSpec(false);
					}
				}

			}
			// Fine modifica BUG MANTIS 3551

			if (request.getAttribute("flagCondiviso") != null) {
				if ((Boolean) request.getAttribute("flagCondiviso")) {
					dettaglioTitoloForm.setFlagCondiviso(true);
				} else {
					dettaglioTitoloForm.setFlagCondiviso(false);
					Navigation.getInstance(request).setTesto("Varia locale");
				}
			}
		}

		if (dettaglioTitoloForm.getTipoProspettazione().equals("AGGNOTA")) {
			Navigation.getInstance(request).setTesto("Aggiorna nota");
			if (request.getAttribute("flagCondiviso") != null) {
				if ((Boolean) request.getAttribute("flagCondiviso")) {
					dettaglioTitoloForm.setFlagCondiviso(true);
				} else {
					dettaglioTitoloForm.setFlagCondiviso(false);
					Navigation.getInstance(request).setTesto("Aggiorna nota locale");
				}
			}
		}

		if (dettaglioTitoloForm.getTipoProspettazione().equals("SUG")) {
			dettaglioTitoloForm.setFlagCondiviso(false);
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLivAut("01");
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setBid("");
			this.caricaDescrizioniSuggerimento(dettaglioTitoloForm);
			Navigation.getInstance(request).setTesto("Crea suggerimento");
		}


		if (dettaglioTitoloForm.getTipoProspettazione().equals("VARIANAT")) {
			Navigation.getInstance(request).setTesto("Cambia natura");
			if (request.getAttribute("flagCondiviso") != null) {
				if ((Boolean) request.getAttribute("flagCondiviso")) {
					dettaglioTitoloForm.setFlagCondiviso(true);
				} else {
					dettaglioTitoloForm.setFlagCondiviso(false);
					Navigation.getInstance(request).setTesto("Aggiorna nota locale");
				}
			}
			this.caricaDescrizioniSuggerimento(dettaglioTitoloForm);
		}


		if (dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
			dettaglioTitoloForm.setFlagCondiviso(false);
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLivAut("04");
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setBid("");
			this.caricaDescrizioniSuggerimento(dettaglioTitoloForm);
			if (request.getAttribute("treeViewRadice") != null) {
				dettaglioTitoloForm.setTreeElementViewCopia((TreeElementViewTitoli) request.getAttribute("treeViewRadice"));
			}
			Navigation.getInstance(request).setTesto("Crea copia reticolo");
		}



		if (dettaglioTitoloForm.getTipoProspettazione().equals("DET")
				|| dettaglioTitoloForm.getTipoProspettazione().equals("AGGNOTA")) {
			this.caricaDescrizioni(dettaglioTitoloForm);
//			Modifica almaviva2 05.03.2010 - BUG 3608 - inserita inizializzazione del tipo Sequenza
			if (dettaglioTitoloForm.getDettTitComVO().getCampoSequenza() == null) {
				dettaglioTitoloForm.getDettTitComVO().setCampoSequenza("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getCampoSici() == null) {
				dettaglioTitoloForm.getDettTitComVO().setCampoSici("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getCampoSottoTipoLegame() == null) {
				dettaglioTitoloForm.getDettTitComVO().setCampoSottoTipoLegame("");
			}

		}

		if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
				|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
				|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
				|| dettaglioTitoloForm.getTipoProspettazione().equals("VARIANAT")
				|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
			this.caricaComboGenerali(dettaglioTitoloForm);
		}

		this.caricaComboNatura(dettaglioTitoloForm);
		if (dettaglioTitoloForm.getNatura() == null) {
			dettaglioTitoloForm.setNatura("");
		}
		// se la natura in mappa in vuota vuol dire che è il primo giro e devo valorizzarla con quella del DB
		// se anche quella del dDB è vuota vuol dire che devo metterci la prima della lista della combo
		// se la natura in mappa non è vuota vuol dire che stò modificando un valore precedentemente immesso
		// quindi non devo effettuare alcun cambiamento.

		// Inizio Modifica almaviva2 30.11.2010 BUG MANTIS 3932 impostazione della natura se in fase di interrogazione
		// la natura usata come filtro non sia disponibile per la creazione atomica,
		// si accede ai default e si caricano quelli; (DettaglioTitoloAction metodo unspecified)

		if (dettaglioTitoloForm.getNatura().equals("")) {
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura() != null) {
				if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("")) {
					dettaglioTitoloForm.setNatura(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura());
					boolean trovato = false;
					if (dettaglioTitoloForm.getListaNatura().size() > 0) {
						ComboCodDescVO codDesc;

						for (int i = 0; i < dettaglioTitoloForm.getListaNatura().size(); i++) {
							codDesc = new ComboCodDescVO();
							codDesc = (ComboCodDescVO) dettaglioTitoloForm.getListaNatura().get(i);
							if (codDesc.getCodice().equals(dettaglioTitoloForm.getNatura())) {
								trovato= true;
								break;
							}
						}
					}
					if (!trovato) {
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setNatura(dettaglioTitoloForm.getNaturaDefault());
						dettaglioTitoloForm.setNatura(dettaglioTitoloForm.getNaturaDefault());
					}
				}
			}
		}
		// Fine Modifica almaviva2 30.11.2010 BUG MANTIS 3932

		// Inizio Modifica almaviva2 Settembre 2014 -  BUG MANTIS 5636 impostazione del tipo Materiale se in fase di interrogazione
		// non sia stato usato come filtro; si accede ai default e si caricano quelli; (DettaglioTitoloAction metodo unspecified)
		this.caricaComboTipoMat(dettaglioTitoloForm);
		if (dettaglioTitoloForm.getTipoMateriale() == null) {
			dettaglioTitoloForm.setTipoMateriale("");
		}
		if (dettaglioTitoloForm.getTipoMateriale().equals("")) {
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat() != null) {
				if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")) {
					dettaglioTitoloForm.setTipoMateriale(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat());
					boolean trovato = false;
					if (dettaglioTitoloForm.getListaTipoMat() != null) {
						if (dettaglioTitoloForm.getListaTipoMat().size() > 0) {
							ComboCodDescVO codDesc;

							for (int i = 0; i < dettaglioTitoloForm.getListaTipoMat().size(); i++) {
								codDesc = new ComboCodDescVO();
								codDesc = (ComboCodDescVO) dettaglioTitoloForm.getListaTipoMat().get(i);
								if (codDesc.getCodice().equals(dettaglioTitoloForm.getTipoMateriale())) {
									trovato= true;
									break;
								}
							}
						}
					}

					if (!trovato) {
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMat("");
						dettaglioTitoloForm.setTipoMateriale("");
					}
				}
			}
		}
		// Fine Modifica almaviva2 Settembre 2014 BUG MANTIS 5636 impostazione della natura se in fase di interrogazione

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("C")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLingua1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("N")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("T")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPaese("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("P")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("D")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLingua1("");
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPaese("");
		}

		// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("B")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPaese("");
		}



//		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
//		// e come le T per i campi da gestire
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("R")) {
//			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMat("");
//			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoRec("");
//			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLingua1("");
//			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPaese("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("C")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("D")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("P")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("T")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("B")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMat("");
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoRec("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")) {
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setLingua1("");
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPaese("");
			} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPaese("");
			}
		}


        // Inizio Intervento per Google3: Intervento per inviare, nel caso di Polo /Bibliotecario non abilitato ai materiali
		// speciali, la parte relativa alla specializzazione stessa in modalità DETTAGLIO (utilizzando il nuovo campo
		// TipoProspetSpec su dettaglioTitoloForm) e quindi non modificabile
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U")) {
			dettaglioTitoloForm.setTipoMateriale("U");

			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
			}

			// MANUTENZIONE ADEGUATIVA APRILE 2018 - almaviva2 - Vedi Mantis
			// Il livello di autorità della specificità deve essere 51 fisso così da non aver problemi al momento
			// di una successiva condivisione in Indice
			if (dettaglioTitoloForm.getTipoProspettazione().equals("SUG")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setLivAutSpec("51");
			}

//			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET")) {
			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniMusica(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboMusica(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("C")) {
			dettaglioTitoloForm.setTipoMateriale("C");

			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
			}

			// MANUTENZIONE ADEGUATIVA APRILE 2018 - almaviva2 - Vedi Mantis
			// Il livello di autorità della specificità deve essere 51 fisso così da non aver problemi al momento
			// di una successiva condivisione in Indice
			if (dettaglioTitoloForm.getTipoProspettazione().equals("SUG")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLivAutSpec("51");
			}

			this.caricaGradiCartografia(dettaglioTitoloForm);
//			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET")) {
			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniCartografia(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboCartografia(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("G")) {
			dettaglioTitoloForm.setTipoMateriale("G");

			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
			}

			// MANUTENZIONE ADEGUATIVA APRILE 2018 - almaviva2 - Vedi Mantis
			// Il livello di autorità della specificità deve essere 51 fisso così da non aver problemi al momento
			// di una successiva condivisione in Indice
			if (dettaglioTitoloForm.getTipoProspettazione().equals("SUG")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setLivAutSpec("51");
			}

//			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET")) {
			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniGrafica(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboGrafica(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("H")) {
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			// Nel caso di Materiale Audiovisivo si devono controllare prima le autorizzazioni per il Materiale in oggetto
			// se è presente si prosegue; se assente si controlla se l'utente è abilitato almeno alla parte musicale
			// perchè in questo caso si dovrà abilitare alla modifica solo quella parte;
			dettaglioTitoloForm.setTipoMateriale("H");
			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
				if (dettaglioTitoloForm.isFlagAbilitazSpecSoloMus())  {
					dettaglioTitoloForm.setTipoProspetSpecSoloMus("");
				} else {
					dettaglioTitoloForm.setTipoProspetSpecSoloMus("DET");
				}
			}

			// MANUTENZIONE ADEGUATIVA APRILE 2018 - almaviva2 - Vedi Mantis
			// Il livello di autorità della specificità deve essere 51 fisso così da non aver problemi al momento
			// di una successiva condivisione in Indice
			if (dettaglioTitoloForm.getTipoProspettazione().equals("SUG")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().setLivAutSpec("51");
			}

			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniAudiovisivo(dettaglioTitoloForm, "");
				this.caricaDescrizioniMusica(dettaglioTitoloForm);
				// Nel caso di Materiale Audiovisivo si devono controllare prima le autorizzazioni per il Materiale in oggetto
				// se è presente si prosegue; se assente si controlla se l'utente è abilitato almeno alla parte musicale
				// perchè in questo caso si dovrà abilitare alla modifica solo quella parte;
				this.caricaComboMusica(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboAudiovisivo(dettaglioTitoloForm);
				this.caricaComboMusica(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("L")) {
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			dettaglioTitoloForm.setTipoMateriale("L");

			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
			}

			// MANUTENZIONE ADEGUATIVA APRILE 2018 - almaviva2 - Vedi Mantis
			// Il livello di autorità della specificità deve essere 51 fisso così da non aver problemi al momento
			// di una successiva condivisione in Indice
			if (dettaglioTitoloForm.getTipoProspettazione().equals("SUG")) {
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().setLivAutSpec("51");
			}

			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniElettronico(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboElettronico(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M")) {
			dettaglioTitoloForm.setTipoMateriale("M");
			// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
			// viene esteso anche al Materiale Moderno e Antico

			// Aprile 2016 - almaviva2 Bug 0006168 esercizio
			// Modificato controllo per impostare correttamente la visualizzazione della mappa di dettaglio per voce
			// del Vai a 'Correggi nota ISBD'
			// if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") ) {
			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
			}
			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniModAnt(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboModAnt(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
			dettaglioTitoloForm.setTipoMateriale("E");
			// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
			// viene esteso anche al Materiale Moderno e Antico

			// Aprile 2016 - almaviva2 Bug 0006168 esercizio
			// Modificato controllo per impostare correttamente la visualizzazione della mappa di dettaglio per voce
			// del Vai a 'Correggi nota ISBD'
			// if (dettaglioTitoloForm.getTipoProspettazione().equals("DET")) {
			if (dettaglioTitoloForm.isFlagAbilitazSpec())  {
				dettaglioTitoloForm.setTipoProspetSpec("");
			} else  {
				dettaglioTitoloForm.setTipoProspetSpec("DET");
			}
			if (dettaglioTitoloForm.getTipoProspettazione().equals("DET") || dettaglioTitoloForm.getTipoProspetSpec().equals("DET")) {
				this.caricaDescrizioniModAnt(dettaglioTitoloForm);
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("SUG")
					|| dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
				this.caricaComboModAnt(dettaglioTitoloForm);
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")) {
			dettaglioTitoloForm.setTipoMateriale("");
		}

		// Inizio modifica almaviva2 BUG MANTIS 3842 08.07.2010 deve essere impostato solo la prima volta
		if (dettaglioTitoloForm.isFirstTime()) {
			dettaglioTitoloForm.setDettTitComVOOLD(dettaglioTitoloForm.getDettTitComVO());
		}
		// Fine modifica almaviva2 BUG MANTIS 3842 08.07.2010 deve essere impostato solo la prima volta

		return mapping.getInputForward();
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute("bid", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("livRicerca", "I");
		return Navigation.getInstance(request).goBack();
	}

	public ActionForward aggiornaCanali(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		dettaglioTitoloForm.setTipoMateriale(dettaglioTitoloForm
				.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat());

		this.caricaComboGenerali(dettaglioTitoloForm);

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U")) {
			dettaglioTitoloForm.setTipoMateriale("U");
			this.caricaComboMusica(dettaglioTitoloForm);
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("C")) {
			dettaglioTitoloForm.setTipoMateriale("C");
			this.caricaComboCartografia(dettaglioTitoloForm);
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("G")) {
			dettaglioTitoloForm.setTipoMateriale("G");
			this.caricaComboGrafica(dettaglioTitoloForm);
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M")) {
			dettaglioTitoloForm.setTipoMateriale("M");
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
			dettaglioTitoloForm.setTipoMateriale("E");
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward copiaReticoloLocale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		dettaglioTitoloForm.setTipoCopiaReticolo("Locale");

		ActionForward forward = this.confermaOper(mapping, form, request, response);
		return forward;
	}

	public ActionForward copiaReticoloCondiviso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		dettaglioTitoloForm.setTipoCopiaReticolo("Condiviso");

		ActionForward forward = this.confermaOper(mapping, form, request, response);
		return forward;
	}



	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;


		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
				&& dettaglioTitoloForm.isFlagCondiviso()
				&& dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("R")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins047"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (dettaglioTitoloForm.getTipoProspettazione().equals("COPIARET")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.funzNoDisp"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		if (dettaglioTitoloForm.getTipoMateriale() == null) dettaglioTitoloForm.setTipoMateriale("");

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLivAut() == null ||
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLivAut().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins001"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura() == null ||
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setNatura("");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins041"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		// Bug mantis esercizio 6421: almaviva2 Maggio 2017 - viene reipostato il campo tipo testo letterario quando, a seguito
		// del cambio tipo-record tale campo viene nascosto nella mappa di dettaglio ma non viene reimpostato a spazio.
		if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("a")
				&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("b")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoTestoLetterario("");
		}


		// Inizio intervento mail Scognamiglio Marzo 2016 almaviva2
		// se il tipo materiale è H e il tipo record è 'm' (si sta schedando ad es. un kit di un DVD-video e un CD-audio)
		// e il tipo di supporto dell'area 0 è 'zu', il sistema non riesce - giustamente - a capire che campi specifici deve prospettare.
		// ora prospetta quelli della rappresentazione. se si salva si crea un mostro non interrogabile
		// inserire  un controllo relativo alla non compatibilità tra tipo record 'm' e tipo materiale H
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("H") &&
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("m")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.incompatibileMatHrecM"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
		// Per tipo materiale elettronico "L" è ammesso solo il tipo record l
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("L") &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("l")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.compatibileMatLrecl"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		// Inizio intervento BUG MANTIS 4910 (esercizio) almaviva2
		// il controllo sulla presenza del tipo record per i doumenti deve essere effettuato prima di altri controlli vari
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("M")
	            || dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("S")
	            || dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("N")
	            || dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("W")) {
	            if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() == null
	            		|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("")) {
	            	ActionMessages errors = new ActionMessages();
	    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.TipoRecordObbligPerDoc"));
	    			this.saveErrors(request, errors);
	    			return mapping.getInputForward();
	            }
		}
		// Fine intervento BUG MANTIS 4910 (esercizio) almaviva2

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("C")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("R")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMat("");
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoRec("");
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("W")) {
			if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")) {
				if (dettaglioTitoloForm.getLegame51() == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.creazWImpossibile"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				} else if (dettaglioTitoloForm.getLegame51().equals("NO")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.creazWImpossibile"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("N")) {
			if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")) {
				if (dettaglioTitoloForm.getLegame51() == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.creazNImpossibile"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				} else if (dettaglioTitoloForm.getLegame51().equals("NO")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.creazNImpossibile"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}

		// Aprile 2017 EVOLUTIVA su link esterni; viene completato intervento affiche si crei automaticamente
		// la string link esterno da passare al protocollo che provvederà a validarla e solo se corretta ad inserirla
		// sulla base dati
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().size() > 0) {
			List<SifRicercaRepertoriVO> listaRepertoriNew = new ArrayList<SifRicercaRepertoriVO>();
			SifRicercaRepertoriVO eleRepertorio = null;

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			listaRepertoriNew = factory.getGestioneRepertorio().getAllRepertori();
			if (listaRepertoriNew.size() > 0) {

				TabellaNumSTDImpronteVO tabLinkEsterni = new TabellaNumSTDImpronteVO();
				String siglaLinkEsterno = "";
				String idLinkEsterno = "";
				String indirizzoCompleto = "";
				Boolean trovato = false;

				for (int iLink = 0; iLink < dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().size(); iLink++) {
					tabLinkEsterni = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(iLink);
					siglaLinkEsterno = tabLinkEsterni.getCampoUno();

					// Manutenzione marzo 2018 almaviva2 - è necessario controllare che la tabella dei link esterni nel caso ci sia una
					// riga, sia correttamente valorizzata altrimenti si deve inviare messaggio di errore.
					if (siglaLinkEsterno.length() == 0) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}


					if (tabLinkEsterni.getDescCampoDue().equals("")) {
						String descrizioneLinkEsterno = factory.getCodici().cercaDescrizioneCodice(siglaLinkEsterno,
								CodiciType.CODICE_LINK_ALTRA_BASE_DATI,
								CodiciRicercaType.RICERCA_CODICE_UNIMARC);
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(iLink).setDescCampoDue(descrizioneLinkEsterno);
					}

					idLinkEsterno = tabLinkEsterni.getCampoDue();
					for (int iRep = 0; iRep < listaRepertoriNew.size(); iRep++) {
						eleRepertorio = listaRepertoriNew.get(iRep);

						if (eleRepertorio.getTipo().toUpperCase().equals("B")
								//&& eleRepertorio.getSigl().toUpperCase().equals(tabLinkEsterni.getDescCampoDue().toUpperCase())) {
								&& eleRepertorio.getSigl().toUpperCase().equals(tabLinkEsterni.getCampoUno().toUpperCase())) {

							indirizzoCompleto = eleRepertorio.getDesc();
							if (siglaLinkEsterno.startsWith("a")) {  			// BRITISH MUSEUM
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							} else if (siglaLinkEsterno.equals("b")) {		// EDIT16
								idLinkEsterno = idLinkEsterno.replace("CNCE", "");
								idLinkEsterno = idLinkEsterno.replace(" ", "");
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(iLink).setCampoDue(idLinkEsterno);
								if (!ValidazioneDati.strIsNumeric(idLinkEsterno)) {
									ActionMessages errors = new ActionMessages();
									errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
									this.saveErrors(request, errors);
									return mapping.getInputForward();
								}
							} else if (siglaLinkEsterno.equals("c") || siglaLinkEsterno.equals("d")) {		// ESTC o ISTC
								if (idLinkEsterno.contains(" ")) {
									ActionMessages errors = new ActionMessages();
									errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
									this.saveErrors(request, errors);
									return mapping.getInputForward();
								}
							} else if (siglaLinkEsterno.equals("e")) {		// VD16
								idLinkEsterno = idLinkEsterno.replace(" ", "+");
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(iLink).setCampoDue(idLinkEsterno);
								if (!idLinkEsterno.contains("+")) {
									ActionMessages errors = new ActionMessages();
									errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
									this.saveErrors(request, errors);
									return mapping.getInputForward();
								}
							} else if (siglaLinkEsterno.equals("f")) {		// VD17
								idLinkEsterno = idLinkEsterno.replace(" ", ":");
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(iLink).setCampoDue(idLinkEsterno);
								if (!idLinkEsterno.contains(":")) {
									ActionMessages errors = new ActionMessages();
									errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
									this.saveErrors(request, errors);
									return mapping.getInputForward();
								}
							}
							String indirizzoRicomposto = ricalcolaLinkEsterno(indirizzoCompleto, idLinkEsterno);
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(iLink).setNota(indirizzoRicomposto);
							trovato = true;
							break;
						}
					}
				}
				if (!trovato) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}

		// Inizio Intervento interno Aprile 2016 - almaviva2: Inserire controllo su validità
		// del personaggio che deve essere valorizzato
		// dell'interprete che deve essere un VID valido e non una stringa

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().size() > 0) {
			TabellaNumSTDImpronteVO tabPersonaggi = new TabellaNumSTDImpronteVO();
			for (int i = 0; i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().size(); i++) {
				tabPersonaggi = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().get(i);
				if (tabPersonaggi.getCampoUno() == null
						|| tabPersonaggi.getCampoUno().length() == 0) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selPersonaggioErrata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				// Luglio 2016 - manutenzione mantis 6235: corretto il controllo sulla presenza dell'interprete:
				// il campo può essere vuoto o null ma se valorizzato deve essere presente un vid
//				if (tabPersonaggi.getNota() == null
//						|| tabPersonaggi.getNota().length() != 10
//						|| !tabPersonaggi.getNota().substring(3, 4).equals("V")) {
//					ActionMessages errors = new ActionMessages();
//					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selInterpreteErrata"));
//					this.saveErrors(request, errors);
//					return mapping.getInputForward();
//				}
				if (!ValidazioneDati.strIsEmpty(tabPersonaggi.getNota())) {
					if (tabPersonaggi.getNota().length() != 10
						|| !tabPersonaggi.getNota().substring(3, 4).equals("V")) {
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selInterpreteErrata"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
					}
				}
			}
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().size() > 0) {
			TabellaNumSTDImpronteVO tabPersonaggi = new TabellaNumSTDImpronteVO();
			for (int i = 0; i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().size(); i++) {
				tabPersonaggi = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().get(i);
				if (tabPersonaggi.getCampoUno() == null
						|| tabPersonaggi.getCampoUno().length() == 0) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selPersonaggioErrata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				// Luglio 2016 - manutenzione mantis 6235: corretto il controllo sulla presenza dell'interprete:
				// il campo può essere vuoto o null ma se valorizzato deve essere presente un vid
//				if (tabPersonaggi.getNota() == null
//						|| tabPersonaggi.getNota().length() != 10
//						|| !tabPersonaggi.getNota().substring(3, 4).equals("V")) {
//					ActionMessages errors = new ActionMessages();
//					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selInterpreteErrata"));
//					this.saveErrors(request, errors);
//					return mapping.getInputForward();
//				}
				if (!ValidazioneDati.strIsEmpty(tabPersonaggi.getNota())) {
						if (tabPersonaggi.getNota().length() != 10
							|| !tabPersonaggi.getNota().substring(3, 4).equals("V")) {
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selInterpreteErrata"));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
					}
				}
			}
		}
		// Fine Intervento interno Aprile 2016 - almaviva2: Inserire controllo su validità






		// Controllo sulla tabella personaggi perchè, se sono stati inseriti personaggi nuovi
		// andranno catturati in Polo affinchè la variazione abbia esito positivo
		List<ComboSoloDescVO> listaInterpreti = new ArrayList<ComboSoloDescVO>();


        // Inizio Intervento per Google3: l’interrogazione effettuata da un Polo non Abilitato ad uno specifico Materiale
        // non propone le specificità ma invia il DocType del moderno mantenendo il tipo Materiale corretto, quindi si deve
        // gestire l’inserimento nel DB di polo di documenti con tipo materiale ‘U’, ‘C’ o ‘G’ senza specificità
        // Inserito il Primo if per gestire la visualizzazion bloccata delle area delle specializzazioni;
		if (dettaglioTitoloForm.isFlagAbilitazSpec()) {
			if (dettaglioTitoloForm.getTipoMateriale().equals("U")) {

				// Inizio modifica almaviva2 19.07.2010 Intervento Interno - inserito contollo su livAutorità del tipo materiale prescelto
				//almaviva5_20100908 escluso controllo liv.aut su natura A musicale (mail RMR del 07-09-2010)
				if (!ValidazioneDati.isFilled(dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getLivAutSpec())
						&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A") ) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins001Spec"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				// Fine modifica almaviva2 19.07.2010

				// Inizio modifica almaviva2 04.05.2011 BUG MANTIS 4411 (collaudo)
				// inserito un controllo per impedire l'inserimento di una notizia con Tipo materiale U,
				// Tipo record 'c' e Tipo testo (specificità) 'b', quindi con incompatibilità tra tipo record e tipo testo.

				if (ValidazioneDati.isFilled(dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTipoTesto())
						&& dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTipoTesto().equals("b")) {
					if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("a")
							&& !dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("b")) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.IncongruenzaTipoRecTipoTesto"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
				// Fine modifica almaviva2 04.05.2011 BUG MANTIS 4411 (collaudo)


				if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloMusVO().getListaPersonaggi().isEmpty() &&
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().isEmpty()) {
				} else if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloMusVO().getListaPersonaggi().isEmpty() &&
						!dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().isEmpty()) {
					TabellaNumSTDImpronteVO tabellaNumSTDImpronteVO = new TabellaNumSTDImpronteVO();
					ComboSoloDescVO comboSoloDescVO = new ComboSoloDescVO();
					for (int i = 0;
						i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().size();
						i++) {
						tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().get(i);
						comboSoloDescVO.setDescrizione(tabellaNumSTDImpronteVO.getNota());
						listaInterpreti.add(comboSoloDescVO);
					}
				} else {
					List<ComboSoloDescVO> listaInterpretiOld = new ArrayList<ComboSoloDescVO>();
					List<ComboSoloDescVO> listaInterpretiNew = new ArrayList<ComboSoloDescVO>();

					TabellaNumSTDImpronteVO tabellaNumSTDImpronteVO = new TabellaNumSTDImpronteVO();
					ComboSoloDescVO comboSoloDescVO = new ComboSoloDescVO();

					for (int i = 0; i < dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloMusVO().getListaPersonaggi().size(); i++) {
						tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloMusVO().getListaPersonaggi().get(i);
						comboSoloDescVO.setDescrizione(tabellaNumSTDImpronteVO.getNota());
						listaInterpretiOld.add(comboSoloDescVO);
					}
					Collections.sort(listaInterpretiOld, ComboSoloDescVO.ORDINA_PER_DESCRIZIONE);

					for (int i = 0;	i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().size(); i++) {
						tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().get(i);
						comboSoloDescVO.setDescrizione(tabellaNumSTDImpronteVO.getNota());
						listaInterpretiNew.add(comboSoloDescVO);
					}
					Collections.sort(listaInterpretiNew, ComboSoloDescVO.ORDINA_PER_DESCRIZIONE);

					for (int i = 0;	i < listaInterpretiNew.size(); i++) {
						if (listaInterpretiOld.contains(listaInterpretiNew.get(i))) {
							listaInterpreti.add(listaInterpretiNew.get(i));
						}
					}
				}

				//almaviva5_20150305 #5780
				if (!controlloAggiornamentoIncipit(request, form) )
					return mapping.getInputForward();
			}

			// Controllo sui campi di longitudine/latitudine
			if (dettaglioTitoloForm.getTipoMateriale().equals("C")) {

				// Inizio modifica almaviva2 19.07.2010 Intervento Interno - inserito contollo su livAutorità del tipo materiale prescelto
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLivAutSpec() == null ||
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLivAutSpec().equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins001Spec"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				// Fine modifica almaviva2 19.07.2010

				// BUG COLLAUDO 5009: problema rimasto è il mancato controllo sui valori ammessi per la latitudine che non puo superare
				// le coordinate 90° 00' 00" quindi, per la latitudine deve ammettere nel campo gradi due caratteri e trasformarli
				// in tre, anteponendo uno zero, nel messaggio xml per l'indice
				// Il diagnostico in caso di digitazione convalori maggiori degli ammessi deve essere
				// La latitudine va da 00° 00' 00" a 90° 00' 00" e viceversa come interfaccia diretta.
				if (!(dettaglioTitoloForm.getLatiInput1gg().toString() +
						dettaglioTitoloForm.getLatiInput1pp().toString() +
						dettaglioTitoloForm.getLatiInput1ss().toString()).equals("")) {
//				if (dettaglioTitoloForm.getLatiInput1gg().length() != 3
//						|| dettaglioTitoloForm.getLatiInput1pp().length() != 2
//						|| dettaglioTitoloForm.getLatiInput1ss().length() != 2) {
					if (dettaglioTitoloForm.getLatiInput1gg().length() != 2
							|| dettaglioTitoloForm.getLatiInput1pp().length() != 2
							|| dettaglioTitoloForm.getLatiInput1ss().length() != 2) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric009"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}

//					Bug collaudo 5009: il formato corretto delle coordinate geografiche non è: 000° 000' 000'' ma 000° 00' 00''
//			        Inserite etichetta Max nord e cosi' via per uniformare a Interfaccia diretta
//			        Modificato controllo su latitudine affinche in presenza di 000° 00' 00'' cioe EQUATORE non si richieda l'emisfero (N/S)
					if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitTipo1().equals("")
							&& !(dettaglioTitoloForm.getLatiInput1gg().toString() +
									dettaglioTitoloForm.getLatiInput1pp().toString() +
//									dettaglioTitoloForm.getLatiInput1ss().toString()).equals("0000000")) {
									dettaglioTitoloForm.getLatiInput1ss().toString()).equals("000000")) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric006"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					} else {

						int lati1 = Integer.parseInt(dettaglioTitoloForm.getLatiInput1gg().toString() +
								dettaglioTitoloForm.getLatiInput1pp().toString() +
								dettaglioTitoloForm.getLatiInput1ss().toString());
						if (lati1 > 900000) {
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric009"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}

						dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLatitValNS1(
								"0" + dettaglioTitoloForm.getLatiInput1gg().toString() + "°" +
								dettaglioTitoloForm.getLatiInput1pp().toString() + "'" +
								dettaglioTitoloForm.getLatiInput1ss().toString() + '"');
					}
				} else {
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLatitValNS1("   °  \'  \"");
				}

				if (!(dettaglioTitoloForm.getLatiInput2gg().toString() +
						dettaglioTitoloForm.getLatiInput2pp().toString() +
						dettaglioTitoloForm.getLatiInput2ss().toString()).equals("")) {
//					if (dettaglioTitoloForm.getLatiInput2gg().length() != 3
//							|| dettaglioTitoloForm.getLatiInput2pp().length() != 2
//							|| dettaglioTitoloForm.getLatiInput2ss().length() != 2) {
					if (dettaglioTitoloForm.getLatiInput2gg().length() != 2
							|| dettaglioTitoloForm.getLatiInput2pp().length() != 2
							|| dettaglioTitoloForm.getLatiInput2ss().length() != 2) {

						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric010"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}

//					Bug collaudo 5009: il formato corretto dele coordinate geografiche non è: 000° 000' 000'' ma 000° 00' 00''
//			        Inserite etichetta Max nord e cosi' via per uniformare a Interfaccia diretta
//			        Modificato controllo su latitudine affinche in presenza di 000° 00' 00'' cioe EQUATORE non si richieda l'emisfero (N/S)
					if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitTipo2().equals("")
							&& !(dettaglioTitoloForm.getLatiInput2gg().toString() +
									dettaglioTitoloForm.getLatiInput2pp().toString() +
//									dettaglioTitoloForm.getLatiInput2ss().toString()).equals("0000000")) {
									dettaglioTitoloForm.getLatiInput2ss().toString()).equals("000000")) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric006"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					} else {

						int lati2 = Integer.parseInt(dettaglioTitoloForm.getLatiInput2gg().toString() +
								dettaglioTitoloForm.getLatiInput2pp().toString() +
								dettaglioTitoloForm.getLatiInput2ss().toString());
						if (lati2 > 900000) {
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric010"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}

						dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLatitValNS2(
								"0" + dettaglioTitoloForm.getLatiInput2gg().toString() + "°" +
								dettaglioTitoloForm.getLatiInput2pp().toString() + "'" +
								dettaglioTitoloForm.getLatiInput2ss().toString() + '"');
					}
				} else {
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLatitValNS2("   °  \'  \"");
				}

				if (!(dettaglioTitoloForm.getLongInput1gg().toString() +
						dettaglioTitoloForm.getLongInput1pp().toString() +
						dettaglioTitoloForm.getLongInput1ss().toString()).equals("")) {
					if (dettaglioTitoloForm.getLongInput1gg().length() != 3
							|| dettaglioTitoloForm.getLongInput1pp().length() != 2
							|| dettaglioTitoloForm.getLongInput1ss().length() != 2) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric007"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitTipo1().equals("")) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric006"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					} else {
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLongitValEO1(
								dettaglioTitoloForm.getLongInput1gg().toString() + "°" +
								dettaglioTitoloForm.getLongInput1pp().toString() + "'" +
								dettaglioTitoloForm.getLongInput1ss().toString() + '"');
					}
				} else {
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLongitValEO1("   °  \'  \"");
				}

				if (!(dettaglioTitoloForm.getLongInput2gg().toString() +
						dettaglioTitoloForm.getLongInput2pp().toString() +
						dettaglioTitoloForm.getLongInput2ss().toString()).equals("")) {
					if (dettaglioTitoloForm.getLongInput2gg().length() != 3
							|| dettaglioTitoloForm.getLongInput2pp().length() != 2
							|| dettaglioTitoloForm.getLongInput2ss().length() != 2) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric008"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitTipo2().equals("")) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ric006"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					} else {
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLongitValEO2(
								dettaglioTitoloForm.getLongInput2gg().toString() + "°" +
								dettaglioTitoloForm.getLongInput2pp().toString() + "'" +
								dettaglioTitoloForm.getLongInput2ss().toString() + '"');
					}
				} else {
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setLongitValEO2("   °  \'  \"");
				}
			}


			// Inizio modifica almaviva2 19.07.2010 Intervento Interno - inserito contollo su livAutorità del tipo materiale prescelto
			if (dettaglioTitoloForm.getTipoMateriale().equals("G")) {
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getLivAutSpec() == null ||
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getLivAutSpec().equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins001Spec"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
			// Fine modifica almaviva2 19.07.2010

			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			if (dettaglioTitoloForm.getTipoMateriale().equals("H")) {
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLivAutSpec() == null ||
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLivAutSpec().equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins001Spec"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 3) Accanto alla Durata regist. son/mus stampa andrebbe indicato il formato: hhmmss;
				// il campo va riempito con tutte e 6 le cifre, es.: 001356 ovvero 13 min., 56 sec.
				// Attualmente prende fino a 3 cifre (forse si confonde con la Lunghezza 115$a1-3?).
				// L’eventuale messaggio di errore (ERROR >>httpId: 000003046 - Protocollo di POLO: Impossibile validare il protocollo
				// per l'invio) andrebbe reso più chiaro: Formato durata errato.
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getDurataRegistraz().length() > 0) {
					if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getDurataRegistraz().length() < 6) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.lunghDurataEegistSonMusStampa"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
				// Fine Modifica gennaio 2015
			}
			if (dettaglioTitoloForm.getTipoMateriale().equals("L")) {
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getLivAutSpec() == null ||
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getLivAutSpec().equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins001Spec"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if (dettaglioTitoloForm.getTipoMateriale().equals("M") || dettaglioTitoloForm.getTipoMateriale().equals("E")) {
			if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloModAntVO().getListaPersonaggi().isEmpty() &&
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().isEmpty()) {
			} else if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloModAntVO().getListaPersonaggi().isEmpty() &&
					!dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().isEmpty()) {
				TabellaNumSTDImpronteVO tabellaNumSTDImpronteVO = new TabellaNumSTDImpronteVO();
				ComboSoloDescVO comboSoloDescVO = new ComboSoloDescVO();
				for (int i = 0;
					i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().size();
					i++) {
					tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().get(i);
					comboSoloDescVO.setDescrizione(tabellaNumSTDImpronteVO.getNota());
					listaInterpreti.add(comboSoloDescVO);
				}
			} else {
				List<ComboSoloDescVO> listaInterpretiOld = new ArrayList<ComboSoloDescVO>();
				List<ComboSoloDescVO> listaInterpretiNew = new ArrayList<ComboSoloDescVO>();

				TabellaNumSTDImpronteVO tabellaNumSTDImpronteVO = new TabellaNumSTDImpronteVO();
				ComboSoloDescVO comboSoloDescVO = new ComboSoloDescVO();

				for (int i = 0; i < dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloModAntVO().getListaPersonaggi().size(); i++) {
					tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloModAntVO().getListaPersonaggi().get(i);
					comboSoloDescVO.setDescrizione(tabellaNumSTDImpronteVO.getNota());
					listaInterpretiOld.add(comboSoloDescVO);
				}
				Collections.sort(listaInterpretiOld, ComboSoloDescVO.ORDINA_PER_DESCRIZIONE);

				for (int i = 0;	i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().size(); i++) {
					tabellaNumSTDImpronteVO = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().get(i);
					comboSoloDescVO.setDescrizione(tabellaNumSTDImpronteVO.getNota());
					listaInterpretiNew.add(comboSoloDescVO);
				}
				Collections.sort(listaInterpretiNew, ComboSoloDescVO.ORDINA_PER_DESCRIZIONE);

				for (int i = 0;	i < listaInterpretiNew.size(); i++) {
					if (listaInterpretiOld.contains(listaInterpretiNew.get(i))) {
						listaInterpreti.add(listaInterpretiNew.get(i));
					}
				}
			}
		}


		List<TabellaNumSTDImpronteVO> listaUno = dettaglioTitoloForm.getListaRepertoriModificato();
		List<TabellaNumSTDImpronteAggiornataVO> listaDue = new ArrayList<TabellaNumSTDImpronteAggiornataVO>();
		for (int i=0; i<listaUno.size(); i++) {
			TabellaNumSTDImpronteVO tabRepert = listaUno.get(i);
			TabellaNumSTDImpronteAggiornataVO tabRepertAgg = new TabellaNumSTDImpronteAggiornataVO();
			tabRepertAgg.setCampoUno(tabRepert.getCampoUno());
			tabRepertAgg.setCampoDue(tabRepert.getCampoDue());
			tabRepertAgg.setDescCampoDue(tabRepert.getDescCampoDue());
			tabRepertAgg.setNota(tabRepert.getNota());
			listaDue.add(i,tabRepertAgg);
		}

		// Inizio intervento almaviva2 BUG 3320 - La nota all'ISBN non deve portare alla fine ". - ". Es. ISBN 0-340-16427-2 (errato)
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().size() > 0) {

			TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
			for (int i = 0; i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().size(); i++) {
				tabImpST = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().get(i);
				if (tabImpST.getCampoUno().equals("") || tabImpST.getCampoDue().equals("")) {
					dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().remove(i);
					i--;
				} else if (tabImpST.getCampoUno().length() > 25){
					// Gennaio 2015 almaviva2: Il numeo Standard non puo essere lungo più di 25 caratteri
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.NumStandardTroppoLungo"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				} else if (tabImpST.getNota().contains("errato")){
					continue;
				} else {
					String notaRit="";

					// Gennaio 2015 almaviva2: Il numeo Standard non puo essere lungo più di 25 caratteri
					if (tabImpST.getCampoUno().length() > 25) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.lunghImprontaInvalido"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}

					if (tabImpST.getCampoDue().equals("I")
							&& (tabImpST.getCampoUno().length() != 10 && tabImpST.getCampoUno().length() != 13)) {
						notaRit = "(errato)";
					} else if (tabImpST.getCampoDue().equals("I") && tabImpST.getCampoUno().length() <= 10) {
						// Inizio intervento BUG MANTIS 4604 (collaudo) almaviva2 30.08.2011
						// insrito diagnostico nel caso di catalogazione con numero standard con il trattino
						// (il sistema dà errore bloccante)
						String appo = tabImpST.getCampoUno().substring(0,9);
						if (!ValidazioneDati.strIsNumeric(appo)) {
							tabImpST.setNota("(errato)" + tabImpST.getNota());
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().set(i, tabImpST);
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.NumStandardErratoBis"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
						// Fine intervento BUG MANTIS 4604 (collaudo) almaviva2 30.08.2011
						notaRit = controllaISBN10(tabImpST.getCampoUno());

					} else if (tabImpST.getCampoDue().equals("I") && tabImpST.getCampoUno().length() > 10) {
						// Inizio intervento BUG MANTIS 4604 (collaudo) almaviva2 30.08.2011
						// insrito diagnostico nel caso di catalogazione con numero standard con il trattino
						// (il sistema dà errore bloccante)
						String appo = tabImpST.getCampoUno().substring(0,12);
						if (!ValidazioneDati.strIsNumeric(appo)) {
							tabImpST.setNota("(errato)" + tabImpST.getNota());
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().set(i, tabImpST);
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.NumStandardErratoBis"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
						// Fine intervento BUG MANTIS 4604 (collaudo) almaviva2 30.08.2011
						notaRit = controllaISBN13(tabImpST.getCampoUno());

					} else if (tabImpST.getCampoDue().equals("J")) {
						notaRit = controllaISSN(tabImpST.getCampoUno());
					}
					if (!notaRit.equals("")) {
						// Modifica almaviva2 BUG INDICE 4721 per eliminare spazi superflui nella nota al Num.Standard.
						tabImpST.setNota((notaRit + tabImpST.getNota()).trim());
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().set(i, tabImpST);
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.NumStandardErrato"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
			}// end for
		}

		// Fine intervento almaviva2 BUG 3320 - La nota all'ISBN non deve portare alla fine ". - ". Es. ISBN 0-340-16427-2 (errato)




		//
		// Inizio intervento interno almaviva2 18.07.2011 - La lunghezza dell'impronta non può superare i 32 caratteri
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().size() > 0) {

			TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
			for (int i = 0; i < dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().size(); i++) {
				tabImpST = (TabellaNumSTDImpronteVO) dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().get(i);
				if (tabImpST.getCampoDue().length() != 32) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.lunghImprontaInvalido"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}// end for
		}
//
		// Fine intervento interno almaviva2 18.07.2011


		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;
		AreaDatiVariazioneTitoloVO areaDatiPass = new AreaDatiVariazioneTitoloVO();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPass.setDetTitoloPFissaVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO());
		areaDatiPass.setDetTitoloCarVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO());
		areaDatiPass.setDetTitoloGraVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO());
		areaDatiPass.setDetTitoloMusVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO());
		// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		areaDatiPass.setDetTitoloAudVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO());
		areaDatiPass.setDetTitoloEleVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO());

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		areaDatiPass.setDetTitoloModAntVO(dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO());

		if (listaInterpreti.size() > 0) {
			areaDatiPass.getDetTitoloMusVO().setListaInterpreti(listaInterpreti);
		}
		if (listaDue.size() > 0) {
			areaDatiPass.getDetTitoloPFissaVO().setListaRepertoriNew(listaDue);
		}



		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid().equals("")) {
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")
						&& dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U")) {
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017, "UM");
					utenteEjb.isAbilitatoAuthority("UM");
					// Inizio Evolutive Google3 30.01.2014
					// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
					// la creazione/modifica di oggeti solo su locale
					if (dettaglioTitoloForm.isFlagCondiviso()) {
						if (utenteEjb.isAuthoritySoloLocale(SbnAuthority.UM)) {
							throw new UtenteNotAuthorizedException();
						}
					}
					// Fine Evolutive Google3 30.01.2014
				} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")
						&& (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat() == null
								|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")
								|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M"))) {
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017, "TU");
					utenteEjb.isAbilitatoAuthority("TU");
					// Inizio Evolutive Google3 30.01.2014
					// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
					// la creazione/modifica di oggeti solo su locale
					if (dettaglioTitoloForm.isFlagCondiviso()) {
						if (utenteEjb.isAuthoritySoloLocale(SbnAuthority.TU)) {
							throw new UtenteNotAuthorizedException();
						}
					}
					// Fine Evolutive Google3 30.01.2014
				} else if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")) {
					if (dettaglioTitoloForm.getTipoMateriale().equals("")) {
						utenteEjb.checkAttivita(CodiciAttivita.getIstance().CREA_DOCUMENTO_1016);
						// Inizio Evolutive Google3 30.01.2014
						// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
						// la creazione/modifica di oggeti solo su locale
						// questo è il caso delle collane - forziamo materiale moderno solo x inserire comunque il controllo
						if (dettaglioTitoloForm.isFlagCondiviso()) {
							if (utenteEjb.isTipoMaterialeSoloLocale("M")) {
								throw new UtenteNotAuthorizedException();
							}
						}
						// Fine Evolutive Google3 30.01.2014
					} else {
						utenteEjb.checkAttivitaMat(CodiciAttivita.getIstance().CREA_DOCUMENTO_1016, dettaglioTitoloForm.getTipoMateriale());
						utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getTipoMateriale());
						// Inizio Evolutive Google3 30.01.2014
						// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
						// la creazione/modifica di oggeti solo su locale
						if (dettaglioTitoloForm.isFlagCondiviso()) {
							if (utenteEjb.isTipoMaterialeSoloLocale(dettaglioTitoloForm.getTipoMateriale())) {
								throw new UtenteNotAuthorizedException();
							}
						}
						// Fine Evolutive Google3 30.01.2014
					}
				}
			} else {
				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")
						&& dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U")) {
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026, "UM");
					utenteEjb.isAbilitatoAuthority("UM");
				} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")
						&& (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat() == null
								|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("")
								|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M"))) {
					utenteEjb.checkAttivitaAut(CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026, "TU");
					utenteEjb.isAbilitatoAuthority("TU");
				} else if (!dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")) {
					if (dettaglioTitoloForm.getTipoMateriale().equals("")) {
						utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
					} else {
						// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo serve per gestire le abilitazioni alla sola parte
						// generale e bloccare la variazione dell'area delle specificita
//						utenteEjb.checkAttivitaMat(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023, dettaglioTitoloForm.getTipoMateriale());
//						utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getTipoMateriale());
						// Fine modifica BUG MANTIS 3551
					}
				}
			}

		}catch(UtenteNotAuthorizedException ute)
		{
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid().equals("")) {
			areaDatiPass.setModifica(false);
		} else {
			areaDatiPass.setModifica(true);
		}


		// solo nel caso dei manoscritti (UNICUM) in creazione non si richiedono i simili perchè non sarebbe comunque possibile catturarli.
		areaDatiPass.setConferma(false);
		if (!areaDatiPass.isModifica()) {
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("b")
					|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("d")
					|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("f")) {
				areaDatiPass.setConferma(true);
			}
		}



		areaDatiPass.setNaturaTitoloDaVariare(areaDatiPass.getDetTitoloPFissaVO().getNatura());

		// salvataggio delle aree prospettate in dettglio per verificare se sono
		// state effettuate modifiche;

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
			areaDatiPass.setVariazioneTuttiComp(false);
			areaDatiPass.setVariazioneCompAntico(false);
			areaDatiPass.setVariazioneNotaAntico(false);

			if (dettaglioTitoloForm.getTipoProspettazione().equals("AGGNOTA")) {
				if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloPFissaVO().getAreaTitNote()
						.equals(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitNote())) {
					areaDatiPass.setVariazioneCompAntico(false);
					areaDatiPass.setVariazioneNotaAntico(false);
				} else {
					areaDatiPass.setVariazioneCompAntico(false);
					areaDatiPass.setVariazioneNotaAntico(true);
				}
			} else {
				boolean test = dettaglioTitoloForm.getDettTitComVOOLD().equals(dettaglioTitoloForm.getDettTitComVO());
				if (!test) {
					areaDatiPass.setVariazioneCompAntico(true);
				}
			}
		} else {
			boolean test = dettaglioTitoloForm.getDettTitComVOOLD().equals(dettaglioTitoloForm.getDettTitComVO());
			areaDatiPass.setVariazioneCompAntico(false);
			areaDatiPass.setVariazioneNotaAntico(false);
			if (!test) {
				areaDatiPass.setVariazioneTuttiComp(true);
			} else {
				if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloPFissaVO().getAreaTitNote() != null) {
					if (dettaglioTitoloForm.getDettTitComVOOLD().getDetTitoloPFissaVO().getAreaTitNote()
							.equals(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitNote())) {
						areaDatiPass.setVariazioneTuttiComp(false);
					} else {
						areaDatiPass.setVariazioneTuttiComp(true);
					}
				}
			}

			// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo serve per gestire le abilitazioni alla sola parte
			// generale e bloccare la variazione dell'area delle specificita
			//boolean testTitBase = dettaglioTitoloForm.getDettTitComVOOLD().equalsTitBase(dettaglioTitoloForm.getDettTitComVO());
			boolean testSpecCar = dettaglioTitoloForm.getDettTitComVOOLD().equalsSpecCar(dettaglioTitoloForm.getDettTitComVO());
			boolean testSpecGra = dettaglioTitoloForm.getDettTitComVOOLD().equalsSpecGra(dettaglioTitoloForm.getDettTitComVO());
			boolean testSpecMus = dettaglioTitoloForm.getDettTitComVOOLD().equalsSpecMus(dettaglioTitoloForm.getDettTitComVO());
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			boolean testSpecAud = dettaglioTitoloForm.getDettTitComVOOLD().equalsSpecAud(dettaglioTitoloForm.getDettTitComVO());
			boolean testSpecEle = dettaglioTitoloForm.getDettTitComVOOLD().equalsSpecEle(dettaglioTitoloForm.getDettTitComVO());


			// intervento settembre 2015: nel caso di materiale H ma abilitazione a U si deve controllare solo quel tipo materiale
			if (dettaglioTitoloForm.getTipoMateriale().toString().equals("H")) {
				if ( !testSpecAud) {
					try{
						// Inizio Modifica almaviva2 per BUG MANTIS 3973 (nota contardi 11-05-10 13:16)
						// per i controlli di abilitazione si deve verificare che si sia in modifica e non in creazione
						if (areaDatiPass.isModifica()) {
							utenteEjb.checkAttivitaMat(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023, dettaglioTitoloForm.getTipoMateriale());
						}
						// Fine Modifica almaviva2 per BUG MANTIS 3973 (nota contardi 11-05-10 13:16)

						// Inizio modifica 26.07.2011 almaviva2
						// nel caso di tipo materiale non presente il controllo su tipoMateriale non deve essere effettuato
						if (dettaglioTitoloForm.getTipoMateriale() != null && !dettaglioTitoloForm.getTipoMateriale().equals("")) {
							utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getTipoMateriale());
						}

					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				} else if ( !testSpecMus) {
					try{
						// Inizio Modifica almaviva2 per BUG MANTIS 3973 (nota contardi 11-05-10 13:16)
						// per i controlli di abilitazione si deve verificare che si sia in modifica e non in creazione
						if (areaDatiPass.isModifica()) {
							utenteEjb.checkAttivitaMat(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023, "U");
						}
						// Fine Modifica almaviva2 per BUG MANTIS 3973 (nota contardi 11-05-10 13:16)

						// Inizio modifica 26.07.2011 almaviva2
						// nel caso di tipo materiale non presente il controllo su tipoMateriale non deve essere effettuato
						if (dettaglioTitoloForm.getTipoMateriale() != null && !dettaglioTitoloForm.getTipoMateriale().equals("")) {
							utenteEjb.isAbilitatoTipoMateriale("U");
						}

					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
			} else {
				if (!testSpecCar || !testSpecGra || !testSpecMus|| !testSpecAud|| !testSpecEle) {
					try{
						// Inizio Modifica almaviva2 per BUG MANTIS 3973 (nota contardi 11-05-10 13:16)
						// per i controlli di abilitazione si deve verificare che si sia in modifica e non in creazione
						if (areaDatiPass.isModifica()) {
							utenteEjb.checkAttivitaMat(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023, dettaglioTitoloForm.getTipoMateriale());
						}
						// Fine Modifica almaviva2 per BUG MANTIS 3973 (nota contardi 11-05-10 13:16)

						// Inizio modifica 26.07.2011 almaviva2
						// nel caso di tipo materiale non presente il controllo su tipoMateriale non deve essere effettuato
						if (dettaglioTitoloForm.getTipoMateriale() != null && !dettaglioTitoloForm.getTipoMateriale().equals("")) {
							utenteEjb.isAbilitatoTipoMateriale(dettaglioTitoloForm.getTipoMateriale());
						}

					}catch(UtenteNotAuthorizedException ute)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
			}

			// Fine modifica BUG MANTIS 3551

		}

		if (areaDatiPass.isVariazioneCompAntico() == false
				&& areaDatiPass.isVariazioneNotaAntico() == false
				&& areaDatiPass.isVariazioneTuttiComp() == false) {
					if (!dettaglioTitoloForm.getNatura().equals(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura())) {
						// siamo nel caso di cambio natura si deve fare il controllo sull'abilitazione a questa attività
						try{
						if (dettaglioTitoloForm.getNatura().equals("C")
								|| dettaglioTitoloForm.getNatura().equals("M")
								|| dettaglioTitoloForm.getNatura().equals("S")
								|| dettaglioTitoloForm.getNatura().equals("W")
								|| dettaglioTitoloForm.getNatura().equals("N")){
							utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_NATURA_DOCUMENTO_1021);
						} else {
							utenteEjb.checkAttivita(CodiciAttivita.getIstance().MODIFICA_NATURA_TITOLO_DI_ACCESSO_TITOLO_UNIFORME_1022);
						}
						}catch(UtenteNotAuthorizedException ute)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
							this.saveErrors(request, errors);
							resetToken(request);
							return mapping.getInputForward();
						}

						areaDatiPass.setNaturaTitoloDaVariare(areaDatiPass.getDetTitoloPFissaVO().getNatura());
						areaDatiPass.setVariazioneTuttiComp(true);
					}
		}



		areaDatiPass.setFlagCondiviso(dettaglioTitoloForm.isFlagCondiviso());
		if (dettaglioTitoloForm.isFlagCondiviso()) {
			areaDatiPass.setInserimentoIndice(true);
			areaDatiPass.setInserimentoPolo(false);
		} else {
			areaDatiPass.setInserimentoIndice(false);
			areaDatiPass.setInserimentoPolo(true);
		}

		//===================================================================================================
		if (dettaglioTitoloForm.getLegame51() != null) {
			areaDatiPass.setPropagaLocMadre(false);
			if (dettaglioTitoloForm.getLegame51().equals("SI")) {
				areaDatiPass.setLegameInf(true);
				// evolutive ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata
				// viene estesa alla N nuova la localizzazione per possesso della madre
				areaDatiPass.setPropagaLocMadre(false);
				if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M") &&
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N"))  {
					areaDatiPass.setPropagaLocMadre(true);
				}


				areaDatiPass.setBidArrivo(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getBidPartenza());
				areaDatiPass.setTipoLegame("51");

				// Inizio Modifica almaviva2 01.12.2010 BUG MANTIS 4023 verifica che la nota al legame non superi gli 80 caratteri
				// (DettaglioTitoloAction - confermaOper)
				if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
					if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
				// Fine Modifica almaviva2 01.12.2010 BUG MANTIS 4023




				areaDatiPass.setNoteLegame(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNoteLegameNew());
				areaDatiPass.setSequenza(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getSequenzaNew());
				//	Inizio intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
				areaDatiPass.setSici(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getSiciNew());
				//	Fine intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
				if (!dettaglioTitoloForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
					areaDatiPass.setFlagCondiviso(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza());
					areaDatiPass.setInserimentoIndice(false);
					areaDatiPass.setInserimentoPolo(true);
				}
			}
		}
		//===================================================================================================


		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		if (dettaglioTitoloForm.getLegameTitUniRinvio() != null) {
			if (dettaglioTitoloForm.getLegameTitUniRinvio().equals("SI")) {
				areaDatiPass.setLegameInf(true);

				areaDatiPass.setBidArrivo(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getBidPartenza());
				areaDatiPass.setTipoLegame("08");

				// Inizio Modifica almaviva2 01.12.2010 BUG MANTIS 4023 verifica che la nota al legame non superi gli 80 caratteri
				// (DettaglioTitoloAction - confermaOper)
				if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
					if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
						ActionMessages errors = new ActionMessages();
						errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
						this.saveErrors(request, errors);
						resetToken(request);
						return mapping.getInputForward();
					}
				}
				// Fine Modifica almaviva2 01.12.2010 BUG MANTIS 4023

				areaDatiPass.setNoteLegame(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNoteLegameNew());
				if (!dettaglioTitoloForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
					areaDatiPass.setFlagCondiviso(dettaglioTitoloForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza());
					areaDatiPass.setInserimentoIndice(false);
					areaDatiPass.setInserimentoPolo(true);
				}
			}
		}



		try {
			areaDatiPassReturn = factory.getGestioneBibliografica()
					.inserisciTitolo(areaDatiPass, Navigation.getInstance(request).getUserTicket());

		} catch (AuthenticationException ae ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>"	+ ae.getMessage() + ae.toString()));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");

		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>"	+ e.getMessage() + e.toString()));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}

		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			if (areaDatiPassReturn.getNumNotizie() > 0) {
				AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassSintetica = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
				areaDatiPassSintetica.setListaSimili(true);
				areaDatiPassSintetica.setIdLista(areaDatiPassReturn.getIdLista());
				areaDatiPassSintetica.setListaSintetica(areaDatiPassReturn.getListaSintetica());
				areaDatiPassSintetica.setMaxRighe(areaDatiPassReturn.getMaxRighe());
				areaDatiPassSintetica.setNumBlocco(areaDatiPassReturn.getNumBlocco());
				areaDatiPassSintetica.setNumNotizie(areaDatiPassReturn.getNumNotizie());
				areaDatiPassSintetica.setNumPrimo(areaDatiPassReturn.getNumPrimo());
				areaDatiPassSintetica.setTotBlocchi(areaDatiPassReturn.getTotBlocchi());
				areaDatiPassSintetica.setTotRighe(areaDatiPassReturn.getTotRighe());
				areaDatiPassSintetica.setLivelloTrovato(areaDatiPassReturn.getLivelloTrovato());
				request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassSintetica);

				areaDatiPass.setBidTemporaneo(areaDatiPassReturn.getBidTemporaneo());
				request.setAttribute("areaDatiPassPerConfVariazione", areaDatiPass);


				//===================================================================
				// Caso di simili in fase di creazione di un legame a titolo
				// prospetto l'elenco dei simili ma imposto le aree per il legame
				if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO() != null) {
					if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getBidPartenza() != null) {
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura());

						if (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("A")) {
							if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
								dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("UM");
							} else {
								dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("TU");
							}
							dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("");
						}
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setDescArrivo(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(dettaglioTitoloForm.isFlagCondiviso());
						request.setAttribute("AreaDatiLegameTitoloVO", dettaglioTitoloForm.getAreaDatiLegameTitoloVO());
					}
				}
				//===================================================================

				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaSimili"));
			}
			if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO() == null
					|| dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null) {
				if (dettaglioTitoloForm.getBidPerRientroAnalitica() != null) {
					request.setAttribute("bid", dettaglioTitoloForm.getBidPerRientroAnalitica());
				} else {
					request.setAttribute("bid", areaDatiPassReturn.getBid());
				}

				if (dettaglioTitoloForm.isFlagCondiviso()) {
					request.setAttribute("livRicerca", "I");
				} else {
					request.setAttribute("livRicerca", "P");
				}
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();

			    // Inizio almaviva2 03.08.2010 - Nuova Gestione messaggistica per modifiche riportate dal software di Indice
//				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.operOk"));
				if (areaDatiPassReturn.getTestoProtocollo() == null) {
					errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.operOk"));
				} else {
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOkConParametro", areaDatiPassReturn.getTestoProtocollo()));
				}

				// Fine almaviva2 03.08.2010
				this.saveErrors(request, errors);
				Navigation.getInstance(request).purgeThis();
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			} else if (dettaglioTitoloForm.getLegame51().equals("SI") ||
					dettaglioTitoloForm.getLegameTitUniRinvio().equals("SI") ) {
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
				//request.setAttribute("bid", areaDatiPassReturn.getBid());
				request.setAttribute("bid", dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getBidPartenza());
				if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("S")
						&& areaDatiPassReturn.getBid() != null) {
					request.setAttribute("bid", areaDatiPassReturn.getBid());
				}
				if (dettaglioTitoloForm.isFlagCondiviso()) {
					request.setAttribute("livRicerca", "I");
				} else {
					request.setAttribute("livRicerca", "P");
				}
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(	"errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).purgeThis();
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			} else {
				dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setIdArrivo(areaDatiPassReturn.getBid());
				dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setNaturaBidArrivo(
								areaDatiPass.getDetTitoloPFissaVO().getNatura());
				if (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("A")) {
					if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("UM");
					} else {
						dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("TU");
					}
				} else {
					dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("");
				}
				dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setDescArrivo(areaDatiPass.getDetTitoloPFissaVO().getAreaTitTitolo());

				dettaglioTitoloForm.getAreaDatiLegameTitoloVO().setFlagCondivisoArrivo(dettaglioTitoloForm.isFlagCondiviso());

				request.setAttribute("AreaDatiLegameTitoloVO",	dettaglioTitoloForm.getAreaDatiLegameTitoloVO());

				return mapping.findForward("gestioneLegameTitolo");
			}
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",	areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			if (areaDatiPassReturn.getCodErr().equals("disalPoloIndice")) {
				request.setAttribute("vaiA", "SI");
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.disalPoloIndice"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			}
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward insNumStandard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().addListaNumStandard(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canNumStandard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioNumStandard() == null
				|| dettaglioTitoloForm.getSelezRadioNumStandard().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblNumStandard"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioNumStandard());
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward insImpronta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().addListaImpronte(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canImpronta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioImpronta() == null
				|| dettaglioTitoloForm.getSelezRadioImpronta().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblImpronta"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioImpronta());
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward insIncipit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		request.setAttribute("tipoProspettazione", "INS");
		request.setAttribute("progrDettaglioIncipit", 999);
		request.setAttribute("idOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("descOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.findForward("inserVariaIncipit");
	}


	public ActionForward hlpElementiOrganicoSintetico(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		request.setAttribute("tipoProspettazione", "INS");
		request.setAttribute("tipoOrganico", "SINTETICO");
		request.setAttribute("idOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("descOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.findForward("hlpElementiOrganico");
	}

	public ActionForward hlpElementiOrganicoAnalitico(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		request.setAttribute("tipoProspettazione", "INS");
		request.setAttribute("tipoOrganico", "ANALITICO");
		request.setAttribute("idOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("descOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.findForward("hlpElementiOrganico");
	}

	public ActionForward hlpElementiOrganicoSinteticoComp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		request.setAttribute("tipoProspettazione", "INS");
		request.setAttribute("tipoOrganico", "SINTETICO-COMP");
		request.setAttribute("idOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("descOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.findForward("hlpElementiOrganico");
	}

	public ActionForward hlpElementiOrganicoAnaliticoComp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		request.setAttribute("tipoProspettazione", "INS");
		request.setAttribute("tipoOrganico", "ANALITICO-COMP");
		request.setAttribute("idOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("descOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.findForward("hlpElementiOrganico");
	}


	public ActionForward modIncipit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioIncipit() == null
				|| dettaglioTitoloForm.getSelezRadioIncipit().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblIncipit"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		request.setAttribute("tipoProspettazione", "AGG");
		request.setAttribute("idOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getBid());
		request.setAttribute("descOggColl", dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo());
		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioIncipit());
		request.setAttribute("dettaglioIncipit", dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaDettagliIncipit().get(index));
		request.setAttribute("progrDettaglioIncipit", index);


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.findForward("inserVariaIncipit");
	}

	public ActionForward canIncipit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioIncipit() == null
				|| dettaglioTitoloForm.getSelezRadioIncipit().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblIncipit"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioIncipit());
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaIncipit().remove(index);

		// Inizio modifica almaviva2 03.05.2010 - BUG MANTIS 3729 inserita cancellazione anche della tabella dettaglio prima non variata
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaDettagliIncipit().remove(index);
		// FIne modifica almaviva2 03.05.2010 - BUG MANTIS 3729 inserita cancellazione anche della tabella dettaglio prima non variata

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}


	public ActionForward insPersonaggio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().addListaPersonaggi(new TabellaNumSTDImpronteVO());
		} else {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().addListaPersonaggi(new TabellaNumSTDImpronteVO());
		}


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canPersonaggio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioPersonaggio() == null
				|| dettaglioTitoloForm.getSelezRadioPersonaggio().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblPersonaggio"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioPersonaggio());

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("M")
				|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().remove(index);
		} else  {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().remove(index);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}



	public ActionForward insRigaVuotaRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		dettaglioTitoloForm.addListaRepertoriModificato(new TabellaNumSTDImpronteVO());

		List<TabellaNumSTDImpronteVO> lista = new ArrayList<TabellaNumSTDImpronteVO>();
		for (int i=0; i<dettaglioTitoloForm.getListaRepertoriModificato().size(); i++) {
			TabellaNumSTDImpronteVO tabRepert = dettaglioTitoloForm.getListaRepertoriModificato().get(i);
			if (tabRepert.getCampoDue() == null || tabRepert.getCampoDue().equals("")) {
				tabRepert.setCampoUno("Si");
			}
			lista.add(tabRepert);
		}
		dettaglioTitoloForm.setListaRepertoriModificato(lista);
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	private void insNuovoRepertorio(HttpServletRequest request, ActionForm form) {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		List<TabellaNumSTDImpronteVO> lista = dettaglioTitoloForm.getListaRepertoriModificato();

		for (int i=0; i<lista.size(); i++) {
			TabellaNumSTDImpronteVO tabRepert = lista.get(i);
			if (tabRepert.getCampoDue() == null || tabRepert.getCampoDue().equals("")) {
				tabRepert.setCampoDue((String) request.getAttribute("sigl"));
				break;
			}
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return;
	}

	public ActionForward hlpRepertorio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("cercaRepertori");
	}


	public ActionForward canRepertorio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioRepertori() == null
				|| dettaglioTitoloForm.getSelezRadioRepertori().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblRepertorio"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm
				.getSelezRadioRepertori());
		dettaglioTitoloForm.getListaRepertoriModificato().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}


	private void caricaDescrizioniSuggerimento(DettaglioTitoloForm dettaglioTitoloForm)
	throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setDescNatura(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura(),
						CodiciType.CODICE_NATURA_BIBLIOGRAFICA,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));

		dettaglioTitoloForm.setDescTipoMat(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat(),
						CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));

		dettaglioTitoloForm.setDescTipoRec(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec(),
						CodiciType.CODICE_GENERE_MATERIALE_UNIMARC,
				CodiciRicercaType.RICERCA_CODICE_UNIMARC));


	}

	private void caricaDescrizioni(DettaglioTitoloForm dettaglioTitoloForm)
			throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setDescNatura(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura(),
						CodiciType.CODICE_NATURA_BIBLIOGRAFICA,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		dettaglioTitoloForm.setDescTipoMat(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat(),
						CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLivAut() != null) {
			dettaglioTitoloForm.setDescLivAut(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLivAut("");
		}

		dettaglioTitoloForm.setDescTipoRec(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec(),
						CodiciType.CODICE_GENERE_MATERIALE_UNIMARC,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLingua1() != null) {
			dettaglioTitoloForm.setDescLingua1(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLingua1(),
							CodiciType.CODICE_LINGUA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLingua1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLingua2() != null) {
			dettaglioTitoloForm.setDescLingua2(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLingua2(),
							CodiciType.CODICE_LINGUA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLingua2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLingua3() != null) {
			dettaglioTitoloForm.setDescLingua3(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getLingua3(),
							CodiciType.CODICE_LINGUA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLingua3("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getPaese() != null) {
			dettaglioTitoloForm.setDescPaese(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getPaese(),
							CodiciType.CODICE_PAESE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescPaese("");
		}

        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoTestoLetterario() != null) {
			if (ValidazioneDati.equals(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat(), "E")
					|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoTestoLetterario().length() == 2) {
				dettaglioTitoloForm.setDescTipoTestoLetterarioArea0(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoTestoLetterario(),
						CodiciType.CODICE_CONTENUTO_LETTERARIO_ANTICO,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));
			} else {
				dettaglioTitoloForm.setDescTipoTestoLetterarioArea0(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoTestoLetterario(),
						CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));
			}
		} else {
			dettaglioTitoloForm.setDescTipoTestoLetterarioArea0("");
		}

		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 13) Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
		//	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni -->
		dettaglioTitoloForm.setDescTipoTestoRegSonora("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoTestoRegSonora() != null) {
			if (ValidazioneDati.equals(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec(), "i")) {
				dettaglioTitoloForm.setDescTipoTestoRegSonora(factory.getCodici().cercaDescrizioneCodice(
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoTestoRegSonora(),
						CodiciType.CODICE_INDICATORE_TESTO_LETTERARIO,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));
			}
		}


		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getFormaContenuto() != null) {
			dettaglioTitoloForm.setDescFormaContenuto(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getFormaContenuto(),
							CodiciType.CODICE_FORMA_DEL_CONTENUTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaContenuto("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getFormaContenutoBIS() != null) {
			dettaglioTitoloForm.setDescFormaContenutoBIS(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getFormaContenutoBIS(),
							CodiciType.CODICE_FORMA_DEL_CONTENUTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaContenutoBIS("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoContenuto() != null) {
			dettaglioTitoloForm.setDescTipoContenuto(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoContenuto(),
							CodiciType.CODICE_TIPO_CONTENUTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoContenuto("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoContenutoBIS() != null) {
			dettaglioTitoloForm.setDescTipoContenutoBIS(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoContenutoBIS(),
							CodiciType.CODICE_TIPO_CONTENUTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoContenutoBIS("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getMovimento() != null) {
			dettaglioTitoloForm.setDescMovimento(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getMovimento(),
							CodiciType.CODICE_MOVIMENTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescMovimento("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getMovimentoBIS() != null) {
			dettaglioTitoloForm.setDescMovimentoBIS(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getMovimentoBIS(),
							CodiciType.CODICE_MOVIMENTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescMovimentoBIS("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getDimensione() != null) {
			dettaglioTitoloForm.setDescDimensione(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getDimensione(),
							CodiciType.CODICE_DIMENSIONALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescDimensione("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getDimensioneBIS() != null) {
			dettaglioTitoloForm.setDescDimensioneBIS(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getDimensioneBIS(),
							CodiciType.CODICE_DIMENSIONALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescDimensioneBIS("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialita1() != null) {
			dettaglioTitoloForm.setDescSensorialita1(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialita1(),
							CodiciType.CODICE_SENSORIALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSensorialita1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialitaBIS1() != null) {
			dettaglioTitoloForm.setDescSensorialitaBIS1(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialitaBIS1(),
							CodiciType.CODICE_SENSORIALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSensorialitaBIS1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialita2() != null) {
			dettaglioTitoloForm.setDescSensorialita2(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialita2(),
							CodiciType.CODICE_SENSORIALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSensorialita2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialitaBIS2() != null) {
			dettaglioTitoloForm.setDescSensorialitaBIS2(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialitaBIS2(),
							CodiciType.CODICE_SENSORIALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSensorialitaBIS2("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialita3() != null) {
			dettaglioTitoloForm.setDescSensorialita3(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialita3(),
							CodiciType.CODICE_SENSORIALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSensorialita3("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialitaBIS3() != null) {
			dettaglioTitoloForm.setDescSensorialitaBIS3(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getSensorialitaBIS3(),
							CodiciType.CODICE_SENSORIALITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSensorialitaBIS3("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMediazione() != null) {
			dettaglioTitoloForm.setDescTipoMediazione(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMediazione(),
							CodiciType.CODICE_TIPO_MEDIAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoMediazione("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMediazioneBIS() != null) {
			dettaglioTitoloForm.setDescTipoMediazioneBIS(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoMediazioneBIS(),
							CodiciType.CODICE_TIPO_MEDIAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoMediazioneBIS("");
		}
        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Fine definizione/Gestione Nuovi campi

		// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
		// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
		dettaglioTitoloForm.setDescPubblicatoSiNo("Pubblicato");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getPubblicatoSiNo() != null
			&& dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getPubblicatoSiNo().equals("1")) {
				dettaglioTitoloForm.setDescPubblicatoSiNo("Non pubblicato");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere1() != null) {
			dettaglioTitoloForm.setDescGenere1(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere1(),
							CodiciType.CODICE_GENERE_PUBBLICAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescGenere1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere2() != null) {
			dettaglioTitoloForm.setDescGenere2(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere2(),
							CodiciType.CODICE_GENERE_PUBBLICAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescGenere2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere3() != null) {
			dettaglioTitoloForm.setDescGenere3(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere3(),
							CodiciType.CODICE_GENERE_PUBBLICAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescGenere3("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere4() != null) {
			dettaglioTitoloForm.setDescGenere4(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getGenere4(),
							CodiciType.CODICE_GENERE_PUBBLICAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescGenere4("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoData() != null) {
			dettaglioTitoloForm.setDescTipoData(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoData(),
							CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
		} else {
			dettaglioTitoloForm.setDescTipoData("");
		}


//		Modifica almaviva2 05.03.2010 - BUG 3608 - inserito controllo per valore ""
		if (dettaglioTitoloForm.getDettTitComVO().getCampoSottoTipoLegame() != null
				&& !dettaglioTitoloForm.getDettTitComVO().getCampoSottoTipoLegame().equals("") ) {
			String appoSTLcod = dettaglioTitoloForm.getDettTitComVO().getCampoSottoTipoLegame();
			String appoSTLdes = factory.getCodici().cercaDescrizioneCodice(appoSTLcod,
						CodiciType.CODICE_LEGAME_TITOLO_MUSICA,	CodiciRicercaType.RICERCA_CODICE_SBN);
			dettaglioTitoloForm.setDescSottoTipoLegame(appoSTLdes);
			dettaglioTitoloForm.getDettTitComVO().setCampoSottoTipoLegame(appoSTLcod + " - " + appoSTLdes);
		} else {
			dettaglioTitoloForm.setDescSottoTipoLegame("");
		}


		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaImpron("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaNumSt("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaImpron("SI");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaNumSt("SI");
		}

		// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// al link dei documenti su Basi Esterne - Link verso base date digitali
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaLinkEsterni("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaLinkEsterni("SI");
		}

		// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// ai repertori cartacei - Riferimento a repertorio cartaceo
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaReperCartaceo("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaReperCartaceo().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setPresenzaReperCartaceo("SI");
		}

		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoSupporto() != null) {
			dettaglioTitoloForm.setDescTipoSupporto(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoSupporto(),
							CodiciType.CODICE_TIPO_SUPPORTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoSupporto("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoSupportoBIS() != null) {
			dettaglioTitoloForm.setDescTipoSupportoBIS(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoSupportoBIS(),
							CodiciType.CODICE_TIPO_SUPPORTO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoSupportoBIS("");
		}

	}

	private void caricaComboNatura(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		String tipoFiltro="";
		String naturaOrigine="";
		if (dettaglioTitoloForm.getLegame51() != null) {
			if (dettaglioTitoloForm.getLegame51().equals("SI")) {
				tipoFiltro="Titolo_infe";
			}
		}
		if (dettaglioTitoloForm.getSpoglio() != null) {
			if (dettaglioTitoloForm.getSpoglio().equals("SI")) {
				tipoFiltro="Titolo_spoglio";
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setNatura("N");
			}
		}
		
		// Inizio Modifica almaviva2 del 20.09.2019 BUG Mantis 7119
		// quando si proviene da creazione nuovo documento a seguito di un crea legame effettuato su un titolo
		// di natura A gli oggetti che è possibile creare sono solo D e P e A
		if (dettaglioTitoloForm.getTipoProspettazione().equals("INS") && 
				dettaglioTitoloForm.getAreaDatiLegameTitoloVO() != null) {
			if (dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() != null && 
					dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("A")) {
				tipoFiltro="Legami_su_A";
			}
		}
		// Fine Modifica almaviva2 del 20.09.2019 BUG Mantis 7119	
		
		
		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		if (dettaglioTitoloForm.getLegameTitUniRinvio() != null) {
			if (dettaglioTitoloForm.getLegameTitUniRinvio().equals("SI")) {
				tipoFiltro="Rinvio";
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setNatura("V");
			}
		}

		// Inizio Modificata gestione del cambio natura che viene fatta non in variazione ma
		// su voce esplicita di vai a almaviva2 5.10.2009 Mantis 3197
		if (dettaglioTitoloForm.getTipoProspettazione() != null) {
			if (dettaglioTitoloForm.getTipoProspettazione().equals("VARIANAT")) {
				tipoFiltro="Cambio_natura";
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("AGG")) {
				tipoFiltro="Solo_descr";
				naturaOrigine = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura();
			} else if (dettaglioTitoloForm.getTipoProspettazione().equals("INS")
					&& dettaglioTitoloForm.getAreaDatiLegameTitoloVO().getBidPartenza() == null) {
				// Modifica almaviva2 del 20.10.2010 BUG Mantis 3932
				// Inserito else if: tutte le creazioni che si possono attivare dal tasto Crea dell'interrogazione Titolo e Sintetiche Titolo
				// (quindi non attivate nell'ambito di una sessione di vai a) possono essere SOLO nature M, S, C
				// Modifica almaviva2 del 19.04.2012 - BUG MANTIS 4841 (collaudo) RACCOLTE FATTIZIE si creano solo in locale
//				tipoFiltro="Solo_docum";
				if (dettaglioTitoloForm.isFlagCondiviso()) {
					tipoFiltro="Solo_docum_condiviso";
				} else {
					tipoFiltro="Solo_docum_locale";
				}
				naturaOrigine = "";
			}

//			if (dettaglioTitoloForm.getTipoProspettazione().equals("AGG")
//					|| dettaglioTitoloForm.getTipoProspettazione().equals("VARIANAT")) {
//				if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura() != null) {
//					if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")
//							|| dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("B")) {
//						tipoFiltro="Cambio_natura";
//					} else {
//						tipoFiltro="Solo_descr";
//						naturaOrigine = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getNatura();
//					}
//				}
//			}
		}
		// Fine Modificata gestione del cambio natura che viene fatta non in variazione ma	
		
		
		dettaglioTitoloForm.setListaNatura(carCombo.loadComboCodiciDescNatura(factory
				.getCodici().getCodiceNaturaBibliografica(), tipoFiltro, naturaOrigine));
	}

	private void caricaComboTipoMat(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setListaTipoMat(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceMaterialeBibliografico()));

	}


	private void caricaComboGenerali(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setListaTipoMat(carCombo
				.loadComboCodiciDesc(factory.getCodici()
						.getCodiceMaterialeBibliografico()));

		dettaglioTitoloForm.setListaLivAut(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceLivelloAutorita()));
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO();
		if (detTitoloPFissaVO.getLivAut() != null) {
			dettaglioTitoloForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							detTitoloPFissaVO.getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLivAut("");
		}

		dettaglioTitoloForm.setListaTipoRec(carCombo
				.loadComboCodiciDesc(factory.getCodici()
						.getCodiceGenereMaterialeUnimarc()));
		dettaglioTitoloForm.setListaLingua(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceLingua()));
		if (detTitoloPFissaVO.getLingua() == null) {
			detTitoloPFissaVO.setLingua("");
		}
		if (detTitoloPFissaVO.getLingua1() == null) {
			detTitoloPFissaVO.setLingua1("");
		}
		if (detTitoloPFissaVO.getLingua2() == null) {
			detTitoloPFissaVO.setLingua2("");
		}
		if (detTitoloPFissaVO.getLingua3() == null) {
			detTitoloPFissaVO.setLingua3("");
		}

		dettaglioTitoloForm.setListaPaese(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodicePaese()));


        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
		//almaviva5_20140929 contenuto lett. distinto per moderno/antico

		// Inizio almaviva2 febbraio 2015: la scelta sulla tabella oltre che dal tipo materiale dipende anche dalla data selezionata;
		if (detTitoloPFissaVO.getTipoMat() == null
				|| detTitoloPFissaVO.getTipoMat().equals("")
				|| detTitoloPFissaVO.getTipoMat().equals(" ")) {
			// caso incerto su quale tabella caricare: si carica il moderno per Default
			dettaglioTitoloForm.setListaTipoTestoLetterarioArea0(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO)));
		} else {
			if (detTitoloPFissaVO.getTipoMat().equals("E")) {
				dettaglioTitoloForm.setListaTipoTestoLetterarioArea0(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONTENUTO_LETTERARIO_ANTICO)));
			} else if (detTitoloPFissaVO.getTipoMat().equals("M")) {
				dettaglioTitoloForm.setListaTipoTestoLetterarioArea0(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO)));
			} else {
				// 03.03.2015 almaviva2: inserito if sul null dalla data pubblicazione per determinare la tabella del testo letterario
				// da caricare (antico o moderno)
				if (detTitoloPFissaVO.getDataPubbl1() == null  || detTitoloPFissaVO.getDataPubbl1().equals("")) {
					// siamo nel caso non ancora definito dell'ANTICO: si carica il moderno per Default
					dettaglioTitoloForm.setListaTipoTestoLetterarioArea0(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO)));
				} else if (detTitoloPFissaVO.getDataPubbl1().compareTo("1831") < 0) {
					// siamo nel caso dell'ANTICO quindi usiamo la tabella opportuna
					dettaglioTitoloForm.setListaTipoTestoLetterarioArea0(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONTENUTO_LETTERARIO_ANTICO)));
				} else {
					// siamo nel caso del MODERNO quindi usiamo la tabella opportuna
					dettaglioTitoloForm.setListaTipoTestoLetterarioArea0(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO)));
				}

			}
		}
//		dettaglioTitoloForm
//				.setListaTipoTestoLetterarioArea0(carCombo
//						.loadComboCodiciDesc(CodiciProvider.getCodici(ValidazioneDati.equals(detTitoloPFissaVO.getTipoMat(), "E") ?
//												CodiciType.CODICE_CONTENUTO_LETTERARIO_ANTICO :
//												CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO)));
		// Fine almaviva2 febbraio 2015: la scelta sulla tabella oltre che dal tipo materiale dipende anche dalla data selezionata;


		dettaglioTitoloForm.setListaTipoTestoRegSonora(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_INDICATORE_TESTO_LETTERARIO)));

		dettaglioTitoloForm.setListaFormaContenuto(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_FORMA_DEL_CONTENUTO)));
		dettaglioTitoloForm.setListaTipoContenuto(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_CONTENUTO)));
		dettaglioTitoloForm.setListaMovimento(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_MOVIMENTO)));
		dettaglioTitoloForm.setListaDimensione(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_DIMENSIONALITA)));
		dettaglioTitoloForm.setListaSensorialita(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_SENSORIALITA)));

		// Modifica del 19.05.2015 almaviva2 (adeguamento all'Indice) per accettare il valore n del tipoMediazione (viene effettuato il
		// caricamento della combo MEDI solo con i valori validi; in questo modo quando la y sara' definitivamente sostituita dalla n
		// non verrà piu' caricata automaticamente perchè non sarà più valida;
		dettaglioTitoloForm.setListaTipoMediazione(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MEDIAZIONE, true)));


        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Fine definizione/Gestione Nuovi campi
		dettaglioTitoloForm.setListaGenere(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceGenerePubblicazione()));
		if (detTitoloPFissaVO.getGenere1() == null) {
			detTitoloPFissaVO.setGenere1("");
		}
		if (detTitoloPFissaVO.getGenere2() == null) {
			detTitoloPFissaVO.setGenere2("");
		}
		if (detTitoloPFissaVO.getGenere3() == null) {
			detTitoloPFissaVO.setGenere3("");
		}
		if (detTitoloPFissaVO.getGenere4() == null) {
			detTitoloPFissaVO.setGenere4("");
		}

		dettaglioTitoloForm.setListaTipoData(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoDataPubblicazione()));

		detTitoloPFissaVO.setPresenzaImpron("");
		detTitoloPFissaVO.setPresenzaNumSt("");
		if (detTitoloPFissaVO.getListaImpronte().size() > 0) {
			detTitoloPFissaVO.setPresenzaImpron("SI");
		}
		if (detTitoloPFissaVO.getListaNumStandard().size() > 0) {
			detTitoloPFissaVO.setPresenzaNumSt("SI");
		}

		dettaglioTitoloForm.setListaTipiNumStandard(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoNumeroStandard()));
		if (detTitoloPFissaVO.getNatura() == null) {
			detTitoloPFissaVO.setNatura("");
		}

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if (detTitoloPFissaVO.getNatura().equals("C")
				|| detTitoloPFissaVO.getNatura().equals("R")) {
			detTitoloPFissaVO.setTipoMat("");
		}


		// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
		// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
		// In creazione il default è REICAT.
		// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
		// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
//		if (detTitoloPFissaVO.getNorme() == null) {
//			detTitoloPFissaVO.setNorme("RICA");
//		}
		dettaglioTitoloForm.setListaNormaCatalografiche(carCombo.loadListaNormeCatalografiche());


		if (detTitoloPFissaVO.getFontePaese() == null) {
			detTitoloPFissaVO.setFontePaese("IT");
		}

		if (detTitoloPFissaVO.getFonteAgenzia() == null) {
			detTitoloPFissaVO.setFonteAgenzia("ICCU");
		}

		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		dettaglioTitoloForm.setListaTipoSupporto(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SUPPORTO)));

		// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// al link dei documenti su Basi Esterne - Link verso base date digitali
		detTitoloPFissaVO.setPresenzaLinkEsterni("");
		dettaglioTitoloForm.setListaLinkEsterni(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_LINK_ALTRA_BASE_DATI)));

		// Aprile 2016 EVOLUTIVA su link esterni; viene completato intervento affiche si crei automaticamente
		// la string link esterno da passare al protocollo che provvederà a validarla e solo se corretta ad inserirla
		// sulla base dati


		// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// ai repertori cartacei - Riferimento a repertorio cartaceo
		detTitoloPFissaVO.setPresenzaReperCartaceo("");

		//almaviva2 27.07.2017 adeguamento a Indice gestione 231
		dettaglioTitoloForm.setListaFormaOpera231(carCombo.loadComboCodiciDesc(factory.getCodici().getCodici(CodiciType.CODICE_FORMA_OPERA, true)));

		// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
		// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
		dettaglioTitoloForm.setListaPubblicatoSiNo(carCombo.loadPubblicatoSiNo());
	}

	private void caricaDescrizioniMusica(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getLivAutSpec() != null) {
			dettaglioTitoloForm.setDescLivAutSpecMus(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getLivAutSpec(),
							CodiciType.CODICE_LIVELLO_AUTORITA,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLivAutSpecMus("");
		}


		// Inizio almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906
		// correzione impostazione campo tipo elaborazione per renderlo visualizzabile sia in
		// dettaglio che in variazione (la tabella didecodifica viene fatta con il cod SBN)

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getElabor() != null) {
			dettaglioTitoloForm.setDescTipoElaborazione(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getElabor(),
							CodiciType.CODICE_ELABORAZIONE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
//							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescElaborazione("");
		}



		// Inizio almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		// Gestione anche del campo tipoElabor nel quale è presente un campo relativo all'Audiovisivo
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTipoElabor() != null) {
			dettaglioTitoloForm.setDescElaborazione(factory.getCodici().cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTipoElabor().toLowerCase(),
					CodiciType.CODICE_ELABORAZIONE,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoElaborazione("");
		}
		// Inizio almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro



		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getPresent() != null) {
			String codPresent = dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getPresent();
			dettaglioTitoloForm.setDescPresentazione(factory.getCodici().cercaDescrizioneCodice(codPresent,
							CodiciType.CODICE_PRESENTAZIONE, CodiciRicercaType.RICERCA_CODICE_SBN));
		} else {
			dettaglioTitoloForm.setDescPresentazione("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getGenereRappr() != null) {
			dettaglioTitoloForm.setDescGenereRappr(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getGenereRappr(),
							CodiciType.CODICE_GENERE_RAPPRESENTAZIONE,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescGenereRappr("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getStesura() != null) {
			dettaglioTitoloForm.setDescStesura(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getStesura(),
							CodiciType.CODICE_STESURA,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescStesura("");
		}

		// Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
		// perchè non è un testo libero (Materiale Musicale manoscritto)
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getMateria() != null) {
			dettaglioTitoloForm.setDescMateria(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getMateria(),
							CodiciType.CODICE_MATERIA,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescMateria("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getComposito() != null) {
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getComposito().equals("S")) {
				dettaglioTitoloForm.setDescComposito("SI");
			} else {
				dettaglioTitoloForm.setDescComposito("NO");
			}
		} else {
			dettaglioTitoloForm.setDescComposito("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getPalinsesto() != null) {
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getPalinsesto().equals("S")) {
					dettaglioTitoloForm.setDescPalinsesto("SI");
				} else {
					dettaglioTitoloForm.setDescPalinsesto("NO");
				}
		} else {
			dettaglioTitoloForm.setDescPalinsesto("");
		}

		dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaPersonaggi("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaIncipit("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaPersonaggi("SI");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaIncipit().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaIncipit("SI");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTonalita() != null) {
			dettaglioTitoloForm.setDescTonalita(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTonalita(),
							CodiciType.CODICE_TONALITA,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTonalita("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic1() != null) {
			dettaglioTitoloForm.setDescFormaMusicale1(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic1(),
							CodiciType.CODICE_FORMA_MUSICALE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaMusicale1("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic2() != null) {
			dettaglioTitoloForm.setDescFormaMusicale2(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic2(),
							CodiciType.CODICE_FORMA_MUSICALE,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaMusicale2("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic3() != null) {
			dettaglioTitoloForm.setDescFormaMusicale3(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic3(),
							CodiciType.CODICE_FORMA_MUSICALE,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaMusicale3("");
		}
	}

	private void caricaComboMusica(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setListaElaborazione(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceElaborazione()));
		dettaglioTitoloForm.setListaPresentazione(carCombo.loadComboCodiciDesc(factory.getCodici().getCodicePresentazione()));
		dettaglioTitoloForm.setListaGenereRappr(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceGenereRappresentazione()));

		dettaglioTitoloForm.setListaStesura(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceStesura()));

		// Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
		// perchè non è un testo libero (Materiale Musicale manoscritto)
		dettaglioTitoloForm.setListaMateria(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceMateria()));

		dettaglioTitoloForm.setListaComposito(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSiNo()));
		dettaglioTitoloForm.setListaPalinsesto(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSiNo()));

		dettaglioTitoloForm.setListaTimbroVocale(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceStrumentiVociOrganico()));

		dettaglioTitoloForm.setListaFormaMusicale(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceFormaMusicale()));
		dettaglioTitoloForm.setListaTonalita(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTonalita()));

		dettaglioTitoloForm.setListaTipoTestoLetterario(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoTestoLetterario()));

		// Inizio Intervento Febbraio 2014: il campo Voce/Strumento presente nell'incipit puo contenere anche il numero
        // di Voci/Strumento quindi deve essere gestita anche la parte numerica da inserire negli appositi campi di Db;
		// Inserito il controllo su presenza di personaggi/Incipit così che nel caso di Variazione venga inviato
		// all'Indice il corretto flag che poi consente la modifica
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaPersonaggi("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaIncipit("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaPersonaggi().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaPersonaggi("SI");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getListaIncipit().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().setPresenzaIncipit("SI");
		}


	}

	private void caricaComboModAnt(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setListaGenereRappr(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceGenereRappresentazione()));
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().setPresenzaPersonaggi("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().setPresenzaPersonaggi("SI");
		}
		dettaglioTitoloForm.setListaTimbroVocale(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceStrumentiVociOrganico()));
	}

	private void caricaDescrizioniModAnt(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getGenereRappr() != null) {
			dettaglioTitoloForm.setDescGenereRappr(factory.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getGenereRappr(),
							CodiciType.CODICE_GENERE_RAPPRESENTAZIONE,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescGenereRappr("");
		}

		dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().setPresenzaPersonaggi("");
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().getListaPersonaggi().size() > 0) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloModAntVO().setPresenzaPersonaggi("SI");
		}

	}


	private void caricaDescrizioniGrafica(
			DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getLivAutSpec() != null) {
			dettaglioTitoloForm.setDescLivAutSpecGra(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
							.getLivAutSpec(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLivAutSpecGra("");
		}


		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getDesignMat() != null) {
			dettaglioTitoloForm.setDescDesignMat(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getDesignMat(),
							CodiciType.CODICE_MATERIALE_GRAFICO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescDesignMat("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getSuppPrim() != null) {
			dettaglioTitoloForm.setDescSuppPrim(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getSuppPrim(),
							CodiciType.CODICE_SUPPORTO_FISICO_PER_GRAFICA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSuppPrim("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicCol() != null) {
			dettaglioTitoloForm.setDescIndicCol(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicCol(),
							CodiciType.CODICE_COLORE116,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicCol("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicTec1() != null) {
			dettaglioTitoloForm.setDescIndicTec1(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicTec1(),
							CodiciType.CODICE_TECNICA_DISEGNI,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicTec1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicTec2() != null) {
			dettaglioTitoloForm.setDescIndicTec2(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicTec2(),
							CodiciType.CODICE_TECNICA_DISEGNI,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicTec2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicTec3() != null) {
			dettaglioTitoloForm.setDescIndicTec3(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicTec3(),
							CodiciType.CODICE_TECNICA_DISEGNI,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicTec3("");
		}


		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicTecSta1() != null) {
			dettaglioTitoloForm.setDescIndicTecSta1(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicTecSta1(),
							CodiciType.CODICE_TECNICA_STAMPE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicTecSta1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicTecSta2() != null) {
			dettaglioTitoloForm.setDescIndicTecSta2(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicTecSta2(),
							CodiciType.CODICE_TECNICA_STAMPE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicTecSta2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getIndicTecSta3() != null) {
			dettaglioTitoloForm.setDescIndicTecSta3(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getIndicTecSta3(),
							CodiciType.CODICE_TECNICA_STAMPE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicTecSta3("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO()
				.getDesignFun() != null) {
			dettaglioTitoloForm.setDescDesignFun(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloGraVO().getDesignFun(),
							CodiciType.CODICE_DESIGNAZIONE_FUNZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescDesignFun("");
		}
	}

	private void caricaComboGrafica(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setListaDesignMat(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceMaterialeGrafico()));
		dettaglioTitoloForm.setListaSuppPrim(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSupportoFisicoGrafica()));
		dettaglioTitoloForm.setListaIndicCol(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceColore116()));

		dettaglioTitoloForm.setListaIndicTec(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTecnicaDisegni()));
		dettaglioTitoloForm.setListaIndicTecSta(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTecnicaStampe()));


		dettaglioTitoloForm.setListaDesignFun(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceDesignazioneFunzione()));

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getIndicTec1() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setIndicTec1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getIndicTec2() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setIndicTec2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getIndicTec3() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setIndicTec3("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getIndicTecSta1() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setIndicTecSta1("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getIndicTecSta2() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setIndicTecSta2("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().getIndicTecSta3() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloGraVO().setIndicTecSta3("");
		}
	}


	private void caricaComboAudiovisivo(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null &&
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("g")) {
			dettaglioTitoloForm.setListaTipoVideo(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_VIDEO)));
			dettaglioTitoloForm.setListaIndicatoreColore(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_INDICATORE_COLORE)));
			dettaglioTitoloForm.setListaIndicatoreSuono(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_INDICATORE_SUONO)));
			dettaglioTitoloForm.setListaSupportoSuono(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_DEL_SUONO)));
			dettaglioTitoloForm.setListaLarghezzaDimensioni(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_LARGHEZZA_O_DIMENSIONI)));
			dettaglioTitoloForm.setListaFormaPubblDistr(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE_DISTRIBUZIONE)));
			dettaglioTitoloForm.setListaTecnicaVideoFilm(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TECNICA_VIDEOREGISTRAZIONI_E_FILM)));
			dettaglioTitoloForm.setListaPresentImmagMov(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PRESENTAZIONE_IMMAGINI_IN_MOVIMENTO)));
			dettaglioTitoloForm.setListaMaterAccompagn(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_MATERIALE_ACCOMPAGNAMENTO)));
			dettaglioTitoloForm.setListaPubblicVideoreg(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE_VIDEOREGISTRAZIONI)));
			dettaglioTitoloForm.setListaPresentVideoreg(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PRESENTAZIONE_VIDEOREGISTRAZIONI)));
			dettaglioTitoloForm.setListaMaterialeEmulsBase(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_MATERIALE_EMULSIONE_BASE)));
			dettaglioTitoloForm.setListaMaterialeSupportSec(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_MATERIALE_SUPPORTO_SECONDARIO)));
			dettaglioTitoloForm.setListaStandardTrasmiss(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_STANDARD_TRASMISSIONE)));
			dettaglioTitoloForm.setListaVersioneAudiovid(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_VERSIONE)));
			dettaglioTitoloForm.setListaElementiProd(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_ELEMENTI_DELLA_PRODUZIONE)));
			dettaglioTitoloForm.setListaSpecCatColoreFilm(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_SPECIFICHE_CATEGORIE_COLORE)));
			dettaglioTitoloForm.setListaEmulsionePellic(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_EMULSIONE_DELLA_PELLICOLA)));
			dettaglioTitoloForm.setListaComposPellic(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_COMPOSIZIONE_DELLA_PELLICOLA)));
			dettaglioTitoloForm.setListaSuonoImmagMovimento(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SUONO_PER_IMMAGINI_IN_MOVIMENTO)));
			dettaglioTitoloForm.setListaTipoPellicStampa(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_PELLICOLA_O_STAMPA)));
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null &&
				(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("i") ||
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("j"))) {
			dettaglioTitoloForm.setListaFormaPubblicazioneDisco(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE_AUDIOVIDEO)));
			dettaglioTitoloForm.setListaVelocita(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_VELOCITA)));
			dettaglioTitoloForm.setListaTipoSuono(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SUONO)));
			dettaglioTitoloForm.setListaLarghezzaScanal(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_LARGHEZZA_DELLA_SCANALATURA)));

			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 2)	Manca la tabella DINA; al suo posto, nel campo ‘Dimensioni’ (126$a4) del tipo record ‘i, j’ si aprono i valori
			// della tabella errata DIME.
//			dettaglioTitoloForm.setListaDimensioni(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_DIMENSIONE)));
			dettaglioTitoloForm.setListaDimensioni(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_DIMENSIONE_DEL_NASTRO)));
			// Fine Modifica gennaio 2015
			dettaglioTitoloForm.setListaLarghezzaNastro(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_LARGHEZZA_DEL_NASTRO)));
			dettaglioTitoloForm.setListaConfigurazNastro(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_CONFIGURAZIONE_DEL_NASTRO)));
			dettaglioTitoloForm.setListaMaterTestAccompagn(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO)));
			dettaglioTitoloForm.setListaTecnicaRegistraz(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TECNICA_REGISTRAZIONE)));
			dettaglioTitoloForm.setListaSpecCarattRiprod(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_SPECIALI_CARATTERISTICHE_RIPRODUZIONE)));
			dettaglioTitoloForm.setListaDatiCodifRegistrazSonore(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DISCO)));
			dettaglioTitoloForm.setListaTipoDiMateriale(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MATERIALE)));
			dettaglioTitoloForm.setListaTipoDiTaglio(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_TAGLIO)));
		}
		dettaglioTitoloForm.setNumSupporti("1");
	}

	private void caricaDescrizioniAudiovisivo(DettaglioTitoloForm dettaglioTitoloForm, String tipoDesc) throws Exception {


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CodiciRicercaType tipoRicerca = (tipoDesc.equals("ULT")) ?
				CodiciRicercaType.RICERCA_CODICE_UNIMARC_ULTERIORE :
					CodiciRicercaType.RICERCA_CODICE_UNIMARC;


		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLivAutSpec() != null) {

			dettaglioTitoloForm.setDescLivAutSpecAud(factory.getCodici()
					.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLivAutSpec(),
							CodiciType.CODICE_LIVELLO_AUTORITA,	tipoRicerca));
		} else {
			dettaglioTitoloForm.setDescLivAutSpecAud("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null &&
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("g")) {

			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoVideo() != null) {
				dettaglioTitoloForm.setDescTipoVideo(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoVideo(),
				CodiciType.CODICE_TIPO_VIDEO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTipoVideo("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getIndicatoreColore() != null) {
				dettaglioTitoloForm.setDescIndicatoreColore(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getIndicatoreColore(),
				CodiciType.CODICE_INDICATORE_COLORE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescIndicatoreColore("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getIndicatoreSuono() != null) {
				dettaglioTitoloForm.setDescIndicatoreSuono(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getIndicatoreSuono(),
				CodiciType.CODICE_INDICATORE_SUONO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescIndicatoreSuono("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSupportoSuono() != null) {
				dettaglioTitoloForm.setDescSupportoSuono(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSupportoSuono(),
				CodiciType.CODICE_SUPPORTO_DEL_SUONO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescSupportoSuono("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLarghezzaDimensioni() != null) {
				dettaglioTitoloForm.setDescLarghezzaDimensioni(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLarghezzaDimensioni(),
				CodiciType.CODICE_LARGHEZZA_O_DIMENSIONI, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescLarghezzaDimensioni("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getFormaPubblDistr() != null) {
				dettaglioTitoloForm.setDescFormaPubblDistr(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getFormaPubblDistr(),
				CodiciType.CODICE_FORMA_PUBBLICAZIONE_DISTRIBUZIONE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescFormaPubblDistr("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTecnicaVideoFilm() != null) {
				dettaglioTitoloForm.setDescTecnicaVideoFilm(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTecnicaVideoFilm(),
				CodiciType.CODICE_TECNICA_VIDEOREGISTRAZIONI_E_FILM, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTecnicaVideoFilm("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getPresentImmagMov() != null) {
				dettaglioTitoloForm.setDescPresentImmagMov(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getPresentImmagMov(),
				CodiciType.CODICE_FORMA_PRESENTAZIONE_IMMAGINI_IN_MOVIMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescPresentImmagMov("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn1() != null) {
				dettaglioTitoloForm.setDescMaterAccompagn1(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn1(),
				CodiciType.CODICE_MATERIALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterAccompagn1("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn2() != null) {
				dettaglioTitoloForm.setDescMaterAccompagn2(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn2(),
				CodiciType.CODICE_MATERIALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterAccompagn2("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn3() != null) {
				dettaglioTitoloForm.setDescMaterAccompagn3(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn3(),
				CodiciType.CODICE_MATERIALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterAccompagn3("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn4() != null) {
				dettaglioTitoloForm.setDescMaterAccompagn4(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterAccompagn4(),
				CodiciType.CODICE_MATERIALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterAccompagn4("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getPubblicVideoreg() != null) {
				dettaglioTitoloForm.setDescPubblicVideoreg(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getPubblicVideoreg(),
				CodiciType.CODICE_FORMA_PUBBLICAZIONE_VIDEOREGISTRAZIONI, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescPubblicVideoreg("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getPresentVideoreg() != null) {
				dettaglioTitoloForm.setDescPresentVideoreg(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getPresentVideoreg(),
				CodiciType.CODICE_FORMA_PRESENTAZIONE_VIDEOREGISTRAZIONI, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescPresentVideoreg("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterialeEmulsBase() != null) {
				dettaglioTitoloForm.setDescMaterialeEmulsBase(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterialeEmulsBase(),
				CodiciType.CODICE_MATERIALE_EMULSIONE_BASE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterialeEmulsBase("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterialeSupportSec() != null) {
				dettaglioTitoloForm.setDescMaterialeSupportSec(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterialeSupportSec(),
				CodiciType.CODICE_MATERIALE_SUPPORTO_SECONDARIO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterialeSupportSec("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getStandardTrasmiss() != null) {
				dettaglioTitoloForm.setDescStandardTrasmiss(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getStandardTrasmiss(),
				CodiciType.CODICE_STANDARD_TRASMISSIONE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescStandardTrasmiss("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getVersioneAudiovid() != null) {
				dettaglioTitoloForm.setDescVersioneAudiovid(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getVersioneAudiovid(),
				CodiciType.CODICE_VERSIONE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescVersioneAudiovid("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getElementiProd() != null) {
				dettaglioTitoloForm.setDescElementiProd(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getElementiProd(),
				CodiciType.CODICE_ELEMENTI_DELLA_PRODUZIONE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescElementiProd("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSpecCatColoreFilm() != null) {
				dettaglioTitoloForm.setDescSpecCatColoreFilm(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSpecCatColoreFilm(),
				CodiciType.CODICE_SPECIFICHE_CATEGORIE_COLORE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescSpecCatColoreFilm("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getEmulsionePellic() != null) {
				dettaglioTitoloForm.setDescEmulsionePellic(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getEmulsionePellic(),
				CodiciType.CODICE_EMULSIONE_DELLA_PELLICOLA, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescEmulsionePellic("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getComposPellic() != null) {
				dettaglioTitoloForm.setDescComposPellic(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getComposPellic(),
				CodiciType.CODICE_COMPOSIZIONE_DELLA_PELLICOLA, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescComposPellic("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSuonoImmagMovimento() != null) {
				dettaglioTitoloForm.setDescSuonoImmagMovimento(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSuonoImmagMovimento(),
				CodiciType.CODICE_TIPO_SUONO_PER_IMMAGINI_IN_MOVIMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescSuonoImmagMovimento("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoPellicStampa() != null) {
				dettaglioTitoloForm.setDescTipoPellicStampa(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoPellicStampa(),
				CodiciType.CODICE_TIPO_PELLICOLA_O_STAMPA, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTipoPellicStampa("");
			}
		} else if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null &&
				(dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("i") ||
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("j"))) {

			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getFormaPubblicazioneDisco() != null) {
				dettaglioTitoloForm.setDescFormaPubblicazioneDisco(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getFormaPubblicazioneDisco(),
				CodiciType.CODICE_FORMA_PUBBLICAZIONE_AUDIOVIDEO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescFormaPubblicazioneDisco("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getVelocita() != null) {
				dettaglioTitoloForm.setDescVelocita(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getVelocita(),
				CodiciType.CODICE_VELOCITA, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescVelocita("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoSuono() != null) {
				dettaglioTitoloForm.setDescTipoSuono(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoSuono(),
				CodiciType.CODICE_TIPO_SUONO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTipoSuono("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLarghezzaScanal() != null) {
				dettaglioTitoloForm.setDescLarghezzaScanal(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLarghezzaScanal(),
				CodiciType.CODICE_LARGHEZZA_DELLA_SCANALATURA, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescLarghezzaScanal("");
			}

			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 2)	Manca la tabella DINA; al suo posto, nel campo ‘Dimensioni’ (126$a4) del tipo record ‘i, j’ si aprono i valori
			// della tabella errata DIME.
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getDimensioni() != null) {
				dettaglioTitoloForm.setDescDimensioni(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getDimensioni(),
				CodiciType.CODICE_DIMENSIONE_DEL_NASTRO, tipoRicerca));
				// CodiciType.CODICE_DIMENSIONE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
			} else {
				dettaglioTitoloForm.setDescDimensioni("");
			}

			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLarghezzaNastro() != null) {
				dettaglioTitoloForm.setDescLarghezzaNastro(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getLarghezzaNastro(),
				CodiciType.CODICE_LARGHEZZA_DEL_NASTRO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescLarghezzaNastro("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getConfigurazNastro() != null) {
				dettaglioTitoloForm.setDescConfigurazNastro(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getConfigurazNastro(),
				CodiciType.CODICE_CONFIGURAZIONE_DEL_NASTRO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescConfigurazNastro("");
			}

			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn1() != null) {
				dettaglioTitoloForm.setDescMaterTestAccompagn1(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn1(),
				CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterTestAccompagn1("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn2() != null) {
				dettaglioTitoloForm.setDescMaterTestAccompagn2(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn2(),
				CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterTestAccompagn2("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn3() != null) {
				dettaglioTitoloForm.setDescMaterTestAccompagn3(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn3(),
				CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterTestAccompagn3("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn4() != null) {
				dettaglioTitoloForm.setDescMaterTestAccompagn4(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn4(),
				CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterTestAccompagn4("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn5() != null) {
				dettaglioTitoloForm.setDescMaterTestAccompagn5(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn5(),
				CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterTestAccompagn5("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn6() != null) {
				dettaglioTitoloForm.setDescMaterTestAccompagn6(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getMaterTestAccompagn6(),
				CodiciType.CODICE_MATERIALE_TESTUALE_ACCOMPAGNAMENTO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescMaterTestAccompagn6("");
			}

			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTecnicaRegistraz() != null) {
				dettaglioTitoloForm.setDescTecnicaRegistraz(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTecnicaRegistraz(),
				CodiciType.CODICE_TECNICA_REGISTRAZIONE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTecnicaRegistraz("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSpecCarattRiprod() != null) {
				dettaglioTitoloForm.setDescSpecCarattRiprod(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getSpecCarattRiprod(),
				CodiciType.CODICE_SPECIALI_CARATTERISTICHE_RIPRODUZIONE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescSpecCarattRiprod("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getDatiCodifRegistrazSonore() != null) {
				dettaglioTitoloForm.setDescDatiCodifRegistrazSonore(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getDatiCodifRegistrazSonore(),
				CodiciType.CODICE_TIPO_DISCO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescDatiCodifRegistrazSonore("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoDiMateriale() != null) {
				dettaglioTitoloForm.setDescTipoDiMateriale(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoDiMateriale(),
				CodiciType.CODICE_TIPO_MATERIALE, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTipoDiMateriale("");
			}
			if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoDiTaglio() != null) {
				dettaglioTitoloForm.setDescTipoDiTaglio(factory.getCodici()
				.cercaDescrizioneCodice(dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO().getTipoDiTaglio(),
				CodiciType.CODICE_TIPO_TAGLIO, tipoRicerca));
			} else {
				dettaglioTitoloForm.setDescTipoDiTaglio("");
			}
		}
	}

	// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
	// Gestione nuovi campi specifici per etichetta 135
	private void caricaComboElettronico(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec() != null
//				&&
//				dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals("l")
				) {
			dettaglioTitoloForm.setListaTipoRisorsaElettronica(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_RISORSA_ELETTRONICA)));
			dettaglioTitoloForm.setListaIndicazioneSpecificaMateriale(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_INDICAZIONE_SPECIFICA_MATERIALE)));
			dettaglioTitoloForm.setListaColoreElettronico(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_COLORE)));
			dettaglioTitoloForm.setListaDimensioniElettronico(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_DIMENSIONI)));
			dettaglioTitoloForm.setListaSuonoElettronico(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_SUONO)));
		}
	}


	private void caricaDescrizioniElettronico(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getLivAutSpec() != null) {
			dettaglioTitoloForm.setDescLivAutSpecEle(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO()
							.getLivAutSpec(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLivAutSpecEle("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getColoreElettronico() != null) {
			dettaglioTitoloForm.setDescColoreElettronico(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO()
							.getColoreElettronico(),
							CodiciType.CODICE_COLORE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescColoreElettronico("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getSuonoElettronico() != null) {
			dettaglioTitoloForm.setDescSuonoElettronico(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO()
							.getSuonoElettronico(),
							CodiciType.CODICE_SUONO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSuonoElettronico("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getDimensioniElettronico() != null) {
			dettaglioTitoloForm.setDescDimensioniElettronico(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO()
							.getDimensioniElettronico(),
							CodiciType.CODICE_DIMENSIONI,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescDimensioniElettronico("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getTipoRisorsaElettronica() != null) {
			dettaglioTitoloForm.setDescTipoRisorsaElettronica(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO()
							.getTipoRisorsaElettronica(),
							CodiciType.CODICE_TIPO_RISORSA_ELETTRONICA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoRisorsaElettronica("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO().getIndicazioneSpecificaMateriale() != null) {
			dettaglioTitoloForm.setDescIndicazioneSpecificaMateriale(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloEleVO()
							.getIndicazioneSpecificaMateriale(),
							CodiciType.CODICE_INDICAZIONE_SPECIFICA_MATERIALE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicazioneSpecificaMateriale("");
		}

	}



	private void caricaDescrizioniCartografia(
			DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLivAutSpec() != null) {
			dettaglioTitoloForm.setDescLivAutSpecCar(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO()
							.getLivAutSpec(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescLivAutSpecCar("");
		}


		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getPubblicazioneGovernativa() != null) {
			dettaglioTitoloForm.setDescPubblicazioneGovernativa(factory
					.getCodici().cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO()
									.getPubblicazioneGovernativa(),
							CodiciType.CODICE_PUBBLICAZIONE_GOVERNATIVA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescPubblicazioneGovernativa("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getIndicatoreColore() != null) {
			dettaglioTitoloForm.setDescIndicCol(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getIndicatoreColore(),
							CodiciType.CODICE_COLORE120,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicCol("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getSupportoFisico() != null) {
			dettaglioTitoloForm.setDescSupportoFisico(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getSupportoFisico(),
							CodiciType.CODICE_SUPPORTO_FISICO_PER_CARTOGRAFICO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescSupportoFisico("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getTecnicaCreazione() != null) {
			dettaglioTitoloForm.setDescTecnicaCreazione(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getTecnicaCreazione(),
							CodiciType.CODICE_TECNICA_CARTOGRAFIA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTecnicaCreazione("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO()
				.getFormaRiproduzione() != null) {
			dettaglioTitoloForm
					.setDescFormaRiproduzione(factory.getCodici()
							.cercaDescrizioneCodice(
									dettaglioTitoloForm.getDettTitComVO()
											.getDetTitoloCarVO()
											.getFormaRiproduzione(),
									CodiciType.CODICE_FORMA_RIPRODUZIONE,
									CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaRiproduzione("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getFormaPubblicazione() != null) {
			dettaglioTitoloForm.setDescFormaPubblicazione(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO()
									.getFormaPubblicazione(),
							CodiciType.CODICE_FORMA_PUBBLICAZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescFormaPubblicazione("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getAltitudine() != null) {
			dettaglioTitoloForm.setDescAltitudine(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getAltitudine(),
							CodiciType.CODICE_ALTITUDINE_SENSORE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescAltitudine("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getIndicatoreTipoScala() != null) {
			dettaglioTitoloForm.setDescIndicatoreTipoScala(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO()
									.getIndicatoreTipoScala(),
							CodiciType.CODICE_INDICATORE_TIPOSCALA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescIndicatoreTipoScala("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getTipoScala() != null) {
			dettaglioTitoloForm.setDescTipoScala(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getTipoScala(),
							CodiciType.CODICE_TIPOSCALA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescTipoScala("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getMeridianoOrigine() != null) {
			dettaglioTitoloForm.setDescMeridianoOrigine(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getMeridianoOrigine(),
							CodiciType.CODICE_MERIDIANO_ORIGINE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescMeridianoOrigine("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getCarattereImmagine() != null) {
			dettaglioTitoloForm
					.setDescCarattereImmagine(factory.getCodici()
							.cercaDescrizioneCodice(
									dettaglioTitoloForm.getDettTitComVO()
											.getDetTitoloCarVO()
											.getCarattereImmagine(),
									CodiciType.CODICE_CARATTERE_IMMAGINE,
									CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescCarattereImmagine("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO()
				.getForma() != null) {
			dettaglioTitoloForm.setDescForma(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getForma(),
							CodiciType.CODICE_FORMA_DOCUMENTO_CARTOGRAFICO,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescForma("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getPiattaforma() != null) {
			dettaglioTitoloForm.setDescPiattaforma(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO().getPiattaforma(),
							CodiciType.CODICE_PIATTAFORMA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescPiattaforma("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getCategoriaSatellite() != null) {
			dettaglioTitoloForm.setDescCategoriaSatellite(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO()
									.getCategoriaSatellite(),
							CodiciType.CODICE_CATEGORIA_SATELLITE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescCategoriaSatellite("");
		}

		// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getProiezioneCarte() != null) {
			dettaglioTitoloForm.setDescProiezioneCarte(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioTitoloForm.getDettTitComVO()
									.getDetTitoloCarVO()
									.getProiezioneCarte(),
							CodiciType.CODICE_PROIEZIONE,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioTitoloForm.setDescProiezioneCarte("");
		}
	}

	private void caricaComboCartografia(DettaglioTitoloForm dettaglioTitoloForm) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioTitoloForm.setListaPubblicazioneGovernativa(carCombo.loadComboCodiciDesc(factory.getCodici().getCodicePubblicazioneGovernativa()));
		dettaglioTitoloForm.setListaIndicCol(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceColore120()));
		dettaglioTitoloForm.setListaSupportoFisico(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSupportoFisicoCartografico()));
		dettaglioTitoloForm.setListaTecnicaCreazione(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTecnicaCartografia()));
		dettaglioTitoloForm.setListaFormaRiproduzione(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceFormaRiproduzione()));
		dettaglioTitoloForm.setListaFormaPubblicazione(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceFormaPubblicazione()));
		dettaglioTitoloForm.setListaAltitudine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceAltitudineSensore()));
		dettaglioTitoloForm.setListaIndicatoreTipoScala(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceIndicatoreTiposcala()));
		// Modifica almaviva2 04.02.2011 BUG MANTIS 4192 Cambiato il metodo di reperimento mdei codici
		// per selezionare quelli validi e non tutti
//		dettaglioTitoloForm.setListaTipoScala(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTiposcala()));
		dettaglioTitoloForm.setListaTipoScala(carCombo.loadComboCodiciDesc(factory.getCodici().getCodici(CodiciType.CODICE_TIPOSCALA, true)));
		dettaglioTitoloForm.setListaMeridianoOrigine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceMeridianoOrigine()));
		dettaglioTitoloForm.setListaCarattereImmagine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceCarattereImmagine()));
		dettaglioTitoloForm.setListaForma(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceFormaDocumentoCartografico()));
		dettaglioTitoloForm.setListaPiattaforma(carCombo.loadComboCodiciDesc(factory.getCodici().getCodicePiattaforma()));
		dettaglioTitoloForm.setListaCategoriaSatellite(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceCategoriaSatellite()));

		// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
		dettaglioTitoloForm.setListaProiezioneCarte(carCombo.loadComboCodiciDesc(factory.getCodici().getCodici(CodiciType.CODICE_PROIEZIONE, true)));

		dettaglioTitoloForm.setListaLongitudine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceEstOvest()));
		dettaglioTitoloForm.setListaLatitudine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceNordSud()));

	}

	private void caricaGradiCartografia(DettaglioTitoloForm dettaglioTitoloForm)
	throws RemoteException, CreateException {

		// Intervento Marao 2014: Bug Mantis Esercizio 5519: ulteriore intervento nel caso in cui, dopo una prima conferma di inserimento
		// in Indice di un Documento di Cartografia, sia l'Indice ha negare il salvataggio per problemi di vario genere: in questo caso
		// nella mappa vengono riportati i valori "   °  \'  \"" e questi equivalgono a valori vuoti o nulli
		String COORDINATA_VUOTA = "   °  \'  \"";
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS1() != null &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS1().equals("") &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS1().equals(COORDINATA_VUOTA)) {
			dettaglioTitoloForm.setLatiInput1gg(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS1().substring(1,3));
			dettaglioTitoloForm.setLatiInput1pp(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS1().substring(3,5));
			dettaglioTitoloForm.setLatiInput1ss(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS1().substring(5,7));
		} else {
			dettaglioTitoloForm.setLatiInput1gg("");
			dettaglioTitoloForm.setLatiInput1pp("");
			dettaglioTitoloForm.setLatiInput1ss("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS2() != null &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS2().equals("") &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS2().equals(COORDINATA_VUOTA)) {
			dettaglioTitoloForm.setLatiInput2gg(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS2().substring(1,3));
			dettaglioTitoloForm.setLatiInput2pp(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS2().substring(3,5));
			dettaglioTitoloForm.setLatiInput2ss(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLatitValNS2().substring(5,7));
		} else {
			dettaglioTitoloForm.setLatiInput2gg("");
			dettaglioTitoloForm.setLatiInput2pp("");
			dettaglioTitoloForm.setLatiInput2ss("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO1() != null &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO1().equals("") &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO1().equals(COORDINATA_VUOTA)) {
			dettaglioTitoloForm.setLongInput1gg(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO1().substring(0,3));
			dettaglioTitoloForm.setLongInput1pp(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO1().substring(3,5));
			dettaglioTitoloForm.setLongInput1ss(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO1().substring(5,7));
		} else {
			dettaglioTitoloForm.setLongInput1gg("");
			dettaglioTitoloForm.setLongInput1pp("");
			dettaglioTitoloForm.setLongInput1ss("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO2() != null &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO2().equals("") &&
				!dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO2().equals(COORDINATA_VUOTA)) {
			dettaglioTitoloForm.setLongInput2gg(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO2().substring(0,3));
			dettaglioTitoloForm.setLongInput2pp(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO2().substring(3,5));
			dettaglioTitoloForm.setLongInput2ss(dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getLongitValEO2().substring(5,7));
		} else {
			dettaglioTitoloForm.setLongInput2gg("");
			dettaglioTitoloForm.setLongInput2pp("");
			dettaglioTitoloForm.setLongInput2ss("");
		}

		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getScalaOriz() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setScalaOriz("");
		}
		if (dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().getScalaVert() == null) {
			dettaglioTitoloForm.getDettTitComVO().getDetTitoloCarVO().setScalaVert("");
		}
	}


	public ActionForward calcolaISBD(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		CostruttoreIsbd costruttoreIsbd = new CostruttoreIsbd();

		//Costruisco l'oggetto che determina l'eventuale valore null dei campi
		Isbd isbd = new Isbd(
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getNumOrdine(), //a929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getNumOpera(), //b929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getNumCatTem(), //c929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTonalita(), //e929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getSezioni(), //f929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTitOrdinam(), //g929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getTitEstratto(), //h929
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getAppellativo(), //i929
				new String[]{dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic1(),
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic2(),
								dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getFormaMusic3(),}, //a928, fino a 3 valori
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgSint(), //b928
				dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO().getOrgAnal() //c929
		);

		try {
			costruttoreIsbd.definisciISBDtitUniMusicale(
				isbd.getA_929(),
				isbd.getB_929(),
				isbd.getC_929(),
				isbd.getE_929(),
				isbd.getF_929(),
				isbd.getG_929(),
				isbd.getH_929(),
				isbd.getI_929(),
				isbd.getA_928(),
				isbd.getB_928(),
				isbd.getC_928());

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo",	e.getMessage()));
			return mapping.getInputForward();
		}

		//almaviva5_20140711 #5606
		String isbdMusica = costruttoreIsbd.getIsbd();
		if (!ValidazioneDati.isFilled(isbdMusica)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.isbd.organico.errato"));
			return mapping.getInputForward();
		}

		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setAreaTitTitolo(isbdMusica);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}


	// MAggio 2015: almaviva2 L'evolutiva comporta l'inserimento sulla mappa di inserimento/variazione
	// la presenza di un nuovo Tasto button.calcolaArea5 SOLO ne caso di tipoMateriale=H che leggendo le
	// variabile ad essa relative componga in maniera automatica l'area5 DESCRIZIONE FISICA; previsto
	// inserimento di un nuovo campo (tipo supporto) e forse un altro per la quantita (es. 2CD)
	public ActionForward calcolaArea5(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		DettaglioTitoloParteFissaVO detTitoloPFissaVO = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO();
		if (!ValidazioneDati.isFilled(detTitoloPFissaVO.getTipoRec())) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.TipoRecordObblig"));
			return mapping.getInputForward();
		}

		DettaglioTitoloAudiovisivoVO detTitoloAudVO = dettaglioTitoloForm.getDettTitComVO().getDetTitoloAudVO();
		if (!ValidazioneDati.isFilled(dettaglioTitoloForm.getNumSupporti()) ) {
			if (ValidazioneDati.in(detTitoloAudVO.getPubblicVideoreg(), "x", "z")) {
				if (!ValidazioneDati.isFilled(detTitoloAudVO.getPresentVideoreg()) ) {
					// Unico cso in cui è ammesso Supporto non definito (lasciare vuoto, se possibile lo indicherà a mano il bibliotecario)
				} else {
					// errore getNumSupporti obbligatorio e maggiore di 0
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selezNumSupportiObblig"));
					return mapping.getInputForward();
				}
			} else {
				// errore getNumSupporti obbligatorio e maggiore di 0
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.selezNumSupportiObblig"));
				return mapping.getInputForward();
			}
		}

		// caricamento delle descrizioni dei campi della specificità audiovisivo
		this.caricaDescrizioniAudiovisivo(dettaglioTitoloForm,"ULT");

		String area5 = "";
		String divisoreVirgola = ", ";
		String divisoreDuePunti = " : ";
		String divisorePuntoVirgola = " ; ";
		String tipoSupporto = "";

		// Supporto fittizio in attesa di tabella codici decodifica 
		Boolean plurale = null;
		if (ValidazioneDati.isFilled(dettaglioTitoloForm.getNumSupporti())) {
			if (Integer.valueOf(dettaglioTitoloForm.getNumSupporti()) == 1) {
				plurale = false;
			} else if (Integer.valueOf(dettaglioTitoloForm.getNumSupporti()) > 1) {
				plurale = true;
			}
		}

		String genericFormaDistrib = "";
		if (detTitoloPFissaVO.getTipoRec().equals("g")) {
			if (detTitoloAudVO.getTipoVideo().equals("c")) {
				// PUVI tipo rec g - tipo video c
				genericFormaDistrib = detTitoloAudVO.getPubblicVideoreg();
			} else {
				// FODI tipo rec g - tipo video a,b
				genericFormaDistrib = detTitoloAudVO.getFormaPubblDistr();
			}
		} else {
			// FPUB tipo rec i,j
			genericFormaDistrib = detTitoloAudVO.getFormaPubblicazioneDisco();
		}



		tipoSupporto = SBNMarcUtil.getDescrizioneArea5(detTitoloPFissaVO.getTipoRec(),
				detTitoloAudVO.getTipoVideo(),
				// detTitoloAudVO.getPubblicVideoreg(),
				genericFormaDistrib,
				detTitoloAudVO.getVelocita(),
				detTitoloAudVO.getPresentVideoreg(),
				plurale);

		if (tipoSupporto == null) {
			if (plurale) {
				tipoSupporto = dettaglioTitoloForm.getNumSupporti() + " " + "SUPPORTI SCONOSCIUTI";
			} else {
				tipoSupporto = dettaglioTitoloForm.getNumSupporti() + " " + "SUPPORTO SCONOSCIUTO";
			}
		} else {
			tipoSupporto = dettaglioTitoloForm.getNumSupporti() + " " + tipoSupporto;
		}

		if (detTitoloPFissaVO.getTipoRec().equals("g")) {
			area5 = tipoSupporto;

			// TRATTAMENTO dettaglioTitoloForm.getDescPresentVideoreg
			// TIPO VIDEO 115$a0 TIVI (tb_audiovideo.tp_mater_audiovis) : c (videoregistrazione)
			if (detTitoloAudVO.getTipoVideo().equals("c")) {
				if (genericFormaDistrib.equals("a")) {
					// Per ora non si fa nulla
				} else if (genericFormaDistrib.equals("b")) {
					if (detTitoloAudVO.getPresentVideoreg().equals("k")) {
						// Per ora non si fa nulla
					} else {
						if (ValidazioneDati.in(detTitoloAudVO.getPresentVideoreg(), "g", "h")) {
							area5 = area5 + " (" + ValorizzaStringArea5("", dettaglioTitoloForm.getDescPresentVideoreg()) + ")";
						} else {
							// Per ora non si fa nulla
						}
					}
				} else if (genericFormaDistrib.equals("c")) {
					if (ValidazioneDati.in(detTitoloAudVO.getPresentVideoreg(), "a", "b", "c", "i", "j")) {
						area5 = area5 + " (" + ValorizzaStringArea5("", dettaglioTitoloForm.getDescPresentVideoreg()) + ")";
					} else {
						// Per ora non si fa nulla
					}
				} else if (genericFormaDistrib.equals("d")) {
					if (ValidazioneDati.in(detTitoloAudVO.getPresentVideoreg(), "d", "e", "f")) {
						area5 = area5 + " (" + ValorizzaStringArea5("", dettaglioTitoloForm.getDescPresentVideoreg()) + ")";
					} else {
						// Per ora non si fa nulla
					}
				} else if (genericFormaDistrib.equals("e")) {
					// Per ora non si fa nulla
				} else if (ValidazioneDati.in(genericFormaDistrib,"x", "z")) {
					// Per ora non si fa nulla
				}

				if (detTitoloAudVO.getLunghezza().length() > 0) {
					area5 = area5 + " (" +
						ValorizzaStringArea5("", detTitoloAudVO.getLunghezza()) + " min)" ;
				}

				area5 = area5 + divisoreDuePunti;

				if (ValidazioneDati.in(detTitoloAudVO.getIndicatoreColore(), "a", "b", "c")) {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescIndicatoreColore());
				}

				if (ValidazioneDati.in(detTitoloAudVO.getStandardTrasmiss(),"a", "b", "c")) {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescStandardTrasmiss());
				}
				area5 = area5 + divisorePuntoVirgola;
				// dovrebbe seguire la dimensione del supporto (non presente nelle specificazioni) aggiunta dal bibliotecario
				//

			}
			// TIPO VIDEO 115$a0 TIVI (tb_audiovideo.tp_mater_audiovis) : b (videoproiezione)
			else if (detTitoloAudVO.getTipoVideo().equals("b")) {
				area5 = area5 + divisoreDuePunti;
				if (ValidazioneDati.in(detTitoloAudVO.getMaterialeEmulsBase(), "a", "c")) {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescMaterialeEmulsBase());
				}
				if (ValidazioneDati.in(detTitoloAudVO.getIndicatoreColore(), "a", "b", "c")) {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescIndicatoreColore());
				}

				area5 = area5 + divisorePuntoVirgola;

				if (ValidazioneDati.in(genericFormaDistrib, "g", "h", "i", "j")) {
					// Per ora non si fa nulla
				} else if (genericFormaDistrib.equals("k")) {
					if (ValidazioneDati.in(detTitoloAudVO.getLarghezzaDimensioni(), "k", "l")) {
						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescLarghezzaDimensioni());
					}
				} else if (ValidazioneDati.in(genericFormaDistrib, "l", "u", "x", "z")) {
					if (ValidazioneDati.in(detTitoloAudVO.getLarghezzaDimensioni(), "r", "s", "t")) {
						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescLarghezzaDimensioni());
					}
				}

			}
			// TIPO VIDEO 115$a0 TIVI (tb_audiovideo.tp_mater_audiovis) : a (film)
			else if (detTitoloAudVO.getTipoVideo().equals("a")) {

				if (detTitoloAudVO.getLunghezza().length() > 0) {
					area5 = area5 + " (" + 	ValorizzaStringArea5("", detTitoloAudVO.getLunghezza()) + " min)" ;
				}
				area5 = area5 + divisoreDuePunti;

				if (ValidazioneDati.in(detTitoloAudVO.getComposPellic(), "u", "z")) {
					// Per ora non si fa nulla
				} else {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescComposPellic());
				}
				if (ValidazioneDati.in(detTitoloAudVO.getIndicatoreColore(), "a", "b", "c")) {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescIndicatoreColore());
				}
				area5 = area5 + divisorePuntoVirgola;

				if (genericFormaDistrib.equals("a")) {
					if (ValidazioneDati.in(detTitoloAudVO.getLarghezzaDimensioni(), "a", "b", "c", "d", "e", "f", "g")) {
						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescLarghezzaDimensioni());
					}
				} else if (ValidazioneDati.in(genericFormaDistrib, "b", "c")) {
					if (ValidazioneDati.in(detTitoloAudVO.getLarghezzaDimensioni(), "a", "m", "n", "o", "p", "q")) {
						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescLarghezzaDimensioni());
					}
				}
			}

		} else if (ValidazioneDati.in(detTitoloPFissaVO.getTipoRec(), "i", "j")) {
			area5 = tipoSupporto;

			if (detTitoloAudVO.getDurataRegistraz() != null && detTitoloAudVO.getDurataRegistraz().length() == 6) {
				area5 = area5 + " (" + ValidazioneDati.trimOrEmpty
					(convertiDurataRegistrazione(detTitoloAudVO.getDurataRegistraz())) + ")";
			}


			// Correttiva Gennaio 2016  almaviva2 ricomposizione area 5: la velocità non va prospettata quindi non andrebbero
			// visualizzati i due punti (:) che la introducono
			// gestione dei campi area5Prima e area5Seconda per controlli e corretta gestione della punteggiatura
			String area5Prima = area5;
			String area5Seconda = "";
			area5 = area5 + divisoreDuePunti;

			// 1.	tipo di materiale (126 $b1) “TIMA”
			if (ValidazioneDati.in(detTitoloAudVO.getTipoDiMateriale(), "u", "x", "z")) {
				// Per ora non si fa nulla
			} else  {
				if (genericFormaDistrib.equals("a")
					&& detTitoloAudVO.getVelocita().equals("g"))  {
					// Per ora non si fa nulla
				} else {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescTipoDiMateriale());
					area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescTipoDiMateriale());
				}
			}

			// 2.	velocità (126 $a 1) “VELO”
			if (ValidazioneDati.in(detTitoloAudVO.getVelocita(), "u", "x", "z")) {
				// Per ora non si fa nulla
			} else  {
				if ((genericFormaDistrib.equals("a")
					&& detTitoloAudVO.getVelocita().equals("g"))
					|| (genericFormaDistrib.equals("c")
							&& detTitoloAudVO.getVelocita().equals("q"))
					|| (genericFormaDistrib.equals("d")
							&& detTitoloAudVO.getVelocita().equals("r")))  {
					// Per ora non si fa nulla
				} else {
					area5 = ValorizzaAllStringArea5(area5,divisoreVirgola, dettaglioTitoloForm.getDescVelocita());
					area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescVelocita());
				}
			}


			// le tabelle TERE, TITA,LASC, CONA e TISU si devono verificare ed impostare solo se genericFormaDistrib.equals("a")
			if (genericFormaDistrib.equals("a")) {

				// 3.	tecnica di registrazione (126 $a 13) “TERE”
				if (ValidazioneDati.in(detTitoloAudVO.getTecnicaRegistraz(), "u", "x", "z")) {
					// Per ora non si fa nulla
				} else  {
					if (ValidazioneDati.in(detTitoloAudVO.getVelocita(), "a", "b", "c", "d", "e")
								&& detTitoloAudVO.getTecnicaRegistraz().equals("a"))  {

						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, CodiciProvider
								.cercaDescrizioneCodice("a", CodiciType.CODICE_TECNICA_REGISTRAZIONE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
						area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, CodiciProvider
								.cercaDescrizioneCodice("a", CodiciType.CODICE_TECNICA_REGISTRAZIONE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
					} else {
						// Per ora non si fa nulla
					}
				}

				// 4.	Direzione del solco (126 $b 2) “TITA”
				if (ValidazioneDati.in(detTitoloAudVO.getTipoDiTaglio(), "u", "x", "z")) {
					// Per ora non si fa nulla
				} else  {
					if (detTitoloAudVO.getVelocita().equals("a")
						&& detTitoloAudVO.getTipoDiTaglio().equals("b"))  {

						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, CodiciProvider
								.cercaDescrizioneCodice("b", CodiciType.CODICE_TIPO_TAGLIO, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
						area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, CodiciProvider
								.cercaDescrizioneCodice("b", CodiciType.CODICE_TIPO_TAGLIO, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
					} else if (ValidazioneDati.in(detTitoloAudVO.getVelocita(), "b", "c", "d", "e")
							&& detTitoloAudVO.getTipoDiTaglio().equals("b"))  {

							area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, CodiciProvider
									.cercaDescrizioneCodice("b", CodiciType.CODICE_TIPO_TAGLIO, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
							area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, CodiciProvider
									.cercaDescrizioneCodice("b", CodiciType.CODICE_TIPO_TAGLIO, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
					}
				}

				// 5.	Dimensioni del solco (126 $a 3) “LASC”
				if (ValidazioneDati.in(detTitoloAudVO.getLarghezzaScanal(), "u", "x", "z")) {
					// Per ora non si fa nulla
				} else  {
					if (detTitoloAudVO.getVelocita().equals("d")
						&& detTitoloAudVO.getLarghezzaScanal().equals("b"))  {

						area5 = ValorizzaAllStringArea5(area5, divisoreVirgola,CodiciProvider
								.cercaDescrizioneCodice("b", CodiciType.CODICE_LARGHEZZA_DELLA_SCANALATURA, CodiciRicercaType.RICERCA_CODICE_UNIMARC_ULTERIORE));
						area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, CodiciProvider
								.cercaDescrizioneCodice("b", CodiciType.CODICE_LARGHEZZA_DELLA_SCANALATURA, CodiciRicercaType.RICERCA_CODICE_UNIMARC_ULTERIORE));
					} else if (ValidazioneDati.in(detTitoloAudVO.getVelocita(), "b", "c", "d", "e")
							&& detTitoloAudVO.getLarghezzaScanal().equals("a"))  {

							area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, CodiciProvider
									.cercaDescrizioneCodice("a", CodiciType.CODICE_LARGHEZZA_DELLA_SCANALATURA, CodiciRicercaType.RICERCA_CODICE_UNIMARC_ULTERIORE));
							area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, CodiciProvider
									.cercaDescrizioneCodice("a", CodiciType.CODICE_LARGHEZZA_DELLA_SCANALATURA, CodiciRicercaType.RICERCA_CODICE_UNIMARC_ULTERIORE));

					}
				}

				// 6.	Numero delle tracce (126 $a 6) “CONA”
				if (ValidazioneDati.in(detTitoloAudVO.getConfigurazNastro(), "u", "x", "z")) {
					// Per ora non si fa nulla
				} else  {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
					area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());

				}

				// 7.	Numero di canali audio (126 $a 2) “TISU”
				if (ValidazioneDati.in(detTitoloAudVO.getTipoSuono(), "u", "x", "z")) {
					// Per ora non si fa nulla
				} else  {
					if (detTitoloAudVO.getVelocita().equals("g"))  {
						// Per ora non si fa nulla
					} else if (ValidazioneDati.in(detTitoloAudVO.getVelocita(), "a", "b", "c", "d", "e")) {
						 if (ValidazioneDati.in(detTitoloAudVO.getTipoSuono(),  "a", "b", "c"))  {
							 area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescTipoSuono());
							 area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescTipoSuono());
						 } else {
							// Per ora non si fa nulla
						 }
					}
				}
			}



			// 8.	Speciali caratteristiche di riproduzione (126 $a 14) “SCAR” (Riduzione del rumore)
			if (ValidazioneDati.in(detTitoloAudVO.getConfigurazNastro(), "u", "x", "z")) {
				// Per ora non si fa nulla
			} else  {
				if (genericFormaDistrib.equals("a"))  {
					// Per ora non si fa nulla
				} else if (genericFormaDistrib.equals("b"))  {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
					area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
				}  else if (genericFormaDistrib.equals("e"))  {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
					area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
				} else if (genericFormaDistrib.equals("f"))  {
					// Per ora non si fa nulla
				} else if (genericFormaDistrib.equals("g"))  {
					// Per ora non si fa nulla
				} else if (genericFormaDistrib.equals("h"))  {
					area5 = ValorizzaAllStringArea5(area5, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
					area5Seconda = ValorizzaAllStringArea5(area5Seconda, divisoreVirgola, dettaglioTitoloForm.getDescConfigurazNastro());
				}
			}


			if (area5Seconda.equals("")) {
				area5 = area5Prima;
			} else {
				if (area5Seconda.substring(0,2).equals(", ")) {
					area5Seconda = area5Seconda.substring(2);
				}
				area5 = area5Prima + divisoreDuePunti + area5Seconda;
			}


			area5 = area5 + divisorePuntoVirgola;

			// 9.	Dimensioni (126 $a 4) “DINA”
				// Per ora non si fa nulla

		}

		area5 = area5.replaceAll("\\s+", " ");
		detTitoloPFissaVO.setAreaTitDescrFis(area5);
		request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + detTitoloPFissaVO.getLivAut());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public String convertiDurataRegistrazione (String durata) {

		int hh = Integer.parseInt(durata.substring(0, 2));
		int mm = Integer.parseInt(durata.substring(2, 4));
		int ss = Integer.parseInt(durata.substring(4, 6));

		if (hh > 0) {
			mm = mm + (hh*60);
		}

		if (ss > 0) {
			return mm + " min " + ss + " s";
		} else {
			return mm + " min";
		}

	}



	public String controllaISBN10 (String codice) {

		// Verifica lunghezza
		if (codice.length() != 10) {
			return "(errato)";
		}
		// Verifica carattere di controllo
		int ris1=0;
		int ris2=0;
		int intControllo = 0;
		String charControllo;
		ris1 = ((Integer.valueOf(codice.substring(0,1)))*10
				+  (Integer.valueOf(codice.substring(1,2)))*9
				+  (Integer.valueOf(codice.substring(2,3)))*8
				+  (Integer.valueOf(codice.substring(3,4)))*7
				+  (Integer.valueOf(codice.substring(4,5)))*6
				+  (Integer.valueOf(codice.substring(5,6)))*5
				+  (Integer.valueOf(codice.substring(6,7)))*4
				+  (Integer.valueOf(codice.substring(7,8)))*3
				+  (Integer.valueOf(codice.substring(8,9)))*2);

		ris2 = (ris1 % 11);

		// Inizio Intervento interno 28.02.2011 almaviva2 BUG MANTIS 4271 - Inserito controllo sul resto a 0 perchè mancante nella routine
		// di controllo dell'ISBN a 10 caratteri
		if (ris2 == 0) {
			if (codice.substring(9).equals("0")) {
				return "";
			} else {
				return "(errato)";
			}
		}
		// Fine  Intervento  interno 28.02.2011 almaviva2

		intControllo = (11 - ris2);

		if (intControllo == 10) {
			charControllo = "X";
		} else {
			charControllo = String.valueOf(intControllo);
		}


		// BUG mantis 5319  collaudo - Maggio 2013 - la X del codice di controllo ISBN a 10 caratteri viene accettata anche
		// se scritta in minuscolo; sara maiuscolizzata in fase di inserimento in Base Dati
		if (!codice.substring(9).toUpperCase().equals(charControllo)) {
			return "(errato)";
		}

		return "";
	}

	public String ValorizzaStringArea5 (String divisore, String campo) {

		if ((ValidazioneDati.trimOrEmpty(campo)).equals("")) {
			return "";
		}
		return divisore + campo;
	}

	public String ValorizzaAllStringArea5 (String area5, String divisore, String campo) {

		if ((ValidazioneDati.trimOrEmpty(campo)).equals("")) {
			return area5;
		}
		if (area5.endsWith(": ")) {
			return area5 + campo;
		}
		if (area5.endsWith("; ")) {
			return area5 + campo;
		}
		return area5 + divisore + campo;
	}
	// Intervento interno 12.01.2011 almaviva2 BUG 4041
	// L'intervento richiesto nel bug in oggetto viene eliminato perchè era una richiesta ERRATA !!!!!
	// La routine viene steriscata e viene ripristinata la precedente versione(DettaglioTitoloAction-controllaISBN13)

//	public String controllaISBN13 (String codice) {
//		// Verifica lunghezza
//		if (codice.length() != 13) {
//			return "(errato)";
//		}
//		// Verifica carattere di controllo
//		int ris1=0;
//		int ris2=0;
//		int intControllo = 0;
//		String charControllo;
//
//		ris1 = ((Integer.valueOf(codice.substring(0,1)))*1
//				+  (Integer.valueOf(codice.substring(1,2)))*3
//				+  (Integer.valueOf(codice.substring(2,3)))*1
//				+  (Integer.valueOf(codice.substring(3,4)))*3
//				+  (Integer.valueOf(codice.substring(4,5)))*1
//				+  (Integer.valueOf(codice.substring(5,6)))*3
//				+  (Integer.valueOf(codice.substring(6,7)))*1
//				+  (Integer.valueOf(codice.substring(7,8)))*3
//				+  (Integer.valueOf(codice.substring(8,9)))*1
//				+  (Integer.valueOf(codice.substring(9,10)))*3
//				+  (Integer.valueOf(codice.substring(10,11)))*1
//				+  (Integer.valueOf(codice.substring(11,12)))*3);
//
//		ris2 = (ris1 % 10);
//
//		// Inizio Intervento interno 09.12.2010 almaviva2 BUG 4041 - intervento successivo ANNULLATO !!!!!
//		// Inizio Intervento interno 26.11.2009 almaviva2 - Inserito controllo sul resto a 0 perchè mancante
////		if (ris2 == 0) {
////			if (codice.substring(12).equals("0")) {
////				return "";
////			} else {
////				return "(errato)";
////			}
////		}
//		// Fine  Intervento interno 26.11.2009 almaviva2 - Inserito controllo sul resto a 0 perchè mancante
//
//		intControllo = (10 - ris2);
//
//		// Inizio Intervento interno 09.12.2010 almaviva2 BUG 4041 - tradotto il valore di intControllo da 10 a X
//		if (intControllo == 10) {
//			charControllo = "X";
//		} else {
//			charControllo = String.valueOf(intControllo);
//		}
//		// Fine Intervento interno 09.12.2010 almaviva2 BUG 4041
//
//		if (!codice.substring(12).equals(charControllo)) {
//			return "(errato)";
//		}
//
//		return "";
//
//	}


	public String controllaISBN13 (String codice) {
		// Verifica lunghezza
		if (codice.length() != 13) {
			return "(errato)";
		}
		// Verifica carattere di controllo
		int ris1=0;
		int ris2=0;
		int intControllo = 0;
		String charControllo;

		ris1 = ((Integer.valueOf(codice.substring(0,1)))*1
				+  (Integer.valueOf(codice.substring(1,2)))*3
				+  (Integer.valueOf(codice.substring(2,3)))*1
				+  (Integer.valueOf(codice.substring(3,4)))*3
				+  (Integer.valueOf(codice.substring(4,5)))*1
				+  (Integer.valueOf(codice.substring(5,6)))*3
				+  (Integer.valueOf(codice.substring(6,7)))*1
				+  (Integer.valueOf(codice.substring(7,8)))*3
				+  (Integer.valueOf(codice.substring(8,9)))*1
				+  (Integer.valueOf(codice.substring(9,10)))*3
				+  (Integer.valueOf(codice.substring(10,11)))*1
				+  (Integer.valueOf(codice.substring(11,12)))*3);

		ris2 = (ris1 % 10);

		// Inizio Intervento interno 26.11.2009 almaviva2 - Inserito controllo sul resto a 0 perchè mancante
		if (ris2 == 0) {
			if (codice.substring(12).equals("0")) {
				return "";
			} else {
				return "(errato)";
			}
		}
		// Fine  Intervento interno 26.11.2009 almaviva2 - Inserito controllo sul resto a 0 perchè mancante

		intControllo = (10 - ris2);

		charControllo = String.valueOf(intControllo);

		if (!codice.substring(12).equals(charControllo)) {
			return "(errato)";
		}

		return "";

	}

	public String controllaISSN (String codice) {

		// Inizio Intervento interno Maggio 2019 ALMAVIVA2 - eliminato carattere trattino dalla stringa del codice ISSN
		codice = codice.replace("-", "");
		// Fine Intervento interno Maggio 2019
		
		// Verifica lunghezza
		if (codice.length() != 8) {
			return "(errato)";
		}
		// Verifica carattere di controllo
		int ris1=0;
		int ris2=0;
		int intControllo = 0;
		String charControllo;
		ris1 = ((Integer.valueOf(codice.substring(0,1)))*8
				+  (Integer.valueOf(codice.substring(1,2)))*7
				+  (Integer.valueOf(codice.substring(2,3)))*6
				+  (Integer.valueOf(codice.substring(3,4)))*5
				+  (Integer.valueOf(codice.substring(4,5)))*4
				+  (Integer.valueOf(codice.substring(5,6)))*3
				+  (Integer.valueOf(codice.substring(6,7)))*2);

		ris2 = (ris1 % 11);
		intControllo = (11 - ris2);

		// Inizio Intervento interno Maggio 2019 ALMAVIVA2 - tradotto il valore di intControllo da 10 a X
		// charControllo = String.valueOf(intControllo);
		if (intControllo == 10) {
			charControllo = "X";
		} else {
			charControllo = String.valueOf(intControllo);
		}
		// Fine Intervento interno Maggio 2019
		
		// Inizio Intervento interno Maggio 2019 ALMAVIVA2 - la X del codice di controllo ISBN a 10 caratteri viene accettata anche
		// se scritta in minuscolo; sara maiuscolizzata in fase di inserimento in Base Dati
//		if (!codice.substring(7).equals(charControllo)) {
//			return "(errato)";
//		}
		if (!codice.substring(7).toUpperCase().equals(charControllo)) {
			return "(errato)";
		}
		// Fine Intervento interno Maggio 2019

		return "";
	}

    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	public ActionForward canArea0DatiComuni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setFormaContenutoBIS("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoContenutoBIS("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setMovimentoBIS("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setDimensioneBIS("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setSensorialitaBIS1("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setSensorialitaBIS2("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setSensorialitaBIS3("");
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().setTipoMediazioneBIS("");
		dettaglioTitoloForm.setPresenzaArea0BIS("NO");

		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward insArea0DatiComuni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		dettaglioTitoloForm.setPresenzaArea0BIS("SI");
		this.saveToken(request);
		return mapping.getInputForward();
	}

	private boolean controlloAggiornamentoIncipit(HttpServletRequest request, ActionForm form) {
		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		DettaglioTitoloMusicaVO detTitoloMusVO = dettaglioTitoloForm.getDettTitComVO().getDetTitoloMusVO();

		List<DettaglioIncipitVO> dettagliIncipit = detTitoloMusVO.getListaDettagliIncipit();
		List<TabellaNumSTDImpronteVO> listaIncipit = detTitoloMusVO.getListaIncipit();

		int sizeDettagli = ValidazioneDati.size(dettagliIncipit);
		int sizeLista = ValidazioneDati.size(listaIncipit);

		if (sizeLista < 1)
			return true;

		//travaso dati sintetica su dettaglio
		for (int idx = 0; idx < sizeLista; idx++) {
			TabellaNumSTDImpronteVO incipit = listaIncipit.get(idx);
			DettaglioIncipitVO dettaglio = dettagliIncipit.get(idx);
			dettaglio.setNumMovimento(incipit.getCampoUno());
			dettaglio.setNumProgDocumento(incipit.getCampoDue());
			dettaglio.setContestoMusicale(incipit.getNota());
		}

		if (sizeDettagli < 2)
			return true;

		//check duplicati
		DettaglioIncipitVO p = on(DettaglioIncipitVO.class);
		for (int idx = 0; idx < sizeDettagli; idx++) {
			DettaglioIncipitVO dettaglio = dettagliIncipit.get(idx);
			List<DettaglioIncipitVO> duplicati = select(dettagliIncipit,
					having(p.getRepeatableId(), equalTo(dettaglio.getRepeatableId())));
			if (ValidazioneDati.size(duplicati) != 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.incipitMusicaDuplicato"));
				return false;
			}
		}

		return true;
	}

	// Modifica del 19.05.2015 almaviva2 (adeguamento all'Indice) per accettare il valore n del tipoMediazione
	private void caricaDefaultDatiComuni(DettaglioTitoloForm dettaglioTitoloForm)
			throws RemoteException, CreateException {

		DettaglioTitoloParteFissaVO detTitoloPFissaVO = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO();
		String tipoRec = detTitoloPFissaVO.getTipoRec();
		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		// Bug esercizio mantis 0006099: eliminata preimpostazione a spazio del tipo supporto;
		//detTitoloPFissaVO.setTipoSupporto("");

		if (ValidazioneDati.in(tipoRec, "a", "b")) {
			detTitoloPFissaVO.setFormaContenuto("i");
			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("e");
			detTitoloPFissaVO.setTipoMediazione("n");
		} else if (ValidazioneDati.in(tipoRec, "c", "d")) {
			detTitoloPFissaVO.setFormaContenuto("d");
			detTitoloPFissaVO.setTipoContenuto("a");
			detTitoloPFissaVO.setMovimento("");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("e");
			detTitoloPFissaVO.setTipoMediazione("n");
		} else if (ValidazioneDati.in(tipoRec, "e", "f")) {
			detTitoloPFissaVO.setFormaContenuto("b");
			detTitoloPFissaVO.setTipoContenuto("c");
			detTitoloPFissaVO.setMovimento("b");
			detTitoloPFissaVO.setDimensione("2");
			detTitoloPFissaVO.setSensorialita1("e");
			detTitoloPFissaVO.setTipoMediazione("n");
		} else if (tipoRec.equals("g")) {
			detTitoloPFissaVO.setFormaContenuto("b");
			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("a");
			detTitoloPFissaVO.setDimensione("2");
			detTitoloPFissaVO.setSensorialita1("e");
			detTitoloPFissaVO.setTipoMediazione("g");
		} else if (tipoRec.equals("i")) {
			detTitoloPFissaVO.setFormaContenuto("h");
			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("a");
			detTitoloPFissaVO.setTipoMediazione("a");
		} else if (tipoRec.equals("j")) {
			detTitoloPFissaVO.setFormaContenuto("d");
			detTitoloPFissaVO.setTipoContenuto("b");
			detTitoloPFissaVO.setMovimento("");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("a");
			detTitoloPFissaVO.setTipoMediazione("a");
		} else if (tipoRec.equals("k")) {
			detTitoloPFissaVO.setFormaContenuto("b");
			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("b");
			detTitoloPFissaVO.setDimensione("2");
			detTitoloPFissaVO.setSensorialita1("e");
			detTitoloPFissaVO.setTipoMediazione("n");
		} else if (tipoRec.equals("l")) {
			// Bug esercizio 5824: almaviva2: in creazione se si sceglie tipo record 'l' il programma mette nella zona area zero
			// forma del contenuto 'm' mentre per terminare la catalogazione bisogna mettere 't'
			// detTitoloPFissaVO.setFormaContenuto("m");
			// detTitoloPFissaVO.setFormaContenuto("t");
			// Bug esercizio 5948: almaviva2: l'intervento sopra descritto viene ANNULLATO: si mette forma del contenuto 'm'
			detTitoloPFissaVO.setFormaContenuto("m");

			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("e");
			// MODIFICA almaviva2 SETTEMBRE tiporec=l forma contenuto=m tipo mediazione=m
			detTitoloPFissaVO.setTipoMediazione("b");
			detTitoloPFissaVO.setTipoMediazione("m");
		} else if (tipoRec.equals("m")) {
			detTitoloPFissaVO.setFormaContenuto("m");
			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("e");
			detTitoloPFissaVO.setTipoMediazione("m");
		} else if (tipoRec.equals("r")) {
			detTitoloPFissaVO.setFormaContenuto("e");
			detTitoloPFissaVO.setTipoContenuto("");
			detTitoloPFissaVO.setMovimento("");
			// Agosto 2015: telefonico con Scognamiglio: per tipo record "r" il campo c181[0].setB_181_2 rimane vuoto
			// detTitoloPFissaVO.setDimensione("3");
			detTitoloPFissaVO.setDimensione("");
			detTitoloPFissaVO.setSensorialita1("e");
//			detTitoloPFissaVO.setTipoMediazione("y");
			detTitoloPFissaVO.setTipoMediazione("n");
		}

		// Gennaio 2016 - Per permettere una catalogazione più agevole, i supporti dei documenti M, W, S
		// che hanno tipo record ‘a’ (testo a stampa) andrebbero valorizzati con ‘nc’ (volume).
		// Pensiamo che non sia possibile definire ulteriori default.
		if ((detTitoloPFissaVO.getNatura().equals("M")
				|| detTitoloPFissaVO.getNatura().equals("W")
				|| detTitoloPFissaVO.getNatura().equals("S"))
				&& tipoRec.equals("a")) {
			detTitoloPFissaVO.setTipoSupporto("nc");
		// Bug esercizio mantis 0006099: eliminata preimpostazione a spazio del tipo supporto;
//		} else {
//			detTitoloPFissaVO.setTipoSupporto("");
			// almaviva2 Febbraio 2016 - Intervento interno - la copia dei dati dello Spoglio
			// non deve comprendere il tipoSupporto che, nel caso delle n deve rimanere null
		} else if (detTitoloPFissaVO.getNatura().equals("N")) {
			detTitoloPFissaVO.setTipoSupporto(null);
		}
	}


	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// al link dei documenti su Basi Esterne - Link verso base date digitali
	public ActionForward insLinkEsterni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().addListaLinkEsterni(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canLinkEsterni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioLinkEsterni() == null
				|| dettaglioTitoloForm.getSelezRadioLinkEsterni().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblLinkEsterno"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioLinkEsterni());
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward ricalcolaURL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		if (dettaglioTitoloForm.getSelezRadioLinkEsterni() == null
				|| dettaglioTitoloForm.getSelezRadioLinkEsterni().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblLinkEsterno"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int indexLink = Integer.parseInt(dettaglioTitoloForm.getSelezRadioLinkEsterni());

		List<SifRicercaRepertoriVO> listaRepertoriNew = new ArrayList<SifRicercaRepertoriVO>();
		SifRicercaRepertoriVO eleRepertorio = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaRepertoriNew = factory.getGestioneRepertorio().getAllRepertori();

		if (listaRepertoriNew.size() > 0) {

			TabellaNumSTDImpronteVO tabLinkEsterni = new TabellaNumSTDImpronteVO();
			String siglaLinkEsterno = "";
			String idLinkEsterno = "";
			String indirizzoCompleto = "";
			Boolean trovato = false;

			tabLinkEsterni = dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(indexLink);
			siglaLinkEsterno = tabLinkEsterni.getCampoUno();

			idLinkEsterno = tabLinkEsterni.getCampoDue();

			if (tabLinkEsterni.getDescCampoDue().equals("")) {
				String descrizioneLinkEsterno = factory.getCodici().cercaDescrizioneCodice(siglaLinkEsterno,
						CodiciType.CODICE_LINK_ALTRA_BASE_DATI,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC);
				tabLinkEsterni.setDescCampoDue(descrizioneLinkEsterno);
			}



			for (int iRep = 0; iRep < listaRepertoriNew.size(); iRep++) {
				eleRepertorio = listaRepertoriNew.get(iRep);

				if (eleRepertorio.getTipo().toUpperCase().equals("B")
						//&& eleRepertorio.getSigl().toUpperCase().equals(tabLinkEsterni.getDescCampoDue().toUpperCase())) {
						&& eleRepertorio.getSigl().toUpperCase().equals(tabLinkEsterni.getCampoUno().toUpperCase())) {

						indirizzoCompleto = eleRepertorio.getDesc();
						if (siglaLinkEsterno.startsWith("a")) {  			// BRITISH MUSEUM
							ActionMessages errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						} else if (siglaLinkEsterno.equals("b")) {		// EDIT16
							idLinkEsterno = idLinkEsterno.replace("CNCE", "");
							idLinkEsterno = idLinkEsterno.replace(" ", "");
							if (!ValidazioneDati.strIsNumeric(idLinkEsterno)) {
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						} else if (siglaLinkEsterno.equals("c") || siglaLinkEsterno.equals("d")) {		// ESTC o ISTC
							if (idLinkEsterno.contains(" ")) {
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						} else if (siglaLinkEsterno.equals("e")) {		// VD16
							idLinkEsterno = idLinkEsterno.replace(" ", "+");
							if (!idLinkEsterno.contains("+")) {
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						} else if (siglaLinkEsterno.equals("f")) {		// VD17
							idLinkEsterno = idLinkEsterno.replace(" ", ":");
							if (!idLinkEsterno.contains(":")) {
								ActionMessages errors = new ActionMessages();
								errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato", idLinkEsterno));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						}
						String indirizzoRicomposto = ricalcolaLinkEsterno(indirizzoCompleto, idLinkEsterno);
						dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaLinkEsterni().get(indexLink).setNota(indirizzoRicomposto);
						trovato = true;
						break;
					}
				}

			if (!trovato) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.idRecordErrato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo
	public ActionForward insReperCartaceo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().addListaReperCartaceo(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canReperCartaceo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioTitoloForm dettaglioTitoloForm = (DettaglioTitoloForm) form;

		// Marzo 2018 - almaviva2 - corretto puntatore alla selezione in caso di cancellazione (si usava quello
		// verso i link esterni e non quello corretto cioè ReperCartaceo)
		if (dettaglioTitoloForm.getSelezRadioReperCartaceo() == null
				|| dettaglioTitoloForm.getSelezRadioReperCartaceo().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.selObblLinkEsterno"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(dettaglioTitoloForm.getSelezRadioReperCartaceo());
		dettaglioTitoloForm.getDettTitComVO().getDetTitoloPFissaVO().getListaReperCartaceo().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}


	public String ricalcolaLinkEsterno (String URLdaRepertorio, String IDrecord) {
		int countID = StringUtils.countMatches(URLdaRepertorio, "_ID_");
		String[] parts = URLdaRepertorio.split("_ID_");
		String SeparatoreTag1 = null;
		String SeparatoreTag2 = null;
		String SeparatoreTag3 = null;
		String SeparatoreTag4 = null;
		String IDS_COMPOSTO = null;
		String TAG = null;

		if(countID == 1){
			 TAG = "_ID_";
			 IDS_COMPOSTO = IDrecord;
		}
		if(countID == 2){
			SeparatoreTag1 = parts[1]; // Stringa fissa tra tag1 e tag2
			TAG = "_ID_" + SeparatoreTag1 + "_ID_";
			// SPLITTO IDrecord COMPOSTO DA I DUE ID CON LA PARTE FISSA
			//escapeMetaCharacters(SeparatoreTag1);
			String[] IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag1));
			IDS_COMPOSTO = IDS[0] + SeparatoreTag1 + IDS[1];
		}
		// AL MOMENTO NON CI SONO URL CON 3 ID
		if(countID == 3){
			SeparatoreTag1 = parts[1]; // Stringa fissa tra tag1 e tag2
			SeparatoreTag2 = parts[2]; // Stringa fissa tra tag2 e tag3
			SeparatoreTag3 = parts[3]; // Stringa fissa tra tag2 e tag3
			TAG = "_ID_" + SeparatoreTag1 + "_ID_" + SeparatoreTag2 + "_ID_" ;

			String[] IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag1));
			String IDS_1 = IDS[0]; // METTO PRIMO TAG in IDS_1
			// AVANZO SULLA STRINGA IDrecord (CLIENT) PER LA LUNGHEZZA DEL PRIMO IDENTIFICATIVO + IL SEPARATORE
			IDrecord = IDrecord.substring(IDS_1.length()+SeparatoreTag1.length(),IDrecord.length()-IDS_1.length()+SeparatoreTag1.length());

			// SPLIT NUOVA STRINGA SENZA PRIMO CAMPO CON IL SECONDO SEPARATORE
			IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag2));
			String IDS_2 = IDS[0]; // METTO SECONDO TAG in IDS_2
			// AVANZO SULLA STRINGA IDrecord PER LA LUNGHEZZA DEL SECONDO IDENTIFICATIVO + IL SEPARATORE
			IDrecord = IDrecord.substring(IDS_2.length()+SeparatoreTag2.length(),IDrecord.length()-IDS_2.length()+SeparatoreTag2.length());

			// SPLIT NUOVA STRINGA SENZA PRIMO e SECONDO CAMPO CON IL TERZO SEPARATORE
			IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag3));
			String IDS_3 = IDS[0]; // METTO SECONDO TAG in IDS_2

			IDS_COMPOSTO = IDS_1 + SeparatoreTag1 + IDS_2 + SeparatoreTag2 + IDS_3;
		}
		// AL MOMENTO NON CI SONO URL CON 4 ID
		if(countID == 4){
			SeparatoreTag1 = parts[1]; // Stringa fissa tra tag1 e tag2
			SeparatoreTag2 = parts[2]; // Stringa fissa tra tag2 e tag3
			SeparatoreTag3 = parts[3]; // Stringa fissa tra tag2 e tag3
			SeparatoreTag4 = parts[4]; // Stringa fissa tra tag2 e tag3
			TAG = "_ID_" + SeparatoreTag1 + "_ID_" + SeparatoreTag2 + "_ID_" + SeparatoreTag3 + "_ID_" ;

			String[] IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag1));
			String IDS_1 = IDS[0]; // METTO PRIMO TAG in IDS_1
			// AVANZO SULLA STRINGA IDrecord (CLIENT) PER LA LUNGHEZZA DEL PRIMO IDENTIFICATIVO + IL SEPARATORE
			IDrecord = IDrecord.substring(IDS_1.length()+SeparatoreTag1.length(),IDrecord.length()-IDS_1.length()+SeparatoreTag1.length());

			// SPLIT NUOVA STRINGA SENZA PRIMO CAMPO CON IL SECONDO SEPARATORE
			IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag2));
			String IDS_2 = IDS[0]; // METTO SECONDO TAG in IDS_2
			// AVANZO SULLA STRINGA IDrecord PER LA LUNGHEZZA DEL SECONDO IDENTIFICATIVO + IL SEPARATORE
			IDrecord = IDrecord.substring(IDS_2.length()+SeparatoreTag2.length(),IDrecord.length()-IDS_2.length()+SeparatoreTag2.length());

			// SPLIT NUOVA STRINGA SENZA PRIMO e SECONDO CAMPO CON IL TERZO SEPARATORE
			IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag3));
			String IDS_3 = IDS[0]; // METTO SECONDO TAG in IDS_2
			// AVANZO SULLA STRINGA IDrecord PER LA LUNGHEZZA DEL TERZO IDENTIFICATIVO + IL SEPARATORE
			IDrecord = IDrecord.substring(IDS_3.length()+SeparatoreTag3.length(),IDrecord.length()-IDS_3.length()+SeparatoreTag3.length());

			// SPLIT NUOVA STRINGA SENZA PRIMO e SECONDO CAMPO CON IL TERZO SEPARATORE
			IDS = IDrecord.split(escapeMetaCharacters(SeparatoreTag4));
			String IDS_4 = IDS[0]; // METTO SECONDO TAG in IDS_2

			IDS_COMPOSTO = IDS_1 + SeparatoreTag1 + IDS_2 + SeparatoreTag2 + IDS_3 + SeparatoreTag3 + IDS_4;
		}

		String URL_Composta = URLdaRepertorio.replace(TAG, IDS_COMPOSTO);
		return URL_Composta;
	}

    public String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&"};
        String outputString="";
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
                inputString = outputString;
            }
        }
        if(outputString.equals("")){
        	return inputString;
        }
        else{
        	return outputString;
        }
    }

}
