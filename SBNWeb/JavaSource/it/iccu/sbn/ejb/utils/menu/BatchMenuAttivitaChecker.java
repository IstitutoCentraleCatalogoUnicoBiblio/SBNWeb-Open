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
package it.iccu.sbn.ejb.utils.menu;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserWrapper;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ActionPathVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.AreaBatchVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ElaborazioniDifferiteConfig;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.custom.amministrazione.BatchAttivazioneVO;

import java.util.List;

import org.apache.log4j.Logger;


public class BatchMenuAttivitaChecker extends BaseMenuAttivitaChecker {

	private static Logger log = Logger.getLogger(BatchMenuAttivitaChecker.class);

	private TipoAttivita type;

	public BatchMenuAttivitaChecker(String... codAttivita) {
		super(codAttivita);
	}

	public BatchMenuAttivitaChecker(TipoAttivita type) {
		this.type = type;
	}

	public boolean check(UserWrapper uw) {
		try {
			//se sono abilitato alle funzioni esplicitate nel costruttore mi fermo qui
			if (ValidazioneDati.isFilled(super.codAttivita) && super.check(uw))
				return true;

			if (type != null) //controllo per tipo attivita
				return checkTipoAttivita(uw);

			//controllo su batch definiti a sistema
			List<BatchAttivazioneVO> batchAttivabili = DomainEJBFactory.getInstance().getPolo().getBatchServiziAll();
			if (!ValidazioneDati.isFilled(batchAttivabili))
				return false;

			for (BatchAttivazioneVO ba : batchAttivabili)
				try {
					uw.getUtenteEjb().checkAttivita(ba.getCod_attivita());
					return true;
				} catch (UtenteNotAuthorizedException e) {
					continue;
				}

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return false;
	}

	private boolean checkTipoAttivita(UserWrapper uw) throws Exception {
		try {
			ElaborazioniDifferiteConfig config = DomainEJBFactory.getInstance().getPolo().getConfigurazioneElaborazioniDifferite(type);
			if (config.isEmpty())
				return false;

			for (AreaBatchVO area : config.getAree())
				for (ActionPathVO ap : area.getAttivita())
					try {
						uw.getUtenteEjb().checkAttivita(ap.getCodAttivita());
						return true;
					} catch (UtenteNotAuthorizedException e) {
						continue;
					}

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return false;
	}

}
