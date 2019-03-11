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
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class AnaliticaDescrittoreForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -7615686729996208520L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private DettaglioDescrittoreVO dettDesGenVO = new DettaglioDescrittoreVO();
	private boolean abilita = true;
	private List listaSoggettari;
	private List listaStatoControllo;
	private String dataInserimento;
	private String dataVariazione;
	private String sogInserimento;
	private String sogModifica;
	private String codice;
	private String did;
	private String didPadre;
	private String didLegame;
	private TreeElementViewSoggetti xidConferma;
	private String tipoLegame;
	private String termine;
	private String termineLegame;
	private List listaSintetica;
	private boolean enableIndice = false;
	private boolean enableNumSogg = false;
	private boolean enableNumUtil = false;
	private boolean enableCercaIndice = true;
	private boolean enableEsamina = true;
	private boolean enableGestione = true;
	private boolean enableInserisci = true;
	private boolean enableConferma = false;
	private boolean enableStampa = true;
	private boolean enableElimina = true;
	private boolean enableScegli = false;
	private boolean enableModifica = false;
	private boolean enableAnaSog = false;
	private boolean enableManuale = false;
	private int numSoggetti;
	private String numUtilizzati;
	private String numSoggettiIndice;
	private String nodoSelezionato;
	private String checkSelezionato;
	private List ricerca;
	private boolean sessione = false;
	private String action;
	private List elementiReticolo;
	private String cidRoot;
	private String paramId;
	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private SBNMarc sbnMarcRispostaSave;
	private String livContr;
	private String livelloContr;
	private String descrizione;
	private String T005;
	private String[] listaDidSelez = null;
	private String listaDidSelezPresent = "NO";
	private int posizioneCorrente;

	private String idFunzione;
	private List<ComboCodDescVO> comboGestione;

	private List<TB_CODICI> listaEdizioni;
	private List<TB_CODICI> listaCategoriaTermine;

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

	public String[] getListaDidSelez() {
		return listaDidSelez;
	}

	public void setListaDidSelez(String[] listaDidSelez) {
		this.listaDidSelez = listaDidSelez;
		if (listaDidSelez != null) {
			this.listaDidSelezPresent = "SI";
		} else
			this.listaDidSelezPresent = "NO";
	}

	public String getListaDidSelezPresent() {
		return listaDidSelezPresent;
	}

	public void setListaDidSelezPresent(String listaDidSelezPresent) {
		this.listaDidSelezPresent = listaDidSelezPresent;
	}

	public void setPosizioneCorrente(int i) {
		this.posizioneCorrente = i;

	}

	public int getPosizioneCorrente() {
		return posizioneCorrente;
	}

	private RicercaSoggettoListaVO outputDescrittori;

	private boolean visualCheckCattura;

	public RicercaSoggettoListaVO getOutputDescrittori() {
		return outputDescrittori;
	}

	public void setOutputDescrittori(RicercaSoggettoListaVO outputDescrittori) {
		this.outputDescrittori = outputDescrittori;
	}

	public String getLivelloContr() {
		return livelloContr;
	}

	public void setLivelloContr(String livelloContr) {
		this.livelloContr = livelloContr;
	}

	public String getLivContr() {
		return livContr;
	}

	public void setLivContr(String livContr) {
		this.livContr = livContr;
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

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public boolean isEnableNumSogg() {
		return enableNumSogg;
	}

	public void setEnableNumSogg(boolean enableNumSogg) {
		this.enableNumSogg = enableNumSogg;
	}

	public boolean isEnableNumUtil() {
		return enableNumUtil;
	}

	public void setEnableNumUtil(boolean enableNumUtil) {
		this.enableNumUtil = enableNumUtil;
	}

	public String getNumUtilizzati() {
		return numUtilizzati;
	}

	public void setNumUtilizzati(String numUtilizzati) {
		this.numUtilizzati = numUtilizzati;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTermine() {
		return termine;
	}

	public void setTermine(String termine) {
		this.termine = termine;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public List getElementiReticolo() {
		return elementiReticolo;
	}

	public void setElementiReticolo(List elementiReticolo) {
		this.elementiReticolo = elementiReticolo;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getDidLegame() {
		return didLegame;
	}

	public void setDidLegame(String didLegame) {
		this.didLegame = didLegame;
	}

	public String getTermineLegame() {
		return termineLegame;
	}

	public void setTermineLegame(String termineLegame) {
		this.termineLegame = termineLegame;
	}

	public String getNodoSelezionato() {
		return nodoSelezionato;
	}

	public void setNodoSelezionato(String nodoSelezionato) {
		this.nodoSelezionato = nodoSelezionato;
	}

	public TreeElementViewSoggetti getXidConferma() {
		return xidConferma;
	}

	public void setXidConferma(TreeElementViewSoggetti xidConferma) {
		this.xidConferma = xidConferma;
	}

	public boolean isEnableInserisci() {
		return enableInserisci;
	}

	public void setEnableInserisci(boolean enableInserisci) {
		this.enableInserisci = enableInserisci;
	}

	public boolean isEnableElimina() {
		return enableElimina;
	}

	public void setEnableElimina(boolean enableElimina) {
		this.enableElimina = enableElimina;
	}

	public boolean isEnableModifica() {
		return enableModifica;
	}

	public void setEnableModifica(boolean enableModifica) {
		this.enableModifica = enableModifica;
	}

	public boolean isEnableScegli() {
		return enableScegli;
	}

	public void setEnableScegli(boolean enableScegli) {
		this.enableScegli = enableScegli;
	}

	public boolean isEnableAnaSog() {
		return enableAnaSog;
	}

	public void setEnableAnaSog(boolean enableAnaSog) {
		this.enableAnaSog = enableAnaSog;
	}

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
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

	public int getNumSoggetti() {
		return numSoggetti;
	}

	public void setNumSoggetti(int numSoggetti) {
		this.numSoggetti = numSoggetti;
	}

	public String getNumSoggettiIndice() {
		return numSoggettiIndice;
	}

	public void setNumSoggettiIndice(String numSoggettiIndice) {
		this.numSoggettiIndice = numSoggettiIndice;
	}

	public String getCheckSelezionato() {
		return checkSelezionato;
	}

	public void setCheckSelezionato(String checkSelezionato) {
		this.checkSelezionato = checkSelezionato;
	}

	public boolean isVisualCheckCattura() {
		return visualCheckCattura;
	}

	public void setVisualCheckCattura(boolean visualCheckCattura) {
		this.visualCheckCattura = visualCheckCattura;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public boolean isEnableStampa() {
		return enableStampa;
	}

	public void setEnableStampa(boolean enableStampa) {
		this.enableStampa = enableStampa;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public String getDidPadre() {
		return didPadre;
	}

	public void setDidPadre(String didPadre) {
		this.didPadre = didPadre;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public List<TB_CODICI> getListaCategoriaTermine() {
		return listaCategoriaTermine;
	}

	public void setListaCategoriaTermine(List<TB_CODICI> listaCategoriaTermine) {
		this.listaCategoriaTermine = listaCategoriaTermine;
	}

}
