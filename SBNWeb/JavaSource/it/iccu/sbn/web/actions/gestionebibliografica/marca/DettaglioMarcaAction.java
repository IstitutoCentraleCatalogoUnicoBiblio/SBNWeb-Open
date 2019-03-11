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

package it.iccu.sbn.web.actions.gestionebibliografica.marca;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SifRicercaRepertoriVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiVariazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.web.actionforms.gestionebibliografica.marca.DettaglioMarcaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.MarcaImageCache;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.imageio.ImageIO;
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
import org.apache.struts.upload.FormFile;

public class DettaglioMarcaAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(DettaglioMarcaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.annulla", "annullaOper");
		map.put("button.ok", "confermaOper");
		map.put("button.caricaImmagine", "caricaImmagine");
		map.put("button.cancelImmagine", "cancellaImmagine");
		return map;
	}

	private void loadDefault(HttpServletRequest request,DettaglioMarcaForm dettaglioMarcaForm) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			dettaglioMarcaForm.getDettMarcaVO().setLivAut((String)utenteEjb.getDefault(ConstantDefault.CREA_MAR_LIVELLO_AUTORITA));
		} catch (DefaultNotFoundException e) {}

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		log.info("DettaglioMarcaAction::unspecified");
		DettaglioMarcaForm dettaglioMarcaForm = (DettaglioMarcaForm) form;

		// gestione upload immagine marca
		FormFile immagine = dettaglioMarcaForm.getUploadImmagine();
		if (immagine != null && immagine.getFileSize() > 0)
			return gestioneUploadImmagine(mapping, dettaglioMarcaForm, request);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (request.getAttribute("bidPerRientroAnalitica") != null) {
			dettaglioMarcaForm.setBidPerRientroAnalitica((String) request.getAttribute("bidPerRientroAnalitica"));
		}
		dettaglioMarcaForm.setDettMarcaVO((DettaglioMarcaGeneraleVO) request.getAttribute("dettaglioMar"));
		dettaglioMarcaForm.setTipoProspettazione((String) request.getAttribute("tipoProspettazione"));

		dettaglioMarcaForm.setCampoCodiceRep1Mod(dettaglioMarcaForm.getDettMarcaVO().getCampoCodiceRep1Old());
		dettaglioMarcaForm.setCampoCodiceRep2Mod(dettaglioMarcaForm.getDettMarcaVO().getCampoCodiceRep2Old());
		dettaglioMarcaForm.setCampoCodiceRep3Mod(dettaglioMarcaForm.getDettMarcaVO().getCampoCodiceRep3Old());
		dettaglioMarcaForm.setCampoProgressivoRep1Mod(dettaglioMarcaForm.getDettMarcaVO().getCampoProgressivoRep1Old());
		dettaglioMarcaForm.setCampoProgressivoRep2Mod(dettaglioMarcaForm.getDettMarcaVO().getCampoProgressivoRep2Old());
		dettaglioMarcaForm.setCampoProgressivoRep3Mod(dettaglioMarcaForm.getDettMarcaVO().getCampoProgressivoRep3Old());

		Navigation navi = Navigation.getInstance(request);
		if (dettaglioMarcaForm.getTipoProspettazione().equals("INS")) {
			dettaglioMarcaForm.getDettMarcaVO().setMid("");
			this.loadDefault(request, dettaglioMarcaForm);
			if (request.getParameter("CONDIVISO") != null) {
				if (request.getParameter("CONDIVISO").equals("TRUE")) {
					dettaglioMarcaForm.setFlagCondiviso(true);
				} else {
					dettaglioMarcaForm.setFlagCondiviso(false);
				}
			}
			navi.setTesto("Crea");
		}

		if (dettaglioMarcaForm.getTipoProspettazione().equals("AGG")) {
			if (request.getAttribute("flagCondiviso") != null) {
				if ((Boolean) request.getAttribute("flagCondiviso")) {
					dettaglioMarcaForm.setFlagCondiviso(true);
				} else {
					dettaglioMarcaForm.setFlagCondiviso(false);
				}
			}
			navi.setTesto("Varia");
		}

		if (dettaglioMarcaForm.getTipoProspettazione().equals("DET")) {
			this.caricaDescrizioni(dettaglioMarcaForm);
		} else if (dettaglioMarcaForm.getTipoProspettazione().equals("INS")
				|| dettaglioMarcaForm.getTipoProspettazione().equals("AGG")) {
			this.caricaComboGenerali(dettaglioMarcaForm);
		}

		if (request.getAttribute("AreaDatiLegameTitoloVO") != null) {
			dettaglioMarcaForm.setAreaDatiLegameTitoloVO((AreaDatiLegameTitoloVO) request.getAttribute("AreaDatiLegameTitoloVO"));
		} else {
			dettaglioMarcaForm.setAreaDatiLegameTitoloVO(null);
		}

		List<byte[]> listaImmagini = dettaglioMarcaForm.getDettMarcaVO().getListaImmagini();
		if (listaImmagini.size() > 0) {
			String mid = dettaglioMarcaForm.getDettMarcaVO().getMid();
			navi.getMarcaImageCache().clearImages(mid);
			navi.getMarcaImageCache().addImages(mid, listaImmagini);

		}
		return mapping.getInputForward();

	}

	private ActionForward gestioneUploadImmagine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request) {

		DettaglioMarcaForm dettaglioMarcaForm = (DettaglioMarcaForm) form;

		// provo a caricare l'immagine
		byte[] imgbuf;
		try {
			imgbuf = dettaglioMarcaForm.getUploadImmagine().getFileData();

			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgbuf));
			if (image == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.marImgNotValid"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			// immagine valida
			List<byte[]> listaImmagini = dettaglioMarcaForm.getDettMarcaVO().getListaImmagini();
			listaImmagini.add(imgbuf);
			String mid = dettaglioMarcaForm.getDettMarcaVO().getMid();
			MarcaImageCache imageCache = Navigation.getInstance(request).getMarcaImageCache();
			imageCache.clearImages(mid);
			imageCache.addImages(mid, listaImmagini);
			dettaglioMarcaForm.setUploadImmagine(null);

			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.ricConfermaImgMarca"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.marImgNotValid"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward annullaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMarcaForm dettaglioMarcaForm = (DettaglioMarcaForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute("bid", dettaglioMarcaForm.getDettMarcaVO()
				.getMid());
		request.setAttribute("livRicerca", "I");
		// Modiofica almaviva2 25.01.2010 BUG MANTIS 3513
		return Navigation.getInstance(request).goBack();
//		return Navigation.getInstance(request).goBack(mapping.findForward("annulla"), true);

	}

	private void caricaDescrizioni(DettaglioMarcaForm dettaglioMarcaForm)
			throws RemoteException, CreateException, NamingException {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioMarcaForm.getDettMarcaVO().getLivAut() != null) {
			dettaglioMarcaForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioMarcaForm.getDettMarcaVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioMarcaForm.setDescLivAut("");
		}

		List<SifRicercaRepertoriVO> listaRepertori = factory.getGestioneRepertorio().getAllRepertori();

		if (dettaglioMarcaForm.getCampoCodiceRep1Mod() != null) {
			for (int i = 0; i < listaRepertori.size(); i++) {
				SifRicercaRepertoriVO sifRicRep = listaRepertori
						.get(i);
				if (sifRicRep.getSigl().equals(
						dettaglioMarcaForm.getCampoCodiceRep1Mod())) {
					dettaglioMarcaForm.setDescRepertori1(sifRicRep.getDesc());
					break;
				}
			}
		} else {
			dettaglioMarcaForm.setDescRepertori1("");
		}

		if (dettaglioMarcaForm.getCampoCodiceRep2Mod() != null) {
			for (int i = 0; i < listaRepertori.size(); i++) {
				SifRicercaRepertoriVO sifRicRep = listaRepertori
						.get(i);
				if (sifRicRep.getSigl().equals(
						dettaglioMarcaForm.getCampoCodiceRep2Mod())) {
					dettaglioMarcaForm.setDescRepertori2(sifRicRep.getDesc());
					break;
				}
			}
		} else {
			dettaglioMarcaForm.setDescRepertori2("");
		}
		if (dettaglioMarcaForm.getCampoCodiceRep3Mod() != null) {
			for (int i = 0; i < listaRepertori.size(); i++) {
				SifRicercaRepertoriVO sifRicRep = listaRepertori
						.get(i);
				if (sifRicRep.getSigl().equals(
						dettaglioMarcaForm.getCampoCodiceRep3Mod())) {
					dettaglioMarcaForm.setDescRepertori3(sifRicRep.getDesc());
					break;
				}
			}
		} else {
			dettaglioMarcaForm.setDescRepertori3("");
		}

	}

	private void caricaComboGenerali(DettaglioMarcaForm dettaglioMarcaForm)
			throws RemoteException, CreateException, NamingException {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (dettaglioMarcaForm.getDettMarcaVO().getLivAut() != null) {
			dettaglioMarcaForm.setDescLivAut(factory.getCodici()
					.cercaDescrizioneCodice(
							dettaglioMarcaForm.getDettMarcaVO().getLivAut(),
							CodiciType.CODICE_LIVELLO_AUTORITA,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC));
		} else {
			dettaglioMarcaForm.setDescLivAut("");
		}

		CaricamentoCombo carCombo = new CaricamentoCombo();
		dettaglioMarcaForm.setListaLivAut(carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodiceLivelloAutorita()));

		List<?> listaRepertori = factory.getGestioneRepertorio()
				.getAllRepertori();
		List<ComboCodDescVO> lista = new ArrayList<ComboCodDescVO>();
		ComboCodDescVO comboCodDescVO = new ComboCodDescVO();
		comboCodDescVO.setCodice("");
		comboCodDescVO.setDescrizione("");
		lista.add(comboCodDescVO);

		for (int i = 0; i < listaRepertori.size(); i++) {
			SifRicercaRepertoriVO sifRicRep = (SifRicercaRepertoriVO) listaRepertori
					.get(i);
			if (sifRicRep.getTipo().equals("M")) {
				comboCodDescVO = new ComboCodDescVO();
				comboCodDescVO.setCodice(sifRicRep.getSigl());
				comboCodDescVO.setDescrizione(sifRicRep.getDesc());
				lista.add(comboCodDescVO);
			}
		}
		dettaglioMarcaForm.setListaRepertori(lista);
	}

	public ActionForward confermaOper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMarcaForm dettaglioMarcaForm = (DettaglioMarcaForm) form;


		// Inizio intervento almaviva2 BUG MANTIS esercizio 4678
		// controllo che il campo MOTTO della marca non superi gli 80 caratteri
		if (dettaglioMarcaForm.getDettMarcaVO().getMotto() != null) {
			if (dettaglioMarcaForm.getDettMarcaVO().getMotto().length() > 80) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.marcaConMottoTroppoLungo"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		// Fine intervento almaviva2 BUG MANTIS esercizio 4678

		// gestione upload immagine marca
		FormFile immagine = dettaglioMarcaForm.getUploadImmagine();
		if (immagine != null && immagine.getFileSize() > 0)
			return gestioneUploadImmagine(mapping, dettaglioMarcaForm, request);

		AreaDatiVariazioneReturnVO areaDatiPassReturn = null;
		AreaDatiVariazioneMarcaVO areaDatiPass = new AreaDatiVariazioneMarcaVO();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		areaDatiPass.setDettMarcaVO(dettaglioMarcaForm.getDettMarcaVO());

		areaDatiPass.getDettMarcaVO().setCampoCodiceRep1(
				dettaglioMarcaForm.getCampoCodiceRep1Mod());
		areaDatiPass.getDettMarcaVO().setCampoCodiceRep2(
				dettaglioMarcaForm.getCampoCodiceRep2Mod());
		areaDatiPass.getDettMarcaVO().setCampoCodiceRep3(
				dettaglioMarcaForm.getCampoCodiceRep3Mod());
		areaDatiPass.getDettMarcaVO().setCampoProgressivoRep1(
				dettaglioMarcaForm.getCampoProgressivoRep1Mod());
		areaDatiPass.getDettMarcaVO().setCampoProgressivoRep2(
				dettaglioMarcaForm.getCampoProgressivoRep2Mod());
		areaDatiPass.getDettMarcaVO().setCampoProgressivoRep3(
				dettaglioMarcaForm.getCampoProgressivoRep3Mod());

		if (dettaglioMarcaForm.getDettMarcaVO().getMid().equals("")) {
			areaDatiPass.setModifica(false);
		} else {
			areaDatiPass.setModifica(true);
		}

		// salvataggio delle aree prospettate in dettglio per verificare se sono
		// state effettuate modifiche;

		areaDatiPass.setVariazione(true);

		areaDatiPass.setInserimentoIndice(true);

		areaDatiPass.setFlagCondiviso(dettaglioMarcaForm.isFlagCondiviso());
		if (dettaglioMarcaForm.isFlagCondiviso()) {
			areaDatiPass.setInserimentoIndice(true);
			areaDatiPass.setInserimentoPolo(false);
		} else {
			areaDatiPass.setInserimentoIndice(false);
			areaDatiPass.setInserimentoPolo(true);
		}

		// Inizio Evolutive Google3 30.01.2014
		// Intervento finalizzato a consentire la creazione di un profilo che consenta solo la cattura e
		// la creazione/modifica di oggeti solo su locale
		it.iccu.sbn.ejb.remote.Utente utenteEjb =(it.iccu.sbn.ejb.remote.Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try{
			if (dettaglioMarcaForm.isFlagCondiviso()) {
				if (utenteEjb.isAuthoritySoloLocale(SbnAuthority.MA)) {
					throw new UtenteNotAuthorizedException();
				}
			}
			}catch(UtenteNotAuthorizedException ute)
			{
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.autNotAuthorized"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		// Fine Evolutive Google3 30.01.2014




		try {
			areaDatiPassReturn = factory.getGestioneBibliografica().inserisciMarca(areaDatiPass, Navigation.getInstance(request).getUserTicket());
		} catch (RemoteException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("ERROR >>" + e.getMessage()));
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(mapping.findForward("annulla"), true);
		}

		if (areaDatiPassReturn == null) {
			request.setAttribute("bid", null);
			request.setAttribute("livRicerca", "I");
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(
					mapping.findForward("annulla"), true);
		}
		if (areaDatiPassReturn.getCodErr().equals("0000")) {
			if (dettaglioMarcaForm.getAreaDatiLegameTitoloVO() == null) {

				if (dettaglioMarcaForm.getBidPerRientroAnalitica() != null) {
					request.setAttribute("bid", dettaglioMarcaForm.getBidPerRientroAnalitica());
				} else {
					request.setAttribute("bid", areaDatiPassReturn.getBid());
				}

				if (dettaglioMarcaForm.isFlagCondiviso()) {
					request.setAttribute("livRicerca", "I");
				} else {
					request.setAttribute("livRicerca", "P");
				}
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.operOk"));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(mapping.findForward("analitica"), true);
			} else {
				dettaglioMarcaForm.getAreaDatiLegameTitoloVO().setIdArrivo(
						areaDatiPassReturn.getBid());
				dettaglioMarcaForm.getAreaDatiLegameTitoloVO()
						.setAuthorityOggettoArrivo("MA");
				dettaglioMarcaForm.getAreaDatiLegameTitoloVO().setDescArrivo(
						areaDatiPass.getDettMarcaVO().getDesc());
				dettaglioMarcaForm.getAreaDatiLegameTitoloVO()
						.setFlagCondivisoArrivo(true);
				request.setAttribute("AreaDatiLegameTitoloVO",
						dettaglioMarcaForm.getAreaDatiLegameTitoloVO());
				if (dettaglioMarcaForm.getAreaDatiLegameTitoloVO()
						.getAuthorityOggettoPartenza() == null) {
					return mapping.findForward("gestioneLegameTitoloMarca");
				}
				if (dettaglioMarcaForm.getAreaDatiLegameTitoloVO()
						.getAuthorityOggettoPartenza().equals("AU")
						|| dettaglioMarcaForm.getAreaDatiLegameTitoloVO()
								.getAuthorityOggettoPartenza().equals("MA")) {
					return mapping.findForward("gestioneLegameFraAutority");
				}
			}
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

	public ActionForward caricaImmagine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMarcaForm dettaglioMarcaForm = (DettaglioMarcaForm) form;

		// gestione upload immagine marca
		FormFile immagine = dettaglioMarcaForm.getUploadImmagine();
		if (immagine != null && immagine.getFileSize() > 0)
			return gestioneUploadImmagine(mapping, dettaglioMarcaForm, request);
		return mapping.getInputForward();
	}

	public ActionForward cancellaImmagine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioMarcaForm dettaglioMarcaForm = (DettaglioMarcaForm) form;

		Navigation navi = Navigation.getInstance(request);

		if (dettaglioMarcaForm.getSelezCheck() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selObblImageCanc"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (dettaglioMarcaForm.getSelezCheck().length == 0 ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selObblImageCanc"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (dettaglioMarcaForm.getSelezCheck().length > 1 ) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.selObblSoloUnaImageCanc"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

//		if (dettaglioMarcaForm.getDettMarcaVO().getListaImmagini().size() == 1) {
//			ActionMessages errors = new ActionMessages();
//			errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.noCancImmagUnica"));
//			this.saveErrors(request, errors);
//			return mapping.getInputForward();
//
//		}

		String[] listaVidSelez = dettaglioMarcaForm.getSelezCheck();
		dettaglioMarcaForm.getDettMarcaVO().getListaImmagini().remove(Integer.valueOf(listaVidSelez[0]).intValue());


		List<byte[]> listaImmagini = dettaglioMarcaForm.getDettMarcaVO().getListaImmagini();
		String mid = dettaglioMarcaForm.getDettMarcaVO().getMid();
		if (listaImmagini.size() > 0) {
			navi.getMarcaImageCache().clearImages(mid);
			navi.getMarcaImageCache().addImages(mid, listaImmagini);
		} else {
			navi.getMarcaImageCache().clearImages(mid);
		}

		return mapping.getInputForward();
	}

}
