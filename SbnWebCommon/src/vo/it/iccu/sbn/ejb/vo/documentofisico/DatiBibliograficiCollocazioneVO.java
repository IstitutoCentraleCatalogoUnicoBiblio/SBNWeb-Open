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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;

public class DatiBibliograficiCollocazioneVO extends SerializableVO {

	private static final long serialVersionUID = 3711417395188918566L;

	private TreeElementViewTitoli analitica;
	private CollocazioneTitoloVO titoloGenerale;
	private CollocazioneTitoloVO[] listaTitoliCollocazione;
	private CollocazioneTitoloVO[] listaTitoliLocalizzazione;
	private CollocazioneTitoloVO[] listaTitoliLocalizzazioneNonPosseduti;

	public DatiBibliograficiCollocazioneVO(TreeElementViewTitoli titoli,
			CollocazioneTitoloVO titoloGenerale,
			CollocazioneTitoloVO[] listaTitoliCollocazione,
			CollocazioneTitoloVO[] listaTitoliLocalizzazione,
			CollocazioneTitoloVO[] listaTitoliLocalizzazioneNonPosseduti) {
		this.analitica = titoli;
		this.titoloGenerale = titoloGenerale;
		this.listaTitoliCollocazione = listaTitoliCollocazione;
		this.listaTitoliLocalizzazione = listaTitoliLocalizzazione;
		this.listaTitoliLocalizzazioneNonPosseduti = listaTitoliLocalizzazioneNonPosseduti;
	}

	public DatiBibliograficiCollocazioneVO() {
		super();
	}

	public TreeElementViewTitoli getAnalitica() {
		return analitica;
	}

	public CollocazioneTitoloVO[] getListaTitoliCollocazione() {
		return listaTitoliCollocazione;
	}

	public CollocazioneTitoloVO getTitoloGenerale() {
		return titoloGenerale;
	}

	public CollocazioneTitoloVO[] getListaTitoliLocalizzazione() {
		return listaTitoliLocalizzazione;
	}

	public CollocazioneTitoloVO[] getListaTitoliLocalizzazioneNonPosseduti() {
		return listaTitoliLocalizzazioneNonPosseduti;
	}

}
