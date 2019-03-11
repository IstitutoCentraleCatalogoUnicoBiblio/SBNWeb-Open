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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;

import java.util.ArrayList;
import java.util.List;

public class AutorizzazioniVO extends BaseVO {

	private static final long serialVersionUID = -2510091443391880315L;
	private String codPoloAutor;
	private String codBibAutor;
	private String codTipoAutor;
	private int idAutor;
	private String autorizzazione = "";
	private List<ServizioVO> servizi = new ArrayList<ServizioVO>();

	public AutorizzazioniVO() {
		this.clear();
	}

	public AutorizzazioniVO(AutorizzazioneVO aut) {
		//almaviva5_20151111 servizi ill
		this.codPoloAutor = aut.getCodPolo();
		this.codBibAutor = aut.getCodBiblioteca();
		this.codTipoAutor = aut.getCodAutorizzazione();
		this.idAutor = aut.getIdAutorizzazione();
		this.autorizzazione = aut.getDesAutorizzazione();
		for (ElementoSinteticaServizioVO ess : aut.getElencoServizi() ) {
			servizi.add(new ServizioVO(ess));
		}

	}

	public void clear() {
		this.codPoloAutor = "";
		this.codBibAutor = "";
		this.codTipoAutor = "";
		this.idAutor = 0;
		this.autorizzazione = "";
		this.servizi.clear();
		this.setTsIns(null);
		this.setTsVar(null);
		this.setUteIns(null);
		this.setUteVar(null);
		this.setFlCanc(null);
	}

	public void setServizi(List<ServizioVO> servizi) {
		this.servizi = servizi;
	}

	public List<ServizioVO> getServizi() {
		return servizi;
	}

	public List<ServizioVO> getListaServizi() {
		return servizi;
	}

	public ServizioVO getListaServizi(int index) {
        // automatically grow List size
        while (index >= this.servizi.size()) {
        	this.servizi.add(new ServizioVO());
        }
        return this.servizi.get(index);
    }

	public String getAutorizzazione() {
		return autorizzazione;
	}

	public void setAutorizzazione(String autorizzazione) {
		this.autorizzazione = autorizzazione;
	}

	public String getCodTipoAutor() {
		return codTipoAutor;
	}

	public void setCodTipoAutor(String codTipoAutor) {
		this.codTipoAutor = codTipoAutor;
	}

	public String getCodBibAutor() {
		return codBibAutor;
	}

	public void setCodBibAutor(String codBibAutor) {
		this.codBibAutor = codBibAutor;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AutorizzazioniVO other = (AutorizzazioniVO) obj;
		if (autorizzazione == null) {
			if (other.autorizzazione != null)
				return false;
		} else if (!autorizzazione.equals(other.autorizzazione))
			return false;
		if (codBibAutor == null) {
			if (other.codBibAutor != null)
				return false;
		} else if (!codBibAutor.equals(other.codBibAutor))
			return false;
		if (codTipoAutor == null) {
			if (other.codTipoAutor != null)
				return false;
		} else if (!codTipoAutor.equals(other.codTipoAutor))
			return false;
		if (servizi == null) {
			if (other.servizi != null)
				return false;
		} else if (!listEquals(this.servizi, other.servizi, ServizioVO.class))
			return false;
		return true;
	}

	public String getCodPoloAutor() {
		return codPoloAutor;
	}

	public void setCodPoloAutor(String codPoloAutor) {
		this.codPoloAutor = codPoloAutor;
	}

	public int getIdAutor() {
		return idAutor;
	}

	public void setIdAutor(int idAutor) {
		this.idAutor = idAutor;
	}

}
