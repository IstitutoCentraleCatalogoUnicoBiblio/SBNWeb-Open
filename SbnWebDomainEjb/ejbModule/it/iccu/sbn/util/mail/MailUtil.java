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
package it.iccu.sbn.util.mail;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.custom.amministrazione.MailProperties;
import it.iccu.sbn.vo.domain.mail.MailAttachmentVO;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class MailUtil {

	public static final String MIME_TYPE_TEXT_PLAIN = "text/plain; charset=UTF-8";
	public static final String MIME_TYPE_HTML = "text/html; charset=UTF-8";

	private static Logger log = Logger.getLogger(MailUtil.class);

	private static class SMTPAuth extends Authenticator {

		private final PasswordAuthentication auth;

		public SMTPAuth(String user, String pswd) {
			auth = new PasswordAuthentication(user, pswd);
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return auth;
		}
	}

	public static void main(String[] args) throws Exception {
		MailVO mail = new MailVO();
		mail.setFrom(new InternetAddress("pippo@pluto.net", "test"));
		mail.getTo().add(new InternetAddress("quequ@libero.it", "simo") );
		mail.setSubject("arriverà pure questa?");
		mail.setBody("chi lo sa...");

		MailUtil.sendMail(mail);
	}

	public static final boolean sendMail(MailVO mail) throws ValidationException,
			ApplicationException {

		try {
			if (mail == null)
				throw new ValidationException(SbnErrorTypes.MAIL_INVALID_PARAMS);

			mail.validate();

			Session session = getSession();
			Transport transport = session.getTransport("smtp");
			MimeMessage msg = new MimeMessage(session);

			InternetAddress from = mail.getFrom();
			msg.setFrom(from);

			msg.setSubject(mail.getSubject() );

			if (ValidazioneDati.isFilled(mail.getTo())) {
				for (InternetAddress to : mail.getTo() )
					msg.addRecipient(RecipientType.TO, to);
			}

			if (ValidazioneDati.isFilled(mail.getCc())) {
				for (InternetAddress cc : mail.getCc() )
					msg.addRecipient(RecipientType.CC, cc);
			}

			if (ValidazioneDati.isFilled(mail.getCcn())) {
				for (InternetAddress ccn : mail.getCcn() )
					msg.addRecipient(RecipientType.BCC, ccn);
			}

			prepareBody(mail, msg);

			log.debug("invio mail: " + mail);

			transport.connect();
			msg.saveChanges();
			transport.sendMessage(msg, msg.getAllRecipients() );
			transport.close();

			return true;

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.MAIL_DELIVERY_ERROR, e);
		}
	}

	private static void prepareBody(MailVO mail, MimeMessage message)
			throws MessagingException {

		//almaviva5_20130702 #4765
		String type = mail.getType();
		type = ValidazioneDati.isFilled(type) ? type : MIME_TYPE_TEXT_PLAIN;

		if (!ValidazioneDati.isFilled(mail.getAttachment())) {
			message.setContent(mail.getBody(), type);
			return;
		}

		// ci sono allegati
		Multipart mp = new MimeMultipart();
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(mail.getBody(), type);
		mp.addBodyPart(textPart);

		for (MailAttachmentVO attach : mail.getAttachment()) {
			MimeBodyPart attachFilePart = new MimeBodyPart();
			attachFilePart.setFileName(attach.getName());
			DataHandler handler = new DataHandler(new ByteArrayDataSource(attach.getData(), attach.getType()));
			attachFilePart.setDataHandler(handler);
			mp.addBodyPart(attachFilePart);
		}

		message.setContent(mp);
	}

	private static Session getSession() throws Exception {

		Session session = null;

		MailProperties mp = DomainEJBFactory.getInstance().getAmministrazioneMail().getPoloMailProperties();
		if (mp == null)
			throw new ValidationException(SbnErrorTypes.MAIL_INVALID_PARAMS);
		Properties props = System.getProperties();

		props.put("mail.transport.protocol", "smtp");

		//livello logging
		String level = CommonConfiguration.getProperty(Configuration.LOG_LEVEL_SBNWEB).toUpperCase();
		Boolean debug = ValidazioneDati.in(level, "ALL", "TRACE", "DEBUG");
		props.put("mail.debug", debug.toString());

		//almaviva5_20110211 host + port non standard
		String host = ValidazioneDati.trimOrEmpty(mp.getSmtp());
		if (ValidazioneDati.isFilled(host)) {
			String[] tokens = host.split("\\:");
			if (ValidazioneDati.size(tokens) == 1)
				props.put("mail.smtp.host", host);
			else {
				props.put("mail.smtp.host", tokens[0]);
				props.put("mail.smtp.port", tokens[1]);
			}
		} else
			throw new ValidationException(SbnErrorTypes.MAIL_INVALID_PARAMS);

		String user = mp.getMailUser();
		String pswd = mp.getMailPassword();
		boolean requireAuth = ValidazioneDati.isFilled(user) && ValidazioneDati.isFilled(pswd);
		if (requireAuth) {
			log.debug("il server '" + host + "' richiede autenticazione...");
			props.setProperty("mail.smtp.auth", "true");
			session = Session.getInstance(props, new SMTPAuth(user, pswd) );
		} else
			session = Session.getInstance(props);

		log.debug("creata mail session " + session.getProperties());

		if (debug) {
			//almaviva5_20190529 redirect mail debug log
			session.setDebugOut(new PrintStream(new LoggingOutputStream(log, Priority.DEBUG), true));
		}

		return session;

	}

	public static final byte[] fileToByteArray(String path) throws Exception {
		InputStream in = null;
		try {
			try {
				File file = new File(path);
				in = new BufferedInputStream(new FileInputStream(file));

				long len = file.length();
				if (len > Integer.MAX_VALUE)
					return null;

				byte[] buf = new byte[(int) len];

				int offset = 0;
				int numRead = 0;
				while (offset < len && (numRead = in.read(buf, offset, (int)len - offset)) >= 0)
					offset += numRead;

				if (offset < len)
					throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);

				return buf;

			} catch (IOException e) {
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			}
		} finally {
			FileUtil.close(in);
		}
	}

	public static void testMailServer(String email) throws Exception {
		MailProperties mp = DomainEJBFactory.getInstance().getAmministrazioneMail().getPoloMailProperties();
		MailVO mail = new MailVO();
		mail.setFrom(new InternetAddress(mp.getIndirizzo(), mp.getDescrizione()));
		mail.getTo().add(new InternetAddress(email));
		mail.setSubject("sbnweb test mail");
		mail.setBody("sbnweb test mail");
		MailAttachmentVO attachment = new MailAttachmentVO();
		attachment.setName("test allegato.txt");
		attachment.setData(("«Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt "
				+ "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam "
				+ "corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Quis aute iure "
				+ "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
				+ "sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id "
				+ "est laborum.»").getBytes("UTF-8"));
		attachment.setType(MailUtil.MIME_TYPE_TEXT_PLAIN);
		mail.getAttachment().add(attachment);
		MailUtil.sendMail(mail);
	}

}
