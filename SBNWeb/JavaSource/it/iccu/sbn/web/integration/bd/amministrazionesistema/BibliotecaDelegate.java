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
package it.iccu.sbn.web.integration.bd.amministrazionesistema;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotechePoloVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheSistemaMetropolitanoVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class BibliotecaDelegate {

	public static final String LISTA_BIBLIOTECHE_AFFILIATE = "lista.bib.affiliate";
	public static final String ATTIVAZIONE_SIF = "sif.attivazione.params";
	public static final String PARAMETRI_RICERCA_BIBLIOTECA = "param.ricerca.biblio";
	public static final String BIBLIOTECHE_FILTRO_SISTEMA_METRO = "srv.codBibSif.metro";

	private static Logger log = Logger.getLogger(BibliotecaDelegate.class);

	private FactoryEJBDelegate factory;
	private UserVO utenteCollegato;
	private HttpServletRequest request;
	private Utente utenteEjb;

	protected void saveErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);

		if (errors == null || errors.isEmpty()) {
			request.removeAttribute("org.apache.struts.action.ERROR");
			return;
		} else {
			request.setAttribute("org.apache.struts.action.ERROR", errors);
			return;
		}
	}

	public static final BibliotecaDelegate getInstance(HttpServletRequest request) {
		return new BibliotecaDelegate(request);
	}

	public BibliotecaDelegate(FactoryEJBDelegate factory, HttpServletRequest request) {
		super();
		this.factory = factory;
		this.utenteCollegato = Navigation.getInstance(request).getUtente();
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute("UTENTE_BEAN");
	}

	public BibliotecaDelegate(HttpServletRequest request) {
		super();
		try {
			this.factory = FactoryEJBDelegate.getInstance();

			this.utenteCollegato = Navigation.getInstance(request).getUtente();
			this.request = request;
			this.utenteEjb = (Utente) request.getSession().getAttribute("UTENTE_BEAN");

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public BibliotecaVO getBiblioteca(String Cod_polo, String Cod_bib)
			throws Exception {
		return factory.getSistema().getBiblioteca(Cod_polo, Cod_bib);
	}

	void updateBiblioteca(BibliotecaVO bibliotecaVO) throws Exception {
		factory.getSistema().updateBiblioteca(bibliotecaVO);
	}

	public ActionForward getSIFListaBibliotecheAffiliatePerAttivita(
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta) {

		ActionMessages errors = new ActionMessages();
		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		try {
			DescrittoreBloccoVO blocco1 = factory.getSistema().getListaBibliotecheAffiliatePerAttivita(
					utenteCollegato.getTicket(), richiesta.getCodPolo(),
					richiesta.getCodBib(), richiesta.getCodAttivita(),
					richiesta.getElementiPerBlocco());

			if (blocco1.getTotRighe() < 1) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"ricerca.biblioteca.affiliata.null"));
				this.saveErrors(request, errors, null);
				return mapping.getInputForward();
			}

			log.debug("getSIFListaBibliotecheAffiliatePerAttivita()");
			navi.setAttribute(ATTIVAZIONE_SIF, richiesta);
			navi.setAttribute(LISTA_BIBLIOTECHE_AFFILIATE, blocco1);

			return navi.goForward(mapping.findForward("sifListaBibliotecheAffiliatePerAttivita"));

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		}

	}

	public Utente getUtenteEjb() {
		return utenteEjb;
	}

	public ActionForward getSIFListaBibliotecheSistemaMetropolitano(SIFListaBibliotecheSistemaMetropolitanoVO richiesta) {

		ActionMessages errors = new ActionMessages();
		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		try {
			DescrittoreBloccoVO blocco1 =
				factory.getSistema().getListaBibliotecheSistemaMetropolitano(
							utenteCollegato.getTicket(),
							richiesta.getCodPolo(), richiesta.getCodBib(),
							richiesta.getElementiPerBlocco());

			if (blocco1.getTotRighe() < 1) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"ricerca.biblioteca.affiliata.null"));
				this.saveErrors(request, errors, null);
				return mapping.getInputForward();
			}

			log.debug("getSIFListaBibliotecheSistemaMetropolitano()");
			navi.setAttribute(ATTIVAZIONE_SIF, richiesta);
			navi.setAttribute(LISTA_BIBLIOTECHE_AFFILIATE, blocco1);

			return navi.goForward(mapping.findForward("sifListaBibliotecheAffiliatePerAttivita"));

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		}

	}

	public ActionForward getSIFListaBibliotechePolo(SIFListaBibliotechePoloVO richiesta) {

		ActionMessages errors = new ActionMessages();
		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		try {
			DescrittoreBloccoVO blocco1 = factory.getSistema().getListaBibliotechePolo(
					utenteCollegato.getTicket(), richiesta.getCodPolo(),
					richiesta.getElementiPerBlocco());

			if (blocco1.getTotRighe() < 1) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"ricerca.biblioteca.affiliata.null"));
				this.saveErrors(request, errors, null);
				return mapping.getInputForward();
			}

			log.debug("getSIFListaBibliotechePolo()");
			navi.setAttribute(ATTIVAZIONE_SIF, richiesta);
			navi.setAttribute(LISTA_BIBLIOTECHE_AFFILIATE, blocco1);

			return navi.goForward(mapping.findForward("sifListaBibliotecheAffiliatePerAttivita"));

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();

		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();
		}

	}

	public DescrittoreBloccoVO caricaBlocco(String idLista,	int numBlocco) throws Exception {
		DescrittoreBloccoVO blocco = null;
		blocco = factory.getSistema().nextBlocco(utenteCollegato.getTicket(), idLista, numBlocco);
		return blocco;
	}

	public ActionForward getSIFListaBibliotecheILLFornitrici(BibliotecaRicercaVO richiesta) {
		log.debug("getSIFListaBibliotecheILLFornitrici()");
		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		navi.setAttribute(ATTIVAZIONE_SIF, richiesta);

		return navi.goForward(mapping.findForward("ricercaBiblioteca"));
	}

	public BibliotecaVO getBiblioteca(int idBiblioteca) throws Exception {
		BibliotecaVO biblioteca = FactoryEJBDelegate.getInstance().getBiblioteca().caricaBiblioteca(idBiblioteca);
		return biblioteca;
	}


}
