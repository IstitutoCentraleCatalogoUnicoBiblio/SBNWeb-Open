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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public class CatalogazioneTitoloGeneraleVO   extends SerializableVO {

	// = CatalogazioneTitoloGeneraleVO.class.hashCode();

// Dati relativi alla catalogazione titolo - campi princiapli e comuni a tutti i TIPI MATERIALE

	/**
	 *
	 */
	private static final long serialVersionUID = -3101065157514063744L;
	private List listaLivAutorita;
	private String livAutoritaSelez;

	private List listaTipiRecord;
	private String tipiRecordSelez;

	private List listaPaese;
	private String paeseSelez;

	private List listaLingue;
	private String linguaSelez1;
	private String linguaSelez2;
	private String linguaSelez3;

	private List listaTipoData;
	private String tipoDataSelez;
	private String dataPub1;
	private String dataPub2;

// Area del Numero Standard
	private String numStandard;
	private List listaTipiNumStandard;
	private String numStandardSelez;
	private String notaNumStandard;

//	 Area dell'Impronta
	private String impronta1;
	private String impronta2;
	private String impronta3;
	private String notaImpronta;


//	 Inizio  area del TITOLO
	private String titTitolo;
	private String titEdizione;
	private String titNumerazione;
	private String titPubblicazione;
	private String titDescrFisica;
	private String titNote;
//	 Fine  area del TITOLO

	private String dataInserimento;
	private String dataAggiornamento;

	public void validaCampiGenerali() throws ValidationException {
		if (this.titTitolo.length() <= 0 ) {
			throw new ValidationException(ValidationExceptionCodici.noCanPrim);
		}
	}


	public String getDataPub1() {
		return dataPub1;
	}


	public void setDataPub1(String dataPub1) {
		this.dataPub1 = dataPub1;
	}


	public String getDataPub2() {
		return dataPub2;
	}


	public void setDataPub2(String dataPub2) {
		this.dataPub2 = dataPub2;
	}


	public String getLinguaSelez1() {
		return linguaSelez1;
	}


	public void setLinguaSelez1(String linguaSelez1) {
		this.linguaSelez1 = linguaSelez1;
	}


	public String getLinguaSelez2() {
		return linguaSelez2;
	}


	public void setLinguaSelez2(String linguaSelez2) {
		this.linguaSelez2 = linguaSelez2;
	}


	public String getLinguaSelez3() {
		return linguaSelez3;
	}


	public void setLinguaSelez3(String linguaSelez3) {
		this.linguaSelez3 = linguaSelez3;
	}


	public List getListaLingue() {
		return listaLingue;
	}


	public void setListaLingue(List listaLingue) {
		this.listaLingue = listaLingue;
	}


	public List getListaLivAutorita() {
		return listaLivAutorita;
	}


	public void setListaLivAutorita(List listaLivAutorita) {
		this.listaLivAutorita = listaLivAutorita;
	}


	public List getListaPaese() {
		return listaPaese;
	}


	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}


	public List getListaTipiNumStandard() {
		return listaTipiNumStandard;
	}


	public void setListaTipiNumStandard(List listaTipiNumStandard) {
		this.listaTipiNumStandard = listaTipiNumStandard;
	}


	public List getListaTipiRecord() {
		return listaTipiRecord;
	}


	public void setListaTipiRecord(List listaTipiRecord) {
		this.listaTipiRecord = listaTipiRecord;
	}


	public List getListaTipoData() {
		return listaTipoData;
	}


	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}


	public String getLivAutoritaSelez() {
		return livAutoritaSelez;
	}


	public void setLivAutoritaSelez(String livAutoritaSelez) {
		this.livAutoritaSelez = livAutoritaSelez;
	}


	public String getNotaNumStandard() {
		return notaNumStandard;
	}


	public void setNotaNumStandard(String notaNumStandard) {
		this.notaNumStandard = notaNumStandard;
	}


	public String getNumStandard() {
		return numStandard;
	}


	public void setNumStandard(String numStandard) {
		this.numStandard = numStandard;
	}


	public String getNumStandardSelez() {
		return numStandardSelez;
	}


	public void setNumStandardSelez(String numStandardSelez) {
		this.numStandardSelez = numStandardSelez;
	}


	public String getPaeseSelez() {
		return paeseSelez;
	}


	public void setPaeseSelez(String paeseSelez) {
		this.paeseSelez = paeseSelez;
	}


	public String getTipiRecordSelez() {
		return tipiRecordSelez;
	}


	public void setTipiRecordSelez(String tipiRecordSelez) {
		this.tipiRecordSelez = tipiRecordSelez;
	}


	public String getTipoDataSelez() {
		return tipoDataSelez;
	}


	public void setTipoDataSelez(String tipoDataSelez) {
		this.tipoDataSelez = tipoDataSelez;
	}


	public String getDataAggiornamento() {
		return dataAggiornamento;
	}


	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}


	public String getDataInserimento() {
		return dataInserimento;
	}


	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}


	public String getTitDescrFisica() {
		return titDescrFisica;
	}


	public void setTitDescrFisica(String titDescrFisica) {
		this.titDescrFisica = titDescrFisica;
	}


	public String getTitEdizione() {
		return titEdizione;
	}


	public void setTitEdizione(String titEdizione) {
		this.titEdizione = titEdizione;
	}


	public String getTitNote() {
		return titNote;
	}


	public void setTitNote(String titNote) {
		this.titNote = titNote;
	}


	public String getTitNumerazione() {
		return titNumerazione;
	}


	public void setTitNumerazione(String titNumerazione) {
		this.titNumerazione = titNumerazione;
	}


	public String getTitPubblicazione() {
		return titPubblicazione;
	}


	public void setTitPubblicazione(String titPubblicazione) {
		this.titPubblicazione = titPubblicazione;
	}


	public String getTitTitolo() {
		return titTitolo;
	}


	public void setTitTitolo(String titTitolo) {
		this.titTitolo = titTitolo;
	}


	public String getImpronta1() {
		return impronta1;
	}


	public void setImpronta1(String impronta1) {
		this.impronta1 = impronta1;
	}


	public String getImpronta2() {
		return impronta2;
	}


	public void setImpronta2(String impronta2) {
		this.impronta2 = impronta2;
	}


	public String getImpronta3() {
		return impronta3;
	}


	public void setImpronta3(String impronta3) {
		this.impronta3 = impronta3;
	}


	public String getNotaImpronta() {
		return notaImpronta;
	}


	public void setNotaImpronta(String notaImpronta) {
		this.notaImpronta = notaImpronta;
	}

}
