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
package it.finsiel.sbn.polo.factoring.profile;

import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;

import org.apache.log4j.Logger;

/**
 * Classe ValidatorContainerUser
 * <p>
 * VERSIONE
 * Permette la gestione di informazioni relative ad un UTENTE
 * (ricostruisce abilitazioni sui gruppi collegati ad esso)
 * </p>
 * @author
 */
public class ValidatorContainerUser extends ValidatorContainerObject {

	private static final long serialVersionUID = -3189761435888841159L;

	static Logger log = Logger.getLogger("iccu.amministrazione.validator.ValidatorContainerUser");

	// Gruppo principale
	private Tbf_bibliotecario tb_utente;

	/**
	 * Returns the tb_utente.
	 * @return Tb_utente
	 */
	public Tbf_bibliotecario getTbf_bibliotecario() {
		return tb_utente;
	}
	/**
	 * Sets the tb_utente.
	 * @param tb_utente The tb_utente to set
	 */
	public void setTbf_bibliotecario(Tbf_bibliotecario tb_utente) {
		this.tb_utente = tb_utente;
	}

}
