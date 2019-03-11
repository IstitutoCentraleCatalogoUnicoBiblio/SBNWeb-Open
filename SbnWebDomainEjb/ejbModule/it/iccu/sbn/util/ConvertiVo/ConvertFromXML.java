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
package it.iccu.sbn.util.ConvertiVo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ill.ILLConfiguration2.Action;
import it.iccu.sbn.util.Base64Util;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.AmountType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ClientIdType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CostInfoType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CurrentState;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateRequester;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DeliveryAddressType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DeliveryService;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DeliveryType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ElectronicAddress;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ExpiryDate;
import it.iccu.sbn.vo.xml.binding.ill.apdu.HistoryReport;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLBiblioType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ItemIdType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ItemType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.MaximumCost;
import it.iccu.sbn.vo.xml.binding.ill.apdu.MediumType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.NeedBeforeDate;
import it.iccu.sbn.vo.xml.binding.ill.apdu.PostalAddress;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RequesterNote;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ServiceDateTimeType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StandardNumberType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusOrErrorReportType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusReportType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.SupplyMediumTypeType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.trunc;

public class ConvertFromXML extends DataBindingConverter {

	private static final long serialVersionUID = 8114217697853861549L;

	protected final static SimpleDateFormat ILL_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
	protected final static NumberFormat AMOUNT_FORMATTER = new DecimalFormat("#0.00");

	private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

	static {
		ILL_TIME_FORMAT.setTimeZone(GMT_TIME_ZONE);
	}

	protected ConvertFromXML() {
		super();
	}

	public String convertiIterISO2SBN(String iso) throws Exception {
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_ATTIVITA_ITER);
		for (TB_CODICI cod : codici)
			if (ValidazioneDati.equals(iso, cod.getCd_flg5()))
				return cod.getCd_tabella();

		return "";
	}

	public String convertiIterSBN2ISO(String sbn) throws Exception {
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_ATTIVITA_ITER);
		for (TB_CODICI cod : codici)
			if (ValidazioneDati.equals(sbn, cod.getCd_tabellaTrim()))
				return cod.getCd_flg5();

		return "";
	}

	private static Timestamp convertiIllDate(ServiceDateTimeType sdt) throws ParseException {
		TimeZone tz = GMT_TIME_ZONE;
		Calendar c = Calendar.getInstance(tz);

		String time = sdt.getTime();
		if (isFilled(time))
			c.setTime(ILL_TIME_FORMAT.parse(time));

		DateType date = sdt.getDate();
		c.set(Calendar.YEAR, date.getYear());
		c.set(Calendar.MONTH, date.getMonth() - 1);
		c.set(Calendar.DAY_OF_MONTH, date.getDay());

		tz = TimeZone.getDefault();
		c.setTimeZone(tz);

		return new Timestamp(c.getTimeInMillis());
	}

	public static Date convertiIllDate(DateType dt) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, dt.getYear());
		c.set(Calendar.MONTH, dt.getMonth() - 1);
		c.set(Calendar.DAY_OF_MONTH, dt.getDay());

		return c.getTime();
	}

	public MovimentoListaVO movimento(ILLAPDU xml) throws Exception {

		MovimentoListaVO webVO = new MovimentoListaVO();

		StatusOrErrorReportType soer = xml.getStatusOrErrorReport();
		StatusReportType sr = soer.getStatusReport();

		CurrentState state = sr.getCurrentState();
		if (state != null) {
			String statoRic = convertiIterISO2SBN(state.getState());
			webVO.setCodStatoRic(statoRic);
			webVO.decode();
		}

		HistoryReport hr = sr.getHistoryReport();
		if (hr != null) {
			webVO.setTitolo(hr.getTitle());
		}

		webVO.setDataRichiesta(convertiIllDate(soer.getServiceDateTime()));
		webVO.setCodRichServ(soer.getTransactionId().getValue());
		//webVO.setIdRichiesta(hVO.getCod_rich_serv());

		webVO.setCognomeNome(soer.getRequesterId());

		return webVO;
	}

	public DocumentoNonSbnVO documentoNonSbn(DocumentoNonSbnVO old, ItemIdType i) throws Exception {

		DocumentoNonSbnVO webVO = old != null ? old : new DocumentoNonSbnVO();

		//<Title>titolo</Title>
		webVO.setTitolo(trunc(i.getTitle(), 240));
		//<Author>AUTORE</Author>
		webVO.setAutore(trunc(i.getAuthor(), 160));

		String nationalBibliograficNumber = i.getNationalBibliograficNumber();
		if (isFilled(nationalBibliograficNumber)) {
			nationalBibliograficNumber = nationalBibliograficNumber.toUpperCase();
			webVO.setBid(ValidazioneDati.leggiXID(nationalBibliograficNumber) ? nationalBibliograficNumber : null);
		}

		webVO.setStato_sugg(' ');

		if (!isFilled(webVO.getFonte()))
			webVO.setFonte('I'); // inserito da server ILL

		ItemType itemType = i.getItemType();
		if (itemType != null) {
			String ityp = itemType.getItyp();
			if (isFilled(ityp))
				webVO.setNatura(ityp.charAt(0));
		}

		MediumType mediumType = i.getMediumType();
		if (mediumType != null) {
			webVO.setTipoRecord(ILLConfiguration2.getInstance().getTipoRecordFromMediumType(mediumType.getMtyp()));
		}

		//<Location-note>collocazione</Location-note>
		String loc = i.getLocationNote();
		if (isFilled(loc)) {
			webVO.setSegnatura(trunc(loc, 40));
			webVO.setOrd_segnatura(OrdinamentoCollocazione2.normalizza(loc));
		}

		List<StandardNumberType> numbers = i.getStandardNumber();
		if (isFilled(numbers)) {
			StandardNumberType std = numbers.get(0);
			webVO.setTipoNumStd(std.getStandardType());
			webVO.setNumeroStd(trunc(std.getValue(), 25));
		}

		//<Year-issue>anno</Year-issue>
		webVO.setAnnata(trunc(i.getYearIssue(), 10));
		webVO.setNum_volume(trunc(i.getVolumeIssue(), 30));

		//inventario
		String additionalNoLetters = i.getAdditionalNoLetters();
		if (isFilled(additionalNoLetters)) {
			try {
				//fix apdu xsd whitespaces collapse
				additionalNoLetters = new String(Base64Util.decode(additionalNoLetters));

				InventarioVO inv = InventarioRFIDParser.parse(additionalNoLetters);
				webVO.setCod_bib_inv(inv.getCodBib());
				webVO.setCod_serie(inv.getCodSerie());
				webVO.setCod_inven(inv.getCodInvent());

			} catch (Exception e) {	}
		}

		// <Edition>edizione</Edition>
		webVO.setAnnoEdizione(i.getEdition());
		// <Publisher>editore</Publisher>
		webVO.setEditore(trunc(i.getPublisher(), 50));
		// <Place-of-publication>luogo</Place-of-publication>
		webVO.setLuogoEdizione(trunc(i.getPlaceOfPublication(), 30));

		// <Sponsoring-body>ente curatore</Sponsoring-body>
		webVO.setEnteCuratore(trunc(i.getSponsoringBody(), 50));
		// <Series-title-number>fa parte di</Series-title-number>
		webVO.setFaParte(trunc(i.getSeriesTitleNumber(), 50));

		// <Volume-issue>volume</Volume-issue>

		// <Issue>fascicolo</Issue>
		webVO.setFascicolo(trunc(i.getIssue(), 50));
		// <Publication-date>data pubb</Publication-date>
		webVO.setDataPubb(trunc(i.getPublicationDate(), 20));
		// <Author-of-article>autore articolo</Author-of-article>
		webVO.setAutoreArticolo(trunc(i.getAuthorOfArticle(), 50));
		// <Title-of-article>titolo articolo</Title-of-article>
		webVO.setTitoloArticolo(trunc(i.getTitleOfArticle(), 50));
		// <Pagination>pagine</Pagination>
		webVO.setPagine(trunc(i.getPagination(), 50));
		// <Verification-reference-source>fonte</Verification-reference-source>
		webVO.setFonteRif(trunc(i.getVerificationReferenceSource(), 50));

		return webVO;
	}

	public DatiRichiestaILLVO datiRichiestaILL(DatiRichiestaILLVO old, ILLRequestType ilt) throws Exception {
		DatiRichiestaILLVO webVO = (DatiRichiestaILLVO) (old != null ? old.copy() : new DatiRichiestaILLVO());
		String country = Locale.getDefault().getCountry();
		webVO.setRequesterId(ServiziUtil.formattaIsil(ilt.getRequesterId(), country) );
		webVO.setResponderId(ServiziUtil.formattaIsil(ilt.getResponderId(), country) );
		String tid = ilt.getTransactionId().getValue();
		webVO.setTransactionId(new Long(tid));
		ILLServiceType servizioILL = ilt.getILLSERVICETYPE();

		ClientIdType clientId = ilt.getClientId();
		if (clientId != null) {
			//gestione utente
			String codUtente = ServiziUtil.espandiCodUtente(clientId.getClientIdentifier());
			webVO.setCodUtente(codUtente);
			webVO.setCognomeNome(clientId.getClientName());
			webVO.setUtente_email(clientId.getEmail());
		}

		DeliveryAddressType da = ilt.getDeliveryAddress();
		if (da != null) {
			PostalAddress postal = da.getPostalAddress();
			if (postal != null) {
				webVO.setVia(postal.getStreetAndNumber());
				webVO.setCap(postal.getPostalCode());
				webVO.setComune(postal.getCity());
				webVO.setProv(postal.getRegion());
				webVO.setCd_paese(postal.getCountry());
			}

			ElectronicAddress ea = da.getElectronicAddress();
			if (ea != null)
				webVO.setRequester_email(ea.getValue());
		}

		webVO.setCostoMax(BigDecimal.ZERO);
		CostInfoType cost = ilt.getCostInfoType();
		if (cost != null) {
			MaximumCost mc = cost.getMaximumCost();
			if (mc != null) {
				AmountType amount = mc.getAmount();
				if (amount != null) {
					webVO.setCostoMax(importo(amount));
					webVO.setCd_valuta(amount.getCurrencyCode());
				}
			}
		}

		DeliveryService ds = ilt.getDeliveryService();
		if (ds != null) {
			//modalità erogazione
			DeliveryType dt = ds.getDelivTyp();
			webVO.setCod_erog(dt.value());
		}

		SupplyMediumTypeType smt = ValidazioneDati.first(ilt.getSupplyMediumType());
		if (smt != null) {
			//supporto
			String supp = ILLConfiguration2.getInstance().getCodiceSbnFromSupplyMediumType(servizioILL, smt.getSupplyTyp(), Action.RECEIVE);
			webVO.setCd_supporto(supp);
		}

		DateRequester dr = ilt.getDateRequester();
		if (dr != null) {
			DateType dt = ValidazioneDati.first(dr.getDate());
			if (dt != null)
				webVO.setDataDesiderata(convertiIllDate(dt));
		}

		NeedBeforeDate nbd = ilt.getNeedBeforeDate();
		if (nbd != null)
			webVO.setDataMassima(convertiIllDate(nbd.getDate()));

		ExpiryDate ed = ilt.getExpiryDate();
		if (ed != null)
			webVO.setDataScadenza(convertiIllDate(ed.getDate()));

		CurrentState cs = ilt.getCurrentState();
		if (cs != null) {
			String state = cs.getState();
			webVO.setCurrentState(state);

			//preparazione messaggio
			MessaggioVO msg = new MessaggioVO(webVO);
			msg.setDataMessaggio(DaoManager.now());
			msg.setTipoInvio(ILLConfiguration2.getInstance().getTipoInvioMessaggio(webVO.getRuolo(), StatoIterRichiesta.of(state)));
			msg.setStato(state);

			RequesterNote requesterNote = ilt.getRequesterNote();
			if (requesterNote != null)
				msg.setNote(ValidazioneDati.first(requesterNote.getNote()) );
			else
				msg.setNote(CodiciProvider.cercaDescrizioneCodice(state, CodiciType.CODICE_ATTIVITA_ITER, CodiciRicercaType.RICERCA_CODICE_SBN));

			if (!isFilled(webVO.getMessaggio()) )
				webVO.addUltimoMessaggio(msg);
			else {
				//check storico messaggi
				MessaggioVO last = ValidazioneDati.first(old.getMessaggio());
				//stato cambiato, si aggiunge un nuovo msg.
				if (last != null && !ValidazioneDati.equals(last.getStato(), msg.getStato()) ) {
					msg.setId_dati_richiesta_ill(old.getIdRichiestaILL());
					webVO.addUltimoMessaggio(msg);
				}

			}
		}

		webVO.setServizio(servizioILL.name() );

		ItemIdType itemId = ilt.getItemId();
		if (itemId != null) {
			String pagination = itemId.getPagination();
			if (isFilled(pagination)) {
				//webVO.setIntervalloCopia(pagination);
			}
		}

		return webVO;
	}

	public BibliotecaILLVO bibliotecaILL(BibliotecaILLVO old, ILLBiblioType ibt) {
		BibliotecaILLVO webVO = old != null ? old : new BibliotecaILLVO();
		webVO.setIsil(ibt.getIdb());
		webVO.setDescrizione(ibt.getDes());
		webVO.setTipoPrestito(ibt.getServprestito());
		webVO.setTipoDocDelivery(ibt.getServprestito());
		webVO.setRuolo(ibt.getStd());
		return webVO;
	}

	public Number importo(AmountType amount) {
		if (amount != null) {
			try {
				String monetaryValue = amount.getMonetaryValue();
				if (isFilled(monetaryValue)) {
					//almaviva5_20181012 #6743 conversione numeri decimali al formato del locale corrente
					DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault() );
					monetaryValue = monetaryValue.replace(',', dfs.getDecimalSeparator()).replace('.', dfs.getDecimalSeparator());

					NumberFormat _format = NumberFormat.getInstance(Locale.getDefault());
					Number value = _format.parse(monetaryValue);
					return new BigDecimal(value.doubleValue());
				}

			} catch (Exception e) {
				return BigDecimal.ZERO;
			}
		}
		return BigDecimal.ZERO;
	}

	public static void main(String... args) throws ParseException {
		ServiceDateTimeType sdt = new ServiceDateTimeType();
		DateType date = new DateType();
		date.setDay(22);
		date.setMonth(11);
		date.setYear(2014);
		sdt.setDate(date);
		sdt.setTime("10:00:00.7");	//HH:mm:ss.S
		System.out.println(convertiIllDate(sdt));

		String kinv = "FI+++000000123";
		System.out.println(kinv.replaceAll("\\+", "\u0020"));
	}

}
