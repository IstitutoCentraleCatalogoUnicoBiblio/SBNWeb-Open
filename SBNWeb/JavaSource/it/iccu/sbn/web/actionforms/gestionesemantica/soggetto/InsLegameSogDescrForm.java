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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.SoggettarioVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class InsLegameSogDescrForm extends SemanticaBaseForm {


	private static final long serialVersionUID = 3900701834408499594L;

	private RicercaComuneVO ricercaComune = new RicercaComuneVO();

	private boolean abilita = true;

	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();

	private DettaglioSoggettoVO dettSogGenVO = new DettaglioSoggettoVO();

	AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();

	private List listaSoggettari;

	private String did;

	private String cid;

	private String descrittore;

	private String note;

	private String dataInserimento;

	private String sogInserimento;

	private String dataModifica;

	private String sogModifica;

	private int numSoggetti;

	private int numUtilizzati;

	private String codice;

	private String action;

	private List ricerca;

	private boolean enableSoggetti = false;

	private boolean enableUtilizzati = false;

	private boolean treeDaLista = false;

	private boolean sessione = false;

	private boolean daControllare;

	private String livelloAutoritaSogg;

	private String T005;

	private String tipoSoggetto;

	private List<TB_CODICI> listaEdizioni;

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public String getDescrizione() {
		for (int index = 0; index < this.listaSoggettari.size(); index++) {
			SoggettarioVO sog = (SoggettarioVO) this.listaSoggettari.get(index);
			if (sog.getCodice().equals(this.codice))
				return sog.getDescrizione();
		}
		return "Non trovato";
	}

	public void validaTesto() throws ValidationException {
		if (this.getDescrittore().length() <= 0) {
			throw new ValidationException(
					ValidationException.erroreNoDigitazione);
		}
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

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
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

	public String getDescrittore() {
		return descrittore;
	}

	public void setDescrittore(String descrittore) {
		this.descrittore = descrittore;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isEnableSoggetti() {
		return enableSoggetti;
	}

	public void setEnableSoggetti(boolean enableSoggetti) {
		this.enableSoggetti = enableSoggetti;
	}

	public boolean isEnableUtilizzati() {
		return enableUtilizzati;
	}

	public void setEnableUtilizzati(boolean enableUtilizzati) {
		this.enableUtilizzati = enableUtilizzati;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getNumSoggetti() {
		return numSoggetti;
	}

	public void setNumSoggetti(int numSoggetti) {
		this.numSoggetti = numSoggetti;
	}

	public int getNumUtilizzati() {
		return numUtilizzati;
	}

	public void setNumUtilizzati(int numUtilizzati) {
		this.numUtilizzati = numUtilizzati;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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

	public boolean isDaControllare() {
		return daControllare;
	}

	public void setDaControllare(boolean daControllare) {
		this.daControllare = daControllare;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLivelloAutoritaSogg() {
		return this.livelloAutoritaSogg;
	}

	public void setLivelloAutoritaSogg(String livelloAutoritaSogg) {
		this.livelloAutoritaSogg = livelloAutoritaSogg;
	}

	public void setT005(String string) {
		this.T005 = string;

	}

	public String getT005() {
		return T005;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public DettaglioSoggettoVO getDettSogGenVO() {
		return dettSogGenVO;
	}

	public void setDettSogGenVO(DettaglioSoggettoVO dettSogGenVO) {
		this.dettSogGenVO = dettSogGenVO;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

}
