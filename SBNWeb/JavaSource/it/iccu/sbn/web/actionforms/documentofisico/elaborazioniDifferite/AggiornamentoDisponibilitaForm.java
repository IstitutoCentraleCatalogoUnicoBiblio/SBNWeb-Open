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


import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

public class AggiornamentoDisponibilitaForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = 4100489111932261216L;
	private EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
	private List listaBiblio;
	private boolean disable;
	private boolean sessione=false;
	private String folder;
	private String codTipoFruizione;
	private List listaCodTipoFruizione;//
	private String codRip;
	private List listaCodRiproducibilita;//
	private String codNoDispo;
	private List listaCodNoDispo;//
	private List listaNatura;
	private List listaStatus;

	private String codiceStatoConservazione;//
	private List listaStatoCons;

	private String codDigitalizzazione;//
	private List listaTipoDigit;

	private List listaLivAut;

	// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
	private List listaTipoData;

	//flitri
	private String filtroDataIngressoDa;
	private String filtroDataIngressoA;
	private String filtroNoDispo;
	private String filtroTipoFruizione;
	private String filtroStatoConservazione;
	private String filtroRip;
	private String filtroNatura;
	private String filtroLivAut;

	// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
	private String aaPubbFrom;
	private String aaPubbTo;
	private String tipoData;


	//
	private String nomeFileAppoggio;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	//
	private String selezione;
	private boolean disableInv;
	private boolean disableColl;
	//
//	private String tipoOperazione;
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public List getListaBiblio() {
		return listaBiblio;
	}
	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getCodTipoFruizione() {
		return codTipoFruizione;
	}
	public void setCodTipoFruizione(String codTipoFruizione) {
		this.codTipoFruizione = codTipoFruizione;
	}
	public List getListaCodTipoFruizione() {
		return listaCodTipoFruizione;
	}
	public void setListaCodTipoFruizione(List listaCodTipoFruizione) {
		this.listaCodTipoFruizione = listaCodTipoFruizione;
	}
	public String getCodNoDispo() {
		return codNoDispo;
	}
	public void setCodNoDispo(String codNoDispo) {
		this.codNoDispo = codNoDispo;
	}
	public List getListaCodNoDispo() {
		return listaCodNoDispo;
	}
	public void setListaCodNoDispo(List listaCodNoDispo) {
		this.listaCodNoDispo = listaCodNoDispo;
	}
	public List getListaStatus() {
		return listaStatus;
	}
	public void setListaStatus(List listaStatus) {
		this.listaStatus = listaStatus;
	}
	public List getListaNatura() {
		return listaNatura;
	}
	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}
	public String getFiltroDataIngressoDa() {
		return filtroDataIngressoDa;
	}
	public void setFiltroDataIngressoDa(String dataIngressoDa) {
		this.filtroDataIngressoDa = dataIngressoDa;
	}
	public String getFiltroDataIngressoA() {
		return filtroDataIngressoA;
	}
	public void setFiltroDataIngressoA(String dataIngressoA) {
		this.filtroDataIngressoA = dataIngressoA;
	}
	public String getNomeFileAppoggio() {
		return nomeFileAppoggio;
	}
	public void setNomeFileAppoggio(String nomeFileAppoggio) {
		this.nomeFileAppoggio = nomeFileAppoggio;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public EsameCollocRicercaVO getRicerca() {
		return ricerca;
	}
	public void setRicerca(EsameCollocRicercaVO ricerca) {
		this.ricerca = ricerca;
	}
	public String getSelezione() {
		return selezione;
	}
	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}
	public boolean isDisableInv() {
		return disableInv;
	}
	public void setDisableInv(boolean disableInv) {
		this.disableInv = disableInv;
	}
	public boolean isDisableColl() {
		return disableColl;
	}
	public void setDisableColl(boolean disableColl) {
		this.disableColl = disableColl;
	}
	public List getListaCodRiproducibilita() {
		return listaCodRiproducibilita;
	}
	public void setListaCodRiproducibilita(List listaCodRiproducibilita) {
		this.listaCodRiproducibilita = listaCodRiproducibilita;
	}
	public String getCodiceStatoConservazione() {
		return codiceStatoConservazione;
	}
	public void setCodiceStatoConservazione(String codiceStatoConservazione) {
		this.codiceStatoConservazione = codiceStatoConservazione;
	}
	public List getListaStatoCons() {
		return listaStatoCons;
	}
	public void setListaStatoCons(List listaStatoCons) {
		this.listaStatoCons = listaStatoCons;
	}
	public String getCodDigitalizzazione() {
		return codDigitalizzazione;
	}
	public void setCodDigitalizzazione(String codDigitalizzazione) {
		this.codDigitalizzazione = codDigitalizzazione;
	}
	public List getListaTipoDigit() {
		return listaTipoDigit;
	}
	public void setListaTipoDigit(List listaTipoDigit) {
		this.listaTipoDigit = listaTipoDigit;
	}
	public String getFiltroNoDispo() {
		return filtroNoDispo;
	}
	public void setFiltroNoDispo(String filtroNoDispo) {
		this.filtroNoDispo = filtroNoDispo;
	}
	public String getFiltroTipoFruizione() {
		return filtroTipoFruizione;
	}
	public void setFiltroTipoFruizione(String filtroTipoFruizione) {
		this.filtroTipoFruizione = filtroTipoFruizione;
	}
	public String getFiltroStatoConservazione() {
		return filtroStatoConservazione;
	}
	public void setFiltroStatoConservazione(String filtroStatoConservazione) {
		this.filtroStatoConservazione = filtroStatoConservazione;
	}
	public String getFiltroRip() {
		return filtroRip;
	}
	public void setFiltroRip(String filtroRip) {
		this.filtroRip = filtroRip;
	}
	public String getFiltroNatura() {
		return filtroNatura;
	}
	public void setFiltroNatura(String filtroNatura) {
		this.filtroNatura = filtroNatura;
	}
	public String getCodRip() {
		return codRip;
	}
	public void setCodRip(String codRip) {
		this.codRip = codRip;
	}
	public List getListaLivAut() {
		return listaLivAut;
	}
	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}
	public String getFiltroLivAut() {
		return filtroLivAut;
	}
	public void setFiltroLivAut(String filtroLivAut) {
		this.filtroLivAut = filtroLivAut;
	}

	public List<?> getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}
	public String getAaPubbFrom() {
		return aaPubbFrom;
	}
	public void setAaPubbFrom(String aaPubbFrom) {
		this.aaPubbFrom = aaPubbFrom;
	}
	public String getAaPubbTo() {
		return aaPubbTo;
	}
	public void setAaPubbTo(String aaPubbTo) {
		this.aaPubbTo = aaPubbTo;
	}

	public String getTipoData() {
		return tipoData;
	}
	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}
}
