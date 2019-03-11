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
package it.iccu.sbn.web.actions.gestionestampe;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.web.actionforms.gestionestampe.common.StampaForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web2.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ReportAction extends AcquisizioniBaseAction {

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma", "conferma");
		map.put("button.indietro", "indietro");
		return map;
	}

//	 in input c'è un arraylist di bean
//	 vengono specificati tipo font e dimensioni
//	 quindi il nome del file è il template
//	 da elaborare e compilare
	protected void preparaStampa(HttpServletResponse response, List stampeVO, String fileJrxml, String tipoFormato, String exportFileName)
	throws IOException	{
		try {
			SbnStampe stampa = new SbnStampe(fileJrxml);
			//almaviva5_20131105 #5428
			stampa.setFormato(TipoStampa.toTipoStampa(tipoFormato));
			InputStream in = stampa.stampa(stampeVO, null);
			if (in == null)
				return;

			ServletOutputStream out = response.getOutputStream();
			response.reset();

			impostaResponse(response, exportFileName, tipoFormato);

			SbnStampe.transferData(in, out);
			in.close();
			out.flush();
			out.close();

		}catch (IOException ex)
		{
			throw ex;
		} catch (Exception e) {
			log.error("", e);
		}
	}


	public void impostaResponse(HttpServletResponse response, String exportFileName, String tipoFormato)
	throws IOException, JRException	{
		TipoStampa tipo = TipoStampa.toTipoStampa(tipoFormato);
		switch (tipo) {
		case PDF:
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="+exportFileName+".pdf");
			break;

		case RTF:
			response.setContentType("application/rtf");
			response.setHeader("Content-disposition", "attachment; filename="+exportFileName+".rtf");
			break;

		case XLS:
			response.setContentType("application/msexcel");
			response.setHeader("Content-disposition", "attachment; filename="+exportFileName+".xls");
			break;

		case HTML:
			response.setContentType("text/html");
			response.setHeader ("Content-Disposition", "attachment;filename="+exportFileName+".html");
			break;

		case CSV:
			response.setContentType("text/csv");
			response.setHeader("Content-disposition", "attachment; filename="+exportFileName+".csv");
			break;
		}
	}


	protected void loadDefault(HttpServletRequest request, StampaForm ricEticStampaForm) throws InfrastructureException, NumberFormatException, RemoteException
	{
		Utente utenteEjb =(Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			ricEticStampaForm.setTipoFormato((String)utenteEjb.getDefault(ConstantDefault.FORMATO_STAMPA));
		} catch (DefaultNotFoundException e) {}
	}
}
