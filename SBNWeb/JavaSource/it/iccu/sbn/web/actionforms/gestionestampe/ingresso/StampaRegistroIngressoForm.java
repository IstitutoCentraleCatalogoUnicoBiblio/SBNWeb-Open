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
package it.iccu.sbn.web.actionforms.gestionestampe.ingresso;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

public class StampaRegistroIngressoForm extends RicercaInventariCollocazioniForm {

	private static final long serialVersionUID = 5060902560417669946L;
	private StampaRegistroVO regGener = new StampaRegistroVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
	private String codPolo;
	private String codBib;
	private String tipoModello;
	private String tipoDiStampa;
	private String regingroCodiceBiblioteca;
	private String regingroSerieInventariale;
	private String regingroCodInventarioDa;
	private String regingroCodInventarioA;
	private String regingroDataDa;
	private String regingroDataA;
	private String statregCodiceBiblioteca;
	private String statregSerieInventariale;
	private String statregCodInventarioDa;
	private String statregCodInventarioA;
	private String statregDataDa;
	private String statregDataA;
	private String chkDataInventario;
	private String chkCodInventario;
	private String chkCodFornitore;
	private String chkTipoOrdine;
	private String chkTitolo;
	private String chkCodMateriale;
	private String chkValore;
	private String chkPrecisazioni;
	private String chkImporto;
	private String chkBid;
	private String chkNrFattura;
	private String chkDataFattura;
	private String chkCollocazione;
	private String codTipoOrdine;
	private List listaCodTipoOrdine;
	private String codSerie;
	public List listaSerie;
	private boolean noSerie;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private ModelloStampaVO modello;

	private String tipoFormato;
	private boolean sessione;
	private String descrBib;
	private List listaBiblioteche;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	public ModelloStampaVO getModello() {
		return modello;
	}

	public void setModello(ModelloStampaVO modello) {
		this.modello = modello;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public StampaRegistroVO getRegGener() {
		return regGener;
	}

	public void setRegGener(StampaRegistroVO regGener) {
		this.regGener = regGener;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getTipoDiStampa() {
		return tipoDiStampa;
	}
	public void setTipoDiStampa(String tipoDiStampa) {
		this.tipoDiStampa = tipoDiStampa;
	}
	public String getRegingroCodiceBiblioteca() {
		return regingroCodiceBiblioteca;
	}
	public void setRegingroCodiceBiblioteca(String regingroCodiceBiblioteca) {
		this.regingroCodiceBiblioteca = regingroCodiceBiblioteca;
	}
	public String getRegingroCodInventarioA() {
		return regingroCodInventarioA;
	}
	public void setRegingroCodInventarioA(String regingroCodInventarioA) {
		this.regingroCodInventarioA = regingroCodInventarioA;
	}
	public String getRegingroCodInventarioDa() {
		return regingroCodInventarioDa;
	}
	public void setRegingroCodInventarioDa(String regingroCodInventarioDa) {
		this.regingroCodInventarioDa = regingroCodInventarioDa;
	}
	public String getRegingroDataA() {
		return regingroDataA;
	}
	public void setRegingroDataA(String regingroDataA) {
		this.regingroDataA = regingroDataA;
	}
	public String getRegingroDataDa() {
		return regingroDataDa;
	}
	public void setRegingroDataDa(String regingroDataDa) {
		this.regingroDataDa = regingroDataDa;
	}
	public String getRegingroSerieInventariale() {
		return regingroSerieInventariale;
	}
	public void setRegingroSerieInventariale(String regingroSerieInventariale) {
		this.regingroSerieInventariale = regingroSerieInventariale;
	}
	public String getStatregCodiceBiblioteca() {
		return statregCodiceBiblioteca;
	}
	public void setStatregCodiceBiblioteca(String statregCodiceBiblioteca) {
		this.statregCodiceBiblioteca = statregCodiceBiblioteca;
	}
	public String getStatregCodInventarioA() {
		return statregCodInventarioA;
	}
	public void setStatregCodInventarioA(String statregCodInventarioA) {
		this.statregCodInventarioA = statregCodInventarioA;
	}
	public String getStatregCodInventarioDa() {
		return statregCodInventarioDa;
	}
	public void setStatregCodInventarioDa(String statregCodInventarioDa) {
		this.statregCodInventarioDa = statregCodInventarioDa;
	}
	public String getStatregDataA() {
		return statregDataA;
	}
	public void setStatregDataA(String statregDataA) {
		this.statregDataA = statregDataA;
	}
	public String getStatregDataDa() {
		return statregDataDa;
	}
	public void setStatregDataDa(String statregDataDa) {
		this.statregDataDa = statregDataDa;
	}
	public String getStatregSerieInventariale() {
		return statregSerieInventariale;
	}
	public void setStatregSerieInventariale(String statregSerieInventariale) {
		this.statregSerieInventariale = statregSerieInventariale;
	}
	public String getChkBid() {
		return chkBid;
	}
	public void setChkBid(String chkBid) {
		this.chkBid = chkBid;
	}
	public String getChkCodFornitore() {
		return chkCodFornitore;
	}
	public void setChkCodFornitore(String chkCodFornitore) {
		this.chkCodFornitore = chkCodFornitore;
	}
	public String getChkCodInventario() {
		return chkCodInventario;
	}
	public void setChkCodInventario(String chkCodInventario) {
		this.chkCodInventario = chkCodInventario;
	}
	public String getChkCodMateriale() {
		return chkCodMateriale;
	}
	public void setChkCodMateriale(String chkCodMateriale) {
		this.chkCodMateriale = chkCodMateriale;
	}
	public String getChkCollocazione() {
		return chkCollocazione;
	}
	public void setChkCollocazione(String chkCollocazione) {
		this.chkCollocazione = chkCollocazione;
	}
	public String getChkDataFattura() {
		return chkDataFattura;
	}
	public void setChkDataFattura(String chkDataFattura) {
		this.chkDataFattura = chkDataFattura;
	}
	public String getChkDataInventario() {
		return chkDataInventario;
	}
	public void setChkDataInventario(String chkDataInventario) {
		this.chkDataInventario = chkDataInventario;
	}
	public String getChkImporto() {
		return chkImporto;
	}
	public void setChkImporto(String chkImporto) {
		this.chkImporto = chkImporto;
	}
	public String getChkNrFattura() {
		return chkNrFattura;
	}
	public void setChkNrFattura(String chkNrFattura) {
		this.chkNrFattura = chkNrFattura;
	}
	public String getChkPrecisazioni() {
		return chkPrecisazioni;
	}
	public void setChkPrecisazioni(String chkPrecisazioni) {
		this.chkPrecisazioni = chkPrecisazioni;
	}
	public String getChkTipoOrdine() {
		return chkTipoOrdine;
	}
	public void setChkTipoOrdine(String chkTipoOrdine) {
		this.chkTipoOrdine = chkTipoOrdine;
	}
	public String getChkTitolo() {
		return chkTitolo;
	}
	public void setChkTitolo(String chkTitolo) {
		this.chkTitolo = chkTitolo;
	}
	public String getChkValore() {
		return chkValore;
	}
	public void setChkValore(String chkValore) {
		this.chkValore = chkValore;
	}
	public String getCodTipoOrdine() {
		return codTipoOrdine;
	}
	public void setCodTipoOrdine(String codTipoOrdine) {
		this.codTipoOrdine = codTipoOrdine;
	}
	public List getListaCodTipoOrdine() {
		return listaCodTipoOrdine;
	}
	public void setListaCodTipoOrdine(List listaCodTipoOrdine) {
		this.listaCodTipoOrdine = listaCodTipoOrdine;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public List getListaSerie() {
		return listaSerie;
	}
	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}



	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public boolean isNoSerie() {
		return noSerie;
	}

	public void setNoSerie(boolean noSerie) {
		this.noSerie = noSerie;
	}

	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}

	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public void setSelezione(String tipoCheck){
		if(tipoCheck.equals("on") || (tipoCheck.equals("null"))){
			chkDataInventario = tipoCheck;
			chkTipoOrdine = tipoCheck;
			chkValore = tipoCheck;
			chkBid = tipoCheck;
			chkCollocazione= tipoCheck;
			chkCodInventario = tipoCheck;
			chkTitolo = tipoCheck;
			chkPrecisazioni = tipoCheck;
			chkNrFattura = tipoCheck;
			chkCodFornitore = tipoCheck;
			chkCodMateriale = tipoCheck;
			chkImporto = tipoCheck;
			chkDataFattura = tipoCheck;
		}
	}

	public void setInvSelezione(String[] selCheck, String[] nameCheck){
		for (int i = 0; i < 13; i++) {
			invertiSelezione(selCheck[i], nameCheck[i]);
		}
	}

	private void invertiSelezione(String checkValue, String nameCheck){
//		String checkType = "chkDataInventario";
		if(nameCheck.equals("chkDataInventario")){
			if(checkValue == null){
				this.chkDataInventario = "on";
			}else if(checkValue.equals("on")){
				this.chkDataInventario = null;
			}
		}else if(nameCheck.equals("chkTipoOrdine")){
			if(checkValue== null){
				this.chkTipoOrdine = "on";
			}else if(checkValue.equals("on")){
				this.chkTipoOrdine = null;
			}
		}else if(nameCheck.equals("chkValore")){
			if(checkValue == null){
				this.chkValore = "on";
			}else if(checkValue.equals("on")){
				this.chkValore = null;
			}
		}else if(nameCheck.equals("chkBid")){
			if(checkValue == null){
				this.chkBid = "on";
			}else if(checkValue.equals("on")){
				this.chkBid = null;
			}
		}else if(nameCheck.equals("chkCollocazione")){
			if(checkValue == null){
				this.chkCollocazione = "on";
			}else if(checkValue.equals("on")){
				this.chkCollocazione = null;
			}
		}else if(nameCheck.equals("chkCodInventario")){
			if(checkValue ==null){
				this.chkCodInventario = "on";
			}else if(checkValue.equals("on")){
				this.chkCodInventario = null;
			}
		}else if(nameCheck.equals("chkTitolo")){
			if(checkValue == null){
				this.chkTitolo = "on";
			}else if(checkValue.equals("on")){
				this.chkTitolo = null;
			}
		}else if(nameCheck.equals("chkPrecisazioni")){
			if(checkValue == null){
				this.chkPrecisazioni = "on";
			}else if(checkValue.equals("on")){
				this.chkPrecisazioni = null;
			}
		}else if(nameCheck.equals("chkNrFattura")){
			if(checkValue == null){
				this.chkNrFattura = "on";
			}else if(checkValue.equals("on")){
				this.chkNrFattura = null;
			}
		}else if(nameCheck.equals("chkCodFornitore")){
			if(checkValue == null){
				this.chkCodFornitore = "on";
			}else if(checkValue.equals("on")){
				this.chkCodFornitore = null;
			}
		}else if(nameCheck.equals("chkCodMateriale")){
			if(checkValue == null){
				this.chkCodMateriale = "on";
			}else if(checkValue.equals("on")){
				this.chkCodMateriale = null;
			}
		}else if(nameCheck.equals("chkImporto")){
			if(checkValue == null){
				this.chkImporto = "on";
			}else if(checkValue.equals("on")){
				this.chkImporto = null;
			}
		}else if(nameCheck.equals("chkDataFattura")){
			if(checkValue == null){
				this.chkDataFattura = "on";
			}else if(checkValue.equals("on")){
				this.chkDataFattura = null;
			}
		}
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
}
