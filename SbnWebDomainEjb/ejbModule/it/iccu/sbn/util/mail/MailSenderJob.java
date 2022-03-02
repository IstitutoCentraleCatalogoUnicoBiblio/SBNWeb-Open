package it.iccu.sbn.util.mail;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.domain.mail.MailVO;

import java.sql.Timestamp;
import java.util.Queue;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import com.sun.mail.smtp.SMTPSendFailedException;

public class MailSenderJob implements StatefulJob {

	private static final String MAIL_SENDER_JOB_LAST_EXECUTION = "mail.job.last.execution";
	private static final int SEND_LOOP_WAIT = 500;
	private static final int SEND_ATTEMPT_INTERVAL = 60 * 1000;

	private static final Logger log = Logger.getLogger(MailSenderJob.class);
	private static int interval;
	
	static {
		try {
			interval = CommonConfiguration.getPropertyAsInteger(Configuration.MAIL_SENDER_JOB_INTERVAL, SEND_ATTEMPT_INTERVAL);
		} catch (Exception e) {
			interval = SEND_ATTEMPT_INTERVAL;
		}
	}

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			final JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
			if (checkLoopLastExecution(jobDataMap)) {
				this.doLoop();
				jobDataMap.put(MAIL_SENDER_JOB_LAST_EXECUTION, DaoManager.now());
			}
		} catch (Exception e) {
			log.error("", e);
			throw new JobExecutionException("errore invio mail async", e);
		}
	}

	private void doLoop() throws Exception {
		final Queue<MailVO> queue = MailUtil.sendQueue;
		int send = 0;
		final int pending = queue.size();
		if (pending < 1) { // non ci sono mail in coda
			return;
		}
		log.debug("mail in coda per l'invio: " + pending);

		final int maxRetries = CommonConfiguration.getPropertyAsInteger(Configuration.MAIL_SEND_MAX_RETRIES, 3);
		log.debug("max tentativi invio: " + maxRetries);
		final int maxMailPerSession = CommonConfiguration.getPropertyAsInteger(Configuration.MAIL_MAX_SEND_PER_SESSION, 10);
		log.debug("max invio mail per sessione: " + maxMailPerSession);

		final Session session = MailUtil.getSession();
		final Transport transport = session.getTransport("smtp");
		transport.connect();
		for (int cnt = 0; cnt < ValidazioneDati.min(pending, maxMailPerSession); cnt++) {
			final MailVO mail = queue.poll();
			if (mail != null) {
				mail.incrementRetries();
				log.debug(String.format("tentativo n. %d invio mail %d di %d: %s", mail.getRetries(), (cnt + 1), pending, mail.summary()));
				try {
					final MimeMessage msg = MailUtil.preparaMessage(mail, session);
					msg.saveChanges();
					transport.sendMessage(msg, msg.getAllRecipients() );
					send++;
				} catch (SMTPSendFailedException e) {
					log.error("errore invio mail: " + e.getMessage());
					// se l'errore è transitorio e non è stato superato il numero di tentativi la mail viene rimessa in coda
					if (mail.getRetries() < maxRetries && MailUtil.isSMTPTransientError(e.getReturnCode()) ) {
						MailUtil.sendMailAsync(mail);
					}

				} catch (MessagingException e) {
					log.error("errore invio mail: " + e.getMessage());
				}
				log.debug("fine invio mail...");
				Thread.sleep(SEND_LOOP_WAIT);
			}
		}
		if (send > 0)
			log.debug("mail inviate: " + send);

		transport.close();
	}

	private boolean checkLoopLastExecution(final JobDataMap params) throws Exception {
		final Timestamp lastExecution = (Timestamp) params.get(MAIL_SENDER_JOB_LAST_EXECUTION);
		// test timeout
		if (lastExecution != null) {
			final Timestamp nextAttempt = DateUtil.addMillis(lastExecution, interval);
			return nextAttempt.before(DaoManager.now());
		} else {
			return true; // primo avvio
		}
	}

}
