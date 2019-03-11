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
package it.iccu.sbn.web.actions.acquisizioni.gare;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.PartecipantiGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.gare.InserisciGaraForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

	public class InserisciGaraAction extends LookupDispatchAction {
		//private InserisciGaraForm insGare;
		//private InserisciGaraForm insGareNorm;



		protected Map<String, String> getKeyMethodMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ricerca.button.salva","salva");
			map.put("ricerca.button.ripristina","ripristina");
			map.put("ricerca.button.indietro","indietro");
	        map.put("acquisizioni.bottone.si", "Si");
	        map.put("acquisizioni.bottone.no", "No");

/*	        map.put("ricerca.button.insRiga","inserisciRiga");
			map.put("ricerca.button.cancRiga","cancellaRiga");*/

//			map.put("ricerca.button.stampa","stampa");
			map.put("ricerca.button.suggBibl","suggerimentiCerca");
			map.put("ricerca.button.suggLett","documentiCerca");
			map.put("ricerca.button.scegli","scegli");
			map.put("ricerca.label.fornitori","fornitore");
			map.put("ordine.bottone.searchTit", "sifbid");
			map.put("ricerca.button.insForn","inserisciForn");
			map.put("ricerca.button.cancForn","cancellaForn");
			map.put("ricerca.label.bibliolist", "biblioCerca");

			return map;
		}

		public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGareNorm = (InserisciGaraForm) form;
			try {
		        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		        UserVO utente = Navigation.getInstance(request).getUtente();
		        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
		        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
		            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_GARE_ACQUISTO, 10, "codBib");
		        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			InserisciGaraForm insGareNorm = (InserisciGaraForm) form;
			try {
				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!insGareNorm.isSessione())
				{
					insGareNorm.setSessione(true);
				}

/*				if (request.getParameter("paramLink") != null) {
					insGareNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("paramLink")));
					insGareNorm.setClicRispPrg(-1);
				}

				if (request.getParameter("paramLinkRisp") != null) {
					insGareNorm.setClicRispPrg(Integer.valueOf(request.getParameter("paramLinkRisp")));
					insGareNorm.setClicNotaPrg(-1);
				}


*/

				// gestione note di riga
				if (request.getParameter("tagNote")!=null )
	    	   	{
	    			//insGareNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
					insGareNorm.setClicRispPrg(-1);
	        		if (insGareNorm.getClicNotaPrg()!=null && insGareNorm.getClicNotaPrg().equals(Integer.valueOf(request.getParameter("tagNote"))))
	        	   	{
	        			insGareNorm.setClicNotaPrg(-1);
	        	   	}
		    		else
		    	   	{
		    			insGareNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
		    	   	}
	    	   	}

	    		// gestione risposta di riga
	    		if (request.getParameter("tagRisp")!=null )
	    	   	{
	    			//insGareNorm.setClicRispPrg(Integer.valueOf(request.getParameter("tagRisp")));
					insGareNorm.setClicNotaPrg(-1);
	        		if (insGareNorm.getClicRispPrg()!=null && insGareNorm.getClicRispPrg().equals(Integer.valueOf(request.getParameter("tagRisp"))))
	        	   	{
	        			insGareNorm.setClicRispPrg(-1);
	        	   	}
		    		else
		    	   	{
		    			insGareNorm.setClicRispPrg(Integer.valueOf(request.getParameter("tagRisp")));
		    	   	}

	    	   	}


				DynaValidatorForm insGare = (DynaValidatorForm) form;
				List arrListaStatoRichiestaOfferta=this.loadStatiRichiestaOfferta();
				insGare.set("listaStatoRichiestaOfferta",arrListaStatoRichiestaOfferta);

				List arrListaStatoPartecipanteGara=this.loadStatoPartecipante();
				insGare.set("listaStatoPartecipanteGara",arrListaStatoPartecipanteGara);

				List arrListaTipoInvio=this.loadTipoInvio();
				insGare.set("listaTipoInvio",arrListaTipoInvio);

				GaraVO oggettoGara= this.loadDatiGara();
				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib().trim());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				if (biblio!=null &&   (oggettoGara.getCodBibl()==null || (oggettoGara.getCodBibl()!=null && oggettoGara.getCodBibl().trim().length()==0)))
				{
					oggettoGara.setCodBibl(biblio);
				}


				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				oggettoGara.setCodPolo(polo);


				// preimpostazione della schermata di inserimento con i valori ricercati

				if (request.getSession().getAttribute("ATTRIBUTEListaSuppGaraVO")!=null)
				{
					ListaSuppGaraVO ele=(ListaSuppGaraVO) request.getSession().getAttribute("ATTRIBUTEListaSuppGaraVO");
					request.getSession().removeAttribute("ATTRIBUTEListaSuppGaraVO");
					if (ele.getBid()!=null )
					{
						oggettoGara.setBid(ele.getBid());
					}
					if (ele.getCodRicOfferta()!=null && ele.getCodRicOfferta().trim().length()>0)
					{
						oggettoGara.setCodRicOfferta(ele.getCodRicOfferta());;
					}
				}


				// reimposta la fattura con i valori immessi nella parte superiore
				if (insGare.get("richOff")!=null )
				{
					GaraVO oggettoTemp=(GaraVO) insGare.get("richOff");

					if (oggettoTemp.getCodBibl()!=null || (oggettoTemp.getBid()!=null && oggettoTemp.getBid().getCodice()!=null) || (oggettoTemp.getPrezzoIndGara()!=0) || (oggettoTemp.getNumCopieRicAcq()!=0 )  )
					{
						oggettoGara=(GaraVO) insGare.get("richOff");
					}
				}
				// carico eventuali righe inserite
				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
				// trasformo in arraylist
				List lista =new ArrayList();

				for (int w=0;  w < elencaRigheGara.length; w++)
				{
					PartecipantiGaraVO elem = elencaRigheGara[w];
					// esame se nota valorizzata
					if (elem.getNoteAlFornitore()!=null && elem.getNoteAlFornitore().trim().length()>0)
					{
						elem.setEsisteNota(true);
					}
					if (elem.getNoteAlFornitore()!=null && elem.getMsgRispDaFornAGara().trim().length()>0)
					{
						elem.setEsisteRisp(true);
					}
					lista.add(elem);
				}
				oggettoGara.setDettaglioPartecipantiGara(lista);
				BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null)
				{
					oggettoGara.setCodBibl(bibScelta.getCod_bib());
				}
				insGare.set("richOff",oggettoGara);


				// eventuali liste suporto esterne



				insGare.set("richOff",oggettoGara);
				int totGare=0;
				if (oggettoGara.getDettaglioPartecipantiGara()!=null && oggettoGara.getDettaglioPartecipantiGara().size()>0)
				{
					totGare=oggettoGara.getDettaglioPartecipantiGara().size();
				}


				insGare.set("numRighePartecipanti",totGare);

				PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[totGare];
				for (int i=0; i<totGare; i++)
				{
					//FornitoreVO eleRicForn = (FornitoreVO) oggettoProf.getListaFornitori().get(i);
					//StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i+1),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
					oggettoDettGara[i]=oggettoGara.getDettaglioPartecipantiGara().get(i);

					//oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
				}

				insGare.set("elencaRighePartecipanti",oggettoDettGara);

				//controllo se ho un risultato di una lista di supporto FORNITORI richiamata da questa pagina (risultato della simulazione)
				ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
					{
						if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
						{
							//completo la riga della fattura con le informazioni dell'ordine scelto
							if (oggettoGara.getDettaglioPartecipantiGara()!=null && oggettoGara.getDettaglioPartecipantiGara().size()>0)
							{
								int indiceStart=oggettoGara.getDettaglioPartecipantiGara().size();
								PartecipantiGaraVO[] listaForn= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
								for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
								{
									indiceStart=indiceStart+1;
									FornitoreVO eleRicForn = ricForn.getSelezioniChiamato().get(i);
									PartecipantiGaraVO eleListaForn = new PartecipantiGaraVO("", "",new StrutturaCombo(eleRicForn.getCodFornitore(), eleRicForn.getNomeFornitore()), "", "", "", "" , "", "" );
									oggettoGara.getDettaglioPartecipantiGara().add(eleListaForn);
								}
							}
							else
							{
								oggettoGara.setDettaglioPartecipantiGara(new ArrayList());
								for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
								{
									FornitoreVO eleRicForn = ricForn.getSelezioniChiamato().get(i);
									PartecipantiGaraVO eleListaForn = new PartecipantiGaraVO("", "",new StrutturaCombo(eleRicForn.getCodFornitore(), eleRicForn.getNomeFornitore()), "", "", "", "" , "", "" );
									oggettoGara.getDettaglioPartecipantiGara().add(eleListaForn);
								}
							}
							insGare.set("richOff",oggettoGara);

							PartecipantiGaraVO[] lista_fin =new PartecipantiGaraVO [oggettoGara.getDettaglioPartecipantiGara().size()];

							for (int r=0;  r < oggettoGara.getDettaglioPartecipantiGara().size(); r++)
							{
								lista_fin [r] = oggettoGara.getDettaglioPartecipantiGara().get(r);
								//lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
							}

							insGare.set("elencaRighePartecipanti",lista_fin);
							//insGare.set("radioPartecipante", null) ;
							insGare.set("numRighePartecipanti",lista_fin.length);

							//insGare.set("elencaRighePartecipanti", oggettoGara.getDettaglioPartecipantiGara());
							//insGare.set("numRighePartecipanti", oggettoGara.getDettaglioPartecipantiGara().size());


							// blocco importazione singolo oggetto
/*
 							Integer[] radioPartecipante=(Integer[])insGare.get("radioPartecipante");
							PartecipantiGaraVO[] oggettoDettGara3= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
							if (radioPartecipante.length!=0)
							{
									int indiceRigaIns= radioPartecipante[0]; // unico check
									FornitoreVO eleRicForn = (FornitoreVO) ricForn.getSelezioniChiamato().get(0);
									String numForn=eleRicForn.getCodFornitore();
									String nomeForn=eleRicForn.getNomeFornitore();
									if (!numForn.equal("") && !nomeForn.equal(""))
									{
										oggettoDettGara3[indiceRigaIns].getFornitore().setCodice(numForn);
										oggettoDettGara3[indiceRigaIns].getFornitore().setDescrizione(nomeForn);
									}
									insGare.set("elencaRighePartecipanti",(PartecipantiGaraVO[]) oggettoDettGara3);
									// ricarica la gara completamente
									GaraVO eleGara=(GaraVO) insGare.get("richOff");
									// trasformo in arraylist
									List<PartecipantiGaraVO> listaPart =new ArrayList();
									for (int w=0;  w < oggettoDettGara3.length; w++)
									{
										PartecipantiGaraVO elem = oggettoDettGara3[w];
										listaPart.add(elem);
									}
									eleGara.setDettaglioPartecipantiGara(listaPart);
									insGare.set("richOff",eleGara);

							}
*/
							}
					}
	/*				else
					{
						// pulizia della maschera di ricerca
						this.ricBuoni.setTipoOrdine("");
						this.ricBuoni.setAnno("");
						this.ricBuoni.setNumero("");

					}
	*/
					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
					request.getSession().removeAttribute("fornitoriSelected");
					request.getSession().removeAttribute("criteriRicercaFornitore");
	 			}


				//controllo se ho un risultato di IMPORTA DA DOCUMENTI richiamata da questa pagina (risultato della simulazione)
				ListaSuppDocumentoVO ricDoc=(ListaSuppDocumentoVO)request.getSession().getAttribute("attributeListaSuppDocumentoVO");
				if (ricDoc!=null && ricDoc.getChiamante()!=null && ricDoc.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricDoc!=null && ricDoc.getSelezioniChiamato()!=null && ricDoc.getSelezioniChiamato().size()!=0 )
					{
						// N.B. I DOC ACCETTATI HANNO OBBLIGATORIAMENTE UN BID ASSOCIATO
						if (ricDoc.getSelezioniChiamato().get(0).getTitolo()!=null && ricDoc.getSelezioniChiamato().get(0).getTitolo().getCodice().length()!=0 )
						{
							GaraVO oggettoGara2= (GaraVO) insGare.get("richOff");
							oggettoGara2.getBid().setCodice(ricDoc.getSelezioniChiamato().get(0).getTitolo().getCodice());
							oggettoGara2.getBid().setDescrizione(ricDoc.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
							insGare.set("richOff",oggettoGara2);
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						GaraVO oggettoGara2= (GaraVO) insGare.get("richOff");
						oggettoGara2.getBid().setCodice("");
						oggettoGara2.getBid().setDescrizione("");
						insGare.set("richOff",oggettoGara2);
					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppDocumentoVO");
					request.getSession().removeAttribute("documentiSelected");
					request.getSession().removeAttribute("criteriRicercaDocumento");
	 			}

				//controllo se ho un risultato di IMPORTA DA SUGGERIMENTI richiamata da questa pagina (risultato della simulazione)
				ListaSuppSuggerimentoVO ricSugg=(ListaSuppSuggerimentoVO)request.getSession().getAttribute("attributeListaSuppSuggerimentoVO");
				if (ricSugg!=null && ricSugg.getChiamante()!=null && ricSugg.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricSugg!=null && ricSugg.getSelezioniChiamato()!=null && ricSugg.getSelezioniChiamato().size()!=0 )
					{
						if (ricSugg.getSelezioniChiamato().get(0).getTitolo()!=null && ricSugg.getSelezioniChiamato().get(0).getTitolo().getCodice().length()!=0 )
						{
							GaraVO oggettoGara2= (GaraVO) insGare.get("richOff");
							oggettoGara2.getBid().setCodice(ricSugg.getSelezioniChiamato().get(0).getTitolo().getCodice());
							oggettoGara2.getBid().setDescrizione(ricSugg.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
							insGare.set("richOff",oggettoGara2);
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						GaraVO oggettoGara2= (GaraVO) insGare.get("richOff");
						oggettoGara2.getBid().setCodice("");
						oggettoGara2.getBid().setDescrizione("");
						insGare.set("richOff",oggettoGara2);
					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppSuggerimentoVO");
					request.getSession().removeAttribute("suggerimentiSelected");
					request.getSession().removeAttribute("criteriRicercaSuggerimento");
	 			}
				// controllo se ho un risultato da interrogazione ricerca
				String bid=(String) request.getAttribute("bid");
				if (bid!=null && bid.length()!=0 )
				{
					String titolo=(String) request.getAttribute("titolo");
					// controllo se ho un risultato da interrogazione ricerca
					//String acq = request.getParameter("ACQUISIZIONI");
					//if ( acq != null) {
					GaraVO oggettoGara5= (GaraVO) insGare.get("richOff");
					oggettoGara5.getBid().setCodice(bid);
					if ( titolo != null) {
						oggettoGara5.getBid().setDescrizione(titolo);
					}
					insGare.set("richOff",oggettoGara5);
				}

				// gestione lista supporto richiamata per le gare
				ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
				{
					// imposta visibilità bottone scegli
					// preimpostazione di campi con elemento ricercato e non trovato
					GaraVO oggettoGara2= (GaraVO) insGare.get("richOff");
					oggettoGara2.setCodRicOfferta(ricArr.getCodRicOfferta());
					oggettoGara2.getBid().setCodice(ricArr.getBid().getCodice());
					oggettoGara2.getBid().setDescrizione(ricArr.getBid().getDescrizione());
					insGare.set("richOff",oggettoGara2);
					if (!ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
					{
						insGare.set("visibilitaIndietroLS",true);
					}
				}


				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
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
			InserisciGaraForm insGare = (InserisciGaraForm) form;

//	 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

			try {
				ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
				if (ricArr!=null  && ricArr.getChiamante()!=null)
				{
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.findForward("indietro");
				}
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


/*
		public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insGare = (DynaValidatorForm) form;

			try {
				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
				//new PartecipantiGaraVO("X10", "01",new StrutturaCombo("33", "Libreria Feltrinelli"), "A", "00000011", "note", "07/02/2006" , "P", "risposta" );
				PartecipantiGaraVO dettGara = new PartecipantiGaraVO("", "",new StrutturaCombo("", ""), "", "", "", "" , "", "" );

				if (elencaRigheGara!=null &&  elencaRigheGara.length!=0)
				{
					PartecipantiGaraVO[] listaDettGara=new PartecipantiGaraVO[elencaRigheGara.length +1];

					for (int i=0; i<elencaRigheGara.length; i++)
					{
						listaDettGara[i]=elencaRigheGara[i];
						// gestione valorizzazione campi note
						if (elencaRigheGara[i].getNoteAlFornitore()!=null && elencaRigheGara[i].getNoteAlFornitore().trim().length()>0)
						{
							elencaRigheGara[i].setEsisteNota(true);
						}
						if (elencaRigheGara[i].getNoteAlFornitore()!=null && elencaRigheGara[i].getMsgRispDaFornAGara().trim().length()>0)
						{
							elencaRigheGara[i].setEsisteRisp(true);
						}

					}
					// imposta numero di riga
					//dettProf.setCodice1(String.valueOf(elencaRigheProf.length+1));
					listaDettGara[elencaRigheGara.length]=dettGara;
					insGare.set("elencaRighePartecipanti",listaDettGara);
					insGare.set("numRighePartecipanti",listaDettGara.length);
					// ricarica la fattura completamente
					List<PartecipantiGaraVO> elencaRigheGaraArr=new ArrayList();
					for (int i=0; i<listaDettGara.length; i++)
					{
						elencaRigheGaraArr.add(listaDettGara[i]);
					}
					// gestione selezione della riga in inserimento
					Integer[]  radioPartecipante=new Integer[1];
					if (listaDettGara!=null && listaDettGara.length>0)
					{
						radioPartecipante[0]=listaDettGara.length -1;
						insGare.set("radioPartecipante",radioPartecipante);
					}
				}
				else
				{
					elencaRigheGara=new PartecipantiGaraVO[1];
					elencaRigheGara[0]=dettGara;
					insGare.set("elencaRighePartecipanti",elencaRigheGara);
					// gestione selezione della riga in inserimento
					Integer[]  radioPartecipante=new Integer[1];
					if (elencaRigheGara!=null && elencaRigheGara.length>0)
					{
						radioPartecipante[0]=elencaRigheGara.length -1;
						insGare.set("radioPartecipante",radioPartecipante);
					}

				}
				insGare.set("numRighePartecipanti",elencaRigheGara.length);
				// trasformazione in arraylist



				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insGare = (DynaValidatorForm) form;
			try {

				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");

				Integer[] radioPartecipante=(Integer[])insGare.get("radioPartecipante");
				if (radioPartecipante.length!=0)
				{

					if (elencaRigheGara.length!=0 )
					{
						List lista =new ArrayList();
						for (int t=0;  t < elencaRigheGara.length; t++)
						{
							PartecipantiGaraVO elem = elencaRigheGara[t];
							lista.add(elem);
						}

						//String[] appo=  selectedDatiIntest;
						int i= (radioPartecipante.length) -1;
						// ciclo dall'ultimo codice selezionato
						while (i>=0)
						{
							int elem = radioPartecipante[i];
							// il valore del num riga è superiore di una unità rispetto all'indice dell'array
							lista.remove(elem);
							i=i-1;
						}

						//this.renumera(lista);
						PartecipantiGaraVO[] lista_fin =new PartecipantiGaraVO [lista.size()];

						for (int r=0;  r < lista.size(); r++)
						{
							lista_fin [r] = (PartecipantiGaraVO)lista.get(r);
							//lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
						}

						insGare.set("elencaRighePartecipanti",lista_fin);
						insGare.set("radioPartecipante", null) ;
						insGare.set("numRighePartecipanti",lista_fin.length);
						// ricarica la fattura completamente
						GaraVO appoGara=(GaraVO)insGare.get("richOff");
						List<PartecipantiGaraVO> elencaRigheGaraArr=new ArrayList();
						for (int j=0; j<lista_fin.length; j++)
						{
							elencaRigheGaraArr.add(lista_fin[j]);
						}
						appoGara.setDettaglioPartecipantiGara(elencaRigheGaraArr);
						insGare.set("richOff",appoGara);

					}
				}
				insGare.set("radioPartecipante",0);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		*/

		public ActionForward inserisciForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insGare = (DynaValidatorForm) form;

			try {

				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
				GaraVO eleGara=(GaraVO) insGare.get("richOff");

				// trasformo in arraylist
				List lista =new ArrayList();
				for (int w=0;  w < elencaRigheGara.length; w++)
				{
					PartecipantiGaraVO elem = elencaRigheGara[w];
					lista.add(elem);
				}
				eleGara.setDettaglioPartecipantiGara(lista);
				insGare.set("richOff",eleGara);
				insGare.set("numRighePartecipanti",elencaRigheGara.length);

				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

				ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
				// carica i criteri di ricerca da passare alla esamina
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=eleGara.getCodBibl();
				String codForn="";
				String nomeForn="";
				String codProfAcq="";
				String paeseForn="";
				String tipoPForn="";
				String provForn="";
				String loc="0"; // I FORNITORI DEVONO ESSERE DI BIBLIOTECA
				String chiama=mapping.getPath();
				//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
				eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
				//ricerca.add(eleRicerca);
				eleRicerca.setModalitaSif(false);
				request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

				return mapping.findForward("fornitori");
				//return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward cancellaForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insGare = (DynaValidatorForm) form;
			try {

				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");

				Integer[] radioPartecipante=(Integer[])insGare.get("radioPartecipante");
				if (radioPartecipante.length!=0)
				{

					if (elencaRigheGara.length!=0 )
					{
						List lista =new ArrayList();
						for (int t=0;  t < elencaRigheGara.length; t++)
						{
							PartecipantiGaraVO elem = elencaRigheGara[t];
							lista.add(elem);
						}

						//String[] appo=  selectedDatiIntest;
						int i= (radioPartecipante.length) -1;
						// ciclo dall'ultimo codice selezionato
						while (i>=0)
						{
							int elem = radioPartecipante[i];
							// il valore del num riga è superiore di una unità rispetto all'indice dell'array
							lista.remove(elem);
							i=i-1;
						}

						//this.renumera(lista);
						PartecipantiGaraVO[] lista_fin =new PartecipantiGaraVO [lista.size()];

						for (int r=0;  r < lista.size(); r++)
						{
							lista_fin [r] = (PartecipantiGaraVO)lista.get(r);
							//lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
						}

						insGare.set("elencaRighePartecipanti",lista_fin);
						insGare.set("radioPartecipante", null) ;
						insGare.set("numRighePartecipanti",lista_fin.length);
						// ricarica la fattura completamente
						GaraVO appoGara=(GaraVO)insGare.get("richOff");
						List<PartecipantiGaraVO> elencaRigheGaraArr=new ArrayList();
						for (int j=0; j<lista_fin.length; j++)
						{
							elencaRigheGaraArr.add(lista_fin[j]);
						}
						appoGara.setDettaglioPartecipantiGara(elencaRigheGaraArr);
						insGare.set("richOff",appoGara);

					}
				}
				insGare.set("radioPartecipante",0);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			//InserisciGaraForm insGare = (InserisciGaraForm) form;
			DynaValidatorForm insGare = (DynaValidatorForm) form;
				try {
					PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
					GaraVO eleGara=(GaraVO) insGare.get("richOff");
					eleGara.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

					String amount=eleGara.getPrezzoIndGaraStr().trim();
				    Double risult=Pulisci.ControllaImporto(amount);
				    eleGara.setPrezzoIndGara(risult);
				    eleGara.setPrezzoIndGaraStr(Pulisci.VisualizzaImporto(eleGara.getPrezzoIndGara()));

					// trasformo in arraylist
					List lista =new ArrayList();
					for (int w=0;  w < elencaRigheGara.length; w++)
					{
						PartecipantiGaraVO elem = elencaRigheGara[w];
						// gestione valorizzazione campi note
						if (elencaRigheGara[w].getNoteAlFornitore()!=null && elencaRigheGara[w].getNoteAlFornitore().trim().length()>0)
						{
							elencaRigheGara[w].setEsisteNota(true);
						}
						if (elencaRigheGara[w].getNoteAlFornitore()!=null && elencaRigheGara[w].getMsgRispDaFornAGara().trim().length()>0)
						{
							elencaRigheGara[w].setEsisteRisp(true);
						}

						lista.add(elem);
					}

					eleGara.setDettaglioPartecipantiGara(lista);
					insGare.set("richOff",eleGara);

				boolean valRitorno = false;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				factory.getGestioneAcquisizioni().ValidaGaraVO(eleGara);
				// fine validazione

				boolean vincitore=false;
				// deve esserci un unico vincitore (nella validazione si controllo che non ce ne sia più di uno)
	 			int eleVinc=999; // valore default
				for (int j=0; j<eleGara.getDettaglioPartecipantiGara().size(); j++)
				{
					PartecipantiGaraVO dett= eleGara.getDettaglioPartecipantiGara().get(j);
					String rigaStato=dett.getStatoPartecipante();
					if (rigaStato.equals("V"))
					{
						eleVinc=j;
						vincitore=true;
						break;
					}
				}
				if (vincitore)
				{
					// ciclo for per impostare i perdenti
					for (int x=0; x<eleGara.getDettaglioPartecipantiGara().size() ; x++)
					{
						if (x!=eleVinc)
						{
							PartecipantiGaraVO dett= eleGara.getDettaglioPartecipantiGara().get(x);
							dett.setStatoPartecipante("P");
						}
					}
					eleGara.setStatoRicOfferta("2");
					insGare.set("richOff",eleGara);
				}

				ActionMessages errors = new ActionMessages();
				((InserisciGaraForm) insGare).setConferma(true);
				((InserisciGaraForm) insGare).setPressioneBottone("salva");
				((InserisciGaraForm) insGare).setDisabilitaTutto(true);
				errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
				this.saveErrors(request, errors);
				//this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				((InserisciGaraForm) insGare).setConferma(false);
				((InserisciGaraForm) insGare).setPressioneBottone("");
				((InserisciGaraForm) insGare).setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);

				return mapping.getInputForward();

			} catch (Exception e) {
				((InserisciGaraForm) insGare).setConferma(false);
				((InserisciGaraForm) insGare).setPressioneBottone("");
				((InserisciGaraForm) insGare).setDisabilitaTutto(false);
				return mapping.getInputForward();
			}
		}

		public ActionForward fornitore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			DynaValidatorForm insGare = (DynaValidatorForm) form;

			try {
				// CONTROLLO CHECK DI RIGA
				Integer[] radioPartecipante=(Integer[])insGare.get("radioPartecipante");
				if (radioPartecipante.length!=0)
				{
						int indiceRigaIns= radioPartecipante[0]; // unico check
				}
/*				else
				{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.buoniOrdine.crea"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}
*/
				// aggiorno le righe eventuali
				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
				GaraVO eleGara=(GaraVO) insGare.get("richOff");
				// trasformo in arraylist
				List lista =new ArrayList();
				for (int w=0;  w < elencaRigheGara.length; w++)
				{
					PartecipantiGaraVO elem = elencaRigheGara[w];
					lista.add(elem);
				}
				eleGara.setDettaglioPartecipantiGara(lista);
				insGare.set("richOff",eleGara);


				// impongo il profilo,  il paese
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

/*				if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
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

				ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
				// carica i criteri di ricerca da passare alla esamina
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=eleGara.getCodBibl();
				String codForn="";
				String nomeForn="";
				String codProfAcq="";
				String paeseForn="";
				String tipoPForn="";
				String provForn="";
				String loc="0"; // I FORNITORI DEVONO ESSERE DI BIBLIOTECA
				String chiama=mapping.getPath();
				//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
				eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
				//ricerca.add(eleRicerca);
				eleRicerca.setModalitaSif(false);
				request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

				return mapping.findForward("fornitori");

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward documentiCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGareNorm = (InserisciGaraForm) form;
			DynaValidatorForm insGare = (DynaValidatorForm) form;
			try {
				GaraVO eleGara=(GaraVO) insGare.get("richOff");
				this.impostaDocumentiCerca(request,mapping, eleGara);
				return mapping.findForward("docum");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		private void impostaDocumentiCerca( HttpServletRequest request, ActionMapping mapping, GaraVO garaPass)
		{
		//impostazione di una lista di supporto
		try {

			ListaSuppDocumentoVO eleRicerca=new ListaSuppDocumentoVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
			//String codP=utenteCollegato.getCodPolo();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			//String codB=utenteCollegato.getCodBib();
			String codB=garaPass.getCodBibl();
			String codDoc="";
			String statoSuggDoc="A"; // solo accettati
			StrutturaTerna ute=new StrutturaTerna("","","");
			//ute.setCodice1(ricDocumenti.getCodUtenteBibl());
			//ute.setCodice2(ricDocumenti.getCodUtenteProg());
			StrutturaCombo tit=new StrutturaCombo("","");
			//tit.setCodice(eleOrdineAttivo.getTitolo().getCodice());
			//tit.setDescrizione(eleOrdineAttivo.getTitolo().getDescrizione());
			String dataDa="";
			String dataA="";
			String edi="";
			String luogo="";
			StrutturaCombo pae=new StrutturaCombo("","");
			StrutturaCombo lin=new StrutturaCombo("","");
			String annoEdi="";
			String chiama=mapping.getPath();
			String ordina="";
			eleRicerca=new ListaSuppDocumentoVO( codP,  codB,  codDoc,  statoSuggDoc,  ute,  tit,  dataDa,  dataA,   edi,  luogo,  pae,  lin,  annoEdi,    chiama,  ordina );
			eleRicerca.setTicket(ticket);
			eleRicerca.setModalitaSif(false);
			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppDocumentoVO", eleRicerca);

		}catch (Exception e) {	}
		}

		public ActionForward suggerimentiCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGareNorm = (InserisciGaraForm) form;
			DynaValidatorForm insGare = (DynaValidatorForm) form;
			try {
				GaraVO eleGara=(GaraVO) insGare.get("richOff");
				this.impostaSuggerimentiCerca(request,mapping, eleGara);
				return mapping.findForward("suggBibl");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		private void impostaSuggerimentiCerca( HttpServletRequest request, ActionMapping mapping,  GaraVO garaPass)
		{
		//impostazione di una lista di supporto
		try {

			//OrdiniVO eleOrdineAttivo= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
			ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			UserVO utenteCollegato = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);

			//String polo=utenteCollegato.getCodPolo();
			String polo="";
			//String bibl=utenteCollegato.getCodBib();
			String bibl=garaPass.getCodBibl();
			String codSugg="";
			String statoSugg="A"; // precisare lo stato solo ACCETTATI
			String dataSuggDa="";
			String dataSuggA="";
			StrutturaCombo titSugg=new StrutturaCombo("","");
			//titSugg.setCodice(eleOrdineAttivo.getTitolo().getCodice());
			//titSugg.setDescrizione(eleOrdineAttivo.getTitolo().getDescrizione());
			StrutturaCombo biblSugg=new StrutturaCombo("","");
			//biblSugg.setCodice(ricSuggerimenti.getCodBibliotec());
			StrutturaCombo sezSugg=new StrutturaCombo("","");
			String chiama=mapping.getPath();
			String ordina="";
			eleRicerca=new ListaSuppSuggerimentoVO( polo,  bibl,  codSugg,  statoSugg, dataSuggDa, dataSuggA,  titSugg, biblSugg,  sezSugg,   chiama,  ordina  );
			eleRicerca.setModalitaSif(false);
			eleRicerca.setTicket(ticket);
			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", eleRicerca);

		}catch (Exception e) {	}
		}


		public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGare = (InserisciGaraForm) form;

			try {

				GaraVO eleGara=(GaraVO) insGare.get("richOff");
				if ( eleGara!=null && eleGara.getCodRicOfferta()!=null &&  eleGara.getCodRicOfferta().trim().length()>0)
				{
					// il record è già esistente
					// lettura
					GaraVO eleGaraLetta=this.loadDatiINS(eleGara);

					insGare.set("richOff",eleGaraLetta);
					int totGare=0;
					if (eleGaraLetta.getDettaglioPartecipantiGara()!=null && eleGaraLetta.getDettaglioPartecipantiGara().size()>0)
					{
						totGare=eleGaraLetta.getDettaglioPartecipantiGara().size();
					}
					insGare.set("numRighePartecipanti",totGare);

					PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[totGare];
					for (int i=0; i<totGare; i++)
					{
						oggettoDettGara[i]=eleGaraLetta.getDettaglioPartecipantiGara().get(i);
						// gestione valorizzazione campi note
						if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getNoteAlFornitore().trim().length()>0)
						{
							oggettoDettGara[i].setEsisteNota(true);
						}
						if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getMsgRispDaFornAGara().trim().length()>0)
						{
							oggettoDettGara[i].setEsisteRisp(true);
						}
					}

					insGare.set("elencaRighePartecipanti",oggettoDettGara);
					if (eleGaraLetta.getStatoRicOfferta().equals("2"))
					{
						// ordine pagamento emesso
						// disabilitazione input
						insGare.setDisabilitaTutto(true);
					}

				}
				else
				{
					GaraVO oggettoGara=this.loadDatiGara();
					if (eleGara!=null )
					{
						if (eleGara.getCodBibl()!=null &&  eleGara.getCodBibl().trim().length()>0)
						{
							oggettoGara.setCodBibl(eleGara.getCodBibl());
						}
						if (eleGara.getCodPolo()!=null &&  eleGara.getCodPolo().trim().length()>0)
						{
							oggettoGara.setCodPolo(eleGara.getCodPolo());
						}
					}
					insGare.set("richOff",oggettoGara);
					insGare.set("numRighePartecipanti",0);
					PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[0];
					insGare.set("elencaRighePartecipanti",oggettoDettGara);
				}




				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insGare = (DynaValidatorForm) form;

			try {

				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) insGare.get("elencaRighePartecipanti");
				GaraVO eleGara=(GaraVO) insGare.get("richOff");
				eleGara.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				String amount=eleGara.getPrezzoIndGaraStr().trim();
			    Double risult=Pulisci.ControllaImporto(amount);
			    eleGara.setPrezzoIndGara(risult);
			    eleGara.setPrezzoIndGaraStr(Pulisci.VisualizzaImporto(eleGara.getPrezzoIndGara()));

				// trasformo in arraylist
				List lista =new ArrayList();
				for (int w=0;  w < elencaRigheGara.length; w++)
				{
					PartecipantiGaraVO elem = elencaRigheGara[w];
					// gestione valorizzazione campi note
					if (elencaRigheGara[w].getNoteAlFornitore()!=null && elencaRigheGara[w].getNoteAlFornitore().trim().length()>0)
					{
						elencaRigheGara[w].setEsisteNota(true);
					}
					if (elencaRigheGara[w].getNoteAlFornitore()!=null && elencaRigheGara[w].getMsgRispDaFornAGara().trim().length()>0)
					{
						elencaRigheGara[w].setEsisteRisp(true);
					}

					lista.add(elem);
				}
				eleGara.setDettaglioPartecipantiGara(lista);
				insGare.set("richOff",eleGara);

				((InserisciGaraForm) insGare).setConferma(false);
				((InserisciGaraForm) insGare).setDisabilitaTutto(false);

				if (((InserisciGaraForm) insGare).getPressioneBottone().equals("salva")) {
					((InserisciGaraForm) insGare).setPressioneBottone("");
					// se il codice ordine è già valorzzato si deve procedere alla modifica
					if (eleGara.getCodRicOfferta()!=null  && eleGara.getCodRicOfferta().length()>0)
					{
						if (!this.modificaGara(eleGara)) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
									"errors.acquisizioni.erroreModifica"));
							this.saveErrors(request, errors);
							//return mapping.getInputForward();
						}
						else
						{
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.modificaOK"));
							this.saveErrors(request, errors);
							return ripristina( mapping,  form,  request,  response);

						}

					}
					else
					{
						if (!this.inserisciGara(eleGara)) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreinserimento"));
							this.saveErrors(request, errors);
							//return mapping.getInputForward();
						}
						else
						{
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.inserimentoOK"));
							this.saveErrors(request, errors);
							return ripristina( mapping,  form,  request,  response);
						}
					}
				}
/*				if (insGare.getPressioneBottone().equal("cancella")) {
					insGare.setPressioneBottone("");
						if (!this.cancellaGara(eleGara)) {
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
									"errors.acquisizioni.erroreCancella"));
							this.saveErrors(request, errors);
						}
						else
						{
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage(
							"errors.acquisizioni.cancellaOK"));
							this.saveErrors(request, errors);
						}
				}			*/
				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				((InserisciGaraForm) insGare).setConferma(false);
				((InserisciGaraForm) insGare).setPressioneBottone("");
				((InserisciGaraForm) insGare).setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			} catch (Exception e) {
				((InserisciGaraForm) insGare).setConferma(false);
				((InserisciGaraForm) insGare).setPressioneBottone("");
	    		((InserisciGaraForm) insGare).setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGare = (InserisciGaraForm) form;

			try {
				// Viene settato il token per le transazioni successive
				this.saveToken(request);
				insGare.setConferma(false);
				insGare.setPressioneBottone("");
	    		insGare.setDisabilitaTutto(false);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}



		public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGare = (InserisciGaraForm) form;

			try {
				ActionMessages errors = new ActionMessages();
				insGare.setConferma(true);
				insGare.setPressioneBottone("cancella");
	    		insGare.setDisabilitaTutto(true);
				errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();
			} catch (Exception e) {
				insGare.setConferma(false);
				insGare.setPressioneBottone("");
	    		insGare.setDisabilitaTutto(false);
				return mapping.getInputForward();
			}
		}


		private GaraVO loadDatiGara() throws Exception {
			// aggiungere data di sistema E IL CODICE DELLA BIBLIOTECA
			// ASSEGNAZIONE DELLA data di sistema
			Date dataodierna=new Date();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String dataOdiernaStr =formato.format(dataodierna);
			// estrapolazione dell'anno
			Calendar c=new GregorianCalendar();
		 	int anno=c.get(Calendar.YEAR);
		 	String annoAttuale="";
		 	annoAttuale=Integer.valueOf(anno).toString();
			//new StrutturaProfiloVO( polo, bibl, prof,  sez,  lin,  pae,  listProf, tipoVar );
			GaraVO richOff=new GaraVO("", "", new StrutturaCombo("", ""), "", dataOdiernaStr,"1", 0, 0, "" );
			richOff.setPrezzoIndGaraStr("0,00");
			PartecipantiGaraVO dettGara = new PartecipantiGaraVO("", "",new StrutturaCombo("", ""), "", "", "", "" , "", "" );
			List lista=new ArrayList();
			//lista.add(eleForn);
			richOff.setDettaglioPartecipantiGara(lista);
			return richOff;

		}

		public ActionForward sifbid(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			InserisciGaraForm insGare = (InserisciGaraForm) form;
			try {
				GaraVO eleGara=(GaraVO) insGare.get("richOff");
				if (eleGara.getBid()!=null &&eleGara.getBid().getCodice()!=null)
				{
					request.setAttribute("bidFromRicOrd", eleGara.getBid().getCodice());
				}
				return mapping.findForward("sifbid");
			} catch (Exception e)
			{
				return mapping.getInputForward();
			}
		}

		public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciGaraForm insGare = (InserisciGaraForm) form;

			try {
				// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
				// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
				ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");

				if (ricArr!=null )
					{
						// gestione del chiamante
						if (ricArr!=null && ricArr.getChiamante()!=null)
						{

							// carico i risultati della selezione nella variabile da restituire
							request.getSession().setAttribute("attributeListaSuppGaraVO", this.AggiornaRisultatiListaSupportoDaIns( insGare, ricArr));
						}

						ActionForward action = new ActionForward();
						action.setName("RITORNA");
						action.setPath(ricArr.getChiamante()+".do");
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
		/**
		 * InserisciGaraAction.java
		 * @param eleRicArr
		 * @return
		 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
		 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
		 */
		private ListaSuppGaraVO AggiornaRisultatiListaSupportoDaIns (InserisciGaraForm insGare, ListaSuppGaraVO eleRicArr)
		{
			try {
				List<GaraVO> risultati=new ArrayList();
				GaraVO eleGara=(GaraVO)insGare.get("richOff");
				risultati.add(eleGara);
				eleRicArr.setSelezioniChiamato(risultati);

			} catch (Exception e) {

			}
			return eleRicArr;
		}


		private boolean inserisciGara(GaraVO gara) throws Exception {
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			valRitorno = factory.getGestioneAcquisizioni().inserisciGara(gara);
			return valRitorno;
		}

		private boolean modificaGara(GaraVO gara) throws Exception {
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			valRitorno = factory.getGestioneAcquisizioni().modificaGara(gara);
			return valRitorno;
		}

		private List loadStatoPartecipante() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("A","A - ATTESA");
			lista.add(elem);
			elem = new StrutturaCombo("P","P - PERDENTE");
			lista.add(elem);
			elem = new StrutturaCombo("V","V - VINCITORE");
			lista.add(elem);
			//this.esaGare.setListaStatoPartecipanteGara(lista);
			return lista;

		}
		private List loadStatiRichiestaOfferta() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("1","Aperta");
			lista.add(elem);
			elem = new StrutturaCombo("2","Chiusa");
			lista.add(elem);
//			elem = new StrutturaCombo("3","Annullata");
//			lista.add(elem);
			elem = new StrutturaCombo("4","Ordinata");
			lista.add(elem);
			//this.esaGare.setListaStatoRichiestaOfferta(lista);
			return lista;

		}

		private List loadTipoInvio() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("F","F - Fax");
			lista.add(elem);
			elem = new StrutturaCombo("P","P - Posta");
			lista.add(elem);
			elem = new StrutturaCombo("S","S - Stampa");
			lista.add(elem);
			//this.esaGare.setListaTipoInvio(lista);
			return lista;
		}

		private GaraVO loadDatiINS(GaraVO ele) throws Exception
		{

			GaraVO eleLetto =null;

			ListaSuppGaraVO eleRicerca=new ListaSuppGaraVO();
			// condizioni di ricerca univoca


			String codP=ele.getCodPolo();
			String codB=ele.getCodBibl();
			StrutturaCombo idtitolo=new StrutturaCombo("","");
			String codRich=ele.getCodRicOfferta();
			String dataRich="";
			String statoRich="";
			String ordina="";
			String chiama=null;

			eleRicerca=new ListaSuppGaraVO( codP,  codB,  idtitolo,  codRich,  dataRich, statoRich,  chiama,  ordina );

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<GaraVO> recordTrovati = new ArrayList();
			try {
				recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaGare(eleRicerca);
			} catch (Exception e) {
		    	e.printStackTrace();
			}
			if (recordTrovati.size()>0)
			{
				eleLetto=recordTrovati.get(0);

				try {
					eleLetto.setPrezzoIndGaraStr(Pulisci.VisualizzaImporto(eleLetto.getPrezzoIndGara()));

				} catch (Exception e) {
				    	//e.printStackTrace();
				    	//throw new ValidationException("importoErrato",
				    	//		ValidationExceptionCodici.importoErrato);
					eleLetto.setPrezzoIndGaraStr("0,00");
				}
			}

			return eleLetto;
		}


}
