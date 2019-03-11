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
package it.iccu.sbn.web.vo;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.vaia.Condiviso;
import it.iccu.sbn.web.vo.vaia.LivelloRicerca;
import it.iccu.sbn.web.vo.vaia.Localizzazione;
import it.iccu.sbn.web.vo.vaia.RootReticolo;
import it.iccu.sbn.web.vo.vaia.TipoOggetto;

import java.util.HashMap;
import java.util.Map;

public class DescrizioneFunzioneVO extends SerializableVO {

	private static final long serialVersionUID = 955916183730415231L;

	private String codice;

	private Map<String, String> descrizione = new HashMap<String, String>();

	private String ActionPath;

	private TipoOggetto tipo;

	private LivelloRicerca livello;

	private Localizzazione loc;

	private Condiviso condiviso;

	private RootReticolo rootReticolo;

	public DescrizioneFunzioneVO() {
		super();
	}

	public DescrizioneFunzioneVO(TipoOggetto tipo, LivelloRicerca livello,
			Localizzazione loc, Condiviso condiviso, RootReticolo rootReticolo) {
		super();
		this.tipo = tipo;
		this.livello = livello;
		this.loc = loc;
		this.condiviso = condiviso;
		this.rootReticolo = rootReticolo;
	}

	public String getKey() {
		return tipo + "_" + livello + "_" + loc + "_" + condiviso + "_"
				+ rootReticolo;
	}

	public String getActionPath() {
		return ActionPath;
	}

	public void setActionPath(String actionPath) {
		ActionPath = actionPath;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione(String codLingua) {
		return descrizione.get(codLingua);
	}

	public void addDescrizione(String codLingua, String descrizione) {
		this.descrizione.put(codLingua, descrizione);
	}

	public Condiviso getCondiviso() {
		return condiviso;
	}

	public void setCondiviso(Condiviso condiviso) {
		this.condiviso = condiviso;
	}

	public LivelloRicerca getLivello() {
		return livello;
	}

	public void setLivello(LivelloRicerca livello) {
		this.livello = livello;
	}

	public Localizzazione getLoc() {
		return loc;
	}

	public void setLoc(Localizzazione loc) {
		this.loc = loc;
	}

	public TipoOggetto getTipo() {
		return tipo;
	}

	public void setTipo(TipoOggetto tipo) {
		this.tipo = tipo;
	}

	public RootReticolo getRootReticolo() {
		return rootReticolo;
	}

	public void setRootReticolo(RootReticolo rootReticolo) {
		this.rootReticolo = rootReticolo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ActionPath == null) ? 0 : ActionPath.hashCode());
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result
				+ ((condiviso == null) ? 0 : condiviso.hashCode());
		result = prime * result + ((livello == null) ? 0 : livello.hashCode());
		result = prime * result + ((loc == null) ? 0 : loc.hashCode());
		result = prime * result
				+ ((rootReticolo == null) ? 0 : rootReticolo.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DescrizioneFunzioneVO other = (DescrizioneFunzioneVO) obj;
		if (ActionPath == null) {
			if (other.ActionPath != null)
				return false;
		} else if (!ActionPath.equals(other.ActionPath))
			return false;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (condiviso == null) {
			if (other.condiviso != null)
				return false;
		} else if (!condiviso.equals(other.condiviso))
			return false;
		if (livello == null) {
			if (other.livello != null)
				return false;
		} else if (!livello.equals(other.livello))
			return false;
		if (loc == null) {
			if (other.loc != null)
				return false;
		} else if (!loc.equals(other.loc))
			return false;
		if (rootReticolo == null) {
			if (other.rootReticolo != null)
				return false;
		} else if (!rootReticolo.equals(other.rootReticolo))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

}
