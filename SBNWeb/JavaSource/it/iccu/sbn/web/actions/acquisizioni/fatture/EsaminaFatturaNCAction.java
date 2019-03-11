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
package it.iccu.sbn.web.actions.acquisizioni.fatture;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fatture.EsaminaFatturaNCForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class EsaminaFatturaNCAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{
	//private EsaminaFatturaForm esaFatture;
	//private EsaminaFatturaForm esaFattureNorm;



	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();

			esaFattureNC.setDisabilitaTutto(true);
			esaFattureNC.setEnableScorrimento(false);

			// gestione di riga del bottone lente di ordine
			ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioEsaminaFatturaSingle");
			// controllo che non riceva l'attributo di sessione di una lista supporto

			FatturaVO oggettoFatt=new FatturaVO() ;

			if (!esaFattureNC.isCaricamentoIniziale() && ricArr!=null )
			{
    			oggettoFatt=this.loadDatiFatturaPassata(ricArr);
    			//esaFattureNC.setCaricamentoIniziale(true);
			}
			else			// reimposta la fattura con i valori immessi
    		{
	    		if (esaFattureNC.getDatiFatturaNC()!=null)
				{
					FatturaVO oggettoTemp=esaFattureNC.getDatiFatturaNC();
					if (oggettoTemp.getFornitoreFattura()!=null || oggettoTemp.getCodBibl()!=null || oggettoTemp.getAnnoFattura()!=null  || oggettoTemp.getProgrFattura()!=null )
					{
						oggettoFatt=esaFattureNC.getDatiFatturaNC();
					}
				}

			}

			// gestione aggiornamento dinamico del cambio al modificarsi della valuta
			if (esaFattureNC.getValuta()!=null && !esaFattureNC.getValuta().equals("") )
			{
    			String valuta=esaFattureNC.getValuta();
    			String[] valuta_composto=valuta.split("\\|");
				// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
				String val_primaParte=valuta_composto[0];
				String val_secondaParte=valuta_composto[1].trim();
				if (val_primaParte!=null && val_primaParte.trim().length()==3 && val_secondaParte!=null && val_secondaParte.trim().length()>0)
				{
					oggettoFatt.setCambioFattura(Double.valueOf(val_secondaParte));
	    			oggettoFatt.setValutaFattura(val_primaParte);
				}
			}
			else
			{
				esaFattureNC.setValuta(oggettoFatt.getValutaFattura()+ "|" + oggettoFatt.getCambioFattura());
			}

			esaFattureNC.setDatiFatturaNC(oggettoFatt);
			int totFatture=0;
			if (oggettoFatt.getRigheDettaglioFattura()!=null && oggettoFatt.getRigheDettaglioFattura().size()>0)
			{
				totFatture=oggettoFatt.getRigheDettaglioFattura().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			esaFattureNC.setNumRigheFatt(totFatture);



			List arrListaIva=this.loadIVA();
			esaFattureNC.setListaIva(arrListaIva);

			List arrListaTipoImpegno=this.loadTipoImpegno();
			esaFattureNC.setListaTipoImpegno(arrListaTipoImpegno);

			List arrListaTipoOrdine=this.loadTipoOrdine();
			esaFattureNC.setListaTipoOrdine(arrListaTipoOrdine);

			List arrListaTipoFatt=this.loadTipoFatt();
			esaFattureNC.setListaTipoFatt(arrListaTipoFatt);

			List arrListaValuta=this.loadValuta(request, oggettoFatt.getCodBibl());
			esaFattureNC.setListaValuta(arrListaValuta);

			List arrListaStatoFatt=this.loadStatoFatt();
			esaFattureNC.setListaStatoFatt(arrListaStatoFatt);


			//esaFattureNC.setEnableScorrimento(esaFattureNorm.isEnableScorrimento());

			return mapping.getInputForward();
		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}


	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;
		try {

			ListaSuppFatturaVO ricSez=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
			if (esaFattureNC!=null &&  esaFattureNC.getRadioRFattNC()>=0)
			{
				int eleScelto=esaFattureNC.getRadioRFattNC();
				// individuazione della fattura scelta
				List<FatturaVO> listaFatt=new ArrayList<FatturaVO>();
				FatturaVO fattScelta=esaFattureNC.getDatiFatturaNC();
				StrutturaFatturaVO fattRigaScelta=fattScelta.getRigheDettaglioFattura().get(eleScelto);
				fattRigaScelta.setFattura(new StrutturaTerna("","",""));
				// imposta fattura di
				fattRigaScelta.getFattura().setCodice1(fattScelta.getAnnoFattura()); // anno
				fattRigaScelta.getFattura().setCodice2(fattScelta.getProgrFattura()); // prog
				fattRigaScelta.getFattura().setCodice3(String.valueOf(eleScelto+1)); // numero di riga della fattura scelto
				fattRigaScelta.setIDFattNC(fattScelta.getIDFatt());
				listaFatt.add(fattScelta);
				ricSez.setSelezioniChiamato(listaFatt);

				// aggiornare il selezioniChiamato aggiungendo la riga

				if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().trim().length()>0)
				{
					 	request.getSession().setAttribute("passaggioListaSuppFatturaVO", ricSez);
						ActionForward action = new ActionForward();
						action.setName("RITORNA");
						action.setPath(ricSez.getChiamante().trim()+".do");
						return action;
				}
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.ricerca"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward fatturaCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;
		try {
				return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;
// 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward ordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;

		try {
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
	}

	public ActionForward bilancio(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;

		try {

				return mapping.getInputForward();

			} catch (Exception e) {
				return mapping.getInputForward();
			}
	}


	private FatturaVO loadDatiFatturaPassata(ListaSuppFatturaVO criteriRicercaFattura) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<FatturaVO> fattureTrovate = new ArrayList();
		fattureTrovate = factory.getGestioneAcquisizioni().getRicercaListaFatture(criteriRicercaFattura);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaBilanci.setBilancio(bilanciTrovato.get(0));
		//for (int i=0; i<bilanciTrovato.size(); i++)
		//this.esaBilanci.setListaImpegni(bilanciTrovato.get(0).getDettagliBilancio());
		FatturaVO fatt=fattureTrovate.get(0);
		try {
			fatt.setImportoFatturaStr(Pulisci.VisualizzaImporto( fatt.getImportoFattura()));
			fatt.setScontoFatturaStr(Pulisci.VisualizzaImporto( fatt.getScontoFattura()));

			for (int w=0;  w < fatt.getRigheDettaglioFattura().size(); w++)
			{
				//BilancioDettVO elem = bil.getDettagliBilancio().get(w);
				fatt.getRigheDettaglioFattura().get(w).setImportoRigaFatturaStr(Pulisci.VisualizzaImporto( fatt.getRigheDettaglioFattura().get(w).getImportoRigaFattura()));
				fatt.getRigheDettaglioFattura().get(w).setSconto1RigaFatturaStr(Pulisci.VisualizzaImporto( fatt.getRigheDettaglioFattura().get(w).getSconto1RigaFattura()));
				fatt.getRigheDettaglioFattura().get(w).setSconto2RigaFatturaStr(Pulisci.VisualizzaImporto( fatt.getRigheDettaglioFattura().get(w).getSconto2RigaFattura()));
				//lista.add(elem);
			}
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			fatt.setImportoFatturaStr("0,00");
			fatt.setScontoFatturaStr("0,00");

		}


		return fatt;
	}






	private List loadTipoFatt() throws Exception {

		List lista = new ArrayList();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoFattura());
		return lista;
	}



    private List loadValuta( HttpServletRequest request,String biblSel ) throws Exception {
		//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//esaord.setListaValuta(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta()));
    	// esegui query su cambi di biblioteca
    	List<CambioVO> listaCambi=null;
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

    	ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		//String codB=Navigation.getInstance(request).getUtente().getCodBib();
		String codB=biblSel;
		String codVal="";
		String desVal="";
		String chiama=null;
		eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
		eleRicerca.setOrdinamento("");
		try {
			listaCambi = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(eleRicerca);
			}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    	List lista = new ArrayList();

    	boolean esisteEuro= false;

    	if (listaCambi==null || listaCambi.size()==0)
		{
    		StrutturaTerna elem = new StrutturaTerna("EUR|1",  "EUR EURO", "1");
			lista.add(elem);
		}
    	else
    	{
        	for (int w=0;  w < listaCambi.size(); w++)
    		{
        		StrutturaTerna elem = new StrutturaTerna(listaCambi.get(w).getCodValuta()+"|"+ String.valueOf(listaCambi.get(w).getTassoCambio()), listaCambi.get(w).getCodValuta()+" "+ listaCambi.get(w).getDesValuta().trim(), String.valueOf(listaCambi.get(w).getTassoCambio()));
    			if (listaCambi.get(w).getCodValuta().equals("EUR"))
    			{
    				esisteEuro=true;
    			}
        		lista.add(elem);
    		}
    	}

		//esaord.setListaValuta(lista);
		return lista;

    }




	private List loadStatoFatt() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1 - Registrata");
		lista.add(elem);
		elem = new StrutturaCombo("2","2 - Controllata");
		lista.add(elem);
		elem = new StrutturaCombo("3","3 - Ordine di pagamento emesso");
		lista.add(elem);
		elem = new StrutturaCombo("4","4 - Contabilizzata");
		lista.add(elem);

		//esaFattureNC.setListaStatoFatt(lista);
		return lista;
	}

	private List loadTipoOrdine() throws Exception {

		List lista = new ArrayList();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine());
		return lista;
	}

	private List loadTipoImpegno() throws Exception {
		List lista = new ArrayList();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale());

		return lista;
	}

	private List loadIVA() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("00","");
		lista.add(elem);
		elem = new StrutturaCombo("00","0 Esente");
		lista.add(elem);
		elem = new StrutturaCombo("20","20 IVA al 20%");
		lista.add(elem);
		elem = new StrutturaCombo("04","4 IVA al 4%");
		lista.add(elem);

		//esaFattureNC.setListaIva(lista);
		return lista;
	}

	private List getListaInventariFattura(String codPolo, String codBibF, int annoF, int progF, Locale locale,
			String ticket, int nRec, ActionForm form)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariFattura(codPolo, codBibF, annoF, progF, locale, ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			return null;
		}else{
			return blocco1.getLista();
		}
	}

	private List getListaInventariRigaFattura(String codPolo,String codBib,String codBibO, String codTipOrd, int annoOrd, int codOrd,  String codBibF, int annoF, int progF,int rigaF, Locale locale,
			String ticket, int nRec, ActionForm form)
	throws Exception {
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaInventariRigaFattura(codPolo, codBib,codBibO, codTipOrd,  annoOrd,  codOrd,codBibF, annoF, progF,  rigaF, locale, ticket, nRec);
		//(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd, int codOrd, String codBibF,  int annoF, int prgF, int rigaF, Locale locale, String ticket, int nRec)
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			return null;
		}else{
			return blocco1.getLista();
		}
	}




	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		EsaminaFatturaNCForm esaFattureNC= (EsaminaFatturaNCForm) form;
		if (!esaFattureNC.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				esaFattureNC.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.acquisizioni.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("GESTIONE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_FATTURE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return false;
	}


}
