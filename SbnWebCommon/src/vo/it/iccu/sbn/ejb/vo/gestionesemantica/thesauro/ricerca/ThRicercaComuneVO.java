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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.web.constant.ConstantDefault;

public class ThRicercaComuneVO extends SerializableVO {

	private static final long serialVersionUID = -5752722087426332100L;

	private String codThesauro;
	private String descThesauro;
	private TipoOrdinamento ordinamentoDescrittore;
	private String elemBlocco;
	private TipoRicerca ricercaTipo;
	private boolean polo;
	private boolean indice;

	// Creo il default di ricercaThesauroTermini inizializzato con stringhe vuote
	private ThRicercaTitoliCollegatiVO ricercaTitoliCollegati = new ThRicercaTitoliCollegatiVO();

	// Creo il default di ricercaThesauroDescrittore inizializzato con stringhe vuote
	private ThRicercaDescrittoreVO ricercaThesauroDescrittore = new ThRicercaDescrittoreVO();

	public ThRicercaComuneVO() {
		super();
		this.elemBlocco = ConstantDefault.ELEMENTI_BLOCCHI.getDefault();
		this.ricercaTipo = TipoRicerca.STRINGA_INIZIALE;
		this.ordinamentoDescrittore = TipoOrdinamento.PER_ID;
	}

	public void validate() throws ValidationException {

		if (isNull(elemBlocco) || !isNumeric(elemBlocco))
			throw new ValidationException("elementi per blocco non valido");

		boolean canale1 = ricercaThesauroDescrittore != null && !ricercaThesauroDescrittore.isEmpty();
		boolean canale2 = ricercaTitoliCollegati != null && !ricercaTitoliCollegati.isEmpty();

		if (canale1 && canale2)
			throw new ValidationException("Digitare un solo canale");

		// non ho impostato nulla!!
		if (!canale1 && !canale2)
			throw new ValidationException(
					"Digitare almeno uno tra i parametri possibili per effettuare la ricerca");

		if (canale1)
			ricercaThesauroDescrittore.validate();

		if (canale2) {
			ricercaTitoliCollegati.validate();
			if (ricercaTitoliCollegati.count() > 1 && isNull(codThesauro))
				throw new ValidationException("Impostare codice thesauro");
		}

	}

	public Object getOperazione() {

		try {
			this.ricercaThesauroDescrittore.validate();
			return this.ricercaThesauroDescrittore;

		} catch (Exception e) {	}

		try {
			this.ricercaTitoliCollegati.validate();
			return this.ricercaTitoliCollegati;

		} catch (Exception e) {	}

		return null;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public String getDescThesauro() {
		return descThesauro;
	}

	public void setDescThesauro(String descThesauro) {
		this.descThesauro = descThesauro;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getRicercaStringaTipo() {
		return ricercaTipo.toString();
	}

	public void setRicercaStringaTipo(String ricercaTipo) {
		this.ricercaTipo = TipoRicerca.valueOf(ricercaTipo);
	}

	public boolean isIndice() {
		return indice;
	}

	public void setIndice(boolean indice) {
		this.indice = indice;
	}

	public boolean isPolo() {
		return polo;
	}

	public void setPolo(boolean polo) {
		this.polo = polo;
	}

	public String getOrdinamentoTermine() {
		return ordinamentoDescrittore.toString();
	}

	public void setOrdinamentoTermine(String ord) {
		this.ordinamentoDescrittore = TipoOrdinamento.valueOf(ord);
	}

	public ThRicercaDescrittoreVO getRicercaThesauroDescrittore() {
		return ricercaThesauroDescrittore;
	}

	public void setRicercaThesauroDescrittore(
			ThRicercaDescrittoreVO ricercaThesauroDescrittore) {
		this.ricercaThesauroDescrittore = ricercaThesauroDescrittore;
	}

	public void setRicercaTitoliCollegati(
			ThRicercaTitoliCollegatiVO ricercaThesauroTermini) {
		this.ricercaTitoliCollegati = ricercaThesauroTermini;
	}

	public ThRicercaTitoliCollegatiVO getRicercaTitoliCollegati() {
		return ricercaTitoliCollegati;
	}

	public void setOrdinamentoDescrittore(TipoOrdinamento ordinamentoDescrittore) {
		this.ordinamentoDescrittore = ordinamentoDescrittore;
	}

	public TipoRicerca getRicercaTipo() {
		return ricercaTipo;
	}

	public void setRicercaTipo(TipoRicerca ricercaTipo) {
		this.ricercaTipo = ricercaTipo;
	}

	public TipoOrdinamento getOrdinamentoDescrittore() {
		return ordinamentoDescrittore;
	}

}
