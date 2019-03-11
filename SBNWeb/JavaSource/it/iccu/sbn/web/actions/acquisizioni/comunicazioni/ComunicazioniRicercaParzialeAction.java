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
package it.iccu.sbn.web.actions.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni.ComunicazioniRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ComunicazioniRicercaParzialeAction extends AcquisizioniBaseAction implements SbnAttivitaChecker{

	//private ComunicazioniRicercaParzialeForm ricComunicazioni;


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.label.fattura","fatturaCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.ordine","ordineCerca");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_COMUNICAZIONI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;

		try {

			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.comunicazioni") && (ricComunicazioni.getCodBibl()==null || (ricComunicazioni.getCodBibl()!=null && ricComunicazioni.getCodBibl().trim().length()==0)) &&  Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute("attributeListaSuppComunicazioneVO");
				request.getSession().removeAttribute("comunicazioniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_COMUNICAZIONE);
*/			}

			this.loadStatoComunicazione( ricComunicazioni);
			this.loadTipoOrdine( ricComunicazioni);
			this.loadTipoDocumento( ricComunicazioni);
			this.loadDirezioneComunicazione( ricComunicazioni);
			this.loadTipoOrdinamento( ricComunicazioni);

			// elenchi differenziati per direzione e tipo doc/ogg
			this.loadTipoMessaggio(ricComunicazioni);
			if (ricComunicazioni.getDirezioneComunicazione()!=null && ricComunicazioni.getDirezioneComunicazione().equals(""))
			{
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals("F"))
				{
					this.loadTipoMessaggioFatt( ricComunicazioni);
				}
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals("O"))
				{
					this.loadTipoMessaggioOrd( ricComunicazioni);
				}
				if (ricComunicazioni.getTipoDocumento()!=null &&  ricComunicazioni.getTipoDocumento().equals(""))
				{
					this.loadTipoMessaggio( ricComunicazioni);
				}
			}
			if (ricComunicazioni.getDirezioneComunicazione()!=null && ricComunicazioni.getDirezioneComunicazione().equals("A"))
			{
				if (ricComunicazioni.getTipoDocumento()!=null &&  ricComunicazioni.getTipoDocumento().equals("F"))
				{
					this.loadTipoMessaggioPerFornFatt( ricComunicazioni);
				}
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals("O"))
				{
					this.loadTipoMessaggioPerFornOrd( ricComunicazioni);
				}
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals(""))
				{
					this.loadTipoMessaggioPerForn( ricComunicazioni);
				}
			}
			if (ricComunicazioni.getDirezioneComunicazione()!=null && ricComunicazioni.getDirezioneComunicazione().equals("D"))
			{
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals("F"))
				{
					this.loadTipoMessaggioDaFornFatt( ricComunicazioni);
				}
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals("O"))
				{
					this.loadTipoMessaggioDaFornOrd( ricComunicazioni);
				}
				if (ricComunicazioni.getTipoDocumento()!=null && ricComunicazioni.getTipoDocumento().equals(""))
				{
					this.loadTipoMessaggioDaForn( ricComunicazioni);
				}
			}
			// fine elenchi differenziati

			// condizioni di ricerca univoca
			if ( ricComunicazioni.getCodMessaggio()!=null &&  ricComunicazioni.getCodMessaggio().trim().length()!=0 )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricComunicazioni.setTipoDocumento("");
				ricComunicazioni.setDirezioneComunicazione("");
				ricComunicazioni.setTipoMessaggio("");
				ricComunicazioni.setFornitore("");
				ricComunicazioni.setCodFornitore("");
				ricComunicazioni.setDataInizio("");
				ricComunicazioni.setDataFine("");

				ricComunicazioni.setAnnoOrdine("");
				ricComunicazioni.setTipoOrdine("");
				ricComunicazioni.setCodiceOrdine("");

				ricComunicazioni.setAnnoFattura("");
				ricComunicazioni.setProgressivoFattura("");

				ricComunicazioni.setDisabilitaTutto(true);
			}
			else
			{
				ricComunicazioni.setDisabilitaTutto(false);
			}



			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			if (biblio!=null && (ricComunicazioni.getCodBibl()==null || (ricComunicazioni.getCodBibl()!=null && ricComunicazioni.getCodBibl().trim().length()==0)))
			{
				ricComunicazioni.setCodBibl(biblio);
			}
			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				ricComunicazioni.setCodBibl(bibScelta.getCod_bib());
			}


			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						ricComunicazioni.setCodFornitore(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						ricComunicazioni.setFornitore(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
						ricComunicazioni.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricComunicazioni.setCodFornitore("");
					ricComunicazioni.setFornitore("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}
			//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
			ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
 			{
				if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
				{
					if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
					{
						ricComunicazioni.setTipoOrdine(ricOrd.getSelezioniChiamato().get(0).getTipoOrdine());
						ricComunicazioni.setAnnoOrdine(ricOrd.getSelezioniChiamato().get(0).getAnnoOrdine());
						ricComunicazioni.setCodiceOrdine(ricOrd.getSelezioniChiamato().get(0).getCodOrdine());
						ricComunicazioni.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricComunicazioni.setTipoOrdine("");
					ricComunicazioni.setAnnoOrdine("");
					ricComunicazioni.setCodiceOrdine("");

				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

 			}
			//controllo se ho un risultato di una lista di supporto FATTURA richiamata da questa pagina (risultato della simulazione)
			ListaSuppFatturaVO ricFatt=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
			if (ricFatt!=null && ricFatt.getChiamante()!=null && ricFatt.getChiamante().equals(mapping.getPath()))
 			{
				if (ricFatt!=null && ricFatt.getSelezioniChiamato()!=null && ricFatt.getSelezioniChiamato().size()!=0 )
				{
					if (ricFatt.getSelezioniChiamato().get(0).getNumFattura()!=null && ricFatt.getSelezioniChiamato().get(0).getNumFattura().length()!=0 )
					{
						ricComunicazioni.setAnnoFattura(ricFatt.getSelezioniChiamato().get(0).getAnnoFattura());
						ricComunicazioni.setProgressivoFattura(ricFatt.getSelezioniChiamato().get(0).getProgrFattura());
						ricComunicazioni.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricComunicazioni.setAnnoFattura("");
					ricComunicazioni.setProgressivoFattura("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFatturaVO");
				request.getSession().removeAttribute("fattureSelected");
				request.getSession().removeAttribute("criteriRicercaFattura");

 			}


			ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");

			// controllo che non riceva l'attributo di sessione di una lista supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null && this.ricOrdini.isProvenienzaVAIA())

			if ((ricArr!=null &&  ricArr.getChiamante()!=null && ricArr.getSelezioniChiamato()==null) )
			{
				// per il layout
				ricComunicazioni.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					ricComunicazioni.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine
					if (ricArr.isModalitaSif())
					{
						List<ComunicazioneVO> listaComunicazioni;
						listaComunicazioni=this.getListaComunicazioniVO(ricArr); // va in errore se non ha risultati
						this.caricaAttributeListaSupp( ricComunicazioni,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						return mapping.findForward("cerca");
					}
					else
					{
						if (!ricComunicazioni.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
						{
							this.caricaAttributeListaSupp( ricComunicazioni,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						}
						else
						{
							ricComunicazioni.setRientroDaSif(false);
						}
						return mapping.getInputForward();

					}

			}


			//almaviva5_20101122 gest. periodici
			SerieOrdineVO ordine = (SerieOrdineVO) request.getAttribute(PeriodiciDelegate.SIF_COMUNICAZIONI);
			if (ordine == null)
				return mapping.getInputForward();

			ricComunicazioni.setCodBibl(ordine.getCod_bib_ord());
			ricComunicazioni.setTipoDocumento("O");
			ricComunicazioni.setDirezioneComunicazione("");
			ricComunicazioni.setTipoMessaggio("");
			//ricComunicazioni.setFornitore(ordine.getFornitore());
			ricComunicazioni.setCodFornitore(ordine.getId_fornitore() + "");
			ricComunicazioni.setDataInizio("");
			ricComunicazioni.setDataFine("");

			ricComunicazioni.setAnnoOrdine(ordine.getAnno_ord() + "");
			ricComunicazioni.setTipoOrdine(ordine.getCod_tip_ord() + "");
			ricComunicazioni.setCodiceOrdine(ordine.getCod_ord() + "");
			ricComunicazioni.setTipoOrdinamSelez("1"); //data disc.

			return cerca(mapping, form, request, response);

		}	catch (ValidationException ve) {
/*				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
*/				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppComunicazioneVO ricArrRec=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
					this.caricaAttributeListaSupp( ricComunicazioni,ricArrRec);
					//return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//return mapping.findForward("crea");
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null )
					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
						this.saveErrors(request, errors);
						//return mapping.getInputForward();
					}
					return mapping.getInputForward();

/*					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea
						this.passaCriteri( ricComunicazioni, request); // imposta il crea con i valori cercati
						return mapping.findForward("crea");
					}
*/				}
				else
				{
					return mapping.getInputForward();
				}

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

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
			String chiama=null;
			//if (ricComunicazioni.isLSRicerca())
			if (ricComunicazioni.isVisibilitaIndietroLS())

			{
				ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
				chiama=ricArr.getChiamante();
			}

			// condizioni di ricerca univoca
			if ( ricComunicazioni.getCodMessaggio()!=null &&  ricComunicazioni.getCodMessaggio().trim().length()!=0 )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricComunicazioni.setTipoDocumento("");
				ricComunicazioni.setDirezioneComunicazione("");
				ricComunicazioni.setTipoMessaggio("");
				ricComunicazioni.setFornitore("");
				ricComunicazioni.setCodFornitore("");
				ricComunicazioni.setDataInizio("");
				ricComunicazioni.setDataFine("");

				ricComunicazioni.setAnnoOrdine("");
				ricComunicazioni.setTipoOrdine("");
				ricComunicazioni.setCodiceOrdine("");

				ricComunicazioni.setAnnoFattura("");
				ricComunicazioni.setProgressivoFattura("");

			}


			ListaSuppComunicazioneVO eleRicerca=new ListaSuppComunicazioneVO();
			request.getSession().removeAttribute("ultimoBloccoCom");

			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricComunicazioni.getCodBibl();
			String codMsg=ricComunicazioni.getCodMessaggio();
			String tipoDoc=ricComunicazioni.getTipoDocumento();
			String tipoMsg=ricComunicazioni.getTipoMessaggio();
			StrutturaCombo forn=new StrutturaCombo("","");
			forn.setCodice(ricComunicazioni.getCodFornitore());
			forn.setDescrizione(ricComunicazioni.getFornitore());
			StrutturaTerna idDoc=new StrutturaTerna("","","");
			if (ricComunicazioni.getTipoDocumento().equals("O"))
			{
				idDoc.setCodice1(ricComunicazioni.getTipoOrdine());
				idDoc.setCodice2(ricComunicazioni.getAnnoOrdine());
				idDoc.setCodice3(ricComunicazioni.getCodiceOrdine());
			}
			if (ricComunicazioni.getTipoDocumento().equals("F"))
			{
				idDoc.setCodice1("");
				idDoc.setCodice2(ricComunicazioni.getAnnoFattura());
				idDoc.setCodice3(ricComunicazioni.getProgressivoFattura());
			}
			String statoCom=ricComunicazioni.getStatoComunicazione();
			String dataComDa=ricComunicazioni.getDataInizio();
			String dataComA=ricComunicazioni.getDataFine();
			String dirCom=ricComunicazioni.getDirezioneComunicazione();
			String tipoInvioCom=ricComunicazioni.getTipoInvioComunicazione();
			//String chiama=null;
			String ordina="";

			eleRicerca=new ListaSuppComunicazioneVO(codP, codB, codMsg,  tipoDoc, tipoMsg,  forn, idDoc, statoCom, dataComDa, dataComA, dirCom, tipoInvioCom, chiama, ordina );
			eleRicerca.setElemXBlocchi(ricComunicazioni.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (ricComunicazioni.getTipoOrdinamSelez()!=null && !ricComunicazioni.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricComunicazioni.getTipoOrdinamSelez());
			}

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppComunicazioneVO", eleRicerca);
			// controllo di esistenza risultati su db se ci sono eccezioni
			List<ComunicazioneVO> listaComunicazioni = this.getListaComunicazioniVO(eleRicerca);
			//almaviva5_201011223 periodici
			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarksExist(PeriodiciDelegate.BOOKMARK_KARDEX, PeriodiciDelegate.BOOKMARK_FASCICOLO))
				navi.purgeThis();

			return mapping.findForward("cerca");

			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
			this.passaCriteri( ricComunicazioni, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void passaCriteri(ComunicazioniRicercaParzialeForm ricComunicazioni, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {


			String chiama=null;
			if (ricComunicazioni.isLSRicerca())
			{
				ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
				chiama=ricArr.getChiamante();
			}


			ListaSuppComunicazioneVO eleRicerca=new ListaSuppComunicazioneVO();

			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricComunicazioni.getCodBibl();
			String codMsg="";
			String tipoDoc=ricComunicazioni.getTipoDocumento();
			String tipoMsg=ricComunicazioni.getTipoMessaggio();
			StrutturaCombo forn=new StrutturaCombo("","");
			forn.setCodice(ricComunicazioni.getCodFornitore());
			forn.setDescrizione(ricComunicazioni.getFornitore());
			StrutturaTerna idDoc=new StrutturaTerna("","","");
			if (ricComunicazioni.getTipoDocumento().equals("O"))
			{
				idDoc.setCodice1(ricComunicazioni.getTipoOrdine());
				idDoc.setCodice2(ricComunicazioni.getAnnoOrdine());
				idDoc.setCodice3(ricComunicazioni.getCodiceOrdine());
			}
			if (ricComunicazioni.getTipoDocumento().equals("F"))
			{
				idDoc.setCodice1("");
				idDoc.setCodice2(ricComunicazioni.getAnnoFattura());
				idDoc.setCodice3(ricComunicazioni.getProgressivoFattura());
			}
			String statoCom="";
			String dataComDa="";
			String dataComA="";
			String dirCom=ricComunicazioni.getDirezioneComunicazione();
			String tipoInvioCom="";
			//String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppComunicazioneVO(codP, codB, codMsg,  tipoDoc, tipoMsg,  forn, idDoc, statoCom, dataComDa, dataComA, dirCom, tipoInvioCom, chiama, ordina );
			eleRicerca.setElemXBlocchi(ricComunicazioni.getElemXBlocchi());
			eleRicerca.setOrdinamento("");

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_COMUNICAZIONE, eleRicerca);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, ricComunicazioni);
			if (forward != null){
				return forward;
			}
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

/*			if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaFornitoreCerca( ricComunicazioni,request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( ComunicazioniRicercaParzialeForm ricComunicazioni,HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricComunicazioni.getCodBibl();
		String codForn=ricComunicazioni.getCodFornitore();
		String nomeForn=ricComunicazioni.getFornitore();
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0"; // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
			if (ricArr!=null )
			{
				//ListaSuppCambioVO eleRicArr=ricArr.get(0);
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
				{
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.getInputForward();
				}
			}
			else
			{
				//almaviva5_201011223 periodici
				Navigation navi = Navigation.getInstance(request);
				if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_FASCICOLO))
					return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_FASCICOLO, false);

				if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_KARDEX))
					return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_KARDEX, false);

				return mapping.getInputForward();
			}
			//return Navigation.getInstance(request)..goBack();
			//return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ordineCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			request.getSession().removeAttribute("ordiniSelected");
			request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

/*			if (request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null )
			{
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaOrdineCerca( ricComunicazioni,request,mapping);
			return mapping.findForward("ordineCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaOrdineCerca(ComunicazioniRicercaParzialeForm ricComunicazioni, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricComunicazioni.getCodBibl();
		String codBAff = null;
		String codOrd=ricComunicazioni.getCodiceOrdine();
		String annoOrd=ricComunicazioni.getAnnoOrdine();
		String tipoOrd=ricComunicazioni.getTipoOrdine(); // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		if (ricComunicazioni.getCodFornitore()!=null &&  ricComunicazioni.getCodFornitore().trim().length()>0 )
		{
			forn.setCodice(ricComunicazioni.getCodFornitore());
		}
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo ("","");
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=null;
		String chiama=mapping.getPath();
		String[] statoOrdArr=new String[0];
		Boolean stamp=false;
		Boolean rinn=false;

		eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
		String ticket=Navigation.getInstance(request).getUserTicket();
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);

		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward fatturaCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ComunicazioniRicercaParzialeForm ricComunicazioni = (ComunicazioniRicercaParzialeForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFatturaVO")==null
			request.getSession().removeAttribute("attributeListaSuppFatturaVO");
			request.getSession().removeAttribute("fattureSelected");
			request.getSession().removeAttribute("criteriRicercaFattura");

/*			if (request.getSession().getAttribute("criteriRicercaFattura")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFatturaVO");
				request.getSession().removeAttribute("fattureSelected");
				request.getSession().removeAttribute("criteriRicercaFattura");
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/			this.impostaFatturaCerca( ricComunicazioni,request,mapping);
			return mapping.findForward("fatturaCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFatturaCerca( ComunicazioniRicercaParzialeForm ricComunicazioni,HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricComunicazioni.getCodBibl();
		String annoF=ricComunicazioni.getAnnoFattura();
		String numF="";
		String staF="";
		String dataDa=ricComunicazioni.getAnnoFattura();
		String dataA=ricComunicazioni.getAnnoFattura();
		String prgF=ricComunicazioni.getProgressivoFattura();
		String dataF="";
		String dataRegF="";
		String tipF="";
		StrutturaTerna ordFatt=new StrutturaTerna("","","");
		StrutturaCombo fornFatt=new StrutturaCombo("","");
		if (ricComunicazioni.getCodFornitore()!=null &&  ricComunicazioni.getCodFornitore().trim().length()>0 )
		{
			fornFatt.setCodice(ricComunicazioni.getCodFornitore());
		}
		StrutturaTerna bilFatt=new StrutturaTerna("","","");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFatturaVO", eleRicerca);
		//String ticket=Navigation.getInstance(request).getUserTicket();
		//eleRicerca.setTicket(ticket);

	}catch (Exception e) {	}
	}

	private List<ComunicazioneVO> getListaComunicazioniVO(ListaSuppComunicazioneVO criRicerca) throws Exception
	{
	List<ComunicazioneVO> listaComunicazioni=new ArrayList();
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaComunicazioni = factory.getGestioneAcquisizioni().getRicercaListaComunicazioni(criRicerca);
	return listaComunicazioni;
	}

	private void caricaAttributeListaSupp(ComunicazioniRicercaParzialeForm ricComunicazioni, ListaSuppComunicazioneVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricComunicazioni.setCodBibl(ricArr.getCodBibl());
		ricComunicazioni.setCodMessaggio(ricArr.getCodiceMessaggio());
		ricComunicazioni.setDataInizio(ricArr.getDataComunicazioneDa());
		ricComunicazioni.setDataFine(ricArr.getDataComunicazioneA());
		ricComunicazioni.setDirezioneComunicazione(ricArr.getDirezioneComunicazione());
		ricComunicazioni.setStatoComunicazione(ricArr.getStatoComunicazione());
		ricComunicazioni.setTipoDocumento(ricArr.getTipoDocumento());
		ricComunicazioni.setTipoInvioComunicazione(ricArr.getTipoInvioComunicazione());
		ricComunicazioni.setTipoMessaggio(ricArr.getTipoMessaggio());
		ricComunicazioni.setCodFornitore(ricArr.getFornitore().getCodice());
		ricComunicazioni.setFornitore(ricArr.getFornitore().getDescrizione());
		if (ricArr.getTipoDocumento().equals("O"))
		{
			ricComunicazioni.setTipoOrdine(ricArr.getIdDocumento().getCodice1());
			ricComunicazioni.setAnnoOrdine(ricArr.getIdDocumento().getCodice2());
			ricComunicazioni.setCodiceOrdine(ricArr.getIdDocumento().getCodice3());
		}
		if (ricArr.getTipoDocumento().equals("F"))
		{
			ricComunicazioni.setAnnoFattura(ricArr.getIdDocumento().getCodice2());
			ricComunicazioni.setProgressivoFattura(ricArr.getIdDocumento().getCodice3());

		}
		//String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
	}catch (Exception e) {	}
	}

	private void loadTipoOrdinamento(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Data (disc.)");
		lista.add(elem);
		elem = new StrutturaCombo("2","Data (asc.)");
		lista.add(elem);
		elem = new StrutturaCombo("3","Nome fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("4","Tipo msg");
		lista.add(elem);

/*		elem = new StrutturaCombo("5","Anno doc");
		lista.add(elem);
		elem = new StrutturaCombo("6","Codice doc");
		lista.add(elem);
*/		ricComunicazioni.setListaTipiOrdinam(lista);
	}

	private void loadTipoOrdine(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricComunicazioni.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));
	}


	private void loadTipoDocumento(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - Fattura");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - Ordine");
		lista.add(elem);

		ricComunicazioni.setListaTipoDocumento(lista);
	}

	private void loadStatoComunicazione(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1	- RICEVUTO");
		lista.add(elem);
		elem = new StrutturaCombo("2","2	- SPEDITO");
		lista.add(elem);
		elem = new StrutturaCombo("3","3	- NON SPEDITO");
		lista.add(elem);
		ricComunicazioni.setListaStatoComunicazione(lista);
	}


	private void loadDirezioneComunicazione(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("A","Per fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("D","Da Fornitore");
		lista.add(elem);

		ricComunicazioni.setListaDirezioneComunicazione(lista);
	}

   private void loadTipoMessaggioCodici(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricComunicazioni.setListaTipoMessaggio(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipiMessaggio()));
    }


	private void loadTipoMessaggio(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));


	}

	private void loadTipoMessaggioOrd(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	private void loadTipoMessaggioFatt(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	private void loadTipoMessaggioDaFornOrd(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	private void loadTipoMessaggioDaFornFatt(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	private void loadTipoMessaggioPerFornOrd(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	private void loadTipoMessaggioPerFornFatt(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);

		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	private void loadTipoMessaggioPerForn(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}
	private void loadTipoMessaggioDaForn(ComunicazioniRicercaParzialeForm ricComunicazioni) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		ricComunicazioni.setListaTipoMessaggio(lista);
		ricComunicazioni.setListaTipoMessaggio(this.ElencaPer(lista, ricComunicazioni,"tipo"));

	}

	public List<StrutturaCombo> ElencaPer(List<StrutturaCombo> lst,ComunicazioniRicercaParzialeForm ricComunicazioni,String sortBy ) throws EJBException {
		//List<OrdiniVO> lst = sintordini.getListaOrdini();
		Comparator comp=null;
		if (sortBy==null)
		{
			comp =new TipoMsgAscending();
		}
		else if (sortBy.equals("tipo")) {
			comp =new TipoMsgAscending();
		}
		if (lst != null)
		{
			if (comp != null)
			{
				Collections.sort(lst, comp);
			}
		}
		return lst;
	}

	private static class TipoMsgAscending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((StrutturaCombo) o1).getDescrizione();
				String e2 = ((StrutturaCombo) o2).getDescrizione();
				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private static class TipoMsgDescending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((StrutturaCombo) o1).getDescrizione();
				String e2 = ((StrutturaCombo) o2).getDescrizione();
				return - e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		// TODO Auto-generated method stub
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione


/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_COMUNICAZIONI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
*/		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_COMUNICAZIONI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;
	}


}
