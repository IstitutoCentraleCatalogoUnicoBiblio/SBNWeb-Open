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

import java.util.Date;
import java.util.List;

public class ListaSuppFornitoreVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -3484080764884328675L;
	private int elemXBlocchi;
	private String codPolo;
	private String codBibl;
	private String codFornitore;
	private String nomeFornitore;
	private String paese;
	private String codLingua;
	private String codSezione;
	private String tipoPartner;
	private String provincia;

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Nuovo Campo Regione e sua gestione
	private String regione;
	private String isbnEditore;

	private String codProfiloAcq;
	private String chiamante;
	private String locale;
	private List<FornitoreVO> selezioniChiamato;
	private String ordinamento;
	private String ticket;
	private boolean flag_canc=false;
	private String tipoRicerca="inizio";
	private boolean modalitaSif=true;
	private String chiaveFor;
	private Date dataInizioPicos;
	private Date dataFinePicos;
	private String[] tipoFornPicosArr;
	private String tipoOperazionePicos;
	private boolean stampaForn=false;


	public ListaSuppFornitoreVO (){};
	public ListaSuppFornitoreVO
				(String codP, String codB, String codForn, String nomeForn, String codProfAcq,
						String paeseForn, String tipoPForn,String provForn, String chiama, String loc)
			throws Exception {
/*		if (tit == null) {
			throw new Exception("Titolo non valido");
		}
		if (forn == null) {
			throw new Exception("Fornitore non valido");
		}
		if (bil == null) {
			throw new Exception("Bilancio non valido");
		}
		if (tipo == null) {
			throw new Exception("tipo ordine non valido");
		}*/
		this.codPolo=codP;
		this.codBibl=codB;
		this.codFornitore=codForn;
		this.nomeFornitore=nomeForn;
		this.codProfiloAcq=codProfAcq;
		this.paese=paeseForn;
		this.tipoPartner=tipoPForn ;
		this.provincia=provForn;
		this.chiamante = chiama;
		this.locale = loc;
	}
	public ListaSuppFornitoreVO(OrdiniVO o) {
		this.codPolo = o.getCodPolo();
		this.codBibl = o.getCodBibl();
		this.codFornitore = o.getAnagFornitore().getCodFornitore();
	}
	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public String getCodBibl() {
		return codBibl;
	}
	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}
	public String getCodFornitore() {
		return codFornitore;
	}
	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodProfiloAcq() {
		return codProfiloAcq;
	}
	public void setCodProfiloAcq(String codProfiloAcq) {
		this.codProfiloAcq = codProfiloAcq;
	}
	public String getNomeFornitore() {
		return nomeFornitore;
	}
	public void setNomeFornitore(String nomeFornitore) {
		this.nomeFornitore = nomeFornitore;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTipoPartner() {
		return tipoPartner;
	}
	public void setTipoPartner(String tipoPartner) {
		this.tipoPartner = tipoPartner;
	}
	public List<FornitoreVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<FornitoreVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public int getElemXBlocchi() {
		return elemXBlocchi;
	}
	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getCodLingua() {
		return codLingua;
	}
	public void setCodLingua(String codLingua) {
		this.codLingua = codLingua;
	}
	public String getCodSezione() {
		return codSezione;
	}
	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}
	public String getChiaveFor() {
		return chiaveFor;
	}
	public void setChiaveFor(String chiaveFor) {
		this.chiaveFor = chiaveFor;
	}
	public Date getDataFinePicos() {
		return dataFinePicos;
	}
	public void setDataFinePicos(Date dataFinePicos) {
		this.dataFinePicos = dataFinePicos;
	}
	public Date getDataInizioPicos() {
		return dataInizioPicos;
	}
	public void setDataInizioPicos(Date dataInizioPicos) {
		this.dataInizioPicos = dataInizioPicos;
	}
	public String[] getTipoFornPicosArr() {
		return tipoFornPicosArr;
	}
	public void setTipoFornPicosArr(String[] tipoFornPicosArr) {
		this.tipoFornPicosArr = tipoFornPicosArr;
	}
	public String getTipoOperazionePicos() {
		return tipoOperazionePicos;
	}
	public void setTipoOperazionePicos(String tipoOperazionePicos) {
		this.tipoOperazionePicos = tipoOperazionePicos;
	}
	public boolean isStampaForn() {
		return stampaForn;
	}
	public void setStampaForn(boolean stampaForn) {
		this.stampaForn = stampaForn;
	}
	public String getRegione() {
		return regione;
	}
	public void setRegione(String regione) {
		this.regione = regione;
	}
	public String getIsbnEditore() {
		return isbnEditore;
	}
	public void setIsbnEditore(String isbnEditore) {
		this.isbnEditore = isbnEditore;
	}


}
