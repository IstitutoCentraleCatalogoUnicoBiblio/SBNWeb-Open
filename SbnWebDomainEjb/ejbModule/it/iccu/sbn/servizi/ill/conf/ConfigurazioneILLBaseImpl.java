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
package it.iccu.sbn.servizi.ill.conf;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;

import org.apache.log4j.Logger;

public abstract class ConfigurazioneILLBaseImpl implements ConfigurazioneILL {

	static Logger log = Logger.getLogger(ConfigurazioneILL.class);

	public ConfigurazioneILLBaseImpl() {
		super();
	}

	static public class AttivitaBuilder {

		private final String codBib;
		private final String servizioLoc;


		public AttivitaBuilder(String codBib, String servizioLoc) {
			this.codBib = codBib;
			this.servizioLoc = servizioLoc;
		}

		private IterServizioVO preparaIter(StatoIterRichiesta stato, String statoMov, String statoRic)
				throws Exception {

			statoMov = coalesce(statoMov, "A");
			statoRic = coalesce(statoRic, "G");
			String statoIso = stato.getISOCode();

			IterServizioVO iter = new IterServizioVO();
			iter.setCodBib(codBib);
			iter.setCodTipoServ(servizioLoc);
			iter.setCodAttivita(statoIso);
			iter.setObbl("N");
			iter.setCodStatoMov(statoMov);
			iter.setCodStatoRich(statoRic);
			iter.setDescrizione(String.format("%s (%s)", CodiciProvider
					.getDescrizioneCodiceSBN(CodiciType.CODICE_STATO_RICHIESTA_ILL,
							statoIso), "ILL"));//statoIso));

			return iter;
		}

		public AttivitaServizioVO attivita(StatoIterRichiesta stato) throws Exception {
			return attivita(stato, null, null);
		}

		public AttivitaServizioVO attivita(StatoIterRichiesta stato, String statoMov, String statoRic)
				throws Exception {

			AttivitaServizioVO attivita = new AttivitaServizioVO();

			IterServizioVO iter = preparaIter(stato, statoMov,	statoRic);
			attivita.setPassoIter(iter);

			return attivita;
		}

	}

	protected List<AttivitaServizioVO> pulisciAttivitaLocali(
			List<AttivitaServizioVO> listaAttivita) {
		List<StatoIterRichiesta> emptyList = Collections.emptyList();
		return pulisciAttivitaLocali(listaAttivita, emptyList);
	}

	protected List<AttivitaServizioVO> pulisciAttivitaLocali(
			List<AttivitaServizioVO> listaAttivita,
			List<StatoIterRichiesta> statiLocaliAmmessi) {
		// si eliminano gli stati locali non ammessi al passo ILL corrente
		Iterator<AttivitaServizioVO> i = listaAttivita.iterator();
		while (i.hasNext()) {
			AttivitaServizioVO attivita = i.next();
			StatoIterRichiesta stato = StatoIterRichiesta.of(attivita.getCodAttivita());

			//gli stati custom non vengono eliminati, la gestione verrà demandata ai
			//bibliotecari
			if (stato != StatoIterRichiesta.CUSTOM) {
				boolean locale = !stato.isILL();
				if (locale && !statiLocaliAmmessi.contains(stato))
					i.remove();
			}
		}
		return listaAttivita;
	}

	protected AttivitaServizioVO get(List<AttivitaServizioVO> listaAttivita,
			StatoIterRichiesta stato) {
		for (AttivitaServizioVO as : listaAttivita)
			if (StatoIterRichiesta.of(as.getCodAttivita()) == stato)
				return as;

		return null;
	}

	protected boolean contiene(List<AttivitaServizioVO> listaAttivita,
			StatoIterRichiesta stato) {

		return (get(listaAttivita, stato) != null);
	}

	//Info ricavabili SOLO dalle note del messaggio
	//F129		Data di scadenza: 2017-01-30
	//F115		Data di nuova scadenza proposta: 30/01/2017
	//F127		Data di spedizione: 05/12/2016 Data di scadenza: 2017-01-04 Costo: EUR 9,00


	public void controllaCambiamentoDiStato(MovimentoVO mov,
			DatiRichiestaILLVO datiILL, ServizioBibliotecaVO servizio,
			StatoIterRichiesta _old, StatoIterRichiesta _new) throws Exception {
		// controlla se la richiesta ha subito un cambiamento di stato, in questo caso la richiesta
		// locale deve essere avanzata di conseguenza
		if (_old == _new)
			return;

		RuoloBiblioteca ruolo = datiILL.getRuolo();
		Timestamp now = DaoManager.now();

		switch (_new) {
		case F118_INVIO_A_BIB_DESTINATARIA:
			if (datiILL.getDataInizio() == null)
				datiILL.setDataInizio(now);
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				datiILL.setDataFine(null);	//in caso fosse stata in precedenza annullata
				mov.setCodStatoMov("A");	//attivo
				mov.setCodStatoRic("C");	//inoltrata a bib. forn.
			}
			break;

		case F1212_RICHIESTA_NON_SODDISFACIBILE:
		case F1221_CONFERMA_ANNULLAMENTO:
		case F1218_TERMINE_SCADUTO:
			//mov rifiutato
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				datiILL.setDataFine(now);
				if (!ILLConfiguration2.getInstance().isRichiestaInoltrabileAltraBib(datiILL) ) {
					mov.setCodStatoMov("C");	//chiuso
					mov.setCodStatoRic("B");	//rifiutato
				}

				aggiornaNoteBibliotecaPerRifiuto(mov, datiILL);
			}
			if (ruolo == RuoloBiblioteca.FORNITRICE) {
				datiILL.setDataFine(now);
				mov.setCodStatoMov("C");	//chiuso
				mov.setCodStatoRic("B");	//rifiutato
			}
			break;

		case F128_RICEZIONE_MATERIALE:
			//doc rientrato in bib. fornitrice
			datiILL.setDataFine(now);
		/*
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				mov.setDataFineEff(now);
				mov.setCodStatoMov("C");	//chiuso
				mov.setCodStatoRic("H");	//conclusa
			}
		*/
			break;

		case F129_CONFERMA_RINNOVO:
			//proroga accettata
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				mov.setCodStatoRic("N");	//prorogata
				//recupero data da note messaggio
				Date dataFinePrev = datiILL.getDataRinnovo();
				if (dataFinePrev == null)
					dataFinePrev = estraiData(datiILL.getUltimoMessaggio(), "Data di scadenza: ");
				if (dataFinePrev != null) {
					datiILL.setDataScadenza(dataFinePrev);
					//la data fine prevista è imposta dalla bib. fornitrice - gg. riservati dalla richiedente per la restituzione
					short ggRestituzioneRichiedente = (servizio != null) ? servizio.getGiorniRestituzioneRichiedente() : 0;
					Date dataRestituzione = DateUtil.addDay(dataFinePrev, -ggRestituzioneRichiedente);
					if (dataRestituzione.after(now))
						mov.setDataFinePrev(DateUtil.withTimeAtEndOfDay(dataRestituzione));
					else
						mov.setDataFinePrev(DateUtil.withTimeAtEndOfDay(dataFinePrev));

				} else
					log.warn(String.format(
							"ATTENZIONE: richiesta ill n. %d: nuova data fine prevista non comunicata dal fornitore",
							datiILL.getTransactionId()));
			}
			break;

		case F12A_NEGAZIONE_RINNOVO:
			//proroga rifiutata
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				mov.setCodStatoRic("S");	//non prorogata
			}
			break;

		case F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA:
			if (ruolo == RuoloBiblioteca.FORNITRICE) {
				mov.setCodStatoMov("C");	//chiuso
				mov.setCodStatoRic("B");	//rifiutato
			}
			break;

		case F115_RICHIESTA_DI_RINNOVO_PRESTITO:
			if (ruolo == RuoloBiblioteca.FORNITRICE) {
				mov.setCodStatoRic("P");	//in attesa di proroga

				Date dataProroga = null;
				Date dataRinnovo = datiILL.getDataRinnovo();
				if (dataRinnovo != null)
					dataProroga = dataRinnovo;
				else
					//recupero data da note messaggio
					dataProroga = estraiData(datiILL.getUltimoMessaggio(), "Data di nuova scadenza proposta: ");

				if (dataProroga != null)
					mov.setDataProroga(new java.sql.Date(dataProroga.getTime()));
				else
					log.warn(String.format(
							"ATTENZIONE: richiesta ill n. %d: data proroga desiderata non comunicata dal richiedente",
							datiILL.getTransactionId()));
			}
			break;

		case F127_SPEDIZIONE_MATERIALE:
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				//la data fine prevista è imposta dalla bib. fornitrice - gg. riservati dalla richiedente per la restituzione
				Date dataScadenza = datiILL.getDataScadenza();
				if (dataScadenza == null)
					dataScadenza = estraiData(datiILL.getUltimoMessaggio(), "Data di scadenza: ");
				if (dataScadenza == null) {
					dataScadenza = ValidazioneDati.<Date>max(mov.getDataInizioPrev(), mov.getDataInizioEff());
					log.warn("Data scadenza non impostata. Viene generato un default: " + dataScadenza.toString());
				}

				Timestamp dataFinePrev = DateUtil.withTimeAtEndOfDay(dataScadenza);
				short ggRestituzioneRichiedente = (servizio != null) ? servizio.getGiorniRestituzioneRichiedente() : 0;
				Timestamp dataRestituzione = DateUtil.addDay(dataFinePrev, -ggRestituzioneRichiedente);
				if (dataRestituzione.after(now))
					mov.setDataFinePrev(dataRestituzione);
				else
					mov.setDataFinePrev(dataFinePrev);

				//costo imposto dalla fornitrice
				Number importo = datiILL.getImporto();
				if (!ValidazioneDati.isFilled(importo))
					importo = estraiCosto(datiILL.getUltimoMessaggio(), "Costo: EUR ");
				datiILL.setImporto(importo);
			}
			break;

		case F1215_PRENOTAZIONE_DOCUMENTO:
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				//ricalcolo data inizio/fine prevista come comunicato da fornitrice
				//recupero data da note messaggio
				Date dataDisponibilita = coalesce(datiILL.getDataRientroPrevisto(), estraiData(datiILL.getUltimoMessaggio(), "Disponibile dal: ") );
				if (dataDisponibilita != null) {
					Timestamp dataInizioPrev = new Timestamp(dataDisponibilita.getTime());
					mov.setDataInizioPrev(dataInizioPrev);
					mov.setDataFinePrev(ServiziUtil.calcolaDataFinePrevista(servizio, dataInizioPrev));

				} else
					log.warn(String.format(
							"ATTENZIONE: richiesta ill n. %d: data disponibilità non comunicata dal richiedente",
							datiILL.getTransactionId()));
			}
			break;

		case F116A_PERDITA_DEL_MATERIALE_PRESTATO:
			datiILL.setDataFine(now);
			if (ruolo == RuoloBiblioteca.RICHIEDENTE) {
				mov.setCodStatoMov("C");	//chiuso
				mov.setCodStatoRic("H");	//conclusa
			}
			break;

		case F127A_STIMA_DEI_COSTI: {
			datiILL.setDataFine(now);
			mov.setCodStatoMov("C");	//chiuso
			mov.setCodStatoRic("H");	//conclusa
			break;
		}

		default:
			break;
		}

	}

	protected Date estraiData(MessaggioVO msg, String template) {
		if (msg == null)
			return null;

		if (!ValidazioneDati.isFilled(template))
			return null;

		String note = msg.getNote();
		if (!ValidazioneDati.isFilled(note))
			return null;

		int start = StringUtils.indexOfIgnoreCase(note, template) + template.length();
		String snippet = StringUtils.substring(note, start, start + 10);
		Date date = ValidazioneDati.coalesce(DateUtil.toDate(snippet), DateUtil.toDateISO(snippet));

		return date;
	}

	protected BigDecimal estraiCosto(MessaggioVO msg, String template) {
		if (msg == null)
			return BigDecimal.ZERO;

		if (!ValidazioneDati.isFilled(template))
			return BigDecimal.ZERO;

		String note = msg.getNote();
		if (!ValidazioneDati.isFilled(note))
			return BigDecimal.ZERO;

		int start = StringUtils.indexOfIgnoreCase(note, template) + template.length();
		String snippet = StringUtils.substring(note, start).replace(',', '.');
		BigDecimal amount = BigDecimal.ZERO;
		try {
			amount = new BigDecimal(snippet);
		} catch (NumberFormatException e) {
			log.error("", e);
		}

		return amount;
	}

	protected void aggiornaNoteBibliotecaPerRifiuto(MovimentoVO mov, DatiRichiestaILLVO datiILL) {
		//almaviva5_20160517
		//Al rifiuto della bib. Fornitrice il messaggio ill (se presente) va copiato nelle note del bibliotecario
		//del mov. locale della bib. Richiedente che sarà posto in stato chiuso/respinto.
		//In caso di assenza del messaggio le note saranno valorizzate automaticamente con
		//"Richiesta respinta dalla biblioteca xxx"
		MessaggioVO msg = datiILL.getUltimoMessaggio();
		StringBuilder noteBib = new StringBuilder(ValidazioneDati.trimOrEmpty(mov.getNoteBibliotecario()) );
		if (msg != null && ValidazioneDati.isFilled(msg.getNote())) {
			noteBib.append(' ').append(msg.getNote()).append('.');
		} else
			noteBib.append(' ')
					.append("Richiesta respinta dalla biblioteca ")
					.append(datiILL.getResponderId()).append('.');

		mov.setNoteBibliotecario(noteBib.toString());
	}

	public abstract List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib,
			String servizioLoc, StatoIterRichiesta statoILL,
			StatoIterRichiesta statoLocale, List<AttivitaServizioVO> iterLocale)
			throws Exception;

	public static void main(String[] args) {
		ConfigurazioneILLBaseImpl conf = new ConfigurazioneILLBaseImpl() {

			@Override
			public List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib, String servizioLoc,
					StatoIterRichiesta statoILL, StatoIterRichiesta statoLocale, List<AttivitaServizioVO> iterLocale)
					throws Exception {
				return null;
			}
		};

		MessaggioVO msg = new MessaggioVO();

		//F129		Data di scadenza: 2017-01-30
		msg.setNote("Data di scadenza: 2017-01-30");
		System.out.println(conf.estraiData(msg, "Data di scadenza: "));

		//F115		Data di nuova scadenza proposta: 30/01/2017
		msg.setNote("Data di nuova scadenza proposta: 22/03/2017");
		System.out.println(conf.estraiData(msg, "Data di nuova scadenza proposta: "));

		//F127		Data di spedizione: 05/12/2016 Data di scadenza: 2017-01-04 Costo: EUR 9,00
		msg.setNote("Data di spedizione: 05/12/2016 Data di scadenza: 2017-01-04 Costo: EUR 9,00");
		System.out.println(conf.estraiCosto(msg, "Costo: EUR "));

	}

}
