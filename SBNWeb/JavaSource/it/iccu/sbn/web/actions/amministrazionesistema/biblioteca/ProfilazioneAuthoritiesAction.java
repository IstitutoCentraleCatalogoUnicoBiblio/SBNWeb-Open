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
package it.iccu.sbn.web.actions.amministrazionesistema.biblioteca;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.CheckVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.ProfilazioneAuthoritiesForm;
import it.iccu.sbn.web.actions.amministrazionesistema.ProfilazioneBaseAction;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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
import org.apache.struts.action.ActionMessages;

public final class ProfilazioneAuthoritiesAction extends ProfilazioneBaseAction {

    static Logger log = Logger.getLogger(ProfilazioneAuthoritiesAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.polo.torna", 					"Back");
		map.put("profilo.polo.annulla", 				"Annulla");
		map.put("profilo.polo.parametri.button.sem.cl", "Classificazioni");
		map.put("profilo.polo.parametri.button.sem.so", "Soggetti");
		map.put("profilo.polo.parametri.button.sem.th", "Thesauri");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();
		ProfilazioneAuthoritiesForm myForm = (ProfilazioneAuthoritiesForm) form;

		Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ABILITAZIONI_BIBLIOTECA);
            myForm.setAbilitatoWrite("FALSE");
        }
        catch (UtenteNotAuthorizedException e) {
            ActionMessages messaggio = new ActionMessages();
            messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messaggio.info.noaut"));
            this.saveErrors(request,messaggio);
            return mapping.findForward("blank");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_BIBLIOTECA);
            myForm.setAbilitatoWrite("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	myForm.setAbilitatoWrite("FALSE");
        }

		List<GruppoParametriVO> elencoAuth = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY);
		List<GruppoParametriVO> elencoSem = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA);
		List<GruppoParametriVO> elencoMat = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI);
		if ((String)request.getAttribute("nomeBib") != null)
			myForm.setNomeBiblioteca((String)request.getAttribute("nomeBib"));
		String codBib = (String)request.getAttribute("biblio");
		if (request.getAttribute("flagMat") != null)
			myForm.setFlagParMat((Boolean)request.getAttribute("flagMat"));

		if (elencoMat != null && elencoMat.size() >0)
			myForm.setElencoMat(elencoMat);
		if (elencoSem == null)
			elencoSem = new ArrayList<GruppoParametriVO>();
		myForm.setElencoSem(elencoSem);
		List<GruppoParametriVO> backupSem = new ArrayList<GruppoParametriVO>();
		if (elencoSem != null)
			for (int t = 0; t < elencoSem.size(); t++) {
				GruppoParametriVO gruppoVO = elencoSem.get(t);
				GruppoParametriVO gruppoBK = new GruppoParametriVO();
				gruppoBK = (GruppoParametriVO)gruppoVO.clone();
				backupSem.add(gruppoBK);
			}
		if (myForm.getBackupSem() == null || myForm.getBackupSem().size() == 0)
			myForm.setBackupSem(backupSem);
		myForm.setChecked((String[])request.getAttribute("checked"));

		if (elencoAuth != null && elencoAuth.size() > 0) {
			myForm.setElencoAuth(elencoAuth);
			List<GruppoParametriVO> backupAuth = new ArrayList<GruppoParametriVO>();
			for (int t = 0; t < elencoAuth.size(); t++) {
				GruppoParametriVO gruppoVO = elencoAuth.get(t);
				GruppoParametriVO gruppoBK = new GruppoParametriVO();
				gruppoBK = (GruppoParametriVO)gruppoVO.clone();
				backupAuth.add(gruppoBK);
			}

			//myForm.setBackupAuth((List<GruppoParametriVO>)elencoAuth.clone());
			myForm.setBackupAuth(backupAuth);
			return mapping.getInputForward();
		}
		else if (elencoAuth == null && myForm.getElencoAuth().size() > 0) {
			return mapping.getInputForward();
		}
		else {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<?> elencoParametri = factory.getBiblioteca().getElencoParametri(codBib);

			List<GruppoParametriVO> authorities = null;
			List<GruppoParametriVO> semantica = null;
			if (elencoParametri != null && elencoParametri.size() == 3) {
				authorities = (List<GruppoParametriVO>)elencoParametri.get(0);
				semantica = (List<GruppoParametriVO>)elencoParametri.get(2);
			}

			for (int i = 0; i < authorities.size(); i++) {
				GruppoParametriVO gruppo = authorities.get(i);
				gruppo.setAcceso("TRUE");
			}

			for (int i = 0; i < semantica.size(); i++) {
				GruppoParametriVO gruppo = semantica.get(i);
				gruppo.setAcceso("TRUE");
			}

			myForm.setElencoAuth(authorities);
			List<GruppoParametriVO> backupAuth = new ArrayList<GruppoParametriVO>();
			for (int t = 0; t < authorities.size(); t++) {
				GruppoParametriVO gruppoVO = authorities.get(t);
				GruppoParametriVO gruppoBK = new GruppoParametriVO();
				gruppoBK = (GruppoParametriVO)gruppoVO.clone();
				backupAuth.add(gruppoBK);
			}
			myForm.setBackupAuth(backupAuth);
			myForm.setElencoSem(semantica);
			backupSem = new ArrayList<GruppoParametriVO>();
			for (int t = 0; t < semantica.size(); t++) {
				GruppoParametriVO gruppoVO = semantica.get(t);
				GruppoParametriVO gruppoBK = new GruppoParametriVO();
				gruppoBK = (GruppoParametriVO)gruppoVO.clone();
				backupSem.add(gruppoBK);
			}
			if (myForm.getBackupSem() == null || myForm.getBackupSem().size() == 0)
				myForm.setBackupSem(backupSem);

			return mapping.getInputForward();
		}
	}

	public ActionForward Back(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneAuthoritiesForm currentForm = (ProfilazioneAuthoritiesForm) form;

		//almaviva5_20100330 #3661 check dati semantici
		//per evitare il salvataggio di profili incompleti
		//si verifica la corretta valorizzazione di tutte le righe
		//di semantica presenti sulle maschere apposite (SO, CL, TH)
		ActionForward check = checkDatiSemantica(mapping, currentForm, request, response);
		if (check != null)
			return check;
		//almaviva5_20111216 #4722 check livelli autorita su biblioteca
		check = checkLivAutPolo(mapping, currentForm.getElencoAuth(), request, response);
		if (check != null)
			return check;
		//

		request.setAttribute("checked", currentForm.getChecked());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY, currentForm.getElencoAuth());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, currentForm.getElencoSem());

		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, currentForm.getElencoMat());

		ActionMessages messaggio = new ActionMessages();
		messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.okauth"));
		this.saveErrors(request,messaggio);
		return mapping.findForward("torna");
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneAuthoritiesForm profilazioneForm = (ProfilazioneAuthoritiesForm) form;
		request.setAttribute("checked", profilazioneForm.getChecked());
		List<GruppoParametriVO> backupAuth = profilazioneForm.getBackupAuth();
		List<GruppoParametriVO> backupSem = profilazioneForm.getBackupSem();
		if (backupAuth.size() > 0)
			request.setAttribute(NavigazioneProfilazione.PARAMETRI_AUTHORITY, backupAuth);
		if (backupSem.size() > 0)
			request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, backupSem);
		//return mapping.findForward("torna");
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, profilazioneForm.getElencoMat());
		request.setAttribute("flagMat", profilazioneForm.isFlagParMat());
		return mapping.findForward("torna");
	}

	public ActionForward Classificazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneAuthoritiesForm myForm = (ProfilazioneAuthoritiesForm) form;
		List<GruppoParametriVO> elencoSem = myForm.getElencoSem();
		if (elencoSem != null) {
			for (int i = 0; i < elencoSem.size(); i++) {
				GruppoParametriVO gruppo = elencoSem.get(i);
				if (!gruppo.getCodice().trim().equals("SCLA")) {
					gruppo.setAcceso("FALSE");
				}
				else
					gruppo.setAcceso("TRUE");
			}
//			if (elencoSem.size() > 0)
				request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, elencoSem);
		}
		request.setAttribute("tipologia", "CLA");
		request.setAttribute("checked", myForm.getChecked());
		request.setAttribute("flagParMat", myForm.isFlagParMat());
		request.setAttribute("nomeBib", myForm.getNomeBiblioteca());
		return Navigation.getInstance(request).goForward(mapping.findForward("semantica"));
	}

	public ActionForward Soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneAuthoritiesForm myForm = (ProfilazioneAuthoritiesForm) form;
		List<GruppoParametriVO> elencoSem = myForm.getElencoSem();
		if (elencoSem != null) {
			for (int i = 0; i < elencoSem.size(); i++) {
				GruppoParametriVO gruppo = elencoSem.get(i);
				if (!gruppo.getCodice().trim().equals("SOGG")) {
					gruppo.setAcceso("FALSE");
				}
				else
					gruppo.setAcceso("TRUE");
			}
//			if (elencoSem.size() > 0)
				request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, elencoSem);
		}
		request.setAttribute("tipologia", "SOGG");
		request.setAttribute("checked", myForm.getChecked());
		request.setAttribute("flagParMat", myForm.isFlagParMat());
		request.setAttribute("nomeBib", myForm.getNomeBiblioteca());
		return Navigation.getInstance(request).goForward(mapping.findForward("semantica"));
	}

	public ActionForward Thesauri(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneAuthoritiesForm myForm = (ProfilazioneAuthoritiesForm) form;
		List<GruppoParametriVO> elencoSem = myForm.getElencoSem();
		if (elencoSem != null) {
			for (int i = 0; i < elencoSem.size(); i++) {
				GruppoParametriVO gruppo = elencoSem.get(i);
				if (!gruppo.getCodice().trim().equals("STHE")) {
					gruppo.setAcceso("FALSE");
				}
				else
					gruppo.setAcceso("TRUE");
			}
//			if (elencoSem.size() > 0)
				request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, elencoSem);
		}
		request.setAttribute("tipologia", "THE");
		request.setAttribute("checked", myForm.getChecked());
		request.setAttribute("flagParMat", myForm.isFlagParMat());
		request.setAttribute("nomeBib", myForm.getNomeBiblioteca());
		return Navigation.getInstance(request).goForward(mapping.findForward("semantica"));
	}

	private ActionForward checkDatiSemantica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneAuthoritiesForm profilazioneForm = (ProfilazioneAuthoritiesForm) form;
		ActionMessages messaggio = new ActionMessages();

		List<GruppoParametriVO> elencoPar = profilazioneForm.getElencoSem();
		if (!ValidazioneDati.isFilled(elencoPar))
			return null;

		for (int i = 0; i < elencoPar.size(); i++) {
			GruppoParametriVO gruppo = elencoPar.get(i);

			for (int z = 0; z < gruppo.getElencoParametri().size(); z++) {
				ParametroVO parametro = gruppo.getElencoParametri().get(z);
				if (ValidazioneDati.equals(parametro.getTipo(), "RADIO")
						&& !ValidazioneDati.isFilled(parametro.getSelezione()) ) {
					messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"profilo.biblioteca.semantica.null2",
							LinkableTagUtils.findMessage(request, getLocale(request), gruppo.getDescrizione() )));
					this.saveErrors(request, messaggio);
					return mapping.getInputForward();
				}

				if (ValidazioneDati.equals(parametro.getTipo(), "CHECK")) {
					boolean trovato = false;
					List<CheckVO> elencoCheck = parametro.getElencoCheck();
					if (ValidazioneDati.isFilled(elencoCheck))
						for (int f = 0; f < elencoCheck.size(); f++) {
							CheckVO ck = elencoCheck.get(f);
							if (ck.isSelezione()) {
								trovato = true;
								break;
							}
					}
					if (!trovato) {
						messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"profilo.biblioteca.semantica.nocheck"));
						this.saveErrors(request, messaggio);
						return mapping.getInputForward();
					}
				}
			}
		}

		return null;	//tutto ok
	}

}
