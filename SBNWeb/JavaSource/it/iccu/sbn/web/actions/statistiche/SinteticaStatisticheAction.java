package it.iccu.sbn.web.actions.statistiche;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.statistiche.SinteticaStatisticheForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.statistiche.StatisticheDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

public final class SinteticaStatisticheAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(SinteticaStatisticheAction.class);
	
	private StatisticheDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new StatisticheDelegate(FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}
	
//	private CodiceConfigVO getSelected(ActionForm form) {
//		SinteticaStatisticheForm currentForm = (SinteticaStatisticheForm) form;
//		String selected = currentForm.getSelezRadio();
//		if (ValidazioneDati.strIsNull(selected) ) 
//			return null;
//
//		List<CodiceConfigVO> elencoCodici = currentForm.getElencoCodici();
//		for (CodiceConfigVO codice : elencoCodici) {
//			if (codice.getCdTabella().equals(selected))
//				return codice;
//		}
//		
//		return null;
//	}
	
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.tab1","tab1");
		map.put("button.tab2","tab2");
		map.put("button.creaExcel","creaExcel");
		map.put("button.conferma", "variabili");
		map.put("button.indietro", "indietro");
		return map;
	}

	class ordinaCodiceAsc implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getCdTabella() != null)
				sa = gp.getCdTabella();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getCdTabella() != null)
				sb = gp.getCdTabella();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sa).compareTo(sb)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaCodiceDec implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getCdTabella() != null)
				sa = gp.getCdTabella();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getCdTabella() != null)
				sb = gp.getCdTabella();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sb).compareTo(sa)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaTitoloAsc implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getDescrizione() != null)
				sa = gp.getDescrizione();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getDescrizione() != null)
				sb = gp.getDescrizione();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sa).compareTo(sb)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaTitoloDec implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getDescrizione() != null)
				sa = gp.getDescrizione();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getDescrizione() != null)
				sb = gp.getDescrizione();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sb).compareTo(sa)); // Ascending

		} // end compare

	} // end class StringComparator

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SinteticaStatisticheForm myForm = (SinteticaStatisticheForm) form;

		String cmd = request.getParameter("cmd");
		SinteticaStatisticheForm currentForm = (SinteticaStatisticheForm) form;
		String user = Navigation.getInstance(request).getUtente().getUserId();
		String codBib = Navigation.getInstance(request).getUtente().getCodBib();
		String codPolo = Navigation.getInstance(request).getUtente().getCodPolo();
		String ticket = Navigation.getInstance(request).getUtente().getTicket();

		log.debug("unspecified()");
		if (request.getAttribute("area") != null) {
			String area = (String)request.getAttribute("area");
			String descrArea = (String)request.getAttribute("descrArea");
			myForm.setDescrArea(descrArea);

			StatisticheDelegate delegate = getDelegate(request);
			DescrittoreBloccoVO blocco1 = delegate.getListaStatistiche(area, ticket, 1000);
			if (blocco1 == null || blocco1.getTotRighe() == 0){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.statistiche.NonEsistonoStatistichePerAreaSelezionata"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}
			if (blocco1.getTotRighe() == 1){
				currentForm.setSelezRadio("0");
			}

			currentForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setBloccoCorrente(blocco1.getNumBlocco());

			currentForm.setElencoStatistiche(blocco1.getLista());
			//
			
			currentForm.setFolder("tab1");
			
			this.loadAllineamento(currentForm);
			this.loadBordi(currentForm);
			this.loadColorDatiBck(currentForm);
			this.loadColorIntestazioniBck(currentForm);
			this.loadDimensioneCarattere(currentForm);
			this.loadOrientamento(currentForm);
			this.loadSpessoreBordo(currentForm);
			this.loadStileCarattere(currentForm);
			this.loadTipoCarattere(currentForm);
			this.loadTipologieRiga(currentForm);
//			this.loadQuery(currentForm);
			this.loadOrientamentoPagina(currentForm);

		}
		return mapping.getInputForward();

	}

	public ActionForward tab1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//visualizzazione folder lista statistichel
		SinteticaStatisticheForm myForm = (SinteticaStatisticheForm) form;
		myForm.setFolder("tab1");
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		return mapping.getInputForward();
	}	

	public ActionForward tab2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//visualizzazione folder creazione file excel
		SinteticaStatisticheForm myForm = (SinteticaStatisticheForm) form;
		myForm.setFolder("tab2");
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		return mapping.getInputForward();
	}	


	public ActionForward variabili(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaStatisticheForm myForm = (SinteticaStatisticheForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			if (request.getAttribute("disableConferma") != null){
				myForm.setDisable(true);
			}
			return mapping.getInputForward();
		}
		try {
			
			int sSel;
			request.setAttribute("checkS", myForm.getSelezRadio());
			if (myForm.getParamExcel() == null){
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.statistiche.creareExcel"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			String check = myForm.getSelezRadio();
			if (check != null && check.length() != 0) {
				sSel = Integer.parseInt(myForm.getSelezRadio());
				StatisticaVO scelS = myForm.getElencoStatistiche().get(sSel);
				myForm.getParamExcel().setQuery(scelS.getNomeStatistica());
				myForm.getParamExcel().setTipoQuery(scelS.getTipoQuery());
				myForm.getParamExcel().setArea(myForm.getDescrArea());
				scelS.setParExc(myForm.getParamExcel());
				request.setAttribute("dettaglioVariabili", scelS);
				
				return mapping.findForward("dettaglioVariabili");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.statistiche.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	
	public ActionForward creaExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SinteticaStatisticheForm myForm = (SinteticaStatisticheForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (myForm.getFolder() != null  && !myForm.getFolder().equals("tab2")){
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.statistiche.folderErrato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			ParametriExcelVO passa=new ParametriExcelVO();
			this.caricaPassa(passa,myForm);
			myForm.setParamExcel(passa);
			myForm.setFolder("tab1");
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		
		if (idCheck.equals("STATISTICHE")) {
			boolean auth;
			try {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().STATISTICHE);
				auth = true;
			} catch (UtenteNotAuthorizedException e) {
				auth = false;
			} catch (RemoteException e) {
				log.error("", e);
				return false;
			}

			Navigation navi = Navigation.getInstance(request);
			UserVO utente = navi.getUtente();
			return (auth && utente.isRoot());
		}

		return true;
	}

	private void loadAllineamento(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("CENTRE","Centrato");
		lista.add(elem);
		elem = new StrutturaCombo("JUSTIFY","Giustificato");
		lista.add(elem);
		elem = new StrutturaCombo("LEFT","Sinistra");
		lista.add(elem);
		elem = new StrutturaCombo("RIGHT","Destra");
		lista.add(elem);

		currForm.setListaAllineamentoStr(lista);
	}
	
	

	private void loadTipologieRiga(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","Titolo");
		lista.add(elem);
		elem = new StrutturaCombo("2","Intestazioni di colonne");
		lista.add(elem);
		elem = new StrutturaCombo("3","Dati");
		lista.add(elem);

		currForm.setListaTipologieRiga(lista);
	}

	private void loadOrientamentoPagina(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("LANDSCAPE","Orizzontale");
		lista.add(elem);
		elem = new StrutturaCombo("PORTRAIT","Verticale");
		lista.add(elem);
		currForm.setListaOrientamentoPagina(lista);
	}
	
	public void caricaPassa(ParametriExcelVO passa, SinteticaStatisticheForm currentForm)
	{
		passa.setOrientamentoPagina(currentForm.getOrientamentoPagina());
		passa.setTipoCarattereDat(currentForm.getTipoCarattereDat());
		passa.setDimensioneCarattereDat(currentForm.getDimensioneCarattereDat());
		passa.setBordiDat(currentForm.getBordiDat());
		passa.setSpessoreBordoDat(currentForm.getSpessoreBordoDat());
		passa.setColorIntestazioniBckDat(currentForm.getColorIntestazioniBckDat());
		passa.setAllineamentoDat(currentForm.getAllineamentoDat());
		passa.setOrientamentoDat(currentForm.getOrientamentoDat());
		passa.setTipoCarattereInt(currentForm.getTipoCarattereInt());
		passa.setDimensioneCarattereInt(currentForm.getDimensioneCarattereInt());
		passa.setStileCarattereInt(currentForm.getStileCarattereInt());
		passa.setBordiInt(currentForm.getBordiInt());
		passa.setSpessoreBordoInt(currentForm.getSpessoreBordoInt());
		passa.setColorIntestazioniBckInt(currentForm.getColorIntestazioniBckInt());
		passa.setAllineamentoInt(currentForm.getAllineamentoInt());
		passa.setOrientamentoInt(currentForm.getOrientamentoInt());
		passa.setTipoCarattereTit(currentForm.getTipoCarattereTit());
		passa.setDimensioneCarattereTit(currentForm.getDimensioneCarattereTit());
		passa.setStileCarattereTit(currentForm.getStileCarattereTit());
		passa.setBordiTit(currentForm.getBordiTit());
		passa.setSpessoreBordoTit(currentForm.getSpessoreBordoTit());
		passa.setColorIntestazioniBckTit(currentForm.getColorIntestazioniBckTit());
		passa.setAllineamentoTit(currentForm.getAllineamentoTit());
		passa.setOrientamentoTit(currentForm.getOrientamentoTit());
		//passa.setTipoQuery(currentForm.getTipoQueryStr());

	}

	private void loadBordi(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("NONE","Nessuno");
		lista.add(elem);
		elem = new StrutturaCombo("ALL","Bordato");
		lista.add(elem);
		elem = new StrutturaCombo("TOP","Sopra");
		lista.add(elem);
		elem = new StrutturaCombo("BOTTOM","Sotto");
		lista.add(elem);
		elem = new StrutturaCombo("LEFT","Sinistra");
		lista.add(elem);
		elem = new StrutturaCombo("RIGHT","Destra");
		lista.add(elem);

		currForm.setListaBordiStr(lista);
	}	

	private void loadColorDatiBck(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("LIGHT_GREEN","Verde");
		lista.add(elem);
		elem = new StrutturaCombo("VERY_LIGHT_YELLOW","Giallo");
		lista.add(elem);
		currForm.setListaColorDatiBckStr(lista);
	}	

	private void loadColorIntestazioniBck(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("LIGHT_GREEN","Verde");
		lista.add(elem);
		elem = new StrutturaCombo("VERY_LIGHT_YELLOW","Giallo");
		lista.add(elem);
		elem = new StrutturaCombo("LIGHT_TURQUOISE","Turchese");
		lista.add(elem);
		elem = new StrutturaCombo("AUTOMATIC","Automatico");
		lista.add(elem);

		currForm.setListaColorIntestazioniBckStr(lista);
	}	
	
	private void loadDimensioneCarattere(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("10","10");
		lista.add(elem);
		elem = new StrutturaCombo("12","12");
		lista.add(elem);
		elem = new StrutturaCombo("14","14");
		lista.add(elem);
		currForm.setListaDimensioneCarattereStr(lista);
	}	

	private void loadOrientamento(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("HORIZONTAL","Orizzontale");
		lista.add(elem);
		elem = new StrutturaCombo("VERTICAL","Verticale");
		lista.add(elem);
		currForm.setListaOrientamentoStr(lista);
	}	

	private void loadSpessoreBordo(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("NONE","Nessuno");
		lista.add(elem);
		elem = new StrutturaCombo("THIN","Sottile");
		lista.add(elem);
		elem = new StrutturaCombo("MEDIUM","Medio");
		lista.add(elem);
		currForm.setListaSpessoreBordoStr(lista);
	}	

	private void loadStileCarattere(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("BOLD","Bold");
		lista.add(elem);
		elem = new StrutturaCombo("NO_BOLD","Normale");
		lista.add(elem);
		currForm.setListaStileCarattereStr(lista);
	}
	
	private void loadTipoCarattere(SinteticaStatisticheForm currForm) throws Exception {
		List lista = new ArrayList();
		
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("ARIAL","Arial");
		lista.add(elem);
		elem = new StrutturaCombo("TIMES","Times");
		lista.add(elem);
		elem = new StrutturaCombo("TAHOMA","Tahoma");
		lista.add(elem);
		elem = new StrutturaCombo("COURIER","Courier");
		lista.add(elem);
		currForm.setListaTipoCarattereStr(lista);
	}
	
}