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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;

import java.util.ArrayList;
import java.util.List;


public class AreaTabellaOggettiDaCondividereVO  extends SerializableVO {

	// = AreaTabellaOggettiDaCatturareVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -906561548861275689L;
	private String idPadre;
	private String descrizionePerRicerca;
	private boolean bidRoot;
	private DettaglioTitoloCompletoVO dettTitComVO = new DettaglioTitoloCompletoVO();
	private String descrizionePerRicercaMadre51;;
	private DettaglioAutoreGeneraleVO dettAutGenVO = new DettaglioAutoreGeneraleVO();
	private String tipoRicerca;
	private String tipoOrdinamSelez;
	private String tipoAuthority;
	private String tipoMateriale;
	private String natura;

	// INIZIO Evolutiva Novembre 2015 - almaviva2 gestione interrogazione da SERVIZI ILL
	private String livelloRicerca;
	private List<BibliotecaVO> filtroLocBib = new ArrayList<BibliotecaVO>();

	// Campi per la prospettazione dei simili trovati (Sintetica)
    private String idLista;
    private int maxRighe;
    private int totRighe;
    private int numBlocco;
    private int numNotizie;
    private int totBlocchi;
    private int numPrimo;
    private List listaSintetica;


	private String codErr;
	private String testoProtocollo;
	private boolean callBatch = false;



	public DettaglioTitoloCompletoVO getDettTitComVO() {
		return dettTitComVO;
	}
	public void setDettTitComVO(DettaglioTitoloCompletoVO dettTitComVO) {
		this.dettTitComVO = dettTitComVO;
	}
	public String getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}
	public boolean isBidRoot() {
		return bidRoot;
	}
	public void setBidRoot(boolean bidRoot) {
		this.bidRoot = bidRoot;
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
	public int getNumPrimo() {
		return numPrimo;
	}
	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public DettaglioAutoreGeneraleVO getDettAutGenVO() {
		return dettAutGenVO;
	}
	public void setDettAutGenVO(DettaglioAutoreGeneraleVO dettAutGenVO) {
		this.dettAutGenVO = dettAutGenVO;
	}
	public String getDescrizionePerRicerca() {
		return descrizionePerRicerca;
	}
	public void setDescrizionePerRicerca(String descrizionePerRicerca) {
		this.descrizionePerRicerca = descrizionePerRicerca;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public String getTipoAuthority() {
		return tipoAuthority;
	}
	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}
	public String getTipoMateriale() {
		return tipoMateriale;
	}
	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}
	public String getDescrizionePerRicercaMadre51() {
		return descrizionePerRicercaMadre51;
	}
	public void setDescrizionePerRicercaMadre51(String descrizionePerRicercaMadre51) {
		this.descrizionePerRicercaMadre51 = descrizionePerRicercaMadre51;
	}
	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}
	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}
	public boolean isCallBatch() {
		return callBatch;
	}
	public void setCallBatch(boolean callBatch) {
		this.callBatch = callBatch;
	}
	public String getLivelloRicerca() {
		return livelloRicerca;
	}
	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}
	public List<BibliotecaVO> getFiltroLocBib() {
		return filtroLocBib;
	}

	public void setFiltroLocBib(List<BibliotecaVO> filtroLocBib) {
		this.filtroLocBib = filtroLocBib;
	}



}
