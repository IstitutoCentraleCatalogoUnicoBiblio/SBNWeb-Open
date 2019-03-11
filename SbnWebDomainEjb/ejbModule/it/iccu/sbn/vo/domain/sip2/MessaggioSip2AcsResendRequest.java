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
 * This message requests that the patron card be blocked by the ACS.
 * This is, for example, sent when the patron is detected tampering with the SC
 * or when a patron forgets to take their card.  The ACS should invalidate the
 * patron’s card and respond with a Patron Status Response message.
 * The ACS could also notify the library staff that the card has been blocked.
 *
 * @author Arge
 */
public class MessaggioSip2AcsResendRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	private static final long serialVersionUID = -3603905317767253051L;

	/**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2AcsResendRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        setCodiceMessaggio(ar[i].substring(0, 2));
        if (ar[i] != null)
        {
        	setSequenceNumber(ar[i].charAt(2));
        	setChecksum(ar[i].substring(5));
        	setErrorDetection(true);
        }
    }

    /**
     * Costruttore
     */
    public MessaggioSip2AcsResendRequest() {
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
