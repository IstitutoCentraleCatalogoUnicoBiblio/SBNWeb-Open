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
package it.iccu.sbn.ejb.model.tree;

import it.iccu.sbn.ejb.model.attivita.MenuAttivitaChecker;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserWrapper;

import java.util.ArrayList;
import java.util.List;

public class TreeElement extends UniqueIdentifiableVO {


	private static final long serialVersionUID = 6010809368169670153L;
	private String key;
    private String text;
    private String description;
    private String url;
    private TreeElement parent;
    private boolean selected;
    private List<TreeElement> children = new ArrayList<TreeElement>();
	private MenuAttivitaChecker checker;

    public TreeElement() {
    	super();
    }

    public TreeElement(String key) {
        setKey(key);
    }

    public TreeElement(String key, TreeElement parent) {
        this(key);
        setParent(parent);
    }

    public TreeElement(String key, TreeElement parent, MenuAttivitaChecker checker) {
        this(key);
		this.checker = checker;
        setParent(parent);
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TreeElement getParent() {
        return parent;
    }

    public List<TreeElement> getChildren() {
        return children;
    }

    public String toString() {
        return getKey() + " - " + getText();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setChildren(List<TreeElement> children) {
        this.children = children;
    }

    public void setKey(String key) {
        if (key != null)
            this.key = key;
    }

    public void setParent(TreeElement parent) {
        if (parent != null) {
            parent.children.add(this);
            setKey(parent.getKey() + '.' + key);
        }
        if (this.parent != null) {
            int i = 0;
            for (TreeElement element : this.parent.children) {
                if (element == this) {
                    this.parent.children.remove(i);
                }
                i++;
            }
        }
        this.parent = parent;
    }
    public void settaParent(TreeElement parent) {
        if (parent != null) {
            parent.children.add(this);
            setKey(key);
        }
    }

	public MenuAttivitaChecker getChecker() {
		return checker;
	}

	public boolean check(UserWrapper uw) {
		if (checker != null)
			return checker.check(uw);
		return true;
	}

}
