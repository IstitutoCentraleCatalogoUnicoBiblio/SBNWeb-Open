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
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Trc_poss_prov_inventari;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.Base64Util;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.documentofisico.InventarioPossedutoVO;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.xml.binding.ill.apdu.AmountType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ItemIdType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ItemType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.MediumType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ObjectFactory;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ServiceDateTimeType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StandardNumberType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.AccessoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.CollocazioneType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DisponibilitaType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.EsitoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.EventoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.FascicoloType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.InventarioType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.PossessoreType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.UtenteType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.fillRight;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;

public final class ConvertToXML extends ConvertFromXML {

	private static final long serialVersionUID = -5654087182743117045L;

	private static DatatypeFactory typeFactory;
	private static ObjectFactory factory;

	private static final String TAG_UNIMARC_PROV = "320";
	private static final String TAG_UNIMARC_POSS = "390";

	static {
		try {
			typeFactory = DatatypeFactory.newInstance();
			factory = new ObjectFactory();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
	}

	protected ConvertToXML() {
		super();
	}


	public final SbnwebType formattaErrore(SbnErrorTypes error, String message) {
		SbnwebType sbn = new SbnwebType();
		EsitoType esito = new EsitoType();
		esito.setReturnCode(error.getIntCode());
		sbn.setEsito(esito);

		StringBuilder msg = new StringBuilder(512);
		msg.append(error.name());
		if (ValidazioneDati.isFilled(message) )
			msg.append(' ').append(message);

		esito.setMessage(msg.toString());

		return sbn;
	}


	public final SbnwebType formattaErrore(SbnBaseException e) {
		StringBuilder msg = new StringBuilder(512);
		String[] labels = e.getLabels();
		if (isFilled(labels)) {
			msg.append(" (");
			msg.append(ValidazioneDati.formatValueList(Arrays.asList(labels), ", "));
			msg.append(")");
		}

		return formattaErrore(e.getErrorCode(), msg.toString());
	}


	public final XMLGregorianCalendar dateToXMLDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		XMLGregorianCalendar xmlDate = typeFactory.newXMLGregorianCalendar(gc);
		xmlDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return xmlDate;
	}


	public final FascicoloType fascicolo(FascicoloDecorator fd) throws Exception {
		FascicoloType ftype = new FascicoloType();
		ftype.setDataPubblicazione(dateToXMLDate(fd.getData_conv_pub() ));
		//ftype.setPeriodicita(PeriodicitaType.fromValue(fd.getCd_per()));
		ftype.setNumerazione(fd.getNumFascicolo());
		ftype.setVolume(fd.getNum_volume());
		ftype.setAnnata(fd.getAnnata());
		ftype.setTipo(CodiciProvider.cercaDescrizioneCodice(fd.getCd_tipo_fasc(),
				CodiciType.CODICE_TIPO_FASCICOLI,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		ftype.setDescrizione(trimOrNull(fd.getDescrizione()));

		return ftype;
	}


	public final CollocazioneType collocazione(CollocazioneVO coll) {
		CollocazioneType c = new CollocazioneType();
		c.setBib(coll.getCodBib());
		c.setSez(trimOrBlank(coll.getCodSez()));
		c.setLoc(trimOrBlank(coll.getCodColloc()));
		c.setSpec(trimOrNull(coll.getSpecColloc()));

		String consis = trimOrEmpty(coll.getConsistenza());
		if (!in(consis, "", "$"))
			c.setConsis(consis);

		return c;
	}


	public final InventarioType inventarioPeriodico(BibliotecaVO bib, Object[] row) {
		InventarioType inv = new InventarioType();

		inv.setBib(bib.getCod_bib());
		inv.setSerie(fillRight((String) row[0], ' ', 3));
		inv.setNumero((Long) row[1]);
		Integer numFascicoli = (Integer) row[4];
		if (isFilled(numFascicoli))
			inv.setNumeroFascicoli(numFascicoli);

		Integer anno = (Integer) row[2];
		if (isFilled(anno))
			inv.setAnno(anno);

		String precis = trimOrEmpty((String) row[3]);
		if (!in(precis, "", "$"))
			inv.setPrecis(precis);

		//almaviva5_20161110
		String id_accesso_remoto = (String) row[5];
		if (isFilled(id_accesso_remoto))
			inv.setUrl(id_accesso_remoto.trim());

		return inv;
	}


	public final InventarioType inventario(Tbc_inventario i) {
		InventarioType inv = new InventarioType();
		Tbc_serie_inventariale serie = i.getCd_serie();
		Tbf_biblioteca_in_polo bib = serie.getCd_polo();
		inv.setBib(bib.getCd_biblioteca());
		inv.setSerie(fillRight(serie.getCd_serie(), ' ', 3));
		inv.setNumero(i.getCd_inven());

		Integer anno = i.getAnno_abb();
		if (isFilled(anno))
			inv.setAnno(anno);

		String precis = trimOrEmpty(i.getPrecis_inv());
		if (!in(precis, "", "$"))
			inv.setPrecis(precis);

		//almaviva5_20161110
		String id_accesso_remoto = i.getId_accesso_remoto();
		if (isFilled(id_accesso_remoto))
			inv.setUrl(id_accesso_remoto.trim());

		//almaviva5_20170607
		String cd_frui = i.getCd_frui();
		if (isFilled(cd_frui))
			inv.setCodFrui(cd_frui.trim());

		return inv;
	}


	public CollocazioneType collocazione(Tbc_collocazione coll) {
		if (coll == null)
			return null;

		CollocazioneType c = new CollocazioneType();
		Tbc_sezione_collocazione sez = coll.getCd_sez();
		Tbf_biblioteca_in_polo bib = sez.getCd_polo();
		c.setBib(bib.getCd_biblioteca());
		c.setSez(trimOrBlank(sez.getCd_sez()));
		c.setLoc(trimOrBlank(coll.getCd_loc()));
		c.setSpec(trimOrNull(coll.getSpec_loc()));

		String consis = trimOrEmpty(coll.getConsis());
		if (!in(consis, "", "$"))
			c.setConsis(consis);

		return c;
	}


	public CollocazioneType collocazione(BibliotecaVO bib, SerieCollocazioneVO coll) {

		CollocazioneType c = new CollocazioneType();
		c.setBib(bib.getCod_bib());
		c.setSez(trimOrBlank(coll.getCodSez()));
		c.setLoc(trimOrBlank(coll.getCd_loc()));
		c.setSpec(trimOrNull(coll.getSpec_loc()));

		return c;
	}


	public DisponibilitaType disponibilita(ControlloDisponibilitaVO cd) {
		DisponibilitaType d = new DisponibilitaType();
		if (!cd.isDisponibile()) {
			d.setDisponibile(false);
			Timestamp data_disponibilita = cd.getDataPrenotazione();
			if (data_disponibilita != null)
				d.setDataDisponibilita(dateToXMLDate(data_disponibilita));

			//almaviva5_20150603
			d.setMotivo(cd.getMotivo());
		} else
			d.setDisponibile(true);

		return d;
	}


	public CollocazioneType collocazione(InventarioPossedutoVO ip) {
		CollocazioneType ct = new CollocazioneType();
		ct.setSez(ip.getCd_sez());
		ct.setLoc(ip.getCd_loc());
		ct.setSpec(ip.getSpec_loc());
		String consis = trimOrEmpty(ip.getConsis());
		if (!in(consis, "", "$"))
			ct.setConsis(consis);

		return ct;
	}


	public InventarioType inventarioPosseduto(InventarioPossedutoVO inv) throws Exception {
		InventarioType ip = new InventarioType();
		ip.setSerie(inv.getCd_serie());
		ip.setNumero(inv.getCd_inven());

		String stato_con = inv.getStato_con();
		if (isFilled(stato_con)) {
			ip.setCdCons(stato_con);
			String descr = CodiciProvider.cercaDescrizioneCodice(stato_con,
					CodiciType.CODICE_STATI_DI_CONSERVAZIONE,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			if (isFilled(descr) )
				ip.setCons(descr);
		}

		String seq_coll = inv.getSeq_coll();
		if (isFilled(seq_coll))
			ip.setSeq(seq_coll);
		String precis_inv = trimOrEmpty(inv.getPrecis_inv());
		if (!in(precis_inv, "", "$"))
			ip.setPrecis(precis_inv);

		ip.setBib(inv.getCd_bib());

		//almaviva5_20161110
		String id_accesso_remoto = inv.getId_accesso_remoto();
		if (isFilled(id_accesso_remoto))
			ip.setUrl(id_accesso_remoto);

		//almaviva5_20170607
		String cd_frui = inv.getCd_frui();
		if (isFilled(cd_frui))
			ip.setCodFrui(cd_frui.trim());

		Integer anno_abb = inv.getAnno_abb();
		if (isFilled(anno_abb))
			ip.setAnno(anno_abb);

		String cd_no_disp = inv.getCd_no_disp();
		if (isFilled(cd_no_disp))
			ip.setCodNoDisp(cd_no_disp.trim());

		return ip;
	}


	public PossessoreType possessore(Tbc_possessore_provenienza pp,
			Trc_poss_prov_inventari ppi) {
		PossessoreType pt = new PossessoreType();
		pt.setId(pp.getPid());
		pt.setNome(trimOrNull(pp.getDs_nome_aut()));
		// tag unimarc del possessore/provenienza:
		// 320=provenienza,
		// 390=possessore
		char codLegame = ppi.getCd_legame();
		pt.setRel(ValidazioneDati.equals(codLegame, 'P') ? TAG_UNIMARC_POSS : TAG_UNIMARC_PROV);

		return pt;
	}

	public UtenteType utente(UtenteBaseVO ub) throws Exception {
		UtenteType ut = new UtenteType();
		ut.setUserId(ub.getCodUtente());

		ut.setCodFiscale(ub.getCodiceFiscale());

		if (ub.isEnte()) {
			ut.setDenominazione(ub.getCognome());
			ut.setTipoEnte(CodiciProvider.cercaDescrizioneCodice(ub.getTipoEnte(), CodiciType.CODICE_TIPO_PERSONA_GIURIDICA,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		} else {
			ut.setCognome(ub.getCognome());
			ut.setNome(ub.getNome());
			ut.setSesso(Character.toString(ub.getSesso()) );
		}

		String email = ServiziUtil.getEmailUtente(ub);
		if (ValidazioneDati.isFilled(email))
			ut.getEmail().add(email);

		return ut;
	}


	public ServiceDateTimeType convertiIllDateTime(Date date) throws ParseException {
		ServiceDateTimeType sdt = factory.createServiceDateTimeType();
		DateType d = convertiIllDate(date);
		sdt.setDate(d);

		String time = ILL_TIME_FORMAT.format(date);
		//almaviva5_20151210
		sdt.setTime(StringUtils.left(time, 10));

		return sdt;
	}


	public DateType convertiIllDate(Date date) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		DateType d = factory.createDateType();
		d.setYear(c.get(Calendar.YEAR));
		d.setMonth(c.get(Calendar.MONTH) + 1);
		d.setDay(c.get(Calendar.DAY_OF_MONTH));
		d.setYear(c.get(Calendar.YEAR));

		return d;
	}


	public ItemIdType costruisciItemId(DocumentoNonSbnVO doc) throws Exception {
		ItemIdType i = factory.createItemIdType();

		ItemType type = factory.createItemType();
		type.setItyp(String.valueOf(doc.getNatura()));
		i.setItemType(type);

		i.setTitle(doc.getTitolo());
		i.setAuthor(doc.getAutore());
		i.setNationalBibliograficNumber(doc.getBid());
		i.setPlaceOfPublication(doc.getLuogoEdizione());
		i.setPublisher(doc.getEditore());
		i.setEdition(doc.getAnnoEdizione());
		i.setYearIssue(doc.getAnnata());

		String tipoNumStd = doc.getTipoNumStd();
		if (isFilled(tipoNumStd)) {
			StandardNumberType std = factory.createStandardNumberType();
			std.setStandardType(tipoNumStd);
			std.setStandardCode(doc.getNumeroStd());
			i.getStandardNumber().add(std);
		}

		i.setLocationNote(doc.getSegnatura());

		Character tipoRecord = doc.getTipoRecord();
		if (isFilled(tipoRecord)) {
			String mediumType = ILLConfiguration2.getInstance().getMediumTypeFromTipoRecord(tipoRecord);
			if (isFilled(mediumType)) {
				MediumType mtype = factory.createMediumType();
				mtype.setMtyp(mediumType);
				i.setMediumType(mtype);
			}
		}

		String num_volume = doc.getNum_volume();
		if (isFilled(num_volume))
			i.setVolumeIssue(num_volume);

		//inventario
		if (doc.hasInventario()) {
			String kinv = ConversioneHibernateVO.toWeb().chiaveInventario(doc.getCod_bib_inv(), doc.getCod_serie(), doc.getCod_inven());
			if (isFilled(kinv)) {
				//fix apdu xsd whitespaces collapse
				kinv = Base64Util.encode(kinv.getBytes());
				i.setAdditionalNoLetters(kinv);
			}
		}

		// <Sponsoring-body>ente curatore</Sponsoring-body>
		i.setSponsoringBody(doc.getEnteCuratore());
		// <Series-title-number>fa parte di</Series-title-number>
		i.setSeriesTitleNumber(doc.getFaParte());
		// <Issue>fascicolo</Issue>
		i.setIssue(doc.getFascicolo());
		// <Publication-date>data pubb</Publication-date>
		i.setPublicationDate(doc.getDataPubb());
		// <Author-of-article>autore articolo</Author-of-article>
		i.setAuthorOfArticle(doc.getAutoreArticolo());
		// <Title-of-article>titolo articolo</Title-of-article>
		i.setTitleOfArticle(doc.getTitoloArticolo());
		// <Pagination>pagine</Pagination>
		i.setPagination(doc.getPagine());
		// <Verification-reference-source>fonte</Verification-reference-source>
		i.setVerificationReferenceSource(doc.getFonteRif());

		return i;
	}

	public AmountType amount(double value) {
		AmountType amount = factory.createAmountType();
		amount.setCurrencyCode("EUR");
		amount.setMonetaryValue(AMOUNT_FORMATTER.format(value));
		return amount;
	}

	public UtenteType utente(EventoAccessoVO e) throws Exception {
		UtenteType u;
		if (e.isAutenticato()) {
			u = utente(e.getUtente());
		} else {
			u = new UtenteType();
			u.setUserId(e.getIdTessera());
		}

		AccessoType accesso = new AccessoType();
		u.setAccesso(accesso);
		accesso.setAutenticato(e.isAutenticato());

		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		DateTime dateTime = LocalDateTime.fromDateFields(e.getDataEvento()).toDateTime();
		accesso.setDataEvento(dateTime.toString(formatter));
		accesso.setEvento(EventoType.fromValue(e.getEvento().name()));

		return u;
	}

	public static void main(String... args) throws ParseException {
		ConvertToXML conv = new ConvertToXML();
		System.out.println(conv.convertiIllDateTime(DaoManager.now()));

		String kinv = "FI   000000123";
		System.out.println(kinv.replaceAll("\\s", "+"));
	}

}
