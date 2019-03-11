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
package it.iccu.sbn.ejb.services.acquisizioni;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaFornitoriVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
 * @ejb.bean name="ServiziAcquisizioni" description="A session bean named
 *           ServiziAcquisizioni" display-name="ServiziAcquisizioni"
 *           jndi-name="sbnWeb/ServiziAcquisizioni" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class ServiziAcquisizioniBean implements
		javax.ejb.SessionBean {

	private static final long serialVersionUID = 5665614421565457711L;

	private static Logger logger = Logger
			.getLogger(ServiziAcquisizioniBean.class.getName());

	private it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni acquisizioni;

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
	}

	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
		try {
			this.acquisizioni = DomainEJBFactory.getInstance().getAcquisizioni();

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
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
			DataException, Exception, ValidationException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);
		switch (tipoStampa) {
		case STAMPA_BUONI_ORDINE:
			stampeOnline.setRigheDatiDB(this.stampaListaBO(parametri));
			break;
		case STAMPA_LISTA_FORNITORI:
			stampeOnline.setRigheDatiDB(this.stampaListaFornitori(ticket, parametri));
//		default:
//			throw new ValidationException("Tipo stampa non previsto");
		}

		return stampeOnline;
	}

	private List stampaListaFornitori(String ticket, List parametri)
	throws ResourceNotFoundException, Exception, ApplicationException, RemoteException {

	if (parametri.size() > 0 && ticket != null){
		StrutturaCombo strutNull = new StrutturaCombo("", "");
		StampaFornitoriVO sfvo = (StampaFornitoriVO)parametri.get(0);
		String polo = sfvo.getPolo();
		String biblio = sfvo.getBiblioteca();
		ListaSuppFornitoreVO lpVO= new  ListaSuppFornitoreVO (polo, biblio, sfvo.getCodiceFornitore(), sfvo.getNomeFornitore(), sfvo.getProfAcquisti(), sfvo.getPaese(),  sfvo.getTipoFornitore(), sfvo.getProvincia(), null, null);
		lpVO.setOrdinamento("");
		lpVO.setLocale(sfvo.getRicercaLocale());
		List listaFornitori = new ArrayList();
		listaFornitori = this.acquisizioni.getRicercaListaFornitori(lpVO);
		return listaFornitori;
	} else {
		throw new ApplicationException("Errore nei parametri di Stampa Lista Fornitori");
	}
}
	/**
     *
     * <!-- begin-xdoclet-definition -->
     *
     * @throws ResourceNotFoundException
     * @throws ApplicationException
     * @throws ValidationException
     * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
     * @generated
     *
     * //TODO: Must provide implementation for bean create stub
     */
	private List stampaListaBO(List parametri)
	throws ResourceNotFoundException, ApplicationException , RemoteException {

	if (parametri.size() > 0 && parametri != null){
		ListaSuppBuoniOrdineVO ricercaBuoniOrd = (ListaSuppBuoniOrdineVO) parametri.get(0);
		List listaBO = this.getRicercaListaBuoniOrd(ricercaBuoniOrd);
		return listaBO;
	} else {
		throw new ApplicationException("Errore nei parametri di Stampa Lista Buoni d'ordine");
	}

	}

	/**
     *
     * <!-- begin-xdoclet-definition -->
     *
     * @throws ResourceNotFoundException
     * @throws ApplicationException
     * @throws ValidationException
     * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
     * @generated
     *
     * //TODO: Must provide implementation for bean create stub
     */
	public List getRicercaListaBuoniOrd(ListaSuppBuoniOrdineVO ricercaBuoniOrd) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		List results = null;
		try {
			results=acquisizioni.getRicercaListaBuoniOrd(ricercaBuoniOrd) ;
/*			ConfigurazioneBOVO config=new ConfigurazioneBOVO();
			config.setCodPolo(ricercaBuoniOrd.getCodPolo());
			config.setCodBibl(ricercaBuoniOrd.getCodBibl());
			acquisizioni.loadConfigurazione(config);
*/

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

}
