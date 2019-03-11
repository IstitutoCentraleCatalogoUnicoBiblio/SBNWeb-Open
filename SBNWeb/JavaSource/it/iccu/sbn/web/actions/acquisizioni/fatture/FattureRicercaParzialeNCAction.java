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
package it.iccu.sbn.web.actions.acquisizioni.fatture;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fatture.FattureRicercaParzialeNCForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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

public class FattureRicercaParzialeNCAction extends AcquisizioniBaseAction implements SbnAttivitaChecker{


	//private FattureRicercaParzialeForm ricFatture;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.ordine","ordiniCerca");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_FATTURE, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
			try {

				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!ricFattureNC.isSessione())
				{
					ricFattureNC.setSessione(true);
				}
				if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.fatture") && (ricFattureNC.getCodiceBibl()==null || (ricFattureNC.getCodiceBibl()!=null && ricFattureNC.getCodiceBibl().trim().length()==0)) &&  Navigation.getInstance(request).getActionCaller()==null)
				{
					// si proviene dal menu
					Pulisci.PulisciVar(request);

/*					request.getSession().removeAttribute("passaggioListaSuppFatturaVO");
					request.getSession().removeAttribute("fattureSelected");
					request.getSession().removeAttribute("criteriRicercaFattura");
*/				}
				if (ricFattureNC.getOrdine()==null)
				{
					ricFattureNC.setOrdine(new StrutturaTerna("","",""));
				}
				if (ricFattureNC.getFornitore1()==null)
				{
					ricFattureNC.setFornitore1(new StrutturaCombo("",""));
				}
				this.loadTipoOrdine( ricFattureNC);
				this.loadTipoFatt( ricFattureNC);
				this.loadStatoFatt( ricFattureNC);
				this.loadTipoOrdinamento( ricFattureNC);

				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				if (biblio!=null &&  ((ricFattureNC.getCodiceBibl()!=null && ricFattureNC.getCodiceBibl().trim().length()==0) || ricFattureNC.getCodiceBibl()==null ))
				{
					ricFattureNC.setCodiceBibl(biblio);
				}

				BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null)
				{
					ricFattureNC.setCodiceBibl(bibScelta.getCod_bib());
				}

				//ricFattureNC.setTipoFatt("F");
				//ricFattureNC.setStatoFatt("");


				// condizioni di ricerca univoca
				if ( ricFattureNC.getAnnoFatt()!=null && ricFattureNC.getProgrFatt()!=null && ricFattureNC.getAnnoFatt().trim().length()!=0 && ricFattureNC.getProgrFatt().trim().length()!=0  )
				{
					// ripulitura di tutti gli altri campi e disabilitazione
					ricFattureNC.setNumFatt("");
					ricFattureNC.setStatoFatt("");
					ricFattureNC.setDataFattDa("");
					ricFattureNC.setDataFattA("");
					ricFattureNC.getFornitore1().setCodice("");
					ricFattureNC.getFornitore1().setDescrizione("");
					ricFattureNC.setTipoFatt("");
					ricFattureNC.getOrdine().setCodice1("");
					ricFattureNC.getOrdine().setCodice2("");
					ricFattureNC.getOrdine().setCodice3("");
					ricFattureNC.setDisabilitaTutto(true);
				}
				else
				{
					ricFattureNC.setDisabilitaTutto(false);
				}

				//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
				ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
					{
						if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
						{
							ricFattureNC.getFornitore1().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
							ricFattureNC.getFornitore1().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
							ricFattureNC.setRientroDaSif(true);
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						ricFattureNC.getFornitore1().setCodice("");
						ricFattureNC.getFornitore1().setDescrizione("");
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
							ricFattureNC.getOrdine().setCodice1(ricOrd.getSelezioniChiamato().get(0).getTipoOrdine());
							ricFattureNC.getOrdine().setCodice2(ricOrd.getSelezioniChiamato().get(0).getAnnoOrdine());
							ricFattureNC.getOrdine().setCodice3(ricOrd.getSelezioniChiamato().get(0).getCodOrdine());
							ricFattureNC.setRientroDaSif(true);
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						ricFattureNC.getOrdine().setCodice1("");
						ricFattureNC.getOrdine().setCodice2("");
						ricFattureNC.getOrdine().setCodice3("");

					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
					request.getSession().removeAttribute("ordiniSelected");
					request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

	 			}


				// controllo che non riceva l'attributo di sessione di una lista supporto
				ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");

				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// per il layout
					ricFattureNC.setVisibilitaIndietroLS(true);
					// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

					//if (ricArr.getChiamante().endsWith("RicercaParziale"))
					//{
						ricFattureNC.setLSRicerca(true); // fai rox 2
					//}
					// per il layout fine
						if (ricArr.isModalitaSif())
						{
							List<FatturaVO> listaFatture;
							listaFatture=this.getListaFatturaVO(ricArr ); // va in errore se non ha risultati
							this.caricaAttributeListaSupp( ricFattureNC, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
							return mapping.findForward("cerca");
						}
						else
						{
							if (!ricFattureNC.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
							{
								this.caricaAttributeListaSupp( ricFattureNC, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
							}
							else
							{
								ricFattureNC.setRientroDaSif(false);
							}
							return mapping.getInputForward();
						}
				}
				else
				{
					return mapping.getInputForward();
				}

			}	catch (ValidationException ve) {

/*					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
*/					// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaCambi=this.getListaCambiVO(ricArr )
					// assenzaRisultati = 4001;
					if (ve.getError()==4001)
					{
						// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
						ListaSuppFatturaVO ricArrRec=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
						this.caricaAttributeListaSupp( ricFattureNC, ricArrRec);
						//ricFattureNC.setVisibilitaIndietroLS(true);
						//return mapping.getInputForward();

						//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
						//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && (ricArrRec.getChiamante().endsWith("RicercaParziale") || ricArrRec.getChiamante().equals("/acquisizioni/fatture/inserisciFattura") || ricArrRec.getChiamante().equals("/acquisizioni/fatture/esaminaFattura")) )
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

/*						else
						{
							// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
							// in tal caso la ricerca senza esito
							// deve automaticamente presentare la maschera di crea
							this.passaCriteri( ricFatture, request); // imposta il crea con i valori cercati
							return mapping.findForward("crea");
						}
*/
					}
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
		FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
			try {

				String chiama=null;
				//if (ricFattureNC.isLSRicerca())
				if (ricFattureNC.isVisibilitaIndietroLS())
				{
					ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
					chiama=ricArr.getChiamante();
				}
				ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
				// carica i criteri di ricerca da passare alla esamina
				request.getSession().removeAttribute("ultimoBloccoFatture");

				// condizioni di ricerca univoca
				if ( ricFattureNC.getAnnoFatt()!=null && ricFattureNC.getProgrFatt()!=null && ricFattureNC.getAnnoFatt().trim().length()!=0 && ricFattureNC.getProgrFatt().trim().length()!=0  )
				{
					// ripulitura di tutti gli altri campi e disabilitazione
					ricFattureNC.setNumFatt("");
					ricFattureNC.setStatoFatt("");
					ricFattureNC.setDataFattDa("");
					ricFattureNC.setDataFattA("");
					ricFattureNC.getFornitore1().setCodice("");
					ricFattureNC.getFornitore1().setDescrizione("");
					ricFattureNC.setTipoFatt("");
					ricFattureNC.getOrdine().setCodice1("");
					ricFattureNC.getOrdine().setCodice2("");
					ricFattureNC.getOrdine().setCodice3("");
				}


				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=ricFattureNC.getCodiceBibl();
				String annoF=ricFattureNC.getAnnoFatt();
				String numF=ricFattureNC.getNumFatt();
				String  staF=ricFattureNC.getStatoFatt();
				String dataDa=ricFattureNC.getDataFattDa();
				String dataA=ricFattureNC.getDataFattA();
				String prgF=ricFattureNC.getProgrFatt();
				String dataF="";
				String dataRegF="";
				String tipF=ricFattureNC.getTipoFatt();
				StrutturaTerna ordFatt=new StrutturaTerna(ricFattureNC.getOrdine().getCodice1(),ricFattureNC.getOrdine().getCodice2(),ricFattureNC.getOrdine().getCodice3());
				StrutturaCombo fornFatt=new StrutturaCombo(ricFattureNC.getFornitore1().getCodice(),ricFattureNC.getFornitore1().getDescrizione());
				StrutturaTerna bilFatt=new StrutturaTerna("","","");
				//String chiama=null;
				String ordina=ricFattureNC.getTipoOrdinamSelez();
				eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
				eleRicerca.setElemXBlocchi(ricFattureNC.getElemXBlocchi());
				eleRicerca.setOrdinamento("");
				if (ricFattureNC.getTipoOrdinamSelez()!=null && !ricFattureNC.getTipoOrdinamSelez().equals(""))
				{
					eleRicerca.setOrdinamento(ricFattureNC.getTipoOrdinamSelez());
				}
				request.getSession().setAttribute("passaggioListaSuppFatturaVO", eleRicerca);
				List<FatturaVO> listaFatture;
/*				if (ricFattureNC.getElemXBlocchi()>0)
				{
					request.setAttribute("numElexBlocchi",ricFattureNC.getElemXBlocchi());
				}
*/				listaFatture=this.getListaFatturaVO(eleRicerca);

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
		FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
		try {
			this.passaCriteri( ricFattureNC, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void passaCriteri(FattureRicercaParzialeNCForm ricFattureNC, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {
			String chiama=null;
			if (ricFattureNC.isLSRicerca())
			{
				ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricFattureNC.getCodiceBibl();
			String annoF="";
			String numF="";
			String  staF="";
			String dataDa="";
			String dataA="";
			String prgF="";
			String dataF="";
			String dataRegF="";
			String tipF=ricFattureNC.getTipoFatt();
			StrutturaTerna ordFatt=new StrutturaTerna(ricFattureNC.getOrdine().getCodice1(),ricFattureNC.getOrdine().getCodice2(),ricFattureNC.getOrdine().getCodice3());
			StrutturaCombo fornFatt=new StrutturaCombo(ricFattureNC.getFornitore1().getCodice(),ricFattureNC.getFornitore1().getDescrizione());
			StrutturaTerna bilFatt=new StrutturaTerna("","","");
			//String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
			eleRicerca.setElemXBlocchi(ricFattureNC.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			request.getSession().setAttribute("passaggioListaSuppFatturaVO", eleRicerca);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
			if (ricArr!=null )
			{
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
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward ordiniCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
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
			this.impostaOrdineCerca( ricFattureNC,request,mapping);
			return mapping.findForward("ordiniCerca");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaOrdineCerca( FattureRicercaParzialeNCForm ricFattureNC, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricFattureNC.getCodiceBibl();
		String codBAff = null;
		String codOrd=ricFattureNC.getOrdine().getCodice3();
		String annoOrd=ricFattureNC.getOrdine().getCodice2();
		String tipoOrd=ricFattureNC.getOrdine().getCodice1(); // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
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

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		FattureRicercaParzialeNCForm ricFattureNC = (FattureRicercaParzialeNCForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, ricFattureNC);
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
			this.impostaFornitoreCerca( ricFattureNC, request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( FattureRicercaParzialeNCForm ricFattureNC, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricFattureNC.getCodiceBibl();
		String codForn=ricFattureNC.getFornitore1().getCodice();
		String nomeForn=ricFattureNC.getFornitore1().getDescrizione();
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

	private void loadTipoOrdine(FattureRicercaParzialeNCForm ricFattureNC) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricFattureNC.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));
	}

	private void loadTipoFatt(FattureRicercaParzialeNCForm ricFattureNC) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricFattureNC.setListaTipoFatt(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoFattura()));
	}

	private List<FatturaVO> getListaFatturaVO(ListaSuppFatturaVO criRicerca) throws Exception
	{
	List<FatturaVO> listaFatture;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaFatture = factory.getGestioneAcquisizioni().getRicercaListaFatture(criRicerca);
	//listaFatture = new ArrayList();
	//FatturaVO fatt=new FatturaVO("X10", "01", "2004", "4", "10", "10/01/2004", "10/01/2004", 400.00,0.00, "EUR", 1, "2", "F" , new StrutturaCombo("33","Libreria Grande"),"");
	//listaFatture.add(fatt);
	return listaFatture;
	}



	private void caricaAttributeListaSupp(FattureRicercaParzialeNCForm ricFattureNC, ListaSuppFatturaVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricFattureNC.setCodiceBibl(ricArr.getCodBibl());
		ricFattureNC.setNumFatt(ricArr.getNumFattura());
		ricFattureNC.setStatoFatt(ricArr.getStatoFattura());
		ricFattureNC.setDataFattDa(ricArr.getDataFatturaDa());
		ricFattureNC.setDataFattA(ricArr.getDataFatturaA());
		ricFattureNC.setProgrFatt(ricArr.getProgrFattura());
		ricFattureNC.setTipoFatt(ricArr.getTipoFattura());
		ricFattureNC.setAnnoFatt(ricArr.getAnnoFattura());
		ricFattureNC.getOrdine().setCodice1(ricArr.getOrdine().getCodice1());
		ricFattureNC.getOrdine().setCodice2(ricArr.getOrdine().getCodice2());
		ricFattureNC.getOrdine().setCodice3(ricArr.getOrdine().getCodice3());

		ricFattureNC.getFornitore1().setCodice(ricArr.getFornitore().getCodice());
		ricFattureNC.getFornitore1().setDescrizione(ricArr.getFornitore().getDescrizione());

	}catch (Exception e) {	}
	}




	private void loadTipoOrdinamento(FattureRicercaParzialeNCForm ricFattureNC) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","Num. fatt.");
		lista.add(elem);
		elem = new StrutturaCombo("2","Data fatt. (disc.)");
		lista.add(elem);
		elem = new StrutturaCombo("3","Stato");
		lista.add(elem);
		elem = new StrutturaCombo("4","Nome fornitore");
		lista.add(elem);
		ricFattureNC.setListaTipiOrdinam(lista);
	}

	private void loadStatoFatt(FattureRicercaParzialeNCForm ricFattureNC) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("1","1 - Registrata");
		lista.add(elem);
		elem = new StrutturaCombo("2","2 - Controllata");
		lista.add(elem);
		elem = new StrutturaCombo("3","3 - Ordine di pagamento emesso");
		lista.add(elem);
		elem = new StrutturaCombo("4","4 - Contabilizzata");
		lista.add(elem);

		ricFattureNC.setListaStatoFatt(lista);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_FATTURE, utente.getCodPolo(), utente.getCodBib(), null);
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
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_FATTURE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;	}


}
