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
package it.iccu.sbn.web.actionforms.gestionestampe.conservazione;

import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

public class StampaRegistroConservazioneForm extends RicercaInventariCollocazioniForm {

	private static final long serialVersionUID = -7352528865439081940L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String ticket;
	private List listaTipiOrdinamento;

	private String tipoOperazione;
	private String tipoOrdinamento;
	private String elemBlocco;
	private String tipoRicerca;
	private EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
	private List listaBiblio;
//	private List listaComboSerie = new ArrayList();
//	private List listaSerie;
	private boolean disable;
	private boolean sessione = false;
	private String folder;
	private String codiceStatoConservazione;
	private List listaCodStatoConservazione;
	private String codiceTipoMateriale;
	private List listaTipoMateriale;
//	private String nomeFileAppoggio;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI
			.getDefault());

//	private String sezione;
//	private String codPoloSez;
//	private String codBibSez;
//	private String dallaCollocazione;
//	private String dallaSpecificazione;
//	private String allaCollocazione;
//	private String allaSpecificazione;
//	//
//	//Ricerca per range di inventari
//	private String serie;
//	private String endInventario;
//	private String startInventario;
//	//
//	private String selezione;
//	private List listaInventari;
//
	private String tipoModello;
	private List<ModelloStampaVO> elencoModelli = new ArrayList<ModelloStampaVO>();
	private String tipoFormato;

	public EsameCollocRicercaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(EsameCollocRicercaVO ricerca) {
		this.ricerca = ricerca;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

//	public List getListaComboSerie() {
//		return listaComboSerie;
//	}
//
//	public void setListaComboSerie(List listaComboSerie) {
//		this.listaComboSerie = listaComboSerie;
//	}
//
//	public List getListaSerie() {
//		return listaSerie;
//	}
//
//	public void setListaSerie(List listaSerie) {
//		this.listaSerie = listaSerie;
//	}
//
	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

//	public String getNomeFileAppoggio() {
//		return nomeFileAppoggio;
//	}
//
//	public void setNomeFileAppoggio(String nomeFileAppoggio) {
//		this.nomeFileAppoggio = nomeFileAppoggio;
//	}
//
	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}



	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getCodiceStatoConservazione() {
		return codiceStatoConservazione;
	}

	public void setCodiceStatoConservazione(String codiceStatoConservazione) {
		this.codiceStatoConservazione = codiceStatoConservazione;
	}

	public List getListaCodStatoConservazione() {
		return listaCodStatoConservazione;
	}

	public void setListaCodStatoConservazione(
			List listaCodStatoConservazione) {
		this.listaCodStatoConservazione = listaCodStatoConservazione;
	}

	public String getCodiceTipoMateriale() {
		return codiceTipoMateriale;
	}

	public void setCodiceTipoMateriale(String codiceTipoMateriale) {
		this.codiceTipoMateriale = codiceTipoMateriale;
	}

	public List getListaTipoMateriale() {
		return listaTipoMateriale;
	}

	public void setListaTipoMateriale(List listaTipoMateriale) {
		this.listaTipoMateriale = listaTipoMateriale;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
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

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

//	public String getDallaCollocazione() {
//		return dallaCollocazione;
//	}
//
//	public void setDallaCollocazione(String dallaCollocazione) {
//		this.dallaCollocazione = dallaCollocazione;
//	}
//
//	public String getDallaSpecificazione() {
//		return dallaSpecificazione;
//	}
//
//	public void setDallaSpecificazione(String dallaSpecificazione) {
//		this.dallaSpecificazione = dallaSpecificazione;
//	}
//
//	public String getAllaCollocazione() {
//		return allaCollocazione;
//	}
//
//	public void setAllaCollocazione(String allaCollocazione) {
//		this.allaCollocazione = allaCollocazione;
//	}
//
//	public String getAllaSpecificazione() {
//		return allaSpecificazione;
//	}
//
//	public void setAllaSpecificazione(String allaSpecificazione) {
//		this.allaSpecificazione = allaSpecificazione;
//	}
//
//	public String getSezione() {
//		return sezione;
//	}
//
//	public void setSezione(String sezione) {
//		this.sezione = sezione;
//	}
//
//	public String getCodBibSez() {
//		return codBibSez;
//	}
//
//	public void setCodBibSez(String codBibSez) {
//		this.codBibSez = codBibSez;
//	}

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public List<ModelloStampaVO > getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ModelloStampaVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

//	public String getCodPoloSez() {
//		return codPoloSez;
//	}
//
//	public void setCodPoloSez(String codPoloSez) {
//		this.codPoloSez = codPoloSez;
//	}
//
	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

//	public String getSerie() {
//		return serie;
//	}
//
//	public void setSerie(String serie) {
//		this.serie = serie;
//	}
//
//	public String getStartInventario() {
//		return startInventario;
//	}
//
//	public void setStartInventario(String startInventario) {
//		this.startInventario = startInventario;
//	}
//
//	public String getSelezione() {
//		return selezione;
//	}
//
//	public void setSelezione(String selezione) {
//		this.selezione = selezione;
//	}
//
//	public List getListaInventari() {
//		return listaInventari;
//	}
//
//	public void setListaInventari(List listaInventari) {
//		this.listaInventari = listaInventari;
//	}
//
//	public String getEndInventario() {
//		return endInventario;
//	}
//
//	public void setEndInventario(String endInventario) {
//		this.endInventario = endInventario;
//	}
//
//	public List<CodiceVO> getListaInventariInput() {
//
//		List<CodiceVO> output = new ArrayList<CodiceVO>();
//		HashSet<CodiceVO> hm = new HashSet();
//
//		for (int i = 1; i <= 36; i++) {
//			try {
//				String format = String.format("%02d", i);
//				Field field1 = this.getClass().getDeclaredField("serie" + format );
//				Field field2 = this.getClass().getDeclaredField("numero" + format);
//				String serie      =  (String) field1.get(this);
//				String inventario =  (String) field2.get(this);
//				if (ValidazioneDati.strIsEmpty(serie) && ValidazioneDati.strIsEmpty(inventario) )
//					continue;
//				if (!serie.equals("  ") && !inventario.trim().equals("")){
//					hm.add(new CodiceVO(serie, inventario));
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		output.addAll(hm);
//		return output;
//	}
//
	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}
}
