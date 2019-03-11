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

import it.iccu.sbn.ejb.AbstractStatelessSessionBean;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.remote.GestioneDefault;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.AreaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.GruppoVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GestioneDefaultBean extends AbstractStatelessSessionBean implements GestioneDefault {


	private static final long serialVersionUID = -5576072159518919026L;
	static Logger log = Logger.getLogger(GestioneDefault.class);

    public List<GruppoVO> getDefaultUtente(int Utente, String idArea, Map attivita, String idBiblioteca, String idPolo) throws java.rmi.RemoteException {
		List<GruppoVO> output = null;
		try {
				output= DomainEJBFactory.getInstance().getAmministrazioneGestioneDefault().getDefUtente(Utente, idArea, attivita, idBiblioteca, idPolo);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public Map<String, String> getTuttiDefaultUtente(String idBiblioteca, String idPolo, Map attivita) throws java.rmi.RemoteException {
    	Map<String, String> output = null;
		try {
			output= DomainEJBFactory.getInstance().getAmministrazioneGestioneDefault().getTuttiDefaultUtente(idBiblioteca, idPolo, attivita);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public boolean setDefaultUtente(int idUtente, List<GruppoVO> campi, String idBiblioteca, String idPolo) throws java.rmi.RemoteException {
		try {
			return DomainEJBFactory.getInstance().getAmministrazioneGestioneDefault().setDefaultUtente(idUtente, campi, idBiblioteca, idPolo);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    public List<AreaVO> getAreeUtente(Map attivita) throws java.rmi.RemoteException {
    	List<AreaVO> output = new ArrayList<AreaVO>();
    	try {
			output = DomainEJBFactory.getInstance().getAmministrazioneGestioneDefault().getAreeUtente(attivita);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }
}
