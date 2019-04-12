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
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaLocalizzazioniView;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioLocalizzazioneForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 4147053348841553453L;
	private SinteticaLocalizzazioniView localizzazione = new SinteticaLocalizzazioniView();
	private String tipoProspettazione;

	private String idOggColl;
	private String descOggColl;

	private String centroSistema;
	private String livRic;

	private List listaMutiloM;
	private List listaFormatoElettr;
	private List listaTipoDigital;

	private AreePassaggioSifVO areePassSifVo = new AreePassaggioSifVO();

	// Inizio dati per l'inserimento dei dati del Doc-Fisico in Indice
	private String aggConsistenza;
	private String segnaturaPolo;
	private String consistenzaPolo;
	private String uriPolo;

	private String codTipoDigitPolo;
	private String descTipoDigitPolo;

	AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla = new AreaDatiLocalizzazioniAuthorityMultiplaVO();

	// Fine dati per l'inserimento dei dati del Doc-Fisico in Indice

	public AreePassaggioSifVO getAreePassSifVo() {
		return areePassSifVo;
	}

	public void setAreePassSifVo(AreePassaggioSifVO areePassSifVo) {
		this.areePassSifVo = areePassSifVo;
	}

	public String getCentroSistema() {
		return centroSistema;
	}

	public void setCentroSistema(String centroSistema) {
		this.centroSistema = centroSistema;
	}

	public String getDescOggColl() {
		return descOggColl;
	}

	public void setDescOggColl(String descOggColl) {
		this.descOggColl = descOggColl;
	}

	public String getIdOggColl() {
		return idOggColl;
	}

	public void setIdOggColl(String idOggColl) {
		this.idOggColl = idOggColl;
	}

	public SinteticaLocalizzazioniView getLocalizzazione() {
		return localizzazione;
	}

	public void setLocalizzazione(SinteticaLocalizzazioniView eleSinteticaLocalizzazioniView) {
		this.localizzazione = eleSinteticaLocalizzazioniView;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public String getLivRic() {
		return livRic;
	}

	public void setLivRic(String livRic) {
		this.livRic = livRic;
	}

	public List getListaFormatoElettr() {
		return listaFormatoElettr;
	}

	public void setListaFormatoElettr(List listaFormatoElettr) {
		this.listaFormatoElettr = listaFormatoElettr;
	}

	public List getListaMutiloM() {
		return listaMutiloM;
	}

	public void setListaMutiloM(List listaMutiloM) {
		this.listaMutiloM = listaMutiloM;
	}

	public List getListaTipoDigital() {
		return listaTipoDigital;
	}

	public void setListaTipoDigital(List listaTipoDigital) {
		this.listaTipoDigital = listaTipoDigital;
	}

	public String getConsistenzaPolo() {
		return consistenzaPolo;
	}

	public void setConsistenzaPolo(String consistenzaPolo) {
		this.consistenzaPolo = consistenzaPolo;
	}

	public String getSegnaturaPolo() {
		return segnaturaPolo;
	}

	public void setSegnaturaPolo(String segnaturaPolo) {
		this.segnaturaPolo = segnaturaPolo;
	}

	public String getAggConsistenza() {
		return aggConsistenza;
	}

	public void setAggConsistenza(String aggConsistenza) {
		this.aggConsistenza = aggConsistenza;
	}

	public String getUriPolo() {
		return uriPolo;
	}

	public void setUriPolo(String uriPolo) {
		this.uriPolo = uriPolo;
	}

	public String getCodTipoDigitPolo() {
		return codTipoDigitPolo;
	}

	public void setCodTipoDigitPolo(String codTipoDigitPolo) {
		this.codTipoDigitPolo = codTipoDigitPolo;
	}

	public String getDescTipoDigitPolo() {
		return descTipoDigitPolo;
	}

	public void setDescTipoDigitPolo(String descTipoDigitPolo) {
		this.descTipoDigitPolo = descTipoDigitPolo;
	}

	public AreaDatiLocalizzazioniAuthorityMultiplaVO getAreaLocalizzaMultipla() {
		return areaLocalizzaMultipla;
	}

	public void setAreaLocalizzaMultipla(AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla) {
		this.areaLocalizzaMultipla = areaLocalizzaMultipla;
	}

}
