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

import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class A03_ConsegnaDocumentoLettore extends AbstractAttivitaCheckerBase {

	public A03_ConsegnaDocumentoLettore() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) {
		dati.setControlloEseguito(StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE);
		ControlloAttivitaServizioResult stato = ControlloAttivitaServizioResult.OK;
		MovimentoVO mov = dati.getMovimento();

		if (delegate == null)
			return ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;

		Servizi servizi = delegate.getGestioneServizi();

		try {
			ServizioBibliotecaVO servizioVO;
			if (dati.getServizio() == null)
				servizioVO = servizi.getServizioBiblioteca(dati.getTicket(),
								mov.getCodPolo(), mov.getCodBibOperante(),
								mov.getCodTipoServ(), mov.getCodServ());
			else
				servizioVO = dati.getServizio();

			if (servizioVO == null)
				return ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;

			//se si tratta di un servizio ill e questa è la bib. richiedente
			//la data fine prevista è imposta dalla bib fornitrice
			if (mov.isRichiestaLocale() || mov.getDatiILL().getRuolo() != RuoloBiblioteca.RICHIEDENTE) {

				Date dataProroga = mov.getDataProroga();
				Timestamp dataInizioEffettiva = DaoManager.now();
				Timestamp dataFinePrevista = ServiziUtil.calcolaDataFinePrevista(servizioVO, dataInizioEffettiva);

				if (StatoIterRichiesta.of(mov.getCodAttivita()) != StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO) {
					// modifico la data inizio effettiva e la data di fine prevista
					// solo se non provengo da attività "05" "collocazione presso punto deposito"
					// cioè è la prima volta che "consegno il documento al lettore" (attività "03")
					mov.setDataInizioEff(dataInizioEffettiva);
					mov.setDataFinePrev(dataFinePrevista);
				}

				if (dataProroga != null	&& dataProroga.compareTo(dataInizioEffettiva) < 0)
					mov.setDataProroga(new java.sql.Date(dataFinePrevista.getTime()));

			}

			//almaviva5_20110207 #4179
			mov.setConsegnato(true);

			//almaviva5_20170505 gestione sale
			if (mov.isWithPrenotazionePosto()) {
				PrenotazionePostoVO pp = mov.getPrenotazionePosto();
				//check consegna stesso giorno
				if (!DateUtil.isSameDay(pp.getTs_inizio(), DaoManager.now()) ) {
					dati.getCodiciMsgSupplementari().add(ControlloAttivitaServizioResult.ERRORE_CONSEGNA_DOC_GIORNO_PRENOTAZIONE_POSTO.name());
					return ControlloAttivitaServizioResult.ERRORE_CONSEGNA_DOC_GIORNO_PRENOTAZIONE_POSTO;
				}
				pp.setStato(StatoPrenotazionePosto.FRUITA);
				//se il movimento è legato alla prenotazione del posto la data fine prevista è quella della prenotazione
				mov.setDataFinePrev(pp.getTs_fine());
			}

		} catch (Exception e) {
			stato = ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;
			log.error("", e);
		}

		return stato;
	}

}
