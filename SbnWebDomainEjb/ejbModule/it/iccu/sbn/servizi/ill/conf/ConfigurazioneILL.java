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
package it.iccu.sbn.servizi.ill.conf;

import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.util.List;

public interface ConfigurazioneILL {

	public abstract List<AttivitaServizioVO> getListaAttivitaSuccessive(
			String codBib, String servizioLoc, StatoIterRichiesta statoILL,
			StatoIterRichiesta statoLocale, List<AttivitaServizioVO> iterLocale) throws Exception;

	public abstract void controllaCambiamentoDiStato(MovimentoVO mov,
			DatiRichiestaILLVO datiILL, ServizioBibliotecaVO servizio,
			StatoIterRichiesta _old, StatoIterRichiesta _new) throws Exception;

}
