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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnMessage;
import it.finsiel.sbn.polo.exception.util.Errore;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.util.MarshallingUtil;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Classe FormatoErrore.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 10-feb-2003
 */
public class FormatoErrore {

    private static Logger log = Logger.getLogger("iccu.box.FormatoErrore");
    private static BigDecimal schemaVersion = ResourceLoader.getPropertyBigDecimal("SCHEMA_VERSION");

	public static final SbnUserType FAKE_SBN_USER;

	static {
		FAKE_SBN_USER = new SbnUserType(); //utente fittizio
		FAKE_SBN_USER.setBiblioteca("      ");
		FAKE_SBN_USER.setUserId("      ");
	}

    /** Prepara il messaggio di errore, in base a quale eccezione di è verificata */
	public static final SBNMarc buildMessaggioErrore(EccezioneIccu ecc,	SbnUserType user) {
		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		String messaggio = ValidazioneDati.trimOrEmpty(ecc.getMessaggio());
		result.setTestoEsito(ecc.getErrorID() + " " + messaggio);
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		String errore = "" + ecc.getErrorID();
		int i = 4 - errore.length();
		for (; i > 0; i--)
			errore = "0" + errore;
		result.setEsito(errore);
		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(user);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);

		return sbnmarc;
	}


    /** Prepara il messaggio di errore, in base a quale eccezione di è verificata */
    public static final String preparaMessaggioErrore(EccezioneIccu ecc, SbnUserType user) {
        String string_response;
        SBNMarc sbnmarc = buildMessaggioErrore(ecc, user);
        try {
            string_response = MarshallingUtil.marshal(sbnmarc);
        } catch (Exception e) {
            log.error("Errore marshalling " + e);
            string_response = "Errore costruzione xml diagnostico";
        }
        return string_response;
    }

    public static final SBNMarc buildMessaggioDiagnostico(EccezioneSbnMessage ecc, SbnUserType user) {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
		String messaggio = ValidazioneDati.trimOrEmpty(ecc.getMessaggio());
		result.setTestoEsito(ecc.getErrorID() + " " + messaggio);
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		String errore = "" + ecc.getErrorID();
		int i = 4 - errore.length();
		for (; i > 0; i--)
			errore = "0" + errore;
        result.setEsito(errore);
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);

        return sbnmarc;
    }

    public static final SBNMarc buildMessaggioErrore(EccezioneIccu errore) {
        return buildMessaggioErrore(errore, FAKE_SBN_USER);
    }

    public static final String preparaMessaggioErrore(EccezioneIccu errore) {
        return preparaMessaggioErrore(errore, FAKE_SBN_USER);
    }


    public static final String preparaMessaggioDiagnostico(EccezioneSbnMessage ecc, SbnUserType user) {
        String string_response;
        SBNMarc sbnmarc = buildMessaggioDiagnostico(ecc, user);
        try {
            string_response = MarshallingUtil.marshal(sbnmarc);
        } catch (Exception e) {
            log.error("Errore marshalling " + e);
            string_response = "Errore costruzione xml diagnostico";
        }
        return string_response;
    }

    public static final String preparaMessaggioErrore(int errore) {
        return preparaMessaggioErrore(errore, FAKE_SBN_USER);
    }


	public static final SBNMarc buildMessaggioErrore(int errore, SbnUserType user) {
		return buildMessaggioErrore(errore, user, 0);
	}

	public static SBNMarc buildMessaggioErrore(int errore, SbnUserType user, int traceId) {
		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		String messaggio = Decodificatore.getErrore(errore).getDescrizione();
		//almaviva5_20131115
		if (traceId > 0)
			messaggio = String.format("%s [errorId: %d]", messaggio, traceId);

		result.setTestoEsito(messaggio);
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		String errors = "" + errore;
		int i = 4 - errors.length();
		for (; i > 0; i--)
			errors = "0" + errors;
		result.setEsito(errors);
		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(user);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);

		return sbnmarc;
	}

    /** Prepara il messaggio di errore, in base a quale eccezione di è verificata */
    public static final String preparaMessaggioErrore(int errore, SbnUserType user) {
        String string_response;
        SBNMarc sbnmarc = buildMessaggioErrore(errore, user);
        try {
            string_response = MarshallingUtil.marshal(sbnmarc);
        } catch (Exception e) {
            log.error("Errore marshalling " + e);
            string_response = "Errore costruzione xml diagnostico";
        }
        return string_response;
    }

    public static final SbnResultType buildSbnResult(int idErrore) {
    	SbnResultType result = new SbnResultType();
    	Errore e = Decodificatore.getErrore(idErrore);
    	if (e != null) {
    		result.setEsito(String.format("%04d", e.getId() ) );
    		result.setTestoEsito(e.getDescrizione() );
    	} else {
    		result.setEsito(String.format("%04d", idErrore) );
    		result.setTestoEsito(Decodificatore.getErrore(-1).getDescrizione() );
    	}

        return result;
    }

}
