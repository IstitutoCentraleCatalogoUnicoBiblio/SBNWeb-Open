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

import java.util.ArrayList;
import java.util.List;

public abstract class DatiLegameTitoloAuthSemanticaVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 7637022979227845207L;

	private TipoOperazioneLegame operazione = TipoOperazioneLegame.CREA;
	private String bid;
	private String bidLivelloAut;
	private String bidNatura;
	private String bidTipoMateriale;

	public TipoOperazioneLegame getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazioneLegame operazione) {
		this.operazione = operazione;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @deprecated Use {@link #getBidLivelloAut()} instead
	 */
	public String getLivAut() {
		return getBidLivelloAut();
	}

	public String getBidLivelloAut() {
		return bidLivelloAut;
	}

	/**
	 * @deprecated Use {@link #setBidLivelloAut(String)} instead
	 */
	public void setLivAut(String livAut) {
		setBidLivelloAut(livAut);
	}

	public void setBidLivelloAut(String livAut) {
		this.bidLivelloAut = livAut;
	}

	/**
	 * @deprecated Use {@link #getBidNatura()} instead
	 */
	public String getNatura() {
		return getBidNatura();
	}

	public String getBidNatura() {
		return bidNatura;
	}

	/**
	 * @deprecated Use {@link #setBidNatura(String)} instead
	 */
	public void setNatura(String natura) {
		setBidNatura(natura);
	}

	public void setBidNatura(String natura) {
		this.bidNatura = natura;
	}

	/**
	 * @deprecated Use {@link #getBidTipoMateriale()} instead
	 */
	public String getTipoMateriale() {
		return getBidTipoMateriale();
	}

	public String getBidTipoMateriale() {
		return bidTipoMateriale;
	}

	/**
	 * @deprecated Use {@link #setBidTipoMateriale(String)} instead
	 */
	public void setTipoMateriale(String tipoMateriale) {
		setBidTipoMateriale(tipoMateriale);
	}

	public void setBidTipoMateriale(String tipoMateriale) {
		this.bidTipoMateriale = tipoMateriale;
	}

	public List<LegameTitoloAuthSemanticaVO> getLegami() {
		return legami;
	}

	public void setLegami(List<LegameTitoloAuthSemanticaVO> legami) {
		this.legami = legami;
	}


	private List<LegameTitoloAuthSemanticaVO> legami = new ArrayList<LegameTitoloAuthSemanticaVO>();

}
