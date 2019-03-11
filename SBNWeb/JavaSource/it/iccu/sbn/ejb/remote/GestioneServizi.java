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
package it.iccu.sbn.ejb.remote;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.SbnBusinessSessionRemote;
import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBObject;

public interface GestioneServizi extends EJBObject, SbnBusinessSessionRemote {

	////////////////////////////////////////////////////////////////
	public boolean cancellaModalitaPagamento(String ticket, String[] idModalita, Integer[] idModalitaNonCancellate, BaseVO utenteVar) throws RemoteException;

	public ModalitaPagamentoVO aggiornaModalitaPagamento(String ticket, ModalitaPagamentoVO modalitaVO) throws RemoteException, ApplicationException;

	public List getModalitaPagamento(String ticket, String codPolo, String codBib) throws RemoteException;

	public List getSupportiBiblioteca(String ticket, String codPolo, String codBib, String fl_svolg) throws RemoteException;

	public boolean cancellaSupportiBiblioteca(String ticket, String[] idSupporti, Integer[] idSupportiNonCancellati, BaseVO utenteVar) throws RemoteException;

	public boolean aggiornaSupportoBiblioteca(String ticket, SupportoBibliotecaVO supportoVO)  throws RemoteException;

	public SupportoBibliotecaVO getSupportoBiblioteca(String ticket, String codPolo, String codBib, String codSupporto) throws RemoteException;

	public ParametriBibliotecaVO aggiornaParametriBiblioteca(String ticket, ParametriBibliotecaVO parametriVO) throws RemoteException, ApplicationException;

	public ParametriBibliotecaVO getParametriBiblioteca(String ticket, String codPolo, String codBib) throws RemoteException;

	public boolean cancellaControlloIter(String ticket, String[] codiciControllo, int idTipoServizio, String codAttivita, String utenteVar) throws RemoteException;

	public boolean aggiornaControlloIter(String ticket, FaseControlloIterVO controlloIterVO, int idTipoServizio,
			String codAttivita, boolean nuovo, short posizione, TipoAggiornamentoIter tipoOperazione) throws RemoteException;

	public List getControlloIter(String ticket, int idTipoServizio, String codAttivita) throws RemoteException;

	public boolean cancellaIterServizio(String ticket, String[] progressiviIter, int idTipoServizio, String utenteVar) throws RemoteException;

	public boolean aggiornaIter(String ticket, int idTipoServizio, int progr_iter_selezionato, IterServizioVO iter, TipoAggiornamentoIter tipoOperazione, boolean nuovo) throws RemoteException;

	public boolean aggiornaAttivitaBibliotecario(String ticket, List bibliotecariDaAggiungere, List bibliotecariDaRimuovere,
			BaseVO infoBase, int idTipoServizio, String codiceAttivita) throws RemoteException;

	public Map getListeAttivitaBibliotecari(String ticket, TipoServizioVO tipoServizioVO, String codAttivita) throws RemoteException;

	public boolean aggiornaTariffeModalitaErogazione(String ticket, TariffeModalitaErogazioneVO tariffeVO, boolean nuovo, int idTipoServizio) throws RemoteException;

	public boolean aggiornaSupportiModalitaErogazione(String ticket, SupportiModalitaErogazioneVO tariffeVO, boolean nuovo, int idSupportiBiblioteca) throws RemoteException;

	public boolean aggiornaModalitaErogazione(String ticket, ModalitaErogazioneVO tariffeVO, boolean nuovo) throws RemoteException;

	public boolean aggiornaModuloRichiesta(String ticket, List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO) throws RemoteException, ApplicationException;

	public boolean aggiornaTipoServizio(String ticket, TipoServizioVO tipoServizioVO) throws RemoteException;

	public boolean aggiornaServizio(String ticket, ServizioBibliotecaVO servizioVO, int idTipoServizio) throws RemoteException;

	public boolean cancellaServizio(String ticket, ServizioBibliotecaVO servizioVO, int idTipoServizio) throws RemoteException;

	public boolean cancellaTariffeModalitaErogazione(String ticket, TariffeModalitaErogazioneVO modErog, int idTipoServizio) throws RemoteException;

	public boolean cancellaSupportiModalitaErogazione(String ticket, SupportiModalitaErogazioneVO modErog, int idSupportiBiblioteca) throws RemoteException;

	public boolean cancellaModalitaErogazione(String ticket, ModalitaErogazioneVO modErog) throws RemoteException;

	public boolean verificaAutoregistrazione(String ticket, String codPolo, String codBib) throws RemoteException;

	public DescrittoreBloccoVO getListaAutorizzazioni(String ticket, RicercaAutorizzazioneVO ricercaAut) throws RemoteException;

	public boolean insertAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione) throws ApplicationException, RemoteException;

	public boolean updateAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione) throws ApplicationException, RemoteException;

	public boolean cancelAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione) throws RemoteException;

	public List getListaServiziPerTipoServizio(String ticket, String codicePolo, String codiceBiblioteca, String codTipoServizio) throws RemoteException;

	public List getListaServizi(String ticket, String codicePolo, String codiceBiblioteca, String codiceAutorizzazione) throws RemoteException;

	public List getListaServiziAutorizzazione(String ticket, UtenteBibliotecaVO utente) throws RemoteException;

	public String getListaAutorizzazioniServizio(String ticket, String codPoloSer, String codBibSer,int idServ) throws RemoteException;

	public ServizioBibliotecaVO getServizioBiblioteca(String ticket, String codPolo, String codBib, String codTipoServizio, String codServizio) throws RemoteException;

	public MovimentoListaVO getMovimentoListaVO(String ticket, MovimentoVO movimentoSelezionato, Locale locale) throws RemoteException;

	public DescrittoreBloccoVO getListaServiziBiblioteca(String ticket, List serviziAssociati, String codicePolo, String codiceBiblioteca, int elemBlocco) throws RemoteException;

	public int getAutomaticoX(String ticket, String codicePolo, String codiceBib, String codAutorizzazione, char codiceAutomaticoX) throws RemoteException;

	public void cancellaOccupazioni(String ticket, Integer[] id, String uteVar) throws RemoteException;

	public DescrittoreBloccoVO getListaOccupazioni(String ticket, RicercaOccupazioneVO ricerca) throws RemoteException;

	public void aggiornaOccupazione(String ticket, OccupazioneVO occupazioneVO, boolean  nuovo) throws AlreadyExistsException, RemoteException;

	public List getListaMaterieCompleta(String ticket, RicercaMateriaVO recMateria) throws RemoteException;

	public void cancellaMaterie(String ticket, Integer[] idMaterie, String uteVar) throws ApplicationException, RemoteException;

	public void aggiornaMateria(String ticket, MateriaVO materiaVO, boolean nuovo) throws AlreadyExistsException, RemoteException;

	public DescrittoreBloccoVO getListaMaterie(String ticket, RicercaMateriaVO materia) throws RemoteException;

	public void aggiornaSpecTitoloStudio(String ticket, SpecTitoloStudioVO specTitoloVO, boolean nuovo) throws AlreadyExistsException, RemoteException;

	public void cancellaSpecTitoloStudio(String ticket, Integer[] id, String uteVar) throws RemoteException;

	public DescrittoreBloccoVO getListaTitoloStudio(String ticket, RicercaTitoloStudioVO ricTDS) throws RemoteException, Exception;

	public DescrittoreBloccoVO getListaUtenti(String ticket, RicercaUtenteBibliotecaVO uteRicerca, int elemBlocco) throws RemoteException;

	public DescrittoreBloccoVO getListaAnagServiziUte(String ticket, UtenteBibliotecaVO utente, int elemBlocco) throws RemoteException;

	public boolean updateDataRinnovoAut(String ticket, String[] lstCodUte, String dataRinnAut) throws RemoteException;

	public UtenteBibliotecaVO getDettaglioUtente(String ticket, RicercaUtenteBibliotecaVO recUte, String numberFormat, Locale locale) throws RemoteException;

	public UtenteBibliotecaVO getDettaglioUtenteBase(String ticket, RicercaUtenteBibliotecaVO recUte, String numberFormat, Locale locale) throws RemoteException;

	public boolean insertUtente(String ticket, UtenteBibliotecaVO recUte) throws RemoteException;

	public boolean updateUtente(String ticket, UtenteBibliotecaVO recUte) throws ApplicationException, RemoteException;

	public boolean importaUtente(String ticket, UtenteBibliotecaVO utente) throws RemoteException;

	public boolean aggiornaChiaveUtenteById(String ticket,  String idUtente, String chiaveUte ) throws RemoteException;

	public UtenteBibliotecaVO importaBibliotecaComeUtente(String ticket, String codPolo, String codBib, BibliotecaVO bibVO, BaseVO vo, Locale locale)	throws RemoteException;

	public boolean cancelUtenteBiblioteca(String ticket, UtenteBibliotecaVO recUte) throws RemoteException;

	public int verificaMovimentiUtente(String ticket, String idUte) throws RemoteException;

	public ControlloAttivitaServizioResult esisteRichiesta(String ticket, MovimentoVO anaMov) throws RemoteException;

	public Map getListaMovimentiPerErogazione(String ticket, MovimentoVO filtroMov, RicercaRichiesteType tipoRicerca, Locale locale) throws RemoteException;

	public Map getListaMovimenti(String ticket, MovimentoVO anaMov, RicercaRichiesteType tipoRicerca, Locale locale) throws RemoteException;

	public void cancellaRichieste(String ticket, Long[] codRichieste, String uteVar) throws RemoteException;

	public void rifiutaRichieste(String ticket, Long[] codRichieste, String uteVar, boolean inviaMailNotifica) throws RemoteException;

	public int getNumeroMovimentiAttivi(String ticket, String codPolo, String codBibInv, String codSerieInv, int codInven) throws RemoteException;

	public int getNumeroRichiesteAttivePerUtente(String ticket, MovimentoVO anaMov) throws RemoteException;

	public int getNumeroMovimentiAttiviPerUtente(String ticket, MovimentoVO anaMov) throws RemoteException;

	public int getNumeroRichiesteGiornalierePerServizio(String ticket, MovimentoVO anaMov) throws RemoteException;

	public List getListaSollecitiUte(String ticket, MovimentoVO anaMov, RicercaRichiesteType tipoRicerca) throws RemoteException;

	public List<TariffeModalitaErogazioneVO>getTariffaModalitaErogazione(String ticket, String codicePolo, String codiceBiblioteca, String codTipoServ, String fl_svolg)	throws RemoteException;

	public List<SupportiModalitaErogazioneVO>getSupportoModalitaErogazione(String ticket, String codicePolo, String codiceBiblioteca, String codSupporto, String fl_svolg)	throws RemoteException;

	public List<ModalitaErogazioneVO>getTariffaModalitaErogaz(String ticket, String codicePolo, String codiceBiblioteca)	throws RemoteException;

	public DescrittoreBloccoVO verificaEsistenzaUtentePolo(String ticket, UtenteBibliotecaVO uteAna) throws RemoteException;

	public DescrittoreBloccoVO verificaEsistenzaUtentePolo(String ticket, BibliotecaVO bibVO, String codPolo, String codBib, BaseVO vo) throws RemoteException;

	public int getNumMovimentiAttiviPerServizioUtente(String ticket, int idUtenteBiblioteca, int idServizio) throws RemoteException;

	public AutorizzazioneVO getTipoAutorizzazione(String ticket, String codPolo, String codBib, String codTipoAut) throws RemoteException;

	public boolean esistonoUtentiCon(String ticket, String codPolo, String codBib, String codAutorizzazione) throws RemoteException;

	public boolean esistonoUtentiConOcc(String ticket, String codPolo, String codBib, int idOcc) throws RemoteException;

	public boolean esistonoUtentiConSpecTit(String ticket, String codPolo, String codBib, int idSpecTit) throws RemoteException;

	public java.util.List getListaTipiServizio(String ticket, String codPolo, String codBib) throws RemoteException;

	public java.util.List<String> getListaCodiciTipiServizio(String ticket, String codPolo, String codBib) throws RemoteException;

	public List<IterServizioVO> getIterServizio(String ticket, String codicePolo, String codiceBiblioteca, String codTipoServ) throws RemoteException;

	public List<ServizioWebDatiRichiestiVO> getServizioWebDatiRichiesti(String ticket, String codPolo, String codBib, String codTipoServizio, String natura) throws RemoteException;

	public TipoServizioVO getTipoServizio(String ticket, String codPolo, String codBib, String codTipoServizio) throws RemoteException;

	public List<ServizioBibliotecaVO> getServiziAttivi(String ticket, String codPolo, String codBibUte, String codUtente, String codBib, Timestamp data, boolean remoto) throws RemoteException;

	public boolean isUtenteAutorizzato(String ticket, String codPolo, String codBibUte, String codUtente, String codBib, String codTipoServ, String codServ, Timestamp data)  throws RemoteException;

	public List<ServizioBibliotecaVO> getServiziAttivi(String ticket, String codPolo, String codBib, String codFrui) throws RemoteException;

	public void scambioControlliIter(String ticket, int idTipoServizio, String codAttivita, short progressivo, TipoAggiornamentoIter tipoOp) throws RemoteException;

	public void scambioIter(String ticket, int id_tipo_servizio, int progressivo, TipoAggiornamentoIter tipoOp) throws RemoteException;

	public UtenteBaseVO getUtente(String ticket, String codPolo, String codBibUte, String codUtente, String codBib) throws RemoteException;

	public UtenteBaseVO getUtente(String ticket, String codUtente) throws RemoteException;

	public MovimentoVO aggiornaRichiesta(String ticket, MovimentoVO movimento, int idServizio) throws ApplicationException, RemoteException;

	public MovimentoVO aggiornaRichiestaPerCambioServizio(String ticket, MovimentoVO nuovaRichiesta, long codRichDaCancellare, int idServizio, String uteVar) throws ApplicationException, RemoteException;

	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String ticket, String codPolo, String codBib, String codTipoServ, int progrIter, DatiRichiestaILLVO datiILL) throws RemoteException;

	public AttivitaServizioVO getAttivita(String ticket, String codPolo, IterServizioVO iterServizioVO) throws RemoteException;

	public PenalitaServizioVO getPenalitaServizio(String ticket, String codPolo, String codBib, String codTipoServizio, String codServizio) throws RemoteException;

	public void sospendiDirittoUtente(String ticket, MovimentoVO mov, Date dataSospensione, BaseVO datiModifica) throws ApplicationException, RemoteException;

	public boolean esistonoPrenotazioni(String ticket, UtenteBaseVO utenteBaseVO, MovimentoVO anaMov, Timestamp data) throws RemoteException;

	public boolean esistonoPrenotazioni(String ticket, MovimentoVO anaMov, Timestamp data) throws RemoteException;

	public int getNumeroPrenotazioni(String ticket, MovimentoVO anaMov) throws RemoteException;

	public int getNumeroPrenotazioni(String ticket) throws RemoteException;

	public DescrittoreBloccoVO getPrenotazioni(String ticket, MovimentoVO anaMov, Locale locale) throws RemoteException;

	public DescrittoreBloccoVO getPrenotazioni(String ticket, String codBibDest, int maxRighe, Locale locale, String tipoOrd) throws RemoteException;

	public DescrittoreBloccoVO getProroghe(String ticket, String codBibDest, int maxRighe, Locale locale, String tipoOrd) throws RemoteException;

	public DescrittoreBloccoVO getGiacenze(String ticket, String codBibDest, int maxRighe, Locale locale, String tipoOrd) throws RemoteException;

	public void aggiornaSegnatura(String ticket, RangeSegnatureVO segnaturaVO, boolean isNew) throws RemoteException;

	public void cancellaSegnature(String ticket, Integer[] id, String uteVar) throws RemoteException;

	public DescrittoreBloccoVO caricaSegnature(String ticket, String codPolo, String codBib, RangeSegnatureVO segnaturaVO) throws RemoteException;

	public List caricaRelazioni(String ticket, String codPolo, String codBib, String codRelazione) throws RemoteException;

	public void cancellaRelazioni(String ticket, Integer[] id, String uteVar) throws RemoteException;

	public void riattivaRelazioni(String ticket, Integer[] id, String uteVar) throws RemoteException;

	public void aggiornaRelazione(String ticket, RelazioniVO relazioneVO) throws RemoteException;

	public DescrittoreBloccoVO getListeTematiche(String ticket,	MovimentoVO filtroMov, boolean attivitaAttuale, int elemBlocco) throws RemoteException;

	public boolean esisteMovimentoAttivo(String ticket,	MovimentoVO movimento) throws RemoteException;

	public boolean movimentoStatoPrecedeConsegnaDocLett(String ticket, MovimentoListaVO movimento) throws RemoteException;

	public DescrittoreBloccoVO getListaDocumentiNonSbn(String ticket, DocumentoNonSbnRicercaVO filtro) throws ApplicationException, RemoteException;

	public DescrittoreBloccoVO getListaEsemplariDocumentiNonSbn(String ticket, DocumentoNonSbnRicercaVO filtro) throws RemoteException;

	public List<DocumentoNonSbnVO> aggiornaDocumentoNonSbn(String ticket, List<DocumentoNonSbnVO> documenti) throws RemoteException, ApplicationException;

	public void aggiornaEsemplariDocumentoNonSbn(String ticket,	List<EsemplareDocumentoNonSbnVO> esemplari) throws RemoteException, ApplicationException;

	public DocumentoNonSbnVO getDettaglioDocumentoNonSbn(String ticket,	DocumentoNonSbnVO documento) throws RemoteException, ApplicationException;

	public CommandResultVO invoke(CommandInvokeVO command) throws RemoteException, ApplicationException;

	public List<ComboCodDescVO> loadCodiciDiversiDaQuelliDefiniti(
			String ticket, String codPolo, String codBib, List codici, String flSvolg) throws RemoteException;

	public RinnovoDirittiDiffVO gestioneDifferitaRinnovoDiritti(RinnovoDirittiDiffVO rinnDirVO ) throws RemoteException, ApplicationException;

	public AutorizzazioniVO getAutorizzazioneByProfessione(String ticket, String codicePolo, String codiceBib, int idOcc) throws RemoteException, ApplicationException;

	public Date getDateAutorizzazione(String ticket, String codPolo, String codBib, String codAutorizzazione, int idutente, String tipo ) throws RemoteException, ApplicationException;

    public boolean checkEsistenzaUtente(String codfiscale, String mail, String[] ateneo_mat, String idAna) throws RemoteException;

	public DocumentoNonSbnVO getCategoriaFruizioneSegnatura(String ticket, String codPolo, String codBib, String ordSegn) throws ApplicationException, RemoteException;

	public List<DocumentoNonSbnVO>  esisteDocumentoNelRangeDiSegnatura(String ticket,  String codPolo, String codBib, RangeSegnatureVO segnaturaVO, String ordinamento) throws ApplicationException, RemoteException;

}
