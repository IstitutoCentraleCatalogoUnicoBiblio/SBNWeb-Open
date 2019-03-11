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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.List;

public class RigheOrdiniStampaBuoniVO extends SerializableVO {

	private static final long serialVersionUID = -2757612730341847076L;

	private String numOrdine=""; // tipo, anno, cod

	private boolean rinnovato;
	private String rinnovoOrigine=""; // tipo, anno, cod

	private boolean abbonamento;
	private String annataAbbOrdine="";
	private String dataPubblDa="";
	private String dataPubbA="";

	private String etichetta_ISBN=""; // COD I
	private String etichetta_ISSN=""; // COD J
	private String etichetta_ISMN=""; // COD M
	private String etichetta_NUMEROEDITORIALE=""; // COD E
	private String etichetta_NUMERODILASTRA=""; // COD L
	private String etichetta_NPUBBLICAZIONEGOVERNATIVA=""; // COD G
	private List<StrutturaTerna> numStandardArr; // tipo, numero, denotipo

	private String tipoOrdine;
	private List<StrutturaInventariOrdVO> righeInventariRilegatura;

	private String titolo="";

	private String prezzo="";

	private String note="";

	public RigheOrdiniStampaBuoniVO (){}

	public boolean isAbbonamento() {
		return abbonamento;
	}

	public void setAbbonamento(boolean abbonamento) {
		this.abbonamento = abbonamento;
	}

	public String getAnnataAbbOrdine() {
		return annataAbbOrdine;
	}

	public void setAnnataAbbOrdine(String annataAbbOrdine) {
		this.annataAbbOrdine = annataAbbOrdine;
	}

	public String getDataPubbA() {
		return dataPubbA;
	}

	public void setDataPubbA(String dataPubbA) {
		this.dataPubbA = dataPubbA;
	}

	public String getDataPubblDa() {
		return dataPubblDa;
	}

	public void setDataPubblDa(String dataPubblDa) {
		this.dataPubblDa = dataPubblDa;
	}

	public String getEtichetta_ISBN() {
		return etichetta_ISBN;
	}

	public void setEtichetta_ISBN(String etichetta_ISBN) {
		this.etichetta_ISBN = etichetta_ISBN;
	}

	public String getEtichetta_ISMN() {
		return etichetta_ISMN;
	}

	public void setEtichetta_ISMN(String etichetta_ISMN) {
		this.etichetta_ISMN = etichetta_ISMN;
	}

	public String getEtichetta_ISSN() {
		return etichetta_ISSN;
	}

	public void setEtichetta_ISSN(String etichetta_ISSN) {
		this.etichetta_ISSN = etichetta_ISSN;
	}

	public String getEtichetta_NPUBBLICAZIONEGOVERNATIVA() {
		return etichetta_NPUBBLICAZIONEGOVERNATIVA;
	}

	public void setEtichetta_NPUBBLICAZIONEGOVERNATIVA(
			String etichetta_NPUBBLICAZIONEGOVERNATIVA) {
		this.etichetta_NPUBBLICAZIONEGOVERNATIVA = etichetta_NPUBBLICAZIONEGOVERNATIVA;
	}

	public String getEtichetta_NUMERODILASTRA() {
		return etichetta_NUMERODILASTRA;
	}

	public void setEtichetta_NUMERODILASTRA(String etichetta_NUMERODILASTRA) {
		this.etichetta_NUMERODILASTRA = etichetta_NUMERODILASTRA;
	}

	public String getEtichetta_NUMEROEDITORIALE() {
		return etichetta_NUMEROEDITORIALE;
	}

	public void setEtichetta_NUMEROEDITORIALE(String etichetta_NUMEROEDITORIALE) {
		this.etichetta_NUMEROEDITORIALE = etichetta_NUMEROEDITORIALE;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNumOrdine() {
		return numOrdine;
	}

	public void setNumOrdine(String numOrdine) {
		this.numOrdine = numOrdine;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public boolean isRinnovato() {
		return rinnovato;
	}

	public void setRinnovato(boolean rinnovato) {
		this.rinnovato = rinnovato;
	}

	public String getRinnovoOrigine() {
		return rinnovoOrigine;
	}

	public void setRinnovoOrigine(String rinnovoOrigine) {
		this.rinnovoOrigine = rinnovoOrigine;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public List<StrutturaTerna> getNumStandardArr() {
		return numStandardArr;
	}

	public void setNumStandardArr(List<StrutturaTerna> numStandardArr) {
		this.numStandardArr = numStandardArr;
	}

	public List<StrutturaInventariOrdVO> getRigheInventariRilegatura() {
		return righeInventariRilegatura;
	}

	public void setRigheInventariRilegatura(
			List<StrutturaInventariOrdVO> righeInventariRilegatura) {
		this.righeInventariRilegatura = righeInventariRilegatura;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

}
