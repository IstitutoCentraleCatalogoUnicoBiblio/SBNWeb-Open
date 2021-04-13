package it.iccu.sbn.util.mail;

import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.util.Queue;

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
			MailVO mail;
			while ((mail = queue.poll()) != null) {
				log.debug("inizio invio mail: " + mail.summary());
				try {
					if (MailUtil.sendMail(mail)) {
						log.debug("mail inviata con successo");
						send++;
					} else {
						log.warn("mail non inviata");
					}

				} catch (SbnBaseException e) {
					log.error("errore invio mail: " + e.getMessage());
				}
				log.debug("fine invio mail...");
			}
			if (send > 0) 
				log.debug("mail inviate: " + send);

		} catch (Exception e) {
			log.error("", e);
			throw new JobExecutionException("errore invio mail async", e);
		}
	}

}
