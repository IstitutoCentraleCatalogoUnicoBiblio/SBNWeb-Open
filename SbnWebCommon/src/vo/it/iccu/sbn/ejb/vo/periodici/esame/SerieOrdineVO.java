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
package it.iccu.sbn.ejb.vo.periodici.esame;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.SerializableComparator;

import java.util.Date;
import java.util.List;

public class SerieOrdineVO extends SerieBaseVO {

	private static final long serialVersionUID = 5150639766232283516L;

	public static final SerializableComparator<SerieOrdineVO> NATURAL_ORDER = new SerializableComparator<SerieOrdineVO>() {

		private static final long serialVersionUID = 8506070690034468713L;

		public int compare(SerieOrdineVO o1, SerieOrdineVO o2) {
			int cmp = o1.anno_ord - o2.anno_ord;
			cmp = (cmp != 0) ? cmp : o1.cod_tip_ord - o2.cod_tip_ord;
			cmp = (cmp != 0) ? cmp : o1.cod_ord - o2.cod_ord;
			//cmp = (cmp == 0) ? super.compareTo(o2) : cmp;
			return cmp;
		}
	};

	private String cod_bib_ord;
	private char cod_tip_ord;
	private int anno_ord;
	private int cod_ord;
	private int id_ordine;
	private String fornitore;
	private int id_fornitore;
	private List<Integer> ordiniPrecedenti;
	private String bid;
	private int annoAbb;

	private Date data_fasc;
	private Date data_fine;

	public String getCod_bib_ord() {
		return cod_bib_ord;
	}

	public void setCod_bib_ord(String cod_bib_ord) {
		this.cod_bib_ord = cod_bib_ord;
	}

	public char getCod_tip_ord() {
		return cod_tip_ord;
	}

	public void setCod_tip_ord(char cod_tip_ord) {
		this.cod_tip_ord = cod_tip_ord;
	}

	public int getAnno_ord() {
		return anno_ord;
	}

	public void setAnno_ord(int anno_ord) {
		this.anno_ord = anno_ord;
	}

	public int getCod_ord() {
		return cod_ord;
	}

	public void setCod_ord(int cod_ord) {
		this.cod_ord = cod_ord;
	}

	public int getId_ordine() {
		return id_ordine;
	}

	public void setId_ordine(int id_ordine) {
		this.id_ordine = id_ordine;
	}

	public String getDescrizione() {
		StringBuilder buf = new StringBuilder();
		if (cod_ord > 0) {
			buf.append(anno_ord);
			buf.append(" ");
			buf.append(cod_tip_ord);
			buf.append(" ");
			buf.append(" ");
			buf.append(cod_ord);
		}
		return buf.toString();
	}

	public String getAbbonamentoDal() {
		if (data_fasc != null && data_fine != null)
			return DateUtil.formattaData(data_fasc);
		return "";
	}

	public String getAbbonamentoAl() {
		if (data_fasc != null && data_fine != null)
			return DateUtil.formattaData(data_fine);
		return "";
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = trimAndSet(fornitore);
	}

	public int getId_fornitore() {
		return id_fornitore;
	}

	public void setId_fornitore(int id_fornitore) {
		this.id_fornitore = id_fornitore;
	}

	public List<Integer> getOrdiniPrecedenti() {
		return ordiniPrecedenti;
	}

	public void setOrdiniPrecedenti(List<Integer> ordiniPrecedenti) {
		this.ordiniPrecedenti = ordiniPrecedenti;
	}

	public int compareTo(SerieOrdineVO o) {
		return NATURAL_ORDER.compare(this, o);
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public int getAnnoAbb() {
		return annoAbb;
	}

	public void setAnnoAbb(int annoAbb) {
		this.annoAbb = annoAbb;
	}

	public Date getData_fasc() {
		return data_fasc;
	}

	public void setData_fasc(Date data_fasc) {
		this.data_fasc = data_fasc;
	}

	public Date getData_fine() {
		return data_fine;
	}

	public void setData_fine(Date data_fine) {
		this.data_fine = data_fine;
	}

	@Override
	public boolean isEmpty() {
		return id_ordine == 0
			&& 	!(cod_ord > 0 && anno_ord > 0 && isFilled(cod_tip_ord) );
	}

}
