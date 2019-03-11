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
 * This message is used by the SC to request patron information from the ACS.
 * The ACS must respond to this command with a Patron Status Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2PatronStatusRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {


    /**
	 *
	 */
	private static final long serialVersionUID = -1057108711530533846L;
	protected String language;				// 3-char, fixed-length required field
    protected String transactionDate;		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
    protected String institutionId;			// AO variable-length required field
    protected String patronIdentifier;		// AA variable-length required field
    protected String terminalPassword;		// AC variable-length required field
    protected String patronPassword;		// AD variable-length required field


    public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
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
		this.patronIdentifier = patronIdentifier;
	}

	public String getPatronPassword() {
		return patronPassword;
	}

	public void setPatronPassword(String patronPassword) {
		this.patronPassword = patronPassword;
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
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2PatronStatusRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.language = ar[i].substring(2,5);			// 3-char, fixed-length required field
        this.transactionDate = ar[i].substring(5,23); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.institutionId = ar[i++].substring(23+2);		// AO variable-length required field
        this.patronIdentifier=ar[i++].substring(2);		// AA variable-length required field
        this.terminalPassword=ar[i++].substring(2);		// AC variable-length required field
        this.patronPassword=ar[i++].substring(2);		// AD variable-length required field
//        if (ar[i].length() > 10) // Error detection
        if (ar[i] != null && ar[i].length() > 0) // Error detection
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(5));
        	setErrorDetection(true);
        }
    }

    /**
     * Costruttore
     */
    public MessaggioSip2PatronStatusRequest() {
    }



}
