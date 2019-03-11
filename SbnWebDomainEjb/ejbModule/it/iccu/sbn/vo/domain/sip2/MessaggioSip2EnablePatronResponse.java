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

import it.iccu.sbn.exception.Sip2ValidationException;

//import javax.naming.*;

/**
 *
 * The ACS should send this message in response to the Patron Enable message from the SC.
 *
 * @author almaviva7
 */
public class MessaggioSip2EnablePatronResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
	/**
	 *
	 */
	private static final long serialVersionUID = -1409875317310264430L;
	protected String 	  patronStatus;			// 14-char, fixed-length required field
	protected String 	  language;				// 3-char, fixed-length required field
	protected String 	  transactionDate; 		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	  institutionId;			// AO variable-length required field
	protected String 	  patronIdentifier;		// AA variable-length required field
	protected String 	  personalName;			// AE variable-length required field
	protected String 	  validPatron;			// BL 1-char, optional field:  Y or N.
	protected String 	  validPatronPassword;	// CQ 1-char, optional field: Y or N



	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2EnablePatronRequest richiesta
     */
    public MessaggioSip2EnablePatronResponse(boolean enableErrorDetection, MessaggioSip2EnablePatronRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_PATRON_ENABLE_RESPONSE);
    	setValidPatron("");
    	setValidPatronPassword("");
    }

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param boolean enableErrorDetection
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2EnablePatronResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_PATRON_ENABLE_RESPONSE);
    }


    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.patronStatus == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Status response - campi obbligatori non validi [Patron status]");
		if (this.language == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Status response - campi obbligatori non validi [Language]");
		if (this.transactionDate == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Status response - campi obbligatori non validi [Transaction date]");
		if (this.institutionId == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Status response - campi obbligatori non validi [Institution id]");
		if (this.patronIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Status response - campi obbligatori non validi [Patron identifier]");
		if (this.personalName == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Status response - campi obbligatori non validi [Personal name]");
    	return true;
	}

    @Override
	public String toString() {
    	try {
			validateResponseRequiredFields();
		} catch (Sip2ValidationException e) {

			log.error(e);
		}
        String stringaMessaggio = "";
        stringaMessaggio += this.getCodiceMessaggio().getCode();
        stringaMessaggio += this.patronStatus;
       	stringaMessaggio += this.language;
       	stringaMessaggio += this.transactionDate;
        stringaMessaggio += this.institutionId;
       	stringaMessaggio += this.patronIdentifier;
        stringaMessaggio += this.personalName;
        stringaMessaggio += this.validPatron;
        stringaMessaggio += this.validPatronPassword;

        for (int i=0; i < this.screenMessagesVect.size(); i++)
			stringaMessaggio += this.screenMessagesVect.get(i);

		for (int i=0; i < this.printLineVect.size(); i++)
			stringaMessaggio += this.printLineVect.get(i);

        if (errorDetection == true)
        {
            stringaMessaggio += SIP2F_SEQUENCE_NUMBER + this.sequenceNumber; // 1 character sequence
            stringaMessaggio += SIP2F_CHECKSUM;
            stringaMessaggio += computeChecksum(stringaMessaggio);
        }
        return stringaMessaggio;
    }



	public String getInstitutionId() {
		return institutionId;
	}


	public void setInstitutionId(String institutionId) {
		this.institutionId = MessaggioSip2.SIP2F_INSTITUTION_ID + institutionId + "|";
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public String getPatronIdentifier() {
		return patronIdentifier;
	}



	public void setPatronIdentifier(String patronIdentifier) {
		this.patronIdentifier = MessaggioSip2.SIP2F_PATRON_IDENTIFIER + patronIdentifier + "|";
	}



	public String getPatronStatus() {
		return patronStatus;
	}



	public void setPatronStatus(String patronStatus) {
		this.patronStatus = patronStatus;
	}



	public String getPersonalName() {
		return personalName;
	}



	public void setPersonalName(String personalName) {
		this.personalName = MessaggioSip2.SIP2F_PERSONAL_NAME + personalName + "|";
	}




	public String getTransactionDate() {
		return transactionDate;
	}



	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}



	public String getValidPatron() {
		return validPatron;
	}



	public void setValidPatron(String validPatron) {
		this.validPatron = MessaggioSip2.SIP2F_VALID_PATRON + validPatron + "|"; // separatore in campo fisso?
	}



	public String getValidPatronPassword() {
		return validPatronPassword;
	}



	public void setValidPatronPassword(String validPatronPassword) {
		this.validPatronPassword = MessaggioSip2.SIP2F_VALID_PATRON_PASSWORD + validPatronPassword + "|"; // separatore in campo fisso?
	}



}
