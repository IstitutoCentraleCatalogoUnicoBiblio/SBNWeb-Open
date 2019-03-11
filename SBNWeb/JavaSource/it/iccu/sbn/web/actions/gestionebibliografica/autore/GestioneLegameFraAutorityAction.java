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
//	ACTION
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.autore;

import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.autore.GestioneLegameFraAutorityForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
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

public class GestioneLegameFraAutorityAction extends DettaglioAutoreAction {

	private static Log log = LogFactory.getLog(GestioneLegameFraAutorityAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.annulla", "annullaOper");
		map.put("button.ok", "confermaOper");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		GestioneLegameFraAutorityForm gestLegAAForm = (GestioneLegameFraAutorityForm) form;

		gestLegAAForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));

		if (request.getParameter("INSRINVIO") != null) {
			log.debug(request.getParameter("INSRINVIO"));
			gestLegAAForm.setCreaRinvio("SI");
			if (gestLegAAForm.getAreaDatiLegameTitoloVO().getIdArrivo() == null
					|| gestLegAAForm.getAreaDatiLegameTitoloVO().getIdArrivo().equals("")) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setIdArrivo("");
				gestLegAAForm.getAreaDatiLegameTitoloVO().setDescArrivo("");
				if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().toString().equals("AU")) {
					gestLegAAForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("AU");
				}
				if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().toString().equals("LU")) {
					gestLegAAForm.getAreaDatiLegameTitoloVO().setAuthorityOggettoArrivo("LU");
				}
				gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoNomeArrivo("");
				gestLegAAForm.getAreaDatiLegameTitoloVO().setFormaIdArrivo("R");
				gestLegAAForm.getAreaDatiLegameTitoloVO().setNotaInformativaIdArrivo("");
				gestLegAAForm.getAreaDatiLegameTitoloVO().setLivAutIdArrivo("51");
				gestLegAAForm.setDescLivAut("");
			}

			CaricamentoCombo carCombo = new CaricamentoCombo();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			gestLegAAForm.setListaTipoNome(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoAutore()));
			gestLegAAForm.setListaLivAut(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita()));


			gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("");
			gestLegAAForm.getAreaDatiLegameTitoloVO().setSottoTipoLegameNew("");
			gestLegAAForm.getAreaDatiLegameTitoloVO().setNoteLegameNew("");
			gestLegAAForm.getAreaDatiLegameTitoloVO().setSiciNew("");
			gestLegAAForm.getAreaDatiLegameTitoloVO().setSequenzaNew("");
			gestLegAAForm.getAreaDatiLegameTitoloVO().setSequenzaMusicaNew("");

			gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("8");

		} else {
			gestLegAAForm.setCreaRinvio("NO");
			if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("AU")) {
				if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("MA")) {
					gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("921");
				} else {
					if (gestLegAAForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("4XX")) {
						gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("8");
					} else {
						gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("4");
					}
				}
			} else if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("MA")) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("921");
			} else if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("LU")
					&& gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("LU")) {
				if (gestLegAAForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("4XX")) {
					gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("8");
				} else {
					gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("4");
				}
			}
		}


		if (gestLegAAForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Cancella")) {
			return confermaOper(mapping, form, request, response);
		}


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameFraAutorityForm gestLegAAForm = (GestioneLegameFraAutorityForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute("bid", gestLegAAForm.getAreaDatiLegameTitoloVO().getBidPartenza());
		request.setAttribute("livRicerca", "I");

		return
		Navigation.getInstance(request).goBack( mapping.findForward("annulla"), true);

	}
	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameFraAutorityForm gestLegAAForm = (GestioneLegameFraAutorityForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (gestLegAAForm.getCreaRinvio().equals("SI")) {
			if (!gestLegAAForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
			} else {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(true);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(true);
//				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(false);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
			}
		} else {
			if (!gestLegAAForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()
					|| !gestLegAAForm.getAreaDatiLegameTitoloVO().isFlagCondivisoArrivo()) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
			} else {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(true);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(true);
				gestLegAAForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(false);
			}
		}

		// Inizio Modifica almaviva2 01.12.2010 BUG MANTIS 4023 verifica che la nota al legame non superi gli 80 caratteri
		// (GestioneLegameFraAutorityAction - confermaOper)
		if (gestLegAAForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
			if (gestLegAAForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		// Fine Modifica almaviva2 01.12.2010 BUG MANTIS 4023


		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;

		if (gestLegAAForm.getCreaRinvio().equals("SI")) {
			gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("4XX");


			// Inizio modifica almaviva2 25.03.2010 MANTIS 3694 controllo su presenza tipo nome solo per autori e non per luoghi
			if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("AU")) {
				// Inizio modifica almaviva2 25.03.2010 MANTIS 3651 controllo su presenza tipo nome
				if (gestLegAAForm.getAreaDatiLegameTitoloVO().getTipoNomeArrivo() == null ||
						gestLegAAForm.getAreaDatiLegameTitoloVO().getTipoNomeArrivo().equals("")) {
					request.setAttribute("bid", null);
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.ins040"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				// Fine modifica almaviva2 25.03.2010 MANTIS 3651
			}


			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.creaFormaRinvio(gestLegAAForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		} else {
			if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("AU") &&
					gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("MA")) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("921");
			} else if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("AU")) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("5XX");
			} else if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("MA")) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("921");
			}  else if (gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoPartenza().equals("LU")
					&& gestLegAAForm.getAreaDatiLegameTitoloVO().getAuthorityOggettoArrivo().equals("LU")) {
				gestLegAAForm.getAreaDatiLegameTitoloVO().setTipoLegameNew("5XX");
			}

			try {
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.collegaElementoAuthority(gestLegAAForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		}

		if (gestLegAAForm.getAreaDatiLegameTitoloVO().isInserimentoIndice()) {
			request.setAttribute("livRicerca", "I");
		} else {
			request.setAttribute("livRicerca", "P");
		}


		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			//request.setAttribute("bid", gestLegAAForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			//return mapping.findForward("analiticaTitolo");
		}

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

}
