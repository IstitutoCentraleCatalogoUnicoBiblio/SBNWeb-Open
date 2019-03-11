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
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario.RicercaBibliotecarioForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.text.SimpleDateFormat;
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
import org.apache.struts.actions.LookupDispatchAction;

public final class RicercaBibliotecarioAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(RicercaBibliotecarioAction.class);
    public static final String SIF_BIBLIOTECARIO = "sif.bibliotecario";

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.bibliotecario.button.cerca", 			"cerca");
		map.put("ricerca.bibliotecario.button.nuovo", 			"nuovo");
		map.put("ricerca.biblioteca.button.reset",				"reset");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		UtenteVO bibliotecario = (UtenteVO) request.getAttribute("bibliotecario");
		if (bibliotecario != null)
			return navi.goBack();

        Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        log.debug("RicercaBibliotecarioAction::unspecified");
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_UTENTE);

        } catch (UtenteNotAuthorizedException e) {

            LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));

            return mapping.findForward("blank");
        }

		RicercaBibliotecarioForm myForm = (RicercaBibliotecarioForm)form;
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_UTENTE);
            myForm.setAbilitatoNuovo("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	myForm.setAbilitatoNuovo("FALSE");
        }

		if (myForm.getNomeRic().equals("") && myForm.getCognomeRic().equals("") && myForm.getUsernameRic().equals("") && myForm.getElencoBib().size() == 0) {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String bibCorrente = navi.getUtente().getCodBib();
			List<ComboVO> elencoBib = new ArrayList<ComboVO>();

			List<ComboVO> elencoProva = new ArrayList<ComboVO>();
			elencoProva.addAll(factory.getSistema().getElencoBiblioteche());
			String poloCorrente = navi.getUtente().getCodPolo();
			String ticket = navi.getUserTicket();
			List<ComboVO> listaBibAff = factory
					.getSistema().getListaComboBibliotecheAffiliatePerAttivita(
							ticket,
							poloCorrente,
							bibCorrente,
							CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_UTENTE);
			elencoBib.addAll(listaBibAff);

			for (int i = 0; i < elencoBib.size(); i++) {
				ComboVO comboEl = elencoBib.get(i);
				comboEl.setDescrizione(comboEl.getCodice() + " " + comboEl.getDescrizione());
			}

			myForm.setElencoBib(elencoBib);
			myForm.setCheckAbilitato("tutti");

			List<ComboVO> elencoOrdinamento = loadOrdinamento();
			myForm.setElencoOrdinamenti(elencoOrdinamento);
			myForm.setSelezioneOrdinamento("cognome");
		}

        myForm.setAcquisizioni("FALSE");
        if (navi.getActionCaller() != null &&
        		navi.getActionCaller().equals("/acquisizioni/suggerimenti/suggerimentiBiblRicercaParziale")) {
        	myForm.setAcquisizioni("TRUE");
        	myForm.setCheckAbilitato("true");
        }
        if (request.getParameter("SIF") != null) {
        	myForm.setAcquisizioni("TRUE");
        	myForm.setCheckAbilitato("true");
        	navi.addBookmark(SIF_BIBLIOTECARIO);
        }

        myForm.setStampaEtichette("FALSE");
        if (navi.getActionCaller() != null &&
        		navi.getActionCaller().equals("/gestionestampe/etichette/stampaEtichette")) {
        	myForm.setStampaEtichette("TRUE");
        	myForm.setCheckAbilitato("true");
        	myForm.setAbilitatoNuovo("FALSE");
        }

      //Manutenzione almaviva2 21.09.2011 -  inserite le chiamate per la lentina del biblotecario (ute ins e var)
        myForm.setEstrattoreIdDocumenti("FALSE");
        if (navi.getActionCaller() != null &&
        		navi.getActionCaller().equals("/common/estrattoreIdDocumenti")) {
        	myForm.setEstrattoreIdDocumenti("TRUE");
        	myForm.setCheckAbilitato("true");
        	myForm.setAbilitatoNuovo("FALSE");
        }

		return
		mapping.getInputForward();
	}

	private List<ComboVO> loadOrdinamento() {
		List<ComboVO> elencoOrdinamento = new ArrayList<ComboVO>();
		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources.AmministrazioneSistemaLabels");
		ComboVO combo = new ComboVO();
		combo.setCodice("nome");
		combo.setDescrizione(bundle.getString("ricerca.bibliotecario.nome"));
		elencoOrdinamento.add(combo);
		combo = new ComboVO();
		combo.setCodice("cognome");
		combo.setDescrizione(bundle.getString("ricerca.bibliotecario.cognome"));
		elencoOrdinamento.add(combo);
		combo = new ComboVO();
		combo.setCodice(NavigazioneProfilazione.USERNAME);
		combo.setDescrizione(bundle.getString("ricerca.bibliotecario.username"));
		elencoOrdinamento.add(combo);
		return elencoOrdinamento;
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getAttribute("back") != null) {
			this.unspecified(mapping, form, request, response);
			return mapping.getInputForward();
		}
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		RicercaBibliotecarioForm myForm = (RicercaBibliotecarioForm)form;

		String cognome = myForm.getCognomeRic();
		String nome = myForm.getNomeRic();
		String username = myForm.getUsernameRic();
		String dataAccesso = myForm.getDataAccesso().trim();
		String checkAbilitato = myForm.getCheckAbilitato();

		if (!dataAccesso.equals("") && this.validaDataPassata(dataAccesso)!= 0) {

			LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.data"));

			return mapping.getInputForward();
		}
		request.setAttribute("data", dataAccesso);

		if (checkAbilitato.equals("tutti"))
			request.setAttribute("abilitato", "ALL");
		else if (checkAbilitato.equals("true"))
			request.setAttribute("abilitato", "TRUE");
		else
			request.setAttribute("abilitato", "FALSE");

		if (!myForm.isCheckEsattaCognome())
			cognome = cognome + "*";
		request.setAttribute("cognome", cognome);
		if (!myForm.isCheckEsattaNome())
			nome = nome + "*";
		request.setAttribute("nome", nome);
		if (!myForm.isCheckEsattaUsername())
			username = username + "*";
		request.setAttribute(NavigazioneProfilazione.USERNAME, username);
		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, myForm.getSelezioneBibRic());
		request.setAttribute("ordinamento", myForm.getSelezioneOrdinamento());
		if (myForm.getAcquisizioni() != null && myForm.getAcquisizioni().equals("TRUE"))
			request.setAttribute("acquisizioni", "TRUE");
		if (myForm.getStampaEtichette() != null && myForm.getStampaEtichette().equals("TRUE"))
			request.setAttribute("stampaEtichette", "TRUE");

		//Manutenzione almaviva2 21.09.2011 -  inserite le chiamate per la lentina del bibliotecario (ute ins e var)
		if (myForm.getEstrattoreIdDocumenti() != null && myForm.getEstrattoreIdDocumenti().equals("TRUE"))
			request.setAttribute("estrattoreIdDocumenti", "TRUE");


		return mapping.findForward("sintetica");
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.setAttribute("provenienza", "ricerca");
		//almaviva5_20151204 #6053
		RicercaBibliotecarioForm currentForm = (RicercaBibliotecarioForm)form;
		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, currentForm.getSelezioneBibRic() );
		return mapping.findForward("nuovo");
	}

	public int validaDataPassata(String data) {
		int DATA_OK=0;
		int DATA_ERRATA=1;
		int DATA_PASSATA_ERRATA=3;

		int codRitorno=-1;
		if (data==null) {
			codRitorno=DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			//DateParser.parseDate(data);
			// l'istruzione sottostante va in errore se non non riesce a fare il parsing del rispetto del formato
			java.util.Date date = format.parse(data);
			log.debug(date);
			if (java.util.regex.Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				//Date oggi = new Date(System.currentTimeMillis());
				//if (date.after(oggi)) {
				//	codRitorno=DATA_MAGGIORE;
                //    throw new Exception(); // data > data odierna
				//}
				codRitorno=DATA_OK;
				return codRitorno; // tutto OK
			} else {
                codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}

	public ActionForward reset(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaBibliotecarioForm myForm = (RicercaBibliotecarioForm)form;
		myForm.setCognomeRic("");
		myForm.setCheckEsattaCognome(false);
		myForm.setCheckEsattaNome(false);
		myForm.setNomeRic("");
		myForm.setUsernameRic("");
		myForm.setCheckEsattaUsername(false);
		myForm.setSelezioneBibRic("");
		myForm.setCheckAbilitato("tutti");
		myForm.setDataAccesso("");
		myForm.setSelezioneOrdinamento("cognome");

		return mapping.getInputForward();
	}

}
