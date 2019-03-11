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
 * dati dell'inventario
 *
 * <p>Classe Java per InventarioType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="InventarioType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documento" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}DocumentoType" minOccurs="0"/>
 *         &lt;element name="collocazione" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}CollocazioneType" minOccurs="0"/>
 *         &lt;element name="fascicolo" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}FascicoloType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="possessore" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}PossessoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="disponibilita" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}DisponibilitaType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bib" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="serie" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="3"/>
 *             &lt;whiteSpace value="preserve"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="numero" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *             &lt;maxInclusive value="999999999"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="precis">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="160"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="anno">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;maxInclusive value="9999"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="codFrui">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="codNoDisp">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="numeroFascicoli" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="cdCons">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="2"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="cons" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="seq" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InventarioType", propOrder = {
    "documento",
    "collocazione",
    "fascicolo",
    "possessore",
    "disponibilita"
})
public class InventarioType

    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected DocumentoType documento;
    protected CollocazioneType collocazione;
    protected List<FascicoloType> fascicolo;
    protected List<PossessoreType> possessore;
    protected DisponibilitaType disponibilita;
    @XmlAttribute(name = "bib", required = true)
    protected String bib;
    @XmlAttribute(name = "serie", required = true)
    protected String serie;
    @XmlAttribute(name = "numero", required = true)
    protected long numero;
    @XmlAttribute(name = "precis")
    protected String precis;
    @XmlAttribute(name = "anno")
    protected Integer anno;
    @XmlAttribute(name = "codFrui")
    protected String codFrui;
    @XmlAttribute(name = "codNoDisp")
    protected String codNoDisp;
    @XmlAttribute(name = "numeroFascicoli")
    protected Integer numeroFascicoli;
    @XmlAttribute(name = "cdCons")
    protected String cdCons;
    @XmlAttribute(name = "cons")
    protected String cons;
    @XmlAttribute(name = "seq")
    protected String seq;
    @XmlAttribute(name = "url")
    protected String url;

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
     * Recupera il valore della proprietà collocazione.
     *
     * @return
     *     possible object is
     *     {@link CollocazioneType }
     *
     */
    public CollocazioneType getCollocazione() {
        return collocazione;
    }

    /**
     * Imposta il valore della proprietà collocazione.
     *
     * @param value
     *     allowed object is
     *     {@link CollocazioneType }
     *
     */
    public void setCollocazione(CollocazioneType value) {
        this.collocazione = value;
    }

    /**
     * Gets the value of the fascicolo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fascicolo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFascicolo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FascicoloType }
     *
     *
     */
    public List<FascicoloType> getFascicolo() {
        if (fascicolo == null) {
            fascicolo = new ArrayList<FascicoloType>();
        }
        return this.fascicolo;
    }

    /**
     * Gets the value of the possessore property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the possessore property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPossessore().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PossessoreType }
     *
     *
     */
    public List<PossessoreType> getPossessore() {
        if (possessore == null) {
            possessore = new ArrayList<PossessoreType>();
        }
        return this.possessore;
    }

    /**
     * Recupera il valore della proprietà disponibilita.
     *
     * @return
     *     possible object is
     *     {@link DisponibilitaType }
     *
     */
    public DisponibilitaType getDisponibilita() {
        return disponibilita;
    }

    /**
     * Imposta il valore della proprietà disponibilita.
     *
     * @param value
     *     allowed object is
     *     {@link DisponibilitaType }
     *
     */
    public void setDisponibilita(DisponibilitaType value) {
        this.disponibilita = value;
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
     * Recupera il valore della proprietà serie.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Imposta il valore della proprietà serie.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSerie(String value) {
        this.serie = value;
    }

    /**
     * Recupera il valore della proprietà numero.
     *
     */
    public long getNumero() {
        return numero;
    }

    /**
     * Imposta il valore della proprietà numero.
     *
     */
    public void setNumero(long value) {
        this.numero = value;
    }

    /**
     * Recupera il valore della proprietà precis.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPrecis() {
        return precis;
    }

    /**
     * Imposta il valore della proprietà precis.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPrecis(String value) {
        this.precis = value;
    }

    /**
     * Recupera il valore della proprietà anno.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * Imposta il valore della proprietà anno.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setAnno(Integer value) {
        this.anno = value;
    }

    /**
     * Recupera il valore della proprietà codFrui.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCodFrui() {
        return codFrui;
    }

    /**
     * Imposta il valore della proprietà codFrui.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCodFrui(String value) {
        this.codFrui = value;
    }

    /**
     * Recupera il valore della proprietà codNoDisp.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCodNoDisp() {
        return codNoDisp;
    }

    /**
     * Imposta il valore della proprietà codNoDisp.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCodNoDisp(String value) {
        this.codNoDisp = value;
    }

    /**
     * Recupera il valore della proprietà numeroFascicoli.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumeroFascicoli() {
        return numeroFascicoli;
    }

    /**
     * Imposta il valore della proprietà numeroFascicoli.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumeroFascicoli(Integer value) {
        this.numeroFascicoli = value;
    }

    /**
     * Recupera il valore della proprietà cdCons.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCdCons() {
        return cdCons;
    }

    /**
     * Imposta il valore della proprietà cdCons.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCdCons(String value) {
        this.cdCons = value;
    }

    /**
     * Recupera il valore della proprietà cons.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCons() {
        return cons;
    }

    /**
     * Imposta il valore della proprietà cons.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCons(String value) {
        this.cons = value;
    }

    /**
     * Recupera il valore della proprietà seq.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSeq() {
        return seq;
    }

    /**
     * Imposta il valore della proprietà seq.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSeq(String value) {
        this.seq = value;
    }

    /**
     * Recupera il valore della proprietà url.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     * Imposta il valore della proprietà url.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUrl(String value) {
        this.url = value;
    }

}
