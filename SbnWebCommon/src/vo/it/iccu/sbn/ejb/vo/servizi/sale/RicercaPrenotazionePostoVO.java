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
package it.iccu.sbn.ejb.vo.servizi.sale;

import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

public class RicercaPrenotazionePostoVO extends PrenotazionePostoVO {

	private static final long serialVersionUID = 4390180134104802770L;

	private String ordinamento;
	private int elementiPerBlocco = Integer.parseInt(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());

	private InventarioVO inventario;
	private boolean chiuse;
	private boolean respinte;
	private boolean scadute;

	private List<String> listaCatMediazione = new ArrayList<String>();
	private int maxRichiesteAssociate;
	private boolean dettaglioCompleto = false;

	private boolean escludiPrenotSenzaSupporto;
	private boolean escludiPrenotConSupporto;

	private List<Integer> prenotazioniEscluse = new ArrayList<Integer>();

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(int elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}

	public InventarioVO getInventario() {
		return inventario;
	}

	public void setInventario(InventarioVO inventario) {
		this.inventario = inventario;
	}

	public boolean isChiuse() {
		return chiuse;
	}

	public void setChiuse(boolean chiuse) {
		this.chiuse = chiuse;
	}

	public boolean isRespinte() {
		return respinte;
	}

	public void setRespinte(boolean respinte) {
		this.respinte = respinte;
	}

	public boolean isScadute() {
		return scadute;
	}

	public void setScadute(boolean scadute) {
		this.scadute = scadute;
	}

	public List<String> getListaCatMediazione() {
		return listaCatMediazione;
	}

	public void setListaCatMediazione(List<String> listaCatMediazione) {
		this.listaCatMediazione = listaCatMediazione;
	}

	public int getMaxRichiesteAssociate() {
		return maxRichiesteAssociate;
	}

	public void setMaxRichiesteAssociate(int maxRichiesteAssociate) {
		this.maxRichiesteAssociate = maxRichiesteAssociate;
	}

	public boolean isDettaglioCompleto() {
		return dettaglioCompleto;
	}

	public void setDettaglioCompleto(boolean dettaglioCompleto) {
		this.dettaglioCompleto = dettaglioCompleto;
	}

	public boolean isEscludiPrenotSenzaSupporto() {
		return escludiPrenotSenzaSupporto;
	}

	public void setEscludiPrenotSenzaSupporto(boolean escludiPrenotSenzaSupporto) {
		this.escludiPrenotSenzaSupporto = escludiPrenotSenzaSupporto;
	}

	public boolean isEscludiPrenotConSupporto() {
		return escludiPrenotConSupporto;
	}

	public void setEscludiPrenotConSupporto(boolean escludiPrenotConSupporto) {
		this.escludiPrenotConSupporto = escludiPrenotConSupporto;
	}

	public List<Integer> getPrenotazioniEscluse() {
		return prenotazioniEscluse;
	}

	public void setPrenotazioniEscluse(List<Integer> prenotazioniEscluse) {
		this.prenotazioniEscluse = prenotazioniEscluse;
	}

}
