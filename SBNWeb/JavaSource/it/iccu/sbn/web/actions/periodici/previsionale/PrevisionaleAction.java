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
package it.iccu.sbn.web.actions.periodici.previsionale;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.DurataPrevisionaleType;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.periodici.PrevisionaleUtil;
import it.iccu.sbn.vo.custom.periodici.ModelloPrevisionaleDecorator;
import it.iccu.sbn.web.actionforms.periodici.previsionale.PrevisionaleForm;
import it.iccu.sbn.web.actions.periodici.PeriodiciListaAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class PrevisionaleAction extends PeriodiciListaAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(PrevisionaleAction.class);

	private static String[] BOTTONIERA = new String[] {
		"button.periodici.previsionale.calcola",
		"button.periodici.chiudi" };


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("button.periodici.previsionale.calcola", "calcola");
		map.put("button.periodici.chiudi", "annulla");

		map.put("button.periodici.escludi.inserisci", "inserisciEscludi");
		map.put("button.periodici.escludi.elimina", "eliminaEscludi");
		map.put("button.periodici.includi.inserisci", "inserisciIncludi");
		map.put("button.periodici.includi.elimina", "eliminaIncludi");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);
		log.debug("init()");
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		if (currentForm.isInitialized() )
			return;

		ParametriPeriodici params = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(params);

		DatiBibliograficiPeriodicoVO dbp = (DatiBibliograficiPeriodicoVO) params.get(ParamType.DATI_BIBLIOGRAFICI_PER_PERIODICI);
		currentForm.setDatiBibliografici(dbp);
		EsameSeriePeriodicoVO esame = (EsameSeriePeriodicoVO) params.get(ParamType.ESAME_SERIE_PERIODICO);
		currentForm.setEsame(esame);

		//la biblioteca é impostata a quella di login
		UserVO utente = Navigation.getInstance(request).getUtente();
		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(utente.getCodPolo());
		bib.setCod_bib(utente.getCodBib());
		bib.setNom_biblioteca(utente.getBiblioteca());
		currentForm.setBiblioteca(bib);

		//almaviva5_20110705 #4530 segn. ICCU: eliminati codici periodicità non gestibili
		String[] escludi = new String[] {
				PeriodicitaFascicoloType.ALTRO.getCd_per(),
				PeriodicitaFascicoloType.IRREGOLARE.getCd_per(),
				PeriodicitaFascicoloType.VARIABILE.getCd_per(),
				PeriodicitaFascicoloType.SCONOSCIUTO.getCd_per()
			};
		currentForm.setListaPeriodicita(CodiciProvider.getCodici(CodiciType.CODICE_PERIODICITA, true, escludi));
		//
		currentForm.setListaTipoNumerazione(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_NUMERAZIONE_FASCICOLO, true, ""));

		List<TB_CODICI> listaTipoDurata = new ArrayList<TB_CODICI>();
		for (DurataPrevisionaleType d : DurataPrevisionaleType.values())
			listaTipoDurata.add(new TB_CODICI(d.ordinal(), d.name()));

		currentForm.setListaTipoDurata(listaTipoDurata);
		currentForm.setDurata(12);
		currentForm.setTipoDurata(DurataPrevisionaleType.MESI.name() );

		RicercaKardexPrevisionaleVO calc = (RicercaKardexPrevisionaleVO) params.get(ParamType.PARAMETRI_PREVISIONALE);
		if (calc != null) {
			currentForm.setDataPrimoFasc(DateUtil.formattaData(calc.getData_conv_pub()));
			currentForm.setRicercaKardex(calc);
			currentForm.setModello(new ModelloPrevisionaleDecorator(calc.getModello()));
		} else {
			currentForm.setDataPrimoFasc(DateUtil.formattaData(new Date()));
			currentForm.setRicercaKardex(new RicercaKardexPrevisionaleVO());
			currentForm.setModello(null);
		}

		currentForm.setPulsanti(BOTTONIERA);
		loadDefault(request, currentForm);
		currentForm.setOperazioneInit(TipoOperazione.PREVISIONALE);
		currentForm.setOperazione(TipoOperazione.PREVISIONALE);

		currentForm.setInitialized(true);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	@Override
	public ActionForward posiziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {

		super.loadForm(request, form);

		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		if (currentForm.getModello() == null) {
			//PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			List<ModelloPrevisionaleVO> modelli = ValidazioneDati.emptyList();//delegate.getListaModelliPrevisionale(new ModelloPrevisionaleVO());
			currentForm.setModelli(modelli);
			if (ValidazioneDati.isFilled(modelli))
				currentForm.setModello(modelli.get(0));
			else
				currentForm.setModello(new ModelloPrevisionaleDecorator(new ModelloPrevisionaleVO()));

			currentForm.getModello().setCd_per(currentForm.getEsame().getCd_per());
		}
		currentForm.setOldModello(currentForm.getModello());
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, PeriodiciConstants.CAMBIO_BID))
			return false;

		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		if (ValidazioneDati.equals(idCheck, "button.periodici.previsionale.salva"))
			return ValidazioneDati.isFilled(currentForm.getFascicoli());

		return true;
	}

	public ActionForward calcola(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);

		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		ModelloPrevisionaleVO modello = currentForm.getModello();
		RicercaKardexPrevisionaleVO calc = (RicercaKardexPrevisionaleVO) currentForm.getRicercaKardex();
		calc.setBiblioteca(currentForm.getBiblioteca());
		SerieTitoloVO tit = new SerieTitoloVO();
		tit.setBid(currentForm.getEsame().getBid());
		calc.setOggettoRicerca(tit);
		calc.setModello(modello);
		calc.setDurata(currentForm.getDurata());
		calc.setTipo(DurataPrevisionaleType.valueOf(currentForm.getTipoDurata()));
		Date dtPubb = DateUtil.toDate(currentForm.getDataPrimoFasc());
		calc.setData_conv_pub(dtPubb);
		calc.setComparator(ValidazioneDati.invertiComparatore(PrevisionaleUtil.ORDINAMENTO_PREVISIONALE));

		//almaviva5_20110705 se la periodicità richiede l'indicazione del giorno di uscita
		//imposto i check al giorno inputato come data primo atteso
		if (currentForm.getOperazione() == TipoOperazione.PREVISIONALE) {
			PeriodicitaFascicoloType cd_per = PeriodicitaFascicoloType.fromString(modello.getCd_per());
			if (cd_per != null && cd_per.isLowerSettimanale() && dtPubb != null) {
				ModelloPrevisionaleDecorator mpd = new ModelloPrevisionaleDecorator(modello);
				mpd.setGiorniInclusi(new Integer[] { DateUtil.getDayOfWeek(dtPubb) });
				calc.setModello(mpd);
				currentForm.setModello(mpd);
			}
		}

		KardexPeriodicoVO kardex = null;
		try {
			kardex = delegate.calcolaPrevisionale(calc);
		} catch (ValidationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error == SbnErrorTypes.PER_ERRORE_GIORNI_OBBLIGATORI_PERIODICITA) {
				currentForm.setOperazione(TipoOperazione.PREVISIONALE_GIORNI);
				currentForm.setOldModello((ModelloPrevisionaleVO) currentForm.getModello().clone());
				return preparaConferma(
						request,
						mapping,
						currentForm,
						new ActionMessage(error.getErrorMessage(), e.getLabels()),
						BOTTONIERA);
			}
		}

		annullaConferma(mapping, currentForm);
		if (kardex == null)
			return mapping.getInputForward();

		ParametriPeriodici params = currentForm.getParametri().copy();
		params.put(ParamType.PARAMETRI_PREVISIONALE, calc);
		params.put(ParamType.PARAMETRI_RICERCA, calc);
		params.put(ParamType.KARDEX_PERIODICO, kardex);
		params.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.PREVISIONALE);
		ParametriPeriodici.send(request, params);

		return navi.goForward(mapping.findForward("esameKardex"));
	}

	@Override
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	public ActionForward inserisciEscludi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		ModelloPrevisionaleDecorator modello = (ModelloPrevisionaleDecorator) currentForm.getModello();
		modello.getDateEscluse().add(new ComboVO());

		return mapping.getInputForward();
	}

	public ActionForward eliminaEscludi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		Integer idx = currentForm.getIdxEscludi();
		if (idx == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
			return mapping.getInputForward();
		}

		ModelloPrevisionaleDecorator modello = (ModelloPrevisionaleDecorator) currentForm.getModello();
		ValidazioneDati.remove(modello.getDateEscluse(), idx.intValue());

		return mapping.getInputForward();
	}

	public ActionForward inserisciIncludi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		ModelloPrevisionaleDecorator modello = (ModelloPrevisionaleDecorator) currentForm.getModello();
		modello.getDateIncluse().add(new ComboVO());

		return mapping.getInputForward();
	}

	public ActionForward eliminaIncludi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		Integer idx = currentForm.getIdxIncludi();
		if (idx == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
			return mapping.getInputForward();
		}

		ModelloPrevisionaleDecorator modello = (ModelloPrevisionaleDecorator) currentForm.getModello();
		ValidazioneDati.remove(modello.getDateIncluse(), idx.intValue());

		return mapping.getInputForward();
	}

	@Override
	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)	throws Exception {
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		try {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			String def = (String) utenteEjb.getDefault(ConstantDefault.PER_KARDEX_ELEM_BLOCCHI);
			currentForm.getRicercaKardex().setNumeroElementiBlocco(Integer.valueOf(def));
		} catch (Exception e) {
			log.error("Errore caricamento default: ", e);
		}
	}

	private ActionForward annullaConferma(ActionMapping mapping, ActionForm form) {
		PrevisionaleForm currentForm = (PrevisionaleForm) form;
		currentForm.setModello(currentForm.getOldModello());
		currentForm.setConferma(false);
		currentForm.setInventario(null);
		currentForm.setOperazione(currentForm.getOperazioneInit());
		currentForm.setPulsanti(BOTTONIERA);
		return mapping.getInputForward();
	}

}

