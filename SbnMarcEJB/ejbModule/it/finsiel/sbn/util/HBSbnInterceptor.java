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
package it.finsiel.sbn.util;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

public class HBSbnInterceptor extends EmptyInterceptor {

	protected static Log log = LogFactory.getLog("sbnmarcPolo");

    private HashMap where;

	private static final long serialVersionUID = 4610936401502934631L;

    public void setWhere(HashMap where)
    {

    }

@Override
public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
	// TODO Auto-generated method stub
	log.debug("onLoad");
	return super.onLoad(entity, id, state, propertyNames, types);
}
@Override
public String onPrepareStatement(String sql) {
	log.debug("onPrepareStatement::"+sql);
	return super.onPrepareStatement(sql);
}
@Override
public void afterTransactionBegin(Transaction tx) {
	log.debug("afterTransactionBegin");
	super.afterTransactionBegin(tx);
}
}
