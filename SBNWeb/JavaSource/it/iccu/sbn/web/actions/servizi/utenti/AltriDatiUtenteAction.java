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
package it.iccu.sbn.web.actions.servizi.utenti;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.actionforms.servizi.utenti.AltriDatiUtenteForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class AltriDatiUtenteAction extends LookupDispatchAction {
	private static Logger log = Logger.getLogger(AltriDatiUtenteAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "Ok");
		map.put("servizi.bottone.annulla", "Annulla");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AltriDatiUtenteForm altriDatiUteForm = (AltriDatiUtenteForm) form;
		if (Navigation.getInstance(request).isFromBar() ) {
			this.impostaStatoMaterie(form);
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!altriDatiUteForm.isSessione()) {
			altriDatiUteForm.setSessione(true);
			altriDatiUteForm.setUteAna((UtenteBibliotecaVO) request.getAttribute("UtenteScelto"));
			this.loadProfessioni(request,altriDatiUteForm);
			this.loadDocumenti(altriDatiUteForm);
			log.debug("unspecified()");
		}
		return mapping.getInputForward();
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AltriDatiUtenteForm altriDatiUteForm = (AltriDatiUtenteForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!altriDatiUteForm.isSessione()) {
				altriDatiUteForm.setSessione(true);
			}

			this.impostaStatoMaterie(form);
			request.setAttribute("UtenteScelto", altriDatiUteForm
					.getUteAna());
			request.setAttribute("VengoDa", "AltriDati");
			resetToken(request);
			return mapping.findForward("ok");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AltriDatiUtenteForm altriDatiUteForm = (AltriDatiUtenteForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!altriDatiUteForm.isSessione()) {
			altriDatiUteForm.setSessione(true);
		}
		resetToken(request);
		this.impostaStatoMaterie(form);
		request.setAttribute("UtenteScelto", altriDatiUteForm.getUteAna());
		request.setAttribute("VengoDa", "AltriDati");
		return mapping.findForward("annulla");
	}

	private void loadProfessioni(HttpServletRequest request, ActionForm form) throws Exception {
		AltriDatiUtenteForm altriDatiUteForm = (AltriDatiUtenteForm) form;
		CaricamentoCombo carCombo = new CaricamentoCombo();
		// professioni
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		altriDatiUteForm.setTitoloStudio(carCombo
				.loadComboCodiciDesc(factory.getCodici()
						.getCodiceTitoloStudio()));
		altriDatiUteForm
				.setProfessioni(carCombo.loadComboCodiciDesc(factory
						.getCodici().getCodiceProfessioni()));
		altriDatiUteForm.setAteneo(carCombo
				.loadComboCodiciDesc(factory.getCodici().getCodiceAteneo()));
		altriDatiUteForm.setTipoPersonalita(carCombo
				.loadComboCodiciDesc(factory.getCodici()
						.getCodiceTipoPersonaGiuridica()));

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
		RicercaTitoloStudioVO ricercaTDS = new RicercaTitoloStudioVO();
		ricercaTDS.setCodPolo(navi.getUtente().getCodPolo());
		ricercaTDS.setCodBib(navi.getUtente().getCodBib());
		ricercaTDS.setTicket(navi.getUserTicket());
		ricercaTDS.setNumeroElementiBlocco(4000);
		DescrittoreBloccoVO blocco1 = delegate.caricaListaSpecialita(ricercaTDS);

		List appoggioSpecTdS = new ArrayList();
		appoggioSpecTdS.add(new SpecTitoloStudioVO("","","",""));
		if (blocco1.getTotRighe() > 0)
			appoggioSpecTdS.addAll(blocco1.getLista());
		altriDatiUteForm.setSpecTitoloStudio(appoggioSpecTdS);

		RicercaOccupazioneVO ricercaOccup = new RicercaOccupazioneVO();
		ricercaOccup.setCodPolo(navi.getUtente().getCodPolo());
		ricercaOccup.setCodBib(navi.getUtente().getCodBib());
		ricercaOccup.setTicket(navi.getUserTicket());
		ricercaOccup.setNumeroElementiBlocco(4000);
		blocco1 = delegate.caricaListaOccupazioni(ricercaOccup);
		List listaOccup = new ArrayList();
		listaOccup.add( new OccupazioneVO("", "", "", ""));
		if (blocco1.getTotRighe() > 0)
			listaOccup.addAll(blocco1.getLista());
		altriDatiUteForm.setOccupazioni(listaOccup);

		RicercaMateriaVO ricMat = new RicercaMateriaVO();
		ricMat.setCodPolo(navi.getUtente().getCodPolo());
		ricMat.setCodBib(navi.getUtente().getCodBib());
		ricMat.setCodice("");
		ricMat.setDescrizione("");
		ricMat.setNumeroElementiBlocco(4000);
		ricMat.setOrdinamento("1");
		ricMat.setTicket(navi.getUserTicket());
		altriDatiUteForm.setAllMaterie(
				factory.getGestioneServizi()
						.getListaMaterieCompleta(Navigation.getInstance(request).getUserTicket(), ricMat));
		if (altriDatiUteForm.getUteAna().getProfessione().getMaterie() == null
				|| altriDatiUteForm.getUteAna().getProfessione().getMaterie().size() == 0) {
			altriDatiUteForm.getUteAna().getProfessione().setMaterie(altriDatiUteForm.getAllMaterie());
		} else {
			if (altriDatiUteForm.getAllMaterie().size() > 0) {
				int index = 0;
				MateriaVO anaMat = new MateriaVO();
				MateriaVO uteMat = new MateriaVO();
				for (int indAna = 0; indAna < altriDatiUteForm.getAllMaterie().size(); indAna++) {
					anaMat = (MateriaVO) altriDatiUteForm.getAllMaterie().get(indAna);
					//anaMat.setProgressivo(indAna);
					String ins = "S";
					for (int indUte = 0; indUte < altriDatiUteForm.getUteAna().getProfessione().getMaterie().size(); indUte++) {
						uteMat = altriDatiUteForm.getUteAna().getProfessione().getMaterie().get(indUte);
						if (uteMat.getIdMateria() == anaMat.getIdMateria()) {
							ins = "N";
							break;
						}
					}
					if (ins.equals("S")) altriDatiUteForm.getUteAna().getProfessione().getMaterie().add(anaMat);
					index++;
				}
				Collections.sort(altriDatiUteForm.getUteAna().getProfessione().getMaterie(),
					MateriaVO.ORDINAMENTO_PER_DESCRIZIONE);
				for (int indUte = 0; indUte < altriDatiUteForm.getUteAna().getProfessione().getMaterie().size(); indUte++) {
					uteMat = altriDatiUteForm.getUteAna().getProfessione().getMaterie().get(indUte);
					uteMat.setProgressivo(indUte + 1);
				}

			}
		}
	}

	private void loadDocumenti(ActionForm form) throws Exception {
		AltriDatiUtenteForm altriDatiUteForm = (AltriDatiUtenteForm) form;
		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		altriDatiUteForm.setElencoDocumenti(carCombo
				.loadComboCodiciDesc(factory.getCodici()
						.getCodiceTipoDocumentoDiRiconoscimento()));
	}

	private void impostaStatoMaterie(ActionForm form) {
		AltriDatiUtenteForm altriDatiUteForm = (AltriDatiUtenteForm) form;
		List<MateriaVO> materie = altriDatiUteForm.getUteAna()
				.getProfessione().getMaterie();
		if (materie == null)
			return;
		for (MateriaVO m : materie) {
			String selez = m.getSelezionato();
			if (selez != null) {
				int stato = m.getStato();
				switch (stato) {
				case MateriaVO.INIZIALE_NON_SELEZIONATO:
					if (selez.equals(""))
						continue;
					m.setStato(MateriaVO.INSERITO_NUOVO);
					break;
				case MateriaVO.INIZIALE_SELEZIONATO:
					if (selez.equals("S"))
						continue;
					m.setStato(MateriaVO.CANCELLATO_DA_INIZIALE_SELEZIONATO);
					break;
				case MateriaVO.INSERITO_NUOVO:
					if (selez.equals("S"))
						continue;
					m.setStato(MateriaVO.INIZIALE_NON_SELEZIONATO);
					break;
				case MateriaVO.CANCELLATO_DA_INIZIALE_SELEZIONATO:
					if (selez.equals(""))
						continue;
					m.setStato(MateriaVO.INIZIALE_SELEZIONATO);
					break;
				}
			}
		}
		return;
	}

}
