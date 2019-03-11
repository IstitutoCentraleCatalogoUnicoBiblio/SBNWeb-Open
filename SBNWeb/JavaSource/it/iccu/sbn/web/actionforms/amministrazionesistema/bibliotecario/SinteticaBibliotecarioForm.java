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

import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class SinteticaBibliotecarioForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -548130483519829382L;
	private int bloccoCorrente;
	private boolean abilitaBlocchi;

	private String selezRadio;
	private List<UtenteVO> elencoUtenti = new ArrayList<UtenteVO>();

	private String abilitatoNuovo;
	private String abilitatoProfiloRead;
	private String abilitatoProfiloWrite;

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

	private String ordinamento;

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public List<UtenteVO> getElencoUtenti() {
		return elencoUtenti;
	}

	public void setElencoUtenti(List<UtenteVO> elencoUtenti) {
		this.elencoUtenti = elencoUtenti;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public int getBloccoCorrente() {
		return bloccoCorrente;
	}

	public void setBloccoCorrente(int bloccoCorrente) {
		this.bloccoCorrente = bloccoCorrente;
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public String getAbilitatoNuovo() {
		return abilitatoNuovo;
	}

	public void setAbilitatoNuovo(String abilitatoNuovo) {
		this.abilitatoNuovo = abilitatoNuovo;
	}

	public String getAbilitatoProfiloRead() {
		return abilitatoProfiloRead;
	}

	public void setAbilitatoProfiloRead(String abilitatoProfiloRead) {
		this.abilitatoProfiloRead = abilitatoProfiloRead;
	}

	public String getAbilitatoProfiloWrite() {
		return abilitatoProfiloWrite;
	}

	public void setAbilitatoProfiloWrite(String abilitatoProfiloWrite) {
		this.abilitatoProfiloWrite = abilitatoProfiloWrite;
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
