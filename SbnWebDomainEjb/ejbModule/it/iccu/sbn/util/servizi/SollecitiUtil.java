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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.CampoSollecito;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.util.bbcode.BBCodeParser;
import it.iccu.sbn.vo.custom.servizi.ElementoStampaSollecitoDecorator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringEscapeUtils;
import org.kefirsf.bb.TextProcessor;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class SollecitiUtil {

	private static final String HTTP_MODEL_HEADER = new StringBuilder().append("<style>")
		.append("p {margin:0px;}")
		//.append("span.title {word-wrap:break-word; font-style:italic;}")
		.append("</style>")
		.append("<div style=\"width:450px;\">").toString();

	private static final String HTTP_MODEL_FOOTER = "</div>";

	public static ModelloSollecitoVO getModelloSollecitoBase() {
		ModelloSollecitoVO ms = new ModelloSollecitoVO();
		StringBuilder buf = new StringBuilder();
		String nl = System.getProperty("line.separator");

		buf.append("[d]Spett.le [g][nomeUtente][/g][/d]").append(nl);
		buf.append("[d][indirizzo][/d]").append(nl);
		buf.append("[d][cittaNazione][/d]").append(nl);
		buf.append(nl);
		buf.append(nl);
		buf.append("[c]Oggetto: Sollecito n. [numSollecito][/c]").append(nl);
		buf.append(nl);
		buf.append(nl);
		buf.append(nl);
		buf.append("In riferimento al documento: [inventario]").append(nl);
		buf.append("Collocazione: [segnatura]").append(nl);
		buf.append(nl);
		buf.append("Titolo documento: [i][titolo][/i]").append(nl);
		buf.append(nl);
		buf.append("prestato in data: [dataPrestito]").append(nl);
		buf.append("Che doveva essere riconsegnato in data: [g][dataScadenza][/g]").append(nl);
		buf.append("Per il tipo Servizio: [tipoServizio]").append(nl);
		buf.append("Tessera lettore: [codUtente]").append(nl);
		buf.append(nl);
		buf.append(nl);
		buf.append("Si prega di restituire il documento al più presto.").append(nl);
		buf.append("In caso di avvenuta restituzione, ignorare il presente sollecito.");
		ms.setModello(buf.toString());

		//modello e-mail
		buf.setLength(0);
		buf.append("[d]Spett.le [g][nomeUtente][/g][/d]").append(nl);
		buf.append(nl);
		buf.append(nl);
		buf.append("In riferimento al documento: [inventario]").append(nl);
		buf.append("Collocazione: [segnatura]").append(nl);
		buf.append(nl);
		buf.append("Titolo documento: [i][titolo][/i]").append(nl);
		buf.append(nl);
		buf.append("prestato in data: [dataPrestito]").append(nl);
		buf.append("Che doveva essere riconsegnato in data: [g][dataScadenza][/g]").append(nl);
		buf.append("Per il tipo Servizio: [tipoServizio]").append(nl);
		buf.append("Tessera lettore: [codUtente]").append(nl);
		buf.append(nl);
		buf.append(nl);
		buf.append("Si prega di restituire il documento al più presto.").append(nl);
		buf.append("In caso di avvenuta restituzione, ignorare il presente sollecito.");
		ms.setModelloMail(buf.toString());

		//modello sms
		buf.setLength(0);
		buf.append("Sollecito richiesta n. [numRichiesta]")
			.append(". Documento: [inventario]")
			.append(". Prestito scaduto il [dataScadenza]")
			.append(". Si prega di restituire al più presto.");
		ms.setModelloSms(buf.toString());

		return ms;
	}

	private static final Pattern SOLL_FIELD_REGEX = Pattern.compile("(\\[.+?\\])", Pattern.CASE_INSENSITIVE);

	public static final String costruisciModelloSollecito(ModelloSollecitoVO modello, TipoModello tipo,
			ElementoStampaSollecitoVO soll, boolean styled) throws Exception {
		TextProcessor tp = BBCodeParser.getParser(styled);

		StringBuilder buf = new StringBuilder(1024);
		if (styled)
			buf.append(HTTP_MODEL_HEADER);

		String text = null;
		switch (tipo) {
		case LETTERA:
			text = modello.getModello();
			break;
		case EMAIL:
			text = modello.getModelloMail();
			break;
		case SMS:
			text = modello.getModelloSms();
			break;
		}

		Scanner s = new Scanner(text);
		while (s.hasNextLine()) {
			String line = tp.process(s.nextLine());
			line = coalesce(costruisciRigaSollecito(line, soll, styled), !styled ? "" : "&nbsp;");
			if (styled) {
				if (line.startsWith("<p"))
					buf.append(line);
				else
					buf.append("<p>").append(line).append("</p>");
			} else
				buf.append(line);
		}
		s.close();
		if (styled)
			buf.append(HTTP_MODEL_FOOTER);

		return buf.toString();
	}

	static final String costruisciRigaSollecito(String text, ElementoStampaSollecitoVO soll, boolean styled) throws Exception {
		if (!isFilled(text))
			return null;

		ElementoStampaSollecitoDecorator s = new ElementoStampaSollecitoDecorator(soll);
		String tmp = new String(text);
		try {
			Matcher m = SOLL_FIELD_REGEX.matcher(tmp);
			while (m.find()) {
				String g = m.group().toLowerCase().replaceAll("\\[|\\]", "");
				CampoSollecito field = CampoSollecito.of(g);
				if (field == null)
					continue;

				String value = "";

				switch (field) {
				case CAP:
					value = s.getCap();
					break;
				case CITTA_NAZIONE:
					value = s.getCittaNazione();
					break;
				case COD_UTENTE:
					value = s.getCodUtente();
					break;
				case DATA_PRESTITO:
					value = s.getDataPrestito();
					break;
				case DATA_SCADENZA:
					value = s.getDataScadenza();
					break;
				case INDIRIZZO:
					value = s.getIndirizzo();
					break;
				case INVENTARIO:
					value = s.getInventario();
					break;
				case NOME_UTENTE:
					value = s.getNomeUtente();
					break;
				case NUM_SOLLECITO:
					value = s.getNumSollecito();
					break;
				case PROVINCIA:
					value = s.getProvincia();
					break;
				case SEGNATURA:
					value = s.getSegnatura();
					break;
				case TIPO_SERVIZIO:
					value = s.getTipoServizio();
					break;
				case TITOLO:
					value = s.getTitolo();
					break;
				case NUM_RICHIESTA:
					value = s.getIdRichiesta();
					break;
				}

				//almaviva5_20151022 #6012
				value = !styled ? value : StringEscapeUtils.escapeHtml(value);
				tmp = tmp.substring(0, m.start()) + value + tmp.substring(m.end());
				m = SOLL_FIELD_REGEX.matcher(tmp);
			}

		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "label.documentofisico.uri.model");
		}

		return tmp;
	}


	public static JasperPrint buildJasperPrint(String model) throws ApplicationException {
		try {
			//InputStream in = JasperReport.class.getResourceAsStream(JRXML_PATH + "htmlComponentReport.jrxml");
			//JasperReport report = JasperCompileManager.compileReport(in);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("html", model);

			SbnStampe ss = new SbnStampe("default_sollecito_html.jrxml");
			ss.setFormato(TipoStampa.PDF);
			ss.stampa(ValidazioneDati.asSingletonList(new ComboVO(model, null)), map);
			JasperPrint jp = ss.getJasperPrint();

			//JasperPrint jp = JasperFillManager.fillReport(report, map);
			return jp;
		} catch (Exception e) {
			throw new ApplicationException();
		}
	}

}
