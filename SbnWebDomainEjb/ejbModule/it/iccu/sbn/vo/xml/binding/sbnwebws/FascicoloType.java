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
//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine.
// Generato il: 2017.05.17 alle 02:58:57 PM CEST
//


package it.iccu.sbn.vo.xml.binding.sbnwebws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * dati del fascicolo
 *
 * <p>Classe Java per FascicoloType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="FascicoloType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numerazione">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="160"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tipo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dataPubblicazione" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="periodicita" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}PeriodicitaType" minOccurs="0"/>
 *         &lt;element name="volume" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="annata" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="descrizione" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FascicoloType", propOrder = {
    "numerazione",
    "tipo",
    "dataPubblicazione",
    "periodicita",
    "volume",
    "annata",
    "descrizione"
})
public class FascicoloType

    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String numerazione;
    @XmlElement(required = true)
    protected String tipo;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataPubblicazione;
    @XmlSchemaType(name = "string")
    protected PeriodicitaType periodicita;
    protected Short volume;
    protected String annata;
    protected String descrizione;

    /**
     * Recupera il valore della proprietà numerazione.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumerazione() {
        return numerazione;
    }

    /**
     * Imposta il valore della proprietà numerazione.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumerazione(String value) {
        this.numerazione = value;
    }

    /**
     * Recupera il valore della proprietà tipo.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Imposta il valore della proprietà tipo.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Recupera il valore della proprietà dataPubblicazione.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDataPubblicazione() {
        return dataPubblicazione;
    }

    /**
     * Imposta il valore della proprietà dataPubblicazione.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDataPubblicazione(XMLGregorianCalendar value) {
        this.dataPubblicazione = value;
    }

    /**
     * Recupera il valore della proprietà periodicita.
     *
     * @return
     *     possible object is
     *     {@link PeriodicitaType }
     *
     */
    public PeriodicitaType getPeriodicita() {
        return periodicita;
    }

    /**
     * Imposta il valore della proprietà periodicita.
     *
     * @param value
     *     allowed object is
     *     {@link PeriodicitaType }
     *
     */
    public void setPeriodicita(PeriodicitaType value) {
        this.periodicita = value;
    }

    /**
     * Recupera il valore della proprietà volume.
     *
     * @return
     *     possible object is
     *     {@link Short }
     *
     */
    public Short getVolume() {
        return volume;
    }

    /**
     * Imposta il valore della proprietà volume.
     *
     * @param value
     *     allowed object is
     *     {@link Short }
     *
     */
    public void setVolume(Short value) {
        this.volume = value;
    }

    /**
     * Recupera il valore della proprietà annata.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAnnata() {
        return annata;
    }

    /**
     * Imposta il valore della proprietà annata.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAnnata(String value) {
        this.annata = value;
    }

    /**
     * Recupera il valore della proprietà descrizione.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il valore della proprietà descrizione.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

}
