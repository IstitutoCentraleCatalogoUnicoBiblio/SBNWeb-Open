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
import javax.xml.bind.annotation.XmlType;


/**
 * risposta sbnweb-ws
 *
 * <p>Classe Java per sbnwebType complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType name="sbnwebType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esito" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}EsitoType"/>
 *         &lt;element name="documento" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}DocumentoType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="kardex" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}KardexType" minOccurs="0"/>
 *           &lt;element name="disponibilita" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}DisponibilitaType" minOccurs="0"/>
 *           &lt;element name="posseduto" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}PossedutoType" minOccurs="0"/>
 *           &lt;element name="utente" type="{http://www.iccu.sbn.it/sbnweb/sbnweb-ws}UtenteType" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sbnwebType", propOrder = {
    "esito",
    "documento",
    "kardex",
    "disponibilita",
    "posseduto",
    "utente"
})
public class SbnwebType

    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected EsitoType esito;
    protected DocumentoType documento;
    protected KardexType kardex;
    protected DisponibilitaType disponibilita;
    protected PossedutoType posseduto;
    protected UtenteType utente;

    /**
     * Recupera il valore della proprietà esito.
     *
     * @return
     *     possible object is
     *     {@link EsitoType }
     *
     */
    public EsitoType getEsito() {
        return esito;
    }

    /**
     * Imposta il valore della proprietà esito.
     *
     * @param value
     *     allowed object is
     *     {@link EsitoType }
     *
     */
    public void setEsito(EsitoType value) {
        this.esito = value;
    }

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
     * Recupera il valore della proprietà kardex.
     *
     * @return
     *     possible object is
     *     {@link KardexType }
     *
     */
    public KardexType getKardex() {
        return kardex;
    }

    /**
     * Imposta il valore della proprietà kardex.
     *
     * @param value
     *     allowed object is
     *     {@link KardexType }
     *
     */
    public void setKardex(KardexType value) {
        this.kardex = value;
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
     * Recupera il valore della proprietà posseduto.
     *
     * @return
     *     possible object is
     *     {@link PossedutoType }
     *
     */
    public PossedutoType getPosseduto() {
        return posseduto;
    }

    /**
     * Imposta il valore della proprietà posseduto.
     *
     * @param value
     *     allowed object is
     *     {@link PossedutoType }
     *
     */
    public void setPosseduto(PossedutoType value) {
        this.posseduto = value;
    }

    /**
     * Recupera il valore della proprietà utente.
     *
     * @return
     *     possible object is
     *     {@link UtenteType }
     *
     */
    public UtenteType getUtente() {
        return utente;
    }

    /**
     * Imposta il valore della proprietà utente.
     *
     * @param value
     *     allowed object is
     *     {@link UtenteType }
     *
     */
    public void setUtente(UtenteType value) {
        this.utente = value;
    }

}
