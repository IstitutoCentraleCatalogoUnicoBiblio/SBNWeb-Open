<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<div id="divForm"><sbn:navform action="/statistiche/sinteticaStatistiche.do">
		<div id="divMessaggio"><sbn:errors /></div>
		<br>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width=800 align="left" style="font-weight: bold; font-size: 15px">
				Area&nbsp;<bean-struts:write
					name="sinteticaStatisticheForm" property="descrArea" /></td>
			</tr></table>
			<br>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="30">
				<c:choose>
					<c:when test="${sinteticaStatisticheForm.folder eq 'tab1'}">
						<td width="50%" class="schedaOn">
						<div align="center">Lista Statistiche</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="50%" class="schedaOff">
						<div align="center"><html:submit property="methodSinStatistiche"
							styleClass="sintButtonLinkDefault">
							<bean:message key="button.tab1" bundle="statisticheLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${sinteticaStatisticheForm.folder eq 'tab2'}">
						<td width="50%" class="schedaOn">
						<div align="center">Impostazioni File Excel dei risultati</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="50%" class="schedaOff">
						<div align="center"><html:submit property="methodSinStatistiche"
							styleClass="sintButtonLinkDefault">
							<bean:message key="button.tab2" bundle="statisticheLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${sinteticaStatisticheForm.folder eq 'tab1'}">
						<jsp:include page="/WEB-INF/jsp/subpages/statistiche/sintStatTab1.jsp" />
					</c:when>
					<c:when test="${sinteticaStatisticheForm.folder eq 'tab2'}">
						<jsp:include page="/WEB-INF/jsp/subpages/statistiche/sintStatTab2.jsp" />
					</c:when>
				</c:choose>
			</tr>
		</table>
	</sbn:navform></div>
</layout:page>