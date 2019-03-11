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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;

public class CreaVariaDescrittoreVO extends CreaVariaSogDesBaseVO {

	private static final long serialVersionUID = -4668621014242781697L;
	private String did;
	private String didIndice;
	private String livelloAutorita;
	private String note;
	private String formaNome;
	private boolean cattura = false;

	private String categoriaTermine;

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public boolean isRinvio() {
		return ValidazioneDati.equalsIgnoreCase(formaNome, "R");
	}

	public String getDidIndice() {
		return didIndice;
	}

	public void setDidIndice(String didIndice) {
		this.didIndice = didIndice;
	}

	public boolean isCattura() {
		return cattura;
	}

	public void setCattura(boolean cattura) {
		this.cattura = cattura;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (!isFilled(getTesto()))
			throw new ValidationException("Digitare il campo testo");

		if (length(getTesto()) > 240)
			throw new ValidationException("Il testo del descrittore supera i 240 caratteri");

		if (length(note) > 240)
			throw new ValidationException("La nota al descrittore supera i 240 caratteri");

		if (!isFilled(getCodiceSoggettario()))
			throw new ValidationException("Parametro Soggettario non valido");

		if (!isFilled(livelloAutorita))
			throw new ValidationException("Parametro Livello autorità non valido");


	}

	public String getCategoriaTermine() {
		return categoriaTermine;
	}

	public void setCategoriaTermine(String categoriaTermine) {
		this.categoriaTermine = categoriaTermine;
	}
}
