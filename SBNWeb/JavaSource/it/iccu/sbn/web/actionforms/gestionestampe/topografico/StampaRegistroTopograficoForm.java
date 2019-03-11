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
package it.iccu.sbn.web.actionforms.gestionestampe.topografico;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class StampaRegistroTopograficoForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = -1644424871922778621L;
	private String codBib;
	private String sezione;
	private String noSezione;
	private String dallaCollocazione;
	private String dallaSpecificazione;
	private String allaCollocazione;
	private String allaSpecificazione;
	private boolean sessione;
	private boolean disableSez;
	private boolean disableColl1;
	private boolean disableSpec1;
	private boolean disableColl2;
	private boolean disableSpec2;
	private boolean disableTastoSez;
	private boolean disableTastoColl;
	private boolean disableTastoSpec;
//	private String codLoc;
//	private String codSpec;
	private String codPolo;
	private String codSez;
	private String codPoloSez;
	private String codBibSez;
	private String test;//hidden
	private List listaTipiOrdinamento;
	private String descrBib;
	private String ticket;
	private String tipoModello;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private List listaBiblioteche;

	public List getListaBiblioteche() {
		return listaBiblioteche;
	}


	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}


	public int getNRec() {
		return nRec;
	}


	public void setNRec(int rec) {
		nRec = rec;
	}



	public String getSezione() {
		return sezione;
	}


	public void setSezione(String sezione) {
		this.sezione = sezione;
	}



	public String getTipoFormato() {
		return tipoFormato;
	}


	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}


	public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if(this.getTipoFormato() == null)
        {

                errors.add("tipoFormato", new ActionMessage(
                        "campo.obbligatorio", "formato"));

        }
        return errors;
    }


	public String getCodBib() {
		return codBib;
	}


	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isDisableSez() {
		return disableSez;
	}
	public void setDisableSez(boolean disableSez) {
		this.disableSez = disableSez;
	}

	public boolean isDisableTastoColl() {
		return disableTastoColl;
	}


	public void setDisableTastoColl(boolean disableTastoColl) {
		this.disableTastoColl = disableTastoColl;
	}


	public boolean isDisableTastoSez() {
		return disableTastoSez;
	}


	public void setDisableTastoSez(boolean disableTastoSez) {
		this.disableTastoSez = disableTastoSez;
	}


	public boolean isDisableTastoSpec() {
		return disableTastoSpec;
	}


	public void setDisableTastoSpec(boolean disableTastoSpec) {
		this.disableTastoSpec = disableTastoSpec;
	}


//	public String getCodLoc() {
//		return codLoc;
//	}
//
//
//	public void setCodLoc(String codLoc) {
//		this.codLoc = codLoc;
//	}
//
//
//	public String getCodSpec() {
//		return codSpec;
//	}
//
//
//	public void setCodSpec(String codSpec) {
//		this.codSpec = codSpec;
//	}


	public String getCodPolo() {
		return codPolo;
	}


	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}


	public String getCodSez() {
		return codSez;
	}


	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}


	public String getCodBibSez() {
		return codBibSez;
	}


	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}


	public String getCodPoloSez() {
		return codPoloSez;
	}


	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}


	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}


	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}


	public String getNoSezione() {
		return noSezione;
	}


	public void setNoSezione(String noSezione) {
		this.noSezione = noSezione;
	}


	public String getTest() {
		return test;
	}


	public void setTest(String test) {
		this.test = test;
	}


	public String getDescrBib() {
		return descrBib;
	}


	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}


	public String getTicket() {
		return ticket;
	}


	public void setTicket(String ticket) {
		this.ticket = ticket;
	}


	public boolean isDisableColl1() {
		return disableColl1;
	}


	public void setDisableColl1(boolean disableColl1) {
		this.disableColl1 = disableColl1;
	}


	public boolean isDisableColl2() {
		return disableColl2;
	}


	public void setDisableColl2(boolean disableColl2) {
		this.disableColl2 = disableColl2;
	}


	public boolean isDisableSpec1() {
		return disableSpec1;
	}


	public void setDisableSpec1(boolean disableSpec1) {
		this.disableSpec1 = disableSpec1;
	}


	public boolean isDisableSpec2() {
		return disableSpec2;
	}


	public void setDisableSpec2(boolean disableSpec2) {
		this.disableSpec2 = disableSpec2;
	}


	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}


	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}


	public String getTipoModello() {
		return tipoModello;
	}


	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}


	public String getDallaCollocazione() {
		return dallaCollocazione;
	}


	public void setDallaCollocazione(String dallaCollocazione) {
		this.dallaCollocazione = dallaCollocazione;
	}


	public String getDallaSpecificazione() {
		return dallaSpecificazione;
	}


	public void setDallaSpecificazione(String dallaSpecificazione) {
		this.dallaSpecificazione = dallaSpecificazione;
	}


	public String getAllaCollocazione() {
		return allaCollocazione;
	}


	public void setAllaCollocazione(String allaCollocazione) {
		this.allaCollocazione = allaCollocazione;
	}


	public String getAllaSpecificazione() {
		return allaSpecificazione;
	}


	public void setAllaSpecificazione(String allaSpecificazione) {
		this.allaSpecificazione = allaSpecificazione;
	}
}
