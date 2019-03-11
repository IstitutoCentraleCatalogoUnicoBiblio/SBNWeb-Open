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
package it.iccu.sbn.ejb.domain.servizi.calendario;

import static it.iccu.sbn.util.matchers.Sale.bySala;
import static it.iccu.sbn.util.matchers.Sale.isSalaPrenotabile;
import static it.iccu.sbn.util.servizi.CalendarioUtil.getDurataPrenotazione;
import static it.iccu.sbn.util.servizi.CalendarioUtil.getDurataTotaleGiorno;
import static it.iccu.sbn.util.servizi.CalendarioUtil.getOrarioAperturaChiusura;
import static it.iccu.sbn.util.servizi.CalendarioUtil.isPrenotabile;
import static it.iccu.sbn.util.servizi.CalendarioUtil.preparaCalendario;
import static it.iccu.sbn.util.servizi.CalendarioUtil.toMinutes;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.domain.servizi.sale.Sale;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.FasciaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO.Disponibilita;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO.SlotSala;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO.StatoGiorno;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.SlotVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.servizi.CalendarioUtil;
import it.iccu.sbn.vo.custom.servizi.sale.CatMediazione;
import it.iccu.sbn.vo.custom.servizi.sale.SalaDecorator;
import it.iccu.sbn.vo.validators.calendario.Interval;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.asList;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.size;

public class GrigliaCalendario {

	static Logger log = Logger.getLogger(GrigliaCalendario.class);

	static double DISPONIBILITA_ALTA = 0.70;
	static double DISPONIBILITA_MEDIA = 0.45;
	static double DISPONIBILITA_BASSA = 0.20;

	static Reference<Calendario> calendario = new Reference<Calendario>() {
		@Override
		protected Calendario init() throws Exception {
			return DomainEJBFactory.getInstance().getCalendario();
		}};

	static Reference<Sale> saleEjb = new Reference<Sale>() {
		@Override
		protected Sale init() throws Exception {
			return DomainEJBFactory.getInstance().getSale();
		}};

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}};

	static Predicate<PrenotazionePostoVO> byPostoAndData(final PostoSalaVO posto, final Timestamp ts_inizio, final Timestamp ts_fine, final int idUtente) {
		return new Predicate<PrenotazionePostoVO>() {
			public boolean test(PrenotazionePostoVO pp) {
				boolean ok = pp.getPosto().getId_posto() == posto.getId_posto();
				if (!ok) {
					for (UtenteBaseVO u : pp.getAltriUtenti()) {
						ok = u.getIdUtente() == idUtente;
						if (ok) break;
					}
				}
				if (ok) {
//					ok = between(pp.getTs_inizio(), ts_inizio, ts_fine)
//						|| between(pp.getTs_fine(), ts_inizio, ts_fine);
					Interval i = Interval.of(ts_inizio, ts_fine);
					Interval ipp = Interval.of(pp.getTs_inizio(), pp.getTs_fine());
					ok = ipp.overlaps(i);
				}
				return ok;
			}};
	}

	static Predicate<PrenotazionePostoVO> byUtenteAndData(final int idUtente, final Date date) {
		return new Predicate<PrenotazionePostoVO>() {
			public boolean test(PrenotazionePostoVO pp) {
				boolean ok = false;
				for (UtenteBaseVO u : pp.getAltriUtenti()) {
					ok = u.getIdUtente() == idUtente;
					if (ok) break;
				}
				return ok &&
					DateUtil.isSameDay(date, pp.getTs_inizio());
			}
		};
	}

	static class Slot {
		List<PostoSalaVO> postiTotali = new ArrayList<PostoSalaVO>();
		List<PostoSalaVO> postiDisponibili = new ArrayList<PostoSalaVO>();
	}

	private String ticket;
	private String codPolo;
	private String codBib;
	private Date inizio;
	private Date fine;
	private InventarioVO inventario;
	private UtenteBaseVO utente;

	private String cd_cat_mediazione;
	private CalendarioVO calendarioBib;
	private List<PrenotazionePostoVO> prenotazioni;
	private CalendarioVO calendarioCatMed;
	private List<SalaVO> sale;
	private List<PostoSalaVO> posti;
	private Map<SalaVO, CalendarioVO> calendariSala;

	private Timestamp now;

	private short numMaxPrenPosto = 0;

	//n. giorni riservati dalla biblioteca per la preparazione del supporto
	private int giorniRiservati = 0;

	private ParametriBibliotecaVO parametriBiblioteca;
	private ServizioBibliotecaVO servizio;
	private boolean utenteRemoto;
	private List<PrenotazionePostoVO> prenotazioniEscluse;


	public GrigliaCalendario(String ticket, RicercaGrigliaCalendarioVO ricerca, Timestamp now) throws SbnBaseException {
		this.ticket = ticket;
		this.codPolo = ricerca.getCodPolo();
		this.codBib = ricerca.getCodBib();
		this.inizio = ricerca.getInizio();
		this.fine = ricerca.getFine();
		this.cd_cat_mediazione = first(ricerca.getCd_cat_mediazione());
		this.inventario = ricerca.getInventario();
		this.utente = ricerca.getUtente();
		this.servizio = ricerca.getServizio();
		this.utenteRemoto = ricerca.isRemoto();
		this.prenotazioniEscluse = ricerca.getPrenotazioniEscluse();

		this.now = now;//DaoManager.now();

		init();
	}

	private void init() throws SbnBaseException {
		try {
			this.parametriBiblioteca = servizi.get().getParametriBiblioteca(ticket, codPolo, codBib);
			if (this.parametriBiblioteca == null)
				throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

			//check su disponibilità inventario
			if (inventario != null) {
				log.debug("check su stato digitalizzazione inventario " + inventario.getChiaveInventario() );
				String digitInv = inventario.getDigitalizzazione();
				if (isFilled(digitInv)) {
					String catMedDigitBib = parametriBiblioteca.getCatMediazioneDigit();
					if (isFilled(catMedDigitBib))
						this.cd_cat_mediazione = catMedDigitBib;
				}

				log.debug("check su disponibilità inventario " + inventario.getChiaveInventario() );
				//controllare se l'inventario ha prenotazioni nell'intervallo scelto.
				//List<PrenotazionePostoVO> prenotazioniInv = getListaPrenotazioniInventario();
				//prenotazioni.addAll(prenotazioniInv);

				//TODO
				//controllare se l'inventario è in prestito nell'intervallo scelto
			}

			//caricamento calendario biblioteca
			log.debug("caricamento calendario biblioteca " + codPolo + codBib);
			ModelloCalendarioVO modelloBib = calendario.get().getCalendarioBiblioteca(ticket, codPolo, codBib);
			if (modelloBib == null)
				throw new ApplicationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_BIB_NON_TROVATO);
			calendarioBib = preparaCalendario(modelloBib, inizio, fine);

			//le prenotazioni nel periodo
			prenotazioni = getListaPrenotazioniPosto();

			//calendario della cat. di mediazione
			if (isFilled(cd_cat_mediazione)) {
				log.debug("caricamento calendario cat. mediazione " + cd_cat_mediazione);
				ModelloCalendarioVO modelloCatMed = calendario.get().getCalendarioCategoriaMediazione(ticket, codPolo, codBib, cd_cat_mediazione);
				if (modelloCatMed != null)
					calendarioCatMed = preparaCalendario(modelloCatMed, inizio, fine);
			}

			//posti (tutti o solo quelli disponibili per la cat. di mediazione)
			posti = saleEjb.get().getPostiByCatMediazione(ticket, codPolo, codBib, cd_cat_mediazione, utenteRemoto);
			//selezione delle sale prenotabili e con posti compatibili
			sale = Stream.of(posti).map(bySala()).filter(isSalaPrenotabile(utenteRemoto)).distinct().toList();

			//eventuali calendari delle sale
			calendariSala = new HashMap<SalaVO, CalendarioVO>();
			for (SalaVO sala : sale) {
				log.debug("caricamento calendario sala " + sala.getCodSala() );
				ModelloCalendarioVO modelloSala = calendario.get().getCalendarioSala(ticket, sala);
				if (modelloSala != null)
					calendariSala.put(sala, preparaCalendario(modelloSala, inizio, fine));
			}

			if (servizio != null)
				numMaxPrenPosto = coalesce(servizio.getNumMaxPrenPosto(), (short)0);

			//se la richiesta è inserita dall'utente si verificano i tempi di preparazione del supporto (se previsto)
			if (utenteRemoto) {
				CatMediazione catMed = CatMediazione.of(CodiciProvider.cercaCodice(this.cd_cat_mediazione,
						CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE, CodiciRicercaType.RICERCA_CODICE_SBN));
				if (catMed != null && catMed.isRichiedeSupporto()) {
					int ggPrepSupp = CalendarioUtil.calcolaGiorniPreparazioneSupporto(calendarioBib, now, servizio);
					if (inizio.compareTo(now) <= 0)
						giorniRiservati = ggPrepSupp;
					else {
						//fix cambio mese
						int diff = DateUtil.diffDays(now, inizio);
						giorniRiservati = ggPrepSupp - diff;
					}
				}
			}

		} catch (SbnBaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

/*
	private List<PrenotazionePostoVO> getListaPrenotazioniInventario() throws Exception {
		RicercaPrenotazionePostoVO ricercaPren = new RicercaPrenotazionePostoVO();

		ricercaPren.setCodPolo(codPolo);
		ricercaPren.setCodBib(codBib);
		ricercaPren.setTs_inizio(new Timestamp(inizio.getTime()));
		ricercaPren.setTs_fine(DateUtil.withTimeAtEndOfDay(fine));
		ricercaPren.setInventario(inventario);

		return sale.get().getListaPrenotazioniPosto(ticket, ricercaPren);
	}
*/
	private List<PrenotazionePostoVO> getListaPrenotazioniPosto() throws Exception {
		RicercaPrenotazionePostoVO ricercaPren = new RicercaPrenotazionePostoVO();

		ricercaPren.setCodPolo(codPolo);
		ricercaPren.setCodBib(codBib);
		ricercaPren.setTs_inizio(new Timestamp(inizio.getTime()));
		ricercaPren.setTs_fine(DateUtil.withTimeAtEndOfDay(fine));

		for (PrenotazionePostoVO pp : this.prenotazioniEscluse )
			ricercaPren.getPrenotazioniEscluse().add(pp.getId_prenotazione());

		return saleEjb.get().getListaPrenotazioniPosto(ticket, ricercaPren);
	}

	private Slot cercaSlotPrenotazione(SalaVO sala, Date when, LocalTime t_start, LocalTime t_end) throws SbnBaseException {
		Slot slot = new Slot();
		try {
			int durataTotGiorno = 0;	//durata in minuti delle fasce giornaliere

			Timestamp ts_inizio = DateUtil.componiData(when, t_start, 0);
			Timestamp ts_fine = DateUtil.componiData(when, t_end, 0);
			if (ts_inizio.before(now) && ts_fine.before(now))
				return slot;

			//	1.verificare che D sia compatibile con calendario biblioteca.
			if (!isPrenotabile(asList(calendarioBib), when, t_start, t_end))
				return slot;
			durataTotGiorno = getDurataTotaleGiorno(asList(calendarioBib), when);

			//	2.se M ha un calendario, controllare che D sia compatibile con esso.
			if (calendarioCatMed != null) {
				if (!isPrenotabile(asList(calendarioCatMed), when, t_start, t_end))
					return slot;
				durataTotGiorno = getDurataTotaleGiorno(asList(calendarioCatMed), when);
			}

			//	3.recuperare sala S con posti che gestiscono mediazione M.
			durataTotGiorno = toMinutes(t_end) - toMinutes(t_start);

			for (PostoSalaVO posto : posti) {
				if (posto.getSala().getIdSala() != sala.getIdSala())
					continue;

				slot.postiTotali.add(posto);
				CalendarioVO calendarioSala = calendariSala.get(sala);
				//	4.se S ha un calendario, controllare che D sia compatibile con esso.
				if (calendarioSala != null && !isPrenotabile(asList(calendarioSala), when, t_start, t_end))
					continue;

				//	5.caricare tutte le prenotazioni per M con data D.
				List<PrenotazionePostoVO> prenotazioniPosto =
						Stream.of(prenotazioni).filter(byPostoAndData(posto, ts_inizio, ts_fine, utente.getIdUtente()) ).toList();

				int durataPrenotGiorno = 0;
				for (PrenotazionePostoVO pp : prenotazioniPosto)
					durataPrenotGiorno += getDurataPrenotazione(pp);

				if (durataPrenotGiorno >= durataTotGiorno)
					continue;

				slot.postiDisponibili.add(posto);
			}

			return slot;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public List<GiornoVO> getGrigliaCalendario() throws SbnBaseException {
		List<GiornoVO> giorni = new ArrayList<GiornoVO>();

		LocalDate now = LocalDate.fromDateFields(this.now);
		LocalDate start = LocalDate.fromDateFields(this.inizio);
		LocalDate end = LocalDate.fromDateFields(this.fine);

		//ciclo iniziale (check su posti censiti e calendario biblioteca)
		while (!start.isAfter(end)) {
			GiornoVO g = new GiornoVO();
			Date when = start.toDate();
			g.setDate(when);
			StatoGiorno stato = StatoGiorno.DISPONIBILE;
			if (start.isBefore(now) || !isFilled(posti) || !isPrenotabile(asList(calendarioBib), when, null, null))
				stato = StatoGiorno.NON_DISPONIBILE;

			g.setStato(stato);
			giorni.add(g);
			//giorno successivo
			start = start.plusDays(1);
		}

		//popolamento fasce
		for (GiornoVO g : giorni) {
			if (g.getStato() == StatoGiorno.DISPONIBILE) {
				Date currentDate = g.getDate();
				FasciaVO aperturaChiusuraBib = getOrarioAperturaChiusura(calendarioBib, currentDate);
				if (aperturaChiusuraBib == null) {	//biblioteca chiusa
					g.setStato(StatoGiorno.NON_DISPONIBILE);
					continue;
				}

				if (--giorniRiservati >= 0) {
					//non prenotabile causa preparazione supporto
					g.setStato(StatoGiorno.NON_DISPONIBILE);
					continue;
				}

				//check max prenotazioni utente su servizio
				if (numMaxPrenPosto > 0) {
					if (Stream.of(prenotazioni).filter(byUtenteAndData(utente.getIdUtente(), currentDate)).count() >= numMaxPrenPosto) {
						g.setStato(StatoGiorno.NON_DISPONIBILE);
						continue;
					}
				}

				for (SalaVO sala : sale) {

					int slotTotali = 0;
					int slotDisponibili = 0;
					Set<PostoSalaVO> posti = new HashSet<PostoSalaVO>();
					int s = 0;
					SlotSala slotSala = new SlotSala(new SalaDecorator(sala));

					CalendarioVO calendarioSala = calendariSala.get(sala);
					//orari apertura sala, se diversi da biblioteca
					FasciaVO aperturaChiusuraSala = coalesce(getOrarioAperturaChiusura(calendarioSala, currentDate), aperturaChiusuraBib);
					LocalTime t_start = aperturaChiusuraSala.getInizio();

					while (t_start.isBefore(aperturaChiusuraSala.getFine())) {
						SlotVO slot = new SlotVO(++s);
						LocalTime t_end = t_start.plusMinutes(sala.getDurataFascia());

						//check su fascia che supera giornata
						if (t_end.isBefore(t_start))
							t_end = aperturaChiusuraSala.getFine();

						//ultima fascia maggiore di chiusura biblioteca
						if (t_end.isAfter(aperturaChiusuraSala.getFine()))
							t_end = aperturaChiusuraSala.getFine();

						slot.setInizio(t_start);
						slot.setFine(t_end);
						slotSala.getSlots().add(slot);

						Slot sp = cercaSlotPrenotazione(sala, currentDate, t_start, t_end);
						slotTotali += size(sp.postiTotali);

						int size = size(sp.postiDisponibili);
						if (size == 0) {
							slot.setActive(false);

						} else {
							slot.setActive(true);
							slot.setDescrizione(String.format("posti disponibili: %d", size));
							slot.setPosti(sp.postiDisponibili);
							posti.addAll(sp.postiDisponibili);
							slotDisponibili += size;
						}
						t_start = t_end;

						slotSala.getPosti().addAll(posti);
					}
					slotSala.setSlotDisponibili(slotDisponibili);
					slotSala.setSlotTotali(slotTotali);
					if (slotDisponibili > 0)
						g.getSlotSala().add(slotSala);
				}

				if (g.getSlotDisponibili() < 1)
					g.setStato(StatoGiorno.NON_DISPONIBILE);
				else {
					double disp = 0;//(double)slotDisponibili / (double)slotTotali;
					if (disp <= DISPONIBILITA_BASSA)
						g.setDisponibilita(Disponibilita.BASSA);
					else
						if (disp <= DISPONIBILITA_MEDIA)
							g.setDisponibilita(Disponibilita.BUONA);
						else
							g.setDisponibilita(Disponibilita.ALTA);
				}
			}
		}

		return giorni;
	}

}
