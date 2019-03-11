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
//Source file: C:\\JAVADEV\\SBN_Evoluzione_Indice\\src\\iccu\\beans\\entity\\tavole\\SQLStatementParser.java

package it.finsiel.sbn.util.parse;

import it.finsiel.sbn.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <P>
 * Classe che legge ed analizza un file XML utilizzando SAX. </font> </div>
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer 21/08/2002
 */
public class PoloXMLParser {
	public int counter = 0;
	private static Logger log = Logger.getLogger("iccu.box.xml.IndiceParser");
	private XMLReader parser;

	/**
	 * public SAXParser saxparser; SAX PARSER DEMO - Costruttore della classe
	 * <p>
	 * Vengono definite tutte le proprietà del parser che si vogliono utilizzare
	 * e in quale directory reperire lo schema xml rispetto il quale si deve
	 * validare il file xml di input.
	 * </p>
	 *
	 * @param nessuno
	 *            @ return istanza della classe
	 * @roseuid 3D64BF2F0122
	 */
	public PoloXMLParser(InputStream schemaSrc, ContentHandler contentHandler,
			ErrorHandler errorHandler) {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			SAXParser saxparser = factory.newSAXParser();

			parser = saxparser.getXMLReader();

			// Registra l'Handler di errori
			parser.setErrorHandler(errorHandler);
			// registra l'handler di contenuto
			parser.setContentHandler(contentHandler);

			String validation = "http://xml.org/sax/features/validation";
			String validation_schema = "http://apache.org/xml/features/validation/schema";
			String namespaces = "http://xml.org/sax/features/namespaces";
			parser.setFeature(validation, true);
			parser.setFeature(validation_schema, true);
			// Disattiva il supporto di namespace
			parser.setFeature(namespaces, true);

			String xsd_path = System.getProperty("java.io.tmpdir") + File.separator + "sbnmarc_errors.xsd";
			FileUtil.uploadFile(schemaSrc, xsd_path, null);
			parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", xsd_path);

		} catch (SAXException e) {
			log.error("Errore di analisi: ", e);

		} catch (ParserConfigurationException e) {
			log.error("Errore di analisi: ", e);

		} catch (Exception e) {
			log.error("Errore di analisi: ", e);
		}
	}

	/**
	 * <p>
	 * Metodo che analizza file Xml, utilizzando handler SAX registrati che
	 * consentono l'analisi degli eventi all'interno di un documento, senza
	 * dovere prima essere caricato. In questo caso viene passato al metodo
	 * l'url del file xml da validare.
	 * </p>
	 *
	 * @param String
	 *            (Uri del file da analizzare)
	 * @param uri
	 * @return Boolean esito operazione di parse
	 * @roseuid 3D64BF2F012C
	 */
	public boolean parse(String uri) {
		boolean esito = true;
		log.debug("Analisi del file XML: " + uri);

		try {

			// Analizza il documento
			parser.parse(new InputSource(uri));

		} catch (IOException e) {
			log.error("Errore di lettura Uri(" + uri + "): ", e);

			esito = false;
		} catch (SAXException e) {
			log.error("Errore di analisi(" + uri + "): ", e);

			esito = false;
		} catch (Exception e) {
			log.error("Errore in fase di parsing(" + uri + "): ", e);

			esito = false;
		}

		return esito;
	}

	/**
	 * <p>
	 * Metodo che analizza file Xml, utilizzando handler SAX registrati che
	 * consentono l'analisi degli eventi all'interno di un documento, senza
	 * dovere prima essere caricato. In questo caso viene passato al metodo
	 * l'url del file xml da validare.
	 * </p>
	 *
	 * @param inputSource
	 *            il sorgente da analizzare
	 * @return Boolean esito operazione di parse
	 * @roseuid 3D64BF2F012C
	 */
	public boolean parse(InputSource inputSource) {
		boolean esito = true;
		log.debug("Analisi del file XML: " + inputSource.getSystemId());

		try {

			// Analizza il documento
			parser.parse(inputSource);

		} catch (IOException e) {
			log.error("Errore di lettura Uri(" + inputSource.getSystemId()
					+ "): ", e);

			esito = false;
		} catch (SAXException e) {
			log.error("Errore di analisi(" + inputSource.getSystemId() + "): ", e);

			esito = false;
		} catch (Exception e) {
			log.error("Errore in fase di parsing(" + inputSource.getSystemId()
					+ "): ", e);

			esito = false;
		}

		return esito;
	}

}
