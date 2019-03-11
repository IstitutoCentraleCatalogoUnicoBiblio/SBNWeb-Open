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

import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class ModelloSollecitoForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 7270581703747960647L;
	private boolean initialized = false;
	private String[] pulsanti;

	private ParametriBibliotecaVO parametri;
	private ModelloSollecitoVO modello = new ModelloSollecitoVO();

	private List<ComboVO> listaCampiModello;
	private String campo;

	private String format;
	private List<ComboVO> listaFormattazione;

	private TipoModello folder = TipoModello.LETTERA;

	private boolean conferma;


	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public ParametriBibliotecaVO getParametri() {
		return parametri;
	}

	public void setParametri(ParametriBibliotecaVO parametri) {
		this.parametri = parametri;
	}

	public ModelloSollecitoVO getModello() {
		return modello;
	}

	public void setModello(ModelloSollecitoVO modello) {
		this.modello = modello;
	}

	public String getInputField() {
		switch (folder) {
		case LETTERA:
			return modello.getModello();
		case EMAIL:
			return modello.getModelloMail();
		case SMS:
			return modello.getModelloSms();
		default:
			return "";
		}
	}

	public void setInputField(String inputField) {
		switch (folder) {
		case LETTERA:
			this.modello.setModello(inputField);
			break;
		case EMAIL:
			this.modello.setModelloMail(inputField);
			break;
		case SMS:
			this.modello.setModelloSms(inputField);
			break;
		}

	}

	public List<ComboVO> getListaCampiModello() {
		return listaCampiModello;
	}

	public void setListaCampiModello(List<ComboVO> listaCampiModello) {
		this.listaCampiModello = listaCampiModello;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<ComboVO> getListaFormattazione() {
		return listaFormattazione;
	}

	public void setListaFormattazione(List<ComboVO> listaFormattazione) {
		this.listaFormattazione = listaFormattazione;
	}

	public TipoModello getFolder() {
		return folder;
	}

	public void setFolder(TipoModello folder) {
		this.folder = folder;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

}
