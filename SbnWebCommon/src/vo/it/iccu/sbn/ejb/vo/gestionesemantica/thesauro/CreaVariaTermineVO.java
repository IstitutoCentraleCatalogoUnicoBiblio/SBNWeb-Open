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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneOggetto;

import java.util.List;

public class CreaVariaTermineVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -8880978046191734517L;

	private TipoOperazioneOggetto operazione = TipoOperazioneOggetto.CREA;
	private String did;
	private String testo;
	private String livelloAutorita;
	private String note;
	private String formaNome;
	private String codThesauro;
	private boolean cattura = false;
	private List<ElementoSinteticaThesauroVO> listaSimili;

	public boolean isCattura() {
		return cattura;
	}

	public void setCattura(boolean cattura) {
		this.cattura = cattura;
	}

	public CreaVariaTermineVO() {
		super();
	}

	public CreaVariaTermineVO(DettaglioTermineThesauroVO dettaglio) {
		super();
		this.did = dettaglio.getDid();
		this.codThesauro = dettaglio.getCodThesauro();
		this.livelloAutorita = dettaglio.getLivAut();
		this.formaNome = dettaglio.getFormaNome();
		this.testo = dettaglio.getTesto();
		this.note = dettaglio.getNote();

		this.setT005(dettaglio.getT005());
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = trimAndSet(testo);
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public boolean isRinvio() {
		return formaNome == null ? false : formaNome.equalsIgnoreCase("R");
	}

	public TipoOperazioneOggetto getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazioneOggetto operazione) {
		this.operazione = operazione;
	}

	public List<ElementoSinteticaThesauroVO> getListaSimili() {
		return listaSimili;
	}

	public void setListaSimili(List<ElementoSinteticaThesauroVO> listaSimili) {
		this.listaSimili = listaSimili;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (!isFilled(codThesauro))
			throw new ValidationException("Parametro Thesauro non valido");

		if (!isFilled(livelloAutorita))
			throw new ValidationException("Parametro Livello autorità non valido");

		if (!isFilled(testo))
			throw new ValidationException("Parametro Descrittore non valido");

		if (length(testo) > 240)
			throw new ValidationException("Il testo del termine supera i 240 caratteri");

		if (length(note) > 240)
			throw new ValidationException("La nota al termine supera i 240 caratteri");
	}



}
