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
package it.iccu.sbn.web.actionforms.periodici;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListaOrdiniPeriodiciForm extends PeriodiciBaseForm {


	private static final long serialVersionUID = 3477884267826881500L;
	private List<SerieOrdineVO> listaOrdini = new ArrayList<SerieOrdineVO>();
	private DescrittoreBloccoVO blocco;
	private int bloccoSelezionato;
	private Set<Integer> blocchiCaricati = new HashSet<Integer>();
	private RicercaOrdiniPeriodicoVO ricerca;
	private int selected;
	private EsameSeriePeriodicoVO esame = new EsameSeriePeriodicoVO();
	private boolean bilancioAttivo;

	public List<SerieOrdineVO> getListaOrdini() {
		return listaOrdini;
	}

	public void setListaOrdini(List<SerieOrdineVO> listaOrdini) {
		this.listaOrdini = listaOrdini;
	}

	public DescrittoreBloccoVO getBlocco() {
		return blocco;
	}

	public void setBlocco(DescrittoreBloccoVO blocco) {
		this.blocco = blocco;
	}

	public RicercaOrdiniPeriodicoVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RicercaOrdiniPeriodicoVO ricerca) {
		this.ricerca = ricerca;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public EsameSeriePeriodicoVO getEsame() {
		return esame;
	}

	public void setEsame(EsameSeriePeriodicoVO esame) {
		this.esame = esame;
	}

	public boolean isBilancioAttivo() {
		return bilancioAttivo;
	}

	public void setBilancioAttivo(boolean bilancioAttivo) {
		this.bilancioAttivo = bilancioAttivo;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public Set<Integer> getBlocchiCaricati() {
		return blocchiCaricati;
	}

	public void setBlocchiCaricati(Set<Integer> blocchiCaricati) {
		this.blocchiCaricati = blocchiCaricati;
	}

}
