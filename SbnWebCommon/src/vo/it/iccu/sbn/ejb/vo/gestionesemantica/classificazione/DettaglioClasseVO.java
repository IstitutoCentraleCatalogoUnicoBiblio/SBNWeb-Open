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

package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.gestionesemantica.DettaglioOggettoSemanticaVO;

public class DettaglioClasseVO extends DettaglioOggettoSemanticaVO {

	private static final long serialVersionUID = 1932875470127954955L;
	private String identificativo;
	private String descrizione;
	private String campoSistema;
	private String campoEdizione;
	private String indicatore;
	private String ulterioreTermine;
	private String simbolo;
	private boolean costruito;
	private int numTitoliPolo;
	private int numTitoliBiblio;

	// Dati eventuali per legami
	private String idPadre;
	private String descrizionePadre;
	private String tipoLegameValore;

	private String tipoLegame;
	private SimboloDeweyVO simboloDewey = new SimboloDeweyVO();

	public DettaglioClasseVO(CreaVariaClasseVO classe) throws ValidationException {
		copyCommonProperties(this, classe);
		this.setLivAut(classe.getLivello());
		this.setCampoSistema(classe.getCodSistemaClassificazione());
		this.setCampoEdizione(classe.getCodEdizioneDewey());
		this.setDataAgg(classe.getDataVariazione());
		this.setDataIns(classe.getDataInserimento());
		this.setIndicatore("NO");
		this.setIdentificativo(classe.getT001());
	}

	public DettaglioClasseVO() {
		super();
	}

	public String getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}

	public String getDescrizionePadre() {
		return descrizionePadre;
	}

	public void setDescrizionePadre(String descrizionePadre) {
		this.descrizionePadre = descrizionePadre;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) throws ValidationException {
		this.identificativo = trimAndSet(identificativo);
		this.simboloDewey = SimboloDeweyVO.parse(this.identificativo);
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getTipoLegameValore() {
		return tipoLegameValore;
	}

	public void setTipoLegameValore(String tipoLegameValore) {
		this.tipoLegameValore = tipoLegameValore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public String getCampoEdizione() {
		return campoEdizione;
	}

	public void setCampoEdizione(String campoEdizione) {
		this.campoEdizione = trimOrFill(campoEdizione, ' ', 2);
	}

	public String getCampoSistema() {
		return campoSistema;
	}

	public void setCampoSistema(String campoSistema) {
		this.campoSistema = campoSistema;
	}

	public String getIndicatore() {
		return indicatore;
	}

	public void setIndicatore(String indicatore) {
		this.indicatore = indicatore;
	}

	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		this.ulterioreTermine = trimAndSet(ulterioreTermine);
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = trimAndSet(simbolo);
	}

	public boolean isCostruito() {
		return costruito;
	}

	public void setCostruito(boolean costruito) {
		this.costruito = costruito;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public SimboloDeweyVO getSimboloDewey() {
		return simboloDewey;
	}

	public void setSimboloDewey(SimboloDeweyVO simboloDewey) {
		this.simboloDewey = simboloDewey;
	}

}
