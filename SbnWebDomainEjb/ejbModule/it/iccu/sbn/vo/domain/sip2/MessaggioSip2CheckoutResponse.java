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
 * This message must be sent by the ACS in response to a Checkout message from the SC.
 *
 * @author almaviva7
 */
public class MessaggioSip2CheckoutResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = 1517695909747129474L;
	protected char 	ok;					// 1-char, fixed-length required field:  0 or 1.
	protected char 	renewalOk;			// 1-char, fixed-length required field:  Y or N.
	protected char 	magneticMedia;		// 1-char, fixed-length required field:  Y or N or U.
	protected char 	desensitize;		// 1-char, fixed-length required field:  Y or N or U.
	protected String 	transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	institutionId;		// AO variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field
	protected String 	itemIdentifier;		// AB variable-length required field
	protected String 	titleIdentifier;	// AJ variable-length required field
	protected String 	dueDate;			// AH variable-length required field
	protected String 	feeType;			// BT 2-char, fixed-length optional field (01 thru 99).  The type of fee associated with 						checking out this item.
	protected String 	securityInhibit;	// CI 1-char, fixed-length optional field:  Y or N.
	protected String 	currencyType;		// BH 3-char fixed-length optional field
	protected String 	feeAmount;			// BV variable-length optional field.  The amount of the fee associated with checking 						out this item.
	protected String 	mediaType;			// CK 3-char, fixed-length optional field
	protected String 	itemProperties;		// CH variable-length optional field
	protected String 	transactionId;		// BK variable-length optional field.  May be assigned by the ACS when checking out 						the item involves a fee.


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2CheckoutResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_CHECKOUT_RESPONSE);

    }
	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2CheckoutRequest richiesta
     */
    public MessaggioSip2CheckoutResponse(boolean enableErrorDetection, MessaggioSip2CheckoutRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_CHECKOUT_RESPONSE);
    	// Campi obbligatori
    	setOk('0'); //default not ok
    	setTitleIdentifier("Untitled"); //default
    	setRenewalOk('N'); //rinnovo richiesta (Y/N)
    	setMagneticMedia('U'); //(Y/N/U)
    	setDesensitize('N'); //(Y/N/U)
    	setTransactionDate(req.getTransactionDate());
    	setInstitutionId(req.getInstitutionId());
    	setPatronIdentifier(req.getPatronIdentifier());
    	setItemIdentifier(req.getItemIdentifier()); //codice a barre dell'articolo (inventario)
    	setDueDate(req.getNbDueDate());
    	setFeeType("01");		// Other - default
    	setSecurityInhibit("");
    	setCurrencyType("ITL");	// Italian Lira - default
    	setFeeAmount("");
    	setMediaType("000");	// Other - default
    	setItemProperties("");
    	setTransactionId("");
    }

    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
/*		if (this.ok != '0' || this.ok != '1')
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi");*/
		if (this.renewalOk != 'Y' && this.renewalOk != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Renewal Ok]");
		if (this.magneticMedia != 'Y' && this.magneticMedia != 'N' && this.magneticMedia != 'U')
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Magnetic media]");
		if (this.desensitize != 'Y' && this.desensitize != 'N' && this.desensitize != 'U')
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Desensitize]");
		if (this.transactionDate == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Transaction Date]");
		if (this.institutionId == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Institution Id]");
		if (this.patronIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Patron Identifier]");
		if (this.itemIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Item Identifier]");
		if (this.titleIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Title Identifier]");
		if (this.dueDate == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkout Response - campi obbligatori non validi [Due Date]");
    	return true;
	}

    @Override
	public String toString() {
    	try {
			validateResponseRequiredFields();
		} catch (Sip2ValidationException e) {

			log.error(e);
			this.setOk('0');
			this.setDesensitize('N');
		}
        String stringaMessaggio = "";
        stringaMessaggio += this.getCodiceMessaggio().getCode();
        stringaMessaggio += this.ok;
        stringaMessaggio += this.renewalOk;
        stringaMessaggio += this.magneticMedia;
        stringaMessaggio += this.desensitize;
        stringaMessaggio += this.transactionDate;
        stringaMessaggio += this.institutionId;
        stringaMessaggio += this.patronIdentifier;
        stringaMessaggio += this.itemIdentifier;
        stringaMessaggio += this.titleIdentifier;
        stringaMessaggio += this.dueDate;
        stringaMessaggio += this.feeType;
        stringaMessaggio += this.securityInhibit;
        stringaMessaggio += this.currencyType;
        stringaMessaggio += this.feeAmount;
        stringaMessaggio += this.mediaType;
        stringaMessaggio += this.itemProperties;
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

	public String getCurrencyType() {
		return currencyType;
	}



	public void setCurrencyType(String currencyType) {
		this.currencyType = MessaggioSip2.SIP2F_CURRENCY_TYPE + currencyType + "|"; // Separatore con campo fisso??;
	}



	public String getFeeAmount() {
		return feeAmount;
	}



	public void setFeeAmount(String feeAmount) {
		this.feeAmount = MessaggioSip2.SIP2F_FEE_AMOUNT + feeAmount + "|";
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


	public char getDesensitize() {
		return desensitize;
	}


	public void setDesensitize(char desensitize) {
		this.desensitize = desensitize;
	}


	public String getDueDate() {
		return dueDate;
	}


	public void setDueDate(String dueDate) {
		this.dueDate = MessaggioSip2.SIP2F_DUE_DATE + dueDate + "|";
	}


	public String getFeeType() {
		return feeType;
	}


	public void setFeeType(String feeType) {
		this.feeType = MessaggioSip2.SIP2F_FEE_TYPE + feeType + "|"; // Separatore con campo fisso??
	}


	public String getItemIdentifier() {
		return itemIdentifier;
	}


	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = MessaggioSip2.SIP2F_ITEM_IDENTIFIER + itemIdentifier + "|";
	}


	public String getItemProperties() {
		return itemProperties;
	}


	public void setItemProperties(String itemProperties) {
		this.itemProperties = MessaggioSip2.SIP2F_ITEM_PROPERTIES + itemProperties + "|";
	}


	public char getMagneticMedia() {
		return magneticMedia;
	}


	public void setMagneticMedia(char magneticMedia) {
		this.magneticMedia = magneticMedia;
	}


	public String getMediaType() {
		return mediaType;
	}


	public void setMediaType(String mediaType) {
		this.mediaType = MessaggioSip2.SIP2F_MEDIA_TYPE + mediaType + "|"; // Separatore con campo fisso??
	}


	public char getOk() {
		return ok;
	}


	public void setOk(char ok) {
		this.ok = ok;
	}




	public char getRenewalOk() {
		return renewalOk;
	}


	public void setRenewalOk(char renewalOk) {
		this.renewalOk = renewalOk;
	}

	public String getSecurityInhibit() {
		return securityInhibit;
	}


	public void setSecurityInhibit(String securityInhibit) {
		this.securityInhibit = MessaggioSip2.SIP2F_SECURITY_INHIBIT + securityInhibit + "|"; // Separatore con campo fisso??
	}


	public String getTitleIdentifier() {
		return titleIdentifier;
	}


	public void setTitleIdentifier(String titleIdentifier) {
		this.titleIdentifier = MessaggioSip2.SIP2F_TITLE_IDENTIFIER + titleIdentifier + "|";
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = MessaggioSip2.SIP2F_TRANSACTION_ID + transactionId + "|";
	}



}
