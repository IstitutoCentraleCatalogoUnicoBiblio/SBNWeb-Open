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
package it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite;

import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

public class ScaricoInventarialeForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = 7851314326431023638L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private List listaBiblio;
	private int versoBiblioteca;
	private String versoBibliotecaDescr;
	private String polo;
	private String motivoDelloScarico;
	private List listaMotivoScarico;
	private int numBuonoScarico;
	private String dataScarico;
	private String dataDelibera;
	private String testoDelibera;
	private String ticket;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private boolean sessione;
	private boolean disable;
	private boolean noSerie;
	private boolean trasferimento;
//	private List listaSerie = new ArrayList();
//	private List listaComboSerie = new ArrayList();
//	//Ricerca per range di inventari
//	private String serie;
//	private String endInventario;
//	private String startInventario;

	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public List getListaMotivoScarico() {
		return listaMotivoScarico;
	}
	public void setListaMotivoScarico(List listaMotivoScarico) {
		this.listaMotivoScarico = listaMotivoScarico;
	}
	public String getMotivoDelloScarico() {
		return motivoDelloScarico;
	}
	public void setMotivoDelloScarico(String motivoDelloScarico) {
		this.motivoDelloScarico = motivoDelloScarico;
	}
	public int getNumBuonoScarico() {
		return numBuonoScarico;
	}
	public void setNumBuonoScarico(int numBuonoScarico) {
		this.numBuonoScarico = numBuonoScarico;
	}
	public String getPolo() {
		return polo;
	}
	public void setPolo(String polo) {
		this.polo = polo;
	}
	public String getTestoDelibera() {
		return testoDelibera;
	}
	public void setTestoDelibera(String testoDelibera) {
		this.testoDelibera = testoDelibera;
	}
	public List getListaBiblio() {
		return listaBiblio;
	}
	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}
	public String getDataDelibera() {
		return dataDelibera;
	}
	public void setDataDelibera(String dataDelibera) {
		this.dataDelibera = dataDelibera;
	}
	public String getDataScarico() {
		return dataScarico;
	}
	public void setDataScarico(String dataScarico) {
		this.dataScarico = dataScarico;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isNoSerie() {
		return noSerie;
	}
	public void setNoSerie(boolean noSerie) {
		this.noSerie = noSerie;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getEndInventario() {
		return endInventario;
	}
	public void setEndInventario(String endInventario) {
		this.endInventario = endInventario;
	}
	public String getStartInventario() {
		return startInventario;
	}
	public void setStartInventario(String startInventario) {
		this.startInventario = startInventario;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public String getVersoBibliotecaDescr() {
		return versoBibliotecaDescr;
	}
	public void setVersoBibliotecaDescr(String versoBibliotecaDescr) {
		this.versoBibliotecaDescr = versoBibliotecaDescr;
	}
	public int getVersoBiblioteca() {
		return versoBiblioteca;
	}
	public void setVersoBiblioteca(int versoBiblioteca) {
		this.versoBiblioteca = versoBiblioteca;
	}
	public boolean isTrasferimento() {
		return trasferimento;
	}
	public void setTrasferimento(boolean trasferimento) {
		this.trasferimento = trasferimento;
	}

}
