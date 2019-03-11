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
package it.iccu.sbn.web.actionforms.servizi.configurazione;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioAttivitaForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -5293800050437238712L;
	private boolean conferma = false;
	private boolean confermaContinuaConfigurazione=false;
	private boolean sessione = false;
	private boolean nuovo    = false;
	private boolean obbligatoria = false;
	private boolean prorogabile  = false;
	//private boolean gestioneControllo=false;

	private String chiamante="";
	private String tipoOperazione = "A";
	private IterServizioVO iterServizio = new IterServizioVO();

	private int progressivoIterScelto=0;


	/**
	 *
	 * Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di aggiornamento
	 * per stabilire se ci sono delle modifiche da salvare o meno, per evitare di effettuare
	 * un aggiornamento inutile con valori uguali a quelli precedentemente inseriti.
	 */
	private IterServizioVO ultimoSalvato;

	private TipoServizioVO tipoServizioVO = new TipoServizioVO();

	private List<ComboCodDescVO> lstCodiciRichiesta;
	private List<ComboCodDescVO> lstCodiciMovimento;
	private List<ComboCodDescVO> lstCodiciAttivita;


	/**
	 * Codici delle attività già assegnate al tipo servizio
	 */
	private List<String> lstCodiciAttivitaGiaAssegnati;

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public String getChiamante() {
		return chiamante;
	}

	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}

	public IterServizioVO getIterServizio() {
		return iterServizio;
	}

	public void setIterServizio(IterServizioVO iterServizio) {
		this.iterServizio = iterServizio;
	}

	public boolean isObbligatoria() {
		return obbligatoria;
	}

	public void setObbligatoria(boolean obbligatoria) {
		this.obbligatoria = obbligatoria;
	}

	public boolean isProrogabile() {
		return prorogabile;
	}

	public void setProrogabile(boolean prorogabile) {
		this.prorogabile = prorogabile;
	}

	public IterServizioVO getUltimoSalvato() {
		return ultimoSalvato;
	}

	public void setUltimoSalvato(IterServizioVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}

	public TipoServizioVO getTipoServizioVO() {
		return tipoServizioVO;
	}

	public void setTipoServizioVO(TipoServizioVO tipoServizioVO) {
		this.tipoServizioVO = tipoServizioVO;
	}

	public List<ComboCodDescVO> getLstCodiciRichiesta() {
		return lstCodiciRichiesta;
	}

	public void setLstCodiciRichiesta(List<ComboCodDescVO> lstCodiciRichiesta) {
		this.lstCodiciRichiesta = lstCodiciRichiesta;
	}

	public List<ComboCodDescVO> getLstCodiciMovimento() {
		return lstCodiciMovimento;
	}

	public void setLstCodiciMovimento(List<ComboCodDescVO> lstCodiciMovimento) {
		this.lstCodiciMovimento = lstCodiciMovimento;
	}

	public List<ComboCodDescVO> getLstCodiciAttivita() {
		return lstCodiciAttivita;
	}

	public void setLstCodiciAttivita(List<ComboCodDescVO> lstCodiciAttivita) {
		this.lstCodiciAttivita = lstCodiciAttivita;
	}

	public List<String> getLstCodiciAttivitaGiaAssegnati() {
		return lstCodiciAttivitaGiaAssegnati;
	}

	public void setLstCodiciAttivitaGiaAssegnati(
			List<String> lstCodiciAttivitaGiaAssegnati) {
		this.lstCodiciAttivitaGiaAssegnati = lstCodiciAttivitaGiaAssegnati;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public int getProgressivoIterScelto() {
		return progressivoIterScelto;
	}

	public void setProgressivoIterScelto(int progressivoIterScelto) {
		this.progressivoIterScelto = progressivoIterScelto;
	}

	public boolean isConfermaContinuaConfigurazione() {
		return confermaContinuaConfigurazione;
	}

	public void setConfermaContinuaConfigurazione(
			boolean confermaContinuaConfigurazione) {
		this.confermaContinuaConfigurazione = confermaContinuaConfigurazione;
	}

}
