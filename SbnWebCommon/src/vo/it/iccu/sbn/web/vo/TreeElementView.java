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
package it.iccu.sbn.web.vo;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.web.vo.tree.TreeElementTextDecoratorBaseImpl;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.RandomAccess;

public abstract class TreeElementView extends UniqueIdentifiableVO {

	private final class InternalTreeElementList<E extends TreeElementView> extends
			AbstractList<TreeElementView> implements List<TreeElementView>,
			RandomAccess, Cloneable, Serializable {

		private static final long serialVersionUID = 8879414362540665803L;
		private TreeElementView[] elements;
		private int size;

		public InternalTreeElementList() {
			super();
			this.elements = new TreeElementView[10];
		}

		public int size() {
			return size;
		}

		public boolean isEmpty() {
			return (size == 0);
		}

		public boolean contains(Object t) {
			return findNodeIndex((TreeElementView) t) >= 0;
		}

		public int findNodeIndex(TreeElementView t) {
			if (t != null) {
				for (int i = 0; i < size; i++)
					if (t.uniqueId == elements[i].uniqueId)
						return i;
			}
			return -1;
		}

		public TreeElementView get(int index) {
			RangeCheck(index);
			return elements[index];
		}

		public TreeElementView set(int index, TreeElementView element) {
			RangeCheck(index);

			TreeElementView oldValue = elements[index];
			elements[index] = element;
			return oldValue;
		}

		public boolean add(TreeElementView e) {
			elements = TreeElementView.checkListCapacity(elements, size + 1);
			modCount++;
			elements[size++] = e;
			return true;
		}

		public void add(int index, TreeElementView element) {
			if (index > size || index < 0)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

			elements = TreeElementView.checkListCapacity(elements, size + 1);
			modCount++;
			System.arraycopy(elements, index, elements, index + 1, size	- index);
			elements[index] = element;
			size++;
		}

		public TreeElementView remove(int index) {
			RangeCheck(index);

			modCount++;
			TreeElementView oldValue = elements[index];

			int numMoved = size - index - 1;
			if (numMoved > 0)
				System.arraycopy(elements, index + 1, elements, index, numMoved);
			elements[--size] = null;

			return oldValue;
		}

		public boolean remove(TreeElementView t) {
			if (t == null) {
				for (int index = 0; index < size; index++)
					if (elements[index] == null) {
						fastRemove(index);
						return true;
					}
			} else {
				for (int index = 0; index < size; index++)
					if (t.uniqueId == elements[index].uniqueId) {
						fastRemove(index);
						return true;
					}
			}
			return false;
		}

		private void fastRemove(int index) {
			modCount++;
			int numMoved = size - index - 1;
			if (numMoved > 0)
				System.arraycopy(elements, index + 1, elements, index, numMoved);
			elements[--size] = null;
		}

		public void clear() {
			modCount++;
			for (int i = 0; i < size; i++)
				elements[i] = null;

			size = 0;
		}

		public boolean addAll(Collection<? extends TreeElementView> c) {
			Object[] a = c.toArray();
			int numNew = a.length;
			elements = TreeElementView.checkListCapacity(elements, size + numNew);
			modCount++;
			System.arraycopy(a, 0, elements, size, numNew);
			size += numNew;
			return numNew != 0;
		}

		public boolean addAll(int index, Collection<? extends TreeElementView> c) {
			if (index > size || index < 0)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

			Object[] a = c.toArray();
			int numNew = a.length;
			elements = TreeElementView.checkListCapacity(elements, size + numNew);
			modCount++;

			int numMoved = size - index;
			if (numMoved > 0)
				System.arraycopy(elements, index, elements, index + numNew, numMoved);

			System.arraycopy(a, 0, elements, index, numNew);
			size += numNew;
			return numNew != 0;
		}

		protected void removeRange(int fromIndex, int toIndex) {
			modCount++;
			int numMoved = size - toIndex;
			System.arraycopy(elements, toIndex, elements, fromIndex, numMoved);

			int newSize = size - (toIndex - fromIndex);
			while (size != newSize)
				elements[--size] = null;
		}

		private void RangeCheck(int index) {
			if (index >= size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	};


	public interface TreeElementTextDecorator {
		public String getText(TreeElementView element);
	}


	private static final String SEPARATORE = "_";
	private static final long serialVersionUID = -2966727333376179129L;

	public static final String TREEVIEW_KEY_PARAM = "TREE_NODE_KEY";
	public static final String TREEVIEW_URL_PARAM = "TREE_URL_KEY";
	public static final String TREEVIEW_ID_PARAM = "TREE_ID";
	public static final String TREEVIEW_SUBMIT_PARAM = "TREE_SUBMIT";
	public static final String TREEVIEW_SUBMIT_NULL = "TREE_NULL";
	public static final String TREEVIEW_SUBMIT_SELECT = "SELECT_ALL";
	public static final String TREEVIEW_SUBMIT_DESELECT = "DESELECT_ALL";

	private static final Comparator<TreeElementView> ORDINAMENTO_LEVEL = new Comparator<TreeElementView>() {
		public int compare(TreeElementView o1, TreeElementView o2) {
			int level1 = o1.getNodeLevel();
			int level2 = o2.getNodeLevel();
			int check = level1 - level2;
			if (check == 0)
				check = o1.getKey().compareTo(o2.getKey());
			return check;
		}
	};

	public enum KeyDisplayMode {
		SHOW,
		SHOW_AS_LINK,
		SHOW_AS_TEXT,
		HIDE;
	}

	private String key = "";
	private String text;
	private String description;
	private String imageStyle;
	private String T005;
	private boolean flagCondiviso;
	private int localizzazione;

	private int rigaReticoloCtr;
	private int idNode = 0;

	private boolean opened;
	private boolean checkVisible;
	private boolean radioVisible;
	private boolean groupingCheck;

	private boolean plusVisible = false;
	private boolean plusDelete = false;

	private SbnAuthority tipoAuthority = null;
	private TreeElementView parent = null;
	private KeyDisplayMode displayKey = KeyDisplayMode.SHOW;

	protected List<TreeElementView> children = new InternalTreeElementList<TreeElementView>();
	private InternalTreeElementList<TreeElementView> castedChildren;

	private String tooltip;
	private boolean redItem;
	private TreeElementTextDecorator textDecorator;


	private static TreeElementView[] checkListCapacity(TreeElementView[] elements, int minCapacity) {
		int oldCapacity = elements.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 2);
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;

			// list = Arrays.copyOf(list, newCapacity); // solo java6!!!
			TreeElementView[] copy =
				(TreeElementView[]) java.lang.reflect.Array.newInstance(TreeElementView.class, newCapacity);
			System.arraycopy(elements, 0, copy, 0, oldCapacity);
			elements = copy;
		}
		return elements;
	}

	{
		//base decorator
		this.textDecorator = new TreeElementTextDecoratorBaseImpl();
	}

	public TreeElementView() {
		this.castedChildren = (InternalTreeElementList<TreeElementView>) children;
	}

	protected TreeElementView addChild(Class<? extends TreeElementView> childClass) {
		try {
			TreeElementView child = childClass.newInstance();
			this.children.add(child);
			child.setParent(this);
			return child;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public TreeElementView addChild() {
		return addChild(this.getClass());
	}

	public void removeChild(TreeElementView child) {
		if (child == null)
			return;
		for (TreeElementView node : castedChildren) {
			if (node.uniqueId == child.uniqueId) {
				child.parent = null;
				castedChildren.remove(child);
				break;
			}
		}
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isRedItem() {
		return redItem;
	}

	public void setRedItem(boolean redItem) {
		this.redItem = redItem;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TreeElementView getParent() {
		return parent;
	}

	public void setParent(TreeElementView parent) {

		this.parent = parent;
	}

	public List<TreeElementView> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public boolean isOpened() {
		return opened;
	}

	public int getIdNode() {
		return idNode;
	}

	public void setIdNode(int idNode) {
		this.idNode = idNode;
	}

	public String getImageStyle() {
		return imageStyle;
	}

	public void setImageStyle(String image) {
		this.imageStyle = image;
	}

	public boolean isCheckVisible() {
		return checkVisible;
	}

	public void setCheckVisible(boolean checkVisible) {
		this.checkVisible = checkVisible;
	}

	public boolean isRadioVisible() {
		return radioVisible;
	}

	public void setRadioVisible(boolean radioVisible) {
		this.radioVisible = radioVisible;
	}

	public int getRigaReticoloCtr() {
		return rigaReticoloCtr;
	}

	public void setRigaReticoloCtr(int rigaReticoloCtr) {
		this.rigaReticoloCtr = rigaReticoloCtr;
	}

	public void open() {
		TreeElementView element = this;
		while (element != null) {
			element.opened = true;
			element = element.getParent();
		}
	}

	public void close() {
		this.opened = false;
	}

	public void closeTree() {
		close();
		for (TreeElementView child : getChildren()) {
			child.closeTree();
		}
	}

	public TreeElementView findElement(String key) {

		if (isNull(key))
			return null;

		if (key.equals(this.key))
			return this;

		for (TreeElementView child : this.children) {
			TreeElementView e = child.findElement(key);
			if (e != null)
				return e;
		}
		return null;
	}

	public TreeElementView findElementByTooltip(String tooltip) {

		if (isNull(tooltip))
			return null;

		if (tooltip.equals(this.tooltip))
			return this;

		for (TreeElementView child : this.children) {
			TreeElementView e = child.findElementByTooltip(tooltip);
			if (e != null)
				return e;
		}
		return null;
	}

	public TreeElementView findElementUnique(int id) {

		if (id == this.getRepeatableId())
			return this;

		for (TreeElementView child : this.children) {
			TreeElementView e = child.findElementUnique(id);
			if (e != null)
				return e;
		}

		return null;
	}

	public TreeElementView findElementById(int id) {

		if (id == this.uniqueId)
			return this;

		for (TreeElementView child : this.children) {
			TreeElementView e = child.findElementById(id);
			if (e != null)
				return e;
		}

		return null;
	}

	public int findMaxElement() {
		int max = this.uniqueId;
		if (!isFilled(children))
			return max;

		for (TreeElementView child : this.children) {
			int maxChild = child.findMaxElement();
			if (maxChild > max)
				max = maxChild;
		}
		return max;
	}

	public List<TreeElementView> traverse() {

		List<TreeElementView> tmp = new ArrayList<TreeElementView>();
		if (!isFilled(children))
			return tmp;

		for (TreeElementView child : this.children) {
			tmp.add(child);
			tmp.addAll(child.traverse());
		}
		return tmp;
	}

	public Queue<TreeElementView> getNodesByLevel(boolean desc) {

		List<TreeElementView> nodi = this.traverse();
		nodi.add(this);

		Queue<TreeElementView> queue = null;
		if (!desc)
			queue = new PriorityQueue<TreeElementView>(nodi.size(), ORDINAMENTO_LEVEL);
		else
			queue = new PriorityQueue<TreeElementView>(nodi.size(),	ValidazioneDati.invertiComparatore(ORDINAMENTO_LEVEL) );

		queue.addAll(nodi);
		return queue;
	}

	public int getOpenedDepth() {

		int depth = this.getNodeLevel();
		if (!opened)
			return depth;

		if (!isFilled(children))
			return depth;

		for (TreeElementView child : this.children) {
			int depthChild = child.getOpenedDepth();
			if (depthChild > depth)
				depth = depthChild;
		}
		return depth;
	}

	public int getNodeLevel() {
		return (this.parent != null ? this.parent.getNodeLevel() + 1 : 0);
	}

	public TreeElementView getRoot() {
		return (this.parent != null ? this.parent.getRoot() : this);
	}

	public boolean isRoot() {
		return (this.getNodeLevel() == 0);
	}

	public boolean isFirstChild() {
		if (this.parent == null)
			return false;
		if (!this.parent.hasChildren())
			return false;

		return (this == this.parent.children.get(0));
	}

	public boolean isLastChild() {
		if (this.parent == null)
			return false;
		if (!this.parent.hasChildren())
			return false;

		List<TreeElementView> siblings = this.parent.children;
		return (this == ValidazioneDati.last(siblings) );
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public int getLocalizzazione() {
		return localizzazione;
	}

	public void setLocalizzazione(int localizzazione) {
		this.localizzazione = localizzazione;
	}

	public boolean isPlusVisible() {
		return plusVisible;
	}

	public void setPlusVisible(boolean forzaLentina) {
		this.plusVisible = forzaLentina;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public SbnAuthority getTipoAuthority() {
		return tipoAuthority;
	}

	public void setTipoAuthority(SbnAuthority tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}

	public final int getRepeatableId() {

		if (parent == null) // root
			return 48; //"0".hashCode();

		int levelInternal = parent.castedChildren.findNodeIndex(this);
		String internal = parent.getRepeatableId() + SEPARATORE	+ String.valueOf(levelInternal);

		return internal.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(64);
		buf.append(this.uniqueId);
		buf.append(SEPARATORE);
		buf.append(this.key);
		buf.append(SEPARATORE);
		buf.append(this.getNodeLevel());
		return buf.toString();
	}

	public KeyDisplayMode getKeyDisplayMode() {
		return displayKey;
	}

	public void setKeyDisplayMode(KeyDisplayMode displayKey) {
		this.displayKey = displayKey;
	}

	public void invert() {
		if (opened)
			this.close();
		else
			this.open();
	}

	public boolean isGroupingCheck() {
		return groupingCheck;
	}

	public void setGroupingCheck(boolean groupingCheck) {
		this.groupingCheck = groupingCheck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uniqueId;
		result = prime * result + ((T005 == null) ? 0 : T005.hashCode());
		result = prime * result + (checkVisible ? 1231 : 1237);
		result = prime * result	+ ((description == null) ? 0 : description.hashCode());
		result = prime * result	+ ((displayKey == null) ? 0 : displayKey.hashCode());
		result = prime * result + (flagCondiviso ? 1231 : 1237);
		result = prime * result + (groupingCheck ? 1231 : 1237);
		result = prime * result + idNode;
		result = prime * result	+ ((imageStyle == null) ? 0 : imageStyle.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + localizzazione;
		result = prime * result + (opened ? 1231 : 1237);
		result = prime * result + (plusVisible ? 1231 : 1237);
		result = prime * result + (radioVisible ? 1231 : 1237);
		result = prime * result + rigaReticoloCtr;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TreeElementView other = (TreeElementView) obj;
		if (uniqueId != other.uniqueId)
			return false;

		return true;
	}

	public boolean isPlusDelete() {
		return plusDelete;
	}

	public void setPlusDelete(boolean plusDelete) {
		this.plusDelete = plusDelete;
	}

	public String getDecoratedKey() {
		return key;
	}

	public String getDecoratedText() {
		if (textDecorator != null)
			return textDecorator.getText(this);
		return text;
	}

	public TreeElementTextDecorator getTextDecorator() {
		return textDecorator;
	}

	public void setTextDecorator(TreeElementTextDecorator decorator) {
		this.textDecorator = decorator;
	}

}
