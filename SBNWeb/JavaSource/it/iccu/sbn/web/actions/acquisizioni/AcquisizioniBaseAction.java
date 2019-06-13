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
package it.iccu.sbn.web.actions.acquisizioni;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class AcquisizioniBaseAction extends SinteticaLookupDispatchAction {

	/**
	  * it.iccu.sbn.web.actions.acquisizioni.ordini
	  * OrdineRicercaParzialeAction.java
	  * fornitoreCercaVeloce
	  * ActionForward
	  * @param mapping
	  * @param request
	  * @param ricOrdini
	  * @return
	  *
	  *
	 */
	protected ActionForward fornitoreCercaVeloce(ActionMapping mapping, HttpServletRequest request, AcquisizioniBaseFormIntf myForm) {
		try{
			FornitoreVO fornVO = null;
			if (myForm.getCodFornitore() != null &&
					myForm.getCodFornitore().equals("")){
				if (myForm.getFornitore() != null &&
						myForm.getFornitore().equals("")){
					//sif
				}else{
					//cerco il fornitore per nome
					fornVO = this.getFornitore(Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(),
							null, myForm.getFornitore(), Navigation.getInstance(request).getUserTicket());
					if (fornVO != null){
						myForm.setFornitoreVO(fornVO);
						myForm.setCodFornitore(fornVO.getCodFornitore().toString());
						myForm.setFornitore(fornVO.getNomeFornitore());
						return mapping.getInputForward();
					}else{
						//sif
					}
				}
			}else{
				//cerco il fornitore per cod
				fornVO = this.getFornitore(Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(),
						myForm.getCodFornitore(), null, Navigation.getInstance(request).getUserTicket());
				if (fornVO != null){
					myForm.setFornitoreVO(fornVO);
					myForm.setCodFornitore(fornVO.getCodFornitore().toString());
					myForm.setFornitore(fornVO.getNomeFornitore());
					return mapping.getInputForward();
				}else{
					//sif
				}
			}
		} catch (DataException ve) {
			if (ve.getMessage() != null && ve.getMessage().equals("recSezioneInesistente")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultati"));
				this.saveErrors(request, errors);
			}else if (ve.getMessage() != null && ve.getMessage().equals("mancanoEstremiPerRicercaFornitore")){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni.mancanoEstremiPerRicercaFornitore"));
					this.saveErrors(request, errors);
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.trovatoPiuDiUnRecordConNomeFornitoreIndicato"));
				this.saveErrors(request, errors);
			}
		}catch (Exception e) { // altri tipi di errore
			return mapping.getInputForward();
		}
		return null;
	}

	private FornitoreVO getFornitore(String codPolo, String codBib,
			String codFornitore, String descr, String ticket) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		FornitoreVO rec = factory.getGestioneAcquisizioni().getFornitore(codPolo, codBib, codFornitore, descr, ticket);
		return rec;
	}

	protected ListaSuppBuoniOrdineVO preparaRicercaBuonoSingle(List<BuoniOrdineVO> buoniOrdine, HttpServletRequest request, int j)	{
		try {
			ListaSuppBuoniOrdineVO ricerca = new ListaSuppBuoniOrdineVO();
			Navigation navi = Navigation.getInstance(request);
			String ticket = navi.getUserTicket();

			BuoniOrdineVO buono = buoniOrdine.get(j);

			// carica i criteri di ricerca da passare alla esamina
			String codP = navi.getUtente().getCodPolo();
			String codB = buono.getCodBibl();
			String numDa = buono.getNumBuonoOrdine();
			String numA = buono.getNumBuonoOrdine();
			String dataDa = buono.getDataBuonoOrdine();
			String dataA = buono.getDataBuonoOrdine();
			String stato = "";
			// StrutturaTerna ord=eleElenco.getListaOrdiniBuono();
			// StrutturaCombo forn=eleElenco.getFornitore();
			// StrutturaTerna bil=eleElenco.getBilancio();
			StrutturaTerna ord = new StrutturaTerna("", "", "");
			StrutturaCombo forn = new StrutturaCombo("", "");
			StrutturaTerna bil = new StrutturaTerna("", "", "");
			String chiama = null;
			String ordina = "";

			ricerca = new ListaSuppBuoniOrdineVO(codP, codB, numDa, numA,
					dataDa, dataA, stato, ord, forn, bil, chiama, ordina);
			// aggiorna ticket
			ricerca.setTicket(ticket);

			request.getSession().setAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE,
					ValidazioneDati.asSingletonList(ricerca));

			return ricerca;

		} catch (Exception e) {	}

		return null;
	}

}
