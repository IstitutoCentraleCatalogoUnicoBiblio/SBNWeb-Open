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
package it.iccu.sbn.servizi.ill;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.conf.ConfigurazioneILL;
import it.iccu.sbn.servizi.ill.conf.requester.ConfigurazionePrestitoRichiedente;
import it.iccu.sbn.servizi.ill.conf.requester.ConfigurazionePreventivoRichiedente;
import it.iccu.sbn.servizi.ill.conf.requester.ConfigurazioneRiproduzioneRichiedente;
import it.iccu.sbn.servizi.ill.conf.responder.ConfigurazionePrestitoFornitrice;
import it.iccu.sbn.servizi.ill.conf.responder.ConfigurazionePreventivoFornitrice;
import it.iccu.sbn.servizi.ill.conf.responder.ConfigurazioneRiproduzioneFornitrice;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class ILLConfiguration2 {

	private static final String REQUESTER_STATE_PREFIX = "F11";
	private static final String RESPONDER_STATE_PREFIX = "F12";

	private static class EmptyConfiguration implements ConfigurazioneILL {

		public List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib,
				String servizioLoc, StatoIterRichiesta statoILL,
				StatoIterRichiesta statoLocale, List<AttivitaServizioVO> iterLocale)
				throws Exception {
			return Collections.emptyList();
		}

		public void controllaCambiamentoDiStato(MovimentoVO mov,
				DatiRichiestaILLVO datiILL, ServizioBibliotecaVO servizio,
				StatoIterRichiesta _old, StatoIterRichiesta _new) throws Exception {
			return;
		}
	};

	public enum Action {
		SEND,
		RECEIVE;
	}

	private static class SupplyMediumType {

		private SupplyMediumType(ILLServiceType service, String codice_sbn, String codice_ipig, Action action) {
			super();
			this.service = service;
			this.codice_sbn = codice_sbn;
			this.codice_ipig = codice_sbn;//codice_ipig;
			this.action = action;
		}

		ILLServiceType service;
		String codice_sbn;
		String codice_ipig;
		Action action;
	}

	private static Logger log = Logger.getLogger(ILLConfiguration2.class);

	static ILLConfiguration2 instance = new ILLConfiguration2();

	private MultiKeyMap configurations;
	private Map<Character, String[]> mediumTypeFromTipoRecord;
	private List<SupplyMediumType> supplyMediumType;

	public static ILLConfiguration2 getInstance() {
		//return instance;
		return new ILLConfiguration2();
	}

	String[] arr(String... values) {
		return values;
	}


	private ILLConfiguration2() {
		configurations = new MultiKeyMap();

		//prestito
		configurations.put(ILLServiceType.PR, RuoloBiblioteca.RICHIEDENTE, new ConfigurazionePrestitoRichiedente());
		configurations.put(ILLServiceType.PR, RuoloBiblioteca.FORNITRICE, new ConfigurazionePrestitoFornitrice());
		configurations.put(ILLServiceType.PI, RuoloBiblioteca.RICHIEDENTE, new ConfigurazionePrestitoRichiedente());
		configurations.put(ILLServiceType.PI, RuoloBiblioteca.FORNITRICE, new ConfigurazionePrestitoFornitrice());

		//riproduzione/document delivery
		configurations.put(ILLServiceType.RI, RuoloBiblioteca.RICHIEDENTE, new ConfigurazioneRiproduzioneRichiedente());
		configurations.put(ILLServiceType.RI, RuoloBiblioteca.FORNITRICE, new ConfigurazioneRiproduzioneFornitrice());

		//preventivi
		configurations.put(ILLServiceType.CP, RuoloBiblioteca.RICHIEDENTE, new ConfigurazionePreventivoRichiedente());
		configurations.put(ILLServiceType.CP, RuoloBiblioteca.FORNITRICE, new ConfigurazionePreventivoFornitrice());

		configurations.put(ILLServiceType.CR, RuoloBiblioteca.RICHIEDENTE, new ConfigurazionePreventivoRichiedente());
		configurations.put(ILLServiceType.CR, RuoloBiblioteca.FORNITRICE, new ConfigurazionePreventivoFornitrice());

		mediumTypeFromTipoRecord = new HashMap<Character, String[]>();
/*
		mediumTypeFromTipoRecord.put('a', "1");
		mediumTypeFromTipoRecord.put('b', "1");
		mediumTypeFromTipoRecord.put('c', "1");
		mediumTypeFromTipoRecord.put('d', "1");
		mediumTypeFromTipoRecord.put('e', "1");
		mediumTypeFromTipoRecord.put('g', "4");
		mediumTypeFromTipoRecord.put('i', "5");
		mediumTypeFromTipoRecord.put('j', "5");
		mediumTypeFromTipoRecord.put('k', "7");
		mediumTypeFromTipoRecord.put('l', "6");
		mediumTypeFromTipoRecord.put('m', "6");
		mediumTypeFromTipoRecord.put('Z', "7");
*/

		mediumTypeFromTipoRecord.put('a', arr("a", "VM"));
		mediumTypeFromTipoRecord.put('b', arr("b"));
		mediumTypeFromTipoRecord.put('c', arr("c"));
		mediumTypeFromTipoRecord.put('d', arr("d"));
		mediumTypeFromTipoRecord.put('e', arr("e"));
		mediumTypeFromTipoRecord.put('g', arr("g"));
		mediumTypeFromTipoRecord.put('i', arr("i"));
		mediumTypeFromTipoRecord.put('k', arr("k"));
		mediumTypeFromTipoRecord.put('l', arr("l"));
		mediumTypeFromTipoRecord.put('m', arr("m"));
		mediumTypeFromTipoRecord.put('Z', arr("Z"));
		mediumTypeFromTipoRecord.put('j', arr("m"));

		supplyMediumType = new ArrayList<ILLConfiguration2.SupplyMediumType>();
		//inviati
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "00", "2", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "01", "6", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "02", "1", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "03", "2", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "04", "7", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "05", "1", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "07", "6", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "08", "7", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "09", "6", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "10", "6", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "11", "7", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "13", "7", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "3", "3", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "4", "4", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "5", "5", Action.SEND));

		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "1", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "00", "2", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "3", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "4", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "5", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "6", Action.SEND));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "7", Action.SEND));

		//ricevuti
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "00", "1", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "00", "2", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "3", "3", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "4", "4", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "5", "5", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "07", "6", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "13", "7", Action.RECEIVE));

		//SBN su SBN ???
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.RI, "07", "07", Action.RECEIVE));

		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "1", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "3", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "4", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "5", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "6", Action.RECEIVE));
		supplyMediumType.add(new SupplyMediumType(ILLServiceType.PR, "05", "7", Action.RECEIVE));
	}


	private ConfigurazioneILL getConfigurazione(ILLServiceType servizioILL, RuoloBiblioteca ruoloBib) {
		ConfigurazioneILL conf = (ConfigurazioneILL) configurations.get(servizioILL, ruoloBib);
		if (conf == null)
			conf = new EmptyConfiguration();

		log.debug("configurazione ill: " + conf.getClass().getSimpleName() );
		return conf;
	}


	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib, String servizioLoc, String servizioILL,
			RuoloBiblioteca ruoloBib, String statoILL, String statoLocale, List<AttivitaServizioVO> iterLocale) throws Exception {
		ConfigurazioneILL conf = getConfigurazione(ILLServiceType.fromValue(servizioILL), ruoloBib);

		List<AttivitaServizioVO> nuovoIter = conf.getListaAttivitaSuccessive(codBib, servizioLoc,
				StatoIterRichiesta.of(statoILL), StatoIterRichiesta.of(statoLocale),
				ValidazioneDati.coalesce(iterLocale, new ArrayList<AttivitaServizioVO>()) );

		return nuovoIter;
	}


	public boolean isRichiestaAnnullabile(DatiRichiestaILLVO dati) {
		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());
		if (dati.isFornitrice()) {
			boolean non_trasmessa = (dati.getTransactionId() == 0);
			boolean prestito = (ILLServiceType.valueOf(dati.getServizio()) == ILLServiceType.PR);
			return !ValidazioneDati.in(stato,
					StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE,
					StatoIterRichiesta.F1218_TERMINE_SCADUTO)
				&& (non_trasmessa ||
					ValidazioneDati.in(stato,
							prestito ? StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO : StatoIterRichiesta.CUSTOM,
							StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO,
							StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA,
							StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA,
							StatoIterRichiesta.F1220_RIFIUTO_ANNULLAMENTO) );
		} else
			//richiedente
			return ValidazioneDati.in(stato, StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA,
					StatoIterRichiesta.F100_DEFINIZIONE_RICHIESTA_DA_UTENTE,
					StatoIterRichiesta.F111_DEFINIZIONE_RICHIESTA,
					StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA);

	}


	public boolean isRichiestaSollecitabile(DatiRichiestaILLVO dati) {
		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());
		return dati.isFornitrice() && ValidazioneDati.in(stato,
				StatoIterRichiesta.F114_ARRIVO_MATERIALE,
				StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO,
				StatoIterRichiesta.F129_CONFERMA_RINNOVO);
	}


	public boolean isRichiestaStoricizzabile(DatiRichiestaILLVO dati, Timestamp dataLimite) {
		Timestamp dataFine = dati.getDataFine();
		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());

		switch (stato) {
		case F100_DEFINIZIONE_RICHIESTA_DA_UTENTE:
		case F111_DEFINIZIONE_RICHIESTA:
		case F1221_CONFERMA_ANNULLAMENTO:
		case F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA:
		case F1218_TERMINE_SCADUTO:
		case F1212_RICHIESTA_NON_SODDISFACIBILE:
		case F116A_PERDITA_DEL_MATERIALE_PRESTATO:
		case F128_RICEZIONE_MATERIALE:
			return (dataFine == null || dataFine.before(dataLimite));

		default:
			return false;
		}
	}


	public void controllaCambiamentoDiStato(MovimentoVO mov, DatiRichiestaILLVO datiILL, ServizioBibliotecaVO servizio,
			StatoIterRichiesta _old, StatoIterRichiesta _new) throws Exception {
		ILLServiceType servizioILL = ILLServiceType.fromValue(datiILL.getServizio());
		RuoloBiblioteca ruoloBib = datiILL.getRuolo();
		ConfigurazioneILL conf = getConfigurazione(servizioILL, ruoloBib);

		mov = mov != null ? mov : new MovimentoVO();
		conf.controllaCambiamentoDiStato(mov, datiILL, servizio, _old, _new);
	}


	public boolean isRichiestaInoltrabileAltraBib(DatiRichiestaILLVO dati) {
		//solo se annullata
		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());
		if (!ValidazioneDati.in(stato,
				StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE,
				StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO))
			return false;

		//solo se la data odierna non è oltre data massimo interresse
		Date dataMassima = dati.getDataMassima();
		if (dataMassima != null && DaoManager.now().after(DateUtil.withTimeAtEndOfDay(dataMassima)))
			return false;

		//possibile solo se esistono altre biblioteche a cui inoltrare la richiesta
		if (Stream.of(dati.getBibliotecheFornitrici()).filter(new Predicate<BibliotecaVO>() {
			public boolean test(BibliotecaVO bib) {
				return !ValidazioneDati.in(bib.getFlCanc(), "s", "S");
			}
		}).count() < 2)
			return false;

		return true;
	}


	public boolean isRichiestaRinnovabile(DatiRichiestaILLVO dati) {
		//solo se annullata
		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());
		switch (dati.getRuolo()) {
		case FORNITRICE:
			if (ValidazioneDati.in(stato,
					StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO))
				return true;

		case RICHIEDENTE:
			if (ValidazioneDati.in(stato,
					StatoIterRichiesta.F114_ARRIVO_MATERIALE,
					StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO,
					StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO))
				return true;
		}

		return false;

	}


	public String getMediumTypeFromTipoRecord(Character tipoRecord) {
		//String mediumType = mediumTypeFromTipoRecord.get(tipoRecord);
		String mediumType = ValidazioneDati.first(mediumTypeFromTipoRecord.get(tipoRecord));
		return mediumType != null ? mediumType : "7"; //altro
	}


	public Character getTipoRecordFromMediumType(String mediumType) {
		for (Character tipoRecord : mediumTypeFromTipoRecord.keySet()) {
			String[] mediumTypes = mediumTypeFromTipoRecord.get(tipoRecord);
			if (Arrays.asList(mediumTypes).contains(mediumType) )
				return tipoRecord;
		}
		return 'Z';
	}


	public String getCodiceSbnFromSupplyMediumType(ILLServiceType servizioILL, String mediumType, Action action) {
		for (SupplyMediumType s : supplyMediumType) {
			if (s.service == servizioILL)
				if (s.action == action && mediumType.equals(s.codice_ipig) )
					return s.codice_sbn;
		}

		return "05"; //printed
	}


	public boolean isRichiestaCondizionabile(DatiRichiestaILLVO dati) {
		return (dati.getTransactionId() > 0)
				&& dati.isFornitrice()
				&& ValidazioneDati.in(StatoIterRichiesta.of(dati.getCurrentState() ),
						StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA,
						StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO,
						StatoIterRichiesta.F1220_RIFIUTO_ANNULLAMENTO);
	}


	public boolean isRichiestaPrenotabile(DatiRichiestaILLVO dati) {
		return (dati.getTransactionId() > 0)
				&& dati.isFornitrice()
				&& ValidazioneDati.in(StatoIterRichiesta.of(dati.getCurrentState()),
						StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA,
						StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO,
						StatoIterRichiesta.F1220_RIFIUTO_ANNULLAMENTO);
	}

	public String generaUrlDocumentDelivery(DatiRichiestaILLVO dati) throws Exception {
		String url = null;
		if (dati != null && dati.getTransactionId() > 0) {
			//solo per riproduzione in formato elettronico
			if (ILLServiceType.valueOf(dati.getServizio()) == ILLServiceType.RI
					&& StatoIterRichiesta.of(dati.getCurrentState()) == StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE
					&& ValidazioneDati.equals(dati.getCod_erog(), "03")) {

				url = String.format("%s?faseILL=%s&prgid=%d",
						CommonConfiguration.getProperty(Configuration.ILL_SBN_DOC_DELIVERY_URL),
						dati.isFornitrice() ? "UPLOAD" : "DOWNLOAD",
						dati.getTransactionId());
			}
		}
		return url;
	}


	public List<TB_CODICI> getListaSupportiILL(final ILLServiceType servizioILL) throws Exception {

		List<TB_CODICI> supportiILL = CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_COPIA_ILL);
		if (servizioILL != null) {
			// per riproduzione va scartato il tipo supporto originale
			supportiILL = Stream.of(supportiILL).filter(new Predicate<TB_CODICI>() {
				public boolean test(TB_CODICI c) {
					if (servizioILL == ILLServiceType.RI)
						//riproduzione, si esclude originale
						return !c.getCd_tabellaTrim().equals("05"); // originale
					else
						//prestito, solo originale
						return c.getCd_tabellaTrim().equals("05"); // originale
				}
			}).toList();
		}

		return supportiILL;
	}

	public List<TB_CODICI> getListaSupportiILL(TipoServizioVO tipoServ) throws Exception {
		ILLServiceType servizioILL = isFilled(tipoServ.getCodServizioILL()) ? ILLServiceType.valueOf(tipoServ.getCodServizioILL()) : null;
		return getListaSupportiILL(servizioILL);
	}

	//tabella codici LMEI
	static String[] MOD_EROGAZIONE_PR = new String[] {
		"01",	//posta prioritaria
		"02",	//posta raccomandata
		"07"	//corriere
	};

	static String[] MOD_EROGAZIONE_RI = new String[] {
		"03",	//file repository
		"04",	//e-mail
		"05",	//fax
		"06"	//ftp/url
	};

	public List<TB_CODICI> getListaModErogazioneILL(final ILLServiceType servizioILL) throws Exception {
		//filtro sulle modalità di erogazione ILL
		List<TB_CODICI> modErogILL = CodiciProvider.getCodici(CodiciType.CODICE_MODALITA_EROGAZIONE_ILL);
		if (servizioILL != null) {
			modErogILL = Stream.of(modErogILL).filter(new Predicate<TB_CODICI>() {
				public boolean test(TB_CODICI c) {
					if (servizioILL == ILLServiceType.RI)
						//tipo servizio riproduzione
						return Arrays.asList(MOD_EROGAZIONE_RI).contains(c.getCd_tabellaTrim());
					else
						//tipo servizio prestito
						return Arrays.asList(MOD_EROGAZIONE_PR).contains(c.getCd_tabellaTrim());
				}
			}).toList();
		}

		return modErogILL;
	}

	public List<TB_CODICI> getListaModErogazioneILL(TipoServizioVO tipoServ) throws Exception {
		ILLServiceType servizioILL = isFilled(tipoServ.getCodServizioILL()) ? ILLServiceType.valueOf(tipoServ.getCodServizioILL()) : null;
		return getListaModErogazioneILL(servizioILL);
	}

	public TipoInvio getTipoInvioMessaggio(RuoloBiblioteca ruolo, StatoIterRichiesta stato) {
		switch (ruolo) {
		case RICHIEDENTE:
			return stato.getISOCode().startsWith(REQUESTER_STATE_PREFIX) ? TipoInvio.INVIATO : TipoInvio.RICEVUTO;
		case FORNITRICE:
			return stato.getISOCode().startsWith(RESPONDER_STATE_PREFIX) ? TipoInvio.INVIATO : TipoInvio.RICEVUTO;
		}
		return TipoInvio.RICEVUTO;
	}

	public boolean isRichiestaPrestito(DatiRichiestaILLVO dati) {
		return in(ILLServiceType.fromValue(dati.getServizio()), ILLServiceType.PR, ILLServiceType.PI);
	}

}
