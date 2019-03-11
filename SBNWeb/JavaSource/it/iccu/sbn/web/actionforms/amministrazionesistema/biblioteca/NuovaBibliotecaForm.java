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
package it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class NuovaBibliotecaForm extends ActionForm {


	private static final long serialVersionUID = -7328777743387585341L;
	private String id;
	private String nome;
	private String indirizzo;
	private String polo;
	private String codBib;
	private String codAna;
	private String unitaOrganizzativa;
	private String casPostale;
	private String cap;
	private String telefono;
	private String prefissoTel;
	private String fax;
	private String prefissoFax;
	private String note;
	private String iva;
	private String codFiscale;
	private String email;
	private List<ComboVO> elencoPaesi = new ArrayList<ComboVO>();
	private String paese;
	private String codCs;
	private String citta;
	private List<ComboVO> elencoProvince = new ArrayList<ComboVO>();
	private String selProvincia = "";
	private List<ComboVO> elencoGruppi = new ArrayList<ComboVO>();
	private String selGruppo = "";
	private List<ComboVO> elencoTipiBib = new ArrayList<ComboVO>();
	private String selTipoBib = "";
	private List<ComboVO> elencoBibCentroSistema = new ArrayList<ComboVO>();
	private String selBibCentroSistema = "";
	private String checkFlag;
	private String checkCentro;
	private List<ComboVO> elencoDug = new ArrayList<ComboVO>();
	private String selDug = "";

	private boolean salvato;
	private String nuovo;
	private String abilitato;
	private String inPolo = "";
	private String abilitatoNuovo;
	private String abilitatoProfilo;

	private String provenienza;
	private List<ComboVO> elencoMetro;
	private String codSistemaMetro;
	private BibliotecaVO biblioteca = new BibliotecaVO();

	private boolean abilitataILL;
	private List<ComboVO> listaRuoloILL = new ArrayList<ComboVO>();
	private List<ComboVO> listaAdesioneILL = new ArrayList<ComboVO>();

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(String abilitato) {
		this.abilitato = abilitato;
	}

	public boolean isSalvato() {
		return salvato;
	}

	public void setSalvato(boolean salvato) {
		this.salvato = salvato;
	}

	public String getNuovo() {
		return nuovo;
	}

	public void setNuovo(String nuovo) {
		this.nuovo = nuovo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodAna() {
		return codAna;
	}

	public void setCodAna(String codAna) {
		this.codAna = codAna;
	}

	public String getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(String unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getCodCs() {
		return codCs;
	}

	public void setCodCs(String codCs) {
		this.codCs = codCs;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public List<ComboVO> getElencoProvince() {
		return elencoProvince;
	}

	public void setElencoProvince(List<ComboVO> elencoProvince) {
		this.elencoProvince = elencoProvince;
	}

	public String getSelProvincia() {
		return selProvincia;
	}

	public void setSelProvincia(String selProvincia) {
		this.selProvincia = selProvincia;
	}

	public List<ComboVO> getElencoTipiBib() {
		return elencoTipiBib;
	}

	public void setElencoTipiBib(List<ComboVO> elencoTipiBib) {
		this.elencoTipiBib = elencoTipiBib;
	}

	public String getSelTipoBib() {
		return selTipoBib;
	}

	public void setSelTipoBib(String selTipoBib) {
		this.selTipoBib = selTipoBib;
	}

	public List<ComboVO> getElencoDug() {
		return elencoDug;
	}

	public void setElencoDug(List<ComboVO> elencoDug) {
		this.elencoDug = elencoDug;
	}

	public String getSelDug() {
		return selDug;
	}

	public void setSelDug(String selDug) {
		this.selDug = selDug;
	}

	public String getCasPostale() {
		return casPostale;
	}

	public void setCasPostale(String casPostale) {
		this.casPostale = casPostale;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public List<ComboVO> getElencoBibCentroSistema() {
		return elencoBibCentroSistema;
	}

	public void setElencoBibCentroSistema(
			List<ComboVO> elencoBibCentroSistema) {
		this.elencoBibCentroSistema = elencoBibCentroSistema;
	}

	public String getSelBibCentroSistema() {
		return selBibCentroSistema;
	}

	public void setSelBibCentroSistema(String selBibCentroSistema) {
		this.selBibCentroSistema = selBibCentroSistema;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getPrefissoTel() {
		return prefissoTel;
	}

	public void setPrefissoTel(String prefissoTel) {
		this.prefissoTel = prefissoTel;
	}

	public String getPrefissoFax() {
		return prefissoFax;
	}

	public void setPrefissoFax(String prefissoFax) {
		this.prefissoFax = prefissoFax;
	}

	public List<ComboVO> getElencoPaesi() {
		return elencoPaesi;
	}

	public void setElencoPaesi(List<ComboVO> elencoPaesi) {
		this.elencoPaesi = elencoPaesi;
	}

	public String getInPolo() {
		return inPolo;
	}

	public void setInPolo(String inPolo) {
		this.inPolo = inPolo;
	}

	public String getCheckCentro() {
		return checkCentro;
	}

	public void setCheckCentro(String checkCentro) {
		this.checkCentro = checkCentro;
	}

	public String getAbilitatoNuovo() {
		return abilitatoNuovo;
	}

	public void setAbilitatoNuovo(String abilitatoNuovo) {
		this.abilitatoNuovo = abilitatoNuovo;
	}

	public String getAbilitatoProfilo() {
		return abilitatoProfilo;
	}

	public void setAbilitatoProfilo(String abilitatoProfilo) {
		this.abilitatoProfilo = abilitatoProfilo;
	}

	public List<ComboVO> getElencoGruppi() {
		return elencoGruppi;
	}

	public void setElencoGruppi(List<ComboVO> elencoGruppi) {
		this.elencoGruppi = elencoGruppi;
	}

	public String getSelGruppo() {
		return selGruppo;
	}

	public void setSelGruppo(String selGruppo) {
		this.selGruppo = selGruppo;
	}

	public void setElencoMetro(List<ComboVO> elencoMetro) {
		this.elencoMetro = elencoMetro;
	}

	public List<ComboVO> getElencoMetro() {
		return elencoMetro;
	}

	public String getCodSistemaMetro() {
		return codSistemaMetro;
	}

	public void setCodSistemaMetro(String codSistemaMetro) {
		this.codSistemaMetro = codSistemaMetro;
	}

	public boolean isAbilitataILL() {
		return abilitataILL;
	}

	public void setAbilitataILL(boolean abilitataILL) {
		this.abilitataILL = abilitataILL;
	}

	public BibliotecaVO getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public List<ComboVO> getListaRuoloILL() {
		return listaRuoloILL;
	}

	public void setListaRuoloILL(List<ComboVO> listaRuoloILL) {
		this.listaRuoloILL = listaRuoloILL;
	}

	public List<ComboVO> getListaAdesioneILL() {
		return listaAdesioneILL;
	}

	public void setListaAdesioneILL(List<ComboVO> listaAdesioneILL) {
		this.listaAdesioneILL = listaAdesioneILL;
	}
}
