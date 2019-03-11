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

import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PeriodiciListaForm extends PeriodiciBaseForm {


	private static final long serialVersionUID = -7447847127477763101L;
	public static final String SUBMIT_BUTTON_POSIZIONA = "btn.fasc.posiziona";
	private Integer[] selected;
	private int bloccoSelezionato;
	private Set<Integer> blocchiCaricati = new HashSet<Integer>();
	private List<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
	private String posizionaTop;
	private String posizionaBottom;
	private KardexPeriodicoVO kardex;
	private InventarioVO inventario;
	private OggettoRiferimentoVO oggettoRiferimento = new OggettoRiferimentoVO();
	private RicercaKardexPeriodicoVO<FascicoloVO> ricercaKardex = new RicercaKardexPeriodicoVO<FascicoloVO>();

	public PeriodiciListaForm() {
		super();
	}

	public Integer[] getSelected() {
		return selected;
	}

	public void setSelected(Integer[] selected) {
		this.selected = selected;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public List<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(List<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}

	public Set<Integer> getBlocchiCaricati() {
		return blocchiCaricati;
	}

	public void setBlocchiCaricati(Set<Integer> blocchiCaricati) {
		this.blocchiCaricati = blocchiCaricati;
	}

	public String getSUBMIT_BUTTON_POSIZIONA() {
		return SUBMIT_BUTTON_POSIZIONA;
	}

	public String getPosizionaTop() {
		return posizionaTop;
	}

	public void setPosizionaTop(String posizionaTop) {
		this.posizionaTop = posizionaTop;
	}

	public String getPosizionaBottom() {
		return posizionaBottom;
	}

	public void setPosizionaBottom(String posizionaBottom) {
		this.posizionaBottom = posizionaBottom;
	}

	public KardexPeriodicoVO getKardex() {
		return kardex;
	}

	public void setKardex(KardexPeriodicoVO kardex) {
		this.kardex = kardex;
	}

	public InventarioVO getInventario() {
		return inventario;
	}

	public void setInventario(InventarioVO inventario) {
		this.inventario = inventario;
	}

	public OggettoRiferimentoVO getOggettoRiferimento() {
		return oggettoRiferimento;
	}

	public void setOggettoRiferimento(OggettoRiferimentoVO oggettoRiferimento) {
		this.oggettoRiferimento = oggettoRiferimento;
	}

	public RicercaKardexPeriodicoVO<FascicoloVO> getRicercaKardex() {
		return ricercaKardex;
	}

	public void setRicercaKardex(RicercaKardexPeriodicoVO<FascicoloVO> ricercaKardex) {
		this.ricercaKardex = ricercaKardex;
	}

}
