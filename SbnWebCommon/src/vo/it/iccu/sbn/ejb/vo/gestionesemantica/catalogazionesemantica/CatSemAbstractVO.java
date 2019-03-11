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
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.SinteticaAbstractPerLegameTitVO;

import java.util.List;

public class CatSemAbstractVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -826969761397249000L;

	private String descrizione;
	private String livelloAutorita;
	private String sogIns;
	private String sogVar;
	private String dataInserimento;
	private String dataVariazione;
	private List<SinteticaAbstractPerLegameTitVO> listaAbstract;

	public CatSemAbstractVO() {
		super();
	}

	public CatSemAbstractVO(String descrizione, String livelloAutorita,
			String sogIns, String sogVar, String dataInserimento,
			String dataVariazione,
			List<SinteticaAbstractPerLegameTitVO> listaAbstract) {
		this.descrizione = descrizione;
		this.livelloAutorita = livelloAutorita;
		this.sogIns = sogIns;
		this.sogVar = sogVar;
		this.dataInserimento = dataInserimento;
		this.dataVariazione = dataVariazione;
		this.listaAbstract = listaAbstract;
	}

	@Override
	public String getDataInserimento() {
		return dataInserimento;
	}

	@Override
	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	@Override
	public String getDataVariazione() {
		return dataVariazione;
	}

	@Override
	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getSogIns() {
		return sogIns;
	}

	public void setSogIns(String sogIns) {
		this.sogIns = sogIns;
	}

	public String getSogVar() {
		return sogVar;
	}

	public void setSogVar(String sogVar) {
		this.sogVar = sogVar;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<SinteticaAbstractPerLegameTitVO> getListaAbstract() {
		return listaAbstract;
	}

	public void setListaAbstract(
			List<SinteticaAbstractPerLegameTitVO> listaAbstract) {
		this.listaAbstract = listaAbstract;
	}

}
