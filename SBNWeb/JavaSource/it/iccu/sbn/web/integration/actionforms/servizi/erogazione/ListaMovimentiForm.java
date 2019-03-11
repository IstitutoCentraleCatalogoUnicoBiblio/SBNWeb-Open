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
package it.iccu.sbn.web.integration.actionforms.servizi.erogazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaMovimentiForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -6489429231799995036L;

	public enum RichiestaListaMovimentiType {
		CANCELLA, RIFIUTA, PRENOTAZIONE;
	}

	// Indica il chiamante. Utilizzato nel caso la Action relativa alla lista
	// movimenti
	// è chiamata da un'altra linea funzionale differente dai servizi
	private String chiamante;
	private String updateCombo;

	// campo per indicare se ricerca è per Inv Seg Ute
	private RicercaRichiesteType tipoRicerca;
	// descrizione segnatura per la quale fare la ricerca
	private String segnaturaRicerca;
	// sessione
	private boolean sessione = false;
	// conferma pre operazioni
	private boolean conferma = false;
	// conferma pre dati nuova richiesta
	private boolean confermaNuovaRichiesta = false;
	// booleano per gestione presenza eventuali solleciti
	private boolean mostraSolleciti = false;

	private RichiestaListaMovimentiType richiesta = null;
	// liste e selezioni corpo pagina
	protected List<MovimentoListaVO> listaMovRicUte;
	private String solleciti;
	private List<SollecitiVO> listaSollecitiUte = new ArrayList<SollecitiVO>();
	protected Long[] codSelMov;
	private String codSelMovSing;
	private List<MovimentoListaVO> listaRichiesteScadute;
	private String codRichiestaScadutaSel;

	private boolean campiModuloRichiesta = true;

	// dati passati al dettaglio
	private MovimentoListaVO mov = new MovimentoListaVO();
	// dati letti passati dalla ricerca
	private MovimentoVO movRicerca = new MovimentoVO();
	// dati nuova richiesta creata
	private MovimentoVO nuovaRichiesta = new MovimentoVO();

	// errore nuova richiesta
	private boolean flgErrNuovaRichiesta;

	// salva id servizio
	private int salvaIdServizio;

	private String cod_erog;
	private String codSupporto;

	// gestione blocchi
	private boolean abilitaBlocchi;
	private String livelloRicerca;

	// filtri per nuova richiesta di servizio
	private String tipoServ;
	private String modErog;
	private String progrAtt;
	private String statoMov;
	private String codBibInv;
	private String codSerieInv;
	private String codInvenInv;
	private String desXRicSeg;
	private String codUtente;
	private String codBibUtente;
	private List<ServizioBibliotecaVO> lstCodiciServizio;
	private String codServNuovaRich;
	private String codTipoServNuovaRich;
	private UtenteBaseVO utenteVO = new UtenteBaseVO();
	private InfoDocumentoVO infoDocumentoVO = new InfoDocumentoVO();

	private List<ComboVO> elencoBib = new ArrayList<ComboVO>();

	private List<SupportoBibliotecaVO> tipiSupporto;

	private boolean rfidEnabled;
	private String rfidChiaveInventario;

	private List<BibliotecaVO> bibliotecaFornitrice = new ArrayList<BibliotecaVO>();

	private String titoloDocAltraBib;

	private List<TB_CODICI> tipiSupportoILL;
	private List<TB_CODICI> modoErogazioneILL;

	public List<SupportoBibliotecaVO> getTipiSupporto() {
		return tipiSupporto;
	}

	public void setTipiSupporto(List<SupportoBibliotecaVO> tipiSupporto) {
		this.tipiSupporto = tipiSupporto;
	}

	private List<TariffeModalitaErogazioneVO> modoErogazione;

	public List<TariffeModalitaErogazioneVO> getModoErogazione() {
		return modoErogazione;
	}

	public void setModoErogazione(
			List<TariffeModalitaErogazioneVO> modoErogazione) {
		this.modoErogazione = modoErogazione;
	}

	public List<ComboVO> getElencoBib() {
		return elencoBib;
	}

	public void setElencoBib(List<ComboVO> elencoBib) {
		this.elencoBib = elencoBib;
	}

	private MovimentoVO anaMov = new MovimentoVO();

	public MovimentoVO getAnaMov() {
		return anaMov;
	}

	public void setAnaMov(MovimentoVO anaMov) {
		this.anaMov = anaMov;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// codSelMov = new Long[] {}; //ROX 14.04.10 TCK 3388
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public Long[] getCodSelMov() {
		return codSelMov;
	}

	public void setCodSelMov(Long[] codSelMov) {
		this.codSelMov = codSelMov;
	}

	public String getCodSelMovSing() {
		return codSelMovSing;
	}

	public void setCodSelMovSing(String codSelMovSing) {
		this.codSelMovSing = codSelMovSing;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isConfermaNuovaRichiesta() {
		return confermaNuovaRichiesta;
	}

	public void setConfermaNuovaRichiesta(boolean confermaNuovaRichiesta) {
		this.confermaNuovaRichiesta = confermaNuovaRichiesta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public RichiestaListaMovimentiType getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(RichiestaListaMovimentiType richiesta) {
		this.richiesta = richiesta;
	}

	public List<MovimentoListaVO> getListaMovRicUte() {
		return listaMovRicUte;
	}

	public void setListaMovRicUte(List<MovimentoListaVO> listaMovRicUte) {
		this.listaMovRicUte = listaMovRicUte;
	}

	public MovimentoListaVO getMov() {
		return mov;
	}

	public void setMov(MovimentoListaVO mov) {
		this.mov = mov;
	}

	public MovimentoVO getMovRicerca() {
		return movRicerca;
	}

	public void setMovRicerca(MovimentoVO movRicerca) {
		this.movRicerca = movRicerca;
	}

	public RicercaRichiesteType getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(RicercaRichiesteType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getModErog() {
		return modErog;
	}

	public void setModErog(String modErog) {
		this.modErog = modErog;
	}

	public String getProgrAtt() {
		return progrAtt;
	}

	public void setProgrAtt(String progrAtt) {
		this.progrAtt = progrAtt;
	}

	public String getStatoMov() {
		return statoMov;
	}

	public void setStatoMov(String statoMov) {
		this.statoMov = statoMov;
	}

	public String getTipoServ() {
		return tipoServ;
	}

	public void setTipoServ(String tipoServ) {
		this.tipoServ = tipoServ;
	}

	public List<SollecitiVO> getListaSollecitiUte() {
		return listaSollecitiUte;
	}

	public void setListaSollecitiUte(List<SollecitiVO> listaSollecitiUte) {
		this.listaSollecitiUte = listaSollecitiUte;
	}

	public String getSolleciti() {
		return solleciti;
	}

	public void setSolleciti(String solleciti) {
		this.solleciti = solleciti;
	}

	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public String getCodBibInv() {
		return codBibInv;
	}

	public void setCodBibInv(String codBibInv) {
		this.codBibInv = codBibInv;
	}

	public String getCodSerieInv() {
		return codSerieInv;
	}

	public void setCodSerieInv(String codSerieInv) {
		this.codSerieInv = codSerieInv != null ? codSerieInv.toUpperCase()
				: null;
	}

	public String getCodInvenInv() {
		return codInvenInv;
	}

	public void setCodInvenInv(String codInvenInv) {
		this.codInvenInv = codInvenInv;
	}

	public String getDesXRicSeg() {
		return desXRicSeg;
	}

	public void setDesXRicSeg(String desXRicSeg) {
		this.desXRicSeg = desXRicSeg;
	}

	public int getListaMovRicUteSize() {
		return (listaMovRicUte == null ? 0 : listaMovRicUte.size());
	}

	public List<ServizioBibliotecaVO> getLstCodiciServizio() {
		return lstCodiciServizio;
	}

	public void setLstCodiciServizio(
			List<ServizioBibliotecaVO> lstCodiciServizio) {
		this.lstCodiciServizio = lstCodiciServizio;
	}

	public String getCodServNuovaRich() {
		return codServNuovaRich;
	}

	public void setCodServNuovaRich(String codServNuovaRich) {
		this.codServNuovaRich = codServNuovaRich;
	}

	public String getCodTipoServNuovaRich() {
		return codTipoServNuovaRich;
	}

	public void setCodTipoServNuovaRich(String codTipoServNuovaRich) {
		this.codTipoServNuovaRich = codTipoServNuovaRich;
	}

	public String getSegnaturaRicerca() {
		return segnaturaRicerca;
	}

	public void setSegnaturaRicerca(String segnaturaRicerca) {
		this.segnaturaRicerca = segnaturaRicerca;
	}

	public List<MovimentoListaVO> getListaRichiesteScadute() {
		return listaRichiesteScadute;
	}

	public void setListaRichiesteScadute(
			List<MovimentoListaVO> listaRichiesteScadute) {
		this.listaRichiesteScadute = listaRichiesteScadute;
	}

	public String getCodRichiestaScadutaSel() {
		return codRichiestaScadutaSel;
	}

	public void setCodRichiestaScadutaSel(String codRichiestaScadutaSel) {
		this.codRichiestaScadutaSel = codRichiestaScadutaSel;
	}

	public boolean isMostraSolleciti() {
		return mostraSolleciti;
	}

	public void setMostraSolleciti(boolean mostraSolleciti) {
		this.mostraSolleciti = mostraSolleciti;
	}

	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}

	public String getCodBibUtente() {
		return codBibUtente;
	}

	public void setCodBibUtente(String codBibUtente) {
		this.codBibUtente = codBibUtente;
	}

	public UtenteBaseVO getUtenteVO() {
		return utenteVO;
	}

	public void setUtenteVO(UtenteBaseVO utenteVO) {
		this.utenteVO = utenteVO;
	}

	public InfoDocumentoVO getInfoDocumentoVO() {
		return infoDocumentoVO;
	}

	public void setInfoDocumentoVO(InfoDocumentoVO infoDocumentoVO) {
		this.infoDocumentoVO = infoDocumentoVO;
	}

	public String getChiamante() {
		return chiamante;
	}

	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}

	public String getUpdateCombo() {
		return updateCombo;
	}

	public void setUpdateCombo(String updateCombo) {
		this.updateCombo = updateCombo;
	}

	public MovimentoVO getNuovaRichiesta() {
		return nuovaRichiesta;
	}

	public void setNuovaRichiesta(MovimentoVO nuovaRichiesta) {
		this.nuovaRichiesta = nuovaRichiesta;
	}

	public boolean isCampiModuloRichiesta() {
		return campiModuloRichiesta;
	}

	public void setCampiModuloRichiesta(boolean campiModuloRichiesta) {
		this.campiModuloRichiesta = campiModuloRichiesta;
	}

	public String getCod_erog() {
		return cod_erog;
	}

	public void setCod_erog(String cod_erog) {
		this.cod_erog = cod_erog;
	}

	public String getCodSupporto() {
		return codSupporto;
	}

	public void setCodSupporto(String codSupporto) {
		this.codSupporto = codSupporto;
	}

	public boolean isFlgErrNuovaRichiesta() {
		return flgErrNuovaRichiesta;
	}

	public void setFlgErrNuovaRichiesta(boolean flgErrNuovaRichiesta) {
		this.flgErrNuovaRichiesta = flgErrNuovaRichiesta;
	}

	public int getSalvaIdServizio() {
		return salvaIdServizio;
	}

	public void setSalvaIdServizio(int salvaIdServizio) {
		this.salvaIdServizio = salvaIdServizio;
	}

	public boolean isRfidEnabled() {
		return rfidEnabled;
	}

	public void setRfidEnabled(boolean rfidEnabled) {
		this.rfidEnabled = rfidEnabled;
	}

	public String getRfidChiaveInventario() {
		return rfidChiaveInventario;
	}

	public void setRfidChiaveInventario(String rfidChiaveInventario) {
		this.rfidChiaveInventario = ValidazioneDati
				.trimOrNull(rfidChiaveInventario);
	}

	public List<BibliotecaVO> getBibliotecaFornitrice() {
		return bibliotecaFornitrice;
	}

	public void setBibliotecaFornitrice(List<BibliotecaVO> bibliotecaFornitrice) {
		this.bibliotecaFornitrice = bibliotecaFornitrice;
	}

	public String getTitoloDocAltraBib() {
		return titoloDocAltraBib;
	}

	public void setTitoloDocAltraBib(String titoloDocAltraBib) {
		this.titoloDocAltraBib = ValidazioneDati.trimOrNull(titoloDocAltraBib);
	}

	public BibliotecaVO getItemBib(int idx) {

		// automatically grow List size
		while (idx >= ValidazioneDati.size(this.bibliotecaFornitrice)) {
			this.bibliotecaFornitrice.add(new BibliotecaVO());
		}
		return this.bibliotecaFornitrice.get(idx);
	}

	public List<TB_CODICI> getTipiSupportoILL() {
		return tipiSupportoILL;
	}

	public void setTipiSupportoILL(List<TB_CODICI> tipiSupportoILL) {
		this.tipiSupportoILL = tipiSupportoILL;
	}

	public List<TB_CODICI> getModoErogazioneILL() {
		return modoErogazioneILL;
	}

	public void setModoErogazioneILL(List<TB_CODICI> setModoErogazioneILL) {
		this.modoErogazioneILL = setModoErogazioneILL;
	}

}
