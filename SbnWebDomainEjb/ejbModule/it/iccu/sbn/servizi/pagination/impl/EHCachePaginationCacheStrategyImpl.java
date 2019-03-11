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
package it.iccu.sbn.servizi.pagination.impl;

import it.iccu.sbn.ejb.services.pagination.QueryPaginationExecutorBean;
import it.iccu.sbn.servizi.pagination.PaginationCacheStrategy;

import java.io.InputStream;
import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EHCachePaginationCacheStrategyImpl implements PaginationCacheStrategy {

	private static CacheManager cacheMgr;

	private final Cache getCache() {
		return cacheMgr.getCache("pagination");
	}

	public boolean init() throws Exception {
		InputStream is = QueryPaginationExecutorBean.class.getResourceAsStream("/META-INF/pagination-ehcache.xml");
		cacheMgr = CacheManager.create(is);
		cacheMgr.getCache("pagination");

		return true;
	}

	public Serializable get(String key) throws Exception {
		Element e = getCache().get(key);
		return (e != null ? e.getValue() : null);
	}

	public Serializable remove(String key) throws Exception {
		return getCache().remove(key);
	}

	public boolean put(String key, Serializable value) throws Exception {
		Element e = new Element(key, value);
		getCache().put(e);
		return true;
	}

}
