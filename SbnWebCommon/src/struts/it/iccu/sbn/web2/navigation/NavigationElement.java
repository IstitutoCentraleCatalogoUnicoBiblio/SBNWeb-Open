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

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class NavigationElement extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 6495493061483125351L;

	private transient Action actionInstance;

	private String uri;
	private String name;
	private String descrizione;
	private String testo;

	private int position = -1;
	private String anchor = "";
	private NavigationBlocchiInfo infoBlocchi;

	private ActionForm form;
	private ActionMapping mapping;
	private String button;
	private String suffix;

	private boolean last = false;
	private boolean href = false;
	private boolean purge = false;
	private boolean back = false;

	private Set<String> linkButtonProperties;

	private long performStart;

	private final String textKey;

	public String getActionForm() {
		return name;
	}

	public NavigationElement(ActionMapping mapping, ActionForm form, int position) {
		super();
		this.uri = mapping.getPath();
		this.name = mapping.getAttribute();
		this.position = position;
		this.form = form;
		this.mapping = mapping;
		this.button = null;
		this.last = false;
		this.href = false;
		this.infoBlocchi = null;
		this.suffix = null;
		this.linkButtonProperties = new HashSet<String>();

		this.testo = this.uri.replace('/', '.') + ".testo";
		this.descrizione = this.uri.replace('/', '.') + ".descrizione";

		this.textKey = this.testo;
	}

	public void setActionForm(String actionForm) {
		this.name = actionForm;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public ActionForm getForm() {
		return form;
	}

	public void setForm(ActionForm form) {
		this.form = form;
	}

	public boolean isHref() {
		return href;
	}

	public void setHref(boolean href) {
		this.href = href;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTesto() {
		return testo;
	}

	public String getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}

	public String getScope() {
		return this.mapping.getScope();
	}

	public ActionMapping getMapping() {
		return mapping;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public void setButton(String key) {
		this.button = key;
	}

	public String getButton() {
		return button;
	}

	public NavigationBlocchiInfo getInfoBlocchi() {
		return this.infoBlocchi;
	}

	public void setInfoBlocchi(NavigationBlocchiInfo currBlocco) {
		this.infoBlocchi = currBlocco;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + uniqueId;
		result = PRIME * result + ((button == null) ? 0 : button.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((form == null) ? 0 : form.hashCode());
		result = PRIME * result + (href ? 1231 : 1237);
		result = PRIME * result + (last ? 1231 : 1237);
		result = PRIME * result + ((mapping == null) ? 0 : mapping.hashCode());
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + position;
		result = PRIME * result + ((testo == null) ? 0 : testo.hashCode());
		result = PRIME * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	public void setAnchorId(String string) {
		this.anchor = string;
	}

	public String getAnchorId() {
		return anchor;
	}

	public Set<String> getLinkButtonProperties() {
		return this.linkButtonProperties;
	}

	public void setSuffissoTesto(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffissoTesto() {
		return this.suffix;
	}

	public void setTesto(String testo) {
		if (testo == null)
			this.testo = this.uri.replace('/', '.') + ".testo";
		else
			this.testo = testo;
	}

	public void setDescrizione(String descr) {
		if (descr == null)
			this.descrizione = this.uri.replace('/', '.') + ".descrizione";
		else
			this.descrizione = descr;
	}

	public boolean isPurge() {
		return purge;
	}

	public void setPurge(boolean purge) {
		this.purge = purge;
	}

	public Action getActionInstance() {
		return actionInstance;
	}

	public void setActionInstance(Action actionInstance) {
		this.actionInstance = actionInstance;
	}

	public boolean isBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}

	public String toString() {
		return "[id: " + uniqueId + " pos: " + position + " name: " + name + "]";
	}

	public void setPerformStart(long time) {
		performStart = time;
	}

	public long getPerformStart() {
		return performStart;
	}

	public String getTextKey() {
		return textKey;
	}
}
