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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.mail.EmailValidator;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.NuovaBibliotecaForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public final class NuovaBibliotecaAction extends LookupDispatchAction implements SbnAttivitaChecker {


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("nuovo.biblioteca.salva", "salva");
		map.put("nuovo.biblioteca.annulla", "annulla");
		map.put("nuovo.biblioteca.abilitazioni", "profilo");

		// almaviva5_20160620 servizi ILL
		map.put("ricerca.biblioteca.button.scegli", "scegli");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		NuovaBibliotecaForm currentForm = (NuovaBibliotecaForm) form;

        Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_BIBLIOTECA);
            currentForm.setAbilitatoNuovo("FALSE");
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
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_BIBLIOTECA);
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_BIBLIOTECA);
            currentForm.setAbilitatoProfilo("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	currentForm.setAbilitatoProfilo("FALSE");
        }

		String idBiblioteca = (String)request.getAttribute(NavigazioneProfilazione.BIBLIOTECA);
		currentForm.setProvenienza((String)request.getAttribute("provenienza"));

		init(currentForm);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		currentForm.setCheckFlag("C");

		currentForm.setAbilitato("FALSE");
		currentForm.setInPolo("FALSE");

		if (!ValidazioneDati.isFilled(idBiblioteca) ) {
			currentForm.setSalvato(false);
			currentForm.setNuovo("TRUE");
			currentForm.setPaese("IT");
			currentForm.setCheckFlag("S");
		}
		else {
			BibliotecaVO biblioteca = factory.getSistema().caricaBiblioteca(Integer.parseInt(idBiblioteca));
			currentForm.setBiblioteca(biblioteca);
			if (biblioteca.getCd_ana_biblioteca() != null)
				currentForm.setCodAna(biblioteca.getCd_ana_biblioteca());
			if (biblioteca.getCod_bib() != null)
				currentForm.setCodBib(biblioteca.getCod_bib());
			if (biblioteca.getCod_polo() != null)
				currentForm.setPolo(biblioteca.getCod_polo());
			currentForm.setNome(biblioteca.getNom_biblioteca());
			currentForm.setUnitaOrganizzativa(biblioteca.getUnit_org());
			currentForm.setSelGruppo(biblioteca.getGruppo());
			//almaviva5_20091015
			currentForm.setCodSistemaMetro(biblioteca.getCodSistemaMetropolitano());
			String indirizzo = biblioteca.getIndirizzo();
			String dug = "";
			for (int y = 0; y < indirizzo.length(); y++) {
				char u = indirizzo.charAt(y);
				if (u == ' ') {
					dug = indirizzo.substring(0, y);
					if (dug.equals("Via") || dug.equals("Viale") || dug.equals("Piazza")
							|| dug.equals("Piazzale") || dug.equals("Circonvallazione") || dug.equals("Largo") || dug.equals("Vicolo")) {
						indirizzo = indirizzo.substring(y+1);
					}
					break;
				}
			}
			currentForm.setSelDug(dug);
			currentForm.setIndirizzo(indirizzo);
			if (biblioteca.getCpostale() != null)
				currentForm.setCasPostale(biblioteca.getCpostale());
			currentForm.setCap(biblioteca.getCap());
			currentForm.setCitta(biblioteca.getLocalita());
			currentForm.setPaese(biblioteca.getPaese());
			currentForm.setSelProvincia(biblioteca.getProvincia());
			if (biblioteca.getTelefono() != null) {
				String telefono = biblioteca.getTelefono();
				String prefisso = "";
				for (int y=0; y<telefono.length(); y++) {
					char u = telefono.charAt(y);
					if (u == ' ') {
						prefisso = telefono.substring(0, y);
						if (prefisso.length() > 7)
							prefisso = "";
						else
							telefono = telefono.substring(y+1);
						break;
					}
				}
				currentForm.setTelefono(telefono);
				currentForm.setPrefissoTel(prefisso);
			}
			if (biblioteca.getFax() != null) {
				String telefono = biblioteca.getFax();
				String prefisso = "";
				for (int y=0; y<telefono.length(); y++) {
					char u = telefono.charAt(y);
					if (u == ' ') {
						prefisso = telefono.substring(0, y);
						if (prefisso.length() > 7)
							prefisso = "";
						else
							telefono = telefono.substring(y+1);
						break;
					}
				}
				currentForm.setFax(telefono);
				currentForm.setPrefissoFax(prefisso);
			}
			if (biblioteca.getE_mail() != null)
				currentForm.setEmail(biblioteca.getE_mail());
			if (biblioteca.getP_iva() != null)
				currentForm.setIva(biblioteca.getP_iva());
			if (biblioteca.getCod_fiscale() != null)
				currentForm.setCodFiscale(biblioteca.getCod_fiscale());
			if (biblioteca.getNote() != null)
				currentForm.setNote(biblioteca.getNote());
			currentForm.setSelTipoBib(biblioteca.getTipo_biblioteca());
			String flag = biblioteca.getFlag_bib();
			if (flag.equals("C") || flag.equals("A") || flag.equals("D")) {
				currentForm.setInPolo("TRUE");
				currentForm.setCheckFlag("S");
				currentForm.setCheckCentro(flag);
				currentForm.setSelBibCentroSistema(biblioteca.getCod_bib_cs());
			}
			else
				currentForm.setCheckFlag(flag);
			currentForm.setId(biblioteca.getIdBiblioteca() + "");
			currentForm.setNuovo("FALSE");
			if (biblioteca.isAbilitata())
				currentForm.setAbilitato("TRUE");
			currentForm.setSalvato(true);

			navi.setTesto(".amministrazionesistema.modificaBiblioteca.testo");
			navi.setDescrizioneX(".amministrazionesistema.modificaBiblioteca.descrizione");

			BibliotecaILLVO bibILL = biblioteca.getBibliotecaILL();
			currentForm.setAbilitataILL(!bibILL.isNuovo());
		}

		return mapping.getInputForward();
	}

	private void init(NuovaBibliotecaForm currentForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

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

		List<ComboVO> elencoProvince = new ArrayList<ComboVO>();
		List<TB_CODICI> elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE, false);
		for (int k = 0; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella().trim());
			combo.setDescrizione(elencoCodici.get(k).getCd_tabella() + " " + elencoCodici.get(k).getDs_tabella());
			elencoProvince.add(combo);
		}
		currentForm.setElencoProvince(elencoProvince);

		List<ComboVO> elencoPaesi = new ArrayList<ComboVO>();
		elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_PAESE, false);

		for (int k = 1; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella().trim());
			combo.setDescrizione(elencoCodici.get(k).getCd_tabella() + " " + elencoCodici.get(k).getDs_tabella());
			elencoPaesi.add(combo);
		}
		currentForm.setElencoPaesi(elencoPaesi);

		List<BibliotecaVO> elencoBibCentroSistema = factory.getSistema().getBibliotecheCentroSistema();
		List<ComboVO> elencoBibCs = new ArrayList<ComboVO>();
		for (int i = 0; i < elencoBibCentroSistema.size(); i++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoBibCentroSistema.get(i).getCod_bib());
			combo.setDescrizione(elencoBibCentroSistema.get(i).getNom_biblioteca());
			elencoBibCs.add(combo);
		}
		currentForm.setElencoBibCentroSistema(elencoBibCs);

		List<ComboVO> elencoTipi = new ArrayList<ComboVO>();
		elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_TIPI_BIBLIOTECA, false);

		for (int k = 1; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella().trim());
			combo.setDescrizione(elencoCodici.get(k).getDs_tabella());
			elencoTipi.add(combo);
		}
		currentForm.setElencoTipiBib(elencoTipi);

		List<ComboVO> elencoGruppi = new ArrayList<ComboVO>();
		elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_ATENEI, false);

		for (int k = 0; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella().trim());
			combo.setDescrizione(elencoCodici.get(k).getDs_tabella());
			elencoGruppi.add(combo);
		}
		currentForm.setElencoGruppi(elencoGruppi);

		// almaviva5_20091015
		List<ComboVO> elencoMetro = new ArrayList<ComboVO>();
		elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_SISTEMA_METROPOLITANO, true);

		for (int k = 0; k < elencoCodici.size(); k++) {
			ComboVO combo = new ComboVO();
			combo.setCodice(elencoCodici.get(k).getCd_tabella().trim());
			combo.setDescrizione(elencoCodici.get(k).getDs_tabella());
			elencoMetro.add(combo);
		}
		currentForm.setElencoMetro(elencoMetro);

		List<ComboVO> listaAdesioneILL = new ArrayList<ComboVO>();
		listaAdesioneILL.add(new ComboVO(" ", "no"));
		listaAdesioneILL.add(new ComboVO("N", "nazionale"));
		listaAdesioneILL.add(new ComboVO("I", "internazionale"));
		listaAdesioneILL.add(new ComboVO("E", "entrambe"));
		currentForm.setListaAdesioneILL(listaAdesioneILL);

		List<ComboVO> listaRuoloILL = new ArrayList<ComboVO>();
		listaRuoloILL.add(new ComboVO("E", "entrambe"));
		listaRuoloILL.add(new ComboVO("R", "richiedente"));
		listaRuoloILL.add(new ComboVO("D", "fornitrice"));
		currentForm.setListaRuoloILL(listaRuoloILL);
	}

	private boolean isNumber(String s) {
		 String validChars = "0123456789";
		 boolean isNumber = true;
		 for (int i = 0; i < s.length() && isNumber; i++) {
			 char c = s.charAt(i);
			 if (validChars.indexOf(c) == -1)
				 isNumber = false;
			 else
				 isNumber = true;
		 }
		 return isNumber;
	 }

	 private boolean isEmail(String email) {
		return EmailValidator.getInstance().validate(email);
	 }

	public ActionForward salva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		NuovaBibliotecaForm currentForm = (NuovaBibliotecaForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		String polo = ValidazioneDati.trimOrEmpty(currentForm.getPolo()).toUpperCase();
		String codBib = ValidazioneDati.trimOrEmpty(currentForm.getCodBib()).toUpperCase();
		String codAna = ValidazioneDati.trimOrEmpty(currentForm.getCodAna()).toUpperCase();

		if (!ValidazioneDati.isFilled(codAna) ) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.codici.ana"));
			return mapping.getInputForward();
		}

		if (!(polo.equals("") && codBib.equals(""))) {
			if (polo.length() != 3) {
				LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.dimensione"));

				return mapping.getInputForward();
			}

			if (codBib.length() != 2) {
				LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.codbib"));

				return mapping.getInputForward();
			}

			if (!polo.equals("") ) {
				if (codBib.trim().equals("") )  {
					LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.codici.bib"));

					return mapping.getInputForward();
				}
				if (codAna.trim().equals("") ) {
					LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.codici.ana"));

					return mapping.getInputForward();
				}
			}

			codBib = ValidazioneDati.fillLeft(codBib, ' ', 3);
		}
		/*//almaviva5_20120319 #4842
		else if (!codAna.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.dimensione"));

			return mapping.getInputForward();
		}
		*/
		String nome = currentForm.getNome().trim();
		if (nome.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.nome"));

			return mapping.getInputForward();
		}
		/*
		for (int y=0; y < nome.length(); y++) {
			char carattere = nome.charAt(y);
			if (y == 0)
				nome = nome.substring(0, 1).toUpperCase() + nome.substring(y+1, nome.length());
			else if (carattere == ' ') {
				nome = nome.substring(0, y + 1) + nome.substring(y+1, y+2).toUpperCase() + nome.substring(y+2, nome.length());
			}
		}
		*/
		String unita = currentForm.getUnitaOrganizzativa().trim();
//		if (unita.equals("")) {
//			ActionMessages messaggio = new ActionMessages();
//			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("nuovo.biblioteca.unita"));
//			this.saveErrors(request,messaggio);
//			return mapping.getInputForward();
//		}

		String gruppo = currentForm.getSelGruppo();

		String indirizzo = currentForm.getIndirizzo().trim().toLowerCase();
		if (indirizzo.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.indirizzo"));

			return mapping.getInputForward();
		}
		/*
		for (int y=0; y < indirizzo.length(); y++) {
			char carattere = indirizzo.charAt(y);
			if (y == 0)
				indirizzo = indirizzo.substring(0, 1).toUpperCase() + indirizzo.substring(y+1, indirizzo.length());
			else if (carattere == ' ') {
				indirizzo = indirizzo.substring(0, y + 1) + indirizzo.substring(y+1, y+2).toUpperCase() + indirizzo.substring(y+2, indirizzo.length());
			}
		}
		*/
		indirizzo = currentForm.getSelDug() + " " + indirizzo;

		String paese = currentForm.getPaese();

		String cap = currentForm.getCap().trim();

		if (cap.equals("") && paese.equals("IT")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.cap.obbligo"));

			return mapping.getInputForward();
		}
		if (!cap.equals("") && !this.isNumber(cap)) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.cap"));

			return mapping.getInputForward();
		}

		String provincia = currentForm.getSelProvincia();

		if (paese.equals("IT") && provincia.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.provincia.obbligo"));

			return mapping.getInputForward();
		}

		String citta = currentForm.getCitta().trim().toUpperCase();
		if (citta.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.localita"));

			return mapping.getInputForward();
		}

		String note = currentForm.getNote();
		if (note.length() > 160) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.note"));

			return mapping.getInputForward();
		}

		String telefono = currentForm.getTelefono().trim();
		String prefissoTel = currentForm.getPrefissoTel().trim();
		if ( (!prefissoTel.equals("") && telefono.equals("")) || (!prefissoTel.equals("") && !this.isNumber(prefissoTel))
				|| (!telefono.equals("") && !this.isNumber(telefono)) ) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.telefono"));

			return mapping.getInputForward();
		}
		telefono = prefissoTel + " " + telefono;

		String fax = currentForm.getFax().trim();
		String prefissoFax = currentForm.getPrefissoFax().trim();
		if ( (!prefissoFax.equals("") && fax.equals("")) || (!prefissoFax.equals("") && !this.isNumber(prefissoFax))
				|| (!fax.equals("") && !this.isNumber(fax)) ) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.fax"));

			return mapping.getInputForward();
		}
		fax = prefissoFax + " " + fax;

		String email = currentForm.getEmail();
		if (!email.equals("") && !isEmail(email)) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.email"));

			return mapping.getInputForward();
		}

		String flag = currentForm.getCheckFlag();
		String codPoloCorrente = factory.getSistema().getCodicePoloCorrente().toUpperCase();
//		if (flag.equals("C") && !codPoloCorrente.equals(polo)) {
//			ActionMessages messaggio = new ActionMessages();
//			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("nuovo.biblioteca.nocs"));
//			this.saveErrors(request,messaggio);
//			return mapping.getInputForward();
//		}
		if (flag.equals("N") && codPoloCorrente.equals(polo)) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.nosbn"));
			return mapping.getInputForward();
		}
		if (flag.equals("S") && polo.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.sbn"));
			return mapping.getInputForward();
		}
//		if (flag.equals("A") && !codPoloCorrente.equals(polo)) {
//			ActionMessages messaggio = new ActionMessages();
//			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("nuovo.biblioteca.noaffiliata"));
//			this.saveErrors(request,messaggio);
//			return mapping.getInputForward();
//		}
		String codiceCs = " ";
		if (flag.equals("S") && currentForm.getNuovo().equals("TRUE")) {
			flag = "C";
			codiceCs = codBib;
		}

		if (flag.equals("S") && currentForm.getNuovo().equals("FALSE")) {
			codiceCs = currentForm.getSelBibCentroSistema();
			if (codiceCs.equals(currentForm.getCodBib()))
				flag = "C";
			else
				flag = "A";
		}

		String casellaPostale = currentForm.getCasPostale().trim().toUpperCase();

		String iva = currentForm.getIva().trim().toUpperCase();
		String codFiscale = currentForm.getCodFiscale().trim().toUpperCase();
		String tipo = currentForm.getSelTipoBib();

		boolean abilita = false;
		if (currentForm.getAbilitato().equals("TRUE"))
			abilita = true;

//		if (codPoloCorrente.equals(polo))
//			abilita = true;

		String utenteInseritore = Navigation.getInstance(request).getUtente().getFirmaUtente();

		BibliotecaVO biblioteca = currentForm.getBiblioteca() != null ? currentForm.getBiblioteca() : new BibliotecaVO();
		biblioteca.setCd_ana_biblioteca(codAna);
		biblioteca.setCod_bib(codBib);
		biblioteca.setCod_polo(polo);
		biblioteca.setNom_biblioteca(nome);
		biblioteca.setUnit_org(unita);
		biblioteca.setGruppo(gruppo);
		biblioteca.setIndirizzo(indirizzo);
		biblioteca.setCpostale(casellaPostale);
		biblioteca.setCap(cap);
		biblioteca.setLocalita(citta);
		biblioteca.setTelefono(telefono);
		biblioteca.setFax(fax);
		biblioteca.setNote(note);
		biblioteca.setP_iva(iva);
		biblioteca.setCod_fiscale(codFiscale);
		biblioteca.setE_mail(email);
		biblioteca.setTipo_biblioteca(tipo);
		biblioteca.setPaese(paese);
		biblioteca.setProvincia(provincia);
		biblioteca.setCod_bib_cs(codiceCs);
		biblioteca.setFlCanc("N");
		biblioteca.setChiave_bib(" ");
		biblioteca.setChiave_ente(" ");
		biblioteca.setFlag_bib(flag);
		//almaviva5_20091015
		biblioteca.setCodSistemaMetropolitano(currentForm.getCodSistemaMetro() );
		biblioteca.setTsVar(DaoManager.now());

		//servizi ILL
		impostaDatiBibILL(currentForm, biblioteca);

		BibliotecaVO bibReturn = new BibliotecaVO();
		try {
			if (!currentForm.isSalvato())
				bibReturn = factory.getSistema().creaBiblioteca(biblioteca, utenteInseritore, false, abilita, codPoloCorrente);
			else {
				biblioteca.setIdBiblioteca(Integer.parseInt(currentForm.getId()));
				bibReturn = factory.getSistema().creaBiblioteca(biblioteca, utenteInseritore, true, abilita, codPoloCorrente);
			}

			//almaviva5_20160621 servizi ILL
			if (Navigation.getInstance(request).bookmarkExists(Bookmark.Servizi.LISTA_MOVIMENTI)) {
				//se si sta creando una biblioteca per erogare un servizio ILL
				//questa va aggiunta all'anagrafe delle bib. ILL.
				ServiziILLDelegate.getInstance(request).aggiornaBibliotecaILL(bibReturn);
			}

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}
		currentForm.setBiblioteca(bibReturn);

		if (bibReturn.getInserito() == 1) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.biblioteca"));

			return mapping.getInputForward();
		}
		if (bibReturn.getInserito() == 2) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.biblioteca.esiste"));

			return mapping.getInputForward();
		}
		if (bibReturn.getInserito() == 3) {
			LinkableTagUtils.addError(request, new ActionMessage("modifica.biblioteca.ok"));

		}
		else {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.ok"));

		}

		if (abilita)
			currentForm.setAbilitato("TRUE");
		else
			currentForm.setAbilitato("FALSE");
		currentForm.setNuovo("FALSE");
		currentForm.setSalvato(true);
		currentForm.setId(bibReturn.getIdBiblioteca() + "");
		currentForm.setInPolo("FALSE");
		if (flag.equals("C") || flag.equals("A")) {
			currentForm.setInPolo("TRUE");
			currentForm.setCheckCentro(flag);
		}

		return mapping.getInputForward();
	}

	private void impostaDatiBibILL(NuovaBibliotecaForm currentForm, BibliotecaVO biblioteca) {
		BibliotecaILLVO bibILL = biblioteca.getBibliotecaILL();
		if (!currentForm.isAbilitataILL()) {
			bibILL.setFlCanc("S");	//associazione da cancellare
		} else {
			bibILL.setIsil(biblioteca.getIsil());
			bibILL.setDescrizione(biblioteca.getNom_biblioteca());
			bibILL.setFlCanc("N");
		}
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovaBibliotecaForm currentForm = (NuovaBibliotecaForm) form;

		String provenienza = currentForm.getProvenienza();
		if (provenienza == null || provenienza.equals("ricerca"))
			return mapping.findForward("torna");
		if (provenienza.equals("sintetica"))
			return mapping.findForward("sintetica");

		return mapping.findForward("torna");
	}

	public ActionForward profilo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		NuovaBibliotecaForm currentForm = (NuovaBibliotecaForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		String polo = ValidazioneDati.trimOrEmpty(currentForm.getPolo()).toUpperCase();
		String codBib = ValidazioneDati.trimOrEmpty(currentForm.getCodBib()).toUpperCase();
		String codAna = ValidazioneDati.trimOrEmpty(currentForm.getCodAna()).toUpperCase();

		if (!(polo.equals("") && codBib.equals(""))) {
			if (polo.length() != 3) {
				LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.dimensione"));

				return mapping.getInputForward();
			}

			if (codBib.length() != 2) {
				LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.codbib"));

				return mapping.getInputForward();
			}

			if (!polo.equals("") ) {
				if (codBib.trim().equals("") )  {
					LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.codici.bib"));

					return mapping.getInputForward();
				}
				if (codAna.trim().equals("") ) {
					LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.codici.ana"));

					return mapping.getInputForward();
				}
			}

			codBib = ValidazioneDati.fillLeft(codBib, ' ', 3);//" " + codBib;
		}
		else if (!codAna.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.dimensione"));

			return mapping.getInputForward();
		}

		String nome = currentForm.getNome().trim();
		if (nome.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.nome"));

			return mapping.getInputForward();
		}
		/*
		for (int y=0; y < nome.length(); y++) {
			char carattere = nome.charAt(y);
			if (y == 0)
				nome = nome.substring(0, 1).toUpperCase() + nome.substring(y+1, nome.length());
			else if (carattere == ' ') {
				nome = nome.substring(0, y + 1) + nome.substring(y+1, y+2).toUpperCase() + nome.substring(y+2, nome.length());
			}
		}
		*/
		String unita = currentForm.getUnitaOrganizzativa().trim();
//		if (unita.equals("")) {
//			ActionMessages messaggio = new ActionMessages();
//			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("nuovo.biblioteca.unita"));
//			this.saveErrors(request,messaggio);
//			return mapping.getInputForward();
//		}

		String indirizzo = currentForm.getIndirizzo().trim().toLowerCase();
		if (indirizzo.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.indirizzo"));

			return mapping.getInputForward();
		}
		/*
		for (int y=0; y < indirizzo.length(); y++) {
			char carattere = indirizzo.charAt(y);
			if (y == 0)
				indirizzo = indirizzo.substring(0, 1).toUpperCase() + indirizzo.substring(y+1, indirizzo.length());
			else if (carattere == ' ') {
				indirizzo = indirizzo.substring(0, y + 1) + indirizzo.substring(y+1, y+2).toUpperCase() + indirizzo.substring(y+2, indirizzo.length());
			}
		}
		*/
		indirizzo = currentForm.getSelDug() + " " + indirizzo;

		String paese = currentForm.getPaese();

		String cap = currentForm.getCap();
		if (cap.equals("") && paese.equals("IT")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.cap.obbligo"));

			return mapping.getInputForward();
		}
		if (!cap.equals("") && !this.isNumber(cap)) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.cap"));

			return mapping.getInputForward();
		}

		String provincia = currentForm.getSelProvincia();

		if (paese.equals("IT") && provincia.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.provincia.obbligo"));

			return mapping.getInputForward();
		}

		String citta = currentForm.getCitta().trim().toUpperCase();
		if (citta.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.localita"));

			return mapping.getInputForward();
		}

		String note = currentForm.getNote();
		if (note.length() > 160) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.bibliotecario.note"));

			return mapping.getInputForward();
		}

		String telefono = currentForm.getTelefono().trim();
		String prefissoTel = currentForm.getPrefissoTel().trim();
		if ( (!prefissoTel.equals("") && telefono.equals("")) || (!prefissoTel.equals("") && !this.isNumber(prefissoTel))
				|| (!telefono.equals("") && !this.isNumber(telefono)) ) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.telefono"));

			return mapping.getInputForward();
		}
		telefono = prefissoTel + " " + telefono;

		String fax = currentForm.getFax().trim();
		String prefissoFax = currentForm.getPrefissoFax().trim();
		if ( (!prefissoFax.equals("") && fax.equals("")) || (!prefissoFax.equals("") && !this.isNumber(prefissoFax))
				|| (!fax.equals("") && !this.isNumber(fax)) ) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.fax"));

			return mapping.getInputForward();
		}
		fax = prefissoFax + " " + fax;

		String email = currentForm.getEmail();
		if (!email.equals("") && !isEmail(email)) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.email"));

			return mapping.getInputForward();
		}

		String flag = currentForm.getCheckFlag();
		String codPoloCorrente = factory.getSistema().getCodicePoloCorrente().toUpperCase();
//		if (flag.equals("C") && !codPoloCorrente.equals(polo)) {
//			ActionMessages messaggio = new ActionMessages();
//			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("nuovo.biblioteca.nocs"));
//			this.saveErrors(request,messaggio);
//			return mapping.getInputForward();
//		}
		if (flag.equals("N") && codPoloCorrente.equals(polo)) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.nosbn"));

			return mapping.getInputForward();
		}
		if (flag.equals("S") && polo.equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.sbn"));

			return mapping.getInputForward();
		}
//		if (flag.equals("A") && !codPoloCorrente.equals(polo)) {
//			ActionMessages messaggio = new ActionMessages();
//			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("nuovo.biblioteca.noaffiliata"));
//			this.saveErrors(request,messaggio);
//			return mapping.getInputForward();
//		}
		String codiceCs = " ";
		if (flag.equals("S") && currentForm.getNuovo().equals("TRUE")) {
			flag = "C";
			codiceCs = codBib;
		}

		if (flag.equals("S") && currentForm.getNuovo().equals("FALSE")) {
			codiceCs = currentForm.getSelBibCentroSistema();
			if (codiceCs.equals(currentForm.getCodBib()))
				flag = "C";
			else
				flag = "A";
		}

		String casellaPostale = currentForm.getCasPostale().trim().toUpperCase();

		String iva = currentForm.getIva().trim().toUpperCase();
		String codFiscale = currentForm.getCodFiscale().trim().toUpperCase();
		String tipo = currentForm.getSelTipoBib();

		String gruppo = currentForm.getSelGruppo();

		boolean abilita = false;

		if (codPoloCorrente.equals(polo))
			abilita = true;
		else {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.polo.profilo"));

			return mapping.getInputForward();
		}

		String utenteInseritore = Navigation.getInstance(request).getUtente().getFirmaUtente();

		BibliotecaVO biblioteca = currentForm.getBiblioteca() != null ? currentForm.getBiblioteca() : new BibliotecaVO();
		biblioteca.setCd_ana_biblioteca(codAna);
		biblioteca.setCod_bib(codBib);
		biblioteca.setCod_polo(polo);
		biblioteca.setNom_biblioteca(nome);
		biblioteca.setUnit_org(unita);
		biblioteca.setGruppo(gruppo);
		biblioteca.setIndirizzo(indirizzo);
		biblioteca.setCpostale(casellaPostale);
		biblioteca.setCap(cap);
		biblioteca.setLocalita(citta);
		biblioteca.setTelefono(telefono);
		biblioteca.setFax(fax);
		biblioteca.setNote(note);
		biblioteca.setP_iva(iva);
		biblioteca.setCod_fiscale(codFiscale);
		biblioteca.setE_mail(email);
		biblioteca.setTipo_biblioteca(tipo);
		biblioteca.setPaese(paese);
		biblioteca.setProvincia(provincia);
		biblioteca.setCod_bib_cs(codiceCs);
		biblioteca.setFlCanc("N");
		biblioteca.setChiave_bib(" ");
		biblioteca.setChiave_ente(" ");
		biblioteca.setFlag_bib(flag);
		biblioteca.setTsVar(DaoManager.now());

		impostaDatiBibILL(currentForm, biblioteca);

		BibliotecaVO bibReturn = new BibliotecaVO();
		try {
			if (!currentForm.isSalvato())
				bibReturn = factory.getSistema().creaBiblioteca(biblioteca, utenteInseritore, false, abilita, codPoloCorrente);
			else {
//			biblioteca.setIdBiblioteca(Long.parseLong(currentForm.getId()));
				biblioteca.setIdBiblioteca(Integer.parseInt(currentForm.getId()));
				bibReturn = factory.getSistema().creaBiblioteca(biblioteca, utenteInseritore, true, abilita, codPoloCorrente);
			}
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		currentForm.setBiblioteca(bibReturn);

		if (bibReturn.getInserito() == 1) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.biblioteca"));

			return mapping.getInputForward();
		}
		if (bibReturn.getInserito() == 2) {
			LinkableTagUtils.addError(request, new ActionMessage("error.nuovo.biblioteca.esiste"));

			return mapping.getInputForward();
		}
		if (bibReturn.getInserito() == 3) {
			LinkableTagUtils.addError(request, new ActionMessage("modifica.biblioteca.ok"));

		}
		else {
			LinkableTagUtils.addError(request, new ActionMessage("nuovo.biblioteca.ok"));

		}

		if (abilita)
			currentForm.setAbilitato("TRUE");
		else
			currentForm.setAbilitato("FALSE");
		currentForm.setNuovo("FALSE");
		currentForm.setSalvato(true);
		currentForm.setId(bibReturn.getIdBiblioteca() + "");
		currentForm.setInPolo("FALSE");
		if (flag.equals("C") || flag.equals("A")) {
			currentForm.setInPolo("TRUE");
			currentForm.setCheckCentro(flag);
		}

		request.setAttribute("nomebiblioteca", bibReturn.getNom_biblioteca());
		request.setAttribute("codbiblioteca", bibReturn.getCod_bib());
		request.setAttribute("recapitobiblioteca", bibReturn.getIndirizzo() + " - " + bibReturn.getLocalita());
		request.setAttribute("idbiblioteca", bibReturn.getIdBiblioteca());
		request.setAttribute("provenienza", "nuovo");

		return mapping.findForward("profilo");
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NuovaBibliotecaForm currentForm = (NuovaBibliotecaForm) form;

		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, currentForm.getBiblioteca());
		return Navigation.getInstance(request).goToBookmark(Bookmark.Servizi.LISTA_MOVIMENTI, false);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		NuovaBibliotecaForm currentForm = (NuovaBibliotecaForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (ValidazioneDati.equals(idCheck, "PROFILO")) {
			return currentForm.getAbilitatoProfilo().equals("TRUE") &&
					!navi.bookmarkExists(Bookmark.Servizi.LISTA_MOVIMENTI);
		}

		if (ValidazioneDati.equals(idCheck, "NUOVA_RICHIESTA_SERVIZI_ILL")) {
			BibliotecaVO bib = currentForm.getBiblioteca();
			return bib != null
					&& !bib.isNuovo()
					&& navi.bookmarkExists(Bookmark.Servizi.LISTA_MOVIMENTI);
		}

		if (ValidazioneDati.equals(idCheck, "BIBLIOTECA_ILL")) {
			return true;
		}

		return false;
	}

}
