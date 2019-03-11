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
package it.finsiel.jms.struts.model;

import java.util.ArrayList;
import java.util.List;

import javax.jms.TextMessage;

import org.apache.struts.action.ActionForm;


public class showCodeModel extends ActionForm{

	private static final long serialVersionUID = -6453286097383658241L;

	private List<TextMessage> message = new ArrayList<TextMessage>();

	private String ID;

	private TextMessage detailsMessage;


	public List<TextMessage> getMessage() {
		return message;
	}

	public void setMessage(List<TextMessage> message) {
		this.message = message;
	}

	public void addMessage(TextMessage message) {
		this.message.add(message);
	}

	public void removeMessage()
	{
		this.message.clear();
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}




	public TextMessage getDetailsMessage() {
		return detailsMessage;
	}




	public void setDetailsMessage(TextMessage detailsMessage) {
		this.detailsMessage = detailsMessage;
	}

}
