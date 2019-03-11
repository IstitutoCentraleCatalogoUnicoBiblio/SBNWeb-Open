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
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.SinteticaBibliotecaForm;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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

public final class SinteticaBibliotecaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

    static Logger log = Logger.getLogger(SinteticaBibliotecaAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.biblioteca.button.indietro", 		"cerca");
		map.put("ricerca.biblioteca.button.nuovo", 			"nuovo");
		map.put("ricerca.biblioteca.button.modifica", 		"modifica");
		map.put("ricerca.biblioteca.button.scheda", 		"modifica");
		map.put("ricerca.biblioteca.button.abilitazioni", 	"abilitazioni");
		map.put("ricerca.biblioteca.button.profilo",	 	"abilitazioni");
		map.put("ricerca.biblioteca.button.scegli",		 	"scegli");
		map.put("button.blocco", "blocco");
		return map;
	}

	class ordinaCodBibAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCod_bib() != null)
			sa = gp.getCod_bib();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCod_bib() != null)
			sb = gp.getCod_bib();
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

	class ordinaCodBibDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCod_bib() != null)
			sa = gp.getCod_bib();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCod_bib() != null)
			sb = gp.getCod_bib();
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

	class ordinaCodAnaAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCd_ana_biblioteca() != null)
			sa = gp.getCd_ana_biblioteca();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCd_ana_biblioteca() != null)
			sb = gp.getCd_ana_biblioteca();
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

	class ordinaCodAnaDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCd_ana_biblioteca() != null)
			sa = gp.getCd_ana_biblioteca();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCd_ana_biblioteca() != null)
			sb = gp.getCd_ana_biblioteca();
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
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getNom_biblioteca() != null)
			sa = gp.getNom_biblioteca();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getNom_biblioteca() != null)
			sb = gp.getNom_biblioteca();
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
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getNom_biblioteca() != null)
			sa = gp.getNom_biblioteca();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getNom_biblioteca() != null)
			sb = gp.getNom_biblioteca();
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

	class ordinaIndirizzoAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getIndirizzo() != null)
			sa = gp.getIndirizzo();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getIndirizzo() != null)
			sb = gp.getIndirizzo();
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

	class ordinaIndirizzoDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getIndirizzo() != null)
			sa = gp.getIndirizzo();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getIndirizzo() != null)
			sb = gp.getIndirizzo();
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

	class ordinaCapAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCap() != null)
			sa = gp.getCap();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCap() != null)
			sb = gp.getCap();
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

	class ordinaCapDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCap() != null)
			sa = gp.getCap();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCap() != null)
			sb = gp.getCap();
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

	class ordinaCittaAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getLocalita() != null)
			sa = gp.getLocalita();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getLocalita() != null)
			sb = gp.getLocalita();
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

	class ordinaCittaDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getLocalita() != null)
			sa = gp.getLocalita();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getLocalita() != null)
			sb = gp.getLocalita();
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

	class ordinaProvinciaAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getProvincia() != null)
			sa = gp.getProvincia();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getProvincia() != null)
			sb = gp.getProvincia();
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

	class ordinaProvinciaDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getProvincia() != null)
			sa = gp.getProvincia();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getProvincia() != null)
			sb = gp.getProvincia();
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

	class ordinaPoloAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCod_polo() != null)
			sa = gp.getCod_polo();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCod_polo() != null)
			sb = gp.getCod_polo();
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

	class ordinaPoloDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getCod_polo() != null)
			sa = gp.getCod_polo();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getCod_polo() != null)
			sb = gp.getCod_polo();
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

	class ordinaTipoAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getTipo_biblioteca() != null)
			sa = gp.getTipo_biblioteca();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getTipo_biblioteca() != null)
			sb = gp.getTipo_biblioteca();
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

	class ordinaTipoDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getTipo_biblioteca() != null)
			sa = gp.getTipo_biblioteca();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getTipo_biblioteca() != null)
			sb = gp.getTipo_biblioteca();
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

	class ordinaFlagAsc implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getFlag_bib() != null)
			sa = gp.getFlag_bib();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getFlag_bib() != null)
			sb = gp.getFlag_bib();
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

	class ordinaFlagDec implements Comparator<Object>
	{
	public final int compare ( Object a, Object b )
	   {

		boolean x = false;
		boolean y = false;
		BibliotecaVO gp = (BibliotecaVO)a;
		String sa = "";
		if (gp.getFlag_bib() != null)
			sa = gp.getFlag_bib();
		else
			x = true;
		String sb = "";
		gp = (BibliotecaVO)b;
		if (gp.getFlag_bib() != null)
			sb = gp.getFlag_bib();
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

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecaForm currenForm = (SinteticaBibliotecaForm)form;

		if (Navigation.getInstance(request).isFromBar() )
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
            currenForm.setAbilitatoNuovo("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
        	currenForm.setAbilitatoNuovo("FALSE");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ABILITAZIONI_BIBLIOTECA);
            currenForm.setAbilitatoProfiloRead("TRUE");
        }
        catch (UtenteNotAuthorizedException e) {
            currenForm.setAbilitatoProfiloRead("FALSE");
        }
        try {
            utente.checkAttivita(CodiciAttivita.getIstance().GESTIONE_ABILITAZIONI_BIBLIOTECA);
            if (currenForm.getAbilitatoProfiloRead().equals("TRUE") && currenForm.getAbilitatoNuovo().equals("TRUE")) {
	            currenForm.setAbilitatoProfiloWrite("TRUE");
	            currenForm.setAbilitatoProfiloRead("FALSE");
            }
            else
            	currenForm.setAbilitatoProfiloWrite("FALSE");
        }
        catch (UtenteNotAuthorizedException e) {
            currenForm.setAbilitatoProfiloWrite("FALSE");
        }

		String cmd = request.getParameter("cmd");

		if (cmd != null && cmd.equals(NavigazioneProfilazione.COD_BIBLIOTECA)) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "codBibAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("codBibAsc")) {
				Collections.sort(biblioteche, new ordinaCodBibDec());
				currenForm.setOrdinamento("codBibDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaCodBibAsc());
				currenForm.setOrdinamento("codBibAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("codAna")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "codAnaAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("codAnaAsc")) {
				Collections.sort(biblioteche, new ordinaCodAnaDec());
				currenForm.setOrdinamento("codAnaDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaCodAnaAsc());
				currenForm.setOrdinamento("codAnaAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("nome")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "nomeAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("nomeAsc")) {
				Collections.sort(biblioteche, new ordinaNomeDec());
				currenForm.setOrdinamento("nomeDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaNomeAsc());
				currenForm.setOrdinamento("nomeAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("indirizzo")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "indirizzoAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("indirizzoAsc")) {
				//almaviva5_20100202 #3492
				Collections.sort(biblioteche, ValidazioneDati.invertiComparatore(BibliotecaVO.ORDINAMENTO_PER_INDIRIZZO_COMPOSTO));
				currenForm.setOrdinamento("indirizzoDec");
				return mapping.getInputForward();
			}
			else {
				//almaviva5_20100202 #3492
				Collections.sort(biblioteche, BibliotecaVO.ORDINAMENTO_PER_INDIRIZZO_COMPOSTO);
				currenForm.setOrdinamento("indirizzoAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("cap")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "capAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("capAsc")) {
				Collections.sort(biblioteche, new ordinaCapDec());
				currenForm.setOrdinamento("capDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaCapAsc());
				currenForm.setOrdinamento("capAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("citta")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "cittaAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("cittaAsc")) {
				Collections.sort(biblioteche, new ordinaCittaDec());
				currenForm.setOrdinamento("cittaDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaCittaAsc());
				currenForm.setOrdinamento("cittaAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("provincia")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "provinciaAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("provinciaAsc")) {
				Collections.sort(biblioteche, new ordinaProvinciaDec());
				currenForm.setOrdinamento("provinciaDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaProvinciaAsc());
				currenForm.setOrdinamento("provinciaAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("polo")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "poloAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("poloAsc")) {
				Collections.sort(biblioteche, new ordinaPoloDec());
				currenForm.setOrdinamento("poloDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaPoloAsc());
				currenForm.setOrdinamento("poloAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("tipo")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "tipoAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("tipoAsc")) {
				Collections.sort(biblioteche, new ordinaTipoDec());
				currenForm.setOrdinamento("tipoDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaTipoAsc());
				currenForm.setOrdinamento("tipoAsc");
				return mapping.getInputForward();
			}
		}
		if (cmd != null && cmd.equals("flag")) {
			String ordinamento = currenForm.getOrdinamento();
			if (ordinamento == null)
				ordinamento = "flagAsc";
			List<BibliotecaVO> biblioteche = currenForm.getElencoBiblioteche();
			if (ordinamento!= null && ordinamento.equals("flagAsc")) {
				Collections.sort(biblioteche, new ordinaFlagDec());
				currenForm.setOrdinamento("flagDec");
				return mapping.getInputForward();
			}
			else {
				Collections.sort(biblioteche, new ordinaFlagAsc());
				currenForm.setOrdinamento("flagAsc");
				return mapping.getInputForward();
			}
		}

		if (request.getAttribute("acquisizioni") != null && !((String)request.getAttribute("acquisizioni")).equals("FALSE")){
			currenForm.setAcquisizioni((String)request.getAttribute("acquisizioni"));
		}else if (ValidazioneDati.in(request.getAttribute("servizioUtente"), "UTENTE", "LISTA_MOVIMENTI", "RICERCA_ILL")){
			currenForm.setServizioUtente((String)request.getAttribute("servizioUtente"));
		}else if (request.getAttribute("scaricoInventariale") != null && (request.getAttribute("scaricoInventariale")).equals("scaricoInventariale")){
				currenForm.setScaricoInventariale((String)request.getAttribute("scaricoInventariale"));
		}else if (request.getAttribute("modificaInvColl") != null && (request.getAttribute("modificaInvColl")).equals("modificaInvColl")){
			currenForm.setScaricoInventariale((String)request.getAttribute("modificaInvColl"));
		}else{
			currenForm.setAcquisizioni("FALSE");
			currenForm.setScaricoInventariale("FALSE");
		}

		if (!ValidazioneDati.isFilled(currenForm.getElencoBiblioteche()) ) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			BibliotecaRicercaVO richiesta = (BibliotecaRicercaVO) request.getAttribute(BibliotecaDelegate.PARAMETRI_RICERCA_BIBLIOTECA);

			String ticket = Navigation.getInstance(request).getUserTicket();

			DescrittoreBloccoVO blocco1 = factory.getSistema().cercaBiblioteche(ticket, richiesta);
			if (!DescrittoreBloccoVO.isFilled(blocco1) ) {
				LinkableTagUtils.addError(request, new ActionMessage("ricerca.bibliotecario.info.null"));
				request.setAttribute("back", "true");
				return mapping.findForward("torna");
			}
			if (ValidazioneDati.size(blocco1.getLista()) == 1)
				currenForm.setSelezRadio(((BibliotecaVO)blocco1.getLista().get(0)).getIdBiblioteca() + "");
			currenForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			//memorizzo le informazioni per la gestione blocchi
			currenForm.setIdLista(blocco1.getIdLista());
			currenForm.setTotRighe(blocco1.getTotRighe());
			currenForm.setTotBlocchi(blocco1.getTotBlocchi());
			currenForm.setBloccoCorrente(blocco1.getNumBlocco());
			currenForm.setMaxRighe(blocco1.getMaxRighe());

			currenForm.setElencoBiblioteche(blocco1.getLista());

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

		return mapping.findForward("nuovo");
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecaForm myForm = (SinteticaBibliotecaForm)form;
		String selezioneRadio = myForm.getSelezRadio();
		if (selezioneRadio == null || selezioneRadio.equals("")) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("ricerca.biblioteca.info.nobiblio"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}
		request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, selezioneRadio);
		request.setAttribute("provenienza", "sintetica");

		return mapping.findForward("nuovo");
	}

	public ActionForward abilitazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecaForm myForm = (SinteticaBibliotecaForm)form;
		String selezioneRadio = myForm.getSelezRadio();
		if (selezioneRadio == null || selezioneRadio.equals("")) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("ricerca.biblioteca.info.nobiblio"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if (!factory.getSistema().controllaAbilitazioneBiblioteca(Integer.parseInt(myForm.getSelezRadio()))) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("ricerca.biblioteca.info.noabiliato"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}

		List<BibliotecaVO> elencoBiblioteche = myForm.getElencoBiblioteche();
		for (int i = 0; i < elencoBiblioteche.size(); i++) {
			BibliotecaVO bib = elencoBiblioteche.get(i);
			if (bib.getIdBiblioteca() == Integer.parseInt(selezioneRadio)) {
				request.setAttribute("nomebiblioteca", bib.getNom_biblioteca());
				request.setAttribute("codbiblioteca", bib.getCod_bib());
				request.setAttribute("recapitobiblioteca", bib.getIndirizzo() + " - " + bib.getLocalita());
				request.setAttribute("idbiblioteca", bib.getIdBiblioteca());
				break;
			}
		}
		request.setAttribute("provenienza", "sintetica");
		return mapping.findForward("profilazione");
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecaForm myForm = (SinteticaBibliotecaForm) form;
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
				myForm.getElencoBiblioteche().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					 myForm.setBloccoCorrente(bloccoVO.getNumBlocco());
				// ho caricato tutte le righe sulla form
				if (myForm.getElencoBiblioteche().size() == bloccoVO.getTotRighe())
					myForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaBibliotecaForm myForm = (SinteticaBibliotecaForm)form;
		String selezioneRadio = myForm.getSelezRadio();
		if (selezioneRadio == null || selezioneRadio.equals("")) {
			ActionMessages messaggio = new ActionMessages();
			messaggio.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("ricerca.biblioteca.info.nobiblio"));
			this.saveErrors(request,messaggio);
			return mapping.getInputForward();
		}

		List<BibliotecaVO> elencoBiblio = myForm.getElencoBiblioteche();
		for (int i = 0; i <elencoBiblio.size(); i++) {
			BibliotecaVO biblioteca = elencoBiblio.get(i);
			if ((biblioteca.getIdBiblioteca() + "").equals(selezioneRadio)) {
				biblioteca = BibliotecaDelegate.getInstance(request).getBiblioteca(biblioteca.getIdBiblioteca());
				request.setAttribute(NavigazioneProfilazione.BIBLIOTECA, biblioteca);
				break;
			}
		}
		if (myForm.getAcquisizioni() != null && myForm.getAcquisizioni().equals("FORNITORE")){
			return mapping.findForward("fornitore");
		}else if (myForm.getAcquisizioni() != null && myForm.getAcquisizioni().equals("ESAMINA")){
			return mapping.findForward("esamina");
		}else if (myForm.getScaricoInventariale() != null && myForm.getScaricoInventariale().equals("scaricoInventariale")){
			request.setAttribute("prov", "sinteticaBiblioteche");
			return mapping.findForward("scaricoInventariale");
		}else if (myForm.getScaricoInventariale() != null && myForm.getScaricoInventariale().equals("modificaInvColl")){
			request.setAttribute("prov", "sinteticaBiblioteche");
			return mapping.findForward("modificaInvColl");
		}else if (myForm.getServizioUtente() != null && myForm.getServizioUtente().equals("UTENTE")){
			return mapping.findForward(NavigazioneProfilazione.ID_UTENTE);
		}else if (myForm.getServizioUtente() != null && myForm.getServizioUtente().equals("LISTA_MOVIMENTI")){
			return Navigation.getInstance(request).goToBookmark(Bookmark.Servizi.LISTA_MOVIMENTI, false);
		}else if (myForm.getServizioUtente() != null && myForm.getServizioUtente().equals("RICERCA_ILL")){
			return Navigation.getInstance(request).goToBookmark(Bookmark.Servizi.EROGAZIONE_RICERCA_ILL, false);
		}else{
			return mapping.getInputForward();
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		if (ValidazioneDati.equals(idCheck, "SERVIZI_ILL")) {
		/*
			SinteticaBibliotecaForm currentForm = (SinteticaBibliotecaForm) form;
			return currentForm.getAbilitatoNuovo().equals("TRUE")
					&& currentForm.getServizioUtente().equals("LISTA_MOVIMENTI");
		*/
			return false;
		}
		return false;
	}

}
