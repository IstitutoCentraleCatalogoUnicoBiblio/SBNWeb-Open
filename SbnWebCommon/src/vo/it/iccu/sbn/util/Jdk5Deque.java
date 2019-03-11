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
package it.iccu.sbn.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class provides a minimalistic deque capabilities (LIFO, FIFO) backed by a
 * simple linked list.
 *
 * It is supposed to be used in OrderStoreImpl, but can be used whenever the storage
 * providing the stack and queue capabalities at the same time is needed.
 *
 * Introduced as a temporary solution for JDK5 - if JDK5 support is dropped, the class
 * can be replaced by java.util.Deque and its implementation (for example java.util.ArrayDeque).
 *
 * @param <E> type of elements stored in deque
 */
public class Jdk5Deque<E> {

    private final ReentrantLock lock = new ReentrantLock();
    private List<E> backingStore;

    public Jdk5Deque() {
        this.backingStore = new LinkedList<E>();
    }

    public Jdk5Deque(Collection<? extends E> c) {
        this.backingStore = new LinkedList<E>(c);
    }

    /**
     * Returns an iterator over the elements in this deque in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this deque in proper sequence
     */
    public Iterator<E> iterator() {
        lock.lock();
        try {
            return backingStore.iterator();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param e the element to add
     */
    public void addFirst(E e) {
        lock.lock();
        try {
            backingStore.add(0, e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Inserts the specified element at the end of this deque.
     *
     * <p>This method is equivalent to {@link #add}.
     *
     * @param e the element to add
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null and this
     *         deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    public void addLast(E e) {
        lock.lock();
        try {
            backingStore.add(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Inserts the specified element at the end of this deque.
     *
     * <p>This method is equivalent to {@link #addLast}.
     *
     * @param e the element to add
     */
    public void add(E e) {
        addLast(e);
    }

    /**
     * Retrieves and removes the first element of this deque.  This method
     * throws an exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public E removeFirst() {
        lock.lock();
        try {
            if (backingStore.size() == 0) {
                throw new NoSuchElementException();
            } else {
                return backingStore.remove(0);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves and removes the last element of this deque.  This method
     * throws an exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public E removeLast() {
        lock.lock();
        try {
            if (backingStore.size() == 0) {
                throw new NoSuchElementException();
            } else {
                return backingStore.remove(backingStore.size() - 1);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the first element of this deque.  This method throws an
     * exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public E getFirst() {
        lock.lock();
        try {
            if (backingStore.size() == 0) {
                throw new NoSuchElementException();
            } else {
                return backingStore.get(0);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the last element of this deque.  This method throws an
     * exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public E getLast() {
        lock.lock();
        try {
            if (backingStore.size() == 0) {
                throw new NoSuchElementException();
            } else {
                return backingStore.get(backingStore.size() - 1);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the size (number of elemtents stored) of this deque.
     *
     * @return the size of this deque
     */
    public int size() {
        lock.lock();
        try {
            return backingStore.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if this deque is empty (stores no element).
     *
     * @return true if this deque is empty
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return backingStore.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns true if this deque contains the specified element. More formally,
     * returns true if and only if this deque contains at least one element e
     * such that (o==null ? e==null : o.equals(e)).
     *
     * @param o element whose presence in this list is to be teste
     * @return true if this deque contains specified element
     */
    public boolean contains(Object o) {
        lock.lock();
        try {
            return backingStore.contains(o);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes all elements from this deque.
     *
     */
    public void clear() {
        lock.lock();
        try {
            backingStore.clear();
        } finally {
            lock.unlock();
        }
    }

    public E poll() {
    	return removeFirst();
    }
}
