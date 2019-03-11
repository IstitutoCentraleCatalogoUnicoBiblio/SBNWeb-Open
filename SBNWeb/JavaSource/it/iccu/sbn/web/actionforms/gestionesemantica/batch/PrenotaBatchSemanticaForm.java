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
package it.iccu.sbn.web.actionforms.gestionesemantica.batch;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriCancellazioneSoggettiNonUtilizzatiVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class PrenotaBatchSemanticaForm extends ActionForm {


	private static final long serialVersionUID = -3564451783087131572L;
	private ParametriCancellazioneSoggettiNonUtilizzatiVO parametri = new ParametriCancellazioneSoggettiNonUtilizzatiVO();
	private boolean initialized = false;
	private boolean conferma = false;
	private String[] listaPulsanti;
	private List<ComboCodDescVO> listaSoggettari;
	private List<TB_CODICI> listaEdizioni;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String[] getListaPulsanti() {
		return listaPulsanti;
	}

	public void setListaPulsanti(String[] listaPulsanti) {
		this.listaPulsanti = listaPulsanti;
	}

	public List<ComboCodDescVO> getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List<ComboCodDescVO> listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public ParametriCancellazioneSoggettiNonUtilizzatiVO getParametri() {
		return parametri;
	}

	public void setParametri(ParametriCancellazioneSoggettiNonUtilizzatiVO parametri) {
		this.parametri = parametri;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

}
