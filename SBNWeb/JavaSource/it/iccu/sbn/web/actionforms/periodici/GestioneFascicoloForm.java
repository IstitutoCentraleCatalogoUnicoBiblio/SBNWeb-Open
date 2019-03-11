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
package it.iccu.sbn.web.actionforms.periodici;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;

import java.util.List;

public class GestioneFascicoloForm extends PeriodiciListaForm {


	private static final long serialVersionUID = -9049748537354286795L;
	private FascicoloVO fascicolo;
	private FascicoloVO oldFascicolo;
	private FascicoloVO fascicoloConferma;
	private int ulterioriFascicoli;

	private List<TB_CODICI> listaTipoFascicolo;
	private List<TB_CODICI> listaPeriodicita;

	private int posizione;


	public FascicoloVO getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloVO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public List<TB_CODICI> getListaTipoFascicolo() {
		return listaTipoFascicolo;
	}

	public void setListaTipoFascicolo(List<TB_CODICI> listaTipoFascicolo) {
		this.listaTipoFascicolo = listaTipoFascicolo;
	}

	public List<TB_CODICI> getListaPeriodicita() {
		return listaPeriodicita;
	}

	public void setListaPeriodicita(List<TB_CODICI> listaPeriodicita) {
		this.listaPeriodicita = listaPeriodicita;
	}

	public FascicoloVO getOldFascicolo() {
		return oldFascicolo;
	}

	public void setOldFascicolo(FascicoloVO oldFascicolo) {
		this.oldFascicolo = oldFascicolo;
	}

	public FascicoloVO getFascicoloConferma() {
		return fascicoloConferma;
	}

	public void setFascicoloConferma(FascicoloVO fascicoloConferma) {
		this.fascicoloConferma = fascicoloConferma;
	}

	public int getUlterioriFascicoli() {
		return ulterioriFascicoli;
	}

	public void setUlterioriFascicoli(int ulterioriFascicoli) {
		this.ulterioriFascicoli = ulterioriFascicoli;
	}

	public int getPosizione() {
		return posizione;
	}

	public void setPosizione(int posizione) {
		this.posizione = posizione;
	}

}
