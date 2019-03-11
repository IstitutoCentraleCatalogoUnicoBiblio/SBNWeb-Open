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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StrutturaProfiloVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 4943252112757454674L;
	private Integer progressivo=0;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private StrutturaCombo profilo;
	private StrutturaCombo sezione;
	private StrutturaCombo lingua;
	private StrutturaCombo paese;
	private List listaFornitori = new ArrayList();
	private String tipoVariazione;
	private String utente;
	private boolean flag_canc=false;
	private int IDSez;
	private Timestamp dataUpd;



	public StrutturaProfiloVO (){};
	public StrutturaProfiloVO (String polo, String bibl, StrutturaCombo prof, StrutturaCombo sez, StrutturaCombo lin, StrutturaCombo pae, List listProf , String tipoVar ) throws Exception {
		if (prof == null) {
			throw new Exception("Profilo non valido");
		}
		this.codPolo = polo;
		this.codBibl = bibl;
		this.profilo = prof;
		this.sezione = sez;
		this.lingua = lin;
		this.paese = pae;
		this.listaFornitori=listProf;
		this.tipoVariazione=tipoVar;

	}

	public String getChiave() {
		String chiave=getCodBibl()+ "|" + profilo.getCodice() ;
		chiave=chiave.trim();
		return chiave;
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



	public StrutturaCombo getLingua() {
		return lingua;
	}



	public void setLingua(StrutturaCombo lingua) {
		this.lingua = lingua;
	}



	public StrutturaCombo getPaese() {
		return paese;
	}



	public void setPaese(StrutturaCombo paese) {
		this.paese = paese;
	}



	public StrutturaCombo getProfilo() {
		return profilo;
	}



	public void setProfilo(StrutturaCombo profilo) {
		this.profilo = profilo;
	}



	public StrutturaCombo getSezione() {
		return sezione;
	}



	public void setSezione(StrutturaCombo sezione) {
		this.sezione = sezione;
	}



	public List getListaFornitori() {
		return listaFornitori;
	}



	public void setListaFornitori(List listaFornitori) {
		this.listaFornitori = listaFornitori;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoVariazione() {
		return tipoVariazione;
	}
	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}
	public Integer getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public int getIDSez() {
		return IDSez;
	}
	public void setIDSez(int sez) {
		IDSez = sez;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}


}
