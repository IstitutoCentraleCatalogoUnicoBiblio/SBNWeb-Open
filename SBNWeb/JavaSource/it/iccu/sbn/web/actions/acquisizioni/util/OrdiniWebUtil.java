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
package it.iccu.sbn.web.actions.acquisizioni.util;

import static org.hamcrest.Matchers.equalTo;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.acquisizioni.OrdiniUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.FormulaIntroOrdineRVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import ch.lambdaj.group.Group;

public class OrdiniWebUtil extends OrdiniUtil {

	public static final List<TB_CODICI> estraiCodiciLavorazioneOrdine() throws Exception {
		List<TB_CODICI> output = new ArrayList<TB_CODICI>();
		output.add(new TB_CODICI("", ""));

		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_NON_DISPONIBILITA);
		for (TB_CODICI cod : codici) {
			if (!ValidazioneDati.in(cod.getCd_flg2(), "S", "s") )
				continue;

			String descr = cod.getCd_flg3();	//descrizione
			output.add(new TB_CODICI(cod.getCd_tabella(), ValidazioneDati.isFilled(descr) ? descr : cod.getDs_tabella()));
		}

		return output;
	}

	public static final boolean aggiornaDisponibilitaInventari(HttpServletRequest request, OrdiniVO ordine, List<StrutturaInventariOrdVO> inventari) {
		try {
			List<StrutturaQuinquies> listaPerDF = new ArrayList<StrutturaQuinquies>();
			for (StrutturaInventariOrdVO inv : inventari) {
				StrutturaQuinquies eleListPerDF = new StrutturaQuinquies();
				eleListPerDF.setCodice1(inv.getSerie());
				eleListPerDF.setCodice2(inv.getNumero());
				eleListPerDF.setCodice3(inv.getDataRientroPresunta());
				// esame se data rientro valorizzata
				if (ValidazioneDati.isFilled(inv.getDataRientro()) )
					eleListPerDF.setCodice4("R");
				else
					eleListPerDF.setCodice4(null);

				//almaviva5_20130910 evolutive google2
				eleListPerDF.setCodice5(Boolean.toString(inv.isFlag_canc()));
				//

				listaPerDF.add(eleListPerDF);
			}

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			AggDispVO richiesta = new AggDispVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

			richiesta.setCodPolo(ordine.getCodPolo());
			richiesta.setCodBib(ordine.getCodBibl());
			richiesta.setCodAttivita(CodiciAttivita.getIstance().GDF_AGGIORNAMENTO_DISPONIBILITA_INVENTARI);
			richiesta.setListaInventari(listaPerDF);

			//almaviva5_20121116 evolutive google
			String cd_tipo_lav = ordine.getCd_tipo_lav();
			richiesta.setCodNoDispo(cd_tipo_lav);
			TB_CODICI c = CodiciProvider.cercaCodice(cd_tipo_lav, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
			if (c == null)
				throw new ApplicationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			richiesta.setCodDigitalizzazione(ValidazioneDati.trimOrEmpty(c.getCd_flg4()));

			String ticket = Navigation.getInstance(request).getUserTicket();

			return factory.getGestioneAcquisizioni().getInventarioBean().aggiornaInventarioDisponibilita(richiesta, ticket);

		} catch (Exception e) {
			if (e.getMessage().equals("inventarioDismesso")) // tck 3732
				LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.inventarioDismesso"));
		}

		return false;
	}

	public static void riposiziona(int pos1, int pos2, List<StrutturaInventariOrdVO> ii)  throws Exception {

		StrutturaInventariOrdVO p = on(StrutturaInventariOrdVO.class);
		StrutturaInventariOrdVO i1 = ii.get(pos1);
		StrutturaInventariOrdVO i2 = ii.get(pos2);

		short p1 = i1.getPosizione().shortValue();
		short p2 = i2.getPosizione().shortValue();
		short v1 = i1.getVolume().shortValue();
		short v2 = i2.getVolume().shortValue();

		if (v1 == v2) {
			i1.setPosizione(p2);
			i2.setPosizione(p1);
			Collections.swap(ii, pos1, pos2);
			return;
		}

		List<StrutturaInventariOrdVO> iiv1 = select(ii, having(p.getVolume(), equalTo(v1)));
		List<StrutturaInventariOrdVO> iiv2 = select(ii, having(p.getVolume(), equalTo(v2)));

		if (pos2 > pos1) {
			//scorrimento down
			short start1 = (ValidazioneDati.first(iiv1).getPosizione() );
			short start2 = (short) (ValidazioneDati.first(iiv1).getPosizione() + iiv2.size());

			for (StrutturaInventariOrdVO i : iiv1 ) {
				i.setVolume(v2);
				i.setPosizione(start2++);
			}

			for (StrutturaInventariOrdVO i : iiv2 ) {
				i.setVolume(v1);
				i.setPosizione(start1++);
			}

			Collections.sort(ii, StrutturaInventariOrdVO.ORDINAMENTO_POSIZIONE_VOLUME);
			return;

		} else {
			//scorrimento up
			short start1 = (short) (ValidazioneDati.first(iiv2).getPosizione() + iiv1.size());
			short start2 = ValidazioneDati.first(iiv2).getPosizione();
			for (StrutturaInventariOrdVO i : iiv1) {
				i.setVolume(v2);
				i.setPosizione(start2++);
			}

			for (StrutturaInventariOrdVO i : iiv2 ) {
				i.setVolume(v1);
				i.setPosizione(start1++);
			}

			Collections.sort(ii, StrutturaInventariOrdVO.ORDINAMENTO_POSIZIONE_VOLUME);
			return;

		}

	}

	public static void rinumera(List<StrutturaInventariOrdVO> ii) throws Exception {

		StrutturaInventariOrdVO p = on(StrutturaInventariOrdVO.class);
		Group<StrutturaInventariOrdVO> volumi = group(ii, by(p.getVolume()));
		Short pos = 0;
		Short vol = 0;
		for (Group<StrutturaInventariOrdVO> gv : volumi.subgroups()) {
			vol++;
			for (StrutturaInventariOrdVO inv : gv.findAll()) {
				inv.setPosizione(++pos);
				inv.setVolume(vol);
			}
		}
	}


	public static List<StrutturaInventariOrdVO> selezionaVolume(List<StrutturaInventariOrdVO> ii, Short v) throws Exception {
		StrutturaInventariOrdVO p = on(StrutturaInventariOrdVO.class);
		return select(ii, having(p.getVolume(), equalTo(v)));
	}


	public static void main(String[] args) throws Exception {
		FormulaIntroOrdineRVO combo = new FormulaIntroOrdineRVO("ITA", "G", "Si prega di fornire un numero sufficiente di supporti CD/DVD");
		String xml = ClonePool.marshal((Serializable) ValidazioneDati.asSingletonList(combo) );
		System.out.println(xml);
		Serializable o = ClonePool.unmarshal(xml);
		System.out.println(o);
	}

}
