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
package it.iccu.sbn.web.actionforms.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaDescrittoriParoleForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 3361019956594366129L;
	AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private RicercaDescrittoreVO ricercaDescrittoreParole = new RicercaDescrittoreVO();
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private FolderType folder;
	private String parole;
	private String primaParola;
	private String parole1;
	private String secondaParola;
	private String parole2;
	private String terzaParola;
	private String parole3;
	private String quartaParola;
	private String parole4;
	private String quintaParola;
	private boolean enableCercaIndice = true;

	private String codice;
	private boolean noTutti  = false;
	private boolean sessione = false;

	private String action;

	private List listaSintetica;

	private int numNotizie;



	// tabella iterate
	// contiene oggetti di tipo it.iccu.sbn.ejb.vo.gestionesemantica.soggetto
	private RicercaSoggettoListaVO output;

	private boolean enableParole=false;
	private boolean enableParole1=false;
	private boolean enableParole2=false;
	private boolean enableParole3=false;

	private boolean abilita = true;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

	public RicercaSoggettoListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaSoggettoListaVO output) {
		this.output = output;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

		public int getNumNotizie() {
		if (this.numNotizie == 0){
			return listaSintetica.size();
		}else
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public boolean isEnableParole() {
		return enableParole;
	}

	public void setEnableParole(boolean enableParole) {
		this.enableParole = enableParole;
	}

	public boolean isEnableParole1() {
		return enableParole1;
	}

	public void setEnableParole1(boolean enableParole1) {
		this.enableParole1 = enableParole1;
	}

	public boolean isEnableParole2() {
		return enableParole2;
	}

	public void setEnableParole2(boolean enableParole2) {
		this.enableParole2 = enableParole2;
	}

	public boolean isEnableParole3() {
		return enableParole3;
	}

	public void setEnableParole3(boolean enableParole3) {
		this.enableParole3 = enableParole3;
	}

	public String getParole() {
		return parole;
	}

	public void setParole(String parole) {
		this.parole = parole;
	}

	public String getParole1() {
		return parole1;
	}

	public void setParole1(String parole1) {
		this.parole1 = parole1;
	}

	public String getParole2() {
		return parole2;
	}

	public void setParole2(String parole2) {
		this.parole2 = parole2;
	}

	public String getParole3() {
		return parole3;
	}

	public void setParole3(String parole3) {
		this.parole3 = parole3;
	}


	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getParole4() {
		return parole4;
	}

	public void setParole4(String parole4) {
		this.parole4 = parole4;
	}

	public boolean isEnableCercaIndice() {
		return enableCercaIndice;
	}

	public void setEnableCercaIndice(boolean enableCercaIndice) {
		this.enableCercaIndice = enableCercaIndice;
	}

	public String getPrimaParola() {
		return primaParola;
	}

	public void setPrimaParola(String primaParola) {
		this.primaParola = primaParola;
	}

	public String getQuartaParola() {
		return quartaParola;
	}

	public void setQuartaParola(String quartaParola) {
		this.quartaParola = quartaParola;
	}

	public String getQuintaParola() {
		return quintaParola;
	}

	public void setQuintaParola(String quintaParola) {
		this.quintaParola = quintaParola;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public RicercaDescrittoreVO getRicercaDescrittoreParole() {
		return ricercaDescrittoreParole;
	}

	public void setRicercaDescrittoreParole(
			RicercaDescrittoreVO ricercaDescrittoreParole) {
		this.ricercaDescrittoreParole = ricercaDescrittoreParole;
	}

	public String getSecondaParola() {
		return secondaParola;
	}

	public void setSecondaParola(String secondaParola) {
		this.secondaParola = secondaParola;
	}

	public String getTerzaParola() {
		return terzaParola;
	}

	public void setTerzaParola(String terzaParola) {
		this.terzaParola = terzaParola;
	}

	public boolean isNoTutti() {
		return noTutti;
	}

	public void setNoTutti(boolean noTutti) {
		this.noTutti = noTutti;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder) {
		this.folder = folder;
	}


}
















