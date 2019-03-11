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
package it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class RicercaBibliotecaForm extends ActionForm {


	private static final long serialVersionUID = -7898683567621078402L;
	private String nomeRic = "";
	private String checkNome;
	private String indirizzoRic = "";
	private String checkIndirizzo;
	private String codiceAnaRic = "";
	private String codiceBibRic = "";
	private String codicePoloRic = "";
	private String checkBibInPolo;
	private String cittaRic = "";
	private boolean checkEsattaCitta;
	private String capRic = "";
	private boolean checkEsattaCap;
	private List<ComboVO> elencoProvince = new ArrayList<ComboVO>();
	private String selezioneProvincia = "";
	private List<ComboVO> elencoTipiBib = new ArrayList<ComboVO>();
	private String selezioneTipoBib = "";
	private List<ComboVO> elencoFlagBib = new ArrayList<ComboVO>();
	private String selezioneFlagBib = "";
	private List<ComboVO> elencoOrdinamenti = new ArrayList<ComboVO>();
	private String selezioneOrdinamento = "";
	private List<ComboVO> elencoDug = new ArrayList<ComboVO>();
	private String selezioneDug = "";
	private String codSistemaMetro = "";

	private String abilitatoNuovo;

	private String acquisizioni;
	private String scaricoInventariale;
	private String servizioUtente;
	private List<TB_CODICI> listaCodiciSistMetro;
	private List<ComboVO> elencoTipoBibSBN;

	private BibliotecaRicercaVO ricerca = new BibliotecaRicercaVO();

	public String getAbilitatoNuovo() {
		return abilitatoNuovo;
	}

	public void setAbilitatoNuovo(String abilitatoNuovo) {
		this.abilitatoNuovo = abilitatoNuovo;
	}

	public String getCodiceAnaRic() {
		return codiceAnaRic;
	}

	public void setCodiceAnaRic(String codiceAnaRic) {
		this.codiceAnaRic = codiceAnaRic;
	}

	public String getCodiceBibRic() {
		return codiceBibRic;
	}

	public void setCodiceBibRic(String codiceBibRic) {
		this.codiceBibRic = codiceBibRic;
	}

	public String getCheckBibInPolo() {
		return checkBibInPolo;
	}

	public void setCheckBibInPolo(String checkBibInPolo) {
		this.checkBibInPolo = checkBibInPolo;
	}

	public String getCittaRic() {
		return cittaRic;
	}

	public void setCittaRic(String cittaRic) {
		this.cittaRic = cittaRic;
	}

	public boolean isCheckEsattaCitta() {
		return checkEsattaCitta;
	}

	public void setCheckEsattaCitta(boolean checkEsattaCitta) {
		this.checkEsattaCitta = checkEsattaCitta;
	}

	public String getCapRic() {
		return capRic;
	}

	public void setCapRic(String capRic) {
		this.capRic = capRic;
	}

	public boolean isCheckEsattaCap() {
		return checkEsattaCap;
	}

	public void setCheckEsattaCap(boolean checkEsattaCap) {
		this.checkEsattaCap = checkEsattaCap;
	}

	public List<ComboVO> getElencoTipiBib() {
		return elencoTipiBib;
	}

	public String getSelezioneTipoBib() {
		return selezioneTipoBib;
	}

	public void setSelezioneTipoBib(String selezioneTipoBib) {
		this.selezioneTipoBib = selezioneTipoBib;
	}

	public List<ComboVO> getElencoFlagBib() {
		return elencoFlagBib;
	}

	public void setElencoFlagBib(List<ComboVO> elencoFlagBib) {
		this.elencoFlagBib = elencoFlagBib;
	}

	public String getSelezioneFlagBib() {
		return selezioneFlagBib;
	}

	public void setSelezioneFlagBib(String selezioneFlagBib) {
		this.selezioneFlagBib = selezioneFlagBib;
	}

	public String getNomeRic() {
		return nomeRic;
	}

	public void setNomeRic(String nomeRic) {
		this.nomeRic = nomeRic;
	}

	public String getIndirizzoRic() {
		return indirizzoRic;
	}

	public void setIndirizzoRic(String indirizzoRic) {
		this.indirizzoRic = indirizzoRic;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.checkEsattaCap = false;
		this.checkEsattaCitta = false;

		super.reset(mapping, request);
	}

	public List<ComboVO> getElencoProvince() {
		return elencoProvince;
	}

	public void setElencoProvince(List<ComboVO> elencoProvince) {
		this.elencoProvince = elencoProvince;
	}

	public String getSelezioneProvincia() {
		return selezioneProvincia;
	}

	public void setSelezioneProvincia(String selezioneProvincia) {
		this.selezioneProvincia = selezioneProvincia;
	}

	public List<ComboVO> getElencoOrdinamenti() {
		return elencoOrdinamenti;
	}

	public String getSelezioneOrdinamento() {
		return selezioneOrdinamento;
	}

	public void setSelezioneOrdinamento(String selezioneOrdinamento) {
		this.selezioneOrdinamento = selezioneOrdinamento;
	}

	public List<ComboVO> getElencoDug() {
		return elencoDug;
	}

	public void setElencoDug(List<ComboVO> elencoDug) {
		this.elencoDug = elencoDug;
	}

	public String getSelezioneDug() {
		return selezioneDug;
	}

	public void setSelezioneDug(String selezioneDug) {
		this.selezioneDug = selezioneDug;
	}

	public String getAcquisizioni() {
		return acquisizioni;
	}

	public void setAcquisizioni(String acquisizioni) {
		this.acquisizioni = acquisizioni;
	}

	public String getCodicePoloRic() {
		return codicePoloRic;
	}

	public void setCodicePoloRic(String codicePoloRic) {
		this.codicePoloRic = codicePoloRic;
	}

	public String getCheckNome() {
		return checkNome;
	}

	public void setCheckNome(String checkNome) {
		this.checkNome = checkNome;
	}

	public String getCheckIndirizzo() {
		return checkIndirizzo;
	}

	public void setCheckIndirizzo(String checkIndirizzo) {
		this.checkIndirizzo = checkIndirizzo;
	}

	public String getScaricoInventariale() {
		return scaricoInventariale;
	}

	public void setScaricoInventariale(String scaricoInventariale) {
		this.scaricoInventariale = scaricoInventariale;
	}

	public String getServizioUtente() {
		return servizioUtente;
	}

	public void setServizioUtente(String servizioUtente) {
		this.servizioUtente = servizioUtente;
	}

	public void setListaCodiciSistMetro(List<TB_CODICI> listaCodiciSistMetro) {
		this.listaCodiciSistMetro = listaCodiciSistMetro;

	}

	public List<TB_CODICI> getListaCodiciSistMetro() {
		return listaCodiciSistMetro;
	}

	public String getCodSistemaMetro() {
		return codSistemaMetro;
	}

	public void setCodSistemaMetro(String codSistemaMetro) {
		this.codSistemaMetro = codSistemaMetro;
	}

	public void setElencoTipoBibSBN(List<ComboVO> elencoTipoBibSBN) {
		this.elencoTipoBibSBN = elencoTipoBibSBN;
	}

	public List<ComboVO> getElencoTipoBibSBN() {
		return elencoTipoBibSBN;
	}

	public void setElencoTipiBib(List<ComboVO> elencoTipiBib) {
		this.elencoTipiBib = elencoTipiBib;
	}

	public BibliotecaRicercaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(BibliotecaRicercaVO ricerca) {
		this.ricerca = ricerca;
	}

	public void setElencoOrdinamenti(List<ComboVO> elencoOrdinamenti) {
		this.elencoOrdinamenti = elencoOrdinamenti;
	}

}
