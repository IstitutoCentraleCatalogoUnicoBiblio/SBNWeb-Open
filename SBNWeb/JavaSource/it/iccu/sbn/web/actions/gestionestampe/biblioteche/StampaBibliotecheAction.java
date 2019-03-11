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
package it.iccu.sbn.web.actions.gestionestampe.biblioteche;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBibliotecheVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.biblioteche.StampaBibliotecheForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StampaBibliotecheAction extends ReportAction{//LookupDispatchAction {
	private StampaBibliotecheForm stampaBibliotecheForm;
	private CaricamentoCombo carCombo = new CaricamentoCombo();

	private void loadTipoBiblioteca(HttpServletRequest request) throws Exception {
		try{
			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String ticket = Navigation.getInstance(request).getUtente().getTicket();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//BibliotecaSearch bs = new BibliotecaSearch();
			//List listaBiblio = factory.getSistema().getBiblioteche(bs, 0);
			List listaBiblioteche = factory.getGestioneBibliografica().getComboBibliotechePolo(polo, ticket);
			this.stampaBibliotecheForm.setListaTipoBiblioteca(listaBiblioteche);
	}catch(Exception ex) {
		// DEVO GESTIRE LE ECCEZIONI PERSONALIZZANDO L'ERRORE
		throw new Exception("Failed to require list of profiles to the services Acquisizioni", ex);

		}
	}
//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom("01","Altro");
//		lista.add(elem);
//		elem = new StrutturaCom("02","Comune");
//		lista.add(elem);
//		elem = new StrutturaCom("03","Comunità");
//		lista.add(elem);
//		elem = new StrutturaCom("04","Ente ecclesiastico");
//		lista.add(elem);
//		elem = new StrutturaCom("05","Privata");
//		lista.add(elem);
//		elem = new StrutturaCom("06","Provinciale");
//		lista.add(elem);
//		this.stampaBibliotecheForm.setListaTipoBiblioteca(lista);


	private void loadProvincia() throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		stampaBibliotecheForm.setListaProvincia(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceProvincia()));
	}

//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom("01","AG Agrigento");
//		lista.add(elem);
//		elem = new StrutturaCom("02","AL Alessandria");
//		lista.add(elem);
//		elem = new StrutturaCom("03","AN Ancona");
//		lista.add(elem);
//		elem = new StrutturaCom("04","AO Aosta");
//		lista.add(elem);
//		elem = new StrutturaCom("05","AR Arezzo");
//		lista.add(elem);
//		elem = new StrutturaCom("06","AP Ascoli Piceno");
//		lista.add(elem);
//		this.stampaBibliotecheForm.setListaProvincia(lista);
//	}

	private void loadPaese() throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		stampaBibliotecheForm.setListaPaese(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
	}
//
//		List lista = new ArrayList();
//		StrutturaCom elem = new StrutturaCom("","");
//		lista.add(elem);
//		elem = new StrutturaCom("01","AF Afghanistan");
//		lista.add(elem);
//		elem = new StrutturaCom("02","ZA Africa del Sud");
//		lista.add(elem);
//		elem = new StrutturaCom("03","AL Albania");
//		lista.add(elem);
//		elem = new StrutturaCom("04","DZ Algeria");
//		lista.add(elem);
//		elem = new StrutturaCom("05","AD Andorra");
//		lista.add(elem);
//		elem = new StrutturaCom("06","AO Angola");
//		lista.add(elem);
//		this.stampaBibliotecheForm.setListaPaese(lista);
//	}

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
//		map.put("button.stampa","stampa");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		stampaBibliotecheForm = (StampaBibliotecheForm) form;
		HttpSession httpSession = request.getSession();
//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());


//		boolean error = false;
		try{

			this.loadTipoBiblioteca(request);
			this.loadProvincia();
			this.loadPaese();

//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);

			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
	//		String bibl = Navigation.getInstance(request).getUtente().getCodBib();
	//		stampaBibliotecheForm.setPolo(polo);
			stampaBibliotecheForm.setElencoModelli(getElencoModelli());
//			stampaBibliotecheForm.setTipoFormato("html");
//		} catch (Exception e) {
//			return mapping.getInputForward();
//		}
		if (request.getAttribute("stampa")!= null ){
				if (!(request.getAttribute("stampa").equals("stampaOnLine"))){
					return mapping.getInputForward();
				}else{
					try{
						String tipoPaese=((StampaBibliotecheForm)form).getPaese();
						String tipoProvincia =((StampaBibliotecheForm)form).getProvincia();
						String tipoRicercaBib =((StampaBibliotecheForm)form).getBiblioteca();
						boolean inserimentiOk = true;
						if(!(tipoProvincia.equals("")) && (!(stampaBibliotecheForm.getPaese() != null) || (!(tipoPaese.equals("IT"))))){ //!(tipoPaese.equalsIgnoreCase("IT")) ||
							inserimentiOk = false;
							ActionMessages errors = new ActionMessages();
							//errore: scelta del paese per la stampa
							errors.add("generico", new ActionMessage("errors.stampa.sceltaPaese"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
//
						}else if(!(stampaBibliotecheForm.getBiblioteca()!= null) || (tipoRicercaBib.equals(""))) {
							inserimentiOk = false;
							ActionMessages errors = new ActionMessages();
							errors.add("generico", new ActionMessage("errors.stampa.sceltaLivRicerca"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}else if(inserimentiOk){
							StampaBibliotecheVO stampaBibliotecheVO = new StampaBibliotecheVO();
							String nomeBiblio= ((StampaBibliotecheForm)form).getNomeBiblioteca();
							String poloBiblio= ((StampaBibliotecheForm)form).getPolo();

							if (stampaBibliotecheForm.getTipoBiblioteca() != null)
								stampaBibliotecheVO.setTipoBiblioteca(stampaBibliotecheForm.getTipoBiblioteca());
							if(stampaBibliotecheForm.getNomeBiblioteca() != null)
								stampaBibliotecheVO.setNomeBiblioteca(stampaBibliotecheForm.getNomeBiblioteca());
							if (stampaBibliotecheForm.getPaese() != null)
								stampaBibliotecheVO.setPaese(stampaBibliotecheForm.getPaese());
							if (stampaBibliotecheForm.getEnteDiAppartenenza() != null)
								stampaBibliotecheVO.setEnteDiAppartenenza(stampaBibliotecheForm.getEnteDiAppartenenza());
							if (stampaBibliotecheForm.getProvincia() != null)
								stampaBibliotecheVO.setProvincia(stampaBibliotecheForm.getProvincia());
							if (stampaBibliotecheForm.getPolo() != null){
								stampaBibliotecheVO.setPolo(stampaBibliotecheForm.getPolo());
							}

							request.setAttribute("stampaBibliotecheVO", "stampaBibliotecheVO");
//							NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
							List inputForStampeService=new ArrayList();
							inputForStampeService.add(stampaBibliotecheVO);

//							String polo = sbvo.getPolo();
//							String biblio = sbvo.getCodiceBiblioteca();
//							String nomeBiblioteca = sbvo.getNomeBiblioteca();

							String ticket = Navigation.getInstance(request).getUtente().getTicket();
							FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

							StampeOnlineVO toPrint=factory.getStampeOnline().stampeOnline(ticket, StampaType.STAMPA_LISTA_BIBLIOTECHE, inputForStampeService);
							List listaBiblioteche = toPrint.getRigheDatiDB();

							if(listaBiblioteche.size() <= 0){
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage("errors.stampaBiblioteche.biblioInesistente"));
								this.saveErrors(request, errors);
								}else{
									String fileJrxml = stampaBibliotecheForm.getTipoModello()+".jrxml";
									String basePath=this.servlet.getServletContext().getRealPath(File.separator);

							//		percorso dei file template: webroot/jrxml/
									String pathJrxml=basePath+File.separator+"jrxml"+File.separator+fileJrxml;
									String tipoFormato=stampaBibliotecheForm.getTipoFormato();
									log.info("path fileJrxml per le stampe on line utenti"+fileJrxml);

									stampaOnLine(response,  listaBiblioteche, tipoFormato, pathJrxml);

									/**
									 * PER ADESSO USO QUESTA FUNZIONE MA DEVO UTILIZZARE JMS PER PRODURRE L'OUTPUT
									 */

									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage("errors.finestampa"));
									this.saveErrors(request, errors);
//									throw new Exception();
									return null;
//									return mapping.findForward("stampa");
								}
						}
					} catch (Exception e) {
//						return mapping.getInputForward();
						throw new Exception();
					}
				}
//				return mapping.getInputForward();
			}
		stampaBibliotecheForm.setTipoFormato(TipoStampa.HTML.name());
		} catch (Exception e) {
			return mapping.getInputForward();
		}
//			}
//				return null;
//			}
		return mapping.getInputForward();
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			request.setAttribute("stampa", "stampaOnLine");
		} catch (Exception e) {
//			throw new Exception();
			return mapping.getInputForward();
		}
		return unspecified(mapping, form, request, response);
//		return mapping.findForward("stampa");
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		stampaBibliotecheForm = (StampaBibliotecheForm) form;
		try {
			request.setAttribute("parametroPassato", stampaBibliotecheForm.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		stampaBibliotecheForm = ((StampaBibliotecheForm)form);
		HttpSession httpSession = request.getSession();
		String tipoPaese=((StampaBibliotecheForm)form).getPaese();
		String tipoProvincia =((StampaBibliotecheForm)form).getProvincia();
		String tipoRicercaBib =((StampaBibliotecheForm)form).getBiblioteca();
		boolean inserimentiOk = true;
			try {
				if(!(tipoProvincia.equals("")) && (!(stampaBibliotecheForm.getPaese() != null) || (!(tipoPaese.equals("IT"))))){ //!(tipoPaese.equalsIgnoreCase("IT")) ||
					inserimentiOk = false;
					ActionMessages errors = new ActionMessages();
					//errore: scelta del paese per la stampa
					errors.add("generico", new ActionMessage("errors.stampa.sceltaPaese"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
//
				}else if(!(stampaBibliotecheForm.getBiblioteca()!= null) || (tipoRicercaBib.equals(""))) {
					inserimentiOk = false;
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.stampaBiblioteca.sceltaLivRicerca"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}else if(inserimentiOk){
					StampaBibliotecheVO stampaBibliotecheVO = new StampaBibliotecheVO();
					String nomeBiblio= ((StampaBibliotecheForm)form).getNomeBiblioteca();
					String poloBiblio= ((StampaBibliotecheForm)form).getPolo();

					if (stampaBibliotecheForm.getTipoBiblioteca() != null)
						stampaBibliotecheVO.setTipoBiblioteca(stampaBibliotecheForm.getTipoBiblioteca());
//					if (!(nomeBiblio.equals(""))){
//						if((stampaBibliotecheForm.getNomeBiblioteca() != null)){
//						stampaBibliotecheVO.setNomeBiblioteca(stampaBibliotecheForm.getNomeBiblioteca());
//						}
//					}else{
//						ActionMessages errors = new ActionMessages();
//						errors.add("generico", new ActionMessage("errors.sceltaBiblioteca"));
//						this.saveErrors(request, errors);
//						return mapping.getInputForward();
//					}
					if(stampaBibliotecheForm.getNomeBiblioteca() != null)
						stampaBibliotecheVO.setNomeBiblioteca(stampaBibliotecheForm.getNomeBiblioteca());
					if (stampaBibliotecheForm.getPaese() != null)
						stampaBibliotecheVO.setPaese(stampaBibliotecheForm.getPaese());
					if (stampaBibliotecheForm.getEnteDiAppartenenza() != null)
						stampaBibliotecheVO.setEnteDiAppartenenza(stampaBibliotecheForm.getEnteDiAppartenenza());
					if (stampaBibliotecheForm.getProvincia() != null)
						stampaBibliotecheVO.setProvincia(stampaBibliotecheForm.getProvincia());
//					if (!(poloBiblio.equals(""))){
//						if (stampaBibliotecheForm.getPolo() != null){
//							stampaBibliotecheVO.setPolo(stampaBibliotecheForm.getPolo());
//						}
//					}else{
//						ActionMessages errors = new ActionMessages();
//						errors.add("generico", new ActionMessage("errors.sceltaPolo"));
//						this.saveErrors(request, errors);
//						return mapping.getInputForward();
//					}
					if (stampaBibliotecheForm.getPolo() != null){
						stampaBibliotecheVO.setPolo(stampaBibliotecheForm.getPolo());
					}

					request.setAttribute("stampaBibliotecheVO", stampaBibliotecheVO);
//					NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
					List inputForStampeService=new ArrayList();
					inputForStampeService.add(stampaBibliotecheVO);



					String fileJrxml = stampaBibliotecheForm.getTipoModello()+".jrxml";
					String basePath=this.servlet.getServletContext().getRealPath(File.separator);

					//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
					String pathJrxml=basePath+File.separator+"jrxml"+File.separator+fileJrxml;
//					String pathDownload = basePath+File.separator+"download";
					String pathDownload = StampeUtil.getBatchFilesPath();

					String tipoFormato=stampaBibliotecheForm.getTipoFormato();
					request.setAttribute("TipoFormato", tipoFormato);
					String polo = Navigation.getInstance(request).getUtente().getCodPolo();
					String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
					UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
					String utente= user.getUserId();

					//codice standard inserimento messaggio di richiesta stampa differita
					StampaDiffVO stam = new StampaDiffVO();
					stam.setTipoStampa(tipoFormato);
					stam.setUser(utente);
					stam.setCodPolo(polo);
					stam.setCodBib(bibl);
					if(stampaBibliotecheForm.getBiblioteca().equals("bibliotecheBiblio")){
						stam.setTipoOrdinamento("BIBLIO");}
					else if(stampaBibliotecheForm.getBiblioteca().equals("bibliotechePolo")){
						stam.setTipoOrdinamento("POLO");
					}else {
						//ordinamento di stampa dell'arraylist di biblioteche verrà stampato così come restituito dal servizio
						stam.setTipoOrdinamento("");
					}
					stam.setParametri(inputForStampeService);
					stam.setTemplate(pathJrxml);
					stam.setDownload(pathDownload);
					stam.setDownloadLinkPath("/");
					stam.setTipoOperazione("STAMPA_BIBLIOTECHE");
					UtilityCastor util= new UtilityCastor();
					String dataCorr = util.getCurrentDate();
					stam.setData(dataCorr);
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					String idMessaggio = factory.getStampeOnline().stampaBiblioteche(stam);

					ActionMessages errors = new ActionMessages();
					idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
					errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
					this.saveErrors(request, errors);
/*					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.finestampa"));
					this.saveErrors(request, errors);*/
				}//Felse
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().INTERROGAZIONE_ANAGRAFICA_BIBLIOTECA);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private void stampaOnLine(HttpServletResponse response, List listaDati, String tipoStampa, String fileJrxml)throws Exception {
		//gv rivista allo scopo di eliminare il metodo presente SOLO per questa stampa.
		//TODO va resa conforme al resto (una volta capito a cosa renderla conforme).
		SbnStampe sbn = new SbnStampe(fileJrxml);
		sbn.setFormato(TipoStampa.toTipoStampa(tipoStampa));
		Map parametri = new HashMap();
		InputStream streamByte=sbn.stampa(listaDati, parametri);
		JasperPrint jasperPrint = sbn.getJasperPrint();
		String exportFileName=jasperPrint.getName();
		impostaResponse(response, exportFileName, tipoStampa);
		ServletOutputStream servletOutputStream =response.getOutputStream();
		SbnStampe.transferData(streamByte, servletOutputStream);//servletOutputStream.write(streamByte);
		streamByte.close();
		servletOutputStream.flush();
		servletOutputStream.close();

	}

	public HttpServletResponse getContentTypeResponse(TipoStampa tipoFormato, HttpServletResponse response){
		String tipo = "";
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

