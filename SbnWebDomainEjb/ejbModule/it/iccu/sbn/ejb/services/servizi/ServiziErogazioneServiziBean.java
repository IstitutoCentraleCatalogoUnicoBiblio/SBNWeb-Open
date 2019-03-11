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
package it.iccu.sbn.ejb.services.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="ServiziErogazioneServizi" description="A session bean named
 *           ServiziErogazioneServizi" display-name="ServiziErogazioneServizi"
 *           jndi-name="sbnWeb/ServiziErogazioneServizi" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class ServiziErogazioneServiziBean implements
		javax.ejb.SessionBean {

	private static final long serialVersionUID = -124635896104199407L;

	private static Logger logger = Logger.getLogger(ServiziErogazioneServiziBean.class);

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */

	private Servizi servizi;

	public void ejbCreate() {
		logger.info("creato ejb");
		try {
			this.servizi = DomainEJBFactory.getInstance().getServizi();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws RemoteException
	 * @throws ApplicationException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List parametri) throws RemoteException, ApplicationException,
			DataException, ValidationException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);
		switch (tipoStampa) {
		case STAMPA_LISTA_UTENTI:
			stampeOnline.setRigheDatiDB(stampaListaUtenti(ticket, parametri));
			break;
		default:
			throw new ValidationException("Tipo stampa non previsto");
		}

		return stampeOnline;
	}

	private List stampaListaUtenti(String ticket, List parametri)
		throws ResourceNotFoundException, ApplicationException , RemoteException {

		if (parametri.size() > 0 && ticket != null){
			RicercaUtenteBibliotecaVO ricerca = (RicercaUtenteBibliotecaVO) parametri.get(0);
			//TODO: provo a non passare il ticket...
			//	ricerca.setTicket(ticket);
			List listaUtenti = servizi.getListaUtenti(ticket, ricerca);
			return listaUtenti;
		} else {
			throw new ApplicationException("Errore nei parametri di Stampa Lista Utenti");
		}

	}
	private UtenteBibliotecaVO stampaTesserinoUtente(String ticket, List parametri)
		throws ResourceNotFoundException, ApplicationException, RemoteException {
	String numberFormat="";
	Locale locale = Locale.getDefault();
	if (parametri.size() > 0 && ticket != null){
		RicercaUtenteBibliotecaVO ricerca = (RicercaUtenteBibliotecaVO) parametri.get(0);
		UtenteBibliotecaVO SingoloUtente = servizi.getDettaglioUtente(ticket, ricerca, numberFormat, locale);
		ricerca.setTicket(ticket);
		return SingoloUtente;
	} else {
		throw new ApplicationException("Errore nei parametri di Stampa Ticket Utente");
	}

}

}
