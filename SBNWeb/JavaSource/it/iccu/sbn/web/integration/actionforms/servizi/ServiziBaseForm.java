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
package it.iccu.sbn.web.integration.actionforms.servizi;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts.action.ActionForm;

public abstract class ServiziBaseForm extends ActionForm {


	private static final long serialVersionUID = 8041096991926373862L;
	private final Timestamp creationTime;
	private String biblioteca;
	private boolean initialized;
	private Set<Integer> blocchiCaricati = new HashSet<Integer>();
	private int maxRighe = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private DescrittoreBloccoVO blocco;
	private int bloccoSelezionato;

	private boolean conferma;

	private String[] pulsanti;

	private ParametriServizi parametri = new ParametriServizi();

	private TipoOperazione operazione;

	{
		this.creationTime = DaoManager.now();
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public Set<Integer> getBlocchiCaricati() {
		return blocchiCaricati;
	}

	public void setBlocchiCaricati(Set<Integer> blocchiCaricati) {
		this.blocchiCaricati = blocchiCaricati;
	}

	public DescrittoreBloccoVO getBlocco() {
		return blocco;
	}

	public void setBlocco(DescrittoreBloccoVO blocco) {
		this.blocco = blocco;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public ParametriServizi getParametri() {
		return parametri;
	}

	public void setParametri(ParametriServizi parametri) {
		this.parametri = parametri;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public TipoOperazione getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazione operazione) {
		this.operazione = operazione;
	}

}
