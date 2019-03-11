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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.PartecipantiGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.gare.EsaminaGaraForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaGaraAction extends LookupDispatchAction implements SbnAttivitaChecker {
	//private EsaminaGaraForm esaGare;
	//private EsaminaGaraForm esaGareNorm;
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("acquisizioni.bottone.si", "Si");
        map.put("acquisizioni.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
/*		map.put("ricerca.button.insRiga","inserisciRiga");
		map.put("ricerca.button.cancRiga","cancellaRiga");
*/
/*		map.put("ricerca.button.suggBibl","suggerimenti");
		map.put("ricerca.button.docum","documenti");*/
		map.put("ricerca.button.suggBibl","suggerimentiCerca");
		map.put("ricerca.button.suggLett","documentiCerca");
		map.put("ricerca.label.fornitori","fornitore");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("ricerca.button.insForn","inserisciForn");
		map.put("ricerca.button.cancForn","cancellaForn");



		//		map.put("ricerca.button.stampa","stampa");
//		map.put("ricerca.label.fornitori","fornitore");
//		map.put("ricerca.label.sezione","sezioneCerca");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!esaGareNorm.isSessione())
			{
				esaGareNorm.setSessione(true);
			}
			//String ticket=Navigation.getInstance(request).getUserTicket();
			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppGaraVO ricArr=(ListaSuppGaraVO) request.getSession().getAttribute("attributeListaSuppGaraVO");
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				esaGareNorm.setEsaminaInibito(true);
				esaGareNorm.setDisabilitaTutto(true);
			}

			esaGareNorm.setListaDaScorrere((List<ListaSuppGaraVO>) request.getSession().getAttribute("criteriRicercaGara"));
			if(esaGareNorm.getListaDaScorrere() != null && esaGareNorm.getListaDaScorrere().size()!=0)
			{
				if(esaGareNorm.getListaDaScorrere().size()>1 )
				{
					esaGareNorm.setEnableScorrimento(true);
					//esaCambio.setPosizioneScorrimento(0);
					// carica ticket

				}
				else
				{
					esaGareNorm.setEnableScorrimento(false);
				}

				//this.loadAppo(resultckeck);
			}
			if (String.valueOf(esaGareNorm.getPosizioneScorrimento()).length()==0 )
			{
				esaGareNorm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			//this.loadDatiOrdinePassato(esaordini.getListaDaScorrere().get(this.esaordini.getPosizioneScorrimento()));
			// reimposta la pagina con i valori immessi oppure carica la pagina iniziale

/*			// gestione note di riga
			if (request.getParameter("paramLink") != null) {
				esaGareNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("paramLink")));
				esaGareNorm.setClicRispPrg(-1);
			}
			// gestione risposta di riga
			if (request.getParameter("paramLinkRisp") != null) {
				esaGareNorm.setClicRispPrg(Integer.valueOf(request.getParameter("paramLinkRisp")));
				esaGareNorm.setClicNotaPrg(-1);
			}
*/

			// gestione note di riga
			if (request.getParameter("tagNote")!=null )
    	   	{
				//esaGareNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
				esaGareNorm.setClicRispPrg(-1);
        		if (esaGareNorm.getClicNotaPrg()!=null && esaGareNorm.getClicNotaPrg().equals(Integer.valueOf(request.getParameter("tagNote"))))
        	   	{
        			esaGareNorm.setClicNotaPrg(-1);
        	   	}
	    		else
	    	   	{
	    			esaGareNorm.setClicNotaPrg(Integer.valueOf(request.getParameter("tagNote")));
	    	   	}
    	   	}

    		// gestione risposta di riga
    		if (request.getParameter("tagRisp")!=null )
    	   	{
    			//esaGareNorm.setClicRispPrg(Integer.valueOf(request.getParameter("tagRisp")));
    			esaGareNorm.setClicNotaPrg(-1);
        		if (esaGareNorm.getClicRispPrg()!=null && esaGareNorm.getClicRispPrg().equals(Integer.valueOf(request.getParameter("tagRisp"))))
        	   	{
        			esaGareNorm.setClicRispPrg(-1);
        	   	}
	    		else
	    	   	{
	    			esaGareNorm.setClicRispPrg(Integer.valueOf(request.getParameter("tagRisp")));
	    	   	}

    	   	}


			DynaValidatorForm esaGare = (DynaValidatorForm) form;
			GaraVO oggettoGara;
			if (!esaGareNorm.isCaricamentoIniziale())
			{
				oggettoGara=this.loadDatiGaraPassata(esaGareNorm.getListaDaScorrere().get(esaGareNorm.getPosizioneScorrimento()));
				esaGareNorm.setCaricamentoIniziale(true);
			}
			else
			{
				oggettoGara=(GaraVO) esaGare.get("richOff");
				// carico eventuali righe inserite
				PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
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
			}
			esaGare.set("richOff",oggettoGara);

			/*
			StrutturaProfiloVO oggettoProf=this.loadDatiProfiloPassato(this.esaProfiloNorm.getListaDaScorrere().get(this.esaProfiloNorm.getPosizioneScorrimento()));


		// reimposta la fattura con i valori immessi precedentemente
			if (esaProfilo.get("datiProfilo")!=null )
			{
				StrutturaProfiloVO oggettoTemp=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

				if (oggettoTemp.getCodBibl()!=null || (oggettoTemp.getProfilo()!=null && oggettoTemp.getProfilo().getCodice()!=null) || (oggettoTemp.getSezione()!=null && oggettoTemp.getSezione().getCodice()!=null) || (oggettoTemp.getPaese()!=null && oggettoTemp.getPaese().getCodice()!=null) || (oggettoTemp.getLingua()!=null && oggettoTemp.getLingua().getCodice()!=null)    )
				{
					oggettoProf=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");
					// carico eventuali righe inserite
					StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
					// trasformo in arraylist
					List lista =new ArrayList();

					for (int w=0;  w < elencaRigheProf.length; w++)
					{
						StrutturaTerna elem = elencaRigheProf[w];
						lista.add(elem);
					}
					oggettoProf.setListaFornitori(lista);

				}
			}
			esaProfilo.set("datiProfilo",oggettoProf);
*/

			//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
/*			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
 			{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
				{
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
					{
						oggettoProf.getSezione().setCodice(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					oggettoProf.getSezione().setCodice("");
				}
				// vedere se è necessaria l'impostazione
				esaProfilo.set("datiProfilo",oggettoProf);
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");

 			}*/


			//controllo se ho un risultato di una lista di supporto FORNITORI richiamata da questa pagina (risultato della simulazione)

/*			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						//dal size aggiungo le righe selezionate e importateù
						if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
						{
							int indiceStart=oggettoProf.getListaFornitori().size();
							for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
							{
								indiceStart=indiceStart+1;
								FornitoreVO eleRicForn = (FornitoreVO) ricForn.getSelezioniChiamato().get(i);
								StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(indiceStart),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
								oggettoProf.getListaFornitori().add(eleListaForn);
							}

						}
						else
						{
							oggettoProf.setListaFornitori(new ArrayList());
							for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
							{
								FornitoreVO eleRicForn = (FornitoreVO) ricForn.getSelezioniChiamato().get(i);
								StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
								oggettoProf.getListaFornitori().add(eleListaForn);
							}
						}
						esaProfilo.set("datiProfilo",oggettoProf);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					this.ricBuoni.setTipoOrdine("");
					this.ricBuoni.setAnno("");
					this.ricBuoni.setNumero("");

				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
 			}*/

			// reimposta la fattura con i valori immessi
/*			if (esaProfilo.get("datiProfilo")!=null )
			{
				StrutturaProfiloVO oggettoTemp=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

				if (oggettoTemp.getProfilo()!=null && oggettoTemp.getCodBibl()!=null && oggettoTemp.getProfilo().getCodice()!=null   )
				{
					oggettoProf=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");
				}
			}*/


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
							PartecipantiGaraVO[] listaForn= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
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
						esaGare.set("richOff",oggettoGara);

						PartecipantiGaraVO[] lista_fin =new PartecipantiGaraVO [oggettoGara.getDettaglioPartecipantiGara().size()];

						for (int r=0;  r < oggettoGara.getDettaglioPartecipantiGara().size(); r++)
						{
							lista_fin [r] = oggettoGara.getDettaglioPartecipantiGara().get(r);
							//lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
						}

						esaGare.set("elencaRighePartecipanti",lista_fin);
						//insGare.set("radioPartecipante", null) ;
						esaGare.set("numRighePartecipanti",lista_fin.length);


						// blocco importazione singolo oggetto

/*						Integer[] radioPartecipante=(Integer[])esaGare.get("radioPartecipante");
						PartecipantiGaraVO[] oggettoDettGara3= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
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
								esaGare.set("elencaRighePartecipanti",(PartecipantiGaraVO[]) oggettoDettGara3);
								// ricarica la gara completamente
								GaraVO eleGara=(GaraVO) esaGare.get("richOff");
								// trasformo in arraylist
								List<PartecipantiGaraVO> listaPart =new ArrayList();
								for (int w=0;  w < oggettoDettGara3.length; w++)
								{
									PartecipantiGaraVO elem = oggettoDettGara3[w];
									listaPart.add(elem);
								}
								eleGara.setDettaglioPartecipantiGara(listaPart);
								esaGare.set("richOff",eleGara);
								oggettoGara=eleGara;

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
						GaraVO oggettoGara2= (GaraVO) esaGare.get("richOff");
						oggettoGara2.getBid().setCodice(ricDoc.getSelezioniChiamato().get(0).getTitolo().getCodice());
						oggettoGara2.getBid().setDescrizione(ricDoc.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
						oggettoGara2.setNaturaBid(ricDoc.getSelezioniChiamato().get(0).getNaturaBid());

						esaGare.set("richOff",oggettoGara2);
						oggettoGara=oggettoGara2;

					}
				}
				else
				{
					// pulizia della maschera di ricerca
					GaraVO oggettoGara2= (GaraVO) esaGare.get("richOff");
					oggettoGara2.getBid().setCodice("");
					oggettoGara2.getBid().setDescrizione("");
					esaGare.set("richOff",oggettoGara2);
					oggettoGara=oggettoGara2;

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
						GaraVO oggettoGara2= (GaraVO) esaGare.get("richOff");
						oggettoGara2.getBid().setCodice(ricSugg.getSelezioniChiamato().get(0).getTitolo().getCodice());
						oggettoGara2.getBid().setDescrizione(ricSugg.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
						oggettoGara2.setNaturaBid(ricSugg.getSelezioniChiamato().get(0).getNaturaBid());
						esaGare.set("richOff",oggettoGara2);
						oggettoGara=oggettoGara2;

					}
				}
				else
				{
					// pulizia della maschera di ricerca
					GaraVO oggettoGara2= (GaraVO) esaGare.get("richOff");
					oggettoGara2.getBid().setCodice("");
					oggettoGara2.getBid().setDescrizione("");
					esaGare.set("richOff",oggettoGara2);
					oggettoGara=oggettoGara2;

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
				GaraVO oggettoGara5= (GaraVO) esaGare.get("richOff");
				oggettoGara5.getBid().setCodice(bid);
				if ( titolo != null) {
					oggettoGara5.getBid().setDescrizione(titolo);
				}
				esaGare.set("richOff",oggettoGara5);
				oggettoGara=oggettoGara5;
			}


			esaGare.set("richOff",oggettoGara);
			int totGare=0;
			if (oggettoGara.getDettaglioPartecipantiGara()!=null && oggettoGara.getDettaglioPartecipantiGara().size()>0)
			{
				totGare=oggettoGara.getDettaglioPartecipantiGara().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			esaGare.set("numRighePartecipanti",totGare);

			PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[totGare];
			for (int i=0; i<totGare; i++)
			{
				//FornitoreVO eleRicForn = (FornitoreVO) oggettoProf.getListaFornitori().get(i);
				//StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i+1),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
				oggettoDettGara[i]=oggettoGara.getDettaglioPartecipantiGara().get(i);
				// esame se nota valorizzata
				if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getNoteAlFornitore().trim().length()>0)
				{
					oggettoDettGara[i].setEsisteNota(true);
				}
				if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getMsgRispDaFornAGara().trim().length()>0)
				{
					oggettoDettGara[i].setEsisteRisp(true);
				}

				//oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
			}
			esaGare.set("elencaRighePartecipanti",oggettoDettGara);


			List arrListaStatoRichiestaOfferta=this.loadStatiRichiestaOfferta();
			esaGare.set("listaStatoRichiestaOfferta",arrListaStatoRichiestaOfferta);

			List arrListaStatoPartecipanteGara=this.loadStatoPartecipante();
			esaGare.set("listaStatoPartecipanteGara",arrListaStatoPartecipanteGara);

			List arrListaTipoInvio=this.loadTipoInvio();
			esaGare.set("listaTipoInvio",arrListaTipoInvio);

			esaGare.set("enableScorrimento",esaGareNorm.isEnableScorrimento());
			// gara chiusa
			if (oggettoGara.getStatoRicOfferta().equals("2"))
			{
				// ordine pagamento emesso
				// disabilitazione input
				((EsaminaGaraForm) esaGare).setDisabilitaTutto(true);
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

	public ActionForward documenti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;

// 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
		return mapping.findForward("docum");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward suggerimenti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;

// 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
		return mapping.findForward("suggBibl");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward fornitore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		DynaValidatorForm esaGare  = (DynaValidatorForm) form;

		try {
			// CONTROLLO CHECK DI RIGA
			Integer[] radioPartecipante=(Integer[])esaGare.get("radioPartecipante");
			if (radioPartecipante.length!=0)
			{
				int indiceRigaIns= radioPartecipante[0]; // unico check
			}
/*			else
			{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.buoniOrdine.crea"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			}
*/
			// aggiorno le righe eventuali

			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheGara.length; w++)
			{
				PartecipantiGaraVO elem = elencaRigheGara[w];
				lista.add(elem);
			}
			eleGara.setDettaglioPartecipantiGara(lista);
			esaGare.set("richOff",eleGara);


			// impongo il profilo,  il paese
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

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;

// 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;

		try {
			// se stato gara aperto è consentito inserire partecipanti
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			if (eleGara!=null && eleGara.getStatoRicOfferta()!=null && !eleGara.getStatoRicOfferta().equals("1"))
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.gareInsImposs"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
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
				//dettGara.setCodice1(String.valueOf(elencaRigheGara.length+1));
				listaDettGara[elencaRigheGara.length]=dettGara;
				esaGare.set("elencaRighePartecipanti",listaDettGara);
				esaGare.set("numRighePartecipanti",listaDettGara.length);
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
					esaGare.set("radioPartecipante",radioPartecipante);
				}

			}
			else
			{
				elencaRigheGara=new PartecipantiGaraVO[1];
				elencaRigheGara[0]=dettGara;
				esaGare.set("elencaRighePartecipanti",elencaRigheGara);
				// gestione selezione della riga in inserimento
				Integer[]  radioPartecipante=new Integer[1];
				if (elencaRigheGara!=null && elencaRigheGara.length>0)
				{
					radioPartecipante[0]=elencaRigheGara.length -1;
					esaGare.set("radioPartecipante",radioPartecipante);
				}

			}
			esaGare.set("numRighePartecipanti",elencaRigheGara.length);
			// trasformazione in arraylist



			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;
		try {

			// se stato gara aperto è consentito cancellare partecipanti
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			if (eleGara!=null && eleGara.getStatoRicOfferta()!=null && !eleGara.getStatoRicOfferta().equals("1"))
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.gareInsImposs"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");

			Integer[] radioPartecipante=(Integer[])esaGare.get("radioPartecipante");
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

					esaGare.set("elencaRighePartecipanti",lista_fin);
					esaGare.set("radioPartecipante", null) ;
					esaGare.set("numRighePartecipanti",lista_fin.length);
					// ricarica la fattura completamente
					GaraVO appoGara=(GaraVO)esaGare.get("richOff");
					List<PartecipantiGaraVO> elencaRigheGaraArr=new ArrayList();
					for (int j=0; j<lista_fin.length; j++)
					{
						elencaRigheGaraArr.add(lista_fin[j]);
					}
					appoGara.setDettaglioPartecipantiGara(elencaRigheGaraArr);
					esaGare.set("richOff",appoGara);

				}
			}
			esaGare.set("radioPartecipante",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
*/

	public ActionForward inserisciForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;

		try {

			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");

			if (eleGara!=null && eleGara.getStatoRicOfferta()!=null && !eleGara.getStatoRicOfferta().equals("1"))
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.gareInsImposs"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheGara.length; w++)
			{
				PartecipantiGaraVO elem = elencaRigheGara[w];
				lista.add(elem);
			}
			eleGara.setDettaglioPartecipantiGara(lista);
			esaGare.set("richOff",eleGara);
			esaGare.set("numRighePartecipanti",elencaRigheGara.length);

			// trasformazione in arraylist

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
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;
		try {

			// se stato gara aperto è consentito cancellare partecipanti
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			if (eleGara!=null && eleGara.getStatoRicOfferta()!=null && !eleGara.getStatoRicOfferta().equals("1"))
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.gareInsImposs"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");

			Integer[] radioPartecipante=(Integer[])esaGare.get("radioPartecipante");
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

					esaGare.set("elencaRighePartecipanti",lista_fin);
					esaGare.set("radioPartecipante", null) ;
					esaGare.set("numRighePartecipanti",lista_fin.length);
					// ricarica la fattura completamente
					GaraVO appoGara=(GaraVO)esaGare.get("richOff");
					List<PartecipantiGaraVO> elencaRigheGaraArr=new ArrayList();
					for (int j=0; j<lista_fin.length; j++)
					{
						elencaRigheGaraArr.add(lista_fin[j]);
					}
					appoGara.setDettaglioPartecipantiGara(elencaRigheGaraArr);
					esaGare.set("richOff",appoGara);

				}
			}
			esaGare.set("radioPartecipante",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//EsaminaGaraForm esaGare = (EsaminaGaraForm) form;
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;
		try {

			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			// trasformo in arraylist
			eleGara.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			String amount=eleGara.getPrezzoIndGaraStr().trim();
		    Double risult=Pulisci.ControllaImporto(amount);
		    eleGara.setPrezzoIndGara(risult);
		    eleGara.setPrezzoIndGaraStr(Pulisci.VisualizzaImporto(eleGara.getPrezzoIndGara()));


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
			esaGare.set("richOff",eleGara);

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
				esaGare.set("richOff",eleGara);
			}





			ActionMessages errors = new ActionMessages();
			((EsaminaGaraForm) esaGare).setConferma(true);
			((EsaminaGaraForm) esaGare).setPressioneBottone("salva");
			((EsaminaGaraForm) esaGare).setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			//this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((EsaminaGaraForm) esaGare).setConferma(false);
			((EsaminaGaraForm) esaGare).setPressioneBottone("");
			((EsaminaGaraForm) esaGare).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		} catch (Exception e) {
			((EsaminaGaraForm) esaGare).setConferma(false);
			((EsaminaGaraForm) esaGare).setPressioneBottone("");
			((EsaminaGaraForm) esaGare).setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGare = (EsaminaGaraForm) form;

		try {

			GaraVO oggettoGara=loadDatiGaraPassata(esaGare.getListaDaScorrere().get(esaGare.getPosizioneScorrimento()));
			esaGare.set("richOff",oggettoGara);
			esaGare.set("numRighePartecipanti",0);

			int totGare=0;
			if (oggettoGara.getDettaglioPartecipantiGara()!=null && oggettoGara.getDettaglioPartecipantiGara().size()>0)
			{
				totGare=oggettoGara.getDettaglioPartecipantiGara().size();
			}
			esaGare.set("numRighePartecipanti",totGare);

			PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[totGare];
			for (int i=0; i<totGare; i++)
			{
				oggettoDettGara[i]=oggettoGara.getDettaglioPartecipantiGara().get(i);
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
			esaGare.set("elencaRighePartecipanti",oggettoDettGara);
			if (oggettoGara.getStatoRicOfferta().equals("2"))
			{
				// ordine pagamento emesso
				// disabilitazione input
				esaGare.setDisabilitaTutto(true);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;
		try {

			PartecipantiGaraVO[] elencaRigheGara= (PartecipantiGaraVO[]) esaGare.get("elencaRighePartecipanti");
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			// trasformo in arraylist
			eleGara.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			String amount=eleGara.getPrezzoIndGaraStr().trim();
		    Double risult=Pulisci.ControllaImporto(amount);
		    eleGara.setPrezzoIndGara(risult);
		    eleGara.setPrezzoIndGaraStr(Pulisci.VisualizzaImporto(eleGara.getPrezzoIndGara()));


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

				lista.add(elencaRigheGara[w]);
			}
			eleGara.setDettaglioPartecipantiGara(lista);
			esaGare.set("richOff",eleGara);
			esaGare.set("elencaRighePartecipanti",elencaRigheGara);

			((EsaminaGaraForm) esaGare).setConferma(false);
			((EsaminaGaraForm) esaGare).setDisabilitaTutto(false);

			if (((EsaminaGaraForm) esaGare).getPressioneBottone().equals("salva")) {
				((EsaminaGaraForm) esaGare).setPressioneBottone("");


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
			if (((EsaminaGaraForm) esaGare).getPressioneBottone().equals("cancella")) {
				((EsaminaGaraForm) esaGare).setPressioneBottone("");
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
						// disabilitazione input dopo la cancellazione
						((EsaminaGaraForm) esaGare).setDisabilitaTutto(true);
					}
			}
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((EsaminaGaraForm) esaGare).setConferma(false);
			((EsaminaGaraForm) esaGare).setPressioneBottone("");
			((EsaminaGaraForm) esaGare).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			((EsaminaGaraForm) esaGare).setConferma(false);
			((EsaminaGaraForm) esaGare).setPressioneBottone("");
			((EsaminaGaraForm) esaGare).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGare = (EsaminaGaraForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaGare.setConferma(false);
			esaGare.setPressioneBottone("");
    		esaGare.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private boolean modificaGara(GaraVO gara) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaGara(gara);
		return valRitorno;
	}

	private boolean cancellaGara(GaraVO gara) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaGara(gara);
		return valRitorno;
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGare = (EsaminaGaraForm) form;
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;

		try {
			int attualePosizione=esaGareNorm.getPosizioneScorrimento()+1;
			int dimensione=esaGareNorm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{

					try
					{
						GaraVO oggettoGara=loadDatiGaraPassata(esaGare.getListaDaScorrere().get(attualePosizione));
						esaGare.set("richOff",oggettoGara);
						esaGare.set("numRighePartecipanti",0);

						int totGare=0;
						if (oggettoGara.getDettaglioPartecipantiGara()!=null && oggettoGara.getDettaglioPartecipantiGara().size()>0)
						{
							totGare=oggettoGara.getDettaglioPartecipantiGara().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						esaGare.set("numRighePartecipanti",totGare);

						PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[totGare];
						for (int i=0; i<totGare; i++)
						{
							oggettoDettGara[i]=oggettoGara.getDettaglioPartecipantiGara().get(i);
							// esame se nota valorizzata
							if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getNoteAlFornitore().trim().length()>0)
							{
								oggettoDettGara[i].setEsisteNota(true);
							}
							if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getMsgRispDaFornAGara().trim().length()>0)
							{
								oggettoDettGara[i].setEsisteRisp(true);
							}
						}
						esaGare.set("elencaRighePartecipanti",oggettoDettGara);
						if (oggettoGara.getStatoRicOfferta().equals("2"))
						{
							// ordine pagamento emesso
							// disabilitazione input
							esaGare.setDisabilitaTutto(true);
						}
						else
						{
							// ordine pagamento emesso
							// disabilitazione input
							esaGare.setDisabilitaTutto(false);
						}
						if (esaGare.isEsaminaInibito())
						{
							esaGare.setDisabilitaTutto(true);
						}

					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							esaGare.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}

					esaGareNorm.setPosizioneScorrimento(attualePosizione);
					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGare = (EsaminaGaraForm) form;
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;

		try {
			int attualePosizione=esaGareNorm.getPosizioneScorrimento()-1;
			int dimensione=esaGareNorm.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						//this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione));
						GaraVO oggettoGara=loadDatiGaraPassata(esaGare.getListaDaScorrere().get(attualePosizione));
						esaGare.set("richOff",oggettoGara);
						esaGare.set("numRighePartecipanti",0);
						int totGare=0;
						if (oggettoGara.getDettaglioPartecipantiGara()!=null && oggettoGara.getDettaglioPartecipantiGara().size()>0)
						{
							totGare=oggettoGara.getDettaglioPartecipantiGara().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						esaGare.set("numRighePartecipanti",totGare);

						PartecipantiGaraVO[] oggettoDettGara=new PartecipantiGaraVO[totGare];
						for (int i=0; i<totGare; i++)
						{
							oggettoDettGara[i]=oggettoGara.getDettaglioPartecipantiGara().get(i);
							// esame se nota valorizzata
							if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getNoteAlFornitore().trim().length()>0)
							{
								oggettoDettGara[i].setEsisteNota(true);
							}
							if (oggettoDettGara[i].getNoteAlFornitore()!=null && oggettoDettGara[i].getMsgRispDaFornAGara().trim().length()>0)
							{
								oggettoDettGara[i].setEsisteRisp(true);
							}

						}
						esaGare.set("elencaRighePartecipanti",oggettoDettGara);

						if (oggettoGara.getStatoRicOfferta().equals("2"))
						{
							// ordine pagamento emesso
							// disabilitazione input
							esaGare.setDisabilitaTutto(true);
						}
						else
						{
							// ordine pagamento emesso
							// disabilitazione input
							esaGare.setDisabilitaTutto(false);
						}
						if (esaGare.isEsaminaInibito())
						{
							esaGare.setDisabilitaTutto(true);
						}

					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							esaGare.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}


					esaGareNorm.setPosizioneScorrimento(attualePosizione);

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGare = (EsaminaGaraForm) form;

		try {
			ActionMessages errors = new ActionMessages();
			esaGare.setConferma(true);
			esaGare.setPressioneBottone("cancella");
    		esaGare.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			esaGare.setConferma(false);
			esaGare.setPressioneBottone("");
    		esaGare.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	private GaraVO loadDatiGaraPassata(ListaSuppGaraVO criteriRicercaGara) throws Exception {

		criteriRicercaGara.setStatoRicOfferta(""); // lo stato potrebbe essere stato cambiato in modifica
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<GaraVO> gareTrovate = new ArrayList();
		gareTrovate = factory.getGestioneAcquisizioni().getRicercaListaGare(criteriRicercaGara);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaBilanci.setBilancio(bilanciTrovato.get(0));
		//for (int i=0; i<bilanciTrovato.size(); i++)
		//this.esaBilanci.setListaImpegni(bilanciTrovato.get(0).getDettagliBilancio());
		GaraVO gar=gareTrovate.get(0);
		try {
			gar.setPrezzoIndGaraStr(Pulisci.VisualizzaImporto( gar.getPrezzoIndGara()));
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			gar.setPrezzoIndGaraStr("0,00");
		}

		return gar;
	}


/*	private void loadRichiestaOfferta() throws Exception {
		// caricamento richiesta
		GaraVO richOff=new GaraVO("X10", "01", new StrutturaCombo("LO10338841", "4: Arbo-asse. Istituto G. Treccani - Roma, 1929 - XVI, 999 p., 182 tav.: ill.; 32 cm."), "00000011", "06/02/2006", "1", 300.00, 2, "" );
		esaGare.setRichOff(richOff);

		// caricamento righe partecipanti
		List lista = new ArrayList();
		PartecipantiGaraVO partecip= new PartecipantiGaraVO("X10", "01",new StrutturaCombo("33", "Libreria Feltrinelli"), "A", "00000011", "note", "07/02/2006" , "P", "risposta" );
		lista.add(partecip);
		partecip= new PartecipantiGaraVO("X10", "01",new StrutturaCombo("22", "Libreria Grande"), "P", "00000012", "note 2", "08/02/2006" , "P", "risposta 2" );
		lista.add(partecip);
		esaGare.setListaPartecipantiGara(lista);
	}*/
	private List loadStatoPartecipante() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("A","A - ATTESA");
		lista.add(elem);
		elem = new StrutturaCombo("P","P - PERDENTE");
		lista.add(elem);
		elem = new StrutturaCombo("V","V - VINCITORE");
		lista.add(elem);
		//esaGare.setListaStatoPartecipanteGara(lista);
		return lista;

	}
	private List loadStatiRichiestaOfferta() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("1","Aperta");
		lista.add(elem);
		elem = new StrutturaCombo("2","Chiusa");
		lista.add(elem);
//		elem = new StrutturaCombo("3","Annullata");
//		lista.add(elem);
		elem = new StrutturaCombo("4","Ordinata");
		lista.add(elem);
		//esaGare.setListaStatoRichiestaOfferta(lista);
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
		//esaGare.setListaTipoInvio(lista);
		return lista;
	}
	public ActionForward documentiCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;

		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppDocumentoVO")==null
			request.getSession().removeAttribute("attributeListaSuppDocumentoVO");
			request.getSession().removeAttribute("documentiSelected");
			request.getSession().removeAttribute("criteriRicercaDocumento");

/*			if (request.getSession().getAttribute("criteriRicercaDocumento")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppDocumentoVO");
				request.getSession().removeAttribute("documentiSelected");
				request.getSession().removeAttribute("criteriRicercaDocumento");
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
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			this.impostaDocumentiCerca(request,mapping,eleGara);
			return mapping.findForward("docum");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaDocumentiCerca( HttpServletRequest request, ActionMapping mapping , GaraVO garaPass)
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
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppDocumentoVO", eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward suggerimentiCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;

		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppSuggerimentoVO")==null
			request.getSession().removeAttribute("attributeListaSuppSuggerimentoVO");
			request.getSession().removeAttribute("suggerimentiSelected");
			request.getSession().removeAttribute("criteriRicercaSuggerimento");

/*			if (request.getSession().getAttribute("criteriRicercaSuggerimento")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppSuggerimentoVO");
				request.getSession().removeAttribute("suggerimentiSelected");
				request.getSession().removeAttribute("criteriRicercaSuggerimento");
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
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
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
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);
		// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
		request.getSession().setAttribute("attributeListaSuppSuggerimentoVO", eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaGaraForm esaGareNorm = (EsaminaGaraForm) form;
		DynaValidatorForm esaGare  = (DynaValidatorForm) form;

		try {
			GaraVO eleGara=(GaraVO) esaGare.get("richOff");
			if (eleGara.getBid()!=null &&eleGara.getBid().getCodice()!=null)
			{
				request.setAttribute("bidFromRicOrd", eleGara.getBid().getCodice());
			}
			return mapping.findForward("sifbid");
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("GESTIONE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_GARE_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return false;
	}

/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		EsaminaGaraForm esaGare = (EsaminaGaraForm) form;

		if (request.getParameter("insRiga0") != null) {
			List lista_new = esaGare.getListaPartecipantiGara();
			int numeroRiga=esaGare.getListaPartecipantiGara().size() + 1;
			PartecipantiGaraVO partecip= new PartecipantiGaraVO("", "",new StrutturaCombo("", ""), "A", "", "", "" , "", "" );
			lista_new.add(partecip);
			esaGare.setListaPartecipantiGara(lista_new);
			esaGare.setNumRighePartecipanti(esaGare.getListaPartecipantiGara().size());
			esaGare.setRadioPartecipante(0);
			return mapping.getInputForward();

	}
	if (request.getParameter("cancRiga0") != null) {

		if ( esaGare.getRadioPartecipante() >= 0 && esaGare.getListaPartecipantiGara().size()!=0 )
		{
			List lista_new = esaGare.getListaPartecipantiGara();
			lista_new.remove(esaGare.getRadioPartecipante());
			esaGare.setListaPartecipantiGara(lista_new);
			esaGare.setNumRighePartecipanti(esaGare.getListaPartecipantiGara().size());
			esaGare.setRadioPartecipante(0);

			return mapping.getInputForward();
		}
		else
		{

			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.buoniOrdine.crea"));
			this.saveErrors(request, errors);

		}
	}

		if (request.getParameter("suggBibl0") != null) {
			return mapping.findForward("suggBibl");
		}

		if (request.getParameter("docum0") != null) {
			return mapping.findForward("docum");
		}


		if (request.getParameter("salva0") != null) {
			//return mapping.findForward("salva");
		}

		if (request.getParameter("ripristina0") != null) {
			//return mapping.findForward("ripristina");
		}

		if (request.getParameter("cancella0") != null) {
			//return mapping.findForward("cancella");
		}


		if (request.getParameter("indietro1") != null) {
			return mapping.findForward("indietro");
		}

		this.loadRichiestaOfferta();
		this.loadStatiRichiestaOfferta();
		this.loadStatoPartecipante();
		this.loadTipoInvio();
		esaGare.setStatoRichiestaOfferta(esaGare.getRichOff().getStatoRicOfferta());
		esaGare.setNumRighePartecipanti(esaGare.getListaPartecipantiGara().size());

		return mapping.getInputForward();
	}*/

}
