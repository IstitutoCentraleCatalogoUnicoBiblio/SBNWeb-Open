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
 * This message can be used by the SC to re-enable canceled patrons.
 * It should only be used for system testing and validation.
 * The ACS should respond with a Patron Enable Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2EnablePatronRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {


	/**
	 *
	 */
	private static final long serialVersionUID = 4391598808733260394L;
	protected String    transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	institutionId;		// AO variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field
	protected String 	terminalPassword;	// AC variable-length optional field
	protected String 	patronPassword;		// AD variable-length optional field

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2EnablePatronRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.transactionDate 	= ar[i].substring(2,20); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.institutionId 		= ar[i++].substring(20+2);	// AO variable-length required field
        this.patronIdentifier 	= ar[i++].substring(2);		// AA variable-length required field.
    	// Cerchiamo i campi opzionali
		for (; ; i++)
		{
			if (ar[i].startsWith(MessaggioSip2.SIP2F_TERMINAL_PASSWORD))
		    	this.terminalPassword = ar[i].substring(2);	 // AC variable-length optional field
			else if (ar[i].startsWith(MessaggioSip2.SIP2F_PATRON_PASSWORD))
			        this.patronPassword	= ar[i].substring(2);// AD variable-length optional field
			else
				break;
		}
//		if (ar[i].length() > 10) // Error detection
//	        {
//        	setSequenceNumber(ar[i].charAt(12));
//        	setChecksum(ar[i].substring(15));
//        	setErrorDetection(true);
//	        }
        if (ar[i]!= null && ar[i].length() > 0) // Error detection
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(5));
        	setErrorDetection(true);
        }
    }

    /**
     * Costruttore
     */
    public MessaggioSip2EnablePatronRequest() {
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


	public String getPatronIdentifier() {
		return patronIdentifier;
	}

	public void setPatronIdentifier(String patronIdentifier) {
		this.patronIdentifier = MessaggioSip2.SIP2F_PATRON_IDENTIFIER + patronIdentifier + "|";
	}

    }
