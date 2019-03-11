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
package it.iccu.sbn.web.actionforms.periodici;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;



public abstract class PeriodiciBaseForm extends ActionForm {


	private static final long serialVersionUID = -8926021768487769699L;

	private final Timestamp creationTime;

	private BibliotecaVO biblioteca = new BibliotecaVO();
	private ParametriPeriodici parametri = new ParametriPeriodici();
	private boolean initialized;
	private boolean conferma;
	private TipoOperazione operazione;
	private TipoOperazione operazioneInit;
	private int numeroElementiBlocco = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());

	private String[] pulsanti;

	public PeriodiciBaseForm() {
		super();
		this.creationTime = DaoManager.now();
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public BibliotecaVO getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public ParametriPeriodici getParametri() {
		return parametri;
	}

	public void setParametri(ParametriPeriodici parametri) {
		this.parametri = parametri;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public TipoOperazione getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazione operazione) {
		this.operazione = operazione;
	}

	public TipoOperazione getOperazioneInit() {
		return operazioneInit;
	}

	public void setOperazioneInit(TipoOperazione operazioneInit) {
		this.operazioneInit = operazioneInit;
	}

	public int getNumeroElementiBlocco() {
		return numeroElementiBlocco;
	}

	public void setNumeroElementiBlocco(int numeroElementiBlocco) {
		this.numeroElementiBlocco = numeroElementiBlocco;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

}
