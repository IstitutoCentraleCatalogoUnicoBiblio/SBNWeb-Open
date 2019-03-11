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
 * This message can be used to send item information to the ACS,
 * without having to do a Checkout or Checkin operation.
 * The item properties could be stored on the ACS’s database.
 * The ACS should respond with an Item Status Update Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2ItemStatusUpdateRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
    /**
	 *
	 */
	private static final long serialVersionUID = -3942345075564671025L;
	protected String transactionDate;		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String institutionId;			// AO variable-length required field
    protected String itemIdentifier;		// AB variable-length required field
    protected String terminalPassword;		// AC variable-length optional field
	protected String itemProperties;		// CH variable-length required field

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2ItemStatusUpdateRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.transactionDate = ar[i].substring(2,20); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.institutionId = ar[i++].substring(20+2);	// AO variable-length required field
        this.itemIdentifier=ar[i++].substring(2);		// AB variable-length required field

    	// Cerchiamo i campi opzionali
    	for (; ; i++)
    	{
            if (ar[i].startsWith(MessaggioSip2.SIP2F_TERMINAL_PASSWORD))
            	this.terminalPassword=ar[i++].substring(2);		// AC variable-length optional field
          else if (ar[i].startsWith(MessaggioSip2.SIP2F_ITEM_PROPERTIES))
       	        this.itemProperties	= ar[i].substring(2);   	// CH variable-length required field
    		else
    			break;
    	}
//        if (ar[i].length() > 10) // Erro detection
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

    public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}


	public String getItemIdentifier() {
		return itemIdentifier;
	}

	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}


	public String getTerminalPassword() {
		return terminalPassword;
	}

	public void setTerminalPassword(String terminalPassword) {
		this.terminalPassword = terminalPassword;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}


    /**
     * Costruttore
     */
    public MessaggioSip2ItemStatusUpdateRequest() {
    }




}
