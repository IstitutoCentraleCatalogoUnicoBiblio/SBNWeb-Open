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

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAutoriDao;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.A152;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A431;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.Ac_210Type;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C105;
import it.iccu.sbn.ejb.model.unimarcmodel.C105bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C115;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C121;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C124;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C125bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C126;
import it.iccu.sbn.ejb.model.unimarcmodel.C127;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C135;
import it.iccu.sbn.ejb.model.unimarcmodel.C140;
import it.iccu.sbn.ejb.model.unimarcmodel.C140bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C181;
import it.iccu.sbn.ejb.model.unimarcmodel.C182;
import it.iccu.sbn.ejb.model.unimarcmodel.C183;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C205;
import it.iccu.sbn.ejb.model.unimarcmodel.C206;
import it.iccu.sbn.ejb.model.unimarcmodel.C207;
import it.iccu.sbn.ejb.model.unimarcmodel.C208;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C215;
import it.iccu.sbn.ejb.model.unimarcmodel.C231;
import it.iccu.sbn.ejb.model.unimarcmodel.C321;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C423;
import it.iccu.sbn.ejb.model.unimarcmodel.C454A;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C923;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndicatorePubblicato;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSpecLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoNota321;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ValidationException;


/**
 * Classe che prende in input tutti i valori dalla form per l'inserimento dei
 * titoli e li inserisce nell'oggetto SBNMarc, che deve comunque essere
 * preventivamente dotato di intestazione (dal metodo chiamante). La classe
 * provvede anche alla validazione dell'SBNMarc.
 *
 * Esempio di utilizzo:
 *
 * SBNMarc sbnMarc = null; SBNRisposta sbnRisposta = null; try { sbnMarc =
 * creaIntestazione(); SBNBody sbnBody = new SBNBody(sbnMarc, "A", "Altro");
 *
 * sbnBody.setLivelloAutorita("51"); sbnBody.setIsadn("123"); [..]
 *
 * sbnBody.addToSBNMarc(); sbnRisposta = eseguiRichiestaServerSBN(sbnMarc, true,
 * frame); } catch (ValidationException ve) { System.out.println("ERROR >>" +
 * ve.getMessage() + ve.toString()); }
 *
 * @author Maurizio Alvino
 *
 */
public class VariazioneBodyTitoli {

	private static Logger log = Logger.getLogger(VariazioneBodyTitoli.class);

	public static final String IID_SPAZIO = " ";

    public static final String TIT_NATURA_M = "M";
    public static final String TIT_NATURA_S = "S";
    public static final String TIT_NATURA_C = "C";
    public static final String TIT_NATURA_W = "W";
    public static final String TIT_NATURA_T = "T";
    public static final String TIT_NATURA_F = "F";
    public static final String TIT_NATURA_P = "P";
    public static final String TIT_NATURA_N = "N";
    public static final String TIT_NATURA_D = "D";
    public static final String TIT_NATURA_A = "A";
    public static final String TIT_NATURA_B = "B";
	public static final int CODICE_ELABORAZIONE = 15;
	public static final int CODICE_LEGAME_TITOLO_TITOLO	 = 36;
	public static final int CODICE_LEGAME_NATURA_TITOLO_TITOLO = 38;

	/** DOCUMENT ME! */
	private SBNMarc sbnMarc = null;

	public static String PrimaDiVariare = "";

	public static String PrimaDiVariareAntico = "";

	public static String PrimaDiVariareNotaAntico = "";

	public static String XMLDaVariare = "";

	// VECTOR per tabella NUMERO STANDART TIPO E NOTA
	List<String> numeroStandart = new ArrayList<String>();

	// VECTOR per tabella NUMERO STANDART TIPO E NOTA
	List<String> notaStandart = new ArrayList<String>();

	// VECTOR per tabella NUMERO STANDART TIPO E NOTA
	List<String> numeroTipoStandart = new ArrayList<String>();

	// VECTOR per tabella NUMERO STANDART TIPO E NOTA
	List<String> paeseTipoStandart = new ArrayList<String>();

	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// al link dei documenti su Basi Esterne - Link verso base date digitali
	List<String> linkEsternoDb = new ArrayList<String>();
	List<String> linkEsternoId = new ArrayList<String>();
	List<String> linkEsternoURL = new ArrayList<String>();
	List<String> linkEsternoNota = new ArrayList<String>();

	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo
	List<String> reperCartaceoAutTit = new ArrayList<String>();
	List<String> reperCartaceoData = new ArrayList<String>();
	List<String> reperCartaceoPos = new ArrayList<String>();
	List<String> reperCartaceoNota = new ArrayList<String>();


	// VECTOR per tabella CAMPO IMPRONTA 1
	List<String> impronta1 = new ArrayList<String>();

	// VECTOR per tabella CAMPO IMPRONTA 2
	List<String> impronta2 = new ArrayList<String>();

	// VECTOR per tabella CAMPO IMPRONTA 3
	List<String> impronta3 = new ArrayList<String>();

	// VECTOR per tabella CAMPO NOTA IMPRONTA
	List<String> notaImpronta = new ArrayList<String>();

	// VECTOR per tabella Personaggio
	List<String> personaggi = new ArrayList<String>();

	List<String> timbriVocale = new ArrayList<String>();

	List<String> interprete = new ArrayList<String>();

	// Contiene i nuovi legami da trattare, siamo nel caso
	// della variazione di un autore legato a un titolo, caso
	// in cui lo stesso autore legato, deve essere cancellato e
	// poi successivamente re-inserito.
	private LegamiType[] arrayNewLegamiAutore = null;

	/** DOCUMENT ME! */
	private String natura = "";

	/** DOCUMENT ME! */
	private String timeStamp = "";

	/** DOCUMENT ME! */
	private String tipoMateriale = "";

	/** DOCUMENT ME! */
	private String BID = "";

	/** DOCUMENT ME! */
	private String norme = "";

	/** DOCUMENT ME! */
	private String areaTitolo = "";

	/** DOCUMENT ME! */
	private String noteInformative = "";

	/** DOCUMENT ME! */
	private String noteCatalogatore = "";

	/** DOCUMENT ME! */
	private String lingua1 = "";

	/** DOCUMENT ME! */
	private String lingua2 = "";

	/** DOCUMENT ME! */
	private String lingua3 = "";

	/** DOCUMENT ME! */
	private String isadn = "";

	/** DOCUMENT ME! */
	private String livelloAutoritaDocumento = "";
	private String livelloAutorita = "";

	/** DOCUMENT ME! */
	private String titoloOrdinamento = "";

	/** DOCUMENT ME! */
	private String titoloEstratto = "";

	/** DOCUMENT ME! */
	private String appellativo = "";

	/** DOCUMENT ME! */
	private String[] formaMusicale = null;

	/** DOCUMENT ME! */
	private String organicoSintetico = "";

	/** DOCUMENT ME! */
	private String organicoAnalitico = "";

	/** DOCUMENT ME! */
	private String numeroOrdine = "";

	/** DOCUMENT ME! */
	private String numeroOpera = "";

	/** DOCUMENT ME! */
	private String numeroCatalogo = "";

	/** DOCUMENT ME! */
	private String datazione = "";

	/** DOCUMENT ME! */
	private String tonalita = "";

	/** DOCUMENT ME! */
	private String sezioni = "";

	/** DOCUMENT ME! */
	private List repertorio = new ArrayList();

	/** DOCUMENT ME! */
	private List genereMateriale = new ArrayList();

	/** DOCUMENT ME! */
	private String tipoTesto = "";

	/** DOCUMENT ME! */
	private String tipoElaborazione = "";

	/** DOCUMENT ME! */
	private String stesura = "";

	/** DOCUMENT ME! */
	private String composito = "";

	/** DOCUMENT ME! */
	private String palinsesto = "";

	/** DOCUMENT ME! */
	private String presentazione = "";

	/** DOCUMENT ME! */
	private String materia = "";

	/** DOCUMENT ME! */
	private String illustrazioni = "";

	/** DOCUMENT ME! */
	private String notazioneMusicale = "";

	/** DOCUMENT ME! */
	private String legatura = "";

	/** DOCUMENT ME! */
	private String conservazione = "";

	/** DOCUMENT ME! */
	private String genereRappresentazione = "";

	/** DOCUMENT ME! */
	private String annoRappresentazione = "";

	/** DOCUMENT ME! */
	private String periodoRappresentazione = "";

	/** DOCUMENT ME! */
	private String sedeRappresentazione = "";

	/** DOCUMENT ME! */
	private String luogoRappresentazione = "";

	/** DOCUMENT ME! */
	private String notaRappresentazione = "";

	/** DOCUMENT ME! */
	private String occasioneRappresentazione = "";

	/** DOCUMENT ME! */
	private String personaggio = "";

	/** DOCUMENT ME! */
	private String timbroVocale = "";

	/** DOCUMENT ME! */
	private String idInterprete = "";

	/** DOCUMENT ME! */
	private C926[] incipit = null;

	/** DOCUMENT ME! */
	private String indicatoreIncipit = "";

	/** DOCUMENT ME! */
	private String numeroComposizioni = "";

	/** DOCUMENT ME! */
	private String contesto = "";

	/** DOCUMENT ME! */
	private String numeroMovimento = "";

	/** DOCUMENT ME! */
	private String numeroProgressivoMovimenti = "";

	/** DOCUMENT ME! */
	private String registroMusicale = "";

	/** DOCUMENT ME! */
	private String codiceFormaMusicale = "";

	/** DOCUMENT ME! */
	private String codiceTonalita = "";

	/** DOCUMENT ME! */
	private String chiaveMusicale = "";

	/** DOCUMENT ME! */
	private String alterazione = "";

	/** DOCUMENT ME! */
	private String misura = "";

	/** DOCUMENT ME! */
	private String tempoMusicale = "";

	/** DOCUMENT ME! */
	private String nomePersonaggio = "";

	/** DOCUMENT ME! */
	private String idIncipitLetterario = "";

	/** DOCUMENT ME! */
	private SbnSimile sbnSimile = SbnSimile.CONFERMA;

	/** DOCUMENT ME! */
	private String numeroSTD = "";

	/** DOCUMENT ME! */
	private String tipoSTD = "";

	/** DOCUMENT ME! */
	private String notaSTD = "";

	/** DOCUMENT ME! */
	private String paeseSTD = "";

	/** DOCUMENT ME! */
	private String tipoData = "";

	// /** DOCUMENT ME! */
	// private String tipoRespons = "";

	/** DOCUMENT ME! */
	private String data1 = "";

	/** DOCUMENT ME! */
	private String data2 = "";

	/** DOCUMENT ME! */
	private String areaEdizione = "";

	/** DOCUMENT ME! */
	private String areaPubblicazione = "";

	/** DOCUMENT ME! */
	private String areaDatiMatematici = "";

	/** DOCUMENT ME! */
	private String areaNumerazione = "";

	/** DOCUMENT ME! */
	private String areaMusica = "";

	/** DOCUMENT ME! */
	private String areaDescrizioneFisica = "";

	/** DOCUMENT ME! */
	private String areaNote = "";

//	 GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
	/**  DOCUMENT ME! */
	private String areaNote323 = "";
	/**  DOCUMENT ME! */
	private String areaNote327 = "";
	/**  DOCUMENT ME! */
	private String areaNote330 = "";
	/**  DOCUMENT ME! */
	private String areaNote336 = "";
	/**  DOCUMENT ME! */
	private String areaNote337 = "";
	/**  DOCUMENT ME! */
	private String areaNoteDATA = "";
	/**  DOCUMENT ME! */
	private String areaNoteORIG = "";
	/**  DOCUMENT ME! */
	private String areaNoteFILI = "";
	/**  DOCUMENT ME! */
	private String areaNotePROV = "";
	/**  DOCUMENT ME! */
	private String areaNotePOSS = "";
//	 GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274





	/** DOCUMENT ME! */
	private String fonteRecord = "";

	/** DOCUMENT ME! */
	private String genere1 = "";

	/** DOCUMENT ME! */
	private String genere2 = "";

	/** DOCUMENT ME! */
	private String genere3 = "";

	/** DOCUMENT ME! */
	private String genere4 = "";

	/** DOCUMENT ME! */
	private String tipoRecord = "";

	/** DOCUMENT ME! */
	private String paese = "";

	/** DOCUMENT ME! */
	private String pubblicazioneGovernativa = "";

	/** DOCUMENT ME! */
	private String indicatoreColoreCartografia = "";

	/** DOCUMENT ME! */
	private String meridianoOrigine = "";

	/** DOCUMENT ME! */
	private String supportoFisico = "";

	/** DOCUMENT ME! */
	private String tecnicaCreazione = "";

	/** DOCUMENT ME! */
	private String formaRiproduzione = "";

	/** DOCUMENT ME! */
	private String formaPubblicazione = "";

	/** DOCUMENT ME! */
	private String altitudine = "";

	/** DOCUMENT ME! */
	private String indicatoreTipoScala = "";

	/** DOCUMENT ME! */
	private String tipoScala = "";

	/** DOCUMENT ME! */
	private String scalaOrizzontale = "";

	/** DOCUMENT ME! */
	private String scalaVerticale = "";

	/** DOCUMENT ME! */
	private String coordinateOvest = "";

	/** DOCUMENT ME! */
	private String coordinateEst = "";

	/** DOCUMENT ME! */
	private String coordinateNord = "";

	/** DOCUMENT ME! */
	private String coordinateSud = "";

	/** DOCUMENT ME! */
	private String carattereImmagine = "";

	/** DOCUMENT ME! */
	private String forma = "";

	/** DOCUMENT ME! */
	private String piattaforma = "";

	/** DOCUMENT ME! */
	private String categoriaSatellite = "";

	// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
	private String proiezioneCarte = "";

	/** DOCUMENT ME! */
	private String designazioneMateriale = "";

	/** DOCUMENT ME! */
	private String supportoPrimario = "";

	/** DOCUMENT ME! */
	private String indicatoreColoreGrafica = "";

	// /** DOCUMENT ME! */
	// private String indicatoreTecnica = "";
	// /** DOCUMENT ME! */
	// private String indicatoreTecnicaStampe = "";
	/** DOCUMENT ME! */
	private String indicatoreTecnica1 = "";

	/** DOCUMENT ME! */
	private String indicatoreTecnica2 = "";

	/** DOCUMENT ME! */
	private String indicatoreTecnica3 = "";

	/** DOCUMENT ME! */
	private String indicatoreTecnicaStampe1 = "";

	/** DOCUMENT ME! */
	private String indicatoreTecnicaStampe2 = "";

	/** DOCUMENT ME! */
	private String indicatoreTecnicaStampe3 = "";

	/** DOCUMENT ME! */
	private String designazioneFunzione = "";

	/** DOCUMENT ME! */
	private String uriDigitalBorn = "";


//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	private String tipoTestoLetterario = "";

	// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
	// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
	// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
	private String tipoTestoRegSonora = "";


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

	// Inizio almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private String livelloAutAudiov = "";
	// tipo record g
	private String tipoVideo = "";
	private String lunghezza = "";
	private String indicatoreColore = "";
	private String indicatoreSuono = "";
	private String supportoSuono = "";
	private String larghezzaDimensioni = "";
	private String formaPubblDistr = "";
	private String tecnicaVideoFilm = "";
	private String presentImmagMov = "";
	private String materAccompagn1 = "";
	private String materAccompagn2 = "";
	private String materAccompagn3 = "";
	private String materAccompagn4 = "";
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

	// tipo record i/j
	private String formaPubblicazioneDisco = "";
	private String velocita = "";
	private String tipoSuono = "";
	private String larghezzaScanal = "";
	private String dimensioni = "";
	private String larghezzaNastro = "";
	private String configurazNastro = "";
	private String materTestAccompagn1 = "";
	private String materTestAccompagn2 = "";
	private String materTestAccompagn3 = "";
	private String materTestAccompagn4 = "";
	private String materTestAccompagn5 = "";
	private String materTestAccompagn6 = "";
	private String tecnicaRegistraz = "";
	private String specCarattRiprod = "";
	private String datiCodifRegistrazSonore = "";
	private String tipoDiMateriale = "";
	private String tipoDiTaglio = "";
	private String durataRegistraz = "";


	private String livelloAutElettr = "";
	// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
	// Gestione nuovi campi specifici per etichetta 135
	private String tipoRisorsaElettronica = "";
	private String indicazioneSpecificaMateriale = "";
	private String coloreElettronico = "";
	private String dimensioniElettronico = "";
	private String suonoElettronico = "";


	// Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro



	// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
    // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
	private String formaOpera231;
	private String dataOpera231;
	private String altreCarat231;



	/** DOCUMENT ME! */
	private boolean modifica = false;

	private AreaDatiVariazioneTitoloVO areaDatiPass;

	/**
	 * @return incipit
	 */
	public C926[] getIncipit() {
		return incipit;
	}

	/**
	 * @param incipit
	 */
	public void setIncipit(C926[] incipit) {
		this.incipit = incipit;
	}

	/**
	 * @return
	 */
	public String getNotazioneMusicale() {
		return notazioneMusicale;
	}

	/**
	 * @param notazioneMusicale
	 */
	public void setNotazioneMusicale(String notazioneMusicale) {
		this.notazioneMusicale = notazioneMusicale;
	}

	/**
	 * @return
	 */
	public String getTipoTesto() {
		return tipoTesto;
	}

	/**
	 * @param tipoTesto
	 */
	public void setTipoTesto(String tipoTesto) {
		this.tipoTesto = tipoTesto;
	}

	/**
	 * @return altitudine
	 */
	public String getAltitudine() {
		return altitudine;
	}

	/**
	 * @param altitudine
	 */
	public void setAltitudine(String altitudine) {
		this.altitudine = altitudine;
	}

	/**
	 * @return carattereImmagine
	 */
	public String getCarattereImmagine() {
		return carattereImmagine;
	}

	/**
	 * @param carattereImmagine
	 */
	public void setCarattereImmagine(String carattereImmagine) {
		this.carattereImmagine = carattereImmagine;
	}

	/**
	 * @return categoriaSatellite
	 */
	public String getCategoriaSatellite() {
		return categoriaSatellite;
	}

	/**
	 * @param categoriaSatellite
	 */
	public void setCategoriaSatellite(String categoriaSatellite) {
		this.categoriaSatellite = categoriaSatellite;
	}


	public String getProiezioneCarte() {
		return proiezioneCarte;
	}

	public void setProiezioneCarte(String proiezioneCarte) {
		this.proiezioneCarte = proiezioneCarte;
	}

	/**
	 * @return coordinateEst
	 */
	public String getCoordinateEst() {
		return coordinateEst;
	}

	/**
	 * @param coordinateEst
	 */
	public void setCoordinateEst(String coordinateEst) {
		this.coordinateEst = coordinateEst;
	}

	/**
	 * @return coordinateNord
	 */
	public String getCoordinateNord() {
		return coordinateNord;
	}

	/**
	 * @param coordinateNord
	 */
	public void setCoordinateNord(String coordinateNord) {
		this.coordinateNord = coordinateNord;
	}

	/**
	 * @return coordinateOvest
	 */
	public String getCoordinateOvest() {
		return coordinateOvest;
	}

	/**
	 * @param coordinateOvest
	 */
	public void setCoordinateOvest(String coordinateOvest) {
		this.coordinateOvest = coordinateOvest;
	}

	/**
	 * @return coordinateSud
	 */
	public String getCoordinateSud() {
		return coordinateSud;
	}

	/**
	 * @param coordinateSud
	 */
	public void setCoordinateSud(String coordinateSud) {
		this.coordinateSud = coordinateSud;
	}

	/**
	 * @return designazioneFunzione
	 */
	public String getDesignazioneFunzione() {
		return designazioneFunzione;
	}

	/**
	 * @param designazioneFunzione
	 */
	public void setDesignazioneFunzione(String designazioneFunzione) {
		this.designazioneFunzione = designazioneFunzione;
	}

	/**
	 * @return designazioneMateriale
	 */
	public String getDesignazioneMateriale() {
		return designazioneMateriale;
	}

	/**
	 * @param designazioneMateriale
	 */
	public void setDesignazioneMateriale(String designazioneMateriale) {
		this.designazioneMateriale = designazioneMateriale;
	}

	/**
	 * @return
	 */
	public String getForma() {
		return forma;
	}

	/**
	 * @param forma
	 */
	public void setForma(String forma) {
		this.forma = forma;
	}

	/**
	 * @return formaPubblicazione
	 */
	public String getFormaPubblicazione() {
		return formaPubblicazione;
	}

	/**
	 * @param formaPubblicazione
	 */
	public void setFormaPubblicazione(String formaPubblicazione) {
		this.formaPubblicazione = formaPubblicazione;
	}

	/**
	 * @return formaRiproduzione
	 */
	public String getFormaRiproduzione() {
		return formaRiproduzione;
	}

	/**
	 * @param formaRiproduzione
	 */
	public void setFormaRiproduzione(String formaRiproduzione) {
		this.formaRiproduzione = formaRiproduzione;
	}

	/**
	 * @return indicatoreColoreCartografia
	 */
	public String getIndicatoreColoreCartografia() {
		return indicatoreColoreCartografia;
	}

	/**
	 * @param indicatoreColoreCartografia
	 */
	public void setIndicatoreColoreCartografia(
			String indicatoreColoreCartografia) {
		this.indicatoreColoreCartografia = indicatoreColoreCartografia;
	}

	/**
	 * @return indicatoreColoreGrafica
	 */
	public String getIndicatoreColoreGrafica() {
		return indicatoreColoreGrafica;
	}

	/**
	 * @param indicatoreColoreGrafica
	 */
	public void setIndicatoreColoreGrafica(String indicatoreColoreGrafica) {
		this.indicatoreColoreGrafica = indicatoreColoreGrafica;
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
	 * @return indicatoreTecnica3
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
	 * @param indicatoreTecnica1
	 */
	public void setIndicatoreTecnica1(String indicatoreTecnica1) {
		this.indicatoreTecnica1 = indicatoreTecnica1;
	}

	/**
	 * @param indicatoreTecnica2
	 */
	public void setIndicatoreTecnica2(String indicatoreTecnica2) {
		this.indicatoreTecnica2 = indicatoreTecnica2;
	}

	/**
	 * @param indicatoreTecnica3
	 */
	public void setIndicatoreTecnica3(String indicatoreTecnica3) {
		this.indicatoreTecnica3 = indicatoreTecnica3;
	}

	/**
	 * @param indicatoreTecnicaStampe1
	 */
	public void setIndicatoreTecnicaStampe1(String indicatoreTecnicaStampe1) {
		this.indicatoreTecnicaStampe1 = indicatoreTecnicaStampe1;
	}

	/**
	 * @param indicatoreTecnicaStampe2
	 */
	public void setIndicatoreTecnicaStampe2(String indicatoreTecnicaStampe2) {
		this.indicatoreTecnicaStampe2 = indicatoreTecnicaStampe2;
	}

	/**
	 * @param indicatoreTecnicaStampe3
	 */
	public void setIndicatoreTecnicaStampe3(String indicatoreTecnicaStampe3) {
		this.indicatoreTecnicaStampe3 = indicatoreTecnicaStampe3;
	}

	/**
	 * @return indicatoreTipoScala
	 */
	public String getIndicatoreTipoScala() {
		return indicatoreTipoScala;
	}

	/**
	 * @param indicatoreTipoScala
	 */
	public void setIndicatoreTipoScala(String indicatoreTipoScala) {
		this.indicatoreTipoScala = indicatoreTipoScala;
	}

	/**
	 * @return meridianoOrigine
	 */
	public String getMeridianoOrigine() {
		return meridianoOrigine;
	}

	/**
	 * @param meridianoOrigine
	 */
	public void setMeridianoOrigine(String meridianoOrigine) {
		this.meridianoOrigine = meridianoOrigine;
	}

	/**
	 * @return piattaforma
	 */
	public String getPiattaforma() {
		return piattaforma;
	}

	/**
	 * @param piattaforma
	 */
	public void setPiattaforma(String piattaforma) {
		this.piattaforma = piattaforma;
	}

	/**
	 * @return pubblicazioneGovernativa
	 */
	public String getPubblicazioneGovernativa() {
		return pubblicazioneGovernativa;
	}

	/**
	 * @param pubblicazioneGovernativa
	 */
	public void setPubblicazioneGovernativa(String pubblicazioneGovernativa) {
		this.pubblicazioneGovernativa = pubblicazioneGovernativa;
	}

	/**
	 * @return scalaOrizzontale
	 */
	public String getScalaOrizzontale() {
		return scalaOrizzontale;
	}

	/**
	 * @param scalaOrizzontale
	 */
	public void setScalaOrizzontale(String scalaOrizzontale) {
		this.scalaOrizzontale = scalaOrizzontale;
	}

	/**
	 * @return scalaVerticale
	 */
	public String getScalaVerticale() {
		return scalaVerticale;
	}

	/**
	 * @param scalaVerticale
	 */
	public void setScalaVerticale(String scalaVerticale) {
		this.scalaVerticale = scalaVerticale;
	}

	/**
	 * @return supportoFisico
	 */
	public String getSupportoFisico() {
		return supportoFisico;
	}

	/**
	 * @param supportoFisico
	 */
	public void setSupportoFisico(String supportoFisico) {
		this.supportoFisico = supportoFisico;
	}

	/**
	 * @return supportoPrimario
	 */
	public String getSupportoPrimario() {
		return supportoPrimario;
	}

	/**
	 * @param supportoPrimario
	 */
	public void setSupportoPrimario(String supportoPrimario) {
		this.supportoPrimario = supportoPrimario;
	}

	/**
	 * @return tecnicaCreazione
	 */
	public String getTecnicaCreazione() {
		return tecnicaCreazione;
	}

	/**
	 * @param tecnicaCreazione
	 */
	public void setTecnicaCreazione(String tecnicaCreazione) {
		this.tecnicaCreazione = tecnicaCreazione;
	}

	/**
	 * @return tipoScala
	 */
	public String getTipoScala() {
		return tipoScala;
	}

	/**
	 * @param tipoScala
	 */
	public void setTipoScala(String tipoScala) {
		this.tipoScala = tipoScala;
	}

	/**
	 * @return fonteRecord
	 */
	public String getFonteRecord() {
		return fonteRecord;
	}

	/**
	 * @param fonteRecord
	 */
	public void setFonteRecord(String fonteRecord) {
		this.fonteRecord = fonteRecord;
	}

	/**
	 * Esempio : (SBNRequest.class, o)
	 *
	 * @param type
	 *            la classe dell'oggetto da istanziare
	 * @param object
	 *            l'oggetto da istanziare
	 */
	public Object newInstance(Class<?> type, Object o) {
		if (o == null) {
			try {
				return type.newInstance();
			} catch (InstantiationException e) {
				log.error("", e);
				return null;
			} catch (IllegalAccessException e) {
				log.error("", e);
				return null;
			}
		} else
			return o;
	}

	/**
	 * Il costruttore di SBNForm prende il parametro SBNMarc dal metodo
	 * chiamante
	 */
	public VariazioneBodyTitoli(SBNMarc sbnMarc, String natura, String tipoMateriale,
			SbnSimile sbnSimile, AreaDatiVariazioneTitoloVO areaDatiPass) {
		this.areaDatiPass = areaDatiPass;
		this.sbnMarc = sbnMarc;
		setNatura(natura);
		setTipoMateriale(tipoMateriale);
		setTipoControllo(sbnSimile);
		setModifica(modifica);
	}

	public VariazioneBodyTitoli() {
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param sbnSimile
	 *            DOCUMENT ME!
	 */
	public void setTipoControllo(SbnSimile sbnSimile) {
		this.sbnSimile = sbnSimile;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws ValidationException
	 *             DOCUMENT ME!
	 */
	private SbnMessageType addToSBNMarc_TitoloUniforme(String bidDaAssegnare, char natura) throws ValidationException {
		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		ModificaType modificaType = null;

		if (!isModifica()) {
			creaType = new CreaType();
			creaType.setTipoControllo(sbnSimile);

			creaTypeChoice = new CreaTypeChoice();
		} else {
			modificaType = new ModificaType();
			modificaType.setTipoControllo(sbnSimile);
		}

		// Istanzio gli oggetti per inserire i vari elementi XML:
		// Norme
		A152 a152 = null;
		// Area titolo
		A230 a230 = null;
		// Note informative
		A300 a300 = null;
		// Note catalogatore
		A830 a830 = null;
		// Lingue
		C101 c101 = null;
		// ISADN
		A015 a015 = null;

		// Norme
		if (ValidazioneDati.notEmpty(getNorme())) {
			a152 = new A152();
			a152.setA_152(getNorme());
		}
		// Area titolo
		if (ValidazioneDati.notEmpty(getAreaTitolo())) {
			a230 = new A230();
			a230.setA_230(getAreaTitolo());
		}
		// Note informative
		if (ValidazioneDati.notEmpty(getNoteInformative())) {
			a300 = new A300();
			a300.setA_300(getNoteInformative());
		}
		// Note catalogatore
		if (ValidazioneDati.notEmpty(getNoteCatalogatore())) {
			a830 = new A830();
			a830.setA_830(getNoteCatalogatore());
		}
		// Lingue
		if (ValidazioneDati.notEmpty(getLingua1())) {
			c101 = new C101();

			if (ValidazioneDati.notEmpty(getLingua1()))
				c101.addA_101(getLingua1());
			if (ValidazioneDati.notEmpty(getLingua2()))
				c101.addA_101(getLingua2());
			if (ValidazioneDati.notEmpty(getLingua3()))
				c101.addA_101(getLingua3());
		}
		// ISADN
		if (ValidazioneDati.notEmpty(getIsadn())) {
			a015 = new A015();
			a015.setA_015(getIsadn());
		}


		C231 c231 = null;
		c231 = new C231();
		if (ValidazioneDati.notEmpty(getAreaTitolo())) {
			c231.setA_231(getAreaTitolo());
		}
		if (ValidazioneDati.notEmpty(getFormaOpera231())) {
			c231.setC_231(getFormaOpera231());
		}
		if (ValidazioneDati.notEmpty(getDataOpera231())) {
			c231.setD_231(getDataOpera231());
		}
		//almaviva2 27.07.2017 adeguamento a Indice gestione 231
//		if (ValidazioneDati.notEmpty(getPaese())) {
//			c231.setE_231(getPaese());
//		}
		C102 c102 = null;
		if (ValidazioneDati.notEmpty(getPaese())) {
			c102 = new C102();
			c102.setA_102(getPaese());
		}


		if (ValidazioneDati.notEmpty(getAltreCarat231())) {
			c231.setK_231(getAltreCarat231());
		}

		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		A431 a431 = null;
		a431 = new A431();
		if (ValidazioneDati.notEmpty(getAreaTitolo())) {
			a431.setA_431(getAreaTitolo());
		}


		ElementAutType elementAutType = new ElementAutType();

		DatiElementoType datiElementoType = null;

		if (getTipoMateriale().equals("Altro")) {
			datiElementoType = new TitoloUniformeType();
		} else if (getTipoMateriale().equals("Musica")) {
			datiElementoType = new TitoloUniformeMusicaType();
		}

		if (!isModifica()) {
			datiElementoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
		} else {
			datiElementoType.setT001(areaDatiPass.getDetTitoloPFissaVO().getBid());
			datiElementoType.setT005(getTimeStamp());

			String codice = "";
			codice = areaDatiPass.getDetTitoloPFissaVO().getTipoMat();
			if (codice.equals("E")) {
				if (areaDatiPass.isVariazioneNotaAntico())
					datiElementoType.setStatoRecord(StatoRecord.V);
				if (areaDatiPass.isVariazioneCompAntico())
					datiElementoType.setStatoRecord(StatoRecord.C);
			} else {
				if (areaDatiPass.isVariazioneTuttiComp())
					datiElementoType.setStatoRecord(StatoRecord.C);
			}
		}

		if (datiElementoType instanceof TitoloUniformeType) {
			TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElementoType;
			if (c101 != null)
				titoloUniformeType.setT101(c101);
			//almaviva2 27.07.2017 adeguamento a Indice gestione 231
			if (c102 != null)
				titoloUniformeType.setT102(c102);
			if (a015 != null)
				titoloUniformeType.setT015(a015);
			if (a152 != null)
				titoloUniformeType.setT152(a152);

			// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
//			if (a230 != null)
//				titoloUniformeType.setT230(a230);
			if (natura == 'A') {
				if (c231 != null)
					titoloUniformeType.setT231(c231);
			} else {
				if (a431 != null) {
					titoloUniformeType.setT431(a431);
					titoloUniformeType.setNaturaTU("V");
				}
			}

			if (a300 != null)
				titoloUniformeType.setT300(a300);
			if (a830 != null)
				titoloUniformeType.setT830(a830);
			titoloUniformeType.setTipoAuthority(SbnAuthority.TU);
			titoloUniformeType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
			elementAutType.setDatiElementoAut(titoloUniformeType);
		} else if (datiElementoType instanceof TitoloUniformeMusicaType) {
			A928 a928 = null;
			A929 a929 = null;

			if (ValidazioneDati.notEmpty(getTitoloOrdinamento())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setG_929(getTitoloOrdinamento());
			}

			if (ValidazioneDati.notEmpty(getTitoloEstratto())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setH_929(getTitoloEstratto());
			}

			if (ValidazioneDati.notEmpty(getAppellativo())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setI_929(getAppellativo());
			}

			if (getFormaMusicale() != null) {
				a928 = (A928) newInstance(A928.class, a928);
				for (int i = 0; i < getFormaMusicale().length; i++)
					if (ValidazioneDati.notEmpty(getFormaMusicale()[i])) {
						a928.addA_928(getFormaMusicale()[i]);
					}
			}

			if (ValidazioneDati.notEmpty(getOrganicoSintetico())) {
				a928 = (A928) newInstance(A928.class, a928);
				a928.setB_928(getOrganicoSintetico());
			}

			if (ValidazioneDati.notEmpty(getOrganicoAnalitico())) {
				a928 = (A928) newInstance(A928.class, a928);
				a928.setC_928(getOrganicoAnalitico());
			}

			if (ValidazioneDati.notEmpty(getNumeroOrdine())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setA_929(getNumeroOrdine());
			}

			if (ValidazioneDati.notEmpty(getNumeroOpera())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setB_929(getNumeroOpera());
			}

			if (ValidazioneDati.notEmpty(getNumeroCatalogo())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setC_929(getNumeroCatalogo());
			}

			if (ValidazioneDati.notEmpty(getDatazione())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setD_929(getDatazione());
			}

			if (ValidazioneDati.notEmpty(getTonalita())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setE_929(getTonalita());
			}

			if (ValidazioneDati.notEmpty(getSezioni())) {
				a929 = (A929) newInstance(A929.class, a929);
				a929.setF_929(getSezioni());
			}

			TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElementoType;
			if (c101 != null)
				titoloUniformeMusicaType.setT101(c101);
			if (a015 != null)
				titoloUniformeMusicaType.setT015(a015);
			if (a152 != null)
				titoloUniformeMusicaType.setT152(a152);
			if (a230 != null)
				titoloUniformeMusicaType.setT230(a230);
			if (a300 != null)
				titoloUniformeMusicaType.setT300(a300);
			if (a830 != null)
				titoloUniformeMusicaType.setT830(a830);
			if (a928 != null)
				titoloUniformeMusicaType.setT928(a928);
			if (a929 != null)
				titoloUniformeMusicaType.setT929(a929);
			titoloUniformeMusicaType.setTipoAuthority(SbnAuthority.UM);

			// Modifica almaviva2 23.11.2009 - Mantis 3362 - inserito controllo sul null ( su UM non c'è il liv.aut per la specificita)
			if (getLivelloAutorita() == null || getLivelloAutorita().equals("")) {
				titoloUniformeMusicaType.setLivelloAut(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
			} else {
				titoloUniformeMusicaType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
			}

			elementAutType.setDatiElementoAut(titoloUniformeMusicaType);
		}

		// INIZIO Nuova gestione repertori per l'autore REPERTORI

		List<TabellaNumSTDImpronteOriginarioVO> listaRepertoriOld = areaDatiPass.getDetTitoloPFissaVO().getListaRepertoriOld();
		List<TabellaNumSTDImpronteAggiornataVO> listaRepertoriNew = areaDatiPass.getDetTitoloPFissaVO().getListaRepertoriNew();

		// totale dei repertori con le modifiche
		int countOld = 0;
		if (listaRepertoriOld != null) {
			countOld = listaRepertoriOld.size();
		}


		int countNew = 0;
		if (listaRepertoriNew != null) {
			countNew = listaRepertoriNew.size();
		}
		LegamiType[] arrayLegamiType = new LegamiType[countOld + countNew];


		FactorySbn indice = null;
		FactorySbn polo = null;
		SbnUserType user = null;
		SbnGestioneAutoriDao gestioneAutoriDao = new SbnGestioneAutoriDao(indice, polo, user);

		if (areaDatiPass.isModifica()) {
			if (countOld > 0) {
				gestioneAutoriDao.eliminaAllRepertori(arrayLegamiType, listaRepertoriOld,	bidDaAssegnare);
			}
			if (countNew > 0) {
				gestioneAutoriDao.addNewRepertori(arrayLegamiType, listaRepertoriNew,	bidDaAssegnare, countOld);
			}
		} else {
			if (countNew > 0) {
				gestioneAutoriDao.addNewRepertori(arrayLegamiType, listaRepertoriNew, bidDaAssegnare, 0);
			}
		}

		// Se è presente almeno un legame (tra vecchi e
		// nuovi) aggiungo l'array dei legami all'ElementAut.
		if ((countOld > 0) || (countNew > 0)) {
			elementAutType.setLegamiElementoAut(arrayLegamiType);
		}

		// FINE Nuova gestione repertori per l'autore






		// fine repertori
		SbnRequestType sbnrequest = new SbnRequestType();
		SbnMessageType sbnmessage = new SbnMessageType();
		if (!isModifica()) {
			creaTypeChoice.setElementoAut(elementAutType);
			creaType.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(creaType);
		} else {
			modificaType.setElementoAut(elementAutType);
			sbnrequest.setModifica(modificaType);
		}
		sbnmessage.setSbnRequest(sbnrequest);
//		sbnMarc.setSbnMessage(sbnmessage);
		return sbnmessage;

	}

	/**
	 * Aggiunge i nodi con i legami alla radice (titolo)
	 *
	 * @param arrayLegamiType
	 * @param tree
	 * @param count
	 */
	public LegamiType[] addLegami(AreaDatiLegameTitoloVO areaDatiPass) {

		UtilityAutori utilityAutori = new UtilityAutori();

		LegamiType[] arrayLegamiType = null;
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameDocType legameDocType;
		LegameTitAccessoType legameTitAccesso;
		LegameElementoAutType legameElementoAut;

		legamiType = new LegamiType();

		legamiType.setIdPartenza(areaDatiPass.getBidPartenza());
		if (areaDatiPass.getTipoOperazione().equals("Condividi")) {
			legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
		} else if (areaDatiPass.getTipoOperazione().equals("Sposta")) {
			legamiType.setTipoOperazione(SbnTipoOperazione.LISTA);
		} else {
			legamiType.setTipoOperazione(SbnTipoOperazione.valueOf(areaDatiPass.getTipoOperazione()));
		}


		arrivoLegame = new ArrivoLegame();
		int i = 0;

		// Operazioni differenti in base all'Authority da legare al titolo
		if (areaDatiPass.getAuthorityOggettoArrivo() != null &&
				!areaDatiPass.getAuthorityOggettoArrivo().equals("")) {

			if (areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.AU).toString())) {
				// //// LEGAME DOCUMENTO - AUTORE ///////////

				legameElementoAut = new LegameElementoAutType();

				// Se lo stato del nodo = MODIFICA
				// In questo caso  è prevista una cancellazione prima, e un inserimento poi
				// perchè il protocollo non consente di aggiornare alcuni dati di legame
				// Se lo stato del nodo = Sposta
				// In questo caso  è prevista una cancellazione prima, e un inserimento poi cambiando solo il vid
				// di arrivo del legame

				if (areaDatiPass.getTipoOperazione().equals((SbnTipoOperazione.MODIFICA).toString())
						|| areaDatiPass.getTipoOperazione().equals("Sposta")) {

					arrayLegamiType = new LegamiType[2];

					legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);
					legameElementoAut = new LegameElementoAutType();
					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(areaDatiPass.getTipoLegameOld()));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}

					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameOld());
					legameElementoAut.setTipoRespons(SbnRespons.valueOf(areaDatiPass.getTipoResponsOld()));
					legameElementoAut.setRelatorCode(areaDatiPass.getRelatorCodeOld());

					// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
					// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
					// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
					//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
					legameElementoAut.setStrumento(areaDatiPass.getSpecStrumVociOld());

					if (areaDatiPass.isIncertoOld()) {
						legameElementoAut.setIncerto(SbnIndicatore.S);
					} else {
						legameElementoAut.setIncerto(SbnIndicatore.N);
					}
					if (areaDatiPass.isSuperfluoOld()) {
						legameElementoAut.setSuperfluo(SbnIndicatore.S);
					} else {
						legameElementoAut.setSuperfluo(SbnIndicatore.N);
					}
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));

					arrivoLegame.setLegameElementoAut(legameElementoAut);

					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;

					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegame);
					arrayLegamiType[i++] = legamiType;


					// Nuova occorrenza per la parte di CREA
					legamiType = new LegamiType();
					legamiType.setIdPartenza(areaDatiPass.getBidPartenza());
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

					legameElementoAut = new LegameElementoAutType();

					if (areaDatiPass.getTipoOperazione().equals("Sposta")) {
						legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivoNew());
					} else {
						legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());
					}



					boolean isAutoreEnte = utilityAutori.isEnte(areaDatiPass.getTipoNomeArrivo());
					String tipoLegameTitAut = this.setTipoLegameConResponsabilita(areaDatiPass.getTipoResponsNew(), isAutoreEnte);
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegameTitAut));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}

					if (!isControlloOkResponsabilitaTipoNome(areaDatiPass.getTipoResponsNew(), areaDatiPass.getRelatorCodeNew()))
						return null;

					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
					legameElementoAut.setTipoRespons(SbnRespons.valueOf(areaDatiPass.getTipoResponsNew()));

					// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
					// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
					// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
					//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
					if (areaDatiPass.getRelatorCodeNew() != null) {
						if (!areaDatiPass.getRelatorCodeNew().equals("")) {
							legameElementoAut.setRelatorCode(areaDatiPass.getRelatorCodeNew());
							legameElementoAut.setStrumento(areaDatiPass.getSpecStrumVociNew());
						}
					}

					if (areaDatiPass.isIncertoNew()) {
						legameElementoAut.setIncerto(SbnIndicatore.S);
					} else {
						legameElementoAut.setIncerto(SbnIndicatore.N);
					}
					if (areaDatiPass.isSuperfluoNew()) {
						legameElementoAut.setSuperfluo(SbnIndicatore.S);
					} else {
						legameElementoAut.setSuperfluo(SbnIndicatore.N);
					}
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));

					arrivoLegame = new ArrivoLegame();
					arrivoLegame.setLegameElementoAut(legameElementoAut);
					ArrivoLegame[] arrayArrivoLegameNew = new ArrivoLegame[1];
					arrayArrivoLegameNew[0] = arrivoLegame;
					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegameNew);
					arrayLegamiType[i++] = legamiType;
				} else {
					arrayLegamiType = new LegamiType[1];
					if ((areaDatiPass.getTipoResponsNew() != null)
							&& (!areaDatiPass.getTipoResponsNew().equals(""))) {
						legameElementoAut.setTipoRespons(SbnRespons.valueOf(areaDatiPass.getTipoResponsNew()));
					}
					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
					if (areaDatiPass.isSuperfluoNew()) {
						legameElementoAut.setSuperfluo(SbnIndicatore.S);
					} else {
						legameElementoAut.setSuperfluo(SbnIndicatore.N);
					}
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
					if ((areaDatiPass.getRelatorCodeNew() != null) && (ValidazioneDati.notEmpty(areaDatiPass.getRelatorCodeNew()))) {
						legameElementoAut.setRelatorCode(areaDatiPass.getRelatorCodeNew());
						// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
						// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
						//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
						legameElementoAut.setStrumento(areaDatiPass.getSpecStrumVociNew());
					}
					if (areaDatiPass.isIncertoNew()) {
						legameElementoAut.setIncerto(SbnIndicatore.S);
					} else {
						legameElementoAut.setIncerto(SbnIndicatore.N);
					}
					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

					boolean isAutoreEnte = utilityAutori.isEnte(areaDatiPass.getTipoNomeArrivo());
					String tipoLegameTitAut = this.setTipoLegameConResponsabilita(areaDatiPass.getTipoResponsNew(), isAutoreEnte);
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegameTitAut));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}

					if (!isControlloOkResponsabilitaTipoNome(areaDatiPass.getTipoResponsNew(), areaDatiPass.getRelatorCodeNew()))
						return null;

					arrivoLegame.setLegameElementoAut(legameElementoAut);

					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;

					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegame);

					arrayLegamiType[i++] = legamiType;

				}// end else

			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.MA).toString())) {
				arrayLegamiType = new LegamiType[1];
				legameElementoAut = new LegameElementoAutType();
				legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
				legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
				legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("921"));

				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
				} else {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
				}

				legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

				arrivoLegame.setLegameElementoAut(legameElementoAut);
				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				// aggiungo arrivo legame
				legamiType.setArrivoLegame(arrayArrivoLegame);
				arrayLegamiType[i++] = legamiType;
			} else if ((areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.TU).toString())) ||
					(areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.UM).toString()))) {

				// Inizio modifica almaviva2 26.11.2010 BUG MANTIS 3993
				// nel caso di spostamento legame Authority TU/UM da un titolo ad un altro si deve prima cancellare
				// poi reinserire il nuovo legame (il malfunzionamento è stato risconntrato in fase di correzione del bug sopra citato;

				legameElementoAut = new LegameElementoAutType();
				// Modifica almaviva2 18.07.2012 BUG MANTIS esercizio 5054
				// Si devono differenziare le operazioni di sposta (si deve cancellare e poi inserire il legame) da tutte le altre operazioni
				// di modifica dei legami 500 (titolo a Authority A)  altrimenti messaggio da Indice:
				// non si può inserire o variare una nota al legame con un titolo uniforme:
				// l'applicativo da messagio di errore: Protocollo di INDICE: 3030 Errore: legame già esistente,
//				if (areaDatiPass.getTipoOperazione().equals((SbnTipoOperazione.MODIFICA).toString())
//						|| areaDatiPass.getTipoOperazione().equals("Sposta")) {

				if (areaDatiPass.getTipoOperazione().equals("Sposta")) {

					arrayLegamiType = new LegamiType[2];

					legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);
					legameElementoAut = new LegameElementoAutType();
					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

					// Il legame arriva non valorizzato !
//					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(areaDatiPass.getTipoLegameOld()));
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("500"));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}
					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameOld());
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));

					arrivoLegame.setLegameElementoAut(legameElementoAut);

					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;

					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegame);
					arrayLegamiType[i++] = legamiType;


					// Nuova occorrenza per la parte di CREA
					legamiType = new LegamiType();
					legamiType.setIdPartenza(areaDatiPass.getBidPartenza());
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

					legameElementoAut = new LegameElementoAutType();

					if (areaDatiPass.getTipoOperazione().equals("Sposta")) {
						legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivoNew());
					} else {
						legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());
					}

					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("500"));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}


					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));

					arrivoLegame = new ArrivoLegame();
					arrivoLegame.setLegameElementoAut(legameElementoAut);
					ArrivoLegame[] arrayArrivoLegameNew = new ArrivoLegame[1];
					arrayArrivoLegameNew[0] = arrivoLegame;
					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegameNew);
					arrayLegamiType[i++] = legamiType;

				} else {
					arrayLegamiType = new LegamiType[1];

					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("500"));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}

					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

					arrivoLegame.setLegameElementoAut(legameElementoAut);
					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;

					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegame);
					arrayLegamiType[i++] = legamiType;
				}


			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.LU).toString())) {
				arrayLegamiType = new LegamiType[1];
				legameElementoAut = new LegameElementoAutType();
				legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
				legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
				legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("620"));

				legameElementoAut.setRelatorCode(areaDatiPass.getRelatorCodeNew());

				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
				} else {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
				}

				legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

				arrivoLegame.setLegameElementoAut(legameElementoAut);
				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				// aggiungo arrivo legame
				legamiType.setArrivoLegame(arrayArrivoLegame);
				arrayLegamiType[i++] = legamiType;
            } else if (areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.SO).toString())) {
            	arrayLegamiType = new LegamiType[1];
                legameElementoAut = new LegameElementoAutType();
				legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
				legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
				legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("606"));

				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
				} else {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
				}

				legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());
				arrivoLegame.setLegameElementoAut(legameElementoAut);
				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				// aggiungo arrivo legame
				legamiType.setArrivoLegame(arrayArrivoLegame);
				arrayLegamiType[i++] = legamiType;
            } else if (areaDatiPass.getAuthorityOggettoArrivo().equals((SbnAuthority.CL).toString())) {
            	arrayLegamiType = new LegamiType[1];
            	legameElementoAut = new LegameElementoAutType();

            	legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
				if (areaDatiPass.isClasseDewey()){
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("676"));
				}else{
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("686"));
				}

				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
				} else {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
				}

				legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());
				arrivoLegame.setLegameElementoAut(legameElementoAut);
				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				// aggiungo arrivo legame
				legamiType.setArrivoLegame(arrayArrivoLegame);
				arrayLegamiType[i++] = legamiType;
            }

		} else {
			if (areaDatiPass.getTipoOperazione().equals("Sposta")) {
				arrayLegamiType = new LegamiType[2];
			} else {
				arrayLegamiType = new LegamiType[1];
			}

			DatiLegame datiLegame = new DatiLegame();
			if (datiLegame.isDocumento(areaDatiPass.getNaturaBidArrivo())) {
				legameDocType = new LegameDocType();
				legameDocType.setNoteLegame(areaDatiPass.getNoteLegameNew());

				String tipoLegame = "";
				try {
					tipoLegame = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO, areaDatiPass.getTipoLegameNew());
				} catch (Exception e) {
					log.error("", e);
				}
				if (tipoLegame.equals(areaDatiPass.getTipoLegameNew())) {
					legameDocType.setTipoLegame(SbnLegameDoc.valueOf(tipoLegame));
				} else {
					legameDocType.setTipoLegame(SbnLegameDoc.valueOf(tipoLegame.substring(1,4)));
				}


				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameDocType.setCondiviso(LegameDocTypeCondivisoType.S);
				} else {
					legameDocType.setCondiviso(LegameDocTypeCondivisoType.N);
				}

				legameDocType.setIdArrivo(areaDatiPass.getIdArrivo());
				if ((areaDatiPass.getSiciNew() != null)	&& (ValidazioneDati.notEmpty(areaDatiPass.getSiciNew())))
					legameDocType.setSici(areaDatiPass.getSiciNew());

				if (ValidazioneDati.isFilled(areaDatiPass.getSequenzaNew()))
					legameDocType.setSequenza(SBNMarcUtil.formattaSequenza(areaDatiPass.getSequenzaNew()));

				arrivoLegame.setLegameDoc(legameDocType);

				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				// aggiungo arrivo legame
				legamiType.setArrivoLegame(arrayArrivoLegame);

//				Per Sposta :Modifico il tipo operazione del primo legame da Modifica in Cancella e aggiungo il secondo legame in Crea
//				Per Diverso da Sposta :aggiungo solo il primo legame
				if (areaDatiPass.getTipoOperazione().equals("Sposta")) {
					legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);
					arrayLegamiType[i++] = legamiType;

					legamiType = new LegamiType();
					legamiType.setIdPartenza(areaDatiPass.getBidPartenza());
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

					legameDocType = new LegameDocType();
					legameDocType.setNoteLegame(areaDatiPass.getNoteLegameNew());

					if (tipoLegame.equals(areaDatiPass.getTipoLegameNew())) {
						legameDocType.setTipoLegame(SbnLegameDoc.valueOf(tipoLegame));
					} else {
						legameDocType.setTipoLegame(SbnLegameDoc.valueOf(tipoLegame.substring(1,4)));
					}


					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameDocType.setCondiviso(LegameDocTypeCondivisoType.S);
					} else {
						legameDocType.setCondiviso(LegameDocTypeCondivisoType.N);
					}

					legameDocType.setIdArrivo(areaDatiPass.getIdArrivoNew());
					if ((areaDatiPass.getSiciNew() != null)	&& (ValidazioneDati.notEmpty(areaDatiPass.getSiciNew())))
						legameDocType.setSici(areaDatiPass.getSiciNew());

					if (ValidazioneDati.isFilled(areaDatiPass.getSequenzaNew()))
						legameDocType.setSequenza(SBNMarcUtil.formattaSequenza(areaDatiPass.getSequenzaNew()));

					arrivoLegame = new ArrivoLegame();
					arrivoLegame.setLegameDoc(legameDocType);
					ArrivoLegame[] arrayArrivoLegameNew = new ArrivoLegame[1];
					arrayArrivoLegameNew[0] = arrivoLegame;
					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegameNew);
					arrayLegamiType[i++] = legamiType;

				} else {
					arrayLegamiType[i++] = legamiType;
				}

			} else if (datiLegame.isTitAccesso(areaDatiPass.getNaturaBidArrivo())) {
				legameTitAccesso = new LegameTitAccessoType();

				legameTitAccesso.setNoteLegame(areaDatiPass.getNoteLegameNew());
				String tipoLegame = "";
				try {
					tipoLegame = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO, areaDatiPass.getTipoLegameNew());
				} catch (Exception e) {
					log.error("", e);
				}

				if (tipoLegame.equals(areaDatiPass.getTipoLegameNew())) {
					legameTitAccesso.setTipoLegame(SbnLegameTitAccesso.valueOf(tipoLegame));
				} else {
					legameTitAccesso.setTipoLegame(SbnLegameTitAccesso.valueOf(tipoLegame.substring(1,4)));
				}


				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameTitAccesso.setCondiviso(LegameTitAccessoTypeCondivisoType.S);
				} else {
					legameTitAccesso.setCondiviso(LegameTitAccessoTypeCondivisoType.N);
				}

				legameTitAccesso.setIdArrivo(areaDatiPass.getIdArrivo());

				// idarrivo
				if ((areaDatiPass.getSequenzaMusicaNew() != null) && (ValidazioneDati.notEmpty(areaDatiPass.getSequenzaMusicaNew()))) {
					legameTitAccesso.setSequenzaMusica(areaDatiPass.getSequenzaMusicaNew());
				}


				if ((areaDatiPass.getSottoTipoLegameNew() != null) && (ValidazioneDati.notEmpty(areaDatiPass.getSottoTipoLegameNew()))) {
					legameTitAccesso.setSottoTipoLegame(SbnSpecLegameDoc.valueOf(areaDatiPass.getSottoTipoLegameNew()));
				}

				arrivoLegame.setLegameTitAccesso(legameTitAccesso);

				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				// aggiungo arrivo legame
				legamiType.setArrivoLegame(arrayArrivoLegame);

//				Per Sposta :Modifico il tipo operazione del primo legame da Modifica in Cancella e aggiungo il secondo legame in Crea
//				Per Diverso da Sposta :aggiungo solo il primo legame
				if (areaDatiPass.getTipoOperazione().equals("Sposta")) {
					legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);
					arrayLegamiType[i++] = legamiType;

					legamiType = new LegamiType();
					legamiType.setIdPartenza(areaDatiPass.getBidPartenza());
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

					legameTitAccesso = new LegameTitAccessoType();

					legameTitAccesso.setNoteLegame(areaDatiPass.getNoteLegameNew());

					if (tipoLegame.equals(areaDatiPass.getTipoLegameNew())) {
						legameTitAccesso.setTipoLegame(SbnLegameTitAccesso.valueOf(tipoLegame));
					} else {
						legameTitAccesso.setTipoLegame(SbnLegameTitAccesso.valueOf(tipoLegame.substring(1,4)));
					}


					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameTitAccesso.setCondiviso(LegameTitAccessoTypeCondivisoType.S);
					} else {
						legameTitAccesso.setCondiviso(LegameTitAccessoTypeCondivisoType.N);
					}

					legameTitAccesso.setIdArrivo(areaDatiPass.getIdArrivoNew());

					if ((areaDatiPass.getSequenzaMusicaNew() != null) && (ValidazioneDati.notEmpty(areaDatiPass.getSequenzaMusicaNew()))) {
						legameTitAccesso.setSequenzaMusica(areaDatiPass.getSequenzaMusicaNew());
					}
					if ((areaDatiPass.getSottoTipoLegameNew() != null) && (ValidazioneDati.notEmpty(areaDatiPass.getSottoTipoLegameNew()))) {
						legameTitAccesso.setSottoTipoLegame(SbnSpecLegameDoc.valueOf(areaDatiPass.getSottoTipoLegameNew()));
					}

					arrivoLegame.setLegameTitAccesso(legameTitAccesso);
					arrivoLegame = new ArrivoLegame();
					ArrivoLegame[] arrayArrivoLegameNew = new ArrivoLegame[1];
					arrayArrivoLegameNew[0] = arrivoLegame;
					// aggiungo arrivo legame
					legamiType.setArrivoLegame(arrayArrivoLegameNew);
					arrayLegamiType[i++] = legamiType;

				} else {
					arrayLegamiType[i++] = legamiType;
				}
			}
		}
		return arrayLegamiType;
	}

	public String setTipoLegameConResponsabilita(String responsabilita, boolean isAutoreEnte) {
			int valueCombo = Integer.parseInt(responsabilita);
			String tipoLegame = "";
			switch (valueCombo) {
				case 1:
					if (isAutoreEnte) {
						tipoLegame = "710";
					} else {
						tipoLegame = "700";
					}
					break;

				case 2:
					if (isAutoreEnte) {
						tipoLegame = "711";
					} else {
						tipoLegame = "701";
					}
					break;

				default:
					if (isAutoreEnte) {
						tipoLegame = "712";
					} else {
						tipoLegame = "702";
					}
			}
			return tipoLegame;
		}

    public boolean isControlloOkResponsabilitaTipoNome(String responsabilita, String relatorCode) {
        boolean esito = true;

        // almaviva2 Bug esercizio Liguria 46 - Riportato intervento su Software irchiesto da De Caro nel 2007
        // non si effettuano controlli su relator code per responsabilità 0
//        List<String>  v = new ArrayList<String>();
//        v.add("200");
//        v.add("820");
//        v.add("790");
//        v.add("280");
//        v.add("290");
//        v.add("390");
//        v.add("590");
//        v.add("040");
//        v.add("005");
//        v.add("275");
//        v.add("580");
//        v.add("190");
//        v.add("300");
//        v.add("250");
//        v.add("310");
//        v.add("320");
//        v.add("400");
//        v.add("110");
//        v.add("160");
//        v.add("500");
//        v.add("490");
//        v.add("420");

		// Con Codice di Responsabilità 4, gli unici
		// Relator Code ammessi sono i seguenti:
		// 340 - Editor
		// 650 - Editore
		// 700 - Scrittore
		// 750 - Tipografo
        if (responsabilita.equals("4")) {
        	if (!relatorCode.equals("")) {
        		// almaviva2 Febbraio 2020 - Nuove regole nella gestione del legame titolo-autore
        	    // Per i seguenti codici di relazione deve essere consentito solo il legame di responsabilità '4':
        	    // '160 Libraio' - '310 Distributore' - '610 Stampatore' - '620 Stampatore delle tavole' - '650 Editore' - '700 Copista'  
	            // if ((!relatorCode.equals("650")) &&	(!relatorCode.equals("340")) && (!relatorCode.equals("700")) && (!relatorCode.equals("750"))) {
	            if ((!relatorCode.equals("160")) &&	
	            		(!relatorCode.equals("310")) && 
	            		(!relatorCode.equals("610")) && 
	            		(!relatorCode.equals("620")) && 
	            		(!relatorCode.equals("650")) && 
	            		(!relatorCode.equals("700"))) {
	                esito = false;
	            }
        	}
        }
        // almaviva2 Bug esercizio Liguria 46 - Riportato intervento su Software irchiesto da De Caro nel 2007
        // non si effettuano controlli su relator code per responsabilità 0
//        else if (responsabilita.equals("0")) {
//        	if (!v.contains(relatorCode)) {
//                esito = false;
//            }
//        }

        return esito;
    }







	/**
	 * DOCUMENT ME!
	 *
	 * @throws ValidationException
	 *             DOCUMENT ME!
	 */
	private SbnMessageType addToSBNMarc_Documento(boolean abilPerTipoMat, boolean abilPerTipoMatMusicale) throws ValidationException {

		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		ModificaType modificaType = null;

		if (!isModifica()) {
			creaType = new CreaType();
			creaType.setTipoControllo(sbnSimile);

			creaTypeChoice = new CreaTypeChoice();
		} else {
			modificaType = new ModificaType();
			modificaType.setTipoControllo(sbnSimile);
		}

		// if (!getTipoMateriale().equals("U")) {
		DocumentoType documentoType = new DocumentoType();
		DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

		DatiDocType datiDocType = null;
		AnticoType anticoType = null;
		MusicaType musicaType = null;
		ModernoType modernoType = null;
		CartograficoType cartograficoType = null;
		GraficoType graficoType = null;
		// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		AudiovisivoType audiovisivoType = null;
		ElettronicoType elettronicoType = null;
		C115 c115 = null;
		C126 c126 = null;
		C127 c127 = null;

		GuidaDoc guidaDoc = new GuidaDoc();

		C100 c100 = null;
		C101 c101 = null;
		C102 c102 = null;
		C105 c105 = null;
		C105bis c105bis = null;
		C140 c140 = null;
		C140bis c140bis = null;
		C200 c200 = null;
		C205 c205 = null;
		C206 c206 = null;
		C207 c207 = null;
		C210 c210 = null;
		C215 c215 = null;
//		 GESTIONE NOTE AGGIUNTIVE 3204 ; almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//		C3XX c3xx = null;
//		 GESTIONE NOTE AGGIUNTIVE 3204 ; almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
		C801 c801 = null;
		C125 c125 = null;
		C128 c128 = null;
		C923 c923 = null;
		C922 c922 = null;
		C120 c120 = null;
		C121 c121 = null;
		C123 c123 = null;
		C124 c124 = null;
		C116 c116 = null;
		C208 c208 = null;
		C856 c856 = null;
		C135 c135 = null;

		// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
		// Gestione nuovi campi specifici per etichetta 135


		// Tipo record
		if (ValidazioneDati.notEmpty(getTipoRecord())) {
			guidaDoc.setTipoRecord(TipoRecord.valueOf(getTipoRecord()));
		}

		int numStandard = 0;
		NumStdType[] numStdType = new NumStdType[numStandard];
		if (areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard() != null
				&& areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size() > 0) {
			numStandard = areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size();
			numStdType = new NumStdType[numStandard];
			for (int i = 0; i < numStandard; i++) {
				if (ValidazioneDati.notEmpty(vectorGetNumeroSTD(i))) {
					numStdType[i] = new NumStdType();
					numStdType[i].setNumeroSTD(vectorGetNumeroSTD(i));
				}
				if (ValidazioneDati.notEmpty(vectorGetNotaSTD(i))) {
					numStdType[i].setNotaSTD(vectorGetNotaSTD(i));
				}

	            // Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
				if (ValidazioneDati.notEmpty(vectorGetTipoSTD(i))) {
					// numStdType[i].setTipoSTD(SbnTipoSTD.valueOf(vectorGetTipoSTD(i)));
					numStdType[i].setTipoSTD(vectorGetTipoSTD(i));
				}
			}
		}

		C012[] c012 = null;

		if (areaDatiPass.getDetTitoloPFissaVO().getListaImpronte() != null
				&& areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().size() > 0) {
			int numImpronta = areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().size();
			c012 = new C012[numImpronta];
			for (int i = 0; i < numImpronta; i++) {
				c012[i] = new C012();
				if (ValidazioneDati.notEmpty(vectorGetImpronta2(i))) {
					c012[i].setA_012_1(vectorGetImpronta2(i).substring(0,10));
					c012[i].setA_012_2(vectorGetImpronta2(i).substring(10,24));
					c012[i].setA_012_3(vectorGetImpronta2(i).substring(24));
				}
				if (vectorGetNotaImpronta(i) != null) {
					if (ValidazioneDati.notEmpty(vectorGetNotaImpronta(i))) {
						c012[i].setNota(vectorGetNotaImpronta(i));
					}

				}
			}
		}

		C927[] c927 = null;
		if (areaDatiPass.getDetTitoloMusVO().getListaPersonaggi() != null
				&& areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().size() > 0) {
			int numPersonaggi = areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().size();
			c927 = new C927[numPersonaggi];
			for (int i = 0; i < numPersonaggi; i++) {
				if (vectorGetPersonaggi(i) != null) {
					if (ValidazioneDati.notEmpty(vectorGetPersonaggi(i))) {
						c927[i] = new C927();
						c927[i].setA_927(vectorGetPersonaggi(i));
					}
					if (vectorGetTimbroVocale(i) != null) {
						if (ValidazioneDati.notEmpty(vectorGetTimbroVocale(i))) {
							c927[i].setB_927(vectorGetTimbroVocale(i));
						}
					}
					if (vectorGetInterprete(i) != null) {
						if (ValidazioneDati.notEmpty(vectorGetInterprete(i))) {
							c927[i].setC3_927(vectorGetInterprete(i));
						}
					}
				}
			}
		}

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if (areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi() != null
				&& areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi().size() > 0) {
			int numPersonaggi = areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi().size();
			c927 = new C927[numPersonaggi];
			for (int i = 0; i < numPersonaggi; i++) {
				if (vectorGetPersonaggi(i) != null) {
					if (ValidazioneDati.notEmpty(vectorGetPersonaggi(i))) {
						c927[i] = new C927();
						c927[i].setA_927(vectorGetPersonaggi(i));
					}
					if (vectorGetTimbroVocale(i) != null) {
						if (ValidazioneDati.notEmpty(vectorGetTimbroVocale(i))) {
							c927[i].setB_927(vectorGetTimbroVocale(i));
						}
					}
					if (vectorGetInterprete(i) != null) {
						if (ValidazioneDati.notEmpty(vectorGetInterprete(i))) {
							c927[i].setC3_927(vectorGetInterprete(i));
						}
					}
				}
			}
		}

		// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// al link dei documenti su Basi Esterne - Link verso base date digitali
		// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// ai repertori cartacei - Riferimento a repertorio cartaceo
		// calcolo del numero di righe delle note 321 totali
		C321[] c321 = null;
		int numRighe321 = 0;
		if (areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni() != null
				&& areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni().size() > 0) {
			numRighe321 = numRighe321 + areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni().size();
		}
		if (areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo() != null
				&& areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo().size() > 0) {
			numRighe321 = numRighe321 + areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo().size();
		}

		if (numRighe321  > 0) {

			c321 = new C321[numRighe321];
			int progIns = -1;
			for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni().size(); i++) {
				progIns++;
				c321[progIns] = new C321();
				if (ValidazioneDati.notEmpty(vectorGetLinkEsternoDb(i))) {
					c321[progIns].setA_321(vectorGetLinkEsternoDb(i));
					c321[progIns].setC_321(vectorGetLinkEsternoId(i));
					c321[progIns].setU_321(vectorGetLinkEsternoURL(i));
					c321[progIns].setTipoNota(TipoNota321.DATABASE);
				}
			}
			for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo().size(); i++) {
				progIns++;
				c321[progIns] = new C321();
				if (ValidazioneDati.notEmpty(vectorGetReperCartaceoAutTit(i))) {
					c321[progIns].setA_321(vectorGetReperCartaceoAutTit(i));
					c321[progIns].setB_321(vectorGetReperCartaceoData(i));
					c321[progIns].setC_321(vectorGetReperCartaceoPos(i));
					c321[progIns].setTipoNota(TipoNota321.REPERTORIO);
				}
			}
		}


		if (ValidazioneDati.notEmpty(getPaese())) {
			c102 = new C102();
			c102.setA_102(getPaese());
		}

		// Date

		if (ValidazioneDati.notEmpty(getTipoData())) {
			c100 = new C100();
			c100.setA_100_8(getTipoData()); // Tipo data

			if (ValidazioneDati.notEmpty(getData1())) {
				c100.setA_100_9(getData1()); // Data 1
			}

			if (ValidazioneDati.notEmpty(getData2())) {
				c100.setA_100_13(getData2()); // Data 2
			}
		}

		// Lingue
		if (ValidazioneDati.notEmpty(getLingua1())) {
			c101 = new C101();

			if (ValidazioneDati.notEmpty(getLingua1()))
				c101.addA_101(getLingua1());
			if (ValidazioneDati.notEmpty(getLingua2()))
				c101.addA_101(getLingua2());
			if (ValidazioneDati.notEmpty(getLingua3()))
				c101.addA_101(getLingua3());
		}

		// Generi
		// Genere per Tipo Materiale Moderno
		if (getTipoMateriale().equals("M")) {
			c105 = new C105();

			if (ValidazioneDati.notEmpty(getGenere1())) {
				c105.addA_105_4(getGenere1());
			}
			if (ValidazioneDati.notEmpty(getGenere2())) {
				c105.addA_105_4(getGenere2());
			}
			if (ValidazioneDati.notEmpty(getGenere3())) {
				c105.addA_105_4(getGenere3());
			}
			if (ValidazioneDati.notEmpty(getGenere4())) {
				c105.addA_105_4(getGenere4());
			}
		}

		// Genere per Tipo Materiale Antico
		if (getTipoMateriale().equals("E")) {
			c140 = new C140();

			if (ValidazioneDati.notEmpty(getGenere1())) {
				c140.addA_140_9(getGenere1());
			}
			if (ValidazioneDati.notEmpty(getGenere2())) {
				c140.addA_140_9(getGenere2());
			}
			if (ValidazioneDati.notEmpty(getGenere3())) {
				c140.addA_140_9(getGenere3());
			}
			if (ValidazioneDati.notEmpty(getGenere4())) {
				c140.addA_140_9(getGenere4());
			}
			// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
			// nel Materiale Antico il tipo testo letterario è registrato sulla 140bis e non sulla 105bis
			if (ValidazioneDati.notEmpty(getTipoTestoLetterario())) {
				c140bis = new C140bis();
				if (getTipoRecord().equals("a") || getTipoRecord().equals("b")) {
					c140bis.setA_140_17(getTipoTestoLetterario());
				}
			}
		}


        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
		// nel Materiale Antico il tipo testo letterario è registrato sulla 140bis e non sulla 105bis
		if (ValidazioneDati.notEmpty(getTipoTestoLetterario())) {
			if (getTipoTestoLetterario().length() == 2) {
				// Siamo nel caso della tabella da 2 caratteri quindi Materiale Antico (140bis)
				c140bis = new C140bis();
				if (getTipoRecord().equals("a") || getTipoRecord().equals("b")) {
					c140bis.setA_140_17(getTipoTestoLetterario());
				}
			}
			if (getTipoTestoLetterario().length() == 1) {
				// Siamo nel caso della tabella da 1 caratteri quindi Materiale diverso da Antico (105bis)
				c105bis = new C105bis();
				if (getTipoRecord().equals("a") || getTipoRecord().equals("b")) {
					c105bis.setA_105_11(getTipoTestoLetterario());
				}
			}
		}



//		C105bis c105bis = null;
//		if (!getTipoMateriale().equals("E")) {
//			if (ValidazioneDati.notEmpty(getTipoTestoLetterario())) {
//				c105bis = new C105bis();
//				if (getTipoRecord().equals("a") || getTipoRecord().equals("b")) {
//					c105bis.setA_105_11(getTipoTestoLetterario());
//				}
//
//			}
//		}


		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
		// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
		C125bis c125bis = null;
		if (getTipoRecord().equals("i")) {
			if (ValidazioneDati.notEmpty(getTipoTestoRegSonora())) {
				c125bis = new C125bis();
				c125bis.setB_125(getTipoTestoRegSonora());
			}
		}


		C181[] c181;

		if (ValidazioneDati.notEmpty(getFormaContenutoBIS())) {
			c181 = new C181[2];
			c181[0] = new C181();
			c181[1] = new C181();
		} else {
			c181 = new C181[1];
			c181[0] = new C181();
		}

		if (ValidazioneDati.notEmpty(getFormaContenuto())) {
			c181[0].setA_181_0(getFormaContenuto());
		}
		if (ValidazioneDati.notEmpty(getTipoContenuto())) {
			c181[0].setB_181_0(getTipoContenuto());
		}
		if (ValidazioneDati.notEmpty(getMovimento())) {
			c181[0].setB_181_1(getMovimento());
		}
		if (ValidazioneDati.notEmpty(getDimensione())) {
			c181[0].setB_181_2(getDimensione());
		}
		if (ValidazioneDati.notEmpty(getSensorialita1())) {
			c181[0].setB_181_3(getSensorialita1());
		}
		if (ValidazioneDati.notEmpty(getSensorialita2())) {
			c181[0].setB_181_4(getSensorialita2());
		}
		if (ValidazioneDati.notEmpty(getSensorialita3())) {
			c181[0].setB_181_5(getSensorialita3());
		}
		// 	dati BIS
		if (ValidazioneDati.notEmpty(getFormaContenutoBIS())) {
			if (ValidazioneDati.notEmpty(getFormaContenutoBIS())) {
				c181[1].setA_181_0(getFormaContenutoBIS());
			}
			if (ValidazioneDati.notEmpty(getTipoContenutoBIS())) {
				c181[1].setB_181_0(getTipoContenutoBIS());
			}
			if (ValidazioneDati.notEmpty(getMovimentoBIS())) {
				c181[1].setB_181_1(getMovimentoBIS());
			}
			if (ValidazioneDati.notEmpty(getDimensioneBIS())) {
				c181[1].setB_181_2(getDimensioneBIS());
			}
			if (ValidazioneDati.notEmpty(getSensorialitaBIS1())) {
				c181[1].setB_181_3(getSensorialitaBIS1());
			}
			if (ValidazioneDati.notEmpty(getSensorialitaBIS2())) {
				c181[1].setB_181_4(getSensorialitaBIS2());
			}
			if (ValidazioneDati.notEmpty(getSensorialitaBIS3())) {
				c181[1].setB_181_5(getSensorialitaBIS3());
			}
		}

		C182[] c182;

		if (ValidazioneDati.notEmpty(getTipoMediazioneBIS())) {
			c182 = new C182[2];
			c182[0] = new C182();
	        c182[1] = new C182();
			if (ValidazioneDati.notEmpty(getTipoMediazione())) {
				c182[0].setA_182_0(getTipoMediazione());
			}
			if (ValidazioneDati.notEmpty(getTipoMediazioneBIS())) {
				c182[1].setA_182_0(getTipoMediazioneBIS());
			}
		} else {
			c182 = new C182[1];
			c182[0] = new C182();

			if (ValidazioneDati.notEmpty(getTipoMediazione())) {
				c182[0].setA_182_0(getTipoMediazione());
			}
		}
//		C182[] c182 = new C182[2];
//		c182[0] = new C182();
//        c182[1] = new C182();
//		if (ValidazioneDati.notEmpty(getTipoMediazione())) {
//			c182[0].setA_182_0(getTipoMediazione());
//		}
//		if (ValidazioneDati.notEmpty(getTipoMediazioneBIS())) {
//			c182[1].setA_182_0(getTipoMediazioneBIS());
//		}

		// Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi


		// Inizio evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		C183[] c183;

		if (ValidazioneDati.notEmpty(getTipoSupportoBIS())) {
			c183 = new C183[2];
			c183[0] = new C183();
	        c183[1] = new C183();
			if (ValidazioneDati.notEmpty(getTipoSupporto())) {
				c183[0].setA_183_0(getTipoSupporto());
			}
			if (ValidazioneDati.notEmpty(getTipoSupportoBIS())) {
				c183[1].setA_183_0(getTipoSupportoBIS());
			}
		} else {
			c183 = new C183[1];
			c183[0] = new C183();

			if (ValidazioneDati.notEmpty(getTipoSupporto())) {
				c183[0].setA_183_0(getTipoSupporto());
			}
		}
		// Fine evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)


		// Gestione Aree
		// Titolo e indicazione di responsabilità
		if (ValidazioneDati.notEmpty(getAreaTitolo())) {
			c200 = new C200();
			c200.addA_200(getAreaTitolo());
			if (ValidazioneDati.notEmpty(getNoteInformative()))
				c200.addF_200(getNoteInformative());
			if (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("W")) {
				c200.setId1(Indicatore.VALUE_1);
			} else {
				c200.setId1(Indicatore.VALUE_2);
			}
		}

		// Area dell'edizione
		if (ValidazioneDati.notEmpty(getAreaEdizione())) {
			c205 = new C205();
			c205.setA_205(getAreaEdizione());
		}

		// Schema da 1.07 a 1.09 - Area dei dati matematici
		if (("e".equalsIgnoreCase(getTipoRecord()))
				|| ("f".equalsIgnoreCase(getTipoRecord()))) {
			if (getAreaDatiMatematici() != null &&
			    ValidazioneDati.notEmpty(getAreaDatiMatematici())) {
				c206 = new C206();
				c206.setA_206(getAreaDatiMatematici());
			}
		}

		// Schema da 1.07 a 1.09 - Area della numerazione
		if ("S".equalsIgnoreCase(getNatura())) {
			if (ValidazioneDati.notEmpty(getAreaNumerazione())) {
				c207 = new C207();
				c207.setA_207(new String[] { getAreaNumerazione() });
			}
		}

		// Area pubblicazione
		if (ValidazioneDati.notEmpty(getAreaPubblicazione())) {
			c210 = new C210();
			Ac_210Type ac_210Type = new Ac_210Type();
			ac_210Type.setA_210(new String[] { getAreaPubblicazione() });
			c210.addAc_210(ac_210Type);
			// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
			// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
			if (pubblicatoSiNo == null) {
				c210.setId2(null);
			} else {
				if (pubblicatoSiNo.equals("1")) {
					c210.setId2(IndicatorePubblicato.valueOf(pubblicatoSiNo));
				} else {
					c210.setId2(null);
				}
			}
		} else {
			// Gennaio 2018 manutenzione per gsetire il flag di poubblicazione anche quando l'area di pubblicazione non sia valorizzato
			c210 = new C210();
			Ac_210Type ac_210Type = new Ac_210Type();
			ac_210Type.setA_210(new String[] { "" });
			c210.addAc_210(ac_210Type);
			// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
			// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
			if (pubblicatoSiNo == null) {
				c210.setId2(null);
			} else {
				if (pubblicatoSiNo.equals("1")) {
					c210.setId2(IndicatorePubblicato.VALUE_0);
				} else {
					c210.setId2(null);
				}
			}
		}




		// Area descrizione fisica
		if (ValidazioneDati.notEmpty(getAreaDescrizioneFisica())) {
			c215 = new C215();
			c215.setA_215(new String[] { getAreaDescrizioneFisica() });
		}


//		 GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

//		// Area delle note
//		if (ValidazioneDati.notEmpty(getAreaNote())) {
//			c3xx = new C3XX();
//			c3xx.setA_3XX(getAreaNote());
//			c3xx.setTipoNota(SbnTipoNota.VALUE_0); // Impostato sempre così per
//													// DEFAULT
//		}

   	    int note =0;
	    if (ValidazioneDati.notEmpty(getAreaNote()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNote323()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNote327()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNote330()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNote336()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNote337()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNoteDATA()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNoteORIG()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNoteFILI()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNotePROV()))
			note++;
		if (ValidazioneDati.notEmpty(getAreaNotePOSS()))
			note++;

		C3XX[] c3xx = new C3XX[(note)];

		// Bug mantis esercizio 6171- almaviva2 aprile 2016
		// a seguito del'intervento di inserimento della nota 321 la valorizzazione dei codici nota nella classe SbnTipoNota
		// utilizzata per inviare il tipo di Nota al protocollo Polo/Indiceè cambiata; la valorizzazione del campo tipo nota viene quindi
		// effettuata con metodo valueOf indicando esplicitamente la nota in oggetto e non con il suo progressivo automatico
		// che ha subito variazioni (e potrebbe subirne altre);
		int k=0;
		if (ValidazioneDati.notEmpty(getAreaNote())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNote());
			// c3xx[k].setTipoNota(SbnTipoNota.VALUE_0);
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("300"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNote323())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNote323());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("323"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNote327())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNote327());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("327"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNote330())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNote330());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("330"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNote336())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNote336());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("336"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNote337())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNote337());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("337"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNoteDATA())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNoteDATA());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("DATA"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNoteORIG())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNoteORIG());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("ORIG"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNoteFILI())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNoteFILI());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("FILI"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNotePROV())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNotePROV());
			c3xx[k].setTipoNota(SbnTipoNota.valueOf("PROV"));
			k++;
		}
		if (ValidazioneDati.notEmpty(getAreaNotePOSS())) {
			c3xx[k] = new C3XX();
			c3xx[k].setA_3XX(getAreaNotePOSS());
			c3xx[k].setTipoNota(SbnTipoNota. valueOf("POSS"));
			k++;
		}
//		 GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

		// Fonte del record
		if (getFonteRecord() != null) {
			if (getFonteRecord().length() > 1) {
				c801 = new C801();
				c801.setA_801(getFonteRecord().substring(0, 2));
				c801.setB_801(getFonteRecord().substring(2));
			}
		}

		// Schema da 1.07 a 1.09 - Area della musica (Area ISBD)
		if (("c".equalsIgnoreCase(getTipoRecord()))
				|| ("d".equalsIgnoreCase(getTipoRecord()))) {
			if (ValidazioneDati.notEmpty(getAreaMusica())) {
				c208 = new C208();
				c208.setA_208(getAreaMusica());
			}
		}

//		 Area descrizione fisica
		if (ValidazioneDati.notEmpty(getUriDigitalBorn())) {
			byte[] indirizzo = getUriDigitalBorn().getBytes();
			c856 = new C856();
			c856.setU_856(getUriDigitalBorn());
			c856.setC9_856_1(indirizzo);
		}



		if (getTipoMateriale().equals("U")) {
			if (ValidazioneDati.notEmpty(getTipoTesto())) {
				c125 = (C125) newInstance(C125.class, c125);
				c125.setB_125(getTipoTesto());
			}
			if (ValidazioneDati.notEmpty(getPresentazione())) {
				c125 = (C125) newInstance(C125.class, c125);
				c125.setA_125_0(getPresentazione());
			}

			if (ValidazioneDati.notEmpty(getTipoElaborazione())) {
				c128 = (C128) newInstance(C128.class, c128);
				c128.setD_128(getTipoElaborazione());
			}
			if (ValidazioneDati.notEmpty(getOrganicoSintetico())) {
				c128 = (C128) newInstance(C128.class, c128);
				c128.setB_128(getOrganicoSintetico());
			}
			if (ValidazioneDati.notEmpty(getOrganicoAnalitico())) {
				c128 = (C128) newInstance(C128.class, c128);
				c128.setC_128(getOrganicoAnalitico());
			}

			if ("bd".indexOf(getTipoRecord()) != -1) {
				if (ValidazioneDati.notEmpty(getStesura())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setB_923(getStesura());
				}
				if (ValidazioneDati.notEmpty(getComposito())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setC_923(SbnIndicatore.valueOf(getComposito()));
				}
				if (ValidazioneDati.notEmpty(getPalinsesto())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setD_923(SbnIndicatore.valueOf(getPalinsesto()));
				}
				if (ValidazioneDati.notEmpty(getDatazione())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setE_923(getDatazione());
				}
				if (ValidazioneDati.notEmpty(getMateria())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setG_923(getMateria());
				}
				if (ValidazioneDati.notEmpty(getIllustrazioni())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setH_923(getIllustrazioni());
				}
				if (ValidazioneDati.notEmpty(getNotazioneMusicale())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setI_923(getNotazioneMusicale());
				}
				if (ValidazioneDati.notEmpty(getLegatura())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setL_923(getLegatura());
				}
				if (ValidazioneDati.notEmpty(getConservazione())) {
					c923 = (C923) newInstance(C923.class, c923);
					c923.setM_923(getConservazione());
				}
			}

			if (ValidazioneDati.notEmpty(getGenereRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setA_922(getGenereRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getAnnoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setP_922(getAnnoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getPeriodoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setQ_922(getPeriodoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getSedeRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setR_922(getSedeRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getLuogoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setS_922(getLuogoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getNotaRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setT_922(getNotaRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getOccasioneRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setU_922(getOccasioneRappresentazione());
			}
		}

		// Campi CARTOGRAFIA
		if (getTipoMateriale().equals("C")) {
			// Pubblicazione governativa
			if (ValidazioneDati.notEmpty(getPubblicazioneGovernativa()))
				c100.setA_100_20(getPubblicazioneGovernativa());

			// Indicatore di colore
			if (ValidazioneDati.notEmpty(getIndicatoreColoreCartografia())) {
				c120 = (C120) newInstance(C120.class, c120);
				c120.setA_120_0(getIndicatoreColoreCartografia());
			} else {
				c120 = (C120) newInstance(C120.class, c120);
				c120.setA_120_0(IID_SPAZIO);
			}

			// Meridiano d'origine
			if (ValidazioneDati.notEmpty(getMeridianoOrigine())) {
//				c120 = (C120) newInstance(C120.class, c120);
				c120.setA_120_9(getMeridianoOrigine());
			} else {
				c120.setA_120_9(IID_SPAZIO);
			}

			// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
			if (ValidazioneDati.notEmpty(getProiezioneCarte())) {
				c120.setA_120_7(getProiezioneCarte());
			}
			// almaviva2 - Novembre 2018 il campo ProiezioneCarte non è obligatorio quindi, nel caso sia vuoto non viene
			// inviato al protocollo il campo c120.setA_120_7
			// else {
			//	c120.setA_120_7(IID_SPAZIO);
			// }

			// Supporto fisico
			if (ValidazioneDati.notEmpty(getSupportoFisico())) {
				c121 = (C121) newInstance(C121.class, c121);
				c121.setA_121_3(getSupportoFisico());
			}

			// Tecnica creazione
			if (ValidazioneDati.notEmpty(getTecnicaCreazione())) {
				c121 = (C121) newInstance(C121.class, c121);
				c121.setA_121_5(getTecnicaCreazione());
			}

			// Forma riproduzione
			if (ValidazioneDati.notEmpty(getFormaRiproduzione())) {
				c121 = (C121) newInstance(C121.class, c121);
				c121.setA_121_6(getFormaRiproduzione());
			}

			// Forma pubblicazione
			if (ValidazioneDati.notEmpty(getFormaPubblicazione())) {
				c121 = (C121) newInstance(C121.class, c121);
				c121.setA_121_8(getFormaPubblicazione());
			}

			// Altitudine
			if (ValidazioneDati.notEmpty(getAltitudine())) {
				c121 = (C121) newInstance(C121.class, c121);
				c121.setB_121_0(getAltitudine());
			}

			// Indicatore tipo scala
			if (ValidazioneDati.notEmpty(getIndicatoreTipoScala())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setId1(Indicatore.valueOf(getIndicatoreTipoScala()));
			}

			// Tipo scala
			if (ValidazioneDati.notEmpty(getTipoScala())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setA_123(getTipoScala());
			}

			// Scala orizzontale
			if (ValidazioneDati.notEmpty(getScalaOrizzontale())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setB_123(getScalaOrizzontale());
			}

			// Scala verticale
			if (ValidazioneDati.notEmpty(getScalaVerticale())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setC_123(getScalaVerticale());
			}

			// Coordinate ovest
			if (ValidazioneDati.notEmpty(getCoordinateOvest())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setD_123(getCoordinateOvest());
			}

			// Coordinate est
			if (ValidazioneDati.notEmpty(getCoordinateEst())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setE_123(getCoordinateEst());
			}

			// Coordinate nord
			if (ValidazioneDati.notEmpty(getCoordinateNord())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setF_123(getCoordinateNord());
			}

			// Coordinate sud
			if (ValidazioneDati.notEmpty(getCoordinateSud())) {
				c123 = (C123) newInstance(C123.class, c123);
				c123.setG_123(getCoordinateSud());
			}

			// Carattere immagine
			if (ValidazioneDati.notEmpty(getCarattereImmagine())) {
				c124 = (C124) newInstance(C124.class, c124);
				c124.setA_124(getCarattereImmagine());
			}

			// Forma
			if (ValidazioneDati.notEmpty(getForma())) {
				c124 = (C124) newInstance(C124.class, c124);
				c124.setB_124(getForma());
			}

			// Piattaforma
			if (ValidazioneDati.notEmpty(getPiattaforma())) {
				c124 = (C124) newInstance(C124.class, c124);
				c124.setD_124(getPiattaforma());
			}

			// Categoria satellite
			if (ValidazioneDati.notEmpty(getCategoriaSatellite())) {
				c124 = (C124) newInstance(C124.class, c124);
				c124.setE_124(getCategoriaSatellite());
			}
		}
		if (getTipoMateriale().equals("G")) {
			// Grafica
			// Designazione materiale
			if (ValidazioneDati.notEmpty(getDesignazioneMateriale())) {
				c116 = (C116) newInstance(C116.class, c116);
				c116.setA_116_0(getDesignazioneMateriale());
			}

			// Supporto primario
			if (ValidazioneDati.notEmpty(getSupportoPrimario())) {
				c116 = (C116) newInstance(C116.class, c116);
				c116.setA_116_1(getSupportoPrimario());
			}

			// Indicatore di colore
			if (ValidazioneDati.notEmpty(getIndicatoreColoreGrafica())) {
				c116 = (C116) newInstance(C116.class, c116);
				c116.setA_116_3(getIndicatoreColoreGrafica());
			}

			// Indicatore di tecnica (disegni)
			if ((ValidazioneDati.notEmpty(getIndicatoreTecnica1()))
					|| (ValidazioneDati.notEmpty(getIndicatoreTecnica2()))
					|| (ValidazioneDati.notEmpty(getIndicatoreTecnica2()))) {
				c116 = (C116) newInstance(C116.class, c116);

				int elem = 0;
				if (ValidazioneDati.notEmpty(getIndicatoreTecnica1()))
					elem++;
				if (ValidazioneDati.notEmpty(getIndicatoreTecnica2()))
					elem++;
				if (ValidazioneDati.notEmpty(getIndicatoreTecnica3()))
					elem++;
				String[] arrIndicTecnica = new String[elem];

				if (ValidazioneDati.notEmpty(getIndicatoreTecnica1()))
					arrIndicTecnica[0] = getIndicatoreTecnica1();
				if (ValidazioneDati.notEmpty(getIndicatoreTecnica2()))
					arrIndicTecnica[1] = getIndicatoreTecnica2();
				if (ValidazioneDati.notEmpty(getIndicatoreTecnica3()))
					arrIndicTecnica[2] = getIndicatoreTecnica3();

				c116.setA_116_4(arrIndicTecnica);
			}

			// Indicatore di tecnica (stampe)
			if ((ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe1()))
					|| (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe2()))
					|| (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe3()))) {
				c116 = (C116) newInstance(C116.class, c116);

				int elem = 0;
				if (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe1()))
					elem++;
				if (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe2()))
					elem++;
				if (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe3()))
					elem++;
				String[] arrIndicTecnicaSt = new String[elem];

				if (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe1()))
					arrIndicTecnicaSt[0] = getIndicatoreTecnicaStampe1();
				if (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe2()))
					arrIndicTecnicaSt[1] = getIndicatoreTecnicaStampe2();
				if (ValidazioneDati.notEmpty(getIndicatoreTecnicaStampe3()))
					arrIndicTecnicaSt[2] = getIndicatoreTecnicaStampe3();

				c116.setA_116_10(arrIndicTecnicaSt);
			}

			// Designazione funzione
			if (ValidazioneDati.notEmpty(getDesignazioneFunzione())) {
				c116 = (C116) newInstance(C116.class, c116);
				c116.setA_116_16(getDesignazioneFunzione());
			}
		}

		// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
		if (getTipoMateriale().equals("H")) {
			if (getTipoRecord().equals("g")) {
				c115 = (C115) newInstance(C115.class, c115);
				if (ValidazioneDati.notEmpty(getTipoVideo())) {
					c115.setA_115_0(getTipoVideo());
				}
				if (ValidazioneDati.notEmpty(getLunghezza())) {
					c115.setA_115_1(getLunghezza());
				}
				if (ValidazioneDati.notEmpty(getIndicatoreColore())) {
					c115.setA_115_4(getIndicatoreColore());
				}
				if (ValidazioneDati.notEmpty(getIndicatoreSuono())) {
					c115.setA_115_5(getIndicatoreSuono());
				}
				if (ValidazioneDati.notEmpty(getSupportoSuono())) {
					c115.setA_115_6(getSupportoSuono());
				}
				if (ValidazioneDati.notEmpty(getLarghezzaDimensioni())) {
					c115.setA_115_7(getLarghezzaDimensioni());
				}
				if (ValidazioneDati.notEmpty(getFormaPubblDistr())) {
					c115.setA_115_8(getFormaPubblDistr());
				}
				if (ValidazioneDati.notEmpty(getTecnicaVideoFilm())) {
					c115.setA_115_9(getTecnicaVideoFilm());
				}
				if (ValidazioneDati.notEmpty(getPresentImmagMov())) {
					c115.setA_115_10(getPresentImmagMov());
				}
				if ((ValidazioneDati.notEmpty(getMaterAccompagn1()))
						|| (ValidazioneDati.notEmpty(getMaterAccompagn2()))
						|| (ValidazioneDati.notEmpty(getMaterAccompagn3()))
						|| (ValidazioneDati.notEmpty(getMaterAccompagn4()))) {
					int elem = 0;
					if (ValidazioneDati.notEmpty(getMaterAccompagn1()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterAccompagn2()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterAccompagn3()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterAccompagn4()))
						elem++;
					String[] arrMaterAccompagn = new String[elem];

					if (ValidazioneDati.notEmpty(getMaterAccompagn1()))
						arrMaterAccompagn[0] = getMaterAccompagn1();
					if (ValidazioneDati.notEmpty(getMaterAccompagn2()))
						arrMaterAccompagn[1] = getMaterAccompagn2();
					if (ValidazioneDati.notEmpty(getMaterAccompagn3()))
						arrMaterAccompagn[2] = getMaterAccompagn3();
					if (ValidazioneDati.notEmpty(getMaterAccompagn4()))
						arrMaterAccompagn[3] = getMaterAccompagn4();
					c115.setA_115_11(arrMaterAccompagn);
				}
				if (ValidazioneDati.notEmpty(getPubblicVideoreg())) {
					c115.setA_115_15(getPubblicVideoreg());
				}
				if (ValidazioneDati.notEmpty(getPresentVideoreg())) {
					c115.setA_115_16(getPresentVideoreg());
				}
				if (ValidazioneDati.notEmpty(getMaterialeEmulsBase())) {
					c115.setA_115_17(getMaterialeEmulsBase());
				}
				if (ValidazioneDati.notEmpty(getMaterialeSupportSec())) {
					c115.setA_115_18(getMaterialeSupportSec());
				}
				if (ValidazioneDati.notEmpty(getStandardTrasmiss())) {
					c115.setA_115_19(getStandardTrasmiss());
				}

				if (ValidazioneDati.notEmpty(getVersioneAudiovid())) {
					c115.setB_115_0(getVersioneAudiovid());
				}
				if (ValidazioneDati.notEmpty(getElementiProd())) {
					c115.setB_115_1(getElementiProd());
				}
				if (ValidazioneDati.notEmpty(getSpecCatColoreFilm())) {
					c115.setB_115_2(getSpecCatColoreFilm());
				}
				if (ValidazioneDati.notEmpty(getEmulsionePellic())) {
					c115.setB_115_3(getEmulsionePellic());
				}
				if (ValidazioneDati.notEmpty(getComposPellic())) {
					c115.setB_115_4(getComposPellic());
				}
				if (ValidazioneDati.notEmpty(getSuonoImmagMovimento())) {
					c115.setB_115_5(getSuonoImmagMovimento());
				}
				if (ValidazioneDati.notEmpty(getTipoPellicStampa())) {
					c115.setB_115_6(getTipoPellicStampa());
				}
			} else if (getTipoRecord().equals("i") || getTipoRecord().equals("j")) {
				c126 = (C126) newInstance(C126.class, c126);

				 if (ValidazioneDati.notEmpty(getFormaPubblicazioneDisco())) {
					 c126.setA_126_0(getFormaPubblicazioneDisco());
				 }
				 if (ValidazioneDati.notEmpty(getVelocita())) {
					 c126.setA_126_1(getVelocita());
				 }
				 if (ValidazioneDati.notEmpty(getTipoSuono())) {
					 c126.setA_126_2(getTipoSuono());
				 }
				 if (ValidazioneDati.notEmpty(getLarghezzaScanal())) {
					 c126.setA_126_3(getLarghezzaScanal());
				 }
				 if (ValidazioneDati.notEmpty(getDimensioni())) {
					 c126.setA_126_4(getDimensioni());
				 }
				 if (ValidazioneDati.notEmpty(getLarghezzaNastro())) {
					 c126.setA_126_5(getLarghezzaNastro());
				 }
				 if (ValidazioneDati.notEmpty(getConfigurazNastro())) {
					 c126.setA_126_6(getConfigurazNastro());
				 }

				if ((ValidazioneDati.notEmpty(getMaterTestAccompagn1()))
						|| (ValidazioneDati.notEmpty(getMaterTestAccompagn2()))
						|| (ValidazioneDati.notEmpty(getMaterTestAccompagn3()))
						|| (ValidazioneDati.notEmpty(getMaterTestAccompagn4()))
						|| (ValidazioneDati.notEmpty(getMaterTestAccompagn5()))
						|| (ValidazioneDati.notEmpty(getMaterTestAccompagn6()))) {
					int elem = 0;
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn1()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn2()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn3()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn4()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn5()))
						elem++;
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn6()))
						elem++;

					String[] arrMaterTestAccompagn = new String[elem];

					if (ValidazioneDati.notEmpty(getMaterTestAccompagn1()))
						arrMaterTestAccompagn[0] = getMaterTestAccompagn1();
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn2()))
						arrMaterTestAccompagn[1] = getMaterTestAccompagn2();
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn3()))
						arrMaterTestAccompagn[2] = getMaterTestAccompagn3();
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn4()))
						arrMaterTestAccompagn[3] = getMaterTestAccompagn4();
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn5()))
						arrMaterTestAccompagn[4] = getMaterTestAccompagn5();
					if (ValidazioneDati.notEmpty(getMaterTestAccompagn6()))
						arrMaterTestAccompagn[5] = getMaterTestAccompagn6();
					c126.setA_126_7(arrMaterTestAccompagn);
				}

				 if (ValidazioneDati.notEmpty(getTecnicaRegistraz())) {
					 c126.setA_126_13(getTecnicaRegistraz());
				 }
				 if (ValidazioneDati.notEmpty(getSpecCarattRiprod())) {
					 c126.setA_126_14(getSpecCarattRiprod());
				 }
				 if (ValidazioneDati.notEmpty(getDatiCodifRegistrazSonore())) {
					 c126.setB_126_0(getDatiCodifRegistrazSonore());
				 }
				 if (ValidazioneDati.notEmpty(getTipoDiMateriale())) {
					 c126.setB_126_1(getTipoDiMateriale());
				 }
				 if (ValidazioneDati.notEmpty(getTipoDiTaglio() )) {
					 c126.setB_126_2(getTipoDiTaglio());
				 }

				 c127 =  (C127) newInstance(C127.class, c127);
				 if (ValidazioneDati.notEmpty(getDurataRegistraz() )) {
					 c127.setA_127_a(getDurataRegistraz());
				 }
			}


			// Parte della Musica
			if (ValidazioneDati.notEmpty(getTipoElaborazione())) {
				c128 = (C128) newInstance(C128.class, c128);
				c128.setD_128(getTipoElaborazione());
			}
			if (ValidazioneDati.notEmpty(getOrganicoSintetico())) {
				c128 = (C128) newInstance(C128.class, c128);
				c128.setB_128(getOrganicoSintetico());
			}
			if (ValidazioneDati.notEmpty(getOrganicoAnalitico())) {
				c128 = (C128) newInstance(C128.class, c128);
				c128.setC_128(getOrganicoAnalitico());
			}

			if (ValidazioneDati.notEmpty(getGenereRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setA_922(getGenereRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getAnnoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setP_922(getAnnoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getPeriodoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setQ_922(getPeriodoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getSedeRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setR_922(getSedeRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getLuogoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setS_922(getLuogoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getNotaRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setT_922(getNotaRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getOccasioneRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setU_922(getOccasioneRappresentazione());
			}
		}
		if (getTipoMateriale().equals("L")) {
			// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
			// Gestione nuovi campi specifici per etichetta 135
			if (getTipoRecord().equals("l")) {
				c135 = (C135) newInstance(C135.class, c135);
				if (ValidazioneDati.notEmpty(getTipoRisorsaElettronica())) {
					c135.setA_135_0(getTipoRisorsaElettronica());
				}
				if (ValidazioneDati.notEmpty(getIndicazioneSpecificaMateriale())) {
					c135.setA_135_1(getIndicazioneSpecificaMateriale());
				}
				if (ValidazioneDati.notEmpty(getColoreElettronico())) {
					c135.setA_135_2(getColoreElettronico());
				}
				if (ValidazioneDati.notEmpty(getDimensioniElettronico())) {
					c135.setA_135_3(getDimensioniElettronico());
				}
				if (ValidazioneDati.notEmpty(getSuonoElettronico())) {
					c135.setA_135_4(getSuonoElettronico());
				}
			}
		}

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if (getTipoMateriale().equals("M") || getTipoMateriale().equals("E")) {
			if (ValidazioneDati.notEmpty(getGenereRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setA_922(getGenereRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getAnnoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setP_922(getAnnoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getPeriodoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setQ_922(getPeriodoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getSedeRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setR_922(getSedeRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getLuogoRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setS_922(getLuogoRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getNotaRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setT_922(getNotaRappresentazione());
			}
			if (ValidazioneDati.notEmpty(getOccasioneRappresentazione())) {
				c922 = (C922) newInstance(C922.class, c922);
				c922.setU_922(getOccasioneRappresentazione());
			}
		}


		// Gestione documenti di type diverso
		if (getTipoMateriale().equals("M")) {
			if (abilPerTipoMat) {
				// UTENTE ABILITATO

				modernoType = new ModernoType();
				modernoType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				modernoType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					modernoType.setT105bis(c105bis);


				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					modernoType.setT125bis(c125bis);


				if (c181 != null)
					modernoType.setT181(c181);
				if (c182 != null)
					modernoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					modernoType.setT183(c183);

				if (!isModifica()) {
					modernoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					modernoType.setT001(getBID());
					modernoType.setT005(getTimeStamp());
				}

				modernoType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				modernoType.setGuida(guidaDoc);
				if (areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard() != null) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							modernoType.addNumSTD(numStdType[i]);
						}
					}
				}


				if (c856 != null)
					modernoType.setT856(new C856[] { c856 });

				if (c102 != null)
					modernoType.setT102(c102);
				if (c100 != null)
					modernoType.setT100(c100);
				if (c101 != null)
					modernoType.setT101(c101);
				if (c105 != null)
					modernoType.setT105(c105);
				if (c200 != null)
					modernoType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					modernoType.setT321(c321);


				if (c205 != null)
					modernoType.setT205(c205);
				if (c207 != null)
					modernoType.setT207(c207);
				if (c210 != null)
					modernoType.setT210(new C210[] { c210 });
				if (c215 != null)
					modernoType.setT215(c215);

//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					modernoType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					modernoType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

				if (c801 != null)
					modernoType.setT801(c801);


				// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
				// viene esteso anche al Materiale Moderno e Antico
				if (c922 != null)
					modernoType.setT922(c922);
				if (c927 != null)
					modernoType.setT927(c927);

				datiDocType = modernoType;
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					datiDocType.setT125bis(c125bis);

				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);

				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}

				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);
				if (areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard() != null) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							datiDocType.addNumSTD(numStdType[i]);
						}
					}
				}

				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c200 != null)
					datiDocType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					datiDocType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					datiDocType.setT801(c801);
			}

		} else if (getTipoMateriale().equals("E")) {

			if (abilPerTipoMat) {
				// UTENTE ABILITATO

				anticoType = new AnticoType();
				anticoType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				anticoType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				// nel Materiale Antico il tipo testo letterario è registrato sulla 140bis e non sulla 105bis
				if (c140bis != null)
					anticoType.setT140bis(c140bis);
				if (c181 != null)
					anticoType.setT181(c181);
				if (c182 != null)
					anticoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					anticoType.setT183(c183);

				if (!isModifica()) {
					anticoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					anticoType.setT001(getBID());
					anticoType.setT005(getTimeStamp());
				}

				anticoType.setLivelloAutDoc(SbnLivello
						.valueOf(getLivelloAutoritaDocumento()));
				anticoType.setGuida(guidaDoc);

				// Settembre 2016 almaviva2; Intervento interno affinche anche il Materiale Antico possa gestire
				// correttamente i NUMERI STANDARD (prima non erano ammessi)
				if (areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard() != null) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							anticoType.addNumSTD(numStdType[i]);
						}
					}
				}

				if (c856 != null)
					anticoType.setT856(new C856[] { c856 });

				if (c102 != null)
					anticoType.setT102(c102);
				if (c100 != null)
					anticoType.setT100(c100);
				if (c101 != null)
					anticoType.setT101(c101);
				if (c140 != null)
					anticoType.setT140(c140);
				if (c200 != null)
					anticoType.setT200(c200);


				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					anticoType.setT321(c321);

				if (c205 != null)
					anticoType.setT205(c205);
				if (c207 != null)
					anticoType.setT207(c207);
				if (c210 != null)
					anticoType.setT210(new C210[] { c210 });
				if (c215 != null)
					anticoType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					anticoType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					anticoType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

				if (c801 != null)
					anticoType.setT801(c801);
				// IMPRONTA
				if (areaDatiPass.getDetTitoloPFissaVO().getListaImpronte() != null) {
					if (c012 != null)
						anticoType.setT012(c012);
				}

				// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
				// viene esteso anche al Materiale Moderno e Antico
				if (c922 != null)
					anticoType.setT922(c922);
				if (c927 != null)
					anticoType.setT927(c927);

				datiDocType = anticoType;
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					datiDocType.setT125bis(c125bis);

				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);

				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}
				datiDocType.setLivelloAutDoc(SbnLivello
						.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);

				// Settembre 2016 almaviva2; Intervento interno affinche anche il Materiale Antico possa gestire
				// correttamente i NUMERI STANDARD (prima non erano ammessi)
				if (areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard() != null) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							datiDocType.addNumSTD(numStdType[i]);
						}
					}
				}


				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });


				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c200 != null)
					datiDocType.setT200(c200);
				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					datiDocType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

				if (c801 != null)
					datiDocType.setT801(c801);
			}

		} else if (getTipoMateriale().equals("U")) {

			if (abilPerTipoMat) {
				// UTENTE ABILITATO

				musicaType = new MusicaType();
				musicaType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				musicaType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					musicaType.setT105bis(c105bis);
				if (c140bis != null)
					musicaType.setT140bis(c140bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					musicaType.setT125bis(c125bis);

				if (c181 != null)
					musicaType.setT181(c181);
				if (c182 != null)
					musicaType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					musicaType.setT183(c183);

				if (!isModifica()) {
					musicaType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					musicaType.setT001(getBID());
					musicaType.setT005(getTimeStamp());
				}

				if (getLivelloAutorita().equals("")) {
					musicaType.setLivelloAut(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				} else {
					musicaType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
				}

				musicaType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				musicaType.setGuida(guidaDoc);

				// I numeri standard e l'impronta li aggiungo solo se
				// il tipo record è diverso da d
				if (!getTipoRecord().equals("d")) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							musicaType.addNumSTD(numStdType[i]);
						}
					}
					// IMPRONTA
					if (c012 != null)
						musicaType.setT012(c012);
				}

				if (c856 != null)
					musicaType.setT856(new C856[] { c856 });


				if (c102 != null)
					musicaType.setT102(c102);
				if (c100 != null)
					musicaType.setT100(c100);
				if (c101 != null)
					musicaType.setT101(c101);
				if (c200 != null)
					musicaType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					musicaType.setT321(c321);

				if (c205 != null)
					musicaType.setT205(c205);
				if (c207 != null)
					musicaType.setT207(c207);
				if (c210 != null)
					musicaType.setT210(new C210[] { c210 });
				if (c215 != null)
					musicaType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					musicaType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					musicaType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					musicaType.setT801(c801);

				if (c125 != null)
					musicaType.setT125(c125);
				if (c128 != null)
					musicaType.setT128(c128);
				if (c923 != null)
					musicaType.setT923(c923);
				if (c922 != null)
					musicaType.setT922(c922);
				// Personaggi
				if (c927 != null)
					musicaType.setT927(c927);

				if (getIncipit() != null)
					musicaType.setT926(getIncipit());

				datiDocType = musicaType;
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					datiDocType.setT125bis(c125bis);

				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);


				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);
				if (c200 != null)
					datiDocType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				// I numeri standard e l'impronta li aggiungo solo se
				// il tipo record è diverso da d
				if (!getTipoRecord().equals("d")) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							datiDocType.addNumSTD(numStdType[i]);
						}
					}
				}

				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					datiDocType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					datiDocType.setT801(c801);
			}

		} else if (getTipoMateriale().equals("C")) {

			if (abilPerTipoMat) {
				// UTENTE ABILITATO

				cartograficoType = new CartograficoType();
				cartograficoType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				cartograficoType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					cartograficoType.setT105bis(c105bis);
				if (c140bis != null)
					cartograficoType.setT140bis(c140bis);

				if (c181 != null)
					cartograficoType.setT181(c181);
				if (c182 != null)
					cartograficoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					cartograficoType.setT183(c183);

				if (!isModifica()) {
					cartograficoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					cartograficoType.setT001(getBID());
					cartograficoType.setT005(getTimeStamp());
				}

				cartograficoType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				cartograficoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
				cartograficoType.setGuida(guidaDoc);

				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
						.getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null)
							&& (numStdType[i].getTipoSTD() != null)) {
						cartograficoType.addNumSTD(numStdType[i]);
					}
				}

				// Segnalazione Carla del 10/03/2015:
				// inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
				if (c012 != null)
					cartograficoType.setT012(c012);

				if (c856 != null)
					cartograficoType.setT856(new C856[] { c856 });


				if (c102 != null)
					cartograficoType.setT102(c102);
				if (c100 != null)
					cartograficoType.setT100(c100);
				if (c101 != null)
					cartograficoType.setT101(c101);
				if (c200 != null)
					cartograficoType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					cartograficoType.setT321(c321);

				if (c205 != null)
					cartograficoType.setT205(c205);
				if (c207 != null)
					cartograficoType.setT207(c207);
				if (c210 != null)
					cartograficoType.setT210(new C210[] { c210 });
				if (c215 != null)
					cartograficoType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					cartograficoType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					cartograficoType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					cartograficoType.setT801(c801);

				if (c120 != null)
					cartograficoType.setT120(c120);
				if (c121 != null)
					cartograficoType.setT121(c121);
				if (c123 != null)
					cartograficoType.setT123(c123);
				if (c124 != null)
					cartograficoType.setT124(c124);

				datiDocType = cartograficoType;
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);
				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);

				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
						.getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null)
							&& (numStdType[i].getTipoSTD() != null)) {
						datiDocType.addNumSTD(numStdType[i]);
					}
				}

				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c200 != null)
					datiDocType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					datiDocType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					datiDocType.setT801(c801);
			}

		} else if (getTipoMateriale().equals("G")) {

			if (abilPerTipoMat) {
				// UTENTE ABILITATO

				graficoType = new GraficoType();
				graficoType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				graficoType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					graficoType.setT105bis(c105bis);
				if (c140bis != null)
					graficoType.setT140bis(c140bis);
				if (c181 != null)
					graficoType.setT181(c181);
				if (c182 != null)
					graficoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					graficoType.setT183(c183);

				if (!isModifica()) {
					graficoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					graficoType.setT001(getBID());
					graficoType.setT005(getTimeStamp());
				}

				graficoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
				graficoType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				graficoType.setGuida(guidaDoc);

				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
						.getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null)
							&& (numStdType[i].getTipoSTD() != null)) {
						graficoType.addNumSTD(numStdType[i]);
					}
				}

				// Segnalazione Carla del 10/03/2015:
				// inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
				if (c012 != null)
					graficoType.setT012(c012);

				if (c856 != null)
					graficoType.setT856(new C856[] { c856 });

				if (c102 != null)
					graficoType.setT102(c102);
				if (c100 != null)
					graficoType.setT100(c100);
				if (c101 != null)
					graficoType.setT101(c101);
				if (c200 != null)
					graficoType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					graficoType.setT321(c321);

				if (c205 != null)
					graficoType.setT205(c205);
				if (c207 != null)
					graficoType.setT207(c207);
				if (c210 != null)
					graficoType.setT210(new C210[] { c210 });
				if (c215 != null)
					graficoType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					graficoType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					graficoType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					graficoType.setT801(c801);
				if (c116 != null)
					graficoType.setT116(c116);

				datiDocType = graficoType;
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);
				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);

				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
						.getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null)
							&& (numStdType[i].getTipoSTD() != null)) {
						datiDocType.addNumSTD(numStdType[i]);
					}
				}

				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c200 != null)
					datiDocType.setT200(c200);
				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					datiDocType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					datiDocType.setT801(c801);
			}

		} else if (getTipoMateriale().equals("H")) {
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			if (abilPerTipoMat) {
				// UTENTE ABILITATO
				audiovisivoType = new AudiovisivoType();
				audiovisivoType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				audiovisivoType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					audiovisivoType.setT105bis(c105bis);
				if (c140bis != null)
					audiovisivoType.setT140bis(c140bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					audiovisivoType.setT125bis(c125bis);

				if (c181 != null)
					audiovisivoType.setT181(c181);
				if (c182 != null)
					audiovisivoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					audiovisivoType.setT183(c183);

				if (!isModifica()) {
					audiovisivoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					audiovisivoType.setT001(getBID());
					audiovisivoType.setT005(getTimeStamp());
				}

				// MODIFICA 23 SETTEMBRE 2015; se non ci sono le autorizzazioni viene inviato il livAutorità del DOC base
				if (getLivelloAutorita().equals("")) {
					audiovisivoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				} else {
					audiovisivoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
				}
				//audiovisivoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));

				audiovisivoType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				audiovisivoType.setGuida(guidaDoc);

				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null) && (numStdType[i].getTipoSTD() != null)) {
						audiovisivoType.addNumSTD(numStdType[i]);
					}
				}

				if (c856 != null)
					audiovisivoType.setT856(new C856[] { c856 });

				if (c102 != null)
					audiovisivoType.setT102(c102);
				if (c100 != null)
					audiovisivoType.setT100(c100);
				if (c101 != null)
					audiovisivoType.setT101(c101);
				if (c200 != null)
					audiovisivoType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					audiovisivoType.setT321(c321);

				if (c205 != null)
					audiovisivoType.setT205(c205);
				if (c207 != null)
					audiovisivoType.setT207(c207);
				if (c210 != null)
					audiovisivoType.setT210(new C210[] { c210 });
				if (c215 != null)
					audiovisivoType.setT215(c215);
				for (int i=0; i<c3xx.length; i++) {
					audiovisivoType.addT3XX(c3xx[i]);
				}
				if (c801 != null)
					audiovisivoType.setT801(c801);

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					audiovisivoType.setT105bis(c105bis);
				if (c140bis != null)
					audiovisivoType.setT140bis(c140bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					audiovisivoType.setT125bis(c125bis);

				if (c181 != null)
					audiovisivoType.setT181(c181);
				if (c182 != null)
					audiovisivoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					audiovisivoType.setT183(c183);

				// Area dell'audiovisivo e della parte musicale e Personaggi
				if (c115 != null)
					audiovisivoType.setT115(c115);
				if (c128 != null)
					audiovisivoType.setT128(c128);
				if (c922 != null)
					audiovisivoType.setT922(c922);
				if (c927 != null)
					audiovisivoType.setT927(c927);

				if (c126 != null)
					audiovisivoType.setT126(c126);
				if (c127 != null)
					audiovisivoType.setT127(c127);



				datiDocType = audiovisivoType;

			} else if (abilPerTipoMatMusicale) {
				// Intervento settembre 2015: nel caso di materiale audiovisivo non abilitato ma abilitato alla musica
				//si lavora con il musicatype

				// UTENTE ABILITATO SOLO ALLA MUSICA

				musicaType = new MusicaType();
				musicaType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				musicaType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					musicaType.setT105bis(c105bis);
				if (c140bis != null)
					musicaType.setT140bis(c140bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					musicaType.setT125bis(c125bis);

				if (c181 != null)
					musicaType.setT181(c181);
				if (c182 != null)
					musicaType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					musicaType.setT183(c183);


				if (!isModifica()) {
					musicaType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					musicaType.setT001(getBID());
					musicaType.setT005(getTimeStamp());
				}

				if (getLivelloAutorita().equals("")) {
					musicaType.setLivelloAut(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				} else {
					musicaType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
				}

				musicaType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				musicaType.setGuida(guidaDoc);

				// I numeri standard e l'impronta li aggiungo solo se
				// il tipo record è diverso da d
				if (!getTipoRecord().equals("d")) {
					for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
							.getListaNumStandard().size(); i++) {
						if ((numStdType[i].getNumeroSTD() != null)
								&& (numStdType[i].getTipoSTD() != null)) {
							musicaType.addNumSTD(numStdType[i]);
						}
					}
					// IMPRONTA
					if (c012 != null)
						musicaType.setT012(c012);
				}

				if (c856 != null)
					musicaType.setT856(new C856[] { c856 });


				if (c102 != null)
					musicaType.setT102(c102);
				if (c100 != null)
					musicaType.setT100(c100);
				if (c101 != null)
					musicaType.setT101(c101);
				if (c200 != null)
					musicaType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					musicaType.setT321(c321);

				if (c205 != null)
					musicaType.setT205(c205);
				if (c207 != null)
					musicaType.setT207(c207);
				if (c210 != null)
					musicaType.setT210(new C210[] { c210 });
				if (c215 != null)
					musicaType.setT215(c215);
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//				if (c3xx != null)
//					musicaType.addT3XX(c3xx);
				for (int i=0; i<c3xx.length; i++) {
					musicaType.addT3XX(c3xx[i]);
				}
//				GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
				if (c801 != null)
					musicaType.setT801(c801);

				if (c125 != null)
					musicaType.setT125(c125);
				if (c128 != null)
					musicaType.setT128(c128);
				if (c923 != null)
					musicaType.setT923(c923);
				if (c922 != null)
					musicaType.setT922(c922);
				// Personaggi
				if (c927 != null)
					musicaType.setT927(c927);

				if (getIncipit() != null)
					musicaType.setT926(getIncipit());

				datiDocType = musicaType;



				// Fine intervento settembre 2015
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);

				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
				// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
				// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
				if (c125bis != null)
					datiDocType.setT125bis(c125bis);

				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);

				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null)&& (numStdType[i].getTipoSTD() != null)) {
						datiDocType.addNumSTD(numStdType[i]);
					}
				}

				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c200 != null)
					datiDocType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
				if (c801 != null)
					datiDocType.setT801(c801);
			}
		} else if (getTipoMateriale().equals("L")) {
			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			if (abilPerTipoMat) {
				// UTENTE ABILITATO
				elettronicoType = new ElettronicoType();
				elettronicoType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				elettronicoType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));


				// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
				// Gestione nuovi campi specifici per etichetta 135
				if (c135 != null)
					elettronicoType.setT135(c135);

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					elettronicoType.setT105bis(c105bis);
				if (c140bis != null)
					elettronicoType.setT140bis(c140bis);
				if (c181 != null)
					elettronicoType.setT181(c181);
				if (c182 != null)
					elettronicoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					elettronicoType.setT183(c183);

				if (!isModifica()) {
					elettronicoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					elettronicoType.setT001(getBID());
					elettronicoType.setT005(getTimeStamp());
				}

				elettronicoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));
				elettronicoType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				elettronicoType.setGuida(guidaDoc);

				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null) && (numStdType[i].getTipoSTD() != null)) {
						elettronicoType.addNumSTD(numStdType[i]);
					}
				}

				if (c856 != null)
					elettronicoType.setT856(new C856[] { c856 });

				if (c102 != null)
					elettronicoType.setT102(c102);
				if (c100 != null)
					elettronicoType.setT100(c100);
				if (c101 != null)
					elettronicoType.setT101(c101);
				if (c200 != null)
					elettronicoType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					elettronicoType.setT321(c321);

				if (c205 != null)
					elettronicoType.setT205(c205);
				if (c207 != null)
					elettronicoType.setT207(c207);
				if (c210 != null)
					elettronicoType.setT210(new C210[] { c210 });
				if (c215 != null)
					elettronicoType.setT215(c215);
				for (int i=0; i<c3xx.length; i++) {
					elettronicoType.addT3XX(c3xx[i]);
				}
				if (c801 != null)
					elettronicoType.setT801(c801);
			    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					elettronicoType.setT105bis(c105bis);
				if (c140bis != null)
					elettronicoType.setT140bis(c140bis);
				if (c181 != null)
					elettronicoType.setT181(c181);
				if (c182 != null)
					elettronicoType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi


				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					elettronicoType.setT183(c183);

				// Area dell'elettronico ????????????????????
//				if (c115 != null)
//					elettronicoType.setT115(c115);


				datiDocType = elettronicoType;
			} else {
				// UTENTE NON ABILITATO
				datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(getTipoMateriale()));

				// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
				if (c105bis != null)
					datiDocType.setT105bis(c105bis);
				if (c181 != null)
					datiDocType.setT181(c181);
				if (c182 != null)
					datiDocType.setT182(c182);
			    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				if (c183 != null)
					datiDocType.setT183(c183);

				if (!isModifica()) {
					datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
				} else {
					datiDocType.setT001(getBID());
					datiDocType.setT005(getTimeStamp());
				}
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
				datiDocType.setGuida(guidaDoc);
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size(); i++) {
					if ((numStdType[i].getNumeroSTD() != null)&& (numStdType[i].getTipoSTD() != null)) {
						datiDocType.addNumSTD(numStdType[i]);
					}
				}

				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				if (c102 != null)
					datiDocType.setT102(c102);
				if (c100 != null)
					datiDocType.setT100(c100);
				if (c101 != null)
					datiDocType.setT101(c101);
				if (c200 != null)
					datiDocType.setT200(c200);

				// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
				// al link dei documenti su Basi Esterne - Link verso base date digitali
				if (c321 != null)
					datiDocType.setT321(c321);

				if (c205 != null)
					datiDocType.setT205(c205);
				if (c207 != null)
					datiDocType.setT207(c207);
				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c215 != null)
					datiDocType.setT215(c215);
				for (int i=0; i<c3xx.length; i++) {
					datiDocType.addT3XX(c3xx[i]);
				}
				if (c801 != null)
					datiDocType.setT801(c801);
			}
		} else {

			// E' una natura C, quindi senza tipo materiale
			datiDocType = new DatiDocType();
			datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));

			if (!isModifica()) {
				datiDocType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
			} else {
				datiDocType.setT001(getBID());
				datiDocType.setT005(getTimeStamp());
			}

			datiDocType.setLivelloAutDoc(SbnLivello.valueOf(getLivelloAutoritaDocumento()));
			datiDocType.setGuida(guidaDoc);

			for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO()
					.getListaNumStandard().size(); i++) {
				if ((numStdType[i].getNumeroSTD() != null)
						&& (numStdType[i].getTipoSTD() != null)) {
					datiDocType.addNumSTD(numStdType[i]);
				}
			}

			if (c856 != null)
				datiDocType.setT856(new C856[] { c856 });

			if (c102 != null)
				datiDocType.setT102(c102);
			if (c100 != null)
				datiDocType.setT100(c100);
			if (c101 != null)
				datiDocType.setT101(c101);
			if (c200 != null)
				datiDocType.setT200(c200);

			// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
			// al link dei documenti su Basi Esterne - Link verso base date digitali
			if (c321 != null)
				datiDocType.setT321(c321);

			if (c205 != null)
				datiDocType.setT205(c205);
			if (c207 != null)
				datiDocType.setT207(c207);
			if (c210 != null)
				datiDocType.setT210(new C210[] { c210 });
			if (c215 != null)
				datiDocType.setT215(c215);
//			GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
//			if (c3xx != null)
//				datiDocType.addT3XX(c3xx);
			for (int i=0; i<c3xx.length; i++) {
				datiDocType.addT3XX(c3xx[i]);
			}
//			GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
			if (c801 != null)
				datiDocType.setT801(c801);

		}

		// Schema da 1.07 a 1.09
		if (c206 != null)
			datiDocType.setT206(new C206[] { c206 });
		if (c208 != null)
			datiDocType.setT208(c208);

		if (isModifica()) {
			// gestione statorecord confronto la stringa originaria del pannello
			// di modifica
			// la la stringa inviata al server dopo l'eventuale modifica
			// se la stringa è diversa imposto statorecord=C questo abilita il
			// controllo dei simili
			// Se invece sono nel materiale Antico devo prevedere anche il
			// confronto differenziato per la nota
			// quindi devo controllare prima tutta la maschera , e poi la sola
			// area della nota
			// in caso di variazione dolo nota metto stato record=V (abilita la
			// variazione della sola nota
			// anche per gli utenti non abilitati) altrimenti stato record=C.
			// Ovviamnete in caso di mancata modifica
			// non inserisco lo stato

			// CONTROLLO SE SONO IN ANTICO DEVO CONTROLLARE LA VARIAZIONE DELLA
			// NOTA
			String codice = "";
			codice = areaDatiPass.getDetTitoloPFissaVO().getTipoMat();
			if (codice.equals("E")) {
				if (areaDatiPass.isVariazioneNotaAntico())
					documentoType.setStatoRecord(StatoRecord.V);
				if (areaDatiPass.isVariazioneCompAntico())
					documentoType.setStatoRecord(StatoRecord.C);
			} else {
				if (areaDatiPass.isVariazioneTuttiComp())
					documentoType.setStatoRecord(StatoRecord.C);
			}
		}

		documentoTypeChoice.setDatiDocumento(datiDocType);
		documentoType.setDocumentoTypeChoice(documentoTypeChoice);

		SbnRequestType sbnrequest = new SbnRequestType();
		SbnMessageType sbnmessage = new SbnMessageType();

		if (!isModifica()) {
			creaTypeChoice.setDocumento(documentoType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(creaType);
		} else {
			modificaType.setDocumento(documentoType);
			sbnrequest.setModifica(modificaType);
		}


		sbnmessage.setSbnRequest(sbnrequest);
//		sbnMarc.setSbnMessage(sbnmessage);
		return sbnmessage;
	}

	/**
	 * @return
	 */
	public String getAlterazione() {
		return alterazione;
	}

	/**
	 * @param alterazione
	 */
	public void setAlterazione(String alterazione) {
		this.alterazione = alterazione;
	}

	/**
	 * @return
	 */
	public String getAnnoRappresentazione() {
		return annoRappresentazione;
	}

	/**
	 * @param annoRappresentazione
	 */
	public void setAnnoRappresentazione(String annoRappresentazione) {
		this.annoRappresentazione = annoRappresentazione;
	}

	/**
	 * @return
	 */
	public String getChiaveMusicale() {
		return chiaveMusicale;
	}

	/**
	 * @param chiaveMusicale
	 */
	public void setChiaveMusicale(String chiaveMusicale) {
		this.chiaveMusicale = chiaveMusicale;
	}

	/**
	 * @return
	 */
	public String getCodiceFormaMusicale() {
		return codiceFormaMusicale;
	}

	/**
	 * @param codiceFormaMusicale
	 */
	public void setCodiceFormaMusicale(String codiceFormaMusicale) {
		this.codiceFormaMusicale = codiceFormaMusicale;
	}

	/**
	 * @return
	 */
	public String getCodiceTonalita() {
		return codiceTonalita;
	}

	/**
	 * @param codiceTonalita
	 */
	public void setCodiceTonalita(String codiceTonalita) {
		this.codiceTonalita = codiceTonalita;
	}

	/**
	 * @return
	 */
	public String getComposito() {
		return composito;
	}

	/**
	 * @param composito
	 */
	public void setComposito(String composito) {
		this.composito = composito;
	}

	/**
	 * @return
	 */
	public String getConservazione() {
		return conservazione;
	}

	/**
	 * @param conservazione
	 */
	public void setConservazione(String conservazione) {
		this.conservazione = conservazione;
	}

	/**
	 * @return
	 */
	public String getContesto() {
		return contesto;
	}

	/**
	 * @param contesto
	 */
	public void setContesto(String contesto) {
		this.contesto = contesto;
	}

	/**
	 * @return
	 */
	public String getGenereRappresentazione() {
		return genereRappresentazione;
	}

	/**
	 * @param genereRappresentazione
	 */
	public void setGenereRappresentazione(String genereRappresentazione) {
		this.genereRappresentazione = genereRappresentazione;
	}

	/**
	 * @return
	 */
	public String getIdIncipitLetterario() {
		return idIncipitLetterario;
	}

	/**
	 * @param idIncipitLetterario
	 */
	public void setIdIncipitLetterario(String idIncipitLetterario) {
		this.idIncipitLetterario = idIncipitLetterario;
	}

	/**
	 * @return
	 */
	public String getIdInterprete() {
		return idInterprete;
	}

	/**
	 * @param idInterprete
	 */
	public void setIdInterprete(String idInterprete) {
		this.idInterprete = idInterprete;
	}

	/**
	 * @return
	 */
	public String getIllustrazioni() {
		return illustrazioni;
	}

	/**
	 * @param illustrazioni
	 */
	public void setIllustrazioni(String illustrazioni) {
		this.illustrazioni = illustrazioni;
	}

	/**
	 * @return
	 */
	public String getIndicatoreIncipit() {
		return indicatoreIncipit;
	}

	/**
	 * @param indicatoreIncipit
	 */
	public void setIndicatoreIncipit(String indicatoreIncipit) {
		this.indicatoreIncipit = indicatoreIncipit;
	}

	/**
	 * @return
	 */
	public String getLegatura() {
		return legatura;
	}

	/**
	 * @param legatura
	 */
	public void setLegatura(String legatura) {
		this.legatura = legatura;
	}

	/**
	 * @return
	 */
	public String getLuogoRappresentazione() {
		return luogoRappresentazione;
	}

	/**
	 * @param luogoRappresentazione
	 */
	public void setLuogoRappresentazione(String luogoRappresentazione) {
		this.luogoRappresentazione = luogoRappresentazione;
	}

	/**
	 * @return
	 */
	public String getMateria() {
		return materia;
	}

	/**
	 * @param materia
	 */
	public void setMateria(String materia) {
		this.materia = materia;
	}

	/**
	 * @return
	 */
	public String getMisura() {
		return misura;
	}

	/**
	 * @param misura
	 */
	public void setMisura(String misura) {
		this.misura = misura;
	}

	/**
	 * @return
	 */
	public String getNomePersonaggio() {
		return nomePersonaggio;
	}

	/**
	 * @param nomePersonaggio
	 */
	public void setNomePersonaggio(String nomePersonaggio) {
		this.nomePersonaggio = nomePersonaggio;
	}

	/**
	 * @return
	 */
	public String getNotaRappresentazione() {
		return notaRappresentazione;
	}

	/**
	 * @param notaRappresentazione
	 */
	public void setNotaRappresentazione(String notaRappresentazione) {
		this.notaRappresentazione = notaRappresentazione;
	}

	/**
	 * @return
	 */
	public String getNumeroComposizioni() {
		return numeroComposizioni;
	}

	/**
	 * @param numeroComposizioni
	 */
	public void setNumeroComposizioni(String numeroComposizioni) {
		this.numeroComposizioni = numeroComposizioni;
	}

	/**
	 * @return
	 */
	public String getNumeroMovimento() {
		return numeroMovimento;
	}

	/**
	 * @param numeroMovimento
	 */
	public void setNumeroMovimento(String numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}

	/**
	 * @return
	 */
	public String getNumeroProgressivoMovimenti() {
		return numeroProgressivoMovimenti;
	}

	/**
	 * @param numeroProgressivoMovimenti
	 */
	public void setNumeroProgressivoMovimenti(String numeroProgressivoMovimenti) {
		this.numeroProgressivoMovimenti = numeroProgressivoMovimenti;
	}

	/**
	 * @return
	 */
	public String getOccasioneRappresentazione() {
		return occasioneRappresentazione;
	}

	/**
	 * @param occasioneRappresentazione
	 */
	public void setOccasioneRappresentazione(String occasioneRappresentazione) {
		this.occasioneRappresentazione = occasioneRappresentazione;
	}

	/**
	 * @return
	 */
	public String getPalinsesto() {
		return palinsesto;
	}

	/**
	 * @param palinsesto
	 */
	public void setPalinsesto(String palinsesto) {
		this.palinsesto = palinsesto;
	}

	/**
	 * @return
	 */
	public String getPeriodoRappresentazione() {
		return periodoRappresentazione;
	}

	/**
	 * @param periodoRappresentazione
	 */
	public void setPeriodoRappresentazione(String periodoRappresentazione) {
		this.periodoRappresentazione = periodoRappresentazione;
	}

	/**
	 * @return organicoSintetico
	 */
	public String getOrganicoSintetico() {
		return organicoSintetico;
	}

	/**
	 * @param organicoSintetico
	 */
	public void setOrganicoSintetico(String string) {
		organicoSintetico = string;
	}

	/**
	 * @return
	 */
	public String getPersonaggio() {
		return personaggio;
	}

	/**
	 * @param personaggio
	 */
	public void setPersonaggio(String personaggio) {
		this.personaggio = personaggio;
	}

	/**
	 * @return
	 */
	public String getPresentazione() {
		return presentazione;
	}

	/**
	 * @param presentazione
	 */
	public void setPresentazione(String presentazione) {
		this.presentazione = presentazione;
	}

	/**
	 * @return
	 */
	public String getRegistroMusicale() {
		return registroMusicale;
	}

	/**
	 * @param registroMusicale
	 */
	public void setRegistroMusicale(String registroMusicale) {
		this.registroMusicale = registroMusicale;
	}

	/**
	 * @return
	 */
	public String getStesura() {
		// mdf test per verifica spazi
		// if(stesura == null)
		// stesura=IID_STRINGAVUOTA;
		// return stesura.trim();
		return stesura;
	}

	/**
	 * @param stesura
	 */
	public void setStesura(String stesura) {
		this.stesura = stesura;
	}

	/**
	 * @return
	 */
	public String getSedeRappresentazione() {
		return sedeRappresentazione;
	}

	/**
	 * @param teatroRappresentazione
	 */
	public void setSedeRappresentazione(String sedeRappresentazione) {
		this.sedeRappresentazione = sedeRappresentazione;
	}

	/**
	 * @return
	 */
	public String getTempoMusicale() {
		return tempoMusicale;
	}

	/**
	 * @param tempoMusicale
	 */
	public void setTempoMusicale(String tempoMusicale) {
		this.tempoMusicale = tempoMusicale;
	}

	/**
	 * @return
	 */
	public String getTimbroVocale() {
		return timbroVocale;
	}

	/**
	 * @param timbroVocale
	 */
	public void setTimbroVocale(String timbroVocale) {
		this.timbroVocale = timbroVocale;
	}

	/**
	 * @return
	 */
	public String getTipoElaborazione() {
		if (tipoElaborazione != null)
			try {
				tipoElaborazione = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_ELABORAZIONE,
						tipoElaborazione.toUpperCase());
			} catch (Exception e) {
				log.error("", e);
			}
		return tipoElaborazione;
	}

	/**
	 * @param tipoElaborazione
	 */
	public void setTipoElaborazione(String tipoElaborazione) {
		this.tipoElaborazione = tipoElaborazione;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws ValidationException
	 *             DOCUMENT ME!
	 */
	private SbnMessageType addToSBNMarc_TitoloAccesso() throws ValidationException {
		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		ModificaType modificaType = null;

		if (!isModifica()) {
			creaType = new CreaType();
			creaType.setTipoControllo(sbnSimile);
			creaTypeChoice = new CreaTypeChoice();
		} else {
			modificaType = new ModificaType();
			modificaType.setTipoControllo(sbnSimile);
		}

		TitAccessoType titAccessoType = new TitAccessoType();
		DocumentoType documentoType = new DocumentoType();
		TitAccessoTypeChoice titAccessoTypeChoice = new TitAccessoTypeChoice();
		DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

		titAccessoType.setTitAccessoTypeChoice(titAccessoTypeChoice);
		documentoType.setDocumentoTypeChoice(documentoTypeChoice);

		if (isModifica()) {
			String codice = "";
			codice = areaDatiPass.getDetTitoloPFissaVO().getTipoMat();
			if (codice.equals("E")) {
				if (areaDatiPass.isVariazioneNotaAntico())
					documentoType.setStatoRecord(StatoRecord.V);
				if (areaDatiPass.isVariazioneCompAntico())
					documentoType.setStatoRecord(StatoRecord.C);
			} else {
				if (areaDatiPass.isVariazioneTuttiComp())
					documentoType.setStatoRecord(StatoRecord.C);
			}
		}

		titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(getNatura()));

		// datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(getNatura()));
		if (!isModifica()) {
			titAccessoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
		} else {
			titAccessoType.setT001(getBID());
			titAccessoType.setT005(getTimeStamp());
		}

		// Timestamp
		if (isModifica()) {
			titAccessoType.setT005(getTimeStamp());
		}

		// Livello di autorità
		titAccessoType.setLivelloAut(SbnLivello.valueOf(getLivelloAutorita()));

		C423 c423 = new C423();

		// Specializzazione per natura T
		if (getNatura().equals(TIT_NATURA_T)) {
			// Lingue
			C101 c101 = null;
			if (ValidazioneDati.notEmpty(getLingua1())) {
				c101 = (C101) newInstance(C101.class, c101);
				c101.addA_101(getLingua1());
			}
			if (ValidazioneDati.notEmpty(getLingua2())) {
				c101 = (C101) newInstance(C101.class, c101);
				c101.addA_101(getLingua2());
			}

			if (ValidazioneDati.notEmpty(getLingua3())) {
				c101 = (C101) newInstance(C101.class, c101);
				c101.addA_101(getLingua3());
			}

			if (c101 != null)
				c423.setT101(c101);

			// Generi
			C105 c105 = null;

			if (ValidazioneDati.notEmpty(getGenere1())) {
				c105 = (C105) newInstance(C105.class, c105);
				c105.addA_105_4(getGenere1());
			}

			if (ValidazioneDati.notEmpty(getGenere2())) {
				c105 = (C105) newInstance(C105.class, c105);
				c105.addA_105_4(getGenere2());
			}

			if (ValidazioneDati.notEmpty(getGenere3())) {
				c105 = (C105) newInstance(C105.class, c105);
				c105.addA_105_4(getGenere3());
			}

			if (ValidazioneDati.notEmpty(getGenere4())) {
				c105 = (C105) newInstance(C105.class, c105);
				c105.addA_105_4(getGenere4());
			}

			if (c105 != null)
				c423.setT105(c105);
		}

		// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
		// Specializzazione per natura B

		C454A c454A = new C454A();

		if (getNatura().equals(TIT_NATURA_B)) {
			// Lingue
			C101 c101 = null;
			if (ValidazioneDati.notEmpty(getLingua1())) {
				c101 = (C101) newInstance(C101.class, c101);
				c101.addA_101(getLingua1());
			}
			if (ValidazioneDati.notEmpty(getLingua2())) {
				c101 = (C101) newInstance(C101.class, c101);
				c101.addA_101(getLingua2());
			}

			if (ValidazioneDati.notEmpty(getLingua3())) {
				c101 = (C101) newInstance(C101.class, c101);
				c101.addA_101(getLingua3());
			}

			if (c101 != null)
				c454A.setT101(c101);
		}

		// Area del titolo
		if (ValidazioneDati.notEmpty(getAreaTitolo())) {
			C200 c200 = new C200();
			c200.addA_200(getAreaTitolo());
			if (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("W")) {
				c200.setId1(Indicatore.VALUE_1);
			} else {
				c200.setId1(Indicatore.VALUE_2);
			}

			// c200.setId1(Indicatore.VALUE_2);

			c423.setT200(c200);

			if (getNatura().equals(TIT_NATURA_B)) {
				// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
				// titAccessoType.getTitAccessoTypeChoice().setT454(c200);
				c454A.setT200(c200);
				titAccessoType.getTitAccessoTypeChoice().setT454A(c454A);
			}

			if (getNatura().equals(TIT_NATURA_D)) {
				titAccessoType.getTitAccessoTypeChoice().setT517(c200);
			}

			if (getNatura().equals(TIT_NATURA_P)) {
				titAccessoType.getTitAccessoTypeChoice().setT510(c200);
			}

			if (getNatura().equals(TIT_NATURA_T)) {
				titAccessoType.getTitAccessoTypeChoice().setT423(c423);
			}
		}

		documentoType.getDocumentoTypeChoice().setDatiTitAccesso(titAccessoType);

		SbnRequestType sbnrequest = new SbnRequestType();
		SbnMessageType sbnmessage = new SbnMessageType();
		if (!isModifica()) {
			creaTypeChoice.setDocumento(documentoType);
			creaType.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(creaType);
		} else {
			modificaType.setDocumento(documentoType);
			sbnrequest.setModifica(modificaType);
		}

		sbnmessage.setSbnRequest(sbnrequest);
//		sbnMarc.setSbnMessage(sbnmessage);
		return sbnmessage;

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws ValidationException
	 *             DOCUMENT ME!
	 */
	public SbnMessageType addToSBNMarc(boolean abilPerTipoMat, boolean abilPerTipoMatMusicale ,String bidDaAssegnare) throws ValidationException {
		SbnMessageType sbnmessage = new SbnMessageType();
		char carNatura = getNatura().charAt(0);

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		switch (carNatura) {
		case 'A':
		case 'V':
			sbnmessage = addToSBNMarc_TitoloUniforme(bidDaAssegnare, carNatura);
			break;
		case 'C':
		case 'R':
		case 'M':
		case 'S':
		case 'W':
		case 'N':
			sbnmessage = addToSBNMarc_Documento(abilPerTipoMat, abilPerTipoMatMusicale);
			break;
		case 'B':
		case 'D':
		case 'P':
		case 'T':
			sbnmessage = addToSBNMarc_TitoloAccesso();
			break;
		}
		return sbnmessage;
	}

	/**
	 * @return natura
	 */
	public String getNatura() {
		return natura;
	}

	/**
	 * @return tipoMateriale
	 */
	public String getTipoMateriale() {
		return tipoMateriale;
	}

	/**
	 * @param natura
	 */
	private void setNatura(String string) {
		natura = string;
	}

	/**
	 * @param tipoMateriale
	 */
	private void setTipoMateriale(String string) {
		tipoMateriale = string;
	}

	/**
	 * @return appellativo
	 */
	public String getAppellativo() {
		return appellativo;
	}

	/**
	 * @return areaTitolo
	 */
	public String getAreaTitolo() {
		return areaTitolo;
	}

	/**
	 * @return datazione
	 */
	public String getDatazione() {
		if (datazione == null)
			datazione = "";
		return datazione.trim();
	}

	/**
	 * @return String[] formaMusicale
	 */
	public String[] getFormaMusicale() {
		return formaMusicale;
	}

	/**
	 * @return isadn
	 */
	public String getIsadn() {
		return isadn;
	}

	/**
	 * @return lingua1
	 */
	public String getLingua1() {
		return lingua1;
	}

	/**
	 * @return lingua2
	 */
	public String getLingua2() {
		return lingua2;
	}

	/**
	 * @return lingua3
	 */
	public String getLingua3() {
		return lingua3;
	}

	/**
	 * @return livelloAutorita
	 */
	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	/**
	 * @return norme
	 */
	public String getNorme() {
		return norme;
	}

	/**
	 * @return noteCatalogatore
	 */
	public String getNoteCatalogatore() {
		return noteCatalogatore;
	}

	/**
	 * @return noteInformative
	 */
	public String getNoteInformative() {
		return noteInformative;
	}

	/**
	 * @return numeroCatalogo
	 */
	public String getNumeroCatalogo() {
		return numeroCatalogo;
	}

	/**
	 * @return numeroOpera
	 */
	public String getNumeroOpera() {
		return numeroOpera;
	}

	/**
	 * @return numeroOrdine
	 */
	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @return organicoAnalitico
	 */
	public String getOrganicoAnalitico() {
		return organicoAnalitico;
	}

	/**
	 * @return sezioni
	 */
	public String getSezioni() {
		return sezioni;
	}

	/**
	 * @return titoloEstratto
	 */
	public String getTitoloEstratto() {
		return titoloEstratto;
	}

	/**
	 * @return titoloOrdinamento
	 */
	public String getTitoloOrdinamento() {
		return titoloOrdinamento;
	}

	/**
	 * @return tonalita
	 */
	public String getTonalita() {
		return tonalita;
	}

	/**
	 * @param appellativo
	 */
	public void setAppellativo(String string) {
		appellativo = string;
	}

	/**
	 * @param areaTitolo
	 */
	public void setAreaTitolo(String string) {
		areaTitolo = string;
	}

	/**
	 * @param datazione
	 */
	public void setDatazione(String string) {
		datazione = string;
	}

	/**
	 * @param String[]
	 *            formaMusicale
	 */
	public void setFormaMusicale(String[] arrString) {
		formaMusicale = arrString;
	}

	/**
	 * @param isadn
	 */
	public void setIsadn(String string) {
		isadn = string;
	}

	/**
	 * @param lingua1
	 */
	public void setLingua1(String string) {
		lingua1 = string;
	}

	/**
	 * @param lingua2
	 */
	public void setLingua2(String string) {
		lingua2 = string;
	}

	/**
	 * @param lingua3
	 */
	public void setLingua3(String string) {
		lingua3 = string;
	}

	/**
	 * @param livelloAutorita
	 */
	public void setLivelloAutorita(String string) {
		livelloAutorita = string;
	}

	/**
	 * @param norme
	 */
	public void setNorme(String string) {
		norme = string;
	}

	/**
	 * @param noteCatalogatore
	 */
	public void setNoteCatalogatore(String string) {
		noteCatalogatore = string;
	}

	/**
	 * @param noteInformative
	 */
	public void setNoteInformative(String string) {
		noteInformative = string;
	}

	/**
	 * @param numeroCatalogo
	 */
	public void setNumeroCatalogo(String string) {
		numeroCatalogo = string;
	}

	/**
	 * @param numeroOpera
	 */
	public void setNumeroOpera(String string) {
		numeroOpera = string;
	}

	/**
	 * @param numeroOrdine
	 */
	public void setNumeroOrdine(String string) {
		numeroOrdine = string;
	}

	/**
	 * @param organicoAnalitico
	 */
	public void setOrganicoAnalitico(String string) {
		organicoAnalitico = string;
	}

	/**
	 * @param sezioni
	 */
	public void setSezioni(String string) {
		sezioni = string;
	}

	/**
	 * @param titoloEstratto
	 */
	public void setTitoloEstratto(String string) {
		titoloEstratto = string;
	}

	/**
	 * @param titoloOrdinamento
	 */
	public void setTitoloOrdinamento(String string) {
		titoloOrdinamento = string;
	}

	/**
	 * @param tonalita
	 */
	public void setTonalita(String string) {
		tonalita = string;
	}

	/**
	 * @param String
	 *            codice, String descrizione
	 */
	public void addRepertorio(String esito, String sigla, String nota) {
		this.repertorio.add(new String[] { esito, sigla, nota });
	}

	/**
	 * @param String
	 *            indice, String descrizione
	 */
	public void addGenereMateriale(int indice, String descrizione) {
		this.genereMateriale.add(indice, descrizione);
	}

	// CAMPO Personaggio nome
	public void vectorSetPersonaggi(int index, String string) {
		personaggi.add(string);
	}

	public String vectorGetPersonaggi(int index) {
		return personaggi.get(index).toString();

	}

	// CAMPO Timbro vocale
	public void vectorSetTimbroVocale(int index, String string) {
		timbriVocale.add(string);
	}

	public String vectorGetTimbroVocale(int index) {
		return timbriVocale.get(index).toString();
	}

	// CAMPO Timbro vocale
	public void vectorSetInterprete(int index, String string) {
		interprete.add(string);
	}

	public String vectorGetInterprete(int index) {
		return interprete.get(index).toString();
	}

	// CAMPO IMPRONTA 1
	public void vectorSetImpronta1(int index, String string) {
		impronta1.add(string);
	}

	public String vectorGetImpronta1(int index) {
		return impronta1.get(index).toString();
	}

	// CAMPO IMPRONTA 2
	public void vectorSetImpronta2(int index, String string) {
		impronta2.add(string);
	}

	public String vectorGetImpronta2(int index) {
		return impronta2.get(index).toString();
	}

	// CAMPO IMPRONTA 3
	public void vectorSetImpronta3(int index, String string) {
		impronta3.add(string);
	}

	public String vectorGetImpronta3(int index) {
		return impronta3.get(index).toString();
	}

	// CAMPO NOTA IMPRONTA
	public void vectorSetNotaImpronta(int index, String string) {
		notaImpronta.add(string);
	}

	public String vectorGetNotaImpronta(int index) {
		return notaImpronta.get(index).toString();
	}

	public void vectorSetNumeroSTD(int index, String string) {
		numeroStandart.add(string);
	}

	public String vectorGetNumeroSTD(int index) {
		return numeroStandart.get(index).toString();
	}

	public void vectorSetNotaSTD(int index, String string) {
		notaStandart.add(string);
	}

	public String vectorGetNotaSTD(int index) {
		return notaStandart.get(index).toString();
	}

	public void vectorSetTipoSTD(int index, String string) {
		numeroTipoStandart.add(string);
	}

	public String vectorGetTipoSTD(int index) {
		return numeroTipoStandart.get(index).toString();
	}

	public void vectorSetPaeseSTD(int index, String string) {
		paeseTipoStandart.add(string);
	}

	public String vectorGetPaeseSTD(int index) {
		return paeseTipoStandart.get(index).toString();
	}

	/**
	 * @return List repertorio
	 */
	public List getRepertorio() {
		return repertorio;
	}

	/**
	 * @return List genereMateriale
	 */
	public List getGenereMateriale() {
		return genereMateriale;
	}

	/**
	 * @return modifica
	 */
	public boolean isModifica() {
		return modifica;
	}

	/**
	 * @param modifica
	 */
	public void setModifica(boolean b) {
		modifica = b;
	}

	/**
	 * @return
	 */
	public String getBID() {
		return BID;
	}

	/**
	 * @param string
	 */
	public void setBID(String string) {
		BID = string;
	}

	/**
	 * @return notaSTD
	 */
	public String getNotaSTD() {
		return notaSTD;
	}

	/**
	 * @return numeroSTD
	 */
	public String getNumeroSTD() {
		return numeroSTD;
	}

	/**
	 * @return paeseSTD
	 */
	public String getPaeseSTD() {
		return paeseSTD;
	}

	/**
	 * @return tipoSTD
	 */
	public String getTipoSTD() {
		return tipoSTD;
	}

	/**
	 * @param string
	 */
	public void setNotaSTD(String string) {
		notaSTD = string;
	}

	/**
	 * @param string
	 */

	public void setNumeroSTD(String string) {
		numeroSTD = string;
	}

	/**
	 * @param string
	 */
	public void setPaeseSTD(String string) {
		paeseSTD = string;
	}

	/**
	 * @param string
	 */
	public void setTipoSTD(String string) {
		tipoSTD = string;
	}

	/**
	 * @return areaNote
	 */
	public String getAreaNote() {
		return areaNote;
	}

	/**
	 * @return data1
	 */
	public String getData1() {
		return data1;
	}

	/**
	 * @return data2
	 */
	public String getData2() {
		return data2;
	}

	/**
	 * @return tipoData
	 */
	public String getTipoData() {
		return tipoData;
	}

	/**
	 * @param string
	 */
	public void setAreaNote(String string) {
		areaNote = string;
	}

	/**
	 * @param string
	 */
	public void setData1(String string) {
		data1 = string;
	}

	/**
	 * @param string
	 */
	public void setData2(String string) {
		data2 = string;
	}

	/**
	 * @param string
	 */
	public void setTipoData(String string) {
		tipoData = string;
	}

	// /**
	// * @param string
	// */
	// public void setTipoRespons(String string) {
	// tipoRespons = string;
	// }
	//
	/**
	 * @return genere1
	 */
	public String getGenere1() {
		return genere1;
	}

	/**
	 * @return genere2
	 */
	public String getGenere2() {
		return genere2;
	}

	/**
	 * @return genere3
	 */
	public String getGenere3() {
		return genere3;
	}

	/**
	 * @return genere4
	 */
	public String getGenere4() {
		return genere4;
	}

	/**
	 * @param string
	 */
	public void setGenere1(String string) {
		genere1 = string;
	}

	/**
	 * @param string
	 */
	public void setGenere2(String string) {
		genere2 = string;
	}

	/**
	 * @param string
	 */
	public void setGenere3(String string) {
		genere3 = string;
	}

	/**
	 * @param string
	 */
	public void setGenere4(String string) {
		genere4 = string;
	}

	/**
	 * @return
	 */
	public String getTipoRecord() {
		return tipoRecord;
	}

	/**
	 * @param string
	 */
	public void setTipoRecord(String string) {
		tipoRecord = string;
	}

	/**
	 * @return
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * @param string
	 */
	public void setPaese(String string) {
		paese = string;
	}

	/**
	 * @return
	 */
	public String getAreaDatiMatematici() {
		return areaDatiMatematici;
	}

	/**
	 * @return
	 */
	public String getAreaDescrizioneFisica() {
		return areaDescrizioneFisica;
	}

	/**
	 * @return
	 */
	public String getAreaEdizione() {
		return areaEdizione;
	}

	/**
	 * @return
	 */
	public String getAreaMusica() {
		return areaMusica;
	}

	/**
	 * @return
	 */
	public String getAreaNumerazione() {
		return areaNumerazione;
	}

	/**
	 * @param string
	 */
	public void setAreaDatiMatematici(String string) {
		areaDatiMatematici = string;
	}

	/**
	 * @param string
	 */
	public void setAreaDescrizioneFisica(String string) {
		areaDescrizioneFisica = string;
	}

	/**
	 * @param string
	 */
	public void setAreaEdizione(String string) {
		areaEdizione = string;
	}

	/**
	 * @param string
	 */
	public void setAreaMusica(String string) {
		areaMusica = string;
	}

	/**
	 * @param string
	 */
	public void setAreaNumerazione(String string) {
		areaNumerazione = string;
	}

	/**
	 * @return
	 */
	public String getAreaPubblicazione() {
		return areaPubblicazione;
	}

	/**
	 * @param string
	 */
	public void setAreaPubblicazione(String string) {
		areaPubblicazione = string;
	}

	/**
	 * @return timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param string
	 */
	public void setTimeStamp(String string) {
		timeStamp = string;
	}

	/**
	 * Rappresenta il nodo autore legato da un titolo. All'atto della modifica
	 * di un titolo, nel caso di modifica dei dati di un autore ad esso legato,
	 * gestisce la cancellazione prima e l'inserimento poi, dello stesso autore
	 * legato al titolo in questione.
	 *
	 * @author Giuseppe Casafina
	 */
	class LegameAutoreAggiuntivo {

		boolean daAggiungere = false;

		String idArrivo;

		SbnAuthority tipoAuthority;

		String tipoLegame;

		String noteLegame;

		boolean superfluo;

		boolean incerto;

		String relatorCode;

		String tipoRespons; // aggiunto il 04/10/2005 (bug 862)

		/**
		 * Crea un oggetto LegameAutoreAggiuntivo.
		 */
		public LegameAutoreAggiuntivo() {
			this.daAggiungere = false;
		}

		public void setLegameAutoreAggiuntivo(String idArrivo,
				SbnAuthority tipoAuthority, String tipoLegame,
				String noteLegame, boolean superfluo, boolean incerto,
				String relatorCode, String tipoRespons) {

			this.daAggiungere = true;

			this.idArrivo = idArrivo;
			this.tipoAuthority = tipoAuthority;
			this.tipoLegame = tipoLegame;
			this.noteLegame = noteLegame;
			this.superfluo = superfluo;
			this.incerto = incerto;
			this.relatorCode = relatorCode;
			this.tipoRespons = tipoRespons;
		}

		/**
		 * True se c'è un legame aggiuntivo al titolo da variare. False se non è
		 * presente alcun legame aggiuntivo.
		 *
		 * @return stato del legame aggiuntivo
		 */
		public boolean isDaAggiungere() {
			return this.daAggiungere;
		}

		/**
		 * @return
		 */
		public String getIdArrivo() {
			return idArrivo;
		}

		/**
		 * @return
		 */
		public boolean isIncerto() {
			return incerto;
		}

		/**
		 * @return
		 */
		public String getNoteLegame() {
			return noteLegame;
		}

		/**
		 * @return
		 */
		public String getRelatorCode() {
			return relatorCode;
		}

		/**
		 * @return
		 */
		public boolean isSuperfluo() {
			return superfluo;
		}

		/**
		 * @return
		 */
		public SbnAuthority getTipoAuthority() {
			return tipoAuthority;
		}

		/**
		 * @return
		 */
		public String getTipoLegame() {
			return tipoLegame;
		}

		// ///////// aggiunto il 04/10/2005 (bug 862)
		/**
		 * @return
		 */
		public String getTipoRespons() {
			return tipoRespons;
		}

		/**
		 * @param string
		 */
		public void setTipoRespons(String string) {
			tipoRespons = string;
		}

		// ///////

		/**
		 * @param string
		 */
		public void setIdArrivo(String string) {
			idArrivo = string;
		}

		/**
		 * @param indicatore
		 */
		public void setIncerto(boolean indicatore) {
			incerto = indicatore;
		}

		/**
		 * @param string
		 */
		public void setNoteLegame(String string) {
			noteLegame = string;
		}

		/**
		 * @param string
		 */
		public void setRelatorCode(String string) {
			relatorCode = string;
		}

		/**
		 * @param indicatore
		 */
		public void setSuperfluo(boolean indicatore) {
			superfluo = indicatore;
		}

		/**
		 * @param authority
		 */
		public void setTipoAuthority(SbnAuthority authority) {
			tipoAuthority = authority;
		}

		/**
		 * @param string
		 */
		public void setTipoLegame(String string) {
			tipoLegame = string;
		}

	}// end class LegameAutoreAggiuntivo

	/**
	 * Imposta tutti i legami del titolo che deve essere modificato, gestisce
	 * anche la creazione del legame con l'autore modificato, consentendone la
	 * cancellazione prima e l'inserimento poi.
	 *
	 * @param legAutoreAggiuntivo
	 *            oggetto LegameAutoreAggiuntivo con i dati del legame ad Autore
	 *            da inserire.
	 */
	public void setArrayLegamiAutoreAggiuntivo(
			LegameAutoreAggiuntivo legAutoreAggiuntivo) {

		LegamiType legamiType = new LegamiType();
		legamiType.setIdPartenza(getBID());
		legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

		LegameElementoAutType legameElementoAut = new LegameElementoAutType();
		legameElementoAut.setNoteLegame(legAutoreAggiuntivo.getNoteLegame());

		if (legAutoreAggiuntivo.isSuperfluo()) {
			legameElementoAut.setSuperfluo(SbnIndicatore.S);
		} else {
			legameElementoAut.setSuperfluo(SbnIndicatore.N);
		}

		legameElementoAut.setTipoAuthority(legAutoreAggiuntivo
				.getTipoAuthority());

		String tipoLegame = legAutoreAggiuntivo.getTipoLegame();
		legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegame));

		if (ValidazioneDati.notEmpty((legAutoreAggiuntivo.getRelatorCode()))) {
			legameElementoAut.setRelatorCode(legAutoreAggiuntivo.getRelatorCode());
		}

		if (legAutoreAggiuntivo.isIncerto()) {
			legameElementoAut.setIncerto(SbnIndicatore.S);
		} else {
			legameElementoAut.setIncerto(SbnIndicatore.N);
		}

		legameElementoAut.setIdArrivo(legAutoreAggiuntivo.getIdArrivo());

		legameElementoAut.setTipoRespons(SbnRespons.valueOf(legAutoreAggiuntivo
				.getTipoRespons()));
		// ////////

		// un solo arrivo legame per legame
		ArrivoLegame arrivoLegame = new ArrivoLegame();

		arrivoLegame.setLegameElementoAut(legameElementoAut);

		ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
		arrayArrivoLegame[0] = arrivoLegame;

		// aggiungo arrivo legame
		legamiType.setArrivoLegame(arrayArrivoLegame);

		// ////////////////////////////////////////
		// Aggiunge il nuovo legame all'array dei LegamiType
		// Calcola la lunghezza dell'array e aggiunge il
		// nuovo legame in coda
		int sizeOldArrayLegami = arrayNewLegamiAutore.length;

		LegamiType[] appoArrayNewLegamiAutore = arrayNewLegamiAutore;

		LegamiType[] appoLegamiType = new LegamiType[sizeOldArrayLegami + 1];
		for (int pos = 0; pos < sizeOldArrayLegami; pos++) {
			appoLegamiType[pos] = appoArrayNewLegamiAutore[pos];
		}
		appoArrayNewLegamiAutore = new LegamiType[sizeOldArrayLegami + 1];

		for (int pos = 0; pos < sizeOldArrayLegami; pos++) {
			appoArrayNewLegamiAutore[pos] = appoLegamiType[pos];
		}
		appoArrayNewLegamiAutore[sizeOldArrayLegami] = legamiType;

		// Imposta l'array globale di legami
		arrayNewLegamiAutore = new LegamiType[sizeOldArrayLegami + 1];
		arrayNewLegamiAutore = appoArrayNewLegamiAutore;
		// //////////////////////////////

	}// end setArrayLegamiAutoreAggiuntivo

	public String getLivelloAutoritaDocumento() {
		return livelloAutoritaDocumento;
	}

	public void setLivelloAutoritaDocumento(String livelloAutoritaDocumento) {
		this.livelloAutoritaDocumento = livelloAutoritaDocumento;
	}

	public String getUriDigitalBorn() {
		return uriDigitalBorn;
	}

	public void setUriDigitalBorn(String uriDigitalBorn) {
		this.uriDigitalBorn = uriDigitalBorn;
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

	public void setAreaNote323(String areaNote323) {
		this.areaNote323 = areaNote323;
	}

	public void setAreaNote327(String areaNote327) {
		this.areaNote327 = areaNote327;
	}

	public void setAreaNote330(String areaNote330) {
		this.areaNote330 = areaNote330;
	}

	public void setAreaNote336(String areaNote336) {
		this.areaNote336 = areaNote336;
	}

	public void setAreaNote337(String areaNote337) {
		this.areaNote337 = areaNote337;
	}

	public void setAreaNoteDATA(String areaNoteDATA) {
		this.areaNoteDATA = areaNoteDATA;
	}

	public void setAreaNoteFILI(String areaNoteFILI) {
		this.areaNoteFILI = areaNoteFILI;
	}

	public void setAreaNoteORIG(String areaNoteORIG) {
		this.areaNoteORIG = areaNoteORIG;
	}

	public void setAreaNotePOSS(String areaNotePOSS) {
		this.areaNotePOSS = areaNotePOSS;
	}

	public void setAreaNotePROV(String areaNotePROV) {
		this.areaNotePROV = areaNotePROV;
	}

	public String getTipoTestoLetterario() {
		return tipoTestoLetterario;
	}

	public void setTipoTestoLetterario(String tipoTestoLetterario) {
		this.tipoTestoLetterario = tipoTestoLetterario;
	}

	public String getFormaContenuto() {
		return formaContenuto;
	}

	public void setFormaContenuto(String formaContenuto) {
		this.formaContenuto = formaContenuto;
	}

	public String getTipoContenuto() {
		return tipoContenuto;
	}

	public void setTipoContenuto(String tipoContenuto) {
		this.tipoContenuto = tipoContenuto;
	}

	public String getMovimento() {
		return movimento;
	}

	public void setMovimento(String movimento) {
		this.movimento = movimento;
	}

	public String getDimensione() {
		return dimensione;
	}

	public void setDimensione(String dimensione) {
		this.dimensione = dimensione;
	}

	public String getSensorialita1() {
		return sensorialita1;
	}

	public void setSensorialita1(String sensorialita1) {
		this.sensorialita1 = sensorialita1;
	}

	public String getSensorialita2() {
		return sensorialita2;
	}

	public void setSensorialita2(String sensorialita2) {
		this.sensorialita2 = sensorialita2;
	}

	public String getSensorialita3() {
		return sensorialita3;
	}

	public void setSensorialita3(String sensorialita3) {
		this.sensorialita3 = sensorialita3;
	}

	public String getTipoMediazione() {
		return tipoMediazione;
	}

	public void setTipoMediazione(String tipoMediazione) {
		this.tipoMediazione = tipoMediazione;
	}

	public String getFormaContenutoBIS() {
		return formaContenutoBIS;
	}

	public void setFormaContenutoBIS(String formaContenutoBIS) {
		this.formaContenutoBIS = formaContenutoBIS;
	}

	public String getTipoContenutoBIS() {
		return tipoContenutoBIS;
	}

	public void setTipoContenutoBIS(String tipoContenutoBIS) {
		this.tipoContenutoBIS = tipoContenutoBIS;
	}

	public String getMovimentoBIS() {
		return movimentoBIS;
	}

	public void setMovimentoBIS(String movimentoBIS) {
		this.movimentoBIS = movimentoBIS;
	}

	public String getDimensioneBIS() {
		return dimensioneBIS;
	}

	public void setDimensioneBIS(String dimensioneBIS) {
		this.dimensioneBIS = dimensioneBIS;
	}

	public String getSensorialitaBIS1() {
		return sensorialitaBIS1;
	}

	public void setSensorialitaBIS1(String sensorialitaBIS1) {
		this.sensorialitaBIS1 = sensorialitaBIS1;
	}

	public String getSensorialitaBIS2() {
		return sensorialitaBIS2;
	}

	public void setSensorialitaBIS2(String sensorialitaBIS2) {
		this.sensorialitaBIS2 = sensorialitaBIS2;
	}

	public String getSensorialitaBIS3() {
		return sensorialitaBIS3;
	}

	public void setSensorialitaBIS3(String sensorialitaBIS3) {
		this.sensorialitaBIS3 = sensorialitaBIS3;
	}

	public String getTipoMediazioneBIS() {
		return tipoMediazioneBIS;
	}

	public void setTipoMediazioneBIS(String tipoMediazioneBIS) {
		this.tipoMediazioneBIS = tipoMediazioneBIS;
	}

	public String getLivelloAutAudiov() {
		return livelloAutAudiov;
	}

	public void setLivelloAutAudiov(String livelloAutAudiov) {
		this.livelloAutAudiov = livelloAutAudiov;
	}

	public String getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(String lunghezza) {
		this.lunghezza = lunghezza;
	}

	public String getIndicatoreColore() {
		return indicatoreColore;
	}

	public void setIndicatoreColore(String indicatoreColore) {
		this.indicatoreColore = indicatoreColore;
	}

	public String getIndicatoreSuono() {
		return indicatoreSuono;
	}

	public void setIndicatoreSuono(String indicatoreSuono) {
		this.indicatoreSuono = indicatoreSuono;
	}

	public String getLarghezzaDimensioni() {
		return larghezzaDimensioni;
	}

	public void setLarghezzaDimensioni(String larghezzaDimensioni) {
		this.larghezzaDimensioni = larghezzaDimensioni;
	}

	public String getFormaPubblDistr() {
		return formaPubblDistr;
	}

	public void setFormaPubblDistr(String formaPubblDistr) {
		this.formaPubblDistr = formaPubblDistr;
	}

	public String getMaterAccompagn1() {
		return materAccompagn1;
	}

	public void setMaterAccompagn1(String materAccompagn1) {
		this.materAccompagn1 = materAccompagn1;
	}

	public String getMaterAccompagn2() {
		return materAccompagn2;
	}

	public void setMaterAccompagn2(String materAccompagn2) {
		this.materAccompagn2 = materAccompagn2;
	}

	public String getMaterAccompagn3() {
		return materAccompagn3;
	}

	public void setMaterAccompagn3(String materAccompagn3) {
		this.materAccompagn3 = materAccompagn3;
	}

	public String getMaterAccompagn4() {
		return materAccompagn4;
	}

	public void setMaterAccompagn4(String materAccompagn4) {
		this.materAccompagn4 = materAccompagn4;
	}

	public String getMaterialeEmulsBase() {
		return materialeEmulsBase;
	}

	public void setMaterialeEmulsBase(String materialeEmulsBase) {
		this.materialeEmulsBase = materialeEmulsBase;
	}

	public String getMaterialeSupportSec() {
		return materialeSupportSec;
	}

	public void setMaterialeSupportSec(String materialeSupportSec) {
		this.materialeSupportSec = materialeSupportSec;
	}

	public String getElementiProd() {
		return elementiProd;
	}

	public void setElementiProd(String elementiProd) {
		this.elementiProd = elementiProd;
	}

	public String getEmulsionePellic() {
		return emulsionePellic;
	}

	public void setEmulsionePellic(String emulsionePellic) {
		this.emulsionePellic = emulsionePellic;
	}

	public String getComposPellic() {
		return composPellic;
	}

	public void setComposPellic(String composPellic) {
		this.composPellic = composPellic;
	}

	public String getLivelloAutElettr() {
		return livelloAutElettr;
	}

	public void setLivelloAutElettr(String livelloAutElettr) {
		this.livelloAutElettr = livelloAutElettr;
	}

	public String getTipoVideo() {
		return tipoVideo;
	}

	public void setTipoVideo(String tipoVideo) {
		this.tipoVideo = tipoVideo;
	}

	public String getSupportoSuono() {
		return supportoSuono;
	}

	public void setSupportoSuono(String supportoSuono) {
		this.supportoSuono = supportoSuono;
	}

	public String getTecnicaVideoFilm() {
		return tecnicaVideoFilm;
	}

	public void setTecnicaVideoFilm(String tecnicaVideoFilm) {
		this.tecnicaVideoFilm = tecnicaVideoFilm;
	}

	public String getStandardTrasmiss() {
		return standardTrasmiss;
	}

	public void setStandardTrasmiss(String standardTrasmiss) {
		this.standardTrasmiss = standardTrasmiss;
	}

	public String getVersioneAudiovid() {
		return versioneAudiovid;
	}

	public void setVersioneAudiovid(String versioneAudiovid) {
		this.versioneAudiovid = versioneAudiovid;
	}

	public String getSpecCatColoreFilm() {
		return specCatColoreFilm;
	}

	public void setSpecCatColoreFilm(String specCatColoreFilm) {
		this.specCatColoreFilm = specCatColoreFilm;
	}

	public String getSuonoImmagMovimento() {
		return suonoImmagMovimento;
	}

	public void setSuonoImmagMovimento(String suonoImmagMovimento) {
		this.suonoImmagMovimento = suonoImmagMovimento;
	}

	public String getTipoPellicStampa() {
		return tipoPellicStampa;
	}

	public void setTipoPellicStampa(String tipoPellicStampa) {
		this.tipoPellicStampa = tipoPellicStampa;
	}

	public String getPresentImmagMov() {
		return presentImmagMov;
	}

	public void setPresentImmagMov(String presentImmagMov) {
		this.presentImmagMov = presentImmagMov;
	}

	public String getPubblicVideoreg() {
		return pubblicVideoreg;
	}

	public void setPubblicVideoreg(String pubblicVideoreg) {
		this.pubblicVideoreg = pubblicVideoreg;
	}

	public String getPresentVideoreg() {
		return presentVideoreg;
	}

	public void setPresentVideoreg(String presentVideoreg) {
		this.presentVideoreg = presentVideoreg;
	}

	public String getVelocita() {
		return velocita;
	}

	public void setVelocita(String velocita) {
		this.velocita = velocita;
	}

	public String getTipoSuono() {
		return tipoSuono;
	}

	public void setTipoSuono(String tipoSuono) {
		this.tipoSuono = tipoSuono;
	}

	public String getMaterTestAccompagn1() {
		return materTestAccompagn1;
	}

	public void setMaterTestAccompagn1(String materTestAccompagn1) {
		this.materTestAccompagn1 = materTestAccompagn1;
	}

	public String getMaterTestAccompagn2() {
		return materTestAccompagn2;
	}

	public void setMaterTestAccompagn2(String materTestAccompagn2) {
		this.materTestAccompagn2 = materTestAccompagn2;
	}

	public String getMaterTestAccompagn3() {
		return materTestAccompagn3;
	}

	public void setMaterTestAccompagn3(String materTestAccompagn3) {
		this.materTestAccompagn3 = materTestAccompagn3;
	}

	public String getMaterTestAccompagn4() {
		return materTestAccompagn4;
	}

	public void setMaterTestAccompagn4(String materTestAccompagn4) {
		this.materTestAccompagn4 = materTestAccompagn4;
	}

	public String getMaterTestAccompagn5() {
		return materTestAccompagn5;
	}

	public void setMaterTestAccompagn5(String materTestAccompagn5) {
		this.materTestAccompagn5 = materTestAccompagn5;
	}

	public String getMaterTestAccompagn6() {
		return materTestAccompagn6;
	}

	public void setMaterTestAccompagn6(String materTestAccompagn6) {
		this.materTestAccompagn6 = materTestAccompagn6;
	}

	public String getTecnicaRegistraz() {
		return tecnicaRegistraz;
	}

	public void setTecnicaRegistraz(String tecnicaRegistraz) {
		this.tecnicaRegistraz = tecnicaRegistraz;
	}

	public String getSpecCarattRiprod() {
		return specCarattRiprod;
	}

	public void setSpecCarattRiprod(String specCarattRiprod) {
		this.specCarattRiprod = specCarattRiprod;
	}

	public String getTipoDiMateriale() {
		return tipoDiMateriale;
	}

	public void setTipoDiMateriale(String tipoDiMateriale) {
		this.tipoDiMateriale = tipoDiMateriale;
	}

	public String getTipoDiTaglio() {
		return tipoDiTaglio;
	}

	public void setTipoDiTaglio(String tipoDiTaglio) {
		this.tipoDiTaglio = tipoDiTaglio;
	}

	public String getFormaPubblicazioneDisco() {
		return formaPubblicazioneDisco;
	}

	public void setFormaPubblicazioneDisco(String formaPubblicazioneDisco) {
		this.formaPubblicazioneDisco = formaPubblicazioneDisco;
	}

	public String getLarghezzaScanal() {
		return larghezzaScanal;
	}

	public void setLarghezzaScanal(String larghezzaScanal) {
		this.larghezzaScanal = larghezzaScanal;
	}

	public String getLarghezzaNastro() {
		return larghezzaNastro;
	}

	public void setLarghezzaNastro(String larghezzaNastro) {
		this.larghezzaNastro = larghezzaNastro;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}

	public String getConfigurazNastro() {
		return configurazNastro;
	}

	public void setConfigurazNastro(String configurazNastro) {
		this.configurazNastro = configurazNastro;
	}

	public String getDatiCodifRegistrazSonore() {
		return datiCodifRegistrazSonore;
	}

	public void setDatiCodifRegistrazSonore(String datiCodifRegistrazSonore) {
		this.datiCodifRegistrazSonore = datiCodifRegistrazSonore;
	}

	public String getDurataRegistraz() {
		return durataRegistraz;
	}

	public void setDurataRegistraz(String durataRegistraz) {
		this.durataRegistraz = durataRegistraz;
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


	List<String> LinkEsternoDb = new ArrayList<String>();
	List<String> LinkEsternoId = new ArrayList<String>();
	List<String> LinkEsternoURL = new ArrayList<String>();
	List<String> LinkEsternoNota = new ArrayList<String>();

	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo
	List<String> ReperCartaceoAutTit = new ArrayList<String>();
	List<String> ReperCartaceoData = new ArrayList<String>();
	List<String> ReperCartaceoPos = new ArrayList<String>();
	List<String> ReperCartaceoNota = new ArrayList<String>();



	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// al link dei documenti su Basi Esterne - Link verso base date digitali
	public void vectorSetLinkEsternoDb(int index, String string) {
		linkEsternoDb.add(string);
	}
	public String vectorGetLinkEsternoDb(int index) {
		return linkEsternoDb.get(index).toString();
	}
	public void vectorSetLinkEsternoId(int index, String string) {
		linkEsternoId.add(string);
	}
	public String vectorGetLinkEsternoId(int index) {
		return linkEsternoId.get(index).toString();
	}
	public void vectorSetLinkEsternoURL(int index, String string) {
		linkEsternoURL.add(string);
	}
	public String vectorGetLinkEsternoURL(int index) {
		return linkEsternoURL.get(index).toString();
	}
	public void vectorSetLinkEsternoNota(int index, String string) {
		linkEsternoNota.add(string);
	}
	public String vectorGetLinkEsternoNota(int index) {
		return linkEsternoNota.get(index).toString();
	}

	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo
	public void vectorSetReperCartaceoAutTit(int index, String string) {
		ReperCartaceoAutTit.add(string);
	}
	public String vectorGetReperCartaceoAutTit(int index) {
		return ReperCartaceoAutTit.get(index).toString();
	}
	public void vectorSetReperCartaceoData(int index, String string) {
		ReperCartaceoData.add(string);
	}
	public String vectorGetReperCartaceoData(int index) {
		return ReperCartaceoData.get(index).toString();
	}
	public void vectorSetReperCartaceoPos(int index, String string) {
		ReperCartaceoPos.add(string);
	}
	public String vectorGetReperCartaceoPos(int index) {
		return ReperCartaceoPos.get(index).toString();
	}
	public void vectorSetReperCartaceoNota(int index, String string) {
		ReperCartaceoNota.add(string);
	}
	public String vectorGetReperCartaceoNota(int index) {
		return ReperCartaceoNota.get(index).toString();
	}

	public String getFormaOpera231() {
		return formaOpera231;
	}

	public void setFormaOpera231(String formaOpera231) {
		this.formaOpera231 = formaOpera231;
	}

	public String getDataOpera231() {
		return dataOpera231;
	}

	public void setDataOpera231(String dataOpera231) {
		this.dataOpera231 = dataOpera231;
	}

	public String getAltreCarat231() {
		return altreCarat231;
	}

	public void setAltreCarat231(String altreCarat231) {
		this.altreCarat231 = altreCarat231;
	}

	public String getPubblicatoSiNo() {
		return pubblicatoSiNo;
	}

	public void setPubblicatoSiNo(String pubblicatoSiNo) {
		this.pubblicatoSiNo = pubblicatoSiNo;
	}

	public String getTipoRisorsaElettronica() {
		return tipoRisorsaElettronica;
	}

	public void setTipoRisorsaElettronica(String tipoRisorsaElettronica) {
		this.tipoRisorsaElettronica = tipoRisorsaElettronica;
	}

	public String getIndicazioneSpecificaMateriale() {
		return indicazioneSpecificaMateriale;
	}

	public void setIndicazioneSpecificaMateriale(
			String indicazioneSpecificaMateriale) {
		this.indicazioneSpecificaMateriale = indicazioneSpecificaMateriale;
	}

	public String getColoreElettronico() {
		return coloreElettronico;
	}

	public void setColoreElettronico(String coloreElettronico) {
		this.coloreElettronico = coloreElettronico;
	}

	public String getDimensioniElettronico() {
		return dimensioniElettronico;
	}

	public void setDimensioniElettronico(String dimensioniElettronico) {
		this.dimensioniElettronico = dimensioniElettronico;
	}

	public String getSuonoElettronico() {
		return suonoElettronico;
	}

	public void setSuonoElettronico(String suonoElettronico) {
		this.suonoElettronico = suonoElettronico;
	}



}// end class XMLBodyTitoli
