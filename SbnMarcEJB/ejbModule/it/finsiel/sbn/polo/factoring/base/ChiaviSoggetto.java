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
package it.finsiel.sbn.polo.factoring.base;

import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.isFilled;

import gnu.trove.THashSet;

import it.finsiel.gateway.intf.KeySoggetto;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A_250;
import it.iccu.sbn.ejb.model.unimarcmodel.X_250;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChiaviSoggetto implements KeySoggetto {

	private static final long serialVersionUID = -902458428486990525L;

	private static final char[][] COPPIA_DELIMITATORI = new char[][] {
		{'(', ')'},
		{'<', '>'},
		{'[', ']'},
		{'{', '}'},
	};

	static final String REGEX_SEPARATORE_TERMINI_SOGGETTO = "\\s-\\s";
	static final String FAKE_SEPARATORE = "fakeDesSep" + System.currentTimeMillis();
	static final String SEPARATORE_TERMINI_SOGGETTO = " - ";
	static final Pattern SEPARATORE_DES = Pattern.compile(REGEX_SEPARATORE_TERMINI_SOGGETTO);

	private String ky_cles1_a;
	private List<String> descrittori;
	private int countDescrittori;
	private A250 _t250;

	public static KeySoggetto build(String codSogg, SbnEdizioneSoggettario edizione, A250 _t250) throws EccezioneSbnDiagnostico {

		ChiaviSoggetto key = new ChiaviSoggetto();
		key._t250 = _t250;
		key.ky_cles1_a = NormalizzaNomi.normalizzaSoggetto(key._t250);

		String[] vettoreDescrittori = preparaDescrittori(key._t250);
		key.descrittori = Arrays.asList(vettoreDescrittori);
		key.countDescrittori = vettoreDescrittori.length;

		return key;
	}

	public static KeySoggetto build(String codSogg, SbnEdizioneSoggettario edizione, String testoSoggetto) throws EccezioneSbnDiagnostico {
		if (!isFilled(testoSoggetto))
			return null;

		return build(codSogg, edizione, costruisciSbnMarcStringaSoggetto(testoSoggetto) );
	}

	private ChiaviSoggetto() {
		super();
	}

	public String getKy_cles1_a() {
		return ky_cles1_a;
	}

	public List<String> getDescrittori() {
		return descrittori;
	}

	public int getCountDescrittori() {
		return countDescrittori;
	}

	public A250 getA250() {
		return _t250;
	}

	@Override
	public String toString() {
		return "KeySoggetto [" + (ky_cles1_a != null ? "ky_cles1_a=" + ky_cles1_a : "") + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KeySoggetto))
			return false;

		try {
			KeySoggetto ks = (KeySoggetto) o;
			if (!ValidazioneDati.equals(this.ky_cles1_a, ks.getKy_cles1_a()) )
				return false;

			if (this.countDescrittori != ks.getCountDescrittori())
				return false;

			Set<String> dd = new THashSet<String>();
			for (String d1 : this.descrittori)
				dd.add(NormalizzaNomi.normalizzaDescrittore(d1));

			for (String d2 : ks.getDescrittori())
				if (!dd.contains(NormalizzaNomi.normalizzaDescrittore(d2)))
					return false;

			return true;

		} catch (EccezioneSbnDiagnostico e) {
			return false;
		}
	}

	static final String[] preparaDescrittori(A250 _t250) {
		int len = _t250.getX_250().length;
		String[] result = new String[len + 1];
		result[0] = ValidazioneDati.trimOrEmpty(_t250.getA_250().getContent());
		for (int i = 0; i < len; i++) {
			String d = _t250.getX_250(i).getContent();
			result[i + 1] = ValidazioneDati.trimOrEmpty(d);
		}

		//almaviva5_20120416 evolutive CFI
		//nuova suddivisione descrittori (da Indice)
		List<String> dd = new ArrayList<String>();
		for (String d : result) {
			String[] altri = d.split(NormalizzaNomi.REGEX_DESCRITTORI_SEP_ULTERIORI);
			for (String altro : altri)
				if (ValidazioneDati.isFilled(altro))
					dd.add(altro.trim());
		}

		result = dd.toArray(new String[0]);

		return result;
	}


	public static final A250 costruisciSbnMarcStringaSoggetto(String testo) throws EccezioneSbnDiagnostico {
		String[] vociSogg = preparaTestoDescrittori(testo);
		A250 a250 = new A250();
		A_250 a_250 = new A_250();
		a_250.setContent(vociSogg[0]);
		// Prima voce della Stringa di Soggetto
		a250.setA_250(a_250);

		if (vociSogg.length > 1)
			// Altre voci della Stringa di Soggetto
			for (int x = 1; x < vociSogg.length; x++) {
				X_250 x_250 = new X_250();
				x_250.setContent(vociSogg[x]);
				a250.addX_250(x_250);
			}

		return a250;
	}

	static final String[] preparaTestoDescrittori(String testoSoggetto) throws EccezioneSbnDiagnostico {
		if (!isFilled(testoSoggetto))
			return null;

		//il testo tra delimitatori non deve essere spezzato in descrittori singoli
		testoSoggetto = mark_testoDelimitato(testoSoggetto);
		String[] descrittori = testoSoggetto.trim().split(REGEX_SEPARATORE_TERMINI_SOGGETTO);
		// validazione dei descrittori
		List<String> tmp = new ArrayList<String>(descrittori.length);
		for (String des : descrittori) {
			String normDes = UnicodeForOrdinamento2.convert(des);
			if (isFilled(normDes))
				tmp.add(des.trim().replaceAll(FAKE_SEPARATORE, SEPARATORE_TERMINI_SOGGETTO));
		}

		if (isFilled(tmp))
			return tmp.toArray(new String[0]);

		return null;
	}

	protected static final String mark_testoDelimitato(String testo) {
		String tmp = new String(testo);
		for (char[] coppia : COPPIA_DELIMITATORI) {
			String regex = "\\" + coppia[0] + ".*?\\" + coppia[1];
			Pattern p = Pattern.compile(regex);
			while (true) {
				Matcher m = p.matcher(tmp);
				if (!m.find()) break;

				String group = m.group();
				if (SEPARATORE_DES.matcher(group).find())
					tmp = m.replaceFirst( group.replaceAll(REGEX_SEPARATORE_TERMINI_SOGGETTO, FAKE_SEPARATORE) );
				else break;
			}
		}

		return tmp;
	}

	public static String normalizzaDescrittore(String descr) throws EccezioneSbnDiagnostico {
		return NormalizzaNomi.normalizzaDescrittore(descr);
	}

	public static void main(String[] args) throws EccezioneSbnDiagnostico {
		String testoSoggetto = "le *feste [per i] bambini . Mario Rossi <1950 - 2000>";

		KeySoggetto key = ChiaviSoggetto.build(null, null, testoSoggetto);
		System.out.println(key.getKy_cles1_a() );
		for (String des : key.getDescrittori() )
			System.out.println(des);
	}

}
