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
package it.iccu.sbn.web.integration.actionforms.servizi.erogazione.prenotazioni;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class ListaPrenotazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -2458537577534128173L;

	private String richiesta;

	private String chiamante;

	private boolean sessione = false;

	private boolean conferma = false;

	//valorizzato se si arriva a lista prenotazioni da dettaglio movimento
	private MovimentoVO movimentoSelezionato;

	private List<MovimentoListaVO> listaPrenotazioni;

	private Long[] codSel;

	private String codSelSing;

	private List<ComboCodDescVO> ordinamentiPrenotazioni;

	private String ordinamento;


	//gestione blocchi
	private boolean abilitaBlocchi;

	private int totBlocchi;

	private String livelloRicerca;




	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public Long[] getCodSel() {
		return codSel;
	}
	public void setCodSel(Long[] codSel) {
		this.codSel = codSel;
	}
	public String getCodSelSing() {
		return codSelSing;
	}
	public void setCodSelSing(String codSelSing) {
		this.codSelSing = codSelSing;
	}
	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}
	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}
	public int getTotBlocchi() {
		return totBlocchi;
	}
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public String getLivelloRicerca() {
		return livelloRicerca;
	}
	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}
	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public List<MovimentoListaVO> getListaPrenotazioni() {
		return listaPrenotazioni;
	}
	public void setListaPrenotazioni(List<MovimentoListaVO> listaPrenotazioni) {
		this.listaPrenotazioni = listaPrenotazioni;
	}
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public MovimentoVO getMovimentoSelezionato() {
		return movimentoSelezionato;
	}
	public void setMovimentoSelezionato(MovimentoVO movimentoSelezionato) {
		this.movimentoSelezionato = movimentoSelezionato;
	}
	public List<ComboCodDescVO> getOrdinamentiPrenotazioni() {
		return ordinamentiPrenotazioni;
	}
	public void setOrdinamentiPrenotazioni(
			List<ComboCodDescVO> ordinamentiPrenotazioni) {
		this.ordinamentiPrenotazioni = ordinamentiPrenotazioni;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}


}
