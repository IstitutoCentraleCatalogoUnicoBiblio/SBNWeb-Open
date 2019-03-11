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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.PosizioneDescrittore;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.web.constant.ConstantDefault;

public class RicercaComuneVO extends SerializableVO {

	private static final long serialVersionUID = -5477819524048413287L;
	private String codSoggettario;
	private String edizioneSoggettario;
	private String descSoggettario;
	private TipoOrdinamento _ordinamento;
	private String formaNome;
	private String elemBlocco;
	private TipoRicerca ricercaTipo;
	private TipoRicerca ricercaTipoD;
	private PosizioneDescrittore ricercaPerUnDescrittore;
	private boolean polo;
	private String livelloAutoritaDa;
	private String livelloAutoritaA;

	// Creo il default di ricercaSoggettoParole inizializzato con stringe vuote
	private RicercaSoggettoVO ricercaSoggetto = new RicercaSoggettoDescrittoriVO();

	// Creo il default di ricercaDescrittore inizializzato con stringe vuote
	private RicercaDescrittoreVO ricercaDescrittore = new RicercaDescrittoreVO();

	public RicercaComuneVO() {
		super();
		this.elemBlocco = ConstantDefault.ELEMENTI_BLOCCHI.getDefault();
		this._ordinamento = TipoOrdinamento.PER_TESTO;
		this.ricercaTipo = TipoRicerca.STRINGA_INIZIALE;
		this.ricercaTipoD = TipoRicerca.STRINGA_INIZIALE;
		this.ricercaPerUnDescrittore = PosizioneDescrittore.QUALUNQUE_POSIZIONE;
	}

	public RicercaComuneVO(String codSoggettario, String descSoggettario,
			String elemBlocco, TipoRicerca ricercaTipo, boolean polo,
			RicercaSoggettoVO ricercaSoggetto,
			RicercaDescrittoreVO ricercaDescrittore) {
		this.codSoggettario = codSoggettario;
		this.descSoggettario = descSoggettario;
		this.elemBlocco = elemBlocco;
		this.ricercaTipo = ricercaTipo;
		this.polo = polo;
		this.ricercaSoggetto = ricercaSoggetto;
		this.ricercaDescrittore = ricercaDescrittore;

	}

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	public String getDescSoggettario() {
		return descSoggettario;
	}

	public void setDescSoggettario(String descSoggettario) {
		this.descSoggettario = descSoggettario;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public boolean isIndice() {
		return !polo;
	}

	public boolean isPolo() {
		return polo;
	}

	public void setPolo(boolean polo) {
		this.polo = polo;
	}

	public RicercaDescrittoreVO getRicercaDescrittore() {
		return ricercaDescrittore;
	}

	public void setRicercaDescrittore(RicercaDescrittoreVO ricercaDescrittore) {
		this.ricercaDescrittore = ricercaDescrittore;
	}

	public RicercaSoggettoVO getRicercaSoggetto() {
		return ricercaSoggetto;
	}

	public void setRicercaSoggetto(RicercaSoggettoVO ricercaSoggetto) {
		this.ricercaSoggetto = ricercaSoggetto;
	}

	public void validate() throws ValidationException {
		if (isNull(elemBlocco) || !isNumeric(elemBlocco))
			throw new ValidationException("elementi per blocco non valido");

		boolean canaleSOG = ricercaSoggetto != null && !ricercaSoggetto.isEmpty();
		boolean canaleDES = ricercaDescrittore != null && !ricercaDescrittore.isEmpty();

		if (canaleSOG && canaleDES)
			throw new ValidationException("Digitare un solo canale");

		if (!canaleSOG && !canaleDES)
			throw new ValidationException("Digitare almeno un canale di ricerca", 5);

		if (canaleSOG) {
			ricercaSoggetto.validate();
			if (ricercaSoggetto.count() > 1 && !isFilled(codSoggettario) )
				throw new ValidationException("Impostare il codice soggettario", 6);
		}

		if (canaleDES)
			ricercaDescrittore.validate();
	}



	public Object getOperazione() {

		try {
			this.ricercaDescrittore.validate();
			return this.ricercaDescrittore;

		} catch (ValidationException e) {
		} catch (NullPointerException e) {
		}
		try {
			this.ricercaSoggetto.validate();
			return this.ricercaSoggetto;

		} catch (ValidationException e) {
		} catch (NullPointerException e) {
		}
		return null;
	}

	public TipoOrdinamento getOrdinamentoDescrittore() {
		return _ordinamento;
	}

	public void setOrdinamentoDescrittore(TipoOrdinamento ordinamentoDescrittore) {
		this._ordinamento = ordinamentoDescrittore;
	}

	public TipoOrdinamento getOrdinamentoSoggetto() {
		return _ordinamento;
	}

	public String getOrdinamento() {
		return _ordinamento.toString();
	}

	public void setOrdinamentoSoggetto(TipoOrdinamento ordinamentoSoggetto) {
		this._ordinamento = ordinamentoSoggetto;
	}

	public void setOrdinamento(String ordinamentoSoggetto) {
		this._ordinamento = TipoOrdinamento.valueOf(ordinamentoSoggetto);
	}

	public PosizioneDescrittore getRicercaPerUnDescrittore() {
		return ricercaPerUnDescrittore;
	}

	public void setRicercaPerUnDescrittore(PosizioneDescrittore ricercaPerUnDescrittore) {
		this.ricercaPerUnDescrittore = ricercaPerUnDescrittore;
	}

	public String getPosizioneDescrittore() {
		return this.ricercaPerUnDescrittore.toString();
	}

	public void setPosizioneDescrittore(String value) {
		this.ricercaPerUnDescrittore = PosizioneDescrittore.valueOf(value);
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public String getLivelloAutoritaDa() {
		return livelloAutoritaDa;
	}

	public void setLivelloAutoritaDa(String livelloAutoritaDa) {
		this.livelloAutoritaDa = livelloAutoritaDa;
	}

	public String getLivelloAutoritaA() {
		return livelloAutoritaA;
	}

	public void setLivelloAutoritaA(String livelloAutoritaA) {
		this.livelloAutoritaA = livelloAutoritaA;
	}

	public TipoRicerca getRicercaTipo() {
		return ricercaTipo;
	}

	public String getRicercaTipoSogg() {
		return ricercaTipo.toString();
	}

	public void setRicercaTipo(TipoRicerca ricercaTipo) {
		this.ricercaTipo = ricercaTipo;
	}

	public void setRicercaTipoSogg(String ricercaTipo) {
		this.ricercaTipo = TipoRicerca.valueOf(ricercaTipo);
	}

	public TipoRicerca getRicercaTipoD() {
		return ricercaTipoD;
	}

	public String getRicercaTipoDescr() {
		return ricercaTipoD.toString();
	}

	public void setRicercaTipoD(TipoRicerca ricercaTipoD) {
		this.ricercaTipoD = ricercaTipoD;
	}

	public void setRicercaTipoDescr(String ricercaTipoD) {
		this.ricercaTipoD = TipoRicerca.valueOf(ricercaTipoD);
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

}
