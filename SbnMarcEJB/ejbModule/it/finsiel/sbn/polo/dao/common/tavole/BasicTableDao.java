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
package it.finsiel.sbn.polo.dao.common.tavole;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo;
import it.finsiel.sbn.util.ConnectionFactory;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;

public class BasicTableDao extends KeyParameter {
	protected Criteria basicCriteria;

	protected static Logger log = Logger.getLogger("sbnmarcPolo");

	public BasicTableDao() {
		super();
	}

	public static final Tbf_biblioteca_in_polo creaIdBib(String codPolo, String codBib) {
		Tbf_polo polo = new Tbf_polo();
		polo.setCd_polo(codPolo);
		Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
		bib.setCd_biblioteca(codBib);
		bib.setCd_polo(polo);
		bib.setFl_canc('N');
		return bib;
	}

	private ConnectionFactory factory = ConnectionFactory.getInstance();

	public boolean isPostgresVersion83() throws InfrastructureException {
		Integer dbversion = factory.getDbversion();
		return (dbversion >= 0);
	}

	public Session getCurrentSession() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		return factory.getSession();
	}

	public Session getSession() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		return factory.getSession();
	}

	public Session getSession2() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		return factory.getSession2();
	}

    public Session getSessionUpdate(Interceptor interc) throws InfrastructureException {
        // log.info("=== DBMessaging.beginTransaction()");
        try {
        	return factory.getSessionUpdate(interc);
        } catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

	public void beginTransaction() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		factory.beginTransaction();
	}

	public void commitTransaction() throws InfrastructureException {
		// Flush the Session and commit the transaction
		// log.info("=== DBMessaging.endTransaction() : Commit ");
		factory.commitTransaction();
	}

	public void rollbackTransaction() throws InfrastructureException {
		// Don't commit the transaction, can be faster for read-only operations
		// log.info("=== DBMessaging.endTransaction() : ROLLBACK !!! ");
		factory.rollbackTransaction();
	}

	public void closeSession() {
		// log.info("=== DBMessaging.endSession()");
		factory.closeSession();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

    public static boolean isSessionPostgreSQL(Session session) throws InfrastructureException {
        String dialect = ((SessionImpl) session).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnPostgreSQLDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }

    public boolean isSessionPostgreSQL() throws InfrastructureException {
		CriteriaImpl apses = (CriteriaImpl) this.basicCriteria;
		Session ses = (Session)apses.getSession();


        String dialect = ((SessionImpl) ses).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnPostgreSQLDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }

    public boolean isSessionOracle() throws InfrastructureException {
		CriteriaImpl apses = (CriteriaImpl) this.basicCriteria;
		Session ses = (Session)apses.getSession();
        String dialect = ((SessionImpl) ses).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnOracleDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }

    public static boolean isSessionOracle(Session session) throws InfrastructureException {
        String dialect = ((SessionImpl) session).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnOracleDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }

	/**
	 * FINE GESTIONE HIBERNATE
	 */

	private String[] convertArray(List value) {
		if (value.size() != 0) {
			String[] ret = new String[value.size()];
			for (int index = 0; index < value.size(); index++) {
				ret[index] = (String) value.get(index);
			}
			return ret;
		} else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD in(1,2,....,n)
	 */
	protected Criterion setParameterIn(Parameter parameter) {
		String[] ret;
		if ((ret = this.convertArray((List) parameter.getValue())) != null) {
			return Restrictions.in(parameter.getKey(), ret);
		} else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD = value
	 */
	protected Criterion setParameterEq(Parameter parameter) {
		if (parameter.isValid()) {
			// log.debug(Restrictions.eqPropertytoSqlString());
			return Restrictions.eq(parameter.getKey(), parameter.getValue());
		} else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD != value
	 */
	protected Criterion setParameterNotEq(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.ne(parameter.getKey(), parameter.getValue());
		else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD >= value
	 */
	protected Criterion setParameterGe(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.ge(parameter.getKey(), parameter.getValue());
		else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD <= value
	 */
	protected Criterion setParameterLe(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.le(parameter.getKey(), parameter.getValue());
		else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD LIKE %value%
	 */
	protected Criterion setParameterLikeAnywhere(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.like(parameter.getKey(), (String) parameter
					.getValue(), MatchMode.ANYWHERE);
		else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD LIKE value%
	 */
	protected Criterion setParameterLikeEnd(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.like(parameter.getKey(), (String) parameter
					.getValue(), MatchMode.START);
		else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD LIKE TO_UPPER(value)%
	 */
	protected Criterion setParameterLikeEndUpper(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.like(parameter.getKey(), ((String) parameter
					.getValue()).toUpperCase(), MatchMode.START);
		else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD between value0 and value1 FIELD >=
	 * value0 FIELD <= value1
	 */
	protected Criterion setParameterDaA(Parameter parameter0,
			Parameter parameter1) {
		if (parameter0.isValid() && parameter1.isValid()) {
            if(parameter1.getKey().equals(parameter0.getKey()))
            {
    			return Restrictions.between(parameter0.getKey(), parameter0
    					.getValue(), parameter1.getValue());
            }
            LogicalExpression restrictions = null;
            Criterion cr = Restrictions.ge(parameter0.getKey(), parameter0.getValue());
            Criterion cr1 = Restrictions.ge(parameter1.getKey(), parameter1.getValue());
            restrictions = Restrictions.and(cr,cr1);
            return restrictions;
		} else if (parameter0.isValid()) {
			return Restrictions.ge(parameter0.getKey(), parameter0.getValue());
		} else if (parameter1.isValid()) {
			return Restrictions.le(parameter1.getKey(), parameter1.getValue());
		} else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL: FIELD0 = FIELD1
	 */
	protected Criterion setParameterEqParameter(String colonna0, String colonna1) {
		return Restrictions.eqProperty(colonna0, colonna1);
	}

	protected Disjunction setDisjunctionEq(Parameter parameter) {
		Disjunction disjunction = null;

		if (parameter.isValid()) {
			disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.eq(parameter.getKey(),
					(parameter.getValue())));
			return disjunction;
		} else
			return null;
	}

	/*
	 * Crea la seguente condizione SQL prelevando i parametri da un HashMap:
	 * <opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale =
	 * XXXtp_materiale1</opzionale> <opzionale dipende="XXXtp_materiale2"> OR
	 * tp_materiale = XXXtp_materiale2 </opzionale> <opzionale
	 * dipende="XXXtp_materiale3"> OR tp_materiale = XXXtp_materiale3
	 * </opzionale> <opzionale dipende="XXXtp_materiale4"> OR tp_materiale =
	 * XXXtp_materiale4 </opzionale> <opzionale dipende="XXXtp_materiale5"> OR
	 * tp_materiale = XXXtp_materiale5 </opzionale> <opzionale
	 * dipende="XXXtp_materiale1"> ) </opzionale>
	 *
	 */
	// protected Criterion parserCollection(HashMap collection,String key,String
	// nameDB,int maxRow){
	// List<String> value = new ArrayList<String>();
	// for (int index = 1; index <= maxRow; index++) {
	// if (collection.containsKey(key+ String.valueOf(index))) {
	// value.add((String)collection.get(key + String.valueOf(index)));
	// }
	// }
	// return this.setParameterIn(nameDB,value);
	// }

	/*
	 * Crea la seguente condizione SQL: FIELD != Costante
	 */
	// protected Criterion setParameterNotEq(HashMap collection,String
	// key,String nameDB,String value){
	// if(collection.containsKey(key)){
	// collection.put(key,value);
	// return this.setParameterNotEq(collection,key,nameDB);
	// }
	// else
	// return null;
	// }

	// protected Criterion setParameterGe(HashMap collection,String key,String
	// nameDB,String value){
	// if(collection.containsKey(key)){
	// collection.put(key,value);
	// return Restrictions.ge(nameDB,collection.get(key));
	// }
	// else
	// return null;
	// }
	// protected Criterion setParameterLe(HashMap collection,String key,String
	// nameDB,String value){
	// if(collection.containsKey(key)){
	// collection.put(key,value);
	// return Restrictions.le(nameDB,collection.get(key));
	// }
	// else
	// return null;
	// }

	protected Criterion setParameterLikeAnywhereUpper(Parameter parameter) {
		// protected Criterion setParameterLikeAnywhereUpper(HashMap
		// collection,String key,String nameDB){
		if (parameter.isValid())
			return Restrictions.like(parameter.getKey(), ((String) parameter
					.getValue()).toUpperCase(), MatchMode.ANYWHERE);
		else
			return null;
	}

	protected Criterion setParameterEqUpper(Parameter parameter) {
		if (parameter.isValid())
			return Restrictions.eq(parameter.getKey(), ((String) parameter
					.getValue()).toUpperCase());
		else
			return null;

	}

	protected Disjunction setCollectionDisjunction(HashMap cdGenere,
			String key, List<String> colonne, int maxRow) {
		Disjunction disjunction = null;
		Disjunction disjunction1 = null;
		for (int index = 1; index <= maxRow; index++) {
			if (cdGenere.containsKey(key + String.valueOf(index))) {
				disjunction1 = Restrictions.disjunction();
				for (int count = 0; count < colonne.size(); count++) {
					disjunction1
							.add(Restrictions.eq(colonne.get(count),
									cdGenere.get(key
											+ String.valueOf(index))));
				}
				if (disjunction == null)
					disjunction = Restrictions.disjunction();
				disjunction.add(disjunction1);
			}
		}
		return disjunction;
	}

	protected Conjunction setCollectionConjunction(HashMap cdGenere,
			String key, List<String> colonne, int maxRow) {
		Conjunction disjunction = null;
		Conjunction disjunction1 = null;
		for (int index = 1; index <= maxRow; index++) {
			if (cdGenere.containsKey(key + String.valueOf(index))) {
				disjunction1 = Restrictions.conjunction();
				for (int count = 0; count < colonne.size(); count++) {
					disjunction1
							.add(Restrictions.eq(colonne.get(count),
									cdGenere.get(key
											+ String.valueOf(index))));
				}
				if (disjunction == null)
					disjunction = Restrictions.conjunction();
				disjunction.add(disjunction1);
			}
		}
		return disjunction;
	}

}
