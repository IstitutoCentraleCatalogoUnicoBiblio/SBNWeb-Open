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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionesemantica.DettaglioOggettoSemanticaVO;
import it.iccu.sbn.util.Constants;
import it.iccu.sbn.web.vo.TreeElementView;

public class TreeElementViewSoggetti extends TreeElementView {

	private static final long serialVersionUID = -3990492801816487208L;
	private String categoriaSoggetto;
	private SbnLegameAut datiLegame;
	private String testo;
	private String note;
	private boolean livelloPolo;
	private String formaNome;
	private int posizione = -1;
	private String notaLegame;

	AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();

	private boolean explored;

	public SbnLegameAut getDatiLegame() {
		return datiLegame;
	}

	public void setDatiLegame(SbnLegameAut datiLegame) {
		this.datiLegame = datiLegame;
	}

	public String getLivelloAutorita() {

		SbnAuthority authority = getTipoAuthority();
		if (authority == null)
			return "";

		switch (authority.getType()) {
		case SbnAuthority.SO_TYPE:
			return areaDatiDettaglioOggettiVO.getDettaglioSoggettoGeneraleVO().getLivAut();

		case SbnAuthority.DE_TYPE:
			return areaDatiDettaglioOggettiVO.getDettaglioDescrittoreGeneraleVO().getLivAut();

		case SbnAuthority.TH_TYPE:
			return areaDatiDettaglioOggettiVO.getDettaglioTermineThesauroVO().getLivAut();

		default:
			return "";
		}

	}

	public DettaglioOggettoSemanticaVO getDettaglio() {

		if (this.isSoggetto() )
			return this.areaDatiDettaglioOggettiVO.dettaglioSoggettoGeneraleVO;
		if (this.isDescrittore())
			return this.areaDatiDettaglioOggettiVO.dettaglioDescrittoreGeneraleVO;
		if (this.isTermineThesauro())
			return this.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO;

		return null;
	}

	public DettaglioOggettoSemanticaVO getDettaglioPadre() {

		TreeElementViewSoggetti padre = (TreeElementViewSoggetti) this.getParent();
		if (padre == null)
			return null;

		if (padre.isSoggetto() )
			return padre.areaDatiDettaglioOggettiVO.dettaglioSoggettoGeneraleVO;
		if (padre.isDescrittore())
			return padre.areaDatiDettaglioOggettiVO.dettaglioDescrittoreGeneraleVO;
		if (padre.isTermineThesauro())
			return padre.areaDatiDettaglioOggettiVO.dettaglioTermineThesauroVO;

		return null;
	}

	@Override
	public String getImageStyle() {
		String imageStyle = null;

		if (getTipoAuthority() == null) {
			String val = getDescription();
			String charVal = val.substring(3, 4);
			if (charVal.equals("C")) {
				imageStyle = "soggettoReticolo";
			} else {
				imageStyle = this.isRinvio() ? "formaRinvio" : "descrittoreReticolo";
			}

		} else if (getTipoAuthority().toString().equals("SO")) {
			imageStyle = "soggettoReticolo";
		} else if (getTipoAuthority().toString().equals("DE")) {
			imageStyle = this.isRinvio() ? "formaRinvio" : "descrittoreReticolo";
		} else if (getTipoAuthority().toString().equals("TH")) {
			imageStyle = this.isRinvio() ? "formaRinvio" : "descrittoreReticolo";
		}

		return imageStyle;
	}

	public String getCategoriaSoggetto() {
		return categoriaSoggetto;
	}

	public void setCategoriaSoggetto(String categoriaSoggetto) {
		this.categoriaSoggetto = categoriaSoggetto;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public boolean isLivelloPolo() {
		return livelloPolo;
	}

	public void setLivelloPolo(boolean livelloPolo) {
		this.livelloPolo = livelloPolo;
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public boolean isRinvio() {
		return formaNome == null ? false : formaNome.equalsIgnoreCase("R");
	}

	public boolean isAutomatico() {
		return isDescrittoreAutomatico();
	}

	public int getPosizione() {
		return posizione;
	}

	public void setPosizione(int posizione) {
		this.posizione = posizione;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public void adoptChildren(TreeElementViewSoggetti oldParent) {

		TreeElementView parent = this.getParent();
		this.setText(oldParent.getText());
		this.setKey(oldParent.getKey());
		this.setT005(oldParent.getT005());
		this.setAreaDatiDettaglioOggettiVO(oldParent.getAreaDatiDettaglioOggettiVO());
		this.children.clear();
		for (TreeElementView child : oldParent.children) {
			// scarto il legame ricorsivo
			if (parent != null && parent.getKey().equals(child.getKey())
					&& oldParent.children.size() == 1)
				continue;

			child.setParent(this);
			this.children.add(child);
		}
		this.setPlusVisible(this.hasChildren());
	}

	public String getNotaLegame() {
		return notaLegame;
	}

	public void setNotaLegame(String notaLegame) {
		this.notaLegame = notaLegame;
	}

	public boolean isSoggetto() {
		String key = this.getKey();
		return (isFilled(key) && ValidazioneDati.eqAuthority(getTipoAuthority(), SbnAuthority.SO) );
	}

	public boolean isDescrittore() {
		String key = this.getKey();
		return (isFilled(key) && ValidazioneDati.eqAuthority(getTipoAuthority(), SbnAuthority.DE) );
	}

	public boolean isTermineThesauro() {
		String key = this.getKey();
		return (isFilled(key) && ValidazioneDati.eqAuthority(getTipoAuthority(), SbnAuthority.TH) );
	}

	@Override
	public void open() {
		this.explored = true;
		super.open();
	}

	public boolean isExplored() {
		return explored;
	}

	public void setExplored(boolean explored) {
		this.explored = explored;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isDescrittoreManuale() {
		return ValidazioneDati.eqAuthority(getTipoAuthority(), SbnAuthority.DE)
				&& posizione == Constants.Semantica.Soggetti.POSIZIONE_DESCRITTORE_MANUALE;
	}

	public boolean isDescrittoreAutomatico() {
		return ValidazioneDati.eqAuthority(getTipoAuthority(), SbnAuthority.DE)
				&& posizione > 0
				&& posizione != Constants.Semantica.Soggetti.POSIZIONE_DESCRITTORE_MANUALE;
	}

}
