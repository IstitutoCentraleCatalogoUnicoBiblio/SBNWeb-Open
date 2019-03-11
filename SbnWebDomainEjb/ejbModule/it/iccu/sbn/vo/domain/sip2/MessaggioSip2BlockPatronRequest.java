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
import it.iccu.sbn.util.MiscString;

/**
 * This message requests that the patron card be blocked by the ACS.
 * This is, for example, sent when the patron is detected tampering with the SC
 * or when a patron forgets to take their card.  The ACS should invalidate the
 * patron’s card and respond with a Patron Status Response message.
 * The ACS could also notify the library staff that the card has been blocked.
 *
 * @author almaviva7
 */
public class MessaggioSip2BlockPatronRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = 751388531459439293L;
	protected char    	cardRetained;			// 1-char, fixed-length required field:  Y or N.
	protected String	transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	institutionId;		// AO variable-length required field
	protected String    blockedCardMsg;		// AL variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field.
	protected String 	terminalPassword;	// AC variable-length required field


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2BlockPatronRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.transactionDate 	= ar[i].substring(2,20); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.institutionId 		= ar[i++].substring(20+2);	// AO variable-length required field
        this.blockedCardMsg 	= ar[i++].substring(2);		// AL variable-length required field
        this.patronIdentifier 	= ar[i++].substring(2);		// AA variable-length required field.
        this.terminalPassword 	= ar[i++].substring(2);		// AC variable-length required field
//        if (ar[i].length() > 10) // Erro detection
//        {
//        	setSequenceNumber(ar[i].charAt(12));
//        	setChecksum(ar[i].substring(15));
//        	setErrorDetection(true);
//        }
        if (ar[i] != null && ar[i].length() > 0)
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(5));
        	setErrorDetection(true);
        }
    }

    /**
     * Costruttore
     */
    public MessaggioSip2BlockPatronRequest() {
    }

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = MessaggioSip2.SIP2F_INSTITUTION_ID + institutionId + "|";
	}

	public String getTerminalPassword() {
		return terminalPassword;
	}

	public void setTerminalPassword(String terminalPassword) {
		this.terminalPassword = MessaggioSip2.SIP2F_TERMINAL_PASSWORD + terminalPassword + "|";
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}


    @Override
	public String toString() {
        String stringaMessaggio = "";

        stringaMessaggio += this.getCodiceMessaggio().getCode();
       	stringaMessaggio += this.transactionDate;
        stringaMessaggio += this.institutionId;
        stringaMessaggio += this.patronIdentifier;
        stringaMessaggio += this.terminalPassword;

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

	public String getBlockedCardMsg() {
		return blockedCardMsg;
	}

	public void setBlockedCardMsg(String blockedCardMsg) {
		this.blockedCardMsg = MessaggioSip2.SIP2F_BLOCKED_CARD_MSG + blockedCardMsg + "|";
	}

	public char getCardRetained() {
		return cardRetained;
	}

	public void setCardRetained(char cardRetained) {
		this.cardRetained = cardRetained;
	}

	public String getPatronIdentifier() {
		return patronIdentifier;
	}

	public void setPatronIdentifier(String patronIdentifier) {
		this.patronIdentifier = MessaggioSip2.SIP2F_PATRON_IDENTIFIER + patronIdentifier + "|";
	}

    }
