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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaLocalizzazioniView;
import it.iccu.sbn.web.actionforms.gestionebibliografica.DettaglioLocalizzazioneForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class DettaglioLocalizzazioneAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(DettaglioLocalizzazioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.annulla", "annullaOper");
		map.put("button.gestLocal.confermaAgg", "confermaAggior");

		map.put("button.copia.segnatura", "copiaSegnatura");
		map.put("button.copia.consistenza", "copiaConsistenza");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.debug("unspecified()");
		DettaglioLocalizzazioneForm dettaglioLocalizzazioneForm = (DettaglioLocalizzazioneForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		dettaglioLocalizzazioneForm.setLocalizzazione(
				(SinteticaLocalizzazioniView) request.getAttribute("elementoLocalizzazione"));
		dettaglioLocalizzazioneForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));
		dettaglioLocalizzazioneForm.setIdOggColl((String) request.getAttribute("idOggColl"));
		dettaglioLocalizzazioneForm.setDescOggColl((String) request.getAttribute("descOggColl"));

		dettaglioLocalizzazioneForm.setAreePassSifVo((AreePassaggioSifVO) request.getAttribute("AreePassSifVo"));
		dettaglioLocalizzazioneForm.setCentroSistema((String) request.getAttribute("centroSistema"));
		dettaglioLocalizzazioneForm.setLivRic((String) request.getAttribute("livRicerca"));

		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
		// Nel caso di POSSESSO dopo l'aggiornamento si prospettano direttamente i dati di dettaglio e con una sola operazione
		// si inviano sia la richiesta di inserimento/correzione che quella di variazione dei dati di possesso;
		if (request.getAttribute("areaLocalizzaMultipla") != null) {
			dettaglioLocalizzazioneForm.setAreaLocalizzaMultipla((AreaDatiLocalizzazioniAuthorityMultiplaVO) request.getAttribute("areaLocalizzaMultipla"));
		}

		// Modifica almaviva2 BUG MANTIS 4158 (collaudo) 18.01.2011
		// viene valorizzata la descrizione del campo tipoDigitalizzazione per la mappa del dettaglio
		dettaglioLocalizzazioneForm.setDescTipoDigitPolo(dettaglioLocalizzazioneForm.getAreePassSifVo().getDescTipoDigitPolo());

		if (!dettaglioLocalizzazioneForm.getTipoProspettazione().equals("DET")) {
			 this.caricaComboGenerali(dettaglioLocalizzazioneForm);
		}

		if (dettaglioLocalizzazioneForm.getTipoProspettazione().equals("GESTCONS")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("listaInventariTitolo")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("consIndiceEsemplare")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("modificaCollocazione")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("modificaInventario")) {
			Navigation.getInstance(request).setTesto("Invio consistenza indice");
			dettaglioLocalizzazioneForm.setConsistenzaPolo(dettaglioLocalizzazioneForm.getAreePassSifVo().getConsistenzaPolo());
			dettaglioLocalizzazioneForm.setSegnaturaPolo(dettaglioLocalizzazioneForm.getAreePassSifVo().getSegnaturaPolo());
			dettaglioLocalizzazioneForm.setUriPolo(dettaglioLocalizzazioneForm.getAreePassSifVo().getUriPolo());
//			dettaglioLocalizzazioneForm.setDescTipoDigitPolo(dettaglioLocalizzazioneForm.getAreePassSifVo().getDescTipoDigitPolo());
			dettaglioLocalizzazioneForm.setCodTipoDigitPolo(dettaglioLocalizzazioneForm.getAreePassSifVo().getCodTipoDigitPolo());
		}


		return mapping.getInputForward();
	}

	private void caricaComboGenerali(DettaglioLocalizzazioneForm dettaglioLocalizzazioneForm)
	throws RemoteException, CreateException, NamingException {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		dettaglioLocalizzazioneForm.setListaMutiloM(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSiNo()));
		dettaglioLocalizzazioneForm.setListaFormatoElettr(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceSiNo()));
		dettaglioLocalizzazioneForm.setListaTipoDigital(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoDigitalizzazione()));
	}


	public ActionForward confermaAggior(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {


		DettaglioLocalizzazioneForm dettaglioLocalizzazioneForm = (DettaglioLocalizzazioneForm) form;

		// Inizio modifica almaviva2 BUG MANTIS 4157 (Collaudo)18.01.2011
		// Vengono effettuati i seguenti controlli nella variazione dei dati Possesso:
		// --> campo URI può essere riempito solo se sono valorizzati i due precedenti (Tipo digitalizzazione e URI) e se il primo
		//     reca il valore 'S';
		// --> il secondo (Tipo digitalizzazione) può essere riempito solo se il primo reca il valore 'S'

		if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione() != null
				&& !dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione().trim().equals("")) {
			if (dettaglioLocalizzazioneForm.getLocalizzazione().getFormatoElettronico().equals("N")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noTipoDigitsenzaFormElet"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		}


		if (dettaglioLocalizzazioneForm.getLocalizzazione().getUriCopiaElettr() != null
				&& !dettaglioLocalizzazioneForm.getLocalizzazione().getUriCopiaElettr().trim().equals("")){
			if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione().equals("")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noURIsenzaTipoDigit"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		}

		// Fine modifica almaviva2 BUG MANTIS 4157 (Collaudo)18.01.2011


		AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();

		areaLocalizza.setIdLoc(dettaglioLocalizzazioneForm.getIdOggColl());
		if (dettaglioLocalizzazioneForm.getAreePassSifVo().getNaturaOggetto() == null
				&& dettaglioLocalizzazioneForm.getAreePassSifVo().getTipMatOggetto() == null) {
			areaLocalizza.setNatura("");
			areaLocalizza.setTipoMat("");
			if (dettaglioLocalizzazioneForm.getIdOggColl().substring(3, 4).equals("V")) {
				areaLocalizza.setAuthority("AU");
			} else if (dettaglioLocalizzazioneForm.getIdOggColl().substring(3, 4).equals("M")) {
				areaLocalizza.setAuthority("MA");
			}
		} else if (dettaglioLocalizzazioneForm.getAreePassSifVo().getNaturaOggetto() != null
				&& dettaglioLocalizzazioneForm.getAreePassSifVo().getTipMatOggetto() != null) {
			if (dettaglioLocalizzazioneForm.getAreePassSifVo().getNaturaOggetto().equals("A")) {
				if (dettaglioLocalizzazioneForm.getAreePassSifVo().getTipMatOggetto().equals("U")) {
					areaLocalizza.setAuthority("UM");
					areaLocalizza.setNatura(dettaglioLocalizzazioneForm.getAreePassSifVo().getNaturaOggetto());
					areaLocalizza.setTipoMat(dettaglioLocalizzazioneForm.getAreePassSifVo().getTipMatOggetto());
				} else {
					areaLocalizza.setAuthority("TU");
					areaLocalizza.setNatura(dettaglioLocalizzazioneForm.getAreePassSifVo().getNaturaOggetto());
					areaLocalizza.setTipoMat(dettaglioLocalizzazioneForm.getAreePassSifVo().getTipMatOggetto());
				}
			} else {
				areaLocalizza.setAuthority("");
				areaLocalizza.setNatura(dettaglioLocalizzazioneForm.getAreePassSifVo().getNaturaOggetto());
				areaLocalizza.setTipoMat(dettaglioLocalizzazioneForm.getAreePassSifVo().getTipMatOggetto());

			}
		}
		if (dettaglioLocalizzazioneForm.getTipoProspettazione().equals("INS")) {
			// Inserimento della localizzazione in Polo/Indice per l'oggetto inserito
			areaLocalizza.setTipoOpe("Localizza");
		} else {
			areaLocalizza.setTipoOpe("Correggi");
		}

		//areaLocalizza.setTipoLoc(dettaglioLocalizzazioneForm.getEleSinteticaLocalizzazioniView().getTipoLoc());
		areaLocalizza.setConsistenza(dettaglioLocalizzazioneForm.getLocalizzazione().getConsistenza());
		areaLocalizza.setIndicatoreMutilo(dettaglioLocalizzazioneForm.getLocalizzazione().getValoreM());

		areaLocalizza.setCodiceAnagrafeBiblioteca(dettaglioLocalizzazioneForm.getLocalizzazione().getIDAnagrafe());
		areaLocalizza.setCodiceSbn(dettaglioLocalizzazioneForm.getLocalizzazione().getIDSbn());
		areaLocalizza.setDispoFormatoElettr(dettaglioLocalizzazioneForm.getLocalizzazione().getFormatoElettronico());
		areaLocalizza.setFondo(dettaglioLocalizzazioneForm.getLocalizzazione().getFondo());
		areaLocalizza.setIdLoc(dettaglioLocalizzazioneForm.getIdOggColl());
		areaLocalizza.setNote(dettaglioLocalizzazioneForm.getLocalizzazione().getNote());
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoLoc().equals("Possesso/Gestione")) {
			areaLocalizza.setSbnTipoLoc(SbnTipoLocalizza.TUTTI);
		} else if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoLoc().equals("Possesso")) {
			areaLocalizza.setSbnTipoLoc(SbnTipoLocalizza.POSSESSO);
		}  else if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoLoc().equals("Gestione")) {
			areaLocalizza.setSbnTipoLoc(SbnTipoLocalizza.GESTIONE);
		}

//		areaLocalizza.setSbnTipoLoc(SbnTipoLocalizza.GESTIONE);
		areaLocalizza.setSegnatura(dettaglioLocalizzazioneForm.getLocalizzazione().getSegnatura());
		areaLocalizza.setSegnaturaAntica(dettaglioLocalizzazioneForm.getLocalizzazione().getSegnaturaAntica());
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione().equals("")) {
			areaLocalizza.setTipoDigitalizzazione("");
		} else {
			areaLocalizza.setTipoDigitalizzazione(dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione());
		}

		areaLocalizza.setTipoLoc(dettaglioLocalizzazioneForm.getLocalizzazione().getTipoLoc());
		areaLocalizza.setUriAccesso(dettaglioLocalizzazioneForm.getLocalizzazione().getUriCopiaElettr());

		boolean isDocFisico = dettaglioLocalizzazioneForm.getTipoProspettazione().equals("GESTCONS")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("listaInventariTitolo")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("consIndiceEsemplare")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("modificaCollocazione")
				|| dettaglioLocalizzazioneForm.getTipoProspettazione().equals("modificaInventario");

		if (isDocFisico) {
			areaLocalizza.setIndice(true);
			areaLocalizza.setPolo(true);
			areaLocalizza.setMantieniAllineamento(true);
		} else {
			if (dettaglioLocalizzazioneForm.getLivRic().equals("I")) {
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(true);
				areaLocalizza.setMantieniAllineamento(true);
			} else if (dettaglioLocalizzazioneForm.getLivRic().equals("P")) {
				if (!areaLocalizza.getAuthority().equals("")) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.LocalNonPoss"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}
				areaLocalizza.setIndice(false);
				areaLocalizza.setPolo(true);
			}

		}

		Navigation navi = Navigation.getInstance(request);
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getIDSbn() == null
				|| dettaglioLocalizzazioneForm.getLocalizzazione().getIDSbn().equals("")) {
			areaLocalizza.setCodiceSbn(navi.getUtente().getCodPolo()
					+ navi.getUtente().getCodBib());
		} else {
			areaLocalizza.setCodiceSbn(dettaglioLocalizzazioneForm.getLocalizzazione().getIDSbn());
		}
//				areaLocalizza.setCodiceSbn(Navigation.getInstance(request).getUtente().getCodPolo()
//						+ Navigation.getInstance(request).getUtente().getCodBib());


		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
		// Nel caso di POSSESSO dopo l'aggiornamento si prospettano direttamente i dati di dettaglio e con una sola operazione
		// si inviano sia la richiesta di inserimento/correzione che quella di variazione dei dati di possesso;
		dettaglioLocalizzazioneForm.getAreaLocalizzaMultipla().addListaAreaLocalizVO(areaLocalizza);


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//		AreaDatiVariazioneReturnVO areaDatiPassReturn = factory.getGestioneBibliografica().localizzaAuthority(areaLocalizza,
//						Navigation.getInstance(request).getUserTicket());
		AreaDatiVariazioneReturnVO areaDatiPassReturn = factory.getGestioneBibliografica().
			localizzaUnicoXML(dettaglioLocalizzazioneForm.getAreaLocalizzaMultipla(), navi.getUserTicket());


		if (areaDatiPassReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		// Inizio modifica almaviva2 BUG MANTIS 4157 (Collaudo)18.01.2011
		// Viene inviato il diagnostico di Operazione OK per codice Ritorno = "0000" per modifica Dati Possesso in Gest.Localizzazione
		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			// Evolutiva Google3: si torna a Analitica Titolo
//			request.setAttribute("bid", dettaglioLocalizzazioneForm.getIdOggColl());
//			request.setAttribute("livRicerca", dettaglioLocalizzazioneForm.getLivRic());
//			ActionMessages errors = new ActionMessages();
//			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
//			this.saveErrors(request, errors);
			request.setAttribute("livRicerca", dettaglioLocalizzazioneForm.getLivRic());
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			if (areaDatiPassReturn.getTestoProtocolloInformational() != null) {
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOkConParametro", areaDatiPassReturn.getTestoProtocolloInformational()));
			} else {
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
			}
			this.saveErrors(request, errors);

			//almaviva5_20140626 with LV
			//se provengo dalla linea di doc.fisico non devo tornare ad analitica titolo ma alla mappa precedente.
			if (isDocFisico)
				return navi.goBack(true);

			return navi.goBack(mapping.findForward("analitica"), true);
		} else {
			if (areaDatiPassReturn.getCodErr().equals("9999")
					|| areaDatiPassReturn.getTestoProtocollo() != null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo",	areaDatiPassReturn.getTestoProtocollo()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica."	+ areaDatiPassReturn.getCodErr()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		if (isDocFisico) {
			return navi.goBack(true);
//			return Navigation.getInstance(request).goToBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL, true);
		}

		return navi.goBack(mapping.findForward("annulla"), true);

	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

//		return Navigation.getInstance(request).goToBookmark(DocumentoFisicoCostant.BOOKMARK_GEST_ESEMPL, true);
		return Navigation.getInstance(request).goBack(true);
	}


	public ActionForward copiaSegnatura(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.getInputForward();

	}

	public ActionForward copiaConsistenza(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DettaglioLocalizzazioneForm dettaglioLocalizzazioneForm = (DettaglioLocalizzazioneForm) form;

		// Modifica almaviva2 14/06/2011 BUG MANTIS 4497
		// Copiando le informazioni relative alla Copia digitale, ovvero il Tipo digitalizzazione e l'URI,
		// nella pagina 'Invio consistenza in Indice'  il sistema deve mettere, in presenza di valori
		// in Tipo digitalizzazione e URI, automaticamente a 'S' il campo Formato elettronico.

		Boolean modUri = false;
		Boolean modTipDig = false;

		// Inizio modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010
		// si copia solo i valori che non hanno una precedente impostazione
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getConsistenza().trim().equals("")) {
			dettaglioLocalizzazioneForm.getLocalizzazione().setConsistenza(dettaglioLocalizzazioneForm.getConsistenzaPolo());
		}
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getSegnatura().trim().equals("")) {
			dettaglioLocalizzazioneForm.getLocalizzazione().setSegnatura(dettaglioLocalizzazioneForm.getSegnaturaPolo());
		}

		// Intervento almaviva2 28.06.2011 -BUG MANTIS 4526 (collaudo) e BUG MANTIS 4532 (esercizio)
		// il tasto copia nell'invio consistenza in Indice mandava in errore l'applicazione; inseriti i controlli sul valore
		// null di dettaglioLocalizzazioneForm.getUriPolo() e dettaglioLocalizzazioneForm.getCodTipoDigitPolo()
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getUriCopiaElettr().trim().equals("")) {
			if (dettaglioLocalizzazioneForm.getUriPolo() != null && !dettaglioLocalizzazioneForm.getUriPolo().equals("")) {
				dettaglioLocalizzazioneForm.getLocalizzazione().setUriCopiaElettr(dettaglioLocalizzazioneForm.getUriPolo());
			}
			if (!dettaglioLocalizzazioneForm.getLocalizzazione().getUriCopiaElettr().trim().equals("")) {
				modUri = true;
			}
		}
		if (dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione().trim().equals("")) {
			if (dettaglioLocalizzazioneForm.getCodTipoDigitPolo() != null && !dettaglioLocalizzazioneForm.getCodTipoDigitPolo().equals("")) {
				dettaglioLocalizzazioneForm.getLocalizzazione().setTipoDigitalizzazione(dettaglioLocalizzazioneForm.getCodTipoDigitPolo());
			}
			if (!dettaglioLocalizzazioneForm.getLocalizzazione().getTipoDigitalizzazione().trim().equals("")) {
				modTipDig = true;
			}
		}

		// Fine modifica almaviva2 07.07.2010 su richiesta di Documento Fisico (Revisione Consistenza in indice) 07.07.2010

		if (modUri && modTipDig) {
			dettaglioLocalizzazioneForm.getLocalizzazione().setFormatoElettronico("S");
		}

		return mapping.getInputForward();
	}

}
