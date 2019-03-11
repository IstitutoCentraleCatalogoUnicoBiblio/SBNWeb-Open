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
package it.iccu.sbn.web.integration.bd.gestioneservizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaPrenotazioniPostoInterceptor implements DescrittoreBloccoInterceptor {

	private Servizi servizi;
	private String ticket;

	public ListaPrenotazioniPostoInterceptor(String ticket) {
		this.ticket = ticket;
		try {
			servizi = DomainEJBFactory.getInstance().getServizi();
		} catch (Exception e) {	}
	}

	public void intercept(DescrittoreBloccoVO blocco) throws Exception {
		List<PrenotazionePostoVO> prenotazioni = blocco.getLista();
		for (PrenotazionePostoVO pp : prenotazioni) {
			List<MovimentoVO> movimenti = new ArrayList<MovimentoVO>();
			for (MovimentoVO mov : pp.getMovimenti()) {
				movimenti.add(servizi.getMovimentoListaVO(ticket, mov, Locale.getDefault()) );
			}
			pp.setMovimenti(movimenti);
		}

	}

}
