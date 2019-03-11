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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.SoggettarioVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class InsDescrPerLegameDescrForm extends SemanticaBaseForm {


	private static final long serialVersionUID = 5269600578820378780L;

	private RicercaComuneVO ricercaComune = new RicercaComuneVO();

	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();

	private DettaglioDescrittoreVO dettDesGenVO = new DettaglioDescrittoreVO();

	AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();

	private boolean abilita = true;
	private List listaSoggettari;
	private List listaFormaNome;
	private List<ComboCodDescVO> listaLivelloAutorita;
	private String did;
	private String livAut;
	private String didPadre;
	private String primodid;
	private String primotesto;
	private String primaForma;
	private String secondaForma;
	private String secondoLivelloAut;
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
	private boolean enableCrea = false;
	private boolean sessione = false;
	private boolean daControllare;
	private String livelloAutorita;
	private String T005;
	private boolean condiviso = false;
	private RicercaSoggettoListaVO outputDescr;
	private String tipoSoggetto;

	private List<TB_CODICI> listaEdizioni;

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
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
		if (this.getDescrittore().length() <= 0 ) {
			throw new ValidationException(ValidationException.erroreNoDigitazione);
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


	public String getPrimodid() {
		return primodid;
	}


	public void setPrimodid(String primodid) {
		this.primodid = primodid;
	}


	public String getPrimotesto() {
		return primotesto;
	}


	public void setPrimotesto(String primotesto) {
		this.primotesto = primotesto;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public DettaglioDescrittoreVO getDettDesGenVO() {
		return dettDesGenVO;
	}

	public void setDettDesGenVO(DettaglioDescrittoreVO dettDesGenVO) {
		this.dettDesGenVO = dettDesGenVO;
	}

	public String getPrimaForma() {
		return primaForma;
	}

	public void setPrimaForma(String primaForma) {
		this.primaForma = primaForma;
	}

	public RicercaSoggettoListaVO getOutputDescr() {
		return outputDescr;
	}

	public void setOutputDescr(RicercaSoggettoListaVO outputDescr) {
		this.outputDescr = outputDescr;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public List getListaFormaNome() {
		return listaFormaNome;
	}

	public void setListaFormaNome(List listaFormaNome) {
		this.listaFormaNome = listaFormaNome;
	}

	public boolean isEnableCrea() {
		return enableCrea;
	}

	public void setEnableCrea(boolean enableCrea) {
		this.enableCrea = enableCrea;
	}

	public String getSecondaForma() {
		return secondaForma;
	}

	public void setSecondaForma(String secondaForma) {
		this.secondaForma = secondaForma;
	}

	public String getSecondoLivelloAut() {
		return secondoLivelloAut;
	}

	public void setSecondoLivelloAut(String secondoLivelloAut) {
		this.secondoLivelloAut = secondoLivelloAut;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public String getDidPadre() {
		return didPadre;
	}

	public void setDidPadre(String didPadre) {
		this.didPadre = didPadre;
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}

	public List<ComboCodDescVO> getListaLivelloAutorita() {
		return listaLivelloAutorita;
	}

	public void setListaLivelloAutorita(List<ComboCodDescVO> listaLivelloAutorita) {
		this.listaLivelloAutorita = listaLivelloAutorita;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

}
