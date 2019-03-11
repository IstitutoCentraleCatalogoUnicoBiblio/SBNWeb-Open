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
package it.iccu.sbn.ejb.vo.servizi;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.AnagrafeUtenteProfessionaleVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;

import java.util.ArrayList;
import java.util.List;

public class AttivitaServizioVO extends SerializableVO {

	private static final long serialVersionUID = 8669550915352204371L;
	private IterServizioVO passoIter;
	private List<FaseControlloIterVO> controlli;
	private List<AnagrafeUtenteProfessionaleVO> bibliotecari;

	public AttivitaServizioVO() {
		passoIter = new IterServizioVO();
		controlli = new ArrayList<FaseControlloIterVO>();
		bibliotecari = new ArrayList<AnagrafeUtenteProfessionaleVO>();
	}

	public IterServizioVO getPassoIter() {
		return passoIter;
	}

	public void setPassoIter(IterServizioVO passoIter) {
		this.passoIter = passoIter;
	}

	public void setControlli(List<FaseControlloIterVO> controlli) {
		this.controlli = controlli;
	}

	public void setBibliotecari(List<AnagrafeUtenteProfessionaleVO> bibliotecari) {
		this.bibliotecari = bibliotecari;
	}

	public List<FaseControlloIterVO> getControlli() {
		return controlli;
	}

	public List<AnagrafeUtenteProfessionaleVO> getBibliotecari() {
		return bibliotecari;
	}

	public String getCodAttivita() {
		return passoIter.getCodAttivita();
	}

}
