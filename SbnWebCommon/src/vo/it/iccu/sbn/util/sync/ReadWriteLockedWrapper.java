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
package it.iccu.sbn.util.sync;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ReadWriteLockedWrapper implements MethodInterceptor {

	@SuppressWarnings("unchecked")
	public static final <T> T wrap(Class<T> clazz) {
		return (T) Enhancer.create(clazz, new ReadWriteLockedWrapper() );
	}

	private enum MethodType {
		SETTER,
		GETTER,
		OTHER;
	}

	private ReadWriteLockedWrapper() {
		ReadWriteLock _lock = new ReentrantReadWriteLock();
		r = _lock.readLock();
		w = _lock.writeLock();
	}

	private final Lock r;
	private final Lock w;

	public Object intercept(Object instance, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {

		String name = method.getName().toLowerCase();
		MethodType type = MethodType.OTHER;

		if (name.startsWith("set"))
			type = MethodType.SETTER;
		else
			if (name.startsWith("get") ||
				name.startsWith("is") ||
				name.startsWith("has"))
			type = MethodType.GETTER;

		switch (type) {
		case GETTER:
			r.lock();
			break;
		case SETTER:
		case OTHER:
		default:
			w.lock();
		}

		try {
			try {
				return proxy.invokeSuper(instance, args);
			} catch (Exception e) {
				return null;
			}

		} finally {
			switch (type) {
			case GETTER:
				r.unlock();
				break;
			case SETTER:
			case OTHER:
			default:
				w.unlock();
			}
		}
	}

}
