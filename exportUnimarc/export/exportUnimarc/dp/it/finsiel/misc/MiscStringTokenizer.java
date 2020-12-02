package it.finsiel.misc;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.nio.charset.Charset;

//import org.xml.sax.SAXException;

/*******************************************************************************
 * 
 * Tokenizer to satisfy also sequence separators and not only character separators
 * Author: 	Argenino
 * Date:	02/09/2008
 ******************************************************************************/

public class MiscStringTokenizer {
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	public static final boolean RETURN_DELIMITERS_AS_TOKEN_TRUE = true;
	public static final boolean RETURN_DELIMITERS_AS_TOKEN_FALSE = false;
	public static final boolean RETURN_EMPTY_TOKENS_TRUE = true;
	public static final boolean RETURN_EMPTY_TOKENS_FALSE = false;
	
	
	
	protected String text;
//	byte[] byteText;
	protected boolean returnDelimitersAsTokens;
	protected char [] charDelimitersAr;
	protected byte [] byteDelimitersAr;
	protected String [] stringDelimitersAr;
	protected Vector tokenVect;	
	boolean returnEmptyTokens;
	int curTokenPos;
	char escapeCharacter = '\\';
	
	public MiscStringTokenizer(String aText, byte[] aByteDelimitersAr, boolean aReturnDelimitersAsTokens, boolean aReturnEmptyTokens) {
		text = aText;
		returnDelimitersAsTokens = aReturnDelimitersAsTokens;
		returnEmptyTokens = aReturnEmptyTokens;
		byteDelimitersAr = aByteDelimitersAr; 
		tokenVect = new Vector();
		splitByteSeparatedTokens();
		curTokenPos = 0;
	}

	public MiscStringTokenizer(byte[] aByteText, byte[] aByteDelimitersAr, boolean aReturnDelimitersAsTokens, boolean aReturnEmptyTokens) {
		
//		byteText = new byte[aByteText.length];
		returnDelimitersAsTokens = aReturnDelimitersAsTokens;
		returnEmptyTokens = aReturnEmptyTokens;
		byteDelimitersAr = aByteDelimitersAr; 
		tokenVect = new Vector();
		splitByteStrByteSeparatedTokens(aByteText);
		curTokenPos = 0;
	}
	
	
	public MiscStringTokenizer(String aText, char[] aCharDelimitersAr, boolean aReturnDelimitersAsTokens, boolean aReturnEmptyTokens) {
		text = aText;
		returnDelimitersAsTokens = aReturnDelimitersAsTokens;
		returnEmptyTokens = aReturnEmptyTokens;
		charDelimitersAr = aCharDelimitersAr; 
		tokenVect = new Vector();
		splitCharacterSeparatedTokens();
		curTokenPos = 0;
	}
	
	
	
	public MiscStringTokenizer(String aText, String[] aStringDelimitersAr, boolean aReturnDelimitersAsTokens, boolean aReturnEmptyTokens) {
		text = aText;
		returnDelimitersAsTokens = aReturnDelimitersAsTokens;
		returnEmptyTokens = aReturnEmptyTokens;
		stringDelimitersAr = aStringDelimitersAr; 
		tokenVect = new Vector();
		splitStringSeparatedTokens();
		curTokenPos = 0;
	}

	
	public MiscStringTokenizer(String aText, char[] aCharDelimitersAr, boolean aReturnDelimitersAsTokens, boolean aReturnEmptyTokens, boolean escaped, boolean keepEscape) {
		text = aText;
		returnDelimitersAsTokens = aReturnDelimitersAsTokens;
		returnEmptyTokens = aReturnEmptyTokens;
		charDelimitersAr = aCharDelimitersAr; 
		tokenVect = new Vector();
		if (escaped == true)
			splitCharacterSeparatedTokensEscaped(keepEscape);
		else
			splitCharacterSeparatedTokens();
		curTokenPos = 0;
	}
	
	public MiscStringTokenizer(String aText, String[] aStringDelimitersAr, boolean aReturnDelimitersAsTokens, boolean aReturnEmptyTokens, boolean escaped, boolean keepEscape) {
		text = aText;
		returnDelimitersAsTokens = aReturnDelimitersAsTokens;
		returnEmptyTokens = aReturnEmptyTokens;
		stringDelimitersAr = aStringDelimitersAr; 
		tokenVect = new Vector();
		if (escaped == true)
			splitCharacterSeparatedTokensEscaped(keepEscape);
		else
			splitCharacterSeparatedTokens();
		curTokenPos = 0;
	}
	
	
	
	
	/**
	 * Tests if there are more tokens available from this tokenizer's string.
	 * If this method returns <tt>true</tt>, then a subsequent call to
	 * <tt>nextToken</tt> with no argument will successfully return a token.
	 * <p>
	 * The current position is not changed.
	 *
	 * @return <code>true</code> if and only if there is at least one token in the
	 *          string after the current position; <code>false</code> otherwise.
	 */
	public boolean hasMoreTokens(){
		if (curTokenPos < tokenVect.size())
			return true;
		return false;
	}
	
	/**
	 * Returns the next token from this string tokenizer.
	 *
	 * @return the next token from this string tokenizer.
	 */
	public String nextToken(){
		if (curTokenPos < tokenVect.size())
			return (String)tokenVect.get(curTokenPos++);
		throw new java.util.NoSuchElementException();
	}	

	
	/**
	 * Returns the same value as nextToken() but does not alter
	 * the internal state of the Tokenizer.  Subsequent calls
	 * to peek() or a call to nextToken() will return the same
	 * token again.
	 *
	 * @return the next token from this string tokenizer.
	 * @throws NoSuchElementException if there are no more tokens in this tokenizer's string.
	 */
	public String peek(){
		if (curTokenPos < tokenVect.size())
			return (String)tokenVect.get(curTokenPos);
		throw new java.util.NoSuchElementException();
	}	
	
	/**
	 * Calculates the number of times that this tokenizer's <code>nextToken</code>
	 * method can be called before it generates an exception. The current position
	 * is not advanced.
	 *
	 * Tokens can be the separators if returnable as tokens 
	 * 
	 * @return the number of tokens remaining in the string using the current delimiter set.
	 * @see #nextToken()
	 */

	public int countTokens(){
		return tokenVect.size();
		}
	

	private void splitCharacterSeparatedTokens()
	{
		StringBuffer sb = new StringBuffer();
		int startPos = 0;
		char[] cArray = text.toCharArray();
		
		int i;
		for (i=0; i < cArray.length; i++)
		{
			int j;
			for (j=0; j < charDelimitersAr.length; j++)
			{
				if (cArray[i] == charDelimitersAr[j])
					break; // found delimiter
			}
			if (j == charDelimitersAr.length) // Se separatore non trovato continua
			{
				continue;
			}
			
			if (startPos != i )	
			{
				tokenVect.add(new String (text.substring(startPos, i))); // Save token
				startPos = i+1;
			}
			else if (startPos == i && returnEmptyTokens)
			{
				tokenVect.add(new String ("")); // save empty token
				startPos = i+1;
			}
			
			else if (startPos == i && !returnEmptyTokens)
			{
				startPos = i+1;
			}
			if (returnDelimitersAsTokens)
			{
				tokenVect.add(new String (""+cArray[i])); // save separator token
			}
		} // End for i
		
		if (startPos != i )	
			tokenVect.add(new String (text.substring(startPos))); // Save token
		else
		{
			// last token empty?
			int j;
			for (j=0; j < charDelimitersAr.length; j++)
			{
				if (cArray[i-1] == charDelimitersAr[j])
					break; // found delimiter
			}
			if (j != charDelimitersAr.length) // Se separatore non trovato continua
			{
				if (returnEmptyTokens)
				{
					tokenVect.add(new String ("")); 
				}
				
			}

		}
	
	} // End splitCharacterSeparatedTokens
	
	private void splitStringSeparatedTokens()
	{
		StringBuffer sb = new StringBuffer();
		int startPos = 0;
		char[] cArray = text.toCharArray();
		
		int i;
		for (i=0; i < cArray.length; i++)
		{
			int j;
			// Cicla su tutti i delimitatori di tipo stringa
			for (j=0; j < stringDelimitersAr.length; j++)
			{
				int k;
				for (k=0; k < stringDelimitersAr[j].length(); k++ )
				{
//					if ((i+k) > cArray.length)
					if ((i+k) >= cArray.length)
						break; // out of bounds
					if (stringDelimitersAr[j].charAt(k) != cArray[i+k])
						break; // NO delimiter found
				}
				if (k == stringDelimitersAr[j].length())
					break; // Trovato delimitatore
				// Continua a cercare
			}
			
			// Se separatore non trovato continua
			if (j == stringDelimitersAr.length) 
				continue;
			
			if (startPos != i )	
			{
				tokenVect.add(new String (text.substring(startPos, i))); // Save token
				startPos = i+stringDelimitersAr[j].length();
				i = startPos-1; // Let auto increment reposistion correctly	
			}
			else if (startPos == i && returnEmptyTokens)
			{
				tokenVect.add(new String ("")); // save empty token
				startPos = i+stringDelimitersAr[j].length();
				i = startPos-1; // Let auto increment reposistion correctly	
			}
			
			else if (startPos == i && !returnEmptyTokens)
			{
				startPos = i+stringDelimitersAr[j].length();
				i = startPos-1; // Let auto increment reposistion correctly	
			}
			if (returnDelimitersAsTokens)
			{
				tokenVect.add(new String (""+stringDelimitersAr[j])); // save separator token
			}
		} // End for i
		
		if (startPos != i )	
			tokenVect.add(new String (text.substring(startPos))); // Save token
	} // End splitStringSeparatedTokens

	/**
	 * Returns the vector of tokens
	 *
	 * @return the vector of tokens generated.
	 */
	public Vector getTokenVect(){
		return  tokenVect;
	}	

	
	
	
	
	public static void main(String args[])
    {
    	char charSepArray[] = { ' ', '\t', '\n'};
    	String stringSepArray[] = { "&$%"}; // , "###"
    	
    	String token;
//        MiscStringTokenizer mst = new MiscStringTokenizer("Prova con separatori\na carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
//        MiscStringTokenizer mst = new MiscStringTokenizer("carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
//        MiscStringTokenizer mst = new MiscStringTokenizer("carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);
//        MiscStringTokenizer mst = new MiscStringTokenizer(" carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);
//        MiscStringTokenizer mst = new MiscStringTokenizer(" carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
//        MiscStringTokenizer mst = new MiscStringTokenizer(" carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
//        MiscStringTokenizer mst = new MiscStringTokenizer(" carattere\t\n ", charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);

//    	MiscStringTokenizer mst = new MiscStringTokenizer("###a&$%c&$%arattere###pippo", stringSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
//    	MiscStringTokenizer mst = new MiscStringTokenizer("###a&$%c&$%arattere###pippo", stringSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);
//    	MiscStringTokenizer mst = new MiscStringTokenizer("###a&$%c&$%arattere###pippo", stringSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
    	MiscStringTokenizer mst = new MiscStringTokenizer("###a&$%c&$%arattere###pippo", stringSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_TRUE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);

//        int tokens = mst.countTokens();
        
        while (mst.hasMoreTokens() == true)
        {
        	token = mst.nextToken();
        	System.out.println("Token: '" + token + "'");
        }
    } // End main
	
	
	

	private void splitCharacterSeparatedTokensEscaped(boolean keepEscape)
	{
		StringBuffer sb = new StringBuffer();
		int startPos = 0;
		char[] cArray = text.toCharArray();
		
		int i;
		int j;
		for (i=0; i < cArray.length; i++)
		{
			// E' una sequenza di escape?
			if (cArray[i] == escapeCharacter)
			{
				if (keepEscape)
					sb.append(cArray[i]); // save escape
				i++;
				sb.append(cArray[i]); 	// save escaped character
				continue;
			}
			
			
			for (j=0; j < charDelimitersAr.length; j++)
			{
				if (cArray[i] == charDelimitersAr[j])
					break; // found delimiter
			}
			if (j == charDelimitersAr.length) // Se separatore non trovato continua
			{
				sb.append(cArray[i]);
				continue;
			}
			
			if (startPos != i )	
			{
				// tokenVect.add(new String (text.substring(startPos, i))); // Save token
				tokenVect.add(new String (sb.toString())); // Save token
				sb.setLength(0); // Clear
				startPos = i+1;
			}

			else if (startPos == i && returnEmptyTokens)
			{
				tokenVect.add(new String ("")); // save empty token
				startPos = i+1;
			}
			
			else if (startPos == i && !returnEmptyTokens)
			{
				startPos = i+1;
			}
			if (returnDelimitersAsTokens)
			{
				tokenVect.add(new String (""+cArray[i])); // save separator token
			}
		} // End for i
		

		
		
		//		if (startPos != i )	
//			tokenVect.add(new String (text.substring(startPos))); // Save token
		
		if (startPos != i )	
			tokenVect.add(new String (sb.toString())); // Save token
		
	} // End splitCharacterSeparatedTokens



	private void splitStringSeparatedTokensEscaped(boolean keepEscape)
	{
		StringBuffer sb = new StringBuffer();
		int startPos = 0;
		char[] cArray = text.toCharArray();
		
		int i;
		for (i=0; i < cArray.length; i++)
		{
			// E' una sequenza di escape?
			if (cArray[i] == escapeCharacter)
			{
				if (keepEscape)
					sb.append(cArray[i]); // save escape
				i++;
				sb.append(cArray[i]); 	// save escaped character
				continue;
			}
		
			
			int j;
			// Cicla su tutti i delimitatori di tipo stringa
			for (j=0; j < stringDelimitersAr.length; j++)
			{
				int k;
				for (k=0; k < stringDelimitersAr[j].length(); k++ )
				{
//					if ((i+k) > cArray.length)
					if ((i+k) >= cArray.length)
						break; // out of bounds
					if (stringDelimitersAr[j].charAt(k) != cArray[i+k])
						break; // NO delimiter found
				}
				if (k == stringDelimitersAr[j].length())
					break; // Trovato delimitatore
				// Continua a cercare
			}
			
			// Se separatore non trovato continua
			if (j == stringDelimitersAr.length)
			{
				sb.append(cArray[i]);
				continue;
			}
			if (startPos != i )	
			{
//				tokenVect.add(new String (text.substring(startPos, i))); // Save token
				tokenVect.add(new String (sb.toString())); // Save token
				sb.setLength(0); // clear
				startPos = i+stringDelimitersAr[j].length();
				i = startPos-1; // Let auto increment reposistion correctly	
			}
			else if (startPos == i && returnEmptyTokens)
			{
				tokenVect.add(new String ("")); // save empty token
				startPos = i+stringDelimitersAr[j].length();
				i = startPos-1; // Let auto increment reposistion correctly	
			}
			
			else if (startPos == i && !returnEmptyTokens)
			{
				startPos = i+stringDelimitersAr[j].length();
				i = startPos-1; // Let auto increment reposistion correctly	
			}
			if (returnDelimitersAsTokens)
			{
				tokenVect.add(new String (""+stringDelimitersAr[j])); // save separator token
			}
		} // End for i
		
		if (startPos != i )	
			//tokenVect.add(new String (text.substring(startPos))); // Save token
			tokenVect.add(new String (sb.toString())); // Save token

	} // End splitStringSeparatedTokens




	private void splitByteSeparatedTokens()
	{
		StringBuffer sb = new StringBuffer();
		int startPos = 0;
		char[] cArray = text.toCharArray();
		
		int i;
		for (i=0; i < cArray.length; i++)
		{
			int j;
			for (j=0; j < byteDelimitersAr.length; j++)
			{
				if (cArray[i] == byteDelimitersAr[j])
					break; // found delimiter
			}
			if (j == byteDelimitersAr.length) // Se separatore non trovato continua
			{
				continue;
			}
			
			if (startPos != i )	
			{
				tokenVect.add(new String (text.substring(startPos, i))); // Save token
				startPos = i+1;
			}
			else if (startPos == i && returnEmptyTokens)
			{
				tokenVect.add(new String ("")); // save empty token
				startPos = i+1;
			}
			
			else if (startPos == i && !returnEmptyTokens)
			{
				startPos = i+1;
			}
			if (returnDelimitersAsTokens)
			{
				tokenVect.add(new String (""+cArray[i])); // save separator token
			}
		} // End for i
		
		if (startPos != i )	
			tokenVect.add(new String (text.substring(startPos))); // Save token
		else
		{
			// last token empty?
			int j;
			for (j=0; j < byteDelimitersAr.length; j++)
			{
				if (cArray[i-1] == byteDelimitersAr[j])
					break; // found delimiter
			}
			if (j != byteDelimitersAr.length) // Se separatore non trovato continua
			{
				if (returnEmptyTokens)
				{
					tokenVect.add(new String ("")); 
				}
				
			}

		}
	
	} // End splitByteSeparatedTokens

	
	
//	String decodeUTF8(byte[] bytes) {
//	    return new String(bytes, UTF8_CHARSET);
//	}
	
	private void splitByteStrByteSeparatedTokens( byte[] byteText)
	{
		

		StringBuffer sb = new StringBuffer();
		int startPos = 0;
//		char[] cArray = text.toCharArray();
		
		int i;
		for (i=0; i < byteText.length; i++)
		{
			int j;
			for (j=0; j < byteDelimitersAr.length; j++)
			{
				if (byteText[i] == byteDelimitersAr[j])
					break; // found delimiter
			}
			if (j == byteDelimitersAr.length) // Se separatore non trovato continua
			{
				continue;
			}
			
			if (startPos != i )	
			{
				int len=i-startPos+1;
				byte[] tba = new byte[len];
				System.arraycopy(byteText, startPos, tba, 0, len);
				String s = new String(tba, UTF8_CHARSET);
				
				tokenVect.add(s); // Save token
				startPos = i+1;
			}
			else if (startPos == i && returnEmptyTokens)
			{
				tokenVect.add(new String ("")); // save empty token
				startPos = i+1;
			}
			
			else if (startPos == i && !returnEmptyTokens)
			{
				startPos = i+1;
			}
//			if (returnDelimitersAsTokens)
//			{
//				tokenVect.add(new String (""+byteText[i])); // save separator token
//			}
		} // End for i
		
		if (startPos != i )
		{
			int len=byteText.length -startPos+1;
			byte[] tba = new byte[len];
			System.arraycopy(byteText, startPos, tba, 0, len);
			String s = new String(tba, UTF8_CHARSET);
			
			tokenVect.add(s); // Save token
			
//			tokenVect.add(new String (text.substring(startPos))); // Save token
		}
		else
		{
			// last token empty?
			int j;
			for (j=0; j < byteDelimitersAr.length; j++)
			{
				if (byteText[i-1] == byteDelimitersAr[j])
					break; // found delimiter
			}
			if (j != byteDelimitersAr.length) // Se separatore non trovato continua
			{
				if (returnEmptyTokens)
				{
					tokenVect.add(new String ("")); 
				}
			}

		}
	
	} // End splitByteStrByteSeparatedTokens
	

} // End MiscStringTokenizer
