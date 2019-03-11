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
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ListaTitoliTrascinaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AbstractSinteticaSoggettiForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class TrascinaTitoliClassiForm extends AbstractSinteticaSoggettiForm {


	private static final long serialVersionUID = 421530138571883527L;
	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();;
	private List listaSintetica;
	private List listaTitoliSelezionati;
	private String parameterConferma;
	private boolean sessione = false;
	private String elemBlocco;
	private String blocchiTotali;
	private String elementi;
	private String[] codClasse;
	private String action;
	private String notazioneTrascinaDa;
	private String testoTrascinaDa;
	private String notazioneTrascinaA;
	private String testoTrascinaA;
	private boolean enableConferma = false;
	private String codice;

	// tabella iterate
	// contiene oggetti di tipo it.iccu.sbn.ejb.vo.gestionesemantica.soggetto
	private ListaTitoliTrascinaVO listaTitoliTrascina;
	private RicercaClasseListaVO outputTrascina;
	private boolean abilita = true;


	public DettaglioClasseVO[] getClassi() throws ValidationException {

		DettaglioClasseVO classe1 = new DettaglioClasseVO();
		classe1.setIdentificativo(notazioneTrascinaDa);
		classe1.setDescrizione(testoTrascinaDa);

		DettaglioClasseVO classe2 = new DettaglioClasseVO();
		classe2.setIdentificativo(notazioneTrascinaA);
		classe2.setDescrizione(testoTrascinaA);

		return new DettaglioClasseVO[] {classe1, classe2};
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

	public RicercaClasseListaVO getOutputTrascina() {
		return outputTrascina;
	}

	public void setOutputTrascina(RicercaClasseListaVO outputTrascina) {
		this.outputTrascina = outputTrascina;
	}

	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
	}

	public String[] getCodClasse() {
		return codClasse;
	}

	public void setCodClasse(String[] codClasse) {
		this.codClasse = codClasse;
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

}
