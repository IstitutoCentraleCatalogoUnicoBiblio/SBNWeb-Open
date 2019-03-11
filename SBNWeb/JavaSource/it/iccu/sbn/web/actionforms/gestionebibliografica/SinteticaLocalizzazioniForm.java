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

package it.iccu.sbn.web.actionforms.gestionebibliografica;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaLocalizzazioniView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;


public class SinteticaLocalizzazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 4700255781443770749L;

	private String myPath;

	private String codaPath;

	private int idPartenza;
    private int descPartenza;
    private int numBlocco;
    private int totBlocchi;
    private int numLoc;
    private String livRic;
	private String idOggColl;
	private String descOggColl;

	private String selezRadio;

    private List listaSintetica;

    private String codPoloRicercaSelez;
    private List listaCodPolo;
    private List listaSinteticaCompleta = new ArrayList();

    private List<String> bidLocalizz = new ArrayList<String>();

    private String centroSistema;

    private String codBiblio;
	private String descBiblio;
	private List listaBiblio;
	private String codTipoLoc;
	private List listaTipoLocalizzazione;
	private String codMutiloM;
	private List listaMutiloM;
	private String consistenza;

	private List tabBibTipoLocOld = new ArrayList();


	private AreePassaggioSifVO areePassSifVo = new AreePassaggioSifVO();
    private String utilizzoComeSif;
	private String tipoProspettazione;
	private String tipoRichiesta;

	AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla = new AreaDatiLocalizzazioniAuthorityMultiplaVO();


    public SinteticaLocalizzazioniView getItem(int index) {

        // automatically grow List size
        while (index >= this.getListaSintetica().size()) {
        	this.getListaSintetica().add(new SinteticaLocalizzazioniView());
        }
        return (SinteticaLocalizzazioniView)this.getListaSintetica().get(index);
    }

	public String getUtilizzoComeSif() {
		return utilizzoComeSif;
	}

	public void setUtilizzoComeSif(String utilizzoComeSif) {
		this.utilizzoComeSif = utilizzoComeSif;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public List addListaSintetica(SinteticaLocalizzazioniView sintLocWiew) {
		listaSintetica.add(sintLocWiew);
		return listaSintetica;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumLoc() {
		return numLoc;
	}

	public void setNumLoc(int numLoc) {
		this.numLoc = numLoc;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getDescPartenza() {
		return descPartenza;
	}

	public void setDescPartenza(int descPartenza) {
		this.descPartenza = descPartenza;
	}

	public int getIdPartenza() {
		return idPartenza;
	}

	public void setIdPartenza(int idPartenza) {
		this.idPartenza = idPartenza;
	}


	public String getLivRic() {
		return livRic;
	}

	public void setLivRic(String livRic) {
		this.livRic = livRic;
	}


	public String getMyPath() {
		return myPath;
	}

	public void setMyPath(String myPath) {
		this.myPath = myPath;
	}

	public AreePassaggioSifVO getAreePassSifVo() {
		return areePassSifVo;
	}

	public void setAreePassSifVo(AreePassaggioSifVO areePassSifVo) {
		this.areePassSifVo = areePassSifVo;
	}

	public String getDescOggColl() {
		return descOggColl;
	}

	public void setDescOggColl(String descOggColl) {
		this.descOggColl = descOggColl;
	}

	public String getIdOggColl() {
		return idOggColl;
	}

	public void setIdOggColl(String idOggColl) {
		this.idOggColl = idOggColl;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public String getDescBiblio() {
		return descBiblio;
	}

	public void setDescBiblio(String descBiblio) {
		this.descBiblio = descBiblio;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

	public List addListaBiblio(ComboCodDescVO elemBiblio) {
		listaBiblio.add(elemBiblio);
		return listaBiblio;
	}


	public List getListaTipoLocalizzazione() {
		return listaTipoLocalizzazione;
	}

	public void setListaTipoLocalizzazione(List listaTipoLocalizzazione) {
		this.listaTipoLocalizzazione = listaTipoLocalizzazione;
	}

	public String getCodBiblio() {
		return codBiblio;
	}

	public void setCodBiblio(String codBiblio) {
		this.codBiblio = codBiblio;
	}

	public String getCodTipoLoc() {
		return codTipoLoc;
	}

	public void setCodTipoLoc(String codTipoLoc) {
		this.codTipoLoc = codTipoLoc;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public String getCentroSistema() {
		return centroSistema;
	}

	public void setCentroSistema(String centroSistema) {
		this.centroSistema = centroSistema;
	}

	public String getCodMutiloM() {
		return codMutiloM;
	}

	public void setCodMutiloM(String codMutiloM) {
		this.codMutiloM = codMutiloM;
	}

	public List getListaMutiloM() {
		return listaMutiloM;
	}

	public void setListaMutiloM(List listaMutiloM) {
		this.listaMutiloM = listaMutiloM;
	}

	public String getCodaPath() {
		return codaPath;
	}

	public void setCodaPath(String codaPath) {
		this.codaPath = codaPath;
	}

	public List getTabBibTipoLocOld() {
		return tabBibTipoLocOld;
	}

	public void setTabBibTipoLocOld(List tabBibTipoLocOld) {
		this.tabBibTipoLocOld = tabBibTipoLocOld;
	}

	public List addTabBibTipoLocOld(String elemento) {
		tabBibTipoLocOld.add(elemento);
		return tabBibTipoLocOld;
	}

	public List getListaSinteticaCompleta() {
		return listaSinteticaCompleta;
	}

	public void setListaSinteticaCompleta(List listaSinteticaCompleta) {
		this.listaSinteticaCompleta = listaSinteticaCompleta;
	}

	public List addListaSinteticaCompleta(SinteticaLocalizzazioniView sintLocWiew) {
		listaSinteticaCompleta.add(sintLocWiew);
		return listaSinteticaCompleta;
	}

	public String getCodPoloRicercaSelez() {
		return codPoloRicercaSelez;
	}

	public void setCodPoloRicercaSelez(String codPoloRicercaSelez) {
		this.codPoloRicercaSelez = codPoloRicercaSelez;
	}

	public List getListaCodPolo() {
		return listaCodPolo;
	}

	public void setListaCodPolo(List listaCodPolo) {
		this.listaCodPolo = listaCodPolo;
	}

	public String getTipoRichiesta() {
		return tipoRichiesta;
	}

	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}

	public List<String> getBidLocalizz() {
		return bidLocalizz;
	}

	public void setBidLocalizz(List<String> bidLocalizz) {
		this.bidLocalizz = bidLocalizz;
	}

	public AreaDatiLocalizzazioniAuthorityMultiplaVO getAreaLocalizzaMultipla() {
		return areaLocalizzaMultipla;
	}

	public void setAreaLocalizzaMultipla(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaLocalizzaMultipla) {
		this.areaLocalizzaMultipla = areaLocalizzaMultipla;
	}


}
