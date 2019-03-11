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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;

import java.util.ArrayList;
import java.util.List;

public class DatiLegameTermineClasseVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 2507920390703342795L;

	public class LegameTermineClasseVO extends DettaglioClasseVO {

		private static final long serialVersionUID = -7584097818700241460L;

		private String notaLegame;

		public LegameTermineClasseVO(DettaglioClasseVO classe) {
			copyCommonProperties(this, classe);
		}

		public String getNotaLegame() {
			return notaLegame;
		}

		public void setNotaLegame(String notaLegame) {
			this.notaLegame = notaLegame;
		}

	}

	private TipoOperazioneLegame operazione = TipoOperazioneLegame.CREA;
	private String codThesauro;
	private String did;
	private String livAut;
	private List<LegameTermineClasseVO> legami = new ArrayList<LegameTermineClasseVO>();

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public List<LegameTermineClasseVO> getLegami() {
		return legami;
	}

	public void setLegami(List<LegameTermineClasseVO> legami) {
		this.legami = legami;
	}

	public TipoOperazioneLegame getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazioneLegame operazione) {
		this.operazione = operazione;
	}

}
