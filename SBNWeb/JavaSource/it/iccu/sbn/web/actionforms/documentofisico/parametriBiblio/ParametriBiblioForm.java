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
package it.iccu.sbn.web.actionforms.documentofisico.parametriBiblio;

import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class ParametriBiblioForm extends ActionForm {


	private static final long serialVersionUID = -8074964984227140870L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private List listaBiblio;
	private String codScarUni;
	private List listaScarUni;
	private String ticket;

	private String codScaricoSelez;
	private List listaCodScarico;

	private List listaComboModelli = new ArrayList();
	private List listaModelli = new ArrayList();
	private boolean noModelli;
	private String codModello;
	private String tipoFormato;

	private boolean disable;
	private boolean sessione=false;
	private String folder;

// stampa etichette
	private String formEtich;
	private List listaFormEtich;
	private boolean utilizzoSerie;

//	 stampa schede
	private boolean stampaTit;
	private String sceltaAutori;
	private List listaSceltaAutori;
	private boolean autori;
	private boolean topografico;
	private boolean soggetti;
	private boolean titoli;
	private boolean editori;
	private boolean classificazioni;
	private boolean possessori;
	private boolean principale;
	private boolean topografico2;
	private boolean soggetti2;
	private boolean titoli2;
	private boolean editori2;
	private boolean classificazioni2;
	private boolean possessori2;
	private boolean richiami;
	private int copiePrincipale;
	private int copieTopografico2;
	private int copieSoggetti2;
	private int copieTitoli2;
	private int copieEditori2;
	private int copieClassificazioni2;
	private int copiePossessori2;
	private int copieRichiami;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public boolean isAutori() {
		return autori;
	}
	public void setAutori(boolean autori) {
		this.autori = autori;
	}
	public boolean isClassificazioni() {
		return classificazioni;
	}
	public void setClassificazioni(boolean classificazioni) {
		this.classificazioni = classificazioni;
	}
	public boolean isClassificazioni2() {
		return classificazioni2;
	}
	public void setClassificazioni2(boolean classificazioni2) {
		this.classificazioni2 = classificazioni2;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodScarUni() {
		return codScarUni;
	}
	public void setCodScarUni(String codScarUni) {
		this.codScarUni = codScarUni;
	}
	public int getCopieEditori2() {
		return copieEditori2;
	}
	public void setCopieEditori2(int copieEditori2) {
		this.copieEditori2 = copieEditori2;
	}
	public int getCopiePossessori2() {
		return copiePossessori2;
	}
	public void setCopiePossessori2(int copiePossessori2) {
		this.copiePossessori2 = copiePossessori2;
	}
	public int getCopiePrincipale() {
		return copiePrincipale;
	}
	public void setCopiePrincipale(int copiePrincipale) {
		this.copiePrincipale = copiePrincipale;
	}
	public int getCopieRichiami() {
		return copieRichiami;
	}
	public void setCopieRichiami(int copieRichiami) {
		this.copieRichiami = copieRichiami;
	}
	public int getCopieSoggetti2() {
		return copieSoggetti2;
	}
	public void setCopieSoggetti2(int copieSoggetti2) {
		this.copieSoggetti2 = copieSoggetti2;
	}
	public int getCopieTitoli2() {
		return copieTitoli2;
	}
	public void setCopieTitoli2(int copieTitoli2) {
		this.copieTitoli2 = copieTitoli2;
	}
	public int getCopieTopografico2() {
		return copieTopografico2;
	}
	public void setCopieTopografico2(int copieTopografico2) {
		this.copieTopografico2 = copieTopografico2;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isEditori() {
		return editori;
	}
	public void setEditori(boolean editori) {
		this.editori = editori;
	}
	public boolean isEditori2() {
		return editori2;
	}
	public void setEditori2(boolean editori2) {
		this.editori2 = editori2;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public String getFormEtich() {
		return formEtich;
	}
	public void setFormEtich(String formEtich) {
		this.formEtich = formEtich;
	}
	public List getListaBiblio() {
		return listaBiblio;
	}
	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}
	public List getListaFormEtich() {
		return listaFormEtich;
	}
	public void setListaFormEtich(List listaFormEtich) {
		this.listaFormEtich = listaFormEtich;
	}
	public List getListaScarUni() {
		return listaScarUni;
	}
	public void setListaScarUni(List listaScarUni) {
		this.listaScarUni = listaScarUni;
	}
	public List getListaSceltaAutori() {
		return listaSceltaAutori;
	}
	public void setListaSceltaAutori(List listaSceltaAutori) {
		this.listaSceltaAutori = listaSceltaAutori;
	}
	public boolean isPossessori() {
		return possessori;
	}
	public void setPossessori(boolean possessori) {
		this.possessori = possessori;
	}
	public boolean isPossessori2() {
		return possessori2;
	}
	public void setPossessori2(boolean possessori2) {
		this.possessori2 = possessori2;
	}
	public boolean isPrincipale() {
		return principale;
	}
	public void setPrincipale(boolean principale) {
		this.principale = principale;
	}
	public boolean isRichiami() {
		return richiami;
	}
	public void setRichiami(boolean richiami) {
		this.richiami = richiami;
	}
	public String getSceltaAutori() {
		return sceltaAutori;
	}
	public void setSceltaAutori(String sceltaAutori) {
		this.sceltaAutori = sceltaAutori;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isSoggetti() {
		return soggetti;
	}
	public void setSoggetti(boolean soggetti) {
		this.soggetti = soggetti;
	}
	public boolean isSoggetti2() {
		return soggetti2;
	}
	public void setSoggetti2(boolean soggetti2) {
		this.soggetti2 = soggetti2;
	}
	public boolean isTitoli() {
		return titoli;
	}
	public void setTitoli(boolean titoli) {
		this.titoli = titoli;
	}
	public boolean isTitoli2() {
		return titoli2;
	}
	public void setTitoli2(boolean titoli2) {
		this.titoli2 = titoli2;
	}
	public boolean isTopografico() {
		return topografico;
	}
	public void setTopografico(boolean topografico) {
		this.topografico = topografico;
	}
	public boolean isTopografico2() {
		return topografico2;
	}
	public void setTopografico2(boolean topografico2) {
		this.topografico2 = topografico2;
	}
	public boolean isUtilizzoSerie() {
		return utilizzoSerie;
	}
	public void setUtilizzoSerie(boolean utilizzoSerie) {
		this.utilizzoSerie = utilizzoSerie;
	}
	public boolean isStampaTit() {
		return stampaTit;
	}
	public void setStampaTit(boolean stampaTit) {
		this.stampaTit = stampaTit;
	}
	public int getCopieClassificazioni2() {
		return copieClassificazioni2;
	}
	public void setCopieClassificazioni2(int copieClassificazioni2) {
		this.copieClassificazioni2 = copieClassificazioni2;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public List getListaComboModelli() {
		return listaComboModelli;
	}
	public void setListaComboModelli(List listaComboModelli) {
		this.listaComboModelli = listaComboModelli;
	}
	public List getListaModelli() {
		return listaModelli;
	}
	public void setListaModelli(List listaModelli) {
		this.listaModelli = listaModelli;
	}
	public boolean isNoModelli() {
		return noModelli;
	}
	public void setNoModelli(boolean noModelli) {
		this.noModelli = noModelli;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodModello() {
		return codModello;
	}
	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}
	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getCodScaricoSelez() {
		return codScaricoSelez;
	}
	public void setCodScaricoSelez(String codScaricoSelez) {
		this.codScaricoSelez = codScaricoSelez;
	}
	public List getListaCodScarico() {
		return listaCodScarico;
	}
	public void setListaCodScarico(List listaCodScarico) {
		this.listaCodScarico = listaCodScarico;
	}
}
