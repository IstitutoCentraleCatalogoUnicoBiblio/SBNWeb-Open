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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.MiscString;

/**
 * This message is used by the SC to request to check in an item,
 * and also to cancel a Checkout request that did not successfully complete.
 * The ACS must respond to this command with a Checkin Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2CheckinRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
	/**
	 *
	 */
	private static final long serialVersionUID = -5016616908168797450L;
	protected String 	noBlock;			// 1-char, fixed-length required field:  Y or N.
	protected String 	transactionDate;	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS.  The date and time that the patron checked out the item at the SC unit.
	protected String 	returnDate;			// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	currentLocation;	// AP variable-length required field
	protected String 	institutionId;		// AO variable-length required field
	protected String 	itemIdentifier;		// AB variable-length required field
	protected String 	terminalPassword;	// AC variable-length required field
	protected String 	itemProperties;		// CH variable-length optional field
	protected String 	cancel;				// BI 1-char, optional field: Y or N


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2CheckinRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.noBlock = ar[i].substring(2, 3);			// 1-char, fixed-length required field:  Y or N.
        this.transactionDate = ar[i].substring(3,21); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.returnDate = ar[i].substring(21,39);		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
//        this.currentLocation = ar[i++].substring(39+2);	// AP variable-length required field
        this.currentLocation = ValidazioneDati.substring(ar[i++], 39+2);	// AP variable-length required field
        this.institutionId = ar[i++].substring(2);		// AO variable-length required field
        this.itemIdentifier=ar[i++].substring(2);		// AB variable-length required field
        this.terminalPassword=ar[i++].substring(2);		// AC variable-length required field

    	// Cerchiamo i campi opzionali
		for (; ; i++)
		{
			if (ar[i].startsWith(MessaggioSip2.SIP2F_ITEM_PROPERTIES))
		    	this.itemProperties = ar[i].substring(2);// CH variable-length optional field
			else if (ar[i].startsWith(MessaggioSip2.SIP2F_CANCEL))
			        this.cancel	= ar[i].substring(2);	// BI 1-char, optional field: Y or N
			else
				break;
		}

//        if (ar[i].length() > 10) // Error detection
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
    public MessaggioSip2CheckinRequest() {
    }

    public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = MessaggioSip2.SIP2F_CANCEL + cancel;
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


	public String getNoBlock() {
		return noBlock;
	}

	public void setNoBlock(String noBlock) {
		this.noBlock = noBlock;
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
       	stringaMessaggio += this.noBlock;
       	stringaMessaggio += this.transactionDate;
       	stringaMessaggio += this.returnDate;
       	stringaMessaggio += this.currentLocation;
        stringaMessaggio += this.institutionId;
       	stringaMessaggio += this.itemIdentifier;
       	stringaMessaggio += this.terminalPassword;
       	stringaMessaggio += this.itemProperties;
       	stringaMessaggio += this.cancel;

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

    public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = SIP2F_CURRENT_LOCATION + currentLocation + "|";
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	
	public static void main(String[] args) throws Exception {
		String test= "09N20180214    10213320180214    102133AP 07|AO 37|ABGES    21196|AC|AY3AZEFD9";
		MessaggioSip2CheckinRequest msg = new MessaggioSip2CheckinRequest(test);
		System.out.println(msg);
	}

}
