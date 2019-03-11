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
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSpeseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RicercaTitCollEditoriVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RigheVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface GestioneAcquisizioni extends EJBObject,SbnBusinessSessionRemote  {
//	public int somma(int a, int b) throws RemoteException;

/*	public DescrittoreBloccoVO nextBlocco(String ticket, String idLista, int numBlocco) throws
	RemoteException, ValidationException;
	public void clearBlocchiIdLista(String ticket, String idLista) throws RemoteException, ValidationException;
	public void clearBlocchiAll(String ticket) throws RemoteException, ValidationException;
*/
	//////////////////////////////////////////////////////////////77
	//public List getTitolo(String bid, String ticket) throws RemoteException,ValidationException ;
	public List  costruisciCatenaRinnovati(StrutturaTerna ordRinn, String codBibl) throws RemoteException, ValidationException;
	public boolean  gestioneStampaOrdini(List listaOggetti, List idList, String tipoOggetti, String utente, String bo) throws RemoteException, ValidationException;
	public List  gestioneStampato(List listaOggetti, String tipoOggetti, String bo) throws RemoteException, ValidationException;

	public ListaSuppFatturaVO gestioneFatturaDaDocFisico(ListaSuppFatturaVO ricercaFatture) throws RemoteException, ValidationException;
	public List getTitoloOrdine(String bidPassato)  throws RemoteException, DaoManagerException;
	public TitoloACQVO getTitoloRox(String bidPassato)  throws RemoteException, DaoManagerException;
	public List getTitolo(List listaBid, String ticket)  throws RemoteException,ValidationException;
	public 	void ValidaSezioneVO (SezioneVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaBilancioVO (BilancioVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaComunicazioneVO (ComunicazioneVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaStrutturaProfiloVO (StrutturaProfiloVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaFatturaVO (FatturaVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaOrdiniVO (OrdiniVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaFornitoreVO (FornitoreVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaSuggerimentoVO (SuggerimentoVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaCambioVO (CambioVO prova) throws RemoteException,ValidationException ;
	public 	void ValidaBuoniOrdineVO (BuoniOrdineVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaGaraVO (GaraVO oggettoVO) throws RemoteException,ValidationException ;
	public 	void ValidaDocumentoVO (DocumentoVO oggettoVO)  throws RemoteException,ValidationException ;

	public Inventario getInventarioBean() throws RemoteException, ValidationException;
	public AmministrazioneMail getAmministrazioneMailBean() throws RemoteException, ValidationException;


	public boolean  inserisciCambio(CambioVO cambio) throws RemoteException, ValidationException;
	public List<CambioVO> getRicercaListaCambi(ListaSuppCambioVO ricercaCambi) throws RemoteException, ValidationException;
	public List<StrutturaInventariOrdVO> getInventariOrdineRilegatura(ListaSuppOrdiniVO ricercaInvOrd) throws RemoteException, ValidationException;
	public List<RigheVO>  ripartoSpese (ListaSuppSpeseVO criteriRiparto) throws RemoteException, ValidationException;
	public List<StampaBuoniVO>  impostaBuoniOrdineDaStampare (ConfigurazioneBOVO configurazione, List listaOggetti, String tipoOggetti, Boolean ristampa, String ticket, String utente, String denoBibl) throws RemoteException, ValidationException;

	public boolean  cancellaCambio(CambioVO cambio) throws RemoteException;
	public boolean  modificaCambio(CambioVO cambio) throws RemoteException, ValidationException;
	public List getListaOrdini() throws RemoteException;
	public List<OrdiniVO> getRicercaListaOrdini(ListaSuppOrdiniVO ricercaOrdini) throws RemoteException, ValidationException;
	public int ValidaPreRicercaOrdini (ListaSuppOrdiniVO ricercaOrdini) throws RemoteException, ValidationException;

	//public DescrittoreBloccoVO getRicercaListaOrdini(ListaSuppOrdiniVO ricercaOrdini) throws RemoteException, ValidationException;
	public List<StrutturaCombo> getRicercaBiblAffiliate(String codiceBiblioteca, String codiceAttivita) throws RemoteException, ValidationException;
	public boolean  inserisciOrdine(OrdiniVO ordine) throws RemoteException, ValidationException;
	public boolean inserisciOrdineBiblHib(OrdiniVO ordine, List listaBiblAff) throws RemoteException, ValidationException;
	public boolean  cancellaOrdine(OrdiniVO ordine) throws RemoteException, ValidationException, ApplicationException;
	public boolean  modificaOrdine(OrdiniVO ordine) throws RemoteException, ValidationException;
	public List<FornitoreVO> getRicercaListaFornitori(ListaSuppFornitoreVO ricercaFornitori) throws RemoteException, ValidationException;

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getRicercaTitCollEditori(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket) throws RemoteException, ValidationException;
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getRicercaEditCollTitolo(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket) throws RemoteException, ValidationException;
	public AreaDatiVariazioneReturnVO gestioneLegameTitEdit(AreaDatiLegameTitoloVO areaDatiLegameTitoloVO, String ticket) throws RemoteException, ValidationException;;



	public boolean  inserisciFornitore(FornitoreVO fornitore) throws RemoteException, ValidationException;
	public boolean  modificaFornitore(FornitoreVO fornitore) throws RemoteException, ValidationException;
	public boolean  cancellaFornitore(FornitoreVO fornitore) throws RemoteException, ValidationException;
	public List<BilancioVO> getRicercaListaBilanci(ListaSuppBilancioVO ricercaBilanci) throws RemoteException, ValidationException;
	public boolean  inserisciBilancio(BilancioVO bilancio) throws RemoteException, ValidationException;
	public boolean  modificaBilancio(BilancioVO bilancio)  throws RemoteException, ValidationException;
	public boolean  cancellaBilancio(BilancioVO bilancio) throws RemoteException, ValidationException;
	public List<SezioneVO> getRicercaListaSezioni(ListaSuppSezioneVO ricercaSezioni) throws RemoteException, ValidationException;
	public boolean  inserisciSezione(SezioneVO sezione) throws RemoteException, ValidationException;
	public boolean  modificaSezione(SezioneVO sezione) throws RemoteException, ValidationException;
	public boolean  cancellaSezione(SezioneVO sezione) throws RemoteException, ValidationException;
	public List<BuoniOrdineVO> getRicercaListaBuoniOrd(ListaSuppBuoniOrdineVO ricercaBuoniOrd) throws RemoteException, ValidationException;
	public boolean  inserisciBuonoOrd(BuoniOrdineVO buonoOrd) throws RemoteException, ValidationException;
	public boolean  modificaBuonoOrd(BuoniOrdineVO buonoOrd) throws RemoteException, ValidationException;
	public boolean  cancellaBuonoOrd(BuoniOrdineVO buonoOrd) throws RemoteException, ValidationException;
	public List<FatturaVO> getRicercaListaFatture(ListaSuppFatturaVO ricercaFatture) throws RemoteException, ValidationException;
	public boolean  inserisciFattura(FatturaVO fattura) throws RemoteException, ValidationException;
	public boolean  modificaFattura(FatturaVO fattura) throws RemoteException, ValidationException;
	public boolean  cancellaFattura(FatturaVO fattura) throws RemoteException, ValidationException;
	public List<StrutturaProfiloVO> getRicercaListaProfili(ListaSuppProfiloVO ricercaProfili) throws RemoteException, ValidationException;
	public boolean  inserisciProfilo(StrutturaProfiloVO profilo) throws RemoteException, ValidationException;
	public boolean  modificaProfilo(StrutturaProfiloVO profilo) throws RemoteException, ValidationException;
	public boolean  cancellaProfilo(StrutturaProfiloVO profilo) throws RemoteException, ValidationException;
	public List<ComunicazioneVO> getRicercaListaComunicazioni(ListaSuppComunicazioneVO ricercaComunicazioni) throws RemoteException, ValidationException;
	public boolean  inserisciComunicazione(ComunicazioneVO comunicazione) throws RemoteException, ValidationException;
	public boolean  modificaComunicazione(ComunicazioneVO comunicazione) throws RemoteException, ValidationException;
	public boolean  cancellaComunicazione(ComunicazioneVO comunicazione) throws RemoteException, ValidationException;
	public List<SuggerimentoVO> getRicercaListaSuggerimenti(ListaSuppSuggerimentoVO ricercaSuggerimenti) throws RemoteException, ValidationException;
	public boolean  inserisciSuggerimento(SuggerimentoVO suggerimento) throws RemoteException, ValidationException;
	public boolean  modificaSuggerimento(SuggerimentoVO suggerimento) throws RemoteException, ValidationException;
	public boolean  cancellaSuggerimento(SuggerimentoVO suggerimento) throws RemoteException, ValidationException;
	public boolean  modificaConfigurazione(ConfigurazioneBOVO configurazione) throws RemoteException, ValidationException;
	public ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws RemoteException, ValidationException;
	public List<DocumentoVO> getRicercaListaDocumenti(ListaSuppDocumentoVO ricercaDocumenti) throws RemoteException, ValidationException;
	public boolean  modificaDocumento(DocumentoVO documento) throws RemoteException, ValidationException;
	public List<GaraVO> getRicercaListaGare(ListaSuppGaraVO ricercaGare) throws RemoteException, ValidationException;
	public boolean  inserisciGara(GaraVO gara) throws RemoteException, ValidationException;
	public boolean  modificaGara(GaraVO gara) throws RemoteException, ValidationException;
	public boolean  cancellaGara(GaraVO gara) throws RemoteException, ValidationException;
	//public DescrittoreBloccoVO getRicercaListaOfferte(ListaSuppOffertaFornitoreVO ricercaOfferte, int nRec) throws RemoteException, ValidationException;
	public List<OffertaFornitoreVO> getRicercaListaOfferte(ListaSuppOffertaFornitoreVO ricercaOfferte) throws RemoteException, ValidationException;
	public  DescrittoreBloccoVO  gestBlock(String ticket,List listaRis,int numxpag) throws RemoteException;
	public  DescrittoreBloccoVO  caricaBlock(String ticket, String idLista,int numBlocco) throws RemoteException, ValidationException;
	public ConfigurazioneORDVO loadConfigurazioneOrdini(ConfigurazioneORDVO configurazioneORD)throws RemoteException, ValidationException;
	public boolean  modificaConfigurazioneOrdini(ConfigurazioneORDVO configurazioneORD) throws RemoteException, ValidationException;
	public List<CambioVO> getRicercaListaCambiHib(ListaSuppCambioVO ricercaCambi) throws RemoteException, ValidationException;
	public boolean  inserisciCambioHib(CambioVO cambio) throws  RemoteException, ValidationException;
	public boolean  cancellaCambioHib(CambioVO cambio) throws RemoteException, ValidationException;
	public boolean  modificaCambioHib(CambioVO cambio) throws RemoteException, ValidationException;
	public List<SezioneVO> getRicercaListaSezioniHib(ListaSuppSezioneVO ricercaSezioni) throws RemoteException, ValidationException;
	public boolean  inserisciSezioneHib(SezioneVO sezione) throws RemoteException, ValidationException;
	public boolean  modificaSezioneHib(SezioneVO sezione) throws RemoteException, ValidationException;
	public boolean  cancellaSezioneHib(SezioneVO sezione) throws RemoteException, ValidationException;
	public List<FornitoreVO> getRicercaListaFornitoriHib(ListaSuppFornitoreVO ricercaFornitori) throws RemoteException, ValidationException;
	public boolean  inserisciFornitoreHib(FornitoreVO fornitore) throws RemoteException, ValidationException;
	public boolean  modificaFornitoreHib(FornitoreVO fornitore) throws RemoteException, ValidationException;
	public boolean  cancellaFornitoreHib(FornitoreVO fornitore) throws RemoteException, ValidationException;
	public void  migrazioneCStoSBNWEBcateneRinnoviBis() throws RemoteException, ValidationException;

	public FornitoreVO getFornitore(String codPolo, String codBib, String codFornitore, String descr, String ticket) throws RemoteException, ValidationException, it.iccu.sbn.ejb.exception.DataException;

	//almaviva5_20121121 evolutive google
	public CommandResultVO invoke(CommandInvokeVO command) throws RemoteException, ValidationException, ApplicationException;

}
