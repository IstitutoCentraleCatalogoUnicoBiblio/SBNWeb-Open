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
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.EsameStoricoForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

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

public class EsameStoricoAction extends  SinteticaLookupDispatchAction {
	//private EsaminaSezioneForm esaSezione;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
/*		map.put("ricerca.button.elabora","elabora");*/
		map.put("ricerca.button.indietro","indietro");
		return map;
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				EsameStoricoForm esaStoria = (EsameStoricoForm) form;

				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!esaStoria.isSessione())
				{
					esaStoria.setSessione(true);
				}

				SezioneVO sezioneInEsame =null;
				sezioneInEsame = (SezioneVO) request.getSession().getAttribute("sezioneEsaminata2");
				//gestire l'esistenza del risultato e che sia univoco
				int iDsezioneInEsame=0;
				iDsezioneInEsame = (Integer) request.getSession().getAttribute("sezioneEsaminata");
				if (iDsezioneInEsame!=0)
				{
					this.loadSezione(esaStoria, request);
				}

				if (sezioneInEsame!=null)
				{
					esaStoria.setSezione(sezioneInEsame);
				}
				esaStoria.setNumBilanci(0);
				if (esaStoria.getSezione()!=null &&  esaStoria.getSezione().getRigheEsameStoria()!=null && esaStoria.getSezione().getRigheEsameStoria().size()>0)
				{
					esaStoria.setNumBilanci( esaStoria.getSezione().getRigheEsameStoria().size());
				}
				if (esaStoria.getNumBilanci()==0)
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.noVariazioniBudget"));
					this.saveErrors(request, errors);

				}
/*				else
				{
					ActionMessages errors = new ActionMessages();
					//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
					errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
					this.saveErrors(request, errors);
				}
*/
				//this.loadSezione(esaStoria, request);
				return mapping.getInputForward();
		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
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
		EsameStoricoForm esaStoria = (EsameStoricoForm) form;
		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
/*	public ActionForward elabora(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsameStoricoForm esaStoria = (EsameStoricoForm) form;
		try {
			//metto in old il valore del budget di sezione originario
			double oldSommaDispSezione =esaStoria.getSezione().getSommaDispSezione();
			String oldSommaDispSezioneStr ="";
			if (esaStoria.getSezione().getSommaDispSezioneStr()!=null)
			{
				oldSommaDispSezioneStr =esaStoria.getSezione().getSommaDispSezioneStr();
			}

			this.loadSezione(esaStoria, request);

			// se è gestito il bilancio azzerare il num delle tipologie
			if (esaStoria.getSezione().getRigheEsameSpesa()!=null && esaStoria.getSezione().getRigheEsameSpesa().size()>0 )
			{
				if (!esaStoria.getSezione().getRigheEsameSpesa().get(0).getTipologia().equals("") && esaStoria.getSezione().getRigheEsameSpesa().get(0).getImpegno().equals(""))
				{
					// altrimenti ..... viceversa azzerare il num dei bilanci
					esaStoria.setListaTipologie(esaStoria.getSezione().getRigheEsameSpesa());
					esaStoria.setNumTipologie(esaStoria.getListaTipologie().size());
					esaStoria.setNumBilanci(0);
					if 	(esaStoria.getNumTipologie()==0)
					{
						ActionMessages errors = new ActionMessages();
						//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
						errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));
						this.saveErrors(request, errors);
					}
				}
				if (!esaStoria.getSezione().getRigheEsameSpesa().get(0).getImpegno().equals("") && esaStoria.getSezione().getRigheEsameSpesa().get(0).getTipologia().equals(""))
				{
					esaStoria.setListaBilanci(esaStoria.getSezione().getRigheEsameSpesa());
					esaStoria.setNumBilanci(esaStoria.getListaBilanci().size());
					esaStoria.setNumTipologie(0);
					if 	(esaStoria.getNumBilanci()==0)
					{
						ActionMessages errors = new ActionMessages();
						//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
						errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));
						this.saveErrors(request, errors);
					}

				}
			}
			else
			{
				esaStoria.setNumTipologie(0);
				esaStoria.setNumBilanci(0);
				ActionMessages errors = new ActionMessages();
				//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
				errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));
				this.saveErrors(request, errors);
			}
			// la disponibilità della sezione non deve essere elaborata sui sottocriteri (deve essere quella originaria)
			if (oldSommaDispSezioneStr!=null)
			{
				esaStoria.getSezione().setSommaDispSezioneStr(oldSommaDispSezioneStr);
			}

			esaStoria.getSezione().setSommaDispSezione(oldSommaDispSezione);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}*/



/*	private void loadTipologie(EsameStoricoForm esaStoria ) throws Exception {
		try {
			// riempimento statico
			List lista=new ArrayList();
			StrutturaTerna tip = new StrutturaTerna("Ordine","770","750");
			lista.add(tip);
			tip = new StrutturaTerna("Abbonamento","400","400");
			lista.add(tip);
			tip = new StrutturaTerna("Ordine continuativo","490","1490");
			lista.add(tip);
			esaStoria.setTipologie(lista);
			esaStoria.setNumTipologie(lista.size());

		} catch (Exception e) {
			e.printStackTrace();

		}
	}*/
	private void loadSezione(EsameStoricoForm esaStoria, HttpServletRequest request ) throws Exception {
		try {
			// riempimento statico
			//String codP, String codB, String codSez, String desSez , double sommaSez, String noteSez, String annoValSez, double bdgSez, String tipoVar
			int IDSez= (Integer) request.getSession().getAttribute("sezioneEsaminata");
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<SezioneVO> sezioneTrovata = new ArrayList();
			// gestire la lista supporto impostat con l'id
			ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			//String codB=Navigation.getInstance(request).getUtente().getCodBib();
			String codB="";
			String codSez="";
			String desSez="";
			String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
			eleRicerca.setIdSezione(IDSez);
			eleRicerca.setStoria(true);
			sezioneTrovata = factory.getGestioneAcquisizioni().getRicercaListaSezioni(eleRicerca);
			//gestire l'esistenza del risultato e che sia univoco
			esaStoria.setSezione(sezioneTrovata.get(0));

			//esaStoria.setNumImpegni(esaStoria.getListaBilanci2().size());

/*			try {
				esaStoria.getSezione().setBudgetSezioneStr(Pulisci.VisualizzaImporto( esaStoria.getSezione().getBudgetSezione()));
			} catch (Exception e) {
			    	//e.printStackTrace();
			    	//throw new ValidationException("importoErrato",
			    	//		ValidationExceptionCodici.importoErrato);
				esaStoria.getSezione().setBudgetSezioneStr("0,00");
			}
			SezioneVO sez=new SezioneVO("CSW"," FI", "CONS", "CONSULTAZIONE", 900.00, "", "2007",1000.00,"" );
			esaStoria.setSezione(sez);
*/		} catch (Exception e) {
		e.printStackTrace();
		}
	}

}
