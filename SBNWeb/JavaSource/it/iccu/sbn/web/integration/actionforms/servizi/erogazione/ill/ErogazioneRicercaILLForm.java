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
package it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ill;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.web.actionforms.servizi.ServiziBaseForm;

import java.util.ArrayList;
import java.util.List;

public class ErogazioneRicercaILLForm extends ServiziBaseForm {


	private static final long serialVersionUID = 8458766033751874200L;

	private List<DatiRichiestaILLVO> richieste = new ArrayList<DatiRichiestaILLVO>();

	private Integer[] selected;
	private Integer[] selectedItems;

	private RuoloBiblioteca folder;

	private DatiRichiestaILLRicercaVO ricerca = new DatiRichiestaILLRicercaVO();
	private List<TB_CODICI> listaStatoRichiestaILL;
	private List<TB_CODICI> listaStatoRichiestaLocale;
	private List<TB_CODICI> listaTipoOrdinamento;

	private BibliotecaVO bibliotecaRichiedente = new BibliotecaVO();
	private BibliotecaVO bibliotecaFornitrice = new BibliotecaVO();

	public List<DatiRichiestaILLVO> getRichieste() {
		return richieste;
	}

	public void setRichieste(List<DatiRichiestaILLVO> richieste) {
		this.richieste = richieste;
	}

	public Integer[] getSelected() {
		return selected;
	}

	public void setSelected(Integer[] selectedILL) {
		this.selected = selectedILL;
	}

	public Integer[] getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Integer[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	public RuoloBiblioteca getFolder() {
		return folder;
	}

	public void setFolder(RuoloBiblioteca folder) {
		this.folder = folder;
	}

	public DatiRichiestaILLRicercaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(DatiRichiestaILLRicercaVO ricerca) {
		this.ricerca = ricerca;
	}

	public List<TB_CODICI> getListaStatoRichiestaILL() {
		return listaStatoRichiestaILL;
	}

	public void setListaStatoRichiestaILL(List<TB_CODICI> listaStatoRichiestaILL) {
		this.listaStatoRichiestaILL = listaStatoRichiestaILL;
	}

	public List<TB_CODICI> getListaStatoRichiestaLocale() {
		return listaStatoRichiestaLocale;
	}

	public void setListaStatoRichiestaLocale(
			List<TB_CODICI> listaStatoRichiestaLocale) {
		this.listaStatoRichiestaLocale = listaStatoRichiestaLocale;
	}

	public List<TB_CODICI> getListaTipoOrdinamento() {
		return listaTipoOrdinamento;
	}

	public BibliotecaVO getBibliotecaRichiedente() {
		return bibliotecaRichiedente;
	}

	public void setBibliotecaRichiedente(BibliotecaVO bibliotecaRichiedente) {
		this.bibliotecaRichiedente = bibliotecaRichiedente;
	}

	public BibliotecaVO getBibliotecaFornitrice() {
		return bibliotecaFornitrice;
	}

	public void setBibliotecaFornitrice(BibliotecaVO bibliotecaFornitrice) {
		this.bibliotecaFornitrice = bibliotecaFornitrice;
	}

	public void setListaTipoOrdinamento(List<TB_CODICI> listaTipoOrdinamento) {
		this.listaTipoOrdinamento = listaTipoOrdinamento;
	}

}
