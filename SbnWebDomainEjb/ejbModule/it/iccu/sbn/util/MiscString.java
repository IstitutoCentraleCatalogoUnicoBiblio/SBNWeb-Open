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
//package digilib.misc;
package it.iccu.sbn.util;

import java.util.ArrayList;

import com.Ostermiller.util.StringTokenizer;

//import java.util.*;
//import com.Ostermiller.util.StringTokenizer;
//import org.springframework.util.StringUtils;

/***********************************************************************************************
*
*	OPERATIONS ON STRINGS
*	=====================
*
***********************************************************************************************/

public class MiscString {
	private static final String sep_generico = "\n"; //"£°ç";
    public MiscString() {
        super();
        }


 /**
 * Converte un eventuale float da testo ad integer.
 * @return int
 * @num_testo java.lang.String
 */
public static int textToInteger(String num_testo) {
	String appo = "";
	int ris = 1;
	if (num_testo.indexOf(".") != -1)
		appo = num_testo.substring(0,num_testo.indexOf("."));
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
 * @return java.lang.String
 * @param str java.lang.String
 */
public static String trim(String str)
    {
	if (str == null)
        {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.trim: ERR001");
		return " ";
		}
	if (str.equals(""))
        {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.trim: ERR002");
		return " ";
		}
	if (str.equalsIgnoreCase("null"))
        {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.trim: ERR003");
		return " ";
		}
	return str.trim();
    } // End trim





/**
 * Converte una stringa formato float nell'intero corrispondente.
 * @return java.lang.String
 * @param str java.lang.String
 */
public static String floatToInt(String str) {
	if (str == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.floatToInt: ERR001");
		return "";
	}
	if (str.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.floatToInt: ERR002");
		return "";
	}
	if (str.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.floatToInt: ERR003");
		return "";
	}
	if (str.indexOf(".") == -1) return str;
	str = str.substring(0,str.indexOf("."));
	return str;
}// End floatToInt


/**
 * Trasforma un array di stringhe in un'unica stringa.
 * @return java.lang.String
 * @param campi java.lang.String[]
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
			input = input.concat(appo + sep_generico);
		}
	} catch (NullPointerException e) {
		e.printStackTrace();
	}

	return input;
}// End inserisciArray



/**
 * Trasforma un array di stringhe in un'unica stringa.
 * @return java.lang.String
 * @param campi java.lang.String[]
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
 * Restituisce l'indice di una determinata occorrenza in una stringa.
 * A partire dall'indice <i>indice</i> cerca in <i>stringa</i> la sequenza <i>str</i>,
 * restituendo l'indice della prima occorrenza.
 * @return int
 * @param indice int
 * @param stringa java.lang.String
 * @param str java.lang.String
 */
public static int inStr(int indice, String stringa, String str) {
	if (str == null || stringa == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.inStr: ERR001");
		return 0;
	}
	if (str.equals("") || stringa.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.inStr: ERR002");
		return 0;
	}
	if (str.equalsIgnoreCase("null") || stringa.equalsIgnoreCase("null") || indice < 0) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.inStr: ERR003");
		return 0;
	}
	return stringa.indexOf(str,indice - 1) + 1;
}// End inStr


/**
 * Restituisce la parte sinistra della stringa specificata
 * @return java.lang.String
 * @param str java.lang.String
 * @param num int
 */
public static String left(String str, int num) {

	if (str == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.left: ERR001");
		return " ";
	}
	if (str.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.left: ERR002");
		return " ";
	}
	if (str.equalsIgnoreCase("null") || num < 1 || num > str.length()) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.left: ERR003");
		return " ";
	}

	try {
		return str.substring(0,num);
	} catch (Exception e) {
		System.out.println("misc.left: ERR003");
		return " ";
	}
} // End left


/**
 * Restituisce la lunghezza della stringa specificata.
 * @return int
 * @param str java.lang.String
 */
public static int len(String str) {
	if (str == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.len: ERR001");
		return 0;
	}
	if (str.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.len: ERR002");
		return 0;
	}
	if (str.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.len: ERR003");
		return 0;
	}
	return str.length();
} // End len





/**
 * Restituisce la sottostringa di <i>str</i> a partire dall'indice <i>inizio</i> e per la lunghezza specificata da <i>lung</i>
 * @return java.lang.String
 * @param str java.lang.String
 * @param inizio int
 * @param lung int
 */
public static String mid(String str, int inizio, int lung) {

	if (str == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.mid: ERR001");
		return " ";
	}
	if (str.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.mid: ERR002");
		return " ";
	}
	if (str.equalsIgnoreCase("null") || inizio < 1 || lung < 0 || inizio > str.length() || lung > str.length() - inizio + 1) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.mid: ERR003");
		return " ";
	}

	try {
		return str.substring(inizio - 1,inizio + lung - 1);
	} catch (Exception e) {
		System.out.println("misc.mid: ERR003");
		return " ";
	}
} // End mid


/**
 * Questo metodo cerca, all'interno di <i>str</i>, la stringa
 * <i>elem</i> e la sostituisce con la stringa <i>sost</i>.
 * @return java.lang.String
 * @param str java.lang.String
 * @param elem java.lang.String
 * @param sost java.lang.String
 */
public static String replace(String str, String elem, String sost) {
  	if (str == null || elem == null || sost == null) {
		return " ";
	}
	if (str.equals("") || elem.equals("")) { //  || sost.equals("")
		return " ";
	}
	if (str.equalsIgnoreCase("null") || elem.equals("null") || sost.equals("null")) {
		return " ";
	}
 	int i = 0;
	while (i < str.length()) {
	  	if (str.substring(i).startsWith(elem)) {
			str = str.substring(0,i) + sost + str.substring(i+elem.length());
	 		i+= sost.length() - 1;
		}
		i++;
  	}
  	return str;

} // End replace

/**
 * Restituisce la parte destra della stringa specificata
 * @return java.lang.String
 * @param str java.lang.String
 * @param num int
 */
public static String right(String str, int num) {

	if (str == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.right: ERR001");
		return " ";
	}
	if (str.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.right: ERR002");
		return " ";
	}
	if (str.equalsIgnoreCase("null") || num < 0) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.right: ERR003");
		return " ";
	}
	/*
	// Vecchia versione: a partire dall'indice num verso destra
	try {
		return str.substring(num+1,str.length());
	} catch (Exception e) {
		System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.right: ERR003");
		return " ";
	} */
	// Nuova versione: a partire da destra num caratteri
	try {
		return str.substring(str.length() - num,str.length());
	} catch (Exception e) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.right: ERR003");
		return " ";
	}
} // End right


/**
 * Trasforma una stringa in un array di stringhe.
 * @return java.lang.String[]
 * @param str java.lang.String
 * @param delim java.lang.String
 */
/*
public static String[] split (String str, String delim) {

	String[] appoReturn = new String[1];
	appoReturn[0] = " ";
	if (str == null || delim == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.split: ERR001");
		return appoReturn;
	}
	if (str.equals("") || delim.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.split: ERR002");
		return appoReturn;
	}
	if (str.equalsIgnoreCase("null") || delim.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.split: ERR003");
		return appoReturn;
	}

	StringTokenizer mioStrTok = new StringTokenizer(str, delim, "", true); //
	java.util.List vecCampi = new java.util.List();
	for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
		String token = mioStrTok.nextToken();
		vecCampi.addElement(token);
	}
	vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String)vecCampi.elementAt(i);
	}
	return arrCampi;
} // End split
*/




// Comma Separated Values
private void csvStringStack(StringBuffer sb, String command, String value)
{
if (command.equals("ADD_ELEMENT"))
	{
	if (sb.length() > 0)
		{
		sb.append(',');
		sb.append(value);
		}
	}
if (command.equals("REMOVE_LAST_ELEMENT"))
	{
	int i = sb.lastIndexOf(",");
	if (i > 0)
		sb.setLength(i);
	else
		sb.setLength(0);
		return;
	}
} // End csv

// User Separator Values String Stack
private void vsvStringStack(StringBuffer sb, String command, String value, String separator)
{
if (command.equals("ADD_ELEMENT"))
	{
	if (sb.length() > 0)
		{
		sb.append(separator);
		sb.append(value);
		}
	}
if (command.equals("REMOVE_LAST_ELEMENT"))
	{
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
 * @return java.lang.String[]
 * @param output java.lang.String
 */

public static String[] estraiCampi (String output) {
	String[] appoReturn = new String[1];
	appoReturn[0] = " ";
	if (output == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR001");
		return appoReturn;
	}
	if (output.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR002");
		return appoReturn;
	}
	if (output.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR003");
		return appoReturn;
	}
	StringTokenizer mioStrTok = new StringTokenizer(output, sep_generico, "", true); // return empty tokens
	java.util.List vecCampi = new ArrayList();
	for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
		vecCampi.add(mioStrTok.nextToken()); // .trim()
	}
	//vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String)vecCampi.get(i);
	}
	return arrCampi;
} // End estraiCampi



/**
 * Trasforma una stringa in un array di stringhe.
 * @return java.lang.String[]
 * @param output java.lang.String
 */

public static String[] estraiCampi (String output, String sep) { // sep is a comma separated list of single characters
	String[] appoReturn = new String[1];
	appoReturn[0] = " ";
	if (output == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR001");
		return appoReturn;
	}
	if (output.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR002");
		return appoReturn;
	}
	if (output.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR003");
		return appoReturn;
	}
	StringTokenizer mioStrTok = new StringTokenizer(output, sep, "", true); // return empty tokens
	java.util.List vecCampi = new ArrayList();
	for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
		String s = mioStrTok.nextToken();
		if (s.length() == 0)
			break;
		vecCampi.add(s); // .trim()
	}
	//vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String)vecCampi.get(i);
	}
	return arrCampi;
} // End estraiCampi




/**
 * Trasforma una stringa in un array di stringhe.
 * @return java.lang.String[]
 * @param output java.lang.String
 */

public static String[] estraiCampiAncheDelimitati (	String output,
													String sep, 	// sep is a comma separated list of single characters
													String delimStart,// delimStart eg: ' o " o #
													String delimEnd)	// delimEnd   eg: ' o " o $
	{
	String[] appoReturn = new String[1];
	appoReturn[0] = " ";
	boolean inDelimetedField = false;
	StringBuffer sb = new StringBuffer(sep + delimStart + delimEnd);
	StringBuffer ds = new StringBuffer(); // delimeted string
	String token;


	if (output == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR001");
		return appoReturn;
	}
	if (output.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR002");
		return appoReturn;
	}
	if (output.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR003");
		return appoReturn;
	}

	StringTokenizer mioStrTok = new StringTokenizer(output, "", sb.toString(), true); // return empty tokens
	java.util.List vecCampi = new ArrayList();
	while (mioStrTok.hasMoreTokens())
		{
		// e' un delimitatore?
		token = mioStrTok.nextToken();
		if (token.length() == 0)
			continue;

		if (inDelimetedField == true)
		{
			if (token.length() == 1 && token.equals(delimEnd))
			{
				inDelimetedField = false;
				vecCampi.add(ds.toString());
				ds.setLength(0);
				continue;
			}
			ds.append(token);
			continue;
		}
		if (token.length() == 1 && token.equals(delimStart))
		{
			inDelimetedField = true;
			continue;
		}
		// e' un separatore?
		if (token.length() == 1)
		{
			if (sep.indexOf(token.charAt(0)) != -1)
				continue; // skip
		}

		vecCampi.add(token);
		}

	//vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String)vecCampi.get(i);
	}
	return arrCampi;
} // End estraiCampiAncheDelimitati

/**
 * Trasforma una stringa in un array di stringhe.
 * eg: "<nome>almaviva7</><cognome>almaviva7</>" e delimitatori "<" ">" mi ritorna
 * 	["<nome>", "Argen... tino", "</>", "<cognome>", "almaviva7", "</>"]
 *
 * @return java.lang.StringBuffer[]
 * @param output java.lang.String
 */

public static StringBuffer[] estraiCampiDelimitatiENon (	String input,
													String delimStart,// delimStart eg: ' o " o #
													String delimEnd)	// delimEnd   eg: ' o " o $
	{
	StringBuffer[] appoReturn = new StringBuffer[1];
	//appoReturn[0] = " ";
	boolean inDelimetedField = false;
	StringBuffer sb = new StringBuffer(delimStart + delimEnd);
	StringBuffer delimString = new StringBuffer(); // delimeted string
	String token;


	if (input == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR001");
		return appoReturn;
	}
	if (input.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR002");
		return appoReturn;
	}
	if (input.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR003");
		return appoReturn;
	}

	StringTokenizer mioStrTok = new StringTokenizer(input, "", sb.toString(), true); // return empty tokens
	java.util.List vecCampi = new ArrayList();
	while (mioStrTok.hasMoreTokens())
		{
		// e' un delimitatore?
		token = mioStrTok.nextToken();
		if (token.length() == 0)
			continue;

		if (inDelimetedField == true)
		{
			if (token.length() == 1 && token.equals(delimEnd))
			{
				inDelimetedField = false;
				delimString.append (token);
				vecCampi.add(new StringBuffer(delimString.toString())); // testo delimitato
				delimString.setLength(0);
				continue;
			}
			delimString.append(new StringBuffer(token)); // testo all'interno del limitarore
			continue;
		}
		if (token.length() == 1 && token.equals(delimStart))
		{
			inDelimetedField = true;
			delimString.append (token);
			continue;
		}
		// testo non delimitato
		vecCampi.add(new StringBuffer(token));
		}

	//vecCampi.trimToSize();
	StringBuffer[] arrCampi = new StringBuffer[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (StringBuffer)vecCampi.get(i);
	}
	return arrCampi;
} // End estraiCampiDelimitatiENon





/**
 * Trasforma una stringa in un array di stringhe.
 * @return java.lang.String[]
 * @param output java.lang.String
 * @param output java.lang.String
 * @param output char
 */

public static String[] estraiCampiConEscapePerSeparatore (String input, String sep, char escapeSequence) { // sep is a comma separated list of single characters
	String[] appoReturn = new String[1];
	StringBuffer sb = new StringBuffer ();
	appoReturn[0] = " ";
	if (input == null) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR001");
		return appoReturn;
	}
	if (input.equals("")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR002");
		return appoReturn;
	}
	if (input.equalsIgnoreCase("null")) {
		//System.out.println("[" + getDate() + " " + getTime() + "] " + "Funzioni.estraiCampi: ERR003");
		return appoReturn;
	}
	StringTokenizer mioStrTok = new StringTokenizer(input, sep, "", true); // return empty tokens
	java.util.List vecCampi = new ArrayList();
	sb.setLength(0); // clear
	for (int i = 0; mioStrTok.hasMoreTokens(); i++) {
		String s = mioStrTok.nextToken();
		if (s.length() > 0 && (s.charAt(s.length()-1) == escapeSequence))
		{
			sb.append(s);
			sb.replace(sb.length()-1, sb.length(), sep);
			continue;
		}

//		if (sb.length() > 0)
			sb.append(s);

//		vecCampi.addElement(s);
		vecCampi.add(sb.toString());
		sb.setLength(0); // clear
	}



	//vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (int i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String)vecCampi.get(i);
	}
	return arrCampi;
} // End estraiCampi





/**
 ** pad a string S with a size of N with char C
 ** on the left (True) or on the right(flase)
 **/
 public static String paddingString( String s, int n, char c , boolean paddingLeft  ) {
   StringBuffer str = new StringBuffer(s);
   int strLength  = str.length();
   if ( n > 0 && n > strLength ) {
     for ( int i = 0; i <= n ; i ++ ) {
           if ( paddingLeft ) {
             if ( i < n - strLength ) str.insert( 0, c );
           }
           else {
             if ( i > strLength ) str.append( c );
           }
     }
   }
   return str.toString();
 }

 public static boolean isEmpty( String s) {
   int strLength  = s.length();
   char c = ' ';
   for (int i=0; i < strLength; i++)
   {
	   c = s.charAt(i);
	   if (c != ' ' && c != '\t' && c != '\n' && c != '\r')
		   return false;
   }
   return true;
 }

 /**
  * Replacement utility - substitutes <b>all</b> occurrences of 'src' with 'dest' in the string 'name'
  * @param name the string that the substitution is going to take place on
  * @param src the string that is going to be replaced
  * @param dest the string that is going to be substituted in
  * @return String with the substituted strings
  */
 public static String substitute( String name, String src, String dest ) {
   if( name == null || src == null || name.length() == 0 ) {
     return name;
   }

   if( dest == null ) {
     dest = "";
   }

   int index = name.indexOf( src );
   if( index == -1 ) {
     return name;
   }

   StringBuffer buf = new StringBuffer();
   int lastIndex = 0;
   while( index != -1 ) {
     buf.append( name.substring( lastIndex, index ) );
     buf.append( dest );
     lastIndex = index + src.length();
     index = name.indexOf( src, lastIndex );
   }
   buf.append( name.substring( lastIndex ) );
   return buf.toString();
 }


} // End MiscString

