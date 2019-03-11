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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.web.vo.TreeElementView;

public class CatalogazioneSemanticaComuneVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -6873295360694491099L;
	private String bid;
	private String testoBid;
	private String naturaBid;
	private String tipoMaterialeBid;
	private String livAutBid;
	private String elemBlocco;
	private String blocchiTotali;
	private String elementi;
	private Integer linkProgressivo;
	private FolderType folder = FolderType.FOLDER_SOGGETTI;
	private FolderType folderClassi = FolderType.FOLDER_CLASSI;
	private FolderType folderAbstract = FolderType.FOLDER_ABSTRACT;

	private TreeElementView reticoloIndice;

	// Creo il default di catalogazione semantica soggetto inizializzato con
	// stringe vuote
	private CatSemSoggettoVO catalogazioneSoggetto = new CatSemSoggettoVO();

	// Creo il default di catalogazione semantica classificazioni inizializzato
	// con stringe vuote
	private CatSemClassificazioneVO catalogazioneClassificazione = new CatSemClassificazioneVO();

	// Creo il default di catalogazione semantica thesauro inizializzato con
	// stringe vuote
	private CatSemThesauroVO catalogazioneThesauro = new CatSemThesauroVO();

	// Creo il default di catalogazione semantica abstract inizializzato con
	// stringe vuote
	private CatSemAbstractVO catalogazioneAbstract = new CatSemAbstractVO();

	public CatalogazioneSemanticaComuneVO() {
		super();
	}

	public CatalogazioneSemanticaComuneVO(String bid, String testoBid,
			boolean polo,
			CatSemSoggettoVO catalogazioneSoggetto,
			CatSemClassificazioneVO catalogazioneClassificazione,
			CatSemThesauroVO catalogazioneThesauro,
			CatSemAbstractVO catalogazioneAbstract) {
		this.bid = bid;
		this.testoBid = testoBid;
		this.setLivelloPolo(polo);
		this.catalogazioneSoggetto = catalogazioneSoggetto;
		this.catalogazioneClassificazione = catalogazioneClassificazione;
		this.catalogazioneThesauro = catalogazioneThesauro;
		this.catalogazioneAbstract = catalogazioneAbstract;

	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public CatSemAbstractVO getCatalogazioneAbstract() {
		return catalogazioneAbstract;
	}

	public void setCatalogazioneAbstract(CatSemAbstractVO catalogazioneAbstract) {
		this.catalogazioneAbstract = catalogazioneAbstract;
	}

	public CatSemClassificazioneVO getCatalogazioneClassificazione() {
		return catalogazioneClassificazione;
	}

	public void setCatalogazioneClassificazione(
			CatSemClassificazioneVO catalogazioneClassificazione) {
		this.catalogazioneClassificazione = catalogazioneClassificazione;
	}

	public CatSemSoggettoVO getCatalogazioneSoggetto() {
		return catalogazioneSoggetto;
	}

	public void setCatalogazioneSoggetto(CatSemSoggettoVO catalogazioneSoggetto) {
		this.catalogazioneSoggetto = catalogazioneSoggetto;
	}

	public CatSemThesauroVO getCatalogazioneThesauro() {
		return catalogazioneThesauro;
	}

	public void setCatalogazioneThesauro(CatSemThesauroVO catalogazioneThesauro) {
		this.catalogazioneThesauro = catalogazioneThesauro;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder) {
		this.folder = folder;
	}

	public boolean isIndice() {
		return !isLivelloPolo();
	}

	public boolean isPolo() {
		return isLivelloPolo();
	}

	public void setPolo(boolean polo) {
		this.setLivelloPolo(polo);
	}

	public String getTestoBid() {
		return testoBid;
	}

	public void setTestoBid(String testoBid) {
		this.testoBid = testoBid;
	}

	public String getBlocchiTotali() {
		return blocchiTotali;
	}

	public void setBlocchiTotali(String blocchiTotali) {
		this.blocchiTotali = blocchiTotali;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getElementi() {
		return elementi;
	}

	public void setElementi(String elementi) {
		this.elementi = elementi;
	}

	public String getLivAutBid() {
		return livAutBid;
	}

	public void setLivAutBid(String livAutBid) {
		this.livAutBid = livAutBid;
	}

	public String getNaturaBid() {
		return naturaBid;
	}

	public void setNaturaBid(String naturaBid) {
		this.naturaBid = naturaBid;
	}

	public String getTipoMaterialeBid() {
		return tipoMaterialeBid;
	}

	public void setTipoMaterialeBid(String tipoMaterialeBid) {
		this.tipoMaterialeBid = tipoMaterialeBid;
	}

	public FolderType getFolderClassi() {
		return folderClassi;
	}

	public void setFolderClassi(FolderType folderClassi) {
		this.folderClassi = folderClassi;
	}

	public FolderType getFolderAbstract() {
		return folderAbstract;
	}

	public void setFolderAbstract(FolderType folderAbstract) {
		this.folderAbstract = folderAbstract;
	}

	public Integer getLinkProgressivo() {
		return linkProgressivo;
	}

	public void setLinkProgressivo(Integer linkProgressivo) {
		this.linkProgressivo = linkProgressivo;
	}

	public TreeElementView getReticoloIndice() {
		return reticoloIndice;
	}

	public void setReticoloIndice(TreeElementView reticoloIndice) {
		this.reticoloIndice = reticoloIndice;
	}

}
