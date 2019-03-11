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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ElementoSinteticaElabDiffVO extends SerializableVO implements Comparable<ElementoSinteticaElabDiffVO> {

	private static final long serialVersionUID = 2926629887383160078L;
	private int progressivo;
	private String visibilita;
	private String procedura;
	private String descrizioneProcedura;
	private String idRichiesta;
	private String dataRichiesta;
	private String dataEsecuzioneProgrammata;
	private String nomeCoda;
	private String dataInizioEsecuzione;
	private String dataFineEsecuzione;
	private String biblioteca;
	private String richiedente;
	private String cognomeNome;
	private String stato;
	private int idCoda;
	private String descrizioneBiblioteca;

	private List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
	private String parametri;

	public static final Comparator<ElementoSinteticaElabDiffVO> ORDINA_PER_PROGRESSIVO = new Comparator<ElementoSinteticaElabDiffVO>() {
		public int compare(ElementoSinteticaElabDiffVO o1, ElementoSinteticaElabDiffVO o2) {
			int myProgr1 = o1.getProgressivo();
			int myProgr2 = o2.getProgressivo();
			return myProgr1 - myProgr2;
		}
	};


	public ElementoSinteticaElabDiffVO() {
		super();
	}

	public ElementoSinteticaElabDiffVO(int progressivo, String procedura,
			String nomeCoda, String idRichiesta, String dataRichiesta,
			String dataEsecuzioneProgrammata, String biblioteca,
			String richiedente, String stato,
			List<DownloadVO> listaDownload, String parametri) {
		this.progressivo = progressivo;
		this.procedura = procedura;
		this.nomeCoda = nomeCoda;
		this.idRichiesta = idRichiesta;
		this.dataRichiesta = dataRichiesta;
		this.dataEsecuzioneProgrammata = dataEsecuzioneProgrammata;
		this.biblioteca = biblioteca;
		this.richiedente = richiedente;
		this.stato = stato;
		// this.scarica = scarica;
		this.listaDownload = listaDownload;
		this.parametri = parametri;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getDataEsecuzioneProgrammata() {
		return dataEsecuzioneProgrammata;
	}

	public void setDataEsecuzioneProgrammata(String dataEsecuzioneProgrammata) {
		this.dataEsecuzioneProgrammata = dataEsecuzioneProgrammata;
	}

	public String getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public String getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public String getParametri() {
		return parametri;
	}

	public void setParametri(String parametri) {
		this.parametri = parametri;
	}

	public String getProcedura() {
		return procedura;
	}

	public void setProcedura(String procedura) {
		this.procedura = trimAndSet(procedura);
	}

	public String getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(String richiedente) {
		this.richiedente = trimAndSet(richiedente);
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = trimAndSet(stato);
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public List<DownloadVO> getListaDownload() {
		return listaDownload;
	}

	public void setListaDownload(List<DownloadVO> listaDownload) {
		this.listaDownload = listaDownload;
	}

	public String getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(String visibilita) {
		this.visibilita = trimAndSet(visibilita);
	}

	public String getNomeCoda() {
		return nomeCoda;
	}

	public void setNomeCoda(String nomeCoda) {
		this.nomeCoda = trimAndSet(nomeCoda);
	}

	public String getDataFineEsecuzione() {
		return dataFineEsecuzione;
	}

	public void setDataFineEsecuzione(String dataFineEsecuzione) {
		this.dataFineEsecuzione = trimAndSet(dataFineEsecuzione);
	}

	public int compareTo(ElementoSinteticaElabDiffVO o) {
		int id1 = Integer.parseInt(this.idRichiesta);
		int id2 = Integer.parseInt(o.idRichiesta);
		return (id1 - id2);
	}

	public String getDataInizioEsecuzione() {
		return dataInizioEsecuzione;
	}

	public void setDataInizioEsecuzione(String dataInizioEsecuzione) {
		this.dataInizioEsecuzione = trimAndSet(dataInizioEsecuzione);
	}

	public int getIdCoda() {
		return idCoda;
	}

	public void setIdCoda(int idCoda) {
		this.idCoda = idCoda;
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = trimAndSet(cognomeNome);
	}

	public void setDescrizioneProcedura(String descrizioneProcedura) {
		this.descrizioneProcedura = descrizioneProcedura;
	}

	public String getDescrizioneProcedura() {
		return descrizioneProcedura;
	}

	public void setDescrizioneBiblioteca(String descrizioneBiblioteca) {
		this.descrizioneBiblioteca = descrizioneBiblioteca;
	}

	public String getDescrizioneBiblioteca() {
		return descrizioneBiblioteca;
	}

}
