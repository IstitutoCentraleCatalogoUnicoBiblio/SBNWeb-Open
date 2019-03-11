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
package it.iccu.sbn.vo.domain.sip2;

/**
 * The ACS should send this message in response to the Login message.
 * When this message is used, it will be the first message sent to the SC.
 *
 * @author almaviva7
 */

public class MessaggioSip2LoginResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
	/**
	 *
	 */
	private static final long serialVersionUID = 2070224749004333949L;
	char ok = ' ';	// 1-char, fixed-length required field:  0 or 1.

    /**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2LoginResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_LOGIN_RESPONSE);
    	setOk('0'); //default not ok
    }

	public boolean isOk() {
		if (ok == '0')
			return false;
		else
			return true;

	}

	public void setOk(char ok) {
		this.ok = ok;
	}

/*    public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.ok == ' ')
			// TO DO the rest...
			throw new Sip2ValidationException("Sip2 protocol: Login response - campi obbligatori non validi");
    	return true;
	}*/

    @Override
	public String toString() {
/*    	try {
			validateResponseRequiredFields();
		} catch (Sip2ValidationException e) {

			log.error(e);
		}*/
        String stringaMessaggio = "";
        stringaMessaggio += getCodiceMessaggio().getCode();
       	stringaMessaggio += this.ok;
        if (errorDetection == true)
        {
            stringaMessaggio += SIP2F_SEQUENCE_NUMBER + this.sequenceNumber; // 1 character sequence
            stringaMessaggio += SIP2F_CHECKSUM;
            stringaMessaggio += computeChecksum(stringaMessaggio);
        }
        return stringaMessaggio;
    }


}
