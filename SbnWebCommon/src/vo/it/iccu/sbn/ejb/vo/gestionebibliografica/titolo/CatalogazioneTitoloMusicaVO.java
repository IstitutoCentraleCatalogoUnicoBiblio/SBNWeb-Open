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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;


public class CatalogazioneTitoloMusicaVO   extends SerializableVO {

	// = CatalogazioneTitoloMusicaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -3917918142799176598L;
	private List listaElaborazioni;
	private String elaborazioneSelez;

	private String organicoSintet;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String organicoAnalit;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private List listaPresentazione;
	private String presentazioneSelez;

	private List listaFormeMusicali;
	private String formaMusicaleSelez1;
	private String formaMusicaleSelez2;
	private String formaMusicaleSelez3;
	private List listaTonalita;
	private String tonalitaSelez;

	private String numOpera;
	//	 TODO: inserire ICONA della tastiera caratteri speciali
	private String numOrdine;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String numCatalogoTem;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String dataCompInizioDa;
	private String dataCompInizioA;
	private String dataCompFineDa;
	private String dataCompFineA;

	private String organicoSintetCompl;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String organicoAnalitCompl;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String titoloOrdinamento;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String titoloEstratto;
	//	 TODO: inserire ICONA della tastiera caratteri speciali

	private String appellativo;
	//	 TODO: inserire ICONA della tastiera caratteri speciali
	public String getAppellativo() {
		return appellativo;
	}
	public void setAppellativo(String appellativo) {
		this.appellativo = appellativo;
	}
	public String getDataCompFineA() {
		return dataCompFineA;
	}
	public void setDataCompFineA(String dataCompFineA) {
		this.dataCompFineA = dataCompFineA;
	}
	public String getDataCompFineDa() {
		return dataCompFineDa;
	}
	public void setDataCompFineDa(String dataCompFineDa) {
		this.dataCompFineDa = dataCompFineDa;
	}
	public String getDataCompInizioA() {
		return dataCompInizioA;
	}
	public void setDataCompInizioA(String dataCompInizioA) {
		this.dataCompInizioA = dataCompInizioA;
	}
	public String getDataCompInizioDa() {
		return dataCompInizioDa;
	}
	public void setDataCompInizioDa(String dataCompInizioDa) {
		this.dataCompInizioDa = dataCompInizioDa;
	}
	public String getElaborazioneSelez() {
		return elaborazioneSelez;
	}
	public void setElaborazioneSelez(String elaborazioneSelez) {
		this.elaborazioneSelez = elaborazioneSelez;
	}
	public String getFormaMusicaleSelez1() {
		return formaMusicaleSelez1;
	}
	public void setFormaMusicaleSelez1(String formaMusicaleSelez1) {
		this.formaMusicaleSelez1 = formaMusicaleSelez1;
	}
	public String getFormaMusicaleSelez2() {
		return formaMusicaleSelez2;
	}
	public void setFormaMusicaleSelez2(String formaMusicaleSelez2) {
		this.formaMusicaleSelez2 = formaMusicaleSelez2;
	}
	public String getFormaMusicaleSelez3() {
		return formaMusicaleSelez3;
	}
	public void setFormaMusicaleSelez3(String formaMusicaleSelez3) {
		this.formaMusicaleSelez3 = formaMusicaleSelez3;
	}
	public List getListaElaborazioni() {
		return listaElaborazioni;
	}
	public void setListaElaborazioni(List listaElaborazioni) {
		this.listaElaborazioni = listaElaborazioni;
	}
	public List getListaFormeMusicali() {
		return listaFormeMusicali;
	}
	public void setListaFormeMusicali(List listaFormeMusicali) {
		this.listaFormeMusicali = listaFormeMusicali;
	}
	public List getListaPresentazione() {
		return listaPresentazione;
	}
	public void setListaPresentazione(List listaPresentazione) {
		this.listaPresentazione = listaPresentazione;
	}
	public List getListaTonalita() {
		return listaTonalita;
	}
	public void setListaTonalita(List listaTonalita) {
		this.listaTonalita = listaTonalita;
	}
	public String getNumCatalogoTem() {
		return numCatalogoTem;
	}
	public void setNumCatalogoTem(String numCatalogoTem) {
		this.numCatalogoTem = numCatalogoTem;
	}
	public String getNumOpera() {
		return numOpera;
	}
	public void setNumOpera(String numOpera) {
		this.numOpera = numOpera;
	}
	public String getNumOrdine() {
		return numOrdine;
	}
	public void setNumOrdine(String numOrdine) {
		this.numOrdine = numOrdine;
	}
	public String getOrganicoAnalit() {
		return organicoAnalit;
	}
	public void setOrganicoAnalit(String organicoAnalit) {
		this.organicoAnalit = organicoAnalit;
	}
	public String getOrganicoAnalitCompl() {
		return organicoAnalitCompl;
	}
	public void setOrganicoAnalitCompl(String organicoAnalitCompl) {
		this.organicoAnalitCompl = organicoAnalitCompl;
	}
	public String getOrganicoSintet() {
		return organicoSintet;
	}
	public void setOrganicoSintet(String organicoSintet) {
		this.organicoSintet = organicoSintet;
	}
	public String getOrganicoSintetCompl() {
		return organicoSintetCompl;
	}
	public void setOrganicoSintetCompl(String organicoSintetCompl) {
		this.organicoSintetCompl = organicoSintetCompl;
	}
	public String getPresentazioneSelez() {
		return presentazioneSelez;
	}
	public void setPresentazioneSelez(String presentazioneSelez) {
		this.presentazioneSelez = presentazioneSelez;
	}
	public String getTitoloEstratto() {
		return titoloEstratto;
	}
	public void setTitoloEstratto(String titoloEstratto) {
		this.titoloEstratto = titoloEstratto;
	}
	public String getTitoloOrdinamento() {
		return titoloOrdinamento;
	}
	public void setTitoloOrdinamento(String titoloOrdinamento) {
		this.titoloOrdinamento = titoloOrdinamento;
	}
	public String getTonalitaSelez() {
		return tonalitaSelez;
	}
	public void setTonalitaSelez(String tonalitaSelez) {
		this.tonalitaSelez = tonalitaSelez;
	}




}
