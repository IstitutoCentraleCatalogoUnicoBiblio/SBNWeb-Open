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
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class DettaglioTitoloForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 2284875472057118887L;

	private boolean firstTime;

	private String bidPerRientroAnalitica;

	private DettaglioTitoloCompletoVO dettTitComVO;
	private DettaglioTitoloCompletoVO dettTitComVOOLD;
	// Aree della sintetica utilizzate per la prospettazione degli oggetti in creazione legame titolo
	private AreaDatiLegameTitoloVO areaDatiLegameTitoloVO;

	private boolean flagCondiviso;

	private String selezRadioNumStandard;
	private String selezRadioImpronta;
	private String selezRadioPersonaggio;
	private String selezRadioIncipit;
	private String selezRadioLinkEsterni;
	private String selezRadioReperCartaceo;

	private List<TabellaNumSTDImpronteVO> listaRepertoriModificato;
	private String selezRadioRepertori;

	private String tipoMateriale;
	private String natura;

	private String naturaDefault;

	private String tipoProspettazione;

    // Inizio Intervento per Google3: l’interrogazione effettuata da un Polo non Abilitato ad uno specifico Materiale
    // non propone le specificità ma invia il DocType del moderno mantenendo il tipo Materiale corretto, quindi si deve
    // gestire l’inserimento nel DB di polo di documenti con tipo materiale ‘U’, ‘C’ o ‘G’ senza specificità
    // Campi per memorizzare lo stato della abilitazioni
	private String tipoProspetSpec;
	private boolean flagAbilitazSpec;
	// Fine Intervento per Google3

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private String tipoProspetSpecSoloMus;
	private boolean flagAbilitazSpecSoloMus;


	private String legame51;
	private String spoglio;

	private String legameTitUniRinvio;

	private String tipoCopiaReticolo;
	private TreeElementViewTitoli treeElementViewCopia;
	// Elenco oggetti per prospettazione descrizione/arrayList descrizioni/combo
	// per il riempimento delle liste di decodifica
	private List listaPaese;
	private String descPaese;

	private List listaNatura;
	private String descNatura;

	private List listaTipoMat;
	private String descTipoMat;

	private List listaLivAut;
	private String descLivAut;

	private List listaTipoRec;
	private String descTipoRec;

	private List listaLingua;
	private String descLingua1;
	private String descLingua2;
	private String descLingua3;

	private List listaGenere;

	private String descGenere1;
	private String descGenere2;
	private String descGenere3;
	private String descGenere4;

	private List listaTipoData;
	private String descTipoData;


//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	private List listaTipoTestoLetterarioArea0;
	private String descTipoTestoLetterarioArea0;

	// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
	// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
	// In creazione il default è REICAT.
	// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
	// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
	private List listaNormaCatalografiche;


	// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
	// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
	//	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni -->
	private List listaTipoTestoRegSonora;;
	private String descTipoTestoRegSonora;

	private String presenzaArea0BIS;

	private List listaFormaContenuto;
	private String descFormaContenuto;
	private String descFormaContenutoBIS;

	private List listaTipoContenuto;
	private String descTipoContenuto;
	private String descTipoContenutoBIS;

	private List listaMovimento;
	private String descMovimento;
	private String descMovimentoBIS;

	private List listaDimensione;
	private String descDimensione;
	private String descDimensioneBIS;

	private List listaSensorialita;
	private String descSensorialita1;
	private String descSensorialita2;
	private String descSensorialita3;
	private String descSensorialitaBIS1;
	private String descSensorialitaBIS2;
	private String descSensorialitaBIS3;

	private List listaTipoMediazione;
	private String descTipoMediazione;
	private String descTipoMediazioneBIS;
//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Fine definizione/Gestione Nuovi campi

	// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
	// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
	private List listaPubblicatoSiNo;
	private String descPubblicatoSiNo;


	// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
	private List listaTipoSupporto;
	private String descTipoSupporto;
	private String descTipoSupportoBIS;


	private List listaTipiNumStandard;
	private String descTipiNumStandard;

	private List listaElaborazione;
	private String descElaborazione;

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private String descTipoElaborazione;


	private List listaPresentazione;
	private String descPresentazione;

	private List listaGenereRappr;
	private String descGenereRappr;

	private String descSottoTipoLegame;

	//Combo e Descrizione per Materiale Grafico
	private List listaDesignMat;
	private String descDesignMat;

	private List listaSuppPrim;
	private String descSuppPrim;

	private List listaIndicCol;
	private String descIndicCol;

	private List listaIndicTec;
	private String descIndicTec1;
	private String descIndicTec2;
	private String descIndicTec3;
	private List listaIndicTecSta;
	private String descIndicTecSta1;
	private String descIndicTecSta2;
	private String descIndicTecSta3;

	private List listaDesignFun;
	private String descDesignFun;

	//Combo e Descrizione per Materiale Cartografico
	private List listaPubblicazioneGovernativa;
	private String descPubblicazioneGovernativa;
	private List listaSupportoFisico;
	private String descSupportoFisico;
	private List listaTecnicaCreazione;
	private String descTecnicaCreazione;
	private List listaFormaRiproduzione;
	private String descFormaRiproduzione;
	private List listaFormaPubblicazione;
	private String descFormaPubblicazione;


	private List listaAltitudine;
	private String descAltitudine;

	private List listaIndicatoreTipoScala;
	private String descIndicatoreTipoScala;

	private List listaTipoScala;
	private String descTipoScala;

	private List listaMeridianoOrigine;
	private String descMeridianoOrigine;

	private List listaCarattereImmagine;
	private String descCarattereImmagine;

	private List listaForma;
	private String descForma;
	private List listaPiattaforma;
	private String descPiattaforma;
	private List listaCategoriaSatellite;
	private String descCategoriaSatellite;

	// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
	private List listaProiezioneCarte;
	private String descProiezioneCarte;

	private List listaLongitudine;
	private List listaLatitudine;

	private String longInput1gg;
	private String longInput1pp;
	private String longInput1ss;
	private String longInput2gg;
	private String longInput2pp;
	private String longInput2ss;
	private String latiInput1gg;
	private String latiInput1pp;
	private String latiInput1ss;
	private String latiInput2gg;
	private String latiInput2pp;
	private String latiInput2ss;


	//Combo e Descrizione per Materiale Musicale
	private List listaStesura;
	private String descStesura;

	// Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
	// perchè non è un testo libero (Materiale Musicale manoscritto)
	private List listaMateria;
	private String descMateria;

	private List listaComposito;
	private String descComposito;
	private List listaPalinsesto;
	private String descPalinsesto;
	private List listaTimbroVocale;

	private List listaFormaMusicale;
	private String descFormaMusicale1;
	private String descFormaMusicale2;
	private String descFormaMusicale3;
	private List listaTonalita;
	private String descTonalita;

	private List listaTipoTestoLetterario;


	// MAggio 2015: almaviva2 L'evolutiva comporta l'inserimento sulla mappa di inserimento/variazione
	// la presenza di un nuovo Tasto button.calcolaArea5 SOLO ne caso di tipoMateriale=H che leggendo le
	// variabile ad essa relative componga in maniera automatica l'area5 DESCRIZIONE FISICA; previsto
	// inserimento di un nuovo campo (tipo supporto) e forse un altro per la quantita di supporti (es. 2CD)
	private String numSupporti;

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private String descTipoVideo;
	private String descLunghezza;
	private String descIndicatoreColore;
	private String descIndicatoreSuono;
	private String descSupportoSuono;
	private String descLarghezzaDimensioni;
	private String descFormaPubblDistr;
	private String descTecnicaVideoFilm;
	private String descPresentImmagMov;
	private String descMaterAccompagn1;
	private String descMaterAccompagn2;
	private String descMaterAccompagn3;
	private String descMaterAccompagn4;
	private String descPubblicVideoreg;
	private String descPresentVideoreg;
	private String descMaterialeEmulsBase;
	private String descMaterialeSupportSec;
	private String descStandardTrasmiss;
	private String descVersioneAudiovid;
	private String descElementiProd;
	private String descSpecCatColoreFilm;
	private String descEmulsionePellic;
	private String descComposPellic;
	private String descSuonoImmagMovimento;
	private String descTipoPellicStampa;



	private String descFormaPubblicazioneDisco = "";
	private String descVelocita = "";
	private String descTipoSuono = "";
	private String descLarghezzaScanal = "";
	private String descDimensioni = "";
	private String descLarghezzaNastro = "";
	private String descConfigurazNastro = "";
	private String descMaterTestAccompagn1 = "";
	private String descMaterTestAccompagn2 = "";
	private String descMaterTestAccompagn3 = "";
	private String descMaterTestAccompagn4 = "";
	private String descMaterTestAccompagn5 = "";
	private String descMaterTestAccompagn6 = "";
	private String descTecnicaRegistraz = "";
	private String descSpecCarattRiprod = "";
	private String descDatiCodifRegistrazSonore = "";
	private String descTipoDiMateriale = "";
	private String descTipoDiTaglio = "";
	private String descDurataRegistraz = "";


	private List listaTipoVideo;
	private List listaLunghezza;
	private List listaIndicatoreColore;
	private List listaIndicatoreSuono;
	private List listaSupportoSuono;
	private List listaLarghezzaDimensioni;
	private List listaFormaPubblDistr;
	private List listaTecnicaVideoFilm;
	private List listaPresentImmagMov;
	private List listaMaterAccompagn;
	private List listaPubblicVideoreg;
	private List listaPresentVideoreg;
	private List listaMaterialeEmulsBase;
	private List listaMaterialeSupportSec;
	private List listaStandardTrasmiss;
	private List listaVersioneAudiovid;
	private List listaElementiProd;
	private List listaSpecCatColoreFilm;
	private List listaEmulsionePellic;
	private List listaComposPellic;
	private List listaSuonoImmagMovimento;
	private List listaTipoPellicStampa;


	private List listaFormaPubblicazioneDisco;
	private List listaVelocita;
	private List listaTipoSuono;
	private List listaLarghezzaScanal;
	private List listaDimensioni;
	private List listaLarghezzaNastro;
	private List listaConfigurazNastro;
	private List listaMaterTestAccompagn;
	private List listaTecnicaRegistraz;
	private List listaSpecCarattRiprod;
	private List listaDatiCodifRegistrazSonore;
	private List listaTipoDiMateriale;
	private List listaTipoDiTaglio;

	// Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

	// almaviva2 Gennaio 2018 - Evolutiva per completamnet attività su materiale elettronico
	// Gestione nuovi campi specifici per etichetta 135
	private List listaTipoRisorsaElettronica;
	private List listaIndicazioneSpecificaMateriale;
	private List listaColoreElettronico;
	private List listaDimensioniElettronico;
	private List listaSuonoElettronico;
	private String descTipoRisorsaElettronica = "";
	private String descIndicazioneSpecificaMateriale = "";
	private String descColoreElettronico = "";
	private String descDimensioniElettronico = "";
	private String descSuonoElettronico = "";


//	Descrizione per i livelli di Autorità relativi allo specifico materiale
	private String descLivAutSpecMus;
	private String descLivAutSpecCar;
	private String descLivAutSpecGra;

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private String descLivAutSpecAud;
	private String descLivAutSpecEle;

	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// al link dei documenti su Basi Esterne - Link verso base date digitali
	private List listaLinkEsterni;
	private String descLinkEsterni;
	private List listaRepertoriTipoB;


	//almaviva2 27.07.2017 adeguamento a Indice gestione 231
	private List listaFormaOpera231;
	private String descFormaOpera231;

	public List getListaRepertoriTipoB() {
		return listaRepertoriTipoB;
	}

	public void setListaRepertoriTipoB(List listaRepertoriTipoB) {
		this.listaRepertoriTipoB = listaRepertoriTipoB;
	}

	//almaviva5_20100217 #3555
	boolean initialized;


	public DettaglioTitoloForm() {
		super();
		this.dettTitComVO = new DettaglioTitoloCompletoVO();
		this.dettTitComVOOLD = new DettaglioTitoloCompletoVO();
		this.areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();
	}

	public boolean isCambioNaturaAmmesso() {

		if (dettTitComVO == null)
			return false;

		DettaglioTitoloParteFissaVO titoloPFissaVO = dettTitComVO.getDetTitoloPFissaVO();
		if (titoloPFissaVO == null)
			return false;


		// almaviva2 11.03.2015 Inizio intervento per consentire il cambio tipo materiale da Antico a Musica/Cartografia/Grafica
//		boolean antico = ValidazioneDati.equals(titoloPFissaVO.getTipoMat(), "E");
//		String bid = titoloPFissaVO.getBid();
//		if (!antico || ValidazioneDati.strIsNull(bid))
//			return true;
//
//		if (antico && bid.matches(".{3}E\\d{6}"))
//			return false;
		// almaviva2 11.03.2015 Fine intervento per consentire il cambio tipo materiale da Antico a Musica/Cartografia/Grafica

		return true;
	}


	public TabellaNumSTDImpronteVO getItemNumStd(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloPFissaVO().getListaNumStandard().size()) {
        	this.dettTitComVO.getDetTitoloPFissaVO().getListaNumStandard().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.dettTitComVO.getDetTitoloPFissaVO().getListaNumStandard().get(index);
    }

	public TabellaNumSTDImpronteVO getItemLinkEsterni(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloPFissaVO().getListaLinkEsterni().size()) {
        	this.dettTitComVO.getDetTitoloPFissaVO().getListaLinkEsterni().add(new TabellaNumSTDImpronteVO());
        }
        return this.dettTitComVO.getDetTitoloPFissaVO().getListaLinkEsterni().get(index);
    }


	public TabellaNumSTDImpronteVO getItemReperCartaceo(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloPFissaVO().getListaReperCartaceo().size()) {
        	this.dettTitComVO.getDetTitoloPFissaVO().getListaReperCartaceo().add(new TabellaNumSTDImpronteVO());
        }
        return this.dettTitComVO.getDetTitoloPFissaVO().getListaReperCartaceo().get(index);
    }


    public TabellaNumSTDImpronteVO getItemImp(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloPFissaVO().getListaImpronte().size()) {
        	this.dettTitComVO.getDetTitoloPFissaVO().getListaImpronte().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.dettTitComVO.getDetTitoloPFissaVO().getListaImpronte().get(index);
    }

    public TabellaNumSTDImpronteVO getitemPersonaggi(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloMusVO().getListaPersonaggi().size()) {
        	this.dettTitComVO.getDetTitoloMusVO().getListaPersonaggi().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.dettTitComVO.getDetTitoloMusVO().getListaPersonaggi().get(index);
    }

    public TabellaNumSTDImpronteVO getitemPersonaggiModAnt(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloModAntVO().getListaPersonaggi().size()) {
        	this.dettTitComVO.getDetTitoloModAntVO().getListaPersonaggi().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.dettTitComVO.getDetTitoloModAntVO().getListaPersonaggi().get(index);
    }

    public TabellaNumSTDImpronteVO getitemIncipit(int index) {

        // automatically grow List size
        while (index >= this.dettTitComVO.getDetTitoloMusVO().getListaIncipit().size()) {
        	this.dettTitComVO.getDetTitoloMusVO().getListaIncipit().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.dettTitComVO.getDetTitoloMusVO().getListaIncipit().get(index);
    }

	public List<TabellaNumSTDImpronteVO> getListaRepertoriModificato() {
		return listaRepertoriModificato;
	}

	public void setListaRepertoriModificato(
			List<TabellaNumSTDImpronteVO> listaRepertoriModificato) {
		this.listaRepertoriModificato = listaRepertoriModificato;
	}

	public List addListaRepertoriModificato(TabellaNumSTDImpronteVO tabRep) {
		listaRepertoriModificato.add(tabRep);
		return listaRepertoriModificato;
	}


	public List creaListaRepertoriModificato() {
		this.listaRepertoriModificato = new ArrayList<TabellaNumSTDImpronteVO>();
		return listaRepertoriModificato;
	}


	public TabellaNumSTDImpronteVO getItem(int idxRepert) {

        // automatically grow List size
        while (idxRepert >= this.getListaRepertoriModificato().size()) {
        	this.getListaRepertoriModificato().add(new TabellaNumSTDImpronteVO());
        }
        return this.getListaRepertoriModificato().get(idxRepert);
    }

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public DettaglioTitoloCompletoVO getDettTitComVO() {
		return dettTitComVO;
	}

	public void setDettTitComVO(DettaglioTitoloCompletoVO dettTitComVO) {
		this.dettTitComVO = dettTitComVO;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public String getDescGenere1() {
		return descGenere1;
	}

	public void setDescGenere1(String descGenere1) {
		this.descGenere1 = descGenere1;
	}

	public String getDescGenere2() {
		return descGenere2;
	}

	public void setDescGenere2(String descGenere2) {
		this.descGenere2 = descGenere2;
	}

	public String getDescGenere3() {
		return descGenere3;
	}

	public void setDescGenere3(String descGenere3) {
		this.descGenere3 = descGenere3;
	}

	public String getDescGenere4() {
		return descGenere4;
	}

	public void setDescGenere4(String descGenere4) {
		this.descGenere4 = descGenere4;
	}

	public String getDescLingua1() {
		return descLingua1;
	}

	public void setDescLingua1(String descLingua1) {
		this.descLingua1 = descLingua1;
	}

	public String getDescLingua2() {
		return descLingua2;
	}

	public void setDescLingua2(String descLingua2) {
		this.descLingua2 = descLingua2;
	}

	public String getDescLingua3() {
		return descLingua3;
	}

	public void setDescLingua3(String descLingua3) {
		this.descLingua3 = descLingua3;
	}

	public String getDescNatura() {
		return descNatura;
	}

	public void setDescNatura(String descNatura) {
		this.descNatura = descNatura;
	}

	public String getDescPaese() {
		return descPaese;
	}

	public void setDescPaese(String descPaese) {
		this.descPaese = descPaese;
	}

	public String getDescTipoData() {
		return descTipoData;
	}

	public void setDescTipoData(String descTipoData) {
		this.descTipoData = descTipoData;
	}

	public String getDescTipoMat() {
		return descTipoMat;
	}

	public void setDescTipoMat(String descTipoMat) {
		this.descTipoMat = descTipoMat;
	}

	public String getDescTipoRec() {
		return descTipoRec;
	}

	public void setDescTipoRec(String descTipoRec) {
		this.descTipoRec = descTipoRec;
	}

	public List getListaLingua() {
		return listaLingua;
	}

	public void setListaLingua(List listaLingua) {
		this.listaLingua = listaLingua;
	}

	public List getListaNatura() {
		return listaNatura;
	}

	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}

	public List getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}

	public List getListaTipoMat() {
		return listaTipoMat;
	}

	public void setListaTipoMat(List listaTipoMat) {
		this.listaTipoMat = listaTipoMat;
	}

	public List getListaTipoRec() {
		return listaTipoRec;
	}

	public void setListaTipoRec(List listaTipoRec) {
		this.listaTipoRec = listaTipoRec;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public String getDescTipiNumStandard() {
		return descTipiNumStandard;
	}

	public void setDescTipiNumStandard(String descTipiNumStandard) {
		this.descTipiNumStandard = descTipiNumStandard;
	}

	public List getListaTipiNumStandard() {
		return listaTipiNumStandard;
	}

	public void setListaTipiNumStandard(List listaTipiNumStandard) {
		this.listaTipiNumStandard = listaTipiNumStandard;
	}

	public String getDescElaborazione() {
		return descElaborazione;
	}

	public void setDescElaborazione(String descElaborazione) {
		this.descElaborazione = descElaborazione;
	}

	public String getDescPresentazione() {
		return descPresentazione;
	}

	public void setDescPresentazione(String descPresentazione) {
		this.descPresentazione = descPresentazione;
	}

	public List getListaElaborazione() {
		return listaElaborazione;
	}

	public void setListaElaborazione(List listaElaborazione) {
		this.listaElaborazione = listaElaborazione;
	}

	public List getListaPresentazione() {
		return listaPresentazione;
	}

	public void setListaPresentazione(List listaPresentazione) {
		this.listaPresentazione = listaPresentazione;
	}

	public String getDescGenereRappr() {
		return descGenereRappr;
	}

	public void setDescGenereRappr(String descGenereRappr) {
		this.descGenereRappr = descGenereRappr;
	}

	public List getListaGenereRappr() {
		return listaGenereRappr;
	}

	public void setListaGenereRappr(List listaGenereRappr) {
		this.listaGenereRappr = listaGenereRappr;
	}

	public String getDescLivAut() {
		return descLivAut;
	}

	public void setDescLivAut(String descLivAut) {
		this.descLivAut = descLivAut;
	}

	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public String getDescDesignFun() {
		return descDesignFun;
	}

	public void setDescDesignFun(String descDesignFun) {
		this.descDesignFun = descDesignFun;
	}

	public String getDescDesignMat() {
		return descDesignMat;
	}

	public void setDescDesignMat(String descDesignMat) {
		this.descDesignMat = descDesignMat;
	}

	public String getDescIndicCol() {
		return descIndicCol;
	}

	public void setDescIndicCol(String descIndicCol) {
		this.descIndicCol = descIndicCol;
	}

	public String getDescIndicTec1() {
		return descIndicTec1;
	}

	public void setDescIndicTec1(String descIndicTec1) {
		this.descIndicTec1 = descIndicTec1;
	}

	public String getDescIndicTec2() {
		return descIndicTec2;
	}

	public void setDescIndicTec2(String descIndicTec2) {
		this.descIndicTec2 = descIndicTec2;
	}

	public String getDescIndicTec3() {
		return descIndicTec3;
	}

	public void setDescIndicTec3(String descIndicTec3) {
		this.descIndicTec3 = descIndicTec3;
	}

	public String getDescSuppPrim() {
		return descSuppPrim;
	}

	public void setDescSuppPrim(String descSuppPrim) {
		this.descSuppPrim = descSuppPrim;
	}

	public List getListaDesignFun() {
		return listaDesignFun;
	}

	public void setListaDesignFun(List listaDesignFun) {
		this.listaDesignFun = listaDesignFun;
	}

	public List getListaDesignMat() {
		return listaDesignMat;
	}

	public void setListaDesignMat(List listaDesignMat) {
		this.listaDesignMat = listaDesignMat;
	}

	public List getListaIndicCol() {
		return listaIndicCol;
	}

	public void setListaIndicCol(List listaIndicCol) {
		this.listaIndicCol = listaIndicCol;
	}

	public List getListaIndicTec() {
		return listaIndicTec;
	}

	public void setListaIndicTec(List listaIndicTec) {
		this.listaIndicTec = listaIndicTec;
	}

	public List getListaSuppPrim() {
		return listaSuppPrim;
	}

	public void setListaSuppPrim(List listaSuppPrim) {
		this.listaSuppPrim = listaSuppPrim;
	}

	public String getSelezRadioNumStandard() {
		return selezRadioNumStandard;
	}

	public void setSelezRadioNumStandard(String selezRadioNumStandard) {
		this.selezRadioNumStandard = selezRadioNumStandard;
	}


	public DettaglioTitoloCompletoVO getDettTitComVOOLD() {
		return dettTitComVOOLD;
	}


	public void setDettTitComVOOLD(DettaglioTitoloCompletoVO dettTitComVOOLD) {
		this.dettTitComVOOLD = (DettaglioTitoloCompletoVO) dettTitComVOOLD.clone();
	}


	public String getSelezRadioImpronta() {
		return selezRadioImpronta;
	}


	public void setSelezRadioImpronta(String selezRadioImpronta) {
		this.selezRadioImpronta = selezRadioImpronta;
	}

	public List getListaGenere() {
		return listaGenere;
	}

	public void setListaGenere(List listaGenere) {
		this.listaGenere = listaGenere;
	}

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public String getDescAltitudine() {
		return descAltitudine;
	}

	public void setDescAltitudine(String descAltitudine) {
		this.descAltitudine = descAltitudine;
	}

	public String getDescCarattereImmagine() {
		return descCarattereImmagine;
	}

	public void setDescCarattereImmagine(String descCarattereImmagine) {
		this.descCarattereImmagine = descCarattereImmagine;
	}

	public String getDescCategoriaSatellite() {
		return descCategoriaSatellite;
	}

	public void setDescCategoriaSatellite(String descCategoriaSatellite) {
		this.descCategoriaSatellite = descCategoriaSatellite;
	}

	public String getDescForma() {
		return descForma;
	}

	public void setDescForma(String descForma) {
		this.descForma = descForma;
	}

	public String getDescFormaPubblicazione() {
		return descFormaPubblicazione;
	}

	public void setDescFormaPubblicazione(String descFormaPubblicazione) {
		this.descFormaPubblicazione = descFormaPubblicazione;
	}

	public String getDescFormaRiproduzione() {
		return descFormaRiproduzione;
	}

	public void setDescFormaRiproduzione(String descFormaRiproduzione) {
		this.descFormaRiproduzione = descFormaRiproduzione;
	}

	public String getDescIndicatoreTipoScala() {
		return descIndicatoreTipoScala;
	}

	public void setDescIndicatoreTipoScala(String descIndicatoreTipoScala) {
		this.descIndicatoreTipoScala = descIndicatoreTipoScala;
	}

	public String getDescMeridianoOrigine() {
		return descMeridianoOrigine;
	}

	public void setDescMeridianoOrigine(String descMeridianoOrigine) {
		this.descMeridianoOrigine = descMeridianoOrigine;
	}

	public String getDescPiattaforma() {
		return descPiattaforma;
	}

	public void setDescPiattaforma(String descPiattaforma) {
		this.descPiattaforma = descPiattaforma;
	}

	public String getDescPubblicazioneGovernativa() {
		return descPubblicazioneGovernativa;
	}

	public void setDescPubblicazioneGovernativa(String descPubblicazioneGovernativa) {
		this.descPubblicazioneGovernativa = descPubblicazioneGovernativa;
	}

	public String getDescSupportoFisico() {
		return descSupportoFisico;
	}

	public void setDescSupportoFisico(String descSupportoFisico) {
		this.descSupportoFisico = descSupportoFisico;
	}

	public String getDescTecnicaCreazione() {
		return descTecnicaCreazione;
	}

	public void setDescTecnicaCreazione(String descTecnicaCreazione) {
		this.descTecnicaCreazione = descTecnicaCreazione;
	}

	public String getDescTipoScala() {
		return descTipoScala;
	}

	public void setDescTipoScala(String descTipoScala) {
		this.descTipoScala = descTipoScala;
	}

	public List getListaAltitudine() {
		return listaAltitudine;
	}

	public void setListaAltitudine(List listaAltitudine) {
		this.listaAltitudine = listaAltitudine;
	}

	public List getListaCarattereImmagine() {
		return listaCarattereImmagine;
	}

	public void setListaCarattereImmagine(List listaCarattereImmagine) {
		this.listaCarattereImmagine = listaCarattereImmagine;
	}

	public List getListaCategoriaSatellite() {
		return listaCategoriaSatellite;
	}

	public void setListaCategoriaSatellite(List listaCategoriaSatellite) {
		this.listaCategoriaSatellite = listaCategoriaSatellite;
	}

	public List getListaForma() {
		return listaForma;
	}

	public void setListaForma(List listaForma) {
		this.listaForma = listaForma;
	}

	public List getListaFormaPubblicazione() {
		return listaFormaPubblicazione;
	}

	public void setListaFormaPubblicazione(List listaFormaPubblicazione) {
		this.listaFormaPubblicazione = listaFormaPubblicazione;
	}

	public List getListaFormaRiproduzione() {
		return listaFormaRiproduzione;
	}

	public void setListaFormaRiproduzione(List listaFormaRiproduzione) {
		this.listaFormaRiproduzione = listaFormaRiproduzione;
	}

	public List getListaIndicatoreTipoScala() {
		return listaIndicatoreTipoScala;
	}

	public void setListaIndicatoreTipoScala(List listaIndicatoreTipoScala) {
		this.listaIndicatoreTipoScala = listaIndicatoreTipoScala;
	}

	public List getListaMeridianoOrigine() {
		return listaMeridianoOrigine;
	}

	public void setListaMeridianoOrigine(List listaMeridianoOrigine) {
		this.listaMeridianoOrigine = listaMeridianoOrigine;
	}

	public List getListaPiattaforma() {
		return listaPiattaforma;
	}

	public void setListaPiattaforma(List listaPiattaforma) {
		this.listaPiattaforma = listaPiattaforma;
	}

	public List getListaPubblicazioneGovernativa() {
		return listaPubblicazioneGovernativa;
	}

	public void setListaPubblicazioneGovernativa(
			List listaPubblicazioneGovernativa) {
		this.listaPubblicazioneGovernativa = listaPubblicazioneGovernativa;
	}

	public List getListaSupportoFisico() {
		return listaSupportoFisico;
	}

	public void setListaSupportoFisico(List listaSupportoFisico) {
		this.listaSupportoFisico = listaSupportoFisico;
	}

	public List getListaTecnicaCreazione() {
		return listaTecnicaCreazione;
	}

	public void setListaTecnicaCreazione(List listaTecnicaCreazione) {
		this.listaTecnicaCreazione = listaTecnicaCreazione;
	}

	public List getListaTipoScala() {
		return listaTipoScala;
	}

	public void setListaTipoScala(List listaTipoScala) {
		this.listaTipoScala = listaTipoScala;
	}

	public List getListaLatitudine() {
		return listaLatitudine;
	}

	public void setListaLatitudine(List listaLatitudine) {
		this.listaLatitudine = listaLatitudine;
	}

	public List getListaLongitudine() {
		return listaLongitudine;
	}

	public void setListaLongitudine(List listaLongitudine) {
		this.listaLongitudine = listaLongitudine;
	}

	public String getLatiInput1gg() {
		return latiInput1gg;
	}

	public void setLatiInput1gg(String latiInput1gg) {
		this.latiInput1gg = latiInput1gg;
	}

	public String getLatiInput1pp() {
		return latiInput1pp;
	}

	public void setLatiInput1pp(String latiInput1pp) {
		this.latiInput1pp = latiInput1pp;
	}

	public String getLatiInput1ss() {
		return latiInput1ss;
	}

	public void setLatiInput1ss(String latiInput1ss) {
		this.latiInput1ss = latiInput1ss;
	}

	public String getLatiInput2gg() {
		return latiInput2gg;
	}

	public void setLatiInput2gg(String latiInput2gg) {
		this.latiInput2gg = latiInput2gg;
	}

	public String getLatiInput2pp() {
		return latiInput2pp;
	}

	public void setLatiInput2pp(String latiInput2pp) {
		this.latiInput2pp = latiInput2pp;
	}

	public String getLatiInput2ss() {
		return latiInput2ss;
	}

	public void setLatiInput2ss(String latiInput2ss) {
		this.latiInput2ss = latiInput2ss;
	}

	public String getLongInput1gg() {
		return longInput1gg;
	}

	public void setLongInput1gg(String longInput1gg) {
		this.longInput1gg = longInput1gg;
	}

	public String getLongInput1pp() {
		return longInput1pp;
	}

	public void setLongInput1pp(String longInput1pp) {
		this.longInput1pp = longInput1pp;
	}

	public String getLongInput1ss() {
		return longInput1ss;
	}

	public void setLongInput1ss(String longInput1ss) {
		this.longInput1ss = longInput1ss;
	}

	public String getLongInput2gg() {
		return longInput2gg;
	}

	public void setLongInput2gg(String longInput2gg) {
		this.longInput2gg = longInput2gg;
	}

	public String getLongInput2pp() {
		return longInput2pp;
	}

	public void setLongInput2pp(String longInput2pp) {
		this.longInput2pp = longInput2pp;
	}

	public String getLongInput2ss() {
		return longInput2ss;
	}

	public void setLongInput2ss(String longInput2ss) {
		this.longInput2ss = longInput2ss;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getDescComposito() {
		return descComposito;
	}

	public void setDescComposito(String descComposito) {
		this.descComposito = descComposito;
	}

	public String getDescPalinsesto() {
		return descPalinsesto;
	}

	public void setDescPalinsesto(String descPalinsesto) {
		this.descPalinsesto = descPalinsesto;
	}

	public String getDescStesura() {
		return descStesura;
	}

	public void setDescStesura(String descStesura) {
		this.descStesura = descStesura;
	}

	public List getListaComposito() {
		return listaComposito;
	}

	public void setListaComposito(List listaComposito) {
		this.listaComposito = listaComposito;
	}

	public List getListaPalinsesto() {
		return listaPalinsesto;
	}

	public void setListaPalinsesto(List listaPalinsesto) {
		this.listaPalinsesto = listaPalinsesto;
	}

	public List getListaStesura() {
		return listaStesura;
	}

	public void setListaStesura(List listaStesura) {
		this.listaStesura = listaStesura;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public String getDescLivAutSpecCar() {
		return descLivAutSpecCar;
	}

	public void setDescLivAutSpecCar(String descLivAutSpecCar) {
		this.descLivAutSpecCar = descLivAutSpecCar;
	}

	public String getDescLivAutSpecGra() {
		return descLivAutSpecGra;
	}

	public void setDescLivAutSpecGra(String descLivAutSpecGra) {
		this.descLivAutSpecGra = descLivAutSpecGra;
	}

	public String getDescLivAutSpecMus() {
		return descLivAutSpecMus;
	}

	public void setDescLivAutSpecMus(String descLivAutSpecMus) {
		this.descLivAutSpecMus = descLivAutSpecMus;
	}

	public String getSelezRadioIncipit() {
		return selezRadioIncipit;
	}

	public void setSelezRadioIncipit(String selezRadioIncipit) {
		this.selezRadioIncipit = selezRadioIncipit;
	}

	public String getSelezRadioPersonaggio() {
		return selezRadioPersonaggio;
	}

	public void setSelezRadioPersonaggio(String selezRadioPersonaggio) {
		this.selezRadioPersonaggio = selezRadioPersonaggio;
	}

	public List getListaTimbroVocale() {
		return listaTimbroVocale;
	}

	public void setListaTimbroVocale(List listaTimbroVocale) {
		this.listaTimbroVocale = listaTimbroVocale;
	}


	public String getDescIndicTecSta1() {
		return descIndicTecSta1;
	}

	public void setDescIndicTecSta1(String descIndicTecSta1) {
		this.descIndicTecSta1 = descIndicTecSta1;
	}

	public String getDescIndicTecSta2() {
		return descIndicTecSta2;
	}

	public void setDescIndicTecSta2(String descIndicTecSta2) {
		this.descIndicTecSta2 = descIndicTecSta2;
	}

	public String getDescIndicTecSta3() {
		return descIndicTecSta3;
	}

	public void setDescIndicTecSta3(String descIndicTecSta3) {
		this.descIndicTecSta3 = descIndicTecSta3;
	}

	public List getListaIndicTecSta() {
		return listaIndicTecSta;
	}

	public void setListaIndicTecSta(List listaIndicTecSta) {
		this.listaIndicTecSta = listaIndicTecSta;
	}

	public List getListaFormaMusicale() {
		return listaFormaMusicale;
	}

	public void setListaFormaMusicale(List listaFormaMusicale) {
		this.listaFormaMusicale = listaFormaMusicale;
	}

	public List getListaTonalita() {
		return listaTonalita;
	}

	public void setListaTonalita(List listaTonalita) {
		this.listaTonalita = listaTonalita;
	}

	public List getListaTipoTestoLetterario() {
		return listaTipoTestoLetterario;
	}

	public void setListaTipoTestoLetterario(List listaTipoTestoLetterario) {
		this.listaTipoTestoLetterario = listaTipoTestoLetterario;
	}

	public String getDescFormaMusicale1() {
		return descFormaMusicale1;
	}

	public void setDescFormaMusicale1(String descFormaMusicale1) {
		this.descFormaMusicale1 = descFormaMusicale1;
	}

	public String getDescFormaMusicale2() {
		return descFormaMusicale2;
	}

	public void setDescFormaMusicale2(String descFormaMusicale2) {
		this.descFormaMusicale2 = descFormaMusicale2;
	}

	public String getDescFormaMusicale3() {
		return descFormaMusicale3;
	}

	public void setDescFormaMusicale3(String descFormaMusicale3) {
		this.descFormaMusicale3 = descFormaMusicale3;
	}

	public String getDescTonalita() {
		return descTonalita;
	}

	public void setDescTonalita(String descTonalita) {
		this.descTonalita = descTonalita;
	}

	public String getLegame51() {
		return legame51;
	}

	public void setLegame51(String legame51) {
		this.legame51 = legame51;
	}

	public String getDescSottoTipoLegame() {
		return descSottoTipoLegame;
	}

	public void setDescSottoTipoLegame(String descSottoTipoLegame) {
		this.descSottoTipoLegame = descSottoTipoLegame;
	}

	public String getBidPerRientroAnalitica() {
		return bidPerRientroAnalitica;
	}

	public void setBidPerRientroAnalitica(String bidPerRientroAnalitica) {
		this.bidPerRientroAnalitica = bidPerRientroAnalitica;
	}

	public String getSelezRadioRepertori() {
		return selezRadioRepertori;
	}

	public void setSelezRadioRepertori(String selezRadioRepertori) {
		this.selezRadioRepertori = selezRadioRepertori;
	}


	public String getTipoCopiaReticolo() {
		return tipoCopiaReticolo;
	}


	public void setTipoCopiaReticolo(String tipoCopiaReticolo) {
		this.tipoCopiaReticolo = tipoCopiaReticolo;
	}


	public TreeElementViewTitoli getTreeElementViewCopia() {
		return treeElementViewCopia;
	}


	public void setTreeElementViewCopia(TreeElementViewTitoli treeElementViewCopia) {
		this.treeElementViewCopia = treeElementViewCopia;
	}


	public String getSpoglio() {
		return spoglio;
	}


	public void setSpoglio(String spoglio) {
		this.spoglio = spoglio;
	}


	public boolean isInitialized() {
		return initialized;
	}


	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public String getNaturaDefault() {
		return naturaDefault;
	}

	public void setNaturaDefault(String naturaDefault) {
		this.naturaDefault = naturaDefault;
	}

	public String getTipoProspetSpec() {
		return tipoProspetSpec;
	}

	public void setTipoProspetSpec(String tipoProspetSpec) {
		this.tipoProspetSpec = tipoProspetSpec;
	}

	public boolean isFlagAbilitazSpec() {
		return flagAbilitazSpec;
	}

	public void setFlagAbilitazSpec(boolean flagAbilitazSpec) {
		this.flagAbilitazSpec = flagAbilitazSpec;
	}

	public List getListaMateria() {
		return listaMateria;
	}

	public void setListaMateria(List listaMateria) {
		this.listaMateria = listaMateria;
	}

	public String getDescMateria() {
		return descMateria;
	}

	public void setDescMateria(String descMateria) {
		this.descMateria = descMateria;
	}

	public List getListaTipoTestoLetterarioArea0() {
		return listaTipoTestoLetterarioArea0;
	}

	public void setListaTipoTestoLetterarioArea0(
			List listaTipoTestoLetterarioArea0) {
		this.listaTipoTestoLetterarioArea0 = listaTipoTestoLetterarioArea0;
	}

	public List getListaNormaCatalografiche() {
		return listaNormaCatalografiche;
	}

	public void setListaNormaCatalografiche(List listaNormaCatalografiche) {
		this.listaNormaCatalografiche = listaNormaCatalografiche;
	}

	public List getListaFormaContenuto() {
		return listaFormaContenuto;
	}

	public void setListaFormaContenuto(List listaFormaContenuto) {
		this.listaFormaContenuto = listaFormaContenuto;
	}

	public String getDescFormaContenuto() {
		return descFormaContenuto;
	}

	public void setDescFormaContenuto(String descFormaContenuto) {
		this.descFormaContenuto = descFormaContenuto;
	}

	public String getDescFormaContenutoBIS() {
		return descFormaContenutoBIS;
	}

	public void setDescFormaContenutoBIS(String descFormaContenutoBIS) {
		this.descFormaContenutoBIS = descFormaContenutoBIS;
	}

	public List getListaTipoContenuto() {
		return listaTipoContenuto;
	}

	public void setListaTipoContenuto(List listaTipoContenuto) {
		this.listaTipoContenuto = listaTipoContenuto;
	}

	public String getDescTipoContenuto() {
		return descTipoContenuto;
	}

	public void setDescTipoContenuto(String descTipoContenuto) {
		this.descTipoContenuto = descTipoContenuto;
	}

	public String getDescTipoContenutoBIS() {
		return descTipoContenutoBIS;
	}

	public void setDescTipoContenutoBIS(String descTipoContenutoBIS) {
		this.descTipoContenutoBIS = descTipoContenutoBIS;
	}

	public List getListaMovimento() {
		return listaMovimento;
	}

	public void setListaMovimento(List listaMovimento) {
		this.listaMovimento = listaMovimento;
	}

	public String getDescMovimento() {
		return descMovimento;
	}

	public void setDescMovimento(String descMovimento) {
		this.descMovimento = descMovimento;
	}

	public String getDescMovimentoBIS() {
		return descMovimentoBIS;
	}

	public void setDescMovimentoBIS(String descMovimentoBIS) {
		this.descMovimentoBIS = descMovimentoBIS;
	}

	public List getListaDimensione() {
		return listaDimensione;
	}

	public void setListaDimensione(List listaDimensione) {
		this.listaDimensione = listaDimensione;
	}

	public String getDescDimensione() {
		return descDimensione;
	}

	public void setDescDimensione(String descDimensione) {
		this.descDimensione = descDimensione;
	}

	public String getDescDimensioneBIS() {
		return descDimensioneBIS;
	}

	public void setDescDimensioneBIS(String descDimensioneBIS) {
		this.descDimensioneBIS = descDimensioneBIS;
	}

	public List getListaSensorialita() {
		return listaSensorialita;
	}

	public void setListaSensorialita(List listaSensorialita) {
		this.listaSensorialita = listaSensorialita;
	}

	public String getDescSensorialita1() {
		return descSensorialita1;
	}

	public void setDescSensorialita1(String descSensorialita1) {
		this.descSensorialita1 = descSensorialita1;
	}

	public String getDescSensorialita2() {
		return descSensorialita2;
	}

	public void setDescSensorialita2(String descSensorialita2) {
		this.descSensorialita2 = descSensorialita2;
	}

	public String getDescSensorialita3() {
		return descSensorialita3;
	}

	public void setDescSensorialita3(String descSensorialita3) {
		this.descSensorialita3 = descSensorialita3;
	}

	public String getDescSensorialitaBIS1() {
		return descSensorialitaBIS1;
	}

	public void setDescSensorialitaBIS1(String descSensorialitaBIS1) {
		this.descSensorialitaBIS1 = descSensorialitaBIS1;
	}

	public String getDescSensorialitaBIS2() {
		return descSensorialitaBIS2;
	}

	public void setDescSensorialitaBIS2(String descSensorialitaBIS2) {
		this.descSensorialitaBIS2 = descSensorialitaBIS2;
	}

	public String getDescSensorialitaBIS3() {
		return descSensorialitaBIS3;
	}

	public void setDescSensorialitaBIS3(String descSensorialitaBIS3) {
		this.descSensorialitaBIS3 = descSensorialitaBIS3;
	}

	public List getListaTipoMediazione() {
		return listaTipoMediazione;
	}

	public void setListaTipoMediazione(List listaTipoMediazione) {
		this.listaTipoMediazione = listaTipoMediazione;
	}

	public String getDescTipoMediazione() {
		return descTipoMediazione;
	}

	public void setDescTipoMediazione(String descTipoMediazione) {
		this.descTipoMediazione = descTipoMediazione;
	}

	public String getDescTipoMediazioneBIS() {
		return descTipoMediazioneBIS;
	}

	public void setDescTipoMediazioneBIS(String descTipoMediazioneBIS) {
		this.descTipoMediazioneBIS = descTipoMediazioneBIS;
	}

	public String getDescTipoTestoLetterarioArea0() {
		return descTipoTestoLetterarioArea0;
	}

	public void setDescTipoTestoLetterarioArea0(String descTipoTestoLetterarioArea0) {
		this.descTipoTestoLetterarioArea0 = descTipoTestoLetterarioArea0;
	}


	public String getDescTipoVideo() {
		return descTipoVideo;
	}

	public void setDescTipoVideo(String descTipoVideo) {
		this.descTipoVideo = descTipoVideo;
	}

	public String getDescLunghezza() {
		return descLunghezza;
	}

	public void setDescLunghezza(String descLunghezza) {
		this.descLunghezza = descLunghezza;
	}

	public String getDescIndicatoreColore() {
		return descIndicatoreColore;
	}

	public void setDescIndicatoreColore(String descIndicatoreColore) {
		this.descIndicatoreColore = descIndicatoreColore;
	}

	public String getDescIndicatoreSuono() {
		return descIndicatoreSuono;
	}

	public void setDescIndicatoreSuono(String descIndicatoreSuono) {
		this.descIndicatoreSuono = descIndicatoreSuono;
	}

	public String getDescSupportoSuono() {
		return descSupportoSuono;
	}

	public void setDescSupportoSuono(String descSupportoSuono) {
		this.descSupportoSuono = descSupportoSuono;
	}

	public String getDescLarghezzaDimensioni() {
		return descLarghezzaDimensioni;
	}

	public void setDescLarghezzaDimensioni(String descLarghezzaDimensioni) {
		this.descLarghezzaDimensioni = descLarghezzaDimensioni;
	}

	public String getDescFormaPubblDistr() {
		return descFormaPubblDistr;
	}

	public void setDescFormaPubblDistr(String descFormaPubblDistr) {
		this.descFormaPubblDistr = descFormaPubblDistr;
	}

	public String getDescTecnicaVideoFilm() {
		return descTecnicaVideoFilm;
	}

	public void setDescTecnicaVideoFilm(String descTecnicaVideoFilm) {
		this.descTecnicaVideoFilm = descTecnicaVideoFilm;
	}

	public String getDescPresentImmagMov() {
		return descPresentImmagMov;
	}

	public void setDescPresentImmagMov(String descPresentImmagMov) {
		this.descPresentImmagMov = descPresentImmagMov;
	}

	public String getDescMaterAccompagn1() {
		return descMaterAccompagn1;
	}

	public void setDescMaterAccompagn1(String descMaterAccompagn1) {
		this.descMaterAccompagn1 = descMaterAccompagn1;
	}

	public String getDescMaterAccompagn2() {
		return descMaterAccompagn2;
	}

	public void setDescMaterAccompagn2(String descMaterAccompagn2) {
		this.descMaterAccompagn2 = descMaterAccompagn2;
	}

	public String getDescMaterAccompagn3() {
		return descMaterAccompagn3;
	}

	public void setDescMaterAccompagn3(String descMaterAccompagn3) {
		this.descMaterAccompagn3 = descMaterAccompagn3;
	}

	public String getDescMaterAccompagn4() {
		return descMaterAccompagn4;
	}

	public void setDescMaterAccompagn4(String descMaterAccompagn4) {
		this.descMaterAccompagn4 = descMaterAccompagn4;
	}

	public String getDescPubblicVideoreg() {
		return descPubblicVideoreg;
	}

	public void setDescPubblicVideoreg(String descPubblicVideoreg) {
		this.descPubblicVideoreg = descPubblicVideoreg;
	}

	public String getDescPresentVideoreg() {
		return descPresentVideoreg;
	}

	public void setDescPresentVideoreg(String descPresentVideoreg) {
		this.descPresentVideoreg = descPresentVideoreg;
	}

	public String getDescMaterialeEmulsBase() {
		return descMaterialeEmulsBase;
	}

	public void setDescMaterialeEmulsBase(String descMaterialeEmulsBase) {
		this.descMaterialeEmulsBase = descMaterialeEmulsBase;
	}

	public String getDescMaterialeSupportSec() {
		return descMaterialeSupportSec;
	}

	public void setDescMaterialeSupportSec(String descMaterialeSupportSec) {
		this.descMaterialeSupportSec = descMaterialeSupportSec;
	}

	public String getDescStandardTrasmiss() {
		return descStandardTrasmiss;
	}

	public void setDescStandardTrasmiss(String descStandardTrasmiss) {
		this.descStandardTrasmiss = descStandardTrasmiss;
	}

	public String getDescVersioneAudiovid() {
		return descVersioneAudiovid;
	}

	public void setDescVersioneAudiovid(String descVersioneAudiovid) {
		this.descVersioneAudiovid = descVersioneAudiovid;
	}

	public String getDescElementiProd() {
		return descElementiProd;
	}

	public void setDescElementiProd(String descElementiProd) {
		this.descElementiProd = descElementiProd;
	}

	public String getDescSpecCatColoreFilm() {
		return descSpecCatColoreFilm;
	}

	public void setDescSpecCatColoreFilm(String descSpecCatColoreFilm) {
		this.descSpecCatColoreFilm = descSpecCatColoreFilm;
	}

	public String getDescEmulsionePellic() {
		return descEmulsionePellic;
	}

	public void setDescEmulsionePellic(String descEmulsionePellic) {
		this.descEmulsionePellic = descEmulsionePellic;
	}

	public String getDescComposPellic() {
		return descComposPellic;
	}

	public void setDescComposPellic(String descComposPellic) {
		this.descComposPellic = descComposPellic;
	}

	public String getDescSuonoImmagMovimento() {
		return descSuonoImmagMovimento;
	}

	public void setDescSuonoImmagMovimento(String descSuonoImmagMovimento) {
		this.descSuonoImmagMovimento = descSuonoImmagMovimento;
	}

	public String getDescTipoPellicStampa() {
		return descTipoPellicStampa;
	}

	public void setDescTipoPellicStampa(String descTipoPellicStampa) {
		this.descTipoPellicStampa = descTipoPellicStampa;
	}

	public List getListaTipoVideo() {
		return listaTipoVideo;
	}

	public void setListaTipoVideo(List listaTipoVideo) {
		this.listaTipoVideo = listaTipoVideo;
	}

	public List getListaLunghezza() {
		return listaLunghezza;
	}

	public void setListaLunghezza(List listaLunghezza) {
		this.listaLunghezza = listaLunghezza;
	}

	public List getListaIndicatoreColore() {
		return listaIndicatoreColore;
	}

	public void setListaIndicatoreColore(List listaIndicatoreColore) {
		this.listaIndicatoreColore = listaIndicatoreColore;
	}

	public List getListaIndicatoreSuono() {
		return listaIndicatoreSuono;
	}

	public void setListaIndicatoreSuono(List listaIndicatoreSuono) {
		this.listaIndicatoreSuono = listaIndicatoreSuono;
	}

	public List getListaSupportoSuono() {
		return listaSupportoSuono;
	}

	public void setListaSupportoSuono(List listaSupportoSuono) {
		this.listaSupportoSuono = listaSupportoSuono;
	}

	public List getListaLarghezzaDimensioni() {
		return listaLarghezzaDimensioni;
	}

	public void setListaLarghezzaDimensioni(List listaLarghezzaDimensioni) {
		this.listaLarghezzaDimensioni = listaLarghezzaDimensioni;
	}

	public List getListaFormaPubblDistr() {
		return listaFormaPubblDistr;
	}

	public void setListaFormaPubblDistr(List listaFormaPubblDistr) {
		this.listaFormaPubblDistr = listaFormaPubblDistr;
	}

	public List getListaTecnicaVideoFilm() {
		return listaTecnicaVideoFilm;
	}

	public void setListaTecnicaVideoFilm(List listaTecnicaVideoFilm) {
		this.listaTecnicaVideoFilm = listaTecnicaVideoFilm;
	}

	public List getListaPresentImmagMov() {
		return listaPresentImmagMov;
	}

	public void setListaPresentImmagMov(List listaPresentImmagMov) {
		this.listaPresentImmagMov = listaPresentImmagMov;
	}

	public List getListaMaterAccompagn() {
		return listaMaterAccompagn;
	}

	public void setListaMaterAccompagn(List listaMaterAccompagn) {
		this.listaMaterAccompagn = listaMaterAccompagn;
	}

	public List getListaPubblicVideoreg() {
		return listaPubblicVideoreg;
	}

	public void setListaPubblicVideoreg(List listaPubblicVideoreg) {
		this.listaPubblicVideoreg = listaPubblicVideoreg;
	}

	public List getListaPresentVideoreg() {
		return listaPresentVideoreg;
	}

	public void setListaPresentVideoreg(List listaPresentVideoreg) {
		this.listaPresentVideoreg = listaPresentVideoreg;
	}

	public List getListaMaterialeEmulsBase() {
		return listaMaterialeEmulsBase;
	}

	public void setListaMaterialeEmulsBase(List listaMaterialeEmulsBase) {
		this.listaMaterialeEmulsBase = listaMaterialeEmulsBase;
	}

	public List getListaMaterialeSupportSec() {
		return listaMaterialeSupportSec;
	}

	public void setListaMaterialeSupportSec(List listaMaterialeSupportSec) {
		this.listaMaterialeSupportSec = listaMaterialeSupportSec;
	}

	public List getListaStandardTrasmiss() {
		return listaStandardTrasmiss;
	}

	public void setListaStandardTrasmiss(List listaStandardTrasmiss) {
		this.listaStandardTrasmiss = listaStandardTrasmiss;
	}

	public List getListaVersioneAudiovid() {
		return listaVersioneAudiovid;
	}

	public void setListaVersioneAudiovid(List listaVersioneAudiovid) {
		this.listaVersioneAudiovid = listaVersioneAudiovid;
	}

	public List getListaElementiProd() {
		return listaElementiProd;
	}

	public void setListaElementiProd(List listaElementiProd) {
		this.listaElementiProd = listaElementiProd;
	}

	public List getListaSpecCatColoreFilm() {
		return listaSpecCatColoreFilm;
	}

	public void setListaSpecCatColoreFilm(List listaSpecCatColoreFilm) {
		this.listaSpecCatColoreFilm = listaSpecCatColoreFilm;
	}

	public List getListaEmulsionePellic() {
		return listaEmulsionePellic;
	}

	public void setListaEmulsionePellic(List listaEmulsionePellic) {
		this.listaEmulsionePellic = listaEmulsionePellic;
	}

	public List getListaComposPellic() {
		return listaComposPellic;
	}

	public void setListaComposPellic(List listaComposPellic) {
		this.listaComposPellic = listaComposPellic;
	}

	public List getListaSuonoImmagMovimento() {
		return listaSuonoImmagMovimento;
	}

	public void setListaSuonoImmagMovimento(List listaSuonoImmagMovimento) {
		this.listaSuonoImmagMovimento = listaSuonoImmagMovimento;
	}

	public List getListaTipoPellicStampa() {
		return listaTipoPellicStampa;
	}

	public void setListaTipoPellicStampa(List listaTipoPellicStampa) {
		this.listaTipoPellicStampa = listaTipoPellicStampa;
	}

	public String getDescLivAutSpecAud() {
		return descLivAutSpecAud;
	}

	public void setDescLivAutSpecAud(String descLivAutSpecAud) {
		this.descLivAutSpecAud = descLivAutSpecAud;
	}

	public String getDescLivAutSpecEle() {
		return descLivAutSpecEle;
	}

	public void setDescLivAutSpecEle(String descLivAutSpecEle) {
		this.descLivAutSpecEle = descLivAutSpecEle;
	}

	public String getDescTipoElaborazione() {
		return descTipoElaborazione;
	}

	public void setDescTipoElaborazione(String descTipoElaborazione) {
		this.descTipoElaborazione = descTipoElaborazione;
	}

	public String getDescFormaPubblicazioneDisco() {
		return descFormaPubblicazioneDisco;
	}

	public void setDescFormaPubblicazioneDisco(String descFormaPubblicazioneDisco) {
		this.descFormaPubblicazioneDisco = descFormaPubblicazioneDisco;
	}

	public String getDescVelocita() {
		return descVelocita;
	}

	public void setDescVelocita(String descVelocita) {
		this.descVelocita = descVelocita;
	}

	public String getDescTipoSuono() {
		return descTipoSuono;
	}

	public void setDescTipoSuono(String descTipoSuono) {
		this.descTipoSuono = descTipoSuono;
	}

	public String getDescLarghezzaScanal() {
		return descLarghezzaScanal;
	}

	public void setDescLarghezzaScanal(String descLarghezzaScanal) {
		this.descLarghezzaScanal = descLarghezzaScanal;
	}

	public String getDescDimensioni() {
		return descDimensioni;
	}

	public void setDescDimensioni(String descDimensioni) {
		this.descDimensioni = descDimensioni;
	}

	public String getDescLarghezzaNastro() {
		return descLarghezzaNastro;
	}

	public void setDescLarghezzaNastro(String descLarghezzaNastro) {
		this.descLarghezzaNastro = descLarghezzaNastro;
	}

	public String getDescConfigurazNastro() {
		return descConfigurazNastro;
	}

	public void setDescConfigurazNastro(String descConfigurazNastro) {
		this.descConfigurazNastro = descConfigurazNastro;
	}

	public String getDescMaterTestAccompagn1() {
		return descMaterTestAccompagn1;
	}

	public void setDescMaterTestAccompagn1(String descMaterTestAccompagn1) {
		this.descMaterTestAccompagn1 = descMaterTestAccompagn1;
	}

	public String getDescMaterTestAccompagn2() {
		return descMaterTestAccompagn2;
	}

	public void setDescMaterTestAccompagn2(String descMaterTestAccompagn2) {
		this.descMaterTestAccompagn2 = descMaterTestAccompagn2;
	}

	public String getDescMaterTestAccompagn3() {
		return descMaterTestAccompagn3;
	}

	public void setDescMaterTestAccompagn3(String descMaterTestAccompagn3) {
		this.descMaterTestAccompagn3 = descMaterTestAccompagn3;
	}

	public String getDescMaterTestAccompagn4() {
		return descMaterTestAccompagn4;
	}

	public void setDescMaterTestAccompagn4(String descMaterTestAccompagn4) {
		this.descMaterTestAccompagn4 = descMaterTestAccompagn4;
	}

	public String getDescMaterTestAccompagn5() {
		return descMaterTestAccompagn5;
	}

	public void setDescMaterTestAccompagn5(String descMaterTestAccompagn5) {
		this.descMaterTestAccompagn5 = descMaterTestAccompagn5;
	}

	public String getDescMaterTestAccompagn6() {
		return descMaterTestAccompagn6;
	}

	public void setDescMaterTestAccompagn6(String descMaterTestAccompagn6) {
		this.descMaterTestAccompagn6 = descMaterTestAccompagn6;
	}

	public String getDescTecnicaRegistraz() {
		return descTecnicaRegistraz;
	}

	public void setDescTecnicaRegistraz(String descTecnicaRegistraz) {
		this.descTecnicaRegistraz = descTecnicaRegistraz;
	}

	public String getDescSpecCarattRiprod() {
		return descSpecCarattRiprod;
	}

	public void setDescSpecCarattRiprod(String descSpecCarattRiprod) {
		this.descSpecCarattRiprod = descSpecCarattRiprod;
	}

	public String getDescDatiCodifRegistrazSonore() {
		return descDatiCodifRegistrazSonore;
	}

	public void setDescDatiCodifRegistrazSonore(String descDatiCodifRegistrazSonore) {
		this.descDatiCodifRegistrazSonore = descDatiCodifRegistrazSonore;
	}

	public String getDescTipoDiMateriale() {
		return descTipoDiMateriale;
	}

	public void setDescTipoDiMateriale(String descTipoDiMateriale) {
		this.descTipoDiMateriale = descTipoDiMateriale;
	}

	public String getDescTipoDiTaglio() {
		return descTipoDiTaglio;
	}

	public void setDescTipoDiTaglio(String descTipoDiTaglio) {
		this.descTipoDiTaglio = descTipoDiTaglio;
	}

	public String getDescDurataRegistraz() {
		return descDurataRegistraz;
	}

	public void setDescDurataRegistraz(String descDurataRegistraz) {
		this.descDurataRegistraz = descDurataRegistraz;
	}

	public List getListaFormaPubblicazioneDisco() {
		return listaFormaPubblicazioneDisco;
	}

	public void setListaFormaPubblicazioneDisco(
			List listaFormaPubblicazioneDisco) {
		this.listaFormaPubblicazioneDisco = listaFormaPubblicazioneDisco;
	}

	public List getListaTipoRisorsaElettronica() {
		return listaTipoRisorsaElettronica;
	}

	public void setListaTipoRisorsaElettronica(List listaTipoRisorsaElettronica) {
		this.listaTipoRisorsaElettronica = listaTipoRisorsaElettronica;
	}

	public List getListaIndicazioneSpecificaMateriale() {
		return listaIndicazioneSpecificaMateriale;
	}

	public void setListaIndicazioneSpecificaMateriale(
			List listaIndicazioneSpecificaMateriale) {
		this.listaIndicazioneSpecificaMateriale = listaIndicazioneSpecificaMateriale;
	}

	public List getListaColoreElettronico() {
		return listaColoreElettronico;
	}

	public void setListaColoreElettronico(List listaColoreElettronico) {
		this.listaColoreElettronico = listaColoreElettronico;
	}

	public List getListaDimensioniElettronico() {
		return listaDimensioniElettronico;
	}

	public void setListaDimensioniElettronico(List listaDimensioniElettronico) {
		this.listaDimensioniElettronico = listaDimensioniElettronico;
	}

	public List getListaSuonoElettronico() {
		return listaSuonoElettronico;
	}

	public void setListaSuonoElettronico(List listaSuonoElettronico) {
		this.listaSuonoElettronico = listaSuonoElettronico;
	}

	public List getListaVelocita() {
		return listaVelocita;
	}

	public void setListaVelocita(List listaVelocita) {
		this.listaVelocita = listaVelocita;
	}

	public List getListaTipoSuono() {
		return listaTipoSuono;
	}

	public void setListaTipoSuono(List listaTipoSuono) {
		this.listaTipoSuono = listaTipoSuono;
	}

	public List getListaLarghezzaScanal() {
		return listaLarghezzaScanal;
	}

	public void setListaLarghezzaScanal(List listaLarghezzaScanal) {
		this.listaLarghezzaScanal = listaLarghezzaScanal;
	}

	public List getListaDimensioni() {
		return listaDimensioni;
	}

	public void setListaDimensioni(List listaDimensioni) {
		this.listaDimensioni = listaDimensioni;
	}

	public List getListaLarghezzaNastro() {
		return listaLarghezzaNastro;
	}

	public void setListaLarghezzaNastro(List listaLarghezzaNastro) {
		this.listaLarghezzaNastro = listaLarghezzaNastro;
	}

	public List getListaConfigurazNastro() {
		return listaConfigurazNastro;
	}

	public void setListaConfigurazNastro(List listaConfigurazNastro) {
		this.listaConfigurazNastro = listaConfigurazNastro;
	}

	public List getListaMaterTestAccompagn() {
		return listaMaterTestAccompagn;
	}

	public void setListaMaterTestAccompagn(List listaMaterTestAccompagn) {
		this.listaMaterTestAccompagn = listaMaterTestAccompagn;
	}

	public List getListaTecnicaRegistraz() {
		return listaTecnicaRegistraz;
	}

	public void setListaTecnicaRegistraz(List listaTecnicaRegistraz) {
		this.listaTecnicaRegistraz = listaTecnicaRegistraz;
	}

	public List getListaSpecCarattRiprod() {
		return listaSpecCarattRiprod;
	}

	public void setListaSpecCarattRiprod(List listaSpecCarattRiprod) {
		this.listaSpecCarattRiprod = listaSpecCarattRiprod;
	}

	public List getListaDatiCodifRegistrazSonore() {
		return listaDatiCodifRegistrazSonore;
	}

	public void setListaDatiCodifRegistrazSonore(
			List listaDatiCodifRegistrazSonore) {
		this.listaDatiCodifRegistrazSonore = listaDatiCodifRegistrazSonore;
	}

	public List getListaTipoDiMateriale() {
		return listaTipoDiMateriale;
	}

	public void setListaTipoDiMateriale(List listaTipoDiMateriale) {
		this.listaTipoDiMateriale = listaTipoDiMateriale;
	}

	public List getListaTipoDiTaglio() {
		return listaTipoDiTaglio;
	}

	public void setListaTipoDiTaglio(List listaTipoDiTaglio) {
		this.listaTipoDiTaglio = listaTipoDiTaglio;
	}

	public boolean isFlagAbilitazSpecSoloMus() {
		return flagAbilitazSpecSoloMus;
	}

	public void setFlagAbilitazSpecSoloMus(boolean flagAbilitazSpecSoloMus) {
		this.flagAbilitazSpecSoloMus = flagAbilitazSpecSoloMus;
	}

	public String getTipoProspetSpecSoloMus() {
		return tipoProspetSpecSoloMus;
	}

	public void setTipoProspetSpecSoloMus(String tipoProspetSpecSoloMus) {
		this.tipoProspetSpecSoloMus = tipoProspetSpecSoloMus;
	}

	public String getPresenzaArea0BIS() {
		return presenzaArea0BIS;
	}

	public void setPresenzaArea0BIS(String presenzaArea0BIS) {
		this.presenzaArea0BIS = presenzaArea0BIS;
	}

	public String getDescTipoTestoRegSonora() {
		return descTipoTestoRegSonora;
	}

	public void setDescTipoTestoRegSonora(String descTipoTestoRegSonora) {
		this.descTipoTestoRegSonora = descTipoTestoRegSonora;
	}

	public List getListaTipoTestoRegSonora() {
		return listaTipoTestoRegSonora;
	}

	public void setListaTipoTestoRegSonora(List listaTipoTestoRegSonora) {
		this.listaTipoTestoRegSonora = listaTipoTestoRegSonora;
	}

	public String getNumSupporti() {
		return numSupporti;
	}

	public void setNumSupporti(String numSupporti) {
		this.numSupporti = numSupporti;
	}

	public List getListaTipoSupporto() {
		return listaTipoSupporto;
	}

	public void setListaTipoSupporto(List listaTipoSupporto) {
		this.listaTipoSupporto = listaTipoSupporto;
	}

	public String getDescTipoSupporto() {
		return descTipoSupporto;
	}

	public void setDescTipoSupporto(String descTipoSupporto) {
		this.descTipoSupporto = descTipoSupporto;
	}

	public String getDescTipoSupportoBIS() {
		return descTipoSupportoBIS;
	}

	public void setDescTipoSupportoBIS(String descTipoSupportoBIS) {
		this.descTipoSupportoBIS = descTipoSupportoBIS;
	}

	public List getListaLinkEsterni() {
		return listaLinkEsterni;
	}

	public void setListaLinkEsterni(List listaLinkEsterni) {
		this.listaLinkEsterni = listaLinkEsterni;
	}

	public String getDescLinkEsterni() {
		return descLinkEsterni;
	}

	public void setDescLinkEsterni(String descLinkEsterni) {
		this.descLinkEsterni = descLinkEsterni;
	}

	public String getSelezRadioLinkEsterni() {
		return selezRadioLinkEsterni;
	}

	public void setSelezRadioLinkEsterni(String selezRadioLinkEsterni) {
		this.selezRadioLinkEsterni = selezRadioLinkEsterni;
	}

	public String getSelezRadioReperCartaceo() {
		return selezRadioReperCartaceo;
	}

	public void setSelezRadioReperCartaceo(String selezRadioReperCartaceo) {
		this.selezRadioReperCartaceo = selezRadioReperCartaceo;
	}

	public String getLegameTitUniRinvio() {
		return legameTitUniRinvio;
	}

	public void setLegameTitUniRinvio(String legameTitUniRinvio) {
		this.legameTitUniRinvio = legameTitUniRinvio;
	}

	public List getListaFormaOpera231() {
		return listaFormaOpera231;
	}

	public void setListaFormaOpera231(List listaFormaOpera231) {
		this.listaFormaOpera231 = listaFormaOpera231;
	}

	public String getDescFormaOpera231() {
		return descFormaOpera231;
	}

	public void setDescFormaOpera231(String descFormaOpera231) {
		this.descFormaOpera231 = descFormaOpera231;
	}

	public List getListaProiezioneCarte() {
		return listaProiezioneCarte;
	}

	public void setListaProiezioneCarte(List listaProiezioneCarte) {
		this.listaProiezioneCarte = listaProiezioneCarte;
	}

	public String getDescProiezioneCarte() {
		return descProiezioneCarte;
	}

	public void setDescProiezioneCarte(String descProiezioneCarte) {
		this.descProiezioneCarte = descProiezioneCarte;
	}

	public List getListaPubblicatoSiNo() {
		return listaPubblicatoSiNo;
	}

	public void setListaPubblicatoSiNo(List listaPubblicatoSiNo) {
		this.listaPubblicatoSiNo = listaPubblicatoSiNo;
	}

	public String getDescPubblicatoSiNo() {
		return descPubblicatoSiNo;
	}

	public void setDescPubblicatoSiNo(String descPubblicatoSiNo) {
		this.descPubblicatoSiNo = descPubblicatoSiNo;
	}

	public String getDescTipoRisorsaElettronica() {
		return descTipoRisorsaElettronica;
	}

	public void setDescTipoRisorsaElettronica(String descTipoRisorsaElettronica) {
		this.descTipoRisorsaElettronica = descTipoRisorsaElettronica;
	}

	public String getDescIndicazioneSpecificaMateriale() {
		return descIndicazioneSpecificaMateriale;
	}

	public void setDescIndicazioneSpecificaMateriale(
			String descIndicazioneSpecificaMateriale) {
		this.descIndicazioneSpecificaMateriale = descIndicazioneSpecificaMateriale;
	}

	public String getDescColoreElettronico() {
		return descColoreElettronico;
	}

	public void setDescColoreElettronico(String descColoreElettronico) {
		this.descColoreElettronico = descColoreElettronico;
	}

	public String getDescDimensioniElettronico() {
		return descDimensioniElettronico;
	}

	public void setDescDimensioniElettronico(String descDimensioniElettronico) {
		this.descDimensioniElettronico = descDimensioniElettronico;
	}

	public String getDescSuonoElettronico() {
		return descSuonoElettronico;
	}

	public void setDescSuonoElettronico(String descSuonoElettronico) {
		this.descSuonoElettronico = descSuonoElettronico;
	}



}
