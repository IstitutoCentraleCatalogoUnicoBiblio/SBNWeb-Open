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
package it.iccu.sbn.util.bibliografica.impl;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBMT;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.extension.bibliografica.FusioneDatiGestionaliDelegate;

public class SbnWebFusioneBaseDelegateImpl implements
		FusioneDatiGestionaliDelegate {

	private DocumentoFisicoBMT documentoFisicoBMT;
	private AcquisizioniBMT acquisizioniBMT;
	private PeriodiciSBN periodiciSBN;

	private DocumentoFisicoBMT getInventarioBean() {
		try {
			if (documentoFisicoBMT != null)
				return documentoFisicoBMT;

			documentoFisicoBMT = DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
			return documentoFisicoBMT;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private AcquisizioniBMT getOrdiniBean() {
		try {
			if (acquisizioniBMT != null)
				return acquisizioniBMT;

			acquisizioniBMT = DomainEJBFactory.getInstance().getAcquisizioniBMT();
			return acquisizioniBMT;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PeriodiciSBN getPeriodiciBean() {
		try {
			if (periodiciSBN != null)
				return periodiciSBN;

			periodiciSBN = DomainEJBFactory.getInstance().getPeriodiciBMT();
			return periodiciSBN;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static Reference<ServiziBibliografici> bibliografica = new Reference<ServiziBibliografici>() {
		@Override
		protected ServiziBibliografici init() throws Exception {
			return DomainEJBFactory.getInstance().getSrvBibliografica();
		}};

	public boolean richiestaCancellazione(String idElementoEliminato,
			String uteVar) throws Exception {
		return false;
	}

	public boolean richiestaSpostamentoLegami(String idElementoEliminato,
			String idElementoAccorpante, String uteVar) throws Exception {

		// Spostamento legami Area Documento Fisico tramite chiamata a oggetti esterni solo nel caso di Documenti
		getInventarioBean().getFusioneTitoli(idElementoEliminato, idElementoAccorpante, uteVar);

		getOrdiniBean().getFusioneCollane(idElementoEliminato, idElementoAccorpante, uteVar);

		// almaviva5_20111111 #4735 spostamento legami fascicoli
		getPeriodiciBean().spostaFascicoliPerFusione(idElementoEliminato, idElementoAccorpante, uteVar);

		// almaviva5_20211103 fusione numeri oclc
		bibliografica.get().spostaAltroIdPerFusione(idElementoEliminato, idElementoAccorpante, uteVar);

		return true;

	}

}
