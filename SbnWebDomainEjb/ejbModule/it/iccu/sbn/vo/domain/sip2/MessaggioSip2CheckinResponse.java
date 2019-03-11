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
 * This message must be sent by the ACS in response to a SC Checkin message.
 * Message must be sent by the ACS in response to a SC Checkin message
 *
 * @author almaviva7
 */
public class MessaggioSip2CheckinResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = 587963772881806670L;
	protected char 		ok;					// 1-char, fixed-length required field:  0 or 1.
	protected char 		resensitize;		// 1-char, fixed-length required field:  Y or N.
	protected char 		magneticMedia;		// 1-char, fixed-length required field:  Y or N or U.
	protected char 		alert;				// 1-char, fixed-length required field:  Y or N.
	protected String 	transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	institutionId;		// AO variable-length required field
	protected String 	itemIdentifier;		// AB variable-length required field
	protected String 	permanentLocation;	// AQ variable-length required field
	protected String 	titleIdentifier;	// AJ variable-length required field
	protected String 	sortBin;			// CL variable-length optional field
	protected String 	patronIdentifier;	// AA variable-length required field
	protected String 	mediaType;			// CK 3-char, fixed-length optional field
	protected String 	itemProperties;		// CH variable-length optional field


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2CheckinResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_CHECKIN_RESPONSE);
    }
	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2CheckoutRequest richiesta
     */
    public MessaggioSip2CheckinResponse(boolean enableErrorDetection, MessaggioSip2CheckinRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_CHECKIN_RESPONSE);
    	// Campi obbligatori
    	setOk('0'); //default not ok
    	setResensitize('N'); //(Y/N)
    	setMagneticMedia('U'); //(Y/N/U)
    	setAlert('Y');
    	setTransactionDate(req.getTransactionDate());
    	setInstitutionId(req.getInstitutionId());
    	setItemIdentifier(req.getItemIdentifier());
    	setPermanentLocation("");
//    	this.titleIdentifier = "";
    	setTitleIdentifier("No title ID");
    	//2.00
//    	this.patronIdentifier = "";
    	setPatronIdentifier("No patron ID");
    	setSortBin("");
    	setMediaType("000");	// Other - default
    	setItemProperties("");
    }

    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.resensitize != 'Y' && this.resensitize != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Resensitize]");
		if (this.magneticMedia != 'Y' && this.magneticMedia != 'N' && this.magneticMedia != 'U')
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Magnetic Media]");
		if (this.alert != 'Y' && this.alert != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Alert]");
		if (this.transactionDate == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Transaction Date]");
		if (this.institutionId == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Institution Id]");
		if (this.itemIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Item Identifier]");
		if (this.permanentLocation == null)
			throw new Sip2ValidationException("Sip2 protocol: Checkin response - campi obbligatori non validi [Permanent Location]");
    	return true;
	}

    @Override
	public String toString() {
    	try {
			validateResponseRequiredFields();
		} catch (Sip2ValidationException e) {

			log.error(e);
			this.setOk('0');
		}
        String stringaMessaggio = "";

        stringaMessaggio += this.getCodiceMessaggio().getCode();
        stringaMessaggio += this.ok;
        stringaMessaggio += this.resensitize;
        stringaMessaggio += this.magneticMedia;
        stringaMessaggio += this.alert;
        stringaMessaggio += this.transactionDate;
        stringaMessaggio += this.institutionId;
        stringaMessaggio += this.itemIdentifier;
        stringaMessaggio += this.permanentLocation;
        stringaMessaggio += this.titleIdentifier;
        stringaMessaggio += this.sortBin;
        stringaMessaggio += this.patronIdentifier;
        stringaMessaggio += this.mediaType;
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
		this.mediaType = MessaggioSip2.SIP2F_MEDIA_TYPE + mediaType + "|"; // separatore in campo fisso?
	}


	public char getOk() {
		return ok;
	}


	public void setOk(char ok) {
		this.ok = ok;
	}



	public String getTitleIdentifier() {
		return titleIdentifier;
	}

	public void setTitleIdentifier(String titleIdentifier) {
		this.titleIdentifier = MessaggioSip2.SIP2F_TITLE_IDENTIFIER + titleIdentifier + "|";
	}
	public char getAlert() {
		return alert;
	}
	public void setAlert(char alert) {
		this.alert = alert;
	}
	public String getPermanentLocation() {
		return permanentLocation;
	}
	public void setPermanentLocation(String permanentLocation) {
		this.permanentLocation = MessaggioSip2.SIP2F_PERMANENT_LOCATION + permanentLocation + "|";
	}
	public char getResensitize() {
		return resensitize;
	}
	public void setResensitize(char resensitize) {
		this.resensitize = resensitize;
	}
	public String getSortBin() {
		return sortBin;
	}
	public void setSortBin(String sortBin) {
		this.sortBin = MessaggioSip2.SIP2F_SORT_BIN + sortBin + "|";
	}




}
