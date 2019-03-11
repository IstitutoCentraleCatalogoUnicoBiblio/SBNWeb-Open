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
package it.iccu.sbn.web.integration.bd.periodici;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;

import java.util.Map;

public class BibEsemplareBloccoInterceptor implements DescrittoreBloccoInterceptor {

	private final String codBib;
	private final PeriodiciDelegate delegate;

	public BibEsemplareBloccoInterceptor(String codBib, PeriodiciDelegate delegate) {
		this.codBib = codBib;
		this.delegate = delegate;
	}

	public void intercept(DescrittoreBloccoVO blocco) throws Exception {

		try {
			for (Object o : blocco.getLista()) {
				FascicoloDecorator fd = (FascicoloDecorator) o;
				Map<String, Integer> listaBib = delegate.getListaBibliotecheEsemplareFascicolo(fd.getBid(),	fd.getFid());
				if (ValidazioneDati.isFilled(listaBib)) {
					int totPolo = 0;
					for (String bib : listaBib.keySet()) {
						int totBib = listaBib.get(bib);
						totPolo += totBib;
						if (bib.equals(codBib))
							fd.setNumEsemplariBib(totBib);
					}
					fd.setNumEsemplariPolo(totPolo);
				}
			}
		} catch (Exception e) {
			return;
		}
	}

}
