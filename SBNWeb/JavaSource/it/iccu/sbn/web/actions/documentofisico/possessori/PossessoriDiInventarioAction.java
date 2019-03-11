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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		Alessandro Segnalini - Inizio Codifica Marzo 2008

package it.iccu.sbn.web.actions.documentofisico.possessori;


import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDiInventarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriDiInventarioSinteticaForm;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriSinteticaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PossessoriDiInventarioAction extends SinteticaLookupDispatchAction  implements SbnAttivitaChecker  {

	private static Log log = LogFactory.getLog(PossessoriSinteticaAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String,String> getKeyMethodMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("button.blocco", "blocco");
		map.put("documentofisico.bottone.indietro", "indietro");
		map.put("analitica.button.nuovolegame", "nuovoLegamePossessore");
		map.put("analitica.button.modlegame", "modificaLegamePossessore");
		map.put("analitica.button.canclegame","cancellaLegamePossessore");
		map.put("sintetica.button.analitica", "analiticaPossessori");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PossessoriDiInventarioSinteticaForm myForm = (PossessoriDiInventarioSinteticaForm)form;
		if(Navigation.getInstance(request).isFromBar() ) {
					return mapping.getInputForward();
		}
//		if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("DOCFISVIS")){
//			myForm.setProv("DOCFISVIS");
//		}
		//"DOCFIS"
		String param[] = this.leggiParametriDaRequest(request);
		String codBib 	= param[0];
		String codSerie = param[1];
		String codInv 	= param[2];
		String descBib 	= param[3];
		String prov		= param[4];
		List possPerInv = new ArrayList();
		int nRec=0;
		ActionMessages errors = new ActionMessages();
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			nRec = Integer.parseInt(((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));
		} catch (Exception e) {
			errors.clear();
			errors.add("generico", new ActionMessage("errors.documentofisico.Faild"));
			this.saveErrors(request, errors);
		}

		try {
			possPerInv = leggiPossessoriPerInventario( codSerie,  codInv ,  codBib,
					Navigation.getInstance(request).getUtente().getCodPolo() ,Navigation.getInstance(request).getUserTicket() , nRec, myForm );
		} catch (ValidationException e) {
			errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(true);
		} catch (DataException e) {
			/* almaviva7 02/07/2008 11.37
			errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico.PossessoriInventariNotFound"));
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(true);
			*/
			// Ignoriamo eccezione se non abbiamo trovato possessori per un determinato inventario per permettere,
			// tramite lista sintetica vuota, la creazione di possessori

		}catch (Exception e) {
			errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(true);
		}


		myForm.setElemPerBlocchi(nRec);
		myForm.setListaPossessori(possPerInv);
		if (myForm.getListaPossessori() != null && myForm.getListaPossessori().size()==1)
			myForm.setSelezRadio( ((PossessoriDiInventarioVO)(myForm.getListaPossessori().get(0))).getPid() );

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		myForm.setCodBib(codBib);
		myForm.setCodInv(codInv);
		myForm.setCodSerie(codSerie);
		myForm.setProv(prov);//almaviva2 24/07/2008
//		myForm.setProv("xyz"); // almaviva7 02/07/2008 11.37
		myForm.setDescBib(descBib);

		impostaParametriInRequest(request,descBib,codBib,codSerie,codInv,prov);
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		PossessoriSinteticaForm myForm = (PossessoriSinteticaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (myForm.getTotRighe() == 0 || myForm.getBloccoSelezionato() > myForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.noElemPerBloc"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}
		int numBlocco = myForm.getBloccoSelezionato();
		String idLista = myForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco>1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				if (myForm.getListaPossessori() != null )
					myForm.getListaPossessori().addAll(bloccoVO.getLista());
				}
		}
		return mapping.getInputForward();
	}

	private void impostaParametriInRequest(HttpServletRequest request,String descBib,String codBib,String serie,String inv,String prov){
		request.setAttribute("codBib",codBib);
		request.setAttribute("codSerie",serie);
		request.setAttribute("codInvent",inv);
		request.setAttribute("descrBib",descBib);
		request.setAttribute("prov", prov);
	}
	private String[] leggiParametriDaRequest(HttpServletRequest request){
		String parametri [] = new String [5];
		parametri [0] = (String)request.getAttribute("codBib");
		parametri [1] = (String)request.getAttribute("codSerie");
		parametri [2] = (String)request.getAttribute("codInvent");
		parametri [3] = (String)request.getAttribute("descrBib");
		parametri [4] = (String)request.getAttribute("prov");
		return parametri;
	}
	private List leggiPossessoriPerInventario(String codSerie, String codInv , String codBib, String codPolo ,String ticket ,int nRec ,PossessoriDiInventarioSinteticaForm myForm ) throws Exception {
		List lista = new ArrayList();
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaPossessoriDiInventario(codSerie, codInv, codBib, codPolo ,ticket, nRec);
		if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
			return null;
		}else{
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((myForm.getTotBlocchi() > 1)){
				myForm.setAbilitaBottoneCarBlocchi(false);
			}else{
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			myForm.setListaPossessori(blocco1.getLista());
			return blocco1.getLista();
		}
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward nuovoLegamePossessore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		PossessoriDiInventarioSinteticaForm myForm = (PossessoriDiInventarioSinteticaForm)form;

		impostaParametriInRequest(request,myForm.getDescBib(),myForm.getCodBib(),myForm.getCodSerie(),myForm.getCodInv(),myForm.getProv());

		return Navigation.getInstance(request).goForward(mapping.findForward("newPossessorePerInventario"));
	}



	public ActionForward cancellaLegamePossessore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		PossessoriDiInventarioSinteticaForm myForm = (PossessoriDiInventarioSinteticaForm)form;
		PossessoriDiInventarioVO rec = null;
		PossessoriDiInventarioVO recApp = null;
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO ret=null; // Arge
		List listaPossInve = myForm.getListaPossessori();
		if (myForm.getSelezRadio() != null) {
			String pid = myForm.getSelezRadio();
			resetToken(request);
			for (int index = 0; index < listaPossInve.size(); index++) {
				rec = (PossessoriDiInventarioVO)listaPossInve.get(index);
				if (rec.getPid().trim().equals(pid)){
					recApp=(PossessoriDiInventarioVO)listaPossInve.get(index);
					break;
				}
			}
			try {
				String parametriDOCFIS [] = (String [])request.getSession().getAttribute("parametriDOCFIS");
//				ret = cancellaLegamePossessoreInventario(recApp,parametriDOCFIS , Navigation.getInstance(request).getUtente().getCodPolo() ,Navigation.getInstance(request).getUtente().getFirmaUtente());

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				ret = factory.getGestioneDocumentoFisico().cancellaLegameInventario(recApp.getPid(),
						recApp.getCodInv(),
						Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(),
						Navigation.getInstance(request).getUtente().getFirmaUtente(),
						Navigation.getInstance(request).getUserTicket()); // almaviva7 03/07/2008 12.54




			} catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				// correggere il file di puntamento dell'errore
				errors.add("generico", new ActionMessage(e.getMessage()));
				this.saveErrors(request, errors);
			    return mapping.getInputForward();
			}
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		impostaParametriInRequest(request, myForm.getDescBib(), myForm.getCodBib(), myForm.getCodSerie(), myForm.getCodInv(), myForm.getProv());
		return unspecified(mapping, form,	request, response);
		//return Navigation.getInstance(request).goBack();

	}
	public ActionForward analiticaPossessori(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		PossessoriDiInventarioSinteticaForm myForm = (PossessoriDiInventarioSinteticaForm)form;
		String pidSel = myForm.getSelezRadio();


		if (myForm.getSelezRadio() != null) {
			String pidNome = myForm.getSelezRadio();
			if (myForm.getProv().trim().equals("DOCFISVIS")){
				// analitica ridotta
				request.setAttribute("presenzaTastoVaiA","NO");
			}

			request.setAttribute("bid", pidNome.substring(0, 10));
			//request.setAttribute("listaPossessoriBySint", myForm.getListaPossessori());
			request.setAttribute("tipoAuthority", "PP");
			request.setAttribute("livRicerca", "P");
			resetToken(request);
			return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.selObblOggSint"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}
/* Arge
	private String cancellaLegamePossessoreInventario(PossessoriDiInventarioVO recAppo ,String [] parametri,String codPolo, String utente) throws Exception {
		PossessoriDiInventarioVO possInv = new PossessoriDiInventarioVO();
		String ret=null;
		possInv.setCodBib(recAppo.getCodBib());

		possInv.setCodInv(recAppo.getCodInv());
		possInv.setCodLegame(recAppo.getCodLegame());
		possInv.setCodPolo(codPolo);
		possInv.setCodSerie(recAppo.getCodSerie());
		possInv.setDataAgg(DaoManager.now());
		possInv.setFl_canc("S");
		possInv.setNotaLegame(recAppo.getNotaLegame());
		possInv.setPid(recAppo.getPid());
		possInv.setUteAgg(utente);
		possInv.setUteIns(recAppo.getUteIns());
		possInv.setDataIns(recAppo.getDataIns());
		possInv.setForma(recAppo.getForma());
		possInv.setNome(recAppo.getNome());


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//		ret = factory.getGestioneDocumentoFisico().legamePossessoreInventario(possInv);
		ret = factory.getGestioneDocumentoFisico().cancellaLegameInventario(recAppo.getPid(), Navigation.getInstance(request).getUtente().getCodPolo(), recAppo.getCodBib(), recAppo.get Navigation.getInstance(request).getUtente().getFirmaUtente()); // almaviva7 03/07/2008 12.54
		return ret;

	}
*/
	public ActionForward modificaLegamePossessore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		PossessoriDiInventarioSinteticaForm myForm = (PossessoriDiInventarioSinteticaForm)form;
		PossessoriDiInventarioVO rec = null;
		PossessoriDiInventarioVO recApp = null;
		List listaPossInve = myForm.getListaPossessori();
		if (myForm.getSelezRadio() != null) {
			String pid = myForm.getSelezRadio();
			resetToken(request);
			for (int index = 0; index < listaPossInve.size(); index++) {
				rec = (PossessoriDiInventarioVO)listaPossInve.get(index);
				if (rec.getPid().trim().equals(pid)){
					recApp=(PossessoriDiInventarioVO)listaPossInve.get(index);
					break;
				}
			}

			request.setAttribute("PROVLEGPOSINV", recApp);
			request.setAttribute("descBib", myForm.getDescBib());
			String parametri [] = new String [5];
			parametri [0] = (myForm.getCodBib());
			parametri [1] = (myForm.getCodSerie());
			parametri [2] = (myForm.getCodInv());
			parametri [3] = (myForm.getDescBib());
			parametri [4] = (myForm.getProv());
			request.getSession().setAttribute("parametriDOCFIS", parametri);
			request.getSession().setAttribute("modificaLegamePossessoreInventario","Si");

			return Navigation.getInstance(request).goForward(mapping.findForward("modPossessorePerInventario"));
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.selObblOggSint"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	} // End modificaLegamePossessore

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		PossessoriDiInventarioSinteticaForm myForm = (PossessoriDiInventarioSinteticaForm) form;
		try{
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GDF_POSSESSORI, Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
