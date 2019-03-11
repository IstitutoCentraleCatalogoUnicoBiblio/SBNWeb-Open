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
package it.iccu.sbn.web.actionforms.servizi.utenti;

import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaServiziForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -4699376812975893872L;
	private boolean sessione = false;
	private List elencoScegliServizio;
	private List ServSelezionati;
	private List ServAnagrafe;
	private String codPolo;
	private String codBib;
	private String codUte;
	private String codBibUte;

	private boolean abilitaBlocchi;
	private int totBlocchi;

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
	public void setAbilitaBlocchi(boolean b) {
		abilitaBlocchi = b;
	}
	/**
	 * @return Returns the abilitaBlocchi.
	 */
	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public String getCodBibUte() {
		return codBibUte;
	}
	public void setCodBibUte(String codBibUte) {
		this.codBibUte = codBibUte;
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
	public List getServAnagrafe() {
		return ServAnagrafe;
	}
	public void setServAnagrafe(List servAnagrafe) {
		ServAnagrafe = servAnagrafe;
	}
	public List getElencoScegliServizio() {
		return elencoScegliServizio;
	}
	public void setElencoScegliServizio(List elencoScegliServizio) {
		this.elencoScegliServizio = elencoScegliServizio;
	}
    public ServizioVO getElencoScegliServizio(int index) {
        // automatically grow List size
        while (index >= this.elencoScegliServizio.size()) {
        	this.elencoScegliServizio.add(new ServizioVO());
        }
        return (ServizioVO)this.elencoScegliServizio.get(index);
    }
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List getServSelezionati() {
		return ServSelezionati;
	}
	public void setServSelezionati(List servSelezionati) {
		ServSelezionati = servSelezionati;
	}
	public String getCodUte() {
		return codUte;
	}
	public void setCodUte(String codUte) {
		this.codUte = codUte;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		List<ServizioVO> servizi = this.elencoScegliServizio;
		if (servizi == null) return;
		for (ServizioVO s: servizi) {
			s.resetCancella();
		}
	//	this.resetMateria(mapping, request);
	}


}
