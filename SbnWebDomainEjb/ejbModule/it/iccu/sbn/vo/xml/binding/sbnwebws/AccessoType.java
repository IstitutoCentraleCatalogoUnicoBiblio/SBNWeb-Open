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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AccessoType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="AccessoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="autenticato" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="evento" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}EventoType"/>
 *         &lt;element name="dataEvento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccessoType", propOrder = {
    "autenticato",
    "evento",
    "dataEvento"
})
public class AccessoType
    extends SerializableVO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected boolean autenticato;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EventoType evento;
    @XmlElement(required = true)
    protected String dataEvento;

    /**
     * Recupera il valore della proprietà autenticato.
     *
     */
    public boolean isAutenticato() {
        return autenticato;
    }

    /**
     * Imposta il valore della proprietà autenticato.
     *
     */
    public void setAutenticato(boolean value) {
        this.autenticato = value;
    }

    /**
     * Recupera il valore della proprietà evento.
     *
     * @return
     *     possible object is
     *     {@link EventoType }
     *
     */
    public EventoType getEvento() {
        return evento;
    }

    /**
     * Imposta il valore della proprietà evento.
     *
     * @param value
     *     allowed object is
     *     {@link EventoType }
     *
     */
    public void setEvento(EventoType value) {
        this.evento = value;
    }

    /**
     * Recupera il valore della proprietà dataEvento.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDataEvento() {
        return dataEvento;
    }

    /**
     * Imposta il valore della proprietà dataEvento.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDataEvento(String value) {
        this.dataEvento = value;
    }

}
