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
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO.BibliotecaType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO.RicercaBibliotecaType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.RicercaBibliotecaForm;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public final class RicercaBibliotecaAction extends LookupDispatchAction implements SbnAttivitaChecker {

    static Logger log = Logger.getLogger(RicercaBibliotecaAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.biblioteca.button.cerca", 			"cerca");
		map.put("ricerca.biblioteca.button.nuovo", 			"nuovo");
		map.put("ricerca.biblioteca.button.reset", 			"reset");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaBibliotecaForm currentForm = (RicercaBibliotecaForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

        Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_BIBLIOTECA);
        }
        catch (UtenteNotAuthorizedException e) {
            ActionMessages messaggio = new ActionMessages();
            messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messaggio.info.noaut"));
            this.saveErrors(request,messaggio);
            return mapping.findForward("blank");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_BIBLIOTECA);
            currentForm.setAbilitatoNuovo("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoNuovo("FALSE");
        }

        preparaCombo(currentForm);

		currentForm.setSelezioneDug("via");
		currentForm.setCheckBibInPolo(BibliotecaType.TUTTE.name() );

		String caller = navi.getActionCaller();
		if (ValidazioneDati.equals(caller, "/acquisizioni/fornitori/inserisciFornitore")) {
			currentForm.setAcquisizioni("FORNITORE");
			// almaviva5_20111202 #4756
			// currentForm.setSelezioneFlagBib(RicercaBibliotecaType.TUTTE.name());
			currentForm.setSelezioneFlagBib(null);
		} else if (ValidazioneDati.equals(caller, "/acquisizioni/fornitori/esaminaFornitore")) {
			currentForm.setAcquisizioni("ESAMINA");
			// almaviva5_20111202 #4756
			// currentForm.setSelezioneFlagBib(RicercaBibliotecaType.TUTTE.name());
			currentForm.setSelezioneFlagBib(null);
		} else if (ValidazioneDati.equals(caller, "/documentofisico/elaborazioniDifferite/scaricoInventariale")) {
			currentForm.setScaricoInventariale("scaricoInventariale");
		} else if (ValidazioneDati.equals(caller, "/documentofisico/datiInventari/modificaInvColl")) {
			currentForm.setScaricoInventariale("modificaInvColl");
		} else if (ValidazioneDati.equals(caller, "/servizi/utenti/DettaglioUtentiAna")) {
			currentForm.setServizioUtente("UTENTE");
			currentForm.setSelezioneFlagBib(null);
		} else if (ValidazioneDati.equals(caller, "/servizi/erogazione/ListaMovimenti")) {
			currentForm.setServizioUtente("LISTA_MOVIMENTI");
			BibliotecaRicercaVO richiesta = (BibliotecaRicercaVO) navi.getAttribute(BibliotecaDelegate.ATTIVAZIONE_SIF);
			if (richiesta != null)
				currentForm.setRicerca(richiesta);

			currentForm.setSelezioneFlagBib(null);
		} else if (ValidazioneDati.equals(caller, "/servizi/erogazione/erogazioneRicercaILL")) {
			currentForm.setServizioUtente("RICERCA_ILL");
			BibliotecaRicercaVO richiesta = (BibliotecaRicercaVO) navi.getAttribute(BibliotecaDelegate.ATTIVAZIONE_SIF);
			if (richiesta != null)
				currentForm.setRicerca(richiesta);

			currentForm.setSelezioneFlagBib(null);
		} else {
			currentForm.setAcquisizioni("FALSE");
			currentForm.setScaricoInventariale("FALSE");
		}


		currentForm.setCheckIndirizzo("inizio");
		currentForm.setCheckNome("inizio");

		return mapping.getInputForward();
	}

	private void preparaCombo(RicercaBibliotecaForm currentForm) throws Exception {

		List<ComboVO> elencoTipi = new ArrayList<ComboVO>();
		List<TB_CODICI> elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_TIPI_BIBLIOTECA, false);
		for (int k = 0; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella());
			combo.setDescrizione(elencoCodici.get(k).getDs_tabella());
			elencoTipi.add(combo);
		}
		currentForm.setElencoTipiBib(elencoTipi);

		List<ComboVO> elencoProvince = new ArrayList<ComboVO>();
		elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE, false);
		for (int k = 0; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella());
			combo.setDescrizione(elencoCodici.get(k).getCd_tabella() + " " + elencoCodici.get(k).getDs_tabella());
			elencoProvince.add(combo);
		}
		currentForm.setElencoProvince(elencoProvince);

		List<TB_CODICI> listaCodiciSistMetro = CodiciProvider.getCodici(CodiciType.CODICE_SISTEMA_METROPOLITANO, true);
		currentForm.setListaCodiciSistMetro(listaCodiciSistMetro);

		List<ComboVO> elencoTipoBib = new ArrayList<ComboVO>();
		elencoTipoBib.add(new ComboVO(BibliotecaType.TUTTE.name(), ""));
		elencoTipoBib.add(new ComboVO(BibliotecaType.SBN.name(), "SBN"));
		elencoTipoBib.add(new ComboVO(BibliotecaType.NON_SBN.name(), "Non SBN"));
		currentForm.setElencoTipoBibSBN(elencoTipoBib);

		List<ComboVO> elencoFlag = new ArrayList<ComboVO>();
		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources.AmministrazioneSistemaLabels");
		elencoFlag.add(new ComboVO(" ", ""));
		elencoFlag.add(new ComboVO(RicercaBibliotecaType.TUTTE.name(), bundle.getString("ricerca.biblioteca.tutte")));
		elencoFlag.add(new ComboVO(RicercaBibliotecaType.NON_ABILITATE.name(), bundle.getString("ricerca.biblioteca.flag.abilitata.no")));
		elencoFlag.add(new ComboVO(RicercaBibliotecaType.ABILITATE.name(), bundle.getString("ricerca.biblioteca.flag.abilitata.si")));
		elencoFlag.add(new ComboVO(RicercaBibliotecaType.CENTRO_SISTEMA.name(), bundle.getString("ricerca.biblioteca.flag.c")));
		elencoFlag.add(new ComboVO(RicercaBibliotecaType.AFFILIATE.name(), bundle.getString("ricerca.biblioteca.flag.affiliata")));
		currentForm.setElencoFlagBib(elencoFlag);

		ComboVO combo = new ComboVO();
		List<ComboVO> elencoOrdinamenti = new ArrayList<ComboVO>();
		combo = new ComboVO();
		combo.setCodice("codiceana");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.cdana"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("codicebib");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.cdbib"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("nome");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.nome"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("indirizzo");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.indirizzo"));
		elencoOrdinamenti.add(combo);
		/* //almaviva5_20100202 #3492
		combo = new ComboVO();
		combo.setCodice("cap");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.cap"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("citta");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.localita"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("provincia");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.provincia"));
		elencoOrdinamenti.add(combo);
		*/
		combo = new ComboVO();
		combo.setCodice("polo");
		combo.setDescrizione(bundle.getString("nuovo.biblioteca.polo"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("tipobib");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.tipo"));
		elencoOrdinamenti.add(combo);
		combo = new ComboVO();
		combo.setCodice("flag");
		combo.setDescrizione(bundle.getString("ricerca.biblioteca.flag"));
		elencoOrdinamenti.add(combo);
		currentForm.setElencoOrdinamenti(elencoOrdinamenti);
		currentForm.setSelezioneOrdinamento("codicebib");

		List<ComboVO> elencoDug = new ArrayList<ComboVO>();
		elencoDug.add(new ComboVO("Via", "Via"));
		elencoDug.add(new ComboVO("Piazza", "Piazza"));
		elencoDug.add(new ComboVO("Piazzale", "Piazzale"));
		elencoDug.add(new ComboVO("Piazzetta", "Piazzetta"));
		elencoDug.add(new ComboVO("Viale", "Viale"));
		elencoDug.add(new ComboVO("Largo", "Largo"));
		elencoDug.add(new ComboVO("Circonvallazione", "C.ne"));
		elencoDug.add(new ComboVO("Vicolo", "Vicolo"));
		elencoDug.add(new ComboVO("Corso", "Corso"));
		elencoDug.add(new ComboVO("Strada", "Strada"));
		currentForm.setElencoDug(elencoDug);

	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (request.getAttribute("back") != null) {
			this.unspecified(mapping, form, request, response);
			return mapping.getInputForward();
		}
		RicercaBibliotecaForm currentForm = (RicercaBibliotecaForm)form;

		BibliotecaRicercaVO richiesta = currentForm.getRicerca();

		String codiceAna = currentForm.getCodiceAnaRic().trim();
		richiesta.setCodiceAna(codiceAna);

		String codiceBib = currentForm.getCodiceBibRic().trim();
		if (!codiceBib.equals(""))
			codiceBib = " " + codiceBib;
		richiesta.setCodiceBib(codiceBib);

		String codicePolo = currentForm.getCodicePoloRic().trim();
		richiesta.setCodicePolo(codicePolo);

		if (!codiceBib.equals("") && codicePolo.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("ricerca.biblioteca.codice.null"));
			return mapping.getInputForward();
		}

		codiceBib = " " + codiceBib;

		String nome = currentForm.getNomeRic().trim();
		if (currentForm.getCheckNome().equals("inizio"))
			nome = nome + "*";
		else if (currentForm.getCheckNome().equals("parole"))
			nome = nome + "%";
		richiesta.setNome(nome);

		String indirizzo = currentForm.getIndirizzoRic().trim();
		if (!indirizzo.equals(""))
			indirizzo = (currentForm.getSelezioneDug() + " " + indirizzo).trim();
		if (currentForm.getCheckIndirizzo().equals("inizio"))
			indirizzo = indirizzo + "*";
		else if (currentForm.getCheckIndirizzo().equals("parole"))
			indirizzo = indirizzo + "%";
		richiesta.setIndirizzo(indirizzo);

		String cap = currentForm.getCapRic();
		if (!currentForm.isCheckEsattaCap())
			cap = cap + "*";
		richiesta.setCap(cap);

		String citta = currentForm.getCittaRic().trim();
		if (!currentForm.isCheckEsattaCitta())
			citta = citta + "*";
		richiesta.setCitta(citta);

		String provincia = currentForm.getSelezioneProvincia().trim();
		richiesta.setProvincia(provincia);

		String tipoBib = currentForm.getSelezioneTipoBib();
		richiesta.setTipoBib(tipoBib);

		String flagBib = currentForm.getSelezioneFlagBib();
		richiesta.setFlagSbn(ValidazioneDati.isFilled(flagBib) ? RicercaBibliotecaType.valueOf(flagBib) : null);

		richiesta.setCodSistemaMetro(currentForm.getCodSistemaMetro());

		String bibinpolo = currentForm.getCheckBibInPolo();
		if (ValidazioneDati.isFilled(bibinpolo))
			richiesta.setBibinpolo(BibliotecaType.valueOf(bibinpolo));

		String ordinamento = currentForm.getSelezioneOrdinamento();
		richiesta.setOrdinamento(ordinamento);
		if (currentForm.getAcquisizioni() != null && !currentForm.getAcquisizioni().equals("FALSE"))
			request.setAttribute("acquisizioni", currentForm.getAcquisizioni());

		if (currentForm.getServizioUtente() != null && !currentForm.getServizioUtente().equals("FALSE"))
			request.setAttribute("servizioUtente", currentForm.getServizioUtente());


		if (currentForm.getScaricoInventariale() != null && currentForm.getScaricoInventariale().equals("scaricoInventariale")){
			request.setAttribute("scaricoInventariale", "scaricoInventariale");
		}
		if (currentForm.getScaricoInventariale() != null && currentForm.getScaricoInventariale().equals("modificaInvColl")){
			request.setAttribute("modificaInvColl", "modificaInvColl");
		}

		request.setAttribute(BibliotecaDelegate.PARAMETRI_RICERCA_BIBLIOTECA, richiesta);
		return mapping.findForward("sintetica");
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.setAttribute("from", "ricerca");
		return mapping.findForward("nuovo");
	}

	public ActionForward reset(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaBibliotecaForm myForm = (RicercaBibliotecaForm) form;
		myForm.setCapRic("");
		myForm.setCheckBibInPolo("tutte");
		myForm.setCodicePoloRic("");
		myForm.setCheckEsattaCap(false);
		myForm.setCheckEsattaCitta(false);
		myForm.setCheckIndirizzo("inizio");
		myForm.setCheckNome("inizio");
		myForm.setCittaRic("");
		myForm.setCodiceAnaRic("");
		myForm.setCodiceBibRic("");
		myForm.setIndirizzoRic("");
		myForm.setNomeRic("");
		myForm.setSelezioneDug("via");
		myForm.setSelezioneFlagBib("");
		myForm.setSelezioneOrdinamento("codicebib");
		myForm.setSelezioneProvincia("");
		myForm.setSelezioneTipoBib("");

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		if (ValidazioneDati.equals(idCheck, "SERVIZI_ILL")) {
		/*
			RicercaBibliotecaForm currentForm = (RicercaBibliotecaForm) form;
			return currentForm.getAbilitatoNuovo().equals("TRUE")
					&& currentForm.getServizioUtente().equals("LISTA_MOVIMENTI");
		*/
			return false;
		}
		return false;
	}

}
