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
package it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;

public class GruppoVO extends BaseVO {

	private static final long serialVersionUID = -2392231633370242918L;

	private String indice;
	private String nome;
	private List<DefaultVO> def = new ArrayList<DefaultVO>();
	private String bundle;
	private int seq_ordinamento;

	public int getSeq_ordinamento() {
		return seq_ordinamento;
	}

	public void setSeq_ordinamento(int seq_ordinamento) {
		this.seq_ordinamento = seq_ordinamento;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<DefaultVO> getDef() {
		return def;
	}

	public void setDef(List<DefaultVO> def) {
		this.def = def;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

}
