<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sbn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
<html:xhtml />
<layout:page>
<sbn:errors bundle="stampeLabels" />

<html:form action="/listaOutputBatch.do">
Risultati output batch biblioteca:<br />
<c:if test="${not empty richiestaBatchForm.listaOutputBatch}">
<div id="content">
<display:table class="simple" width="100%" border="1"
		requestURI="/listaOutputBatch.do"
		name="sessionScope.richiestaBatchForm.listaOutputBatch"
		sort="list" pagesize="15" id="row">
		<display:column title=" " >
			<html:radio name ="richiestaBatchForm" property="fileSelezionato" value="${row.id}" />
		</display:column>
		<display:column title="fileName" property="fileName" />
		<display:column property="rowCreatedTime" title="Data" />
		<display:column property="codBib" title="Codice biblioteca" />
		<display:column property="msgCodaJms" title="messaggioId" />

</display:table>
</div>
<div id="content">
	Esporta il file in Formato
	<html:select property="tipoFileOut">
		<html:option value="application/pdf">PDF</html:option>
		<html:option value="application/rtf">RTF</html:option>
		<html:option value="text/html">HTML</html:option>
		<html:option value="application/vnd.ms-excel">XLS</html:option>
		<html:option value="csv">CSV</html:option>
	</html:select>&nbsp;

<html:submit styleClass="submit" value="Download" property="downloadBtn" title="Download" />
</div>
</c:if>


</html:form>
</layout:page>
