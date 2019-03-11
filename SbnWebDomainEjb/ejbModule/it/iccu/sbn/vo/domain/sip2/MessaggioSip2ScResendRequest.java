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
 * This message requests the ACS to re-transmit its last message.
 * It is sent by the SC to the ACS when the checksum in a received message does not match
 * the value calculated by the SC.  The ACS should respond by re-transmitting its last message,
 * This message should never include a “sequence number” field, even when error detection is
 * enabled, (see “Checksums and Sequence Numbers” below) but would include a “checksum”
 * field since checksums are in use.
 *
 * @author almaviva7
 */
public class MessaggioSip2ScResendRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {
    /**
	 *
	 */
	private static final long serialVersionUID = 6581675875909352137L;

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2ScResendRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        setCodiceMessaggio(ar[i].substring(0, 2));
//        if (ar[i] != null)
//        {
//        	setSequenceNumber(ar[i].charAt(2));
//        	setChecksum(ar[i].substring(5));
//        	setErrorDetection(true);
//        }
        if (ar[i]!= null && ar[i].length() > 0) // Error detection
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(5));
        	setErrorDetection(true);
        }
    }

    /**
     * Costruttore
     */
    public MessaggioSip2ScResendRequest() {
    }

	@Override
	public String getChecksum() {
		return checksum;
	}

	@Override
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

}
