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
package it.iccu.sbn.persistence.dao.servizi;

import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.util.Arrays;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

public abstract class ServiziBaseDAO extends DaoManager {

	private static final String SEPARATORE_CAMPI = "-";
	private static final String SEPARATORE_ORDINE = ":";

	private static final class NotEmptyPropertySelector implements PropertySelector {

		private static final long serialVersionUID = -1422802059676824900L;

		public boolean include(Object object, String propertyName, Type type) {
			if (object == null)
				return false;
			if ((object instanceof Number)
					&& ((Number) object).longValue() == 0L)
				return false;
			if ((object instanceof Character)
					&& ((Character) object).charValue() == 0)
				return false;
			if ((object instanceof String)
					&& ((String) object).trim().equals(""))
				return false;
			return true;
		}

	}

	protected static final PropertySelector NOT_EMPTY = new NotEmptyPropertySelector();

	protected static void createCriteriaOrder(String tipoOrdinamento, String alias, Criteria criteria) {
		if (tipoOrdinamento == null)
			return;
		if (alias == null) alias = "";
		if (!alias.equals("")) alias += '.';
		try {
			String[] campo_ordine = tipoOrdinamento.split(SEPARATORE_CAMPI);
			for (int i = 0; i < campo_ordine.length; i++) {
				String[] ordine = campo_ordine[i].split(SEPARATORE_ORDINE);
				if (ordine[1].equalsIgnoreCase("A"))
					criteria.addOrder(Order.asc(alias + ordine[0]));
				if (ordine[1].equalsIgnoreCase("D"))
					criteria.addOrder(Order.desc(alias + ordine[0]));
			}
		} catch (Exception e) {
			return;
		}
	}

	protected static void createCriteriaOrderIC(String tipoOrdinamento, String alias, Criteria criteria) {
		if (tipoOrdinamento == null)
			return;
		if (alias == null) alias = "";
		if (!alias.equals("")) alias += '.';
		try {
			String[] campo_ordine = tipoOrdinamento.split(SEPARATORE_CAMPI);
			for (int i = 0; i < campo_ordine.length; i++) {
				String[] ordine = campo_ordine[i].split(SEPARATORE_ORDINE);
				if (ordine[1].equalsIgnoreCase("A"))
					criteria.addOrder(Order.asc(alias + ordine[0]).ignoreCase());
				if (ordine[1].equalsIgnoreCase("D"))
					criteria.addOrder(Order.desc(alias + ordine[0]).ignoreCase());
			}
		} catch (Exception e) {
			return;
		}
	}

	protected static String createHQLOrder(String tipoOrdinamento, String alias) {
		if (tipoOrdinamento == null)
			return null;

		if (alias == null) alias = "";
		if (!alias.equals("")) alias += '.';
		try {
			String[] campo_ordine = tipoOrdinamento.split(SEPARATORE_CAMPI);

			Iterator<String> i = Arrays.asList(campo_ordine).iterator();
			StringBuilder order = new StringBuilder(64);
			order.append("order by ");
			for (;;) {
				String[] ordine = i.next().split(SEPARATORE_ORDINE);
				order.append(alias);
				order.append(ordine[0]).append(" ");
				if (ordine[1].equalsIgnoreCase("A"))
					order.append("ASC");
				else
					order.append("DESC");

				if (i.hasNext())
					order.append(", ");
				else
					break;
			}

			return order.toString();

		} catch (Exception e) {
			return null;
		}
	}

	protected Criterion impostaFiltroStatoRicMov(MovimentoVO filtroMov) {

		// impostao i criteri di ricerca per gli stati della richiesta e del movimento
		// in relazione a movimenti attivi e/o annullati e/o chiusi

		// inizializzo i flag sui dati di default
		boolean mov_incorso = true;
		boolean mov_respinti = false;
		boolean mov_chiusi = false;
		//Inizio modifica del 30/03/2010 almaviva
		boolean mov_prenotazioni = false;
		boolean utenteWeb = false;
		//Fine modifica del 30/03/2010 almaviva

		if (filtroMov instanceof MovimentoRicercaVO) {
			MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) filtroMov;
			// riporto quanto indicato come criterio di ricerca
			// sui movimenti in corso e/o respinti e/o chiusi
			mov_incorso = movRicerca.isRichiesteInCorso();
			mov_respinti = movRicerca.isRichiesteRespinte();
			mov_chiusi = movRicerca.isRichiesteEvase();
			mov_prenotazioni = movRicerca.isRichiestePrenotazioni();
			utenteWeb = movRicerca.isUtenteLettore();
		}

		// con Disjunction riesco a fare la or tra le 3
		// Restrinctions precedentemente create
		Disjunction or = Restrictions.disjunction();

		if (mov_incorso)
			if (utenteWeb)
				//almaviva5_20101220 #4069 aggiunto filtro stato richiesta
				or.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
				//almaviva5_20160608 servizi ILL. Aggiunto stato 'C' (inoltro a bib. destinataria)
						   Restrictions.in("cod_stato_rich", new String[]{"G", "N", "S", "P", "C"}) ),
						   Restrictions.and(Restrictions.isNotNull("cod_stato_mov"),
						   Restrictions.in("cod_stato_mov", new String[]{"A", "S"}))));
			else
				// in caso di movimenti in corso imposto gli opportuni stati del movimento
				or.add(Restrictions.or(Restrictions.isNull("cod_stato_mov"),
					       Restrictions.in("cod_stato_mov", new String[]{"A", "S", "P"}) ));


		if (mov_respinti)
			// in caso di movimenti respinti imposto gli opportuni stati della richiesta e del movimento
			or.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
				   Restrictions.in("cod_stato_rich", new String[]{"B","D","F"}) ),
				   Restrictions.and(Restrictions.isNotNull("cod_stato_mov"),
				   Restrictions.in("cod_stato_mov", new String[]{"C", "E"}))));


		if (mov_chiusi)
			if (utenteWeb)
				//almaviva5_20101220 #4069 aggiunto filtro stato richiesta
				or.add(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
						   Restrictions.in("cod_stato_rich", new String[]{"H"}) ));
			else
				// in caso di movimenti evasi imposto gli opportuni stati della richiesta e del movimento
				or.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
					   Restrictions.eq("cod_stato_rich", "H") ),
					   Restrictions.and(Restrictions.isNotNull("cod_stato_mov"),
					   Restrictions.eq("cod_stato_mov", "C"))));


		if (mov_prenotazioni)
			// in caso di movimenti di prenotazioni
			or.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
				   Restrictions.eq("cod_stato_rich", "A") ),
				   Restrictions.and(Restrictions.isNotNull("cod_stato_mov"),
				   Restrictions.eq("cod_stato_mov", "P"))));

		/*
		if (utenteWeb)
			// in caso di movimenti in corso imposto gli opportuni stati del movimento
			//almaviva5_20101220 #4069 aggiunto filtro stato richiesta
			or.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
					   Restrictions.in("cod_stato_rich", new String[]{"G", "N", "S", "P"}) ),
					   Restrictions.and(Restrictions.isNotNull("cod_stato_mov"),
					   Restrictions.in("cod_stato_mov", new String[]{"A", "S"}))));
		*/
		return or;
	}

}
