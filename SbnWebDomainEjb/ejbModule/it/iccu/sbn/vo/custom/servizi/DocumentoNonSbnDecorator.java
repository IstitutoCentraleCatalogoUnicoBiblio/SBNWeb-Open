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
package it.iccu.sbn.vo.custom.servizi;

import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.util.Comparator;

public class DocumentoNonSbnDecorator extends DocumentoNonSbnVO {

	public static class Sezioni {
		public static final String DOC_DATI_SPOGLIO = "DOC_DATI_SPOGLIO";
		public static final String DOC_BID_TITOLO = "DOC_BID_TITOLO";
		public static final String DOC_NATURA = "DOC_NATURA";
		public static final String DOC_TIPO_DOCUMENTO = "DOC_TIPO_DOCUMENTO";
		public static final String DOC_BIBLIOTECHE = "DOC_BIBLIOTECHE";
	}

	private static final long serialVersionUID = -2199784341790469275L;

	private String categoriaFruizione;
	private String nonDisponibile;
	private String kcles;

	private static final String togliAsterisco(String value) {
		if (isNull(value))
			return "";
		int idx = value.indexOf("*");

		return (idx > -1  && idx < (value.length() - 1) ) ? value.substring(idx + 1) : value;
	}


	public static final Comparator<DocumentoNonSbnVO> ORDINA_PER_TITOLO = new Comparator<DocumentoNonSbnVO>() {
		public int compare(DocumentoNonSbnVO o1, DocumentoNonSbnVO o2) {
			DocumentoNonSbnDecorator dnsd1 = (DocumentoNonSbnDecorator) o1;
			DocumentoNonSbnDecorator dnsd2 = (DocumentoNonSbnDecorator) o2;
			return dnsd1.kcles.compareTo(dnsd2.kcles);
		}
	};

	public DocumentoNonSbnDecorator(DocumentoNonSbnVO doc) {
		super(doc);
		this.kcles = OrdinamentoCollocazione2.normalizza(togliAsterisco(titolo));
		if (doc.getTipo_doc_lett() == 'P')
			try {
				this.categoriaFruizione = trimOrBlank(CodiciProvider.cercaDescrizioneCodice(codFruizione, CodiciType.CODICE_CATEGORIA_FRUIZIONE, CodiciRicercaType.RICERCA_CODICE_SBN));
				this.nonDisponibile = trimOrBlank(CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN));
			} catch (Exception e) {
				return;
			}
	}

	public DocumentoNonSbnDecorator(DocumentoNonSbnDecorator dd) {
		super(dd);
	    this.categoriaFruizione = dd.categoriaFruizione;
	    this.nonDisponibile = dd.nonDisponibile;
	    this.kcles = dd.kcles;
	}

	public String getCategoriaFruizione() {
		return categoriaFruizione;
	}

	public String getNonDisponibile() {
		return nonDisponibile;
	}

	public String getFormattedMarcRecord() {
		if (!isFilled(marcRecord))
			return "";

		StringBuilder buf = new StringBuilder(1024);
		for (String row : marcRecord.split("\\n"))
			buf.append(row).append("<br/>");

		return buf.toString();
	}

	public int getNumBiblioteche() {
		return size(super.getBiblioteche());
	}

}
