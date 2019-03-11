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
package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.validators.acquisizioni.StrutturaInventariOrdValidator;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.EsaminaOrdineRForm;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.EsaminaOrdineRForm.TipoOperazione;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OrdineBaseForm;
import it.iccu.sbn.web.actions.acquisizioni.util.OrdiniWebUtil;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ch.lambdaj.Lambda;

	public class EsaminaOrdineRAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsaminaOrdineAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro", "indietro");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("ricerca.button.salva", "salva");
		map.put("acquisizioni.bottone.si", "si");
		map.put("acquisizioni.bottone.no", "no");
		map.put("ricerca.button.insRiga", "inserisciRiga");
		map.put("ricerca.button.cancRiga", "cancellaRiga");
		map.put("ricerca.button.completa", "completa");
		map.put("ricerca.label.rilegaturaData", "assegnaData");

		// almaviva5_20121107 evolutive google
		map.put("servizi.bottone.frecciaSu", "moveUp");
		map.put("servizi.bottone.frecciaGiu", "moveDown");
		map.put("ricerca.button.aggRiga", "aggiungiRiga");
		map.put("button.selAllTitoli", "tutti");
		map.put("button.deSelAllTitoli", "nessuno");

		map.put("ordine.button.lockVolume", "volume");
		map.put("ordine.button.unlockVolume", "volume");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

    	try {
			List<StrutturaInventariOrdVO> listaInv = new ArrayList<StrutturaInventariOrdVO>();

			HttpSession session = request.getSession();
			if (session.getAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R)!=null )
			{
				OrdiniVO datiOrdine = (OrdiniVO)session.getAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R);
				currentForm.setDatiOrdine(datiOrdine);
				session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R);
			}

    		if (session.getAttribute("elencoINVANDATA") != null) {
				// carica
				currentForm.setElencaInventari((List<StrutturaInventariOrdVO>) session.getAttribute("elencoINVANDATA"));
				listaInv = currentForm.getElencaInventari();
				session.removeAttribute("elencoINVANDATA");

				currentForm.setListaInventariOld(ClonePool.deepCopy(listaInv));
				// aggiunto

			}

			// caricamento inventari

			String ticket = navi.getUserTicket();
			String polo = navi.getUtente().getCodPolo();
			String codBib = currentForm.getDatiOrdine().getCodBibl();
			if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getCodPolo()!=null && currentForm.getDatiOrdine().getCodPolo().length()==3)
				polo = currentForm.getDatiOrdine().getCodPolo();

			// leggo serie inventariale per caricare combo lista serie
			List<SerieVO> listaSerie = this.getListaSerie(polo, codBib, ticket);
			if (!ValidazioneDati.isFilled(listaSerie) ) {
				SerieVO eleListaSerie = new SerieVO();
				eleListaSerie.setCodSerie("   ");
				eleListaSerie.setDescrSerie("serie a blank                                                                   ");
				listaSerie = new ArrayList<SerieVO>();
				listaSerie.add(eleListaSerie);

				currentForm.setListaSerie(listaSerie);
				currentForm.set("listaSerie",currentForm.getListaSerie());

			} else {
				//esaordR.setListaSerie(listaSerie);
				// carico la lista delle serie nella combo
				currentForm.setListaSerie(listaSerie);
				currentForm.set("listaSerie",currentForm.getListaSerie());
			}


			if (!currentForm.isCaricamentoIniziale()) {
				//almaviva5_20120220 rfid
				currentForm.setRfidEnabled(Boolean.valueOf(CommonConfiguration.getProperty(Configuration.RFID_ENABLE, "false")));
				currentForm.set(EsaminaOrdineRForm.LOCK_VOLUME, false);

				currentForm.setCaricamentoIniziale(true);

			}
			else			// reimposta la fattura con i valori immessi
    		{
				if (currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI) != null) {
					listaInv = new ArrayList<StrutturaInventariOrdVO>(
							Arrays.asList((StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI)));


				}
			}

				currentForm.setElencaInventari(listaInv);
				currentForm.setNumRigheInv(listaInv.size());


	    		if (request.getParameter("tagNote")!=null )
	    	   	{
	        		if (currentForm.getClicNotaPrg()!=null && currentForm.getClicNotaPrg().equals(Integer.valueOf(request.getParameter("tagNote"))))
	        	   	{
	        			currentForm.setClicNotaPrg(-1);
	        	   	}
		    		else
		    	   	{
		    			currentForm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
		    	   	}
	    	   	}


	    		StrutturaInventariOrdVO[] oggettoInv=new StrutturaInventariOrdVO[listaInv.size()];
				for (int i=0; i<listaInv.size(); i++)
				{
					oggettoInv[i]=listaInv.get(i);
				}
				currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI,oggettoInv);
				currentForm.set("numRigheInv",oggettoInv.length);
				if (ValidazioneDati.size(listaInv) == 1)
					currentForm.set(EsaminaOrdineRForm.SELECTED, new Integer[] {ValidazioneDati.first(listaInv).getRepeatableId() } );

				//almaviva5_20122911 evolutive google
				preparaListaTipoData(form);

				return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	enum TipoData {
		USCITA,
		RIENTRO_PRESUNTA,
		RIENTRO;
	}


	private void preparaListaTipoData(ActionForm form) {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		OrdiniVO ordine = currentForm.getDatiOrdine();

		List<ComboVO> output = new ArrayList<ComboVO>();
		if (!ordine.isSpedito() ) {
			output.add(new ComboVO(TipoData.USCITA.name(), "uscita"));
			output.add(new ComboVO(TipoData.RIENTRO_PRESUNTA.name(), "rientro presunta"));
		} else
			output.add(new ComboVO(TipoData.RIENTRO.name(), "rientro"));

		currentForm.setListaTipoData(output);
		currentForm.set("listaTipoData", output);

	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		try {
			if (request.getSession().getAttribute("chiamante") != null) {
				// fare prima la get dinamica e poi settare l'elenco
				StrutturaInventariOrdVO[] elencaRighe0 = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
				// trasformo in arraylist
    			List<StrutturaInventariOrdVO> listaInv = new ArrayList<StrutturaInventariOrdVO>();
				for (StrutturaInventariOrdVO sio : elencaRighe0)
					listaInv.add(sio);

				currentForm.setElencaInventari(listaInv);
				//request.getSession().setAttribute(NavigazioneAcquisizioni.EsaminaOrdineRForm.LISTA_INVENTARI_ORDINE,  esaordR.getElencaInventari());
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(request.getSession().getAttribute("chiamante")+".do");
				action.setRedirect(true);
				return action;
				//return mapping.findForward("indietro");

			} else
				return mapping.getInputForward();

		}
		catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		UserVO utente = Navigation.getInstance(request).getUtente();

		try {
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			if (!ValidazioneDati.isFilled(inventari))
				return aggiungiRiga(mapping, currentForm, request, response);

			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			if (ValidazioneDati.size(items) != 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}

			Boolean lock = (Boolean) currentForm.get(EsaminaOrdineRForm.LOCK_VOLUME);
			List<StrutturaInventariOrdVO> tmp = new ArrayList<StrutturaInventariOrdVO>(Arrays.asList(inventari));
			Integer selected = items.get(0);
			int idx = StrutturaInventariOrdVO.indexOfRepeatableId(selected, tmp);
			StrutturaInventariOrdVO old = tmp.get(idx);

			StrutturaInventariOrdVO inv = new StrutturaInventariOrdVO(utente.getCodPolo(), utente.getCodBib());
			short pos = (short) (old.getPosizione() != null ? old.getPosizione() : idx + 1);
			List<StrutturaInventariOrdVO> volume = OrdiniWebUtil.selezionaVolume(tmp, old.getVolume());
			boolean first = ValidazioneDati.size(volume) == 1 || ValidazioneDati.first(volume) == old;

			inv.setPosizione(pos);
			inv.setVolume(old.getVolume());
			tmp.add(idx, inv);
			for (int i = idx + 1; i < tmp.size(); i++) {
				StrutturaInventariOrdVO sio = tmp.get(i);
				sio.setPosizione(++pos);
				if (!lock && first) {
					Short v = sio.getVolume();
					sio.setVolume(++v);
				}
			}

			inventari = tmp.toArray(new StrutturaInventariOrdVO[0]);
			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, inventari);
			currentForm.set("numRigheInv", inventari.length);

			currentForm.set(EsaminaOrdineRForm.SELECTED, new Integer[] { inv.getRepeatableId() });

		} catch (Exception e) {
			log.error("", e);
		}

		return mapping.getInputForward();

	}

	public ActionForward aggiungiRiga(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();
		StrutturaInventariOrdVO inv = new StrutturaInventariOrdVO(utente.getCodPolo(), utente.getCodBib());

		try {
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			if (!ValidazioneDati.isFilled(inventari)) {
				inventari = new StrutturaInventariOrdVO[1];
				inventari[0] = inv;
				// almaviva5_20121107 evolutive google
				inv.setPosizione((short) 1);
				inv.setVolume((short) 1);
				currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, inventari);
				currentForm.set("numRigheInv", 1);
				// gestione selezione della riga in inserimento
				currentForm.set(EsaminaOrdineRForm.SELECTED, new Integer[] { inv.getRepeatableId() } );
				return mapping.getInputForward();
			}

			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			int items_sel = ValidazioneDati.size(items);
			if (items_sel > 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}

			Boolean lock = (Boolean) currentForm.get(EsaminaOrdineRForm.LOCK_VOLUME);
			List<StrutturaInventariOrdVO> tmp = new ArrayList<StrutturaInventariOrdVO>(Arrays.asList(inventari));
			if (items_sel == 0) {
				StrutturaInventariOrdVO last = ValidazioneDati.last(tmp);
				short vol = last.getVolume();
				inv.setVolume(lock ? vol : ++vol);
				tmp.add(inv);
				OrdiniWebUtil.rinumera(tmp);

			} else {

				Integer selected = items.get(0);
				int idx = StrutturaInventariOrdVO.indexOfRepeatableId(selected, tmp);
				StrutturaInventariOrdVO old = tmp.get(idx);

				short pos = (short) (old.getPosizione() != null ? old.getPosizione() : idx + 1);
				List<StrutturaInventariOrdVO> volume = OrdiniWebUtil.selezionaVolume(tmp, old.getVolume());
				boolean last = ValidazioneDati.size(volume) == 1 || ValidazioneDati.last(volume) == old;

				inv.setPosizione(++pos);
				Short vol = old.getVolume();
				inv.setVolume( (!lock && last) ? ++vol : vol);
				tmp.add(idx + 1, inv);
				for (int i = idx + 2; i < tmp.size(); i++) {
					StrutturaInventariOrdVO sio = tmp.get(i);
					sio.setPosizione(++pos);
					if (!lock && last) {
						Short v = sio.getVolume();
						sio.setVolume(++v);
					}
				}
			}

			inventari = tmp.toArray(new StrutturaInventariOrdVO[0]);
			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, inventari);
			currentForm.set("numRigheInv", inventari.length);

			currentForm.set(EsaminaOrdineRForm.SELECTED, new Integer[] { inv.getRepeatableId() });

			navi.getCache().getCurrentElement().setAnchorId(inv.getRepeatableId() + "");
			request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + inv.getRepeatableId());
			request.setAttribute("last", inventari.length - 1);

		} catch (Exception e) {
			log.error("", e);
		}

		return mapping.getInputForward();

	}


	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;

		try {
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			if (!ValidazioneDati.isFilled(inventari))
				return mapping.getInputForward();

			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			if (!ValidazioneDati.isFilled(items)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));
				return mapping.getInputForward();
			}


			List<StrutturaInventariOrdVO> tmp = new ArrayList<StrutturaInventariOrdVO>(Arrays.asList(inventari));
			for (Integer id : items) {
				int idx = StrutturaInventariOrdVO.indexOfRepeatableId(id, tmp);
				tmp.remove(idx);
			}

			/*
			short pos = 1;
			for (int i = 0; i < tmp.size(); i++)
				tmp.get(i).setPosizione(pos++);
			*/
			OrdiniWebUtil.rinumera(tmp);

			inventari = tmp.toArray(new StrutturaInventariOrdVO[0]);
			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, inventari);
			currentForm.set("numRigheInv", inventari.length);

			currentForm.set(EsaminaOrdineRForm.SELECTED, null);

		} catch (Exception e) {
			log.error("", e);
		}

		return mapping.getInputForward();
	}


	public ActionForward completa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
			// CONTROLLO CHECK DI RIGA
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			if (!ValidazioneDati.isFilled(inventari))
				return mapping.getInputForward();

			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			if (ValidazioneDati.size(items) != 1)
				return mapping.getInputForward();

			OrdiniVO ordine = currentForm.getDatiOrdine();

			Integer selected = ValidazioneDati.first(items);
			StrutturaInventariOrdVO inv = StrutturaInventariOrdVO.searchRepeatableId(selected, Arrays.asList(inventari));

			this.validaInv(request, inv, ordine, inventari);
			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, inventari);

			/*
			//almaviva5_20131007 evolutive google2
			AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
			List<InventarioVO> altriInvDigit = delegate.esisteInventarioDigitalizzato(inv);
			if (ValidazioneDati.isFilled(altriInvDigit)) {
				List<String> tmp = new ArrayList<String>();
				for (InventarioVO i : altriInvDigit)
					tmp.add("'" + i.getChiaveInventario() + "'");
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordine.inventari.digit.altri",
					inv.getChiave(), ValidazioneDati.formatValueList(tmp, ", ")) );
				currentForm.setConferma(true);
				currentForm.setDisabilitaTutto(true);
				currentForm.setOperazione(TipoOperazione.INV_DUPLICATO);
				return mapping.getInputForward();
			}
			*/
			//almaviva5_20130121 evolutive google
			List<StrutturaInventariOrdVO> tmp = new ArrayList<StrutturaInventariOrdVO>(Arrays.asList(inventari));
			if (selected == ValidazioneDati.last(tmp).getRepeatableId() )
				return aggiungiRiga(mapping, currentForm, request, response);

			return mapping.getInputForward();

		} catch (SbnBaseException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni."	+ e.getMessage()));
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error("", e);
			return mapping.getInputForward();
		}
	}

	public ActionForward assegnaData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		try {
			String tipoData = (String) currentForm.get("tipoData");
			if (!ValidazioneDati.isFilled(tipoData))
				return mapping.getInputForward();

			TipoData tipo = TipoData.valueOf(tipoData);

			List<StrutturaInventariOrdVO> inventari = Arrays.asList((StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI));
			if (!ValidazioneDati.isFilled(inventari))
				return mapping.getInputForward();

			String data = ValidazioneDati.trimOrNull((String) currentForm.get("data") );
			if (data != null && ValidazioneDati.validaData(data) != ValidazioneDati.DATA_OK)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "ordine.label.dataInv");


			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			if (!ValidazioneDati.isFilled(items)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));
				return mapping.getInputForward();
			}

			for (Integer id : items) {
				StrutturaInventariOrdVO inv = StrutturaInventariOrdVO.search(id, inventari);
				switch (tipo) {
				case USCITA:
					inv.setDataUscita(data);
					break;

				case RIENTRO:
					inv.setDataRientro(data);
					break;

				case RIENTRO_PRESUNTA:
					inv.setDataRientroPresunta(data);
					break;
				}
			}

			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		try {
			List<StrutturaInventariOrdVO> listaInv = currentForm.getElencaInventari();
			currentForm.setElencaInventari(listaInv);
			currentForm.setNumRigheInv(listaInv.size());
			StrutturaInventariOrdVO[] oggettoInv = new StrutturaInventariOrdVO[listaInv.size()];
			for (int i = 0; i < listaInv.size(); i++) {
				oggettoInv[i] = listaInv.get(i);
			}

			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, oggettoInv);
			currentForm.set("numRigheInv", oggettoInv.length);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;

    	try {
    		// validazione elenco
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			// gestione del tipo rilegatura senza inventari associati tck 3026 e .... collaudo
			if (!ValidazioneDati.isFilled(inventari) )
				throw new ValidationException("rilegaturaInventariAssenti",
						ValidationExceptionCodici.rilegaturaInventariAssenti);

			OrdiniVO ordine = currentForm.getDatiOrdine();
			List<String> duplicati = new ArrayList<String>();
			StrutturaInventariOrdVO p = Lambda.on(StrutturaInventariOrdVO.class);

			AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
			for (StrutturaInventariOrdVO inv : inventari) {
				// esame se nota valorizzata
				this.validaInv(request, inv, ordine, inventari);
				//almaviva5_20131007 evolutive google2
				//check duplicati solo se inserito dopo l'apertura della form
				if (inv.getCreationTime().after(currentForm.getCreationTime())) {
					List<InventarioVO> altriInvDigit = delegate.esisteInventarioDigitalizzato(inv, Lambda.collect(inventari, p.getChiave()));
					if (ValidazioneDati.isFilled(altriInvDigit))
						duplicati.add("'" + inv.getChiave() + "'");
				}

			}

			//conferma su duplicati
			if (ValidazioneDati.isFilled(duplicati)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordine.inventari.digit.altri.multiplo",
						ValidazioneDati.formatValueList(duplicati, ", ")) );
				currentForm.setConferma(true);
				currentForm.setDisabilitaTutto(true);
				currentForm.setOperazione(TipoOperazione.INV_DUPLICATO);
				return mapping.getInputForward();
			}

    		currentForm.setConferma(true);
    		currentForm.setDisabilitaTutto(true);
    		currentForm.setOperazione(TipoOperazione.SALVA);
    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));
    		return mapping.getInputForward();

    	}	catch (SbnBaseException ve) {
    		SbnErrorTypes error = ve.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, ve);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni."	+ ve.getMessage()));

			return mapping.getInputForward();

    	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public List<StrutturaQuater> getListaInventariInput(String disp) {

		List<StrutturaQuater> output = new ArrayList<StrutturaQuater>();
		Set<StrutturaQuater> hm = new HashSet<StrutturaQuater>();
		for (int i = 1; i <= 36; i++) {
			try {
				String format = String.format("%02d", i);
				Field field1 = this.getClass().getDeclaredField("serie" + format);
				Field field2 = this.getClass().getDeclaredField("numero" + format);
				Field field3 = this.getClass().getDeclaredField("dataRientroPresunta" + format);
				Field field4 = this.getClass().getField(disp);
				String serie = (String) field1.get(this);
				String inventario = (String) field2.get(this);
				String data = (String) field3.get(this);
				String codiceNoDisp = (String) field4.get(this);

				if (!serie.equals("  ") && !inventario.trim().equals("")) {
					hm.add(new StrutturaQuater(serie, inventario, data, codiceNoDisp));
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}

		output.addAll(hm);

		return output;

	}



	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		try {
			Navigation navi = Navigation.getInstance(request);

			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			TipoOperazione op = currentForm.getOperazione();
			currentForm.setOperazione(null);
			List<StrutturaInventariOrdVO> listaInv = new ArrayList<StrutturaInventariOrdVO>(
					Arrays.asList((StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI)));

			currentForm.setElencaInventari(listaInv);
			OrdiniVO ordine = currentForm.getDatiOrdine();
			ordine.setRigheInventariRilegatura(listaInv);

			switch (op) {
			case SALVA:
			case INV_DUPLICATO:
				// salvataggio ordine con gli inventari
				boolean esitoOperazione = false;
				if (ordine.getIDOrd() > 0)
					esitoOperazione = this.modificaOrdine(ordine);
				else {
					ordine.setUtente(navi.getUtente().getFirmaUtente());
					esitoOperazione = this.inserisciOrdine(ordine);
					if (esitoOperazione)
						request.getSession().setAttribute("nuovoOrdineRilegatura", ordine);
				}
				if (!esitoOperazione)
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreModifica"));
				else {
					//almaviva5_20130910 evolutive google2
					//tutti gli inventari già legati vengono prima cancellati
					List<StrutturaInventariOrdVO> inventariOld = currentForm.getListaInventariOld();
					for (StrutturaInventariOrdVO sio : inventariOld)
						sio.setFlag_canc(true);

					List<StrutturaInventariOrdVO> inventari = currentForm.getElencaInventari();
					request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE, inventari);

					//inventari vengono prima cancellati, poi reinseriti.
					inventariOld.addAll(inventari);
					//

					//attivazione metodo di almaviva2 per cambiare la disponibilità  con codice=null
					Boolean risultato = true;
					if (ValidazioneDati.isFilled(inventari))
						risultato = OrdiniWebUtil.aggiornaDisponibilitaInventari(request, ordine, inventariOld);

					if (!risultato)
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.legameInventariAggDispKO"));
					else
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.legameInventariOK"));

					// aggiornamento dell'ordine per il problema concorrenza
					ListaSuppOrdiniVO criteriRicercaOrdine = new ListaSuppOrdiniVO();
					criteriRicercaOrdine.setAnnoOrdine(ordine.getAnnoOrdine());
					criteriRicercaOrdine.setTipoOrdine(ordine.getTipoOrdine());
					criteriRicercaOrdine.setCodOrdine(ordine.getCodOrdine());
					criteriRicercaOrdine.setCodBibl(ordine.getCodBibl());
					criteriRicercaOrdine.setCodPolo(ordine.getCodPolo());
					criteriRicercaOrdine.setOrdinamento("");

					List<OrdiniVO> arrOrdini = this.leggiOrdine(criteriRicercaOrdine);
					if (ValidazioneDati.size(arrOrdini) == 1) {
						OrdiniVO datiOrdine = arrOrdini.get(0);
						currentForm.setDatiOrdine(datiOrdine);

						//almaviva5_20140320 aggiornamento dettagli ordine su mappa precedente
						NavigationElement prev = navi.getCache().getPreviousElement();
						if (prev != null) {
							ActionForm fprev = prev.getForm();
							if (fprev != null && fprev instanceof OrdineBaseForm)
								((OrdineBaseForm) fprev).setDatiOrdine(datiOrdine);
						}
					}

				}
				break;
			}
			// fine salvataggio
			return mapping.getInputForward();

		} catch (SbnBaseException e) {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setOperazione(null);
			currentForm.setElencaInventari(new ArrayList<StrutturaInventariOrdVO>());

			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni."	+ e.getMessage()));

			return mapping.getInputForward();
		}
		catch (Exception e) {

			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setOperazione(null);
			currentForm.setElencaInventari(new ArrayList<StrutturaInventariOrdVO>());

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		TipoOperazione op = currentForm.getOperazione();
		currentForm.setOperazione(null);
		currentForm.setConferma(false);
		currentForm.setDisabilitaTutto(false);

		switch (op) {
		case SALVA:
			break;
		case INV_DUPLICATO:
			//cancella riga duplicata
			//return cancellaRiga(mapping, currentForm, request, response);
			break;
		}

		return mapping.getInputForward();
	}

	private void validaInv(HttpServletRequest request, StrutturaInventariOrdVO inv, OrdiniVO ordine, StrutturaInventariOrdVO[] inventari) throws Exception {

		StrutturaInventariOrdVO old = inv.copy();
		int old_idx = Arrays.binarySearch(inventari, old);
		try {
			StrutturaInventariOrdValidator validator = new StrutturaInventariOrdValidator(ordine);
			inv.validate(validator);

			UserVO utente = Navigation.getInstance(request).getUtente();
			String ticket = utente.getTicket();
			String polo = utente.getCodPolo();
			String bib = ValidazioneDati.isFilled(ordine.getCodBibl()) ? ordine.getCodBibl() : utente.getCodBib();

			String serie = inv.getSerie();
			Integer num = Integer.valueOf(inv.getNumero().trim());

			if (serie != null && num > 0) {
				InventarioVO inventario = null;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				try {
					inventario = factory.getGestioneDocumentoFisico()
							.getInventario(polo, bib, serie, num, this.getLocale(request, Constants.SBN_LOCALE), ticket);

				} catch (Exception e) {
					log.error("", e);
				}

				// controllo bid per recupero titolo
				if (inventario != null) {
					if (ValidazioneDati.in(inventario.getCancDB2i(), "S", "s"))
						throw new ApplicationException(SbnErrorTypes.GDF_INVENTARIO_CANCELLATO,
								new InventarioVO(bib, serie, num).getChiaveInventario() );

					if (inventario.isDismesso() )
						throw new ApplicationException(SbnErrorTypes.GDF_INVENTARIO_DISMESSO,
								inventario.getChiaveInventario() );

					inv.setTitolo(inventario.getBid());
					inv.setBid(inventario.getBid());
					// controllo titolo
					if (this.controllaTitolo(inventario.getBid()) != null) {
						String isbd = this.controllaTitolo(inventario.getBid());
						inv.setTitolo(inventario.getBid() + " - " + isbd);
					}

				} else {
					// specificare messaggio: inventario inesistente
					throw new ApplicationException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO,
							new InventarioVO(bib, serie, num).getChiaveInventario() );
				}

				Set<String> keys = new HashSet<String>();

				for (StrutturaInventariOrdVO sio : inventari) {
					if (inv != sio)
						sio.validate(validator);

					String kinv = sio.getChiave();
					if (keys.contains(kinv))
						throw new ValidationException(SbnErrorTypes.GDF_INVENTARIO_DUPLICATO, kinv );
					keys.add(kinv);
				}

			} else
				// specificare messaggio: inventario inesistente
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.inventarioInesistente"));

		} catch (SbnBaseException ve) {
			//ripristino inventario
			inventari[old_idx] = old;
			ve.addException(new ValidationException(SbnErrorTypes.ACQ_POSIZIONE_INVENTARIO,
					old.getPosizione().toString(), old.getVolume().toString()) );
			throw ve;
		}
	}


	private String controllaTitolo(String bidPassato) throws Exception {
		String tito = null;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			TitoloACQVO tit = factory.getGestioneAcquisizioni().getTitoloRox(bidPassato);
			if (tit != null)
				tito = tit.getIsbd();

		} catch (Exception e) {
			log.error("", e);
		}
		return tito;
	}

	private List<SerieVO> getListaSerie(String codPolo, String codBib, String ticket) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SerieVO> serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		return serie;
	}

	private boolean modificaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaOrdine(ordine);
		return valRitorno;
	}

	private boolean inserisciOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciOrdine(ordine);
		return valRitorno;
	}


	private List<OrdiniVO> leggiOrdine(ListaSuppOrdiniVO criteriRicercaOrdine) throws Exception {
		List<OrdiniVO> ordLetti = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ordLetti = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criteriRicercaOrdine);
		return ordLetti;
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;

		try {
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			if (!ValidazioneDati.isFilled(inventari))
				return mapping.getInputForward();

			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			if (ValidazioneDati.size(items) != 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordine.inv.sposta.troppi.elementi"));
				return mapping.getInputForward();
			}

			Integer selected = items.get(0);
			int idx = StrutturaInventariOrdVO.indexOfRepeatableId(selected, Arrays.asList(inventari) );
			if (idx < 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}

			List<StrutturaInventariOrdVO> tmp = new ArrayList<StrutturaInventariOrdVO>(Arrays.asList(inventari));
			OrdiniWebUtil.riposiziona(idx, idx - 1, tmp);
			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, tmp.toArray(new StrutturaInventariOrdVO[0]) );

			currentForm.set(EsaminaOrdineRForm.SELECTED, new Integer[] {selected} );

		} catch (Exception e) {
			log.error("", e);
		}

		return mapping.getInputForward();
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;

		try {
			StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
			if (!ValidazioneDati.isFilled(inventari))
				return mapping.getInputForward();

			List<Integer> items = getMultiBoxSelectedItems((Integer[]) currentForm.get(EsaminaOrdineRForm.SELECTED));
			if (ValidazioneDati.size(items) != 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordine.inv.sposta.troppi.elementi"));
				return mapping.getInputForward();
			}

			Integer selected = items.get(0);
			int idx = StrutturaInventariOrdVO.indexOfRepeatableId(selected, Arrays.asList(inventari) );
			if (idx >= inventari.length - 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}

			List<StrutturaInventariOrdVO> tmp = new ArrayList<StrutturaInventariOrdVO>(Arrays.asList(inventari));
			OrdiniWebUtil.riposiziona(idx, idx + 1, tmp);
			currentForm.set(EsaminaOrdineRForm.LISTA_INVENTARI, tmp.toArray(new StrutturaInventariOrdVO[0]) );

			currentForm.set(EsaminaOrdineRForm.SELECTED, new Integer[] {selected});

		} catch (Exception e) {
			log.error("", e);
		}

		return mapping.getInputForward();
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
		int size = ValidazioneDati.size(inventari);
		if (size > 0) {
			Integer[] selected = new Integer[size];
			for (int idx = 0; idx < size; idx++)
				selected[idx] = inventari[idx].getRepeatableId();

			currentForm.set(EsaminaOrdineRForm.SELECTED, selected);
		}

		return mapping.getInputForward();
	}

	public ActionForward nessuno(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		currentForm.set(EsaminaOrdineRForm.SELECTED, null);
		return mapping.getInputForward();
	}

	public ActionForward volume(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;

		Boolean lock = (Boolean) currentForm.get(EsaminaOrdineRForm.LOCK_VOLUME);
		currentForm.set(EsaminaOrdineRForm.LOCK_VOLUME, !lock);
		//almaviva5_20130614 evolutive google
		StrutturaInventariOrdVO[] inventari = (StrutturaInventariOrdVO[]) currentForm.get(EsaminaOrdineRForm.LISTA_INVENTARI);
		if (ValidazioneDati.isFilled(inventari))
			request.setAttribute("last", inventari.length - 1);

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		EsaminaOrdineRForm currentForm = (EsaminaOrdineRForm) form;
		if (ValidazioneDati.equals(idCheck, NavigazioneServizi.RFID))
			return currentForm.isRfidEnabled();

		return true;
	}

}
