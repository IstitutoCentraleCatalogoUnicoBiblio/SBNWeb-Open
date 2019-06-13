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


import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriLegameForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriLegameAction extends LookupDispatchAction {

	static Logger log = Logger.getLogger(PossessoriLegameAction.class);

	@Override
	protected Map<String,String> getKeyMethodMap() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("dettaglio.button.salva", "salvaLegame");
		map.put("dettaglio.button.annulla", "annullaLegame");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		PossessoriLegameForm possLegame = (PossessoriLegameForm) form;
		possLegame.setTipoCollegamento("8");


		//INSERIMENTI_RINVIO

		String pid_provenienza = (String)request.getAttribute("bid");
		String desc_provenienza = (String)request.getAttribute("desc");


		possLegame.setDescIniziale(desc_provenienza);
		possLegame.getPossDettVO().setPid(pid_provenienza);
		possLegame.getPossDettVO().setForma("R");


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		caricaComboGenerali(possLegame);
		return mapping.getInputForward();
	}

	public ActionForward salvaLegame(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		PossessoriLegameForm possDett = (PossessoriLegameForm) form;
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
		//livello autorità obbligatorio
		if (possDett.getPossDettVO().getNota().trim().length() > 320) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.documentofisico.ilTestoDellaNotaNonPuoSuperare320Caratteri"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		String ret = null ;
		try {
			//inserimento del possessore corrente
			//chiavi calcolate
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi(possDett.getPossDettVO().getTipoTitolo(),possDett.getPossDettVO().getNome());
			ret = inserisciLegamePossessore (Navigation.getInstance(request),form,key);

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
//		ActionMessages errors = new ActionMessages();
//		errors.add("generico", new ActionMessage("errors.documentofisico.operOk"));
//		this.saveErrors(request, errors);
//		Navigation.getInstance(request).purgeThis();
		request.setAttribute("bid", ret);
		request.setAttribute("livRicerca", "P");

		return Navigation.getInstance(request).goBack(mapping.findForward("salvaLegame"));
	}


	public ActionForward annullaLegame(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		PossessoriLegameForm possLegame = (PossessoriLegameForm) form;
		request.setAttribute("bid", possLegame.getPossDettVO().getPid());
		request.setAttribute("livRicerca", "P");
		return mapping.findForward("annullaLegame");
 	}

	private void caricaComboGenerali(PossessoriLegameForm possDettForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		possDettForm.getPossDettVO().setListalivAutority(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita()));
		possDettForm.getPossDettVO().setListaTipoTitolo(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoAutore()));
	}

	private String inserisciLegamePossessore(Navigation nav, ActionForm form ,GeneraChiave key ) throws Exception {

		PossessoriLegameForm possDett = (PossessoriLegameForm) form;

		String codPolo = nav.getUtente().getCodPolo();
		String uteFirma = nav.getUtente().getFirmaUtente();
		String ticket = nav.getUserTicket();
		String nota_legame = possDett.getNotaAlLegame();
		String tipo_legame = possDett.getTipoCollegamento();


		String ret = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().inserisciLegameAlPossessori(possDett.getPossDettVO(), codPolo, uteFirma, ticket,key,nota_legame,tipo_legame);

		return ret;

	}


}
