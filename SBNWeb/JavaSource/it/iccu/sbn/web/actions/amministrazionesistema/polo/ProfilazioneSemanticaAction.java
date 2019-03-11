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
package it.iccu.sbn.web.actions.amministrazionesistema.polo;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.polo.ProfilazioneSemanticaForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public final class ProfilazioneSemanticaAction extends LookupDispatchAction {

    static Logger log = Logger.getLogger(ProfilazioneSemanticaAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("profilo.polo.torna", 				"Back");
		map.put("profilo.polo.annulla", 			"Annulla");
		map.put("profilo.polo.button.aggiungi",		"Aggiungi");
		map.put("profilo.polo.button.rimuovi",		"Rimuovi");
		map.put("profilo.polo.parametri.button.conferma.aggiungi", "Inserisci");
		map.put("profilo.polo.parametri.button.conferma.annulla", "Elimina");
		return map;
	}

	class StringComparator implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		GruppoParametriVO gp = (GruppoParametriVO)a;
		String sa = gp.getCodice();
		gp = (GruppoParametriVO)b;
		String sb = gp.getCodice();
		return( (sa).compareToIgnoreCase( sb )); // Ascending

	   } // end compare
	} // end class StringComparator

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();
		ProfilazioneSemanticaForm myForm = (ProfilazioneSemanticaForm) form;

		Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().PROFILAZIONE_POLO);
        }
        catch (UtenteNotAuthorizedException e) {
            ActionMessages messaggio = new ActionMessages();
            messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messaggio.info.noaut"));
            this.saveErrors(request,messaggio);
            return mapping.findForward("blank");
        }

		myForm.setTipologia((String)request.getAttribute("tipologia"));
		myForm.setCheckedAtt((String[])request.getAttribute("checked"));
		if ((Boolean)request.getAttribute("flagParMat") != null)
			myForm.setFlagParMateriali((Boolean)request.getAttribute("flagParMat"));
		List<GruppoParametriVO> elencoSem = (List<GruppoParametriVO>)request.getAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA);
		myForm.setElencoParSemantica(elencoSem);

		if (elencoSem != null && elencoSem.size() > 0) {
			List<GruppoParametriVO> backup = new ArrayList<GruppoParametriVO>();
			for (int t = 0; t < elencoSem.size(); t++) {
				GruppoParametriVO gruppoVO = elencoSem.get(t);
				GruppoParametriVO gruppoBK = new GruppoParametriVO();
				gruppoBK = (GruppoParametriVO)gruppoVO.clone();
				backup.add(gruppoBK);
			}

			myForm.setBackupParSemantica(backup);
		}
		return mapping.getInputForward();
	}

	public ActionForward Back(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneSemanticaForm profilazioneForm = (ProfilazioneSemanticaForm) form;

		request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, profilazioneForm.getElencoParSemantica());
		request.setAttribute("checked", profilazioneForm.getCheckedAtt());
		request.setAttribute("flagMat", profilazioneForm.isFlagParMateriali());
		ActionMessages messaggio = new ActionMessages();
		messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.oksem"));
		this.saveErrors(request,messaggio);

		return mapping.findForward("torna");
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneSemanticaForm profilazioneForm = (ProfilazioneSemanticaForm) form;
		List<GruppoParametriVO> backupSem = profilazioneForm.getBackupParSemantica();
		if (backupSem.size() > 0)
			request.setAttribute(NavigazioneProfilazione.PARAMETRI_SEMANTICA, backupSem);
		request.setAttribute("checked", profilazioneForm.getCheckedAtt());
		request.setAttribute("flagMat", profilazioneForm.isFlagParMateriali());
		return mapping.findForward("torna");
	}

	public ActionForward Aggiungi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneSemanticaForm myForm = (ProfilazioneSemanticaForm) form;

		List<GruppoParametriVO> elencoSem = myForm.getElencoParSemantica();
		List<String> elencoThe = new ArrayList<String>();
		CodiciType codice = null;
		if (myForm.getTipologia().equals("CLA"))
			codice = CodiciType.CODICE_SISTEMA_CLASSE;
		else if (myForm.getTipologia().equals("SOGG"))
			codice = CodiciType.CODICE_SOGGETTARIO;
		else if (myForm.getTipologia().equals("THE"))
			codice = CodiciType.CODICE_THESAURO;

		List<ComboVO> elencoCombo = new ArrayList<ComboVO>();

		for (int i = 0; i < elencoSem.size(); i++) {
			GruppoParametriVO gruppo = elencoSem.get(i);
			if (gruppo.getAcceso().equals("TRUE")) {
				List<ParametroVO> elencoParametri = gruppo.getElencoParametri();
				for (int r = 0; r < elencoParametri.size(); r++) {
					if (elencoParametri.get(r).getTipo().equals("TESTO"))
						elencoThe.add(elencoParametri.get(r).getSelezione());
				}
			}
		}

		List<TB_CODICI> elencoCodici = CodiciProvider.getCodici(codice, false);

		boolean inserito = false;
		for (int k = 1; k < elencoCodici.size(); k++) {
			TB_CODICI tabCodice = elencoCodici.get(k);
			boolean trovato = false;
			for (int w = 0; w < elencoThe.size(); w++) {
				if (elencoThe.get(w).equals(tabCodice.getDs_tabella())) {
					trovato = true;
					break;
				}
			}
			if (!trovato) {
				ComboVO combo = new ComboVO();
				combo.setDescrizione(tabCodice.getDs_tabella());
				combo.setCodice(tabCodice.getCd_tabella());
				elencoCombo.add(combo);
				inserito = true;
			}
		}
		if (!inserito) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("profilo.polo.parametri.info.noparsem"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}
		myForm.setNuovo("TRUE");
		myForm.setElencoScelteNuovo(elencoCombo);
		return mapping.getInputForward();
	}

	public ActionForward Rimuovi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneSemanticaForm myForm = (ProfilazioneSemanticaForm) form;
		List<GruppoParametriVO> elencoGruppi = myForm.getElencoParSemantica();
		for (int i = 0; i < elencoGruppi.size(); i++) {
			GruppoParametriVO gruppo = elencoGruppi.get(i);
			if (gruppo.getAcceso().equals("TRUE") && gruppo.isSelezioneCheck()) {
				elencoGruppi.remove(i);
				i--;
			}
		}
		for (int i = 0; i < elencoGruppi.size(); i++) {
			GruppoParametriVO gruppo = elencoGruppi.get(i);
			gruppo.setIndice(i +"");
		}
		Collections.sort(elencoGruppi, new StringComparator());
		myForm.setElencoParSemantica(elencoGruppi);
		return mapping.getInputForward();
	}

	public ActionForward Inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ProfilazioneSemanticaForm myForm = (ProfilazioneSemanticaForm) form;
		String selezione = myForm.getSelezioneNuovo();
		List<GruppoParametriVO> elencoGruppi = myForm.getElencoParSemantica();
		String tipologia = myForm.getTipologia();

		GruppoParametriVO gruppo = new GruppoParametriVO();
		gruppo.setAcceso("TRUE");
		gruppo.setSelezioneCheck(true);
		List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();

		CodiciType codice = null;
		String descrizione = "";
		List<String> opzioniRadio = new ArrayList<String>();
		opzioniRadio.add("Si'");
		opzioniRadio.add("No");

		if (tipologia.equals("CLA")) {
			codice = CodiciType.CODICE_SISTEMA_CLASSE;
			descrizione = "profilo.polo.parametri.classificazioni";
		}
		else if (tipologia.equals("SOGG")) {
			codice = CodiciType.CODICE_SOGGETTARIO;
			descrizione = "profilo.polo.parametri.soggetti";
		}
		else if (tipologia.equals("THE")) {
			codice = CodiciType.CODICE_THESAURO;
			descrizione = "profilo.polo.parametri.thesauri";
		}
		gruppo.setCodice(codice.getTp_Tabella());
		gruppo.setDescrizione(descrizione);


		ParametroVO parametro = new ParametroVO();
		if (tipologia.equals("CLA"))
			parametro.setDescrizione("profilo.polo.parametri.classificazione");
		else if (tipologia.equals("SOGG"))
			parametro.setDescrizione("profilo.polo.parametri.soggetto");
		else if (tipologia.equals("THE"))
			parametro.setDescrizione("profilo.polo.parametri.thesauro");
		parametro.setIndex("0");

		List<TB_CODICI> elencoCodici = CodiciProvider.getCodici(codice, false);

		for (int k = 1; k < elencoCodici.size(); k++) {
			TB_CODICI tabCodice = elencoCodici.get(k);
			if (tabCodice.getCd_tabella().trim().equals(selezione.trim())) {
				parametro.setSelezione(tabCodice.getDs_tabella());
			}
		}
		parametro.setTipo("TESTO");
		elencoParametri.add(parametro);

		parametro = new ParametroVO();
		parametro.setDescrizione("profilo.polo.parametri.sololocale");
		parametro.setIndex("1");
		parametro.setRadioOptions(opzioniRadio);
		parametro.setSelezione("No");
		parametro.setTipo("RADIO");
		elencoParametri.add(parametro);

		gruppo.setElencoParametri(elencoParametri);
		elencoGruppi.add(gruppo);

		Collections.sort(elencoGruppi, new StringComparator());
		for (int i = 0; i < elencoGruppi.size(); i++) {
			GruppoParametriVO gruppoSem = elencoGruppi.get(i);
			gruppoSem.setIndice(i +"");
		}

		myForm.setElencoParSemantica(elencoGruppi);
		myForm.setNuovo("FALSE");
		myForm.setSelezioneNuovo(null);
		myForm.setElencoScelteNuovo(new ArrayList<ComboVO>());
		return mapping.getInputForward();
	}

//	public ActionForward Elimina(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		ProfilazioneSemanticaForm myForm = (ProfilazioneSemanticaForm) form;
//		myForm.setNuovo("FALSE");
//		myForm.setSelezioneNuovo(null);
//		myForm.setElencoScelteNuovo(new ArrayList<ComboVO>());
//		return mapping.getInputForward();
//	}

}
