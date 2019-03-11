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
package it.iccu.sbn.web2.tags.util;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.linkedin.urls.detection.Match;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;

public class UrlMatcher {

	private enum Type {
		URL,
		MAIL;
	}

	// Pattern for recognizing a URL, based off RFC 3986
	private static String URL_REGEX = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
	private static Pattern MARK_LINK_PATTERN = Pattern.compile(URL_REGEX,	Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static String MAIL_REGEX ="^[A-Za-z0-9+_.-]+@(.+)$";
	private static Pattern MARK_MAIL_PATTERN = Pattern.compile(MAIL_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private String styleId;


	private static final List<String> VALID_SCHEMES = Arrays.asList(
			"http://", "https://", "ftp://", "ftps://", "http%3a//", "https%3a//", "ftp%3a//", "ftps%3a//", "mailto://");


	public UrlMatcher(String styleId) {
		this.styleId = styleId;
/*
		URL_REGEX = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
		MARK_LINK_PATTERN = Pattern.compile(URL_REGEX,	Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		MAIL_REGEX ="^[A-Za-z0-9+_.-]+@(.+)$";
		MARK_MAIL_PATTERN = Pattern.compile(MAIL_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
*/
	}

	String render(final String value) {
		if (!ValidazioneDati.isFilled(value))
			return value;

		String input = value.replaceAll("\\s+|\\t+|\\p{Cntrl}+", " ");
		StringBuilder output = new StringBuilder();
		for (String token : input.split("\\s")) {
			if (MARK_LINK_PATTERN.matcher(token).matches())
				output.append(renderUrl(styleId, token, Type.URL)).append(' ');
			else
				if (MARK_MAIL_PATTERN.matcher(token).matches())
					output.append(renderUrl(styleId, token, Type.MAIL)).append(' ');
				else {
					UrlDetector ud = new UrlDetector(token, UrlDetectorOptions.Default);
					List<Match> matches = ud.match();
					if (ValidazioneDati.isFilled(matches))
						output.append(renderUrl(styleId, matches.get(0).getUrl(), Type.URL)).append(' ');
					else
						output.append(token).append(' ');
				}



		}

		return output.toString().trim();
	}

	private String addSchema(String url, Type type) {
		url = StringUtils.stripEnd(url, ".");
		String tmp = url.toLowerCase();
		for (String schema : VALID_SCHEMES)
			if (tmp.startsWith(schema))
				return url;

		return (type == Type.URL ? "http://" : "mailto://") + url;
	}

	private String renderUrl(String styleId, String url, Type type) {

		StringBuilder out = new StringBuilder();
		out.append("<a target=\"_blank\" ");
		if (styleId != null)
			out.append("class=\"" + styleId + "\" ");
		out.append("href=\"" + addSchema(url, type) + "\">" + url + "</a>");
		return out.toString();
	}


	public String match(String input) {
		return render(input);
	}


	public static void main(String[] args) {
		UrlMatcher um = new UrlMatcher(null);
		//String test = "consultare www.iccu.sbn.it per altre info. Oppure contattare mario.rossi-iccu@beniculturali.it.";
		//String test = "Vedi repubblica.it oppure www.corriere.it. In alternativa contatta mario.rossi_iccu@iccu.beniculturali.it.";
		String test = "http://193.206.221.14/verificaDispo2.php?BIB=%20IC&INV=%20%20%20%20000050155&TIT=La%20*montagna&AUT=&ANNO=1912-1915&NAT=S\n mario.rossi@almaviva.it";
		System.out.println(um.match(test));

	}

}
