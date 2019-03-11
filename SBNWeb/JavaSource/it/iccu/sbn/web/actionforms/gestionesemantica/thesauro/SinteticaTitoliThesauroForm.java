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

import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;

public class SinteticaTitoliThesauroForm extends AbstractSinteticaThesauriForm {


	private static final long serialVersionUID = 2728242252157276609L;
	private ParametriThesauro parametri;
	private DatiFusioneTerminiVO datiLegame = new DatiFusioneTerminiVO();
	private boolean initialized = false;
	private String[] titoliSelezionati;
	private boolean enableConferma = false;
	private boolean fusione = false;
	private String[] listaPulsanti;

	public String[] getListaPulsanti() {
		return listaPulsanti;
	}

	public void setListaPulsanti(String[] listaPulsanti) {
		this.listaPulsanti = listaPulsanti;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public DettaglioTermineThesauroVO[] getTermini() {
		return this.datiLegame != null ? new DettaglioTermineThesauroVO[] {
				datiLegame.getDid1(), datiLegame.getDid2() } : null;
	}

	public String[] getTitoliSelezionati() {
		return titoliSelezionati;
	}

	public void setTitoliSelezionati(String[] titoliSelezionati) {
		this.titoliSelezionati = titoliSelezionati;
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

	public DatiFusioneTerminiVO getDatiLegame() {
		return datiLegame;
	}

	public void setDatiLegame(DatiFusioneTerminiVO datiLegame) {
		this.datiLegame = datiLegame;
	}

	public boolean isFusione() {
		return fusione;
	}

	public void setFusione(boolean fusione) {
		this.fusione = fusione;
	}


}
