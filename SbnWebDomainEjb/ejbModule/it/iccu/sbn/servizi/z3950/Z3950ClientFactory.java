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
package it.iccu.sbn.servizi.z3950;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;



public class Z3950ClientFactory {

	private static Logger log = Logger.getLogger(Z3950ClientFactory.class);

	private static String[][] NO_SORT_PAIR;

	static {
		try {
			//almaviva5_20130517 delimitatori caratteri no-sort
			NO_SORT_PAIR = new String[][] {
				{
					new String(new byte[] { (byte) 0xC2, (byte) 0x88 },	"UTF8"),	// UTF8 C288
					new String(new byte[] { (byte) 0xC2, (byte) 0x89 },	"UTF8")		// UTF8 C289
				},
				{
					new String(new byte[] { (byte) 0x1B, (byte) 0x48 },	"UTF8"), 	// UTF8 1B48
					new String(new byte[] { (byte) 0x1B, (byte) 0x49 },	"UTF8") 	// UTF8 1B49
				}
			};

		} catch (UnsupportedEncodingException e) { }
	}

	public static String stripNoSortDelimiters(String data) {
		for (String[] pair : NO_SORT_PAIR) {
			String nsb = pair[0];
			String nse = pair[1];

			//almaviva5_20130517 delimitatori caratteri no-sort
			if (data.contains(nsb))
				data = data.replace(nsb, "");

			if (data.contains(nse))
				data = data.replace(nse, "*");
		}

		return data;
	}

	public static Z3950Client getClient() throws Exception {
		String client = CommonConfiguration.getProperty("OPAC_Z3950_CLIENT");
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(client);
		Z3950Client z3950Client = (Z3950Client) clazz.newInstance();

		log.debug("creazione client z39.50: " + z3950Client);
		return z3950Client;
	}

	public static DocumentoNonSbnVO documentoFromMarc(Record rec) throws Exception {

		String leader = rec.getLeader().toString();

		DocumentoNonSbnVO doc = new DocumentoNonSbnVO();
		doc.setTipo_doc_lett('D');
		doc.setFonte('I');

		String id = sub(rec, "001", null);
		id = id.replaceAll("\\\\", "");	// es. IT\ICCU\CFIV\010140
		if (id.startsWith("ITICCU"))
			id = id.replaceAll("ITICCU", "");
		if (ValidazioneDati.leggiXID(id))
			doc.setBid(id);

		//autore principale
/*
		String autore = sub(rec, "700", "a") + sub(rec, "700", "b");		//personale
		if (!ValidazioneDati.isFilled(autore))
			autore = sub(rec, "710", "a") + sub(rec, "710", "b");			//ente
		doc.setAutore(autore);
*/
		doc.setAutore(getAutore(rec));

		doc.setLingua(sub(rec, "101", "a").toUpperCase());
		doc.setPaese(sub(rec, "102", "a").toUpperCase());
		doc.setEditore(sub(rec, "210", "c"));
		doc.setLuogoEdizione(sub(rec, "210", "a"));

		//anno pubblicazione
		String anno = "";
		anno = sub(rec, "210", "d");
		if (!ValidazioneDati.strIsNumeric(anno)) {
			//data da tag 100
			String $a = sub(rec, "100", "a");
			if (ValidazioneDati.length($a) >= 13)
				anno = $a.substring(9, 13); //data1
		}

		//doc.setAnnoEdizione(anno);
		doc.setDataPubb(anno);

		//tipo record
		doc.setTipoRecord(leader.charAt(6));

		doc.setNatura(getNatura(rec));

		//TODO numero standard (solo ISBN?)
		String isbn = sub(rec, "010", "a");
		if (ValidazioneDati.isFilled(isbn)) {
			TB_CODICI cod = CodiciProvider.cercaCodice("010", CodiciType.CODICE_TIPO_NUMERO_STANDARD, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
			if (cod != null) {
				doc.setTipoNumStd(cod.getCd_tabellaTrim());
				doc.setNumeroStd(isbn);
			}
		}

		//Ricostruzione titolo
		String isbd = ricostruisciIsbd(rec);
		doc.setTitolo(isbd);

		//biblioteche (tag 899)
		// $1RM0117$2BVE BA$fP/G
		List<BibliotecaVO> biblioteche = new ArrayList<BibliotecaVO>();
		List<VariableField> loc899s = rec.getVariableFields("899");
		for (VariableField vf : loc899s) {
			DataField _899 = (DataField) vf;
			BibliotecaVO bib = new BibliotecaVO();
			bib.setCd_ana_biblioteca(_899.getSubfield('1').getData());
			String codSbn = _899.getSubfield('2').getData();
			if (ValidazioneDati.length(codSbn) == 6) {
				bib.setCod_polo(codSbn.substring(0, 3));
				bib.setCod_bib(codSbn.substring(3));
				bib.setPaese("IT");
			}
			biblioteche.add(bib);
		}

		Collections.sort(biblioteche);
		doc.setBiblioteche(biblioteche);

		return doc;
	}

	private static String ricostruisciIsbd(final Record rec) {
		String leader = rec.getLeader().toString();

		StringBuilder isbd = new StringBuilder(1024);
		//isbd.append(concatenaSubFieldsTitolo(rec, "200"));
		isbd.append(getIsbd(rec));

		//TODO livello gerarchico
		char livello = leader.charAt(8);
		if (Character.isDigit(livello) )
			switch (livello) {
			case '2':
				String t461 = sub(rec, "461", "a");
				if (ValidazioneDati.isFilled(t461))
					isbd.append(" - ").append(concatenaSubFieldsTitolo(rec, "461"));

				String t462 = sub(rec, "462", "a");
				if (ValidazioneDati.isFilled(t462))
					isbd.append(" - ").append(concatenaSubFieldsTitolo(rec, "462"));
				break;
			}

		return stripNoSortDelimiters(isbd.toString());
	}

	private static String concatenaSubFieldsTitolo(Record rec, String tag) {

		//sottocampi	"a__c__e__f__g"
	    //sintassi		"; __ . __ : __ / __ ; "
	    //sintassi		"a; c . e : f / g ; "

		StringBuilder out = new StringBuilder(1024);
		out.append(sub(rec, tag, "a"));
		String $c = sub(rec, tag, "c");
		out.append(ValidazioneDati.isFilled($c) ? "; " : "").append($c);
		String $e = sub(rec, tag, "e");
		out.append(ValidazioneDati.isFilled($e) ? " . " : "").append($e);
		String $f = sub(rec, tag, "f");
		out.append(ValidazioneDati.isFilled($f) ? " : " : "").append($f);
		String $g = sub(rec, tag, "g");
		out.append(ValidazioneDati.isFilled($g) ? " / " : "").append($g);

		return out.toString();
	}

	private static String getIsbd(final Record rec) {
		StringBuilder buf = new StringBuilder(1240);

		List<VariableField> fields = rec.getVariableFields("200");
		for (VariableField vf : fields) {

			// (primo)$a;$a;$a ecc
			DataField df = (DataField) vf;

			boolean firstA = true;

			boolean parentesiB = false;
			int idxLastB = 0;
			boolean firstB = true;

			List<Subfield> subfields = df.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {

				case 'a':
					if (firstA) {
						buf.append(subfield.getData());
						firstA = false;
					} else {
						buf.append(" ; ").append(subfield.getData());
					}
					break;

				case 'b':
					if (firstB) {
						buf.append(" [").append(subfield.getData());
						firstB = false;
						parentesiB = true;
						idxLastB = buf.length();
					} else {
						buf.append(" ").append(subfield.getData());
						idxLastB = buf.length();
					}
					break;

				case 'c':
					buf.append(" . ").append(subfield.getData());
					break;

				case 'd':
					buf.append(" = ").append(subfield.getData());
					break;

				case 'e':
					buf.append(" : ").append(subfield.getData());
					break;

				case 'f':
					buf.append(" / ").append(subfield.getData());
					break;

				case 'g':
					buf.append(" ; ").append(subfield.getData());
					break;

				case 'v':
					buf.append(" ; ").append(subfield.getData());
					break;

				default:
					break;
				}
			}

			// inserisco la parentesi dopo l'ultima b inserita
			if (parentesiB) {
				buf.insert(idxLastB, "] ");
			}

		}

		return buf.toString();
	}

	private static String getAutore(final Record rec) {

		StringBuilder buf = new StringBuilder(160);

		List<VariableField> fields = rec.getVariableFields("700");
		for (VariableField vf : fields) {
			DataField df = (DataField) vf;
			List<Subfield> subfields = df.getSubfields();
			for (Subfield sf : subfields) {
				if (sf.getCode() != '3' && sf.getCode() != '4') {
					buf.append(sf.getData());
				}
			}
		}

		fields = rec.getVariableFields("710");
		for (VariableField vf : fields) {
			DataField df = (DataField) vf;
			List<Subfield> subfields = df.getSubfields();
			for (Subfield sf : subfields) {
				if (sf.getCode() != '3' && sf.getCode() != '4') {
					buf.append(sf.getData());
				}
			}
		}

		return buf.toString();
	}

	private static String sub(Record rec, String tag, String sub) {
		VariableField f = rec.getVariableField(tag);
		if (f == null)
			return "";

		if (f instanceof DataField) {
			DataField df = (DataField) f;
			StringBuilder buf = new StringBuilder(512);
			Iterator<Subfield> i = df.getSubfields(sub).iterator();
			while (i.hasNext()) {
				buf.append(' ');
				buf.append(i.next().getData());
			}
			return ValidazioneDati.trimOrEmpty(buf.toString());
		}

		if (f instanceof ControlField) {
			ControlField cf = (ControlField) f;
			return ValidazioneDati.trimOrEmpty(cf.getData());
		}

		return "";
	}

	private static char getNatura(final Record record) {
		String natura = null;

		String leader = record.getLeader().toString();
		if (leader.charAt(9) != ' ') {
			natura = String.valueOf(leader.charAt(9));
		} else {
			DataField tit = (DataField) record.getVariableField("200");
			if (leader.charAt(7) == 'm' && tit != null && tit.getIndicator1() == '0') {
				natura = "w";
			} else {
				natura = String.valueOf(leader.charAt(7));
			}
		}
		return natura.toUpperCase().charAt(0);
	}

	public static List<TB_CODICI> getCodiciNumeroStandardZ3950() throws Exception {
		//filtra i numeri standard sui codici gestiti in Z39.50
		PQFBuilder builder = new PQFBuilder();
		List<TB_CODICI> codici = new ArrayList<TB_CODICI>(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_NUMERO_STANDARD, true) );
		Iterator<TB_CODICI> i = codici.iterator();
		while (i.hasNext())
			if (!builder.numeroStdBib1Attr.containsKey(i.next().getCd_tabellaTrim()))
				i.remove();

		codici.add(0, new TB_CODICI("",  ""));
		return codici;
	}

}
