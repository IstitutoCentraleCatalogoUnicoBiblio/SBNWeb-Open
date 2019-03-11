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
//	SBNWeb - Rifacimento ClientServer
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class InterrogazioneVaiAForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -2725209238589753967L;
	private String bidTitRifer;
	private String descBidTitRifer;

	private List listaVaiAAcquis;
	private String vaiAAcquisSelez;

	private List listaVaiACatalSemant;
	private String vaiACatalSemantSelez;

	private List listaVaiAGestDocFis;
	private String vaiAGestDocFisSelez;

	private List listaVaiAGestBibliog;
	private String vaiAGestBibliogSelez;

	private List listaVaiACatalUnimarc;
	private String vaiACatalUnimarcSelez;

	private List listaVaiAGestPossessori;
	private String vaiAGestPossessoriSelez;


	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
	private List listaVaiAGestPeriodici;
	private String vaiAGestPeriodiciSelez;
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici

	public String getBidTitRifer() {
		return bidTitRifer;
	}

	public void setBidTitRifer(String bidTitRifer) {
		this.bidTitRifer = bidTitRifer;
	}

	public String getDescBidTitRifer() {
		return descBidTitRifer;
	}

	public void setDescBidTitRifer(String descBidTitRifer) {
		this.descBidTitRifer = descBidTitRifer;
	}

	public List getListaVaiAAcquis() {
		return listaVaiAAcquis;
	}

	public void setListaVaiAAcquis(List listaVaiAAcquis) {
//		if (listaVaiAAcquis == null) {
//			this.listaVaiAAcquis = new ArrayList();
//			this.listaVaiAAcquis.add(new ComboCodDescVO("", ""));
//		} else {
			this.listaVaiAAcquis = listaVaiAAcquis;
//		}
	}

	public List getListaVaiACatalSemant() {
		return listaVaiACatalSemant;
	}

	public void setListaVaiACatalSemant(List listaVaiACatalSemant) {
//		if (listaVaiACatalSemant == null) {
//			this.listaVaiACatalSemant = new ArrayList();
//			this.listaVaiACatalSemant.add(new ComboCodDescVO("", ""));
//		} else {
			this.listaVaiACatalSemant = listaVaiACatalSemant;
//		}
	}

	public List getListaVaiACatalUnimarc() {
		return listaVaiACatalUnimarc;
	}

	public void setListaVaiACatalUnimarc(List listaVaiACatalUnimarc) {
//		if (listaVaiACatalUnimarc == null) {
//			this.listaVaiACatalUnimarc = new ArrayList();
//			this.listaVaiACatalUnimarc.add(new ComboCodDescVO("", ""));
//		} else {
			this.listaVaiACatalUnimarc = listaVaiACatalUnimarc;
//		}
	}

	public List getListaVaiAGestBibliog() {
		return listaVaiAGestBibliog;
	}

	public void setListaVaiAGestBibliog(List listaVaiAGestBibliog) {
		if (listaVaiAGestBibliog == null) {
			this.listaVaiAGestBibliog = new ArrayList();
			this.listaVaiAGestBibliog.add(new ComboCodDescVO("", ""));
		} else {
			this.listaVaiAGestBibliog = listaVaiAGestBibliog;
		}
	}

	// Inizio modifica almaviva2 2010.10.27 intervento interno;
	// nel caso dei Possessori la tendina di gestione Bibliografica non si deve vedere;
	public void initNullListaVaiAGestBibliog() {
		this.listaVaiAGestBibliog = null;
	}


	public List getListaVaiAGestDocFis() {
		return listaVaiAGestDocFis;
	}

	public void setListaVaiAGestDocFis(List listaVaiAGestDocFis) {
//		if (listaVaiAGestDocFis == null) {
//			this.listaVaiAGestDocFis = new ArrayList();
//			this.listaVaiAGestDocFis.add(new ComboCodDescVO("", ""));
//		} else {
			this.listaVaiAGestDocFis = listaVaiAGestDocFis;
//		}
	}

	public String getVaiAAcquisSelez() {
		return vaiAAcquisSelez;
	}

	public void setVaiAAcquisSelez(String vaiAAcquisSelez) {
		this.vaiAAcquisSelez = vaiAAcquisSelez;
	}

	public String getVaiACatalSemantSelez() {
		return vaiACatalSemantSelez;
	}

	public void setVaiACatalSemantSelez(String vaiACatalSemantSelez) {
		this.vaiACatalSemantSelez = vaiACatalSemantSelez;
	}

	public String getVaiACatalUnimarcSelez() {
		return vaiACatalUnimarcSelez;
	}

	public void setVaiACatalUnimarcSelez(String vaiACatalUnimarcSelez) {
		this.vaiACatalUnimarcSelez = vaiACatalUnimarcSelez;
	}

	public String getVaiAGestBibliogSelez() {
		return vaiAGestBibliogSelez;
	}

	public void setVaiAGestBibliogSelez(String vaiAGestBibliogSelez) {
		this.vaiAGestBibliogSelez = vaiAGestBibliogSelez;
	}

	public String getVaiAGestDocFisSelez() {
		return vaiAGestDocFisSelez;
	}

	public void setVaiAGestDocFisSelez(String vaiAGestDocFisSelez) {
		this.vaiAGestDocFisSelez = vaiAGestDocFisSelez;
	}

	public List getListaVaiAGestPossessori() {
		return listaVaiAGestPossessori;
	}

	public void setListaVaiAGestPossessori(List listaVaiAGestPossessori) {
//		if (listaVaiAGestPossessori == null) {
//			this.listaVaiAGestPossessori = new ArrayList();
//			this.listaVaiAGestPossessori.add(new ComboCodDescVO("", ""));
//		} else {
			this.listaVaiAGestPossessori = listaVaiAGestPossessori;
//		}
	}

	public String getVaiAGestPossessoriSelez() {
		return vaiAGestPossessoriSelez;
	}

	public void setVaiAGestPossessoriSelez(String vaiAGestPossessoriSelez) {
		this.vaiAGestPossessoriSelez = vaiAGestPossessoriSelez;
	}

	public List getListaVaiAGestPeriodici() {
		return listaVaiAGestPeriodici;
	}

	public void setListaVaiAGestPeriodici(List listaVaiAGestPeriodici) {
		this.listaVaiAGestPeriodici = listaVaiAGestPeriodici;
	}

	public String getVaiAGestPeriodiciSelez() {
		return vaiAGestPeriodiciSelez;
	}

	public void setVaiAGestPeriodiciSelez(String vaiAGestPeriodiciSelez) {
		this.vaiAGestPeriodiciSelez = vaiAGestPeriodiciSelez;
	}

}
