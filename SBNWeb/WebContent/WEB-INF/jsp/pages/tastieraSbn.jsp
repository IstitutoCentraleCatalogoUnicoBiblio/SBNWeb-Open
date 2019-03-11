<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<layout:page>
	<sbn:navform action="/TastieraSbn">
		<div id="divForm">
			<div id="divMessaggio"><sbn:errors /></div>
			<br>
			<html:textarea property="inputField" styleClass="expandedLabel"
				tabindex="1" />
			<br>
			<table width="100%" border="0" class="SchedaImg1">
				<tr>
					<logic:iterate id="category" property="categories"
						name="tastieraSbnForm">
						<c:choose>

							<c:when test="${tastieraSbnForm.folder eq category.key}">
								<td class="schedaOn" align="center">
									<bean-struts:write name="category" property="key" />
								</td>
							</c:when>

							<c:otherwise>
								<td class="schedaOff" align="center">
									<html:submit property="methodTastieraSbn"
										styleClass="sintButtonLinkDefault">
										<bean-struts:write name="category" property="key" />
									</html:submit>
								</td>
							</c:otherwise>

						</c:choose>
					</logic:iterate>
				</tr>
			</table>
			<sbn:tastieraButtons inputField="inputField" keys="currentKeyList"
				name="tastieraSbnForm" />
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td>
						<html:submit property="methodTastieraSbn">
							<bean:message key="button.ok" />
						</html:submit>
						<html:submit property="methodTastieraSbn">
							<bean:message key="button.annulla" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
