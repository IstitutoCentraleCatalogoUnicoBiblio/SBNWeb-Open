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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * disponibilità dell'inventario
 *
 * <p>Classe Java per DisponibilitaType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="DisponibilitaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="prenotazioni" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}PrenotazioniType" minOccurs="0"/>
 *         &lt;element name="inventario" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}InventarioType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="disponibile" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="dataDisponibilita" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisponibilitaType", propOrder = {
    "prenotazioni",
    "inventario"
})
public class DisponibilitaType

    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected PrenotazioniType prenotazioni;
    protected InventarioType inventario;
    @XmlAttribute(name = "disponibile", required = true)
    protected boolean disponibile;
    @XmlAttribute(name = "dataDisponibilita")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataDisponibilita;
    @XmlAttribute(name = "motivo")
    protected String motivo;

    /**
     * Recupera il valore della proprietà prenotazioni.
     *
     * @return
     *     possible object is
     *     {@link PrenotazioniType }
     *
     */
    public PrenotazioniType getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Imposta il valore della proprietà prenotazioni.
     *
     * @param value
     *     allowed object is
     *     {@link PrenotazioniType }
     *
     */
    public void setPrenotazioni(PrenotazioniType value) {
        this.prenotazioni = value;
    }

    /**
     * Recupera il valore della proprietà inventario.
     *
     * @return
     *     possible object is
     *     {@link InventarioType }
     *
     */
    public InventarioType getInventario() {
        return inventario;
    }

    /**
     * Imposta il valore della proprietà inventario.
     *
     * @param value
     *     allowed object is
     *     {@link InventarioType }
     *
     */
    public void setInventario(InventarioType value) {
        this.inventario = value;
    }

    /**
     * Recupera il valore della proprietà disponibile.
     *
     */
    public boolean isDisponibile() {
        return disponibile;
    }

    /**
     * Imposta il valore della proprietà disponibile.
     *
     */
    public void setDisponibile(boolean value) {
        this.disponibile = value;
    }

    /**
     * Recupera il valore della proprietà dataDisponibilita.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDataDisponibilita() {
        return dataDisponibilita;
    }

    /**
     * Imposta il valore della proprietà dataDisponibilita.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDataDisponibilita(XMLGregorianCalendar value) {
        this.dataDisponibilita = value;
    }

    /**
     * Recupera il valore della proprietà motivo.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Imposta il valore della proprietà motivo.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMotivo(String value) {
        this.motivo = value;
    }

}
