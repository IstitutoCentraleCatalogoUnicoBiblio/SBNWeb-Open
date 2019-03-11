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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.SelezioneBibliotecaForm;
import it.iccu.sbn.web.integration.action.erogazione.ListaMovimentiAction;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.Ticket;
import it.iccu.sbn.web.integration.bd.serviziweb.ServiziWebDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
public class SelezioneBibliotecaAction extends ListaMovimentiAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.iscrizione", "iscrizione");
		return map;
	}
	//
	@SuppressWarnings("unchecked")
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SelezioneBibliotecaForm currentForm = (SelezioneBibliotecaForm) form;
		HttpSession session = request.getSession();
		//
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		//
		currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
		currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));//da esportare nelle action e jsp
		//
		String polo = Navigation.getInstance(request).getPolo();
	    //
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();
		//
		//se provengo da opac quindi per inserimento richiesta servizio
		//posseduto della bib erogante, in questo caso devo avere impostato
		//il codBibErogante se lui risulta iscritto si procede con l'inserimento
		//della richiesta stessa

		RichiestaOpacVO ricOpac = (RichiestaOpacVO)session.getAttribute(Constants.RICHIESTA_OPAC);

		/*if (ricOpac!=null) {
			//inserimento richiesta da opac
			currentForm.setListaBiblio(ricOpac.getListaBiblioOpac());

			List codBib = new ArrayList();
			if (currentForm.getListaBiblio().size()>0) {

				for (int i = 0; i < currentForm.getListaBiblio().size(); i++) {
					if (ricOpac.getCodBibOpac()==null) {
						BibliotecaVO bib = (BibliotecaVO) currentForm.getListaBiblio().get(i);
						codBib.add(bib.getCod_bib());

					}
				}

			}
			currentForm.setInsRichOpac(ricOpac.getCodBibOpac());
		}else
		*/
		currentForm.setInsRichOpac("");
		List listaBibiscr = factory.getGestioneServiziWeb().listaBiblioIscritto(polo, utente.getIdUtente());
		currentForm.setListaBiblio(listaBibiscr);

		List codBib = new ArrayList();
		if (currentForm.getListaBiblio().size()>0) {

			for (int i = 0; i < currentForm.getListaBiblio().size(); i++) {
				BibliotecaVO bib = (BibliotecaVO) currentForm.getListaBiblio().get(i);
				codBib.add(bib.getCod_bib());

			}
		}

		if (!ValidazioneDati.isFilled(codBib) )
			codBib.add("999");


		if (codBib.get(0).equals("999") )	{
			List listaBibautoNonIscritto = factory.getGestioneServiziWeb().listaBiblioNonIscr(polo, utente.getIdUtente(), codBib);
			currentForm.setListaBiblioAuto(listaBibautoNonIscritto);
		} else {
			List listaBibautoLogicamente = factory.getGestioneServiziWeb().listaBiblioAuto(polo, utente.getIdUtente(), codBib);

			//se ci sono biblioteche
			if (ValidazioneDati.isFilled(listaBibautoLogicamente) ) {
				//caricamento della lista delle biblioteche
				//che ammettono autoregistrazione da web
				currentForm.setListaBiblioAuto(listaBibautoLogicamente);
			}else {
				List listaBibautoNonIscritto = factory.getGestioneServiziWeb().listaBiblioNonIscr(polo, utente.getIdUtente(), codBib);
				currentForm.setListaBiblioAuto(listaBibautoNonIscritto);

			}
		}

		Navigation.getInstance(request).makeFirst();
		return mapping.getInputForward();
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SelezioneBibliotecaForm currentForm = (SelezioneBibliotecaForm) form;
		HttpSession session = request.getSession();
		List bib = currentForm.getListaBiblio();
		BibliotecaVO bibl = currentForm.getBiblio();
		String biblioSel = "";
		String polo = "";
		Integer idUteBib=null;
		String codBib="";
		session.setAttribute(Constants.BIBLIO,bibl);
		for (int i = 0; i < bib.size(); i++) {
			BibliotecaVO bibAppo = (BibliotecaVO)bib.get(i);
			if(bibAppo.getCod_bib().equals(bibl.getCod_bib())){
				biblioSel = bibAppo.getNom_biblioteca();
				polo = bibAppo.getCod_polo();
				idUteBib = bibAppo.getId_utenti_biblioteca();
				codBib = bibAppo.getCod_bib();
			}

		}
		//
		session.setAttribute(Constants.ID_UTE_BIB,idUteBib );
		session.setAttribute(Constants.BIBLIO_SEL,biblioSel );
		session.setAttribute(Constants.POLO,polo );
		session.setAttribute(Constants.COD_BIBLIO,codBib);
		session.removeAttribute(Constants.USER_MENU);
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();;
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		Navigation navi = Navigation.getInstance(request);
		boolean remoto = factory.getGestioneServiziWeb().setRemote(utente, polo, codBib, navi.getUserAddress());
		utente.setRemoto(remoto);

		/*
		if (remoto==false) {
			//Invio msg. "errore nella selezione della bib."
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.selezioneBib"));
			this.saveErrors(request, errors);
		}
		//if  (currentForm.getInsRichOpac().length()>0) {
		//	return (mapping.findForward("listaMovimentiOpac"));//mappa selezione del servizio
		//}
		*/
		ServiziWebDelegate delegate = ServiziWebDelegate.getInstance(request);;
		boolean abilitaRemoto = delegate.isAbilitatoInserimentoRemoto(polo, codBib, utente);
		navi.purgeThis();

		if (!utente.isCompleto())
			LinkableTagUtils.addError(request, new ActionMessage("message.utenti.dati.incompleti"));

		if (abilitaRemoto)
			return (mapping.findForward("menuServizi"));
		else
			return (mapping.findForward("esameRichieste"));

	}

	// almaviva 2009
	public ActionForward iscrizione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

		SelezioneBibliotecaForm currentForm = (SelezioneBibliotecaForm) form;
		HttpSession session = request.getSession();

		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		Navigation navi = Navigation.getInstance(request);
		String polo = navi.getPolo();

		try {
			//iscrizione ad una nuova bib. del polo
			//se ha selezionato una bib. procedo con l'iscrizione
			BibliotecaVO firstBib = utente.getListaBiblio().get(0);
			String ticket = Ticket.getUtenteWebTicket(polo, firstBib.getCod_bib(), navi.getUserAddress());

			RicercaUtenteBibliotecaVO recUte = new RicercaUtenteBibliotecaVO();

			recUte.setIdUte(firstBib.getId_utenti_biblioteca());

			UtenteBibliotecaVO dettaglio = factory.getGestioneServizi().getDettaglioUtente(ticket, recUte, null, null);
			BibliotecaVO bibSel = (BibliotecaVO) currentForm.getListaBiblioAuto().get(currentForm.getBiblioAuto().getPrg());

			dettaglio.setCodiceBiblioteca(currentForm.getBiblio().getCod_bib());
			dettaglio.setCodBibSer(bibSel.getCod_bib());

			Date now = new Date();
			String dataInizio = DateUtil.formattaData(now);
			String dataFine = "31/12/" + DateUtil.getYear(now);
			dettaglio.getBibliopolo().setInizioAuto(dataInizio);
			dettaglio.getBibliopolo().setFineAuto(dataFine);

			List<ServizioVO> diritti = factory.getGestioneServiziWeb().getListaServiziAutorizzazione(ticket, dettaglio);

			//almaviva5_20110127 #4178
			if (ValidazioneDati.isFilled(diritti)) {
				ServizioVO diritto = diritti.get(0);
				AutorizzazioniVO autorizzazioni = dettaglio.getAutorizzazioni();
				autorizzazioni.setServizi(diritti);
				autorizzazioni.setCodTipoAutor(diritto.getAutorizzazione());

				for (ServizioVO srv : diritti) {
					srv.setFlag_aut_ereditato(diritto.getAutorizzazione());
					srv.setDataInizio(dataInizio);
					srv.setDataFine(dataFine);
				}
			}

			boolean insUteWeb = false;
			boolean updUteBib = false;

			Integer idute = bibSel.getId_utenti_biblioteca();
			if (idute != null)
				updUteBib = factory.getGestioneServiziWeb().updateUtentiBib(idute);

			if (!updUteBib)
				 //se non è mai stato iscritto alla bib. inserisco
				 insUteWeb = factory.getGestioneServizi().importaUtente(ticket, dettaglio);

			if (insUteWeb || updUteBib) {
				//carico la combo delle bib. a qui è iscritto
				List listaBibiscr = factory.getGestioneServiziWeb().listaBiblioIscritto(polo, utente.getIdUtente());
				currentForm.setListaBiblio(listaBibiscr);
				List codBib = new ArrayList();
				if (ValidazioneDati.isFilled(currentForm.getListaBiblio()) ) {

					for (int i = 0; i < currentForm.getListaBiblio().size(); i++) {
						BibliotecaVO bib1 = (BibliotecaVO) currentForm.getListaBiblio().get(i);
						codBib.add(bib1.getCod_bib());

					}

				}
				//carico la combo delle bib. alle quali si puo iscrivere
				List listaBibautoLogicamente = factory.getGestioneServiziWeb().listaBiblioAuto(polo, utente.getIdUtente(), codBib);

				//se ci sono bib.
				if (ValidazioneDati.isFilled(listaBibautoLogicamente) ) {
					//caricamento della lista delle biblioteche
					//che ammettono autoregistrazione da web
					currentForm.setListaBiblioAuto(listaBibautoLogicamente);
				}else {
					List listaBibautoNonIscritto = factory.getGestioneServiziWeb().listaBiblioNonIscr(polo, utente.getIdUtente(), codBib);
					currentForm.setListaBiblioAuto(listaBibautoNonIscritto);
				}
				//Invio msg. "Iscrizione effettuata con successo"
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.iscrizioneOk"));
				this.saveErrors(request, errors);

				return (mapping.getInputForward());
			} else {
				//invio msg: Iscrizione fallita riprovare.
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.aiscrizioneNotOk"));
				this.saveErrors(request, errors);
				//
				return (mapping.getInputForward());
				//
			}


		} catch (Exception e) {
			//errore di Iscrizione
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.aiscrizioneNotOk"));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		}

	}
}
