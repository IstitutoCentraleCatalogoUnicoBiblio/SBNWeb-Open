<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<div id="divForm">
	<sbn:navform action="/amministrazionesistema/gestioneCodice.do">
		<div id="divMessaggio"><sbn:errors /></div>

		<table width="100%" border="0">
			<tr>
				<td><b><bean:message key="dettaglio.codici.tabella.titolo"
					bundle="amministrazioneSistemaLabels" />:&nbsp;</b> <c:out
					value="${navForm.configCodici.descrizione}"></c:out>
					&nbsp;(<c:out value="${navForm.configCodici.cdTabella}" />)
				</td>
			</tr>
		</table>
		<br />

		<c:choose>
			<c:when test="${dettaglioCodiceForm.configCodici.tipoTabella eq 'DICT'}">
				<jsp:include page="/WEB-INF/jsp/subpages/amministrazionesistema/codici/gestioneCodiceDICT.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/jsp/subpages/amministrazionesistema/codici/gestioneCodiceCROSS.jsp" />
			</c:otherwise>
		</c:choose>

		<br />

		<div id="divFooter">
		<table border="0" align="center">
			<tr>
				<td width="100%" align="center"><html:submit
					styleClass="pulsanti" property="methodGestCodice">
					<bean:message key="dettaglio.codici.button.ok"
						bundle="amministrazioneSistemaLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodGestCodice">
					<bean:message key="dettaglio.codici.button.indietro"
						bundle="amministrazioneSistemaLabels" />
				</html:submit>
				</td>
			</tr>
		</table>
		</div>

	</sbn:navform>
	</div>
</layout:page>