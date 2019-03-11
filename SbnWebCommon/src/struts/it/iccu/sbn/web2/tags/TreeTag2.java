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

import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.util.ResponseUtils;

public class TreeTag2 extends BaseHandlerTag {

	private static final long serialVersionUID = -2425476138592449098L;
	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	private static Logger log = Logger.getLogger(TreeTag2.class);

	private String root;
	private String propertyCheck;
	private String divClass;
	private String propertyRadio;
	private String imagesPath;
	private boolean enableNodeSubmit;
	private boolean enableSelectAll;
	private boolean visualCheck = true;
	private boolean enableSubmit = false;
	private String linkStyleId = "sintButtonLinkDefault";
	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String name) {
		this.root = name;
	}

	public String getDivClass() {
		return divClass;
	}

	public void setDivClass(String divClass) {
		this.divClass = divClass;
	}

	public String getPropertyCheck() {
		return propertyCheck;
	}

	public void setPropertyCheck(String propertyCheck) {
		this.propertyCheck = propertyCheck;
	}

	public String getPropertyRadio() {
		return propertyRadio;
	}

	public void setPropertyRadio(String propertyRadio) {
		this.propertyRadio = propertyRadio;
	}

	private static boolean isNull(String value) {
		return (value == null || "".equals(value.trim() ));
	}

	private void printTreeElement(TreeElementView element, String actionPath,
			JspWriter out, HttpServletResponse response, String formName, int elementId) throws IOException, JspException {

		if (element != null
				&& (element.getParent() == null
						|| element.getParent().isOpened())) {

			StringBuffer tid = new StringBuffer(Constants.TREE_ELEMENT);
			tid.append('_').append(element.getUniqueId());

			out.print("<tr>\n");
			out.print("<td>");
			out.println("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
			out.print("<tr>\n");

			int nodeLevel = element.getNodeLevel();
			for (int td = 0; td < nodeLevel; td++)
				out.print("<td width=\"16px\">&nbsp;</td>\n");

			out.print("<td width=\"16px\" valign=\"top\">\n");

			int uniqueId = element.getRepeatableId();
			if (element.hasChildren() && element.isPlusDelete()) {

				out.print("&nbsp;");

			}
			else if (element.hasChildren() || element.isPlusVisible()) {

				String anchor = LinkableTagUtils.ANCHOR_PREFIX + uniqueId;
				out.print("<a name=\"" + anchor + "\"></a>");

				if (enabled) {
					if (!enableNodeSubmit) {
						out.print("<a href=\"");
						//almaviva5_20070316 url rewriting per session id
						String myPath = response.encodeURL(actionPath + "?"
															+ TreeElementView.TREEVIEW_ID_PARAM + "="
															+ element.getUniqueId() + "&"
															+ TreeElementView.TREEVIEW_KEY_PARAM + "="
															+ uniqueId + "&"
															+ LinkableTagUtils.FORM_TARGET_PARAM + "="
															+ elementId);
						out.print(myPath + "\">\n");
					}
					else {
						String fieldName = TreeElementView.TREEVIEW_SUBMIT_PARAM +  "-" + element.getUniqueId();
						String link = "javascript:validateSubmit('" + fieldName + "', '" + element.getRepeatableId() + "');";

						out.append("<input type=\"hidden\" name=\""	+ fieldName + "\" value=\"" + TreeElementView.TREEVIEW_SUBMIT_NULL + "\" />");
						out.append("<a href=\"" + link + "\" >");
					}

				} // if (enabled)

				if (element.isOpened()) {
					out.print("<img src=\"" + this.imagesPath + "/LenteReticApertoBis.gif\" alt=\"-\" />");
				}
				else {
					out.print("<img src=\"" + this.imagesPath + "/LenteReticChiusoBis.gif\" alt=\"+\" />");
				}

				if (enabled)
					out.print("</a>\n");

			} else
				out.print("&nbsp;");

			out.print("</td>\n");

			out.print("<td width=\"16px\" valign=\"top\">\n");
			out.print("<label for=\"");
			out.print(tid + "_R\">\n");
			out.print("<img src=\"" + this.imagesPath + "/"
					+ element.getImageStyle() + ".gif\" alt=\""
					+ element.getUniqueId() + "\" />\n");
			out.print("</label>\n");
			out.print("</td>\n");

			if (element.getKeyDisplayMode() == KeyDisplayMode.SHOW_AS_TEXT)
				out.print("<td width=\"400px\" valign=\"top\">\n<div id=\"nodo\">");
			else
				out.print("<td width=\"1px\" valign=\"top\">\n");
			String key = "&nbsp;" + displayKey(uniqueId, element, response, actionPath, elementId) + "&nbsp;";
			if (element.isRedItem())
				out.print("<div style='color:#FF0000'>");
			if (element.getTooltip() != null && !element.getTooltip().equals("")) {
				out.print("<div title='" + element.getTooltip() + "'>");
				out.print(key);
				if (element.getKeyDisplayMode() == KeyDisplayMode.SHOW_AS_TEXT)
					out.print("</div></div></td>\n");
				else
					out.print("</div></td>\n");
			}
			else {
				out.print(key);
				if (element.getKeyDisplayMode() == KeyDisplayMode.SHOW_AS_TEXT)
					out.print("</div></td>\n");
				else
					out.print("</td>\n");
			}
			if (element.isRedItem())
				out.print("</div>\n");
			String displayedValue = ResponseUtils.filter( element.getDecoratedText() );
			if (isNull(displayedValue) )
				displayedValue = "&nbsp;";

			out.print("<td valign=\"top\">\n<div id=\"nodo\">");
			out.print("<div title='" + element.getKey() + "'>");
			out.print(displayedValue);
			out.print("</div></div></td>\n");

			if (element.isRadioVisible()) {
				out.print("<td width=\"20px\" valign=\"top\">\n");
				out.print("<input type=\"radio\" name=\"");
				out.print(getPropertyRadio());
				out.print("\" id=\"");
				out.print(tid + "_R\" value=\"");
				out.print(uniqueId);

				out.print("\"");
				try {
					String radioValue = lookupProperty(formName, this.getPropertyRadio());
					if (radioValue != null && radioValue.equals(String.valueOf(uniqueId)))
						out.print(" checked=\"checked\"");
				} catch (JspException e) {
					log.error("", e);
				}

				// Inizio Modifica per inserire il JavaScript che modifica l'impostazione del Vai A
				if (this.enableSubmit)
					out.print(" onchange=\"this.form.submit()\"");
				// Fine Modifica per inserire il JavaScript che modifica l'impostazione del Vai A
				if (!enabled)
					out.print(" disabled=\"true\"");
				out.print(" />\n");
			}

			out.print("</td>\n");
			if (!enableSelectAll)
				out.print("<td width=\"20px\" valign=\"top\">\n");

			if (element.isCheckVisible() && !element.isGroupingCheck() && this.isVisualCheck()) {
				if (enableSelectAll) {
					out.print("<td width=\"10px\" valign=\"top\">\n");
					out.print("<td width=\"15px\" valign=\"top\">\n");
				}
				out.print("<input type=\"checkbox\" name=\"");
				out.print(getPropertyCheck());
				out.print("\" id=\"");
				out.print(tid + "_C\" value=\"");
				out.print(uniqueId);
				out.print("\"");

				if (nodeIsChecked(uniqueId, formName) )
					out.print(" checked=\"checked\"");

				if (!enabled)
					out.print(" disabled=\"true\"");

				out.print(" />\n");
				if (enableSelectAll)
					out.print("<td width=\"5px\" valign=\"top\">\n");
			}

			//almaviva5_20071003 mod per almaviva7 check di gruppo
			if (element.isCheckVisible() && element.isGroupingCheck()) {

				if (enableSelectAll) {
					out.print("<td width=\"10px\" valign=\"top\">\n");
					out.print("<td width=\"15px\" valign=\"top\">\n");
					boolean checked = false;
					for (TreeElementView child : element.traverse())
						if (nodeIsChecked(child.getRepeatableId(), formName)) {
							checked = true;
							break;
						}

					String anchor = LinkableTagUtils.ANCHOR_PREFIX + uniqueId;
					out.print("<a name=\"" + anchor + "\"></a>");
					String fieldName = "";
					if (checked)
						fieldName = TreeElementView.TREEVIEW_SUBMIT_DESELECT +  "-" + element.getUniqueId();
					else
						fieldName = TreeElementView.TREEVIEW_SUBMIT_SELECT +  "-" + element.getUniqueId();
					String link = "javascript:validateSubmit('" + fieldName + "', '" + element.getRepeatableId() + "');";

					out.append("<input type=\"hidden\" name=\""	+ fieldName + "\" value=\"" + TreeElementView.TREEVIEW_SUBMIT_NULL + "\" />");
					out.append("<a href=\"" + link + "\" ");
					if (checked)
						out.append("title=\"Deseleziona tutto\">");
					else
						out.append("title=\"Seleziona tutto\">");

					out.print("<img src=\"");
					if (checked)
						out.append(this.imagesPath + "/selezNessuno.gif\" alt=\"Deseleziona tutto\" />");
					else
						out.append(this.imagesPath + "/tuttoBottom.gif\" alt=\"Seleziona tutto\" />");

					out.print("</a>");
					out.print("<td width=\"15px\" valign=\"top\">\n");
				}

				out.print("<input type=\"checkbox\" name=\"dummy_check\" id=\"");
				out.print(tid + "_C\" value=\"");
				out.print(uniqueId);
				out.print("\" disabled=\"true\"");

				// se anche uno solo dei figli è checked imposto il
				// checked del padre a true
				for (TreeElementView child : element.traverse())
					if (nodeIsChecked(child.getRepeatableId(), formName)) {
						out.print(" checked=\"checked\"");
						break;
					}

				out.print(" />\n");
				out.print("<td width=\"5px\" valign=\"top\">\n");
			}

			out.print("</td>\n");
			out.print("</tr>\n");
			out.println("</table>");

			if (element.hasChildren()) {
				for (TreeElementView child : element.getChildren()) {
					printTreeElement(child, actionPath, out, response, formName, elementId);
				}
			}
		}
	}

	private boolean nodeIsChecked(int id, String formName)
			throws JspException {
		try {
			String[] checkedNodes = this.getArrayProperty(formName, this
					.getPropertyCheck());
			if (checkedNodes != null)
				for (String checked : checkedNodes) {
					if (checked == null) continue;
					if (checked.equals(String.valueOf(id)) ) return true;
				}

		} catch (JspException e) {
			log.error("", e);
		}
		return false;
	}

	private String displayKey(int uniqueId, TreeElementView element, HttpServletResponse response, String actionPath, int elementId) {
		String key = element.getDecoratedKey();
		if (key == null)
			key = "";

		KeyDisplayMode keyDisplayMode = element.getKeyDisplayMode();

		if (!enabled && keyDisplayMode == KeyDisplayMode.SHOW_AS_LINK)
			keyDisplayMode = KeyDisplayMode.SHOW;

		switch (keyDisplayMode) {
		case SHOW:
			String filter = ResponseUtils.filter(key);
			// la key non deve andare a capo in caso di spazi interni
			String key2 = filter.replaceAll("\\s+", "&nbsp;");
			return key2;

		case SHOW_AS_LINK:
			StringBuffer buf = new StringBuffer();
			buf.append("<a class=\"" + this.linkStyleId + "\" href=\"");
			buf.append(response.encodeURL(actionPath + "?"
					+ TreeElementView.TREEVIEW_ID_PARAM + "="
					+ element.getUniqueId() + "&"
					+ TreeElementView.TREEVIEW_URL_PARAM + "="
					+ uniqueId + "&"
					+ LinkableTagUtils.FORM_TARGET_PARAM + "="
					+ elementId));
			buf.append("\">");
			// la key non deve andare a capo in caso di spazi interni
			String key3 = key.replaceAll("\\s+", "&nbsp;");
			buf.append(key3);
			buf.append("</a>");
			return buf.toString();

		case SHOW_AS_TEXT:
			filter = ResponseUtils.filter(key);
			return filter;

		case HIDE:
			return "";
		default:
			return "";
		}
	}

	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		Navigation navi = Navigation.getInstance(request);
		NavigationElement element = navi.getCache().getCurrentElement();
		String path = request.getContextPath() + element.getMapping().getPath() + ".do";
		ActionForm form = element.getForm();
		if (form == null) {
			// la form richiesta non è più presente sulla navigazione, sessione corrotta
			LinkableTagUtils.addError(request, new ActionMessage("errors.navigation.corrupted") );
			return EVAL_PAGE;
		}

		log.debug("(userid: " + navi.getUtente().getUserId() + ") - form instance type: " + form.getClass().getSimpleName() );
		String formName = element.getName();
		if (formName == null)
			formName = FORM_NAME;

		try {
			TreeElementView root = (TreeElementView)PropertyUtils.getProperty(form, this.root);

			out.print("<div class=\"");
			out.print(getDivClass());
			out.println("\">");
			out.println("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
			printTreeElement(root, path, out, response, formName, element.getUniqueId() );
			out.println("</table>");
			out.println("</div>");

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.treetag.property") );
			navi.setExceptionLog(e);
			log.error("", e);
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public boolean isVisualCheck() {
		return visualCheck;
	}

	public void setVisualCheck(boolean visualCheck) {
		this.visualCheck = visualCheck;
	}

	private String[] getArrayProperty(String beanName, String property)
			throws JspException {
		Object bean = TagUtils.getInstance().lookup(this.pageContext, beanName,	null);
		if (bean == null)
			throw new JspException(messages.getMessage("getter.bean", beanName));

		try {
			return BeanUtils.getArrayProperty(bean, property);

		} catch (IllegalAccessException e) {
			throw new JspException(messages.getMessage("getter.access",
					property, beanName));

		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			throw new JspException(messages.getMessage("getter.result",
					property, t.toString()));

		} catch (NoSuchMethodException e) {
			throw new JspException(messages.getMessage("getter.method",
					property, beanName));
		}
	}

	public String getImagesPath() {
		return imagesPath;
	}

	public void setImagesPath(String imagesPath) {
		this.imagesPath = imagesPath;
	}

	public String getLinkStyleId() {
		return linkStyleId;
	}

	public void setLinkStyleId(String linkStyleId) {
		this.linkStyleId = linkStyleId;
	}

	public boolean isEnableSubmit() {
		return enableSubmit;
	}

	public void setEnableSubmit(boolean enableSubmit) {
		this.enableSubmit = enableSubmit;
	}

	public boolean isEnableNodeSubmit() {
		return enableNodeSubmit;
	}

	public void setEnableNodeSubmit(boolean enableNodeSubmit) {
		this.enableNodeSubmit = enableNodeSubmit;
	}

	public boolean isEnableSelectAll() {
		return enableSelectAll;
	}

	public void setEnableSelectAll(boolean enableSelectAll) {
		this.enableSelectAll = enableSelectAll;
	}
}
