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
package it.iccu.sbn.SbnMarcFactory.util;

/**
 * Utilità sulle stringhe
 *
 * @author Corrado Di Pietro
 */
public class UtilityStringhe {
    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param what DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String removeStart(String str, String what) {
        if (str.startsWith(what)) {
            int x = what.length();

            return str.substring(x);
        } else {
            return str;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String cutAfter(String str, char c) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                //   System.out.println(str.substring(i+1));
                return str.substring(i + 1);
            }
        }

        return str;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tmp DOCUMENT ME!
     * @param str DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String contains(String[] tmp, String[] str) {
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < str.length; j++) {
                if (tmp[i].startsWith(str[j])) {
                    return tmp[i];
                }
            }
        }

        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param tmp DOCUMENT ME!
     * @param str DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean strstr(String tmp, char str) {
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == str) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tmp DOCUMENT ME!
     * @param where DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String cut(String tmp, String where) {
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < tmp.length(); i++) {
            if (!string(tmp.charAt(i)).equals(where)) {
                ret.append(string(tmp.charAt(i)));
            }

            if (string(tmp.charAt(i)).equals(where)) {
                return ret.toString();
            }
        }

        return ret.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String string(char c) {
        char[] buf = new char[1];
        buf[0] = c;

        return new String(buf);
    }
	/**
	 *
	 * @param s oggetto
	 * @return la stringa vuota se l'oggetto è null
	 * altrimenti l'oggetto
	 */
	public static String nullToEmpty(Object s){
		return (s==null)?"":s.toString();
	}

}
