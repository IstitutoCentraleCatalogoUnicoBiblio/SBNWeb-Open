/**
 *
 */
package it.iccu.sbn.ejb.domain.bibliografica;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SifRicercaRepertoriVO;
import it.iccu.sbn.persistence.dao.bibliografica.Tb_repertorioDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Repertorio"
 * 		description="A session bean named Repertorio"
 *      display-name="Repertorio"
 *      jndi-name="sbnWeb/Repertorio"
 *      type="Stateless"
 *      transaction-type="Container"
 *      view-type = "remote"
 *
 * @ejb.util generate="no"
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class RepertorioBean extends TicketChecker implements SessionBean {

	private static final long serialVersionUID = -1112841699444812648L;

	private static Logger logger = Logger.getLogger(RepertorioBean.class);

	private SessionContext ctx;

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws NamingException
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() throws CreateException {
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException,
			RemoteException {
		this.ctx = ctx;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @throws NamingException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public void bindRepertori() throws DaoManagerException, NamingException {
		logger.debug("Caricamento repertori");
		UserTransaction tx = ctx.getUserTransaction();
		try {
			DaoManager.begin(tx);
			Tb_repertorioDao dao = new Tb_repertorioDao();
			List repertori = dao.selectAll();
			int size = ValidazioneDati.size(repertori);
			List lista = new ArrayList(size);
			for (int x = 0; x < size; x++) {
				Tb_repertorio rep = (Tb_repertorio) repertori.get(x);
				if (rep.getCd_sig_repertorio() == null)
					break;

				SifRicercaRepertoriVO elenRep = new SifRicercaRepertoriVO();
				elenRep.setTipo(String.valueOf(rep.getTp_repertorio()));
				elenRep.setDesc(rep.getDs_repertorio().trim());
				elenRep.setSigl(rep.getCd_sig_repertorio().trim());

				lista.add(elenRep);
			}
			JNDIUtil.getContext().rebind("sbn.repertori", lista);
			DaoManager.commit(tx);
		} catch (Exception e) {
			DaoManager.rollback(tx);
		} finally {

		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @throws NamingException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public List getAllRepertori() throws DaoManagerException, NamingException {

//		// Map<String, V>
		List result = (List)this.ctx.lookup("sbn.repertori");
		return result;
	}

	public void truncate(String ticket) throws EJBException {
		logger.debug("truncate repertori");
		checkTicket(ticket);
		UserTransaction tx = ctx.getUserTransaction();
		try {
			DaoManager.begin(tx);
			Tb_repertorioDao dao = new Tb_repertorioDao();
			dao.truncate(ticket);
			DaoManager.commit(tx);

		} catch (Exception e) {
			DaoManager.rollback(tx);
		} finally {

		}
	}

}
