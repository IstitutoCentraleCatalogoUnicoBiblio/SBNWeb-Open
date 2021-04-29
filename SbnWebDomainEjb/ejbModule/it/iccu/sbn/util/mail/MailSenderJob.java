package it.iccu.sbn.util.mail;

import it.iccu.sbn.vo.domain.mail.MailVO;

import java.util.Queue;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class MailSenderJob implements StatefulJob {

	private static Logger log = Logger.getLogger(MailSenderJob.class);

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			final Queue<MailVO> queue = MailUtil.sendQueue;
			int send = 0;
			if (queue.isEmpty()) {
				// non ci sono mail in coda
				return;
			}
			final Session session = MailUtil.getSession();
			final Transport transport = session.getTransport("smtp");
			transport.connect();
			MailVO mail;
			while ((mail = queue.poll()) != null) {
				log.debug("inizio invio mail: " + mail.summary());
				try {
					final MimeMessage msg = MailUtil.preparaMessage(mail, session);
					msg.saveChanges();
					transport.sendMessage(msg, msg.getAllRecipients() );
					send++;
				} catch (MessagingException e) {
					log.error("errore invio mail: " + e.getMessage());
				}
				log.debug("fine invio mail...");
			}
			if (send > 0) 
				log.debug("mail inviate: " + send);

			transport.close();

		} catch (Exception e) {
			log.error("", e);
			throw new JobExecutionException("errore invio mail async", e);
		}
	}

}
