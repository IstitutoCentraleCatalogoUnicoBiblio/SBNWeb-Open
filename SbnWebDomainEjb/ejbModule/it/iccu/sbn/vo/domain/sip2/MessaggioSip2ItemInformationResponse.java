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
 * The ACS must send this message in response to the Item Information message.
 *
 * @author almaviva7
 */
public class MessaggioSip2ItemInformationResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
/**
	 *
	 */
	private static final long serialVersionUID = 7136044107239392042L;
	/*
	- Circulation status / char2 (00-99)
	1	other
	2	on order
	3 	available
	4	charged
	5	charged; not to be recalled until earliest recall date
	6	in process
	7	recalled
	8	waiting on hold shelf
	9	waiting to be shelved
	10	in transit between library locations
	11	claimed returned
	12	lost
	13	missing
*/
	protected String	circulationStatus;	// 2-char, fixed-length required field (00 thru 99)
/*
	- Security marker / char2 (00-99)
	00	other
	01	none
	02	3M tattle-tape security strip
	03	3M whisper tape
*/
	protected String 	securityMarker;		// 2-char, fixed-length required field (00 thru 99)
/*
	- Fee type / char2 (01-99)
	01	other/unknown
	02	administrative
	03	damage
	04	overdue
	05	processing
	06	rental
	07	replacement
	08	computer access charge
	09	hold fee
*/
	protected String 	feeType;			// 2-char, fixed-length required field (01 thru 99).  The type of fee associated with 						checking out this item.
	protected String 	transactionDate;	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	holdQueueLength;	// CF variable-length optional field
	protected String 	dueDate;			// AH variable-length optional field.
	protected String 	recallDate;			// CJ 18-char, fixed-length optional field:  YYYYMMDDZZZZHHMMSS
	protected String 	holdPickupDate;		// CM 18-char, fixed-length optional field:  YYYYMMDDZZZZHHMMSS
	protected String 	itemIdentifier;		// AB variable-length required field
	protected String 	titleIdentifier;	// AJ variable-length required field
	protected String 	owner;				// BG variable-length optional field
	protected String 	currencyType;		// BH 3 char, fixed-length optional field
	protected String 	feeAmount;			// BV variable-length optional field.  The amount of the fee associated with this item.
	protected String 	mediaType;			// CK 3-char, fixed-length optional field
	protected String 	permanentLocation; 	// AQ variable-length optional field
	protected String 	currentLocation; 	// AP variable-length optional field
	protected String 	itemProperties;		// CH variable-length optional field

	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2PatronStatusRequest richiesta
     */
    public MessaggioSip2ItemInformationResponse(boolean enableErrorDetection, MessaggioSip2ItemInformationRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_ITEM_INFORMATION_RESPONSE);
    	// Campi obbligatori
    	setTransactionDate(req.getSIP2Date(null));	// YYYYMMDDZZZZHHMMSS
    	// default
    	setSecurityMarker("02");	// 3M tattle-tape security strip
    	setFeeType("01");			// other/unknown
    	setCirculationStatus("01");	// other (errore - default)
		setHoldQueueLength("");
		setDueDate("");
		setRecallDate("");		//CJ
		setHoldPickupDate("");	//CM
		setOwner("");			//BG
		setCurrencyType("ITL");	//Italian Lira - default
		setFeeAmount("");
		setMediaType("000");	// Other - default
		setPermanentLocation("");
		setCurrentLocation("");
		setItemProperties("");
    }

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2ItemInformationResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_ITEM_INFORMATION_RESPONSE);
    }


    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.circulationStatus == null )
			throw new Sip2ValidationException("Sip2 protocol: Item Information Response - campi obbligatori non validi [Circulation Status]");
		if (this.securityMarker == null )
			throw new Sip2ValidationException("Sip2 protocol: Item Information Response - campi obbligatori non validi [Security Marker]");
		if (this.feeType == null )
			throw new Sip2ValidationException("Sip2 protocol: Item Information Response - campi obbligatori non validi [Fee Type]");
		if (this.transactionDate == null )
			throw new Sip2ValidationException("Sip2 protocol: Item Information Response - campi obbligatori non validi [Transaction Date]");
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
		stringaMessaggio += this.circulationStatus;
		stringaMessaggio += this.securityMarker;
		stringaMessaggio += this.feeType;
		stringaMessaggio += this.transactionDate;
		stringaMessaggio += this.holdQueueLength;
		stringaMessaggio += this.dueDate;
		stringaMessaggio += this.recallDate;
		stringaMessaggio += this.holdPickupDate;
		stringaMessaggio += this.itemIdentifier;
		stringaMessaggio += this.titleIdentifier;
		stringaMessaggio += this.owner;
		stringaMessaggio += this.currencyType;
		stringaMessaggio += this.feeAmount;
		stringaMessaggio += this.mediaType;
		stringaMessaggio += this.permanentLocation;
		stringaMessaggio += this.currentLocation;
		stringaMessaggio += this.itemProperties;

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
		this.currencyType = MessaggioSip2.SIP2F_CURRENCY_TYPE + currencyType + "|"; // separatore su campo fisso??
	}

	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = MessaggioSip2.SIP2F_FEE_AMOUNT + feeAmount + "|";
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}


	public String getCirculationStatus() {
		return circulationStatus;
	}


	public void setCirculationStatus(String circulationStatus) {
		this.circulationStatus = circulationStatus;
	}


	public String getCurrentLocation() {
		return currentLocation;
	}


	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = MessaggioSip2.SIP2F_CURRENT_LOCATION + currentLocation + "|";
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
		this.feeType = feeType;
	}


	public String getHoldPickupDate() {
		return holdPickupDate;
	}


	public void setHoldPickupDate(String holdPickupDate) {
		this.holdPickupDate = MessaggioSip2.SIP2F_HOLD_PICKUP_DATE + holdPickupDate + "|"; // separatore su campo fisso??
	}


	public String getHoldQueueLength() {
		return holdQueueLength;
	}


	public void setHoldQueueLength(String holdQueueLength) {
		this.holdQueueLength = MessaggioSip2.SIP2F_HOLD_QUEUE_LENGTH + holdQueueLength + "|";
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


	public String getMediaType() {
		return mediaType;
	}


	public void setMediaType(String mediaType) {
		this.mediaType = MessaggioSip2.SIP2F_MEDIA_TYPE + mediaType + "|";
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = MessaggioSip2.SIP2F_OWNER + owner + "|";
	}


	public String getPermanentLocation() {
		return permanentLocation;
	}


	public void setPermanentLocation(String permanentLocation) {
		this.permanentLocation = MessaggioSip2.SIP2F_PERMANENT_LOCATION + permanentLocation + "|";
	}


	public String getRecallDate() {
		return recallDate;
	}


	public void setRecallDate(String recallDate) {
		this.recallDate = MessaggioSip2.SIP2F_RECALL_DATE + recallDate + "|"; // separatore su campo fisso??
	}


	public String getSecurityMarker() {
		return securityMarker;
	}


	public void setSecurityMarker(String securityMarker) {
		this.securityMarker = securityMarker;
	}


	public String getTitleIdentifier() {
		return titleIdentifier;
	}


	public void setTitleIdentifier(String titleIdentifier) {
		this.titleIdentifier = MessaggioSip2.SIP2F_TITLE_IDENTIFIER + titleIdentifier + "|";
	}


}
