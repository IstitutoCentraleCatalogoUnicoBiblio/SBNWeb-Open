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
package it.iccu.sbn.ejb.utils.stampe;

import gnu.trove.THashSet;

import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.log4j.Logger;

public class StampeUtil {

	@SuppressWarnings("serial")
	private static final Map<String, String> FONT_MAPPING = new HashMap<String, String>() {/**
		 * 
		 */
		private static final long serialVersionUID = -7492390099319895337L;

	{
		put("Arial", "SansSerif");
		put("Times New Roman", "Serif");
		put("Courier New", "Monospaced");
	}};

	private static Logger log = Logger.getLogger(StampeUtil.class);

	private static final Set<String> JDK_AVAILABLE_FONTS;

	static {
		JDK_AVAILABLE_FONTS = new THashSet<String>(
				Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		for (String font : JDK_AVAILABLE_FONTS)
			log.debug(String.format("mapped font: '%s'.", font));
	}

	public static int JASPER_A4_W = (int) (8.27 * 72);
	public static int JASPER_A4_H = (int) (11.7 * 72);


	public static String ttf2pdfFontName(String ttfFontName, boolean bold,
			boolean italic) {
		boolean oblique = false;
		String pdfFontName = null;

		//----------------------------------------------------------------------
		// -------
		// PDF font names
		// Courier, Courier-Bold, Courier-BoldOblique, Courier-Oblique,

		if ("Courier New".equals(ttfFontName)) {
			pdfFontName = "Courier";

			oblique = italic;
		}

		//----------------------------------------------------------------------
		// -------
		// PDF font names
		// Times-Roman, Times-Bold, Times-BoldItalic, Times-Italic,

		else if ("Times New Roman".equals(ttfFontName)) {
			pdfFontName = "Times";
		}

		//----------------------------------------------------------------------
		// -------
		// PDF font names
		// Helvetica, Helvetica Bold, Helvetica BoldOblique, Helvetica Oblique,

		else if ("Arial".equals(ttfFontName)) {
			pdfFontName = "Helvetica";

			oblique = italic;
		}

		if (bold || italic) {
			pdfFontName += "-";

			if (bold) {
				pdfFontName += "Bold";
			}

			if (oblique) {
				pdfFontName += "Oblique";
			} else if (italic) {
				pdfFontName += "Italic";
			}

		}
		return pdfFontName;

	}

	public static JasperReport updateFontsAndCompile(JasperDesign jasperDesign,
			String fontName, int fontSize) throws JRException

	{
		// nello stile di default definito nel template...
		JRStyle jrStyle = jasperDesign.getDefaultStyle();

		JRStyle grassetto = jasperDesign.getStylesMap().get(
				"grassetto");

		// ...modifico i valori con quelli presenti nelle variabili
		jrStyle.setFontSize(fontSize);
		grassetto.setFontSize(fontSize);

		jrStyle.setFontName(fontName);
		grassetto.setFontName(fontName);

		// ... aggiorno il tipo di font per l'esportazione in formato pdf
		jrStyle.setPdfFontName(ttf2pdfFontName(fontName, false, false));
		grassetto.setPdfFontName(ttf2pdfFontName(fontName, true, false));

		// ... reimposto lo stile modificato come default
		jasperDesign.setDefaultStyle(jrStyle);

		//almaviva5_20101022 #3915
		jasperDesign.setProperty(
				JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT,
				Boolean.TRUE.toString());

		// compilo

		JasperReport jasperReport = JasperCompileManager
				.compileReport(jasperDesign);

		return jasperReport;
	}

	public static String concatena(String[] varString) {
		String risultato = "";

		if (varString != null)
			for (int i = 0; i < varString.length; i++) {
				risultato += varString[i];
			}

		return risultato;
	}

	public static String concatena(float[] varFloat) {
		String risultato = "";

		if (varFloat != null)
			for (int i = 0; i < varFloat.length; i++) {
				risultato += new Float(varFloat[i]).toString();
			}

		return risultato;
	}

	public static int converti(float ingresso, float conversione) {
		// float conversione=72f/2.54f;
		return new Float(ingresso * conversione).intValue();
	}

	public static float fattoreDiConversione(String unitaDiMisura) {
		if ("cm".equals(unitaDiMisura)) {
			return 72f / 2.54f;
		}

		if ("mm".equals(unitaDiMisura)) {
			return 72f / 25.4f;
		}

		if ("inch".equals(unitaDiMisura)) {
			return 72f;
		}

		if ("punti".equals(unitaDiMisura)) {
			return 1f;
		}
		return 72f / 2.54f;
	}

	public static final String getBatchFilesPath() {
		try {
			return CommonConfiguration.getProperty(Configuration.SBNWEB_BATCH_FILES_PATH, ".");
		} catch (Exception e) {
			return ".";
		}
	}

	public static void copy(File src, File dest) throws IOException {
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);

			byte[] buffer = new byte[8 * 1024];

			while (true) {
				int bytesRead = in.read(buffer);
				if (bytesRead <= 0) {
					break;
				}
				out.write(buffer, 0, bytesRead);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// failsafe
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// failsafe
				}
			}
		}
	}

	public static String mapFont(String fontFamily) {
		String font = fontFamily;
		if (!JDK_AVAILABLE_FONTS.contains(fontFamily)) {
			font = FONT_MAPPING.get(fontFamily);
			log.warn(String.format("font non disponibile per questa JVM; eseguo mapping: '%s' --> '%s'", fontFamily, font));
}
		return font;
	}

}
