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
package it.iccu.sbn.ejb.domain.servizi.calendario;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.sale.Sale;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.CalendarioDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_modello_calendario;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConvertToHibernate;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.vo.validators.calendario.ModelloCalendarioValidator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.util.List;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;

public class CalendarioBean extends TicketChecker implements Calendario {

	private static final long serialVersionUID = -1319760098962915075L;

	private CalendarioDAO dao = new CalendarioDAO();

	static Reference<Sale> sale = new Reference<Sale>() {
		@Override
		protected Sale init() throws Exception {
			return DomainEJBFactory.getInstance().getSale();
		}};

	public ModelloCalendarioVO aggiornaModelloCalendario(String ticket, ModelloCalendarioVO modello) throws SbnBaseException {
		try {
			checkTicket(ticket);
			modello.validate(new ModelloCalendarioValidator() );

			// verifica congruenza calendario con quello di biblioteca
			checkCongruenzaCalendarioBib(ticket, modello);

			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			if (modello.isNuovo()) {
				modello.setTsIns(DaoManager.now());
				modello.setUteIns(firmaUtente);
				modello.setFlCanc("N");
			}
			modello.setUteVar(firmaUtente);

			Tbl_modello_calendario tbl_modello_calendario = ConvertToHibernate.Calendario.modelloCalendario(modello);
			tbl_modello_calendario = dao.aggiornaCalendario(tbl_modello_calendario);

			ModelloCalendarioVO updated = ConvertToWeb.Calendario.modelloCalendario(tbl_modello_calendario);

			return updated;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION, e);
		}
	}

	private void checkCongruenzaCalendarioBib(String ticket, ModelloCalendarioVO modello) throws SbnBaseException {
		if (modello.getTipo() == TipoCalendario.BIBLIOTECA)
			return;

		ModelloCalendarioVO modelloBib = getCalendarioBiblioteca(ticket, modello.getCodPolo(), modello.getCodBib());
		if (modelloBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_BIB_NON_TROVATO);

		//TODO: da controllare
		//CalendarioUtil.checkCongruenzaCalendario(modello, modelloBib);
	}

	public ModelloCalendarioVO getCalendarioBiblioteca(String ticket, String codPolo, String codBib) throws SbnBaseException {

		try {
			checkTicket(ticket);
			Tbl_modello_calendario tbl_modello_calendario = dao.getCalendarioBiblioteca(codPolo, codBib);
			ModelloCalendarioVO modello = ConvertToWeb.Calendario.modelloCalendario(tbl_modello_calendario);

			return modello;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public ModelloCalendarioVO getCalendarioCategoriaMediazione(String ticket, String codPolo, String codBib,
			String cd_cat_mediazione) throws SbnBaseException {

		try {
			checkTicket(ticket);
			Tbl_modello_calendario tbl_modello_calendario = dao.getCalendarioCategoriaMediazione(codPolo, codBib, cd_cat_mediazione);
			ModelloCalendarioVO modello = ConvertToWeb.Calendario.modelloCalendario(tbl_modello_calendario);

			return modello;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public ModelloCalendarioVO getCalendarioSala(String ticket, SalaVO sala) throws SbnBaseException {

		try {
			checkTicket(ticket);
			List<Tbl_modello_calendario> modelli = dao.getCalendariSala(ConvertToHibernate.Sale.sala(null, sala));
			Tbl_modello_calendario tbl_modello_calendario = first(modelli);
			ModelloCalendarioVO modello = ConvertToWeb.Calendario.modelloCalendario(tbl_modello_calendario);

			return modello;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public CalendarioVO cancellaModelloCalendario(String ticket, ModelloCalendarioVO modello) throws SbnBaseException {
		modello.setFlCanc("S");
		return aggiornaModelloCalendario(ticket, modello);
	}

	public List<GiornoVO> getGrigliaCalendario(String ticket, RicercaGrigliaCalendarioVO ricerca) throws SbnBaseException {

		checkTicket(ticket);
		Timestamp now = DaoManager.now();
		GrigliaCalendario gc = new GrigliaCalendario(ticket, ricerca, now);

		return gc.getGrigliaCalendario();
	}

}
