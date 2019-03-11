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
package it.iccu.sbn.util.file;

import java.io.Closeable;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import org.jumpmind.symmetric.csv.CsvWriter;

public class CsvWriter2 extends CsvWriter implements Flushable, Closeable {

	public CsvWriter2(Writer w, char sep) {
		super(w, sep);
	}

	private CsvWriter2(OutputStream out, char sep, Charset charset) {
		super(out, sep, charset);
	}

	private CsvWriter2(String file, char sep, Charset charset) {
		super(file, sep, charset);
	}

	private CsvWriter2(String file) {
		super(file);
	}

}
