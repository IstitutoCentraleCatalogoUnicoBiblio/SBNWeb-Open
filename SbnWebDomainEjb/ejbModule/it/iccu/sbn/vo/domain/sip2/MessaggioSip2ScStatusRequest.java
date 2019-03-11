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
 * The SC status message sends SC status to the ACS.
 * It requires an ACS Status Response message reply from the ACS.
 * This message will be the first message sent by the SC to the ACS
 * once a connection has been established (exception: the Login Message
 * may be sent first to login to an ACS server program).
 * The ACS will respond with a message that establishes some of the rules
 * to be followed by the SC and establishes some parameters
 * needed for further communication.
 * @author Finsiel
 */
public class MessaggioSip2ScStatusRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = -7057590749627030911L;

	protected char 	 statusCode;	// 1-char, fixed-length required field: 0 or 1 or 2
								    // 0 SC unit is OK
								    // 1 SC printer is out of paper
								    // 2 SC is about to shut down

	protected String maxPrintWidth;	// 3-char, fixed-length required field
    protected String version;		// 4-char, fixed-length required field:  x.xx

    /**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2ScStatusRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        setCodiceMessaggio(ar[i].substring(0, 2));
        setStatusCode(ar[i].charAt(2));
        setMaxPrintWidth(ar[i].substring(3,6));
        setVersion(ar[i].substring(6,10));
        if (ar[i].length() > 10)
        {
        	setSequenceNumber(ar[i].charAt(12));
        	setChecksum(ar[i].substring(15));
        	setErrorDetection(true);
        }
    }

    /**
     * Costruttore
     */
    public MessaggioSip2ScStatusRequest() {
    }

	public String getMaxPrintWidth() {
		return maxPrintWidth;
	}

	public void setMaxPrintWidth(String maxPrintWidth) {
		this.maxPrintWidth = maxPrintWidth;
	}

	public char getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(char statusCode) {
		this.statusCode = statusCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


}
