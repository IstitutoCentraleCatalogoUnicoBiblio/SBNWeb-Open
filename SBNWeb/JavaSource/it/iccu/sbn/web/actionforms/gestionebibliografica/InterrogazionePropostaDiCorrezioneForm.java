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



public class InterrogazionePropostaDiCorrezioneForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -9055739395149634297L;
	AreaDatiPropostaDiCorrezioneVO areaDatiPropostaDiCorrezioneVO = new AreaDatiPropostaDiCorrezioneVO();
	AreaDatiVariazionePropostaDiCorrezioneVO areaDatiVariazionePropostaDiCorrezioneVO = new AreaDatiVariazionePropostaDiCorrezioneVO();
	SinteticaProposteDiCorrezioneView proposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();

	private String tipoProspettazione;
	private List listaSintetica;
	private List listaBiliotecari;
	private List listaStatiProposta;

	private String selezRadio;
	private String tastoCreaPresente;


	// Inizio Modifica almaviva2 BUG 3900 26.10.2010 i check richiestiDaMe e richiestiAMe vengono trasformati in Radio Button esclusivi;
//	private boolean richDaMe;
//	private boolean richDaMe_old;
//	private boolean richDaMeUserid;
//	private boolean richDaMeUserid_old;
//	private boolean richAMe;
//	private boolean richAMe_old;
	private String tipoRichiesta;
	// Fine Modifica almaviva2 BUG 3900 26.10.2010

	public List getListaBiliotecari() {
		return listaBiliotecari;
	}

	public void setListaBiliotecari(List listaBiliotecari) {
		this.listaBiliotecari = listaBiliotecari;
	}


	public List addListaBiliotecari(ComboCodDescVO elemBiblio) {
		if (listaBiliotecari == null) {
			listaBiliotecari = new ArrayList();
		}
		listaBiliotecari.add(elemBiblio);
		return listaBiliotecari;
	}


	public List getListaStatiProposta() {
		return listaStatiProposta;
	}

	public void setListaStatiProposta(List listaStatiProposta) {
		this.listaStatiProposta = listaStatiProposta;
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

//	public boolean isRichAMe() {
//		return richAMe;
//	}
//
//	public void setRichAMe(boolean richAMe) {
//		this.richAMe = richAMe;
//	}
//
//	public boolean isRichDaMe() {
//		return richDaMe;
//	}
//
//	public void setRichDaMe(boolean richDaMe) {
//		this.richDaMe = richDaMe;
//	}
//
//	public boolean isRichDaMeUserid() {
//		return richDaMeUserid;
//	}
//
//	public void setRichDaMeUserid(boolean richDaMeUserid) {
//		this.richDaMeUserid = richDaMeUserid;
//	}

	public String getTastoCreaPresente() {
		return tastoCreaPresente;
	}

	public void setTastoCreaPresente(String tastoCreaPresente) {
		this.tastoCreaPresente = tastoCreaPresente;
	}

//	public boolean isRichAMe_old() {
//		return richAMe_old;
//	}
//
//	public void setRichAMe_old(boolean richAMe_old) {
//		this.richAMe_old = richAMe_old;
//	}
//
//	public boolean isRichDaMe_old() {
//		return richDaMe_old;
//	}
//
//	public void setRichDaMe_old(boolean richDaMe_old) {
//		this.richDaMe_old = richDaMe_old;
//	}
//
//	public boolean isRichDaMeUserid_old() {
//		return richDaMeUserid_old;
//	}
//
//	public void setRichDaMeUserid_old(boolean richDaMeUserid_old) {
//		this.richDaMeUserid_old = richDaMeUserid_old;
//	}
//
//	public void save() {
//		this.richAMe_old = this.richAMe;
//		this.richDaMe_old = this.richDaMe;
//		this.richDaMeUserid_old = this.richDaMeUserid;
//	}
//
//	public void restore()
//	{
//		this.richAMe = this.richAMe_old;
//		this.richDaMe = this.richDaMe_old;
//		this.richDaMeUserid = this.richDaMeUserid_old;
//	}

	public String getTipoRichiesta() {
		return tipoRichiesta;
	}

	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}

}
