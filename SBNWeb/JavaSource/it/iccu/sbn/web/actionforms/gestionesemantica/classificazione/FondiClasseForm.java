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
package it.iccu.sbn.web.actionforms.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ListaTitoliTrascinaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AbstractSinteticaSoggettiForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class FondiClasseForm extends AbstractSinteticaSoggettiForm {


	private static final long serialVersionUID = 8265063739232862038L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();
	private List listaSintetica;
	private List listaTitoliSelezionati;
	private String parameterConferma;
	private boolean sessione = false;
	private String elemBlocco;
	private String blocchiTotali;
	private String elementi;
	private String[] codSoggetto;
	private String action;
	private String did;
	private String notazioneTrascinaDa;
	private String testoTrascinaDa;
	private String notazioneTrascinaA;
	private String testoTrascinaA;
	private boolean enableConferma = false;
	private String codice;
	private String notazioneFusione;
	private boolean enableFondi = false;
	private boolean enableCarica = true;
	private List listaSoggettari;
	private boolean abilita = true;

	// tabella iterate
	// contiene oggetti di tipo it.iccu.sbn.ejb.vo.gestionesemantica.soggetto
	private ListaTitoliTrascinaVO listaTitoliTrascina;
	private RicercaSoggettoListaVO outputTrascina;

	public DettaglioClasseVO[] getClassi() throws ValidationException {

		DettaglioClasseVO classe1 = new DettaglioClasseVO();
		classe1.setIdentificativo(notazioneTrascinaDa);
		classe1.setDescrizione(testoTrascinaDa);

		DettaglioClasseVO classe2 = new DettaglioClasseVO();
		classe2.setIdentificativo(notazioneTrascinaA);
		classe2.setDescrizione(testoTrascinaA);

		return new DettaglioClasseVO[] {classe1, classe2};
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		return errors;
	}

	public String getBlocchiTotali() {
		return blocchiTotali;
	}

	public void setBlocchiTotali(String blocchiTotali) {
		this.blocchiTotali = blocchiTotali;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getElementi() {
		return elementi;
	}

	public void setElementi(String elementi) {
		this.elementi = elementi;
	}


	public ListaTitoliTrascinaVO getListaTitoliTrascina() {
		return listaTitoliTrascina;
	}

	public void setListaTitoliTrascina(ListaTitoliTrascinaVO output) {
		this.listaTitoliTrascina = output;
	}

	public String[] getCodSoggetto() {
		return codSoggetto;
	}

	public void setCodSoggetto(String[] codSoggetto) {
		this.codSoggetto = codSoggetto;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
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

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTestoTrascinaA() {
		return testoTrascinaA;
	}

	public void setTestoTrascinaA(String testoTrascinaA) {
		this.testoTrascinaA = testoTrascinaA;
	}

	public String getTestoTrascinaDa() {
		return testoTrascinaDa;
	}

	public void setTestoTrascinaDa(String testoTrascinaDa) {
		this.testoTrascinaDa = testoTrascinaDa;
	}

	public ListaTitoliTrascinaVO getItem(int index) {

	    // automatically grow List size
         while (index >= this.listaSintetica.size()) {
        	this.listaSintetica.add(new ListaTitoliTrascinaVO());
        }
        return (ListaTitoliTrascinaVO)this.listaSintetica.get(index);
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public List getListaTitoliSelezionati() {
		return listaTitoliSelezionati;
	}

	public void setListaTitoliSelezionati(List listaTitoliSelezionati) {
		this.listaTitoliSelezionati = listaTitoliSelezionati;
	}

	public String getParameterConferma() {
		return parameterConferma;
	}

	public void setParameterConferma(String parameterConferma) {
		this.parameterConferma = parameterConferma;
	}



	public RicercaSoggettoListaVO getOutputTrascina() {
		return outputTrascina;
	}

	public void setOutputTrascina(RicercaSoggettoListaVO outputTrascina) {
		this.outputTrascina = outputTrascina;
	}

	public String getNotazioneFusione() {
		return notazioneFusione;
	}

	public void setNotazioneFusione(String notazioneFusione) {
		this.notazioneFusione = notazioneFusione;
	}

	public String getNotazioneTrascinaA() {
		return notazioneTrascinaA;
	}

	public void setNotazioneTrascinaA(String notazioneTrascinaA) {
		this.notazioneTrascinaA = notazioneTrascinaA;
	}

	public String getNotazioneTrascinaDa() {
		return notazioneTrascinaDa;
	}

	public void setNotazioneTrascinaDa(String notazioneTrascinaDa) {
		this.notazioneTrascinaDa = notazioneTrascinaDa;
	}

	public boolean isEnableFondi() {
		return enableFondi;
	}

	public void setEnableFondi(boolean enableFondi) {
		this.enableFondi = enableFondi;
	}


	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
	}

	public boolean isEnableCarica() {
		return enableCarica;
	}

	public void setEnableCarica(boolean enableCarica) {
		this.enableCarica = enableCarica;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}








}
