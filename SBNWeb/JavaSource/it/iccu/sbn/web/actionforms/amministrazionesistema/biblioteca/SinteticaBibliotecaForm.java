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
package it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class SinteticaBibliotecaForm extends ActionForm {


	private static final long serialVersionUID = 1983586364294686741L;
	private int bloccoCorrente;
	private boolean abilitaBlocchi;
    private int maxRighe = ConstantDefault.ELEMENTI_BLOCCHI.getDefaultAsNumber();
    private int totRighe;
    private String idLista = "";

    private int totBlocchi;
	private String selezRadio;
	private List<BibliotecaVO> elencoBiblioteche = new ArrayList<BibliotecaVO>();

	private String ordinamento;

	private String abilitatoNuovo;
	private String abilitatoProfiloRead;
	private String abilitatoProfiloWrite;

	private String acquisizioni;
	private String scaricoInventariale;
	private String servizioUtente;


	public String getAcquisizioni() {
		return acquisizioni;
	}

	public void setAcquisizioni(String acquisizioni) {
		this.acquisizioni = acquisizioni;
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

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}


	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
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

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public List<BibliotecaVO> getElencoBiblioteche() {
		return elencoBiblioteche;
	}

	public void setElencoBiblioteche(List<BibliotecaVO> elencoBiblioteche) {
		this.elencoBiblioteche = elencoBiblioteche;
	}

	public String getScaricoInventariale() {
		return scaricoInventariale;
	}

	public void setScaricoInventariale(String scaricoInventariale) {
		this.scaricoInventariale = scaricoInventariale;
	}

	public String getServizioUtente() {
		return servizioUtente;
	}

	public void setServizioUtente(String servizioUtente) {
		this.servizioUtente = servizioUtente;
	}

}
