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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the it.iccu.sbn.vo.xml.binding.sbnwebws package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Sbnweb_QNAME = new QName("http://www.iccu.sbn.it/sbnweb/sbnweb-ws", "sbnweb");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.iccu.sbn.vo.xml.binding.sbnwebws
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SbnwebType }
     *
     */
    public SbnwebType createSbnwebType() {
        return new SbnwebType();
    }

    /**
     * Create an instance of {@link PrenotazioniType }
     *
     */
    public PrenotazioniType createPrenotazioniType() {
        return new PrenotazioniType();
    }

    /**
     * Create an instance of {@link UtenteType }
     *
     */
    public UtenteType createUtenteType() {
        return new UtenteType();
    }

    /**
     * Create an instance of {@link FascicoloType }
     *
     */
    public FascicoloType createFascicoloType() {
        return new FascicoloType();
    }

    /**
     * Create an instance of {@link InventarioType }
     *
     */
    public InventarioType createInventarioType() {
        return new InventarioType();
    }

    /**
     * Create an instance of {@link PossessoreType }
     *
     */
    public PossessoreType createPossessoreType() {
        return new PossessoreType();
    }

    /**
     * Create an instance of {@link EsitoType }
     *
     */
    public EsitoType createEsitoType() {
        return new EsitoType();
    }

    /**
     * Create an instance of {@link NonCollocatoType }
     *
     */
    public NonCollocatoType createNonCollocatoType() {
        return new NonCollocatoType();
    }

    /**
     * Create an instance of {@link KardexType }
     *
     */
    public KardexType createKardexType() {
        return new KardexType();
    }

    /**
     * Create an instance of {@link AccessoType }
     *
     */
    public AccessoType createAccessoType() {
        return new AccessoType();
    }

    /**
     * Create an instance of {@link DisponibilitaType }
     *
     */
    public DisponibilitaType createDisponibilitaType() {
        return new DisponibilitaType();
    }

    /**
     * Create an instance of {@link DocumentoType }
     *
     */
    public DocumentoType createDocumentoType() {
        return new DocumentoType();
    }

    /**
     * Create an instance of {@link CollocazioneType }
     *
     */
    public CollocazioneType createCollocazioneType() {
        return new CollocazioneType();
    }

    /**
     * Create an instance of {@link PossedutoType }
     *
     */
    public PossedutoType createPossedutoType() {
        return new PossedutoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SbnwebType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.iccu.sbn.it/sbnweb/sbnweb-ws", name = "sbnweb")
    public JAXBElement<SbnwebType> createSbnweb(SbnwebType value) {
        return new JAXBElement<SbnwebType>(_Sbnweb_QNAME, SbnwebType.class, null, value);
    }

}
