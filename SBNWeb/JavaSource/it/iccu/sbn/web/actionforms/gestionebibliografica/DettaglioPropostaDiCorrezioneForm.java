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
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazionePropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaProposteDiCorrezioneView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;



public class DettaglioPropostaDiCorrezioneForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -545960657469443128L;
	AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVO = new AreaDatiPropostaDiCorrezioneVO();
	AreaDatiVariazionePropostaDiCorrezioneVO areaDatiVariazionePropostaDiCorrezioneVO = new AreaDatiVariazionePropostaDiCorrezioneVO();
	SinteticaProposteDiCorrezioneView proposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();

	private String tipoProspettazione;
	private List listaSintetica;
	private List listaBibliotecari;
	private List listaStatiProposta;
	private List listaDestinatariProp;
	private String selezRadio;

	public List getListaBibliotecari() {
		return listaBibliotecari;
	}

	public void setListaBibliotecari(List listaBibliotecari) {
		this.listaBibliotecari = listaBibliotecari;
	}


	public List addListaBibliotecari(ComboCodDescVO elemBiblio) {
		if (listaBibliotecari == null) {
			listaBibliotecari = new ArrayList();
		}
		listaBibliotecari.add(elemBiblio);
		return listaBibliotecari;
	}

	public List addListaStatiProposta(ComboCodDescVO elemStati) {
		if (listaStatiProposta == null) {
			listaStatiProposta = new ArrayList();
		}
		listaStatiProposta.add(elemStati);
		return listaStatiProposta;
	}

	public AreaDatiPropostaDiCorrezioneVO getAreaDatiPropostaDiCorrezioneVO() {
		return areaDatiPropostaDiCorrezioneVO;
	}

	public void setAreaDatiPropostaDiCorrezioneVO(
			AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVO) {
		this.areaDatiPropostaDiCorrezioneVO = areaDatiPropostaDiCorrezioneVO;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}


	public AreaDatiVariazionePropostaDiCorrezioneVO getAreaDatiVariazionePropostaDiCorrezioneVO() {
		return areaDatiVariazionePropostaDiCorrezioneVO;
	}

	public void setAreaDatiVariazionePropostaDiCorrezioneVO(
			AreaDatiVariazionePropostaDiCorrezioneVO areaDatiVariazionePropostaDiCorrezioneVO) {
		this.areaDatiVariazionePropostaDiCorrezioneVO = areaDatiVariazionePropostaDiCorrezioneVO;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public SinteticaProposteDiCorrezioneView getProposteDiCorrezioneView() {
		return proposteDiCorrezioneView;
	}

	public void setProposteDiCorrezioneView(
			SinteticaProposteDiCorrezioneView proposteDiCorrezioneView) {
		this.proposteDiCorrezioneView = proposteDiCorrezioneView;
	}

	public List getListaStatiProposta() {
		return listaStatiProposta;
	}

	public void setListaStatiProposta(List listaStatiProposta) {
		this.listaStatiProposta = listaStatiProposta;
	}

	public List getListaDestinatariProp() {
		return listaDestinatariProp;
	}

	public void setListaDestinatariProp(List listaDestinatariProp) {
		this.listaDestinatariProp = listaDestinatariProp;
	}

}
