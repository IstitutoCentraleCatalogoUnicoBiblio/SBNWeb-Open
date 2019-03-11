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
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerStampaOrdineVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author VO per la stampa lista fascicoli. Struttura utilizzata sia in input
 *         che in output a fronte della valorizzazione dell'attributo lista che
 *         conterra il dettaglio della stampa
 */
public class StampaListaFascicoliVO extends	DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -7066350997985012826L;

	private String codPolo;
	private String codBib;
	private String statoFascicolo;
	private String codFornitore;
	private String descrFornitore;
	private String tipoOrd;
	private String annoOrd;
	private String codiceOrd;
	private String annoIniziale;
	private String annoFinale;
	private String periodicita;
	private String statoOrdine;
	private String tipoOrdine;
	private String ordinamento;
	private String stampaNote;
	private String tipoFormato;
	private List<StampaListaFascicoliDettaglioVO> lista = new ArrayList<StampaListaFascicoliDettaglioVO>();
	private String descrBib;

	private RicercaKardexPeriodicoPerStampaOrdineVO<FascicoloVO> parametri = new RicercaKardexPeriodicoPerStampaOrdineVO<FascicoloVO>();

	protected Locale locale = Locale.getDefault();
	protected String numberFormat = "###,###,###,##0.00";

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	/**
	 *
	 */
	public StampaListaFascicoliVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public StampaListaFascicoliVO(String codPolo, String codBib,
			String statoFascicolo, String codFornitore, String descrFornitore,
			String tipoOrd, String annoOrd, String codiceOrd,
			String annoIniziale, String annoFinale, String periodicita,
			String statoOrdine, String tipoOrdine, String ordinamento,
			String stampaNote, String tipoFormato,
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.statoFascicolo = statoFascicolo;
		this.codFornitore = codFornitore;
		this.descrFornitore = descrFornitore;
		this.tipoOrd = tipoOrd;
		this.annoOrd = annoOrd;
		this.codiceOrd = codiceOrd;
		this.annoIniziale = annoIniziale;
		this.annoFinale = annoFinale;
		this.periodicita = periodicita;
		this.statoOrdine = statoOrdine;
		this.tipoOrdine = tipoOrdine;
		this.ordinamento = ordinamento;
		this.stampaNote = stampaNote;
		this.tipoFormato = tipoFormato;
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

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
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

	public String getStatoFascicolo() {
		return statoFascicolo;
	}

	public void setStatoFascicolo(String statoFascicolo) {
		this.statoFascicolo = statoFascicolo;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getDescrFornitore() {
		return descrFornitore;
	}

	public void setDescrFornitore(String descrFornitore) {
		this.descrFornitore = descrFornitore;
	}

	public String getTipoOrd() {
		return tipoOrd;
	}

	public void setTipoOrd(String tipoOrd) {
		this.tipoOrd = tipoOrd;
	}

	public String getAnnoOrd() {
		return annoOrd;
	}

	public void setAnnoOrd(String annoOrd) {
		this.annoOrd = annoOrd;
	}

	public String getCodiceOrd() {
		return codiceOrd;
	}

	public void setCodiceOrd(String codiceOrd) {
		this.codiceOrd = codiceOrd;
	}

	public String getAnnoIniziale() {
		return annoIniziale;
	}

	public void setAnnoIniziale(String annoIniziale) {
		this.annoIniziale = annoIniziale;
	}

	public String getAnnoFinale() {
		return annoFinale;
	}

	public void setAnnoFinale(String annoFinale) {
		this.annoFinale = annoFinale;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public String getStatoOrdine() {
		return statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getStampaNote() {
		return stampaNote;
	}

	public void setStampaNote(String stampaNote) {
		this.stampaNote = stampaNote;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public List<StampaListaFascicoliDettaglioVO> getLista() {
		return lista;
	}

	public void setLista(List<StampaListaFascicoliDettaglioVO> lista) {
		this.lista = lista;
	}

	public RicercaKardexPeriodicoPerStampaOrdineVO<FascicoloVO> getParametri() {
		return parametri;
	}

	public void setParametri(RicercaKardexPeriodicoPerStampaOrdineVO<FascicoloVO> parametri) {
		this.parametri = parametri;
	}
}
