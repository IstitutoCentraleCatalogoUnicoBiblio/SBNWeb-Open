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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * dati della collocazione
 *
 * <p>Classe Java per CollocazioneType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="CollocazioneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documento" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}DocumentoType" minOccurs="0"/>
 *         &lt;element name="inventario" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}InventarioType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bib" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="sez" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="10"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="loc" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="24"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="spec">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="12"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="consis">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="255"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollocazioneType", propOrder = {
    "documento",
    "inventario"
})
public class CollocazioneType

    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected DocumentoType documento;
    protected List<InventarioType> inventario;
    @XmlAttribute(name = "bib", required = true)
    protected String bib;
    @XmlAttribute(name = "sez", required = true)
    protected String sez;
    @XmlAttribute(name = "loc", required = true)
    protected String loc;
    @XmlAttribute(name = "spec")
    protected String spec;
    @XmlAttribute(name = "consis")
    protected String consis;

    /**
     * Recupera il valore della proprietà documento.
     *
     * @return
     *     possible object is
     *     {@link DocumentoType }
     *
     */
    public DocumentoType getDocumento() {
        return documento;
    }

    /**
     * Imposta il valore della proprietà documento.
     *
     * @param value
     *     allowed object is
     *     {@link DocumentoType }
     *
     */
    public void setDocumento(DocumentoType value) {
        this.documento = value;
    }

    /**
     * Gets the value of the inventario property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inventario property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInventario().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InventarioType }
     *
     *
     */
    public List<InventarioType> getInventario() {
        if (inventario == null) {
            inventario = new ArrayList<InventarioType>();
        }
        return this.inventario;
    }

    /**
     * Recupera il valore della proprietà bib.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBib() {
        return bib;
    }

    /**
     * Imposta il valore della proprietà bib.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBib(String value) {
        this.bib = value;
    }

    /**
     * Recupera il valore della proprietà sez.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSez() {
        return sez;
    }

    /**
     * Imposta il valore della proprietà sez.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSez(String value) {
        this.sez = value;
    }

    /**
     * Recupera il valore della proprietà loc.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLoc() {
        return loc;
    }

    /**
     * Imposta il valore della proprietà loc.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLoc(String value) {
        this.loc = value;
    }

    /**
     * Recupera il valore della proprietà spec.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpec() {
        return spec;
    }

    /**
     * Imposta il valore della proprietà spec.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpec(String value) {
        this.spec = value;
    }

    /**
     * Recupera il valore della proprietà consis.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConsis() {
        return consis;
    }

    /**
     * Imposta il valore della proprietà consis.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConsis(String value) {
        this.consis = value;
    }

}
