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
package it.iccu.sbn.web.actions.gestionebibliografica.isbd.finsiel;

/**
 * @author Maurizio Alvino
 *
 */
public class Isbd {
	/**	Stringa per a929, Numero d'ordine */
	private String a_929 = null;

	/**	Stringa per b929, Numero d'opera */
	private String b_929 = null;

	/**	Stringa per c929, Numero catalogo tematico */
	private String c_929 = null;

	/**	Stringa per e929, Tonalita */
	private String e_929 = null;

	/**	Stringa per f929, Sezioni */
	private String f_929 = null;

	/**	Stringa per g929, Titolo di ordinamento */
	private String g_929 = null;

	/**	Stringa per h929, Titolo estratto */
	private String h_929 = null;

	/**	Stringa per i929, Appellativo */
	private String i_929 = null;

	/**	Stringa per a928, Forma musicale */
	private String[] a_928 = null;

	/**	Stringa per b928, Organico Sintetico */
	private String b_928 = null;

	/**	Stringa per c928, Organico Analitico */
	private String c_928 = null;

	/**
	 * @return a_928
	 */
	public String[] getA_928() {
		return a_928;
	}

	/**
	 * @return a_929
	 */
	public String getA_929() {
		return a_929;
	}

	/**
	 * @return b_928
	 */
	public String getB_928() {
		return b_928;
	}

	/**
	 * @return b_929
	 */
	public String getB_929() {
		return b_929;
	}

	/**
	 * @return c_928
	 */
	public String getC_928() {
		return c_928;
	}

	/**
	 * @return c_929
	 */
	public String getC_929() {
		return c_929;
	}

	/**
	 * @return e_929
	 */
	public String getE_929() {
		return e_929;
	}

	/**
	 * @return f_929
	 */
	public String getF_929() {
		return f_929;
	}

	/**
	 * @return g_929
	 */
	public String getG_929() {
		return g_929;
	}

	/**
	 * @return h_929
	 */
	public String getH_929() {
		return h_929;
	}

	/**
	 * @return i_929
	 */
	public String getI_929() {
		return i_929;
	}

	/**
	 * Restituisce il valore di un campo se non vuoto,
	 * null altrimenti.
	 *
	 * @param s
	 * @return
	 */
	private String getValueOrNull(String s) {
		return !s.equals("") ? s : null;
	}

	/**
	 * Restituisce il valore di un campo se non vuoto,
	 * null altrimenti.
	 *
	 * @param a
	 * @return
	 */
	private String[] getValueOrNull(String[] a) {
		String[] ris = new String[3];
		for (int i=0; i<3; i++)
			ris[i] = !a[i].equals("") ? a[i] : null;

		return ris;
	}

	/**
	 * Costruttore di Isbd.
	 *
	 * @param a_929 Numero d'ordine
	 * @param b_929 Numero d'opera
	 * @param c_929 Numero di catalogo tematico
	 * @param e_929 Tonalita
	 * @param f_929 Sezioni
	 * @param g_929 Ordinamento
	 * @param h_929 Estratto
	 * @param i_929 Appellativo
	 * @param a_928 Forma musicale
	 * @param b_928 Organico sintetico
	 * @param c_928 Organico analitico
	 */
	public Isbd(
		String a_929,
		String b_929,
		String c_929,
		String e_929,
		String f_929,
		String g_929,
		String h_929,
		String i_929,
		String[] a_928,
		String b_928,
		String c_928
	) {
		this.a_929 = getValueOrNull(a_929);
		this.b_929 = getValueOrNull(b_929);
		this.c_929 = getValueOrNull(c_929);
		this.e_929 = getValueOrNull(e_929);
		this.f_929 = getValueOrNull(f_929);
		this.g_929 = getValueOrNull(g_929);
		this.h_929 = getValueOrNull(h_929);
		this.i_929 = getValueOrNull(i_929);
		this.a_928 = getValueOrNull(a_928);
		this.b_928 = getValueOrNull(b_928);
		this.c_928 = getValueOrNull(c_928);
	}
}
