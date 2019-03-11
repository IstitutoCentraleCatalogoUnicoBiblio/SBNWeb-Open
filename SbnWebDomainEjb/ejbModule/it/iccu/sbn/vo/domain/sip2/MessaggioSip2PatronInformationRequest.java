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
 * This message is a superset of the Patron Status Request message.
 * It should be used to request patron information.
 * The ACS should respond with the Patron Information Response message.
 *
 * @author almaviva7
 */
public class MessaggioSip2PatronInformationRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
    /**
	 *
	 */
	private static final long serialVersionUID = -5209664346762499267L;
	protected String language;				// 3-char, fixed-length required field
    protected String transactionDate;		// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
	protected String summary;				// 10-char, fixed-length required field
    protected String institutionId;			// AO variable-length required field
    protected String patronIdentifier;		// AA variable-length required field
    protected String terminalPassword;		// AC variable-length optional field
    protected String patronPassword;		// AD variable-length optional field
    protected String startItem;				// BP variable-length optional field
    protected String endItem;				// BQ variable-length optional field

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2PatronInformationRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        this.setCodiceMessaggio(ar[i].substring(0, 2));
        this.language = ar[i].substring(2,5);			// 3-char, fixed-length required field
        this.transactionDate = ar[i].substring(5,23); 	// 18-char, fixed-length required field:  YYYYMMDDZZZZHHMMSS
        this.summary = ar[i].substring(23,33);			// 10-char, fixed-length required field
        this.institutionId = ar[i++].substring(33+2);		// AO variable-length required field
        this.patronIdentifier=ar[i++].substring(2);		// AA variable-length required field


    	// Cerchiamo i campi opzionali
		for (; ; i++)
		{
	        if (ar[i].startsWith(MessaggioSip2.SIP2F_TERMINAL_PASSWORD))
	        	this.terminalPassword=ar[i].substring(2);		// AC variable-length optional field

	        else if (ar[i].startsWith(MessaggioSip2.SIP2F_PATRON_PASSWORD))
	        	this.patronPassword=ar[i].substring(2);		// AD variable-length optional field

	        else if (ar[i].startsWith(MessaggioSip2.SIP2F_START_ITEM))
	            this.startItem=ar[i].substring(2);			// BP variable-length optional field

	        else if (ar[i].startsWith(MessaggioSip2.SIP2F_END_ITEM))
	        	this.endItem=ar[i].substring(2);				// BQ variable-length optional field
			else
				break;
		}

//        if (ar[i].length() > 10) // Erro detection
        if (ar[i] != null && ar[i].length() > 0) // Error detection
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(3+2));
        	setErrorDetection(true);
        }
    }

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
     * Costruttore
     */
    public MessaggioSip2PatronInformationRequest() {
    }

	public String getEndItem() {
		return endItem;
	}

	public void setEndItem(String endItem) {
		this.endItem = endItem;
	}

	public String getStartItem() {
		return startItem;
	}

	public void setStartItem(String startItem) {
		this.startItem = startItem;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}



}
