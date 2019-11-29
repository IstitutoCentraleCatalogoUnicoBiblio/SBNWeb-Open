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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

public class ErogazioneRicercaForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 634185755582200298L;
	public static final String LOCALE = "L";
	public static final String ILL    = "I";
	public static final String TUTTI  = " ";

	public static final String FOLDER_UTENTE_DOC   = "S";
	public static final String FOLDER_LISTE        = "L";
	public static final String FOLDER_SOLLECITI    = "SL";
	public static final String FOLDER_PRENOTAZIONI = "P";
	public static final String FOLDER_GIACENZE     = "G";
	public static final String FOLDER_PROROGHE     = "PG";

	private boolean enable;
	private String pathForm;
	private String provengoDa;
	private RicercaRichiesteType tipoRicerca = RicercaRichiesteType.RICERCA_PER_UTENTE;
	private RicercaRichiesteType tipoRicerca_old = null;

	// gestisce la pagina di conferma con richiesrta
	boolean conferma = false;
	private String richiesta = null;

	//  salva la sessione
	private boolean sessione = false;

	// indica che folder sto usando
	private String folder;

	//tipi di svolgimento
	String[] svolgimenti            = {"Locale", "ILL"};
	List<ComboCodDescVO> svolgimentiSelezionati = new ArrayList<ComboCodDescVO>();

	// tipo di ricerca
	private String segnaturaRicerca;

	//liste per i campi select
	private List lstTipiServizio;
	private List lstStatiRichiesta;
	private List lstStatiMovimento;
	//private List lstCodAttivita;
	private List lstCodiciTipiServizio;
	//private List lstCodiciModalitaErogazione;

	List<TariffeModalitaErogazioneVO> tariffeErogazioneVO;
	List<IterServizioVO> iterServizioVO;

	private MovimentoVO anaMov = new MovimentoRicercaVO();
	private MovimentoVO filtroPrenot = new MovimentoRicercaVO();
	private List<TipoServizioVO> listaTipiServizio;
	private List<ComboCodDescVO> ordinamentiMov;


	private InfoDocumentoVO infoDocumentoVO;

	//////////////////////////////////////////////////////
	//Gestione Prenotazioni
	private List<MovimentoListaVO> listaPrenotazioni;
	private Long[] codPreMul;
	private String codPreSing;
	private List<ComboCodDescVO> ordinamentiPrenotazioni;
	//Fine gestione Prenotazioni
	//////////////////////////////////////////////////////


	//////////////////////////////////////////////////////
	//Gestione Proroghe
	private List<MovimentoListaVO> listaProroghe;
	private Long[] codProMul;
	private String codProSing;
	private List<ComboCodDescVO> ordinamentiProroghe;
	//Fine gestione Prenotazioni
	//////////////////////////////////////////////////////

	//////////////////////////////////////////////////////
	//Gestione Giacenze
	private List<MovimentoListaVO> listaGiacenze;
	private Long[] codGiaMul;
	private String codGiaSing;
	private List<ComboCodDescVO> ordinamentiGiacenze;
	//Fine gestione Prenotazioni
	//////////////////////////////////////////////////////

	//////////////////////////////////////////////////////
	//Gestione Solleciti (movimenti scaduti e non sollecitati)
	private List<MovimentoListaVO> listaSolleciti;
	private Long[] codSolMul;
	private String codSolSing;
	private List<ComboCodDescVO> ordinamentiSolleciti;
	//Fine gestione Solleciti
	//////////////////////////////////////////////////////

	//ordinamento per funzione Ordina
	private String ordPrenotazioni;
	private String ordProroghe;
	private String ordGiacenze;
	private String ordSolleciti;

	//////////////////////////////////////////////////////
	//Gestione blocchi
	private boolean abilitaBlocchi;

	private int totBlocchi;

	private String livelloRicerca;
	//Fine gestione blocchi
	//////////////////////////////////////////////////////

	//almaviva5_20120220 rfid
	private boolean rfidEnabled;

	public MovimentoVO getAnaMov() {
		return anaMov;
	}

	public void setAnaMov(MovimentoVO anaMov) {
		this.anaMov = anaMov;
	}

	public MovimentoVO getFiltroPrenot() {
		return filtroPrenot;
	}

	public void setFiltroPrenot(MovimentoVO filtroPrenot) {
		this.filtroPrenot = filtroPrenot;
	}

	public List<TipoServizioVO> getListaTipiServizio() {
		return listaTipiServizio;
	}

	public void setListaTipiServizio(List<TipoServizioVO> listaTipiServizio) {
		this.listaTipiServizio = listaTipiServizio;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getPathForm() {
		return pathForm;
	}

	public void setPathForm(String pathForm) {
		this.pathForm = pathForm;
	}

	public String getProvengoDa() {
		return provengoDa;
	}

	public void setProvengoDa(String provengoDa) {
		this.provengoDa = provengoDa;
	}
	public String getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public RicercaRichiesteType getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(RicercaRichiesteType ricercaRichiesteType) {
		this.tipoRicerca = ricercaRichiesteType;
	}

	public RicercaRichiesteType getTipoRicerca_old() {
		return tipoRicerca_old;
	}

	public void setTipoRicerca_old(RicercaRichiesteType ricercaRichiesteType) {
		this.tipoRicerca_old = ricercaRichiesteType;
	}

	public void setTipoRicercaStr(String value) {
		this.tipoRicerca = RicercaRichiesteType.valueOf(value);
	}

	public String getTipoRicercaStr() {
		return this.tipoRicerca.toString();
	}

	public List getLstTipiServizio() {
		return lstTipiServizio;
	}

	public void setLstTipiServizio(List lstTipiServizio) {
		this.lstTipiServizio = lstTipiServizio;
	}

	public List getLstStatiRichiesta() {
		return lstStatiRichiesta;
	}

	public void setLstStatiRichiesta(List lstStatiRichieste) {
		this.lstStatiRichiesta = lstStatiRichieste;
	}

	public List getLstStatiMovimento() {
		return lstStatiMovimento;
	}

	public void setLstStatiMovimento(List lstStatiMovimento) {
		this.lstStatiMovimento = lstStatiMovimento;
	}

//	public List getLstCodAttivita() {
//		return lstCodAttivita;
//	}
//
//	public void setLstCodAttivita(List lstCodAttivita) {
//		this.lstCodAttivita = lstCodAttivita;
//	}

	public List getLstCodiciTipiServizio() {
		return lstCodiciTipiServizio;
	}

	public void setLstCodiciTipiServizio(List lstCodiciTipiServizio) {
		this.lstCodiciTipiServizio = lstCodiciTipiServizio;
	}

//	public List getLstCodiciModalitaErogazione() {
//		return lstCodiciModalitaErogazione;
//	}
//
//	public void setLstCodiciModalitaErogazione(List lstCodiciModalitaErogazione) {
//		this.lstCodiciModalitaErogazione = lstCodiciModalitaErogazione;
//	}

	public List<TariffeModalitaErogazioneVO> getTariffeErogazioneVO() {
		return tariffeErogazioneVO;
	}

	public void setTariffeErogazioneVO(
			List<TariffeModalitaErogazioneVO> tariffeErogazioneVO) {
		this.tariffeErogazioneVO = tariffeErogazioneVO;
	}

	public List<IterServizioVO> getIterServizioVO() {
		return iterServizioVO;
	}

	public void setIterServizioVO(List<IterServizioVO> iterServizioVO) {
		this.iterServizioVO = iterServizioVO;
	}

	public String[] getSvolgimenti() {
		return svolgimenti;
	}

//	public void setSvolgimenti(String[] svolgimenti) {
//		this.svolgimenti = svolgimenti;
//	}

	public List<ComboCodDescVO> getSvolgimentiSelezionati() {
		return svolgimentiSelezionati;
	}

	public void setSvolgimentiSelezionati(List<ComboCodDescVO> value) {
		this.svolgimentiSelezionati = value;
	/*
		if (value.length == this.svolgimenti.length) {
			this.setLocIllTutto(ErogazioneRicercaForm.TUTTI);
		} else if (value.length==1 &&  value[0].equalsIgnoreCase("Locale")) {
			this.setLocIllTutto(ErogazioneRicercaForm.LOCALE);
		} else if (value.length==1 &&  value[0].equalsIgnoreCase("ILL")) {
			this.setLocIllTutto(ErogazioneRicercaForm.ILL);
		} else
			this.setLocIllTutto(ErogazioneRicercaForm.LOCALE);
		*/
	}

	public InfoDocumentoVO getInfoDocumentoVO() {
		return infoDocumentoVO;
	}

	public void setInfoDocumentoVO(InfoDocumentoVO infoDocumentoVO) {
		this.infoDocumentoVO = infoDocumentoVO;
	}

	public List<MovimentoListaVO> getListaPrenotazioni() {
		return listaPrenotazioni;
	}

	public void setListaPrenotazioni(List<MovimentoListaVO> listaPrenotazioni) {
		this.listaPrenotazioni = listaPrenotazioni;
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public Long[] getCodPreMul() {
		return codPreMul;
	}

	public void setCodPreMul(Long[] codPreMul) {
		this.codPreMul = codPreMul;
	}

	public String getCodPreSing() {
		return codPreSing;
	}

	public void setCodPreSing(String codPreSing) {
		this.codPreSing = codPreSing;
	}

	public Long[] getCodProMul() {
		return codProMul;
	}

	public void setCodProMul(Long[] codProMul) {
		this.codProMul = codProMul;
	}

	public Long[] getCodGiaMul() {
		return codGiaMul;
	}

	public void setCodGiaMul(Long[] codGiaMul) {
		this.codGiaMul = codGiaMul;
	}

	public String getCodProSing() {
		return codProSing;
	}

	public void setCodProSing(String codProSing) {
		this.codProSing = codProSing;
	}

	public String getCodGiaSing() {
		return codGiaSing;
	}

	public void setCodGiaSing(String codGiaSing) {
		this.codGiaSing = codGiaSing;
	}

	public List<ComboCodDescVO> getOrdinamentiPrenotazioni() {
		return ordinamentiPrenotazioni;
	}

	public void setOrdinamentiPrenotazioni(
			List<ComboCodDescVO> ordinamentiPrenotazioni) {
		this.ordinamentiPrenotazioni = ordinamentiPrenotazioni;
	}

	public List<ComboCodDescVO> getOrdinamentiProroghe() {
		return ordinamentiProroghe;
	}

	public void setOrdinamentiProroghe(
			List<ComboCodDescVO> ordinamentiProroghe) {
		this.ordinamentiProroghe = ordinamentiProroghe;
	}

	public List<ComboCodDescVO> getOrdinamentiGiacenze() {
		return ordinamentiGiacenze;
	}

	public void setOrdinamentiGiacenze(
			List<ComboCodDescVO> ordinamentiGiacenze) {
		this.ordinamentiGiacenze = ordinamentiGiacenze;
	}

	public List<ComboCodDescVO> getOrdinamentiSolleciti() {
		return ordinamentiSolleciti;
	}

	public void setOrdinamentiSolleciti(
			List<ComboCodDescVO> ordinamentiSolleciti) {
		this.ordinamentiSolleciti = ordinamentiSolleciti;
	}

	public List<MovimentoListaVO> getListaSolleciti() {
		return listaSolleciti;
	}

	public void setListaSolleciti(List<MovimentoListaVO> listaSolleciti) {
		this.listaSolleciti = listaSolleciti;
	}

	public List<MovimentoListaVO> getListaProroghe() {
		return listaProroghe;
	}

	public void setListaProroghe(List<MovimentoListaVO> listaProroghe) {
		this.listaProroghe = listaProroghe;
	}

	public List<MovimentoListaVO> getListaGiacenze() {
		return listaGiacenze;
	}

	public void setListaGiacenze(List<MovimentoListaVO> listaGiacenze) {
		this.listaGiacenze = listaGiacenze;
	}


	public Long[] getCodSolMul() {
		return codSolMul;
	}

	public void setCodSolMul(Long[] codSolMul) {
		this.codSolMul = codSolMul;
	}

	public String getCodSolSing() {
		return codSolSing;
	}

	public void setCodSolSing(String codSolSing) {
		this.codSolSing = codSolSing;
	}

	public List<ComboCodDescVO> getOrdinamentiMov() {
		return ordinamentiMov;
	}

	public void setOrdinamentiMov(List<ComboCodDescVO> ordinamentiMov) {
		this.ordinamentiMov = ordinamentiMov;
	}

	public String getOrdPrenotazioni() {
		return ordPrenotazioni;
	}

	public void setOrdPrenotazioni(String ordPrenotazioni) {
		this.ordPrenotazioni = ordPrenotazioni;
	}

	public String getOrdProroghe() {
		return ordProroghe;
	}

	public void setOrdProroghe(String ordProroghe) {
		this.ordProroghe = ordProroghe;
	}

	public String getOrdGiacenze() {
		return ordGiacenze;
	}

	public void setOrdGiacenze(String ordGiacenze) {
		this.ordGiacenze = ordGiacenze;
	}

	public String getOrdSolleciti() {
		return ordSolleciti;
	}

	public void setOrdSolleciti(String ordSolleciti) {
		this.ordSolleciti = ordSolleciti;
	}

	public String getSegnaturaRicerca() {
		return segnaturaRicerca;
	}

	public void setSegnaturaRicerca(String segnaturaRicerca) {
		this.segnaturaRicerca = segnaturaRicerca;
	}

	public boolean isRfidEnabled() {
		return rfidEnabled;
	}

	public void setRfidEnabled(boolean enableRfid) {
		this.rfidEnabled = enableRfid;
	}

}
