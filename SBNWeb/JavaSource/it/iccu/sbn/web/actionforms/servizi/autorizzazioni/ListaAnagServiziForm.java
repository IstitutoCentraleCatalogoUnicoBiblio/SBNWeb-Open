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
package it.iccu.sbn.web.actionforms.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaAnagServiziForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -2517421027211563493L;
	private boolean sessione = false;
	private List ServAssociati;
	private String codPolo;
	private String codBib;
	private List servDaAssociare;

	private boolean abilitaBlocchi;
	private int totBlocchi;

	public void setAbilitaBlocchi(boolean b) {
		abilitaBlocchi = b;
	}
	/**
	 * @return Returns the abilitaBlocchi.
	 */
	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}
	/**
	 * @return Returns the totBlocchi.
	 */
	public int getTotBlocchi() {
		return totBlocchi;
	}
	/**
	 * @param totBlocchi The totBlocchi to set.
	 */
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public List getServAssociati() {
		return ServAssociati;
	}
	public void setServAssociati(List servAssociati) {
		ServAssociati = servAssociati;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public ElementoSinteticaServizioVO getServiziDaAss(int index) {
		// automatically grow List size
		while (index >= this.servDaAssociare.size()) {
			this.servDaAssociare.add(new ElementoSinteticaServizioVO());
		}
		return (ElementoSinteticaServizioVO) this.servDaAssociare.get(index);
	}
	public ElementoSinteticaServizioVO getServDaAssociare(int index) {
		// automatically grow List size
		while (index >= this.servDaAssociare.size()) {
			this.servDaAssociare.add(new ElementoSinteticaServizioVO());
		}
		return (ElementoSinteticaServizioVO) this.servDaAssociare.get(index);
	}

	public List getServDaAssociare() {
		return servDaAssociare;
	}
	public void setServDaAssociare(List servDaAssociare) {
		this.servDaAssociare = servDaAssociare;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		List<ElementoSinteticaServizioVO> servizi = this.servDaAssociare;
		if (servizi == null) return;
		for (ElementoSinteticaServizioVO s: servizi) {
			s.resetCancella();
		}

	}

}
