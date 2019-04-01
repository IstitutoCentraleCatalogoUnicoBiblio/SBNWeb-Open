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
package it.iccu.sbn.ejb.domain.semantica.classi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.vo.custom.semantica.UserMessage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class ClassiBean extends TicketChecker implements Classi {

	private static final long serialVersionUID = 673171448272738782L;

	static Logger log = Logger.getLogger(Classi.class);

	static class UserMessages {
		private static Map<String, List<UserMessage>> userMessages = new ConcurrentHashMap<String, List<UserMessage>>();
		
		static List<UserMessage> get(String ticket) {
			if (!isFilled(ticket))
				return new ArrayList<UserMessage>();

			List<UserMessage> messages = userMessages.get(ticket);
			if (messages == null) {
				messages = new ArrayList<UserMessage>();
				userMessages.put(ticket, messages);
			}
			return messages;
		}
		
		static void remove(String ticket) {
			if (isFilled(ticket))
				userMessages.remove(ticket);
		}
	}

	static Reference<Semantica> semantica = new Reference<Semantica>() {
		@Override
		protected Semantica init() throws Exception {
			return DomainEJBFactory.getInstance().getSemantica();
		}};

	SessionContext ctx;

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
	}

	public List<UserMessage> consumeMessages(String ticket) {
		List<UserMessage> cached = UserMessages.get(ticket);
		UserMessages.remove(ticket);
		return cached;
	}


	public CreaVariaClasseVO importaClasseDaIndice(CreaVariaClasseVO richiesta,
			String ticket) throws DAOException {


		try {
			checkTicket(ticket);

			CreaVariaClasseVO creaVariaClasseVO = semantica.get().importaClasse(richiesta, ticket);

			return creaVariaClasseVO;


		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}


}
