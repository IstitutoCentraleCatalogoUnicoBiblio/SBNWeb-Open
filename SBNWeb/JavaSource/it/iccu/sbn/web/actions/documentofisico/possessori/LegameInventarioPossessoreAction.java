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
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDiInventarioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriLegameInventarioForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
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
import org.apache.struts.actions.LookupDispatchAction;

public class LegameInventarioPossessoreAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(PossessoriDettaglioAction.class);

	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("dettaglio.button.salva", "salvaPoss");
		map.put("dettaglio.button.annulla", "annullaPoss");
//		map.put("analitica.button.modlegame", "modificaLegamePossessoreInventario"); // almaviva7 02/07/2008 15.16
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PossessoriLegameInventarioForm  possLegame= (PossessoriLegameInventarioForm) form;

		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		String parametriDOCFIS[] = (String []) request.getSession().getAttribute("parametriDOCFIS");

		String pid="";
		String desc="";
		String codeBib="";
		String descBib="";
		String codeSerie="";
		String codeInv="";

		if (request.getAttribute("PROVLEGPOSINV") ==  null){
			if (request.getAttribute("pidFA") != null && request.getAttribute("descrFA") != null){
				pid=(String)request.getAttribute("pidFA");//rosa
				desc=(String)request.getAttribute("descrFA");//rosa
			}else{
				pid=(String)request.getAttribute("bid");
				desc=(String)request.getAttribute("desc");
			}
			codeBib=parametriDOCFIS[0];
			codeSerie=parametriDOCFIS[1];
			codeInv=parametriDOCFIS[2];
			descBib=parametriDOCFIS[3];
			possLegame.setProv("I");
		} else {
			PossessoriDiInventarioVO app = (PossessoriDiInventarioVO)request.getAttribute("PROVLEGPOSINV");
			pid=app.getPid();
			desc=app.getNome();
			codeBib=app.getCodBib();
			codeSerie=app.getCodSerie();
			codeInv=app.getCodInv();
			descBib=(String)request.getAttribute("descBib");
			possLegame.setNotaAlLegame(app.getNotaLegame());
			possLegame.setProv("M");
			possLegame.setCodeLegame(app.getCodLegame());
		}


		possLegame.setCodeBib(codeBib);
		possLegame.setDescBib(descBib);
		possLegame.setPidOrigine(pid);
		possLegame.setNomeOrigine(desc);
		possLegame.setCodeInv(codeInv);
		possLegame.setCodeSerie(codeSerie);



		this.saveToken(request);

		return mapping.getInputForward();
	}

	public ActionForward salvaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws DataException,Exception {

		PossessoriLegameInventarioForm  possLegame= (PossessoriLegameInventarioForm) form;
		//nome obbligatorio
		if (possLegame.getCodeLegame().trim().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.codeLegame"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (possLegame.getNotaAlLegame().trim().length() > 320) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.ilTestoDellaNotaNonPuoSuperare320Caratteri"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String ret = null ;
		try {
				String parametriDOCFIS [] = (String [])request.getSession().getAttribute("parametriDOCFIS");
				boolean modifica = false;
				if (request.getAttribute("modificaLegamePossessoreInventario") ==  null)
					modifica = true;
				ret = inserisciLegamePossessoreInventario(possLegame,parametriDOCFIS , Navigation.getInstance(request).getUtente().getCodPolo() ,Navigation.getInstance(request).getUtente().getFirmaUtente(), modifica);

		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			// correggere il file di puntamento dell'errore
			errors.add("generico", new ActionMessage(e.getMessage()));
			this.saveErrors(request, errors);
		    return mapping.getInputForward();
		}catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			// correggere il file di puntamento dell'errore
			errors.add("generico", new ActionMessage(e.getMessage()));
			this.saveErrors(request, errors);
		    return mapping.getInputForward();
		}

		//Navigation.getInstance(request).purgeThis();
		String parametriDOCFIS [] = (String [])request.getSession().getAttribute("parametriDOCFIS");
		request.getSession().removeAttribute("parametriDOCFIS");
		request.setAttribute("codBib",parametriDOCFIS [0]);
		request.setAttribute("codSerie",parametriDOCFIS [1]);
		request.setAttribute("codInvent",parametriDOCFIS [2]);
		request.setAttribute("descrBib",parametriDOCFIS [3]);
		request.setAttribute("prov",parametriDOCFIS [4]);

		if (possLegame.getProv().trim().equals("I")){

			return Navigation.getInstance(request).goBack(mapping.findForward("possessoriInventario"));
		}
		else
			return Navigation.getInstance(request).goBack(mapping.findForward("possessoriInventario"));
	}


	public ActionForward annullaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//		String parametriDOCFIS [] = (String [])request.getAttribute("parametriDOCFIS");
//		request.setAttribute("parametriDOCFIS", parametriDOCFIS);

		return Navigation.getInstance(request).goBack(true);

	}

// almaviva7 02/07/2008 15.16
//	private String inserisciLegamePossessoreInventario(PossessoriLegameInventarioForm  possLegame,String [] parametri,String codPolo, String utente) throws Exception {
private String inserisciLegamePossessoreInventario(PossessoriLegameInventarioForm  possLegame,String [] parametri,String codPolo, String utente, boolean modifica) throws Exception {
		PossessoriDiInventarioVO possInv = new PossessoriDiInventarioVO();
		String ret=null;
		possInv.setCodBib(possLegame.getCodeBib());
		possInv.setCodInv(""+parametri[2]);
		possInv.setCodLegame(possLegame.getCodeLegame());
		possInv.setCodPolo(codPolo);
		possInv.setCodSerie(parametri[1]);
		possInv.setDataAgg(DaoManager.now());
		possInv.setDataIns(DaoManager.now());
		possInv.setFl_canc("N");
		possInv.setNotaLegame(possLegame.getNotaAlLegame());
		possInv.setPid(possLegame.getPidOrigine());
		possInv.setUteAgg(utente);
		possInv.setUteIns(utente);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (modifica)
			ret = factory.getGestioneDocumentoFisico().modificaLegamePossessoreInventario(possInv);
		else
			ret = factory.getGestioneDocumentoFisico().legamePossessoreInventario(possInv);
		return ret;

	}



/*
// almaviva7 02/07/2008
	public ActionForward modificaLegamePossessoreInventario(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
	            throws Exception {

		PossessoriLegameInventarioForm  possLegame= (PossessoriLegameInventarioForm) form;

		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		String parametriDOCFIS[] = (String []) request.getSession().getAttribute("parametriDOCFIS");

		String pid="";
		String desc="";
		String codeBib="";
		String descBib="";
		String codeSerie="";
		String codeInv="";

		if (request.getAttribute("PROVLEGPOSINV") ==  null){
			pid=(String)request.getAttribute("bid");
			desc=(String)request.getAttribute("desc");
			codeBib=parametriDOCFIS[0];
			codeSerie=parametriDOCFIS[1];
			codeInv=parametriDOCFIS[2];
			descBib=parametriDOCFIS[3];
			possLegame.setProv("I");
		} else {
			PossessoriDiInventarioVO app = (PossessoriDiInventarioVO)request.getAttribute("PROVLEGPOSINV");
			pid=app.getPid();
			desc=app.getNome();
			codeBib=app.getCodBib();
			descBib=(String)request.getAttribute("descBib");
			possLegame.setNotaAlLegame(app.getNotaLegame());
			possLegame.setProv("M");
			possLegame.setCodeLegame(app.getCodLegame());
		}


		possLegame.setCodeBib(codeBib);
		possLegame.setDescBib(descBib);
		possLegame.setPidOrigine(pid);
		possLegame.setNomeOrigine(desc);
		possLegame.setCodeInv(codeInv);
		possLegame.setCodeSerie(codeSerie);

//		request.setAttribute("modificaLegamePossessoreInventario","Si");

		this.saveToken(request);

//		return mapping.getInputForward();
		return Navigation.getInstance(request).goForward(mapping.findForward("modPossessorePerInventario"));

	} // End modificaLegamePossessoreInventario
*/



}
