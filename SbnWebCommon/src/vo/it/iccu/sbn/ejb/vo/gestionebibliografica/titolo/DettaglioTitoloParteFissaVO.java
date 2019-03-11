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

package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;

public class DettaglioTitoloParteFissaVO extends BaseVO {

	// = DettaglioTitoloParteFissaVO.class.hashCode();

	// Dati relativi alla ricerca marca - Canali principali

	/**
	 *
	 */
	private static final long serialVersionUID = 2920732200163626577L;
	private String natura;
	private String tipoMat;
	private String bid;
	private String isadn;

	private String livAut;
	private String tipoRec;

	private String livCatal;
	private String fonteRec;

	private String paese;
	private String lingua;
	private String lingua1;
	private String lingua2;
	private String lingua3;
	private String genere1;
	private String genere2;
	private String genere3;
	private String genere4;
	private String tipoData;
	private String dataPubbl1;
	private String dataPubbl2;

//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
//	il tipoTestoLetterario è valido solo per tipo rec a/b
	private String tipoTestoLetterario;


	// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
	// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
	// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
	private String tipoTestoRegSonora;

	// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
	private String formaContenuto;
	private String tipoContenuto;
	private String movimento;
	private String dimensione;
	private String sensorialita1;
	private String sensorialita2;
	private String sensorialita3;
	private String tipoMediazione;
	private String tipoSupporto;

	private String formaContenutoBIS;
	private String tipoContenutoBIS;
	private String movimentoBIS;
	private String dimensioneBIS;
	private String sensorialitaBIS1;
	private String sensorialitaBIS2;
	private String sensorialitaBIS3;
	private String tipoMediazioneBIS;
	private String tipoSupportoBIS;
//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Fine definizione/Gestione Nuovi campi

	// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
	// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
	private String pubblicatoSiNo;

	private String areaTitTitolo;
	private String areaTitEdiz;
	private String areaTitDatiMatem;
	private String areaTitNumer;
	private String areaTitPubbl;
	private String areaTitDescrFis;
	private String areaTitNote;

//	GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
	private String areaTitNote323;
	private String areaTitNote327;
	private String areaTitNote330;
	private String areaTitNote336;
	private String areaTitNote337;
	private String areaTitNoteDATA;
	private String areaTitNoteORIG;
	private String areaTitNoteFILI;
	private String areaTitNotePROV;
	private String areaTitNotePOSS;
//	GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274


	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// al link dei documenti su Basi Esterne - Link verso base date digital
	private String presenzaLinkEsterni;
	private List<TabellaNumSTDImpronteVO> listaLinkEsterni;

	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo
	private String presenzaReperCartaceo;
	private List<TabellaNumSTDImpronteVO> listaReperCartaceo;

	private String areaTitMusica;

	private String presenzaImpron;
	private String presenzaNumSt;
	private String presenzaRepertori;
	private List<TabellaNumSTDImpronteVO> listaNumStandard;
	private List<TabellaNumSTDImpronteVO> listaImpronte;
	private List <TabellaNumSTDImpronteOriginarioVO>listaRepertoriOld;
	private List <TabellaNumSTDImpronteAggiornataVO>listaRepertoriNew;

	private String numStanNumero;
	private String numStanTipo;
	private String numStanNota;

	private String dataAgg;
	private String versione;
	private String dataIns;
	private String norme;
	private String fonteAgenzia;
	private String fontePaese;

	private String noteInformative;
	private String noteCatalogatore;

	private String uriDigitalBorn;

	private String ocn;


	// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
    // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
	private String formaOpera231;
	private String dataOpera231;
	private String altreCarat231;

	public DettaglioTitoloParteFissaVO() {
		super();
		this.listaNumStandard = new ArrayList<TabellaNumSTDImpronteVO>();
		this.listaImpronte = new ArrayList<TabellaNumSTDImpronteVO>();
		this.listaLinkEsterni = new ArrayList<TabellaNumSTDImpronteVO>();
		this.listaReperCartaceo = new ArrayList<TabellaNumSTDImpronteVO>();
	}

	public String getAreaTitDescrFis() {
		return areaTitDescrFis;
	}

	public void setAreaTitDescrFis(String areaTitDescrFis) {
		this.areaTitDescrFis = areaTitDescrFis;
	}

	public String getAreaTitEdiz() {
		return areaTitEdiz;
	}

	public void setAreaTitEdiz(String areaTitEdiz) {
		this.areaTitEdiz = areaTitEdiz;
	}

	public String getAreaTitNote() {
		return areaTitNote;
	}

	public void setAreaTitNote(String areaTitNote) {
		this.areaTitNote = areaTitNote;
	}

	public String getAreaTitNumer() {
		return areaTitNumer;
	}

	public void setAreaTitNumer(String areaTitNumer) {
		this.areaTitNumer = areaTitNumer;
	}

	public String getAreaTitPubbl() {
		return areaTitPubbl;
	}

	public void setAreaTitPubbl(String areaTitPubbl) {
		this.areaTitPubbl = areaTitPubbl;
	}

	public String getAreaTitTitolo() {
		return areaTitTitolo;
	}

	public void setAreaTitTitolo(String areaTitTitolo) {
		this.areaTitTitolo = areaTitTitolo;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDataPubbl1() {
		return dataPubbl1;
	}

	public void setDataPubbl1(String dataPubbl1) {
		this.dataPubbl1 = dataPubbl1;
	}

	public String getDataPubbl2() {
		return dataPubbl2;
	}

	public void setDataPubbl2(String dataPubbl2) {
		this.dataPubbl2 = dataPubbl2;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getNumStanNota() {
		return numStanNota;
	}

	public void setNumStanNota(String numStanNota) {
		this.numStanNota = numStanNota;
	}

	public String getNumStanNumero() {
		return numStanNumero;
	}

	public void setNumStanNumero(String numStanNumero) {
		this.numStanNumero = numStanNumero;
	}

	public String getNumStanTipo() {
		return numStanTipo;
	}

	public void setNumStanTipo(String numStanTipo) {
		this.numStanTipo = numStanTipo;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getTipoData() {
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public String getTipoMat() {
		return tipoMat;
	}

	public void setTipoMat(String tipoMat) {
		this.tipoMat = tipoMat;
	}

	public String getTipoRec() {
		return tipoRec;
	}

	public void setTipoRec(String tipoRec) {
		this.tipoRec = tipoRec;
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

	public String getGenere1() {
		return genere1;
	}

	public void setGenere1(String genere1) {
		this.genere1 = genere1;
	}

	public String getGenere2() {
		return genere2;
	}

	public void setGenere2(String genere2) {
		this.genere2 = genere2;
	}

	public String getGenere3() {
		return genere3;
	}

	public void setGenere3(String genere3) {
		this.genere3 = genere3;
	}

	public String getGenere4() {
		return genere4;
	}

	public void setGenere4(String genere4) {
		this.genere4 = genere4;
	}

	public String getFonteRec() {
		return fonteRec;
	}

	public void setFonteRec(String fonteRec) {
		this.fonteRec = fonteRec;
	}

	public String getLivCatal() {
		return livCatal;
	}

	public void setLivCatal(String livCatal) {
		this.livCatal = livCatal;
	}

	public String getNoteCatalogatore() {
		return noteCatalogatore;
	}

	public void setNoteCatalogatore(String noteCatalogatore) {
		this.noteCatalogatore = noteCatalogatore;
	}

	public String getNoteInformative() {
		return noteInformative;
	}

	public void setNoteInformative(String noteInformative) {
		this.noteInformative = noteInformative;
	}

	public String getIsadn() {
		return isadn;
	}

	public void setIsadn(String isadn) {
		this.isadn = isadn;
	}

	public List getListaImpronte() {
		return listaImpronte;
	}

	public void setListaImpronte(List listaImpronte) {
		this.listaImpronte = listaImpronte;
	}

	public List addListaImpronte(TabellaNumSTDImpronteVO tabImp) {
		listaImpronte.add(tabImp);
		return listaImpronte;
	}

	public List getListaNumStandard() {
		return listaNumStandard;
	}

	public void setListaNumStandard(List listaNumStandard) {
		this.listaNumStandard = listaNumStandard;
	}

	public List addListaNumStandard(TabellaNumSTDImpronteVO tabNSt) {
		listaNumStandard.add(tabNSt);
		return listaNumStandard;
	}

	public String getPresenzaImpron() {
		return presenzaImpron;
	}

	public void setPresenzaImpron(String presenzaImpron) {
		this.presenzaImpron = presenzaImpron;
	}

	public String getPresenzaNumSt() {
		return presenzaNumSt;
	}

	public void setPresenzaNumSt(String presenzaNumSt) {
		this.presenzaNumSt = presenzaNumSt;
	}

	public String getAreaTitDatiMatem() {
		return areaTitDatiMatem;
	}

	public void setAreaTitDatiMatem(String areaTitDatiMatem) {
		this.areaTitDatiMatem = areaTitDatiMatem;
	}

	public String getAreaTitMusica() {
		return areaTitMusica;
	}

	public void setAreaTitMusica(String areaTitMusica) {
		this.areaTitMusica = areaTitMusica;
	}

	public String getVersione() {
		return versione;
	}

	public void setVersione(String versione) {
		this.versione = versione;
	}

	public String getNorme() {
		return norme;
	}

	public void setNorme(String norme) {
		this.norme = norme;
	}

	public List<TabellaNumSTDImpronteOriginarioVO> getListaRepertoriOld() {
		return listaRepertoriOld;
	}

	public void setListaRepertoriOld(List<TabellaNumSTDImpronteOriginarioVO> listaRepertoriOld) {
		this.listaRepertoriOld = listaRepertoriOld;
	}

	public List<TabellaNumSTDImpronteAggiornataVO> getListaRepertoriNew() {
		return listaRepertoriNew;
	}

	public void setListaRepertoriNew(
			List<TabellaNumSTDImpronteAggiornataVO> listaRepertoriNew) {
		this.listaRepertoriNew = listaRepertoriNew;
	}

	public String getPresenzaRepertori() {
		return presenzaRepertori;
	}

	public void setPresenzaRepertori(String presenzaRepertori) {
		this.presenzaRepertori = presenzaRepertori;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloParteFissaVO other = (DettaglioTitoloParteFissaVO) obj;

		if (!listEquals(this.listaImpronte, other.listaImpronte,
				TabellaNumSTDImpronteVO.class))
			return false;

		if (!listEquals(this.listaNumStandard, other.listaNumStandard,
				TabellaNumSTDImpronteVO.class))
			return false;

		if (areaTitDatiMatem == null) {
			if (other.areaTitDatiMatem != null)
				return false;
		} else if (!areaTitDatiMatem.equals(other.areaTitDatiMatem))
			return false;
		if (areaTitDescrFis == null) {
			if (other.areaTitDescrFis != null)
				return false;
		} else if (!areaTitDescrFis.equals(other.areaTitDescrFis))
			return false;
		if (areaTitEdiz == null) {
			if (other.areaTitEdiz != null)
				return false;
		} else if (!areaTitEdiz.equals(other.areaTitEdiz))
			return false;
		if (areaTitMusica == null) {
			if (other.areaTitMusica != null)
				return false;
		} else if (!areaTitMusica.equals(other.areaTitMusica))
			return false;

//		Ripristinata l'area delle note che è comunque presente; Inizio almaviva2 modifica 26 febbraio 2010 - Intervento interno
		if (areaTitNote == null) {
			if (other.areaTitNote != null)
				return false;
		} else if (!areaTitNote.equals(other.areaTitNote))
			return false;


		if (areaTitNumer == null) {
			if (other.areaTitNumer != null)
				return false;
		} else if (!areaTitNumer.equals(other.areaTitNumer))
			return false;
		if (areaTitPubbl == null) {
			if (other.areaTitPubbl != null)
				return false;
		} else if (!areaTitPubbl.equals(other.areaTitPubbl))
			return false;
		if (areaTitTitolo == null) {
			if (other.areaTitTitolo != null)
				return false;
		} else if (!areaTitTitolo.equals(other.areaTitTitolo))
			return false;


	//  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
		if (tipoTestoLetterario == null) {
			if (other.tipoTestoLetterario != null)
				return false;
		} else if (!tipoTestoLetterario.equals(other.tipoTestoLetterario))
			return false;


		if (tipoTestoRegSonora == null) {
			if (other.tipoTestoRegSonora != null)
				return false;
		} else if (!tipoTestoRegSonora.equals(other.tipoTestoRegSonora))
			return false;


		if (formaContenuto == null) {
			if (other.formaContenuto != null)
				return false;
		} else if (!formaContenuto.equals(other.formaContenuto))
			return false;
		if (formaContenutoBIS == null) {
			if (other.formaContenutoBIS != null)
				return false;
		} else if (!formaContenutoBIS.equals(other.formaContenutoBIS))
			return false;

		if (tipoContenuto == null) {
			if (other.tipoContenuto != null)
				return false;
		} else if (!tipoContenuto.equals(other.tipoContenuto))
			return false;
		if (tipoContenutoBIS == null) {
			if (other.tipoContenutoBIS != null)
				return false;
		} else if (!tipoContenutoBIS.equals(other.tipoContenutoBIS))
			return false;

		if (movimento == null) {
			if (other.movimento != null)
				return false;
		} else if (!movimento.equals(other.movimento))
			return false;
		if (movimentoBIS == null) {
			if (other.movimentoBIS != null)
				return false;
		} else if (!movimentoBIS.equals(other.movimentoBIS))
			return false;

		if (dimensione == null) {
			if (other.dimensione != null)
				return false;
		} else if (!dimensione.equals(other.dimensione))
			return false;
		if (dimensioneBIS == null) {
			if (other.dimensioneBIS != null)
				return false;
		} else if (!dimensioneBIS.equals(other.dimensioneBIS))
			return false;

		if (sensorialita1 == null) {
			if (other.sensorialita1 != null)
				return false;
		} else if (!sensorialita1.equals(other.sensorialita1))
			return false;
		if (sensorialitaBIS1 == null) {
			if (other.sensorialitaBIS1 != null)
				return false;
		} else if (!sensorialitaBIS1.equals(other.sensorialitaBIS1))
			return false;
		if (sensorialita2 == null) {
			if (other.sensorialita2 != null)
				return false;
		} else if (!sensorialita2.equals(other.sensorialita2))
			return false;
		if (sensorialitaBIS2 == null) {
			if (other.sensorialitaBIS2 != null)
				return false;
		} else if (!sensorialitaBIS2.equals(other.sensorialitaBIS2))
			return false;
		if (sensorialita3 == null) {
			if (other.sensorialita3 != null)
				return false;
		} else if (!sensorialita3.equals(other.sensorialita3))
			return false;
		if (sensorialitaBIS3 == null) {
			if (other.sensorialitaBIS3 != null)
				return false;
		} else if (!sensorialitaBIS3.equals(other.sensorialitaBIS3))
			return false;

		if (tipoMediazione == null) {
			if (other.tipoMediazione != null)
				return false;
		} else if (!tipoMediazione.equals(other.tipoMediazione))
			return false;
		if (tipoMediazioneBIS == null) {
			if (other.tipoMediazioneBIS != null)
				return false;
		} else if (!tipoMediazioneBIS.equals(other.tipoMediazioneBIS))
			return false;

		if (pubblicatoSiNo == null) {
			if (other.pubblicatoSiNo != null)
				return false;
		} else if (!pubblicatoSiNo.equals(other.pubblicatoSiNo))
			return false;

		if (tipoSupporto == null) {
			if (other.tipoSupporto != null)
				return false;
		} else if (!tipoSupporto.equals(other.tipoSupporto))
			return false;
		if (tipoSupportoBIS == null) {
			if (other.tipoSupportoBIS != null)
				return false;
		} else if (!tipoSupportoBIS.equals(other.tipoSupportoBIS))
			return false;
	//  Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi





//		GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
		if (areaTitNote323 == null) {
			if (other.areaTitNote323 != null)
				return false;
		} else if (!areaTitNote323.equals(other.areaTitNote323))
			return false;

		if (areaTitNote327 == null) {
			if (other.areaTitNote327 != null)
				return false;
		} else if (!areaTitNote327.equals(other.areaTitNote327))
			return false;
		if (areaTitNote330 == null) {
			if (other.areaTitNote330 != null)
				return false;
		} else if (!areaTitNote330.equals(other.areaTitNote330))
			return false;
		if (areaTitNote336 == null) {
			if (other.areaTitNote336 != null)
				return false;
		} else if (!areaTitNote336.equals(other.areaTitNote336))
			return false;
		if (areaTitNote337 == null) {
			if (other.areaTitNote337 != null)
				return false;
		} else if (!areaTitNote337.equals(other.areaTitNote337))
			return false;
		if (areaTitNoteDATA == null) {
			if (other.areaTitNoteDATA != null)
				return false;
		} else if (!areaTitNoteDATA.equals(other.areaTitNoteDATA))
			return false;
		if (areaTitNoteFILI == null) {
			if (other.areaTitNoteFILI != null)
				return false;
		} else if (!areaTitNoteFILI.equals(other.areaTitNoteFILI))
			return false;
		if (areaTitNoteORIG == null) {
			if (other.areaTitNoteORIG != null)
				return false;
		} else if (!areaTitNoteORIG.equals(other.areaTitNoteORIG))
			return false;
		if (areaTitNotePOSS == null) {
			if (other.areaTitNotePOSS != null)
				return false;
		} else if (!areaTitNotePOSS.equals(other.areaTitNotePOSS))
			return false;
		if (areaTitNotePROV == null) {
			if (other.areaTitNotePROV != null)
				return false;
		} else if (!areaTitNotePROV.equals(other.areaTitNotePROV))
			return false;

		//		GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

		// Inizio almaviva2 25.05.2011 BUG 4413 (esercizio) si inserisce il controllo sulla variazione
		// del campo URIDigitalBorn per effettuare correttamente il controllo sul Variazioni effettuate;
		if (uriDigitalBorn == null) {
			if (other.uriDigitalBorn != null)
				return false;
		} else if (!uriDigitalBorn.equals(other.uriDigitalBorn))
			return false;
		// Fine almaviva2 25.05.2011 BUG 4413 (esercizio)


		// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// al link dei documenti su Basi Esterne - Link verso base date digitali
		if (!listEquals(this.listaLinkEsterni, other.listaLinkEsterni,
				TabellaNumSTDImpronteVO.class))
			return false;
		// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
		// ai repertori cartacei - Riferimento a repertorio cartaceo
		if (!listEquals(this.listaReperCartaceo, other.listaReperCartaceo,
				TabellaNumSTDImpronteVO.class))
			return false;


		if (bid == null) {
			if (other.bid != null)
				return false;
		} else if (!bid.equals(other.bid))
			return false;
		if (dataAgg == null) {
			if (other.dataAgg != null)
				return false;
		} else if (!dataAgg.equals(other.dataAgg))
			return false;
		if (dataIns == null) {
			if (other.dataIns != null)
				return false;
		} else if (!dataIns.equals(other.dataIns))
			return false;
		if (dataPubbl1 == null) {
			if (other.dataPubbl1 != null)
				return false;
		} else if (!dataPubbl1.equals(other.dataPubbl1))
			return false;
		if (dataPubbl2 == null) {
			if (other.dataPubbl2 != null)
				return false;
		} else if (!dataPubbl2.equals(other.dataPubbl2))
			return false;
		if (fonteRec == null) {
			if (other.fonteRec != null)
				return false;
		} else if (!fonteRec.equals(other.fonteRec))
			return false;
		if (genere1 == null) {
			if (other.genere1 != null)
				return false;
		} else if (!genere1.equals(other.genere1))
			return false;
		if (genere2 == null) {
			if (other.genere2 != null)
				return false;
		} else if (!genere2.equals(other.genere2))
			return false;
		if (genere3 == null) {
			if (other.genere3 != null)
				return false;
		} else if (!genere3.equals(other.genere3))
			return false;
		if (genere4 == null) {
			if (other.genere4 != null)
				return false;
		} else if (!genere4.equals(other.genere4))
			return false;
		if (isadn == null) {
			if (other.isadn != null)
				return false;
		} else if (!isadn.equals(other.isadn))
			return false;
		if (lingua == null) {
			if (other.lingua != null)
				return false;
		} else if (!lingua.equals(other.lingua))
			return false;
		if (lingua1 == null) {
			if (other.lingua1 != null)
				return false;
		} else if (!lingua1.equals(other.lingua1))
			return false;
		if (lingua2 == null) {
			if (other.lingua2 != null)
				return false;
		} else if (!lingua2.equals(other.lingua2))
			return false;
		if (lingua3 == null) {
			if (other.lingua3 != null)
				return false;
		} else if (!lingua3.equals(other.lingua3))
			return false;
		if (livAut == null) {
			if (other.livAut != null)
				return false;
		} else if (!livAut.equals(other.livAut))
			return false;
		if (livCatal == null) {
			if (other.livCatal != null)
				return false;
		} else if (!livCatal.equals(other.livCatal))
			return false;
		if (natura == null) {
			if (other.natura != null)
				return false;
		} else if (!natura.equals(other.natura))
			return false;
		if (norme == null) {
			if (other.norme != null)
				return false;
		} else if (!norme.equals(other.norme))
			return false;
		if (noteCatalogatore == null) {
			if (other.noteCatalogatore != null)
				return false;
		} else if (!noteCatalogatore.equals(other.noteCatalogatore))
			return false;
		if (noteInformative == null) {
			if (other.noteInformative != null)
				return false;
		} else if (!noteInformative.equals(other.noteInformative))
			return false;
		if (numStanNota == null) {
			if (other.numStanNota != null)
				return false;
		} else if (!numStanNota.equals(other.numStanNota))
			return false;
		if (numStanNumero == null) {
			if (other.numStanNumero != null)
				return false;
		} else if (!numStanNumero.equals(other.numStanNumero))
			return false;
		if (numStanTipo == null) {
			if (other.numStanTipo != null)
				return false;
		} else if (!numStanTipo.equals(other.numStanTipo))
			return false;
		if (paese == null) {
			if (other.paese != null)
				return false;
		} else if (!paese.equals(other.paese))
			return false;
		if (presenzaImpron == null) {
			if (other.presenzaImpron != null)
				return false;
		} else if (!presenzaImpron.equals(other.presenzaImpron))
			return false;
		if (presenzaNumSt == null) {
			if (other.presenzaNumSt != null)
				return false;
		} else if (!presenzaNumSt.equals(other.presenzaNumSt))
			return false;
		if (presenzaRepertori == null) {
			if (other.presenzaRepertori != null)
				return false;
		} else if (!presenzaRepertori.equals(other.presenzaRepertori))
			return false;
		if (tipoData == null) {
			if (other.tipoData != null)
				return false;
		} else if (!tipoData.equals(other.tipoData))
			return false;
		if (tipoMat == null) {
			if (other.tipoMat != null)
				return false;
		} else if (!tipoMat.equals(other.tipoMat))
			return false;
		if (tipoRec == null) {
			if (other.tipoRec != null)
				return false;
		} else if (!tipoRec.equals(other.tipoRec))
			return false;
		if (versione == null) {
			if (other.versione != null)
				return false;
		} else if (!versione.equals(other.versione))
			return false;

		//almaviva2 27.07.2017 adeguamento a Indice gestione 231
		if (formaOpera231 == null) {
			if (other.formaOpera231 != null)
				return false;
		} else if (!formaOpera231.equals(other.formaOpera231))
			return false;
		if (dataOpera231 == null) {
			if (other.dataOpera231 != null)
				return false;
		} else if (!dataOpera231.equals(other.dataOpera231))
			return false;
		if (altreCarat231 == null) {
			if (other.altreCarat231 != null)
				return false;
		} else if (!altreCarat231.equals(other.altreCarat231))
			return false;

		return true;
	}

	public String getFonteAgenzia() {
		return fonteAgenzia;
	}

	public void setFonteAgenzia(String fonteAgenzia) {
		this.fonteAgenzia = fonteAgenzia;
	}

	public String getFontePaese() {
		return fontePaese;
	}

	public void setFontePaese(String fontePaese) {
		this.fontePaese = fontePaese;
	}

	public String getUriDigitalBorn() {
		return uriDigitalBorn;
	}

	public void setUriDigitalBorn(String uriDigitalBorn) {
		this.uriDigitalBorn = uriDigitalBorn;
	}

	public String getAreaTitNote323() {
		return areaTitNote323;
	}

	public void setAreaTitNote323(String areaTitNote323) {
		this.areaTitNote323 = areaTitNote323;
	}

	public String getAreaTitNote327() {
		return areaTitNote327;
	}

	public void setAreaTitNote327(String areaTitNote327) {
		this.areaTitNote327 = areaTitNote327;
	}

	public String getAreaTitNote330() {
		return areaTitNote330;
	}

	public void setAreaTitNote330(String areaTitNote330) {
		this.areaTitNote330 = areaTitNote330;
	}

	public String getAreaTitNote336() {
		return areaTitNote336;
	}

	public void setAreaTitNote336(String areaTitNote336) {
		this.areaTitNote336 = areaTitNote336;
	}

	public String getAreaTitNote337() {
		return areaTitNote337;
	}

	public void setAreaTitNote337(String areaTitNote337) {
		this.areaTitNote337 = areaTitNote337;
	}

	public String getAreaTitNoteDATA() {
		return areaTitNoteDATA;
	}

	public void setAreaTitNoteDATA(String areaTitNoteDATA) {
		this.areaTitNoteDATA = areaTitNoteDATA;
	}

	public String getAreaTitNoteFILI() {
		return areaTitNoteFILI;
	}

	public void setAreaTitNoteFILI(String areaTitNoteFILI) {
		this.areaTitNoteFILI = areaTitNoteFILI;
	}

	public String getAreaTitNoteORIG() {
		return areaTitNoteORIG;
	}

	public void setAreaTitNoteORIG(String areaTitNoteORIG) {
		this.areaTitNoteORIG = areaTitNoteORIG;
	}

	public String getAreaTitNotePOSS() {
		return areaTitNotePOSS;
	}

	public void setAreaTitNotePOSS(String areaTitNotePOSS) {
		this.areaTitNotePOSS = areaTitNotePOSS;
	}

	public String getAreaTitNotePROV() {
		return areaTitNotePROV;
	}

	public void setAreaTitNotePROV(String areaTitNotePROV) {
		this.areaTitNotePROV = areaTitNotePROV;
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

	public String getPresenzaLinkEsterni() {
		return presenzaLinkEsterni;
	}

	public void setPresenzaLinkEsterni(String presenzaLinkEsterni) {
		this.presenzaLinkEsterni = presenzaLinkEsterni;
	}

	public List addListaLinkEsterni(TabellaNumSTDImpronteVO tabLinkEsterni) {
		listaLinkEsterni.add(tabLinkEsterni);
		return listaLinkEsterni;
	}

	public List<TabellaNumSTDImpronteVO> getListaLinkEsterni() {
		return listaLinkEsterni;
	}

	public void setListaLinkEsterni(
			List<TabellaNumSTDImpronteVO> listaLinkEsterni) {
		this.listaLinkEsterni = listaLinkEsterni;
	}

	public String getOcn() {
		return ocn;
	}

	public void setOcn(String ocn) {
		this.ocn = trimAndSet(ocn);
	}

	public String getPresenzaReperCartaceo() {
		return presenzaReperCartaceo;
	}

	public void setPresenzaReperCartaceo(String presenzaReperCartaceo) {
		this.presenzaReperCartaceo = presenzaReperCartaceo;
	}

	public List<TabellaNumSTDImpronteVO> getListaReperCartaceo() {
		return listaReperCartaceo;
	}

	public void setListaReperCartaceo(
			List<TabellaNumSTDImpronteVO> listaReperCartaceo) {
		this.listaReperCartaceo = listaReperCartaceo;
	}

	public List addListaReperCartaceo(TabellaNumSTDImpronteVO tabReperCartaceo) {
		listaReperCartaceo.add(tabReperCartaceo);
		return listaReperCartaceo;
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

}
