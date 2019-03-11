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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class PrenotazionePostoSala extends AbstractAttivitaCheckerBase {

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}};

	static Reference<Inventario> inventario = new Reference<Inventario>() {
		@Override
		protected Inventario init() throws Exception {
			return DomainEJBFactory.getInstance().getInventario();
		}};

	public PrenotazionePostoSala() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.PRENOTAZIONE_POSTO);
		MovimentoVO mov = dati.getMovimento();
		//TODO solo su inventario?
		if (!mov.isRichiestaSuInventario())
			return ControlloAttivitaServizioResult.ERRORE_CONTROLLO_BLOCCANTE_NON_SUPERATO;

		PrenotazionePostoVO pp = mov.getPrenotazionePosto();
		if (pp == null) {
			if (dati.getOperatore() != OperatoreType.UTENTE)
				return ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE;

			//richiesta da remoto, check copia digitale
			boolean digitalizzato = false;
			ParametriBibliotecaVO parametriBiblioteca = servizi.get().getParametriBiblioteca(dati.getTicket(), mov.getCodPolo(), mov.getCodBibOperante());
			if (parametriBiblioteca == null)
				return ControlloAttivitaServizioResult.ERRORE_CONFIGURAZIONE_INCOMPLETA;

			String catMediazioneDigit = parametriBiblioteca.getCatMediazioneDigit();
			if (ValidazioneDati.isFilled(catMediazioneDigit) ) {
				//se la richiesta è inserita da remoto e la biblioteca ha definito una cat. di mediazione per i
				//documenti digitalizzati, è necessario controllare che l'inventario, e in subordine il titolo, abbia
				//il flag di digitalizzazione impostato
				digitalizzato = inventario.get().inventarioDigitalizzato(mov.getCodPolo(), mov.getCodBibInv(),
					mov.getCodSerieInv(), Integer.valueOf(mov.getCodInvenInv()));
			}

			if (digitalizzato) {
				dati.setCatMediazioneDigit(catMediazioneDigit);
				log.info(String.format("Inventario '%s' disponibile in formato digitale.", mov.getDatiInventario()));
				return ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE_INV_DIGITALIZZATO;
			} else
				return ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_POSTO_MANCANTE;
		}


		mov.setDataInizioPrev(pp.getTs_inizio());
		mov.setDataFinePrev(pp.getTs_fine());
		mov.setCodStatoRic(MovimentoVO.STATO_RICHIESTA_IMMESSA);
		mov.setCodStatoMov(MovimentoVO.STATO_PRENOTAZIONE);
		mov.setFlTipoRec(RichiestaRecordType.FLAG_PRENOTAZIONE);

		return ControlloAttivitaServizioResult.OK;
	}

}
