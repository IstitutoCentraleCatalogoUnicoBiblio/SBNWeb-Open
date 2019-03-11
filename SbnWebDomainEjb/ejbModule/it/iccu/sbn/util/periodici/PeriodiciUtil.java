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
package it.iccu.sbn.util.periodici;

import gnu.trove.TIntHashSet;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerComunicazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.periodici.Tbp_fascicolo;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.periodici.policy.CalcoloDataFascicoloPolicy;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.validators.periodici.FascicoloValidator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import ch.lambdaj.group.Group;

public final class PeriodiciUtil {

	private static final SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
	private static final String MSG_RECLAMO = "02";
	private static final String MSG_SOLLECITO = "24";
	private static final String MSG_SOLLECITO_RECLAMO = "25";

	private static final Pattern REGEX_ANNO = Pattern.compile("\\d{4}");
	private static final Pattern REGEX_ANNATA = Pattern.compile("\\d{4}/\\d{4}");

	private static final Comparator<Number> NUMBER_NATURAL_SORT = new Comparator<Number>() {

		public int compare(Number n1, Number n2) {
			return n1.intValue() - n2.intValue();
		}
	};

	public static void impostaStatoFascicoli(SeriePeriodicoType serieType, List<FascicoloVO> listaFascicoli, int elementiPerBlocco) throws Exception {

		int gapThreshold = ValidazioneDati.max( (elementiPerBlocco * 10), 100);
		int size = ValidazioneDati.size(listaFascicoli);
		TIntHashSet keys = new TIntHashSet(size + 1);

		for (int idx = 0; idx < size; idx++ ) {
			FascicoloVO fascicolo = listaFascicoli.get(idx);
			fascicolo.setStato(StatoFascicolo.ATTESO);

			if (isRicevuto(fascicolo)) {
				fascicolo.setStato(StatoFascicolo.RICEVUTO);
				//memorizzo il fid del fascicolo ricevuto
				keys.add(fascicolo.getFid());
				continue;
			}

			//atteso o lacuna?
			int loop = 0;
			for (int idx2 = (idx - 1); idx2 >= 0; idx2--) {
				FascicoloVO altroFasc = listaFascicoli.get(idx2);
				EsemplareFascicoloVO altroEsempl = altroFasc.getEsemplare();
				if (altroEsempl != null
						&& !altroEsempl.isCancellato()
						&& altroEsempl.getData_arrivo() != null
						//almaviva5_20110419 #4382
						//&& fascicolo.getData_conv_pub().before(altroEsempl.getData_arrivo())) {
						&& fascicolo.getData_conv_pub().before(altroFasc.getData_conv_pub())) {
					fascicolo.setStato(StatoFascicolo.LACUNA);
					break;
				}
				//estendo la ricerca della lacuna per min 100 fascicoli o 10 blocchi
				if ((++loop % gapThreshold) == 0)
					break;
			}
		}

		Date now = new Date();

		//secondo giro: stato esteso ed eliminazione duplicati
		Iterator<FascicoloVO> i = listaFascicoli.iterator();
		while (i.hasNext()) {
			FascicoloVO f = i.next();
			EsemplareFascicoloVO ef = f.getEsemplare();
			//se esiste già un fid completo di esemplare elimino l'eventuale
			//riga duplicata legata a esemplare cancellato
			if (keys.contains(f.getFid()) && !isRicevuto(f) ) {
				i.remove();
				continue;
			} else
				keys.add(f.getFid());

			switch (f.getStato()) {
			case ATTESO:
				if (serieType == SeriePeriodicoType.ORDINE) {
					if (f.isSollecitato())
						f.setStato(StatoFascicolo.SOLLECITO);
				} else
					//almaviva5_20110125 per fascicoli non in abbonamento
					f.setStato(StatoFascicolo.DA_ASSOCIARE);
				break;

			case LACUNA:
				if (serieType == SeriePeriodicoType.ORDINE && f.isSollecitato())
					f.setStato(StatoFascicolo.RECLAMO);
				break;

			case RICEVUTO:
				if (ef.getCd_inven() > 0)
					f.setStato(StatoFascicolo.POSSEDUTO);
				if (ValidazioneDati.equals(ef.getCd_no_disp(), 'S'))
					f.setStato(StatoFascicolo.SMARRITO);
				if (ValidazioneDati.equals(ef.getCd_no_disp(), 'R'))
					f.setStato(StatoFascicolo.RILEGATURA);
				break;

			default:
				continue;
			}

			TB_CODICI cod = CodiciProvider.cercaCodice(f.getCd_per(), CodiciType.CODICE_PERIODICITA, CodiciRicercaType.RICERCA_CODICE_SBN);
			if (cod == null)
				continue;

			//gg. ritardo ammessi per tipo periodicità
			String ggRit = cod.getCd_flg3();
			if (!ValidazioneDati.isFilled(ggRit) || !ValidazioneDati.strIsNumeric(ggRit))
				continue;

			Date dtPub = f.getData_conv_pub();
			Date dtLimite = DateUtil.addDay(dtPub, Integer.valueOf(ggRit));
			f.setRitardo(now.after(dtLimite));	//é in ritardo?
		}
	}

	private static boolean isRicevuto(FascicoloVO fascicolo) {
		EsemplareFascicoloVO ef = fascicolo.getEsemplare();
		return (ef != null && !ef.isCancellato());
	}

	public static String formattaNumeroFascicolo(FascicoloVO f) {
		if (ValidazioneDati.isFilled(f.getNum_alter()))
			return f.getNum_alter();

		StringBuilder buf = new StringBuilder(512);
		if (ValidazioneDati.isFilled(f.getNum_in_fasc()))
			buf.append(f.getNum_in_fasc());
		if (ValidazioneDati.isFilled(f.getNum_fi_fasc()))
			buf.append(" - ").append(f.getNum_fi_fasc());

		return buf.toString();
	}

	public static String formattaNumeroTbpFascicolo(Tbp_fascicolo f) {
		if (ValidazioneDati.isFilled(f.getNum_alter()))
			return f.getNum_alter().trim();

		StringBuilder buf = new StringBuilder(512);
		if (ValidazioneDati.isFilled(f.getNum_in_fasc()))
			buf.append(f.getNum_in_fasc());
		if (ValidazioneDati.isFilled(f.getNum_fi_fasc()))
			buf.append(" - ").append(f.getNum_fi_fasc());

		return buf.toString();
	}

	public static void impostaPeriodicita(KardexPeriodicoVO kardex) {

		String cd_per = null;
		List<FascicoloVO> listaFascicoli = kardex.getFascicoli();
		if (!ValidazioneDati.isFilled(listaFascicoli))
			return;

		FascicoloVO p = on(FascicoloVO.class);
		Group<FascicoloVO> gruppi = group(listaFascicoli, by(p.getCd_per()));

		int max = 0;
		for (Group<FascicoloVO> g : gruppi.subgroups())
			if (g.getSize() > max) {
				cd_per = g.first().getCd_per();
				max = g.getSize();
			}

		kardex.setTipoPeriodicita(cd_per);
	}

	public static Date calcolaDataProssimoFascicolo(List<FascicoloVO> ff) throws Exception {

		FascicoloVO f = ff.get(0);
		List<Date> dtPrev = new ArrayList<Date>(ff.size());
		for (int idx = 0; idx < ff.size(); idx++)
			dtPrev.add(ff.get(idx).getData_conv_pub());

		Collections.sort(dtPrev);

		CalcoloDataFascicoloPolicy policy = PrevisionaleUtil.getPolicy(f.getCd_per());

		Date dtPubb = f.getData_conv_pub();
		Date next;
		try {
			//almaviva5_20110627 #4530 fix periodicità irregolare
			next = policy.calcola(f.getCd_per(), dtPubb, dtPrev, false);
		} catch (ApplicationException e) {
			if (e.getErrorCode() == SbnErrorTypes.PER_ERRORE_TIPO_PERIODICITA_NON_GESTITO)
				next = DateUtil.addDay(dtPubb, 1);
			else
				throw e;
		}

		return next;

		/*
		TB_CODICI c = CodiciProvider.cercaCodice(f.getCd_per(), CodiciType.CODICE_PERIODICITA, CodiciRicercaType.RICERCA_CODICE_SBN);
		if (c == null)
			return null;

		String ggPer = c.getCd_flg2();
		int gg = ValidazioneDati.isFilled(ggPer) && ValidazioneDati.strIsNumeric(ggPer) ? Integer.valueOf(ggPer) : 0;
		//almaviva5_20101203 #4030 nel caso di fascicolo multiplo la nuova data va calcolata a partire dalla data finale
		return DateUtil.addDay(f.getData_fi_pubbl() != null ? f.getData_fi_pubbl() : f.getData_conv_pub(), gg);
		*/
	}

	public static final FascicoloVO preparaFascicoloPerRicezione(FascicoloVO f) {
		EsemplareFascicoloVO ef = f.getEsemplare() == null ? new EsemplareFascicoloVO(f) : f.getEsemplare();
		ef.setData_arrivo(ef.getData_arrivo() != null ? ef.getData_arrivo() : new Date() );
		ef.setFlCanc("N");	//per eventuale recupero
		ef.setCd_no_disp(null);

		pulisciChiaviRicezione(ef);

		f.setStato(StatoFascicolo.RICEVUTO);
		f.setEsemplare(ef);

		return f;
	}

	public static final void pulisciChiaviRicezione(EsemplareFascicoloVO ef) {

		ef.setId_ordine(0);
		ef.setCod_bib_ord(null);
		ef.setAnno_ord(0);
		ef.setCod_tip_ord((char) 0);
		ef.setCod_ord(0);

		ef.setCd_serie(null);
		ef.setCd_inven(0);
		ef.setGrp_fasc(null);

		ef.setCd_no_disp(null);
	}

	public static final void checkAnnataFascicoli(List<FascicoloVO> fascicoli) throws ValidationException {

		FascicoloVO param = on(FascicoloVO.class);
		Group<FascicoloVO> gruppi = group(fascicoli, by(param.getAnnata()) );
		int size = gruppi.subgroups().size();
		if (size != 1)	//tutti stessa annata
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_ANNATA_INCONGRUENTE);

		String annata = gruppi.first().getAnnata();
		//se l'annata è vuota tutti i fascicoli devono essere dello stesso anno
		if (!ValidazioneDati.isFilled(annata)) {
			int anno = DateUtil.getYear(gruppi.first().getData_conv_pub());
			for (FascicoloVO f : fascicoli)
				if (anno != DateUtil.getYear(f.getData_conv_pub()))
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_DATA_CONV_INCONGRUENTE);
		}

		//annata valorizzata
		for (FascicoloVO f : gruppi.findAll() ) {
			annata = f.getAnnata();
			if (ValidazioneDati.isFilled(annata)) {
				String[] anni = REGEX_ANNO.matcher(annata).matches() ? new String[] {annata} :
								REGEX_ANNATA.matcher(annata).matches() ? annata.split("/") : null;

				if (anni != null && !ValidazioneDati.in(String.valueOf(DateUtil.getYear(f.getData_conv_pub())), anni))
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_ANNATA_INCONGRUENTE);
			}
		}
	}

	public static final void checkFascicoliPerAssociazione(List<FascicoloVO> fascicoli) throws ValidationException {

		if (!ValidazioneDati.isFilled(fascicoli))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_EMPTY_LIST);

		int id_ordine = 0;
		//tutti i fascicoli devono essere della stessa annata
		checkAnnataFascicoli(fascicoli);

		for (FascicoloVO f : fascicoli) {

			if (!StatoFascicolo.isPosseduto(f.getStato()) )
				continue;
			//se ricevuto devo controllare che non sia legato ad inventario
			//e che, se legato ad ordine, sia lo stesso per tutti i fascicoli
			if (f.getEsemplare().isLegatoInventario())
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_POSSEDUTO, formattaNumeroFascicolo(f));

			int ord = f.getEsemplare().getId_ordine();
			if (ord > 0)
				if (id_ordine > 0 && ord != id_ordine)
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_RICEVUTO, formattaNumeroFascicolo(f));

			id_ordine = ord;
		}
	}

	public static final void checkFascicoliPerRicezioneSuOrdine(List<FascicoloVO> fascicoli) throws ValidationException {

		if (!ValidazioneDati.isFilled(fascicoli))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_EMPTY_LIST);

		//tutti i fascicoli devono essere della stessa annata
		checkAnnataFascicoli(fascicoli);

		for (FascicoloVO f : fascicoli) {

			if (!StatoFascicolo.isPosseduto(f.getStato()) )
				continue;

			if (f.getEsemplare().isLegatoOrdine() )
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_RICEVUTO,
						formattaNumeroFascicolo(f));
		}

	}

	public static final boolean checkFascicoliPerAnnullamentoRicezione(List<FascicoloVO> fascicoli) {

		for (FascicoloVO f : fascicoli)
			if (!StatoFascicolo.isPosseduto(f.getStato()))
				return false;

		return true;
	}

	public static final void associaInventario(FascicoloVO f, InventarioVO inv) {
		if (inv != null) {
			EsemplareFascicoloVO ef = f.getEsemplare();
			pulisciChiaviRicezione(ef);
			if (inv.getCodInvent() > 0) {
				ef.setCodPolo(inv.getCodPolo());
				ef.setCodBib(inv.getCodBib());
				ef.setCd_serie(inv.getCodSerie());
				ef.setCd_inven(inv.getCodInvent());
			}

			if (ValidazioneDati.isFilled(inv.getCodBibO())) {	//ordine associato
				ef.setCod_bib_ord(inv.getCodBibO());
				ef.setAnno_ord(Integer.valueOf(inv.getAnnoOrd()) );
				ef.setCod_tip_ord(inv.getCodTipoOrd().charAt(0));
				ef.setCod_ord(Integer.valueOf(inv.getCodOrd()) );
				ef.setId_ordine(0);
			}
		}
	}

	public static final boolean checkFascicoliPerSollecito(List<FascicoloVO> fascicoli) throws Exception {

		for (FascicoloVO f : fascicoli) {
			if (StatoFascicolo.isPosseduto(f.getStato()))
				return false;

			Date now = new Date(System.currentTimeMillis());

			TB_CODICI cod = CodiciProvider.cercaCodice(f.getCd_per(), CodiciType.CODICE_PERIODICITA, CodiciRicercaType.RICERCA_CODICE_SBN);
			if (cod == null)
				return false;

			//gg. ritardo ammessi per tipo periodicità
			String ggRit = cod.getCd_flg3();
			if (!ValidazioneDati.isFilled(ggRit) || !ValidazioneDati.strIsNumeric(ggRit))
				return false;

			Date dtPub = f.getData_conv_pub();
			Date dtLimite = DateUtil.addDay(dtPub, Integer.valueOf(ggRit));
			if (now.before(dtLimite))
				return false;	//ci sono ancora gg. di tolleranza
		}

		return true;
	}

	public static final String calcolaTipoMessaggioSollecito(List<FascicoloVO> fascicoli) {
		if (!ValidazioneDati.isFilled(fascicoli))
			return "";

		String msg = null;

		for (FascicoloVO f : fascicoli) {
			switch (f.getStato()) {
			case ATTESO:
			case SOLLECITO:
				if (ValidazioneDati.in(msg, MSG_RECLAMO, MSG_SOLLECITO_RECLAMO))
					msg = MSG_SOLLECITO_RECLAMO;
				else
					msg = MSG_SOLLECITO;
				break;
			case LACUNA:
			case RECLAMO:
				if (ValidazioneDati.in(msg, MSG_SOLLECITO, MSG_SOLLECITO_RECLAMO))
					msg = MSG_SOLLECITO_RECLAMO;
				else
					msg = MSG_RECLAMO;
				break;
			default:
				continue;
			}
		}

		return msg;
	}

	public static final void eliminaFascicoliPerComunicazione(RicercaKardexPeriodicoVO<FascicoloVO> richiesta,
			List<FascicoloVO> fascicoli) {
		if (richiesta instanceof RicercaKardexPeriodicoPerComunicazioneVO) {
			ComunicazioneVO com = ((RicercaKardexPeriodicoPerComunicazioneVO<FascicoloVO>)richiesta).getComunicazione();
			String msg = ValidazioneDati.trimOrEmpty(com.getTipoMessaggio());
			Iterator<FascicoloVO> i = fascicoli.iterator();
			while (i.hasNext()) {
				switch (i.next().getStato()) {
				case POSSEDUTO:
				case RICEVUTO:
				case RILEGATURA:
				case SMARRITO:
					i.remove();
					continue;
				case ATTESO:
				case SOLLECITO:
					if (msg.equals(MSG_RECLAMO))
						i.remove();
					continue;
				case LACUNA:
				case RECLAMO:
					if (msg.equals(MSG_SOLLECITO))
						i.remove();
					continue;
				}
			}
		}
	}

	public static final String preparaNoteComunicazioneFascicoli(List<FascicoloVO> fascicoli) {
		StringBuilder buf = new StringBuilder(512);
		Iterator<FascicoloVO> i = fascicoli.iterator();
		while (i.hasNext()) {
			FascicoloDecorator fd = (FascicoloDecorator)i.next();
			buf.append(fd.getDescrizioneFascicolo());
			if (i.hasNext())
				buf.append(", ");
		}
		return buf.toString();
	}

	public static final String preparaConsistenzaFascicoli(List<FascicoloVO> fascicoli) {
		//almaviva5_20111205 #4718
		//es. Vol. 30 (2010): n. 3, 5-6, 10; vol. 31 (2011): n.1, 4.
		fascicoli = new ArrayList<FascicoloVO>(fascicoli);
		Collections.sort(fascicoli, FascicoloDecorator.ORDINAMENTO_FASCICOLO);

		StringBuilder buf = new StringBuilder(512);
		FascicoloVO param = on(FascicoloVO.class);
		Group<FascicoloVO> grpVol = group(fascicoli, by(param.getNum_volume()) );
		//gruppo volume
		Iterator<Group<FascicoloVO>> iv = grpVol.subgroups().iterator();
		while (iv.hasNext()) {
			//gruppo annata
			Group<FascicoloVO> vol = iv.next();
			Group<FascicoloVO> grpAnno = group(vol.findAll(), by(param.getAnnata()) );
			Iterator<Group<FascicoloVO>> ia = grpAnno.subgroups().iterator();
			while (ia.hasNext()) {
				Group<FascicoloVO> annata = ia.next();
				Short num_volume = vol.first().getNum_volume();
				buf.append("Vol. ").append(num_volume != null ? num_volume : "??");
				String annataDescr = annata.first().getAnnata();
				if (ValidazioneDati.isFilled(annataDescr))
					buf.append(" (").append(annataDescr).append("): ");
				else
					buf.append(": ");

				buf.append("n. ").append(preparaElencoFascicoliPerConsistenza(annata.findAll()));
				if (ia.hasNext())
					buf.append("; ");

			}

			if (iv.hasNext())
				buf.append("; ");

		}

		return buf.toString();
	}

	private static String preparaElencoFascicoliPerConsistenza(List<FascicoloVO> ff) {
		//almaviva5_20111205 #4718
		StringBuilder buf = new StringBuilder(512);
		List<String> altriFascicoli = new ArrayList<String>();

		Iterator<FascicoloVO> i = ff.iterator();
		while (i.hasNext()) {
			//i fascicoli "alternativi" sono trattati in coda
			FascicoloVO f = i.next();
			if (ValidazioneDati.isFilled(f.getNum_alter())) {
				altriFascicoli.add(f.getNum_alter());
				i.remove();
			}
		}

		if (ValidazioneDati.isFilled(ff)) {
			FascicoloVO first = ValidazioneDati.first(ff);
			FascicoloVO latest = null;

			FascicoloDecorator fd = new FascicoloDecorator(first);
			buf.append(fd.getNumFascicolo());
			int size = ff.size();

			for (int idx = 1; idx < size; idx++) {
				FascicoloVO prev = ff.get(idx - 1);
				FascicoloVO curr = ff.get(idx);

				if ( (curr.getNum_in_fasc() == (prev.getNum_in_fasc() + 1))
					|| (FascicoloValidator.isMultiplo(prev) && curr.getNum_in_fasc() == (prev.getNum_fi_fasc() + 1)) ) {
					latest = curr;
					if (idx == (size - 1)) //ultimo
						buf.append("-").append(FascicoloValidator.isMultiplo(curr) ? curr.getNum_fi_fasc() : curr.getNum_in_fasc());
				} else {
					if (latest != null)
						buf.append("-").append(FascicoloValidator.isMultiplo(latest) ? latest.getNum_fi_fasc() : latest.getNum_in_fasc());
					buf.append(", ").append(FascicoloValidator.isMultiplo(curr) ? curr.getNum_fi_fasc() : curr.getNum_in_fasc());
					latest = null;
				}
			}
		}
		//si aggiungono in coda i fascicoli "alternativi"
		String altri = ValidazioneDati.formatValueList(altriFascicoli, ", ");
		if (ValidazioneDati.isFilled(altri))
			if (buf.length() > 0)
				buf.append(", ").append(altri);
			else
				buf.append(altri);	//nessun altro fascicolo?

		return buf.toString();
	}

	public static final boolean checkTipoMessaggioComunicazione(String tpMsg) {
		return ValidazioneDati.in(tpMsg,
				MSG_RECLAMO,
				MSG_SOLLECITO,
				MSG_SOLLECITO_RECLAMO);

	}

	public static final void associaFascicoloCreazioneOrdine(OrdiniVO ordine, FascicoloVO f) {
		ordine.setAnnataAbbOrdine(f.getAnnata());
		ordine.setNumVolAbbOrdine(f.getNum_volume() != null ? f.getNum_volume() : 0);
		ordine.setNumFascicoloAbbOrdine(formattaNumeroFascicolo(f));

		Date dtConvPub = f.getData_conv_pub();
		ordine.setDataPubblFascicoloAbbOrdine(DateUtil.formattaData(dtConvPub));

		//fine abbonamento
		String durata = ordine.getPeriodoValAbbOrdine();
		if (ValidazioneDati.isFilled(durata)) {
			Calendar c = Calendar.getInstance();
			c.setTime(dtConvPub);
			c.add(Calendar.YEAR, Integer.valueOf(durata));
			ordine.setDataFineAbbOrdine(DateUtil.formattaData(c.getTime()));
		}

	}

	public static final boolean isFascicoloModificabile(String cod_bib, FascicoloDecorator fd) {
		//posso modificare la desc. bibliografica se sono abilitato e:
		//-non esiste altra biblioteca che ha ricevuto il fid
		//-ho già ricevuto in qualche forma questo fid
		if (fd.isRicevuto())
			return true;

		List<String> listaBib = fd.getListaBibRicezione();
		return !ValidazioneDati.isFilled(listaBib) || listaBib.contains(cod_bib);
	}

	public static final boolean isFascicoloCancellabile(String cod_bib, FascicoloVO f) {
		//posso cancellare il fascicolo se non esiste altra biblioteca che ha ricevuto il fid
		FascicoloDecorator fd = (FascicoloDecorator) f;
		List<String> listaBib = fd.getListaBibRicezione();
		return !StatoFascicolo.isPosseduto(fd.getStato())
			&& (!ValidazioneDati.isFilled(listaBib) || (listaBib.size() == 1 && listaBib.contains(cod_bib)));
	}

	public static final void checkFascicoliPerCancellazione(String cod_bib, List<FascicoloVO> ff) throws ValidationException {
		for (FascicoloVO f : ff)
			if (StatoFascicolo.isPosseduto(f.getStato()) )
				if (f.getEsemplare().isLegatoInventario())
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_POSSEDUTO, formattaNumeroFascicolo(f));
				else
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_RICEVUTO, formattaNumeroFascicolo(f));
			else
				//almaviva5_20111028 #4705
				if (!isFascicoloCancellabile(cod_bib, f) )
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_POSSEDUTO_POLO, formattaNumeroFascicolo(f));
	}

	public static final void checkFascicoliPerAssociaGruppo(List<FascicoloVO> ff) throws ValidationException {

		FascicoloDecorator fd = new FascicoloDecorator(ff.get(0));
		String inv = fd.getChiaveInventario();
		for (int i = 1; i < ff.size(); i++) {
			fd = new FascicoloDecorator(ff.get(i));
			if (!ValidazioneDati.equals(fd.getChiaveInventario(), inv))
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_INVENTARIO_GRUPPO);
		}

		if (!ValidazioneDati.isFilled(inv))
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_INVENTARIO_GRUPPO);
	}

	public static final int getRepeatableId(FascicoloVO f) {
		Date dtPubb = f.getData_conv_pub();
		if (dtPubb == null)
			return -1;

		StringBuilder buf = new StringBuilder(32);
		buf.append(dtFormat.format(dtPubb));
		buf.append(f.getBid()).append(f.getCd_tipo_fasc());
		//almaviva5_20111205 #4753
		String norm = OrdinamentoCollocazione2.normalizza(formattaNumeroFascicolo(f));
		buf.append(norm);

		EsemplareFascicoloVO ef = f.getEsemplare();
		if (ef != null && !ef.isCancellato())
			buf.append(ef.getId_ese_fascicolo());

		return buf.toString().hashCode();
	}

	public static final Date calcolaDataInizioAbb(Tba_ordini o) {
		if (o.getData_fasc() != null)
			return o.getData_fasc();
		if (ValidazioneDati.isFilled(o.getAnno_abb()) )
			return DateUtil.firstDayOfYear(o.getAnno_abb().intValue());

		return DateUtil.firstDayOfYear(o.getAnno_ord().intValue());
	}

	public static final Date calcolaDataFineAbb(Tba_ordini o) {
		if (o.getData_fine() != null)
			return o.getData_fine();
		if (ValidazioneDati.isFilled(o.getAnno_abb()) )
			return DateUtil.lastDayOfYear(o.getAnno_abb().intValue());

		return DateUtil.lastDayOfYear(o.getAnno_ord().intValue());
	}

	public static final FascicoloVO preparaModelloFascicolo(FascicoloVO mod) {

		FascicoloVO f = new FascicoloVO();
		f.setCodPolo(mod.getCodPolo());
		f.setCodBib(mod.getCodBib());
		f.setBid(mod.getBid());
		f.setCd_per(mod.getCd_per());
		f.setCd_tipo_fasc(mod.getCd_tipo_fasc());
		f.setAnnata(mod.getAnnata());
		f.setNum_volume(mod.getNum_volume());

		return f;
	}

	public static String formattaAnnateFascicolo(List<? extends Number> rangeAnnoPubb) {
		if (!ValidazioneDati.isFilled(rangeAnnoPubb))
			return null;

		Collections.sort(rangeAnnoPubb, NUMBER_NATURAL_SORT);
		Number first = ValidazioneDati.first(rangeAnnoPubb);

		int size = ValidazioneDati.size(rangeAnnoPubb);
		if (size == 1)
			return String.valueOf(first);

		int latest = 0;
		StringBuilder buf = new StringBuilder(512);
		buf.append(first);
		for (int i = 1; i < size; i++) {
			int prev = rangeAnnoPubb.get(i - 1).intValue();
			int curr = rangeAnnoPubb.get(i).intValue();
			if (curr == (prev + 1)) {
				latest = curr;
				if (i == (size - 1)) //ultimo
					buf.append("-").append(curr);
			} else {
				if (latest > 0)
					buf.append("-").append(latest);
				buf.append(", ").append(curr);
				latest = 0;
			}
		}

		return buf.toString();
	}

}
