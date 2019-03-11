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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.List;

public class CreaVariaClasseVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -7608934677709292265L;
	private String simbolo;
	private String descrizione;
	private String livello;
	private String ulterioreTermine;
	private String codSistemaClassificazione;
	private String codEdizioneDewey;
	private String action;
	private String T001;
	private boolean forzaCreazione = false;
	private List<SinteticaClasseVO> listaSimili;
	private boolean costruito = false;
	private int numTitoliPolo;
	private int numTitoliBiblio;
	private short rank;

	public short getRank() {
		return rank;
	}

	public void setRank(short rank) {
		this.rank = rank;
	}

	public String getT001() {
		return T001;
	}

	public void setT001(String t001) {
		T001 = t001;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCodEdizioneDewey() {
		return codEdizioneDewey;
	}

	public void setCodEdizioneDewey(String codEdizioneDewey) {
		this.codEdizioneDewey = trimOrFill(codEdizioneDewey, ' ', 2);
	}

	public String getCodSistemaClassificazione() {
		return codSistemaClassificazione;
	}

	public void setCodSistemaClassificazione(String codSistemaClassificazione) {
		this.codSistemaClassificazione = codSistemaClassificazione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public String getLivello() {
		return livello;
	}

	public void setLivello(String livello) {
		this.livello = livello;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = trimAndSet(simbolo);
	}

	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		this.ulterioreTermine = trimAndSet(ulterioreTermine);
	}

	public boolean isForzaCreazione() {
		return forzaCreazione;
	}

	public void setForzaCreazione(boolean forzaCreazione) {
		this.forzaCreazione = forzaCreazione;
	}

	public List<SinteticaClasseVO> getListaSimili() {
		return listaSimili;
	}

	public void setListaSimili(List<SinteticaClasseVO> listaSimili) {
		this.listaSimili = listaSimili;
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

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (!isFilled(simbolo) )
			throw new ValidationException("Digitare il campo simbolo");

		if (length(descrizione) > 240)
			throw new ValidationException("La descrizione supera i 240 caratteri");

		if (length(ulterioreTermine) > 240)
			throw new ValidationException("L'Ulteriore Termine supera i 240 caratteri");

		if (!isFilled(livello) )
			throw new ValidationException("Digitare il campo stato di controllo");
	}

}
