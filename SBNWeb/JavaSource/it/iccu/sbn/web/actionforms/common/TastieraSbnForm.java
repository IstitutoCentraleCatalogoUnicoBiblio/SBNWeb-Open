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
package it.iccu.sbn.web.actionforms.common;

import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;


public class TastieraSbnForm extends ActionForm {


	private static final long serialVersionUID = -7757560470123342552L;
	private List<?> currentKeyList;
	private List<?> keys;
	private Map<?, ?> categories;
	private String inputField;
	private boolean init;
	private String folder;
	private int limit;

	private String targetName;
	private String targetProperty;

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
	}

	public Map<?, ?> getCategories() {
		return categories;
	}

	public void setCategories(Map<?, ?> categories) {
		this.categories = categories;
	}

	public List<?> getKeys() {
		return keys;
	}

	public void setKeys(List<?> keys) {
		this.keys = keys;
	}

	public boolean isInitialized() {
		return this.init;
	}

	public void setInitialized(boolean b) {
		this.init = b;
	}


	public String getInputField() {
		return inputField;
	}


	public void setInputField(String inputField) {
		this.inputField = inputField;
	}

	public List<?> getCurrentKeyList() {
		return currentKeyList;
	}

	public void setCurrentKeyList(List<?> currentKeyList) {
		this.currentKeyList = currentKeyList;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFolder() {
		return folder;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

}
