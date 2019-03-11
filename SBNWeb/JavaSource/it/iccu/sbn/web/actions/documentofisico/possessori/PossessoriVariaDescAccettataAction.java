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


import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class PossessoriVariaDescAccettataAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(PossessoriDettaglioAction.class);

    //private InterrogazioneAutoreForm ricAut;
    //private CaricamentoCombo carCombo = new CaricamentoCombo();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dettaglio.button.salva", "salvaPoss");
		map.put("dettaglio.button.annulla", "annullaPoss");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		PossessoriDettaglioForm possDett = (PossessoriDettaglioForm) form;

		TreeElementViewTitoli elementoTree = (TreeElementViewTitoli)request.getAttribute("reticolo");
		possDett.setPossDettVO(elementoTree.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO());

		possDett.setAppoNome(possDett.getPossDettVO().getNome());
		possDett.setAppoNota(possDett.getPossDettVO().getNota());

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

		Navigation navi = Navigation.getInstance(request);
		try {
			//verifica prima esistenza di simili e segnalazione
			GeneraChiave keyNew = new GeneraChiave(possDett.getPossDettVO().getNome().trim(), "");
			keyNew.calcoloClesToSearch(possDett.getPossDettVO().getNome().trim());
			//
			GeneraChiave keyOld = new GeneraChiave(possDett.getAppoNome().trim(), "");
			keyOld.calcoloClesToSearch(possDett.getAppoNome().trim());
			//
			PossessoriRicercaForm possRic = new PossessoriRicercaForm();
			PossessoriRicercaVO ricerca = possRic.getRicercaPoss();
			ricerca.setSoloBib(false);
			ricerca.setChkTipoNomeA(false);
			ricerca.setChkTipoNomeB(false);
			ricerca.setChkTipoNomeC(false);
			ricerca.setChkTipoNomeD(false);
			ricerca.setChkTipoNomeE(false);
			ricerca.setChkTipoNomeR(false);
			ricerca.setChkTipoNomeG(false);
			if (possDett.getPossDettVO().getTipoTitolo() != null){
				if (possDett.getPossDettVO().getTipoTitolo().equals("A")){
					ricerca.setChkTipoNomeA(true);
				}else if (possDett.getPossDettVO().getTipoTitolo().equals("B")){
					ricerca.setChkTipoNomeB(true);
				}else if (possDett.getPossDettVO().getTipoTitolo().equals("C")){
					ricerca.setChkTipoNomeC(true);
				}else if (possDett.getPossDettVO().getTipoTitolo().equals("D")){
					ricerca.setChkTipoNomeD(true);
				}else if (possDett.getPossDettVO().getTipoTitolo().equals("E")){
					ricerca.setChkTipoNomeE(true);
				}else if (possDett.getPossDettVO().getTipoTitolo().equals("R")){
					ricerca.setChkTipoNomeR(true);
				}else if (possDett.getPossDettVO().getTipoTitolo().equals("G")){
					ricerca.setChkTipoNomeG(true);
				}
			}
			ricerca.setNote("");
			ricerca.setFormaAutore("A");
			ricerca.setTipoOrdinamSelez("");
			ricerca.setTipoRicerca("intero");
			PossessoriSinteticaForm possSint = new PossessoriSinteticaForm();
			List<?> listaPoss = null;
			if (keyNew.equals(keyOld)){
				ricerca.setPid(possDett.getPossDettVO().getPid());
				ricerca.setNome(possDett.getAppoNome());
				listaPoss = this.getListaPossessori_1(navi.getUtente().getCodPolo(),
						navi.getUtente().getCodBib(), possRic, navi.getUserTicket(),
						ricerca.getNRec() , keyOld, possSint);
			}else{
				ricerca.setPid("");
				ricerca.setNome(possDett.getPossDettVO().getNome());
				listaPoss = this.getListaPossessori(navi.getUtente().getCodPolo(),
						navi.getUtente().getCodBib(), possRic, navi.getUserTicket(),
						ricerca.getNRec() , keyNew, possSint);
				//inserimento del possessore corrente
				//chiavi calcolate
				if (listaPoss.size() > 0){
					possSint.setProv("SIMILI");
					request.setAttribute("listaPossessoriSimile", listaPoss);
					//esistono gia simili per cui impedisco inserimento su questa base
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.documentofisico.forzaturaPossessoreNoDisp"));
					this.saveErrors(request, errors);

					// almaviva2 Aprile 2015: nuove funzionalità per gestire la fusione fra Possessori in uscita da una richiesta di
					// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)
					possSint.setIdOggColl(possDett.getPossDettVO().getPid());
					possSint.setDescOggColl(possDett.getPossDettVO().getNome());
					possSint.setProspettaDatiOggColl("SI");

					request.getSession().setAttribute("possessoriSinteticaForm", possSint);
					return mapping.findForward("sinteticaPossessore");
				}
			}
		}catch (Exception e) {
			// altri tipi di errore
			log.error("", e);
		}

		try {
			//inserimento del possessore corrente
			//chiavi calcolate
			GeneraChiave key = new GeneraChiave();
			key.estraiChiavi(possDett.getPossDettVO().getTipoTitolo(),possDett.getPossDettVO().getNome());
			inserisciPossessori(navi,form,key);

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
		navi.purgeThis();

		// qui impostare i dati per la chiamata all'analitica.Per ora fittizi
		request.setAttribute("bid", possDett.getPossDettVO().getPid());
		request.setAttribute("tipoAuthority", "PP");
		request.setAttribute("livRicerca", "P");
//		return Navigation.getInstance(request).goBack();//almaviva2 18/11/2009 bug
		return mapping.findForward("analitica");//almaviva2 18/11/2009
	}



	public ActionForward annullaPoss(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		return Navigation.getInstance(request).goBack(true);

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
		ret = factory.getGestioneDocumentoFisico().modificaPossessori(possDett.getPossDettVO(), codPolo, uteFirma, ticket,key);
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

	private List<?> getListaPossessori_1(String codPolo, String codBib, ActionForm form ,String ticket, int nRec ,
			GeneraChiave key,PossessoriSinteticaForm possSint	) throws Exception {
		PossessoriRicercaForm possRic = (PossessoriRicercaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaPossessori_1(codPolo, codBib, possRic.getRicercaPoss() ,ticket , nRec,key);
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
