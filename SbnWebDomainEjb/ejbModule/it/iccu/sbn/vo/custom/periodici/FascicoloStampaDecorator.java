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
package it.iccu.sbn.vo.custom.periodici;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.common.SerializableComparator;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;

import java.util.Date;

public class FascicoloStampaDecorator extends FascicoloDecorator {

	private static final long serialVersionUID = -3271264536048826329L;

	private static final OrdinamentoUnicode unicode = new OrdinamentoUnicode();

	public static final SerializableComparator<FascicoloVO> ORDINAMENTO_NOME_FORNITORE = new SerializableComparator<FascicoloVO>() {

		private static final long serialVersionUID = 5298545194622406799L;

		public int compare(FascicoloVO f1, FascicoloVO f2) {
			FascicoloStampaDecorator fsd1 = (FascicoloStampaDecorator) f1;
			FascicoloStampaDecorator fsd2 = (FascicoloStampaDecorator) f2;
			int cmp = ValidazioneDati.compare(fsd1.chiave_for, fsd2.chiave_for);
			cmp = (cmp != 0) ? cmp : compareChiaveOrdine(fsd1, fsd2);
			cmp = (cmp != 0) ? cmp : ValidazioneDati.invertiComparatore(ORDINAMENTO_FASCICOLO).compare(f1, f2);

			return cmp;
		}
	};

	public static final SerializableComparator<FascicoloVO> ORDINAMENTO_DATA_ORDINE = new SerializableComparator<FascicoloVO>() {

		private static final long serialVersionUID = 5298545194622406799L;

		public int compare(FascicoloVO f1, FascicoloVO f2) {
			FascicoloStampaDecorator fsd1 = (FascicoloStampaDecorator) f1;
			FascicoloStampaDecorator fsd2 = (FascicoloStampaDecorator) f2;
			int cmp = ValidazioneDati.compare(fsd1.data_ord, fsd2.data_ord);
			cmp = (cmp != 0) ? cmp : compareChiaveOrdine(fsd1, fsd2);
			cmp = (cmp != 0) ? cmp : ValidazioneDati.invertiComparatore(ORDINAMENTO_FASCICOLO).compare(f1, f2);

			return cmp;
		}
	};

	public static final SerializableComparator<FascicoloVO> ORDINAMENTO_TIPO_ORDINE = new SerializableComparator<FascicoloVO>() {

		private static final long serialVersionUID = 5298545194622406799L;

		public int compare(FascicoloVO f1, FascicoloVO f2) {
			FascicoloStampaDecorator fsd1 = (FascicoloStampaDecorator) f1;
			FascicoloStampaDecorator fsd2 = (FascicoloStampaDecorator) f2;
			int cmp = ValidazioneDati.compare(fsd1.cod_tip_ord, fsd2.cod_tip_ord);
			cmp = (cmp != 0) ? cmp : compareChiaveOrdine(fsd1, fsd2);
			cmp = (cmp != 0) ? cmp : ValidazioneDati.invertiComparatore(ORDINAMENTO_FASCICOLO).compare(f1, f2);

			return cmp;
		}
	};

	private static final int compareChiaveOrdine(FascicoloStampaDecorator o1, FascicoloStampaDecorator o2) {
		int cmp = o1.anno_ord - o2.anno_ord;
		cmp = (cmp != 0) ? cmp : o1.cod_tip_ord - o2.cod_tip_ord;
		cmp = (cmp != 0) ? cmp : o1.cod_ord - o2.cod_ord;
		return cmp;
	}

	private final int cod_fornitore;
	private final String nom_fornitore;
	private final String chiave_for;
	private char cod_tip_ord;
	private int anno_ord;
	private int cod_ord;
	private Date data_ord;

	private String isbd;

	public FascicoloStampaDecorator(FascicoloVO f, Tba_ordini ordine) {
		super(f);
		Tbr_fornitori fornitore = ordine.getCod_fornitore();

		this.cod_fornitore = fornitore.getCod_fornitore();
		this.nom_fornitore = trimOrEmpty(fornitore.getNom_fornitore());
		this.chiave_for = unicode.convert(nom_fornitore);

		this.cod_tip_ord = ordine.getCod_tip_ord();
		this.anno_ord = ordine.getAnno_ord().intValue();
		this.cod_ord = ordine.getCod_ord();

		this.data_ord = ordine.getData_ins();

	}

	public String getNom_fornitore() {
		return nom_fornitore;
	}

	public int getCod_fornitore() {
		return cod_fornitore;
	}

	@Override
	public String getChiaveOrdine() {
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

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

}
