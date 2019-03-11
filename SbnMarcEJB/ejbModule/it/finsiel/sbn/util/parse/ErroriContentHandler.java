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
package it.finsiel.sbn.util.parse;

import it.finsiel.sbn.polo.exception.util.Errori;

import org.apache.log4j.Category;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Classe ErroriContentHandler.java
 * <p>
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer
 *
 * @version 17-set-2002
 */
public class ErroriContentHandler implements ContentHandler {
    static Category log =
        Category.getInstance("iccu.box.xml.ErroriContentHandler");

    /**
     * Gli attributi di questa classe sono costituiti da costanti
     */

    //public static final String  REFERENTE_DEFAULT= "referente_default";
    public static final String NOME_REF_DEF = "nome_ref_def";
    public static final String MAIL_REF_DEF = "mail_ref_def";
    public static final String ERRORE = "Errore";
    public static final String ID = "id";
    public static final String DESCRIZIONE = "descrizione";
    //public static final String  REFERENTE = "referente";
    public static final String NOME = "nome";
    public static final String MAIL = "mail";

    /**
     * Valore dell'elemento corrente
     */
    private String currentElementValue = null;

    private String nome_ref_def;

    private String mail_ref_def;

    private Integer currentErrorID;

    private String currentErrorDesc;

    private String currentNomeRef;

    private String currentMailRef;

    private Errori error;

    public ErroriContentHandler(Errori error)
    {
    	this.error = error;
    }


    /**
     * <p>
     * Questo metodo controlla se l'elemento in analisi (localName) è un attributo
     * della classe SBNMARCContentHandler.
     * In tal caso viene riempita una AttributeQueue, che è un'estensione di una
     * Map,
     * con il nome dell'elemento ed il suo valore. Poi questa hashtable viene passata
     * ad un vettore
     * attributeVector. Se l'elemento è riconosciuto come factoring il nome del
     * factoring e
     * l'AttributeQueue vengono passate al wrapper SBNMARC2Obj.Se l'elemento non è
     * infine riconosciuto
     * come attributo della classe SBNMARCContentHandler viene assegnato come
     * attributo a quello
     * gerarchicamente superiore con un _.
     * </p>
     * @param String namespaceURI
     * @param String localName: nome elemento che viene analizzato
     * @param String rawName
     * @param Attributes atts attributi elemento xml
     * @throws org.xml.sax.SAXException
     */
    public void startElement(
        String namespaceURI,
        String localName,
        String rawName,
        Attributes atts)
        throws SAXException {

        try {

            currentElementValue = "";
            if (localName.equals(ERRORE)) {
            	currentErrorID = new Integer(atts.getValue(ID));
                currentNomeRef = nome_ref_def;
                currentMailRef = mail_ref_def;
            }

        } catch (Exception e) {
            log.fatal("errz: " + e);
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Metodo che indica che la fine di un elemento è stata raggiunta:
     * il parser non distingue tra elementi vuoti o non vuoti.
     * Se l'elemento di chiusura (localName) corrisponde ad uno degli attributi della
     * classe SBNMARCContentHandler,
     * il suo nome ed il suo valore (già immagazzinati nell'AttributeQueue) vengono
     * passati al wrapper SBNMARC2Obj.
     * Questo passaggio avviene tramite opportuni metodi di setObject per ogni
     * elemento definito come oggetto
     * e tramite opportuni metodi di executeFactoring per ogni elemento riconosciuto
     * come factoring.
     * E' a questo punto che sono stati raccolti tutti i dati da passare
     * all'elaborazione.
     * </p>
     * @param String namespaceURI
     * @param String localName: nome elemento che viene analizzato
     * @param String rawName
     * @param namespaceURI
     * @param localName
     * @param rawName
     * @throws org.xml.sax.SAXException
     */
    public void endElement(
        String namespaceURI,
        String localName,
        String rawName)
        throws SAXException {
        try {
            if (localName.equals(NOME_REF_DEF))
                nome_ref_def = currentElementValue;
            else if (localName.equals(MAIL_REF_DEF))
                mail_ref_def = currentElementValue;
            else if (localName.equals(DESCRIZIONE))
                currentErrorDesc = currentElementValue;
            else if (localName.equals(NOME))
                currentNomeRef = currentElementValue;
            else if (localName.equals(MAIL))
                currentMailRef = currentElementValue;
            else if (localName.equals(ERRORE))
            	this.error.settaErrore(
                    currentErrorID,
                    currentErrorDesc,
                    currentNomeRef,
                    currentMailRef);
            return;

        } catch (Exception e) {
            log.fatal("ERRORE- endElement" + e);
            e.printStackTrace();
        }
    }

    /**
     * Metodo che riporta i dati carattere in un elemento.
     *
     * @param ch
     * @param start
     * @param length
     * @throws org.xml.sax.SAXException
     * @roseuid 3D64BF2A0017
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        String data = new String(ch, start, length).trim();
        currentElementValue += data;
    }

	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(String)
	 */
	public void endPrefixMapping(String arg0) throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
		throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#processingInstruction(String, String)
	 */
	public void processingInstruction(String arg0, String arg1)
		throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(Locator)
	 */
	public void setDocumentLocator(Locator arg0) {
	}

	/**
	 * @see org.xml.sax.ContentHandler#skippedEntity(String)
	 */
	public void skippedEntity(String arg0) throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(String, String)
	 */
	public void startPrefixMapping(String arg0, String arg1)
		throws SAXException {
	}

}
