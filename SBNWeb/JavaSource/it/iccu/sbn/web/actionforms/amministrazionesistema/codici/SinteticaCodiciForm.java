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
package it.iccu.sbn.web.actionforms.amministrazionesistema.codici;

import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class SinteticaCodiciForm extends ActionForm {


	private static final long serialVersionUID = -4566574610200858920L;
	int bloccoCorrente;
	private boolean abilitaBlocchi;
    private int maxRighe = 200000;
    private int totRighe;
    private String idLista = "";

    private int totBlocchi;
	private String selezRadio;

	private String ordinamento;

	private List<CodiceConfigVO> elencoCodici = new ArrayList<CodiceConfigVO>();

	private String abilitaScrittura = "";

	public String getAbilitaScrittura() {
		return abilitaScrittura;
	}

	public void setAbilitaScrittura(String abilitaScrittura) {
		this.abilitaScrittura = abilitaScrittura;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getBloccoCorrente() {
		return bloccoCorrente;
	}

	public void setBloccoCorrente(int bloccoCorrente) {
		this.bloccoCorrente = bloccoCorrente;
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public List<CodiceConfigVO> getElencoCodici() {
		return elencoCodici;
	}

	public void setElencoCodici(List<CodiceConfigVO> elencoCodici) {
		this.elencoCodici = elencoCodici;
	}

}
