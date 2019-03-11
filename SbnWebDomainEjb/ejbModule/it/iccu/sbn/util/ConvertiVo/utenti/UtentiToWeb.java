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
package it.iccu.sbn.util.ConvertiVo.utenti;

import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO.TipoEvento;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_accesso_utente;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.util.ConvertiVo.DataBindingConverter;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;

public class UtentiToWeb extends DataBindingConverter {

	private static final long serialVersionUID = 4047023941204608576L;

	UtentiDAO udao = new UtentiDAO();

	public EventoAccessoVO eventoAccesso(Tbl_accesso_utente hVO) throws DaoManagerException {

		EventoAccessoVO webVO = new EventoAccessoVO();
		fillBaseWeb(hVO, webVO);

		webVO.setId_evento(hVO.getId());
		webVO.setIdTessera(hVO.getId_tessera());
		webVO.setDataEvento(hVO.getTs_evento());
		webVO.setEvento(TipoEvento.of(hVO.getFl_evento()));

		boolean autenticato = hVO.getFl_autenticato() == 'S';
		webVO.setAutenticato(autenticato);

		Tbf_biblioteca_in_polo bib = hVO.getBiblioteca();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());

		Tbl_utenti utente = hVO.getUtente();
		if (utente != null)
			webVO.setUtente(ServiziConversioneVO.daHibernateAWebUtente(utente));

		boolean forzato = hVO.getFl_forzatura() == 'S';
		webVO.setForzato(forzato);

		return webVO;
	}

}
