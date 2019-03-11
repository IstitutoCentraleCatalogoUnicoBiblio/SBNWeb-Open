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

import java.util.ArrayList;
import java.util.List;

//import javax.naming.*;

/**
 * 	The ACS must send this message in response to the Patron Information message.
 * @author Finsiel
 */
public class MessaggioSip2PatronInformationResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = -2124087300131390931L;
	protected String 	patronStatus; 		// 14-char, fixed-length required field
	protected String 	language;			// 3-char, fixed-length required field
	protected String 	transactionDate;	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String 	holdItemsCount;		// 4-char, fixed-length required field
	protected String 	overdueItemsCount;	// 4-char, fixed-length required field
	protected String 	chargedItemsCount;	// 4-char, fixed-length required field
	protected String 	fineItemsCount;		// 4-char, fixed-length required field
	protected String 	recallItemsCount;	// 4-char, fixed-length required field
	protected String 	unavailableHoldsCount;  // 4-char, fixed-length required field
	protected String 	institutionId;		// AO variable-length required field
	protected String 	patronIdentifier;	// AA variable-length required field
	protected String 	personalName;		// AE variable-length required field
	protected String 	holdItemsLimit;		// BZ 4-char, fixed-length optional field
	protected String 	overdueItemsLimit;	// CA 4-char, fixed-length optional field
	protected String 	chargedItemsLimit;	// CB 4-char, fixed-length optional field
	protected String 	validPatron;		// BL 1-char, optional field: Y or N
	protected String 	validPatronPassword;// CQ 1-char, optional field: Y or N
	protected String 	currencyType; 		// BH 3-char, fixed-length optional field
	protected String 	feeAmount; 			// BV variable-length optional field.  The amount of fees owed by this patron.
	protected String 	feeLimit; 			// CC variable-length optional field.  The fee limit amount.
	// item: zero or more instances of one of the following, based on “summary” field of the Patron Information message:
	protected List<String>  holdItemsVect;			// AS variable-length optional field  (this field should be sent for each hold item).
	protected List<String> 	overdueItemsVect;		// AT variable-length optional field  (this field should be sent for each overdue item).
	protected List<String> 	chargedItemsVect;		// AU variable-length optional field  (this field should be sent for each charged item).
	protected List<String> 	fineItemsVect;			// AV variable-length optional field  (this field should be sent for each fine item).
	protected List<String> 	recallItemsVect;		// BU variable-length optional field  (this field should be sent for each recall item).
	protected List<String> 	unavailableHoldItemsVect; // CD variable-length optional field  (this field should be sent for each unavailable 							hold item).

	protected String 	homeAddress;			// BD variable-length optional field
	protected String 	eMailAddress;		// BE variable-length optional field
	protected String 	homePhoneNumber;		// BF variable-length optional field


	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2PatronInformationResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);

    	patronStatus = "";
    	language = "";
    	transactionDate = "";
    	holdItemsCount = "";
    	overdueItemsCount = "";
    	chargedItemsCount = "";
    	fineItemsCount = "";
    	recallItemsCount = "";
    	unavailableHoldsCount = "";
    	institutionId = "";
    	patronIdentifier = "";
    	personalName = "";
    	holdItemsLimit = "";
    	overdueItemsLimit = "";
    	chargedItemsLimit = "";
    	validPatron = "";
    	validPatronPassword = "";
    	currencyType = "";
    	feeAmount = "";
    	feeLimit = "";
    	homeAddress = "";
    	eMailAddress = "";
    	homePhoneNumber = "";

    	holdItemsVect = new ArrayList<String>();
    	overdueItemsVect = new ArrayList<String>();
    	chargedItemsVect = new ArrayList<String>();
    	fineItemsVect = new ArrayList<String>();
    	recallItemsVect = new ArrayList<String>();
    	unavailableHoldItemsVect = new ArrayList<String>();
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_PATRON_INFORMATION_RESPONSE);
    }

	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2PatronInformationRequest richiesta
     */
    public MessaggioSip2PatronInformationResponse(boolean enableErrorDetection, MessaggioSip2PatronInformationRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	// Campi obbligatori
    	setLanguage(req.getLanguage());
    	setTransactionDate(req.getTransactionDate());
    	//setSummary... //2.0 field
    	setInstitutionId(req.getInstitutionId());
    	setPatronIdentifier(req.getPatronIdentifier());

    	// Impostato dopo i controlli
//    	setPatronStatus("");

    	setHoldItemsCount("0000");			// Nr. oggetti tenuti/presi / prestiti in corso?
    	setOverdueItemsCount("0000");		// Nr. oggetti arretrati / prestiti scaduti
    	setChargedItemsCount("0000");		// Nr. oggetti pagati / o addebitati?
    	setFineItemsCount("0000");			// Nr. oggetti buoni/pregiati / prestiti andati a buon fine
    	setRecallItemsCount("0000");		// Nr. oggetti ricordati / prestiti sollecitati?
    	setUnavailableHoldsCount("0000");	// Nr. oggetti tenuti/presi non disponibili
    	setPersonalName("Name");			// Nome utente
    	setHoldItemsLimit("0000");			// default
    	setOverdueItemsLimit("0000");		// default
    	setChargedItemsLimit("0000");		// default
    	setValidPatron("N");				// default
    	setValidPatronPassword("N");		// default
    	setCurrencyType("ITL");				// Italian Lira - default
    	setFeeAmount("");
    	setFeeLimit("");
    	setHomeAddress("");
    	setEMailAddress("");
    	setHomePhoneNumber("");

    	holdItemsVect = new ArrayList<String>();
    	overdueItemsVect = new ArrayList<String>();
    	chargedItemsVect = new ArrayList<String>();
    	fineItemsVect = new ArrayList<String>();
    	recallItemsVect = new ArrayList<String>();
    	unavailableHoldItemsVect = new ArrayList<String>();
    	setCodiceMessaggio(Sip2MessageType.SIP2_ACS_PATRON_INFORMATION_RESPONSE);
    }

    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.patronStatus == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Patron status]");
		if (this.language == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Language]");
		if (this.transactionDate == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Transaction Date]");
/*		//2.00
		if (this.holdItemsCount == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Hold Items Count]");
		if (this.overdueItemsCount == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Overdue Items Count]");
		if (this.chargedItemsCount == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Charged Items Count]");
		if (this.fineItemsCount == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Fine Items Count]");
		if (this.recallItemsCount == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Recall Items Count]");
		if (this.unavailableHoldsCount == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Unavailable Holds Count]");*/
		if (this.institutionId == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Institution Id]");
		if (this.patronIdentifier == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Patron Identifier]");
		if (this.personalName == null)
			throw new Sip2ValidationException("Sip2 protocol: Patron Information response - campi obbligatori non validi [Personal Name]");
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
        stringaMessaggio += this.patronStatus;
       	stringaMessaggio += this.language;
       	stringaMessaggio += this.transactionDate;
       	stringaMessaggio += this.holdItemsCount;
       	stringaMessaggio += this.overdueItemsCount;
       	stringaMessaggio += this.chargedItemsCount;
       	stringaMessaggio += this.fineItemsCount;
       	stringaMessaggio += this.recallItemsCount;
       	stringaMessaggio += this.unavailableHoldsCount;
        stringaMessaggio += this.institutionId;
       	stringaMessaggio += this.patronIdentifier;
        stringaMessaggio += this.personalName;
       	stringaMessaggio += this.holdItemsLimit;
       	stringaMessaggio += this.overdueItemsLimit;
       	stringaMessaggio += this.chargedItemsLimit;
        stringaMessaggio += this.validPatron;
        stringaMessaggio += this.validPatronPassword;
        stringaMessaggio += this.currencyType;
        stringaMessaggio += this.feeAmount;
//        if (this.feeAmount.length()>0)
//        	stringaMessaggio += "|"; // variable length closure
       	stringaMessaggio += this.feeLimit;
//        if (this.feeLimit.length()>0)
//        	stringaMessaggio += "|"; // variable length closure

    	// item: zero or more instances of one of the following, based on “summary” field of the Patron Information message:
		for (int i=0; i < this.holdItemsVect.size(); i++)
			stringaMessaggio += this.holdItemsVect.get(i); // +"|"
		for (int i=0; i < this.overdueItemsVect.size(); i++)
			stringaMessaggio += this.overdueItemsVect.get(i); // +"|"
		for (int i=0; i < this.chargedItemsVect.size(); i++)
			stringaMessaggio += this.chargedItemsVect.get(i); // +"|"
		for (int i=0; i < this.fineItemsVect.size(); i++)
			stringaMessaggio += this.fineItemsVect.get(i); // +"|"
		for (int i=0; i < this.recallItemsVect.size(); i++)
			stringaMessaggio += this.recallItemsVect.get(i); // +"|"
		for (int i=0; i < this.unavailableHoldItemsVect.size(); i++)
			stringaMessaggio += this.unavailableHoldItemsVect.get(i); // +"|"

       	stringaMessaggio += this.homeAddress;
//       	if (this.homeAddress.length() > 0)
//       		stringaMessaggio += "|";
       	stringaMessaggio += this.eMailAddress;
//       	if (this.eMailAddress.length() > 0)
//       		stringaMessaggio += "|";
       	stringaMessaggio += this.homePhoneNumber;
//       	if (this.homePhoneNumber.length() > 0)
//       		stringaMessaggio += "|";

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
		this.currencyType = MessaggioSip2.SIP2F_CURRENCY_TYPE + currencyType + "|"; // NMB: Protocollo dice campo fisso ma usa delimitatore | nei loro esempi. Il delimitatore pensavo fosse solo per i campi a lunghezza variabile.
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

	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPatronIdentifier() {
		return patronIdentifier;
	}
	public void setPatronIdentifier(String patronIdentifier) {
		this.patronIdentifier = MessaggioSip2.SIP2F_PATRON_IDENTIFIER + patronIdentifier + "|";
	}

	public String getPatronStatus() {
		return patronStatus;
	}
	public void setPatronStatus(String patronStatus) {
		this.patronStatus = patronStatus;
	}

	public String getPersonalName() {
		return personalName;
	}
	public void setPersonalName(String personalName) {
		this.personalName = MessaggioSip2.SIP2F_PERSONAL_NAME + personalName + "|";
	}

	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getValidPatron() {
		return validPatron;
	}
	public void setValidPatron(String validPatron) {
		this.validPatron = MessaggioSip2.SIP2F_VALID_PATRON + validPatron + "|"; // NMB: Protocollo dice campo fisso ma usa delimitatore | nei loro esempi. Il delimitatore pensavo fosse solo per i campi a lunghezza variabile.
	}

	public String getValidPatronPassword() {
		return validPatronPassword;
	}
	public void setValidPatronPassword(String validPatronPassword) {
		this.validPatronPassword = MessaggioSip2.SIP2F_VALID_PATRON_PASSWORD + validPatronPassword + "|"; // NMB: Protocollo dice campo fisso ma usa delimitatore | nei loro esempi. Il delimitatore pensavo fosse solo per i campi a lunghezza variabile.
	}

	public List<String> getChargedItems() {
		return chargedItemsVect;
	}

	public String getChargedItemsCount() {
		return chargedItemsCount;
	}
	public void setChargedItemsCount(String chargedItemsCount) {
		this.chargedItemsCount = chargedItemsCount;
	}


	public String getChargedItemsLimit() {
		return chargedItemsLimit;
	}
	public void setChargedItemsLimit(String chargedItemsLimit) {
		this.chargedItemsLimit = MessaggioSip2.SIP2F_CHARGED_ITEMS_LIMIT  + chargedItemsLimit + "|"; // NMB: Protocollo dice campo fisso ma usa delimitatore | nei loro esempi. Il delimitatore pensavo fosse solo per i campi a lunghezza variabile.
	}

	public String getEMailAddress() {
		return eMailAddress;
	}
	public void setEMailAddress(String mailAddress) {
		eMailAddress = MessaggioSip2.SIP2F_E_MAIL_ADDRESS + mailAddress + "|";
	}

	public String getFeeLimit() {
		return feeLimit;
	}
	public void setFeeLimit(String feeLimit) {
		this.feeLimit = MessaggioSip2.SIP2F_FEE_LIMIT + feeLimit + "|";
	}

	public List<String> getFineItems() {
		return fineItemsVect;
	}

	public String getFineItemsCount() {
		return fineItemsCount;
	}
	public void setFineItemsCount(String fineItemsCount) {
		this.fineItemsCount = fineItemsCount;
	}

	public List<String> getHoldItems() {
		return holdItemsVect;
	}

	public String getHoldItemsCount() {
		return holdItemsCount;
	}
	public void setHoldItemsCount(String holdItemsCount) {
		this.holdItemsCount = holdItemsCount;
	}

	public String getHoldItemsLimit() {
		return holdItemsLimit;
	}
	public void setHoldItemsLimit(String holdItemsLimit) {
		this.holdItemsLimit = MessaggioSip2.SIP2F_HOLD_ITEMS_LIMIT + holdItemsLimit + "|"; // NMB: Protocollo dice campo fisso ma usa delimitatore | nei loro esempi. Il delimitatore pensavo fosse solo per i campi a lunghezza variabile.
	}

	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = MessaggioSip2.SIP2F_HOME_ADDRESS + homeAddress + "|";
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}
	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = MessaggioSip2.SIP2F_HOME_PHONE_NUMBER + homePhoneNumber + "|";
	}

	public List<String> getOverdueItems() {
		return overdueItemsVect;
	}

	public String getOverdueItemsCount() {
		return overdueItemsCount;
	}
	public void setOverdueItemsCount(String overdueItemsCount) {
		this.overdueItemsCount = overdueItemsCount;
	}


	public String getOverdueItemsLimit() {
		return overdueItemsLimit;
	}


	public void setOverdueItemsLimit(String overdueItemsLimit) {
		this.overdueItemsLimit = MessaggioSip2.SIP2F_OVERDUE_ITEMS_LIMIT + overdueItemsLimit + "|"; // NMB: Protocollo dice campo fisso ma usa delimitatore | nei loro esempi. Il delimitatore pensavo fosse solo per i campi a lunghezza variabile.
	}


	public List<String> getRecallItems() {
		return recallItemsVect;
	}


	public String getRecallItemsCount() {
		return recallItemsCount;
	}


	public void setRecallItemsCount(String recallItemsCount) {
		this.recallItemsCount = recallItemsCount;
	}


	public List<String> getUnavailableHoldItems() {
		return unavailableHoldItemsVect;
	}



	public String getUnavailableHoldsCount() {
		return unavailableHoldsCount;
	}


	public void setUnavailableHoldsCount(String unavailableHoldsCount) {
		this.unavailableHoldsCount = unavailableHoldsCount;
	}


	public void addHoldItem(String anHoldItem) {
		this.holdItemsVect.add(MessaggioSip2.SIP2F_HOLD_ITEMS + anHoldItem +"|");
	}
	public void addOverdueItem(String anOverdueItem) {
		this.overdueItemsVect.add(MessaggioSip2.SIP2F_OVERDUE_ITEMS + anOverdueItem + "|");
	}
	public void addChargedItem(String aChargedItem) {
		this.chargedItemsVect.add(MessaggioSip2.SIP2F_CHARGED_ITEMS + aChargedItem + "|");
	}
	public void addFineItem(String aFineItem) {
		this.fineItemsVect.add(MessaggioSip2.SIP2F_FINE_ITEMS + aFineItem + "|");
	}
	public void addRecallItem(String aRecallItem) {
		this.recallItemsVect.add(MessaggioSip2.SIP2F_RECALL_ITEMS + aRecallItem + "|");
	}
	public void addUnavailableHoldItem(String anUnavailableHoldItem) {
		this.unavailableHoldItemsVect.add(MessaggioSip2.SIP2F_UNAVAILABLE_HOLD_ITEMS + anUnavailableHoldItem + "|");
	}


}
