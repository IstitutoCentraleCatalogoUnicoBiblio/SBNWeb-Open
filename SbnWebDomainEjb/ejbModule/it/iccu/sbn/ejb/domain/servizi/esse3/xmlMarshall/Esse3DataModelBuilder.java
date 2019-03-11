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
package it.iccu.sbn.ejb.domain.servizi.esse3.xmlMarshall;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;

public class Esse3DataModelBuilder {
	static Logger log = Logger.getLogger(Esse3DataModelBuilder.class);

	private static JAXBContext ctx;
	protected static Schema apdu_xsd;
	private static String XML_ENCODING = "UTF-8";
	//private static ObjectFactory factory;

	static {
		try {
			//factory = new ObjectFactory();
			ctx = JAXBContext.newInstance(PERSONA.class.getPackage().getName());
			SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			URL xsd_url = Esse3DataModelBuilder.class.getResource("/META-INF/esse3sync.xsd");
			apdu_xsd = sf.newSchema(xsd_url);
		} catch (JAXBException e) {
			log.error("", e);
		} catch (SAXException e) {
			log.error("", e);
		}
	}


	public static PERSONA unmarshal(String xml) throws JAXBException {
		Unmarshaller um = ctx.createUnmarshaller();
		PERSONA persona = (PERSONA) um.unmarshal(new StringReader(xml));

		return persona;
	}

	public static String marshal(PERSONA apdu) throws JAXBException {
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, XML_ENCODING);
		//m.setSchema(apdu_xsd);
		StringWriter sw = new StringWriter(4096);
		m.marshal(apdu, sw);

		return sw.toString();
	}
	public static List<PERSONA> unmarshalToSingleValueList(String xml) throws JAXBException {
		PERSONA persona = unmarshal(xml);
		List<PERSONA> persone = new ArrayList<PERSONA>();
		if(persona != null)
			persone.add(persona);
		return persone;
	}

}
