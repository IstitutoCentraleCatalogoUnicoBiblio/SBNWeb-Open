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

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRRewindableDataSource;

public class StampaBuoniDiOrdineVO implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -2887103959999761078L;
	private String nomeLogoImg;
	private JRRewindableDataSource intestazioni;//Sarà ricavato da ConfigurazioneBOVO
	private String numBuonoOrdine;
	private String dataBuonoOrdine;
	private String testoIt;
	private String testoEn;
//	é valido sia per l'inglese che per l'italiano
	private String testoIntroduttivo;
	private JRRewindableDataSource ordini;//List<OrdiniVO> listaOrdini;
	private JRRewindableDataSource datiFineStampa;//List<OrdiniVO> formulaChiusura;
	private double importo_Fornitura_Euro;
	private String nomeFirmaImg;
	private FornitoreVO anagFornitore;
	private String codBibl;
	private String codPolo;


	//Il destinatario per ora deve iniziare con Spett.le
	//ma giacchè potrebbe variare é preferibile metterlo anzichè come label, come field del report
//	private String destinatario;

//	private String ticket;
//	private Integer progressivo=0;
//	private String utente;
//	private String codPolo;
//	private int IDBuonoOrd;
//	private int IDBil;
//	private String codBibl;
//	private String denoBibl;
//	private String numBuonoOrdine;
//	private String dataBuono_Ordine;
//	private String statoBuonoOrdine;
//	private StrutturaCombo fornitore;
//
//	private StrutturaTerna bilancio;
//	private double importo;
//	private String importoStr;
//	private String tipoVariazione;
//	private boolean flag_canc=false;


//// Dati relativi alla ricerca titolo - Canali principali
//	private String titolo;
//	//	 TODO: inserire ICONA della tastiera caratteri speciali
//	private boolean titoloPunt;
//
//	private String bid;
//
//	private List listaTipiNumStandard;
//	private String numStandardSelez;
//	private String numStandard1;
//	private String numStandard2;
//
//	private String impronta1;
//	private String impronta2;
//	private String impronta3;
//
////	 Dati relativi alla ricerca titolo - Filtri principali ed eventualmente selezione del Tipo materiale (Tipo Record)
//	private List listaNature;
//	private String naturaSelez1;
//	private String naturaSelez2;
//	private String naturaSelez3;
//	private String naturaSelez4;
//	private List listaSottoNatureD;
//	private String sottoNaturaDSelez;
//
//	private List listaTipoData;
//	private String tipoDataSelez;
//	private String data1Da;
//	private String data1A;
//	private String data2Da;
//	private String data2A;
//	private boolean matAntico;
//
//	private List listaLingue;
//	private String linguaSelez;
//	private List listaPaese;
//	private String paeseSelez;
//	private String luogoPubbl;
//	private boolean luogoPubblPunt;
//
//	private String nomeCollegato;
//	//	 TODO: inserire ICONA della tastiera caratteri speciali
//	private boolean nomeCollegatoPunt;
//
//	private List listaResponsabilita;
//	private String responsabilitaSelez;
//	private List listaRelazioni;
//	private String relazioniSelez;
//
//	private List listaTipiRecord;
//	private String tipiRecordSelez;
//	private boolean libretto;
//
//
////	 Output dei 3 RadioButton per la Specificità (presenti/assenti/ignora)
//	private String specificitaRadio;
//
////	 Dati relativi alla ricerca titolo - Parametri generici di Ricerca sempre presenti
//	private int elemXBlocchi;
//	private List listaTipiOrdinam;
//	private String tipoOrdinamSelez;
//	private List listaFormatoLista;
//	private String formatoListaSelez;
//
//	private boolean ricLocale;
//	private List listaBiblioteche;
//	private String bibliotecaSelez;
//	private boolean ricIndice;
//
//
//	public String getBibliotecaSelez() {
//		return bibliotecaSelez;
//	}
//	public void setBibliotecaSelez(String bibliotecaSelez) {
//		this.bibliotecaSelez = bibliotecaSelez;
//	}
//	public String getBid() {
//		return bid;
//	}
//	public void setBid(String bid) {
//		this.bid = bid;
//	}
//	public String getData1A() {
//		return data1A;
//	}
//	public void setData1A(String data1A) {
//		this.data1A = data1A;
//	}
//	public String getData1Da() {
//		return data1Da;
//	}
//	public void setData1Da(String data1Da) {
//		this.data1Da = data1Da;
//	}
//	public String getData2A() {
//		return data2A;
//	}
//	public void setData2A(String data2A) {
//		this.data2A = data2A;
//	}
//	public String getData2Da() {
//		return data2Da;
//	}
//	public void setData2Da(String data2Da) {
//		this.data2Da = data2Da;
//	}
//	public int getElemXBlocchi() {
//		return elemXBlocchi;
//	}
//	public void setElemXBlocchi(int elemXBlocchi) {
//		this.elemXBlocchi = elemXBlocchi;
//	}
//	public String getFormatoListaSelez() {
//		return formatoListaSelez;
//	}
//	public void setFormatoListaSelez(String formatoListaSelez) {
//		this.formatoListaSelez = formatoListaSelez;
//	}
//	public String getImpronta1() {
//		return impronta1;
//	}
//	public void setImpronta1(String impronta1) {
//		this.impronta1 = impronta1;
//	}
//	public String getImpronta2() {
//		return impronta2;
//	}
//	public void setImpronta2(String impronta2) {
//		this.impronta2 = impronta2;
//	}
//	public String getImpronta3() {
//		return impronta3;
//	}
//	public void setImpronta3(String impronta3) {
//		this.impronta3 = impronta3;
//	}
//	public boolean isLibretto() {
//		return libretto;
//	}
//	public void setLibretto(boolean libretto) {
//		this.libretto = libretto;
//	}
//	public String getLinguaSelez() {
//		return linguaSelez;
//	}
//	public void setLinguaSelez(String linguaSelez) {
//		this.linguaSelez = linguaSelez;
//	}
//	public List getListaBiblioteche() {
//		return listaBiblioteche;
//	}
//	public void setListaBiblioteche(List listaBiblioteche) {
//		this.listaBiblioteche = listaBiblioteche;
//	}
//	public List getListaFormatoLista() {
//		return listaFormatoLista;
//	}
//	public void setListaFormatoLista(List listaFormatoLista) {
//		this.listaFormatoLista = listaFormatoLista;
//	}
//	public List getListaLingue() {
//		return listaLingue;
//	}
//	public void setListaLingue(List listaLingue) {
//		this.listaLingue = listaLingue;
//	}
//	public List getListaNature() {
//		return listaNature;
//	}
//	public void setListaNature(List listaNature) {
//		this.listaNature = listaNature;
//	}
//	public List getListaPaese() {
//		return listaPaese;
//	}
//	public void setListaPaese(List listaPaese) {
//		this.listaPaese = listaPaese;
//	}
//	public List getListaRelazioni() {
//		return listaRelazioni;
//	}
//	public void setListaRelazioni(List listaRelazioni) {
//		this.listaRelazioni = listaRelazioni;
//	}
//	public List getListaResponsabilita() {
//		return listaResponsabilita;
//	}
//	public void setListaResponsabilita(List listaResponsabilita) {
//		this.listaResponsabilita = listaResponsabilita;
//	}
//	public List getListaSottonatureD() {
//		return listaSottoNatureD;
//	}
//	public void setListaSottonatureD(List listaSottonatureD) {
//		this.listaSottoNatureD = listaSottonatureD;
//	}
//	public List getListaTipiNumStandard() {
//		return listaTipiNumStandard;
//	}
//	public void setListaTipiNumStandard(List listaTipiNumStandard) {
//		this.listaTipiNumStandard = listaTipiNumStandard;
//	}
//	public List getListaTipiOrdinam() {
//		return listaTipiOrdinam;
//	}
//	public void setListaTipiOrdinam(List listaTipiOrdinam) {
//		this.listaTipiOrdinam = listaTipiOrdinam;
//	}
//	public List getListaTipiRecord() {
//		return listaTipiRecord;
//	}
//	public void setListaTipiRecord(List listaTipiRecord) {
//		this.listaTipiRecord = listaTipiRecord;
//	}
//	public List getListaTipoData() {
//		return listaTipoData;
//	}
//	public void setListaTipoData(List listaTipoData) {
//		this.listaTipoData = listaTipoData;
//	}
//	public String getLuogoPubbl() {
//		return luogoPubbl;
//	}
//	public void setLuogoPubbl(String luogoPubbl) {
//		this.luogoPubbl = luogoPubbl;
//	}
//	public boolean isLuogoPubblPunt() {
//		return luogoPubblPunt;
//	}
//	public void setLuogoPubblPunt(boolean luogoPubblPunt) {
//		this.luogoPubblPunt = luogoPubblPunt;
//	}
//	public boolean isMatAntico() {
//		return matAntico;
//	}
//	public void setMatAntico(boolean matAntico) {
//		this.matAntico = matAntico;
//	}
//	public String getNaturaSelez1() {
//		return naturaSelez1;
//	}
//	public void setNaturaSelez1(String naturaSelez1) {
//		this.naturaSelez1 = naturaSelez1;
//	}
//	public String getNaturaSelez2() {
//		return naturaSelez2;
//	}
//	public void setNaturaSelez2(String naturaSelez2) {
//		this.naturaSelez2 = naturaSelez2;
//	}
//	public String getNaturaSelez3() {
//		return naturaSelez3;
//	}
//	public void setNaturaSelez3(String naturaSelez3) {
//		this.naturaSelez3 = naturaSelez3;
//	}
//	public String getNaturaSelez4() {
//		return naturaSelez4;
//	}
//	public void setNaturaSelez4(String naturaSelez4) {
//		this.naturaSelez4 = naturaSelez4;
//	}
//	public String getNomeCollegato() {
//		return nomeCollegato;
//	}
//	public void setNomeCollegato(String nomeCollegato) {
//		this.nomeCollegato = nomeCollegato;
//	}
//	public boolean isNomeCollegatoPunt() {
//		return nomeCollegatoPunt;
//	}
//	public void setNomeCollegatoPunt(boolean nomeCollegatoPunt) {
//		this.nomeCollegatoPunt = nomeCollegatoPunt;
//	}
//	public String getNumStandard1() {
//		return numStandard1;
//	}
//	public void setNumStandard1(String numStandard1) {
//		this.numStandard1 = numStandard1;
//	}
//	public String getNumStandard2() {
//		return numStandard2;
//	}
//	public void setNumStandard2(String numStandard2) {
//		this.numStandard2 = numStandard2;
//	}
//	public String getNumStandardSelez() {
//		return numStandardSelez;
//	}
//	public void setNumStandardSelez(String numStandardSelez) {
//		this.numStandardSelez = numStandardSelez;
//	}
//	public String getPaeseSelez() {
//		return paeseSelez;
//	}
//	public void setPaeseSelez(String paeseSelez) {
//		this.paeseSelez = paeseSelez;
//	}
//	public String getRelazioniSelez() {
//		return relazioniSelez;
//	}
//	public void setRelazioniSelez(String relazioniSelez) {
//		this.relazioniSelez = relazioniSelez;
//	}
//	public String getResponsabilitaSelez() {
//		return responsabilitaSelez;
//	}
//	public void setResponsabilitaSelez(String responsabilitaSelez) {
//		this.responsabilitaSelez = responsabilitaSelez;
//	}
//	public boolean isRicIndice() {
//		return ricIndice;
//	}
//	public void setRicIndice(boolean ricIndice) {
//		this.ricIndice = ricIndice;
//	}
//	public boolean isRicLocale() {
//		return ricLocale;
//	}
//	public void setRicLocale(boolean ricLocale) {
//		this.ricLocale = ricLocale;
//	}
//	public String getSottoNaturaDSelez() {
//		return sottoNaturaDSelez;
//	}
//	public void setSottoNaturaDSelez(String sottonaturaDSelez) {
//		this.sottoNaturaDSelez = sottonaturaDSelez;
//	}
//	public String getSpecificitaRadio() {
//		return specificitaRadio;
//	}
//	public void setSpecificitaRadio(String specificitaRadio) {
//		this.specificitaRadio = specificitaRadio;
//	}
//	public String getTipiRecordSelez() {
//		return tipiRecordSelez;
//	}
//	public void setTipiRecordSelez(String tipiRecordSelez) {
//		this.tipiRecordSelez = tipiRecordSelez;
//	}
//	public String getTipoDataSelez() {
//		return tipoDataSelez;
//	}
//	public void setTipoDataSelez(String tipoDataSelez) {
//		this.tipoDataSelez = tipoDataSelez;
//	}
//	public String getTipoOrdinamSelez() {
//		return tipoOrdinamSelez;
//	}
//	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
//		this.tipoOrdinamSelez = tipoOrdinamSelez;
//	}
//	public String getTitolo() {
//		return titolo;
//	}
//	public void setTitolo(String titolo) {
//		this.titolo = titolo;
//	}
//	public boolean isTitoloPunt() {
//		return titoloPunt;
//	}
//	public void setTitoloPunt(boolean titoloPunt) {
//		this.titoloPunt = titoloPunt;
//	}
//	public List getListaSottoNatureD() {
//	return listaSottoNatureD;
//}
//public void setListaSottoNatureD(List listaSottoNatureD) {
//	this.listaSottoNatureD = listaSottoNatureD;
//}

//	public List<OrdiniVO> getFormulaChiusura() {
//		return formulaChiusura;
//	}
//	public void setFormulaChiusura(List<OrdiniVO> formulaChiusura) {
//		this.formulaChiusura = formulaChiusura;
//	}
//	public List<OrdiniVO> getIntestazione() {
//		return intestazione;
//	}
//	public void setIntestazione(List<OrdiniVO> intestazione) {
//		this.intestazione = intestazione;
//	}
//	public List<OrdiniVO> getListaOrdini() {
//		return listaOrdini;
//	}
//	public void setListaOrdini(List<OrdiniVO> listaOrdini) {
//		this.listaOrdini = listaOrdini;
//	}
	public String getDataBuonoOrdine() {
		return dataBuonoOrdine;
	}
	public void setDataBuonoOrdine(String dataBuonoOrdine) {
		this.dataBuonoOrdine = dataBuonoOrdine;
	}

	public double getImporto_Fornitura_Euro() {
		return importo_Fornitura_Euro;
	}
	public void setImporto_Fornitura_Euro(double importo_Fornitura) {
		this.importo_Fornitura_Euro = importo_Fornitura;
	}

	public String getNomeFirmaImg() {
		return nomeFirmaImg;
	}
	public void setNomeFirmaImg(String nomeFirmaImg) {
		this.nomeFirmaImg = nomeFirmaImg;
	}
	public String getNomeLogoImg() {
		return nomeLogoImg;
	}
	public void setNomeLogoImg(String nomeLogoImg) {
		this.nomeLogoImg = nomeLogoImg;
	}

	public String getTestoEn() {
		return testoEn;
	}
	public void setTestoEn(String testoEn) {
		this.testoEn = testoEn;
	}
	public String getTestoIntroduttivo() {
		return testoIntroduttivo;
	}
	public void setTestoIntroduttivo(String testoIntroduttivo) {
		this.testoIntroduttivo = testoIntroduttivo;
	}
	public String getTestoIt() {
		return testoIt;
	}
	public void setTestoIt(String testoIt) {
		this.testoIt = testoIt;
	}
	public String getNumBuonoOrdine() {
		return numBuonoOrdine;
	}
	public void setNumBuonoOrdine(String numBuonoOrdine) {
		this.numBuonoOrdine = numBuonoOrdine;
	}
	public FornitoreVO getAnagFornitore() {
		return anagFornitore;
	}
	public void setAnagFornitore(FornitoreVO anagFornitore) {
		this.anagFornitore = anagFornitore;
	}
	public String getCodBibl() {
		return codBibl;
	}
	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public JRRewindableDataSource getDatiFineStampa() {
		return datiFineStampa;
	}
	public void setDatiFineStampa(JRRewindableDataSource datiFineStampa) {
		this.datiFineStampa = datiFineStampa;
	}
	public JRRewindableDataSource getIntestazioni() {
		return intestazioni;
	}
	public void setIntestazioni(JRRewindableDataSource intestazioni) {
		this.intestazioni = intestazioni;
	}
	public JRRewindableDataSource getOrdini() {
		return ordini;
	}
	public void setOrdini(JRRewindableDataSource ordini) {
		this.ordini = ordini;
	}

//	public SbnStampaBuoniOrdine getSbn() {
//		return sbn;
//	}
//	public void setSbn(SbnStampaBuoniOrdine sbn) {
//		this.sbn = sbn;
//	}
}
