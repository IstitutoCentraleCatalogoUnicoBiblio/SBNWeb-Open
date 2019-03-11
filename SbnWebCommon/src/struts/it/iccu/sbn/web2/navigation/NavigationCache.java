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
package it.iccu.sbn.web2.navigation;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web2.navigation.NavigationForward.DirectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class NavigationCache extends SerializableVO {

	private static final long serialVersionUID = 62545943115995586L;
	private List<NavigationElement> cache;
	private int currentPosition;
	private DirectionType direction;
	private transient Navigation navigation;
	private int bookmarkPosition;
	private Lock lock;

	public NavigationCache(Navigation navigation) {
		this.navigation = navigation;
		this.cache = new ArrayList<NavigationElement>();
		this.currentPosition = -1;
		this.bookmarkPosition = -1;
		this.direction = DirectionType.DEFAULT;
		this.lock = new ReentrantLock();
	}

	protected NavigationElement addElement(ActionMapping mapping, ActionForm form) {
		try {
			lock.lock();
			this.currentPosition = cache.size();
			NavigationElement element = new NavigationElement(mapping, form, this.currentPosition);
			cache.add(element);
			return element;
		} finally {
			lock.unlock();
		}
	}

	protected void clearFromHere(int position) {
		try {
			lock.lock();
			for (int index = this.getLastPosition(); index > position; index--) {
				NavigationElement element = cache.get(index);
				navigation.removeBookmarks(element);
				navigation.removeAttributes(element);
				cache.remove(element);
			}
			this.currentPosition = position;
		} finally {
			lock.unlock();
		}
	}

	protected List<NavigationElement> getLockedElements() {
		lock.lock();
		return cache;
	}

	protected void unlockElements() {
		lock.unlock();
	}

	public int getCurrentPosition() {
		try {
			lock.lock();
			return currentPosition;
		} finally {
			lock.unlock();
		}
	}

	public DirectionType getDirection() {
		try {
			lock.lock();
			return direction;
		} finally {
			lock.unlock();
		}
	}

	public NavigationElement getElement(String uri, String actionForm) {
		try {
			lock.lock();
			NavigationElement element;
			for (int index = this.getLastPosition(); index >= 0; index--) {
				element = cache.get(index);
				if (element.getActionForm().equals(actionForm)
						&& element.getUri().equals(uri)) {
					this.currentPosition = index;
					return element;
				}
			}
			return null;
		} finally {
			lock.unlock();
		}
	}

	public NavigationElement getElementAt(int position) {
		try {
			lock.lock();
			if (position < 0 || position > this.getLastPosition())
				return null;

			NavigationElement element = cache.get(position);
			return element;
		} finally {
			lock.unlock();
		}
	}

	public NavigationElement getCurrentElement() {
		try {
			lock.lock();
			if (this.currentPosition < 0)
				return null;

			NavigationElement element = cache.get(this.currentPosition);
			return element;
		} finally {
			lock.unlock();
		}
	}

	public NavigationElement getPreviousElement() {
		try {
			lock.lock();
			if (this.currentPosition < 1)
				return null;

			int index = this.currentPosition - 1;
			while (index >= 0) {
				NavigationElement element = cache.get(index);
				if (!element.isPurge() )
					return element;
				index--;
			}

			return null;

		} finally {
			lock.unlock();
		}
	}

	public NavigationElement getElementByForm(ActionForm form) {
		try {
			lock.lock();
			NavigationElement element;
			for (int index = this.getLastPosition(); index >= 0; index--) {
				element = cache.get(index);
				if (element.getForm() == form) {
					this.currentPosition = index;
					return element;
				}
			}
			return null;
		} finally {
			lock.unlock();
		}
	}

	public NavigationElement getElementById(int id) {
		try {
			lock.lock();
			NavigationElement element;
			for (int index = this.getLastPosition(); index >= 0; index--) {
				element = cache.get(index);
				if (element.getUniqueId() == id) {
					this.currentPosition = index;
					return element;
				}
			}
			return null;
		} finally {
			lock.unlock();
		}
	}


	public NavigationElement getElementAtBookmark(String bookmark) {
		try {
			lock.lock();
			NavigationElement e = navigation.getBookmarks().get(bookmark);
			return e;
		} finally {
			lock.unlock();
		}
	}

	public int getLastPosition() {
		try {
			lock.lock();
			return cache.size() - 1;
		} finally {
			lock.unlock();
		}
	}

	protected void removeAll() {
		try {
			lock.lock();
			this.cache.clear();
			this.currentPosition = -1;
		} finally {
			lock.unlock();
		}
	}

	protected void setDirection(DirectionType direction) {
		try {
			lock.lock();
			this.direction = direction;
		} finally {
			lock.unlock();
		}
	}

	public boolean isEmpty() {
		try {
			lock.lock();
			return (cache.size() == 0);
		} finally {
			lock.unlock();
		}
	}

	public int getBookmarkPosition() {
		try {
			lock.lock();
			return bookmarkPosition;
		} finally {
			lock.unlock();
		}
	}

	protected void setBookmarkPosition(int bookmarkPosition) {
		try {
			lock.lock();
			this.bookmarkPosition = bookmarkPosition;
		} finally {
			lock.unlock();
		}
	}

	protected void shiftTo(int position) {

		try {
			lock.lock();

			if (position < 0 || position > this.getLastPosition())
				return;

			NavigationElement current = this.getCurrentElement();
			List<NavigationElement> newCache = new ArrayList<NavigationElement>();
			int newPos = -1;

			int size = cache.size();
			for (int index = 0; index < size; index++) {
				NavigationElement element = cache.get(index);
				if (element.getPosition() >= position) {
					newPos++;
					element.setPosition(newPos);
					newCache.add(element);
				} else {
					navigation.removeFormFromScope(element);
					navigation.removeBookmarks(element);
					navigation.removeAttributes(element);
				}
			}
			if (newPos > -1) {
				// l'elemento corrente ha probabilmente cambiato posizione
				this.currentPosition = current.getPosition();
				this.cache = newCache;
			}
		} finally {
			lock.unlock();
		}
	}

	protected void purge() {

		try {
			lock.lock();
			List<NavigationElement> newCache = new ArrayList<NavigationElement>();
			int newPos = -1;

			int oldSize = cache.size();
			for (int index = 0; index < oldSize; index++) {
				NavigationElement element = cache.get(index);
				if (element.isPurge()) {
					navigation.removeFormFromScope(element);
					navigation.removeBookmarks(element);
					navigation.removeAttributes(element);
					continue;
				}

				element.setPosition(++newPos);
				newCache.add(element);
			}
			this.cache = newCache;
			int size = newCache.size();
			if (this.currentPosition >= size)
				// l'elemento corrente ha probabilmente cambiato posizione
				this.currentPosition = size - 1;

		} finally {
			lock.unlock();
		}
	}

}
