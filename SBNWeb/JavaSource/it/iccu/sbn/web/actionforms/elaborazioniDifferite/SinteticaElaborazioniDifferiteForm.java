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
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SinteticaElaborazioniDifferiteForm extends ActionForm {


	private static final long serialVersionUID = 790154552582017446L;
	private boolean disabilitaTutto = false;
	private boolean sessione;
	private String codiceBibl;
	private List<ElementoSinteticaElabDiffVO> listaRichieste;
	private String[] richiesteSelez;

	private int progressivoSintetica = 0;
	private String idLista;
	private int totRighe;
	private int totBlocchi;
	private int numBlocco;
	private int maxRighe;
	private RichiestaElaborazioniDifferiteVO ricerca = new RichiestaElaborazioniDifferiteVO();

	public List<ElementoSinteticaElabDiffVO> getListaRichieste() {
		return listaRichieste;
	}

	public void setListaRichieste(
			List<ElementoSinteticaElabDiffVO> listaRichieste) {
		this.listaRichieste = listaRichieste;
	}

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

	public int getProgressivoSintetica() {
		return progressivoSintetica;
	}

	public void setProgressivoSintetica(int progressivoSintetica) {
		this.progressivoSintetica = progressivoSintetica;
	}

	public String[] getRichiesteSelez() {
		return richiesteSelez;
	}

	public void setRichiesteSelez(String[] richiesteSelez) {
		this.richiesteSelez = richiesteSelez;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;

	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;

	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public String getIdLista() {
		return idLista;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public RichiestaElaborazioniDifferiteVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RichiestaElaborazioniDifferiteVO ricerca) {
		this.ricerca = ricerca;
	}

}
