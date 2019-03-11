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
package it.iccu.sbn.util.mail.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecaDao;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.vo.custom.amministrazione.MailProperties;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;

public final class ServiziMail {

	private Map<String, InternetAddress> addresses = new HashMap<String, InternetAddress>();


	public InternetAddress getMailBiblioteca(String codPolo, String codBib, boolean checkForzatura) throws Exception {
		// almaviva5_20150119 #5701
		InternetAddress addr = addresses.get(codBib);
		if (addr != null)
			return addr;

		if (checkForzatura) {
			//almaviva5_20161026 check forzatura polo
			MailProperties mp = DomainEJBFactory.getInstance().getAmministrazioneMail().getPoloMailProperties();
			if (mp.isForzaMittentePolo())
				return new InternetAddress(mp.getIndirizzo(), mp.getDescrizione(), "UTF-8");
		}

		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		Tbf_biblioteca bib = dao.getBiblioteca(codPolo, codBib);
		if (bib == null || ValidazioneDati.strIsNull(bib.getE_mail()))
			throw new ApplicationException(SbnErrorTypes.MAIL_INVALID_PARAMS);

		addr = new InternetAddress(ValidazioneDati.trimOrEmpty(bib.getE_mail()),
				ValidazioneDati.trimOrEmpty(bib.getNom_biblioteca()), "UTF-8");

		addresses.put(codBib, addr);

		return addr;

	}

	public InternetAddress getMailBiblioteca(String codPolo, String codBib) throws Exception {
		return getMailBiblioteca(codPolo, codBib, true);
	}

	public InternetAddress getMailUtenteMovimento(Tbl_richiesta_servizio req) throws Exception {
		Tbl_utenti utente = req.getId_utenti_biblioteca().getId_utenti();
		String mailAddr = ValidazioneDati.trimOrEmpty(ConversioneHibernateVO.toWeb().getEmailUtente(utente));
		if (!ValidazioneDati.isFilled(mailAddr))
			return null;

		String nome = utente.getCognome().trim() + " " + utente.getNome().trim();
		return new InternetAddress(mailAddr, nome, "UTF-8");
	}

}
