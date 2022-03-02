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
package it.iccu.sbn.vo.domain.mail;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

public class MailVO extends SerializableVO {

	private static final long serialVersionUID = 6246606590704960597L;
	private InternetAddress from;
	private List<InternetAddress> to = new ArrayList<InternetAddress>();
	private List<InternetAddress> cc = new ArrayList<InternetAddress>();
	private List<InternetAddress> ccn = new ArrayList<InternetAddress>();
	private List<InternetAddress> replyTo = new ArrayList<InternetAddress>();
	private List<MailAttachmentVO> attachment = new ArrayList<MailAttachmentVO>();

	private String subject;
	private String body;
	private String type;

	private int retries;

	public MailVO() {
		super();
	}

	public MailVO(InternetAddress from, InternetAddress to, String subject, String body) {
		super();
		this.from = from;
		this.to.add(to);
		this.subject = subject;
		this.body = body;
	}

	public InternetAddress getFrom() {
		return from;
	}

	public void setFrom(InternetAddress from) {
		this.from = from;
	}

	public List<InternetAddress> getTo() {
		return to;
	}

	public List<InternetAddress> getCc() {
		return cc;
	}

	public List<InternetAddress> getCcn() {
		return ccn;
	}

	public List<InternetAddress> getReplyTo() {
		return replyTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<MailAttachmentVO> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<MailAttachmentVO> attachment) {
		this.attachment = attachment;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public void validate() throws ValidationException {

		if (from == null || !isFilled(to))
			throw new ValidationException(SbnErrorTypes.MAIL_INVALID_PARAMS);
	}

	public String summary() {
		StringBuilder buf = new StringBuilder();
		buf.append("[from=").append(from.getAddress())
			.append(", to=").append(ValidazioneDati.formatValueList(this.to, ","));
		if (ValidazioneDati.isFilled(this.replyTo)) {
			buf.append(", replyTo=").append(ValidazioneDati.formatValueList(this.replyTo, ","));
		}
		buf.append(", subject=").append(subject)
			.append("]");
		return buf.toString();
	}

	public int getRetries() {
		return retries;
	}

	public void incrementRetries() {
		this.retries++;
	}

}
