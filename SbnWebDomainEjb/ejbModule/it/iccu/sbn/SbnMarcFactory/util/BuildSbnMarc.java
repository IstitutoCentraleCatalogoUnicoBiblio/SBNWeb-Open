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
package it.iccu.sbn.SbnMarcFactory.util;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;


public class BuildSbnMarc {

	private static Logger log = Logger.getLogger(BuildSbnMarc.class);

	private UtilityCastor utilityCastor = new UtilityCastor();

	private BigDecimal SCHEMA_VERSION;

	public BuildSbnMarc() {
		SCHEMA_VERSION = SBNMarcUtil.getSchemaVersion();
	}

	public SBNMarc getDummyErrorXML(String tipoServer, SbnUserType user) throws SbnMarcException {
		SBNMarc sbnmarc = creaIntestazione(user);
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		result.setEsito("9999");
		String testoEsito = "";
		if (ValidazioneDati.isFilled(tipoServer))
			testoEsito = "Protocollo di " + tipoServer + ": ";

		result.setTestoEsito(testoEsito + "Impossibile contattare il server");
		response.setSbnResult(result);
		SbnResponseTypeChoice responseType = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		responseType.setSbnOutput(output);
		response.setSbnResponseTypeChoice(responseType);
		message.setSbnResponse(response);
		sbnmarc.setSbnMessage(message);
		return sbnmarc;
	}

    public SBNMarc creaIntestazione(SbnUserType user) throws SbnMarcException {
        SBNMarc sbnRootRequest = null;
        try {
            sbnRootRequest = new SBNMarc();
            sbnRootRequest.setSbnUser(user);
            sbnRootRequest.setSchemaVersion(SCHEMA_VERSION);
        } catch (Exception e) {
            log.error("", e);
        	throw new SbnMarcException("Impossibile creare SbnMarc", e);
        }
        return sbnRootRequest;
    }

	public void validateSbnMarc(SBNMarc sbnmarc)
			throws org.exolab.castor.xml.ValidationException, Exception {
		sbnmarc.validate();
	}

    public String marshalSbnMarc(SBNMarc sbnmarc) throws ValidationException, Exception {
        String xml = null;
        if (sbnmarc != null) {
            try {
                StringWriter stringWriter = new StringWriter();
                // 1- Validate
                sbnmarc.validate();
                // 2- Marshall
                sbnmarc.marshal(stringWriter);
                xml = stringWriter.toString();
                stringWriter.close();

            } catch (org.exolab.castor.xml.ValidationException e) {
            	log.error("", e);
                throw e;
            } catch (Exception e) {
            	log.error("", e);
                throw e;
            }
        }
        return xml;
    }
    public SBNMarc unmarshalSbnMarc(String sbnmarc) throws MarshalException, ValidationException
    {
        StringReader sr = new StringReader(sbnmarc);
        SBNMarc sbnRisposta = null;
		try {
			sbnRisposta = SBNMarc.unmarshalSBNMarc(sr);

	        if (!utilityCastor.isEsitoPositivo(sbnRisposta)) {
	        	String messaggioErroreXML = null;
	            messaggioErroreXML = utilityCastor.getTestoEsito(sbnRisposta);

	            // Nessun elemento trovato
	            if (!utilityCastor.isElementiTrovati(sbnRisposta)) {

	                if (utilityCastor.isError3001(sbnRisposta)) {
	                    // Informazione
	                	log.info(messaggioErroreXML);

	                } else {
	                	log.info(messaggioErroreXML);
	                }

	            } else
		            // Bypass dell'errore 3004
		            // Client/server ometto gestione dei simili
		            // reinserire appena possibile
		            if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)) {
		                log.info("Trovati simili");
		            } else
			            // Bypass dell'errore 3014
			            if (utilityCastor.isOggettoModificatoAltroUtente(sbnRisposta)) {

			                // Errore 3014: La data di variazione non coincide
			                messaggioErroreXML = "Altri aggiornamenti in corso sullo stesso oggetto: ripetere la ricerca";
			                log.info(messaggioErroreXML);
			                //sbnRisposta = null;
			            }
	            // 1020 Errore lettura file XML di recupero
	            // if (XMLCastUtil.isErroreDiRecupero(sbnRisposta)){
	            // sbnRisposta = null;
	        }
		} catch (MarshalException e) {
			log.error("", e);
			throw e;
		} catch (ValidationException e) {
			log.error("", e);
			throw e;
		} finally {
			sr.close();
		}

		return sbnRisposta;
    }

	public static SBNMarc unmarshal(String xml) throws MarshalException, ValidationException {
		StringReader sr = new StringReader(xml);
		return SBNMarc.unmarshalSBNMarc(sr);
	}

	 public static String marshal(SBNMarc sbnmarc) throws ValidationException, Exception {
		 BuildSbnMarc build = new BuildSbnMarc();
		 return build.marshalSbnMarc(sbnmarc);
	 }

}
