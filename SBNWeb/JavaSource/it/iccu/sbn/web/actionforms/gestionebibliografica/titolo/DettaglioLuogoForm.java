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
//	SBNWeb - Rifacimento ClientServer
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class DettaglioLuogoForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 7396581518099937229L;
	private String tipoProspettazione;
	private boolean flagCondiviso;

	private List listaRelatorCode;
	private String descRelatorCode;

    private List listaLivAut;
	private String descLivAut;
	private List listaFormaNome;
	private String descFormaNome;
	private List listaPaese;
	private String descPaese;

	private String bidPerRientroAnalitica;

	private DettaglioLuogoGeneraleVO dettLuoComVO = new DettaglioLuogoGeneraleVO();

	// Aree della sintetica utilizzate per la prospettazione degli oggetti in creazione legame titolo
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();


	// Evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità
	// di gestire i campi nota informativa , nota catalogatore e legame a repertori
	private List<TabellaNumSTDImpronteVO> listaRepertoriModificato;
	private String selezRadioRepertori;

	public DettaglioLuogoGeneraleVO getDettLuoComVO() {
		return dettLuoComVO;
	}

	public void setDettLuoComVO(DettaglioLuogoGeneraleVO dettLuoComVO) {
		this.dettLuoComVO = dettLuoComVO;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public String getDescRelatorCode() {
		return descRelatorCode;
	}

	public void setDescRelatorCode(String descRelatorCode) {
		this.descRelatorCode = descRelatorCode;
	}

	public List getListaRelatorCode() {
		return listaRelatorCode;
	}

	public void setListaRelatorCode(List listaRelatorCode) {
		this.listaRelatorCode = listaRelatorCode;
	}

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public DettaglioLuogoForm() {
		super();
		this.listaRepertoriModificato = new ArrayList<TabellaNumSTDImpronteVO>();
	}
	public List<TabellaNumSTDImpronteVO> getListaRepertoriModificato() {
		return listaRepertoriModificato;
	}

	public void setListaRepertoriModificato(
			List<TabellaNumSTDImpronteVO> listaRepertoriModificato) {
		this.listaRepertoriModificato = listaRepertoriModificato;
	}

	public List addListaRepertoriModificato(TabellaNumSTDImpronteVO tabRep) {
		listaRepertoriModificato.add(tabRep);
		return listaRepertoriModificato;
	}

	public TabellaNumSTDImpronteVO getItem(int idxRepert) {

        // automatically grow List size
        while (idxRepert >= this.getListaRepertoriModificato().size()) {
        	this.getListaRepertoriModificato().add(new TabellaNumSTDImpronteVO());
        }
        return this.getListaRepertoriModificato().get(idxRepert);
    }

	public String getSelezRadioRepertori() {
		return selezRadioRepertori;
	}

	public void setSelezRadioRepertori(String selezRadioRepertori) {
		this.selezRadioRepertori = selezRadioRepertori;
	}

	public String getDescLivAut() {
		return descLivAut;
	}

	public void setDescLivAut(String descLivAut) {
		this.descLivAut = descLivAut;
	}

	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public String getDescFormaNome() {
		return descFormaNome;
	}

	public void setDescFormaNome(String descFormaNome) {
		this.descFormaNome = descFormaNome;
	}

	public List getListaFormaNome() {
		return listaFormaNome;
	}

	public void setListaFormaNome(List listaFormaNome) {
		this.listaFormaNome = listaFormaNome;
	}

	public String getDescPaese() {
		return descPaese;
	}

	public void setDescPaese(String descPaese) {
		this.descPaese = descPaese;
	}

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public String getBidPerRientroAnalitica() {
		return bidPerRientroAnalitica;
	}

	public void setBidPerRientroAnalitica(String bidPerRientroAnalitica) {
		this.bidPerRientroAnalitica = bidPerRientroAnalitica;
	}

}
