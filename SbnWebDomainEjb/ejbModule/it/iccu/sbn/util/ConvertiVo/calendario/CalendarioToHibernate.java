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
package it.iccu.sbn.util.ConvertiVo.calendario;

import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_modello_calendario;

public class CalendarioToHibernate extends CalendarioDataBinder {

	private static final long serialVersionUID = 4638264174295768172L;

	private SaleDAO dao = new SaleDAO();
	private Tbf_biblioteca_in_poloDao bibDAO = new Tbf_biblioteca_in_poloDao();

	public final Tbl_modello_calendario modelloCalendario(ModelloCalendarioVO webVO) throws DaoManagerException {
		if (webVO == null)
			return null;

		Tbl_modello_calendario hVO = new Tbl_modello_calendario();
		hVO.setId_modello(webVO.getId());
		hVO.setBiblioteca(bibDAO.select(webVO.getCodPolo(), webVO.getCodBib()));
		hVO.setDescrizione(webVO.getDescrizione());
		hVO.setDt_inizio(webVO.getInizio());
		hVO.setDt_fine(webVO.getFine());
		hVO.setCd_tipo(webVO.getTipo().getCd_tipo());
		hVO.setCd_cat_mediazione(webVO.getCd_cat_mediazione());

		String json = builder.create().toJson(webVO);
		hVO.setJson_modello(json);

		fillBaseHibernate(hVO, webVO);

		SalaVO sala = webVO.getSala();
		if (sala != null) {
			hVO.setSala(dao.getSalaById(sala.getIdSala()));
		}

		return hVO;
	}

}
