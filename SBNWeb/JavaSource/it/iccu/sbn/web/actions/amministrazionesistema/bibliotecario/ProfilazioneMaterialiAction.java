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
package it.iccu.sbn.web.actions.amministrazionesistema.bibliotecario;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario.ProfilazioneMaterialiForm;
import it.iccu.sbn.web.actions.amministrazionesistema.ProfilazioneBaseAction;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

public final class ProfilazioneMaterialiAction extends ProfilazioneBaseAction {

    static Logger log = Logger.getLogger(ProfilazioneMaterialiAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.polo.torna", "Back");
		map.put("profilo.polo.annulla", 	"Annulla");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();
		ProfilazioneMaterialiForm currentForm = (ProfilazioneMaterialiForm) form;

		Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ABILITAZIONI_UTENTE);
            currentForm.setAbilitatoWrite("FALSE");
        }
        catch (UtenteNotAuthorizedException e) {
            ActionMessages messaggio = new ActionMessages();
            messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messaggio.info.noaut"));
            this.saveErrors(request,messaggio);
            return mapping.findForward("blank");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_UTENTE);
            currentForm.setAbilitatoWrite("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoWrite("FALSE");
        }

		currentForm.setNomeUt((String)request.getAttribute(NavigazioneProfilazione.NOME_UTENTE));
		currentForm.setNomeUsername((String)request.getAttribute(NavigazioneProfilazione.USERNAME));
		currentForm.setNomeBiblio((String)request.getAttribute(NavigazioneProfilazione.BIBLIOTECA));
		String codBib = (String) request.getAttribute(NavigazioneProfilazione.COD_BIBLIOTECA);
        if (ValidazioneDati.isFilled(codBib))
        	currentForm.setCodBiblioteca(codBib);

		currentForm.setChecked((String[])request.getAttribute("checked"));
		List<GruppoParametriVO> elencoMat = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI);
		String idUtente = (String)request.getAttribute(NavigazioneProfilazione.ID_UTENTE);

		if (request.getAttribute("flagAuth") != null)
			currentForm.setFlagParAuth((Boolean)request.getAttribute("flagAuth"));

		if (elencoMat.size() > 0) {
			currentForm.setElencoParMat(elencoMat);
			List<GruppoParametriVO> backup = new ArrayList<GruppoParametriVO>();
			for (int t = 0; t < elencoMat.size(); t++) {
				GruppoParametriVO gruppoVO = elencoMat.get(t);
				GruppoParametriVO gruppoBK = new GruppoParametriVO();
				gruppoBK = (GruppoParametriVO)gruppoVO.clone();
				backup.add(gruppoBK);
			}

			currentForm.setBackupParMat(backup);
			return mapping.getInputForward();
		}
		else {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			List<?> elencoParametri = factory.getBibliotecario().getElencoParametri(idUtente);

			List<GruppoParametriVO> materiali = null;
			if (elencoParametri != null && elencoParametri.size() == 3) {
				materiali = (List<GruppoParametriVO>)elencoParametri.get(1);
			}

			for (int i = 0; i < materiali.size(); i++) {
				GruppoParametriVO gruppo = materiali.get(i);
				gruppo.setAcceso("TRUE");
			}

			currentForm.setElencoParMat(materiali);


			return mapping.getInputForward();
		}
	}

	public ActionForward Back(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneMaterialiForm currentForm = (ProfilazioneMaterialiForm) form;

		//almaviva5_20111207 #4722 check livelli autorita su biblioteca
		ActionForward check = checkLivAutBiblioteca(mapping,
				currentForm.getCodBiblioteca(), currentForm.getElencoParMat(),
				request, response);
		if (check != null)
			return check;

		request.setAttribute("checked", currentForm.getChecked());
		request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, currentForm.getElencoParMat());
		request.setAttribute("flagAuth", currentForm.isFlagParAuth());
		//almaviva5_20140702
		request.setAttribute(NavigazioneProfilazione.METERIALI_OK, "ok");



//		ProfilazioneTreeElementView ptew = profilazioneForm.getProfilazioneTreeElementView();
//		if (profilazioneForm.getRadioItemSelez() != null && !(profilazioneForm.getRadioItemSelez().equals(""))) {
//			TreeElementView findElementView = ptew.findElementUnique(Integer.parseInt(profilazioneForm.getRadioItemSelez()));
//			request.setAttribute("radioselected", findElementView.getSerialId() + "");
//		}
//		else
//			request.setAttribute("radioselected", "");
//		String[] checked = profilazioneForm.getCheckItemSelez();
//		if (checked != null)
//			for (int i= 0; i < checked.length; i++) {
//				TreeElementView findElementView = ptew.findElementUnique(Integer.parseInt(checked[i]));
//				checked[i] = findElementView.getSerialId() + "";
//			}
//		request.setAttribute("checked", checked);
//		profilazioneForm.getProfilazioneTreeElementView().closeTree();
//		profilazioneForm.getProfilazioneTreeElementView().setParent(profilazioneForm.getOldParent());
		ActionMessages messaggio = new ActionMessages();
		messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.okmat"));
		this.saveErrors(request,messaggio);

		return mapping.findForward("torna");
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneMaterialiForm profilazioneForm = (ProfilazioneMaterialiForm) form;
		request.setAttribute("checked", profilazioneForm.getChecked());
		List<GruppoParametriVO> backupMat = profilazioneForm.getBackupParMat();
		if (backupMat.size() > 0)
			request.setAttribute(NavigazioneProfilazione.PARAMETRI_MATERIALI, backupMat);
		request.setAttribute("flagAuth", profilazioneForm.isFlagParAuth());
		return mapping.findForward("torna");
	}

}
