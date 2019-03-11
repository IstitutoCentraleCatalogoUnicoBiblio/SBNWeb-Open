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
package it.iccu.sbn.web.integration.action.erogazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.InserimentoRichiestaForm;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm.RichiestaListaMovimentiType;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
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

public class InserimentoRichiestaAction extends ListaMovimentiAction {
	//
	private static String[] COPPIA_NATURA = { "S99", "S9", "S8", "S26",
		"S25", "S24", "S22", "S18", "S16", "S15", "S14", "S13",
		"S12", "S11", "S10", "M8", "M26",
		"M25", "M24", "M22", "M18", "M16", "M15", "M14", "M13",
		"M12", "M11", "W8", "W26",
		"W25", "W24", "W22", "W18", "W16", "W15", "W14", "W13",
		"W12", "W11"};
	//
	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.avanti", "avanti");
		map.put("servizi.bottone.indietro", "indietro");
		map.put("servizi.bottone.insRich", "insRichiesta");
		return map;
	}
	//
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InserimentoRichiestaForm currentForm = (InserimentoRichiestaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			currentForm.setMov(new MovimentoListaVO((MovimentoVO) request.getAttribute(ServiziDelegate.MOV_DA_INSERIRE)));
			currentForm.setUtenteVO((UtenteBaseVO)request.getAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE));
			currentForm.setInfoDocumentoVO((InfoDocumentoVO)request.getAttribute(NavigazioneServizi.DATI_DOCUMENTO));

			/*
			HttpSession session = request.getSession();

			UtenteWeb utente = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
			currentForm.setCodUtente(utente.getUserId());
			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setCod_biblio((String) session.getAttribute(Constants.COD_BIBLIO));
			currentForm.setBibsel((String) session.getAttribute(Constants.BIBLIO_SEL));
			//descrServizio
			RichiestaOpacVO ricOpac = (RichiestaOpacVO) session.getAttribute(Constants.RichiestaOpacVO);
			currentForm.setAutore(ricOpac.getAutore());
			currentForm.setTitolo(ricOpac.getTitolo());
			currentForm.setAnno(ricOpac.getAnno());
			currentForm.setServizio(ricOpac.getDescrServizio());

			currentForm.setDataRic(DaoManager.now().toLocaleString());
			InfoDocumentoVO docVO = (InfoDocumentoVO) request.getAttribute(Constants.INFO_DOCUMENTO);
			currentForm.setInfoDocumentoVO(docVO);
			currentForm.setLstCodiciServizio((List<ServizioBibliotecaVO>) request.getAttribute(Constants.SERVIZI_ATTIVI_DOCUMENTO));
			currentForm.setCodServNuovaRich(ricOpac.getServizio());
			*/

			//String ticket, String codPolo, String codBib, String codTipoServizio)
			// Caricamento dinamico dei campi presenti
			// sulla jsp "richiestaServizioLoc.jsp",
			// richiamando il metodo getServizioWebDatiRichiesti,
			// ritorna una lista di campi che dovranno
			// essere filtrati per visibilità e per natura del
			// documento
			String ticket = Navigation.getInstance(request).getUserTicket();
			String codPolo = currentForm.getMov().getCodPolo();
			String codBib = currentForm.getMov().getCodBibOperante();
			String codTipoServizio = currentForm.getMov().getCodTipoServ();
			String Servizio = currentForm.getMov().getCodServ();
			InfoDocumentoVO docVO = currentForm.getInfoDocumentoVO();

			currentForm.setServizio(Servizio);

			//richiamo il metodo getServizioWebDatiRichiesti ritorna
			//una lista di campi
			String natura;
			if (docVO.isRichiestaSuInventario())
				natura = docVO.getInventarioTitoloVO().getNatura();
			else
				natura = String.valueOf(docVO.getDocumentoNonSbnVO().getNatura());
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ServizioWebDatiRichiestiVO> listaCampi =
				factory.getGestioneServizi().getServizioWebDatiRichiesti(ticket, codPolo, codBib, codTipoServizio, natura);

		    //caricamento combo modalità di erogazione da fare!!!

			if (ValidazioneDati.isFilled(listaCampi) ) {
				// imposto in lstDirittiUtente la descrizione del Codice Tipo Servizio
				// prendendola dalla tabella tb_codici
				List<ServizioWebDatiRichiestiVO> campiVisibili = new ArrayList<ServizioWebDatiRichiestiVO>();
				for (ServizioWebDatiRichiestiVO campo : listaCampi) {
					if (campo.isUtilizzato()){//se utilizzato
						if (testCampo(natura, String.valueOf(campo.getCampoRichiesta()))){
							//se legame tra natura documento e cd_tabella è true
							campiVisibili.add(campo);
						}
					}
				}
				currentForm.setMostraCampi(campiVisibili);
			}

			return mapping.getInputForward();
		}

		catch (Exception ex) {
			//Errore di caricamento campi inserimento richiesta servizio
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.erroreInsRichiesta"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return (mapping.getInputForward());
		}
	}

	//
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			//
			return (mapping.findForward("listaMovimentiOpac"));
			//
		} catch (Exception ex) {
			// ricordare di inserire il msg di errore
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.ListaServiziOpac"));
			this.saveErrors(request, error);
			return (mapping.getInputForward());
		}
	}
	//
	public ActionForward avanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InserimentoRichiestaForm currentForm = (InserimentoRichiestaForm) form;
		//
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			// inizio controlli
			ActionMessages err = this.controlloCampiObbligatori(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}

			ActionMessages errF = this.controlliFormali(request, currentForm);
			if (errF.size() > 0) {
				this.saveErrors(request, errF);
				return mapping.getInputForward();
			}
			//fine controlli

			//Dopo i controlli caricamento tariffa DA FARE
			currentForm.setTariffa(100);
			return mapping.getInputForward();

		} catch (Exception ex) {
			// errore nell'inserimento della richiesta
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errore.servizi.utenti.erroreInsRichOpac"));
			this.saveErrors(request, error);
		}
		return mapping.getInputForward();
	}

	//
	public ActionForward insRichiesta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InserimentoRichiestaForm currentForm = (InserimentoRichiestaForm) form;
		HttpSession session = request.getSession();
		String codPolo = Navigation.getInstance(request).getPolo();
		//
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			// inizio controlli
			ActionMessages err = this.controlloCampiObbligatori(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}
			//
			ActionMessages errF = this.controlliFormali(request, currentForm);
			if (errF.size() > 0) {
				this.saveErrors(request, errF);
				return mapping.getInputForward();
			}
			// fine controlli
			//*****************************************************************************
			//
			// inizio inserimento richiesta di servizio locale per
			// documento sbn da opac
			UtenteWeb utente = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
			currentForm.setUtenteVO(new UtenteBaseVO());
			currentForm.getUtenteVO().setCodBib(currentForm.getCod_biblio());//utente.getCodBib());
			currentForm.getUtenteVO().setCodUtente(utente.getUserId());
			currentForm.getMovRicerca().setCodPolo(codPolo);
			currentForm.getMovRicerca().setCodBibOperante(currentForm.getCod_biblio());
			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
			// chiamata al metodo per l'inserimento di una nuova richiesta
			MovimentoVO movimento = null;
			ActionMessages error = new ActionMessages();
			DatiControlloVO datiControllo = controlliNuovaRichiesta(request, mapping, currentForm, getOperatore(request));
			//
			ControlloAttivitaServizioResult checkAttivita = datiControllo.getResult();
			//****************************************
			movimento = datiControllo.getMovimento();
			//
			switch (checkAttivita) {
			case OK:
				break;

			case OK_NON_ANCORA_DISPONIBILE:
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				Timestamp dataRitiro = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkAttivita.getMessage(), dataRitiro));
				movimento.setDataInizioPrev(dataRitiro);
				break;

			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
				disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				//almaviva5_20151012 nuova gestione prenotazioni
				int prenotazioniPendenti = disponibilitaVO.getPrenotazioniPendenti();
				//indice del messaggio per 0, una o più prenotazioni pendenti.
				int idMsg = prenotazioniPendenti == 0 ? 0 : prenotazioniPendenti > 1 ? 2 : 1;
				Timestamp dataPrenotazione = disponibilitaVO.getDataPrenotazione();
					error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(checkAttivita.getMessage(idMsg),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO, disponibilitaVO.getMovimentoAttivo().getCodTipoServ()),
						dataPrenotazione,
						prenotazioniPendenti));
				addErrors(request, error);
				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				movimento.setDataInizioPrev(dataPrenotazione);
				currentForm.setNuovaRichiesta(movimento);
				//
				currentForm.setConferma(true);
				saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

				return mapping.getInputForward();

			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
				disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				Timestamp dataPrenotazione2 = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkAttivita.getMessage()) );
				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				if (dataPrenotazione2.getTime() < System.currentTimeMillis())
					dataPrenotazione2 = DaoManager.now();

				movimento.setDataInizioPrev(dataPrenotazione2);
				currentForm.setNuovaRichiesta(movimento);
				currentForm.setConferma(true);
				saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

				return mapping.getInputForward();

			case ERRORE_DOCUMENTO_NON_DISPONIBILE:
			case ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL:
				String codNoDisp = datiControllo.getCodNoDisp();
				String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
				LinkableTagUtils.addError(request, new ActionMessage(checkAttivita.getMessage(), descr, datiControllo.getDataRedisp()));
				return mapping.getInputForward();

			default:
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(checkAttivita.getMessage()));
				this.saveErrors(request, error);
				return mapping.getInputForward();
			}

			//Inizio inserimento richiesta
			//controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio
			ControlloAttivitaServizio primoIter = null;
			ServizioBibliotecaVO servizioSelezionato = this.getServizio(currentForm.getLstCodiciServizio(),
					currentForm.getCodTipoServNuovaRich(), currentForm.getCodServNuovaRich());
			List<ControlloAttivitaServizio> listaAttivita = this.getListaAttivitaSuccessive(servizioSelezionato.getCodPolo(),
					servizioSelezionato.getCodBib(), servizioSelezionato.getCodTipoServ(), 0, null, request);

			primoIter = this.primoPassoIter(listaAttivita);

			datiControllo = super.eseguiControlli(request, movimento, primoIter, getOperatore(request), false, checkAttivita);
			if (datiControllo.getResult() != ControlloAttivitaServizioResult.OK)
				throw new ValidationException("Errore durante creazione nuova richiesta");

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.aggiornaRichiesta(movimento, servizioSelezionato.getIdServizio());


			//fine inserimento richiesta
			//
		} catch (Exception ex) {
			// errore nell'inserimento della richiesta
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.utenti.erroreInsRichOpac"));
			this.saveErrors(request, error);
		}
		return mapping.getInputForward();
	}

	private ActionMessages controlloCampiObbligatori(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		//
		InserimentoRichiestaForm currentForm = (InserimentoRichiestaForm) form;
		ActionMessages errors = new ActionMessages();
		//
		List<ServizioWebDatiRichiestiVO> listaCampi = currentForm.getMostraCampi();

		for (ServizioWebDatiRichiestiVO campo : listaCampi) {
			//
			if (campo.getCampoRichiesta()== 9){ //se volume
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getVolInter()) ) {
						// Volume campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.volumeObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 10){ //se numeroFascicolo
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getNumFasc()) ) {
						// Numero fascicolo campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.numeroFascicoloObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 11){ //se intervalloCopia
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getIntcopia()) ) {
						// Intervallo copia campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.intervalloCopiaObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 12){ //se noteUtente
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getNoteUte()) ) {
						// Note utente campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.noteUtenteObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 13){ //se spesaMax
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getSpesaMax()) ) {
						// Spesa massima sostenibile campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.spesaMaxObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 15){ //se dataLimiteInteresse
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDataLimInteresse()) ) {
						// Data limite di interesse campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.dataLimiteInteresseObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 16){ //se dataPrevRitiroDocumento
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDataPrevRitiroDocumento()) ) {
						// Data movimentazione campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.dataPrevistaRitiroDocumentoObbligatorio"));

						return errors;

					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 18){ //se dataDisponibDocumento
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDataDisponibDocumento()) ) {
						// Data movimentazione campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.dataDisponibilitaDocumentoObbligatorio"));

						return errors;

					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 22){ //se modErog
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getModErog()) ) {
						// Modalità di erogazione campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.modErogObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 24){ //se supporto
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getSupporto()) ) {
						// Supporto campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.supportoObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 25){ //se sala
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getSala()) ) {
						// Sala campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.salaObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 26){ //se posto
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getPosto()) ) {
						// Posto campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.postoObbligatorio"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 99){ //se annoPeriodico
				if (campo.isObbligatorio()){//se obbligatorio
					//
					if (!ValidazioneDati.isFilled(currentForm.getAnnoPeriodico()) ) {
						// Anno periodico campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.annoPeriodicoObbligatorio"));

						return errors;
					}

				}
			}
		}
		return errors;
		//
	}
	//
	private ActionMessages controlliFormali(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		//
		InserimentoRichiestaForm currentForm = (InserimentoRichiestaForm) form;
		ActionMessages errors = new ActionMessages();
		//
		//String elem; //Fare controlli
		List<ServizioWebDatiRichiestiVO> listaCampi = currentForm.getMostraCampi();

		for (ServizioWebDatiRichiestiVO campo : listaCampi) {
			//
			if (campo.getCampoRichiesta()== 12){ //se noteUte
				if (currentForm.getNoteUte().trim()!="") {
					if (currentForm.getNoteUte().trim().length() > 255) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.noteUtenteMax"));
					return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 15){ //se dataLimiteInteresse
				if (currentForm.getDataLimInteresse().trim()!=""){
					String apData = currentForm.getDataLimInteresse().trim();
					if (!DateUtil.isData(apData)) {
						// Formato data limite di interesse errato
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.dataLimiteInteresseErr"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 16){ //se dataPrevistaRitiroDocumento
				if (currentForm.getDataPrevRitiroDocumento().trim()!=""){
					String apData = currentForm.getDataPrevRitiroDocumento().trim();
					if (!DateUtil.isData(apData)) {
						// Formato data limite di interesse errato
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.dataPrevistaRitiroDocumentoErr"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 18){ //se dataDisponibDocumento
				String apData = currentForm.getDataDisponibDocumento().trim();
				if (apData !=""){
					if (!DateUtil.isData(apData)) {
						// Formato data mvimentazione errato
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.dataDisponibilitaDocumentoErr"));

						return errors;
					}
				}
				//
			}
			//
			if (campo.getCampoRichiesta()== 99){ //se annoPeriodico
				if (currentForm.getAnnoPeriodico().trim()!="") {
					if (currentForm.getAnnoPeriodico().trim().length() < 4) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.inserimentoRichiesta.annoPeriodicoMax"));
					return errors;
					}
				}
			}
		}
		return errors;
		//
	}
	//
	private boolean testCampo(String natura, String j) {
		String test = natura + j;
		//
		for (int i = 0; i < COPPIA_NATURA.length; i++)
			if (test.equals(COPPIA_NATURA[i]))
				return true;

		return false;
	}
	//
}
