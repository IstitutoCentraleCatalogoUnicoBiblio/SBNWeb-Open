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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class AreaDatiAccorpamentoVO extends SerializableVO {

	private static final long serialVersionUID = -7268615520635742437L;

	private String  idElementoEliminato;
	private String  idElementoAccorpante;
	private String	tipoMateriale;
	private String	livelloBaseDati;

	SbnAuthority tipoAuthority;

	// ID dei titoli collegati da trasferire se = null è accorpamento altrimenti è spostamento
	String[] idTitoliLegati;

//	 Nel caso di true si deve cattuarae da Indice il bid di arrivo ma non fondere in Indice ma solo in Polo
	private boolean	chiamataAllineamento;

//	 Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797 - si identifica la chiamata per fusione massiva perchè
//   il bid da eliminare deve essere marcato come solo locale (non condiviso)
	private boolean	chiamataFusioneMassiva;


	public AreaDatiAccorpamentoVO() {
		super();
		chiamataAllineamento = false;
		chiamataFusioneMassiva = false;
	}
	public String getIdElementoAccorpante() {
		return idElementoAccorpante;
	}
	public void setIdElementoAccorpante(String idElementoAccorpante) {
		this.idElementoAccorpante = idElementoAccorpante;
	}
	public String getIdElementoEliminato() {
		return idElementoEliminato;
	}
	public void setIdElementoEliminato(String idElementoEliminato) {
		this.idElementoEliminato = idElementoEliminato;
	}
	public SbnAuthority getTipoAuthority() {
		return tipoAuthority;
	}
	public void setTipoAuthority(SbnAuthority tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}
	public String getTipoMateriale() {
		return tipoMateriale;
	}
	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}
	public String[] getIdTitoliLegati() {
		return idTitoliLegati;
	}
	public void setIdTitoliLegati(String[] idTitoliLegati) {
		this.idTitoliLegati = idTitoliLegati;
	}
	public String getLivelloBaseDati() {
		return livelloBaseDati;
	}
	public void setLivelloBaseDati(String livelloBaseDati) {
		this.livelloBaseDati = livelloBaseDati;
	}
	public boolean isChiamataAllineamento() {
		return chiamataAllineamento;
	}
	public void setChiamataAllineamento(boolean chiamataAllineamento) {
		this.chiamataAllineamento = chiamataAllineamento;
	}
	public boolean isChiamataFusioneMassiva() {
		return chiamataFusioneMassiva;
	}
	public void setChiamataFusioneMassiva(boolean chiamataFusioneMassiva) {
		this.chiamataFusioneMassiva = chiamataFusioneMassiva;
	}



}
