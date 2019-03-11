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
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.AuthenticationException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioCancAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazionePropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiVariazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiVariazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioElenchiListeConfrontoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiScambiaResponsLegameTitAutVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJBObject;

public interface GestioneBibliografica extends EJBObject {

	SBNMarc eseguiRichiestaServerSBN(SBNMarc marc) throws RemoteException;

	// AREA RICHIESTA PROGRESSIVO SBN (Bid-Vid ...)
	public AreaDatiPassaggioGetIdSbnVO getIdSbn(
			AreaDatiPassaggioGetIdSbnVO areaDatiPass, String ticket) throws RemoteException;

	// AREA ACCORPAMENTO FRA OGGETTI
	public AreaDatiAccorpamentoReturnVO richiestaAccorpamento(
			AreaDatiAccorpamentoVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiAccorpamentoReturnVO richiestaSpostamentoLegami(
			AreaDatiAccorpamentoVO areaDatiPass, String ticket) throws RemoteException;

	// AREA LOCALIZZAZIONI
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO cercaLocalizzazioni(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass, boolean soloPresenzaPolo, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO localizzaAuthorityMultipla(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO localizzaUnicoXML(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO localizzaAuthority(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass, String ticket)
			throws RemoteException;

	// AREA CANCELLAZIONE AUTHORITY
	public AreaDatiPassaggioCancAuthorityVO cancellaAuthority(
			AreaDatiPassaggioCancAuthorityVO areaDatiPass, String ticket)
			throws RemoteException;

	// AREA CATTURA
	public AreaDatiVariazioneReturnVO catturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO catturaAutore(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO catturaMarca(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO catturaLuogo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO scatturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO inserisciReticoloCatturato(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
			throws RemoteException;


	public AreaTabellaOggettiDaCondividereVO ricercaDocumentoPerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass, String ticket)
		throws RemoteException;

	public AreaTabellaOggettiDaCondividereVO ricercaAutorePerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass, String ticket)
		throws RemoteException;

	// AREA TITOLI
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoli(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket)
			throws RemoteException;


	// AREA SCHEDE TITOLI

	public AreaDatiPassaggioElenchiListeConfrontoVO getElenchiListeConfronto(
			AreaDatiPassaggioElenchiListeConfrontoVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioSchedaDocCiclicaVO getSchedaDocCiclica(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioSchedaDocCiclicaVO insertTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioSchedaDocCiclicaVO cancellaTabelleTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket)
			throws RemoteException;

	// Modifica almaviva2 04.08.2010 - Gestione periodici nuovo servizio per ricavare, dato un bid,  tutti i legami
	// fra periodici sia verso l'alto che verso il basso
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLegamiFraPeriodici(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket)
			throws RemoteException;


	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoliPerGestionali(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, Locale locale, String ticket)
			throws RemoteException;


	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoTitoliPerBID(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoTitoli(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO inserisciTitolo (
			AreaDatiVariazioneTitoloVO areaDatiPass, String ticket) throws RemoteException,AuthenticationException;

	public AreaDatiVariazioneReturnVO inserisciLegameTitolo(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiVariazioneReturnVO trascinaLegameAutore(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiVariazioneReturnVO scambiaResponsabilitaLegameTitoloAutore(
			AreaDatiScambiaResponsLegameTitAutVO areaDatiPass, String ticket) throws RemoteException;


	// AREA AUTORI
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutori(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoAutorePerVid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoAutori(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO inserisciAutore(
			AreaDatiVariazioneAutoreVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutoriCollegati(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO collegaElementoAuthority(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiVariazioneReturnVO creaFormaRinvio(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiVariazioneReturnVO scambiaForma(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) throws RemoteException;

	// AREA MARCHE
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarche(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoMarchePerMid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoMarche(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiVariazioneReturnVO inserisciMarca(
			AreaDatiVariazioneMarcaVO areaDatiPass, String ticket) throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarcheCollegate(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLuoghi(
			AreaDatiPassaggioInterrogazioneLuogoVO areaDatiPass, String ticket)
	throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoLuoghi(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket)
	throws RemoteException;

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoLuoghiPerLid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket)
	throws RemoteException;


	public List getComboBibliotechePolo(String cd_polo, String ticket)
	throws RemoteException;

	public AreaDatiVariazioneReturnVO inserisciLuogo(
			AreaDatiVariazioneLuogoVO areaDatiPass, String ticket)
	throws RemoteException;


	public AllineaVO richiediListaAllineamenti(
			AllineaVO areaDatiPass, String ticket)
	throws RemoteException;

	public CatturaMassivaBatchVO catturaMassivaBatch(
			CatturaMassivaBatchVO areaDatiPass, String ticket)
	throws RemoteException;

	public AllineaVO allineaBaseLocale(
			AllineaVO areaDatiPass, String ticket)
	throws RemoteException;

	public AllineaVO allineamentoRepertoriDaIndice(String ticket)
	throws RemoteException;

	public AreaDatiPropostaDiCorrezioneVO cercaPropostaDiCorrezione(
			AreaDatiPropostaDiCorrezioneVO areaDatiPass, String ticket)
	throws RemoteException;

	public AreaDatiVariazionePropostaDiCorrezioneVO inserisciPropostaDiCorrezione(
			AreaDatiVariazionePropostaDiCorrezioneVO areaDatiPass, String ticket)
	throws RemoteException;


	public AreaParametriStampaSchedeVo schedulatorePassiStampaSchede(
			AreaParametriStampaSchedeVo areaDatiPass, String ticket, BatchLogWriter logger)
	throws RemoteException;

	public CommandResultVO invoke(CommandInvokeVO command) throws RemoteException, ApplicationException;
}

