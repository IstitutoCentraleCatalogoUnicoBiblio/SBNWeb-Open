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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault;

import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class A08_ConsegnaCopie extends AbstractAttivitaCheckerBase {

	public A08_ConsegnaCopie() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.CONSEGNA_COPIE);
		MovimentoVO movimento = dati.getMovimento();
		movimento.setDataFineEff(DaoManager.now());

		return ControlloAttivitaServizioResult.OK;
	}

}
