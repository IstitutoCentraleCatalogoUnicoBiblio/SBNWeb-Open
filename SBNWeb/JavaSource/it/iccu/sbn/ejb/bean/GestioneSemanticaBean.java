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
package it.iccu.sbn.ejb.bean;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.AbstractStatelessSessionBean;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.AuthenticationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.C2_250;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriConfigType;
import it.iccu.sbn.ejb.model.unimarcmodel.SistemaClassificazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.GestioneSemantica;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.FormaNomeType;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
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
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.semantica.TbCodiciSogg;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

public class GestioneSemanticaBean extends AbstractStatelessSessionBean implements GestioneSemantica {


	private static final long serialVersionUID = 8607867510517267363L;

	private static Logger log = Logger.getLogger(GestioneSemantica.class);

	private Semantica semantica;
	private Profiler profiler;

	private Semantica getSemantica() throws Exception {
		if (semantica != null)
			return semantica;

		semantica = DomainEJBFactory.getInstance().getSemantica();
		return semantica;
	}

	private Profiler getProfiler() throws Exception {
		if (profiler != null)
			return profiler;

		profiler = DomainEJBFactory.getInstance().getProfiler();
		return profiler;
	}

	public void ejbCreate() {
		log.info("creato ejb");
	}

	public RicercaSoggettoListaVO ricercaSoggetti(
			RicercaComuneVO ricercaComune, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		RicercaSoggettoListaVO response = null;
		try {
			if (!ValidazioneDati.strIsNull(ricercaComune.getCodSoggettario())) {
				String soggUnimarc = CodiciProvider.SBNToUnimarc(
						CodiciType.CODICE_SOGGETTARIO, ricercaComune
								.getCodSoggettario());
				if (ValidazioneDati.strIsNull(soggUnimarc))
					throw new ValidationException("Parametro codice soggettario errato");

				List<TB_CODICI> listaSogg = getCodiciSoggettario(true, ticket);
				if (!ValidazioneDati.isFilled(listaSogg))
					throw new ValidationException("Parametro codice soggettario errato");

				for (TB_CODICI sogg : listaSogg) {
					TbCodiciSogg s = new TbCodiciSogg(sogg);
					if (s.getCd_unimarc().trim().equals(soggUnimarc) )
						if (s.isSoloLocale() && ricercaComune.isIndice())
							throw new ValidationException("Soggettario gestito dalla biblioteca solo in Locale");
				}

			}

			RicercaComuneVO ricercaComuneEjb = ricercaComune.copy();
			response = this.getSemantica().ricercaSoggetti(ricercaComuneEjb, ticket);

		} catch (ValidationException ve) {
			log.error("", ve);
			throw ve;

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception e) {
			log.error("", e);
			log.error("Errore nella richiesta al Server SBN", e);
			throw new InfrastructureException(e.getMessage());
		}
		return response;
	}

	public CreaVariaDescrittoreVO creaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaDescrittoreVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenza())
				|| legame.getDidPartenza().length() != 10) {
			throw new ValidationException("Parametro Did non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidArrivo())
				|| legame.getDidArrivo().length() != 10) {
			throw new ValidationException(
					"Parametro Did arrivo legame non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaFormaNome())) {
			throw new ValidationException(
					"Parametro forma primo Descrittore non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidArrivoFormaNome())) {
			throw new ValidationException(
					"Parametro forma secondo Descrittore non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		if ((legame.getDidPartenzaFormaNome().equals("A") && legame
				.getDidArrivoFormaNome().equals("A"))
				&& (legame.getTipoLegame().equals("UF") || legame
						.getTipoLegame().equals("USE"))) {
			throw new ValidationException(
					"Il legame è possibile unicamente tra un descrittore in forma accettata e le sue forme varianti");
		}

		if ((legame.getDidPartenzaFormaNome().equals("A") && legame
				.getDidArrivoFormaNome().equals("R"))
				&& (legame.getTipoLegame().equals("BT")
						|| legame.getTipoLegame().equals("RT") || legame
						.getTipoLegame().equals("NT"))) {
			throw new ValidationException(
					"Il legame è possibile unicamente tra due descrittori in forma accettata");
		}

		if ((legame.getDidPartenzaFormaNome().equals("A") && legame
				.getDidArrivoFormaNome().equals("R"))
				&& (legame.getTipoLegame().equals("USE"))) {
			throw new ValidationException(
					"Il descrittore di partenza e' una forma accettata. Non è possibile fare un legame di tipo US con un altro descrittore");
		}

		if ((legame.getDidPartenzaFormaNome().equals("R") && legame
				.getDidArrivoFormaNome().equals("A"))
				&& (legame.getTipoLegame().equals("UF"))) {
			throw new ValidationException(
					"Il descrittore di partenza e' una forma di rinvio. Non è possibile fare un legame di tipo UF con un altro descrittore");
		}

		if ((legame.getDidPartenzaFormaNome().equals("R") && legame
				.getDidArrivoFormaNome().equals("A"))
				&& (legame.getTipoLegame().equals("BT")
						|| legame.getTipoLegame().equals("RT") || legame
						.getTipoLegame().equals("NT"))) {
			throw new ValidationException(
					"Il legame è possibile unicamente tra due descrittori in forma accettata");
		}

		try {
			legameOut = this.getSemantica().creaLegameDescrittori(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public CreaVariaDescrittoreVO scambioFormaDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaDescrittoreVO legameOut = null;

		if (legame == null)
			throw new ValidationException("Parametri di Input assenti");

		if (ValidazioneDati.strIsNull(legame.getDidPartenza())
				|| legame.getDidPartenza().length() != 10)
			throw new ValidationException("Parametro Did non valido");

		if (ValidazioneDati.strIsNull(legame.getDidArrivo())
				|| legame.getDidArrivo().length() != 10)
			throw new ValidationException("Parametro Did arrivo legame non valido");

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaLivelloAut()))
			throw new ValidationException("Parametro Livello autorità non valido");

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaFormaNome()))
			throw new ValidationException("Parametro forma primo Descrittore non valido");

		if (ValidazioneDati.strIsNull(legame.getDidArrivoFormaNome()))
			throw new ValidationException("Parametro forma secondo Descrittore non valido");

		if (!legame.isLivelloPolo()) {
			throw new ValidationException("La funzione è disponibile solo a livello di Polo");
		}

		try {
			legameOut = this.getSemantica().scambioFormaDescrittori(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public CreaVariaDescrittoreVO modificaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaDescrittoreVO legameOut = null;

		if (legame == null)
			throw new ValidationException("Parametri di Input assenti");

		if (ValidazioneDati.strIsNull(legame.getDidPartenza())
				|| legame.getDidPartenza().length() != 10)
			throw new ValidationException("Parametro Did non valido");

		if (ValidazioneDati.strIsNull(legame.getDidArrivo())
				|| legame.getDidArrivo().length() != 10)
			throw new ValidationException(
					"Parametro Did arrivo legame non valido");

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaFormaNome())) {
			throw new ValidationException(
					"Parametro forma primo Descrittore non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidArrivoFormaNome())) {
			throw new ValidationException(
					"Parametro forma secondo Descrittore non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		if ((legame.getDidPartenzaFormaNome().equals("A") && legame
				.getDidArrivoFormaNome().equals("A"))
				&& (legame.getTipoLegame().equals("UF") || legame
						.getTipoLegame().equals("USE"))) {
			throw new ValidationException(
					"Il legame è possibile unicamente tra un descrittore in forma accettata e le sue forme varianti");
		}

		if ((legame.getDidPartenzaFormaNome().equals("A") && legame
				.getDidArrivoFormaNome().equals("R"))
				&& (legame.getTipoLegame().equals("BT")
						|| legame.getTipoLegame().equals("RT") || legame
						.getTipoLegame().equals("NT"))) {
			throw new ValidationException(
					"Il legame è possibile unicamente tra due descrittori in forma accettata");
		}

		if ((legame.getDidPartenzaFormaNome().equals("R") && legame
				.getDidArrivoFormaNome().equals("A"))
				&& (legame.getTipoLegame().equals("BT")
						|| legame.getTipoLegame().equals("RT") || legame
						.getTipoLegame().equals("NT"))) {
			throw new ValidationException(
					"Il legame è possibile unicamente tra due descrittori in forma accettata");
		}

		try {
			legameOut = this.getSemantica()
					.modificaLegameDescrittori(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public CreaVariaDescrittoreVO cancellaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaDescrittoreVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenza())
				|| legame.getDidPartenza().length() != 10) {
			throw new ValidationException("Parametro Did non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidArrivo())
				|| legame.getDidArrivo().length() != 10) {
			throw new ValidationException(
					"Parametro Did arrivo legame non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidPartenzaFormaNome())) {
			throw new ValidationException(
					"Parametro forma primo Descrittore non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDidArrivoFormaNome())) {
			throw new ValidationException(
					"Parametro forma secondo Descrittore non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		try {
			legameOut = this.getSemantica()
					.cancellaLegameDescrittori(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloSoggettoVO creaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloSoggettoVO legameOut = null;

		if (legame == null || !ValidazioneDati.isFilled(legame.getLegami()) ) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		/*
		//almaviva5_20140203
		if (ValidazioneDati.equals(legame.getBidTipoMateriale(), "U")) {
			throw new ValidationException(
					"Il legame è escluso per il materiale musicale ");
		}
		*/

		try {
			legameOut = this.getSemantica().creaLegameTitoloSoggetto(legame, ticket);
			if (!ValidazioneDati.in(legameOut.getEsitoType(),
					SbnMarcEsitoType.OK,
					SbnMarcEsitoType.LEGAME_ESISTENTE))
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloClasseVO creaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloClasseVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		try {
			legameOut = this.getSemantica().creaLegameTitoloClasse(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloSoggettoVO invioInIndiceLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloSoggettoVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		/*
		//almaviva5_20140203
		if (ValidazioneDati.equals(legame.getBidTipoMateriale(), "U")) {
			throw new ValidationException(
					"Il legame è escluso per il materiale musicale ");
		}
		*/

		try {
			legameOut = this.getSemantica().invioInIndiceLegameTitoloSoggetto(
					legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloClasseVO invioInIndiceLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloClasseVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		// if (ValidazioneDati.strIsNull(legame.getIdentificativoClasse())) {
		// throw new ValidationException(
		// "Parametro Identificativo classificazione non valido");
		// }

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			legameOut = this.getSemantica().invioInIndiceLegameTitoloClasse(legame,
					ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloSoggettoVO modificaInvioInIndiceLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloSoggettoVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		/*
		//almaviva5_20140203
		if (ValidazioneDati.equals(legame.getBidTipoMateriale(), "U")) {
			throw new ValidationException(
					"Il legame è escluso per il materiale musicale ");
		}
		*/

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			legameOut = this.getSemantica()
					.modificaInvioInIndiceLegameTitoloSoggetto(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloClasseVO modificaInvioInIndiceLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloClasseVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		// if (ValidazioneDati.strIsNull(legame.getIdentificativoClasse())) {
		// throw new ValidationException(
		// "Parametro Identificativo classificazione non valido");
		// }

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			legameOut = this.getSemantica().modificaInvioInIndiceLegameTitoloClasse(
					legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloSoggettoVO modificaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloSoggettoVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		/*
		//almaviva5_20140203
		if (ValidazioneDati.equals(legame.getBidTipoMateriale(), "U")) {
			throw new ValidationException(
					"Il legame è escluso per il materiale musicale ");
		}
		*/

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			legameOut = this.getSemantica().modificaLegameTitoloSoggetto(legame,
					ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloClasseVO modificaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloClasseVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		// if (ValidazioneDati.strIsNull(legame.getIdentificativoClasse())) {
		// throw new ValidationException(
		// "Parametro Identificativo classificazione non valido");
		// }

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		if (!legame.isLivelloPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			legameOut = this.getSemantica().modificaLegameTitoloClasse(legame,
					ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloSoggettoVO cancellaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloSoggettoVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		try {
			legameOut = this.getSemantica().cancellaLegameTitoloSoggetto(legame,
					ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public DatiLegameTitoloClasseVO cancellaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		DatiLegameTitoloClasseVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		// if (ValidazioneDati.strIsNull(legame.getIdentificativoClasse())) {
		// throw new ValidationException(
		// "Parametro Identificativo classificazione non valido");
		// }

		if (ValidazioneDati.strIsNull(legame.getBid())
				|| legame.getBid().length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getBidNatura())) {
			throw new ValidationException("Parametro Natura Titolo non valido");
		}

		try {
			legameOut = this.getSemantica().cancellaLegameTitoloClasse(legame,
					ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public CreaVariaDescrittoreVO creaLegameSoggettoDescrittore(
			CreaLegameSoggettoDescrittoreVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		RicercaSoggettoListaVO ricercaOut = null;
		CreaVariaDescrittoreVO didLegame = null;
		CreaVariaDescrittoreVO legameOut = null;

		if (legame == null)
			throw new ValidationException("Parametri di Input assenti");


		if (ValidazioneDati.strIsNull(legame.getCodSoggettario()))
			throw new ValidationException("Parametro Soggettario non valido");


		if (ValidazioneDati.strIsNull(legame.getCid())
				|| legame.getCid().length() != 10) {
			throw new ValidationException("Parametro Cid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getLivelloAutorita())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getTestoDescrittore())) {
			throw new ValidationException("Parametro Descrittore non valido");
		}

		if (!legame.isPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		try {
			RicercaComuneVO ricerca = new RicercaComuneVO();
			ricerca.setCodSoggettario(legame.getCodSoggettario());
			ricerca.setRicercaSoggetto(null);
			ricerca.getRicercaDescrittore().setTestoDescr(legame.getTestoDescrittore());
			ricerca.setOrdinamentoDescrittore(TipoOrdinamento.PER_TESTO);
			ricerca.setRicercaTipoD(TipoRicerca.STRINGA_ESATTA);

			ricerca.setPolo(legame.isPolo());
			ricercaOut = this.getSemantica().ricercaSoggetti(ricerca, ticket);
			if (!ValidazioneDati.in(ricercaOut.getEsitoType(),
					SbnMarcEsitoType.OK,
					SbnMarcEsitoType.NON_TROVATO))
				throw new EJBException(ricercaOut.getTestoEsito());

			int size = ValidazioneDati.size(ricercaOut.getRisultati());
			if (size > 1)
				throw new EJBException("Incongruenza sulla Base Dati: trovato più di un descrittore");
			else
			if (size == 0) {
				CreaVariaDescrittoreVO descrittore = new CreaVariaDescrittoreVO();

				descrittore.setCodiceSoggettario(legame.getCodSoggettario());
				descrittore.setEdizioneSoggettario(legame.getEdizioneSoggettario());
				descrittore.setLivelloAutorita(legame.getLivelloAutorita());
				descrittore.setNote(legame.getNotaLegame());
				descrittore.setTesto(legame.getTestoDescrittore());
				descrittore.setLivelloPolo(legame.isPolo());
				descrittore.setFormaNome("A");

				didLegame = this.getSemantica().creaDescrittoreManuale(
						descrittore, ticket);

				if (!didLegame.isEsitoOk())
					throw new EJBException(didLegame.getTestoEsito());

			} else {
				ElementoSinteticaDescrittoreVO didTrovato = (ElementoSinteticaDescrittoreVO) ricercaOut.getRisultati().get(0);
				didLegame = new CreaVariaDescrittoreVO();
				didLegame.setDid(didTrovato.getDid());
				didLegame.setTesto(didTrovato.getTermine());
			}

			legameOut = this.getSemantica().creaLegameSoggettoDescrittore(legame
					.getCid(), legame.getCategoriaSoggetto(), legame.getT005(),
					legame.getLivelloAutorita(), didLegame.getDid(), legame
							.getNotaLegame(), legame.isCondiviso(), legame
							.isPolo(), ticket);

			if (legameOut.getEsitoType() != SbnMarcEsitoType.OK)
				throw new EJBException(legameOut.getTestoEsito());

		} catch (ValidationException e) {
			throw e;

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		return legameOut;
	}

	public CreaVariaDescrittoreVO cancellaLegameSoggettoDescrittore(
			CreaLegameSoggettoDescrittoreVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		CreaVariaDescrittoreVO legameOut = null;

		if (legame == null) {
			throw new ValidationException("Parametri di Input assenti");
		}

		if (ValidazioneDati.strIsNull(legame.getCodSoggettario())) {
			throw new ValidationException("Parametro Soggettario non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getCid())
				|| legame.getCid().length() != 10) {
			throw new ValidationException("Parametro Cid non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getLivelloAutorita())) {
			throw new ValidationException(
					"Parametro Livello autorità non valido");
		}

		if (ValidazioneDati.strIsNull(legame.getDid())
				|| legame.getDid().length() != 10) {
			throw new ValidationException("Parametro Did non valido");
		}

		if (!legame.isPolo()) {
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");
		}

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			String soggUnimarc = CodiciProvider.SBNToUnimarc(
					CodiciType.CODICE_SOGGETTARIO, legame.getCodSoggettario());
			if (ValidazioneDati.strIsNull(soggUnimarc))
				throw new ValidationException(
						"Parametro codice soggettario errato");

			legameOut = this.getSemantica().cancellaLegameSoggettoDescrittore(legame
					.getCid(), legame.getCategoriaSoggetto(), legame.getT005(),
					legame.getLivelloAutorita(), legame.getDid(), legame
							.getNotaLegame(), legame.isCondiviso(), legame
							.isPolo(), ticket);

			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public CreaVariaDescrittoreVO creaDescrittoreManuale(
			CreaVariaDescrittoreVO descrittore, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		CreaVariaDescrittoreVO didOut = null;

		if (descrittore == null)
			throw new ValidationException("Parametri di Input assenti");

		descrittore.validate();

		if (!descrittore.isLivelloPolo())
			throw new ValidationException("La funzione è disponibile solo a livello di Polo");


		try {
			CreaVariaDescrittoreVO descrittoreEjb = (CreaVariaDescrittoreVO) descrittore.clone();
			didOut = this.getSemantica().creaDescrittoreManuale(descrittoreEjb,	ticket);

			if (!didOut.isEsitoOk())
				throw new EJBException(didOut.getTestoEsito());

		} catch (ValidationException e) {
			throw e;

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return didOut;
	}

	public RicercaSoggettoListaVO ricercaSoggettiPerDescrittore(
			RicercaComuneVO ricercaComune, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		RicercaSoggettoListaVO marcOut = null;
		try {
			RicercaComuneVO ricercaComuneEjb = (RicercaComuneVO) ricercaComune.clone();
			marcOut = this.getSemantica().ricercaSoggettiPerDescrittore(ricercaComuneEjb, ticket);

		} catch (DataException de) {
			throw de;

		} catch (ValidationException e) {
			throw e;

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		return marcOut;
	}

	public ContaSoggettiCollegatiVO contaSoggettiPerDidCollegato(
			boolean livelloPolo, String Did, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		ContaSoggettiCollegatiVO marcOut = null;
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().contaSoggettiPerDidCollegato(livelloPolo,
					Did, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public RicercaSoggettoListaVO ricercaSoggettiPerDidCollegato(
			boolean livelloPolo, String Did, int elementiBlocco, String ticket,
			TipoOrdinamento tipoOrdinamento,
			String edizione)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		RicercaSoggettoListaVO marcOut = null;
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().ricercaSoggettiPerDidCollegato(
					livelloPolo, Did, ticket, elementiBlocco, tipoOrdinamento, edizione);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public RicercaSoggettoListaVO ricercaDescrittoriPerDidCollegato(
			boolean livelloPolo, String Did, int elementiBlocco, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		RicercaSoggettoListaVO marcOut = null;
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().ricercaDescrittoriPerDidCollegato(
					livelloPolo, Did, ticket, elementiBlocco);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public AnaliticaSoggettoVO creaAnaliticaSoggettoPerCid(boolean livelloPolo,
			String codCid, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		AnaliticaSoggettoVO marcOut = null;

		if (ValidazioneDati.strIsNull(codCid) || codCid.length() != 10) {
			throw new ValidationException("Parametro Cid non valido");
		}
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().creaAnaliticaSoggettoPerCid(livelloPolo,
					codCid.toUpperCase(), ticket, true);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;

	}

	public AnaliticaSoggettoVO creaAnaliticaSoggettoPerDid(boolean livelloPolo,
			String codDid, int i, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		AnaliticaSoggettoVO marcOut = null;
		if (ValidazioneDati.strIsNull(codDid) || codDid.length() != 10) {
			throw new ValidationException("Parametro Did non valido");
		}
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().creaAnaliticaSoggettoPerDid(livelloPolo,
					codDid.toUpperCase(), i, ticket, true);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;

	}

	public SBNMarcCommonVO getNextBloccoSoggetti(SBNMarcCommonVO areaDatiPass,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {
		SBNMarcCommonVO marcOut = null;
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica()
					.getNextBloccoSoggetti(areaDatiPass, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;

	}

	public SBNMarcCommonVO getNextBloccoClassi(SBNMarcCommonVO areaDatiPass,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {
		SBNMarcCommonVO marcOut = null;
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().getNextBloccoClassi(areaDatiPass, ticket);
		} catch (ValidationException e) {
			throw e;
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;

	}

	public RicercaSoggettoListaVO getNextBloccoDescrittori(
			RicercaSoggettoListaVO areaDatiPass, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		RicercaSoggettoListaVO marcOut = null;
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().getNextBloccoDescrittori(areaDatiPass,
					ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaSoggettoVO creaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws EJBException, InfrastructureException,
			DataException, AuthenticationException, ValidationException {

		try {
			CreaVariaSoggettoVO richiestaEjb = richiesta.copy();
			CreaVariaSoggettoVO response = this.getSemantica().creaSoggetto(richiestaEjb, ticket);
			return response;

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (ValidationException e) {
			throw e;

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

	}

	public CreaVariaSoggettoVO importaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		CreaVariaSoggettoVO marcOut = null;

		if (ValidazioneDati.strIsNull(richiesta.getTesto())) {
			throw new ValidationException("Digitare il campo testo");
		}

		if (ValidazioneDati.strIsNull(richiesta.getLivello())) {
			throw new ValidationException(
					"Digitare il campo stato di controllo");
		}

		if (ValidazioneDati.strIsEmpty(richiesta.getTipoSoggetto())) {
			throw new ValidationException("Digitare il campo tipo di soggetto");
		}

		try {
			String soggUnimarc = CodiciProvider.SBNToUnimarc(
					CodiciType.CODICE_SOGGETTARIO, richiesta
							.getCodiceSoggettario());
			if (ValidazioneDati.strIsNull(soggUnimarc))
				throw new ValidationException(
						"Parametro codice soggettario errato");

			CreaVariaSoggettoVO richiestaEjb = (CreaVariaSoggettoVO) richiesta
					.clone();
			richiestaEjb.setCodiceSoggettario(soggUnimarc);
			marcOut = this.getSemantica().importaSoggetto(richiestaEjb, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaSoggettoVO importaSoggettoConDescrittori(
			CreaVariaSoggettoVO richiesta, TreeElementViewSoggetti reticolo,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		CreaVariaSoggettoVO marcOut = null;
		if (ValidazioneDati.strIsNull(richiesta.getTesto())) {
			throw new ValidationException("Digitare il campo testo");
		}

		if (ValidazioneDati.strIsNull(richiesta.getLivello())) {
			throw new ValidationException("Digitare il campo stato di controllo");
		}

		if (ValidazioneDati.strIsEmpty(richiesta.getTipoSoggetto())) {
			throw new ValidationException("Digitare il campo tipo di soggetto");
		}

		if (reticolo == null)// || reticolo.getChildren().size() < 1)
			throw new ValidationException("Reticolo soggetto non valido");

		try {

			CreaVariaSoggettoVO richiestaEjb = (CreaVariaSoggettoVO) richiesta.clone();
			marcOut = this.getSemantica().importaSoggettoConDescrittori(richiestaEjb, reticolo, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaSoggettoVO catturaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		CreaVariaSoggettoVO marcOut = null;

		try {

			String soggUnimarc = CodiciProvider.SBNToUnimarc(
					CodiciType.CODICE_SOGGETTARIO, richiesta
							.getCodiceSoggettario());
			if (ValidazioneDati.strIsNull(soggUnimarc))
				throw new ValidationException(
						"Parametro codice soggettario errato");
			CreaVariaSoggettoVO richiestaEjb = (CreaVariaSoggettoVO) richiesta
					.clone();
			richiestaEjb.setCodiceSoggettario(soggUnimarc);
			marcOut = this.getSemantica().catturaSoggetto(richiestaEjb, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public AreaDatiAccorpamentoReturnVO trascinaTitoliTraSoggetti(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		AreaDatiAccorpamentoReturnVO marcOut = null;
		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().trascinaTitoliTraSoggetti(livelloPolo,
					area, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public AreaDatiAccorpamentoReturnVO fondiSoggetti(boolean livelloPolo,
			AreaDatiAccorpamentoVO area, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		AreaDatiAccorpamentoReturnVO marcOut = null;
		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().fondiSoggetti(livelloPolo, area, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public AreaDatiAccorpamentoReturnVO richiestaAccorpamentoSoggetti(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		AreaDatiAccorpamentoReturnVO marcOut = null;
		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().richiestaAccorpamentoSoggetti(livelloPolo,
					area, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaSoggettoVO variaSoggetto(CreaVariaSoggettoVO soggetto,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		try {
			CreaVariaSoggettoVO soggettoEjb = soggetto.copy();
			CreaVariaSoggettoVO response = this.getSemantica().variaSoggetto(soggettoEjb, ticket);
			return response;

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (ValidationException e) {
			throw e;

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

	}

	public CreaVariaDescrittoreVO variaDescrittore(
			CreaVariaDescrittoreVO descrittore, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		CreaVariaDescrittoreVO marcOut = null;
		descrittore.validate();

		try {
			String soggUnimarc = CodiciProvider.SBNToUnimarc(
					CodiciType.CODICE_SOGGETTARIO, descrittore
							.getCodiceSoggettario());
			if (ValidazioneDati.strIsNull(soggUnimarc))
				throw new ValidationException("Parametro codice soggettario errato");
			CreaVariaDescrittoreVO descrittoreEjb = (CreaVariaDescrittoreVO) descrittore.clone();
			descrittoreEjb.setCodiceSoggettario(soggUnimarc);
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			marcOut = this.getSemantica().variaDescrittore(descrittoreEjb, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public boolean cancellaSoggettoDescrittore(boolean livelloPolo, String xid,
			String ticket, SbnAuthority authority) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		boolean result = false;
		if (xid == null || xid.length() != 10)
			throw new ValidationException("Parametro errato");

		xid = xid.toUpperCase();

		try {
			result = this.getSemantica().cancellaSoggettoDescrittore(livelloPolo, xid, ticket, authority);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return result;
	}

	public CatSemSoggettoVO ricercaSoggettiPerBidCollegato(boolean livelloPolo,
			String bid, String codSoggettario, int elementiBlocco, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		if (bid == null || bid.length() != 10)
			throw new ValidationException("Parametro errato");

		String codSoggUnimarc = null;

		CatSemSoggettoVO result;
		try {
			if (ValidazioneDati.isFilled(codSoggettario))
				codSoggUnimarc = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_SOGGETTARIO,	codSoggettario);
			bid = bid.toUpperCase();

			result = this.getSemantica().ricercaSoggettiPerBidCollegato(livelloPolo,
					bid, codSoggUnimarc, ticket, elementiBlocco);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		result.setCodSoggettario(codSoggettario);
		return result;
	}

	public RicercaClasseListaVO cercaClassi(RicercaClassiVO datiRicerca,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		try {
			if (ValidazioneDati
					.strIsNull(datiRicerca.getIdentificativoClasse())
					&& ValidazioneDati.strIsNull(datiRicerca.getSimbolo())
					&& ValidazioneDati.strIsNull(datiRicerca.getParole())) {
				throw new ValidationException(
						"Digitare almeno un canale di ricerca primaria");
			}

			// if ((datiRicerca.getIdentificativoClasse() != null && datiRicerca
			// .getIdentificativoClasse().length() > 0)
			// // && ((datiRicerca.getCodEdizioneDewey() != null &&
			// // datiRicerca
			// // .getDescEdizioneDewey().length() > 0)
			// && ((datiRicerca.getLivelloAutoritaA() != null && datiRicerca
			// .getLivelloAutoritaA().length() > 0)
			// || (datiRicerca.getLivelloAutoritaDa() != null && datiRicerca
			// .getLivelloAutoritaDa().length() > 0)
			// // || (datiRicerca.getOrdinamentoClasse()!= null &&
			// // datiRicerca
			// // .getOrdinamentoClasse().length() > 0)
			// || (datiRicerca.getParole() != null && datiRicerca
			// .getParole().length() > 0) || (datiRicerca
			// .getSimbolo() != null && datiRicerca.getSimbolo()
			// .length() > 0))) {
			// throw new ValidationException(
			// "Digitare un solo canale di ricerca primario");
			// }

			if (((datiRicerca.getCodSistemaClassificazione() != null && datiRicerca
					.getCodSistemaClassificazione().length() > 0) && (datiRicerca
					.getParole() != null && datiRicerca.getParole().length() > 0))
					&& (datiRicerca.getIdentificativoClasse() != null && datiRicerca
							.getIdentificativoClasse().length() > 0)) {
				throw new ValidationException(
						"Digitare un solo canale di ricerca primario");
			}

			// if (((datiRicerca.getCodSistemaClassificazione() != null &&
			// datiRicerca
			// .getCodSistemaClassificazione().length() > 0) && (datiRicerca
			// .getSimbolo() != null && datiRicerca.getSimbolo().length() > 0))
			// && (datiRicerca.getIdentificativoClasse() != null && datiRicerca
			// .getIdentificativoClasse().length() > 0)) {
			// throw new ValidationException(
			// "Digitare un solo canale di ricerca primario");
			// }

			if (!datiRicerca.isRicercaPerImporta()
					&& !ValidazioneDati.strIsNull(datiRicerca.getParole())) {
				String[] parole = datiRicerca.getParole().split("\\s+");
				if (parole.length > 4)
					throw new ValidationException(
							"Digitare al massimo quattro parole");
			}

		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			throw new EJBException(e.getMessage());
		}

		RicercaClassiVO tmp = datiRicerca.copy();

		RicercaClasseListaVO result;
		try {
			result = this.getSemantica().cercaClassi(tmp, ticket);
		} catch (ValidationException e) {
			throw e;
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		return result;
	}

	public CatSemClassificazioneVO ricercaClassiPerBidCollegato(
			boolean livelloPolo, String bid, String codClassificazione,
			String codEdizione, int elementiBlocco, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		if (ValidazioneDati.length(bid) != 10)
			throw new ValidationException("Parametro bid errato");

		CatSemClassificazioneVO result;
		try {
			bid = bid.toUpperCase();
			result = this.getSemantica().ricercaClassiPerBidCollegato(livelloPolo,
					bid, codClassificazione, codEdizione, ticket,
					elementiBlocco);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		return result;
	}

	// public CatSemClassificazioneVO ricercaClassiPerBidCollegato(
	// boolean livelloPolo, String bid, int elementiBlocco, String ticket)
	// throws EJBException, InfrastructureException, DataException,
	// ValidationException {
	//
	// if (bid == null || bid.length() != 10)
	// throw new ValidationException("Parametro bid errato");
	//
	//
	//
	// CatSemClassificazioneVO result;
	// try {
	// bid = bid.toUpperCase();
	// // // SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
	// result = this.semantica.ricercaClassiPerBidCollegato(livelloPolo,
	// bid, ticket, elementiBlocco);
	// } catch (DAOException de) {
	// throw new DataException(de.getMessage());
	//
	// } catch (Exception ve) {
	// log.error("", ve);
	// logger.error("Errore nella richiesta al Server SBN", ve);
	// throw new InfrastructureException(ve.getMessage());
	// }
	//
	// return result;
	// }

	public RicercaAbstractListaVO ricercaAbstractPerBidCollegato(
			RicercaAbstractVO datiRicerca, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		if (datiRicerca.getBid() == null || datiRicerca.getBid().length() != 10)
			throw new ValidationException("Parametro bid errato");

		RicercaAbstractVO tmp = (RicercaAbstractVO) datiRicerca.clone();

		tmp.setBid(tmp.getBid().toUpperCase());

		RicercaAbstractListaVO result;

		try {
			result = this.getSemantica().ricercaAbstractPerBidCollegato(tmp, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		return result;
	}

	public CreaVariaClasseVO creaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		CreaVariaClasseVO marcOut = null;
		richiesta.validate();

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			marcOut = this.getSemantica().creaClasse(richiesta, ticket);
			if (!marcOut.isEsitoOk()) {

			}
		} catch (ValidationException e) {
				throw e;
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaClasseVO importaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		CreaVariaClasseVO marcOut = null;

		if (richiesta.getLivello() == null
				|| richiesta.getLivello().length() <= 0) {
			throw new ValidationException(
					"Digitare il campo stato di controllo");
		}

		if (richiesta.getSimbolo() == null
				|| richiesta.getSimbolo().length() <= 0) {
			throw new ValidationException("Digitare il campo simbolo");
		}

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			marcOut = this.getSemantica().importaClasse(richiesta, ticket);
			if (!marcOut.isEsitoOk()) {

			}
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaClasseVO catturaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		CreaVariaClasseVO marcOut = null;

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			marcOut = this.getSemantica().catturaClasse(richiesta, ticket);
			if (!marcOut.isEsitoOk()) {

			}
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaClasseVO variaClasse(CreaVariaClasseVO classe, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		CreaVariaClasseVO marcOut = null;

		classe.validate();

		try {
			// se non ho il timestamp assegnato devo richiedere il dettaglio
			if (ValidazioneDati.strIsNull(classe.getT005()))
				classe = this.getSemantica().analiticaClasse(classe.isLivelloPolo(),
						classe.getSimbolo(), ticket);

			marcOut = this.getSemantica().variaClasse(classe, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public AreaDatiAccorpamentoReturnVO trascinaTitoliTraClassi(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		AreaDatiAccorpamentoReturnVO marcOut = null;
		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().trascinaTitoliTraClassi(livelloPolo, area,
					ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public boolean cancellaClasse(boolean livelloPolo, String xid, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		boolean result = false;
		if (xid == null)
			throw new ValidationException("Parametro errato");

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			result = this.getSemantica().cancellaClasse(livelloPolo, xid, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return result;
	}

	public AreaDatiAccorpamentoReturnVO richiestaAccorpamentoClassi(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		AreaDatiAccorpamentoReturnVO marcOut = null;
		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().richiestaAccorpamentoClassi(livelloPolo,
					area, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaClasseVO analiticaClasse(boolean livelloPolo,
			String simbolo, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaClasseVO classe = null;

		if (ValidazioneDati.strIsNull(simbolo)) {
			throw new ValidationException("Digitare il campo simbolo");
		}

		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			classe = this.getSemantica().analiticaClasse(livelloPolo, simbolo, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return classe;
	}

	// THESAURO

	public RicercaThesauroListaVO ricercaThesauro(
			ThRicercaComuneVO ricercaComune, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		RicercaThesauroListaVO marcOut = null;
		try {
			ricercaComune.validate();
			ThRicercaComuneVO ricercaComuneEjb = (ThRicercaComuneVO) ricercaComune
					.clone();
			marcOut = this.getSemantica().ricercaThesauro(ricercaComuneEjb, ticket);

		} catch (ValidationException ve) {
			log.error("", ve);
			throw ve;

		} catch (DAOException de) {
			log.error("", de);
			throw new DataException(de.getMessage());

		} catch (Exception e) {
			log.error("", e);
			log.error("Errore nella richiesta al Server SBN", e);
			throw new InfrastructureException(e.getMessage());
		}
		return marcOut;
	}

	public CatSemThesauroVO ricercaTerminiPerBidCollegato(boolean livelloPolo,
			String bid, String codThesauro, int elementiBlocco, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		if (bid == null || bid.length() != 10)
			throw new ValidationException("Parametro errato");

		CatSemThesauroVO result;
		try {
			bid = bid.toUpperCase();

			result = this.getSemantica().ricercaTerminiPerBidCollegato(livelloPolo,
					bid, codThesauro, ticket, elementiBlocco);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		result.setCodThesauro(codThesauro);
		return result;
	}

	public RicercaThesauroListaVO ricercaTerminiPerDidCollegato(
			boolean livelloPolo, String ticket, String did, int elementiBlocco)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		if (ValidazioneDati.strIsNull(did) || did.length() != 10)
			throw new ValidationException("Parametro errato");

		RicercaThesauroListaVO result;
		try {
			did = did.toUpperCase();
			result = this.getSemantica().ricercaTerminiPerDidCollegato(livelloPolo,
					ticket, did, elementiBlocco);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

		return result;
	}

	// THESAURO

	public RicercaThesauroListaVO getNextBloccoTermini(
			RicercaThesauroListaVO areaDatiPass, String ticket)
			throws EJBException {
		RicercaThesauroListaVO marcOut = null;
		try {

			// // SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().getNextBloccoTermini(areaDatiPass, ticket);
		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
		}
		return marcOut;
	}

	public CreaVariaTermineVO gestioneTermineThesauro(
			CreaVariaTermineVO termine, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		CreaVariaTermineVO didOut = null;

		if (termine == null)
			throw new ValidationException("Parametri di Input assenti");

		termine.validate();

		if (!termine.isLivelloPolo())
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");

		if (termine.getOperazione() == null)
			throw new ValidationException(
					"Tipo operazione richiesta non valido");

		try {
			CreaVariaTermineVO termineEjb = termine.copy();
			didOut = this.getSemantica().gestioneTermineThesauro(termineEjb, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return didOut;
	}

	public AreaDatiAccorpamentoReturnVO fondiTerminiThesauro(
			boolean livelloPolo, DatiFusioneTerminiVO datiFusione, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		if (datiFusione == null || datiFusione.getDid1() == null
				|| datiFusione.getDid2() == null)
			throw new ValidationException("Parametri di Input assenti");

		if (datiFusione.getDid1().isRinvio()
				|| datiFusione.getDid2().isRinvio())
			throw new ValidationException(
					"Funzione abilitata solo per termini in forma accettata");

		AreaDatiAccorpamentoReturnVO marcOut = null;
		try {

			marcOut = this.getSemantica().fondiTerminiThesauro(livelloPolo,
					datiFusione, ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaTermineVO gestioneLegameTerminiThesauro(
			DatiLegameTerminiVO legame, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaTermineVO legameOut = null;

		if (legame == null)
			throw new ValidationException("Parametri di Input assenti");

		DettaglioTermineThesauroVO did1 = legame.getDid1();
		DettaglioTermineThesauroVO did2 = legame.getDid2();

		if (did1 == null || did2 == null)
			throw new ValidationException("Parametri di Input assenti");

		if (ValidazioneDati.strIsNull(did1.getDid()))
			throw new ValidationException("Parametro Did partenza non valido");

		if (ValidazioneDati.strIsNull(did2.getDid()))
			throw new ValidationException(
					"Parametro Did arrivo legame non valido");

		if (ValidazioneDati.strIsNull(did1.getLivAut()))
			throw new ValidationException(
					"Parametro Livello autorità non valido");

		if (ValidazioneDati.strIsNull(did2.getLivAut()))
			throw new ValidationException(
					"Parametro forma primo termine non valido");

		if (ValidazioneDati.strIsNull(did2.getFormaNome()))
			throw new ValidationException(
					"Parametro forma secondo termine non valido");

		if (!legame.isLivelloPolo())
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");

		try {

			// testo forma nome e tipo legame
			String formaNome1 = did1.getFormaNome();
			String formaNome2 = did2.getFormaNome();
			String tipoLegame = legame.getTipoLegame();
			TB_CODICI codice = CodiciProvider.cercaCodice(tipoLegame,
					CodiciType.CODICE_TIPO_LEGAME_TERMINI_THESAURO,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			String cd_flg7 = codice.getCd_flg7();
			if (ValidazioneDati.strIsNull(cd_flg7))
				throw new ValidationException(
						"Il legame è possibile unicamente tra due termini in forma accettata");

			switch (FormaNomeType.fromString(cd_flg7)) {
			case ACCETTATA_ACCETTATA:
				if (!formaNome1.equals("A") || !formaNome1.equals(formaNome2))
					throw new ValidationException(
							"Il legame è possibile unicamente tra due termini in forma accettata");
				break;
			case ACCETTATA_RINVIO:
				if (!formaNome1.equals("A") || !formaNome2.equals("R"))
					throw new ValidationException(
							"Il legame è possibile unicamente tra un termine in forma accettata e le sue forme varianti");
				break;
			case RINVIO_ACCETTATA:
				if (!formaNome1.equals("R") || !formaNome2.equals("A"))
					throw new ValidationException(
							"Il legame è possibile unicamente tra un termine in forma di rinvio e la sua forma accettata");
				break;
			}

			legameOut = this.getSemantica().gestioneLegameTermini(legame, ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	}

	public AnaliticaThesauroVO creaAnaliticaThesauroPerDid(boolean livelloPolo,
			String codDid, int i, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		AnaliticaThesauroVO marcOut = null;
		if (ValidazioneDati.strIsNull(codDid) || codDid.length() != 10) {
			throw new ValidationException("Parametro Did non valido");
		}
		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			marcOut = this.getSemantica().creaAnaliticaThesauroPerDid(livelloPolo,
					codDid.toUpperCase(), i, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;

	}

	public SBNMarcCommonVO gestioneLegameTitoloTermini(
			DatiLegameTitoloTermineVO legame, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		SBNMarcCommonVO legameOut = null;

		if (legame == null)
			throw new ValidationException("Parametri di Input assenti");

		if (ValidazioneDati.strIsNull(legame.getBid()))
			throw new ValidationException("Parametro Bid non valido");

		if (ValidazioneDati.strIsNull(legame.getBidLivelloAut()))
			throw new ValidationException(
					"Parametro Livello autorità non valido");

		if (legame.getLegami().size() < 1)
			throw new ValidationException(
					"Dati arrivo del legame non impostati");

		if (ValidazioneDati.strIsNull(legame.getBidNatura()))
			throw new ValidationException("Parametro Natura Titolo non valido");

		if (!legame.isLivelloPolo())
			throw new ValidationException(
					"La funzione è disponibile solo a livello di Polo");

		//almaviva5_20130520 #5316
		if (!ValidazioneDati.in(legame.getBidNatura(), "M", "S", "W", "N", "R")) {
			throw new ValidationException(
					"Il legame è possibile unicamente per titoli con natura M, S, W, R o N");
		}

		/*
		//almaviva5_20140203
		if (ValidazioneDati.equals(legame.getBidTipoMateriale(), "U")) {
			throw new ValidationException(
					"Il legame è escluso per il materiale musicale ");
		}
		*/

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);

			legameOut = this.getSemantica().gestioneLegameTitoloTermini(legame,
					ticket);
			if (!legameOut.isEsitoOk())
				throw new EJBException(legameOut.getTestoEsito());

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return legameOut;
	} // gestioneLegameTitoloTermini

	// public RicercaThesauroListaVO ricercaTitoliPerTermine(
	// ThRicercaComuneVO ricercaComune, String ticket)
	// throws EJBException, InfrastructureException, DataException,
	// ValidationException {
	// RicercaThesauroListaVO marcOut = null;
	// try {
	//
	// String thesUnimarc = CodiciProvider.SBNToUnimarc(
	// CodiciType.CODICE_THESAURO, ricercaComune.getCodThesauro());
	// if (ValidazioneDati.strIsNull(thesUnimarc))
	// throw new ValidationException("Parametro errato");
	// ricercaComune.setCodThesauro(thesUnimarc);
	// marcOut = this.semantica.ricercaTitoliPerTermine(ricercaComune,
	// ticket);
	//
	// } catch (DAOException de) {
	// throw new DataException(de.getMessage());
	//
	// } catch (Exception ve) {
	// log.error("", ve);
	// logger.error("Errore nella richiesta al Server SBN", ve);
	// throw new InfrastructureException(ve.getMessage());
	// }
	//
	// return marcOut;
	// }

	// public RicercaThesauroListaVO ricercaTerminiPerDidCollegato(
	// boolean livelloPolo, String Did, int elementiBlocco, String ticket)
	// throws EJBException, InfrastructureException, DataException,
	// ValidationException {
	// RicercaThesauroListaVO marcOut = null;
	// try {
	//
	// // SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
	//
	// marcOut = this.semantica.ricercaTerminiPerDidCollegato(
	// livelloPolo, Did, ticket, elementiBlocco);
	// } catch (DAOException de) {
	// throw new DataException(de.getMessage());
	//
	// } catch (Exception ve) {
	// log.error("", ve);
	// logger.error("Errore nella richiesta al Server SBN", ve);
	// throw new InfrastructureException(ve.getMessage());
	// }
	// return marcOut;
	// }

	public CreaVariaAbstractVO creaLegameTitoloAbstract(
			CreaVariaAbstractVO richiesta, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaAbstractVO marcOut = null;
		richiesta.validate();

		try {

			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			marcOut = this.getSemantica()
					.creaLegameTitoloAbstract(richiesta, ticket);
			if (!marcOut.isEsitoOk()) {

			}
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public CreaVariaAbstractVO variaLegameTitoloAbstract(
			CreaVariaAbstractVO abstracto, String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {

		CreaVariaAbstractVO marcOut = null;

		abstracto.validate();

		try {

			marcOut = this.getSemantica().variaLegameTitoloAbstract(abstracto,
					ticket);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return marcOut;
	}

	public boolean cancellaLegameTitoloAbstract(String bid, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		boolean result = false;
		if (bid == null || bid.length() != 10) {
			throw new ValidationException("Parametro Bid non valido");
		}

		bid = bid.toUpperCase();

		try {
			// SemanticaDAO dao = DAOFactory.getDAO(SemanticaDAO.class);
			result = this.getSemantica().cancellaLegameTitoloAbstract(bid, ticket);
		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}
		return result;
	}

	public List<TB_CODICI> getCodiciSoggettario(boolean livelloPolo,
			String ticket) throws EJBException, InfrastructureException,
			DataException, ValidationException {

		List<TB_CODICI> output = new ArrayList<TB_CODICI>();
		try {
			ticket = livelloPolo ? ticket : null; // per indice non ha senso il ticket

			ParametriConfigType param = this.getProfiler().getParametriGenerali(ticket);
			if (param != null && param.getC2_250Count() > 0) {
				List<TB_CODICI> soggettari = CodiciProvider.getCodici(CodiciType.CODICE_SOGGETTARIO, true);

				for (C2_250 c250 : param.getC2_250()) {
					String soggProfilo = ValidazioneDati.trimOrEmpty(c250.getContent());
					if (ValidazioneDati.strIsNull(soggProfilo))
						continue;

					for (TB_CODICI soggettario : soggettari) {
						String soggUni = ValidazioneDati.trimOrEmpty(soggettario.getCd_unimarc());
						if (ValidazioneDati.strIsNull(soggUni))
							continue;

						//almaviva5_20130131 #5255 controllo per codici soggettario firenze in indice
						if (ValidazioneDati.in(soggProfilo, "FE", "FN", "FI") )
							soggProfilo = "FI";

						if (ValidazioneDati.equals(soggUni, soggProfilo) ) {
							// AGGIUNGO NELLA LISTA DI RITORNO
							TbCodiciSogg sogg = new TbCodiciSogg(soggettario);
							if (livelloPolo)	//almaviva5_20091111
								sogg.setSoloLocale(c250.getSololocale());
							else
								sogg.setSoloLocale(false);

							output.add(sogg);
							break;
						}
					}
				}
			}
			output.add(0, new TbCodiciSogg(new TB_CODICI("", "", "")));
		} catch (RemoteException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		}
		return output;
	}

	public List<TB_CODICI> getCodiciThesauro(String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		List<TB_CODICI> ret = new ArrayList<TB_CODICI>();
		try {
			ParametriConfigType param = this.getProfiler().getParametriGenerali(ticket);
			if (param.getC2_250Count() > 0) {
				List<TB_CODICI> thesauri = CodiciProvider.getCodici(CodiciType.CODICE_THESAURO, false);

				for (C2_250 c250 : param.getC2_250()) {
					String soggProfilo = ValidazioneDati.trimOrEmpty(c250.getContent());
					if (ValidazioneDati.strIsNull(soggProfilo))
						continue;

					for (TB_CODICI thesauro : thesauri) {
						if (ValidazioneDati.trimOrEmpty(thesauro.getCd_unimarc()).equals(soggProfilo)) {
							// AGGIUNGO NELLA LISTA DI RITORNO
							ret.add(thesauro);
							break;
						}
					}
				}
			}
			ret.add(0, new TB_CODICI("", "", ""));
		} catch (RemoteException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		}
		return ret;
	}

	public List<TB_CODICI> getSistemiClassificazione(String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		List<TB_CODICI> ret = new ArrayList<TB_CODICI>();
		try {
			ParametriConfigType param = this.getProfiler().getParametriGenerali(ticket);
			if (param.getSistemaClassificazioneCount() > 0) {
				List<TB_CODICI> listaSistCla = CodiciProvider.getCodici(CodiciType.CODICE_SISTEMA_CLASSE, false);

				for (SistemaClassificazione scProfilo : param.getSistemaClassificazione()) {
					String value = ValidazioneDati.trimOrEmpty(scProfilo.getContent());
					if (ValidazioneDati.strIsNull(value))
						continue;

					for (TB_CODICI sistCla : listaSistCla) {
						if (ValidazioneDati.trimOrEmpty(sistCla.getCd_unimarc()).equals(value)) {
							// AGGIUNGO NELLA LISTA DI RITORNO
							ret.add(sistCla);
							break;
						}
					}
				}
			}
			ret.add(0, new TB_CODICI("", "", ""));
		} catch (RemoteException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		}
		return ret;
	}

	public boolean isIndice(String cod_soggettario, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {
		try {
			// DEVO CONVERTIRE IL COD_SOGGETTARIO IN COD_UNIMARC
			return this.getProfiler().isSemanticaIndice(CodiciProvider.SBNToUnimarc(
					CodiciType.CODICE_SOGGETTARIO, cod_soggettario), ticket);
		} catch (RemoteException e) {
			log.error("", e);
			throw new InfrastructureException("Errore Interno al server");
		} catch (Exception e) {
			log.error("", e);
			throw new InfrastructureException("Errore Interno al server");
		}
	}

	public boolean isAutorizzatoSoggettario(String ticket) throws EJBException,
			InfrastructureException, DataException, ValidationException {
		boolean enable = false;
		try {
			if (!this.getProfiler()
					.isOkAttivitaUser(
							CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017,
							"0", SbnAuthority.SO, ticket))
				enable = true;
		} catch (RemoteException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		}
		return enable;

	}

	public boolean aggiornaDatiCondivisioneSoggetto(
			List<DatiCondivisioneSoggettoVO> datiCondivisione, String ticket)
			throws EJBException, InfrastructureException, DataException,
			ValidationException {

		try {
			getSemantica()
					.aggiornaDatiCondivisioneSoggetto(datiCondivisione, ticket);
			return true;

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

	}

	public boolean isDescrittoreAutomaticoPerAltriSoggetti(String ticket,
			String did) throws RemoteException, InfrastructureException,
			DataException, ValidationException {

		try {
			return getSemantica().isDescrittoreAutomaticoPerAltriSoggetti(ticket, did);

		} catch (DAOException de) {
			throw new DataException(de.getMessage());

		} catch (Exception ve) {
			log.error("", ve);
			log.error("Errore nella richiesta al Server SBN", ve);
			throw new InfrastructureException(ve.getMessage());
		}

	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			CommandResultVO invoke = this.getSemantica().invoke(command);
			if (invoke.getError() != null)
				return invoke;

			switch (invoke.getCommand()) {}

			return invoke;

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

}
