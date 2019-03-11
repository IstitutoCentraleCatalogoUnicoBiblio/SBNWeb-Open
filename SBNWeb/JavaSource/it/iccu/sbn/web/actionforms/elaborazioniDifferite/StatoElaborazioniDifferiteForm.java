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

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class StatoElaborazioniDifferiteForm extends ActionForm {


	private static final long serialVersionUID = -852761101756984624L;
	int bloccoCorrente;
	private boolean abilitaBlocchi;
	private int totBlocchi;

	private Collection<StrutturaCombo> listaVisibilita;
	private String visibilitaSelez;

	private Collection<StrutturaCombo> listaProcedure;
	private String proceduraSelez;
	private String identificativo;
	private Collection<StrutturaCombo> listaDataEsecuzioneProgrammata;
	private String dataEsecuzioneProgrammataSelez;
	private Collection<StrutturaCombo> listaRichiedente;
	private String richiedenteSelez;
	private Collection<StrutturaCombo> listaStato;
	private String statoSelez;
	private Collection<StrutturaCombo> listaTipiOrdinamento;
	private String tipoOrdinamSelez;
	private int elemXBlocchi = Integer.parseInt(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private Collection<StrutturaCombo> listaElementiPerBlocco;
	private String elementiPerBloccoSelez = ConstantDefault.ELEMENTI_BLOCCHI.getDefault();

	private boolean disabilitaTutto = false;
	private boolean sessione;
	private String codiceBibl;
	private RichiestaElaborazioniDifferiteVO ricerca = new RichiestaElaborazioniDifferiteVO();

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public Collection<StrutturaCombo> getListaDataEsecuzioneProgrammata() {
		return listaDataEsecuzioneProgrammata;
	}

	public void setListaDataEsecuzioneProgrammata(
			Collection<StrutturaCombo> listaDataEsecuzioneProgrammata) {
		this.listaDataEsecuzioneProgrammata = listaDataEsecuzioneProgrammata;
	}

	public Collection<StrutturaCombo> getListaProcedure() {
		return listaProcedure;
	}

	public void setListaProcedure(Collection<StrutturaCombo> listaProcedure) {
		this.listaProcedure = listaProcedure;
	}

	public Collection<StrutturaCombo> getListaRichiedente() {
		return listaRichiedente;
	}

	public void setListaRichiedente(Collection<StrutturaCombo> listaRichiedente) {
		this.listaRichiedente = listaRichiedente;
	}

	public Collection<StrutturaCombo> getListaStato() {
		return listaStato;
	}

	public void setListaStato(Collection<StrutturaCombo> listaStato) {
		this.listaStato = listaStato;
	}

	public Collection<StrutturaCombo> getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(Collection<StrutturaCombo> listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
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

	public String getDataEsecuzioneProgrammataSelez() {
		return dataEsecuzioneProgrammataSelez;
	}

	public void setDataEsecuzioneProgrammataSelez(
			String dataEsecuzioneProgrammataSelez) {
		this.dataEsecuzioneProgrammataSelez = dataEsecuzioneProgrammataSelez;
	}

	public String getProceduraSelez() {
		return proceduraSelez;
	}

	public void setProceduraSelez(String proceduraSelez) {
		this.proceduraSelez = proceduraSelez;
	}

	public String getRichiedenteSelez() {
		return richiedenteSelez;
	}

	public void setRichiedenteSelez(String richiedenteSelez) {
		this.richiedenteSelez = richiedenteSelez;
	}

	public String getStatoSelez() {
		return statoSelez;
	}

	public void setStatoSelez(String statoSelez) {
		this.statoSelez = statoSelez;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getElementiPerBloccoSelez() {
		return elementiPerBloccoSelez;
	}

	public void setElementiPerBloccoSelez(String elementiPerBloccoSelez) {
		this.elementiPerBloccoSelez = elementiPerBloccoSelez;
	}

	public Collection<StrutturaCombo> getListaElementiPerBlocco() {
		return listaElementiPerBlocco;
	}

	public void setListaElementiPerBlocco(Collection<StrutturaCombo> listaElementiPerBlocco) {
		this.listaElementiPerBlocco = listaElementiPerBlocco;
	}

	public Collection<StrutturaCombo> getListaVisibilita() {
		return listaVisibilita;
	}

	public void setListaVisibilita(Collection<StrutturaCombo> listaVisibilita) {
		this.listaVisibilita = listaVisibilita;
	}

	public String getVisibilitaSelez() {
		return visibilitaSelez;
	}

	public void setVisibilitaSelez(String visibilitaSelez) {
		this.visibilitaSelez = visibilitaSelez;
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public int getBloccoCorrente() {
		return bloccoCorrente;
	}

	public void setBloccoCorrente(int bloccoCorrente) {
		this.bloccoCorrente = bloccoCorrente;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public RichiestaElaborazioniDifferiteVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RichiestaElaborazioniDifferiteVO ricerca) {
		this.ricerca = ricerca;
	}

}
