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
 * This message is used by the SC to request to check out an item, and also to cancel a Checkin
 * request that did not successfully complete.
 * The ACS must respond to this command with a Checkout Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2CheckoutRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
	/**
	 *
	 */
	private static final long serialVersionUID = 919200593938464765L;
	protected String	scRenewalPolicy;	// 1-char, fixed-length required field:  Y or N.
	protected String 	noBlock;			// 1-char, fixed-length required field:  Y or N.
	protected String 	transactionDate;	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS.  The date and time that the patron checked out the item at the SC unit.
	protected String 	nbDueDate;			// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	institutionId;		// AO variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field
	protected String 	itemIdentifier;		// AB variable-length required field
	protected String 	terminalPassword;	// AC variable-length required field
	protected String 	itemProperties;		// CH variable-length optional field
	protected String 	patronPassword;		// AD variable-length optional field
	protected String 	feeAcknowledged;	// BO 1-char, optional field: Y or N
	protected String 	cancel;				// BI 1-char, optional field: Y or N

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2CheckoutRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.scRenewalPolicy = ar[i].substring(2, 3);
        this.noBlock = ar[i].substring(3, 4);
        this.transactionDate = ar[i].substring(4,22); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.nbDueDate = ar[i].substring(22,40);		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.institutionId = ar[i++].substring(40+2);	// AO variable-length required field
        this.patronIdentifier=ar[i++].substring(2);		// AA variable-length required field
        this.itemIdentifier=ar[i++].substring(2);		// AB variable-length required field
        this.terminalPassword=ar[i++].substring(2);		// AC variable-length required field
    	// Cerchiamo i campi opzionali
    	for (; ; i++)
    	{
    		if (ar[i].startsWith(MessaggioSip2.SIP2F_ITEM_PROPERTIES))
    			this.itemProperties=ar[i].substring(2);	// CH variable-length optional field
    		else if (ar[i].startsWith(MessaggioSip2.SIP2F_PATRON_PASSWORD))
                this.patronPassword=ar[i].substring(2);	// AD variable-length optional field
            else if (ar[i].startsWith(MessaggioSip2.SIP2F_FEE_ACKNOWLEDGED))
    	    	this.feeAcknowledged = ar[i].substring(2);	  // BO 1-char, optional field: Y or N
    		else if (ar[i].startsWith(MessaggioSip2.SIP2F_CANCEL))
       	        this.cancel	= ar[i].substring(2);   // BI 1-char, optional field: Y or N
    		else
    			break;
    	}

        if (ar[i]!= null && ar[i].length() > 0) // Error detection
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(5));
        	setErrorDetection(true);
        }
    }

    void test(String stringa){
    	try {
            System.out.println("-- " + stringa);
            System.out.println("-----------------");
        	String ar[] = MiscString.estraiCampi(stringa, "|");
        	int i=0;
        	System.out.println("ar.length: "+ar.length);
        	System.out.print(ar[i].substring(0, 2));
        	System.out.print(ar[i].substring(2, 3));
        	System.out.print(ar[i].substring(3, 4));
        	System.out.print(ar[i].substring(4,22)); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        	System.out.print(ar[i].substring(22,40));		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        	System.out.print(ar[i++].substring(40+2));	// AO variable-length required field
        	System.out.print(ar[i++].substring(2));		// AA variable-length required field
        	System.out.print(ar[i++].substring(2));		// AB variable-length required field
        	System.out.print(ar[i++].substring(2));		// AC variable-length required field
        	System.out.println("i:"+i);
        	// Cerchiamo i campi opzionali
        	for (; ; i++)
        	{
        		if (ar[i].startsWith(MessaggioSip2.SIP2F_ITEM_PROPERTIES))
        			System.out.println(MessaggioSip2.SIP2F_ITEM_PROPERTIES + ": " + ar[i].substring(2));	// CH variable-length optional field
        		else if (ar[i].startsWith(MessaggioSip2.SIP2F_PATRON_PASSWORD))
        			System.out.println(MessaggioSip2.SIP2F_PATRON_PASSWORD + ": " + ar[i].substring(2));	// AD variable-length optional field
                else if (ar[i].startsWith(MessaggioSip2.SIP2F_FEE_ACKNOWLEDGED))
                	System.out.println(MessaggioSip2.SIP2F_FEE_ACKNOWLEDGED + ": " + ar[i].substring(2));	  // BO 1-char, optional field: Y or N
        		else if (ar[i].startsWith(MessaggioSip2.SIP2F_CANCEL))
        			System.out.println(MessaggioSip2.SIP2F_CANCEL + ": " + ar[i].substring(2));   // BI 1-char, optional field: Y or N
        		else
        			break;
        	}

            if (ar[i]!= null && ar[i].length() > 0) // Error detection
            {
            	System.out.println("AY" + ar[i].charAt(2));
            	System.out.println("AZ" + ar[i].substring(5));
            }
    	} catch (Exception e) {
    		System.out.println("-----------------------------> Eccezione: " + e.getMessage());
    	}
    }

    /**
     * Costruttore
     */
    public MessaggioSip2CheckoutRequest() {
    }

    public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = MessaggioSip2.SIP2F_CANCEL + cancel;
	}

	public String getFeeAcknowledged() {
		return feeAcknowledged;
	}

	public void setFeeAcknowledged(String feeAcknowledged) {
		this.feeAcknowledged = MessaggioSip2.SIP2F_FEE_ACKNOWLEDGED + feeAcknowledged;
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

	public String getNbDueDate() {
		return nbDueDate;
	}

	public void setNbDueDate(String nbDueDate) {
		this.nbDueDate = nbDueDate;
	}

	public String getNoBlock() {
		return noBlock;
	}

	public void setNoBlock(String noBlock) {
		this.noBlock = noBlock;
	}

	public String getScRenewalPolicy() {
		return scRenewalPolicy;
	}

	public void setScRenewalPolicy(String scRenewalPolicy) {
		this.scRenewalPolicy = scRenewalPolicy;
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

	public String getPatronPassword() {
		return patronPassword;
	}

	public void setPatronPassword(String patronPassword) {
		this.patronPassword = MessaggioSip2.SIP2F_PATRON_PASSWORD + patronPassword + "|";
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
       	stringaMessaggio += this.scRenewalPolicy;
       	stringaMessaggio += this.noBlock;
       	stringaMessaggio += this.transactionDate;
       	stringaMessaggio += this.nbDueDate;
        stringaMessaggio += this.institutionId;
       	stringaMessaggio += this.patronIdentifier;
       	stringaMessaggio += this.itemIdentifier;
       	stringaMessaggio += this.terminalPassword;
       	stringaMessaggio += this.itemProperties;
       	stringaMessaggio += this.patronPassword;
       	stringaMessaggio += this.feeAcknowledged;
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
}
