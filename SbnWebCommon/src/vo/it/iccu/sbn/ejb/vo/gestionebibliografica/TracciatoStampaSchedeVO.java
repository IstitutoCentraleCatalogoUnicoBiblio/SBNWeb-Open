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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescDatiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class TracciatoStampaSchedeVO extends SerializableVO {


	private static final long serialVersionUID = -5987008570730939145L;


	//Inizio Elenco campi per copertina della Stampa
	private String dataDiElaborazione;
	private String tipoMateriale;
	private String tipoMaterialeDescr;
	private String tipoCatalogoDesc;
	private String tipoOrdinamento;
	private String descrBib;

	private String codSezioneDa;
	private String codSezioneA;
	private String codCollocazioneDa;
	private String codSpecificazioneDa;
	private String codCollocazioneA;
	private String codSpecificazioneA;
	private String numTitoliTot;

	private String intestTitoloAdAutore;
    private String titoloCollana;
    private String titoliAnalitici;
    private String datiCollocazione;
	//FIne Elenco campi per copertina della Stampa

	private String bidEsaminato;
	private String naturaBidEsaminato;
	private String tipoRecBidEsaminato;
	private String linguaBidEsaminato;
	private String paeseBidEsaminato;
	private String oggettoCondiviso;


	// TOPOGRAFICO: prima riga, allineato a sinistra
	// trim(cod_sezione) + " " + trim(cod_loc) + " " + trim(spec_loc) + " " + trim(seq di invent);
	private String topografico;
	private List listaTopografico;

	private String intestazionePerCatalogo;

	// INTESTAZIONE: seconda riga, allineato a destra  puo esser composta dai campi sotto esposti
	// cognome autore
	private String intestPrimaria;
	private String intestPrimariaFacolt1;
	private String intestPrimariaFacolt2;

	private String vidAutoreRespons1;
	private String autoreRespons1;
	private List listaAutoriRespons2;
	private List listaAutoriRespons3;
	private List listaAutoriRespons4;
	private String titoloUniforme;

	// titolo natura A se presente nei legami del bid richiesto
	//private String isbdNaturaAlegata;

	// soggetto se presente nei legami del bid richiesto
	private String soggettoLegato;

	// classe se presente nei legami del bid richiesto
	private String classeLegata;


	// ISBD: terza riga, allineato a destra
	// area1: titolo/responsabilità
	// area2: edizione
	// area3: area specifica del materiale
	// area4: pubblicazione
	// area5: collazione
	// area6: isbd collana se presente nei legami del bid richiesto
	// area6Bis: numero sequenza collana (vedi sopra)
	// area7: area delle note (ed eventualmente isbd del titolo di natura B se legato)
	// area8: numero Standard (issn o isbn)
	private String isbdArea1;
	private String isbdArea2;
	private String isbdArea3;
	private String isbdArea4;
	private String isbdArea5;
	private String isbdArea6;
	private String isbdArea6Bis;
	private String isbdArea6conBid;
	private String isbdArea7;
	private String isbdArea7conBid;
	private String isbdArea8;

	private List listaLegami51;

	private String isbdArea1Madre;
	private String isbdArea1MadreconBid;

	private List listaSoggetti;
	private List listaClassi;
	private List listaNatureT;
	private List listaNatureD;

	// TRACCIATO: Stringa fissa che deve contenere la parola "tracciato" quarta riga, allineato a destra
	// Elenco dei soggetti legati al bid quinta riga, allineato a destra (preceduti da Numeri Arabi tutti di seguito
	private String elencoSoggetti;

	// Elenco delle intestazioni secondarie (responsabilità 2 e/o 3)e altri titoli accesso (natura D, P in assenza di autore principale)
	// legati al bid sesta riga, allineato a destra (preceduti da Numeri Romani tutti di seguito
	private String elencoIntestSecondarie;

	// Numero di inventario ottava riga, allineato a destra
	private String numInventario;
	private List listaNumInventario;

	//
	// Campi e settaggi per la chiamata a Jasper
	private String id;
	private String bid;
	private String collocazione;
	private String titcat;
	private String isbdsegue;
	private String rows;
	public Collection<TracciatoStampaSchedeSubReportVO> body;

	public String getBid() {
		return bid;
	}
	public Collection<TracciatoStampaSchedeSubReportVO> getBody() {
		return body;
	}

	public String getCollocazione() {
		return collocazione;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getIsbdsegue() {
		return isbdsegue;
	}
	public String getRows() {
		return rows;
	}

	public String getTitcat() {
		return titcat;
	}

	public TracciatoStampaSchedeVO(String dataElab) {
		this(dataElab, null);
	}

	public TracciatoStampaSchedeVO(String dataElab, String descrBib) {
		super();
		// TODO Auto-generated constructor stub
		this.dataDiElaborazione = dataElab;
		this.descrBib = ValidazioneDati.coalesce(descrBib, "Tutte le biblioteche");

		this.bidEsaminato = "";
		this.oggettoCondiviso = "";
		this.naturaBidEsaminato = "";
		this.tipoRecBidEsaminato = "";
		this.linguaBidEsaminato = "";
		this.paeseBidEsaminato = "";
		this.topografico = "";
		this.vidAutoreRespons1 = "";
		this.autoreRespons1 = "";
		this.soggettoLegato = "";
		this.classeLegata = "";
		this.isbdArea1 = "";
		this.isbdArea2 = "";
		this.isbdArea3 = "";
		this.isbdArea4 = "";
		this.isbdArea5 = "";
		this.isbdArea6 = "";
		this.isbdArea6Bis = "";
		this.isbdArea6conBid = "";
		this.isbdArea7 = "";
		this.isbdArea7conBid = "";
		this.isbdArea8 = "";
		this.elencoSoggetti = "";
		this.elencoIntestSecondarie = "";
		this.numInventario = "";
		this.isbdArea1Madre = "";
		this.isbdArea1MadreconBid = "";
		listaClassi = new ArrayList();
		listaSoggetti = new ArrayList();
		listaLegami51 = new ArrayList();
		listaNatureT = new ArrayList();
		listaNatureD = new ArrayList();
		listaAutoriRespons2 = new ArrayList();
		listaAutoriRespons3 = new ArrayList();
		listaAutoriRespons4 = new ArrayList();
		listaTopografico = new ArrayList();
		listaNumInventario = new ArrayList();
	}

	public String getClasseLegata() {
		return classeLegata;
	}

	public void setClasseLegata(String classeLegata) {
		this.classeLegata = classeLegata;
	}


	public String getTopografico() {
		return topografico;
	}


	public void setTopografico(String topografico) {
		this.topografico = topografico;
	}


	public List getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List addListaClassi(String newRiga) {
		listaClassi.add(newRiga);
		return listaClassi;
	}


	public String getElencoIntestSecondarie() {
		return elencoIntestSecondarie;
	}

	public void setElencoIntestSecondarie(String elencoIntestSecondarie) {
		this.elencoIntestSecondarie = elencoIntestSecondarie;
	}

	public String getElencoSoggetti() {
		return elencoSoggetti;
	}

	public void setElencoSoggetti(String elencoSoggetti) {
		this.elencoSoggetti = elencoSoggetti;
	}

	public String getIsbdArea1() {
		return isbdArea1;
	}

	public void setIsbdArea1(String isbdArea1) {
		this.isbdArea1 = isbdArea1;
	}

	public String getIsbdArea2() {
		return isbdArea2;
	}

	public void setIsbdArea2(String isbdArea2) {
		this.isbdArea2 = isbdArea2;
	}

	public String getIsbdArea3() {
		return isbdArea3;
	}

	public void setIsbdArea3(String isbdArea3) {
		this.isbdArea3 = isbdArea3;
	}

	public String getIsbdArea4() {
		return isbdArea4;
	}

	public void setIsbdArea4(String isbdArea4) {
		this.isbdArea4 = isbdArea4;
	}

	public String getIsbdArea5() {
		return isbdArea5;
	}

	public void setIsbdArea5(String isbdArea5) {
		this.isbdArea5 = isbdArea5;
	}

	public String getIsbdArea6() {
		return isbdArea6;
	}

	public void setIsbdArea6(String isbdArea6) {
		this.isbdArea6 = isbdArea6;
	}

	public String getIsbdArea6Bis() {
		return isbdArea6Bis;
	}

	public void setIsbdArea6Bis(String isbdArea6Bis) {
		this.isbdArea6Bis = isbdArea6Bis;
	}

	public String getIsbdArea7() {
		return isbdArea7;
	}

	public void setIsbdArea7(String isbdArea7) {
		this.isbdArea7 = isbdArea7;
	}

	public String getIsbdArea8() {
		return isbdArea8;
	}

	public void setIsbdArea8(String isbdArea8) {
		this.isbdArea8 = isbdArea8;
	}


	public String getNumInventario() {
		return numInventario;
	}

	public void setNumInventario(String numInventario) {
		this.numInventario = numInventario;
	}

	public String getSoggettoLegato() {
		return soggettoLegato;
	}

	public void setSoggettoLegato(String soggettoLegato) {
		this.soggettoLegato = soggettoLegato;
	}

	public String getBidEsaminato() {
		return bidEsaminato;
	}

	public void setBidEsaminato(String bidEsaminato) {
		this.bidEsaminato = bidEsaminato;
	}

	public List getListaLegami51() {
		return listaLegami51;
	}

	public void setListaLegami51(List listaLegami51) {
		this.listaLegami51 = listaLegami51;
	}

	public List addListaLegami51(ComboCodDescDatiVO newRiga) {
		listaLegami51.add(newRiga);
		return listaLegami51;
	}


	public String getAutoreRespons1() {
		return autoreRespons1;
	}

	public void setAutoreRespons1(String autoreRespons1) {
		this.autoreRespons1 = autoreRespons1;
	}

	public String getStringaToPrintSchede() {

		this.bid = this.getBidEsaminato();
		this.collocazione = this.getTopografico();


		if (this.intestazionePerCatalogo != null) {
			this.titcat = this.intestazionePerCatalogo;
		}

		// da qui inizia la scheda generale che si ripresenta sempre a prescindare dal catalogo richiesto
		body = new ArrayList<TracciatoStampaSchedeSubReportVO>();
		TracciatoStampaSchedeSubReportVO subReportVO;

		if (this.intestazionePerCatalogo != null ){
			// Inizio modifica almaviva2 2010.11.03 Bug Mantis 3889
			// Se intestazionePerCatalogo è uguale a intestPrimaria rimane così comè altrimenti
			// si devono stamapre tutte e due quindi la prima riga del body dovrà essere quella dell'intestPrimaria
			if (!this.intestazionePerCatalogo.equals(this.intestPrimaria)) {
				subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestazionePerCatalogo);
				body.add(subReportVO);
			}
			// Fine modifica almaviva2 2010.11.03 Bug Mantis 3889
		}

		subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimaria);
		body.add(subReportVO);

		if (this.getIntestPrimariaFacolt1() != null) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimariaFacolt1);
			body.add(subReportVO);

			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimariaFacolt2);
			body.add(subReportVO);
		}

		String isbdAppo = "";
		String delimitatoreAree = ". - ";

		if (!this.getIsbdArea1().equals("")) {
			isbdAppo = isbdAppo + this.getIsbdArea1();
		}
		if (!this.getIsbdArea2().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea2;
		}
		if (!this.getIsbdArea3().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea3;
		}
		if (!this.getIsbdArea4().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea4;
		}

		if (!this.getIsbdArea5().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea5;
		}

		// 6.11.2009 almaviva2 BUG 3307 Punto 3 L'area della collezione è racchiusa tra parentesi tonde, ma è preceduta da ". - "
		if (!this.getIsbdArea6().equals("")) {
//			isbdAppo = isbdAppo + this.getIsbdArea6();
//			 20.11.2009 almaviva2 BUG 3307 quando l'ultimo elemento di un'area è un punto (. ),
			// non si aggiunge punto, spazio, lineetta, spazio (. - ), ma solo spazio lineetta spazio
			if (isbdAppo.endsWith(".")) {
				isbdAppo = isbdAppo + " - " + this.getIsbdArea6();
			} else {
				isbdAppo = isbdAppo + delimitatoreAree + this.getIsbdArea6();
			}

		}

		subReportVO = new TracciatoStampaSchedeSubReportVO(isbdAppo);
		body.add(subReportVO);



		isbdAppo = "";
		if (!this.getIsbdArea7().trim().equals("")) {
			isbdAppo = this.isbdArea7;
		}

//		 6.11.2009 almaviva2 BUG 3307 Punto 1 Ogni volta che c'è un a capo per una nuova area (es. ISBN), l'area non è preceduta dalla punteggiatura ". - "
		if (!this.getIsbdArea8().equals("")) {
			if (isbdAppo.equals("")) {
				isbdAppo = this.isbdArea8;
			} else {
				if (isbdAppo.endsWith(". ")) {
					isbdAppo = isbdAppo + "- " + this.isbdArea8;
				} else {
					isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea8;
				}
			}
		}

		subReportVO = new TracciatoStampaSchedeSubReportVO(isbdAppo);
		body.add(subReportVO);



		// Inserimento riga vuota
		subReportVO = new TracciatoStampaSchedeSubReportVO("");
		body.add(subReportVO);



		this.isbdsegue = this.getIsbdArea1() + "..." + this.getIsbdArea8();


		if (!this.getIsbdArea1Madre().equals("")) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(" FA PARTE DI ");
			body.add(subReportVO);

			subReportVO = new TracciatoStampaSchedeSubReportVO(this.getIsbdArea1Madre());
			body.add(subReportVO);
		}

		if (this.getListaNatureT().size() > 0) {
			subReportVO = new TracciatoStampaSchedeSubReportVO("CONTIENE ANCHE ");
			body.add(subReportVO);
			for (int i = 0; i < this.getListaNatureT().size(); i++) {
				subReportVO = new TracciatoStampaSchedeSubReportVO((String) this.getListaNatureT().get(i));
				body.add(subReportVO);
			}
		}


		if (this.getListaLegami51().size() > 0) {
			subReportVO = new TracciatoStampaSchedeSubReportVO("CONTIENE ");
			body.add(subReportVO);
			ComboCodDescDatiVO appoggioLegami51 = new ComboCodDescDatiVO();
			for (int i = 0; i < this.getListaLegami51().size(); i++) {
				appoggioLegami51 = (ComboCodDescDatiVO) this.getListaLegami51().get(i);
				if (appoggioLegami51.getCodice() != null && !appoggioLegami51.getCodice().equals(""))  {
					subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getCodice());
					body.add(subReportVO);
				}
				subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getDescrizione());
				body.add(subReportVO);
			}
		}

		String secondAppo = "";
		secondAppo = this.getElencoSoggetti() + " " + this.getElencoIntestSecondarie();
		subReportVO = new TracciatoStampaSchedeSubReportVO(secondAppo);
		body.add(subReportVO);

		for (int i = 0; i < this.getListaClassi().size(); i++) {
			subReportVO = new TracciatoStampaSchedeSubReportVO((String) this.getListaClassi().get(i));
			body.add(subReportVO);
		}

		subReportVO = new TracciatoStampaSchedeSubReportVO(this.getNumInventario());
		body.add(subReportVO);

		this.rows = body.size()+"";
		return "";

	}


	public String getStringaToPrintCataloghi() {

		this.bid = this.getBidEsaminato();
		this.collocazione = this.getTopografico();



		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// richiesta mail contardi 07.11.2012 (la collocazione, attualmente in fondo alla scheda, andrebbe spostata in alto a destra)
		// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013 SI RICAMBIAAAAAAAAAAAAAAAA
		// questa parte va alla fine della scheda del singolo titolo
//		if (this.datiCollocazione.equals("SI") &&  this.getListaTopografico().size()>0) {
//			this.collocazione = this.getListaTopografico().get(0).toString() + " " + this.getListaNumInventario().get(0).toString();
//			this.intestPrimaria = this.intestPrimaria + "                                                  " + this.collocazione;
//		}

		if (this.intestazionePerCatalogo != null) {
			this.titcat = this.intestazionePerCatalogo;
		}


		// da qui inizia la scheda generale che si ripresenta sempre a prescindare dal catalogo richiesto

		body = new ArrayList<TracciatoStampaSchedeSubReportVO>();
		TracciatoStampaSchedeSubReportVO subReportVO;

//		if (this.intestTitoloAdAutore.equals("SI") && (this.autoreRespons1 != null && this.autoreRespons1.length() > 0)) {
//			subReportVO = new TracciatoStampaSchedeSubReportVO(this.autoreRespons1);
//			body.add(subReportVO);
//		}
//		bug  #0003762 esercizio


		// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
		if (this.tipoCatalogoDesc.equals("AUT")) {
			if (this.autoreRespons1 != null && this.autoreRespons1.length() > 0) {
				subReportVO = new TracciatoStampaSchedeSubReportVO(this.autoreRespons1);
				body.add(subReportVO);
			}
		} else if (this.tipoCatalogoDesc.equals("TIP")) {
			// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestazionePerCatalogo);
			body.add(subReportVO);
		} else if (this.tipoCatalogoDesc.equals("TIT")) {
			// nel caso del titolo vale la riga relativa al titolo che verrà comunque stampata dopo;
		} else if (this.tipoCatalogoDesc.equals("POS")) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestazionePerCatalogo);
			body.add(subReportVO);
		} else if (this.tipoCatalogoDesc.equals("SOG")) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestazionePerCatalogo);
			body.add(subReportVO);
			subReportVO = new TracciatoStampaSchedeSubReportVO("");
			body.add(subReportVO);
		} else if (this.tipoCatalogoDesc.equals("CLA")) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestazionePerCatalogo);
			body.add(subReportVO);
		} else if (this.tipoCatalogoDesc.equals("EDI")) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestazionePerCatalogo);
			body.add(subReportVO);
		}
//		subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimaria);
//		body.add(subReportVO);

		if (!this.tipoCatalogoDesc.equals("AUT")) {
			if (this.intestTitoloAdAutore.equals("SI") && (this.autoreRespons1 != null && this.autoreRespons1.length() > 0)) {
				subReportVO = new TracciatoStampaSchedeSubReportVO(this.autoreRespons1);
				body.add(subReportVO);
			} //		bug  #0003762 esercizio
		}


		if (this.getIntestPrimariaFacolt1() != null) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimariaFacolt1);
			body.add(subReportVO);

			subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimariaFacolt2);
			body.add(subReportVO);
		}

		String isbdAppo = "";
		String delimitatoreAree = ". - ";

		if (!this.getIsbdArea1().equals("")) {
			isbdAppo = isbdAppo + this.getIsbdArea1();
		}
		if (!this.getIsbdArea2().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea2;
		}
		if (!this.getIsbdArea3().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea3;
		}
		if (!this.getIsbdArea4().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea4;
		}

		if (!this.getIsbdArea5().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea5;
		}

		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// Mail Contardi 07.11.0212 (- Il titolo di  collana tra parentesi deve essere stampato a seguire l’area della descrizione fisica dopo (  . -  ))
		if (this.titoloCollana.equals("SI") && !this.getIsbdArea6().equals("")) {
			if (isbdAppo.endsWith(".")) {
				isbdAppo = isbdAppo + " - " + this.getIsbdArea6();
			} else {
				isbdAppo = isbdAppo + delimitatoreAree + this.getIsbdArea6();
			}
		}



		subReportVO = new TracciatoStampaSchedeSubReportVO(isbdAppo);
		body.add(subReportVO);

		isbdAppo = "";
		if (!this.getIsbdArea7().trim().equals("")) {
			isbdAppo = this.isbdArea7;

			subReportVO = new TracciatoStampaSchedeSubReportVO(isbdAppo);
			body.add(subReportVO);
			isbdAppo = "";
		}

		// 6.11.2009 almaviva2 BUG 3307 Punto 1 Ogni volta che c'è un a capo per una nuova area (es. ISBN), l'area non è preceduta dalla punteggiatura ". - "

		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// richiesta mail contardi 07.11.2012(La scheda seguente presenta una doppia punteggiatura convenzionale;)
		if (!this.getIsbdArea8().equals("")) {
			if (isbdAppo.equals("")) {
				isbdAppo = this.isbdArea8;
			} else {
				if (isbdAppo.contains(delimitatoreAree)) {
					isbdAppo = isbdAppo + this.isbdArea8;
				} else {
					isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea8;
				}

			}
			subReportVO = new TracciatoStampaSchedeSubReportVO(isbdAppo);
			body.add(subReportVO);
		}


		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// richiesta mail contardi 07.11.2012 - Vedi sopra
//		if (this.titoloCollana.equals("SI") && !this.getIsbdArea6().equals("")) {
//			subReportVO = new TracciatoStampaSchedeSubReportVO(this.getIsbdArea6());
//			body.add(subReportVO);
//		}


		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// richiesta mail contardi 07.11.2012 (la collocazione, attualmente in fondo alla scheda, andrebbe spostata in alto a destra)
		// viene quindi asteriscato qui visto che è stato sportato in alto
//		if (this.datiCollocazione.equals("SI") ) {
//			for (int i = 0; i < this.getListaNumInventario().size(); i++) {
//				subReportVO = new TracciatoStampaSchedeSubReportVO(this.getListaTopografico().get(i).toString() + " " + this.getListaNumInventario().get(i).toString());
//				body.add(subReportVO);
//			}
//		}



		this.isbdsegue = this.getIsbdArea1() + "..." + this.getIsbdArea8();

		// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013 SI RICAMBIAAAAAAAAAAAAAAAA
		for (int i = 0; i < this.getListaNumInventario().size(); i++) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.getListaTopografico().get(i).toString() + " " + this.getListaNumInventario().get(i).toString());
			body.add(subReportVO);
		}




		if (!this.getIsbdArea1Madre().equals("")) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(" FA PARTE DI ");
			body.add(subReportVO);

			subReportVO = new TracciatoStampaSchedeSubReportVO(this.getIsbdArea1Madre());
			body.add(subReportVO);
		}

		if (this.getListaNatureT().size() > 0) {
			subReportVO = new TracciatoStampaSchedeSubReportVO("CONTIENE ANCHE ");
			body.add(subReportVO);
			for (int i = 0; i < this.getListaNatureT().size(); i++) {
				subReportVO = new TracciatoStampaSchedeSubReportVO((String) this.getListaNatureT().get(i));
				body.add(subReportVO);
			}
		}


		if (this.getListaLegami51().size() > 0) {
			subReportVO = new TracciatoStampaSchedeSubReportVO("CONTIENE ");
			body.add(subReportVO);
			ComboCodDescDatiVO appoggioLegami51 = new ComboCodDescDatiVO();
			for (int i = 0; i < this.getListaLegami51().size(); i++) {
				appoggioLegami51 = (ComboCodDescDatiVO) this.getListaLegami51().get(i);
				if (appoggioLegami51.getCodice() != null && !appoggioLegami51.getCodice().equals(""))  {
					subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getCodice());
					body.add(subReportVO);
				}
				// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
				// si inserisce anche l'inventario; l'ISBN deve andara a riga nuova (come le note) ea riga nuova riportare l'inventario
//				subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getDescrizione());
				subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getDescrizione());
				body.add(subReportVO);
				if (appoggioLegami51.getDatiTre().length() > 0) {
					subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getDatiTre());
					body.add(subReportVO);
				}
				if (appoggioLegami51.getDatiDue().length() > 0) {
					subReportVO = new TracciatoStampaSchedeSubReportVO(appoggioLegami51.getDatiDue() + " " + appoggioLegami51.getDatiUno());
				}
				body.add(subReportVO);

			}
		}

		// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
		// La stringa dei soggetti non deve essere stampata
//		String secondAppo = "";
//		secondAppo = this.getElencoSoggetti() + " " + this.getElencoIntestSecondarie();
//		subReportVO = new TracciatoStampaSchedeSubReportVO(secondAppo);
//		body.add(subReportVO);


		// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
		// La stringa delle calssi si stampa per tutti i cataloghi tranne che per quella per classi perchà compare già nell'intestazione
		// della stampa stessa
		if (!this.tipoCatalogoDesc.equals("CLA")) {
			for (int i = 0; i < this.getListaClassi().size(); i++) {
				subReportVO = new TracciatoStampaSchedeSubReportVO((String) this.getListaClassi().get(i));
				body.add(subReportVO);
			}
		}

		if (this.getNumInventario() != null && this.getNumInventario().length() > 0) {
			subReportVO = new TracciatoStampaSchedeSubReportVO(this.getNumInventario());
			body.add(subReportVO);
		}

		subReportVO = new TracciatoStampaSchedeSubReportVO("----------------------------------------------------------------------------"
				+ "------------------------------------------------------------------------------------");
		body.add(subReportVO);

		this.rows = body.size()+"";
		return "";
	}


	public SinteticaTitoliView getStringaForListaSchede() {

		SinteticaTitoliView sinteticaTitoliView = new SinteticaTitoliView();

		sinteticaTitoliView.setBid(bidEsaminato);
		sinteticaTitoliView.setNatura(naturaBidEsaminato);

        if (tipoMateriale.equals("E")) {
        	sinteticaTitoliView.setTipoMateriale("antico");
        	sinteticaTitoliView.setImageUrl("sintAntico.gif");
        } else if (tipoMateriale.equals("C")) {
        	sinteticaTitoliView.setTipoMateriale("cartografia");
        	sinteticaTitoliView.setImageUrl("sintCartografia.gif");
        } else if (tipoMateriale.equals("G")) {
        	sinteticaTitoliView.setTipoMateriale("grafica");
        	sinteticaTitoliView.setImageUrl("sintGrafica.gif");
        } else if (tipoMateriale.equals("M")) {
        	sinteticaTitoliView.setTipoMateriale("moderno");
        	sinteticaTitoliView.setImageUrl("sintModerno.gif");
        } else if (tipoMateriale.equals("U")) {
        	sinteticaTitoliView.setTipoMateriale("musica");
        	sinteticaTitoliView.setImageUrl("sintMusica.gif");
        } else {
        	sinteticaTitoliView.setTipoMateriale("bianco");
        }

		// da qui inizia la scheda generale che si ripresenta sempre a prescindare dal catalogo richiesto
		body = new ArrayList<TracciatoStampaSchedeSubReportVO>();
		TracciatoStampaSchedeSubReportVO subReportVO;


		subReportVO = new TracciatoStampaSchedeSubReportVO(this.intestPrimaria);

		String isbdAppo = bidEsaminato + " " + oggettoCondiviso + " " + naturaBidEsaminato + " " + tipoRecBidEsaminato +  " " + paeseBidEsaminato +  " " + linguaBidEsaminato + "<br>";
		String delimitatoreAree = ". - ";

		if (!this.getIsbdArea1().equals("")) {
			isbdAppo = isbdAppo + isbdArea1;
		}
		if (!this.getIsbdArea2().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + isbdArea2;
		}
		if (!this.getIsbdArea3().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + isbdArea3;
		}
		if (!this.getIsbdArea4().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + isbdArea4;
		}

		if (!this.getIsbdArea5().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + isbdArea5;
		}

		if (!isbdArea6conBid.equals("")) {
			if (isbdAppo.endsWith(".")) {
				isbdAppo = isbdAppo + " - " + isbdArea6conBid;
			} else {
				isbdAppo = isbdAppo + delimitatoreAree + isbdArea6conBid;
			}
		}


		if (!isbdArea7conBid.trim().equals("")) {
			isbdAppo = isbdAppo + delimitatoreAree + isbdArea7conBid;
		}

		if (!isbdArea8.equals("")) {
			if (isbdAppo.equals("")) {
				isbdAppo = isbdArea8;
			} else {
				if (isbdAppo.endsWith(". ")) {
					isbdAppo = isbdAppo + "- " + isbdArea8;
				} else {
					isbdAppo = isbdAppo + delimitatoreAree + isbdArea8;
				}
			}
		}

		if (!vidAutoreRespons1.equals("")) {
			isbdAppo = isbdAppo + "<br>" + " --> " + vidAutoreRespons1 + " 1 " + autoreRespons1;
		}

		if (!isbdArea1MadreconBid.equals("")) {
			isbdAppo = isbdAppo + "<br>" + " FA PARTE DI " + isbdArea1MadreconBid ;
		}

		if (this.getListaLegami51().size() > 0) {
			isbdAppo = isbdAppo + "<br>" + " CONTIENE VOLUMI INFERIORI - verificare su Analitica" ;
		}


		String secondAppo = "";
		secondAppo = this.getElencoSoggetti() + " " + this.getElencoIntestSecondarie();
		if (secondAppo != null && secondAppo.trim().length() > 0) {
			isbdAppo = isbdAppo + "<br>" + " SOGGETTI E INTESTAZIONI SECONDARIE " + secondAppo ;
		}

		secondAppo = "";
		for (int i = 0; i < this.getListaClassi().size(); i++) {
			secondAppo = secondAppo + " " + (String) this.getListaClassi().get(i);
		}
		if (secondAppo != null && secondAppo.trim().length() > 0) {
			isbdAppo = isbdAppo + "<br>" + " CLASSI " + secondAppo ;
		}

		sinteticaTitoliView.setTipoRecDescrizioneLegami(isbdAppo);
		return sinteticaTitoliView;

	}



	public List getListaNatureT() {
		return listaNatureT;
	}


	public void setListaNatureT(List listaNatureT) {
		this.listaNatureT = listaNatureT;
	}

	public List addListaNatureT(String newRiga) {
		listaNatureT.add(newRiga);
		return listaNatureT;
	}


	public String getIsbdArea1Madre() {
		return isbdArea1Madre;
	}


	public void setIsbdArea1Madre(String isbdArea1Madre) {
		this.isbdArea1Madre = isbdArea1Madre;
	}

	public List getListaAutoriRespons2() {
		return listaAutoriRespons2;
	}

	public void setListaAutoriRespons2(List listaAutoriRespons2) {
		this.listaAutoriRespons2 = listaAutoriRespons2;
	}

	public List getListaAutoriRespons3() {
		return listaAutoriRespons3;
	}

	public void setListaAutoriRespons3(List listaAutoriRespons3) {
		this.listaAutoriRespons3 = listaAutoriRespons3;
	}

	public List addListaAutoriRespons2(String newRiga) {
		listaAutoriRespons2.add(newRiga);
		return listaAutoriRespons2;
	}

	public List addListaAutoriRespons3(String newRiga) {
		listaAutoriRespons3.add(newRiga);
		return listaAutoriRespons3;
	}

	public List addListaAutoriRespons4(String newRiga) {
		listaAutoriRespons4.add(newRiga);
		return listaAutoriRespons4;
	}


	public String getTitoloUniforme() {
		return titoloUniforme;
	}

	public void setTitoloUniforme(String titoloUniforme) {
		this.titoloUniforme = titoloUniforme;
	}

	public String getIntestPrimaria() {
		return intestPrimaria;
	}

	public void setIntestPrimaria(String intestPrimaria) {
		this.intestPrimaria = intestPrimaria;
	}

	public String getIntestPrimariaFacolt1() {
		return intestPrimariaFacolt1;
	}

	public void setIntestPrimariaFacolt1(String intestPrimariaFacolt1) {
		this.intestPrimariaFacolt1 = intestPrimariaFacolt1;
	}

	public String getIntestPrimariaFacolt2() {
		return intestPrimariaFacolt2;
	}

	public void setIntestPrimariaFacolt2(String intestPrimariaFacolt2) {
		this.intestPrimariaFacolt2 = intestPrimariaFacolt2;
	}

	public String getIntestazionePerCatalogo() {
		return intestazionePerCatalogo;
	}

	public void setIntestazionePerCatalogo(String intestazionePerCatalogo) {
		this.intestazionePerCatalogo = intestazionePerCatalogo;
	}

	public List getListaSoggetti() {
		return listaSoggetti;
	}

	public void setListaSoggetti(List listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

	public List addListaSoggetti(String newRiga) {
		listaSoggetti.add(newRiga);
		return listaSoggetti;
	}

	public List getListaNatureD() {
		return listaNatureD;
	}

	public void setListaNatureD(List listaNatureD) {
		this.listaNatureD = listaNatureD;
	}

	public List addListaNatureD(String newRiga) {
		listaNatureD.add(newRiga);
		return listaNatureD;
	}
	public List getListaTopografico() {
		return listaTopografico;
	}
	public void setListaTopografico(List listaTopografico) {
		this.listaTopografico = listaTopografico;
	}

	public List addListaTopografico(String newRiga) {
		listaTopografico.add(newRiga);
		return listaTopografico;
	}
	public List getListaNumInventario() {
		return listaNumInventario;
	}
	public void setListaNumInventario(List listaNumInventario) {
		this.listaNumInventario = listaNumInventario;
	}

	public List addListaNumInventario(String newRiga) {
		listaNumInventario.add(newRiga);
		return listaNumInventario;
	}

	public String getNaturaBidEsaminato() {
		return naturaBidEsaminato;
	}
	public void setNaturaBidEsaminato(String naturaBidEsaminato) {
		this.naturaBidEsaminato = naturaBidEsaminato;
	}

	public String getTipoMaterialeDescr() {
		return tipoMaterialeDescr;
	}
	public void setTipoMaterialeDescr(String tipoMaterialeDescr) {
		this.tipoMaterialeDescr = tipoMaterialeDescr;
	}
	public String getTipoCatalogoDesc() {
		return tipoCatalogoDesc;
	}
	public void setTipoCatalogoDesc(String tipoCatalogoDesc) {
		this.tipoCatalogoDesc = tipoCatalogoDesc;
	}
	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}
	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}
	public String getCodSezioneDa() {
		return codSezioneDa;
	}
	public void setCodSezioneDa(String codSezioneDa) {
		this.codSezioneDa = codSezioneDa;
	}
	public String getCodSezioneA() {
		return codSezioneA;
	}
	public void setCodSezioneA(String codSezioneA) {
		this.codSezioneA = codSezioneA;
	}
	public String getCodCollocazioneDa() {
		return codCollocazioneDa;
	}
	public void setCodCollocazioneDa(String codCollocazioneDa) {
		this.codCollocazioneDa = codCollocazioneDa;
	}
	public String getCodSpecificazioneDa() {
		return codSpecificazioneDa;
	}
	public void setCodSpecificazioneDa(String codSpecificazioneDa) {
		this.codSpecificazioneDa = codSpecificazioneDa;
	}
	public String getCodSpecificazioneA() {
		return codSpecificazioneA;
	}
	public void setCodSpecificazioneA(String codSpecificazioneA) {
		this.codSpecificazioneA = codSpecificazioneA;
	}

	public String getCodCollocazioneA() {
		return codCollocazioneA;
	}
	public void setCodCollocazioneA(String codCollocazioneA) {
		this.codCollocazioneA = codCollocazioneA;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getNumTitoliTot() {
		return numTitoliTot;
	}
	public void setNumTitoliTot(String numTitoliTot) {
		this.numTitoliTot = numTitoliTot;
	}
	public String getDataDiElaborazione() {
		return dataDiElaborazione;
	}
	public void setDataDiElaborazione(String dataDiElaborazione) {
		this.dataDiElaborazione = dataDiElaborazione;
	}
	public String getIntestTitoloAdAutore() {
		return intestTitoloAdAutore;
	}
	public void setIntestTitoloAdAutore(String intestTitoloAdAutore) {
		this.intestTitoloAdAutore = intestTitoloAdAutore;
	}
	public String getTitoloCollana() {
		return titoloCollana;
	}
	public void setTitoloCollana(String titoloCollana) {
		this.titoloCollana = titoloCollana;
	}
	public String getTitoliAnalitici() {
		return titoliAnalitici;
	}
	public void setTitoliAnalitici(String titoliAnalitici) {
		this.titoliAnalitici = titoliAnalitici;
	}
	public String getDatiCollocazione() {
		return datiCollocazione;
	}
	public void setDatiCollocazione(String datiCollocazione) {
		this.datiCollocazione = datiCollocazione;
	}
	public String getVidAutoreRespons1() {
		return vidAutoreRespons1;
	}
	public void setVidAutoreRespons1(String vidAutoreRespons1) {
		this.vidAutoreRespons1 = vidAutoreRespons1;
	}
	public String getIsbdArea7conBid() {
		return isbdArea7conBid;
	}
	public void setIsbdArea7conBid(String isbdArea7conBid) {
		this.isbdArea7conBid = isbdArea7conBid;
	}
	public String getIsbdArea6conBid() {
		return isbdArea6conBid;
	}
	public void setIsbdArea6conBid(String isbdArea6conBid) {
		this.isbdArea6conBid = isbdArea6conBid;
	}
	public String getTipoRecBidEsaminato() {
		return tipoRecBidEsaminato;
	}
	public void setTipoRecBidEsaminato(String tipoRecBidEsaminato) {
		this.tipoRecBidEsaminato = tipoRecBidEsaminato;
	}
	public String getLinguaBidEsaminato() {
		return linguaBidEsaminato;
	}
	public void setLinguaBidEsaminato(String linguaBidEsaminato) {
		this.linguaBidEsaminato = linguaBidEsaminato;
	}
	public String getPaeseBidEsaminato() {
		return paeseBidEsaminato;
	}
	public void setPaeseBidEsaminato(String paeseBidEsaminato) {
		this.paeseBidEsaminato = paeseBidEsaminato;
	}
	public String getIsbdArea1MadreconBid() {
		return isbdArea1MadreconBid;
	}
	public void setIsbdArea1MadreconBid(String isbdArea1MadreconBid) {
		this.isbdArea1MadreconBid = isbdArea1MadreconBid;
	}
	public String getTipoMateriale() {
		return tipoMateriale;
	}
	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}
	public String getOggettoCondiviso() {
		return oggettoCondiviso;
	}
	public void setOggettoCondiviso(String oggettoCondiviso) {
		this.oggettoCondiviso = oggettoCondiviso;
	}
	public List getListaAutoriRespons4() {
		return listaAutoriRespons4;
	}
	public void setListaAutoriRespons4(List listaAutoriRespons4) {
		this.listaAutoriRespons4 = listaAutoriRespons4;
	}

}
