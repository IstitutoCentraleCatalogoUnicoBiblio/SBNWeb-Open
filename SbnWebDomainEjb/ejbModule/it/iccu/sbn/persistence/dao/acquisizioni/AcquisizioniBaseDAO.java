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
package it.iccu.sbn.persistence.dao.acquisizioni;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.common.AbstractJDBCManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.rtrim;

public abstract class AcquisizioniBaseDAO extends AbstractJDBCManager {

	static Logger log = Logger.getLogger(AcquisizioniBaseDAO.class);

	public class ChiaveOrdine {

		String tipo = "";
		String anno = "";
		String cod = "";
		String anno_abb = "";
		String id_ordine = "";
		String bib = "";

		ChiaveOrdine() {}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append("[");
			if (bib != null)
				buf.append("bib=").append(bib).append(", ");
			if (tipo != null)
				buf.append("tipo=").append(tipo).append(", ");
			if (anno != null)
				buf.append("anno=").append(anno).append(", ");
			if (cod != null)
				buf.append("cod=").append(cod).append(", ");
			if (anno_abb != null)
				buf.append("anno_abb=").append(anno_abb).append(", ");
			if (id_ordine != null)
				buf.append("id_ordine=").append(id_ordine);
			buf.append("]");
			return buf.toString();
		}

		public String getTipo() {
			return tipo;
		}

		public String getAnno() {
			return anno;
		}

		public String getCod() {
			return cod;
		}

		public String getAnno_abb() {
			return anno_abb;
		}

		public String getId_ordine() {
			return id_ordine;
		}

		public String getBib() {
			return bib;
		}
	}

	protected static final int addParam(PreparedStatement ps, int idx,
			Object param) throws SQLException {
		int cnt = idx + 1;
		if (param instanceof String) {
			String p = (String) param;
			if (!isFilled(p))
				return idx;

			ps.setString(cnt, rtrim(p));
			return cnt;
		}

		if (param instanceof Integer) {
			Integer p = (Integer) param;
			if (!isFilled(p))
				return idx;

			ps.setInt(cnt, p.intValue());
			return cnt;
		}

		if (param instanceof Long) {
			Long p = (Long) param;
			if (!isFilled(p))
				return idx;

			ps.setLong(cnt, p.longValue());
			return cnt;
		}

		return idx;
	}

	public AcquisizioniBaseDAO() {
		super();
	}

	protected List<ChiaveOrdine> catenaRinnovi(int idPrimoOrd,
			ChiaveOrdine parCatena) throws DataException, ApplicationException,
			ValidationException {
		List<ChiaveOrdine> arrayCatenaOrdiniRinnovati = null;
		ChiaveOrdine ordineCatena = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			// riceve l'id di un ordine e la terna identificativa del primo ordine
			StringBuilder sql = new StringBuilder(512);
			sql.append("select ord.id_ordine, ord.cod_tip_ord, ord.anno_ord, ord.cod_ord,");
			sql.append(" ord.anno_1ord, ord.cod_1ord, ord.rinnovato, ord.anno_abb, ord.cd_bib");
			sql.append(" from  tba_ordini ord ");
			sql.append(" where  ord.fl_canc<>'S' ");
			if (parCatena != null
					&& ValidazioneDati.isFilled(parCatena.tipo)
					&& ValidazioneDati.isFilled(parCatena.anno)
					&& ValidazioneDati.isFilled(parCatena.cod)) {
				sql.append(" and ord.cod_tip_ord='").append(parCatena.tipo.trim()).append("'");	//tipo ordine
				sql.append(" and ord.anno_1ord=").append(parCatena.anno.trim());					//anno ordine
				sql.append(" and ord.cod_1ord=").append(parCatena.cod.trim());					//num. ordine

				//almaviva5_20151221 segnalazione CFI
				sql.append(" and ord.cd_bib='").append(parCatena.bib).append("'");				//bib ordine
			}
			if (idPrimoOrd > 0) {
				sql.append(" and ord.id_ordine=").append(idPrimoOrd);
			}
			sql.append(" and ord.continuativo='1' ");
			//almaviva5_20160531 escludi annullati
			sql.append(" and ord.stato_ordine <> 'N' ");
			sql.append(" order by anno_abb");	//ord.ts_ins

			pstmt = connection.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			String tipoOrdAppo = "";
			String anno1OrdAppo = "";
			String cod1OrdAppo = "";
			String annoAbbAppo = "";
			String idAppo = "";
			arrayCatenaOrdiniRinnovati = new ArrayList<ChiaveOrdine>();

			// va aggiunto l'origine come primo elemento della catena
			if (parCatena != null
					&& ValidazioneDati.isFilled(parCatena.tipo)
					&& ValidazioneDati.isFilled(parCatena.anno)
					&& ValidazioneDati.isFilled(parCatena.cod)) {
				arrayCatenaOrdiniRinnovati.add(parCatena);
			}

			while (rs.next()) {
				ordineCatena = new ChiaveOrdine();
				tipoOrdAppo = rs.getString("cod_tip_ord");
				anno1OrdAppo = String.valueOf(rs.getInt("anno_ord"));
				cod1OrdAppo = String.valueOf(rs.getInt("cod_ord"));
				annoAbbAppo = String.valueOf(rs.getInt("anno_abb"));
				idAppo = String.valueOf(rs.getInt("id_ordine"));
				ordineCatena.tipo = (tipoOrdAppo);
				ordineCatena.anno = (anno1OrdAppo);
				ordineCatena.cod = (cod1OrdAppo);
				ordineCatena.anno_abb = (annoAbbAppo);
				ordineCatena.id_ordine = (idAppo);
				ordineCatena.bib = (rs.getString("cd_bib"));
				arrayCatenaOrdiniRinnovati.add(ordineCatena);
			} // End while

			rs.close();
			pstmt.close();
			connection.close();

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		return arrayCatenaOrdiniRinnovati;
	}

	public String struttura(String sqlString) {
		int pos = 0;
		pos = sqlString.indexOf("where");
		if (pos > 0) {
			sqlString += " and ";
		} else {
			sqlString += " where ";
		}

		return sqlString;
	}

}
