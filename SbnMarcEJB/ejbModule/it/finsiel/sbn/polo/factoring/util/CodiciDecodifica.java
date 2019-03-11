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
package it.finsiel.sbn.polo.factoring.util;

/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CodiciDecodifica {
	private String _codice; //questo è cd_tabella
	private String _descrizione;
	private String _tp_tabella;
	private String _cd_unimarc;
	private String	_cd_marc_21;
	private String	_tp_materiale;

	public String getCd_unimarc(){
		return _cd_unimarc;
	}

	public void setCd_unimarc(String cd_unimarc){
		this._cd_unimarc = cd_unimarc;
	}


	public String getCd_marc_21(){
		return _cd_marc_21;
	}

	public void setCd_marc_21(String cd_marc_21){
		this._cd_marc_21 = cd_marc_21;
	}

	public String getTp_materiale(){
		return _tp_materiale;
	}

	public void setTp_materiale(String tp_materiale){
		this._tp_materiale = tp_materiale;
	}


	/**
	 * Returns the codice.
	 * @return String
	 */
	public String getCodice() {
		return _codice;
	}

	/**
	 * Returns the descrizione.
	 * @return String
	 */
	public String getDescrizione() {
		return _descrizione;
	}

	/**
	 * Sets the codice.
	 * @param codice The codice to set
	 */
	public void setCodice(String codice) {
		this._codice = codice;
	}

	/**
	 * Sets the descrizione.
	 * @param descrizione The descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this._descrizione = descrizione;
	}

	/**
	 * Returns the tp_tabella.
	 * @return String
	 */
	public String getTp_tabella() {
		return _tp_tabella;
	}

	/**
	 * Sets the tp_tabella.
	 * @param tp_tabella The tp_tabella to set
	 */
	public void setTp_tabella(String tp_tabella) {
		this._tp_tabella = tp_tabella;
	}

}
