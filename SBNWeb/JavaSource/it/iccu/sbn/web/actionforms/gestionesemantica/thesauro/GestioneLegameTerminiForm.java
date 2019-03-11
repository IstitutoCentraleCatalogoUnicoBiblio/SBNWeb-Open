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
package it.iccu.sbn.web.actionforms.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTerminiType;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class GestioneLegameTerminiForm extends ActionForm {


	private static final long serialVersionUID = -4059344294881991055L;
	private boolean initialized = false;
	private ParametriThesauro parametri;
	private ModalitaLegameTerminiType modalita;
	private DatiLegameTerminiVO datiLegame = new DatiLegameTerminiVO();
	private List<ComboCodDescVO> listaTipiLegame;
	private String tipoLegame;
	private String[] listaPulsanti;


	public String[] getListaPulsanti() {
		return listaPulsanti;
	}

	public void setListaPulsanti(String[] listaPulsanti) {
		this.listaPulsanti = listaPulsanti;
	}

	public DettaglioTermineThesauroVO[] getTermini() {
		return this.datiLegame != null ? new DettaglioTermineThesauroVO[] {
				datiLegame.getDid1(), datiLegame.getDid2() } : null;
	}

	public GestioneLegameTerminiForm() {
		super();
		this.tipoLegame = "RT";
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public ModalitaLegameTerminiType getModalita() {
		return modalita;
	}

	public void setModalita(ModalitaLegameTerminiType modalita) {
		this.modalita = modalita;
	}

	public DatiLegameTerminiVO getDatiLegame() {
		return datiLegame;
	}

	public void setDatiLegame(DatiLegameTerminiVO datiLegame) {
		this.datiLegame = datiLegame;
	}

	public List<ComboCodDescVO> getListaTipiLegame() {
		return listaTipiLegame;
	}

	public void setListaTipiLegame(List<ComboCodDescVO> listaTipiLegame) {
		this.listaTipiLegame = listaTipiLegame;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getTipoLegameJSP() {
		return tipoLegame.toString();
	}

	public void setTipoLegameJSP(String tipoLegameJSP) {
		this.tipoLegame = tipoLegameJSP;
	}

}
