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


import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.web.actionforms.documentofisico.possessori.PossessoriLegameForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.util.CaricamentoCombo;
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

public class PossessoriVariaDescRinvioAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(PossessoriLegameAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("dettaglio.button.salva", "salva");
		map.put("dettaglio.button.annulla", "annulla");
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
		String provenienza = (String)request.getAttribute("PROVENIENZA");
		String pid_provenienza = (String)request.getAttribute("bid");
		String desc_provenienza = (String)request.getAttribute("desc");

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli)request.getAttribute("reticolo");
		possLegame.setPidOrigine(elementoTree.getParent().getKey());

		possLegame.setNotaAlLegame(elementoTree.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().getNotaAlLegame());
		possLegame.setDescIniziale(desc_provenienza);


		possLegame.setPossDettVO(elementoTree.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO());


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		caricaComboGenerali(possLegame);
		return mapping.getInputForward();
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form,
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
		String ret = null ;
		try {
			//inserimento del possessore corrente
			//chiavi calcolate
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi(possDett.getPossDettVO().getTipoTitolo(),possDett.getPossDettVO().getNome());
			ret = modificaLegamePossessore (Navigation.getInstance(request),form,key,possDett.getPidOrigine());

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

		request.setAttribute("bid", possDett.getPidOrigine());
		request.setAttribute("tipoAuthority", "PP");
		request.setAttribute("livRicerca", "P");
		return Navigation.getInstance(request).goBack();
	}


	public ActionForward annulla(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		PossessoriLegameForm possLegame = (PossessoriLegameForm) form;
	return  Navigation.getInstance(request).goBack(true);
 	}

	private void caricaComboGenerali(PossessoriLegameForm possDettForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		possDettForm.getPossDettVO().setListalivAutority(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita()));
		possDettForm.getPossDettVO().setListaTipoTitolo(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoAutore()));
	}

	private String modificaLegamePossessore(Navigation nav, ActionForm form ,GeneraChiave key,String pidOrigine ) throws Exception {

		PossessoriLegameForm possDett = (PossessoriLegameForm) form;

		String codPolo = nav.getUtente().getCodPolo();
		String uteFirma = nav.getUtente().getFirmaUtente();
		String ticket = nav.getUserTicket();
		String nota_legame = possDett.getNotaAlLegame();
		String tipo_legame = possDett.getTipoCollegamento();
		String ret = null;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().modificaLegameAlPossessori(possDett.getPossDettVO(), codPolo, uteFirma, ticket,key,nota_legame,tipo_legame, pidOrigine);

		return ret;
	}
}
