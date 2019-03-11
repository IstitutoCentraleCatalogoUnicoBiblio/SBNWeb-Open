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
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;
import java.util.StringTokenizer;

public class PossessoriDettaglioVO extends SerializableVO {

//	static final long serialVersionUID = InterrogazioneAutoreGeneraleVO.class.hashCode();



	/**
	 *
	 */
	private static final long serialVersionUID = -6533145543736212654L;

	private String tipoRicerca;

	private String pid;
	private String tipoTitolo ;
	private String forma;
	private String livello;
	private String nome;
	private String nota;
	private String dataIns;
	private String dataVar;

	private List listaTipoTitolo;
	private List listalivAutority;



	private int nRec;
	private int elemXBlocchi;
	private int numPrimo;
	private int maxRighe;
	private List listaFormatoLista;
	private String formatoListaSelez;
	private int bloccoSelezionato=0;
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int elemPerBlocchi;

	String[] parole;
//	String[] tipoNome;
	boolean[] tipoNomeBoolean;

	// almaviva2 Aprile 2015: campi nuovi per gestire la fusione fra Possessori in uscita da una richiesta di
	// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)
	private String pidArrivoDiFusione;



	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


	public void validaCanaliPrim() throws ValidationException {
		int combinazioni = 0;

		if (this.nome.length() > 0 ) combinazioni = combinazioni +1;

		if (combinazioni == 0)
			throw new ValidationException("noCanPrim", ValidationExceptionCodici.noCanPrim);
		if (combinazioni > 1)
			throw new ValidationException("soloUnCanPrim", ValidationExceptionCodici.soloUnCanPrim);
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

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getLivello() {
		return livello;
	}

	public void setLivello(String livello) {
		this.livello = livello;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public List getListaTipoTitolo() {
		return listaTipoTitolo;
	}

	public void setListaTipoTitolo(List listaTipoTitolo) {
		this.listaTipoTitolo = listaTipoTitolo;
	}

	public List getListalivAutority() {
		return listalivAutority;
	}

	public void setListalivAutority(List listalivAutority) {
		this.listalivAutority = listalivAutority;
	}

	public String getTipoTitolo() {
		return tipoTitolo;
	}

	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDataVar() {
		return dataVar;
	}

	public void setDataVar(String dataVar) {
		this.dataVar = dataVar;
	}

	public String getPidArrivoDiFusione() {
		return pidArrivoDiFusione;
	}

	public void setPidArrivoDiFusione(String pidArrivoDiFusione) {
		this.pidArrivoDiFusione = pidArrivoDiFusione;
	}

}
