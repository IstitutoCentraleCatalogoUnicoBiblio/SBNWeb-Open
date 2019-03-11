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
package it.iccu.sbn.ejb.domain.semantica.soggetti;

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.iccu.sbn.SbnMarcFactory.util.semantica.ReticoloSoggetti;
import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineLegameTitoloSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineModificaSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.FondiSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.custom.semantica.UserMessage;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class SoggettiBean extends TicketChecker implements Soggetti {

	private static final long serialVersionUID = -1757544682915157733L;

	static Logger log = Logger.getLogger(Soggetti.class);

	static final ThreadLocal<List<UserMessage>> userMessages = new ThreadLocal<List<UserMessage>>() {
		@Override
		protected List<UserMessage> initialValue() {
			return new ArrayList<UserMessage>();
		}
	};

	static Reference<Semantica> semantica = new Reference<Semantica>() {
		@Override
		protected Semantica init() throws Exception {
			return DomainEJBFactory.getInstance().getSemantica();
		}};

	static Reference<Profiler> profiler = new Reference<Profiler>() {
		@Override
		protected Profiler init() throws Exception {
			return DomainEJBFactory.getInstance().getProfiler();
		}};

	static Reference<Interrogazione> interrogazione = new Reference<Interrogazione>() {
		@Override
		protected Interrogazione init() throws Exception {
			return DomainEJBFactory.getInstance().getInterrogazione();
		}};

	static Predicate<DatiCondivisioneSoggettoVO> testCidIndiceCondiviso(final String cidIndice) {
		return new Predicate<DatiCondivisioneSoggettoVO>() {
			public boolean test(DatiCondivisioneSoggettoVO dcs) {
				return dcs != null && ValidazioneDati.equals(dcs.getCidIndice(), cidIndice);
			}
		};
	}

	SessionContext ctx;

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
	}

	public List<UserMessage> consumeMessages() {
		List<UserMessage> cached = userMessages.get();
		userMessages.remove();
		return cached;
	}

	public AnaliticaSoggettoVO importaSoggettoDaIndice(String ticket, DettaglioSoggettoVO soggettoIndice) throws SbnBaseException {
		checkTicket(ticket);
		List<UserMessage> messages = userMessages.get();
		try {
			AnaliticaSoggettoVO reticoloSoggettoIndice = caricaReticoloSoggetto(ticket, false, soggettoIndice.getCid());
			if (reticoloSoggettoIndice == null)
				return null;

			CreaVariaSoggettoVO richiesta = new CreaVariaSoggettoVO(soggettoIndice);

			richiesta.setLivelloPolo(true); // creazione in polo

			CreaVariaSoggettoVO soggettoImportato = semantica.get().creaSoggetto(richiesta, ticket);

			if (soggettoImportato.isEsitoOk() ) {
				//il soggetto di indice è stato importato con lo stesso cid
				AnaliticaSoggettoVO analiticaSoggettoPolo = caricaReticoloSoggetto(ticket, true, soggettoImportato.getCid() );
				attivaCondivisioneSoggetto(ticket, analiticaSoggettoPolo.getDettaglio(), soggettoIndice.getCid(), OrigineSoggetto.INDICE);
				messages.add(new UserMessage("errors.gestioneSemantica.operOkIns", soggettoImportato.getCid()));
				return analiticaSoggettoPolo;
			}

			log.warn(String.format("importaSoggettoDaIndice(): creazione di '%s' in polo fallita: '%s'", soggettoIndice.getCid(),
					soggettoImportato.getTestoEsito()));
			// esiste un soggetto in polo con lo stesso testo
			if (ValidazioneDati.in(soggettoImportato.getEsitoType(),
					SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA,
					SbnMarcEsitoType.TROVATI_SIMILI) ) {
				ElementoSinteticaSoggettoVO simile =
					ReticoloSoggetti.trovaSimileConTestoEsatto(soggettoIndice.getTesto(), soggettoImportato.getListaSimili() );
				if (simile == null) {
					messages.add(new UserMessage("errors.gestioneSemantica.incongruenzaSogg"));
					return null;
				}
				log.debug("importaSoggettoDaIndice(): trovato simile in polo: " + simile);
				//viene tentata la creazione del cid di polo in indice per creare un link tra i due soggetti
				AnaliticaSoggettoVO reticoloSoggettoSimile = caricaReticoloSoggetto(ticket, true, simile.getCid());
				AnaliticaSoggettoVO nuovoSoggettoIndice = eseguiCreazioneSoggettoIndice(ticket, reticoloSoggettoSimile.getDettaglio(), simile.getCid());
				if (nuovoSoggettoIndice != null && nuovoSoggettoIndice.isEsitoOk()) {
					//link creato, restituisce il cid di polo
					attivaCondivisioneSoggetto(ticket, reticoloSoggettoSimile.getDettaglio(),
							nuovoSoggettoIndice.getDettaglio().getCid(), OrigineSoggetto.INDICE);
					return reticoloSoggettoSimile;
				}

				return nuovoSoggettoIndice;
			}

			// esiste un soggetto con lo stesso CID
			if (soggettoImportato.isEsitoIDEsistente() ) {
				ElementoSinteticaSoggettoVO simileInPolo = first(soggettoImportato.getListaSimili());
				log.debug("importaSoggettoDaIndice(): cid uguale in polo: " + simileInPolo);
				AnaliticaSoggettoVO analiticaSimilePolo = caricaReticoloSoggetto(ticket, true, simileInPolo.getCid() );
				DettaglioSoggettoVO soggettoPolo = analiticaSimilePolo.getDettaglio();

				//verifica se i due soggetti sono identici
				if (ReticoloSoggetti.isSoggettoEquals(soggettoIndice.getTesto(), simileInPolo.getTesto())) {
					// testo uguale: è lo stesso soggetto
					attivaCondivisioneSoggetto(ticket, soggettoPolo, soggettoIndice.getCid(), OrigineSoggetto.INDICE );
					return analiticaSimilePolo;
				}

				//i soggetti sono diversi
				log.warn(String.format("importaSoggettoDaIndice(): soggetto '%s' presente in polo ma non uguale in indice", simileInPolo.getCid()));

				//si cerca in polo un soggetto uguale
				AnaliticaSoggettoVO simileInPoloConCidDiverso = cercaSoggettoPerTestoEsatto(ticket, soggettoIndice);
				if (simileInPoloConCidDiverso.isEsitoOk() ) {
					//trovato simile
					log.debug("importaSoggettoDaIndice(): trovato simile in polo: " + simileInPoloConCidDiverso.getDettaglio().getCid());
					attivaCondivisioneSoggetto(ticket, simileInPoloConCidDiverso.getDettaglio(), soggettoIndice.getCid(), OrigineSoggetto.INDICE);
					return simileInPoloConCidDiverso;
				}

				//simile in polo non trovato
				//viene tentata la creazione in indice del soggetto di polo con un nuovo cid
				CreaVariaSoggettoVO soggettoAccorpante = new CreaVariaSoggettoVO(soggettoIndice);
				soggettoAccorpante.setCid(getNuovoIdSoggetto(ticket));	//nuovo cid di polo
				soggettoAccorpante.setLivelloPolo(true);

				log.warn(String.format(
						"importaSoggettoDaIndice(): simile in polo non trovato. Tentativo invio nuovo cid '%s' in indice per creazione link...",
						soggettoAccorpante.getCid()));

				DettaglioSoggettoVO nuovoSoggettoPolo = soggettoIndice.copy();
				nuovoSoggettoPolo.setCid(soggettoAccorpante.getCid());
				nuovoSoggettoPolo.setLivAut(soggettoPolo.getLivAut());
				AnaliticaSoggettoVO nuovoSoggettoIndice = eseguiCreazioneSoggettoIndice(ticket, nuovoSoggettoPolo, soggettoAccorpante.getCid());
				if (nuovoSoggettoIndice != null && nuovoSoggettoIndice.getEsitoType() == SbnMarcEsitoType.OK) {
					soggettoAccorpante = semantica.get().creaSoggetto(soggettoAccorpante, ticket);
					//if (!creaSoggettoPerFusione(ticket, soggettoEliminato, soggettoAccorpante) )
					//	return null;

					if (soggettoAccorpante.getEsitoType() != SbnMarcEsitoType.OK) {
						//creazione in polo non riuscita, si elimina il soggetto in indice
						semantica.get().cancellaSoggettoDescrittore(false, soggettoAccorpante.getCid(), ticket, SbnAuthority.SO);
						messages.add(new UserMessage("errors.gestioneSemantica.incongruo", soggettoAccorpante.getTestoEsito()));
						return null;
					}
					//eliminazione link condiviso del vecchio soggetto
					disattivaCondivisioneSoggetto(ticket, soggettoPolo.getDatiCondivisione());

					//attivazione link condiviso del nuovo soggetto
					analiticaSimilePolo = caricaReticoloSoggetto(ticket, true, soggettoAccorpante.getCid());
					attivaCondivisioneSoggetto(ticket, analiticaSimilePolo.getDettaglio(),
							nuovoSoggettoIndice.getDettaglio().getCid(), OrigineSoggetto.POLO);
					return analiticaSimilePolo;
				}
			}
/*
			// il soggetto è stato trovato ma con edizione diversa, si verifica la compatibilità delle due edizioni
			if (soggettoImportato.getEsitoType() == SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA) {
				ElementoSinteticaSoggettoVO simile =
						ReticoloSoggetti.trovaSimileConTestoEsatto(soggettoIndice.getTesto(), soggettoImportato.getListaSimili() );
				if (simile == null) {
					messages.add(new UserMessage("errors.gestioneSemantica.incongruenzaSogg"));
					return null;
				}

				//dettaglio del simile in polo
				AnaliticaSoggettoVO reticoloSoggettoSimile = caricaReticoloSoggetto(ticket, true, simile.getCid());
				DettaglioSoggettoVO dettaglioSimile = (DettaglioSoggettoVO) reticoloSoggettoSimile.getReticolo().getDettaglio();

				DettaglioSoggettoVO soggIndice = (DettaglioSoggettoVO) reticoloSoggettoIndice.getReticolo().getDettaglio();
				String cidIndice = soggIndice.getCid();

				boolean compatibile = SoggettiUtil.isEdizioneCompatibile(
						SbnEdizioneSoggettario.valueOf(soggIndice.getEdizioneSoggettario()),
						SbnEdizioneSoggettario.valueOf(simile.getEdizioneSoggettario()));
				if (!compatibile) {
					//edizioni non compatibili, viene tentato il cambio edizione in polo
					CreaVariaSoggettoVO simileInPolo = new CreaVariaSoggettoVO(dettaglioSimile);
					simileInPolo.setEdizioneSoggettario(SbnEdizioneSoggettario.E.toString());
					CreaVariaSoggettoVO soggPoloModificato = modificaSoggetto(ticket, simileInPolo);
					if (soggPoloModificato.isEsitoOk()) {
						messages.add(new UserMessage("errors.gestioneSemantica.soggetto.importa.cambio.edizione.polo", simile.getCid()));
						// edizione modificata, provo ad attivare la condivisione tra i due soggetti
						attivaCondivisioneSoggetto(ticket, dettaglioSimile, cidIndice, OrigineSoggetto.INDICE );
						return caricaReticoloSoggetto(ticket, true, dettaglioSimile.getCid());
					}
				} else {
					// edizioni compatibili, provo ad attivare la condivisione tra i due soggetti
					if (attivaCondivisioneSoggetto(ticket, dettaglioSimile, cidIndice, OrigineSoggetto.INDICE ) ) {
						TreeElementViewSoggetti root = reticoloSoggettoSimile.getReticolo();
						root.setFlagCondiviso(true);
						root.getDettaglio().setCondiviso(true);
					}
					return reticoloSoggettoSimile;
				}
			}
*/
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", soggettoImportato.getTestoEsito()));
			return null;

		} catch (SbnMarcDiagnosticoException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			return null;

		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	private AnaliticaSoggettoVO cercaSoggettoPerTestoEsatto(String ticket, DettaglioSoggettoVO soggetto) throws Exception {
		//SbnEdizioneSoggettario edizione = null;
		//KeySoggetto key = ReticoloSoggetti.getChiaveSoggetto(soggetto.getCampoSoggettario(), edizione, soggetto.getTesto());
		RicercaComuneVO ricerca = new RicercaComuneVO();
		ricerca.setCodSoggettario(soggetto.getCampoSoggettario());
		ricerca.setEdizioneSoggettario(soggetto.getEdizioneSoggettario());
		ricerca.setPolo(true);
		ricerca.setRicercaTipo(TipoRicerca.STRINGA_ESATTA);
		ricerca.getRicercaSoggetto().setTestoSogg(soggetto.getTesto());

		RicercaSoggettoListaVO ricercaSoggetti = semantica.get().ricercaSoggetti(ricerca, ticket);
		if (ricercaSoggetti.getEsitoType() == SbnMarcEsitoType.OK) {
			ElementoSinteticaSoggettoVO simile = (ElementoSinteticaSoggettoVO) first(ricercaSoggetti.getRisultati());
			return caricaReticoloSoggetto(ticket, true, simile.getCid());
		}

		//errore
		AnaliticaSoggettoVO analiticaSoggettoVO = new AnaliticaSoggettoVO();
		analiticaSoggettoVO.setEsito(ricercaSoggetti.getEsito());
		return analiticaSoggettoVO;
	}

	public AnaliticaSoggettoVO caricaReticoloSoggetto(String ticket, boolean livelloPolo, String cid)
			throws SbnBaseException, RemoteException {
		checkTicket(ticket);
		List<UserMessage> messages = userMessages.get();
		try {
			AnaliticaSoggettoVO analitica = semantica.get().creaAnaliticaSoggettoPerCid(livelloPolo, cid, ticket, true);

			if (analitica.isEsitoOk() || analitica.isEsitoNonTrovato() )
				return analitica;

			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", analitica.getTestoEsito()));

			return null;

		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			return null;
		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return null;
		}
	}

	public boolean attivaCondivisioneSoggetto(String ticket, DettaglioSoggettoVO soggettoPolo, String cidIndice,
			OrigineSoggetto origineSoggetto) throws SbnBaseException, RemoteException {
		checkTicket(ticket);

		//almaviva5_20130227 check per condivisione già presente
		List<DatiCondivisioneSoggettoVO> datiCondivisionePolo = soggettoPolo.getDatiCondivisione();
		if (isFilled(datiCondivisionePolo) ) {
			if (Stream.of(datiCondivisionePolo).anyMatch(testCidIndiceCondiviso(cidIndice)) )
				return true;
		}

		//Timestamp ts = DaoManager.now();
		String firmaUtente = DaoManager.getFirmaUtente(ticket);

		if (isFilled(datiCondivisionePolo) ) { //cancello vecchie condivisioni
			for (DatiCondivisioneSoggettoVO vecchiaCondivisione : datiCondivisionePolo) {
				vecchiaCondivisione.setFlCanc("S");
				vecchiaCondivisione.setUteVar(firmaUtente);
				//vecchiaCondivisione.setTsVar(ts);
			}
		}

		//attivo condivisione tra polo e indice su questo cid
		String cidPolo = soggettoPolo.getCid();
		DatiCondivisioneSoggettoVO nuovaCondivisione = preparaDatiCondivisione(cidPolo, cidIndice, origineSoggetto, firmaUtente);

		// almaviva2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
		// la cattura avviene solo per le authority selzionate con il check (come per le W)
		datiCondivisionePolo = coalesce(datiCondivisionePolo, new ArrayList<DatiCondivisioneSoggettoVO>() );

		datiCondivisionePolo.add(nuovaCondivisione);

		try {
			semantica.get().aggiornaDatiCondivisioneSoggetto(datiCondivisionePolo, ticket);
			if (!ValidazioneDati.equals(cidPolo, cidIndice) ) {
				log.debug(String.format("attivata condivisione tra cid di polo '%s' e quello di indice '%s'", cidPolo, cidIndice));
				//messaggio all'utente in caso di cid non uguali
				List<UserMessage> messages = userMessages.get();
				messages.add(new UserMessage("errors.gestioneSemantica.soggetto.condivisione.attiva", cidPolo, cidIndice));
			}

		} catch (DAOException e) {
			return false;
		}

		soggettoPolo.setCondiviso(true);

		return true;
	}

	private DatiCondivisioneSoggettoVO preparaDatiCondivisione(String cidPolo, String cidIndice,
			OrigineSoggetto origineSoggetto, String firmaUtente) {
		DatiCondivisioneSoggettoVO nuovaCondivisione = new DatiCondivisioneSoggettoVO();
		nuovaCondivisione.setCidPolo(cidPolo);
		nuovaCondivisione.setCidIndice(cidIndice);
		nuovaCondivisione.setOrigineSoggetto(origineSoggetto);
		nuovaCondivisione.setOrigineLegame(OrigineLegameTitoloSoggetto.NESSUNO);
		nuovaCondivisione.setOrigineModifica(OrigineModificaSoggetto.NESSUNO);

		Timestamp ts = DaoManager.now();
		nuovaCondivisione.setTsIns(ts);
		//nuovaCondivisione.setTsVar(ts);

		nuovaCondivisione.setUteIns(firmaUtente);
		nuovaCondivisione.setUteVar(firmaUtente);
		return nuovaCondivisione;
	}

	public CreaVariaSoggettoVO modificaSoggetto(String ticket, CreaVariaSoggettoVO soggetto) throws SbnBaseException {
		checkTicket(ticket);
		List<UserMessage> messages = userMessages.get();
		try {
			CreaVariaSoggettoVO soggVariato = semantica.get().variaSoggetto((CreaVariaSoggettoVO) soggetto.copy(), ticket);

			SbnMarcEsitoType esito = soggVariato.getEsitoType();
			if (esito == SbnMarcEsitoType.OK)
				messages.add(new UserMessage("errors.gestioneSemantica.polo.operOk"));

			String soggSBN = CommonConfiguration.getProperty(Configuration.CODICE_SOGGETTARIO_FIRENZE, "FIR");

			//almaviva5_20120827 si prova ad aggiornare il soggetto in indice
			if (!soggetto.isLivelloPolo()
				//|| !soggetto.isCondiviso()
				|| !ValidazioneDati.equals(soggSBN, soggetto.getCodiceSoggettario())	//solo FIR
				|| esito != SbnMarcEsitoType.OK)
				return soggVariato;

			try {
				CommandInvokeVO cmd = CommandInvokeVO.build(ticket, CommandType.SEM_DATI_CONDIVISIONE_SOGGETTO,
						soggetto.getCid(), null, null);
				CommandResultVO result = semantica.get().invoke(cmd);
				result.throwError();

				List<DatiCondivisioneSoggettoVO> datiCondivisione =
					new ArrayList<DatiCondivisioneSoggettoVO>(result.getResultAsCollection(DatiCondivisioneSoggettoVO.class));

				//almaviva5_20121025 #5160 check profilo indice
				if(!isAbilitatoSoggettazioneIndice()) {
					disattivaCondivisioneSoggetto(ticket, datiCondivisione);
					return soggVariato;
				}

				if (!isFilled(datiCondivisione)) {
					DatiCondivisioneSoggettoVO nuovaCondivisione = preparaDatiCondivisione(soggetto.getCid(),
							soggetto.getCid(), OrigineSoggetto.POLO, DaoManager.getFirmaUtente(ticket));
					datiCondivisione.add(nuovaCondivisione);
				}

				DatiCondivisioneSoggettoVO dcs = first(datiCondivisione);

				AnaliticaSoggettoVO analitica = caricaReticoloSoggetto(ticket, false, dcs.getCidPolo());
				if (analitica == null) {
					messages.add(new UserMessage("errors.gestioneSemantica.incongruo", SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito()));
					if (soggetto.isCondiviso())
						disattivaCondivisioneSoggetto(ticket, datiCondivisione);
					return soggVariato;
				}

				CreaVariaSoggettoVO soggettoIndice = soggetto.copy();
				soggettoIndice.setLivelloPolo(false);

				boolean creazione = false;
				if (analitica.getEsitoType() == SbnMarcEsitoType.NON_TROVATO) {
					//non trovato, tentativo creazione
					//SOLO se marcato come condiviso
					creazione = soggetto.isCondiviso();
				} else {
					//trovato, modifica
					DettaglioSoggettoVO dettaglioIndice = analitica.getDettaglio();
					soggettoIndice.setT005(dettaglioIndice.getT005());
					soggettoIndice.setCid(dettaglioIndice.getCid());
					soggettoIndice.setLivello(ValidazioneDati.max(soggetto.getLivello(), dettaglioIndice.getLivAut()));
				}

				SbnMarcEsitoType esitoIndice = SbnMarcEsitoType.ERRORE_GENERICO;
				boolean retry = true;
				while (retry) {
					retry = false;
					CreaVariaSoggettoVO soggVariatoIndice = creazione ?
							semantica.get().creaSoggetto(soggettoIndice, ticket) :
							semantica.get().variaSoggetto(soggettoIndice, ticket);
					esitoIndice = soggVariatoIndice.getEsitoType();
					switch (esitoIndice) {
					case OK:
						messages.add(new UserMessage("errors.gestioneSemantica.indice.operOk"));
						DettaglioSoggettoVO dettaglioPolo = caricaReticoloSoggetto(ticket, true, soggetto.getCid()).getDettaglio();
						attivaCondivisioneSoggetto(ticket, dettaglioPolo, soggVariatoIndice.getCid(), OrigineSoggetto.POLO);
						break;

					case TROVATI_SIMILI:
						//riprovo con forzatura
						if (!soggettoIndice.isForzaCreazione()) {
							profiler.get().isForzaturaAuthority(SbnAuthority.SO, null);
							soggettoIndice.setForzaCreazione(true);
							retry = true;
							log.warn("trovato simile in indice; si procede con tentativo di forzatura");
							continue;
						}

					case ULTIMA_VARIAZIONE_NON_COINCIDENTE:
					case NON_TROVATO: {
						//inviato da indice anche se cid non esistente
						break;
					}

					default: {
					/*
						CreaVariaSoggettoVO soggettoAccorpante = soggetto.copy();
						soggettoAccorpante.setCid(getNuovoIdSoggetto(ticket));
						soggettoAccorpante.setLivelloPolo(true);
						if (creaSoggettoPerFusione(ticket, soggVariato, soggettoAccorpante) ) {
							DettaglioOggettoSemanticaVO dettaglio2 = caricaReticoloSoggetto(ticket, true, soggettoAccorpante.getCid()).getReticolo().getDettaglio();
							return new CreaVariaSoggettoVO((DettaglioSoggettoVO) dettaglio2);
						}
					*/
						messages.add(new UserMessage("errors.gestioneSemantica.incongruo", soggVariatoIndice.getTestoEsito()));
						if (soggetto.isCondiviso())
							disattivaCondivisioneSoggetto(ticket, datiCondivisione);
					}

					}
				}
/*
				if (esitoIndice != SbnMarcEsitoType.OK) {
					boolean uguale = ReticoloSoggetti.isSoggettoEquals(soggetto.getTesto(), dettaglioIndice.getTesto());
					SbnEdizioneSoggettario edPol = SbnEdizioneSoggettario.valueOf(soggetto.getEdizioneSoggettario());
					SbnEdizioneSoggettario edIdx = SbnEdizioneSoggettario.valueOf(dettaglioIndice.getEdizioneSoggettario());
					boolean compatibile = SoggettiUtil.isEdizioneCompatibile(edPol, edIdx);

					if (!uguale || !compatibile) {
						disattivaCondivisioneSoggetto(ticket, datiCondivisione);
					}
				}
*/
			} catch (Exception e) {
				log.error("", e);
			}

			return soggVariato;

		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			// nessun codice selezionato
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;
		}

	}

	private boolean isAbilitatoSoggettazioneIndice() throws Exception {
		boolean isDebug = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SBNMARC_LOCAL_DEBUG, "false"));
		if (!isDebug) {
			boolean okAuthority = profiler.get().isAbilitazioneAuthority(SbnAuthority.SO, null);
			boolean okAttivita = profiler.get().isOkAttivita("Modifica elemento di authority", "Modifica Soggetto", null);
			if (!okAuthority || !okAttivita)
				return false;
		}

		return true;
	}

	private void disattivaCondivisioneSoggetto(String ticket, List<DatiCondivisioneSoggettoVO> datiCondivisione)
			throws RemoteException, ApplicationException, Exception {
		List<UserMessage> messages = userMessages.get();
		for (DatiCondivisioneSoggettoVO dcs2 : datiCondivisione) {
			dcs2.setUteVar(DaoManager.getFirmaUtente(ticket));
			dcs2.setFlCanc("S");
		}

		CommandInvokeVO cmd2 = CommandInvokeVO.build(ticket, CommandType.SEM_AGGIORNA_DATI_CONDIVISIONE_SOGGETTO,
				new ArrayList<DatiCondivisioneSoggettoVO>(datiCondivisione));
		CommandResultVO result2 = semantica.get().invoke(cmd2);
		result2.throwError();
		String cidPolo = first(datiCondivisione).getCidPolo();
		String cidIndice = first(datiCondivisione).getCidIndice();

		log.warn("disattivazione condivisione soggetto: " + cidPolo + " <--> " + cidIndice);
		messages.add(new UserMessage("errors.gestioneSemantica.soggetto.condivisione.cancella",	cidPolo, cidIndice));
	}

	String verificaCidPerCreazioneIndice(String ticket, String cidIndice) {
		List<UserMessage> messages = userMessages.get();
		//almaviva5_20141008 #5650
		CommandInvokeVO command = new CommandInvokeVO(ticket,
				CommandType.SEM_VERIFICA_CID_CREAZIONE_INDICE, cidIndice);
		try {
			CommandResultVO result = semantica.get().invoke(command);
			result.throwError();

			return (String) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			messages.add(new UserMessage(e));
		} catch (ValidationException e) {
			messages.add(new UserMessage(e));
		} catch (Exception e) {
			log.error("", e);
			messages.add(new UserMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()));
		}

		return null;
	}

	private AnaliticaSoggettoVO eseguiCreazioneSoggettoIndice(String ticket, DettaglioSoggettoVO soggettoPolo, String cidIndice) {

		List<UserMessage> messages = userMessages.get();
		CreaVariaSoggettoVO richiesta = new CreaVariaSoggettoVO(soggettoPolo);
		CreaVariaSoggettoVO soggettoCreato = null;

		richiesta.setLivelloPolo(false);
		//almaviva5_20120917 #5111 l'indice non controlla soggetti più lunghi di 80 caratteri.
		richiesta.setForzaCreazione(ValidazioneDati.length(soggettoPolo.getTesto()) >= 80);
		//almaviva5_20120801
		richiesta.setCid(cidIndice);

		try {
/*
			//almaviva5_20141008 #5650
			String newCid = verificaCidPerCreazioneIndice(ticket, cidIndice);
			if (newCid == null)
				return null;
			richiesta.setCid(newCid);
*/
			boolean retry = true;
			while (retry) {
				retry = false;
				soggettoCreato = semantica.get().creaSoggetto((CreaVariaSoggettoVO) richiesta.copy(), ticket);
				// il soggetto non esiste, lo creo
				if (soggettoCreato.isEsitoOk()) {
					attivaCondivisioneSoggetto(ticket, soggettoPolo, soggettoCreato.getCid(), OrigineSoggetto.POLO);
					return caricaReticoloSoggetto(ticket, false, soggettoCreato.getCid());
				}

				log.warn(String.format("eseguiCreazioneSoggettoIndice(): creazione di '%s' in indice fallita: '%s'", richiesta.getCid(),
						soggettoCreato.getTestoEsito()));

				//almaviva5_20141007 #5650
				if (soggettoCreato.getEsitoType() == SbnMarcEsitoType.ID_NON_VALIDO) {
					//il cid inviato non è valido (polo diverso?), provo a creare un nuovo cid in indice
					retry = true;
					richiesta.setCid(SBNMarcUtil.SBNMARC_DEFAULT_ID);
					continue;
				}

				// esiste un soggetto con lo stesso testo, ritorno questo
				if (ValidazioneDati.in(soggettoCreato.getEsitoType(),
						SbnMarcEsitoType.TROVATI_SIMILI,
						SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA,
						SbnMarcEsitoType.SOGGETTO_GIA_MAPPATO) ) {
					ElementoSinteticaSoggettoVO simile = ReticoloSoggetti.trovaSimileConTestoEsatto(
							soggettoPolo.getTesto(),
							soggettoCreato.getListaSimili());
					if (simile == null) {
						if (richiesta.isForzaCreazione()) {
							//almaviva5_20160421 #6163
							if (soggettoCreato.isCambioEdizione()) {
								simile = first(soggettoCreato.getListaSimili());
								//se l'indice ha semplicemente cambiato l'edizione, ritorno il soggetto di indice
								attivaCondivisioneSoggetto(ticket, soggettoPolo, simile.getCid(), OrigineSoggetto.INDICE);
								return caricaReticoloSoggetto(ticket, false, simile.getCid());
							}

							//soggetti incompatibili
							messages.add(new UserMessage("errors.gestioneSemantica.incongruenzaSogg"));
							return null;

						} else {
							//riprovo con forzatura
							retry = true;
							richiesta.setForzaCreazione(true);
							continue;
						}

					} else {
						log.debug("eseguiCreazioneSoggettoIndice(): trovato simile in polo: " + simile);
						//almaviva5_20130110 #5209
						SbnEdizioneSoggettario edPol = SbnEdizioneSoggettario.valueOf(soggettoPolo.getEdizioneSoggettario());
						SbnEdizioneSoggettario edIdx = SbnEdizioneSoggettario.valueOf(simile.getEdizioneSoggettario());
						if (!SoggettiUtil.isEdizioneCompatibile(edPol, edIdx) ) {
							//se l'edizione dei due soggetti non è compatibile è necessario forzare un cambio edizione in indice
							if (richiesta.isForzaCreazione()) {
								messages.add(new UserMessage("errors.gestioneSemantica.incongruenzaSogg"));
								return null;
							} else {
								//riprovo con forzatura
								retry = true;
								richiesta.setForzaCreazione(true);
								continue;
							}
						}
					}

					attivaCondivisioneSoggetto(ticket, soggettoPolo, simile.getCid(), OrigineSoggetto.INDICE);
					return caricaReticoloSoggetto(ticket, false, simile.getCid());
				}

			}

			// esiste un soggetto con lo stesso CID
			if (soggettoCreato.isEsitoIDEsistente()) {
				//verifica che il soggetto sia lo stesso
				ElementoSinteticaSoggettoVO simile = first(soggettoCreato.getListaSimili());
				log.debug("eseguiCreazioneSoggettoIndice(): trovato simile in indice: " + simile);
				if (simile != null && ReticoloSoggetti.isSoggettoEquals(simile.getTesto(), soggettoPolo.getTesto())) {
					attivaCondivisioneSoggetto(ticket, soggettoPolo, simile.getCid(), OrigineSoggetto.INDICE);
					return caricaReticoloSoggetto(ticket, false, simile.getCid());
				}

				//i soggetti sono diversi
				//viene tentata la creazione in indice del soggetto di polo con un nuovo cid
				//in caso di successo il vecchio cid di polo verrà fuso con il nuovo
				log.warn(String.format("eseguiCreazioneSoggettoIndice(): soggetto '%s' presente in indice ma non uguale", soggettoPolo.getCid()));

				//almaviva5_20180621 #6624
				if (!profiler.get().isOkAttivita(ticket, CodiciAttivita.getIstance().FONDE_SOGGETTO_1272) ) {
					log.warn("eseguiCreazioneSoggettoIndice(): utente non abilitato alla fusione in polo, impossibile continuare.");
					messages.add(new UserMessage("errors.gestioneSemantica.soggetto.fondi.polo.non.abilitato"));
					return null;
				}

				CreaVariaSoggettoVO soggettoEliminato = new CreaVariaSoggettoVO(soggettoPolo);
				CreaVariaSoggettoVO soggettoAccorpante = new CreaVariaSoggettoVO(soggettoPolo);
				soggettoAccorpante.setCid(getNuovoIdSoggetto(ticket));	//nuovo cid di polo
				soggettoAccorpante.setLivelloPolo(true);

				log.debug(String.format(
						"eseguiCreazioneSoggettoIndice(): tentativo invio nuovo cid '%s' in indice per creazione link...",
						soggettoAccorpante.getCid()));

				DettaglioSoggettoVO nuovoSoggettoPolo = soggettoPolo.copy();
				nuovoSoggettoPolo.setCid(soggettoAccorpante.getCid());
				AnaliticaSoggettoVO soggettoIndice = eseguiCreazioneSoggettoIndice(ticket, nuovoSoggettoPolo, soggettoAccorpante.getCid());
				if (soggettoIndice != null && soggettoIndice.getEsitoType() == SbnMarcEsitoType.OK) {
					if (!creaSoggettoPerFusione(ticket, soggettoEliminato, soggettoAccorpante) ) {
						//cancellazione soggetto in indice
						semantica.get().cancellaSoggettoDescrittore(false, soggettoAccorpante.getCid(), ticket, SbnAuthority.SO);
						return null;
					}

					//ok soggetto fuso
					nuovoSoggettoPolo = caricaReticoloSoggetto(ticket, true, soggettoAccorpante.getCid()).getDettaglio();
					attivaCondivisioneSoggetto(ticket, nuovoSoggettoPolo, soggettoIndice.getDettaglio().getCid(), OrigineSoggetto.POLO);
					return soggettoIndice;
				}
			}

			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", soggettoCreato.getTestoEsito()));
			return null;

		} catch (SbnMarcDiagnosticoException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			return null;

		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;
		}

	}

	private boolean creaSoggettoPerFusione(String ticket, CreaVariaSoggettoVO soggettoEliminato, CreaVariaSoggettoVO soggettoAccorpante)
			throws DAOException, RemoteException {
		List<UserMessage> messages = userMessages.get();

		log.debug(String.format("fusione del soggetto '%s' su '%s'...", soggettoEliminato.getCid(), soggettoAccorpante.getCid()));
		soggettoAccorpante.setCattura(true);	//disattivazione controlli di duplicazione
		CreaVariaSoggettoVO soggettoCreato = semantica.get().creaSoggetto(soggettoAccorpante, ticket);
		if (soggettoCreato.getEsitoType() != SbnMarcEsitoType.OK)
			return false;

		FondiSoggettoVO fusione = new FondiSoggettoVO();
		fusione.setIdElementoEliminato(soggettoEliminato.getCid());
		fusione.setIdElementoAccorpante(soggettoCreato.getCid());
		AreaDatiAccorpamentoReturnVO fondiSoggetti = semantica.get().fondiSoggetti(true, fusione, ticket);

		boolean ok = SbnMarcEsitoType.of(fondiSoggetti.getCodiceRitorno()) == SbnMarcEsitoType.OK;
		if (ok) {
			messages.add(new UserMessage("errors.gestioneSemantica.soggetto.condiviso.fondi.polo",
					soggettoEliminato.getCid(), soggettoCreato.getCid()));
		} else {
			messages.add(new UserMessage("errors.gestioneSemantica.SBNMarc", fondiSoggetti.getIdErrore()));
			//cancellazione cid in polo
			semantica.get().cancellaSoggettoDescrittore(true, soggettoAccorpante.getCid(), ticket, SbnAuthority.SO);
		}
		return ok;
	}

	private String getNuovoIdSoggetto(String ticket) {
		List<UserMessage> messages = userMessages.get();
		try {
			AreaDatiPassaggioGetIdSbnVO areaDatiPass = new AreaDatiPassaggioGetIdSbnVO();
			areaDatiPass.setTipoAut("SO");
			areaDatiPass.setTipoMat(null);
			areaDatiPass.setTipoRec(null);
			AreaDatiPassaggioGetIdSbnVO idSbn =	interrogazione.get().getIdSbn(areaDatiPass, ticket);
			if (SbnMarcEsitoType.of(idSbn.getCodErr()) != SbnMarcEsitoType.OK) {
				messages.add(new UserMessage("errors.gestioneSemantica.incongruo", areaDatiPass.getTestoProtocollo()));
				return null;
			}

			return idSbn.getIdSbn();

		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;
		}

	}


	public AnaliticaSoggettoVO inviaSoggettoInIndice(String ticket, DettaglioSoggettoVO soggetto) throws SbnBaseException {
		checkTicket(ticket);
		List<UserMessage> messages = userMessages.get();
		try {
			// controllo che il soggetto da inviare sia compatibili con i vincoli dell'indice
			if (!checkSoggettoPerIndice(soggetto.getTesto())) {
				return null;
			}

			List<DatiCondivisioneSoggettoVO> datiCondivisione = soggetto.getDatiCondivisione();
			// il soggetto risulta già condiviso
			if (isFilled(datiCondivisione) ) {
			/*
				DatiCondivisioneSoggettoVO dcs = first(datiCondivisione);
				AnaliticaSoggettoVO reticoloSoggettoIndice = caricaReticoloSoggetto(ticket, false, dcs.getCidIndice());
				if (reticoloSoggettoIndice == null)
					return null;
				// trovato in indice
				if (reticoloSoggettoIndice.isEsitoOk()) {
					DettaglioSoggettoVO soggettoIndice =
						(DettaglioSoggettoVO) reticoloSoggettoIndice.getReticolo().getDettaglio();
					// controllo testo polo == testo indice
					// se uguale è il mio cid. ho finito
					if (SoggettiUtil.isSoggettoEquals(soggetto.getTesto(), soggettoIndice.getTesto()))
						return reticoloSoggettoIndice;

					// non è uguale:
					// se l'origine del soggetto in indice è il polo allora non sono più allineati
					// devo inviare un nuovo cid in indice
					//if (dcs.getOrigineSoggetto() != OrigineSoggetto.POLO) {

						// se cid di polo != da cid indice provo a ri-creare il mio in indice
						if (!soggetto.getCid().equals(soggettoIndice.getCid()))
							return eseguiCreazioneSoggettoIndice(ticket, soggetto, soggetto.getCid() );
					//}

					// se cid di polo == cid indice creo con un nuovo
					// progressivo di polo
					String idSoggetto = getIdSoggetto(ticket);
					if (idSoggetto == null)
						return null;

					return eseguiCreazioneSoggettoIndice(ticket, soggetto, idSoggetto);
				}

				//non trovato in indice
				if (reticoloSoggettoIndice.isEsitoNonTrovato()) {

					// se cid di polo != da cid indice provo a ri-creare il mio in indice
					if (!soggetto.getCid().equals(dcs.getCidIndice()))
						return eseguiCreazioneSoggettoIndice(ticket, soggetto, soggetto.getCid() );

					// se cid di polo == cid indice creo con un nuovo progressivo di polo
					String idSoggetto = getIdSoggetto(ticket);
					if (idSoggetto == null)
						return null;

					soggetto.setCid(idSoggetto); // imposto nuovo id di polo
					return eseguiCreazioneSoggettoIndice(ticket, soggetto, idSoggetto);
				}

				messages.add(new UserMessage("errors.gestioneSemantica.incongruo",
						reticoloSoggettoIndice.getTestoEsito()));
				return null;
			*/
			}

			return eseguiCreazioneSoggettoIndice(ticket, soggetto, soggetto.getCid() );
		/*
		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;
		*/
		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;
		}


	}

	private boolean checkSoggettoPerIndice(String testoSoggetto) {
		List<UserMessage> messages = userMessages.get();
		// controllo lunghezza per invio in indice
		if (!SoggettiUtil.checkSoggettoPerIndice(testoSoggetto)) {
			messages.add(new UserMessage("errors.gestioneSemantica.soggetto.indiceMaxLen", SoggettiUtil.MAX_LEN_SOGGETTO_INDICE));
			return false;
		}

		return true;
	}

	public DatiLegameTitoloSoggettoVO invioInIndiceLegamiTitoloSoggetto(String ticket,
			TreeElementViewTitoli reticoloIndice, List<ElementoSinteticaSoggettoVO> soggettiDaInviare)
			throws SbnBaseException {
		checkTicket(ticket);
		List<UserMessage> messages = userMessages.get();

		DatiLegameTitoloSoggettoVO risposta = null;

		if (reticoloIndice == null) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema"));
			return null;
		}

		DettaglioTitoloParteFissaVO detTitoloPFissaVO = reticoloIndice
			.getAreaDatiDettaglioOggettiVO()
			.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO();
		String T005 = detTitoloPFissaVO.getVersione(); // timestamp del titolo

		if (!isFilled(soggettiDaInviare) ) {
			messages.add(new UserMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return null;
		}

		// controllo che i soggetti da inviare siano compatibili con i vincoli dell'indice
		for (ElementoSinteticaSoggettoVO soggetto : soggettiDaInviare)
			if (!checkSoggettoPerIndice(soggetto.getTesto() ))
				return null;

		List<ElementoSinteticaSoggettoVO> listaSoggettiLegatiDaReinviare = new ArrayList<ElementoSinteticaSoggettoVO>();

		try {
			//almaviva5_20090915 disattivato a fronte risoluzione bug indice
			/*
			String bid = reticoloIndice.getKey(); // titolo

			// 1. carico situazione polo
			CatSemSoggettoVO listaSoggettiPolo = factory
				.getGestioneSemantica().ricercaSoggettiPerBidCollegato(
					true, bid, null, SBNMARC_MAX_RIGHE, utenteCollegato.getTicket());

			if (!listaSoggettiPolo.isEsitoOk()) {
				messages.add(new UserMessage(
						"errors.gestioneSemantica.incongruo", listaSoggettiPolo.getTestoEsito()));

				return null;
			}

			// 2. carico situazione indice
			CatSemSoggettoVO listaSoggettiIndice = factory
				.getGestioneSemantica().ricercaSoggettiPerBidCollegato(
					false, bid, null, SBNMARC_MAX_RIGHE, utenteCollegato.getTicket());

			if (!listaSoggettiIndice.isEsitoOk() && !listaSoggettiIndice.isEsitoNonTrovato() ) {
				messages.add(new UserMessage(
						"errors.gestioneSemantica.incongruo", listaSoggettiIndice.getTestoEsito()));

				return null;
			}


			// 3. cancello tutti i legami in indice (se posso)
			if (listaSoggettiIndice.getListaSoggetti().size() > 0) {

				DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();

				for (ElementoSinteticaSoggettoVO soggettoLegato : listaSoggettiIndice.getListaSoggetti())
					legame.getLegami().add(legame.new LegameTitoloSoggettoVO(soggettoLegato.getCid(), ""));

				// CANCELLO IL LEGAME TRA TITOLO E SOGGETTO SULLA BASE DATI DI INDICE
				T005 = detTitoloPFissaVO.getVersione();
				legame.setBid(detTitoloPFissaVO.getBid() );
				legame.setBidNatura(detTitoloPFissaVO.getNatura());
				legame.setT005(T005);
				legame.setBidLivelloAut(detTitoloPFissaVO.getLivAut());
				legame.setBidTipoMateriale(detTitoloPFissaVO.getTipoMat());
				legame.setLivelloPolo(false);

				risposta = factory.getGestioneSemantica().cancellaLegameTitoloSoggetto(legame, utenteCollegato.getTicket());

				if (!risposta.isEsitoOk()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									risposta.getTestoEsito()));

					return null;
				}

				//aggiorno timestamp titolo
				T005 = risposta.getT005();

			} // fase 3


			// 4. merge polo/indice (primo controllo per cid e poi per testo esatto)
			// tutti i soggetti in indice e non in polo vanno eliminati dalla lista
			List<ElementoSinteticaSoggettoVO> listaSoggettiLegatiDaReinviare =
				generaListaSoggettiLegatiDaReinviare(listaSoggettiPolo.getListaSoggetti(),
					listaSoggettiIndice.getListaSoggetti(), soggettiDaInviare);
			*/

			// 5. invio in indice dei cid mancanti
			for (ElementoSinteticaSoggettoVO soggetto : soggettiDaInviare) {
				AnaliticaSoggettoVO analiticaSogg = caricaReticoloSoggetto(ticket, true, soggetto.getCid() );
				if (analiticaSogg == null)
					return null;

				DettaglioSoggettoVO dettaglio = analiticaSogg.getDettaglio();

				// provo l'invio in indice del soggetto
				AnaliticaSoggettoVO soggettoInIndice = inviaSoggettoInIndice(ticket, dettaglio);
				if (soggettoInIndice == null)
					return null;

				DettaglioSoggettoVO dettaglioIndice = soggettoInIndice.getDettaglio();
				listaSoggettiLegatiDaReinviare.add(new ElementoSinteticaSoggettoVO(dettaglioIndice));
			} // fase 5

			// 6. reinvio di tutti i legami in indice

			//tolgo i duplicati
			Map<String, ElementoSinteticaSoggettoVO> soggUnique =
				new HashMap<String, ElementoSinteticaSoggettoVO>();
			for (ElementoSinteticaSoggettoVO soggettoLegato: listaSoggettiLegatiDaReinviare)
				soggUnique.put(soggettoLegato.getCid(), soggettoLegato);

			if (soggUnique.size() > 0) {

				DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();

				for (ElementoSinteticaSoggettoVO soggettoLegato : soggUnique.values() )
					legame.getLegami().add(legame.new LegameTitoloSoggettoVO(soggettoLegato.getCid(), ""));

				// CREO IL LEGAME TRA TITOLO E SOGGETTO SULLA BASE DATI DI
				// INDICE
				legame.setBid(detTitoloPFissaVO.getBid() );
				legame.setBidNatura(detTitoloPFissaVO.getNatura());
				legame.setT005(T005);
				legame.setBidLivelloAut(detTitoloPFissaVO.getLivAut());
				legame.setBidTipoMateriale(detTitoloPFissaVO.getTipoMat());
				legame.setLivelloPolo(false);

				risposta = semantica.get().invioInIndiceLegameTitoloSoggetto(legame, ticket);

				if (!risposta.isEsitoOk()) {
					messages.add(new UserMessage("errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));
					return null;
				}

			} // fase 6

			messages.add(new UserMessage("errors.gestioneSemantica.operOk"));

			return risposta;

		} catch (ValidationException e) {
			// errori indice
			messages.add(new UserMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return null;

		} catch (DataException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (InfrastructureException e) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return null;

		} catch (Exception e) {
			messages.add(new UserMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return null;
		}


	}

	public boolean attivaCondivisioneTitoloSoggetto(String ticket, String cidPolo, String bid)
			throws SbnBaseException, RemoteException {
		checkTicket(ticket);
		List<UserMessage> messages = userMessages.get();

		AnaliticaSoggettoVO analitica = caricaReticoloSoggetto(ticket, true, cidPolo);
		if (!analitica.isEsitoOk()) {
			messages.add(new UserMessage("errors.gestioneSemantica.incongruo", analitica.getTestoEsito()));
			return false;
		}

		DettaglioSoggettoVO soggettoPolo = analitica.getDettaglio();
		//almaviva5_20130227 check per condivisione già presente
		List<DatiCondivisioneSoggettoVO> datiCondivisione = soggettoPolo.getDatiCondivisione();
		if (!isFilled(datiCondivisione) )
			return false;

		DatiCondivisioneSoggettoVO nuovaCondivisione = first(datiCondivisione).copy();
		nuovaCondivisione.setBid(bid);

		String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
		nuovaCondivisione.setUteIns(firmaUtente);
		nuovaCondivisione.setUteVar(firmaUtente);
		Timestamp ts = DaoManager.now();
		nuovaCondivisione.setTsIns(ts);
		//nuovaCondivisione.setTsVar(ts);
		nuovaCondivisione.setOrigineLegame(OrigineLegameTitoloSoggetto.INDICE);

		try {
			datiCondivisione.add(nuovaCondivisione);
			semantica.get().aggiornaDatiCondivisioneSoggetto(datiCondivisione, ticket);

		} catch (DAOException e) {
			return false;
		}

		return true;
	}

}
