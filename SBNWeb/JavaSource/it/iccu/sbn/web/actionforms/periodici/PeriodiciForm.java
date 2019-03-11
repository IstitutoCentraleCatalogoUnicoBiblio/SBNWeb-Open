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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PeriodiciForm extends PeriodiciBaseForm {


	private static final long serialVersionUID = 3598728144908673038L;
	private List<ComboCodDescVO> comboGestioneEsamina;
	private String idFunzioneEsamina;
	private DatiBibliograficiPeriodicoVO datiBibliografici;
	private EsameSeriePeriodicoVO esame = new EsameSeriePeriodicoVO();
	private RicercaKardexPeriodicoVO<FascicoloVO> paramRicercaKardex = new RicercaKardexPeriodicoVO<FascicoloVO>();

	private int selectedItem;
	private String bidSelezionato;

	private int bloccoSelezionato;
	private Set<Integer> blocchiCaricati = new HashSet<Integer>();


	public void setComboGestioneEsamina(List<ComboCodDescVO> comboGestioneEsamina) {
		this.comboGestioneEsamina = comboGestioneEsamina;

	}

	public List<ComboCodDescVO> getComboGestioneEsamina() {
		return comboGestioneEsamina;
	}

	public void setIdFunzioneEsamina(String idFunzioneEsamina) {
		this.idFunzioneEsamina = idFunzioneEsamina;
	}

	public String getIdFunzioneEsamina() {
		return idFunzioneEsamina;
	}

	public void setDatiBibliografici(DatiBibliograficiPeriodicoVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public DatiBibliograficiPeriodicoVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public EsameSeriePeriodicoVO getEsame() {
		return esame;
	}

	public void setEsame(EsameSeriePeriodicoVO esame) {
		this.esame = esame;
	}

	public String getBidSelezionato() {
		return bidSelezionato;
	}

	public void setBidSelezionato(String bidSelezionato) {
		this.bidSelezionato = bidSelezionato;
	}

	public RicercaKardexPeriodicoVO<FascicoloVO> getParamRicercaKardex() {
		return paramRicercaKardex;
	}

	public void setParamRicercaKardex(RicercaKardexPeriodicoVO<FascicoloVO> ricercaKardex) {
		this.paramRicercaKardex = ricercaKardex;
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

	public int getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(int selected) {
		this.selectedItem = selected;
	}

}
