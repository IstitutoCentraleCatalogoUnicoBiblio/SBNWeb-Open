<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="stampeLabels" />
	<html:form action="/codaJms.do">
		<div id="content">
		<table width="100%" border="0">
			<tr>
				<td>
					<label for="codBib">
					<bean:message key="code.codice.biblioteca" bundle="stampeLabels" />
					</label>
				</td>
				<td>
					<html:text styleId="codBib" property="codBib" size="5" maxlength="4" value="011"></html:text>
				</td>
			</tr>

			<tr>
				<td><label for="codFunz">
				<bean:message key="code.codice.funzione" bundle="stampeLabels" />
				</label></td>
				<td><html:text styleId="codFunz" property="codFunz" size="5"
					maxlength="4"></html:text></td>
			</tr>
			<tr>
				<td><label for="jobName">
				<bean:message key="code.job.name" bundle="stampeLabels" /></label></td>
				<td><html:text styleId="jobName" property="jobName" size="20"
					maxlength="16"></html:text></td>
			</tr>
			<tr>
				<td><label for="fileOut"><bean:message key="code.nome.fileout" bundle="stampeLabels" /></label></td>
				<td>
					<html:text styleId="fileOut" property="fileOut" size="40" maxlength="31" />
				</td>
			</tr>
			<tr>
				<td><label for="flagPriorita"><bean:message key="code.priorita" bundle="stampeLabels" />
				</label></td>
				<td><html:select property="priorita">
					<html:option value="0">Bassa</html:option>
					<html:option value="1">Media</html:option>
					<html:option value="2">Alta</html:option>
					</html:select>
				</td>
			</tr>
			</table>
			</div>
			<div id="footer">
			<table width="100%" border="0">
				<tr>
					<td colspan="2">
					<html:submit styleClass="submit" property="sendMessageBtn">
						<bean:message key="code.button.invia" bundle="stampeLabels" />
					</html:submit>
					<html:submit styleClass="submit" value="Invia CODA asincrona" property="sendMessageAsincronoBtn" title="Invia CODA asincrona" />
					<html:submit styleClass="submit" value="Visualizza risultati" property="btnListaOutput" title="Visualizza risultati" />
					</td>
				</tr>
			</table>
			</div>
	</html:form>
</layout:page>
