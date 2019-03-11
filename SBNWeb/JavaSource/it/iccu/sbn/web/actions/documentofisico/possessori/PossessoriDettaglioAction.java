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
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriDettaglioForm;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriRicercaForm;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriSinteticaForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriDettaglioAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(PossessoriDettaglioAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String,String> getKeyMethodMap() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("dettaglio.button.salva", "salvaPoss");
		map.put("dettaglio.button.annulla", "annullaPoss");


		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PossessoriDettaglioForm possDett = (PossessoriDettaglioForm) form;
		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		possDett.setPossDettVO((PossessoriDettaglioVO) request.getAttribute("dettaglioPoss"));



		HttpSession httpSession = request.getSession();

//		String parametri [] = new String [5];
//		if ((String)request.getAttribute("prov")!= null && !((String)request.getAttribute("prov")).trim().equals("")){
//			//sono stato chiamato dal documento fisico e quindi prendo i parametri inviati
//			parametri [0] = (String)request.getAttribute("codBib");
//			parametri [1] = (String)request.getAttribute("codSerie");
//			parametri [2] = ((String)request.getAttribute("codInvent")).toString();
//			parametri [3] = (String)request.getAttribute("descrBib");
//			parametri [4] = (String)request.getAttribute("prov");
//		}
//		request.setAttribute("parametriDOCFIS", parametri);
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		caricaComboGenerali(possDett);
		return mapping.getInputForward();
	}

	public ActionForward salvaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		PossessoriDettaglioForm possDett = (PossessoriDettaglioForm) form;
		//nome obbligatorio
		if (possDett.getPossDettVO().getNome().trim().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.nome"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		//tipo nome obbligatorio
		if (possDett.getPossDettVO().getLivello().trim().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.livautorita"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		//livello autorità obbligatorio
		if (possDett.getPossDettVO().getTipoTitolo().trim().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.tiponome"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		String ret = null ;
		try {
//			verifica prima esistenza di simili e segnalazione

			GeneraChiave keyDuoble = new GeneraChiave(possDett.getPossDettVO().getNome().trim(),"");
			keyDuoble.calcoloClesToSearch(possDett.getPossDettVO().getNome().trim());
			PossessoriRicercaForm possRic = new PossessoriRicercaForm();
			possRic.getRicercaPoss().setNome(possDett.getPossDettVO().getNome());
			possRic.getRicercaPoss().setPid("");
			possRic.getRicercaPoss().setSoloBib(false);
			possRic.getRicercaPoss().setChkTipoNomeA(false);
			possRic.getRicercaPoss().setChkTipoNomeB(false);
			possRic.getRicercaPoss().setChkTipoNomeC(false);
			possRic.getRicercaPoss().setChkTipoNomeD(false);
			possRic.getRicercaPoss().setChkTipoNomeE(false);
			possRic.getRicercaPoss().setChkTipoNomeR(false);
			possRic.getRicercaPoss().setChkTipoNomeG(false);
			possRic.getRicercaPoss().setNote("");
			possRic.getRicercaPoss().setFormaAutore("");
			possRic.getRicercaPoss().setTipoOrdinamSelez("");
			possRic.getRicercaPoss().setTipoRicerca("intero");
			PossessoriSinteticaForm possSint = new PossessoriSinteticaForm();
			try {
				List listaPoss = this.getListaPossessori(Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(), possRic, Navigation.getInstance(request).getUserTicket(),
						possRic.getRicercaPoss().getNRec() , keyDuoble, possSint);
				//inserimento del possessore corrente
				//chiavi calcolate
				if (listaPoss.size()>0){
					possSint.setProv("SIMILI");
					//esistono gia simili per cui impedisco inserimento su questa base
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.documentofisico.forzaturaPossessoreNoDisp"));
					this.saveErrors(request, errors);
					request.getSession().setAttribute("possessoriSinteticaForm", possSint);
					return mapping.findForward("sinteticaPossessore");
					//				return mapping.getInputForward();
				}
			} catch (DataException e) {

			} catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				if (possDett.getPossDettVO().getTipoTitolo() != null &&  possDett.getPossDettVO().getTipoTitolo().equals("D")){
					if (e.getMessage().startsWith("String index out of range:")){
						errors.add("generico", new ActionMessage("errors.documentofisico.tiponome"));
						this.saveErrors(request, errors);
					    return mapping.getInputForward();
					}
				}
				// correggere il file di puntamento dell'errore
				errors.add("generico", new ActionMessage(e.getMessage()));
				this.saveErrors(request, errors);
			    return mapping.getInputForward();
			}



		} catch (Exception e) {

		}

		try {
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi(possDett.getPossDettVO().getTipoTitolo(),possDett.getPossDettVO().getNome().trim());
			ret = inserisciPossessori (Navigation.getInstance(request),form,key);

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			if (possDett.getPossDettVO().getTipoTitolo() != null &&  possDett.getPossDettVO().getTipoTitolo().equals("D")){
				if (e.getMessage().startsWith("String index out of range:")){
					errors.add("generico", new ActionMessage("errors.documentofisico.tiponome"));
					this.saveErrors(request, errors);
				    return mapping.getInputForward();
				}
			}
			// correggere il file di puntamento dell'errore
			errors.add("generico", new ActionMessage(e.getMessage()));
			this.saveErrors(request, errors);
		    return mapping.getInputForward();
		}
		ActionMessages errors = new ActionMessages();
		errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		Navigation.getInstance(request).purgeThis();

		// qui impostare i dati per la chiamata all'analitica.Per ora fittizi
		request.setAttribute("bid", ret.substring(0, 10));
		request.setAttribute("desc", possDett.getPossDettVO().getNome());
		//request.setAttribute("listaPossessoriBySint", new ArrayList());
		request.setAttribute("tipoAuthority", "PP");
		request.setAttribute("livRicerca", "P");
//		String parametriDOCFIS [] = (String [])request.getAttribute("parametriDOCFIS");
//		request.setAttribute("parametriDOCFIS", parametriDOCFIS);
		if (request.getSession().getAttribute("parametriDOCFIS")!=null){

			return Navigation.getInstance(request).goForward(mapping.findForward("attivaPossessorePerInventario"));
		}

		return Navigation.getInstance(request).goForward(mapping.findForward("analitica"));
	}


	public ActionForward annullaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//		String parametriDOCFIS [] = (String [])request.getAttribute("parametriDOCFIS");
//		request.setAttribute("parametriDOCFIS", parametriDOCFIS);

		return mapping.findForward("annulla");

	}




	private void caricaComboGenerali(PossessoriDettaglioForm possDettForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		possDettForm.getPossDettVO().setListalivAutority(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita()));
		possDettForm.getPossDettVO().setListaTipoTitolo(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoAutore()));
	}

	private String inserisciPossessori(Navigation nav, ActionForm form ,GeneraChiave key ) throws Exception {
		PossessoriDettaglioForm possDett = (PossessoriDettaglioForm) form;
		String codPolo = nav.getUtente().getCodPolo();
		String uteFirma = nav.getUtente().getFirmaUtente();
		String ticket = nav.getUserTicket();
		String ret = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().inserisciPossessori(possDett.getPossDettVO(), codPolo, uteFirma, ticket,key);
		return ret;

	}
	private List<?> getListaPossessori(String codPolo, String codBib, ActionForm form ,String ticket, int nRec ,
			GeneraChiave key,PossessoriSinteticaForm possSint	) throws Exception {
		PossessoriRicercaForm possRic = (PossessoriRicercaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaPossessori(codPolo, codBib, possRic.getRicercaPoss() ,ticket , nRec,key);
		possSint.setIdLista(blocco1.getIdLista());
		possSint.setTotBlocchi(blocco1.getTotBlocchi());
		possSint.setTotRighe(blocco1.getTotRighe());
		possSint.setBloccoSelezionato(blocco1.getNumBlocco());
		possSint.setElemPerBlocchi(blocco1.getMaxRighe());
		if ((blocco1.getTotBlocchi() > 1)){
			possSint.setAbilitaBottoneCarBlocchi(false);
		}else{
			possSint.setAbilitaBottoneCarBlocchi(true);
		}
		possSint.setListaPossessori(blocco1.getLista());

		return blocco1.getLista();
	}

}
