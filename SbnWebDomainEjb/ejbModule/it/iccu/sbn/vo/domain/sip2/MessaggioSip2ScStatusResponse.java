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
 * The ACS must send this message in response to a SC Status message.
 * This message will be the first message sent by the ACS to the SC,
 * since it establishes some of the rules to be followed by the SC and
 * establishes some parameters needed for further communication
 * (exception: the Login Response Message may be sent first to complete login of the SC).
 *
 * @author almaviva7
 */
public class MessaggioSip2ScStatusResponse  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
	/**
	 *
	 */
	private static final long serialVersionUID = 6395055768830608453L;
	protected char onLineStatus;		// 1-char, fixed-length required field: Y or N.
	protected char checkinOk;			// 1-char, fixed-length required field: Y or N.
	protected char checkoutOk;			// 1-char, fixed-length required field: Y or N.
	protected char acsRenewalPolicy;	// 1-char, fixed-length	required field: Y or N.
	protected char statusUpdateOk;		// 1-char, fixed-length required field: Y or N.
	protected char offlineOk;			// 1-char, fixed-length required field: Y or N.
	protected String timeoutPeriod;		// 3-char, fixed-length required field
	protected String retriesAllowed;	// 3-char, fixed-length required field
	protected String dateTimeSync;	    // 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String protocolVersion;	// 4-char, fixed-length required field:  x.xx
	protected String institutionId;		// AO variable-length required field
	protected String libraryName;		// AM variable-length optional field
	protected String supportedMessages;	// BX variable-length required field (2.00 protocol version)
	protected String terminalLocation;	// AN variable-length optional field

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2ScStatusResponse(boolean enableErrorDetection, char sequenceNumber) throws Exception {
    	super(enableErrorDetection, sequenceNumber);
    	this.setCodiceMessaggio(Sip2MessageType.SIP2_ACS_STATUS);
    }

	/**
     * Costruttore a partire dalla richiesta ricevuta dal polo
     *
     * @param boolean enableErrorDetection
     * @param MessaggioSip2ScStatusRequest richiesta
     */
    public MessaggioSip2ScStatusResponse(boolean enableErrorDetection, String codBiblio, MessaggioSip2ScStatusRequest req) throws Exception {
    	super(enableErrorDetection, req.getSequenceNumber());
    	this.setCodiceMessaggio(Sip2MessageType.SIP2_ACS_STATUS);
    	setOnLineStatus('Y');
    	setCheckinOk('Y');
    	setCheckoutOk('Y');
    	// rinnovo non implementato
    	setAcsRenewalPolicy('N');
    	setStatusUpdateOk('Y');
    	setOfflineOk('Y');
    	setTimeoutPeriod("999"); //Unknown (Server : default 1000)
    	setRetriesAllowed("999"); //Unknown
    	setDateTimeSync(req.getSIP2Date(null));
    	setProtocolVersion(req.getVersion());
    	setInstitutionId(codBiblio);
    	setLibraryName("");
    	setSupportedMessages("");
    	setTerminalLocation("");
    }

    public char getAcsRenewalPolicy() {
		return acsRenewalPolicy;
	}

	public void setAcsRenewalPolicy(char acsRenewalPolicy) {
		this.acsRenewalPolicy = acsRenewalPolicy;
	}

	public char getCheckinOk() {
		return checkinOk;
	}

	public void setCheckinOk(char checkinOk) {
		this.checkinOk = checkinOk;
	}

	public char getCheckoutOk() {
		return checkoutOk;
	}

	public void setCheckoutOk(char checkoutOk) {
		this.checkoutOk = checkoutOk;
	}

	public String getDateTimeSync() {
		return dateTimeSync;
	}

	public void setDateTimeSync(String dateTimeSync) {
		this.dateTimeSync = dateTimeSync;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = MessaggioSip2.SIP2F_INSTITUTION_ID+institutionId+"|";
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = MessaggioSip2.SIP2F_LIBRARY_NAME+libraryName+"|";
	}

	public char getOfflineOk() {
		return offlineOk;
	}

	public void setOfflineOk(char offlineOk) {
		this.offlineOk = offlineOk;
	}

	public char getOnLineStatus() {
		return onLineStatus;
	}

	public void setOnLineStatus(char onLineStatus) {
		this.onLineStatus = onLineStatus;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getRetriesAllowed() {
		return retriesAllowed;
	}

	public void setRetriesAllowed(String retriesAllowed) {
		this.retriesAllowed = retriesAllowed;
	}


	public char getStatusUpdateOk() {
		return statusUpdateOk;
	}

	public void setStatusUpdateOk(char statusUpdateOk) {
		this.statusUpdateOk = statusUpdateOk;
	}

	public String getSupportedMessages() {
		return supportedMessages;
	}

	public void setSupportedMessages(String supportedMessages) {
		this.supportedMessages = MessaggioSip2.SIP2F_SUPPORTED_MESSAGES+supportedMessages+"|";
	}

	public String getTerminalLocation() {
		return terminalLocation;
	}

	public void setTerminalLocation(String terminalLocation) {
		this.terminalLocation = MessaggioSip2.SIP2F_TERMINAL_LOCATION+terminalLocation+"|";
	}

	public String getTimeoutPeriod() {
		return timeoutPeriod;
	}

	public void setTimeoutPeriod(String timeoutPeriod) {
		this.timeoutPeriod = timeoutPeriod;
	}

    @Override
	public boolean validateResponseRequiredFields()throws Sip2ValidationException {
		if (this.onLineStatus != 'Y' && this.onLineStatus != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [On line status]");
		if (this.checkinOk != 'Y' && this.checkinOk != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Check-in ok]");
		if (this.checkoutOk != 'Y' && this.checkoutOk != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Check-out ok]");
		if (this.acsRenewalPolicy != 'Y' && this.acsRenewalPolicy != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [ACS Renewal Policy]");
		if (this.statusUpdateOk != 'Y' && this.statusUpdateOk != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Status update ok]");
		if (this.offlineOk != 'Y' && this.offlineOk != 'N')
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Offline ok]");
		if (this.timeoutPeriod == null)
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Timeout period]");
		if (this.retriesAllowed == null)
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Retries allowed]");
		if (this.dateTimeSync == null)
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Date/Time sync]");
		if (this.protocolVersion == null)
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Protocol version]");
		if (this.institutionId == null)
			throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Institution id]");
		if (this.protocolVersion.equals("2.00")){
			if (this.supportedMessages == null)
				throw new Sip2ValidationException("Sip2 protocol: Sc Status response - campi obbligatori non validi [Supported messages]");
		}
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
        stringaMessaggio += getCodiceMessaggio().getCode();
		stringaMessaggio += getOnLineStatus();
		stringaMessaggio += getCheckinOk();
		stringaMessaggio += getCheckoutOk();
		stringaMessaggio += getAcsRenewalPolicy();
		stringaMessaggio += getStatusUpdateOk();
		stringaMessaggio += getOfflineOk();
		stringaMessaggio += getTimeoutPeriod();
		stringaMessaggio += getRetriesAllowed();
		stringaMessaggio += getDateTimeSync();
		stringaMessaggio += getProtocolVersion();

		stringaMessaggio += getInstitutionId();
		stringaMessaggio += getLibraryName();

		stringaMessaggio += getSupportedMessages();
		stringaMessaggio += getTerminalLocation();

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
