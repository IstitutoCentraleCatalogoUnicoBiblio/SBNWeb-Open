<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
<div>
		<div id="divForm">
<sbn:navform action="/amministrazionesistema/abilitazionePolo/profilazionePolo.do">
			<div id="divMessaggio">
					<sbn:errors bundle="amministrazioneSistemaMessages" />
			</div>
<!--html:form action="/amministrazionesistema/abilitazionePolo/profilazionePolo.do"-->
<table border="0">
	<tr>
		<td align="left" style="font-weight: bold; font-size: 15px">
			<bean:message key="profilo.polo.titolo" bundle="amministrazioneSistemaLabels"/>:
		</td>
	</tr>
	<tr>
		<td align="left">
			<b><bean:message key="profilo.polo.codice" bundle="amministrazioneSistemaLabels"/>: </b>
			<c:out value="${profilazionePoloForm.codicePolo}"/>
		</td>
	</tr>
</table>

<br/>

    <c:choose>
	<c:when test="${profilazionePoloForm.conferma eq 'TRUE'}">
		<sbn:tree root="profilazioneTreeElementView"
			divClass="analitica"
			propertyCheck="checkItemSelez"
			propertyRadio="radioItemSelez"
			imagesPath="/sbn/images/tree"
			enableNodeSubmit="true"
			enabled="false"
			enableSelectAll="true">
			</sbn:tree>

		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<td width="100%" align="center">
						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.button.salva.ok" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.button.salva.no" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<sbn:tree root="profilazioneTreeElementView"
			divClass="analitica"
			propertyCheck="checkItemSelez"
			propertyRadio="radioItemSelez"
			imagesPath="/sbn/images/tree"
			enableNodeSubmit="true"
			enabled="true"
			enableSelectAll="true">
			</sbn:tree>
		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<td width="100%" align="center">
						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.salva" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.button.auth" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.button.mat" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.reset" bundle="amministrazioneSistemaLabels" />
						</html:submit>

					<!--/td-->
					<!--td width="50"/-->
					<!--td align="right"-->
						<html:submit styleClass="buttonSelezTutti" property="methodProfilazione" title="Seleziona tutto">
							<bean:message key="profilo.polo.selezionaTutti" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="buttonSelezNessuno" property="methodProfilazione" title="Deseleziona tutto">
							<bean:message key="profilo.polo.deSelezionaTutti" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</c:otherwise>
	</c:choose>

	</sbn:navform>
    <!--/html:form-->
  </layout:page>