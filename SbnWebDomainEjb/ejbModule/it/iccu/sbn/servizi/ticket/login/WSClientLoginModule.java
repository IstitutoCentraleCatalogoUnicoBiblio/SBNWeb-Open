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
package it.iccu.sbn.servizi.ticket.login;

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.vo.custom.amministrazione.UserProfile;
import it.iccu.sbn.web2.util.Constants;

import java.net.InetAddress;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

public class WSClientLoginModule implements LoginModule {

	private static Logger log = Logger.getLogger(WSClientLoginModule.class);
	private static AmministrazioneBibliotecario amministrazione;

	private NameCallback cbUser;
	private PasswordCallback cbPwd;
	private Subject subject;
	private UserProfile user;

	class SbnPrincipal implements Principal {

		private final String name;

		private SbnPrincipal(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	class SbnRoles implements Group {

		private final String name;
		private final Set<Principal> members = new THashSet<Principal>();

		private SbnRoles(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean addMember(Principal user) {
			return members.add(user);
		}

		public boolean removeMember(Principal user) {
			return members.remove(user);
		}

		public boolean isMember(Principal member) {
			return members.contains(member);
		}

		public Enumeration<? extends Principal> members() {
			return Collections.enumeration(members);
		}

	}

	private AmministrazioneBibliotecario getAmministrazione() {
		if (amministrazione != null)
			return amministrazione;

		try {
			amministrazione = DomainEJBFactory.getInstance().getBibliotecario();

		} catch (Exception e) {
			log.error("", e);
		}
		return amministrazione;
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		log.debug("initialize");
		this.subject = subject;
		cbUser = new NameCallback("name");
		cbPwd = new PasswordCallback("Password", false);
		try {
			callbackHandler.handle(new Callback[] { cbUser, cbPwd });

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public boolean login() throws LoginException {
		log.debug("login");
		try {
			user = getAmministrazione().login(cbUser.getName(),
					new String(cbPwd.getPassword()), InetAddress.getLocalHost() );

			log.debug(user);

			return true;

		} catch (Exception e) {
			log.error("", e);
		}

		return false;
	}

	public boolean commit() throws LoginException {
		log.debug("commit");
		Set<Principal> principals = subject.getPrincipals();
		principals.add(new SbnPrincipal(user.getUserName()));
		Group roles = new SbnRoles("Roles");
		roles.addMember(new SbnPrincipal(Constants.SBNWEB_WS_SECURITY_DOMAIN));
		principals.add(roles);
		return true;
	}

	public boolean abort() throws LoginException {
		log.debug("abort");
		return true;
	}

	public boolean logout() throws LoginException {
		log.debug("logout");
		return true;
	}

}
