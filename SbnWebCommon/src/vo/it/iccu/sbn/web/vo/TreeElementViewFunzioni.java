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

import java.util.List;

public class TreeElementViewFunzioni extends TreeElementView {

	private static final long serialVersionUID = -2906927351673199029L;
	private String key;
    private String text;
    private String description;
    private String url;
    private TreeElementViewFunzioni parent;
    private boolean selected;

    public TreeElementViewFunzioni() {
    }

    public TreeElementViewFunzioni(String key) {
        setKey(key);
    }

    public TreeElementViewFunzioni(String key, TreeElementViewFunzioni parent) {
        this(key);
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

    public TreeElementViewFunzioni getParent() {
        return parent;
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

    public void setChildren(List<TreeElementView> children) {
        this.children = children;
    }

    public void setKey(String key) {
        if (key != null)
            this.key = key;
    }

    public void setParent(TreeElementViewFunzioni parent) {
        if (parent != null) {
            parent.children.add(this);
            setKey(parent.getKey() + '.' + key);
        }
        if (this.parent != null) {
            int i = 0;
            for (TreeElementView element : this.parent.children) {
                if (element == this) {
                    this.parent.children.remove(i);
                }
                i++;
            }
        }
        this.parent = parent;
    }
    public void settaParent(TreeElementViewFunzioni parent) {
        if (parent != null) {
            parent.children.add(this);
            setKey(key);
        }
    }
}
