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
package it.iccu.sbn.vo.custom.periodici;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.vo.xml.binding.sbnwebws.CollocazioneType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.InventarioType;

import java.util.List;

public class KardexPeriodicoOpacVO extends KardexPeriodicoVO {

	private static final long serialVersionUID = 6110930250907614797L;

	private List<InventarioType> inventari = ValidazioneDati.emptyList();
	private CollocazioneType collocazione;
	private InventarioType inventario;

	public KardexPeriodicoOpacVO(SeriePeriodicoType tipo) {
		super(tipo);
	}

	public List<InventarioType> getInventari() {
		return inventari;
	}

	public void setInventari(List<InventarioType> inventari) {
		this.inventari = inventari;
	}

	public CollocazioneType getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(CollocazioneType collocazione) {
		this.collocazione = collocazione;
	}

	public InventarioType getInventario() {
		return inventario;
	}

	public void setInventario(InventarioType inventario) {
		this.inventario = inventario;
	}

}
