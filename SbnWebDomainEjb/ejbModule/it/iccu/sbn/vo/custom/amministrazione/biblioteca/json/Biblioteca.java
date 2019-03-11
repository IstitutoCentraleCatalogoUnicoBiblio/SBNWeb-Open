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
package it.iccu.sbn.vo.custom.amministrazione.biblioteca.json;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Biblioteca extends SerializableVO {

	private static final long serialVersionUID = -750204376777084617L;

	@SerializedName("anno-censimento")
	private String anno_censimento;
	@SerializedName("data-aggiornamento")
	private String data_aggiornamento;
	@SerializedName("codici-identificativi")
	private CodiciIdentificativi codici_identificativi;
	@SerializedName("denominazioni")
	private Denominazioni denominazioni;
	@SerializedName("indirizzo")
	private Indirizzo location;

	@SerializedName("contatti")
	private List<Contatto> contatti;
	@SerializedName("accesso")
	private Accesso accesso;
	@SerializedName("tipologia-amministrativa")
	private String tipologia_amministrativa;
	@SerializedName("tipologia-funzionale")
	private String tipologia_funzionale;
	@SerializedName("ente")
	private String ente;

	public String getAnno_censimento() {
		return anno_censimento;
	}

	public void setAnno_censimento(String anno_censimento) {
		this.anno_censimento = anno_censimento;
	}

	public String getData_aggiornamento() {
		return data_aggiornamento;
	}

	public void setData_aggiornamento(String data_aggiornamento) {
		this.data_aggiornamento = data_aggiornamento;
	}

	public CodiciIdentificativi getCodici_identificativi() {
		return codici_identificativi;
	}

	public void setCodici_identificativi(CodiciIdentificativi codici) {
		this.codici_identificativi = codici;
	}

	public Denominazioni getDenominazioni() {
		return denominazioni;
	}

	public void setDenominazioni(Denominazioni denominazioni) {
		this.denominazioni = denominazioni;
	}

	public Indirizzo getLocation() {
		return location;
	}

	public void setLocation(Indirizzo location) {
		this.location = location;
	}

	public List<Contatto> getContatti() {
		return contatti;
	}

	public void setContatti(List<Contatto> contatti) {
		this.contatti = contatti;
	}

	public Accesso getAccesso() {
		return accesso;
	}

	public void setAccesso(Accesso accesso) {
		this.accesso = accesso;
	}

	public String getTipologia_amministrativa() {
		return tipologia_amministrativa;
	}

	public void setTipologia_amministrativa(String tipologia_amministrativa) {
		this.tipologia_amministrativa = tipologia_amministrativa;
	}

	public String getTipologia_funzionale() {
		return tipologia_funzionale;
	}

	public void setTipologia_funzionale(String tipologia_funzionale) {
		this.tipologia_funzionale = tipologia_funzionale;
	}

	public String getEnte() {
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

}
