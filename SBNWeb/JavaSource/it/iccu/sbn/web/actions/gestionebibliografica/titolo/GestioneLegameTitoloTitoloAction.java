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

package it.iccu.sbn.web.actions.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.GestioneLegameTitoloTitoloForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
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

public class GestioneLegameTitoloTitoloAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(DettaglioTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.canImpronta", "canImpronta");
		map.put("button.insImpronta", "insImpronta");

		map.put("button.annulla", "annullaOper");
		map.put("button.ok", "confermaOper");
		map.put("ricerca.button.aggiornaCanali", "aggiornaMappa");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		GestioneLegameTitoloTitoloForm gestLegTTForm = (GestioneLegameTitoloTitoloForm) form;

		CaricamentoCombo carCombo = new CaricamentoCombo();

		log.info("GestioneLegameTitoloTitoloAction:unspecified");
		gestLegTTForm = (GestioneLegameTitoloTitoloForm) form;

		if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
			gestLegTTForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request
					.getAttribute("AreaDatiLegameTitoloVO"));
		}

		gestLegTTForm.setPresenzaSottoTipoD("NO");
		gestLegTTForm.setPresenzaNumSequenza("NO");

		if (request.getParameter("CREALEGINF") != null) {
			gestLegTTForm.setCreaLegameInferiore("SI");
			gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO().setBid("");
			gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO().setLivAut("51");
			gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO().setTipoRec("a");
			gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO().setNatura("W");
			gestLegTTForm.getAreaDatiVarTitoloVO().setTipoLegame("51");
			gestLegTTForm.getAreaDatiVarTitoloVO().setSequenza("");

			gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO()
					.setTipoMat(gestLegTTForm.getAreaDatiLegameTitoloVO().getTipMatBidPartenza());
			this.caricaComboGenerali(gestLegTTForm);
		} else {
			gestLegTTForm.setCreaLegameInferiore("NO");
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			gestLegTTForm.setListaTipoLegame(carCombo
					.loadComboCodiciDesc(factory.getCodici().getLICR(gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza(),
							gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo())));


//			Inizio L.V. Volevo eliminare il 51 che ha una gestione apposita
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoOperazione() != null) {
				if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Crea")) {
					if (gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M")
							&& gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("M")) {
						List listaTipoLegame = gestLegTTForm.getListaTipoLegame();
						List listaTipoLegameDepurato = new ArrayList();
						ComboCodDescVO codDesc;
						for (int i = 0; i < listaTipoLegame.size(); i++) {
							codDesc = (ComboCodDescVO) listaTipoLegame.get(i);
							if (!codDesc.getCodice().equals(
									gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza()
									+ "51"
									+ gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo())) {
								listaTipoLegameDepurato.add(codDesc);
							}
						}
						gestLegTTForm.setListaTipoLegame(listaTipoLegameDepurato);
					}
				}
			}
//			Fine L.V. Volevo eliminare il 51 che ha una gestione apposita


			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("")) {
				ComboCodDescVO comboCodDescVO = new ComboCodDescVO();
				// Modifica almaviva2 19.07.2010 - Intervento interno - inserito controllo per mancanza della lista che può accadere solo
				// se dopo aver creato la notizia si scopre che il legame fra le due non attivabile (scoperto nei test BUG MANTIS 3733)
				if (gestLegTTForm.getListaTipoLegame().isEmpty()) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage(
							"errors.gestioneBibliografica.testoProtocollo",
							"La notizia è stata creata con l'identificativo "
							+ gestLegTTForm.getAreaDatiLegameTitoloVO().getIdArrivo()
							+ " ma non è possibile legarla alla notizia di partenza per assenza di codici legame validi"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				comboCodDescVO = (ComboCodDescVO) gestLegTTForm.getListaTipoLegame().get(0);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setTipoLegameNew(comboCodDescVO.getCodice());
			}

			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Modifica")) {
				String codiceLegame = gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
										gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew() +
										gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo();

				 String tipoLegameUnimarc = "";
				try {
					tipoLegameUnimarc = (factory.getCodici().cercaDescrizioneCodice(
							codiceLegame,
							CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
							CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (!tipoLegameUnimarc.equals("")) {
					gestLegTTForm.getAreaDatiLegameTitoloVO().setTipoLegameNew(
							gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
							tipoLegameUnimarc +
							gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo());
				}
			}
			gestLegTTForm.setListaSottonatureD(carCombo
					.loadComboCodiciDesc(factory.getCodici().getCodiceLegameTitoloMusica()));

		}

		if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoOperazione().equals("Cancella")) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String codiceLegame = gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
				gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew() +
				gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo();

			 String tipoLegameUnimarc = "";
				try {
					tipoLegameUnimarc = (factory.getCodici().cercaDescrizioneCodice(
							codiceLegame,
							CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
							CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (!tipoLegameUnimarc.equals("")) {
					gestLegTTForm.getAreaDatiLegameTitoloVO().setTipoLegameNew(
							gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
							tipoLegameUnimarc +
							gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo());
				}

//			gestLegTTForm.getAreaDatiLegameTitoloVO().setTipoLegameNew(
//					gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza() +
//					gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew() +
//					gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo());

			return confermaOper(mapping, form, request, response);
		}

		if ((gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M") || gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("N"))
				&& gestLegTTForm.getAreaDatiLegameTitoloVO().getTipMatBidPartenza().equals("U")
				&& gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("D")) {
			gestLegTTForm.setPresenzaSottoTipoD("SI");
		} else if ((gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("M") || gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("N"))
				&& gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidArrivo().equals("D")
				& gestLegTTForm.getAreaDatiLegameTitoloVO().getSottoTipoLegameNew() != null) {
			// BUG esercizio 6616: almaviva2 Giugno 2018 nel caso in cui in fase di allineamento arrivi
			// una notizia con tipo materiale diverso da MUSICA e sottotipoLegame valorizzato questo deve essere pulito
			// per non lasciare un legame con attributo non valido (inserito tutto else if)
			gestLegTTForm.setPresenzaSottoTipoD("SI");
		}

		if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew() != null) {
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().length() > 3 ) {
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
				if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("A01A")
						|| gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("A02A")
						|| gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("A04A")
						|| gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().equals("A10A")) {
					gestLegTTForm.setPresenzaNumSequenza("NO");
				} else {
					if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().substring(1,3).equals("01")
							|| gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().substring(1,3).equals("51")) {
						gestLegTTForm.setPresenzaNumSequenza("SI");
					}
				}
			}
		}

		// Inizio Modifica almaviva2 BUG MANTIS 4079 16.12.2010
		// controllo che non si creino legami fra due bid uguale (legame circolare strettissimo) (GestioneLegameTitoloTitoloAction-unspecified)
		if (gestLegTTForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals(gestLegTTForm.getAreaDatiLegameTitoloVO().getIdArrivo())) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.LegameImpossibile"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		// Fine Modifica almaviva2 BUG MANTIS 4079 16.12.2010


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	private void caricaComboGenerali(GestioneLegameTitoloTitoloForm form)
			throws RemoteException, CreateException, NamingException {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CaricamentoCombo carCombo = new CaricamentoCombo();

		form.setListaNatura(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceNaturaBibliografica()));
		form.setListaTipoMat(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceMaterialeBibliografico()));
		form.setListaLivAut(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceLivelloAutorita()));
		form.setListaTipoRec(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceGenereMaterialeUnimarc()));
		form.setListaLingua1(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceLingua()));
		form.setListaLingua2(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceLingua()));
		form.setListaLingua3(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceLingua()));
		form.setListaPaese(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodicePaese()));
		form.setListaGenere1(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceGenerePubblicazione()));
		form.setListaGenere2(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceGenerePubblicazione()));
		form.setListaGenere3(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceGenerePubblicazione()));
		form.setListaGenere4(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceGenerePubblicazione()));
		form.setListaTipoData(carCombo.loadComboCodiciDesc(factory.getCodici()
				.getCodiceTipoDataPubblicazione()));
		form.setListaTipiNumStandard(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceTipoNumeroStandard()));

		form.setDescNatura(factory.getCodici()
				.cercaDescrizioneCodice(
						form.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO().getNatura(),
						CodiciType.CODICE_NATURA_BIBLIOGRAFICA,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		form.setDescTipoMat(factory.getCodici()
				.cercaDescrizioneCodice(
						form.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO().getTipoMat(),
						CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO,
						CodiciRicercaType.RICERCA_CODICE_UNIMARC));

	}


	public ActionForward insImpronta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloTitoloForm gestLegTTForm = (GestioneLegameTitoloTitoloForm) form;

		gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO()
				.addListaImpronte(new TabellaNumSTDImpronteVO());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward canImpronta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloTitoloForm gestLegTTForm = (GestioneLegameTitoloTitoloForm) form;

		if (gestLegTTForm.getSelezRadioImpronta() == null
				|| gestLegTTForm.getSelezRadioImpronta().equals("")) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage(
					"errors.gestioneBibliografica.selObblImpronta"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		int index = Integer.parseInt(gestLegTTForm
				.getSelezRadioImpronta());
		gestLegTTForm.getAreaDatiVarTitoloVO().getDetTitoloPFissaVO()
				.getListaImpronte().remove(index);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();
	}



	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

 		GestioneLegameTitoloTitoloForm gestLegTTForm = (GestioneLegameTitoloTitoloForm) form;

 		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		// Inizio Modifica almaviva2 BUG MANTIS 4079 16.12.2010
		// controllo che non si creino legami fra due bid uguale (legame circolare strettissimo) (GestioneLegameTitoloTitoloAction-confermaOper)
		if (gestLegTTForm.getAreaDatiLegameTitoloVO().getBidPartenza().equals(gestLegTTForm.getAreaDatiLegameTitoloVO().getIdArrivo())) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.LegameImpossibile"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		// Fine Modifica almaviva2 BUG MANTIS 4079 16.12.2010

		// Inizio Modifica almaviva2 01.12.2010 BUG MANTIS 4023 verifica che la nota al legame non superi gli 80 caratteri
		// (GestioneLegameTitoloTitoloAction - confermaOper)
		if (gestLegTTForm.getAreaDatiLegameTitoloVO().getNoteLegameNew() != null) {
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getNoteLegameNew().length() > 80) {
    			ActionMessages errors = new ActionMessages();
    			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.notaLegameSup80Char"));
    			this.saveErrors(request, errors);
    			return mapping.getInputForward();
			}
		}
		// Fine Modifica almaviva2 01.12.2010 BUG MANTIS 4023


		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;
		if (gestLegTTForm.getCreaLegameInferiore().equals("SI")) {
			try {
				if (!gestLegTTForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()) {
					gestLegTTForm.getAreaDatiVarTitoloVO().setFlagCondiviso(false);
					gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoIndice(false);
					gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoPolo(true);
					gestLegTTForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
					gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
					gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
				} else {
					gestLegTTForm.getAreaDatiVarTitoloVO().setFlagCondiviso(true);
					gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoIndice(true);
					gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoPolo(false);
					gestLegTTForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(true);
					gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(true);
					gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(false);
				}

				gestLegTTForm.getAreaDatiVarTitoloVO().setLegameInf(true);
				gestLegTTForm.getAreaDatiVarTitoloVO().setBidArrivo(gestLegTTForm.getAreaDatiLegameTitoloVO().getBidPartenza());
				areaDatiPassReturn = factory.getGestioneBibliografica()
						.inserisciTitolo(gestLegTTForm.getAreaDatiVarTitoloVO(), Navigation.getInstance(request).getUserTicket());
			} catch (RemoteException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
				this.saveErrors(request, errors);
				return mapping.findForward("annulla");
			}
		} else {
			if (!gestLegTTForm.getAreaDatiLegameTitoloVO().isFlagCondivisoPartenza()
					|| !gestLegTTForm.getAreaDatiLegameTitoloVO().isFlagCondivisoArrivo()) {
				gestLegTTForm.getAreaDatiVarTitoloVO().setFlagCondiviso(false);
				gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoIndice(false);
				gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoPolo(true);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(false);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(false);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(true);
			} else {
				gestLegTTForm.getAreaDatiVarTitoloVO().setFlagCondiviso(true);
				gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoIndice(true);
				gestLegTTForm.getAreaDatiVarTitoloVO().setInserimentoPolo(false);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setFlagCondivisoLegame(true);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoIndice(true);
				gestLegTTForm.getAreaDatiLegameTitoloVO().setInserimentoPolo(false);
			}
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getNaturaBidPartenza().equals("A")) {
				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.collegaElementoAuthority(gestLegTTForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					return mapping.findForward("analiticaTitolo");
				}
			} else {
				try {
					areaDatiPassReturn = factory.getGestioneBibliografica()
							.inserisciLegameTitolo(
									gestLegTTForm.getAreaDatiLegameTitoloVO(), Navigation.getInstance(request).getUserTicket());
				} catch (RemoteException e) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("ERROR >>"
							+ e.getMessage() + e.toString()));
					this.saveErrors(request, errors);
					return mapping.findForward("annulla");
				}
			}
		}

		if (gestLegTTForm.getAreaDatiLegameTitoloVO() == null) {
			request.setAttribute("livRicerca", "I");
		} else {
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().isInserimentoIndice()) {
				request.setAttribute("livRicerca", "I");
			} else {
				request.setAttribute("livRicerca", "P");
			}
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			//request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return mapping.findForward("annulla");
		}
		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getBidRientroAnalitica() != null) {
				request.setAttribute("bid", gestLegTTForm.getAreaDatiLegameTitoloVO().getBidRientroAnalitica());
			} else {
				request.setAttribute("bid", gestLegTTForm.getAreaDatiLegameTitoloVO().getBidPartenza());
			}
			//request.setAttribute("livRicerca", "I");
			request.setAttribute("vaiA", "SI");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.operOk"));
			this.saveErrors(request, errors);
			request.setAttribute("messaggio", "operOk");
			return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);

		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
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


	public ActionForward aggiornaMappa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

 		GestioneLegameTitoloTitoloForm gestLegTTForm = (GestioneLegameTitoloTitoloForm) form;

 		gestLegTTForm.setPresenzaNumSequenza("NO");
		if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew() != null) {
			if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().length() > 3 ) {
				if (gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().substring(1,3).equals("01")
						|| gestLegTTForm.getAreaDatiLegameTitoloVO().getTipoLegameNew().substring(1,3).equals("51")) {
					gestLegTTForm.setPresenzaNumSequenza("SI");
				}
			}
		}
		return mapping.getInputForward();
	}

}
