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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class GestioneLegameTitoloTitoloForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 8463641814002169990L;
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
	AreaDatiVariazioneTitoloVO areaDatiVarTitoloVO = new AreaDatiVariazioneTitoloVO();

	private List listaTipoLegame;
	private String creaLegameInferiore;

	private String presenzaSottoTipoD;
	private String presenzaNumSequenza;
	private List listaSottonatureD;


	// Liste per la creazione del titolo in legame 51
	private List listaNatura;
	private List listaTipoMat;
	private List listaLivAut;
	private List listaTipoRec;
	private List listaLingua1;
	private List listaLingua2;
	private List listaLingua3;
	private List listaPaese;
	private List listaGenere1;
	private List listaGenere2;
	private List listaGenere3;
	private List listaGenere4;
	private List listaTipoData;
	private List listaTipiNumStandard;

	private String descNatura;
	private String descTipoMat;

	private String selezRadioImpronta;


    public TabellaNumSTDImpronteVO getItemImp(int index) {

        // automatically grow List size
        while (index >= this.areaDatiVarTitoloVO.getDetTitoloPFissaVO().getListaImpronte().size()) {
        	this.areaDatiVarTitoloVO.getDetTitoloPFissaVO().getListaImpronte().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.areaDatiVarTitoloVO.getDetTitoloPFissaVO().getListaImpronte().get(index);
    }


	public String getSelezRadioImpronta() {
		return selezRadioImpronta;
	}

	public void setSelezRadioImpronta(String selezRadioImpronta) {
		this.selezRadioImpronta = selezRadioImpronta;
	}

	public AreaDatiVariazioneTitoloVO getAreaDatiVarTitoloVO() {
		return areaDatiVarTitoloVO;
	}

	public void setAreaDatiVarTitoloVO(
			AreaDatiVariazioneTitoloVO areaDatiVarTitoloVO) {
		this.areaDatiVarTitoloVO = areaDatiVarTitoloVO;
	}

	public String getCreaLegameInferiore() {
		return creaLegameInferiore;
	}

	public void setCreaLegameInferiore(String creaLegameInferiore) {
		this.creaLegameInferiore = creaLegameInferiore;
	}

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public List getListaTipoLegame() {
		return listaTipoLegame;
	}

	public void setListaTipoLegame(List listaTipoLegame) {
		this.listaTipoLegame = listaTipoLegame;
	}

//	public List addListaTipoLegame(ComboCodDescVO codDesc) {
//		if (listaTipoLegame == null) {
//			listaTipoLegame = new ArrayList();
//		}
//		listaTipoLegame.add(codDesc);
//		return listaTipoLegame;
//	}

	public List getListaGenere1() {
		return listaGenere1;
	}

	public void setListaGenere1(List listaGenere1) {
		this.listaGenere1 = listaGenere1;
	}

	public List getListaGenere2() {
		return listaGenere2;
	}

	public void setListaGenere2(List listaGenere2) {
		this.listaGenere2 = listaGenere2;
	}

	public List getListaGenere3() {
		return listaGenere3;
	}

	public void setListaGenere3(List listaGenere3) {
		this.listaGenere3 = listaGenere3;
	}

	public List getListaGenere4() {
		return listaGenere4;
	}

	public void setListaGenere4(List listaGenere4) {
		this.listaGenere4 = listaGenere4;
	}

	public List getListaLingua1() {
		return listaLingua1;
	}

	public void setListaLingua1(List listaLingua1) {
		this.listaLingua1 = listaLingua1;
	}

	public List getListaLingua2() {
		return listaLingua2;
	}

	public void setListaLingua2(List listaLingua2) {
		this.listaLingua2 = listaLingua2;
	}

	public List getListaLingua3() {
		return listaLingua3;
	}

	public void setListaLingua3(List listaLingua3) {
		this.listaLingua3 = listaLingua3;
	}

	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public List getListaNatura() {
		return listaNatura;
	}

	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public List getListaTipiNumStandard() {
		return listaTipiNumStandard;
	}

	public void setListaTipiNumStandard(List listaTipiNumStandard) {
		this.listaTipiNumStandard = listaTipiNumStandard;
	}

	public List getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}

	public List getListaTipoMat() {
		return listaTipoMat;
	}

	public void setListaTipoMat(List listaTipoMat) {
		this.listaTipoMat = listaTipoMat;
	}

	public List getListaTipoRec() {
		return listaTipoRec;
	}

	public void setListaTipoRec(List listaTipoRec) {
		this.listaTipoRec = listaTipoRec;
	}

	public String getDescNatura() {
		return descNatura;
	}

	public void setDescNatura(String descNatura) {
		this.descNatura = descNatura;
	}

	public String getDescTipoMat() {
		return descTipoMat;
	}

	public void setDescTipoMat(String descTipoMat) {
		this.descTipoMat = descTipoMat;
	}

	public List getListaSottonatureD() {
		return listaSottonatureD;
	}

	public void setListaSottonatureD(List listaSottonatureD) {
		this.listaSottonatureD = listaSottonatureD;
	}


	public String getPresenzaSottoTipoD() {
		return presenzaSottoTipoD;
	}


	public void setPresenzaSottoTipoD(String presenzaSottoTipoD) {
		this.presenzaSottoTipoD = presenzaSottoTipoD;
	}


	public String getPresenzaNumSequenza() {
		return presenzaNumSequenza;
	}


	public void setPresenzaNumSequenza(String presenzaNumSequenza) {
		this.presenzaNumSequenza = presenzaNumSequenza;
	}
}
