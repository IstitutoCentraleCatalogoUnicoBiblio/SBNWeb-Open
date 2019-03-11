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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;


public class AreaDatiPropostaDiCorrezioneVO extends SerializableVO {

	// = AreaDatiPropostaDiCorrezioneVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -8542778543799306588L;
	private String codErr;
	private String testoProtocollo;

	private int idProposta;
	private String tipoAuthorityProposta;
	private String tipoMaterialeProposta;
	private String naturaProposta;
	private String idOggettoProposta;
	private String dataInserimentoPropostaDa;
	private String dataInserimentoPropostaA;
	private String statoProposta;
	private String testoProposta;
	private String mittenteProposta;
	private String useridProposta;
	private String destinatarioProposta;


	private List listaSintetica;


	public List getListaSintetica() {
		return listaSintetica;
	}
	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}
	public String getCodErr() {
		return codErr;
	}
	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}
	public String getIdOggettoProposta() {
		return idOggettoProposta;
	}
	public void setIdOggettoProposta(String idOggettoProposta) {
		this.idOggettoProposta = idOggettoProposta;
	}
	public int getIdProposta() {
		return idProposta;
	}
	public void setIdProposta(int idProposta) {
		this.idProposta = idProposta;
	}
	public String getMittenteProposta() {
		return mittenteProposta;
	}
	public void setMittenteProposta(String mittenteProposta) {
		this.mittenteProposta = mittenteProposta;
	}
	public String getTipoMaterialeProposta() {
		return tipoMaterialeProposta;
	}
	public void setTipoMaterialeProposta(String tipoMaterialeProposta) {
		this.tipoMaterialeProposta = tipoMaterialeProposta;
	}
	public String getStatoProposta() {
		return statoProposta;
	}
	public void setStatoProposta(String statoProposta) {
		this.statoProposta = statoProposta;
	}
	public String getTestoProposta() {
		return testoProposta;
	}
	public void setTestoProposta(String testoProposta) {
		this.testoProposta = testoProposta;
	}
	public String getTestoProtocollo() {
		return testoProtocollo;
	}
	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}
	public String getTipoAuthorityProposta() {
		return tipoAuthorityProposta;
	}
	public void setTipoAuthorityProposta(String tipoAuthorityProposta) {
		this.tipoAuthorityProposta = tipoAuthorityProposta;
	}
	public String getUseridProposta() {
		return useridProposta;
	}
	public void setUseridProposta(String useridProposta) {
		this.useridProposta = useridProposta;
	}
	public String getDataInserimentoPropostaA() {
		return dataInserimentoPropostaA;
	}
	public void setDataInserimentoPropostaA(String dataInserimentoPropostaA) {
		this.dataInserimentoPropostaA = dataInserimentoPropostaA;
	}
	public String getDataInserimentoPropostaDa() {
		return dataInserimentoPropostaDa;
	}
	public void setDataInserimentoPropostaDa(String dataInserimentoPropostaDa) {
		this.dataInserimentoPropostaDa = dataInserimentoPropostaDa;
	}
	public String getDestinatarioProposta() {
		return destinatarioProposta;
	}
	public void setDestinatarioProposta(String destinatarioProposta) {
		this.destinatarioProposta = destinatarioProposta;
	}
	public String getNaturaProposta() {
		return naturaProposta;
	}
	public void setNaturaProposta(String naturaProposta) {
		this.naturaProposta = naturaProposta;
	}
	}
