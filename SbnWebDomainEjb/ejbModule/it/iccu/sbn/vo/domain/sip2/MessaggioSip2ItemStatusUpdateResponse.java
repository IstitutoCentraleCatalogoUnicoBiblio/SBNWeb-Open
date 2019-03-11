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
public class MessaggioSip2ItemStatusUpdateResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = 2796971558693271002L;
	protected char 		itemPropertiesOk;	// 1-char, fixed-length required field:  0 or 1.
	protected String 	transactionDate; 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	itemIdentifier;		// AB variable-length required field
	protected String 	titleIdentifier;	// AJ variable-length optional field
	protected String 	itemProperties;		// CH variable-length optional field


	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2ItemStatusUpdateRequest richiesta
     */
    public MessaggioSip2ItemStatusUpdateResponse(boolean enableErrorDetection, MessaggioSip2ItemStatusUpdateRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_ITEM_STATUS_UPDATE_RESPONSE);
    	setTitleIdentifier("");
    	setItemProperties("");
    }

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2ItemStatusUpdateResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_ITEM_STATUS_UPDATE_RESPONSE);
    }

    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.itemPropertiesOk != '0' && this.itemPropertiesOk != '1')
			throw new Sip2ValidationException("Sip2 protocol: Item Status Update Response - campi obbligatori non validi [Item Properties Ok]");
		if (this.transactionDate == null)
			throw new Sip2ValidationException("Sip2 protocol: Item Status Update Response - campi obbligatori non validi [Transaction Date]");
		if (this.itemIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Item Status Update Response - campi obbligatori non validi [Item Identifier]");
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
        stringaMessaggio += this.itemPropertiesOk;
        stringaMessaggio += this.transactionDate;
        stringaMessaggio += this.itemIdentifier;
        stringaMessaggio += this.titleIdentifier;
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


	public String getTitleIdentifier() {
		return titleIdentifier;
	}


	public void setTitleIdentifier(String titleIdentifier) {
		this.titleIdentifier = MessaggioSip2.SIP2F_TITLE_IDENTIFIER + titleIdentifier + "|";
	}
	public char getItemPropertiesOk() {
		return itemPropertiesOk;
	}
	public void setItemPropertiesOk(char itemPropertiesOk) {
		this.itemPropertiesOk = itemPropertiesOk;
	}




}
