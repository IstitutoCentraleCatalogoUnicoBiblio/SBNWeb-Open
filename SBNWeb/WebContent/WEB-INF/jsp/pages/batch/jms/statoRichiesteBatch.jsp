<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sbn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
<html:xhtml />
<layout:page>
<sbn:errors bundle="stampeLabels" />
<html:form action="/listaOutputBatch.do">

	Stato delle richieste batch biblioteca :<strong>${richiestaBatchForm.codBib} </strong><br />
	<c:if test="${not empty richiestaBatchForm.statoRichiesteBatch}">
	<div id="layout">
		<display:table class="layout"  border="1"
				requestURI="/statoRichieste.do"
				name="requestScope.richiestaBatchForm.statoRichiesteBatch"
				sort="list" pagesize="15" id="row">
				<display:column property="jobName" title="jobName" />
				<display:column property="msgCodaJms" title="Richiesta N°" />
				<display:column property="dataRic" title="Data" />
				<display:column property="queueName" title="Coda" />
				<display:column property="priorita" title="Priorità" />
				<display:column property="stato" title="Stato" />

		</display:table>
	</div>
	<div id="footer">
		<html:submit styleClass="submit" value="Seleziona" property="selezionaBtn" title="Seleziona" />
	</div>
	</c:if>
</html:form>
</layout:page>
