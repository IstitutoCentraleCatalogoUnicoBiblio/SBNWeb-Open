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
package it.iccu.sbn.web.actionforms.gestionestampe.etichette;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

public class StampaEtichetteForm  extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = -6865069556351298470L;
	//campi di form
	private String codBib;
	private String codPolo;
	private String biblioteca;
	private String descrBib;
	private String descrBibEtichetta;
	private List listaComboSerie = new ArrayList();
	private List listaSerie;
	private List listaBiblioteche;
	private boolean noSerie;
	private String ticket;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String codBibliotec;
	private String nomeBibliotec;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private ModelloStampaVO modello;
	private boolean disableModBarCode;
	private boolean disableModConfig;
	private boolean modBarCode = false;
	private boolean modConfig = false;
	private boolean sessione = false;


	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public ModelloStampaVO getModello() {
		return modello;
	}

	public void setModello(ModelloStampaVO modello) {
		this.modello = modello;
	}

	public String getNomeBibliotec() {
		return nomeBibliotec;
	}

	public void setNomeBibliotec(String nomeBibliotec) {
		this.nomeBibliotec = nomeBibliotec;
	}

	private String dataInizio;
	private String dataFine;

	public int getnRec() {
		return nRec;
	}

	public void setnRec(int nRec) {
		this.nRec = nRec;
	}

	public String getCodBibliotec() {
		return codBibliotec;
	}

	public void setCodBibliotec(String codBibliotec) {
		this.codBibliotec = codBibliotec;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	//codice del modello da usare per la stampa
	protected String codModello;

	public StampaEtichetteForm() throws Exception {
		super();
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodModello() {
		return codModello;
	}

	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}


	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public List getListaComboSerie() {
		return listaComboSerie;
	}

	public void setListaComboSerie(List listaComboSerie) {
		this.listaComboSerie = listaComboSerie;
	}

	public List getListaSerie() {
		return listaSerie;
	}

	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public boolean isNoSerie() {
		return noSerie;
	}

	public void setNoSerie(boolean noSerie) {
		this.noSerie = noSerie;
	}
//	public String getTipoOperazione() {
//		// gv tipoOperazione
//		// S ... selezione intervallo di collocazione
//		// I ... selezione per intervallo di inventario
//		// N ... elenco di inventari
//		// impostarlo in funzione dei campi selezionati dall'utente:
//		boolean isS=this.isSelected("S");
//		boolean isI=this.isSelected("I");
//		boolean isN=this.isSelected("N");
//		// se è impostato qualcosa in una delle tre aree principali
//		// torna il valore di quell'area
//		if(isS && !isI && !isN) return "S";
//		if(!isS && isI && !isN) return "I";
//		if(!isS && !isI && isN) return "N";
//		// se non ha toccato nulla torna S
//		if(!isS && !isI && !isN) return "S";
//		//se è stata toccata più di un'area torno null
//		return null;
//	}
//
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

	public String getDescrBibEtichetta() {
		return descrBibEtichetta;
	}

	public void setDescrBibEtichetta(String descrBibEtichetta) {
		this.descrBibEtichetta = descrBibEtichetta;
	}

	public boolean isDisableModBarCode() {
		return disableModBarCode;
	}

	public void setDisableModBarCode(boolean disableModBarCode) {
		this.disableModBarCode = disableModBarCode;
	}

	public boolean isDisableModConfig() {
		return disableModConfig;
	}

	public void setDisableModConfig(boolean disableModConfig) {
		this.disableModConfig = disableModConfig;
	}

	public boolean isModBarCode() {
		return modBarCode;
	}

	public void setModBarCode(boolean modBarCode) {
		this.modBarCode = modBarCode;
	}

	public boolean isModConfig() {
		return modConfig;
	}

	public void setModConfig(boolean modConfig) {
		this.modConfig = modConfig;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

//	private boolean isSelected(String tipoOperazione){
//		if(tipoOperazione.equalsIgnoreCase("S")){
//			if(!(getSezione().equals(""))) return true;
//			if(!(dallaCollocazione.equals(""))) return true;
//			if(!(dallaSpecificazione.equals(""))) return true;
//			if(!(allaCollocazione.equals(""))) return true;
//			if(!(allaSpecificazione.equals(""))) return true;
//		} else if(tipoOperazione.equalsIgnoreCase("I")){
//			if(!(serie.equals(""))) return true;
//			if(!(endInventario.equals(""))) return true;
//			if(!(startInventario.equals(""))) return true;
//		} else if(tipoOperazione.equalsIgnoreCase("N")){
//			if(!(numero01.equals(""))) return true;
//			if(!(numero02.equals(""))) return true;
//			if(!(numero03.equals(""))) return true;
//			if(!(numero04.equals(""))) return true;
//			if(!(numero05.equals(""))) return true;
//			if(!(numero06.equals(""))) return true;
//			if(!(numero07.equals(""))) return true;
//			if(!(numero08.equals(""))) return true;
//			if(!(numero09.equals(""))) return true;
//			if(!(numero10.equals(""))) return true;
//			if(!(numero11.equals(""))) return true;
//			if(!(numero12.equals(""))) return true;
//			if(!(numero13.equals(""))) return true;
//			if(!(numero14.equals(""))) return true;
//			if(!(numero15.equals(""))) return true;
//			if(!(numero16.equals(""))) return true;
//			if(!(numero17.equals(""))) return true;
//			if(!(numero18.equals(""))) return true;
//			if(!(numero19.equals(""))) return true;
//			if(!(numero20.equals(""))) return true;
//			if(!(numero21.equals(""))) return true;
//			if(!(numero22.equals(""))) return true;
//			if(!(numero23.equals(""))) return true;
//			if(!(numero24.equals(""))) return true;
//			if(!(numero25.equals(""))) return true;
//			if(!(numero26.equals(""))) return true;
//			if(!(numero27.equals(""))) return true;
//			if(!(numero28.equals(""))) return true;
//			if(!(numero29.equals(""))) return true;
//			if(!(numero30.equals(""))) return true;
//			if(!(numero31.equals(""))) return true;
//			if(!(numero32.equals(""))) return true;
//			if(!(numero33.equals(""))) return true;
//			if(!(numero34.equals(""))) return true;
//			if(!(numero35.equals(""))) return true;
//			if(!(numero36.equals(""))) return true;
//			if(!(serie01.equals("   "))) return true;
//			if(!(serie02.equals("   "))) return true;
//			if(!(serie03.equals("   "))) return true;
//			if(!(serie04.equals("   "))) return true;
//			if(!(serie05.equals("   "))) return true;
//			if(!(serie06.equals("   "))) return true;
//			if(!(serie07.equals("   "))) return true;
//			if(!(serie08.equals("   "))) return true;
//			if(!(serie09.equals("   "))) return true;
//			if(!(serie10.equals("   "))) return true;
//			if(!(serie11.equals("   "))) return true;
//			if(!(serie12.equals("   "))) return true;
//			if(!(serie13.equals("   "))) return true;
//			if(!(serie14.equals("   "))) return true;
//			if(!(serie15.equals("   "))) return true;
//			if(!(serie16.equals("   "))) return true;
//			if(!(serie17.equals("   "))) return true;
//			if(!(serie18.equals("   "))) return true;
//			if(!(serie19.equals("   "))) return true;
//			if(!(serie20.equals("   "))) return true;
//			if(!(serie21.equals("   "))) return true;
//			if(!(serie22.equals("   "))) return true;
//			if(!(serie23.equals("   "))) return true;
//			if(!(serie24.equals("   "))) return true;
//			if(!(serie25.equals("   "))) return true;
//			if(!(serie26.equals("   "))) return true;
//			if(!(serie27.equals("   "))) return true;
//			if(!(serie28.equals("   "))) return true;
//			if(!(serie29.equals("   "))) return true;
//			if(!(serie30.equals("   "))) return true;
//			if(!(serie31.equals("   "))) return true;
//			if(!(serie32.equals("   "))) return true;
//			if(!(serie33.equals("   "))) return true;
//			if(!(serie34.equals("   "))) return true;
//			if(!(serie35.equals("   "))) return true;
//			if(!(serie36.equals("   "))) return true;
//		}
//		return false;
//	}

}
