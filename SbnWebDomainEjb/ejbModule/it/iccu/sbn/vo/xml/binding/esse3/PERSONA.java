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
// Generato il: 2018.12.03 alle 06:27:53 PM CET
//


package it.iccu.sbn.vo.xml.binding.esse3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per anonymous complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="TIPO_RECORD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="USER_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DISABLE_FLG" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
 *         &lt;element name="ANA_ID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="COGNOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATA_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="COD_FIS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EMAIL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EMAIL_ATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CELLULARE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NAZ_NASC_COD" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/>
 *         &lt;element name="NAZ_NASC_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COM_NASC_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COM_NASC_COD_ISTAT" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="COM_NASC_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CITSTRA_NASC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NAZ_RES_COD" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="NAZ_RES_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROV_RES_SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROV_RES_COD" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PROV_RES_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COM_RES_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COM_RES_COD_ISTAT" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="COM_RES_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LOC_RES_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDIRIZZO_RES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDIRIZZO_RES_CIVICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CAP_RES" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TEL_RES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NAZ_DOM_COD" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="NAZ_DOM_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROV_DOM_SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROV_DOM_COD" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PROV_DOM_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COM_DOM_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COM_DOM_COD_ISTAT" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="COM_DOM_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LOC_DOM_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDIRIZZO_DOM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDIRIZZO_DOM_CIVICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CAP_DOM" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TEL_DOM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATA_MOD_ANA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="STA_STU_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRICOLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATA_INI_ATT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="DATA_FIN_ATT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "PERSONA")
public class PERSONA {

    @XmlElement(name = "TIPO_RECORD", required = true)
    protected String tiporecord;
    @XmlElement(name = "USER_ID", required = true)
    protected String userid;
    @XmlElement(name = "DISABLE_FLG")
    protected Byte disableflg;
    @XmlElement(name = "ANA_ID")
    protected long anaid;
    @XmlElement(name = "COGNOME")
    protected String cognome;
    @XmlElement(name = "NOME")
    protected String nome;
    @XmlElement(name = "SESSO")
    protected String sesso;
    @XmlElement(name = "DATA_NASCITA")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datanascita;
    @XmlElement(name = "COD_FIS", required = true)
    protected String codfis;
    @XmlElement(name = "EMAIL")
    protected String email;
    @XmlElement(name = "EMAIL_ATE")
    protected String emailate;
    @XmlElement(name = "CELLULARE")
    protected String cellulare;
    @XmlElement(name = "NAZ_NASC_COD")
    protected Short naznasccod;
    @XmlElement(name = "NAZ_NASC_DES")
    protected String naznascdes;
    @XmlElement(name = "COM_NASC_COD")
    protected String comnasccod;
    @XmlElement(name = "COM_NASC_COD_ISTAT")
    protected Integer comnasccodistat;
    @XmlElement(name = "COM_NASC_DES")
    protected String comnascdes;
    @XmlElement(name = "CITSTRA_NASC")
    protected String citstranasc;
    @XmlElement(name = "NAZ_RES_COD")
    protected Integer nazrescod;
    @XmlElement(name = "NAZ_RES_DES")
    protected String nazresdes;
    @XmlElement(name = "PROV_RES_SIGLA")
    protected String provressigla;
    @XmlElement(name = "PROV_RES_COD")
    protected Integer provrescod;
    @XmlElement(name = "PROV_RES_DES")
    protected String provresdes;
    @XmlElement(name = "COM_RES_COD")
    protected String comrescod;
    @XmlElement(name = "COM_RES_COD_ISTAT")
    protected Integer comrescodistat;
    @XmlElement(name = "COM_RES_DES")
    protected String comresdes;
    @XmlElement(name = "LOC_RES_DES")
    protected String locresdes;
    @XmlElement(name = "INDIRIZZO_RES")
    protected String indirizzores;
    @XmlElement(name = "INDIRIZZO_RES_CIVICO")
    protected String indirizzorescivico;
    @XmlElement(name = "CAP_RES")
    protected Integer capres;
    @XmlElement(name = "TEL_RES")
    protected String telres;
    @XmlElement(name = "NAZ_DOM_COD")
    protected Integer nazdomcod;
    @XmlElement(name = "NAZ_DOM_DES")
    protected String nazdomdes;
    @XmlElement(name = "PROV_DOM_SIGLA")
    protected String provdomsigla;
    @XmlElement(name = "PROV_DOM_COD")
    protected Integer provdomcod;
    @XmlElement(name = "PROV_DOM_DES")
    protected String provdomdes;
    @XmlElement(name = "COM_DOM_COD")
    protected String comdomcod;
    @XmlElement(name = "COM_DOM_COD_ISTAT")
    protected Integer comdomcodistat;
    @XmlElement(name = "COM_DOM_DES")
    protected String comdomdes;
    @XmlElement(name = "LOC_DOM_DES")
    protected String locdomdes;
    @XmlElement(name = "INDIRIZZO_DOM")
    protected String indirizzodom;
    @XmlElement(name = "INDIRIZZO_DOM_CIVICO")
    protected String indirizzodomcivico;
    @XmlElement(name = "CAP_DOM")
    protected Integer capdom;
    @XmlElement(name = "TEL_DOM")
    protected String teldom;
    @XmlElement(name = "DATA_MOD_ANA")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datamodana;
    @XmlElement(name = "STA_STU_COD")
    protected String stastucod;
    @XmlElement(name = "MATRICOLA")
    protected String matricola;
    @XmlElement(name = "DATA_INI_ATT")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datainiatt;
    @XmlElement(name = "DATA_FIN_ATT")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datafinatt;

    /**
     * Recupera il valore della proprietà tiporecord.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTIPORECORD() {
        return tiporecord;
    }

    /**
     * Imposta il valore della proprietà tiporecord.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTIPORECORD(String value) {
        this.tiporecord = value;
    }

    /**
     * Recupera il valore della proprietà userid.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * Imposta il valore della proprietà userid.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

    /**
     * Recupera il valore della proprietà disableflg.
     *
     * @return
     *     possible object is
     *     {@link Byte }
     *
     */
    public Byte getDISABLEFLG() {
        return disableflg;
    }

    /**
     * Imposta il valore della proprietà disableflg.
     *
     * @param value
     *     allowed object is
     *     {@link Byte }
     *
     */
    public void setDISABLEFLG(Byte value) {
        this.disableflg = value;
    }

    /**
     * Recupera il valore della proprietà anaid.
     *
     */
    public long getANAID() {
        return anaid;
    }

    /**
     * Imposta il valore della proprietà anaid.
     *
     */
    public void setANAID(long value) {
        this.anaid = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOGNOME() {
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
    public void setCOGNOME(String value) {
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
    public String getNOME() {
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
    public void setNOME(String value) {
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
    public String getSESSO() {
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
    public void setSESSO(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà datanascita.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDATANASCITA() {
        return datanascita;
    }

    /**
     * Imposta il valore della proprietà datanascita.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDATANASCITA(XMLGregorianCalendar value) {
        this.datanascita = value;
    }

    /**
     * Recupera il valore della proprietà codfis.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCODFIS() {
        return codfis;
    }

    /**
     * Imposta il valore della proprietà codfis.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCODFIS(String value) {
        this.codfis = value;
    }

    /**
     * Recupera il valore della proprietà email.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEMAIL() {
        return email;
    }

    /**
     * Imposta il valore della proprietà email.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEMAIL(String value) {
        this.email = value;
    }

    /**
     * Recupera il valore della proprietà emailate.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEMAILATE() {
        return emailate;
    }

    /**
     * Imposta il valore della proprietà emailate.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEMAILATE(String value) {
        this.emailate = value;
    }

    /**
     * Recupera il valore della proprietà cellulare.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCELLULARE() {
        return cellulare;
    }

    /**
     * Imposta il valore della proprietà cellulare.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCELLULARE(String value) {
        this.cellulare = value;
    }

    /**
     * Recupera il valore della proprietà naznasccod.
     *
     * @return
     *     possible object is
     *     {@link Short }
     *
     */
    public Short getNAZNASCCOD() {
        return naznasccod;
    }

    /**
     * Imposta il valore della proprietà naznasccod.
     *
     * @param value
     *     allowed object is
     *     {@link Short }
     *
     */
    public void setNAZNASCCOD(Short value) {
        this.naznasccod = value;
    }

    /**
     * Recupera il valore della proprietà naznascdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAZNASCDES() {
        return naznascdes;
    }

    /**
     * Imposta il valore della proprietà naznascdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAZNASCDES(String value) {
        this.naznascdes = value;
    }

    /**
     * Recupera il valore della proprietà comnasccod.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOMNASCCOD() {
        return comnasccod;
    }

    /**
     * Imposta il valore della proprietà comnasccod.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOMNASCCOD(String value) {
        this.comnasccod = value;
    }

    /**
     * Recupera il valore della proprietà comnasccodistat.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCOMNASCCODISTAT() {
        return comnasccodistat;
    }

    /**
     * Imposta il valore della proprietà comnasccodistat.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCOMNASCCODISTAT(Integer value) {
        this.comnasccodistat = value;
    }

    /**
     * Recupera il valore della proprietà comnascdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOMNASCDES() {
        return comnascdes;
    }

    /**
     * Imposta il valore della proprietà comnascdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOMNASCDES(String value) {
        this.comnascdes = value;
    }

    /**
     * Recupera il valore della proprietà citstranasc.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCITSTRANASC() {
        return citstranasc;
    }

    /**
     * Imposta il valore della proprietà citstranasc.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCITSTRANASC(String value) {
        this.citstranasc = value;
    }

    /**
     * Recupera il valore della proprietà nazrescod.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNAZRESCOD() {
        return nazrescod;
    }

    /**
     * Imposta il valore della proprietà nazrescod.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNAZRESCOD(Integer value) {
        this.nazrescod = value;
    }

    /**
     * Recupera il valore della proprietà nazresdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAZRESDES() {
        return nazresdes;
    }

    /**
     * Imposta il valore della proprietà nazresdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAZRESDES(String value) {
        this.nazresdes = value;
    }

    /**
     * Recupera il valore della proprietà provressigla.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPROVRESSIGLA() {
        return provressigla;
    }

    /**
     * Imposta il valore della proprietà provressigla.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPROVRESSIGLA(String value) {
        this.provressigla = value;
    }

    /**
     * Recupera il valore della proprietà provrescod.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPROVRESCOD() {
        return provrescod;
    }

    /**
     * Imposta il valore della proprietà provrescod.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPROVRESCOD(Integer value) {
        this.provrescod = value;
    }

    /**
     * Recupera il valore della proprietà provresdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPROVRESDES() {
        return provresdes;
    }

    /**
     * Imposta il valore della proprietà provresdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPROVRESDES(String value) {
        this.provresdes = value;
    }

    /**
     * Recupera il valore della proprietà comrescod.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOMRESCOD() {
        return comrescod;
    }

    /**
     * Imposta il valore della proprietà comrescod.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOMRESCOD(String value) {
        this.comrescod = value;
    }

    /**
     * Recupera il valore della proprietà comrescodistat.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCOMRESCODISTAT() {
        return comrescodistat;
    }

    /**
     * Imposta il valore della proprietà comrescodistat.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCOMRESCODISTAT(Integer value) {
        this.comrescodistat = value;
    }

    /**
     * Recupera il valore della proprietà comresdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOMRESDES() {
        return comresdes;
    }

    /**
     * Imposta il valore della proprietà comresdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOMRESDES(String value) {
        this.comresdes = value;
    }

    /**
     * Recupera il valore della proprietà locresdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLOCRESDES() {
        return locresdes;
    }

    /**
     * Imposta il valore della proprietà locresdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLOCRESDES(String value) {
        this.locresdes = value;
    }

    /**
     * Recupera il valore della proprietà indirizzores.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getINDIRIZZORES() {
        return indirizzores;
    }

    /**
     * Imposta il valore della proprietà indirizzores.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setINDIRIZZORES(String value) {
        this.indirizzores = value;
    }

    /**
     * Recupera il valore della proprietà indirizzorescivico.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getINDIRIZZORESCIVICO() {
        return indirizzorescivico;
    }

    /**
     * Imposta il valore della proprietà indirizzorescivico.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setINDIRIZZORESCIVICO(String value) {
        this.indirizzorescivico = value;
    }

    /**
     * Recupera il valore della proprietà capres.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCAPRES() {
        return capres;
    }

    /**
     * Imposta il valore della proprietà capres.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCAPRES(Integer value) {
        this.capres = value;
    }

    /**
     * Recupera il valore della proprietà telres.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTELRES() {
        return telres;
    }

    /**
     * Imposta il valore della proprietà telres.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTELRES(String value) {
        this.telres = value;
    }

    /**
     * Recupera il valore della proprietà nazdomcod.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNAZDOMCOD() {
        return nazdomcod;
    }

    /**
     * Imposta il valore della proprietà nazdomcod.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNAZDOMCOD(Integer value) {
        this.nazdomcod = value;
    }

    /**
     * Recupera il valore della proprietà nazdomdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAZDOMDES() {
        return nazdomdes;
    }

    /**
     * Imposta il valore della proprietà nazdomdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAZDOMDES(String value) {
        this.nazdomdes = value;
    }

    /**
     * Recupera il valore della proprietà provdomsigla.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPROVDOMSIGLA() {
        return provdomsigla;
    }

    /**
     * Imposta il valore della proprietà provdomsigla.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPROVDOMSIGLA(String value) {
        this.provdomsigla = value;
    }

    /**
     * Recupera il valore della proprietà provdomcod.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPROVDOMCOD() {
        return provdomcod;
    }

    /**
     * Imposta il valore della proprietà provdomcod.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPROVDOMCOD(Integer value) {
        this.provdomcod = value;
    }

    /**
     * Recupera il valore della proprietà provdomdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPROVDOMDES() {
        return provdomdes;
    }

    /**
     * Imposta il valore della proprietà provdomdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPROVDOMDES(String value) {
        this.provdomdes = value;
    }

    /**
     * Recupera il valore della proprietà comdomcod.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOMDOMCOD() {
        return comdomcod;
    }

    /**
     * Imposta il valore della proprietà comdomcod.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOMDOMCOD(String value) {
        this.comdomcod = value;
    }

    /**
     * Recupera il valore della proprietà comdomcodistat.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCOMDOMCODISTAT() {
        return comdomcodistat;
    }

    /**
     * Imposta il valore della proprietà comdomcodistat.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCOMDOMCODISTAT(Integer value) {
        this.comdomcodistat = value;
    }

    /**
     * Recupera il valore della proprietà comdomdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOMDOMDES() {
        return comdomdes;
    }

    /**
     * Imposta il valore della proprietà comdomdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOMDOMDES(String value) {
        this.comdomdes = value;
    }

    /**
     * Recupera il valore della proprietà locdomdes.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLOCDOMDES() {
        return locdomdes;
    }

    /**
     * Imposta il valore della proprietà locdomdes.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLOCDOMDES(String value) {
        this.locdomdes = value;
    }

    /**
     * Recupera il valore della proprietà indirizzodom.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getINDIRIZZODOM() {
        return indirizzodom;
    }

    /**
     * Imposta il valore della proprietà indirizzodom.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setINDIRIZZODOM(String value) {
        this.indirizzodom = value;
    }

    /**
     * Recupera il valore della proprietà indirizzodomcivico.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getINDIRIZZODOMCIVICO() {
        return indirizzodomcivico;
    }

    /**
     * Imposta il valore della proprietà indirizzodomcivico.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setINDIRIZZODOMCIVICO(String value) {
        this.indirizzodomcivico = value;
    }

    /**
     * Recupera il valore della proprietà capdom.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCAPDOM() {
        return capdom;
    }

    /**
     * Imposta il valore della proprietà capdom.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCAPDOM(Integer value) {
        this.capdom = value;
    }

    /**
     * Recupera il valore della proprietà teldom.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTELDOM() {
        return teldom;
    }

    /**
     * Imposta il valore della proprietà teldom.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTELDOM(String value) {
        this.teldom = value;
    }

    /**
     * Recupera il valore della proprietà datamodana.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDATAMODANA() {
        return datamodana;
    }

    /**
     * Imposta il valore della proprietà datamodana.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDATAMODANA(XMLGregorianCalendar value) {
        this.datamodana = value;
    }

    /**
     * Recupera il valore della proprietà stastucod.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSTASTUCOD() {
        return stastucod;
    }

    /**
     * Imposta il valore della proprietà stastucod.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSTASTUCOD(String value) {
        this.stastucod = value;
    }

    /**
     * Recupera il valore della proprietà matricola.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMATRICOLA() {
        return matricola;
    }

    /**
     * Imposta il valore della proprietà matricola.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMATRICOLA(String value) {
        this.matricola = value;
    }

    /**
     * Recupera il valore della proprietà datainiatt.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDATAINIATT() {
        return datainiatt;
    }

    /**
     * Imposta il valore della proprietà datainiatt.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDATAINIATT(XMLGregorianCalendar value) {
        this.datainiatt = value;
    }

    /**
     * Recupera il valore della proprietà datafinatt.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDATAFINATT() {
        return datafinatt;
    }

    /**
     * Imposta il valore della proprietà datafinatt.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDATAFINATT(XMLGregorianCalendar value) {
        this.datafinatt = value;
    }

}
