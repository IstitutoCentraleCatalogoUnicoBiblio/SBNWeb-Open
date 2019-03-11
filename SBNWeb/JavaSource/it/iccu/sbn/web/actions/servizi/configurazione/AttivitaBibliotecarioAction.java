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
package it.iccu.sbn.web.actions.servizi.configurazione;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.common.AnagrafeUtenteProfessionaleVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.configurazione.AttivitaBibliotecarioForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class AttivitaBibliotecarioAction extends ConfigurazioneBaseAction {

	private static final String KEY_SEP = "_";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok",       "ok");
		map.put("servizi.bottone.annulla",  "chiudi");
		map.put("servizi.bottone.indietro", "chiudi");
		map.put("servizi.bottone.aggiungi", "aggiungi");
		map.put("servizi.bottone.rimuovi",  "rimuovi");

		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		AttivitaBibliotecarioForm currentForm = (AttivitaBibliotecarioForm)form;


		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			if (!currentForm.isSessione()) {
				TipoServizioVO tipoServizio = (TipoServizioVO)request.getAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR);
				if (tipoServizio==null) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.tipoServizioRichiesto"));

					throw new Exception("Tipo servizio non presente");
				}
				String codAttivita  = (String)request.getAttribute(ServiziBaseAction.COD_ATTIVITA_ATTR);
				String descAttivita = (String)request.getAttribute(ServiziBaseAction.DESC_ATTIVITA_ATTR);
				if (codAttivita==null || codAttivita.equals("") || descAttivita==null || descAttivita.equals("")) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.attivitaAssente"));

					throw new Exception("Nessuna attività selezionata");
				}

				currentForm.setCodiceAttivita(codAttivita);
				currentForm.setTipoServizio(tipoServizio);
				currentForm.setDescrizioneAttivita(descAttivita);
				caricaBibliotecari(currentForm, request);

				currentForm.setSessione(true);
			}
		} catch (RemoteException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);

			return backForward(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);

			return backForward(request, true);//backForward(mapping, pathChiamante);
		}

		return mapping.getInputForward();
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		AttivitaBibliotecarioForm currentForm = (AttivitaBibliotecarioForm)form;

		try {

			if (!currentForm.getAttivita_bibliotecari().equals(currentForm.getUltimo_attivita_bibliotecari())) {
				impostaChiaviDaAggiornare(currentForm);

				String utente = this.getFirmaUtente(request);
				Timestamp now = DaoManager.now();

				BaseVO attivitaBibliotecarioInfo = new BaseVO();
				attivitaBibliotecarioInfo.setFlCanc("N");

				attivitaBibliotecarioInfo.setTsIns(now);
				attivitaBibliotecarioInfo.setTsVar(now);
				attivitaBibliotecarioInfo.setUteIns(utente);
				attivitaBibliotecarioInfo.setUteVar(utente);

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				boolean operazioneOK = factory.getGestioneServizi()
									.aggiornaAttivitaBibliotecario(Navigation.getInstance(request).getUserTicket(), currentForm.getChiaviBibliotecariDaAggiungere(),
											currentForm.getChiaviBibliotecariDaRimuovere(), attivitaBibliotecarioInfo,
											currentForm.getTipoServizio().getIdTipoServizio(), currentForm.getCodiceAttivita());
				if (operazioneOK) {

					currentForm.setUltimo_attivita_bibliotecari((TreeMap<String, AnagrafeUtenteProfessionaleVO>)currentForm.getAttivita_bibliotecari().clone());


					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

					if (currentForm.getAttivita_bibliotecari().size()>0) {
						request.setAttribute("abilitata", "true");
					}
					else {
						request.setAttribute("abilitata", "false");
					}
				} else {
					throw new ApplicationException("Errore durante aggiornamento dati");
				}
			}
			else {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));

				resetToken(request);
				return mapping.getInputForward();
			}
		} catch (ApplicationException e) {
			resetToken(request);
			log.info(e.getMessage());
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		} catch (ValidationException e) {
			resetToken(request);
			log.info(e.getMessage());
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
		//return backForward(request);
	}


	public ActionForward aggiungi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		AttivitaBibliotecarioForm currentForm = (AttivitaBibliotecarioForm)form;

		String [] bibliotecariDaAggiungere = currentForm.getBibliotecariSelected();
		if (bibliotecariDaAggiungere.length>0) {
			for (int pos=0; pos<bibliotecariDaAggiungere.length; pos++) {
				currentForm.getAttivita_bibliotecari()
								 .put(bibliotecariDaAggiungere[pos],
									  (AnagrafeUtenteProfessionaleVO)(currentForm.getBibliotecari().get(bibliotecariDaAggiungere[pos])).clone() );
				currentForm.getBibliotecari().remove(bibliotecariDaAggiungere[pos]);
			}
		}

		return mapping.getInputForward();
	}

	public ActionForward rimuovi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		AttivitaBibliotecarioForm currentForm = (AttivitaBibliotecarioForm)form;

		String [] bibliotecariDaRimuovere = currentForm.getAttivita_bibliotecariSelected();
		if (bibliotecariDaRimuovere.length>0) {
			for (int pos=0; pos<bibliotecariDaRimuovere.length; pos++) {
				currentForm.getBibliotecari()
								 .put(bibliotecariDaRimuovere[pos],
									  (AnagrafeUtenteProfessionaleVO)(currentForm.getAttivita_bibliotecari().get(bibliotecariDaRimuovere[pos])).clone() );
				currentForm.getAttivita_bibliotecari().remove(bibliotecariDaRimuovere[pos]);
			}
		}

		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	{
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (request.getSession().getAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE)!=null) {
			request.getSession().setAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA, "true");
		}
		return mapping.findForward("indietro");
		//return backForward(request);
	}


	private void caricaBibliotecari(AttivitaBibliotecarioForm form, HttpServletRequest request)
	throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		Map<String, List> mappaBibliotecari = factory.getGestioneServizi().getListeAttivitaBibliotecari(Navigation.getInstance(request).getUserTicket(), form.getTipoServizio(), form.getCodiceAttivita());

		TreeMap<String, AnagrafeUtenteProfessionaleVO> bibliotecari          = new TreeMap<String, AnagrafeUtenteProfessionaleVO>();
		TreeMap<String, AnagrafeUtenteProfessionaleVO> bibliotecariAssociati = new TreeMap<String, AnagrafeUtenteProfessionaleVO>();
		AnagrafeUtenteProfessionaleVO anagrafeUtente = new AnagrafeUtenteProfessionaleVO();

		List<AnagrafeUtenteProfessionaleVO> bibliotecariList = mappaBibliotecari.get("BIBLIOTECARI");
		Iterator<AnagrafeUtenteProfessionaleVO> i = bibliotecariList.iterator();
		while (i.hasNext()) {
			anagrafeUtente = i.next();
			bibliotecari.put(this.getKeyBibliotecario(anagrafeUtente), anagrafeUtente);
		}

		bibliotecariList = mappaBibliotecari.get("BIBLIOTECARI_ASSOCIATI");
		i = bibliotecariList.iterator();
		while (i.hasNext()) {
			anagrafeUtente = i.next();
			bibliotecariAssociati.put(this.getKeyBibliotecario(anagrafeUtente), anagrafeUtente);
		}

		form.setBibliotecari(bibliotecari);
		form.setAttivita_bibliotecari(bibliotecariAssociati);
		form.setUltimo_attivita_bibliotecari((TreeMap<String, AnagrafeUtenteProfessionaleVO>)bibliotecariAssociati.clone());
	}


	private void impostaChiaviDaAggiornare(AttivitaBibliotecarioForm currentForm) throws Exception {
		Set<String> chiaviOld = currentForm.getUltimo_attivita_bibliotecari().keySet();
		Set<String> chiaviNew = currentForm.getAttivita_bibliotecari().keySet();

		currentForm.setChiaviBibliotecariDaRimuovere(new ArrayList<String>());

		Iterator<String> i_old = chiaviOld.iterator();
		String chiave, idUtenteProfessionale;
		while (i_old.hasNext()) {
			chiave=i_old.next();
			if (!chiaviNew.contains(chiave)) {
				//Si tratta di un bibliotecario che era associato ma che è stato rimosso dall'attività
				idUtenteProfessionale=getIDBibliotecarioByKey(chiave);
				if (idUtenteProfessionale.equals(""))
					throw new Exception("Bibliotecari da rimuovere. Exception nel parsing della chiave ["+chiave+"]");
				currentForm.getChiaviBibliotecariDaRimuovere().add(idUtenteProfessionale);
			}
		}

		currentForm.setChiaviBibliotecariDaAggiungere(new ArrayList<String>());

		Iterator<String> i_new = chiaviNew.iterator();
		while (i_new.hasNext()) {
			chiave=i_new.next();
			if (!chiaviOld.contains(chiave)) {
				//Si tratta di un bibliotecario appena associato
				idUtenteProfessionale=getIDBibliotecarioByKey(chiave);
				if (idUtenteProfessionale.equals(""))
					throw new Exception("Bibliotecari da aggiungere. Exception nel parsing della chiave ["+chiave+"]");
				currentForm.getChiaviBibliotecariDaAggiungere().add(idUtenteProfessionale);
			}
		}
	}


	private String getKeyBibliotecario(AnagrafeUtenteProfessionaleVO anagrafeUtente) {
		String cognome = anagrafeUtente.getCognome();
		String nome = anagrafeUtente.getNome();
		String key = ValidazioneDati.coalesce(cognome, "") + KEY_SEP
				+ ValidazioneDati.coalesce(nome, "") + KEY_SEP
				+ anagrafeUtente.getIdUtenteProfessionale();

		return key;
	}

	private String getIDBibliotecarioByKey(String key){
		return (key.lastIndexOf(KEY_SEP)!=-1 ? key.substring(key.lastIndexOf(KEY_SEP)+1) : "");
	}
}
