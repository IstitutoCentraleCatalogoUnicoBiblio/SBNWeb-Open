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
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class DettaglioControlloIterForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 6254994740355100754L;
	private boolean sessione = false;
	private boolean nuovo    = false;
	private boolean conferma = false;

	private boolean bloccante=false;

	private String chiamante = "";

	private FaseControlloIterVO controlloIterVO = new FaseControlloIterVO();
	/**
	 *
	 * Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di aggiornamento
	 * per stabilire se ci sono delle modifiche da salvare o meno, per evitare di effettuare
	 * un aggiornamento inutile con valori uguali a quelli precedentemente inseriti.
	 */
	private FaseControlloIterVO ultimoSalvato;
	private TipoServizioVO tipoServizioVO = new TipoServizioVO();
	private IterServizioVO iterServizioVO = new IterServizioVO();

	private List<ComboCodDescVO> lstCodiciControllo;

	/**
	 * Controllo scelto dalla lista. E' una stringa costituita da:<br/>
	 * codice controllo+"_"+descrizione controllo
	 */
	private String conrolloScelto;
	/**
	 * Posizione nella lista di controlli in cui inserire il nuovo controllo
	 */
	private short posizioneInserimento=0;
	/**
	 * Tipo di operazione:<br/>
	 * <ul>
	 * <li>I: Inserimento</li>
	 * <li>A: Aggiunta in coda</li>
	 * </ul>
	 */
	private String tipoOperazione;



	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		bloccante=false;
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

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getChiamante() {
		return chiamante;
	}

	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}

	public boolean isBloccante() {
		return bloccante;
	}

	public void setBloccante(boolean bloccante) {
		this.bloccante = bloccante;
	}



	public FaseControlloIterVO getControlloIterVO() {
		return controlloIterVO;
	}



	public void setControlloIterVO(FaseControlloIterVO controlloIter) {
		this.controlloIterVO = controlloIter;
	}



	public TipoServizioVO getTipoServizioVO() {
		return tipoServizioVO;
	}



	public void setTipoServizioVO(TipoServizioVO tipoServizioVO) {
		this.tipoServizioVO = tipoServizioVO;
	}



	public IterServizioVO getIterServizioVO() {
		return iterServizioVO;
	}



	public void setIterServizioVO(IterServizioVO iterServizioVO) {
		this.iterServizioVO = iterServizioVO;
	}



	public FaseControlloIterVO getUltimoSalvato() {
		return ultimoSalvato;
	}



	public void setUltimoSalvato(FaseControlloIterVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}



	public List<ComboCodDescVO> getLstCodiciControllo() {
		return lstCodiciControllo;
	}



	public void setLstCodiciControllo(List<ComboCodDescVO> lstCodiciControllo) {
		this.lstCodiciControllo = lstCodiciControllo;
	}



	public String getConrolloScelto() {
		return conrolloScelto;
	}



	public void setConrolloScelto(String conrolloScelto) {
		this.conrolloScelto = conrolloScelto;
	}



	public short getPosizioneInserimento() {
		return posizioneInserimento;
	}



	public void setPosizioneInserimento(short posizioneInserimento) {
		this.posizioneInserimento = posizioneInserimento;
	}



	public String getTipoOperazione() {
		return tipoOperazione;
	}



	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
}
