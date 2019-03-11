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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;


public class StampaBuoniVO extends SerializableVO {

	private static final long serialVersionUID = -719225992548036934L;

	private String ticket;
	private String utente;

	private boolean presenzaLogoImg=false;
	private String nomeLogoImg; // indicazione del file immagine del logo

	private String codPolo;
	private String codBibl;
	private String denoBib;

	private String listaDatiIntestazione;

	private boolean etichettaProtocollo=false; // indicazione del protocollo (da aggiungere manualmente)
	private boolean etichettaDataProtocollo=false; //indicazione della data di protocollo (da aggiungere manualmente)

	private String numProtocollo="";
	private String numBuonoOrdine="";
	private String dataStampa="";

	private FornitoreVO anagFornitore=new FornitoreVO();
	private String testoOggetto; // distinzione per tipologia A, C, D, L,V,R (in codice2 c'è il tipo ordine non è posizionale)

	private String testoIntroduttivo=""; // distinzione per tipologia A, C, D, L,V,R (in codice2 c'è il tipo ordine non è posizionale)

	private boolean presenzaPrezzo=false; // valido per tutti gli ordini
	private List<RigheOrdiniStampaBuoniVO> listaOrdiniDaStampare=new ArrayList<RigheOrdiniStampaBuoniVO> ();
	private List listaOggDaStampare=new ArrayList();
	private String importoFornitura;
	private String valuta;
	private String importoFornituraEur;


	private String listaDatiFineStampa;

	private boolean presenzaFirmaImg=false;
	private String nomeFirmaImg; // indicazione del file immagine della firma

	private boolean indicaRistampa=false;
	private String etichettaRistampa="";
	private String reportErrori="";

	public StampaBuoniVO (){}



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






	public boolean isEtichettaDataProtocollo() {
		return etichettaDataProtocollo;
	}



	public void setEtichettaDataProtocollo(boolean etichettaDataProtocollo) {
		this.etichettaDataProtocollo = etichettaDataProtocollo;
	}



	public boolean isEtichettaProtocollo() {
		return etichettaProtocollo;
	}



	public void setEtichettaProtocollo(boolean etichettaProtocollo) {
		this.etichettaProtocollo = etichettaProtocollo;
	}



	public String getImportoFornitura() {
		return importoFornitura;
	}



	public void setImportoFornitura(String importoFornitura) {
		this.importoFornitura = importoFornitura;
	}



	public boolean isIndicaRistampa() {
		return indicaRistampa;
	}



	public void setIndicaRistampa(boolean indicaRistampa) {
		this.indicaRistampa = indicaRistampa;
	}



	public List<RigheOrdiniStampaBuoniVO> getListaOrdiniDaStampare() {
		return listaOrdiniDaStampare;
	}

	public Integer getNumeroOrdiniDaStampare() {
		//serve a jasper per la numerazione delle pagine
		return new Integer(listaOrdiniDaStampare.size());
	}



	public void setListaOrdiniDaStampare(
			List<RigheOrdiniStampaBuoniVO> listaOrdiniDaStampare) {
		this.listaOrdiniDaStampare = listaOrdiniDaStampare;
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



	public String getNumBuonoOrdine() {
		return numBuonoOrdine;
	}



	public void setNumBuonoOrdine(String numBuonoOrdine) {
		this.numBuonoOrdine = numBuonoOrdine;
	}



	public String getNumProtocollo() {
		return numProtocollo;
	}



	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}



	public boolean isPresenzaFirmaImg() {
		return presenzaFirmaImg;
	}



	public void setPresenzaFirmaImg(boolean presenzaFirmaImg) {
		this.presenzaFirmaImg = presenzaFirmaImg;
	}



	public boolean isPresenzaLogoImg() {
		return presenzaLogoImg;
	}



	public void setPresenzaLogoImg(boolean presenzaLogoImg) {
		this.presenzaLogoImg = presenzaLogoImg;
	}



	public boolean isPresenzaPrezzo() {
		return presenzaPrezzo;
	}



	public void setPresenzaPrezzo(boolean presenzaPrezzo) {
		this.presenzaPrezzo = presenzaPrezzo;
	}



	public String getTestoIntroduttivo() {
		return testoIntroduttivo;
	}



	public void setTestoIntroduttivo(String testoIntroduttivo) {
		this.testoIntroduttivo = testoIntroduttivo;
	}



	public String getTestoOggetto() {
		return testoOggetto;
	}



	public void setTestoOggetto(String testoOggetto) {
		this.testoOggetto = testoOggetto;
	}



	public String getTicket() {
		return ticket;
	}



	public void setTicket(String ticket) {
		this.ticket = ticket;
	}



	public String getUtente() {
		return utente;
	}



	public void setUtente(String utente) {
		this.utente = utente;
	}



	public String getListaDatiFineStampa() {
		return listaDatiFineStampa;
	}



	public void setListaDatiFineStampa(String listaDatiFineStampa) {
		this.listaDatiFineStampa = listaDatiFineStampa;
	}



	public String getListaDatiIntestazione() {
		return listaDatiIntestazione;
	}



	public void setListaDatiIntestazione(String listaDatiIntestazione) {
		this.listaDatiIntestazione = listaDatiIntestazione;
	}



	public String getDataStampa() {
		return dataStampa;
	}



	public void setDataStampa(String dataStampa) {
		this.dataStampa = dataStampa;
	}



	public String getEtichettaRistampa() {
		return etichettaRistampa;
	}



	public void setEtichettaRistampa(String etichettaRistampa) {
		this.etichettaRistampa = etichettaRistampa;
	}



	public String getDenoBib() {
		return denoBib;
	}



	public void setDenoBib(String denoBib) {
		this.denoBib = denoBib;
	}



	public List getListaOggDaStampare() {
		return listaOggDaStampare;
	}



	public void setListaOggDaStampare(List listaOggDaStampare) {
		this.listaOggDaStampare = listaOggDaStampare;
	}



	public String getReportErrori() {
		return reportErrori;
	}



	public void setReportErrori(String reportErrori) {
		this.reportErrori = reportErrori;
	}



	public String getValuta() {
		return valuta;
	}



	public void setValuta(String valuta) {
		this.valuta = valuta;
	}



	public String getImportoFornituraEur() {
		return importoFornituraEur;
	}



	public void setImportoFornituraEur(String importoFornituraEur) {
		this.importoFornituraEur = importoFornituraEur;
	}





}
