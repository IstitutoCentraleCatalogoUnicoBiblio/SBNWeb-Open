<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<div style="width:100%;" class="SchedaImg1">

	<div style="width:18%; float:left;">
		<c:choose>
			<c:when test="${navForm.folder eq 'RICHIEDENTE'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.erogazione.ill.ricerca.folder.requester" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodRicercaILL" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.erogazione.ill.ricerca.folder.requester" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:18%; float:left;">
		<c:choose>
			<c:when test="${navForm.folder eq 'FORNITRICE'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.erogazione.ill.ricerca.folder.responder" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodRicercaILL" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.erogazione.ill.ricerca.folder.responder" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

</div>

