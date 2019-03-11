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
package it.iccu.sbn.ejb.vo.servizi.serviziweb;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;

import java.sql.Timestamp;
import java.util.List;

public class UtenteWeb extends BaseVO {

	private static final long serialVersionUID = -2205200091854266833L;

	private String codPolo;
	private String codBib;
	private String userId;
	private Integer idUtente;
	//Modifica almaviva 2009
	private String password;
	private String postaElettronica;
	private String postaElettronica2;
	private String codFiscale;
	private String cognome;
	private String nome;
	private boolean remoto = false;

	//
	private Timestamp lastAccess;
	private char changePassword;
	private Timestamp tsVarPassword;
	//
	private List<BibliotecaVO> listaBiblio;
	private List<BibliotecaVO> listabibDest;
	private List<DocumentoNonSbnVO> listaTipoDoc;
	private List<ListaServiziVO> modErogazione;
	private List<ListaServiziVO> supporto;
	private List<ListaServiziVO> listaServ;
	private List<ListaServiziVO> listaServILL;

	private boolean scaduto;
	private boolean SIP2;

	private String tipoUtente;

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public List<ListaServiziVO> getListaServILL() {
		return listaServILL;
	}

	public void setListaServILL(List<ListaServiziVO> listaServILL) {
		this.listaServILL = listaServILL;
	}

	public List<ListaServiziVO> getListaServ() {
		return listaServ;
	}

	public void setListaServ(List<ListaServiziVO> listaServ) {
		this.listaServ = listaServ;
	}

	public List<ListaServiziVO> getSupporto() {
		return supporto;
	}

	public void setSupporto(List<ListaServiziVO> supporto) {
		this.supporto = supporto;
	}

	public List<ListaServiziVO> getModErogazione() {
		return modErogazione;
	}

	public void setModErogazione(List<ListaServiziVO> modErogazione) {
		this.modErogazione = modErogazione;
	}

	public List<DocumentoNonSbnVO> getListaTipoDoc() {
		return listaTipoDoc;
	}

	public void setListaTipoDoc(List<DocumentoNonSbnVO> listaTipoDoc) {
		this.listaTipoDoc = listaTipoDoc;
	}

	public List<BibliotecaVO> getListabibDest() {
		return listabibDest;
	}

	public void setListabibDest(List<BibliotecaVO> listabibDest) {
		this.listabibDest = listabibDest;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = trimAndSet(userId);
	}

	public List<BibliotecaVO> getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List<BibliotecaVO> listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

	public void setLastAccess(Timestamp lastAccess) {
		this.lastAccess = lastAccess;
	}

	public Timestamp getLastAccess() {
		return lastAccess;
	}

	public void setChangePassword(char changePassword) {
		this.changePassword = changePassword;
	}

	public char getChangePassword() {
		return changePassword;
	}

	public void setTsVarPassword(Timestamp tsVarPassword) {
		this.tsVarPassword = tsVarPassword;
	}

	public Timestamp getTsVarPassword() {
		return tsVarPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getPostaElettronica() {
		return postaElettronica;
	}

	public void setPostaElettronica(String postaElettronica) {
		this.postaElettronica = trimAndSet(postaElettronica);
	}

	public String getPostaElettronica2() {
		return postaElettronica2;
	}

	public void setPostaElettronica2(String postaElettronica2) {
		this.postaElettronica2 = trimAndSet(postaElettronica2);
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = trimAndSet(codFiscale);
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCognome(String cognome) {
		this.cognome = trimAndSet(cognome);
	}

	public String getCognome() {
		return cognome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public String getNome() {
		return nome;
	}

	public boolean isRemoto() {
		return remoto;
	}

	public void setRemoto(boolean remoto) {
		this.remoto = remoto;
	}

	public boolean isScaduto() {
		return scaduto;
	}

	public void setScaduto(boolean scaduto) {
		this.scaduto = scaduto;
	}

	public boolean isCompleto() {
		return isFilled(codFiscale) && (isFilled(postaElettronica) || isFilled(postaElettronica2));
	}

	public boolean isSIP2() {
		return SIP2;
	}

	public void setSIP2(boolean isSIP2) {
		this.SIP2 = isSIP2;
	}

	public boolean isCancellato() {
		return ValidazioneDati.in(flCanc, "s", "S");
	}

	public String getCognomeNome() {
		return trimOrEmpty(cognome + " " + nome);
	}

	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

}
