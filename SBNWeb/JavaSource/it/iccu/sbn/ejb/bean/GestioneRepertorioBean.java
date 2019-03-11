/**
 *
 */
package it.iccu.sbn.ejb.bean;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.remote.GestioneRepertorio;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.ticket.TicketChecker;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class GestioneRepertorioBean extends TicketChecker implements GestioneRepertorio {


	private static final long serialVersionUID = 1739817663250639612L;
	private static Logger log = Logger.getLogger(GestioneRepertorio.class);

/* Commento per Fabrizio*/
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		log.debug("creato ejb");
	}

	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {
	}

	public void unsetSessionContext() {
	}

	public void ejbRemove() {
	}



	public List getAllRepertori() {

		// // Map<String, V>

		it.iccu.sbn.ejb.domain.bibliografica.RepertorioHome dhome;
		try {
			return DomainEJBFactory.getInstance().getRepertorio().getAllRepertori();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
