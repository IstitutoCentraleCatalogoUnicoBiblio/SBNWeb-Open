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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class A05_CollocazionePuntoDeposito extends AbstractAttivitaCheckerBase {

	public A05_CollocazionePuntoDeposito() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO);
		ControlloAttivitaServizioResult stato = ControlloAttivitaServizioResult.OK;
		MovimentoVO mov = dati.getMovimento();

		if (delegate == null)
			return ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;

		try {
			ServizioBibliotecaVO servizioVO;
			if (dati.getServizio() == null)
				servizioVO = delegate.getGestioneServizi()
						.getServizioBiblioteca(dati.getTicket(),
								mov.getCodPolo(), mov.getCodBibOperante(),
								mov.getCodTipoServ(), mov.getCodServ());
			else
				servizioVO = dati.getServizio();

			if (servizioVO == null)
				return ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;

			Timestamp now = DaoManager.now();

			// solo se la data di fine prevista è maggiore
			// di quella corrente imposto
			// la data di inizio prevista e
			// la data di fine prevista
			if (mov.getDataFinePrev().before(now) )
				return stato;

			if (mov.getDataInizioEff() == null) {
				// errore: non si può continuare
				// perchè la collocazione punto deposito
				// non segue la consegna del documento al lettore
				// (dato che non è impostata la data di inizio effettiva)
				dati.setCodiciMsgSupplementari(ValidazioneDati.asSingletonList("errors.servizi.documento.collocazione.no.dopo.consegna"));
				return ControlloAttivitaServizioResult.ERRORE_COLLOCAZIONE_PUNTO_DEPOSITO_NO_DOPO_CONSEGNA;
			}

			// riporto la data di inizio effettiva presente sul movimento
			// in formato Calendar per effettuare le successive somme
			// (siamo già passati per la consegna del documento al lettore
			// dove viene impostata tale data)

			//se si tratta di un servizio ill e questa è la bib. richiedente
			//la data fine prevista è imposta dalla bib fornitrice
			if (mov.isRichiestaLocale() || mov.getDatiILL().getRuolo() != RuoloBiblioteca.RICHIEDENTE) {

				// estraggo dal servizio (diritto) la durata del deposito
				Short durataDeposito = servizioVO.getMaxGgDep();

				// sommo alla data di inizio effettiva la durata del deposito
				Timestamp nuovaDataInizioPrev = DateUtil.addDay(mov.getDataInizioEff(), durataDeposito);

				// e la imposto nella data di inizio prevista
				mov.setDataInizioPrev(nuovaDataInizioPrev);

				// sommo alla data di inizio prevista la durata del servizio
				Timestamp nuovaDataFinePrev = ServiziUtil.calcolaDataFinePrevista(servizioVO, nuovaDataInizioPrev);

				// e la imposto nella data di fine prevista
				mov.setDataFinePrev(nuovaDataFinePrev);

			}
		} catch (Exception e) {
			stato = ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;
			log.error("", e);
		}

		return stato;

	}

}
