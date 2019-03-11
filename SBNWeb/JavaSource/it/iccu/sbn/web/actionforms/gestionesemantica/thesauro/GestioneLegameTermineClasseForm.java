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
package it.iccu.sbn.web.actionforms.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO.LegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class GestioneLegameTermineClasseForm extends ActionForm {


	private static final long serialVersionUID = -3504763407786067492L;
	private boolean initialized;
	private boolean conferma;

	private OggettoRiferimentoVO oggettoRiferimento = new OggettoRiferimentoVO();
	private ParametriThesauro parametri = new ParametriThesauro();
	private DettaglioClasseVO classe;
	private DettaglioTermineThesauroVO dettaglio;
	private String[] pulsanti;
	private LegameTermineClasseVO legame;

	private List<TB_CODICI> listaSistemiClassificazione;
	private List<TB_CODICI> listaEdizioni;
	private List<TB_CODICI> listaStatoControllo;

	public List<TB_CODICI> getListaSistemiClassificazione() {
		return listaSistemiClassificazione;
	}

	public void setListaSistemiClassificazione(
			List<TB_CODICI> listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public List<TB_CODICI> getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List<TB_CODICI> listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public LegameTermineClasseVO getLegame() {
		return legame;
	}

	public void setLegame(LegameTermineClasseVO legame) {
		this.legame = legame;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public OggettoRiferimentoVO getOggettoRiferimento() {
		return oggettoRiferimento;
	}

	public void setOggettoRiferimento(OggettoRiferimentoVO oggettoRiferimento) {
		this.oggettoRiferimento = oggettoRiferimento;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public DettaglioClasseVO getClasse() {
		return classe;
	}

	public void setClasse(DettaglioClasseVO classe) {
		this.classe = classe;
	}

	public DettaglioTermineThesauroVO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(DettaglioTermineThesauroVO dettaglio) {
		this.dettaglio = dettaglio;
	}

}
