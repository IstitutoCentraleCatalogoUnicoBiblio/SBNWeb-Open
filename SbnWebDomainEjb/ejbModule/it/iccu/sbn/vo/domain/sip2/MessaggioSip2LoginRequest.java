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
 *	This message can be used to login to an ACS server program.
 * The ACS should respond with the Login Response message.
 * Whether to use this message or to use some other mechanism to login to the ACS
 * is configurable on the SC.  When this message is used, it will be the first
 * message sent to the ACS.
 *
 * @author almaviva7
 */
public class MessaggioSip2LoginRequest  extends MessaggioSip2 //extends DesignModel.BusinessObjects.LBO_BusinessObject.BusinessObject
    {

	/**
	 *
	 */
	private static final long serialVersionUID = 7558044056541709457L;
	protected char userIdAlgorithm;		// 1-char, fixed-length required field; the algorithm used to encrypt the user id.
	protected char userPwdAlgorithm;	// 1-char, fixed-length required field; the algorithm used to encrypt the password
    protected String loginUserID;		// CN variable-length required field
    protected String loginPassword;		// CO variable-length required field
    protected String locationCode;		// CP variable-length optional field; the SC location.
    protected String codicePolo;		//    codice del polo (estrapolato da SC location)
    protected String codiceBiblioteca;	//    codice della biblioteca (estrapolato da SC location)
    protected String codiceServizio;	//    codice del servizio (estrapolato da SC location)

    /**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    public MessaggioSip2LoginRequest(java.lang.String stringa) throws Exception {
    	String ar[] = MiscString.estraiCampi(stringa, "|");
    	int i=0;
        setCodiceMessaggio(ar[i].substring(0, 2));
        setUserIdAlgorithm(ar[i].charAt(2));
        setUserPwdAlgorithm(ar[i].charAt(3));
        setLoginUserID(ar[i++].substring(6));
        setLoginPassword(ar[i++].substring(2));
        if (ar[i].startsWith(MessaggioSip2.SIP2F_LOCATION_CODE))
        	setLocationCode(ar[i++].substring(2));	// Optional
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
    public MessaggioSip2LoginRequest() {
    }

	@Override
	public String getChecksum() {
		return checksum;
	}

	@Override
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
//		setCodPoloBiblioServizio();
	}

	public String getCodiceBiblioteca(){
		return codiceBiblioteca;
	}

	public String getCodicePolo(){
		return codicePolo;
	}

	public String getCodiceServizio(){
		return codiceServizio;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getLoginUserID() {
		return loginUserID;
	}

	public void setLoginUserID(String loginUserID) {
		this.loginUserID = loginUserID;
	}


	public char getUserIdAlgorithm() {
		return userIdAlgorithm;
	}

	public void setUserIdAlgorithm(char userIdAlgorithm) {
		this.userIdAlgorithm = userIdAlgorithm;
	}

	public char getUserPwdAlgorithm() {
		return userPwdAlgorithm;
	}

	public void setUserPwdAlgorithm(char userPwdAlgorithm) {
		this.userPwdAlgorithm = userPwdAlgorithm;
	}

}
