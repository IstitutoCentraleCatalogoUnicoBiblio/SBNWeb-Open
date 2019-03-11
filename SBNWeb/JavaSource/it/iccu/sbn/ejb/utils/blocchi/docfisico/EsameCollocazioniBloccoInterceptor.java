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
package it.iccu.sbn.ejb.utils.blocchi.docfisico;

import it.iccu.sbn.ejb.domain.documentofisico.Collocazione;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneUltKeyLocVO;

import java.util.List;

public class EsameCollocazioniBloccoInterceptor implements DescrittoreBloccoInterceptor {

	private Collocazione collocazione;
	private String ticket;

	public EsameCollocazioniBloccoInterceptor(String ticket, Collocazione collocazione) {
		this.ticket = ticket;
		this.collocazione = collocazione;
	}

	public void intercept(DescrittoreBloccoVO blocco) throws Exception {
		List<CollocazioneUltKeyLocVO> lista = blocco.getLista();
		int size = ValidazioneDati.size(lista);
		for (int idx = 0; idx < size; idx++) {
			//lettura completa titolo di collocazione
			CollocazioneUltKeyLocVO coll = lista.get(idx);
			CollocazioneUltKeyLocVO elem = collocazione.creaElementoListaCollocazioni(coll.getKeyColloc(), ticket, coll.getPrg() );
			lista.remove(idx);
			lista.add(idx, elem);
		}
	}

}
