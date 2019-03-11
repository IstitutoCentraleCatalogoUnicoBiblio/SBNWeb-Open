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
package it.iccu.sbn.web.actionforms.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.ArrayList;
import java.util.List;

public class AnaliticaSoggettoForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -1016759278864600114L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private DettaglioDescrittoreVO dettDesGenVO = new DettaglioDescrittoreVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private RicercaSoggettoListaVO outputLista;
	private List titoliBiblio;
	private boolean abilita = true;

	private String sogInserimento;
	private String sogModifica;
	private String dataInserimento;
	private String dataVariazione;
	private String codice;
	private String descrizione;
	private String cid;
	private FolderType folder;
	private String cidTrascinaDa;
	private String testoTrascinaDa;
	private String livContr;
	private String livelloContr;
	private String codCategoriaSoggetto;
	private String tipoLegame;
	private String legatoTit;
	private String Testo;
	private String nodoSelezionato;
	private String checkSelezionato;
	private String did;
	private TreeElementViewSoggetti xidConferma;
	private String termine;
	private List listaSintetica;
	private List elementiReticolo;
	private boolean enableIndice = false;
	private boolean enableOk = false;
	private boolean enableNumPolo = false;
	private boolean enableNumBiblio = false;
	private boolean enableNumIndice = false;
	private boolean enableCercaIndice = true;
	private boolean enableEsamina = true;
	private boolean enableLegame = true;
	private boolean enableCrea = true;
	private boolean enableGestione = true;
	private boolean enableInserisci = true;
	private boolean enableElimina = true;
	private boolean enableScegli = false;
	private boolean enableModifica = false;
	private boolean enableConferma = false;
	private boolean enableAnaSog = false;
	private boolean enableManuale = false;
	private boolean enableTit = false;
	private boolean enableStampa = true;
	private boolean enableSogColl = true;
	private boolean treeDaLista = false;

	private int numTitoliPolo;
	private int numTitoliBiblio;
	private int numTitoliIndice;
	private List ricerca;
	private boolean sessione = false;
	private String action;
	private String descrizioneCategoriaSoggetto;
	private String categoriaSoggetto;
	private RicercaSoggettoListaVO output;
	private RicercaSoggettoListaVO outputlistaprima;
	private String cidRoot;
	private String paramId;
	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private boolean visualCheckCattura;
	private SBNMarc sbnMarcRispostaSave;
	private String[] listaCidSelez = null;
	private String listaCidSelezPresent = "NO";
	private int posizioneCorrente;
	private boolean enableInvioIndice;

	// labelGestioneSemantica
	private List<ComboCodDescVO> comboGestione = new ArrayList<ComboCodDescVO>();
	private List<ComboCodDescVO> comboGestioneNonFiltrata = new ArrayList<ComboCodDescVO>();
	private String idFunzione;
	private List<ComboCodDescVO> comboGestioneEsamina = new ArrayList<ComboCodDescVO>();
	private List<ComboCodDescVO> comboGestioneEsaminaNonFiltrata = new ArrayList<ComboCodDescVO>();
	private String idFunzioneEsamina;

	private List<ComboCodDescVO> listaStatoControllo;
	private List<ComboCodDescVO> listaTipoSoggetto;
	private List<ComboCodDescVO> listaSoggettari;
	private List<TB_CODICI> listaEdizioni;

	public List<ComboCodDescVO> getComboGestioneNonFiltrata() {
		return comboGestioneNonFiltrata;
	}

	public void setComboGestioneNonFiltrata(
			List<ComboCodDescVO> comboGestioneNonFiltrata) {
		this.comboGestioneNonFiltrata = comboGestioneNonFiltrata;
	}

	public List<ComboCodDescVO> getComboGestioneEsaminaNonFiltrata() {
		return comboGestioneEsaminaNonFiltrata;
	}

	public void setComboGestioneEsaminaNonFiltrata(
			List<ComboCodDescVO> comboGestioneEsaminaNonFiltrata) {
		this.comboGestioneEsaminaNonFiltrata = comboGestioneEsaminaNonFiltrata;
	}

	public String getIdFunzione() {
		return idFunzione;
	}

	public void setIdFunzione(String idFunzione) {
		this.idFunzione = idFunzione;
	}

	public List<ComboCodDescVO> getComboGestione() {
		return comboGestione;
	}

	public void setComboGestione(List<ComboCodDescVO> comboGestione) {
		this.comboGestione = comboGestione;
	}

	public boolean isEnableInvioIndice() {
		return enableInvioIndice;
	}

	public void setEnableInvioIndice(boolean enableInvioIndice) {
		this.enableInvioIndice = enableInvioIndice;
	}

	public String[] getListaCidSelez() {
		return listaCidSelez;
	}

	public void setListaCidSelez(String[] listaCidSelez) {
		this.listaCidSelez = listaCidSelez;
		if (listaCidSelez != null) {
			this.listaCidSelezPresent = "SI";
		} else
			this.listaCidSelezPresent = "NO";
	}

	public String getListaCidSelezPresent() {
		return listaCidSelezPresent;
	}

	public void setListaCidSelezPresent(String listaCidSelezPresent) {
		this.listaCidSelezPresent = listaCidSelezPresent;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizioneTipoSoggetto() {
		// for (int index = 0; index < this.listaTipoSoggetto.size(); index++) {
		// SoggettarioVO sog = (SoggettarioVO)
		// this.listaTipoSoggetto.get(index);
		// if (sog.getCodice().equals(this.codice))
		// return sog.getDescrizione();
		// }
		return "Non trovato";
	}

	public boolean isEnableCercaIndice() {
		return enableCercaIndice;
	}

	public void setEnableCercaIndice(boolean enableCercaIndice) {
		this.enableCercaIndice = enableCercaIndice;
	}

	public boolean isEnableEsamina() {
		return enableEsamina;
	}

	public void setEnableEsamina(boolean enableEsamina) {
		this.enableEsamina = enableEsamina;
	}

	public boolean isEnableGestione() {
		return enableGestione;
	}

	public void setEnableGestione(boolean enableGestione) {
		this.enableGestione = enableGestione;
	}

	public boolean isEnableIndice() {
		return enableIndice;
	}

	public void setEnableIndice(boolean enableIndice) {
		this.enableIndice = enableIndice;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	// public int getNumTitoliBiblio() {
	// if (this.numTitoliBiblio == 0) {
	// return listaSintetica.size();
	// } else {
	// return numTitoliBiblio;
	// }
	// }

	public boolean isEnableNumPolo() {
		return enableNumPolo;
	}

	public void setEnableNumPolo(boolean enableNumPolo) {
		this.enableNumPolo = enableNumPolo;
	}

	public boolean isEnableNumBiblio() {
		return enableNumBiblio;
	}

	public void setEnableNumBiblio(boolean enableNumBiblio) {
		this.enableNumBiblio = enableNumBiblio;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public int getNumTitoliIndice() {
		return numTitoliIndice;
	}

	public void setNumTitoliIndice(int numTitoliIndice) {
		this.numTitoliIndice = numTitoliIndice;
	}

	public boolean isEnableNumIndice() {
		return enableNumIndice;
	}

	public void setEnableNumIndice(boolean enableNumIndice) {
		this.enableNumIndice = enableNumIndice;
	}

	public boolean isEnableElimina() {
		return enableElimina;
	}

	public void setEnableElimina(boolean enableElimina) {
		this.enableElimina = enableElimina;
	}

	public boolean isEnableInserisci() {
		return enableInserisci;
	}

	public void setEnableInserisci(boolean enableInserisci) {
		this.enableInserisci = enableInserisci;
	}

	public String getSogInserimento() {
		return sogInserimento;
	}

	public void setSogInserimento(String sogInserimento) {
		this.sogInserimento = sogInserimento;
	}

	public String getSogModifica() {
		return sogModifica;
	}

	public void setSogModifica(String sogModifica) {
		this.sogModifica = sogModifica;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLegatoTit() {
		return legatoTit;
	}

	public void setLegatoTit(String legatoTit) {
		this.legatoTit = legatoTit;
	}

	public String getLivContr() {
		return livContr;
	}

	public void setLivContr(String livContr) {
		this.livContr = livContr;
	}

	public String getTesto() {
		return Testo;
	}

	public void setTesto(String testo) {
		Testo = testo;
	}

	public boolean isEnableCrea() {
		return enableCrea;
	}

	public void setEnableCrea(boolean enableCrea) {
		this.enableCrea = enableCrea;
	}

	public RicercaSoggettoListaVO getOutputLista() {
		return outputLista;
	}

	public void setOutputLista(RicercaSoggettoListaVO outputLista) {
		this.outputLista = outputLista;
	}

	public boolean isEnableScegli() {
		return enableScegli;
	}

	public void setEnableScegli(boolean enableScegli) {
		this.enableScegli = enableScegli;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isEnableModifica() {
		return enableModifica;
	}

	public void setEnableModifica(boolean enableModifica) {
		this.enableModifica = enableModifica;
	}

	public String getDescrizioneCategoriaSoggetto() {
		return descrizioneCategoriaSoggetto;
	}

	public void setDescrizioneCategoriaSoggetto(
			String descrizioneCategoriaSoggetto) {
		this.descrizioneCategoriaSoggetto = descrizioneCategoriaSoggetto;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getNodoSelezionato() {
		return nodoSelezionato;
	}

	public void setNodoSelezionato(String nodoSelezionato) {
		this.nodoSelezionato = nodoSelezionato;
	}

	public String getTermine() {
		return termine;
	}

	public void setTermine(String termine) {
		this.termine = termine;
	}

	public List getElementiReticolo() {
		return elementiReticolo;
	}

	public void setElementiReticolo(List elementiReticolo) {
		this.elementiReticolo = elementiReticolo;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public TreeElementViewSoggetti getXidConferma() {
		return xidConferma;
	}

	public void setXidConferma(TreeElementViewSoggetti xidConferma) {
		this.xidConferma = xidConferma;
	}

	public String getCidTrascinaDa() {
		return cidTrascinaDa;
	}

	public void setCidTrascinaDa(String cidTrascinaDa) {
		this.cidTrascinaDa = cidTrascinaDa;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public String getTestoTrascinaDa() {
		return testoTrascinaDa;
	}

	public void setTestoTrascinaDa(String testoTrascinaDa) {
		this.testoTrascinaDa = testoTrascinaDa;
	}

	public String getCidRoot() {
		return cidRoot;
	}

	public void setCidRoot(String cidRoot) {
		this.cidRoot = cidRoot;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public SBNMarc getSbnMarcRispostaSave() {
		return sbnMarcRispostaSave;
	}

	public void setSbnMarcRispostaSave(SBNMarc sbnMarcRispostaSave) {
		this.sbnMarcRispostaSave = sbnMarcRispostaSave;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public String getCheckSelezionato() {
		return checkSelezionato;
	}

	public void setCheckSelezionato(String checkSelezionato) {
		this.checkSelezionato = checkSelezionato;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCategoriaSoggetto() {
		return categoriaSoggetto;
	}

	public void setCategoriaSoggetto(String categoriaSoggetto) {
		this.categoriaSoggetto = categoriaSoggetto;
	}

	public boolean isEnableAnaSog() {
		return enableAnaSog;
	}

	public void setEnableAnaSog(boolean enableAnaSog) {
		this.enableAnaSog = enableAnaSog;
	}

	public String getLivelloContr() {
		return livelloContr;
	}

	public void setLivelloContr(String livelloContr) {
		this.livelloContr = livelloContr;
	}

	public String getCodCategoriaSoggetto() {
		return codCategoriaSoggetto;
	}

	public void setCodCategoriaSoggetto(String codCategoriaSoggetto) {
		this.codCategoriaSoggetto = codCategoriaSoggetto;
	}

	public RicercaSoggettoListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaSoggettoListaVO output) {
		this.output = output;
	}

	public DettaglioDescrittoreVO getDettDesGenVO() {
		return dettDesGenVO;
	}

	public void setDettDesGenVO(DettaglioDescrittoreVO dettDesGenVO) {
		this.dettDesGenVO = dettDesGenVO;
	}

	public boolean isEnableManuale() {
		return enableManuale;
	}

	public void setEnableManuale(boolean enableManuale) {
		this.enableManuale = enableManuale;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public boolean isVisualCheckCattura() {
		return visualCheckCattura;
	}

	public void setVisualCheckCattura(boolean visualCheckCattura) {
		this.visualCheckCattura = visualCheckCattura;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder2) {
		this.folder = folder2;
	}

	public boolean isEnableTit() {
		return enableTit;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public boolean isEnableSogColl() {
		return enableSogColl;
	}

	public void setEnableSogColl(boolean enableSogColl) {
		this.enableSogColl = enableSogColl;
	}

	public boolean isEnableStampa() {
		return enableStampa;
	}

	public void setEnableStampa(boolean enableStampa) {
		this.enableStampa = enableStampa;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public boolean isEnableLegame() {
		return enableLegame;
	}

	public void setEnableLegame(boolean enableLegame) {
		this.enableLegame = enableLegame;
	}

	public void setPosizioneCorrente(int i) {
		this.posizioneCorrente = i;

	}

	public int getPosizioneCorrente() {
		return posizioneCorrente;
	}

	public RicercaSoggettoListaVO getOutputlistaprima() {
		return outputlistaprima;
	}

	public void setOutputlistaprima(RicercaSoggettoListaVO outputlistaprima) {
		this.outputlistaprima = outputlistaprima;
	}

	public List<ComboCodDescVO> getComboGestioneEsamina() {
		return comboGestioneEsamina;
	}

	public void setComboGestioneEsamina(
			List<ComboCodDescVO> comboGestioneEsamina) {
		this.comboGestioneEsamina = comboGestioneEsamina;
	}

	public String getIdFunzioneEsamina() {
		return idFunzioneEsamina;
	}

	public void setIdFunzioneEsamina(String idFunzioneEsamina) {
		this.idFunzioneEsamina = idFunzioneEsamina;
	}

	public List<ComboCodDescVO> getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List<ComboCodDescVO> listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public List<ComboCodDescVO> getListaTipoSoggetto() {
		return listaTipoSoggetto;
	}

	public void setListaTipoSoggetto(List<ComboCodDescVO> listaTipoSoggetto) {
		this.listaTipoSoggetto = listaTipoSoggetto;
	}

	public List<ComboCodDescVO> getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List<ComboCodDescVO> listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public DatiCondivisioneSoggettoVO getDatiCondivisione() {
		if (treeElementViewSoggetti != null) {
			DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) treeElementViewSoggetti.getDettaglio();
			if (dettaglio != null)
				return ValidazioneDati.first(dettaglio.getDatiCondivisione());
		}
		return null;
	}

}
