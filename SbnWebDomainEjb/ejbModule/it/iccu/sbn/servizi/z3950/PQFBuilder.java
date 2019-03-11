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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.util.Jdk5Deque;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PQFBuilder extends SerializableVO {

	private static final long serialVersionUID = 5371138991613973341L;

	private Jdk5Deque<String> expr = new Jdk5Deque<String>();
	private List<String> filtriTipoRecord = new ArrayList<String>();

	Map<String, String> numeroStdBib1Attr = new HashMap<String, String>();

	public static String buildPQFQuery(DocumentoNonSbnRicercaVO filtro) throws ValidationException {
		PQFBuilder builder = new PQFBuilder();

		return builder.setTitolo(filtro.getTitolo())
			.setNatura(filtro.getNatura())
			.addTipoRecord(ValidazioneDati.coalesce(filtro.getTipoRecord(), '\u0020').toString())//.addTipoRecord("a")
			.setNumeroStandard(filtro.getTipoNumStd(), filtro.getNumeroStd())
			.setId(filtro.getBid())
			.setAutore(filtro.getAutore())
			.setDataDa(filtro.getAnnoDa())
			.setDataA(filtro.getAnnoA())
			.addTipoRecord(filtro.getTipoRecord1())
			.addTipoRecord(filtro.getTipoRecord2())
			.addTipoRecord(filtro.getTipoRecord3())
			.build();
	}

	private PQFBuilder setDataA(String data) {
		if (isFilled(data)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			expr.add(String.format("@attr 1=31 @attr 4=4 @attr 2=2 \"%s\"", data));
		}

		return this;
	}

	private PQFBuilder setDataDa(String data) {
		if (isFilled(data)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			expr.add(String.format("@attr 1=31 @attr 4=4 @attr 2=4 \"%s\"", data));
		}

		return this;
	}

	private PQFBuilder setAutore(String autore) {
		if (isFilled(autore)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

//			expr.add("@or");
//			expr.add("@or");
//			expr.add("@or");
			expr.add(String.format("@attr 1=1003 \"%s\"", autore));
//			expr.add(String.format("@attr 1=1004 \"%s\"", autore));
//			expr.add(String.format("@attr 1=1005 \"%s\"", autore));
//			expr.add(String.format("@attr 1=1006 \"%s\"", autore));
		}

		return this;
	}

	private PQFBuilder setId(String id) {
		if (isFilled(id)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			expr.add(String.format("@attr 6=3 @attr 1=1032 %s", id));
		}

		return this;
	}

	PQFBuilder() {
		super();
		numeroStdBib1Attr.put("I", "7");		//ISBN
		numeroStdBib1Attr.put("J", "8");		//ISSN
		numeroStdBib1Attr.put("B", "48");		//BNI
		numeroStdBib1Attr.put("M", "1092"); 	//ISMN

		numeroStdBib1Attr.put("E", "51");
		numeroStdBib1Attr.put("Y", "6010");
		numeroStdBib1Attr.put("N", "5022");
		numeroStdBib1Attr.put("L", "1202");

		//numeroStdBib1Attr.put("X", "5009");
		//numeroStdBib1Attr.put("O", "3006");
		//numeroStdBib1Attr.put("U", "5092");
		//numeroStdBib1Attr.put("V", "1091");
		//numeroStdBib1Attr.put("G", "50");
		//numeroStdBib1Attr.put("F", "1201");
		//numeroStdBib1Attr.put("T", "1214");
		//numeroStdBib1Attr.put("Q", "5214");
		//numeroStdBib1Attr.put("A", "5710");
		//numeroStdBib1Attr.put("H", "5714");
		//numeroStdBib1Attr.put("O", "5716");
	}

	private PQFBuilder setNumeroStandard(String tipoNumStd, String numeroStd) {
		String attr;
		if (isFilled(tipoNumStd) && isFilled(numeroStd) && (isFilled(attr = numeroStdBib1Attr.get(tipoNumStd))) ) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			expr.add(String.format("@attr 1=%s \"%s\"", attr, numeroStd));
		}

		return this;
	}

	public PQFBuilder addTipoRecord(String tipoRecord) {
		if (isFilled(tipoRecord))
			filtriTipoRecord.add(tipoRecord);

		return this;
	}

	private PQFBuilder setTipoRecord() {
		if (isFilled(filtriTipoRecord)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			int size = size(filtriTipoRecord);

			if (size == 1)
				expr.add(String.format("@attr 1=1001 %s", filtriTipoRecord.get(0)));
			else {
				for (int i = 1; i < size; i++)
					expr.add("@or");
				for (String tr : filtriTipoRecord)
					expr.add(String.format("@attr 1=1001 %s", tr));
			}
		}

		return this;
	}

	public PQFBuilder setNatura(Character natura) {
		if (isFilled(natura)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			expr.add(String.format("@attr 1=1021 %s", Character.toLowerCase(natura)));
		}

		return this;
	}

	public PQFBuilder setTitolo(String titolo) {
		if (isFilled(titolo)) {
			if (!expr.isEmpty())
				expr.addFirst("@and");

			expr.add(String.format("@attr 1=4 \"%s\"", titolo));
			//expr.add(String.format("@attr 1=1016 \"%s\"", titolo));
			//expr.add(String.format("@attr 1=4 @attr 4=6 \"%s\"", titolo));
		}

		return this;
	}

	public String build() throws ValidationException {
		//String pqf = String.format("@or @attr 1=4 \"%s\" @attr 7=1 @attr 1=4 0", titolo);

		//String pqf = String.format("@or @attr 1=1016 \"%s\" @attr 7=1 @attr 1=4 0", titolo);
		//String.format("@or @and @attr 1=1016 \"%s\" @attr 1=1021 m @attr 7=1 @attr 1=4 0", titolo);
		setTipoRecord();

		if (expr.isEmpty())
			throw new ValidationException(SbnErrorTypes.SRV_ILL_ERRORE_Z3950_QUERY);

		StringBuilder pqf = new StringBuilder(256);
		//pqf.append("@or");

		while (!expr.isEmpty()) {
			pqf.append(' ').append(expr.poll());
		}

		//pqf.append(" @attr 7=1 @attr 1=4 0");

		return pqf.toString();
	}
}
