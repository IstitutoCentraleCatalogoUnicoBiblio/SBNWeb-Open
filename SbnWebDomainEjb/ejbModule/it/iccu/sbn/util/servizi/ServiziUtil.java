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
package it.iccu.sbn.util.servizi;

import static it.iccu.sbn.web.constant.ServiziConstant.NUM_PAGINE_ERROR;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Isil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class ServiziUtil {

	protected static Logger log = Logger.getLogger(ServiziUtil.class);

	public static final String espandiCodUtente(final String original_cdute) {
		if (!ValidazioneDati.isFilled(original_cdute))
			return null;

		//almaviva5_20150825 #5957
		//codUtente = codUtente.toUpperCase().replaceAll("\\s+", "");
		String processed_cdute = original_cdute.trim();
		int length = processed_cdute.length();
		if (length < 3 || length >= 12)
			return original_cdute;

		String codBib = processed_cdute.substring(0, 2);
		String parteNumerica = processed_cdute.substring(2);
		if (!ValidazioneDati.strIsNumeric(parteNumerica))
			return original_cdute;

		String format = String.format("%2s%010d", codBib.trim(), Integer.parseInt(parteNumerica) ).toUpperCase();
		log.debug("Cod utente espanso: " + original_cdute + " --> " + format);
		return format;
	}

	public static final String formattaSegnaturaCollocazione(
			Tbc_inventario inv, BigDecimal annoPeriodico) {
		if (inv != null && inv.getKey_loc() != null) {
			Tbc_collocazione key_loc = inv.getKey_loc();
			return formattaSegnaturaCollocazione(key_loc.getCd_sez().getCd_sez(),
					key_loc.getCd_loc(), key_loc.getSpec_loc(),
					inv.getSeq_coll(), inv.getB().getCd_natura(),
					annoPeriodico);
		}

		return "";

	}

	public static final String formattaSegnaturaCollocazione(String cd_sez,
			String cd_loc, String spec_loc, String seq_coll, char natura,
			BigDecimal annoPeriodico) {

		StringBuilder buf = new StringBuilder(64);
		// Sezione
		if (ValidazioneDati.isFilled(cd_sez))
			buf.append(ValidazioneDati.trimOrEmpty(cd_sez));

		// Collocazione
		if (ValidazioneDati.isFilled(cd_loc))
			buf.append(" ").append(ValidazioneDati.trimOrEmpty(cd_loc));

		// Specificazione
		if (ValidazioneDati.isFilled(spec_loc))
			buf.append(" ").append(ValidazioneDati.trimOrEmpty(spec_loc));

		// Numero di sequenza
		if (ValidazioneDati.isFilled(seq_coll))
			buf.append("/").append(ValidazioneDati.trimOrEmpty(seq_coll));

		if (ValidazioneDati.isFilled(annoPeriodico) && natura == 'S') // anno periodico
			buf.append(" [").append(annoPeriodico.intValue()).append("]");

		return ValidazioneDati.trimOrEmpty(buf.toString());
	}

	public static final String formattaSegnaturaCollocazione(Tbl_richiesta_servizio req) {

		Tbc_inventario inv = req.getCd_polo_inv();

		if (inv != null)
			return formattaSegnaturaCollocazione(inv, req.getAnno_period());

		return "";
	}

	public static final String formattaChiaveDocumento(Tbl_richiesta_servizio req) {

		StringBuilder buf = new StringBuilder();
		Tbc_inventario inv = req.getCd_polo_inv();
		Tbl_esemplare_documento_lettore ese = req.getId_esemplare_documenti_lettore();

		if (inv != null) {

			Tbc_serie_inventariale serie = inv.getCd_serie();
			buf.append(serie.getCd_polo().getCd_biblioteca());
			String cdSerie = serie.getCd_serie();
			if (ValidazioneDati.isFilled(cdSerie))
				buf.append(' ').append(cdSerie);
			buf.append(' ').append(inv.getCd_inven());
		}

		if (ese != null) {
			Tbl_documenti_lettori doc = ese.getId_documenti_lettore();
			buf.append(doc.getCd_bib().getCd_biblioteca());
			buf.append(' ').append(doc.getTipo_doc_lett()).append(doc.getCod_doc_lett());
			buf.append('-').append(ese.getPrg_esemplare());
		}

		return ValidazioneDati.trimOrEmpty(buf.toString());
	}

	public static RifiutaPrenotazioniScaduteVO buildRifiutaPrenotazioniScaduteVO(String codPolo, String codBib, String utente) {
		RifiutaPrenotazioniScaduteVO richiesta = new RifiutaPrenotazioniScaduteVO();
		richiesta.setCodPolo(codPolo);
		richiesta.setCodBib(codBib);
		richiesta.setUser(utente);
		//data impostata alla fine del giorno precedente
		richiesta.setDataFinePrevista(DateTime.now().withTimeAtStartOfDay().minusMillis(1).toDate());
		richiesta.setCodAttivita(CodiciAttivita.getIstance().SRV_RIFIUTO_PRENOTAZIONI_SCADUTE);

		return richiesta;
	}

	public static final int getNumeroPaginePerRiproduzione(final String intervalloCopie) throws ValidationException {

		if (!ValidazioneDati.isFilled(intervalloCopie))
			return 0;

		// se l'intervallo copia è impostato

		// verifico che nella stringa non siano presenti
		// più numeri contenuti tra uno più spazi
		if (intervalloCopie.matches(".*[0-9]+\\s+[0-9]+.*"))
			// intervallo copia non impostato correttamente: impostare numeri di pagina
			// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
			//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
			return NUM_PAGINE_ERROR;

		// inizializzo il campo che conterrà
		// il totale delle pagine relative all'intervallo copia
		int totalePagine = 0;


		// elimino tutti gli spazi presenti nella stringa
		String pagine = intervalloCopie.replaceAll("\\s+", "");

		if (!ValidazioneDati.strIsNumeric(pagine.substring(pagine.length() - 1 ,pagine.length())))
			// se l'ultimo carattere della stringa non è un numero
			// intervallo copia non impostato correttamente: impostare numeri di pagina
			// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
			//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
			return NUM_PAGINE_ERROR;

		// creo una lista contenente tutti gli intervalli
		// impostati nel campo pagina (es: intervallo "5-6", intervallo "8", intervallo "15-19")
		List<String> intervalli = Arrays.asList(pagine.split(","));

		if (intervalli.isEmpty())
			// intervallo copia non impostato correttamente: impostare numeri di pagina
			// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
			//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
			return NUM_PAGINE_ERROR;

		Iterator<String> intervallo = intervalli.iterator();
		String[] pagineIntervalli = null;
		int pagina_1;
		int pagina_2;
		String appoStr;
		while (intervallo.hasNext()) {
			// per ogni intervallo creo un altro array contenente
			// le due pagine, se presenti
			// o la singola pagina dell'intervallo
			appoStr = intervallo.next();
			pagineIntervalli = appoStr.split("-");
			if (pagineIntervalli.length == 0)
				// intervallo copia non impostato correttamente: impostare numeri di pagina
				// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
				//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
				return NUM_PAGINE_ERROR;

			if (pagineIntervalli.length > 2)
				// intervallo copia non impostato correttamente: impostare numeri di pagina
				// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
				//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
				return NUM_PAGINE_ERROR;

			if (pagineIntervalli.length == 1) {
				if (appoStr.contains("-"))
					// intervallo copia non impostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
					return NUM_PAGINE_ERROR;

				if (ValidazioneDati.notEmpty(pagineIntervalli[0]) &&
					ValidazioneDati.strIsNumeric(pagineIntervalli[0])) {
					// se la pagina è impostata ed è numerica
					pagina_1 = Integer.valueOf(pagineIntervalli[0]);
					totalePagine = totalePagine + 1;
				} else
					// intervallo copia non impostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
					return NUM_PAGINE_ERROR;

			}

			if (pagineIntervalli.length == 2) {
				pagina_1 = 0;
				pagina_2 = 0;
				if (ValidazioneDati.notEmpty(pagineIntervalli[0]) &&
					ValidazioneDati.strIsNumeric(pagineIntervalli[0])) {
					pagina_1 = Integer.valueOf(pagineIntervalli[0]);
				} else
					// intervallo copia non impostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
					return NUM_PAGINE_ERROR;

				if (ValidazioneDati.notEmpty(pagineIntervalli[1]) &&
						ValidazioneDati.strIsNumeric(pagineIntervalli[1])) {
						pagina_2 = Integer.valueOf(pagineIntervalli[1]);
					} else
						// intervallo copia non impostato correttamente: impostare numeri di pagina
						// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
						//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
						return NUM_PAGINE_ERROR;

				if (pagina_2 < pagina_1)
					// intervallo copia non impostato correttamente: impostare numeri di pagina
					// o intervalli di pagine separati da virgole (es: 1-3,5,8-12)
					//throw new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE);
					return NUM_PAGINE_ERROR;

				totalePagine = totalePagine + (pagina_2 - pagina_1 + 1);

			}

		}

		return totalePagine;
	}

	public static final EsemplareDocumentoNonSbnVO selezionaEsemplareAttivo(final DocumentoNonSbnVO docNoSbn) {
		//almaviva5_20130319 #5286
		return Stream.of(docNoSbn.getEsemplari()).filter(new Predicate<EsemplareDocumentoNonSbnVO>() {
			public boolean test(EsemplareDocumentoNonSbnVO esempl) {
				return !ValidazioneDati.in(esempl.getFlCanc(), "s", "S");
			}
		}).findFirst().orElse(null);
	}

	public static final String getEmailUtente(UtenteBibliotecaVO u) {
		//almaviva5_20150430
		AnagrafeVO anag = (u != null) ? u.getAnagrafe() : null;
		String email1 = anag != null ? anag.getPostaElettronica() : null;
		String email2 = anag != null ? anag.getPostaElettronica2() : null;
		return ValidazioneDati.isFilled(email1) ? email1 : email2;
	}

	public static final String getEmailUtente(UtenteWeb u) {
		//almaviva5_20150430
		String email1 = u != null ? u.getPostaElettronica() : null;
		String email2 = u != null ? u.getPostaElettronica2() : null;
		return ValidazioneDati.isFilled(email1) ? email1 : email2;
	}

	public static final String getEmailUtente(UtenteBaseVO u) {
		//almaviva5_20151203
		String email1 = u != null ? u.getMail1() : null;
		String email2 = u != null ? u.getMail2() : null;
		return ValidazioneDati.isFilled(email1) ? email1 : email2;
	}

	public static void controllaProroga(MovimentoVO mov,
			ParametriBibliotecaVO parBib, ServizioBibliotecaVO servizio, IterServizioVO iter) throws ApplicationException {
		//check stati del movimento
		boolean ok = iter.isRinnovabile() && mov.getCodStatoMov().equals("A") && !ValidazioneDati.in(mov.getCodStatoRic(), "S", "P");
		if (!ok)
			throw new ApplicationException(SbnErrorTypes.SRV_RICHIESTA_NON_PROROGABILE);

		//check rinnovi
		short numRinnoviGiaEffettuati = mov.getNumRinnovi();
		ok = (numRinnoviGiaEffettuati < MovimentoVO.MAX_RINNOVI);
		if (!ok)
			throw new ApplicationException(SbnErrorTypes.SRV_RICHIESTA_NON_PROROGABILE);

		short durataMassimaRinnovo = 0;
		short numeroRinnovo = (short)(numRinnoviGiaEffettuati+1);

		switch(numeroRinnovo) {
		case 1:
			durataMassimaRinnovo = servizio.getDurMaxRinn1();
			break;
		case 2:
			durataMassimaRinnovo = servizio.getDurMaxRinn2();
			break;
		case 3:
			durataMassimaRinnovo = servizio.getDurMaxRinn3();
			break;
		default:
			throw new ApplicationException(SbnErrorTypes.SRV_RICHIESTA_NON_PROROGABILE);
		}

		ok = (durataMassimaRinnovo > 0);
		if (!ok)
			throw new ApplicationException(SbnErrorTypes.SRV_RICHIESTA_NON_PROROGABILE);

		Timestamp now = DaoManager.now();
		Date dataFinePrevista = mov.getDataFinePrev();

		//data proroga = alla data scadenza del servizio + i giorni di rinnovo
		Date dataProrogaMax = DateUtil.addDay(dataFinePrevista, durataMassimaRinnovo);

		ok = (dataProrogaMax.after(DateUtils.truncate(now, Calendar.DAY_OF_MONTH)) );
		if (!ok)
			throw new ApplicationException(SbnErrorTypes.SRV_RICHIESTA_NON_PROROGABILE);

		//controllo quanti giorni prima della scadenza prevista può essere richiesta la proroga
		short ggRinnovoRichiesta = parBib.getGgRinnovoRichiesta();
		Date dataLimite = DateUtil.addDay(dataFinePrevista, -ggRinnovoRichiesta);
		ok = (ggRinnovoRichiesta == 0) || (now.after(DateUtils.truncate(dataLimite, Calendar.DAY_OF_MONTH)) );
		if (!ok)
			throw new ApplicationException(SbnErrorTypes.SRV_RICHIESTA_NON_PROROGABILE);

	}

	public static Timestamp calcolaDataFinePrevista(ServizioBibliotecaVO servizio, Timestamp dataInizio) throws Exception {
		Short durata = servizio.getDurMov();	//durata in gg del servizio
		if (durata > 1)
			return DateUtil.addDay(dataInizio, durata);

		//check per servizio che si conclude in giornata
		CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(servizio.getCodTipoServ(),
				CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
		if (ts == null)
			throw new ApplicationException(SbnErrorTypes.SRV_TIPO_SERVIZIO_NON_TROVATO);

		//richiesta ICCU del 2015-11-20:
		//solo per consultazione il servizio di durata 1 giorno non deve superare la data odierna
		if (ts.isConsultazione())
			return DateUtil.withTimeAtEndOfDay(dataInizio); //stesso giorno alle 23:59:59
		else
			return DateUtil.addDay(dataInizio, durata);
	}

	public static IterServizioVO trovaAttivitaServizioChePrecede(List<IterServizioVO> listaIter, final String codAttivita) {
		//restituisce il passo iter immediatamente precedente al cod.attivita fornito
		IterServizioVO primoIter = ValidazioneDati.first(listaIter);
		IterServizioVO iter = Stream.of(listaIter).filter(new Predicate<IterServizioVO>() {
			public boolean test(IterServizioVO iter) {
				return iter.getCodAttivita().equals(codAttivita);
			}
		}).findFirst().orElse(null);

		if (iter == null || iter == primoIter)
			return primoIter;

		int idx = listaIter.indexOf(iter);
		return listaIter.get(--idx);	//iter precedente
	}

	public static Timestamp calcolaDataInizioPrevista(
			ParametriBibliotecaVO parametri, ServizioBibliotecaVO servizioVO,
			Timestamp dataRichiesta) {
		//almaviva5_20160408 #6150
		// se presente (>0) nel servizio (diritto) il numero massimo di giorni in cui il documento
		// può essere riservato
		Timestamp dataInizioPrev;
		if (servizioVO.getMaxGgAnt() > 0)
			// la data di inizio prevista è data dalla somma della data attuale con
			// il numero massimo di giorni in cui il documento può essere riservato
			// preso dal servizio (diritto)
			dataInizioPrev = DateUtil.addDay(dataRichiesta, servizioVO.getMaxGgAnt());
		else {
			// altrimenti, se il numero massimo di giorni in cui il documento può essere riservato
			// preso dai parametri di biblioteca è > 0
			if (parametri.getGgValiditaPrelazione() > 0) {
				// la data di inizio prevista è data dalla somma della data attuale con
				// il numero massimo di giorni in cui il documento può essere riservato
				// preso dai parametri di biblioteca
				dataInizioPrev = DateUtil.addDay(dataRichiesta, parametri.getGgValiditaPrelazione());
			} else {
				// la data di inizio prevista è la data corrente
				dataInizioPrev = dataRichiesta;
			}
		}

		return dataInizioPrev;
	}

	static Pattern PATTERN_ISIL = Pattern.compile("[A-Z]{2}\\d{4}");	//cod. provincia + 4 cifre numeriche

	public static String formattaIsil(String idb, String cd_paese) {
		if (!ValidazioneDati.isFilled(idb))
			return idb;

		idb = idb.trim();
		//check cod paese
		String[] tokens = idb.split("\\u002D");	//trattino
		switch (tokens.length) {
		case 1:	//senza codice
			if (PATTERN_ISIL.matcher(idb).matches())
				return cd_paese + '\u002D' + idb;
			else
				return idb;

		default:
			return idb;
		}
	}

	public static String shortIsil(String idb) {
		//check cod paese
	/*
		String[] tokens = idb.split("\\u002D");
		switch (tokens.length) {
		case 1:	//senza codice
			return idb;
		default:
			return tokens[1];
		}
	*/
		return Isil.parse(idb).getSuffisso();
	}

	public static List<ServizioBibliotecaVO> getServiziLocaliLegatiTipoServizioILL(List<ServizioBibliotecaVO> diritti,
			final ILLServiceType illSrv) throws Exception {
		//cerca i diritti utente che appartengono al servizio locale mappato sul servizio ILL in input
		return Stream.of(diritti).filter(new Predicate<ServizioBibliotecaVO>() {
			public boolean test(ServizioBibliotecaVO srv) {
				return srv.isILL() && ILLServiceType.valueOf(srv.getCodServizioILL()) == illSrv;
			}
		}).toList();
	}

	public static String generaFakeSegnaturaDocIll(String tid, RuoloBiblioteca ruolo, String requesterId,
			String responderId) {
		StringBuilder buf = new StringBuilder(64);
/*
		buf.append(tid).append(';').append(ruolo.getFl_ruolo()).append(';')
				.append(requesterId).append(';').append(responderId)
				.append(';').append(IdGenerator.getId());
		return "ILL_" + UUID.nameUUIDFromBytes(buf.toString().getBytes());
*/
		buf.append(String.format("<richiesta n.%s, coll. non fornita>", tid));
		return buf.toString();
	}

	public static List<BibliotecaVO> controllaPrioritaBibliotecheILL(List<BibliotecaVO> biblioteche, int max) throws ValidationException {
		List<BibliotecaVO> bibliotecheScelte = Stream.of(biblioteche).filter(new Predicate<BibliotecaVO>() {
			public boolean test(BibliotecaVO bib) {
				return bib.getPriorita() > 0;
			}
		}).toList();

		if (ValidazioneDati.size(bibliotecheScelte) > max)
			throw new ValidationException(SbnErrorTypes.SRV_ILL_TROPPE_BIBLIOTECHE_SELEZIONATE, Integer.toString(max) );

		Set<Integer> priorita = new HashSet<Integer>();
		for (BibliotecaVO bib : bibliotecheScelte) {
			if (priorita.contains(bib.getPriorita()))
				throw new ValidationException(SbnErrorTypes.SRV_ILL_PRIORITA_BIB_DUPLICATA);
			priorita.add(bib.getPriorita());
		}

		return bibliotecheScelte;
	}

	public static void calcolaCostoServizio(MovimentoVO mov, SupportoBibliotecaVO supp,
			TariffeModalitaErogazioneVO tariffa)
			throws ValidationException, NumberFormatException, ParseException {

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
		double costoServizio = 0;
		if (supp == null) {
			costoServizio = (mov.getNumPezziShort() * tariffa.getCostoUnitarioDouble()) + tariffa.getTarBaseDouble();

		} else {
			costoServizio = (mov.getNumPezziShort() * supp.getImportoUnitarioDouble()) + supp.getCostoFissoDouble()
					+ (mov.getNumPezziShort() * tariffa.getCostoUnitarioDouble()) + tariffa.getTarBaseDouble();
		}
		mov.setCostoServizio(costoServizio);
	}

	public static void main(String[] args) throws ValidationException {
		String intervalloCopie="1-3,5,8-12";
		System.out.println(intervalloCopie + " --> " + getNumeroPaginePerRiproduzione(intervalloCopie));
		System.out.println('#' + espandiCodUtente("IC  3457 ") + '#');
		System.out.println(generaFakeSegnaturaDocIll("49259", RuoloBiblioteca.FORNITRICE, "IT-RM0280", "IT-RM0267"));
	}

}
