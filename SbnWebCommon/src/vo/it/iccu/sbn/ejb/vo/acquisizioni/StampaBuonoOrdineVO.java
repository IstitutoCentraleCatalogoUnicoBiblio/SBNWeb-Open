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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;

import java.util.List;

public class StampaBuonoOrdineVO extends SerializableVO {

	private static final long serialVersionUID = 8765529153719527992L;

	private String ticket;
	private String utente;
	private String codPolo;
	private String codBibl;
	private List<OrdiniVO> listaOrdiniDaStampare;
	private ConfigurazioneBOVO configurazione;
	private List<BuoniOrdineVO> listaBuoniOrdineDaStampare;

	private StampaType tipoStampa;

	public StampaBuonoOrdineVO() {
	};

	public StampaBuonoOrdineVO(String codP, String codB) throws Exception {
		// if (annoF == null) {
		// throw new Exception("Anno fattura non valido");
		// }
		this.codPolo = codP;
		this.codBibl = codB;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public ConfigurazioneBOVO getConfigurazione() {
		return configurazione;
	}

	public void setConfigurazione(ConfigurazioneBOVO configurazione) {
		this.configurazione = configurazione;
	}

	public List<OrdiniVO> getListaOrdiniDaStampare() {
		return listaOrdiniDaStampare;
	}

	public void setListaOrdiniDaStampare(
			List<OrdiniVO> listaOrdiniDaStampare) {
		this.listaOrdiniDaStampare = listaOrdiniDaStampare;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public List<BuoniOrdineVO> getListaBuoniOrdineDaStampare() {
		return listaBuoniOrdineDaStampare;
	}

	public void setListaBuoniOrdineDaStampare(
			List<BuoniOrdineVO> listaBuoniOrdineDaStampare) {
		this.listaBuoniOrdineDaStampare = listaBuoniOrdineDaStampare;
	}

	public StampaType getTipoStampa() {
		return tipoStampa;
	}

	public void setTipoStampa(StampaType tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

}
