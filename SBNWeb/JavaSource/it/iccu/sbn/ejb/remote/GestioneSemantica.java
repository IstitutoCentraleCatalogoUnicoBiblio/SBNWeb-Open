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
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.CreaVariaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ContaSoggettiCollegatiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface GestioneSemantica extends EJBObject {

	public RicercaSoggettoListaVO ricercaSoggetti(
			RicercaComuneVO ricercaComune, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public RicercaSoggettoListaVO ricercaSoggettiPerDescrittore(
			RicercaComuneVO ricercaComune, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public AnaliticaSoggettoVO creaAnaliticaSoggettoPerCid(boolean livelloPolo,
			String codCid, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public AnaliticaSoggettoVO creaAnaliticaSoggettoPerDid(boolean livelloPolo,
			String codDid, int i, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public SBNMarcCommonVO getNextBloccoSoggetti(SBNMarcCommonVO areaDatiPass,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public RicercaSoggettoListaVO ricercaSoggettiPerDidCollegato(
			boolean livelloPolo, String Did, int elementiBlocco, String ticket, TipoOrdinamento tipoOrdinamento, String edizione)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public RicercaSoggettoListaVO ricercaDescrittoriPerDidCollegato(
			boolean livelloPolo, String Did, int elementiBlocco, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public ContaSoggettiCollegatiVO contaSoggettiPerDidCollegato(
			boolean livelloPolo, String Did, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public AreaDatiAccorpamentoReturnVO trascinaTitoliTraSoggetti(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public AreaDatiAccorpamentoReturnVO fondiSoggetti(boolean livelloPolo,
			AreaDatiAccorpamentoVO area, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public AreaDatiAccorpamentoReturnVO richiestaAccorpamentoSoggetti(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public RicercaSoggettoListaVO getNextBloccoDescrittori(
			RicercaSoggettoListaVO areaDatiPass, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public SBNMarcCommonVO getNextBloccoClassi(SBNMarcCommonVO areaDatiPass,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaSoggettoVO variaSoggetto(CreaVariaSoggettoVO soggetto,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaDescrittoreVO variaDescrittore(
			CreaVariaDescrittoreVO descrittore, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaSoggettoVO creaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, AuthenticationException, ValidationException;

	public CreaVariaSoggettoVO importaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaSoggettoVO importaSoggettoConDescrittori(
			CreaVariaSoggettoVO richiesta, TreeElementViewSoggetti reticolo,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaSoggettoVO catturaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public boolean cancellaSoggettoDescrittore(boolean livelloPolo, String xid,
			String ticket, SbnAuthority authority) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public RicercaClasseListaVO cercaClassi(RicercaClassiVO datiRicerca,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CatSemSoggettoVO ricercaSoggettiPerBidCollegato(boolean livelloPolo,
			String bid, String codSoggettario, int elementiBlocco, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CatSemClassificazioneVO ricercaClassiPerBidCollegato(
			boolean livelloPolo, String bid, String codClassificazione,
			String deweyEd, int elementiBlocco, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public RicercaAbstractListaVO ricercaAbstractPerBidCollegato(
			RicercaAbstractVO datiRicerca, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaClasseVO creaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaClasseVO importaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaClasseVO catturaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public CreaVariaClasseVO variaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public AreaDatiAccorpamentoReturnVO trascinaTitoliTraClassi(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public boolean cancellaClasse(boolean livelloPolo, String xid, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public AreaDatiAccorpamentoReturnVO richiestaAccorpamentoClassi(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaClasseVO analiticaClasse(boolean livelloPolo,
			String simbolo, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public CreaVariaDescrittoreVO creaLegameSoggettoDescrittore(
			CreaLegameSoggettoDescrittoreVO legame, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;;

	public CreaVariaDescrittoreVO creaDescrittoreManuale(
			CreaVariaDescrittoreVO descrittore, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;;

	public CreaVariaDescrittoreVO creaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaDescrittoreVO scambioFormaDescrittori(
			DatiLegameDescrittoreVO legame, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public DatiLegameTitoloSoggettoVO creaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloSoggettoVO invioInIndiceLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloSoggettoVO modificaInvioInIndiceLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloSoggettoVO modificaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloSoggettoVO cancellaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloClasseVO creaLegameTitoloClasse(DatiLegameTitoloClasseVO legame,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public DatiLegameTitoloClasseVO invioInIndiceLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloClasseVO modificaInvioInIndiceLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloClasseVO modificaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public DatiLegameTitoloClasseVO cancellaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public CreaVariaDescrittoreVO modificaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaDescrittoreVO cancellaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaDescrittoreVO cancellaLegameSoggettoDescrittore(
			CreaLegameSoggettoDescrittoreVO legame, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	// THESAURO
	public RicercaThesauroListaVO ricercaThesauro(
			ThRicercaComuneVO ricercaComune, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaTermineVO gestioneTermineThesauro(CreaVariaTermineVO termine,
			String ticket) throws RemoteException, InfrastructureException,
			DataException, ValidationException;

	public AreaDatiAccorpamentoReturnVO fondiTerminiThesauro(boolean livelloPolo,
			DatiFusioneTerminiVO datiFusione, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public CreaVariaTermineVO gestioneLegameTerminiThesauro(
			DatiLegameTerminiVO legame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public CatSemThesauroVO ricercaTerminiPerBidCollegato(boolean livelloPolo,
			String bid, String codThesauro, int elementiBlocco, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public RicercaThesauroListaVO ricercaTerminiPerDidCollegato(
			boolean livelloPolo, String ticket, String did, int elementiBlocco)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public RicercaThesauroListaVO getNextBloccoTermini(
			RicercaThesauroListaVO areaDatiPass, String ticket)
			throws RemoteException;

	public AnaliticaThesauroVO creaAnaliticaThesauroPerDid(boolean livelloPolo,
			String codDid, int i, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public SBNMarcCommonVO gestioneLegameTitoloTermini(
			DatiLegameTitoloTermineVO datiLegame, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	// ABSTRACT

	public CreaVariaAbstractVO creaLegameTitoloAbstract(
			CreaVariaAbstractVO richiesta, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public CreaVariaAbstractVO variaLegameTitoloAbstract(
			CreaVariaAbstractVO abstracto, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public boolean cancellaLegameTitoloAbstract(String bid, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public List getCodiciSoggettario(boolean livelloPolo, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public List getCodiciThesauro(String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public List getSistemiClassificazione(String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public boolean isIndice(String cod_soggettario, String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public boolean isAutorizzatoSoggettario(String ticket)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException;

	public boolean aggiornaDatiCondivisioneSoggetto(
			List<DatiCondivisioneSoggettoVO> datiCondivisione, String ticket) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public boolean isDescrittoreAutomaticoPerAltriSoggetti(String ticket,
			String did) throws RemoteException,
			InfrastructureException, DataException, ValidationException;

	public CommandResultVO invoke(CommandInvokeVO command) throws RemoteException, ApplicationException;

}
