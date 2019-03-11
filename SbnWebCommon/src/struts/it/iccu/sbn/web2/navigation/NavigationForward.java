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

import it.iccu.sbn.util.cloning.ClonePool;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.config.ForwardConfig;

public class NavigationForward extends ActionRedirect {

	public enum DirectionType {
		DEFAULT, FORWARD, BACK, BOOKMARK;
	}

	private static final long serialVersionUID = -475542242379569094L;
	private DirectionType direction = DirectionType.DEFAULT;
	private int bookmarkPosition = -1;
	private boolean fromBar = false;
	private ActionForm forcedInstance = null;

	public int getBookmarkPosition() {
		return bookmarkPosition;
	}

	public void setBookmarkPosition(int exactPosition) {
		this.bookmarkPosition = exactPosition;
	}

	public NavigationForward(ForwardConfig config, DirectionType direction,
			boolean fromBar) {

		ClonePool.copyCommonProperties(this, config);

		setRedirect(false);
		this.direction = direction;
		this.fromBar = fromBar;
	}

	public NavigationForward(ForwardConfig config, boolean redirect) {

		ClonePool.copyCommonProperties(this, config);
		setRedirect(redirect);
	}

	public NavigationForward(DirectionType direction) {
		this(direction, false);
	}

	public NavigationForward(ForwardConfig config, DirectionType direction) {
		this(config, direction, false);
	}

	public NavigationForward(DirectionType direction, boolean fromBar) {
		super();
		this.direction = direction;
		this.fromBar = fromBar;
		this.setRedirect(false);
	}

	public DirectionType getDirection() {
		return direction;
	}

	public void setDirection(DirectionType direction) {
		this.direction = direction;
	}

	public boolean isFromBar() {
		return fromBar;
	}

	protected ActionForm getFormInstance() {
		return forcedInstance;
	}

	protected NavigationForward forceFormInstance(ActionForm instance) {
		this.forcedInstance = instance;
		return this;
	}

}
