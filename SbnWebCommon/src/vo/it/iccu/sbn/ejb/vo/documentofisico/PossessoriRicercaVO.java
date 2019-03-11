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
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

public class PossessoriRicercaVO implements Serializable {

//	static final long serialVersionUID = InterrogazioneAutoreGeneraleVO.class.hashCode();


	/**
	 *
	 */
	private static final long serialVersionUID = -2488105567448166019L;

	private String nome;
	private String tipoRicerca;
	private String pid;
	private String note;
	private boolean chkTipoNomeA;
	private boolean chkTipoNomeB;
	private boolean chkTipoNomeC;
	private boolean chkTipoNomeD;
	private boolean chkTipoNomeE;
	private boolean chkTipoNomeR;
	private boolean chkTipoNomeG;
	private String formaAutore;
	private boolean soloBib;


	private int nRec;
	private int elemXBlocchi;
	private int numPrimo;
	private int maxRighe;
	private List listaTipiOrdinam;
	private String tipoOrdinamSelez;
	private List listaFormatoLista;
	private String formatoListaSelez;
	private int bloccoSelezionato=0;
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int elemPerBlocchi;
	private boolean abilitaBottoneCarBlocchi;


	String[] parole;
//	String[] tipoNome;
	boolean[] tipoNomeBoolean;

	public String[] getParole() {
		return parole;
	}

	public void setParole(String[] parole) {
		this.parole = parole;
	}


	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getFormaAutore() {
		return formaAutore;
	}

	public void setFormaAutore(String formaAutore) {
		this.formaAutore = formaAutore;
	}


//
//	public List getListaPaese() {
//		return listaPaese;
//	}
//
//	public void setListaPaese(List listaPaese) {
//		this.listaPaese = listaPaese;
//	}

	public List getListaTipiOrdinam() {
		return listaTipiOrdinam;
	}

	public void setListaTipiOrdinam(List listaTipiOrdinam) {
		this.listaTipiOrdinam = listaTipiOrdinam;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}




	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


	public boolean isChkTipoNomeA() {
		return chkTipoNomeA;
	}

	public void setChkTipoNomeA(boolean chkTipoNomeA) {
		this.chkTipoNomeA = chkTipoNomeA;
	}

	public boolean isChkTipoNomeB() {
		return chkTipoNomeB;
	}

	public void setChkTipoNomeB(boolean chkTipoNomeB) {
		this.chkTipoNomeB = chkTipoNomeB;
	}

	public boolean isChkTipoNomeC() {
		return chkTipoNomeC;
	}

	public void setChkTipoNomeC(boolean chkTipoNomeC) {
		this.chkTipoNomeC = chkTipoNomeC;
	}

	public boolean isChkTipoNomeD() {
		return chkTipoNomeD;
	}

	public void setChkTipoNomeD(boolean chkTipoNomeD) {
		this.chkTipoNomeD = chkTipoNomeD;
	}

	public boolean isChkTipoNomeE() {
		return chkTipoNomeE;
	}

	public void setChkTipoNomeE(boolean chkTipoNomeE) {
		this.chkTipoNomeE = chkTipoNomeE;
	}

	public boolean isChkTipoNomeG() {
		return chkTipoNomeG;
	}

	public void setChkTipoNomeG(boolean chkTipoNomeG) {
		this.chkTipoNomeG = chkTipoNomeG;
	}

	public boolean isChkTipoNomeR() {
		return chkTipoNomeR;
	}

	public void setChkTipoNomeR(boolean chkTipoNomeR) {
		this.chkTipoNomeR = chkTipoNomeR;
	}

	public void validaCanaliPrim() throws ValidationException {
		int combinazioni = 0;

		if (this.nome.length() > 0 ) combinazioni = combinazioni +1;
		if (this.pid.length()  > 0 ) combinazioni = combinazioni +1;


		if (combinazioni == 0)
			throw new ValidationException("noCanPrim", ValidationExceptionCodici.noCanPrim);
		if (combinazioni > 1)
			throw new ValidationException("soloUnCanPrim", ValidationExceptionCodici.soloUnCanPrim);

		if ((this.pid.length() > 0 && this.pid.length() < 10) || this.pid.length() > 10 )
			throw new ValidationException("pidErrato", ValidationExceptionCodici.pidErrato);


	}

	public void validaParametriGener() throws ValidationException {

		if (!this.getNome().equals("") && this.getTipoRicerca().equals("parole")) {
			StringTokenizer st = new StringTokenizer(this.getNome());

			if (st.countTokens() > 4) {
				// inserire diagnostico di troppe parole (al massimo 4)
				throw new ValidationException("ric021", ValidationExceptionCodici.livRicObblig);
			} else {
				// devono essere massimo 4 parole
				parole = new String[4];
				int      i = 0;
				while (st.hasMoreTokens()) {
					parole[i] = st.nextToken();
					i++;
				}
			}
		}
		// TIPO NOME

		int i = -1;
		int countTipoNome = 0;
		tipoNomeBoolean = new boolean[7];

		if (this.chkTipoNomeA){
			this.tipoNomeBoolean[++i] = true;
			countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeB){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeC){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeD){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeE){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeR) {
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeG){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}

		if (countTipoNome > 4 ) {
			// inserire diagnostico di troppi tipi nome (al massimo 4)
			throw new ValidationException("ric022", ValidationExceptionCodici.livRicObblig);
		}
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public boolean[] getTipoNomeBoolean() {
		return tipoNomeBoolean;
	}

	public void setTipoNomeBoolean(boolean[] tipoNomeBoolean) {
		this.tipoNomeBoolean = tipoNomeBoolean;
	}

	public String getFormatoListaSelez() {
		return formatoListaSelez;
	}

	public void setFormatoListaSelez(String formatoListaSelez) {
		this.formatoListaSelez = formatoListaSelez;
	}

	public List getListaFormatoLista() {
		return listaFormatoLista;
	}

	public void setListaFormatoLista(List listaFormatoLista) {
		this.listaFormatoLista = listaFormatoLista;
	}


	public void save() {}

	public void restore(){}

	public boolean isSoloBib() {
		return soloBib;
	}

	public void setSoloBib(boolean soloBib) {
		this.soloBib = soloBib;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public boolean isAbilitaBottoneCarBlocchi() {
		return abilitaBottoneCarBlocchi;
	}

	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
	}

	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}

	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
