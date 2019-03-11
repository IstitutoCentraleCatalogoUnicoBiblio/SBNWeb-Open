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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TracciatoStampaCataloghiVO extends SerializableVO {

	private static final long serialVersionUID = -5729691452499556580L;

	private String bidEsaminato;


	private String intestazionePerCatalogo;

	// INTESTAZIONE: seconda riga, allineato a destra  puo esser composta dai campi sotto esposti
	// cognome autore
	private String intestPrimaria;
	private String intestPrimariaFacolt1;
	private String intestPrimariaFacolt2;

	private String autoreRespons1;
	private List listaAutoriRespons2;
	private List listaAutoriRespons3;
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
	private String isbdArea7;
	private String isbdArea8;

	private List listaLegami51;

	private String isbdArea1Madre;
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

	//
	// Campi e settaggi per la chiamata a Jasper
	private String id;
	private String bid;
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

	public TracciatoStampaCataloghiVO() {
		super();
		// TODO Auto-generated constructor stub
		this.bidEsaminato = "";
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
		this.isbdArea7 = "";
		this.isbdArea8 = "";
		this.elencoSoggetti = "";
		this.elencoIntestSecondarie = "";
		this.isbdArea1Madre = "";
		listaClassi = new ArrayList();
		listaSoggetti = new ArrayList();
		listaLegami51 = new ArrayList();
		listaNatureT = new ArrayList();
		listaNatureD = new ArrayList();
		listaAutoriRespons2 = new ArrayList();
		listaAutoriRespons3 = new ArrayList();
	}

	public String getClasseLegata() {
		return classeLegata;
	}

	public void setClasseLegata(String classeLegata) {
		this.classeLegata = classeLegata;
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

	public List addListaLegami51(String newRiga) {
		listaLegami51.add(newRiga);
		return listaLegami51;
	}


	public String getAutoreRespons1() {
		return autoreRespons1;
	}

	public void setAutoreRespons1(String autoreRespons1) {
		this.autoreRespons1 = autoreRespons1;
	}

	public String getStringaToPrint() {

		this.bid = this.getBidEsaminato();

		if (this.intestazionePerCatalogo != null) {
			this.titcat = this.intestazionePerCatalogo;
		}

		// da qui inizia la scheda generale che si ripresenta sempre a prescindare dal catalogo richiesto

		body = new ArrayList<TracciatoStampaSchedeSubReportVO>();
		TracciatoStampaSchedeSubReportVO subReportVO;

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
				isbdAppo = isbdAppo + delimitatoreAree + this.isbdArea8;
			}
		}

		subReportVO = new TracciatoStampaSchedeSubReportVO(isbdAppo);
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
			for (int i = 0; i < this.getListaLegami51().size(); i++) {
				subReportVO = new TracciatoStampaSchedeSubReportVO((String) this.getListaLegami51().get(i));
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

		this.rows = body.size()+"";
		return "";

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
}
