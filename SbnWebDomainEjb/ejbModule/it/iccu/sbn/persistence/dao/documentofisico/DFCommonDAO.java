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
package it.iccu.sbn.persistence.dao.documentofisico;

import it.iccu.sbn.persistence.dao.common.AbstractJDBCManager;
import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class DFCommonDAO extends DaoManager {

	private static Logger log = Logger.getLogger(DFCommonDAO.class);

	protected static final char BLANK = '\u0020';

	protected static final Criterion intervalloColloc1(String collDa,
			String specDa, String collA, String specA) {
		StringBuffer sql = new StringBuffer();
		sql.append("rpad({alias}.ord_loc,80)||rpad({alias}.ord_spec,40) between '");
		sql.append(collDa + specDa);
		sql.append("' and '");
		sql.append(collA + specA);
		sql.append("'");
		return Restrictions.sqlRestriction(sql.toString());
	}

	protected static final Criterion intervalloColloc2(String codSerie,
			int codInv) {
		StringBuffer sql = new StringBuffer();
		sql.append("((");
		sql.append("{alias}.cd_serie = '");
		sql.append(codSerie);
		sql.append("' and {alias}.cd_inven >= ");
		sql.append(codInv);
		sql.append(")");
		sql.append(" or ({alias}.cd_serie >'");
		sql.append(codSerie);
		sql.append("'))");
		return Restrictions.sqlRestriction(sql.toString());
	}

	protected static final Criterion daOrdLocAOrdLocOrdSpec(String daOrdLoc,
			String aOrdLoc, String daSpec) {

		StringBuffer sql = new StringBuffer();
		sql.append("({alias}.ord_loc ").append(" between ");
		sql.append("'").append(daOrdLoc).append("'");
		sql.append(" and ");
		sql.append("'").append(aOrdLoc).append("'");
		sql.append(" and ");
		sql.append("rpad({alias}.ord_loc,80)||rpad({alias}.ord_spec,40) > '");
		sql.append(daOrdLoc + daSpec);
		sql.append("')");

		return Restrictions.sqlRestriction(sql.toString());
	}

	protected static final Criterion daOrdLocAOrdLoc(String daOrdLoc,
			String aOrdLoc) {
		StringBuffer sql = new StringBuffer();
		sql.append("({alias}.ord_loc > '");
		sql.append(daOrdLoc);
		sql.append("' and {alias}.ord_loc <='");
		sql.append(aOrdLoc);
		sql.append("')");
		return Restrictions.sqlRestriction(sql.toString());
	}

	protected static final Criterion daOrdLocAOrdLocDesc(String daOrdLoc,
			String aOrdLoc) {
		StringBuffer sql = new StringBuffer();
		sql.append("({alias}.ord_loc >= '");
		sql.append(daOrdLoc);
		sql.append("' and {alias}.ord_loc <'");
		sql.append(aOrdLoc);
		sql.append("')");
		return Restrictions.sqlRestriction(sql.toString());
	}

	protected static final Criterion ordLocOrdSpecKeyLoc(String ordLoc,
			String ordSpec, int keyLoc) {
		StringBuffer sql = new StringBuffer();
		sql.append("({alias}.ord_loc = '");
		sql.append(ordLoc);
		sql.append("' and {alias}.ord_spec ='");
		sql.append(ordSpec);
		sql.append("' and {alias}.key_loc >");
		sql.append(keyLoc);
		sql.append(")");
		return Restrictions.sqlRestriction(sql.toString());
	}

	protected static final Criterion ordLocDaSpecASpec(String ordLoc,
			String daSpec, String aSpec) {
		StringBuffer sql = new StringBuffer();
		sql.append("({alias}.ord_loc = '");
		sql.append(ordLoc);
		sql.append("' and {alias}.ord_spec >'");
		sql.append(daSpec);
		sql.append("' and {alias}.ord_spec <='");
		sql.append(aSpec);
		sql.append("')");
		return Restrictions.sqlRestriction(sql.toString());
	}

	protected void aggiungiFiltroInventariDigitalizzati(StringBuilder buf,
			boolean escludiDigit, String tipoDigit) {

		// almaviva5_20130910 evolutive google2
		log.debug("aggiungi Filtri Inventario digitalizzato");

		StringBuilder sql = new StringBuilder();
		sql.append("left join tbc_inventario i2 on i2.fl_canc<>'S' ");
		sql.append("and i2.cd_sit in ('1', '2') ");
		sql.append("and i2.cd_polo=inv.cd_polo ");
		sql.append("and i2.cd_bib=inv.cd_bib ");
		sql.append("and i2.bid=inv.bid ");
		sql.append("and (i2.cd_serie<>inv.cd_serie or i2.cd_inven<>inv.cd_inven) ");
		sql.append("and (i2.digitalizzazione in ('0', '1', '2') ");
		sql.append("or i2.cd_no_disp in ( ");
		sql.append("select cod.cd_tabella ");
		sql.append("from tb_codici cod ");
		sql.append("where true ");
		sql.append("and cod.tp_tabella = 'CCND' ");
		sql.append("and cod.cd_flg2 = 'S' ");
		sql.append("and :filtroDigit ");
		sql.append(" ) )").append(BLANK);
		String filtro = sql.toString();

		tipoDigit = isFilled(tipoDigit) ? tipoDigit : "C";
		String inClause = null;

		if ("T".equals(tipoDigit))
			// escludi parziale e completa
			inClause = AbstractJDBCManager.buildINClause("cod.cd_flg4",	Arrays.asList(new String[] { "0", "1" }));
		if ("C".equals(tipoDigit))
			// escludi completa
			inClause = AbstractJDBCManager.buildINClause("cod.cd_flg4",	Arrays.asList(new String[] { "1" }));
		if ("P".equals(tipoDigit))
			// escludi parziale e completa
			inClause = AbstractJDBCManager.buildINClause("cod.cd_flg4",	Arrays.asList(new String[] { "0" }));

		filtro = filtro.replace(":filtroDigit", inClause);

		buf.append(BLANK).append(filtro);
	}

}
