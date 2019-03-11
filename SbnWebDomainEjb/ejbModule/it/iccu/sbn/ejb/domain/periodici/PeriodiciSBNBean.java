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
package it.iccu.sbn.ejb.domain.periodici;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.utils.isbd.IsbdTokenizer;
import it.iccu.sbn.ejb.utils.isbd.IsbdVO;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SerializableComparator;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaListaFascicoliDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaListaFascicoliVO;
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.InventariCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.RicercaPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.CatenaRinnoviOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.esame.RicercaKardexEsameBiblioPoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieEsemplareCollVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.MessaggioFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexIntestazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerAssociaInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerComunicazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerCreaOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerStampaOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.TipoRangeKardex;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_esemplare_titoloDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.periodici.EsamePeriodicoDAO;
import it.iccu.sbn.persistence.dao.periodici.PeriodiciDAO;
import it.iccu.sbn.persistence.dao.periodici.PrevisionaleDAO;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.periodici.Tbp_esemplare_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Tbp_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Tbp_modello_previsionale;
import it.iccu.sbn.polo.orm.periodici.Trp_messaggio_fascicolo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.util.periodici.PrevisionaleUtil;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.custom.periodici.FascicoloStampaDecorator;
import it.iccu.sbn.vo.custom.periodici.InventarioOrdineDecorator;
import it.iccu.sbn.vo.custom.periodici.KardexPeriodicoOpacVO;
import it.iccu.sbn.vo.custom.periodici.ModelloPrevisionaleDecorator;
import it.iccu.sbn.vo.custom.periodici.SerieOrdineDecorator;
import it.iccu.sbn.vo.validators.documentofisico.InventarioValidator;
import it.iccu.sbn.vo.validators.periodici.FascicoloValidator;
import it.iccu.sbn.vo.validators.periodici.PrevisionaleValidator;
import it.iccu.sbn.vo.validators.periodici.RicercaKardexPeriodicoValidator;
import it.iccu.sbn.vo.xml.binding.sbnwebws.CollocazioneType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.InventarioType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

public class PeriodiciSBNBean extends TicketChecker implements SessionBean {

	private static final long serialVersionUID = 7976077788913934319L;
	private static final Class<PeriodiciSBNBean> clazz = PeriodiciSBNBean.class;
	private static Logger log = Logger.getLogger(clazz);

	private static final Map<CommandType, Method> mappings;

	private static final void mapMethod(CommandType cmd, String methodName) throws Exception  {

		Class<?>[] signature = cmd.getSignature();
		if (ValidazioneDati.isFilled(signature)) {
			List<Class<?>> tmp = new ArrayList<Class<?>>();
			tmp.add(CommandInvokeVO.class);
			for (Class<?> c : signature)
				tmp.add(c);

			mappings.put(cmd, clazz.getDeclaredMethod(methodName, tmp.toArray(new Class[0])) );

		} else
			mappings.put(cmd, clazz.getDeclaredMethod(methodName, new Class[] {CommandInvokeVO.class}) );
	}

	static {
		mappings = new THashMap<CommandType, Method>();
		try {
			mapMethod(CommandType.PER_ESAME_SERIE_PERIODICO, "getEsameSeriePeriodico");
			mapMethod(CommandType.PER_KARDEX_PERIODICO, "getKardexPeriodico");
			mapMethod(CommandType.PER_DETTAGLIO_FASCICOLO, "getDettaglioFascicolo");
			mapMethod(CommandType.PER_AGGIORNA_FASCICOLO, "aggiornaFascicolo");
			mapMethod(CommandType.PER_RICEZIONE_FASCICOLO, "ricezioneFascicolo");
			mapMethod(CommandType.PER_LISTA_ORDINI, "getListaOrdiniPeriodico");
			mapMethod(CommandType.PER_ASSOCIA_FASCICOLI_INVENTARIO, "associaFascicoliInventario");
			mapMethod(CommandType.PER_ANNULLA_RICEZIONE_FASCICOLO, "annullaRicezioneFascicoli");
			mapMethod(CommandType.PER_LISTA_INVENTARI_COLLOCAZIONE, "getListaInventariDiCollocazione");
			mapMethod(CommandType.PER_RICEZIONE_MULTIPLA_ORDINE, "ricezioneMultiplaOrdine");
			mapMethod(CommandType.PER_CANCELLAZIONE_MULTIPLA_FASCICOLI, "cancellazioneMultiplaFascicoli");
			mapMethod(CommandType.PER_ASSOCIA_GRUPPO_INVENTARIO, "associaFascicoliGruppoInventario");
			mapMethod(CommandType.PER_KARDEX_ESAME_BIBLIO_POLO, "getEsameKardexBiblioPolo");
			mapMethod(CommandType.PER_CERCA_UNICO_INVENTARIO, "getUnicoInventario");
			mapMethod(CommandType.PER_LISTA_MODELLI_PREVISIONALE, "getListaModelliPrevisionale");
			mapMethod(CommandType.PER_CALCOLA_PREVISIONALE, "calcolaPrevisionale");
			mapMethod(CommandType.PER_VERIFICA_ESISTENZA_FASCICOLO, "verificaEsistenzaFascicolo");
			mapMethod(CommandType.PER_LISTA_BIBLIOTECHE_ESEMPLARE_FASCICOLO, "getListaBibliotecheEsemplareFascicolo");
			mapMethod(CommandType.PER_KARDEX_PERIODICO_OPAC, "getKardexPeriodicoPerOPAC");
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private Interrogazione interrogazione;
	private SessionContext ctx;

	private Interrogazione getInterrogazione() throws Exception {
		if (interrogazione != null)
			return interrogazione;

		this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();

		return interrogazione;
	}

	public void ejbCreate() {
		log.debug("creato ejb");
		return;
	}

	public void ejbActivate() {
		return;
	}

	public void ejbPassivate() {
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
		return;
	}

	public void unsetSessionContext() {
		return;
	}

	public void ejbRemove() {
		return;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ValidationException, ApplicationException {
		try {
			if (command == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

			command.validate();
			String ticket = command.getTicket();
			checkTicket(ticket);

			CommandType cmd = command.getCommand();
			Method method = mappings.get(cmd);
			if (method == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_INVALID_COMMAND, cmd.name());

			log.debug("invoke command: " + cmd + ", method: " + method.getName() + "()");

			Serializable[] params = command.getParams();
			if (ValidazioneDati.isFilled(params)) {
				List<Object> tmp = new ArrayList<Object>();
				tmp.add(command);
				for (Serializable p : params)
					tmp.add(p);

				return CommandResultVO.build(command, (Serializable) method.invoke(this, tmp.toArray(new Object[0]) ), null);
			}

			return CommandResultVO.build(command, (Serializable) method.invoke(this, command), null);

		} catch	(ApplicationException e) {
			throw e;

		} catch (ValidationException e) {
			throw e;

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);

		} catch (InvocationTargetException e) {
			Throwable t = e.getCause();
			log.error("", t);
			ctx.setRollbackOnly();
			if (t instanceof SbnBaseException)
				return CommandResultVO.build(command, null, t);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, t));

		} catch (Exception e) {
			log.error("", e);
			ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}

	}

	protected EsameSeriePeriodicoVO getEsameSeriePeriodico(CommandInvokeVO invoke, RicercaPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		EsameSeriePeriodicoVO output = new EsameSeriePeriodicoVO();
		String bid = richiesta.getBid();
		output.setBid(bid);

		//0.dati reticolo da gest. bibliografica
		AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
		areaDatiPass.setOggDiRicerca(bid);
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaReturn = getInterrogazione().ricercaLegamiFraPeriodici(areaDatiPass, invoke.getTicket() );
		if (areaReturn == null || ValidazioneDati.equals(areaReturn.getCodErr(), "9999"))
			throw new ApplicationException(SbnErrorTypes.GB_ERRORE_PROTOCOLLO, areaReturn.getTestoProtocollo());
		output.setIsbd(areaReturn.getStringDaProsp());

		List<ElementoSeriePeriodicoVO> listaConSerie = new ArrayList<ElementoSeriePeriodicoVO>();

		EsamePeriodicoDAO edao = new EsamePeriodicoDAO();

		edao.cercaEsamePeriodicoPerBiblioteca(richiesta, listaConSerie);
		BaseVO.sortAndEnumerate(listaConSerie, ElementoSeriePeriodicoVO.ORDINAMENTO_SERIE_PERIODICO );
		output.setSerie(listaConSerie);

		//periodicità
		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo t = tdao.getTitolo(bid);
		if (t != null) {
			PeriodiciDAO dao = new PeriodiciDAO();
			//dao.testInsertMassivo(bid, 1876, 50000);
			if (ValidazioneDati.isFilled(t.getCd_periodicita()))
				output.setCd_per(t.getCd_periodicita().toString());
			else
				//almaviva5_20101126 #4014 ricavo periodicità da fascicolo più recente
				output.setCd_per(dao.cercaPeriodicitaTitolo(bid));

			//almaviva5_20101126 #4016 ISSN
			output.setNumero_std(ConversioneHibernateVO.toWeb().listaNumeriISSN(dao.getListaNumeriISSN(t)));
		}
		return output;
	}


	@SuppressWarnings("unchecked")
	protected KardexPeriodicoVO getEsameKardexBiblioPolo(CommandInvokeVO invoke, RicercaKardexEsameBiblioPoloVO richiesta) throws Exception {

		richiesta.validate(new RicercaKardexPeriodicoValidator() );

		PeriodiciDAO dao = new PeriodiciDAO();
		String bid = richiesta.getBid();
		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.FASCICOLO);
		kardex.setBid(bid);
		List<Short> range = dao.getRangeAnnoPubbFascicoliPerEsame(bid);
		if (!ValidazioneDati.isFilled(range))
			return kardex;

		Date from = richiesta.getBegin();
		Date to   = richiesta.getEnd();
		List<Tbp_fascicolo> fascicoli = dao.getListaFascicoliPerBid(bid, range, from, to);

		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>(fascicoli.size());
		kardex.setFascicoli(listaFascicoli);

		for (Tbp_fascicolo f : fascicoli) {
			FascicoloDecorator fd = new FascicoloDecorator(ConversioneHibernateVO.toWeb().fascicolo(f, null));
			listaFascicoli.add(fd);
			/*
			Map<String, Integer> listaBib = dao.getListaBibliotecheEsemplareFascicolo(fd.getBid(), fd.getFid());
			if (ValidazioneDati.isFilled(listaBib)) {
				int totPolo = 0;
				for (String bib : listaBib.keySet()) {
					int totBib = listaBib.get(bib);
					totPolo += totBib;
					if (bib.equals(richiesta.getBiblioteca().getCod_bib()))
						fd.setNumEsemplariBib(totBib);
				}
				fd.setNumEsemplariPolo(totPolo);
			}
			*/
		}

		BaseVO.sortAndEnumerate(listaFascicoli,	richiesta.getComparator());

		return kardex;
	}

	protected KardexPeriodicoVO getKardexPeriodico(CommandInvokeVO invoke, RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {
		richiesta.validate(new RicercaKardexPeriodicoValidator() );
		KardexPeriodicoVO kardex = null;

		SerieBaseVO oggettoRicerca = richiesta.getOggettoRicerca();
		SeriePeriodicoType serieType = SeriePeriodicoType.fromClass(oggettoRicerca);
		switch (serieType) {
		case ORDINE:
			richiesta.setTipoRange(TipoRangeKardex.ORDINE);
			kardex = gestioneKardexDaOrdine(richiesta);
			break;
		case COLLOCAZIONE:
			impostaRangeAnnoPerKardex(richiesta);
			kardex = getKardexPeriodicoPerCollocazione(richiesta);
			break;
		case ESEMPLARE:
			impostaRangeAnnoPerKardex(richiesta);
			kardex = getKardexPeriodicoPerEsemplare(richiesta);
			break;
		case FASCICOLO:
			kardex = getKardexEsameEsemplariPerFascicolo(richiesta);
			break;
		case TITOLO:
			return getKardexPeriodicoPerPrevisionale(richiesta);
		}

		List<FascicoloVO> fascicoli = kardex.getFascicoli();
		if (ValidazioneDati.isFilled(fascicoli)) {
			BaseVO.sortAndEnumerate(fascicoli, richiesta.getComparator() );
			//imposta stato fascicolo
			//ATTENZIONE: il metodo presuppone che i fascicoli siano ordinati per data pubb decrescente
			PeriodiciUtil.impostaStatoFascicoli(serieType, fascicoli, richiesta.getNumeroElementiBlocco() );

			if (serieType != SeriePeriodicoType.FASCICOLO) {
				//elimina fascicoli incompatibili per kardex da comunicaz. fornitore
				PeriodiciUtil.eliminaFascicoliPerComunicazione(richiesta, fascicoli);
				//imposta periodicita prevalente
				PeriodiciUtil.impostaPeriodicita(kardex);
			}
		}

		return kardex;
	}

	private KardexPeriodicoVO gestioneKardexDaOrdine(RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		if (richiesta instanceof RicercaKardexPeriodicoPerComunicazioneVO)
			return getKardexPeriodicoPerOrdineDaComunicazione((RicercaKardexPeriodicoPerComunicazioneVO<FascicoloVO>) richiesta);

		if (richiesta instanceof RicercaKardexPeriodicoPerCreaOrdineVO)
			return getKardexPeriodicoPerCreazioneOrdine((RicercaKardexPeriodicoPerCreaOrdineVO<FascicoloVO>) richiesta);

		if (richiesta instanceof RicercaKardexPeriodicoPerAssociaInventarioVO) {
			impostaRangeAnnoPerKardex(richiesta);
			return getKardexPeriodicoPerAssociaInventario((RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO>) richiesta);
		}

		impostaRangeAnnoPerKardex(richiesta);
		return getKardexPeriodicoPerOrdine(richiesta);

	}

	private KardexPeriodicoVO getKardexPeriodicoPerPrevisionale(RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		SerieTitoloVO tit = (SerieTitoloVO) richiesta.getOggettoRicerca();

		Date from = richiesta.getBegin();
		Date to = richiesta.getEnd();
		int cnt = dao.count_listaFascicoliPerBid(tit.getBid(), from, to );
		DaoManager.checkMaxResults(cnt);

		List<FascicoloVO> output = new ArrayList<FascicoloVO>(cnt);
		List<Tbp_fascicolo> fascicoli = dao.getListaFascicoliPerBid(tit.getBid(), from, to );

		for (Tbp_fascicolo row : fascicoli) {
			FascicoloDecorator fd = new FascicoloDecorator(ConversioneHibernateVO.toWeb().fascicolo(row, null));
			output.add(fd);
		}

		BaseVO.sortAndEnumerate(output, richiesta.getComparator() );

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.TITOLO);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(tit.getBid());
		kardex.setFascicoli(output);
		kardex.setBiblioteca(richiesta.getBiblioteca());

		//intestazione
		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo titolo = tdao.getTitolo(tit.getBid());
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(tit, titolo.getBid() + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private KardexPeriodicoVO getKardexEsameEsemplariPerFascicolo(RicercaKardexPeriodicoVO<FascicoloVO> richiesta)  throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();

		SerieFascicoloVO sf = (SerieFascicoloVO) richiesta.getOggettoRicerca();
		FascicoloVO fasc = sf.getFascicolo();

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.ESEMPLARE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(fasc.getBid());
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>();
		kardex.setFascicoli(listaFascicoli);

		List<Object[]> fascicoli = dao
				.getListaEsemplariPerFid(richiesta.getBiblioteca()
						.getCod_polo(), richiesta.getBiblioteca().getCod_bib(),
						fasc.getBid(), fasc.getFid(), sf.isEsemplariInPolo());

		for (Object[] row : fascicoli) {
			Tbp_fascicolo f = (Tbp_fascicolo) row[0];
			Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
			FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
			listaFascicoli.add(new FascicoloDecorator(fascicolo));
		}

		BaseVO.sortAndEnumerate(listaFascicoli, richiesta.getComparator() );

		//intestazione
		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo titolo = tdao.getTitolo(fasc.getBid());
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(sf, titolo.getBid() + " " + isbd.getT200_areaTitolo() );
//		kint.setFrom(DateUtil.formattaData(from));
//		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private KardexPeriodicoVO getKardexPeriodicoPerAssociaInventario(RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO> richiesta) throws Exception {

		KardexPeriodicoVO kardex = getKardexPeriodicoPerOrdine(richiesta);
		if (kardex.isEmpty())
			return kardex;

		//mi interessano solo i ricevuti non legati a inventario
		Iterator<FascicoloVO> i = kardex.getFascicoli().iterator();
		while(i.hasNext()) {
			EsemplareFascicoloVO ef = i.next().getEsemplare();
			if (ef != null  && ef.isLegatoInventario())
				i.remove();
		}

		return kardex;
	}


	private void impostaListaBibliotecheEsemplareFascicolo(List<FascicoloVO> fascicoli) throws DaoManagerException {
		if (!ValidazioneDati.isFilled(fascicoli))
			return;

		PeriodiciDAO dao = new PeriodiciDAO();
		for (FascicoloVO f : fascicoli) {
			if (f.isNuovo())
				continue;

			FascicoloDecorator fd = (FascicoloDecorator) f;
			Map<String, Integer> listaBib = dao.getListaBibliotecheEsemplareFascicolo(fd.getBid(), fd.getFid());
			fd.setListaBibRicezione(new ArrayList<String>(listaBib.keySet()));
		}
	}

	private KardexPeriodicoVO getKardexPeriodicoPerCreazioneOrdine(RicercaKardexPeriodicoPerCreaOrdineVO<FascicoloVO> richiesta) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();

		SerieOrdineVO so = (SerieOrdineVO) richiesta.getOggettoRicerca();

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.ORDINE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(so.getBid());
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>();
		kardex.setFascicoli(listaFascicoli);


		//range di interesse:
		//viene calcolato come intervallo gen./dic. su anno abbonamento
		int annoAbb = richiesta.getAnnoAbb();
		//almaviva5_20111108 #4724 ricerca estesa alle date del fascicolo su dettagli ordine
		Date from = ValidazioneDati.min(DateUtil.firstDayOfYear(annoAbb), richiesta.getBegin());
		Date to = ValidazioneDati.max(DateUtil.lastDayOfYear(annoAbb), richiesta.getEnd());
		//
		List<Object[]> fascicoli = dao.getListaFascicoliPerRangeDate(so.getBid(), from, to);

		for (Object[] row : fascicoli) {
			Tbp_fascicolo f = (Tbp_fascicolo) row[0];
			Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
			FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
			listaFascicoli.add(new FascicoloDecorator(fascicolo));
		}

		//intestazione
		Tb_titolo titolo = dao.getTitolo(so.getBid());
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(so, so.getBid() + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private KardexPeriodicoVO getKardexPeriodicoPerStampa(RicercaKardexPeriodicoPerStampaOrdineVO<FascicoloVO> richiesta)  throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		Tba_ordiniDao odao = new Tba_ordiniDao();
		TitoloDAO tdao = new TitoloDAO();

		SerieOrdineVO so = (SerieOrdineVO) richiesta.getOggettoRicerca();

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.ORDINE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(so.getBid());
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>();
		kardex.setFascicoli(listaFascicoli);

		BibliotecaVO bib = richiesta.getBiblioteca();

		int id_ordine = so.getId_ordine();
		if (!so.isEmpty() && id_ordine == 0) {
			Tba_ordini ordine = odao.getOrdine(richiesta.getBiblioteca()
					.getCod_polo(), so.getCod_bib_ord(), so.getAnno_ord(), so
					.getCod_tip_ord(), so.getCod_ord());
			id_ordine = ordine.getId_ordine();
		}

		Set<StatoFascicolo> filtroStato = richiesta.getFiltroStatoFascicolo();
		boolean isFiltroStato = ValidazioneDati.isFilled(filtroStato);

		PeriodicitaFascicoloType filtroCdPer = richiesta.getCd_per();
		boolean isFiltroCdPer = (filtroCdPer != null);

		Date from = richiesta.getBegin();
		Date to = richiesta.getEnd();

		Session session = dao.getCurrentSession();
		List<Integer> listaOrdini = dao.getListaIdOrdiniPerStampa(bib.getCod_polo(), bib.getCod_bib(),
				id_ordine, so.getId_fornitore(), richiesta.getStato_ordine(),
				so.getCod_tip_ord(), from, to);

		if (!ValidazioneDati.isFilled(listaOrdini))
			return kardex;

		for (int id : listaOrdini) {
			Tba_ordini ordine = odao.getOrdineById(id);
			Tb_titolo titolo = tdao.getTitolo(ordine.getBid());
			String isbd = ConversioneHibernateVO.toWeb().estraiTitoloPerAreaServizi(titolo);

			Date[] range = impostaRangeAnnoPerKardexOrdine(ordine);
			List<Object[]> fascicoli = dao.getListaFascicoliPerOrdine(ordine, range[0], range[1] );

			List<FascicoloVO> tmp = new ArrayList<FascicoloVO>();

			for (Object[] row : fascicoli) {
				Tbp_fascicolo f = (Tbp_fascicolo) row[0];
				Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
				FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
				FascicoloStampaDecorator fsd = new FascicoloStampaDecorator(fascicolo, ordine);
				fsd.setIsbd(isbd);
				tmp.add(fsd);
			}

			if (ValidazioneDati.isFilled(tmp)) {
				BaseVO.sortAndEnumerate(tmp, ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO) );
				PeriodiciUtil.impostaStatoFascicoli(SeriePeriodicoType.ORDINE, tmp, richiesta.getNumeroElementiBlocco() );

				//altri filtri
				Iterator<FascicoloVO> i = tmp.iterator();
				while (i.hasNext()) {
					FascicoloVO f = i.next();
					//stato fascicolo
					if (isFiltroStato && !filtroStato.contains(f.getStato())) {
						i.remove();
						continue;
					}

					//periodicità
					if (isFiltroCdPer && !ValidazioneDati.in(filtroCdPer.getCd_per(), f.getCd_per())) {
						i.remove();
						continue;
					}
				}

				listaFascicoli.addAll(tmp);
				session.clear();
			}

		}

		return kardex;
	}

	private KardexPeriodicoVO getKardexPeriodicoPerEsemplare(RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		Tbc_esemplare_titoloDao edao = new Tbc_esemplare_titoloDao();

		SerieEsemplareCollVO se = (SerieEsemplareCollVO) richiesta.getOggettoRicerca();
		Tbc_esemplare_titolo e = edao.getEsemplare(se.getCodPolo(), se.getCodBib(), se.getBid(), se.getCd_doc());
		if (e == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.ESEMPLARE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(e.getB().getBid());
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>();
		kardex.setFascicoli(listaFascicoli);

		//range di interesse:
		Date from = richiesta.getBegin();
		Date to   = richiesta.getEnd();
		TipoRangeKardex tipoRange = richiesta.getTipoRange();
		List<Integer> rangeAnnoPubb = richiesta.getRangeAnnoPubb();

		List<Object[]> fascicoli = dao.getListaFascicoliPerEsemplare(e, from, to, rangeAnnoPubb, tipoRange);

		for (Object[] row : fascicoli) {
			Tbp_fascicolo f = (Tbp_fascicolo) row[0];
			Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
			FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
			listaFascicoli.add(new FascicoloDecorator(fascicolo));
		}

		//intestazione
		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo titolo = tdao.getTitolo(e.getB().getBid());
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(se, titolo.getBid() + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private KardexPeriodicoVO getKardexPeriodicoPerCollocazione(RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		Tbc_collocazioneDao cdao = new Tbc_collocazioneDao();

		SerieCollocazioneVO sc = (SerieCollocazioneVO) richiesta.getOggettoRicerca();
		Tbc_collocazione coll = cdao.getCollocazione(sc.getKey_loc());
		if (coll == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.COLLOCAZIONE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(coll.getB().getBid());

		//range di interesse:
		Date from = richiesta.getBegin();
		Date to   = richiesta.getEnd();
		TipoRangeKardex tipoRange = richiesta.getTipoRange();
		List<Integer> rangeAnnoPubb = richiesta.getRangeAnnoPubb();

		List<Object[]> fascicoli = dao.getListaFascicoliPerCollocazione(coll, from, to, rangeAnnoPubb, tipoRange);
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>(fascicoli.size());
		kardex.setFascicoli(listaFascicoli);

		for (Object[] row : fascicoli) {
			Tbp_fascicolo f = (Tbp_fascicolo) row[0];
			Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
			FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
			listaFascicoli.add(new FascicoloDecorator(fascicolo));
		}

		//intestazione
		Tb_titolo titolo = coll.getB();
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(sc, titolo.getBid() + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private KardexPeriodicoVO getKardexPeriodicoPerOrdine(RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		EsamePeriodicoDAO esameDAO = new EsamePeriodicoDAO();
		Tba_ordiniDao odao = new Tba_ordiniDao();

		SerieOrdineVO so = (SerieOrdineVO) richiesta.getOggettoRicerca();
		Tba_ordini ordine = so.getId_ordine() > 0 ? odao.getOrdineById(so.getId_ordine()) :
													odao.getOrdine(richiesta.getBiblioteca().getCod_polo(),
															so.getCod_bib_ord(), so.getAnno_ord(),
															so.getCod_tip_ord(), so.getCod_ord());
		if (ordine == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.ORDINE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(ordine.getBid());

		//range di interesse:
		//se specificato dalla richiesta si usa quello, altrimenti viene caricato dai dati di ordine
		//Date from = ValidazioneDati.isFilled(richiesta.getDataFrom()) ? DateUtil.toDate(richiesta.getDataFrom()) : ordine.getData_fasc();
		//Date to   = ValidazioneDati.isFilled(richiesta.getDataTo()) ? DateUtil.toDate(richiesta.getDataTo()) : ordine.getData_fine();

		//almaviva5_20101019 se vengo da sif devo controllare che la catena degli ordini sia valorizzata
		if (!ValidazioneDati.isFilled(so.getOrdiniPrecedenti())) {
			List<Integer> ordiniPrecedenti = ValidazioneDati.asSingletonList(ordine.getId_ordine());
			so.setOrdiniPrecedenti(ordiniPrecedenti);
			Tbf_biblioteca_in_polo bib = ordine.getCd_bib();
			List<CatenaRinnoviOrdineVO> catene = esameDAO.getCatenaRinnoviOrdine(bib.getCd_polo().getCd_polo(),	bib.getCd_biblioteca(), ordine.getBid());
			if (ValidazioneDati.isFilled(catene))
				for (CatenaRinnoviOrdineVO cr : catene)
					if (cr.getOrdiniPrecedenti().contains(so.getId_ordine()))
						so.setOrdiniPrecedenti(cr.getOrdiniPrecedenti());
		}

		Date from = richiesta.getBegin();
		Date to = richiesta.getEnd();

		List<Object[]> fascicoli = dao.getListaFascicoliPerOrdine(ordine, from, to);
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>(fascicoli.size());
		kardex.setFascicoli(listaFascicoli);

		for (Object[] row : fascicoli) {
			Tbp_fascicolo f = (Tbp_fascicolo) row[0];
			Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
			FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
			//cerco solleciti per il fascicolo (non ricevuto)
			if (ef == null || ValidazioneDati.in(ef.getFl_canc(), 's', 'S') )
				fascicolo.setSollecitato(dao.esisteSollecitoFascicolo(f, ordine));
			listaFascicoli.add(new FascicoloDecorator(fascicolo));
		}

		//intestazione
		String bid = ordine.getBid();
		Tb_titolo titolo = dao.getTitolo(bid);
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(so, bid + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private KardexPeriodicoVO getKardexPeriodicoPerOrdineDaComunicazione(RicercaKardexPeriodicoPerComunicazioneVO<FascicoloVO> richiesta) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		Tba_ordiniDao odao = new Tba_ordiniDao();

		SerieOrdineVO so = (SerieOrdineVO) richiesta.getOggettoRicerca();
		Tba_ordini ordine = so.getId_ordine() > 0 ? odao.getOrdineById(so.getId_ordine()) :
													odao.getOrdine(richiesta.getBiblioteca().getCod_polo(),
															so.getCod_bib_ord(), so.getAnno_ord(),
															so.getCod_tip_ord(), so.getCod_ord());
		if (ordine == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		TitoloDAO tdao = new TitoloDAO();
		String bid = ordine.getBid();
		Tb_titolo titolo = tdao.getTitolo(bid);
		if (titolo == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		//almaviva5_20111205 #4662
		if (!ValidazioneDati.in(titolo.getCd_natura(), 's', 'S'))
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_TIPOLOGIA_NON_GESTITA, bid);

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.ORDINE);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(bid);

		//range di interesse:
		//se specificato dalla richiesta si usa quello, altrimenti viene caricato dai dati di ordine
		Date from = ValidazioneDati.isFilled(richiesta.getDataFrom()) ? DateUtil.toDate(richiesta.getDataFrom()) : PeriodiciUtil.calcolaDataInizioAbb(ordine);
		Date to   = ValidazioneDati.isFilled(richiesta.getDataTo()) ? DateUtil.toDate(richiesta.getDataTo()) : PeriodiciUtil.calcolaDataFineAbb(ordine);

		ComunicazioneVO com = richiesta.getComunicazione();
		//almaviva5_20111205 #4662
		int cod_msg = ValidazioneDati.isFilled(com.getCodiceMessaggio()) ? Integer.valueOf(com.getCodiceMessaggio()) : -1;
		List<Object[]> fascicoli = dao.getListaFascicoliPerOrdineDaComunicazione(ordine, from, to, cod_msg);
		List<FascicoloVO> listaFascicoli = new ArrayList<FascicoloVO>(fascicoli.size());
		kardex.setFascicoli(listaFascicoli);

		for (Object[] row : fascicoli) {
			Tbp_fascicolo f = (Tbp_fascicolo) row[0];
			Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
			FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
			//cerco solleciti per il fascicolo (non ricevuto)
			if (ef == null || ValidazioneDati.in(ef.getFl_canc(), 's', 'S') )
				fascicolo.setSollecitato(dao.esisteSollecitoFascicolo(f, ordine));
			listaFascicoli.add(new FascicoloDecorator(fascicolo));
		}

		//intestazione
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(so, bid + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}


	protected List<FascicoloVO> aggiornaFascicolo(CommandInvokeVO invoke, SeriePeriodicoType type, List<FascicoloVO> fascicoli) throws Exception {

		List<FascicoloVO> output = new ArrayList<FascicoloVO>(fascicoli.size());
		PeriodiciDAO dao = new PeriodiciDAO();
		String firmaUtente = DaoManager.getFirmaUtente(invoke.getTicket());
		Timestamp ts = DaoManager.now();
		FascicoloValidator fv = new FascicoloValidator(type);
		int loop = 0;
		for (FascicoloVO f : fascicoli) {
			f.validate(fv);
			output.add(internalAggiornaFascicolo(f, firmaUtente, ts));
			if ((++loop % 100) == 0)
				dao.clearSession();
		}

		return output;
	}

	private boolean internalVerificaEsistenzaFascicolo(FascicoloVO fascicolo) throws Exception {
		PeriodiciDAO dao = new PeriodiciDAO();
		Tbp_fascicolo f = ConversioneHibernateVO.toHibernate().fascicolo(fascicolo);
		List<Tbp_fascicolo> ff = dao.verificaEsistenzaFascicolo(f);
		if (!ValidazioneDati.isFilled(ff))
			return false;

		String numFascicolo = OrdinamentoCollocazione2.normalizza(PeriodiciUtil.formattaNumeroFascicolo(fascicolo));

		for (Tbp_fascicolo other : ff) {
			String numOther = OrdinamentoCollocazione2.normalizza(PeriodiciUtil.formattaNumeroTbpFascicolo(other));
			if (ValidazioneDati.equals(numOther, numFascicolo))
				return true;
		}

		return false;
	}

	private FascicoloVO internalAggiornaFascicolo(FascicoloVO fascicolo, String firmaUtente, Timestamp ts) throws Exception {
		if (fascicolo.isNuovo()) {
			fascicolo.setUteIns(firmaUtente);
			fascicolo.setTsIns(ts);
		}
		fascicolo.setUteVar(firmaUtente);
		EsemplareFascicoloVO esemplare = fascicolo.getEsemplare();
		if (esemplare != null) {
			if (esemplare.isNuovo()) {
				esemplare.setUteIns(firmaUtente);
				esemplare.setTsIns(ts);
			}
			esemplare.setUteVar(firmaUtente);
		}

		PeriodiciDAO dao = new PeriodiciDAO();
		Tbp_fascicolo f = null;
		Tbp_esemplare_fascicolo ef = null;

		try {
			f = ConversioneHibernateVO.toHibernate().fascicolo(fascicolo);
			//almaviva5_20101216 controllo cancellazione
			if (fascicolo.isCancellato()) {
				ef = ConversioneHibernateVO.toHibernate().esemplareFascicolo(esemplare);
				if (dao.verificaEsistenzaAltriEsemplariFascicolo(f, ef))
					//almaviva5_20111004 #4654
					throw new ApplicationException(SbnErrorTypes.PER_ERRORE_FASCICOLO_POSSEDUTO,
						PeriodiciUtil.formattaNumeroFascicolo(fascicolo) );
			} else
				//almaviva5_20101115 #3985 se non sto cancellando devo controllare se esiste
				//un fascicolo duplicato
				//if (dao.verificaEsistenzaFascicolo(f))
				if (internalVerificaEsistenzaFascicolo(fascicolo))
					throw new ApplicationException(SbnErrorTypes.PER_FASCICOLO_DUPLICATO,
						DateUtil.formattaData(f.getData_in_pubbl()),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_FASCICOLI, f.getCd_tipo_fasc().toString()),
						PeriodiciUtil.formattaNumeroFascicolo(fascicolo));

			// update parte bibliografica
			f = dao.saveFascicolo(f);

			//almaviva5_20110223 #4160 controllo max n.pezzi per inventario
			if (!verificaPartizioneIventario(esemplare) )
				throw new ApplicationException(SbnErrorTypes.PER_NUMERO_PEZZI_TROPPO_GRANDE);

			ef = ConversioneHibernateVO.toHibernate().esemplareFascicolo(esemplare);
			//update parte amministrativa
			ef = ef != null ? dao.saveEsemplareFascicolo(ef) : null;

		}  catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		}

		FascicoloDecorator fd = new FascicoloDecorator(ConversioneHibernateVO.toWeb().fascicolo(f, ef) );
		//almaviva5_20110210 imposto bib. ricezione
		if (!fascicolo.isNuovo() && !fascicolo.isCancellato() )
			impostaListaBibliotecheEsemplareFascicolo(ValidazioneDati.asSingletonList((FascicoloVO)fd));

		return fd;
	}

	private boolean verificaPartizioneIventario(EsemplareFascicoloVO e)	throws Exception {
		if (e != null && !e.isCancellato() && e.getCd_inven() > 0) {
			Tbc_inventarioDao idao = new Tbc_inventarioDao();
			Tbc_inventario inv = idao.getInventario(e.getCodPolo(), e.getCodBib(), e.getCd_serie(), e.getCd_inven());
			//verifico che il gruppo scelto per l'esemplare sia compatibile con il n.pezzi per l'inventario
			if (inv != null && ValidazioneDati.isFilled(inv.getNum_vol()))
				return (ValidazioneDati.compare(e.getGrp_fasc(), inv.getNum_vol()) <= 0);
		}

		return true;
	}

	protected FascicoloVO ricezioneFascicolo(CommandInvokeVO invoke, KardexPeriodicoVO kardex, FascicoloVO fascicolo) throws Exception {

		SeriePeriodicoType tipo = kardex.getTipo();
		fascicolo.validate( new FascicoloValidator(tipo) );

		switch (tipo) {
		case ESEMPLARE:
		case COLLOCAZIONE:
			break;
		case ORDINE:
			EsemplareFascicoloVO ef = fascicolo.getEsemplare();
			if (ef.getId_ordine() == 0 && !ValidazioneDati.isFilled(ef.getCod_bib_ord()))
				ef.setId_ordine(kardex.getIntestazione().getOrdine().getId_ordine());
			break;
		}

		String firmaUtente = DaoManager.getFirmaUtente(invoke.getTicket());
		Timestamp ts = DaoManager.now();

		return internalAggiornaFascicolo(fascicolo, firmaUtente, ts);
	}

	@SuppressWarnings("unchecked")
	protected List<SerieOrdineVO> getListaOrdiniPeriodico(CommandInvokeVO invoke, RicercaOrdiniPeriodicoVO ricerca) throws Exception {

		ricerca.validate();
		SerieBaseVO oggettoRicerca = ricerca.getOggettoRicerca();

		PeriodiciDAO dao = new PeriodiciDAO();
		EsamePeriodicoDAO esameDAO = new EsamePeriodicoDAO();
		Tba_ordiniDao odao = new Tba_ordiniDao();

		List<Tba_ordini> listaOrdini = null;
		List<CatenaRinnoviOrdineVO> catene = null;
		String codPolo = ricerca.getBiblioteca().getCod_polo();
		String codBib = ricerca.getBiblioteca().getCod_bib();

		if (oggettoRicerca instanceof SerieEsemplareCollVO) {
			SerieEsemplareCollVO e = (SerieEsemplareCollVO) oggettoRicerca;
			listaOrdini = dao.getListaOrdiniPeriodicoEsemplare(codPolo, codBib, e.getBid(), e.getCd_doc());
			catene = esameDAO.getCatenaRinnoviOrdine(codPolo, codBib, e.getBid());
		}
		else
		if (oggettoRicerca instanceof SerieCollocazioneVO) {
			SerieCollocazioneVO c = (SerieCollocazioneVO) oggettoRicerca;
			listaOrdini = dao.getListaOrdiniPeriodicoCollocazione(c.getKey_loc());
			catene = esameDAO.getCatenaRinnoviOrdine(codPolo, codBib, c.getBid());
		}
		else
		if (oggettoRicerca instanceof SerieOrdineVO) {
			SerieOrdineVO o = (SerieOrdineVO) oggettoRicerca;
			listaOrdini = ValidazioneDati.asSingletonList(odao.getOrdineById(o.getId_ordine()));
			catene = esameDAO.getCatenaRinnoviOrdine(codPolo, codBib, o.getBid());
		}
		else
			return null;


		if (!ValidazioneDati.isFilled(listaOrdini))
			return null;

		Set<Integer> ordini_cache = new THashSet<Integer>();
		List<SerieOrdineVO> output = new ArrayList<SerieOrdineVO>(listaOrdini.size());

		for (Tba_ordini o : listaOrdini) {
			int id_ordine = o.getId_ordine();
			if (ordini_cache.contains(id_ordine))
				continue;

			ordini_cache.add(id_ordine);
			SerieOrdineVO serieOrdine = ConversioneHibernateVO.toWeb().serieOrdine(o, null, catene);
			output.add(new SerieOrdineDecorator(serieOrdine, ConversioneHibernateVO.toWeb().ordine(o)));

			//per ogni ordine che ha fascicoli devo includere tutti gli ordini della
			//catena di rinnovi (una volta sola);
			for (CatenaRinnoviOrdineVO c : catene) {
				if (c.getOrdiniPrecedenti().contains(id_ordine) )
					for (Integer id_rinn : c.getOrdiniPrecedenti()) {
						if (ordini_cache.contains(id_rinn))
							continue;
						ordini_cache.add(id_rinn);
						Tba_ordini rinn = odao.getOrdineById(id_rinn);
						SerieOrdineVO serieRinn = ConversioneHibernateVO.toWeb().serieOrdine(rinn, null, catene);
						output.add(new SerieOrdineDecorator(serieRinn, ConversioneHibernateVO.toWeb().ordine(rinn)));
					}
			}
		}

		BaseVO.sortAndEnumerate(output, ricerca.getComparator() );

		return output;
	}

	protected boolean associaFascicoliInventario(CommandInvokeVO invoke,
			InventarioVO inv, SeriePeriodicoType type,
			List<FascicoloVO> fascicoli) throws Exception {

		inv.validate(new InventarioValidator());

		for (FascicoloVO f : fascicoli) {
			PeriodiciUtil.preparaFascicoloPerRicezione(f);
			EsemplareFascicoloVO ef = f.getEsemplare();
			if (inv.getCodInvent() > 0) {
				ef.setCodPolo(inv.getCodPolo());
				ef.setCodBib(inv.getCodBib());
				ef.setCd_serie(inv.getCodSerie());
				ef.setCd_inven(inv.getCodInvent());
			}

			String codOrd = ValidazioneDati.trimOrEmpty(inv.getCodOrd());
			if (ValidazioneDati.isFilled(codOrd) && Integer.valueOf(codOrd) > 0) {	//ordine associato
				ef.setCod_bib_ord(inv.getCodBibO());
				ef.setAnno_ord(Integer.valueOf(inv.getAnnoOrd()) );
				ef.setCod_tip_ord(inv.getCodTipoOrd().charAt(0));
				ef.setCod_ord(Integer.valueOf(codOrd) );
				ef.setId_ordine(0);
			}

			//almaviva5_20110112 partizione fascicolo
			ef.setGrp_fasc(ValidazioneDati.isFilled(inv.getGruppoFascicolo()) ? Integer.valueOf(inv.getGruppoFascicolo()) : null);
		}

		aggiornaFascicolo(invoke, type, fascicoli);
		return true;
	}

	protected boolean associaFascicoliGruppoInventario(CommandInvokeVO invoke,
			String grpInv, SeriePeriodicoType type,	List<FascicoloVO> fascicoli) throws Exception {

		InventarioVO inv = new InventarioVO();
		inv.setGruppoFascicolo(grpInv);
		inv.validate(new InventarioValidator());

		for (FascicoloVO f : fascicoli) {
			EsemplareFascicoloVO ef = f.getEsemplare();
			ef.setGrp_fasc(ValidazioneDati.isFilled(inv.getGruppoFascicolo()) ? Integer.valueOf(inv.getGruppoFascicolo()) : null);
		}

		aggiornaFascicolo(invoke, type, fascicoli);
		return true;
	}

	protected boolean annullaRicezioneFascicoli(CommandInvokeVO invoke, SeriePeriodicoType type,
			List<FascicoloVO> fascicoli) throws Exception {

		for (FascicoloVO f : fascicoli) {
			EsemplareFascicoloVO ef = f.getEsemplare();
			if (ef != null) {
				ef.setData_arrivo(null);
				ef.setFlCanc("S");
			}
		}

		aggiornaFascicolo(invoke, type, fascicoli);
		return true;
	}

	protected boolean cancellazioneMultiplaFascicoli(CommandInvokeVO invoke, List<FascicoloVO> fascicoli) throws Exception {

		for (FascicoloVO f : fascicoli)
			f.setFlCanc("S");

		aggiornaFascicolo(invoke, null, fascicoli);
		return true;
	}


	private int cercaPrimoInventarioAnnoAbb(int annoAbb, List<InventarioListeVO> inventari) {
		if (!ValidazioneDati.isFilled(inventari))
			return 0;

		String anno = Integer.valueOf(annoAbb).toString();
		for (InventarioListeVO i : inventari) {
			String annoAbbInv = ValidazioneDati.trimOrEmpty(i.getAnnoAbb());
			if (annoAbbInv.compareTo(anno) <= 0)
				return i.getPrg();
		}

		return 0;
	}

	protected InventariCollocazioneVO getListaInventariDiCollocazione(
			CommandInvokeVO invoke, EsameCollocRicercaVO ricerca)
			throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();

		List<Tbc_inventario> inventari = dao.getListaInventariCollocazione(ricerca.getKeyLoc());
		List<InventarioListeVO> output = new ArrayList<InventarioListeVO>(inventari.size());
		int prg = 0;
		for (Tbc_inventario i : inventari) {
			InventarioListeVO inv = ConversioneHibernateVO.toWeb().inventarioListe(i);
			inv.setPrg(++prg);
			output.add(inv);
		}

		InventariCollocazioneVO invColl = new InventariCollocazioneVO();
		invColl.setInventari(output);
		invColl.setPrgPrimoInventarioAnnoAbb(cercaPrimoInventarioAnnoAbb(ricerca.getAnnoAbb(), output));

		return invColl;
	}

	public void inserisciMessaggiFascicolo(ComunicazioneVO com) throws ValidationException,
			ApplicationException {

    	List<FascicoloVO> fascicoli = com.getFascicoli();
    	if (!ValidazioneDati.isFilled(fascicoli))
    		return;

		PeriodiciDAO dao = new PeriodiciDAO();

		for (FascicoloVO f : fascicoli) {
    		MessaggioFascicoloVO mf = new MessaggioFascicoloVO();
    		mf.setCodPolo(com.getCodPolo());
    		mf.setCodBib(com.getCodBibl());
    		mf.setCod_msg(Integer.valueOf(com.getCodiceMessaggio()));
    		mf.setBid(f.getBid());
    		mf.setFid(f.getFid());

    		StrutturaTerna ordine = com.getIdDocumento();
			mf.setCod_bib_ord(com.getCodBibl());
			mf.setAnno_ord(Integer.valueOf(ordine.getCodice2()));
			mf.setCod_tip_ord(ordine.getCodice1().charAt(0));
			mf.setCod_ord(Integer.valueOf(ordine.getCodice3()));

    		mf.setUteIns(com.getUtente());
    		mf.setTsIns(DaoManager.now());
    		mf.setUteVar(com.getUtente());
    		mf.setFlCanc("N");

			try {
				dao.saveMessaggioFascicolo(ConversioneHibernateVO.toHibernate().messaggioFascicolo(mf));
			} catch (DaoManagerException e) {
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
			} catch (DAOConcurrentException e) {
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
			} catch (Exception e) {
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
			}
    	}

	}

	public void cancellaMessaggiFascicolo(ComunicazioneVO com) throws ValidationException, ApplicationException {

		PeriodiciDAO dao = new PeriodiciDAO();

		try {
			List<Trp_messaggio_fascicolo> messaggi =
				dao.getListaMessaggiComunicazioneFascicolo(com.getCodPolo(), com.getCodBibl(), Integer.valueOf(com.getCodiceMessaggio()));

			for (Trp_messaggio_fascicolo m : messaggi) {
				m.setUte_var(com.getUtente());
				m.setFl_canc('S');
				dao.saveMessaggioFascicolo(m);
			}

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		} catch (DAOConcurrentException e) {
			log.error("", e);
			throw new ApplicationException(
					SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
		}
	}

	public int spostaFascicoliPerFusione(String bidOld, String bidNew, String uteVar) throws ValidationException, ApplicationException {

		UserTransaction tx = ctx.getUserTransaction();
		PeriodiciDAO dao = new PeriodiciDAO();
		TitoloDAO tdao = new TitoloDAO();
		boolean ok = false;
		try {
			DaoManager.begin(tx);
			//almaviva5_20120913 soppressione errore per bid non trovato
			Tb_titolo old = tdao.getTitoloLazy(bidOld);
			//solo periodici (cd_natura='S')
			if (old == null)
				throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND, "Oggetto non trovato");

			if (!ValidazioneDati.in(old.getCd_natura(), 'S', 's')) {
				ok = true;
				return -1;
			}

			Tb_titolo _new = tdao.getTitoloLazy(bidNew);
			if (_new == null)
				throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND, "Oggetto non trovato");

			if (!ValidazioneDati.in(_new.getCd_natura(), 'S', 's')) {
				ok = true;
				return -1;
			}

			int cnt = dao.spostaFascicoliPerFusione(old, _new, uteVar);
			log.debug("Spostamento " + cnt + " legami a fascicoli da " + bidOld + " a " + bidNew);
			ok = true;
			return cnt;

		} catch (ApplicationException e) {
			throw e;

		} catch (ValidationException e) {
			throw e;

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);

		} catch (DAOConcurrentException e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);

		} finally {
			DaoManager.endTransaction(tx, ok);
		}

	}

	public String getAnnateFascicoliTitolo(String bid) throws ValidationException, ApplicationException {
		PeriodiciDAO dao = new PeriodiciDAO();
		try {
			List<Short> rangeAnnoPubb = dao.rangeAnnoPubbFascicoliPerTitolo(bid);

			return PeriodiciUtil.formattaAnnateFascicolo(rangeAnnoPubb);

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
		}

	}

	protected boolean ricezioneMultiplaOrdine(CommandInvokeVO invoke, SerieOrdineVO ordine, List<FascicoloVO> fascicoli) throws Exception {

		for (FascicoloVO f : fascicoli) {
			PeriodiciUtil.preparaFascicoloPerRicezione(f);
			EsemplareFascicoloVO ef = f.getEsemplare();
			ef.setId_ordine(ordine.getId_ordine());
		}

		aggiornaFascicolo(invoke, SeriePeriodicoType.ORDINE, fascicoli);
		return true;
	}

	protected InventarioVO getUnicoInventario(CommandInvokeVO invoke, SerieBaseVO target) throws Exception {
		PeriodiciDAO dao = new PeriodiciDAO();
		SeriePeriodicoType type = SeriePeriodicoType.fromClass(target);
		Tbc_inventario inv = null;
		switch (type) {
		case ESEMPLARE:
			SerieEsemplareCollVO se = (SerieEsemplareCollVO) target;
			Tbc_esemplare_titoloDao edao = new Tbc_esemplare_titoloDao();
			inv = dao.getUnicoInventario(edao.getEsemplare(se.getCodPolo(), se.getCodBib(), se.getBid(), se.getCd_doc()));
			return inv != null ? ConversioneHibernateVO.toWeb().inventarioListe(inv) : null;

		case COLLOCAZIONE:
			SerieCollocazioneVO sc = (SerieCollocazioneVO) target;
			Tbc_collocazioneDao cdao = new Tbc_collocazioneDao();
			inv = dao.getUnicoInventario(cdao.getCollocazione(sc.getKey_loc()));
			return inv != null ? ConversioneHibernateVO.toWeb().inventarioListe(inv) : null;

		case ORDINE:
			SerieOrdineVO so = (SerieOrdineVO) target;
			int cnt = dao.countInventariOrdine(so.getCod_bib_ord(), so.getAnno_ord(), so.getCod_tip_ord(), so.getCod_ord());
			inv = dao.getUnicoInventario(so.getCod_bib_ord(), so.getAnno_ord(), so.getCod_tip_ord(), so.getCod_ord());
			return new InventarioOrdineDecorator(inv != null ? ConversioneHibernateVO.toWeb().inventarioListe(inv) : null, cnt);
		}

		return null;


	}

	private void impostaRangeAnnoPerKardex(RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {
		PeriodiciDAO dao = new PeriodiciDAO();

		/* almaviva5_20110704 #4517
		//range di interesse specificato dall'utente (ha precedenza):
		Date from = DateUtil.toDate(richiesta.getDataFrom());
		Date to   = DateUtil.toDate(richiesta.getDataTo());
		if (from != null || to != null) {
			richiesta.setTipoRange(TipoRangeKardex.DATA_INVENTARIO);
			richiesta.setRangeAnnoPubb(null);
			return;
		}
		*/

		//almaviva5_20110204 range anno inventari + anno pubb
		List<Integer> rangeAnnoPubb = null;
		SerieBaseVO sb = richiesta.getOggettoRicerca();
		SeriePeriodicoType type = SeriePeriodicoType.fromClass(sb);
		switch (type) {
		case ESEMPLARE:
			SerieEsemplareCollVO se = (SerieEsemplareCollVO) sb;
			rangeAnnoPubb = dao.getRangeAnnoPubbFascicoliEsemplare(se.getCodPolo(), se.getCodBib(), se.getBid(), se.getCd_doc());
			richiesta.setRangeAnnoPubb(ValidazioneDati.isFilled(rangeAnnoPubb) ? rangeAnnoPubb : ValidazioneDati.asSingletonList(-1));
//			richiesta.setTipoRange(ValidazioneDati.isFilled(rangeAnnoPubb) ?
//					TipoRangeKardex.DATA_CONV_PUBB : TipoRangeKardex.DATA_INVENTARIO);
			break;

		case COLLOCAZIONE:
			int key_loc = ((SerieCollocazioneVO)sb).getKey_loc();
			rangeAnnoPubb = dao.getRangeAnnoPubbFascicoliCollocazione(key_loc);
			richiesta.setRangeAnnoPubb(ValidazioneDati.isFilled(rangeAnnoPubb) ? rangeAnnoPubb : ValidazioneDati.asSingletonList(-1));
			//richiesta.setTipoRange(ValidazioneDati.isFilled(rangeAnnoPubb) ?
			//		TipoRangeKardex.DATA_CONV_PUBB : TipoRangeKardex.DATA_INVENTARIO);
			break;

		case ORDINE:
			richiesta.setTipoRange(TipoRangeKardex.DATA_FASCICOLO);
			Tba_ordiniDao odao = new Tba_ordiniDao();
			SerieOrdineVO so = (SerieOrdineVO)sb;
			Tba_ordini ordine = so.getId_ordine() > 0 ? odao.getOrdineById(so.getId_ordine()) :
														odao.getOrdine(richiesta.getBiblioteca().getCod_polo(),
																so.getCod_bib_ord(), so.getAnno_ord(),
																so.getCod_tip_ord(), so.getCod_ord());

			Date[] range = impostaRangeAnnoPerKardexOrdine(ordine);

			richiesta.setKardexBegin(range[0]);
			richiesta.setKardexEnd(range[1]);
			break;
		}

	}

	private Date[] impostaRangeAnnoPerKardexOrdine(Tba_ordini ordine) throws DaoManagerException {

		PeriodiciDAO dao = new PeriodiciDAO();

		Date ord_from = PeriodiciUtil.calcolaDataInizioAbb(ordine);
		Date ord_to = PeriodiciUtil.calcolaDataFineAbb(ordine);

		//se ci sono fascicoli ricevuti sull'ordine é necessario espandere il range del kardex per includerli
		Object[] range = dao.getRangeAnnoPubbFascicoliOrdine(ordine.getId_ordine());
		if (range != null && range[0] != null) {
			ord_from = ValidazioneDati.min((Date)range[0], ord_from);
			ord_to = ValidazioneDati.max((Date)range[1], ord_to);
		}

		return new Date[] { ord_from, ord_to };
	}

	protected List<FascicoloVO> getDettaglioFascicolo(CommandInvokeVO invoke,
			List<FascicoloVO> fascicoli, Boolean errorIfNotFound) throws Exception {

		errorIfNotFound = ValidazioneDati.coalesce(errorIfNotFound, true);
		PeriodiciDAO dao = new PeriodiciDAO();
		List<FascicoloVO> output = new ArrayList<FascicoloVO>(fascicoli.size());

		for (FascicoloVO row : fascicoli) {
			//almaviva5_20110523 il previsionale gestisce anche fascicoli non salvati.
			if (row.isNuovo() && !errorIfNotFound) {
				FascicoloDecorator fd = new FascicoloDecorator(row);

				//almaviva5_20121106 #58 LIG
				TitoloDAO tdao = new TitoloDAO();
				Tb_titolo titolo = tdao.getTitolo(fd.getBid());
				IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
				fd.setIsbd(isbd.getT200_areaTitolo() );

				output.add(fd);
				continue;
			}

			Tbp_fascicolo f = dao.getFascicolo(row.getBid(), row.getFid());
			if (f == null || ValidazioneDati.equals(f.getFl_canc(), 'S') )
				throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);
			if (!f.getTs_var().equals(row.getTsVar()))
				throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);

			EsemplareFascicoloVO esempl = row.getEsemplare();
			boolean ricevuto = esempl != null && !esempl.isCancellato();
			Tbp_esemplare_fascicolo ef = ricevuto ? dao.getEsemplareFascicoloById(esempl.getId_ese_fascicolo()) : null;
			FascicoloDecorator fd = new FascicoloDecorator(ConversioneHibernateVO.toWeb().fascicolo(f, ef));

			//almaviva5_20121106 #58 LIG
			Tb_titolo titolo = f.getTitolo();
			IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
			fd.setIsbd(isbd.getT200_areaTitolo() );

			//almaviva5_20110426
			fd.setProgr(row.getProgr());
			output.add(fd);
		}

		impostaListaBibliotecheEsemplareFascicolo(output);

		return output;
	}

	protected List<ModelloPrevisionaleVO> getListaModelliPrevisionale(
			CommandInvokeVO invoke, ModelloPrevisionaleVO ricerca) throws Exception {

		PrevisionaleDAO dao = new PrevisionaleDAO();

		List<Tbp_modello_previsionale> modelli = dao.getListaModelliPrevisionale();
		List<ModelloPrevisionaleVO> output = new ArrayList<ModelloPrevisionaleVO>(modelli.size());

		int prg = 0;
		for (Tbp_modello_previsionale m : modelli) {
			ModelloPrevisionaleVO mod = ConversioneHibernateVO.toWeb().modelloPrevisionale(m);
			mod.setProgr(++prg);
			output.add(new ModelloPrevisionaleDecorator(mod) );
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	protected KardexPeriodicoVO calcolaPrevisionale(CommandInvokeVO invoke, RicercaKardexPrevisionaleVO richiesta) throws Exception {

		richiesta.validate(new PrevisionaleValidator());
		List<FascicoloVO> fascicoli = PrevisionaleUtil.calcola(richiesta);
		List<FascicoloVO> output = new ArrayList<FascicoloVO>(fascicoli.size());
		for (FascicoloVO f : fascicoli)
			output.add(new FascicoloDecorator(f));

		SerieTitoloVO tit = (SerieTitoloVO) richiesta.getOggettoRicerca();

		if (ValidazioneDati.isFilled(output)) {
			//prendo i range date dei fascicoli calcolati per cercare quelli già esistenti
			output = interpolateListaFascicoli(tit.getBid(), output, richiesta.getComparator());
			BaseVO.sortAndEnumerate(output, richiesta.getComparator());
		}

		KardexPeriodicoVO kardex = new KardexPeriodicoVO(SeriePeriodicoType.TITOLO);
		ClonePool.copyCommonProperties(kardex, richiesta);
		kardex.setBid(tit.getBid());
		kardex.setFascicoli(output);
		kardex.setBiblioteca(richiesta.getBiblioteca());

		//intestazione
		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo titolo = tdao.getTitolo(tit.getBid());
		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(tit, titolo.getBid() + " " + isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(Collections.min(output).getData_conv_pub()));
		kint.setTo(DateUtil.formattaData(Collections.max(output).getData_conv_pub()));
		kardex.setIntestazione(kint);

		return kardex;
	}

	private List<FascicoloVO> interpolateListaFascicoli(String bid, List<FascicoloVO> list, Comparator<FascicoloVO> comparator) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();

		//é importante che la lista sia ordinata per la ricerca binaria
		BaseVO.sortAndEnumerate(list, comparator);

		Date from = Collections.min(list).getData_conv_pub();
		Date to = Collections.max(list).getData_conv_pub();

		int cnt = dao.count_listaFascicoliPerBid(bid, from, to);
		DaoManager.checkMaxResults(cnt);

		List<Tbp_fascicolo> fascicoli = dao.getListaFascicoliPerBid(bid, from, to);
		int size = ValidazioneDati.size(fascicoli);
		if (size < 1)
			return list;

		List<FascicoloVO> tmp = new ArrayList<FascicoloVO>(size);

		for (Tbp_fascicolo row : fascicoli) {
			FascicoloDecorator fd = new FascicoloDecorator(ConversioneHibernateVO.toWeb().fascicolo(row, null));
			tmp.add(fd);
			int pos = Collections.binarySearch(list, fd, comparator);
			if (pos < 0)
				//non trovato, va aggiunto alla lista
				continue;
			else
				//trovato, cancello quello virtuale dalla lista
				list.remove(pos);
		}

		list.addAll(tmp);

		return list;
	}

	protected void verificaEsistenzaFascicolo(CommandInvokeVO invoke, FascicoloVO fascicolo, List<FascicoloVO> list) throws Exception {

		fascicolo.validate(new FascicoloValidator(null));

		if (fascicolo.isNuovo() || ValidazioneDati.isFilled(list) ) {
			FascicoloVO other = UniqueIdentifiableVO.searchRepeatableId(fascicolo.getRepeatableId(), list);
			if (other != null && other.getProgr() != fascicolo.getProgr())
				throw new ApplicationException(SbnErrorTypes.PER_FASCICOLO_DUPLICATO,
						DateUtil.formattaData(fascicolo.getData_in_pubbl()),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_FASCICOLI, fascicolo.getCd_tipo_fasc()),
						PeriodiciUtil.formattaNumeroFascicolo(fascicolo));
		}

		if (internalVerificaEsistenzaFascicolo(fascicolo))
			throw new ApplicationException(SbnErrorTypes.PER_FASCICOLO_DUPLICATO,
					DateUtil.formattaData(fascicolo.getData_in_pubbl()),
					CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_FASCICOLI, fascicolo.getCd_tipo_fasc().toString()),
					PeriodiciUtil.formattaNumeroFascicolo(fascicolo));

	}

	protected Map<String, Integer> getListaBibliotecheEsemplareFascicolo(CommandInvokeVO invoke, String bid, Integer fid) throws Exception {

		PeriodiciDAO dao = new PeriodiciDAO();
		Map<String, Integer> listaBib = dao.getListaBibliotecheEsemplareFascicolo(bid, fid);
		return listaBib;

	}

	public boolean getListaFascicoliPerStampa(StampaListaFascicoliVO input,
			String ticket, BatchLogWriter blw)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException, DataException {
    	boolean ok = false;
    	Logger _log = blw.getLogger();

    	try {
    		KardexPeriodicoVO kp = this.getKardexPeriodicoPerStampa(input.getParametri());
    		if (kp.isEmpty())
    			return false;

    		List<FascicoloVO> fascicoli = kp.getFascicoli();
    		String ordinamento = input.getOrdinamento();
    		SerializableComparator<FascicoloVO> c = FascicoloStampaDecorator.ORDINAMENTO_NOME_FORNITORE;
    		switch (Integer.valueOf(ordinamento)) {
    		case 1:	//nome fornitore
    			c = FascicoloStampaDecorator.ORDINAMENTO_NOME_FORNITORE;
    			break;
    		case 2:	// Cronologico (data ordine)
    			c = FascicoloStampaDecorator.ORDINAMENTO_DATA_ORDINE;
    			break;
    		case 3: //Tipologia ordine
    			c = FascicoloStampaDecorator.ORDINAMENTO_TIPO_ORDINE;
    			break;
    		case 4:
    			break;
    		}

    		Collections.sort(fascicoli, c);
	   		List<StampaListaFascicoliDettaglioVO> output = input.getLista();

    		for (FascicoloVO f : fascicoli ) {
    			FascicoloStampaDecorator fsd = (FascicoloStampaDecorator) f;
        		StampaListaFascicoliDettaglioVO slfd = new StampaListaFascicoliDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

				slfd.setFornitore(fsd.getNom_fornitore());
				slfd.setOrdine(fsd.getChiaveOrdine());
				slfd.setAnnoIniziale(DateUtil.formattaData(fsd.getData_in_pubbl()));
				slfd.setAnnoFinale(DateUtil.formattaData(fsd.getData_fi_pubbl()));
				slfd.setDescrFascicolo(fsd.getDescrizioneFascicolo());
				slfd.setStatoFormato(String.valueOf(fsd.getStato()));
				slfd.setDataPubblicazione(DateUtil.formattaData(fsd.getData_conv_pub()));
				slfd.setNote(fsd.getNote());
				slfd.setBid(fsd.getBid());
				slfd.setIsbd(fsd.getIsbd());

				output.add(slfd);
			}

    		ok = true;

    	}catch (DaoManagerException e) {
    		_log.error("", e);
    		throw new DataException (e);
    	} catch (Exception e) {
    		_log.error("", e);
    		throw new DataException (e);
    	}
    	return ok;
    }

	protected KardexPeriodicoVO getKardexPeriodicoPerOPAC(CommandInvokeVO invoke, RicercaKardexPeriodicoVO<FascicoloVO> richiesta) throws Exception {

		richiesta.validate(new RicercaKardexPeriodicoValidator() );

		PeriodiciDAO dao = new PeriodiciDAO();
		Tbc_inventarioDao idao = new Tbc_inventarioDao();

		BibliotecaVO bib = richiesta.getBiblioteca();
		SerieBaseVO sb = richiesta.getOggettoRicerca();
		log.debug("kardex oggetto ricerca: " + sb);

		Date from = richiesta.getBegin();
		Date to   = richiesta.getEnd();

		String bid = null;
		List<FascicoloVO> listaFascicoli = null;
		List<InventarioType> inventari = null;
		List<Object[]> result = null;

		SeriePeriodicoType type = SeriePeriodicoType.fromClass(sb);
		KardexPeriodicoOpacVO kardex = new KardexPeriodicoOpacVO(type);

		switch (type) {
		case COLLOCAZIONE:
			SerieCollocazioneVO c = (SerieCollocazioneVO) sb;
			bid = c.getBid();

			String ord_loc = ValidazioneDati.coalesce(OrdinamentoCollocazione2.normalizza(c.getCd_loc()), " ");
			String ord_spc = ValidazioneDati.coalesce(OrdinamentoCollocazione2.normalizza(c.getSpec_loc()), " ");
			result = dao.countFascicoliPerInventariCollocazione(bib.getCod_polo(), bib.getCod_bib(), c.getBid(),
					c.getCodSez(), ord_loc, ord_spc);

			int size = ValidazioneDati.size(result);
			inventari = new ArrayList<InventarioType>(size);
			kardex.setInventari(inventari);

			//almaviva5_20121212 ricostruzione consistenza per coll duplicate
			Map<Integer, String> consistenze = new LinkedHashMap<Integer, String>(size);
			for (Object[] row : result) {
				InventarioType itype = ConversioneHibernateVO.toXML().inventarioPeriodico(bib, row);
				Tbc_inventario i = idao.getInventario(bib.getCod_polo(), bib.getCod_bib(), itype.getSerie(), (int) itype.getNumero() );
				Tbc_collocazione coll = i.getKey_loc();
				if (coll != null) {
					//salvo le consistenze di collocazione
					String consis = trimOrEmpty(coll.getConsis());
					if (!in(consis, "", "$"))
						consistenze.put(coll.getKey_loc(), consis);
				}

				inventari.add(itype);
			}

			//almaviva5_20121212 ricostruzione consistenza
			CollocazioneType ctype = ConversioneHibernateVO.toXML().collocazione(bib, c);
			ctype.setConsis(ValidazioneDati.formatValueList(new ArrayList<String>(consistenze.values()), "; "));
			//

			kardex.setCollocazione(ctype);

			break;

		case TITOLO:
			SerieTitoloVO st = (SerieTitoloVO) sb;
			bid = st.getBid();
			result = dao.countFascicoliPerInventariTitolo(bib.getCod_polo(), bib.getCod_bib(), bid);

			inventari = new ArrayList<InventarioType>(result.size());
			kardex.setInventari(inventari);

			for (Object[] row : result) {
				InventarioType inv = ConversioneHibernateVO.toXML().inventarioPeriodico(bib, row);
				inventari.add(inv);
				Tbc_inventario i = idao.getInventario(bib.getCod_polo(), bib.getCod_bib(), inv.getSerie(), (int) inv.getNumero() );
				inv.setCollocazione(ConversioneHibernateVO.toXML().collocazione(i.getKey_loc()));
			}
		/*
			result = dao.getListaFascicoliRicevutiPerBiblioteca(bib.getCod_polo(), bib.getCod_bib(), bid, from, to);

			listaFascicoli = new ArrayList<FascicoloVO>(result.size());
			kardex.setFascicoli(listaFascicoli);

			for (Object[] row : result) {
				Tbp_fascicolo f = (Tbp_fascicolo) row[0];
				Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
				FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
				listaFascicoli.add(new FascicoloDecorator(fascicolo));
			}

			if (ValidazioneDati.isFilled(listaFascicoli)) {
				BaseVO.sortAndEnumerate(listaFascicoli, richiesta.getComparator());
				PeriodiciUtil.impostaStatoFascicoli(SeriePeriodicoType.TITOLO,
						listaFascicoli, richiesta.getNumeroElementiBlocco());
			}
		*/
			break;

		case INVENTARIO:
			SerieInventarioVO si = (SerieInventarioVO) sb;
			bid = si.getBid();
			result = dao.getListaFascicoliRicevutiPerInventario(bib.getCod_polo(), bib.getCod_bib(), bid,
					si.getCd_serie(), si.getCd_inven(),	from, to);

			listaFascicoli = new ArrayList<FascicoloVO>(result.size());
			kardex.setFascicoli(listaFascicoli);

			Tbc_inventario i = idao.getInventario(bib.getCod_polo(), bib.getCod_bib(), si.getCd_serie(), si.getCd_inven() );
			if (i == null)
				throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

			InventarioType inv = ConversioneHibernateVO.toXML().inventario(i);
			inv.setCollocazione(ConversioneHibernateVO.toXML().collocazione(i.getKey_loc()));
			kardex.setInventario(inv);

			for (Object[] row : result) {
				Tbp_fascicolo f = (Tbp_fascicolo) row[0];
				Tbp_esemplare_fascicolo ef = (Tbp_esemplare_fascicolo) row[1];
				FascicoloVO fascicolo = ConversioneHibernateVO.toWeb().fascicolo(f, ef);
				listaFascicoli.add(new FascicoloDecorator(fascicolo));
			}

			if (ValidazioneDati.isFilled(listaFascicoli)) {
				BaseVO.sortAndEnumerate(listaFascicoli, richiesta.getComparator());
				PeriodiciUtil.impostaStatoFascicoli(SeriePeriodicoType.TITOLO,
						listaFascicoli, richiesta.getNumeroElementiBlocco());
			}
			break;

		default:
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
		}

		kardex.setBid(bid);

		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo titolo = tdao.getTitoloLazy(bid);
		if (titolo == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);
//		if (!ValidazioneDati.equals(titolo.getCd_natura(), 'S'))
//			throw new ApplicationException(SbnErrorTypes.PER_ERRORE_TITOLO_NON_PERIODICO);

		IsbdVO isbd = IsbdTokenizer.tokenize(titolo.getIsbd(), titolo.getIndice_isbd());
		KardexIntestazioneVO kint = new KardexIntestazioneVO(sb, isbd.getT200_areaTitolo() );
		kint.setFrom(DateUtil.formattaData(from));
		kint.setTo(DateUtil.formattaData(to));
		kardex.setIntestazione(kint);

		return kardex;
	}

	//almaviva5_20170721 #5612
	public Map<String, Integer> countEsemplariBiblioteche(String bid) throws ValidationException, ApplicationException {
		PeriodiciDAO dao = new PeriodiciDAO();
		try {
			return dao.countEsemplariBiblioteche(bid);

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
		}

	}

}
