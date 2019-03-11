<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean-el" %>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html-el" %>
<%@ taglib uri="http://struts.apache.org/tags-logic-el" prefix="logic-el" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="page1" %>
<page1:page>
<h1>ESAMINA</h1>
<logic-el:present name="showCodeModel" parameter="detailsMessage">
<table>
	<tr>
		<td>ID:</td>
		<td><bean:write name="showCodeModel" property="detailsMessage.JMSMessageID"/></td>
	</tr>
	<tr>
		<td>Blocco</td>
		<td><bean:write name="showCodeModel" property="detailsMessage.JMSCorrelationID"/></td>
	</tr>
	<tr>
		<td>Priorita</td>
		<td><bean:write name="showCodeModel" property="detailsMessage.JMSPriority"/></td>
	</tr>
	<tr>
		<td>Timestamp</td>
		<td><bean:write name="showCodeModel" property="detailsMessage.JMSTimestamp"/></td>
	</tr>
	<tr>
		<td>Tipo</td>
		<td><bean:write name="showCodeModel" property="detailsMessage.JMSType"/></td>
	</tr>
</table>
<br/>
<table>
	<tr>
		<td>Testo</td>
	</tr>
	<tr>
		<td><bean:write name="showCodeModel" property="detailsMessage.text"/></td>
	</tr>
</table>
</logic-el:present>
<logic-el:notPresent name="showCodeModel" parameter="detailsMessage">
<h2>Oggetto rimosso o prelevato
</logic-el:notPresent>
<br/>
<html:link action="/jms/showCode?methodShow=indietro">indietro</html:link>
<html:link action="/jms/showCode?methodShow=elimina">elimina</html:link>

</page1:page>

