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
 * dati sulla prenotabilità del documento
 *
 * <p>Classe Java per PrenotazioniType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="PrenotazioniType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="servizio" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}ServizioType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="numeroPrenotazioni" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="prenotabile" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrenotazioniType", propOrder = {
    "servizio"
})
public class PrenotazioniType

    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<String> servizio;
    @XmlAttribute(name = "numeroPrenotazioni")
    protected Integer numeroPrenotazioni;
    @XmlAttribute(name = "prenotabile", required = true)
    protected boolean prenotabile;

    /**
     * Gets the value of the servizio property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servizio property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServizio().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getServizio() {
        if (servizio == null) {
            servizio = new ArrayList<String>();
        }
        return this.servizio;
    }

    /**
     * Recupera il valore della proprietà numeroPrenotazioni.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumeroPrenotazioni() {
        return numeroPrenotazioni;
    }

    /**
     * Imposta il valore della proprietà numeroPrenotazioni.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumeroPrenotazioni(Integer value) {
        this.numeroPrenotazioni = value;
    }

    /**
     * Recupera il valore della proprietà prenotabile.
     *
     */
    public boolean isPrenotabile() {
        return prenotabile;
    }

    /**
     * Imposta il valore della proprietà prenotabile.
     *
     */
    public void setPrenotabile(boolean value) {
        this.prenotabile = value;
    }

}
