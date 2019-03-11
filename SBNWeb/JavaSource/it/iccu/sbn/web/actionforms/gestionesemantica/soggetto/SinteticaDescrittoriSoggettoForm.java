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
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaDescrittoriSoggettoForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -4382617575567840027L;
	AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private RicercaSoggettoDescrittoriVO ricercaSoggettoDescrittore = new RicercaSoggettoDescrittoriVO();
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private FolderType folder;
	private String descrittoriSogg;
	private String primoDid;
	private String descrittoriSogg1;
	private String secondoDid;
	private String descrittoriSogg2;
	private String terzoDid;
	private String descrittoriSogg3;
	private String quartoDid;
	private String descrittoriSogg4;
	private String quintoDid;
	private boolean noTutti  = false;
	private boolean sessione = false;
	private String action;
	private RicercaSoggettoListaVO outputDescrittori;
	private String codice;
	private boolean enableCercaIndice = true;
	private int numNotizie;
	private List listaSintetica;

	private boolean enableDescrittori=false;
	private boolean enableDescrittori1=false;
	private boolean enableDescrittori2=false;
	private boolean enableDescrittori3=false;



	private boolean abilita = true;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

//	public String getSogUnico() {
//		RicercaVO lisST = (RicercaVO)listaSintetica.get(1);
//		return lisST.getCid();
//	}
//
//	public int getNumNotizie() {
//		if (this.numNotizie == 0){
//			return listaSintetica.size();
//		}else
//		return numNotizie;
//	}
//
//	public void setNumNotizie(int numNotizie) {
//		this.numNotizie = numNotizie;
//	}
//
	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}


	public String getDescrittoriSogg() {
		return descrittoriSogg;
	}

	public void setDescrittoriSogg(String descrittoriSogg) {
		this.descrittoriSogg = descrittoriSogg;
	}

	public String getDescrittoriSogg1() {
		return descrittoriSogg1;
	}

	public void setDescrittoriSogg1(String descrittoriSogg1) {
		this.descrittoriSogg1 = descrittoriSogg1;
	}

	public String getDescrittoriSogg2() {
		return descrittoriSogg2;
	}

	public void setDescrittoriSogg2(String descrittoriSogg2) {
		this.descrittoriSogg2 = descrittoriSogg2;
	}

	public String getDescrittoriSogg3() {
		return descrittoriSogg3;
	}

	public void setDescrittoriSogg3(String descrittoriSogg3) {
		this.descrittoriSogg3 = descrittoriSogg3;
	}


	public boolean isEnableDescrittori() {
		return enableDescrittori;
	}

	public void setEnableDescrittori(boolean enableDescrittori) {
		this.enableDescrittori = enableDescrittori;
	}

	public boolean isEnableDescrittori1() {
		return enableDescrittori1;
	}

	public void setEnableDescrittori1(boolean enableDescrittori1) {
		this.enableDescrittori1 = enableDescrittori1;
	}

	public boolean isEnableDescrittori2() {
		return enableDescrittori2;
	}

	public void setEnableDescrittori2(boolean enableDescrittori2) {
		this.enableDescrittori2 = enableDescrittori2;
	}

	public boolean isEnableDescrittori3() {
		return enableDescrittori3;
	}

	public void setEnableDescrittori3(boolean enableDescrittori3) {
		this.enableDescrittori3 = enableDescrittori3;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public RicercaSoggettoListaVO getOutputDescrittori() {
		return outputDescrittori;
	}

	public void setOutputDescrittori(
			RicercaSoggettoListaVO outputDescrittori) {
		this.outputDescrittori = outputDescrittori;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public RicercaSoggettoDescrittoriVO getRicercaSoggettoDescrittore() {
		return ricercaSoggettoDescrittore;
	}

	public void setRicercaSoggettoDescrittore(
			RicercaSoggettoDescrittoriVO ricercaSoggettoDescrittore) {
		this.ricercaSoggettoDescrittore = ricercaSoggettoDescrittore;
	}

	public boolean isEnableCercaIndice() {
		return enableCercaIndice;
	}

	public void setEnableCercaIndice(boolean enableCercaIndice) {
		this.enableCercaIndice = enableCercaIndice;
	}

	public String getPrimoDid() {
		return primoDid;
	}

	public void setPrimoDid(String primoDid) {
		this.primoDid = primoDid;
	}

	public String getQuartoDid() {
		return quartoDid;
	}

	public void setQuartoDid(String quartoDid) {
		this.quartoDid = quartoDid;
	}

	public String getSecondoDid() {
		return secondoDid;
	}

	public void setSecondoDid(String secondoDid) {
		this.secondoDid = secondoDid;
	}

	public String getTerzoDid() {
		return terzoDid;
	}

	public void setTerzoDid(String terzoDid) {
		this.terzoDid = terzoDid;
	}

	public String getDescrittoriSogg4() {
		return descrittoriSogg4;
	}

	public void setDescrittoriSogg4(String descrittoriSogg4) {
		this.descrittoriSogg4 = descrittoriSogg4;
	}

	public String getQuintoDid() {
		return quintoDid;
	}

	public void setQuintoDid(String quintoDid) {
		this.quintoDid = quintoDid;
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
















