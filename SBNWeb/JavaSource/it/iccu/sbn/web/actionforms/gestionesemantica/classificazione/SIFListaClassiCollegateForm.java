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
package it.iccu.sbn.web.actionforms.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ParametriClassi;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.sif.AttivazioneSIFListaClassiCollegateVO;

public class SIFListaClassiCollegateForm extends AbstractSinteticaClassiForm {


	private static final long serialVersionUID = -3838008494025440748L;
	private boolean initialized;
	private String codSelezionato;
	private AttivazioneSIFListaClassiCollegateVO datiSIF;
	private RicercaClasseListaVO output = new RicercaClasseListaVO();
	private ParametriClassi parametri;

	public ParametriClassi getParametri() {
		return parametri;
	}

	public void setParametri(ParametriClassi parametri) {
		this.parametri = parametri;
	}

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public RicercaClasseListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaClasseListaVO output) {
		this.output = output;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public AttivazioneSIFListaClassiCollegateVO getDatiSIF() {
		return datiSIF;
	}

	public void setDatiSIF(AttivazioneSIFListaClassiCollegateVO datiSIF) {
		this.datiSIF = datiSIF;
	}

}
