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
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.persistence.dao.servizi.UtilitaDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_accesso_utente;
import it.iccu.sbn.util.ConvertiVo.DataBindingConverter;

public class UtentiToHibernate extends DataBindingConverter {

	private static final long serialVersionUID = -2661660874521998301L;



	public Tbl_accesso_utente eventoAccesso(EventoAccessoVO webVO) throws DaoManagerException {

		Tbl_accesso_utente hVO = new Tbl_accesso_utente();
		fillBaseHibernate(hVO, webVO);

		UtilitaDAO dao = new UtilitaDAO();
		Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(webVO.getCodPolo(), webVO.getCodBib());
		hVO.setBiblioteca(bib);

		UtenteBaseVO utente = webVO.getUtente();
		if (utente != null) {
			UtentiDAO udao = new UtentiDAO();
			hVO.setUtente(udao.getUtenteAnagraficaById(utente.getIdUtente()) );
		}
	/*
		Integer posto = webVO.getPosto();
		if (posto != null) {
			SaleDAO sdao = SaleDAO();
			posto = sdao.getPostoById(posto);
			hVO.setPosto(posto);
		}
	*/
		hVO.setId_tessera(webVO.getIdTessera());
		hVO.setTs_evento(webVO.getDataEvento());
		hVO.setFl_evento(webVO.getEvento().getFl_evento());

		hVO.setFl_autenticato(webVO.isAutenticato() ? 'S' : 'N');
		hVO.setFl_forzatura(webVO.isForzato() ? 'S' : 'N');

		return hVO;
	}

}
