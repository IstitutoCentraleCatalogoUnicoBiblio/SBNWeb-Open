<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<table width="100%" border="0" class="SchedaImg1">
	<tr>
		<c:choose>
			<c:when
				test="${periodiciForm.folder eq 'CON_FASCICOLI'}">
				<td width="50%" class="schedaOn" align="center"><bean:message
					key="folder.periodici.con.esemplari" bundle="periodiciLabels" /></td>
			</c:when>
			<c:otherwise>
				<td width="50%" class="schedaOff" align="center"><html:submit
					property="methodPeriodici" styleClass="sintButtonLinkDefault">
					<bean:message key="folder.periodici.con.esemplari"
						bundle="periodiciLabels" />
				</html:submit></td>
			</c:otherwise>
		</c:choose>

		<c:choose>
			<c:when
				test="${periodiciForm.folder eq 'SENZA_FASCICOLI'}">
				<td width="50" class="schedaOn" align="center"><bean:message
					key="folder.periodici.senza.esemplari" bundle="periodiciLabels" /></td>
			</c:when>
			<c:otherwise>
				<td width="50" class="schedaOff" align="center"><html:submit
					property="methodPeriodici" styleClass="sintButtonLinkDefault">
					<bean:message key="folder.periodici.senza.esemplari"
						bundle="periodiciLabels" />
				</html:submit></td>
			</c:otherwise>
		</c:choose>

	</tr>
</table>
