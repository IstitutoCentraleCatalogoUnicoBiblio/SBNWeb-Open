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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		Alessandro Segnalini - Inizio Codifica Marzo 2008

package it.iccu.sbn.web.actions.documentofisico.possessori;


import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoreListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriSinteticaForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PossessoriSinteticaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker  {

	private static Log log = LogFactory.getLog(PossessoriSinteticaAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	// almaviva2 Aprile 2015: tasti supplememtari per gestire la fusione fra Possessori in uscita da una richiesta di
	// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)

	@Override
	protected Map<String,String> getKeyMethodMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("sintetica.button.selAllPossessori", "selezionaTutti");
		map.put("sintetica.button.deSelAllPossessori", "deSelezionaTutti");
		// analitica da link PID
		map.put("sintetica.button.analitica", "analiticaPossessori");
		// analitica da pulsante
		map.put("sintetica.button.analiticaPossessori", "analiticaPossessori");
		map.put("button.blocco", "blocco");
		map.put("analitica.button.inventari", "inventariPossessori");
		map.put("sintetica.button.legameInvPoss", "attivaLegame");
		map.put("button.creaPoss", "creaPoss");
		map.put("documentofisico.bottone.indietro", "indietro");
		// almaviva2 Aprile 2015: tasti supplememtari per gestire la fusione fra Possessori in uscita da una richiesta di
		// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)
		map.put("possessori.tornaAldettaglio", "indietro");
		map.put("possessori.fondiPossessori", "fondiPossessori");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(Navigation.getInstance(request).isFromBar() ) {
					return mapping.getInputForward();
		}
		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm)form;
		if (request.getAttribute("listaPossessoriSimile") != null){
			//si esegue la nuova ricerca del possessore indicato in gestione per ottenere la nuova sintetica dei simili
			myForm.setListaPossessori((List)request.getAttribute("listaPossessoriSimile"));
		}
		if (myForm.getListaPossessori().size()==1)
			myForm.setSelezRadio(((PossessoreListeVO)(myForm.getListaPossessori().get(0))).getPid());
		HttpSession httpSession = request.getSession();
		if (request.getAttribute("bottoneLega")!= null && ((String)request.getAttribute("bottoneLega")).trim().equals("ok"))
			myForm.setProv("attivaLegame");

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}
	public ActionForward analiticaPossessori(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm)form;
		String pidSel = myForm.getSelezRadio();
/*
 almaviva7 02/07/2008 11.37
		if (myForm.getProv()!= null && !myForm.getProv().trim().equals("")){
			request.setAttribute("presenzaTastoVaiA","NO");
		}
*/

		if (myForm.getSelezCheck() != null && myForm.getSelezCheck().length > 0) {
			String[] listaVidSelez = myForm.getSelezCheck();
			if (listaVidSelez[0] != null) {
				for (int i = 0; i < listaVidSelez.length; i++) {
					request.setAttribute("bid", listaVidSelez[0]);
					request.setAttribute("livRicerca", "P");
					request.setAttribute("tipoAuthority", "PP");
					//request.setAttribute("areaDatiPassPerInterrogazione", sinteticaAutoriForm.getDatiInterrAutore());
					request.setAttribute("listaBidSelez", listaVidSelez);
				}
				resetToken(request);
				return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
			}
		}

		if (myForm.getSelezRadio() != null) {
			String pidNome = myForm.getSelezRadio();
			request.setAttribute("pidBySint", pidNome.substring(0, 10));
			request.setAttribute("bid", pidNome.substring(0, 10));
			request.setAttribute("listaPossessoriBySint", myForm.getListaPossessori());
			request.setAttribute("tipoAuthority", "PP");
			request.setAttribute("livRicerca", "P");
			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.selObblOggSint"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}
	public ActionForward selezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{

		PossessoriSinteticaForm sinteticaPossessoriForm = (PossessoriSinteticaForm) form;
		int numElem = sinteticaPossessoriForm.getListaPossessori().size();
		String[] listaPidSelez = new String[numElem];
		for (int i = 0; i < numElem; i++) {

			listaPidSelez[i] = ""+((PossessoreListeVO)(sinteticaPossessoriForm.getListaPossessori().get(i))).getPid()	;
		}
		sinteticaPossessoriForm.setSelezCheck(listaPidSelez);
		return mapping.getInputForward();
	}

	public ActionForward deSelezionaTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		PossessoriSinteticaForm sinteticaPossessoriForm = (PossessoriSinteticaForm) form;
		sinteticaPossessoriForm.setSelezCheck(null);
		return mapping.getInputForward();
	}
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (myForm.getTotRighe() == 0 || myForm.getBloccoSelezionato() > myForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}


		int numBlocco = myForm.getBloccoSelezionato();
		String idLista = myForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				if (myForm.getListaPossessori() != null )
					myForm.getListaPossessori().addAll(bloccoVO.getLista());

			}

		}
		return mapping.getInputForward();
	}

	public ActionForward inventariPossessori(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm)form;
		String pidSel = myForm.getSelezRadio();

		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		PossessoreListeVO rec ;
		PossessoreListeVO recApp=null ;
		if (myForm.getSelezRadio() != null) {

			for (int index = 0; index < myForm.getListaPossessori().size(); index++) {
				rec = (PossessoreListeVO)myForm.getListaPossessori().get(index);
				if (rec.getPid().trim().equals(pidSel)){
					recApp=(PossessoreListeVO)myForm.getListaPossessori().get(index);
					break;
				}
			}
			String pidNome = myForm.getSelezRadio();
			request.setAttribute("prov", "listaSuppInvPoss");
			request.setAttribute("codPolo", Navigation.getInstance(request).getUtente().getCodPolo());
			request.setAttribute("descr", recApp.getNome());
			request.setAttribute("pid", pidSel);
			request.setAttribute("codBib", Navigation.getInstance(request).getUtente().getCodBib());

			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("inventariPossessori"));
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.selObblOggSint"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}

	public ActionForward attivaLegame(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm)form;
		List listaPossInve = myForm.getListaPossessori();
		PossessoreListeVO rec = null;
		PossessoreListeVO recApp = null;
		String pidSel = myForm.getSelezRadio();
		if (myForm.getSelezRadio() != null) {
			String pidNome = myForm.getSelezRadio();
			for (int index = 0; index < listaPossInve.size(); index++) {
				rec = (PossessoreListeVO)listaPossInve.get(index);
				if (rec.getPid().trim().equals(pidSel)){
					recApp=(PossessoreListeVO)listaPossInve.get(index);
					break;
				}
			}
			request.setAttribute("prov", "listaSuppInvPoss");
			request.setAttribute("codPolo", Navigation.getInstance(request).getUtente().getCodPolo());
			if (recApp.getForma() != null && recApp.getForma().equals("R")){
				request.setAttribute("pidFA", recApp.getNomeFormaAccettata().substring(0, 10));
				request.setAttribute("descrFA", recApp.getNomeFormaAccettata().substring(13));
			}
			request.setAttribute("desc", recApp.getNome());
			request.setAttribute("bid", pidSel);
			request.setAttribute("codBib", Navigation.getInstance(request).getUtente().getCodBib());


			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("attivaPossessorePerInventario"));
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.selObblOggSint"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}
	public ActionForward creaPoss(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm)form;
		PossessoriDettaglioVO possDett = new PossessoriDettaglioVO();
		possDett.setNome(myForm.getNomeRicerca());
		request.setAttribute("dettaglioPoss", possDett);
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		return mapping.findForward("creaPossessoredaSintetica");
    }
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm) form;
			try{
				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				if (idCheck.equalsIgnoreCase("inventari") ){
					UserVO utente = Navigation.getInstance(request).getUtente();
					try {
						utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_DATI_DI_INVENTARIO, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	// almaviva2 Aprile 2015: tasti supplememtari per gestire la fusione fra Possessori in uscita da una richiesta di
	// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)
	public ActionForward fondiPossessori(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm)form;
		PossessoriDettaglioVO possDett = new PossessoriDettaglioVO();

		String codPolo = Navigation.getInstance(request).getUtente().getCodPolo();
		String uteFirma = Navigation.getInstance(request).getUtente().getFirmaUtente();
		String ticket = Navigation.getInstance(request).getUserTicket();
		String ret = null;

		possDett.setPid(myForm.getIdOggColl());
		possDett.setPidArrivoDiFusione(myForm.getSelezRadio());

		GeneraChiave key = new GeneraChiave();
		//key.estraiChiavi(possDett.getPossDettVO().getTipoTitolo(),possDett.getPossDettVO().getNome().trim());

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().fondiPossessori(possDett, codPolo, uteFirma, ticket, key);

		if (ret.startsWith("0000")) {
			request.setAttribute("pidBySint", myForm.getSelezRadio());
			request.setAttribute("bid", myForm.getSelezRadio());
			request.setAttribute("listaPossessoriBySint", myForm.getListaPossessori());
			request.setAttribute("tipoAuthority", "PP");
			request.setAttribute("livRicerca", "P");
			request.setAttribute("messaggio", "operOk");
			request.setAttribute("testoMessaggio", ret.substring(4));
			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
		} else if (ret.startsWith("9999")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operKoConParametro", ret.substring(4)));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

}
