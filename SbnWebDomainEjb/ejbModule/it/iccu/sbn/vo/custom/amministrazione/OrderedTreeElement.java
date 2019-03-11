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
package it.iccu.sbn.vo.custom.amministrazione;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderedTreeElement {
	private Object key;
	private Object value;
	private Boolean checked;
	private OrderedTreeElement child;
	private OrderedTreeElement parent;
	private List<OrderedTreeElement> elements = new ArrayList<OrderedTreeElement>();
	private String type; // Default
	private boolean parametered;
	private String tooltip = "";
	private String sortValue;

	public OrderedTreeElement() {
		this.key = null;
		this.value = null;
		this.checked = false;
		this.child = null;
		this.parent = null;
		this.type = "CHECK";
	}

	public OrderedTreeElement(Object key, Object value) {
		this.key = key;
		this.value = value;
		this.checked = false;
		this.child = null;
		this.parent = null;
		this.type = "CHECK";
	}

	public OrderedTreeElement(Object key, Object value, String type) {
		this.key = key;
		this.value = value;
		this.checked = false;
		this.child = null;
		this.parent = null;
		this.type = type;
	}

	public OrderedTreeElement(Object key, Object value, String type,
			String tooltip) {
		this.key = key;
		this.value = value;
		this.checked = false;
		this.child = null;
		this.parent = null;
		this.type = type;
		this.tooltip = tooltip;
	}

	public OrderedTreeElement(Object key, Object value, String type,
			String tooltip, String sortValue) {
		this.key = key;
		this.value = value;
		this.checked = false;
		this.child = null;
		this.parent = null;
		this.type = type;
		this.tooltip = tooltip;
		this.sortValue = sortValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public OrderedTreeElement getChild() {
		return child;
	}

	public OrderedTreeElement getParent() {
		return parent;
	}

	public List<OrderedTreeElement> getElements() {
		return elements;
	}

	public void addElement(Object key, Object value) {
		OrderedTreeElement ote = new OrderedTreeElement(key, value);
		ote.parent = this;
		this.elements.add(ote);
		sort();
	}

	public void addElement(Object key, Object value, String type, String tooltip) {
		OrderedTreeElement ote = new OrderedTreeElement(key, value, type,
				tooltip);
		ote.parent = this;
		this.elements.add(ote);
		sort();
	}

	public void addElementNoSort(Object key, Object value, String type,
			String tooltip) {
		OrderedTreeElement ote = new OrderedTreeElement(key, value, type,
				tooltip);
		ote.parent = this;
		this.elements.add(ote);
	}

	public void addElementNoSort(Object key, Object value, String type,
			String tooltip, String sortValue) {
		OrderedTreeElement ote = new OrderedTreeElement(key, value, type,
				tooltip, sortValue);
		ote.parent = this;
		this.elements.add(ote);
	}

	public void addElementNoSort(Object key, Object value) {
		OrderedTreeElement ote = new OrderedTreeElement(key, value);
		ote.parent = this;
		this.elements.add(ote);
	}

	public void addElementNoSortWithType(Object key, Object value, String type) {
		OrderedTreeElement ote = new OrderedTreeElement(key, value, type);
		ote.parent = this;
		this.elements.add(ote);
	}

	public boolean addChild(OrderedTreeElement element, String key) {
		// Find element in elements list to whom we add a new elements list
		for (int i = 0; i < elements.size(); i++) {
			OrderedTreeElement ote = elements.get(i);
			String childKey = (String) ote.getKey();

			if ((childKey).compareTo(key) == 0) {
				ote.child = element;
				element.parent = this;
				return true;
			}
		}
		return false;
	}

	public void sort() {
		Collections.sort(this.elements, new StringComparator());
	}

	public void sortByValue() {
		Collections.sort(this.elements, new StringComparatorByValue());
	}

	public void sortBySortValue() {
		Collections.sort(this.elements, new StringComparatorBySortValue());
	}

	public OrderedTreeElement find(OrderedTreeElement ote, String Key) {
		// Find element in elements list to whom we add a new elements list
		for (int i = 0; i < ote.elements.size(); i++) {
			OrderedTreeElement elm = ote.elements.get(i);
			String K = (String) elm.getKey();
			if (K.compareTo(Key) == 0) {
				return elm;
			}
			OrderedTreeElement child = elm.getChild();
			if (child != null) {
				OrderedTreeElement ote1 = find(child, Key);
				if (ote1 != null)
					return ote1;
			}
		}
		return null;
	}

	public OrderedTreeElement findByValue(OrderedTreeElement ote, String Value) {
		// Find element in elements list to whom we add a new elements list
		for (int i = 0; i < ote.elements.size(); i++) {
			OrderedTreeElement elm = ote.elements.get(i);
			String V = (String) elm.getValue();
			if (V.compareTo(Value) == 0) {
				return elm;
			}
			OrderedTreeElement child = elm.getChild();
			if (child != null) {
				OrderedTreeElement ote1 = findByValue(child, Value);
				if (ote1 != null)
					return ote1;
			}
		}
		return null;
	}

	public static void dumpOrderedTreeElement(OrderedTreeElement ote, int level) {
		int i;
		for (i = 0; i < ote.getElements().size(); i++) {
			OrderedTreeElement elm = ote.getElements().get(i);
			for (int j = 0; j < level; j++)
				System.out.print("    ");

			System.out.println("Key=" + elm.getKey() + " Value="
					+ elm.getValue());
			// se ci sono figli stampa i figli
			OrderedTreeElement child = elm.getChild();
			if (child != null)
				dumpOrderedTreeElement(child, level + 1);
		}
	}

	public static void clearChecked(OrderedTreeElement ote) {
		int i;
		for (i = 0; i < ote.getElements().size(); i++) {
			OrderedTreeElement elm = ote.getElements().get(i);
			// if (elm.getChecked() == true)
			elm.setChecked(false);

			// se ci sono figli cicla sui figli
			OrderedTreeElement child = elm.getChild();
			if (child != null)
				clearChecked(child);
		}
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public boolean isParametered() {
		return parametered;
	}

	public void setParametered(boolean parametered) {
		this.parametered = parametered;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getSortValue() {
		return sortValue;
	}

	public void setSortValue(String sortValue) {
		this.sortValue = sortValue;
	}
}

class StringComparator implements Comparator<OrderedTreeElement> {

	public int compare(OrderedTreeElement a, OrderedTreeElement b) {

		OrderedTreeElement ote = a;
		String sa = (String) ote.getKey();
		ote = b;
		String sb = (String) ote.getKey();
		return ((sa).compareToIgnoreCase(sb)); // Ascending

	} // end compare
} // end class StringComparator

class StringComparatorByValue implements Comparator<OrderedTreeElement> {

	public final int compare(OrderedTreeElement a, OrderedTreeElement b) {

		OrderedTreeElement ote = a;
		String sa = (String) ote.getValue();
		ote = b;
		String sb = (String) ote.getValue();
		return ((sa).compareToIgnoreCase(sb)); // Ascending

	} // end compare
} // end class StringComparator

class StringComparatorBySortValue implements Comparator<OrderedTreeElement> {
	public final int compare(OrderedTreeElement a, OrderedTreeElement b) {

		OrderedTreeElement ote = a;
		String sa = ote.getSortValue();
		ote = b;
		String sb = ote.getSortValue();
		return ((sa).compareToIgnoreCase(sb)); // Ascending

	} // end compare
} // end class StringComparator

/*
 * class OrderedTreeApp { public static void main(String[] args) throws
 * IOException { int intValue; String value;
 *
 *
 * System.out.print("Hello OrderedTreeApp\n\n");
 *
 * OrderedTreeElement ote = new OrderedTreeElement(new String("root"), new
 * String("")); ote.addElement(new String("k5"), new String("v5"));
 * ote.addElement(new String("k4"), new String("v4")); ote.addElement(new
 * String("k3"), new String("v3")); ote.addElement(new String("k2"), new
 * String("v2")); ote.addElement(new String("k1"), new String("v1"));
 *
 * OrderedTreeElement ote2 = new OrderedTreeElement(new String("nodo1.n"), new
 * String("")); ote2.addElement(new String("k4.5"), new String("v4.5"));
 * ote2.addElement(new String("k4.4"), new String("v4.4")); ote2.addElement(new
 * String("k4.3"), new String("v4.3")); ote2.addElement(new String("k4.2"), new
 * String("v4.2")); ote2.addElement(new String("k4.1"), new String("v4.1"));
 * ote.addChild(ote2, "k4");
 *
 * OrderedTreeElement ote3 = new OrderedTreeElement(new String("nodo3.n"), new
 * String("")); ote3.addElement(new String("k4.3.5"), new String("v4.3.5"));
 * ote3.addElement(new String("k4.3.4"), new String("v4.3.4"));
 * ote3.addElement(new String("k4.3.3"), new String("v4.3.3"));
 * ote3.addElement(new String("k4.3.2"), new String("v4.3.2"));
 * ote3.addElement(new String("k4.3.1"), new String("v4.3.2"));
 * ote2.addChild(ote3, "k4.3");
 *
 *
 * OrderedTreeElement ote4 = new OrderedTreeElement(new String("nodo4.n"), new
 * String("")); ote4.addElementNoSort(new String("k4.3.5.5"), new
 * String("v4.3.5.5")); ote4.addElementNoSort(new String("k4.3.4.4"), new
 * String("v4.3.4.4")); ote4.addElementNoSort(new String("k4.3.3.3"), new
 * String("v4.3.3.3")); ote4.addElementNoSort(new String("k4.3.2.2"), new
 * String("v4.3.2.2")); ote4.addElementNoSort(new String("k4.3.1.1"), new
 * String("v4.3.2.1")); ote3.addChild(ote4, "k4.3.2");
 *
 *
 * OrderedTreeElement ote1 = new OrderedTreeElement(new String("nodo1.1"), new
 * String("")); ote1.addElement(new String("k5.5"), new String("v5.5"));
 * ote1.addElement(new String("k5.4"), new String("v5.4")); ote1.addElement(new
 * String("k5.3"), new String("v5.3")); ote1.addElement(new String("k5.2"), new
 * String("v5.2")); ote1.addElement(new String("k5.1"), new String("v5.1"));
 * ote.addChild(ote1, "k5");
 *
 *
 *
 * dumpOrderedTreeElement(ote, 0); OrderedTreeElement oteFind = ote.find(ote,
 * "k4.3.3.3");
 *
 * System.out.println("\n\nFound " + oteFind.getKey() + ":" +
 * oteFind.getValue());
 *
 * System.out.print("Good Bye OrderedTreeApp\n\n"); } // end main()
 * //------------------------------------------------------------- public static
 * String getString() throws IOException { InputStreamReader isr = new
 * InputStreamReader(System.in); BufferedReader br = new BufferedReader(isr);
 * String s = br.readLine(); return s; }
 * //------------------------------------------------------------- public static
 * char getChar() throws IOException { String s = getString(); return
 * s.charAt(0); }
 * //------------------------------------------------------------- public static
 * int getInt() throws IOException { String s = getString(); return
 * Integer.parseInt(s); }
 * //-------------------------------------------------------------
 *
 *
 * } // end class OrderedTreeApp
 *
 * *
 */
