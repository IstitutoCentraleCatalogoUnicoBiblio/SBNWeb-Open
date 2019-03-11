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
package it.iccu.sbn.web.actionforms.periodici.previsionale;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.web.actionforms.periodici.PeriodiciListaForm;

import java.util.List;

public class PrevisionaleForm extends PeriodiciListaForm {


	private static final long serialVersionUID = 1598223177470725469L;
	private DatiBibliograficiPeriodicoVO datiBibliografici;
	private EsameSeriePeriodicoVO esame = new EsameSeriePeriodicoVO();
	private List<ModelloPrevisionaleVO> modelli = ValidazioneDati.emptyList();
	private int idModello;
	private ModelloPrevisionaleVO modello = new ModelloPrevisionaleVO();
	private ModelloPrevisionaleVO oldModello = new ModelloPrevisionaleVO();
	private String dataPrimoFasc;
	private List<TB_CODICI> listaPeriodicita;
	private Integer idxEscludi;
	private Integer idxIncludi;

	private int durata;
	private String tipoDurata;
	private List<TB_CODICI> listaTipoDurata;
	private List<TB_CODICI> listaTipoNumerazione;

	public EsameSeriePeriodicoVO getEsame() {
		return esame;
	}

	public void setEsame(EsameSeriePeriodicoVO esame) {
		this.esame = esame;
	}

	public void setModelli(List<ModelloPrevisionaleVO> modelli) {
		this.modelli = modelli;
	}

	public List<ModelloPrevisionaleVO> getModelli() {
		return modelli;
	}

	public int getIdModello() {
		return idModello;
	}

	public void setIdModello(int idModello) {
		this.idModello = idModello;
	}

	public void setModello(ModelloPrevisionaleVO modello) {
		this.modello = modello;
	}

	public ModelloPrevisionaleVO getModello() {
		return modello;
	}

	public List<TB_CODICI> getListaPeriodicita() {
		return listaPeriodicita;
	}

	public void setListaPeriodicita(List<TB_CODICI> listaPeriodicita) {
		this.listaPeriodicita = listaPeriodicita;
	}

	public List<TB_CODICI> getListaTipoDurata() {
		return listaTipoDurata;
	}

	public void setListaTipoDurata(List<TB_CODICI> listaTipoDurata) {
		this.listaTipoDurata = listaTipoDurata;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public String getTipoDurata() {
		return tipoDurata;
	}

	public void setTipoDurata(String tipoDurata) {
		this.tipoDurata = tipoDurata;
	}

	public String getDataPrimoFasc() {
		return dataPrimoFasc;
	}

	public void setDataPrimoFasc(String dataPrimoFasc) {
		this.dataPrimoFasc = dataPrimoFasc;
	}

	public DatiBibliograficiPeriodicoVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(DatiBibliograficiPeriodicoVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public Integer getIdxEscludi() {
		return idxEscludi;
	}

	public void setIdxEscludi(Integer idxEscludi) {
		this.idxEscludi = idxEscludi;
	}

	public Integer getIdxIncludi() {
		return idxIncludi;
	}

	public void setIdxIncludi(Integer idxIncludi) {
		this.idxIncludi = idxIncludi;
	}

	public void setListaTipoNumerazione(List<TB_CODICI> listaTipoNumerazione) {
		this.listaTipoNumerazione = listaTipoNumerazione;
	}

	public List<TB_CODICI> getListaTipoNumerazione() {
		return listaTipoNumerazione;
	}

	public ModelloPrevisionaleVO getOldModello() {
		return oldModello;
	}

	public void setOldModello(ModelloPrevisionaleVO oldModello) {
		this.oldModello = oldModello;
	}

}
