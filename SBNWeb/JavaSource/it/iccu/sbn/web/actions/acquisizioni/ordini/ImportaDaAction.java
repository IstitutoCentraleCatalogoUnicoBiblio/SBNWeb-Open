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
package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.ImportaDaForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web2.navigation.Navigation;

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

public class ImportaDaAction extends LookupDispatchAction {

	//private ImportaDaForm importFrom;


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.conferma","Si");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
		        return mapping.getInputForward();

			OrdiniVO eleOrdineAttivo= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
			if (eleOrdineAttivo!=null)
			{
				// errore
			}
			navi.addBookmark(Bookmark.Acquisizioni.Ordini.IMPORTA_DATI_ORDINE);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ImportaDaForm importFrom = (ImportaDaForm ) form;
		try {
			request.getSession().removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ImportaDaForm importFrom = (ImportaDaForm ) form;
		try {
    		ActionMessages errors = new ActionMessages();
    		importFrom.setConferma(true);
    		errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		this.saveErrors(request, errors);
    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward bilancioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ImportaDaForm importFrom = (ImportaDaForm ) form;
		try {
			this.impostaBilancioCerca(request,mapping);
			return mapping.findForward("bilancioCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}*/

	private void impostaDocumentiCerca( HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {

		OrdiniVO eleOrdineAttivo= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
		ListaSuppDocumentoVO eleRicerca=new ListaSuppDocumentoVO();
		String ticket=Navigation.getInstance(request).getUserTicket();
		String codP=eleOrdineAttivo.getCodPolo();
		String codB=eleOrdineAttivo.getCodBibl();
		String codDoc="";
		String statoSuggDoc="A"; // solo accettati
		StrutturaTerna ute=new StrutturaTerna("","","");
		//ute.setCodice1(ricDocumenti.getCodUtenteBibl());
		//ute.setCodice2(ricDocumenti.getCodUtenteProg());
		StrutturaCombo tit=new StrutturaCombo("","");
		tit.setCodice(eleOrdineAttivo.getTitolo().getCodice());
		tit.setDescrizione(eleOrdineAttivo.getTitolo().getDescrizione());
		String dataDa="";
		String dataA="";
		String edi="";
		String luogo="";
		StrutturaCombo pae=new StrutturaCombo("","");
		StrutturaCombo lin=new StrutturaCombo("","");
		String annoEdi="";
		String chiama=(String)request.getSession().getAttribute("chiamante");
		String ordina="";
		eleRicerca=new ListaSuppDocumentoVO( codP,  codB,  codDoc,  statoSuggDoc,  ute,  tit,  dataDa,  dataA,   edi,  luogo,  pae,  lin,  annoEdi,    chiama,  ordina );
		eleRicerca.setTicket(ticket);
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppDocumentoVO", eleRicerca);

	}catch (Exception e) {	}
	}


	private void impostaOfferteCerca( HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {

		OrdiniVO eleOrdineAttivo= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
		ListaSuppOffertaFornitoreVO eleRicerca=new ListaSuppOffertaFornitoreVO();
		String ticket=Navigation.getInstance(request).getUserTicket();

		String codP=eleOrdineAttivo.getCodPolo();
		String codB=eleOrdineAttivo.getCodBibl();
		String idOff="";
		StrutturaCombo forn=new StrutturaCombo("","");
		forn.setCodice(eleOrdineAttivo.getFornitore().getCodice());
		forn.setDescrizione(eleOrdineAttivo.getFornitore().getDescrizione());
		String statoOff="A"; // stato offerta da precisare solo quelle accettate
		StrutturaCombo pae=new StrutturaCombo("","");
		StrutturaCombo ling=new StrutturaCombo("","");
		StrutturaCombo bidOff=new StrutturaCombo("","");

		StrutturaCombo KTitIsdb=new StrutturaCombo("","");
		KTitIsdb.setDescrizione(eleOrdineAttivo.getTitolo().getDescrizione());

		StrutturaCombo aut=new StrutturaCombo("","");
		//aut.setCodice(ricOfferte.getAutore());

		StrutturaCombo classif=new StrutturaCombo("","");
		//classif.setCodice(ricOfferte.getClassificazione());

		String chiama=(String)request.getSession().getAttribute("chiamante");
		String ordina="";

		eleRicerca=new ListaSuppOffertaFornitoreVO( codP,  codB,   idOff,   forn,  statoOff,  pae ,  ling,   bidOff,  KTitIsdb, aut, classif,  chiama,  ordina );
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		request.getSession().setAttribute("attributeListaSuppOffertaFornitoreVO", eleRicerca);


	}catch (Exception e) {	}
	}

	private void impostaSuggerimentiCerca( HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {

		OrdiniVO eleOrdineAttivo= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
		ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
		String ticket=Navigation.getInstance(request).getUserTicket();
		String polo=eleOrdineAttivo.getCodPolo();
		String bibl=eleOrdineAttivo.getCodBibl();
		String codSugg="";
		String statoSugg="A"; // precisare lo stato accettato
		String dataSuggDa="";
		String dataSuggA="";
		StrutturaCombo titSugg=new StrutturaCombo("","");
		titSugg.setCodice(eleOrdineAttivo.getTitolo().getCodice());
		titSugg.setDescrizione(eleOrdineAttivo.getTitolo().getDescrizione());
		StrutturaCombo biblSugg=new StrutturaCombo("","");
		//biblSugg.setCodice(ricSuggerimenti.getCodBibliotec());
		StrutturaCombo sezSugg=new StrutturaCombo("","");
		String chiama=(String)request.getSession().getAttribute("chiamante");
		String ordina="";

		eleRicerca=new ListaSuppSuggerimentoVO( polo,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", eleRicerca);

	}catch (Exception e) {	}
	}

	private void impostaGareCerca( HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {

		OrdiniVO eleOrdineAttivo= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
		ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO();
		String ticket=Navigation.getInstance(request).getUserTicket();

		String codP=eleOrdineAttivo.getCodPolo();
		String codB=eleOrdineAttivo.getCodBibl();
		StrutturaCombo idtitolo=new StrutturaCombo("","");
		idtitolo.setCodice(eleOrdineAttivo.getTitolo().getCodice());
		idtitolo.setDescrizione(eleOrdineAttivo.getTitolo().getDescrizione());
		String codRich="";
		String dataRich="";
		String statoRich="2"; // da precisare se solo quelle chiuse
		String chiama=(String)request.getSession().getAttribute("chiamante");
		String ordina="";
		eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina );
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		request.getSession().setAttribute("attributeListaSuppGaraVO", eleRicerca);

	}catch (Exception e) {	}
	}


	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		ImportaDaForm importFrom = (ImportaDaForm ) form;

		try {
			//importFrom.setConferma(false);
			if (importFrom.getSelectedImportaDa().equals("documenti"))
			{
				//Documenti non SBN
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppDocumentoVO")==null
				request.getSession().removeAttribute("attributeListaSuppDocumentoVO");
				request.getSession().removeAttribute("documentiSelected");
				request.getSession().removeAttribute("criteriRicercaDocumento");

/*				if (request.getSession().getAttribute("criteriRicercaDocumento")==null )
				{
					request.getSession().removeAttribute("attributeListaSuppDocumentoVO");
					request.getSession().removeAttribute("documentiSelected");
					request.getSession().removeAttribute("criteriRicercaDocumento");
				}
				else
				{
					//throw new Exception("limite di ricorsione");
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
*/
				this.impostaDocumentiCerca(request,mapping);
				return mapping.findForward("salva1");
			}
			else if (importFrom.getSelectedImportaDa().equals("suggerimenti"))
			{
				//suggerimenti
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppSuggerimentoVO")==null
				request.getSession().removeAttribute("attributeListaSuppSuggerimentoVO");
				request.getSession().removeAttribute("suggerimentiSelected");
				request.getSession().removeAttribute("criteriRicercaSuggerimento");

/*				if (request.getSession().getAttribute("criteriRicercaSuggerimento")==null )
				{
					request.getSession().removeAttribute("attributeListaSuppSuggerimentoVO");
					request.getSession().removeAttribute("suggerimentiSelected");
					request.getSession().removeAttribute("criteriRicercaSuggerimento");
				}
				else
				{
					//throw new Exception("limite di ricorsione");
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
*/
				this.impostaSuggerimentiCerca(request,mapping);
				return mapping.findForward("salva2");
			}
			else if (importFrom.getSelectedImportaDa().equals("offerte"))
			{
				//offerte
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppOffertaFornitoreVO")==null
				request.getSession().removeAttribute("attributeListaSuppOffertaFornitoreVO");
				request.getSession().removeAttribute("offerteSelected");
				request.getSession().removeAttribute("criteriRicercaOfferta");
				request.getSession().removeAttribute("ultimoBloccoOfferte");
				request.getSession().removeAttribute("listaOfferteEmessa");

/*				if (request.getSession().getAttribute("criteriRicercaOfferta")==null )
				{
					request.getSession().removeAttribute("attributeListaSuppOffertaFornitoreVO");
					request.getSession().removeAttribute("offerteSelected");
					request.getSession().removeAttribute("criteriRicercaOfferta");
					request.getSession().removeAttribute("ultimoBloccoOfferte");
					request.getSession().removeAttribute("listaOfferteEmessa");
				}
				else
				{
					//throw new Exception("limite di ricorsione");
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
*/
				this.impostaOfferteCerca(request,mapping);
				return mapping.findForward("salva3");
			}
			else if (importFrom.getSelectedImportaDa().equals("gare"))
			{
				//gare
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppGaraVO")==null
				request.getSession().removeAttribute("attributeListaSuppGaraVO");
				request.getSession().removeAttribute("gareSelected");
				request.getSession().removeAttribute("criteriRicercaGara");

/*				if (request.getSession().getAttribute("criteriRicercaGara")==null )
				{
					request.getSession().removeAttribute("attributeListaSuppGaraVO");
					request.getSession().removeAttribute("gareSelected");
					request.getSession().removeAttribute("criteriRicercaGara");
				}
				else
				{
					//throw new Exception("limite di ricorsione");
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
*/				this.impostaGareCerca(request,mapping);
				return mapping.findForward("salva4");
			}
			else
			{
				return mapping.getInputForward();
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ImportaDaForm importFrom = (ImportaDaForm ) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		importFrom.setConferma(false);
		return mapping.getInputForward();
	}



	private void loadImportaDa(ImportaDaForm importFrom) throws Exception
	{
	String Tipo1="ordine.label.documenti";
	String Tipo2="ordine.label.suggerimenti";
	String Tipo3="ordine.label.offerte";
	String Tipo4="ordine.label.gare";
	List lista = new ArrayList();
	StrutturaCombo ord = new StrutturaCombo(Tipo1, "documenti");
	lista.add(ord);
	ord = new StrutturaCombo(Tipo2, "suggerimenti");
	lista.add(ord);
	ord = new StrutturaCombo(Tipo3, "offerte");
	lista.add(ord);
	ord = new StrutturaCombo(Tipo4, "gare");
	lista.add(ord);
	importFrom.setImportaDa(lista);
	//importFrom.setSelectedImportaDa("suggerimenti");
	}


}
