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
package it.iccu.sbn.servizi.bibliotecario.auth.impl;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.extension.auth.bibliotecario.BibLoginResponse;
import it.iccu.sbn.extension.auth.bibliotecario.BibLoginResponse.LoginResult;
import it.iccu.sbn.extension.auth.bibliotecario.BibliotecarioAuthException;
import it.iccu.sbn.extension.auth.bibliotecario.BibliotecarioAuthStrategy;
import it.iccu.sbn.persistence.dao.amministrazione.ContatoriDAO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_utenti_professionali_webDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.util.cipher.PasswordEncrypter;

import java.sql.Timestamp;
import java.util.Set;

public class SbnWebDefaultAuthStrategy implements BibliotecarioAuthStrategy {

	public BibLoginResponse login(String userId, String plainPwd) throws BibliotecarioAuthException {

		try {
			PasswordEncrypter crypt = new PasswordEncrypter(plainPwd);
			String encrypted = crypt.encrypt(plainPwd);

			Tbf_utenti_professionali_webDao dao = new Tbf_utenti_professionali_webDao();
			Tbf_utenti_professionali_web utente = dao.select(userId, encrypted, true);
			if (utente == null)
				return new BibLoginResponseImpl(LoginResult.NOT_FOUND);

			//almaviva5_20090727 utente cancellato
			if (utente.getFl_canc() == 'S')
				return new BibLoginResponseImpl(LoginResult.DISABLED);

			Tbf_anagrafe_utenti_professionali utenteProf = utente.getId_utente_professionale();

			Set<?> utentiProf = utenteProf.getTrf_utente_professionale_biblioteca();
			if (utentiProf == null)	//non trovato
				return new BibLoginResponseImpl(LoginResult.NOT_FOUND);

			Trf_utente_professionale_biblioteca bibUteProf = (Trf_utente_professionale_biblioteca) ValidazioneDati.first(utentiProf);

			if (bibUteProf == null)
				return new BibLoginResponseImpl(LoginResult.NOT_FOUND);

			//Verifica che l'ultimo accesso non sia inferiore a 6 mesi (180gg) come da normativa
			Timestamp now = DaoManager.now();

			Timestamp lastAccess = utente.getLast_access();
			String cdPolo = DomainEJBFactory.getInstance().getPolo().getInfoPolo().getCd_polo();
			int days = ContatoriDAO.getDisattivazioneUtenteDays(cdPolo);
			if (days > 0) {
				if (now.after(DateUtil.addDay(lastAccess, days)) ) {
					utente.setFl_canc('S');
					dao.update(utente);
					return new BibLoginResponseImpl(LoginResult.DISABLED);
				}
			}

			if (utente.getChange_password() == 'S')
				return new BibLoginResponseImpl(LoginResult.AUTH_DATA_EXPIRED);

			// Verifica l'ultima variazione della password che non deve essere inferiore a 3 mesi (90gg) come da normativa
			Timestamp lastVar = utente.getTs_var();
			int pwdDays = ContatoriDAO.getRinnovoPasswordDays(cdPolo);
			//almaviva5_20090727
			if (pwdDays > 0 && now.after(DateUtil.addDay(lastVar, pwdDays)) ) {
				utente.setChange_password('S');
				dao.update(utente);
				return new BibLoginResponseImpl(LoginResult.AUTH_DATA_EXPIRED);
			}

			//user logged
			utente.setLast_access(now);
			dao.update(utente);

			return new BibLoginResponseImpl(LoginResult.LOGGED);

		} catch (Exception e) {
			throw new BibliotecarioAuthException(e);
		}
	}

	public void logout(String userId) throws BibliotecarioAuthException {
		return;
	}

	public int priority() {
		return 1;
	}

}
