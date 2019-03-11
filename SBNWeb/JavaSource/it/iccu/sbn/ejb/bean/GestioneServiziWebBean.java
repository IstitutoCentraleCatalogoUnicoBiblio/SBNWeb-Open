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
package it.iccu.sbn.ejb.bean;

//import it.iccu.sbn.ejb.AbstractStatelessSessionBean;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.SbnBusinessSessionBean;
import it.iccu.sbn.ejb.domain.servizi.serviziweb.ServiziWeb;
import it.iccu.sbn.ejb.remote.GestioneServiziWeb;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotFoundException;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class GestioneServiziWebBean extends SbnBusinessSessionBean implements GestioneServiziWeb {


	private static final long serialVersionUID = -2100803560329867313L;

	private static Logger log = Logger.getLogger(GestioneServiziWeb.class);

	private ServiziWeb servizi;

	public ServiziWeb getServizi() {
		if (servizi != null)
			return servizi;

		try {
			this.servizi = DomainEJBFactory.getInstance().getServiziWeb();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		return servizi;
	}

	public void ejbCreate() {
		log.info("creato ejb");
	}

	// almaviva 2009
	public boolean setRemote(UtenteWeb utente, String polo, String cdBib, InetAddress inetAddress) throws EJBException,UtenteNotFoundException {
	try {
		return getServizi().setRemote(utente, polo, cdBib, inetAddress);
	} catch (Exception e) {
		//e.printStackTrace();

		throw new UtenteNotFoundException("Indirizzo ip non valido");
	}
}
	public UtenteWeb login(String ticket, String userId, String password, String sip2_ticket, InetAddress inetAddress  ) throws EJBException,UtenteNotFoundException {
		try {
			return getServizi().login(ticket, userId, password, sip2_ticket, inetAddress);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new UtenteNotFoundException("Utente e/o password non validi");
		}
	}
	public UtenteWeb biblioDest() throws EJBException {
		try {
			return getServizi().biblioDest();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	public DocumentoNonSbnVO ricercaPerSegnatura(String polo, String codBib,String segnatura) throws EJBException {
		try {
			return getServizi().ricercaPerSegnatura(polo, codBib, segnatura);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	public UtenteWeb tipoDoc() throws EJBException {
		try {
			return getServizi().tipoDoc();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	public boolean inserisciNuovoDoc(DocumentoNonSbnVO doc) throws EJBException {
		try {
			return getServizi().inserisciNuovoDoc(doc);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	public UtenteWeb modErog() throws EJBException {
		try {
			return getServizi().modErog();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	public UtenteWeb supporto() throws EJBException {
		try {
			return getServizi().supporto();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	public UtenteWeb listaServ(Integer idUte) throws EJBException {

		try {
			return getServizi().listaServ(idUte);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	public UtenteWeb listaServILL() throws EJBException {

		try {
			return getServizi().listaServILL();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	public List dirittiUtente(Integer idUtente) throws EJBException {

		try {
			return getServizi().dirittiUtente(idUtente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	public List dirittiDocumento(DocumentoNonSbnVO datiDocumento) throws EJBException {

		try {
			return getServizi().dirittiDocumento(datiDocumento);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	// almaviva 2009
	public boolean updateUtentiBib(Integer id_utente) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException{
	try {
		return getServizi().updateUtentiBib(id_utente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//


	// almaviva 2009
	public boolean cambioPwd(String ticket, String userId, String pwdNew) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException{
	try {
		return getServizi().cambioPwd(ticket, userId, pwdNew) ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//
	// almaviva 2009
	//
	public UtenteWeb recuperoPassword(String username) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException{
	try {
		return getServizi().recuperoPassword(username) ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//
	// almaviva 2009
	//
	public List listaBiblioIscritto(String cdpolo, Integer codUte) throws EJBException {

		try {
			return getServizi().listaBiblioIscritto(cdpolo,codUte);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	//
	// almaviva 2009
	//
	public List listaBiblioAuto(String cdpolo, Integer codUte, List cdBib) throws EJBException {

		try {
			return getServizi().listaBiblioAuto(cdpolo,codUte,cdBib);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	//
	// almaviva 2009
	//
	public List listaBiblioNonIscr(String cdpolo, Integer codUte, List cdBib) throws EJBException {

		try {
			return getServizi().listaBiblioNonIscr(cdpolo,codUte,cdBib);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//
	// almaviva 2009
	//
	public List getListaBibAutoregistrazione(String cdpolo) throws EJBException {

		try {
			return getServizi().getListaBibAutoregistrazione(cdpolo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//
	// almaviva 2009
	//

	public boolean inserimentoUtenteWeb(UtenteBibliotecaVO utente) throws EJBException {
		try {
			return getServizi().inserimentoUtenteWeb(utente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//
	public UtenteWeb esistenzaUtenteWeb(String codfiscale, String mail) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException{
	try {
		return getServizi().esistenzaUtenteWeb(codfiscale, mail) ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	public List getListaServiziAutorizzazione(String ticket, UtenteBibliotecaVO utente) throws EJBException {
		try {
			return getServizi().getListaServiziAutorizzazione(ticket, utente);
			} catch (Exception e) {
				e.printStackTrace();
				throw new EJBException(e);
			}
	}

	// almaviva 2009
	//
	public List controlloBibRicOpac(String cdpolo, Integer codUte, String cdBib) throws EJBException {

		try {
			return getServizi().controlloBibRicOpac(cdpolo,codUte,cdBib);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	// almaviva 2009
	//

	public int getLimMax(String polo) throws EJBException {
		try {
			return getServizi().getLimMax(polo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	// almaviva 2009
	//

	public int contaRangeSegnature(String polo, String codBib) throws EJBException {
		try {
			return getServizi().contaRangeSegnature(polo,codBib);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}
	//
	//
} // End SezioniWebBean
