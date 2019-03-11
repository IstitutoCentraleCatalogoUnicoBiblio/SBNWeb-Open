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
package it.iccu.sbn.web.actions.amministrazionesistema;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.DefaultVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.GruppoVO;
import it.iccu.sbn.web.actionforms.amministrazionesistema.gestioneDefault.GestioneDefaultForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.ConstantDefault.EditorType;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.DefaultDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.struts.actions.LookupDispatchAction;

public class GestioneDefaultAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(GestioneDefaultAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.button.annulla", "annulla");
		map.put("profilo.button.salva",	  "salva");
		return map;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	try {
    		Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
    			return mapping.getInputForward();

    		GestioneDefaultForm currentForm = (GestioneDefaultForm) form;
	        UserVO utente = navi.getUtente();
			int idUtente = utente.getIdUtenteProfessionale();
	        String idPolo = utente.getCodPolo();
			String idBiblioteca = utente.getCodBib();

			DefaultDelegate delegate = DefaultDelegate.getInstance(request);
			String idArea = (String)request.getAttribute("area");
			String utilizzo = (String)request.getAttribute("user");
			currentForm.setIdarea(idArea);
			Utente user = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
			List<GruppoVO> listaDefaultUtente = new ArrayList<GruppoVO>();
			if (utilizzo.equals(NavigazioneProfilazione.BIBLIOTECA)) {
				//almaviva5_20140701 #3198
				currentForm.setEditor(EditorType.BIB);
				listaDefaultUtente = delegate.caricaDefaultUtente(request, idUtente, idArea, null, idBiblioteca, idPolo);
			}
			else if (utilizzo.equals(NavigazioneProfilazione.ID_UTENTE)) {
				//almaviva5_20140701 #3198
				currentForm.setEditor(EditorType.USER);
				Map<?, ?> attivita = user.getListaAttivita();
				listaDefaultUtente = delegate.caricaDefaultUtente(request, idUtente, idArea, attivita, idBiblioteca, idPolo);
			}
			if (listaDefaultUtente == null)
				throw new Exception();

			currentForm.setUtilizzo(utilizzo);
			currentForm.setCampi(listaDefaultUtente);
			return mapping.getInputForward();
    	}
    	catch (Exception e) {
    		log.error("Errore durante il recupero dei dati.");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.gestione.default.db"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
    	}
    }

    public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	GestioneDefaultForm myForm = (GestioneDefaultForm) form;
    	String utilizzo = myForm.getUtilizzo();
		if (utilizzo.equals(NavigazioneProfilazione.BIBLIOTECA))
			return mapping.findForward("defaultBiblioteca");
		else
			return mapping.findForward("defaultUtente");
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        try {
			UserVO utente = Navigation.getInstance(request).getUtente();
			int idUtente = utente.getIdUtenteProfessionale();
			GestioneDefaultForm myForm = (GestioneDefaultForm) form;
	        String idPolo = utente.getCodPolo();
			String idBiblioteca = utente.getCodBib();
			List<GruppoVO> campi = myForm.getCampi();
			DefaultDelegate delegate = DefaultDelegate.getInstance(request);
			String utilizzo = myForm.getUtilizzo();
			boolean inserito = false;
			if (utilizzo.equals(NavigazioneProfilazione.ID_UTENTE)) {
				inserito = delegate.salvaDefaultUtente(request, idUtente, campi, "", "");
			}
			else if (utilizzo.equals(NavigazioneProfilazione.BIBLIOTECA)) {
				inserito = delegate.salvaDefaultUtente(request, 0, campi, idBiblioteca, idPolo);
			}
			if (!inserito)
				throw new Exception();

			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.reloadDefault();

			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("gestione.default.salva"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();

		} catch (Exception e){
    		log.error("Errore durante il salvataggio dei dati sulla base dati.");
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.gestione.default.salva"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
    	}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		//almaviva5_20140701 #3198
		if (ValidazioneDati.equals(idCheck, "EDITOR")) {
			GestioneDefaultForm currentForm = (GestioneDefaultForm) form;
			DefaultVO def = (DefaultVO) request.getAttribute("defaultVO");
			ConstantDefault _default = def.get_default();
			if (_default == null)
				return true;

			return Arrays.asList(_default.getEditor()).contains(currentForm.getEditor());
			//Object o = request.getAttribute("defaultVO");
			//log.debug(o.getClass().getSimpleName());
			//return true;
		}
		return false;
	}
}
