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


import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.SbnBusinessSessionBean;
import it.iccu.sbn.ejb.domain.statistiche.StatisticheSBN;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.remote.Statistiche;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.statistiche.AreaVO;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class StatisticheBean extends SbnBusinessSessionBean implements Statistiche {


	private static final long serialVersionUID = -2089845863089604554L;

	private static Logger log = Logger.getLogger(Statistiche.class);

	private StatisticheSBN statisticheSBN;

	public void ejbCreate() {
		log.info("creato ejb");
		try {
			this.statisticheSBN = DomainEJBFactory.getInstance().getStatistiche();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	public List<AreaVO> getAreeUtente(Map attivita) throws EJBException {
		List<AreaVO> output = new ArrayList<AreaVO>();
		try {
			output = statisticheSBN.getAreeUtente(attivita);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public DescrittoreBloccoVO getListaStatistiche(String area, String ticket, int elemBlocco) throws EJBException {
		List<StatisticaVO> output = new ArrayList<StatisticaVO>();
		DescrittoreBloccoVO blocco1 = null;
		try {
			output = this.statisticheSBN.getListaStatistiche(area, ticket);
			blocco1 = this.saveBlocchi(ticket, output, elemBlocco);
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blocco1;
	}

	public List<DettVarStatisticaVO> getDettVarStatistica(int idConfig, String ticket) throws EJBException {
		List<DettVarStatisticaVO> output = new ArrayList<DettVarStatisticaVO>();
		try {
			output = statisticheSBN.getDettVarStatistica(idConfig, ticket);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public List<CodiceVO> getListaCodDescrDaTabGenerica(String codPolo, String valori0/*codBib*/, String valori1/*tabella*/,
			 String  valori2/*codice*/,  String valori3/*descrizione*/, String valori4/*valore codBib*/, String ticket) throws EJBException {
		List<CodiceVO> output = new ArrayList<CodiceVO>();
		try {
			output = statisticheSBN.getListaCodDescrDaTabGenerica(codPolo, valori0/*codBib*/, valori1/*tabella*/,
					 valori2/*codice*/, valori3/*descrizione*/, valori4/*valore codBib*/, ticket);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

}
