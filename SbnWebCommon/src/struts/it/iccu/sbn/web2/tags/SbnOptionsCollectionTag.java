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
package it.iccu.sbn.web2.tags;

import org.apache.struts.taglib.html.OptionsCollectionTag;

import it.iccu.sbn.web2.util.LinkableTagUtils;

public class SbnOptionsCollectionTag extends OptionsCollectionTag {

	private static final long serialVersionUID = -7550067885896944927L;

	@Override
	protected void addOption(StringBuffer sb, String label, String value,
			boolean matched) {
		String newLabel = LinkableTagUtils.findMessage(pageContext, pageContext
				.getRequest().getLocale().toString(), label, null);
		super.addOption(sb, newLabel, value, matched);
	}

}
