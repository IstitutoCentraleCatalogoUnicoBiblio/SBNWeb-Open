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
package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.EsaminaOrdineModForm;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OrdineBaseForm;
import it.iccu.sbn.web.actionforms.periodici.ListaOrdiniPeriodiciForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.OrdiniWebUtil;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class OrdineBaseAction extends AcquisizioniBaseAction implements SbnAttivitaChecker {

	protected static final String[] BOTTONIERA_ESAMINA = new String[] {
		"ricerca.button.inventari",
		"ricerca.button.listaInventariOrdine",
		"ricerca.button.schedoneRinnovi",
		"ricerca.button.indietro"
	};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		//almaviva5_20101201 periodici
		map.put("ordine.button.scegliFascicolo", "scegliFascicolo");
		return map;
	}

	public ActionForward scegliFascicolo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OrdineBaseForm currentForm = (OrdineBaseForm) form;
		OrdiniVO ordine = currentForm.getDatiOrdine();

		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(ordine.getCodPolo());
		bib.setCod_bib(ordine.getCodBibl());
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		return delegate.sifScegliFascicoloPerCreaOrdine(bib, ordine, NavigazioneAcquisizioni.SIF_KARDEX_RETURN);
	}

	protected void checkFascicoloPerCreazioneOrdine(HttpServletRequest request,	ActionForm form) {

		List<FascicoloVO> ff = (List<FascicoloVO>) request.getAttribute(NavigazioneAcquisizioni.SIF_KARDEX_RETURN);
		if (!ValidazioneDati.isFilled(ff))
			return;

		OrdineBaseForm currentForm = (OrdineBaseForm) form;
		PeriodiciUtil.associaFascicoloCreazioneOrdine(currentForm.getDatiOrdine(), ff.get(0));
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		initCombo(form);
		//almaviva5_20101202 #4004
		checkFascicoloPerCreazioneOrdine(request, form);

		return super.execute(mapping, form, request, response);
	}

	private void initCombo(ActionForm form) throws Exception {
		OrdineBaseForm currentForm = (OrdineBaseForm) form;
		currentForm.setListaTipoLavorazione(OrdiniWebUtil.estraiCodiciLavorazioneOrdine());

	}

	protected boolean isSifEsameOrdinePeriodico(HttpServletRequest request, ActionForm form) {
		//almaviva5_20110208 sif da lista ordini periodico
		Navigation navi = Navigation.getInstance(request);
		NavigationCache cache = navi.getCache();
		NavigationElement e = cache.getElementByForm(form);
		ActionForm caller = cache.getElementAt(e.getPosition() - 1).getForm();
		return (caller instanceof ListaOrdiniPeriodiciForm);
	}

	protected ActionForward controllaPrezzi(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//rp20110926
		OrdineBaseForm currentForm = (OrdineBaseForm) form;
		if (!currentForm.getPrezzoStr().equals(Pulisci.VisualizzaImporto(currentForm.getPrezzoOrdine()))){
			String amount=currentForm.getPrezzoStr().trim();
			Double risult=Pulisci.ControllaImporto(amount);
			currentForm.setPrezzoOrdine(risult);
		}
		if (currentForm.getPrezzoOrdine()  == 0 ){//formPrezzo
			if (currentForm.getDatiOrdine().getPrezzoEuroOrdine()== 0){
				if (currentForm.getDatiOrdine().getTipoOrdine() != null &&
						(currentForm.getDatiOrdine().getTipoOrdine().equals("A") ||
								currentForm.getDatiOrdine().getTipoOrdine().equals("V"))){
					if (currentForm.getDatiOrdine().getBilancio().getCodice3() != null &&
							(currentForm.getDatiOrdine().getBilancio().getCodice3().equals("1") ||
									currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2"))){
						//bug 0004467 collaudo rp
						request.setAttribute("errore", "errore");
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.PerAcquistoEVisioneTrattenutaPrezzoObbligatorio"));
						this.saveErrors(request, errors);
						currentForm.setConferma(false);
						return mapping.getInputForward();
					}
				}
			}			//
			ActionForward forward = controllaPrezzoSuOrdine(mapping, request, currentForm);
			if (forward != null){
				return forward;
			}
			this.controllaPrezzo(request, form);
			this.controllaPrezzoEuro(request, form);
			return null;
		}else{
			if (currentForm.getPrezzoOrdine() == (currentForm.getDatiOrdine().getPrezzoOrdine())){
				if (currentForm.getValuta() != null
						&& (currentForm.getValuta().equals(currentForm.getDatiOrdine().getValutaOrdine()))){
					//ok ins/mod
					this.controllaPrezzo(request, form);
					this.controllaPrezzoEuro(request, form);
					return null;
				}else{
					//converti
					//this.loadValuta( currentForm, request);
					this.converti(mapping, form, request, response);
					if (currentForm.getValuta().equals(currentForm.getDatiOrdine().getValutaOrdine())){
						if (currentForm.getPrezzoOrdine() == currentForm.getDatiOrdine().getPrezzoOrdine()){
							// ok mod
							this.controllaPrezzo(request, form);
							this.controllaPrezzoEuro(request, form);
						}
//						else{
//							//msg conferma
//							ActionMessages errors = new ActionMessages();
//							errors.add("generico", new ActionMessage("errors.acquisizioni.controllarePrezzoOrdineEPrezzoEuroOrdine"));
//							this.saveErrors(request, errors);
//							return mapping.getInputForward();
//						}
					}else{
						this.controllaPrezzo(request, form);
						this.controllaPrezzoEuro(request, form);
						currentForm.getDatiOrdine().setValutaOrdine(currentForm.getValuta());
					}
				}
			}else{
				this.converti(mapping, form, request, response);
				if (currentForm.getValuta() != null
						&& (currentForm.getValuta().equals(currentForm.getDatiOrdine().getValutaOrdine()))){
					this.controllaPrezzo(request, form);
					this.controllaPrezzoEuro(request, form);
				}else{
					//msg conferma
					this.controllaPrezzo(request, form);
					this.controllaPrezzoEuro(request, form);
					currentForm.getDatiOrdine().setValutaOrdine(currentForm.getValuta());
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.valutaCambiataConfermare"));
					this.saveErrors(request, errors);
					currentForm.setConferma(true);
					if (currentForm instanceof EsaminaOrdineModForm){
						String tipoOperazione = (String) request.getAttribute("salva");
						if (tipoOperazione != null){
							EsaminaOrdineModForm currentForm1 = (EsaminaOrdineModForm)form;
							currentForm1.setPressioneBottone(tipoOperazione);
						}
					}
					return mapping.getInputForward();
				}
			}
		}
		return null;
	}

	protected ActionForward controllaPrezzoSuOrdine(ActionMapping mapping,
			HttpServletRequest request, OrdineBaseForm currentForm) throws Exception {

		if (currentForm.getDatiOrdine().getPrezzoOrdine() == 0 ){//|| currentForm.getPrezzoStr().trim().equals("")){
			if (currentForm.getDatiOrdine().getPrezzoEuroOrdine()== 0){
//				if (currentForm.getDatiOrdine().getTipoOrdine() != null &&
//						(currentForm.getDatiOrdine().getTipoOrdine().equals("A") ||
//								currentForm.getDatiOrdine().getTipoOrdine().equals("V"))){
//					if (currentForm.getDatiOrdine().getBilancio().getCodice3() != null &&
//							(currentForm.getDatiOrdine().getBilancio().getCodice3().equals("1") ||
//									currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2"))){
//						//bug 0004467 collaudo rp
//						request.setAttribute("errore", "errore");
//						ActionMessages errors = new ActionMessages();
//						errors.add("generico", new ActionMessage("errors.acquisizioni.PerAcquistoEVisioneTrattenutaPrezzoObbligatorio"));
//						this.saveErrors(request, errors);
//					}
//				}
			}else{
				if (currentForm.getDatiOrdine().getValutaOrdine() != null && currentForm.getDatiOrdine().getValutaOrdine().equals("EUR")){
					// se valuta è Eur e record ordine pregresso con prezzo a 0 e prezzoEur <> 0
					//imposto nel prezzo della form il prezzoEur presente sull'ordine
					currentForm.setPrezzoOrdine(currentForm.getDatiOrdine().getPrezzoEuroOrdine());
					currentForm.setPrezzoStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoEuroOrdine()));
				}else{
					//msg di base dati squadrata da sanare
					currentForm.setPrezzoOrdine(currentForm.getDatiOrdine().getPrezzoOrdine());
					currentForm.setPrezzoEuroOrdine(currentForm.getDatiOrdine().getPrezzoEuroOrdine());
					request.setAttribute("errore", "errore");
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.baseDatiSquadrataDaSanare"));
					this.saveErrors(request, errors);
//					return mapping.getInputForward();
				}
			}
		}else{
				currentForm.setPrezzoOrdine(currentForm.getDatiOrdine().getPrezzoOrdine());
				currentForm.setPrezzoStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoOrdine()));
		}
		return null;
	}

	protected void controllaPrezzo(HttpServletRequest request, ActionForm form) throws Exception {
		OrdineBaseForm currentForm = (OrdineBaseForm) form;
		String amount=currentForm.getPrezzoStr().trim();
		Double risult=Pulisci.ControllaImporto(amount);
		currentForm.getDatiOrdine().setPrezzoOrdine(risult);
		currentForm.getDatiOrdine().setPrezzoOrdineStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoOrdine()));
		currentForm.setPrezzoOrdine(risult);
		currentForm.setPrezzoStr(Pulisci.VisualizzaImporto( currentForm.getPrezzoOrdine()));
	}

	protected void controllaPrezzoEuro(HttpServletRequest request, ActionForm form) throws Exception {
		OrdineBaseForm currentForm = (OrdineBaseForm) form;
		String amountEur = null;
//		if (currentForm.getDatiOrdine().getPrezzoEuroOrdineStr() != null && !currentForm.getDatiOrdine().getPrezzoEuroOrdineStr().equals("0,00")){
//			amountEur=currentForm.getDatiOrdine().getPrezzoEuroOrdineStr().trim();
//		}else{
//			amountEur=currentForm.getPrezzoStr().trim();
//		}
		if (currentForm.getDatiOrdine().getPrezzoEuroOrdine() != 0){
			amountEur=(Pulisci.VisualizzaImporto(currentForm.getDatiOrdine().getPrezzoEuroOrdine()));
		}else{
			amountEur=currentForm.getPrezzoStr().trim();
		}
		Double risultEur=Pulisci.ControllaImporto(amountEur);
		currentForm.getDatiOrdine().setPrezzoEuroOrdine(risultEur);
		currentForm.getDatiOrdine().setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(currentForm.getDatiOrdine().getPrezzoEuroOrdine()));
		currentForm.setPrezzoEuroOrdine(risultEur);
		currentForm.setPrezzoEurStr(Pulisci.VisualizzaImporto( currentForm.getPrezzoEuroOrdine()));
	}

	protected ActionForward converti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	OrdineBaseForm currentForm = (OrdineBaseForm) form;

    	for (int w=0;  w < currentForm.getListaValuta().size() ; w++)
		{
    		StrutturaTerna ele=(StrutturaTerna) currentForm.getListaValuta().get(w);
//    		if (ele.getCodice1().equals(esaord.getvDatiOrdine().getValutaOrdine()))
        		if (ele.getCodice1().equals(currentForm.getValuta()))
    		{

//    			String amount=esaord.getDatiOrdine().getPrezzoOrdineStr().trim();
    			String amount=currentForm.getPrezzoStr().trim();
    		    Double risult=Pulisci.ControllaImporto(amount);
    		    currentForm.getDatiOrdine().setPrezzoOrdine(risult);
    		    currentForm.getDatiOrdine().setPrezzoOrdineStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoOrdine()));
//    		    esaord.setPrezzoStr(Pulisci.VisualizzaImporto( esaord.getDatiOrdine().getPrezzoOrdine()));

    		    // da convertire
    			double prodotto=Double.valueOf(ele.getCodice3().trim())* risult; // valore double del prezzo in euro
    		    currentForm.getDatiOrdine().setPrezzoEuroOrdine(prodotto);
    		    currentForm.getDatiOrdine().setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoEuroOrdine()));
//    		    esaord.setPrezzoEurStr(Pulisci.VisualizzaImporto( esaord.getDatiOrdine().getPrezzoEuroOrdine()));

    			break;
    		}
		}

    	return mapping.getInputForward();
	}

	protected void loadValuta(OrdineBaseForm currentForm, HttpServletRequest request) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		ListaSuppCambioVO eleRicerca = new ListaSuppCambioVO();
		// carica i criteri di ricerca da passare alla esamina
		UserVO utente = Navigation.getInstance(request).getUtente();
		String codP = utente.getCodPolo();
		String codB = utente.getCodBib();

		OrdiniVO ordine = currentForm.getDatiOrdine();
		if (ordine != null
				&& ordine.getCodBibl() != null
				&& ordine.getCodBibl().trim().length() > 0) {
			codB = ordine.getCodBibl();
		}
		if (ordine != null
				&& ordine.getCodPolo() != null
				&& ordine.getCodPolo().trim().length() > 0) {
			codP = ordine.getCodPolo();
		}

		String codVal = "";
		String desVal = "";
		String chiama = null;
		eleRicerca = new ListaSuppCambioVO(codP, codB, codVal, desVal, chiama);
		eleRicerca.setOrdinamento("");
		List<CambioVO> listaCambi = factory
					.getGestioneAcquisizioni().getRicercaListaCambiHib(eleRicerca);

		List<StrutturaTerna> lista = new ArrayList<StrutturaTerna>();

		for (CambioVO cambio : listaCambi) {
			StrutturaTerna st = new StrutturaTerna(cambio.getCodValuta(),
					cambio.getDesValuta().trim(), String.valueOf(cambio.getTassoCambio()));

			lista.add(st);
		}

		AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
		CambioVO rif = delegate.getValutaRiferimento(codP, codB);
		if (rif == null)
			throw new ValidationException("assenzaRisultati", ValidationExceptionCodici.assenzaRisultati);
		currentForm.setValutaRif(rif);

		if (ordine != null
				&& !ValidazioneDati.isFilled(ordine.getValutaOrdine())) {
			if (currentForm.getValuta() == null) {
				ordine.setValutaOrdine(rif.getCodValuta());
				currentForm.setValuta(ordine.getValutaOrdine());
			} else {
				ordine.setValutaOrdine(currentForm.getValuta());
			}
		} else {
			if (currentForm.getValuta() == null) {
				currentForm.setValuta(ordine.getValutaOrdine());
			} else {
				ordine.setValutaOrdine(currentForm.getValuta());
			}
		}

		currentForm.setListaValuta(lista);

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		//almaviva5_20140617 #5078
		if (idCheck.equals("VALUTA")) {
			OrdineBaseForm currentForm = (OrdineBaseForm) form;
			return currentForm.getValutaRif() != null;
		}

		return false;
	}

}
