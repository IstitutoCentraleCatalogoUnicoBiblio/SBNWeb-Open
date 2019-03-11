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
package it.iccu.sbn.polo;


import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.Type;

public class ContainsSQLFunction extends StandardSQLFunction {

    private static final String POSTGRES_VERSION_83 = "8.3.0";

	public ContainsSQLFunction(String name) {
    	super(name);
	}

	public ContainsSQLFunction(String name, Type type) {
    	super(name, type);
	}

	public Type getReturnType(Type type, Mapping mapping) throws QueryException {

		return Hibernate.BOOLEAN;
	}

	public boolean hasArguments() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public String render(List args, SessionFactoryImplementor factory) throws QueryException {

		String[] parole = ((String)args.get(1)).split("\\s+");
		String tmp = parole[0];
		for (int i = 1; i < parole.length; i++)
			tmp = tmp + " & " + parole[i];

		StringBuffer buf = new StringBuffer();
		buf.append("(");
		buf.append("args.get(0) ");

		String version = getVersion(factory);
		if (version.compareTo(POSTGRES_VERSION_83) < 0) { // config TSearch2 (solo se ver. < 8.3)
			factory.getCurrentSession().getNamedQuery("set_curcfg").uniqueResult();
			buf.append("@@ to_tsquery('default', '");
		} else
			buf.append("@@ to_tsquery('");

		buf.append(tmp);
		buf.append("') )");

		return buf.toString();
	}

	private String getVersion(SessionFactoryImplementor factory) {
		try {
			Session session = factory.getCurrentSession();
			String version = (String) session.createSQLQuery("select version()").uniqueResult();
			String[] tokens = version.split("\\s");
			return tokens[1];

		} catch (Exception ex) {
			return POSTGRES_VERSION_83;
		}
	}
}
