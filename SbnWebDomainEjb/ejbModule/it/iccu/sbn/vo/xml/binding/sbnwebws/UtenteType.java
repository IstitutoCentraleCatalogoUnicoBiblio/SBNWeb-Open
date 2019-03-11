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
// Generato il: 2018.05.10 alle 09:21:21 PM CEST
//


package it.iccu.sbn.vo.xml.binding.sbnwebws;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per UtenteType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="UtenteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accesso" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}AccessoType" minOccurs="0"/>
 *         &lt;element name="user-id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cod-fiscale">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="cognome">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;maxLength value="80"/>
 *                   &lt;whiteSpace value="collapse"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *             &lt;element name="nome">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;maxLength value="25"/>
 *                   &lt;whiteSpace value="collapse"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *             &lt;element name="sesso">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;enumeration value="M"/>
 *                   &lt;enumeration value="F"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="denominazione">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;maxLength value="80"/>
 *                   &lt;whiteSpace value="collapse"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *             &lt;element name="tipo-ente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="email" maxOccurs="2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
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
@XmlType(name = "UtenteType", propOrder = {
    "accesso",
    "userId",
    "codFiscale",
    "cognome",
    "nome",
    "sesso",
    "denominazione",
    "tipoEnte",
    "email"
})
public class UtenteType
    extends SerializableVO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected AccessoType accesso;
    @XmlElement(name = "user-id", required = true)
    protected String userId;
    @XmlElement(name = "cod-fiscale", required = true)
    protected String codFiscale;
    protected String cognome;
    protected String nome;
    protected String sesso;
    protected String denominazione;
    @XmlElement(name = "tipo-ente")
    protected String tipoEnte;
    protected List<String> email;

    /**
     * Recupera il valore della proprietà accesso.
     *
     * @return
     *     possible object is
     *     {@link AccessoType }
     *
     */
    public AccessoType getAccesso() {
        return accesso;
    }

    /**
     * Imposta il valore della proprietà accesso.
     *
     * @param value
     *     allowed object is
     *     {@link AccessoType }
     *
     */
    public void setAccesso(AccessoType value) {
        this.accesso = value;
    }

    /**
     * Recupera il valore della proprietà userId.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Imposta il valore della proprietà userId.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Recupera il valore della proprietà codFiscale.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCodFiscale() {
        return codFiscale;
    }

    /**
     * Imposta il valore della proprietà codFiscale.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCodFiscale(String value) {
        this.codFiscale = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà sesso.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietà sesso.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà denominazione.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * Imposta il valore della proprietà denominazione.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDenominazione(String value) {
        this.denominazione = value;
    }

    /**
     * Recupera il valore della proprietà tipoEnte.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTipoEnte() {
        return tipoEnte;
    }

    /**
     * Imposta il valore della proprietà tipoEnte.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTipoEnte(String value) {
        this.tipoEnte = value;
    }

    /**
     * Gets the value of the email property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the email property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmail().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getEmail() {
        if (email == null) {
            email = new ArrayList<String>();
        }
        return this.email;
    }

}
