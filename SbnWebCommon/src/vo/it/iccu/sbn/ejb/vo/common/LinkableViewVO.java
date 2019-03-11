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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LinkableViewVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -4977638760246053569L;
	private static final String XID_REGEX = "([A-Z]{1}\\w{2}[E|M|V|C|D|L|P|0-9]\\d{6})";
	private static final Pattern patternXid = Pattern.compile(XID_REGEX, Pattern.CASE_INSENSITIVE);
	private static final String HTML_NEW_LINE_REGEX = "(&lt;br\\s{0,1}?&gt;)|(&lt;br\\s*?/&gt;)";
	private static final Pattern patternNewLine = Pattern.compile(HTML_NEW_LINE_REGEX, Pattern.CASE_INSENSITIVE);
	public static final String HTML_NEW_LINE = "<br />";

	protected static final String filter(String value) {
		int len = length(value);
		if (len < 1)
			return value;

		StringBuilder result = null;
		String filtered = null;
		for (int i = 0; i < len; i++) {
			filtered = null;
			switch (value.charAt(i)) {
			case 60: // '<'
				filtered = "&lt;";
				break;

			case 62: // '>'
				filtered = "&gt;";
				break;

			case 38: // '&'
				filtered = "&amp;";
				break;

			case 34: // '"'
				filtered = "&quot;";
				break;

			case 39: // '\''
				filtered = "&#39;";
				break;
			}

			if (result == null) {
				if (filtered != null) {
					result = new StringBuilder(len + 50);
					if (i > 0)
						result.append(value.substring(0, i));
					result.append(filtered);
				}
			} else if (filtered == null)
				result.append(value.charAt(i));
			else
				result.append(filtered);
		}

		return result != null ? result.toString() : value;
	}

	protected static final String findXid(String value) {
		Matcher m = patternXid.matcher(value);
		return m.replaceAll("key{$1}");
	}

	protected static final String htmlFilter(String value) {

		String tmp = filter(value);
		if (tmp != null)
			tmp = patternNewLine.matcher(tmp).replaceAll(HTML_NEW_LINE);
		return tmp;
	}

	public LinkableViewVO() {
		super();
	}

}
