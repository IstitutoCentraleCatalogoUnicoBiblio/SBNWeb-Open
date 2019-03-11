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
package it.iccu.sbn.ejb.vo.documentofisico;

public class InventarioDettaglioVO extends InventarioVO {

	private static final long serialVersionUID = 6607650445503838867L;

	private String titNatura;
	private String titIsbd;
	private String fattFornitore;
	private String fattNumFattura;
	private String fattDataFattura;
	private String descrProven;
	private String numFattura;
	private String dataFattura;
	private String descrFornitore;
	private int collKeyLoc;
	private String collCodLoc;
	private String collSpecLoc;
	private String collBidLoc;
	private String collConsis;
	private String collCodPoloDoc;
	private String collCodBibDoc;
	private String collCodDoc;
	private String collBidDoc;
	private String collCodPoloSez;
	private String collCodBibSez;
	private String collCodSez;
	private String collCodSistema;
	private String collCodEdizione;
	private String collClasse;
	private String collIndice;
	private String titIsbdTrattato;

	private Character titTipoRecord;

	public InventarioDettaglioVO(InventarioVO inv) throws Exception {
		super(inv);
		this.titNatura = "";
		this.titIsbd = "";
		this.fattFornitore = "";
		this.fattNumFattura = "";
		this.fattDataFattura = "";
		this.descrProven = "";
		this.numFattura = "";
		this.dataFattura = "";
		this.descrFornitore = "";
		this.collKeyLoc = 0;
		this.collCodLoc = "";
		this.collSpecLoc = "";
		this.collBidLoc = "";
		this.collConsis = "";
		this.collCodPoloDoc = "";
		this.collCodBibDoc = "";
		this.collCodDoc = "";
		this.collBidDoc = "";
		this.collCodPoloSez = "";
		this.collCodBibSez = "";
		this.collCodSez = "";
		this.collCodSistema = "";
		this.collCodEdizione = "";
		this.collClasse = "";
		this.collIndice = "";
		this.titIsbdTrattato = "";
	}

	public InventarioDettaglioVO(/* InventarioVO inv, */ String titNatura, String titIsbd, String fattFornitore,
			String fattNumFattura, String fattDataFattura, String descrProven, String numFattura, String dataFattura,
			String descrFornitore, int collKeyLoc, String collCodLoc, String collSpecLoc, String collBidLoc,
			String collConsis, String collCodPoloDoc, String collCodBibDoc, String collCodDoc, String collBidDoc,
			String collCodPoloSez, String collCodBibSez, String collCodSez, String collCodSistema,
			String collCodEdizione, String collClasse, String collIndice, String titIsbdTrattato) throws Exception {
		// super(inv);
		super();
		this.titNatura = titNatura;
		this.titIsbd = titIsbd;
		this.fattFornitore = fattFornitore;
		this.fattNumFattura = fattNumFattura;
		this.fattDataFattura = fattDataFattura;
		this.descrProven = descrProven;
		this.numFattura = numFattura;
		this.dataFattura = dataFattura;
		this.descrFornitore = descrFornitore;
		this.collKeyLoc = collKeyLoc;
		this.collCodLoc = collCodLoc;
		this.collSpecLoc = collSpecLoc;
		this.collBidLoc = collBidLoc;
		this.collConsis = collConsis;
		this.collCodPoloDoc = collCodPoloDoc;
		this.collCodBibDoc = collCodBibDoc;
		this.collCodDoc = collCodDoc;
		this.collBidDoc = collBidDoc;
		this.collCodPoloSez = collCodPoloSez;
		this.collCodBibSez = collCodBibSez;
		this.collCodSez = collCodSez;
		this.collCodSistema = collCodSistema;
		this.collCodEdizione = collCodEdizione;
		this.collClasse = collClasse;
		this.collIndice = collIndice;
		this.titIsbdTrattato = titIsbdTrattato;
	}

	public String getCollCodLoc() {
		return collCodLoc;
	}

	public void setCollCodLoc(String collCodLoc) {
		this.collCodLoc = collCodLoc;
	}

	public String getCollCodSez() {
		return collCodSez;
	}

	public void setCollCodSez(String collCodSez) {
		this.collCodSez = collCodSez;
	}

	public String getCollConsis() {
		return collConsis;
	}

	public void setCollConsis(String collConsis) {
		this.collConsis = collConsis;
	}

	public String getCollSpecLoc() {
		return collSpecLoc;
	}

	public void setCollSpecLoc(String collSpecLoc) {
		this.collSpecLoc = collSpecLoc;
	}

	public String getFattDataFattura() {
		return fattDataFattura;
	}

	public void setFattDataFattura(String fattDataFattura) {
		this.fattDataFattura = fattDataFattura;
	}

	public String getFattFornitore() {
		return fattFornitore;
	}

	public void setFattFornitore(String fattFornitore) {
		this.fattFornitore = fattFornitore;
	}

	public String getFattNumFattura() {
		return fattNumFattura;
	}

	public void setFattNumFattura(String fattNumFattura) {
		this.fattNumFattura = fattNumFattura;
	}

	public String getTitIsbd() {
		return titIsbd;
	}

	public void setTitIsbd(String titIsbd) {
		this.titIsbd = titIsbd;
	}

	public String getTitNatura() {
		return titNatura;
	}

	public void setTitNatura(String titNatura) {
		this.titNatura = titNatura;
	}

	public String getCollBidDoc() {
		return collBidDoc;
	}

	public void setCollBidDoc(String collBidDoc) {
		this.collBidDoc = collBidDoc;
	}

	public String getCollBidLoc() {
		return collBidLoc;
	}

	public void setCollBidLoc(String collBidLoc) {
		this.collBidLoc = collBidLoc;
	}

	public String getCollCodPoloSez() {
		return collCodPoloSez;
	}

	public void setCollCodPoloSez(String collCodPoloSez) {
		this.collCodPoloSez = collCodPoloSez;
	}

	public String getCollCodBibDoc() {
		return collCodBibDoc;
	}

	public void setCollCodBibDoc(String collCodBibDoc) {
		this.collCodBibDoc = collCodBibDoc;
	}

	public String getCollCodDoc() {
		return collCodDoc;
	}

	public void setCollCodDoc(String collCodDoc) {
		this.collCodDoc = collCodDoc;
	}

	public String getCollIndice() {
		return collIndice;
	}

	public void setCollIndice(String collIndice) {
		this.collIndice = collIndice;
	}

	public int getCollKeyLoc() {
		return collKeyLoc;
	}

	public void setCollKeyLoc(int collKeyLoc) {
		this.collKeyLoc = collKeyLoc;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}

	public String getDescrFornitore() {
		return descrFornitore;
	}

	public void setDescrFornitore(String descrFornitore) {
		this.descrFornitore = descrFornitore;
	}

	public String getNumFattura() {
		return numFattura;
	}

	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}

	public String getDescrProven() {
		return descrProven;
	}

	public void setDescrProven(String descrProven) {
		this.descrProven = descrProven;
	}

	public String getCollClasse() {
		return collClasse;
	}

	public void setCollClasse(String collClasse) {
		this.collClasse = collClasse;
	}

	public String getCollCodBibSez() {
		return collCodBibSez;
	}

	public void setCollCodBibSez(String collCodBibSez) {
		this.collCodBibSez = collCodBibSez;
	}

	public String getCollCodEdizione() {
		return collCodEdizione;
	}

	public void setCollCodEdizione(String collCodEdizione) {
		this.collCodEdizione = collCodEdizione;
	}

	public String getCollCodPoloDoc() {
		return collCodPoloDoc;
	}

	public void setCollCodPoloDoc(String collCodPoloDoc) {
		this.collCodPoloDoc = collCodPoloDoc;
	}

	public String getCollCodSistema() {
		return collCodSistema;
	}

	public void setCollCodSistema(String collCodSistema) {
		this.collCodSistema = collCodSistema;
	}

	public InventarioDettaglioVO() {
	}

	public String getTitIsbdTrattato() {
		return titIsbdTrattato;
	}

	public void setTitIsbdTrattato(String titIsbdTrattato) {
		this.titIsbdTrattato = titIsbdTrattato;
	}

	public Character getTitTipoRecord() {
		return titTipoRecord;
	}

	public void setTitTipoRecord(Character tipoRecord) {
		this.titTipoRecord = tipoRecord;
	}

}
