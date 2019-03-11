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
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fatture.InserisciFatturaForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

	public class InserisciFatturaAction extends AcquisizioniBaseAction {
		//private InserisciFatturaForm insFatture;
		//private InserisciFatturaForm insFattureNorm;



		protected Map<String, String> getKeyMethodMap() {
			Map<String, String> map = new HashMap<String, String>();

			map.put("ricerca.button.salva","salva");
			map.put("ricerca.button.ripristina","ripristina");
			map.put("ricerca.button.cancella","cancella");
			map.put("ricerca.button.indietro","indietro");
	        map.put("servizi.bottone.si", "Si");
	        map.put("servizi.bottone.no", "No");
			map.put("ricerca.button.insRiga","inserisciRiga");
			map.put("ricerca.button.altreSpese","inserisciRigaAltreSpese");
			map.put("ricerca.button.cancRiga","cancellaRiga");
			map.put("ricerca.button.stampa","stampa");
			map.put("ricerca.button.ordine","ordine");
			map.put("ricerca.label.listaNoteC","listaNote");
			map.put("ordine.label.fornitore","fornitoreCerca");
			map.put("ricerca.button.controllaordine","controllaordine");
			map.put("ricerca.label.listaInv","listaInv");
			map.put("buono.label.tabBilancio","bilancio");
			map.put("ricerca.button.scegli","scegli");
			map.put("ricerca.label.fattura","fatturaCerca");
			map.put("ricerca.button.contabilizzaNotaCredito","contabilizza");
			map.put("ricerca.label.ordinePagamento","ordinePagamento");
			map.put("ricerca.label.listaInv2","listaInvRiga");
			map.put("ricerca.label.bibliolist", "biblioCerca");
			return map;
		}

		public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFattureNorm = (InserisciFatturaForm) form;
			try {
		        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		        UserVO utente = Navigation.getInstance(request).getUtente();
		        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
		        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
		            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_FATTURE, 10, "codBibDalista");
		        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			InserisciFatturaForm insFattureNorm = (InserisciFatturaForm) form;
			try {
				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!insFattureNorm.isSessione())
				{
					loadDefault(request, mapping, form);
					insFattureNorm.setSessione(true);
				}


				// gestione di riga del bottone lente di bilancio
				if (request.getParameter("bottoneBilancio")!=null )
	    	   	{
		    		if (Integer.valueOf(request.getParameter("bottoneBilancio"))>=0 )
		    	   	{
	        			insFattureNorm.setNumRigaBottone(Integer.valueOf(request.getParameter("bottoneBilancio")));
	        			return bilancio( mapping,  form,  request,  response);
		    	   	}
	    	   	}

				// gestione di riga del bottone lente di ordine
				if (request.getParameter("bottoneOrdine")!=null )
	    	   	{
		    		if (Integer.valueOf(request.getParameter("bottoneOrdine"))>=0 )
		    	   	{
	        			insFattureNorm.setNumRigaBottone(Integer.valueOf(request.getParameter("bottoneOrdine")));
	        			return ordine( mapping,  form,  request,  response);
		    	   	}
	    	   	}
				// gestione di riga del bottone lente di fattura
				if (request.getParameter("bottoneFattura")!=null )
	    	   	{
		    		if (Integer.valueOf(request.getParameter("bottoneFattura"))>=0 )
		    	   	{
	        			insFattureNorm.setNumRigaBottone(Integer.valueOf(request.getParameter("bottoneFattura")));
	        			return fatturaCerca( mapping,  form,  request,  response);
		    	   	}
	    	   	}
				// gestione di riga del bottone lente di inventari
				if (request.getParameter("bottoneInventari")!=null )
	    	   	{
		    		if (Integer.valueOf(request.getParameter("bottoneInventari"))>=0 )
		    	   	{
	        			insFattureNorm.setNumRigaBottone(Integer.valueOf(request.getParameter("bottoneInventari")));
	        			return listaInvRiga( mapping,  form,  request,  response);
		    	   	}
	    	   	}

				// gestione note di riga
				/*				if (request.getParameter("paramLink") != null) {
									insFattureNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("paramLink")));
								}
				*/

	    		if (request.getParameter("tagNote")!=null )
	    	   	{

	    			if (insFattureNorm.getClicNotaPrg()!=null && insFattureNorm.getClicNotaPrg().equals(Integer.valueOf(request.getParameter("tagNote"))))
	        	   	{
	        			insFattureNorm.setClicNotaPrg(-1);
	        	   	}
		    		else
		    	   	{
		    			insFattureNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
		    	   	}
	    	   	}


				DynaValidatorForm insFatture = (DynaValidatorForm) form;

				// gestione fattura in caso di caricamento successivo
				FatturaVO oggettoFatt=this.loadDatiFattura();

				insFattureNorm.setDatiFatturaOld((FatturaVO)oggettoFatt.clone());


/*
				FatturaVO oggettoFatt=new FatturaVO();
				Boolean isCaricamentoIniziale= (Boolean)insFatture.get("caricamentoIniziale");
				if (!isCaricamentoIniziale)
				{
	    			oggettoFatt=this.loadDatiFattura();
					insFatture.set("caricamentoIniziale",true);
				}
				else	// reimposta la fattura con i valori immessi
	    		{
		    		if (insFatture.get("datiFattura")!=null)
					{
						FatturaVO oggettoTemp=(FatturaVO) insFatture.get("datiFattura");
						if (oggettoTemp.getFornitoreFattura()!=null || oggettoTemp.getCodBibl()!=null || oggettoTemp.getAnnoFattura()!=null  || oggettoTemp.getProgrFattura()!=null )
						{
							oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
						}
					}

					// carico eventuali righe inserite
					if (insFatture.get("elencaRigheFatt")!=null )
					{
						StrutturaFatturaVO[] elencaRigheFatt0= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
						// trasformo in arraylist
						List listaRig =new ArrayList();
						for (int w=0;  w < elencaRigheFatt0.length; w++)
						{
							StrutturaFatturaVO elem = elencaRigheFatt0[w];
							listaRig.add(elem);
						}
						oggettoFatt.setRigheDettaglioFattura(listaRig);
						insFatture.set("datiFattura",oggettoFatt);
					}
				}
*/


				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();




				// preimpostazione della schermata di inserimento con i valori ricercati

				if (request.getSession().getAttribute("ATTRIBUTEListaSuppFatturaVO")!=null)
				{
					ListaSuppFatturaVO ele=(ListaSuppFatturaVO) request.getSession().getAttribute("ATTRIBUTEListaSuppFatturaVO");
					request.getSession().removeAttribute("ATTRIBUTEListaSuppFatturaVO");
					if (ele.getTipoFattura()!=null && ele.getTipoFattura().trim().length()>0 )
					{
						oggettoFatt.setTipoFattura(ele.getTipoFattura());
					}
					if (ele.getFornitore()!=null )
					{
						oggettoFatt.setFornitoreFattura(ele.getFornitore());;
					}
					// MANCA GESTIONE DI RIGA ORDINE
				}



				// reimposta la fattura con i valori immessi
				if (insFatture.get("datiFattura")!=null )
				{
/*					ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
					// GESTIONE DEL RICHIAMO DI LISTA SUPPORTO FATTURA DA FATTURA
					if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().equals(mapping.getPath()))
		 			{
						oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
		 			}*/

					FatturaVO oggettoTemp=(FatturaVO) insFatture.get("datiFattura");

					if (oggettoTemp.getFornitoreFattura()!=null || oggettoTemp.getCodBibl()!=null || oggettoTemp.getAnnoFattura()!=null  || oggettoTemp.getProgrFattura()!=null )
					{
						oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
					}
				}
				insFatture.set("datiFattura",oggettoFatt);


				// gestione aggiornamento dinamico del cambio al modificarsi della valuta
				if (insFatture.get("valuta")!=null && !insFatture.get("valuta").equals("") )
				{
	    			String valuta=(String) insFatture.get("valuta");
	    			String[] valuta_composto=valuta.split("\\|");
					// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
					String val_primaParte=valuta_composto[0];
					String val_secondaParte=valuta_composto[1].trim();
					if (val_primaParte!=null && val_primaParte.trim().length()==3 && val_secondaParte!=null && val_secondaParte.trim().length()>0)
					{
						oggettoFatt.setCambioFattura(Double.valueOf(val_secondaParte));
		    			oggettoFatt.setValutaFattura(val_primaParte);
					}
				}
				else
				{
					insFatture.set("valuta",oggettoFatt.getValutaFattura()+ "|" + oggettoFatt.getCambioFattura());
				}


				// carico eventuali righe inserite
				StrutturaFatturaVO[] elencaRigheFatt0= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				// trasformo in arraylist
				List listaRig =new ArrayList();
				for (int w=0;  w < elencaRigheFatt0.length; w++)
				{
					StrutturaFatturaVO elem = elencaRigheFatt0[w];
					listaRig.add(elem);
				}
				oggettoFatt.setRigheDettaglioFattura(listaRig);
				insFatture.set("datiFattura",oggettoFatt);

				// fine reimposta
				if (biblio!=null && oggettoFatt!=null &&  (oggettoFatt.getCodBibl()==null || (oggettoFatt.getCodBibl()!=null && oggettoFatt.getCodBibl().trim().length()==0)))
				{
					oggettoFatt.setCodBibl(biblio);
				}
				oggettoFatt.setTicket(ticket);
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				oggettoFatt.setCodPolo(polo);
				// lettura configurazione
				ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
				configurazioneCriteri.setCodBibl(oggettoFatt.getCodBibl());
				configurazioneCriteri.setCodPolo(oggettoFatt.getCodPolo());
				ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
				Boolean gestBil =true;
				if (configurazioneLetta!=null && !configurazioneLetta.isGestioneBilancio())
				{
					gestBil =configurazioneLetta.isGestioneBilancio();
				}

				if (!gestBil)
				{
					((InserisciFatturaForm) insFatture).setGestBil(false);
					oggettoFatt.setGestBil(false);
				}

				//oggettoFatt.getRigheDettaglioFattura().get(0).setCodBibl(biblio);
				//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
				ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
					{
						if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
						{
							oggettoFatt.getFornitoreFattura().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
							oggettoFatt.getFornitoreFattura().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
							insFatture.set("datiFattura",oggettoFatt);

						}
					}
	/*				else
					{
						// pulizia della maschera di ricerca
						this.ricBuoni.setCodFornitore("");
						this.ricBuoni.setFornitore("");
					}
	*/
					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
					request.getSession().removeAttribute("fornitoriSelected");
					request.getSession().removeAttribute("criteriRicercaFornitore");

	 			}
				List arrListaIva=this.loadIVA();
				insFatture.set("listaIva",arrListaIva);

				List arrListaTipoImpegno=this.loadTipoImpegno();
				insFatture.set("listaTipoImpegno",arrListaTipoImpegno);

				List arrListaTipoOrdine=this.loadTipoOrdine();
				insFatture.set("listaTipoOrdine",arrListaTipoOrdine);

				List arrListaTipoFatt=this.loadTipoFatt();
				insFatture.set("listaTipoFatt",arrListaTipoFatt);

				List arrListaStatoFatt=this.loadStatoFatt();
				insFatture.set("listaStatoFatt",arrListaStatoFatt);

				//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
				ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
					{
						if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
						{
							//completo la riga della fattura con le informazioni dell'ordine scelto
							Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
							StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
							if (radioRFatt.length!=0)
							{
									int indiceRigaIns= radioRFatt[0]; // unico check
									// test su riga di altre spese (inibito ordine e tipo impegno di bilancio)
									if (!elencaRigheFatt[indiceRigaIns].getCodPolo().equals("*"))
									{
										String tipo=ricOrd.getSelezioniChiamato().get(0).getTipoOrdine();
										String anno=ricOrd.getSelezioniChiamato().get(0).getAnnoOrdine();
										String codice=ricOrd.getSelezioniChiamato().get(0).getCodOrdine();
										String esercizio=ricOrd.getSelezioniChiamato().get(0).getBilancio().getCodice1();
										String capitolo=ricOrd.getSelezioniChiamato().get(0).getBilancio().getCodice2();
										String impegno=ricOrd.getSelezioniChiamato().get(0).getBilancio().getCodice3();
										StrutturaCombo fornit=new StrutturaCombo("","");
										// per la gestione del richiamo delle righe di inventario vengono memorizzate le informazioni di bid e desc
										if (ricOrd.getSelezioniChiamato().get(0).getTitolo()!=null && ricOrd.getSelezioniChiamato().get(0).getTitolo().getDescrizione()!=null && ricOrd.getSelezioniChiamato().get(0).getTitolo().getDescrizione().trim().length()>0)
										{
											elencaRigheFatt[indiceRigaIns].setTitOrdine(ricOrd.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
										}
										if (ricOrd.getSelezioniChiamato().get(0).getTitolo()!=null && ricOrd.getSelezioniChiamato().get(0).getTitolo().getCodice()!=null && ricOrd.getSelezioniChiamato().get(0).getTitolo().getCodice().trim().length()>0)
										{
											elencaRigheFatt[indiceRigaIns].setTitOrdine(ricOrd.getSelezioniChiamato().get(0).getTitolo().getCodice());
										}
										// fine gestione del richiamo delle righe di inventario

										if (ricOrd.getSelezioniChiamato().get(0).getFornitore()!=null)
										{
											fornit=ricOrd.getSelezioniChiamato().get(0).getFornitore();
										}

										if (!tipo.equals("") && !anno.equals("") && !codice.equals(""))
										{
											elencaRigheFatt[indiceRigaIns].getOrdine().setCodice1(tipo);
											elencaRigheFatt[indiceRigaIns].getOrdine().setCodice2(anno);
											elencaRigheFatt[indiceRigaIns].getOrdine().setCodice3(codice);
										}
										if (!esercizio.equals("") && !capitolo.equals("") ) //&& !impegno.equal("")
		 								{
											elencaRigheFatt[indiceRigaIns].getBilancio().setCodice1(esercizio);
											elencaRigheFatt[indiceRigaIns].getBilancio().setCodice2(capitolo);
											elencaRigheFatt[indiceRigaIns].getBilancio().setCodice3(impegno);
										}

										insFatture.set("elencaRigheFatt",elencaRigheFatt );
										// ricarica la fattura completamente
										FatturaVO datiFattAppo=(FatturaVO)insFatture.get("datiFattura");
										List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
										for (int m=0; m<elencaRigheFatt.length; m++)
										{
											elencaRigheFattArr.add(elencaRigheFatt[m]);
										}
										datiFattAppo.setRigheDettaglioFattura(elencaRigheFattArr);

										if (datiFattAppo.getFornitoreFattura()==null || (datiFattAppo.getFornitoreFattura()!=null && datiFattAppo.getFornitoreFattura().getCodice()!=null && datiFattAppo.getFornitoreFattura().getCodice().trim().length()==0 ))
										{
											datiFattAppo.setFornitoreFattura(fornit);
										}
										insFatture.set("datiFattura",datiFattAppo);

									}

							}

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
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
					request.getSession().removeAttribute("ordiniSelected");
					request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

	 			}
				//controllo se ho un risultato di una lista di supporto BILANCIO richiamata da questa pagina (risultato della simulazione)
				ListaSuppBilancioVO ricBil=(ListaSuppBilancioVO)request.getSession().getAttribute("attributeListaSuppBilancioVO");
				if (ricBil!=null && ricBil.getChiamante()!=null && ricBil.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricBil!=null && ricBil.getSelezioniChiamato()!=null && ricBil.getSelezioniChiamato().size()!=0 )
					{
						if (ricBil.getSelezioniChiamato().get(0).getChiave()!=null && ricBil.getSelezioniChiamato().get(0).getChiave().length()!=0 )
						{
							//completo la riga della fattura con le informazioni dell'ordine scelto
							Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
							StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
							if (radioRFatt.length!=0)
							{
									int indiceRigaIns= radioRFatt[0]; // unico check
									String esercizio=ricBil.getSelezioniChiamato().get(0).getEsercizio();
									String capitolo=ricBil.getSelezioniChiamato().get(0).getCapitolo();
									String impegno=ricBil.getSelezioniChiamato().get(0).getDettagliBilancio().get(0).getImpegno();
									if (!esercizio.equals("") && !capitolo.equals("") ) //&& !impegno.equal("")
	 								{
										elencaRigheFatt[indiceRigaIns].getBilancio().setCodice1(esercizio);
										elencaRigheFatt[indiceRigaIns].getBilancio().setCodice2(capitolo);
										// test su riga di altre spese (inibito ordine e tipo impegno di bilancio)
										if (!elencaRigheFatt[indiceRigaIns].getCodPolo().equals("*"))
										{
											elencaRigheFatt[indiceRigaIns].getBilancio().setCodice3(impegno);
										}
										else
										{
											elencaRigheFatt[indiceRigaIns].getBilancio().setCodice3("4");
										}
									}
									insFatture.set("elencaRigheFatt",elencaRigheFatt );
									// ricarica la fattura completamente
									FatturaVO datiFattAppo=(FatturaVO)insFatture.get("datiFattura");
									List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
									for (int m=0; m<elencaRigheFatt.length; m++)
									{
										elencaRigheFattArr.add(elencaRigheFatt[m]);
									}
									datiFattAppo.setRigheDettaglioFattura(elencaRigheFattArr);
									insFatture.set("datiFattura",datiFattAppo);
							}

						}
					}
	/*				else
					{
						// pulizia della maschera di ricerca
						this.ricOrdini.setEsercizio("");
						this.ricOrdini.setCapitolo("");
						//this.ricOrdini.setTipoImpegno("");
					}
	*/

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					request.getSession().removeAttribute("attributeListaSuppBilancioVO");
					request.getSession().removeAttribute("bilanciSelected");
					request.getSession().removeAttribute("criteriRicercaBilancio");

	 			}

/*				// reimposta la fattura con i valori immessi
				if (insFatture.get("datiFattura")!=null )
				{
					FatturaVO oggettoTemp=(FatturaVO) insFatture.get("datiFattura");

					if (oggettoTemp.getFornitoreFattura()!=null && oggettoTemp.getCodBibl()!=null && oggettoTemp.getAnnoFattura()!=null  && oggettoTemp.getProgrFattura()!=null )
					{
						oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
					}
				}
*/
				BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null)
				{
					oggettoFatt.setCodBibl(bibScelta.getCod_bib());
				}

				// ho dovuto spostarlo perchè riveve il parametro della biblioteca
				List arrListaValuta=this.loadValuta(request,oggettoFatt.getCodBibl() );
				insFatture.set("listaValuta",arrListaValuta);

				insFatture.set("datiFattura",oggettoFatt);
				int totFatture=0;
				if (oggettoFatt.getRigheDettaglioFattura()!=null && oggettoFatt.getRigheDettaglioFattura().size()>0)
				{
					totFatture=oggettoFatt.getRigheDettaglioFattura().size();
				}
				//this.insBilanci.setNumImpegni(totImpegni);
				insFatture.set("numRigheFatt",totFatture);

				StrutturaFatturaVO[] oggettoDettFatt=new StrutturaFatturaVO[totFatture];
				for (int i=0; i<totFatture; i++)
				{
					oggettoDettFatt[i]=oggettoFatt.getRigheDettaglioFattura().get(i);
				}
				insFatture.set("elencaRigheFatt", oggettoDettFatt);

				ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
				if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null && !ricArr.getChiamante().equals(mapping.getPath()))
				{
					// imposta visibilità bottone scegli
					// perimpostazione di campi con elemento ricercato e non trovato
					FatturaVO oggFatt= (FatturaVO) insFatture.get("datiFattura");
					oggFatt.setAnnoFattura(ricArr.getAnnoFattura());
					oggFatt.setProgrFattura(ricArr.getProgrFattura());
					insFatture.set("datiFattura", oggFatt);
					insFatture.set("visibilitaIndietroLS",true);
				}




				// GESTIONE DEL RICHIAMO DI LISTA SUPPORTO FATTURA DA FATTURA
				if (ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().equals(mapping.getPath()))
	 			{
					request.getSession().setAttribute("datiFattura", request.getSession().getAttribute("datiFatturadaFATT"));
					request.getSession().setAttribute("elencaRigheFatt", request.getSession().getAttribute("elencaRigheFattdaFATT"));
					request.getSession().setAttribute("radioRFatt", request.getSession().getAttribute("radioRFattdaFATT"));
					request.getSession().setAttribute("numRigheFatt", request.getSession().getAttribute("numRigheFattdaFATT"));

					insFatture.set("radioRFatt",request.getSession().getAttribute("radioRFatt"));
					insFatture.set("elencaRigheFatt",request.getSession().getAttribute("elencaRigheFatt"));
					insFatture.set("datiFattura",request.getSession().getAttribute("datiFattura"));
					insFatture.set("numRigheFatt",request.getSession().getAttribute("numRigheFatt"));



						if (ricArr!=null && ricArr.getSelezioniChiamato()!=null && ricArr.getSelezioniChiamato().size()!=0 )
						{
							if (ricArr.getSelezioniChiamato().get(0).getChiave()!=null && ricArr.getSelezioniChiamato().get(0).getChiave().length()!=0 )
							{
								//completo la riga della fattura con le informazioni dell'ordine scelto
								Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
								StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
								if (radioRFatt.length!=0)
								{
										int indiceRigaIns= radioRFatt[0]; // unico check
										String fatt1=ricArr.getSelezioniChiamato().get(0).getAnnoFattura();
										String fatt2=ricArr.getSelezioniChiamato().get(0).getProgrFattura();
										String fatt3=ricArr.getSelezioniChiamato().get(0).getNumFattura();
										if (!fatt1.equals("") && !fatt2.equals("") ) //&& !impegno.equal("")
		 								{
											elencaRigheFatt[indiceRigaIns].getFattura().setCodice1(fatt1);
											elencaRigheFatt[indiceRigaIns].getFattura().setCodice2(fatt2);
											elencaRigheFatt[indiceRigaIns].getFattura().setCodice3(fatt3);
										}
										insFatture.set("elencaRigheFatt",elencaRigheFatt );
										// ricarica la fattura completamente
										FatturaVO datiFattAppo=(FatturaVO)insFatture.get("datiFattura");
										List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
										for (int m=0; m<elencaRigheFatt.length; m++)
										{
											elencaRigheFattArr.add(elencaRigheFatt[m]);
										}
										datiFattAppo.setRigheDettaglioFattura(elencaRigheFattArr);
										insFatture.set("datiFattura",datiFattAppo);

										int totFattureRighe=0;
										if (datiFattAppo.getRigheDettaglioFattura()!=null && datiFattAppo.getRigheDettaglioFattura().size()>0)
										{
											totFattureRighe=datiFattAppo.getRigheDettaglioFattura().size();
										}
										//this.insBilanci.setNumImpegni(totImpegni);
										insFatture.set("numRigheFatt",totFattureRighe);


								}

							}
						}
		/*				else
						{
							// pulizia della maschera di ricerca
							this.ricOrdini.setEsercizio("");
							this.ricOrdini.setCapitolo("");
							//this.ricOrdini.setTipoImpegno("");
						}
		*/

						// il reset dell'attributo di sessione deve avvenire solo dal chiamante
						//request.getSession().removeAttribute("attributeListaSuppBilancioVO");
						//request.getSession().removeAttribute("bilanciSelected");
						//request.getSession().removeAttribute("criteriRicercaBilancio");

						// AGGIORNAMENTO DELLE VARIABILI DI SESSIONE CON QUELLE SALVATE
						request.getSession().setAttribute("attributeListaSuppFatturaVO", request.getSession().getAttribute("attributeListaSuppFatturaVOdaFATT"));
						request.getSession().setAttribute("fattureSelected", request.getSession().getAttribute("fattureSelecteddaFATT"));
						request.getSession().setAttribute("criteriRicercaFattura", request.getSession().getAttribute("criteriRicercaFatturadaFATT"));

						// il reset dell'attributo di sessione deve avvenire solo dal chiamante

						request.getSession().removeAttribute("attributeListaSuppFatturaVOdaFATT");
						request.getSession().removeAttribute("fattureSelecteddaFATT");
						request.getSession().removeAttribute("criteriRicercaFatturadaFATT");
						request.getSession().removeAttribute("datiFatturadaFATT");
						request.getSession().removeAttribute("elencaRigheFattdaFATT");
						request.getSession().removeAttribute("radioRFattdaFATT");
						request.getSession().removeAttribute("numRigheFattdaFATT");

	 			}

				ListaSuppFatturaVO ricArrLista=(ListaSuppFatturaVO) request.getSession().getAttribute("passaggioListaSuppFatturaVO");
				// GESTIONE DEL RICHIAMO DI LISTA SUPPORTO FATTURA DA FATTURA (solo nel caso di note di credito)
				if (ricArrLista!=null && ricArrLista.getChiamante()!=null && ricArrLista.getChiamante().equals(mapping.getPath()))
	 			{
						if (ricArrLista!=null && ricArrLista.getSelezioniChiamato()!=null && ricArrLista.getSelezioniChiamato().size()!=0 )
						{
							if (ricArrLista.getSelezioniChiamato().get(0).getChiave()!=null && ricArrLista.getSelezioniChiamato().get(0).getChiave().length()!=0 )
							{
								//completo la riga della fattura con le informazioni dell'ordine scelto
								Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
								StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
								if (radioRFatt.length!=0)
								{

										int indiceRigaIns= radioRFatt[0]; // unico check
										//((InserisciFatturaForm) insFatture).getNumRigaBottone();

										String fatt1=ricArrLista.getSelezioniChiamato().get(0).getAnnoFattura();
										String fatt2=ricArrLista.getSelezioniChiamato().get(0).getProgrFattura();
										// il numero non è quello della fattura ma l'indicazione della riga di fattura legata alla nota di credito
										//String fatt3=ricArrLista.getSelezioniChiamato().get(0).getNumFattura();
										String fatt3="";
										int fatt4=0; // per l'id
										if (ricArrLista.getSelezioniChiamato().get(0).getRigheDettaglioFattura()!=null && ricArrLista.getSelezioniChiamato().get(0).getRigheDettaglioFattura().size()>0 )
										{
											for (int t=0;  t < ricArrLista.getSelezioniChiamato().get(0).getRigheDettaglioFattura().size(); t++)
											{
												StrutturaFatturaVO eleFatt=ricArrLista.getSelezioniChiamato().get(0).getRigheDettaglioFattura().get(t);
												if (eleFatt!=null )
												{
													fatt4=ricArrLista.getSelezioniChiamato().get(0).getIDFatt(); // memorizzo l'id della fattura della nota di credito
												}
												if (eleFatt!=null && eleFatt.getFattura()!=null && eleFatt.getFattura().getCodice3()!=null && eleFatt.getFattura().getCodice3().trim().length()>0)
												{
													fatt3=eleFatt.getFattura().getCodice3();
													break;
												}
											}
										}
										if (!fatt1.equals("") && !fatt2.equals("") ) //&& !impegno.equal("")
		 								{
											elencaRigheFatt[indiceRigaIns].getFattura().setCodice1(fatt1);
											elencaRigheFatt[indiceRigaIns].getFattura().setCodice2(fatt2);
											elencaRigheFatt[indiceRigaIns].getFattura().setCodice3(fatt3);
											elencaRigheFatt[indiceRigaIns].setIDFattNC(fatt4); // memorizzo l'id della fattura della nota di credito

		 								}
										insFatture.set("elencaRigheFatt",elencaRigheFatt );
										// ricarica la fattura completamente
										FatturaVO datiFattAppo=(FatturaVO)insFatture.get("datiFattura");
										List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
										for (int m=0; m<elencaRigheFatt.length; m++)
										{
											elencaRigheFattArr.add(elencaRigheFatt[m]);
										}
										datiFattAppo.setRigheDettaglioFattura(elencaRigheFattArr);


										// gestione memorizzazione fornitore ereditato da fattura se non presente
										StrutturaCombo fornit=new StrutturaCombo("","");
										if (ricArrLista.getSelezioniChiamato().get(0).getFornitoreFattura()!=null)
										{
											fornit=ricArrLista.getSelezioniChiamato().get(0).getFornitoreFattura();
										}
										if (datiFattAppo.getFornitoreFattura()==null || (datiFattAppo.getFornitoreFattura()!=null && datiFattAppo.getFornitoreFattura().getCodice()!=null && datiFattAppo.getFornitoreFattura().getCodice().trim().length()==0 ))
										{
											datiFattAppo.setFornitoreFattura(fornit);
										}

										insFatture.set("datiFattura",datiFattAppo);

										int totFattureRighe=0;
										if (datiFattAppo.getRigheDettaglioFattura()!=null && datiFattAppo.getRigheDettaglioFattura().size()>0)
										{
											totFattureRighe=datiFattAppo.getRigheDettaglioFattura().size();
										}
										//this.insBilanci.setNumImpegni(totImpegni);
										insFatture.set("numRigheFatt",totFattureRighe);


								}

							}
						}
		/*				else
						{
							// pulizia della maschera di ricerca
							this.ricOrdini.setEsercizio("");
							this.ricOrdini.setCapitolo("");
							//this.ricOrdini.setTipoImpegno("");
						}
		*/


						// il reset dell'attributo di sessione deve avvenire solo dal chiamante

						request.getSession().removeAttribute("passaggioListaSuppFatturaVO");

	 			}



	/*
				this.totalizzaImporto();
	*/



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

		public ActionForward fatturaCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			// ATTENZIONE C'E' LA RICORSIONE DA FATTURA RICHIAMO FATTURA
			// SALVATAGGIO VECCHIA LISTA SUPPORTO
			try {

				// gestione della pressione del bottone di riga su riga che non ha check
				if (((InserisciFatturaForm) insFatture).getNumRigaBottone()>=0)
				{
					// riga selezionata cambiare la posizione del radio button
					Integer[]  radioRFattBottone=new Integer[1];
					Integer indiceRigaBottone=((InserisciFatturaForm) insFatture).getNumRigaBottone();
					radioRFattBottone[0]=indiceRigaBottone;
					insFatture.set("radioRFatt",radioRFattBottone);
				}

				// CONTROLLO CHECK DI RIGA
				Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					int indiceRigaIns= radioRFatt[0]; // unico check
				}
				else
				{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.buoniOrdine.crea"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}

				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				//Integer[]
				radioRFatt=(Integer[])insFatture.get("radioRFatt");
				request.getSession().setAttribute("datiFatturadaFATT", oggettoFatt);
				request.getSession().setAttribute("elencaRigheFattdaFATT", elencaRigheFatture);
				request.getSession().setAttribute("radioRFattdaFATT", radioRFatt);
				request.getSession().setAttribute("numRigheFattdaFATT", elencaRigheFatture.length);

				if (radioRFatt.length!=0)
				{
					if (elencaRigheFatture.length!=0 )
					{
						for (int q=0; q<elencaRigheFatture.length; q++)
						{
							oggettoFatt.getRigheDettaglioFattura().add(elencaRigheFatture[q]);
						}

						int indiceRigaIns= radioRFatt[0]; // unico check

						// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
						// && request.getSession().getAttribute("attributeListaSuppFatturaVO")==null
/*						if (request.getSession().getAttribute("attributeListaSuppFatturaVO")!=null )
						{
							request.getSession().setAttribute("attributeListaSuppFatturaVOdaFATT", (ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO"));
							request.getSession().setAttribute("fattureSelecteddaFATT", (String[]) request.getSession().getAttribute("fattureSelected"));
							request.getSession().setAttribute("criteriRicercaFatturadaFATT", (List<ListaSuppFatturaVO>) request.getSession().getAttribute("criteriRicercaFattura"));

						}
*/

						ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=oggettoFatt.getCodBibl();
						String annoF=elencaRigheFatture[indiceRigaIns].getFattura().getCodice1().trim();
						//String numF=elencaRigheFatture[indiceRigaIns].getFattura().getCodice3().trim();
						String numF="";
						String staF="";
						String dataDa="";
						String dataA="";
						String prgF=elencaRigheFatture[indiceRigaIns].getFattura().getCodice2().trim();
						String dataF="";
						String dataRegF="";
						String tipF="F"; // DEVE CERCARE FRA LE FATTURE
						StrutturaTerna ordFatt=new StrutturaTerna("","","");
						StrutturaCombo fornFatt=new StrutturaCombo("","");
						fornFatt.setCodice(oggettoFatt.getFornitoreFattura().getCodice());
						StrutturaTerna bilFatt=new StrutturaTerna("","","");
						//if ()
						//{
						//}
						//bilFatt.setCodice1(elencaRigheFatture[indiceRigaIns].getBilancio().getCodice1());
						//bilFatt.setCodice2(elencaRigheFatture[indiceRigaIns].getBilancio().getCodice2());
						//bilFatt.setCodice3(elencaRigheFatture[indiceRigaIns].getBilancio().getCodice3());

						String chiama=mapping.getPath();
						String ordina="";
						eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
						eleRicerca.setModalitaSif(false);
						//
						//ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().equals(mapping.getPath())

						//request.getSession().setAttribute("attributeListaSuppFatturaVO", (ListaSuppFatturaVO) eleRicerca);
						//return mapping.findForward("fatturaCerca");

						request.getSession().setAttribute("passaggioListaSuppFatturaVO", eleRicerca);
						//  ok return mapping.findForward("fatturaCercaLista");
						return mapping.findForward("fatturaRicercaNC");
						//return mapping.findForward("fatturaCercaNC");

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

		public ActionForward listaInvRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			try {
				// || Navigation.getInstance(request).isFromBack()
				if (Navigation.getInstance(request).isFromBar()){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.inventariAssenti"));
					this.saveErrors(request, errors);

			    	return mapping.getInputForward();
			    }

			    // gestione della pressione del bottone di riga su riga che non ha check
				if (((InserisciFatturaForm) insFatture).getNumRigaBottone()>=0)
				{
					// riga selezionata cambiare la posizione del radio button
					Integer[]  radioRFattBottone=new Integer[1];
					Integer indiceRigaBottone=((InserisciFatturaForm) insFatture).getNumRigaBottone();
					radioRFattBottone[0]=indiceRigaBottone;
					insFatture.set("radioRFatt",radioRFattBottone);
				}

			    // CONTROLLO CHECK DI RIGA
				Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					int indiceRigaIns= radioRFatt[0]; // unico check
				}
				else
				{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.buoniOrdine.crea"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}
				// test se salvataggio effettuato
/*			    if (!((InserisciFatturaForm) insFatture).isFatturaSalvata())
			    {
			    	ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
*/

				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");

				// controllo che la fattura sia stata salvata
				if (((InserisciFatturaForm) insFatture).getDatiFatturaOld().equals(oggettoFatt))
				{

					((InserisciFatturaForm) insFatture).setFatturaSalvata(true);
	/*					    if (!((EsaminaFatturaForm) esaFatture).isFatturaSalvata())
				    {
					}
	*/
				}
				else
				{
					((InserisciFatturaForm) insFatture).setFatturaSalvata(false);
			    	ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();

				}




				// test su stato fattura se già pagata e contabilizzata
				String modal="V";
				if (!oggettoFatt.getStatoFattura().equals("3") && !oggettoFatt.getStatoFattura().equals("4"))
				{
					modal="A";
				}
				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				//Integer[]
				radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					if (elencaRigheFatture.length!=0 )
					{
						  int indiceRigaIns= radioRFatt[0]; // unico check

						  String codTipOrd=elencaRigheFatture[indiceRigaIns].getOrdine().getCodice1();
						  if (codTipOrd.equals("R"))
						  {
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage(
										"errors.acquisizioni.inventariOrdineRilegatura"));
								this.saveErrors(request, errors);
				        	  return mapping.getInputForward();
						  }

						  String annoOrd=elencaRigheFatture[indiceRigaIns].getOrdine().getCodice2();
					      String codOrd=elencaRigheFatture[indiceRigaIns].getOrdine().getCodice3();
						  request.setAttribute("codBibO", oggettoFatt.getCodBibl());
				          request.setAttribute("codTipoOrd", codTipOrd );
				          request.setAttribute("annoOrd",annoOrd);
				          request.setAttribute("codOrd", codOrd );
				          request.setAttribute("prov", "rigaFattura");
				          request.setAttribute("tipoOperazione", modal); // oppure visual
				          //request.setAttribute("prezzo",elencaRigheFatture[indiceRigaIns].getImportoRigaFattura());
				          request.setAttribute("prezzo",elencaRigheFatture[indiceRigaIns].getImportoScontatoConIvaDouble());

				          StrutturaCombo titOrdRFatt=new StrutturaCombo("","");
				          if ( elencaRigheFatture[indiceRigaIns].getTitOrdine()!=null)
				          {
				        	  titOrdRFatt.setDescrizione(elencaRigheFatture[indiceRigaIns].getTitOrdine()) ;
				          }
				          if ( elencaRigheFatture[indiceRigaIns].getBidOrdine()!=null)
				          {
				        	  titOrdRFatt.setCodice(elencaRigheFatture[indiceRigaIns].getBidOrdine()) ;
				          }
				          request.setAttribute("titOrd", titOrdRFatt );
				          request.setAttribute("rigaFatt", String.valueOf(elencaRigheFatture[indiceRigaIns].getRigaFattura()));
						  request.setAttribute("codBibF", oggettoFatt.getCodBibl());
				          request.setAttribute("annoF",  oggettoFatt.getAnnoFattura());
				          request.setAttribute("prgF",  oggettoFatt.getProgrFattura() );

				          if (codTipOrd!=null && codTipOrd.trim().length()>0 && annoOrd!=null && annoOrd.trim().length()>0 && codOrd!=null && codOrd.trim().length()>0  )
				          {
				        	return mapping.findForward("sifListeInventari");
				          }
				          else
				          {
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage(
										"errors.acquisizioni.inventariOrdineAssente"));
								this.saveErrors(request, errors);
				        	  return mapping.getInputForward();
				          }
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


		public ActionForward contabilizza(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

//	 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

			try {
				// CONTROLLARE che si passi da controllata e poi CAMBIO STATO in pagata
					FatturaVO oggettoFatt= (FatturaVO) insFatture.get("datiFattura");
					// controllo che la fattura sia stata salvata
					if (((InserisciFatturaForm) insFatture).getDatiFatturaOld().equals(oggettoFatt))
					{

						((InserisciFatturaForm) insFatture).setFatturaSalvata(true);
		/*					    if (!((EsaminaFatturaForm) esaFatture).isFatturaSalvata())
					    {
						}
		*/
					}
					else
					{
						((InserisciFatturaForm) insFatture).setFatturaSalvata(false);
				    	ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();

					}


					if (!oggettoFatt.getStatoFattura().equals("2"))
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaControllata"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					else
					{
						oggettoFatt.setStatoFattura("4"); // posto a pagata
						insFatture.set("datiFattura",oggettoFatt);
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.cambioStatoDaSalvare"));
						this.saveErrors(request, errors);

						((InserisciFatturaForm) insFatture).setPressioneBottone("salva");
						return Si( mapping,  form,  request,  response);

						//return mapping.getInputForward();
					}
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward ordinePagamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			// CONTROLLARE che si passi da controllata e poi CAMBIO STATO in pagata
			try {
				FatturaVO oggettoFatt= (FatturaVO) insFatture.get("datiFattura");
				// controllo che la fattura sia stata salvata
				if (((InserisciFatturaForm) insFatture).getDatiFatturaOld().equals(oggettoFatt))
				{

					((InserisciFatturaForm) insFatture).setFatturaSalvata(true);
	/*					    if (!((EsaminaFatturaForm) esaFatture).isFatturaSalvata())
				    {
					}
	*/
				}
				else
				{
					((InserisciFatturaForm) insFatture).setFatturaSalvata(false);
			    	ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();

				}


				if (!oggettoFatt.getStatoFattura().equals("2"))
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaControllata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				else
				{
					oggettoFatt.setStatoFattura("3"); // posto a pagata
					insFatture.set("datiFattura",oggettoFatt);
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.cambioStatoDaSalvare"));
					this.saveErrors(request, errors);

					((InserisciFatturaForm) insFatture).setPressioneBottone("salva");
					return Si( mapping,  form,  request,  response);

					//return mapping.getInputForward();
				}
			} catch (Exception e) {
				return mapping.getInputForward();
			}


		}

		public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;
			try {
				ActionForward forward = fornitoreCercaVeloce(mapping, request, insFatture);
				if (forward != null){
					return forward;
				}
				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
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
				this.impostaFornitoreCerca(request,mapping, oggettoFatt);
				return mapping.findForward("fornitoreCerca");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		private void impostaFornitoreCerca(HttpServletRequest request, ActionMapping mapping, FatturaVO oggettoFatt )
		{
		//impostazione di una lista di supporto
		try {
			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=oggettoFatt.getCodBibl();
			String codForn=oggettoFatt.getFornitoreFattura().getCodice();
			String nomeForn=oggettoFatt.getFornitoreFattura().getDescrizione();
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


		public ActionForward listaInv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();

			try {

				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");

				// controllo che la fattura sia stata salvata
				if (((InserisciFatturaForm) insFatture).getDatiFatturaOld().equals(oggettoFatt))
				{

					((InserisciFatturaForm) insFatture).setFatturaSalvata(true);
	/*					    if (!((EsaminaFatturaForm) esaFatture).isFatturaSalvata())
				    {
					}
	*/
				}
				else
				{
					((InserisciFatturaForm) insFatture).setFatturaSalvata(false);
			    	ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();

				}


				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				request.setAttribute("prov", "fattura");
				request.setAttribute("codBibF", oggettoFatt.getCodBibl());
				request.setAttribute("annoFatt",oggettoFatt.getAnnoFattura());
				request.setAttribute("prgFatt", oggettoFatt.getProgrFattura());
				return mapping.findForward("sifListeInventari");

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward controllaordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFattureNorm = (InserisciFatturaForm) form;
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			// CONTROLLARE TOT DELLE RIGHE INFERIORE AL TOT FATTURA E CAMBIO STATO in controllata
			try {


				// test se salvataggio effettuato
/*			    if (!((InserisciFatturaForm) insFatture).isFatturaSalvata())
			    {
			    	ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
*/

				FatturaVO oggettoFatt= (FatturaVO) insFatture.get("datiFattura");

				// controllo che la fattura sia stata salvata
				if (((InserisciFatturaForm) insFatture).getDatiFatturaOld().equals(oggettoFatt))
				{

					((InserisciFatturaForm) insFatture).setFatturaSalvata(true);
	/*					    if (!((EsaminaFatturaForm) esaFatture).isFatturaSalvata())
				    {
					}
	*/
				}
				else
				{
					((InserisciFatturaForm) insFatture).setFatturaSalvata(false);
			    	ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaNonSalvata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();

				}


				StrutturaFatturaVO[] oggettoDettFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				int totFatture=oggettoDettFatt.length;

				// CONTROLLO CHE TUTTE LE RIGHE DI FATTURA ABBIANO UN INVENTARIO LEGATO
				String codPolo=oggettoFatt.getCodPolo();
				String codBib=oggettoFatt.getCodBibl();
				int annoFattura=0;
				if (oggettoFatt.getAnnoFattura()!=null && oggettoFatt.getAnnoFattura().trim().length()>0)
				{
					annoFattura=Integer.valueOf(oggettoFatt.getAnnoFattura());
				}
				int progrFattura=0;
				if (oggettoFatt.getProgrFattura()!=null && oggettoFatt.getProgrFattura().trim().length()>0)
				{
					progrFattura=Integer.valueOf(oggettoFatt.getProgrFattura());
				}
				String ticket=Navigation.getInstance(request).getUserTicket();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				List listaInv = null;
				//listaInventariFattura = (DescrittoreBloccoVO) factory.getGestioneDocumentoFisico().getListaInventariFattura( codPolo,  codBib, annoFattura,  progrFattura, ticket,99);
				try
				{
					listaInv = new ArrayList();
					listaInv = this.getListaInventariFattura(codPolo,  codBib, annoFattura,  progrFattura, this.getLocale(request, Constants.SBN_LOCALE), ticket,insFattureNorm.getNRec(),insFattureNorm );
					//(myForm.getCodPolo(), myForm.getCodBibF(),
					//		myForm.getAnnoFattura(), myForm.getProgrFattura(), myForm.getTicket(), myForm.getNRec(), form);

				} catch (Exception e) {
					e.printStackTrace();
				}

				// controllo di presenza esclusiva di soli ordini di rilegatura
				boolean soloOrdiniRilegatura=true;
				for (int p=0; p<totFatture; p++)
				{
					StrutturaFatturaVO elem = oggettoDettFatt[p];
					if (!elem.getOrdine().getCodice1().equals("R"))
					{
						soloOrdiniRilegatura=false;
						break;
					}
				}
				if (!soloOrdiniRilegatura)
				{
					if (listaInv!=null && listaInv.size()>0 )
					{
						// controllo che tutte le righe della fattura siano legate ad un inventario
						boolean tuttiTrovati=true;
						for (int p=0; p<totFatture; p++)
						{
							boolean trovato=false;
							StrutturaFatturaVO elem = oggettoDettFatt[p];
							int numR =elem.getRigaFattura();
							// esame della tipologia dell'ordine se RILEGATURA oppure ORDINE NON PRESENTE va escluso dal controllo
							// elem.getBilancio().getCodice3().equals("4")
							if (!elem.getOrdine().getCodice1().equals("R") && !elem.getOrdine().equals(new StrutturaTerna("", "", "")) && !elem.getBilancio().getCodice3().equals("4") )
							{
								for (int u=0; u<listaInv.size(); u++)
								{
									InventarioListeVO eleInv = (InventarioListeVO) listaInv.get(u);
									if (eleInv.getRigaFattura()!=null && eleInv.getRigaFattura().trim().length()>0)
									{
										int rigaEleInv=Integer.valueOf(eleInv.getRigaFattura());
										if (rigaEleInv==numR)
										{
											trovato=true;
											break;
										}
									}
								} // fine secondo for
							}

							if (!trovato)
							{
								// esame della tipologia dell'ordine se RILEGATURA va escluso dal controllo
								if (!elem.getOrdine().getCodice1().equals("R") && !elem.getOrdine().equals(new StrutturaTerna("", "", "")) && !elem.getBilancio().getCodice3().equals("4"))
								{
									tuttiTrovati=false;
									break;
								}
							}

						} // fine primo for
						if (!tuttiTrovati)
						{
							// Controllo fattura impossibile: non tutte le righe sono legate ad un inventario
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.acquisizioni.controlloImpossibile"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
					}
					else
					{
						// Controllo fattura impossibile: non tutte le righe sono legate ad un inventario
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni.controlloImpossibileNoInv"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}

				// aggiungere tutto il resto
				String amountFattImp=oggettoFatt.getImportoFatturaStr().trim();
			    Double risultFattImp=Pulisci.ControllaImporto(amountFattImp);
			    oggettoFatt.setImportoFattura(risultFattImp);
			    oggettoFatt.setImportoFatturaStr(Pulisci.VisualizzaImporto(oggettoFatt.getImportoFattura()));

				String amountFattSc1=oggettoFatt.getScontoFatturaStr().trim();
			    Double risultFattSc1=Pulisci.ControllaImporto(amountFattSc1);
			    oggettoFatt.setScontoFattura(risultFattSc1);
			    oggettoFatt.setScontoFatturaStr(Pulisci.VisualizzaImporto(oggettoFatt.getScontoFattura()));


				double totImportoFattura=0;
				if (oggettoFatt.getImportoFattura()>0)
				{
					totImportoFattura=oggettoFatt.getImportoFattura();
				}
				double totImportoRighe=0;

				List lista =new ArrayList();
				for (int t=0; t<totFatture; t++)
				{
					StrutturaFatturaVO elem = oggettoDettFatt[t];
					String amountDettFattImp=elem.getImportoRigaFatturaStr().trim();
				    Double risultDettFattImp=Pulisci.ControllaImporto(amountDettFattImp);
				    elem.setImportoRigaFattura(risultDettFattImp);
				    elem.setImportoRigaFatturaStr(Pulisci.VisualizzaImporto(elem.getImportoRigaFattura()));

					String amountDettFattSc1=elem.getSconto1RigaFatturaStr().trim();
				    Double risultDettFattSc1=Pulisci.ControllaImporto(amountDettFattSc1);
				    elem.setSconto1RigaFattura(risultDettFattSc1);
				    elem.setSconto1RigaFatturaStr(Pulisci.VisualizzaImporto(elem.getSconto1RigaFattura()));

					String amountDettFattSc2=elem.getSconto2RigaFatturaStr().trim();
				    Double risultDettFattSc2=Pulisci.ControllaImporto(amountDettFattSc2);
				    elem.setSconto2RigaFattura(risultDettFattSc2);
				    elem.setSconto2RigaFatturaStr(Pulisci.VisualizzaImporto(elem.getSconto2RigaFattura()));

				    lista.add(elem);
					//totImportoRighe= totImportoRighe + elem.getImportoRigaFattura();
				    // tenendo conto degli sconti
				    double importo=0;
				    double sconto1=0;
				    double sconto2=0;
				    double sconto3=0;
				    double iva=0;


				    double importo1=0; //(importo scontato 1)
				    double importo2=0; //(importo scontato 2)
				    double importo3=0; //(importo scontato 3) sconto di fattura
				    double importo4=0; //(importo con aggiunta dell'iva)

				    // adeguare i controlli

				    importo=elem.getImportoRigaFattura();
				    sconto1=elem.getImportoRigaFattura()* elem.getSconto1RigaFattura()/100;
				    importo1=elem.getImportoRigaFattura() - sconto1;
				    sconto2=importo1*elem.getSconto2RigaFattura()/100;
					importo2=importo1 - sconto2;
				    sconto3=importo2*oggettoFatt.getScontoFattura()/100;
					importo3=importo2 - sconto3;
					// calcolo dell'iva che va aggiunto
					iva=importo3*Double.valueOf(elem.getCodIvaRigaFattura().trim())/100;
					importo4=importo3 + iva;


				    totImportoRighe= totImportoRighe + importo4;

				}
				String totImportoRigheStr=Pulisci.VisualizzaImporto(totImportoRighe);
			    totImportoRighe=Pulisci.ControllaImporto(totImportoRigheStr);

				if (totImportoRighe!=totImportoFattura || totImportoFattura==0)
				{
					ActionMessages errors = new ActionMessages();
					//errors.add("generico", new ActionMessage("errors.acquisizioni.erroreFatturaErrata"));
					errors.add("generico", new ActionMessage("errors.acquisizioni.erroreFatturaErrataDiverso", totImportoRighe, null));
					this.saveErrors(request, errors);
					oggettoFatt.setStatoFattura("1"); // posto a registrata
					return mapping.getInputForward();


				}
				else
				{
					// ulteriore controllo di conti

					// controllo positivo
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.fatturaControllo"));
					this.saveErrors(request, errors);
				}

				oggettoFatt.setStatoFattura("2"); // posto a controllata
				oggettoFatt.setRigheDettaglioFattura(lista);
				insFatture.set("datiFattura",oggettoFatt);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.cambioStatoDaSalvare"));
				this.saveErrors(request, errors);

				((InserisciFatturaForm) insFatture).setPressioneBottone("salva");
				return Si( mapping,  form,  request,  response);


				//return mapping.getInputForward();
//

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}



		public ActionForward listaNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			try {
				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
				ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
				// carica i criteri di ricerca da passare alla esamina
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=oggettoFatt.getCodBibl();
				String annoF="";
				String numF="";
				String staF="";
				String dataDa="";
				String dataA="";
				String prgF="";
				String dataF="";
				String dataRegF="";
				String tipF="N"; // DEVE CERCARE FRA LE NOTE DI CREDITO
				StrutturaTerna ordFatt=new StrutturaTerna("","","");
				StrutturaCombo fornFatt=new StrutturaCombo("","");
				//fornFatt.setCodice(oggettoFatt.getFornitoreFattura().getCodice());
				StrutturaTerna bilFatt=new StrutturaTerna("","","");
				String chiama=mapping.getPath();
				String ordina="";
				eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
				eleRicerca.setIDFattNC(oggettoFatt.getIDFatt());
				//
				//ricArr!=null && ricArr.getChiamante()!=null && ricArr.getChiamante().equals(mapping.getPath())

				//request.getSession().setAttribute("attributeListaSuppFatturaVO", (ListaSuppFatturaVO) eleRicerca);
				//return mapping.findForward("fatturaCerca");

				request.getSession().setAttribute("passaggioListaSuppFatturaVO", eleRicerca);
				return mapping.findForward("fatturaCercaLista");
				//return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;

			try {
				ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
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

		public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			try {
				//FatturaVO appoFatt=(FatturaVO)insFatture.get("datiFattura");
				//List<StrutturaFatturaVO> elencaRigheFattArr=appoFatt.getRigheDettaglioFattura();

				StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
/*				for (int q=0; q<elencaRigheFatt.length; q++)
				{
					appoFatt.getRigheDettaglioFattura().add(elencaRigheFatt[q]);
				}
*/
/*				int totRigFatt=appoFatt.getRigheDettaglioFattura().size();
				StrutturaFatturaVO[] elencaRigheFatt=new StrutturaFatturaVO[totRigFatt];

				for (int i=0; i<totRigFatt; i++)
				{
					elencaRigheFatt[i]=appoFatt.getRigheDettaglioFattura().get(i);
				}
				insFatture.set("elencaRigheFatt",elencaRigheFatt);
*/
				//StrutturaFatturaVO[]	elencaRigheFatt=(StrutturaFatturaVO[]) esaFatture.get("elencaRigheFatt");
				StrutturaFatturaVO dettFatt = new StrutturaFatturaVO("", "", new StrutturaTerna("", "", ""),1,"",0.00, 0.00, 0.00, "00",new StrutturaTerna("", "", ""), new StrutturaTerna("", "", ""));
				dettFatt.setImportoRigaFatturaStr("0,00");
				dettFatt.setSconto1RigaFatturaStr("0,00");
				dettFatt.setSconto2RigaFatturaStr("0,00");

				if (elencaRigheFatt!=null &&  elencaRigheFatt.length!=0)
				{
					StrutturaFatturaVO[] listaDettFatt=new StrutturaFatturaVO[elencaRigheFatt.length +1];

					for (int i=0; i<elencaRigheFatt.length; i++)
					{
						listaDettFatt[i]=elencaRigheFatt[i];
					}
					// imposta il progressivo della riga
					dettFatt.setProgrRigaFattura(elencaRigheFatt.length+1);
					// imposta numero di riga ()
					dettFatt.setRigaFattura(elencaRigheFatt.length+1);
					if (elencaRigheFatt[elencaRigheFatt.length-1].getRigaFattura()>elencaRigheFatt.length)
					{
						dettFatt.setRigaFattura(elencaRigheFatt[elencaRigheFatt.length-1].getRigaFattura()+1);
					}

					// automatismo di copia dei dati (ordine e bilancio) della riga precedente
					dettFatt.setOrdine((StrutturaTerna)elencaRigheFatt[elencaRigheFatt.length-1].getOrdine().clone());
					dettFatt.setBilancio((StrutturaTerna)elencaRigheFatt[elencaRigheFatt.length-1].getBilancio().clone());
					// fine automatismo

					listaDettFatt[elencaRigheFatt.length]=dettFatt;
					insFatture.set("elencaRigheFatt",listaDettFatt);
					insFatture.set("numRigheFatt",listaDettFatt.length);
					// ricarica la fattura completamente
					List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
					for (int i=0; i<listaDettFatt.length; i++)
					{
						elencaRigheFattArr.add(listaDettFatt[i]);
					}
					// ricarica la fattura completamente
					FatturaVO appoFatt=(FatturaVO)insFatture.get("datiFattura");
					appoFatt.setRigheDettaglioFattura(elencaRigheFattArr);
					insFatture.set("datiFattura",appoFatt);
					// gestione selezione della riga in inserimento
					Integer[]  radioRFatt=new Integer[1];
					if (appoFatt!=null && appoFatt.getRigheDettaglioFattura().size()>0)
					{
						radioRFatt[0]=appoFatt.getRigheDettaglioFattura().size() -1;
						insFatture.set("radioRFatt",radioRFatt);
					}
				}
				else
				{
					elencaRigheFatt=new StrutturaFatturaVO[1];
					elencaRigheFatt[0]=dettFatt;
					insFatture.set("elencaRigheFatt",elencaRigheFatt);
					insFatture.set("numRigheFatt",elencaRigheFatt.length);
					// ricarica la fattura completamente
					FatturaVO appoFatt=(FatturaVO)insFatture.get("datiFattura");
					List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
					for (int i=0; i<elencaRigheFatt.length; i++)
					{
						elencaRigheFattArr.add(elencaRigheFatt[i]);
					}
					appoFatt.setRigheDettaglioFattura(elencaRigheFattArr);
					insFatture.set("datiFattura", appoFatt);
					// gestione selezione della riga in inserimento
					Integer[]  radioRFatt=new Integer[1];
					if (appoFatt!=null && appoFatt.getRigheDettaglioFattura().size()>0)
						if (appoFatt.getRigheDettaglioFattura().size() > 1)	{
							radioRFatt[0]=appoFatt.getRigheDettaglioFattura().size() -1;
						}else{
							radioRFatt[0]=appoFatt.getRigheDettaglioFattura().size();
						}
					insFatture.set("radioRFatt",radioRFatt);
				}
				// trasformazione in arraylist
				// gestione selezione oggetto presente in campo di input di partenza

				//if (ricArr!=null && ricArr.getCodiceSezione()!=null && ricArr.getCodiceSezione().trim().length()>0)
				//{

				//}
			    ((InserisciFatturaForm) insFatture).setFatturaSalvata(false);

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward inserisciRigaAltreSpese(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			try {

				StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				StrutturaFatturaVO dettFatt = new StrutturaFatturaVO("*", "", new StrutturaTerna("", "", ""),1,"",0.00, 0.00, 0.00, "00",new StrutturaTerna("", "", "4"), new StrutturaTerna("", "", ""));
				dettFatt.setImportoRigaFatturaStr("0,00");
				dettFatt.setSconto1RigaFatturaStr("0,00");
				dettFatt.setSconto2RigaFatturaStr("0,00");

				if (elencaRigheFatt!=null &&  elencaRigheFatt.length!=0)
				{
					StrutturaFatturaVO[] listaDettFatt=new StrutturaFatturaVO[elencaRigheFatt.length +1];

					for (int i=0; i<elencaRigheFatt.length; i++)
					{
						listaDettFatt[i]=elencaRigheFatt[i];
					}

					// imposta il progressivo della riga
					dettFatt.setProgrRigaFattura(elencaRigheFatt.length+1);
					// imposta numero di riga ()
					dettFatt.setRigaFattura(elencaRigheFatt.length+1);
					if (elencaRigheFatt[elencaRigheFatt.length-1].getRigaFattura()>elencaRigheFatt.length)
					{
						dettFatt.setRigaFattura(elencaRigheFatt[elencaRigheFatt.length-1].getRigaFattura()+1);
					}

					listaDettFatt[elencaRigheFatt.length]=dettFatt;
					insFatture.set("elencaRigheFatt",listaDettFatt);
					insFatture.set("numRigheFatt",listaDettFatt.length);
					// ricarica la fattura completamente
					List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
					for (int i=0; i<listaDettFatt.length; i++)
					{
						elencaRigheFattArr.add(listaDettFatt[i]);
					}
					// ricarica la fattura completamente
					FatturaVO appoFatt=(FatturaVO)insFatture.get("datiFattura");
					appoFatt.setRigheDettaglioFattura(elencaRigheFattArr);
					insFatture.set("datiFattura",appoFatt);

					// gestione selezione della riga in inserimento
					Integer[]  radioRFatt=new Integer[1];
					if (appoFatt!=null && appoFatt.getRigheDettaglioFattura().size()>0)
					{
						radioRFatt[0]=appoFatt.getRigheDettaglioFattura().size() -1;
						insFatture.set("radioRFatt",radioRFatt);
					}



				}
				else
				{
					elencaRigheFatt=new StrutturaFatturaVO[1];
					elencaRigheFatt[0]=dettFatt;
					insFatture.set("elencaRigheFatt",elencaRigheFatt);
					insFatture.set("numRigheFatt",elencaRigheFatt.length);
					// ricarica la fattura completamente
					FatturaVO appoFatt=(FatturaVO)insFatture.get("datiFattura");
					List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
					for (int i=0; i<elencaRigheFatt.length; i++)
					{
						elencaRigheFattArr.add(elencaRigheFatt[i]);
					}
					appoFatt.setRigheDettaglioFattura(elencaRigheFattArr);
					insFatture.set("datiFattura",appoFatt);
					// gestione selezione della riga in inserimento
					Integer[]  radioRFatt=new Integer[1];
					if (appoFatt!=null && appoFatt.getRigheDettaglioFattura().size()>0)
					{
						radioRFatt[0]=appoFatt.getRigheDettaglioFattura().size() -1;
						insFatture.set("radioRFatt",radioRFatt);
					}

				}

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			InserisciFatturaForm insFattureNorm = (InserisciFatturaForm) form;

			try {
				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");

				if (radioRFatt.length!=0)
				{

					if (elencaRigheFatture.length!=0 )
					{

						// CONTROLLO CHE LA RIGA NON ABBIA UN INVENTARIO LEGATO
						FatturaVO appoFatt=(FatturaVO)insFattureNorm.get("datiFattura");
						String codPolo=appoFatt.getCodPolo();
						String codBib=appoFatt.getCodBibl();

						String codBibO=appoFatt.getCodBibl();
						String codTipOrd=elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice1();

						int annoOrd=0;
						if (elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2()!=null && elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2().trim().length()>0 )
						{
							annoOrd=Integer.valueOf(elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2());
						}
						int codOrd=0;
						if (elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3()!=null && elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3().trim().length()>0 )
						{
							codOrd=Integer.valueOf(elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3());
						}
						int rigaF=0;
						if (elencaRigheFatture[radioRFatt[0]].getRigaFattura()>=0)
						{
							rigaF=elencaRigheFatture[radioRFatt[0]].getRigaFattura();
						}

						String codBibF=appoFatt.getCodBibl();
						int annoFattura=0;
						if (appoFatt.getAnnoFattura()!=null && appoFatt.getAnnoFattura().trim().length()>0)
						{
							annoFattura=Integer.valueOf(appoFatt.getAnnoFattura());
						}
						int progrFattura=0;
						if (appoFatt.getProgrFattura()!=null && appoFatt.getProgrFattura().trim().length()>0)
						{
							progrFattura=Integer.valueOf(appoFatt.getProgrFattura());
						}

						String ticket=Navigation.getInstance(request).getUserTicket();
						FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
						List listaInv = null;
						//listaInventariFattura = (DescrittoreBloccoVO) factory.getGestioneDocumentoFisico().getListaInventariFattura( codPolo,  codBib, annoFattura,  progrFattura, ticket,99);
						try
						{
							listaInv = new ArrayList();
							listaInv = this.getListaInventariRigaFattura(codPolo,  codBib,codBibO,codTipOrd, annoOrd,codOrd, codBibF, annoFattura,  progrFattura, rigaF,  this.getLocale(request, Constants.SBN_LOCALE), ticket,insFattureNorm.getNRec(),insFattureNorm );
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (listaInv.size()>0)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.acquisizioni.erroreFatturaRigaConInventari", elencaRigheFatture[radioRFatt[0]].getRigaFattura(), null));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
						//	FINE CONTROLLO


						List lista =new ArrayList();
						for (int t=0;  t < elencaRigheFatture.length; t++)
						{
							StrutturaFatturaVO elem = elencaRigheFatture[t];
							lista.add(elem);
						}

						//String[] appo=  selectedDatiIntest;
						int i= (radioRFatt.length) -1;
						// ciclo dall'ultimo codice selezionato
						while (i>=0)
						{
							int elem = radioRFatt[i];
							// il valore del num riga è superiore di una unità rispetto all'indice dell'array
							lista.remove(elem);
							i=i-1;
						}

						//this.renumera(lista);


						StrutturaFatturaVO[] lista_fin =new StrutturaFatturaVO [lista.size()];

						for (int r=0;  r < lista.size(); r++)
						{
							lista_fin [r] = (StrutturaFatturaVO)lista.get(r);
							//lista_fin [r].setRigaFattura(r+1); //renumera
							lista_fin [r].setProgrRigaFattura(r+1); //renumera solo i progressivi non le righe fattura da passare a inventari
						}
						if (lista!=null && lista.size()>0 && ((StrutturaFatturaVO)lista.get(lista.size()-1)).getRigaFattura()==lista.size())
						{
							for (int r=0;  r < lista.size(); r++)
							{
								lista_fin [r].setRigaFattura(r+1); //renumera
							}
						}


						insFatture.set("elencaRigheFatt",lista_fin);
						insFatture.set("radioRFatt", null) ;
						insFatture.set("numRigheFatt",lista_fin.length);
						// ricarica la fattura completamente
						appoFatt=(FatturaVO)insFatture.get("datiFattura");
						List<StrutturaFatturaVO> elencaRigheFattArr=new ArrayList();
						for (int j=0; j<lista_fin.length; j++)
						{
							elencaRigheFattArr.add(lista_fin[j]);
						}
						appoFatt.setRigheDettaglioFattura(elencaRigheFattArr);
						// se non sono presenti righe il fornitore diventa editabile
						if (lista_fin.length==0)
						{
							// fornitore editabile
							appoFatt.getFornitoreFattura().setCodice("");
							appoFatt.getFornitoreFattura().setDescrizione("");
						}


						insFatture.set("datiFattura",appoFatt);

					}
					radioRFatt=new Integer[1];
					radioRFatt[0]=0;
					insFatture.set("radioRFatt",radioRFatt);

				}
			    ((InserisciFatturaForm) insFatture).setFatturaSalvata(false);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			//InserisciFatturaForm insFatture = (InserisciFatturaForm) form;
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			try {
				StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				FatturaVO eleFattura=(FatturaVO)insFatture.get("datiFattura");

				String amountFattImp=eleFattura.getImportoFatturaStr().trim();
			    Double risultFattImp=Pulisci.ControllaImporto(amountFattImp);
			    eleFattura.setImportoFattura(risultFattImp);
			    eleFattura.setImportoFatturaStr(Pulisci.VisualizzaImporto(eleFattura.getImportoFattura()));

				String amountFattSc1=eleFattura.getScontoFatturaStr().trim();
			    Double risultFattSc1=Pulisci.ControllaImporto(amountFattSc1);
			    eleFattura.setScontoFattura(risultFattSc1);
			    eleFattura.setScontoFatturaStr(Pulisci.VisualizzaImporto(eleFattura.getScontoFattura()));

			    // gestione aggiornamento dinamico del cambio al modificarsi della valuta
			    if (insFatture.get("valuta")!=null && !insFatture.get("valuta").equals(""))
				{
	    			String valuta=(String) insFatture.get("valuta");
	    			String[] valuta_composto=valuta.split("\\|");
					// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
					String val_primaParte=valuta_composto[0];
					String val_secondaParte=valuta_composto[1].trim();
					if (val_primaParte!=null && val_primaParte.trim().length()==3 && val_secondaParte!=null && val_secondaParte.trim().length()>0)
					{
						eleFattura.setCambioFattura(Double.valueOf(val_secondaParte));
						eleFattura.setValutaFattura(val_primaParte);
					}
				}

				// trasformo in arraylist
				List lista =new ArrayList();
				for (int w=0;  w < elencaRigheFatt.length; w++)
				{
					StrutturaFatturaVO elem = elencaRigheFatt[w];

					String amountDettFattImp=elem.getImportoRigaFatturaStr().trim();
				    Double risultDettFattImp=Pulisci.ControllaImporto(amountDettFattImp);
				    elem.setImportoRigaFattura(risultDettFattImp);
				    elem.setImportoRigaFatturaStr(Pulisci.VisualizzaImporto(elem.getImportoRigaFattura()));

					String amountDettFattSc1=elem.getSconto1RigaFatturaStr().trim();
				    Double risultDettFattSc1=Pulisci.ControllaImporto(amountDettFattSc1);
				    elem.setSconto1RigaFattura(risultDettFattSc1);
				    elem.setSconto1RigaFatturaStr(Pulisci.VisualizzaImporto(elem.getSconto1RigaFattura()));

					String amountDettFattSc2=elem.getSconto2RigaFatturaStr().trim();
				    Double risultDettFattSc2=Pulisci.ControllaImporto(amountDettFattSc2);
				    elem.setSconto2RigaFattura(risultDettFattSc2);
				    elem.setSconto2RigaFatturaStr(Pulisci.VisualizzaImporto(elem.getSconto2RigaFattura()));

				    lista.add(elem);
				}
				eleFattura.setRigheDettaglioFattura(lista);
				//eleFattura.setIDFatt(null); aggiunto per testare l'inserimento

				insFatture.set("datiFattura",eleFattura);

				boolean valRitorno = false;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				factory.getGestioneAcquisizioni().ValidaFatturaVO(eleFattura);
				// fine validazione

				ActionMessages errors = new ActionMessages();
				((InserisciFatturaForm) insFatture).setConferma(true);
				((InserisciFatturaForm) insFatture).setPressioneBottone("salva");
				((InserisciFatturaForm) insFatture).setDisabilitaTutto(true);
				errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				((InserisciFatturaForm) insFatture).setConferma(false);
				((InserisciFatturaForm) insFatture).setPressioneBottone("");
				((InserisciFatturaForm) insFatture).setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);

				return mapping.getInputForward();

			} catch (Exception e) {
				((InserisciFatturaForm) insFatture).setConferma(false);
				((InserisciFatturaForm) insFatture).setPressioneBottone("");
				((InserisciFatturaForm) insFatture).setDisabilitaTutto(false);
				return mapping.getInputForward();
			}
		}

		public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;


			try {
				// n.b. il ripristina in inserimento consiste nell'azzeramento se il record è inesistente,
				// mentre se si vuole effettuare modifica su un record appena inserito deve leggere
				FatturaVO eleFattura=(FatturaVO)insFatture.get("datiFattura");

				if ( eleFattura!=null && eleFattura.getIDFatt()!=null  && eleFattura.getIDFatt()>0)
				{
					// il record è già esistente
					// lettura
					FatturaVO eleFatturaLetto=this.loadDatiFattINS(eleFattura);


					insFatture.set("datiFattura",eleFatturaLetto);
					int totFatture=0;
					if (eleFatturaLetto.getRigheDettaglioFattura()!=null && eleFatturaLetto.getRigheDettaglioFattura().size()>0)
					{
						totFatture=eleFatturaLetto.getRigheDettaglioFattura().size();
					}
					//this.insBilanci.setNumImpegni(totImpegni);
					insFatture.set("numRigheFatt",totFatture);

					StrutturaFatturaVO[] oggettoDettFatt=new StrutturaFatturaVO[totFatture];
					for (int i=0; i<totFatture; i++)
					{
						oggettoDettFatt[i]=eleFatturaLetto.getRigheDettaglioFattura().get(i);
					}
					insFatture.set("elencaRigheFatt",oggettoDettFatt);

					if (eleFatturaLetto.getStatoFattura().equals("3") || eleFatturaLetto.getStatoFattura().equals("4"))
					{
						// ordine pagamento emesso
						// disabilitazione input
						((InserisciFatturaForm) insFatture).setDisabilitaTutto(true);
					}


				}
				else
				{
					FatturaVO oggettoFatt=this.loadDatiFattura();
					if (eleFattura!=null )
					{
						if (eleFattura.getCodBibl()!=null &&  eleFattura.getCodBibl().trim().length()>0)
						{
							oggettoFatt.setCodBibl(eleFattura.getCodBibl());
						}
						if (eleFattura.getCodPolo()!=null &&  eleFattura.getCodPolo().trim().length()>0)
						{
							oggettoFatt.setCodPolo(eleFattura.getCodPolo());
						}
						if (!((InserisciFatturaForm) insFatture).isGestBil())
						{
							oggettoFatt.setGestBil(false);
						}
					}
					insFatture.set("datiFattura",oggettoFatt);
					insFatture.set("numRigheFatt",0);
					StrutturaFatturaVO[] oggettoDettFatt=new StrutturaFatturaVO[0];
					insFatture.set("elencaRigheFatt",oggettoDettFatt);
				}

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			try {

//				Inizio Bug collaudo 4455 - Maggio 2013 sul dettaglio di una fattura è presente il tasto stampa che permette di stampare online
//				la fattura stessa in vari formati (pdf e altro); se creo la fattura, dopo averla salvata,
//				il bottone stampa non c'è e devo richiamare la fattura stessa per stamparla;
//				è stata effettuata la modifica affinche il salva appaia anche il bottone stampa ed è stato copiato
//				il contenuto del metodo dalla Action dell'esaminaFattura

				FatturaVO eleFattura=(FatturaVO)insFatture.get("datiFattura");
				if (eleFattura!=null && eleFattura.getAnagFornitore()!=null)
				{
					if (eleFattura.getAnagFornitore().getCodiceFiscale()!=null
							&& eleFattura.getAnagFornitore().getCodiceFiscale().trim().length()==0 ) {
						eleFattura.getAnagFornitore().setCodiceFiscale(null);
					}
					if (eleFattura.getAnagFornitore().getPartitaIva()!=null
							&& eleFattura.getAnagFornitore().getPartitaIva().trim().length()==0 ) {
						eleFattura.getAnagFornitore().setPartitaIva(null);
					}
				}

				request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_FATTURA);
				eleFattura.setTicket(Navigation.getInstance(request).getUserTicket());
				request.setAttribute("DATI_STAMPE_ON_LINE", eleFattura);
				return mapping.findForward("stampaOL");
//				return mapping.getInputForward();
//				Fine Bug collaudo 4455 - Maggio 2013
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}
		public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;

			try {
				StrutturaFatturaVO[] elencaRigheFatt= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				FatturaVO eleFattura=(FatturaVO)insFatture.get("datiFattura");
				eleFattura.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				String amountFattImp=eleFattura.getImportoFatturaStr().trim();
			    Double risultFattImp=Pulisci.ControllaImporto(amountFattImp);
			    eleFattura.setImportoFattura(risultFattImp);
			    eleFattura.setImportoFatturaStr(Pulisci.VisualizzaImporto(eleFattura.getImportoFattura()));

				String amountFattSc1=eleFattura.getScontoFatturaStr().trim();
			    Double risultFattSc1=Pulisci.ControllaImporto(amountFattSc1);
			    eleFattura.setScontoFattura(risultFattSc1);
			    eleFattura.setScontoFatturaStr(Pulisci.VisualizzaImporto(eleFattura.getScontoFattura()));

				// trasformo in arraylist
				List lista =new ArrayList();
				for (int w=0;  w < elencaRigheFatt.length; w++)
				{
					StrutturaFatturaVO elem = elencaRigheFatt[w];

					String amountDettFattImp=elem.getImportoRigaFatturaStr().trim();
				    Double risultDettFattImp=Pulisci.ControllaImporto(amountDettFattImp);
				    elem.setImportoRigaFattura(risultDettFattImp);
				    elem.setImportoRigaFatturaStr(Pulisci.VisualizzaImporto(elem.getImportoRigaFattura()));

					String amountDettFattSc1=elem.getSconto1RigaFatturaStr().trim();
				    Double risultDettFattSc1=Pulisci.ControllaImporto(amountDettFattSc1);
				    elem.setSconto1RigaFattura(risultDettFattSc1);
				    elem.setSconto1RigaFatturaStr(Pulisci.VisualizzaImporto(elem.getSconto1RigaFattura()));

					String amountDettFattSc2=elem.getSconto2RigaFatturaStr().trim();
				    Double risultDettFattSc2=Pulisci.ControllaImporto(amountDettFattSc2);
				    elem.setSconto2RigaFattura(risultDettFattSc2);
				    elem.setSconto2RigaFatturaStr(Pulisci.VisualizzaImporto(elem.getSconto2RigaFattura()));

				    lista.add(elem);
				}
				eleFattura.setRigheDettaglioFattura(lista);
				insFatture.set("datiFattura",eleFattura);
				//Integer[] radioImpegno=(Integer[])insBilanci.get("radioImpegno");

				((InserisciFatturaForm) insFatture).setConferma(false);
				((InserisciFatturaForm) insFatture).setDisabilitaTutto(false);

				if (((InserisciFatturaForm) insFatture).getPressioneBottone().equals("salva")) {
					((InserisciFatturaForm) insFatture).setPressioneBottone("");

					// se il codice ordine è già valorzzato si deve procedere alla modifica
					if (eleFattura.getIDFatt()!=null )
					{
						if (!this.modificaFattura(eleFattura)) {
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

							FatturaVO eleFatturaSaved=new FatturaVO();
							eleFatturaSaved=loadDatiFattINS(eleFattura);

							if (eleFatturaSaved!=null)
							{
								((InserisciFatturaForm) insFatture).setDatiFatturaOld((FatturaVO)eleFatturaSaved.clone());
								insFatture.set("datiFattura",eleFatturaSaved.clone());
								int totFatture=0;
				    			if (eleFatturaSaved.getRigheDettaglioFattura()!=null && eleFatturaSaved.getRigheDettaglioFattura().size()>0)
				    			{
				    				totFatture=eleFatturaSaved.getRigheDettaglioFattura().size();
				    			}
				    			//this.insBilanci.setNumImpegni(totImpegni);
				    			insFatture.set("numRigheFatt",totFatture);

				    			StrutturaFatturaVO[] oggettoDettFatt=new StrutturaFatturaVO[totFatture];
				    			for (int i=0; i<totFatture; i++)
				    			{
				    				oggettoDettFatt[i]=eleFatturaSaved.getRigheDettaglioFattura().get(i);
				    			}
				    			insFatture.set("elencaRigheFatt",oggettoDettFatt);
				    			if (eleFatturaSaved.getStatoFattura().equals("3") || eleFatturaSaved.getStatoFattura().equals("4"))
								{
									// ordine pagamento emesso
									// disabilitazione input
									((InserisciFatturaForm) insFatture).setDisabilitaTutto(true);
								}

							}

						}

					}
					else
					{
						if (!this.inserisciFattura(eleFattura)) {
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
							FatturaVO eleFatturaSaved=null;
							eleFatturaSaved=loadDatiFattINS(eleFattura);
							if (eleFatturaSaved!=null)
							{
								((InserisciFatturaForm) insFatture).setDatiFatturaOld((FatturaVO)eleFatturaSaved.clone());
								insFatture.set("datiFattura",eleFatturaSaved.clone());
				    			int totFatture=0;
				    			if (eleFatturaSaved.getRigheDettaglioFattura()!=null && eleFatturaSaved.getRigheDettaglioFattura().size()>0)
				    			{
				    				totFatture=eleFatturaSaved.getRigheDettaglioFattura().size();
				    			}
				    			//this.insBilanci.setNumImpegni(totImpegni);
				    			insFatture.set("numRigheFatt",totFatture);

				    			StrutturaFatturaVO[] oggettoDettFatt=new StrutturaFatturaVO[totFatture];
				    			for (int i=0; i<totFatture; i++)
				    			{
				    				oggettoDettFatt[i]=eleFatturaSaved.getRigheDettaglioFattura().get(i);
				    			}
				    			insFatture.set("elencaRigheFatt",oggettoDettFatt);
				    			if (eleFatturaSaved.getStatoFattura().equals("3") || eleFatturaSaved.getStatoFattura().equals("4"))
								{
									// ordine pagamento emesso
									// disabilitazione input
									((InserisciFatturaForm) insFatture).setDisabilitaTutto(true);
								}

							}

						}

					}
				}
				if (((InserisciFatturaForm) insFatture).getPressioneBottone().equals("cancella")) {
					((InserisciFatturaForm) insFatture).setPressioneBottone("");
						if (!this.cancellaFattura(eleFattura)) {
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
				}
				((InserisciFatturaForm) insFatture).setFatturaSalvata(true);
				return mapping.getInputForward();
			}	catch (ValidationException ve) {
				((InserisciFatturaForm) insFatture).setConferma(false);
				((InserisciFatturaForm) insFatture).setPressioneBottone("");
				((InserisciFatturaForm) insFatture).setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
/*				if (ve.getMsg().equal("msgBil")){
					errors.add("generico", new ActionMessage("errors.acquisizioni."+ ve.getMessage(), ve.getBid(), ve.getIsbd()));
				}
				else
				{
					errors.add("generico", new ActionMessage("errors.acquisizioni."+ ve.getMessage()));
				}
*/				errors.add("generico", new ActionMessage("errors.acquisizioni."+ ve.getMessage()));

					this.saveErrors(request, errors);
					return mapping.getInputForward();
			} catch (Exception e) {
				((InserisciFatturaForm) insFatture).setConferma(false);
				((InserisciFatturaForm) insFatture).setPressioneBottone("");
				((InserisciFatturaForm) insFatture).setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;

			try {
				// Viene settato il token per le transazioni successive
				this.saveToken(request);
				insFatture.setConferma(false);
				insFatture.setPressioneBottone("");
	    		insFatture.setDisabilitaTutto(false);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}




		public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;

			try {
				ActionMessages errors = new ActionMessages();
				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");

				//if (radioRFatt.length!=0)
				//{
					if (elencaRigheFatture.length==0 )
					{
						insFatture.setConferma(true);
						insFatture.setPressioneBottone("cancella");
			    		insFatture.setDisabilitaTutto(true);
						errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
						this.saveErrors(request, errors);
						this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
						return mapping.getInputForward();
					}
				//}
					else
					{
						errors.add("generico", new ActionMessage("errors.acquisizioni.erroreFatturaCancImpossibile"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}


			} catch (Exception e) {
				insFatture.setConferma(false);
				insFatture.setPressioneBottone("");
	    		insFatture.setDisabilitaTutto(false);
				return mapping.getInputForward();
			}
		}

		public ActionForward ordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			InserisciFatturaForm insFattureNorm = (InserisciFatturaForm) form;

			try {
				// gestione della pressione del bottone di riga su riga che non ha check
				if (((InserisciFatturaForm) insFatture).getNumRigaBottone()>=0)
				{
					// riga selezionata cambiare la posizione del radio button
					Integer[]  radioRFattBottone=new Integer[1];
					Integer indiceRigaBottone=((InserisciFatturaForm) insFatture).getNumRigaBottone();
					radioRFattBottone[0]=indiceRigaBottone;
					insFatture.set("radioRFatt",radioRFattBottone);
				}

				// CONTROLLO CHECK DI RIGA
				Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					int indiceRigaIns= radioRFatt[0]; // unico check
				}
				else
				{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.buoniOrdine.crea"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}


				// stato buono da cambiare
				// impongo il fornitore e il bilancio prescelti
				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				//Integer[]
				radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					if (elencaRigheFatture.length!=0 )
					{
						// CONTROLLO CHE LA RIGA NON ABBIA UN INVENTARIO LEGATO
						FatturaVO appoFatt=(FatturaVO)insFattureNorm.get("datiFattura");
						String codPolo=appoFatt.getCodPolo();
						String codBib=appoFatt.getCodBibl();

						String codBibO=appoFatt.getCodBibl();
						String codTipOrd=elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice1();

						int annoOrd=0;
						if (elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2()!=null && elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2().trim().length()>0 )
						{
							annoOrd=Integer.valueOf(elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2());
						}
						int codOrd=0;
						if (elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3()!=null && elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3().trim().length()>0 )
						{
							codOrd=Integer.valueOf(elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3());
						}
						int rigaF=0;
						if (elencaRigheFatture[radioRFatt[0]].getRigaFattura()>=0)
						{
							rigaF=elencaRigheFatture[radioRFatt[0]].getRigaFattura();
						}

						String codBibF=appoFatt.getCodBibl();
						int annoFattura=0;
						if (appoFatt.getAnnoFattura()!=null && appoFatt.getAnnoFattura().trim().length()>0)
						{
							annoFattura=Integer.valueOf(appoFatt.getAnnoFattura());
						}
						int progrFattura=0;
						if (appoFatt.getProgrFattura()!=null && appoFatt.getProgrFattura().trim().length()>0)
						{
							progrFattura=Integer.valueOf(appoFatt.getProgrFattura());
						}

						String ticket=Navigation.getInstance(request).getUserTicket();
						FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
						List listaInv = null;
						//listaInventariFattura = (DescrittoreBloccoVO) factory.getGestioneDocumentoFisico().getListaInventariFattura( codPolo,  codBib, annoFattura,  progrFattura, ticket,99);
						try
						{
							listaInv = new ArrayList();
							listaInv = this.getListaInventariRigaFattura(codPolo,  codBib,codBibO,codTipOrd, annoOrd,codOrd, codBibF, annoFattura,  progrFattura, rigaF,  this.getLocale(request, Constants.SBN_LOCALE), ticket,insFattureNorm.getNRec(),insFattureNorm );
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (listaInv.size()>0)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.acquisizioni.erroreFatturaRigaConInventari", elencaRigheFatture[radioRFatt[0]].getRigaFattura(), null));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
						//	FINE CONTROLLO




						for (int q=0; q<elencaRigheFatture.length; q++)
						{
							oggettoFatt.getRigheDettaglioFattura().add(elencaRigheFatture[q]);
						}
						int indiceRigaIns= radioRFatt[0]; // unico check
						// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
						// && request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null
						request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
						request.getSession().removeAttribute("ordiniSelected");
						request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

/*						if (request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null )
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
						ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=oggettoFatt.getCodBibl();
						String codBAff = null;
						//String tipoOrd=oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getOrdine().getCodice1(); // A
						//String annoOrd=oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getOrdine().getCodice2();
						//String codOrd=oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getOrdine().getCodice3();

						// tck 2405 deve riportare sulla maschera di ricerca nient'altro se non il fornitore che è immutabile
						/*
						String tipoOrd=elencaRigheFatture[indiceRigaIns].getOrdine().getCodice1(); // A
						String annoOrd2=elencaRigheFatture[indiceRigaIns].getOrdine().getCodice2().trim();
						String codOrd2=elencaRigheFatture[indiceRigaIns].getOrdine().getCodice3().trim();
						StrutturaTerna bil=new StrutturaTerna(elencaRigheFatture[indiceRigaIns].getBilancio().getCodice1(),elencaRigheFatture[indiceRigaIns].getBilancio().getCodice2(),elencaRigheFatture[indiceRigaIns].getBilancio().getCodice3() );
						*/

						String tipoOrd=""; // A
						String annoOrd2="";
						String codOrd2="";

						String dataOrdDa=null;
						String dataOrdA=null;
						String cont=null;
						String statoOrd=null;
						StrutturaCombo forn=new StrutturaCombo (oggettoFatt.getFornitoreFattura().getCodice(),oggettoFatt.getFornitoreFattura().getDescrizione().trim());
						String tipoInvioOrd=null;
						//StrutturaTerna bil=new StrutturaTerna(oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getBilancio().getCodice1(),oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getBilancio().getCodice2(),"" );
						StrutturaTerna bil=new StrutturaTerna("","","");
						String sezioneAcqOrd=null;
						StrutturaCombo tit=new StrutturaCombo ("","");
						String dataFineAbbOrdDa=null;
						String dataFineAbbOrdA=null;
						String naturaOrd=null;
						String chiama=mapping.getPath();
						String[] statoOrdArr=new String[1];
						statoOrdArr[0]="A";
						Boolean rinn=false;
						Boolean stamp=false;

						eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd2,  annoOrd2,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
						String ticket2=Navigation.getInstance(request).getUserTicket();
						eleRicerca.setTicket(ticket2);
						eleRicerca.setModalitaSif(false);

						request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);
						return mapping.findForward("ordine");
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

		public ActionForward bilancio(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			DynaValidatorForm insFatture = (DynaValidatorForm) form;
			InserisciFatturaForm insFattureNorm = (InserisciFatturaForm) form;

			try {

				// gestione della pressione del bottone di riga su riga che non ha check
				if (((InserisciFatturaForm) insFatture).getNumRigaBottone()>=0)
				{
					// riga selezionata cambiare la posizione del radio button
					Integer[]  radioRFattBottone=new Integer[1];
					Integer indiceRigaBottone=((InserisciFatturaForm) insFatture).getNumRigaBottone();
					radioRFattBottone[0]=indiceRigaBottone;
					insFatture.set("radioRFatt",radioRFattBottone);
				}

				// CONTROLLO CHECK DI RIGA
				Integer[] radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					int indiceRigaIns= radioRFatt[0]; // unico check
				}
				else
				{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.buoniOrdine.crea"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}

				// stato buono da cambiare
				// impongo il fornitore e il bilancio prescelti
				FatturaVO oggettoFatt=(FatturaVO) insFatture.get("datiFattura");
				StrutturaFatturaVO[] elencaRigheFatture= (StrutturaFatturaVO[]) insFatture.get("elencaRigheFatt");
				radioRFatt=(Integer[])insFatture.get("radioRFatt");
				if (radioRFatt.length!=0)
				{
					if (elencaRigheFatture.length!=0 )
					{

						// CONTROLLO CHE LA RIGA NON ABBIA UN INVENTARIO LEGATO
						FatturaVO appoFatt=(FatturaVO)insFattureNorm.get("datiFattura");
						String codPolo=appoFatt.getCodPolo();
						String codBib=appoFatt.getCodBibl();

						String codBibO=appoFatt.getCodBibl();
						String codTipOrd=elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice1();

						int annoOrd=0;
						if (elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2()!=null && elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2().trim().length()>0 )
						{
							annoOrd=Integer.valueOf(elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice2());
						}
						int codOrd=0;
						if (elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3()!=null && elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3().trim().length()>0 )
						{
							codOrd=Integer.valueOf(elencaRigheFatture[radioRFatt[0]].getOrdine().getCodice3());
						}
						int rigaF=0;
						if (elencaRigheFatture[radioRFatt[0]].getRigaFattura()>=0)
						{
							rigaF=elencaRigheFatture[radioRFatt[0]].getRigaFattura();
						}

						String codBibF=appoFatt.getCodBibl();
						int annoFattura=0;
						if (appoFatt.getAnnoFattura()!=null && appoFatt.getAnnoFattura().trim().length()>0)
						{
							annoFattura=Integer.valueOf(appoFatt.getAnnoFattura());
						}
						int progrFattura=0;
						if (appoFatt.getProgrFattura()!=null && appoFatt.getProgrFattura().trim().length()>0)
						{
							progrFattura=Integer.valueOf(appoFatt.getProgrFattura());
						}

						String ticket=Navigation.getInstance(request).getUserTicket();
						FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
						List listaInv = null;
						//listaInventariFattura = (DescrittoreBloccoVO) factory.getGestioneDocumentoFisico().getListaInventariFattura( codPolo,  codBib, annoFattura,  progrFattura, ticket,99);
						try
						{
							listaInv = new ArrayList();
							listaInv = this.getListaInventariRigaFattura(codPolo,  codBib,codBibO,codTipOrd, annoOrd,codOrd, codBibF, annoFattura,  progrFattura, rigaF,  this.getLocale(request, Constants.SBN_LOCALE), ticket,insFattureNorm.getNRec(),insFattureNorm );
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (listaInv.size()>0)
						{
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.acquisizioni.erroreFatturaRigaConInventari", elencaRigheFatture[radioRFatt[0]].getRigaFattura(), null));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
						//	FINE CONTROLLO

						for (int q=0; q<elencaRigheFatture.length; q++)
						{
							oggettoFatt.getRigheDettaglioFattura().add(elencaRigheFatture[q]);
						}

						int indiceRigaIns= radioRFatt[0]; // unico check

						ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=oggettoFatt.getCodBibl();
						//String ese=oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getBilancio().getCodice1();
						//String cap=oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getBilancio().getCodice2();
						//String imp=oggettoFatt.getRigheDettaglioFattura().get(indiceRigaIns).getBilancio().getCodice3();
						String ese=elencaRigheFatture[indiceRigaIns].getBilancio().getCodice1().trim();
						String cap=elencaRigheFatture[indiceRigaIns].getBilancio().getCodice2().trim();
						String imp=elencaRigheFatture[indiceRigaIns].getBilancio().getCodice3();
						String chiama=mapping.getPath();
						String ordina="";
						eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
						eleRicerca.setModalitaSif(false);
						request.getSession().setAttribute("attributeListaSuppBilancioVO", eleRicerca);

						return mapping.findForward("bilancio");
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


		private FatturaVO loadDatiFattura() throws Exception {
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

			FatturaVO fatt=new FatturaVO("", "", annoAttuale, "", "", "", dataOdiernaStr, 0.00,0.00, "EUR",1 , "1", "F" , new StrutturaCombo("",""),"");
			fatt.setImportoFatturaStr("0,00");
			fatt.setScontoFatturaStr("0,00");

			StrutturaFatturaVO strutFatt =new StrutturaFatturaVO("", "", new StrutturaTerna("", "", ""),1,"",0.00, 0.00, 0.00, "00",new StrutturaTerna("", "", ""), new StrutturaTerna("", "", ""));
			strutFatt.setImportoRigaFatturaStr("0,00");
			strutFatt.setSconto1RigaFatturaStr("0,00");
			strutFatt.setSconto2RigaFatturaStr("0,00");

			List lista=new ArrayList();
			//lista.add(strutFatt);
			fatt.setRigheDettaglioFattura(lista);

			return fatt;

		}


		public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;

			try {
				// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
				// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
				ListaSuppFatturaVO ricArr=(ListaSuppFatturaVO) request.getSession().getAttribute("attributeListaSuppFatturaVO");
				if (ricArr!=null )
					{
						// gestione del chiamante
						if (ricArr!=null && ricArr.getChiamante()!=null)
						{

							// carico i risultati della selezione nella variabile da restituire
							request.getSession().setAttribute("attributeListaSuppFatturaVO", this.AggiornaRisultatiListaSupportoDaIns( insFatture, ricArr));
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
		 * InserisciBilancioAction.java
		 * @param eleRicArr
		 * @return
		 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
		 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
		 */
		private ListaSuppFatturaVO AggiornaRisultatiListaSupportoDaIns (InserisciFatturaForm insFatture, ListaSuppFatturaVO eleRicArr)
		{
			try {
				List<FatturaVO> risultati=new ArrayList();
				FatturaVO eleFattura=(FatturaVO)insFatture.get("datiFattura");
				risultati.add(eleFattura);
				eleRicArr.setSelezioniChiamato(risultati);

			} catch (Exception e) {

			}
			return eleRicArr;
		}




	/*	private void loadDatiFattura() throws Exception {
			// caricamento fattura

			FatturaVO fatt=new FatturaVO("X10", "01", "2004", "4", "10", "10/01/2004", "10/01/2004", 400.00,0.00, "EUR", 1, "2", "F" , new StrutturaCombo("33","Libreria Grande"),"");
			this.esaFatture.setDatiFattura(fatt);
			// caricamento righe
			List lista = new ArrayList();
			 StrutturaFatturaVO strutFatt = new StrutturaFatturaVO("X10", "01", new StrutturaTerna("", "", ""),1,"note",400.00, 0.00, 0.00, "0",new StrutturaTerna("2004", "3", "0"), new StrutturaTerna("14", "A", "2005"));
			 //List lista_new = new ArrayList();
			 //BilancioDettVO dettBil = new BilancioDettVO("1",0.00,0.00,0.00,0.00 );
			 //lista_new.add(dettBil);
			 //strutFatt.getBilancio().setDettagliBilancio(lista_new);

			 lista.add(strutFatt);
			 strutFatt = new StrutturaFatturaVO("X10", "01", new StrutturaTerna("", "", ""),1,"note 2",300.00, 0.00, 0.00, "0",new StrutturaTerna("2004", "3", "0"), new StrutturaTerna("14", "A", "2005"));

			 //strutFatt = new StrutturaFatturaVO("X10", "01", fatt,2,"note 2",300.00, 0.00, 0.00, "0",new BilancioVO("X10","01","2004", "3",0, ""), new OrdineVO("14", "A", new StrutturaCombo("RAV0008725", "Airone: vivere la natura conoscere il mondo"), new StrutturaCombo("33", "Libreria Grande"), new BilancioVO("X10","01","2004", "3",0,""), "A", "S", true, "27/03/2005", "X10", "01", "19.00") );
			 //lista_new = new ArrayList();
			 //dettBil = new BilancioDettVO("1",0.00,0.00,0.00,0.00 );
			 //lista_new.add(dettBil);
			 //strutFatt.getBilancio().setDettagliBilancio(lista_new);

			 lista.add(strutFatt);


			 this.esaFatture.setListaFattura(lista);
		}


		private void totalizzaImporto() throws Exception {

			int i;
			double appoimp=0;
			for (i=0;  i < this.esaFatture.getListaFattura().size(); i++)
			{
				StrutturaFatturaVO strutFatt = (StrutturaFatturaVO) this.esaFatture.getListaFattura().get(i);
				double impF=strutFatt.getImportoRigaFattura();
				// renumera lista
				strutFatt.setRigaFattura(i+1);
				appoimp=appoimp + impF;
			}
			//String totImporto= String.valueOf(appoimp);
			this.esaFatture.getDatiFattura().setImportoFattura(appoimp);
			}

		private void renumera() throws Exception {

			int i;
			double appoimp=0;
			for (i=0;  i < this.esaFatture.getListaFattura().size(); i++)
			{
				StrutturaFatturaVO strutFatt = (StrutturaFatturaVO) this.esaFatture.getListaFattura().get(i);
				// renumera lista
				strutFatt.setRigaFattura(i+1);
			}
		}

	*/


		private List loadTipoFatt() throws Exception {
			List lista = new ArrayList();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo carCombo = new CaricamentoCombo();
			//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
			lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoFattura());

/*			StrutturaCombo elem = new StrutturaCombo("F","F - Fattura");
			lista.add(elem);
			elem = new StrutturaCombo("N","N - Nota di Credito");
			lista.add(elem);
*/			//this.esaFatture.setListaTipoFatt(lista);
			return lista;
		}

/*		private List loadValuta() throws Exception {
			List lista = new ArrayList();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
			lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());

			StrutturaCombo elem = new StrutturaCombo("AFA","AFA - afghani");
			lista.add(elem);
			elem = new StrutturaCombo("EUR","EUR - euro");
			lista.add(elem);
			elem = new StrutturaCombo("NZD","NZD - dollaro neozelandese");
			lista.add(elem);
			//this.esaFatture.setListaValuta(lista);
			return lista;
		}
*/

	    private List loadValuta( HttpServletRequest request, String biblSel ) throws Exception {
			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//esaord.setListaValuta(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta()));
	    	// esegui query su cambi di biblioteca
	    	List<CambioVO> listaCambi=null;
	    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

	    	ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			//String codB=Navigation.getInstance(request).getUtente().getCodBib();
			String codB=biblSel;
			String codVal="";
			String desVal="";
			String chiama=null;
			eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
			eleRicerca.setOrdinamento("");
			try {
				listaCambi = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(eleRicerca);
				}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	    	List lista = new ArrayList();

	    	boolean esisteEuro= false;

	    	if (listaCambi==null || listaCambi.size()==0)
			{
	    		StrutturaTerna elem = new StrutturaTerna("EUR|1",  "EUR EURO", "1");
				lista.add(elem);
			}
	    	else
	    	{
	        	for (int w=0;  w < listaCambi.size(); w++)
	    		{
	        		StrutturaTerna elem = new StrutturaTerna(listaCambi.get(w).getCodValuta()+"|"+ String.valueOf(listaCambi.get(w).getTassoCambio()), listaCambi.get(w).getCodValuta()+" "+ listaCambi.get(w).getDesValuta().trim(), String.valueOf(listaCambi.get(w).getTassoCambio()));
	    			if (listaCambi.get(w).getCodValuta().equals("EUR"))
	    			{
	    				esisteEuro=true;
	    			}
	        		lista.add(elem);
	    		}
	    	}

			//esaord.setListaValuta(lista);
			return lista;

	    }


		private List loadStatoFatt() throws Exception {
			List lista = new ArrayList();
			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("1","1 - Registrata");
			lista.add(elem);
			elem = new StrutturaCombo("2","2 - Controllata");
			lista.add(elem);
			elem = new StrutturaCombo("3","3 - Ordine di pagamento emesso");
			lista.add(elem);
			elem = new StrutturaCombo("4","4 - Contabilizzata");
			lista.add(elem);

			//this.esaFatture.setListaStatoFatt(lista);
			return lista;
		}

		private List loadTipoOrdine() throws Exception {
			List lista = new ArrayList();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo carCombo = new CaricamentoCombo();
			lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine());

/*			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("A","A - Acquisto");
			lista.add(elem);
			elem = new StrutturaCombo("L","L - Deposito Legale");
			lista.add(elem);
			elem = new StrutturaCombo("D","D - Dono");
			lista.add(elem);
			elem = new StrutturaCombo("C","C - Scambio");
			lista.add(elem);
			elem = new StrutturaCombo("V","V - Visione Trattenuta");
			lista.add(elem);
*/
			//this.esaFatture.setListaTipoOrdine(lista);
			return lista;
		}

		private List loadTipoImpegno() throws Exception {

			List lista = new ArrayList();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo carCombo = new CaricamentoCombo();
			//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
			lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale());

/*			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
			elem = new StrutturaCombo("1","1  - Monografie e periodici non in abbonamento");
			lista.add(elem);
			elem = new StrutturaCombo("2","2  - Periodici in abbonamento");
			lista.add(elem);
			elem = new StrutturaCombo("3","3  - Collane");
			lista.add(elem);
*/
			//this.esaFatture.setListaTipoImpegno(lista);
			return lista;
		}

		private List loadIVA() throws Exception {
			List lista = new ArrayList();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo carCombo = new CaricamentoCombo();
			lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceIva());

/*			StrutturaCombo elem = new StrutturaCombo("00","");
			lista.add(elem);
			elem = new StrutturaCombo("00","0 Esente");
			lista.add(elem);
			elem = new StrutturaCombo("20","20 IVA al 20%");
			lista.add(elem);
			elem = new StrutturaCombo("04","4 IVA al 4%");
			lista.add(elem);
*/
			//this.esaFatture.setListaIva(lista);
			return lista;
		}

		private boolean inserisciFattura(FatturaVO fattura) throws Exception {
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			valRitorno = factory.getGestioneAcquisizioni().inserisciFattura(fattura);
			return valRitorno;
		}

		private boolean modificaFattura(FatturaVO fattura) throws Exception {
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			valRitorno = factory.getGestioneAcquisizioni().modificaFattura(fattura);
			return valRitorno;
		}


		private boolean cancellaFattura(FatturaVO fattura) throws Exception {
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			valRitorno = factory.getGestioneAcquisizioni().cancellaFattura(fattura);
			return valRitorno;
		}


		private List getListaInventariFattura(String codPolo, String codBibF, int annoF, int progF,
				Locale locale, String ticket, int nRec, ActionForm form)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;
			DescrittoreBloccoVO blocco1;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			blocco1 = factory.getGestioneDocumentoFisico().getListaInventariFattura(codPolo, codBibF, annoF, progF, locale, ticket, nRec);
			if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
				return null;
			}else{
				return blocco1.getLista();
			}
		}

		private List getListaInventariRigaFattura(String codPolo,String codBib,String codBibO, String codTipOrd, int annoOrd, int codOrd,  String codBibF, int annoF, int progF,int rigaF, Locale locale,
				String ticket, int nRec, ActionForm form)
		throws Exception {
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;
			DescrittoreBloccoVO blocco1;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			blocco1 = factory.getGestioneDocumentoFisico().getListaInventariRigaFattura(codPolo, codBib,codBibO, codTipOrd,  annoOrd,  codOrd,codBibF, annoF, progF,  rigaF, locale, ticket, nRec);
			//(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd, int codOrd, String codBibF,  int annoF, int prgF, int rigaF, Locale locale, String ticket, int nRec)
			if (blocco1 == null ||  blocco1.getTotRighe() <= 0)  {
				return null;
			}else{
				return blocco1.getLista();
			}
		}



		private ActionForward loadDefault(HttpServletRequest request,
				ActionMapping mapping, ActionForm form) {
			ActionMessages errors = new ActionMessages();
			InserisciFatturaForm insFatture = (InserisciFatturaForm) form;
			if (!insFatture.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					insFatture.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				} catch (Exception e) {
					errors.clear();
					errors.add("noSelection", new ActionMessage("error.acquisizioni.erroreDefault"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
			return mapping.getInputForward();
		}

		private ConfigurazioneORDVO loadConfigurazioneORD(ConfigurazioneORDVO configurazioneORD) throws Exception {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
			configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
			//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
			//gestire l'esistenza del risultato e che sia univoco
			//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
			// impostazione delle variabili dinamiche

			return configurazioneTrovata;
		}



		private FatturaVO loadDatiFattINS(FatturaVO eleFattura) throws Exception
		{

			FatturaVO fatt=null;

			ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
			// condizioni di ricerca univoca

			String codP=eleFattura.getCodPolo();
			String codB=eleFattura.getCodBibl();
			String annoF=eleFattura.getAnnoFattura();
			String numF=eleFattura.getNumFattura();
			String staF="";
			String dataDa="";
			String dataA="";
			String prgF=eleFattura.getProgrFattura(); // non ho il progressivo in fase di inserimento
			String dataF=eleFattura.getDataFattura();
			String dataRegF=eleFattura.getDataRegFattura();
			String tipF=eleFattura.getTipoFattura();
			StrutturaTerna ordFatt=null;
			StrutturaCombo fornFatt=new StrutturaCombo(eleFattura.getFornitoreFattura().getCodice(),"");
			StrutturaTerna bilFatt=null;
			String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<FatturaVO> fattureTrovate = new ArrayList();
			try {
				fattureTrovate = factory.getGestioneAcquisizioni().getRicercaListaFatture(eleRicerca);
			} catch (Exception e) {
		    	e.printStackTrace();
			}
			if (fattureTrovate.size()>0)
			{
				fatt=fattureTrovate.get(0);
				try {
					fatt.setImportoFatturaStr(Pulisci.VisualizzaImporto( fatt.getImportoFattura()));
					fatt.setScontoFatturaStr(Pulisci.VisualizzaImporto( fatt.getScontoFattura()));

					for (int w=0;  w < fatt.getRigheDettaglioFattura().size(); w++)
					{
						//BilancioDettVO elem = bil.getDettagliBilancio().get(w);
						fatt.getRigheDettaglioFattura().get(w).setImportoRigaFatturaStr(Pulisci.VisualizzaImporto( fatt.getRigheDettaglioFattura().get(w).getImportoRigaFattura()));
						fatt.getRigheDettaglioFattura().get(w).setSconto1RigaFatturaStr(Pulisci.VisualizzaImporto( fatt.getRigheDettaglioFattura().get(w).getSconto1RigaFattura()));
						fatt.getRigheDettaglioFattura().get(w).setSconto2RigaFatturaStr(Pulisci.VisualizzaImporto( fatt.getRigheDettaglioFattura().get(w).getSconto2RigaFattura()));
						//lista.add(elem);
					}
				} catch (Exception e) {
				    	//e.printStackTrace();
				    	//throw new ValidationException("importoErrato",
				    	//		ValidationExceptionCodici.importoErrato);
					fatt.setImportoFatturaStr("0,00");
					fatt.setScontoFatturaStr("0,00");

				}
			}

			//gestire l'esistenza del risultato e che sia univoco
/*			try {
				insCambio.getDatiCambio().setTassoCambioStr(Pulisci.VisualizzaImportoCambio( insCambio.getDatiCambio().getTassoCambio()));
			} catch (Exception e) {
			    	//e.printStackTrace();
			    	//throw new ValidationException("importoErrato",
			    	//		ValidationExceptionCodici.importoErrato);
				insCambio.getDatiCambio().setTassoCambioStr("0,00");
			}

*/
			return fatt;
		}

}
