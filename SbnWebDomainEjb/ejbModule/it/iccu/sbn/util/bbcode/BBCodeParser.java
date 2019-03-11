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
package it.iccu.sbn.util.bbcode;

import java.io.InputStream;

import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;

public class BBCodeParser {

	static class DummyTextProcessor implements TextProcessor {

		public CharSequence process(CharSequence source) {
			return source;
		}

		public String process(String source) {
			return source;
		}

		public StringBuilder process(StringBuilder source) {
			return source;
		}

		public StringBuffer process(StringBuffer source) {
			return source;
		}

	}

	private static TextProcessor dummy = new DummyTextProcessor();
	private static TextProcessor instance;

	static {
		InputStream in = BBCodeParser.class.getResourceAsStream("/META-INF/bb-parser-conf.xml");
		instance = BBProcessorFactory.getInstance().create(in);
	}

	public static TextProcessor getParser(boolean styled) {
		return styled ? instance : dummy;
	}

}
