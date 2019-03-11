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

import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class DettaglioTipoServizioForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -2652059998449882235L;
	private String  chiamante;
	private boolean conferma=false;
	private boolean sessione=false;
	private boolean nuovo   =false;
	private List lstTipiServizio;
	private TipoServizioVO tipoServizio = new TipoServizioVO();
	//Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di aggiornamento
	//per stabilire se ci sono delle modifiche da salvare o meno, per evitare di effettuare
	//un aggiornamento inutile con valori uguali a quelli precedentemente inseriti.
	private TipoServizioVO ultimoSalvato;

	private boolean ammInsUtenteParamBiblioteca;

	public boolean isAmmInsUtenteParamBiblioteca() {
		return ammInsUtenteParamBiblioteca;
	}
	public void setAmmInsUtenteParamBiblioteca(boolean ammInsUtenteParamBiblioteca) {
		this.ammInsUtenteParamBiblioteca = ammInsUtenteParamBiblioteca;
	}

	private String inserimentoUtente = null;

	public String getInserimentoUtente() {
		return inserimentoUtente;
	}

	public void setInserimentoUtente(String inserimentoUtente) {
		this.inserimentoUtente = inserimentoUtente;
	}

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
	public TipoServizioVO getTipoServizio() {
		return tipoServizio;
	}
	public void setTipoServizio(TipoServizioVO tipoServizio) {
		this.tipoServizio = tipoServizio;
	}
	public List getLstTipiServizio() {
		return lstTipiServizio;
	}
	public void setLstTipiServizio(List lstTipiServizio) {
		this.lstTipiServizio = lstTipiServizio;
	}
	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public TipoServizioVO getUltimoSalvato() {
		return ultimoSalvato;
	}
	public void setUltimoSalvato(TipoServizioVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}

	@Override
	public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest)
    {
		tipoServizio.setPenalita(false);
    }
}
