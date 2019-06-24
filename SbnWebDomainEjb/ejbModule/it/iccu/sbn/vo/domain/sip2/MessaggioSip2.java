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

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.exception.Sip2ValidationException;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * La seguente classe (JavaBean) rappresenta la corrispondente tabella della base
 * dati .
 * @author Finsiel
 */
public abstract class MessaggioSip2 implements Serializable  {

	private static final long serialVersionUID = 6794516959844090546L;

	protected static Logger log = Logger.getLogger(MessaggioSip2.class);

	protected List<String> 	screenMessagesVect;	// AF variable-length optional field (REPEATABLE and truncated if too long for SC screen)
	protected List<String> 	printLineVect; 		// AG variable-length optional field (REPEATABLE and truncated if too long for SC printer)

	// Command messages sent by the Self Check (SC) to the Automated Circulation System (ACS)
	public enum Sip2MessageType {
		SIP2_SC_BLOCK_PATRON 			("01"),
		SIP2_SC_CHECKIN 				("09"),
		SIP2_SC_CHECKOUT 				("11"),
		SIP2_SC_END_PATRON_SESSION 		("35"),
		SIP2_SC_FEE_PAID 				("37"),
		SIP2_SC_HOLD 					("15"),
		SIP2_SC_ITEM_INFORMATION 		("17"),
		SIP2_SC_ITEM_STATUS_UPDATE 		("19"),
		SIP2_SC_LOGIN 					("93"),
		SIP2_SC_PATRON_ENABLE 			("25"),
		SIP2_SC_PATRON_INFORMATION 		("63"),
		SIP2_SC_PATRON_STATUS_REQUEST	("23"),
		SIP2_SC_RENEW	 				("29"),
		SIP2_SC_RENEW_ALL	 			("65"),
		SIP2_SC_ACS_RESEND_REQUEST	 	("97"),
		SIP2_SC_STATUS_MESSAGE 			("99"),

		// Response messages sent by the Automated Circulation System (ACS) to the Self Check (SC)
		SIP2_ACS_PATRON_STATUS_RESPONSE 		("24"),
		SIP2_ACS_CHECKOUT_RESPONSE 				("12"),
		SIP2_ACS_CHECKIN_RESPONSE 				("10"),
		SIP2_ACS_STATUS 						("98"),
		SIP2_ACS_REQUEST_SC_RESEND 				("96"),
		SIP2_ACS_LOGIN_RESPONSE 				("94"),
		SIP2_ACS_PATRON_INFORMATION_RESPONSE	("64"),
		SIP2_ACS_END_SESSION_RESPONSE 			("36"),
		SIP2_ACS_FEE_PAID_RESPONSE 				("38"),
		SIP2_ACS_ITEM_INFORMATION_RESPONSE 		("18"),
		SIP2_ACS_ITEM_STATUS_UPDATE_RESPONSE	("20"),
		SIP2_ACS_PATRON_ENABLE_RESPONSE 		("26"),
		SIP2_ACS_HOLD_RESPONSE 					("16"),
		SIP2_ACS_RENEW_RESPONSE 				("30"),
		SIP2_ACS_RENEW_ALL_RESPONSE 			("66"),

		INVALID("00");

		static Map<String, Sip2MessageType> values;

		static {
			Sip2MessageType[] codici = Sip2MessageType.class.getEnumConstants();
			values = new THashMap<String, Sip2MessageType>();
			for (int i = 0; i < codici.length; i++)
				values.put(codici[i].code, codici[i]);
		}

		private final String code;

		private Sip2MessageType(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public static Sip2MessageType of(String code) {
			Sip2MessageType messageType = values.get(code);
			return messageType != null ? messageType : INVALID;
		}
	}

	// Field identifiers for variable-length or optional fields.
	public final static String SIP2F_BLOCKED_CARD_MSG		= "AL";
	public final static String SIP2F_CANCEL					= "BI";
	public final static String SIP2F_CHARGED_ITEMS			= "AU";
	public final static String SIP2F_CHARGED_ITEMS_LIMIT	= "CB";
	public final static String SIP2F_CHECKSUM				= "AZ";	// (used only when error detection is enabled)
	public final static String SIP2F_CURRENCY_TYPE			= "BH";
	public final static String SIP2F_CURRENT_LOCATION		= "AP";
	public final static String SIP2F_DUE_DATE				= "AH";
	public final static String SIP2F_END_ITEM				= "BQ";
	public final static String SIP2F_EXPIRATION_DATE		= "BW";
	public final static String SIP2F_E_MAIL_ADDRESS			= "BE";
	public final static String SIP2F_FEE_ACKNOWLEDGED		= "BO";
	public final static String SIP2F_FEE_AMOUNT				= "BV";
	public final static String SIP2F_FEE_IDENTIFIER			= "CG";
	public final static String SIP2F_FEE_LIMIT				= "CC";
	public final static String SIP2F_FEE_TYPE				= "BT";
	public final static String SIP2F_FINE_ITEMS				= "AV";
	public final static String SIP2F_HOLD_ITEMS				= "AS";
	public final static String SIP2F_HOLD_ITEMS_LIMIT		= "BZ";
	public final static String SIP2F_HOLD_PICKUP_DATE		= "CM";
	public final static String SIP2F_HOLD_QUEUE_LENGTH		= "CF";
	public final static String SIP2F_HOLD_TYPE				= "BY";
	public final static String SIP2F_HOME_ADDRESS			= "BD";
	public final static String SIP2F_HOME_PHONE_NUMBER		= "BF";
	public final static String SIP2F_INSTITUTION_ID			= "AO";
	public final static String SIP2F_ITEM_IDENTIFIER		= "AB";
	public final static String SIP2F_ITEM_PROPERTIES		= "CH";
	public final static String SIP2F_LIBRARY_NAME			= "AM";
	public final static String SIP2F_LOCATION_CODE			= "CP";
	public final static String SIP2F_LOGIN_PASSWORD			= "CO";
	public final static String SIP2F_LOGIN_USER_ID			= "CN";
	public final static String SIP2F_MEDIA_TYPE				= "CK";
	public final static String SIP2F_OVERDUE_ITEMS			= "AT";
	public final static String SIP2F_OVERDUE_ITEMS_LIMIT	= "CA";
	public final static String SIP2F_OWNER					= "BG";
	public final static String SIP2F_PATRON_IDENTIFIER		= "AA";
	public final static String SIP2F_PATRON_PASSWORD		= "AD";
	public final static String SIP2F_PERMANENT_LOCATION		= "AQ";
	public final static String SIP2F_PERSONAL_NAME			= "AE";
	public final static String SIP2F_PICKUP_LOCATION		= "BS";
	public final static String SIP2F_PRINT_LINE				= "AG";
	public final static String SIP2F_QUEUE_POSITION			= "BR";
	public final static String SIP2F_RECALL_DATE			= "CJ";
	public final static String SIP2F_RECALL_ITEMS			= "BU";
	public final static String SIP2F_RENEWED_ITEMS			= "BM";
	public final static String SIP2F_SCREEN_MESSAGE			= "AF";
	public final static String SIP2F_SECURITY_INHIBIT		= "CI";
	public final static String SIP2F_SEQUENCE_NUMBER		= "AY";	// (used only when error detection is enabled)
	public final static String SIP2F_SORT_BIN				= "CL";
	public final static String SIP2F_START_ITEM				= "BP";
	public final static String SIP2F_SUPPORTED_MESSAGES		= "BX";
	public final static String SIP2F_TERMINAL_LOCATION		= "AN";
	public final static String SIP2F_TERMINAL_PASSWORD		= "AC";
	public final static String SIP2F_TITLE_IDENTIFIER		= "AJ";
	public final static String SIP2F_TRANSACTION_ID			= "BK";
	public final static String SIP2F_UNAVAILABLE_HOLD_ITEMS = "CD";
	public final static String SIP2F_UNRENEWED_ITEMS		= "BN";
	public final static String SIP2F_VALID_PATRON			= "BL";
	public final static String SIP2F_VALID_PATRON_PASSWORD	= "CQ";

	public final static String SIP2F_UNDEEFINED1	= "AI";
	public final static String SIP2F_UNDEEFINED2	= "AK";
	public final static String SIP2F_UNDEEFINED3	= "AR";
	public final static String SIP2F_UNDEEFINED4	= "AW";
	public final static String SIP2F_UNDEEFINED5	= "AX";
	public final static String SIP2F_UNDEEFINED6	= "BA";
	public final static String SIP2F_UNDEEFINED7	= "BB";
	public final static String SIP2F_UNDEEFINED8	= "BC";
	public final static String SIP2F_UNDEEFINED9	= "BJ";
	public final static String SIP2F_UNDEEFINED10	= "CE";

	// FV = Field Values

	public final static int	  SIP2FV_CIRCULATION_STATUS_OTHER 			= 1;
	public final static int	  SIP2FV_CIRCULATION_STATUS_ON_ORDER 		= 2;
	public final static int	  SIP2FV_CIRCULATION_STATUS_AVAILABLE 		= 3;
	public final static int	  SIP2FV_CIRCULATION_STATUS_CHARGED 		= 4;
	public final static int	  SIP2FV_CIRCULATION_STATUS_CHARGED_RECALL_DATE = 5;
	public final static int	  SIP2FV_CIRCULATION_STATUS_IN_PROCESS 	= 6;
	public final static int	  SIP2FV_CIRCULATION_STATUS_RECALLED 		= 7;
	public final static int	  SIP2FV_CIRCULATION_STATUS_WAIT_ON_HOLD_SHELF = 8;
	public final static int	  SIP2FV_CIRCULATION_STATUS_WAIT_TO_BE_RESHELVED = 9;
	public final static int	  SIP2FV_CIRCULATION_STATUS_IN_TRANSIT 	= 10;
	public final static int	  SIP2FV_CIRCULATION_STATUS_CLAIMED_RETURNED = 11;
	public final static int	  SIP2FV_CIRCULATION_STATUS_LOST 			= 12;
	public final static int	  SIP2FV_CIRCULATION_STATUS_MISSING 		= 13;

	public final static int	  SIP2FV_FEE_TYPE_OTHER_UNKNOWN 	= 1;
	public final static int	  SIP2FV_FEE_TYPE_ADMINISTRATIVE = 2;
	public final static int	  SIP2FV_FEE_TYPE_DAMAGE 		= 3;
	public final static int	  SIP2FV_FEE_TYPE_OVERDUE 		= 4;
	public final static int	  SIP2FV_FEE_TYPE_PROCESSING 	= 5;
	public final static int	  SIP2FV_FEE_TYPE_RENTAL 		= 6;
	public final static int	  SIP2FV_FEE_TYPE_REPLACEMENT 	= 7;
	public final static int	  SIP2FV_FEE_TYPE_COMPUTER_ACCESS_CHARGE = 8;
	public final static int	  SIP2FV_FEE_TYPE_HOLD_FEE 		= 9;

	public final static char SIP2FV_HOLD_MODE_ADD_PATRON_TO_HOLD_QUEUE_FOR_ITEM = '+';
	public final static char SIP2FV_HOLD_MODE_REMOVE_PATRON_FROM_HOLD_QUEUE_FOR_ITEM = '-';
	public final static char SIP2FV_HOLD_MODE_CHANGE_HOLD_TO_MATCH_PARAMETERS = '*';

	public final static char SIP2FV_HOLD_TYPE_OTHER 							= '1';
	public final static char SIP2FV_HOLD_TYPE_ANY_COPY_OF_A_TITLE 				= '2';
	public final static char SIP2FV_HOLD_TYPE_A_SPECIFIC_COPY_OF_A_TITLE 		= '3';
	public final static char SIP2FV_HOLD_TYPE_ANY_COPY_AT_BRANCH_OR_SUBLOCATION = '4';

	public final static String SIP2FV_LANGUAGE_UNKNOWN_DEFAULT		= "000";
	public final static String SIP2FV_LANGUAGE_ENGLISH			    = "001";
	public final static String SIP2FV_LANGUAGE_FRENCH			    = "002";
	public final static String SIP2FV_LANGUAGE_GERMAN			    = "003";
	public final static String SIP2FV_LANGUAGE_ITALIAN			    = "004";
	public final static String SIP2FV_LANGUAGE_DUTCH			    = "005";
	public final static String SIP2FV_LANGUAGE_SWEDISH			    = "006";
	public final static String SIP2FV_LANGUAGE_FINNISH			    = "007";
	public final static String SIP2FV_LANGUAGE_SPANISH			    = "008";
	public final static String SIP2FV_LANGUAGE_DANISH			    = "009";
	public final static String SIP2FV_LANGUAGE_PORTUGUESE		    = "010";
	public final static String SIP2FV_LANGUAGE_CANADIAN_FRENCH		= "011";
	public final static String SIP2FV_LANGUAGE_NORWEGIAN		    = "012";
	public final static String SIP2FV_LANGUAGE_HEBREW			    = "013";
	public final static String SIP2FV_LANGUAGE_JAPANESE		      	= "014";
	public final static String SIP2FV_LANGUAGE_RUSSIAN			    = "015";
	public final static String SIP2FV_LANGUAGE_ARABIC			    = "016";
	public final static String SIP2FV_LANGUAGE_POLISH			    = "017";
	public final static String SIP2FV_LANGUAGE_GREEK			    = "018";
	public final static String SIP2FV_LANGUAGE_CHINESE			    = "019";
	public final static String SIP2FV_LANGUAGE_KOREAN 			    = "020";
	public final static String SIP2FV_LANGUAGE_NORTH_AMERICAN_SPANISH = "021";
	public final static String SIP2FV_LANGUAGE_TAMIL			    = "022";
	public final static String SIP2FV_LANGUAGE_MALAY			    = "023";
	public final static String SIP2FV_LANGUAGE_UNITED_KINGDOM		= "024";
	public final static String SIP2FV_LANGUAGE_ICELANDIC		    = "025";
	public final static String SIP2FV_LANGUAGE_BELGIAN			    = "026";
	public final static String SIP2FV_LANGUAGE_TAIWANESE		    = "027";


	public final static String SIP2FV_MEDIA_TYPE_OTHER                = "000";
	public final static String SIP2FV_MEDIA_TYPE_BOOK                 = "001";
	public final static String SIP2FV_MEDIA_TYPE_MAGAZINE             = "002";
	public final static String SIP2FV_MEDIA_TYPE_BOUND_JOURNAL        = "003";
	public final static String SIP2FV_MEDIA_TYPE_AUDIO_TAPE           = "004";
	public final static String SIP2FV_MEDIA_TYPE_VIDEO_TAPE           = "005";
	public final static String SIP2FV_MEDIA_TYPE_CD_CDROM             = "006";
	public final static String SIP2FV_MEDIA_TYPE_DISKETTE             = "007";
	public final static String SIP2FV_MEDIA_TYPE_BOOK_WITH_DISKETTE   = "008";
	public final static String SIP2FV_MEDIA_TYPE_BOOK_WITH_CD         = "009";
	public final static String SIP2FV_MEDIA_TYPE_BOOK_WITH_AUDIO_TAPE = "010";

	public final static int SIP2FV_PATRON_STATUS_CHARGE_PRIVILEGES_DENIED           = 0;
	public final static int SIP2FV_PATRON_STATUS_RENEWAL_PRIVILEGES_DENIED          = 1;
	public final static int SIP2FV_PATRON_STATUS_RECALL_PRIVILEGES_DENIED           = 2;
	public final static int SIP2FV_PATRON_STATUS_HOLD_PRIVILEGES_DENIED             = 3;
	public final static int SIP2FV_PATRON_STATUS_CARD_REPORTED_LOST                 = 4;
	public final static int SIP2FV_PATRON_STATUS_TOO_MANY_ITEMS_CHARGED             = 5;
	public final static int SIP2FV_PATRON_STATUS_TOO_MANY_ITEMS_OVERDUE             = 6;
	public final static int SIP2FV_PATRON_STATUS_TOO_MANY_RENEWALS                  = 7;
	public final static int SIP2FV_PATRON_STATUS_TOO_MANY_CLAIMS_OF_ITEMS_RETURNED  = 8;
	public final static int SIP2FV_PATRON_STATUS_TOO_MANY_ITEMS_LOST                = 9;
	public final static int SIP2FV_PATRON_STATUS_EXCESSIVE_OUTSTANDING_FINES        = 10;
	public final static int SIP2FV_PATRON_STATUS_EXCESSIVE_OUTSTANDING_FEES         = 11;
	public final static int SIP2FV_PATRON_STATUS_RECALL_OVERDUE                     = 12;
	public final static int SIP2FV_PATRON_STATUS_TOO_MANY_ITEMS_BILLED              = 13;

	public final static String SIP2FV_PAYMENT_TYPE_CASH         = "00";
	public final static String SIP2FV_PAYMENT_TYPE_VISA         = "01";
	public final static String SIP2FV_PAYMENT_TYPE_CREDIT_CARD	= "02";

 	public final static String SIP2FV_SECURITY_MARKER_OTHER         = "00";
	public final static String SIP2FV_SECURITY_MARKER_NONE          = "01";
	public final static String SIP2FV_SECURITY_MARKER_TATTLE_TAPE	= "02";
	public final static String SIP2FV_SECURITY_MARKER_WISPER_TAPE	= "02";

	public final static char SIP2FV_STATUS_CODE_OK						= '0';
	public final static char SIP2FV_STATUS_CODE_PRINTER_OUT_OF_PAPER	= '1';
	public final static char SIP2FV_STATUS_CODE_SHUTDOWN				= '2';

 	public final static int SIP2FV_SUMMARY_HOLD_ITEMS                 = 0;
	public final static int SIP2FV_SUMMARY_OVERDUE_ITEMS              = 1;
	public final static int SIP2FV_SUMMARY_CHARGED_ITEMS              = 2;
	public final static int SIP2FV_SUMMARY_FINE_ITEMS                 = 3;
	public final static int SIP2FV_SUMMARY_RECALL_ITEMS               = 4;
	public final static int SIP2FV_SUMMARY_UNAVAILABLE_HOLDS          = 5;
	public final static int SIP2FV_SUMMARY_FEE_ITEMS                  = 6;

	public final static int SIP2FV_SUPPORTED_MESSAGES_PATRON_STATUS_REQUEST  = 0;
	public final static int SIP2FV_SUPPORTED_MESSAGES_CHECKOUT               = 1;
	public final static int SIP2FV_SUPPORTED_MESSAGES_CHECKIN                = 2;
	public final static int SIP2FV_SUPPORTED_MESSAGES_BLOCK_PATRON           = 3;
	public final static int SIP2FV_SUPPORTED_MESSAGES_SC_ACS_STATUS          = 4;
	public final static int SIP2FV_SUPPORTED_MESSAGES_REQUEST_SC_ACS_RESEND  = 5;
	public final static int SIP2FV_SUPPORTED_MESSAGES_LOGIN                  = 6;
	public final static int SIP2FV_SUPPORTED_MESSAGES_PATRON_INFORMATION     = 7;
	public final static int SIP2FV_SUPPORTED_MESSAGES_END_PATRON_SESSION     = 8;
	public final static int SIP2FV_SUPPORTED_MESSAGES_FEE_PAID               = 9;
	public final static int SIP2FV_SUPPORTED_MESSAGES_ITEM_INFORMATION       = 10;
	public final static int SIP2FV_SUPPORTED_MESSAGES_ITEM_STATUS_UPDATE     = 11;
	public final static int SIP2FV_SUPPORTED_MESSAGES_PATRON_ENABLE          = 12;
	public final static int SIP2FV_SUPPORTED_MESSAGES_HOLD                   = 13;
	public final static int SIP2FV_SUPPORTED_MESSAGES_RENEW                  = 14;
	public final static int SIP2FV_SUPPORTED_MESSAGES_RENEW_ALL              = 15;

	public static final char SIP2_LINE_TERMINATOR = '\r';






    private Sip2MessageType codiceMessaggio;
    protected char sequenceNumber;
    protected String checksum;
    boolean errorDetection;

	/**
	 * Questo metodo crea una istanza della classe Messaggio
	 *
	 * @param msg
	 *            La stringa del messaggio
	 * @param type
	 *            Indica se di input od output
	 */
	public static MessaggioSip2 createRequestMessage(String message) {

		try {
			String code = ValidazioneDati.substring(message, 0, 2);
			Sip2MessageType messageType = Sip2MessageType.of(code);
			switch (messageType) {

			case SIP2_SC_LOGIN:
				return new MessaggioSip2LoginRequest(message);

			case SIP2_SC_STATUS_MESSAGE:
				return new MessaggioSip2ScStatusRequest(message);

			case SIP2_SC_PATRON_STATUS_REQUEST:
				return new MessaggioSip2PatronStatusRequest(message);

			case SIP2_SC_PATRON_INFORMATION:
				return new MessaggioSip2PatronInformationRequest(message);

			case SIP2_SC_ITEM_INFORMATION:
				return new MessaggioSip2ItemInformationRequest(message);

			case SIP2_SC_CHECKOUT:
				return new MessaggioSip2CheckoutRequest(message);

			case SIP2_SC_CHECKIN:
				return new MessaggioSip2CheckinRequest(message);

			case SIP2_SC_FEE_PAID:
				return new MessaggioSip2FeePaidRequest(message);

			case SIP2_SC_END_PATRON_SESSION:
				return new MessaggioSip2EndPatronSessionRequest(message);

			case SIP2_SC_BLOCK_PATRON:
				return new MessaggioSip2BlockPatronRequest(message);

			case SIP2_SC_PATRON_ENABLE:
				return new MessaggioSip2EnablePatronRequest(message);

			case SIP2_SC_ITEM_STATUS_UPDATE:
				return new MessaggioSip2ItemStatusUpdateRequest(message);

			case SIP2_SC_ACS_RESEND_REQUEST:
				return new MessaggioSip2ScResendRequest(message);

			default:
				log.error("Messaggio SIP2 non gestito");
				break;
			}

		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

    /**
     * Costruttore a partire dalla stringa del messaggio ricevuto dal polo
     *
     * @param stringa La stringa del messaggio
     */
    MessaggioSip2(String stringa, boolean errorDetection) throws Exception {

    	Sip2MessageType codMsg = Sip2MessageType.of(stringa.substring(0, 2) );
        setCodiceMessaggio(codMsg);
        this.errorDetection = errorDetection;
    	setSequenceNumber(sequenceNumber);
    }

	MessaggioSip2(boolean enableErrorDetection, char sequenceNumber) {
		screenMessagesVect = new ArrayList<String>();
		printLineVect = new ArrayList<String>();
		setErrorDetection(enableErrorDetection);
		setSequenceNumber(sequenceNumber);
	}

    /**
     * Costruttore
     */
    MessaggioSip2() {
    	codiceMessaggio = Sip2MessageType.INVALID;
    	this.errorDetection = false;
    	screenMessagesVect = new ArrayList<String>();
    	printLineVect = new ArrayList<String>();
    }

    /**
     * @param codiceMessaggio
     */
    public void setCodiceMessaggio(Sip2MessageType codiceMessaggio) {
        this.codiceMessaggio = codiceMessaggio;
    }

    public void setCodiceMessaggio(String codiceMessaggio) {
        this.setCodiceMessaggio(Sip2MessageType.of(codiceMessaggio) );
    }

    /**
     * Il metodo ritorna l'array di byte da spedire al polo in base ai dati del
     * messaggio.
     *
     * @return byte[]
     */
    public byte[] toStream() {
        return null;
    }



    /**
     * Il metodo restituisce il messaggio di output
     *
     * @return String: il messaggio di output
     */
//    public String getMessaggioOutput() {
//        return messaggioOutput;
//    }

    /**
     * Il metodo detta il messaggio di output
     *
     * @param String MesseggioOut: il valore del mesaggio da settare
     */
//    public void setMessaggioOutput(String MesseggioOut) {
//        messaggioOutput = MesseggioOut;
//    }

    /*************************************************************************
    * compute_checksum   Computes a Standard Protocol checksum for a string
    *                    passed as a parameter.  The string is assumed to
    *                    include all characters of the message up to and
    *                    including the checksum’s field ID characters.
    *                    Returns a pointer to an ASCII representation of the
    *                    checksum.
    *
    *    message         message on which to compute checksum
    *
    *************************************************************************/
    String computeChecksum (String message)
    {
    	String ascii_checksum;
    	short checksum = 0;
//    	 String ENCODING = "UTF-8";

//    	 try
//    	  {
//    	     byte [  ]  data = message.getBytes  ( ENCODING ) ;
    		byte [  ]  data = message.getBytes  () ;
    		for  ( int b = 0; b  <  data.length; b++ )
    	   checksum += data [ b ]; //   * multiplier++
//    	  }
//    	 catch  ( java.io.UnsupportedEncodingException ex )
//    	  {
//    	     ex.printStackTrace (  ) ;
//    	  }


    	checksum = (short) -(checksum & 0xFFFF);
    	//sprintf (ascii_checksum, “%4.4X”, checksum);
    	ascii_checksum = Integer.toHexString(checksum).toUpperCase();

    	if (ascii_checksum.length() > 4)
    		ascii_checksum = ascii_checksum.substring(4);

    	return (ascii_checksum);
    }

	public Sip2MessageType getCodiceMessaggio() {
		return codiceMessaggio;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public char getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(char sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public boolean isErrorDetection() {
		return errorDetection;
	}

	public void setErrorDetection(boolean errorDetection) {
		this.errorDetection = errorDetection;
	}

	public void addScreenMessage(String aScreenMessage) {
		this.screenMessagesVect.add(new String(SIP2F_SCREEN_MESSAGE	+ aScreenMessage + "|"));
	}

	public void addPrintLine(String aPrintLine) {
		this.printLineVect.add(SIP2F_PRINT_LINE + aPrintLine + "|");
	}

	public List<String> getPrintLineVect() {
		return printLineVect;
	}

	public void setPrintLineVect(List<String> printLineVect) {
		this.printLineVect = printLineVect;
	}

	public List<String> getScreenMessagesVect() {
		return screenMessagesVect;
	}

	public void setScreenMessagesVect(List<String> screenMessagesVect) {
		this.screenMessagesVect = screenMessagesVect;
	}

	public boolean validateResponseRequiredFields() throws Sip2ValidationException {
   		throw new Sip2ValidationException("Sip2 protocol: Vlidation not implemented in derived class");

//		return true;
	}

    /**
     * Il metodo trasforma in stringa il messaggio
     * @throws Exception
     */

    @Override
	public String toString() {
    	try {
			validateResponseRequiredFields();
		} catch (Sip2ValidationException e) {
			log.error(e);
		}

        String stringaMessaggio = "";
        stringaMessaggio += codiceMessaggio.getCode();
        if (errorDetection == true)
        {
            stringaMessaggio += SIP2F_SEQUENCE_NUMBER + this.sequenceNumber; // 1 character sequence
            stringaMessaggio += SIP2F_CHECKSUM;
            stringaMessaggio = computeChecksum(stringaMessaggio);
        }
        return stringaMessaggio;
    }

    /**
     * Return input timestamp converted to SIP2 date (YYYYMMDDZZZZHHMMSS)
     * If input timestamp is null, return current SIP2 date.
     *
     * @param timestamp date to convert
     * @return string sip2 format date
     */
    public String getSIP2Date(Timestamp timestamp){
    	//YYYYMMDDZZZZHHMMSS
    	Calendar calendar = Calendar.getInstance(); //today default
    	if (timestamp!=null) calendar.setTimeInMillis(timestamp.getTime());
    	String year = String.valueOf(calendar.get(Calendar.YEAR));
    	String month = String.valueOf(calendar.get(Calendar.MONTH)+1); //Gregorian and Julian calendars: JANUARY = 0
    	String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    	String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    	String min = String.valueOf(calendar.get(Calendar.MINUTE));
    	String sec = String.valueOf(calendar.get(Calendar.SECOND));
    	calendar = null;
    	month = month.length()==1 ? "0" + month : month;
    	day = day.length()==1 ? "0" + day : day;
    	hour = hour.length()==1 ? "0" + hour : hour;
    	min = min.length()==1 ? "0" + min : min;
    	sec = sec.length()==1 ? "0" + sec : sec;
    	return year + month + day + "    " + hour + min + sec;
    }

}
