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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.excel.ExcelPrintable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaDati950VO extends SerializableVO implements ExcelPrintable {

	private static final long serialVersionUID = 465099394662781770L;

	private List<String> bid;

	private String codPolo;

	private List<String> codBibliotecaInventario;
	private List<String> consistenzaEsemplare;
	private List<String> codSerieInventariale;
	private List<Integer> codInventario;

	private List<String> codPoloSezioneCollocazione;
	private List<String> codBibliotecaSezioneCollocazione;
	private List<String> codSezioneCollocazione;
	private List<String> codCollocazione;
	private List<String> specCollocazione;
	private List<String> consistenzaCollocazione;

	private List<Integer> error;
	private List<String> msgError;

	public ListaDati950VO(){
		// vuoto
		bid = new ArrayList<String>();
		consistenzaEsemplare = new ArrayList<String>();
		codSerieInventariale = new ArrayList<String>();
		codInventario = new ArrayList<Integer>();
		codSezioneCollocazione = new ArrayList<String>();
		codCollocazione = new ArrayList<String>();
		specCollocazione = new ArrayList<String>();
		consistenzaCollocazione = new ArrayList<String>();
		error = new ArrayList<Integer>();
		msgError = new ArrayList<String>();
	}

	public void add(String bid, String consistenzaEsemplare,
			String codSerieInventariale, int codInventario,
			String codSezioneCollocazione, String codCollocazione,
			String specCollocazione, String consistenzaCollocazione,
			int error, String msgError){
		this.bid.add(bid);
		this.consistenzaEsemplare.add(consistenzaEsemplare);
		this.codSerieInventariale.add(codSerieInventariale);
		this.codInventario.add(codInventario);
		this.codSezioneCollocazione.add(codSezioneCollocazione);
		this.codCollocazione.add(codCollocazione);
		this.specCollocazione.add(specCollocazione);
		this.consistenzaCollocazione.add(consistenzaCollocazione);
		this.error.add(error);
		this.msgError.add(msgError);
	}

	public List<String> getBid() {
		return bid;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public List<String> getCodBibliotecaInventario() {
		return codBibliotecaInventario;
	}

	public List<String> getConsistenzaEsemplare() {
		return consistenzaEsemplare;
	}

	public List<String> getCodSerieInventariale() {
		return codSerieInventariale;
	}

	public List<Integer> getCodInventario() {
		return codInventario;
	}

	public List<String> getCodPoloSezioneCollocazione() {
		return codPoloSezioneCollocazione;
	}

	public List<String> getCodBibliotecaSezioneCollocazione() {
		return codBibliotecaSezioneCollocazione;
	}

	public List<String> getCodSezioneCollocazione() {
		return codSezioneCollocazione;
	}

	public List<String> getCodCollocazione() {
		return codCollocazione;
	}

	public List<String> getSpecCollocazione() {
		return specCollocazione;
	}

	public List<String> getConsistenzaCollocazione() {
		return consistenzaCollocazione;
	}

	public List<Integer> getError() {
		return error;
	}

	public List<String> getMsgError() {
		return msgError;
	}

	public Serializable[] getValues() {
		return new Serializable[] {
			(Serializable) bid,
			codPolo,
			(Serializable) codBibliotecaInventario,
			(Serializable) consistenzaEsemplare,
			(Serializable) codSerieInventariale,
			(Serializable) codInventario,
			(Serializable) codPoloSezioneCollocazione,
			(Serializable) codBibliotecaSezioneCollocazione,
			(Serializable) codSezioneCollocazione,
			(Serializable) codCollocazione,
			(Serializable) specCollocazione,
			(Serializable) consistenzaCollocazione,
			(Serializable) error,
			(Serializable) msgError
		};
	}

}
