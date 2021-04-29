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
package it.iccu.sbn.ejb.domain.amministrazione;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.vo.custom.amministrazione.MailProperties;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;
import javax.mail.internet.InternetAddress;


public interface AmministrazioneMail extends EJBObject {
/*
	public int inviaMail(String address, String oggetto, String testo)
			throws DaoManagerException, RemoteException;
*/
	public int inviaMail(InternetAddress from, InternetAddress to, String oggetto, String testo)
		throws DaoManagerException, RemoteException;

	public MailProperties getPoloMailProperties() throws DaoManagerException, RemoteException;

	public void reload() throws DaoManagerException, RemoteException;

	public AddressPair getMailBiblioteca(String cdPolo, String cdBib) throws ApplicationException, RemoteException;
}
