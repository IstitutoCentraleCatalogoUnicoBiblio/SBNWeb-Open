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
package it.iccu.sbn.servizi.utenti.auth.impl;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.extension.auth.utente.AuthenticationData;
import it.iccu.sbn.extension.auth.utente.LoginRequest;
import it.iccu.sbn.extension.auth.utente.LoginResponse;
import it.iccu.sbn.extension.auth.utente.LoginResponse.LoginResult;
import it.iccu.sbn.extension.auth.utente.LogoutRequest;
import it.iccu.sbn.extension.auth.utente.UserSessionInfo;
import it.iccu.sbn.extension.auth.utente.UtenteAuthException;
import it.iccu.sbn.extension.auth.utente.UtenteAuthenticationStrategy;
import it.iccu.sbn.persistence.dao.amministrazione.ContatoriDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.servizi.ServiziUtil;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

public abstract class SbnWebUtenteLettoreLoginBaseImpl implements UtenteAuthenticationStrategy {

	static Logger log = Logger.getLogger(UtenteAuthenticationStrategy.class);

	static final String DUMMY_PASSWORD = SbnWebUtenteLettoreLoginBaseImpl.class.getSimpleName();

	static final UserSessionInfo USER_SESSION_INFO = new UserSessionInfo() {
		private static final long serialVersionUID = 1L;
	};

	public SbnWebUtenteLettoreLoginBaseImpl() {
		super();
	}

	public LoginResponse login(String ticket, LoginRequest loginRequest) throws UtenteAuthException {
		try {
			String cdPolo = DomainEJBFactory.getInstance().getPolo().getInfoPolo().getCd_polo();

			String userId = ServiziUtil.espandiCodUtente(loginRequest.getUserId());
			log.debug("tentativo login per utente: " + userId);

			String password = null;

			switch (loginRequest.getRequestType()) {
			case LOGIN: {
				//login completo
				AuthenticationData authData = loginRequest.getAuthenticationData();
				if (authData == null || !(authData instanceof PlainPasswordAuthData))
					return new LoginResponseImpl(LoginResult.UNSUPPORTED, null, USER_SESSION_INFO);

				password = ((PlainPasswordAuthData) authData).getPassword();
				break;
			}

			case USER_EXISTS: {
				//solo check esistenza
				password = DUMMY_PASSWORD;
				break;
			}

			}

			PasswordEncrypter crypt = new PasswordEncrypter(password);
			Tbl_utenti utente = getUtente(userId, crypt.encrypt(password), true);

			if (utente == null)
				return new LoginResponseImpl(LoginResult.NOT_FOUND, null, USER_SESSION_INFO);

			UtenteBaseVO utenteBaseVO = ServiziConversioneVO.daHibernateAWebUtente(utente);
			LoginResult status = LoginResult.LOGGED;

			//check scadenza password
			status = checkUserStatus(cdPolo, ticket, utente);

			return new LoginResponseImpl(status, utenteBaseVO, USER_SESSION_INFO);

		} catch (Exception e) {
			throw new UtenteAuthException(e);
		}
	}

	/**
	 * Metodo astratto ridefinito dalle classi figlie che esegue l'accesso vero e proprio alla base dati
	 * @param userId nome utente
	 * @param password password
	 * @param checkPassword se impostato a {@code false} va verificata solo l'esistenza dell'utente
	 * @return istanza della tabella {@link Tbl_utenti}, {@code null} altrimenti
	 * @throws Exception
	 */
	protected abstract Tbl_utenti getUtente(String userId, String password, boolean checkPassword) throws Exception;

	private LoginResult checkUserStatus(String codPolo, String ticket, Tbl_utenti utente) throws Exception {

		LoginResult status = LoginResult.LOGGED;
		Timestamp now = DaoManager.now();

		//check utente disattivato
		int gg_max_ute = ContatoriDAO.getDisattivazioneUtenteLettore(codPolo);
		if (gg_max_ute > 0) {
			Timestamp lastAccess = utente.getLast_access();
			if (now.after(DateUtil.addDay(lastAccess, gg_max_ute)) )
				status = LoginResult.DISABLED;
		}

		if (status != LoginResult.DISABLED) {

			if (utente.getChange_password() == 'S')
				status = LoginResult.AUTH_DATA_EXPIRED;
			else {
				// check modifica password
				int gg_max_pwd = ContatoriDAO.getRinnovoPasswordUtenteLettore();
				if (gg_max_pwd > 0) {
					Timestamp lastVarPwd = utente.getTs_var_password();
					if (now.after(DateUtil.addDay(lastVarPwd, gg_max_pwd))) {
						status = LoginResult.AUTH_DATA_EXPIRED;
					}
				}
			}
		}

		if (status != LoginResult.DISABLED) {
			utente.setLast_access(now);
			utente.setUte_var(DaoManager.getFirmaUtente(ticket));
			utente.setChange_password(status == LoginResult.AUTH_DATA_EXPIRED ? 'S' : 'N');

			UtentiDAO dao = new UtentiDAO();
			dao.aggiornaUtente(utente);
		}

		return status;
	}

	public void logout(String ticket, LogoutRequest logoutRequest) throws UtenteAuthException {
		return;
	}

}
