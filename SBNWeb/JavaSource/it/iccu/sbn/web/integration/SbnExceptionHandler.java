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
package it.iccu.sbn.web.integration;

import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class SbnExceptionHandler extends ExceptionHandler {

	private static Logger log = Logger.getLogger(SbnExceptionHandler.class);

	@Override
	public ActionForward execute(Exception ex, ExceptionConfig ae,
			ActionMapping mapping, ActionForm formInstance,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		Navigation navi = Navigation.getInstance(request);
		navi.setExceptionLog(ex);
		log.error("", ex);

		final Writer output = new StringWriter();
		PrintWriter writer = new PrintWriter(output);
		UserVO utente = navi.getUtente();
		if (utente != null) {
			writer.println("userid: " + utente.getUserId() + "<br />");
			writer.println("ticket: " + utente.getTicket() + "<br />");
			writer.println("<hr />");
		}
		ex.printStackTrace(writer);

		//almaviva5_20111121
		int errorId;
		LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.generico"));
		if ( (ex instanceof SbnBaseException))
			errorId = ((SbnBaseException)ex).getErrorId();
		else
			errorId = IdGenerator.getId();

		log.error("erroreGenerico [errorId: " + errorId + ']');
		LinkableTagUtils.addError(request, new ActionMessage("ERROR_ID_TEMPLATE", String.valueOf(errorId) ));

		// la form richiesta non è più presente sulla navigazione, sessione corrotta
		//LinkableTagUtils.addError(request, new ActionMessage("errors.global", output.toString() ));
		LinkableTagUtils.addError(request, new ActionMessage("errors.global.empty", output.toString() ));

		return mapping.findForward("error");
	}



}
