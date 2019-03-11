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
package it.iccu.sbn.web.actionforms.gestionestampe.richiesteill;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaRichiesteIllForm extends ActionForm {


	private static final long serialVersionUID = -3247472623603834072L;
	private String elemBlocco;
	private String dal;
	private String al;
	private String statoRichiesta;
	private List listaStatoRichiesta;
	private String statoCircolazione;
	private List listaStatoCircolazione;
	private String bibliotecaRichiedente;
	private List listaBibliotecaRichiedente;
	private String codiceUtente1a;
	private String codiceUtente1b;
	private String codiceUtente2a;
	private String codiceUtente2b;

	private boolean chkStampaLocalizzazioneRich;
	private boolean chkStampaLocalizzazioneBibl;
	private boolean chkStampaStimaCostoILL;
	private boolean chkStampaPrestitoInterbibl;
	private boolean chkStampaPrestitoInterbiblTratt;
	private boolean chkStampaPrestitoMatMult;
	private boolean chkStampaRiproduzioneRich;
	private boolean chkStampaRiproduzioneTratt;
	private boolean chkStampaServizioInterbibl;
	private boolean chkStampaServizioSemplice;
	private boolean chkStampaServizioInterbiblPrecat;
	private boolean chkStampaTutte;
	private String tipoFormato;

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getCodiceUtente1a() {
		return codiceUtente1a;
	}
	public void setCodiceUtente1a(String codiceUtente1a) {
		this.codiceUtente1a = codiceUtente1a;
	}
	public String getCodiceUtente1b() {
		return codiceUtente1b;
	}
	public void setCodiceUtente1b(String codiceUtente1b) {
		this.codiceUtente1b = codiceUtente1b;
	}
	public String getCodiceUtente2a() {
		return codiceUtente2a;
	}
	public void setCodiceUtente2a(String codiceUtente2a) {
		this.codiceUtente2a = codiceUtente2a;
	}
	public String getCodiceUtente2b() {
		return codiceUtente2b;
	}
	public void setCodiceUtente2b(String codiceUtente2b) {
		this.codiceUtente2b = codiceUtente2b;
	}
	public String getAl() {
		return al;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public String getBibliotecaRichiedente() {
		return bibliotecaRichiedente;
	}
	public void setBibliotecaRichiedente(String bibliotecaRichiedente) {
		this.bibliotecaRichiedente = bibliotecaRichiedente;
	}
	public boolean isChkStampaLocalizzazioneBibl() {
		return chkStampaLocalizzazioneBibl;
	}
	public void setChkStampaLocalizzazioneBibl(boolean chkStampaLocalizzazioneBibl) {
		this.chkStampaLocalizzazioneBibl = chkStampaLocalizzazioneBibl;
	}
	public boolean isChkStampaLocalizzazioneRich() {
		return chkStampaLocalizzazioneRich;
	}
	public void setChkStampaLocalizzazioneRich(boolean chkStampaLocalizzazioneRich) {
		this.chkStampaLocalizzazioneRich = chkStampaLocalizzazioneRich;
	}
	public boolean isChkStampaPrestitoInterbibl() {
		return chkStampaPrestitoInterbibl;
	}
	public void setChkStampaPrestitoInterbibl(boolean chkStampaPrestitoInterbibl) {
		this.chkStampaPrestitoInterbibl = chkStampaPrestitoInterbibl;
	}
	public boolean isChkStampaPrestitoInterbiblTratt() {
		return chkStampaPrestitoInterbiblTratt;
	}
	public void setChkStampaPrestitoInterbiblTratt(
			boolean chkStampaPrestitoInterbiblTratt) {
		this.chkStampaPrestitoInterbiblTratt = chkStampaPrestitoInterbiblTratt;
	}
	public boolean isChkStampaPrestitoMatMult() {
		return chkStampaPrestitoMatMult;
	}
	public void setChkStampaPrestitoMatMult(boolean chkStampaPrestitoMatMult) {
		this.chkStampaPrestitoMatMult = chkStampaPrestitoMatMult;
	}
	public boolean isChkStampaRiproduzioneRich() {
		return chkStampaRiproduzioneRich;
	}
	public void setChkStampaRiproduzioneRich(boolean chkStampaRiproduzioneRich) {
		this.chkStampaRiproduzioneRich = chkStampaRiproduzioneRich;
	}
	public boolean isChkStampaRiproduzioneTratt() {
		return chkStampaRiproduzioneTratt;
	}
	public void setChkStampaRiproduzioneTratt(boolean chkStampaRiproduzioneTratt) {
		this.chkStampaRiproduzioneTratt = chkStampaRiproduzioneTratt;
	}
	public boolean isChkStampaServizioInterbibl() {
		return chkStampaServizioInterbibl;
	}
	public void setChkStampaServizioInterbibl(boolean chkStampaServizioInterbibl) {
		this.chkStampaServizioInterbibl = chkStampaServizioInterbibl;
	}
	public boolean isChkStampaServizioInterbiblPrecat() {
		return chkStampaServizioInterbiblPrecat;
	}
	public void setChkStampaServizioInterbiblPrecat(
			boolean chkStampaServizioInterbiblPrecat) {
		this.chkStampaServizioInterbiblPrecat = chkStampaServizioInterbiblPrecat;
	}
	public boolean isChkStampaServizioSemplice() {
		return chkStampaServizioSemplice;
	}
	public void setChkStampaServizioSemplice(boolean chkStampaServizioSemplice) {
		this.chkStampaServizioSemplice = chkStampaServizioSemplice;
	}
	public boolean isChkStampaStimaCostoILL() {
		return chkStampaStimaCostoILL;
	}
	public void setChkStampaStimaCostoILL(boolean chkStampaStimaCostoILL) {
		this.chkStampaStimaCostoILL = chkStampaStimaCostoILL;
	}
	public boolean isChkStampaTutte() {
		return chkStampaTutte;
	}
	public void setChkStampaTutte(boolean chkStampaTutte) {
		this.chkStampaTutte = chkStampaTutte;
	}
	public String getDal() {
		return dal;
	}
	public void setDal(String dal) {
		this.dal = dal;
	}
	public String getElemBlocco() {
		return elemBlocco;
	}
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}
	public List getListaBibliotecaRichiedente() {
		return listaBibliotecaRichiedente;
	}
	public void setListaBibliotecaRichiedente(List listaBibliotecaRichiedente) {
		this.listaBibliotecaRichiedente = listaBibliotecaRichiedente;
	}
	public List getListaStatoCircolazione() {
		return listaStatoCircolazione;
	}
	public void setListaStatoCircolazione(List listaStatoCircolazione) {
		this.listaStatoCircolazione = listaStatoCircolazione;
	}
	public List getListaStatoRichiesta() {
		return listaStatoRichiesta;
	}
	public void setListaStatoRichiesta(List listaStatoRichiesta) {
		this.listaStatoRichiesta = listaStatoRichiesta;
	}
	public String getStatoCircolazione() {
		return statoCircolazione;
	}
	public void setStatoCircolazione(String statoCircolazione) {
		this.statoCircolazione = statoCircolazione;
	}
	public String getStatoRichiesta() {
		return statoRichiesta;
	}
	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}


}
