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
package it.iccu.sbn.web.actions.acquisizioni.sezioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.ListaSezioniForm;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaSezioniAction extends SinteticaLookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.scegli","scegli");
		map.put("button.blocco", "caricaBlocco");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			ListaSezioniForm listaSez = (ListaSezioniForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			if(!listaSez.isSessione())
			{
				listaSez.setSessione(true);
			}
			// attenzione al nome della var di sessione
			//solo x test
/*			ListaSuppSezioneVO ricArr=new ListaSuppSezioneVO();
			ricArr.setCodiceSezione("CONS");*/
			//fine solo x test

			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);

			// gestione selezione oggetto presente in campo di input di partenza

			if (ricArr!=null && ricArr.getCodiceSezione()!=null && ricArr.getCodiceSezione().trim().length()>0)
			{
				listaSez.setRadioSel(ricArr.getCodiceSezione().toUpperCase().trim());
			}

			// pulizia del codice per la effettuare la ricerca globale // richiesta di rossana del 29 maggio 2008
			ricArr.setCodiceSezione("");


			List<SezioneVO> listaSezioni;
			listaSezioni=this.getListaSezioniVO(ricArr, request );
			//this.loadFornitori();
			listaSez.setListaSezioni(listaSezioni);
			listaSez.setNumSezioni(listaSezioni.size());

			// gestione automatismo check su unico elemento lista con radio
			if (listaSez.getListaSezioni()!=null &&  listaSez.getListaSezioni().size()==1)
			{
				listaSez.setRadioSel(listaSez.getListaSezioni().get(0).getCodiceSezione().trim());
			}


			// gestione blocchi

			DescrittoreBloccoVO blocco1= null;
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			String ticket=utenteCollegato.getTicket();

			int maxElementiBlocco=50;

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			blocco1 = factory.getGestioneAcquisizioni().gestBlock(ticket,listaSezioni,maxElementiBlocco);
			listaSez.setListaSezioni(blocco1.getLista());

			if (blocco1 != null)
			{
			//if (blocco1 == null)
			//abilito i tasti per il blocco se necessario
			//memorizzo le informazioni per la gestione blocchi
			listaSez.setIdLista(blocco1.getIdLista()); //si
			listaSez.setTotRighe(blocco1.getTotRighe()); //no
			listaSez.setTotBlocchi(blocco1.getTotBlocchi()); //no
			listaSez.setMaxRighe(blocco1.getMaxRighe()); //no
			//this.sinCambio.setNumBlocco(blocco1.getNumBlocco()); //no
			listaSez.setBloccoSelezionato(blocco1.getNumBlocco()); //si
			//sinOfferte.setNumNotizie(sinOfferte.getTotRighe());
			//sinOfferte.setNumRighe(2);
			//sinOfferte.setNumBlocco(1);
			listaSez.setUltimoBlocco(blocco1);
			//listaSez.setLivelloRicerca(Navigation.getInstance(request).getUtente().getCodBib());
			}
			// fine gestione blocchi




//			SezioneVO sezOggetto= (SezioneVO) request.getAttribute("sezOggetto");
//			List<SezioneVO> listaSezOggetto=new ArrayList<SezioneVO>();
//			if (sezOggetto!=null)
//			{
//				listaSezOggetto=sezOggetto;
//			}
//
//			if (listaSezOggetto!=null && listaSezOggetto.size()>0)
//			{
//				String [] appoSelSez=new String [listaSezOggetto.size()];
//				for (int i=0;  i < listaSezOggetto.size(); i++)
//				{
//					String codSezi=listaSezOggetto.get(i).getCodiceSezione();
//					appoSelSez[i]=codSezi;
//				}
//				listaSez.setSelectedSezioni(appoSelSez);
//			}

			return mapping.getInputForward();

			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
					// assenzaRisultati = 4001;

					if (ve.getError()==4001)
					{
						listaSez.setRisultatiPresenti(false);
					}

					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaSezioniForm listaSez = (ListaSezioniForm) form;
		try {
				//return mapping.findForward("indietro");
			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);

			if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().trim().length()>0)
			{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante().trim()+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
			}
			else
			{
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ListaSezioniForm listaSez = (ListaSezioniForm) form;
		try {
			//request.setAttribute("sezScelto", (String []) listaSez.getSelectedSezioni());

			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);


			if (listaSez!=null && listaSez.getRadioSel()!=null &&  listaSez.getRadioSel().trim().length() >0)
			{
				String [] appoSez=new String [1];
				appoSez[0]=listaSez.getRadioSel().trim();
				// nel caso sia una scelta radio faccio in modo che l'array abbia un unico elemento
				listaSez.setSelectedSezioni(appoSez);
				List<SezioneVO> arrSezioStrut=new ArrayList<SezioneVO>();
				// selezioni effettuate
				// ciclo sui selezionati
				for (int i=0;  i < listaSez.getSelectedSezioni().length; i++)
				{
					String codProfil=listaSez.getSelectedSezioni()[i].trim();
					// ciclo su tutti i profili
					for (int j=0;  j < listaSez.getListaSezioni().size(); j++)
					{

						SezioneVO profilStrut=listaSez.getListaSezioni().get(j);
						if (profilStrut.getCodiceSezione().trim().equals(codProfil))
						{
							arrSezioStrut.add(profilStrut);
						}
					}
				}
				// aggiornamento della variabile di sessione
				ricSez.setSelezioniChiamato(arrSezioStrut);
			}

			//return mapping.findForward("indietro");

			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().trim().length()>0)
			{
					//request.setAttribute("fornScelto", (String) listSuppFornitori.getSelectedFornitori());
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricSez.getChiamante().trim()+".do");
					//request.getSession().removeAttribute("chiamante");
					return action;
			}
			else
			{
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSezioniForm listaSez = (ListaSezioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!listaSez.isSessione()) {
			listaSez.setSessione(true);
		}
		int numBlocco =0;
		numBlocco = listaSez.getBloccoSelezionato();
		String idLista = listaSez.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		// && numBlocco <=sinOfferte.getTotBlocchi()
		if ( numBlocco > 1  && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoSucc = factory.getGestioneAcquisizioni().nextBlocco(ticket,idLista, numBlocco);
			//DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			// old DescrittoreBloccoVO bloccoSucc = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().caricaBlock(ticket,idLista, numBlocco);

			//DescrittoreBloccoVO bloccoSucc = delegate.caricaBlocco(ticket,idLista, numBlocco);
			if (bloccoSucc != null) {
				listaSez.getListaSezioni().addAll(bloccoSucc.getLista());
//				if (bloccoSucc.getNumBlocco() < bloccoSucc.getTotBlocchi())
//					this.sinOfferte.setBloccoSelezionato(bloccoSucc.getNumBlocco() + 1);
//				// ho caricato tutte le righe sulla form
//				if (eleutenti.getListaUtenti().size() == bloccoVO.getTotRighe())
//					eleutenti.setAbilitaBlocchi(false);
				request.getSession().setAttribute("ultimoBloccoSezioni",bloccoSucc);
				listaSez.setUltimoBlocco(bloccoSucc); // aggiunto per gestire il ritorno alla sintetica con l'ultima lista completa caricata
			}
			else
			{
				request.getSession().setAttribute("ultimoBloccoSezioni",listaSez.getUltimoBlocco());
			}
		}
		return mapping.getInputForward();
	}



	private List<SezioneVO> getListaSezioniVO(ListaSuppSezioneVO criRicerca, HttpServletRequest request) throws Exception
	{
	List<SezioneVO> listaSezioni;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	criRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Documento Fisico 09/05/08
	listaSezioni = factory.getGestioneAcquisizioni().getRicercaListaSezioni(criRicerca);
	return listaSezioni;
	}


}
