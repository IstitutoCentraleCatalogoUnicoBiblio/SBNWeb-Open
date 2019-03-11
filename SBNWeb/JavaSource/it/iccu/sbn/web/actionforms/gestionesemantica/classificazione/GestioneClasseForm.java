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
package it.iccu.sbn.web.actionforms.gestionesemantica.classificazione;



import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class GestioneClasseForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -583320061864950634L;
	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();
	private DettaglioClasseVO dettClaGen = new DettaglioClasseVO();
	private AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private boolean abilita = true;
	private boolean enableEsamina = false;
	private boolean enableTrascina = false;
	private boolean enableElimina = false;
	private boolean enableFondi = false;
	private boolean modificato = false;
	private String xidConferma;
	private boolean enableConferma = false;
	private String statoControllo;
	private String sistemaClassificazione ;
	private String edizioneDewey;
	private List listaSistemiClassificazione;
	private List<TB_CODICI> listaEdizioni;
	private List listaStatoControllo;
	private String identificativoClasse;
	private String descrizione;
	private String ulterioreTermine;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private int numTitoliPolo;
	private int numTitoliBiblio;
	private int numTitoliIndice;
	private String notazioneFusione;
	private List titoliBiblio;
	private String action;
	private List ricerca;

	private boolean enableNumPolo = false;
	private boolean enableNumBiblio = false;
	private boolean enableNumIndice = false;
	private boolean enableGestione = false;
	private boolean sessione = false;

	private List<ComboCodDescVO> comboGestioneEsamina = new ArrayList<ComboCodDescVO>();
	private String idFunzioneEsamina;

	public List<ComboCodDescVO> getComboGestioneEsamina() {
		return comboGestioneEsamina;
	}

	public void setComboGestioneEsamina(List<ComboCodDescVO> comboGestioneEsamina) {
		this.comboGestioneEsamina = comboGestioneEsamina;
	}

	public String getIdFunzioneEsamina() {
		return idFunzioneEsamina;
	}

	public void setIdFunzioneEsamina(String idFunzioneEsamina) {
		this.idFunzioneEsamina = idFunzioneEsamina;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(String dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		if (this.descrizione != null) {
			if (!this.descrizione.equals(descrizione))
				this.setModificato(true);
		}
		this.descrizione = descrizione;
	}


	public String getEdizioneDewey() {
		return edizioneDewey;
	}

	public void setEdizioneDewey(String edizioneDewey) {
		this.edizioneDewey = edizioneDewey;
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

	public boolean isEnableNumBiblio() {
		return enableNumBiblio;
	}

	public void setEnableNumBiblio(boolean enableNumBiblio) {
		this.enableNumBiblio = enableNumBiblio;
	}

	public boolean isEnableNumIndice() {
		return enableNumIndice;
	}

	public void setEnableNumIndice(boolean enableNumIndice) {
		this.enableNumIndice = enableNumIndice;
	}

	public boolean isEnableNumPolo() {
		return enableNumPolo;
	}

	public void setEnableNumPolo(boolean enableNumPolo) {
		this.enableNumPolo = enableNumPolo;
	}

	public String getIdentificativoClasse() {
		return identificativoClasse;
	}

	public void setIdentificativoClasse(String identificativoClasse) {
		this.identificativoClasse = identificativoClasse;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> list) {
		this.listaEdizioni = list;
	}

	public List getListaSistemiClassificazione() {
		return listaSistemiClassificazione;
	}

	public void setListaSistemiClassificazione(List listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
	}

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTitoliIndice() {
		return numTitoliIndice;
	}

	public void setNumTitoliIndice(int numTitoliIndice) {
		this.numTitoliIndice = numTitoliIndice;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getSistemaClassificazione() {
		return sistemaClassificazione;
	}

	public void setSistemaClassificazione(String sistemaClassificazione) {
		this.sistemaClassificazione = sistemaClassificazione;
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



	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		if (this.ulterioreTermine != null) {
			if (!this.ulterioreTermine.equals(ulterioreTermine))
				this.setModificato(true);
		}
		this.ulterioreTermine = ulterioreTermine;
	}

	public DettaglioClasseVO getDettClaGen() {
		return dettClaGen;
	}

	public void setDettClaGen(DettaglioClasseVO dettClaGen) {
		this.dettClaGen = dettClaGen;
	}

	public boolean isEnableTrascina() {
		return enableTrascina;
	}

	public void setEnableTrascina(boolean enableTrascina) {
		this.enableTrascina = enableTrascina;
	}

	public boolean isModificato() {
		return modificato;
	}

	public void setModificato(boolean modificato) {
		this.modificato = modificato;
	}

	public String getStatoControllo() {
		return statoControllo;
	}

	public void setStatoControllo(String statoControllo) {
		if (this.statoControllo != null) {
			if (!this.statoControllo.equals(statoControllo))
				this.setModificato(true);
		}
		this.statoControllo = statoControllo;
	}

	public boolean isEnableElimina() {
		return enableElimina;
	}

	public void setEnableElimina(boolean enableElimina) {
		this.enableElimina = enableElimina;
	}

	public String getXidConferma() {
		return xidConferma;
	}

	public void setXidConferma(String xidConferma) {
		this.xidConferma = xidConferma;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public String getNotazioneFusione() {
		return notazioneFusione;
	}

	public void setNotazioneFusione(String notazioneFusione) {
		this.notazioneFusione = notazioneFusione;
	}

	public boolean isEnableFondi() {
		return enableFondi;
	}

	public void setEnableFondi(boolean enableFondi) {
		this.enableFondi = enableFondi;
	}

	public void setDatiBibliografici(AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio2) {
		this.datiBibliografici = titoliBiblio2;

	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}



}
