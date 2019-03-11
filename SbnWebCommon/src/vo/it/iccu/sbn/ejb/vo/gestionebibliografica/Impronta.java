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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

/**
 * <p>Title: Interfaccia in diretta</p>
 *
 * <p>Description: Interfaccia web per il sistema bibliotecario nazionale</p>
 *
 * <p>Impronta legata a un titolo antico o musicale.</p>
 *
 * <p>Copyright: Copyright (c) 2002</p>
 *
 * <p>Company: Finsiel</p>
 *
 * @author Giuseppe Casafina
 *
 * @version 1.0
 */
public class Impronta extends SerializableVO {

	// = Impronta.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -6701125695184129995L;
	/**
	 * Lunghezza standard del primo campo
	 */
	private final int LENGTH_CAMPO_1 = 10;
	/**
	 * Lunghezza standard del secondo campo
	 */
	private final int LENGTH_CAMPO_2 = 14;
	/**
	 * Lunghezza standard del terzo campo
	 */
	private final int LENGTH_CAMPO_3 = 8;
	/**
	 * Lunghezza standard totale dell'impronta
	 */
	private final int LENGTH_IMPRONTA =
		LENGTH_CAMPO_1 +
		LENGTH_CAMPO_2 +
		LENGTH_CAMPO_3;

	/**
	 * Elenco di caratteri in cui deve essere compreso
	 * il carattere speciale (presente tra due parentesi
	 * tonde) del secondo campo dell'impronta.
	 */
	private final String CARATTERE_SPECIALE_CAMPO2 = "37CS";
	/**
	 * Elenco di caratteri in cui deve essere compreso
	 * il carattere speciale (presente tra due parentesi
	 * tonde) del terzo campo dell'impronta.
	 */
	private final String CARATTERE_SPECIALE_CAMPO3 = "ACEFGHMQRTXYZ";
	/**
	 * Carattere blank (spazio).
	 */
	private final String SPAZIO = " ";

	/**
	 * Messaggio d'errore di default: per tutti
	 * i casi eventuali d'errore non previsti.
	 */
	private final String ERRORE_DEFAULT = "Impronta non valida.";

	/**
	 * Messaggio d'errore di digitazione dei
	 * caratteri alfanumerici separati da spazio.
	 */
	private final String ERRORE_CH_ALFANUMERICI_SPAZIO =
		"Il formato dell'impronta non è corretto\n" +
		"Controllare l'indicazione dei caratteri\n" +
		"Formato atteso:\n" +
		"4 gruppi di 4 chr alfanumerici separati da spazio";
	/**
	 * Messaggio d'errore di digitazione dopo il quarto
	 * gruppo di caratteri separati da parentesi tonde e
	 * seguiti da spazio.
	 */
	private final String ERRORE_CH_SPECIALE_CAMPO2 =
		"Il formato dell'impronta non è corretto\n" +
		"Controllare l'indicazione dopo il quarto gruppo di caratteri\n" +
		"Formato atteso:\n" +
		"un chr scelto tra 3, 7, C, S racchiuso da parentesi " +
		"tonde e seguito da spazio";
	/**
	 * Messaggio d'errore di digitazione della data e
	 * dei caratteri separati da parentesi tonde.
	 */
	private final String ERRORE_CH_SPECIALE_CAMPO3 =
		"Il formato dell'impronta non è corretto\n" +
		"Controllare l'indicazione di forma della data\n" +
		"Formato atteso:\n" +
		"un chr scelto tra A, C, E, F, G, H, M, Q, R, T, X, Y, Z " +
		"racchiuso da parentesi tonde";
	/**
	 * Messaggio d'errore di digitazione della data in
	 * cifre arabe seguita da spazio.
	 */
	private final String ERRORE_DATA_CAMPO3 =
		"Il formato dell'impronta non è corretto\n" +
		"Controllare l'indicazione della data\n" +
		"Formato atteso:\n" +
		"la data in cifre arabe seguita da spazio";
	/**
	 * Messaggio d'errore sulla lunghezza dell'impronta inserita.
	 */
	private final String ERRORE_LUNGHEZZA = "La lunghezza " +
		"dell'impronta deve essere di "+LENGTH_IMPRONTA+" caratteri.";

	/**
	 * Messaggio d'errore
	 */
	private String messaggioErrore;

	/**
	 * Impronta del titolo.
	 */
	private String impronta;

	/**
	 * Nota dell'Impronta.
	 */
	private String nota;

	/**
	 * Primo campo dell'impronta.
	 */
	private String primoCampo;
	/**
	 * Secondo campo dell'impronta.
	 */
	private String secondoCampo;
	/**
	 * Terzo campo dell'impronta.
	 */
	private String terzoCampo;


	/**
	 * Crea un nuovo oggetto Impronta.
	 *
	 * @param impronta Impronta da esaminare.
	 * @param nota nota dell'impronta.
	 */
	public Impronta (
		String impronta,
		String nota){

		this.impronta = impronta;
		if (nota == null){
			this.nota = "";
		} else {
			this.nota = nota;
		}

		// Valorizza i tre campi da cui deve
		// essere composta l'impronta.
		setCampiImpronta(impronta);

		// Imposta il messaggio d'errore con un testo di default.
		setMessaggioErrore(ERRORE_DEFAULT);
	}

	/**
	 * Crea un nuovo oggetto Impronta.
	 *
	 * @param primoCampo	primo campo dell'impronta da esaminare.
	 * @param secondoCampo	secondo campo dell'impronta da esaminare.
	 * @param terzoCampo	terzo campo dell'impronta da esaminare.
	 * @param nota nota dell'impronta.
	 */
	public Impronta (
		String primoCampo,
		String secondoCampo,
		String terzoCampo,
		String nota){

		this.impronta 		= primoCampo + secondoCampo + terzoCampo;
		this.primoCampo 	= primoCampo;
		this.secondoCampo 	= secondoCampo;
		this.terzoCampo 	= terzoCampo;
		if (nota == null){
			this.nota = "";
		} else {
			this.nota = nota;
		}

		// Imposta il messaggio d'errore con un testo di default.
		setMessaggioErrore(ERRORE_DEFAULT);
	}


	/**
	 * Verifica l'esattezza della lunghezza dell'impronta,
	 * dopodichè la divide in tre parti andando a impostare
	 * i tre campi da cui è composto il modello standard.
	 *
	 * @param impronta Impronta da esaminare.
	 */
	private void setCampiImpronta(String impronta){

		int beginIndex = 0;
		int endIndex = 0;

		// Se la lunghezza dell'impronta è giusta,
		// imposta i 3 campi che la compongono
		if ( isOkLunghezzaImpronta(impronta) ){
			// Campo nr.1
			beginIndex		= 0;
			endIndex 		= LENGTH_IMPRONTA - (LENGTH_CAMPO_2 + LENGTH_CAMPO_3);
			primoCampo 		= impronta.substring(beginIndex, endIndex);
			// Campo nr.2
			beginIndex 		= endIndex;
			endIndex 		= (LENGTH_IMPRONTA - (LENGTH_CAMPO_1 + LENGTH_CAMPO_3) + beginIndex);
			secondoCampo 	= impronta.substring(beginIndex, endIndex);
			// Campo nr.3
			beginIndex 		= endIndex;
			endIndex 		= (LENGTH_IMPRONTA - (LENGTH_CAMPO_1 + LENGTH_CAMPO_2) + beginIndex);
			terzoCampo 		= impronta.substring(beginIndex, endIndex);
		}
	}// end setCampiImpronta

	/**
	 * @return
	 */
	public String getImpronta() {
		return impronta;
	}

	/**
	 * @return
	 */
	public String getMessaggioErrore() {
		return messaggioErrore;
	}

	/**
	 * Imposta il messaggio d'errore.
	 *
	 * @param messaggioErrore Messaggio dell'errore provocato
	 */
	private void setMessaggioErrore(String messaggioErrore){
		this.messaggioErrore = messaggioErrore;
	}

	/**
	 * @return
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @param string
	 */
	private void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @return
	 */
	public String getPrimoCampo() {
		return primoCampo;
	}

	/**
	 * @return
	 */
	public String getSecondoCampo() {
		return secondoCampo;
	}

	/**
	 * @return
	 */
	public String getTerzoCampo() {
		return terzoCampo;
	}

	/**
	 * Confronta la lunghezza dell'impronta in esame e
	 * prepara il messaggio d'errore nel caso in cui
	 * non sia conforme al modello di confronto.
	 *
	 * @param impronta Impronta da esaminare
	 * @return true se la lunghezza è ok, false se non lo è.
	 */
	private boolean isOkLunghezzaImpronta(String impronta){
		boolean ok = false;

		if (impronta.length() == LENGTH_IMPRONTA){
			ok = true;
		} else {
			setMessaggioErrore(ERRORE_LUNGHEZZA);
		}

		return ok;
	}

	/**
	 * Aggiunge tanti spazi quanti ne servono
	 * per raggiungere l'esatta lunghezza (secondo
	 * standard) dell'impronta inserita.
	 *
	 * @param impronta
	 */
	private void setLunghezzaEsatta(String impronta){
		String spazi = "";
		int nrSpazi = LENGTH_IMPRONTA - impronta.length();
		for (int i=0; i<nrSpazi; i++){
			spazi = spazi + SPAZIO;
		}
		// Imposta l'impronta
		this.impronta = impronta + spazi;
		// Imposta i 3 campi dell'impronta
		setCampiImpronta(impronta + spazi);
	}

	/**
	 * Restituisce true se l'impronta è conforme
	 * al modello di confronto, false se non lo è.
	 *
	 * @return ok Esito dell'esame dell'impronta.
	 */
	public boolean isOkImpronta(){

		boolean ok = false;

		// Controlla la lunghezza dell'impronta
		if ( isOkLunghezzaImpronta(getImpronta()) ){

	// COMMENTATO IL 13/05/2004
//			// Controlla i tre campi che formano l'impronta
//			if ( (isOkPrimoCampoImpronta(getPrimoCampo()))
//				&& (isOkSecondoCampoImpronta(getSecondoCampo()))
//				&& (isOkTerzoCampoImpronta(getTerzoCampo())) ){
//
//				ok = true;
//			}

			// INSERITO IL 13/05/2004
			// Viene aggirato ogni controllo sull'Impronta
			// da parte di ID (eccetto quello sulla lunghezza),
			// in quanto gli eventuali messaggi di errore
			// verranno gestiti dal protocollo.
			ok = true;
		}

		// INSERITO IL 15/06/2004
		// Se la lunghezza dell'impronta inserita
		// non è esatta, vengono aggiunti N spazi per
		// renderla conforme allo standard, questo perchè
		// dovrà essere il protocollo ad occuparsi dei
		// vari messaggi d'errore.
		else {
			// CASO TEST (ROMA): CNCE006399
			setLunghezzaEsatta(getImpronta());
			ok = true;
		}

		return ok;
	}

	/**
	 * Restituisce true se il primo campo dell'impronta è
	 * conforme al modello di confronto, se non lo è
	 * restituisce false e prepara il messaggio d'errore.
	 *
	 * @param primoCampo	primo campo dell'impronta.
	 * @return ok 			Esito dell'esame dell'impronta.
	 */
	private boolean isOkPrimoCampoImpronta(String primoCampo){
		boolean ok = true;

		for (int i=0; i<primoCampo.length(); i++){
			if (!okCaratterePrimoCampo(i, primoCampo.charAt(i))){
				ok = false;
			}
			if (!ok){
				break;
			}
		}

		return ok;
	}

	/**
	 * Restituisce true se il secondo campo dell'impronta è
	 * conforme al modello di confronto, se non lo è
	 * restituisce false e prepara il messaggio d'errore.
	 *
	 * @param secondoCampo	secondo campo dell'impronta.
	 * @return ok 			Esito dell'esame dell'impronta.
	 */
	private boolean isOkSecondoCampoImpronta(String secondoCampo){
		boolean ok = true;

		for (int i=0; i<secondoCampo.length(); i++){
			if (!okCarattereSecondoCampo(i, secondoCampo.charAt(i))){
				ok = false;
			}
			if (!ok){
				break;
			}
		}

		return ok;
	}

	/**
	 * Restituisce true se il terzo campo dell'impronta è
	 * conforme al modello di confronto, se non lo è
	 * restituisce false e prepara il messaggio d'errore.
	 *
	 * @param terzoCampo	terzo campo dell'impronta.
	 * @return ok 			Esito dell'esame dell'impronta.
	 */
	private boolean isOkTerzoCampoImpronta(String terzoCampo){
		boolean ok = true;

		for (int i=0; i<terzoCampo.length(); i++){
			if (!okCarattereTerzoCampo(i, terzoCampo.charAt(i))){
				ok = false;
			}
			if (!ok){
				break;
			}
		}

		return ok;
	}

	/**
	 * Dato in input un singolo carattere con relativa posizione,
	 * del primo campo dell'impronta da esaminare, lo testa
	 * sulla base del modello di confronto per il primo campo
	 * dell'impronta.
	 * Restituisce true se l'esito è positivo, altrimenti
	 * restituisce false e prepara il messaggio d'errore.
	 *
	 * @param 	posizione posizione del singolo carattere del primo campo.
	 * @param 	ch singolo carattere del primo campo.
	 * @return 	true se l'esito è positivo, false se non lo è.
	 */
	private boolean okCaratterePrimoCampo(int posizione, char ch){
		boolean ok = true;
		String carattere = String.valueOf(ch);

		// Ammesso qualsiasi carattere tranne lo spazio
		if ( (posizione == 0) || (posizione == 1) ||
			(posizione == 2) || (posizione == 3) ){

			if ( carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Deve essere uno spazio
		if ( posizione == 4 ){
			if ( !carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Ammesso qualsiasi carattere tranne lo spazio
		if ( (posizione == 5) || (posizione == 6) ||
			(posizione == 7) || (posizione == 8) ){

			if ( carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Deve essere uno spazio
		if ( posizione == 9 ){
			if ( !carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		return ok;
	}// end okCaratterePrimoCampo


	/**
	 * Dato in input un singolo carattere con relativa posizione,
	 * del secondo campo dell'impronta da esaminare, lo testa
	 * sulla base del modello di confronto per il secondo campo
	 * dell'impronta.
	 * Restituisce true se l'esito è positivo, altrimenti
	 * restituisce false e prepara il messaggio d'errore.
	 *
	 * @param 	posizione posizione del singolo carattere del secondo campo.
	 * @param 	ch singolo carattere del secondo campo.
	 * @return 	true se l'esito è positivo, false se non lo è.
	 */
	private boolean okCarattereSecondoCampo(int posizione, char ch){
		boolean ok = true;
		String carattere = String.valueOf(ch);

		// Ammesso qualsiasi carattere tranne lo spazio
		if ( (posizione ==  0) || (posizione == 1) ||
			(posizione == 2) || (posizione == 3) ){

			if ( carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Deve essere uno spazio
		if ( posizione == 4 ){
			if ( !carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Ammesso qualsiasi carattere tranne lo spazio
		if ( (posizione == 5) || (posizione == 6) ||
			(posizione == 7) || (posizione == 8) ){

			if ( carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Deve essere uno spazio
		if ( posizione == 9 ){
			if ( !carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_ALFANUMERICI_SPAZIO);
			}
		}

		// Carattere speciale del secondo campo
		if ( (posizione == 10) || (posizione == 11) || (posizione == 12) ){
			if (posizione == 11){
				boolean trovato = false;
				// Controllo del carattere speciale che deve
				// essere uguale a uno tra quelli consentiti
				// presenti nel modello di confronto
				for (int i= 0; i<CARATTERE_SPECIALE_CAMPO2.length(); i++){
					String confronto = String
						.valueOf(CARATTERE_SPECIALE_CAMPO2.charAt(i));
					if ( carattere.equals(confronto) ) trovato = true;
					if (trovato) break;
				}
				if (!trovato){
					ok = false;
					setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO2);
				}
			} else {
				if ( (posizione==10)&&(!carattere.equals("(")) ){
					ok = false;
					setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO2);
				}
				if ( (posizione==12)&&(!carattere.equals(")")) ){
					ok = false;
					setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO2);
				}
			}
		}

		// Deve essere uno spazio
		if ( posizione == 13 ){
			if ( !carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO2);
			}
		}

		return ok;
	}// end okCarattereSecondoCampo


	/**
	 * Dato in input un singolo carattere con relativa posizione,
	 * del terzo campo dell'impronta da esaminare, lo testa
	 * sulla base del modello di confronto per il terzo campo
	 * dell'impronta.
	 * Restituisce true se l'esito è positivo, altrimenti
	 * restituisce false e prepara il messaggio d'errore.
	 *
	 * @param 	posizione posizione del singolo carattere del terzo campo.
	 * @param 	ch singolo carattere del terzo campo.
	 * @return 	true se l'esito è positivo, false se non lo è.
	 */
	private boolean okCarattereTerzoCampo(int posizione, char ch){
		boolean ok = true;
		String carattere = String.valueOf(ch);

		// Ammessi solo caratteri numerici (senza spazi)
		if ( (posizione == 0) || (posizione == 1) ||
			(posizione == 2) || (posizione == 3) ){

			if ( !isNumber(ch) ){
				ok = false;
				setMessaggioErrore(ERRORE_DATA_CAMPO3);
			}
		}

		// Deve essere uno spazio
		if ( posizione == 4 ){
			if ( !carattere.equals(SPAZIO) ){
				ok = false;
				setMessaggioErrore(ERRORE_DATA_CAMPO3);
			}
		}

		// Carattere speciale del terzo campo
		if ( (posizione == 5) || (posizione == 6) || (posizione == 7) ){
			if (posizione == 6){
				boolean trovato = false;
				// Controllo del carattere speciale che deve
				// essere uguale a uno tra quelli consentiti
				// presenti nel modello di confronto
				for (int i= 0; i<CARATTERE_SPECIALE_CAMPO3.length(); i++){
					String confronto = String
						.valueOf(CARATTERE_SPECIALE_CAMPO3.charAt(i));
					if ( carattere.equals(confronto) ) trovato = true;
					if (trovato) break;
				}
				if (!trovato){
					ok = false;
					setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO3);
				}
			} else {
				if ( (posizione==5)&&(!carattere.equals("(")) ){
					ok = false;
					setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO3);
				}
				if ( (posizione==7)&&(!carattere.equals(")")) ){
					ok = false;
					setMessaggioErrore(ERRORE_CH_SPECIALE_CAMPO3);
				}
			}
		}

		return ok;
	}// end okCarattereTerzoCampo


	/**
	 * Controlla se un dato carattere è o meno un numero
	 * compreso tra zero e nove.
	 *
	 * @param 	carattere carattere da verificare.
	 * @return	true se è un carattere numerico, altrimenti false.
	 */
	private boolean isNumber(char carattere){
		boolean trovato = false;
		String modello = "0123456789";

		for (int i=0; i<modello.length(); i++){
			if ( carattere == modello.charAt(i) ){
				trovato = true;
				break;
			}
		}

		return trovato;
	}


	// Metodo main per testare l'impronta.
//	public static void main(String[] args){
//		System.out.println("--- INIZIO TEST IMPRONTA ---");
//		String strImpronta = "n-e- an05 u-io maqu (C) 1597 (X)";
//		System.out.println("***"+strImpronta+"***");
//		Impronta impronta = new Impronta(strImpronta, null);
//
//		if (impronta.isOkImpronta()){
//			System.out.println("Impronta corretta");
//		} else {
//			System.out.println(""+impronta.getMessaggioErrore());
//		}
//		System.out.println("--- FINE TEST IMPRONTA ---");
//	}

}// end class Impronta
