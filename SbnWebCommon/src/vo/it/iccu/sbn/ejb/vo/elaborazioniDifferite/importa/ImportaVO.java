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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriTimestampRangeVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.List;

public class ImportaVO extends ParametriTimestampRangeVO {

	private static final long serialVersionUID = -7516589345197846012L;

	public static final String[] NATURE = new String[] { "M", "S", "C", "N" };

	public static final String[] MATERIALI = new String[] { "M", "E", "U", "G", "C" };

	private String codScaricoSelez;
	private boolean exportDB = true;
	private boolean ancheTitoli01 = false;
	private boolean soloDocLocali = false;

	private String descTitoloDa;
	private String descTitoloA;

	private String[] materiali; // tutto - moderno - musica - antico -

	private String tipoRecord1;
	private String tipoRecord2;
	private String tipoRecord3;

	private String[] nature; // monografie - periodici - collane -

	private String lingua1;
	private String lingua2;
	private String lingua3;

	private String paese1;
	private String paese2;

	private String tipoData;
	private String aaPubbFrom;
	private String aaPubbTo;

	private String ateneo;
	private List<BibliotecaVO> listaBib;

	// //// # tab dati catalografici
	// checkbox field
	// private boolean tuttoArchivio = false;

	private String classAlSimbolo;
	private String classDalSimbolo;
	private String classSistemaSelez;

	// //// # valori impostati dopo selezione da una lista esterna
	// es. da lista sezioni, collocazioni, specificazioni
	private String codPoloSez;
	private String codBibSez;
	private String possCodSez;
	private String tipoCollocazione;

	// archivio
	private String dateVar = ""; // tutto archivio - solo aggiornamenti

	private List<String> listaBID;

	private String codPoloSerie;
	private String codBibSerie;
	private String possSerie;

	private String possAllaCollocazione;
	private String possAlNumero;
	private String possDallaCollocazione;
	private String possDalNumero;
	private String possSpecificazioneCollA;
	private String possSpecificazioneCollDa;
	private String possTipoSezione;

	private String ticket;

//	private TipoEstrazioneUnimarc tipoEstrazione = null; // default -> lista documenti da

	/**
	 * @return Returns the possSpecificazione.
	 */
	/**
	 * @return Returns the classAlSimbolo.
	 */
	public String getClassAlSimbolo() {
		return classAlSimbolo;
	}

	/**
	 * @return Returns the classDalSimbolo.
	 */
	public String getClassDalSimbolo() {
		return classDalSimbolo;
	}

	/**
	 * @return Returns the classSistemaSelez.
	 */
	public String getClassSistemaSelez() {
		return classSistemaSelez;
	}

	/**
	 * @return Returns the codBibSez.
	 */
	public String getCodBibSez() {
		return codBibSez;
	}

	/**
	 * @return Returns the codPoloSez.
	 */
	public String getCodPoloSez() {
		return codPoloSez;
	}

	/**
	 * @return Returns the codScaricoSelez.
	 */
	public String getCodScaricoSelez() {
		return codScaricoSelez;
	}

	/**
	 * @return Returns the possCodSez.
	 */
	public String getPossCodSez() {
		return possCodSez;
	}

	/**
	 * @return Returns the dateVar.
	 */
	public String getDateVar() {
		return dateVar;
	}

	/**
	 * @return Returns the possAllaCollocazione.
	 */
	public String getPossAllaCollocazione() {
		return possAllaCollocazione;
	}

	/**
	 * @return Returns the possAlNumero.
	 */
	public String getPossAlNumero() {
		return possAlNumero;
	}

	/**
	 * @return Returns the possDallaCollocazione.
	 */
	public String getPossDallaCollocazione() {
		return possDallaCollocazione;
	}

	/**
	 * @return Returns the possDalNumero.
	 */
	public String getPossDalNumero() {
		return possDalNumero;
	}

	/**
	 * @return Returns the possSerie.
	 */
	public String getPossSerie() {
		return possSerie;
	}

	/**
	 * @return Returns the possSpecificazioneCollA.
	 */
	public String getPossSpecificazioneCollA() {
		return possSpecificazioneCollA;
	}

	/**
	 * @return Returns the possSpecificazioneCollDa.
	 */
	public String getPossSpecificazioneCollDa() {
		return possSpecificazioneCollDa;
	}

	/**
	 * @return Returns the ancheTitoli01.
	 */
	public boolean isAncheTitoli01() {
		return ancheTitoli01;
	}

	public boolean isExportDB() {
		return exportDB;
	}

	/**
	 * @return Returns the soloDocLocali.
	 */
	public boolean isSoloDocLocali() {
		return soloDocLocali;
	}

	/**
	 * @param ancheTitoli01
	 *            The ancheTitoli01 to set.
	 */
	public void setAncheTitoli01(boolean ancheTitoli01) {
		this.ancheTitoli01 = ancheTitoli01;
	}

	/**
	 * @param classAlSimbolo
	 *            The classAlSimbolo to set.
	 */
	public void setClassAlSimbolo(String classAlSimbolo) {
		this.classAlSimbolo = classAlSimbolo;
	}

	/**
	 * @param classDalSimbolo
	 *            The classDalSimbolo to set.
	 */
	public void setClassDalSimbolo(String classDalSimbolo) {
		this.classDalSimbolo = classDalSimbolo;
	}

	/**
	 * @param classSistemaSelez
	 *            The classSistemaSelez to set.
	 */
	public void setClassSistemaSelez(String classSistemaSelez) {
		this.classSistemaSelez = classSistemaSelez;
	}

	/**
	 * @param codBibSez
	 *            The codBibSez to set.
	 */
	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	/**
	 * @param codPoloSez
	 *            The codPoloSez to set.
	 */
	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}

	/**
	 * @param codScaricoSelez
	 *            The codScaricoSelez to set.
	 */
	public void setCodScaricoSelez(String codScaricoSelez) {
		this.codScaricoSelez = codScaricoSelez;
	}

	/**
	 * @param possCodSez
	 *            The possCodSez to set.
	 */
	public void setPossCodSez(String codSez) {
		this.possCodSez = codSez;//trimOrFill(codSez, ' ', 10);
	}

	/**
	 * @param dateVar
	 *            The dateVar to set.
	 */
	public void setDateVar(String dateVar) {
		this.dateVar = dateVar;
	}

	public void setExportDB(boolean exportDB) {
		this.exportDB = exportDB;
	}

	/**
	 * @param possAllaCollocazione
	 *            The possAllaCollocazione to set.
	 */
	public void setPossAllaCollocazione(String possAllaCollocazione) {
		this.possAllaCollocazione = trimAndSet(possAllaCollocazione);
	}

	/**
	 * @param possAlNumero
	 *            The possAlNumero to set.
	 */
	public void setPossAlNumero(String possAlNumero) {
		this.possAlNumero = trimAndSet(possAlNumero);
	}

	/**
	 * @param possDallaCollocazione
	 *            The possDallaCollocazione to set.
	 */
	public void setPossDallaCollocazione(String possDallaCollocazione) {
		this.possDallaCollocazione = trimAndSet(possDallaCollocazione);
	}

	/**
	 * @param possDalNumero
	 *            The possDalNumero to set.
	 */
	public void setPossDalNumero(String possDalNumero) {
		this.possDalNumero = trimAndSet(possDalNumero);
	}

	/**
	 * @param possSerie
	 *            The possSerie to set.
	 */
	public void setPossSerie(String possSerie) {
		this.possSerie = trimOrFill(possSerie, ' ', 3);
	}

	/**
	 * @param possSpecificazioneCollA
	 *            The possSpecificazioneCollA to set.
	 */
	public void setPossSpecificazioneCollA(String possSpecificazioneCollA) {
		this.possSpecificazioneCollA = trimAndSet(possSpecificazioneCollA);
	}

	/**
	 * @param possSpecificazioneCollDa
	 *            The possSpecificazioneCollDa to set.
	 */
	public void setPossSpecificazioneCollDa(String possSpecificazioneCollDa) {
		this.possSpecificazioneCollDa = trimAndSet(possSpecificazioneCollDa);
	}

	/**
	 * @param soloDocLocali
	 *            The soloDocLocali to set.
	 */
	public void setSoloDocLocali(boolean soloDocLocali) {
		this.soloDocLocali = soloDocLocali;
	}

	/**
	 * @param tuttoArchivio
	 *            The tuttoArchivio to set.
	 */
	public void setTuttoArchivio(boolean tuttoArchivio) {
		// this.tuttoArchivio = tuttoArchivio;
		if (tuttoArchivio)
			this.setDateVar("t");
		else
			this.setDateVar("s");
	}

/*	public TipoEstrazioneUnimarc getTipoEstrazione() {
		return tipoEstrazione;
	}

	public void setTipoEstrazione(TipoEstrazioneUnimarc tipoEstrazione) {
		this.tipoEstrazione = tipoEstrazione;
	}

	public String getTipoEstrazioneJSP() {
		if (tipoEstrazione == null)
			return "";
		return tipoEstrazione.name();
	}

	public void setTipoEstrazioneJSP(String tipoEstrazione) {
		this.tipoEstrazione = TipoEstrazioneUnimarc.valueOf(tipoEstrazione);
	}*/

	public List<String> getListaBID() {
		return listaBID;
	}

	public void setListaBID(List<String> listaBid) {
		this.listaBID = listaBid;
	}

	public List<BibliotecaVO> getListaBib() {
		return listaBib;
	}

	public void setListaBib(List<BibliotecaVO> listaBib) {
		this.listaBib = listaBib;
	}

	public boolean isIncrementale() {
		return (tsVar_da != null || tsVar_a != null);
	}

	public boolean isSoloPosseduto() {
		return isFilled(listaBib);
	}

	public boolean isRangeCollocazioni() {
		return length(possCodSez) > 0 &&
			isFilled(codPoloSez) &&
			isFilled(codBibSez)
//			&&
//			isFilled(tipoCollocazione)
			;
	}

	public boolean isRangeInventari() {
		return length(possSerie) > 0 &&
			isFilled(codPoloSerie) &&
			isFilled(codBibSerie) &&
			isFilled(possDalNumero) &&
			isFilled(possAlNumero);
	}

	private void validateRangeCollocazioni() throws ValidationException {

		if (length(possCodSez) < 1)
			return;	//non valido le collocazioni

		if (!isFilled(codPoloSez) || !isFilled(codBibSez) || !isFilled(tipoCollocazione) )
			throw new ValidationException(SbnErrorTypes.GDF_CODICE_SEZIONE_NON_VERIFICATO);

		CodiciNormalizzatiVO collSpec = null;
		try {
			collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(tipoCollocazione,
					possDallaCollocazione,
					possAllaCollocazione,
					false,
					possSpecificazioneCollDa,
					possSpecificazioneCollA,
					false,
					null);

		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.GDF_RANGE_COLLOCAZIONI_ERRATO);
		}

		String from = collSpec.getDaColl() + collSpec.getDaSpec();
		String to = collSpec.getAColl() + collSpec.getASpec();
		if ( from.compareTo(to) > 0)
			throw new ValidationException(SbnErrorTypes.GDF_RANGE_COLLOCAZIONI_ERRATO);

		if (ValidazioneDati.size(listaBib) != 1)
			throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);
	}

	private void validateRangeInventari() throws ValidationException {

		if (!isFilled(possDalNumero) && !isFilled(possAlNumero))
			return;

		if (ValidazioneDati.size(listaBib) != 1)
			throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);

		if (isFilled(possDalNumero)) {
			if (isNumeric(possDalNumero)) {
				if (length(possDalNumero) > 9) {
					throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventDaEccedente");
				}
			} else {
				throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventDaDeveEssereNumerico");
			}
		} else {
			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "errInvObbl");
		}
		if (isFilled(possAlNumero)) {
			if (isNumeric(possAlNumero)) {
				if (length(possAlNumero) > 9) {
					throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventAEccedente");
				}
			} else {
				throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventADeveEssereNumerico");
			}
		} else {
			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "errInvObbl");
		}
		if (isFilled(possDalNumero) && isFilled(possAlNumero)) {
			if (Integer.valueOf(possAlNumero) < Integer.valueOf(possDalNumero)) {
				throw new ValidationException(SbnErrorTypes.GDF_GENERIC,
						"codInventDaDeveEssereMinoreDiCodInventA");
			}
		} else {
			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "ERRORE: codInvent = null");
		}
		if (isFilled(possDalNumero) && !possDalNumero.equals("0")
				&& isNumeric(possDalNumero)
				&& Integer.valueOf(possDalNumero) >= 0) {
		} else {
			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "indicareIDueEstremiDellIntervallo");
		}
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		//validazione anni pubblicazione
		if (isFilled(aaPubbFrom)) {
			if (!isNumeric(aaPubbFrom) || aaPubbFrom.length() != 4)	//non é un anno valido
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
		}

		if (isFilled(aaPubbTo)) {
			if (!isNumeric(aaPubbTo) || aaPubbTo.length() != 4)	//non é un anno valido
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
		}

		if (isFilled(aaPubbFrom) && isFilled(aaPubbTo)) {
			if (Integer.valueOf(aaPubbTo) < Integer.valueOf(aaPubbFrom))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);
		}

//		if (!isFilled(nature) && tipoEstrazione != TipoEstrazioneUnimarc.FILE)
//			throw new ValidationException(SbnErrorTypes.UNI_ALMENO_UNA_NATURA);

		if (isFilled(ateneo) && isFilled(listaBib) )
			throw new ValidationException(SbnErrorTypes.UNI_ESTRAZIONE_BIB_ATENEO);

		//almaviva5_20100407 validazione range posseduto
//		if (tipoEstrazione != TipoEstrazioneUnimarc.FILE) {
//			validateRangeCollocazioni();
//			validateRangeInventari();
//
//			if (isRangeCollocazioni() && isRangeInventari())
//				throw new ValidationException(SbnErrorTypes.UNI_SPECIFICARE_SOLO_UN_RANGE_POSSEDUTO);
//
//		}

		//almaviva5_20101109 #3977
//		if (tipoEstrazione == TipoEstrazioneUnimarc.FILE && !isFilled(listaBID))
//			throw new ValidationException(SbnErrorTypes.UNI_FILE_BID_NON_PRESENTE);
	}

	public String getAaPubbFrom() {
		return aaPubbFrom;
	}

	public void setAaPubbFrom(String aaPubbFrom) {
		this.aaPubbFrom = trimAndSet(aaPubbFrom);
	}

	public String getAaPubbTo() {
		return aaPubbTo;
	}

	public void setAaPubbTo(String aaPubbTo) {
		this.aaPubbTo = trimAndSet(aaPubbTo);
	}

	public String[] getMateriali() {
		return materiali;
	}

	public void setMateriali(String[] materiali) {
		this.materiali = materiali;
	}

	public String[] getNature() {
		return nature;
	}

	public void setNature(String[] nature) {
		this.nature = nature;
	}

	public String getTipoRecord1() {
		return tipoRecord1;
	}

	public void setTipoRecord1(String tipoRecord1) {
		this.tipoRecord1 = tipoRecord1;
	}

	public String getTipoRecord2() {
		return tipoRecord2;
	}

	public void setTipoRecord2(String tipoRecord2) {
		this.tipoRecord2 = tipoRecord2;
	}

	public String getTipoRecord3() {
		return tipoRecord3;
	}

	public void setTipoRecord3(String tipoRecord3) {
		this.tipoRecord3 = tipoRecord3;
	}

	public String getLingua1() {
		return lingua1;
	}

	public void setLingua1(String lingua1) {
		this.lingua1 = lingua1;
	}

	public String getLingua2() {
		return lingua2;
	}

	public void setLingua2(String lingua2) {
		this.lingua2 = lingua2;
	}

	public String getLingua3() {
		return lingua3;
	}

	public void setLingua3(String lingua3) {
		this.lingua3 = lingua3;
	}

	public String getPaese1() {
		return paese1;
	}

	public void setPaese1(String paese1) {
		this.paese1 = paese1;
	}

	public String getPaese2() {
		return paese2;
	}

	public void setPaese2(String paese2) {
		this.paese2 = paese2;
	}

	public String getTipoData() {
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public String getAteneo() {
		return ateneo;
	}

	public void setAteneo(String ateneo) {
		this.ateneo = trimAndSet(ateneo);
	}

	public String getDescTitoloDa() {
		return descTitoloDa;
	}

	public void setDescTitoloDa(String descTitoloDa) {
		this.descTitoloDa = trimAndSet(descTitoloDa);
	}

	public String getDescTitoloA() {
		return descTitoloA;
	}

	public void setDescTitoloA(String descTitoloA) {
		this.descTitoloA = trimAndSet(descTitoloA);
	}

	public String getPossTipoSezione() {
		return possTipoSezione;
	}

	public void setPossTipoSezione(String possTipoSezione) {
		this.possTipoSezione = trimAndSet(possTipoSezione);
	}

	public String getTipoCollocazione() {
		return tipoCollocazione;
	}

	public void setTipoCollocazione(String tipoColl) {
		this.tipoCollocazione = trimAndSet(tipoColl);
	}

	public String getCodPoloSerie() {
		return codPoloSerie;
	}

	public void setCodPoloSerie(String codPoloSerie) {
		this.codPoloSerie = codPoloSerie;
	}

	public String getCodBibSerie() {
		return codBibSerie;
	}

	public void setCodBibSerie(String codBibSerie) {
		this.codBibSerie = codBibSerie;
	}

	public void clearFiltriPosseduto() {
		this.codPoloSez = null;
		this.codBibSez = null;
		this.possCodSez = null;
		this.tipoCollocazione = null;

		this.codPoloSerie = null;
		this.codBibSerie = null;
		this.possSerie = null;

		this.possAllaCollocazione = null;
		this.possAlNumero = null;
		this.possDallaCollocazione = null;
		this.possDalNumero = null;
		this.possSpecificazioneCollA = null;
		this.possSpecificazioneCollDa = null;
		this.possTipoSezione = null;
	}





	// inizio nuovi campi IMPORT

	private int stato = 0;
	public static final int _STATO_CARICA_RECORD = 0;
	public static final int _STATO_UNI_001 = 1;
	public static final int _STATO_UNI_950 = 2;
	public static final int _STATO_VERIFICA_BID = 3;

	// Inizio Modifica almaviva2 27.09.2011 - Classe per il trattamento dei dati bibliografici/semantici della tabella import1
//	public static final int _STATO_CARICAMENTO_BIBLIO_SEMANTICO = 9;

	public static final int _STATO_UNI_700 = 700;
	public static final int _STATO_UNI_410 = 410;
	public static final int _STATO_UNI_500 = 500;
	public static final int _STATO_UNI_600 = 600;
	public static final int _STATO_UNI_200 = 200;
	public static final int _STATO_UNI_1000 = 1000;
	// Fine Modifica almaviva2 27.09.2011 - Classe per il trattamento dei dati bibliografici/semantici della tabella import1

	// Modifica almaviva2 09.07.2012 - Inserimento nuovo valore per richiesta cancellazione tabelle lavoro per nr_richiesta
	public static final int _STATO_PER_CANCELLAZIONE = 999;



	public int getStatoImport() {
		return stato;
	}
	public void setStatoImport(int stato) {
		this.stato = stato;
	}

	private List<String> queries;
	private String importFileName;
	private String numRichiesta;
	private String numRichiestaVerificaBid;
	private String numRichiestaCaricamento;
	private String numRichiestaCancellazione;
	private String etichettaUnimarc;
	private boolean isRicercaInPolo = true; //default polo

	public void setQueries(List<String> queries){
		this.queries = queries;
	}
	public List<String> getQueries(){
		return this.queries;
	}

	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	public String getNumRichiesta() {
		return numRichiesta;
	}
	public void setNumRichiesta(String numRichiesta) {
		this.numRichiesta = numRichiesta;
	}

	public String getNumRichiestaVerificaBid() {
		return numRichiestaVerificaBid;
	}
	public void setNumRichiestaVerificaBid(String numRichiestaVerificaBid) {
		this.numRichiestaVerificaBid = numRichiestaVerificaBid;
	}

	public String getEtichettaUnimarc() {
		return etichettaUnimarc;
	}
	public void setEtichettaUnimarc(String etichettaUnimarc) {
		this.etichettaUnimarc = etichettaUnimarc;
	}

	public boolean isRicercaInPolo() {
		return isRicercaInPolo;
	}
	public void setRicercaInPolo(boolean isRicercaInPolo) {
		this.isRicercaInPolo = isRicercaInPolo;
	}

	public String getNumRichiestaCaricamento() {
		return numRichiestaCaricamento;
	}

	public void setNumRichiestaCaricamento(String numRichiestaCaricamento) {
		this.numRichiestaCaricamento = numRichiestaCaricamento;
	}

	public String getNumRichiestaCancellazione() {
		return numRichiestaCancellazione;
	}

	public void setNumRichiestaCancellazione(String numRichiestaCancellazione) {
		this.numRichiestaCancellazione = numRichiestaCancellazione;
	}
}
