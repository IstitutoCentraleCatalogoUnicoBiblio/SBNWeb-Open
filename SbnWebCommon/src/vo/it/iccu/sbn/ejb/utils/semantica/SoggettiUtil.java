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
package it.iccu.sbn.ejb.utils.semantica;

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A_250;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.X_250;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.util.Constants.Semantica.Soggetti;
import it.iccu.sbn.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

public class SoggettiUtil {

	private static final char[][] COPPIA_DELIMITATORI = new char[][] {
		{'(', ')'},
		{'<', '>'},
		{'[', ']'},
		{'{', '}'},
	};

	private static final Pattern SEPARATORE_DES = Pattern.compile(Soggetti.REGEX_SEPARATORE_TERMINI_SOGGETTO);
	private static final String FAKE_SEPARATORE = "fakeDesSep" + IdGenerator.getId();

	private static final Pattern REGEX_SEPARATORI_SOGGETTO = Pattern.compile("(\\s-\\s)|(\\s\\[.*?\\]\\s)|(\\s\\.\\s)");

	public static final int MAX_LEN_SOGGETTO_INDICE = 240;
	//private static final int MAX_LEN_DESCRITTORE_INDICE = 80;

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
					tmp = m.replaceFirst( group.replaceAll(Soggetti.REGEX_SEPARATORE_TERMINI_SOGGETTO, FAKE_SEPARATORE) );
				else break;
			}
		}

		return tmp;
	}

	public static final String eliminaAsterischi(String value) {
		int idx = -1;
		do {
			idx = value.indexOf('*');
			value = value.substring(idx + 1);
		} while (idx >= 0);

		return value;
	}

	protected static final int getPosizioneDescrittoreAutomatico(String testoSoggetto,	String testoDescrittore) {

		// tolgo doppi spazi
		testoSoggetto = testoSoggetto.replaceAll("\\s+", " ").trim();
		testoDescrittore = testoDescrittore.replaceAll("\\s+", " ").trim();

		OrdinamentoUnicode u = OrdinamentoUnicode.getInstance();
		String desNormalizzato = u.convert(testoDescrittore).trim();

		String[] tokens = preparaTestoDescrittori(testoSoggetto);

		for (int i = 0; i < tokens.length; i++) {
			String desAutNormalizzato = u.convert(tokens[i]);
			if (desAutNormalizzato.trim().equalsIgnoreCase(desNormalizzato))
				return i;
		}

		return -1;
	}

	static final String[] preparaTestoDescrittori(String testoSoggetto) {
		if (!isFilled(testoSoggetto))
			return null;

		//il testo tra delimitatori non deve essere spezzato in descrittori singoli
		testoSoggetto = mark_testoDelimitato(testoSoggetto);
		String[] descrittori = testoSoggetto.trim().split(Soggetti.REGEX_SEPARATORE_TERMINI_SOGGETTO);
		// validazione dei descrittori
		OrdinamentoUnicode u = new OrdinamentoUnicode();
		List<String> tmp = new ArrayList<String>(descrittori.length);
		for (String des : descrittori) {
			String normDes = u.convert(des);
			if (isFilled(normDes))
				tmp.add(des.trim().replaceAll(FAKE_SEPARATORE, Soggetti.SEPARATORE_TERMINI_SOGGETTO));
		}

		if (isFilled(tmp))
			return tmp.toArray(new String[0]);

		return null;
	}

	public static void main(String[] args) {
		String[] descrittori = preparaTestoDescrittori("le *feste [per i] bambini");
		for (String des : descrittori)
			System.out.println(des);
	}

	public static final String costruisciStringaSoggetto(SoggettoType soggettoType) {
		return costruisciStringaSoggetto(soggettoType, Soggetti.SEPARATORE_TERMINI_SOGGETTO);
	}

	public static final String costruisciStringaSoggetto(SoggettoType soggettoType, String separatore) {
		A250 t250 = soggettoType.getT250();
		StringBuilder buf = new StringBuilder(240);

		buf.append(t250.getA_250().getContent());

		if (t250.getX_250() != null)
			for (int i = 0; i < t250.getX_250().length; i++) {
				String d = t250.getX_250(i).getContent();
				if (isFilled(d))
					//descrittore con separatore in testa?
					if (in(d.charAt(0), '[', '.'))
						buf.append(' ').append(d);
					else
						//separatore classico
						buf.append(separatore).append(d);
			}

		return buf.toString();
	}

	static final A250 costruisciSbnMarcStringaSoggetto(String testo) {
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

	static final boolean isSoggettoEquals(String testoSoggetto1, String testoSoggetto2) {

		OrdinamentoUnicode unicode = OrdinamentoUnicode.getInstance();

		String s1 = unicode.convert(testoSoggetto1).trim().replaceAll("\\s+", " ");
		String s2 = unicode.convert(testoSoggetto2).trim().replaceAll("\\s+", " ");
		if (!s1.equals(s2))
			return false;

		//check descrittori automatici
		List<String> dd1 = scomponiSoggettoInDescrittori(testoSoggetto1);
		List<String> dd2 = scomponiSoggettoInDescrittori(testoSoggetto2);
		if (dd1.size() != dd2.size())
			return false;

		Set<String> dd = new THashSet<String>();
		for (String d1 : dd1)
			dd.add(normalizzaDescrittore(d1));

		for (String d2 : dd2)
			if (!dd.contains(normalizzaDescrittore(d2)))
				return false;

		return true;
	}

	public static final List<String> scomponiSoggettoInDescrittori(final String ds_sogg) {

		List<String> descrittori = new ArrayList<String>();

		String tmp = new String(ds_sogg);
		Matcher m = REGEX_SEPARATORI_SOGGETTO.matcher(tmp);

		boolean found = m.find();
		while (found) {
			String d = tmp.substring(0, m.start());
			descrittori.add(trimOrEmpty(d) );

			tmp = tmp.substring(m.end());
			m = REGEX_SEPARATORI_SOGGETTO.matcher(tmp);
			found = m.find();
		}
		if (isFilled(tmp))
			descrittori.add(trimOrEmpty(tmp) );

		return descrittori;
	}

	public static final String normalizzaDescrittore(final String input) {

		if (!isFilled(input))
			return "";

		//almaviva5_20120416 evolutive CFI
		//nuova suddivisione descrittori (da Indice)
		String result = eliminaAsterischi(input);
		result = trimOrEmpty(OrdinamentoUnicode.getInstance().convert(result));

		return result.replaceAll("\\s+", " ");

	}

	public static final boolean isEdizioneCompatibile(SbnEdizioneSoggettario e1, SbnEdizioneSoggettario e2) {
		int t1 = e1.getType();
		int t2 = e2.getType();

		switch (t1) {
		case SbnEdizioneSoggettario.I_TYPE:
			return in(e2, SbnEdizioneSoggettario.I, SbnEdizioneSoggettario.E);

		case SbnEdizioneSoggettario.N_TYPE:
			return in(e2, SbnEdizioneSoggettario.N, SbnEdizioneSoggettario.E);

		case SbnEdizioneSoggettario.E_TYPE:
			return true;
 		}

		return t1 == t2;
	}

	public static boolean checkSoggettoPerIndice(String testoSoggetto) {
		// controllo lunghezza per invio in indice
		if (ValidazioneDati.trimOrEmpty(testoSoggetto).length() > MAX_LEN_SOGGETTO_INDICE) {
			return false;
		}

		//almaviva5_20110314 controllo eliminato a fronte risoluzione bug indice.

		/*
		// controllo lunghezza singoli descrittori
		String[] descrittori = testoSoggetto.trim().split(Constants.SEPARATORE_TERMINI_SOGGETTO);
		for (String descrittore : descrittori) {
			if (descrittore.trim().length() > SbnSoggettiDAO.MAX_LEN_DESCRITTORE_INDICE) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.gestioneSemantica.indiceMaxLenDesc",
						SbnSoggettiDAO.MAX_LEN_DESCRITTORE_INDICE));

				return false;
			}
		}
		*/
		return true;
	}

	static ElementoSinteticaSoggettoVO trovaSimileConTestoEsatto(
			String testoSoggetto, List<ElementoSinteticaSoggettoVO> listaSimili) {

		for (ElementoSinteticaSoggettoVO simile : listaSimili) {
			if (SoggettiUtil.isSoggettoEquals(testoSoggetto, simile.getTesto()))
				return simile;
		}
		return null;
	}

}
