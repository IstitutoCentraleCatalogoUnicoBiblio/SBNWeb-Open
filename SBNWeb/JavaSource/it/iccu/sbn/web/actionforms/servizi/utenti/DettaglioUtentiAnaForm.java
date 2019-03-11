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
package it.iccu.sbn.web.actionforms.servizi.utenti;

import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


public class DettaglioUtentiAnaForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -8103865762776363225L;
	public static final String FOLDER_SERVIZIO_CANCELLA = "UC";
	public static final String FOLDER_ANAGRAFICA = "A ";
	public static final String FOLDER_BIBLIOPOLO = "B ";
	public static final String FOLDER_AUTORIZZAZIONI = "U ";

	public enum RichiestaType {
		AGGIORNA,
		CANCELLA,
		RESET_PASSWORD,
		CODICE_FISCALE;
	}

	//se mostrare altri dati utente
	private boolean mostraAltriDati=false;

	//indica se i dati di domicilio saranno impostati uguali a quelli di residenza
	private boolean flagDomUgualeRes=false;

// x tutti indica se proteggere i campi (?? dubbi su reale utilizzo)
	private boolean enable;
	private String pathForm;
	private String provengoDa;
//  salva la sessione
	private boolean sessione = false;
// indica che folder sto usando
	private String folder;
// gestisce la pagina di conferma e la richiesrta Ok/Canc/CodFisc
	boolean conferma = false;
	private RichiestaType richiesta = null;
// anagrafe
	private UtenteBibliotecaVO uteAna = new UtenteBibliotecaVO();
// copia di UteAna per verificare eventuali modifiche per l'aggiornamento
	private UtenteBibliotecaVO uteAnaOLD;
	private List provinciaResidenza;
	private List nazCitta;
	private List provinciaDomicilio;
	private List provenienze;
// professioni
	private List professioni;
	private List occupazioni;
	private List ateneo;
	private List tipoPersonalita;
	private List specTitoloStudio;
	private List titoloStudio;
	private List elencoMaterie;
	//private List allMaterie;
	private List<ElementoSinteticaServizioVO> lstServiziAutor;
	private String[] codMateria;

	private List titoloStudioArr;
	private List professioneArr;


// bibliopolo

// documenti
	private List elencoDocumenti;

// autorizzazioni
	private List elencoAutorizzazioni;
//	private List<ServizioVO> elencoServizi;
	private RicercaAutorizzazioneVO ricAut;
// array di ritorno select utente secco
	private List singoloUte;
// fine
	// indica se è stato già controllata la presenza di omonimi
	private boolean utentePoloControllato = false;
	// porta i dati digitati in ricerca per un nuovo utente
	private RicercaUtenteBibliotecaVO uteRic = new RicercaUtenteBibliotecaVO();
	private UtenteBibliotecaVO dettaglioConferma;
	private Integer clicNotaPrg;
	private List<UtenteBibliotecaVO> selectedUtenti;
	private int posizioneScorrimento=0;
	private int numUtenti=0;

	//tipo utente persona  P o ente E
	private String tipoUtente="";


	public UtenteBibliotecaVO getUteAnaOLD() {
		return uteAnaOLD;
	}

	public void setUteAnaOLD(UtenteBibliotecaVO uteAnaOLD) {
		this.uteAnaOLD =  (UtenteBibliotecaVO) uteAnaOLD.clone();
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

	public UtenteBibliotecaVO getUteAna() {
		return uteAna;
	}

	public void setUteAna(UtenteBibliotecaVO uteAna) {
		this.uteAna = uteAna;
	}

	public List getProvinciaDomicilio() {
		return provinciaDomicilio;
	}

	public void setProvinciaDomicilio(List provinciaDomicilio) {
		this.provinciaDomicilio = provinciaDomicilio;
	}

	public List getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(List provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public List getNazCitta() {
		return nazCitta;
	}

	public void setNazCitta(List nazCitta) {
		this.nazCitta = nazCitta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List getOccupazioni() {
		return occupazioni;
	}

	public void setOccupazioni(List occupazioni) {
		this.occupazioni = occupazioni;
	}

	public List getProfessioni() {
		return professioni;
	}

	public void setProfessioni(List professioni) {
		this.professioni = professioni;
	}

	public List getSpecTitoloStudio() {
		return specTitoloStudio;
	}

	public void setSpecTitoloStudio(List specTitoloStudio) {
		this.specTitoloStudio = specTitoloStudio;
	}

	public List getTipoPersonalita() {
		return tipoPersonalita;
	}

	public void setTipoPersonalita(List tipoPersonalita) {
		this.tipoPersonalita = tipoPersonalita;
	}

	public List getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(List titoloStudio) {
		this.titoloStudio = titoloStudio;
	}

	public List getAteneo() {
		return ateneo;
	}

	public void setAteneo(List ateneo) {
		this.ateneo = ateneo;
	}


	public List getElencoMaterie() {
		return elencoMaterie;
	}

	public void setElencoMaterie(List elencoMaterie) {
		this.elencoMaterie = elencoMaterie;
	}

	public String[] getCodMateria() {
		return codMateria;
	}

	public void setCodMateria(String[] codMateria) {
		this.codMateria = codMateria;
	}

	public List getElencoDocumenti() {
		return elencoDocumenti;
	}

	public void setElencoDocumenti(List elencoDocumenti) {
		this.elencoDocumenti = elencoDocumenti;
	}

	public List getElencoAutorizzazioni() {
		return elencoAutorizzazioni;
	}

	public void setElencoAutorizzazioni(List elencoAutorizzazioni) {
		this.elencoAutorizzazioni = elencoAutorizzazioni;
	}


//	public List getElencoServizi() {
//		return elencoServizi;
//	}
//
//	public void setElencoServizi(List elencoServizi) {
//		this.elencoServizi = elencoServizi;
//	}

	public List getProvenienze() {
		return provenienze;
	}

	public void setProvenienze(List provenienze) {
		this.provenienze = provenienze;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public RichiestaType getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(RichiestaType richiesta) {
		this.richiesta = richiesta;
	}

	public List getSingoloUte() {
		return singoloUte;
	}

	public void setSingoloUte(List singoloUte) {
		this.singoloUte = singoloUte;
	}

	public String getPathForm() {
		return pathForm;
	}

	public void setPathForm(String pathForm) {
		this.pathForm = pathForm;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		List<ServizioVO> servizi = this.getUteAna().getAutorizzazioni().getListaServizi();
		if (servizi != null) {
			for (ServizioVO s: servizi) {
				s.resetCancella();
			}
		}

//		List<MateriaVO> materie = this.getUteAna().getProfessione().getMaterie();
//		if (materie!=null) {
//			for (MateriaVO m: materie) {
//					m.setSelezionato("");
//			}
//		}
	}

	public String getProvengoDa() {
		return provengoDa;
	}

	public void setProvengoDa(String provengoDa) {
		this.provengoDa = provengoDa;
	}

	public RicercaAutorizzazioneVO getRicAut() {
		return ricAut;
	}

	public void setRicAut(RicercaAutorizzazioneVO ricAut) {
		this.ricAut = ricAut;
	}

	public boolean isUtentePoloControllato() {
		return utentePoloControllato;
	}

	public void setUtentePoloControllato(boolean utentePoloControllato) {
		this.utentePoloControllato = utentePoloControllato;
	}

	public List getLstServiziAutor() {
		return lstServiziAutor;
	}

	public void setLstServiziAutor(List lstServiziAutor) {
		this.lstServiziAutor = lstServiziAutor;
	}

	public RicercaUtenteBibliotecaVO getUteRic() {
		return uteRic;
	}

	public void setUteRic(RicercaUtenteBibliotecaVO uteRic) {
		this.uteRic = uteRic;
	}

	public String getFolderServizioCancella() {
		return DettaglioUtentiAnaForm.FOLDER_SERVIZIO_CANCELLA;
	}

	public String getFolderAnagrafica() {
		return DettaglioUtentiAnaForm.FOLDER_ANAGRAFICA;
	}

	public String getFolderBiblioPolo() {
		return DettaglioUtentiAnaForm.FOLDER_BIBLIOPOLO;
	}

	public String getFolderAutorizzazioni() {
		return DettaglioUtentiAnaForm.FOLDER_AUTORIZZAZIONI;
	}

	public boolean isMostraAltriDati() {
		return mostraAltriDati;
	}

	public void setMostraAltriDati(boolean mostraAltriDati) {
		this.mostraAltriDati = mostraAltriDati;
	}

	public boolean isFlagDomUgualeRes() {
		return flagDomUgualeRes;
	}

	public void setFlagDomUgualeRes(boolean flagDomUgualeRes) {
		this.flagDomUgualeRes = flagDomUgualeRes;
	}

	public void setDettaglioConferma(UtenteBibliotecaVO dettaglioConferma) {
		this.dettaglioConferma = dettaglioConferma;
	}

	public UtenteBibliotecaVO getDettaglioConferma() {
		return dettaglioConferma;
	}

	public Integer getClicNotaPrg() {
		return clicNotaPrg;
	}

	public void setClicNotaPrg(Integer clicNotaPrg) {
		this.clicNotaPrg = clicNotaPrg;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}



	public int getNumUtenti() {
		return numUtenti;
	}

	public void setNumUtenti(int numUtenti) {
		this.numUtenti = numUtenti;
	}

	public List<UtenteBibliotecaVO> getSelectedUtenti() {
		return selectedUtenti;
	}

	public void setSelectedUtenti(List<UtenteBibliotecaVO> listaUtenti) {
		this.selectedUtenti = listaUtenti;
	}

	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}



	public List getTitoloStudioArr() {
		return titoloStudioArr;
	}

	public void setTitoloStudioArr(List titoloStudioArr) {
		this.titoloStudioArr = titoloStudioArr;
	}

	public List getProfessioneArr() {
		return professioneArr;
	}

	public void setProfessioneArr(List professioneArr) {
		this.professioneArr = professioneArr;
	}



}
