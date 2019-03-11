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

package it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta;


import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.util.List;

public class EsportaVO extends ExportCommonVO {

	private static final long serialVersionUID = -7516589345197846012L;

	public static final String[] NATURE = new String[] { "M", "S", "C", "N", "R" };

	// Manutenzione Febbraio 2018 - gestione nuovi materiale Elettronico e Audiovisivo ("H", "L")
	public static final String[] MATERIALI = new String[] { "M", "E", "U", "G", "C", "H", "L" };

	private String codScaricoSelez;

	private boolean ancheTitoli01 = false;
	private boolean soloDocLocali = false;
	private boolean soloDocCondivisi = false;
	private boolean soloDocPosseduti = false;


	// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
	// gli estremi di filtro non sono più solo per titolo ma la tabella su cui applicare dipende dal catalogo selezionato
	private String descTitoloDa;
	private String descTitoloA;
//	private String descAutoreDa;
//	private String descAutoreA;
	private String tipoCatalogo;
	private String catalogoSelezDa;
	private String catalogoSelezA;



	private String[] materiali; // tutto - moderno - musica - antico -

	private String tipoRecord1;
	private String tipoRecord2;
	private String tipoRecord3;

	private String[] nature; // monografie - periodici - collane -

	private String lingua1;
	private String lingua2;
	private String lingua3;
	private boolean soloLinguaNoItaliano = false;

	private boolean soloPaeseNoItalia = false;


	private String tipoData;
	private String aaPubbFrom;
	private String aaPubbTo;

	// Intervento L. almaviva2 06.07.2011 - Bug MANTIS 4510 (collaudo) vengono dichiarati i campi da utilizzare per
	// filtrare per data ts_var e data ts_ins (il filtro su data ins e data var ammette solo 4 chr, serve gg/mm/aaaa)
	private Timestamp dataInsFrom = null;
	private Timestamp dataInsTo = null;
	private Timestamp dataAggFrom = null;
	private Timestamp dataAggTo = null;
	// Manutenzione almaviva2 21.09.2011 -  inserite le chiamate/nuovi campi per la lentina del biblotecario (ute ins e var)
	// richiamata dall'estrattore magno

	// Intervento del 25.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	// Modifiche per attivazione stampa catalogo per Soggetti e Classi
	private String codSoggettario;
	private String codSistemaClassificazione;

	private String uteIns;
	private String uteVar;


	private String ateneo;
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


	private String codTipoFruizione;
	private String codRip;
	private String codNoDispo;
	private String codiceStatoConservazione;

	protected TipoEstrazioneUnimarc tipoEstrazione = TipoEstrazioneUnimarc.ARCHIVIO; // default -> lista documenti da

	//almaviva5_20121112 evolutive google
	protected TipoOutput tipoOutput = TipoOutput.BID;
	protected TipoOutput tipoInput = TipoOutput.BID;

	private String etichette;

	//almaviva5_20130904 evolutive google2
	private String digitalizzati;
	private boolean escludiDigit;
	private String tipoDigit;

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

	public TipoEstrazioneUnimarc getTipoEstrazione() {
		return tipoEstrazione;
	}

	public void setTipoEstrazione(TipoEstrazioneUnimarc tipoEstrazione) {
		this.tipoEstrazione = tipoEstrazione;
	}

	public List<String> getListaBID() {
		return listaBID;
	}

	public void setListaBID(List<String> listaBid) {
		this.listaBID = listaBid;
	}

	public boolean isIncrementale() {
		return (tsVar_da != null || tsVar_a != null);
	}

	public boolean isSoloPosseduto() {
		return isFilled(getListaBib());
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

	protected void validateRangeCollocazioni() throws ValidationException {

		if (length(possCodSez) < 1)
			return;	//non valido le collocazioni

		if (!isFilled(codPoloSez) || !isFilled(codBibSez) || !isFilled(tipoCollocazione) )
			throw new ValidationException(SbnErrorTypes.GDF_CODICE_SEZIONE_NON_VERIFICATO);

		CodiciNormalizzatiVO collSpec = null;
		try {
			collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(tipoCollocazione,
					possDallaCollocazione,
					possAllaCollocazione,
					possSpecificazioneCollDa,
					possSpecificazioneCollA,
					null);

		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.GDF_RANGE_COLLOCAZIONI_ERRATO);
		}

		String from = collSpec.getDaColl() + collSpec.getDaSpec();
		String to = collSpec.getAColl() + collSpec.getASpec();
		if ( from.compareTo(to) > 0)
			throw new ValidationException(SbnErrorTypes.GDF_RANGE_COLLOCAZIONI_ERRATO);

		if (ValidazioneDati.size(getListaBib()) != 1)
			throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);
	}

	protected void validateRangeInventari() throws ValidationException {

		if (!isFilled(possDalNumero) && !isFilled(possAlNumero))
			return;

		if (ValidazioneDati.size(getListaBib()) != 1)
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


		String testoErr="";
		//validazione anni pubblicazione
		if (isFilled(aaPubbFrom)) {
			// marzo 2017 Evolutiva per adeguare la ricerca con filtro per data al nuovo formato delle date che ora possono
			// contenere il carattere"." nel terzo e quarto carattere
//			if (!isNumeric(aaPubbFrom) || aaPubbFrom.length() != 4)	//non é un anno valido
//				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			testoErr = DateUtil.verificaFormatoDataParziale(aaPubbFrom, "Data1Da");
			if (testoErr.length()>0) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			}
			if (aaPubbFrom.endsWith(".")) {
				aaPubbFrom = aaPubbFrom.replace('.', '0');
			}
		}

		if (isFilled(aaPubbTo)) {
			// marzo 2017 Evolutiva per adeguare la ricerca con filtro per data al nuovo formato delle date che ora possono
			// contenere il carattere"." nel terzo e quarto carattere
//			if (!isNumeric(aaPubbTo) || aaPubbTo.length() != 4)	//non é un anno valido
//				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			testoErr = DateUtil.verificaFormatoDataParziale(aaPubbTo, "Data1A");
			if (testoErr.length() > 0) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			}
			if (aaPubbTo.endsWith(".")) {
				aaPubbTo = aaPubbTo.replace('.', '9');
			}
		}

		if (isFilled(aaPubbFrom) && isFilled(aaPubbTo)) {
			if (Integer.valueOf(aaPubbTo) < Integer.valueOf(aaPubbFrom))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);
		}

		if (!isFilled(nature) && tipoEstrazione != TipoEstrazioneUnimarc.FILE)
			throw new ValidationException(SbnErrorTypes.UNI_ALMENO_UNA_NATURA);

		if (isFilled(ateneo) && isFilled(getListaBib()) )
			throw new ValidationException(SbnErrorTypes.UNI_ESTRAZIONE_BIB_ATENEO);

		if (!isFilled(codScaricoSelez) )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "label.etichetteelenco");

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
		if (tipoEstrazione == TipoEstrazioneUnimarc.FILE)
			if (!isFilled(listaBID) && !isFilled(inputFile))
				throw new ValidationException(SbnErrorTypes.UNI_FILE_ID_NON_PRESENTE);


		// Inizio Modifiche per Estrattore Magno almaviva2 marzo 2011
		if (isSoloLinguaNoItaliano()) {
			this.lingua1 = "";
			this.lingua2 = "";
			this.lingua3 = "";
		}
		if (isSoloPaeseNoItalia()) {
			this.setPaese1("");
			this.setPaese2("");
		}
		// Fine Modifiche per Estrattore Magno almaviva2 marzo 2011

		//almaviva5_20130424 segnalazione ICCU
		if (soloDocCondivisi && soloDocLocali)
			throw new ValidationException(SbnErrorTypes.UNI_SOLO_LOCALI_O_CONDIVISI);

	}


	// Intervento L. almaviva2 06.07.2011 - Bug MANTIS 4510 (collaudo) vengono dichiarati i campi da utilizzare per
	// filtrare per data ts_var e data ts_ins (il filtro su data ins e data var ammette solo 4 chr, serve gg/mm/aaaa)
	public void validaDateTimeStamp(String tipoTimeStamp, String dataFrom, String dataTo) throws ValidationException {

		Timestamp from = null;
		Timestamp to = null;

		if (tipoTimeStamp.equals("INS")) {
			if (isFilled(dataFrom) && ValidazioneDati.validaData(dataFrom) != ValidazioneDati.DATA_OK)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

			if (isFilled(dataTo) && ValidazioneDati.validaData(dataTo) != ValidazioneDati.DATA_OK)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

			from = DateUtil.toTimestamp(dataFrom);
			to = DateUtil.toTimestampA(dataTo);

			if (isFilled(dataFrom) && isFilled(dataTo) )
				if (to.before(from))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

			dataInsFrom = from;
			dataInsTo = to;
		}

		if (tipoTimeStamp.equals("AGG")) {
			if (isFilled(varFrom) && ValidazioneDati.validaData(varFrom) != ValidazioneDati.DATA_OK)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

			if (isFilled(varTo)	&& ValidazioneDati.validaData(varTo) != ValidazioneDati.DATA_OK)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

			from = DateUtil.toTimestamp(varFrom);
			to = DateUtil.toTimestampA(varTo);

			if (isFilled(varFrom) && isFilled(varTo) )
				if (to.before(from))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

			dataAggFrom = from;
			dataAggTo = to;
		}
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

	public boolean isSoloDocCondivisi() {
		return soloDocCondivisi;
	}

	public void setSoloDocCondivisi(boolean soloDocCondivisi) {
		this.soloDocCondivisi = soloDocCondivisi;
	}

	public boolean isSoloLinguaNoItaliano() {
		return soloLinguaNoItaliano;
	}

	public void setSoloLinguaNoItaliano(boolean soloLinguaNoItaliano) {
		this.soloLinguaNoItaliano = soloLinguaNoItaliano;
	}

	public boolean isSoloPaeseNoItalia() {
		return soloPaeseNoItalia;
	}

	public void setSoloPaeseNoItalia(boolean soloPaeseNoItalia) {
		this.soloPaeseNoItalia = soloPaeseNoItalia;
	}

	public String getCodTipoFruizione() {
		return codTipoFruizione;
	}

	public void setCodTipoFruizione(String codTipoFruizione) {
		this.codTipoFruizione = codTipoFruizione;
	}

	public String getCodRip() {
		return codRip;
	}

	public void setCodRip(String codRip) {
		this.codRip = codRip;
	}

	public String getCodNoDispo() {
		return codNoDispo;
	}

	public void setCodNoDispo(String codNoDispo) {
		this.codNoDispo = codNoDispo;
	}

	public String getCodiceStatoConservazione() {
		return codiceStatoConservazione;
	}

	public void setCodiceStatoConservazione(String codiceStatoConservazione) {
		this.codiceStatoConservazione = codiceStatoConservazione;
	}

	public Timestamp getDataInsFrom() {
		return dataInsFrom;
	}

	public void setDataInsFrom(Timestamp dataInsFrom) {
		this.dataInsFrom = dataInsFrom;
	}

	public Timestamp getDataInsTo() {
		return dataInsTo;
	}

	public void setDataInsTo(Timestamp dataInsTo) {
		this.dataInsTo = dataInsTo;
	}

	public Timestamp getDataAggFrom() {
		return dataAggFrom;
	}

	public void setDataAggFrom(Timestamp dataAggFrom) {
		this.dataAggFrom = dataAggFrom;
	}

	public Timestamp getDataAggTo() {
		return dataAggTo;
	}

	public void setDataAggTo(Timestamp dataAggTo) {
		this.dataAggTo = dataAggTo;
	}

	public String getUteIns() {
		return uteIns;
	}

	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}

	public String getUteVar() {
		return uteVar;
	}

	public void setUteVar(String uteVar) {
		this.uteVar = uteVar;
	}

	public String getTipoCatalogo() {
		return trimOrEmpty(tipoCatalogo);
	}

	public void setTipoCatalogo(String tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}

	public String getCatalogoSelezDa() {
		return catalogoSelezDa;
	}

	public void setCatalogoSelezDa(String catalogoSelezDa) {
		this.catalogoSelezDa = trimAndSet(catalogoSelezDa);
	}

	public String getCatalogoSelezA() {
		return catalogoSelezA;
	}

	public void setCatalogoSelezA(String catalogoSelezA) {
		this.catalogoSelezA = trimAndSet(catalogoSelezA);
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

	public TipoOutput getTipoOutput() {
		return tipoOutput;
	}

	public void setTipoOutput(TipoOutput tipoOutput) {
		this.tipoOutput = tipoOutput;
	}

	public TipoOutput getTipoInput() {
		return tipoInput;
	}

	public void setTipoInput(TipoOutput tipoInput) {
		this.tipoInput = tipoInput;
	}

	public String getEtichette() {
		return etichette;
	}

	public void setEtichette(String etichette) {
		this.etichette = etichette;
	}

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	public String getCodSistemaClassificazione() {
		return codSistemaClassificazione;
	}

	public void setCodSistemaClassificazione(String codSistemaClassificazione) {
		this.codSistemaClassificazione = codSistemaClassificazione;
	}

	public String getDigitalizzati() {
		return digitalizzati;
	}

	public void setDigitalizzati(String digitalizzati) {
		this.digitalizzati = trimAndSet(digitalizzati);
	}

	public boolean isEscludiDigit() {
		return escludiDigit;
	}

	public void setEscludiDigit(boolean escludiDigit) {
		this.escludiDigit = escludiDigit;
	}

	public String getTipoDigit() {
		return tipoDigit;
	}

	public void setTipoDigit(String tipoDigit) {
		this.tipoDigit = tipoDigit;
	}

	public boolean isSoloDocPosseduti() {
		return soloDocPosseduti;
	}

	public void setSoloDocPosseduti(boolean soloDocPosseduti) {
		this.soloDocPosseduti = soloDocPosseduti;
	}

}
