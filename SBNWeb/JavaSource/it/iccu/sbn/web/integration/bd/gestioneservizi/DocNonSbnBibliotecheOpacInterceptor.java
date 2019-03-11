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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;

import java.util.List;

public class DocNonSbnBibliotecheOpacInterceptor implements DescrittoreBloccoInterceptor {

	private final ServiziILLDelegate delegate;
	private final String tipoServizio;
	private final String isilBibRichiedente;

	public DocNonSbnBibliotecheOpacInterceptor(ServiziILLDelegate delegate, String tipoServizio, String isilBibRichiedente) {
		this.delegate = delegate;
		this.tipoServizio = tipoServizio;
		this.isilBibRichiedente = isilBibRichiedente;
	}

	public void intercept(DescrittoreBloccoVO blocco) {

		try {
			for (Object o : blocco.getLista()) {
				DocumentoNonSbnVO d = (DocumentoNonSbnVO) o;

				//almaviva5_20160331
				if (d.getTipo_doc_lett() != 'D')
					continue;

				List<BibliotecaVO> bibliotecheOpac = d.getBiblioteche();
				if (!ValidazioneDati.isFilled(bibliotecheOpac))
					continue;

				d.setBiblioteche(null);
				List<BibliotecaVO> bibliotecheOpacFiltrata = delegate.filtraBibliotecheAffiliateILL(bibliotecheOpac, tipoServizio, isilBibRichiedente);
				if (ValidazioneDati.isFilled(bibliotecheOpacFiltrata))
					d.setBiblioteche(bibliotecheOpacFiltrata);
			}

		} catch (Exception e) {
			return;
		}
	}

}
