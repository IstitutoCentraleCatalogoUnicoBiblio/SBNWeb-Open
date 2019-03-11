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

import java.util.List;

public class CreaVariaSoggettoVO extends CreaVariaSogDesBaseVO {

	private static final long serialVersionUID = -2241866509824033964L;

	private String cid;
	private String cidIndice;
	private String livello;
	private String tipoSoggetto;
	private String note;
	private String action;
	private int livelloAutProfilo;
	private boolean creaDescrittori = false;
	private boolean forzaCreazione = false;
	private List<ElementoSinteticaSoggettoVO> listaSimili;

	private boolean cambioEdizione;

	public CreaVariaSoggettoVO(DettaglioSoggettoVO ds) {
		this.setCodiceSoggettario(ds.getCampoSoggettario());
		this.setCid(ds.getCid());
		this.setLivello(ds.getLivAut());
		this.setT005(ds.getT005());
		this.setTipoSoggetto(ds.getCategoriaSoggetto());
		this.setTesto(ds.getTesto());
		this.setCondiviso(ds.isCondiviso());
		this.setNote(ds.getNote());
		this.setLivelloPolo(ds.isLivelloPolo());
		this.setEdizioneSoggettario(ds.getEdizioneSoggettario());
	}

	public CreaVariaSoggettoVO() {
		super();
	}

	public List<ElementoSinteticaSoggettoVO> getListaSimili() {
		return listaSimili;
	}

	public void setListaSimili(List<ElementoSinteticaSoggettoVO> listaSimili) {
		this.listaSimili = listaSimili;
	}

	public boolean isCambioEdizione() {
		return cambioEdizione;
	}

	public void setCambioEdizione(boolean cambioEdizione) {
		this.cambioEdizione = cambioEdizione;
	}

	public boolean isCreaDescrittori() {
		return creaDescrittori;
	}

	public void setCreaDescrittori(boolean creaDescrittori) {
		this.creaDescrittori = creaDescrittori;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLivello() {
		return livello;
	}

	public void setLivello(String livello) {
		this.livello = livello;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public int getLivelloAutProfilo() {
		return livelloAutProfilo;
	}

	public void setLivelloAutProfilo(int livelloAutProfilo) {
		this.livelloAutProfilo = livelloAutProfilo;
	}

	public String getCidIndice() {
		return cidIndice;
	}

	public void setCidIndice(String cidIndice) {
		this.cidIndice = cidIndice;
	}

	public boolean isForzaCreazione() {
		return forzaCreazione;
	}

	public void setForzaCreazione(boolean forzaCreazione) {
		this.forzaCreazione = forzaCreazione;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (!isFilled(getTesto()) )
			throw new ValidationException("Digitare il campo testo");

		if (length(getTesto()) > 240)
			throw new ValidationException("Il testo del soggetto supera i 240 caratteri");

		if (length(note) > 320)
			throw new ValidationException("La nota al soggetto supera i 320 caratteri");

		if (!isFilled(livello) )
			throw new ValidationException("Digitare il campo stato di controllo");
	}

}
