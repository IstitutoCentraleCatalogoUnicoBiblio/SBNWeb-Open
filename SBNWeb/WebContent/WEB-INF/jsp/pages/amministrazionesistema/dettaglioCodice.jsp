<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<div id="divForm"><sbn:navform
		action="/amministrazionesistema/dettaglioCodice.do">
		<div id="divMessaggio"><sbn:errors /></div>

		<table width="100%" border="0">
			<tr>
				<td>
					<b><bean:message key="dettaglio.codici.tabella.titolo" bundle="amministrazioneSistemaLabels" />:&nbsp;</b>
					<c:out value="${navForm.configCodici.descrizione}" />
					&nbsp;(<c:out value="${navForm.configCodici.cdTabella}" />)
				</td>
			</tr>
		</table>
		<br />
		<sbn:blocchi numBlocco="dettaglioBloccoCorrente" numNotizie="dettaglioTotRighe"
			parameter="methodSinCodici" totBlocchi="dettaglioTotBlocchi"
			elementiPerBlocco="dettaglioMaxRighe" />
		<logic:notEmpty name="navForm" property="dettaglioElencoCodici">
			<c:choose>
				<c:when
					test="${navForm.configCodici.tipoTabella eq 'DICT'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/amministrazionesistema/codici/dettaglioCodiceDICT.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/amministrazionesistema/codici/dettaglioCodiceCROSS.jsp" />
				</c:otherwise>
			</c:choose>

			<table border="0" align="center" width="100%">
				<tr>
					<td align="left"
						style="font-size: 80%; font-style: oblique; color: red;"><bean:message
						key="dettaglio.codici.messaggio"
						bundle="amministrazioneSistemaLabels" /></td>
				</tr>
			</table>
		</logic:notEmpty>

		<div id="divFooter">
		<table border="0" style="height: 40px" align="center">
			<tr>
				<td><sbn:bottoniera buttons="pulsanti" /></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
	</div>
</layout:page>