<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<div style="width:100%;" class="SchedaImg1">
	<div style="width:33%; float:left;">
		<c:choose>
			<c:when test="${navForm.folder eq 'LETTERA'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.configurazione.sollecito.lettera" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodModSoll" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.configurazione.sollecito.lettera" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:33%; float:left;">
		<c:choose>
			<c:when test="${navForm.folder eq 'EMAIL'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.utenti.email" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodModSoll" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.utenti.email" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div style="width:34%; float:left;">
		<c:choose>
			<c:when test="${navForm.folder eq 'SMS'}">
				<div class="schedaOn" style="text-align: center;">
					<bean:message key="servizi.configurazione.sollecito.sms" bundle="serviziLabels" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="schedaOff" align="center">
					<html:submit style="margin-left:auto; margin-right:auto;" property="methodModSoll" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
						<bean:message key="servizi.configurazione.sollecito.sms" bundle="serviziLabels" />
					</html:submit>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
