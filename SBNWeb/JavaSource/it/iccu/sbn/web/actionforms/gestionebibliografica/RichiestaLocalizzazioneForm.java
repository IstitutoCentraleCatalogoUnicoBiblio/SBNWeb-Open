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
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.LocalizzazioneMassivaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import org.apache.struts.upload.FormFile;

public class RichiestaLocalizzazioneForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 2470473584325032573L;
	private boolean initialized;
	private LocalizzazioneMassivaVO richiesta = new LocalizzazioneMassivaVO();
	private String[] pulsanti;
	private FormFile input;

	private String pendingMessage;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public LocalizzazioneMassivaVO getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(LocalizzazioneMassivaVO richiesta) {
		this.richiesta = richiesta;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public String getPendingMessage() {
		return pendingMessage;
	}

	public void setPendingMessage(String pendingMessage) {
		this.pendingMessage = pendingMessage;
	}

	public FormFile getInput() {
		return input;
	}

	public void setInput(FormFile input) {
		this.input = input;
	}
}
