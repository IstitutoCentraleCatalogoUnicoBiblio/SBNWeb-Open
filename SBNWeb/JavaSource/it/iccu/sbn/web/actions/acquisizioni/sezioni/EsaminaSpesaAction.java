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

import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.EsaminaSpesaForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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

public class EsaminaSpesaAction extends  SinteticaLookupDispatchAction {
	//private EsaminaSezioneForm esaSezione;

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.elabora","elabora");
		map.put("ricerca.button.indietro","indietro");
		return map;
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				EsaminaSpesaForm esaSpesa = (EsaminaSpesaForm) form;

				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();
				if(!esaSpesa.isSessione())
				{
					esaSpesa.setSessione(true);
				}

				SezioneVO sezioneInEsame =null;
				sezioneInEsame = (SezioneVO) request.getSession().getAttribute("sezioneEsaminata2");
				//gestire l'esistenza del risultato e che sia univoco
				int iDsezioneInEsame=0;
				iDsezioneInEsame = (Integer) request.getSession().getAttribute("sezioneEsaminata");
				if (iDsezioneInEsame!=0)
				{
					this.loadSezione(esaSpesa, request);
				}

				if (sezioneInEsame!=null)
				{
					esaSpesa.setSezione(sezioneInEsame);
				}
/*				else
				{
					ActionMessages errors = new ActionMessages();
					//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
					errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
					this.saveErrors(request, errors);
				}
*/
				//this.loadSezione(esaSpesa, request);
				// controllo dell'abilitazione alla gestione del bilancio

				ConfigurazioneORDVO criteri=new  ConfigurazioneORDVO();
				criteri.setCodBibl(esaSpesa.getSezione().getCodBibl());
				criteri.setCodPolo(esaSpesa.getSezione().getCodPolo());
				ConfigurazioneORDVO confLetto=this.loadConfigurazione(criteri);
				if (confLetto!=null && !confLetto.isGestioneBilancio())
				{
					esaSpesa.setGestBil(false);
				}

				return mapping.getInputForward();
/*		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
*/		// altri tipi di errore
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
		EsaminaSpesaForm esaSpesa = (EsaminaSpesaForm) form;
		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward elabora(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaSpesaForm esaSpesa = (EsaminaSpesaForm) form;
		try {
			if ((esaSpesa.getDataA()!=null && esaSpesa.getDataA().trim().length()>0) || (esaSpesa.getDataDa()!=null && esaSpesa.getDataDa().trim().length()>0) || (esaSpesa.getEsercizio()!=null && esaSpesa.getEsercizio().trim().length()>0))
			{
				//metto in old il valore del budget di sezione originario
				double oldSommaDispSezione =esaSpesa.getSezione().getSommaDispSezione();
				String oldSommaDispSezioneStr ="";
				if (esaSpesa.getSezione().getSommaDispSezioneStr()!=null)
				{
					oldSommaDispSezioneStr =esaSpesa.getSezione().getSommaDispSezioneStr();
				}

				this.loadSezione(esaSpesa, request);

				// se è gestito il bilancio azzerare il num delle tipologie
				if (esaSpesa.getSezione().getRigheEsameSpesa()!=null && esaSpesa.getSezione().getRigheEsameSpesa().size()>0 )
				{
					if (!esaSpesa.getSezione().getRigheEsameSpesa().get(0).getTipologia().equals("") && esaSpesa.getSezione().getRigheEsameSpesa().get(0).getImpegno().equals(""))
					{
						// altrimenti ..... viceversa azzerare il num dei bilanci
						esaSpesa.setListaTipologie(esaSpesa.getSezione().getRigheEsameSpesa());
						esaSpesa.setNumTipologie(esaSpesa.getListaTipologie().size());
						esaSpesa.setNumBilanci(0);
						if 	(esaSpesa.getNumTipologie()==0)
						{
							ActionMessages errors = new ActionMessages();
							//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
							errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));
							this.saveErrors(request, errors);
						}
					}
					if (!esaSpesa.getSezione().getRigheEsameSpesa().get(0).getImpegno().equals("") && esaSpesa.getSezione().getRigheEsameSpesa().get(0).getTipologia().equals(""))
					{
						esaSpesa.setListaBilanci(esaSpesa.getSezione().getRigheEsameSpesa());
						esaSpesa.setNumBilanci(esaSpesa.getListaBilanci().size());
						esaSpesa.setNumTipologie(0);
						if 	(esaSpesa.getNumBilanci()==0)
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
					esaSpesa.setNumTipologie(0);
					esaSpesa.setNumBilanci(0);
					ActionMessages errors = new ActionMessages();
					//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
					errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultatiElaborazione"));
					this.saveErrors(request, errors);
				}
				// la disponibilità della sezione non deve essere elaborata sui sottocriteri (deve essere quella originaria)
				if (oldSommaDispSezioneStr!=null)
				{
					esaSpesa.getSezione().setSommaDispSezioneStr(oldSommaDispSezioneStr);
				}

				esaSpesa.getSezione().setSommaDispSezione(oldSommaDispSezione);
				return mapping.getInputForward();
			}
			else
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.noElabora"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadBilanci(EsaminaSpesaForm esaSpesa ) throws Exception {
		try {
			// riempimento statico
			List lista=new ArrayList();
			BilancioVO bil = new BilancioVO("CSW"," FI","2007", "1",0.00, "" );

			List listaDett=new ArrayList();
			BilancioDettVO bilDett=new BilancioDettVO("1",0.00,400.00,0.00,0.00 );
			bilDett.setAcquisito(300.00);
			listaDett.add(bilDett);
			bilDett=new BilancioDettVO("2",0.00,500.00,0.00,0.00 );
			bilDett.setAcquisito(200.00);
			listaDett.add(bilDett);
			bil.setDettagliBilancio(listaDett);
			lista.add(bil);
			esaSpesa.setListaBilanci(lista);
			int num=0;
			for (int i=0; i<lista.size(); i++)
			{
				BilancioVO uno=(BilancioVO)lista.get(i);
				num=num + uno.getDettagliBilancio().size();
			}
			esaSpesa.setNumImpegni(num);
			esaSpesa.setNumBilanci(lista.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadTipologie(EsaminaSpesaForm esaSpesa ) throws Exception {
		try {
			// riempimento statico
			List lista=new ArrayList();
			StrutturaTerna tip = new StrutturaTerna("Ordine","770","750");
			lista.add(tip);
			tip = new StrutturaTerna("Abbonamento","400","400");
			lista.add(tip);
			tip = new StrutturaTerna("Ordine continuativo","490","1490");
			lista.add(tip);
			esaSpesa.setTipologie(lista);
			esaSpesa.setNumTipologie(lista.size());

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	private ConfigurazioneORDVO loadConfigurazione(ConfigurazioneORDVO configurazioneORD) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		return configurazioneTrovata;
	}


	private void loadSezione(EsaminaSpesaForm esaSpesa, HttpServletRequest request ) throws Exception {
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
			eleRicerca.setOrdinamento("");
			eleRicerca.setDataOrdineA(esaSpesa.getDataA());
			eleRicerca.setDataOrdineDa(esaSpesa.getDataDa());
			eleRicerca.setEsercizio(esaSpesa.getEsercizio());
			eleRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Documento Fisico 09/05/08
			sezioneTrovata = factory.getGestioneAcquisizioni().getRicercaListaSezioni(eleRicerca);
			//gestire l'esistenza del risultato e che sia univoco
			esaSpesa.setSezione(sezioneTrovata.get(0));

			//esaSpesa.setNumImpegni(esaSpesa.getListaBilanci2().size());

/*			try {
				esaSpesa.getSezione().setBudgetSezioneStr(Pulisci.VisualizzaImporto( esaSpesa.getSezione().getBudgetSezione()));
			} catch (Exception e) {
			    	//e.printStackTrace();
			    	//throw new ValidationException("importoErrato",
			    	//		ValidationExceptionCodici.importoErrato);
				esaSpesa.getSezione().setBudgetSezioneStr("0,00");
			}
			SezioneVO sez=new SezioneVO("CSW"," FI", "CONS", "CONSULTAZIONE", 900.00, "", "2007",1000.00,"" );
			esaSpesa.setSezione(sez);
*/		} catch (Exception e) {
		e.printStackTrace();
		}
	}

}
