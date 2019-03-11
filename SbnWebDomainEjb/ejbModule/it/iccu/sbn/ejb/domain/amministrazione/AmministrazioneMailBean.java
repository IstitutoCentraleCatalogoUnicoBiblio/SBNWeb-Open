/**
 *
 */
package it.iccu.sbn.ejb.domain.amministrazione;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_mailDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_poloDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_mail;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.mail.servizi.ServiziMail;
import it.iccu.sbn.vo.custom.amministrazione.MailProperties;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;


public abstract class AmministrazioneMailBean extends TicketChecker implements SessionBean {

	private static final long serialVersionUID = 8540398692909652725L;

	private static final Logger log = Logger.getLogger(AmministrazioneMailBean.class);

	// PROPRIETA' DEL MAIL SERVER PREDEFINITO:
	private MailProperties mailProperties;

	public void ejbCreate() {
		log.debug("AmministrazioneMailBean creato ejb");
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		return;
	}

	public MailProperties getPoloMailProperties() throws DaoManagerException, EJBException {
		if (mailProperties != null)
			return mailProperties;

		Tbf_mailDao dao = new Tbf_mailDao();
		Tbf_mail m = dao.getMailProperties();
		if (m == null)
			return null;

		MailPropertiesImpl mp = new MailPropertiesImpl();
		mp.smtp = trimOrEmpty(m.getSmtp());
		mp.pop = trimOrEmpty(m.getPop3());
		mp.mailUser = trimOrEmpty(m.getUser_name());
		mp.mailPassword = trimOrEmpty(m.getPassword());
		mp.indirizzo = trimOrEmpty(m.getIndirizzo());
		mp.descrizione = trimOrEmpty(m.getDescrizione());
		Character fl_forzatura = coalesce(m.getFl_forzatura(), 'N');
		mp.forzaMittentePolo = fl_forzatura.equals('S');

		//almaviva5_20170523 #6385
		if (!isFilled(mp.indirizzo)) {
			log.warn("indirizzo mittente default non impostato, si ricava dalla tabella di polo");
			Tbf_poloDao pdao = new Tbf_poloDao();
			mp.indirizzo = ValidazioneDati.trimOrEmpty(pdao.getPolo().getEmail());
		}

		log.info("Caricate le property del mail server:");
		log.info("POP3: " + mp.pop);
		log.info("SMTP: " + mp.smtp);
		log.info("SMTP user: " + mp.mailUser);
		log.info("SMTP password: " + mp.mailPassword);
		log.info("INDIRIZZO: " + mp.indirizzo);
		log.info("DESCRIZIONE: " + mp.descrizione);
		log.info("FORZA MITTENTE POLO: " + mp.forzaMittentePolo);

		this.mailProperties = mp;

		return mailProperties;
	}

	public void reload() throws DaoManagerException, EJBException {
		this.mailProperties = null;
		getPoloMailProperties();
	}

	public int inviaMail(InternetAddress from, InternetAddress to, String oggetto, String testo)
		throws DaoManagerException, RemoteException {

		try {
			if (from == null) {
				MailProperties mp = getPoloMailProperties();
				if (mp == null || !isFilled(mp.getIndirizzo()))
					throw new ApplicationException(SbnErrorTypes.MAIL_INVALID_PARAMS);

				from = new InternetAddress(mp.getIndirizzo(), mp.getDescrizione());
			}

			MailVO mail = new MailVO(from, to, oggetto, testo);
			boolean inviaMail = MailUtil.sendMail(mail);

			return inviaMail ? 0 : 1;

		} catch (Exception e) {
			log.error("", e);
			return 1;
		}

	}

	public InternetAddress getMailBiblioteca(String cdPolo, String cdBib) throws ApplicationException, EJBException {
		try {
			MailProperties mp;
			if ( (mp = getPoloMailProperties()) == null)
				throw new ApplicationException(SbnErrorTypes.MAIL_INVALID_PARAMS);
			if (!isFilled(mp.getSmtp()))
				throw new ApplicationException(SbnErrorTypes.MAIL_INVALID_PARAMS);

			return (new ServiziMail()).getMailBiblioteca(cdPolo, cdBib);
		} catch (Exception e) {
			if (e instanceof ApplicationException)
				throw (ApplicationException)e;

			throw new EJBException(e);
		}
	}

}