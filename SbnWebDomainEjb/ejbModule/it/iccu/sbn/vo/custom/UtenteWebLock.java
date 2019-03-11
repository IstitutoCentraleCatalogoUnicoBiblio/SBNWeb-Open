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
package it.iccu.sbn.vo.custom;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.util.servizi.ServiziUtil;

public class UtenteWebLock extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 3086071631448556346L;

	private final UtenteBibliotecaVO utente;

	private final boolean lockCodFiscale;
	private final boolean lockMailAddress;
	private final boolean lockResidenza;
	private final boolean lockDomicilio;
	private final boolean lockTelefono;


	public UtenteWebLock(UtenteBibliotecaVO utente) {
		super();
		this.utente = utente;

		if (creationTime.before(utente.getTsVar())) {
			lockCodFiscale = false;
			lockMailAddress = false;
			lockResidenza = false;
			lockDomicilio = false;
			lockTelefono = false;
			return;
		}

		AnagrafeVO anag = utente.getAnagrafe();

		lockCodFiscale = isFilled(anag.getCodFiscale());
		//almaviva5_20150430
		//lockMailAddress = isFilled(anag.getPostaElettronica());
		lockMailAddress = isFilled(ServiziUtil.getEmailUtente(utente));
		lockResidenza = anag.getResidenza().impostato();
		lockDomicilio = anag.getDomicilio().impostato();
		lockTelefono = isFilled(anag.getResidenza().getTelefono()) && isFilled(anag.getResidenza().getFax())
			&& isFilled(anag.getDomicilio().getTelefono()) && isFilled(utente.getTipoSMS());
	}


	public boolean isLockCodFiscale() {
		return lockCodFiscale;
	}


	public boolean isLockMailAddress() {
		return lockMailAddress;
	}


	public boolean isLockResidenza() {
		return lockResidenza;
	}


	public boolean isLockDomicilio() {
		return lockDomicilio;
	}


	public boolean isLockTelefono() {
		return lockTelefono;
	}


	public UtenteBibliotecaVO getUtente() {
		return utente;
	}

}
