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
package it.iccu.sbn.web.actions.gestionestampe.bibliotecari;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecarioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.web.actionforms.gestionestampe.bibliotecari.StampaBibliotecariForm;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class StampaBibliotecariAction extends LookupDispatchAction {

	private StampaBibliotecariForm ricBibliotecari;

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		HttpSession httpSession = request.getSession();
//		NavigationPathBO.updateForm(httpSession, form, mapping.getPath());
		ricBibliotecari = (StampaBibliotecariForm) form;
		ricBibliotecari.setCodiceBiblioteca("01");

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		ricBibliotecari = (StampaBibliotecariForm) form;
		try {
			request.setAttribute("parametroPassato", ricBibliotecari.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//TODO gv questa stampa NON funziona batch come le altre (ammesso che funzioni)
		HttpSession httpSession = request.getSession();
		ricBibliotecari = (StampaBibliotecariForm) form;
		try {
			BibliotecarioVO stampaBibliotecarioVO = new BibliotecarioVO();
			if (ricBibliotecari.getCodiceBiblioteca() != null)
				stampaBibliotecarioVO.setCodiceBiblioteca(ricBibliotecari.getCodiceBiblioteca());//set(ricBibliotecari.getCognomeNome());
			if (ricBibliotecari.getCognomeNome() != null)
				stampaBibliotecarioVO.setNominativo(ricBibliotecari.getCognomeNome());
			if (ricBibliotecari.getUfficioAppartenenza() != null)
				stampaBibliotecarioVO.setUfficioAppartenenza(ricBibliotecari.getUfficioAppartenenza());
//			if (stampaFornitoriForm.getProfAcq() != null)
//				stampaFornitoriVO.setProfAcquisti(stampaFornitoriForm.getProfAcq());
//			if (stampaFornitoriForm.getProv() != null)
//				stampaFornitoriVO.setProvincia(stampaFornitoriForm.getProv());
//			if (stampaFornitoriForm.getTpForn() != null)
//				stampaFornitoriVO.setTipoFornitore(stampaFornitoriForm.getTpForn());

		//	request.setAttribute("stampaBibliotecariVO", BibliotecarioVO);

				//DEFAULT SU ACQUISTO
//				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				List inputForStampeService=new ArrayList();
				inputForStampeService.add(stampaBibliotecarioVO);

				String tipoFormato=ricBibliotecari.getTipoFormato();

				request.setAttribute("TipoFormato", tipoFormato);
				//TODO gv qui bisognerebbe prendere il valore dalla combo della jsp
				String fileJrxml = "default_bibliotecari.jrxml";
				//DatiVO datiF= new DatiFornitoreVO ("codP", "codB", "codForn", "tipoPagForn" , "codCliForn", "numContForn", "telContForn", "faxContForn", "valForn");
				stampa(fileJrxml,tipoFormato, response);

				//return mapping.findForward("default");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return null;
	}

	private void stampa(String fileJrxml,String tipoFormato,HttpServletResponse response) throws Exception
	{
		List inputForStampeService=new ArrayList();

		for (int k = 1; k<4; k++){
			BibliotecarioVO	vo	 = new BibliotecarioVO ();
			//vo.setCodiceBiblioteca("000000001");
			vo.setCodiceBibliotecario("90");
			vo.setNominativo("Mario Rossi");
			vo.setNoteCompetenza("Non risulta alcuna nota");
			vo.setUfficioAppartenenza("Biblioteca Principale dell'ICCU");
			vo.setLivelloAutA("A");
			vo.setLivelloAutC("C");
			vo.setLivelloAutS("S");
			vo.setLivelloAutT("T");

//			vo.setLivelloAut1("1");
//			vo.setLivelloAut2("2");
//			vo.setLivelloAut3("3");
//			vo.setLivelloAut4("4");
//			vo.setLivelloAut5("5");
//			vo.setLivelloAut6("6");
//			vo.setLivelloAut7("7");
//			vo.setLivelloAut8("8");
//			vo.setLivelloAut9("9");
//			vo.setLivelloAut10("10");
//			vo.setLivelloAut11("11");
//			vo.setLivelloAut12("12");
//			vo.setLivelloAut13("13");

			inputForStampeService.add(vo);
		}

		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
//		percorso dei file template: webroot/jrxml/
		String pathJrxml=basePath+File.separator+"jrxml"+File.separator+fileJrxml;
		SbnStampe sbn = new SbnStampe(pathJrxml);
		sbn.setFormato(TipoStampa.toTipoStampa(tipoFormato));
		Map parametri=new HashMap();
		InputStream streamByte = sbn.stampa(inputForStampeService, parametri);
		ServletOutputStream servletOutputStream = response.getOutputStream();
		response = getContentTypeResponse(TipoStampa.toTipoStampa(tipoFormato), response);
		SbnStampe.transferData(streamByte, servletOutputStream);
		streamByte.close();
		servletOutputStream.flush();
		servletOutputStream.close();
	}


	public HttpServletResponse getContentTypeResponse(TipoStampa tipoFormato, HttpServletResponse response){
		switch (tipoFormato) {
		case PDF:
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=prova.pdf");

			break;
	  case RTF:
			response.setContentType("application/rtf");
			response.setHeader("Content-disposition", "inline; filename=prova.rtf");

	        break;
	  case XLS:
			response.setContentType("application/msexcel");
			response.setHeader("Content-disposition", "inline; filename=prova.xls");

			break;
	  case HTML:
			response.setContentType("text/html");

			 break;
	  case CSV:
			response.setContentType("text/csv");
			response.setHeader("Content-disposition", "inline; filename=prova.csv");

	        break;
	  default:
			response.setContentType("application/pdf");
	}
		return response;
	}
}
