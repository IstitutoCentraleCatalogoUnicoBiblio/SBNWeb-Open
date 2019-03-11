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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;
import java.util.TreeMap;

public class AreaDatiPassaggioInterrogazioneTitoloReturnVO extends
		SerializableVO {

	private static final long serialVersionUID = -5009803447974846510L;

	private String livelloTrovato;
	private String idLista;
	private int maxRighe;
	private int totRighe;
	private int numBlocco;
	private int numNotizie;
	private int totBlocchi;
	private int numPrimo;

	private List listaSintetica;

	private boolean listaSimili;

	private C899 c899Localizzazioni;

	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici nuovo servizio per ricavare, dato un bid,  tutti i legami
	// fra periodici sia verso l'alto che verso il basso
	private String stringDaProsp;
	private TreeMap treeLegamiPeriodici;
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici


	private String codErr;
	private String testoProtocollo;

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
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

	public String getLivelloTrovato() {
		return livelloTrovato;
	}

	public void setLivelloTrovato(String livelloTrovato) {
		this.livelloTrovato = livelloTrovato;
	}

	public boolean isListaSimili() {
		return listaSimili;
	}

	public void setListaSimili(boolean listaSimili) {
		this.listaSimili = listaSimili;
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public C899 getC899Localizzazioni() {
		return c899Localizzazioni;
	}

	public void setC899Localizzazioni(C899 localizzazioni) {
		c899Localizzazioni = localizzazioni;
	}

	public String getStringDaProsp() {
		return stringDaProsp;
	}

	public void setStringDaProsp(String stringDaProsp) {
		this.stringDaProsp = stringDaProsp;
	}

	public TreeMap getTreeLegamiPeriodici() {
		return treeLegamiPeriodici;
	}

	public void setTreeLegamiPeriodici(TreeMap treeLegamiPeriodici) {
		this.treeLegamiPeriodici = treeLegamiPeriodici;
	}

}
