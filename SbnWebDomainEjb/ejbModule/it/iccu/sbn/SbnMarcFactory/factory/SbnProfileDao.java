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
package it.iccu.sbn.SbnMarcFactory.factory;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.exception.UtenteNotProfiledException;

/**
 * Classe che permette di eseguire una interrogazione al server SBN
 * sulle credenziali (login e password) fornite da un utente.
 * Imposta il profilo utente ritornato dal server nella classe XMLProfilo.
 * @author Corrado Di Pietro
 */
public class SbnProfileDao {

	private SbnUserType user;
	private SbnUserType profile;
	private FactorySbn server;

	public SbnProfileDao(FactorySbn server, SbnUserType user, SbnUserType profile) {
		this.server = server;
		this.user = user;
		this.profile = profile;
	}

    public SbnProfileType profile() throws SbnMarcException, UtenteNotProfiledException {
    	SbnProfileType profiloUtente = null;
    	SBNMarc sbn;
        if ((sbn = sendMessage()) != null) {
        	SbnResponseType sbnResponse = sbn.getSbnMessage().getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
        	if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
				profiloUtente = sbnResponse.getSbnResponseTypeChoice().getSbnUserProfile();
				return profiloUtente;
			} else
        		throw new UtenteNotProfiledException(sbnResult.getTestoEsito());
         }

        throw new SbnMarcException("Impossibile caricare profilo utente");

    }

	private SBNMarc sendMessage() throws SbnMarcException {
		this.server.setMessage(this.createMessage(), this.user);
		SBNMarc sbnProfilo = this.server.eseguiRichiestaServer();

		return sbnProfilo;
	}

	private SbnMessageType createMessage() {
		CercaType cercaType = new CercaType();
		cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
		cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
		cercaType.setCercaSbnProfile(this.profile);
		SbnRequestType sbnrequest = new SbnRequestType();
		sbnrequest.setCerca(cercaType);
		SbnMessageType sbnmessage = new SbnMessageType();
		sbnmessage.setSbnRequest(sbnrequest);

		return sbnmessage;
	}
}
