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
package it.iccu.sbn.ejb.vo.periodici.kardex;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.util.cloning.ClonePool;

import java.util.ArrayList;
import java.util.List;

public class KardexPeriodicoVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -3618316554485908586L;
	private List<FascicoloVO> fascicoli = ValidazioneDati.emptyList();
	private KardexIntestazioneVO intestazione;
	private SeriePeriodicoType tipo;
	private BibliotecaVO biblioteca;
	private String bid;
	private String tipoPeriodicita;

	private TipoRangeKardex tipoRange;
	protected List<Integer> rangeAnnoPubb = ValidazioneDati.emptyList();


	public KardexPeriodicoVO(KardexPeriodicoVO kardex) {
		this.intestazione = (KardexIntestazioneVO) (kardex.intestazione == null ? null : kardex.intestazione.clone());
		this.tipo = kardex.tipo;
		this.biblioteca = kardex.biblioteca == null ? null : new BibliotecaVO(kardex.biblioteca);
		this.bid = kardex.bid;
		this.tipoPeriodicita = kardex.tipoPeriodicita;
		this.tipoRange = kardex.tipoRange;

		try {
			//this.fascicoli = ClonePool.deepCopy(kardex.fascicoli);
			int size = ValidazioneDati.size(kardex.fascicoli);
			this.fascicoli = new ArrayList<FascicoloVO>(size);
			for (int idx = 0; idx < size; idx++)
				this.fascicoli.add(kardex.fascicoli.get(idx).copyThis());

			this.rangeAnnoPubb = ClonePool.deepCopy(kardex.rangeAnnoPubb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public KardexPeriodicoVO(SeriePeriodicoType tipo) {
		super();
		this.tipo = tipo;
	}

	public void setFascicoli(List<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}

	public KardexIntestazioneVO getIntestazione() {
		return intestazione;
	}

	public void setIntestazione(KardexIntestazioneVO intestazione) {
		this.intestazione = intestazione;
	}

	public SeriePeriodicoType getTipo() {
		return tipo;
	}

	@Override
	public boolean isEmpty() {
		return !isFilled(fascicoli);
	}

	public BibliotecaVO getBiblioteca() {
		return biblioteca;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTipoPeriodicita() {
		return tipoPeriodicita;
	}

	public void setTipoPeriodicita(String tipoPeriodicita) {
		this.tipoPeriodicita = tipoPeriodicita;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public TipoRangeKardex getTipoRange() {
		return tipoRange;
	}

	public void setTipoRange(TipoRangeKardex tipoRange) {
		this.tipoRange = tipoRange;
	}

	public List<Integer> getRangeAnnoPubb() {
		return rangeAnnoPubb;
	}

	public void setRangeAnnoPubb(List<Integer> rangeAnnoPubb) {
		this.rangeAnnoPubb = rangeAnnoPubb;
	}

}
