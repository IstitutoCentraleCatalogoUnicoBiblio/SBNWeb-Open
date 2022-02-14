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
package it.iccu.sbn.servizi.utenti.auth.impl.esse3;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.esse3.conf.Esse3BibConfigEntry;
import it.iccu.sbn.ejb.domain.servizi.esse3.conf.Esse3ConfigProvider;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.Esse3ServicesImpl;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3Response;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3ResponseType;
import it.iccu.sbn.ejb.exception.ApplicationException;

import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.extension.auth.utente.LoginRequest;
import it.iccu.sbn.extension.auth.utente.LoginResponse;
import it.iccu.sbn.extension.auth.utente.LoginResponse.LoginResult;
import it.iccu.sbn.extension.auth.utente.LogoutRequest;
import it.iccu.sbn.extension.auth.utente.UtenteAuthException;
import it.iccu.sbn.extension.auth.utente.UtenteAuthenticationStrategy;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.servizi.utenti.auth.impl.LoginResponseImpl;
import it.iccu.sbn.servizi.utenti.auth.impl.PlainPasswordAuthData;

import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
/**
 * Classe UtenteAuthenticationStrategy chiamata WebService su ESSE3<br>
 * Manca da fare il logout e TODO: gestione UtenteBaseVO
 * @author LFV
 * @version 1.0
 * @since 13/07/2018
 */
public class Esse3AuthStrategyImpl implements UtenteAuthenticationStrategy {

	static Logger log = Logger.getLogger(UtenteAuthenticationStrategy.class);
	private UtentiDAO dao;

	public Esse3AuthStrategyImpl() {
		dao = new UtentiDAO();
	}

	private Esse3ServicesImpl createEndpointClient(URL ws_url) {
		Esse3ServicesImpl esse3;
		//LFV 10/10/2018
		//Inserire ESSE3_WS_URL=https://servizi.lumsa.it/services/ESSE3WS in sbnweb.conf
		try {
			esse3 = new Esse3ServicesImpl(ws_url.toString());
		} catch (Exception e) {
			log.error(e);
			esse3 = new Esse3ServicesImpl();
		}
		return esse3;
	}

	private LoginResponseImpl buildResponse(Esse3Response response, String cod_polo, String ticket, String codUtente, Esse3BibConfigEntry config) {
		UtenteBaseVO utente = new UtenteBaseVO();
		try {
			utente = DomainEJBFactory.getInstance().getServizi().getUtente(ticket, codUtente);
			if (utente == null)
				response.setResponseCode(Esse3ResponseType.ERRORE_RECUPERO_DATI);
			
		} catch (Exception e) {
			log.error(e);
			response.setResponseCode(Esse3ResponseType.ERRORE_RECUPERO_DATI);
		}

		switch (response.getResponseKey()) {
		case OK:
			return new LoginResponseImpl(LoginResult.LOGGED, utente, new Esse3SessionInfo(response.getSessionId(), config));

		default:
			return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);
		}
	}

	public LoginResponse login(String ticket, LoginRequest loginRequest) throws UtenteAuthException {
		Esse3Response login = new Esse3Response(1500);
		String cdPolo = "";
		try {
			//almaviva5_20190320 check conf. esse3
			if (Esse3ConfigProvider.empty())
				return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);

			log.info("Tentativo di login ESSE3");
			cdPolo = DomainEJBFactory.getInstance().getPolo().getInfoPolo().getCd_polo();
			PlainPasswordAuthData authenticationData = (PlainPasswordAuthData) loginRequest.getAuthenticationData();

			//cerca userid su DB
			Tbl_utenti utente = dao.getUtente(loginRequest.getUserId());
			if (utente == null)
				return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);
			//estrai codBib prima iscrizione
			Esse3BibConfigEntry bibConfig = Esse3ConfigProvider.fromCdBib(utente.getCd_bib().getCd_biblioteca());
			if (bibConfig == Esse3ConfigProvider.EMPTY)
				return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);
			if (!bibConfig.canLogin())
				return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);

			log.debug("configurazione esse3 caricata: " + bibConfig);
			//creare Esse3ServicesImpl con url letto da file json
			Esse3ServicesImpl esse3 = createEndpointClient(new URL(bibConfig.getLogin_ws_url()));

			login = esse3.login(loginRequest.getUserId(), authenticationData.getPassword());

			log.info("Login ESSE3: " + login.getResponseKey());
			return buildResponse(login, cdPolo, ticket, loginRequest.getUserId(), bibConfig);

		} catch (Exception e) {
			throw new UtenteAuthException(new ApplicationException(SbnErrorTypes.DB_FALUIRE));
		}

	}

	public void logout(String ticket, LogoutRequest logoutRequest) throws UtenteAuthException {
		Esse3SessionInfo userSessionInfo = (Esse3SessionInfo) logoutRequest.getUserSessionInfo();
		try {
			Esse3ServicesImpl esse3 = createEndpointClient(new URL(userSessionInfo.getConfig().getLogin_ws_url()));
			//TODO: Sid?
			esse3.logout(userSessionInfo.getSessionId());
		} catch (MalformedURLException e) {
			log.error("", e);
			throw new UtenteAuthException(e);
		}
	}

	public int priority() {
		return 3;
	}

}
