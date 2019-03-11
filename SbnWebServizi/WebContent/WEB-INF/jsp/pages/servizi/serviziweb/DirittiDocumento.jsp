<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
<div id="header"></div>
<div id="data">
	<sbn:navform action="/serviziweb/dirittiDocumento.do" >

	<table cellspacing="0" width="100%" border="0">
			<tr>
				<th colspan="4" align="right">
				<bean:message key="servizi.documento.serviziDisponibiliDocumentoTitolo"
					bundle="serviziWebLabels" />
				</th>
			</tr>
			<tr>
				<td colspan="4" align="center">
				<b><c:out value="${dirittiDocumentoForm.titolo}"> </c:out></b>
				</td>
			</tr>

	</table>


	<table cellspacing="0" width="100%" border="0">

			<tr>
				<td >
					&nbsp;
				</td>
			</tr>
			<tr>
			<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
					<b><bean:message key="servizi.documento.serviziDisponibiliDocumento"
					bundle="serviziWebLabels" /></b>
				</th>


			</tr>



		</table>
	</sbn:navform>
</div>
</layout:page>