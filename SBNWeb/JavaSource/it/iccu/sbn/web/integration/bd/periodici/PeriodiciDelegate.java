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
package it.iccu.sbn.web.integration.bd.periodici;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoDecorator;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.InventariCollocazioneDecorator;
import it.iccu.sbn.ejb.vo.periodici.InventariCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.KardexPeriodicoDecorator;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.PosizioneFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.RicercaPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.RicercaKardexEsameBiblioPoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieEsemplareCollVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexIntestazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerAssociaInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerComunicazioneVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerCreaOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class PeriodiciDelegate {

	public static final String LISTA_COLL_ESEMPL_PERIODICI = "listaCollEsemplPeriodici";
	public static final String LISTA_COLL_ESEMPL_KARDEX = "listaCollEsemplKardex";
	public static final String SIF_COMUNICAZIONI = "sif.per.comunicazioni";
	public static final String SIF_INVENTARIO = "inventario";
	public static final String SIF_KARDEX_ORDINE_INVENTARIO = "sif.kardex.ord.inv";
	public static final String BOOKMARK_KARDEX = "per.kardex.list.bookmark";
	public static final String BOOKMARK_FASCICOLO = "per.gest.fasc.bookmark";
	public static final String BOOKMARK_DESCRIZIONE_FASCICOLI = "per.descr.fasc.lista.bookmark";

	private static final String PERIODICI_DELEGATE = "req.periodici.delegate";

	private static Logger log = Logger.getLogger(PeriodiciDelegate.class);

	private final UserVO utente;
	private final HttpServletRequest request;
	private final FactoryEJBDelegate factory;
	private final Utente utenteEjb;

	public static final PeriodiciDelegate getInstance(HttpServletRequest request) throws Exception {
		PeriodiciDelegate delegate = (PeriodiciDelegate) request.getAttribute(PERIODICI_DELEGATE);
		if (delegate == null) {
			delegate = new PeriodiciDelegate(request);
			request.setAttribute(PERIODICI_DELEGATE, delegate);
		}
		return delegate;
	}

	private PeriodiciDelegate(HttpServletRequest request) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		this.request = request;
		factory = FactoryEJBDelegate.getInstance();
		utente = navi.getUtente();
		utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

	}

	public EsameSeriePeriodicoVO getEsameSeriePeriodico(RicercaPeriodicoVO<FascicoloVO> richiesta) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_ESAME_SERIE_PERIODICO, richiesta);
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			EsameSeriePeriodicoVO esame = (EsameSeriePeriodicoVO) result.getResult();

			return separaEsamePeriodicoBlocchi(esame);

		} catch (ApplicationException e) {
			log.error("", e);
			SbnErrorTypes errorCode = e.getErrorCode();
			if (errorCode == SbnErrorTypes.GB_ERRORE_PROTOCOLLO)	//errore sbnmarc
				LinkableTagUtils.addError(request, new ActionMessage(e.getLabels()[0]));
			else
				LinkableTagUtils.addError(request, new ActionMessage(errorCode.getErrorMessage()));
		}

		return null;

	}

	public KardexPeriodicoVO getKardexPeriodico(BibliotecaVO bib, RicercaKardexPeriodicoVO<? extends FascicoloVO> richiesta) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_KARDEX_PERIODICO, richiesta);
		try {
			richiesta.setBiblioteca(bib);
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return separaKardexBlocchi(richiesta, (KardexPeriodicoVO) result.getResult(), true, null);

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	private KardexPeriodicoVO separaKardexBlocchi(RicercaKardexPeriodicoVO<? extends FascicoloVO> richiesta,
			KardexPeriodicoVO kardex, boolean removeFullList,
			DescrittoreBloccoInterceptor interceptor) {

		List<FascicoloVO> fascicoli = kardex.getFascicoli();
		DescrittoreBloccoVO blocco1 = DescrittoreBlocchiUtil.saveBlocchi(
				utente.getTicket(), fascicoli,
				richiesta.getNumeroElementiBlocco(), interceptor);
		KardexPeriodicoDecorator kblocco = new KardexPeriodicoDecorator(kardex);
		kblocco.setBlocco(blocco1);

		//creo un array con tutti i timestamp dei fascicoli trovati
		//per questioni di spazio/performance memorizzo la rappr. numerica
		int size = ValidazioneDati.size(fascicoli);
		if (size > 0) {
			long[] times = new long[size];
			int[] ids = new int[size];
			for (int idx = 0; idx < size; idx++) {
				FascicoloVO f = fascicoli.get(idx);
				times[idx] = f.getData_conv_pub().getTime();
				ids[idx] = f.getRepeatableId();
			}

			kblocco.setTimestamps(times);
			kblocco.setIds(ids);

			kblocco.setTimeFrom(times[size - 1]);
			kblocco.setTimeTo(times[0]);
		}

		//date limite del kardex
		if (ValidazioneDati.isFilled(richiesta.getDataFrom()))
			kblocco.setTimeFrom(DateUtil.toDate(richiesta.getDataFrom()).getTime());
		if (ValidazioneDati.isFilled(richiesta.getDataTo()))
			kblocco.setTimeTo(DateUtil.toDate(richiesta.getDataTo()).getTime());

		if (removeFullList) {
			kblocco.setFascicoli(ValidazioneDati.emptyList(FascicoloVO.class));
			kblocco.getOriginal().setFascicoli(ValidazioneDati.emptyList(FascicoloVO.class));
		}

		return kblocco;
	}

	private EsameSeriePeriodicoVO separaEsamePeriodicoBlocchi(EsameSeriePeriodicoVO esame) {

		List<ElementoSeriePeriodicoVO> serie = esame.getSerie();
		DescrittoreBloccoVO blocco1 = DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(),
				serie, ConstantDefault.ELEMENTI_BLOCCHI.getDefaultAsNumber(), null );
		EsameSeriePeriodicoDecorator espd = new EsameSeriePeriodicoDecorator(esame);
		espd.setBlocco(blocco1);

		espd.setSerie(ValidazioneDati.emptyList(ElementoSeriePeriodicoVO.class));

		return espd;
	}


	public List<FascicoloVO> aggiornaFascicolo(SeriePeriodicoType type, List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_AGGIORNA_FASCICOLO, type, copy );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (List<FascicoloVO>) result.getResultAsCollection(FascicoloVO.class);

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	public FascicoloVO riceviFascicolo(KardexPeriodicoVO kardex, FascicoloVO f) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_RICEZIONE_FASCICOLO, kardex.copy(), f.copy() );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (FascicoloVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return null;

	}

	public ActionForward sifListaInventariDiOrdine(FascicoloVO fascicolo, KardexIntestazioneVO intestazione) throws Exception {

		SerieOrdineVO ordine = null;
		EsemplareFascicoloVO ef = fascicolo != null ? fascicolo.getEsemplare() : null;
		if (ef != null && ValidazioneDati.isFilled(ef.getCod_bib_ord())) {
			ordine = new SerieOrdineVO();
			ClonePool.copyCommonProperties(ordine, ef);
		} else
			ordine = intestazione.getOrdine();

        request.setAttribute("codBibF", "");
        request.setAttribute("codBibO", ordine.getCod_bib_ord());
        request.setAttribute("codTipoOrd", String.valueOf(ordine.getCod_tip_ord()));

        request.setAttribute("annoOrd", String.valueOf(ordine.getAnno_ord()));
        request.setAttribute("codOrd", String.valueOf(ordine.getCod_ord()));

        request.setAttribute("prov", "listaSuppInvOrd");

        StrutturaCombo titoloOrdine = new StrutturaCombo("", intestazione.getDescrizionePeriodico());
        request.setAttribute("titOrd", titoloOrdine);

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();
		return navi.goForward(mapping.findForward("sifListeInventari"));
	}

	public ActionForward sifListaInventariDiCollocazione(KardexPeriodicoVO kardex, Date dataPub) throws Exception {

		request.setAttribute("tipoLista", "listaInvDiColloc");
        request.setAttribute("prov", "fascicolo");
        request.setAttribute("codBib", kardex.getBiblioteca().getCod_bib());
        request.setAttribute("descrBib", kardex.getBiblioteca().getNom_biblioteca());

        EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
        SerieCollocazioneVO coll = kardex.getIntestazione().getCollocazione();
		ricerca.setKeyLoc(coll.getKey_loc());
        ricerca.setCodSez(coll.getCodSez());
        ricerca.setCodLoc(coll.getCd_loc());
        ricerca.setCodSpec(coll.getSpec_loc());
        ricerca.setOrdLst("ID");
        if (dataPub == null)
        	ricerca.setAnnoAbb(0);
        else {
        	Calendar c = Calendar.getInstance();
        	c.setTime(dataPub);
        	ricerca.setAnnoAbb(c.get(Calendar.YEAR));
        }
		request.setAttribute("paramRicerca", ricerca);

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();
		return navi.goForward(mapping.findForward("sifListeInventari"));
	}

	public ActionForward sifListaInventariDiCollocazione(BibliotecaVO bib, SerieCollocazioneVO coll) throws Exception {

		request.setAttribute("tipoLista", "listaInvDiColloc");
        request.setAttribute("prov", "esamePeriodici");
        request.setAttribute("codBib", bib.getCod_bib());
        request.setAttribute("descrBib", bib.getNom_biblioteca());

        EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
		ricerca.setKeyLoc(coll.getKey_loc());
        ricerca.setCodSez(coll.getCodSez());
        ricerca.setCodLoc(coll.getCd_loc());
        ricerca.setCodSpec(coll.getSpec_loc());
        ricerca.setOrdLst("ID");
       	ricerca.setAnnoAbb(0);

		request.setAttribute("paramRicerca", ricerca);

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();
		return navi.goForward(mapping.findForward("sifListeInventari"));
	}

	public ActionForward sifListaCatenaRinnoviOrdine(SerieOrdineVO ordine)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		// ricostruzione della catena dei rinnovi
		StrutturaTerna keyOrd = new StrutturaTerna("", "", ""); // tipo, anno, cod
		keyOrd.setCodice1(String.valueOf(ordine.getCod_tip_ord()));
		keyOrd.setCodice2(String.valueOf(ordine.getAnno_ord()));
		keyOrd.setCodice3(String.valueOf(ordine.getCod_ord()));

		String cod_bib_ord = ordine.getCod_bib_ord();
		List<StrutturaQuinquies> result = factory.getGestioneAcquisizioni().costruisciCatenaRinnovati(keyOrd, cod_bib_ord);

		if (!ValidazioneDati.isFilled(result)) {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));
			return mapping.getInputForward();
		}

		for (StrutturaQuinquies sq : result) {
			sq.setCodice4(utente.getCodPolo());
			sq.setCodice5(cod_bib_ord);
		}

		request.setAttribute("listaOrdini", result);
		request.setAttribute("prov", "listaOrdPeriodici");
		request.setAttribute("locale", Locale.getDefault());
		request.setAttribute("tipoLista", "listaInvDiColloc");

		return navi.goForward(mapping.findForward("sifListeInventari"));
	}


	public ActionForward sifListaInventariDiEsemplare(KardexPeriodicoVO kardex)	throws Exception {

		SerieEsemplareCollVO esempl = kardex.getIntestazione().getEsemplare();

		EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
		paramRicerca.setCodPolo(esempl.getCodPolo());
		paramRicerca.setCodBib(esempl.getCodBib());
		paramRicerca.setCodPoloDoc(esempl.getCodPolo());
		paramRicerca.setCodBibDoc(esempl.getCodBib());
		paramRicerca.setBidDoc(esempl.getBid());
		paramRicerca.setCodDoc(esempl.getCd_doc());
		paramRicerca.setCodBibSez(esempl.getCodBib());

		request.setAttribute("paramRicerca", paramRicerca);
		request.setAttribute("prov","posseduto");
		request.setAttribute("listaCollEsemplare",LISTA_COLL_ESEMPL_KARDEX);
        request.setAttribute("codBib", kardex.getBiblioteca().getCod_bib());
        request.setAttribute("descrBib", kardex.getBiblioteca().getNom_biblioteca());

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();
		return navi.goForward(mapping.findForward("sifCollocazioni"));
	}


	public ActionForward sifKardexPeriodico(BibliotecaVO bib, SerieBaseVO oggettoRicerca) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ParametriPeriodici parametri = new ParametriPeriodici();
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoVO<FascicoloVO>(bib, oggettoRicerca);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.KARDEX);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		ParametriPeriodici.send(request, parametri);

		return navi.goForward(mapping.findForward("sifKardex"));
	}


	public ActionForward sifKardexPeriodicoDaInv(BibliotecaVO bib,
			InventarioTitoloVO inv) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ParametriPeriodici parametri = new ParametriPeriodici();
		SerieCollocazioneVO coll = new SerieCollocazioneVO();
		coll.setKey_loc(inv.getKeyLoc());
		coll.setCodSez(inv.getCodSez());
		coll.setCd_loc(inv.getCodLoc());
		coll.setSpec_loc(inv.getSpecLoc());
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoVO<FascicoloVO>(bib, coll);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

		Calendar now = Calendar.getInstance();
		//se l'inv prevede un anno abbonamento seleziono i fascicoli
		//dell'anno di riferimento, in caso contrario prendo l'anno corrente
		String annoAbb = ValidazioneDati.isFilled(inv.getAnnoAbb())
				&& ValidazioneDati.strIsNumeric(inv.getAnnoAbb())
				&& Integer.valueOf(inv.getAnnoAbb()) > 0 ?
				inv.getAnnoAbb() : String.valueOf(now.get(Calendar.YEAR));

		Integer anno = Integer.valueOf(annoAbb);
		//data inizio
		ricerca.setDataFrom(DateUtil.formattaData(DateUtil.firstDayOfYear(anno)));
		//data fine
		ricerca.setDataTo(DateUtil.formattaData(DateUtil.lastDayOfYear(anno)));

		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.KARDEX);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		parametri.put(ParamType.INVENTARIO, inv);
		ParametriPeriodici.send(request, parametri);

		return navi.goForward(mapping.findForward("sifKardex"));
	}


	public ActionForward sifKardexPeriodicoDaEsemplare(BibliotecaVO bib, EsemplareDettaglioVO esemplare) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ParametriPeriodici parametri = new ParametriPeriodici();
		SerieEsemplareCollVO esempl = new SerieEsemplareCollVO();
		esempl.setCodPolo(esemplare.getCodPolo());
		esempl.setCodBib(esemplare.getCodBib());
		esempl.setBid(esemplare.getBid());
		esempl.setCd_doc(esemplare.getCodDoc());
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoVO<FascicoloVO>(bib, esempl);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.KARDEX);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		ParametriPeriodici.send(request, parametri);

		return navi.goForward(mapping.findForward("sifKardex"));
	}



	public DescrittoreBloccoVO getListaOrdiniPeriodico(RicercaOrdiniPeriodicoVO ricerca) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_LISTA_ORDINI, ricerca.copy() );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			List<SerieOrdineVO> output = (List<SerieOrdineVO>) result.getResultAsCollection(SerieOrdineVO.class);
			if (!ValidazioneDati.isFilled(output)) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.inventariNotFound"));
				return null;
			}

			return DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(), output, ricerca.getNumeroElementiBlocco(), null);

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return null;

	}

	public boolean associaFascicoliInventario(InventarioVO inv, SeriePeriodicoType type, List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.PER_ASSOCIA_FASCICOLI_INVENTARIO, inv.copy(), type, copy );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return false;

	}

	public boolean annullaRicezioneFascicoli(SeriePeriodicoType type, List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.PER_ANNULLA_RICEZIONE_FASCICOLO, type, copy );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return false;

	}

	public DescrittoreBloccoVO nextBlocco(String idLista, int bloccoSelezionato) throws Exception {
		return DescrittoreBlocchiUtil.browseBlocco(utente.getTicket(), idLista, bloccoSelezionato);
	}

	public InventariCollocazioneVO getListaInventariDiCollocazione(EsameCollocRicercaVO ricerca, int nRec) throws Exception {

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_LISTA_INVENTARI_COLLOCAZIONE, ricerca.copy() );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			InventariCollocazioneVO output = (InventariCollocazioneVO) result.getResult();
			if (output.isEmpty())
				throw new DataException("inventariNotFound");

			InventariCollocazioneDecorator icd = new InventariCollocazioneDecorator(output);
			icd.setBlocco(DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(), output.getInventari(), nRec, null));

			return icd;

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;
	}

	protected List<ModelloPrevisionaleVO> getListaModelliPrevisionale(ModelloPrevisionaleVO ricerca) throws Exception {

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_LISTA_MODELLI_PREVISIONALE, ricerca.copy() );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			List<ModelloPrevisionaleVO> output =
				(List<ModelloPrevisionaleVO>) result.getResultAsCollection(ModelloPrevisionaleVO.class);

			return output;

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;
	}

	public ActionForward sifListaComunicazioniOrdine(SerieOrdineVO ordine, FascicoloVO f) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		request.setAttribute(SIF_COMUNICAZIONI, ordine);
		return navi.goForward(mapping.findForward("sifComunicazioni"));
	}

	public ActionForward sifInserisciComunicazioneOrdine(SerieOrdineVO ordine, List<FascicoloVO> fascicoli) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		StrutturaCombo forn = new StrutturaCombo("", "");
		forn.setCodice(ordine.getId_fornitore() + "");
		forn.setDescrizione(ordine.getFornitore());
		StrutturaTerna idDoc = new StrutturaTerna("", "", "");
		idDoc.setCodice1(ordine.getCod_tip_ord() + "");
		idDoc.setCodice2(ordine.getAnno_ord() + "");
		idDoc.setCodice3(ordine.getCod_ord() + "");
		ListaSuppComunicazioneVO lsc = new ListaSuppComunicazioneVO(
				utente.getCodPolo(), ordine.getCod_bib_ord(), "", "O", PeriodiciUtil.calcolaTipoMessaggioSollecito(fascicoli),
				forn, idDoc, "", "", "", "A", "", mapping.getPath(), "");
		lsc.setElemXBlocchi(Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault()));
		lsc.setFascicoli(fascicoli);
		//almaviva5_20111205 #4718
		lsc.setNote(PeriodiciUtil.preparaConsistenzaFascicoli(fascicoli));
		//lsc.setNote(PeriodiciUtil.preparaNoteComunicazioneFascicoli(fascicoli));

		HttpSession session = request.getSession();
		session.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_COMUNICAZIONE, lsc);
		//request.setAttribute(SIF_COMUNICAZIONI, ordine);
		return navi.goForward(mapping.findForward("sifInsComunicazione"));
	}

	public ActionForward sifScegliFascicoliPerComunicazione(BibliotecaVO bib, ComunicazioneVO com, String attribute) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ParametriPeriodici parametri = new ParametriPeriodici();
		SerieOrdineVO so = new SerieOrdineVO();

		StrutturaTerna ordine = com.getIdDocumento();
		so.setCod_bib_ord(com.getCodBibl());
		so.setAnno_ord(Integer.valueOf(ordine.getCodice2()));
		so.setCod_tip_ord(ordine.getCodice1().charAt(0));
		so.setCod_ord(Integer.valueOf(ordine.getCodice3()));
        so.setId_fornitore(Integer.parseInt(com.getFornitore().getCodice()));
        so.setFornitore(com.getFornitore().getDescrizione());

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoPerComunicazioneVO<FascicoloVO>(bib, so, com);
		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		if (kardex.isEmpty()) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
			return mapping.getInputForward();
		}

		parametri.put(ParamType.SIF_KARDEX_RETURN_ATTRIBUTE, attribute);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.SCEGLI_FASCICOLI_PER_COMUNICAZIONE);
		ParametriPeriodici.send(request, parametri);

		return navi.goForward(mapping.findForward("sifKardex"));
	}

	public boolean ricezioneMultiplaOrdine(KardexIntestazioneVO intestazione, List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_RICEZIONE_MULTIPLA_ORDINE,
				intestazione.getOrdine(), copy);
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return false;
	}

	public ActionForward sifScegliFascicoloPerCreaOrdine(BibliotecaVO bib, OrdiniVO ordine, String attribute) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ParametriPeriodici parametri = new ParametriPeriodici();
		SerieOrdineVO so = new SerieOrdineVO();

		so.setCod_bib_ord(ordine.getCodBibl());
		so.setBid(ordine.getTitolo().getCodice());
		StrutturaCombo forn = ordine.getFornitore();
		if (forn != null && ValidazioneDati.isFilled(forn.getCodice())) {
			so.setId_fornitore(Integer.parseInt(forn.getCodice()));
			so.setFornitore(forn.getDescrizione());
		}

		int annoAbb = ValidazioneDati.isFilled(ordine.getAnnoAbbOrdine())
				&& ValidazioneDati.strIsNumeric(ordine.getAnnoAbbOrdine()) ? Integer
				.valueOf(ordine.getAnnoAbbOrdine()) : 0;
		so.setAnnoAbb(annoAbb);

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoPerCreaOrdineVO<FascicoloVO>(bib, so, annoAbb, ordine.isContinuativo());
		//almaviva5_20111108 #4724
		ricerca.setDataFrom(ordine.getDataPubblFascicoloAbbOrdine());
		ricerca.setDataTo(ordine.getDataFineAbbOrdine());
		//
		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		if (kardex.isEmpty()) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
			return mapping.getInputForward();
		}

		parametri.put(ParamType.SIF_KARDEX_RETURN_ATTRIBUTE, attribute);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.SCEGLI_FASCICOLI_PER_CREA_ORDINE);
		ParametriPeriodici.send(request, parametri);

		return navi.goForward(mapping.findForward("sifKardex"));
	}

	public PosizioneFascicoloVO getPosizioneFascicoloPerData(String dataScroll, KardexPeriodicoDecorator kardex) throws Exception {
		try {
			//almaviva5_20110316 gestione digitazione solo annata
			Date scroll;
			if (ValidazioneDati.isFilled(dataScroll) && dataScroll.matches(PeriodiciConstants.REGEX_FORMATO_ANNO))
				scroll = DateUtil.firstDayOfYear(Integer.valueOf(dataScroll));
			else {
				scroll = DateUtil.toDate(dataScroll);
				if (scroll == null)
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT,
							LinkableTagUtils.findMessage(request, Locale.getDefault(), "periodici.kardex.posiziona"));
			}
			long target = scroll.getTime();
			long[] timestamps = kardex.getTimestamps();	//date conv. dei fascicoli nel kardex

			int size = timestamps.length;
			if (size < 1)
				return null;
			//la data cercata é nel range del kardex?
			//i fascicoli sono ordinati per data conv. desc
//			if (target > kardex.getTimeTo() || target < kardex.getTimeFrom())
//				throw new ValidationException(SbnErrorTypes.PER_DATA_POSIZIONA_ERRATA);

			//cerco l'indice nel kardex del fascicolo con data conv. più prossima al target
			int nearest = 0;
			long min = Long.MAX_VALUE;
			for (int idx = 0; idx < size; idx++) {
				long curr = timestamps[idx];
				long diff = curr - target; //Math.abs(curr - target);
				if (diff < min) {
					min = diff;
					nearest = idx;
				}
				if (diff <= 0)
					break;
			}

			//ricavo il numero di blocco che contiene il fascicolo trovato
			DescrittoreBloccoVO blocco1 = kardex.getBlocco();
			int numBlocco = (int)Math.ceil((double)(nearest + 1) / (double)blocco1.getMaxRighe());
			//carico il blocco ed estraggo il fascicolo più vicino al target
			DescrittoreBloccoVO blocco = DescrittoreBlocchiUtil.browseBlocco(utente.getTicket(), blocco1.getIdLista(), numBlocco);
			blocco = (blocco == null) ? blocco1 : blocco; //solo 1 blocco?
			//devo calcolare l'indice relativo alla sottolista utilizzando l'offset del blocco
			FascicoloDecorator fd = (FascicoloDecorator) blocco.getLista().get(nearest - blocco.getStartOffset());

			return new PosizioneFascicoloVO(blocco, fd);

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return null;
	}

	public PosizioneFascicoloVO getPosizioneFascicoloPerId(int targetId, KardexPeriodicoDecorator kardex) throws Exception {
		try {
			int[] ids = kardex.getIds();	//id dei fascicoli nel kardex
			int size = ids != null ? ids.length : 0;
			if (size < 1)
				return null;

			//cerco l'indice nel kardex del fascicolo con id uguale al target
			int pos = -1;
			for (int idx = 0; idx < size; idx++)
				if (ids[idx] == targetId) {
					pos = idx;
					break;
				}

			if (pos < 0)
				return null;

			//ricavo il numero di blocco che contiene il fascicolo trovato
			DescrittoreBloccoVO blocco1 = kardex.getBlocco();
			int numBlocco = (int)Math.ceil((double)(pos + 1) / (double)blocco1.getMaxRighe());
			//carico il blocco ed estraggo il fascicolo più vicino al target
			DescrittoreBloccoVO blocco = DescrittoreBlocchiUtil.browseBlocco(utente.getTicket(), blocco1.getIdLista(), numBlocco);
			blocco = (blocco == null) ? blocco1 : blocco; //solo 1 blocco?
			//devo calcolare l'indice relativo alla sottolista utilizzando l'offset del blocco
			FascicoloDecorator fd = (FascicoloDecorator) blocco.getLista().get(pos - blocco.getStartOffset());

			return new PosizioneFascicoloVO(blocco, fd);

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return null;
	}

	public ActionForward sifEsaminaOrdine(SerieOrdineVO ordine)  throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		ListaSuppOrdiniVO ricerca = new ListaSuppOrdiniVO();
		ricerca.setCodPolo(utente.getCodPolo());
		ricerca.setCodBibl(ordine.getCod_bib_ord());
		ricerca.setAnnoOrdine(ordine.getAnno_ord() + "");
		ricerca.setTipoOrdine(ordine.getCod_tip_ord() + "");
		ricerca.setCodOrdine(ordine.getCod_ord() + "");

		request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE, ValidazioneDati.asSingletonList(ricerca) );
		return navi.goForward(mapping.findForward("ordine"));
	}

	private boolean isAbilitato(String attivita) throws Exception {

		try {
			utenteEjb.checkAttivita(attivita);
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}
	}

	public boolean isAbilitatoDescrizioneFascicoli() throws Exception {
		return isAbilitato(CodiciAttivita.getIstance().DESCRIZIONE_FASCICOLI);
	}

	public boolean isAbilitatoAmministrazioneFascicoli() throws Exception {
		return isAbilitato(CodiciAttivita.getIstance().AMMINISTRAZIONE_FASCICOLI);
	}


	public ConfigurazioneORDVO loadConfigurazioneOrdini(String codPolo, String codBib)  throws Exception {

		ConfigurazioneORDVO richiesta = new ConfigurazioneORDVO();
		richiesta.setCodPolo(codPolo);
		richiesta.setCodBibl(codBib);
		ConfigurazioneORDVO conf = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(richiesta);

		return conf;
	}

	public boolean cancellazioneMultiplaFascicoli(List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.PER_CANCELLAZIONE_MULTIPLA_FASCICOLI, copy );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return false;
	}

	public boolean associaFascicoliGruppoInventario(String grpInv, SeriePeriodicoType type,
		List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.PER_ASSOCIA_GRUPPO_INVENTARIO, grpInv, type, copy );
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(SbnErrorTypes.ERROR_GENERIC.getErrorMessage()) );
		}

		return false;

	}

	public KardexPeriodicoVO getEsameKardexBiblioPolo(BibliotecaVO bib, RicercaKardexEsameBiblioPoloVO richiesta) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_KARDEX_ESAME_BIBLIO_POLO, richiesta);
		try {
			richiesta.setBiblioteca(bib);
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return separaKardexBlocchi(richiesta, (KardexPeriodicoVO) result.getResult(), true,
				new BibEsemplareBloccoInterceptor(richiesta.getBiblioteca().getCod_bib(), this) );

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	public InventarioVO getUnicoInventario(SerieBaseVO target) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_CERCA_UNICO_INVENTARIO, target);
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (InventarioVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	public KardexPeriodicoVO getKardexPeriodicoPerAssociaInventario(BibliotecaVO bib, InventarioVO inv) throws Exception {
		SerieOrdineVO so = new SerieOrdineVO();

		so.setCod_bib_ord(inv.getCodBibO());
		so.setBid(inv.getBid());
		so.setAnno_ord(Integer.valueOf(inv.getAnnoOrd()));
		so.setCod_tip_ord(inv.getCodTipoOrd().charAt(0));
		so.setCod_ord(Integer.valueOf(inv.getCodOrd()));

		RicercaKardexPeriodicoVO<FascicoloVO> richiesta = new RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO>(bib, so, inv);
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.PER_KARDEX_PERIODICO, richiesta);
		try {
			richiesta.setBiblioteca(bib);
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (KardexPeriodicoVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	public boolean checkFascicoliOrdinePerAssociaInventario(BibliotecaVO bib, InventarioVO inv) throws Exception {

		//se non abilitato il controllo è inutile
		if (!isAbilitatoAmministrazioneFascicoli())
			return false;

		SerieOrdineVO so = new SerieOrdineVO();

		so.setCod_bib_ord(inv.getCodBibO());
		so.setBid(inv.getBid());
		so.setAnno_ord(Integer.valueOf(inv.getAnnoOrd()));
		so.setCod_tip_ord(inv.getCodTipoOrd().charAt(0));
		so.setCod_ord(Integer.valueOf(inv.getCodOrd()));

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO>(bib, so, inv);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		LinkableTagUtils.resetErrors(request);
		if (kardex == null)
			return false;

		return !kardex.isEmpty();
	}

	public ActionForward sifKardexPeriodicoOrdinePerAssociaInventario(BibliotecaVO bib, InventarioVO inv,
			String attribute) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();


		ParametriPeriodici parametri = new ParametriPeriodici();
		SerieOrdineVO so = new SerieOrdineVO();

		so.setCod_bib_ord(inv.getCodBibO());
		so.setBid(inv.getBid());
		so.setAnno_ord(Integer.valueOf(inv.getAnnoOrd()));
		so.setCod_tip_ord(inv.getCodTipoOrd().charAt(0));
		so.setCod_ord(Integer.valueOf(inv.getCodOrd()));

		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoPerAssociaInventarioVO<FascicoloVO>(bib, so, inv);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

		KardexPeriodicoVO kardex = getKardexPeriodico(bib, ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		if (kardex.isEmpty()) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
			return mapping.getInputForward();
		}

		parametri.put(ParamType.SIF_KARDEX_RETURN_ATTRIBUTE, attribute);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.SCEGLI_FASCICOLI_PER_ASSOCIA_INVENTARI_ORDINE);
		ParametriPeriodici.send(request, parametri);

		return navi.goForward(mapping.findForward("sifKardex"));
	}

	public List<FascicoloVO> getDettaglioFascicolo(List<FascicoloVO> fascicoli, boolean errorIfNotFound) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.PER_DETTAGLIO_FASCICOLO,
				copy,
				errorIfNotFound);
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return (List<FascicoloVO>) result.getResultAsCollection(FascicoloVO.class);

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	public KardexPeriodicoVO calcolaPrevisionale(RicercaKardexPrevisionaleVO richiesta) throws Exception {

		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
				CommandType.PER_CALCOLA_PREVISIONALE, richiesta.copy());
		try {
			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();

			return separaKardexBlocchi(richiesta, (KardexPeriodicoVO) result.getResult(), false, null);

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			if (e.getErrorCode() == SbnErrorTypes.PER_ERRORE_GIORNI_OBBLIGATORI_PERIODICITA)
				throw e;

			LinkableTagUtils.addError(request, e);
		}

		return null;
	}

	public boolean checkEsistenzaFascicolo(FascicoloVO fascicolo, List<FascicoloVO> fascicoli) throws Exception {

		Serializable copy = (Serializable) ClonePool.deepCopy(fascicoli);

		try {
			CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
					CommandType.PER_VERIFICA_ESISTENZA_FASCICOLO, fascicolo.copy(), copy);

			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();
			return false;

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return true;
	}

	public KardexPeriodicoVO duplicaKardexPrevisionale(RicercaKardexPeriodicoVO<FascicoloVO> ricerca, KardexPeriodicoVO kardex) {
		KardexPeriodicoVO oldKardex = ((kardex instanceof KardexPeriodicoDecorator) ?
				((KardexPeriodicoDecorator) kardex).getOriginal() :
				(KardexPeriodicoVO) kardex.clone());
		oldKardex.setFascicoli(kardex.getFascicoli());
		return separaKardexBlocchi(ricerca, oldKardex, false, null);
	}

	public boolean checkFascicoliPrevistiPerCancellazione(String cod_bib, List<FascicoloVO> fascicoli) throws Exception {

		try {
			//si eliminano dal controllo i fascicoli previsti perché sempre
			//cancellabili
			Iterator<FascicoloVO> i = fascicoli.iterator();
			while (i.hasNext())
				if (i.next().isNuovo()) i.remove();

			//lettura e check dei fascicoli esistenti
			List<FascicoloVO> ff = getDettaglioFascicolo(fascicoli, true);
			if (ff == null)
				return false;

			//almaviva5_20111028 #4705
			PeriodiciUtil.checkFascicoliPerCancellazione(cod_bib, ff);

			return true;

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return false;
	}

	public Map<String, Integer> getListaBibliotecheEsemplareFascicolo(String bid, int fid) throws Exception {
		try {
			CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(),
					CommandType.PER_LISTA_BIBLIOTECHE_ESEMPLARE_FASCICOLO, bid, fid);

			CommandResultVO result = factory.getPeriodici().invoke(command);
			result.throwError();
			return (Map<String, Integer>) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return null;
	}
}
