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
package it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario;

import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class RicercaBibliotecarioForm extends ActionForm {


	private static final long serialVersionUID = -5995251953757102332L;
	private String nomeRic = "";
	private String cognomeRic = "";
	private String usernameRic = "";
	private String selezioneBibRic;
	private List<ComboVO> elencoBib = new ArrayList<ComboVO>();
	private List<ComboVO> elencoOrdinamenti = new ArrayList<ComboVO>();
	private String selezioneOrdinamento;
	private boolean checkEsattaNome;
	private boolean checkEsattaCognome;
	private boolean checkEsattaUsername;
	private String checkAbilitato;
	private String dataAccesso = "";
	private String abilitatoNuovo = "";
	private String acquisizioni;
	private String stampaEtichette;

	// Manutenzione almaviva2 21.09.2011 -  inserite le chiamate/nuovi campi per la lentina del biblotecario (ute ins e var)
	// richiamata dall'estrattore magno
	private String estrattoreIdDocumenti;

	public String getStampaEtichette() {
		return stampaEtichette;
	}

	public void setStampaEtichette(String stampaEtichette) {
		this.stampaEtichette = stampaEtichette;
	}

	private String flagCentroSistema = "FALSE";

	public String getFlagCentroSistema() {
		return flagCentroSistema;
	}

	public void setFlagCentroSistema(String flagCentroSistema) {
		this.flagCentroSistema = flagCentroSistema;
	}

	public String getAbilitatoNuovo() {
		return abilitatoNuovo;
	}

	public void setAbilitatoNuovo(String abilitatoNuovo) {
		this.abilitatoNuovo = abilitatoNuovo;
	}

	public String getCheckAbilitato() {
		return checkAbilitato;
	}

	public void setCheckAbilitato(String checkAbilitato) {
		this.checkAbilitato = checkAbilitato;
	}

	public boolean isCheckEsattaNome() {
		return checkEsattaNome;
	}

	public void setCheckEsattaNome(boolean checkEsattaNome) {
		this.checkEsattaNome = checkEsattaNome;
	}

	public boolean isCheckEsattaCognome() {
		return checkEsattaCognome;
	}

	public void setCheckEsattaCognome(boolean checkEsattaCognome) {
		this.checkEsattaCognome = checkEsattaCognome;
	}

	public boolean isCheckEsattaUsername() {
		return checkEsattaUsername;
	}

	public void setCheckEsattaUsername(boolean checkEsattaUsername) {
		this.checkEsattaUsername = checkEsattaUsername;
	}

	public String getCognomeRic() {
		return cognomeRic;
	}

	public void setCognomeRic(String cognome) {
		this.cognomeRic = cognome;
	}

	public String getSelezioneBibRic() {
		return selezioneBibRic;
	}

	public void setSelezioneBibRic(String selezioneBib) {
		this.selezioneBibRic = selezioneBib;
	}

	public List<ComboVO> getElencoBib() {
		return elencoBib;
	}

	public void setElencoBib(List<ComboVO> elencoBib) {
		this.elencoBib = elencoBib;
	}

	public String getNomeRic() {
		return nomeRic;
	}

	public void setNomeRic(String nome) {
		this.nomeRic = nome;
	}

	public String getUsernameRic() {
		return usernameRic;
	}

	public void setUsernameRic(String username) {
		this.usernameRic = username;
	}

//	@Override
//	public void reset(ActionMapping mapping, HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		this.checkEsattaNome = false;
//		this.checkEsattaCognome = false;
//		this.checkEsattaUsername = false;
//		this.nomeRic = "";
//		this.cognomeRic = "";
//		this.selezioneBibRic = "";
//		this.usernameRic = "";
//		this.dataAccesso = "";
//		super.reset(mapping, request);
//	}

	public String getDataAccesso() {
		return dataAccesso;
	}

	public void setDataAccesso(String dataAccesso) {
		this.dataAccesso = dataAccesso;
	}

	public List<ComboVO> getElencoOrdinamenti() {
		return elencoOrdinamenti;
	}

	public void setElencoOrdinamenti(List<ComboVO> elencoOrdinamenti) {
		this.elencoOrdinamenti = elencoOrdinamenti;
	}

	public String getSelezioneOrdinamento() {
		return selezioneOrdinamento;
	}

	public void setSelezioneOrdinamento(String selezioneOrdinamento) {
		this.selezioneOrdinamento = selezioneOrdinamento;
	}

	public String getAcquisizioni() {
		return acquisizioni;
	}

	public void setAcquisizioni(String acquisizioni) {
		this.acquisizioni = acquisizioni;
	}

	public String getEstrattoreIdDocumenti() {
		return estrattoreIdDocumenti;
	}

	public void setEstrattoreIdDocumenti(String estrattoreIdDocumenti) {
		this.estrattoreIdDocumenti = estrattoreIdDocumenti;
	}

}
