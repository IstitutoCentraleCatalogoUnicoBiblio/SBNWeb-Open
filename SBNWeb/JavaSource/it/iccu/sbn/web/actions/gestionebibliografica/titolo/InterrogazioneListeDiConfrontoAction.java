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
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheSistemaMetropolitanoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioElenchiListeConfrontoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.InterrogazioneListeDiConfrontoForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

public class InterrogazioneListeDiConfrontoAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(InterrogazioneListeDiConfrontoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("ricerca.button.cerca", "cercaTit");
		map.put("button.annulla", "annullaOperazione");
		map.put("button.caricaTabelleDB", "caricaTabelleDB");
		map.put("button.cercabiblioteche", "listaSupportoBib");
		map.put("button.cancellaTabelleDB", "cancellaTabelleDB");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneListeDiConfrontoForm confrontoForm = (InterrogazioneListeDiConfrontoForm) form;

		if (Navigation.getInstance(request).isFirst()) {
			confrontoForm.setPresenzaLoadFile("SI");
		}

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		confrontoForm.setProvenienza("");

		// Impostazione di inizializzazione jsp
		caricaComboGeneriche(request, confrontoForm);


//		 BIBLIOTECHE
//		List<BibliotecaVO> sifBiblio = (List<BibliotecaVO>) request.getAttribute(BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO);
//		if (ValidazioneDati.isFilled(sifBiblio) ) {
//			confrontoForm.setProvenienza("SERVIZI");
//			BibliotecaVO bib;
//			String listCodBiblioSel = "";
//			for (int i = 0; i < sifBiblio.size(); i++) {
//				bib = sifBiblio.get(i);
//				confrontoForm.getFiltroLocBib().add(bib);
//				if (i == 0)
//					listCodBiblioSel += bib.getCod_bib();
//				else
//					listCodBiblioSel += ", " + bib.getCod_bib();
//			}
//			if (listCodBiblioSel.length() > 0) {
//				confrontoForm.setElencoBiblioMetropolitane(listCodBiblioSel);
//			}
//		}




		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}



	public ActionForward cercaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneListeDiConfrontoForm confrontoForm = (InterrogazioneListeDiConfrontoForm) form;

		// Inizio gestione ciclica per caricamento da file dei bid per la catalogazione in Indice
		if (ValidazioneDati.isFilled(confrontoForm.getListaBidDaFile()) ) {
			request.setAttribute("bid", confrontoForm.getListaBidDaFile().get(0));
			request.setAttribute("livRicerca", "P");

			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassTitoloVO = new AreaDatiPassaggioInterrogazioneTitoloVO();
			areaDatiPassTitoloVO.setRicercaPolo(true);
			areaDatiPassTitoloVO.setRicercaIndice(false);

			request.setAttribute("listaBidDaFile", confrontoForm.getListaBidDaFile());
			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
		}
		// Fine gestione ciclica per caricamento da file dei bid per la catalogazione in Indice

		if (confrontoForm.getDataListaSelez().equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noCanPrim"));

			resetToken(request);
			return mapping.getInputForward();
		}


		AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPassCiclicaVO = new AreaDatiPassaggioSchedaDocCiclicaVO();

		// Richieste Scognamiglio/Contardi Novembre 2011: varie ed eventuali
		if (!confrontoForm.getDataListaSelez().equals("")) {
			areaDatiPassCiclicaVO.setIdListaSelez(Integer.parseInt(confrontoForm.getDataListaSelez()));
		}
//		areaDatiPassCiclicaVO.setDataListaSelez(confrontoForm.getDataListaSelez());


		areaDatiPassCiclicaVO.setStatoLavorRecordSelez(confrontoForm.getStatoLavorRecordSelez());

		request.setAttribute("areaDatiPassCiclicaVO", areaDatiPassCiclicaVO);
		return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaSchedeTitoli"));

	}

	public ActionForward annullaOperazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("InterrogazioneTitoloAction::annulla");

		return	Navigation.getInstance(request).goBack();
	}


	public ActionForward caricaTabelleDB(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneListeDiConfrontoForm confrontoForm = (InterrogazioneListeDiConfrontoForm) form;
		AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass = new AreaDatiPassaggioSchedaDocCiclicaVO();

		byte[] buf;
		try {
			buf = confrontoForm.getUploadImmagine().getFileData();

			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
			List<String> listaBidDaFile = new ArrayList<String>();

			String line = null;

			Set<String> ids = new HashSet<String>(8192);

			while ( (line = reader.readLine() ) != null ) {
				if (!ValidazioneDati.isFilled(line))
					continue;

				//check duplicati
				if (ids.contains(line))
					continue;
				ids.add(line);

				if (line.substring(1, 2).equals("|")) {
					listaBidDaFile.add(line);
				} else {
					String newLine = "N|" + line;
					listaBidDaFile.add(newLine);
				}
				// Bug mantis collaudo 5040 - Maggio 2013 - Non deve essere possibile caricare una lista di VID
				// mettendo il check su: 'Valida automaticamente le coppie di uguali, impostando il valore IN TRATTAMENTO'
				// e si deve emettare apposito msg. (Vedi Nel manuale sull'Import a p. 28: "N.B.: A differenza dei titoli,
				// la gestione del file di autori non potrà avvalersi della validazione automatica delle coppie di uguali
				// (Figura 12), in quanto l';omonimia non è sufficiente a stabilire l'identità".

				// Inizio Intervento Interno su richiesta : almaviva2 Giugno 2014: al controllo sulla V in quarta posizione
				// si deve aggiungere quello sulla selezione attuale del check in oggetto (Valida automaticamente le coppie ...)
				// inoltre il controllo va fatto una sola volta: la prima
				if (listaBidDaFile.size() == 1) {
					if (listaBidDaFile.get(0).substring(5,6).equals("V") && confrontoForm.isCaricaConFusioneAutomatica()) {

						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.listeDiConfrontoAutori"));

						return mapping.getInputForward();
					}
				}
				// Fine Intervento Interno su richiesta : almaviva2 Giugno 2014:

			}

			areaDatiPass.setListaCoppieEsitoConfronto(listaBidDaFile);
			String appoNomeFile = confrontoForm.getUploadImmagine().getFileName();
			String appoNomeFileNew = "";
			if (appoNomeFile.contains(".")) {
				for (int h = 0; h < appoNomeFile.length(); h++) {
					if (appoNomeFile.charAt(h) != '.') {
						appoNomeFileNew = appoNomeFileNew + appoNomeFile.charAt(h);
					} else {
						appoNomeFile = appoNomeFileNew;
						break;
					}
				}
			}
			areaDatiPass.setNomeListaSelez(appoNomeFile);
			areaDatiPass.setCodErr("");
			areaDatiPass.setCaricaConFusioneAutomatica(confrontoForm.isCaricaConFusioneAutomatica());

			// Chiamata al server per il caricamento
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			try {
				factory.getGestioneBibliografica().insertTbReportIndice(
						areaDatiPass, Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {

				LinkableTagUtils.addError(request, new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",
						"ERROR >>" + e.getMessage() + e.toString()));

				return mapping.getInputForward();
			}

			if (!areaDatiPass.getCodErr().equals("")) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
						"ERROR >>" + areaDatiPass.getTestoProtocollo()));

				return mapping.getInputForward();
			}

		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.marImgNotValid"));

			return mapping.getInputForward();
		}

		// Si ricaricano le liste presenti e caricate sul DB e si invia messaggio di conferma.
		caricaComboGeneriche(request, confrontoForm);


		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.operOkConParametro", " Lista creata: "
							+ areaDatiPass.getNomeListaSelez()));

		return mapping.getInputForward();
	}


	public ActionForward cancellaTabelleDB(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneListeDiConfrontoForm confrontoForm = (InterrogazioneListeDiConfrontoForm) form;
		if (confrontoForm.getDataListaSelezPerCanc().equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noCanPrim"));

			resetToken(request);
			return mapping.getInputForward();
		}

		AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPassCiclicaVO = new AreaDatiPassaggioSchedaDocCiclicaVO();
		areaDatiPassCiclicaVO.setIdListaSelez(Integer.parseInt(confrontoForm.getDataListaSelezPerCanc()));
		areaDatiPassCiclicaVO.setCodErr("");


		// Chiamata al server per il caricamento
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try {
			factory.getGestioneBibliografica().cancellaTabelleTbReportIndice(
					areaDatiPassCiclicaVO, Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {

			LinkableTagUtils.addError(request, new ActionMessage(	"errors.gestioneBibliografica.testoProtocollo",
					"ERROR >>" + e.getMessage() + e.toString()));

			return mapping.getInputForward();
		}

		if (!areaDatiPassCiclicaVO.getCodErr().equals("")) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
					"ERROR >>" + areaDatiPassCiclicaVO.getTestoProtocollo()));

			return mapping.getInputForward();
		}


		// Si ricaricano le liste presenti e caricate sul DB e si invia messaggio di conferma.
		caricaComboGeneriche(request, confrontoForm);


		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.operOkConParametro", " Lista cancellata: "
							+ confrontoForm.getDataListaSelezPerCanc()));

		return mapping.getInputForward();
	}






	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			UserVO utente = Navigation.getInstance(request).getUtente();

			SIFListaBibliotecheSistemaMetropolitanoVO richiesta =
				new SIFListaBibliotecheSistemaMetropolitanoVO(
								utente.getCodPolo(),
								utente.getCodBib(),
								"Biblioteche sistema Metropolitano",
								true,
								10,
								BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO);

			return biblio.getSIFListaBibliotecheSistemaMetropolitano(richiesta);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.marImgNotValid"));

			return mapping.getInputForward();
		}

	}

	public void caricaComboGeneriche(HttpServletRequest request, InterrogazioneListeDiConfrontoForm confrontoForm) throws Exception {

		AreaDatiPassaggioElenchiListeConfrontoVO elenchiListeConfrontoVO = new AreaDatiPassaggioElenchiListeConfrontoVO();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		AreaDatiPassaggioElenchiListeConfrontoVO elenchiListeConfrontoReturnVO = factory.getGestioneBibliografica().getElenchiListeConfronto(elenchiListeConfrontoVO, Navigation.getInstance(request).getUserTicket());


		// Impostazione di inizializzazione jsp area generalizzata


//		generico = new ComboCodDescVO();
//		generico.setCodice("");
//		generico.setDescrizione("");
//		confrontoForm.addListaNomeLista(generico);
//		for (int i = 0; i < elenchiListeConfrontoReturnVO.getListaNomeLista().size(); i++) {
//			generico = new ComboCodDescVO();
//			generico.setCodice(String.valueOf(elenchiListeConfrontoReturnVO.getListaIdLista().get(i)));
//			generico.setDescrizione((String) elenchiListeConfrontoReturnVO.getListaNomeLista().get(i));
//			confrontoForm.addListaNomeLista(generico);
//		}

		// almaviva2 22.05.2012 Inserito metodo per ripulire la combo delli liste attive altrimenti per ogni chiamata
		// le liste si ripetevano
		confrontoForm.svuotaListaDataLista();

		ComboCodDescVO generico;
		generico = new ComboCodDescVO();
		generico.setCodice("");
		generico.setDescrizione("");
		confrontoForm.addListaDataLista(generico);

		for (int i = 0; i < elenchiListeConfrontoReturnVO.getListaDataLista().size(); i++) {
			generico = new ComboCodDescVO();
			generico.setCodice(String.valueOf(elenchiListeConfrontoReturnVO.getListaIdLista().get(i)));
			generico.setDescrizione(String.valueOf(elenchiListeConfrontoReturnVO.getListaIdLista().get(i))
					+ " - "
					+ (String) elenchiListeConfrontoReturnVO.getListaDataLista().get(i));
			confrontoForm.addListaDataLista(generico);
		}

		confrontoForm.setListaStatoLavorRecord(this.loadStatoLavorRecordLista());

	}


	public List loadStatoLavorRecordLista() {
		List lista = new ArrayList();
		ComboCodDescVO generico = new ComboCodDescVO();
		generico.setCodice("");
		generico.setDescrizione("");
		lista.add(generico);
		generico = new ComboCodDescVO();
		generico.setCodice("1");
		generico.setDescrizione("DA TRATTARE");
		lista.add(generico);
		generico = new ComboCodDescVO();
		generico.setCodice("2");
		generico.setDescrizione("IN TRATTAMENTO");
		lista.add(generico);
		// questo valore vale solo per le interrogazioni e non per gli aggiornamenti
		generico = new ComboCodDescVO();
		generico.setCodice("5");
		generico.setDescrizione("DA TRATTARE o IN TRATTAMENTO");
		lista.add(generico);
		// Intervento del 23.11.2011 almaviva2 - esito incontro Contardi-almaviva1 del 22.11.2011

		generico = new ComboCodDescVO();
		generico.setCodice("4");
		generico.setDescrizione("TRATTATO");
		lista.add(generico);
		generico = new ComboCodDescVO();
		generico.setCodice("3");
		generico.setDescrizione("ESCLUSO");
		lista.add(generico);

		return lista;
	}


}
