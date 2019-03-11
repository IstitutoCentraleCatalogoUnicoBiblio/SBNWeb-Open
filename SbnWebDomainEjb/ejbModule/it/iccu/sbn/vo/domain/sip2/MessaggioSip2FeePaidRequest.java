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
 *	This message can be used to notify the ACS that a fee has been collected from the patron.
 *	The ACS should record this information in their database and respond with a
 *	Fee Paid Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2FeePaidRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
	/**
	 *
	 */
	private static final long serialVersionUID = 5297974395510926266L;
	protected String	transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String	feeType;			// 2-char, fixed-length required field (01 thru 99). identifies a fee type to apply the payment to.
	protected String 	paymentType;		// 2-char, fixed-length required field (00 thru 99)
	protected String 	currencyType;		// 3-char, fixed-length required field
	protected String 	feeAmount;			// BV variable-length required field; the amount paid.
	protected String 	institutionId;		// AO variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field.
	protected String 	terminalPassword;	// AC variable-length optional field
	protected String 	patronPassword;		// AD variable-length optional field
	protected String 	feeIdentifier;		// CG variable-length optional field; identifies a specific fee to apply the payment to.
	protected String 	transactionId;		// BK variable-length optional field; a transaction id assigned by the payment device.


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2FeePaidRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
    	this.setCodiceMessaggio(ar[i].substring(0, 2));
    	this.transactionDate = ar[i].substring(2,20); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS


    	this.feeType 			= ar[i].substring(20,22);    // 2-char, fixed-length required field (01 thru 99). identifies a fee type to apply the payment to.
    	this.paymentType 		= ar[i].substring(22,24);    // 2-char, fixed-length required field (00 thru 99)
    	this.currencyType		= ar[i].substring(24,27);    // 3-char, fixed-length required field
    	this.feeAmount 			= ar[i++].substring(27+2);  // BV variable-length required field; the amount paid.
    	this.institutionId 		= ar[i++].substring(2);	  // AO variable-length required field
    	this.patronIdentifier 	= ar[i++].substring(2);   // AA variable-length required field.

    	// Cerchiamo i campi opzionali
    	for (; ; i++)
    	{
    		if (ar[i].startsWith(MessaggioSip2.SIP2F_TERMINAL_PASSWORD))
    	    	this.terminalPassword = ar[i].substring(2);	  // AC variable-length optional field
    		else if (ar[i].startsWith(MessaggioSip2.SIP2F_PATRON_PASSWORD))
       	        this.patronPassword	= ar[i].substring(2);   // AD variable-length optional field
    		else if (ar[i].startsWith(MessaggioSip2.SIP2F_FEE_IDENTIFIER))
    	        this.feeIdentifier = ar[i].substring(2);   // CG variable-length optional field; identifies a specific fee to apply the payment to.
    		else if (ar[i].startsWith(MessaggioSip2.SIP2F_TRANSACTION_ID))
    	    	this.transactionId = ar[i].substring(2);   // BK variable-length optional field; a transaction id assigned by the payment device.
    		else
    			break;
    	}

//        if (ar[i].length() > 10) // Error detection
//        {
//        	setSequenceNumber(ar[i].charAt(12));
//        	setChecksum(ar[i].substring(15));
//        	setErrorDetection(true);
//        }
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
    public MessaggioSip2FeePaidRequest() {
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
       	stringaMessaggio += this.feeType;
       	stringaMessaggio += this.paymentType;
       	stringaMessaggio += this.currencyType;
       	stringaMessaggio += this.feeAmount;
       	stringaMessaggio += this.institutionId;
       	stringaMessaggio += this.patronIdentifier;
       	stringaMessaggio += this.terminalPassword;
       	stringaMessaggio += this.patronPassword;
       	stringaMessaggio += this.feeIdentifier;
       	stringaMessaggio += this.transactionId;

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


    }
