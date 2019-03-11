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
//almaviva5_20061018
package it.iccu.sbn.web.actions.servizi.util;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.Calendar;

public class CodiceFiscale {

	private String Nome;
	private String Cognome;
	private Calendar DataDiNascita;
	private String LuogoDiNascita;
	private char Sesso;
	private String Codicefiscale;
	private boolean errore;
	private final char CodiciMese[] = { 'a', 'b', 'c', 'd', 'e', 'h', 'l', 'm',
			'p', 'r', 's', 't' };

	final char caratteri[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	final int codicidispari[] = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18,
			20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23, 1, 0, 5, 7, 9, 13,
			15, 17, 19, 21 };

	final int codicipari[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9 };

	final char CodiciControllo[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };

	private String pulisci(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++)
			switch (s.charAt(i)) {
			case 224:
				s1 = s1 + "a";
				break;

			case 232:
				s1 = s1 + "e";
				break;

			case 233:
				s1 = s1 + "e";
				break;

			case 236:
				s1 = s1 + "i";
				break;

			case 242:
				s1 = s1 + "o";
				break;

			case 249:
				s1 = s1 + "u";
				break;

			default:
				s1 = s1 + s.charAt(i);
				break;

			case 32: // ' '
			case 39: // '\''
				break;
			}

		for (int j = 0; j < s1.length(); j++)
			if ("abcdefghijklmnopqrstuvwxyz".indexOf(s1.charAt(j)) < 0)
				errore = true;

		return s1;
	}

	private String pulisciLuogo(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++)
			switch (s.charAt(i)) {
			case 224:
				s1 = s1 + "a'";
				break;

			case 232:
				s1 = s1 + "e'";
				break;

			case 233:
				s1 = s1 + "e'";
				break;

			case 236:
				s1 = s1 + "i'";
				break;

			case 242:
				s1 = s1 + "o'";
				break;

			case 249:
				s1 = s1 + "u'";
				break;

			default:
				s1 = s1 + s.charAt(i);
				break;
			}

		return s1;
	}

	private boolean IsVocal(char c) {
		return "aeiou".indexOf(c) >= 0;
	}

	private String CodiceCognome(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++)
			if (!IsVocal(s.charAt(i)))
				s1 = s1 + s.charAt(i);

		if (s1.length() < 3) {
			for (int j = 0; j < s.length(); j++)
				if (IsVocal(s.charAt(j)))
					s1 = s1 + s.charAt(j);

			if (s1.length() < 3)
				s1 = s1 + "xxx";
		}
		return s1.substring(0, 3);
	}

	private String CodiceNome(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++)
			if (!IsVocal(s.charAt(i)))
				s1 = s1 + s.charAt(i);

		if (s1.length() > 3)
			return "" + s1.charAt(0) + s1.charAt(2) + s1.charAt(3);
		if (s1.length() == 3)
			return s1;
		for (int j = 0; j < s.length(); j++)
			if (IsVocal(s.charAt(j)))
				s1 = s1 + s.charAt(j);

		if (s1.length() < 3)
			s1 = s1 + "xxx";
		return s1.substring(0, 3);
	}

	private String CodiceData(Calendar calendar) {
		int i = calendar.get(1);
		int j = calendar.get(2);
		int k = calendar.get(5);
		i %= 100;
		String s;
		if (i < 10)
			s = "0" + String.valueOf(i);
		else
			s = "" + String.valueOf(i);
		if (Sesso == 'f' || Sesso == 'F')
			k += 40;
		String s1;
		if (k < 10)
			s1 = "0" + String.valueOf(k);
		else
			s1 = String.valueOf(k);
		if (k < 10)
			return "" + String.valueOf(i) + CodiciMese[j] + "0"
					+ String.valueOf(k);
		else
			return s + CodiciMese[j] + s1;
	}

	private String CodiceLuogo(String s) {
		CodiceFiscaleComuni codici = new CodiceFiscaleComuni();
		String codice = codici.getComune(s);
		if (ValidazioneDati.strIsNull(codice)) codice = "Z999";

		return codice;
	}

	private char CodiceControllo(String s) {
		int i = 0;
		int j = 0;
		for (int k = 0; k <= 14; k += 2) {
			char c = s.charAt(k);
			for (int i1 = 0; i1 < caratteri.length; i1++)
				if (caratteri[i1] == c)
					j += codicidispari[i1];

		}

		for (int l = 1; l <= 13; l += 2) {
			char c1 = s.charAt(l);
			for (int j1 = 0; j1 < caratteri.length; j1++)
				if (caratteri[j1] == c1)
					i += codicipari[j1];

		}

		return CodiciControllo[(i + j) % 26];
	}

	private void CalcolaCodice() throws Exception {
		String s = Cognome;
		String s1 = Nome;
		String s2 = LuogoDiNascita;
		s = s.toLowerCase();
		s = pulisci(s);
		s1 = s1.toLowerCase();
		s1 = pulisci(s1);
		s2 = s2.toLowerCase();
		s2 = pulisciLuogo(s2);
		s2 = s2.toUpperCase();
		if (!errore)
			Codicefiscale = CodiceCognome(s).trim().toUpperCase();
		if (!errore)
			Codicefiscale = Codicefiscale + CodiceNome(s1).trim().toUpperCase();
		if (!errore)
			Codicefiscale = Codicefiscale
					+ CodiceData(DataDiNascita).trim().toUpperCase();
		if (!errore)
			Codicefiscale = Codicefiscale
					+ CodiceLuogo(s2).trim().toUpperCase();
		if (!errore)
			Codicefiscale = Codicefiscale + CodiceControllo(Codicefiscale);
		if (errore)
			throw new Exception();

	}

	public CodiceFiscale(String cognome, String nome, Calendar dataNascita,
			String luogo, char sesso) throws Exception {
		Codicefiscale = "";
		errore = false;
		this.Nome = nome;
		this.Cognome = cognome;
		DataDiNascita = dataNascita;
		LuogoDiNascita = luogo;
		this.Sesso = sesso;
		CalcolaCodice();
	}

	public String getNome() {
		return Nome;
	}

	public String getCognome() {
		return Cognome;
	}

	public Calendar getDataDiNascita() {
		return DataDiNascita;
	}

	public String getLuogoDiNascita() {
		return LuogoDiNascita;
	}

	public String getCodiceFiscale() {
		return Codicefiscale;
	}

	public char getSesso() {
		return Sesso;
	}

}
