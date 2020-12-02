package it.finsiel.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import com.stevenrbrandt.ubiq2.v1.pattwo.*; // regular expression
//package digilib.misc;

//import java.util.*;
//import com.Ostermiller.util.StringTokenizer;
//import org.springframework.util.StringUtils;

/*******************************************************************************
 * 
 * OPERATIONS ON STRINGS =====================
 * 
 ******************************************************************************/

public class MiscString {
//	private static final String sep_generico = "\n"; // "���";
	public static final boolean KEEP_DELIMITERS_FALSE = false;
	public static final boolean KEEP_DELIMITERS_TRUE = true;
	public static final boolean KEEP_GROUP_DELIMITERS_FALSE = false;
	public static final boolean KEEP_GROUP_DELIMITERS_TRUE = true;
	public static final boolean TRIM_FALSE = false;
	public static final boolean TRIM_TRUE = true;
	public static final boolean HAS_ESCAPED_CHARACTERS_FALSE = false;
	public static final boolean HAS_ESCAPED_CHARACTERS_TRUE = true;
	public static final boolean KEEP_ESCAPE_TRUE = true;
	public static final boolean KEEP_ESCAPE_FALSE = false;


	
	public static final boolean PADDING_LEFT = true;
	public static final boolean PADDING_RIGHT = false;
	
	
	static char charSepArray[] = {'\n'}; 		// ' ', '\t', 
	static String stringSepArray[] = { "&$%"}; // , "###"
	
	
	
	public MiscString() {
		super();
	}

	/**
	 * Converte un eventuale float da testo ad integer.
	 * 
	 * @return int
	 * @num_testo java.lang.String
	 */
	public static int textToInteger(String num_testo) {
		String appo = "";
		int ris = 1;
		if (num_testo.indexOf(".") != -1)
			appo = num_testo.substring(0, num_testo.indexOf("."));
		else
			appo = num_testo;

		try {
			ris = Integer.parseInt(appo);
		} catch (NumberFormatException e) {
			ris = 0;
		}

		return ris;
	} // End textToInteger

	/**
	 * Taglia i blank all'inizio e alla fine della stringa.
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 */
	public static String trim(String str) {
		if (str == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.trim: ERR001");
			return " ";
		}
		if (str.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.trim: ERR002");
			return " ";
		}
		if (str.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.trim: ERR003");
			return " ";
		}
		return str.trim();
	} // End trim

	/**
	 * Taglia i blank all'inizio e alla fine della stringa.
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 */
	public static String trimRight(String str) {
		if (str == null)
			return null;
		if (str.equals(""))
			return "";

		int len = str.length();
		int pos = 0;
		for (pos = len - 1; pos >= 0; pos--)
		{
			if (str.charAt(pos) != ' ' && str.charAt(pos) != '\t')
				break;
		}
		if (pos >= -1)
			return str.substring(0, pos+1);
		else
			return str;
	} // End trim

	// Regular Expression trim right
	public static String trimRightRE(String str) {
		return str.replaceAll("\\s+$", "");
	} // End trim
	
	/**
	 * Converte una stringa formato float nell'intero corrispondente.
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 */
	public static String floatToInt(String str) {
		if (str == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.floatToInt: ERR001");
			return "";
		}
		if (str.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.floatToInt: ERR002");
			return "";
		}
		if (str.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.floatToInt: ERR003");
			return "";
		}
		if (str.indexOf(".") == -1)
			return str;
		str = str.substring(0, str.indexOf("."));
		return str;
	}// End floatToInt

	/**
	 * Trasforma un array di stringhe in un'unica stringa.
	 * 
	 * @return java.lang.String
	 * @param campi
	 *            java.lang.String[]
	 */
	public static String inserisciArray(String[] campi) {
		String input = "";
		String appo = "";
		try {
			for (int i = 0; i < campi.length; i++) {
				appo = campi[i].trim();
				if (appo.equals("")) {
					appo = " ";
				}
				input = input.concat(appo + charSepArray[0]); // sep_generico
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return input;
	}// End inserisciArray

	/**
	 * Trasforma un array di stringhe in un'unica stringa.
	 * 
	 * @return java.lang.String
	 * @param campi
	 *            java.lang.String[]
	 */
	public static String inserisciArray(String[] campi, String sep) {
		String input = "";
		String appo = "";
		try {
			for (int i = 0; i < campi.length; i++) {
				appo = campi[i].trim();
				if (appo.equals("")) {
					appo = " ";
				}
				input = input.concat(appo + sep);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return input;
	}// End inserisciArray

	/**
	 * Restituisce l'indice di una determinata occorrenza in una stringa. A
	 * partire dall'indice <i>indice</i> cerca in <i>stringa</i> la sequenza
	 * <i>str</i>, restituendo l'indice della prima occorrenza.
	 * 
	 * @return int
	 * @param indice
	 *            int
	 * @param stringa
	 *            java.lang.String
	 * @param str
	 *            java.lang.String
	 */
	public static int inStr(int indice, String stringa, String str) {
		if (str == null || stringa == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.inStr: ERR001");
			return 0;
		}
		if (str.equals("") || stringa.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.inStr: ERR002");
			return 0;
		}
		if (str.equalsIgnoreCase("null") || stringa.equalsIgnoreCase("null")
				|| indice < 0) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.inStr: ERR003");
			return 0;
		}
		return stringa.indexOf(str, indice - 1) + 1;
	}// End inStr

	/**
	 * Restituisce la parte sinistra della stringa specificata
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 * @param num
	 *            int
	 */
	public static String left(String str, int num) {

		if (str == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.left: ERR001");
			return " ";
		}
		if (str.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.left: ERR002");
			return " ";
		}
		if (str.equalsIgnoreCase("null") || num < 1 || num > str.length()) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.left: ERR003");
			return " ";
		}

		try {
			return str.substring(0, num);
		} catch (Exception e) {
			System.out.println("misc.left: ERR003");
			return " ";
		}
	} // End left

	/**
	 * Restituisce la lunghezza della stringa specificata.
	 * 
	 * @return int
	 * @param str
	 *            java.lang.String
	 */
	public static int len(String str) {
		if (str == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.len: ERR001");
			return 0;
		}
		if (str.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.len: ERR002");
			return 0;
		}
		if (str.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.len: ERR003");
			return 0;
		}
		return str.length();
	} // End len

	/**
	 * Restituisce la sottostringa di <i>str</i> a partire dall'indice
	 * <i>inizio</i> e per la lunghezza specificata da <i>lung</i>
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 * @param inizio
	 *            int
	 * @param lung
	 *            int
	 */
	public static String mid(String str, int inizio, int lung) {

		if (str == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.mid: ERR001");
			return " ";
		}
		if (str.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.mid: ERR002");
			return " ";
		}
		if (str.equalsIgnoreCase("null") || inizio < 1 || lung < 0
				|| inizio > str.length() || lung > str.length() - inizio + 1) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.mid: ERR003");
			return " ";
		}

		try {
			return str.substring(inizio - 1, inizio + lung - 1);
		} catch (Exception e) {
			System.out.println("misc.mid: ERR003");
			return " ";
		}
	} // End mid


	/**
	 * Restituisce la parte destra della stringa specificata
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 * @param num
	 *            int
	 */
	public static String right(String str, int num) {

		if (str == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.right: ERR001");
			return " ";
		}
		if (str.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.right: ERR002");
			return " ";
		}
		if (str.equalsIgnoreCase("null") || num < 0) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.right: ERR003");
			return " ";
		}
		/*
		 * // Vecchia versione: a partire dall'indice num verso destra try {
		 * return str.substring(num+1,str.length()); } catch (Exception e) {
		 * System.out.println("[" + getDate() + " " + getTime() + "] " +
		 * "Funzioni.right: ERR003"); return " "; }
		 */
		// Nuova versione: a partire da destra num caratteri
		try {
			return str.substring(str.length() - num, str.length());
		} catch (Exception e) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.right: ERR003");
			return " ";
		}
	} // End right

	/**
	 * Trasforma una stringa in un array di stringhe.
	 * 
	 * @return java.lang.String[]
	 * @param str
	 *            java.lang.String
	 * @param delim
	 *            java.lang.String
	 */
	/*
	 * public static String[] split (String str, String delim) {
	 * 
	 * String[] appoReturn = new String[1]; appoReturn[0] = " "; if (str == null ||
	 * delim == null) { //System.out.println("[" + getDate() + " " + getTime() + "] " +
	 * "Funzioni.split: ERR001"); return appoReturn; } if (str.equals("") ||
	 * delim.equals("")) { //System.out.println("[" + getDate() + " " +
	 * getTime() + "] " + "Funzioni.split: ERR002"); return appoReturn; } if
	 * (str.equalsIgnoreCase("null") || delim.equalsIgnoreCase("null")) {
	 * //System.out.println("[" + getDate() + " " + getTime() + "] " +
	 * "Funzioni.split: ERR003"); return appoReturn; }
	 * 
	 * StringTokenizer mioStrTok = new StringTokenizer(str, delim, "", true); //
	 * java.util.Vector vecCampi = new java.util.Vector(); for (int i = 0;
	 * mioStrTok.hasMoreTokens(); i++) { String token = mioStrTok.nextToken();
	 * vecCampi.addElement(token); } vecCampi.trimToSize(); String[] arrCampi =
	 * new String[vecCampi.size()]; for (int i = 0; i < vecCampi.size(); i++) {
	 * arrCampi[i] = (String)vecCampi.elementAt(i); } return arrCampi; } // End
	 * split
	 */

	// Comma Separated Values
	private void csvStringStack(StringBuffer sb, String command, String value) {
		if (command.equals("ADD_ELEMENT")) {
			if (sb.length() > 0) {
				sb.append(',');
				sb.append(value);
			}
		}
		if (command.equals("REMOVE_LAST_ELEMENT")) {
			int i = sb.lastIndexOf(",");
			if (i > 0)
				sb.setLength(i);
			else
				sb.setLength(0);
			return;
		}
	} // End csv

	// User Separator Values String Stack
	private void vsvStringStack(StringBuffer sb, String command, String value,
			String separator) {
		if (command.equals("ADD_ELEMENT")) {
			if (sb.length() > 0) {
				sb.append(separator);
				sb.append(value);
			}
		}
		if (command.equals("REMOVE_LAST_ELEMENT")) {
			int i = sb.lastIndexOf(separator);
			if (i > 0)
				sb.setLength(i);
			else
				sb.setLength(0);
			return;
		}
	} // End csv

	/**
	 * Trasforma una stringa in un array di stringhe.
	 * 
	 * @return java.lang.String[]
	 * @param output
	 *            java.lang.String
	 */

	public static String[] estraiCampi(String output) {
		String[] appoReturn = new String[1];
		appoReturn[0] = " ";
		if (output == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (output.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (output.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}
//		StringTokenizer mioStrTok = new StringTokenizer(output, charSepArray[0], "", true); // return empty tokens
        MiscStringTokenizer mioStrTok = new MiscStringTokenizer(output, charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);

		java.util.Vector vecCampi = new java.util.Vector();
//		for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
//			vecCampi.addElement(mioStrTok.nextToken()); // .trim()
//		}
		vecCampi = mioStrTok.getTokenVect();
		
		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampi




	/**
	 * * pad a string S with a size of N with char C * on the left (True) or on
	 * the right(flase)
	 */
	public static String paddingString(String s, int n, char c,	boolean paddingLeft) {
		StringBuffer str = new StringBuffer(s);
		int strLength = str.length();
		if (n > 0 && n > strLength) {
			for (int i = 0; i <= n; i++) {
				if (paddingLeft) {
					if (i < n - strLength)
						str.insert(0, c);
				} else {
					if (i > strLength)
						str.append(c);
				}
			}
		}
		return str.toString();
	}

	public static boolean isEmpty(String s) {
		int strLength = s.length();
		char c = ' ';
		for (int i = 0; i < strLength; i++) {
			c = s.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n' && c != '\r')
				return false;
		}
		return true;
	}

	/**
	 * Replacement utility - substitutes <b>all</b> occurrences of 'src' with
	 * 'dest' in the string 'name'
	 * 
	 * @param name
	 *            the string that the substitution is going to take place on
	 * @param src
	 *            the string that is going to be replaced
	 * @param dest
	 *            the string that is going to be substituted in
	 * @return String with the substituted strings
	 */
	public static String substitute(String name, String src, String dest) {
		if (name == null || src == null || name.length() == 0) {
			return name;
		}

		if (dest == null) {
			dest = "";
		}

		int index = name.indexOf(src);
		if (index == -1) {
			return name;
		}

		StringBuffer buf = new StringBuffer();
		int lastIndex = 0;
		while (index != -1) {
			buf.append(name.substring(lastIndex, index));
			buf.append(dest);
			lastIndex = index + src.length();
			index = name.indexOf(src, lastIndex);
		}
		buf.append(name.substring(lastIndex));
		return buf.toString();
	}

	/**
	 * Trasforma una stringa in un array di stringhe.
	 * 
	 * @return java.lang.String[]
	 * @param output
	 *            java.lang.String
	 */

	public static String[] estraiCampi(String output, char[] aCharDelimitersAr, boolean returnEpmtyTokens) {
		String[] appoReturn = new String[1];
		appoReturn[0] = " ";
		if (output == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (output.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (output.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}
		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(output, aCharDelimitersAr, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, returnEpmtyTokens);
		Vector vecCampi = mioStrTok.getTokenVect();
		
		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampi

	public static String[] estraiCampi(String output, byte[] aByteDelimitersAr, boolean returnEpmtyTokens) {
		String[] appoReturn = new String[1];
		appoReturn[0] = " ";
		if (output == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (output.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (output.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}
		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(output, aByteDelimitersAr, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, returnEpmtyTokens);
		Vector vecCampi = mioStrTok.getTokenVect();
		
		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampi
	
	
	
	
	/**
	 * Trasforma una stringa in un array di stringhe.
	 */

	public static String[] estraiCampiConEscapePerSeparatore(String input,
			char[] aCharDelimitersAr, //String sep,
			char escapeSequence) { // sep is a comma separated list
												// of single characters
		String[] appoReturn = new String[1];
		StringBuffer sb = new StringBuffer();
		appoReturn[0] = " ";
		if (input == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (input.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (input.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}
		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(input, aCharDelimitersAr, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE); 
		java.util.Vector vecCampi = new java.util.Vector();
		sb.setLength(0); // clear
		for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
			String s = mioStrTok.nextToken();
			if (s.length() > 0 && (s.charAt(s.length() - 1) == escapeSequence)) {
				sb.append(s);
				sb.replace(sb.length() - 1, sb.length(), ""+aCharDelimitersAr[0]); // sep
				continue;
			}

			// if (sb.length() > 0)
			sb.append(s);

			// vecCampi.addElement(s);
			vecCampi.addElement(sb.toString());
			sb.setLength(0); // clear
		}

		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampiConEscapePerSeparatore


	public static String[] estraiCampiConEscapePerSeparatore(String input,
			String[] aStringDelimitersAr, //String sep,
			char escapeSequence) { // sep is a comma separated list
												// of single characters
		String[] appoReturn = new String[1];
		StringBuffer sb = new StringBuffer();
		appoReturn[0] = " ";
		if (input == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (input.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (input.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}
		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(input, aStringDelimitersAr, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE); 
		java.util.Vector vecCampi = new java.util.Vector();
		sb.setLength(0); // clear
		for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
			String s = mioStrTok.nextToken();
			if (s.length() > 0 && (s.charAt(s.length() - 1) == escapeSequence)) {
				sb.append(s);
				sb.replace(sb.length() - 1, sb.length(), aStringDelimitersAr[0]); // sep
				continue;
			}

			// if (sb.length() > 0)
			sb.append(s);

			// vecCampi.addElement(s);
			vecCampi.addElement(sb.toString());
			sb.setLength(0); // clear
		}

		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampiConEscapePerSeparatore
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Trasforma una stringa in un array di stringhe.
	 * 
	 * @return java.lang.String[]
	 * @param output
	 *            java.lang.String
	 */
	public static String[] estraiCampiAncheDelimitati(String output,
			char sep, // sep is a comma separated list of single characters
			char delimStart,// delimStart eg: ' o " o #
			char delimEnd // delimEnd eg: ' o " o $
	
	)
	{
		char charDelimitersStartEndAr[] = new char[3];
		charDelimitersStartEndAr[0] = sep;
		charDelimitersStartEndAr[1] = delimStart;
		charDelimitersStartEndAr[2] = delimEnd;
		
		
		String[] appoReturn = new String[1];
		appoReturn[0] = " ";
		boolean inDelimetedField = false;
//		StringBuffer sb = new StringBuffer(sep + aCharDelimitersStartAr[0] + aCharDelimitersEndAr);
		StringBuffer ds = new StringBuffer(); // delimeted string
		String token;

		if (output == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (output.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (output.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}

//		StringTokenizer mioStrTok = new StringTokenizer(output, "", sb.toString(), true); // return empty tokens
		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(output, charDelimitersStartEndAr, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE); // return empty tokens

		java.util.Vector vecCampi = new java.util.Vector();
		while (mioStrTok.hasMoreTokens()) {
			// e' un delimitatore?
			token = mioStrTok.nextToken();
			if (token.length() == 0)
				continue;

			if (inDelimetedField == true) {
//				if (token.length() == 1 && token.equals(delimEnd)) {
				if (token.length() == 1 && token.charAt(0) == delimEnd) { // 09/12/2010
					inDelimetedField = false;
					vecCampi.addElement(ds.toString());
					ds.setLength(0);
					continue;
				}
				ds.append(token);
				continue;
			}
//			if (token.length() == 1 && token.equals(delimStart)) {
			if (token.length() == 1 && token.charAt(0) == delimStart) { // 09/12/2010
				inDelimetedField = true;
				continue;
			}
			// e' un separatore?
			if (token.length() == 1) {
//				if (sep.indexOf(token.charAt(0)) != -1)
				if (token.charAt(0) == sep)
					continue; // skip
			}

			vecCampi.addElement(token);
		}

		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampiAncheDelimitati
	


	
	
	/**
	 * Questo metodo cerca, all'interno di <i>str</i>, la stringa <i>elem</i>
	 * e la sostituisce con la stringa <i>sost</i>.
	 * 
	 * @return java.lang.String
	 * @param str
	 *            java.lang.String
	 * @param elem
	 *            java.lang.String
	 * @param sost
	 *            java.lang.String
	 *            
	 */
	public static String replace(String str, String elem, String sost) {
		
		if (str == null || elem == null || sost == null) {
			return " ";
		}
		
		if (str.equals("") || elem.equals("")) { 
			return " ";
		}
		
		if (str.equalsIgnoreCase("null") || elem.equals("null")	|| sost.equals("null")) {
			return " ";
		}
		
		int i = 0;
		while (i < str.length()) 
		{

			if (str.substring(i).startsWith(elem)) {
				str = str.substring(0, i) + sost
						+ str.substring(i + elem.length());
				i += sost.length() - 1;
			}
			i++;
			
		} // end while
		return str;

	} // End replace
	
	
	public static String regexpReplace(String inputStr, String searchStr, String replaceStr) 
		{
        // Compile regular expression
        Pattern pattern = Pattern.compile(searchStr);
        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(inputStr);
//        int groups = matcher.groupCount();
        
        
        String output = matcher.replaceAll(replaceStr);

        
        return output;			
		}
	


	/**
	 * Trasforma una stringa in un array di stringhe usando delimitatori.
	 * Ignora i deilimtatori se all'interno di delimitatori di gruppo
	 * Opzionalmente mantieni come token anche i delimitatori o delimitatori di gruppo
	 * 
	 * eg: estraiCampiDelimitatiENon(" 1, 2, 3 (4, 5), 6", ", ", '(', ')', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_FALSE)
	 * @return java.lang.StringBuffer[]
	 * @param output
	 *            java.lang.String
	 */

	public static String[] estraiCampiDelimitatiENon(String input,
			String delimiters,
			char groupDelimiterStart,// delimStart eg: ' o " o # o (
			char groupDelimiterEnd, // delimEnd eg: ' o " o $ o )
			boolean keepDelimiters,
			boolean keepGroupDelimiters,
			boolean trim,
			boolean hasEscapedCharacter, // eg " la \"citta'\" e' vuota"
			boolean keepEscapeCharacter
			)
	{
		boolean returnDelimitersAsTokens = MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE;
		char charDelimitersStartEndAr[] = new char[delimiters.length()+2];
		for (int i=0; i < delimiters.length(); i++)
			charDelimitersStartEndAr[i] = delimiters.charAt(i);
		charDelimitersStartEndAr[delimiters.length()] = groupDelimiterStart;
		charDelimitersStartEndAr[delimiters.length()+1] = groupDelimiterEnd;

//		StringBuffer[] appoReturn = new StringBuffer[1];
		// appoReturn[0] = " ";
		boolean inDelimetedField = false;
		StringBuffer sb = new StringBuffer();
		StringBuffer delimString = new StringBuffer(); // delimeted string
		String token;

		if (input == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return null;
		}
		if (input.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return null;
		}
		if (input.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return null;
		}

//		StringTokenizer mioStrTok = new StringTokenizer(input, "", sb.toString(), true); // return empty tokens
		MiscStringTokenizer mioStrTok;
		if (hasEscapedCharacter == true)
			mioStrTok = new MiscStringTokenizer(input, charDelimitersStartEndAr, returnDelimitersAsTokens, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE, HAS_ESCAPED_CHARACTERS_TRUE, keepEscapeCharacter); // return empty tokens
		else
			mioStrTok = new MiscStringTokenizer(input, charDelimitersStartEndAr, returnDelimitersAsTokens, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // return empty tokens

		java.util.Vector vecCampi = new java.util.Vector();
		while (mioStrTok.hasMoreTokens()) {
			// e' un delimitatore?
			token = mioStrTok.nextToken();
			if (token.length() == 0 || (inDelimetedField == false && token.length() == 1 && delimiters.indexOf(token) >= 0 ))
			{
				String s;
				if (trim == true)
					s = sb.toString().trim();
				else
					s = sb.toString();
					
				if (s.length() > 0)					
					vecCampi.addElement(new String(s));

				sb.setLength(0);
				continue;
			}

			if (inDelimetedField == true) {
				if (token.length() == 1 && token.charAt(0) == groupDelimiterEnd) {
					inDelimetedField = false;
					if (keepGroupDelimiters == true)
						delimString.append(token);
					//vecCampi.addElement(new StringBuffer(delimString.toString())); // testo delimitato
					sb.append(delimString);
					
					delimString.setLength(0);
					continue;
				}
				delimString.append(token); // testo all'interno del limitarore
				continue;
			}
			
			if (token.length() == 1 && token.charAt(0) == groupDelimiterStart) {
				inDelimetedField = true;
				if (keepGroupDelimiters == true)
					delimString.append(token);
				continue;
			}
			
			// e' un delimitatore?
			if (token.length() == 1 && delimiters.indexOf(token) >= 0 )
			{
				String s;
				if (trim == true)
					s = sb.toString().trim();
				else
					s = sb.toString();
					
				if (s.length() > 0)					
					vecCampi.addElement(new String(s));

				sb.setLength(0);
				continue;
			}
			
			//vecCampi.addElement(new StringBuffer(token));
			sb.append(token);
		} // End while

		if (sb.length() > 0)
		{
//			if (trim == true)
//				vecCampi.addElement(new String(sb.toString()).trim());
//			else
//				vecCampi.addElement(new String(sb.toString()));
				String s;
				if (trim == true)
					s = sb.toString().trim();
				else
					s = sb.toString();
					
				if (s.length() > 0)					
					vecCampi.addElement(new String(s));

			sb.setLength(0);
		}
		
		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampiDelimitatiENon



	/**
	 * Trasforma una stringa in un array di stringhe.
	 * 
	 * @return java.lang.String[]
	 * @param stringIn
	 *            java.lang.String
	 */

	public static String[] estraiCampi(String stringIn, String[] aStringDelimitersAr, boolean returnDelimiter) { 
		String[] appoReturn = new String[1];
		appoReturn[0] = " ";
		if (stringIn == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (stringIn.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (stringIn.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}
//		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(output, aStringDelimitersAr, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);
		MiscStringTokenizer mioStrTok = new MiscStringTokenizer(stringIn, aStringDelimitersAr, returnDelimiter, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);

		Vector vecCampi = mioStrTok.getTokenVect();
		vecCampi.trimToSize();
		String[] arrCampi = new String[vecCampi.size()];
		for (int i = 0; i < vecCampi.size(); i++) {
			arrCampi[i] = (String) vecCampi.elementAt(i);
		}
		return arrCampi;
	} // End estraiCampi
	

public static int countPattern(Pattern p, String content) // String regex = "[aeiou]";
{
	int counter = 0;
	Matcher m = p.matcher(content);         // Create Matcher
	while (m.find()) {
		counter++;
	}
	//System.out.println("Total vowels: " + vowelcount);
	return counter;

}



public static String[] estraiCampi(byte [] byteBufferIn, byte[] aByteDelimitersAr, boolean returnEpmtyTokens) {
	String[] appoReturn = new String[1];
	appoReturn[0] = " ";
	if (byteBufferIn == null) {
		// System.out.println("[" + getDate() + " " + getTime() + "] " +
		// "Funzioni.estraiCampi: ERR001");
		return appoReturn;
	}
//	if (byteBufferIn.equals("")) {
//		// System.out.println("[" + getDate() + " " + getTime() + "] " +
//		// "Funzioni.estraiCampi: ERR002");
//		return appoReturn;
//	}
//	if (byteBufferIn.equalsIgnoreCase("null")) {
//		// System.out.println("[" + getDate() + " " + getTime() + "] " +
//		// "Funzioni.estraiCampi: ERR003");
//		return appoReturn;
//	}
	if (byteBufferIn.length == 0)
		return appoReturn;
		
	
	
	MiscStringTokenizer mioStrTok = new MiscStringTokenizer(byteBufferIn, aByteDelimitersAr, 
			MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, returnEpmtyTokens);
	Vector vecCampi = mioStrTok.getTokenVect();
	
	vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String) vecCampi.elementAt(i);
	}
	return arrCampi;
} // End estraiCampi


public static List<byte[]> split_byte_array(byte[] arr, byte byteSepIn)
{
    List<byte[]> result = new ArrayList<byte[]>();
    int start = 0;
    for (int i = 0; i < arr.length; i++)
    {
        if (arr[i] == byteSepIn && i!=0)
        {
            byte[] _in = new byte[i - start];
            System.arraycopy(arr, start, _in, 0, i - start);
            
            result.add(_in);
            start = i+1;
        }
        else if (arr[i] == byteSepIn && i == 0)
        {
            byte[] _in = new byte[]{}; // Empty array
            result.add(_in);
            start = i + 1;
        }
        else if (arr.length - 1 == i && i != start)
        {
            byte[] _in = new byte[i - start+1];
            System.arraycopy(arr, start, _in, 0, i - start+1);
            result.add(_in);
        }
    }
    return result;
}


public static void dump_byte_arrayList( List<byte[]> al)
{
//	for (byte[] bar : al)
	System.out.println ("------");
	for (int i=0; i < al.size(); i++)
	{
		String s = new String(al.get(i));
		System.out.println (i+" "+s);
	}
}


} // End MiscString

