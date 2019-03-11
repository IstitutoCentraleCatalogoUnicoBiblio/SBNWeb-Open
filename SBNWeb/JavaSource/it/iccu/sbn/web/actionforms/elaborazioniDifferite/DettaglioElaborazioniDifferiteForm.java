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
package it.iccu.sbn.web.actionforms.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElementoSinteticaElabDiffVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class DettaglioElaborazioniDifferiteForm extends ActionForm {


	private static final long serialVersionUID = 7168946307767448568L;
	private boolean disabilitaTutto = false;
	private boolean sessione;
	private String codiceBibl;
	private String[] pulsanti;

	private List<ElementoSinteticaElabDiffVO> listaRichieste;
	private String[] richiesteSelez;
	private boolean conferma = false;

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getCodiceBibl() {
		return codiceBibl;
	}

	public void setCodiceBibl(String codiceBibl) {
		this.codiceBibl = codiceBibl;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public List<ElementoSinteticaElabDiffVO> getListaRichieste() {
		return listaRichieste;
	}

	public void setListaRichieste(
			List<ElementoSinteticaElabDiffVO> listaRichieste) {
		this.listaRichieste = listaRichieste;
	}

	public String[] getRichiesteSelez() {
		return richiesteSelez;
	}

	public void setRichiesteSelez(String[] richiesteSelez) {
		this.richiesteSelez = richiesteSelez;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

}
