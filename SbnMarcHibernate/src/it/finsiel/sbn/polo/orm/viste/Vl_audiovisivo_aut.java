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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;

/**
 * Classe Vl_audiovisivo_aut ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
 * AUTOMATICAMENTE. NESSUNA MODIFICA DEVE ESSERE APPORTATA MANUALMENTE, PERCHE'
 * SARA' PERSA IN FUTURO. OGNI AGGIUNTA MANUALE NON E' ATTUALMENTE POSSIBILE.
 *
 * <p>
 * Classe che contiene gli attributi estratti dalla tavola del DB
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 10/10/2014
 */
public class Vl_audiovisivo_aut extends Vl_titolo_aut {

	private static final long serialVersionUID = 6353632915789851969L;

	private String TP_MATER_AUDIOVIS;
	private String CD_FORMA_VIDEO;
	private String CD_TECNICA;
	private String CD_FORMA;
	private String CD_VELOCITA;

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String
				.valueOf(getBID()))
				+ " "
				+ ((getVID() == null) ? "" : String.valueOf(getVID()))
				+ " "
				+ getTP_RESPONSABILITA() + " " + getCD_RELAZIONE());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved = true;
	}

	public void onLoad() {
		_saved = true;
	}

	public boolean isSaved() {
		return _saved;
	}

	public void setCD_FORMA(String value) {
		this.CD_FORMA = value;
		this.settaParametro(KeyParameter.XXXcd_forma, value);
	}

	public String getCD_FORMA() {
		return CD_FORMA;
	}

	public void setCD_VELOCITA(String value) {
		this.CD_VELOCITA = value;
		this.settaParametro(KeyParameter.XXXcd_velocita, value);
	}

	public String getCD_VELOCITA() {
		return CD_VELOCITA;
	}

	public void setTP_MATER_AUDIOVIS(String value) {
		this.TP_MATER_AUDIOVIS = value;
		this.settaParametro(KeyParameter.XXXtp_mater_audiovis, value);
	}

	public String getTP_MATER_AUDIOVIS() {
		return TP_MATER_AUDIOVIS;
	}

	public void setCD_FORMA_VIDEO(String value) {
		this.CD_FORMA_VIDEO = value;
		this.settaParametro(KeyParameter.XXXcd_forma_video, value);
	}

	public String getCD_FORMA_VIDEO() {
		return CD_FORMA_VIDEO;
	}

	public void setCD_TECNICA(String value) {
		this.CD_TECNICA = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica, value);
	}

	public String getCD_TECNICA() {
		return CD_TECNICA;
	}

}
