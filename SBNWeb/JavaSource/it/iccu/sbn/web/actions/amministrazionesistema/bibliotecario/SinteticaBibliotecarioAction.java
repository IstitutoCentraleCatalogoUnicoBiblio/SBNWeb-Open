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
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario.SinteticaBibliotecarioForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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

public final class SinteticaBibliotecarioAction extends SinteticaLookupDispatchAction {

    static Logger log = Logger.getLogger(SinteticaBibliotecarioAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.bibliotecario.button.indietro",		"cerca");
		map.put("ricerca.bibliotecario.button.nuovo", 			"nuovo");
		map.put("ricerca.bibliotecario.button.modifica", 		"modifica");
		map.put("ricerca.bibliotecario.button.scheda",	 		"modifica");
		map.put("ricerca.bibliotecario.button.abilitazioni", 	"abilitazioni");
		map.put("ricerca.bibliotecario.button.profilo", 		"abilitazioni");
		map.put("ricerca.bibliotecario.button.scegli", 			"scegli");
		map.put("button.blocco", "blocco");
		return map;
	}

	class ordinaCognomeAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {
		boolean x = false;
		boolean y = false;
		UtenteVO gp = (UtenteVO)a;
		String sa = "";
		if (gp.getCognome() != null)
			sa = gp.getCognome();
		else
			x = true;
		String sb = "";
		gp = (UtenteVO)b;
		if (gp.getCognome() != null)
			sb = gp.getCognome();
		else
			y = true;
		if (x && !y)
			return 1;
		if (!x && y)
			return -1;
		if (x && y)
			return 0;
		return( (sa).compareTo( sb )); // Ascending

	   } // end compare

	} // end class StringComparator

	class ordinaCognomeDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {
		boolean x = false;
		boolean y = false;
		UtenteVO gp = (UtenteVO)a;
		String sa = "";
		if (gp.getCognome() != null)
			sa = gp.getCognome();
		else
			x = true;
		String sb = "";
		gp = (UtenteVO)b;
		if (gp.getCognome() != null)
			sb = gp.getCognome();
		else
			y = true;
		if (x && !y)
			return 1;
		if (!x && y)
			return -1;
		if (x && y)
			return 0;
		return( (sb).compareTo( sa )); // Ascending

	   } // end compare

	} // end class StringComparator

	class ordinaNomeAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {
		boolean x = false;
		boolean y = false;
		UtenteVO gp = (UtenteVO)a;
		String sa = "";
		if (gp.getNome() != null)
			sa = gp.getNome();
		else
			x = true;
		String sb = "";
		gp = (UtenteVO)b;
		if (gp.getNome() != null)
			sb = gp.getNome();
		else
			y = true;
		if (x && !y)
			return 1;
		if (!x && y)
			return -1;
		if (x && y)
			return 0;
		return( (sa).compareTo( sb )); // Ascending

	   } // end compare

	} // end class StringComparator

	class ordinaNomeDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {
		boolean x = false;
		boolean y = false;
		UtenteVO gp = (UtenteVO)a;
		String sa = "";
		if (gp.getNome() != null)
			sa = gp.getNome();
		else
			x = true;
		String sb = "";
		gp = (UtenteVO)b;
		if (gp.getNome() != null)
			sb = gp.getNome();
		else
			y = true;
		if (x && !y)
			return 1;
		if (!x && y)
			return -1;
		if (x && y)
			return 0;
		return( (sb).compareTo( sa )); // Ascending

	   } // end compare

	} // end class StringComparator

	class ordinaUsernameAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		UtenteVO gp = (UtenteVO)a;
		String sa = "";
		if (gp.getUsername() != null)
			sa = gp.getUsername();
		else
			x = true;
		String sb = "";
		gp = (UtenteVO)b;
		if (gp.getUsername() != null)
			sb = gp.getUsername();
		else
			y = true;
		if (x && !y)
			return -1;
		if (!x && y)
			return 1;
		if (x && y)
			return 0;
		return( (sa).compareTo( sb )); // Ascending

	   } // end compare

	} // end class StringComparator

	class ordinaUsernameDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {
		boolean x = false;
		boolean y = false;
		UtenteVO gp = (UtenteVO)a;
		String sa = "";
		if (gp.getUsername() != null)
			sa = gp.getUsername();
		else
			x = true;
		String sb = "";
		gp = (UtenteVO)b;
		if (gp.getUsername() != null)
			sb = gp.getUsername();
		else
			y = true;
		if (x && !y)
			return -1;
		if (!x && y)
			return 1;
		if (x && y)
			return 0;
		return( (sb).compareTo( sa )); // Ascending

	   } // end compare

	} // end class StringComparator

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        Utente utente = (Utente)request.getSession().getAttribute(Constants.UTENTE_BEAN);
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().ANAGRAFICA_UTENTE);
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_UTENTE);
        }
        catch (UtenteNotAuthorizedException e) {

            LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));

            return mapping.findForward("blank");
        }
        SinteticaBibliotecarioForm myForm = (SinteticaBibliotecarioForm)form;
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ANAGRAFICA_UTENTE);
            myForm.setAbilitatoNuovo("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	myForm.setAbilitatoNuovo("FALSE");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ABILITAZIONI_UTENTE);
            myForm.setAbilitatoProfiloRead("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
            myForm.setAbilitatoProfiloRead("FALSE");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_UTENTE);
            if (myForm.getAbilitatoProfiloRead().equals("TRUE") && myForm.getAbilitatoNuovo().equals("TRUE")) {
	            myForm.setAbilitatoProfiloWrite("TRUE");
	            myForm.setAbilitatoProfiloRead("FALSE");
            }
            else
            	myForm.setAbilitatoProfiloWrite("FALSE");
        }
        catch (UtenteNotAuthorizedException e) {
            myForm.setAbilitatoProfiloWrite("FALSE");
        }

		String cmd = request.getParameter("cmd");
		if (cmd != null && cmd.equals("cognome")) {
			String ordinamento = myForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "cognomeAsc";
			List<UtenteVO> utenti = myForm.getElencoUtenti();
			if (ordinamento!= null && ordinamento.equals("cognomeAsc")) {
				Collections.sort(utenti, new ordinaCognomeDec());
				myForm.setOrdinamento("cognomeDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(utenti, new ordinaCognomeAsc());
				myForm.setOrdinamento("cognomeAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("nome")) {
			String ordinamento = myForm.getOrdinamento();
			List<UtenteVO> utenti = myForm.getElencoUtenti();
			if (ordinamento!= null && ordinamento.equals("nomeAsc")) {
				Collections.sort(utenti, new ordinaNomeDec());
				myForm.setOrdinamento("nomeDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(utenti, new ordinaNomeAsc());
				myForm.setOrdinamento("nomeAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals(NavigazioneProfilazione.USERNAME)) {
			String ordinamento = myForm.getOrdinamento();
			List<UtenteVO> utenti = myForm.getElencoUtenti();
			if (ordinamento!= null && ordinamento.equals("usernameAsc")) {
				Collections.sort(utenti, new ordinaUsernameDec());
				myForm.setOrdinamento("usernameDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(utenti, new ordinaUsernameAsc());
				myForm.setOrdinamento("usernameAsc");
				return mapping.getInputForward();
			}
		}

		//Manutenzione almaviva2 21.09.2011 -  inserite le chiamate per la lentina del biblotecario (ute ins e var)
		if (request.getAttribute("acquisizioni") != null && ((String)request.getAttribute("acquisizioni")).equals("TRUE")){
			myForm.setAcquisizioni("TRUE");
		}else if (request.getAttribute("stampaEtichette") != null && ((String)request.getAttribute("stampaEtichette")).equals("TRUE")){
			myForm.setStampaEtichette("TRUE");
			myForm.setAbilitatoNuovo("FALSE");
			myForm.setAbilitatoProfiloRead("FALSE");
			myForm.setAbilitatoProfiloWrite("FALSE");
		}else if (request.getAttribute("estrattoreIdDocumenti") != null && ((String)request.getAttribute("estrattoreIdDocumenti")).equals("TRUE")){
			myForm.setEstrattoreIdDocumenti("TRUE");
			myForm.setAbilitatoNuovo("FALSE");
			myForm.setAbilitatoProfiloRead("FALSE");
			myForm.setAbilitatoProfiloWrite("FALSE");
		}else{
			myForm.setAcquisizioni("FALSE");
		}

		if (myForm.getElencoUtenti().size() == 0) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String nome = (String)request.getAttribute("nome");
			String cognome = (String)request.getAttribute("cognome");
			String username = (String)request.getAttribute(NavigazioneProfilazione.USERNAME);
			String biblioteca = (String)request.getAttribute(NavigazioneProfilazione.BIBLIOTECA);
			//almaviva5_20151204 #6053
			myForm.setBiblioteca(biblioteca);

			Navigation navi = Navigation.getInstance(request);
			String ticket = navi.getUserTicket();
			String data = (String)request.getAttribute("data");
			String abilitato = (String)request.getAttribute("abilitato");
			String ordinamento = (String)request.getAttribute("ordinamento");

			DescrittoreBloccoVO blocco1 = factory.getSistema().cercaUtenti(ticket, cognome, nome, username, biblioteca, data, abilitato, ordinamento, myForm.getMaxRighe());
			if (blocco1.getLista() == null || blocco1.getLista().size() == 0) {

				LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.info.null"));

				//request.setAttribute("back", "true");
				//return mapping.findForward("torna");
				return navi.goBack(true);
			}
			if (blocco1.getLista().size() == 1)
				myForm.setSelezRadio(((UtenteVO)blocco1.getLista().get(0)).getId() + "");

			myForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			//memorizzo le informazioni per la gestione blocchi
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setBloccoCorrente(blocco1.getNumBlocco());

			myForm.setElencoUtenti(blocco1.getLista());
		}

		return mapping.getInputForward();
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("torna");
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("provenienza", "sintetica");
		//almaviva5_20151204 #6053
		SinteticaBibliotecarioForm currentForm = (SinteticaBibliotecarioForm)form;
		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, currentForm.getBiblioteca() );

		return mapping.findForward("nuovo");
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecarioForm myForm = (SinteticaBibliotecarioForm)form;
		String selezioneRadio = myForm.getSelezRadio();
		if (selezioneRadio == null || selezioneRadio.equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.info.noutente"));

			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneProfilazione.ID_UTENTE, selezioneRadio + "");
		request.setAttribute("provenienza", "sintetica");

		return mapping.findForward("nuovo");
	}

	public ActionForward abilitazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecarioForm myForm = (SinteticaBibliotecarioForm)form;
		String selezioneRadio = myForm.getSelezRadio();
		if (selezioneRadio == null || selezioneRadio.equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.info.noutente"));

			return mapping.getInputForward();
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if (!factory.getSistema().controllaAbilitazioneBibliotecario(Integer.parseInt(myForm.getSelezRadio()))) {

			LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.info.noabiliato"));

			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneProfilazione.ID_UTENTE, myForm.getSelezRadio());
		request.setAttribute("provenienza", "sintetica");
		return mapping.findForward("profilazione");
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecarioForm myForm = (SinteticaBibliotecarioForm)form;
		String selezioneRadio = myForm.getSelezRadio();
		if (selezioneRadio == null || selezioneRadio.equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.info.noutente"));

			return mapping.getInputForward();
		}

		List<UtenteVO> elencoUtenti = myForm.getElencoUtenti();
		for (int i = 0; i <elencoUtenti.size(); i++) {
			UtenteVO bibliotecario = elencoUtenti.get(i);
			if ((bibliotecario.getId() + "").equals(selezioneRadio)) {
				request.setAttribute("bibliotecario", bibliotecario);
				break;
			}
		}
		if (myForm.getStampaEtichette() != null && myForm.getStampaEtichette().equals("TRUE")){
			return mapping.findForward("stampaEtichette");
		}


		// Manutenzione almaviva2 21.09.2011 -  inserite le chiamate/nuovi campi per la lentina del biblotecario (ute ins e var)
		// richiamata dall'estrattore magno
		if (myForm.getEstrattoreIdDocumenti() != null && myForm.getEstrattoreIdDocumenti().equals("TRUE")){
			return mapping.findForward("estrattoreIdDocumenti");
		}


		Navigation navi = Navigation.getInstance(request);
		if (navi.bookmarkExists(RicercaBibliotecarioAction.SIF_BIBLIOTECARIO))
			return navi.goToBookmark(RicercaBibliotecarioAction.SIF_BIBLIOTECARIO, false);

		return mapping.findForward("acquisizioni");
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecarioForm myForm = (SinteticaBibliotecarioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		int numBlocco = myForm.getBloccoCorrente();
		String idLista = myForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getSistema().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				myForm.getElencoUtenti().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					 myForm.setBloccoCorrente(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (myForm.getElencoUtenti().size() == bloccoVO.getTotRighe())
					myForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

}
