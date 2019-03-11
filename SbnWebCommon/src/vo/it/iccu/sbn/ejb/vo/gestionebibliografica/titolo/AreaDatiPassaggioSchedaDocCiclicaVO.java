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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;

public class AreaDatiPassaggioSchedaDocCiclicaVO  extends SerializableVO {
	/**
	 *
	 */

	private static final long serialVersionUID = 6530403361684083298L;

	public String nomeListaSelez;
	public int idListaSelez;
	public String dataListaSelez;
	public String statoLavorRecordSelez;

	private String bidDocLocale;
	private List<String> listaIdDocArrFusione;

	boolean autore = false;
	boolean titolo = false;

	private List<SinteticaTitoliView> listaSchedaIdLocale;
	private List<SinteticaTitoliView> listaSinteticaSchede;

	public String statoLavorRecord;
	public String statoConfronto;

	private String ticket;


	// Campi per aggiornamento Tb_report_indice_id_locali
	public boolean aggiornamento = false;
	public String statoLavorRecordNew;
	public String idArrivoFusione;
	public String tipoTrattamento;

	// Campi da utilizzare per la funzione successivo (idDocLoc è l'identificativo dell'ultimo id prospettato)
	public boolean successivo = false;
	public int idDocLoc = 0;

	// Campi da utilizzare per la funzione di FUSIONE MASSIVA
	private List listaCoppieBidDaFondere;

	// Campi da utilizzare per la funzione di Caricamento delle tabelle (esempio: U|ANA0012003|RMR0110348)
	private List listaCoppieEsitoConfronto;
	private boolean caricaConFusioneAutomatica;



	private String codErr;
	private String testoProtocollo;
	private String messaggisticaDiLavorazione;


	public List addListaIdDocArrFusione(String id) {
		if (listaIdDocArrFusione == null) {
			listaIdDocArrFusione = new ArrayList<String>();
		}
		listaIdDocArrFusione.add(id);
		return listaIdDocArrFusione;
	}

	public void newListaIdDocArrFusione() {
		listaIdDocArrFusione = new ArrayList<String>();
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
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public List<SinteticaTitoliView> getListaSinteticaSchede() {
		return listaSinteticaSchede;
	}
	public void setListaSinteticaSchede(
			List<SinteticaTitoliView> listaSinteticaSchede) {
		this.listaSinteticaSchede = listaSinteticaSchede;
	}
	public List addListaSinteticaSchede(SinteticaTitoliView scheda) {
		if (listaSinteticaSchede == null) {
			listaSinteticaSchede = new ArrayList<SinteticaTitoliView>();
		}
		listaSinteticaSchede.add(scheda);
		return listaSinteticaSchede;
	}

	public void newListaSinteticaSchede() {
		listaSinteticaSchede = new ArrayList<SinteticaTitoliView>();
	}











	public String getNomeListaSelez() {
		return nomeListaSelez;
	}
	public void setNomeListaSelez(String nomeListaSelez) {
		this.nomeListaSelez = nomeListaSelez;
	}
	public String getDataListaSelez() {
		return dataListaSelez;
	}
	public void setDataListaSelez(String dataListaSelez) {
		this.dataListaSelez = dataListaSelez;
	}
	public String getStatoLavorRecordSelez() {
		return statoLavorRecordSelez;
	}
	public void setStatoLavorRecordSelez(String statoLavorRecordSelez) {
		this.statoLavorRecordSelez = statoLavorRecordSelez;
	}

	public List<SinteticaTitoliView> getListaSchedaIdLocale() {
		return listaSchedaIdLocale;
	}
	public void setListaSchedaIdLocale(
			List<SinteticaTitoliView> listaSchedaIdLocale) {
		this.listaSchedaIdLocale = listaSchedaIdLocale;
	}
	public List addListaSchedaIdLocale(SinteticaTitoliView scheda) {
		if (listaSchedaIdLocale == null) {
			listaSchedaIdLocale = new ArrayList<SinteticaTitoliView>();
		}

		listaSchedaIdLocale.add(scheda);
		return listaSchedaIdLocale;
	}

	public void newListaSchedaIdLocale() {
		listaSchedaIdLocale = new ArrayList<SinteticaTitoliView>();
	}


	public int getIdListaSelez() {
		return idListaSelez;
	}
	public void setIdListaSelez(int idListaSelez) {
		this.idListaSelez = idListaSelez;
	}
	public String getStatoLavorRecordNew() {
		return statoLavorRecordNew;
	}
	public void setStatoLavorRecordNew(String statoLavorRecordNew) {
		this.statoLavorRecordNew = statoLavorRecordNew;
	}
	public String getIdArrivoFusione() {
		return idArrivoFusione;
	}
	public void setIdArrivoFusione(String idArrivoFusione) {
		this.idArrivoFusione = idArrivoFusione;
	}
	public String getTipoTrattamento() {
		return tipoTrattamento;
	}
	public void setTipoTrattamento(String tipoTrattamento) {
		this.tipoTrattamento = tipoTrattamento;
	}
	public boolean isAggiornamento() {
		return aggiornamento;
	}
	public void setAggiornamento(boolean aggiornamento) {
		this.aggiornamento = aggiornamento;
	}
	public String getStatoLavorRecord() {
		return statoLavorRecord;
	}
	public void setStatoLavorRecord(String statoLavorRecord) {
		this.statoLavorRecord = statoLavorRecord;
	}
	public String getStatoConfronto() {
		return statoConfronto;
	}
	public void setStatoConfronto(String statoConfronto) {
		this.statoConfronto = statoConfronto;
	}
	public boolean isSuccessivo() {
		return successivo;
	}
	public void setSuccessivo(boolean successivo) {
		this.successivo = successivo;
	}
	public int getIdDocLoc() {
		return idDocLoc;
	}
	public void setIdDocLoc(int idDocLoc) {
		this.idDocLoc = idDocLoc;
	}
	public String getBidDocLocale() {
		return bidDocLocale;
	}
	public void setBidDocLocale(String bidDocLocale) {
		this.bidDocLocale = bidDocLocale;
	}

	public List<String> getListaIdDocArrFusione() {
		return listaIdDocArrFusione;
	}

	public void setListaIdDocArrFusione(List<String> listaIdDocArrFusione) {
		this.listaIdDocArrFusione = listaIdDocArrFusione;
	}

	public List getListaCoppieBidDaFondere() {
		return listaCoppieBidDaFondere;
	}

	public void setListaCoppieBidDaFondere(List listaCoppieBidDaFondere) {
		this.listaCoppieBidDaFondere = listaCoppieBidDaFondere;
	}

	public List addListaCoppieBidDaFondere(String schedaCoppie) {
		if (listaCoppieBidDaFondere == null) {
			listaCoppieBidDaFondere = new ArrayList();
		}

		listaCoppieBidDaFondere.add(schedaCoppie);
		return listaCoppieBidDaFondere;
	}

	public List getListaCoppieEsitoConfronto() {
		return listaCoppieEsitoConfronto;
	}

	public void setListaCoppieEsitoConfronto(List listaCoppieEsitoConfronto) {
		this.listaCoppieEsitoConfronto = listaCoppieEsitoConfronto;
	}

	public List addListaCoppieEsitoConfronto(String schedaCoppie) {
		if (listaCoppieEsitoConfronto == null) {
			listaCoppieEsitoConfronto = new ArrayList();
		}

		listaCoppieEsitoConfronto.add(schedaCoppie);
		return listaCoppieEsitoConfronto;
	}

	public String getMessaggisticaDiLavorazione() {
		return messaggisticaDiLavorazione;
	}

	public void setMessaggisticaDiLavorazione(String messaggisticaDiLavorazione) {
		this.messaggisticaDiLavorazione = messaggisticaDiLavorazione;
	}

	public boolean isAutore() {
		return autore;
	}

	public void setAutore(boolean autore) {
		this.autore = autore;
	}

	public boolean isTitolo() {
		return titolo;
	}

	public void setTitolo(boolean titolo) {
		this.titolo = titolo;
	}

	public boolean isCaricaConFusioneAutomatica() {
		return caricaConFusioneAutomatica;
	}

	public void setCaricaConFusioneAutomatica(boolean caricaConFusioneAutomatica) {
		this.caricaConFusioneAutomatica = caricaConFusioneAutomatica;
	}


}
