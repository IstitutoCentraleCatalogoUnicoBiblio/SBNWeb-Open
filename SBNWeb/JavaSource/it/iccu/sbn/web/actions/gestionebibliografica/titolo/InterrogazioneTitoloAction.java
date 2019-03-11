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

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheSistemaMetropolitanoVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloAudiovisivoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloCartografiaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloElettronicoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGraficaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloMusicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.InterrogazioneTitoloForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;

public class InterrogazioneTitoloAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(InterrogazioneTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("ricerca.button.campiSpecifici", "aggTipoRecord");
		map.put("ricerca.button.cerca", "cercaTit");
		map.put("button.creaTit", "creaTit");
		map.put("button.creaTitLoc", "creaTitLocale");
		map.put("button.annulla", "annullaOperazione");
		map.put("button.caricaFileIdCatalLocale", "caricaFileIdCatalLocale");
		map.put("button.cercabiblioteche", "listaSupportoBib");

		map.put("button.cercaPerServiziILL", "cercaTitoliPerILL");
		return map;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equals("POST"))
			((InterrogazioneTitoloForm)form).getInterrGener().save();
		return super.execute(mapping, form, request, response);
	}


	private void loadDefault(HttpServletRequest request, InterrogazioneTitoloForm ricTit) throws InfrastructureException, NumberFormatException, RemoteException
	{
		if (ricTit.isInitialized())
			return;

		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			InterrogazioneTitoloGeneraleVO interrGener = ricTit.getInterrGener();
			interrGener.setElemXBlocchi(Integer.parseInt((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_ELEMENTI_BLOCCHI)));
			interrGener.setFormatoListaSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_FORMATO_LISTA));
			interrGener.setTipoOrdinamSelez((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_ORDINAMENTO));
			interrGener.setTitoloPunt(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_PUNTUALE)));
			interrGener.setRicLocale(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_LIVELLO_POLO)));
			interrGener.setRicIndice(Boolean.parseBoolean((String)utenteEjb.getDefault(ConstantDefault.RIC_TIT_LIVELLO_INDICE)));
			ricTit.setInitialized(true);
		} catch (DefaultNotFoundException e) {}

	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloForm ricTit = (InterrogazioneTitoloForm) form;

		// Modifica del 06.10.2009 per presentazione del load File solo se si chiama la mappa dal Menù
		// BUG mantis 3205
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFirst()) {
			ricTit.setPresenzaLoadFile("SI");
		}

		if (navi.isFromBar() ) {
			ricTit.getInterrGener().restore();
			return mapping.getInputForward();
		}

		// INIZIO VERIFICA ABILITAZIONE ALLA CREAZIONE
		// le verifiche possono essere effettuate solo dopo in quanto si potrebbe impostare la creazione di un Tit Uniforme o di
		// un documento nel passo successivo
		ricTit.setCreaDoc("SI");
		ricTit.setCreaDocLoc("SI");
		// FINE VERIFICA ABILITAZIONE ALLA CREAZIONE */


		//Setting Default
		ricTit.getInterrGener().setBibliotecaSelez("");
		ricTit.getInterrGener().setTipoMaterialeImpronta("improntaAntico");
		this.loadDefault(request, ricTit);

		ricTit.setProvenienza("");

		// Intervento interno 26.11.2009 - almaviva2 - per gestire il filtre sulle Biblioteche
		// Per area servizi si filtrano le biblio di area metropolitana
		// per tutte le altre aree si può scegliere solo di filtrare per la biblio di navigazione
		if (request.getParameter("SERVIZI") != null) {
			ricTit.setProvenienza("SERVIZI");
			// almaviva2 Vedi Mantis BUG 3224
			ricTit.getInterrGener().setRicIndice(false);
			ricTit.getInterrGener().setRicLocale(true);
			//almaviva5_20151125 sif da dati richiesta ILL
			DocumentoNonSbnVO doc = (DocumentoNonSbnVO) request.getAttribute(NavigazioneServizi.DETTAGLIO_DOCUMENTO);
			if (doc != null)
				valorizzaRicercaPerDocumentoNonSBN(ricTit.getInterrGener(), doc);
		}

//		 BIBLIOTECHE
		List<BibliotecaVO> sifBiblio = (List<BibliotecaVO>) request.getAttribute(BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO);
		if (ValidazioneDati.isFilled(sifBiblio) ) {
			ricTit.setProvenienza("SERVIZI");
			ricTit.getInterrGener().setRicIndice(false);
			ricTit.getInterrGener().setRicLocale(true);
			String listCodBiblioSel = "";
			ricTit.getFiltroLocBib().clear();
			for (int i = 0; i < sifBiblio.size(); i++) {
				BibliotecaVO bib = sifBiblio.get(i);
				ricTit.getFiltroLocBib().add(bib);
				if (i == 0)
					listCodBiblioSel += bib.getCod_bib();
				else
					listCodBiblioSel += ", " + bib.getCod_bib();
			}
			if (listCodBiblioSel.length() > 0) {
				ricTit.setElencoBiblioMetropolitane(listCodBiblioSel);
			}
		}

		if (request.getParameter("MOVIMENTI_UTENTE") != null) {
			ricTit.setProvenienza("MOVIMENTI_UTENTE");
			// almaviva2 Vedi Mantis BUG 3562
			ricTit.getInterrGener().setRicIndice(false);
			ricTit.getInterrGener().setRicLocale(true);

		}

		if (request.getParameter("ACQUISIZIONI") != null) {
			ricTit.setProvenienza("ACQUISIZIONI");
			ricTit.setRitornoDaEsterna(request.getParameter("ACQUISIZIONI"));
			if (request.getAttribute("bidFromRicOrd") != null) {
				if (!request.getAttribute("bidFromRicOrd").equals("")) {
					ricTit.getInterrGener().setBid((String) request.getAttribute("bidFromRicOrd"));
				}
			}
			ricTit.getInterrGener().setRicIndice(false);
			ricTit.getInterrGener().setRicLocale(true);
		}


		if (request.getParameter("NEWLEGAME") != null) {
			ricTit.setProvenienza("NEWLEGAME");
			ricTit.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO)request.getAttribute("AreaDatiLegameTitoloVO"));
			if (ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {
				ricTit.setPresenzaRicLocale("NO");
				ricTit.getInterrGener().setRicLocale(false);
				// Intervento interno richiesto da  almaviva Aprile 2015
				// nel caso in cui si effettui la ricerca in Indice di un elemento per fusione per una natura W
				// si deve utilizzare il titolo della M superiore altrimenti la ricerca non andra' a buon fine;
				if (ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("W")) {
					ricTit.getInterrGener().setTitolo(ricTit.getAreaDatiLegameTitoloVO().getDescPartenzaMsuperiore());
				} else  {
					ricTit.getInterrGener().setTitolo(ricTit.getAreaDatiLegameTitoloVO().getDescPartenza());
				}
				// ricTit.getInterrGener().setTitolo(ricTit.getAreaDatiLegameTitoloVO().getDescPartenza());
				ricTit.getInterrGener().setNaturaSelez1(ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza());
				ricTit.getInterrGener().setTipoDataSelez(ricTit.getAreaDatiLegameTitoloVO().getTipoDataPubblPartenza());
				ricTit.getInterrGener().setData1Da(ricTit.getAreaDatiLegameTitoloVO().getData1DaPubblPartenza());
			}
		}

		if (request.getAttribute(TitoliCollegatiInvoke.xidDiRicerca) != null) {
			ricTit.setProvenienza("INTERFILTRATA");

			// Inizio Modifica almaviva2 25.01.2010 BUG MANTIS 3512 - eliminare i cre in caso di utilizzo per esamina con filtro
			ricTit.setCreaDoc("NO");
			ricTit.setCreaDocLoc("NO");
			// Fine Modifica almaviva2 25.01.2010 BUG MANTIS 351

			ricTit.setXidDiRicerca((String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicerca));
			ricTit.setXidDiRicercaDesc((String) request.getAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc));
			ricTit.setTipoMatDiRic((String) request.getAttribute(TitoliCollegatiInvoke.tipMatDiRicerca));
			ricTit.setNaturaDiRic((String) request.getAttribute(TitoliCollegatiInvoke.naturaDiRicerca));
			ricTit.setOggettoDiRicerca((Integer) request.getAttribute(TitoliCollegatiInvoke.oggDiRicerca));
			if (request.getAttribute(TitoliCollegatiInvoke.livDiRicerca) != null) {
				if ((Integer) request.getAttribute(TitoliCollegatiInvoke.livDiRicerca) == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
						|| (Integer) request.getAttribute(TitoliCollegatiInvoke.livDiRicerca) == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
					ricTit.setLivRicerca("P");
					ricTit.getInterrGener().setRicLocale(true);
					ricTit.getInterrGener().setBibliotecaSelez("");
					ricTit.getInterrGener().setRicIndice(false);
				} else if ((Integer) request.getAttribute(TitoliCollegatiInvoke.livDiRicerca) == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
					ricTit.setLivRicerca("I");
					ricTit.getInterrGener().setRicLocale(false);
					ricTit.getInterrGener().setBibliotecaSelez("");
					ricTit.getInterrGener().setRicIndice(true);
				}
			}
		}


		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Impostazione di inizializzazione jsp
		caricaComboGenerTitolo(ricTit.getInterrGener(), ricTit.getPresenzaLoadFile());

		if (ricTit.getInterrGener() == null) {
			ricTit.getInterrGener().setTitolo("");
		}

		// INIZIO Evolutiva Novembre 2015 - almaviva2 gestione interrogazione da SERVIZI ILL
		// se si viene da servizi i campi NumStandard e Impronta vanno mantenuti
		if (request.getParameter("SERVIZI") == null) {
			ricTit.getInterrGener().setNumStandardSelez("");
			ricTit.getInterrGener().setNumStandard1("");
			ricTit.getInterrGener().setNumStandard2("");
			ricTit.getInterrGener().setImpronta1("");
			ricTit.getInterrGener().setImpronta2("");
			ricTit.getInterrGener().setImpronta3("");
		}


		if (request.getParameter("NEWLEGAME") != null) {
			if (ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Fondi")) {

				// Modifica BUG 3220 si taglia l'impronta solo se presente e valorizzata 7.10.2009 almaviva2
				if (ricTit.getAreaDatiLegameTitoloVO().getImprontaPartenza() != null
						&& ricTit.getAreaDatiLegameTitoloVO().getImprontaPartenza().length() > 31) {
					ricTit.getInterrGener().setImpronta1(ricTit.getAreaDatiLegameTitoloVO().getImprontaPartenza().substring(0, 10));
					ricTit.getInterrGener().setImpronta2(ricTit.getAreaDatiLegameTitoloVO().getImprontaPartenza().substring(10, 24));
					ricTit.getInterrGener().setImpronta3(ricTit.getAreaDatiLegameTitoloVO().getImprontaPartenza().substring(24, 32));
				}
				ricTit.getInterrGener().setNumStandardSelez(ricTit.getAreaDatiLegameTitoloVO().getTipNumStandardPartenza());
				ricTit.getInterrGener().setNumStandard1(ricTit.getAreaDatiLegameTitoloVO().getValNumStandardPartenza());
			}

			// Inizio intervento almaviva2 - mail Scognamiglio 12.06.2012
			//.Gestione della nuova entrata da Lega Titolo a Raccolta con valorizzqazione della natura di ricerca con
			// il valore R e preimpostazione della ricerca in Polo
			if (ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
					&& ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Crea")
					&& ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo() != null
					&& ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("R")) {
				ricTit.getInterrGener().setRicLocale(true);
				ricTit.getInterrGener().setRicIndice(false);
				ricTit.getInterrGener().setNaturaSelez1("R");
			}
		}

		// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
		// qui va inserita le ricerca titolo come nella normale strada del lega titolo ed asteriscata la chiamata diretta al crea.
		// viene preimpostata la natura N per facilitare l'operato del bibliotecario
		// viene attivato immediatamente il tasto di creazione a prescindere dalla effettuazione della ricerca stessa
		if (ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione() != null
				&& ricTit.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Crea")
				&& ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo() != null
				&& ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N")) {

			ricTit.getInterrGener().setNaturaSelez1("N");

			// Inizio Dicembre 2015 almaviva2 : in fase di creazione di uno spoglio sotto una M si porta tutto il dettaglio
			// del titolo madre così da preimpostare tutti i campi della N che salvo alcune rare eccezioni saranno gli stessi
			if (request.getAttribute("dettaglioTitMadre") != null) {
				ricTit.setDettTitComMadreVO((DettaglioTitoloCompletoVO) request.getAttribute("dettaglioTitMadre"));
			}
			// Fine Dicembre 2015 almaviva2

			if (ricTit.getAreaDatiLegameTitoloVO().isFlagCondivisoArrivo() &&
					ricTit.getAreaDatiLegameTitoloVO().isFlagCondivisoLegame()) {
				ricTit.getInterrGener().setRicLocale(false);
				ricTit.getInterrGener().setRicIndice(true);
				ricTit.setLivRicerca("I");
				ricTit.setCreaDoc("SI");
			} else {
				ricTit.getInterrGener().setRicLocale(true);
				ricTit.getInterrGener().setRicIndice(false);
				ricTit.setLivRicerca("P");
				ricTit.setCreaDocLoc("SI");
			}
		}


		return mapping.getInputForward();
	}


	public ActionForward aggTipoRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloForm ricTit = (InterrogazioneTitoloForm) form;

		String tipoRec = ricTit.getInterrGener().getTipiRecordSelez().toUpperCase();

		if (ricTit.getInterrGener().getSpecificitaSelez() == null) {
			ricTit.getInterrGener().setSpecificitaSelez("000");
		}
		if (ricTit.getInterrGener().getSpecificitaSelez().equals("000")) {
			ricTit.getInterrGener().setTipoMateriale("*");
		} else if (ricTit.getInterrGener().getSpecificitaSelez().equals("002")) {
			// Inizio modifica 16 luglio 2008
			// ricTit.getInterrGener().setTipoMateriale("");
			ricTit.getInterrGener().setTipoMateriale("M");
			// Fine   modifica 16 luglio 2008
		}


		if (ricTit.getInterrGener().getNumStandardSelez().equals("L") || ricTit.getInterrGener().getNumStandardSelez().equals("E")) {
			if (!ricTit.getInterrGener().getNumStandard1().equals("")) {
				if (!ricTit.getInterrGener().getNumStandard2().equals("")) {
					try {
						Integer.parseInt(ricTit.getInterrGener().getNumStandard1().trim());
						Integer.parseInt(ricTit.getInterrGener().getNumStandard2().trim());
					} catch (NumberFormatException nfe) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ric027"));
						this.saveErrors(request, errors);
						caricaComboGenerTitolo(ricTit.getInterrGener(), ricTit.getPresenzaLoadFile());
						return mapping.getInputForward();
					}

					ricTit.getInterrGener().setTipoMateriale("U");
					inizCaricaMusic(ricTit.getInterrMusic());
					ricTit.getInterrGener().setSpecificitaSelez("001");
					return mapping.getInputForward();
				}
			}
		}




//		if ((ricTit.getInterrGener().getNumStandardSelez().equals("L")) &&
//				(!ricTit.getInterrGener().getNumStandard1().equals(""))) {
//			ricTit.getInterrGener().setTipoMateriale("U");
//			inizCaricaMusic(ricTit.getInterrMusic());
//			ricTit.getInterrGener().setSpecificitaSelez("001");
//			return mapping.getInputForward();
//		}

		// Inizio modifiche a seguito della mail del 16/07/2008 di Gabriella Contardi
//		if (ricTit.getInterrGener().isLibretto()) {
//			ricTit.getInterrGener().setTipoMateriale("U");
//			ricTit.getInterrMusic().setTipoTestoLetterarioSelez("b");
//			inizCaricaMusic(ricTit.getInterrMusic());
//			ricTit.getInterrGener().setSpecificitaSelez("001");
//			return mapping.getInputForward();
//		}

		if (ricTit.getInterrGener().isLibretto()) {
			if (ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
				// Specificità presenti
				ricTit.getInterrGener().setTipoMateriale("U");
				ricTit.getInterrMusic().setTipoTestoLetterarioSelez("b");
				inizCaricaMusic(ricTit.getInterrMusic());
			} else if (ricTit.getInterrGener().getSpecificitaSelez().equals("000")
					|| ricTit.getInterrGener().getSpecificitaSelez().equals("002")) {
						// Specificità spazio (indifferenti) o Specificità assenti
				ricTit.getInterrGener().setTipoMateriale("M");
			}
			return mapping.getInputForward();
		}
		//	Fine   modifiche a seguito della mail del 16/07/2008 di Gabriella Contardi


		if (ricTit.getInterrGener().isMatAntico()) {
			ricTit.getInterrGener().setTipoMateriale("E");
			ricTit.getInterrGener().setSpecificitaSelez("001");
			// Intervento per Evolutive BARI 30.10.2012 Viene inserito il filtro di ricerca per genere
			CaricamentoCombo carCombo = new CaricamentoCombo();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ricTit.getInterrGener().setListaGeneri(carCombo.loadComboCodiciDescGenere(factory.getCodici().getCodiceGenerePubblicazione(), "E"));
			return mapping.getInputForward();
		}

		// Intervento per Evolutive BARI 30.10.2012 Viene inserito il filtro di ricerca per genere
		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ricTit.getInterrGener().setListaGeneri(carCombo.loadComboCodiciDescGenere(factory.getCodici().getCodiceGenerePubblicazione(), "Z"));


		if ((tipoRec.equals("C") || tipoRec.equals("D")) && ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("U");
			inizCaricaMusic(ricTit.getInterrMusic());
			return mapping.getInputForward();
		}

		if ((tipoRec.equals("E") || tipoRec.equals("F")) && ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("C");
			inizCaricaCartog(ricTit.getInterrCartog());
			return mapping.getInputForward();
		}

		if (tipoRec.equals("K") && ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("G");
			inizCaricaGrafic(ricTit.getInterrGrafic());
			return mapping.getInputForward();
		}

        // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		if ((tipoRec.equals("G") ||
				tipoRec.equals("J") ||
				tipoRec.equals("I")) && ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("H");
			inizCaricaAudiovisivo(ricTit.getInterrAudiovisivo());
			return mapping.getInputForward();
		}
		// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
		// Gestione nuovi campi specifici per etichetta 135
		if (tipoRec.equals("L") && ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("L");
			inizCaricaElettronico(ricTit.getInterrElettronico());
			return mapping.getInputForward();
		}
		// Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro


		if ((ricTit.getInterrGener().getNaturaSelez1().equals("A")  ||
					ricTit.getInterrGener().getNaturaSelez2().equals("A") ||
					ricTit.getInterrGener().getNaturaSelez3().equals("A") ||
					ricTit.getInterrGener().getNaturaSelez4().equals("A")) &&
							ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("U");
			inizCaricaMusic(ricTit.getInterrMusic());
			ricTit.getInterrGener().setNaturaSelez1("A");
			ricTit.getInterrGener().setNaturaSelez2("");
			ricTit.getInterrGener().setNaturaSelez3("");
			ricTit.getInterrGener().setNaturaSelez4("");
		} else if (ricTit.getInterrGener().getSpecificitaSelez().equals("001")) {
			ricTit.getInterrGener().setTipoMateriale("M");
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward cercaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloForm ricTit = (InterrogazioneTitoloForm) form;

		// Inizio Modifica almaviva2 11.01.2013 BUG MANTIS 5205 (esercizio) - i valori dei campi vanno resettati altrimenti
		// non vengono riaccesi dopo averli spenti x la casistica di errore ricerca
		ricTit.setCreaDoc("SI");
		ricTit.setCreaDocLoc("SI");
		// Fine Modifica almaviva2 25.01.2010 BUG MANTIS 5205


		// Inizio gestione ciclica per caricamento da file dei bid per la catalogazione in Indice
		InterrogazioneTitoloGeneraleVO interrGener = ricTit.getInterrGener();
		if (ValidazioneDati.isFilled(ricTit.getListaBidDaFile()) ) {
			request.setAttribute("bid", ricTit.getListaBidDaFile().get(0));
			request.setAttribute("livRicerca", "P");

			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setRicercaIndice(false);

			areaDatiPass.setInterTitGen(interrGener);
			areaDatiPass.setInterTitCar(ricTit.getInterrCartog());
			areaDatiPass.setInterTitGra(ricTit.getInterrGrafic());
			areaDatiPass.setInterTitMus(ricTit.getInterrMusic());
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			areaDatiPass.setInterTitAud(ricTit.getInterrAudiovisivo());
			areaDatiPass.setInterTitEle(ricTit.getInterrElettronico());

			areaDatiPass.setOggChiamante(99);
			areaDatiPass.setTipoOggetto(99);
			areaDatiPass.setTipoOggettoFiltrato(99);
			areaDatiPass.setOggDiRicerca("");

			request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);
			request.setAttribute("listaBidDaFile", ricTit.getListaBidDaFile());
			request.setAttribute("presenzaTastoCercaInIndice", "NO");
			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
		}
		// Fine gestione ciclica per caricamento da file dei bid per la catalogazione in Indice


		// Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
		// e opportunamente salvato sulla tb_titolo campo ky_editore
		if (ValidazioneDati.isFilled(interrGener.getParoleEditore())) {
			String concatenazioneNature = interrGener.getNaturaSelez1() + interrGener.getNaturaSelez2() +
								interrGener.getNaturaSelez3() + interrGener.getNaturaSelez4();
			if (!concatenazioneNature.equals("C")) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ric029"));
				this.saveErrors(request, errors);
				caricaComboGenerTitolo(interrGener, ricTit.getPresenzaLoadFile());
				return mapping.getInputForward();
			}
		}
		// Fine Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore

		interrGener.setTipoMateriale("");
		if (interrGener.getSpecificitaSelez() == null) {
			interrGener.setSpecificitaSelez("000");
		}

		String tipoRec = interrGener.getTipiRecordSelez().toUpperCase();


		if (!interrGener.getSottoNaturaDSelez().equals("")) {
			interrGener.setNaturaSelez1("D");
			interrGener.setNaturaSelez2("");
			interrGener.setNaturaSelez3("");
			interrGener.setNaturaSelez4("");
		}


		if (interrGener.getNumStandardSelez().equals("L") || interrGener.getNumStandardSelez().equals("E")) {
			if (!interrGener.getNumStandard1().equals("")) {
				if (!interrGener.getNumStandard2().equals("")) {
					try {
						Integer.parseInt(interrGener.getNumStandard1().trim());
						Integer.parseInt(interrGener.getNumStandard2().trim());
					} catch (NumberFormatException nfe) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ric027"));
						this.saveErrors(request, errors);
						caricaComboGenerTitolo(interrGener, ricTit.getPresenzaLoadFile());
						return mapping.getInputForward();
					}
					interrGener.setTipoMateriale("U");
					inizCaricaMusic(ricTit.getInterrMusic());
					interrGener.setSpecificitaSelez("001");
				}
			}
		}

		else if (interrGener.isLibretto() && interrGener.isMatAntico()) {

			// Intervento del 03.03.2015 per gestire libretto anche antico
			if (interrGener.getSpecificitaSelez().equals("001")) {
				// Specificità presenti; si cercano i libretti ma solo con specificità presente QUINDI:
				// TipoMateriale("E") per tiporecord = a/b
				// TipoMateriale("U") per tiporecord = c/d
				if (tipoRec.equals("A") || tipoRec.equals("B")) {
					interrGener.setTipoMateriale("E");
				} else if (tipoRec.equals("C") || tipoRec.equals("D")) {
					interrGener.setTipoMateriale("U");
					inizCaricaMusic(ricTit.getInterrMusic());
				}
				ricTit.getInterrMusic().setTipoTestoLetterarioSelez("b");
				inizCaricaMusic(ricTit.getInterrMusic());
			} else {
				// Specificità spazio (indifferenti) oppure Specificità assenti; si cercano i libretti di tutti i materiali ma data ante 1830
				interrGener.setTipiRecordSelez("");
				interrGener.setTipoMateriale("*");
				interrGener.setData1Da("1000");
				interrGener.setData1A("1830");
			}
		}

		else if (interrGener.isLibretto()) {

		if (interrGener.getSpecificitaSelez().equals("001")) {
				// Specificità presenti
				interrGener.setTipoMateriale("U");
				ricTit.getInterrMusic().setTipoTestoLetterarioSelez("b");
				inizCaricaMusic(ricTit.getInterrMusic());
			} else {
				// inizio almaviva2 febbraio 2015
				// La ricerca per genere è obsoleta nel caso del libretto; nella sbngestione titoli vengono impostati
				// i campi relativi al tipoTestoLetterario 105bis e 140bis
//				if (tipoRec.equals("A")) {
//					interrGener.setGenereSelez1("2");
//				} else if (tipoRec.equals("B")) {
//					interrGener.setGenereSelez1("3");
//				} else {
//					interrGener.setGenereSelez1("2");
//					interrGener.setGenereSelez2("3");
//				}
				// fine almaviva2 febbraio 2015

				interrGener.setTipiRecordSelez("");
				if (interrGener.getSpecificitaSelez().equals("000")) {
					// Specificità spazio (indifferenti)
					interrGener.setTipoMateriale("*");
				} else if (interrGener.getSpecificitaSelez().equals("002")) {
						// Specificità assenti
						interrGener.setTipoMateriale("M");
				}
			}
			//	Fine   modifiche a seguito della mail del 16/07/2008 di Gabriella Contardi

		} else if (interrGener.isMatAntico()) {

			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio TEMPORANEA
			// La selezione del check Antico puo avere doppio significato;
			// se NON state indicate specificità quindi (SpecificitaSelez vuoto "000" o assenti "002") si continua ad impostare
			//    "E" in TipoMateriale
			// se state indicate specificità quindi (SpecificitaSelez Presenti) si imposta il giusto tipo Materiale in base al tipo_record
			//    ma si impostano anche le date pubblicazione da 1 a 1831

			if (interrGener.getSpecificitaSelez().equals("000")) {   		// Specificità spazio (indifferenti)
				interrGener.setTipoMateriale("E");
				interrGener.setSpecificitaSelez("001");
			} else if (interrGener.getSpecificitaSelez().equals("002")) {	// Specificità assenti
				interrGener.setTipoMateriale("E");
				interrGener.setSpecificitaSelez("001");
			} else if (interrGener.getSpecificitaSelez().equals("001")) {	// Specificità Presenti da decodificare per tipoRecord
				interrGener.setData1Da("1000");
				interrGener.setData1A("1830");
				if (tipoRec.equals("A") || tipoRec.equals("B")) {
					interrGener.setTipoMateriale("E");
				} else if (tipoRec.equals("C") || tipoRec.equals("D")) {
					interrGener.setTipoMateriale("U");
					inizCaricaMusic(ricTit.getInterrMusic());
				} else if (tipoRec.equals("E") || tipoRec.equals("F")) {
					interrGener.setTipoMateriale("C");
					inizCaricaCartog(ricTit.getInterrCartog());
				}  else if (tipoRec.equals("K")) {
					interrGener.setTipoMateriale("G");
					inizCaricaGrafic(ricTit.getInterrGrafic());
				} else if (tipoRec.equals("G") || tipoRec.equals("J") || tipoRec.equals("I")) {
					interrGener.setTipoMateriale("H");
					inizCaricaAudiovisivo(ricTit.getInterrAudiovisivo());
				} else if (tipoRec.equals("L")) {
					interrGener.setTipoMateriale("L");
					inizCaricaElettronico(ricTit.getInterrElettronico());
				}
			}


//			interrGener.setTipoMateriale("E");
//			interrGener.setSpecificitaSelez("001");
			// Fine Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio TEMPORANEA

		} else if ((tipoRec.equals("C") || tipoRec.equals("D")) && interrGener.getSpecificitaSelez().equals("001")) {
			interrGener.setTipoMateriale("U");
			inizCaricaMusic(ricTit.getInterrMusic());
		} else if ((tipoRec.equals("E") || tipoRec.equals("F")) && interrGener.getSpecificitaSelez().equals("001")) {
			interrGener.setTipoMateriale("C");
			inizCaricaCartog(ricTit.getInterrCartog());

			// Inizio modifica almaviva2 07.07.2010 BUG MANTIS 3814
			if (ricTit.getInterrCartog().getLongInput1gg() == null) ricTit.getInterrCartog().setLongInput1gg("");
			if (ricTit.getInterrCartog().getLongInput1pp() == null) ricTit.getInterrCartog().setLongInput1pp("");
			if (ricTit.getInterrCartog().getLongInput1ss() == null) ricTit.getInterrCartog().setLongInput1ss("");

			if (ricTit.getInterrCartog().getLongInput2gg() == null) ricTit.getInterrCartog().setLongInput2gg("");
			if (ricTit.getInterrCartog().getLongInput2pp() == null) ricTit.getInterrCartog().setLongInput2pp("");
			if (ricTit.getInterrCartog().getLongInput2ss() == null) ricTit.getInterrCartog().setLongInput2ss("");

			if (ricTit.getInterrCartog().getLatiInput1gg() == null) ricTit.getInterrCartog().setLatiInput1gg("");
			if (ricTit.getInterrCartog().getLatiInput1pp() == null) ricTit.getInterrCartog().setLatiInput1pp("");
			if (ricTit.getInterrCartog().getLatiInput1ss() == null) ricTit.getInterrCartog().setLatiInput1ss("");

			if (ricTit.getInterrCartog().getLatiInput2gg() == null) ricTit.getInterrCartog().setLatiInput2gg("");
			if (ricTit.getInterrCartog().getLatiInput2pp() == null) ricTit.getInterrCartog().setLatiInput2pp("");
			if (ricTit.getInterrCartog().getLatiInput2ss() == null) ricTit.getInterrCartog().setLatiInput2ss("");
			// Fine modifica almaviva2 07.07.2010 BUG MANTIS 3814

			ricTit.getInterrCartog().setLongitudineInput1(
						ricTit.getInterrCartog().getLongInput1gg() +
						ricTit.getInterrCartog().getLongInput1pp() +
						ricTit.getInterrCartog().getLongInput1ss());
			ricTit.getInterrCartog().setLongitudineInput2(
					ricTit.getInterrCartog().getLongInput2gg() +
					ricTit.getInterrCartog().getLongInput2pp() +
					ricTit.getInterrCartog().getLongInput2ss());
			ricTit.getInterrCartog().setLatitudineInput1(
					ricTit.getInterrCartog().getLatiInput1gg() +
					ricTit.getInterrCartog().getLatiInput1pp() +
					ricTit.getInterrCartog().getLatiInput1ss());
			ricTit.getInterrCartog().setLatitudineInput2(
					ricTit.getInterrCartog().getLatiInput2gg() +
					ricTit.getInterrCartog().getLatiInput2pp() +
					ricTit.getInterrCartog().getLatiInput2ss());
		} else if ((tipoRec.equals("G") || tipoRec.equals("J") || tipoRec.equals("I")) && interrGener.getSpecificitaSelez().equals("001")) {
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			interrGener.setTipoMateriale("H");
			inizCaricaAudiovisivo(ricTit.getInterrAudiovisivo());
		} else if ((tipoRec.equals("L")) && interrGener.getSpecificitaSelez().equals("001")) {
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			interrGener.setTipoMateriale("L");
			inizCaricaElettronico(ricTit.getInterrElettronico());

		} else if (tipoRec.equals("K") && interrGener.getSpecificitaSelez().equals("001")) {
			interrGener.setTipoMateriale("G");
			inizCaricaGrafic(ricTit.getInterrGrafic());
		} else if ((tipoRec.equals("A") || tipoRec.equals("B")) && interrGener.getSpecificitaSelez().equals("001")) {
			interrGener.setTipoMateriale("M");
		} else if ((interrGener.getNaturaSelez1().equals("A")  ||
					interrGener.getNaturaSelez2().equals("A") ||
					interrGener.getNaturaSelez3().equals("A") ||
					interrGener.getNaturaSelez4().equals("A")) &&
							interrGener.getSpecificitaSelez().equals("001")) {
			interrGener.setTipoMateriale("U");
			inizCaricaMusic(ricTit.getInterrMusic());
			interrGener.setNaturaSelez1("A");
			interrGener.setNaturaSelez2("");
			interrGener.setNaturaSelez3("");
			interrGener.setNaturaSelez4("");
		} else if (interrGener.getSpecificitaSelez().equals("001")) {
			interrGener.setTipoMateriale("*");
		}


		if (interrGener.getTipoMateriale().equals("*") &&
				interrGener.getTipiRecordSelez().equals("") &&
				interrGener.isLibretto() == false &&
				interrGener.isMatAntico() == false ) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.TipoRecordObblig"));
			this.saveErrors(request, errors);
			caricaComboGenerTitolo(interrGener, ricTit.getPresenzaLoadFile());
			return mapping.getInputForward();
		}



		try {
			interrGener.validaParametriGener();
		} catch (ValidationException e) {
			e.printStackTrace();
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica." + e.getMessage()));
			this.saveErrors(request, errors);
			caricaComboGenerTitolo(interrGener, ricTit.getPresenzaLoadFile());
			return mapping.getInputForward();
		}

		if (!interrGener.getImpronta1().equals("") ||
				!interrGener.getImpronta2().equals("") ||
				!interrGener.getImpronta3().equals("")) {
			if (interrGener.getTipoMaterialeImpronta().equals("improntaAntico")) {
				interrGener.setTipoMateriale("E");
			} else {
				interrGener.setTipoMateriale("U");
				inizCaricaMusic(ricTit.getInterrMusic());
			}
		}

		if (interrGener.getNumStandardSelez().equals("I") &&
				interrGener.getNumStandard1().length() == 13 &&
				(!interrGener.getNumStandard1().substring(0,3).equals("978") &&
						!interrGener.getNumStandard1().substring(0,3).equals("979"))) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.ric026"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (interrGener.getTipoMateriale() == null || interrGener.getTipoMateriale().equals("")) {
			if (interrGener.getSpecificitaSelez().equals("000")) {
				interrGener.setTipoMateriale("*");
			} else if (interrGener.getSpecificitaSelez().equals("002")) {
				// Inizio modifica 16 luglio 2008
				// ricTit.getInterrGener().setTipoMateriale("");
				interrGener.setTipoMateriale("M");
				// Fine   modifica 16 luglio 2008
			}
		}

		AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
		areaDatiPass.setRicercaPolo(false);
		areaDatiPass.setRicercaIndice(false);

		if (interrGener.isRicLocale()) {
			request.setAttribute("livRicerca", "P");
			ricTit.setLivRicerca("P");
			areaDatiPass.setRicercaPolo(true);
			if (interrGener.getBibliotecaSelez().length() > 0) {
				request.setAttribute("livRicerca", "B");
				request.setAttribute("codBib", interrGener
						.getBibliotecaSelez().toString());
			}
		}
		if (interrGener.isRicIndice()) {
			request.setAttribute("livRicerca", "I");
			ricTit.setLivRicerca("I");
			areaDatiPass.setRicercaIndice(true);
		}

//		 Intervento interno 26.11.2009 - almaviva2 - per gestire il filtre sulle Biblioteche
		// Per area servizi si filtrano le biblio di area metropolitana
		// per tutte le altre aree si può scegliere solo di filtrare per la biblio di navigazione
		if  (interrGener.isRicFiltrataPerBib()) {
			BibliotecaVO bib = new BibliotecaVO();
			UserVO utente = Navigation.getInstance(request).getUtente();
			bib.setCod_polo(utente.getCodPolo());
			bib.setCod_bib(utente.getCodBib());
			areaDatiPass.getFiltroLocBib().add(bib);

			// BUG esercione 0006369: almaviva2 06.03.2017 - in Interrogazione titolo, se si è messo il check
			//su "Documenti posseduti" si dovrebbe escludere la ricerca su livellolivello Indice.
			request.setAttribute("livRicerca", "P");
			ricTit.setLivRicerca("P");
			areaDatiPass.setRicercaPolo(true);
			areaDatiPass.setRicercaIndice(false);

		} else if (ricTit.getFiltroLocBib() != null && ricTit.getFiltroLocBib().size() > 0) {
			areaDatiPass.setFiltroLocBib(ricTit.getFiltroLocBib());
		}


		// Inizio modifica almaviva2 BUG MANTIS 3923 2010.10.06
		// dopo la modifica del BUG 3883 il campo potrebbe essere nullo viene quindi controllato e impostato.
		if (interrGener.getLuogoPubbl() == null) {
			interrGener.setLuogoPubbl("");
		}


		areaDatiPass.setInterTitGen(interrGener);
		areaDatiPass.setInterTitCar(ricTit.getInterrCartog());
		areaDatiPass.setInterTitGra(ricTit.getInterrGrafic());
		areaDatiPass.setInterTitMus(ricTit.getInterrMusic());
		// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		areaDatiPass.setInterTitAud(ricTit.getInterrAudiovisivo());
		areaDatiPass.setInterTitEle(ricTit.getInterrElettronico());

		areaDatiPass.setOggChiamante(99);
		areaDatiPass.setTipoOggetto(99);
		areaDatiPass.setTipoOggettoFiltrato(99);
		areaDatiPass.setOggDiRicerca("");

		if (ricTit.getProvenienza().equals("INTERFILTRATA")) {
			areaDatiPass.setOggDiRicerca(ricTit.getXidDiRicerca());
			areaDatiPass.setTipMatTitBase(ricTit.getTipoMatDiRic());
			areaDatiPass.setNaturaTitBase(ricTit.getNaturaDiRic());
			areaDatiPass.setCodiceLegame("");
			areaDatiPass.setCodiceSici(interrGener.getCodiceSici());
			areaDatiPass.setTipoOggettoFiltrato(ricTit.getOggettoDiRicerca());
		}

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		areaDatiPassReturn = factory.getGestioneBibliografica().ricercaTitoli(areaDatiPass, Navigation.getInstance(request).getUserTicket());

		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			// Inizio Modifica almaviva2 11.01.2013 BUG MANTIS 5205 (esercizio) - eliminare i crea in caso di errore
			ricTit.setCreaDoc("NO");
			ricTit.setCreaDocLoc("NO");
			// Fine Modifica almaviva2 25.01.2010 BUG MANTIS 5205
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getNumNotizie() > 0) {
			request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);
			request.setAttribute("areaDatiPassReturnSintetica", areaDatiPassReturn);
			if (ricTit.getProvenienza().equals("NEWLEGAME")) {
				request.setAttribute("AreaDatiLegameTitoloVO", ricTit.getAreaDatiLegameTitoloVO());
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerLegame"));
			} else if (ricTit.getProvenienza().equals("SERVIZI")) {
				// Giuseppe - inizio
				request.setAttribute("elencoBiblSelezionate", ricTit.getElencoBiblioMetropolitane());
				// Giuseppe - fine

				//almaviva5_20101103 #3958
				request.setAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI, ricTit.getFiltroLocBib() );

				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerServizi"));

			} else if (ricTit.getProvenienza().equals("MOVIMENTI_UTENTE")) {
				//almaviva5_20101103 #3958
				request.setAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI, ricTit.getFiltroLocBib() );
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerMovimentiUtente"));
			} else if (ricTit.getProvenienza().equals("ACQUISIZIONI")) {
				request.setAttribute("ritornoDaEsterna", ricTit.getRitornoDaEsterna());
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerAcquisizioni"));
			} else {
				return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoli"));
			}
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);

			resetToken(request);
			return mapping.getInputForward();
		}

	}

	public ActionForward creaTit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloForm ricTit = (InterrogazioneTitoloForm) form;

		if (!ricTit.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(ricTit.getInterrGener().getTitolo());
			if (ricTit.getInterrGener().getTipoMateriale().equals("") ||
					ricTit.getInterrGener().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(ricTit.getInterrGener().getTipoMateriale());
			}

			if (ricTit.getInterrGener().getTipiRecordSelez().equals("")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoRec("a");
				dettTitComVO.getDetTitoloPFissaVO().setTipoRec("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoRec(ricTit.getInterrGener().getTipiRecordSelez());
			}

			if (ricTit.getInterrGener().getNaturaSelez1().equals("")) {
//				dettTitComVO.getDetTitoloPFissaVO().setNatura("M");
				dettTitComVO.getDetTitoloPFissaVO().setNatura("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(ricTit.getInterrGener().getNaturaSelez1());
			}


			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!ricTit.getInterrGener().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(ricTit.getInterrGener().getTipoDataSelez());
			}
			if (!ricTit.getInterrGener().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(ricTit.getInterrGener().getData1Da());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			// Inizio Modifica almaviva2 - BUG MANTIS COLLAUDO 4383 inserimento valorizzazione di lingua e paese da riportare nella
			// mappa di creazione titolo
			if (!ricTit.getInterrGener().getLinguaSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setLingua(ricTit.getInterrGener().getLinguaSelez());
				dettTitComVO.getDetTitoloPFissaVO().setLingua1(ricTit.getInterrGener().getLinguaSelez());
			}
			if (!ricTit.getInterrGener().getPaeseSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setPaese(ricTit.getInterrGener().getPaeseSelez());
			}
			if (!ricTit.getInterrGener().getNumStandardSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().addListaNumStandard(
						new TabellaNumSTDImpronteVO(ricTit.getInterrGener().getNumStandard1(), ricTit.getInterrGener().getNumStandardSelez(), "descCampoDue", ""));
			}
			// Fine Modifica almaviva2 - BUG MANTIS COLLAUDO 4383

			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricTit.getInterrGener().getTitolo());

			resetToken(request);
			return mapping.findForward("creaTitoloPartecipato");

		} else {
			request.setAttribute("tipoProspettazione", "INS");


			// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
			// qui va inserita le creazione del nuovo spoglio
			// Modifica del 25 settembre 2015 - in caso di creazione il campo ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo()
			// puo essere vuoto: si inserisce il controllo sul valore null

//			if (ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M") &&
//					ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N")) {
			if ((ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M") ||
					ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("S")) &&
					(ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo() != null &&
							ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N"))) {

				// Bug Mantis esercizio 6001: almaviva2 ottobre 2015 - quando si fa una ricerca e poi si va in creazione,
				// nel form non viene riportato il titolo usato per la ricerca (il relativo campo è vuoto)
				ricTit.getAreaDatiLegameTitoloVO().setDescArrivo(ricTit.getInterrGener().getTitolo());

				request.setAttribute("AreaDatiLegameTitoloVO", ricTit.getAreaDatiLegameTitoloVO());

				// Inizio Dicembre 2015 almaviva2 : in fase di creazione di uno spoglio sotto una M si porta tutto il dettaglio
				// del titolo madre così da preimpostare tutti i campi della N che salvo alcune rare eccezioni saranno gli stessi
				request.setAttribute("dettaglioTitMadre", ricTit.getDettTitComMadreVO());
				// Fine Dicembre 2015 almaviva2

				return mapping.findForward("creaSpoglioPartecipato51");
			}



			ricTit.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setDescArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricTit.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricTit.getAreaDatiLegameTitoloVO());

			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(ricTit.getInterrGener().getTitolo());
			if (ricTit.getInterrGener().getTipoMateriale().equals("") ||
					ricTit.getInterrGener().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(ricTit.getInterrGener().getTipoMateriale());
			}
			if (!ricTit.getInterrGener().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(ricTit.getInterrGener().getNaturaSelez1());
			}
			if (!ricTit.getInterrGener().getLinguaSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setLingua(ricTit.getInterrGener().getLinguaSelez());
			}
			if (!ricTit.getInterrGener().getPaeseSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setPaese(ricTit.getInterrGener().getPaeseSelez());
			}
			if (!ricTit.getInterrGener().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(ricTit.getInterrGener().getTipoDataSelez());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!ricTit.getInterrGener().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(ricTit.getInterrGener().getData1Da());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricTit.getInterrGener().getTitolo());

			resetToken(request);
			return mapping.findForward("creaTitoloPartecipato");
		}

	}

	public ActionForward creaTitLocale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloForm ricTit = (InterrogazioneTitoloForm) form;

		if (!ricTit.getProvenienza().equals("NEWLEGAME")) {
			request.setAttribute("tipoProspettazione", "INS");
			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(ricTit.getInterrGener().getTitolo());
			if (ricTit.getInterrGener().getTipoMateriale().equals("") ||
					ricTit.getInterrGener().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(ricTit.getInterrGener().getTipoMateriale());
			}

			// Intervento interno almaviva2 Luglio 2015: dalla mappa di Interrogazione Titolo si riporta alla fase di Crea
			// anche il dato relativo al tipoRecord da utilizzare in fase di Creazione locale o condivisa
			if (ricTit.getInterrGener().getTipiRecordSelez().equals("") ||
					ricTit.getInterrGener().getTipiRecordSelez().equals("*")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoRec("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoRec(ricTit.getInterrGener().getTipiRecordSelez());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!ricTit.getInterrGener().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(ricTit.getInterrGener().getTipoDataSelez());
			}
			if (!ricTit.getInterrGener().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(ricTit.getInterrGener().getData1Da());
			}
			// Inserita anche la natura perchè mancante ripetto al crea
			if (!ricTit.getInterrGener().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(ricTit.getInterrGener().getNaturaSelez1());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			// Inizio Modifica almaviva2 - BUG MANTIS COLLAUDO 4383 inserimento valorizzazione di lingua e paese da riportare nella
			// mappa di creazione titolo
			if (!ricTit.getInterrGener().getLinguaSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setLingua(ricTit.getInterrGener().getLinguaSelez());
				dettTitComVO.getDetTitoloPFissaVO().setLingua1(ricTit.getInterrGener().getLinguaSelez());
			}
			if (!ricTit.getInterrGener().getPaeseSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setPaese(ricTit.getInterrGener().getPaeseSelez());
			}
			if (!ricTit.getInterrGener().getNumStandardSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().addListaNumStandard(
						new TabellaNumSTDImpronteVO(ricTit.getInterrGener().getNumStandard1(), ricTit.getInterrGener().getNumStandardSelez(), "descCampoDue", ""));
			}
			// Fine Modifica almaviva2 - BUG MANTIS COLLAUDO 4383


			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricTit.getInterrGener().getTitolo());

			resetToken(request);
			return mapping.findForward("creaTitoloLocale");

		} else {
			request.setAttribute("tipoProspettazione", "INS");

			// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
			// qui va inserita le creazione del nuovo spoglio
			// Bug esercizio 6000: almaviva2 Ottobre 2015  inserito controllo su getNaturaBidPartenza().equals("S") (oltre che "M")
			if ((ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M") ||
					ricTit.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("S")) &&
					(ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo() != null &&
							ricTit.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("N"))) {

				// Bug Mantis esercizio 6001: almaviva2 ottobre 2015 - quando si fa una ricerca e poi si va in creazione,
				// nel form non viene riportato il titolo usato per la ricerca (il relativo campo è vuoto)
				ricTit.getAreaDatiLegameTitoloVO().setDescArrivo(ricTit.getInterrGener().getTitolo());

				request.setAttribute("AreaDatiLegameTitoloVO", ricTit.getAreaDatiLegameTitoloVO());

				// Inizio Dicembre 2015 almaviva2 : in fase di creazione di uno spoglio sotto una M si porta tutto il dettaglio
				// del titolo madre così da preimpostare tutti i campi della N che salvo alcune rare eccezioni saranno gli stessi
				request.setAttribute("dettaglioTitMadre", ricTit.getDettTitComMadreVO());
				// Fine Dicembre 2015 almaviva2

				return mapping.findForward("creaSpoglioLocale51");
			}


			ricTit.getAreaDatiLegameTitoloVO().setIdArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setNaturaBidArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setDescArrivo("");
			ricTit.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			ricTit.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSiciNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			ricTit.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");
			request.setAttribute("AreaDatiLegameTitoloVO", ricTit.getAreaDatiLegameTitoloVO());

			DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
			dettTitComVO.getDetTitoloPFissaVO().setAreaTitTitolo(ricTit.getInterrGener().getTitolo());
			if (ricTit.getInterrGener().getTipoMateriale().equals("") ||
					ricTit.getInterrGener().getTipoMateriale().equals("*")) {
//				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("M");
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat("");
			} else  {
				dettTitComVO.getDetTitoloPFissaVO().setTipoMat(ricTit.getInterrGener().getTipoMateriale());
			}
			if (!ricTit.getInterrGener().getNaturaSelez1().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setNatura(ricTit.getInterrGener().getNaturaSelez1());
			}
			if (!ricTit.getInterrGener().getLinguaSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setLingua(ricTit.getInterrGener().getLinguaSelez());
			}
			if (!ricTit.getInterrGener().getPaeseSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setPaese(ricTit.getInterrGener().getPaeseSelez());
			}
			if (!ricTit.getInterrGener().getTipoDataSelez().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setTipoData(ricTit.getInterrGener().getTipoDataSelez());
			}

			// Inizio modifica almaviva2 27.07.2010 - BUG MANTIS 3691
			if (!ricTit.getInterrGener().getData1Da().equals("")) {
				dettTitComVO.getDetTitoloPFissaVO().setDataPubbl1(ricTit.getInterrGener().getData1Da());
			}
			// Fine modifica almaviva2 27.07.2010 - BUG MANTIS 3691

			request.setAttribute("dettaglioTit", dettTitComVO);
			request.setAttribute("bid", "");
			request.setAttribute("desc", ricTit.getInterrGener().getTitolo());

			resetToken(request);
			return mapping.findForward("creaTitoloLocale");
		}

	}

	public void caricaComboGenerTitolo(InterrogazioneTitoloGeneraleVO titGenVO, String primaCall) throws Exception {
		// Impostazione di inizializzazione jsp area generalizzata per tutti i
		// tipi materiale

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if (titGenVO.getBid() == null) {
			titGenVO.setBid("");
		}
		titGenVO.setListaTipiNumStandard(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TIPO_NUMERO_STANDARD)));
		titGenVO.setListaNature(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_NATURA_BIBLIOGRAFICA)));
		titGenVO.setListaSottonatureD(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_LEGAME_TITOLO_MUSICA)));
		titGenVO.setListaTipoData(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE)));
		titGenVO.setListaLingue(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_LINGUA)));
		titGenVO.setListaPaese(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_PAESE)));
		titGenVO.setListaResponsabilita(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_RESPONSABILITA)));
		titGenVO.setListaRelazioni(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_LEGAME_TITOLO_AUTORE)));
		titGenVO.setListaTipiRecord(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC)));
		// Intervento per Evolutive BARI 30.10.2012 Viene inserito il filtro di ricerca per genere
		titGenVO.setListaGeneri(carCombo.loadComboCodiciDescGenere(CodiciProvider.getCodici(CodiciType.CODICE_GENERE_PUBBLICAZIONE), ""));



		List listaOrdTit = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_ORDINAMENTO_TITOLI));
		if (primaCall.equals("SI")) {
			int size = listaOrdTit.size();
			for (int i = 0; i < size; i++) {
				if (((ComboCodDescVO)listaOrdTit.get(i)).getCodice().toString().equals("4")) {
					listaOrdTit.remove(i);
					break;
				}
			}
			titGenVO.setListaTipiOrdinam(listaOrdTit);
		} else {
			titGenVO.setListaTipiOrdinam(listaOrdTit);
		}

		titGenVO.setListaFormatoLista(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceMinMax()));
		titGenVO.setListaSpecificita(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceSpecificita()));
		titGenVO.setListaResponsabilita(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_RESPONSABILITA)));
		titGenVO.setListaBiblioteche(carCombo.loadBiblioteche());
	}

	public void inizCaricaCartog(InterrogazioneTitoloCartografiaVO titCarVO) throws Exception {
		// Impostazione di inizializzazione jsp Combo specifiche area cartografia

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		titCarVO.setListaMeridianiOrigine(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_MERIDIANO_ORIGINE)));



		// Inizio modifica almaviva2 24.06.2011 - BUG MANTIS collaudo 4386 (adeguamento della mappa di interrogazione per parametri
		// di cartografia a quella di creazione (drop per tipologia di latitudine/longitudine invece dei RadioButton)
//		if (titCarVO.getLongitudineRadio1() == null || titCarVO.getLongitudineRadio1().equals("vuoto")) {
//			titCarVO.setLongitudineRadio1("");
//			titCarVO.setLongitudineInput1("");
//		}
//		if (titCarVO.getLongitudineRadio2() == null || titCarVO.getLongitudineRadio2().equals("vuoto")) {
//			titCarVO.setLongitudineRadio2("");
//			titCarVO.setLongitudineInput2("");
//		}
//		if (titCarVO.getLatitudineRadio1() == null || titCarVO.getLatitudineRadio1().equals("vuoto")) {
//			titCarVO.setLatitudineRadio1("");
//			titCarVO.setLatitudineInput1("");
//		}
//		if (titCarVO.getLatitudineRadio2() == null || titCarVO.getLatitudineRadio2().equals("vuoto")) {
//			titCarVO.setLatitudineRadio2("");
//			titCarVO.setLatitudineInput2("");
//		}

		// Modifica almaviva2 BUG MANTIS (collaudo) 4831 ;
		// è necessario impostare a vuoto anche i campi tipo Longitudine e tipo Latitudine quando non sono richieste le specificità
		if (titCarVO.getLongitTipo1() == null || titCarVO.getLongitTipo1().equals("")) {
			titCarVO.setLongitTipo1("");
			titCarVO.setLongitudineInput1("");
		}
		if (titCarVO.getLongitTipo2() == null || titCarVO.getLongitTipo2().equals("")) {
			titCarVO.setLongitTipo2("");
			titCarVO.setLongitudineInput2("");
		}
		if (titCarVO.getLatitTipo1() == null || titCarVO.getLatitTipo1().equals("")) {
			titCarVO.setLatitTipo1("");
			titCarVO.setLatitudineInput1("");
		}
		if (titCarVO.getLatitTipo2() == null || titCarVO.getLatitTipo2().equals("")) {
			titCarVO.setLatitTipo2("");
			titCarVO.setLatitudineInput2("");
		}
		if (titCarVO.getListaLongitudine() == null || titCarVO.getListaLongitudine().isEmpty()) {
			titCarVO.setListaLongitudine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceEstOvest()));
		}

		if (titCarVO.getListaLatitudine() == null || titCarVO.getListaLatitudine().isEmpty()) {
			titCarVO.setListaLatitudine(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceNordSud()));
		}
		// Fine modifica almaviva2 24.06.2011 - BUG MANTIS collaudo 4386

		if (titCarVO.getTipoScalaRadio() == null) {
			titCarVO.setTipoScalaRadio("");
		}


	}

	public void inizCaricaGrafic(InterrogazioneTitoloGraficaVO titGraVO) throws Exception {
		// Impostazione di inizializzazione jsp Combo specifiche area grafica

		CaricamentoCombo carCombo = new CaricamentoCombo();
//		FactoryEJBDelegate factory = (FactoryEJBDelegate) this.servlet.getServletContext().getAttribute(FactoryEJBPlugin.FACTORY_EJB);
		titGraVO.setListaDesignSpecMater(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_MATERIALE_GRAFICO)));
		if (titGraVO.getDesignSpecMaterSelez() == null) {
			titGraVO.setDesignSpecMaterSelez("");
		}
		titGraVO.setListaSupportoPrimario(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_FISICO_PER_GRAFICA)));
		titGraVO.setListaIndicatoreColore(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_COLORE116)));
		titGraVO.setListaIndicatoreTecnicaGrafica(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TECNICA_STAMPE)));
		titGraVO.setListaIndicatoreTecnicaDisegno(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TECNICA_DISEGNI)));
		titGraVO.setListaDesignatoreFunzione(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_DESIGNAZIONE_FUNZIONE)));
	}

	public void inizCaricaMusic(InterrogazioneTitoloMusicaVO titMusVO) throws Exception {
		// Impostazione di inizializzazione jsp Combo specifiche area musica

		CaricamentoCombo carCombo = new CaricamentoCombo();
//		FactoryEJBDelegate factory = (FactoryEJBDelegate) this.servlet.getServletContext().getAttribute(FactoryEJBPlugin.FACTORY_EJB);
		titMusVO.setListaTipoTestoLetterario(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TIPO_TESTO_LETTERARIO)));
		titMusVO.setListaElaborazioni(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_ELABORAZIONE)));
		titMusVO.setListaPresentazione(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_PRESENTAZIONE)));
		titMusVO.setListaFormeMusicali(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_FORMA_MUSICALE)));
		titMusVO.setListaTonalita(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TONALITA)));
	}

	public void inizCaricaAudiovisivo(InterrogazioneTitoloAudiovisivoVO titAudvisVO) throws Exception {
		// Impostazione di inizializzazione jsp Combo specifiche area audiovisivo

		CaricamentoCombo carCombo = new CaricamentoCombo();
		titAudvisVO.setListaTipoVideo(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TIPO_VIDEO)));
		titAudvisVO.setListaFormaPubblDistr(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE_DISTRIBUZIONE)));
		titAudvisVO.setListaTecnicaVideoFilm(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TECNICA_VIDEOREGISTRAZIONI_E_FILM)));
		//04.03.2015 Corretiva almaviva2:
		// tabella per campo FormaPubblicazione errata CODICE_FORMA_PUBBLICAZIONE sostituita da CODICE_FORMA_PUBBLICAZIONE_AUDIOVIDEO
		// titAudvisVO.setListaFormaPubblicazione(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE)));
		titAudvisVO.setListaFormaPubblicazione(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE_AUDIOVIDEO)));

		titAudvisVO.setListaVelocita(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_VELOCITA)));
	}

	public void inizCaricaElettronico(InterrogazioneTitoloElettronicoVO titElettrVO) throws Exception {
		// Impostazione di inizializzazione jsp Combo specifiche area elettronico

		CaricamentoCombo carCombo = new CaricamentoCombo();
		titElettrVO.setListaTipoRisorsaElettronica(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_TIPO_RISORSA_ELETTRONICA)));
		titElettrVO.setListaIndicazioneSpecificaMateriale(carCombo.loadComboCodiciDesc (CodiciProvider.getCodici(CodiciType.CODICE_INDICAZIONE_SPECIFICA_MATERIALE)));
	}



	public ActionForward annullaOperazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("InterrogazioneTitoloAction::annulla");

		return	Navigation.getInstance(request).goBack();
	}


	public ActionForward caricaFileIdCatalLocale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InterrogazioneTitoloForm currentForm = (InterrogazioneTitoloForm) form;

		try {
			BufferedReader reader;
			FormFile file = currentForm.getUploadImmagine();
			try {
				InputStream in = file.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
			} catch (Exception e) {
				log.error("", e);
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				return mapping.getInputForward();
			}

			List<String> listaBidDaFile = new ArrayList<String>();

			int inserted = 0;
			int dropped = 0;
			String bid = null;
			while ( (bid = reader.readLine() ) != null ) {
				bid = bid.trim();
				if (!ValidazioneDati.leggiXID(bid)) {
					dropped++;
					continue;
				}
				inserted++;
				listaBidDaFile.add(bid);
			}

			reader.close();

			if (!ValidazioneDati.isFilled(listaBidDaFile)) { // nessun bid
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));
				return mapping.getInputForward();
			}

			currentForm.setListaBidDaFile(listaBidDaFile);
			currentForm.getInterrGener().setRicLocale(true);
			currentForm.getInterrGener().setRicIndice(false);

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.esporta.caricaFile", file.getFileName(), inserted, dropped));
			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.marImgNotValid"));
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		InterrogazioneTitoloForm currentForm = (InterrogazioneTitoloForm) form;

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			UserVO utente = Navigation.getInstance(request).getUtente();

			SIFListaBibliotecheSistemaMetropolitanoVO richiesta =
				new SIFListaBibliotecheSistemaMetropolitanoVO(
								utente.getCodPolo(),
								utente.getCodBib(),
								"Biblioteche sistema Metropolitano",
								false,
								ConstantDefault.ELEMENTI_BLOCCHI.getDefaultAsNumber(),
								BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO);

			//almaviva5_20110728 #4588
			List<BibliotecaVO> filtroBib = currentForm.getFiltroLocBib();
			if (ValidazioneDati.isFilled(filtroBib)) {
				Integer[] selected = new Integer[filtroBib.size()];
				for (int i = 0; i < selected.length; i++)
					selected[i] = filtroBib.get(i).getRepeatableId();

				richiesta.setSelected(selected);
			}

			return biblio.getSIFListaBibliotecheSistemaMetropolitano(richiesta);

		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.marImgNotValid"));
			return mapping.getInputForward();
		}
	}


	// INIZIO Evolutiva Novembre 2015 - almaviva2 gestione interrogazione da SERVIZI ILL
	private void valorizzaRicercaPerDocumentoNonSBN(
			InterrogazioneTitoloGeneraleVO titoloGeneraleVO,
			DocumentoNonSbnVO doc) throws Exception {

		//valorizzazione mappa di ricerca titolo con i dati del doc non sbn (da OPAC?)
		String bid = doc.getBid();
		if (ValidazioneDati.isFilled(bid))
			titoloGeneraleVO.setBid(bid);

		String titolo = doc.getTitolo();
		if (ValidazioneDati.isFilled(titolo))
			titoloGeneraleVO.setTitolo(titolo);

		char natura = doc.getNatura();
		titoloGeneraleVO.setNaturaSelez1(String.valueOf(natura));

		String tipoNumStd = doc.getTipoNumStd();
		if (ValidazioneDati.isFilled(tipoNumStd))
			titoloGeneraleVO.setNumStandardSelez(tipoNumStd);

		String numeroStd = doc.getNumeroStd();
		if (ValidazioneDati.isFilled(numeroStd))
			// settembre 2018 - almaviva2 se nel numero standard sono presenti dei trattini vanno eliminati altrimenti
			// la ricerca non andrà a buon fine es. 978-88-315-3376-8 diventerà 9788831533768
			numeroStd = numeroStd.replaceAll("-", "");

			titoloGeneraleVO.setNumStandard1(numeroStd);

		String paese = doc.getPaese();
		if (ValidazioneDati.isFilled(paese))
			titoloGeneraleVO.setPaeseSelez(paese);

		String lingua = doc.getLingua();
		if (ValidazioneDati.isFilled(lingua))
			titoloGeneraleVO.setLinguaSelez(lingua);

	}



	public ActionForward cercaTitoliPerILL(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		InterrogazioneTitoloForm ricTit = (InterrogazioneTitoloForm) form;
		InterrogazioneTitoloGeneraleVO interrGener = ricTit.getInterrGener();
		AreaTabellaOggettiDaCondividereVO areaTabellaOggettiDaCondividereVO = new AreaTabellaOggettiDaCondividereVO();
		DettaglioTitoloCompletoVO dettaglioTitoloCompletoVO = new DettaglioTitoloCompletoVO();


		AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
		areaDatiPass.setRicercaPolo(false);
		areaDatiPass.setRicercaIndice(false);

		areaTabellaOggettiDaCondividereVO.setLivelloRicerca("LOC");
		areaTabellaOggettiDaCondividereVO.setNatura(interrGener.getNaturaSelez1());
		areaTabellaOggettiDaCondividereVO.setDescrizionePerRicerca(interrGener.getTitolo());
		if (ricTit.getFiltroLocBib() != null && ricTit.getFiltroLocBib().size() > 0) {
			areaTabellaOggettiDaCondividereVO.setFiltroLocBib(ricTit.getFiltroLocBib());
		}

		areaTabellaOggettiDaCondividereVO.setIdPadre(interrGener.getBid());
		areaTabellaOggettiDaCondividereVO.setBidRoot(true);

		if (interrGener.getBid().length() > 0) {
			dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setBid(interrGener.getBid());
			areaTabellaOggettiDaCondividereVO.setTipoRicerca("PRIMARIC");
		} else {
			areaTabellaOggettiDaCondividereVO.setTipoRicerca("IDENTIFICATIVO");
		}

		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setTipoMat("");


		// almaviva2 Febbraio 2016 - Intervento interno - Interrogazione per cercaTitoliPerILL; nel caso di assenza di bid
		// e di assenza del numero standard non si deve valorizzare la lista dei possibili numSTD altrimenti la procedura va in errore
		if (interrGener.getNumStandardSelez().equals("") && interrGener.getNumStandard1().equals("")) {
			// Non si deve valorizzare alcun elemento della tabella dei numeri standard
		} else {
			dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setNumStanTipo(interrGener.getNumStandardSelez());
			dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setNumStanNumero(interrGener.getNumStandard1());

			TabellaNumSTDImpronteVO tabNumSt = new TabellaNumSTDImpronteVO(
					interrGener.getNumStandard1(), interrGener.getNumStandardSelez(), "", "");
			dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().addListaNumStandard(tabNumSt);
		}


		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setAreaTitTitolo(interrGener.getTitolo());
		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setNatura(interrGener.getNaturaSelez1());
		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setTipoRec(interrGener.getTipiRecordSelez());
		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setTipoData(interrGener.getTipoDataSelez());
		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setPaese(interrGener.getPaeseSelez());
		dettaglioTitoloCompletoVO.getDetTitoloPFissaVO().setLingua(interrGener.getLinguaSelez());

		areaTabellaOggettiDaCondividereVO.setDettTitComVO(dettaglioTitoloCompletoVO);
		areaTabellaOggettiDaCondividereVO.setDettAutGenVO(null);


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		try {
			areaTabellaOggettiDaCondividereVO = factory.getGestioneBibliografica().ricercaDocumentoPerCondividi(
					areaTabellaOggettiDaCondividereVO, Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			resetToken(request);
			return mapping.getInputForward();

		}

		if (areaTabellaOggettiDaCondividereVO == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			resetToken(request);
			return mapping.getInputForward();
		}


		if (!areaTabellaOggettiDaCondividereVO.getCodErr().equals("0000")) {
			if (areaTabellaOggettiDaCondividereVO.getCodErr().equals("9999") || areaTabellaOggettiDaCondividereVO.getTestoProtocollo() != null ) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaTabellaOggettiDaCondividereVO.getTestoProtocollo()));
				this.saveErrors(request, errors);
				areaDatiPass.setRicercaPolo(false);
				areaDatiPass.setRicercaIndice(false);
				// Inizio Modifica almaviva2 11.01.2013 BUG MANTIS 5205 (esercizio) - eliminare i crea in caso di errore
				ricTit.setCreaDoc("NO");
				ricTit.setCreaDocLoc("NO");
				// Fine Modifica almaviva2 25.01.2010 BUG MANTIS 5205
				return mapping.getInputForward();
			} else if (!areaTabellaOggettiDaCondividereVO.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica." + areaTabellaOggettiDaCondividereVO.getCodErr()));
				areaDatiPass.setRicercaPolo(false);
				areaDatiPass.setRicercaIndice(false);
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (areaTabellaOggettiDaCondividereVO.getNumNotizie() > 0) {
			request.setAttribute("areaDatiPassPerInterrogazione", areaDatiPass);

			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassaggioInterrogazioneTitoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSimili(true);
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setLivelloTrovato("P");
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setIdLista(areaTabellaOggettiDaCondividereVO.getIdLista());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setListaSintetica(areaTabellaOggettiDaCondividereVO.getListaSintetica());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setMaxRighe(areaTabellaOggettiDaCondividereVO.getMaxRighe());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumBlocco(areaTabellaOggettiDaCondividereVO.getNumBlocco());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumNotizie(areaTabellaOggettiDaCondividereVO.getNumNotizie());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setNumPrimo(areaTabellaOggettiDaCondividereVO.getNumPrimo());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotBlocchi(areaTabellaOggettiDaCondividereVO.getTotBlocchi());
			areaDatiPassaggioInterrogazioneTitoloReturnVO.setTotRighe(areaTabellaOggettiDaCondividereVO.getTotRighe());

			request.setAttribute("bidRoot",	"NO");
			request.setAttribute("areaDatiPassReturnSintetica",	areaDatiPassaggioInterrogazioneTitoloReturnVO);

			request.setAttribute("elencoBiblSelezionate", ricTit.getElencoBiblioMetropolitane());
			request.setAttribute(ServiziDelegate.LISTA_BIB_SISTEMI_METROPOLITANI, ricTit.getFiltroLocBib() );
			return Navigation.getInstance(request).goForward(mapping.findForward("sinteticaTitoliPerServizi"));
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.gestioneBibliografica.titNotFound"));
			this.saveErrors(request, errors);
			areaDatiPass.setRicercaPolo(false);
			areaDatiPass.setRicercaIndice(false);
			resetToken(request);
			return mapping.getInputForward();
		}

	}
}
