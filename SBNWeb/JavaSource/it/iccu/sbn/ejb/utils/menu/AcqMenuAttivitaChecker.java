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
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserWrapper;
import it.iccu.sbn.vo.domain.CodiciAttivita;

public class AcqMenuAttivitaChecker extends BaseMenuAttivitaChecker {

	private static Acquisizioni gestioneAcquisizioni;

	private Acquisizioni getAcquisizioni() throws Exception {
		if (gestioneAcquisizioni == null) {
			gestioneAcquisizioni = DomainEJBFactory.getInstance().getAcquisizioni();
		}

		return gestioneAcquisizioni;
	}

	@Override
	public boolean check(UserWrapper uw) {
		//controllo cod attivita
		if (!super.check(uw) )
			return false;

		//controllo per acquisizioni
		try {
			Boolean check = true;
			if (ValidazioneDati.in(codAttivita[0], CodiciAttivita.getIstance().GA_INTERROGAZIONE_BILANCIO,
					CodiciAttivita.getIstance().GA_INTERROGAZIONE_SEZIONE_ACQUISIZIONI,
					CodiciAttivita.getIstance().GA_INTERROGAZIONE_PROFILI_DI_ACQUISTO) ) {
				ConfigurazioneORDVO ordiniConf = new ConfigurazioneORDVO();
				ordiniConf.setCodBibl(uw.getUser().getCodBib());
				ordiniConf.setCodPolo(uw.getUser().getCodPolo());
				ConfigurazioneORDVO conf = getAcquisizioni().loadConfigurazioneOrdini(ordiniConf);
				if (conf == null)
					return check;

				if (!conf.isGestioneBilancio()
						&& codAttivita[0] == CodiciAttivita.getIstance().GA_INTERROGAZIONE_BILANCIO) {
					check = conf.isGestioneBilancio();
				}

				if (!conf.isGestioneSezione()
						&& codAttivita[0] == CodiciAttivita.getIstance().GA_INTERROGAZIONE_SEZIONE_ACQUISIZIONI) {
					check = conf.isGestioneSezione();
				}

				if (!conf.isGestioneProfilo()
						&& codAttivita[0] == CodiciAttivita.getIstance().GA_INTERROGAZIONE_PROFILI_DI_ACQUISTO) {
					check = conf.isGestioneProfilo();
				}
			}
			return check;

		} catch (Exception e) {
			return false;
		}
	}

	public AcqMenuAttivitaChecker(String... codAttivita) {
		super(codAttivita);
	}

}
