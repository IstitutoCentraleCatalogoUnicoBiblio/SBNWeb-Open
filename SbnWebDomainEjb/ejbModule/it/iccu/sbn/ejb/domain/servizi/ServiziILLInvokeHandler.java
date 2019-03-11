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
package it.iccu.sbn.ejb.domain.servizi;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziIllDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_messaggio;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.validators.servizi.DatiRichiestaILLValidator;
import it.iccu.sbn.vo.validators.servizi.MessaggioValidator;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.TransactionIdType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;


public class ServiziILLInvokeHandler extends SerializableVO {

	private static final long serialVersionUID = 976989191876507730L;

	private static Logger log = Logger.getLogger(Servizi.class);

	private static final Map<String, Comparator<DatiRichiestaILLVO>> comparators = new HashMap<String, Comparator<DatiRichiestaILLVO>>();

	static {
		// Utenti locali
		// Biblioteche richiedenti e destinatarie, è impossibile vista che il dato non sta né nella sintetica né nel dettaglio?
		// Tipo di servizio (prestito o riproduzioni)
		// Date (una volta che ci siamo chiariti).
		comparators.put("01", DatiRichiestaILLVO.ORDINA_PER_COGNOME_NOME);
		comparators.put("02", ValidazioneDati.invertiComparatore(DatiRichiestaILLVO.ORDINA_PER_COGNOME_NOME));

		comparators.put("03", DatiRichiestaILLVO.ORDINA_PER_BIB_RICHIEDENTE);
		comparators.put("04", ValidazioneDati.invertiComparatore(DatiRichiestaILLVO.ORDINA_PER_BIB_RICHIEDENTE));

		comparators.put("05", DatiRichiestaILLVO.ORDINA_PER_BIB_FORNITRICE);
		comparators.put("06", ValidazioneDati.invertiComparatore(DatiRichiestaILLVO.ORDINA_PER_BIB_FORNITRICE));

		comparators.put("07", DatiRichiestaILLVO.ORDINA_PER_SERVIZIO);
		comparators.put("08", ValidazioneDati.invertiComparatore(DatiRichiestaILLVO.ORDINA_PER_SERVIZIO));

		comparators.put("09", DatiRichiestaILLVO.ORDINA_PER_DATA_AGG);
		comparators.put("10", ValidazioneDati.invertiComparatore(DatiRichiestaILLVO.ORDINA_PER_DATA_AGG));
	}

	static Predicate<AttivitaServizioVO> byCodAttivita(final String state) {
		return new Predicate<AttivitaServizioVO>() {
			public boolean test(AttivitaServizioVO att) {
				return state.equals(att.getCodAttivita());
			}
		};
	}

	private final ServiziBean ejb;

	public ServiziILLInvokeHandler(ServiziBean ejb) {
		this.ejb = ejb;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ValidationException, ApplicationException {
		try {
			switch (command.getCommand() ) {

			case SRV_ILL_XML_REQUEST:
				return executeIllRequest(command);

			case SRV_ILL_RICERCA_RICHIESTE:
				return getListaRichiesteILL(command);

			case SRV_ILL_AGGIORNA_DATI_RICHIESTA:
				return aggiornaDatiRichiestaILL(command);

			case SRV_ILL_DETTAGLIO_RICHIESTA:
				return getDettaglioRichiestaILL(command);

			case SRV_ILL_RIFIUTA_RICHIESTA:
				return rifiutaRichiestaILL(command);
/*
			case SRV_ILL_CONFIGURA_TIPO_SERVIZIO:
				return configuraTipoServizioILL(command);
*/
			case SRV_ILL_INSERISCI_PRENOTAZIONE:
				return inserisciPrenotazioneILL(command);

			case SRV_ILL_FILTRA_BIBLIOTECHE_AFFILIATE:
				return filtraBibliotecheAffiliateILL(command);

			case SRV_ILL_INOLTRA_AD_ALTRA_BIBLIOTECA:
				return inoltraRichiestaAdAltraBiblioteca(command);

			case SRV_ILL_AGGIORNA_BIBLIOTECA:
				return aggiornaBibliotecaILL(command);

			case SRV_ILL_INVIA_MESSAGGIO:
				return inviaMessaggio(command);

			case SRV_ILL_PONI_CONDIZIONE:
				return poniCondizione(command);

			case SRV_ILL_LISTA_BIBLIOTECHE_POLO_ILL:
				return listaBibliotechePoloILL(command);

			case SRV_ILL_AVANZA_RICHIESTA:
				return avanzaRichiestaILL(command);

			case SRV_ILL_ALLINEA_RICHIESTA:
				return allineaRichiestaILL(command);

			case SRV_ILL_CREA_DOC_DA_INVENTARIO:
				return creaDocumentoNonPossedutoDaInventario(command);

			default:
				return null;
			}

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch	(ApplicationException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			ejb.ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

	private void ordinaListaRichieste(List<DatiRichiestaILLVO> listaDaOrdinare, String tipoOrd) {

		if (!ValidazioneDati.isFilled(tipoOrd) || !ValidazioneDati.isFilled(listaDaOrdinare))
			return;

		Comparator<DatiRichiestaILLVO> c = comparators.get(tipoOrd);
		if (c == null) {
			log.warn("Tipo ordinamento richiesto non esistente: '" + tipoOrd + "'");
			return;
		}

		BaseVO.sortAndEnumerate(listaDaOrdinare, c);
	}

	private CommandResultVO executeIllRequest(CommandInvokeVO command) throws Exception {
		ILLAPDU output = null;
		Serializable param = command.getParams()[0];
		String targetIsil =  (String) command.getParams()[1];
		if (param instanceof String) {
			String xml = (String) param;
			output = ILLRequestBuilder.executeHandler(command.getTicket(), xml, targetIsil);
		} else {
			ILLAPDU apdu = (ILLAPDU) param;
			output = ILLRequestBuilder.executeHandler(command.getTicket(), apdu, targetIsil);
		}
		return CommandResultVO.build(command, output, null);
	}

	private CommandResultVO getListaRichiesteILL(CommandInvokeVO command) throws Exception {
		DatiRichiestaILLRicercaVO ricerca = (DatiRichiestaILLRicercaVO) command.getParams()[0];
		RichiesteServizioDAO dao = new RichiesteServizioDAO();

		String codUtente = ricerca.getCodUtente();
		if (isFilled(codUtente)) {
			UtenteBaseVO utente = ejb.getUtente(command.getTicket(), codUtente);
			if (utente != null)
				codUtente = utente.getCodUtente();
		}

		List<Tbl_dati_richiesta_ill> richieste = dao.getListaDatiRichiestaIll(
				ricerca.getTransactionId(), ricerca.getRequesterId(),
				ricerca.getResponderId(), ServiziUtil.espandiCodUtente(codUtente),
				ricerca.getRuolo(),	ricerca.getCurrentState(), ricerca.getCodStatoRic(),
				ricerca.getCodStatoMov(), 0 );
		ArrayList<DatiRichiestaILLVO> output = new ArrayList<DatiRichiestaILLVO>(ValidazioneDati.size(richieste));
		for (Tbl_dati_richiesta_ill dri : richieste) {
			output.add(ConversioneHibernateVO.toWeb().datiRichiestaILL(dri, dri.getRichiesta()));
		}

		ordinaListaRichieste(output, ricerca.getTipoOrdinamento());

		return CommandResultVO.build(command, output, null);
	}

	private CommandResultVO rifiutaRichiestaILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];

		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());
		//se la richiesta ha già generato un TransactionId va notificato il rifiuto all'altra biblioteca
		if (ValidazioneDati.isFilled(dati.getTransactionId()) &&
				!ValidazioneDati.in(stato,
						StatoIterRichiesta.F100_DEFINIZIONE_RICHIESTA_DA_UTENTE,
						StatoIterRichiesta.F111_DEFINIZIONE_RICHIESTA)) {

			MessaggioVO msg = ValidazioneDati.first(dati.getMessaggio());
			msg.validate(new MessaggioValidator());
			StatoIterRichiesta newState = StatoIterRichiesta.of(msg.getStato());
			dati.setCurrentState(newState.getISOCode());

			ILLAPDU response = null;
			switch (newState) {
			case F1221_CONFERMA_ANNULLAMENTO:
				response = ILLRequestBuilder.rispostaRichiestaDiAnnullamento(dati, msg, true);
				break;

			case F112A_PROPOSTA_DI_ANNULLAMENTO:
				response = ILLRequestBuilder.propostaAnnullamentoRichiesta(dati, msg);
				break;

			default:
				response = ILLRequestBuilder.rifiutaRichiesta(dati, msg);
				break;
			}

			ILLRequestBuilder.checkResponse(response);

		} else
			dati.setCurrentState(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode());

		CommandResultVO result = aggiornaDatiRichiestaILL(command);
		result.throwError();
		dati = (DatiRichiestaILLVO) result.getResult();

		//rifiuto mov locale (solo se effettiva)
		if (StatoIterRichiesta.of(dati.getCurrentState()) != StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO) {
			long cod_rich_serv = dati.getCod_rich_serv();
			if (ValidazioneDati.isFilled(cod_rich_serv))
				ejb.rifiutaRichieste(command.getTicket(), new Long[] { cod_rich_serv }, firmaUtente, false);
		}

		return CommandResultVO.build(command, dati);
	}

	private CommandResultVO getDettaglioRichiestaILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];

		DatiRichiestaILLVO datiRichiestaILL = getDettaglioRichiestaILL(ticket, dati.getIdRichiestaILL() );

		return CommandResultVO.build(command, datiRichiestaILL);
	}

	private DatiRichiestaILLVO getDettaglioRichiestaILL(String ticket, int idRichiestaILL) throws Exception {
		RichiesteServizioDAO dao = new RichiesteServizioDAO();
		Tbl_dati_richiesta_ill dri = dao.getDatiRichiestaIll(idRichiestaILL);

		DatiRichiestaILLVO datiRichiestaILL = ConversioneHibernateVO.toWeb().datiRichiestaILL(dri, dri.getRichiesta());

		if (datiRichiestaILL.isRichiedente()) {
			List<BibliotecaVO> biblioteche = datiRichiestaILL.getBibliotecheFornitrici();
			if (!isFilled(biblioteche)) {
				//dati bib.fornitrice
				BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
				richiesta.setCodiceAna(datiRichiestaILL.getResponderId());
				biblioteche = DomainEJBFactory.getInstance().getBiblioteca().cercaBiblioteche(ticket, richiesta);
				datiRichiestaILL.setBibliotecheFornitrici(biblioteche);
			}
		}

		return datiRichiestaILL;
	}

	private CommandResultVO aggiornaDatiRichiestaILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];

		dati.validate(new DatiRichiestaILLValidator());

		Timestamp now = DaoManager.now();
		RichiesteServizioDAO dao = new RichiesteServizioDAO();
		//check cambio stato su server ILL
		if (dati.isNuovo())
			dati.setUltimoCambioStato(now);
		else {
			Tbl_dati_richiesta_ill old = dao.getDatiRichiestaIll(dati.getIdRichiestaILL());
			String oldState = trimOrEmpty(old.getCd_stato());
			if (!ValidazioneDati.equals(oldState, dati.getCurrentState()))
				dati.setUltimoCambioStato(now);
		}

		//aggiorna doc
		DocumentoNonSbnVO doc = dati.getDocumento();
		if (doc != null)
			ejb.aggiornaDocumentoNonSbn(ticket, ValidazioneDati.asSingletonList(doc));

		//aggiorna dati ILL
		dati.setUteVar(firmaUtente);
		Tbl_dati_richiesta_ill dati_ill = ConversioneHibernateVO.toHibernate().datiRichiestaILL(null, dati);
		dati_ill = dao.aggiornaDatiRichiestaILL(dati_ill);

		//aggiorna messaggi
		for (MessaggioVO msg : dati.getMessaggio()) {
			if (msg.isNuovo()) {
				msg.setUteIns(firmaUtente);
				msg.setTsIns(now);
			}
			msg.setUteVar(firmaUtente);
			Tbl_messaggio messaggio = ConversioneHibernateVO.toHibernate().messaggio(msg);
			messaggio.setDati_richiesta(dati_ill);
			dao.aggiornaMessaggio(messaggio);
		}

		dati = ConversioneHibernateVO.toWeb().datiRichiestaILL(dati_ill, dati_ill.getRichiesta());
		return CommandResultVO.build(command, dati);
	}


	public DatiRichiestaILLVO aggiornaDatiRichiestaILL(String ticket,
			DatiRichiestaILLVO datiILL) throws Exception {
		CommandResultVO result = invoke(CommandInvokeVO.build(ticket, CommandType.SRV_ILL_AGGIORNA_DATI_RICHIESTA, datiILL));
		result.throwError();
		return (DatiRichiestaILLVO) result.getResult();
	}

/*
	private CommandResultVO configuraTipoServizioILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		TipoServizioVO tipoServizio = (TipoServizioVO) command.getParams()[0];

		if (!ejb.aggiornaTipoServizio(ticket, tipoServizio) )
			return CommandResultVO.build(command, false);

		IterServizioDAO dao = new IterServizioDAO();

		//almaviva5_20160127 servizi ILL
		//se si sta inserendo un nuovo servizio ILL questo va configurato in automatico
		String codTipoServizio = tipoServizio.getCodiceTipoServizio();
		int idTipoServizio = tipoServizio.getIdTipoServizio();

		CodTipoServizio cod = CodTipoServizio.get(CodiciProvider.cercaCodice(codTipoServizio,
				CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		ILLConfiguration conf = ILLConfiguration.getInstance();
		List<IterServizioVO> iterServizioCompleto = conf.buildIterServizioCompleto(tipoServizio.getCodBib(),
				codTipoServizio, cod.getRuolo() );

		try {
			//1. creazione iter
			for (IterServizioVO iter : iterServizioCompleto) {
				Tbl_iter_servizio iterServizio = ConversioneHibernateVO.toHibernate().iterServizio(iter, idTipoServizio, true);
				iterServizio.setUte_ins(firmaUtente);
				iterServizio.setUte_var(firmaUtente);

				dao.aggiornaIterServizio(idTipoServizio, iter.getProgrIter(),
						iterServizio, TipoAggiornamentoIter.MODIFICA);
			}

			//2. creazione supporti
			//3. creazione mod. erogazione

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return CommandResultVO.build(command, true);
	}
*/
	private CommandResultVO inserisciPrenotazioneILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		MovimentoVO mov = (MovimentoVO) command.getParams()[0];
		DatiRichiestaILLVO dati = mov.getDatiILL();

		mov.setFlTipoRec(RichiestaRecordType.FLAG_PRENOTAZIONE);
		mov.setCodStatoMov("P");	//prenotazione
		mov.setCodStatoRic("A");	//immessa
		dati.setCurrentState(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO.getISOCode());
		MovimentoVO p = ejb.aggiornaRichiesta(ticket, mov, mov.getIdServizio());

		//notifica alla bib. richiedente
		MessaggioVO msg = ValidazioneDati.first(dati.getMessaggio());
		msg.validate(new MessaggioValidator());

		ILLAPDU response = ILLRequestBuilder.prenotaDocumento(p, msg);
		ILLRequestBuilder.checkResponse(response);

		return CommandResultVO.build(command, p);
	}

	private CommandResultVO filtraBibliotecheAffiliateILL(CommandInvokeVO command) throws Exception {

		@SuppressWarnings("unchecked")
		ArrayList<BibliotecaVO> bibliotecheOpac = new ArrayList<BibliotecaVO>((List<BibliotecaVO>) command.getParams()[0]);

		CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(
				(String) command.getParams()[1],
				CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN));

		String isilBibRichiedente = (String) command.getParams()[2];

		Set<String> codiciAnagrafe = ValidazioneDati.listToMap(bibliotecheOpac, String.class, "cd_ana_biblioteca").keySet();
		ServiziIllDAO dao = new ServiziIllDAO();
		//si filtrano le sole biblioteche che aderiscono a ILL come fornitrici
		Map<String, Tbf_biblioteca> bibFornitrici = ValidazioneDati.listToMap(
				dao.getCodAnagrafeBibliotecheILLFornitrici(codiciAnagrafe),
				String.class, "cd_ana_biblioteca");

		//la biblioteca va riportata solamente se aderisce a ILL
		Iterator<BibliotecaVO> i = bibliotecheOpac.iterator();
		while (i.hasNext()) {
			BibliotecaVO bib = i.next();
			String isil = ValidazioneDati.fillRight(bib.getCd_ana_biblioteca(), ' ', Constants.ISIL_MAX_LENGTH);
			Tbf_biblioteca bibFrn = bibFornitrici.get(isil);
			if (bibFrn == null)
				i.remove();
			else
				//esclusione bib chiamante
				if (ValidazioneDati.equals(isilBibRichiedente, isil.trim() ))
					i.remove();
				else {
					//il tag 899 di opac non riporta la denominazione della bib, viene ricavata dall'anagrafe di polo
					bib.setNom_biblioteca(bibFrn.getNom_biblioteca());
					Tbl_biblioteca_ill tbl_bibIll = dao.getBibliotecaByIsil(ServiziUtil.formattaIsil(isil, Locale.getDefault().getCountry() ));
					if (tbl_bibIll == null)
						i.remove();
					else {
						//N = nazionale, I = internazionale, E = entrambi
						boolean ill_prestito = ValidazioneDati.in(tbl_bibIll.getFl_servprestito(), 'N', 'I', 'E');
						boolean ill_riproduzione = ValidazioneDati.in(tbl_bibIll.getFl_servdd(), 'N', 'I', 'E');

						//filtro su servizio selezionato
						if (ts != null) {
							if (ValidazioneDati.in(ts.getFamiglia(),
									ServiziConstant.FAMIGLIA_SRV_PRESTITO,
									ServiziConstant.FAMIGLIA_SRV_CONSULTAZIONE)
								&& !ill_prestito)
								i.remove();
							else
								if (ValidazioneDati.in(ts.getFamiglia(),
										ServiziConstant.FAMIGLIA_SRV_RIPRODUZIONE)
									&& !ill_riproduzione)
									i.remove();
						}
					}
				}
		}

		return CommandResultVO.build(command, bibliotecheOpac);
	}

	private CommandResultVO inoltraRichiestaAdAltraBiblioteca(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		MovimentoVO mov = (MovimentoVO) command.getParams()[0];
		BibliotecaVO bibForn = (BibliotecaVO) command.getParams()[1];

		ServiziIllDAO dao = new ServiziIllDAO();
		//si cancella la vecchia richiesta ILL
		DatiRichiestaILLVO dati = mov.getDatiILL();
		dao.cancellaRichiestaILL(dati.getIdRichiestaILL(), DaoManager.getFirmaUtente(ticket));

		//la bib scelta viene spostata in prima posizione
		List<BibliotecaVO> biblioteche = dati.getBibliotecheFornitrici();
		biblioteche.remove(bibForn);
		biblioteche.add(0, bibForn);

		//preparazione VO per inoltro
		dati.setTransactionId(0);	//sarà assegnato un nuovo numero
		dati.setIdRichiestaILL(0);	//nuova occorrenza sul DB
		dati.setResponderId(bibForn.getIsil());
		dati.setCurrentState(StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA.getISOCode());

		//notifica alla bib. fornitrice
		MessaggioVO msg = ValidazioneDati.first(dati.getMessaggio());
		msg.validate(new MessaggioValidator());

		dati.setDataInizio(msg.getDataMessaggio());
		dati.setDataFine(null);

		ILLAPDU response = ILLRequestBuilder.inoltraRichiestaAdAltraBiblioteca(mov, msg);
		ILLRequestBuilder.checkResponse(response);

		ILLRequestType requestType = ValidazioneDati.first(response.getILLRequest());
		if (requestType != null) {
			// cod. richiesta ill
			TransactionIdType tid = requestType.getTransactionId();
			if (tid != null)
				dati.setTransactionId(Long.valueOf(tid.getValue()));
		}

		mov.setCodStatoMov("A");
		mov.setCodStatoRic("G");
		MovimentoVO m = ejb.aggiornaRichiesta(ticket, mov, mov.getIdServizio());

		return CommandResultVO.build(command, m);
	}


	private CommandResultVO aggiornaBibliotecaILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		BibliotecaVO bib = (BibliotecaVO) command.getParams()[0];

		ServiziIllDAO dao = new ServiziIllDAO();

		String isil = ServiziUtil.formattaIsil(bib.getIsil(), Locale.getDefault().getCountry() );
		Tbl_biblioteca_ill tbl_bibIll = dao.getBibliotecaByIsil(isil);
		BibliotecaILLVO bibIll = ConversioneHibernateVO.toWeb().bibliotecaILL(tbl_bibIll, null);
		if (bibIll == null) {
			//non esiste
			bibIll = new BibliotecaILLVO();
			bibIll.setDescrizione(bib.getNom_biblioteca());
			bibIll.setTipoPrestito("A");
			bibIll.setTipoDocDelivery("A");
			bibIll.setRuolo("A");
		}

		bibIll.setIsil(isil);
		if (bibIll.isNuovo() ) {
			//nuova biblioteca, aggancio a anagrafica biblioteche
			BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
			richiesta.setCodiceAna(isil);
			List<BibliotecaVO> biblioteche = DomainEJBFactory.getInstance().getBiblioteca().cercaBiblioteche(ticket, richiesta);
			BibliotecaVO anagBib = ValidazioneDati.first(biblioteche);
			if (anagBib == null)
				throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_NON_TROVATA);

			bibIll.setBiblioteca(anagBib);
		}

		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		bibIll.setUteVar(firmaUtente);
		Tbl_biblioteca_ill biblioteca_ill = ConversioneHibernateVO.toHibernate().bibliotecaILL(null, bibIll);
		dao.aggiornaBiblioteca(biblioteca_ill);

		return CommandResultVO.build(command);
	}

	private CommandResultVO inviaMessaggio(CommandInvokeVO command) throws Exception {
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];

		MessaggioVO msg = ValidazioneDati.first(dati.getMessaggio());
		msg.validate(new MessaggioValidator());

		ILLAPDU response = ILLRequestBuilder.inviaMessaggio(dati, msg);
		ILLRequestBuilder.checkResponse(response);

		CommandResultVO result = aggiornaDatiRichiestaILL(command);
		result.throwError();
		dati = (DatiRichiestaILLVO) result.getResult();

		return CommandResultVO.build(command, dati);
	}

	private CommandResultVO poniCondizione(CommandInvokeVO command) throws Exception {
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];

		MessaggioVO msg = ValidazioneDati.first(dati.getMessaggio());
		msg.validate(new MessaggioValidator());

		ILLAPDU response = ILLRequestBuilder.condizioneRichiesta(dati, msg);
		ILLRequestBuilder.checkResponse(response);

		dati.setCurrentState(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA.getISOCode());
		CommandResultVO result = aggiornaDatiRichiestaILL(command);
		result.throwError();
		dati = (DatiRichiestaILLVO) result.getResult();

		return CommandResultVO.build(command, dati);
	}

	private CommandResultVO listaBibliotechePoloILL(CommandInvokeVO command) throws Exception {
		String codPolo = (String) command.getParams()[0];

		ArrayList<BibliotecaVO> bibliotecheILL = new ArrayList<BibliotecaVO>();

		List<BibliotecaVO> bibliotechePolo = DomainEJBFactory.getInstance().getBiblioteca().getListaBibliotechePolo(codPolo);

		String country = Locale.getDefault().getCountry();
		ServiziIllDAO dao = new ServiziIllDAO();
		for (BibliotecaVO bib : bibliotechePolo) {

			//verranno allineate solo le biblioteche attivate a ILL
			ParametriBibliotecaVO parametriBiblioteca = ejb.getParametriBiblioteca(command.getTicket(), codPolo, bib.getCod_bib());
			if (parametriBiblioteca == null || !parametriBiblioteca.isServiziILLAttivi())
				continue;

			String isil = ServiziUtil.formattaIsil(bib.getIsil(), country);
			Tbl_biblioteca_ill bibILL = dao.getBibliotecaByIsil(isil);
			if (bibILL != null) {	//la biblioteca aderisce a ILL
				bib.getBibliotecaILL().setRuolo(String.valueOf(bibILL.getFl_ruolo()));
				bibliotecheILL.add(bib);
			}

		}

		return CommandResultVO.build(command, bibliotecheILL);
	}

	private CommandResultVO avanzaRichiestaILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];
		final String newState = (String) command.getParams()[1];
		MessaggioVO msg = (MessaggioVO) command.getParams()[2];

		MovimentoVO mov = dati.getMovimento();
		if (mov != null && !mov.isNuovo() ) {
			//devo controllare anche il movimento locale
			List<AttivitaServizioVO> listaAttivitaSuccessive = ejb.getListaAttivitaSuccessive(ticket, mov.getCodPolo(), mov.getCodBibOperante(),
					mov.getCodTipoServ(), Integer.valueOf(mov.getProgrIter()), dati);

			Optional<AttivitaServizioVO> attSrv = Stream.of(listaAttivitaSuccessive).filter(byCodAttivita(newState)).findSingle();

			if (!attSrv.isPresent())
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_CONFIGURAZIONE_ITER);

			ControlloAttivitaServizio controllo = new ControlloAttivitaServizio(attSrv.get());
			List<String> messaggi = new ArrayList<String>();
			DatiControlloVO datiControllo = new DatiControlloVO(ticket, mov, OperatoreType.BIBLIOTECARIO, false, null);
			if (msg != null)
				datiControllo.setUltimoMessaggio(msg);

			ControlloAttivitaServizioResult result = ControlloAttivitaServizioResult.OK;
			boolean ok = controllo.controlloIter(datiControllo, messaggi, false, false);
			if (ok) {
				result = controllo.controlloDefault(datiControllo);
				if (result == ControlloAttivitaServizioResult.OK)
					result = controllo.post(datiControllo);
			}

			if (result == ControlloAttivitaServizioResult.OK) {
				mov = ejb.aggiornaRichiesta(ticket, mov, mov.getIdServizio() );
				return CommandResultVO.build(command, mov.getDatiILL() );
			}

			throw new ApplicationException(SbnErrorTypes.SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO);
		}

		dati = aggiornaDatiRichiestaILL(ticket, dati);

		return CommandResultVO.build(command, dati);
	}

	private CommandResultVO allineaRichiestaILL(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		DatiRichiestaILLVO dati = (DatiRichiestaILLVO) command.getParams()[0];

		ILLAPDU apdu = ILLRequestBuilder.statusQuery(dati.getTransactionId(), null, null, null, null);
		ILLRequestBuilder.checkResponse(apdu);

		String targetIsil = dati.isRichiedente() ? dati.getRequesterId() : dati.getResponderId();
		ILLRequestBuilder.executeHandler(ticket, apdu, targetIsil);

		(new ServiziIllDAO()).clearSession();

		dati = getDettaglioRichiestaILL(ticket, dati.getIdRichiestaILL());

		return CommandResultVO.build(command, dati);
	}

	private CommandResultVO creaDocumentoNonPossedutoDaInventario(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		String codBib = (String) command.getParams()[0];
		InventarioVO inv = (InventarioVO) command.getParams()[1];

		InventarioDettaglioVO id = DomainEJBFactory.getInstance().getInventario().getInventarioDettaglio(
				inv.getCodPolo(), inv.getCodBib(), inv.getCodSerie(), inv.getCodInvent(), Locale.getDefault(), ticket);

		DocumentoNonSbnVO doc = new DocumentoNonSbnVO();
		doc.setCodPolo(DaoManager.codPoloFromTicket(ticket));
		doc.setCodBib(codBib);
		doc.setFonte('O');
		doc.setTipo_doc_lett('D');
		doc.setTitolo(id.getTitIsbdTrattato());
		char natura = id.getNatura().charAt(0);
		doc.setNatura(natura);
		doc.setSegnatura(ServiziUtil.formattaSegnaturaCollocazione(id.getCollCodSez(), id.getCollCodLoc(),
				id.getCollSpecLoc(), id.getSeqColl(), natura, null));
		//chiave inventario
		doc.setCod_bib_inv(inv.getCodBib());
		doc.setCod_serie(inv.getCodSerie());
		doc.setCod_inven(inv.getCodInvent());

		doc.setTipoRecord(id.getTitTipoRecord());

		BibliotecaVO bibForn = DomainEJBFactory.getInstance().getBiblioteca().getBiblioteca(inv.getCodPolo(), inv.getCodBib());
		doc.setBiblioteche(ValidazioneDati.asSingletonList(bibForn));

		List<DocumentoNonSbnVO> documenti = ejb.aggiornaDocumentoNonSbn(ticket, ValidazioneDati.asSingletonList(doc));

		return CommandResultVO.build(command, ValidazioneDati.first(documenti));
	}

}
