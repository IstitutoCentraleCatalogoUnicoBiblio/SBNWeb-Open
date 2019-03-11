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
 * 	The ACS must send this message in response to the End Patron Session message.
 * @author Finsiel
 */
public class MessaggioSip2EndPatronSessionResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = 1723024673986596991L;
	protected char	endSession;			// 1-char, fixed-length required field:  Y or N.
	protected String	transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	institutionId;		// AO variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field.


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2EndPatronSessionResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_END_SESSION_RESPONSE);
    }
	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2EndPatronSessionRequest richiesta
     */
    public MessaggioSip2EndPatronSessionResponse(boolean enableErrorDetection, MessaggioSip2EndPatronSessionRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_END_SESSION_RESPONSE);
    	// Campi obbligatori
    	// 2.00 field end-session (1 char: Y / N)
    	setEndSession('Y');
    	setTransactionDate(req.getTransactionDate());
    	setInstitutionId(req.getInstitutionId());
    	setPatronIdentifier(req.getPatronIdentifier());
    }

    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
    	// 2.00 end session (Y/N)
//		if (this.endSession != 'Y' && this.endSession != 'N')
//			throw new Sip2ValidationException("Sip2 protocol: End Patron Session Response - campi obbligatori non validi [End session]");
		if (this.transactionDate == null)
			throw new Sip2ValidationException("Sip2 protocol: End Patron Session Response - campi obbligatori non validi [Transaction Date]");
		if (this.institutionId == null)
			throw new Sip2ValidationException("Sip2 protocol: End Patron Session Response - campi obbligatori non validi [Institution Id]");
		if (this.patronIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: End Patron Session Response - campi obbligatori non validi [Patron Identifier]");
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
        stringaMessaggio += this.endSession;
        stringaMessaggio += this.transactionDate;
        stringaMessaggio += this.institutionId;
        stringaMessaggio += this.patronIdentifier;

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

	public String getPatronIdentifier() {
		return patronIdentifier;
	}

	public void setPatronIdentifier(String patronIdentifier) {
		this.patronIdentifier = MessaggioSip2.SIP2F_PATRON_IDENTIFIER + patronIdentifier + "|";
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public char getEndSession() {
		return endSession;
	}
	public void setEndSession(char endSession) {
		this.endSession = endSession;
	}



}
