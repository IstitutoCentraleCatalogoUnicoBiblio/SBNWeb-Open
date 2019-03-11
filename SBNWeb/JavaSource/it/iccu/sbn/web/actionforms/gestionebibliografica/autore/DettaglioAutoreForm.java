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

package it.iccu.sbn.web.actionforms.gestionebibliografica.autore;

import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class DettaglioAutoreForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -2760741444106413253L;

	private String bidPerRientroAnalitica;

    private DettaglioAutoreGeneraleVO dettAutoreVO = new DettaglioAutoreGeneraleVO();
    private DettaglioAutoreGeneraleVO dettAutoreVOOLD = new DettaglioAutoreGeneraleVO();

	private String selezRadioRepertori;

	private String tipoProspettazione;
	private boolean flagCondiviso;
	private String aggiornaFlagCondiviso;

	// Elenco oggetti per prospettazione descrizione/arrayList descrizioni/combo
	// per il riempimento delle liste di decodifica
	private List listaPaese;
	private String descPaese;
    private List listaLivAut;
	private String descLivAut;

	private List listaLingua1;
	private String descLingua1;
	private List listaTipoNome;
	private String descTipoNome;
	private List listaFormaNome;
	private String descFormaNome;

	private List listaRelatorCode;
	private String descRelatorCode;

	private List listaSpecStrumVoci;
	private String descSpecStrumVoci;

	private String appoTipoLegameCastor;
	private String descTipoLegameCastor;


	// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
	// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
	// In creazione il default è REICAT.
	// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
	// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
	private List listaNormaCatalografiche;


	private List<TabellaNumSTDImpronteVO> listaRepertoriModificato;

	// Aree della sintetica utilizzate per la prospettazione degli oggetti in creazione legame titolo
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();


	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public DettaglioAutoreForm() {
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




	public DettaglioAutoreGeneraleVO getDettAutoreVO() {
		return dettAutoreVO;
	}

	public void setDettAutoreVO(DettaglioAutoreGeneraleVO dettAutoreVO) {
		this.dettAutoreVO = dettAutoreVO;
	}

	public String getDescLingua1() {
		return descLingua1;
	}

	public void setDescLingua1(String descLingua1) {
		this.descLingua1 = descLingua1;
	}

	public String getDescLivAut() {
		return descLivAut;
	}

	public void setDescLivAut(String descLivAut) {
		this.descLivAut = descLivAut;
	}

	public String getDescPaese() {
		return descPaese;
	}

	public void setDescPaese(String descPaese) {
		this.descPaese = descPaese;
	}

	public List getListaLingua1() {
		return listaLingua1;
	}

	public void setListaLingua1(List listaLingua1) {
		this.listaLingua1 = listaLingua1;
	}

	public List getListaNormaCatalografiche() {
		return listaNormaCatalografiche;
	}

	public void setListaNormaCatalografiche(List listaNormaCatalografiche) {
		this.listaNormaCatalografiche = listaNormaCatalografiche;
	}


	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public DettaglioAutoreGeneraleVO getDettAutoreVOOLD() {
		return dettAutoreVOOLD;
	}

	public void setDettAutoreVOOLD(DettaglioAutoreGeneraleVO dettAutoreVOOLD) {
		this.dettAutoreVOOLD = dettAutoreVOOLD;
	}

	public String getSelezRadioRepertori() {
		return selezRadioRepertori;
	}

	public void setSelezRadioRepertori(String selezRadioRepertori) {
		this.selezRadioRepertori = selezRadioRepertori;
	}

	public String getDescTipoNome() {
		return descTipoNome;
	}

	public void setDescTipoNome(String descTipoNome) {
		this.descTipoNome = descTipoNome;
	}

	public List getListaTipoNome() {
		return listaTipoNome;
	}

	public void setListaTipoNome(List listaTipoNome) {
		this.listaTipoNome = listaTipoNome;
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

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public String getBidPerRientroAnalitica() {
		return bidPerRientroAnalitica;
	}

	public void setBidPerRientroAnalitica(String bidPerRientroAnalitica) {
		this.bidPerRientroAnalitica = bidPerRientroAnalitica;
	}

	public String getDescTipoLegameCastor() {
		return descTipoLegameCastor;
	}

	public void setDescTipoLegameCastor(String descTipoLegameCastor) {
		this.descTipoLegameCastor = descTipoLegameCastor;
	}

	public String getAppoTipoLegameCastor() {
		return appoTipoLegameCastor;
	}

	public void setAppoTipoLegameCastor(String appoTipoLegameCastor) {
		this.appoTipoLegameCastor = appoTipoLegameCastor;
	}

	public String getAggiornaFlagCondiviso() {
		return aggiornaFlagCondiviso;
	}

	public void setAggiornaFlagCondiviso(String aggiornaFlagCondiviso) {
		this.aggiornaFlagCondiviso = aggiornaFlagCondiviso;
	}

	public List getListaSpecStrumVoci() {
		return listaSpecStrumVoci;
	}

	public void setListaSpecStrumVoci(List listaSpecStrumVoci) {
		this.listaSpecStrumVoci = listaSpecStrumVoci;
	}

	public String getDescSpecStrumVoci() {
		return descSpecStrumVoci;
	}

	public void setDescSpecStrumVoci(String descSpecStrumVoci) {
		this.descSpecStrumVoci = descSpecStrumVoci;
	}

	}
