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
package it.finsiel.sbn.polo.factoring.util;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tb_titolo;

public class TimbroCondivisione {


	public Tb_titolo modificaTimbroCondivisione(Tb_titolo tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{

		// // Inizio Intervento almaviva2 30.05.2011 su richiesta /almaviva1
		// nel caso di presenza gestionali non si interrompe la procedura ma si aggiorna allo stato di "non condiviso" così
		// da consentire lo spostamento dei gestionali e poi si esce; si elimina il controllo che impedisce di trasformare un
		// Documento da Condiviso a Locale
//		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
//			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}
	public Tb_autore modificaTimbroCondivisione(Tb_autore tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{
		// almaviva2 31 Luglio 2009 Esplicita richiesta fatta con apposita funzione di servizio richiesta da contardi/almaviva1
//		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
//			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}
	public Tb_marca modificaTimbroCondivisione(Tb_marca tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{
		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}
	public Tb_descrittore modificaTimbroCondivisione(Tb_descrittore tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{
		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}
	public Tb_soggetto modificaTimbroCondivisione(Tb_soggetto tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{
		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}
	public Tb_classe modificaTimbroCondivisione(Tb_classe tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{
		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}
	public Tb_termine_thesauro modificaTimbroCondivisione(Tb_termine_thesauro tabella,String user,String _condiviso) throws EccezioneSbnDiagnostico{
		if((tabella.getFL_CONDIVISO().equals("s")) && _condiviso.equals("n"))
			throw new EccezioneSbnDiagnostico(7667, "La condivisione risulta attiva non può essere eliminata ");
		if(!tabella.getFL_CONDIVISO().equals(_condiviso)) {
			tabella.setFL_CONDIVISO(_condiviso);
			tabella.setTS_CONDIVISO(TableDao.now());
			tabella.setUTE_CONDIVISO(user);
		}
		return tabella;
	}

}
