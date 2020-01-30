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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;


import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.UtilityDate;
import it.iccu.sbn.ejb.model.unimarcmodel.Ac_210Type;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C205;
import it.iccu.sbn.ejb.model.unimarcmodel.C208;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndIncipit;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoNota321;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioIncipitVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.util.List;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

/**
 * @author Maurizio Alvino
 *
 */
public class UtilityCampiTitolo {

	UtilityCastor utilityCastor = new UtilityCastor();

	public UtilityCampiTitolo() {
		super();
	}

	public static final String IID_STRINGAVUOTA = "";
	private String tipoAuthority = "TITOLO";
	private int numLegami = 0;
	private int numArriviLegame = 0;
	private LegamiType[] legamiType = null;
	private List[] arrivoLegami = null;

	private String natura = "";
	private String bid = "";
	private String tipoMateriale = "";
	private String livelloAut = "";
	private String paese = "";
	private String titoloIndicazioneResponsabilita = "";
	private String tipoData = "";
	private String tipoRecord = "";
	private String livelloBibliografico = "";
	private String versione = "";
	private String dataInserimento = "";
	private String data1 = "";
	private String data2 = "";
	private String areaEdizione = "";
	private String areaNumerazione = "";
	private String areaPubblicazione = "";
	private String areaDescrizioneFisica = "";
	private String areaNote = "";

//	GESTIONE DELLE NOTE AGGIUNTIVE  3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
	private String areaNote323 = "";
	private String areaNote327 = "";
	private String areaNote330 = "";
	private String areaNote336 = "";
	private String areaNote337 = "";
	private String areaNoteDATA = "";
	private String areaNoteORIG = "";
	private String areaNoteFILI = "";
	private String areaNotePROV = "";
	private String areaNotePOSS = "";
//	END GESTIONE DELLE NOTE AGGIUNTIVE  3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274


//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	private String tipoTestoLetterario = "";

	private String formaContenuto = "";
	private String tipoContenuto = "";
	private String movimento = "";
	private String dimensione = "";
	private String sensorialita1 = "";
	private String sensorialita2 = "";
	private String sensorialita3 = "";
	private String tipoMediazione = "";

	private String formaContenutoBIS = "";
	private String tipoContenutoBIS = "";
	private String movimentoBIS = "";
	private String dimensioneBIS = "";
	private String sensorialitaBIS1 = "";
	private String sensorialitaBIS2 = "";
	private String sensorialitaBIS3 = "";
	private String tipoMediazioneBIS = "";
//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Fine definizione/Gestione Nuovi campi

	// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
	// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
	private String pubblicatoSiNo = "";

	// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
	private String tipoSupporto = "";
	private String tipoSupportoBIS = "";

//  almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private String livelloAutAudiov = "";
	private String tipoVideo = "";
	private String lunghezza = "";
	private String indicatoreColore = "";
	private String indicatoreSuono = "";
	private String supportoSuono = "";
	private String larghezzaDimensioni = "";
	private String formaPubblDistr = "";
	private String tecnicaVideoFilm = "";
	private String presentImmagMov = "";
	private String[] arrMaterAccompagn = new String[4];
	private String pubblicVideoreg = "";
	private String presentVideoreg = "";
	private String materialeEmulsBase = "";
	private String materialeSupportSec = "";
	private String standardTrasmiss = "";
	private String versioneAudiovid = "";
	private String elementiProd = "";
	private String specCatColoreFilm = "";
	private String emulsionePellic = "";
	private String composPellic = "";
	private String suonoImmagMovimento = "";
	private String tipoPellicStampa = "";

	private String tipoTestoRegSonora = "";

	private String formaPubblicazioneDisco = "";
	private String velocita = "";
	private String tipoSuono = "";
	private String larghezzaScanal = "";
	private String dimensioni = "";
	private String larghezzaNastro = "";
	private String configurazNastro = "";
	private String[] arrMaterTestAccompagn = new String[6];
	private String tecnicaRegistraz = "";
	private String specCarattRiprod = "";
	private String datiCodifRegistrazSonore = "";
	private String tipoDiMateriale = "";
	private String tipoDiTaglio = "";
	private String durataRegistraz = "";

	private String livelloAutElettr = "";
//  almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

	// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
	// Gestione nuovi campi specifici per etichetta 135
	private String tipoRisorsaElettronica = "";
	private String indicazioneSpecificaMateriale = "";
	private String coloreElettronico = "";
	private String dimensioniElettronico = "";
	private String suonoElettronico = "";




	private String fonteRecord = "";

	private String norme = "";
	private String agenzia = "";

	private String uriDigitalBorn = "";

	private String notaInformativa = "";
	private String notaCatalogatore = "";

	private String numeroOrdine = "";
	private String numeroOpera = "";
	private String numeroCatalogoTematico = "";
	private String datazione = "";
	private String tonalita = "";
	private String sezioni = "";
	private String titoloOrdinamento = "";
	private String titoloEstratto = "";
	private String titoloAppellativo = "";
	private String organicoSintetico = "";
	private String organicoAnalitico = "";
	private String[] formaMusicale = new String[3];

	/**  DOCUMENT ME! */
	private String areaMusica = "";

	/**  DOCUMENT ME! */
	private String tipoTesto = "";

	/**  DOCUMENT ME! */
	private String tipoElaborazione = "";

	/**  DOCUMENT ME! */
	private String stesura = "";

	/**  DOCUMENT ME! */
	private String composito = "";

	/**  DOCUMENT ME! */
	private String palinsesto = "";

	/**  DOCUMENT ME! */
	private String presentazione = "";

	/**  DOCUMENT ME! */
	private String materia = "";

	/**  DOCUMENT ME! */
	private String illustrazioni = "";

	/**  DOCUMENT ME! */
	private String notazioneMusicale = "";

	/**  DOCUMENT ME! */
	private String legatura = "";

	/**  DOCUMENT ME! */
	private String conservazione = "";

	/**  DOCUMENT ME! */
	private String genereRappresentazione = "";

	/**  DOCUMENT ME! */
	private String annoRappresentazione = "";

	/**  DOCUMENT ME! */
	private String periodoRappresentazione = "";

	/**  DOCUMENT ME! */
	private String sedeRappresentazione = "";

	/**  DOCUMENT ME! */
	private String luogoRappresentazione = "";

	/**  DOCUMENT ME! */
	private String notaRappresentazione = "";

	/**  DOCUMENT ME! */
	private String occasioneRappresentazione = "";

	/**  DOCUMENT ME! */
	private String timbroVocale = "";

	/**  DOCUMENT ME! */
	private String idInterprete = "";

	/**  DOCUMENT ME! */
	private C926[] incipit = null;

	/**  DOCUMENT ME! */
	private String indicatoreIncipit = "";

	/**  DOCUMENT ME! */
	private String numeroComposizioni = "";

	/**  DOCUMENT ME! */
	private String contesto = "";

	/**  DOCUMENT ME! */
	private String numeroMovimento = "";

	/**  DOCUMENT ME! */
	private String numeroProgressivoMovimenti = "";

	/**  DOCUMENT ME! */
	private String registroMusicale = "";

	/**  DOCUMENT ME! */
	private String codiceFormaMusicale = "";

	/**  DOCUMENT ME! */
	private String codiceTonalita = "";

	/**  DOCUMENT ME! */
	private String chiaveMusicale = "";

	/**  DOCUMENT ME! */
	private String alterazione = "";

	/**  DOCUMENT ME! */
	private String misura = "";

	/**  DOCUMENT ME! */
	private String tempoMusicale = "";

	/**  DOCUMENT ME! */
	private String nomePersonaggio = "";

	/**  DOCUMENT ME! */
	private String idIncipitLetterario = "";


	private String[] arrLingua = new String[3];
	private String[] arrGenere = new String[4];

	private int numStdCount = 0;

	private String[] numSTD = null;
	private String[] tipoSTD = null;
	private String[] notaSTD = null;

	private int improntaCount = 0;

	private String[] impronta1 = null;
	private String[] impronta2 = null;
	private String[] impronta3 = null;
	private String[] notaImpronta = null;



	private int personaggioCount = 0;

	private String[] personaggi = null;
	private String[] timbriVocale = null;
	private String[] interprete = null;


	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// al link dei documenti su Basi Esterne - Link verso base date digitali
	private int linkEsterniCount = 0;
	private String[] linkEsterniBD = null;
	private String[] linkEsterniID = null;
	private String[] linkEsterniURL = null;

	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo
	private int reperCartaceoCount = 0;
	private String[] reperCartaceoAutTit = null;
	private String[] reperCartaceoData = null;
	private String[] reperCartaceoPos = null;


	private String isadn = "";

	private List vectorRepertori = null;

	private List vectorPersonaggi = null;

	/**  DOCUMENT ME! */
	private String pubblicazioneGovernativa = "";

	private String areaDatiMatematici = "";

	/**  DOCUMENT ME! */
	private String indicatoreColoreCartografia = "";

	/**  DOCUMENT ME! */
	private String meridianoOrigine = "";

	/**  DOCUMENT ME! */
	private String supportoFisico = "";

	/**  DOCUMENT ME! */
	private String tecnicaCreazione = "";

	/**  DOCUMENT ME! */
	private String formaRiproduzione = "";

	/**  DOCUMENT ME! */
	private String formaPubblicazione = "";

	/**  DOCUMENT ME! */
	private String altitudine = "";

	/**  DOCUMENT ME! */
	private String indicatoreTipoScala = "";

	/**  DOCUMENT ME! */
	private String tipoScala = "";

	/**  DOCUMENT ME! */
	private String scalaOrizzontale = "";

	/**  DOCUMENT ME! */
	private String scalaVerticale = "";

	/**  DOCUMENT ME! */
	private String coordinateOvest = "";

	/**  DOCUMENT ME! */
	private String coordinateEst = "";

	/**  DOCUMENT ME! */
	private String coordinateNord = "";

	/**  DOCUMENT ME! */
	private String coordinateSud = "";

	/**  DOCUMENT ME! */
	private String carattereImmagine = "";

	/**  DOCUMENT ME! */
	private String forma = "";

	/**  DOCUMENT ME! */
	private String piattaforma = "";

	/**  DOCUMENT ME! */
	private String categoriaSatellite = "";

	// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
	private String proiezioneCarte = "";


	/**  DOCUMENT ME! */
	private String designazioneMateriale = "";

	/**  DOCUMENT ME! */
	private String supportoPrimario = "";

	/**  DOCUMENT ME! */
	private String indicatoreColoreGrafica = "";

	/**  DOCUMENT ME! */
	private String indicatoreTecnica1 = "";
	/**  DOCUMENT ME! */
	private String indicatoreTecnica2 = "";
	/**  DOCUMENT ME! */
	private String indicatoreTecnica3 = "";
	/**  DOCUMENT ME! */
	private String indicatoreTecnicaStampe1 = "";
	/**  DOCUMENT ME! */
	private String indicatoreTecnicaStampe2 = "";
	/**  DOCUMENT ME! */
	private String indicatoreTecnicaStampe3 = "";

	/**  DOCUMENT ME! */
	private String designazioneFunzione = "";

	private String livelloAutCar = "";
	private String livelloAutGra = "";
	private String livelloAutMus = "";


	// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
    // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
	private String formaOpera231;
	private String dataOpera231;
	private String altreCarat231;

	/**
	 * @param natura
	 */
	public void setNatura(String natura) {
		this.natura = natura;
	}

	/**
	 * @param tipoMateriale
	 */
	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	/**
	 * @return areaDatiMatematici
	 */
	public String getAreaDatiMatematici() {
		return areaDatiMatematici;
	}

	/**
	 * @param areaDatiMatematici
	 */
	public void setAreaDatiMatematici(String areaDatiMatematici) {
		this.areaDatiMatematici = areaDatiMatematici;
	}

	/**
	 * @return vectorPersonaggi
	 */
	public List getListPersonaggi() {
		return vectorPersonaggi;
	}

	/**
	 * @param vectorPersonaggi
	 */
	public void setListPersonaggi(List vectorPersonaggi) {
		this.vectorPersonaggi = vectorPersonaggi;
	}

	/**
	 * @return incipit
	 */
	public C926[] getIncipit() {
		return incipit;
	}

	/**
	 * @return
	 */
	public String getAlterazione() {
		return alterazione;
	}

	/**
	 * @return
	 */
	public String getAnnoRappresentazione() {
		return annoRappresentazione;
	}

	/**
	 * @return
	 */
	public String getChiaveMusicale() {
		return chiaveMusicale;
	}

	/**
	 * @return
	 */
	public String getCodiceFormaMusicale() {
		return codiceFormaMusicale;
	}

	/**
	 * @return
	 */
	public String getCodiceTonalita() {
		return codiceTonalita;
	}

	/**
	 * @return
	 */
	public String getComposito() {
		return composito;
	}

	/**
	 * @return
	 */
	public String getConservazione() {
		return conservazione;
	}

	/**
	 * @return
	 */
	public String getContesto() {
		return contesto;
	}

	/**
	 * @return
	 */
	public String getGenereRappresentazione() {
		return genereRappresentazione;
	}

	/**
	 * @return
	 */
	public String getIdIncipitLetterario() {
		return idIncipitLetterario;
	}

	/**
	 * @return
	 */
	public String getIdInterprete() {
		return idInterprete;
	}

	/**
	 * @return
	 */
	public String getIllustrazioni() {
		return illustrazioni;
	}

	/**
	 * @return
	 */
	public String getIndicatoreIncipit() {
		return indicatoreIncipit;
	}

	/**
	 * @return
	 */
	public String getLegatura() {
		return legatura;
	}

	/**
	 * @return
	 */
	public String getLuogoRappresentazione() {
		return luogoRappresentazione;
	}

	/**
	 * @return
	 */
	public String getMateria() {
		return materia;
	}

	/**
	 * @return
	 */
	public String getMisura() {
		return misura;
	}

	/**
	 * @return
	 */
	public String getNomePersonaggio() {
		return nomePersonaggio;
	}

	/**
	 * @return
	 */
	public String getNotaRappresentazione() {
		return notaRappresentazione;
	}

	/**
	 * @return
	 */
	public String getNotazioneMusicale() {
		return notazioneMusicale;
	}

	/**
	 * @return
	 */
	public String getNumeroComposizioni() {
		return numeroComposizioni;
	}

	/**
	 * @return
	 */
	public String getNumeroMovimento() {
		return numeroMovimento;
	}

	/**
	 * @return
	 */
	public String getNumeroProgressivoMovimenti() {
		return numeroProgressivoMovimenti;
	}

	/**
	 * @return
	 */
	public String getOccasioneRappresentazione() {
		return occasioneRappresentazione;
	}

	/**
	 * @return
	 */
	public String getPalinsesto() {
		return palinsesto;
	}

	/**
	 * @return
	 */
	public String getPeriodoRappresentazione() {
		return periodoRappresentazione;
	}

	/**
	 * @return
	 */
	public String[] getPersonaggio() {
		return personaggi;
	}
	public String[] getTimbriVocale() {
		return timbriVocale;
	}
	public String[] getInterprete() {
		return interprete;
	}

	/**
	 * @return
	 */
	public String getPresentazione() {
		return presentazione;
	}

	/**
	 * @return
	 */
	public String getRegistroMusicale() {
		return registroMusicale;
	}

	/**
	 * @return
	 */
	public String getStesura() {
		return stesura;
	}

	/**
	 * @return
	 */
	public String getSedeRappresentazione() {
		return sedeRappresentazione;
	}

	/**
	 * @return
	 */
	public String getTempoMusicale() {
		return tempoMusicale;
	}

	/**
	 * @return
	 */
	public String getTimbroVocale() {
		return timbroVocale;
	}

	/**
	 * @return
	 */
	public String getTipoElaborazione() {
		return tipoElaborazione;
	}

	/**
	 * @return
	 */
	public String getTipoTesto() {
		return tipoTesto;
	}

	/**
	 * @return areaMusica
	 */
	public String getAreaMusica() {
		return areaMusica;
	}

	/**
	 * @param areaMusica
	 */
	//public void setAreaMusica(String areaMusica) {
	//	this.areaMusica = areaMusica;
	//}

	/**
	 * @return r
	 */
	public String getAreaDescrizioneFisica() {
		return areaDescrizioneFisica;
	}

	/**
	 * @return r
	 */
	public String getAreaEdizione() {
		return areaEdizione;
	}

	/**
	 * @return r
	 */
	public String getAreaNote() {
		return areaNote;
	}

	/**
	 * @return r
	 */
	public String getAreaNumerazione() {
		return areaNumerazione;
	}

	/**
	 * @return r
	 */
	public String getAreaPubblicazione() {
		return areaPubblicazione;
	}

	/**
	 * @return r
	 */
	public String[] getArrGenere() {
		return arrGenere;
	}

	/**
	 * @return r
	 */
	public List[] getArrivoLegami() {
		return arrivoLegami;
	}

	/**
	 * @return r
	 */
	public String[] getArrLingua() {
		return arrLingua;
	}

	/**
	 * @return r
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * @return r
	 */
	public String getData1() {
		return data1;
	}

	/**
	 * @return r
	 */
	public String getData2() {
		return data2;
	}

	/**
	 * @return r
	 */
	public String getDataInserimento() {
		UtilityDate utilityDate = new UtilityDate();
		if (dataInserimento.length() == 0) {
			return dataInserimento;
		}
		if (!dataInserimento.substring(4,5).equals("-")) {
			return utilityDate.converteDataSBN(dataInserimento);
		}
		return dataInserimento;
	}

	/**
	 * @return r
	 */
	public String getDatazione() {
		return datazione;
	}

	/**
	 * @return r
	 */
	public String getFonteRecord() {
		return fonteRecord;
	}

	/**
	 * @return r
	 */
	public String[] getFormaMusicale() {
		if (formaMusicale == null) formaMusicale = new String[3];
		return formaMusicale;
	}

	/**
	 * @return r
	 */
	public String getIsadn() {
		return isadn;
	}

	/**
	 * @return r
	 */
	public LegamiType[] getLegamiType() {
		return legamiType;
	}

	/**
	 * @return r
	 */
	public String getLivelloAut() {
		return livelloAut;
	}

	/**
	 * @return r
	 */
	public String getLivelloBibliografico() {
		return livelloBibliografico;
	}

	/**
	 * @return r
	 */
	public String getNatura() {
		return natura;
	}

	/**
	 * @return r
	 */
	public String getNotaCatalogatore() {
		return notaCatalogatore;
	}

	/**
	 * @return r
	 */
	public String getNotaInformativa() {
		return notaInformativa;
	}

	/**
	 * @return r
	 */
	public String[] getNotaSTD() {
		return notaSTD;
	}
	// CAMPI IMPRONTA
	public int getImprontaCount() {
		return improntaCount;
	}
	public String[] getImpronta1() {
		return impronta1;
	}
	public String[] getImpronta2() {
		return impronta2;
	}
	public String[] getImpronta3() {
		return impronta3;
	}
	public String[] getNotaImpronta() {
		return notaImpronta;
	}

	/**
	 * @return r
	 */
	public int getNumArriviLegame() {
		return numArriviLegame;
	}

	/**
	 * @return r
	 */
	public String getNumeroCatalogoTematico() {
		return numeroCatalogoTematico;
	}

	/**
	 * @return r
	 */
	public String getNumeroOpera() {
		return numeroOpera;
	}

	/**
	 * @return r
	 */
	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @return r
	 */
	public int getNumLegami() {
		return numLegami;
	}

	/**
	 * @return r
	 */
	public String[] getNumSTD() {
		return numSTD;
	}

	/**
	 * @return r
	 */
	public int getNumStdCount() {
		return numStdCount;
	}

	public int getNumPersonaggio() {
		return personaggioCount;
	}

	/**
	 * @return r
	 */
	public String getOrganicoAnalitico() {
		return organicoAnalitico;
	}

	/**
	 * @return r
	 */
	public String getOrganicoSintetico() {
		return organicoSintetico;
	}

	/**
	 * @return r
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * @return r
	 */
	public String getSezioni() {
		return sezioni;
	}

	/**
	 * @return r
	 */
	public String getTipoAuthority() {
		return tipoAuthority;
	}

	/**
	 * @return r
	 */
	public String getTipoData() {
		return tipoData;
	}

	/**
	 * @return r
	 */
	public String getTipoMateriale() {
		return tipoMateriale;
	}

	/**
	 * @return r
	 */
	public String getTipoRecord() {
		return tipoRecord;
	}

	/**
	 * @return r
	 */
	public String[] getTipoSTD() {
		return tipoSTD;
	}

	/**
	 * @return r
	 */
	public String getTitoloAppellativo() {
		return titoloAppellativo;
	}

	/**
	 * @return r
	 */
	public String getTitoloEstratto() {
		return titoloEstratto;
	}

	/**
	 * @return r
	 */
	public String getTitoloIndicazioneResponsabilita() {
		return titoloIndicazioneResponsabilita;
	}

	/**
	 * @return r
	 */
	public String getTitoloOrdinamento() {
		return titoloOrdinamento;
	}

	/**
	 * @return r
	 */
	public String getTonalita() {
		return tonalita;
	}

	/**
	 * @return r
	 */
	public List getListRepertori() {
		return vectorRepertori;
	}

	/**
	 * @return r
	 */
	public String getVersione() {
		return versione;
	}



	/**
	 * Costruttore che imposta i valori dei campi del titolo
	 */
	public UtilityCampiTitolo(String BID, String BIDRoot, SBNMarc sbnMarc) {
		DocumentoType documentoType = null;
		DatiElementoType datiElementoType = null;

		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarc);


		// CONTROLLO PER TROVARE L'ELEMENTO
		// CORRISPONDENTE AL BID SELEZIONATO.
		if (documentoType != null) {

			DatiDocType datiDocType =
				this.getDatiDocTypeDocumento(documentoType, BID);
			TitAccessoType titAccessoType =
				this.getTitAccessoTypeDocumento(documentoType, BID);

			if (datiDocType != null) {
				//E' un documento
				natura = datiDocType.getNaturaDoc().toString();
				bid = datiDocType.getT001().toString();
				tipoMateriale = datiDocType.getTipoMateriale().toString();
				livelloAut = datiDocType.getLivelloAutDoc().toString();
				titoloIndicazioneResponsabilita = datiDocType.getT200().getA_200()[0];
				if (datiDocType.getGuida() != null)
					if (datiDocType.getGuida().getTipoRecord() != null)
						tipoRecord = datiDocType.getGuida().getTipoRecord().toString();
				if (datiDocType.getT100() != null)
					if (datiDocType.getT100().getA_100_8() != null)
						tipoData = datiDocType.getT100().getA_100_8().toString();
				if (datiDocType.getT102() != null)
					if (datiDocType.getT102().getA_102() != null)
						paese = datiDocType.getT102().getA_102().toString();
				versione = datiDocType.getT005().toString();
				if (datiDocType.getT100().getA_100_0() != null)
					dataInserimento = datiDocType.getT100().getA_100_0().toString();
				if (datiDocType.getT100().getA_100_9() != null)
					data1 = datiDocType.getT100().getA_100_9().toString();
				if (datiDocType.getT100().getA_100_13() != null)
					data2 = datiDocType.getT100().getA_100_13().toString();
				if (datiDocType.getT205() != null)
					areaEdizione = datiDocType.getT205().getA_205();
				if (datiDocType.getT207() != null)
					for (int i=0; i < datiDocType.getT207().getA_207Count(); i++) areaNumerazione += datiDocType.getT207().getA_207()[i];
				if (datiDocType.getT215() != null){
					for (int i=0; i < datiDocType.getT215().getA_215Count(); i++) areaDescrizioneFisica += datiDocType.getT215().getA_215()[i];
				}

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				// nel Materiale Antico il tipo testo letterario è registrato sulla 140bis e non sulla 105bis

				if (datiDocType.getT105bis() != null) {
					tipoTestoLetterario = datiDocType.getT105bis().getA_105_11();
				} else if (datiDocType.getT140bis() != null) {
					tipoTestoLetterario = datiDocType.getT140bis().getA_140_17();
				}


				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (tipoRecord != null && tipoRecord.equals("i")) {
					if (datiDocType.getT125bis() != null) {
						tipoTestoRegSonora = datiDocType.getT125bis().getB_125();
					}
				}




				if (datiDocType.getT181() != null) {
					if (datiDocType.getT181Count() > 0) {
						if (datiDocType.getT181(0).getA_181_0() != null)
							formaContenuto = datiDocType.getT181(0).getA_181_0();
						if (datiDocType.getT181(0).getB_181_0() != null)
							tipoContenuto = datiDocType.getT181(0).getB_181_0();
						if (datiDocType.getT181(0).getB_181_1() != null)
							movimento = datiDocType.getT181(0).getB_181_1();
						if (datiDocType.getT181(0).getB_181_2() != null)
							dimensione = datiDocType.getT181(0).getB_181_2();
						if (datiDocType.getT181(0).getB_181_3() != null)
							sensorialita1 = datiDocType.getT181(0).getB_181_3();
						if (datiDocType.getT181(0).getB_181_4() != null)
							sensorialita2 = datiDocType.getT181(0).getB_181_4();
						if (datiDocType.getT181(0).getB_181_5() != null)
							sensorialita3 = datiDocType.getT181(0).getB_181_5();
					}
					if (datiDocType.getT181Count() > 1) {
						if (datiDocType.getT181(1).getA_181_0() != null)
							formaContenutoBIS = datiDocType.getT181(1).getA_181_0();
						if (datiDocType.getT181(1).getB_181_0() != null)
							tipoContenutoBIS = datiDocType.getT181(1).getB_181_0();
						if (datiDocType.getT181(1).getB_181_1() != null)
							movimentoBIS = datiDocType.getT181(1).getB_181_1();
						if (datiDocType.getT181(1).getB_181_2() != null)
							dimensioneBIS = datiDocType.getT181(1).getB_181_2();
						if (datiDocType.getT181(1).getB_181_3() != null)
							sensorialitaBIS1 = datiDocType.getT181(1).getB_181_3();
						if (datiDocType.getT181(1).getB_181_4() != null)
							sensorialitaBIS2 = datiDocType.getT181(1).getB_181_4();
						if (datiDocType.getT181(1).getB_181_5() != null)
							sensorialitaBIS3 = datiDocType.getT181(1).getB_181_5();
					}
				}
				if (datiDocType.getT182() != null) {
					if (datiDocType.getT182Count() > 0) {
						if (datiDocType.getT182(0).getA_182_0() != null)
							tipoMediazione = datiDocType.getT182(0).getA_182_0();
					}
					if (datiDocType.getT182Count() > 1) {
						if (datiDocType.getT182(1).getA_182_0() != null)
							tipoMediazioneBIS = datiDocType.getT182(1).getA_182_0();

					}
				}

				// Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (datiDocType.getT183() != null) {
					if (datiDocType.getT183Count() > 0) {
						if (datiDocType.getT183(0).getA_183_0() != null)
							tipoSupporto = datiDocType.getT183(0).getA_183_0();
					}
					if (datiDocType.getT183Count() > 1) {
						if (datiDocType.getT183(1).getA_183_0() != null)
							tipoSupportoBIS = datiDocType.getT183(1).getA_183_0();

					}
				}


//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (datiDocType.getT3XX() != null)
//					for (int i=0; i < datiDocType.getT3XX().length; i++)
//						areaNote += datiDocType.getT3XX()[i].getA_3XX().toString();


				if (datiDocType.getT3XX() != null){
					for (int i=0; i < datiDocType.getT3XX().length; i++){
						SbnTipoNota tipoNota = datiDocType.getT3XX(i).getTipoNota();

						String codice = "";
						try {
							codice = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_TIPO_NOTA, tipoNota.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}

						// Bug mantis esercizio 6171- almaviva2 aprile 2016 a seguito del'intervento di inserimento della nota 321
						// la valorizzazione dei codici nota nella classe SbnTipoNota utilizzata per inviare il tipo di Nota
						// al protocollo Polo/Indiceè cambiata; la valorizzazione del campo tipo nota viene quindi effettuata
						// con metodo valueOf indicando esplicitamente la nota in oggetto e non con il suo progressivo automatico
						// che ha subito variazioni (e potrebbe subirne altre);
						if(codice.equals(SbnTipoNota.valueOf("300").toString())){
							areaNote = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("323").toString())){
							areaNote323 = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("327").toString())){
							areaNote327 = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("330").toString())){
							areaNote330 = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("336").toString())){
							areaNote336 = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("337").toString())){
							areaNote337 = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("DATA").toString())){
							areaNoteDATA = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("FILI").toString())){
							areaNoteFILI = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("PROV").toString())){
							areaNotePROV = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
						if(codice.equals(SbnTipoNota.valueOf("POSS").toString())){
							areaNotePOSS = datiDocType.getT3XX()[i].getA_3XX().toString();
						}
					}
				}

//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

				if (datiDocType.getT801() != null) {
					fonteRecord = datiDocType.getT801().getA_801().toString() +
									datiDocType.getT801().getB_801().toString();
				}
				if (datiDocType.getT210() != null) {
					for (int i=0; i < datiDocType.getT210Count(); i++)
						for (int j=0; j < datiDocType.getT210(i).getAc_210Count(); j++) {

							// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
							// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
							if (j == 0) {
								if (datiDocType.getT210(i).getId2() == null) {
									pubblicatoSiNo = "#";
								} else {
									pubblicatoSiNo = datiDocType.getT210(i).getId2().toString();
								}
							}
							for (int k=0; k<datiDocType.getT210(i).getAc_210(j).getA_210Count(); k++){
								areaPubblicazione += datiDocType.getT210(i).getAc_210(j).getA_210()[k];
							}
						}
				}

				if (datiDocType.getT856() != null) {
					for (int i=0; i < datiDocType.getT856Count(); i++) {

						// Inizio Modifica almaviva2 BUG MANTIS esercizio 5138: correzione della situazione per cui inserendo l'url
						// nell'apposito campo e viene duplicata e triplicata.. tutte le volte che si apre 'varia descrizione'...
						// e poi si salva.Viene asteriscata la precedente impostazione anche perchè il bug citato (4624) non contiene
						// nessun riferimento al campo uriDigitalBorn

						// Modifica almaviva2 19.09.2011 BUG MANTIS esercizio 4624
						// l'area del uriDigitalBorn da prospettare deve esssere o l'una o l'altra a seconda di quella valorizzata
						// o l'accostamento delle due (controllando la presenza dei null)
						// uriDigitalBorn += new String(datiDocType.getT856(i).getC9_856_1());

						// Fine Modifica almaviva2 BUG MANTIS esercizio 5138
						if (datiDocType.getT856(i).getU_856() != null) {
							uriDigitalBorn += datiDocType.getT856(i).getU_856();
						}
					}
				}
				// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
				if (datiDocType.getGuida().getLivelloBibliografico() != null) {
					livelloBibliografico = datiDocType.getGuida().getLivelloBibliografico().toString();
				}

				if (datiDocType.getT101() != null) {
					for (int i=0; i < datiDocType.getT101().getA_101().length; i++) {
						arrLingua[i] = datiDocType.getT101().getA_101()[i].toString();
					}
				}
				//Numeri standard
				numStdCount = datiDocType.getNumSTDCount();
				if (numStdCount > 0) {
					numSTD = new String[numStdCount];
					tipoSTD = new String[numStdCount];
					notaSTD = new String[numStdCount];
					for (int i=0; i < numStdCount; i++) {
						numSTD[i] = datiDocType.getNumSTD()[i].getNumeroSTD();
						tipoSTD[i] = datiDocType.getNumSTD()[i].getTipoSTD().toString();
						notaSTD[i] = datiDocType.getNumSTD()[i].getNotaSTD();
					}
				}


				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// ai repertori cartacei - Riferimento a repertorio cartaceo
				int count321 = datiDocType.getT321Count();
				int progInsRep = -1;
				int progInsLink = -1;

				int countLink = 0;
				int countRep = 0;

				for (int i=0; i < count321; i++) {
					if (datiDocType.getT321()[i].getTipoNota().equals(TipoNota321.DATABASE)) {
						countLink++;
					} else if (datiDocType.getT321()[i].getTipoNota().equals(TipoNota321.REPERTORIO)) {
						countRep++;
					}
				}

				if (countLink > 0) {
					linkEsterniBD = new String[countLink];
					linkEsterniID = new String[countLink];
					linkEsterniURL = new String[countLink];
				}
				if (countRep > 0) {
					reperCartaceoAutTit = new String[countRep];
					reperCartaceoData = new String[countRep];
					reperCartaceoPos = new String[countRep];
				}

				for (int i=0; i < count321; i++) {
					if (datiDocType.getT321()[i].getTipoNota().equals(TipoNota321.DATABASE)) {
						progInsLink++;
						linkEsterniBD[progInsLink] = datiDocType.getT321()[i].getA_321();
						linkEsterniID[progInsLink] = datiDocType.getT321()[i].getC_321();
						linkEsterniURL[progInsLink] = datiDocType.getT321()[i].getU_321();
					} else if (datiDocType.getT321()[i].getTipoNota().equals(TipoNota321.REPERTORIO)) {
						progInsRep++;
						reperCartaceoAutTit[progInsRep] = datiDocType.getT321()[i].getA_321();
						reperCartaceoData[progInsRep] = datiDocType.getT321()[i].getB_321();
						reperCartaceoPos[progInsRep] = datiDocType.getT321()[i].getC_321();
					}
				}
				linkEsterniCount = ++progInsLink;
				reperCartaceoCount = ++progInsRep;

////////////////// NUOVE MODIFICHE GESTIONE GENERICA
				// Schema da 1.07 a 1.09
				if (datiDocType.getT206() != null) {
					for (int i=0; i<datiDocType.getT206Count(); i++)
						areaDatiMatematici += datiDocType.getT206()[i].getA_206();
				}
				if (datiDocType.getT208() != null) {
					// Inizio modifica per corretta gestione Area Musica
					C208 c208 = datiDocType.getT208();
					String c208_string = "";
			        c208_string = concatena(c208_string, c208.getA_208(), "", "");
			        if (c208.getD_208Count() > 0) {
			            c208_string = concatena(c208_string, c208.getD_208(), " = ", " = ", "");
			        }
					// Fine modifica per corretta gestione Area Musica

					areaMusica = c208_string;
				}
//////////////////


				if (tipoMateriale.equals("C")) {
					//Gestire qui la cartografia
					if (datiDocType instanceof CartograficoType) {
						CartograficoType cartograficoType = (CartograficoType)datiDocType;

						// Segnalazione Carla del 10/03/2015:
						// inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta
						// anche nei Materiale Grafico e Cartografico
						improntaCount = cartograficoType.getT012Count();
						if (improntaCount > 0) {
							impronta1 = new String[improntaCount];
							impronta2 = new String[improntaCount];
							impronta3 = new String[improntaCount];
							notaImpronta = new String[improntaCount];
							for (int i=0; i < improntaCount; i++) {
								if (cartograficoType.getT012()[i].getA_012_1() != null){
									impronta1[i] = cartograficoType.getT012()[i].getA_012_1().toString();
								}
								if (cartograficoType.getT012()[i].getA_012_2() != null){
									impronta2[i] = cartograficoType.getT012()[i].getA_012_2().toString();
								}
								if (cartograficoType.getT012()[i].getA_012_3()  != null){
									impronta3[i] = cartograficoType.getT012()[i].getA_012_3().toString();
								}
								if (cartograficoType.getT012()[i].getNota() != null){
									notaImpronta[i] = cartograficoType.getT012()[i].getNota().toString();
								}
							}
						}

						if (cartograficoType.getLivelloAut() != null) {
							livelloAutCar = cartograficoType.getLivelloAut().toString();
						}

						if (cartograficoType.getT100() != null)
							pubblicazioneGovernativa = cartograficoType.getT100().getA_100_20();
						if (cartograficoType.getT120() != null) {
							indicatoreColoreCartografia = cartograficoType.getT120().getA_120_0();
							meridianoOrigine = cartograficoType.getT120().getA_120_9();
							// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
							proiezioneCarte = cartograficoType.getT120().getA_120_7();
						}
						if (cartograficoType.getT121() != null) {
							supportoFisico = cartograficoType.getT121().getA_121_3();
							tecnicaCreazione = cartograficoType.getT121().getA_121_5();
							formaRiproduzione = cartograficoType.getT121().getA_121_6();
							formaPubblicazione = cartograficoType.getT121().getA_121_8();
							altitudine = cartograficoType.getT121().getB_121_0();
						}
						if (cartograficoType.getT123() != null) {
							indicatoreTipoScala = cartograficoType.getT123().getId1().toString();
							tipoScala =  cartograficoType.getT123().getA_123();
							scalaOrizzontale = cartograficoType.getT123().getB_123();
							scalaVerticale = cartograficoType.getT123().getC_123();
							coordinateOvest = cartograficoType.getT123().getD_123();
							coordinateEst = cartograficoType.getT123().getE_123();
							coordinateNord = cartograficoType.getT123().getF_123();
							coordinateSud = cartograficoType.getT123().getG_123();
						}
						if (cartograficoType.getT124() != null) {
							carattereImmagine = cartograficoType.getT124().getA_124();
							forma = cartograficoType.getT124().getB_124();
							piattaforma = cartograficoType.getT124().getD_124();
							categoriaSatellite = cartograficoType.getT124().getE_124();

						}
					}
				}

				if (tipoMateriale.equals("G")) {
					//Gestire qui la grafica
					if (datiDocType instanceof GraficoType) {
						GraficoType graficoType = (GraficoType)datiDocType;

						// Segnalazione Carla del 10/03/2015:
						// inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta
						// anche nei Materiale Grafico e Cartografico
						improntaCount = graficoType.getT012Count();
						if (improntaCount > 0) {
							impronta1 = new String[improntaCount];
							impronta2 = new String[improntaCount];
							impronta3 = new String[improntaCount];
							notaImpronta = new String[improntaCount];
							for (int i=0; i < improntaCount; i++) {
								if (graficoType.getT012()[i].getA_012_1() != null){
									impronta1[i] = graficoType.getT012()[i].getA_012_1().toString();
								}
								if (graficoType.getT012()[i].getA_012_2() != null){
									impronta2[i] = graficoType.getT012()[i].getA_012_2().toString();
								}
								if (graficoType.getT012()[i].getA_012_3()  != null){
									impronta3[i] = graficoType.getT012()[i].getA_012_3().toString();
								}
								if (graficoType.getT012()[i].getNota() != null){
									notaImpronta[i] = graficoType.getT012()[i].getNota().toString();
								}
							}
						}

						if (graficoType.getLivelloAut() != null) {
							livelloAutGra = graficoType.getLivelloAut().toString();
						}


						if (graficoType.getT116() != null) {
							if(graficoType.getT116().getA_116_0() != null)
								designazioneMateriale = graficoType.getT116().getA_116_0();
							if(graficoType.getT116().getA_116_1() != null)
								supportoPrimario = graficoType.getT116().getA_116_1();
							if(graficoType.getT116().getA_116_3() != null)
								indicatoreColoreGrafica = graficoType.getT116().getA_116_3();

							if (graficoType.getT116().getA_116_4Count() == 1){
								indicatoreTecnica1 = graficoType.getT116().getA_116_4(0);
							}
							if (graficoType.getT116().getA_116_4Count() == 2){
								indicatoreTecnica1 = graficoType.getT116().getA_116_4(0);
								indicatoreTecnica2 = graficoType.getT116().getA_116_4(1);
							}
							if (graficoType.getT116().getA_116_4Count() == 3){
								indicatoreTecnica1 = graficoType.getT116().getA_116_4(0);
								indicatoreTecnica3 = graficoType.getT116().getA_116_4(2);
								indicatoreTecnica2 = graficoType.getT116().getA_116_4(1);
							}
							// Indicatori di tecnica STAMPA (A_116_10)
							if (graficoType.getT116().getA_116_10Count() == 1){
								indicatoreTecnicaStampe1 = graficoType.getT116().getA_116_10(0);
							}
							if (graficoType.getT116().getA_116_10Count() == 2){
								indicatoreTecnicaStampe1 = graficoType.getT116().getA_116_10(0);
								indicatoreTecnicaStampe2 = graficoType.getT116().getA_116_10(1);
							}
							if (graficoType.getT116().getA_116_10Count() == 3){
								indicatoreTecnicaStampe1 = graficoType.getT116().getA_116_10(0);
								indicatoreTecnicaStampe2 = graficoType.getT116().getA_116_10(1);
								indicatoreTecnicaStampe3 = graficoType.getT116().getA_116_10(2);
							}

							if(graficoType.getT116().getA_116_16() != null)
								designazioneFunzione = graficoType.getT116().getA_116_16();
						}
					}
				}
				if (tipoMateriale.equals("E")) {
					if (datiDocType instanceof AnticoType) {
						AnticoType anticoType = (AnticoType)datiDocType;
						// IMPRONTA
						improntaCount = anticoType.getT012Count();
						if (improntaCount > 0) {
							impronta1 = new String[improntaCount];
							impronta2 = new String[improntaCount];
							impronta3 = new String[improntaCount];
							notaImpronta = new String[improntaCount];
							for (int i=0; i < improntaCount; i++) {
								if (anticoType.getT012()[i].getA_012_1() != null){
									impronta1[i] = anticoType.getT012()[i].getA_012_1().toString();
								}
								if (anticoType.getT012()[i].getA_012_2() != null){
									impronta2[i] = anticoType.getT012()[i].getA_012_2().toString();
								}
								if (anticoType.getT012()[i].getA_012_3()  != null){
									impronta3[i] = anticoType.getT012()[i].getA_012_3().toString();
								}
								if (anticoType.getT012()[i].getNota() != null){
									notaImpronta[i] = anticoType.getT012()[i].getNota().toString();
								}
							}
						}

//						Controllo aggiunto il 23/04/2004 /////
						 //Generi
						 if (anticoType.getT140() != null) {
							 for (int i = 0; i < anticoType.getT140().getA_140_9().length && i < 4; i++) {
								 arrGenere[i] = anticoType.getT140()
								 .getA_140_9()[i].toString();
							 }
						 }

						// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
						// nel Materiale Antico il tipo testo letterario è registrato sulla 140bis e non sulla 105bis
						if (anticoType.getT140bis() != null) {
							tipoTestoLetterario = anticoType.getT140bis().getA_140_17();
						}

						// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
						// viene esteso anche al Materiale Moderno e Antico
						// Area T922 tipica anche del tipo Materiale U
						if (anticoType.getT922() != null) {
							genereRappresentazione = anticoType.getT922().getA_922();
							annoRappresentazione = anticoType.getT922().getP_922();
							periodoRappresentazione = anticoType.getT922().getQ_922();
							sedeRappresentazione = anticoType.getT922().getR_922();
							luogoRappresentazione = anticoType.getT922().getS_922();
							notaRappresentazione = anticoType.getT922().getT_922();
							occasioneRappresentazione = anticoType.getT922().getU_922();
						}
						// Area T927 tipica anche del tipo Materiale U - Personaggio
						personaggioCount = anticoType.getT927Count();
						if (personaggioCount > 0) {
							personaggi = new String[personaggioCount];
							timbriVocale = new String[personaggioCount];
							interprete = new String[personaggioCount];
							for (int i=0; i < personaggioCount; i++) {
								if(anticoType.getT927()[i].getA_927() != null)
									personaggi[i] = anticoType.getT927()[i].getA_927().toString();
								if(anticoType.getT927()[i].getB_927() != null)
									timbriVocale[i] = anticoType.getT927()[i].getB_927().toString();
								if(anticoType.getT927()[i].getC3_927() != null)
									interprete[i] = anticoType.getT927()[i].getC3_927().toString();
							}
						}
					}
				}

				if (tipoMateriale.equals("M")){
					//ModernoType
					if (datiDocType instanceof ModernoType){
						ModernoType modernoType = (ModernoType)datiDocType;
						//Generi
						if (modernoType.getT105() != null) {
							for (int i = 0; i < modernoType.getT105().getA_105_4().length && i < 4; i++) {
								arrGenere[i] = modernoType.getT105()
								.getA_105_4()[i].toString();
							}
						}

						// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
						// viene esteso anche al Materiale Moderno e Antico
						// Area T922 tipica anche del tipo Materiale U
						if (modernoType.getT922() != null) {
							genereRappresentazione = modernoType.getT922().getA_922();
							annoRappresentazione = modernoType.getT922().getP_922();
							periodoRappresentazione = modernoType.getT922().getQ_922();
							sedeRappresentazione = modernoType.getT922().getR_922();
							luogoRappresentazione = modernoType.getT922().getS_922();
							notaRappresentazione = modernoType.getT922().getT_922();
							occasioneRappresentazione = modernoType.getT922().getU_922();
						}
						// Area T927 tipica anche del tipo Materiale U - Personaggio
						personaggioCount = modernoType.getT927Count();
						if (personaggioCount > 0) {
							personaggi = new String[personaggioCount];
							timbriVocale = new String[personaggioCount];
							interprete = new String[personaggioCount];
							for (int i=0; i < personaggioCount; i++) {
								if(modernoType.getT927()[i].getA_927() != null)
									personaggi[i] = modernoType.getT927()[i].getA_927().toString();
								if(modernoType.getT927()[i].getB_927() != null)
									timbriVocale[i] = modernoType.getT927()[i].getB_927().toString();
								if(modernoType.getT927()[i].getC3_927() != null)
									interprete[i] = modernoType.getT927()[i].getC3_927().toString();
							}
						}
					}
				}

				// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
				if (tipoMateriale.equals("H")) {
					//AudiovisivoType
					if (datiDocType instanceof AudiovisivoType){
						AudiovisivoType audiovisivoType = (AudiovisivoType)datiDocType;
						if (audiovisivoType.getLivelloAut() != null) {
							livelloAutAudiov = audiovisivoType.getLivelloAut().toString();
						}

						// Area T115
						if (audiovisivoType.getT115() != null) {
							tipoVideo = audiovisivoType.getT115().getA_115_0();
							lunghezza = audiovisivoType.getT115().getA_115_1();
							indicatoreColore = audiovisivoType.getT115().getA_115_4();
							indicatoreSuono = audiovisivoType.getT115().getA_115_5();
							supportoSuono = audiovisivoType.getT115().getA_115_6();
							larghezzaDimensioni = audiovisivoType.getT115().getA_115_7();
							formaPubblDistr = audiovisivoType.getT115().getA_115_8();
							tecnicaVideoFilm = audiovisivoType.getT115().getA_115_9();
							presentImmagMov = audiovisivoType.getT115().getA_115_10();
							for (int i=0; i < audiovisivoType.getT115().getA_115_11().length; i++) {
								if (audiovisivoType.getT115().getA_115_11()[i] != null) {
									arrMaterAccompagn[i] = audiovisivoType.getT115().getA_115_11()[i].toString();
								}
							}
							pubblicVideoreg = audiovisivoType.getT115().getA_115_15();
							presentVideoreg = audiovisivoType.getT115().getA_115_16();
							materialeEmulsBase = audiovisivoType.getT115().getA_115_17();
							materialeSupportSec = audiovisivoType.getT115().getA_115_18();
							standardTrasmiss = audiovisivoType.getT115().getA_115_19();

							versioneAudiovid = audiovisivoType.getT115().getB_115_0();
							elementiProd = audiovisivoType.getT115().getB_115_1();
							specCatColoreFilm = audiovisivoType.getT115().getB_115_2();
							emulsionePellic = audiovisivoType.getT115().getB_115_3();
							composPellic = audiovisivoType.getT115().getB_115_4();
							suonoImmagMovimento = audiovisivoType.getT115().getB_115_5();
							tipoPellicStampa = audiovisivoType.getT115().getB_115_6();
						}

						// Area T125Bis
						if (audiovisivoType.getT125bis() != null) {
							tipoTestoRegSonora = audiovisivoType.getT125bis().getB_125();
						}


						// Area T126
						if (audiovisivoType.getT126() != null) {
							formaPubblicazioneDisco = audiovisivoType.getT126().getA_126_0();
							velocita = audiovisivoType.getT126().getA_126_1();
							tipoSuono = audiovisivoType.getT126().getA_126_2();
							larghezzaScanal = audiovisivoType.getT126().getA_126_3();
							dimensioni = audiovisivoType.getT126().getA_126_4();
							larghezzaNastro = audiovisivoType.getT126().getA_126_5();
							configurazNastro = audiovisivoType.getT126().getA_126_6();
							for (int i=0; i < audiovisivoType.getT126().getA_126_7().length; i++) {
								if (audiovisivoType.getT126().getA_126_7()[i] != null) {
									arrMaterTestAccompagn[i] = audiovisivoType.getT126().getA_126_7()[i].toString();
								}
							}
							tecnicaRegistraz = audiovisivoType.getT126().getA_126_13();
							specCarattRiprod = audiovisivoType.getT126().getA_126_14();
							datiCodifRegistrazSonore = audiovisivoType.getT126().getB_126_0();
							tipoDiMateriale = audiovisivoType.getT126().getB_126_1();
							tipoDiTaglio = audiovisivoType.getT126().getB_126_2();
						}

						// Area T127
						if (audiovisivoType.getT127() != null) {
							durataRegistraz = audiovisivoType.getT127().getA_127_a();
						}

						// Area T128 tipica anche del tipo Materiale U
						if (audiovisivoType.getT128() != null) {
							if (audiovisivoType.getT128().getD_128() != null) {
								tipoElaborazione = audiovisivoType.getT128().getD_128().toUpperCase();
							}
							organicoSintetico = audiovisivoType.getT128().getB_128();
							organicoAnalitico = audiovisivoType.getT128().getC_128();
						}
						// Area T922 tipica anche del tipo Materiale U
						if (audiovisivoType.getT922() != null) {
							genereRappresentazione = audiovisivoType.getT922().getA_922();
							annoRappresentazione = audiovisivoType.getT922().getP_922();
							periodoRappresentazione = audiovisivoType.getT922().getQ_922();
							sedeRappresentazione = audiovisivoType.getT922().getR_922();
							luogoRappresentazione = audiovisivoType.getT922().getS_922();
							notaRappresentazione = audiovisivoType.getT922().getT_922();
							occasioneRappresentazione = audiovisivoType.getT922().getU_922();
						}
						// Area T927 tipica anche del tipo Materiale U - Personaggio
						personaggioCount = audiovisivoType.getT927Count();
						if (personaggioCount > 0) {
							personaggi = new String[personaggioCount];
							timbriVocale = new String[personaggioCount];
							interprete = new String[personaggioCount];
							for (int i=0; i < personaggioCount; i++) {
								if(audiovisivoType.getT927()[i].getA_927() != null)
									personaggi[i] = audiovisivoType.getT927()[i].getA_927().toString();
								if(audiovisivoType.getT927()[i].getB_927() != null)
									timbriVocale[i] = audiovisivoType.getT927()[i].getB_927().toString();
								if(audiovisivoType.getT927()[i].getC3_927() != null)
									interprete[i] = audiovisivoType.getT927()[i].getC3_927().toString();
							}
						}
					} else {
						// Intervento Settembre 2015: se il POLO non è abilitato al tipo materiale H ma solo ad U
						// Indice invia il MusicaType che deve essere opportunamente caricato se presente
						if (datiDocType instanceof MusicaType) {

							MusicaType musicaType = (MusicaType)datiDocType;
							if (musicaType.getLivelloAut() != null) {
								livelloAutMus = musicaType.getLivelloAut().toString();
							}

							if (musicaType.getT125() != null) {
								tipoTesto = musicaType.getT125().getB_125();
								presentazione = musicaType.getT125().getA_125_0();
							}
							if (musicaType.getT128() != null) {
								// Inizio almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906
								// correzione impostazione campo tipo elaborazione per renderlo visualizzabile sia in
								// dettaglio che in variazione (utilizzata funzione di UPPER)
//								tipoElaborazione = musicaType.getT128().getD_128();
								if (musicaType.getT128().getD_128() != null) {
									tipoElaborazione = musicaType.getT128().getD_128().toUpperCase();
								}
								// Fine almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906
								organicoSintetico = musicaType.getT128().getB_128();
								organicoAnalitico = musicaType.getT128().getC_128();
							}
							if (musicaType.getT923() != null) {
								stesura = musicaType.getT923().getB_923();
								try {
									composito = musicaType.getT923().getC_923().toString();
								} catch (NullPointerException npe) {
								}
								try {
									palinsesto = musicaType.getT923().getD_923().toString();
								} catch (NullPointerException npe) {
									//non fare nulla, oggetto a null
								}
								//almaviva5_20200128 #7335
								datazione = trimOrEmpty(musicaType.getT923().getE_923());
								materia = musicaType.getT923().getG_923();
								illustrazioni = musicaType.getT923().getH_923();
								notazioneMusicale = musicaType.getT923().getI_923();
								legatura = musicaType.getT923().getL_923();
								conservazione = musicaType.getT923().getM_923();
							}
							if (musicaType.getT922() != null) {
								genereRappresentazione = musicaType.getT922().getA_922();
								annoRappresentazione = musicaType.getT922().getP_922();
								periodoRappresentazione = musicaType.getT922().getQ_922();
								sedeRappresentazione = musicaType.getT922().getR_922();
								luogoRappresentazione = musicaType.getT922().getS_922();
								notaRappresentazione = musicaType.getT922().getT_922();
								occasioneRappresentazione = musicaType.getT922().getU_922();
							}
							//Incipit
							if (musicaType.getT926() != null) incipit = musicaType.getT926();
							// IMPRONTA
							improntaCount = musicaType.getT012Count();
							if (improntaCount > 0) {
								impronta1 = new String[improntaCount];
								impronta2 = new String[improntaCount];
								impronta3 = new String[improntaCount];
								notaImpronta = new String[improntaCount];
								for (int i=0; i < improntaCount; i++) {
									if (musicaType.getT012()[i].getA_012_1() != null){
										impronta1[i] = musicaType.getT012()[i].getA_012_1().toString();
									}
									if (musicaType.getT012()[i].getA_012_2() != null){
										impronta2[i] = musicaType.getT012()[i].getA_012_2().toString();
									}
									if (musicaType.getT012()[i].getA_012_3()  != null){
										impronta3[i] = musicaType.getT012()[i].getA_012_3().toString();
									}
									if (musicaType.getT012()[i].getNota() != null){
										notaImpronta[i] = musicaType.getT012()[i].getNota().toString();
									}
								}
							}

							// Personaggio
							personaggioCount = musicaType.getT927Count();
							if (personaggioCount > 0) {
								personaggi = new String[personaggioCount];
								timbriVocale = new String[personaggioCount];
								interprete = new String[personaggioCount];
								for (int i=0; i < personaggioCount; i++) {
									if(musicaType.getT927()[i].getA_927() != null)
										personaggi[i] = musicaType.getT927()[i].getA_927().toString();
									if(musicaType.getT927()[i].getB_927() != null)
										timbriVocale[i] = musicaType.getT927()[i].getB_927().toString();
									if(musicaType.getT927()[i].getC3_927() != null)
										interprete[i] = musicaType.getT927()[i].getC3_927().toString();
								}
							}
						}
					}
				}

				if (tipoMateriale.equals("L")) {
					//ELETTRONICOTYPE
					if (datiDocType instanceof ElettronicoType){
						ElettronicoType elettronicoType = (ElettronicoType)datiDocType;
						if (elettronicoType.getLivelloAut() != null) {
							livelloAutElettr = elettronicoType.getLivelloAut().toString();
						}
						// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
						// Gestione nuovi campi specifici per etichetta 135
						if (elettronicoType.getT135() != null) {
							tipoRisorsaElettronica = elettronicoType.getT135().getA_135_0();
							indicazioneSpecificaMateriale = elettronicoType.getT135().getA_135_1();
							coloreElettronico = elettronicoType.getT135().getA_135_2();
							dimensioniElettronico = elettronicoType.getT135().getA_135_3();
							suonoElettronico = elettronicoType.getT135().getA_135_4();

						}
					}
				}
				// Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

				if (tipoMateriale.equals("U")) {
					//MusicaType
					if (datiDocType instanceof MusicaType) {

						MusicaType musicaType = (MusicaType)datiDocType;

						if (musicaType.getLivelloAut() != null) {
							livelloAutMus = musicaType.getLivelloAut().toString();
						}


						if (musicaType.getT125() != null) {
							tipoTesto = musicaType.getT125().getB_125();
							presentazione = musicaType.getT125().getA_125_0();
						}
						if (musicaType.getT128() != null) {
							// Inizio almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906
							// correzione impostazione campo tipo elaborazione per renderlo visualizzabile sia in
							// dettaglio che in variazione (utilizzata funzione di UPPER)
//							tipoElaborazione = musicaType.getT128().getD_128();
							if (musicaType.getT128().getD_128() != null) {
								tipoElaborazione = musicaType.getT128().getD_128().toUpperCase();
							}
							// Fine almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906
							organicoSintetico = musicaType.getT128().getB_128();
							organicoAnalitico = musicaType.getT128().getC_128();
						}
						if (musicaType.getT923() != null) {
							stesura = musicaType.getT923().getB_923();
							try {
								composito = musicaType.getT923().getC_923().toString();
							} catch (NullPointerException npe) {
							}
							try {
								palinsesto = musicaType.getT923().getD_923().toString();
							} catch (NullPointerException npe) {
								//non fare nulla, oggetto a null
							}
							datazione = musicaType.getT923().getE_923();
							materia = musicaType.getT923().getG_923();
							illustrazioni = musicaType.getT923().getH_923();
							notazioneMusicale = musicaType.getT923().getI_923();
							legatura = musicaType.getT923().getL_923();
							conservazione = musicaType.getT923().getM_923();
						}
						if (musicaType.getT922() != null) {
							genereRappresentazione = musicaType.getT922().getA_922();
							annoRappresentazione = musicaType.getT922().getP_922();
							periodoRappresentazione = musicaType.getT922().getQ_922();
							sedeRappresentazione = musicaType.getT922().getR_922();
							luogoRappresentazione = musicaType.getT922().getS_922();
							notaRappresentazione = musicaType.getT922().getT_922();
							occasioneRappresentazione = musicaType.getT922().getU_922();
						}
						//Incipit
						if (musicaType.getT926() != null) incipit = musicaType.getT926();
						// IMPRONTA
						improntaCount = musicaType.getT012Count();
						if (improntaCount > 0) {
							impronta1 = new String[improntaCount];
							impronta2 = new String[improntaCount];
							impronta3 = new String[improntaCount];
							notaImpronta = new String[improntaCount];
							for (int i=0; i < improntaCount; i++) {
								if (musicaType.getT012()[i].getA_012_1() != null){
									impronta1[i] = musicaType.getT012()[i].getA_012_1().toString();
								}
								if (musicaType.getT012()[i].getA_012_2() != null){
									impronta2[i] = musicaType.getT012()[i].getA_012_2().toString();
								}
								if (musicaType.getT012()[i].getA_012_3()  != null){
									impronta3[i] = musicaType.getT012()[i].getA_012_3().toString();
								}
								if (musicaType.getT012()[i].getNota() != null){
									notaImpronta[i] = musicaType.getT012()[i].getNota().toString();
								}
							}
						}

						// Personaggio
						personaggioCount = musicaType.getT927Count();
						if (personaggioCount > 0) {
							personaggi = new String[personaggioCount];
							timbriVocale = new String[personaggioCount];
							interprete = new String[personaggioCount];
							for (int i=0; i < personaggioCount; i++) {
								if(musicaType.getT927()[i].getA_927() != null)
									personaggi[i] = musicaType.getT927()[i].getA_927().toString();
								if(musicaType.getT927()[i].getB_927() != null)
									timbriVocale[i] = musicaType.getT927()[i].getB_927().toString();
								if(musicaType.getT927()[i].getC3_927() != null)
									interprete[i] = musicaType.getT927()[i].getC3_927().toString();
							}
						}
					}
				}
			} else if (titAccessoType != null){
				//E' un titolo di accesso
				natura = titAccessoType.getNaturaTitAccesso().toString();
				bid = titAccessoType.getT001().toString();
				versione =  titAccessoType.getT005();
				livelloAut = titAccessoType.getLivelloAut().toString();
				if (titAccessoType.getTitAccessoTypeChoice().getT423() != null) {
					for (int i=0; i<titAccessoType.getTitAccessoTypeChoice().getT423().getT200().getA_200().length; i++) titoloIndicazioneResponsabilita += titAccessoType.getTitAccessoTypeChoice().getT423().getT200().getA_200(i).toString() + ";";
				// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
				// } else if (titAccessoType.getTitAccessoTypeChoice().getT454() != null) {
				} else if (titAccessoType.getTitAccessoTypeChoice().getT454A() != null) {
					// for (int i=0; i<titAccessoType.getTitAccessoTypeChoice().getT454().getA_200().length; i++) {
					for (int i=0; i<titAccessoType.getTitAccessoTypeChoice().getT454A().getT200().getA_200().length; i++) {
						titoloIndicazioneResponsabilita += titAccessoType.getTitAccessoTypeChoice().getT454A().getT200().getA_200()[i] + ";";
					}
				} else if (titAccessoType.getTitAccessoTypeChoice().getT510() != null) {
					for (int i=0; i<titAccessoType.getTitAccessoTypeChoice().getT510().getA_200().length; i++) titoloIndicazioneResponsabilita += titAccessoType.getTitAccessoTypeChoice().getT510().getA_200(i).toString() + ";";
				} else if (titAccessoType.getTitAccessoTypeChoice().getT517() != null) {
					for (int i=0; i<titAccessoType.getTitAccessoTypeChoice().getT517().getA_200().length; i++) titoloIndicazioneResponsabilita += titAccessoType.getTitAccessoTypeChoice().getT517().getA_200(i).toString() + ";";
				} else if (documentoType.getDocumentoTypeChoice().getDatiDocumento().getT200() != null) {
					for (int i=0; i<documentoType.getDocumentoTypeChoice().getDatiDocumento().getT200().getA_200().length; i++) titoloIndicazioneResponsabilita = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT200().getA_200(i) + ";";
				}
				// TOGLIE LA VIRGOLA FINALE
				if (titoloIndicazioneResponsabilita.charAt(titoloIndicazioneResponsabilita.length()-1)==';'){
					titoloIndicazioneResponsabilita = titoloIndicazioneResponsabilita.substring(0, titoloIndicazioneResponsabilita.length()-1);
				}
				//Lingue
				if (titAccessoType.getTitAccessoTypeChoice().getT423() != null) {
					if (titAccessoType.getTitAccessoTypeChoice().getT423().getT101() != null) {
						for (int i = 0; i < titAccessoType.getTitAccessoTypeChoice().getT423().getT101().getA_101().length; i++) {
							arrLingua[i] =
								titAccessoType.getTitAccessoTypeChoice()
								.getT423().getT101()
								.getA_101()[i].toString();
						}
					}
				}
				// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
				if (titAccessoType.getTitAccessoTypeChoice().getT454A() != null) {
					if (titAccessoType.getTitAccessoTypeChoice().getT454A().getT101() != null) {
						for (int i = 0; i < titAccessoType.getTitAccessoTypeChoice().getT454A().getT101().getA_101().length; i++) {
							arrLingua[i] = titAccessoType.getTitAccessoTypeChoice().getT454A().getT101().getA_101()[i].toString();
						}
					}
				}




				//Generi
				if (titAccessoType.getTitAccessoTypeChoice().getT423() != null) {
					if (titAccessoType.getTitAccessoTypeChoice().getT423().getT105() != null) {
						for (int i = 0; i < titAccessoType.getTitAccessoTypeChoice().getT423().getT105().getA_105_4().length && i < 4; i++) {
							arrGenere[i] =
								titAccessoType.getTitAccessoTypeChoice()
								.getT423().getT105()
								.getA_105_4()[i].toString();
						}
					}
				}
			}
		}

		else {

			//E' un tipo authority
			datiElementoType = utilityCastor.getElementoAuthority(BID, sbnMarc);

			if (datiElementoType.getTipoAuthority().getType() == SbnAuthority.TU_TYPE)
				tipoMateriale = "Altro";
			if (datiElementoType.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
				tipoMateriale = "Musica";

			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
			// natura = "A";
			if (datiElementoType.getNaturaTU() == null) {
				natura = "A";
			} else {
				natura = datiElementoType.getNaturaTU().toString();
			}

			bid = datiElementoType.getT001().toString();
			livelloAut = datiElementoType.getLivelloAut().toString();
			versione = datiElementoType.getT005();

			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
			if (datiElementoType.getT100() != null) {
				dataInserimento = datiElementoType.getT100().getA_100_0().toString();
			}

			TitoloUniformeMusicaType titoloUniformeMusicaType = new TitoloUniformeMusicaType();
			TitoloUniformeType titoloUniformeType = new TitoloUniformeType();

			if (datiElementoType instanceof TitoloUniformeType) {
				titoloUniformeType = (TitoloUniformeType)datiElementoType;
				// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
	            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
                // con tipo legame 431 (A08V)
				 if (titoloUniformeType.getT230()  != null) {
					 titoloIndicazioneResponsabilita = titoloUniformeType.getT230().getA_230();
	             } else if (titoloUniformeType.getT231()  != null) {
	            	 titoloIndicazioneResponsabilita = titoloUniformeType.getT231().getA_231();
	            	 // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
	                 // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
	            	 formaOpera231 = titoloUniformeType.getT231().getC_231();
	            	 dataOpera231 = titoloUniformeType.getT231().getD_231();
	            	 altreCarat231 = titoloUniformeType.getT231().getK_231();
	            	 //almaviva2 27.07.2017 adeguamento a Indice gestione 231
	            	 // paese = titoloUniformeType.getT231().getE_231();;
	             } else if (titoloUniformeType.getT431()  != null) {
	            	 titoloIndicazioneResponsabilita = titoloUniformeType.getT431().getA_431();

	             }


				 //almaviva2 27.07.2017 adeguamento a Indice gestione 231
				 if (titoloUniformeType.getT102() != null) {
					 paese = titoloUniformeType.getT102().getA_102();
				 }

				if (titoloUniformeType.getT101() != null)
					for (int i=0; i<titoloUniformeType.getT101().getA_101Count(); i++)
						arrLingua[i] = titoloUniformeType.getT101().getA_101()[i];
				if (titoloUniformeType.getT300() != null && titoloUniformeType.getT300().getA_300() != null)
					notaInformativa = titoloUniformeType.getT300().getA_300().toString();
				if (titoloUniformeType.getT830() != null && titoloUniformeType.getT830().getA_830() != null)
					notaCatalogatore = titoloUniformeType.getT830().getA_830().toString();
				if (titoloUniformeType.getT015() != null && titoloUniformeType.getT015().getA_015() != null)
					isadn = titoloUniformeType.getT015().getA_015().toString();
				norme = utilityCastor.getNormeDatiElemento(datiElementoType);
				agenzia = utilityCastor.getAgenziaDatiElemento(datiElementoType);
			}

			if (datiElementoType instanceof TitoloUniformeMusicaType) {
				titoloUniformeMusicaType = (TitoloUniformeMusicaType)datiElementoType;
				titoloIndicazioneResponsabilita = titoloUniformeMusicaType.getT230().getA_230();
				if (titoloUniformeMusicaType.getT929() != null) {
					if (titoloUniformeMusicaType.getT929().getA_929() != null)
						numeroOrdine = titoloUniformeMusicaType.getT929().getA_929().toString();
					if (titoloUniformeMusicaType.getT929().getB_929() != null)
						numeroOpera = titoloUniformeMusicaType.getT929().getB_929().toString();
					if (titoloUniformeMusicaType.getT929().getC_929() != null)
						numeroCatalogoTematico = titoloUniformeMusicaType.getT929().getC_929().toString();
					if (titoloUniformeMusicaType.getT929().getD_929() != null)
						datazione = titoloUniformeMusicaType.getT929().getD_929().toString();
					if (titoloUniformeMusicaType.getT929().getE_929() != null)
						tonalita = titoloUniformeMusicaType.getT929().getE_929().toString();
					if (titoloUniformeMusicaType.getT929().getF_929() != null)
						sezioni = titoloUniformeMusicaType.getT929().getF_929().toString();
					if (titoloUniformeMusicaType.getT929().getG_929() != null)
						titoloOrdinamento = titoloUniformeMusicaType.getT929().getG_929().toString();
					if (titoloUniformeMusicaType.getT929().getH_929() != null)
						titoloEstratto = titoloUniformeMusicaType.getT929().getH_929().toString();
					if (titoloUniformeMusicaType.getT929().getI_929() != null)
						titoloAppellativo = titoloUniformeMusicaType.getT929().getI_929().toString();
					if (titoloUniformeMusicaType.getT928() != null) {
						for (int i=0; i<titoloUniformeMusicaType.getT928().getA_928Count(); i++)
							formaMusicale[i] = titoloUniformeMusicaType.getT928().getA_928()[i];
						organicoSintetico = titoloUniformeMusicaType.getT928().getB_928();
						organicoAnalitico = titoloUniformeMusicaType.getT928().getC_928();
					}
					if (titoloUniformeMusicaType.getT929().getD_929() != null)
						datazione = titoloUniformeMusicaType.getT929().getD_929().toString();
					if (titoloUniformeMusicaType.getT929().getF_929() != null)
						sezioni = titoloUniformeMusicaType.getT929().getF_929().toString();
				}
				if (titoloUniformeMusicaType.getT300() != null)
					notaInformativa = titoloUniformeMusicaType.getT300().getA_300().toString();
				if (titoloUniformeMusicaType.getT830() != null)
					notaCatalogatore = titoloUniformeMusicaType.getT830().getA_830().toString();
				if (titoloUniformeMusicaType.getT015() != null && titoloUniformeMusicaType.getT015().getA_015() != null)
					isadn = titoloUniformeMusicaType.getT015().getA_015().toString();
				norme = utilityCastor.getNormeDatiElemento(datiElementoType);
				agenzia = utilityCastor.getAgenziaDatiElemento(datiElementoType);
			}
			//ANALISI DEI LEGAMI TIPO AUTHORITY
			UtilityCastor utilityCastor = new UtilityCastor();
			vectorRepertori = utilityCastor.getRepertoriElementoAuthority(bid, sbnMarc);

		}

	}

	/**
	 * @return tipoScala
	 */
	public String getTipoScala() {
		return tipoScala;
	}

	/**
	 * @return altitudine
	 */
	public String getAltitudine() {
		return altitudine;
	}

	/**
	 * @return carattereImmagine
	 */
	public String getCarattereImmagine() {
		return carattereImmagine;
	}

	/**
	 * @return categoriaSatellite
	 */
	public String getCategoriaSatellite() {
		return categoriaSatellite;
	}


	public String getProiezioneCarte() {
		return proiezioneCarte;
	}

	/**
	 * @return coordinateEst
	 */
	public String getCoordinateEst() {
		return coordinateEst;
	}

	/**
	 * @return coordinateNord
	 */
	public String getCoordinateNord() {
		return coordinateNord;
	}

	/**
	 * @return coordinateOvest
	 */
	public String getCoordinateOvest() {
		return coordinateOvest;
	}

	/**
	 * @return coordinateSud
	 */
	public String getCoordinateSud() {
		return coordinateSud;
	}

	/**
	 * @return designazioneFunzione
	 */
	public String getDesignazioneFunzione() {
		return designazioneFunzione;
	}

	/**
	 * @return designazioneMateriale
	 */
	public String getDesignazioneMateriale() {
		return designazioneMateriale;
	}

	/**
	 * @return forma
	 */
	public String getForma() {
		return forma;
	}

	/**
	 * @return formaPubblicazione
	 */
	public String getFormaPubblicazione() {
		return formaPubblicazione;
	}

	/**
	 * @return formaRiproduzione
	 */
	public String getFormaRiproduzione() {
		return formaRiproduzione;
	}

	/**
	 * @return indicatoreColoreCartografia
	 */
	public String getIndicatoreColoreCartografia() {
		return indicatoreColoreCartografia;
	}

	/**
	 * @return indicatoreColoreGrafica
	 */
	public String getIndicatoreColoreGrafica() {
		return indicatoreColoreGrafica;
	}

	/**
	 * @return indicatoreTecnica1
	 */
	public String getIndicatoreTecnica1() {
		return indicatoreTecnica1;
	}
	/**
	 * @return indicatoreTecnica2
	 */
	public String getIndicatoreTecnica2() {
		return indicatoreTecnica2;
	}
	/**
	 * @return indicatoreTecnica
	 */
	public String getIndicatoreTecnica3() {
		return indicatoreTecnica3;
	}
	/**
	 * @return indicatoreTecnicaStampe1
	 */
	public String getIndicatoreTecnicaStampe1() {
		return indicatoreTecnicaStampe1;
	}
	/**
	 * @return indicatoreTecnicaStampe2
	 */
	public String getIndicatoreTecnicaStampe2() {
		return indicatoreTecnicaStampe2;
	}
	/**
	 * @return indicatoreTecnicaStampe3
	 */
	public String getIndicatoreTecnicaStampe3() {
		return indicatoreTecnicaStampe3;
	}


	/**
	 * @return indicatoreTipoScala
	 */
	public String getIndicatoreTipoScala() {
		return indicatoreTipoScala;
	}

	/**
	 * @return meridianoOrigine
	 */
	public String getMeridianoOrigine() {
		return meridianoOrigine;
	}

	/**
	 * @return piattaforma
	 */
	public String getPiattaforma() {
		return piattaforma;
	}

	/**
	 * @return pubblicazioneGovernativa
	 */
	public String getPubblicazioneGovernativa() {
		return pubblicazioneGovernativa;
	}

	/**
	 * @return scalaOrizzontale
	 */
	public String getScalaOrizzontale() {
		return scalaOrizzontale;
	}

	/**
	 * @return scalaVerticale
	 */
	public String getScalaVerticale() {
		return scalaVerticale;
	}

	/**
	 * @return supportoFisico
	 */
	public String getSupportoFisico() {
		return supportoFisico;
	}

	/**
	 * @return supportoPrimario
	 */
	public String getSupportoPrimario() {
		return supportoPrimario;
	}

	/**
	 * @return tecnicaCreazione
	 */
	public String getTecnicaCreazione() {
		return tecnicaCreazione;
	}

	public String getAreaTitoloCompleto(String BID, SBNMarc sbnMarcType,
			boolean reticolo) {
		String AreaTitoloCompleto = IID_STRINGAVUOTA;

		DocumentoType documentoType = null;
		DatiElementoType datiElementoType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);
		DatiDocType datiDocType = null;

		if (documentoType != null) {
			datiDocType = getDatiDocTypeDocumento(documentoType, BID);

			if (datiDocType != null) {
				if (reticolo) {
					AreaTitoloCompleto = this.getTitoloResponsabilitaXReticolo(datiDocType);
				} else {
					AreaTitoloCompleto = this.getTitoloResponsabilita(datiDocType);
				}
			} else if (getTitAccessoTypeDocumento(documentoType, BID) != null) {
				// E' un titolo di accesso
				TitAccessoType titAccessoType = getTitAccessoTypeDocumento(documentoType, BID);

				if (titAccessoType.getNaturaTitAccesso().toString().equals("B")) {
					// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
					//C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
					C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454A().getT200();
					if (reticolo) {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
					} else {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccesso(titAccessoType, c200);
					}
				} else
				// se i Dati sono di natura D leggo T517
				if (titAccessoType.getNaturaTitAccesso().toString().equals("D")) {
					C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT517();
					if (reticolo) {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
					} else {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccesso(titAccessoType, c200);
					}
				} else
				// se i Dati sono di natura P leggo T510
				if (titAccessoType.getNaturaTitAccesso().toString().equals("P")) {
					C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT510();
					if (reticolo) {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
					} else {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccesso(titAccessoType, c200);
					}
				} else
				// se i Dati sono di natura T leggo T423
				if (titAccessoType.getNaturaTitAccesso().toString().equals("T")) {
					C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT423().getT200();
					if (reticolo) {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
					} else {
						AreaTitoloCompleto = this.getTitoloResponsabilitaDatiTitAccesso(titAccessoType, c200);
					}
				}
			}
		} else {
			datiElementoType = utilityCastor.getElementoAuthority(BID, sbnMarcType);
			if (datiElementoType instanceof TitoloUniformeType) {
				TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElementoType;
				// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
	            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
                // con tipo legame 431 (A08V)
				 if (titoloUniformeType.getT230()  != null) {
					 AreaTitoloCompleto = titoloUniformeType.getT230().getA_230().toString();
	             } else if (titoloUniformeType.getT231()  != null) {
	            	 AreaTitoloCompleto = titoloUniformeType.getT231().getA_231().toString();
	             } else if (titoloUniformeType.getT431()  != null) {
	            	 AreaTitoloCompleto = titoloUniformeType.getT431().getA_431().toString();
	             }
			}
			if (datiElementoType instanceof TitoloUniformeMusicaType) {
				TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElementoType;
				AreaTitoloCompleto = titoloUniformeMusicaType.getT230().getA_230().toString();
			}
		}
		return AreaTitoloCompleto;
	}

	public String getAreaNumerazioneCompleta(String BID,SBNMarc sbnMarcType) {
		String areaNumerazione= "";
		DocumentoType documentoType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);
		DatiDocType datiDocType = null;

		if (documentoType != null) { //E' un titolo

			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);

			if (datiDocType != null){
				if (datiDocType.getT207() != null){
					for(int k=0; k < datiDocType.getT207().getA_207Count(); k++){
						if(k==0)
							areaNumerazione += datiDocType.getT207().getA_207()[k].toString();
						else
							// BUG MANTIS 5173 (esercizio) correzione replicata da Intervento su Interfaccia Diretta di Marco DiFabio
//							areaNumerazione += ". - " + datiDocType.getT207().getA_207()[k].toString();
						areaNumerazione += " ; " + datiDocType.getT207().getA_207()[k].toString();
					}
				}
			}
		}
		return	areaNumerazione;
	}



	public String getAreaDescrizioneFisicaCompleta(String BID,
			SBNMarc sbnMarcType) {
		String areaDescrizioneFisica = IID_STRINGAVUOTA;
		DocumentoType documentoType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);
		DatiDocType datiDocType = null;

		if (documentoType != null) { // E' un titolo

			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);

			if (datiDocType != null) { // E' un documento
				if (datiDocType.getT215() != null) {
					for (int i = 0; i < datiDocType.getT215().getA_215Count(); i++) {
						if (i == 0) {
							areaDescrizioneFisica = datiDocType.getT215()
									.getA_215()[i];
						} else {
							areaDescrizioneFisica = areaDescrizioneFisica
									+ " ; "
									+ datiDocType.getT215().getA_215()[i];
						}
					}
					if (datiDocType.getT215().getC_215() != null) {
						areaDescrizioneFisica = areaDescrizioneFisica + " : "
								+ datiDocType.getT215().getC_215();
					}
					for (int i = 0; i < datiDocType.getT215().getD_215Count(); i++) {
						areaDescrizioneFisica = areaDescrizioneFisica + " ; "
								+ datiDocType.getT215().getD_215()[i];
					}
					for (int i = 0; i < datiDocType.getT215().getE_215Count(); i++) {
						areaDescrizioneFisica = areaDescrizioneFisica + " + "
								+ datiDocType.getT215().getE_215()[i];
					}
				}
			}
		}
		return areaDescrizioneFisica;
	}

	public String getAreaEdizioneCompleta(String BID, SBNMarc sbnMarcType) {
		String areaEdizioneCompleta = IID_STRINGAVUOTA;
		DocumentoType documentoType = null;
		DatiDocType datiDocType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);

		if (documentoType != null) {
			// E' un titolo
			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);

			if (datiDocType != null) {
				C205 c205 = datiDocType.getT205();
				if (datiDocType.getT205() != null) {
					if (c205.getA_205() != null) {
						areaEdizioneCompleta = c205.getA_205();
					}
					for (int k = 0; k < c205.getB_205Count(); k++) {
						areaEdizioneCompleta = areaEdizioneCompleta + ", "
								+ c205.getB_205(k);
					}
					for (int k = 0; k < c205.getF_205Count(); k++) {
						areaEdizioneCompleta = areaEdizioneCompleta + " / "
								+ c205.getF_205(k);
					}
					for (int k = 0; k < c205.getG_205Count(); k++) {
						areaEdizioneCompleta = areaEdizioneCompleta + " ; "
								+ c205.getG_205(k);
					}
				}
			}
		}
		return areaEdizioneCompleta;
	}
	/**
	 * Restituisce il DatiDocType di un oggetto Documento dati i corrispondenti
	 * DocumentoType e BID. Restituisce null se non esiste.
	 *
	 * @param documentoType
	 *            Oggetto DocumentoType del Documento.
	 * @param BID
	 *            Identificativo del Documento.
	 * @return datiDocType Oggetto DatiDocType del Documento.
	 */

	public DatiDocType getDatiDocTypeDocumento(DocumentoType documentoType,
			String BID) {

		DatiDocType datiDocType = null;

		// CONTROLLA L'ELEMENTO PADRE
		if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
			datiDocType = documentoType.getDocumentoTypeChoice()
					.getDatiDocumento();
			if (!datiDocType.getT001().equals(BID)) {
				datiDocType = null;
			}
		}

		// SE NON E' STATO TROVATO L'ID SELEZIONATO,
		// CONTROLLA I LEGAMI DEL DOCUMENTO.
		if (datiDocType == null) {
			for (int x = 0; x < documentoType.getLegamiDocumentoCount(); x++) {
				for (int y = 0; y < documentoType.getLegamiDocumento(x)
						.getArrivoLegameCount(); y++) {

					// L'ELEMENTO LEGATO E' UN DOCUMENTO
					if (documentoType.getLegamiDocumento(x).getArrivoLegame(y)
							.getLegameDoc() != null) {

						// Legame Documento
						datiDocType = documentoType.getLegamiDocumento(x)
								.getArrivoLegame(y).getLegameDoc()
								.getDocumentoLegato().getDocumentoTypeChoice()
								.getDatiDocumento();
						if (datiDocType.getT001().equals(BID)) {
							break;
						} else {
							// Richiama se stesso per controllare
							// negli eventuali legami del legame a
							// Documento trovato.
							datiDocType = getDatiDocTypeDocumento(documentoType
									.getLegamiDocumento(x).getArrivoLegame(y)
									.getLegameDoc().getDocumentoLegato(), BID);

							// Se il DatiDocType tornato non è null,
							// vuol dire che l'elemento è stato
							// trovato quindi interrompe il ciclo.
							if (datiDocType != null) {
								break;
							}
						}
					}// end if

				}
			}
		}// end if

		return datiDocType;
	}
	/**
	 * Restituisce l'Area Note completa dell'elemento selezionato.
	 *
	 * @param BID
	 *            Identificativo dell'elemento selezionato.
	 * @param sbnMarcType
	 *            Albero Castor.
	 * @return areaNote Area Note completa dell'elemento selezionato.
	 */

	public String getAreaNoteCompleta(String BID, SBNMarc sbnMarcType) {
		String areaNote = IID_STRINGAVUOTA;
		DocumentoType documentoType = null;
		DatiDocType datiDocType = null;

		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);

		if (documentoType != null) { // E' un titolo
			// datiDocType =
			// documentoType.getDocumentoTypeChoice().getDatiDocumento();
			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);
			if (datiDocType != null) {
				if (datiDocType.getT3XX() != null) {
					for (int k = 0; k < datiDocType.getT3XXCount(); k++) {
// Intervento almaviva2 - BUG MANTIS 3360 - si visualizzano solo le note 300
						if (datiDocType.getT3XX()[k].getTipoNota().toString().equals("300")) {
							if (areaNote.equals(""))
								areaNote += datiDocType.getT3XX()[k].getA_3XX().toString();
							else
								areaNote += ". - " + datiDocType.getT3XX()[k].getA_3XX().toString();
						}
					}
				}
			}
		}
		return areaNote;
	}


//	GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
	public String getAreaNoteSpecifiche(String BID,SBNMarc sbnMarcType) {
		String areaNote= "";
		DocumentoType documentoType = null;
		DatiDocType datiDocType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);
		if (documentoType != null) { //E' un titolo
			// datiDocType = documentoType.getDocumentoTypeChoice().getDatiDocumento();
			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);
			if (datiDocType != null) {
				if (datiDocType.getT3XX() != null){
					for (int i = 0; i < datiDocType.getT3XXCount(); i++) {
						SbnTipoNota tipoNota = datiDocType.getT3XX(i).getTipoNota();
//						String nota = Codici.getDescrizioneCodice("NOTA", tipoNota.toString());
						String nota =  "";
						try {
							nota = CodiciProvider.getDescrizioneCodiceUNIMARC(CodiciType.CODICE_TIPO_NOTA, tipoNota.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}

						if(nota.equalsIgnoreCase("NOTA ISBD")){
							areaNote = datiDocType.getT3XX()[i].getA_3XX().toString();
							break;
						}

						if(nota.equalsIgnoreCase("Nota al cast")){
							areaNote = datiDocType.getT3XX()[i].getA_3XX().toString();
							break;
						}
					}
				}
			}
		}
		return areaNote;
	}

	public String getAreaNotaSpecifica(String BID,SBNMarc sbnMarcType, SbnTipoNota tipoNotaA) {
		// almaviva2 MAnutenzione per concatenare le note presenti su più occorrenze della datiDocType.getT3XX (note 323, 327...)
		String areaNote= "";
		DocumentoType documentoType = null;
		DatiDocType datiDocType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);
		if (documentoType != null) { //E' un titolo
			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);
			if (datiDocType != null) {
				if (datiDocType.getT3XX() != null){
					for (int i = 0; i < datiDocType.getT3XXCount(); i++) {
						SbnTipoNota tipoNota = datiDocType.getT3XX(i).getTipoNota();
						if (tipoNotaA.toString().equals(tipoNota.toString())) {
							// areaNote = datiDocType.getT3XX()[i].getA_3XX().toString();
							areaNote = areaNote + datiDocType.getT3XX()[i].getA_3XX().toString();
						}
					}
				}
			}
		}
		return areaNote;
	}
//	GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274




	/**
	 * DOCUMENT ME!
	 *
	 * @param datiDocType
	 *            DOCUMENT ME!
	 *
	 * @return Area completa del titolo !
	 */

	public String getAreaPubblicazioneCompleta(String BID, SBNMarc sbnMarcType) {
		String AreaPubblicazione = IID_STRINGAVUOTA;
		DocumentoType documentoType = null;
		DatiDocType datiDocType = null;
		documentoType = utilityCastor.getElementoDocumento(BID, sbnMarcType);

		if (documentoType != null) {
			// E' un titolo

			// datiDocType =
			// documentoType.getDocumentoTypeChoice().getDatiDocumento();
			// Modifica di Peppe
			datiDocType = this.getDatiDocTypeDocumento(documentoType, BID);

			if (datiDocType != null) {
				AreaPubblicazione = this.getAreaPubblicazione(datiDocType);
			}
		}
		return AreaPubblicazione;
	}
	/**
	 * Restituisce il TitAccessoType di un oggetto Documento dati i
	 * corrispondenti DocumentoType e BID. Restituisce null se non esiste.
	 *
	 * @param documentoType
	 *            Oggetto DocumentoType del Documento.
	 * @param BID
	 *            Identificativo del Documento.
	 * @return titAccessoType Oggetto TitAccessoType del Documento.
	 */

	public TitAccessoType getTitAccessoTypeDocumento(
			DocumentoType documentoType, String BID) {

		TitAccessoType titAccessoType = null;

		// CONTROLLA L'ELEMENTO PADRE
		if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
			titAccessoType = documentoType.getDocumentoTypeChoice().getDatiTitAccesso();
			if (!titAccessoType.getT001().equals(BID)) {
				titAccessoType = null;
			}
		}

		// SE NON E' STATO TROVATO L'ID SELEZIONATO,
		// CONTROLLA I LEGAMI DEL DOCUMENTO.
		if (titAccessoType == null) {
			for (int x = 0; x < documentoType.getLegamiDocumentoCount(); x++) {
				for (int y = 0; y < documentoType.getLegamiDocumento(x)
						.getArrivoLegameCount(); y++) {

					// L'ELEMENTO LEGATO E' UN TITOLO ACCESSO
					if (documentoType.getLegamiDocumento(x).getArrivoLegame(y)
							.getLegameTitAccesso() != null) {

						// Legame TitAccesso
						titAccessoType = documentoType.getLegamiDocumento(x)
								.getArrivoLegame(y).getLegameTitAccesso()
								.getTitAccessoLegato().getDocumentoTypeChoice()
								.getDatiTitAccesso();
						if (titAccessoType.getT001().equals(BID)) {
							break;
						} else {
							titAccessoType = null;
						}
					}

					// L'ELEMENTO LEGATO E' UN DOCUMENTO
					else if (documentoType.getLegamiDocumento(x)
							.getArrivoLegame(y).getLegameDoc() != null) {

						// Richiama se stesso per controllare
						// negli eventuali legami del legame a
						// Documento trovato.
						titAccessoType = getTitAccessoTypeDocumento(
								documentoType.getLegamiDocumento(x)
										.getArrivoLegame(y).getLegameDoc()
										.getDocumentoLegato(), BID);

						// Se il TitAccessoType tornato non è null,
						// vuol dire che l'elemento è stato
						// trovato quindi interrompe il ciclo.
						if (titAccessoType != null) {
							break;
						}
					}
				}
			}
		}// end if

		return titAccessoType;
	}
	public String getTitoloResponsabilitaXReticolo(DatiDocType datiDocType) {
		String TitoloResponsabilita = IID_STRINGAVUOTA;

		if (datiDocType instanceof DatiDocType) {
			C200 c200 = datiDocType.getT200();

			for (int k = 0; k < c200.getA_200Count(); k++) {
				if (k == 0) {
					TitoloResponsabilita = TitoloResponsabilita
							+ c200.getA_200(k);
				} else {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getA_200(k);
				}
			}

			for (int k = 0; k < c200.getB_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + "["
						+ c200.getB_200(k) + "]";
			}
// Inizio modifica almaviva2 19.11.2009 - la parte Autore viene asteriscata in questa posizione e riporata in coda (dopo area E)
//			// Titolo proprio di altro autore: VECCHIA GESTIONE
//			// Titolo proprio di altro autore: NUOVA GESTIONE
//			for (int k = 0; k < c200.getCf_200Count(); k++) {
//				TitoloResponsabilita = TitoloResponsabilita + " . "
//						+ c200.getCf_200(k).getC_200();
//				for (int k1 = 0; k1 < c200.getCf_200(k).getF_200Count(); k1++) {
//					TitoloResponsabilita = TitoloResponsabilita + " / "
//							+ c200.getCf_200(k).getF_200(k1);
//				}
//				for (int k2 = 0; k2 < c200.getCf_200(k).getG_200Count(); k2++) {
//					TitoloResponsabilita = TitoloResponsabilita + " ; "
//							+ c200.getCf_200(k).getG_200(k2);
//				}
//			}
// Fine modifica almaviva2 11.19.2009 - la parte Autore viene asteriscata in questa posizione e riporata in coda (dopo area E)

			for (int k = 0; k < c200.getD_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " = "
						+ c200.getD_200(k);
			}

			for (int k = 0; k < c200.getE_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " : "
						+ c200.getE_200(k);
			}


//			 Modifica almaviva2 19.11.2009 - la parte Autore viene spostata qui e viene inserita anche l'area F prima assente
			for (int k = 0; k < c200.getF_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " / "
						+ c200.getF_200(k);
			}

			// Titolo proprio di altro autore: VECCHIA GESTIONE
			// Titolo proprio di altro autore: NUOVA GESTIONE
			for (int k = 0; k < c200.getCf_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " . "
						+ c200.getCf_200(k).getC_200();
				for (int k1 = 0; k1 < c200.getCf_200(k).getF_200Count(); k1++) {
					TitoloResponsabilita = TitoloResponsabilita + " / "
							+ c200.getCf_200(k).getF_200(k1);
				}
				for (int k2 = 0; k2 < c200.getCf_200(k).getG_200Count(); k2++) {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getCf_200(k).getG_200(k2);
				}
			}
		}

		return TitoloResponsabilita;
	}

	public String getTitoloResponsabilitaDatiTitAccessoXReticolo(
			TitAccessoType titAccessoType, C200 c200) {
		String TitoloResponsabilita = IID_STRINGAVUOTA;

		// se i Dati sono di natura B leggo T454
		if (titAccessoType.getNaturaTitAccesso().toString().equals("B")) {
			// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
			// c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
			c200 = titAccessoType.getTitAccessoTypeChoice().getT454A().getT200();
		}

		// se i Dati sono di natura D leggo T517
		if (titAccessoType.getNaturaTitAccesso().toString().equals("D")) {
			c200 = titAccessoType.getTitAccessoTypeChoice().getT517();
		}

		// se i Dati sono di natura P leggo T510
		if (titAccessoType.getNaturaTitAccesso().toString().equals("P")) {
			c200 = titAccessoType.getTitAccessoTypeChoice().getT510();
		}

		// se i Dati sono di natura T leggo T423
		if (titAccessoType.getNaturaTitAccesso().toString().equals("T")) {
			c200 = titAccessoType.getTitAccessoTypeChoice().getT423().getT200();
		}

		// +++ Fine parte aggiunta da Maurizio
		if (titAccessoType instanceof TitAccessoType) {
			for (int k = 0; k < c200.getA_200Count(); k++) {
				if (k == 0) {
					TitoloResponsabilita = TitoloResponsabilita
							+ c200.getA_200(k);
				} else {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getA_200(k);
				}
			}

			for (int k = 0; k < c200.getB_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + "["
						+ c200.getB_200(k) + "]";
			}

			// Titolo proprio di altro autore: NUOVA GESTIONE
			for (int k = 0; k < c200.getCf_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " . "
						+ c200.getCf_200(k).getC_200();
				for (int k1 = 0; k1 < c200.getCf_200(k).getF_200Count(); k1++) {
					TitoloResponsabilita = TitoloResponsabilita + " / "
							+ c200.getCf_200(k).getF_200(k1);
				}
				for (int k2 = 0; k2 < c200.getCf_200(k).getG_200Count(); k2++) {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getCf_200(k).getG_200(k2);
				}
			}

			for (int k = 0; k < c200.getD_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " = "
						+ c200.getD_200(k);
			}

			for (int k = 0; k < c200.getE_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " : "
						+ c200.getE_200(k);
			}
		}
		return TitoloResponsabilita;
	}

	public String getTitoloResponsabilita(DatiDocType datiDocType) {
		String TitoloResponsabilita = IID_STRINGAVUOTA;

		if (datiDocType instanceof DatiDocType) {
			C200 c200 = datiDocType.getT200();

			for (int k = 0; k < c200.getA_200Count(); k++) {
				if (k == 0) {
					TitoloResponsabilita = TitoloResponsabilita
							+ c200.getA_200(k);
				} else {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getA_200(k);
				}
			}

			for (int k = 0; k < c200.getB_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + "["
						+ c200.getB_200(k) + "]";
			}

			for (int k = 0; k < c200.getD_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " = "
						+ c200.getD_200(k);
			}

			for (int k = 0; k < c200.getE_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " : "
						+ c200.getE_200(k);
			}

			for (int k = 0; k < c200.getF_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " / "
						+ c200.getF_200(k);
			}

			for (int k = 0; k < c200.getG_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " ; "
						+ c200.getG_200(k);
			}
			// // Titolo proprio di altro autore: NUOVA GESTIONE
			for (int k = 0; k < c200.getCf_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " . "
						+ c200.getCf_200(k).getC_200();
				for (int k1 = 0; k1 < c200.getCf_200(k).getF_200Count(); k1++) {
					TitoloResponsabilita = TitoloResponsabilita + " / "
							+ c200.getCf_200(k).getF_200(k1);
				}
				for (int k2 = 0; k2 < c200.getCf_200(k).getG_200Count(); k2++) {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getCf_200(k).getG_200(k2);
				}
			}
			for (int k = 0; k < c200.getH_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " . "
						+ c200.getH_200(k);
			}

			for (int k = 0; k < c200.getI_200Count(); k++) {
				if (c200.getH_200() != null) {
					TitoloResponsabilita = TitoloResponsabilita + " , "
							+ c200.getI_200(k);
				} else {
					TitoloResponsabilita = TitoloResponsabilita + " . "
							+ c200.getI_200(k);
				}
			}
		}

		return TitoloResponsabilita;
	}
	public String getTitoloResponsabilitaDatiTitAccesso(
			TitAccessoType titAccessoType, C200 c200) {
		String TitoloResponsabilita = IID_STRINGAVUOTA;

		// se i Dati sono di natura B leggo T454
		if (titAccessoType.getNaturaTitAccesso().toString().equals("B")) {
			// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
			// c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
			c200 = titAccessoType.getTitAccessoTypeChoice().getT454A().getT200();
		}

		// se i Dati sono di natura D leggo T517
		if (titAccessoType.getNaturaTitAccesso().toString().equals("D")) {
			c200 = titAccessoType.getTitAccessoTypeChoice().getT517();
		}

		// se i Dati sono di natura P leggo T510
		if (titAccessoType.getNaturaTitAccesso().toString().equals("P")) {
			c200 = titAccessoType.getTitAccessoTypeChoice().getT510();
		}

		// se i Dati sono di natura T leggo T423
		if (titAccessoType.getNaturaTitAccesso().toString().equals("T")) {
			c200 = titAccessoType.getTitAccessoTypeChoice().getT423().getT200();
		}

		// +++ Fine parte aggiunta da Maurizio
		if (titAccessoType instanceof TitAccessoType) {
			for (int k = 0; k < c200.getA_200Count(); k++) {
				if (k == 0) {
					TitoloResponsabilita = TitoloResponsabilita
							+ c200.getA_200(k);
				} else {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getA_200(k);
				}
			}

			for (int k = 0; k < c200.getB_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + "["
						+ c200.getB_200(k) + "]";
			}

			// Titolo proprio di altro autore
			for (int k = 0; k < c200.getCf_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " . "
						+ c200.getCf_200(k).getC_200();
				for (int k1 = 0; k1 < c200.getCf_200(k).getF_200Count(); k1++) {
					TitoloResponsabilita = TitoloResponsabilita + " / "
							+ c200.getCf_200(k).getF_200(k1);
				}
				for (int k2 = 0; k2 < c200.getCf_200(k).getG_200Count(); k2++) {
					TitoloResponsabilita = TitoloResponsabilita + " ; "
							+ c200.getCf_200(k).getG_200(k2);
				}
			}

			for (int k = 0; k < c200.getD_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " = "
						+ c200.getD_200(k);
			}

			for (int k = 0; k < c200.getE_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " : "
						+ c200.getE_200(k);
			}

			for (int k = 0; k < c200.getF_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " / "
						+ c200.getF_200(k);
			}

			for (int k = 0; k < c200.getG_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " ; "
						+ c200.getG_200(k);
			}

			for (int k = 0; k < c200.getH_200Count(); k++) {
				TitoloResponsabilita = TitoloResponsabilita + " . "
						+ c200.getH_200(k);
			}

			for (int k = 0; k < c200.getI_200Count(); k++) {
				if (c200.getH_200() != null) {
					TitoloResponsabilita = TitoloResponsabilita + " , "
							+ c200.getI_200(k);
				} else {
					TitoloResponsabilita = TitoloResponsabilita + " . "
							+ c200.getI_200(k);
				}
			}
		}

		return TitoloResponsabilita;
	}

	public String getAreaPubblicazione(DatiDocType datiDocType) {
		String AreaPubblicazione = IID_STRINGAVUOTA;
		String luogo = IID_STRINGAVUOTA;
		String editore = IID_STRINGAVUOTA;
		int s;
		int k;
		if (datiDocType instanceof DatiDocType) {
			C210[] c210 = datiDocType.getT210();

			for (k = 0; k < datiDocType.getT210Count(); k++) {
				for (s = 0; s < c210[k].getAc_210Count(); s++) {
					Ac_210Type ac_210Type = c210[k].getAc_210(s);
					// for a_210 luogo
					for (int j = 0; j < ac_210Type.getA_210Count(); j++) {
						if (s == 0) {
							// almaviva TEST PER ESCLUZIONE PRIMA PUNTEGGIATURA
							// luogo = ". - " + ac_210Type.getA_210(j);
							luogo = luogo + ac_210Type.getA_210(j);
						} else {
							luogo = luogo + " ; " + ac_210Type.getA_210(j);
						}
					}
					// for c_210 editore
					for (int j = 0; j < ac_210Type.getC_210Count(); j++) {
						editore = editore + " : " + ac_210Type.getC_210(j);
					}
					AreaPubblicazione = AreaPubblicazione + luogo + editore;
					luogo = IID_STRINGAVUOTA;
					editore = IID_STRINGAVUOTA;
				}

				for (s = 0; s < c210[k].getD_210Count(); s++) {
					if (s == 0) {
						// AreaPubblicazione = AreaPubblicazione + ", "
						// +c210[k].getD_210(s);
						// TEST PER ELIMINAZIONE BUG 298 rimesso perchè non
						// funziona
						AreaPubblicazione = AreaPubblicazione + ", "
								+ c210[k].getD_210(s);
					} else {
						AreaPubblicazione = AreaPubblicazione + ", "
								+ c210[k].getD_210(s);
					}
				}
				boolean apertaTonda = false;
				for (s = 0; s < c210[k].getE_210Count(); s++) {
					if (s == 0) {
						AreaPubblicazione = AreaPubblicazione + " ("
								+ c210[k].getE_210(s);
						apertaTonda = true;
					}
					// if ((s > 0) || (s < (c210[k].getE_210Count() - 1))) {
					else {
						AreaPubblicazione = AreaPubblicazione + " ; "
								+ c210[k].getE_210(s);
					}
				}
				for (s = 0; s < c210[k].getG_210Count(); s++) {
					if ((s == 0) && (!apertaTonda)) {
						AreaPubblicazione = AreaPubblicazione + "("
								+ c210[k].getG_210(s);
						apertaTonda = true;
					} else {
						AreaPubblicazione = AreaPubblicazione + " : "
								+ c210[k].getG_210(s);
					}
				}
				for (s = 0; s < c210[k].getH_210Count(); s++) {
					if ((s == 0) && (!apertaTonda)) {
						AreaPubblicazione = AreaPubblicazione + " ("
								+ c210[k].getH_210(s);
						apertaTonda = true;
					} else {
						AreaPubblicazione = AreaPubblicazione + ", "
								+ c210[k].getH_210(s);
					}
				}
				if (apertaTonda) {
					AreaPubblicazione = AreaPubblicazione + ")";
					apertaTonda = false;
				}

			}
		}

		return AreaPubblicazione;
	}

	public String getLivelloAutCar() {
		return livelloAutCar;
	}

	public void setLivelloAutCar(String livelloAutCar) {
		this.livelloAutCar = livelloAutCar;
	}

	public String getLivelloAutGra() {
		return livelloAutGra;
	}

	public void setLivelloAutGra(String livelloAutGra) {
		this.livelloAutGra = livelloAutGra;
	}

	public String getLivelloAutMus() {
		return livelloAutMus;
	}

	public void setLivelloAutMus(String livelloAutMus) {
		this.livelloAutMus = livelloAutMus;
	}


	public C926 getIncipit(DettaglioIncipitVO elemDetInc, int i){
		// "N° movimento", // f_926
		// "N° prog. nel documento", // g_926
		// "Contesto musicale", // c_926
		// "Indicatore", // a_926
		// "N° composizione", // b_926
		// "Voce/strumento", // h_926
		// "Nome personaggio", // q_926
		// "Indicazione del movimento", // p_926
		// "Forma musicale", // i_926
		// "Tonalità", // l_926
		// "Chiave", // m_926
		// "Alterazioni", // n_926
		// "Misura", // o_926
		// "Incipit testuale" // r_926

		C926 riga = new C926();
		String IID_STRINGAVUOTA = "";
		String f926 = elemDetInc.getNumMovimento();
		String g926 = elemDetInc.getNumProgDocumento();
		String c926 = elemDetInc.getContestoMusicale();
		//String a926 = String.valueOf(++i);
		String b926 = elemDetInc.getNumComposizione();
		String h926 = elemDetInc.getVoceStrumentoSelez();
		String q926 = elemDetInc.getNomePersonaggio();
		String p926 = elemDetInc.getIndicazMovimento();
		String i926 = elemDetInc.getFormaMusicaleSelez();
		String l926 = elemDetInc.getTonalitaSelez();
		String m926 = elemDetInc.getChiave();
		String n926 = elemDetInc.getAlterazioni();
		String o926 = elemDetInc.getMisura();
		String r926 = elemDetInc.getIncipitTestuale();

		if ( !IID_STRINGAVUOTA.equals(f926) ){
			riga.setF_926(f926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(g926) ){
			riga.setG_926(g926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(c926) ){
			riga.setC_926(c926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(b926) ){
			riga.setB_926(b926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(h926) ){
			riga.setH_926(h926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(q926) ){
			riga.setQ_926(q926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(p926) ){
			riga.setP_926(p926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(i926) ){
			riga.setI_926(i926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(l926) ){
			riga.setL_926(l926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(m926) ){
			riga.setM_926(m926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(n926) ){
			riga.setN_926(n926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(o926) ){
			riga.setO_926(o926.trim());
		}
		if ( !IID_STRINGAVUOTA.equals(r926) ){
			riga.setR_926(r926.trim());
		}
		IndIncipit ind = null;
		riga.setA_926(ind);


		return riga;
	}

	public String getUriDigitalBorn() {
		return uriDigitalBorn;
	}


	// Inizio COPIATA Dalla prte del protocollo per riportare l'area musica correttamente;
    /**
     * Crea una stringa concatenando contenuto al prefisso e suffisso.<br>
     * Segue la formula:<br>
     * 'prefisso'+'contenuto'+'suffisso'
     */
    protected String concatena(String stringa, String contenuto, String prefisso, String suffisso) {
        if (prefisso.startsWith("."))
            if (stringa.endsWith("."))
                prefisso = prefisso.substring(1);
        return stringa + prefisso + contenuto + suffisso;
    }
    /**
     * Crea una stringa concatenando tutti gli elementi dell'array di stringhe
     * contenuto.<br>
     * Segue la formula:<br>
     * 'intro'+'primoElemento' [+ 'prefisso'+'altriElementi'+'suffisso']
     */
    protected String concatena(
        String stringa,
        String contenuto[],
        String intro,
        String prefisso,
        String suffisso) {
        if (intro.startsWith("."))
            if (stringa.endsWith("."))
                intro = intro.substring(1);
        StringBuffer sb = new StringBuffer(stringa);
        sb.append(intro);
        for (int i = 0; i < contenuto.length; i++) {
            if (i != 0)
                sb.append(prefisso);
            sb.append(contenuto[i] + suffisso);
        }
        return sb.toString();
    }
	// Fine COPIATA Dalla prte del protocollo per riportare l'area musica correttamente;

    public String componiStringaRicTitEsatta(String stringa) {

		String appoggio1 = "";
		String appoggio2 = "";
		Boolean trovato = false;
		for (int i = 0; i < stringa.length(); i++) {
			if (stringa.charAt(i) == '*') {
				i++;
				appoggio1 = stringa.substring(i, stringa.length());
				trovato = true;
				break;
			}
		}

		if (!trovato) {
			appoggio1 = stringa;
		}

		trovato = false;
		for (int i = 1; i < appoggio1.length()-1; i++) {
			if (appoggio1.charAt(i) == '/') {
				if (appoggio1.charAt(i-1) == ' ' && appoggio1.charAt(i+1) == ' ') {
					appoggio2 = appoggio1.substring(0, i-1);
					trovato = true;
					break;
				}
			} else if (appoggio1.charAt(i) == ':') {
				if (appoggio1.charAt(i-1) == ' ' && appoggio1.charAt(i+1) == ' ') {
					appoggio2 = appoggio1.substring(0, i-1);
					trovato = true;
					break;
				}
			} else if (appoggio1.charAt(i) == ';') {
				if (appoggio1.charAt(i-1) == ' ' && appoggio1.charAt(i+1) == ' ') {
					appoggio2 = appoggio1.substring(0, i-1);
					trovato = true;
					break;
				}
			}
		}
		if (trovato) {
			return appoggio2;
		} else {
			return appoggio1;
		}

    }

    public String componiTitSenzaAsterisco(String stringa) {
    	String appoTitoloOrigNew = "";
    	for (int i = 0; i < stringa.length(); i++) {
    		if (stringa.charAt(i) == '*') {
    			i++;
    			appoTitoloOrigNew = appoTitoloOrigNew + stringa.substring(i);
    			break;
    		} else {
    			appoTitoloOrigNew = appoTitoloOrigNew + stringa.charAt(i);
    		}
    	}
    	return appoTitoloOrigNew;
    }

	public String getAgenzia() {
		return agenzia;
	}

	public void setAgenzia(String agenzia) {
		this.agenzia = agenzia;
	}

	public String getNorme() {
		return norme;
	}

	public void setNorme(String norme) {
		this.norme = norme;
	}

	public String getAreaNote323() {
		return areaNote323;
	}

	public String getAreaNote327() {
		return areaNote327;
	}

	public String getAreaNote330() {
		return areaNote330;
	}

	public String getAreaNote336() {
		return areaNote336;
	}

	public String getAreaNote337() {
		return areaNote337;
	}

	public String getAreaNoteDATA() {
		return areaNoteDATA;
	}

	public String getAreaNoteFILI() {
		return areaNoteFILI;
	}

	public String getAreaNoteORIG() {
		return areaNoteORIG;
	}

	public String getAreaNotePOSS() {
		return areaNotePOSS;
	}

	public String getAreaNotePROV() {
		return areaNotePROV;
	}

	public String getTipoTestoLetterario() {
		return tipoTestoLetterario;
	}

	public String getFormaContenuto() {
		return formaContenuto;
	}

	public String getTipoContenuto() {
		return tipoContenuto;
	}

	public String getFormaContenutoBIS() {
		return formaContenutoBIS;
	}

	public String getTipoContenutoBIS() {
		return tipoContenutoBIS;
	}

	public String getMovimento() {
		return movimento;
	}

	public String getDimensione() {
		return dimensione;
	}

	public String getSensorialita1() {
		return sensorialita1;
	}

	public String getSensorialita2() {
		return sensorialita2;
	}

	public String getSensorialita3() {
		return sensorialita3;
	}

	public String getTipoMediazione() {
		return tipoMediazione;
	}

	public String getMovimentoBIS() {
		return movimentoBIS;
	}

	public String getDimensioneBIS() {
		return dimensioneBIS;
	}

	public String getSensorialitaBIS1() {
		return sensorialitaBIS1;
	}

	public String getSensorialitaBIS2() {
		return sensorialitaBIS2;
	}

	public String getSensorialitaBIS3() {
		return sensorialitaBIS3;
	}

	public String getTipoMediazioneBIS() {
		return tipoMediazioneBIS;
	}

	public String getLivelloAutAudiov() {
		return livelloAutAudiov;
	}

	public String getTipoVideo() {
		return tipoVideo;
	}

	public String getLunghezza() {
		return lunghezza;
	}

	public String getIndicatoreColore() {
		return indicatoreColore;
	}

	public String getIndicatoreSuono() {
		return indicatoreSuono;
	}

	public String getSupportoSuono() {
		return supportoSuono;
	}

	public String getLarghezzaDimensioni() {
		return larghezzaDimensioni;
	}

	public String getFormaPubblDistr() {
		return formaPubblDistr;
	}

	public String getTecnicaVideoFilm() {
		return tecnicaVideoFilm;
	}

	public String getPresentImmagMov() {
		return presentImmagMov;
	}

	public String getPubblicVideoreg() {
		return pubblicVideoreg;
	}

	public String getMaterialeEmulsBase() {
		return materialeEmulsBase;
	}

	public String getStandardTrasmiss() {
		return standardTrasmiss;
	}

	public String getVersioneAudiovid() {
		return versioneAudiovid;
	}

	public String getSpecCatColoreFilm() {
		return specCatColoreFilm;
	}

	public String getTipoPellicStampa() {
		return tipoPellicStampa;
	}

	public String getPresentVideoreg() {
		return presentVideoreg;
	}

	public String getElementiProd() {
		return elementiProd;
	}

	public String getEmulsionePellic() {
		return emulsionePellic;
	}

	public String getComposPellic() {
		return composPellic;
	}

	public String getSuonoImmagMovimento() {
		return suonoImmagMovimento;
	}

	public String getLivelloAutElettr() {
		return livelloAutElettr;
	}

	public String getMaterialeSupportSec() {
		return materialeSupportSec;
	}

	public String[] getArrMaterAccompagn() {
		return arrMaterAccompagn;
	}

	public String getFormaPubblicazioneDisco() {
		return formaPubblicazioneDisco;
	}

	public String getVelocita() {
		return velocita;
	}

	public String getTipoSuono() {
		return tipoSuono;
	}

	public String getLarghezzaScanal() {
		return larghezzaScanal;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public String getLarghezzaNastro() {
		return larghezzaNastro;
	}

	public String getTipoDiMateriale() {
		return tipoDiMateriale;
	}

	public String getTipoDiTaglio() {
		return tipoDiTaglio;
	}

	public String getTecnicaRegistraz() {
		return tecnicaRegistraz;
	}

	public String getSpecCarattRiprod() {
		return specCarattRiprod;
	}

	public String getDatiCodifRegistrazSonore() {
		return datiCodifRegistrazSonore;
	}

	public String getDurataRegistraz() {
		return durataRegistraz;
	}

	public String getConfigurazNastro() {
		return configurazNastro;
	}

	public String[] getArrMaterTestAccompagn() {
		return arrMaterTestAccompagn;
	}

	public void setArrMaterTestAccompagn(String[] arrMaterTestAccompagn) {
		this.arrMaterTestAccompagn = arrMaterTestAccompagn;
	}

	public String getTipoTestoRegSonora() {
		return tipoTestoRegSonora;
	}

	public void setTipoTestoRegSonora(String tipoTestoRegSonora) {
		this.tipoTestoRegSonora = tipoTestoRegSonora;
	}

	public String getTipoSupporto() {
		return tipoSupporto;
	}

	public void setTipoSupporto(String tipoSupporto) {
		this.tipoSupporto = tipoSupporto;
	}

	public String getTipoSupportoBIS() {
		return tipoSupportoBIS;
	}

	public void setTipoSupportoBIS(String tipoSupportoBIS) {
		this.tipoSupportoBIS = tipoSupportoBIS;
	}

	public int getLinkEsterniCount() {
		return linkEsterniCount;
	}

	public void setLinkEsterniCount(int linkEsterniCount) {
		this.linkEsterniCount = linkEsterniCount;
	}

	public String[] getLinkEsterniBD() {
		return linkEsterniBD;
	}

	public String[] getLinkEsterniID() {
		return linkEsterniID;
	}

	public String[] getLinkEsterniURL() {
		return linkEsterniURL;
	}

	public int getReperCartaceoCount() {
		return reperCartaceoCount;
	}

	public void setReperCartaceoCount(int reperCartaceoCount) {
		this.reperCartaceoCount = reperCartaceoCount;
	}

	public String[] getReperCartaceoAutTit() {
		return reperCartaceoAutTit;
	}

	public void setReperCartaceoAutTit(String[] reperCartaceoAutTit) {
		this.reperCartaceoAutTit = reperCartaceoAutTit;
	}

	public String[] getReperCartaceoData() {
		return reperCartaceoData;
	}

	public void setReperCartaceoData(String[] reperCartaceoData) {
		this.reperCartaceoData = reperCartaceoData;
	}

	public String[] getReperCartaceoPos() {
		return reperCartaceoPos;
	}

	public void setReperCartaceoPos(String[] reperCartaceoPos) {
		this.reperCartaceoPos = reperCartaceoPos;
	}

	public String getFormaOpera231() {
		return formaOpera231;
	}

	public String getDataOpera231() {
		return dataOpera231;
	}

	public String getAltreCarat231() {
		return altreCarat231;
	}

	public String getPubblicatoSiNo() {
		return pubblicatoSiNo;
	}


	public String getTipoRisorsaElettronica() {
		return tipoRisorsaElettronica;
	}
	public String getIndicazioneSpecificaMateriale() {
		return indicazioneSpecificaMateriale;
	}
	public String getColoreElettronico() {
		return coloreElettronico;
	}
	public String getDimensioniElettronico() {
		return dimensioniElettronico;
	}
	public String getSuonoElettronico() {
		return suonoElettronico;
	}

}


