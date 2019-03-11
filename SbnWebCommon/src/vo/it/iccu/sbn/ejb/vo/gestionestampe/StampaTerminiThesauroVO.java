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
package it.iccu.sbn.ejb.vo.gestionestampe;

import java.io.Serializable;
import java.sql.Timestamp;

import net.sf.jasperreports.engine.JRRewindableDataSource;

public class StampaTerminiThesauroVO  implements Serializable {//extends ParametriStampaVO
	/**
	 *
	 */
	private static final long serialVersionUID = 6082297994238563815L;

	private String codBib;
	private String codPolo;
	//Da ParametriStampaVO che é esteso da ParametriStampaTerminiThesauroVO

	//Data di inserimento da
	private Timestamp dateInsDa; //tsIns_da
	//Data di inserimento a
	private Timestamp dateInsA; //tsIns_a
	//Data di variazione da
	private Timestamp dateAggDa; //tsVar_da
	//Dta di variazione a
	private Timestamp dateAggA; //tsVar_a
	//Stampa dei titoli collegati
	private String isStampaTitoli; //= Boolean.valueOf(true); //stampaTitoli
	//Stampa solo stringa thesauro
	private String isStampaStringaThes; //stampaNote
	//

	//Da ParametriStampaTerminiThesauroVO

	//Descrizione del Thesauro
	private String descThesauro; //descrizioneThesauro

	//Codice del Thesauro
	private String codeThesauro;

	//Stampa solo thesauri inseriti o modificati dalla biblioteca
	private String isThesauriBiblio; //soloTerminiBiblioteca

	//Relazione con latri termini
	private String isRelazAltriTerm; //stampaTerminiCollegati

	private String isSoloLegamiTitoliUtilizzati;

	private String isSoloLegamiTitoliInseritiDaBiblio;
	//


	//Da ElementoStampaSematicaVO
	private String termine; //testo

	private String did; 	//id

	private String note; 	//note

//	private String livelloAutorita;
	//


	//String denominazione della biblioteca
	private String denBib;

	//Stampa note al legame titolo thesauro
	private String isStampaNoteTitle;

	//Stampa delle note al Thesauro
	private String isStampaNoteThes;

	//Relazione con altre forme non accettate
	private String isRelazAltreForm;

	//Stampa dei termini collegati dalla propria biblioteca
	private String isStampaTermBiblio;


	//Dalla lista di ElementoStampaTerminiThesauroVO (no ElementoStampaSematicaVO della classe ParametriStampaVO che é estesa da ParametriStampaTerminiThesauroVO)
	private JRRewindableDataSource titoli; //legami contiene bid, titolo, notaLegame   List<LegameTitoloVO>

//	private List<LegameTitoloVO> titles;

//	Dalla lista di LegameTermineVO della classe ElementoStampaTerminiThesauroVO
	private JRRewindableDataSource termini; //List<LegameTermineVO>

//	private List<LegameTermineVO> terms;


	public Timestamp getDateAggA() {
		return dateAggA;
	}



	public void setDateAggA(Timestamp dateAggA) {
		this.dateAggA = dateAggA;
	}



	public Timestamp getDateAggDa() {
		return dateAggDa;
	}



	public void setDateAggDa(Timestamp dateAggDa) {
		this.dateAggDa = dateAggDa;
	}



	public Timestamp getDateInsA() {
		return dateInsA;
	}



	public void setDateInsA(Timestamp dateInsA) {
		this.dateInsA = dateInsA;
	}



	public Timestamp getDateInsDa() {
		return dateInsDa;
	}



	public void setDateInsDa(Timestamp dateInsDa) {
		this.dateInsDa = dateInsDa;
	}



	public String getDenBib() {
		return denBib;
	}



	public void setDenBib(String denBib) {
		this.denBib = denBib;
	}



	public String getDescThesauro() {
		return descThesauro;
	}



	public void setDescThesauro(String descThesauro) {
		this.descThesauro = descThesauro;
	}



	public String getDid() {
		return did;
	}



	public void setDid(String did) {
		this.did = did;
	}


	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public String getTermine() {
		return termine;
	}



	public void setTermine(String termine) {
		this.termine = termine;
	}



	public void setRelazAltreForm(String isRelazAltreForm) {
		this.isRelazAltreForm = isRelazAltreForm;
	}



	public void setRelazAltriTerm(String isRelazAltriTerm) {
		this.isRelazAltriTerm = isRelazAltriTerm;
	}



	public void setStampaNoteThes(String isStampaNoteThes) {
		this.isStampaNoteThes = isStampaNoteThes;
	}



	public void setStampaNoteTitle(String isStampaNoteTitle) {
		this.isStampaNoteTitle = isStampaNoteTitle;
	}



	public void setStampaStringaThes(String isStampaStringaThes) {
		this.isStampaStringaThes = isStampaStringaThes;
	}



	public void setStampaTermBiblio(String isStampaTermBiblio) {
		this.isStampaTermBiblio = isStampaTermBiblio;
	}



	public void setStampaTitoli(String isStampaTitoli) {
		this.isStampaTitoli = isStampaTitoli;
	}



	public void setThesauriBiblio(String isThesauriBiblio) {
		this.isThesauriBiblio = isThesauriBiblio;
	}



	public String isRelazAltreForm() {
		return isRelazAltreForm;
	}



	public String isRelazAltriTerm() {
		return isRelazAltriTerm;
	}



	public String isStampaNoteThes() {
		return isStampaNoteThes;
	}



	public String isStampaNoteTitle() {
		return isStampaNoteTitle;
	}



	public String isStampaStringaThes() {
		return isStampaStringaThes;
	}



	public String isStampaTermBiblio() {
		return isStampaTermBiblio;
	}



	public String isStampaTitoli() {
		return isStampaTitoli;
	}



	public String isThesauriBiblio() {
		return isThesauriBiblio;
	}



	public String getIsRelazAltreForm() {
		return isRelazAltreForm;
	}



	public String getIsRelazAltriTerm() {
		return isRelazAltriTerm;
	}



	public String getIsStampaNoteThes() {
		return isStampaNoteThes;
	}



	public String getIsStampaNoteTitle() {
		return isStampaNoteTitle;
	}



	public String getIsStampaStringaThes() {
		return isStampaStringaThes;
	}



	public String getIsStampaTermBiblio() {
		return isStampaTermBiblio;
	}



	public String getIsStampaTitoli() {
		return isStampaTitoli;
	}



	public String getIsThesauriBiblio() {
		return isThesauriBiblio;
	}



	public String getCodBib() {
		return codBib;
	}



	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}



	public String getCodPolo() {
		return codPolo;
	}



	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}



	public JRRewindableDataSource getTermini() {
		return termini;
	}



	public void setTermini(JRRewindableDataSource termini) {
		this.termini = termini;
	}



	public JRRewindableDataSource getTitoli() {
		return titoli;
	}



	public void setTitoli(JRRewindableDataSource titoli) {
		this.titoli = titoli;
	}



	public String getCodeThesauro() {
		return codeThesauro;
	}



	public void setCodeThesauro(String codeThesauro) {
		this.codeThesauro = codeThesauro;
	}



	public String isSoloLegamiTitoliInseritiDaBiblio() {
		return isSoloLegamiTitoliInseritiDaBiblio;
	}



	public String isSoloLegamiTitoliUtilizzati() {
		return isSoloLegamiTitoliUtilizzati;
	}



	public void setSoloLegamiTitoliInseritiDaBiblio(
			String isSoloLegamiTitoliInseritiDaBiblio) {
		this.isSoloLegamiTitoliInseritiDaBiblio = isSoloLegamiTitoliInseritiDaBiblio;
	}



	public void setSoloLegamiTitoliUtilizzati(String isSoloLegamiTitoliUtilizzati) {
		this.isSoloLegamiTitoliUtilizzati = isSoloLegamiTitoliUtilizzati;
	}



	public String getIsSoloLegamiTitoliInseritiDaBiblio() {
		return isSoloLegamiTitoliInseritiDaBiblio;
	}



	public String getIsSoloLegamiTitoliUtilizzati() {
		return isSoloLegamiTitoliUtilizzati;
	}



	public void setIsRelazAltreForm(String isRelazAltreForm) {
		this.isRelazAltreForm = isRelazAltreForm;
	}



	public void setIsRelazAltriTerm(String isRelazAltriTerm) {
		this.isRelazAltriTerm = isRelazAltriTerm;
	}



	public void setIsSoloLegamiTitoliInseritiDaBiblio(
			String isSoloLegamiTitoliInseritiDaBiblio) {
		this.isSoloLegamiTitoliInseritiDaBiblio = isSoloLegamiTitoliInseritiDaBiblio;
	}



	public void setIsSoloLegamiTitoliUtilizzati(String isSoloLegamiTitoliUtilizzati) {
		this.isSoloLegamiTitoliUtilizzati = isSoloLegamiTitoliUtilizzati;
	}



	public void setIsStampaNoteThes(String isStampaNoteThes) {
		this.isStampaNoteThes = isStampaNoteThes;
	}



	public void setIsStampaNoteTitle(String isStampaNoteTitle) {
		this.isStampaNoteTitle = isStampaNoteTitle;
	}



	public void setIsStampaStringaThes(String isStampaStringaThes) {
		this.isStampaStringaThes = isStampaStringaThes;
	}



	public void setIsStampaTermBiblio(String isStampaTermBiblio) {
		this.isStampaTermBiblio = isStampaTermBiblio;
	}



	public void setIsStampaTitoli(String isStampaTitoli) {
		this.isStampaTitoli = isStampaTitoli;
	}



	public void setIsThesauriBiblio(String isThesauriBiblio) {
		this.isThesauriBiblio = isThesauriBiblio;
	}



//	public String getLivelloAutorita() {
//		return livelloAutorita;
//	}



//	public void setLivelloAutorita(String livelloAutorita) {
//		this.livelloAutorita = livelloAutorita;
//	}



//	public List<LegameTermineVO> getTerms() {
//		return terms;
//	}
//
//
//
//	public void setTerms(List<LegameTermineVO> terms) {
//		this.terms = terms;
//	}
//
//
//
//	public List<LegameTitoloVO> getTitles() {
//		return titles;
//	}
//
//
//
//	public void setTitles(List<LegameTitoloVO> titles) {
//		this.titles = titles;
//	}

}
