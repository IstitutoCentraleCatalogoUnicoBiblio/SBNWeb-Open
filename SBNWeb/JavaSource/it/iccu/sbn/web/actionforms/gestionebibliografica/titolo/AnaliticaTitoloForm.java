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
package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnaliticaTitoloForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -3248668409060259647L;
	//	 Aree della sintetica utilizzate per interrogare in indice con l'area passata da interrogazione
	AreaDatiPassaggioInterrogazioneTitoloVO datiInterrTitolo = new AreaDatiPassaggioInterrogazioneTitoloVO();
	AreaDatiPassaggioInterrogazioneAutoreVO datiInterrAutore = new AreaDatiPassaggioInterrogazioneAutoreVO();
	AreaDatiPassaggioInterrogazioneMarcaVO datiInterrMarca = new AreaDatiPassaggioInterrogazioneMarcaVO();
	AreaDatiPassaggioInterrogazioneLuogoVO datiInterrLuogo = new AreaDatiPassaggioInterrogazioneLuogoVO();

	//almaviva5_20110107
	private String elencoBibliotecheSelezionate;
	private List<BibliotecaVO> filtroBib;

	// Inizio attributi speciali
	private boolean serializeAnaliticaTitolo = true;
	private boolean clone = false;
	// Fine attributi speciali

	TabellaEsaminaVO tabellaEsaminaVO = new TabellaEsaminaVO();

	private String myPath;
	private List listaEsaminaTit;
	private String esaminaTitSelez;

	private String visualVaiA;
	InterrogazioneVaiAForm interrogazioneVaiAForm = new InterrogazioneVaiAForm();

	private String livRicerca;

	private String bidRoot;

	// Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
	// operante che nel caso di centro Sistema non coincidono; inserita impostazione iniziale nel Costruttore
	private String codiceBiblioSbn;


	private String tipoAuthority;

	private String paramId;

    private TreeElementViewTitoli treeElementViewTitoli;

    private String radioItemSelez;

    private String[] checkItemSelez = null;

    private SBNMarc sbnMarcRispostaSave;

    String[] listaBidSelez = null;

    private String listaBidSelezPresent = "NO";
    private boolean visualCheckCattura;

    private String confermaCanc;

    private String gestioneInferiori;

    private String tipoOperazioneConferma;

    private String presenzaTastoVaiA;
    private String presenzaTastoCercaInIndice;
    private String presenzaTastoAnaliticaDiIndice;
    private String provenienzaChiamatainSIF;

    private boolean analiticaAttiva;

    private boolean rinviaInIndice = false;
    private String areaLogIncrementale = "";

	private Map tabellaTimeStampIndice = new HashMap();
	private Map tabellaTimeStampPolo = new HashMap();

	private String erroreRichAnalitica;

	// Lista per catalogazione in ciclo da File
	private String listaBidDaFilePresent = "NO";
	private List listaBidDaFile;


	public String getErroreRichAnalitica() {
		return erroreRichAnalitica;
	}

	public void setErroreRichAnalitica(String erroreRichAnalitica) {
		this.erroreRichAnalitica = erroreRichAnalitica;
	}

	public String[] getCheckItemSelez() {
		return checkItemSelez;
	}

	public void setCheckItemSelez(String[] checkItemSelez) {
		this.checkItemSelez = checkItemSelez;
	}

	public String getRadioItemSelez() {
		return radioItemSelez;
	}

	public void setRadioItemSelez(String radioItemSelez) {
		this.radioItemSelez = radioItemSelez;
	}

	public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public TreeElementViewTitoli getTreeElementViewTitoli() {
        return treeElementViewTitoli;
    }

    public void setTreeElementViewTitoli(TreeElementViewTitoli treeElementViewTitoli) {
        this.treeElementViewTitoli = treeElementViewTitoli;
    }

	public SBNMarc getSbnMarcRispostaSave() {
		return sbnMarcRispostaSave;
	}

	public void setSbnMarcRispostaSave(SBNMarc sbnMarcRispostaSave) {
		this.sbnMarcRispostaSave = sbnMarcRispostaSave;
	}

	public String getBidRoot() {
		return bidRoot;
	}

	public void setBidRoot(String bidRoot) {
		this.bidRoot = bidRoot;
	}

	public String getMyPath() {
		return myPath;
	}

	public void setMyPath(String myPath) {
		this.myPath = myPath;
	}

	public String getEsaminaTitSelez() {
		return esaminaTitSelez;
	}

	public void setEsaminaTitSelez(String esaminaTitSelez) {
		this.esaminaTitSelez = esaminaTitSelez;
	}

	public List getListaEsaminaTit() {
		return listaEsaminaTit;
	}

	public void setListaEsaminaTit(List listaEsaminaTit) {
		this.listaEsaminaTit = listaEsaminaTit;
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}

	public String getVisualVaiA() {
		return visualVaiA;
	}

	public void setVisualVaiA(String visualVaiA) {
		this.visualVaiA = visualVaiA;
	}

	public InterrogazioneVaiAForm getInterrogazioneVaiAForm() {
		return interrogazioneVaiAForm;
	}

	public void setInterrogazioneVaiAForm(
			InterrogazioneVaiAForm interrogazioneVaiAForm) {
		this.interrogazioneVaiAForm = interrogazioneVaiAForm;
	}

	public AreaDatiPassaggioInterrogazioneTitoloVO getDatiInterrTitolo() {
		return datiInterrTitolo;
	}

	public void setDatiInterrTitolo(
			AreaDatiPassaggioInterrogazioneTitoloVO datiInterrTitolo) {
		this.datiInterrTitolo = datiInterrTitolo;
	}

	public String[] getListaBidSelez() {
		return listaBidSelez;
	}

	public void setListaBidSelez(String[] listaBidSelez) {
		this.listaBidSelez = listaBidSelez;
		this.listaBidSelezPresent = "SI";
	}

	public boolean isVisualCheckCattura() {
		return visualCheckCattura;
	}

	public void setVisualCheckCattura(boolean visualCheckCattura) {
		this.visualCheckCattura = visualCheckCattura;
	}

	public String getListaBidSelezPresent() {
		return listaBidSelezPresent;
	}

	public void setListaBidSelezPresent(String listaBidSelezPresent) {
		this.listaBidSelezPresent = listaBidSelezPresent;
	}

	public String getTipoAuthority() {
		return tipoAuthority;
	}

	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}

	public String getConfermaCanc() {
		return confermaCanc;
	}

	public void setConfermaCanc(String confermaCanc) {
		this.confermaCanc = confermaCanc;
	}

	public AreaDatiPassaggioInterrogazioneAutoreVO getDatiInterrAutore() {
		return datiInterrAutore;
	}

	public void setDatiInterrAutore(
			AreaDatiPassaggioInterrogazioneAutoreVO datiInterrAutore) {
		this.datiInterrAutore = datiInterrAutore;
	}

	public AreaDatiPassaggioInterrogazioneMarcaVO getDatiInterrMarca() {
		return datiInterrMarca;
	}

	public void setDatiInterrMarca(
			AreaDatiPassaggioInterrogazioneMarcaVO datiInterrMarca) {
		this.datiInterrMarca = datiInterrMarca;
	}

	public String getGestioneInferiori() {
		return gestioneInferiori;
	}

	public void setGestioneInferiori(String gestioneInferiori) {
		this.gestioneInferiori = gestioneInferiori;
	}

	public String getTipoOperazioneConferma() {
		return tipoOperazioneConferma;
	}

	public void setTipoOperazioneConferma(String tipoOperazioneConferma) {
		this.tipoOperazioneConferma = tipoOperazioneConferma;
	}

	public AreaDatiPassaggioInterrogazioneLuogoVO getDatiInterrLuogo() {
		return datiInterrLuogo;
	}

	public void setDatiInterrLuogo(
			AreaDatiPassaggioInterrogazioneLuogoVO datiInterrLuogo) {
		this.datiInterrLuogo = datiInterrLuogo;
	}


	public Map getTabellaTimeStampIndice() {
		return tabellaTimeStampIndice;
	}

	public void setTabellaTimeStampIndice(Map tabellaTimeStampIndice) {
		this.tabellaTimeStampIndice = tabellaTimeStampIndice;
	}

	public Map getTabellaTimeStampPolo() {
		return tabellaTimeStampPolo;
	}

	public void setTabellaTimeStampPolo(Map tabellaTimeStampPolo) {
		this.tabellaTimeStampPolo = tabellaTimeStampPolo;
	}

	public TabellaEsaminaVO getTabellaEsaminaVO() {
		return tabellaEsaminaVO;
	}

	public void setTabellaEsaminaVO(TabellaEsaminaVO tabellaEsaminaVO) {
		this.tabellaEsaminaVO = tabellaEsaminaVO;
	}

	public String getPresenzaTastoVaiA() {
		return presenzaTastoVaiA;
	}

	public void setPresenzaTastoVaiA(String presenzaTastoVaiA) {
		this.presenzaTastoVaiA = presenzaTastoVaiA;
	}

	public String getPresenzaTastoCercaInIndice() {
		return presenzaTastoCercaInIndice;
	}

	public void setPresenzaTastoCercaInIndice(String presenzaTastoCercaInIndice) {
		this.presenzaTastoCercaInIndice = presenzaTastoCercaInIndice;
	}

	public String getProvenienzaChiamatainSIF() {
		return provenienzaChiamatainSIF;
	}

	public void setProvenienzaChiamatainSIF(String provenienzaChiamatainSIF) {
		this.provenienzaChiamatainSIF = provenienzaChiamatainSIF;
	}

	public boolean isAnaliticaAttiva() {
		return analiticaAttiva;
	}

	public void setAnaliticaAttiva(boolean analiticaAttiva) {
		this.analiticaAttiva = analiticaAttiva;
	}



	public AnaliticaTitoloForm cloneForm(boolean serializeAnaliticaTitolo) throws Exception {

		try {
			this.serializeAnaliticaTitolo = serializeAnaliticaTitolo;
			AnaliticaTitoloForm clone = ClonePool.deepCopy(this);
			clone.serializeAnaliticaTitolo = true;
			clone.clone = true;
			return clone;
		} finally {
			this.serializeAnaliticaTitolo = true;
		}
	}

	public boolean isRinviaInIndice() {
		return rinviaInIndice;
	}

	public void setRinviaInIndice(boolean rinviaInIndice) {
		this.rinviaInIndice = rinviaInIndice;
	}

	public String getAreaLogIncrementale() {
		return areaLogIncrementale;
	}

	public void setAreaLogIncrementale(String areaLogIncrementale) {
		this.areaLogIncrementale = areaLogIncrementale;
	}

	public List getListaBidDaFile() {
		return listaBidDaFile;
	}

	public void setListaBidDaFile(List listaBidDaFile) {
		this.listaBidDaFile = listaBidDaFile;
	}

	public String getListaBidDaFilePresent() {
		return listaBidDaFilePresent;
	}

	public void setListaBidDaFilePresent(String listaBidDaFilePresent) {
		this.listaBidDaFilePresent = listaBidDaFilePresent;
	}

	public String getCodiceBiblioSbn() {
		return codiceBiblioSbn;
	}

	public void setCodiceBiblioSbn(String codiceBiblioSbn) {
		this.codiceBiblioSbn = codiceBiblioSbn;
	}

	public String getPresenzaTastoAnaliticaDiIndice() {
		return presenzaTastoAnaliticaDiIndice;
	}

	public void setPresenzaTastoAnaliticaDiIndice(
			String presenzaTastoAnaliticaDiIndice) {
		this.presenzaTastoAnaliticaDiIndice = presenzaTastoAnaliticaDiIndice;
	}

	public String getElencoBibliotecheSelezionate() {
		return elencoBibliotecheSelezionate;
	}

	public void setElencoBibliotecheSelezionate(String elencoBibliotecheSelezionate) {
		this.elencoBibliotecheSelezionate = elencoBibliotecheSelezionate;
	}

	public List<BibliotecaVO> getFiltroBib() {
		return filtroBib;
	}

	public void setFiltroBib(List<BibliotecaVO> filtroBib) {
		this.filtroBib = filtroBib;
	}


}
