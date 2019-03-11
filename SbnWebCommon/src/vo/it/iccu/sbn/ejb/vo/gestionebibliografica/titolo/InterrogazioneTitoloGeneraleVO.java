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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;

public class InterrogazioneTitoloGeneraleVO extends SerializableVO {

	private static final long serialVersionUID = 1521640019834521376L;

	// Campi non visuali
	private String tipoMateriale;

	// Dati relativi alla ricerca titolo - Canali principali
	private String titolo;

	private boolean titoloPunt;
	private boolean matAntico;
	private boolean luogoPubblPunt;
	private boolean nomeCollegatoPunt;
	private boolean libretto;
	private boolean ricLocale;
	private boolean ricIndice;

	private boolean titoloPunt_old;
	private boolean matAntico_old;

	private boolean luogoPubblPunt_old;
	private boolean nomeCollegatoPunt_old;
	private boolean libretto_old;
	private boolean ricLocale_old;
	private boolean ricIndice_old;

	private String bid;

	private List listaTipiNumStandard;
	private String numStandardSelez;
	private String numStandard1;
	private String numStandard2;

	private String impronta1;
	private String impronta2;
	private String impronta3;
	private String tipoMaterialeImpronta;

	// Dati relativi alla ricerca titolo - Filtri principali ed eventualmente
	// selezione del Tipo materiale (Tipo Record)
	private List listaNature;
	private String naturaSelez1;
	private String naturaSelez2;
	private String naturaSelez3;
	private String naturaSelez4;
	private List listaSottoNatureD;
	private String sottoNaturaDSelez;
	private String codiceSici;

	private List listaGeneri;
	private String genereSelez1;
	private String genereSelez2;
	private String genereSelez3;
	private String genereSelez4;

	private List listaTipoData;
	private String tipoDataSelez;
	private String data1Da;
	private String data1A;
	private String data2Da;
	private String data2A;

	private List listaLingue;
	private String linguaSelez;
	private List listaPaese;
	private String paeseSelez;
	private String luogoPubbl;

	private String nomeCollegato;
	private String paroleEditore;

	private List listaResponsabilita;
	private String responsabilitaSelez;
	private List listaRelazioni;
	private String relazioniSelez;

	private List listaTipiRecord;
	private String tipiRecordSelez;

	// Output dei 3 RadioButton per la Specificità (presenti/assenti/ignora)
	// private String specificitaRadio;
	private List listaSpecificita;
	private String specificitaSelez;

	// Dati relativi alla ricerca titolo - Parametri generici di Ricerca sempre
	// presenti
	private int elemXBlocchi;
	private List listaTipiOrdinam;
	private String tipoOrdinamSelez;
	private List listaFormatoLista;
	private String formatoListaSelez;

	private List listaBiblioteche;
	private String bibliotecaSelez;

	// Intervento interno 26.11.2009 - almaviva2 - per gestire il filtre
	// sulle Biblioteche
	// Per area servizi si filtrano le biblio di area metropolitana
	// per tutte le altre aree si può scegliere solo di filtrare per la biblio
	// di navigazione
	private boolean ricFiltrataPerBib;

	public String getBibliotecaSelez() {
		return bibliotecaSelez;
	}

	public void setBibliotecaSelez(String bibliotecaSelez) {
		this.bibliotecaSelez = bibliotecaSelez;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getData1A() {
		return data1A;
	}

	public void setData1A(String data1A) {
		this.data1A = data1A;
	}

	public String getData1Da() {
		return data1Da;
	}

	public void setData1Da(String data1Da) {
		this.data1Da = data1Da;
	}

	public String getData2A() {
		return data2A;
	}

	public void setData2A(String data2A) {
		this.data2A = data2A;
	}

	public String getData2Da() {
		return data2Da;
	}

	public void setData2Da(String data2Da) {
		this.data2Da = data2Da;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getFormatoListaSelez() {
		return formatoListaSelez;
	}

	public void setFormatoListaSelez(String formatoListaSelez) {
		this.formatoListaSelez = formatoListaSelez;
	}

	public String getImpronta1() {
		return impronta1;
	}

	public void setImpronta1(String impronta1) {
		this.impronta1 = impronta1;
	}

	public String getImpronta2() {
		return impronta2;
	}

	public void setImpronta2(String impronta2) {
		this.impronta2 = impronta2;
	}

	public String getImpronta3() {
		return impronta3;
	}

	public void setImpronta3(String impronta3) {
		this.impronta3 = impronta3;
	}

	public boolean isLibretto() {
		return libretto;
	}

	public void setLibretto(boolean value) {
		if (this.libretto != value) {
			this.libretto = value;
			this.genereSelez1 = "";
			this.genereSelez2 = "";
		}
	}

	public String getLinguaSelez() {
		return linguaSelez;
	}

	public void setLinguaSelez(String linguaSelez) {
		this.linguaSelez = linguaSelez;
	}

	public List getListaBiblioteche() {
		return listaBiblioteche;
	}

	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}

	public List getListaFormatoLista() {
		return listaFormatoLista;
	}

	public void setListaFormatoLista(List listaFormatoLista) {
		this.listaFormatoLista = listaFormatoLista;
	}

	public List getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List listaLingue) {
		this.listaLingue = listaLingue;
	}

	public List getListaNature() {
		return listaNature;
	}

	public void setListaNature(List listaNature) {
		this.listaNature = listaNature;
	}

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public List getListaRelazioni() {
		return listaRelazioni;
	}

	public void setListaRelazioni(List listaRelazioni) {
		this.listaRelazioni = listaRelazioni;
	}

	public List getListaResponsabilita() {
		if (this.listaResponsabilita != null
				&& this.listaResponsabilita.size() == 0) {
			this.listaResponsabilita = new ArrayList();
		}
		return listaResponsabilita;
	}

	public void setListaResponsabilita(List listaResponsabilita) {
		this.listaResponsabilita = listaResponsabilita;
	}

	public List getListaSottonatureD() {
		return listaSottoNatureD;
	}

	public void setListaSottonatureD(List listaSottonatureD) {
		this.listaSottoNatureD = listaSottonatureD;
	}

	public List getListaTipiNumStandard() {
		return listaTipiNumStandard;
	}

	public void setListaTipiNumStandard(List listaTipiNumStandard) {
		this.listaTipiNumStandard = listaTipiNumStandard;
	}

	public List getListaTipiOrdinam() {
		return listaTipiOrdinam;
	}

	public void setListaTipiOrdinam(List listaTipiOrdinam) {
		this.listaTipiOrdinam = listaTipiOrdinam;
	}

	public List getListaTipiRecord() {
		return listaTipiRecord;
	}

	public void setListaTipiRecord(List listaTipiRecord) {
		this.listaTipiRecord = listaTipiRecord;
	}

	public List getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}

	public String getLuogoPubbl() {
		return luogoPubbl;
	}

	public void setLuogoPubbl(String luogoPubbl) {
		this.luogoPubbl = luogoPubbl;
	}

	public boolean isLuogoPubblPunt() {
		return luogoPubblPunt;
	}

	public void setLuogoPubblPunt(boolean luogoPubblPunt) {
		this.luogoPubblPunt = luogoPubblPunt;
	}

	public boolean isMatAntico() {
		return matAntico;
	}

	public void setMatAntico(boolean matAntico) {
		this.matAntico = matAntico;
	}

	public String getNaturaSelez1() {
		return naturaSelez1;
	}

	public void setNaturaSelez1(String naturaSelez1) {
		this.naturaSelez1 = naturaSelez1;
	}

	public String getNaturaSelez2() {
		return naturaSelez2;
	}

	public void setNaturaSelez2(String naturaSelez2) {
		this.naturaSelez2 = naturaSelez2;
	}

	public String getNaturaSelez3() {
		return naturaSelez3;
	}

	public void setNaturaSelez3(String naturaSelez3) {
		this.naturaSelez3 = naturaSelez3;
	}

	public String getNaturaSelez4() {
		return naturaSelez4;
	}

	public void setNaturaSelez4(String naturaSelez4) {
		this.naturaSelez4 = naturaSelez4;
	}

	public String getNomeCollegato() {
		return nomeCollegato;
	}

	public void setNomeCollegato(String nomeCollegato) {
		this.nomeCollegato = nomeCollegato;
	}

	public boolean isNomeCollegatoPunt() {
		return nomeCollegatoPunt;
	}

	public void setNomeCollegatoPunt(boolean nomeCollegatoPunt) {
		this.nomeCollegatoPunt = nomeCollegatoPunt;
	}

	public String getNumStandard1() {
		return numStandard1;
	}

	public void setNumStandard1(String numStandard1) {
		this.numStandard1 = numStandard1;
	}

	public String getNumStandard2() {
		return numStandard2;
	}

	public void setNumStandard2(String numStandard2) {
		this.numStandard2 = numStandard2;
	}

	public String getNumStandardSelez() {
		return numStandardSelez;
	}

	public void setNumStandardSelez(String numStandardSelez) {
		this.numStandardSelez = numStandardSelez;
	}

	public String getPaeseSelez() {
		return paeseSelez;
	}

	public void setPaeseSelez(String paeseSelez) {
		this.paeseSelez = paeseSelez;
	}

	public String getRelazioniSelez() {
		return relazioniSelez;
	}

	public void setRelazioniSelez(String relazioniSelez) {
		this.relazioniSelez = relazioniSelez;
	}

	public String getResponsabilitaSelez() {
		return responsabilitaSelez;
	}

	public void setResponsabilitaSelez(String responsabilitaSelez) {
		this.responsabilitaSelez = responsabilitaSelez;
	}

	public boolean isRicIndice() {
		return ricIndice;
	}

	public void setRicIndice(boolean ricIndice) {
		this.ricIndice = ricIndice;
	}

	public boolean isRicLocale() {
		return ricLocale;
	}

	public void setRicLocale(boolean ricLocale) {
		this.ricLocale = ricLocale;
	}

	public String getSottoNaturaDSelez() {
		return sottoNaturaDSelez;
	}

	public void setSottoNaturaDSelez(String sottonaturaDSelez) {
		this.sottoNaturaDSelez = sottonaturaDSelez;
	}

	public String getTipiRecordSelez() {
		return tipiRecordSelez;
	}

	public void setTipiRecordSelez(String tipiRecordSelez) {
		this.tipiRecordSelez = tipiRecordSelez;
	}

	public String getTipoDataSelez() {
		return tipoDataSelez;
	}

	public void setTipoDataSelez(String tipoDataSelez) {
		this.tipoDataSelez = tipoDataSelez;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public boolean isTitoloPunt() {
		return titoloPunt;
	}

	public void setTitoloPunt(boolean titoloPunt) {
		this.titoloPunt = titoloPunt;
	}

	public void validaParametriGener() throws ValidationException {
		if (this.ricLocale == false && this.ricIndice == false) {
			throw new ValidationException("livRicObblig",
					ValidationExceptionCodici.livRicObblig);
		}
	}

	public List getListaSottoNatureD() {
		return listaSottoNatureD;
	}

	public void setListaSottoNatureD(List listaSottoNatureD) {
		this.listaSottoNatureD = listaSottoNatureD;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public List getListaSpecificita() {
		return listaSpecificita;
	}

	public void setListaSpecificita(List listaSpecificita) {
		this.listaSpecificita = listaSpecificita;
	}

	public String getSpecificitaSelez() {
		return specificitaSelez;
	}

	public void setSpecificitaSelez(String specificitaSelez) {
		this.specificitaSelez = specificitaSelez;
	}

	public void save() {
		this.ricLocale_old = this.ricLocale;
		this.ricIndice_old = this.ricIndice;

		this.titoloPunt_old = this.titoloPunt;
		this.matAntico_old = this.matAntico;
		this.luogoPubblPunt_old = this.luogoPubblPunt;
		this.nomeCollegatoPunt_old = this.nomeCollegatoPunt;
		this.libretto_old = this.libretto;
	}

	public void restore() {
		this.ricLocale = this.ricLocale_old;
		this.ricIndice = this.ricIndice_old;

		this.titoloPunt = this.titoloPunt_old;
		this.matAntico = this.matAntico_old;
		this.luogoPubblPunt = this.luogoPubblPunt_old;
		this.nomeCollegatoPunt = this.nomeCollegatoPunt_old;
		this.libretto = this.libretto_old;
	}

	public String getGenereSelez1() {
		return genereSelez1;
	}

	public void setGenereSelez1(String genereSelez1) {
		this.genereSelez1 = genereSelez1;
	}

	public String getGenereSelez2() {
		return genereSelez2;
	}

	public void setGenereSelez2(String genereSelez2) {
		this.genereSelez2 = genereSelez2;
	}

	public String getGenereSelez3() {
		return genereSelez3;
	}

	public void setGenereSelez3(String genereSelez3) {
		this.genereSelez3 = genereSelez3;
	}

	public String getGenereSelez4() {
		return genereSelez4;
	}

	public void setGenereSelez4(String genereSelez4) {
		this.genereSelez4 = genereSelez4;
	}

	public String getTipoMaterialeImpronta() {
		return tipoMaterialeImpronta;
	}

	public void setTipoMaterialeImpronta(String tipoMaterialeImpronta) {
		this.tipoMaterialeImpronta = tipoMaterialeImpronta;
	}

	public String getCodiceSici() {
		return codiceSici;
	}

	public void setCodiceSici(String codiceSici) {
		this.codiceSici = codiceSici;
	}

	public boolean isRicFiltrataPerBib() {
		return ricFiltrataPerBib;
	}

	public void setRicFiltrataPerBib(boolean ricFiltrataPerBib) {
		this.ricFiltrataPerBib = ricFiltrataPerBib;
	}

	public List getListaGeneri() {
		return listaGeneri;
	}

	public void setListaGeneri(List listaGeneri) {
		this.listaGeneri = listaGeneri;
	}

	public String getParoleEditore() {
		return paroleEditore;
	}

	public void setParoleEditore(String paroleEditore) {
		this.paroleEditore = paroleEditore;
	}
}
