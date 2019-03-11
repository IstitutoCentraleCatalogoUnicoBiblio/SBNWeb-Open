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
package it.iccu.sbn.servizi.ill.api.impl;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ill.api.ILLBaseAction;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.TransactionIdType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.rmi.RemoteException;
import java.util.Locale;

public abstract class AvanzamentoRichiestaILLBaseHandler extends ILLMessageHandler {

	private static final long serialVersionUID = 7327561922889046543L;

	public AvanzamentoRichiestaILLBaseHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
	}

	protected void cambiaStatoRichiestaILL(String ticket, DatiRichiestaILLVO dati_richiesta_ill, String oldState, String newState)
			throws DaoManagerException, RemoteException, Exception, ApplicationException {

		log.debug(String.format("richiesta ill n. %d: stato: %s --> %s", dati_richiesta_ill.getTransactionId(),
				oldState, newState));
		long idRichiesta = dati_richiesta_ill.getCod_rich_serv();
		if (idRichiesta > 0) {
			// aggiorna mov. locale + richiesta ill
			RichiesteServizioDAO dao = new RichiesteServizioDAO();
			Tbl_richiesta_servizio rs = dao.getMovimentoById(idRichiesta);
			MovimentoVO mov = ServiziConversioneVO.daHibernateAWebMovimento(rs, Locale.getDefault());
			ServizioBibliotecaVO servizio = getServizi().getServizioBiblioteca(ticket, getCodPolo(), getCodBib(),
					mov.getCodTipoServ(), mov.getCodServ());

			ILLConfiguration2.getInstance().controllaCambiamentoDiStato(mov, dati_richiesta_ill, servizio,
					StatoIterRichiesta.of(oldState), StatoIterRichiesta.of(newState));

			mov.setDatiILL(dati_richiesta_ill);
			getServizi().aggiornaRichiesta(ticket, mov, servizio.getIdServizio());
		} else {
			// aggiorna solo richiesta ill
			ILLConfiguration2.getInstance().controllaCambiamentoDiStato(null, dati_richiesta_ill, null,
					StatoIterRichiesta.of(oldState), StatoIterRichiesta.of(newState));
			aggiornaDatiRichiestaILL(dati_richiesta_ill);
		}
	}

	protected String formattaNomeXML(TransactionIdType tid, StatoIterRichiesta stato, String isil) {
		StringBuilder buf = new StringBuilder(32);
		buf.append(String.format("%06d", Long.valueOf(tid.getValue())))
			.append('_')
			.append(stato.getISOCode());
		if (isFilled(isil))
			buf.append('_')
			.append(isil);
		buf.append(".xml");

		return buf.toString();
	}

	@Override
	protected void valorizzaDatiBase(DatiRichiestaILLVO dri, ILLBaseAction action) {
		dri.setRequesterId(action.getRequesterId());
		dri.setResponderId(action.getResponderId());
		String tid = action.getTransactionId().getValue();
		dri.setTransactionId(new Long(tid));
	}

	public ILLAPDU output() {
		return null;
	}

}
