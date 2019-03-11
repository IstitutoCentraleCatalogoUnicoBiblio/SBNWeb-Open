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

import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaUtentiForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 4717966899380093087L;
	private boolean conferma = false;
	private boolean sessione = false;
	private String pathForm;
	private String richiesta = null;
	private List listaUtenti;
	private String[] codUtente;
	private int codUtenteSing;
	private RicercaUtenteBibliotecaVO ric;
	private UtenteBibliotecaVO uteTrovato;
	private String dataRinnovoAut;
	private boolean utilUtenti = false;
	// private boolean abilitaBlocchi;
	// private int totBlocchi;
	private String livelloRicerca;
	private boolean listaUtentiPolo;

	private String[] codSelUte = new String[0];
	private String[] selectedUtenti;
	private int numUtenti = 0;
	boolean abilitaCercaInPolo = false;
	private Set<String> idUtentiTrovatiInBibl;
	private String[] pulsanti;

	/**
	 * @return Returns the totBlocchi.
	 */
	// public int getTotBlocchi() {
	// return totBlocchi;
	// }
	/**
	 * @param totBlocchi
	 *            The totBlocchi to set.
	 */
	// public void setTotBlocchi(int totBlocchi) {
	// this.totBlocchi = totBlocchi;
	// }

	// public void setAbilitaBlocchi(boolean b) {
	// abilitaBlocchi = b;
	// }
	/**
	 * @return Returns the abilitaBlocchi.
	 */
	// public boolean isAbilitaBlocchi() {
	// return abilitaBlocchi;
	// }

	public String getDataRinnovoAut() {
		return dataRinnovoAut;
	}

	public void setDataRinnovoAut(String dataRinnovoAut) {
		this.dataRinnovoAut = dataRinnovoAut;
	}

	public List getListaUtenti() {
		return listaUtenti;
	}

	public void setListaUtenti(List listaUtenti) {
		this.listaUtenti = listaUtenti;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String[] getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String[] codUtente) {
		this.codUtente = codUtente;
	}

	public RicercaUtenteBibliotecaVO getRic() {
		return ric;
	}

	public void setRic(RicercaUtenteBibliotecaVO ric) {
		this.ric = ric;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}

	public String getPathForm() {
		return pathForm;
	}

	public void setPathForm(String pathForm) {
		this.pathForm = pathForm;
	}

	public boolean isUtilUtenti() {
		return utilUtenti;
	}

	public void setUtilUtenti(boolean utilUtenti) {
		this.utilUtenti = utilUtenti;
	}

	public UtenteBibliotecaVO getUteTrovato() {
		return uteTrovato;
	}

	public void setUteTrovato(UtenteBibliotecaVO uteTrovato) {
		this.uteTrovato = uteTrovato;
	}

	public void setLivelloRicerca(String codBib) {
		this.livelloRicerca = codBib;
	}

	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public int getCodUtenteSing() {
		return codUtenteSing;
	}

	public void setCodUtenteSing(int codUtenteSing) {
		this.codUtenteSing = codUtenteSing;
	}

	public void setListaUtentiPolo(boolean b) {
		this.listaUtentiPolo = b;
	}

	public boolean isListaUtentiPolo() {
		return listaUtentiPolo;
	}

	public String[] getCodSelUte() {
		return codSelUte;
	}

	public void setCodSelUte(String[] codSelUte) {
		this.codSelUte = codSelUte;
	}

	public String[] getSelectedUtenti() {
		return selectedUtenti;
	}

	public void setSelectedUtenti(String[] selectedUtenti) {
		this.selectedUtenti = selectedUtenti;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// this.codSelUte=new String[]{};
		// this.selectedUtenti=new String[0];
	}

	public int getNumUtenti() {
		return numUtenti;
	}

	public void setNumUtenti(int numUtenti) {
		this.numUtenti = numUtenti;
	}

	public boolean isAbilitaCercaInPolo() {
		return abilitaCercaInPolo;
	}

	public void setAbilitaCercaInPolo(boolean abilitaCercaInPolo) {
		this.abilitaCercaInPolo = abilitaCercaInPolo;
	}

	public Set<String> getIdUtentiTrovatiInBibl() {
		return idUtentiTrovatiInBibl;
	}

	public void setIdUtentiTrovatiInBibl(Set<String> idUtentiTrovatiInBibl) {
		this.idUtentiTrovatiInBibl = idUtentiTrovatiInBibl;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}
}
