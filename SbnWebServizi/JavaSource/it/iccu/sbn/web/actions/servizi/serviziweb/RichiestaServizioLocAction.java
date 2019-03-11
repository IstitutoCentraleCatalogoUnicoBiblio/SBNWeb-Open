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

import static it.iccu.sbn.util.Constants.Servizi.Movimenti.NUMBER_FORMAT_PREZZI;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.ListaMovimentiOpacForm;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.RichiestaServizioLocForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.erogazione.ListaMovimentiAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm.RichiestaListaMovimentiType;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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

public class RichiestaServizioLocAction extends ListaMovimentiAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.avanti", "avanti");
		map.put("servizi.bottone.indietro", "indietro");
		map.put("servizi.documento.insRich", "insRichiesta");
		map.put("servizi.bottone.ok", "ok");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages error = new ActionMessages();

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			if (request.getParameter("cambiaSupportoRicServLoc") != null)
				return this.cambiaSupportoRicServLoc(mapping, form, request, response);

			RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;

			Navigation.getInstance(request).addBookmark(Bookmark.Servizi.ModuloWeb.INSERIMENTO_RICHIESTA);
			//almaviva5_20170403 prenotazione posto
			PrenotazionePostoVO pp = (PrenotazionePostoVO) request.getAttribute(NavigazioneServizi.PRENOTAZIONE_POSTO);
			if (pp != null) {
				currentForm.getNuovaRichiesta().setPrenotazionePosto(new PrenotazionePostoDecorator(pp));
			}

			currentForm.setTotCopieRich(0);
			currentForm.setApintCopie("");

			HttpSession session = request.getSession();
			currentForm.setLettura(false);

			UtenteWeb utente = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
			currentForm.setCodUtente(utente.getUserId());
			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());

			currentForm.getInfoPwd().setUserName(utente.getUserId());

			currentForm.setBibsel((String) session.getAttribute(Constants.BIBLIO_SEL));
			currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));
			String codPolo = Navigation.getInstance(request).getPolo();
			//descrServizio
			RichiestaOpacVO ricOpac = (RichiestaOpacVO) session.getAttribute(Constants.RICHIESTA_OPAC);
			currentForm.setAutore(ricOpac.getAutore());
			currentForm.setTitolo(ricOpac.getTitolo());
			currentForm.setAnno(ricOpac.getAnno());
			currentForm.setServizio(ricOpac.getDescrServizio());
			currentForm.setCodBibInv(ricOpac.getCodBibOpac());
			currentForm.setCod_biblio(ricOpac.getBibIscr());

			String data = DateUtil.formattaData(DaoManager.now());
			currentForm.setDataRic(data);
			currentForm.setDataDisponibDocumento(data);

			{
				InfoDocumentoVO infoVO = (InfoDocumentoVO) request.getAttribute(Constants.INFO_DOCUMENTO);
				if (infoVO != null)
					currentForm.setInfoDocumentoVO(infoVO);
			}

			List<ServizioBibliotecaVO> listaSrvDoc = (List<ServizioBibliotecaVO>) request.getAttribute(Constants.SERVIZI_ATTIVI_DOCUMENTO);
			if (listaSrvDoc != null)
				currentForm.setLstCodiciServizio(listaSrvDoc);
			currentForm.setCodServNuovaRich(ricOpac.getServizio());

			currentForm.setUtenteVO(new UtenteBaseVO());
			currentForm.getUtenteVO().setCodBib(currentForm.getCod_biblio());//utente.getCodBib());
			currentForm.getUtenteVO().setCodUtente(utente.getUserId());
			currentForm.getMovRicerca().setCodPolo(codPolo);
			if (session.getAttribute(Constants.COD_BIBLIO) == null)
				currentForm.getMovRicerca().setCodBibOperante(ricOpac.getCodBibOpac());
			else
				currentForm.getMovRicerca().setCodBibOperante((String)session.getAttribute(Constants.COD_BIBLIO));

			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);

			//String ticket, String codPolo, String codBib, String codTipoServizio)
			// Caricamento dinamico dei campi presenti
			// sulla jsp "richiestaServizioLoc.jsp",
			// richiamando il metodo getServizioWebDatiRichiesti,
			// ritorna una lista di campi che dovranno
			// essere filtrati per visibilità e per natura del
			// documento
			String ticket = Navigation.getInstance(request).getUserTicket();

			String codBib = (String) session.getAttribute(Constants.COD_BIBLIO);//ricOpac.getCodBibOpac();
			String codTipoServizio = ricOpac.getCodServizio();//"CO"
			currentForm.setCodTipoServNuovaRich(codTipoServizio);
			//
			//String codSerieInv = ricOpac.getCodSerieOpac();
			//int codInvenInv = ricOpac.getCodInventOpac();
			//
			NavigationForward goBack = Navigation.getInstance(request).goBack(true);
			//
			//controllo se l'utente è abilitato ad inserire una richiesta
			ActionForward forward = verificaUtentePerRichiestaRemota(request, ricOpac);
			if (forward != null)
				return forward;

			currentForm.setMostraCampi(preparaListaCampi(form, ticket, codPolo, codBib, codTipoServizio, null, request));

			//almaviva5_20171006 servizi ill
			InfoDocumentoVO infoVO = currentForm.getInfoDocumentoVO();
			if (infoVO.isRichiestaSuInventario() && !ricOpac.getCodBibOpac().equals(codBib) ) {

				Navigation.getInstance(request).setTesto(".serviziweb.richiestaServizioLoc.RICHIESTA_ILL.testo");
				Navigation.getInstance(request).setDescrizioneX(".serviziweb.richiestaServizioLoc.RICHIESTA_ILL.descrizione");

				currentForm.setListaPaesi(CodiciProvider.getCodici(CodiciType.CODICE_PAESE));
				currentForm.setListaProvincia(CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE));

				//la richiesta sarà sempre su un doc. di tipo 'D'.
				DocumentoNonSbnVO doc = ServiziILLDelegate.getInstance(request)
						.creaDocumentoNonPossedutoDaInventario(codBib, infoVO.getInventarioTitoloVO());
				infoVO.setDocumentoNonSbnVO(doc);
				infoVO.setInventarioTitoloVO(null);

				//almaviva5_20160927 servizi ill
				TipoServizioVO tipoServizio = getTipoServizio(codPolo, codBib, codTipoServizio, request);
				if (!tipoServizio.isNuovo()) {
					List<TB_CODICI> tipiSupportoILL = ILLConfiguration2.getInstance().getListaSupportiILL(tipoServizio);
					tipiSupportoILL = CaricamentoCombo.cutFirst(tipiSupportoILL);
					currentForm.setTipiSupportoILL(tipiSupportoILL);
					List<TB_CODICI> modoErogazioneILL = ILLConfiguration2.getInstance().getListaModErogazioneILL(tipoServizio);
					modoErogazioneILL = CaricamentoCombo.cutFirst(modoErogazioneILL);
					currentForm.setModoErogazioneILL(modoErogazioneILL);
				}
			}

			//********************** inizio controlli nuova richiesta ****************************
			MovimentoVO movimento = null;
			ControlloAttivitaServizioResult checkAttivitaBase = null;

			DatiControlloVO datiControllo = controlliNuovaRichiesta(request, mapping, currentForm, getOperatore(request));
			checkAttivitaBase = datiControllo.getResult();
			movimento = datiControllo.getMovimento();
			currentForm.setDati(movimento.getDatiILL());

			switch (checkAttivitaBase) {
			case OK:
				break;

			case OK_NON_ANCORA_DISPONIBILE:
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				Timestamp tsRitiro = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkAttivitaBase.getMessage(), tsRitiro));
				Timestamp tsInizioPrev = movimento.getDataInizioPrev();
				movimento.setDataInizioPrev(tsRitiro.after(tsInizioPrev) ? tsRitiro : tsInizioPrev);
				break;

			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
				disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				Timestamp dataPrenotazione = disponibilitaVO.getDataPrenotazione();
					error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(checkAttivitaBase.getMessage(),
						disponibilitaVO.getMovimento().getCodTipoServ(),
						dataPrenotazione));
				addErrors(request, error);
				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				currentForm.setMostraCampi(preparaListaCampi(form, ticket, codPolo, codBib, codTipoServizio, RichiestaListaMovimentiType.PRENOTAZIONE, request));
				movimento.setDataInizioPrev(dataPrenotazione);
				currentForm.setNuovaRichiesta(movimento);
				return goBack;

			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
				//per documento impegnato da altro utente
				//se bib. ammette prenotazione prospetto la mappa di inserimento
				//altrimenti msg:Documento impegnato da altro utente, tornera
				//disponibile in data (data scadenza del servizio) FLAG_PRENOTAZIONE
				ControlloDisponibilitaVO disponibilitaVOAtt = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				//almaviva5_20151012 nuova gestione prenotazioni
				int prenotazioniPendenti = disponibilitaVOAtt.getPrenotazioniPendenti();
				//indice del messaggio per 0, una o più prenotazioni pendenti.
				int idMsg = prenotazioniPendenti == 0 ? 0 : prenotazioniPendenti > 1 ? 2 : 1;
				Timestamp dataPrenotazioneAtt = disponibilitaVOAtt.getDataPrenotazione();
					error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(checkAttivitaBase.getMessage(idMsg),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO, disponibilitaVOAtt.getMovimentoAttivo().getCodTipoServ()),
						dataPrenotazioneAtt,
						prenotazioniPendenti));
				addErrors(request, error);
				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				currentForm.setMostraCampi(preparaListaCampi(form, ticket, codPolo, codBib, codTipoServizio, RichiestaListaMovimentiType.PRENOTAZIONE, request));
				movimento.setDataInizioPrev(dataPrenotazioneAtt);
				currentForm.setNuovaRichiesta(movimento);
				//se bib. ammette prenotazione
				return mapping.getInputForward();//prenotazione

			case ERRORE_DOCUMENTO_NON_DISPONIBILE:
			case ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL:
				String codNoDisp = datiControllo.getCodNoDisp();
				String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
				LinkableTagUtils.addError(request, new ActionMessage(checkAttivitaBase.getMessage(), descr, datiControllo.getDataRedisp()));
				return goBack;

			default:
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(checkAttivitaBase.getMessage()));
				this.saveErrors(request, error);
				//
				return goBack;
			}

			//*********************fine controlli nuova richiesta**********************
			//

		    //caricamento combo Modalità Erogazione e Tipi Supporto
			List<SupportoBibliotecaVO> listaSupporti = (List<SupportoBibliotecaVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_SUPPORTI);
			currentForm.setTipiSupporto(listaSupporti);
			if (currentForm.getTipiSupporto().size() > 0)
				currentForm.getDetMov().setCodSupporto(currentForm.getTipiSupporto().get(0).getCodSupporto());

			List<TariffeModalitaErogazioneVO> listaTariffe = (List<TariffeModalitaErogazioneVO>) request.getAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE);
			currentForm.setModoErogazione(listaTariffe);
			currentForm.getDetMov().setCod_erog(currentForm.getModoErogazione().get(0).getCodErog());
			//La data ritiro del documento non può essere
			//superiore alla data della richiesta sommata a i gg. di prelazione
			//(che vengono configurati nei Parametri di biblioteca o nei Diritti
			//dei Servizi).
			//inizio caricamento Array di date
			if (!currentForm.getNuovaRichiesta().isWithPrenotazionePosto()) {
				ParametriBibliotecaVO paramBib = this.getParametriBiblioteca(currentForm.getMovRicerca().getCodPolo(), currentForm.getCodBibInv(), request);
				Timestamp dataPrelievo = DaoManager.now();
				List<ComboCodDescVO> dateDispPerPrelievo = new ArrayList<ComboCodDescVO>();
				for (int i = 0; i < paramBib.getGgValiditaPrelazione() + 1; i++) {
					//caricamento combo
					dateDispPerPrelievo.add(new ComboCodDescVO(DateUtil.formattaData(dataPrelievo), null));
					dataPrelievo = DateUtil.addDay(dataPrelievo, 1);
				}
				currentForm.setDataPrevRitDoc(dateDispPerPrelievo);

			} else {
				//la data inizio prevista è guidata dalla prenotazione
				pp = currentForm.getNuovaRichiesta().getPrenotazionePosto();
				String dataRitiro = DateUtil.formattaData(pp.getTs_inizio());
				currentForm.setDataPrevRitDoc(ValidazioneDati.asList(new ComboCodDescVO(dataRitiro, null)));
				currentForm.setDataPrevRitiroDocumento(dataRitiro);
			}

			//fine caricamento Array di date

			//controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio
			ControlloAttivitaServizio primoIter = null;
			ServizioBibliotecaVO servizioSelezionato = this.getServizio(currentForm.getLstCodiciServizio(),
					currentForm.getCodTipoServNuovaRich(), currentForm.getCodServNuovaRich());
			List<ControlloAttivitaServizio> listaAttivita = this.getListaAttivitaSuccessive(servizioSelezionato.getCodPolo(), servizioSelezionato.getCodBib(), servizioSelezionato.getCodTipoServ(), 0, null, request);
			primoIter = this.primoPassoIter(listaAttivita);
			DatiControlloVO controlliPrimoIter = super.eseguiControlli(request, movimento, primoIter, getOperatore(request), false, checkAttivitaBase);
			if (controlliPrimoIter.getResult() != ControlloAttivitaServizioResult.OK) {
				if (controlliPrimoIter.getResult() == ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE)
					return prenotazionePosto(mapping, currentForm, request, response);

				//la richiesta di servizio verrà scartata e si procede con una prenotazione posto
				if (controlliPrimoIter.getResult() == ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE_INV_DIGITALIZZATO) {
					return prenotazionePostoCatMed(mapping, currentForm, request, response, controlliPrimoIter.getCatMediazioneDigit() );
				}

				throw new ValidationException("Errore durante creazione nuova richiesta");
			}

			// almaviva5_20100729 #3857
			//prevForm.setFlg(false);
			return mapping.getInputForward();
		}
		catch (ValidationException ex) {
			//Errore di caricamento campi inserimento richiesta servizio locale
			resetToken(request);
			SbnErrorTypes errors = ex.getErrorCode();
			if (errors != SbnErrorTypes.ERROR_GENERIC) {
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errors.getErrorMessage() ));
				this.saveErrors(request, error);
			}
			NavigationForward goBack = Navigation.getInstance(request).goBack(true);
			return goBack;
		}
	}


	private ActionForward prenotazionePostoCatMed(ActionMapping mapping, RichiestaServizioLocForm currentForm,
			HttpServletRequest request, HttpServletResponse response, String catMediazioneDigit) throws Exception {

		Navigation navi = Navigation.getInstance(request);

		String codBib = (String) request.getSession().getAttribute(Constants.COD_BIBLIO);
		BibliotecaVO bib = DomainEJBFactory.getInstance().getBiblioteca().getBiblioteca(navi.getPolo(), codBib);
		ParametriServizi parametri = new ParametriServizi();
		parametri.put(ParamType.BIBLIOTECA, bib);

		RicercaGrigliaCalendarioVO grigliaCalendario = new RicercaGrigliaCalendarioVO();
		UtenteWeb utente = (UtenteWeb) request.getSession().getAttribute(Constants.UTENTE_WEB_KEY);

		UtenteBaseVO utenteBase = ServiziDelegate.getInstance(request).getUtente(utente.getUserId());
		if (utenteBase == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));
			return mapping.getInputForward();
		}
		grigliaCalendario.setUtente(utenteBase);

		grigliaCalendario.getCd_cat_mediazione().add(catMediazioneDigit);
		parametri.put(ParamType.GRIGLIA_CALENDARIO, grigliaCalendario);
		parametri.put(ParamType.MODALITA_GESTIONE_PRENOT_POSTO, ModalitaGestioneType.CREA);
		ParametriServizi.send(request, parametri);

		navi.purgeThis();
		return navi.goForward(mapping.findForward("prenotaPosto"));
	}

	private List<ServizioWebDatiRichiestiVO> preparaListaCampi(ActionForm form, String ticket,
			String codPolo, String codBib, String codTipoServizio, RichiestaListaMovimentiType type, HttpServletRequest request) throws Exception {

		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		InfoDocumentoVO infoVO = currentForm.getInfoDocumentoVO();
		CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(codTipoServizio,
				CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
		String natura;
		if (infoVO.isRichiestaSuInventario())
			natura = infoVO.getInventarioTitoloVO().getNatura();
		else
			natura = String.valueOf(infoVO.getDocumentoNonSbnVO().getNatura());

		//Metodo getServizioWebDatiRichiesti ritorna controlliNuovaRichiesta
		//una lista di campi da prospettare sulla jsp
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<ServizioWebDatiRichiestiVO> campi =
			factory.getGestioneServizi().getServizioWebDatiRichiesti(ticket, codPolo, codBib, codTipoServizio, natura);
		if (ValidazioneDati.isFilled(campi) ) {
			TipoServizioVO tipoServizio = getTipoServizio(codPolo, codBib, codTipoServizio, request);
			// imposto in lstDirittiUtente la descrizione del Codice Tipo Servizio
			// prendendola dalla tabella tb_codici
			List<ServizioWebDatiRichiestiVO> campiVisibili = new ArrayList<ServizioWebDatiRichiestiVO>();
			for (ServizioWebDatiRichiestiVO campo : campi) {
				if (type == RichiestaListaMovimentiType.PRENOTAZIONE && campo.getCampoRichiesta() != 15)
					continue;

				//servizi ILL: supporto e mod. erogazione sono campi gestiti da campi separati
				if (ts.isILL() && tipoServizio.isILL() && ValidazioneDati.in(campo.getCampoRichiesta(), 22, 24))
					continue;

				if (campo.isUtilizzato()){//se utilizzato
					if (testCampo(natura, String.valueOf(campo.getCampoRichiesta())))
						//se legame tra natura documento e cd_tabella è true
						campiVisibili.add(campo);
				}
			}
			return campiVisibili;
		}

		return null;
	}

	private ActionForward verificaUtentePerRichiestaRemota(HttpServletRequest request, RichiestaOpacVO ricOpac) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		UtenteWeb utente = (UtenteWeb) request.getSession().getAttribute(Constants.UTENTE_WEB_KEY);
		String codPolo = navi.getUtente().getCodPolo();

		// verifica se nei parametri di biblioteca è impostato il campo "E' ammesso l'inserimento della richiesta da parte dell'utente"
		// per impostarlo nel form e riutilizzarlo quando si deve o meno prospettare l'equivalente campo  tra le proprietà del Servizio
		// se impostato nei parametri di biblioteca non verranno prospettati tra le proprietà del servizio i campi sull'inserimento
		// della richiesta da parte dell'utente e quello relativo anche da WEB
		//
		ParametriBibliotecaVO param = this.getParametriBiblioteca(codPolo, ricOpac.getCodBibOpac(), request);

		//gestione flg per la gestione del bottone "Avanti"
		/*	almaviva5_20100729 #3857
		NavigationElement prev = Navigation.getInstance(request).getCache().getPreviousElement();
		ListaMovimentiOpacForm prevForm = (ListaMovimentiOpacForm) prev.getForm();
		prevForm.setFlg(true);
		*/
		if (param == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfiguraServizio"));
			return navi.goBack(true);
		}

		if (param.isAmmessoInserimentoUtente()) {

			if (param.isAncheDaRemoto() )
				return null; //posso inserire

			//ammesso, ma non da remoto
			if (utente.isRemoto()) {
				//la biblioteca non ammette richieste da remoto
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.noInsRichDaRemoto"));
				return navi.goBack(true);
			}
		}

		//la scelta é delegata al singolo servizio
		TipoServizioVO srv = this.getTipoServizio(codPolo, ricOpac.getCodBibOpac(), ricOpac.getCodServizio(), request);
		if (srv == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfiguraServizio"));
			return navi.goBack(true);
		}

		if (!srv.isIns_richieste_utente() ) {
			//la biblioteca non ammette richieste da remoto per il servizio.
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.noInsRichDaRemotoTipoServizio",
				CodiciProvider.cercaDescrizioneCodice(srv.getCodiceTipoServizio(), CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN) ));
			return navi.goBack(true);
		}

		//la biblioteca ammette richiesta da utente
		if (srv.isAnche_da_remoto() )
			return null; //posso inserire

		//ammesso, ma non da remoto
		if (utente.isRemoto() ) {
			//la biblioteca non ammette richieste da remoto per il servizio.
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.noInsRichDaRemotoTipoServizio",
				CodiciProvider.cercaDescrizioneCodice(srv.getCodiceTipoServizio(), CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN) ));
			return navi.goBack(true);
		}

		return null;
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
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.ListaServiziOpac"));
			return (mapping.getInputForward());
		}
	}
	//
	public ActionForward avanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		//
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			// inizio controlli
			ActionMessages err = this.controlliCampiObbligatori(request, currentForm);
			if (err.size() > 0) {
				this.saveErrors(request, err);
				return mapping.getInputForward();
			}

			ActionMessages errF = this.controlliFormali(request, currentForm);
			if (errF.size() > 0) {
				this.saveErrors(request, errF);
				return mapping.getInputForward();
			}

			//Inizio calcolo costo del servizio

			//currentForm.setTotCopieRich(currentForm.getIntcopia());


			// se presente il supporto:
			// costo del servizio = (costo unitario(supporto) * n.ro-pezzi + costo fisso (supporto) +
			//                       costo unitario(erogazione)* n.ro-pezzi + costo fisso (erogazione))

			// se non presente il supporto:
			// costo del servizio = costo unitario(erogazione)* n.ro-pezzi + costo fisso (erogazione)

			// NB: all’atto dell’inserimento della richiesta, il n.ro pezzi è un campo ammesso solo per le riproduzione,
			// negli altri casi è assunto dal sistema uguale a zero.

			// Meglio precisare:
			// a) il n. pezzi è ricavato (=conteggiato) dal sistema sulla base di quanto inserito dall’utente,
			// con una particolare sintassi, nel campo intervallo pagine, che è liberamente implementabile
			// nella configurazione del modulo di richiesta;
			// b)	il costo unitario può essere diverso da 0 per qualsiasi servizio,
			// che abbia una modalità di erogazione a pagamento. Es. Prestito con spedizione per posta:
			// Intendo dire che il costo della modalità di erogazione dipende dal supporto se il servizio prevede più supporti,
			// ciascuno con la propria modalità di erogazione, ma può esistere anche direttamente legata al servizio,
			// se questo non prevede ulteriori supporti.


			// effettuo anche il calcolo del costo servizio
			// considerando il supporto
			// e la modalità di erogazione (associata al supporto o al servizio se non presente il supporto)
			TariffeModalitaErogazioneVO tariffe = this.getModalitaErogazione(currentForm.getModoErogazione(), currentForm.getDetMov().getCod_erog());
			//
			if (!ValidazioneDati.strIsNull(currentForm.getDetMov().getCodSupporto())) {
				// se presente il supporto
				// dopo aver fatto opportuni controlli imposto l'intervallo copia
				// quando non siamo nella visualizzaione
				ActionMessages errG = this.impostaIntervalloCopia(currentForm, request, "Aggiorna");
				//
				if (errG.size() > 0) {
					//this.saveErrors(request, errF);
					return mapping.getInputForward();
				}
			}
			//
			DecimalFormat f = new DecimalFormat ("0.00");
			//
			double costoServizio;
			if (ValidazioneDati.strIsNull(currentForm.getDetMov().getCodSupporto())) {
			//if (!ValidazioneDati.isFilled(currentForm.getTipiSupporto()) ) {
				// se non presenti i supporti

				costoServizio =(
						( currentForm.getTotCopieRich() * tariffe.getCostoUnitarioDouble())
					    + tariffe.getTarBaseDouble() );
			}

			else {
				// se presenti i supporti

				// imposto il VO con il Supporto
				SupportoBibliotecaVO supporto = this.getSupporto(currentForm.getTipiSupporto(), currentForm.getDetMov().getCodSupporto());

				//currentForm.setTariffa
				costoServizio =(
				( currentForm.getTotCopieRich() * supporto.getImportoUnitarioDouble() )
				+ supporto.getCostoFissoDouble()
				+ ( currentForm.getTotCopieRich() * tariffe.getCostoUnitarioDouble() )
			    + tariffe.getTarBaseDouble() );
			}
			currentForm.setTariffa(f.format(costoServizio));
			//currentForm.setTariffa(costoServizio);

			if(currentForm.getTariffa()== null){
				//Tariffa non disponibile contattare la biblioteca
				ActionMessages error = new ActionMessages();
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.tariffa.err"));
				this.saveErrors(request, error);
				currentForm.setLettura(false);
				return mapping.getInputForward();

			}

			currentForm.setLettura(true);
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

	public ActionForward insRichiesta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		HttpSession session = request.getSession();
		Navigation navi = Navigation.getInstance(request);

		String codPolo = navi.getPolo();

		if (navi.isFromBar())
			return mapping.getInputForward();

		UtenteWeb utente = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
		if (!checkPassword(request, currentForm, utente))
			return mapping.getInputForward();

		try {
			// inizio controlli
			ActionMessages err = this.controlliCampiObbligatori(request, currentForm);
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
			currentForm.setUtenteVO(new UtenteBaseVO());
			currentForm.getUtenteVO().setCodBib(currentForm.getCod_biblio());//utente.getCodBib());
			currentForm.getUtenteVO().setCodUtente(utente.getUserId());
			currentForm.getMovRicerca().setCodPolo(codPolo);

			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
			//********************** inizio controlli nuova richiesta ****************************
			ActionMessages error = new ActionMessages();
			MovimentoVO nuovaRichiesta = currentForm.getNuovaRichiesta();
			Timestamp inizioPrev = new Timestamp(DateUtil.copiaOrario(nuovaRichiesta.getDataRichiesta(),
					DateUtil.toTimestamp(currentForm.getDataPrevRitiroDocumento())).getTime());
			nuovaRichiesta.setDataInizioPrev(inizioPrev);
			DatiControlloVO datiControllo = controlliNuovaRichiesta(request, mapping, currentForm, getOperatore(request));
			//
			ControlloAttivitaServizioResult checkAttivita = datiControllo.getResult();
			//
			MovimentoVO movimento = datiControllo.getMovimento();
			//
			//almaviva5_20121017 #5143
			String intcopia = currentForm.getIntcopia();
			if (ValidazioneDati.isFilled(intcopia)) {
				int numPezzi = ServiziUtil.getNumeroPaginePerRiproduzione(intcopia);
				if (numPezzi != ServiziConstant.NUM_PAGINE_ERROR)
					movimento.setNumPezzi(String.valueOf(numPezzi));
				else
					LinkableTagUtils.addError(request, new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE) );
			}

			switch (checkAttivita) {
			case OK:
			case OK_NON_ANCORA_DISPONIBILE:
				break;

			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
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
				//
				currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
				movimento.setDataInizioPrev(dataPrenotazione);
				//
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
			//*********************fine controllo nuova richiesta**********************
			//Inizio inserimento richiesta
			//controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio
			ControlloAttivitaServizio primoIter = null;
			ServizioBibliotecaVO servizioSelezionato = this.getServizio(currentForm.getLstCodiciServizio(),
					currentForm.getCodTipoServNuovaRich(), currentForm.getCodServNuovaRich());
			List<ControlloAttivitaServizio> listaAttivita = this.getListaAttivitaSuccessive(servizioSelezionato.getCodPolo(),
					servizioSelezionato.getCodBib(), servizioSelezionato.getCodTipoServ(), 0, null, request);
			primoIter = this.primoPassoIter(listaAttivita);
			DatiControlloVO controlliPrimoIter = super.eseguiControlli(request, movimento, primoIter, getOperatore(request), false, checkAttivita);
			if (controlliPrimoIter.getResult() != ControlloAttivitaServizioResult.OK) {
				if (controlliPrimoIter.getResult() == ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE)
					return prenotazionePosto(mapping, currentForm, request, response);

				throw new ValidationException("Errore durante creazione nuova richiesta");
			}

			//
			//*************Inizio valorizzazione campi del VO per l'inserimento della richiesta *****
			//

			if (currentForm.getApintCopie() != ""){
		    	movimento.setNoteUtente(currentForm.getApintCopie() + currentForm.getNoteUte());
		    }else{
		    	if (currentForm.getNoteUte()== null){
		    		currentForm.setNoteUte("");

		    	}
		    	movimento.setNoteUtente(currentForm.getNoteUte());

		    }
		    //modifica almaviva del 16/03/2010

//			Timestamp inizioPrev = new Timestamp(DateUtil.copiaOrario(movimento.getDataRichiesta(),
//					DateUtil.toTimestamp(currentForm.getDataPrevRitiroDocumento())).getTime());
//			movimento.setDataInizioPrev(inizioPrev);
			movimento.setDataMaxString(currentForm.getDataLimInteresse());
			//modifica almaviva del 23/03/2010
			movimento.setCostoServizio(currentForm.getTariffa());
			movimento.setNumVolume(currentForm.getVolInter());
			movimento.setCod_erog(currentForm.getDetMov().getCod_erog());
			movimento.setCodSupporto(currentForm.getDetMov().getCodSupporto());//supporto
			movimento.setPrezzoMax(currentForm.getSpesaMax());
			//
			//movimento.setNumPezzi(String.valueOf(currentForm.getTotCopieRich()));
			movimento.setAnnoPeriodico(currentForm.getAnnoPeriodico());
			movimento.setNumFascicolo(currentForm.getNumFasc());
			//
			//************Fine valorizzazione campi del VO ******************************************
			//
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			delegate.aggiornaRichiesta(movimento, servizioSelezionato.getIdServizio());
			//fine inserimento richiesta
			//invio msg.
			// almaviva5_20100729 #3858
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.utenti.InsRichOpacOk"));

			//almaviva5_20170607 messaggio supplementare da db
			this.aggiungiMessaggioPoloPersonalizzato(request);

			//gestione flg per la gestione del bottone "Avanti"
			NavigationElement prev = navi.getCache().getPreviousElement();
			ListaMovimentiOpacForm prevForm = (ListaMovimentiOpacForm) prev.getForm();

			prevForm.setFlg(true);
			return (mapping.findForward("listaMovimentiOpac"));
			//
		} catch (ValidationException e) {
			//almaviva5_20101018 #3858
			log.error("", e);
			currentForm.setLettura(false);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.utenti.erroreInsRichOpac"));

		} catch (ApplicationException e) {
			//almaviva5_20101018 #3858
			log.error("", e);
			currentForm.setLettura(false);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.utenti.erroreInsRichOpac"));

		} catch (Exception e) {
			// errore nell'inserimento della richiesta
			// almaviva5_20100729 #3858
			currentForm.setLettura(false);
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.utenti.erroreInsRichOpac"));
			log.error("", e);
		}
		return mapping.getInputForward();
	}

	protected void aggiungiMessaggioPoloPersonalizzato(HttpServletRequest request) throws Exception {
		List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SRV_EROGAZIONE);
		ModelloStampaVO msg = ValidazioneDati.first(modelli);
		if (msg != null && ValidazioneDati.isFilled(msg.getJrxml()) )
			LinkableTagUtils.addError(request, new ActionMessage(msg.getJrxml()));
	}

	//
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		HttpSession session = request.getSession();
		String codPolo = Navigation.getInstance(request).getPolo();

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			// inizio inserimento richiesta di prenotazione per il documento sbn da opac
			UtenteWeb utente = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
			currentForm.setUtenteVO(new UtenteBaseVO());
			currentForm.getUtenteVO().setCodBib(currentForm.getCod_biblio());//utente.getCodBib());
			currentForm.getUtenteVO().setCodUtente(utente.getUserId());
			currentForm.getMovRicerca().setCodPolo(codPolo);
			//currentForm.getMovRicerca().setCodBibOperante((String)session.getAttribute(Constants.COD_BIBLIO));
			currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);

			//********************** inizio controlli nuova richiesta ****************************
			ActionMessages error = new ActionMessages();
			DatiControlloVO datiControllo = controlliNuovaRichiesta(request, mapping, currentForm, getOperatore(request));
			ControlloAttivitaServizioResult checkAttivita = datiControllo.getResult();
			MovimentoVO movimento = datiControllo.getMovimento();
			//
			switch (checkAttivita) {
			case OK:
				break;

			case OK_NON_ANCORA_DISPONIBILE:
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
				Timestamp tsRitiro = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkAttivita.getMessage(), tsRitiro));
				Timestamp tsInizioPrev = movimento.getDataInizioPrev();
				movimento.setDataInizioPrev(tsRitiro.after(tsInizioPrev) ? tsRitiro : tsInizioPrev);
				break;

			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
				//inizio prenotazione
				if (!checkPassword(request, currentForm, utente))
					return mapping.getInputForward();

				// inizio controlli
				ActionMessages err = this.controlliCampiObbligatori(request, currentForm);
				if (err.size() > 0) {
					this.saveErrors(request, err);
					return mapping.getInputForward();
				}

				ActionMessages errF = this.controlliFormali(request, currentForm);
				if (errF.size() > 0) {
					this.saveErrors(request, errF);
					return mapping.getInputForward();
				}
				movimento.setDataMaxString(currentForm.getDataLimInteresse());
				MovimentoListaVO prenot = creaPrenotazione(request, mapping, currentForm);
				if (prenot == null)
					return mapping.getInputForward();
				//fine prenotazione
				//invio msg:"Richiesta di prenotazione inserita correttamente"
				error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.erogazione.prenotazioneInserita"));
				this.saveErrors(request, error);

				return (mapping.findForward("listaMovimentiOpac"));

			/*
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
				if (!checkPassword(request, currentForm, utente))
					return mapping.getInputForward();
				creaPrenotazione(request, mapping, currentForm);
				return mapping.getInputForward();
			*/

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
			//
		} catch (Exception ex) {
			// errore nell'inserimento della richiesta
			ActionMessages error = new ActionMessages();
			error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.utenti.errore.Prenotazione"));
			this.saveErrors(request, error);
		}
		return mapping.getInputForward();
	}
	//

	private boolean checkPassword(HttpServletRequest request, ActionForm form, UtenteWeb utente) {
		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		if (utente.isSIP2())
			return true;

		String pwd = currentForm.getInfoPwd().getPassword();
		if (!ValidazioneDati.isFilled(pwd) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.verifica.password"));
			return false;
		}
		if (!ValidazioneDati.equals(pwd, utente.getPassword()) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errore.servizi.verifica.password.diff"));
			return false;
		}

		return true; //ok
	}

	private ActionMessages controlliCampiObbligatori(HttpServletRequest request,
			ActionForm form) throws ValidationException {
		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		ActionMessages errors = new ActionMessages();

		//almaviva5_20120307 nessun controllo per SIP2
		UtenteWeb utente = (UtenteWeb) request.getSession().getAttribute(Constants.UTENTE_WEB_KEY);
		if (utente.isSIP2())
			return new ActionMessages();

		for (ServizioWebDatiRichiestiVO campo : currentForm.getMostraCampi() ) {
			switch (campo.getCampoRichiesta()) {
			case 18: //se dataDisponibDocumento
				if (campo.isObbligatorio()) {// se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDataDisponibDocumento()) ) {
						// Data disponibilità del documento campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.DataDisponibDocumento.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 16: //se dataPrevRitiroDocumento
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDataPrevRitiroDocumento()) ) {
						// Data prevista di ritiro documento campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.DataPrevRitiroDocumento.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 15: //se dataLimiteInteresse
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDataLimInteresse()) ) {
						// Data limite di interesse campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.dataLimiteInteresse.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 24: //se supporto
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDetMov().getCodSupporto()) ) {
						// Supporto campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.supporto.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 22: //se modErog
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getDetMov().getCod_erog()) ) {
						// Modalità di erogazione campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.modErogazione.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 99: //se annoPeriodico
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getAnnoPeriodico()) ) {
						// Anno periodico campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.annoPeriodico.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 9: //se volume
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getVolInter()) ) {
						// Volume campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.volume.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 10: //se numeroFascicolo
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getNumFasc()) ) {
						// Numero fascicolo campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.numeroFascicolo.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 11: //se intervalloCopia
				if (!ValidazioneDati.strIsNull(currentForm.getIntcopia())){
					currentForm.setApintCopie(currentForm.getIntcopia() + "))" );//intervalloCopia
				}

				if (campo.isObbligatorio()){//se obbligatorio
					if (ValidazioneDati.strIsNull(currentForm.getIntcopia())){
						// Intervallo copia campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.intervalloCopia.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 13: //se spesaMax
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getSpesaMax()) ) {
						// Spesa massima sostenibile campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.spesaMax.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 12: //se noteUtente
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getNoteUte()) ) {
						// Note utente campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.note.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 25: //se sala
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getSala()) ) {
						// Sala campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.sala.Obbligatorio"));
						return errors;
					}
				}

			case 26: //se posto
				if (campo.isObbligatorio()){//se obbligatorio
					if (!ValidazioneDati.isFilled(currentForm.getPosto()) ) {
						// Posto campo obbligatorio
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.posto.Obbligatorio"));
						return errors;
					}
				}
				break;

			case 27: //copyright
				if (campo.isObbligatorio()){//se obbligatorio
					if (!currentForm.isCopyright()) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.copyright.Obbligatorio"));
						return errors;
					}
				}
				break;
			}
		}
		return errors;
	}
	//
	private ActionMessages controlliFormali(HttpServletRequest request,
			ActionForm form) throws RemoteException {

		//almaviva5_20120307 nessun controllo per SIP2
		UtenteWeb utente = (UtenteWeb) request.getSession().getAttribute(Constants.UTENTE_WEB_KEY);
		if (utente.isSIP2())
			return new ActionMessages();

		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;
		ActionMessages errors = new ActionMessages();

		for (ServizioWebDatiRichiestiVO campo : currentForm.getMostraCampi()) {
			//private String dataDisponibDocumento;//18 private String dataMov;
			//private String dataPrevRitiroDocumento;//16
			if (campo.getCampoRichiesta()== 18){ //se dataDisponibDocumento
				String apData = currentForm.getDataDisponibDocumento().trim();
				if (apData !=""){
					if (!DateUtil.isData(apData)) {
						// Formato data disponibilità del documento campo obbligatorio errato
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.dataDisponibDocumento.err"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 16){ //se dataPrevRitiroDocumento
				String apData = currentForm.getDataPrevRitiroDocumento();
				if (apData !=""){
					if (!DateUtil.isData(apData)) {
						// Formato data disponibilità del documento campo obbligatorio errato
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.dataPrevRitiroDocumento.err"));

						return errors;
					}
				}
			}
		    /*
			if (campo.getCampoRichiesta()== 16){ //se dataPrevRitiroDocumento
				//
				//La data ritiro del documento non può essere
				//superiore alla data della richiesta sommata a i gg. di prelazione
				//(che vengono configurati nei Parametri di biblioteca o nei Diritti
				//dei Servizi).
				//
				ParametriBibliotecaVO parametri = this.getParametriBiblioteca(currentForm.getMovRicerca().getCodPolo(), currentForm.getCodBibInv(), request);
		 		Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				Timestamp OggiPrel = (DaoManager.now());
				if (parametri.getGgValiditaPrelazione() > 0) {
	                calendar.add(Calendar.DATE, parametri.getGgValiditaPrelazione());
					OggiPrel = (new Timestamp(calendar.getTimeInMillis()));

				}
				//
				String DataPrevRitDoc = currentForm.getDataPrevRitiroDocumento();
				Date dataRitiro = DateUtil.toDate(DataPrevRitDoc);
				//
				if (dataRitiro.after(OggiPrel)) {
					// Data prevista ritiro documento: superiore alla data
					// della richiesta più i giorni di prelazione.
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.dataPrevRitiroDocSupPrel"));

					return errors;
				}
				//
				Date apData = DateUtil.toDate(currentForm.getDataRic().trim());
				if (dataRitiro.before(apData)) {
					// Data prevista ritiro documento: minore della data
					// richiesta.
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.dataPrevRitiroDocMinRich"));

					return errors;
				}
			}
			*/
			//
			if (campo.getCampoRichiesta()== 15){ //se dataLimiteInteresse
				if (currentForm.getDataLimInteresse().trim()!=""){
					String apData = currentForm.getDataLimInteresse().trim();
					if (!DateUtil.isData(apData)) {
						// Formato data limite di interesse errato
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.dataLimiteInteresse.err"));

						return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 99){ //se annoPeriodico
				if (currentForm.getAnnoPeriodico().trim()!="") {
					if (currentForm.getAnnoPeriodico().trim().length() < 4) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.campo.annoPer.max"));

					return errors;
					}
				}
			}
			//
			if (campo.getCampoRichiesta()== 12){ //se noteUte
				if (currentForm.getNoteUte().trim()!="") {
					if (currentForm.getNoteUte().trim().length() > 255) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.campo.note.max"));

					return errors;
					}
				}
			}
		}
		return errors;
	}

	private boolean testCampo(String natura, String j) {
		String test = natura + j;

		for (int i = 0; i < ServiziConstant.COPPIA_NATURA.length; i++)
			if (ValidazioneDati.equals(test, ServiziConstant.COPPIA_NATURA[i]))
				return true;

		return false;
	}

	private void loadCollections(RichiestaServizioLocForm currentForm, HttpServletRequest request) throws Exception  {
		//
		HttpSession session = request.getSession();
		//
		RichiestaOpacVO ricOpac = (RichiestaOpacVO) session.getAttribute(Constants.RICHIESTA_OPAC);
		//
		String codPolo = Navigation.getInstance(request).getPolo();
		String codTipoServizio = ricOpac.getCodServizio();//"CO"
		MovimentoListaVO detMov = currentForm.getDetMov();
		List<TariffeModalitaErogazioneVO> listaTariffe = this
				.getTariffeModalitaErogazione(codPolo, currentForm.getCod_biblio(), codTipoServizio, request);
		if (listaTariffe != null)
			currentForm.setModoErogazione(listaTariffe);
		else
			currentForm.setModoErogazione(new ArrayList<TariffeModalitaErogazioneVO>());

		try {
			// Ricerca della Categoria di Riproduzione del documento per estrarre
			// i supporti ad essa associati
			// viene utilizzata la tabella "tb_codici" relativamente al codice LSUP
			List<TB_CODICI> listaRelazioni = CodiciProvider.getCodiciCross(CodiciType.CODICE_TIPI_RIPRODUZIONE_CODICE_SUPPORTO, detMov.getCat_riproduzione(), true);
			//
			List<SupportoBibliotecaVO> listaSupporti = new ArrayList();
			SupportoBibliotecaVO appoSupporto = new SupportoBibliotecaVO();
			//
			if (ValidazioneDati.isFilled(listaRelazioni) ) {
				Iterator<TB_CODICI> iterator = listaRelazioni.iterator();
				while (iterator.hasNext()) {
					appoSupporto = this.getSupportoBiblioteca(
							codPolo,  currentForm.getCod_biblio(), iterator.next().getCd_tabella().trim(), request);
					if (appoSupporto != null) {
						listaSupporti.add(appoSupporto);
					}
				}
			}
			//
			if (listaSupporti != null) {
				listaSupporti.add(0, new SupportoBibliotecaVO());
				currentForm.setTipiSupporto(listaSupporti);
			} else
				currentForm.setTipiSupporto(new ArrayList<SupportoBibliotecaVO>());

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	private ActionForward cambiaSupportoRicServLoc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			//currentForm.setConferma(false);

			MovimentoVO detMov = currentForm.getDetMov();

			// ult_supp è "S"

			// La lista dei supporti, estratta a partire dalla Categoria di Riproduzione,
			// è stata precedentemente salvata su TipiSupporto presente sul Form
			// Il supporto selezionato è presente su currentForm.getDetMov().getCodSupporto()

			// estraggo le modalità di erogazione associate al supporto selezionato
			List<SupportiModalitaErogazioneVO> listaSupportiModalitaErogazione = this.getSupportiModalitaErogazione(currentForm.getMovRicerca().getCodPolo(),
					currentForm.getMovRicerca().getCodBibOperante(), currentForm.getDetMov().getCodSupporto(), request);
			if (ValidazioneDati.isFilled(listaSupportiModalitaErogazione) ) {
				List<TariffeModalitaErogazioneVO> listaTariffe = new ArrayList<TariffeModalitaErogazioneVO>();
				Iterator<SupportiModalitaErogazioneVO> iterator_1 = listaSupportiModalitaErogazione.iterator();
				while (iterator_1.hasNext()) {
					TariffeModalitaErogazioneVO tariffeModalitaErogazioneVO = new TariffeModalitaErogazioneVO();
					ClonePool.copyCommonProperties(tariffeModalitaErogazioneVO,iterator_1.next());
					listaTariffe.add(tariffeModalitaErogazioneVO);
				}
				currentForm.setModoErogazione(listaTariffe);
				// imposto la prima modalità di erogazione
				detMov.setCod_erog(listaTariffe.get(0).getCodErog());
			} else {

				// ERRORE !!! Non ci sono modalità di erogazione
				// associate al supporto selezionato
				currentForm.setModoErogazione(new ArrayList<TariffeModalitaErogazioneVO>());
				currentForm.setFlgErrNuovaRichiesta(true);
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonAssociateAlSupporto"));
				this.saveErrors(request, errors);
				saveToken(request);
				return mapping.getInputForward();
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			e.printStackTrace();
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}
	//
	private ActionMessages impostaIntervalloCopia(RichiestaServizioLocForm currentForm, HttpServletRequest request, String flgModifica) throws ValidationException {
	//private ActionMessages impostaIntervalloCopia(RichiestaServizioLocForm dettForm, HttpServletRequest request, String flgModifica)
	//throws ValidationException, NumberFormatException, ParseException {
	ActionMessages errors = new ActionMessages();
	//
	MovimentoListaVO mov = currentForm.getDetMov();
	MovimentoListaVO movSalvato = currentForm.getMovimentoSalvato();
	//
	if (!ValidazioneDati.strIsNull(currentForm.getIntcopia())){
		//se l'intervallo copia è impostato
		//riporto nella String pagine il valore dell'intervallo pagine
		String pagine = currentForm.getIntcopia();// mov.getIntervalloCopia();
		//verifico che nella stringa non siano presenti
		//più numeri contenuti tra uno più spazi
		//
		if (java.util.regex.Pattern.matches(".*[0-9]+\\s+[0-9]+.*", pagine)) {
			//intervallo copia non impostato correttamente: impostare numeri di pagina
			//o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
			this.saveErrors(request, errors);
			return errors;
		}
		// inizializzo il campo che conterrà
		// il totale delle pagine relative all'intervallo copia
		int totalePagine = 0;
		// elimino tutti gli spazi presenti nella stringa
		pagine = pagine.replaceAll("\\s+", "");
		// riporto il valore normalizzato sul formù
		currentForm.setIntcopia(pagine);
		currentForm.setApintCopie(pagine);

		if (!ValidazioneDati.strIsNumeric(pagine.substring(pagine.length() - 1 ,pagine.length()))) {
			// se l'ultimo carattere della stringa non è un numero
			// intervallo copia non impostato correttamente: impostare numeri di pagina
			// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
			this.saveErrors(request, errors);
			return errors;
		}
		// creo uuna lista contenente tutti gli intervalli
		// impostati nel campo pagina (es: intervallo "5-6", intervallo "8", intervallo "15-19")
		List<String> intervalli = Arrays.asList(pagine.split(","));
		//
		if (intervalli.isEmpty()) {
			// intervallo copia non impostato correttamente: impostare numeri di pagina
			// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
			this.saveErrors(request, errors);
			return errors;
		}
		Iterator<String> iterator_1 = intervalli.iterator();
		String[] pagineIntervalli = null;
		int pagina_1;
		int pagina_2;
		String appoStr;
		while (iterator_1.hasNext()) {
			// per ogni intervallo creo un altro array contenente
			// le due pagine, se presenti
			// o la singola pagina dell'intervallo
			appoStr = iterator_1.next();
			pagineIntervalli = appoStr.split("-");
			if (pagineIntervalli.length == 0) {
				// intervallo copia non impoostato correttamente: impostare numeri di pagina
				// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
				this.saveErrors(request, errors);
				return errors;
			}
			if (pagineIntervalli.length > 2) {
				// intervallo copia non impoostato correttamente: impostare numeri di pagina
				// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
				this.saveErrors(request, errors);
				return errors;
			}
			if (pagineIntervalli.length == 1) {
				if (ValidazioneDati.notEmpty(pagineIntervalli[0]) &&
					ValidazioneDati.strIsNumeric(pagineIntervalli[0])) {
					// se la pagina è impostata ed è numerica
					pagina_1 = Integer.valueOf(pagineIntervalli[0]);
					totalePagine = totalePagine + 1;
				} else {
					// intervallo copia non impoostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
					this.saveErrors(request, errors);
					return errors;
				}
			}
			if (pagineIntervalli.length == 2) {
				pagina_1 = 0;
				pagina_2 = 0;
				if (ValidazioneDati.notEmpty(pagineIntervalli[0]) &&
					ValidazioneDati.strIsNumeric(pagineIntervalli[0])) {
					pagina_1 = Integer.valueOf(pagineIntervalli[0]);
				} else {
					// intervallo copia non impoostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
					this.saveErrors(request, errors);
					return errors;
				}
				if (ValidazioneDati.notEmpty(pagineIntervalli[1]) &&
						ValidazioneDati.strIsNumeric(pagineIntervalli[1])) {
						pagina_2 = Integer.valueOf(pagineIntervalli[1]);
					} else {
						// intervallo copia non impoostato correttamente: impostare numeri di pagina
						// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
						this.saveErrors(request, errors);
						return errors;
					}
				if (pagina_2 < pagina_1) {
					// intervallo copia non impoostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.intervalloCopiaErrato"));
					this.saveErrors(request, errors);
					return errors;
				}
				totalePagine = totalePagine + (pagina_2 - pagina_1 + 1);
			}
		}
		// se non si sono verificati errori
		// imposto il totale pagine nel numero pezzi
		mov.setNumPezzi(String.valueOf(totalePagine));
		currentForm.setTotCopieRich(totalePagine);

		// imposto l'intervallo copia normalizzato (senza gli eventuali spazi) che sarà riportato all'inizio della nota utente
		String inizioNoteUtente = pagine + "))";

		// posizStringaParentesi è la posizione dove si trova la stringa "))"
		int posizStringaParentesi = currentForm.getNoteUte().indexOf("))");
		if (posizStringaParentesi < 0) {
			// se non trovo la stringa "))" nella nota dell'utente
			// salvo in un campo della form il vero valore delle note utente
			// comprensivo della copia intervallo da riportare sul DB
			currentForm.setSalvaNoteUtente(inizioNoteUtente + mov.getNoteUtente());
			// non effettuo operazioni sulle note utente
		} else {
			// se ho trovato la stringa "))" nella nota utente
			// salvo il testo trovato dopo la stringa "))"
			String appoNoteUtente = mov.getNoteUtente().substring(posizStringaParentesi + 2);
			// salvo in un campo della form il vero valore delle note utente
			// comprensivo della copia intervallo da riportare sul DB
			currentForm.setSalvaNoteUtente(inizioNoteUtente + appoNoteUtente);
			// riporto il testo trovato dopo la stringa "))"
			mov.setNoteUtente(appoNoteUtente);
		}
	} else {// in caso contrario l'intervallo copia non è impostato
		if (flgModifica.equals("Visualizza")) {
			// se siamo in visualizzazione
			if (!ValidazioneDati.strIsNull(mov.getNoteUtente())) {
				//se presenti le note utente
				//devo riportare gli intervalli copia a partire dalle note utente
				//posizStringaParentesi è la posizione dove si trova la stringa "))"
				int posizStringaParentesi = mov.getNoteUtente().indexOf("))");
				if (posizStringaParentesi < 0) {
					// se non trovo la stringa "))" nella nota dell'utente
					// e non ho intervalli da impostare
					// non faccio niente
				} else {
					// se ho trovato la stringa "))" nella nota utente
					// trovo, prima della stringa "))" l'intervallo copia
					// imposto copia intervallo
					currentForm.setIntcopia(mov.getNoteUtente().substring(0, posizStringaParentesi + 1 - 1));
					// e lo salvo in intervalloCopia del movimento salvato
					movSalvato.setIntervalloCopia(currentForm.getIntcopia());
					currentForm.setApintCopie(currentForm.getIntcopia());
					// salvo in un campo della form il vero valore delle note utente
					// comprensivo della copia intervallo da riportare sul DB
					currentForm.setSalvaNoteUtente(mov.getNoteUtente());
					// salvo il testo trovato dopo la stringa "))"
					String appoNoteUtente = mov.getNoteUtente().substring(posizStringaParentesi + 2);
					// riporto sulle note utente solo il testo trovato dopo la stringa "))"
					mov.setNoteUtente(appoNoteUtente);
					// e lo salvo in noteUtente del movimento salvato
					movSalvato.setNoteUtente(mov.getNoteUtente());
				}
			}
		} else {// se siamo in aggiornamento
			if (!ValidazioneDati.strIsNull(mov.getNoteUtente())) {
				// se presenti le note utente
				// devo impostare correttamente le note utente (senza intervalli copia)
				// posizStringaParentesi è la posizione dove si trova la stringa "))"
				int posizStringaParentesi = mov.getNoteUtente().indexOf("))");
				if (posizStringaParentesi >= 0) {
					// salvo il testo trovato dopo la stringa "))"
					String appoNoteUtente = mov.getNoteUtente().substring(posizStringaParentesi + 2);
					// riporto sulle note utente solo il testo trovato dopo la stringa "))"
					mov.setNoteUtente(appoNoteUtente);
					// salvo in un campo della form il vero valore delle note utente
					// (in questo caso uguale alle note utente perchè senza intervallo copia)
					currentForm.setSalvaNoteUtente(mov.getNoteUtente());
				} else {// se già in note utente non c'è l'intervallo copia
					// note utente sono già impostate correttamente
					// salvo in un campo della form il vero valore delle note utente
					// (in questo caso uguale alle note utente perchè senza intervallo copia)
					currentForm.setSalvaNoteUtente(mov.getNoteUtente());
				}
			}
		}
	}
	return errors;
	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			TipoOperazione attivita = TipoOperazione.valueOf(idCheck);
			RichiestaServizioLocForm currentForm = (RichiestaServizioLocForm) form;

			switch (attivita) {
			case TARIFFA:
				String tariffa = currentForm.getTariffa();
				if (!ValidazioneDati.isFilled(tariffa))
					return false;

				//la tariffa/costo (campo 14) viene mostrata sempre se > 0
				List<ServizioWebDatiRichiestiVO> campi = currentForm.getMostraCampi();
				//cerco nella configurazione del tipo servizio
				if (ValidazioneDati.isFilled(campi) )
					for (ServizioWebDatiRichiestiVO campo : campi)
						if (campo.getCampoRichiesta() == 14)
							return true;

				//se non previsto dalla conf. mostro il costo solo se > 0

				double tar = ValidazioneDati.getDoubleFromString(tariffa, NUMBER_FORMAT_PREZZI, getLocale(request));
				return (tar > 0);

			case PASSWORD:
				return ValidazioneDati.isFilled(currentForm.getTariffa()) ||
					ValidazioneDati.equals(currentForm.getRichiesta(), RichiestaListaMovimentiType.PRENOTAZIONE);

			case DOC_ALTRA_BIBLIOTECA: {
				return currentForm.getNuovaRichiesta().isRichiedenteRichiestaILL();
			}

			default:
				return super.checkAttivita(request, form, idCheck);
			}

		} catch (Exception ex) {
			return false;
		}

	}


}
