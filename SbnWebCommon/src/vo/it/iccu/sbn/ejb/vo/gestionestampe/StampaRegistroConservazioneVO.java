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
package it.iccu.sbn.ejb.vo.gestionestampe;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.documentofisico.DocumentoFisicoElaborazioniDifferiteOutputVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author
 * VO per la stampa del registro Topografico.Struttura utilizzata sia in input che in output
 * a fronte della valorizzazione dell'attributo lista che conterra il dettaglio della stampa
 */
public class StampaRegistroConservazioneVO extends DocumentoFisicoElaborazioniDifferiteOutputVO{

    /**
	 *
	 */
	private static final long serialVersionUID = -7066350997985012826L;

	// Attributes
	private String tipoOrdinamento;
	private String statoConservazione;
	private String tipoMateriale;
	private List<StampaRegistroConservazioneDettaglioVO> lista = new ArrayList<StampaRegistroConservazioneDettaglioVO>();
	private String descrBib;

	protected Locale locale = Locale.getDefault();
	protected String numberFormat = "###,###,###,##0.00";

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}


	//output
	private List<String> errori = new ArrayList<String>();
	private String msg;
	/**
	 *
	 */
	public StampaRegistroConservazioneVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	public StampaRegistroConservazioneVO(String codPolo, String codeBiblioteca, String sez,
			String coll1,String spec1,String coll2,String spec2, ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
		this.codPolo = codPolo;
		this.codBib = codeBiblioteca;
		this.sezione = sez;
		this.dallaCollocazione = coll1;
		this.dallaSpecificazione = spec1;
		this.allaCollocazione = coll2;
		this.allaSpecificazione = spec2;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public List<String> getErrori() {
		return errori;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getStatoConservazione() {
		return statoConservazione;
	}

	public void setStatoConservazione(String statoConservazione) {
		this.statoConservazione = statoConservazione;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public List<StampaRegistroConservazioneDettaglioVO> getLista() {
		return lista;
	}

	public void setLista(List<StampaRegistroConservazioneDettaglioVO> lista) {
		this.lista = lista;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getDallaCollocazione() {
		return dallaCollocazione;
	}
	public void setDallaCollocazione(String dallaCollocazione) {
		this.dallaCollocazione = dallaCollocazione;
	}
	public String getDallaSpecificazione() {
		return dallaSpecificazione;
	}
	public void setDallaSpecificazione(String dallaSpecificazione) {
		this.dallaSpecificazione = dallaSpecificazione;
	}
	public String getAllaCollocazione() {
		return allaCollocazione;
	}
	public void setAllaCollocazione(String allaCollocazione) {
		this.allaCollocazione = allaCollocazione;
	}
	public String getAllaSpecificazione() {
		return allaSpecificazione;
	}
	public void setAllaSpecificazione(String allaSpecificazione) {
		this.allaSpecificazione = allaSpecificazione;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getNumberFormat() {
		return numberFormat;
	}
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
}
