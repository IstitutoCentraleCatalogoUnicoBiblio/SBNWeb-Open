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
package it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class AcquisizioneUriCopiaDigitaleForm extends ActionForm {


	private static final long serialVersionUID = 3826071285821018664L;

	public enum FolderType {
		WITH_URI,
		WITHOUT_URI,
	}

	private boolean initialized;
	private AcquisizioneUriCopiaDigitaleVO richiesta = new AcquisizioneUriCopiaDigitaleVO();
	private String[] pulsanti;
	private FormFile input;
	private String model;

	private List<TB_CODICI> listaTipoDigit;
	private List<TB_CODICI> listaTeche;
	private List<TB_CODICI> listaDispDaRemoto;
	private List<TB_CODICI> listaTipoFileInput;
	private List<TB_CODICI>  listaTipoModelloURI;

	private String test;

	private FolderType folder;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public AcquisizioneUriCopiaDigitaleVO getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(AcquisizioneUriCopiaDigitaleVO richiesta) {
		this.richiesta = richiesta;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public FormFile getInput() {
		return input;
	}

	public void setInput(FormFile input) {
		this.input = input;
	}

	public List<TB_CODICI> getListaTipoDigit() {
		return listaTipoDigit;
	}

	public void setListaTipoDigit(List<TB_CODICI> listaTipoDigit) {
		this.listaTipoDigit = listaTipoDigit;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public List<TB_CODICI> getListaTeche() {
		return listaTeche;
	}

	public void setListaTeche(List<TB_CODICI> listaTeche) {
		this.listaTeche = listaTeche;
	}

	public List<TB_CODICI> getListaDispDaRemoto() {
		return listaDispDaRemoto;
	}

	public void setListaDispDaRemoto(List<TB_CODICI> listaDispDaRemoto) {
		this.listaDispDaRemoto = listaDispDaRemoto;
	}

	public List<TB_CODICI> getListaTipoFileInput() {
		return listaTipoFileInput;
	}

	public void setListaTipoFileInput(List<TB_CODICI> listaTipoFileInput) {
		this.listaTipoFileInput = listaTipoFileInput;
	}

	public List<TB_CODICI> getListaTipoModelloURI() {
		return listaTipoModelloURI;
	}

	public void setListaTipoModelloURI(List<TB_CODICI> listaTipoModelloURI) {
		this.listaTipoModelloURI = listaTipoModelloURI;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder) {
		this.folder = folder;
	}

	public String getFolderJSP() {
		return folder.name();
	}

}
