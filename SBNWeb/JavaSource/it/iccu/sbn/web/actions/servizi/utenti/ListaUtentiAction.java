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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.servizi.utenti.SinteticaUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.utenti.ListaUtentiForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaUtentiAction extends UtenteBaseAction {

	private static Logger log = Logger.getLogger(ListaUtentiAction.class);

	private static final String[] BOTTONIERA_EROGAZIONE = new String[] {
		"servizi.bottone.scegli",
		"servizi.bottone.nuovo",
		"servizi.bottone.esamina",
		"servizi.bottone.annullaOperazione"
	};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.scegli",            "scegli");
		map.put("servizi.bottone.esamina",           "Ok");
		map.put("servizi.bottone.si",                "Si");
		map.put("servizi.bottone.no",                "No");
		//map.put("servizi.bottone.scegli",            "Scegli");
		map.put("servizi.bottone.annullaOperazione", "AnnullaOperazione");
		map.put("servizi.bottone.nuovo",             "Nuovo");
		map.put("servizi.bottone.cancella",          "Cancella");
		//map.put("servizi.bottone.annulla",           "Annulla");
		map.put("servizi.bottone.indietro",           "Annulla");
		map.put("servizi.bottone.rinnovaAut",        "RinnovaAut");
		map.put("button.blocco",                     "blocco");
		map.put("servizi.bottone.importaUtente",     "importa");
		map.put("servizi.bottone.deselTutti",        "deselTutti");
		map.put("servizi.bottone.selTutti",          "selTutti");
		map.put("servizi.bottone.stampa",            "stampaUtenti");
		map.put("servizi.bottone.importaDaBiblioteca", "importaBiblioteca");
		map.put("servizi.bottone.cercaInPolo", "cercaInPolo");
		map.put("servizi.bottone.aggiornaChiaveUtente", "aggiornaChiaveUtente");


		return map;
	}

	private void checkFormXCanc(HttpServletRequest request, ActionForm form)
			throws Exception {

		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		int error = 0;
		// controlllo Richieste
		error = this.verificaMovimenti(listaUtenti.getCodUtente(), Navigation.getInstance(request).getUtente().getTicket());
		if (error == -1) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.erroreLetturaMovimenti"));

			throw new Exception();
		}
		if (error > 0) {
			LinkableTagUtils.addError(request, new ActionMessage(
			"errors.servizi.cancellazioneImpossibileMovimentiPresenti"));

			throw new Exception();
		}
	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm currentForm = (ListaUtentiForm) form;

		request.setAttribute("parametroSubmit", mapping.getParameter());

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				log.info("ListaUtentiAction::unspecified");
				if (request.getAttribute("RicercaUtenti")!=null && request.getAttribute("RicercaUtenti") instanceof RicercaUtenteBibliotecaVO  )
					loadParameter(request, form, mapping);
				else
					return mapping.findForward("annulla");

			}

			if (request.getParameter("UTENTI_POLO") != null) {
				currentForm.setListaUtentiPolo(true);
				Navigation.getInstance(request).setTesto("Omonimi in Polo");
				this.saveMessages(request, ConfermaDati.preparaConferma(this,
						mapping, request));
				return mapping.getInputForward();
			}
			if (request.getAttribute("UTENTIPOLO")!= null )
			{
				String ricercasulpolo=(String) request.getAttribute("UTENTIPOLO");
				if (ricercasulpolo!=null && ricercasulpolo.equals("si"))
					currentForm.setListaUtentiPolo(true);

			}


/*			FactoryEJBDelegate factory = (FactoryEJBDelegate) this.servlet.getServletContext().getAttribute(FactoryEJBPlugin.FACTORY_EJBX);
			String listaAutor="";
			try {
				listaAutor = factory.getGestioneServizi().getListaAutorizzazioniServizio(Navigation.getInstance(request).getUserTicket(),"CSW", " FI",3);
			} catch (Exception e) {
			}
*/			if (currentForm.getListaUtenti()!=null && currentForm.getListaUtenti().size()>0)
				currentForm.setNumUtenti(currentForm.getListaUtenti().size());

			// gestione automatismo check su unico elemento lista
			if (currentForm.getListaUtenti()!=null && currentForm.getListaUtenti().size()==1)
			{
				String  [] appoSelProva= new String [1];
				appoSelProva[0]=String.valueOf(((SinteticaUtenteVO) currentForm.getListaUtenti().get(0)).getIdUtente());
				currentForm.setCodUtente(appoSelProva);
			}
			//if (listaUtenti.getRic()!=null && listaUtenti.getRic().getCognome()!=null && listaUtenti.getRic().getCognome().trim().length()>0 && listaUtenti.getRic().getNome()!=null && listaUtenti.getRic().getNome().trim().length()>0 )
			if (currentForm.getRic()!=null && currentForm.getRic().getCognomeNome()!=null && currentForm.getRic().getCognomeNome().trim().length()>0)
			{
				currentForm.setAbilitaCercaInPolo(true);
			}
			else
			{
				currentForm.setAbilitaCercaInPolo(false);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
			//return mapping.findForward("annulla");

		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm currentForm = (ListaUtentiForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		resetToken(request);

		List<SinteticaUtenteVO> listaUtenti = this.getListaSinteticaUtentiSelezionati(currentForm);
		Navigation navi = Navigation.getInstance(request);
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (ValidazioneDati.isFilled(listaUtenti) ) {
			request.setAttribute("Nuovo", "O");
			request.setAttribute("PathForm", mapping.getPath());
			// RICERCO E CREO LISTA
			List<UtenteBibliotecaVO> utentiSelez = new ArrayList<UtenteBibliotecaVO>();
			for (int i = 0; i< listaUtenti.size(); i++) {
				currentForm.getRic().setIdUte(listaUtenti.get(i).getIdUtente());
				try {
					currentForm.setUteTrovato(factory.getGestioneServizi().getDettaglioUtente(navi.getUserTicket(), currentForm.getRic(),ServiziConstant.NUMBER_FORMAT_CONVERTER, this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/ ));
					if (currentForm.getUteTrovato()!=null &&  currentForm.getUteTrovato().getCodiceUtente() != null) {
						utentiSelez.add(currentForm.getUteTrovato());
					}
				} catch (Exception ex) {
					log.error("", ex);
				}
			}
			if (utentiSelez!=null && utentiSelez.size()>0) {
				request.setAttribute("UtentiSel",utentiSelez );
			}
			request.setAttribute("UtenteScelto", currentForm.getUteTrovato());
			request.setAttribute("RicercaUtenti", currentForm.getRic());
			return mapping.findForward("esamina");
		}
		else if (currentForm.getCodUtenteSing() > 0) {
			request.setAttribute("Nuovo", "O");
			request.setAttribute("PathForm", mapping.getPath());
			currentForm.getRic().setIdUte(Integer.valueOf(currentForm.getCodUtenteSing()));
			currentForm.setUteTrovato(factory.getGestioneServizi().getDettaglioUtente(navi.getUserTicket(), currentForm.getRic(),ServiziConstant.NUMBER_FORMAT_CONVERTER, this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/ ));
			if (currentForm.getUteTrovato().getCodiceUtente() == null) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));

				if (currentForm.getRic().getParametro() != null) {
					request.setAttribute("IdUte", "false");
					//return mapping.findForward("utilityUte");
					return mapping.findForward(currentForm.getRic().getParametro());
				} else {
					return mapping.getInputForward();
				}
			}
			/*
			if (listaUtenti.getRic().getParametro() != null) {
				request.setAttribute("IdUte", "true");
				//request.setAttribute("utente", listaUtenti.getRic());
				request.setAttribute(NavigazioneServizi.COD_BIB_UTENTE, listaUtenti.getUteTrovato().getCodiceBiblioteca());
				request.setAttribute(NavigazioneServizi.COD_UTENTE, listaUtenti.getUteTrovato().getCodiceUtente().trim());
				//return mapping.findForward("utilityUte");
				return mapping.findForward(listaUtenti.getRic().getParametro());
			} else {
			*/
				request.setAttribute("UtenteScelto", currentForm.getUteTrovato());
				request.setAttribute("RicercaUtenti", currentForm.getRic());
				return mapping.findForward("esamina");
			//}
		} else {

		// messaggio di errore per nessuna selezione effettuata

		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));

		// nessun codice selezionato
		saveToken(request);
		return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm currentForm = (ListaUtentiForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		resetToken(request);

		List<SinteticaUtenteVO> listaSinteticaUtenteSel = this.getListaSinteticaUtentiSelezionati(currentForm);
		int codiceUtenteSel=0;
		if (listaSinteticaUtenteSel!=null &&  listaSinteticaUtenteSel.size() > 1)
		{
			// messaggio errore selezione multipla

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezoneMultiplaInvalida"));

			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}
		if (listaSinteticaUtenteSel!=null &&  listaSinteticaUtenteSel.size() == 1)
		{
			codiceUtenteSel=listaSinteticaUtenteSel.get(0).getIdUtente();
		}
		else if (currentForm.getCodUtenteSing() > 0 )
		{
			codiceUtenteSel=currentForm.getCodUtenteSing();
		}

		if (codiceUtenteSel > 0 ) {
			request.setAttribute("Nuovo", "O");
			request.setAttribute("PathForm", mapping.getPath());
			currentForm.getRic().setIdUte(
					Integer.valueOf(codiceUtenteSel));
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			currentForm.setUteTrovato(factory.getGestioneServizi()
											 .getDettaglioUtente(Navigation.getInstance(request).getUserTicket(), currentForm.getRic(),ServiziConstant.NUMBER_FORMAT_CONVERTER, this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/ ));
			if (currentForm.getUteTrovato().getCodiceUtente() == null) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));

				if (currentForm.getRic().getParametro() != null) {
					request.setAttribute("IdUte", "false");
					//return mapping.findForward("utilityUte");
					return mapping.findForward(currentForm.getRic().getParametro());
				} else {
					return mapping.getInputForward();
				}
			}
			/*
			if (listaUtenti.getRic().getParametro() != null) {
			*/
				request.setAttribute("IdUte", "true");
				//request.setAttribute("utente", listaUtenti.getRic());
				request.setAttribute(NavigazioneServizi.COD_BIB_UTENTE, currentForm.getUteTrovato().getCodiceBiblioteca());
				request.setAttribute(NavigazioneServizi.COD_UTENTE, currentForm.getUteTrovato().getCodiceUtente().trim());

				//request.setAttribute("SCEGLI", "SI");
				request.setAttribute("SCEGLI", "NO");

				//return mapping.findForward("utilityUte");


				request.setAttribute("SCEGLI", "SI");

				return mapping.findForward(currentForm.getRic().getParametro());
			/*
			} else {
				request.setAttribute("UtenteScelto", listaUtenti.getUteTrovato());
				request.setAttribute("RicercaUtenti", listaUtenti.getRic());
				return mapping.findForward("esamina");
			}
			*/
		}
		// messaggio di errore per nessuna selezione effettuata

		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));

		// nessun codice selezionato
		saveToken(request);
		return mapping.getInputForward();
	}


	public ActionForward importaBiblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			if (Navigation.getInstance(request).isFromBar() )		return mapping.getInputForward();

			if (!isTokenValid(request)) {
				saveToken(request);
			}

			return mapping.findForward("importaDaBiblioteca");
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			ListaUtentiForm currentForm = (ListaUtentiForm) form;
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			resetToken(request);

			List<SinteticaUtenteVO> selecteds = this.getListaSinteticaUtentiSelezionati(currentForm);
			int codiceUtenteSel = 0;
			if (!ValidazioneDati.isFilled(selecteds)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
				saveToken(request);
				return mapping.getInputForward();
			}

			if (ValidazioneDati.size(selecteds) != 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.SelezoneMultiplaInvalida"));
				saveToken(request);
				return mapping.getInputForward();
			}

			boolean importaUtenteBase = false;
			codiceUtenteSel = selecteds.get(0).getIdUtente();
			// in caso di utente con sola anagrafica e senza alcuna
			// localizzazione (utente base)
			// creo la localizzazione vuota recupero l'id creato e lo
			// impongo a codiceUtenteSel
			// se la localizzazione era flaggata allora la recupero
			if (selecteds.get(0).getIdUtente() == 0
					&& selecteds.get(0).getCodice() != null
					&& selecteds.get(0).getCodice().trim().length() > 0) {
				importaUtenteBase = true;
			}

			if (currentForm.getCodUtenteSing() > 0)
				codiceUtenteSel = currentForm.getCodUtenteSing();

			if (codiceUtenteSel > 0 || importaUtenteBase) {
				request.setAttribute("Nuovo", "O");
				request.setAttribute("PathForm", mapping.getPath());
				UtenteBibliotecaVO uteTrovato = new UtenteBibliotecaVO();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				Navigation navi = Navigation.getInstance(request);
				if (codiceUtenteSel > 0) {
					currentForm.getRic().setIdUte(Integer.valueOf(codiceUtenteSel));
					currentForm.setUteTrovato(factory.getGestioneServizi()
							.getDettaglioUtente(navi.getUserTicket(),
									currentForm.getRic(),
									ServiziConstant.NUMBER_FORMAT_CONVERTER,
									this.getLocale(request,	Constants.SBN_LOCALE)));
					uteTrovato = currentForm.getUteTrovato();
					if (uteTrovato.getCodiceUtente() == null) {
						// solo alle condizioni di cui sopra
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));
						return mapping.getInputForward();
					}
				} else if (importaUtenteBase) {
					currentForm.getRic().setIdUte(0);
					currentForm.getRic().setCodUte(selecteds.get(0).getCodice());
					currentForm.setUteTrovato(factory.getGestioneServizi()
							.getDettaglioUtenteBase(navi.getUserTicket(),
									currentForm.getRic(),
									ServiziConstant.NUMBER_FORMAT_CONVERTER,
									this.getLocale(request,	Constants.SBN_LOCALE)));
					uteTrovato = currentForm.getUteTrovato();
					uteTrovato.setChangePassword("S"); // concordato con rossana il 12.11.09
				}

				// Pulisco utente dei dati appartenenti alle altre biblioteche
				uteTrovato.setImportato(true);
				uteTrovato.setCodBibSer(navi.getUtente().getCodBib());
				uteTrovato.setCodPoloSer(navi.getUtente().getCodPolo());
				uteTrovato.setAutorizzazioni(new AutorizzazioniVO());
				uteTrovato.getProfessione().getMaterie().clear();
				uteTrovato.getProfessione().setIdOccupazione(null);
				uteTrovato.getProfessione().setIdSpecTitoloStudio(null);
				uteTrovato.getBibliopolo().clear();

				// l'id appartiene alla biblioteca da cui sto importando
				// l'utente
				// all'atto della registrazione ne sarà creato uno nuovo
				uteTrovato.setIdUtenteBiblioteca(null);
				// Antonio giovedì 11 ottobre 2007
				uteTrovato.setNuovoUte(false);

				request.setAttribute("UtenteScelto", uteTrovato);
				// inserito per evitare il controllo esistenza omonimi in polo
				request.setAttribute(DettaglioUtentiAnaAction.UTENTI_POLO_CONTROLLATO, true);
				return mapping.findForward("esamina");

			}
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			// nessun codice selezionato
			saveToken(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaUtenti.isSessione()) {
			listaUtenti.setSessione(true);
		}
		Navigation navi = Navigation.getInstance(request);
		request.setAttribute("Nuovo", "N");
		request.setAttribute("PathForm", mapping.getPath());

		this.resetToken(request);
		if (listaUtenti.getUteTrovato() != null) {
			listaUtenti.getUteTrovato().clearUtenteBiblioteca();
			listaUtenti.getUteTrovato().getAutorizzazioni().getListaServizi()
					.clear();
			listaUtenti.getUteTrovato().getProfessione().getMaterie().clear();
			listaUtenti.getUteTrovato().setCodPoloSer(
					navi.getUtente().getCodPolo());
			listaUtenti.getUteTrovato().setCodBibSer(
					navi.getUtente().getCodBib());
			listaUtenti.getUteTrovato().setCodPolo(
					navi.getUtente().getCodPolo());
			listaUtenti.getUteTrovato().setCodiceBiblioteca(
					navi.getUtente().getCodBib());
		}
		request.setAttribute("RicercaUtenti", listaUtenti.getRic());
		listaUtenti.setRichiesta(null);
		listaUtenti.setConferma(false);
		return mapping.findForward("nuovo");
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaUtenti.isSessione()) {
			listaUtenti.setSessione(true);
		}
		if (listaUtenti.getRic().getParametro() != null) {
			request.setAttribute("IdUte", "false");
			//return mapping.findForward("utilityUte");
			return mapping.findForward(listaUtenti.getRic().getParametro());
		}

		if (listaUtenti.isListaUtentiPolo()) {
			request.setAttribute(
					DettaglioUtentiAnaAction.UTENTI_POLO_CONTROLLATO, true);
			return Navigation.getInstance(request).goBack();
		} else {
			resetToken(request);
			return mapping.findForward("annulla");
		}
	}

	public ActionForward cercaInPolo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm currentForm = (ListaUtentiForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		try {
			if (ValidazioneDati.isFilled(currentForm.getListaUtenti()) ) {
				Set<String> idTrovati = new HashSet<String>();
				for (Object o : currentForm.getListaUtenti() ) {
					SinteticaUtenteVO su = (SinteticaUtenteVO) o;
					idTrovati.add(su.getCodice());
				}
				currentForm.setIdUtentiTrovatiInBibl(idTrovati);

				// applicare la ricerca per nome e cognome (da ripulire degli altri criteri???) senza esatto e farla su polo escludendo la biblioteca operante
				if (currentForm.isAbilitaCercaInPolo())	{
					DescrittoreBloccoVO blocco1 = null;
					ServiziDelegate delegate = ServiziDelegate.getInstance(request);
					Navigation navi = Navigation.getInstance(request);

					RicercaUtenteBibliotecaVO ricerca = currentForm.getRic().copy();
					ricerca.setRicercaUtentePolo(true); // imposto per la ricerca
					ricerca.setIdTrovati(idTrovati);
					try {
						blocco1 = delegate.caricaListaUtenti(request, navi.getUserTicket(), ricerca, ricerca.getNumeroElementiBlocco());

					} catch (Exception e) {
						log.error("", e);
					}

					ricerca.setRicercaUtentePolo(false); // reset dopo ricerca
					if (DescrittoreBloccoVO.isFilled(blocco1) ) {
						request.setAttribute(ServiziDelegate.LISTA_UTENTI, blocco1);
						request.setAttribute("RicercaUtenti", ricerca);
						//request.setAttribute("PathForm", mapping.getPath());

						//request.setAttribute(BIBLIOTECA_ATTR, listaelementi.getRicerca().getCodBibSer());
						request.setAttribute(BIBLIOTECA_ATTR, "di Polo");
						request.setAttribute(BIBLIOTECA_ATTR, "X");
						//request.setAttribute(PATH_CHIAMANTE_ATTR, "RicercaUtenti");
						request.setAttribute("UTENTIPOLO","si");

						loadParameter(request, form, mapping);
						currentForm.setListaUtentiPolo(true);

						request.setAttribute(BIBLIOTECA_ATTR, "di Polo");
						request.setAttribute(BIBLIOTECA_ATTR, "X");
						currentForm.setLivelloRicerca((String)request.getAttribute(BIBLIOTECA_ATTR));

						if (ValidazioneDati.isFilled(currentForm.getListaUtenti()) ) {

							currentForm.setListaUtenti(currentForm.getListaUtenti());
							//currentForm.setTotRighe(currentForm.getListaUtenti().size());
							currentForm.setNumUtenti(currentForm.getListaUtenti().size());

							if (currentForm.getListaUtenti()!=null && currentForm.getListaUtenti().size()==0)
							{

								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));

								resetToken(request);
								//listaUtenti.setNonTrovato(true);
								return mapping.getInputForward();
							}
						}
						else
						{

							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));

							resetToken(request);
							//listaUtenti.setNonTrovato(true);
							return mapping.getInputForward();
						}

						// gestione automatismo check su unico elemento lista
						if (currentForm.getListaUtenti()!=null && currentForm.getListaUtenti().size()==1)
						{
							String  [] appoSelProva= new String [1];
							appoSelProva[0]=String.valueOf(((SinteticaUtenteVO) currentForm.getListaUtenti().get(0)).getIdUtente());
							currentForm.setCodUtente(appoSelProva);
						}

						//almaviva5_20110421 #4393
						currentForm.setCodUtenteSing(0);
						return mapping.getInputForward();
					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));

						resetToken(request);
						//listaUtenti.setNonTrovato(true);
						return mapping.getInputForward();
					}
				}
			}




			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));

			resetToken(request);
			//listaUtenti.setNonTrovato(true);
			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
}


	public ActionForward RinnovaAut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaUtenti.isSessione()) {
			listaUtenti.setSessione(true);
		}
		resetToken(request);
		if (listaUtenti.getCodUtente() != null
				&& listaUtenti.getCodUtente().length > 0) {
			listaUtenti.setConferma(true);
			listaUtenti.setRichiesta("RinnovaAut");

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.DataRinnovoListaServizi"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
			saveToken(request);
			return mapping.getInputForward();
		} else {
			// messaggio di errore.

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));

			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!listaUtenti.isSessione()) {
				listaUtenti.setSessione(true);
			}
			resetToken(request);
			if (listaUtenti.getCodUtente() != null
					&& listaUtenti.getCodUtente().length > 0) {
				this.checkFormXCanc(request, form);
				listaUtenti.setConferma(true);
				listaUtenti.setRichiesta("Cancella");

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.confermaOperazione"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this,
						mapping, request));
				saveToken(request);
				return mapping.getInputForward();
			} else {
				// messaggio di errore.

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.servizi.codiceNessunaSelezione"));

				// nessun codice selezionato
				saveToken(request);
				return mapping.getInputForward();
			}
		} catch (Exception ex) {
			return mapping.getInputForward();
		}
	}

	/*
	public ActionForward Scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return this.Ok(mapping,form,request,response);
	}
	*/

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			ListaUtentiForm currentForm = (ListaUtentiForm) form;
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			if (currentForm.getRichiesta().equals("Cancella")) {
				// codice selezionati x cui effettuo operazione di cancellazione
				// codice selezionati x cui effettuo operazione di cancellazione



				this.checkFormXCanc(request, form);
				Navigation navi = Navigation.getInstance(request);
				boolean ret = true;
				ret = this.cancmultipla(currentForm, currentForm.getCodUtente(), navi.getUserTicket(), this.getLocale(request, Constants.SBN_LOCALE)/*request.getLocale()*/, request);

				// mando msg di operazione ok
				if (ret) {
					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.servizi.codiceCancellazioneEffettuata"));

					ServiziDelegate delegate = ServiziDelegate.getInstance(request);

					DescrittoreBloccoVO blocco1 = delegate.caricaListaUtenti(
							request, navi.getUserTicket(),
							currentForm.getRic(), currentForm.getRic()
									.getNumeroElementiBlocco());
					if (blocco1.getTotRighe() > 0) {
						request.setAttribute(ServiziDelegate.LISTA_UTENTI,
								blocco1);
						loadParameter(request, form, mapping);

						currentForm.setRichiesta(null);
						currentForm.setConferma(false);
						return mapping.getInputForward();
						//return mapping.findForward("annulla");
					} else {
						//LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
						loadParameter(request, form, mapping);

						resetToken(request);
						currentForm.setRichiesta(null);
						currentForm.setConferma(false);
						currentForm.setListaUtenti(new ArrayList<SinteticaUtenteVO>());
						currentForm.setTotRighe(0);
						return mapping.getInputForward();
					}
				} else {
					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.servizi.codiceErroreCancellazione"));

					currentForm.setRichiesta(null);
					currentForm.setConferma(false);
					return mapping.getInputForward();
				}
			}
			if (currentForm.getRichiesta().equals("RinnovaAut")) {
				boolean error = false;
				String data = "";
				int codRitorno = -1;
				data = currentForm.getDataRinnovoAut();
				if (!ValidazioneDati.strIsNull(data)) {
					try {
						codRitorno = ValidazioneDati.validaData(data);
						if (codRitorno != ValidazioneDati.DATA_OK)
							throw new Exception();
					} catch (Exception e) {

						switch (codRitorno) {
						case ValidazioneDati.DATA_ERRATA:
							LinkableTagUtils.addError(request, new ActionMessage(
									"errors.servizi.dataFormatoErrore"));

							error = true;
						case ValidazioneDati.DATA_PASSATA_ERRATA:
							LinkableTagUtils.addError(request, new ActionMessage(
									"errors.servizi.dataVuotaErrore"));

							error = true;
						}
					}
					if (error)
						throw new Exception();

//					this.aggiornaDataSuListaServizi(request, listaUtenti
//							.getListaUtenti(), listaUtenti.getDataRinnovoAut());

					//30.11.09 this.aggiornaDataSuListaServizi(request, listaUtenti.getCodUtente(), listaUtenti.getDataRinnovoAut());

					currentForm.setRichiesta(null);
					currentForm.setConferma(false);
//					 new gestione batch inizio
					RinnovoDirittiDiffVO richiesta = new RinnovoDirittiDiffVO();
					richiesta.setCodPolo(((SinteticaUtenteVO)currentForm.getListaUtenti().get(0)).getPolo());
					richiesta.setCodBib(((SinteticaUtenteVO)currentForm.getListaUtenti().get(0)).getBiblioteca());
					richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
					richiesta.setCodAttivita(CodiciAttivita.getIstance().SRV_DIRITTI_UTENTE);

//					richiesta.setParametri(inputForStampeService);
//					richiesta.setTipoOrdinamento(ordinamFile);
					String ticket=Navigation.getInstance(request).getUserTicket();
					richiesta.setTicket(ticket);
					richiesta.setCodUtente(currentForm.getCodUtente());
					richiesta.setDataRinnovoAut(currentForm.getDataRinnovoAut());
					richiesta.setTipoRinnModalitaDiff("L");

					String basePath=this.servlet.getServletContext().getRealPath(File.separator);
					richiesta.setBasePath(basePath);
					String downloadPath = StampeUtil.getBatchFilesPath();
					log.info("download path: " + downloadPath);
					String downloadLinkPath = "/";
					richiesta.setDownloadPath(downloadPath);
					richiesta.setDownloadLinkPath(downloadLinkPath);


//					AmministrazionePolo  anministrazionePolo;

					String s =  null;
					try {
						FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
						s = factory.getPolo().prenotaElaborazioneDifferita(richiesta, null);
					} catch (ApplicationException e) {
						if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
						{

							LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

							return mapping.getInputForward();
						}
					} catch (Exception e) {
						log.error("", e);
					}

					if (s == null) {

						LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotBatchKO"));

						resetToken(request);
						return mapping.getInputForward();
					}


					LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));

					resetToken(request);
					return mapping.getInputForward();

					//30.11.09 listaUtenti.setRichiesta(null);
					//30.11.09 listaUtenti.setConferma(false);
					//30.11.09 return mapping.getInputForward();


					//return mapping.findForward("rinnovaAut");
					//return mapping.findForward("annulla");
				} else {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.servizi.NessunaDataDigitata"));

					this.saveToken(request);
					currentForm.setRichiesta(null);
					currentForm.setConferma(false);
					return mapping.getInputForward();
				}
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward AnnullaOperazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return this.Annulla(mapping, form, request, response);
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaUtenti.isSessione()) {
			listaUtenti.setSessione(true);
		}
		listaUtenti.setRichiesta(null);
		listaUtenti.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm listaUtenti = (ListaUtentiForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaUtenti.isSessione()) {
			listaUtenti.setSessione(true);
		}
		int numBlocco = listaUtenti.getBloccoSelezionato();
		String idLista = listaUtenti.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket,
					idLista, numBlocco);
			if (bloccoVO != null && bloccoVO.getLista().size()>0) {
				int progressivoPrimoElementoBloccoCaricato=((SinteticaUtenteVO)bloccoVO.getLista().get(0)).getProgressivo();

				List<SinteticaUtenteVO> listaUtentiDaAggiornare=listaUtenti.getListaUtenti();
				Iterator<SinteticaUtenteVO> i = listaUtentiDaAggiornare.iterator();
				int posizioneInserimentoBlocco=1;
				while (i.hasNext()) {
					if (progressivoPrimoElementoBloccoCaricato < i.next().getProgressivo()) {
						break;
					}
					posizioneInserimentoBlocco++;
				}
				if (posizioneInserimentoBlocco>listaUtentiDaAggiornare.size())
					listaUtenti.getListaUtenti().addAll(bloccoVO.getLista());
				else
					listaUtenti.getListaUtenti().addAll(posizioneInserimentoBlocco-1, bloccoVO.getLista());

			}
		}
		if (listaUtenti.getRic().getParametro() != null &&
				ValidazioneDati.in(listaUtenti.getRic().getParametro(),
					"erogazione_ricerca_utente",
					"ricerca_sale",
					"prenot_posto",
					"erogazione_ricerca_ill")) {
			listaUtenti.setUtilUtenti(true);
			listaUtenti.setRichiesta("ErogazioneRicerca");
			//listaUtenti.setConferma(true);
			request.setAttribute("parametroSubmit", mapping.getParameter());
/*
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.SelezionaUtente"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));*/
			saveToken(request);
		}

		return mapping.getInputForward();
	}


	public ActionForward stampaUtenti(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	{
		try {
			ListaUtentiForm elencoUtenti = (ListaUtentiForm)form;

			if (elencoUtenti.getCodUtente()==null || elencoUtenti.getCodUtente().length==0) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaUtenti.nessunaSelezionePerStampa"));

				return mapping.getInputForward();
			} else {
				List<SinteticaUtenteVO> listaSinteticaUtenteSel = this.getListaSinteticaUtentiSelezionati(elencoUtenti);

				request.setAttribute("FUNZIONE_STAMPA",     StampaType.STAMPA_LISTA_UTENTI);
				request.setAttribute("DATI_STAMPE_ON_LINE", listaSinteticaUtenteSel);

				return mapping.findForward("stampaUtenti");
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	private boolean cancmultipla(ListaUtentiForm listaUtentiForm, String[] codSelUte, String ticket, Locale locale, HttpServletRequest request)
			throws Exception {
		boolean ret = true;
		if (codSelUte.length == 0) {
			return ret;
		}
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		RicercaUtenteBibliotecaVO ricercaUtenteVO = listaUtentiForm.getRic();

		List<SinteticaUtenteVO> listaSinteticaVO = listaUtentiForm.getListaUtenti();
		SinteticaUtenteVO eleUte;
		UtenteBibliotecaVO utenteVO;
		for (int y = 0; y < codSelUte.length; y++) {
			for (int ind = 0; ind < listaSinteticaVO.size(); ind++) {
				eleUte = listaSinteticaVO.get(ind);
				if (eleUte.getIdUtente() == Integer.valueOf(codSelUte[y]) ) {
					//imposto id_utente per la ricerca di UtenteBibliotecaVO
					ricercaUtenteVO.setIdUte(eleUte.getIdUtente());
					utenteVO = factory.getGestioneServizi().getDettaglioUtente(Navigation.getInstance(request).getUserTicket(), ricercaUtenteVO, ServiziConstant.NUMBER_FORMAT_CONVERTER, locale);

					ret = factory.getGestioneServizi().cancelUtenteBiblioteca(Navigation.getInstance(request).getUserTicket(), utenteVO);
							//.cancelMultiUtente(eleUte);
					if (!ret)
						break;
				}
			}
			if (!ret)
				break;
		}
		return ret;
	}

	private void loadParameter(HttpServletRequest request, ActionForm form,
			ActionMapping mapping) throws Exception {

		ListaUtentiForm currentForm = (ListaUtentiForm) form;

		RicercaUtenteBibliotecaVO ricerca = (RicercaUtenteBibliotecaVO) request.getAttribute("RicercaUtenti");
		if (ricerca == null) {
			ricerca = new RicercaUtenteBibliotecaVO();
			//almaviva5_20111109 gestione bib. affiliate
			ricerca.setCodBibSer(Navigation.getInstance(request).getUtente().getCodBib());
		}
		currentForm.setRic(ricerca);
		currentForm.setBiblioteca(ricerca.getCodBibSer());

		if (currentForm.getRic() == null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneServizi.parametri"));

			throw new Exception();
		}

		if (ricerca.getParametro() != null &&
				ValidazioneDati.in(ricerca.getParametro(),
					"erogazione_ricerca_utente",
					"ricerca_sale",
					"prenot_posto",
					"erogazione_ricerca_ill")) {
			currentForm.setRichiesta("ErogazioneRicerca");
			currentForm.setPulsanti(BOTTONIERA_EROGAZIONE);
		}
		if (ricerca.getParametro() != null && ricerca.getParametro().equalsIgnoreCase("lista_movimenti_utente")) {
			currentForm.setRichiesta("ListaMovimenti");
			currentForm.setPulsanti(BOTTONIERA_EROGAZIONE);
		}
		if (ricerca.getParametro() != null && ricerca.getParametro().equalsIgnoreCase("acquisizioni_suggLett")) {
			currentForm.setRichiesta("SuggLett");
			currentForm.setPulsanti(BOTTONIERA_EROGAZIONE);
		}

		if (ricerca.getParametro() != null &&
				!ValidazioneDati.in(ricerca.getParametro(),
					"erogazione_ricerca_utente",
					"ricerca_sale",
					"prenot_posto",
					"erogazione_ricerca_ill",
					"lista_movimenti_utente",
					"acquisizioni_suggLett")) {
			currentForm.getRic().setParametro("E");
			currentForm.setUtilUtenti(true);
			currentForm.setConferma(true);
			currentForm.setRichiesta("RinnovaAut");

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.servizi.SelezionaUtente"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
			saveToken(request);
		}
		currentForm.setPathForm((String) request.getAttribute("PathForm"));
		DescrittoreBloccoVO blocco1 = ((DescrittoreBloccoVO) request
				.getAttribute(ServiziDelegate.LISTA_UTENTI));
		// abilito i tasti per il blocco se necessario
		// memorizzo le informazioni per la gestione blocchi
		if(blocco1!=null)
		{
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setListaUtenti(blocco1.getLista());
			currentForm.setMaxRighe(blocco1.getMaxRighe());
		}
		if (request.getAttribute(PATH_CHIAMANTE_ATTR) != null && request.getAttribute(PATH_CHIAMANTE_ATTR).equals("RicercaUtenti")) {
			currentForm.setLivelloRicerca((String)request.getAttribute(BIBLIOTECA_ATTR));
		}
		else {
			currentForm.setLivelloRicerca(Navigation.getInstance(request)
					.getUtente().getCodBib());
		}

		if (currentForm.getListaUtenti()!=null && currentForm.getListaUtenti().size()==1) {
			currentForm.setCodUtenteSing(((SinteticaUtenteVO) currentForm.getListaUtenti().get(0)).getIdUtente());

		}
	}

	public int verificaMovimenti(String[] lstUte, String ticket) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		int totEle = 0;
		for (int ind = 0; ind < lstUte.length; ind++) {
			int numRec = 0;
			numRec = factory.getGestioneServizi().verificaMovimentiUtente(ticket, lstUte[ind]);
			totEle = totEle + numRec;
		}
		return totEle;
	}



	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm elencoUtenti = (ListaUtentiForm) form;
		int numUtenti=0;

		try {
			numUtenti=elencoUtenti.getListaUtenti().size();
			if (numUtenti > 0) {
				String[] elemSel = new String[numUtenti];

				SinteticaUtenteVO utenteVO;
				for (int m = 0; m < numUtenti; m++) {
					utenteVO=(SinteticaUtenteVO)elencoUtenti.getListaUtenti().get(m);

					elemSel[m] = String.valueOf(utenteVO.getIdUtente());
				}
				elencoUtenti.setCodUtente(elemSel);
			}
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}

	public ActionForward aggiornaChiaveUtente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm elencoUtenti = (ListaUtentiForm) form;
		int numUtenti=0;

		try {
			numUtenti=elencoUtenti.getListaUtenti().size();
			if (numUtenti > 0) {
				String[] elemSel = new String[numUtenti];
				SinteticaUtenteVO utenteVO;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				//String idLista = elencoUtenti.getIdLista();
				String ticket = Navigation.getInstance(request).getUserTicket();
				//if (numBlocco > 1 && idLista != null) {
					ServiziDelegate delegate = ServiziDelegate.getInstance(request);
					RicercaUtenteBibliotecaVO ricAppo= new RicercaUtenteBibliotecaVO();
					ricAppo.setCodBibSer(elencoUtenti.getRic().getCodBibSer());
					ricAppo.setCodPoloSer(elencoUtenti.getRic().getCodPoloSer());
					DescrittoreBloccoVO blocco1=null;
					try {
						blocco1 = delegate.caricaListaUtenti(request,ticket, ricAppo,999);
					} catch (Exception e) {
						log.error("", e);
					}
					if (blocco1!=null && blocco1.getLista()!=null && blocco1.getLista().size()>0)
					{
						for (int m = 0; m < blocco1.getLista().size(); m++) {
							utenteVO = (SinteticaUtenteVO) blocco1.getLista().get(m);
							elemSel[m] = String.valueOf(utenteVO.getIdUtente());
							String d = utenteVO.getDescrizione();
							GeneraChiave key = new GeneraChiave();
							key.estraiChiavi("", d);

							if (utenteVO.getIdUtente()>0) {
								factory.getGestioneServizi().aggiornaChiaveUtenteById(Navigation.getInstance(request).getUserTicket(), elemSel[m],key.getKy_cles1_A());
							}
						}
					}
			}
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.getInputForward();
		}
	}


	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaUtentiForm elencoUtenti = (ListaUtentiForm) form;
		try {
			elencoUtenti.setCodUtente(null);
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	private List<SinteticaUtenteVO> getListaSinteticaUtentiSelezionati(ListaUtentiForm elencoUtenti) {
		List<SinteticaUtenteVO> listaUtentiSelezionati = new ArrayList<SinteticaUtenteVO>();

		String[] codiciUtenteSel = elencoUtenti.getCodUtente();
		List<SinteticaUtenteVO> listaSinteticaUtente = elencoUtenti.getListaUtenti();
		Iterator<SinteticaUtenteVO> iterator=null;
		SinteticaUtenteVO sintetica=null;

		for (int i=0; codiciUtenteSel!=null && i<codiciUtenteSel.length; i++) {
			iterator = listaSinteticaUtente.iterator();
			while (iterator.hasNext()) {
				sintetica=iterator.next();
				if (sintetica.getIdUtente()==Integer.parseInt(codiciUtenteSel[i])) {
					listaUtentiSelezionati.add(sintetica);
					break;
				}
			}
		}

		return listaUtentiSelezionati;
	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.nuovo") ) {
			ListaUtentiForm currentForm = (ListaUtentiForm) form;
			String param = currentForm.getRic().getParametro();
			return ValidazioneDati.in(param, "prenot_posto");
		}

		return super.checkAttivita(request, form, idCheck);
	}

}
